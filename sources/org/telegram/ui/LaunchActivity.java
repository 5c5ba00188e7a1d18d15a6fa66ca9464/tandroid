package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
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
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.arch.core.util.Function;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.api.Status;
import com.google.common.primitives.Longs;
import j$.util.function.Consumer;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SharedPrefsHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.voip.VideoCapturerDevice;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_chatlists;
import org.telegram.tgnet.tl.TL_stars;
import org.telegram.tgnet.tl.TL_stories;
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
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SearchTagsList;
import org.telegram.ui.Components.SideMenultItemAnimator;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickerSetBulletinLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.TermsOfServiceView;
import org.telegram.ui.Components.ThemeEditorView;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.Components.spoilers.SpoilerEffect2;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.LauncherIconController;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.PaymentFormActivity;
import org.telegram.ui.SelectAnimatedEmojiDialog;
import org.telegram.ui.Stars.ISuperRipple;
import org.telegram.ui.Stars.StarsController;
import org.telegram.ui.Stars.SuperRipple;
import org.telegram.ui.Stars.SuperRippleFallback;
import org.telegram.ui.Stories.StoriesController;
import org.telegram.ui.Stories.StoriesListPlaceProvider;
import org.telegram.ui.Stories.StoryViewer;
import org.telegram.ui.Stories.recorder.StoryRecorder;
import org.telegram.ui.WallpapersListActivity;
import org.telegram.ui.bots.BotWebViewAttachedSheet;
import org.telegram.ui.bots.BotWebViewSheet;
import org.telegram.ui.bots.WebViewRequestProps;
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
    public static Runnable whenResumed;
    private ActionBarLayout actionBarLayout;
    private long alreadyShownFreeDiscSpaceAlertForced;
    private SizeNotifierFrameLayout backgroundTablet;
    private BlockingUpdateView blockingUpdateView;
    private BottomSheetTabsOverlay bottomSheetTabsOverlay;
    private boolean checkFreeDiscSpaceShown;
    private ArrayList contactsToSend;
    private Uri contactsToSendUri;
    private int currentConnectionState;
    private ISuperRipple currentRipple;
    private View customNavigationBar;
    private String documentsMimeType;
    private ArrayList documentsOriginalPathsArray;
    private ArrayList documentsPathsArray;
    private ArrayList documentsUrisArray;
    private DrawerLayoutAdapter drawerLayoutAdapter;
    public DrawerLayoutContainer drawerLayoutContainer;
    private HashMap englishLocaleStrings;
    private Uri exportingChatUri;
    View feedbackView;
    private boolean finished;
    private FireworksOverlay fireworksOverlay;
    private FlagSecureReason flagSecureReason;
    public FrameLayout frameLayout;
    private ArrayList importingStickers;
    private ArrayList importingStickersEmoji;
    private String importingStickersSoftware;
    private SideMenultItemAnimator itemAnimator;
    private RelativeLayout launchLayout;
    private ActionBarLayout layersActionBarLayout;
    private boolean loadingLocaleDialog;
    private TLRPC.TL_theme loadingTheme;
    private boolean loadingThemeAccent;
    private String loadingThemeFileName;
    private Theme.ThemeInfo loadingThemeInfo;
    private AlertDialog loadingThemeProgressDialog;
    private TLRPC.TL_wallPaper loadingThemeWallpaper;
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
    private ArrayList photoPathsArray;
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
    private HashMap systemLocaleStrings;
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
    private Utilities.Callback webviewShareAPIDoneListener;
    public static final Pattern PREFIX_T_ME_PATTERN = Pattern.compile("^(?:http(?:s|)://|)([A-z0-9-]+?)\\.t\\.me");
    private static final ArrayList mainFragmentsStack = new ArrayList();
    private static final ArrayList layerFragmentsStack = new ArrayList();
    private static final ArrayList rightFragmentsStack = new ArrayList();
    public ArrayList sheetFragmentsStack = new ArrayList();
    private List overlayPasscodeViews = new ArrayList();
    public final ArrayList visibleDialogs = new ArrayList();
    private boolean isNavigationBarColorFrozen = false;
    private List onUserLeaveHintListeners = new ArrayList();
    private SparseIntArray requestedPermissions = new SparseIntArray();
    private int requsetPermissionsPointer = 5934;
    private Consumer blurListener = new Consumer() { // from class: org.telegram.ui.LaunchActivity.1
        1() {
        }

        @Override // j$.util.function.Consumer
        /* renamed from: accept */
        public void r(Boolean bool) {
            LaunchActivity.systemBlurEnabled = bool.booleanValue();
        }

        @Override // j$.util.function.Consumer
        public /* synthetic */ Consumer andThen(Consumer consumer) {
            return Consumer.-CC.$default$andThen(this, consumer);
        }
    };

    /* loaded from: classes4.dex */
    class 1 implements Consumer {
        1() {
        }

        @Override // j$.util.function.Consumer
        /* renamed from: accept */
        public void r(Boolean bool) {
            LaunchActivity.systemBlurEnabled = bool.booleanValue();
        }

        @Override // j$.util.function.Consumer
        public /* synthetic */ Consumer andThen(Consumer consumer) {
            return Consumer.-CC.$default$andThen(this, consumer);
        }
    }

    /* loaded from: classes4.dex */
    public class 10 extends RelativeLayout {
        private boolean inLayout;
        private Path path = new Path();

        10(Context context) {
            super(context);
            this.path = new Path();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (LaunchActivity.this.layersActionBarLayout != null) {
                LaunchActivity.this.layersActionBarLayout.parentDraw(this, canvas);
            }
            super.dispatchDraw(canvas);
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

        @Override // android.widget.RelativeLayout, android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.inLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    /* loaded from: classes4.dex */
    public class 11 extends SizeNotifierFrameLayout {
        11(Context context) {
            super(context);
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
        protected boolean isActionBarVisible() {
            return false;
        }
    }

    /* loaded from: classes4.dex */
    public class 12 extends SelectAnimatedEmojiDialog {
        final /* synthetic */ SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] val$popup;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        12(BaseFragment baseFragment, Context context, boolean z, Integer num, int i, Theme.ResourcesProvider resourcesProvider, SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr) {
            super(baseFragment, context, z, num, i, resourcesProvider);
            r15 = selectAnimatedEmojiDialogWindowArr;
        }

        @Override // org.telegram.ui.SelectAnimatedEmojiDialog
        protected void onEmojiSelected(View view, Long l, TLRPC.Document document, Integer num) {
            TLRPC.EmojiStatus emojiStatus;
            if (l == null) {
                emojiStatus = new TLRPC.TL_emojiStatusEmpty();
            } else if (num != null) {
                TLRPC.TL_emojiStatusUntil tL_emojiStatusUntil = new TLRPC.TL_emojiStatusUntil();
                tL_emojiStatusUntil.document_id = l.longValue();
                tL_emojiStatusUntil.until = num.intValue();
                emojiStatus = tL_emojiStatusUntil;
            } else {
                TLRPC.TL_emojiStatus tL_emojiStatus = new TLRPC.TL_emojiStatus();
                tL_emojiStatus.document_id = l.longValue();
                emojiStatus = tL_emojiStatus;
            }
            MessagesController.getInstance(LaunchActivity.this.currentAccount).updateEmojiStatus(emojiStatus);
            TLRPC.User currentUser = UserConfig.getInstance(LaunchActivity.this.currentAccount).getCurrentUser();
            if (currentUser != null) {
                for (int i = 0; i < LaunchActivity.this.sideMenu.getChildCount(); i++) {
                    View childAt = LaunchActivity.this.sideMenu.getChildAt(i);
                    if (childAt instanceof DrawerUserCell) {
                        DrawerUserCell drawerUserCell = (DrawerUserCell) childAt;
                        drawerUserCell.setAccount(drawerUserCell.getAccountNumber());
                    } else if (childAt instanceof DrawerProfileCell) {
                        if (l != null) {
                            ((DrawerProfileCell) childAt).animateStateChange(l.longValue());
                        }
                        ((DrawerProfileCell) childAt).setUser(currentUser, LaunchActivity.this.drawerLayoutAdapter.isAccountsShown());
                    } else if ((childAt instanceof DrawerActionCell) && LaunchActivity.this.drawerLayoutAdapter.getId(LaunchActivity.this.sideMenu.getChildAdapterPosition(childAt)) == 15) {
                        boolean z = DialogObject.getEmojiStatusDocumentId(currentUser.emoji_status) != 0;
                        ((DrawerActionCell) childAt).updateTextAndIcon(LaunchActivity.this.getString(z ? R.string.ChangeEmojiStatus : R.string.SetEmojiStatus), z ? R.drawable.msg_status_edit : R.drawable.msg_status_set);
                    }
                }
            }
            if (r15[0] != null) {
                LaunchActivity.this.selectAnimatedEmojiDialog = null;
                r15[0].dismiss();
            }
        }

        @Override // org.telegram.ui.SelectAnimatedEmojiDialog
        public void onSettings() {
            DrawerLayoutContainer drawerLayoutContainer = LaunchActivity.this.drawerLayoutContainer;
            if (drawerLayoutContainer != null) {
                drawerLayoutContainer.closeDrawer();
            }
        }
    }

    /* loaded from: classes4.dex */
    public class 13 extends SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow {
        13(View view, int i, int i2) {
            super(view, i, i2);
        }

        @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
        public void dismiss() {
            super.dismiss();
            LaunchActivity.this.selectAnimatedEmojiDialog = null;
        }
    }

    /* loaded from: classes4.dex */
    public class 14 extends BlockingUpdateView {
        14(Context context) {
            super(context);
        }

        @Override // org.telegram.ui.Components.BlockingUpdateView, android.view.View
        public void setVisibility(int i) {
            super.setVisibility(i);
            if (i == 8) {
                LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            }
        }
    }

    /* loaded from: classes4.dex */
    public class 15 implements TermsOfServiceView.TermsOfServiceViewDelegate {
        15() {
        }

        public /* synthetic */ void lambda$onAcceptTerms$0() {
            LaunchActivity.this.termsOfServiceView.setVisibility(8);
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
    }

    /* loaded from: classes4.dex */
    public class 16 extends DialogsActivity {
        16(Bundle bundle) {
            super(bundle);
        }

        @Override // org.telegram.ui.DialogsActivity
        public boolean shouldShowNextButton(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
            if (LaunchActivity.this.exportingChatUri != null) {
                return false;
            }
            if (LaunchActivity.this.contactsToSend != null && LaunchActivity.this.contactsToSend.size() == 1 && !LaunchActivity.mainFragmentsStack.isEmpty()) {
                return true;
            }
            if (arrayList.size() <= 1) {
                return LaunchActivity.this.videoPath != null || (LaunchActivity.this.photoPathsArray != null && LaunchActivity.this.photoPathsArray.size() > 0);
            }
            return false;
        }
    }

    /* loaded from: classes4.dex */
    public class 17 implements ChatRightsEditActivity.ChatRightsEditActivityDelegate {
        final /* synthetic */ DialogsActivity val$fragment;
        final /* synthetic */ int val$intentAccount;

        17(DialogsActivity dialogsActivity, int i) {
            r2 = dialogsActivity;
            r3 = i;
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didChangeOwner(TLRPC.User user) {
        }

        @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
        public void didSetRights(int i, TLRPC.TL_chatAdminRights tL_chatAdminRights, TLRPC.TL_chatBannedRights tL_chatBannedRights, String str) {
            r2.removeSelfFromStack();
            NotificationCenter.getInstance(r3).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
        }
    }

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

        public /* synthetic */ void lambda$onMessagesLoaded$0(AccountInstance accountInstance, long j, BaseFragment baseFragment) {
            long j2 = -j;
            ChatObject.Call groupCall = accountInstance.getMessagesController().getGroupCall(j2, false);
            VoIPHelper.startCall(accountInstance.getMessagesController().getChat(Long.valueOf(j2)), accountInstance.getMessagesController().getInputPeer(j), null, false, Boolean.valueOf(groupCall == null || !groupCall.call.rtmp_stream), LaunchActivity.this, baseFragment, accountInstance);
        }

        public /* synthetic */ void lambda$onMessagesLoaded$1(final AccountInstance accountInstance, final long j, final BaseFragment baseFragment) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$18$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.18.this.lambda$onMessagesLoaded$0(accountInstance, j, baseFragment);
                }
            });
        }

        public /* synthetic */ void lambda$onMessagesLoaded$2(String str, final long j, final BaseFragment baseFragment) {
            if (str != null) {
                final AccountInstance accountInstance = AccountInstance.getInstance(LaunchActivity.this.currentAccount);
                long j2 = -j;
                if (accountInstance.getMessagesController().getGroupCall(j2, false) != null) {
                    VoIPHelper.startCall(accountInstance.getMessagesController().getChat(Long.valueOf(j2)), accountInstance.getMessagesController().getInputPeer(j), null, false, Boolean.valueOf(!r0.call.rtmp_stream), LaunchActivity.this, baseFragment, accountInstance);
                    return;
                }
                TLRPC.ChatFull chatFull = accountInstance.getMessagesController().getChatFull(j2);
                if (chatFull != null) {
                    if (chatFull.call != null) {
                        accountInstance.getMessagesController().getGroupCall(j2, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$18$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                LaunchActivity.18.this.lambda$onMessagesLoaded$1(accountInstance, j, baseFragment);
                            }
                        });
                    } else if (baseFragment.getParentActivity() != null) {
                        BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.linkbroken, LocaleController.getString(R.string.InviteExpired)).show();
                    }
                }
            }
        }

        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        public void onError() {
            if (!LaunchActivity.this.isFinishing()) {
                AlertsCreator.showSimpleAlert((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1), LocaleController.getString(R.string.JoinToGroupErrorNotExist));
            }
            try {
                this.val$dismissLoading.run();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0026, code lost:
        
            if (((org.telegram.ui.ChatActivity) r8).getDialogId() == r7.val$dialog_id) goto L66;
         */
        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
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
                if (baseFragment instanceof ChatActivity) {
                }
            }
            BaseFragment baseFragment2 = this.val$lastFragment;
            if (!(baseFragment2 instanceof ChatActivity) || ((ChatActivity) baseFragment2).getDialogId() != this.val$dialog_id || this.val$messageId != null) {
                chatActivity = new ChatActivity(this.val$args);
                LaunchActivity.this.getActionBarLayout().presentFragment(chatActivity);
                final BaseFragment baseFragment3 = chatActivity;
                final String str = this.val$livestream;
                final long j = this.val$dialog_id;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$18$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.18.this.lambda$onMessagesLoaded$2(str, j, baseFragment3);
                    }
                }, 150L);
            }
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
            final BaseFragment baseFragment32 = chatActivity;
            final String str2 = this.val$livestream;
            final long j2 = this.val$dialog_id;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$18$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.18.this.lambda$onMessagesLoaded$2(str2, j2, baseFragment32);
                }
            }, 150L);
        }
    }

    /* loaded from: classes4.dex */
    public class 19 implements MessagesController.MessagesLoadedCallback {
        final /* synthetic */ Bundle val$args;
        final /* synthetic */ boolean[] val$canceled;
        final /* synthetic */ Runnable val$dismissLoading;
        final /* synthetic */ TLRPC.ChatInvite val$invite;

        19(Runnable runnable, boolean[] zArr, Bundle bundle, TLRPC.ChatInvite chatInvite) {
            r2 = runnable;
            r3 = zArr;
            r4 = bundle;
            r5 = chatInvite;
        }

        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        public void onError() {
            if (!LaunchActivity.this.isFinishing()) {
                AlertsCreator.showSimpleAlert((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1), LocaleController.getString(R.string.JoinToGroupErrorNotExist));
            }
            try {
                r2.run();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
        public void onMessagesLoaded(boolean z) {
            try {
                r2.run();
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (r3[0]) {
                return;
            }
            ChatActivity chatActivity = new ChatActivity(r4);
            TLRPC.ChatInvite chatInvite = r5;
            if (chatInvite instanceof TLRPC.TL_chatInvitePeek) {
                chatActivity.setChatInvite(chatInvite);
            }
            LaunchActivity.this.getActionBarLayout().presentFragment(chatActivity);
        }
    }

    /* loaded from: classes4.dex */
    class 2 extends FrameLayout {
        2(Context context) {
            super(context);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            LaunchActivity.this.drawRippleAbove(canvas, this);
        }
    }

    /* loaded from: classes4.dex */
    class 20 extends ClickableSpan {
        20() {
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(View view) {
            LaunchActivity.this.getActionBarLayout().presentFragment(new PremiumPreviewFragment("gift"));
        }

        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }
    }

    /* loaded from: classes4.dex */
    class 21 extends AnimatorListenerAdapter {
        final /* synthetic */ RLottieImageView val$darkThemeView;
        final /* synthetic */ boolean val$toDark;

        21(boolean z, RLottieImageView rLottieImageView) {
            r2 = z;
            r3 = rLottieImageView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            LaunchActivity.this.rippleAbove = null;
            LaunchActivity.this.drawerLayoutContainer.invalidate();
            LaunchActivity.this.themeSwitchImageView.invalidate();
            LaunchActivity.this.themeSwitchImageView.setImageDrawable(null);
            LaunchActivity.this.themeSwitchImageView.setVisibility(8);
            LaunchActivity.this.themeSwitchSunView.setVisibility(8);
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.themeAccentListUpdated, new Object[0]);
            if (!r2) {
                r3.setVisibility(0);
            }
            DrawerProfileCell.switchingTheme = false;
        }
    }

    /* loaded from: classes4.dex */
    public class 22 implements Runnable {
        22() {
        }

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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 23 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$toColor;

        23(int i) {
            r2 = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            LaunchActivity.this.setNavigationBarColor(r2, false);
        }
    }

    /* loaded from: classes4.dex */
    public class 3 extends DrawerLayoutContainer {
        private boolean wasPortrait;

        3(Context context) {
            super(context);
        }

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

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.ActionBar.DrawerLayoutContainer, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            if (LaunchActivity.this.actionBarLayout.getParent() == this) {
                LaunchActivity.this.actionBarLayout.parentDraw(this, canvas);
            }
            super.dispatchDraw(canvas);
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
    }

    /* loaded from: classes4.dex */
    class 4 extends View {
        4(Context context) {
            super(context);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (LaunchActivity.this.themeSwitchSunDrawable != null) {
                LaunchActivity.this.themeSwitchSunDrawable.draw(canvas);
                invalidate();
            }
        }
    }

    /* loaded from: classes4.dex */
    class 5 extends FireworksOverlay {
        5(Context context) {
            super(context);
            setVisibility(8);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.FireworksOverlay
        public void onStop() {
            super.onStop();
            setVisibility(8);
        }

        @Override // org.telegram.ui.Components.FireworksOverlay
        public void start(boolean z) {
            setVisibility(0);
            super.start(z);
        }
    }

    /* loaded from: classes4.dex */
    class 6 extends RecyclerListView {
        6(Context context) {
            super(context);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean drawChild(Canvas canvas, View view, long j) {
            int i;
            if (LaunchActivity.this.itemAnimator != null && LaunchActivity.this.itemAnimator.isRunning() && LaunchActivity.this.itemAnimator.isAnimatingChild(view)) {
                i = canvas.save();
                canvas.clipRect(0, LaunchActivity.this.itemAnimator.getAnimationClipTop(), getMeasuredWidth(), getMeasuredHeight());
            } else {
                i = -1;
            }
            boolean drawChild = super.drawChild(canvas, view, j);
            if (i >= 0) {
                canvas.restoreToCount(i);
                invalidate();
                invalidateViews();
            }
            return drawChild;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 7 extends ItemTouchHelper.SimpleCallback {
        private RecyclerView.ViewHolder selectedViewHolder;

        /* loaded from: classes4.dex */
        public class 1 extends AnimatorListenerAdapter {
            final /* synthetic */ View val$view;

            1(View view) {
                r2 = view;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                r2.setBackground(null);
            }
        }

        7(int i, int i2) {
            super(i, i2);
        }

        private void clearSelectedViewHolder() {
            RecyclerView.ViewHolder viewHolder = this.selectedViewHolder;
            if (viewHolder != null) {
                View view = viewHolder.itemView;
                this.selectedViewHolder = null;
                view.setTranslationX(0.0f);
                view.setTranslationY(0.0f);
                if (Build.VERSION.SDK_INT >= 21) {
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "elevation", 0.0f);
                    ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.7.1
                        final /* synthetic */ View val$view;

                        1(View view2) {
                            r2 = view2;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            r2.setBackground(null);
                        }
                    });
                    ofFloat.setDuration(150L).start();
                }
            }
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            clearSelectedViewHolder();
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            View view;
            View view2;
            View view3 = viewHolder.itemView;
            if (LaunchActivity.this.drawerLayoutAdapter.isAccountsShown()) {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(LaunchActivity.this.drawerLayoutAdapter.getFirstAccountPosition() - 1);
                RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = recyclerView.findViewHolderForAdapterPosition(LaunchActivity.this.drawerLayoutAdapter.getLastAccountPosition() + 1);
                if ((findViewHolderForAdapterPosition != null && (view2 = findViewHolderForAdapterPosition.itemView) != null && view2.getBottom() == view3.getTop() && f2 < 0.0f) || (findViewHolderForAdapterPosition2 != null && (view = findViewHolderForAdapterPosition2.itemView) != null && view.getTop() == view3.getBottom() && f2 > 0.0f)) {
                    f2 = 0.0f;
                }
            }
            view3.setTranslationX(f);
            view3.setTranslationY(f2);
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
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            clearSelectedViewHolder();
            if (i != 0) {
                this.selectedViewHolder = viewHolder;
                View view = viewHolder.itemView;
                LaunchActivity.this.sideMenu.cancelClickRunnables(false);
                view.setBackgroundColor(Theme.getColor(Theme.key_dialogBackground));
                if (Build.VERSION.SDK_INT >= 21) {
                    ObjectAnimator.ofFloat(view, "elevation", AndroidUtilities.dp(1.0f)).setDuration(150L).start();
                }
            }
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }
    }

    /* loaded from: classes4.dex */
    public class 8 extends DialogsActivity {
        final /* synthetic */ int val$accountNumber;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        8(Bundle bundle, int i) {
            super(bundle);
            r3 = i;
        }

        @Override // org.telegram.ui.ActionBar.BaseFragment
        public void onPreviewOpenAnimationEnd() {
            super.onPreviewOpenAnimationEnd();
            LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            LaunchActivity.this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(false);
            LaunchActivity.this.switchToAccount(r3, true);
            LaunchActivity.this.actionBarLayout.getView().invalidate();
        }

        @Override // org.telegram.ui.DialogsActivity, org.telegram.ui.ActionBar.BaseFragment
        public void onTransitionAnimationEnd(boolean z, boolean z2) {
            super.onTransitionAnimationEnd(z, z2);
            if (z || !z2) {
                return;
            }
            LaunchActivity.this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(false);
            LaunchActivity.this.actionBarLayout.getView().invalidate();
        }
    }

    /* loaded from: classes4.dex */
    class 9 implements View.OnAttachStateChangeListener {
        9() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            LaunchActivity.this.getWindowManager().addCrossWindowBlurEnabledListener(Consumer.Wrapper.convert(LaunchActivity.this.blurListener));
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            LaunchActivity.this.getWindowManager().removeCrossWindowBlurEnabledListener(Consumer.Wrapper.convert(LaunchActivity.this.blurListener));
        }
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

    private void checkFreeDiscSpace(final int i) {
        staticInstanceForAlerts = this;
        AutoDeleteMediaTask.run();
        SharedConfig.checkLogsToDelete();
        if ((Build.VERSION.SDK_INT < 26 || i != 0) && !this.checkFreeDiscSpaceShown) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkFreeDiscSpace$155(i);
                }
            }, 2000L);
        }
    }

    public static void checkFreeDiscSpaceStatic(int i) {
        LaunchActivity launchActivity = staticInstanceForAlerts;
        if (launchActivity != null) {
            launchActivity.checkFreeDiscSpace(i);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x00c2, code lost:
    
        if (r7.actionBarLayout.getFragmentStack().isEmpty() == false) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0129, code lost:
    
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0126, code lost:
    
        if (r7.actionBarLayout.getFragmentStack().isEmpty() == false) goto L127;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void checkLayout() {
        View view;
        if (!AndroidUtilities.isTablet() || this.rightActionBarLayout == null) {
            return;
        }
        if (AndroidUtilities.getWasTablet() == null || AndroidUtilities.getWasTablet().booleanValue() == AndroidUtilities.isTabletForce()) {
            int i = 8;
            if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                this.tabletFullSize = true;
                List<BaseFragment> fragmentStack = this.rightActionBarLayout.getFragmentStack();
                if (!fragmentStack.isEmpty()) {
                    while (fragmentStack.size() > 0) {
                        BaseFragment baseFragment = fragmentStack.get(0);
                        if (baseFragment instanceof ChatActivity) {
                            ((ChatActivity) baseFragment).setIgnoreAttachOnPause(true);
                        }
                        baseFragment.onPause();
                        baseFragment.onFragmentDestroy();
                        baseFragment.setParentLayout(null);
                        fragmentStack.remove(baseFragment);
                        this.actionBarLayout.addFragmentToStack(baseFragment);
                    }
                    PasscodeViewDialog passcodeViewDialog = this.passcodeDialog;
                    if (passcodeViewDialog == null || passcodeViewDialog.passcodeView.getVisibility() != 0) {
                        this.actionBarLayout.rebuildFragments(1);
                    }
                }
                this.shadowTabletSide.setVisibility(8);
                this.rightActionBarLayout.getView().setVisibility(8);
                view = this.backgroundTablet;
            } else {
                this.tabletFullSize = false;
                List<BaseFragment> fragmentStack2 = this.actionBarLayout.getFragmentStack();
                if (fragmentStack2.size() >= 2) {
                    while (1 < fragmentStack2.size()) {
                        BaseFragment baseFragment2 = fragmentStack2.get(1);
                        if (baseFragment2 instanceof ChatActivity) {
                            ((ChatActivity) baseFragment2).setIgnoreAttachOnPause(true);
                        }
                        baseFragment2.onPause();
                        baseFragment2.onFragmentDestroy();
                        baseFragment2.setParentLayout(null);
                        fragmentStack2.remove(baseFragment2);
                        this.rightActionBarLayout.addFragmentToStack(baseFragment2);
                    }
                    PasscodeViewDialog passcodeViewDialog2 = this.passcodeDialog;
                    if (passcodeViewDialog2 == null || passcodeViewDialog2.passcodeView.getVisibility() != 0) {
                        this.actionBarLayout.rebuildFragments(1);
                        this.rightActionBarLayout.rebuildFragments(1);
                    }
                }
                this.rightActionBarLayout.getView().setVisibility(this.rightActionBarLayout.getFragmentStack().isEmpty() ? 8 : 0);
                this.backgroundTablet.setVisibility(this.rightActionBarLayout.getFragmentStack().isEmpty() ? 0 : 8);
                view = this.shadowTabletSide;
            }
            view.setVisibility(i);
        }
    }

    private void checkSystemBarColors() {
        checkSystemBarColors(false, true, !this.isNavigationBarColorFrozen, true);
    }

    private void checkSystemBarColors(boolean z, boolean z2) {
        checkSystemBarColors(false, z, z2, true);
    }

    private void checkWasMutedByAdmin(boolean z) {
        ChatObject.Call call;
        long j;
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        boolean z2 = false;
        if (sharedInstance == null || (call = sharedInstance.groupCall) == null) {
            this.wasMutedByAdminRaisedHand = false;
            return;
        }
        boolean z3 = this.wasMutedByAdminRaisedHand;
        TLRPC.InputPeer groupCallPeer = sharedInstance.getGroupCallPeer();
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
        TLRPC.TL_groupCallParticipant tL_groupCallParticipant = (TLRPC.TL_groupCallParticipant) call.participants.get(j);
        boolean z4 = (tL_groupCallParticipant == null || tL_groupCallParticipant.can_self_unmute || !tL_groupCallParticipant.muted) ? false : true;
        if (z4 && tL_groupCallParticipant.raise_hand_rating != 0) {
            z2 = true;
        }
        this.wasMutedByAdminRaisedHand = z2;
        if (z || !z3 || z2 || z4 || GroupCallActivity.groupCallInstance != null) {
            return;
        }
        showVoiceChatTooltip(38);
    }

    public static void clearFragments() {
        Iterator it = mainFragmentsStack.iterator();
        while (it.hasNext()) {
            ((BaseFragment) it.next()).onFragmentDestroy();
        }
        mainFragmentsStack.clear();
        if (AndroidUtilities.isTablet()) {
            Iterator it2 = layerFragmentsStack.iterator();
            while (it2.hasNext()) {
                ((BaseFragment) it2.next()).onFragmentDestroy();
            }
            layerFragmentsStack.clear();
            Iterator it3 = rightFragmentsStack.iterator();
            while (it3.hasNext()) {
                ((BaseFragment) it3.next()).onFragmentDestroy();
            }
            rightFragmentsStack.clear();
        }
    }

    public static void dismissAllWeb() {
        ArrayList arrayList;
        BaseFragment safeLastFragment = getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        EmptyBaseFragment sheetFragment = safeLastFragment.getParentLayout() instanceof ActionBarLayout ? ((ActionBarLayout) safeLastFragment.getParentLayout()).getSheetFragment(false) : null;
        if (sheetFragment != null && (arrayList = sheetFragment.sheetsStack) != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                ((BaseFragment.AttachedSheet) sheetFragment.sheetsStack.get(size)).dismiss(true);
            }
        }
        ArrayList arrayList2 = safeLastFragment.sheetsStack;
        if (arrayList2 != null) {
            for (int size2 = arrayList2.size() - 1; size2 >= 0; size2--) {
                ((BaseFragment.AttachedSheet) safeLastFragment.sheetsStack.get(size2)).dismiss(true);
            }
        }
    }

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
        int i3 = i - iArr2[0];
        int i4 = i2 - iArr2[1];
        canvas.save();
        canvas.translate(i3, i4);
        this.rippleAbove.getBackground().draw(canvas);
        canvas.restore();
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0075  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private List findContacts(String str, String str2, boolean z) {
        String str3;
        String lowerCase;
        TLRPC.User user;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        ContactsController contactsController = ContactsController.getInstance(this.currentAccount);
        ArrayList arrayList = new ArrayList(contactsController.contacts);
        ArrayList arrayList2 = new ArrayList();
        String str4 = null;
        int i = 0;
        if (str2 != null) {
            String stripExceptNumbers = PhoneFormat.stripExceptNumbers(str2);
            TLRPC.TL_contact tL_contact = contactsController.contactsByPhone.get(stripExceptNumbers);
            if (tL_contact == null) {
                tL_contact = contactsController.contactsByShortPhone.get(stripExceptNumbers.substring(Math.max(0, stripExceptNumbers.length() - 7)));
            }
            if (tL_contact != null) {
                TLRPC.User user2 = messagesController.getUser(Long.valueOf(tL_contact.user_id));
                if (user2 == null || (user2.self && !z)) {
                    str3 = null;
                    if (arrayList2.isEmpty() && str3 != null) {
                        lowerCase = str3.trim().toLowerCase();
                        if (!TextUtils.isEmpty(lowerCase)) {
                            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
                            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                                translitString = null;
                            }
                            String[] strArr = {lowerCase, translitString};
                            int size = arrayList.size();
                            int i2 = 0;
                            while (i2 < size) {
                                TLRPC.TL_contact tL_contact2 = (TLRPC.TL_contact) arrayList.get(i2);
                                if (tL_contact2 != null && (user = messagesController.getUser(Long.valueOf(tL_contact2.user_id))) != null && (!user.self || z)) {
                                    int i3 = 3;
                                    String[] strArr2 = new String[3];
                                    strArr2[i] = ContactsController.formatName(user.first_name, user.last_name).toLowerCase();
                                    String translitString2 = LocaleController.getInstance().getTranslitString(strArr2[i]);
                                    strArr2[1] = translitString2;
                                    if (strArr2[i].equals(translitString2)) {
                                        strArr2[1] = str4;
                                    }
                                    int i4 = 2;
                                    if (UserObject.isReplyUser(user)) {
                                        strArr2[2] = LocaleController.getString(R.string.RepliesTitle).toLowerCase();
                                    } else if (user.self) {
                                        strArr2[2] = LocaleController.getString(R.string.SavedMessages).toLowerCase();
                                    }
                                    int i5 = 0;
                                    boolean z2 = false;
                                    while (true) {
                                        if (i5 >= i4) {
                                            break;
                                        }
                                        String str5 = strArr[i5];
                                        if (str5 != null) {
                                            while (i < i3) {
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
                                                i3 = 3;
                                            }
                                            String publicUsername = UserObject.getPublicUsername(user);
                                            if (!z2 && publicUsername != null && publicUsername.startsWith(str5)) {
                                                z2 = true;
                                            }
                                            if (z2) {
                                                arrayList2.add(tL_contact2);
                                                break;
                                            }
                                        }
                                        i5++;
                                        i = 0;
                                        i3 = 3;
                                        i4 = 2;
                                    }
                                }
                                i2++;
                                str4 = null;
                                i = 0;
                            }
                        }
                    }
                    return arrayList2;
                }
                arrayList2.add(tL_contact);
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

    private BaseFragment getClientNotActivatedFragment() {
        return LoginActivity.loadCurrentState(false, this.currentAccount).getInt("currentViewNum", 0) != 0 ? new LoginActivity() : new IntroActivity();
    }

    public static BaseFragment getLastFragment() {
        INavigationLayout actionBarLayout;
        LaunchActivity launchActivity = instance;
        if (launchActivity == null || launchActivity.sheetFragmentsStack.isEmpty()) {
            LaunchActivity launchActivity2 = instance;
            if (launchActivity2 == null || launchActivity2.getActionBarLayout() == null) {
                return null;
            }
            actionBarLayout = instance.getActionBarLayout();
        } else {
            actionBarLayout = (INavigationLayout) instance.sheetFragmentsStack.get(r0.size() - 1);
        }
        return actionBarLayout.getLastFragment();
    }

    public static BaseFragment getSafeLastFragment() {
        INavigationLayout actionBarLayout;
        LaunchActivity launchActivity = instance;
        if (launchActivity == null || launchActivity.sheetFragmentsStack.isEmpty()) {
            LaunchActivity launchActivity2 = instance;
            if (launchActivity2 == null || launchActivity2.getActionBarLayout() == null) {
                return null;
            }
            actionBarLayout = instance.getActionBarLayout();
        } else {
            actionBarLayout = (INavigationLayout) instance.sheetFragmentsStack.get(r0.size() - 1);
        }
        return actionBarLayout.getSafeLastFragment();
    }

    private String getStringForLanguageAlert(HashMap hashMap, String str, int i) {
        String str2 = (String) hashMap.get(str);
        return str2 == null ? LocaleController.getString(str, i) : str2;
    }

    public static int getTimestampFromLink(Uri uri) {
        int i;
        String query = uri.getPathSegments().contains(MediaStreamTrack.VIDEO_TRACK_KIND) ? uri.getQuery() : uri.getQueryParameter("t") != null ? uri.getQueryParameter("t") : null;
        if (query == null) {
            return -1;
        }
        try {
            i = Integer.parseInt(query);
        } catch (Throwable unused) {
            i = -1;
        }
        if (i == -1) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
            try {
                return (int) ((simpleDateFormat.parse(query).getTime() - simpleDateFormat.parse("00:00").getTime()) / 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    private boolean handleIntent(Intent intent, boolean z, boolean z2, boolean z3) {
        return handleIntent(intent, z, z2, z3, null, true, false);
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:77)
        */
    private boolean handleIntent(android.content.Intent r105, boolean r106, boolean r107, boolean r108, org.telegram.messenger.browser.Browser.Progress r109, boolean r110, boolean r111) {
        /*
            Method dump skipped, instructions count: 13852
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.LaunchActivity.handleIntent(android.content.Intent, boolean, boolean, boolean, org.telegram.messenger.browser.Browser$Progress, boolean, boolean):boolean");
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

    private void invalidateTabletMode() {
        long j;
        ArrayList arrayList;
        Boolean wasTablet = AndroidUtilities.getWasTablet();
        if (wasTablet == null) {
            return;
        }
        AndroidUtilities.resetWasTabletFlag();
        if (wasTablet.booleanValue() != AndroidUtilities.isTablet()) {
            long j2 = 0;
            if (wasTablet.booleanValue()) {
                ArrayList arrayList2 = mainFragmentsStack;
                ArrayList arrayList3 = rightFragmentsStack;
                arrayList2.addAll(arrayList3);
                ArrayList arrayList4 = layerFragmentsStack;
                arrayList2.addAll(arrayList4);
                arrayList3.clear();
                arrayList4.clear();
                j = 0;
            } else {
                ArrayList arrayList5 = mainFragmentsStack;
                ArrayList<BaseFragment> arrayList6 = new ArrayList(arrayList5);
                arrayList5.clear();
                rightFragmentsStack.clear();
                layerFragmentsStack.clear();
                long j3 = 0;
                j = 0;
                for (BaseFragment baseFragment : arrayList6) {
                    if (baseFragment instanceof DialogsActivity) {
                        DialogsActivity dialogsActivity = (DialogsActivity) baseFragment;
                        if (dialogsActivity.isMainDialogList() && !dialogsActivity.isArchive()) {
                            arrayList = mainFragmentsStack;
                            arrayList.add(baseFragment);
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
                    arrayList = layerFragmentsStack;
                    arrayList.add(baseFragment);
                }
                j2 = j3;
            }
            setupActionBarLayout();
            this.actionBarLayout.rebuildFragments(1);
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.rebuildFragments(1);
                this.layersActionBarLayout.rebuildFragments(1);
                Iterator it = mainFragmentsStack.iterator();
                while (it.hasNext()) {
                    BaseFragment baseFragment2 = (BaseFragment) it.next();
                    if (baseFragment2 instanceof DialogsActivity) {
                        DialogsActivity dialogsActivity2 = (DialogsActivity) baseFragment2;
                        if (dialogsActivity2.isMainDialogList()) {
                            dialogsActivity2.setOpenedDialogId(j2, j);
                        }
                    }
                }
            }
        }
    }

    public /* synthetic */ void lambda$animateNavigationBarColor$164(ValueAnimator valueAnimator) {
        setNavigationBarColor(((Integer) valueAnimator.getAnimatedValue()).intValue(), false);
    }

    public /* synthetic */ void lambda$checkAppUpdate$128(TLRPC.TL_help_appUpdate tL_help_appUpdate, int i, Browser.Progress progress) {
        BaseFragment lastFragment;
        TLRPC.TL_help_appUpdate tL_help_appUpdate2 = SharedConfig.pendingAppUpdate;
        if (tL_help_appUpdate2 == null || !tL_help_appUpdate2.version.equals(tL_help_appUpdate.version)) {
            boolean newAppVersionAvailable = SharedConfig.setNewAppVersionAvailable(tL_help_appUpdate);
            if (newAppVersionAvailable) {
                if (tL_help_appUpdate.can_not_skip) {
                    showUpdateActivity(i, tL_help_appUpdate, false);
                } else if (ApplicationLoader.isStandaloneBuild() || BuildVars.DEBUG_VERSION) {
                    this.drawerLayoutAdapter.notifyDataSetChanged();
                    ApplicationLoader.applicationLoaderInstance.showUpdateAppPopup(this, tL_help_appUpdate, i);
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

    public static /* synthetic */ void lambda$checkAppUpdate$129(Browser.Progress progress) {
        if (progress != null) {
            progress.end();
            BaseFragment lastFragment = getLastFragment();
            if (lastFragment != null) {
                BulletinFactory.of(lastFragment).createSimpleBulletin(R.raw.chats_infotip, LocaleController.getString(R.string.YourVersionIsLatest)).show();
            }
        }
    }

    public static /* synthetic */ void lambda$checkAppUpdate$130(Browser.Progress progress, TLRPC.TL_error tL_error) {
        if (progress != null) {
            progress.end();
            BaseFragment lastFragment = getLastFragment();
            if (lastFragment != null) {
                BulletinFactory.of(lastFragment).showForError(tL_error);
            }
        }
    }

    public /* synthetic */ void lambda$checkAppUpdate$131(final int i, final Browser.Progress progress, TLObject tLObject, final TLRPC.TL_error tL_error) {
        Runnable runnable;
        SharedConfig.lastUpdateCheckTime = System.currentTimeMillis();
        SharedConfig.saveConfig();
        if (tLObject instanceof TLRPC.TL_help_appUpdate) {
            final TLRPC.TL_help_appUpdate tL_help_appUpdate = (TLRPC.TL_help_appUpdate) tLObject;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda97
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$checkAppUpdate$128(tL_help_appUpdate, i, progress);
                }
            });
            return;
        }
        if (tLObject instanceof TLRPC.TL_help_noAppUpdate) {
            runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda98
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.lambda$checkAppUpdate$129(Browser.Progress.this);
                }
            };
        } else if (tL_error == null) {
            return;
        } else {
            runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda99
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.lambda$checkAppUpdate$130(Browser.Progress.this, tL_error);
                }
            };
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    public /* synthetic */ void lambda$checkAppUpdate$132(int i) {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(i, true);
    }

    public /* synthetic */ void lambda$checkFreeDiscSpace$153(DialogInterface dialogInterface) {
        this.checkFreeDiscSpaceShown = false;
    }

    public /* synthetic */ void lambda$checkFreeDiscSpace$154() {
        if (this.checkFreeDiscSpaceShown) {
            return;
        }
        try {
            Dialog createFreeSpaceDialog = AlertsCreator.createFreeSpaceDialog(this);
            createFreeSpaceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda107
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LaunchActivity.this.lambda$checkFreeDiscSpace$153(dialogInterface);
                }
            });
            this.checkFreeDiscSpaceShown = true;
            createFreeSpaceDialog.show();
        } catch (Throwable unused) {
        }
    }

    public /* synthetic */ void lambda$checkFreeDiscSpace$155(int i) {
        File directory;
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            try {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                if ((((i == 2 || i == 1) && Math.abs(this.alreadyShownFreeDiscSpaceAlertForced - System.currentTimeMillis()) > 240000) || Math.abs(globalMainSettings.getLong("last_space_check", 0L) - System.currentTimeMillis()) >= 259200000) && (directory = FileLoader.getDirectory(4)) != null) {
                    StatFs statFs = new StatFs(directory.getAbsolutePath());
                    long availableBlocksLong = statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
                    if (i > 0 || availableBlocksLong < 52428800) {
                        if (i > 0) {
                            this.alreadyShownFreeDiscSpaceAlertForced = System.currentTimeMillis();
                        }
                        globalMainSettings.edit().putLong("last_space_check", System.currentTimeMillis()).commit();
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda39
                            @Override // java.lang.Runnable
                            public final void run() {
                                LaunchActivity.this.lambda$checkFreeDiscSpace$154();
                            }
                        });
                    }
                }
            } catch (Throwable unused) {
            }
        }
    }

    public static /* synthetic */ void lambda$didReceivedNotification$140(int i, DialogInterface dialogInterface, int i2) {
        ArrayList arrayList = mainFragmentsStack;
        if (arrayList.isEmpty()) {
            return;
        }
        MessagesController.getInstance(i).openByUserName("spambot", (BaseFragment) arrayList.get(arrayList.size() - 1), 1);
    }

    public /* synthetic */ void lambda$didReceivedNotification$141(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).performLogout(2);
    }

    public static /* synthetic */ void lambda$didReceivedNotification$142(HashMap hashMap, int i, TLRPC.MessageMedia messageMedia, int i2, boolean z, int i3) {
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            MessageObject messageObject = (MessageObject) ((Map.Entry) it.next()).getValue();
            SendMessagesHelper.getInstance(i).sendMessage(SendMessagesHelper.SendMessageParams.of(messageMedia, messageObject.getDialogId(), messageObject, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, z, i3));
        }
    }

    public /* synthetic */ void lambda$didReceivedNotification$143(final HashMap hashMap, final int i, DialogInterface dialogInterface, int i2) {
        ArrayList arrayList = mainFragmentsStack;
        if (!arrayList.isEmpty() && AndroidUtilities.isMapsInstalled((BaseFragment) arrayList.get(arrayList.size() - 1))) {
            LocationActivity locationActivity = new LocationActivity(0);
            locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda38
                @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
                public final void didSelectLocation(TLRPC.MessageMedia messageMedia, int i3, boolean z, int i4) {
                    LaunchActivity.lambda$didReceivedNotification$142(hashMap, i, messageMedia, i3, z, i4);
                }
            });
            lambda$runLinkRequest$93(locationActivity);
        }
    }

    public static /* synthetic */ void lambda$didReceivedNotification$144(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, false);
    }

    public static /* synthetic */ void lambda$didReceivedNotification$145(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    public static /* synthetic */ void lambda$didReceivedNotification$146(int i, HashMap hashMap, boolean z, boolean z2, DialogInterface dialogInterface, int i2) {
        ContactsController.getInstance(i).syncPhoneBookByAlert(hashMap, z, z2, true);
    }

    public /* synthetic */ void lambda$didReceivedNotification$147(ValueAnimator valueAnimator) {
        this.frameLayout.invalidate();
    }

    public /* synthetic */ void lambda$didReceivedNotification$148() {
        if (this.isNavigationBarColorFrozen) {
            this.isNavigationBarColorFrozen = false;
            checkSystemBarColors(false, true);
        }
    }

    public /* synthetic */ void lambda$didReceivedNotification$149(TLObject tLObject, Theme.ThemeInfo themeInfo) {
        if (!(tLObject instanceof TLRPC.TL_wallPaper)) {
            onThemeLoadFinish();
            return;
        }
        TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) tLObject;
        this.loadingThemeInfo = themeInfo;
        this.loadingThemeWallpaperName = FileLoader.getAttachFileName(tL_wallPaper.document);
        this.loadingThemeWallpaper = tL_wallPaper;
        FileLoader.getInstance(themeInfo.account).loadFile(tL_wallPaper.document, tL_wallPaper, 1, 1);
    }

    public /* synthetic */ void lambda$didReceivedNotification$150(final Theme.ThemeInfo themeInfo, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$didReceivedNotification$149(tLObject, themeInfo);
            }
        });
    }

    public /* synthetic */ void lambda$didReceivedNotification$151() {
        if (this.loadingTheme == null) {
            return;
        }
        File file = new File(ApplicationLoader.getFilesDirFixed(), "remote" + this.loadingTheme.id + ".attheme");
        TLRPC.TL_theme tL_theme = this.loadingTheme;
        Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file, tL_theme.title, tL_theme, true);
        if (applyThemeFile != null) {
            lambda$runLinkRequest$93(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
        }
        onThemeLoadFinish();
    }

    public /* synthetic */ void lambda$didReceivedNotification$152(Theme.ThemeInfo themeInfo, File file) {
        themeInfo.createBackground(file, themeInfo.pathToWallpaper);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$didReceivedNotification$151();
            }
        });
    }

    public /* synthetic */ void lambda$didSelectDialogs$134(int i, DialogsActivity dialogsActivity, boolean z, ArrayList arrayList, Uri uri, AlertDialog alertDialog, long j) {
        String str;
        long j2 = j;
        if (j2 != 0) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("scrollToTopOnResume", true);
            if (!AndroidUtilities.isTablet()) {
                NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            }
            if (DialogObject.isUserDialog(j)) {
                str = "user_id";
            } else {
                j2 = -j2;
                str = "chat_id";
            }
            bundle.putLong(str, j2);
            ChatActivity chatActivity = new ChatActivity(bundle);
            chatActivity.setOpenImport();
            getActionBarLayout().presentFragment(chatActivity, dialogsActivity != null || z, dialogsActivity == null, true, false);
        } else {
            this.documentsUrisArray = arrayList;
            if (arrayList == null) {
                this.documentsUrisArray = new ArrayList();
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

    public /* synthetic */ void lambda$didSelectDialogs$135(ChatActivity chatActivity, ArrayList arrayList, int i, CharSequence charSequence, int i2, boolean z, TLRPC.User user, boolean z2, int i3, long j, boolean z3) {
        MessageObject messageObject;
        TLRPC.TL_forumTopic findTopic;
        if (chatActivity != null) {
            getActionBarLayout().presentFragment(chatActivity, true, false, true, false);
        }
        AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            long j2 = ((MessagesStorage.TopicKey) arrayList.get(i4)).dialogId;
            long j3 = ((MessagesStorage.TopicKey) arrayList.get(i4)).topicId;
            if (j3 == 0 || (findTopic = accountInstance.getMessagesController().getTopicsController().findTopic(-j2, j3)) == null || findTopic.topicStartMessage == null) {
                messageObject = null;
            } else {
                MessageObject messageObject2 = new MessageObject(accountInstance.getCurrentAccount(), findTopic.topicStartMessage, false, false);
                messageObject2.isTopicMainMessage = true;
                messageObject = messageObject2;
            }
            SendMessagesHelper.SendMessageParams of = SendMessagesHelper.SendMessageParams.of(user, j2, messageObject, messageObject, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, z2, i3 != 0 ? i3 : i);
            if (TextUtils.isEmpty(charSequence)) {
                of.effect_id = j;
            }
            of.invert_media = z3;
            SendMessagesHelper.getInstance(i2).sendMessage(of);
            if (!TextUtils.isEmpty(charSequence)) {
                SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), j2, z, i3 != 0 ? i3 : i, j);
            }
        }
    }

    public /* synthetic */ void lambda$handleIntent$15(String str) {
        if (this.actionBarLayout.getFragmentStack().isEmpty()) {
            return;
        }
        this.actionBarLayout.getFragmentStack().get(0).presentFragment(new PremiumPreviewFragment(Uri.parse(str).getQueryParameter("ref")));
    }

    public /* synthetic */ void lambda$handleIntent$16(Intent intent, boolean z) {
        handleIntent(intent, true, false, false);
    }

    public /* synthetic */ void lambda$handleIntent$17(AlertDialog alertDialog, TLRPC.TL_error tL_error, String str, Bundle bundle, TLObject tLObject, TLRPC.TL_account_sendConfirmPhoneCode tL_account_sendConfirmPhoneCode) {
        alertDialog.dismiss();
        if (tL_error == null) {
            lambda$runLinkRequest$93(new LoginActivity().cancelAccountDeletion(str, bundle, (TLRPC.TL_auth_sentCode) tLObject));
        } else {
            AlertsCreator.processError(this.currentAccount, tL_error, getActionBarLayout().getLastFragment(), tL_account_sendConfirmPhoneCode, new Object[0]);
        }
    }

    public /* synthetic */ void lambda$handleIntent$18(final AlertDialog alertDialog, final String str, final Bundle bundle, final TLRPC.TL_account_sendConfirmPhoneCode tL_account_sendConfirmPhoneCode, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda61
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$handleIntent$17(alertDialog, tL_error, str, bundle, tLObject, tL_account_sendConfirmPhoneCode);
            }
        });
    }

    public /* synthetic */ void lambda$handleIntent$19(long j, long j2, ChatActivity chatActivity) {
        FileLog.d("LaunchActivity openForum after load " + j + " " + j2 + " TL_forumTopic " + MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(j, j2));
        if (this.actionBarLayout != null) {
            ForumUtilities.applyTopic(chatActivity, MessagesStorage.TopicKey.of(-j, j2));
            getActionBarLayout().presentFragment(chatActivity);
        }
    }

    public static /* synthetic */ void lambda$handleIntent$20(int[] iArr, long j, TLRPC.MessageMedia messageMedia, int i, boolean z, int i2) {
        SendMessagesHelper.getInstance(iArr[0]).sendMessage(SendMessagesHelper.SendMessageParams.of(messageMedia, j, (MessageObject) null, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, z, i2));
    }

    public /* synthetic */ void lambda$handleIntent$21(final int[] iArr, LocationController.SharingLocationInfo sharingLocationInfo) {
        int i = sharingLocationInfo.messageObject.currentAccount;
        iArr[0] = i;
        switchToAccount(i, true);
        LocationActivity locationActivity = new LocationActivity(2);
        locationActivity.setMessageObject(sharingLocationInfo.messageObject);
        final long dialogId = sharingLocationInfo.messageObject.getDialogId();
        locationActivity.setDelegate(new LocationActivity.LocationActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda108
            @Override // org.telegram.ui.LocationActivity.LocationActivityDelegate
            public final void didSelectLocation(TLRPC.MessageMedia messageMedia, int i2, boolean z, int i3) {
                LaunchActivity.lambda$handleIntent$20(iArr, dialogId, messageMedia, i2, z, i3);
            }
        });
        lambda$runLinkRequest$93(locationActivity);
    }

    public /* synthetic */ void lambda$handleIntent$22() {
        if (this.actionBarLayout.getFragmentStack().isEmpty()) {
            return;
        }
        this.actionBarLayout.getFragmentStack().get(0).showDialog(new StickersAlert(this, this.importingStickersSoftware, this.importingStickers, this.importingStickersEmoji, null));
    }

    public static /* synthetic */ void lambda$handleIntent$23(Thread thread, Throwable th) {
        if (thread == Looper.getMainLooper().getThread()) {
            FileLog.fatal(th, true);
        }
    }

    public static /* synthetic */ void lambda$handleIntent$24(Thread thread, Throwable th) {
        if (thread == Looper.getMainLooper().getThread()) {
            FileLog.fatal(th, true);
        }
    }

    public /* synthetic */ void lambda$handleIntent$25(BaseFragment baseFragment, boolean z) {
        presentFragment(baseFragment, z, false);
    }

    public /* synthetic */ void lambda$handleIntent$26(boolean z, int[] iArr, TLRPC.User user, String str, ContactsActivity contactsActivity) {
        TLRPC.UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(user.id);
        VoIPHelper.startCall(user, z, userFull != null && userFull.video_calls_available, this, userFull, AccountInstance.getInstance(iArr[0]));
    }

    public static /* synthetic */ void lambda$handleIntent$27(ActionIntroActivity actionIntroActivity, TLRPC.TL_error tL_error) {
        AlertsCreator.showSimpleAlert(actionIntroActivity, LocaleController.getString(R.string.AuthAnotherClient), LocaleController.getString(R.string.ErrorOccurred) + "\n" + tL_error.text);
    }

    public static /* synthetic */ void lambda$handleIntent$28(AlertDialog alertDialog, TLObject tLObject, final ActionIntroActivity actionIntroActivity, final TLRPC.TL_error tL_error) {
        try {
            alertDialog.dismiss();
        } catch (Exception unused) {
        }
        if (tLObject instanceof TLRPC.TL_authorization) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda167
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$handleIntent$27(ActionIntroActivity.this, tL_error);
            }
        });
    }

    public static /* synthetic */ void lambda$handleIntent$29(final AlertDialog alertDialog, final ActionIntroActivity actionIntroActivity, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda166
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$handleIntent$28(AlertDialog.this, tLObject, actionIntroActivity, tL_error);
            }
        });
    }

    public /* synthetic */ void lambda$handleIntent$30(final ActionIntroActivity actionIntroActivity, String str) {
        final AlertDialog alertDialog = new AlertDialog(this, 3);
        alertDialog.setCanCancel(false);
        alertDialog.show();
        byte[] decode = Base64.decode(str.substring(17), 8);
        TLRPC.TL_auth_acceptLoginToken tL_auth_acceptLoginToken = new TLRPC.TL_auth_acceptLoginToken();
        tL_auth_acceptLoginToken.token = decode;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_auth_acceptLoginToken, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda142
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.lambda$handleIntent$29(AlertDialog.this, actionIntroActivity, tLObject, tL_error);
            }
        });
    }

    public /* synthetic */ void lambda$handleIntent$31(BaseFragment baseFragment, String str, String str2, DialogInterface dialogInterface, int i) {
        NewContactBottomSheet newContactBottomSheet = new NewContactBottomSheet(baseFragment, this);
        newContactBottomSheet.setInitialPhoneNumber(str, false);
        if (str2 != null) {
            String[] split = str2.split(" ", 2);
            newContactBottomSheet.setInitialName(split[0], split.length > 1 ? split[1] : null);
        }
        newContactBottomSheet.show();
    }

    public /* synthetic */ void lambda$onActivityResult$137() {
        GroupCallPip.clearForce();
        GroupCallPip.updateVisibility(this);
    }

    public static /* synthetic */ boolean lambda$onCreate$0() {
        return SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture;
    }

    public /* synthetic */ void lambda$onCreate$1(View view) {
        showSelectStatusDialog();
    }

    public /* synthetic */ void lambda$onCreate$2() {
        this.drawerLayoutContainer.closeDrawer(false);
    }

    public /* synthetic */ void lambda$onCreate$3(TLRPC.TL_attachMenuBot tL_attachMenuBot) {
        tL_attachMenuBot.side_menu_disclaimer_needed = false;
        tL_attachMenuBot.inactive = false;
        showAttachMenuBot(tL_attachMenuBot, null, true);
        MediaDataController.getInstance(this.currentAccount).updateAttachMenuBotsInCache();
    }

    public /* synthetic */ void lambda$onCreate$4(final TLRPC.TL_attachMenuBot tL_attachMenuBot, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda133
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$onCreate$3(tL_attachMenuBot);
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$5(final TLRPC.TL_attachMenuBot tL_attachMenuBot, Boolean bool) {
        TLRPC.TL_messages_toggleBotInAttachMenu tL_messages_toggleBotInAttachMenu = new TLRPC.TL_messages_toggleBotInAttachMenu();
        tL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(this.currentAccount).getInputUser(tL_attachMenuBot.bot_id);
        tL_messages_toggleBotInAttachMenu.enabled = true;
        tL_messages_toggleBotInAttachMenu.write_allowed = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda119
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.this.lambda$onCreate$4(tL_attachMenuBot, tLObject, tL_error);
            }
        }, 66);
    }

    public /* synthetic */ void lambda$onCreate$6(View view, int i, float f, float f2) {
        BaseFragment mediaActivity;
        int i2;
        BaseFragment chatActivity;
        BaseFragment callLogActivity;
        if (this.drawerLayoutAdapter.click(view, i)) {
            this.drawerLayoutContainer.closeDrawer(false);
            return;
        }
        if (i == 0) {
            DrawerProfileCell drawerProfileCell = (DrawerProfileCell) view;
            if (drawerProfileCell.isInAvatar(f, f2)) {
                openSettings(drawerProfileCell.hasAvatar());
                return;
            } else {
                this.drawerLayoutAdapter.setAccountsShown(!r7.isAccountsShown(), true);
                return;
            }
        }
        if (view instanceof DrawerUserCell) {
            switchToAccount(((DrawerUserCell) view).getAccountNumber(), true);
        } else {
            Integer num = null;
            if (view instanceof DrawerAddCell) {
                int i3 = 0;
                for (int i4 = 3; i4 >= 0; i4--) {
                    if (!UserConfig.getInstance(i4).isClientActivated()) {
                        i3++;
                        if (num == null) {
                            num = Integer.valueOf(i4);
                        }
                    }
                }
                if (!UserConfig.hasPremiumOnAccounts()) {
                    i3--;
                }
                if (i3 <= 0 || num == null) {
                    if (UserConfig.hasPremiumOnAccounts() || this.actionBarLayout.getFragmentStack().size() <= 0) {
                        return;
                    }
                    BaseFragment baseFragment = this.actionBarLayout.getFragmentStack().get(0);
                    LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(baseFragment, this, 7, this.currentAccount, null);
                    baseFragment.showDialog(limitReachedBottomSheet);
                    limitReachedBottomSheet.onShowPremiumScreenRunnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda62
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$onCreate$2();
                        }
                    };
                    return;
                }
                callLogActivity = new LoginActivity(num.intValue());
            } else {
                int id = this.drawerLayoutAdapter.getId(i);
                final TLRPC.TL_attachMenuBot attachMenuBot = this.drawerLayoutAdapter.getAttachMenuBot(i);
                if (attachMenuBot != null) {
                    if (attachMenuBot.inactive || attachMenuBot.side_menu_disclaimer_needed) {
                        WebAppDisclaimerAlert.show(this, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda63
                            @Override // com.google.android.exoplayer2.util.Consumer
                            public final void accept(Object obj) {
                                LaunchActivity.this.lambda$onCreate$5(attachMenuBot, (Boolean) obj);
                            }
                        }, null, null);
                        return;
                    } else {
                        showAttachMenuBot(attachMenuBot, null, true);
                        return;
                    }
                }
                if (id == 2) {
                    chatActivity = new GroupCreateActivity(new Bundle());
                } else if (id == 3) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("onlyUsers", true);
                    bundle.putBoolean("destroyAfterSelect", true);
                    bundle.putBoolean("createSecretChat", true);
                    bundle.putBoolean("allowBots", false);
                    bundle.putBoolean("allowSelf", false);
                    chatActivity = new ContactsActivity(bundle);
                } else if (id == 4) {
                    SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                    if (BuildVars.DEBUG_VERSION || !globalMainSettings.getBoolean("channel_intro", false)) {
                        lambda$runLinkRequest$93(new ActionIntroActivity(0));
                        globalMainSettings.edit().putBoolean("channel_intro", true).commit();
                    } else {
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("step", 0);
                        chatActivity = new ChannelCreateActivity(bundle2);
                    }
                } else if (id == 6) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putBoolean("needFinishFragment", false);
                    chatActivity = new ContactsActivity(bundle3);
                } else if (id == 7) {
                    callLogActivity = new InviteContactsActivity();
                } else {
                    if (id == 8) {
                        openSettings(false);
                        return;
                    }
                    if (id == 9) {
                        i2 = R.string.TelegramFaqUrl;
                    } else if (id == 10) {
                        callLogActivity = new CallLogActivity();
                    } else if (id == 11) {
                        Bundle bundle4 = new Bundle();
                        bundle4.putLong("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                        chatActivity = new ChatActivity(bundle4);
                    } else {
                        if (id != 13) {
                            if (id == 15) {
                                showSelectStatusDialog();
                                return;
                            }
                            if (id == 16) {
                                this.drawerLayoutContainer.closeDrawer(true);
                                Bundle bundle5 = new Bundle();
                                bundle5.putLong("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                                bundle5.putBoolean("my_profile", true);
                                mediaActivity = new ProfileActivity(bundle5, null);
                            } else {
                                if (id != 17) {
                                    return;
                                }
                                this.drawerLayoutContainer.closeDrawer(true);
                                Bundle bundle6 = new Bundle();
                                bundle6.putLong("dialog_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                                bundle6.putInt("type", 1);
                                mediaActivity = new MediaActivity(bundle6, null);
                            }
                            lambda$runLinkRequest$93(mediaActivity);
                            return;
                        }
                        i2 = R.string.TelegramFeaturesUrl;
                    }
                    Browser.openUrl(this, LocaleController.getString(i2));
                }
                lambda$runLinkRequest$93(chatActivity);
            }
            lambda$runLinkRequest$93(callLogActivity);
        }
        this.drawerLayoutContainer.closeDrawer(false);
    }

    public /* synthetic */ boolean lambda$onCreate$7(ItemTouchHelper itemTouchHelper, View view, int i) {
        if (view instanceof DrawerUserCell) {
            int accountNumber = ((DrawerUserCell) view).getAccountNumber();
            if (accountNumber != this.currentAccount && !AndroidUtilities.isTablet()) {
                8 r6 = new DialogsActivity(null) { // from class: org.telegram.ui.LaunchActivity.8
                    final /* synthetic */ int val$accountNumber;

                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    8(Bundle bundle, int accountNumber2) {
                        super(bundle);
                        r3 = accountNumber2;
                    }

                    @Override // org.telegram.ui.ActionBar.BaseFragment
                    public void onPreviewOpenAnimationEnd() {
                        super.onPreviewOpenAnimationEnd();
                        LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                        LaunchActivity.this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(false);
                        LaunchActivity.this.switchToAccount(r3, true);
                        LaunchActivity.this.actionBarLayout.getView().invalidate();
                    }

                    @Override // org.telegram.ui.DialogsActivity, org.telegram.ui.ActionBar.BaseFragment
                    public void onTransitionAnimationEnd(boolean z, boolean z2) {
                        super.onTransitionAnimationEnd(z, z2);
                        if (z || !z2) {
                            return;
                        }
                        LaunchActivity.this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(false);
                        LaunchActivity.this.actionBarLayout.getView().invalidate();
                    }
                };
                r6.setCurrentAccount(accountNumber2);
                this.actionBarLayout.presentFragmentAsPreview(r6);
                this.drawerLayoutContainer.setDrawCurrentPreviewFragmentAbove(true);
                return true;
            }
            itemTouchHelper.startDrag(this.sideMenu.getChildViewHolder(view));
        }
        if (!(view instanceof DrawerActionCell)) {
            return false;
        }
        this.drawerLayoutAdapter.getId(i);
        TLRPC.TL_attachMenuBot attachMenuBot = this.drawerLayoutAdapter.getAttachMenuBot(i);
        if (attachMenuBot == null) {
            return false;
        }
        BotWebViewSheet.deleteBot(this.currentAccount, attachMenuBot.bot_id, null);
        return true;
    }

    public /* synthetic */ void lambda$onCreate$8() {
        checkSystemBarColors(true, false);
        if (getLastFragment() == null || getLastFragment().getLastStoryViewer() == null) {
            return;
        }
        getLastFragment().getLastStoryViewer().updatePlayingMode();
    }

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

    public static /* synthetic */ void lambda$onPause$138(int i) {
        ApplicationLoader.mainInterfacePausedStageQueue = true;
        ApplicationLoader.mainInterfacePausedStageQueueTime = 0L;
        if (VoIPService.getSharedInstance() == null) {
            MessagesController.getInstance(i).ignoreSetOnline = false;
        }
    }

    public /* synthetic */ void lambda$onPowerSaver$136() {
        lambda$runLinkRequest$93(new LiteModeSettingsActivity());
    }

    public static /* synthetic */ void lambda$onResume$139() {
        ApplicationLoader.mainInterfacePausedStageQueue = false;
        ApplicationLoader.mainInterfacePausedStageQueueTime = System.currentTimeMillis();
    }

    public /* synthetic */ void lambda$openForumFromLink$126(TLObject tLObject, Integer num, long j, Runnable runnable, String str, int i, int i2) {
        TLRPC.Message message;
        if (tLObject instanceof TLRPC.messages_Messages) {
            ArrayList<TLRPC.Message> arrayList = ((TLRPC.messages_Messages) tLObject).messages;
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                if (arrayList.get(i3) != null && arrayList.get(i3).id == num.intValue()) {
                    message = arrayList.get(i3);
                    break;
                }
            }
        }
        message = null;
        if (message != null) {
            int i4 = this.currentAccount;
            Integer valueOf = Integer.valueOf(message.id);
            int i5 = this.currentAccount;
            runCommentRequest(i4, null, valueOf, null, Long.valueOf(MessageObject.getTopicId(i5, message, MessagesController.getInstance(i5).isForum(message))), MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j)), runnable, str, i, i2);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", -j);
        lambda$runLinkRequest$93(TopicsFragment.getTopicsOrChat(this, bundle));
        if (runnable != null) {
            runnable.run();
        }
    }

    public /* synthetic */ void lambda$openForumFromLink$127(final Integer num, final long j, final Runnable runnable, final String str, final int i, final int i2, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda128
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openForumFromLink$126(tLObject, num, j, runnable, str, i, i2);
            }
        });
    }

    public static /* synthetic */ void lambda$openMessage$39(Browser.Progress progress) {
        if (progress != null) {
            progress.end();
        }
    }

    public static /* synthetic */ void lambda$openMessage$40(Browser.Progress progress) {
        if (progress != null) {
            progress.end();
        }
    }

    public /* synthetic */ void lambda$openMessage$41(Browser.Progress progress, TLObject tLObject, long j, int i, BaseFragment baseFragment, Bundle bundle, ChatActivity chatActivity, String str, int i2) {
        if (progress != null) {
            progress.end();
        }
        if (tLObject instanceof TLRPC.TL_messages_chats) {
            TLRPC.TL_messages_chats tL_messages_chats = (TLRPC.TL_messages_chats) tLObject;
            if (!tL_messages_chats.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(tL_messages_chats.chats, false);
                TLRPC.Chat chat = tL_messages_chats.chats.get(0);
                if (chat != null && chat.forum) {
                    openForumFromLink(-j, Integer.valueOf(i), null);
                }
                if (baseFragment == null || MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, baseFragment)) {
                    ChatActivity chatActivity2 = new ChatActivity(bundle);
                    chatActivity.setHighlightQuote(i, str, i2);
                    getActionBarLayout().presentFragment(chatActivity2);
                    return;
                }
                return;
            }
        }
        showAlertDialog(AlertsCreator.createNoAccessAlert(this, LocaleController.getString(R.string.DialogNotAvailable), LocaleController.getString(R.string.LinkNotFound), null));
    }

    public /* synthetic */ void lambda$openMessage$42(final Browser.Progress progress, final long j, final int i, final BaseFragment baseFragment, final Bundle bundle, final ChatActivity chatActivity, final String str, final int i2, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda170
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openMessage$41(progress, tLObject, j, i, baseFragment, bundle, chatActivity, str, i2);
            }
        });
    }

    public /* synthetic */ void lambda$openMessage$43(int i) {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(i, true);
    }

    public /* synthetic */ void lambda$openMessage$44(final Bundle bundle, final int i, final String str, final int i2, final long j, final Browser.Progress progress, final BaseFragment baseFragment) {
        final ChatActivity chatActivity = new ChatActivity(bundle);
        chatActivity.setHighlightQuote(i, str, i2);
        if ((AndroidUtilities.isTablet() ? this.rightActionBarLayout : getActionBarLayout()).presentFragment(chatActivity) || j >= 0) {
            if (progress != null) {
                progress.end();
                return;
            }
            return;
        }
        TLRPC.TL_channels_getChannels tL_channels_getChannels = new TLRPC.TL_channels_getChannels();
        TLRPC.TL_inputChannel tL_inputChannel = new TLRPC.TL_inputChannel();
        tL_inputChannel.channel_id = -j;
        tL_channels_getChannels.id.add(tL_inputChannel);
        final int sendRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda161
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.this.lambda$openMessage$42(progress, j, i, baseFragment, bundle, chatActivity, str, i2, tLObject, tL_error);
            }
        });
        if (progress != null) {
            progress.onCancel(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda162
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$openMessage$43(sendRequest);
                }
            });
        }
    }

    public /* synthetic */ void lambda$openMyStory$165(TLObject tLObject, int i, long j, boolean z) {
        StoriesListPlaceProvider storiesListPlaceProvider;
        TL_stories.StoryItem storyItem;
        if (tLObject instanceof TL_stories.TL_stories_stories) {
            TL_stories.TL_stories_stories tL_stories_stories = (TL_stories.TL_stories_stories) tLObject;
            int i2 = 0;
            while (true) {
                storiesListPlaceProvider = null;
                if (i2 >= tL_stories_stories.stories.size()) {
                    storyItem = null;
                    break;
                } else {
                    if (tL_stories_stories.stories.get(i2).id == i) {
                        storyItem = tL_stories_stories.stories.get(i2);
                        break;
                    }
                    i2++;
                }
            }
            if (storyItem != null) {
                storyItem.dialogId = j;
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
                ArrayList arrayList = new ArrayList();
                arrayList.add(Long.valueOf(j));
                if (z) {
                    lastFragment.getOrCreateStoryViewer().showViewsAfterOpening();
                }
                lastFragment.getOrCreateStoryViewer().open(this, storyItem, arrayList, 0, null, null, storiesListPlaceProvider2, false);
                return;
            }
        }
        BulletinFactory.global().createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.StoryNotFound)).show(false);
    }

    public /* synthetic */ void lambda$openMyStory$166(final int i, final long j, final boolean z, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda106
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openMyStory$165(tLObject, i, j, z);
            }
        });
    }

    public /* synthetic */ void lambda$openStories$167(int[] iArr, long[] jArr) {
        int i = iArr[0] - 1;
        iArr[0] = i;
        if (i == 0) {
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.storiesUpdated, new Object[0]);
            openStories(jArr, false);
        }
    }

    public static /* synthetic */ void lambda$openStories$168(TLObject tLObject, MessagesController messagesController, long j, Runnable runnable) {
        if (tLObject instanceof TL_stories.TL_stories_peerStories) {
            TL_stories.TL_stories_peerStories tL_stories_peerStories = (TL_stories.TL_stories_peerStories) tLObject;
            messagesController.putUsers(tL_stories_peerStories.users, false);
            messagesController.getStoriesController().putStories(j, tL_stories_peerStories.stories);
        }
        runnable.run();
    }

    public static /* synthetic */ void lambda$openStories$169(final MessagesController messagesController, final long j, final Runnable runnable, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda121
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$openStories$168(TLObject.this, messagesController, j, runnable);
            }
        });
    }

    public /* synthetic */ void lambda$openTopicRequest$34(TLRPC.TL_error tL_error, TLObject tLObject, int i, TLRPC.Chat chat, int i2, int i3, Runnable runnable, String str, int i4, ArrayList arrayList, int i5) {
        if (tL_error == null) {
            TLRPC.TL_messages_forumTopics tL_messages_forumTopics = (TLRPC.TL_messages_forumTopics) tLObject;
            SparseArray<TLRPC.Message> sparseArray = new SparseArray<>();
            for (int i6 = 0; i6 < tL_messages_forumTopics.messages.size(); i6++) {
                sparseArray.put(tL_messages_forumTopics.messages.get(i6).id, tL_messages_forumTopics.messages.get(i6));
            }
            MessagesController.getInstance(i).putUsers(tL_messages_forumTopics.users, false);
            MessagesController.getInstance(i).putChats(tL_messages_forumTopics.chats, false);
            MessagesController.getInstance(i).getTopicsController().processTopics(chat.id, tL_messages_forumTopics.topics, sparseArray, false, 2, -1);
            openTopicRequest(i, i2, chat, i3, MessagesController.getInstance(i).getTopicsController().findTopic(chat.id, i2), runnable, str, i4, arrayList, i5);
        }
    }

    public /* synthetic */ void lambda$openTopicRequest$35(final int i, final TLRPC.Chat chat, final int i2, final int i3, final Runnable runnable, final String str, final int i4, final ArrayList arrayList, final int i5, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda159
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$openTopicRequest$34(tL_error, tLObject, i, chat, i2, i3, runnable, str, i4, arrayList, i5);
            }
        });
    }

    public /* synthetic */ boolean lambda$processAttachMenuBot$119(TLRPC.User user, String str, int i, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i2, TopicsFragment topicsFragment) {
        String str2;
        long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(j)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j));
        } else {
            if (DialogObject.isUserDialog(j)) {
                str2 = "user_id";
            } else {
                j = -j;
                str2 = "chat_id";
            }
            bundle.putLong(str2, j);
        }
        bundle.putString("attach_bot", UserObject.getPublicUsername(user));
        if (str != null) {
            bundle.putString("attach_bot_start_command", str);
        }
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            getActionBarLayout().presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
        return true;
    }

    public /* synthetic */ void lambda$processAttachMenuBot$120(DialogsActivity dialogsActivity, BaseFragment baseFragment, TLRPC.User user, String str) {
        if (dialogsActivity == null) {
            if (baseFragment instanceof ChatActivity) {
                ((ChatActivity) baseFragment).openAttachBotLayout(user.id, str, true);
                return;
            }
            return;
        }
        if (baseFragment != null) {
            baseFragment.dismissCurrentDialog();
        }
        for (int i = 0; i < this.visibleDialogs.size(); i++) {
            if (((Dialog) this.visibleDialogs.get(i)).isShowing()) {
                ((Dialog) this.visibleDialogs.get(i)).dismiss();
            }
        }
        this.visibleDialogs.clear();
        lambda$runLinkRequest$93(dialogsActivity);
    }

    public /* synthetic */ void lambda$processAttachMenuBot$121(TLObject tLObject, int i, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC.User user, final String str) {
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda173
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$processAttachMenuBot$120(dialogsActivity, baseFragment, user, str);
                }
            });
        }
    }

    public /* synthetic */ void lambda$processAttachMenuBot$122(final int i, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC.User user, final String str, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda172
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$processAttachMenuBot$121(tLObject, i, dialogsActivity, baseFragment, user, str);
            }
        });
    }

    public /* synthetic */ void lambda$processAttachMenuBot$123(final int i, long j, final DialogsActivity dialogsActivity, final BaseFragment baseFragment, final TLRPC.User user, final String str, Boolean bool) {
        TLRPC.TL_messages_toggleBotInAttachMenu tL_messages_toggleBotInAttachMenu = new TLRPC.TL_messages_toggleBotInAttachMenu();
        tL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(j);
        tL_messages_toggleBotInAttachMenu.enabled = true;
        tL_messages_toggleBotInAttachMenu.write_allowed = true;
        ConnectionsManager.getInstance(i).sendRequest(tL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda168
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.this.lambda$processAttachMenuBot$122(i, dialogsActivity, baseFragment, user, str, tLObject, tL_error);
            }
        }, 66);
    }

    public /* synthetic */ void lambda$processAttachMenuBot$124(TLObject tLObject, final int i, String str, String str2, final TLRPC.User user, final String str3, final long j) {
        BulletinFactory of;
        int i2;
        DialogsActivity dialogsActivity;
        final TLRPC.User user2 = user;
        if (tLObject instanceof TLRPC.TL_attachMenuBotsBot) {
            TLRPC.TL_attachMenuBotsBot tL_attachMenuBotsBot = (TLRPC.TL_attachMenuBotsBot) tLObject;
            MessagesController.getInstance(i).putUsers(tL_attachMenuBotsBot.users, false);
            TLRPC.TL_attachMenuBot tL_attachMenuBot = tL_attachMenuBotsBot.bot;
            if (str != null) {
                showAttachMenuBot(tL_attachMenuBot, str, false);
                return;
            }
            ArrayList arrayList = mainFragmentsStack;
            BaseFragment baseFragment = (BaseFragment) arrayList.get(arrayList.size() - 1);
            if (AndroidUtilities.isTablet() && !(baseFragment instanceof ChatActivity)) {
                ArrayList arrayList2 = rightFragmentsStack;
                if (!arrayList2.isEmpty()) {
                    baseFragment = (BaseFragment) arrayList2.get(arrayList2.size() - 1);
                }
            }
            final BaseFragment baseFragment2 = baseFragment;
            ArrayList arrayList3 = new ArrayList();
            if (!TextUtils.isEmpty(str2)) {
                for (String str4 : str2.split(" ")) {
                    if (MediaDataController.canShowAttachMenuBotForTarget(tL_attachMenuBot, str4)) {
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
                dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda164
                    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                    public final boolean didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList4, CharSequence charSequence, boolean z, boolean z2, int i3, TopicsFragment topicsFragment) {
                        boolean lambda$processAttachMenuBot$119;
                        lambda$processAttachMenuBot$119 = LaunchActivity.this.lambda$processAttachMenuBot$119(user2, str3, i, dialogsActivity3, arrayList4, charSequence, z, z2, i3, topicsFragment);
                        return lambda$processAttachMenuBot$119;
                    }
                });
                dialogsActivity = dialogsActivity2;
            }
            if (tL_attachMenuBot.inactive) {
                AttachBotIntroTopView attachBotIntroTopView = new AttachBotIntroTopView(this);
                attachBotIntroTopView.setColor(Theme.getColor(Theme.key_chat_attachIcon));
                attachBotIntroTopView.setBackgroundColor(Theme.getColor(Theme.key_dialogTopBackground));
                attachBotIntroTopView.setAttachBot(tL_attachMenuBot);
                final DialogsActivity dialogsActivity3 = dialogsActivity;
                com.google.android.exoplayer2.util.Consumer consumer = new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda165
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        LaunchActivity.this.lambda$processAttachMenuBot$123(i, j, dialogsActivity3, baseFragment2, user, str3, (Boolean) obj);
                    }
                };
                if (!tL_attachMenuBot.request_write_access) {
                    user2 = null;
                }
                WebAppDisclaimerAlert.show(this, consumer, user2, null);
                return;
            }
            if (dialogsActivity != null) {
                if (baseFragment2 != null) {
                    baseFragment2.dismissCurrentDialog();
                }
                for (int i3 = 0; i3 < this.visibleDialogs.size(); i3++) {
                    if (((Dialog) this.visibleDialogs.get(i3)).isShowing()) {
                        ((Dialog) this.visibleDialogs.get(i3)).dismiss();
                    }
                }
                this.visibleDialogs.clear();
                lambda$runLinkRequest$93(dialogsActivity);
                return;
            }
            if (baseFragment2 instanceof ChatActivity) {
                ChatActivity chatActivity = (ChatActivity) baseFragment2;
                if (MediaDataController.canShowAttachMenuBot(tL_attachMenuBot, chatActivity.getCurrentUser() != null ? chatActivity.getCurrentUser() : chatActivity.getCurrentChat())) {
                    chatActivity.openAttachBotLayout(user2.id, str3, false);
                    return;
                } else {
                    BulletinFactory.of(baseFragment2).createErrorBulletin(LocaleController.getString(R.string.BotAlreadyAddedToAttachMenu)).show();
                    return;
                }
            }
            of = BulletinFactory.of(baseFragment2);
            i2 = R.string.BotAlreadyAddedToAttachMenu;
        } else {
            ArrayList arrayList4 = mainFragmentsStack;
            of = BulletinFactory.of((BaseFragment) arrayList4.get(arrayList4.size() - 1));
            i2 = R.string.BotCantAddToAttachMenu;
        }
        of.createErrorBulletin(LocaleController.getString(i2)).show();
    }

    public /* synthetic */ void lambda$processAttachMenuBot$125(final int i, final String str, final String str2, final TLRPC.User user, final String str3, final long j, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda146
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$processAttachMenuBot$124(tLObject, i, str, str2, user, str3, j);
            }
        });
    }

    public /* synthetic */ void lambda$processAttachedMenuBotFromShortcut$114(TLRPC.User user) {
        MessagesController.getInstance(this.currentAccount).openApp(user, 0);
    }

    public /* synthetic */ void lambda$processAttachedMenuBotFromShortcut$115(TLRPC.User user, Utilities.Callback callback) {
        MessagesController.getInstance(this.currentAccount).putUser(user, true);
        callback.run(user);
    }

    public /* synthetic */ void lambda$processAttachedMenuBotFromShortcut$116(long j, final Utilities.Callback callback) {
        final TLRPC.User user = MessagesStorage.getInstance(this.currentAccount).getUser(j);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda123
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$processAttachedMenuBotFromShortcut$115(user, callback);
            }
        });
    }

    public /* synthetic */ void lambda$processBoostDialog$117(Browser.Progress progress, Long l, TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus, ChatMessageCell chatMessageCell, Runnable runnable, ChannelBoostsController.CanApplyBoost canApplyBoost) {
        RightSlidingDialogContainer rightSlidingDialogContainer;
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
        boolean z = false;
        if (!(lastFragment instanceof ChatActivity) ? !(!(lastFragment instanceof DialogsActivity) || (rightSlidingDialogContainer = ((DialogsActivity) lastFragment).rightSlidingDialogContainer) == null || rightSlidingDialogContainer.getCurrentFragmetDialogId() != l.longValue()) : ((ChatActivity) lastFragment).getDialogId() == l.longValue()) {
            z = true;
        }
        limitReachedBottomSheet.setBoostsStats(tL_premium_boostsStatus, z);
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

    public /* synthetic */ void lambda$processBoostDialog$118(final Browser.Progress progress, final Runnable runnable, ChannelBoostsController channelBoostsController, final Long l, final ChatMessageCell chatMessageCell, final TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus) {
        if (tL_premium_boostsStatus != null) {
            channelBoostsController.userCanBoostChannel(l.longValue(), tL_premium_boostsStatus, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda92
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    LaunchActivity.this.lambda$processBoostDialog$117(progress, l, tL_premium_boostsStatus, chatMessageCell, runnable, (ChannelBoostsController.CanApplyBoost) obj);
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

    public /* synthetic */ void lambda$processWebAppBot$109(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, boolean z, Integer num, Long l, Long l2, Integer num2, String str13, HashMap hashMap, String str14, String str15, String str16, String str17, TLRPC.TL_wallPaper tL_wallPaper, String str18, String str19, String str20, boolean z2, String str21, int i2, int i3, String str22, String str23, String str24, Browser.Progress progress, boolean z3, int i4, boolean z4, String str25, boolean z5, boolean z6, boolean z7, boolean z8) {
        runLinkRequest(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, null, null, progress, z3, i4, z4, str25, z5, z6, z7, z8);
    }

    public /* synthetic */ void lambda$processWebAppBot$110(BaseFragment baseFragment, int i, TLRPC.User user, TLRPC.TL_messages_botApp tL_messages_botApp, AtomicBoolean atomicBoolean, String str, boolean z, boolean z2, boolean z3) {
        BaseFragment baseFragment2 = baseFragment;
        if (baseFragment2 == null || !isActive || isFinishing() || isDestroyed()) {
            return;
        }
        long j = user.id;
        WebViewRequestProps of = WebViewRequestProps.of(i, j, j, null, null, 3, 0, false, tL_messages_botApp.app, atomicBoolean.get(), str, user, 0, z);
        if (getBottomSheetTabs() == null || getBottomSheetTabs().tryReopenTab(of) == null) {
            SharedPrefsHelper.setWebViewConfirmShown(this.currentAccount, user.id, true);
            if (AndroidUtilities.isTablet()) {
                BotWebViewSheet botWebViewSheet = new BotWebViewSheet(this, baseFragment.getResourceProvider());
                botWebViewSheet.setWasOpenedByLinkIntent(z2);
                botWebViewSheet.setDefaultFullsize(!z);
                botWebViewSheet.setNeedsContext(false);
                botWebViewSheet.setParentActivity(this);
                botWebViewSheet.requestWebView(baseFragment2, of);
                botWebViewSheet.show();
                if (tL_messages_botApp.inactive || z3) {
                    botWebViewSheet.showJustAddedBulletin();
                    return;
                }
                return;
            }
            if (baseFragment.getParentLayout() instanceof ActionBarLayout) {
                baseFragment2 = ((ActionBarLayout) baseFragment.getParentLayout()).getSheetFragment();
            }
            BotWebViewAttachedSheet createBotViewer = baseFragment2.createBotViewer();
            createBotViewer.setWasOpenedByLinkIntent(z2);
            createBotViewer.setDefaultFullsize(!z);
            createBotViewer.setNeedsContext(false);
            createBotViewer.setParentActivity(this);
            createBotViewer.requestWebView(baseFragment2, of);
            createBotViewer.show();
            if (tL_messages_botApp.inactive || z3) {
                createBotViewer.showJustAddedBulletin();
            }
        }
    }

    public /* synthetic */ void lambda$processWebAppBot$112(Runnable runnable, final int i, final TLRPC.User user, final TLRPC.TL_messages_botApp tL_messages_botApp, final String str, final boolean z, final boolean z2, final boolean z3, boolean z4, boolean z5, Browser.Progress progress) {
        runnable.run();
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        ArrayList arrayList = mainFragmentsStack;
        BaseFragment baseFragment = (arrayList == null || arrayList.isEmpty()) ? null : (BaseFragment) arrayList.get(arrayList.size() - 1);
        final BaseFragment baseFragment2 = baseFragment;
        final Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda157
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$processWebAppBot$110(baseFragment2, i, user, tL_messages_botApp, atomicBoolean, str, z, z2, z3);
            }
        };
        if (!z4) {
            if (tL_messages_botApp.inactive && z5) {
                WebAppDisclaimerAlert.show(this, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda158
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        runnable2.run();
                    }
                }, null, progress != null ? new ChatActivity$ChatMessageCellDelegate$$ExternalSyntheticLambda11(progress) : null);
                return;
            } else if (tL_messages_botApp.request_write_access || z3) {
                AlertsCreator.createBotLaunchAlert(baseFragment, atomicBoolean, user, runnable2);
                return;
            }
        }
        runnable2.run();
    }

    public /* synthetic */ void lambda$processWebAppBot$113(final Browser.Progress progress, final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final boolean z, final Integer num, final Long l, final Long l2, final Integer num2, final String str13, final HashMap hashMap, final String str14, final String str15, final String str16, final String str17, final TLRPC.TL_wallPaper tL_wallPaper, final String str18, final String str19, final String str20, final boolean z2, final String str21, final int i2, final int i3, final String str22, final String str23, final String str24, final boolean z3, final int i4, final boolean z4, final String str25, final boolean z5, final boolean z6, final boolean z7, final boolean z8, final Runnable runnable, final TLRPC.User user, final String str26, final boolean z9, final boolean z10, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (progress != null) {
            progress.end();
        }
        if (tL_error != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda149
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$processWebAppBot$109(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, progress, z3, i4, z4, str25, z5, z6, z7, z8);
                }
            });
        } else {
            final TLRPC.TL_messages_botApp tL_messages_botApp = (TLRPC.TL_messages_botApp) tLObject;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda150
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$processWebAppBot$112(runnable, i, user, tL_messages_botApp, str26, z5, z6, z3, z9, z10, progress);
                }
            });
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(9:1|(5:3|(1:5)|6|7|(4:14|(3:16|(1:18)(1:33)|19)(4:34|(1:36)(1:(1:43))|39|40)|(2:31|32)|(2:22|29)(1:30)))|45|46|47|(1:49)|(0)|(0)(0)|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00c8, code lost:
    
        if (r23 != null) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00ca, code lost:
    
        r8.setHighlightQuote(r0, r23, r25);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00cf, code lost:
    
        r8.setHighlightMessageId(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00d9, code lost:
    
        if (r23 != null) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0105, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0106, code lost:
    
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0113 A[Catch: Exception -> 0x010f, TRY_LEAVE, TryCatch #0 {Exception -> 0x010f, blocks: (B:32:0x010b, B:22:0x0113), top: B:31:0x010b }] */
    /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x010b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runCommentRequest$32(TLObject tLObject, int i, TLRPC.Chat chat, Long l, Integer num, Integer num2, Runnable runnable, String str, int i2, int i3, TLRPC.TL_messages_getDiscussionMessage tL_messages_getDiscussionMessage, Runnable runnable2) {
        int intValue;
        if (tLObject instanceof TLRPC.TL_messages_discussionMessage) {
            TLRPC.TL_messages_discussionMessage tL_messages_discussionMessage = (TLRPC.TL_messages_discussionMessage) tLObject;
            MessagesController.getInstance(i).putUsers(tL_messages_discussionMessage.users, false);
            MessagesController.getInstance(i).putChats(tL_messages_discussionMessage.chats, false);
            ArrayList arrayList = new ArrayList();
            int size = tL_messages_discussionMessage.messages.size();
            for (int i4 = 0; i4 < size; i4++) {
                arrayList.add(new MessageObject(UserConfig.selectedAccount, tL_messages_discussionMessage.messages.get(i4), true, true));
            }
            if (!arrayList.isEmpty() || (chat.forum && l != null && l.longValue() == 1)) {
                if (chat.forum) {
                    openTopicRequest(i, (int) l.longValue(), chat, (num != null ? num : num2).intValue(), null, runnable, str, i2, arrayList, i3);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong("chat_id", -((MessageObject) arrayList.get(0)).getDialogId());
                    bundle.putInt("message_id", Math.max(1, num2.intValue()));
                    ChatActivity chatActivity = new ChatActivity(bundle);
                    chatActivity.setThreadMessages(arrayList, chat, tL_messages_getDiscussionMessage.msg_id, tL_messages_discussionMessage.read_inbox_max_id, tL_messages_discussionMessage.read_outbox_max_id, null);
                    if (num != null) {
                        intValue = num.intValue();
                    } else if (l != null) {
                        intValue = num2.intValue();
                    }
                    lambda$runLinkRequest$93(chatActivity);
                }
                if (runnable2 != null) {
                    try {
                        runnable2.run();
                    } catch (Exception e) {
                        FileLog.e(e);
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
        ArrayList arrayList2 = mainFragmentsStack;
        if (!arrayList2.isEmpty()) {
            BulletinFactory.of((BaseFragment) arrayList2.get(arrayList2.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.ChannelPostDeleted)).show();
        }
        if (runnable2 != null) {
        }
        if (runnable == null) {
        }
    }

    public /* synthetic */ void lambda$runCommentRequest$33(final int i, final TLRPC.Chat chat, final Long l, final Integer num, final Integer num2, final Runnable runnable, final String str, final int i2, final int i3, final TLRPC.TL_messages_getDiscussionMessage tL_messages_getDiscussionMessage, final Runnable runnable2, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda124
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runCommentRequest$32(tLObject, i, chat, l, num, num2, runnable, str, i2, i3, tL_messages_getDiscussionMessage, runnable2);
            }
        });
    }

    public /* synthetic */ void lambda$runImportRequest$36(TLObject tLObject, Uri uri, int i, AlertDialog alertDialog) {
        boolean z;
        if (isFinishing()) {
            return;
        }
        if (tLObject == null || this.actionBarLayout == null) {
            if (this.documentsUrisArray == null) {
                this.documentsUrisArray = new ArrayList();
            }
            this.documentsUrisArray.add(0, this.exportingChatUri);
            this.exportingChatUri = null;
            openDialogsToSend(true);
        } else {
            TLRPC.TL_messages_historyImportParsed tL_messages_historyImportParsed = (TLRPC.TL_messages_historyImportParsed) tLObject;
            Bundle bundle = new Bundle();
            bundle.putBoolean("onlySelect", true);
            bundle.putString("importTitle", tL_messages_historyImportParsed.title);
            bundle.putBoolean("allowSwitchAccount", true);
            if (tL_messages_historyImportParsed.pm) {
                bundle.putInt("dialogsType", 12);
            } else if (tL_messages_historyImportParsed.group) {
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
                        }
                        if (uri2.contains(it2.next())) {
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
            getActionBarLayout().presentFragment(dialogsActivity, !AndroidUtilities.isTablet() ? this.actionBarLayout.getFragmentStack().size() <= 1 || !(this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity) : this.layersActionBarLayout.getFragmentStack().size() <= 0 || !(this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity), false, true, false);
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$runImportRequest$37(final Uri uri, final int i, final AlertDialog alertDialog, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda120
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runImportRequest$36(tLObject, uri, i, alertDialog);
            }
        }, 2L);
    }

    public static /* synthetic */ void lambda$runImportRequest$38(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$100(final int[] iArr, final int i, final Runnable runnable, final Integer num, final Integer num2, final Long l, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda112
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$99(tLObject, iArr, i, runnable, num, num2, l);
            }
        });
    }

    public static /* synthetic */ void lambda$runLinkRequest$101(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$102(Runnable runnable, TLObject tLObject, boolean z, Long l, Browser.Progress progress, Long l2, Integer num, BaseFragment baseFragment, int i, Bundle bundle) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC.TL_messages_chats) {
            TLRPC.TL_messages_chats tL_messages_chats = (TLRPC.TL_messages_chats) tLObject;
            if (!tL_messages_chats.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(tL_messages_chats.chats, false);
                TLRPC.Chat chat = tL_messages_chats.chats.get(0);
                if (chat != null && z && ChatObject.isBoostSupported(chat)) {
                    processBoostDialog(Long.valueOf(-l.longValue()), null, progress);
                } else if (chat != null && chat.forum) {
                    long j = -l.longValue();
                    if (l2 != null) {
                        openForumFromLink(j, num, null);
                    } else {
                        openForumFromLink(j, null, null);
                    }
                }
                if (baseFragment == null || MessagesController.getInstance(i).checkCanOpenChat(bundle, baseFragment)) {
                    getActionBarLayout().presentFragment(new ChatActivity(bundle));
                    return;
                }
                return;
            }
        }
        showAlertDialog(AlertsCreator.createNoAccessAlert(this, LocaleController.getString(R.string.DialogNotAvailable), LocaleController.getString(R.string.LinkNotFound), null));
    }

    public /* synthetic */ void lambda$runLinkRequest$103(final Runnable runnable, final boolean z, final Long l, final Browser.Progress progress, final Long l2, final Integer num, final BaseFragment baseFragment, final int i, final Bundle bundle, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda129
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$102(runnable, tLObject, z, l, progress, l2, num, baseFragment, i, bundle);
            }
        });
    }

    public /* synthetic */ void lambda$runLinkRequest$104(final Bundle bundle, final Long l, int[] iArr, final Runnable runnable, final boolean z, final Browser.Progress progress, final Long l2, final Integer num, final BaseFragment baseFragment, final int i) {
        if (getActionBarLayout().presentFragment(new ChatActivity(bundle))) {
            return;
        }
        TLRPC.TL_channels_getChannels tL_channels_getChannels = new TLRPC.TL_channels_getChannels();
        TLRPC.TL_inputChannel tL_inputChannel = new TLRPC.TL_inputChannel();
        tL_inputChannel.channel_id = l.longValue();
        tL_channels_getChannels.id.add(tL_inputChannel);
        iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda115
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.this.lambda$runLinkRequest$103(runnable, z, l, progress, l2, num, baseFragment, i, bundle, tLObject, tL_error);
            }
        });
    }

    public /* synthetic */ void lambda$runLinkRequest$105(TLObject tLObject) {
        long j;
        String str;
        if (!(tLObject instanceof TLRPC.TL_account_resolvedBusinessChatLinks)) {
            showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString(R.string.BusinessLink), LocaleController.getString(R.string.BusinessLinkInvalid)));
            return;
        }
        TLRPC.TL_account_resolvedBusinessChatLinks tL_account_resolvedBusinessChatLinks = (TLRPC.TL_account_resolvedBusinessChatLinks) tLObject;
        MessagesController.getInstance(this.currentAccount).putUsers(tL_account_resolvedBusinessChatLinks.users, false);
        MessagesController.getInstance(this.currentAccount).putChats(tL_account_resolvedBusinessChatLinks.chats, false);
        MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(tL_account_resolvedBusinessChatLinks.users, tL_account_resolvedBusinessChatLinks.chats, true, true);
        Bundle bundle = new Bundle();
        TLRPC.Peer peer = tL_account_resolvedBusinessChatLinks.peer;
        if (!(peer instanceof TLRPC.TL_peerUser)) {
            if ((peer instanceof TLRPC.TL_peerChat) || (peer instanceof TLRPC.TL_peerChannel)) {
                j = peer.channel_id;
                str = "chat_id";
            }
            ChatActivity chatActivity = new ChatActivity(bundle);
            chatActivity.setResolvedChatLink(tL_account_resolvedBusinessChatLinks);
            presentFragment(chatActivity, false, true);
        }
        j = peer.user_id;
        str = "user_id";
        bundle.putLong(str, j);
        ChatActivity chatActivity2 = new ChatActivity(bundle);
        chatActivity2.setResolvedChatLink(tL_account_resolvedBusinessChatLinks);
        presentFragment(chatActivity2, false, true);
    }

    public /* synthetic */ void lambda$runLinkRequest$106(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda95
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$105(tLObject);
            }
        });
    }

    public static /* synthetic */ void lambda$runLinkRequest$107(int i, int[] iArr, Runnable runnable, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    public static /* synthetic */ void lambda$runLinkRequest$108(int i, int[] iArr, Runnable runnable) {
        ConnectionsManager.getInstance(i).cancelRequest(iArr[0], true);
        if (runnable != null) {
            runnable.run();
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$47(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, boolean z, Integer num, Long l, Long l2, Integer num2, String str13, HashMap hashMap, String str14, String str15, String str16, String str17, TLRPC.TL_wallPaper tL_wallPaper, String str18, String str19, String str20, boolean z2, String str21, int i2, String str22, String str23, String str24, String str25, String str26, Browser.Progress progress, boolean z3, int i3, boolean z4, String str27, boolean z5, boolean z6, boolean z7, boolean z8, int i4) {
        if (i4 != i) {
            switchToAccount(i4, true);
        }
        runLinkRequest(i4, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, 1, i2, str22, str23, str24, str25, str26, progress, z3, i3, z4, str27, z5, z6, z7, z8);
    }

    public static /* synthetic */ void lambda$runLinkRequest$48(Browser.Progress progress, AlertDialog alertDialog) {
        if (progress != null) {
            progress.end();
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$49(TLObject tLObject, int i, String str, TLRPC.TL_error tL_error, Runnable runnable) {
        if (tLObject instanceof TLRPC.User) {
            TLRPC.User user = (TLRPC.User) tLObject;
            MessagesController.getInstance(i).putUser(user, false);
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", user.id);
            lambda$runLinkRequest$93(new ChatActivity(bundle));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("cant import contact token. token=");
            sb.append(str);
            sb.append(" err=");
            sb.append(tL_error == null ? null : tL_error.text);
            FileLog.e(sb.toString());
            BulletinFactory.of((BaseFragment) mainFragmentsStack.get(r2.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.NoUsernameFound)).show();
        }
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$50(final int i, final String str, final Runnable runnable, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda113
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$49(tLObject, i, str, tL_error, runnable);
            }
        });
    }

    public static /* synthetic */ void lambda$runLinkRequest$51(TLObject tLObject, int i, String str, Runnable runnable) {
        ArrayList<TLRPC.Chat> arrayList;
        ArrayList<TLRPC.User> arrayList2;
        BaseFragment baseFragment = (BaseFragment) mainFragmentsStack.get(r0.size() - 1);
        try {
            if (tLObject instanceof TL_chatlists.chatlist_ChatlistInvite) {
                TL_chatlists.chatlist_ChatlistInvite chatlist_chatlistinvite = (TL_chatlists.chatlist_ChatlistInvite) tLObject;
                boolean z = chatlist_chatlistinvite instanceof TL_chatlists.TL_chatlists_chatlistInvite;
                if (z) {
                    TL_chatlists.TL_chatlists_chatlistInvite tL_chatlists_chatlistInvite = (TL_chatlists.TL_chatlists_chatlistInvite) chatlist_chatlistinvite;
                    arrayList = tL_chatlists_chatlistInvite.chats;
                    arrayList2 = tL_chatlists_chatlistInvite.users;
                } else if (chatlist_chatlistinvite instanceof TL_chatlists.TL_chatlists_chatlistInviteAlready) {
                    TL_chatlists.TL_chatlists_chatlistInviteAlready tL_chatlists_chatlistInviteAlready = (TL_chatlists.TL_chatlists_chatlistInviteAlready) chatlist_chatlistinvite;
                    arrayList = tL_chatlists_chatlistInviteAlready.chats;
                    arrayList2 = tL_chatlists_chatlistInviteAlready.users;
                } else {
                    arrayList = null;
                    arrayList2 = null;
                }
                MessagesController.getInstance(i).putChats(arrayList, false);
                MessagesController.getInstance(i).putUsers(arrayList2, false);
                if (!z || !((TL_chatlists.TL_chatlists_chatlistInvite) chatlist_chatlistinvite).peers.isEmpty()) {
                    FolderBottomSheet folderBottomSheet = new FolderBottomSheet(baseFragment, str, chatlist_chatlistinvite);
                    if (baseFragment != null) {
                        baseFragment.showDialog(folderBottomSheet);
                    } else {
                        folderBottomSheet.show();
                    }
                    runnable.run();
                    return;
                }
            }
            runnable.run();
            return;
        } catch (Exception e) {
            FileLog.e(e);
            return;
        }
        BulletinFactory.of(baseFragment).createErrorBulletin(LocaleController.getString(R.string.NoFolderFound)).show();
    }

    public static /* synthetic */ void lambda$runLinkRequest$52(final int i, final String str, final Runnable runnable, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda96
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$runLinkRequest$51(TLObject.this, i, str, runnable);
            }
        });
    }

    public static /* synthetic */ void lambda$runLinkRequest$53(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static /* synthetic */ void lambda$runLinkRequest$54(Runnable runnable, String str) {
        if (runnable == null || !"paid".equals(str)) {
            return;
        }
        runnable.run();
    }

    public static /* synthetic */ void lambda$runLinkRequest$55(Runnable runnable, PaymentFormActivity.InvoiceStatus invoiceStatus) {
        if (invoiceStatus == PaymentFormActivity.InvoiceStatus.PAID) {
            runnable.run();
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$56(TLRPC.TL_error tL_error, TLObject tLObject, TLRPC.TL_inputInvoiceSlug tL_inputInvoiceSlug, final Runnable runnable, int i, String str) {
        PaymentFormActivity paymentFormActivity;
        if (tL_error != null) {
            BulletinFactory.of((BaseFragment) mainFragmentsStack.get(r7.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.PaymentInvoiceLinkInvalid)).show();
        } else if (!isFinishing()) {
            if (tLObject instanceof TLRPC.TL_payments_paymentFormStars) {
                final Runnable runnable2 = this.navigateToPremiumGiftCallback;
                this.navigateToPremiumGiftCallback = null;
                StarsController.getInstance(this.currentAccount).openPaymentForm(null, tL_inputInvoiceSlug, (TLRPC.TL_payments_paymentFormStars) tLObject, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda125
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.lambda$runLinkRequest$53(runnable);
                    }
                }, new Utilities.Callback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda126
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        LaunchActivity.lambda$runLinkRequest$54(runnable2, (String) obj);
                    }
                });
                return;
            }
            if (tLObject instanceof TLRPC.PaymentForm) {
                TLRPC.PaymentForm paymentForm = (TLRPC.PaymentForm) tLObject;
                MessagesController.getInstance(i).putUsers(paymentForm.users, false);
                paymentFormActivity = new PaymentFormActivity(paymentForm, str, getActionBarLayout().getLastFragment());
            } else {
                paymentFormActivity = tLObject instanceof TLRPC.PaymentReceipt ? new PaymentFormActivity((TLRPC.PaymentReceipt) tLObject) : null;
            }
            if (paymentFormActivity != null) {
                final Runnable runnable3 = this.navigateToPremiumGiftCallback;
                if (runnable3 != null) {
                    this.navigateToPremiumGiftCallback = null;
                    paymentFormActivity.setPaymentFormCallback(new PaymentFormActivity.PaymentFormCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda127
                        @Override // org.telegram.ui.PaymentFormActivity.PaymentFormCallback
                        public final void onInvoiceStatusChanged(PaymentFormActivity.InvoiceStatus invoiceStatus) {
                            LaunchActivity.lambda$runLinkRequest$55(runnable3, invoiceStatus);
                        }
                    });
                }
                lambda$runLinkRequest$93(paymentFormActivity);
            }
        }
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$57(final TLRPC.TL_inputInvoiceSlug tL_inputInvoiceSlug, final Runnable runnable, final int i, final String str, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda116
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$56(tL_error, tLObject, tL_inputInvoiceSlug, runnable, i, str);
            }
        });
    }

    public /* synthetic */ void lambda$runLinkRequest$58(Runnable runnable, Long l, TL_stories.StoryItem storyItem) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        BaseFragment lastFragment = getLastFragment();
        if (storyItem == null) {
            BulletinFactory global = BulletinFactory.global();
            if (global != null) {
                global.createSimpleBulletin(R.raw.story_bomb2, LocaleController.getString(R.string.StoryNotFound)).show();
                return;
            }
            return;
        }
        if (storyItem instanceof TL_stories.TL_storyItemDeleted) {
            BulletinFactory global2 = BulletinFactory.global();
            if (global2 != null) {
                global2.createSimpleBulletin(R.raw.story_bomb1, LocaleController.getString(R.string.StoryNotFound)).show();
                return;
            }
            return;
        }
        if (lastFragment != null) {
            storyItem.dialogId = l.longValue();
            StoryViewer createOverlayStoryViewer = lastFragment.createOverlayStoryViewer();
            createOverlayStoryViewer.instantClose();
            createOverlayStoryViewer.open(this, storyItem, (StoryViewer.PlaceProvider) null);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$59(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, boolean z, Integer num, Long l, Long l2, Integer num2, String str13, HashMap hashMap, String str14, String str15, String str16, String str17, TLRPC.TL_wallPaper tL_wallPaper, String str18, String str19, String str20, boolean z2, String str21, int i2, int i3, String str22, String str23, String str24, Browser.Progress progress, boolean z3, int i4, boolean z4, String str25, boolean z5, boolean z6, boolean z7, boolean z8) {
        runLinkRequest(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, null, null, progress, z3, i4, z4, str25, z5, z6, z7, z8);
    }

    public static /* synthetic */ void lambda$runLinkRequest$60(TLObject tLObject, int i) {
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true, null);
        }
    }

    public static /* synthetic */ void lambda$runLinkRequest$61(final int i, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda171
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$runLinkRequest$60(TLObject.this, i);
            }
        });
    }

    public /* synthetic */ void lambda$runLinkRequest$62(TLRPC.TL_attachMenuBot tL_attachMenuBot, final int i, Long l, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, boolean z, Integer num, Long l2, Long l3, Integer num2, String str13, HashMap hashMap, String str14, String str15, String str16, String str17, TLRPC.TL_wallPaper tL_wallPaper, String str18, String str19, String str20, boolean z2, String str21, int i2, int i3, String str22, String str23, String str24, String str25, String str26, Browser.Progress progress, boolean z3, int i4, boolean z4, String str27, TLRPC.User user, Runnable runnable, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9, Boolean bool) {
        tL_attachMenuBot.inactive = false;
        tL_attachMenuBot.request_write_access = false;
        TLRPC.TL_messages_toggleBotInAttachMenu tL_messages_toggleBotInAttachMenu = new TLRPC.TL_messages_toggleBotInAttachMenu();
        tL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(l.longValue());
        tL_messages_toggleBotInAttachMenu.enabled = true;
        tL_messages_toggleBotInAttachMenu.write_allowed = true;
        ConnectionsManager.getInstance(i).sendRequest(tL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda156
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.lambda$runLinkRequest$61(i, tLObject, tL_error);
            }
        }, 66);
        processWebAppBot(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l2, l3, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, str25, str26, progress, z3, i4, z4, str27, user, runnable, z5, true, z6, z7, z8, z9);
    }

    public static /* synthetic */ void lambda$runLinkRequest$63(TLObject tLObject, int i) {
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            MediaDataController.getInstance(i).loadAttachMenuBots(false, true, null);
        }
    }

    public static /* synthetic */ void lambda$runLinkRequest$64(final int i, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda169
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$runLinkRequest$63(TLObject.this, i);
            }
        });
    }

    public /* synthetic */ void lambda$runLinkRequest$65(Long l, TLRPC.TL_attachMenuBot tL_attachMenuBot, AtomicBoolean atomicBoolean, final int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, boolean z, Integer num, Long l2, Long l3, Integer num2, String str13, HashMap hashMap, String str14, String str15, String str16, String str17, TLRPC.TL_wallPaper tL_wallPaper, String str18, String str19, String str20, boolean z2, String str21, int i2, int i3, String str22, String str23, String str24, String str25, String str26, Browser.Progress progress, boolean z3, int i4, boolean z4, String str27, TLRPC.User user, Runnable runnable, boolean z5, boolean z6, boolean z7, boolean z8) {
        SharedPrefsHelper.setWebViewConfirmShown(this.currentAccount, l.longValue(), true);
        tL_attachMenuBot.inactive = false;
        tL_attachMenuBot.request_write_access = true ^ atomicBoolean.get();
        TLRPC.TL_messages_toggleBotInAttachMenu tL_messages_toggleBotInAttachMenu = new TLRPC.TL_messages_toggleBotInAttachMenu();
        tL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(l.longValue());
        tL_messages_toggleBotInAttachMenu.write_allowed = atomicBoolean.get();
        ConnectionsManager.getInstance(i).sendRequest(tL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda155
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.lambda$runLinkRequest$64(i, tLObject, tL_error);
            }
        }, 66);
        processWebAppBot(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l2, l3, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, str25, str26, progress, z3, i4, z4, str27, user, runnable, false, false, z5, z6, z7, z8);
    }

    public /* synthetic */ void lambda$runLinkRequest$66(TLRPC.TL_error tL_error, final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final boolean z, final Integer num, final Long l, final Long l2, final Integer num2, final String str13, final HashMap hashMap, final String str14, final String str15, final String str16, final String str17, final TLRPC.TL_wallPaper tL_wallPaper, final String str18, final String str19, final String str20, final boolean z2, final String str21, final int i2, final int i3, final String str22, final String str23, final String str24, final Browser.Progress progress, final boolean z3, final int i4, final boolean z4, final String str25, final boolean z5, final boolean z6, final boolean z7, final boolean z8, TLObject tLObject, final Long l3, final String str26, final String str27, final TLRPC.User user, final Runnable runnable) {
        LaunchActivity launchActivity;
        ChatActivity$ChatMessageCellDelegate$$ExternalSyntheticLambda11 chatActivity$ChatMessageCellDelegate$$ExternalSyntheticLambda11;
        if (tL_error != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda143
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$runLinkRequest$59(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, progress, z3, i4, z4, str25, z5, z6, z7, z8);
                }
            });
            return;
        }
        if (tLObject instanceof TLRPC.TL_attachMenuBotsBot) {
            final TLRPC.TL_attachMenuBot tL_attachMenuBot = ((TLRPC.TL_attachMenuBotsBot) tLObject).bot;
            final boolean z9 = tL_attachMenuBot != null && (tL_attachMenuBot.show_in_side_menu || tL_attachMenuBot.show_in_attach_menu);
            if ((!tL_attachMenuBot.inactive && !tL_attachMenuBot.side_menu_disclaimer_needed) || !z9) {
                if (!tL_attachMenuBot.request_write_access && !z3) {
                    processWebAppBot(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, str26, str27, progress, z3, i4, z4, str25, user, runnable, false, false, z5, z6, z7, z8);
                    return;
                } else {
                    final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
                    AlertsCreator.createBotLaunchAlert(getLastFragment(), atomicBoolean, user, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda145
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$runLinkRequest$65(l3, tL_attachMenuBot, atomicBoolean, i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, str26, str27, progress, z3, i4, z4, str25, user, runnable, z5, z6, z7, z8);
                        }
                    });
                    return;
                }
            }
            com.google.android.exoplayer2.util.Consumer consumer = new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda144
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    LaunchActivity.this.lambda$runLinkRequest$62(tL_attachMenuBot, i, l3, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, str26, str27, progress, z3, i4, z4, str25, user, runnable, z9, z5, z6, z7, z8, (Boolean) obj);
                }
            };
            if (progress != null) {
                chatActivity$ChatMessageCellDelegate$$ExternalSyntheticLambda11 = new ChatActivity$ChatMessageCellDelegate$$ExternalSyntheticLambda11(progress);
                launchActivity = this;
            } else {
                launchActivity = this;
                chatActivity$ChatMessageCellDelegate$$ExternalSyntheticLambda11 = null;
            }
            WebAppDisclaimerAlert.show(launchActivity, consumer, null, chatActivity$ChatMessageCellDelegate$$ExternalSyntheticLambda11);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$67(final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final boolean z, final Integer num, final Long l, final Long l2, final Integer num2, final String str13, final HashMap hashMap, final String str14, final String str15, final String str16, final String str17, final TLRPC.TL_wallPaper tL_wallPaper, final String str18, final String str19, final String str20, final boolean z2, final String str21, final int i2, final int i3, final String str22, final String str23, final String str24, final Browser.Progress progress, final boolean z3, final int i4, final boolean z4, final String str25, final boolean z5, final boolean z6, final boolean z7, final boolean z8, final Long l3, final String str26, final String str27, final TLRPC.User user, final Runnable runnable, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda137
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$66(tL_error, i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, progress, z3, i4, z4, str25, z5, z6, z7, z8, tLObject, l3, str26, str27, user, runnable);
            }
        });
    }

    public /* synthetic */ boolean lambda$runLinkRequest$68(String str, int i, TLRPC.User user, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i2, TopicsFragment topicsFragment) {
        String str2;
        long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        TLRPC.TL_inputMediaGame tL_inputMediaGame = new TLRPC.TL_inputMediaGame();
        TLRPC.TL_inputGameShortName tL_inputGameShortName = new TLRPC.TL_inputGameShortName();
        tL_inputMediaGame.id = tL_inputGameShortName;
        tL_inputGameShortName.short_name = str;
        tL_inputGameShortName.bot_id = MessagesController.getInstance(i).getInputUser(user);
        SendMessagesHelper.getInstance(i).sendGame(MessagesController.getInstance(i).getInputPeer(j), tL_inputMediaGame, 0L, 0L);
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(j)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j));
        } else {
            if (DialogObject.isUserDialog(j)) {
                str2 = "user_id";
            } else {
                j = -j;
                str2 = "chat_id";
            }
            bundle.putLong(str2, j);
        }
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            getActionBarLayout().presentFragment(new ChatActivity(bundle), true, false, true, false);
        }
        return true;
    }

    public /* synthetic */ void lambda$runLinkRequest$69(int i, TLRPC.Chat chat, DialogsActivity dialogsActivity) {
        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        bundle.putLong("chat_id", chat.id);
        if (MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, dialogsActivity)) {
            presentFragment(new ChatActivity(bundle), true, false);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x010c A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$70(String str, TLRPC.TL_chatAdminRights tL_chatAdminRights, boolean z, String str2, final int i, final TLRPC.Chat chat, final DialogsActivity dialogsActivity, TLRPC.User user, long j, String str3) {
        TLRPC.TL_chatAdminRights tL_chatAdminRights2;
        TLRPC.TL_chatAdminRights tL_chatAdminRights3;
        if (str != null) {
            String[] split = str.split("\\+| ");
            tL_chatAdminRights2 = new TLRPC.TL_chatAdminRights();
            for (String str4 : split) {
                str4.hashCode();
                char c = 65535;
                switch (str4.hashCode()) {
                    case -2110462504:
                        if (str4.equals("ban_users")) {
                            c = 0;
                        }
                        switch (c) {
                            case 0:
                            case '\r':
                                tL_chatAdminRights2.ban_users = true;
                                break;
                            case 1:
                                tL_chatAdminRights2.anonymous = true;
                                break;
                            case 2:
                                tL_chatAdminRights2.change_info = true;
                                break;
                            case 3:
                                tL_chatAdminRights2.delete_messages = true;
                                break;
                            case 4:
                                tL_chatAdminRights2.edit_messages = true;
                                break;
                            case 5:
                            case 14:
                                tL_chatAdminRights2.manage_call = true;
                                break;
                            case 6:
                            case 7:
                                tL_chatAdminRights2.other = true;
                                break;
                            case '\b':
                            case '\f':
                                tL_chatAdminRights2.add_admins = true;
                                break;
                            case '\t':
                                tL_chatAdminRights2.invite_users = true;
                                break;
                            case '\n':
                                tL_chatAdminRights2.post_messages = true;
                                break;
                            case 11:
                                tL_chatAdminRights2.pin_messages = true;
                                break;
                        }
                        break;
                    case -2095811475:
                        if (str4.equals("anonymous")) {
                            c = 1;
                        }
                        switch (c) {
                        }
                        break;
                    case -1654794275:
                        if (str4.equals("change_info")) {
                            c = 2;
                        }
                        switch (c) {
                        }
                        break;
                    case -1593320096:
                        if (str4.equals("delete_messages")) {
                            c = 3;
                        }
                        switch (c) {
                        }
                        break;
                    case -939200543:
                        if (str4.equals("edit_messages")) {
                            c = 4;
                        }
                        switch (c) {
                        }
                        break;
                    case 22162680:
                        if (str4.equals("manage_call")) {
                            c = 5;
                        }
                        switch (c) {
                        }
                        break;
                    case 22169074:
                        if (str4.equals("manage_chat")) {
                            c = 6;
                        }
                        switch (c) {
                        }
                        break;
                    case 106069776:
                        if (str4.equals("other")) {
                            c = 7;
                        }
                        switch (c) {
                        }
                        break;
                    case 449085338:
                        if (str4.equals("promote_members")) {
                            c = '\b';
                        }
                        switch (c) {
                        }
                        break;
                    case 632157522:
                        if (str4.equals("invite_users")) {
                            c = '\t';
                        }
                        switch (c) {
                        }
                        break;
                    case 758599179:
                        if (str4.equals("post_messages")) {
                            c = '\n';
                        }
                        switch (c) {
                        }
                        break;
                    case 1357805750:
                        if (str4.equals("pin_messages")) {
                            c = 11;
                        }
                        switch (c) {
                        }
                        break;
                    case 1529816162:
                        if (str4.equals("add_admins")) {
                            c = '\f';
                        }
                        switch (c) {
                        }
                        break;
                    case 1542893206:
                        if (str4.equals("restrict_members")) {
                            c = '\r';
                        }
                        switch (c) {
                        }
                        break;
                    case 1641337725:
                        if (str4.equals("manage_video_chats")) {
                            c = 14;
                        }
                        switch (c) {
                        }
                        break;
                    default:
                        switch (c) {
                        }
                        break;
                }
            }
        } else {
            tL_chatAdminRights2 = null;
        }
        if (tL_chatAdminRights2 == null && tL_chatAdminRights == null) {
            tL_chatAdminRights3 = null;
        } else {
            if (tL_chatAdminRights2 != null) {
                if (tL_chatAdminRights == null) {
                    tL_chatAdminRights3 = tL_chatAdminRights2;
                } else {
                    tL_chatAdminRights.change_info = tL_chatAdminRights2.change_info || tL_chatAdminRights.change_info;
                    tL_chatAdminRights.post_messages = tL_chatAdminRights2.post_messages || tL_chatAdminRights.post_messages;
                    tL_chatAdminRights.edit_messages = tL_chatAdminRights2.edit_messages || tL_chatAdminRights.edit_messages;
                    tL_chatAdminRights.add_admins = tL_chatAdminRights2.add_admins || tL_chatAdminRights.add_admins;
                    tL_chatAdminRights.delete_messages = tL_chatAdminRights2.delete_messages || tL_chatAdminRights.delete_messages;
                    tL_chatAdminRights.ban_users = tL_chatAdminRights2.ban_users || tL_chatAdminRights.ban_users;
                    tL_chatAdminRights.invite_users = tL_chatAdminRights2.invite_users || tL_chatAdminRights.invite_users;
                    tL_chatAdminRights.pin_messages = tL_chatAdminRights2.pin_messages || tL_chatAdminRights.pin_messages;
                    tL_chatAdminRights.manage_call = tL_chatAdminRights2.manage_call || tL_chatAdminRights.manage_call;
                    tL_chatAdminRights.anonymous = tL_chatAdminRights2.anonymous || tL_chatAdminRights.anonymous;
                    tL_chatAdminRights.other = tL_chatAdminRights2.other || tL_chatAdminRights.other;
                }
            }
            tL_chatAdminRights3 = tL_chatAdminRights;
        }
        if (z && tL_chatAdminRights2 == null && !TextUtils.isEmpty(str2)) {
            MessagesController.getInstance(this.currentAccount).addUserToChat(chat.id, user, 0, str2, dialogsActivity, true, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda163
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$runLinkRequest$69(i, chat, dialogsActivity);
                }
            }, null);
            return;
        }
        ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(user.id, -j, tL_chatAdminRights3, null, null, str3, 2, true, !z, str2);
        chatRightsEditActivity.setDelegate(new ChatRightsEditActivity.ChatRightsEditActivityDelegate() { // from class: org.telegram.ui.LaunchActivity.17
            final /* synthetic */ DialogsActivity val$fragment;
            final /* synthetic */ int val$intentAccount;

            17(final DialogsActivity dialogsActivity2, final int i2) {
                r2 = dialogsActivity2;
                r3 = i2;
            }

            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didChangeOwner(TLRPC.User user2) {
            }

            @Override // org.telegram.ui.ChatRightsEditActivity.ChatRightsEditActivityDelegate
            public void didSetRights(int i2, TLRPC.TL_chatAdminRights tL_chatAdminRights4, TLRPC.TL_chatBannedRights tL_chatBannedRights, String str5) {
                r2.removeSelfFromStack();
                NotificationCenter.getInstance(r3).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
            }
        });
        getActionBarLayout().presentFragment(chatRightsEditActivity, false);
    }

    public /* synthetic */ void lambda$runLinkRequest$71(final String str, final String str2, final int i, final TLRPC.Chat chat, final DialogsActivity dialogsActivity, final TLRPC.User user, final long j, final boolean z, final TLRPC.TL_chatAdminRights tL_chatAdminRights, final String str3) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda151
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$70(str, tL_chatAdminRights, z, str2, i, chat, dialogsActivity, user, j, str3);
            }
        });
    }

    public /* synthetic */ void lambda$runLinkRequest$72(long j, int i, TLRPC.User user, String str, DialogInterface dialogInterface, int i2) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        long j2 = -j;
        bundle.putLong("chat_id", j2);
        ChatActivity chatActivity = new ChatActivity(bundle);
        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
        MessagesController.getInstance(i).addUserToChat(j2, user, 0, str, chatActivity, null);
        getActionBarLayout().presentFragment(chatActivity, true, false, true, false);
    }

    public /* synthetic */ boolean lambda$runLinkRequest$73(final int i, final TLRPC.User user, final String str, final String str2, final DialogsActivity dialogsActivity, DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i2, TopicsFragment topicsFragment) {
        TLRPC.TL_chatAdminRights tL_chatAdminRights;
        final long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
        if (chat == null || (!chat.creator && ((tL_chatAdminRights = chat.admin_rights) == null || !tL_chatAdminRights.add_admins))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            int i3 = R.string.AddBot;
            builder.setTitle(LocaleController.getString(i3));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", R.string.AddMembersAlertNamesText, UserObject.getUserName(user), chat == null ? "" : chat.title)));
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            builder.setPositiveButton(LocaleController.getString(i3), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda131
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i4) {
                    LaunchActivity.this.lambda$runLinkRequest$72(j, i, user, str2, dialogInterface, i4);
                }
            });
            builder.show();
        } else {
            MessagesController.getInstance(i).checkIsInChat(false, chat, user, new MessagesController.IsInChatCheckedCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda130
                @Override // org.telegram.messenger.MessagesController.IsInChatCheckedCallback
                public final void run(boolean z3, TLRPC.TL_chatAdminRights tL_chatAdminRights2, String str3) {
                    LaunchActivity.this.lambda$runLinkRequest$71(str, str2, i, chat, dialogsActivity, user, j, z3, tL_chatAdminRights2, str3);
                }
            });
        }
        return true;
    }

    public static /* synthetic */ void lambda$runLinkRequest$74(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$75(final int i, final Runnable runnable, final String str, final String str2, final boolean z, final String str3, final String str4, final int i2, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final String str13, final String str14, final String str15, final String str16, final boolean z2, final Integer num, final Long l, final Long l2, final Integer num2, final HashMap hashMap, final String str17, final String str18, final String str19, final String str20, final TLRPC.TL_wallPaper tL_wallPaper, final String str21, final String str22, final int i3, final int i4, final String str23, final String str24, final String str25, final Browser.Progress progress, final boolean z3, final boolean z4, final String str26, final boolean z5, final boolean z6, final boolean z7, final boolean z8, final String str27, int[] iArr, final Long l3) {
        BulletinFactory of;
        String string;
        long longValue;
        boolean z9;
        BulletinFactory of2;
        int i5;
        final TLRPC.User user;
        if (!isFinishing()) {
            if (i == 0 || l3 == null) {
                if (l3 == null || this.actionBarLayout == null || (!(str == null && str2 == null) && ((str == null || l3.longValue() <= 0) && ((str2 == null || l3.longValue() <= 0) && ((!z || l3.longValue() >= 0) && (str3 == null || l3.longValue() >= 0)))))) {
                    try {
                        BaseFragment lastFragment = getLastFragment();
                        if (lastFragment != null) {
                            if (lastFragment instanceof ChatActivity) {
                                ((ChatActivity) lastFragment).shakeContent();
                            }
                            if (AndroidUtilities.isNumeric(str5)) {
                                of = BulletinFactory.of(lastFragment);
                                string = LocaleController.getString(R.string.NoPhoneFound);
                            } else {
                                of = BulletinFactory.of(lastFragment);
                                string = LocaleController.getString(R.string.NoUsernameFound);
                            }
                            of.createErrorBulletin(string).show();
                        }
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                } else {
                    if (!TextUtils.isEmpty(str4) && (user = MessagesController.getInstance(i2).getUser(l3)) != null && user.bot) {
                        if (!user.bot_attach_menu || MediaDataController.getInstance(i2).botInAttachMenu(user.id)) {
                            processWebAppBot(i2, str5, str6, str7, str8, str9, str10, str11, str12, str13, str14, str15, str16, z2, num, l, l2, num2, str, hashMap, str17, str18, str19, str20, tL_wallPaper, str21, str22, str2, z, str3, i3, i4, str23, str24, str25, str4, str27, progress, z3, i, z4, str26, user, runnable, false, false, z5, z6, z7, z8);
                            return;
                        }
                        TLRPC.TL_messages_getAttachMenuBot tL_messages_getAttachMenuBot = new TLRPC.TL_messages_getAttachMenuBot();
                        tL_messages_getAttachMenuBot.bot = MessagesController.getInstance(i2).getInputUser(l3.longValue());
                        ConnectionsManager.getInstance(i2).sendRequest(tL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda101
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                LaunchActivity.this.lambda$runLinkRequest$67(i2, str5, str6, str7, str8, str9, str10, str11, str12, str13, str14, str15, str16, z2, num, l, l2, num2, str, hashMap, str17, str18, str19, str20, tL_wallPaper, str21, str22, str2, z, str3, i3, i4, str23, str24, str25, progress, z3, i, z4, str26, z5, z6, z7, z8, l3, str4, str27, user, runnable, tLObject, tL_error);
                            }
                        });
                        return;
                    }
                    String str28 = str16;
                    if (z4 && ChatObject.isBoostSupported(MessagesController.getInstance(i2).getChat(Long.valueOf(-l3.longValue())))) {
                        processBoostDialog(l3, runnable, progress);
                        return;
                    }
                    if (str27 != null) {
                        TLRPC.User user2 = MessagesController.getInstance(i2).getUser(l3);
                        if (user2 != null && user2.bot) {
                            MessagesController.getInstance(i2).openApp(null, user2, str27, 0, progress);
                        }
                    } else if (str23 != null && str24 == null) {
                        TLRPC.User user3 = MessagesController.getInstance(i2).getUser(l3);
                        if (user3 == null || !user3.bot) {
                            ArrayList arrayList = mainFragmentsStack;
                            of2 = BulletinFactory.of((BaseFragment) arrayList.get(arrayList.size() - 1));
                            i5 = R.string.BotSetAttachLinkNotBot;
                        } else if (user3.bot_attach_menu) {
                            processAttachMenuBot(i2, l3.longValue(), str25, user3, str23, str27);
                        } else {
                            ArrayList arrayList2 = mainFragmentsStack;
                            of2 = BulletinFactory.of((BaseFragment) arrayList2.get(arrayList2.size() - 1));
                            i5 = R.string.BotCantAddToAttachMenu;
                        }
                        of2.createErrorBulletin(LocaleController.getString(i5)).show();
                    } else if (num != null && ((num2 != null || l2 != null) && l3.longValue() < 0)) {
                        int runCommentRequest = runCommentRequest(i2, runnable, num, num2, l2, MessagesController.getInstance(i2).getChat(Long.valueOf(-l3.longValue())));
                        iArr[0] = runCommentRequest;
                        if (runCommentRequest != 0) {
                            return;
                        }
                    } else if (str != null) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("onlySelect", true);
                        bundle.putBoolean("cantSendToChannels", true);
                        bundle.putInt("dialogsType", 1);
                        bundle.putString("selectAlertString", LocaleController.getString(R.string.SendGameToText));
                        bundle.putString("selectAlertStringGroup", LocaleController.getString(R.string.SendGameToGroupText));
                        DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                        final TLRPC.User user4 = MessagesController.getInstance(i2).getUser(l3);
                        dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda102
                            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                            public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList3, CharSequence charSequence, boolean z10, boolean z11, int i6, TopicsFragment topicsFragment) {
                                boolean lambda$runLinkRequest$68;
                                lambda$runLinkRequest$68 = LaunchActivity.this.lambda$runLinkRequest$68(str, i2, user4, dialogsActivity2, arrayList3, charSequence, z10, z11, i6, topicsFragment);
                                return lambda$runLinkRequest$68;
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
                    } else if (str10 == null && str11 == null) {
                        Bundle bundle2 = new Bundle();
                        TLRPC.User user5 = MessagesController.getInstance(i2).getUser(l3);
                        long longValue2 = l3.longValue();
                        long longValue3 = l3.longValue();
                        if (longValue2 < 0) {
                            bundle2.putLong("chat_id", -longValue3);
                            longValue = l3.longValue();
                        } else {
                            bundle2.putLong("user_id", longValue3);
                            longValue = l3.longValue();
                            if (str28 != null) {
                                if (str28.startsWith("@")) {
                                    str28 = " " + str28;
                                }
                                bundle2.putString("start_text", str28);
                            }
                        }
                        if (str9 == null || user5 == null || !user5.bot) {
                            z9 = false;
                        } else {
                            bundle2.putString("botUser", str9);
                            z9 = true;
                        }
                        if (this.navigateToPremiumBot) {
                            this.navigateToPremiumBot = false;
                            bundle2.putBoolean("premium_bot", true);
                        }
                        if (num != null) {
                            bundle2.putInt("message_id", num.intValue());
                        }
                        if (str2 != null) {
                            bundle2.putString("voicechat", str2);
                        }
                        if (z) {
                            bundle2.putBoolean("videochat", true);
                        }
                        if (str3 != null) {
                            bundle2.putString("livestream", str3);
                        }
                        if (i4 >= 0) {
                            bundle2.putInt("video_timestamp", i4);
                        }
                        if (str24 != null) {
                            bundle2.putString("attach_bot", str24);
                        }
                        if (str23 != null) {
                            bundle2.putString("attach_bot_start_command", str23);
                        }
                        ArrayList arrayList3 = mainFragmentsStack;
                        BaseFragment baseFragment = (arrayList3.isEmpty() || str2 != null) ? null : (BaseFragment) arrayList3.get(arrayList3.size() - 1);
                        if (baseFragment == null || MessagesController.getInstance(i2).checkCanOpenChat(bundle2, baseFragment)) {
                            boolean z10 = (baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).getDialogId() == longValue;
                            if (z9 && z10) {
                                ((ChatActivity) baseFragment).setBotUser(str9);
                            } else if (str24 == null || !z10) {
                                long j = -longValue;
                                TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j));
                                if (z7) {
                                    try {
                                        runnable.run();
                                    } catch (Exception e2) {
                                        FileLog.e(e2);
                                    }
                                    if (isFinishing()) {
                                        return;
                                    }
                                    Bundle bundle3 = new Bundle();
                                    long longValue4 = l3.longValue();
                                    long longValue5 = l3.longValue();
                                    if (longValue4 < 0) {
                                        bundle3.putLong("chat_id", -longValue5);
                                    } else {
                                        bundle3.putLong("user_id", longValue5);
                                    }
                                    getActionBarLayout().presentFragment(new ProfileActivity(bundle3));
                                    return;
                                }
                                if (chat == null || !chat.forum) {
                                    MessagesController.getInstance(i2).ensureMessagesLoaded(longValue, num == null ? 0 : num.intValue(), new 18(runnable, str3, baseFragment, longValue, num, bundle2));
                                    return;
                                }
                                Long valueOf = (l2 != null || num == null) ? l2 : Long.valueOf(num.intValue());
                                if (valueOf != null && valueOf.longValue() != 0) {
                                    openForumFromLink(longValue, num, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda103
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LaunchActivity.lambda$runLinkRequest$74(runnable);
                                        }
                                    });
                                    return;
                                }
                                Bundle bundle4 = new Bundle();
                                bundle4.putLong("chat_id", j);
                                if (str2 != null) {
                                    bundle4.putString("voicechat", str2);
                                }
                                if (z) {
                                    bundle4.putBoolean("videochat", true);
                                }
                                lambda$runLinkRequest$93(TopicsFragment.getTopicsOrChat(this, bundle4));
                            } else {
                                ((ChatActivity) baseFragment).openAttachBotLayout(str24);
                            }
                        }
                    } else {
                        final TLRPC.User user6 = MessagesController.getInstance(i2).getUser(l3);
                        if (user6 == null || (user6.bot && user6.bot_nochats)) {
                            try {
                                ArrayList arrayList4 = mainFragmentsStack;
                                if (arrayList4.isEmpty()) {
                                    return;
                                }
                                BulletinFactory.of((BaseFragment) arrayList4.get(arrayList4.size() - 1)).createErrorBulletin(LocaleController.getString(R.string.BotCantJoinGroups)).show();
                                return;
                            } catch (Exception e3) {
                                FileLog.e(e3);
                                return;
                            }
                        }
                        Bundle bundle5 = new Bundle();
                        bundle5.putBoolean("onlySelect", true);
                        bundle5.putInt("dialogsType", 2);
                        bundle5.putBoolean("resetDelegate", false);
                        bundle5.putBoolean("closeFragment", false);
                        bundle5.putBoolean("allowGroups", str10 != null);
                        bundle5.putBoolean("allowChannels", str11 != null);
                        String str29 = TextUtils.isEmpty(str10) ? TextUtils.isEmpty(str11) ? null : str11 : str10;
                        final DialogsActivity dialogsActivity2 = new DialogsActivity(bundle5);
                        final String str30 = str29;
                        dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda104
                            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                            public final boolean didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList5, CharSequence charSequence, boolean z11, boolean z12, int i6, TopicsFragment topicsFragment) {
                                boolean lambda$runLinkRequest$73;
                                lambda$runLinkRequest$73 = LaunchActivity.this.lambda$runLinkRequest$73(i2, user6, str12, str30, dialogsActivity2, dialogsActivity3, arrayList5, charSequence, z11, z12, i6, topicsFragment);
                                return lambda$runLinkRequest$73;
                            }
                        });
                        lambda$runLinkRequest$93(dialogsActivity2);
                    }
                }
                try {
                    runnable.run();
                    return;
                } catch (Exception e4) {
                    FileLog.e(e4);
                    return;
                }
            }
            MessagesController.getInstance(this.currentAccount).getStoriesController().resolveStoryLink(l3.longValue(), i, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda100
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    LaunchActivity.this.lambda$runLinkRequest$58(runnable, l3, (TL_stories.StoryItem) obj);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$runLinkRequest$76(boolean[] zArr, DialogInterface dialogInterface) {
        zArr[0] = true;
    }

    public static /* synthetic */ void lambda$runLinkRequest$77(BaseFragment baseFragment, long j, TLRPC.Chat chat) {
        BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.stars_send, LocaleController.getString(R.string.StarsSubscriptionCompleted), AndroidUtilities.replaceTags(LocaleController.formatPluralString("StarsSubscriptionCompletedText", (int) j, chat.title))).show(true);
    }

    public /* synthetic */ void lambda$runLinkRequest$78(Long l, final long j) {
        BaseFragment safeLastFragment = getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        final ChatActivity of = ChatActivity.of(l.longValue());
        safeLastFragment.presentFragment(of);
        final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-l.longValue()));
        if (chat != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda160
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.lambda$runLinkRequest$77(BaseFragment.this, j, chat);
                }
            }, 250L);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$79(final long j, String str, final Long l) {
        if (!"paid".equals(str) || l.longValue() == 0) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda148
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$78(l, j);
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0030, code lost:
    
        if (r10.chat.has_geo != false) goto L76;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$runLinkRequest$80(TLRPC.TL_error tL_error, TLObject tLObject, int i, AlertDialog alertDialog, Runnable runnable, String str) {
        int i2;
        if (isFinishing()) {
            return;
        }
        if (tL_error != null || this.actionBarLayout == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(LocaleController.getString(R.string.AppName));
            if (tL_error.text.startsWith("FLOOD_WAIT")) {
                i2 = R.string.FloodWait;
            } else if (tL_error.text.startsWith("INVITE_HASH_EXPIRED")) {
                builder.setTitle(LocaleController.getString(R.string.ExpiredLink));
                i2 = R.string.InviteExpired;
            } else {
                i2 = R.string.JoinToGroupErrorNotExist;
            }
            builder.setMessage(LocaleController.getString(i2));
            builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
            showAlertDialog(builder);
        } else {
            TLRPC.ChatInvite chatInvite = (TLRPC.ChatInvite) tLObject;
            TLRPC.Chat chat = chatInvite.chat;
            if (chat != null) {
                if (ChatObject.isLeftFromChat(chat)) {
                    TLRPC.Chat chat2 = chatInvite.chat;
                    if (!chat2.kicked) {
                        if (!ChatObject.isPublic(chat2)) {
                            if (!(chatInvite instanceof TLRPC.TL_chatInvitePeek)) {
                            }
                        }
                    }
                }
                MessagesController.getInstance(i).putChat(chatInvite.chat, false);
                ArrayList arrayList = new ArrayList();
                arrayList.add(chatInvite.chat);
                MessagesStorage.getInstance(i).putUsersAndChats(null, arrayList, false, true);
                Bundle bundle = new Bundle();
                bundle.putLong("chat_id", chatInvite.chat.id);
                ArrayList arrayList2 = mainFragmentsStack;
                if (arrayList2.isEmpty() || MessagesController.getInstance(i).checkCanOpenChat(bundle, (BaseFragment) arrayList2.get(arrayList2.size() - 1))) {
                    final boolean[] zArr = new boolean[1];
                    alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda135
                        @Override // android.content.DialogInterface.OnCancelListener
                        public final void onCancel(DialogInterface dialogInterface) {
                            LaunchActivity.lambda$runLinkRequest$76(zArr, dialogInterface);
                        }
                    });
                    if (!chatInvite.chat.forum) {
                        MessagesController.getInstance(i).ensureMessagesLoaded(-chatInvite.chat.id, 0, new MessagesController.MessagesLoadedCallback() { // from class: org.telegram.ui.LaunchActivity.19
                            final /* synthetic */ Bundle val$args;
                            final /* synthetic */ boolean[] val$canceled;
                            final /* synthetic */ Runnable val$dismissLoading;
                            final /* synthetic */ TLRPC.ChatInvite val$invite;

                            19(Runnable runnable2, final boolean[] zArr2, Bundle bundle2, TLRPC.ChatInvite chatInvite2) {
                                r2 = runnable2;
                                r3 = zArr2;
                                r4 = bundle2;
                                r5 = chatInvite2;
                            }

                            @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
                            public void onError() {
                                if (!LaunchActivity.this.isFinishing()) {
                                    AlertsCreator.showSimpleAlert((BaseFragment) LaunchActivity.mainFragmentsStack.get(LaunchActivity.mainFragmentsStack.size() - 1), LocaleController.getString(R.string.JoinToGroupErrorNotExist));
                                }
                                try {
                                    r2.run();
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                            }

                            @Override // org.telegram.messenger.MessagesController.MessagesLoadedCallback
                            public void onMessagesLoaded(boolean z) {
                                try {
                                    r2.run();
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                                if (r3[0]) {
                                    return;
                                }
                                ChatActivity chatActivity = new ChatActivity(r4);
                                TLRPC.ChatInvite chatInvite2 = r5;
                                if (chatInvite2 instanceof TLRPC.TL_chatInvitePeek) {
                                    chatActivity.setChatInvite(chatInvite2);
                                }
                                LaunchActivity.this.getActionBarLayout().presentFragment(chatActivity);
                            }
                        });
                        return;
                    } else {
                        Bundle bundle2 = new Bundle();
                        bundle2.putLong("chat_id", chatInvite2.chat.id);
                        lambda$runLinkRequest$93(TopicsFragment.getTopicsOrChat(this, bundle2));
                    }
                }
            }
            TL_stars.TL_starsSubscriptionPricing tL_starsSubscriptionPricing = chatInvite2.subscription_pricing;
            if (tL_starsSubscriptionPricing == null || chatInvite2.can_refulfill_subscription) {
                ArrayList arrayList3 = mainFragmentsStack;
                BaseFragment baseFragment = (BaseFragment) arrayList3.get(arrayList3.size() - 1);
                baseFragment.showDialog(new JoinGroupAlert(this, chatInvite2, str, baseFragment, baseFragment instanceof ChatActivity ? ((ChatActivity) baseFragment).themeDelegate : null));
            } else {
                final long j = tL_starsSubscriptionPricing.amount;
                MessagesController.getInstance(i).putChat(chatInvite2.chat, false);
                StarsController.getInstance(this.currentAccount).subscribeTo(str, chatInvite2, new Utilities.Callback2() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda136
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        LaunchActivity.this.lambda$runLinkRequest$79(j, (String) obj, (Long) obj2);
                    }
                });
            }
        }
        try {
            runnable2.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$81(final int i, final AlertDialog alertDialog, final Runnable runnable, final String str, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda114
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$80(tL_error, tLObject, i, alertDialog, runnable, str);
            }
        });
    }

    public /* synthetic */ void lambda$runLinkRequest$82(Runnable runnable, TLRPC.TL_error tL_error, TLObject tLObject, int i) {
        if (isFinishing()) {
            return;
        }
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tL_error != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(LocaleController.getString(R.string.AppName));
            builder.setMessage(LocaleController.getString(tL_error.text.startsWith("FLOOD_WAIT") ? R.string.FloodWait : tL_error.text.equals("USERS_TOO_MUCH") ? R.string.JoinToGroupErrorFull : R.string.JoinToGroupErrorNotExist));
            builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
            showAlertDialog(builder);
            return;
        }
        if (this.actionBarLayout != null) {
            TLRPC.Updates updates = (TLRPC.Updates) tLObject;
            if (updates.chats.isEmpty()) {
                return;
            }
            TLRPC.Chat chat = updates.chats.get(0);
            chat.left = false;
            chat.kicked = false;
            MessagesController.getInstance(i).putUsers(updates.users, false);
            MessagesController.getInstance(i).putChats(updates.chats, false);
            Bundle bundle = new Bundle();
            bundle.putLong("chat_id", chat.id);
            ArrayList arrayList = mainFragmentsStack;
            if (arrayList.isEmpty() || MessagesController.getInstance(i).checkCanOpenChat(bundle, (BaseFragment) arrayList.get(arrayList.size() - 1))) {
                ChatActivity chatActivity = new ChatActivity(bundle);
                NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                getActionBarLayout().presentFragment(chatActivity, false, true, true, false);
            }
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$83(final int i, final Runnable runnable, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            MessagesController.getInstance(i).processUpdates((TLRPC.Updates) tLObject, false);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda93
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$82(runnable, tL_error, tLObject, i);
            }
        });
    }

    public /* synthetic */ boolean lambda$runLinkRequest$84(boolean z, int i, String str, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z2, boolean z3, int i2, TopicsFragment topicsFragment) {
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

    public /* synthetic */ void lambda$runLinkRequest$85(Runnable runnable, TLObject tLObject, int i, TLRPC.TL_account_authorizationForm tL_account_authorizationForm, TLRPC.TL_account_getAuthorizationForm tL_account_getAuthorizationForm, String str, String str2, String str3) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject != null) {
            MessagesController.getInstance(i).putUsers(tL_account_authorizationForm.users, false);
            lambda$runLinkRequest$93(new PassportActivity(5, tL_account_getAuthorizationForm.bot_id, tL_account_getAuthorizationForm.scope, tL_account_getAuthorizationForm.public_key, str, str2, str3, tL_account_authorizationForm, (TLRPC.account_Password) tLObject));
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$86(final Runnable runnable, final int i, final TLRPC.TL_account_authorizationForm tL_account_authorizationForm, final TLRPC.TL_account_getAuthorizationForm tL_account_getAuthorizationForm, final String str, final String str2, final String str3, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda134
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$85(runnable, tLObject, i, tL_account_authorizationForm, tL_account_getAuthorizationForm, str, str2, str3);
            }
        });
    }

    public /* synthetic */ void lambda$runLinkRequest$87(Runnable runnable, TLRPC.TL_error tL_error) {
        try {
            runnable.run();
            if ("APP_VERSION_OUTDATED".equals(tL_error.text)) {
                AlertsCreator.showUpdateAppAlert(this, LocaleController.getString(R.string.UpdateAppAlert), true);
            } else {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString(R.string.ErrorOccurred) + "\n" + tL_error.text));
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$88(int[] iArr, final int i, final Runnable runnable, final TLRPC.TL_account_getAuthorizationForm tL_account_getAuthorizationForm, final String str, final String str2, final String str3, TLObject tLObject, final TLRPC.TL_error tL_error) {
        final TLRPC.TL_account_authorizationForm tL_account_authorizationForm = (TLRPC.TL_account_authorizationForm) tLObject;
        if (tL_account_authorizationForm == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda118
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$runLinkRequest$87(runnable, tL_error);
                }
            });
        } else {
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(new TLRPC.TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda117
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error2) {
                    LaunchActivity.this.lambda$runLinkRequest$86(runnable, i, tL_account_authorizationForm, tL_account_getAuthorizationForm, str, str2, str3, tLObject2, tL_error2);
                }
            });
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$89(Runnable runnable, TLObject tLObject) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC.TL_help_deepLinkInfo) {
            TLRPC.TL_help_deepLinkInfo tL_help_deepLinkInfo = (TLRPC.TL_help_deepLinkInfo) tLObject;
            AlertsCreator.showUpdateAppAlert(this, tL_help_deepLinkInfo.message, tL_help_deepLinkInfo.update_app);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$90(final Runnable runnable, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda122
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$89(runnable, tLObject);
            }
        });
    }

    public /* synthetic */ void lambda$runLinkRequest$91(Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        String str;
        AlertDialog.Builder createSimpleAlert;
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tLObject instanceof TLRPC.TL_langPackLanguage) {
            createSimpleAlert = AlertsCreator.createLanguageAlert(this, (TLRPC.TL_langPackLanguage) tLObject);
        } else {
            if (tL_error == null) {
                return;
            }
            if ("LANG_CODE_NOT_SUPPORTED".equals(tL_error.text)) {
                str = LocaleController.getString(R.string.LanguageUnsupportedError);
            } else {
                str = LocaleController.getString(R.string.ErrorOccurred) + "\n" + tL_error.text;
            }
            createSimpleAlert = AlertsCreator.createSimpleAlert(this, str);
        }
        showAlertDialog(createSimpleAlert);
    }

    public /* synthetic */ void lambda$runLinkRequest$92(final Runnable runnable, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda110
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$91(runnable, tLObject, tL_error);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$runLinkRequest$94(Runnable runnable, TLObject tLObject, TLRPC.TL_wallPaper tL_wallPaper, TLRPC.TL_error tL_error) {
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (!(tLObject instanceof TLRPC.TL_wallPaper)) {
            showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString(R.string.ErrorOccurred) + "\n" + tL_error.text));
            return;
        }
        TLRPC.TL_wallPaper tL_wallPaper2 = (TLRPC.TL_wallPaper) tLObject;
        if (tL_wallPaper2.pattern) {
            String str = tL_wallPaper2.slug;
            TLRPC.WallPaperSettings wallPaperSettings = tL_wallPaper.settings;
            WallpapersListActivity.ColorWallpaper colorWallpaper = new WallpapersListActivity.ColorWallpaper(str, wallPaperSettings.background_color, wallPaperSettings.second_background_color, wallPaperSettings.third_background_color, wallPaperSettings.fourth_background_color, AndroidUtilities.getWallpaperRotation(wallPaperSettings.rotation, false), r0.intensity / 100.0f, tL_wallPaper.settings.motion, null);
            colorWallpaper.pattern = tL_wallPaper2;
            tL_wallPaper2 = colorWallpaper;
        }
        ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(tL_wallPaper2, null, true, false);
        TLRPC.WallPaperSettings wallPaperSettings2 = tL_wallPaper.settings;
        themePreviewActivity.setInitialModes(wallPaperSettings2.blur, wallPaperSettings2.motion, wallPaperSettings2.intensity);
        lambda$runLinkRequest$93(themePreviewActivity);
    }

    public /* synthetic */ void lambda$runLinkRequest$95(final Runnable runnable, final TLRPC.TL_wallPaper tL_wallPaper, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda90
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$94(runnable, tLObject, tL_wallPaper, tL_error);
            }
        });
    }

    public /* synthetic */ void lambda$runLinkRequest$96(Browser.Progress progress) {
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

    public /* synthetic */ void lambda$runLinkRequest$97(TLObject tLObject, AlertDialog alertDialog, Runnable runnable, TLRPC.TL_error tL_error) {
        char c;
        if (tLObject instanceof TLRPC.TL_theme) {
            TLRPC.TL_theme tL_theme = (TLRPC.TL_theme) tLObject;
            TLRPC.TL_wallPaper tL_wallPaper = null;
            c = 0;
            TLRPC.ThemeSettings themeSettings = tL_theme.settings.size() > 0 ? tL_theme.settings.get(0) : null;
            if (themeSettings != null) {
                Theme.ThemeInfo theme = Theme.getTheme(Theme.getBaseThemeKey(themeSettings));
                if (theme != null) {
                    TLRPC.WallPaper wallPaper = themeSettings.wallpaper;
                    if (wallPaper instanceof TLRPC.TL_wallPaper) {
                        tL_wallPaper = (TLRPC.TL_wallPaper) wallPaper;
                        if (!FileLoader.getInstance(this.currentAccount).getPathToAttach(tL_wallPaper.document, true).exists()) {
                            this.loadingThemeProgressDialog = alertDialog;
                            this.loadingThemeAccent = true;
                            this.loadingThemeInfo = theme;
                            this.loadingTheme = tL_theme;
                            this.loadingThemeWallpaper = tL_wallPaper;
                            this.loadingThemeWallpaperName = FileLoader.getAttachFileName(tL_wallPaper.document);
                            FileLoader.getInstance(this.currentAccount).loadFile(tL_wallPaper.document, tL_wallPaper, 1, 1);
                            return;
                        }
                    }
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    openThemeAccentPreview(tL_theme, tL_wallPaper, theme);
                }
                c = 1;
            } else {
                TLRPC.Document document = tL_theme.document;
                if (document != null) {
                    this.loadingThemeAccent = false;
                    this.loadingTheme = tL_theme;
                    this.loadingThemeFileName = FileLoader.getAttachFileName(document);
                    this.loadingThemeProgressDialog = alertDialog;
                    FileLoader.getInstance(this.currentAccount).loadFile(this.loadingTheme.document, tL_theme, 1, 1);
                }
                c = 1;
            }
        } else {
            if (tL_error == null || !"THEME_FORMAT_INVALID".equals(tL_error.text)) {
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
            showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString(R.string.Theme), LocaleController.getString(c == 1 ? R.string.ThemeNotSupported : R.string.ThemeNotFound)));
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$98(final AlertDialog alertDialog, final Runnable runnable, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda94
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$runLinkRequest$97(tLObject, alertDialog, runnable, tL_error);
            }
        });
    }

    public /* synthetic */ void lambda$runLinkRequest$99(TLObject tLObject, int[] iArr, int i, Runnable runnable, Integer num, Integer num2, Long l) {
        if (tLObject instanceof TLRPC.TL_messages_chats) {
            TLRPC.TL_messages_chats tL_messages_chats = (TLRPC.TL_messages_chats) tLObject;
            if (!tL_messages_chats.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(tL_messages_chats.chats, false);
                iArr[0] = runCommentRequest(i, runnable, num, num2, l, tL_messages_chats.chats.get(0));
                return;
            }
        }
        try {
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
        showAlertDialog(AlertsCreator.createNoAccessAlert(this, LocaleController.getString(R.string.DialogNotAvailable), LocaleController.getString(R.string.LinkNotFound), null));
    }

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

    public static /* synthetic */ void lambda$setupActionBarLayout$11(View view) {
    }

    public /* synthetic */ void lambda$showAlertDialog$133(AlertDialog alertDialog, DialogInterface dialogInterface) {
        Bulletin createSimpleBulletin;
        if (alertDialog != null) {
            if (alertDialog == this.localeDialog) {
                ActionBarLayout actionBarLayout = this.actionBarLayout;
                BaseFragment lastFragment = actionBarLayout == null ? null : actionBarLayout.getLastFragment();
                try {
                    String str = LocaleController.getInstance().getCurrentLocaleInfo().shortName;
                    if (lastFragment != null) {
                        createSimpleBulletin = BulletinFactory.of(lastFragment).createSimpleBulletin(R.raw.msg_translate, getStringForLanguageAlert(str.equals("en") ? this.englishLocaleStrings : this.systemLocaleStrings, "ChangeLanguageLater", R.string.ChangeLanguageLater));
                    } else {
                        createSimpleBulletin = BulletinFactory.of(Bulletin.BulletinWindow.make(this), null).createSimpleBulletin(R.raw.msg_translate, getStringForLanguageAlert(str.equals("en") ? this.englishLocaleStrings : this.systemLocaleStrings, "ChangeLanguageLater", R.string.ChangeLanguageLater));
                    }
                    createSimpleBulletin.setDuration(5000).show();
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

    public /* synthetic */ void lambda$showLanguageAlert$159(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
        this.systemLocaleStrings = hashMap;
        if (this.englishLocaleStrings == null || hashMap == null) {
            return;
        }
        showLanguageAlertInternal(localeInfoArr[1], localeInfoArr[0], str);
    }

    public /* synthetic */ void lambda$showLanguageAlert$160(final LocaleController.LocaleInfo[] localeInfoArr, final String str, TLObject tLObject, TLRPC.TL_error tL_error) {
        final HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC.Vector vector = (TLRPC.Vector) tLObject;
            for (int i = 0; i < vector.objects.size(); i++) {
                TLRPC.LangPackString langPackString = (TLRPC.LangPackString) vector.objects.get(i);
                hashMap.put(langPackString.key, langPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda105
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showLanguageAlert$159(hashMap, localeInfoArr, str);
            }
        });
    }

    public /* synthetic */ void lambda$showLanguageAlert$161(HashMap hashMap, LocaleController.LocaleInfo[] localeInfoArr, String str) {
        this.englishLocaleStrings = hashMap;
        if (hashMap == null || this.systemLocaleStrings == null) {
            return;
        }
        showLanguageAlertInternal(localeInfoArr[1], localeInfoArr[0], str);
    }

    public /* synthetic */ void lambda$showLanguageAlert$162(final LocaleController.LocaleInfo[] localeInfoArr, final String str, TLObject tLObject, TLRPC.TL_error tL_error) {
        final HashMap hashMap = new HashMap();
        if (tLObject != null) {
            TLRPC.Vector vector = (TLRPC.Vector) tLObject;
            for (int i = 0; i < vector.objects.size(); i++) {
                TLRPC.LangPackString langPackString = (TLRPC.LangPackString) vector.objects.get(i);
                hashMap.put(langPackString.key, langPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda111
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showLanguageAlert$161(hashMap, localeInfoArr, str);
            }
        });
    }

    public static /* synthetic */ void lambda$showLanguageAlertInternal$156(LocaleController.LocaleInfo[] localeInfoArr, LanguageCell[] languageCellArr, View view) {
        Integer num = (Integer) view.getTag();
        localeInfoArr[0] = ((LanguageCell) view).getCurrentLocale();
        int i = 0;
        while (i < languageCellArr.length) {
            languageCellArr[i].setLanguageSelected(i == num.intValue(), true);
            i++;
        }
    }

    public /* synthetic */ void lambda$showLanguageAlertInternal$157(View view) {
        this.localeDialog = null;
        this.drawerLayoutContainer.closeDrawer(true);
        lambda$runLinkRequest$93(new LanguageSelectActivity());
        for (int i = 0; i < this.visibleDialogs.size(); i++) {
            if (((Dialog) this.visibleDialogs.get(i)).isShowing()) {
                ((Dialog) this.visibleDialogs.get(i)).dismiss();
            }
        }
        this.visibleDialogs.clear();
    }

    public /* synthetic */ void lambda$showLanguageAlertInternal$158(LocaleController.LocaleInfo[] localeInfoArr, DialogInterface dialogInterface, int i) {
        LocaleController.getInstance().applyLanguage(localeInfoArr[0], true, false, this.currentAccount);
        rebuildAllFragments(true);
    }

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

    public /* synthetic */ void lambda$showPasscodeActivity$14(PasscodeView passcodeView) {
        SharedConfig.isWaitingForPasscodeEnter = false;
        Intent intent = this.passcodeSaveIntent;
        if (intent != null) {
            handleIntent(intent, this.passcodeSaveIntentIsNew, this.passcodeSaveIntentIsRestore, true, null, false, true);
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

    public static /* synthetic */ DialogsActivity lambda$switchToAccount$12(Void r1) {
        return new DialogsActivity(null);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: IfRegionVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r0v9 java.util.ArrayList, still in use, count: 2, list:
          (r0v9 java.util.ArrayList) from 0x0008: INVOKE (r0v9 java.util.ArrayList) VIRTUAL call: java.util.ArrayList.isEmpty():boolean A[MD:():boolean (c), WRAPPED]
          (r0v9 java.util.ArrayList) from 0x0017: PHI (r0v2 java.util.ArrayList) = (r0v1 java.util.ArrayList), (r0v9 java.util.ArrayList) binds: [B:17:0x0015, B:4:0x000c] A[DONT_GENERATE, DONT_INLINE]
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:151)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:116)
        	at jadx.core.dex.visitors.regions.TernaryMod.makeTernaryInsn(TernaryMod.java:114)
        	at jadx.core.dex.visitors.regions.TernaryMod.processRegion(TernaryMod.java:62)
        	at jadx.core.dex.visitors.regions.TernaryMod.enterRegion(TernaryMod.java:45)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:67)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:19)
        	at jadx.core.dex.visitors.regions.TernaryMod.process(TernaryMod.java:35)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.process(IfRegionVisitor.java:34)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.visit(IfRegionVisitor.java:30)
        */
    public /* synthetic */ void lambda$updateCurrentConnectionState$163() {
        /*
            r2 = this;
            boolean r0 = org.telegram.messenger.AndroidUtilities.isTablet()
            if (r0 == 0) goto Lf
            java.util.ArrayList r0 = org.telegram.ui.LaunchActivity.layerFragmentsStack
            boolean r1 = r0.isEmpty()
            if (r1 != 0) goto L24
            goto L17
        Lf:
            java.util.ArrayList r0 = org.telegram.ui.LaunchActivity.mainFragmentsStack
            boolean r1 = r0.isEmpty()
            if (r1 != 0) goto L24
        L17:
            int r1 = r0.size()
            int r1 = r1 + (-1)
            java.lang.Object r0 = r0.get(r1)
            org.telegram.ui.ActionBar.BaseFragment r0 = (org.telegram.ui.ActionBar.BaseFragment) r0
            goto L25
        L24:
            r0 = 0
        L25:
            boolean r1 = r0 instanceof org.telegram.ui.ProxyListActivity
            if (r1 != 0) goto L36
            boolean r0 = r0 instanceof org.telegram.ui.ProxySettingsActivity
            if (r0 == 0) goto L2e
            goto L36
        L2e:
            org.telegram.ui.ProxyListActivity r0 = new org.telegram.ui.ProxyListActivity
            r0.<init>()
            r2.lambda$runLinkRequest$93(r0)
        L36:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.LaunchActivity.lambda$updateCurrentConnectionState$163():void");
    }

    public static void makeRipple(float f, float f2, float f3) {
        LaunchActivity launchActivity = instance;
        if (launchActivity == null) {
            return;
        }
        launchActivity.makeRippleInternal(f, f2, f3);
    }

    private void makeRippleInternal(float f, float f2, float f3) {
        ISuperRipple iSuperRipple;
        ISuperRipple superRippleFallback;
        View decorView = getWindow().getDecorView();
        if (decorView == null) {
            return;
        }
        int i = Build.VERSION.SDK_INT;
        if (i >= 33) {
            ISuperRipple iSuperRipple2 = this.currentRipple;
            if (iSuperRipple2 == null || iSuperRipple2.view != decorView) {
                superRippleFallback = new SuperRipple(decorView);
                this.currentRipple = superRippleFallback;
            }
        } else if (i >= 26 && ((iSuperRipple = this.currentRipple) == null || iSuperRipple.view != decorView)) {
            superRippleFallback = new SuperRippleFallback(decorView);
            this.currentRipple = superRippleFallback;
        }
        ISuperRipple iSuperRipple3 = this.currentRipple;
        if (iSuperRipple3 != null) {
            iSuperRipple3.animate(f, f2, f3);
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
        LiteMode.removeOnPowerSaverAppliedListener(new LaunchActivity$$ExternalSyntheticLambda21(this));
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
            22 r0 = new Runnable() { // from class: org.telegram.ui.LaunchActivity.22
                22() {
                }

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
            this.lockRunnable = r0;
            if (SharedConfig.appLocked) {
                AndroidUtilities.runOnUIThread(r0, 1000L);
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

    public void onPowerSaver(boolean z) {
        BaseFragment lastFragment;
        if (Build.VERSION.SDK_INT < 21 || this.actionBarLayout == null || !z || LiteMode.getPowerSaverLevel() >= 100 || (lastFragment = this.actionBarLayout.getLastFragment()) == null || (lastFragment instanceof LiteModeSettingsActivity)) {
            return;
        }
        int batteryLevel = LiteMode.getBatteryLevel();
        BulletinFactory.of(lastFragment).createSimpleBulletin(new BatteryDrawable(batteryLevel / 100.0f, -1, lastFragment.getThemedColor(Theme.key_dialogSwipeRemove), 1.3f), LocaleController.getString(R.string.LowPowerEnabledTitle), LocaleController.formatString("LowPowerEnabledSubtitle", R.string.LowPowerEnabledSubtitle, String.format("%d%%", Integer.valueOf(batteryLevel))), LocaleController.getString(R.string.Disable), new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda64
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$onPowerSaver$136();
            }
        }).setDuration(5000).show();
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

    /* JADX WARN: Removed duplicated region for block: B:21:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0103  */
    /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void openDialogsToSend(boolean z) {
        int i;
        GroupCallActivity groupCallActivity;
        Bundle bundle = new Bundle();
        bundle.putBoolean("onlySelect", true);
        bundle.putBoolean("canSelectTopics", true);
        bundle.putInt("dialogsType", 3);
        bundle.putBoolean("allowSwitchAccount", true);
        ArrayList arrayList = this.contactsToSend;
        if (arrayList != null) {
            if (arrayList.size() != 1) {
                bundle.putString("selectAlertString", LocaleController.getString(R.string.SendMessagesToText));
                i = R.string.SendContactToGroupText;
            }
            16 r5 = new DialogsActivity(bundle) { // from class: org.telegram.ui.LaunchActivity.16
                16(Bundle bundle2) {
                    super(bundle2);
                }

                @Override // org.telegram.ui.DialogsActivity
                public boolean shouldShowNextButton(DialogsActivity dialogsActivity, ArrayList arrayList2, CharSequence charSequence, boolean z2) {
                    if (LaunchActivity.this.exportingChatUri != null) {
                        return false;
                    }
                    if (LaunchActivity.this.contactsToSend != null && LaunchActivity.this.contactsToSend.size() == 1 && !LaunchActivity.mainFragmentsStack.isEmpty()) {
                        return true;
                    }
                    if (arrayList2.size() <= 1) {
                        return LaunchActivity.this.videoPath != null || (LaunchActivity.this.photoPathsArray != null && LaunchActivity.this.photoPathsArray.size() > 0);
                    }
                    return false;
                }
            };
            r5.setDelegate(this);
            getActionBarLayout().presentFragment(r5, AndroidUtilities.isTablet() ? !(this.actionBarLayout.getFragmentStack().size() <= 1 || !(this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity)) : !(this.layersActionBarLayout.getFragmentStack().size() <= 0 || !(this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity)), !z, true, false);
            if (!SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                SecretMediaViewer.getInstance().closePhoto(false, false);
            } else if (!PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                PhotoViewer.getInstance().closePhoto(false, true);
            } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                ArticleViewer.getInstance().close(false, true);
            }
            StoryRecorder.destroyInstance();
            groupCallActivity = GroupCallActivity.groupCallInstance;
            if (groupCallActivity != null) {
                groupCallActivity.dismiss();
            }
            if (z) {
                this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
                if (!AndroidUtilities.isTablet()) {
                    this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    return;
                } else {
                    this.actionBarLayout.rebuildFragments(1);
                    this.rightActionBarLayout.rebuildFragments(1);
                    return;
                }
            }
            return;
        }
        bundle2.putString("selectAlertString", LocaleController.getString(R.string.SendMessagesToText));
        i = R.string.SendMessagesToGroupText;
        bundle2.putString("selectAlertStringGroup", LocaleController.getString(i));
        16 r52 = new DialogsActivity(bundle2) { // from class: org.telegram.ui.LaunchActivity.16
            16(Bundle bundle2) {
                super(bundle2);
            }

            @Override // org.telegram.ui.DialogsActivity
            public boolean shouldShowNextButton(DialogsActivity dialogsActivity, ArrayList arrayList2, CharSequence charSequence, boolean z2) {
                if (LaunchActivity.this.exportingChatUri != null) {
                    return false;
                }
                if (LaunchActivity.this.contactsToSend != null && LaunchActivity.this.contactsToSend.size() == 1 && !LaunchActivity.mainFragmentsStack.isEmpty()) {
                    return true;
                }
                if (arrayList2.size() <= 1) {
                    return LaunchActivity.this.videoPath != null || (LaunchActivity.this.photoPathsArray != null && LaunchActivity.this.photoPathsArray.size() > 0);
                }
                return false;
            }
        };
        r52.setDelegate(this);
        getActionBarLayout().presentFragment(r52, AndroidUtilities.isTablet() ? !(this.actionBarLayout.getFragmentStack().size() <= 1 || !(this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity)) : !(this.layersActionBarLayout.getFragmentStack().size() <= 0 || !(this.layersActionBarLayout.getFragmentStack().get(this.layersActionBarLayout.getFragmentStack().size() - 1) instanceof DialogsActivity)), !z, true, false);
        if (!SecretMediaViewer.hasInstance()) {
        }
        if (!PhotoViewer.hasInstance()) {
        }
        if (ArticleViewer.hasInstance()) {
            ArticleViewer.getInstance().close(false, true);
        }
        StoryRecorder.destroyInstance();
        groupCallActivity = GroupCallActivity.groupCallInstance;
        if (groupCallActivity != null) {
        }
        if (z) {
        }
    }

    private void openForumFromLink(long j, Integer num, Runnable runnable) {
        openForumFromLink(j, num, null, runnable, 0, -1);
    }

    private void openForumFromLink(final long j, final Integer num, final String str, final Runnable runnable, final int i, final int i2) {
        if (num != null) {
            TLRPC.TL_channels_getMessages tL_channels_getMessages = new TLRPC.TL_channels_getMessages();
            tL_channels_getMessages.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(-j);
            tL_channels_getMessages.id.add(num);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_channels_getMessages, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda91
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LaunchActivity.this.lambda$openForumFromLink$127(num, j, runnable, str, i, i2, tLObject, tL_error);
                }
            });
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", -j);
        lambda$runLinkRequest$93(TopicsFragment.getTopicsOrChat(this, bundle));
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00e4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void openMyStory(final int i, final boolean z) {
        TL_stories.StoryItem storyItem;
        StoriesController.StoriesList storiesList;
        StoriesListPlaceProvider of;
        StoriesController.StoriesList storiesList2;
        MessageObject findMessageObject;
        MessageObject findMessageObject2;
        StoriesListPlaceProvider of2;
        final long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        StoriesController storiesController = MessagesController.getInstance(this.currentAccount).getStoriesController();
        TL_stories.PeerStories stories = storiesController.getStories(clientUserId);
        if (stories != null) {
            int i2 = 0;
            while (true) {
                if (i2 >= stories.stories.size()) {
                    storyItem = null;
                    break;
                } else {
                    if (stories.stories.get(i2).id == i) {
                        storyItem = stories.stories.get(i2);
                        break;
                    }
                    i2++;
                }
            }
            if (storyItem != null) {
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
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(Long.valueOf(storyItem.dialogId));
                    if (z) {
                        lastFragment.getOrCreateStoryViewer().showViewsAfterOpening();
                    }
                    lastFragment.getOrCreateStoryViewer().open(this, storyItem, arrayList, 0, null, stories, of2, false);
                    return;
                }
                of2 = null;
                lastFragment.getOrCreateStoryViewer().instantClose();
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(Long.valueOf(storyItem.dialogId));
                if (z) {
                }
                lastFragment.getOrCreateStoryViewer().open(this, storyItem, arrayList2, 0, null, stories, of2, false);
                return;
            }
        } else {
            storyItem = null;
        }
        if (storyItem == null) {
            StoriesController.StoriesList storiesList3 = storiesController.getStoriesList(clientUserId, 0);
            if (storiesList3 == null || (findMessageObject2 = storiesList3.findMessageObject(i)) == null) {
                storiesList3 = null;
            } else {
                storyItem = findMessageObject2.storyItem;
            }
            if (storyItem != null || (storiesList2 = storiesController.getStoriesList(clientUserId, 1)) == null || (findMessageObject = storiesList2.findMessageObject(i)) == null) {
                storiesList = storiesList3;
            } else {
                TL_stories.StoryItem storyItem2 = findMessageObject.storyItem;
                storiesList = storiesList2;
                storyItem = storyItem2;
            }
            if (storyItem != null && storiesList != null) {
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
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.add(Long.valueOf(storyItem.dialogId));
                    if (z) {
                        lastFragment2.getOrCreateStoryViewer().showViewsAfterOpening();
                    }
                    lastFragment2.getOrCreateStoryViewer().open(this, storyItem, arrayList3, 0, storiesList, null, of, false);
                    return;
                }
                of = null;
                lastFragment2.getOrCreateStoryViewer().instantClose();
                ArrayList arrayList32 = new ArrayList();
                arrayList32.add(Long.valueOf(storyItem.dialogId));
                if (z) {
                }
                lastFragment2.getOrCreateStoryViewer().open(this, storyItem, arrayList32, 0, storiesList, null, of, false);
                return;
            }
        }
        TL_stories.TL_stories_getStoriesByID tL_stories_getStoriesByID = new TL_stories.TL_stories_getStoriesByID();
        tL_stories_getStoriesByID.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(clientUserId);
        tL_stories_getStoriesByID.id.add(Integer.valueOf(i));
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_stories_getStoriesByID, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda60
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.this.lambda$openMyStory$166(i, clientUserId, z, tLObject, tL_error);
            }
        });
    }

    private void openSettings(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(this.currentAccount).clientUserId);
        if (z) {
            bundle.putBoolean("expandPhoto", true);
        }
        lambda$runLinkRequest$93(new ProfileActivity(bundle));
        this.drawerLayoutContainer.closeDrawer(false);
    }

    private void openStories(long[] jArr, boolean z) {
        boolean z2;
        StoriesListPlaceProvider of;
        final long[] jArr2 = jArr;
        int i = 0;
        while (true) {
            if (i >= jArr2.length) {
                z2 = true;
                break;
            }
            TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(jArr2[i]));
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
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        if (!z2) {
            ArrayList arrayList4 = new ArrayList();
            for (int i2 = 0; i2 < jArr2.length; i2++) {
                TLRPC.User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(jArr2[i2]));
                if (user2 == null || !user2.stories_hidden) {
                    arrayList4.add(Long.valueOf(jArr2[i2]));
                }
            }
            jArr2 = Longs.toArray(arrayList4);
        }
        int i3 = 0;
        if (z) {
            while (i3 < jArr2.length) {
                arrayList3.add(Long.valueOf(jArr2[i3]));
                i3++;
            }
        } else {
            while (i3 < jArr2.length) {
                arrayList2.add(Long.valueOf(jArr2[i3]));
                i3++;
            }
        }
        if (!arrayList3.isEmpty() && z) {
            final MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
            final int[] iArr = {arrayList3.size()};
            final Runnable runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda46
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$openStories$167(iArr, jArr2);
                }
            };
            for (int i4 = 0; i4 < arrayList3.size(); i4++) {
                final long longValue = ((Long) arrayList3.get(i4)).longValue();
                TL_stories.TL_stories_getPeerStories tL_stories_getPeerStories = new TL_stories.TL_stories_getPeerStories();
                TLRPC.InputPeer inputPeer = messagesController.getInputPeer(longValue);
                tL_stories_getPeerStories.peer = inputPeer;
                if (inputPeer instanceof TLRPC.TL_inputPeerEmpty) {
                    iArr[0] = iArr[0] - 1;
                } else if (inputPeer == null) {
                    iArr[0] = iArr[0] - 1;
                } else {
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_stories_getPeerStories, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda47
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            LaunchActivity.lambda$openStories$169(MessagesController.this, longValue, runnable, tLObject, tL_error);
                        }
                    });
                }
            }
            return;
        }
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        for (int i5 = 0; i5 < arrayList.size(); i5++) {
            long peerDialogId = DialogObject.getPeerDialogId(((TL_stories.PeerStories) arrayList.get(i5)).peer);
            if (peerDialogId != clientUserId && !arrayList2.contains(Long.valueOf(peerDialogId)) && storiesController.hasUnreadStories(peerDialogId)) {
                arrayList2.add(Long.valueOf(peerDialogId));
            }
        }
        if (arrayList2.isEmpty()) {
            return;
        }
        if (lastFragment instanceof DialogsActivity) {
            try {
                of = StoriesListPlaceProvider.of(((DialogsActivity) lastFragment).dialogStoriesCell.recyclerListView);
            } catch (Exception unused) {
            }
            StoriesListPlaceProvider storiesListPlaceProvider = of;
            lastFragment.getOrCreateStoryViewer().instantClose();
            lastFragment.getOrCreateStoryViewer().open(this, null, arrayList2, 0, null, null, storiesListPlaceProvider, false);
        }
        of = null;
        StoriesListPlaceProvider storiesListPlaceProvider2 = of;
        lastFragment.getOrCreateStoryViewer().instantClose();
        lastFragment.getOrCreateStoryViewer().open(this, null, arrayList2, 0, null, null, storiesListPlaceProvider2, false);
    }

    private void openThemeAccentPreview(TLRPC.TL_theme tL_theme, TLRPC.TL_wallPaper tL_wallPaper, Theme.ThemeInfo themeInfo) {
        int i = themeInfo.lastAccentId;
        Theme.ThemeAccent createNewAccent = themeInfo.createNewAccent(tL_theme, this.currentAccount);
        themeInfo.prevAccentId = themeInfo.currentAccentId;
        themeInfo.setCurrentAccentId(createNewAccent.id);
        createNewAccent.pattern = tL_wallPaper;
        lambda$runLinkRequest$93(new ThemePreviewActivity(themeInfo, i != themeInfo.lastAccentId, 0, false, false));
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x011c  */
    /* JADX WARN: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void openTopicRequest(final int i, final int i2, final TLRPC.Chat chat, final int i3, TLRPC.TL_forumTopic tL_forumTopic, final Runnable runnable, final String str, final int i4, final ArrayList arrayList, final int i5) {
        TLRPC.TL_forumTopic findTopic = tL_forumTopic == null ? MessagesController.getInstance(i).getTopicsController().findTopic(chat.id, i2) : tL_forumTopic;
        if (findTopic == null) {
            TLRPC.TL_channels_getForumTopicsByID tL_channels_getForumTopicsByID = new TLRPC.TL_channels_getForumTopicsByID();
            tL_channels_getForumTopicsByID.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(chat.id);
            tL_channels_getForumTopicsByID.topics.add(Integer.valueOf(i2));
            ConnectionsManager.getInstance(i).sendRequest(tL_channels_getForumTopicsByID, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda147
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LaunchActivity.this.lambda$openTopicRequest$35(i, chat, i2, i3, runnable, str, i4, arrayList, i5, tLObject, tL_error);
                }
            });
            return;
        }
        ArrayList arrayList2 = mainFragmentsStack;
        BaseFragment baseFragment = !arrayList2.isEmpty() ? (BaseFragment) arrayList2.get(arrayList2.size() - 1) : null;
        if (baseFragment instanceof ChatActivity) {
            ChatActivity chatActivity = (ChatActivity) baseFragment;
            if (chatActivity.getDialogId() == (-chat.id) && chatActivity.isTopic && chatActivity.getTopicId() == findTopic.id) {
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
        bundle.putLong("chat_id", chat.id);
        if (i3 != findTopic.id) {
            bundle.putInt("message_id", Math.max(1, i3));
        }
        ChatActivity chatActivity2 = new ChatActivity(bundle);
        if (arrayList.isEmpty()) {
            TLRPC.Message message = new TLRPC.Message();
            message.id = 1;
            message.action = new TLRPC.TL_messageActionChannelMigrateFrom();
            arrayList.add(new MessageObject(i, message, false, false));
        }
        chatActivity2.setThreadMessages(arrayList, chat, i3, findTopic.read_inbox_max_id, findTopic.read_outbox_max_id, findTopic);
        if (i3 != findTopic.id) {
            if (str != null) {
                chatActivity2.setHighlightQuote(i3, str, i5);
            } else {
                chatActivity2.setHighlightMessageId(i3);
            }
            chatActivity2.scrollToMessageId(i3, i4, true, 0, true, 0, null);
        }
        lambda$runLinkRequest$93(chatActivity2);
        if (runnable == null) {
        }
    }

    private void processAttachMenuBot(final int i, final long j, final String str, final TLRPC.User user, final String str2, final String str3) {
        TLRPC.TL_messages_getAttachMenuBot tL_messages_getAttachMenuBot = new TLRPC.TL_messages_getAttachMenuBot();
        tL_messages_getAttachMenuBot.bot = MessagesController.getInstance(i).getInputUser(j);
        ConnectionsManager.getInstance(i).sendRequest(tL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda132
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.this.lambda$processAttachMenuBot$125(i, str3, str, user, str2, j, tLObject, tL_error);
            }
        });
    }

    private void processAttachedMenuBotFromShortcut(final long j) {
        for (int i = 0; i < this.visibleDialogs.size(); i++) {
            if (this.visibleDialogs.get(i) instanceof BotWebViewSheet) {
                BotWebViewSheet botWebViewSheet = (BotWebViewSheet) this.visibleDialogs.get(i);
                if (botWebViewSheet.isShowing() && botWebViewSheet.getBotId() == j) {
                    return;
                }
            }
        }
        BaseFragment safeLastFragment = getSafeLastFragment();
        if (safeLastFragment != null && safeLastFragment.sheetsStack != null) {
            for (int i2 = 0; i2 < safeLastFragment.sheetsStack.size(); i2++) {
                if (((BaseFragment.AttachedSheet) safeLastFragment.sheetsStack.get(i2)).isShown() && (safeLastFragment.sheetsStack.get(i2) instanceof BotWebViewAttachedSheet) && ((BotWebViewAttachedSheet) safeLastFragment.sheetsStack.get(i2)).getBotId() == j) {
                    return;
                }
            }
        }
        EmptyBaseFragment sheetFragment = this.actionBarLayout.getSheetFragment(false);
        if (sheetFragment != null && sheetFragment.sheetsStack != null) {
            for (int i3 = 0; i3 < sheetFragment.sheetsStack.size(); i3++) {
                if (((BaseFragment.AttachedSheet) sheetFragment.sheetsStack.get(i3)).isShown() && (sheetFragment.sheetsStack.get(i3) instanceof BotWebViewAttachedSheet) && ((BotWebViewAttachedSheet) sheetFragment.sheetsStack.get(i3)).getBotId() == j) {
                    return;
                }
            }
        }
        final Utilities.Callback callback = new Utilities.Callback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda55
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                LaunchActivity.this.lambda$processAttachedMenuBotFromShortcut$114((TLRPC.User) obj);
            }
        };
        TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j));
        if (user != null) {
            callback.run(user);
        } else {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda56
                @Override // java.lang.Runnable
                public final void run() {
                    LaunchActivity.this.lambda$processAttachedMenuBotFromShortcut$116(j, callback);
                }
            });
        }
    }

    private void processBoostDialog(Long l, Runnable runnable, Browser.Progress progress) {
        processBoostDialog(l, runnable, progress, null);
    }

    private void processBoostDialog(final Long l, final Runnable runnable, final Browser.Progress progress, final ChatMessageCell chatMessageCell) {
        final ChannelBoostsController boostsController = MessagesController.getInstance(this.currentAccount).getBoostsController();
        if (progress != null) {
            progress.init();
        }
        boostsController.getBoostsStats(l.longValue(), new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda59
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                LaunchActivity.this.lambda$processBoostDialog$118(progress, runnable, boostsController, l, chatMessageCell, (TL_stories.TL_premium_boostsStatus) obj);
            }
        });
    }

    private void processWebAppBot(final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final boolean z, final Integer num, final Long l, final Long l2, final Integer num2, final String str13, final HashMap hashMap, final String str14, final String str15, final String str16, final String str17, final TLRPC.TL_wallPaper tL_wallPaper, final String str18, final String str19, final String str20, final boolean z2, final String str21, final int i2, final int i3, final String str22, final String str23, final String str24, String str25, final String str26, final Browser.Progress progress, final boolean z3, final int i4, final boolean z4, final String str27, final TLRPC.User user, final Runnable runnable, final boolean z5, final boolean z6, final boolean z7, final boolean z8, final boolean z9, final boolean z10) {
        TLRPC.TL_messages_getBotApp tL_messages_getBotApp = new TLRPC.TL_messages_getBotApp();
        TLRPC.TL_inputBotAppShortName tL_inputBotAppShortName = new TLRPC.TL_inputBotAppShortName();
        tL_inputBotAppShortName.bot_id = MessagesController.getInstance(i).getInputUser(user);
        tL_inputBotAppShortName.short_name = str25;
        tL_messages_getBotApp.app = tL_inputBotAppShortName;
        ConnectionsManager.getInstance(i).sendRequest(tL_messages_getBotApp, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda141
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.this.lambda$processWebAppBot$113(progress, i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, i2, i3, str22, str23, str24, z3, i4, z4, str27, z7, z8, z9, z10, runnable, user, str26, z6, z5, tLObject, tL_error);
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x004b, code lost:
    
        r8 = new java.io.BufferedReader(new java.io.InputStreamReader(r0));
        r5 = new java.lang.StringBuilder();
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x005a, code lost:
    
        r6 = r8.readLine();
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x005e, code lost:
    
        if (r6 == null) goto L219;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0060, code lost:
    
        if (r3 >= 100) goto L218;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0062, code lost:
    
        r5.append(r6);
        r5.append('\n');
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x006d, code lost:
    
        r4 = r5.toString();
     */
    /* JADX WARN: Removed duplicated region for block: B:91:0x00e4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String readImport(Uri uri) {
        InputStream inputStream;
        String fixFileName = FileLoader.fixFileName(MediaController.getFileName(uri));
        int i = 0;
        InputStream inputStream2 = null;
        r4 = null;
        r4 = null;
        r4 = null;
        r4 = null;
        r4 = null;
        String str = null;
        if (fixFileName != null && fixFileName.endsWith(".zip")) {
            try {
                ZipInputStream zipInputStream = new ZipInputStream(getContentResolver().openInputStream(uri));
                while (true) {
                    try {
                        ZipEntry nextEntry = zipInputStream.getNextEntry();
                        if (nextEntry == null) {
                            break;
                        }
                        String name = nextEntry.getName();
                        if (name != null) {
                            int lastIndexOf = name.lastIndexOf("/");
                            if (lastIndexOf >= 0) {
                                name = name.substring(lastIndexOf + 1);
                            }
                            if (name.endsWith(".txt")) {
                                try {
                                    break;
                                } catch (Exception e) {
                                    FileLog.e(e);
                                    zipInputStream.close();
                                    return null;
                                }
                            }
                        }
                    } finally {
                    }
                }
                zipInputStream.closeEntry();
                zipInputStream.close();
            } catch (Exception e2) {
                try {
                    FileLog.e(e2);
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
            }
            return str;
        }
        try {
            inputStream = getContentResolver().openInputStream(uri);
            try {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null || i >= 100) {
                            break;
                        }
                        sb.append(readLine);
                        sb.append('\n');
                        i++;
                    }
                    String sb2 = sb.toString();
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e4) {
                            FileLog.e(e4);
                        }
                    }
                    return sb2;
                } catch (Exception e5) {
                    e = e5;
                    FileLog.e(e);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e6) {
                            FileLog.e(e6);
                        }
                    }
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                inputStream2 = inputStream;
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (Exception e7) {
                        FileLog.e(e7);
                    }
                }
                throw th;
            }
        } catch (Exception e8) {
            e = e8;
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
            if (inputStream2 != null) {
            }
            throw th;
        }
    }

    private int runCommentRequest(int i, Runnable runnable, Integer num, Integer num2, Long l, TLRPC.Chat chat) {
        return runCommentRequest(i, runnable, num, num2, l, chat, null, null, 0, -1);
    }

    private int runCommentRequest(final int i, final Runnable runnable, final Integer num, final Integer num2, final Long l, final TLRPC.Chat chat, final Runnable runnable2, final String str, final int i2, final int i3) {
        if (chat == null) {
            return 0;
        }
        final TLRPC.TL_messages_getDiscussionMessage tL_messages_getDiscussionMessage = new TLRPC.TL_messages_getDiscussionMessage();
        tL_messages_getDiscussionMessage.peer = MessagesController.getInputPeer(chat);
        tL_messages_getDiscussionMessage.msg_id = num2 != null ? num.intValue() : (int) l.longValue();
        return ConnectionsManager.getInstance(i).sendRequest(tL_messages_getDiscussionMessage, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda109
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.this.lambda$runCommentRequest$33(i, chat, l, num2, num, runnable2, str, i2, i3, tL_messages_getDiscussionMessage, runnable, tLObject, tL_error);
            }
        });
    }

    private void runImportRequest(final Uri uri, ArrayList arrayList) {
        final int i = UserConfig.selectedAccount;
        final AlertDialog alertDialog = new AlertDialog(this, 3);
        final int[] iArr = {0};
        String readImport = readImport(uri);
        if (readImport == null) {
            return;
        }
        TLRPC.TL_messages_checkHistoryImport tL_messages_checkHistoryImport = new TLRPC.TL_messages_checkHistoryImport();
        tL_messages_checkHistoryImport.import_head = readImport;
        iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tL_messages_checkHistoryImport, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda53
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                LaunchActivity.this.lambda$runImportRequest$37(uri, i, alertDialog, tLObject, tL_error);
            }
        });
        final Runnable runnable = null;
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda54
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                LaunchActivity.lambda$runImportRequest$38(i, iArr, runnable, dialogInterface);
            }
        });
        try {
            alertDialog.showDelayed(300L);
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x05df  */
    /* JADX WARN: Removed duplicated region for block: B:40:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void runLinkRequest(final int i, final String str, final String str2, final String str3, final String str4, final String str5, final String str6, final String str7, final String str8, final String str9, final String str10, final String str11, final String str12, final boolean z, final Integer num, final Long l, final Long l2, final Integer num2, final String str13, final HashMap hashMap, final String str14, final String str15, final String str16, final String str17, final TLRPC.TL_wallPaper tL_wallPaper, final String str18, final String str19, final String str20, final boolean z2, final String str21, final int i2, final int i3, final String str22, final String str23, final String str24, final String str25, final String str26, final Browser.Progress progress, final boolean z3, final int i4, final boolean z4, final String str27, final boolean z5, final boolean z6, final boolean z7, final boolean z8) {
        final int i5;
        final int[] iArr;
        char c;
        Runnable runnable;
        final Browser.Progress progress2;
        final Runnable runnable2;
        EmojiPacksAlert emojiPacksAlert;
        StickersAlert stickersAlert;
        if (i2 == 0 && UserConfig.getActivatedAccountsCount() >= 2 && hashMap != null) {
            AlertsCreator.createAccountSelectDialog(this, new AlertsCreator.AccountSelectDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda67
                @Override // org.telegram.ui.Components.AlertsCreator.AccountSelectDelegate
                public final void didSelectAccount(int i6) {
                    LaunchActivity.this.lambda$runLinkRequest$47(i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, str13, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, str20, z2, str21, i3, str22, str23, str24, str25, str26, progress, z3, i4, z4, str27, z5, z6, z7, z8, i6);
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
            builder.setTitle(LocaleController.getString(R.string.AppName));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.OtherLoginCode, str16)));
            builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
            showAlertDialog(builder);
            return;
        }
        if (str17 != null) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(LocaleController.getString(R.string.AuthAnotherClient));
            builder2.setMessage(LocaleController.getString(R.string.AuthAnotherClientUrl));
            builder2.setPositiveButton(LocaleController.getString(R.string.OK), null);
            showAlertDialog(builder2);
            return;
        }
        final AlertDialog alertDialog = new AlertDialog(this, 3);
        final Runnable runnable3 = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda78
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$runLinkRequest$48(Browser.Progress.this, alertDialog);
            }
        };
        final int[] iArr2 = {0};
        if (str10 != null) {
            TLRPC.TL_contacts_importContactToken tL_contacts_importContactToken = new TLRPC.TL_contacts_importContactToken();
            tL_contacts_importContactToken.token = str10;
            iArr2[0] = ConnectionsManager.getInstance(i).sendRequest(tL_contacts_importContactToken, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda81
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LaunchActivity.this.lambda$runLinkRequest$50(i, str10, runnable3, tLObject, tL_error);
                }
            });
        } else if (str11 != null) {
            TL_chatlists.TL_chatlists_checkChatlistInvite tL_chatlists_checkChatlistInvite = new TL_chatlists.TL_chatlists_checkChatlistInvite();
            tL_chatlists_checkChatlistInvite.slug = str11;
            iArr2[0] = ConnectionsManager.getInstance(i).sendRequest(tL_chatlists_checkChatlistInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda82
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LaunchActivity.lambda$runLinkRequest$52(i, str11, runnable3, tLObject, tL_error);
                }
            });
        } else if (str18 != null) {
            TLRPC.TL_payments_getPaymentForm tL_payments_getPaymentForm = new TLRPC.TL_payments_getPaymentForm();
            final TLRPC.TL_inputInvoiceSlug tL_inputInvoiceSlug = new TLRPC.TL_inputInvoiceSlug();
            tL_inputInvoiceSlug.slug = str18;
            tL_payments_getPaymentForm.invoice = tL_inputInvoiceSlug;
            iArr2[0] = ConnectionsManager.getInstance(i).sendRequest(tL_payments_getPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda83
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    LaunchActivity.this.lambda$runLinkRequest$57(tL_inputInvoiceSlug, runnable3, i, str18, tLObject, tL_error);
                }
            });
        } else {
            if (str != null) {
                if (progress != null) {
                    progress.init();
                }
                MessagesController.getInstance(i).getUserNameResolver().resolve(str, new com.google.android.exoplayer2.util.Consumer() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda84
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        LaunchActivity.this.lambda$runLinkRequest$75(i4, runnable3, str13, str20, z2, str21, str25, i, str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, z, num, l, l2, num2, hashMap, str14, str15, str16, str17, tL_wallPaper, str18, str19, i2, i3, str22, str23, str24, progress, z3, z4, str27, z5, z6, z7, z8, str26, iArr2, (Long) obj);
                    }
                });
                i5 = i;
                iArr = iArr2;
                alertDialog = alertDialog;
                progress2 = progress;
                runnable = null;
                c = 0;
                runnable2 = runnable;
                if (iArr[c] == 0) {
                    alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda79
                        @Override // android.content.DialogInterface.OnCancelListener
                        public final void onCancel(DialogInterface dialogInterface) {
                            LaunchActivity.lambda$runLinkRequest$107(i5, iArr, runnable2, dialogInterface);
                        }
                    });
                    if (progress2 != null) {
                        progress2.onCancel(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda80
                            @Override // java.lang.Runnable
                            public final void run() {
                                LaunchActivity.lambda$runLinkRequest$108(i5, iArr, runnable2);
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
            if (str2 != null) {
                if (i2 == 0) {
                    TLRPC.TL_messages_checkChatInvite tL_messages_checkChatInvite = new TLRPC.TL_messages_checkChatInvite();
                    tL_messages_checkChatInvite.hash = str2;
                    iArr = iArr2;
                    c = 0;
                    iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tL_messages_checkChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda85
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            LaunchActivity.this.lambda$runLinkRequest$81(i, alertDialog, runnable3, str2, tLObject, tL_error);
                        }
                    }, 2);
                } else {
                    iArr = iArr2;
                    c = 0;
                    if (i2 == 1) {
                        TLRPC.TL_messages_importChatInvite tL_messages_importChatInvite = new TLRPC.TL_messages_importChatInvite();
                        tL_messages_importChatInvite.hash = str2;
                        i5 = i;
                        ConnectionsManager.getInstance(i).sendRequest(tL_messages_importChatInvite, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda86
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                LaunchActivity.this.lambda$runLinkRequest$83(i5, runnable3, tLObject, tL_error);
                            }
                        }, 2);
                        alertDialog = alertDialog;
                        progress2 = progress;
                        runnable = null;
                    }
                }
                i5 = i;
                alertDialog = alertDialog;
                progress2 = progress;
                runnable = null;
            } else {
                i5 = i;
                String str28 = str3;
                iArr = iArr2;
                c = 0;
                if (str28 != null) {
                    ArrayList arrayList = mainFragmentsStack;
                    if (arrayList.isEmpty()) {
                        return;
                    }
                    TLRPC.TL_inputStickerSetShortName tL_inputStickerSetShortName = new TLRPC.TL_inputStickerSetShortName();
                    tL_inputStickerSetShortName.short_name = str28;
                    BaseFragment baseFragment = (BaseFragment) arrayList.get(arrayList.size() - 1);
                    if (baseFragment instanceof ChatActivity) {
                        ChatActivity chatActivity = (ChatActivity) baseFragment;
                        stickersAlert = new StickersAlert(this, baseFragment, tL_inputStickerSetShortName, null, chatActivity.getChatActivityEnterViewForStickers(), chatActivity.getResourceProvider(), false);
                        stickersAlert.setCalcMandatoryInsets(chatActivity.isKeyboardVisible());
                    } else {
                        stickersAlert = new StickersAlert(this, baseFragment, tL_inputStickerSetShortName, null, null, false);
                    }
                    stickersAlert.probablyEmojis = str4 != null;
                    baseFragment.showDialog(stickersAlert);
                    return;
                }
                if (str4 != null) {
                    ArrayList arrayList2 = mainFragmentsStack;
                    if (arrayList2.isEmpty()) {
                        return;
                    }
                    TLRPC.TL_inputStickerSetShortName tL_inputStickerSetShortName2 = new TLRPC.TL_inputStickerSetShortName();
                    if (str28 == null) {
                        str28 = str4;
                    }
                    tL_inputStickerSetShortName2.short_name = str28;
                    ArrayList arrayList3 = new ArrayList(1);
                    arrayList3.add(tL_inputStickerSetShortName2);
                    BaseFragment baseFragment2 = (BaseFragment) arrayList2.get(arrayList2.size() - 1);
                    if (baseFragment2 instanceof ChatActivity) {
                        ChatActivity chatActivity2 = (ChatActivity) baseFragment2;
                        emojiPacksAlert = new EmojiPacksAlert(baseFragment2, this, chatActivity2.getResourceProvider(), arrayList3);
                        emojiPacksAlert.setCalcMandatoryInsets(chatActivity2.isKeyboardVisible());
                    } else {
                        emojiPacksAlert = new EmojiPacksAlert(baseFragment2, this, null, arrayList3);
                    }
                    baseFragment2.showDialog(emojiPacksAlert);
                    return;
                }
                runnable = null;
                if (str9 != null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("onlySelect", true);
                    bundle.putInt("dialogsType", 3);
                    DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                    dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda87
                        @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                        public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList4, CharSequence charSequence, boolean z9, boolean z10, int i7, TopicsFragment topicsFragment) {
                            boolean lambda$runLinkRequest$84;
                            lambda$runLinkRequest$84 = LaunchActivity.this.lambda$runLinkRequest$84(z, i5, str9, dialogsActivity2, arrayList4, charSequence, z9, z10, i7, topicsFragment);
                            return lambda$runLinkRequest$84;
                        }
                    });
                    presentFragment(dialogsActivity, false, true);
                } else if (hashMap != null) {
                    long longValue = Utilities.parseLong((String) hashMap.get("bot_id")).longValue();
                    if (longValue == 0) {
                        return;
                    }
                    final String str29 = (String) hashMap.get("payload");
                    final String str30 = (String) hashMap.get("nonce");
                    final String str31 = (String) hashMap.get("callback_url");
                    final TLRPC.TL_account_getAuthorizationForm tL_account_getAuthorizationForm = new TLRPC.TL_account_getAuthorizationForm();
                    tL_account_getAuthorizationForm.bot_id = longValue;
                    tL_account_getAuthorizationForm.scope = (String) hashMap.get("scope");
                    tL_account_getAuthorizationForm.public_key = (String) hashMap.get("public_key");
                    iArr[0] = ConnectionsManager.getInstance(i).sendRequest(tL_account_getAuthorizationForm, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda88
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            LaunchActivity.this.lambda$runLinkRequest$88(iArr, i, runnable3, tL_account_getAuthorizationForm, str29, str30, str31, tLObject, tL_error);
                        }
                    });
                } else if (str15 != null) {
                    TLRPC.TL_help_getDeepLinkInfo tL_help_getDeepLinkInfo = new TLRPC.TL_help_getDeepLinkInfo();
                    tL_help_getDeepLinkInfo.path = str15;
                    iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_help_getDeepLinkInfo, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda68
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            LaunchActivity.this.lambda$runLinkRequest$90(runnable3, tLObject, tL_error);
                        }
                    });
                } else if (str14 != null) {
                    TLRPC.TL_langpack_getLanguage tL_langpack_getLanguage = new TLRPC.TL_langpack_getLanguage();
                    tL_langpack_getLanguage.lang_code = str14;
                    tL_langpack_getLanguage.lang_pack = "android";
                    iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_langpack_getLanguage, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda69
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            LaunchActivity.this.lambda$runLinkRequest$92(runnable3, tLObject, tL_error);
                        }
                    });
                } else if (tL_wallPaper != null) {
                    if (TextUtils.isEmpty(tL_wallPaper.slug)) {
                        try {
                            TLRPC.WallPaperSettings wallPaperSettings = tL_wallPaper.settings;
                            int i7 = wallPaperSettings.third_background_color;
                            final ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(i7 != 0 ? new WallpapersListActivity.ColorWallpaper("c", wallPaperSettings.background_color, wallPaperSettings.second_background_color, i7, wallPaperSettings.fourth_background_color) : new WallpapersListActivity.ColorWallpaper("c", wallPaperSettings.background_color, wallPaperSettings.second_background_color, AndroidUtilities.getWallpaperRotation(wallPaperSettings.rotation, false)), null, true, false);
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda70
                                @Override // java.lang.Runnable
                                public final void run() {
                                    LaunchActivity.this.lambda$runLinkRequest$93(themePreviewActivity);
                                }
                            });
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                    TLRPC.TL_account_getWallPaper tL_account_getWallPaper = new TLRPC.TL_account_getWallPaper();
                    TLRPC.TL_inputWallPaperSlug tL_inputWallPaperSlug = new TLRPC.TL_inputWallPaperSlug();
                    tL_inputWallPaperSlug.slug = tL_wallPaper.slug;
                    tL_account_getWallPaper.wallpaper = tL_inputWallPaperSlug;
                    iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda71
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            LaunchActivity.this.lambda$runLinkRequest$95(runnable3, tL_wallPaper, tLObject, tL_error);
                        }
                    });
                } else if (str19 != null) {
                    progress2 = progress;
                    runnable2 = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda72
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$runLinkRequest$96(progress2);
                        }
                    };
                    TLRPC.TL_account_getTheme tL_account_getTheme = new TLRPC.TL_account_getTheme();
                    tL_account_getTheme.format = "android";
                    TLRPC.TL_inputThemeSlug tL_inputThemeSlug = new TLRPC.TL_inputThemeSlug();
                    tL_inputThemeSlug.slug = str19;
                    tL_account_getTheme.theme = tL_inputThemeSlug;
                    alertDialog = alertDialog;
                    iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_getTheme, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda73
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            LaunchActivity.this.lambda$runLinkRequest$98(alertDialog, runnable3, tLObject, tL_error);
                        }
                    });
                    if (iArr[c] == 0) {
                    }
                } else {
                    alertDialog = alertDialog;
                    progress2 = progress;
                    if (l == null || (num == null && !z4)) {
                        if (str27 != null) {
                            TLRPC.TL_account_resolveBusinessChatLink tL_account_resolveBusinessChatLink = new TLRPC.TL_account_resolveBusinessChatLink();
                            tL_account_resolveBusinessChatLink.slug = str27;
                            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_resolveBusinessChatLink, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda77
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$106(tLObject, tL_error);
                                }
                            });
                        }
                    } else if (l2 != null) {
                        TLRPC.Chat chat = MessagesController.getInstance(i).getChat(l);
                        if (chat != null) {
                            iArr[0] = runCommentRequest(i, runnable3, num, num2, l2, chat);
                        } else {
                            TLRPC.TL_channels_getChannels tL_channels_getChannels = new TLRPC.TL_channels_getChannels();
                            TLRPC.TL_inputChannel tL_inputChannel = new TLRPC.TL_inputChannel();
                            tL_inputChannel.channel_id = l.longValue();
                            tL_channels_getChannels.id.add(tL_inputChannel);
                            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_channels_getChannels, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda74
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    LaunchActivity.this.lambda$runLinkRequest$100(iArr, i, runnable3, num, num2, l2, tLObject, tL_error);
                                }
                            });
                        }
                    } else {
                        final Bundle bundle2 = new Bundle();
                        bundle2.putLong("chat_id", l.longValue());
                        if (num != null) {
                            bundle2.putInt("message_id", num.intValue());
                        }
                        TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(l);
                        if (chat2 != null && ChatObject.isBoostSupported(chat2) && z4) {
                            processBoostDialog(Long.valueOf(-l.longValue()), runnable3, progress2);
                        } else if (chat2 == null || !chat2.forum) {
                            ArrayList arrayList4 = mainFragmentsStack;
                            BaseFragment baseFragment3 = !arrayList4.isEmpty() ? (BaseFragment) arrayList4.get(arrayList4.size() - 1) : null;
                            if (baseFragment3 == null || MessagesController.getInstance(i).checkCanOpenChat(bundle2, baseFragment3)) {
                                final BaseFragment baseFragment4 = baseFragment3;
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda76
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        LaunchActivity.this.lambda$runLinkRequest$104(bundle2, l, iArr, runnable3, z4, progress, l2, num, baseFragment4, i);
                                    }
                                });
                            }
                        } else {
                            openForumFromLink(-l.longValue(), num, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda75
                                @Override // java.lang.Runnable
                                public final void run() {
                                    LaunchActivity.lambda$runLinkRequest$101(runnable3);
                                }
                            });
                        }
                    }
                }
                alertDialog = alertDialog;
                progress2 = progress;
            }
            runnable2 = runnable;
            if (iArr[c] == 0) {
            }
        }
        iArr = iArr2;
        progress2 = progress;
        runnable = null;
        i5 = i;
        c = 0;
        runnable2 = runnable;
        if (iArr[c] == 0) {
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
            10 r2 = new RelativeLayout(this) { // from class: org.telegram.ui.LaunchActivity.10
                private boolean inLayout;
                private Path path = new Path();

                10(Context this) {
                    super(this);
                    this.path = new Path();
                }

                @Override // android.view.ViewGroup, android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    if (LaunchActivity.this.layersActionBarLayout != null) {
                        LaunchActivity.this.layersActionBarLayout.parentDraw(this, canvas);
                    }
                    super.dispatchDraw(canvas);
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

                @Override // android.widget.RelativeLayout, android.view.View, android.view.ViewParent
                public void requestLayout() {
                    if (this.inLayout) {
                        return;
                    }
                    super.requestLayout();
                }
            };
            this.launchLayout = r2;
            if (indexOfChild != -1) {
                this.drawerLayoutContainer.addView(r2, indexOfChild, LayoutHelper.createFrame(-1, -1.0f));
            } else {
                this.drawerLayoutContainer.addView(r2, LayoutHelper.createFrame(-1, -1.0f));
            }
            11 r0 = new SizeNotifierFrameLayout(this) { // from class: org.telegram.ui.LaunchActivity.11
                11(Context this) {
                    super(this);
                }

                @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
                protected boolean isActionBarVisible() {
                    return false;
                }
            };
            this.backgroundTablet = r0;
            r0.setOccupyStatusBar(false);
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
            ArrayList arrayList = layerFragmentsStack;
            frameLayout2.setVisibility(arrayList.isEmpty() ? 8 : 0);
            this.shadowTablet.setBackgroundColor(2130706432);
            this.launchLayout.addView(this.shadowTablet);
            this.shadowTablet.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda48
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view2, MotionEvent motionEvent) {
                    boolean lambda$setupActionBarLayout$10;
                    lambda$setupActionBarLayout$10 = LaunchActivity.this.lambda$setupActionBarLayout$10(view2, motionEvent);
                    return lambda$setupActionBarLayout$10;
                }
            });
            this.shadowTablet.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda49
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
            this.layersActionBarLayout.setFragmentStack(arrayList);
            this.layersActionBarLayout.setDelegate(this);
            this.layersActionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
            ViewGroup view2 = this.layersActionBarLayout.getView();
            view2.setBackgroundResource(R.drawable.popup_fixed_alert3);
            view2.setVisibility(arrayList.isEmpty() ? 8 : 0);
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

    private void showAttachMenuBot(TLRPC.TL_attachMenuBot tL_attachMenuBot, String str, boolean z) {
        this.drawerLayoutContainer.closeDrawer();
        BaseFragment lastFragment = getLastFragment();
        if (lastFragment == null) {
            return;
        }
        int i = this.currentAccount;
        long j = tL_attachMenuBot.bot_id;
        WebViewRequestProps of = WebViewRequestProps.of(i, j, j, tL_attachMenuBot.short_name, null, 1, 0, false, null, false, str, null, 2, false);
        if (getBottomSheetTabs() == null || getBottomSheetTabs().tryReopenTab(of) == null) {
            if (AndroidUtilities.isTablet()) {
                BotWebViewSheet botWebViewSheet = new BotWebViewSheet(this, lastFragment.getResourceProvider());
                botWebViewSheet.setNeedsContext(false);
                botWebViewSheet.setDefaultFullsize(z);
                botWebViewSheet.setParentActivity(this);
                botWebViewSheet.requestWebView(lastFragment, of);
                botWebViewSheet.show();
                return;
            }
            BotWebViewAttachedSheet createBotViewer = (lastFragment.getParentLayout() instanceof ActionBarLayout ? ((ActionBarLayout) lastFragment.getParentLayout()).getSheetFragment() : lastFragment).createBotViewer();
            createBotViewer.setNeedsContext(false);
            createBotViewer.setDefaultFullsize(z);
            createBotViewer.setParentActivity(this);
            createBotViewer.requestWebView(lastFragment, of);
            createBotViewer.show();
        }
    }

    private void showLanguageAlert(boolean z) {
        LocaleController.LocaleInfo localeInfo;
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            try {
                if (!this.loadingLocaleDialog && !ApplicationLoader.mainInterfacePaused) {
                    String string = MessagesController.getGlobalMainSettings().getString("language_showed2", "");
                    final String str = MessagesController.getInstance(this.currentAccount).suggestedLangCode;
                    if (!z && string.equals(str)) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("alert already showed for " + string);
                            return;
                        }
                        return;
                    }
                    final LocaleController.LocaleInfo[] localeInfoArr = new LocaleController.LocaleInfo[2];
                    String str2 = str.contains("-") ? str.split("-")[0] : str;
                    String str3 = "in".equals(str2) ? "id" : "iw".equals(str2) ? "he" : "jw".equals(str2) ? "jv" : null;
                    for (int i = 0; i < LocaleController.getInstance().languages.size(); i++) {
                        LocaleController.LocaleInfo localeInfo2 = LocaleController.getInstance().languages.get(i);
                        if (localeInfo2.shortName.equals("en")) {
                            localeInfoArr[0] = localeInfo2;
                        }
                        if (localeInfo2.shortName.replace("_", "-").equals(str) || localeInfo2.shortName.equals(str2) || localeInfo2.shortName.equals(str3)) {
                            localeInfoArr[1] = localeInfo2;
                        }
                        if (localeInfoArr[0] != null && localeInfoArr[1] != null) {
                            break;
                        }
                    }
                    LocaleController.LocaleInfo localeInfo3 = localeInfoArr[0];
                    if (localeInfo3 != null && (localeInfo = localeInfoArr[1]) != null && localeInfo3 != localeInfo) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("show lang alert for " + localeInfoArr[0].getKey() + " and " + localeInfoArr[1].getKey());
                        }
                        this.systemLocaleStrings = null;
                        this.englishLocaleStrings = null;
                        this.loadingLocaleDialog = true;
                        TLRPC.TL_langpack_getStrings tL_langpack_getStrings = new TLRPC.TL_langpack_getStrings();
                        tL_langpack_getStrings.lang_code = localeInfoArr[1].getLangCode();
                        tL_langpack_getStrings.keys.add("English");
                        tL_langpack_getStrings.keys.add("ChooseYourLanguage");
                        tL_langpack_getStrings.keys.add("ChooseYourLanguageOther");
                        tL_langpack_getStrings.keys.add("ChangeLanguageLater");
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_langpack_getStrings, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda43
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                LaunchActivity.this.lambda$showLanguageAlert$160(localeInfoArr, str, tLObject, tL_error);
                            }
                        }, 8);
                        TLRPC.TL_langpack_getStrings tL_langpack_getStrings2 = new TLRPC.TL_langpack_getStrings();
                        tL_langpack_getStrings2.lang_code = localeInfoArr[0].getLangCode();
                        tL_langpack_getStrings2.keys.add("English");
                        tL_langpack_getStrings2.keys.add("ChooseYourLanguage");
                        tL_langpack_getStrings2.keys.add("ChooseYourLanguageOther");
                        tL_langpack_getStrings2.keys.add("ChangeLanguageLater");
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_langpack_getStrings2, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda44
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                LaunchActivity.this.lambda$showLanguageAlert$162(localeInfoArr, str, tLObject, tL_error);
                            }
                        }, 8);
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x006d A[Catch: Exception -> 0x001e, TryCatch #0 {Exception -> 0x001e, blocks: (B:3:0x0009, B:5:0x0011, B:9:0x0022, B:14:0x005b, B:17:0x0066, B:19:0x006d, B:22:0x0080, B:26:0x00a2, B:31:0x00bd), top: B:2:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0053  */
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
                HashMap hashMap = this.systemLocaleStrings;
                int i2 = R.string.ChooseYourLanguage;
                builder.setTitle(getStringForLanguageAlert(hashMap, "ChooseYourLanguage", i2));
                builder.setSubtitle(getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguage", i2));
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(1);
                final LanguageCell[] languageCellArr = new LanguageCell[2];
                String stringForLanguageAlert = getStringForLanguageAlert(this.systemLocaleStrings, "English", R.string.English);
                LocaleController.LocaleInfo[] localeInfoArr = {!z ? localeInfo3 : localeInfo2, !z ? localeInfo2 : localeInfo3};
                if (z) {
                    localeInfo3 = localeInfo2;
                }
                final LocaleController.LocaleInfo[] localeInfoArr2 = {localeInfo3};
                i = 0;
                while (i < 2) {
                    LanguageCell languageCell = new LanguageCell(this);
                    languageCellArr[i] = languageCell;
                    LocaleController.LocaleInfo localeInfo4 = localeInfoArr[i];
                    languageCell.setLanguage(localeInfo4, localeInfo4 == localeInfo2 ? stringForLanguageAlert : null, true);
                    languageCellArr[i].setTag(Integer.valueOf(i));
                    languageCellArr[i].setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                    languageCellArr[i].setLanguageSelected(i == 0, false);
                    linearLayout.addView(languageCellArr[i], LayoutHelper.createLinear(-1, 50));
                    languageCellArr[i].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda138
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LaunchActivity.lambda$showLanguageAlertInternal$156(localeInfoArr2, languageCellArr, view);
                        }
                    });
                    i++;
                }
                LanguageCell languageCell2 = new LanguageCell(this);
                HashMap hashMap2 = this.systemLocaleStrings;
                int i3 = R.string.ChooseYourLanguageOther;
                languageCell2.setValue(getStringForLanguageAlert(hashMap2, "ChooseYourLanguageOther", i3), getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", i3));
                languageCell2.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
                languageCell2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda139
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LaunchActivity.this.lambda$showLanguageAlertInternal$157(view);
                    }
                });
                linearLayout.addView(languageCell2, LayoutHelper.createLinear(-1, 50));
                builder.setView(linearLayout);
                builder.setNegativeButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda140
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        LaunchActivity.this.lambda$showLanguageAlertInternal$158(localeInfoArr2, dialogInterface, i4);
                    }
                });
                this.localeDialog = showAlertDialog(builder);
                MessagesController.getGlobalMainSettings().edit().putString("language_showed2", str).commit();
            }
            z = true;
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            HashMap hashMap3 = this.systemLocaleStrings;
            int i22 = R.string.ChooseYourLanguage;
            builder2.setTitle(getStringForLanguageAlert(hashMap3, "ChooseYourLanguage", i22));
            builder2.setSubtitle(getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguage", i22));
            LinearLayout linearLayout2 = new LinearLayout(this);
            linearLayout2.setOrientation(1);
            final LanguageCell[] languageCellArr2 = new LanguageCell[2];
            String stringForLanguageAlert2 = getStringForLanguageAlert(this.systemLocaleStrings, "English", R.string.English);
            LocaleController.LocaleInfo[] localeInfoArr3 = {!z ? localeInfo3 : localeInfo2, !z ? localeInfo2 : localeInfo3};
            if (z) {
            }
            final LocaleController.LocaleInfo[] localeInfoArr22 = {localeInfo3};
            i = 0;
            while (i < 2) {
            }
            LanguageCell languageCell22 = new LanguageCell(this);
            HashMap hashMap22 = this.systemLocaleStrings;
            int i32 = R.string.ChooseYourLanguageOther;
            languageCell22.setValue(getStringForLanguageAlert(hashMap22, "ChooseYourLanguageOther", i32), getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", i32));
            languageCell22.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 2));
            languageCell22.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda139
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LaunchActivity.this.lambda$showLanguageAlertInternal$157(view);
                }
            });
            linearLayout2.addView(languageCell22, LayoutHelper.createLinear(-1, 50));
            builder2.setView(linearLayout2);
            builder2.setNegativeButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda140
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i4) {
                    LaunchActivity.this.lambda$showLanguageAlertInternal$158(localeInfoArr22, dialogInterface, i4);
                }
            });
            this.localeDialog = showAlertDialog(builder2);
            MessagesController.getGlobalMainSettings().edit().putString("language_showed2", str).commit();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private void showTosActivity(int i, TLRPC.TL_help_termsOfService tL_help_termsOfService) {
        if (this.termsOfServiceView == null) {
            TermsOfServiceView termsOfServiceView = new TermsOfServiceView(this);
            this.termsOfServiceView = termsOfServiceView;
            termsOfServiceView.setAlpha(0.0f);
            this.drawerLayoutContainer.addView(this.termsOfServiceView, LayoutHelper.createFrame(-1, -1.0f));
            this.termsOfServiceView.setDelegate(new 15());
        }
        TLRPC.TL_help_termsOfService tL_help_termsOfService2 = UserConfig.getInstance(i).unacceptedTermsOfService;
        if (tL_help_termsOfService2 != tL_help_termsOfService && (tL_help_termsOfService2 == null || !tL_help_termsOfService2.id.data.equals(tL_help_termsOfService.id.data))) {
            UserConfig.getInstance(i).unacceptedTermsOfService = tL_help_termsOfService;
            UserConfig.getInstance(i).saveConfig(false);
        }
        this.termsOfServiceView.show(i, tL_help_termsOfService);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        this.termsOfServiceView.animate().alpha(1.0f).setDuration(150L).setInterpolator(AndroidUtilities.decelerateInterpolator).setListener(null).start();
    }

    private void showUpdateActivity(int i, TLRPC.TL_help_appUpdate tL_help_appUpdate, boolean z) {
        if (this.blockingUpdateView == null) {
            14 r0 = new BlockingUpdateView(this) { // from class: org.telegram.ui.LaunchActivity.14
                14(Context this) {
                    super(this);
                }

                @Override // org.telegram.ui.Components.BlockingUpdateView, android.view.View
                public void setVisibility(int i2) {
                    super.setVisibility(i2);
                    if (i2 == 8) {
                        LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    }
                }
            };
            this.blockingUpdateView = r0;
            this.drawerLayoutContainer.addView(r0, LayoutHelper.createFrame(-1, -1.0f));
        }
        this.blockingUpdateView.show(i, tL_help_appUpdate, z);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
    }

    private void showVoiceChatTooltip(int i) {
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        if (sharedInstance == null || mainFragmentsStack.isEmpty() || sharedInstance.groupCall == null) {
            return;
        }
        TLRPC.Chat chat = sharedInstance.getChat();
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
        lambda$runLinkRequest$93(new IntroActivity().setOnLogout());
    }

    private void updateCurrentConnectionState(int i) {
        int i2;
        String str;
        if (this.actionBarLayout == null) {
            return;
        }
        int connectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
        this.currentConnectionState = connectionState;
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
            i2 = 0;
            str = null;
        }
        this.actionBarLayout.setTitleOverlayText(str, i2, (connectionState == 1 || connectionState == 4) ? new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$updateCurrentConnectionState$163();
            }
        } : null);
    }

    public void addOnUserLeaveHintListener(Runnable runnable) {
        this.onUserLeaveHintListeners.add(runnable);
    }

    public void addOverlayPasscodeView(PasscodeView passcodeView) {
        this.overlayPasscodeViews.add(passcodeView);
    }

    public boolean allowShowFingerprintDialog(PasscodeView passcodeView) {
        PasscodeViewDialog passcodeViewDialog;
        if (!this.overlayPasscodeViews.isEmpty() || (passcodeViewDialog = this.passcodeDialog) == null) {
            List list = this.overlayPasscodeViews;
            if (list.get(list.size() - 1) == passcodeView) {
                return true;
            }
        } else if (passcodeView == passcodeViewDialog.passcodeView) {
            return true;
        }
        return false;
    }

    public void animateNavigationBarColor(int i) {
        ValueAnimator ofArgb;
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        ValueAnimator valueAnimator = this.navBarAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.navBarAnimator = null;
        }
        ofArgb = ValueAnimator.ofArgb(getNavigationBarColor(), i);
        this.navBarAnimator = ofArgb;
        ofArgb.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda51
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                LaunchActivity.this.lambda$animateNavigationBarColor$164(valueAnimator2);
            }
        });
        this.navBarAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.23
            final /* synthetic */ int val$toColor;

            23(int i2) {
                r2 = i2;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                LaunchActivity.this.setNavigationBarColor(r2, false);
            }
        });
        this.navBarAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.navBarAnimator.setDuration(320L);
        this.navBarAnimator.start();
    }

    public void checkAppUpdate(boolean z, final Browser.Progress progress) {
        if (z || !BuildVars.DEBUG_VERSION) {
            if (z || BuildVars.CHECK_UPDATES) {
                if (z || Math.abs(System.currentTimeMillis() - SharedConfig.lastUpdateCheckTime) >= MessagesController.getInstance(0).updateCheckDelay * 1000) {
                    TLRPC.TL_help_getAppUpdate tL_help_getAppUpdate = new TLRPC.TL_help_getAppUpdate();
                    try {
                        tL_help_getAppUpdate.source = ApplicationLoader.applicationContext.getPackageManager().getInstallerPackageName(ApplicationLoader.applicationContext.getPackageName());
                    } catch (Exception unused) {
                    }
                    if (tL_help_getAppUpdate.source == null) {
                        tL_help_getAppUpdate.source = "";
                    }
                    final int i = this.currentAccount;
                    final int sendRequest = ConnectionsManager.getInstance(i).sendRequest(tL_help_getAppUpdate, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda41
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            LaunchActivity.this.lambda$checkAppUpdate$131(i, progress, tLObject, tL_error);
                        }
                    });
                    if (progress != null) {
                        progress.init();
                        progress.onCancel(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda42
                            @Override // java.lang.Runnable
                            public final void run() {
                                LaunchActivity.this.lambda$checkAppUpdate$132(sendRequest);
                            }
                        });
                    }
                }
            }
        }
    }

    public void checkSystemBarColors(boolean z) {
        checkSystemBarColors(z, true, !this.isNavigationBarColorFrozen, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x007e, code lost:
    
        if (r7.isShown() != false) goto L123;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x009e, code lost:
    
        r6 = r7.isAttachedLightStatusBar();
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x009c, code lost:
    
        if (r7.isShown() != false) goto L123;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void checkSystemBarColors(boolean z, boolean z2, boolean z3, boolean z4) {
        boolean z5;
        BaseFragment.AttachedSheet attachedSheet;
        ArrayList arrayList = mainFragmentsStack;
        BaseFragment baseFragment = !arrayList.isEmpty() ? (BaseFragment) arrayList.get(arrayList.size() - 1) : null;
        if (baseFragment != null && (baseFragment.isRemovingFromStack() || baseFragment.isInPreviewMode())) {
            baseFragment = arrayList.size() > 1 ? (BaseFragment) arrayList.get(arrayList.size() - 2) : null;
        }
        boolean z6 = baseFragment != null && baseFragment.hasForceLightStatusBar();
        int i = Build.VERSION.SDK_INT;
        if (i >= 23) {
            if (z2) {
                if (baseFragment != null) {
                    z5 = baseFragment.isLightStatusBar();
                    if (baseFragment.getParentLayout() instanceof ActionBarLayout) {
                        ActionBarLayout actionBarLayout = (ActionBarLayout) baseFragment.getParentLayout();
                        if (actionBarLayout.getSheetFragment(false) == null || actionBarLayout.getSheetFragment(false).getLastSheet() == null) {
                            ArrayList arrayList2 = baseFragment.sheetsStack;
                            if (arrayList2 != null && !arrayList2.isEmpty()) {
                                ArrayList arrayList3 = baseFragment.sheetsStack;
                                attachedSheet = (BaseFragment.AttachedSheet) arrayList3.get(arrayList3.size() - 1);
                            }
                        } else {
                            attachedSheet = actionBarLayout.getSheetFragment(false).getLastSheet();
                        }
                    }
                } else {
                    z5 = ColorUtils.calculateLuminance(Theme.getColor(Theme.key_actionBarDefault, null, true)) > 0.699999988079071d;
                }
                AndroidUtilities.setLightStatusBar(getWindow(), z5, z6);
            }
            if (i >= 26 && z3 && (!z || baseFragment == null || !baseFragment.isInPreviewMode())) {
                int color = (baseFragment == null || !z) ? Theme.getColor(Theme.key_windowBackgroundGray, null, true) : baseFragment.getNavigationBarColor();
                if (this.actionBarLayout.getSheetFragment(false) != null) {
                    EmptyBaseFragment sheetFragment = this.actionBarLayout.getSheetFragment(false);
                    if (sheetFragment.sheetsStack != null) {
                        for (int i2 = 0; i2 < sheetFragment.sheetsStack.size(); i2++) {
                            BaseFragment.AttachedSheet attachedSheet2 = (BaseFragment.AttachedSheet) sheetFragment.sheetsStack.get(i2);
                            if (attachedSheet2.attachedToParent()) {
                                color = attachedSheet2.getNavigationBarColor(color);
                            }
                        }
                    }
                }
                setNavigationBarColor(color, z4);
                setLightNavigationBar(AndroidUtilities.computePerceivedBrightness(color) >= 0.721f);
            }
        }
        if (Build.VERSION.SDK_INT < 21 || !z2) {
            return;
        }
        getWindow().setStatusBarColor(0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:423:0x08d8, code lost:
    
        if (r7 != null) goto L907;
     */
    /* JADX WARN: Code restructure failed: missing block: B:424:0x08da, code lost:
    
        r0 = org.telegram.ui.Components.BulletinFactory.of(r7, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x08df, code lost:
    
        r0 = org.telegram.ui.Components.BulletinFactory.of(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x08fe, code lost:
    
        if (r7 != null) goto L907;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:405:0x0866. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:249:0x061d  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x0627 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0628  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:69:? A[RETURN, SYNTHETIC] */
    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void didReceivedNotification(int i, final int i2, Object... objArr) {
        DrawerLayoutAdapter drawerLayoutAdapter;
        Bulletin make;
        BulletinFactory of;
        Object obj;
        String str;
        BulletinFactory of2;
        Object obj2;
        GroupCallActivity groupCallActivity;
        boolean z;
        ActionBarLayout actionBarLayout;
        Animator createCircularReveal;
        View childAt;
        BaseFragment baseFragment;
        AlertDialog create;
        int i3;
        if (i == NotificationCenter.appDidLogout) {
            switchToAvailableAccountOrLogout();
            return;
        }
        String[] strArr = null;
        strArr = null;
        r7 = false;
        r7 = false;
        boolean z2 = false;
        if (i == NotificationCenter.openBoostForUsersDialog) {
            Long l = (Long) objArr[0];
            l.longValue();
            processBoostDialog(l, null, null, objArr.length > 1 ? (ChatMessageCell) objArr[1] : null);
            return;
        }
        if (i == NotificationCenter.closeOtherAppActivities) {
            if (objArr[0] != this) {
                onFinish();
                finish();
                return;
            }
            return;
        }
        if (i == NotificationCenter.didUpdateConnectionState) {
            int connectionState = ConnectionsManager.getInstance(i2).getConnectionState();
            if (this.currentConnectionState != connectionState) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("switch to state " + connectionState);
                }
                this.currentConnectionState = connectionState;
                updateCurrentConnectionState(i2);
                return;
            }
            return;
        }
        if (i != NotificationCenter.mainUserInfoChanged && i != NotificationCenter.attachMenuBotsDidLoad) {
            if (i == NotificationCenter.needShowAlert) {
                Integer num = (Integer) objArr[0];
                if (num.intValue() == 6) {
                    return;
                }
                if (num.intValue() == 3 && this.proxyErrorDialog != null) {
                    return;
                }
                if (num.intValue() == 4) {
                    showTosActivity(i2, (TLRPC.TL_help_termsOfService) objArr[1]);
                    return;
                }
                ArrayList arrayList = mainFragmentsStack;
                BaseFragment baseFragment2 = !arrayList.isEmpty() ? (BaseFragment) arrayList.get(arrayList.size() - 1) : null;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(LocaleController.getString(R.string.AppName));
                if (baseFragment2 != null) {
                    Map hashMap = new HashMap();
                    int i4 = Theme.key_dialogTopBackground;
                    hashMap.put("info1.**", Integer.valueOf(baseFragment2.getThemedColor(i4)));
                    hashMap.put("info2.**", Integer.valueOf(baseFragment2.getThemedColor(i4)));
                    builder.setTopAnimation(R.raw.not_available, 52, false, baseFragment2.getThemedColor(i4), hashMap);
                    builder.setTopAnimationIsNew(true);
                }
                if (num.intValue() != 2 && num.intValue() != 3) {
                    builder.setNegativeButton(LocaleController.getString(R.string.MoreInfo), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda6
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i5) {
                            LaunchActivity.lambda$didReceivedNotification$140(i2, dialogInterface, i5);
                        }
                    });
                }
                if (num.intValue() == 5) {
                    i3 = R.string.NobodyLikesSpam3;
                } else if (num.intValue() == 0) {
                    i3 = R.string.NobodyLikesSpam1;
                } else if (num.intValue() == 1) {
                    i3 = R.string.NobodyLikesSpam2;
                } else {
                    if (num.intValue() == 2) {
                        SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf((String) objArr[1]);
                        String str2 = (String) objArr[2];
                        if (str2.startsWith("PREMIUM_GIFT_SELF_REQUIRED_")) {
                            String str3 = (String) objArr[1];
                            int indexOf = str3.indexOf(42);
                            int i5 = indexOf + 1;
                            int indexOf2 = str3.indexOf(42, i5);
                            if (indexOf != -1 && indexOf2 != -1 && indexOf != indexOf2) {
                                valueOf.replace(indexOf, indexOf2 + 1, (CharSequence) str3.substring(i5, indexOf2));
                                valueOf.setSpan(new ClickableSpan() { // from class: org.telegram.ui.LaunchActivity.20
                                    20() {
                                    }

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
                        if (str2.startsWith("AUTH_KEY_DROP_")) {
                            builder.setPositiveButton(LocaleController.getString(R.string.Cancel), null);
                            builder.setNegativeButton(LocaleController.getString(R.string.LogOut), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda9
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i6) {
                                    LaunchActivity.this.lambda$didReceivedNotification$141(dialogInterface, i6);
                                }
                            });
                        } else {
                            if (str2.startsWith("PREMIUM_")) {
                                builder.setTitle(LocaleController.getString(R.string.TelegramPremium));
                            }
                            builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
                        }
                    } else if (num.intValue() == 3) {
                        builder.setTitle(LocaleController.getString(R.string.Proxy));
                        builder.setMessage(LocaleController.getString(R.string.UseProxyTelegramError));
                        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
                        this.proxyErrorDialog = showAlertDialog(builder);
                        return;
                    }
                    if (!arrayList.isEmpty()) {
                        return;
                    }
                    baseFragment = (BaseFragment) arrayList.get(arrayList.size() - 1);
                    create = builder.create();
                }
                builder.setMessage(LocaleController.getString(i3));
                builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
                if (!arrayList.isEmpty()) {
                }
            } else if (i == NotificationCenter.wasUnableToFindCurrentLocation) {
                final HashMap hashMap2 = (HashMap) objArr[0];
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle(LocaleController.getString(R.string.AppName));
                builder2.setPositiveButton(LocaleController.getString(R.string.OK), null);
                builder2.setNegativeButton(LocaleController.getString(R.string.ShareYouLocationUnableManually), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda10
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i6) {
                        LaunchActivity.this.lambda$didReceivedNotification$143(hashMap2, i2, dialogInterface, i6);
                    }
                });
                builder2.setMessage(LocaleController.getString(R.string.ShareYouLocationUnable));
                ArrayList arrayList2 = mainFragmentsStack;
                if (arrayList2.isEmpty()) {
                    return;
                }
                baseFragment = (BaseFragment) arrayList2.get(arrayList2.size() - 1);
                create = builder2.create();
            } else {
                if (i == NotificationCenter.didSetNewWallpapper) {
                    RecyclerListView recyclerListView = this.sideMenu;
                    if (recyclerListView != null && (childAt = recyclerListView.getChildAt(0)) != null) {
                        childAt.invalidate();
                    }
                    SizeNotifierFrameLayout sizeNotifierFrameLayout = this.backgroundTablet;
                    if (sizeNotifierFrameLayout != null) {
                        sizeNotifierFrameLayout.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
                        return;
                    }
                    return;
                }
                if (i == NotificationCenter.didSetPasscode) {
                    this.flagSecureReason.invalidate();
                    return;
                }
                if (i == NotificationCenter.reloadInterface) {
                    ArrayList arrayList3 = mainFragmentsStack;
                    boolean z3 = arrayList3.size() > 1 && (arrayList3.get(arrayList3.size() - 1) instanceof ProfileActivity);
                    rebuildAllFragments((!z3 || ((ProfileActivity) arrayList3.get(arrayList3.size() - 1)).isSettings()) ? z3 : false);
                    return;
                }
                if (i == NotificationCenter.suggestedLangpack) {
                    showLanguageAlert(false);
                    return;
                }
                if (i == NotificationCenter.openArticle) {
                    ArrayList arrayList4 = mainFragmentsStack;
                    if (arrayList4.isEmpty()) {
                        return;
                    }
                    LaunchActivity launchActivity = instance;
                    if (launchActivity == null || launchActivity.getBottomSheetTabs() == null || instance.getBottomSheetTabs().tryReopenTab((TLRPC.TL_webPage) objArr[0]) == null) {
                        ((BaseFragment) arrayList4.get(arrayList4.size() - 1)).createArticleViewer(false).open((TLRPC.TL_webPage) objArr[0], (String) objArr[1]);
                        return;
                    }
                    return;
                }
                if (i == NotificationCenter.hasNewContactsToImport) {
                    ActionBarLayout actionBarLayout2 = this.actionBarLayout;
                    if (actionBarLayout2 == null || actionBarLayout2.getFragmentStack().isEmpty()) {
                        return;
                    }
                    ((Integer) objArr[0]).intValue();
                    final HashMap hashMap3 = (HashMap) objArr[1];
                    final boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
                    final boolean booleanValue2 = ((Boolean) objArr[3]).booleanValue();
                    BaseFragment baseFragment3 = this.actionBarLayout.getFragmentStack().get(this.actionBarLayout.getFragmentStack().size() - 1);
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                    builder3.setTopAnimation(R.raw.permission_request_contacts, 72, false, Theme.getColor(Theme.key_dialogTopBackground));
                    builder3.setTitle(LocaleController.getString(R.string.UpdateContactsTitle));
                    builder3.setMessage(LocaleController.getString(R.string.UpdateContactsMessage));
                    builder3.setPositiveButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda11
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i6) {
                            LaunchActivity.lambda$didReceivedNotification$144(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i6);
                        }
                    });
                    builder3.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda12
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i6) {
                            LaunchActivity.lambda$didReceivedNotification$145(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i6);
                        }
                    });
                    builder3.setOnBackButtonListener(new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda13
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i6) {
                            LaunchActivity.lambda$didReceivedNotification$146(i2, hashMap3, booleanValue, booleanValue2, dialogInterface, i6);
                        }
                    });
                    AlertDialog create2 = builder3.create();
                    baseFragment3.showDialog(create2);
                    create2.setCanceledOnTouchOutside(false);
                    return;
                }
                if (i == NotificationCenter.didSetNewTheme) {
                    if (!((Boolean) objArr[0]).booleanValue()) {
                        if (this.sideMenu != null) {
                            FrameLayout frameLayout = this.sideMenuContainer;
                            if (frameLayout != null) {
                                frameLayout.setBackgroundColor(Theme.getColor(Theme.key_chats_menuBackground));
                            }
                            RecyclerListView recyclerListView2 = this.sideMenu;
                            int i6 = Theme.key_chats_menuBackground;
                            recyclerListView2.setBackgroundColor(Theme.getColor(i6));
                            this.sideMenu.setGlowColor(Theme.getColor(i6));
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
                    boolean z4 = objArr.length > 2 && ((Boolean) objArr[2]).booleanValue();
                    if (booleanValue3 && !this.isNavigationBarColorFrozen && !this.actionBarLayout.isTransitionAnimationInProgress()) {
                        z2 = true;
                    }
                    checkSystemBarColors(z4, true, z2, true);
                    return;
                }
                if (i == NotificationCenter.needSetDayNightTheme) {
                    if (Build.VERSION.SDK_INT >= 21 && objArr[2] != null) {
                        if (this.themeSwitchImageView.getVisibility() == 0) {
                            return;
                        }
                        try {
                            int[] iArr = (int[]) objArr[2];
                            boolean booleanValue4 = ((Boolean) objArr[4]).booleanValue();
                            RLottieImageView rLottieImageView = (RLottieImageView) objArr[5];
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
                                this.rippleAbove.getBackground().setAlpha(NotificationCenter.playerDidStartPlaying);
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
                            int i7 = measuredWidth - iArr[0];
                            int i8 = measuredHeight - iArr[1];
                            double sqrt = Math.sqrt((i7 * i7) + (i8 * i8));
                            int i9 = iArr[0];
                            int i10 = measuredHeight - iArr[1];
                            float max = (float) Math.max(sqrt, Math.sqrt((i9 * i9) + (i10 * i10)));
                            int i11 = measuredWidth - iArr[0];
                            int i12 = iArr[1];
                            double sqrt2 = Math.sqrt((i11 * i11) + (i12 * i12));
                            int i13 = iArr[0];
                            int i14 = iArr[1];
                            float max2 = Math.max(max, (float) Math.max(sqrt2, Math.sqrt((i13 * i13) + (i14 * i14))));
                            View view3 = booleanValue4 ? this.drawerLayoutContainer : this.themeSwitchImageView;
                            int i15 = iArr[0];
                            int i16 = iArr[1];
                            float f = booleanValue4 ? 0.0f : max2;
                            if (!booleanValue4) {
                                max2 = 0.0f;
                            }
                            createCircularReveal = ViewAnimationUtils.createCircularReveal(view3, i15, i16, f, max2);
                            createCircularReveal.setDuration(400L);
                            createCircularReveal.setInterpolator(Easings.easeInOutQuad);
                            createCircularReveal.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.21
                                final /* synthetic */ RLottieImageView val$darkThemeView;
                                final /* synthetic */ boolean val$toDark;

                                21(boolean booleanValue42, RLottieImageView rLottieImageView2) {
                                    r2 = booleanValue42;
                                    r3 = rLottieImageView2;
                                }

                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationEnd(Animator animator) {
                                    LaunchActivity.this.rippleAbove = null;
                                    LaunchActivity.this.drawerLayoutContainer.invalidate();
                                    LaunchActivity.this.themeSwitchImageView.invalidate();
                                    LaunchActivity.this.themeSwitchImageView.setImageDrawable(null);
                                    LaunchActivity.this.themeSwitchImageView.setVisibility(8);
                                    LaunchActivity.this.themeSwitchSunView.setVisibility(8);
                                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.themeAccentListUpdated, new Object[0]);
                                    if (!r2) {
                                        r3.setVisibility(0);
                                    }
                                    DrawerProfileCell.switchingTheme = false;
                                }
                            });
                            if (this.rippleAbove != null) {
                                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda14
                                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        LaunchActivity.this.lambda$didReceivedNotification$147(valueAnimator);
                                    }
                                });
                                ofFloat.setDuration(createCircularReveal.getDuration());
                                ofFloat.start();
                            }
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda15
                                @Override // java.lang.Runnable
                                public final void run() {
                                    LaunchActivity.this.lambda$didReceivedNotification$148();
                                }
                            }, booleanValue42 ? (measuredHeight - iArr[1]) / AndroidUtilities.dp(2.25f) : 50L);
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
                        Runnable runnable = objArr.length > 7 ? (Runnable) objArr[7] : null;
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
                    Runnable runnable2 = objArr.length > 7 ? (Runnable) objArr[7] : null;
                    actionBarLayout = this.actionBarLayout;
                    if (actionBarLayout != null) {
                    }
                } else {
                    if (i == NotificationCenter.notificationsCountUpdated) {
                        RecyclerListView recyclerListView4 = this.sideMenu;
                        if (recyclerListView4 != null) {
                            Integer num2 = (Integer) objArr[0];
                            int childCount = recyclerListView4.getChildCount();
                            for (int i17 = 0; i17 < childCount; i17++) {
                                View childAt3 = this.sideMenu.getChildAt(i17);
                                if ((childAt3 instanceof DrawerUserCell) && ((DrawerUserCell) childAt3).getAccountNumber() == num2.intValue()) {
                                    childAt3.invalidate();
                                    return;
                                }
                            }
                            return;
                        }
                        return;
                    }
                    if (i == NotificationCenter.needShowPlayServicesAlert) {
                        try {
                            ((Status) objArr[0]).startResolutionForResult(this, NotificationCenter.filePreparingStarted);
                            return;
                        } catch (Throwable unused2) {
                            return;
                        }
                    }
                    if (i == NotificationCenter.fileLoaded) {
                        String str4 = (String) objArr[0];
                        if (SharedConfig.isAppUpdateAvailable()) {
                            FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str4);
                        }
                        String str5 = this.loadingThemeFileName;
                        if (str5 == null) {
                            String str6 = this.loadingThemeWallpaperName;
                            if (str6 == null || !str6.equals(str4)) {
                                return;
                            }
                            this.loadingThemeWallpaperName = null;
                            final File file = (File) objArr[1];
                            if (!this.loadingThemeAccent) {
                                final Theme.ThemeInfo themeInfo3 = this.loadingThemeInfo;
                                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda8
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        LaunchActivity.this.lambda$didReceivedNotification$152(themeInfo3, file);
                                    }
                                });
                                return;
                            }
                            openThemeAccentPreview(this.loadingTheme, this.loadingThemeWallpaper, this.loadingThemeInfo);
                        } else {
                            if (!str5.equals(str4)) {
                                return;
                            }
                            this.loadingThemeFileName = null;
                            File file2 = new File(ApplicationLoader.getFilesDirFixed(), "remote" + this.loadingTheme.id + ".attheme");
                            TLRPC.TL_theme tL_theme = this.loadingTheme;
                            final Theme.ThemeInfo fillThemeValues = Theme.fillThemeValues(file2, tL_theme.title, tL_theme);
                            if (fillThemeValues != null) {
                                if (fillThemeValues.pathToWallpaper != null && !new File(fillThemeValues.pathToWallpaper).exists()) {
                                    TLRPC.TL_account_getWallPaper tL_account_getWallPaper = new TLRPC.TL_account_getWallPaper();
                                    TLRPC.TL_inputWallPaperSlug tL_inputWallPaperSlug = new TLRPC.TL_inputWallPaperSlug();
                                    tL_inputWallPaperSlug.slug = fillThemeValues.slug;
                                    tL_account_getWallPaper.wallpaper = tL_inputWallPaperSlug;
                                    ConnectionsManager.getInstance(fillThemeValues.account).sendRequest(tL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda7
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                            LaunchActivity.this.lambda$didReceivedNotification$150(fillThemeValues, tLObject, tL_error);
                                        }
                                    });
                                    return;
                                }
                                TLRPC.TL_theme tL_theme2 = this.loadingTheme;
                                Theme.ThemeInfo applyThemeFile = Theme.applyThemeFile(file2, tL_theme2.title, tL_theme2, true);
                                if (applyThemeFile != null) {
                                    lambda$runLinkRequest$93(new ThemePreviewActivity(applyThemeFile, true, 0, false, false));
                                }
                            }
                        }
                        onThemeLoadFinish();
                        return;
                    }
                    if (i == NotificationCenter.fileLoadFailed) {
                        String str7 = (String) objArr[0];
                        if (str7.equals(this.loadingThemeFileName) || str7.equals(this.loadingThemeWallpaperName)) {
                            onThemeLoadFinish();
                        }
                        if (SharedConfig.isAppUpdateAvailable()) {
                            FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str7);
                            return;
                        }
                        return;
                    }
                    if (i == NotificationCenter.screenStateChanged) {
                        if (ApplicationLoader.mainInterfacePaused) {
                            return;
                        }
                        if (ApplicationLoader.isScreenOn) {
                            onPasscodeResume();
                            return;
                        } else {
                            onPasscodePause();
                            return;
                        }
                    }
                    if (i == NotificationCenter.needCheckSystemBarColors) {
                        checkSystemBarColors(objArr.length > 0 && ((Boolean) objArr[0]).booleanValue());
                        return;
                    }
                    if (i == NotificationCenter.historyImportProgressChanged) {
                        if (objArr.length > 1) {
                            ArrayList arrayList5 = mainFragmentsStack;
                            if (arrayList5.isEmpty()) {
                                return;
                            }
                            AlertsCreator.processError(this.currentAccount, (TLRPC.TL_error) objArr[2], (BaseFragment) arrayList5.get(arrayList5.size() - 1), (TLObject) objArr[1], new Object[0]);
                            return;
                        }
                        return;
                    }
                    if (i == NotificationCenter.billingConfirmPurchaseError) {
                        int i18 = this.currentAccount;
                        TLRPC.TL_error tL_error = (TLRPC.TL_error) objArr[1];
                        ArrayList arrayList6 = mainFragmentsStack;
                        AlertsCreator.processError(i18, tL_error, (BaseFragment) arrayList6.get(arrayList6.size() - 1), (TLObject) objArr[0], new Object[0]);
                        return;
                    }
                    if (i == NotificationCenter.stickersImportComplete) {
                        MediaDataController mediaDataController = MediaDataController.getInstance(i2);
                        TLObject tLObject = (TLObject) objArr[0];
                        ArrayList arrayList7 = mainFragmentsStack;
                        mediaDataController.toggleStickerSet(this, tLObject, 2, !arrayList7.isEmpty() ? (BaseFragment) arrayList7.get(arrayList7.size() - 1) : null, false, true);
                        return;
                    }
                    if (i == NotificationCenter.newSuggestionsAvailable) {
                        this.sideMenu.invalidateViews();
                        return;
                    }
                    if (i == NotificationCenter.showBulletin) {
                        ArrayList arrayList8 = mainFragmentsStack;
                        if (arrayList8.isEmpty()) {
                            return;
                        }
                        int intValue3 = ((Integer) objArr[0]).intValue();
                        BottomSheet.ContainerView container = (!GroupCallActivity.groupCallUiVisible || (groupCallActivity = GroupCallActivity.groupCallInstance) == null) ? null : groupCallActivity.getContainer();
                        BaseFragment baseFragment4 = container == null ? (BaseFragment) arrayList8.get(arrayList8.size() - 1) : null;
                        switch (intValue3) {
                            case 0:
                                TLRPC.Document document = (TLRPC.Document) objArr[1];
                                int intValue4 = ((Integer) objArr[2]).intValue();
                                StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(this, null, intValue4, document, null);
                                int i19 = (intValue4 == 6 || intValue4 == 7) ? 3500 : 1500;
                                make = baseFragment4 != null ? Bulletin.make(baseFragment4, stickerSetBulletinLayout, i19) : Bulletin.make(container, stickerSetBulletinLayout, i19);
                                make.show();
                                return;
                            case 1:
                                if (baseFragment4 != null) {
                                    of = BulletinFactory.of(baseFragment4);
                                    obj = objArr[1];
                                } else {
                                    of = BulletinFactory.of(container, null);
                                    obj = objArr[1];
                                }
                                str = (String) obj;
                                make = of.createErrorBulletin(str);
                                make.show();
                                return;
                            case 2:
                                str = LocaleController.getString(((Long) objArr[1]).longValue() > 0 ? R.string.YourBioChanged : R.string.ChannelDescriptionChanged);
                                break;
                            case 3:
                                str = LocaleController.getString(((Long) objArr[1]).longValue() > 0 ? R.string.YourNameChanged : R.string.ChannelTitleChanged);
                                break;
                            case 4:
                                make = baseFragment4 != null ? BulletinFactory.of(baseFragment4).createErrorBulletinSubtitle((String) objArr[1], (String) objArr[2], baseFragment4.getResourceProvider()) : BulletinFactory.of(container, null).createErrorBulletinSubtitle((String) objArr[1], (String) objArr[2], null);
                                make.show();
                                return;
                            case 5:
                                AppIconBulletinLayout appIconBulletinLayout = new AppIconBulletinLayout(this, (LauncherIconController.LauncherIcon) objArr[1], null);
                                make = baseFragment4 != null ? Bulletin.make(baseFragment4, appIconBulletinLayout, 1500) : Bulletin.make(container, appIconBulletinLayout, 1500);
                                make.show();
                                return;
                            case 6:
                                if (baseFragment4 != null) {
                                    of2 = BulletinFactory.of(baseFragment4);
                                    obj2 = objArr[1];
                                } else {
                                    of2 = BulletinFactory.of(container, null);
                                    obj2 = objArr[1];
                                }
                                make = of2.createSuccessBulletin((String) obj2);
                                make.show();
                                return;
                            default:
                                return;
                        }
                    } else {
                        if (i == NotificationCenter.groupCallUpdated) {
                            checkWasMutedByAdmin(false);
                            return;
                        }
                        if (i == NotificationCenter.fileLoadProgressChanged || i == NotificationCenter.appUpdateAvailable) {
                            return;
                        }
                        if (i == NotificationCenter.currentUserShowLimitReachedDialog) {
                            ArrayList arrayList9 = mainFragmentsStack;
                            if (arrayList9.isEmpty()) {
                                return;
                            }
                            BaseFragment baseFragment5 = (BaseFragment) arrayList9.get(arrayList9.size() - 1);
                            if (baseFragment5.getParentActivity() != null) {
                                baseFragment5.showDialog(new LimitReachedBottomSheet(baseFragment5, baseFragment5.getParentActivity(), ((Integer) objArr[0]).intValue(), this.currentAccount, null));
                                return;
                            }
                            return;
                        }
                        if (i == NotificationCenter.currentUserPremiumStatusChanged) {
                            DrawerLayoutAdapter drawerLayoutAdapter2 = this.drawerLayoutAdapter;
                            if (drawerLayoutAdapter2 != null) {
                                drawerLayoutAdapter2.notifyDataSetChanged();
                            }
                            MessagesController.getMainSettings(this.currentAccount).edit().remove("transcribeButtonPressed").apply();
                            return;
                        }
                        if (i == NotificationCenter.requestPermissions) {
                            int intValue5 = ((Integer) objArr[0]).intValue();
                            if (intValue5 == 0 && Build.VERSION.SDK_INT >= 31) {
                                strArr = new String[]{"android.permission.BLUETOOTH_CONNECT"};
                            }
                            if (strArr != null) {
                                int i20 = this.requsetPermissionsPointer + 1;
                                this.requsetPermissionsPointer = i20;
                                this.requestedPermissions.put(i20, intValue5);
                                ActivityCompat.requestPermissions(this, strArr, this.requsetPermissionsPointer);
                                return;
                            }
                            return;
                        }
                        if (i == NotificationCenter.chatSwithcedToForum) {
                            ForumUtilities.switchAllFragmentsInStackToForum(((Long) objArr[0]).longValue(), this.actionBarLayout);
                            return;
                        } else if (i != NotificationCenter.storiesEnabledUpdate || (drawerLayoutAdapter = this.drawerLayoutAdapter) == null) {
                            return;
                        }
                    }
                }
            }
            baseFragment.showDialog(create);
            return;
        }
        drawerLayoutAdapter = this.drawerLayoutAdapter;
        drawerLayoutAdapter.notifyDataSetChanged();
    }

    /* JADX WARN: Removed duplicated region for block: B:107:0x01fb  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0420  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x04c0  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x04d3  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x04e2 A[LOOP:2: B:160:0x04da->B:162:0x04e2, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0512 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:173:0x0529 A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:181:0x03d6  */
    /* JADX WARN: Removed duplicated region for block: B:184:0x03e0  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x03ea  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x03e5  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x03db  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x031b  */
    @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean didSelectDialogs(final DialogsActivity dialogsActivity, final ArrayList arrayList, final CharSequence charSequence, final boolean z, boolean z2, int i, TopicsFragment topicsFragment) {
        ChatActivity chatActivity;
        boolean z3;
        ChatActivity chatActivity2;
        int i2;
        boolean z4;
        MessageObject messageObject;
        long j;
        ChatActivity chatActivity3;
        String str;
        boolean z5;
        boolean z6;
        boolean z7;
        String str2;
        int size;
        ArrayList arrayList2;
        String str3;
        ArrayList arrayList3;
        int i3;
        String str4;
        ArrayList arrayList4;
        String str5;
        ArrayList arrayList5 = arrayList;
        final int currentAccount = dialogsActivity != null ? dialogsActivity.getCurrentAccount() : this.currentAccount;
        final Uri uri = this.exportingChatUri;
        if (uri != null) {
            final ArrayList arrayList6 = this.documentsUrisArray != null ? new ArrayList(this.documentsUrisArray) : null;
            final AlertDialog alertDialog = new AlertDialog(this, 3);
            SendMessagesHelper.getInstance(currentAccount).prepareImportHistory(((MessagesStorage.TopicKey) arrayList5.get(0)).dialogId, this.exportingChatUri, this.documentsUrisArray, new MessagesStorage.LongCallback() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda65
                @Override // org.telegram.messenger.MessagesStorage.LongCallback
                public final void run(long j2) {
                    LaunchActivity.this.lambda$didSelectDialogs$134(currentAccount, dialogsActivity, z, arrayList6, uri, alertDialog, j2);
                }
            });
            try {
                alertDialog.showDelayed(300L);
            } catch (Exception unused) {
            }
        } else {
            final boolean z8 = dialogsActivity == null || dialogsActivity.notify || z2;
            final int i4 = i != 0 ? i : dialogsActivity == null ? 0 : dialogsActivity.scheduleDate;
            if (arrayList.size() <= 1) {
                long j2 = ((MessagesStorage.TopicKey) arrayList5.get(0)).dialogId;
                Bundle bundle = new Bundle();
                bundle.putBoolean("scrollToTopOnResume", true);
                if (!AndroidUtilities.isTablet()) {
                    NotificationCenter.getInstance(currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.closeChats, new Object[0]);
                }
                if (DialogObject.isEncryptedDialog(j2)) {
                    bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j2));
                } else {
                    if (DialogObject.isUserDialog(j2)) {
                        str5 = "user_id";
                    } else {
                        j2 = -j2;
                        str5 = "chat_id";
                    }
                    bundle.putLong(str5, j2);
                }
                if (!MessagesController.getInstance(currentAccount).checkCanOpenChat(bundle, dialogsActivity)) {
                    return false;
                }
                ChatActivity chatActivity4 = new ChatActivity(bundle);
                ForumUtilities.applyTopic(chatActivity4, (MessagesStorage.TopicKey) arrayList5.get(0));
                chatActivity = chatActivity4;
            } else {
                chatActivity = null;
            }
            ArrayList arrayList7 = this.contactsToSend;
            int size2 = arrayList7 != null ? arrayList7.size() : 0;
            if (this.videoPath != null) {
                size2++;
            }
            if (this.voicePath != null) {
                size2++;
            }
            ArrayList arrayList8 = this.photoPathsArray;
            if (arrayList8 != null) {
                size2 += arrayList8.size();
            }
            ArrayList arrayList9 = this.documentsPathsArray;
            if (arrayList9 != null) {
                size2 += arrayList9.size();
            }
            ArrayList arrayList10 = this.documentsUrisArray;
            if (arrayList10 != null) {
                size2 += arrayList10.size();
            }
            if (this.videoPath == null && this.voicePath == null && this.photoPathsArray == null && this.documentsPathsArray == null && this.documentsUrisArray == null && this.sendingText != null) {
                size2++;
            }
            for (int i5 = 0; i5 < arrayList.size(); i5++) {
                if (AlertsCreator.checkSlowMode(this, this.currentAccount, ((MessagesStorage.TopicKey) arrayList5.get(i5)).dialogId, size2 > 1)) {
                    return false;
                }
            }
            if (topicsFragment != null) {
                topicsFragment.removeSelfFromStack();
            }
            ArrayList arrayList11 = this.contactsToSend;
            if (arrayList11 != null && arrayList11.size() == 1) {
                ArrayList arrayList12 = mainFragmentsStack;
                if (!arrayList12.isEmpty()) {
                    PhonebookShareAlert phonebookShareAlert = new PhonebookShareAlert((BaseFragment) arrayList12.get(arrayList12.size() - 1), null, null, this.contactsToSendUri, null, null, null);
                    final ChatActivity chatActivity5 = chatActivity;
                    phonebookShareAlert.setDelegate(new ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda66
                        @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                        public final void didSelectContact(TLRPC.User user, boolean z9, int i6, long j3, boolean z10) {
                            LaunchActivity.this.lambda$didSelectDialogs$135(chatActivity5, arrayList, i4, charSequence, currentAccount, z8, user, z9, i6, j3, z10);
                        }

                        @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                        public /* synthetic */ void didSelectContacts(ArrayList arrayList13, String str6, boolean z9, int i6, long j3, boolean z10) {
                            ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate.-CC.$default$didSelectContacts(this, arrayList13, str6, z9, i6, j3, z10);
                        }
                    });
                    ((BaseFragment) arrayList12.get(arrayList12.size() - 1)).showDialog(phonebookShareAlert);
                    chatActivity2 = chatActivity;
                    z3 = true;
                    if (dialogsActivity != null && chatActivity2 == null && !z3) {
                        dialogsActivity.lambda$onBackPressed$319();
                    }
                }
            }
            int i6 = 0;
            z3 = false;
            String str6 = null;
            while (i6 < arrayList.size()) {
                long j3 = ((MessagesStorage.TopicKey) arrayList5.get(i6)).dialogId;
                long j4 = ((MessagesStorage.TopicKey) arrayList5.get(i6)).topicId;
                AccountInstance accountInstance = AccountInstance.getInstance(UserConfig.selectedAccount);
                if (j4 != 0) {
                    i2 = i6;
                    TLRPC.TL_forumTopic findTopic = accountInstance.getMessagesController().getTopicsController().findTopic(-j3, j4);
                    if (findTopic != null && findTopic.topicStartMessage != null) {
                        z4 = z3;
                        messageObject = new MessageObject(accountInstance.getCurrentAccount(), findTopic.topicStartMessage, false, false);
                        messageObject.isTopicMainMessage = true;
                        if (chatActivity == null) {
                            j = j3;
                            chatActivity3 = chatActivity;
                            getActionBarLayout().presentFragment(chatActivity, dialogsActivity != null, dialogsActivity == null || this.videoPath != null || ((arrayList4 = this.photoPathsArray) != null && arrayList4.size() > 0), true, false);
                            boolean z9 = dialogsActivity != null;
                            String str7 = this.videoPath;
                            if (str7 == null || j4 != 0) {
                                ArrayList arrayList13 = this.photoPathsArray;
                                if (arrayList13 == null || arrayList13.size() <= 0 || j4 != 0) {
                                    if (this.videoPath != null) {
                                        String str8 = this.sendingText;
                                        if (str8 != null && str8.length() <= 1024) {
                                            str6 = this.sendingText;
                                            this.sendingText = null;
                                        }
                                        ArrayList arrayList14 = new ArrayList();
                                        arrayList14.add(this.videoPath);
                                        SendMessagesHelper.prepareSendingDocuments(accountInstance, arrayList14, arrayList14, null, str6, null, j, messageObject, messageObject, null, null, null, z8, i4, null, null, 0, 0L, false);
                                    } else {
                                        ArrayList arrayList15 = this.photoPathsArray;
                                        if (arrayList15 != null && arrayList15.size() > 0) {
                                            String str9 = this.sendingText;
                                            if (str9 != null && str9.length() <= 1024 && this.photoPathsArray.size() == 1) {
                                                ((SendMessagesHelper.SendingMediaInfo) this.photoPathsArray.get(0)).caption = this.sendingText;
                                                this.sendingText = null;
                                            }
                                            SendMessagesHelper.prepareSendingMedia(accountInstance, this.photoPathsArray, j, messageObject, messageObject, null, null, false, false, null, z8, i4, 0, false, null, null, 0, 0L, false);
                                        }
                                    }
                                    str = str6;
                                    z6 = false;
                                } else {
                                    boolean openPhotosEditor = chatActivity3.openPhotosEditor(this.photoPathsArray, (charSequence == null || charSequence.length() == 0) ? this.sendingText : charSequence);
                                    if (openPhotosEditor) {
                                        this.sendingText = null;
                                    }
                                    str = str6;
                                    z6 = false;
                                    z7 = z9;
                                    z5 = openPhotosEditor;
                                }
                            } else {
                                chatActivity3.openVideoEditor(str7, this.sendingText);
                                this.sendingText = null;
                                str = str6;
                                z6 = true;
                            }
                            z7 = z9;
                            z5 = false;
                        } else {
                            j = j3;
                            chatActivity3 = chatActivity;
                            if (this.videoPath != null) {
                                String str10 = this.sendingText;
                                if (str10 != null && str10.length() <= 1024) {
                                    str6 = this.sendingText;
                                    this.sendingText = null;
                                }
                                ArrayList arrayList16 = new ArrayList();
                                arrayList16.add(this.videoPath);
                                SendMessagesHelper.prepareSendingDocuments(accountInstance, arrayList16, arrayList16, null, str6, null, j, messageObject, messageObject, null, null, null, z8, i4, null, null, 0, 0L, false);
                            }
                            str = str6;
                            z5 = false;
                            z6 = false;
                            z7 = z4;
                        }
                        if (this.photoPathsArray != null && !z5) {
                            str4 = this.sendingText;
                            if (str4 != null && str4.length() <= 1024 && this.photoPathsArray.size() == 1) {
                                ((SendMessagesHelper.SendingMediaInfo) this.photoPathsArray.get(0)).caption = this.sendingText;
                                this.sendingText = null;
                            }
                            SendMessagesHelper.prepareSendingMedia(accountInstance, this.photoPathsArray, j, messageObject, messageObject, null, null, false, false, null, z8, i4, 0, false, null, null, 0, 0L, false);
                        }
                        if (this.documentsPathsArray == null || this.documentsUrisArray != null) {
                            str2 = this.sendingText;
                            if (str2 != null && str2.length() <= 1024) {
                                ArrayList arrayList17 = this.documentsPathsArray;
                                size = arrayList17 == null ? arrayList17.size() : 0;
                                arrayList2 = this.documentsUrisArray;
                                if (size + (arrayList2 == null ? arrayList2.size() : 0) == 1) {
                                    String str11 = this.sendingText;
                                    this.sendingText = null;
                                    str = str11;
                                }
                            }
                            SendMessagesHelper.prepareSendingDocuments(accountInstance, this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, str, this.documentsMimeType, j, messageObject, messageObject, null, null, null, z8, i4, null, null, 0, 0L, false);
                        }
                        if (this.voicePath != null) {
                            File file = new File(this.voicePath);
                            if (file.exists()) {
                                TLRPC.TL_document tL_document = new TLRPC.TL_document();
                                tL_document.file_reference = new byte[0];
                                tL_document.dc_id = Integer.MIN_VALUE;
                                tL_document.id = SharedConfig.getLastLocalId();
                                tL_document.user_id = accountInstance.getUserConfig().getClientUserId();
                                tL_document.mime_type = "audio/ogg";
                                tL_document.date = accountInstance.getConnectionsManager().getCurrentTime();
                                tL_document.size = (int) file.length();
                                TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio = new TLRPC.TL_documentAttributeAudio();
                                tL_documentAttributeAudio.voice = true;
                                byte[] waveform = MediaController.getWaveform(file.getAbsolutePath());
                                tL_documentAttributeAudio.waveform = waveform;
                                if (waveform != null) {
                                    tL_documentAttributeAudio.flags |= 4;
                                }
                                tL_document.attributes.add(tL_documentAttributeAudio);
                                accountInstance.getSendMessagesHelper().sendMessage(SendMessagesHelper.SendMessageParams.of(tL_document, null, file.getAbsolutePath(), j, messageObject, messageObject, this.sendingText, null, null, null, z8, i4, 0, null, null, false));
                                if (this.sendingText != null) {
                                    this.sendingText = null;
                                }
                                str3 = this.sendingText;
                                if (str3 != null) {
                                    SendMessagesHelper.prepareSendingText(accountInstance, str3, j, j4, z8, i4, 0L);
                                }
                                arrayList3 = this.contactsToSend;
                                if (arrayList3 != null && !arrayList3.isEmpty()) {
                                    i3 = 0;
                                    while (i3 < this.contactsToSend.size()) {
                                        SendMessagesHelper.getInstance(currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of((TLRPC.User) this.contactsToSend.get(i3), j, messageObject, messageObject, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, z8, i4));
                                        i3++;
                                        z7 = z7;
                                    }
                                }
                                boolean z10 = z7;
                                if (TextUtils.isEmpty(charSequence) && !z6 && !z5) {
                                    SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), j, j4, z8, i4, 0L);
                                }
                                i6 = i2 + 1;
                                arrayList5 = arrayList;
                                z3 = z10;
                                chatActivity = chatActivity3;
                                str6 = str;
                            }
                        }
                        str3 = this.sendingText;
                        if (str3 != null) {
                        }
                        arrayList3 = this.contactsToSend;
                        if (arrayList3 != null) {
                            i3 = 0;
                            while (i3 < this.contactsToSend.size()) {
                            }
                        }
                        boolean z102 = z7;
                        if (TextUtils.isEmpty(charSequence)) {
                            SendMessagesHelper.prepareSendingText(accountInstance, charSequence.toString(), j, j4, z8, i4, 0L);
                        }
                        i6 = i2 + 1;
                        arrayList5 = arrayList;
                        z3 = z102;
                        chatActivity = chatActivity3;
                        str6 = str;
                    }
                } else {
                    i2 = i6;
                }
                z4 = z3;
                messageObject = null;
                if (chatActivity == null) {
                }
                if (this.photoPathsArray != null) {
                    str4 = this.sendingText;
                    if (str4 != null) {
                        ((SendMessagesHelper.SendingMediaInfo) this.photoPathsArray.get(0)).caption = this.sendingText;
                        this.sendingText = null;
                    }
                    SendMessagesHelper.prepareSendingMedia(accountInstance, this.photoPathsArray, j, messageObject, messageObject, null, null, false, false, null, z8, i4, 0, false, null, null, 0, 0L, false);
                }
                if (this.documentsPathsArray == null) {
                }
                str2 = this.sendingText;
                if (str2 != null) {
                    ArrayList arrayList172 = this.documentsPathsArray;
                    if (arrayList172 == null) {
                    }
                    arrayList2 = this.documentsUrisArray;
                    if (size + (arrayList2 == null ? arrayList2.size() : 0) == 1) {
                    }
                }
                SendMessagesHelper.prepareSendingDocuments(accountInstance, this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, str, this.documentsMimeType, j, messageObject, messageObject, null, null, null, z8, i4, null, null, 0, 0L, false);
                if (this.voicePath != null) {
                }
                str3 = this.sendingText;
                if (str3 != null) {
                }
                arrayList3 = this.contactsToSend;
                if (arrayList3 != null) {
                }
                boolean z1022 = z7;
                if (TextUtils.isEmpty(charSequence)) {
                }
                i6 = i2 + 1;
                arrayList5 = arrayList;
                z3 = z1022;
                chatActivity = chatActivity3;
                str6 = str;
            }
            chatActivity2 = chatActivity;
            if (dialogsActivity != null) {
                dialogsActivity.lambda$onBackPressed$319();
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

    @Override // androidx.core.app.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        BaseFragment lastFragment;
        int streamMinVolume;
        keyEvent.getKeyCode();
        if ((keyEvent.getKeyCode() == 24 || keyEvent.getKeyCode() == 25) && (lastFragment = getLastFragment()) != null && lastFragment.getLastStoryViewer() != null) {
            lastFragment.getLastStoryViewer().dispatchKeyEvent(keyEvent);
            return true;
        }
        if (keyEvent.getAction() == 0 && (keyEvent.getKeyCode() == 24 || keyEvent.getKeyCode() == 25)) {
            if (VoIPService.getSharedInstance() == null) {
                ArrayList arrayList = mainFragmentsStack;
                if (!arrayList.isEmpty() && ((!PhotoViewer.hasInstance() || !PhotoViewer.getInstance().isVisible()) && keyEvent.getRepeatCount() == 0)) {
                    BaseFragment baseFragment = (BaseFragment) arrayList.get(arrayList.size() - 1);
                    if ((baseFragment instanceof ChatActivity) && !BaseFragment.hasSheets(baseFragment) && ((ChatActivity) baseFragment).maybePlayVisibleVideo()) {
                        return true;
                    }
                    if (AndroidUtilities.isTablet()) {
                        ArrayList arrayList2 = rightFragmentsStack;
                        if (!arrayList2.isEmpty()) {
                            BaseFragment baseFragment2 = (BaseFragment) arrayList2.get(arrayList2.size() - 1);
                            if ((baseFragment2 instanceof ChatActivity) && !BaseFragment.hasSheets(baseFragment2) && ((ChatActivity) baseFragment2).maybePlayVisibleVideo()) {
                                return true;
                            }
                        }
                    }
                }
            } else if (Build.VERSION.SDK_INT >= 32) {
                boolean isSpeakerMuted = WebRtcAudioTrack.isSpeakerMuted();
                AudioManager audioManager = (AudioManager) getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
                streamMinVolume = audioManager.getStreamMinVolume(0);
                boolean z = audioManager.getStreamVolume(0) == streamMinVolume && keyEvent.getKeyCode() == 25;
                WebRtcAudioTrack.setSpeakerMute(z);
                if (isSpeakerMuted != WebRtcAudioTrack.isSpeakerMuted()) {
                    showVoiceChatTooltip(z ? 42 : 43);
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

    public INavigationLayout getActionBarLayout() {
        ActionBarLayout actionBarLayout = this.actionBarLayout;
        if (this.sheetFragmentsStack.isEmpty()) {
            return actionBarLayout;
        }
        return (INavigationLayout) this.sheetFragmentsStack.get(r0.size() - 1);
    }

    public BottomSheetTabs getBottomSheetTabs() {
        ActionBarLayout actionBarLayout;
        ActionBarLayout actionBarLayout2 = this.rightActionBarLayout;
        if (actionBarLayout2 == null || actionBarLayout2.getBottomSheetTabs() == null) {
            ActionBarLayout actionBarLayout3 = this.actionBarLayout;
            if (actionBarLayout3 == null || actionBarLayout3.getBottomSheetTabs() == null) {
                return null;
            }
            actionBarLayout = this.actionBarLayout;
        } else {
            actionBarLayout = this.rightActionBarLayout;
        }
        return actionBarLayout.getBottomSheetTabs();
    }

    public BottomSheetTabsOverlay getBottomSheetTabsOverlay() {
        return this.bottomSheetTabsOverlay;
    }

    public FireworksOverlay getFireworksOverlay() {
        return this.fireworksOverlay;
    }

    public INavigationLayout getLayersActionBarLayout() {
        return this.layersActionBarLayout;
    }

    public FrameLayout getMainContainerFrameLayout() {
        return this.frameLayout;
    }

    public int getMainFragmentsCount() {
        return mainFragmentsStack.size();
    }

    public int getNavigationBarColor() {
        int navigationBarColor;
        if (Build.VERSION.SDK_INT < 26) {
            return 0;
        }
        Window window = getWindow();
        if (this.customNavigationBar != null) {
            return this.drawerLayoutContainer.getNavigationBarColor();
        }
        navigationBarColor = window.getNavigationBarColor();
        return navigationBarColor;
    }

    public INavigationLayout getRightActionBarLayout() {
        return this.rightActionBarLayout;
    }

    public Dialog getVisibleDialog() {
        for (int size = this.visibleDialogs.size() - 1; size >= 0; size--) {
            Dialog dialog = (Dialog) this.visibleDialogs.get(size);
            if (dialog.isShowing()) {
                return dialog;
            }
        }
        return null;
    }

    public void hideVisibleActionMode() {
        ActionMode actionMode = this.visibleActionMode;
        if (actionMode == null) {
            return;
        }
        actionMode.finish();
    }

    public boolean isLightNavigationBar() {
        return AndroidUtilities.getLightNavigationBar(getWindow());
    }

    /* JADX WARN: Code restructure failed: missing block: B:86:0x016b, code lost:
    
        if (org.telegram.ui.LaunchActivity.mainFragmentsStack.size() == 1) goto L189;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x017c, code lost:
    
        if ((r6.get(0) instanceof org.telegram.ui.IntroActivity) == false) goto L188;
     */
    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean needAddFragmentToStack(BaseFragment baseFragment, INavigationLayout iNavigationLayout) {
        boolean z;
        ActionBarLayout actionBarLayout;
        ActionBarLayout actionBarLayout2;
        ActionBarLayout actionBarLayout3;
        if (AndroidUtilities.isTablet()) {
            boolean z2 = baseFragment instanceof LoginActivity;
            this.drawerLayoutContainer.setAllowOpenDrawer((z2 || (baseFragment instanceof IntroActivity) || (baseFragment instanceof CountrySelectActivity) || (baseFragment instanceof ProxyListActivity) || (baseFragment instanceof ProxySettingsActivity) || this.layersActionBarLayout.getView().getVisibility() == 0) ? false : true, true);
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
            } else if (!(baseFragment instanceof ChatActivity) || ((ChatActivity) baseFragment).isInScheduleMode()) {
                ActionBarLayout actionBarLayout4 = this.layersActionBarLayout;
                if (iNavigationLayout != actionBarLayout4) {
                    actionBarLayout4.getView().setVisibility(0);
                    this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                    int i = 0;
                    while (true) {
                        if (i >= 4) {
                            i = -1;
                            break;
                        }
                        if (UserConfig.getInstance(i).isClientActivated()) {
                            break;
                        }
                        i++;
                    }
                    if (z2 && i == -1) {
                        this.backgroundTablet.setVisibility(0);
                        this.shadowTabletSide.setVisibility(8);
                        this.shadowTablet.setBackgroundColor(0);
                    } else {
                        this.shadowTablet.setBackgroundColor(2130706432);
                    }
                    this.layersActionBarLayout.addFragmentToStack(baseFragment);
                    return false;
                }
            } else {
                boolean z3 = this.tabletFullSize;
                if (!z3 && iNavigationLayout != (actionBarLayout2 = this.rightActionBarLayout)) {
                    actionBarLayout2.getView().setVisibility(0);
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.addFragmentToStack(baseFragment);
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            ActionBarLayout actionBarLayout5 = this.layersActionBarLayout;
                            actionBarLayout5.removeFragmentFromStack(actionBarLayout5.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                }
                if (z3 && iNavigationLayout != (actionBarLayout = this.actionBarLayout)) {
                    actionBarLayout.addFragmentToStack(baseFragment);
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            ActionBarLayout actionBarLayout6 = this.layersActionBarLayout;
                            actionBarLayout6.removeFragmentFromStack(actionBarLayout6.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(true);
                    }
                    return false;
                }
            }
        } else {
            if ((baseFragment instanceof LoginActivity) || (baseFragment instanceof IntroActivity) || (baseFragment instanceof ProxyListActivity) || (baseFragment instanceof ProxySettingsActivity)) {
                ArrayList arrayList = mainFragmentsStack;
                if (arrayList.size() != 0) {
                }
                z = false;
            } else {
                if (baseFragment instanceof CountrySelectActivity) {
                }
                z = true;
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(z, false);
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0049, code lost:
    
        if (r4.layersActionBarLayout.getFragmentStack().size() == 1) goto L43;
     */
    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean needCloseLastFragment(INavigationLayout iNavigationLayout) {
        if (!AndroidUtilities.isTablet()) {
            if (iNavigationLayout.getFragmentStack().size() > 1) {
                if (iNavigationLayout.getFragmentStack().size() >= 2 && !(iNavigationLayout.getFragmentStack().get(0) instanceof LoginActivity)) {
                    this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                }
                return true;
            }
            onFinish();
            finish();
            return false;
        }
        if (iNavigationLayout != this.actionBarLayout || iNavigationLayout.getFragmentStack().size() > 1 || this.switchingAccount) {
            if (iNavigationLayout == this.rightActionBarLayout) {
                if (!this.tabletFullSize) {
                    this.backgroundTablet.setVisibility(0);
                }
            } else if (iNavigationLayout == this.layersActionBarLayout) {
                if (this.actionBarLayout.getFragmentStack().isEmpty()) {
                }
            }
            return true;
        }
        onFinish();
        finish();
        return false;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public /* synthetic */ boolean needPresentFragment(BaseFragment baseFragment, boolean z, boolean z2, INavigationLayout iNavigationLayout) {
        return INavigationLayout.INavigationLayoutDelegate.-CC.$default$needPresentFragment(this, baseFragment, z, z2, iNavigationLayout);
    }

    /* JADX WARN: Code restructure failed: missing block: B:136:0x0292, code lost:
    
        if (org.telegram.ui.LaunchActivity.mainFragmentsStack.size() == 1) goto L295;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x02ab, code lost:
    
        if ((r9.get(0) instanceof org.telegram.ui.LoginActivity) == false) goto L294;
     */
    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean needPresentFragment(INavigationLayout iNavigationLayout, INavigationLayout.NavigationParams navigationParams) {
        boolean z;
        ActionBarLayout actionBarLayout;
        ActionBarLayout actionBarLayout2;
        ActionBarLayout actionBarLayout3;
        ActionBarLayout actionBarLayout4;
        BaseFragment baseFragment = navigationParams.fragment;
        boolean z2 = navigationParams.removeLast;
        boolean z3 = navigationParams.noAnimation;
        if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
        }
        if (AndroidUtilities.isTablet()) {
            boolean z4 = baseFragment instanceof LoginActivity;
            this.drawerLayoutContainer.setAllowOpenDrawer((z4 || (baseFragment instanceof IntroActivity) || (baseFragment instanceof CountrySelectActivity) || ((actionBarLayout4 = this.layersActionBarLayout) != null && actionBarLayout4.getView().getVisibility() == 0)) ? false : true, true);
            if ((baseFragment instanceof DialogsActivity) && ((DialogsActivity) baseFragment).isMainDialogList() && iNavigationLayout != (actionBarLayout3 = this.actionBarLayout)) {
                actionBarLayout3.removeAllFragments();
                getActionBarLayout().presentFragment(navigationParams.setRemoveLast(z2).setNoAnimation(z3).setCheckPresentFromDelegate(false));
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
            if ((baseFragment instanceof ChatActivity) && !((ChatActivity) baseFragment).isInScheduleMode()) {
                boolean z5 = this.tabletFullSize;
                if ((!z5 && iNavigationLayout == this.rightActionBarLayout) || (z5 && iNavigationLayout == this.actionBarLayout)) {
                    boolean z6 = (z5 && iNavigationLayout == (actionBarLayout2 = this.actionBarLayout) && actionBarLayout2.getFragmentStack().size() == 1) ? false : true;
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            ActionBarLayout actionBarLayout5 = this.layersActionBarLayout;
                            actionBarLayout5.removeFragmentFromStack(actionBarLayout5.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z3);
                    }
                    if (!z6) {
                        getActionBarLayout().presentFragment(navigationParams.setNoAnimation(z3).setCheckPresentFromDelegate(false));
                    }
                    return z6;
                }
                if (!z5 && iNavigationLayout != (actionBarLayout = this.rightActionBarLayout) && actionBarLayout != null) {
                    if (actionBarLayout.getView() != null) {
                        this.rightActionBarLayout.getView().setVisibility(0);
                    }
                    this.backgroundTablet.setVisibility(8);
                    this.rightActionBarLayout.removeAllFragments();
                    this.rightActionBarLayout.presentFragment(navigationParams.setNoAnimation(true).setRemoveLast(z2).setCheckPresentFromDelegate(false));
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            ActionBarLayout actionBarLayout6 = this.layersActionBarLayout;
                            actionBarLayout6.removeFragmentFromStack(actionBarLayout6.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z3);
                    }
                    return false;
                }
                if (z5 && iNavigationLayout != this.actionBarLayout) {
                    getActionBarLayout().presentFragment(navigationParams.setRemoveLast(this.actionBarLayout.getFragmentStack().size() > 1).setNoAnimation(z3).setCheckPresentFromDelegate(false));
                    if (!this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                        while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                            ActionBarLayout actionBarLayout7 = this.layersActionBarLayout;
                            actionBarLayout7.removeFragmentFromStack(actionBarLayout7.getFragmentStack().get(0));
                        }
                        this.layersActionBarLayout.closeLastFragment(!z3);
                    }
                    return false;
                }
                ActionBarLayout actionBarLayout8 = this.layersActionBarLayout;
                if (actionBarLayout8 != null && actionBarLayout8.getFragmentStack() != null && !this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                    while (this.layersActionBarLayout.getFragmentStack().size() - 1 > 0) {
                        ActionBarLayout actionBarLayout9 = this.layersActionBarLayout;
                        actionBarLayout9.removeFragmentFromStack(actionBarLayout9.getFragmentStack().get(0));
                    }
                    this.layersActionBarLayout.closeLastFragment(!z3);
                }
                getActionBarLayout().presentFragment(navigationParams.setRemoveLast(this.actionBarLayout.getFragmentStack().size() > 1).setNoAnimation(z3).setCheckPresentFromDelegate(false));
                return false;
            }
            ActionBarLayout actionBarLayout10 = this.layersActionBarLayout;
            if (iNavigationLayout != actionBarLayout10) {
                actionBarLayout10.getView().setVisibility(0);
                this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                int i = 0;
                while (true) {
                    if (i >= 4) {
                        i = -1;
                        break;
                    }
                    if (UserConfig.getInstance(i).isClientActivated()) {
                        break;
                    }
                    i++;
                }
                if (z4 && i == -1) {
                    this.backgroundTablet.setVisibility(0);
                    this.shadowTabletSide.setVisibility(8);
                    this.shadowTablet.setBackgroundColor(0);
                } else {
                    this.shadowTablet.setBackgroundColor(2130706432);
                }
                this.layersActionBarLayout.presentFragment(navigationParams.setRemoveLast(z2).setNoAnimation(z3).setCheckPresentFromDelegate(false));
                return false;
            }
        } else {
            if ((baseFragment instanceof LoginActivity) || (baseFragment instanceof IntroActivity) || (baseFragment instanceof ProxyListActivity) || (baseFragment instanceof ProxySettingsActivity)) {
                ArrayList arrayList = mainFragmentsStack;
                if (arrayList.size() != 0) {
                    if (!(arrayList.get(0) instanceof IntroActivity)) {
                    }
                }
                z = false;
            } else {
                if (baseFragment instanceof CountrySelectActivity) {
                }
                z = true;
            }
            this.drawerLayoutContainer.setAllowOpenDrawer(z, false);
        }
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeFinished(ActionMode actionMode) {
        int type;
        super.onActionModeFinished(actionMode);
        if (this.visibleActionMode == actionMode) {
            this.visibleActionMode = null;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            type = actionMode.getType();
            if (type == 1) {
                return;
            }
        }
        this.actionBarLayout.onActionModeFinished(actionMode);
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onActionModeFinished(actionMode);
            this.layersActionBarLayout.onActionModeFinished(actionMode);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeStarted(ActionMode actionMode) {
        int type;
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
        if (Build.VERSION.SDK_INT >= 23) {
            type = actionMode.getType();
            if (type == 1) {
                return;
            }
        }
        this.actionBarLayout.onActionModeStarted(actionMode);
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onActionModeStarted(actionMode);
            this.layersActionBarLayout.onActionModeStarted(actionMode);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        VoIPService sharedInstance;
        boolean canDrawOverlays;
        if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("reset lastPauseTime onActivityResult");
            }
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        if (i == 105) {
            if (Build.VERSION.SDK_INT >= 23) {
                canDrawOverlays = Settings.canDrawOverlays(this);
                ApplicationLoader.canDrawOverlays = canDrawOverlays;
                if (canDrawOverlays) {
                    GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
                    if (groupCallActivity != null) {
                        groupCallActivity.dismissInternal();
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda36
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.this.lambda$onActivityResult$137();
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
            return;
        }
        if (i == 140) {
            LocationController.getInstance(this.currentAccount).startFusedLocationRequest(i2 == -1);
            return;
        }
        if (i == 521) {
            Utilities.Callback callback = this.webviewShareAPIDoneListener;
            if (callback != null) {
                callback.run(Boolean.valueOf(i2 == -1));
                this.webviewShareAPIDoneListener = null;
                return;
            }
            return;
        }
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

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        ActionBarLayout actionBarLayout;
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
                return;
            }
            if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                SecretMediaViewer.getInstance().closePhoto(true, false);
                return;
            }
            if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                PhotoViewer.getInstance().closePhoto(true, false);
                return;
            }
            if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                ArticleViewer.getInstance().close(true, false);
                return;
            }
            if (this.drawerLayoutContainer.isDrawerOpened()) {
                this.drawerLayoutContainer.closeDrawer(false);
                return;
            }
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.getView().getVisibility() == 0) {
                    actionBarLayout = this.layersActionBarLayout;
                    actionBarLayout.onBackPressed();
                } else if (this.rightActionBarLayout.getView().getVisibility() == 0 && !this.rightActionBarLayout.getFragmentStack().isEmpty()) {
                    BaseFragment baseFragment = this.rightActionBarLayout.getFragmentStack().get(this.rightActionBarLayout.getFragmentStack().size() - 1);
                    if (baseFragment.onBackPressed()) {
                        baseFragment.lambda$onBackPressed$319();
                        return;
                    }
                    return;
                }
            }
            actionBarLayout = this.actionBarLayout;
            actionBarLayout.onBackPressed();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        AndroidUtilities.checkDisplaySize(this, configuration);
        AndroidUtilities.setPreferredMaxRefreshRate(getWindow());
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

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x0423, code lost:
    
        if (r15.actionBarLayout.addFragmentToStack(r0) != false) goto L301;
     */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onCreate(Bundle bundle) {
        boolean z;
        boolean isBackgroundRestricted;
        ActionBarLayout actionBarLayout;
        ProfileActivity profileActivity;
        boolean isInMultiWindowMode;
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
        FlagSecureReason flagSecureReason = new FlagSecureReason(getWindow(), new FlagSecureReason.FlagSecureCondition() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda16
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
            isInMultiWindowMode = isInMultiWindowMode();
            AndroidUtilities.isInMultiwindow = isInMultiWindowMode;
        }
        Theme.createCommonChatResources();
        Theme.createDialogsResources(this);
        if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.appLocked) {
            SharedConfig.lastPauseTime = (int) (SystemClock.elapsedRealtime() / 1000);
        }
        AndroidUtilities.fillStatusBarHeight(this, false);
        this.actionBarLayout = new ActionBarLayout(this, true);
        2 r4 = new FrameLayout(this) { // from class: org.telegram.ui.LaunchActivity.2
            2(Context this) {
                super(this);
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                super.dispatchDraw(canvas);
                LaunchActivity.this.drawRippleAbove(canvas, this);
            }
        };
        this.frameLayout = r4;
        r4.setClipToPadding(false);
        this.frameLayout.setClipChildren(false);
        char c = 65535;
        setContentView(this.frameLayout, new ViewGroup.LayoutParams(-1, -1));
        if (i2 >= 21) {
            ImageView imageView = new ImageView(this);
            this.themeSwitchImageView = imageView;
            imageView.setVisibility(8);
        }
        3 r5 = new 3(this);
        this.drawerLayoutContainer = r5;
        r5.setClipChildren(false);
        this.drawerLayoutContainer.setClipToPadding(false);
        this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        this.frameLayout.addView(this.drawerLayoutContainer, LayoutHelper.createFrame(-1, -1.0f));
        if (i2 >= 21) {
            4 r0 = new View(this) { // from class: org.telegram.ui.LaunchActivity.4
                4(Context this) {
                    super(this);
                }

                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    if (LaunchActivity.this.themeSwitchSunDrawable != null) {
                        LaunchActivity.this.themeSwitchSunDrawable.draw(canvas);
                        invalidate();
                    }
                }
            };
            this.themeSwitchSunView = r0;
            this.frameLayout.addView(r0, LayoutHelper.createFrame(48, 48.0f));
            this.themeSwitchSunView.setVisibility(8);
        }
        FrameLayout frameLayout = this.frameLayout;
        BottomSheetTabsOverlay bottomSheetTabsOverlay = new BottomSheetTabsOverlay(this);
        this.bottomSheetTabsOverlay = bottomSheetTabsOverlay;
        frameLayout.addView(bottomSheetTabsOverlay);
        FrameLayout frameLayout2 = this.frameLayout;
        5 r3 = new FireworksOverlay(this) { // from class: org.telegram.ui.LaunchActivity.5
            5(Context this) {
                super(this);
                setVisibility(8);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.FireworksOverlay
            public void onStop() {
                super.onStop();
                setVisibility(8);
            }

            @Override // org.telegram.ui.Components.FireworksOverlay
            public void start(boolean z2) {
                setVisibility(0);
                super.start(z2);
            }
        };
        this.fireworksOverlay = r3;
        frameLayout2.addView(r3);
        setupActionBarLayout();
        this.sideMenuContainer = new FrameLayout(this);
        6 r02 = new RecyclerListView(this) { // from class: org.telegram.ui.LaunchActivity.6
            6(Context this) {
                super(this);
            }

            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view, long j) {
                int i3;
                if (LaunchActivity.this.itemAnimator != null && LaunchActivity.this.itemAnimator.isRunning() && LaunchActivity.this.itemAnimator.isAnimatingChild(view)) {
                    i3 = canvas.save();
                    canvas.clipRect(0, LaunchActivity.this.itemAnimator.getAnimationClipTop(), getMeasuredWidth(), getMeasuredHeight());
                } else {
                    i3 = -1;
                }
                boolean drawChild = super.drawChild(canvas, view, j);
                if (i3 >= 0) {
                    canvas.restoreToCount(i3);
                    invalidate();
                    invalidateViews();
                }
                return drawChild;
            }
        };
        this.sideMenu = r02;
        SideMenultItemAnimator sideMenultItemAnimator = new SideMenultItemAnimator(r02);
        this.itemAnimator = sideMenultItemAnimator;
        this.sideMenu.setItemAnimator(sideMenultItemAnimator);
        RecyclerListView recyclerListView = this.sideMenu;
        int i3 = Theme.key_chats_menuBackground;
        recyclerListView.setBackgroundColor(Theme.getColor(i3));
        this.sideMenuContainer.setBackgroundColor(Theme.getColor(i3));
        this.sideMenu.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.sideMenu.setAllowItemsInteractionDuringAnimation(false);
        RecyclerListView recyclerListView2 = this.sideMenu;
        DrawerLayoutAdapter drawerLayoutAdapter = new DrawerLayoutAdapter(this, this.itemAnimator, this.drawerLayoutContainer);
        this.drawerLayoutAdapter = drawerLayoutAdapter;
        recyclerListView2.setAdapter(drawerLayoutAdapter);
        this.drawerLayoutAdapter.setOnPremiumDrawableClick(new View.OnClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda17
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LaunchActivity.this.lambda$onCreate$1(view);
            }
        });
        this.sideMenuContainer.addView(this.sideMenu, LayoutHelper.createFrame(-1, -1.0f));
        this.drawerLayoutContainer.setDrawerLayout(this.sideMenuContainer, this.sideMenu);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.sideMenuContainer.getLayoutParams();
        Point realScreenSize = AndroidUtilities.getRealScreenSize();
        layoutParams.width = AndroidUtilities.isTablet() ? AndroidUtilities.dp(320.0f) : Math.min(AndroidUtilities.dp(320.0f), Math.min(realScreenSize.x, realScreenSize.y) - AndroidUtilities.dp(56.0f));
        layoutParams.height = -1;
        this.sideMenuContainer.setLayoutParams(layoutParams);
        this.sideMenu.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda18
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ boolean hasDoubleTap(View view, int i4) {
                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i4);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public /* synthetic */ void onDoubleTap(View view, int i4, float f, float f2) {
                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i4, f, f2);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
            public final void onItemClick(View view, int i4, float f, float f2) {
                LaunchActivity.this.lambda$onCreate$6(view, i4, f, f2);
            }
        });
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(3, 0) { // from class: org.telegram.ui.LaunchActivity.7
            private RecyclerView.ViewHolder selectedViewHolder;

            /* loaded from: classes4.dex */
            public class 1 extends AnimatorListenerAdapter {
                final /* synthetic */ View val$view;

                1(View view2) {
                    r2 = view2;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    r2.setBackground(null);
                }
            }

            7(int i4, int i22) {
                super(i4, i22);
            }

            private void clearSelectedViewHolder() {
                RecyclerView.ViewHolder viewHolder = this.selectedViewHolder;
                if (viewHolder != null) {
                    View view2 = viewHolder.itemView;
                    this.selectedViewHolder = null;
                    view2.setTranslationX(0.0f);
                    view2.setTranslationY(0.0f);
                    if (Build.VERSION.SDK_INT >= 21) {
                        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view2, "elevation", 0.0f);
                        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LaunchActivity.7.1
                            final /* synthetic */ View val$view;

                            1(View view22) {
                                r2 = view22;
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                r2.setBackground(null);
                            }
                        });
                        ofFloat.setDuration(150L).start();
                    }
                }
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                clearSelectedViewHolder();
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i4, boolean z2) {
                View view;
                View view2;
                View view3 = viewHolder.itemView;
                if (LaunchActivity.this.drawerLayoutAdapter.isAccountsShown()) {
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(LaunchActivity.this.drawerLayoutAdapter.getFirstAccountPosition() - 1);
                    RecyclerView.ViewHolder findViewHolderForAdapterPosition2 = recyclerView.findViewHolderForAdapterPosition(LaunchActivity.this.drawerLayoutAdapter.getLastAccountPosition() + 1);
                    if ((findViewHolderForAdapterPosition != null && (view2 = findViewHolderForAdapterPosition.itemView) != null && view2.getBottom() == view3.getTop() && f2 < 0.0f) || (findViewHolderForAdapterPosition2 != null && (view = findViewHolderForAdapterPosition2.itemView) != null && view.getTop() == view3.getBottom() && f2 > 0.0f)) {
                        f2 = 0.0f;
                    }
                }
                view3.setTranslationX(f);
                view3.setTranslationY(f2);
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
                    View view = viewHolder.itemView;
                    LaunchActivity.this.sideMenu.cancelClickRunnables(false);
                    view.setBackgroundColor(Theme.getColor(Theme.key_dialogBackground));
                    if (Build.VERSION.SDK_INT >= 21) {
                        ObjectAnimator.ofFloat(view, "elevation", AndroidUtilities.dp(1.0f)).setDuration(150L).start();
                    }
                }
            }

            @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i4) {
            }
        });
        itemTouchHelper.attachToRecyclerView(this.sideMenu);
        this.sideMenu.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda19
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i4) {
                boolean lambda$onCreate$7;
                lambda$onCreate$7 = LaunchActivity.this.lambda$onCreate$7(itemTouchHelper, view, i4);
                return lambda$onCreate$7;
            }
        });
        ApplicationLoader.applicationLoaderInstance.takeUpdateLayout(this, this.sideMenu, this.sideMenuContainer);
        this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
        this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
        this.actionBarLayout.setFragmentStack(mainFragmentsStack);
        this.actionBarLayout.setFragmentStackChangedListener(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda20
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
        LiteMode.addOnPowerSaverAppliedListener(new LaunchActivity$$ExternalSyntheticLambda21(this));
        if (this.actionBarLayout.getFragmentStack().isEmpty() && ((actionBarLayout = this.layersActionBarLayout) == null || actionBarLayout.getFragmentStack().isEmpty())) {
            if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                DialogsActivity dialogsActivity = new DialogsActivity(null);
                dialogsActivity.setSideMenu(this.sideMenu);
                this.actionBarLayout.addFragmentToStack(dialogsActivity);
                this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            } else {
                this.actionBarLayout.addFragmentToStack(getClientNotActivatedFragment());
                this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
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
                                profileActivity = new ProfileActivity(bundle2);
                                this.actionBarLayout.addFragmentToStack(profileActivity);
                            } else if (c != 2) {
                                if (c != 3) {
                                    if (c != 4) {
                                        if (c == 5) {
                                            WallpapersListActivity wallpapersListActivity = new WallpapersListActivity(0);
                                            this.actionBarLayout.addFragmentToStack(wallpapersListActivity);
                                            wallpapersListActivity.restoreSelfArgs(bundle);
                                        }
                                    } else if (bundle2 != null) {
                                        profileActivity = new ProfileActivity(bundle2);
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
                            profileActivity.restoreSelfArgs(bundle);
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
        handleIntent(getIntent(), false, bundle != null, false, null, true, true);
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
                ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda22
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
        if (i5 >= 28) {
            isBackgroundRestricted = ((ActivityManager) getSystemService("activity")).isBackgroundRestricted();
            if (isBackgroundRestricted && System.currentTimeMillis() - SharedConfig.BackgroundActivityPrefs.getLastCheckedBackgroundActivity() >= 86400000 && SharedConfig.BackgroundActivityPrefs.getDismissedCount() < 3) {
                AlertsCreator.createBackgroundActivityDialog(this).show();
                SharedConfig.BackgroundActivityPrefs.setLastCheckedBackgroundActivity(System.currentTimeMillis());
            }
        }
        if (i5 >= 31) {
            getWindow().getDecorView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: org.telegram.ui.LaunchActivity.9
                9() {
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view) {
                    LaunchActivity.this.getWindowManager().addCrossWindowBlurEnabledListener(Consumer.Wrapper.convert(LaunchActivity.this.blurListener));
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view) {
                    LaunchActivity.this.getWindowManager().removeCrossWindowBlurEnabledListener(Consumer.Wrapper.convert(LaunchActivity.this.blurListener));
                }
            });
        }
        BackupAgent.requestBackup(this);
        RestrictedLanguagesSelectActivity.checkRestrictedLanguages(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
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
                if (((Dialog) this.visibleDialogs.get(i)).isShowing()) {
                    ((Dialog) this.visibleDialogs.get(i)).dismiss();
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

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        ActionBarLayout actionBarLayout;
        if (i == 82 && !SharedConfig.isWaitingForPasscodeEnter) {
            if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                return super.onKeyUp(i, keyEvent);
            }
            if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                return super.onKeyUp(i, keyEvent);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.getView().getVisibility() == 0 && !this.layersActionBarLayout.getFragmentStack().isEmpty()) {
                    actionBarLayout = this.layersActionBarLayout;
                } else if (this.rightActionBarLayout.getView().getVisibility() == 0 && !this.rightActionBarLayout.getFragmentStack().isEmpty()) {
                    actionBarLayout = this.rightActionBarLayout;
                }
                actionBarLayout.getView().onKeyUp(i, keyEvent);
            } else if (this.actionBarLayout.getFragmentStack().size() == 1) {
                if (this.drawerLayoutContainer.isDrawerOpened()) {
                    this.drawerLayoutContainer.closeDrawer(false);
                } else {
                    if (getCurrentFocus() != null) {
                        AndroidUtilities.hideKeyboard(getCurrentFocus());
                    }
                    this.drawerLayoutContainer.openDrawer(false);
                }
            }
            actionBarLayout = this.actionBarLayout;
            actionBarLayout.getView().onKeyUp(i, keyEvent);
        }
        return super.onKeyUp(i, keyEvent);
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

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public /* synthetic */ void onMeasureOverride(int[] iArr) {
        INavigationLayout.INavigationLayoutDelegate.-CC.$default$onMeasureOverride(this, iArr);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onMultiWindowModeChanged(boolean z) {
        AndroidUtilities.isInMultiwindow = z;
        checkLayout();
        super.onMultiWindowModeChanged(z);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false, null, true, true);
    }

    public void onNewIntent(Intent intent, Browser.Progress progress) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false, progress, true, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        isResumed = false;
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, Integer.valueOf(LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM));
        ApplicationLoader.mainInterfacePaused = true;
        final int i = this.currentAccount;
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$onPause$138(i);
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
        Iterator it = this.overlayPasscodeViews.iterator();
        while (it.hasNext()) {
            ((PasscodeView) it.next()).onPause();
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(!(ApplicationLoader.applicationLoaderInstance != null ? r1.onPause() : false), false);
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onPause();
        }
        StoryRecorder.onPause();
        if (VoIPFragment.getInstance() != null) {
            VoIPFragment.onPause();
        }
        SpoilerEffect2.pause(true);
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public boolean onPreIme() {
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
            return true;
        }
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
            return true;
        }
        if (!ArticleViewer.hasInstance() || !ArticleViewer.getInstance().isVisible()) {
            return false;
        }
        ArticleViewer.getInstance().close(true, false);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.INavigationLayout.INavigationLayoutDelegate
    public void onRebuildAllFragments(INavigationLayout iNavigationLayout, boolean z) {
        if (AndroidUtilities.isTablet() && iNavigationLayout == this.layersActionBarLayout) {
            this.rightActionBarLayout.rebuildAllFragmentViews(z, z);
            this.actionBarLayout.rebuildAllFragmentViews(z, z);
        }
        this.drawerLayoutAdapter.notifyDataSetChanged();
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
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.activityPermissionsGranted, Integer.valueOf(i), strArr, iArr);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        boolean canDrawOverlays;
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
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, Integer.valueOf(LiteMode.FLAG_ANIMATED_EMOJI_CHAT_NOT_PREMIUM));
        MediaController mediaController = MediaController.getInstance();
        ViewGroup view = this.actionBarLayout.getView();
        this.feedbackView = view;
        mediaController.setFeedbackView(view, true);
        ApplicationLoader.mainInterfacePaused = false;
        MessagesController.getInstance(this.currentAccount).sortDialogs(null);
        showLanguageAlert(false);
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$onResume$139();
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
            Iterator it = this.overlayPasscodeViews.iterator();
            while (it.hasNext()) {
                ((PasscodeView) it.next()).onResume();
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
            TLRPC.TL_help_appUpdate tL_help_appUpdate = SharedConfig.pendingAppUpdate;
            if (tL_help_appUpdate != null && tL_help_appUpdate.can_not_skip) {
                showUpdateActivity(UserConfig.selectedAccount, SharedConfig.pendingAppUpdate, true);
            }
        }
        checkAppUpdate(false, null);
        if (Build.VERSION.SDK_INT >= 23) {
            canDrawOverlays = Settings.canDrawOverlays(this);
            ApplicationLoader.canDrawOverlays = canDrawOverlays;
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
        Runnable runnable2 = whenResumed;
        if (runnable2 != null) {
            runnable2.run();
            whenResumed = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        List<BaseFragment> fragmentStack;
        int size;
        BaseFragment baseFragment;
        String str;
        try {
            super.onSaveInstanceState(bundle);
            if (AndroidUtilities.isTablet()) {
                ActionBarLayout actionBarLayout = this.layersActionBarLayout;
                if (actionBarLayout == null || actionBarLayout.getFragmentStack().isEmpty()) {
                    ActionBarLayout actionBarLayout2 = this.rightActionBarLayout;
                    if (actionBarLayout2 == null || actionBarLayout2.getFragmentStack().isEmpty()) {
                        if (!this.actionBarLayout.getFragmentStack().isEmpty()) {
                            fragmentStack = this.actionBarLayout.getFragmentStack();
                            size = this.actionBarLayout.getFragmentStack().size() - 1;
                        }
                        baseFragment = null;
                    } else {
                        fragmentStack = this.rightActionBarLayout.getFragmentStack();
                        size = this.rightActionBarLayout.getFragmentStack().size() - 1;
                    }
                } else {
                    fragmentStack = this.layersActionBarLayout.getFragmentStack();
                    size = this.layersActionBarLayout.getFragmentStack().size() - 1;
                }
                baseFragment = fragmentStack.get(size);
            } else {
                if (!this.actionBarLayout.getFragmentStack().isEmpty()) {
                    fragmentStack = this.actionBarLayout.getFragmentStack();
                    size = this.actionBarLayout.getFragmentStack().size() - 1;
                    baseFragment = fragmentStack.get(size);
                }
                baseFragment = null;
            }
        } catch (Exception e) {
            FileLog.e(e);
            return;
        }
        if (baseFragment != null) {
            Bundle arguments = baseFragment.getArguments();
            if ((baseFragment instanceof ChatActivity) && arguments != null) {
                bundle.putBundle("args", arguments);
                str = "chat";
            } else if ((baseFragment instanceof GroupCreateFinalActivity) && arguments != null) {
                bundle.putBundle("args", arguments);
                str = "group";
            } else {
                if (!(baseFragment instanceof WallpapersListActivity)) {
                    if (!(baseFragment instanceof ProfileActivity)) {
                        if ((baseFragment instanceof ChannelCreateActivity) && arguments != null && arguments.getInt("step") == 0) {
                            bundle.putBundle("args", arguments);
                            str = "channel";
                        }
                        baseFragment.saveSelfArgs(bundle);
                    }
                    ProfileActivity profileActivity = (ProfileActivity) baseFragment;
                    if (!profileActivity.isSettings()) {
                        if (profileActivity.isChat() && arguments != null) {
                            bundle.putBundle("args", arguments);
                            str = "chat_profile";
                        }
                        baseFragment.saveSelfArgs(bundle);
                    }
                    str = "settings";
                    FileLog.e(e);
                    return;
                }
                str = "wallpapers";
            }
            bundle.putString("fragment", str);
            baseFragment.saveSelfArgs(bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        Browser.bindCustomTabsService(this);
        ApplicationLoader.mainInterfaceStopped = false;
        GroupCallPip.updateVisibility(this);
        GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
        if (groupCallActivity != null) {
            groupCallActivity.onResume();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        Browser.unbindCustomTabsService(this);
        ApplicationLoader.mainInterfaceStopped = true;
        GroupCallPip.updateVisibility(this);
        GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
        if (groupCallActivity != null) {
            groupCallActivity.onPause();
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

    @Override // android.app.Activity
    protected void onUserLeaveHint() {
        Iterator it = this.onUserLeaveHintListeners.iterator();
        while (it.hasNext()) {
            ((Runnable) it.next()).run();
        }
        ActionBarLayout actionBarLayout = this.actionBarLayout;
        if (actionBarLayout != null) {
            actionBarLayout.onUserLeaveHint();
        }
    }

    public void openMessage(final long j, final int i, final String str, final Browser.Progress progress, int i2, final int i3) {
        Integer valueOf;
        Runnable runnable;
        TLRPC.Chat chat;
        if (j >= 0 || (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j))) == null || !ChatObject.isForum(chat)) {
            if (progress != null) {
                progress.init();
            }
            final Bundle bundle = new Bundle();
            if (j >= 0) {
                bundle.putLong("user_id", j);
            } else {
                long j2 = -j;
                TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(j2));
                if (chat2 == null || !chat2.forum) {
                    bundle.putLong("chat_id", j2);
                } else {
                    valueOf = Integer.valueOf(i);
                    runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda153
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchActivity.lambda$openMessage$40(Browser.Progress.this);
                        }
                    };
                }
            }
            bundle.putInt("message_id", i);
            ArrayList arrayList = mainFragmentsStack;
            final BaseFragment baseFragment = !arrayList.isEmpty() ? (BaseFragment) arrayList.get(arrayList.size() - 1) : null;
            if (baseFragment == null || MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, baseFragment)) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda154
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchActivity.this.lambda$openMessage$44(bundle, i, str, i3, j, progress, baseFragment);
                    }
                });
                return;
            }
            return;
        }
        if (progress != null) {
            progress.init();
        }
        valueOf = Integer.valueOf(i);
        runnable = new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda152
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.lambda$openMessage$39(Browser.Progress.this);
            }
        };
        openForumFromLink(j, valueOf, str, runnable, i2, i3);
    }

    /* renamed from: presentFragment */
    public void lambda$runLinkRequest$93(BaseFragment baseFragment) {
        getActionBarLayout().presentFragment(baseFragment);
    }

    public boolean presentFragment(BaseFragment baseFragment, boolean z, boolean z2) {
        return getActionBarLayout().presentFragment(baseFragment, z, z2, true, false);
    }

    public void rebuildAllFragments(boolean z) {
        ActionBarLayout actionBarLayout = this.layersActionBarLayout;
        if (actionBarLayout == null) {
            actionBarLayout = this.actionBarLayout;
        }
        actionBarLayout.rebuildAllFragmentViews(z, z);
    }

    public void removeOnUserLeaveHintListener(Runnable runnable) {
        this.onUserLeaveHintListeners.remove(runnable);
    }

    public void removeOverlayPasscodeView(PasscodeView passcodeView) {
        this.overlayPasscodeViews.remove(passcodeView);
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

    public void setLightNavigationBar(boolean z) {
        if (Build.VERSION.SDK_INT >= 26) {
            AndroidUtilities.setLightNavigationBar(getWindow(), z);
        }
    }

    public void setNavigateToPremiumBot(boolean z) {
        this.navigateToPremiumBot = z;
    }

    public void setNavigateToPremiumGiftCallback(Runnable runnable) {
        this.navigateToPremiumGiftCallback = runnable;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0026, code lost:
    
        if (org.telegram.messenger.AndroidUtilities.computePerceivedBrightness(r4) >= 0.721f) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x003e, code lost:
    
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x003c, code lost:
    
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x003a, code lost:
    
        if (org.telegram.messenger.AndroidUtilities.computePerceivedBrightness(r4) >= 0.721f) goto L46;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setNavigationBarColor(int i, boolean z) {
        int navigationBarColor;
        boolean z2;
        if (Build.VERSION.SDK_INT >= 26) {
            Window window = getWindow();
            if (this.customNavigationBar == null) {
                navigationBarColor = window.getNavigationBarColor();
                if (navigationBarColor != i) {
                    window.setNavigationBarColor(i);
                    if (z) {
                    }
                }
            } else if (this.drawerLayoutContainer.getNavigationBarColor() != i) {
                this.drawerLayoutContainer.setNavigationBarColor(i);
                if (z) {
                }
            }
            AndroidUtilities.setLightNavigationBar(window, z2);
        }
        BottomSheetTabs bottomSheetTabs = getBottomSheetTabs();
        if (bottomSheetTabs != null) {
            bottomSheetTabs.setNavigationBarColor(i);
        }
    }

    public Dialog showAlertDialog(AlertDialog.Builder builder) {
        try {
            final AlertDialog show = builder.show();
            show.setCanceledOnTouchOutside(true);
            show.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda89
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LaunchActivity.this.lambda$showAlertDialog$133(show, dialogInterface);
                }
            });
            this.visibleDialogs.add(show);
            return show;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x002e  */
    /* JADX WARN: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void showBulletin(Function function) {
        BaseFragment baseFragment;
        ArrayList arrayList = layerFragmentsStack;
        if (arrayList.isEmpty()) {
            arrayList = rightFragmentsStack;
            if (arrayList.isEmpty()) {
                arrayList = mainFragmentsStack;
                if (arrayList.isEmpty()) {
                    baseFragment = null;
                    if (BulletinFactory.canShowBulletin(baseFragment)) {
                        return;
                    }
                    ((Bulletin) function.apply(BulletinFactory.of(baseFragment))).show();
                    return;
                }
            }
        }
        baseFragment = (BaseFragment) arrayList.get(arrayList.size() - 1);
        if (BulletinFactory.canShowBulletin(baseFragment)) {
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
        this.passcodeDialog.passcodeView.onShow(this.overlayPasscodeViews.isEmpty() && z, z2, i, i2, new Runnable() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda57
            @Override // java.lang.Runnable
            public final void run() {
                LaunchActivity.this.lambda$showPasscodeActivity$13(runnable);
            }
        }, runnable2);
        int i3 = 0;
        while (i3 < this.overlayPasscodeViews.size()) {
            ((PasscodeView) this.overlayPasscodeViews.get(i3)).onShow(z && i3 == this.overlayPasscodeViews.size() - 1, z2, i, i2, null, null);
            i3++;
        }
        SharedConfig.isWaitingForPasscodeEnter = true;
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
        PasscodeView.PasscodeViewDelegate passcodeViewDelegate = new PasscodeView.PasscodeViewDelegate() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda58
            @Override // org.telegram.ui.Components.PasscodeView.PasscodeViewDelegate
            public final void didAcceptedPassword(PasscodeView passcodeView) {
                LaunchActivity.this.lambda$showPasscodeActivity$14(passcodeView);
            }
        };
        this.passcodeDialog.passcodeView.setDelegate(passcodeViewDelegate);
        Iterator it = this.overlayPasscodeViews.iterator();
        while (it.hasNext()) {
            ((PasscodeView) it.next()).setDelegate(passcodeViewDelegate);
        }
        try {
            NotificationsController.getInstance(UserConfig.selectedAccount).showNotifications();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void showSelectStatusDialog() {
        BaseFragment lastFragment;
        int i;
        View view;
        int i2;
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable;
        WindowInsets rootWindowInsets;
        WindowInsets rootWindowInsets2;
        int stableInsetLeft;
        if (this.selectAnimatedEmojiDialog != null || SharedConfig.appLocked || (lastFragment = this.actionBarLayout.getLastFragment()) == null) {
            return;
        }
        View childAt = this.sideMenu.getChildAt(0);
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[1];
        TLRPC.User user = MessagesController.getInstance(UserConfig.selectedAccount).getUser(Long.valueOf(UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId()));
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
            if (Build.VERSION.SDK_INT >= 23 && getWindow() != null && getWindow().getDecorView() != null) {
                rootWindowInsets = getWindow().getDecorView().getRootWindowInsets();
                if (rootWindowInsets != null) {
                    rootWindowInsets2 = getWindow().getDecorView().getRootWindowInsets();
                    stableInsetLeft = rootWindowInsets2.getStableInsetLeft();
                    i -= stableInsetLeft;
                }
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
        12 r7 = new SelectAnimatedEmojiDialog(lastFragment, this, true, Integer.valueOf(i), 0, null) { // from class: org.telegram.ui.LaunchActivity.12
            final /* synthetic */ SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] val$popup;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            12(BaseFragment lastFragment2, Context this, boolean z2, Integer num, int i3, Theme.ResourcesProvider resourcesProvider, SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr2) {
                super(lastFragment2, this, z2, num, i3, resourcesProvider);
                r15 = selectAnimatedEmojiDialogWindowArr2;
            }

            @Override // org.telegram.ui.SelectAnimatedEmojiDialog
            protected void onEmojiSelected(View view3, Long l, TLRPC.Document document, Integer num) {
                TLRPC.EmojiStatus emojiStatus;
                if (l == null) {
                    emojiStatus = new TLRPC.TL_emojiStatusEmpty();
                } else if (num != null) {
                    TLRPC.TL_emojiStatusUntil tL_emojiStatusUntil = new TLRPC.TL_emojiStatusUntil();
                    tL_emojiStatusUntil.document_id = l.longValue();
                    tL_emojiStatusUntil.until = num.intValue();
                    emojiStatus = tL_emojiStatusUntil;
                } else {
                    TLRPC.TL_emojiStatus tL_emojiStatus = new TLRPC.TL_emojiStatus();
                    tL_emojiStatus.document_id = l.longValue();
                    emojiStatus = tL_emojiStatus;
                }
                MessagesController.getInstance(LaunchActivity.this.currentAccount).updateEmojiStatus(emojiStatus);
                TLRPC.User currentUser = UserConfig.getInstance(LaunchActivity.this.currentAccount).getCurrentUser();
                if (currentUser != null) {
                    for (int i3 = 0; i3 < LaunchActivity.this.sideMenu.getChildCount(); i3++) {
                        View childAt2 = LaunchActivity.this.sideMenu.getChildAt(i3);
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
                            ((DrawerActionCell) childAt2).updateTextAndIcon(LaunchActivity.this.getString(z2 ? R.string.ChangeEmojiStatus : R.string.SetEmojiStatus), z2 ? R.drawable.msg_status_edit : R.drawable.msg_status_set);
                        }
                    }
                }
                if (r15[0] != null) {
                    LaunchActivity.this.selectAnimatedEmojiDialog = null;
                    r15[0].dismiss();
                }
            }

            @Override // org.telegram.ui.SelectAnimatedEmojiDialog
            public void onSettings() {
                DrawerLayoutContainer drawerLayoutContainer = LaunchActivity.this.drawerLayoutContainer;
                if (drawerLayoutContainer != null) {
                    drawerLayoutContainer.closeDrawer();
                }
            }
        };
        if (user != null) {
            r7.setExpireDateHint(DialogObject.getEmojiStatusUntil(user.emoji_status));
        }
        r7.setSelected((swapAnimatedEmojiDrawable == null || !(swapAnimatedEmojiDrawable.getDrawable() instanceof AnimatedEmojiDrawable)) ? null : Long.valueOf(((AnimatedEmojiDrawable) swapAnimatedEmojiDrawable.getDrawable()).getDocumentId()));
        r7.setSaveState(2);
        r7.setScrimDrawable(swapAnimatedEmojiDrawable, view2);
        13 r0 = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(r7, -2, -2) { // from class: org.telegram.ui.LaunchActivity.13
            13(View r72, int i3, int i22) {
                super(r72, i3, i22);
            }

            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
            public void dismiss() {
                super.dismiss();
                LaunchActivity.this.selectAnimatedEmojiDialog = null;
            }
        };
        this.selectAnimatedEmojiDialog = r0;
        selectAnimatedEmojiDialogWindowArr2[0] = r0;
        r0.showAsDropDown(this.sideMenu.getChildAt(0), 0, i2, 48);
        selectAnimatedEmojiDialogWindowArr2[0].dimBehind();
    }

    public void switchToAccount(int i, boolean z) {
        switchToAccount(i, z, new GenericProvider() { // from class: org.telegram.ui.LaunchActivity$$ExternalSyntheticLambda40
            @Override // org.telegram.messenger.GenericProvider
            public final Object provide(Object obj) {
                DialogsActivity lambda$switchToAccount$12;
                lambda$switchToAccount$12 = LaunchActivity.lambda$switchToAccount$12((Void) obj);
                return lambda$switchToAccount$12;
            }
        });
    }

    public void switchToAccount(int i, boolean z, GenericProvider genericProvider) {
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
        DialogsActivity dialogsActivity = (DialogsActivity) genericProvider.provide(null);
        dialogsActivity.setSideMenu(this.sideMenu);
        this.actionBarLayout.addFragmentToStack(dialogsActivity, -3);
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

    public void whenWebviewShareAPIDone(Utilities.Callback callback) {
        this.webviewShareAPIDoneListener = callback;
    }
}
