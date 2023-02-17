package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.Property;
import android.util.StateSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScrollerCustom;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FilesMigrationService;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.XiaomiUtilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatInvite;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$EmojiStatus;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$RequestPeerType;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$TL_channels_sendAsPeers;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_dialog;
import org.telegram.tgnet.TLRPC$TL_dialogFolder;
import org.telegram.tgnet.TLRPC$TL_emojiStatus;
import org.telegram.tgnet.TLRPC$TL_emojiStatusEmpty;
import org.telegram.tgnet.TLRPC$TL_emojiStatusUntil;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_help_premiumPromo;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_messages_checkHistoryImportPeer;
import org.telegram.tgnet.TLRPC$TL_messages_checkedHistoryImportPeer;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_updateDialogFilter;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_recentMeUrlChat;
import org.telegram.tgnet.TLRPC$TL_recentMeUrlChatInvite;
import org.telegram.tgnet.TLRPC$TL_recentMeUrlStickerSet;
import org.telegram.tgnet.TLRPC$TL_recentMeUrlUnknown;
import org.telegram.tgnet.TLRPC$TL_recentMeUrlUser;
import org.telegram.tgnet.TLRPC$TL_requestPeerTypeBroadcast;
import org.telegram.tgnet.TLRPC$TL_requestPeerTypeChat;
import org.telegram.tgnet.TLRPC$TL_requestPeerTypeUser;
import org.telegram.tgnet.TLRPC$TL_userEmpty;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.MenuDrawable;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Adapters.DialogsAdapter;
import org.telegram.ui.Adapters.DialogsSearchAdapter;
import org.telegram.ui.Adapters.FiltersView;
import org.telegram.ui.Cells.AccountSelectCell;
import org.telegram.ui.Cells.ArchiveHintInnerCell;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.DialogsEmptyCell;
import org.telegram.ui.Cells.DialogsHintCell;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.DrawerActionCell;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.DrawerProfileCell;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.HashtagSearchCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.HintDialogCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.RequestPeerRequirementsCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BlurredRecyclerView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.ChatAvatarContainer;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.DialogsItemAnimator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.FilterTabsView;
import org.telegram.ui.Components.FiltersListBottomSheet;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.FloatingDebug.FloatingDebugController;
import org.telegram.ui.Components.FloatingDebug.FloatingDebugProvider;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.FragmentContextView;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.PacmanAnimation;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.ProxyDrawable;
import org.telegram.ui.Components.PullForegroundDrawable;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.RecyclerAnimationScrollHelper;
import org.telegram.ui.Components.RecyclerItemsEnterAnimator;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SearchViewPager;
import org.telegram.ui.Components.SimpleThemeDescription;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.Components.ViewPagerFixed;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.FilteredSearchView;
import org.telegram.ui.GroupCreateFinalActivity;
import org.telegram.ui.SelectAnimatedEmojiDialog;
/* loaded from: classes3.dex */
public class DialogsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate, FloatingDebugProvider {
    public static boolean[] dialogsLoaded = new boolean[4];
    private static final Interpolator interpolator = DialogsActivity$$ExternalSyntheticLambda39.INSTANCE;
    public static float viewOffset = 0.0f;
    public final Property<DialogsActivity, Float> SCROLL_Y;
    private ValueAnimator actionBarColorAnimator;
    private Paint actionBarDefaultPaint;
    private ArrayList<View> actionModeViews;
    private ActionBarMenuSubItem addToFolderItem;
    private String addToGroupAlertString;
    private float additionalFloatingTranslation;
    private float additionalFloatingTranslation2;
    private float additionalOffset;
    private boolean afterSignup;
    private boolean allowBots;
    private boolean allowChannels;
    private boolean allowGlobalSearch;
    private boolean allowGroups;
    private boolean allowMoving;
    private boolean allowSwipeDuringCurrentTouch;
    private boolean allowSwitchAccount;
    private boolean allowUsers;
    private DrawerProfileCell.AnimatedStatusView animatedStatusView;
    private boolean animatingForward;
    private int animationIndex;
    private ActionBarMenuItem archive2Item;
    private ActionBarMenuSubItem archiveItem;
    private boolean askAboutContacts;
    private boolean askingForPermissions;
    private ChatAvatarContainer avatarContainer;
    private boolean backAnimation;
    private BackDrawable backDrawable;
    private ActionBarMenuSubItem blockItem;
    private View blurredView;
    private Long cacheSize;
    private int canClearCacheCount;
    private boolean canDeletePsaSelected;
    private int canMuteCount;
    private int canPinCount;
    private int canReadCount;
    private int canReportSpamCount;
    private boolean canSelectTopics;
    private boolean canShowFilterTabsView;
    private boolean canShowHiddenArchive;
    private int canUnarchiveCount;
    private int canUnmuteCount;
    private boolean cantSendToChannels;
    private boolean checkCanWrite;
    private boolean checkPermission;
    private boolean checkingImportDialog;
    private ActionBarMenuSubItem clearItem;
    private boolean closeFragment;
    private boolean closeSearchFieldOnHide;
    private ChatActivityEnterView commentView;
    private AnimatorSet commentViewAnimator;
    private View commentViewBg;
    private float contactsAlpha;
    private ValueAnimator contactsAlphaAnimator;
    private int currentConnectionState;
    View databaseMigrationHint;
    private int debugLastUpdateAction;
    private DialogsActivityDelegate delegate;
    private ActionBarMenuItem deleteItem;
    private Long deviceSize;
    private DialogsHintCell dialogsHintCell;
    private boolean dialogsListFrozen;
    private boolean disableActionBarScrolling;
    private ActionBarMenuItem doneItem;
    private AnimatorSet doneItemAnimator;
    private ActionBarMenuItem downloadsItem;
    private boolean downloadsItemVisible;
    private float filterTabsMoveFrom;
    private float filterTabsProgress;
    private FilterTabsView filterTabsView;
    private boolean filterTabsViewIsVisible;
    private ValueAnimator filtersTabAnimator;
    private FiltersView filtersView;
    private RLottieImageView floatingButton;
    private FrameLayout floatingButtonContainer;
    private float floatingButtonHideProgress;
    private float floatingButtonPanOffset;
    private float floatingButtonTranslation;
    private boolean floatingForceVisible;
    private boolean floatingHidden;
    private final AccelerateDecelerateInterpolator floatingInterpolator;
    private AnimatorSet floatingProgressAnimator;
    private RadialProgressView floatingProgressView;
    private boolean floatingProgressVisible;
    private int folderId;
    private int forumCount;
    private FragmentContextView fragmentContextView;
    private FragmentContextView fragmentLocationContextView;
    private ArrayList<TLRPC$Dialog> frozenDialogsList;
    private boolean hasInvoice;
    private int hasPoll;
    private int initialDialogsType;
    private String initialSearchString;
    private int initialSearchType;
    boolean isDrawerTransition;
    private boolean isFirstTab;
    private boolean isNextButton;
    private boolean isPremiumHintUpgrade;
    boolean isSlideBackTransition;
    private int lastMeasuredTopPadding;
    private int maximumVelocity;
    private boolean maybeStartTracking;
    private MenuDrawable menuDrawable;
    private int messagesCount;
    private ArrayList<MessagesController.DialogFilter> movingDialogFilters;
    private DialogCell movingView;
    private boolean movingWas;
    private ActionBarMenuItem muteItem;
    public boolean notify;
    private boolean onlySelect;
    private MessagesStorage.TopicKey openedDialogId;
    private PacmanAnimation pacmanAnimation;
    private Paint paint;
    private RLottieDrawable passcodeDrawable;
    private ActionBarMenuItem passcodeItem;
    private boolean passcodeItemVisible;
    private AlertDialog permissionDialog;
    private ActionBarMenuSubItem pin2Item;
    private ActionBarMenuItem pinItem;
    private Drawable premiumStar;
    private int prevPosition;
    private int prevTop;
    private float progressToActionMode;
    private ProxyDrawable proxyDrawable;
    private ActionBarMenuItem proxyItem;
    private boolean proxyItemVisible;
    private ActionBarMenuSubItem readItem;
    private RectF rect;
    private ActionBarMenuSubItem removeFromFolderItem;
    private long requestPeerBotId;
    private TLRPC$RequestPeerType requestPeerType;
    private boolean resetDelegate;
    private boolean rightFragmentTransitionInProgress;
    public RightSlidingDialogContainer rightSlidingDialogContainer;
    private AnimatorSet scrimAnimatorSet;
    private Paint scrimPaint;
    private ActionBarPopupWindow scrimPopupWindow;
    private ActionBarMenuSubItem[] scrimPopupWindowItems;
    private View scrimView;
    private boolean scrimViewAppearing;
    private Drawable scrimViewBackground;
    private int[] scrimViewLocation;
    private boolean scrimViewSelected;
    private float scrollAdditionalOffset;
    private boolean scrollBarVisible;
    private boolean scrollUpdated;
    private boolean scrollingManually;
    private float searchAnimationProgress;
    private boolean searchAnimationTabsDelayedCrossfade;
    private AnimatorSet searchAnimator;
    private long searchDialogId;
    private boolean searchFiltersWasShowed;
    private boolean searchIsShowed;
    private ActionBarMenuItem searchItem;
    private TLObject searchObject;
    private String searchString;
    private ViewPagerFixed.TabsView searchTabsView;
    private SearchViewPager searchViewPager;
    private boolean searchWas;
    private boolean searchWasFullyShowed;
    private boolean searching;
    private String selectAlertString;
    private String selectAlertStringGroup;
    private SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialog;
    private View selectedCountView;
    private ArrayList<Long> selectedDialogs;
    private NumberTextView selectedDialogsCountTextView;
    private ActionBarPopupWindow sendPopupWindow;
    private boolean showSetPasswordConfirm;
    private String showingSuggestion;
    private RecyclerView sideMenu;
    ValueAnimator slideBackTransitionAnimator;
    boolean slideFragmentLite;
    float slideFragmentProgress;
    private DialogCell slidingView;
    private boolean slowedReloadAfterDialogClick;
    private AnimatorSet speedAnimator;
    private ActionBarMenuItem speedItem;
    private long startArchivePullingTime;
    private boolean startedTracking;
    private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable statusDrawable;
    private ActionBarMenuItem switchItem;
    private Animator tabsAlphaAnimator;
    private AnimatorSet tabsAnimation;
    private boolean tabsAnimationInProgress;
    private float tabsYOffset;
    private TextPaint textPaint;
    private Bulletin topBulletin;
    private int topPadding;
    private UndoView[] undoView;
    private FrameLayout updateLayout;
    private AnimatorSet updateLayoutAnimator;
    private RadialProgress2 updateLayoutIcon;
    private boolean updatePullAfterScroll;
    private TextView updateTextView;
    private ViewPage[] viewPages;
    private boolean waitingForScrollFinished;
    public boolean whiteActionBar;
    private ImageView[] writeButton;
    private FrameLayout writeButtonContainer;

    /* loaded from: classes3.dex */
    public interface DialogsActivityDelegate {
        boolean didSelectDialogs(DialogsActivity dialogsActivity, ArrayList<MessagesStorage.TopicKey> arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createActionMode$33(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ float lambda$static$0(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2 * f2 * f2) + 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCommentView() {
    }

    public boolean shouldShowNextButton(DialogsActivity dialogsActivity, ArrayList<Long> arrayList, CharSequence charSequence, boolean z) {
        return false;
    }

    public MessagesStorage.TopicKey getOpenedDialogId() {
        return this.openedDialogId;
    }

    /* loaded from: classes3.dex */
    public class ViewPage extends FrameLayout {
        private DialogsAdapter animationSupportDialogsAdapter;
        private RecyclerListView animationSupportListView;
        private int archivePullViewState;
        private DialogsAdapter dialogsAdapter;
        private DialogsItemAnimator dialogsItemAnimator;
        private int dialogsType;
        private boolean isLocked;
        private ItemTouchHelper itemTouchhelper;
        private int lastItemsCount;
        private LinearLayoutManager layoutManager;
        private DialogsRecyclerView listView;
        private FlickerLoadingView progressView;
        private PullForegroundDrawable pullForegroundDrawable;
        private RecyclerItemsEnterAnimator recyclerItemsEnterAnimator;
        private RecyclerAnimationScrollHelper scrollHelper;
        private int selectedType;
        private SwipeController swipeController;
        Runnable updateListRunnable;
        boolean updating;

        public ViewPage(Context context) {
            super(context);
            this.updateListRunnable = new Runnable() { // from class: org.telegram.ui.DialogsActivity$ViewPage$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.ViewPage.this.lambda$new$0();
                }
            };
        }

        public boolean isDefaultDialogType() {
            int i = this.dialogsType;
            return i == 0 || i == 7 || i == 8;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            this.dialogsAdapter.updateList(this.listView, this.dialogsType == 0 && DialogsActivity.this.hasHiddenArchive() && this.archivePullViewState == 2, ((BaseFragment) DialogsActivity.this).actionBar.getTranslationY());
            this.listView.updateDialogsOnNextDraw = true;
            this.updating = false;
        }

        public void updateList(boolean z) {
            if (((BaseFragment) DialogsActivity.this).isPaused) {
                return;
            }
            if (z) {
                AndroidUtilities.cancelRunOnUIThread(this.updateListRunnable);
                this.listView.setItemAnimator(this.dialogsItemAnimator);
                this.updateListRunnable.run();
            } else if (this.updating) {
            } else {
                this.updating = true;
                if (!this.dialogsItemAnimator.isRunning()) {
                    this.listView.setItemAnimator(null);
                }
                AndroidUtilities.runOnUIThread(this.updateListRunnable, 36L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ContentView extends SizeNotifierFrameLayout {
        private Paint actionBarSearchPaint;
        private int inputFieldHeight;
        private int[] pos;
        private int startedTrackingPointerId;
        private int startedTrackingX;
        private int startedTrackingY;
        private VelocityTracker velocityTracker;
        private boolean wasPortrait;
        private Paint windowBackgroundPaint;

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        public ContentView(Context context) {
            super(context);
            this.actionBarSearchPaint = new Paint(1);
            this.windowBackgroundPaint = new Paint();
            this.pos = new int[2];
            this.needBlur = true;
            this.blurBehindViews.add(this);
        }

        private boolean prepareForMoving(MotionEvent motionEvent, boolean z) {
            int nextPageId = DialogsActivity.this.filterTabsView.getNextPageId(z);
            if (nextPageId < 0) {
                return false;
            }
            getParent().requestDisallowInterceptTouchEvent(true);
            DialogsActivity.this.maybeStartTracking = false;
            DialogsActivity.this.startedTracking = true;
            this.startedTrackingX = (int) (motionEvent.getX() + DialogsActivity.this.additionalOffset);
            ((BaseFragment) DialogsActivity.this).actionBar.setEnabled(false);
            DialogsActivity.this.filterTabsView.setEnabled(false);
            DialogsActivity.this.viewPages[1].selectedType = nextPageId;
            DialogsActivity.this.viewPages[1].setVisibility(0);
            DialogsActivity.this.animatingForward = z;
            DialogsActivity.this.showScrollbars(false);
            DialogsActivity.this.switchToCurrentSelectedMode(true);
            if (z) {
                DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth());
            } else {
                DialogsActivity.this.viewPages[1].setTranslationX(-DialogsActivity.this.viewPages[0].getMeasuredWidth());
            }
            return true;
        }

        @Override // android.view.View
        public void setPadding(int i, int i2, int i3, int i4) {
            DialogsActivity.this.topPadding = i2;
            DialogsActivity.this.updateContextViewPosition();
            DialogsActivity dialogsActivity = DialogsActivity.this;
            RightSlidingDialogContainer rightSlidingDialogContainer = dialogsActivity.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer != null) {
                rightSlidingDialogContainer.setFragmentViewPadding(dialogsActivity.topPadding);
            }
            DialogsActivity dialogsActivity2 = DialogsActivity.this;
            if (dialogsActivity2.whiteActionBar && dialogsActivity2.searchViewPager != null) {
                DialogsActivity.this.searchViewPager.setTranslationY(DialogsActivity.this.topPadding - DialogsActivity.this.lastMeasuredTopPadding);
            } else {
                requestLayout();
            }
        }

        public boolean checkTabsAnimationInProgress() {
            boolean z;
            if (DialogsActivity.this.tabsAnimationInProgress) {
                if (DialogsActivity.this.backAnimation) {
                    if (Math.abs(DialogsActivity.this.viewPages[0].getTranslationX()) < 1.0f) {
                        DialogsActivity.this.viewPages[0].setTranslationX(0.0f);
                        DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth() * (DialogsActivity.this.animatingForward ? 1 : -1));
                        z = true;
                    }
                    z = false;
                } else {
                    if (Math.abs(DialogsActivity.this.viewPages[1].getTranslationX()) < 1.0f) {
                        DialogsActivity.this.viewPages[0].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth() * (DialogsActivity.this.animatingForward ? -1 : 1));
                        DialogsActivity.this.viewPages[1].setTranslationX(0.0f);
                        z = true;
                    }
                    z = false;
                }
                if (z) {
                    DialogsActivity.this.showScrollbars(true);
                    if (DialogsActivity.this.tabsAnimation != null) {
                        DialogsActivity.this.tabsAnimation.cancel();
                        DialogsActivity.this.tabsAnimation = null;
                    }
                    DialogsActivity.this.tabsAnimationInProgress = false;
                }
                return DialogsActivity.this.tabsAnimationInProgress;
            }
            return false;
        }

        public int getActionBarFullHeight() {
            float height = ((BaseFragment) DialogsActivity.this).actionBar.getHeight();
            float f = 0.0f;
            float measuredHeight = (DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() == 8) ? 0.0f : DialogsActivity.this.filterTabsView.getMeasuredHeight() - ((1.0f - DialogsActivity.this.filterTabsProgress) * DialogsActivity.this.filterTabsView.getMeasuredHeight());
            if (DialogsActivity.this.searchTabsView != null && DialogsActivity.this.searchTabsView.getVisibility() != 8) {
                f = DialogsActivity.this.searchTabsView.getMeasuredHeight();
            }
            return (int) (height + (measuredHeight * (1.0f - DialogsActivity.this.searchAnimationProgress)) + (f * DialogsActivity.this.searchAnimationProgress));
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if ((view == DialogsActivity.this.fragmentContextView && DialogsActivity.this.fragmentContextView.isCallStyle()) || view == DialogsActivity.this.blurredView) {
                return true;
            }
            if (view != DialogsActivity.this.viewPages[0] && ((DialogsActivity.this.viewPages.length <= 1 || view != DialogsActivity.this.viewPages[1]) && view != DialogsActivity.this.fragmentContextView && view != DialogsActivity.this.fragmentLocationContextView && view != DialogsActivity.this.dialogsHintCell)) {
                if (view == ((BaseFragment) DialogsActivity.this).actionBar && DialogsActivity.this.slideFragmentProgress != 1.0f) {
                    canvas.save();
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    if (dialogsActivity.slideFragmentLite) {
                        canvas.translate((dialogsActivity.isDrawerTransition ? 1 : -1) * AndroidUtilities.dp(40.0f) * (1.0f - DialogsActivity.this.slideFragmentProgress), 0.0f);
                    } else {
                        float f = 1.0f - ((1.0f - dialogsActivity.slideFragmentProgress) * 0.05f);
                        canvas.translate((dialogsActivity.isDrawerTransition ? AndroidUtilities.dp(4.0f) : -AndroidUtilities.dp(4.0f)) * (1.0f - DialogsActivity.this.slideFragmentProgress), 0.0f);
                        canvas.scale(f, f, DialogsActivity.this.isDrawerTransition ? getMeasuredWidth() : 0.0f, (((BaseFragment) DialogsActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + (ActionBar.getCurrentActionBarHeight() / 2.0f));
                    }
                    boolean drawChild = super.drawChild(canvas, view, j);
                    canvas.restore();
                    return drawChild;
                }
                return super.drawChild(canvas, view, j);
            }
            canvas.save();
            canvas.clipRect(0.0f, (-getY()) + ((BaseFragment) DialogsActivity.this).actionBar.getY() + getActionBarFullHeight(), getMeasuredWidth(), getMeasuredHeight());
            DialogsActivity dialogsActivity2 = DialogsActivity.this;
            float f2 = dialogsActivity2.slideFragmentProgress;
            if (f2 != 1.0f) {
                if (dialogsActivity2.slideFragmentLite) {
                    canvas.translate((dialogsActivity2.isDrawerTransition ? 1 : -1) * AndroidUtilities.dp(40.0f) * (1.0f - DialogsActivity.this.slideFragmentProgress), 0.0f);
                } else {
                    float f3 = 1.0f - ((1.0f - f2) * 0.05f);
                    canvas.translate((dialogsActivity2.isDrawerTransition ? AndroidUtilities.dp(4.0f) : -AndroidUtilities.dp(4.0f)) * (1.0f - DialogsActivity.this.slideFragmentProgress), 0.0f);
                    canvas.scale(f3, f3, DialogsActivity.this.isDrawerTransition ? getMeasuredWidth() : 0.0f, (-getY()) + ((BaseFragment) DialogsActivity.this).actionBar.getY() + getActionBarFullHeight());
                }
            }
            boolean drawChild2 = super.drawChild(canvas, view, j);
            canvas.restore();
            return drawChild2;
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            int y;
            float f;
            Paint paint;
            int i;
            int actionBarFullHeight = getActionBarFullHeight();
            if (!((BaseFragment) DialogsActivity.this).inPreviewMode) {
                y = (int) ((-getY()) + ((BaseFragment) DialogsActivity.this).actionBar.getY());
            } else {
                y = AndroidUtilities.statusBarHeight;
            }
            int i2 = y;
            int i3 = i2 + actionBarFullHeight;
            DialogsActivity.this.rightSlidingDialogContainer.setCurrentTop(i3);
            DialogsActivity dialogsActivity = DialogsActivity.this;
            if (dialogsActivity.whiteActionBar) {
                if (dialogsActivity.searchAnimationProgress != 1.0f) {
                    if (DialogsActivity.this.searchAnimationProgress == 0.0f && DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0) {
                        DialogsActivity.this.filterTabsView.setTranslationY(((BaseFragment) DialogsActivity.this).actionBar.getTranslationY());
                    }
                } else {
                    this.actionBarSearchPaint.setColor(Theme.getColor("windowBackgroundWhite"));
                    if (DialogsActivity.this.searchTabsView != null) {
                        DialogsActivity.this.searchTabsView.setTranslationY(0.0f);
                        DialogsActivity.this.searchTabsView.setAlpha(1.0f);
                        if (DialogsActivity.this.filtersView != null) {
                            DialogsActivity.this.filtersView.setTranslationY(0.0f);
                            DialogsActivity.this.filtersView.setAlpha(1.0f);
                        }
                    }
                }
                Rect rect = AndroidUtilities.rectTmp2;
                rect.set(0, i2, getMeasuredWidth(), i3);
                drawBlurRect(canvas, 0.0f, rect, DialogsActivity.this.searchAnimationProgress == 1.0f ? this.actionBarSearchPaint : DialogsActivity.this.actionBarDefaultPaint, true);
                if (DialogsActivity.this.searchAnimationProgress > 0.0f && DialogsActivity.this.searchAnimationProgress < 1.0f) {
                    this.actionBarSearchPaint.setColor(ColorUtils.blendARGB(Theme.getColor(DialogsActivity.this.folderId != 0 ? "actionBarDefaultArchived" : "actionBarDefault"), Theme.getColor("windowBackgroundWhite"), DialogsActivity.this.searchAnimationProgress));
                    if (DialogsActivity.this.searchIsShowed || !DialogsActivity.this.searchWasFullyShowed) {
                        canvas.save();
                        canvas.clipRect(0, i2, getMeasuredWidth(), i3);
                        drawBlurCircle(canvas, 0.0f, getMeasuredWidth() - AndroidUtilities.dp(24.0f), (((BaseFragment) DialogsActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0) + ((((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight() - i) / 2.0f), getMeasuredWidth() * 1.3f * DialogsActivity.this.searchAnimationProgress, this.actionBarSearchPaint, true);
                        canvas.restore();
                    } else {
                        rect.set(0, i2, getMeasuredWidth(), i3);
                        drawBlurRect(canvas, 0.0f, rect, this.actionBarSearchPaint, true);
                    }
                    if (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0) {
                        DialogsActivity.this.filterTabsView.setTranslationY(actionBarFullHeight - (((BaseFragment) DialogsActivity.this).actionBar.getHeight() + DialogsActivity.this.filterTabsView.getMeasuredHeight()));
                    }
                    if (DialogsActivity.this.searchTabsView != null) {
                        float height = actionBarFullHeight - (((BaseFragment) DialogsActivity.this).actionBar.getHeight() + DialogsActivity.this.searchTabsView.getMeasuredHeight());
                        float f2 = DialogsActivity.this.searchAnimationTabsDelayedCrossfade ? DialogsActivity.this.searchAnimationProgress < 0.5f ? 0.0f : (DialogsActivity.this.searchAnimationProgress - 0.5f) / 0.5f : DialogsActivity.this.searchAnimationProgress;
                        DialogsActivity.this.searchTabsView.setTranslationY(height);
                        DialogsActivity.this.searchTabsView.setAlpha(f2);
                        if (DialogsActivity.this.filtersView != null) {
                            DialogsActivity.this.filtersView.setTranslationY(height);
                            DialogsActivity.this.filtersView.setAlpha(f2);
                        }
                    }
                }
            } else if (!((BaseFragment) dialogsActivity).inPreviewMode) {
                if (DialogsActivity.this.progressToActionMode > 0.0f) {
                    this.actionBarSearchPaint.setColor(ColorUtils.blendARGB(Theme.getColor(DialogsActivity.this.folderId != 0 ? "actionBarDefaultArchived" : "actionBarDefault"), Theme.getColor("windowBackgroundWhite"), DialogsActivity.this.progressToActionMode));
                    Rect rect2 = AndroidUtilities.rectTmp2;
                    rect2.set(0, i2, getMeasuredWidth(), i3);
                    drawBlurRect(canvas, 0.0f, rect2, this.actionBarSearchPaint, true);
                } else {
                    Rect rect3 = AndroidUtilities.rectTmp2;
                    rect3.set(0, i2, getMeasuredWidth(), i3);
                    drawBlurRect(canvas, 0.0f, rect3, DialogsActivity.this.actionBarDefaultPaint, true);
                }
            }
            DialogsActivity.this.tabsYOffset = 0.0f;
            if ((DialogsActivity.this.filtersTabAnimator == null && !DialogsActivity.this.rightSlidingDialogContainer.hasFragment()) || DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() != 0) {
                f = 1.0f;
                if (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0) {
                    DialogsActivity.this.filterTabsView.setTranslationY(((BaseFragment) DialogsActivity.this).actionBar.getTranslationY());
                    DialogsActivity.this.filterTabsView.setAlpha(1.0f);
                }
            } else {
                DialogsActivity dialogsActivity2 = DialogsActivity.this;
                f = 1.0f;
                dialogsActivity2.tabsYOffset = (-(1.0f - dialogsActivity2.filterTabsProgress)) * DialogsActivity.this.filterTabsView.getMeasuredHeight();
                DialogsActivity.this.filterTabsView.setTranslationY(((BaseFragment) DialogsActivity.this).actionBar.getTranslationY() + DialogsActivity.this.tabsYOffset);
                DialogsActivity.this.filterTabsView.setAlpha(DialogsActivity.this.filterTabsProgress);
                DialogsActivity.this.viewPages[0].setTranslationY((-(1.0f - DialogsActivity.this.filterTabsProgress)) * DialogsActivity.this.filterTabsMoveFrom);
            }
            DialogsActivity.this.updateContextViewPosition();
            super.dispatchDraw(canvas);
            DialogsActivity dialogsActivity3 = DialogsActivity.this;
            if (dialogsActivity3.whiteActionBar && dialogsActivity3.searchAnimationProgress > 0.0f && DialogsActivity.this.searchAnimationProgress < f && DialogsActivity.this.searchTabsView != null) {
                this.windowBackgroundPaint.setColor(Theme.getColor("windowBackgroundWhite"));
                this.windowBackgroundPaint.setAlpha((int) (paint.getAlpha() * DialogsActivity.this.searchAnimationProgress));
                canvas.drawRect(0.0f, i3, getMeasuredWidth(), i2 + ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight() + DialogsActivity.this.searchTabsView.getMeasuredHeight(), this.windowBackgroundPaint);
            }
            if (((BaseFragment) DialogsActivity.this).parentLayout != null && ((BaseFragment) DialogsActivity.this).actionBar != null && !((BaseFragment) DialogsActivity.this).actionBar.getCastShadows()) {
                int y2 = (int) (((BaseFragment) DialogsActivity.this).actionBar.getY() + getActionBarFullHeight());
                ((BaseFragment) DialogsActivity.this).parentLayout.drawHeaderShadow(canvas, (int) ((f - DialogsActivity.this.searchAnimationProgress) * 255.0f), y2);
                if (DialogsActivity.this.searchAnimationProgress > 0.0f) {
                    if (DialogsActivity.this.searchAnimationProgress < f) {
                        int alpha = Theme.dividerPaint.getAlpha();
                        Theme.dividerPaint.setAlpha((int) (alpha * DialogsActivity.this.searchAnimationProgress));
                        float f3 = y2;
                        canvas.drawLine(0.0f, f3, getMeasuredWidth(), f3, Theme.dividerPaint);
                        Theme.dividerPaint.setAlpha(alpha);
                    } else {
                        float f4 = y2;
                        canvas.drawLine(0.0f, f4, getMeasuredWidth(), f4, Theme.dividerPaint);
                    }
                }
            }
            if (DialogsActivity.this.fragmentContextView != null && DialogsActivity.this.fragmentContextView.isCallStyle()) {
                canvas.save();
                canvas.translate(DialogsActivity.this.fragmentContextView.getX(), DialogsActivity.this.fragmentContextView.getY());
                DialogsActivity dialogsActivity4 = DialogsActivity.this;
                float f5 = dialogsActivity4.slideFragmentProgress;
                if (f5 != f) {
                    if (dialogsActivity4.slideFragmentLite) {
                        canvas.translate((dialogsActivity4.isDrawerTransition ? 1 : -1) * AndroidUtilities.dp(40.0f) * (f - DialogsActivity.this.slideFragmentProgress), 0.0f);
                    } else {
                        float f6 = f - ((f - f5) * 0.05f);
                        canvas.translate((dialogsActivity4.isDrawerTransition ? AndroidUtilities.dp(4.0f) : -AndroidUtilities.dp(4.0f)) * (f - DialogsActivity.this.slideFragmentProgress), 0.0f);
                        canvas.scale(f6, f, DialogsActivity.this.isDrawerTransition ? getMeasuredWidth() : 0.0f, DialogsActivity.this.fragmentContextView.getY());
                    }
                }
                DialogsActivity.this.fragmentContextView.setDrawOverlay(true);
                DialogsActivity.this.fragmentContextView.draw(canvas);
                DialogsActivity.this.fragmentContextView.setDrawOverlay(false);
                canvas.restore();
            }
            if (DialogsActivity.this.blurredView != null && DialogsActivity.this.blurredView.getVisibility() == 0) {
                if (DialogsActivity.this.blurredView.getAlpha() != f) {
                    if (DialogsActivity.this.blurredView.getAlpha() != 0.0f) {
                        canvas.saveLayerAlpha(DialogsActivity.this.blurredView.getLeft(), DialogsActivity.this.blurredView.getTop(), DialogsActivity.this.blurredView.getRight(), DialogsActivity.this.blurredView.getBottom(), (int) (DialogsActivity.this.blurredView.getAlpha() * 255.0f), 31);
                        canvas.translate(DialogsActivity.this.blurredView.getLeft(), DialogsActivity.this.blurredView.getTop());
                        DialogsActivity.this.blurredView.draw(canvas);
                        canvas.restore();
                    }
                } else {
                    DialogsActivity.this.blurredView.draw(canvas);
                }
            }
            if (DialogsActivity.this.scrimView != null) {
                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), DialogsActivity.this.scrimPaint);
                canvas.save();
                getLocationInWindow(this.pos);
                canvas.translate(DialogsActivity.this.scrimViewLocation[0] - this.pos[0], DialogsActivity.this.scrimViewLocation[1] - (Build.VERSION.SDK_INT < 21 ? AndroidUtilities.statusBarHeight : 0));
                if (DialogsActivity.this.scrimViewBackground != null) {
                    DialogsActivity.this.scrimViewBackground.setAlpha(DialogsActivity.this.scrimViewAppearing ? 255 : (int) ((DialogsActivity.this.scrimPaint.getAlpha() / 50.0f) * 255.0f));
                    DialogsActivity.this.scrimViewBackground.setBounds(0, 0, DialogsActivity.this.scrimView.getWidth(), DialogsActivity.this.scrimView.getHeight());
                    DialogsActivity.this.scrimViewBackground.draw(canvas);
                }
                Drawable selectorDrawable = DialogsActivity.this.filterTabsView.getListView().getSelectorDrawable();
                if (DialogsActivity.this.scrimViewAppearing && selectorDrawable != null) {
                    canvas.save();
                    Rect bounds = selectorDrawable.getBounds();
                    canvas.translate(-bounds.left, -bounds.top);
                    selectorDrawable.draw(canvas);
                    canvas.restore();
                }
                DialogsActivity.this.scrimView.draw(canvas);
                if (DialogsActivity.this.scrimViewSelected) {
                    Drawable selectorDrawable2 = DialogsActivity.this.filterTabsView.getSelectorDrawable();
                    canvas.translate(-DialogsActivity.this.scrimViewLocation[0], (-selectorDrawable2.getIntrinsicHeight()) - 1);
                    selectorDrawable2.draw(canvas);
                }
                canvas.restore();
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int dp;
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            boolean z = size2 > size;
            setMeasuredDimension(size, size2);
            int paddingTop = size2 - getPaddingTop();
            if (DialogsActivity.this.doneItem != null) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) DialogsActivity.this.doneItem.getLayoutParams();
                layoutParams.topMargin = ((BaseFragment) DialogsActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0;
                layoutParams.height = ActionBar.getCurrentActionBarHeight();
            }
            measureChildWithMargins(((BaseFragment) DialogsActivity.this).actionBar, i, 0, i2, 0);
            int measureKeyboardHeight = measureKeyboardHeight();
            int childCount = getChildCount();
            float f = 0.0f;
            if (DialogsActivity.this.commentView != null) {
                measureChildWithMargins(DialogsActivity.this.commentView, i, 0, i2, 0);
                Object tag = DialogsActivity.this.commentView.getTag();
                if (tag != null && tag.equals(2)) {
                    if (measureKeyboardHeight <= AndroidUtilities.dp(20.0f) && !AndroidUtilities.isInMultiwindow) {
                        paddingTop -= DialogsActivity.this.commentView.getEmojiPadding();
                    }
                    this.inputFieldHeight = DialogsActivity.this.commentView.getMeasuredHeight();
                } else {
                    this.inputFieldHeight = 0;
                }
                if (SharedConfig.smoothKeyboard && DialogsActivity.this.commentView.isPopupShowing()) {
                    ((BaseFragment) DialogsActivity.this).fragmentView.setTranslationY(0.0f);
                    for (int i3 = 0; i3 < DialogsActivity.this.viewPages.length; i3++) {
                        if (DialogsActivity.this.viewPages[i3] != null) {
                            DialogsActivity.this.viewPages[i3].setTranslationY(0.0f);
                        }
                    }
                    if (!DialogsActivity.this.onlySelect) {
                        ((BaseFragment) DialogsActivity.this).actionBar.setTranslationY(0.0f);
                        if (DialogsActivity.this.topBulletin != null) {
                            DialogsActivity.this.topBulletin.updatePosition();
                        }
                    }
                    DialogsActivity.this.searchViewPager.setTranslationY(0.0f);
                }
            }
            int i4 = 0;
            while (i4 < childCount) {
                View childAt = getChildAt(i4);
                if (childAt != null && childAt.getVisibility() != 8 && childAt != DialogsActivity.this.commentView && childAt != ((BaseFragment) DialogsActivity.this).actionBar) {
                    if (childAt instanceof DatabaseMigrationHint) {
                        childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), (((View.MeasureSpec.getSize(i2) + measureKeyboardHeight) - this.inputFieldHeight) + AndroidUtilities.dp(2.0f)) - ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight()), 1073741824));
                    } else if (!(childAt instanceof ViewPage)) {
                        if (childAt == DialogsActivity.this.searchViewPager) {
                            DialogsActivity.this.searchViewPager.setTranslationY(0.0f);
                            childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), ((((View.MeasureSpec.getSize(i2) + measureKeyboardHeight) - this.inputFieldHeight) + AndroidUtilities.dp(2.0f)) - (DialogsActivity.this.onlySelect ? 0 : ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight())) - DialogsActivity.this.topPadding) - (DialogsActivity.this.searchTabsView == null ? 0 : AndroidUtilities.dp(44.0f)), 1073741824));
                            childAt.setPivotX(childAt.getMeasuredWidth() / 2);
                        } else if (DialogsActivity.this.commentView != null && DialogsActivity.this.commentView.isPopupView(childAt)) {
                            if (AndroidUtilities.isInMultiwindow) {
                                if (AndroidUtilities.isTablet()) {
                                    childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(320.0f), ((paddingTop - this.inputFieldHeight) - AndroidUtilities.statusBarHeight) + getPaddingTop()), 1073741824));
                                } else {
                                    childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(((paddingTop - this.inputFieldHeight) - AndroidUtilities.statusBarHeight) + getPaddingTop(), 1073741824));
                                }
                            } else {
                                childAt.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
                            }
                        } else {
                            if (childAt == DialogsActivity.this.rightSlidingDialogContainer) {
                                int size3 = View.MeasureSpec.getSize(i2);
                                DialogsActivity dialogsActivity = DialogsActivity.this;
                                int i5 = (dialogsActivity.isSlideBackTransition || dialogsActivity.isDrawerTransition) ? (int) (size3 * 0.05f) : 0;
                                dialogsActivity.rightSlidingDialogContainer.setTransitionPaddingBottom(i5);
                                childAt.measure(i, View.MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), size3 + i5), 1073741824));
                            } else {
                                measureChildWithMargins(childAt, i, 0, i2, 0);
                            }
                            i4++;
                            f = 0.0f;
                        }
                        i4++;
                        f = 0.0f;
                    } else {
                        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
                        if (DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() != 0) {
                            dp = ((((paddingTop - this.inputFieldHeight) + AndroidUtilities.dp(2.0f)) - (DialogsActivity.this.onlySelect ? 0 : ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight())) - DialogsActivity.this.topPadding) - (DialogsActivity.this.dialogsHintCell != null ? DialogsActivity.this.dialogsHintCell.height() : 0);
                        } else {
                            dp = ((((paddingTop - this.inputFieldHeight) + AndroidUtilities.dp(2.0f)) - AndroidUtilities.dp(44.0f)) - DialogsActivity.this.topPadding) - (DialogsActivity.this.dialogsHintCell != null ? DialogsActivity.this.dialogsHintCell.height() : 0);
                            if (DialogsActivity.this.rightSlidingDialogContainer.hasFragment()) {
                                dp += AndroidUtilities.dp(44.0f);
                            }
                        }
                        if (DialogsActivity.this.filtersTabAnimator != null && DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0) {
                            dp = (int) (dp + DialogsActivity.this.filterTabsMoveFrom);
                        } else {
                            childAt.setTranslationY(f);
                        }
                        DialogsActivity dialogsActivity2 = DialogsActivity.this;
                        int i6 = (dialogsActivity2.isSlideBackTransition || dialogsActivity2.isDrawerTransition) ? (int) (dp * 0.05f) : 0;
                        childAt.setPadding(childAt.getPaddingLeft(), childAt.getPaddingTop(), childAt.getPaddingRight(), i6);
                        childAt.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), dp + i6), 1073741824));
                        childAt.setPivotX(childAt.getMeasuredWidth() / 2);
                    }
                }
                i4++;
                f = 0.0f;
            }
            if (z != this.wasPortrait) {
                post(new Runnable() { // from class: org.telegram.ui.DialogsActivity$ContentView$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.ContentView.this.lambda$onMeasure$0();
                    }
                });
                this.wasPortrait = z;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMeasure$0() {
            if (DialogsActivity.this.selectAnimatedEmojiDialog != null) {
                DialogsActivity.this.selectAnimatedEmojiDialog.dismiss();
                DialogsActivity.this.selectAnimatedEmojiDialog = null;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:38:0x00a2  */
        /* JADX WARN: Removed duplicated region for block: B:45:0x00bd  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x00d2  */
        /* JADX WARN: Removed duplicated region for block: B:58:0x010e  */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            int i10;
            int measuredHeight;
            int childCount = getChildCount();
            Object tag = DialogsActivity.this.commentView != null ? DialogsActivity.this.commentView.getTag() : null;
            int measureKeyboardHeight = measureKeyboardHeight();
            int emojiPadding = (tag == null || !tag.equals(2) || measureKeyboardHeight > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow) ? 0 : DialogsActivity.this.commentView.getEmojiPadding();
            setBottomClip(emojiPadding);
            DialogsActivity dialogsActivity = DialogsActivity.this;
            dialogsActivity.lastMeasuredTopPadding = dialogsActivity.topPadding;
            int i11 = -1;
            while (i11 < childCount) {
                View childAt = i11 == -1 ? DialogsActivity.this.commentView : getChildAt(i11);
                if (childAt != null && childAt.getVisibility() != 8) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight2 = childAt.getMeasuredHeight();
                    int i12 = layoutParams.gravity;
                    if (i12 == -1) {
                        i12 = 51;
                    }
                    int i13 = i12 & 7;
                    int i14 = i12 & MessagesStorage.LAST_DB_VERSION;
                    int i15 = i13 & 7;
                    if (i15 == 1) {
                        i5 = (((i3 - i) - measuredWidth) / 2) + layoutParams.leftMargin;
                        i6 = layoutParams.rightMargin;
                    } else if (i15 == 5) {
                        i5 = i3 - measuredWidth;
                        i6 = layoutParams.rightMargin;
                    } else {
                        i7 = layoutParams.leftMargin;
                        if (i14 == 16) {
                            if (i14 == 48) {
                                i10 = layoutParams.topMargin + getPaddingTop();
                            } else if (i14 == 80) {
                                i8 = ((i4 - emojiPadding) - i2) - measuredHeight2;
                                i9 = layoutParams.bottomMargin;
                            } else {
                                i10 = layoutParams.topMargin;
                            }
                            if (DialogsActivity.this.commentView != null || !DialogsActivity.this.commentView.isPopupView(childAt)) {
                                if (childAt != DialogsActivity.this.filterTabsView || childAt == DialogsActivity.this.searchTabsView || childAt == DialogsActivity.this.filtersView) {
                                    i10 = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                                } else {
                                    if (childAt == DialogsActivity.this.searchViewPager) {
                                        i10 = (DialogsActivity.this.onlySelect ? 0 : ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight()) + DialogsActivity.this.topPadding;
                                        measuredHeight = DialogsActivity.this.searchTabsView == null ? 0 : AndroidUtilities.dp(44.0f);
                                    } else if (childAt instanceof DatabaseMigrationHint) {
                                        i10 = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                                    } else if (childAt instanceof ViewPage) {
                                        if (!DialogsActivity.this.onlySelect) {
                                            if (DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() != 0) {
                                                i10 = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                                            } else {
                                                i10 = AndroidUtilities.dp(44.0f);
                                            }
                                        }
                                        i10 += DialogsActivity.this.topPadding;
                                        if (DialogsActivity.this.dialogsHintCell != null) {
                                            measuredHeight = DialogsActivity.this.dialogsHintCell.height();
                                        }
                                    } else if (childAt instanceof DialogsHintCell) {
                                        measuredHeight = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight() + ((DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() != 0) ? 0 : DialogsActivity.this.filterTabsView.getMeasuredHeight());
                                    } else if (childAt instanceof FragmentContextView) {
                                        measuredHeight = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                                    } else if (childAt == DialogsActivity.this.floatingButtonContainer && DialogsActivity.this.selectAnimatedEmojiDialog != null) {
                                        i10 += measureKeyboardHeight;
                                    }
                                    i10 += measuredHeight;
                                }
                                childAt.layout(i7, i10, measuredWidth + i7, measuredHeight2 + i10);
                            } else if (AndroidUtilities.isInMultiwindow) {
                                i10 = DialogsActivity.this.commentView.getTop() - childAt.getMeasuredHeight();
                                measuredHeight = AndroidUtilities.dp(1.0f);
                                i10 += measuredHeight;
                                childAt.layout(i7, i10, measuredWidth + i7, measuredHeight2 + i10);
                            } else {
                                i10 = DialogsActivity.this.commentView.getBottom();
                                childAt.layout(i7, i10, measuredWidth + i7, measuredHeight2 + i10);
                            }
                        } else {
                            i8 = ((((i4 - emojiPadding) - i2) - measuredHeight2) / 2) + layoutParams.topMargin;
                            i9 = layoutParams.bottomMargin;
                        }
                        i10 = i8 - i9;
                        if (DialogsActivity.this.commentView != null) {
                        }
                        if (childAt != DialogsActivity.this.filterTabsView) {
                        }
                        i10 = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                        childAt.layout(i7, i10, measuredWidth + i7, measuredHeight2 + i10);
                    }
                    i7 = i5 - i6;
                    if (i14 == 16) {
                    }
                    i10 = i8 - i9;
                    if (DialogsActivity.this.commentView != null) {
                    }
                    if (childAt != DialogsActivity.this.filterTabsView) {
                    }
                    i10 = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
                    childAt.layout(i7, i10, measuredWidth + i7, measuredHeight2 + i10);
                }
                i11++;
            }
            DialogsActivity.this.searchViewPager.setKeyboardHeight(measureKeyboardHeight);
            notifyHeightChanged();
            DialogsActivity.this.updateContextViewPosition();
            DialogsActivity.this.updateCommentView();
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if ((actionMasked == 1 || actionMasked == 3) && ((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed()) {
                DialogsActivity.this.allowMoving = true;
            }
            if (checkTabsAnimationInProgress()) {
                return true;
            }
            return (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.isAnimatingIndicator()) || onTouchEvent(motionEvent);
        }

        @Override // android.view.ViewGroup, android.view.ViewParent
        public void requestDisallowInterceptTouchEvent(boolean z) {
            if (DialogsActivity.this.maybeStartTracking && !DialogsActivity.this.startedTracking) {
                onTouchEvent(null);
            }
            super.requestDisallowInterceptTouchEvent(z);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            float f;
            float f2;
            float measuredWidth;
            int measuredWidth2;
            if (((BaseFragment) DialogsActivity.this).parentLayout == null || DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.isEditing() || DialogsActivity.this.searching || DialogsActivity.this.rightSlidingDialogContainer.hasFragment() || ((BaseFragment) DialogsActivity.this).parentLayout.checkTransitionAnimation() || ((BaseFragment) DialogsActivity.this).parentLayout.isInPreviewMode() || ((BaseFragment) DialogsActivity.this).parentLayout.isPreviewOpenAnimationInProgress() || ((BaseFragment) DialogsActivity.this).parentLayout.getDrawerLayoutContainer().isDrawerOpened() || !((motionEvent == null || DialogsActivity.this.startedTracking || motionEvent.getY() > ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight() + ((BaseFragment) DialogsActivity.this).actionBar.getTranslationY()) && SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 5)) {
                return false;
            }
            if (motionEvent != null) {
                if (this.velocityTracker == null) {
                    this.velocityTracker = VelocityTracker.obtain();
                }
                this.velocityTracker.addMovement(motionEvent);
            }
            if (motionEvent != null && motionEvent.getAction() == 0 && checkTabsAnimationInProgress()) {
                DialogsActivity.this.startedTracking = true;
                this.startedTrackingPointerId = motionEvent.getPointerId(0);
                this.startedTrackingX = (int) motionEvent.getX();
                ((BaseFragment) DialogsActivity.this).parentLayout.getDrawerLayoutContainer().setAllowOpenDrawerBySwipe(false);
                if (DialogsActivity.this.animatingForward) {
                    if (this.startedTrackingX >= DialogsActivity.this.viewPages[0].getMeasuredWidth() + DialogsActivity.this.viewPages[0].getTranslationX()) {
                        ViewPage viewPage = DialogsActivity.this.viewPages[0];
                        DialogsActivity.this.viewPages[0] = DialogsActivity.this.viewPages[1];
                        DialogsActivity.this.viewPages[1] = viewPage;
                        DialogsActivity.this.animatingForward = false;
                        DialogsActivity dialogsActivity = DialogsActivity.this;
                        dialogsActivity.additionalOffset = dialogsActivity.viewPages[0].getTranslationX();
                        DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[0].selectedType, 1.0f);
                        DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[1].selectedType, DialogsActivity.this.additionalOffset / DialogsActivity.this.viewPages[0].getMeasuredWidth());
                        DialogsActivity.this.switchToCurrentSelectedMode(true);
                        DialogsActivity.this.viewPages[0].dialogsAdapter.resume();
                        DialogsActivity.this.viewPages[1].dialogsAdapter.pause();
                    } else {
                        DialogsActivity dialogsActivity2 = DialogsActivity.this;
                        dialogsActivity2.additionalOffset = dialogsActivity2.viewPages[0].getTranslationX();
                    }
                } else if (this.startedTrackingX < DialogsActivity.this.viewPages[1].getMeasuredWidth() + DialogsActivity.this.viewPages[1].getTranslationX()) {
                    ViewPage viewPage2 = DialogsActivity.this.viewPages[0];
                    DialogsActivity.this.viewPages[0] = DialogsActivity.this.viewPages[1];
                    DialogsActivity.this.viewPages[1] = viewPage2;
                    DialogsActivity.this.animatingForward = true;
                    DialogsActivity dialogsActivity3 = DialogsActivity.this;
                    dialogsActivity3.additionalOffset = dialogsActivity3.viewPages[0].getTranslationX();
                    DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[0].selectedType, 1.0f);
                    DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[1].selectedType, (-DialogsActivity.this.additionalOffset) / DialogsActivity.this.viewPages[0].getMeasuredWidth());
                    DialogsActivity.this.switchToCurrentSelectedMode(true);
                    DialogsActivity.this.viewPages[0].dialogsAdapter.resume();
                    DialogsActivity.this.viewPages[1].dialogsAdapter.pause();
                } else {
                    DialogsActivity dialogsActivity4 = DialogsActivity.this;
                    dialogsActivity4.additionalOffset = dialogsActivity4.viewPages[0].getTranslationX();
                }
                DialogsActivity.this.tabsAnimation.removeAllListeners();
                DialogsActivity.this.tabsAnimation.cancel();
                DialogsActivity.this.tabsAnimationInProgress = false;
            } else if (motionEvent != null && motionEvent.getAction() == 0) {
                DialogsActivity.this.additionalOffset = 0.0f;
            }
            if (motionEvent != null && motionEvent.getAction() == 0 && !DialogsActivity.this.startedTracking && !DialogsActivity.this.maybeStartTracking && DialogsActivity.this.filterTabsView.getVisibility() == 0) {
                this.startedTrackingPointerId = motionEvent.getPointerId(0);
                DialogsActivity.this.maybeStartTracking = true;
                this.startedTrackingX = (int) motionEvent.getX();
                this.startedTrackingY = (int) motionEvent.getY();
                this.velocityTracker.clear();
            } else if (motionEvent != null && motionEvent.getAction() == 2 && motionEvent.getPointerId(0) == this.startedTrackingPointerId) {
                int x = (int) ((motionEvent.getX() - this.startedTrackingX) + DialogsActivity.this.additionalOffset);
                int abs = Math.abs(((int) motionEvent.getY()) - this.startedTrackingY);
                if (DialogsActivity.this.startedTracking && ((DialogsActivity.this.animatingForward && x > 0) || (!DialogsActivity.this.animatingForward && x < 0))) {
                    if (!prepareForMoving(motionEvent, x < 0)) {
                        DialogsActivity.this.maybeStartTracking = true;
                        DialogsActivity.this.startedTracking = false;
                        DialogsActivity.this.viewPages[0].setTranslationX(0.0f);
                        DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.animatingForward ? DialogsActivity.this.viewPages[0].getMeasuredWidth() : -DialogsActivity.this.viewPages[0].getMeasuredWidth());
                        DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[1].selectedType, 0.0f);
                    }
                }
                if (!DialogsActivity.this.maybeStartTracking || DialogsActivity.this.startedTracking) {
                    if (DialogsActivity.this.startedTracking) {
                        DialogsActivity.this.viewPages[0].setTranslationX(x);
                        if (DialogsActivity.this.animatingForward) {
                            DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth() + x);
                        } else {
                            DialogsActivity.this.viewPages[1].setTranslationX(x - DialogsActivity.this.viewPages[0].getMeasuredWidth());
                        }
                        float abs2 = Math.abs(x) / DialogsActivity.this.viewPages[0].getMeasuredWidth();
                        if (!DialogsActivity.this.viewPages[1].isLocked || abs2 <= 0.3f) {
                            DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[1].selectedType, abs2);
                        } else {
                            dispatchTouchEvent(MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0));
                            DialogsActivity.this.filterTabsView.shakeLock(DialogsActivity.this.viewPages[1].selectedType);
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$ContentView$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    DialogsActivity.ContentView.this.lambda$onTouchEvent$1();
                                }
                            }, 200L);
                            return false;
                        }
                    }
                } else {
                    float pixelsInCM = AndroidUtilities.getPixelsInCM(0.3f, true);
                    int x2 = (int) (motionEvent.getX() - this.startedTrackingX);
                    if (Math.abs(x2) >= pixelsInCM && Math.abs(x2) > abs) {
                        prepareForMoving(motionEvent, x < 0);
                    }
                }
            } else if (motionEvent == null || (motionEvent.getPointerId(0) == this.startedTrackingPointerId && (motionEvent.getAction() == 3 || motionEvent.getAction() == 1 || motionEvent.getAction() == 6))) {
                this.velocityTracker.computeCurrentVelocity(1000, DialogsActivity.this.maximumVelocity);
                if (motionEvent == null || motionEvent.getAction() == 3) {
                    f = 0.0f;
                    f2 = 0.0f;
                } else {
                    f = this.velocityTracker.getXVelocity();
                    f2 = this.velocityTracker.getYVelocity();
                    if (!DialogsActivity.this.startedTracking && Math.abs(f) >= 3000.0f && Math.abs(f) > Math.abs(f2)) {
                        prepareForMoving(motionEvent, f < 0.0f);
                    }
                }
                if (DialogsActivity.this.startedTracking) {
                    float x3 = DialogsActivity.this.viewPages[0].getX();
                    DialogsActivity.this.tabsAnimation = new AnimatorSet();
                    if (DialogsActivity.this.viewPages[1].isLocked) {
                        DialogsActivity.this.backAnimation = true;
                    } else if (DialogsActivity.this.additionalOffset != 0.0f) {
                        if (Math.abs(f) <= 1500.0f) {
                            if (DialogsActivity.this.animatingForward) {
                                DialogsActivity dialogsActivity5 = DialogsActivity.this;
                                dialogsActivity5.backAnimation = dialogsActivity5.viewPages[1].getX() > ((float) (DialogsActivity.this.viewPages[0].getMeasuredWidth() >> 1));
                            } else {
                                DialogsActivity dialogsActivity6 = DialogsActivity.this;
                                dialogsActivity6.backAnimation = dialogsActivity6.viewPages[0].getX() < ((float) (DialogsActivity.this.viewPages[0].getMeasuredWidth() >> 1));
                            }
                        } else {
                            DialogsActivity dialogsActivity7 = DialogsActivity.this;
                            dialogsActivity7.backAnimation = !dialogsActivity7.animatingForward ? f >= 0.0f : f <= 0.0f;
                        }
                    } else {
                        DialogsActivity.this.backAnimation = Math.abs(x3) < ((float) DialogsActivity.this.viewPages[0].getMeasuredWidth()) / 3.0f && (Math.abs(f) < 3500.0f || Math.abs(f) < Math.abs(f2));
                    }
                    if (!DialogsActivity.this.backAnimation) {
                        measuredWidth = DialogsActivity.this.viewPages[0].getMeasuredWidth() - Math.abs(x3);
                        if (DialogsActivity.this.animatingForward) {
                            DialogsActivity.this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[0], View.TRANSLATION_X, -DialogsActivity.this.viewPages[0].getMeasuredWidth()), ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[1], View.TRANSLATION_X, 0.0f));
                        } else {
                            DialogsActivity.this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[0], View.TRANSLATION_X, DialogsActivity.this.viewPages[0].getMeasuredWidth()), ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[1], View.TRANSLATION_X, 0.0f));
                        }
                    } else {
                        measuredWidth = Math.abs(x3);
                        if (DialogsActivity.this.animatingForward) {
                            DialogsActivity.this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[0], View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[1], View.TRANSLATION_X, DialogsActivity.this.viewPages[1].getMeasuredWidth()));
                        } else {
                            DialogsActivity.this.tabsAnimation.playTogether(ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[0], View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(DialogsActivity.this.viewPages[1], View.TRANSLATION_X, -DialogsActivity.this.viewPages[1].getMeasuredWidth()));
                        }
                    }
                    DialogsActivity.this.tabsAnimation.setInterpolator(DialogsActivity.interpolator);
                    int measuredWidth3 = getMeasuredWidth();
                    float f3 = measuredWidth3 / 2;
                    float distanceInfluenceForSnapDuration = f3 + (AndroidUtilities.distanceInfluenceForSnapDuration(Math.min(1.0f, (measuredWidth * 1.0f) / measuredWidth3)) * f3);
                    float abs3 = Math.abs(f);
                    if (abs3 > 0.0f) {
                        measuredWidth2 = Math.round(Math.abs(distanceInfluenceForSnapDuration / abs3) * 1000.0f) * 4;
                    } else {
                        measuredWidth2 = (int) (((measuredWidth / getMeasuredWidth()) + 1.0f) * 100.0f);
                    }
                    DialogsActivity.this.tabsAnimation.setDuration(Math.max((int) ImageReceiver.DEFAULT_CROSSFADE_DURATION, Math.min(measuredWidth2, 600)));
                    DialogsActivity.this.tabsAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.ContentView.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            DialogsActivity.this.tabsAnimation = null;
                            if (!DialogsActivity.this.backAnimation) {
                                ViewPage viewPage3 = DialogsActivity.this.viewPages[0];
                                DialogsActivity.this.viewPages[0] = DialogsActivity.this.viewPages[1];
                                DialogsActivity.this.viewPages[1] = viewPage3;
                                DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[0].selectedType, 1.0f);
                                DialogsActivity.this.updateCounters(false);
                                DialogsActivity.this.viewPages[0].dialogsAdapter.resume();
                                DialogsActivity.this.viewPages[1].dialogsAdapter.pause();
                            }
                            DialogsActivity dialogsActivity8 = DialogsActivity.this;
                            dialogsActivity8.isFirstTab = dialogsActivity8.viewPages[0].selectedType == DialogsActivity.this.filterTabsView.getFirstTabId();
                            DialogsActivity.this.updateDrawerSwipeEnabled();
                            DialogsActivity.this.viewPages[1].setVisibility(8);
                            DialogsActivity.this.showScrollbars(true);
                            DialogsActivity.this.tabsAnimationInProgress = false;
                            DialogsActivity.this.maybeStartTracking = false;
                            ((BaseFragment) DialogsActivity.this).actionBar.setEnabled(true);
                            DialogsActivity.this.filterTabsView.setEnabled(true);
                            DialogsActivity dialogsActivity9 = DialogsActivity.this;
                            dialogsActivity9.checkListLoad(dialogsActivity9.viewPages[0]);
                        }
                    });
                    DialogsActivity.this.tabsAnimation.start();
                    DialogsActivity.this.tabsAnimationInProgress = true;
                    DialogsActivity.this.startedTracking = false;
                } else {
                    DialogsActivity dialogsActivity8 = DialogsActivity.this;
                    dialogsActivity8.isFirstTab = dialogsActivity8.viewPages[0].selectedType == DialogsActivity.this.filterTabsView.getFirstTabId();
                    DialogsActivity.this.updateDrawerSwipeEnabled();
                    DialogsActivity.this.maybeStartTracking = false;
                    ((BaseFragment) DialogsActivity.this).actionBar.setEnabled(true);
                    DialogsActivity.this.filterTabsView.setEnabled(true);
                }
                VelocityTracker velocityTracker = this.velocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    this.velocityTracker = null;
                }
            }
            return DialogsActivity.this.startedTracking;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTouchEvent$1() {
            DialogsActivity.this.showDialog(new LimitReachedBottomSheet(DialogsActivity.this, getContext(), 3, ((BaseFragment) DialogsActivity.this).currentAccount));
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
        protected void drawList(Canvas canvas, boolean z) {
            if (DialogsActivity.this.searchIsShowed) {
                if (DialogsActivity.this.searchViewPager == null || DialogsActivity.this.searchViewPager.getVisibility() != 0) {
                    return;
                }
                DialogsActivity.this.searchViewPager.drawForBlur(canvas);
                return;
            }
            for (int i = 0; i < DialogsActivity.this.viewPages.length; i++) {
                if (DialogsActivity.this.viewPages[i] != null && DialogsActivity.this.viewPages[i].getVisibility() == 0) {
                    for (int i2 = 0; i2 < DialogsActivity.this.viewPages[i].listView.getChildCount(); i2++) {
                        View childAt = DialogsActivity.this.viewPages[i].listView.getChildAt(i2);
                        if (childAt.getY() < DialogsActivity.this.viewPages[i].listView.blurTopPadding + AndroidUtilities.dp(100.0f)) {
                            int save = canvas.save();
                            canvas.translate(DialogsActivity.this.viewPages[i].getX(), DialogsActivity.this.viewPages[i].getY() + DialogsActivity.this.viewPages[i].listView.getY() + childAt.getY());
                            if (childAt instanceof DialogCell) {
                                DialogCell dialogCell = (DialogCell) childAt;
                                dialogCell.drawingForBlur = true;
                                dialogCell.draw(canvas);
                                dialogCell.drawingForBlur = false;
                            } else {
                                childAt.draw(canvas);
                            }
                            canvas.restoreToCount(save);
                        }
                    }
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public class DialogsRecyclerView extends BlurredRecyclerView {
        float animateFromSelectorPosition;
        boolean animateSwitchingSelector;
        private RecyclerListView animationSupportListView;
        LongSparseArray<View> animationSupportViewsByDialogId;
        private int appliedPaddingTop;
        private boolean firstLayout;
        private boolean ignoreLayout;
        float lastDrawSelectorY;
        private int lastListPadding;
        Paint paint;
        private final ViewPage parentPage;
        RectF rectF;
        private float rightFragmentOpenedProgress;
        private Paint selectorPaint;
        float selectorPositionProgress;
        public boolean updateDialogsOnNextDraw;

        @Override // org.telegram.ui.Components.RecyclerListView
        protected boolean updateEmptyViewAnimated() {
            return true;
        }

        public DialogsRecyclerView(Context context, ViewPage viewPage) {
            super(context);
            this.firstLayout = true;
            this.paint = new Paint();
            this.rectF = new RectF();
            this.selectorPositionProgress = 1.0f;
            this.parentPage = viewPage;
            this.additionalClipBottom = AndroidUtilities.dp(200.0f);
        }

        public void prepareSelectorForAnimation() {
            this.selectorPositionProgress = 0.0f;
            this.animateFromSelectorPosition = this.lastDrawSelectorY;
            this.animateSwitchingSelector = this.rightFragmentOpenedProgress != 0.0f;
        }

        public void setViewsOffset(float f) {
            View findViewByPosition;
            DialogsActivity.viewOffset = f;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).setTranslationY(f);
            }
            if (this.selectorPosition != -1 && (findViewByPosition = getLayoutManager().findViewByPosition(this.selectorPosition)) != null) {
                this.selectorRect.set(findViewByPosition.getLeft(), (int) (findViewByPosition.getTop() + f), findViewByPosition.getRight(), (int) (findViewByPosition.getBottom() + f));
                this.selectorDrawable.setBounds(this.selectorRect);
            }
            invalidate();
        }

        public float getViewOffset() {
            return DialogsActivity.viewOffset;
        }

        @Override // android.view.ViewGroup
        public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
            super.addView(view, i, layoutParams);
            view.setTranslationY(DialogsActivity.viewOffset);
            view.setTranslationX(0.0f);
            view.setAlpha(1.0f);
        }

        @Override // android.view.ViewGroup, android.view.ViewManager
        public void removeView(View view) {
            super.removeView(view);
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
            view.setAlpha(1.0f);
        }

        @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
        public void onDraw(Canvas canvas) {
            if (this.parentPage.pullForegroundDrawable != null && DialogsActivity.viewOffset != 0.0f) {
                int paddingTop = getPaddingTop();
                if (paddingTop != 0) {
                    canvas.save();
                    canvas.translate(0.0f, paddingTop);
                }
                this.parentPage.pullForegroundDrawable.drawOverScroll(canvas);
                if (paddingTop != 0) {
                    canvas.restore();
                }
            }
            super.onDraw(canvas);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Removed duplicated region for block: B:101:0x02d8  */
        /* JADX WARN: Removed duplicated region for block: B:98:0x029b  */
        @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void dispatchDraw(Canvas canvas) {
            float f;
            boolean z;
            float f2;
            int i;
            int i2;
            int i3;
            DialogCell dialogCell;
            float f3;
            float f4;
            DialogCell dialogCell2;
            Canvas canvas2;
            int i4;
            View view;
            DialogCell dialogCell3;
            int childAdapterPosition;
            Paint paint;
            Canvas canvas3 = canvas;
            canvas.save();
            int i5 = 0;
            if (this.rightFragmentOpenedProgress > 0.0f) {
                canvas3.clipRect(0, 0, AndroidUtilities.lerp(getMeasuredWidth(), AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize()), this.rightFragmentOpenedProgress), getMeasuredHeight());
                this.paint.setColor(Theme.getColor("chats_pinnedOverlay"));
                this.paint.setAlpha((int) (paint.getAlpha() * this.rightFragmentOpenedProgress));
                canvas.drawRect(0.0f, 0.0f, AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize()), getMeasuredHeight(), this.paint);
                int alpha = Theme.dividerPaint.getAlpha();
                Theme.dividerPaint.setAlpha((int) (this.rightFragmentOpenedProgress * alpha));
                canvas.drawRect(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize()), 0.0f, AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize()) - 1, getMeasuredHeight(), Theme.dividerPaint);
                Theme.dividerPaint.setAlpha(alpha);
            }
            int i6 = Integer.MIN_VALUE;
            int i7 = ConnectionsManager.DEFAULT_DATACENTER_ID;
            if (this.animationSupportListView != null) {
                if (this.animationSupportViewsByDialogId == null) {
                    this.animationSupportViewsByDialogId = new LongSparseArray<>();
                }
                for (int i8 = 0; i8 < this.animationSupportListView.getChildCount(); i8++) {
                    View childAt = this.animationSupportListView.getChildAt(i8);
                    if ((childAt instanceof DialogCell) && childAt.getBottom() > 0) {
                        this.animationSupportViewsByDialogId.put(((DialogCell) childAt).getDialogId(), childAt);
                    }
                }
            }
            DialogCell dialogCell4 = null;
            float f5 = 2.14748365E9f;
            float f6 = -2.14748365E9f;
            int i9 = 0;
            while (i9 < getChildCount()) {
                View childAt2 = getChildAt(i9);
                if (childAt2 instanceof DialogCell) {
                    DialogCell dialogCell5 = (DialogCell) childAt2;
                    dialogCell5.setRightFragmentOpenedProgress(this.rightFragmentOpenedProgress);
                    if (AndroidUtilities.isTablet()) {
                        dialogCell5.setDialogSelected(dialogCell5.getDialogId() == DialogsActivity.this.openedDialogId.dialogId);
                    }
                    LongSparseArray<View> longSparseArray = this.animationSupportViewsByDialogId;
                    if (longSparseArray == null || this.animationSupportListView == null) {
                        i = i9;
                    } else {
                        View view2 = longSparseArray.get(dialogCell5.getDialogId());
                        i = i9;
                        this.animationSupportViewsByDialogId.delete(dialogCell5.getDialogId());
                        if (view2 != null) {
                            int childLayoutPosition = this.animationSupportListView.getChildLayoutPosition(view2);
                            if (childLayoutPosition > i6) {
                                i6 = childLayoutPosition;
                            }
                            if (childLayoutPosition < i7) {
                                i7 = childLayoutPosition;
                            }
                            dialogCell5.collapseOffset = (view2.getTop() - dialogCell5.getTop()) * this.rightFragmentOpenedProgress;
                            if (dialogCell5.getTop() + dialogCell5.collapseOffset < f5) {
                                f5 = dialogCell5.getTop() + dialogCell5.collapseOffset;
                            }
                            float top = dialogCell5.getTop() + AndroidUtilities.lerp(dialogCell5.getMeasuredHeight(), view2.getMeasuredHeight(), this.rightFragmentOpenedProgress);
                            float f7 = dialogCell5.collapseOffset;
                            if (top + f7 > f6) {
                                f6 = top + f7;
                            }
                        }
                    }
                    if (this.updateDialogsOnNextDraw && dialogCell5.update(i5, true) && (childAdapterPosition = getChildAdapterPosition(dialogCell5)) >= 0) {
                        getAdapter().notifyItemChanged(childAdapterPosition);
                    }
                    i2 = i6;
                    if (dialogCell5.getDialogId() == DialogsActivity.this.rightSlidingDialogContainer.getCurrentFragmetDialogId()) {
                        f3 = f5;
                        f4 = f6;
                        dialogCell2 = dialogCell5;
                        dialogCell = dialogCell2;
                    } else {
                        dialogCell = dialogCell4;
                        f3 = f5;
                        f4 = f6;
                        dialogCell2 = dialogCell5;
                    }
                    i3 = i7;
                } else {
                    i = i9;
                    i2 = i6;
                    i3 = i7;
                    dialogCell = dialogCell4;
                    f3 = f5;
                    f4 = f6;
                    dialogCell2 = null;
                }
                if (this.animationSupportListView != null) {
                    int save = canvas.save();
                    canvas.translate(childAt2.getX(), childAt2.getY());
                    if (dialogCell2 == null) {
                        canvas2 = canvas;
                        i4 = save;
                        view = childAt2;
                        dialogCell3 = dialogCell2;
                        canvas.saveLayerAlpha(0.0f, 0.0f, childAt2.getMeasuredWidth(), childAt2.getMeasuredHeight(), (int) ((1.0f - this.rightFragmentOpenedProgress) * 255.0f), 31);
                    } else {
                        canvas2 = canvas;
                        i4 = save;
                        view = childAt2;
                        dialogCell3 = dialogCell2;
                    }
                    view.draw(canvas2);
                    if (dialogCell3 != null && dialogCell3 != dialogCell) {
                        dialogCell3.collapseOffset = 0.0f;
                    }
                    canvas2.restoreToCount(i4);
                } else {
                    canvas2 = canvas;
                }
                canvas3 = canvas2;
                i7 = i3;
                dialogCell4 = dialogCell;
                f5 = f3;
                f6 = f4;
                i5 = 0;
                i9 = i + 1;
                i6 = i2;
            }
            Canvas canvas4 = canvas3;
            if (dialogCell4 != null) {
                canvas.save();
                this.lastDrawSelectorY = dialogCell4.getY() + dialogCell4.collapseOffset + dialogCell4.avatarImage.getImageY();
                dialogCell4.collapseOffset = 0.0f;
                float f8 = this.selectorPositionProgress;
                if (f8 != 1.0f) {
                    float f9 = f8 + 0.08f;
                    this.selectorPositionProgress = f9;
                    this.selectorPositionProgress = Utilities.clamp(f9, 1.0f, 0.0f);
                    invalidate();
                }
                float interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(this.selectorPositionProgress);
                if (interpolation != 1.0f) {
                    float f10 = this.animateFromSelectorPosition;
                    if (f10 != -2.14748365E9f) {
                        if (Math.abs(f10 - this.lastDrawSelectorY) < getMeasuredHeight() * 0.4f) {
                            this.lastDrawSelectorY = AndroidUtilities.lerp(this.animateFromSelectorPosition, this.lastDrawSelectorY, interpolation);
                        } else {
                            z = true;
                            if (this.animateSwitchingSelector || (!z && this.animateFromSelectorPosition != -2.14748365E9f)) {
                                interpolation = this.rightFragmentOpenedProgress;
                            }
                            f2 = 1.0f - interpolation;
                            if (f2 == 1.0f) {
                                this.lastDrawSelectorY = -2.14748365E9f;
                            }
                            float f11 = (-AndroidUtilities.dp(5.0f)) * f2;
                            RectF rectF = AndroidUtilities.rectTmp;
                            rectF.set((-AndroidUtilities.dp(4.0f)) + f11, this.lastDrawSelectorY - AndroidUtilities.dp(1.0f), AndroidUtilities.dp(4.0f) + f11, this.lastDrawSelectorY + dialogCell4.avatarImage.getImageHeight() + AndroidUtilities.dp(1.0f));
                            if (this.selectorPaint == null) {
                                this.selectorPaint = new Paint(1);
                            }
                            this.selectorPaint.setColor(Theme.getColor("featuredStickers_addButton"));
                            canvas4.drawRoundRect(rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.selectorPaint);
                            canvas.restore();
                            f = -2.14748365E9f;
                        }
                    }
                }
                z = false;
                if (this.animateSwitchingSelector) {
                }
                interpolation = this.rightFragmentOpenedProgress;
                f2 = 1.0f - interpolation;
                if (f2 == 1.0f) {
                }
                float f112 = (-AndroidUtilities.dp(5.0f)) * f2;
                RectF rectF2 = AndroidUtilities.rectTmp;
                rectF2.set((-AndroidUtilities.dp(4.0f)) + f112, this.lastDrawSelectorY - AndroidUtilities.dp(1.0f), AndroidUtilities.dp(4.0f) + f112, this.lastDrawSelectorY + dialogCell4.avatarImage.getImageHeight() + AndroidUtilities.dp(1.0f));
                if (this.selectorPaint == null) {
                }
                this.selectorPaint.setColor(Theme.getColor("featuredStickers_addButton"));
                canvas4.drawRoundRect(rectF2, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.selectorPaint);
                canvas.restore();
                f = -2.14748365E9f;
            } else {
                f = -2.14748365E9f;
                this.lastDrawSelectorY = -2.14748365E9f;
            }
            if (this.animationSupportViewsByDialogId != null) {
                float f12 = 2.14748365E9f;
                for (int i10 = 0; i10 < this.animationSupportViewsByDialogId.size(); i10++) {
                    View valueAt = this.animationSupportViewsByDialogId.valueAt(i10);
                    int childLayoutPosition2 = this.animationSupportListView.getChildLayoutPosition(valueAt);
                    if (childLayoutPosition2 < i7 && valueAt.getTop() > f) {
                        f = valueAt.getTop();
                    }
                    if (childLayoutPosition2 > i6 && valueAt.getBottom() < f12) {
                        f12 = valueAt.getBottom();
                    }
                }
                for (int i11 = 0; i11 < this.animationSupportViewsByDialogId.size(); i11++) {
                    View valueAt2 = this.animationSupportViewsByDialogId.valueAt(i11);
                    if (valueAt2 instanceof DialogCell) {
                        int childLayoutPosition3 = this.animationSupportListView.getChildLayoutPosition(valueAt2);
                        DialogCell dialogCell6 = (DialogCell) valueAt2;
                        dialogCell6.isTransitionSupport = false;
                        dialogCell6.buildLayout();
                        dialogCell6.isTransitionSupport = true;
                        dialogCell6.setRightFragmentOpenedProgress(this.rightFragmentOpenedProgress);
                        int save2 = canvas.save();
                        if (childLayoutPosition3 > i6) {
                            canvas4.translate(valueAt2.getX(), (valueAt2.getBottom() + f6) - f12);
                        } else {
                            canvas4.translate(valueAt2.getX(), (valueAt2.getTop() + f6) - f);
                        }
                        valueAt2.draw(canvas4);
                        canvas4.restoreToCount(save2);
                    }
                }
                this.animationSupportViewsByDialogId.clear();
            }
            this.updateDialogsOnNextDraw = false;
            if (this.animationSupportListView != null) {
                invalidate();
            }
            if (this.animationSupportListView == null) {
                super.dispatchDraw(canvas);
            }
            if (drawMovingViewsOverlayed()) {
                this.paint.setColor(Theme.getColor("windowBackgroundWhite"));
                for (int i12 = 0; i12 < getChildCount(); i12++) {
                    View childAt3 = getChildAt(i12);
                    if (((childAt3 instanceof DialogCell) && ((DialogCell) childAt3).isMoving()) || ((childAt3 instanceof DialogsAdapter.LastEmptyView) && ((DialogsAdapter.LastEmptyView) childAt3).moving)) {
                        if (childAt3.getAlpha() != 1.0f) {
                            this.rectF.set(childAt3.getX(), childAt3.getY(), childAt3.getX() + childAt3.getMeasuredWidth(), childAt3.getY() + childAt3.getMeasuredHeight());
                            canvas4.saveLayerAlpha(this.rectF, (int) (childAt3.getAlpha() * 255.0f), 31);
                        } else {
                            canvas.save();
                        }
                        canvas4.translate(childAt3.getX(), childAt3.getY());
                        canvas.drawRect(0.0f, 0.0f, childAt3.getMeasuredWidth(), childAt3.getMeasuredHeight(), this.paint);
                        childAt3.draw(canvas4);
                        canvas.restore();
                    }
                }
                invalidate();
            }
            if (DialogsActivity.this.slidingView == null || DialogsActivity.this.pacmanAnimation == null) {
                return;
            }
            DialogsActivity.this.pacmanAnimation.draw(canvas4, DialogsActivity.this.slidingView.getTop() + (DialogsActivity.this.slidingView.getMeasuredHeight() / 2));
        }

        private boolean drawMovingViewsOverlayed() {
            return getItemAnimator() != null && getItemAnimator().isRunning();
        }

        @Override // org.telegram.ui.Components.BlurredRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean drawChild(Canvas canvas, View view, long j) {
            if (drawMovingViewsOverlayed() && (view instanceof DialogCell) && ((DialogCell) view).isMoving()) {
                return true;
            }
            return super.drawChild(canvas, view, j);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView
        public void setAdapter(RecyclerView.Adapter adapter) {
            super.setAdapter(adapter);
            this.firstLayout = true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
        public void onMeasure(int i, int i2) {
            int i3;
            RecyclerView.ViewHolder findViewHolderForAdapterPosition;
            if (DialogsActivity.this.onlySelect) {
                i3 = 0;
            } else if (DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() != 0) {
                i3 = ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight();
            } else {
                i3 = AndroidUtilities.dp(44.0f);
            }
            int findFirstVisibleItemPosition = this.parentPage.layoutManager.findFirstVisibleItemPosition();
            if (findFirstVisibleItemPosition != -1 && this.parentPage.itemTouchhelper.isIdle() && (findViewHolderForAdapterPosition = this.parentPage.listView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition)) != null) {
                int top = findViewHolderForAdapterPosition.itemView.getTop();
                if (this.parentPage.dialogsType == 0 && DialogsActivity.this.hasHiddenArchive() && this.parentPage.archivePullViewState == 2) {
                    findFirstVisibleItemPosition = Math.max(1, findFirstVisibleItemPosition);
                }
                this.ignoreLayout = true;
                this.parentPage.layoutManager.scrollToPositionWithOffset(findFirstVisibleItemPosition, (int) ((top - this.lastListPadding) + DialogsActivity.this.scrollAdditionalOffset));
                this.ignoreLayout = false;
            }
            if (!DialogsActivity.this.onlySelect) {
                this.ignoreLayout = true;
                if (DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() != 0) {
                    i3 = (!((BaseFragment) DialogsActivity.this).inPreviewMode || Build.VERSION.SDK_INT < 21) ? 0 : AndroidUtilities.statusBarHeight;
                } else {
                    i3 = ActionBar.getCurrentActionBarHeight() + (((BaseFragment) DialogsActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0);
                }
                setTopGlowOffset(i3);
                setPadding(0, i3, 0, 0);
                this.parentPage.progressView.setPaddingTop(i3);
                this.ignoreLayout = false;
            }
            if (this.firstLayout && DialogsActivity.this.getMessagesController().dialogsLoaded) {
                if (this.parentPage.dialogsType == 0 && DialogsActivity.this.hasHiddenArchive()) {
                    this.ignoreLayout = true;
                    ((LinearLayoutManager) getLayoutManager()).scrollToPositionWithOffset(1, 0);
                    this.ignoreLayout = false;
                }
                this.firstLayout = false;
            }
            super.onMeasure(i, i2);
            if (DialogsActivity.this.onlySelect || this.appliedPaddingTop == i3 || DialogsActivity.this.viewPages == null || DialogsActivity.this.viewPages.length <= 1 || DialogsActivity.this.startedTracking) {
                return;
            }
            if ((DialogsActivity.this.tabsAnimation == null || !DialogsActivity.this.tabsAnimation.isRunning()) && !DialogsActivity.this.tabsAnimationInProgress) {
                if (DialogsActivity.this.filterTabsView == null || !DialogsActivity.this.filterTabsView.isAnimatingIndicator()) {
                    DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            this.lastListPadding = getPaddingTop();
            DialogsActivity.this.scrollAdditionalOffset = 0.0f;
        }

        @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void toggleArchiveHidden(boolean z, DialogCell dialogCell) {
            SharedConfig.toggleArchiveHidden();
            if (SharedConfig.archiveHidden) {
                if (dialogCell != null) {
                    DialogsActivity.this.disableActionBarScrolling = true;
                    DialogsActivity.this.waitingForScrollFinished = true;
                    smoothScrollBy(0, dialogCell.getMeasuredHeight() + (dialogCell.getTop() - getPaddingTop()), CubicBezierInterpolator.EASE_OUT);
                    if (z) {
                        DialogsActivity.this.updatePullAfterScroll = true;
                    } else {
                        updatePullState();
                    }
                }
                DialogsActivity.this.getUndoView().showWithAction(0L, 6, null, null);
                return;
            }
            DialogsActivity.this.getUndoView().showWithAction(0L, 7, null, null);
            updatePullState();
            if (!z || dialogCell == null) {
                return;
            }
            dialogCell.resetPinnedArchiveState();
            dialogCell.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePullState() {
            this.parentPage.archivePullViewState = SharedConfig.archiveHidden ? 2 : 0;
            if (this.parentPage.pullForegroundDrawable != null) {
                this.parentPage.pullForegroundDrawable.setWillDraw(this.parentPage.archivePullViewState != 0);
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            LinearLayoutManager linearLayoutManager;
            int findFirstVisibleItemPosition;
            if (this.fastScrollAnimationRunning || DialogsActivity.this.waitingForScrollFinished || DialogsActivity.this.rightFragmentTransitionInProgress) {
                return false;
            }
            int action = motionEvent.getAction();
            if (action == 0) {
                setOverScrollMode(0);
            }
            if ((action == 1 || action == 3) && !this.parentPage.itemTouchhelper.isIdle() && this.parentPage.swipeController.swipingFolder) {
                this.parentPage.swipeController.swipeFolderBack = true;
                if (this.parentPage.itemTouchhelper.checkHorizontalSwipe(null, 4) != 0 && this.parentPage.swipeController.currentItemViewHolder != null) {
                    View view = this.parentPage.swipeController.currentItemViewHolder.itemView;
                    if (view instanceof DialogCell) {
                        DialogCell dialogCell = (DialogCell) view;
                        long dialogId = dialogCell.getDialogId();
                        if (DialogObject.isFolderDialogId(dialogId)) {
                            toggleArchiveHidden(false, dialogCell);
                        } else {
                            TLRPC$Dialog tLRPC$Dialog = DialogsActivity.this.getMessagesController().dialogs_dict.get(dialogId);
                            if (tLRPC$Dialog != null) {
                                if (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) != 1) {
                                    if (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) != 3) {
                                        if (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) != 0) {
                                            if (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 4) {
                                                ArrayList arrayList = new ArrayList();
                                                arrayList.add(Long.valueOf(dialogId));
                                                DialogsActivity.this.performSelectedDialogsAction(arrayList, 102, true, false);
                                            }
                                        } else {
                                            ArrayList arrayList2 = new ArrayList();
                                            arrayList2.add(Long.valueOf(dialogId));
                                            DialogsActivity.this.canPinCount = !DialogsActivity.this.isDialogPinned(tLRPC$Dialog) ? 1 : 0;
                                            DialogsActivity.this.performSelectedDialogsAction(arrayList2, 100, true, false);
                                        }
                                    } else if (!DialogsActivity.this.getMessagesController().isDialogMuted(dialogId, 0)) {
                                        NotificationsController.getInstance(UserConfig.selectedAccount).setDialogNotificationsSettings(dialogId, 0, 3);
                                        if (BulletinFactory.canShowBulletin(DialogsActivity.this)) {
                                            BulletinFactory.createMuteBulletin(DialogsActivity.this, 3).show();
                                        }
                                    } else {
                                        ArrayList arrayList3 = new ArrayList();
                                        arrayList3.add(Long.valueOf(dialogId));
                                        DialogsActivity dialogsActivity = DialogsActivity.this;
                                        dialogsActivity.canMuteCount = !MessagesController.getInstance(((BaseFragment) dialogsActivity).currentAccount).isDialogMuted(dialogId, 0);
                                        DialogsActivity dialogsActivity2 = DialogsActivity.this;
                                        dialogsActivity2.canUnmuteCount = dialogsActivity2.canMuteCount > 0 ? 0 : 1;
                                        DialogsActivity.this.performSelectedDialogsAction(arrayList3, 104, true, false);
                                    }
                                } else {
                                    ArrayList arrayList4 = new ArrayList();
                                    arrayList4.add(Long.valueOf(dialogId));
                                    DialogsActivity.this.canReadCount = (tLRPC$Dialog.unread_count > 0 || tLRPC$Dialog.unread_mark) ? 1 : 0;
                                    DialogsActivity.this.performSelectedDialogsAction(arrayList4, FileLoader.MEDIA_DIR_VIDEO_PUBLIC, true, false);
                                }
                            }
                        }
                    }
                }
            }
            boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (this.parentPage.dialogsType == 0 && ((action == 1 || action == 3) && this.parentPage.archivePullViewState == 2 && DialogsActivity.this.hasHiddenArchive() && (findFirstVisibleItemPosition = (linearLayoutManager = (LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition()) == 0)) {
                int paddingTop = getPaddingTop();
                View findViewByPosition = linearLayoutManager.findViewByPosition(findFirstVisibleItemPosition);
                int dp = (int) (AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 78.0f : 72.0f) * 0.85f);
                int top = (findViewByPosition.getTop() - paddingTop) + findViewByPosition.getMeasuredHeight();
                if (findViewByPosition instanceof DialogCell) {
                    long currentTimeMillis = System.currentTimeMillis() - DialogsActivity.this.startArchivePullingTime;
                    if (top < dp || currentTimeMillis < 200) {
                        DialogsActivity.this.disableActionBarScrolling = true;
                        smoothScrollBy(0, top, CubicBezierInterpolator.EASE_OUT_QUINT);
                        this.parentPage.archivePullViewState = 2;
                    } else if (this.parentPage.archivePullViewState != 1) {
                        if (getViewOffset() == 0.0f) {
                            DialogsActivity.this.disableActionBarScrolling = true;
                            smoothScrollBy(0, findViewByPosition.getTop() - paddingTop, CubicBezierInterpolator.EASE_OUT_QUINT);
                        }
                        if (!DialogsActivity.this.canShowHiddenArchive) {
                            DialogsActivity.this.canShowHiddenArchive = true;
                            performHapticFeedback(3, 2);
                            if (this.parentPage.pullForegroundDrawable != null) {
                                this.parentPage.pullForegroundDrawable.colorize(true);
                            }
                        }
                        ((DialogCell) findViewByPosition).startOutAnimation();
                        this.parentPage.archivePullViewState = 1;
                        if (AndroidUtilities.isAccessibilityScreenReaderEnabled()) {
                            AndroidUtilities.makeAccessibilityAnnouncement(LocaleController.getString(R.string.AccDescrArchivedChatsShown));
                        }
                    }
                    if (getViewOffset() != 0.0f) {
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(getViewOffset(), 0.0f);
                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$DialogsRecyclerView$$ExternalSyntheticLambda0
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                DialogsActivity.DialogsRecyclerView.this.lambda$onTouchEvent$0(valueAnimator);
                            }
                        });
                        ofFloat.setDuration(Math.max(100L, 350.0f - ((getViewOffset() / PullForegroundDrawable.getMaxOverscroll()) * 120.0f)));
                        ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                        setScrollEnabled(false);
                        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.DialogsRecyclerView.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                super.onAnimationEnd(animator);
                                DialogsRecyclerView.this.setScrollEnabled(true);
                            }
                        });
                        ofFloat.start();
                    }
                }
            }
            return onTouchEvent;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTouchEvent$0(ValueAnimator valueAnimator) {
            setViewsOffset(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (this.fastScrollAnimationRunning || DialogsActivity.this.waitingForScrollFinished || this.parentPage.dialogsItemAnimator.isRunning()) {
                return false;
            }
            if (motionEvent.getAction() == 0) {
                DialogsActivity dialogsActivity = DialogsActivity.this;
                dialogsActivity.allowSwipeDuringCurrentTouch = !((BaseFragment) dialogsActivity).actionBar.isActionModeShowed();
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.RecyclerListView
        public boolean allowSelectChildAtPosition(View view) {
            return !(view instanceof HeaderCell) || view.isClickable();
        }

        public void setOpenRightFragmentProgress(float f) {
            this.rightFragmentOpenedProgress = f;
            invalidate();
        }

        public void setAnimationSupportView(RecyclerListView recyclerListView, float f, boolean z) {
            RecyclerListView recyclerListView2 = recyclerListView == null ? this.animationSupportListView : this;
            if (recyclerListView2 == null) {
                this.animationSupportListView = recyclerListView;
                return;
            }
            int i = ConnectionsManager.DEFAULT_DATACENTER_ID;
            DialogCell dialogCell = null;
            DialogCell dialogCell2 = null;
            for (int i2 = 0; i2 < recyclerListView2.getChildCount(); i2++) {
                View childAt = recyclerListView2.getChildAt(i2);
                if (childAt instanceof DialogCell) {
                    DialogCell dialogCell3 = (DialogCell) childAt;
                    if (dialogCell3.getDialogId() == DialogsActivity.this.rightSlidingDialogContainer.getCurrentFragmetDialogId()) {
                        dialogCell = dialogCell3;
                    }
                    if (childAt.getTop() >= getPaddingTop() && dialogCell3.getDialogId() != 0 && childAt.getTop() < i) {
                        DialogCell dialogCell4 = (DialogCell) childAt;
                        i = dialogCell4.getTop();
                        dialogCell2 = dialogCell4;
                    }
                }
            }
            DialogCell dialogCell5 = (dialogCell == null || getAdapter().getItemCount() * AndroidUtilities.dp(70.0f) <= getMeasuredHeight() || ((float) (dialogCell2.getTop() - getPaddingTop())) <= ((float) (getMeasuredHeight() - getPaddingTop())) / 2.0f) ? dialogCell2 : dialogCell;
            this.animationSupportListView = recyclerListView;
            if (dialogCell5 != null) {
                if (recyclerListView != null) {
                    recyclerListView.setPadding(getPaddingLeft(), this.topPadding, getPaddingLeft(), getPaddingBottom());
                    DialogsAdapter dialogsAdapter = (DialogsAdapter) recyclerListView.getAdapter();
                    int findDialogPosition = dialogsAdapter.findDialogPosition(dialogCell5.getDialogId());
                    int top = (int) ((dialogCell5.getTop() - recyclerListView2.getPaddingTop()) + f);
                    if (findDialogPosition >= 0) {
                        ((LinearLayoutManager) recyclerListView.getLayoutManager()).scrollToPositionWithOffset(findDialogPosition, dialogsAdapter.fixScrollGap(this, findDialogPosition, top, this.parentPage.dialogsType == 0 && this.parentPage.archivePullViewState == 2 && DialogsActivity.this.hasHiddenArchive(), z));
                    }
                }
                int findDialogPosition2 = ((DialogsAdapter) getAdapter()).findDialogPosition(dialogCell5.getDialogId());
                int top2 = dialogCell5.getTop() - getPaddingTop();
                if (findDialogPosition2 >= 0) {
                    ((LinearLayoutManager) getLayoutManager()).scrollToPositionWithOffset(findDialogPosition2, top2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class SwipeController extends ItemTouchHelper.Callback {
        private RecyclerView.ViewHolder currentItemViewHolder;
        private ViewPage parentPage;
        private boolean swipeFolderBack;
        private boolean swipingFolder;

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public float getSwipeEscapeVelocity(float f) {
            return 3500.0f;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
            return 0.45f;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public float getSwipeVelocityThreshold(float f) {
            return Float.MAX_VALUE;
        }

        public SwipeController(ViewPage viewPage) {
            this.parentPage = viewPage;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            TLRPC$Dialog tLRPC$Dialog;
            if (!DialogsActivity.this.waitingForDialogsAnimationEnd(this.parentPage) && ((((BaseFragment) DialogsActivity.this).parentLayout == null || !((BaseFragment) DialogsActivity.this).parentLayout.isInPreviewMode()) && !DialogsActivity.this.rightSlidingDialogContainer.hasFragment())) {
                if (!this.swipingFolder || !this.swipeFolderBack) {
                    if (!DialogsActivity.this.onlySelect && this.parentPage.isDefaultDialogType() && DialogsActivity.this.slidingView == null) {
                        View view = viewHolder.itemView;
                        if (view instanceof DialogCell) {
                            DialogCell dialogCell = (DialogCell) view;
                            long dialogId = dialogCell.getDialogId();
                            MessagesController.DialogFilter dialogFilter = null;
                            if (!((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed(null)) {
                                if ((DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() != 0 || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) != 5) && DialogsActivity.this.allowSwipeDuringCurrentTouch && (((dialogId != DialogsActivity.this.getUserConfig().clientUserId && dialogId != 777000) || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) != 2) && (!DialogsActivity.this.getMessagesController().isPromoDialog(dialogId, false) || DialogsActivity.this.getMessagesController().promoDialogType == MessagesController.PROMO_TYPE_PSA))) {
                                    boolean z = DialogsActivity.this.folderId == 0 && (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 3 || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 1 || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 0 || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 4) && !DialogsActivity.this.rightSlidingDialogContainer.hasFragment();
                                    if (SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) == 1) {
                                        if (DialogsActivity.this.viewPages[0].dialogsType == 7 || DialogsActivity.this.viewPages[0].dialogsType == 8) {
                                            dialogFilter = DialogsActivity.this.getMessagesController().selectedDialogFilter[DialogsActivity.this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
                                        }
                                        if (dialogFilter != null && (dialogFilter.flags & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ) != 0 && (tLRPC$Dialog = DialogsActivity.this.getMessagesController().dialogs_dict.get(dialogId)) != null && !dialogFilter.alwaysShow(((BaseFragment) DialogsActivity.this).currentAccount, tLRPC$Dialog) && (tLRPC$Dialog.unread_count > 0 || tLRPC$Dialog.unread_mark)) {
                                            z = false;
                                        }
                                    }
                                    this.swipeFolderBack = false;
                                    this.swipingFolder = (z && !DialogObject.isFolderDialogId(dialogCell.getDialogId())) || (SharedConfig.archiveHidden && DialogObject.isFolderDialogId(dialogCell.getDialogId()));
                                    dialogCell.setSliding(true);
                                    return ItemTouchHelper.Callback.makeMovementFlags(0, 4);
                                }
                            } else {
                                TLRPC$Dialog tLRPC$Dialog2 = DialogsActivity.this.getMessagesController().dialogs_dict.get(dialogId);
                                if (!DialogsActivity.this.allowMoving || tLRPC$Dialog2 == null || !DialogsActivity.this.isDialogPinned(tLRPC$Dialog2) || DialogObject.isFolderDialogId(dialogId)) {
                                    return 0;
                                }
                                DialogsActivity.this.movingView = (DialogCell) viewHolder.itemView;
                                DialogsActivity.this.movingView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                                this.swipeFolderBack = false;
                                return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
                            }
                        }
                    }
                } else {
                    View view2 = viewHolder.itemView;
                    if (view2 instanceof DialogCell) {
                        ((DialogCell) view2).swipeCanceled = true;
                    }
                    this.swipingFolder = false;
                    return 0;
                }
            }
            return 0;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            long dialogId;
            TLRPC$Dialog tLRPC$Dialog;
            View view = viewHolder2.itemView;
            if ((view instanceof DialogCell) && (tLRPC$Dialog = DialogsActivity.this.getMessagesController().dialogs_dict.get((dialogId = ((DialogCell) view).getDialogId()))) != null && DialogsActivity.this.isDialogPinned(tLRPC$Dialog) && !DialogObject.isFolderDialogId(dialogId)) {
                int adapterPosition = viewHolder.getAdapterPosition();
                int adapterPosition2 = viewHolder2.getAdapterPosition();
                if (this.parentPage.listView.getItemAnimator() == null) {
                    this.parentPage.listView.setItemAnimator(this.parentPage.dialogsItemAnimator);
                }
                this.parentPage.dialogsAdapter.moveDialogs(this.parentPage.listView, adapterPosition, adapterPosition2);
                if (DialogsActivity.this.viewPages[0].dialogsType != 7 && DialogsActivity.this.viewPages[0].dialogsType != 8) {
                    DialogsActivity.this.movingWas = true;
                } else {
                    MessagesController.DialogFilter dialogFilter = DialogsActivity.this.getMessagesController().selectedDialogFilter[DialogsActivity.this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
                    if (!DialogsActivity.this.movingDialogFilters.contains(dialogFilter)) {
                        DialogsActivity.this.movingDialogFilters.add(dialogFilter);
                    }
                }
                return true;
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int convertToAbsoluteDirection(int i, int i2) {
            if (this.swipeFolderBack) {
                return 0;
            }
            return super.convertToAbsoluteDirection(i, i2);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder == null) {
                DialogsActivity.this.slidingView = null;
                return;
            }
            DialogCell dialogCell = (DialogCell) viewHolder.itemView;
            long dialogId = dialogCell.getDialogId();
            if (DialogObject.isFolderDialogId(dialogId)) {
                this.parentPage.listView.toggleArchiveHidden(false, dialogCell);
                return;
            }
            final TLRPC$Dialog tLRPC$Dialog = DialogsActivity.this.getMessagesController().dialogs_dict.get(dialogId);
            if (tLRPC$Dialog == null) {
                return;
            }
            if (DialogsActivity.this.getMessagesController().isPromoDialog(dialogId, false) || DialogsActivity.this.folderId != 0 || SharedConfig.getChatSwipeAction(((BaseFragment) DialogsActivity.this).currentAccount) != 1) {
                DialogsActivity.this.slidingView = dialogCell;
                final int adapterPosition = viewHolder.getAdapterPosition();
                final int itemCount = this.parentPage.dialogsAdapter.getItemCount();
                Runnable runnable = new Runnable() { // from class: org.telegram.ui.DialogsActivity$SwipeController$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.SwipeController.this.lambda$onSwiped$1(tLRPC$Dialog, itemCount, adapterPosition);
                    }
                };
                DialogsActivity.this.setDialogsListFrozen(true);
                if (Utilities.random.nextInt(1000) == 1) {
                    if (DialogsActivity.this.pacmanAnimation == null) {
                        DialogsActivity.this.pacmanAnimation = new PacmanAnimation(this.parentPage.listView);
                    }
                    DialogsActivity.this.pacmanAnimation.setFinishRunnable(runnable);
                    DialogsActivity.this.pacmanAnimation.start();
                    return;
                }
                runnable.run();
                return;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(Long.valueOf(dialogId));
            DialogsActivity.this.canReadCount = (tLRPC$Dialog.unread_count > 0 || tLRPC$Dialog.unread_mark) ? 1 : 0;
            DialogsActivity.this.performSelectedDialogsAction(arrayList, FileLoader.MEDIA_DIR_VIDEO_PUBLIC, true, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSwiped$1(final TLRPC$Dialog tLRPC$Dialog, int i, int i2) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition;
            if (DialogsActivity.this.frozenDialogsList == null) {
                return;
            }
            DialogsActivity.this.frozenDialogsList.remove(tLRPC$Dialog);
            final int i3 = tLRPC$Dialog.pinnedNum;
            DialogsActivity.this.slidingView = null;
            this.parentPage.listView.invalidate();
            int findLastVisibleItemPosition = this.parentPage.layoutManager.findLastVisibleItemPosition();
            if (findLastVisibleItemPosition == i - 1) {
                this.parentPage.layoutManager.findViewByPosition(findLastVisibleItemPosition).requestLayout();
            }
            boolean z = false;
            if (!DialogsActivity.this.getMessagesController().isPromoDialog(tLRPC$Dialog.id, false)) {
                int addDialogToFolder = DialogsActivity.this.getMessagesController().addDialogToFolder(tLRPC$Dialog.id, DialogsActivity.this.folderId == 0 ? 1 : 0, -1, 0L);
                if (addDialogToFolder != 2 || i2 != 0) {
                    this.parentPage.dialogsItemAnimator.prepareForRemove();
                    this.parentPage.updateList(true);
                }
                if (DialogsActivity.this.folderId == 0) {
                    if (addDialogToFolder == 2) {
                        this.parentPage.dialogsItemAnimator.prepareForRemove();
                        if (i2 == 0) {
                            DialogsActivity.this.setDialogsListFrozen(true);
                            this.parentPage.updateList(true);
                            DialogsActivity.this.checkAnimationFinished();
                        } else {
                            this.parentPage.updateList(true);
                            if (!SharedConfig.archiveHidden && this.parentPage.layoutManager.findFirstVisibleItemPosition() == 0) {
                                DialogsActivity.this.disableActionBarScrolling = true;
                                this.parentPage.listView.smoothScrollBy(0, -AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 78.0f : 72.0f));
                            }
                        }
                        DialogsActivity dialogsActivity = DialogsActivity.this;
                        DialogsActivity.this.frozenDialogsList.add(0, dialogsActivity.getDialogsArray(((BaseFragment) dialogsActivity).currentAccount, this.parentPage.dialogsType, DialogsActivity.this.folderId, false).get(0));
                    } else if (addDialogToFolder == 1 && (findViewHolderForAdapterPosition = this.parentPage.listView.findViewHolderForAdapterPosition(0)) != null) {
                        View view = findViewHolderForAdapterPosition.itemView;
                        if (view instanceof DialogCell) {
                            DialogCell dialogCell = (DialogCell) view;
                            dialogCell.checkCurrentDialogIndex(true);
                            dialogCell.animateArchiveAvatar();
                        }
                    }
                    SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                    z = (globalMainSettings.getBoolean("archivehint_l", false) || SharedConfig.archiveHidden) ? true : true;
                    if (!z) {
                        globalMainSettings.edit().putBoolean("archivehint_l", true).commit();
                    }
                    DialogsActivity.this.getUndoView().showWithAction(tLRPC$Dialog.id, z ? 2 : 3, null, new Runnable() { // from class: org.telegram.ui.DialogsActivity$SwipeController$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.SwipeController.this.lambda$onSwiped$0(tLRPC$Dialog, i3);
                        }
                    });
                }
                if (DialogsActivity.this.folderId == 0 || !DialogsActivity.this.frozenDialogsList.isEmpty()) {
                    return;
                }
                this.parentPage.listView.setEmptyView(null);
                this.parentPage.progressView.setVisibility(4);
                return;
            }
            DialogsActivity.this.getMessagesController().hidePromoDialog();
            this.parentPage.dialogsItemAnimator.prepareForRemove();
            this.parentPage.updateList(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSwiped$0(TLRPC$Dialog tLRPC$Dialog, int i) {
            DialogsActivity.this.dialogsListFrozen = true;
            DialogsActivity.this.getMessagesController().addDialogToFolder(tLRPC$Dialog.id, 0, i, 0L);
            DialogsActivity.this.dialogsListFrozen = false;
            ArrayList<TLRPC$Dialog> dialogs = DialogsActivity.this.getMessagesController().getDialogs(0);
            int indexOf = dialogs.indexOf(tLRPC$Dialog);
            if (indexOf >= 0) {
                ArrayList<TLRPC$Dialog> dialogs2 = DialogsActivity.this.getMessagesController().getDialogs(1);
                if (!dialogs2.isEmpty() || indexOf != 1) {
                    DialogsActivity.this.setDialogsListFrozen(true);
                    this.parentPage.dialogsItemAnimator.prepareForRemove();
                    this.parentPage.updateList(true);
                    DialogsActivity.this.checkAnimationFinished();
                }
                if (dialogs2.isEmpty()) {
                    dialogs.remove(0);
                    if (indexOf == 1) {
                        DialogsActivity.this.setDialogsListFrozen(true);
                        this.parentPage.updateList(true);
                        DialogsActivity.this.checkAnimationFinished();
                        return;
                    }
                    DialogsActivity.this.frozenDialogsList.remove(0);
                    this.parentPage.dialogsItemAnimator.prepareForRemove();
                    this.parentPage.updateList(true);
                    return;
                }
                return;
            }
            this.parentPage.updateList(false);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder != null) {
                this.parentPage.listView.hideSelector(false);
            }
            this.currentItemViewHolder = viewHolder;
            if (viewHolder != null) {
                View view = viewHolder.itemView;
                if (view instanceof DialogCell) {
                    ((DialogCell) view).swipeCanceled = false;
                }
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public long getAnimationDuration(RecyclerView recyclerView, int i, float f, float f2) {
            if (i == 4) {
                return 200L;
            }
            if (i == 8 && DialogsActivity.this.movingView != null) {
                final DialogCell dialogCell = DialogsActivity.this.movingView;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$SwipeController$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        dialogCell.setBackgroundDrawable(null);
                    }
                }, this.parentPage.dialogsItemAnimator.getMoveDuration());
                DialogsActivity.this.movingView = null;
            }
            return super.getAnimationDuration(recyclerView, i, f, f2);
        }
    }

    public DialogsActivity(Bundle bundle) {
        super(bundle);
        this.initialSearchType = -1;
        this.isFirstTab = true;
        this.allowGlobalSearch = true;
        this.contactsAlpha = 1.0f;
        this.undoView = new UndoView[2];
        this.scrimViewLocation = new int[2];
        this.movingDialogFilters = new ArrayList<>();
        this.actionBarDefaultPaint = new Paint();
        this.actionModeViews = new ArrayList<>();
        this.rect = new RectF();
        this.paint = new Paint(1);
        this.textPaint = new TextPaint(1);
        this.askAboutContacts = true;
        this.floatingInterpolator = new AccelerateDecelerateInterpolator();
        this.checkPermission = true;
        this.resetDelegate = true;
        this.openedDialogId = new MessagesStorage.TopicKey();
        this.selectedDialogs = new ArrayList<>();
        this.notify = true;
        this.animationIndex = -1;
        this.debugLastUpdateAction = -1;
        this.SCROLL_Y = new AnimationProperties.FloatProperty<DialogsActivity>("animationValue") { // from class: org.telegram.ui.DialogsActivity.1
            @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
            public void setValue(DialogsActivity dialogsActivity, float f) {
                dialogsActivity.setScrollY(f);
            }

            @Override // android.util.Property
            public Float get(DialogsActivity dialogsActivity) {
                return Float.valueOf(((BaseFragment) DialogsActivity.this).actionBar.getTranslationY());
            }
        };
        this.scrollBarVisible = true;
        this.isNextButton = false;
        this.slideFragmentProgress = 1.0f;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        Bundle bundle = this.arguments;
        if (bundle != null) {
            this.onlySelect = bundle.getBoolean("onlySelect", false);
            this.canSelectTopics = this.arguments.getBoolean("canSelectTopics", false);
            this.cantSendToChannels = this.arguments.getBoolean("cantSendToChannels", false);
            this.initialDialogsType = this.arguments.getInt("dialogsType", 0);
            this.selectAlertString = this.arguments.getString("selectAlertString");
            this.selectAlertStringGroup = this.arguments.getString("selectAlertStringGroup");
            this.addToGroupAlertString = this.arguments.getString("addToGroupAlertString");
            this.allowSwitchAccount = this.arguments.getBoolean("allowSwitchAccount");
            this.checkCanWrite = this.arguments.getBoolean("checkCanWrite", true);
            this.afterSignup = this.arguments.getBoolean("afterSignup", false);
            this.folderId = this.arguments.getInt("folderId", 0);
            this.resetDelegate = this.arguments.getBoolean("resetDelegate", true);
            this.messagesCount = this.arguments.getInt("messagesCount", 0);
            this.hasPoll = this.arguments.getInt("hasPoll", 0);
            this.hasInvoice = this.arguments.getBoolean("hasInvoice", false);
            this.showSetPasswordConfirm = this.arguments.getBoolean("showSetPasswordConfirm", this.showSetPasswordConfirm);
            this.arguments.getInt("otherwiseRelogin");
            this.allowGroups = this.arguments.getBoolean("allowGroups", true);
            this.allowChannels = this.arguments.getBoolean("allowChannels", true);
            this.allowUsers = this.arguments.getBoolean("allowUsers", true);
            this.allowBots = this.arguments.getBoolean("allowBots", true);
            this.closeFragment = this.arguments.getBoolean("closeFragment", true);
            this.allowGlobalSearch = this.arguments.getBoolean("allowGlobalSearch", true);
            byte[] byteArray = this.arguments.getByteArray("requestPeerType");
            if (byteArray != null) {
                try {
                    SerializedData serializedData = new SerializedData(byteArray);
                    this.requestPeerType = TLRPC$RequestPeerType.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                    serializedData.cleanup();
                } catch (Exception unused) {
                }
            }
            this.requestPeerBotId = this.arguments.getLong("requestPeerBotId", 0L);
        }
        if (this.initialDialogsType == 0) {
            this.askAboutContacts = MessagesController.getGlobalNotificationsSettings().getBoolean("askAboutContacts", true);
            SharedConfig.loadProxyList();
        }
        if (this.searchString == null) {
            this.currentConnectionState = getConnectionsManager().getConnectionState();
            getNotificationCenter().addObserver(this, NotificationCenter.dialogsNeedReload);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
            if (!this.onlySelect) {
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.closeSearchByActiveAction);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.proxySettingsChanged);
                getNotificationCenter().addObserver(this, NotificationCenter.filterSettingsUpdated);
                getNotificationCenter().addObserver(this, NotificationCenter.dialogFiltersUpdated);
                getNotificationCenter().addObserver(this, NotificationCenter.dialogsUnreadCounterChanged);
            }
            getNotificationCenter().addObserver(this, NotificationCenter.updateInterfaces);
            getNotificationCenter().addObserver(this, NotificationCenter.encryptedChatUpdated);
            getNotificationCenter().addObserver(this, NotificationCenter.contactsDidLoad);
            getNotificationCenter().addObserver(this, NotificationCenter.appDidLogout);
            getNotificationCenter().addObserver(this, NotificationCenter.openedChatChanged);
            getNotificationCenter().addObserver(this, NotificationCenter.notificationsSettingsUpdated);
            getNotificationCenter().addObserver(this, NotificationCenter.messageReceivedByAck);
            getNotificationCenter().addObserver(this, NotificationCenter.messageReceivedByServer);
            getNotificationCenter().addObserver(this, NotificationCenter.messageSendError);
            getNotificationCenter().addObserver(this, NotificationCenter.needReloadRecentDialogsSearch);
            getNotificationCenter().addObserver(this, NotificationCenter.replyMessagesDidLoad);
            getNotificationCenter().addObserver(this, NotificationCenter.reloadHints);
            getNotificationCenter().addObserver(this, NotificationCenter.didUpdateConnectionState);
            getNotificationCenter().addObserver(this, NotificationCenter.onDownloadingFilesChanged);
            getNotificationCenter().addObserver(this, NotificationCenter.needDeleteDialog);
            getNotificationCenter().addObserver(this, NotificationCenter.folderBecomeEmpty);
            getNotificationCenter().addObserver(this, NotificationCenter.newSuggestionsAvailable);
            getNotificationCenter().addObserver(this, NotificationCenter.fileLoaded);
            getNotificationCenter().addObserver(this, NotificationCenter.fileLoadFailed);
            getNotificationCenter().addObserver(this, NotificationCenter.fileLoadProgressChanged);
            getNotificationCenter().addObserver(this, NotificationCenter.dialogsUnreadReactionsCounterChanged);
            getNotificationCenter().addObserver(this, NotificationCenter.forceImportContactsStart);
            getNotificationCenter().addObserver(this, NotificationCenter.userEmojiStatusUpdated);
            getNotificationCenter().addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.appUpdateAvailable);
        }
        getNotificationCenter().addObserver(this, NotificationCenter.messagesDeleted);
        getNotificationCenter().addObserver(this, NotificationCenter.onDatabaseMigration);
        getNotificationCenter().addObserver(this, NotificationCenter.onDatabaseOpened);
        getNotificationCenter().addObserver(this, NotificationCenter.didClearDatabase);
        loadDialogs(getAccountInstance());
        getMessagesController().loadPinnedDialogs(this.folderId, 0L, null);
        if (this.databaseMigrationHint != null && !getMessagesStorage().isDatabaseMigrationInProgress()) {
            View view = this.databaseMigrationHint;
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            this.databaseMigrationHint = null;
        }
        if (this.initialDialogsType == 0) {
            AndroidUtilities.runOnUIThread(DialogsActivity$$ExternalSyntheticLambda66.INSTANCE, 350L);
        }
        return true;
    }

    public static void loadDialogs(final AccountInstance accountInstance) {
        int currentAccount = accountInstance.getCurrentAccount();
        if (dialogsLoaded[currentAccount]) {
            return;
        }
        MessagesController messagesController = accountInstance.getMessagesController();
        messagesController.loadGlobalNotificationsSettings();
        messagesController.loadDialogs(0, 0, 100, true);
        messagesController.loadHintDialogs();
        messagesController.loadUserInfo(accountInstance.getUserConfig().getCurrentUser(), false, 0);
        accountInstance.getContactsController().checkInviteText();
        accountInstance.getMediaDataController().chekAllMedia(false);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.lambda$loadDialogs$1(AccountInstance.this);
            }
        }, 200L);
        Iterator<String> it = messagesController.diceEmojies.iterator();
        while (it.hasNext()) {
            accountInstance.getMediaDataController().loadStickersByEmojiOrName(it.next(), true, true);
        }
        dialogsLoaded[currentAccount] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadDialogs$1(AccountInstance accountInstance) {
        accountInstance.getDownloadController().loadDownloadingFiles();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void preload() {
        if (SharedConfig.getDevicePerformanceClass() < 2) {
            SecretMediaViewer.getInstance();
            ChatActivity.preload(ApplicationLoader.applicationContext);
            ArticleViewer.hasInstance();
        }
    }

    public void updateStatus(TLRPC$User tLRPC$User, boolean z) {
        if (this.statusDrawable == null || this.actionBar == null) {
            return;
        }
        Long emojiStatusDocumentId = UserObject.getEmojiStatusDocumentId(tLRPC$User);
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = null;
        if (emojiStatusDocumentId != null) {
            this.statusDrawable.set(emojiStatusDocumentId.longValue(), z);
            this.actionBar.setRightDrawableOnClick(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda31
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.this.lambda$updateStatus$2(view);
                }
            });
            SelectAnimatedEmojiDialog.preload(this.currentAccount);
        } else if (tLRPC$User != null && MessagesController.getInstance(this.currentAccount).isPremiumUser(tLRPC$User)) {
            if (this.premiumStar == null) {
                this.premiumStar = getContext().getResources().getDrawable(R.drawable.msg_premium_liststar).mutate();
                this.premiumStar = new AnimatedEmojiDrawable.WrapSizeDrawable(this, this.premiumStar, AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f)) { // from class: org.telegram.ui.DialogsActivity.2
                    @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.WrapSizeDrawable, android.graphics.drawable.Drawable
                    public void draw(Canvas canvas) {
                        canvas.save();
                        canvas.translate(AndroidUtilities.dp(-2.0f), AndroidUtilities.dp(1.0f));
                        super.draw(canvas);
                        canvas.restore();
                    }
                };
            }
            this.premiumStar.setColorFilter(new PorterDuffColorFilter(Theme.getColor("profile_verifiedBackground"), PorterDuff.Mode.MULTIPLY));
            this.statusDrawable.set(this.premiumStar, z);
            this.actionBar.setRightDrawableOnClick(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda25
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.this.lambda$updateStatus$3(view);
                }
            });
            SelectAnimatedEmojiDialog.preload(this.currentAccount);
        } else {
            this.statusDrawable.set((Drawable) null, z);
            this.actionBar.setRightDrawableOnClick(null);
        }
        this.statusDrawable.setColor(Integer.valueOf(Theme.getColor("profile_verifiedBackground")));
        DrawerProfileCell.AnimatedStatusView animatedStatusView = this.animatedStatusView;
        if (animatedStatusView != null) {
            animatedStatusView.setColor(Theme.getColor("profile_verifiedBackground"));
        }
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow = this.selectAnimatedEmojiDialog;
        if (selectAnimatedEmojiDialogWindow == null || !(selectAnimatedEmojiDialogWindow.getContentView() instanceof SelectAnimatedEmojiDialog)) {
            return;
        }
        SimpleTextView titleTextView = this.actionBar.getTitleTextView();
        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = (SelectAnimatedEmojiDialog) this.selectAnimatedEmojiDialog.getContentView();
        if (titleTextView != null) {
            Drawable rightDrawable = titleTextView.getRightDrawable();
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = this.statusDrawable;
            if (rightDrawable == swapAnimatedEmojiDrawable2) {
                swapAnimatedEmojiDrawable = swapAnimatedEmojiDrawable2;
            }
        }
        selectAnimatedEmojiDialog.setScrimDrawable(swapAnimatedEmojiDrawable, titleTextView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateStatus$2(View view) {
        showSelectStatusDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateStatus$3(View view) {
        showSelectStatusDialog();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (this.searchString == null) {
            getNotificationCenter().removeObserver(this, NotificationCenter.dialogsNeedReload);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
            if (!this.onlySelect) {
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeSearchByActiveAction);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxySettingsChanged);
                getNotificationCenter().removeObserver(this, NotificationCenter.filterSettingsUpdated);
                getNotificationCenter().removeObserver(this, NotificationCenter.dialogFiltersUpdated);
                getNotificationCenter().removeObserver(this, NotificationCenter.dialogsUnreadCounterChanged);
            }
            getNotificationCenter().removeObserver(this, NotificationCenter.updateInterfaces);
            getNotificationCenter().removeObserver(this, NotificationCenter.encryptedChatUpdated);
            getNotificationCenter().removeObserver(this, NotificationCenter.contactsDidLoad);
            getNotificationCenter().removeObserver(this, NotificationCenter.appDidLogout);
            getNotificationCenter().removeObserver(this, NotificationCenter.openedChatChanged);
            getNotificationCenter().removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
            getNotificationCenter().removeObserver(this, NotificationCenter.messageReceivedByAck);
            getNotificationCenter().removeObserver(this, NotificationCenter.messageReceivedByServer);
            getNotificationCenter().removeObserver(this, NotificationCenter.messageSendError);
            getNotificationCenter().removeObserver(this, NotificationCenter.needReloadRecentDialogsSearch);
            getNotificationCenter().removeObserver(this, NotificationCenter.replyMessagesDidLoad);
            getNotificationCenter().removeObserver(this, NotificationCenter.reloadHints);
            getNotificationCenter().removeObserver(this, NotificationCenter.didUpdateConnectionState);
            getNotificationCenter().removeObserver(this, NotificationCenter.onDownloadingFilesChanged);
            getNotificationCenter().removeObserver(this, NotificationCenter.needDeleteDialog);
            getNotificationCenter().removeObserver(this, NotificationCenter.folderBecomeEmpty);
            getNotificationCenter().removeObserver(this, NotificationCenter.newSuggestionsAvailable);
            getNotificationCenter().removeObserver(this, NotificationCenter.fileLoaded);
            getNotificationCenter().removeObserver(this, NotificationCenter.fileLoadFailed);
            getNotificationCenter().removeObserver(this, NotificationCenter.fileLoadProgressChanged);
            getNotificationCenter().removeObserver(this, NotificationCenter.dialogsUnreadReactionsCounterChanged);
            getNotificationCenter().removeObserver(this, NotificationCenter.forceImportContactsStart);
            getNotificationCenter().removeObserver(this, NotificationCenter.userEmojiStatusUpdated);
            getNotificationCenter().removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.appUpdateAvailable);
        }
        getNotificationCenter().removeObserver(this, NotificationCenter.messagesDeleted);
        getNotificationCenter().removeObserver(this, NotificationCenter.onDatabaseMigration);
        getNotificationCenter().removeObserver(this, NotificationCenter.onDatabaseOpened);
        getNotificationCenter().removeObserver(this, NotificationCenter.didClearDatabase);
        ChatActivityEnterView chatActivityEnterView = this.commentView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onDestroy();
        }
        UndoView[] undoViewArr = this.undoView;
        if (undoViewArr[0] != null) {
            undoViewArr[0].hide(true, 0);
        }
        getNotificationCenter().onAnimationFinish(this.animationIndex);
        this.delegate = null;
        SuggestClearDatabaseBottomSheet.dismissDialog();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ActionBar createActionBar(Context context) {
        ActionBar actionBar = new ActionBar(context) { // from class: org.telegram.ui.DialogsActivity.3
            @Override // org.telegram.ui.ActionBar.ActionBar, android.view.View
            public void setTranslationY(float f) {
                if (f != getTranslationY() && ((BaseFragment) DialogsActivity.this).fragmentView != null) {
                    ((BaseFragment) DialogsActivity.this).fragmentView.invalidate();
                }
                super.setTranslationY(f);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.ActionBar.ActionBar
            public boolean shouldClipChild(View view) {
                return super.shouldClipChild(view) || view == DialogsActivity.this.doneItem;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.ActionBar.ActionBar, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view, long j) {
                if (!((BaseFragment) DialogsActivity.this).inPreviewMode || DialogsActivity.this.avatarContainer == null || view == DialogsActivity.this.avatarContainer) {
                    return super.drawChild(canvas, view, j);
                }
                return false;
            }

            @Override // org.telegram.ui.ActionBar.ActionBar
            public void setTitleOverlayText(String str, int i, Runnable runnable) {
                super.setTitleOverlayText(str, i, runnable);
                if (DialogsActivity.this.selectAnimatedEmojiDialog == null || !(DialogsActivity.this.selectAnimatedEmojiDialog.getContentView() instanceof SelectAnimatedEmojiDialog)) {
                    return;
                }
                SimpleTextView titleTextView = getTitleTextView();
                ((SelectAnimatedEmojiDialog) DialogsActivity.this.selectAnimatedEmojiDialog.getContentView()).setScrimDrawable((titleTextView == null || titleTextView.getRightDrawable() != DialogsActivity.this.statusDrawable) ? null : DialogsActivity.this.statusDrawable, titleTextView);
            }

            @Override // org.telegram.ui.ActionBar.ActionBar
            protected boolean onSearchChangedIgnoreTitles() {
                RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
                return rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment();
            }

            @Override // org.telegram.ui.ActionBar.ActionBar
            public void onSearchFieldVisibilityChanged(boolean z) {
                RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
                if (rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment()) {
                    getBackButton().animate().alpha(z ? 1.0f : 0.0f).start();
                }
                super.onSearchFieldVisibilityChanged(z);
            }
        };
        actionBar.setItemsBackgroundColor(Theme.getColor("actionBarDefaultSelector"), false);
        actionBar.setItemsBackgroundColor(Theme.getColor("actionBarActionModeDefaultSelector"), true);
        actionBar.setItemsColor(Theme.getColor("actionBarDefaultIcon"), false);
        actionBar.setItemsColor(Theme.getColor("actionBarActionModeDefaultIcon"), true);
        if (this.inPreviewMode || (AndroidUtilities.isTablet() && this.folderId != 0)) {
            actionBar.setOccupyStatusBar(false);
        }
        return actionBar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:234:0x0aaa  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0ab7  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x0b0a  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0ba9 A[LOOP:2: B:252:0x0ba6->B:254:0x0ba9, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:257:0x0bd2  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x0c32  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x0c78  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x0c7b  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x0c88  */
    /* JADX WARN: Removed duplicated region for block: B:280:0x0d0d  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x0d18  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x0d42  */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r13v5 */
    /* JADX WARN: Type inference failed for: r7v0 */
    /* JADX WARN: Type inference failed for: r7v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r7v5 */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(final Context context) {
        int i;
        CombinedDrawable combinedDrawable;
        int i2;
        int i3;
        FilterTabsView filterTabsView;
        int i4;
        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto;
        Drawable drawable;
        final Context context2 = context;
        ?? r13 = 0;
        this.searching = false;
        this.searchWas = false;
        this.pacmanAnimation = null;
        this.selectedDialogs.clear();
        this.maximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                Theme.createChatResources(context2, false);
            }
        });
        ActionBarMenu createMenu = this.actionBar.createMenu();
        float f = 0.0f;
        int i5 = 8;
        ?? r7 = 1;
        if (!this.onlySelect && this.searchString == null && this.folderId == 0) {
            ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, (ActionBarMenu) null, Theme.getColor("actionBarDefaultSelector"), Theme.getColor("actionBarDefaultIcon"), true);
            this.doneItem = actionBarMenuItem;
            actionBarMenuItem.setText(LocaleController.getString("Done", R.string.Done).toUpperCase());
            this.actionBar.addView(this.doneItem, LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 0.0f, 10.0f, 0.0f));
            this.doneItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda27
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.this.lambda$createView$5(view);
                }
            });
            this.doneItem.setAlpha(0.0f);
            this.doneItem.setVisibility(8);
            ProxyDrawable proxyDrawable = new ProxyDrawable(context2);
            this.proxyDrawable = proxyDrawable;
            ActionBarMenuItem addItem = createMenu.addItem(2, proxyDrawable);
            this.proxyItem = addItem;
            addItem.setContentDescription(LocaleController.getString("ProxySettings", R.string.ProxySettings));
            RLottieDrawable rLottieDrawable = new RLottieDrawable(R.raw.passcode_lock_close, "passcode_lock_close", AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
            this.passcodeDrawable = rLottieDrawable;
            ActionBarMenuItem addItem2 = createMenu.addItem(1, rLottieDrawable);
            this.passcodeItem = addItem2;
            addItem2.setContentDescription(LocaleController.getString("AccDescrPasscodeLock", R.string.AccDescrPasscodeLock));
            ActionBarMenuItem addItem3 = createMenu.addItem(3, new ColorDrawable(0));
            this.downloadsItem = addItem3;
            addItem3.addView(new DownloadProgressIcon(this.currentAccount, context2));
            this.downloadsItem.setContentDescription(LocaleController.getString("DownloadsTabs", R.string.DownloadsTabs));
            this.downloadsItem.setVisibility(8);
            updatePasscodeButton();
            updateProxyButton(false, false);
        }
        ActionBarMenuItem actionBarMenuItemSearchListener = createMenu.addItem(0, R.drawable.ic_ab_search).setIsSearchField(true, false).setActionBarMenuItemSearchListener(new 4(context2, createMenu));
        this.searchItem = actionBarMenuItemSearchListener;
        int i6 = this.initialDialogsType;
        if (i6 == 2 || i6 == 14) {
            actionBarMenuItemSearchListener.setVisibility(8);
        }
        ActionBarMenuItem actionBarMenuItem2 = this.searchItem;
        int i7 = R.string.Search;
        actionBarMenuItem2.setSearchFieldHint(LocaleController.getString("Search", i7));
        this.searchItem.setContentDescription(LocaleController.getString("Search", i7));
        String str = "actionBarDefault";
        if (this.onlySelect) {
            this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
            int i8 = this.initialDialogsType;
            if (i8 == 3 && this.selectAlertString == null) {
                this.actionBar.setTitle(LocaleController.getString("ForwardTo", R.string.ForwardTo));
            } else if (i8 == 10) {
                this.actionBar.setTitle(LocaleController.getString("SelectChats", R.string.SelectChats));
            } else {
                TLRPC$RequestPeerType tLRPC$RequestPeerType = this.requestPeerType;
                if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeUser) {
                    if (((TLRPC$TL_requestPeerTypeUser) tLRPC$RequestPeerType).bot != null) {
                        if (((TLRPC$TL_requestPeerTypeUser) tLRPC$RequestPeerType).bot.booleanValue()) {
                            this.actionBar.setTitle(LocaleController.getString("ChooseBot", R.string.ChooseBot));
                        } else {
                            this.actionBar.setTitle(LocaleController.getString("ChooseUser", R.string.ChooseUser));
                        }
                    } else {
                        this.actionBar.setTitle(LocaleController.getString("ChooseUser", R.string.ChooseUser));
                    }
                } else if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeBroadcast) {
                    this.actionBar.setTitle(LocaleController.getString("ChooseChannel", R.string.ChooseChannel));
                } else if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeChat) {
                    this.actionBar.setTitle(LocaleController.getString("ChooseGroup", R.string.ChooseGroup));
                } else {
                    this.actionBar.setTitle(LocaleController.getString("SelectChat", R.string.SelectChat));
                }
            }
            this.actionBar.setBackgroundColor(Theme.getColor("actionBarDefault"));
        } else {
            if (this.searchString != null || this.folderId != 0) {
                ActionBar actionBar = this.actionBar;
                BackDrawable backDrawable = new BackDrawable(false);
                this.backDrawable = backDrawable;
                actionBar.setBackButtonDrawable(backDrawable);
            } else {
                ActionBar actionBar2 = this.actionBar;
                MenuDrawable menuDrawable = new MenuDrawable();
                this.menuDrawable = menuDrawable;
                actionBar2.setBackButtonDrawable(menuDrawable);
                this.menuDrawable.setRoundCap();
                this.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrOpenMenu", R.string.AccDescrOpenMenu));
            }
            if (this.folderId != 0) {
                this.actionBar.setTitle(LocaleController.getString("ArchivedChats", R.string.ArchivedChats));
            } else {
                AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(null, AndroidUtilities.dp(26.0f));
                this.statusDrawable = swapAnimatedEmojiDrawable;
                swapAnimatedEmojiDrawable.center = true;
                if (BuildVars.DEBUG_VERSION) {
                    this.actionBar.setTitle(LocaleController.getString("AppNameBeta", R.string.AppNameBeta), this.statusDrawable);
                } else {
                    this.actionBar.setTitle(LocaleController.getString("AppName", R.string.AppName), this.statusDrawable);
                }
                updateStatus(UserConfig.getInstance(this.currentAccount).getCurrentUser(), false);
            }
            if (this.folderId == 0) {
                this.actionBar.setSupportsHolidayImage(true);
            }
        }
        if (!this.onlySelect) {
            this.actionBar.setAddToContainer(false);
            this.actionBar.setCastShadows(false);
            this.actionBar.setClipContent(true);
        }
        this.actionBar.setTitleActionRunnable(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$createView$6();
            }
        });
        if (this.initialDialogsType == 0 && this.folderId == 0 && !this.onlySelect && TextUtils.isEmpty(this.searchString)) {
            this.scrimPaint = new Paint() { // from class: org.telegram.ui.DialogsActivity.5
                @Override // android.graphics.Paint
                public void setAlpha(int i9) {
                    super.setAlpha(i9);
                    if (((BaseFragment) DialogsActivity.this).fragmentView != null) {
                        ((BaseFragment) DialogsActivity.this).fragmentView.invalidate();
                    }
                }
            };
            6 r0 = new 6(context2);
            this.filterTabsView = r0;
            r0.setVisibility(8);
            this.canShowFilterTabsView = false;
            this.filterTabsView.setDelegate(new 7(context2));
        }
        int i9 = 17;
        if (this.allowSwitchAccount && UserConfig.getActivatedAccountsCount() > 1) {
            this.switchItem = createMenu.addItemWithWidth(1, 0, AndroidUtilities.dp(56.0f));
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
            BackupImageView backupImageView = new BackupImageView(context2);
            backupImageView.setRoundRadius(AndroidUtilities.dp(18.0f));
            this.switchItem.addView(backupImageView, LayoutHelper.createFrame(36, 36, 17));
            TLRPC$User currentUser = getUserConfig().getCurrentUser();
            avatarDrawable.setInfo(currentUser);
            backupImageView.getImageReceiver().setCurrentAccount(this.currentAccount);
            backupImageView.setImage(ImageLocation.getForUserOrChat(currentUser, 1), "50_50", ImageLocation.getForUserOrChat(currentUser, 2), "50_50", (currentUser == null || (tLRPC$UserProfilePhoto = currentUser.photo) == null || (drawable = tLRPC$UserProfilePhoto.strippedBitmap) == null) ? avatarDrawable : drawable, currentUser);
            int i10 = 0;
            for (int i11 = 4; i10 < i11; i11 = 4) {
                if (AccountInstance.getInstance(i10).getUserConfig().getCurrentUser() != null) {
                    AccountSelectCell accountSelectCell = new AccountSelectCell(context2, false);
                    accountSelectCell.setAccount(i10, true);
                    this.switchItem.addSubItem(i10 + 10, accountSelectCell, AndroidUtilities.dp(230.0f), AndroidUtilities.dp(48.0f));
                }
                i10++;
            }
        }
        this.actionBar.setAllowOverlayTitle(true);
        RecyclerView recyclerView = this.sideMenu;
        if (recyclerView != null) {
            recyclerView.setBackgroundColor(Theme.getColor("chats_menuBackground"));
            this.sideMenu.setGlowColor(Theme.getColor("chats_menuBackground"));
            this.sideMenu.getAdapter().notifyDataSetChanged();
        }
        createActionMode(null);
        final ContentView contentView = new ContentView(context2);
        this.fragmentView = contentView;
        int i12 = (this.folderId == 0 && this.initialDialogsType == 0 && !this.onlySelect) ? 2 : 1;
        this.viewPages = new ViewPage[i12];
        int i13 = 0;
        while (i13 < i12) {
            final ViewPage viewPage = new ViewPage(context2) { // from class: org.telegram.ui.DialogsActivity.8
                @Override // android.view.View
                public void setTranslationX(float f2) {
                    if (getTranslationX() != f2) {
                        super.setTranslationX(f2);
                        if (DialogsActivity.this.tabsAnimationInProgress && DialogsActivity.this.viewPages[0] == this) {
                            DialogsActivity.this.filterTabsView.selectTabWithId(DialogsActivity.this.viewPages[1].selectedType, Math.abs(DialogsActivity.this.viewPages[0].getTranslationX()) / DialogsActivity.this.viewPages[0].getMeasuredWidth());
                        }
                        contentView.invalidateBlur();
                    }
                }
            };
            contentView.addView(viewPage, LayoutHelper.createFrame(-1, -1.0f));
            viewPage.dialogsType = this.initialDialogsType;
            this.viewPages[i13] = viewPage;
            viewPage.progressView = new FlickerLoadingView(context2);
            viewPage.progressView.setViewType(7);
            viewPage.progressView.setVisibility(i5);
            viewPage.addView(viewPage.progressView, LayoutHelper.createFrame(-2, -2, i9));
            viewPage.listView = new DialogsRecyclerView(context2, viewPage);
            viewPage.listView.setAllowStopHeaveOperations(r7);
            viewPage.listView.setAccessibilityEnabled(r13);
            viewPage.listView.setAnimateEmptyView(r7, r13);
            viewPage.listView.setClipToPadding(r13);
            viewPage.listView.setPivotY(f);
            if (this.initialDialogsType == 15) {
                viewPage.listView.setBackgroundColor(getThemedColor("windowBackgroundGray"));
            }
            viewPage.dialogsItemAnimator = new DialogsItemAnimator(this, viewPage.listView) { // from class: org.telegram.ui.DialogsActivity.9
                @Override // androidx.recyclerview.widget.SimpleItemAnimator
                public void onRemoveStarting(RecyclerView.ViewHolder viewHolder) {
                    super.onRemoveStarting(viewHolder);
                    if (viewPage.layoutManager.findFirstVisibleItemPosition() == 0) {
                        View findViewByPosition = viewPage.layoutManager.findViewByPosition(0);
                        if (findViewByPosition != null) {
                            findViewByPosition.invalidate();
                        }
                        if (viewPage.archivePullViewState == 2) {
                            viewPage.archivePullViewState = 1;
                        }
                        if (viewPage.pullForegroundDrawable != null) {
                            viewPage.pullForegroundDrawable.doNotShow();
                        }
                    }
                }
            };
            viewPage.listView.setVerticalScrollBarEnabled(r7);
            viewPage.listView.setInstantClick(r7);
            viewPage.layoutManager = new 10(context2, viewPage);
            viewPage.layoutManager.setOrientation(r7);
            viewPage.listView.setLayoutManager(viewPage.layoutManager);
            viewPage.listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
            viewPage.addView(viewPage.listView, LayoutHelper.createFrame(-1, -1.0f));
            viewPage.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda90
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i14) {
                    DialogsActivity.this.lambda$createView$7(viewPage, view, i14);
                }
            });
            viewPage.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListenerExtended() { // from class: org.telegram.ui.DialogsActivity.12
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public boolean onItemClick(View view, int i14, float f2, float f3) {
                    if (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0 && DialogsActivity.this.filterTabsView.isEditing()) {
                        return false;
                    }
                    return DialogsActivity.this.onItemLongClick(viewPage.listView, view, i14, f2, f3, viewPage.dialogsType, viewPage.dialogsAdapter);
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public void onMove(float f2, float f3) {
                    Point point = AndroidUtilities.displaySize;
                    if (point.x > point.y) {
                        DialogsActivity.this.movePreviewFragment(f3);
                    }
                }

                @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
                public void onLongClickRelease() {
                    Point point = AndroidUtilities.displaySize;
                    if (point.x > point.y) {
                        DialogsActivity.this.finishPreviewFragment();
                    }
                }
            });
            viewPage.swipeController = new SwipeController(viewPage);
            viewPage.recyclerItemsEnterAnimator = new RecyclerItemsEnterAnimator(viewPage.listView, r13);
            viewPage.itemTouchhelper = new ItemTouchHelper(viewPage.swipeController);
            viewPage.itemTouchhelper.attachToRecyclerView(viewPage.listView);
            viewPage.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.DialogsActivity.13
                private boolean wasManualScroll;

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView2, int i14) {
                    if (i14 != 1) {
                        DialogsActivity.this.scrollingManually = false;
                    } else {
                        this.wasManualScroll = true;
                        DialogsActivity.this.scrollingManually = true;
                    }
                    if (i14 == 0) {
                        this.wasManualScroll = false;
                        DialogsActivity.this.disableActionBarScrolling = false;
                        if (DialogsActivity.this.waitingForScrollFinished) {
                            DialogsActivity.this.waitingForScrollFinished = false;
                            if (DialogsActivity.this.updatePullAfterScroll) {
                                viewPage.listView.updatePullState();
                                DialogsActivity.this.updatePullAfterScroll = false;
                            }
                            viewPage.dialogsAdapter.notifyDataSetChanged();
                        }
                        if (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0 && DialogsActivity.this.viewPages[0].listView == recyclerView2) {
                            int i15 = (int) (-((BaseFragment) DialogsActivity.this).actionBar.getTranslationY());
                            int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
                            if (i15 == 0 || i15 == currentActionBarHeight) {
                                return;
                            }
                            if (i15 < currentActionBarHeight / 2) {
                                if (DialogsActivity.this.viewPages[0].listView.canScrollVertically(-1)) {
                                    recyclerView2.smoothScrollBy(0, -i15);
                                }
                            } else if (DialogsActivity.this.viewPages[0].listView.canScrollVertically(1)) {
                                recyclerView2.smoothScrollBy(0, currentActionBarHeight - i15);
                            }
                        }
                    }
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView2, int i14, int i15) {
                    View childAt;
                    int findFirstVisibleItemPosition;
                    boolean z;
                    boolean z2;
                    viewPage.dialogsItemAnimator.onListScroll(-i15);
                    DialogsActivity.this.checkListLoad(viewPage);
                    if (DialogsActivity.this.initialDialogsType != 10 && this.wasManualScroll && DialogsActivity.this.floatingButtonContainer.getVisibility() != 8 && recyclerView2.getChildCount() > 0 && (findFirstVisibleItemPosition = viewPage.layoutManager.findFirstVisibleItemPosition()) != -1) {
                        RecyclerView.ViewHolder findViewHolderForAdapterPosition = recyclerView2.findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
                        if (!DialogsActivity.this.hasHiddenArchive() || (findViewHolderForAdapterPosition != null && findViewHolderForAdapterPosition.getAdapterPosition() != 0)) {
                            int top = findViewHolderForAdapterPosition != null ? findViewHolderForAdapterPosition.itemView.getTop() : 0;
                            if (DialogsActivity.this.prevPosition == findFirstVisibleItemPosition) {
                                int i16 = DialogsActivity.this.prevTop - top;
                                z = top < DialogsActivity.this.prevTop;
                                if (Math.abs(i16) <= 1) {
                                    z2 = false;
                                    if (z2 && DialogsActivity.this.scrollUpdated && (z || DialogsActivity.this.scrollingManually)) {
                                        DialogsActivity.this.hideFloatingButton(z);
                                    }
                                    DialogsActivity.this.prevPosition = findFirstVisibleItemPosition;
                                    DialogsActivity.this.prevTop = top;
                                    DialogsActivity.this.scrollUpdated = true;
                                }
                            } else {
                                z = findFirstVisibleItemPosition > DialogsActivity.this.prevPosition;
                            }
                            z2 = true;
                            if (z2) {
                                DialogsActivity.this.hideFloatingButton(z);
                            }
                            DialogsActivity.this.prevPosition = findFirstVisibleItemPosition;
                            DialogsActivity.this.prevTop = top;
                            DialogsActivity.this.scrollUpdated = true;
                        }
                    }
                    if (DialogsActivity.this.filterTabsView != null && DialogsActivity.this.filterTabsView.getVisibility() == 0 && recyclerView2 == DialogsActivity.this.viewPages[0].listView && !DialogsActivity.this.searching && !((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed() && !DialogsActivity.this.disableActionBarScrolling && DialogsActivity.this.filterTabsViewIsVisible && !DialogsActivity.this.rightSlidingDialogContainer.hasFragment()) {
                        if (i15 > 0 && DialogsActivity.this.hasHiddenArchive() && DialogsActivity.this.viewPages[0].dialogsType == 0 && (childAt = recyclerView2.getChildAt(0)) != null && recyclerView2.getChildViewHolder(childAt).getAdapterPosition() == 0) {
                            int measuredHeight = childAt.getMeasuredHeight() + (childAt.getTop() - recyclerView2.getPaddingTop());
                            if (measuredHeight + i15 > 0) {
                                if (measuredHeight >= 0) {
                                    return;
                                }
                                i15 = -measuredHeight;
                            }
                        }
                        float translationY = ((BaseFragment) DialogsActivity.this).actionBar.getTranslationY();
                        float f2 = translationY - i15;
                        if (f2 < (-ActionBar.getCurrentActionBarHeight())) {
                            f2 = -ActionBar.getCurrentActionBarHeight();
                        } else if (f2 > 0.0f) {
                            f2 = 0.0f;
                        }
                        if (f2 != translationY) {
                            DialogsActivity.this.setScrollY(f2);
                        }
                    }
                    if (((BaseFragment) DialogsActivity.this).fragmentView != null) {
                        ((SizeNotifierFrameLayout) ((BaseFragment) DialogsActivity.this).fragmentView).invalidateBlur();
                    }
                    RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
                    if (rightSlidingDialogContainer == null || !rightSlidingDialogContainer.hasFragment()) {
                        return;
                    }
                    viewPage.listView.invalidate();
                }
            });
            viewPage.archivePullViewState = SharedConfig.archiveHidden ? 2 : 0;
            if (viewPage.pullForegroundDrawable == null && this.folderId == 0) {
                viewPage.pullForegroundDrawable = new PullForegroundDrawable(this, LocaleController.getString("AccSwipeForArchive", R.string.AccSwipeForArchive), LocaleController.getString("AccReleaseForArchive", R.string.AccReleaseForArchive)) { // from class: org.telegram.ui.DialogsActivity.14
                    @Override // org.telegram.ui.Components.PullForegroundDrawable
                    protected float getViewOffset() {
                        return viewPage.listView.getViewOffset();
                    }
                };
                if (hasHiddenArchive()) {
                    viewPage.pullForegroundDrawable.showHidden();
                } else {
                    viewPage.pullForegroundDrawable.doNotShow();
                }
                viewPage.pullForegroundDrawable.setWillDraw(viewPage.archivePullViewState != 0);
            }
            int i14 = i13;
            int i15 = i12;
            ContentView contentView2 = contentView;
            String str2 = str;
            viewPage.dialogsAdapter = new DialogsAdapter(this, context, viewPage.dialogsType, this.folderId, this.onlySelect, this.selectedDialogs, this.currentAccount, this.requestPeerType) { // from class: org.telegram.ui.DialogsActivity.15
                @Override // org.telegram.ui.Adapters.DialogsAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
                public void notifyDataSetChanged() {
                    viewPage.lastItemsCount = getItemCount();
                    try {
                        super.notifyDataSetChanged();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    if (DialogsActivity.this.initialDialogsType == 15) {
                        DialogsActivity.this.searchItem.setVisibility(this.isEmpty ? 8 : 0);
                    }
                }

                @Override // org.telegram.ui.Adapters.DialogsAdapter, org.telegram.ui.Cells.DialogCell.DialogCellDelegate
                public void onButtonClicked(DialogCell dialogCell) {
                    TLRPC$TL_forumTopic findTopic;
                    if (dialogCell.getMessage() == null || (findTopic = DialogsActivity.this.getMessagesController().getTopicsController().findTopic(-dialogCell.getDialogId(), MessageObject.getTopicId(dialogCell.getMessage().messageOwner, true))) == null) {
                        return;
                    }
                    if (DialogsActivity.this.onlySelect) {
                        DialogsActivity.this.didSelectResult(dialogCell.getDialogId(), findTopic.id, false, false);
                    } else {
                        ForumUtilities.openTopic(DialogsActivity.this, -dialogCell.getDialogId(), findTopic, 0);
                    }
                }

                @Override // org.telegram.ui.Adapters.DialogsAdapter, org.telegram.ui.Cells.DialogCell.DialogCellDelegate
                public void onButtonLongPress(DialogCell dialogCell) {
                    DialogsActivity.this.onItemLongClick(viewPage.listView, dialogCell, viewPage.listView.getChildAdapterPosition(dialogCell), 0.0f, 0.0f, viewPage.dialogsType, viewPage.dialogsAdapter);
                }

                @Override // org.telegram.ui.Adapters.DialogsAdapter
                public void onCreateGroupForThisClick() {
                    DialogsActivity.this.createGroupForThis();
                }
            };
            viewPage.dialogsAdapter.setRecyclerListView(viewPage.listView);
            viewPage.dialogsAdapter.setForceShowEmptyCell(this.afterSignup);
            if (AndroidUtilities.isTablet() && this.openedDialogId.dialogId != 0) {
                viewPage.dialogsAdapter.setOpenedDialogId(this.openedDialogId.dialogId);
            }
            viewPage.dialogsAdapter.setArchivedPullDrawable(viewPage.pullForegroundDrawable);
            viewPage.listView.setAdapter(viewPage.dialogsAdapter);
            viewPage.listView.setEmptyView(this.folderId == 0 ? viewPage.progressView : null);
            viewPage.scrollHelper = new RecyclerAnimationScrollHelper(viewPage.listView, viewPage.layoutManager);
            if (i14 != 0) {
                this.viewPages[i14].setVisibility(8);
            }
            i13 = i14 + 1;
            context2 = context;
            contentView = contentView2;
            i12 = i15;
            str = str2;
            i9 = 17;
            r7 = 1;
            i5 = 8;
            f = 0.0f;
            r13 = 0;
        }
        final ContentView contentView3 = contentView;
        String str3 = str;
        if (this.searchString != null) {
            i = 2;
        } else {
            i = !this.onlySelect ? 1 : 0;
        }
        SearchViewPager searchViewPager = new SearchViewPager(context, this, i, this.initialDialogsType, this.folderId, new SearchViewPager.ChatPreviewDelegate() { // from class: org.telegram.ui.DialogsActivity.16
            @Override // org.telegram.ui.Components.SearchViewPager.ChatPreviewDelegate
            public void startChatPreview(RecyclerListView recyclerListView, DialogCell dialogCell) {
                DialogsActivity.this.showChatPreview(dialogCell);
            }

            @Override // org.telegram.ui.Components.SearchViewPager.ChatPreviewDelegate
            public void move(float f2) {
                Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    DialogsActivity.this.movePreviewFragment(f2);
                }
            }

            @Override // org.telegram.ui.Components.SearchViewPager.ChatPreviewDelegate
            public void finish() {
                Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    DialogsActivity.this.finishPreviewFragment();
                }
            }
        }) { // from class: org.telegram.ui.DialogsActivity.17
            @Override // org.telegram.ui.Components.ViewPagerFixed
            protected boolean onBackProgress(float f2) {
                return false;
            }

            @Override // org.telegram.ui.Components.ViewPagerFixed
            protected void onTabPageSelected(int i16) {
                DialogsActivity.this.updateSpeedItem(i16 == 2);
            }

            @Override // org.telegram.ui.Components.SearchViewPager
            protected boolean includeDownloads() {
                RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
                return rightSlidingDialogContainer == null || !rightSlidingDialogContainer.hasFragment();
            }
        };
        this.searchViewPager = searchViewPager;
        contentView3.addView(searchViewPager);
        this.searchViewPager.dialogsSearchAdapter.setDelegate(new 18());
        this.searchViewPager.searchListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda89
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i16) {
                DialogsActivity.this.lambda$createView$8(view, i16);
            }
        });
        this.searchViewPager.searchListView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListenerExtended() { // from class: org.telegram.ui.DialogsActivity.19
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
            public boolean onItemClick(View view, int i16, float f2, float f3) {
                DialogsActivity dialogsActivity = DialogsActivity.this;
                return dialogsActivity.onItemLongClick(dialogsActivity.searchViewPager.searchListView, view, i16, f2, f3, -1, DialogsActivity.this.searchViewPager.dialogsSearchAdapter);
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
            public void onMove(float f2, float f3) {
                Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    DialogsActivity.this.movePreviewFragment(f3);
                }
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended
            public void onLongClickRelease() {
                Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    DialogsActivity.this.finishPreviewFragment();
                }
            }
        });
        this.searchViewPager.setFilteredSearchViewDelegate(new FilteredSearchView.Delegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda91
            @Override // org.telegram.ui.FilteredSearchView.Delegate
            public final void updateFiltersView(boolean z, ArrayList arrayList, ArrayList arrayList2, boolean z2) {
                DialogsActivity.this.lambda$createView$9(z, arrayList, arrayList2, z2);
            }
        });
        this.searchViewPager.setVisibility(8);
        FiltersView filtersView = new FiltersView(getParentActivity(), null);
        this.filtersView = filtersView;
        filtersView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda88
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i16) {
                DialogsActivity.this.lambda$createView$10(view, i16);
            }
        });
        contentView3.addView(this.filtersView, LayoutHelper.createFrame(-1, -2, 48));
        this.filtersView.setVisibility(8);
        FrameLayout frameLayout = new FrameLayout(context);
        this.floatingButtonContainer = frameLayout;
        frameLayout.setVisibility(((!this.onlySelect || this.initialDialogsType == 10) && this.folderId == 0) ? 0 : 8);
        FrameLayout frameLayout2 = this.floatingButtonContainer;
        int i16 = Build.VERSION.SDK_INT;
        int i17 = i16 >= 21 ? 56 : 60;
        float f2 = i16 >= 21 ? 56 : 60;
        boolean z = LocaleController.isRTL;
        contentView3.addView(frameLayout2, LayoutHelper.createFrame(i17, f2, (z ? 3 : 5) | 80, z ? 14.0f : 0.0f, 0.0f, z ? 0.0f : 14.0f, 14.0f));
        this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda29
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DialogsActivity.this.lambda$createView$11(view);
            }
        });
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.floatingButton = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), PorterDuff.Mode.MULTIPLY));
        if (this.initialDialogsType == 10) {
            this.floatingButton.setImageResource(R.drawable.floating_check);
            this.floatingButtonContainer.setContentDescription(LocaleController.getString("Done", R.string.Done));
        } else {
            this.floatingButton.setAnimation(R.raw.write_contacts_fab_icon, 52, 52);
            this.floatingButtonContainer.setContentDescription(LocaleController.getString("NewMessageTitle", R.string.NewMessageTitle));
        }
        if (i16 >= 21) {
            StateListAnimator stateListAnimator = new StateListAnimator();
            FrameLayout frameLayout3 = this.floatingButtonContainer;
            Property property = View.TRANSLATION_Z;
            stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(frameLayout3, property, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButtonContainer, property, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
            this.floatingButtonContainer.setStateListAnimator(stateListAnimator);
            this.floatingButtonContainer.setOutlineProvider(new ViewOutlineProvider(this) { // from class: org.telegram.ui.DialogsActivity.20
                @Override // android.view.ViewOutlineProvider
                @SuppressLint({"NewApi"})
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
        if (i16 < 21) {
            Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable2 = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable2.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            combinedDrawable = combinedDrawable2;
        } else {
            combinedDrawable = createSimpleSelectorCircleDrawable;
        }
        updateFloatingButtonColor();
        this.floatingButtonContainer.addView(this.floatingButton, LayoutHelper.createFrame(-1, -1.0f));
        RadialProgressView radialProgressView = new RadialProgressView(context);
        this.floatingProgressView = radialProgressView;
        radialProgressView.setProgressColor(Theme.getColor("chats_actionIcon"));
        this.floatingProgressView.setScaleX(0.1f);
        this.floatingProgressView.setScaleY(0.1f);
        this.floatingProgressView.setAlpha(0.0f);
        this.floatingProgressView.setVisibility(8);
        this.floatingProgressView.setSize(AndroidUtilities.dp(22.0f));
        this.floatingButtonContainer.addView(this.floatingProgressView, LayoutHelper.createFrame(-1, -1.0f));
        this.searchTabsView = null;
        if (!this.onlySelect && this.initialDialogsType == 0) {
            FragmentContextView fragmentContextView = new FragmentContextView(context, this, true);
            this.fragmentLocationContextView = fragmentContextView;
            fragmentContextView.setLayoutParams(LayoutHelper.createFrame(-1, 38.0f, 51, 0.0f, -36.0f, 0.0f, 0.0f));
            contentView3.addView(this.fragmentLocationContextView);
            FragmentContextView fragmentContextView2 = new FragmentContextView(context, this, false);
            this.fragmentContextView = fragmentContextView2;
            fragmentContextView2.setLayoutParams(LayoutHelper.createFrame(-1, 38.0f, 51, 0.0f, -36.0f, 0.0f, 0.0f));
            contentView3.addView(this.fragmentContextView);
            this.fragmentContextView.setAdditionalContextView(this.fragmentLocationContextView);
            this.fragmentLocationContextView.setAdditionalContextView(this.fragmentContextView);
            this.dialogsHintCell = new DialogsHintCell(context);
            updateDialogsHint();
            CacheControlActivity.calculateTotalSize(new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda76
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.this.lambda$createView$12((Long) obj);
                }
            });
            CacheControlActivity.getDeviceTotalSize(new Utilities.Callback2() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda74
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    DialogsActivity.this.lambda$createView$13((Long) obj, (Long) obj2);
                }
            });
            contentView3.addView(this.dialogsHintCell);
        } else if (this.initialDialogsType == 3) {
            ChatActivityEnterView chatActivityEnterView = this.commentView;
            if (chatActivityEnterView != null) {
                chatActivityEnterView.onDestroy();
            }
            i2 = 21;
            this.commentView = new ChatActivityEnterView(getParentActivity(), contentView3, null, false) { // from class: org.telegram.ui.DialogsActivity.21
                @Override // android.view.ViewGroup, android.view.View
                public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 0) {
                        AndroidUtilities.requestAdjustResize(DialogsActivity.this.getParentActivity(), ((BaseFragment) DialogsActivity.this).classGuid);
                    }
                    return super.dispatchTouchEvent(motionEvent);
                }

                @Override // android.view.View
                public void setTranslationY(float f3) {
                    super.setTranslationY(f3);
                }
            };
            contentView3.setClipChildren(false);
            contentView3.setClipToPadding(false);
            ChatActivityEnterView chatActivityEnterView2 = this.commentView;
            chatActivityEnterView2.allowBlur = false;
            chatActivityEnterView2.forceSmoothKeyboard(true);
            this.commentView.setAllowStickersAndGifs(true, false, false);
            this.commentView.setForceShowSendButton(true, false);
            this.commentView.setPadding(0, 0, AndroidUtilities.dp(20.0f), 0);
            this.commentView.setVisibility(8);
            this.commentView.getSendButton().setAlpha(0.0f);
            View view = new View(getParentActivity());
            this.commentViewBg = view;
            view.setBackgroundColor(getThemedColor("chat_messagePanelBackground"));
            contentView3.addView(this.commentViewBg, LayoutHelper.createFrame(-1, 1600.0f, 87, 0.0f, 0.0f, 0.0f, -1600.0f));
            contentView3.addView(this.commentView, LayoutHelper.createFrame(-1, -2, 83));
            this.commentView.setDelegate(new ChatActivityEnterView.ChatActivityEnterViewDelegate() { // from class: org.telegram.ui.DialogsActivity.22
                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void bottomPanelTranslationYChanged(float f3) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void didPressAttachButton() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ int getContentViewHeight() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$getContentViewHeight(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ TLRPC$TL_channels_sendAsPeers getSendAsPeers() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$getSendAsPeers(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ boolean hasForwardingMessages() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$hasForwardingMessages(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ boolean hasScheduledMessages() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$hasScheduledMessages(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ int measureKeyboardHeight() {
                    return ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$measureKeyboardHeight(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void needChangeVideoPreviewState(int i18, float f3) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void needSendTyping() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void needShowMediaBanHint() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void needStartRecordAudio(int i18) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void needStartRecordVideo(int i18, boolean z2, int i19) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onAttachButtonHidden() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onAttachButtonShow() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onAudioVideoInterfaceUpdated() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void onContextMenuClose() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onContextMenuClose(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void onContextMenuOpen() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onContextMenuOpen(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void onEditTextScroll() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onEditTextScroll(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onMessageEditEnd(boolean z2) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onPreAudioVideoRecord() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onSendLongClick() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onStickersExpandedChange() {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onStickersTab(boolean z2) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onSwitchRecordMode(boolean z2) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onTextChanged(CharSequence charSequence, boolean z2) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onTextSelectionChanged(int i18, int i19) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onTextSpansChanged(CharSequence charSequence) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void onTrendingStickersShowed(boolean z2) {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$onTrendingStickersShowed(this, z2);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onUpdateSlowModeButton(View view2, boolean z2, CharSequence charSequence) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onWindowSizeChanged(int i18) {
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void openScheduledMessages() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$openScheduledMessages(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void prepareMessageSending() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$prepareMessageSending(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public /* synthetic */ void scrollToSendingMessage() {
                    ChatActivityEnterView.ChatActivityEnterViewDelegate.-CC.$default$scrollToSendingMessage(this);
                }

                @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate
                public void onMessageSend(CharSequence charSequence, boolean z2, int i18) {
                    if (DialogsActivity.this.delegate == null || DialogsActivity.this.selectedDialogs.isEmpty()) {
                        return;
                    }
                    ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
                    for (int i19 = 0; i19 < DialogsActivity.this.selectedDialogs.size(); i19++) {
                        arrayList.add(MessagesStorage.TopicKey.of(((Long) DialogsActivity.this.selectedDialogs.get(i19)).longValue(), 0));
                    }
                    DialogsActivity.this.delegate.didSelectDialogs(DialogsActivity.this, arrayList, charSequence, false, null);
                }
            });
            FrameLayout frameLayout4 = new FrameLayout(context) { // from class: org.telegram.ui.DialogsActivity.23
                @Override // android.view.View
                public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                    accessibilityNodeInfo.setText(LocaleController.formatPluralString("AccDescrShareInChats", DialogsActivity.this.selectedDialogs.size(), new Object[0]));
                    accessibilityNodeInfo.setClassName(Button.class.getName());
                    accessibilityNodeInfo.setLongClickable(true);
                    accessibilityNodeInfo.setClickable(true);
                }
            };
            this.writeButtonContainer = frameLayout4;
            frameLayout4.setFocusable(true);
            this.writeButtonContainer.setFocusableInTouchMode(true);
            i3 = 4;
            this.writeButtonContainer.setVisibility(4);
            this.writeButtonContainer.setScaleX(0.2f);
            this.writeButtonContainer.setScaleY(0.2f);
            this.writeButtonContainer.setAlpha(0.0f);
            contentView3.addView(this.writeButtonContainer, LayoutHelper.createFrame(60, 60.0f, 85, 0.0f, 0.0f, 6.0f, 10.0f));
            this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
            this.textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            View view2 = new View(context) { // from class: org.telegram.ui.DialogsActivity.24
                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    String format = String.format("%d", Integer.valueOf(Math.max(1, DialogsActivity.this.selectedDialogs.size())));
                    int ceil = (int) Math.ceil(DialogsActivity.this.textPaint.measureText(format));
                    int max = Math.max(AndroidUtilities.dp(16.0f) + ceil, AndroidUtilities.dp(24.0f));
                    int measuredWidth = getMeasuredWidth() / 2;
                    int measuredHeight = getMeasuredHeight() / 2;
                    DialogsActivity.this.textPaint.setColor(DialogsActivity.this.getThemedColor("dialogRoundCheckBoxCheck"));
                    DialogsActivity.this.paint.setColor(DialogsActivity.this.getThemedColor(Theme.isCurrentThemeDark() ? "voipgroup_inviteMembersBackground" : "dialogBackground"));
                    int i18 = max / 2;
                    int i19 = measuredWidth - i18;
                    int i20 = i18 + measuredWidth;
                    DialogsActivity.this.rect.set(i19, 0.0f, i20, getMeasuredHeight());
                    canvas.drawRoundRect(DialogsActivity.this.rect, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), DialogsActivity.this.paint);
                    DialogsActivity.this.paint.setColor(DialogsActivity.this.getThemedColor("dialogRoundCheckBox"));
                    DialogsActivity.this.rect.set(i19 + AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), i20 - AndroidUtilities.dp(2.0f), getMeasuredHeight() - AndroidUtilities.dp(2.0f));
                    canvas.drawRoundRect(DialogsActivity.this.rect, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), DialogsActivity.this.paint);
                    canvas.drawText(format, measuredWidth - (ceil / 2), AndroidUtilities.dp(16.2f), DialogsActivity.this.textPaint);
                }
            };
            this.selectedCountView = view2;
            view2.setAlpha(0.0f);
            this.selectedCountView.setScaleX(0.2f);
            this.selectedCountView.setScaleY(0.2f);
            contentView3.addView(this.selectedCountView, LayoutHelper.createFrame(42, 24.0f, 85, 0.0f, 0.0f, -8.0f, 9.0f));
            final FrameLayout frameLayout5 = new FrameLayout(context);
            Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), getThemedColor("dialogFloatingButton"), getThemedColor(i16 >= 21 ? "dialogFloatingButtonPressed" : "dialogFloatingButton"));
            if (i16 < 21) {
                Drawable mutate2 = context.getResources().getDrawable(R.drawable.floating_shadow_profile).mutate();
                mutate2.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                CombinedDrawable combinedDrawable3 = new CombinedDrawable(mutate2, combinedDrawable, 0, 0);
                combinedDrawable3.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                createSimpleSelectorCircleDrawable2 = combinedDrawable3;
            }
            frameLayout5.setBackgroundDrawable(createSimpleSelectorCircleDrawable2);
            frameLayout5.setImportantForAccessibility(2);
            if (i16 >= 21) {
                frameLayout5.setOutlineProvider(new ViewOutlineProvider(this) { // from class: org.telegram.ui.DialogsActivity.25
                    @Override // android.view.ViewOutlineProvider
                    @SuppressLint({"NewApi"})
                    public void getOutline(View view3, Outline outline) {
                        outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    }
                });
            }
            frameLayout5.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda24
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    DialogsActivity.this.lambda$createView$14(view3);
                }
            });
            frameLayout5.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda37
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view3) {
                    boolean lambda$createView$15;
                    lambda$createView$15 = DialogsActivity.this.lambda$createView$15(frameLayout5, view3);
                    return lambda$createView$15;
                }
            });
            this.writeButton = new ImageView[2];
            int i18 = 0;
            for (int i19 = 2; i18 < i19; i19 = 2) {
                this.writeButton[i18] = new ImageView(context);
                this.writeButton[i18].setImageResource(i18 == 1 ? R.drawable.msg_arrow_forward : R.drawable.attach_send);
                this.writeButton[i18].setColorFilter(new PorterDuffColorFilter(getThemedColor("dialogFloatingIcon"), PorterDuff.Mode.MULTIPLY));
                this.writeButton[i18].setScaleType(ImageView.ScaleType.CENTER);
                ImageView imageView = this.writeButton[i18];
                int i20 = Build.VERSION.SDK_INT;
                frameLayout5.addView(imageView, LayoutHelper.createFrame(i20 >= 21 ? 56 : 60, i20 >= 21 ? 56 : 60, 17));
                i18++;
            }
            AndroidUtilities.updateViewVisibilityAnimated(this.writeButton[0], true, 0.5f, false);
            AndroidUtilities.updateViewVisibilityAnimated(this.writeButton[1], false, 0.5f, false);
            FrameLayout frameLayout6 = this.writeButtonContainer;
            int i21 = Build.VERSION.SDK_INT;
            frameLayout6.addView(frameLayout5, LayoutHelper.createFrame(i21 >= 21 ? 56 : 60, i21 >= 21 ? 56.0f : 60.0f, 51, i21 >= 21 ? 2.0f : 0.0f, 0.0f, 0.0f, 0.0f));
            filterTabsView = this.filterTabsView;
            if (filterTabsView != null) {
                contentView3.addView(filterTabsView, LayoutHelper.createFrame(-1, 44.0f));
            }
            if (!this.onlySelect) {
                FrameLayout.LayoutParams createFrame = LayoutHelper.createFrame(-1, -2.0f);
                if (this.inPreviewMode && Build.VERSION.SDK_INT >= i2) {
                    createFrame.topMargin = AndroidUtilities.statusBarHeight;
                }
                contentView3.addView(this.actionBar, createFrame);
                DrawerProfileCell.AnimatedStatusView animatedStatusView = new DrawerProfileCell.AnimatedStatusView(context, 20, 60);
                this.animatedStatusView = animatedStatusView;
                contentView3.addView(animatedStatusView, LayoutHelper.createFrame(20, 20, 51));
            }
            if (this.searchString == null && this.initialDialogsType == 0) {
                FrameLayout frameLayout7 = new FrameLayout(context) { // from class: org.telegram.ui.DialogsActivity.26
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
                            DialogsActivity.this.updateLayoutIcon.setBackgroundGradientDrawable(this.updateGradient);
                            DialogsActivity.this.updateLayoutIcon.draw(canvas);
                        }
                        super.draw(canvas);
                    }

                    @Override // android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i22, int i23) {
                        super.onMeasure(i22, i23);
                        int size = View.MeasureSpec.getSize(i22);
                        if (this.lastGradientWidth != size) {
                            this.updateGradient = new LinearGradient(0.0f, 0.0f, size, 0.0f, new int[]{-9846926, -11291731}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                            this.lastGradientWidth = size;
                        }
                        int measuredWidth = (getMeasuredWidth() - DialogsActivity.this.updateTextView.getMeasuredWidth()) / 2;
                        DialogsActivity.this.updateLayoutIcon.setProgressRect(measuredWidth, AndroidUtilities.dp(13.0f), AndroidUtilities.dp(22.0f) + measuredWidth, AndroidUtilities.dp(35.0f));
                    }

                    @Override // android.view.View
                    public void setTranslationY(float f3) {
                        super.setTranslationY(f3);
                        DialogsActivity.this.additionalFloatingTranslation2 = AndroidUtilities.dp(48.0f) - f3;
                        if (DialogsActivity.this.additionalFloatingTranslation2 < 0.0f) {
                            DialogsActivity.this.additionalFloatingTranslation2 = 0.0f;
                        }
                        if (DialogsActivity.this.floatingHidden) {
                            return;
                        }
                        DialogsActivity.this.updateFloatingButtonOffset();
                    }
                };
                this.updateLayout = frameLayout7;
                frameLayout7.setWillNotDraw(false);
                this.updateLayout.setVisibility(i3);
                this.updateLayout.setTranslationY(AndroidUtilities.dp(48.0f));
                if (Build.VERSION.SDK_INT >= i2) {
                    this.updateLayout.setBackground(Theme.getSelectorDrawable(1090519039, false));
                }
                contentView3.addView(this.updateLayout, LayoutHelper.createFrame(-1, 48, 83));
                this.updateLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda22
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        DialogsActivity.this.lambda$createView$16(view3);
                    }
                });
                RadialProgress2 radialProgress2 = new RadialProgress2(this.updateLayout);
                this.updateLayoutIcon = radialProgress2;
                radialProgress2.setColors(-1, -1, -1, -1);
                this.updateLayoutIcon.setCircleRadius(AndroidUtilities.dp(11.0f));
                this.updateLayoutIcon.setAsMini();
                this.updateLayoutIcon.setIcon(15, true, false);
                TextView textView = new TextView(context);
                this.updateTextView = textView;
                textView.setTextSize(1, 15.0f);
                this.updateTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                this.updateTextView.setText(LocaleController.getString("AppUpdateNow", R.string.AppUpdateNow).toUpperCase());
                this.updateTextView.setTextColor(-1);
                this.updateTextView.setPadding(AndroidUtilities.dp(30.0f), 0, 0, 0);
                this.updateLayout.addView(this.updateTextView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 0.0f));
            }
            for (i4 = 0; i4 < 2; i4++) {
                this.undoView[i4] = new 27(context);
                contentView3.addView(this.undoView[i4], LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
            }
            if (this.folderId != 0) {
                this.viewPages[0].listView.setGlowColor(Theme.getColor("actionBarDefaultArchived"));
                this.actionBar.setTitleColor(Theme.getColor("actionBarDefaultArchivedTitle"));
                this.actionBar.setItemsColor(Theme.getColor("actionBarDefaultArchivedIcon"), false);
                this.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarDefaultArchivedSelector"), false);
                this.actionBar.setSearchTextColor(Theme.getColor("actionBarDefaultArchivedSearch"), false);
                this.actionBar.setSearchTextColor(Theme.getColor("actionBarDefaultSearchArchivedPlaceholder"), true);
            }
            if (!this.onlySelect && this.initialDialogsType == 0) {
                View view3 = new View(context) { // from class: org.telegram.ui.DialogsActivity.28
                    @Override // android.view.View
                    public void setAlpha(float f3) {
                        super.setAlpha(f3);
                        if (((BaseFragment) DialogsActivity.this).fragmentView != null) {
                            ((BaseFragment) DialogsActivity.this).fragmentView.invalidate();
                        }
                    }
                };
                this.blurredView = view3;
                if (Build.VERSION.SDK_INT >= 23) {
                    view3.setForeground(new ColorDrawable(ColorUtils.setAlphaComponent(getThemedColor("windowBackgroundWhite"), 100)));
                }
                this.blurredView.setFocusable(false);
                this.blurredView.setImportantForAccessibility(2);
                this.blurredView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda26
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view4) {
                        DialogsActivity.this.lambda$createView$17(view4);
                    }
                });
                this.blurredView.setVisibility(8);
                this.blurredView.setFitsSystemWindows(true);
                contentView3.addView(this.blurredView, LayoutHelper.createFrame(-1, -1.0f));
            }
            this.actionBarDefaultPaint.setColor(Theme.getColor(this.folderId != 0 ? str3 : "actionBarDefaultArchived"));
            if (this.inPreviewMode) {
                TLRPC$User currentUser2 = getUserConfig().getCurrentUser();
                ChatAvatarContainer chatAvatarContainer = new ChatAvatarContainer(this.actionBar.getContext(), null, false);
                this.avatarContainer = chatAvatarContainer;
                chatAvatarContainer.setTitle(UserObject.getUserName(currentUser2));
                this.avatarContainer.setSubtitle(LocaleController.formatUserStatus(this.currentAccount, currentUser2));
                this.avatarContainer.setUserAvatar(currentUser2, true);
                this.avatarContainer.setOccupyStatusBar(false);
                this.avatarContainer.setLeftPadding(AndroidUtilities.dp(10.0f));
                this.actionBar.addView(this.avatarContainer, 0, LayoutHelper.createFrame(-2, -1.0f, 51, 0.0f, 0.0f, 40.0f, 0.0f));
                this.floatingButton.setVisibility(i3);
                this.actionBar.setOccupyStatusBar(false);
                this.actionBar.setBackgroundColor(Theme.getColor(str3));
                FragmentContextView fragmentContextView3 = this.fragmentContextView;
                if (fragmentContextView3 != null) {
                    contentView3.removeView(fragmentContextView3);
                }
                FragmentContextView fragmentContextView4 = this.fragmentLocationContextView;
                if (fragmentContextView4 != null) {
                    contentView3.removeView(fragmentContextView4);
                }
            }
            this.searchIsShowed = false;
            updateFilterTabs(true, false);
            if (this.searchString == null) {
                showSearch(true, false, false);
                this.actionBar.openSearchField(this.searchString, false);
            } else if (this.initialSearchString != null) {
                showSearch(true, false, false);
                this.actionBar.openSearchField(this.initialSearchString, false);
                this.initialSearchString = null;
                FilterTabsView filterTabsView2 = this.filterTabsView;
                if (filterTabsView2 != null) {
                    filterTabsView2.setTranslationY(-AndroidUtilities.dp(44.0f));
                }
            } else {
                showSearch(false, false, false);
            }
            if (Build.VERSION.SDK_INT >= 30) {
                FilesMigrationService.checkBottomSheet(this);
            }
            updateMenuButton(false);
            this.actionBar.setDrawBlurBackground(contentView3);
            RightSlidingDialogContainer rightSlidingDialogContainer = new RightSlidingDialogContainer(context) { // from class: org.telegram.ui.DialogsActivity.29
                boolean anotherFragmentOpened;
                float fromScrollYProperty;
                ViewPage transitionPage;

                @Override // org.telegram.ui.RightSlidingDialogContainer
                boolean getOccupyStatusbar() {
                    return ((BaseFragment) DialogsActivity.this).actionBar != null && ((BaseFragment) DialogsActivity.this).actionBar.getOccupyStatusBar();
                }

                @Override // org.telegram.ui.RightSlidingDialogContainer
                public void openAnimationStarted(boolean z2) {
                    if (!DialogsActivity.this.isArchive()) {
                        ((BaseFragment) DialogsActivity.this).actionBar.setBackButtonDrawable(DialogsActivity.this.menuDrawable = new MenuDrawable());
                        DialogsActivity.this.menuDrawable.setRoundCap();
                    }
                    DialogsActivity.this.rightFragmentTransitionInProgress = true;
                    contentView3.requestLayout();
                    this.fromScrollYProperty = ((BaseFragment) DialogsActivity.this).actionBar.getTranslationY();
                    if (DialogsActivity.this.canShowFilterTabsView && DialogsActivity.this.filterTabsView != null) {
                        DialogsActivity.this.filterTabsView.setVisibility(0);
                    }
                    ViewPage viewPage2 = DialogsActivity.this.viewPages[0];
                    this.transitionPage = viewPage2;
                    if (viewPage2.animationSupportListView == null) {
                        this.transitionPage.animationSupportListView = new BlurredRecyclerView(this, context) { // from class: org.telegram.ui.DialogsActivity.29.1
                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                            public void dispatchDraw(Canvas canvas) {
                            }

                            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                                return false;
                            }

                            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                                return false;
                            }

                            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
                            public boolean onTouchEvent(MotionEvent motionEvent) {
                                return false;
                            }
                        };
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setNeedFixEndGap(false);
                        this.transitionPage.animationSupportListView.setLayoutManager(linearLayoutManager);
                        ViewPage viewPage3 = this.transitionPage;
                        viewPage3.animationSupportDialogsAdapter = new DialogsAdapter(DialogsActivity.this, context, viewPage3.dialogsType, DialogsActivity.this.folderId, DialogsActivity.this.onlySelect, DialogsActivity.this.selectedDialogs, ((BaseFragment) DialogsActivity.this).currentAccount, DialogsActivity.this.requestPeerType);
                        this.transitionPage.animationSupportDialogsAdapter.setIsTransitionSupport();
                        this.transitionPage.animationSupportListView.setAdapter(this.transitionPage.animationSupportDialogsAdapter);
                        ViewPage viewPage4 = this.transitionPage;
                        viewPage4.addView(viewPage4.animationSupportListView);
                    }
                    this.transitionPage.animationSupportDialogsAdapter.setDialogsType(this.transitionPage.dialogsType);
                    this.transitionPage.dialogsAdapter.setCollapsedView(false, this.transitionPage.listView);
                    this.transitionPage.dialogsAdapter.setDialogsListFrozen(true);
                    this.transitionPage.animationSupportDialogsAdapter.setDialogsListFrozen(true);
                    this.transitionPage.layoutManager.setNeedFixEndGap(false);
                    DialogsActivity.this.setDialogsListFrozen(true);
                    DialogsActivity.this.hideFloatingButton(this.anotherFragmentOpened);
                    this.transitionPage.dialogsAdapter.notifyDataSetChanged();
                    this.transitionPage.animationSupportDialogsAdapter.notifyDataSetChanged();
                    this.transitionPage.listView.setAnimationSupportView(this.transitionPage.animationSupportListView, -((BaseFragment) DialogsActivity.this).actionBar.getTranslationY(), z2);
                    this.transitionPage.listView.setClipChildren(false);
                    ((BaseFragment) DialogsActivity.this).actionBar.setAllowOverlayTitle(false);
                    this.transitionPage.listView.stopScroll();
                    DialogsActivity.this.updateDrawerSwipeEnabled();
                }

                @Override // org.telegram.ui.RightSlidingDialogContainer
                public void openAnimationFinished() {
                    if (!DialogsActivity.this.canShowFilterTabsView && DialogsActivity.this.filterTabsView != null) {
                        DialogsActivity.this.filterTabsView.setVisibility(8);
                    }
                    this.transitionPage.layoutManager.setNeedFixGap(true);
                    this.transitionPage.dialogsAdapter.setCollapsedView(hasFragment(), this.transitionPage.listView);
                    this.transitionPage.dialogsAdapter.setDialogsListFrozen(false);
                    this.transitionPage.animationSupportDialogsAdapter.setDialogsListFrozen(false);
                    DialogsActivity.this.setDialogsListFrozen(false);
                    this.transitionPage.listView.setClipChildren(true);
                    this.transitionPage.listView.invalidate();
                    this.transitionPage.dialogsAdapter.notifyDataSetChanged();
                    this.transitionPage.animationSupportDialogsAdapter.notifyDataSetChanged();
                    this.transitionPage.listView.setAnimationSupportView(null, 0.0f, hasFragment());
                    DialogsActivity.this.rightFragmentTransitionInProgress = false;
                    ((BaseFragment) DialogsActivity.this).actionBar.setAllowOverlayTitle(true ^ hasFragment());
                    contentView3.requestLayout();
                    DialogsActivity.this.setScrollY(0.0f);
                    DialogsActivity.this.searchViewPager.updateTabs();
                    DialogsActivity.this.updateDrawerSwipeEnabled();
                }

                @Override // org.telegram.ui.RightSlidingDialogContainer
                void setOpenProgress(float f3) {
                    boolean z2 = f3 > 0.0f;
                    if (this.anotherFragmentOpened != z2) {
                        this.anotherFragmentOpened = z2;
                    }
                    DialogsActivity.this.filterTabsMoveFrom = AndroidUtilities.dp(44.0f);
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    dialogsActivity.filterTabsProgress = dialogsActivity.canShowFilterTabsView ? 1.0f - f3 : 0.0f;
                    if (((BaseFragment) DialogsActivity.this).fragmentView != null) {
                        ((BaseFragment) DialogsActivity.this).fragmentView.invalidate();
                    }
                    DialogsActivity.this.setScrollY(AndroidUtilities.lerp(this.fromScrollYProperty, 0.0f, f3));
                    DialogsActivity.this.updateDrawerSwipeEnabled();
                    if (DialogsActivity.this.menuDrawable != null && hasFragment()) {
                        DialogsActivity.this.menuDrawable.setRotation(f3, false);
                    }
                    if (((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView() != null) {
                        ((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView().setAlpha(1.0f - f3);
                        if (((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView().getAlpha() > 0.0f) {
                            ((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView().setVisibility(0);
                        }
                    }
                    if (DialogsActivity.this.proxyItem != null) {
                        DialogsActivity.this.proxyItem.setAlpha(1.0f - f3);
                    }
                    if (DialogsActivity.this.downloadsItem != null) {
                        DialogsActivity.this.downloadsItem.setAlpha(1.0f - f3);
                    }
                    if (DialogsActivity.this.passcodeItem != null) {
                        DialogsActivity.this.passcodeItem.setAlpha(1.0f - f3);
                    }
                    if (DialogsActivity.this.searchItem != null) {
                        DialogsActivity.this.searchItem.setAlpha(this.anotherFragmentOpened ? 0.0f : 1.0f);
                    }
                    if (((BaseFragment) DialogsActivity.this).actionBar.getBackButton() != null) {
                        ((BaseFragment) DialogsActivity.this).actionBar.getBackButton().setAlpha(f3 != 1.0f ? 1.0f : 0.0f);
                    }
                    if (DialogsActivity.this.folderId != 0) {
                        DialogsActivity.this.actionBarDefaultPaint.setColor(ColorUtils.blendARGB(Theme.getColor("actionBarDefaultArchived"), Theme.getColor("actionBarDefault"), f3));
                    }
                    ViewPage viewPage2 = this.transitionPage;
                    if (viewPage2 != null) {
                        viewPage2.listView.setOpenRightFragmentProgress(f3);
                    }
                }
            };
            this.rightSlidingDialogContainer = rightSlidingDialogContainer;
            rightSlidingDialogContainer.setOpenProgress(0.0f);
            contentView3.addView(this.rightSlidingDialogContainer, LayoutHelper.createFrame(-1, -1.0f));
            return this.fragmentView;
        }
        i3 = 4;
        i2 = 21;
        filterTabsView = this.filterTabsView;
        if (filterTabsView != null) {
        }
        if (!this.onlySelect) {
        }
        if (this.searchString == null) {
            FrameLayout frameLayout72 = new FrameLayout(context) { // from class: org.telegram.ui.DialogsActivity.26
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
                        DialogsActivity.this.updateLayoutIcon.setBackgroundGradientDrawable(this.updateGradient);
                        DialogsActivity.this.updateLayoutIcon.draw(canvas);
                    }
                    super.draw(canvas);
                }

                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i22, int i23) {
                    super.onMeasure(i22, i23);
                    int size = View.MeasureSpec.getSize(i22);
                    if (this.lastGradientWidth != size) {
                        this.updateGradient = new LinearGradient(0.0f, 0.0f, size, 0.0f, new int[]{-9846926, -11291731}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                        this.lastGradientWidth = size;
                    }
                    int measuredWidth = (getMeasuredWidth() - DialogsActivity.this.updateTextView.getMeasuredWidth()) / 2;
                    DialogsActivity.this.updateLayoutIcon.setProgressRect(measuredWidth, AndroidUtilities.dp(13.0f), AndroidUtilities.dp(22.0f) + measuredWidth, AndroidUtilities.dp(35.0f));
                }

                @Override // android.view.View
                public void setTranslationY(float f3) {
                    super.setTranslationY(f3);
                    DialogsActivity.this.additionalFloatingTranslation2 = AndroidUtilities.dp(48.0f) - f3;
                    if (DialogsActivity.this.additionalFloatingTranslation2 < 0.0f) {
                        DialogsActivity.this.additionalFloatingTranslation2 = 0.0f;
                    }
                    if (DialogsActivity.this.floatingHidden) {
                        return;
                    }
                    DialogsActivity.this.updateFloatingButtonOffset();
                }
            };
            this.updateLayout = frameLayout72;
            frameLayout72.setWillNotDraw(false);
            this.updateLayout.setVisibility(i3);
            this.updateLayout.setTranslationY(AndroidUtilities.dp(48.0f));
            if (Build.VERSION.SDK_INT >= i2) {
            }
            contentView3.addView(this.updateLayout, LayoutHelper.createFrame(-1, 48, 83));
            this.updateLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda22
                @Override // android.view.View.OnClickListener
                public final void onClick(View view32) {
                    DialogsActivity.this.lambda$createView$16(view32);
                }
            });
            RadialProgress2 radialProgress22 = new RadialProgress2(this.updateLayout);
            this.updateLayoutIcon = radialProgress22;
            radialProgress22.setColors(-1, -1, -1, -1);
            this.updateLayoutIcon.setCircleRadius(AndroidUtilities.dp(11.0f));
            this.updateLayoutIcon.setAsMini();
            this.updateLayoutIcon.setIcon(15, true, false);
            TextView textView2 = new TextView(context);
            this.updateTextView = textView2;
            textView2.setTextSize(1, 15.0f);
            this.updateTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.updateTextView.setText(LocaleController.getString("AppUpdateNow", R.string.AppUpdateNow).toUpperCase());
            this.updateTextView.setTextColor(-1);
            this.updateTextView.setPadding(AndroidUtilities.dp(30.0f), 0, 0, 0);
            this.updateLayout.addView(this.updateTextView, LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, 0.0f));
        }
        while (i4 < 2) {
        }
        if (this.folderId != 0) {
        }
        if (!this.onlySelect) {
            View view32 = new View(context) { // from class: org.telegram.ui.DialogsActivity.28
                @Override // android.view.View
                public void setAlpha(float f3) {
                    super.setAlpha(f3);
                    if (((BaseFragment) DialogsActivity.this).fragmentView != null) {
                        ((BaseFragment) DialogsActivity.this).fragmentView.invalidate();
                    }
                }
            };
            this.blurredView = view32;
            if (Build.VERSION.SDK_INT >= 23) {
            }
            this.blurredView.setFocusable(false);
            this.blurredView.setImportantForAccessibility(2);
            this.blurredView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda26
                @Override // android.view.View.OnClickListener
                public final void onClick(View view4) {
                    DialogsActivity.this.lambda$createView$17(view4);
                }
            });
            this.blurredView.setVisibility(8);
            this.blurredView.setFitsSystemWindows(true);
            contentView3.addView(this.blurredView, LayoutHelper.createFrame(-1, -1.0f));
        }
        this.actionBarDefaultPaint.setColor(Theme.getColor(this.folderId != 0 ? str3 : "actionBarDefaultArchived"));
        if (this.inPreviewMode) {
        }
        this.searchIsShowed = false;
        updateFilterTabs(true, false);
        if (this.searchString == null) {
        }
        if (Build.VERSION.SDK_INT >= 30) {
        }
        updateMenuButton(false);
        this.actionBar.setDrawBlurBackground(contentView3);
        RightSlidingDialogContainer rightSlidingDialogContainer2 = new RightSlidingDialogContainer(context) { // from class: org.telegram.ui.DialogsActivity.29
            boolean anotherFragmentOpened;
            float fromScrollYProperty;
            ViewPage transitionPage;

            @Override // org.telegram.ui.RightSlidingDialogContainer
            boolean getOccupyStatusbar() {
                return ((BaseFragment) DialogsActivity.this).actionBar != null && ((BaseFragment) DialogsActivity.this).actionBar.getOccupyStatusBar();
            }

            @Override // org.telegram.ui.RightSlidingDialogContainer
            public void openAnimationStarted(boolean z2) {
                if (!DialogsActivity.this.isArchive()) {
                    ((BaseFragment) DialogsActivity.this).actionBar.setBackButtonDrawable(DialogsActivity.this.menuDrawable = new MenuDrawable());
                    DialogsActivity.this.menuDrawable.setRoundCap();
                }
                DialogsActivity.this.rightFragmentTransitionInProgress = true;
                contentView3.requestLayout();
                this.fromScrollYProperty = ((BaseFragment) DialogsActivity.this).actionBar.getTranslationY();
                if (DialogsActivity.this.canShowFilterTabsView && DialogsActivity.this.filterTabsView != null) {
                    DialogsActivity.this.filterTabsView.setVisibility(0);
                }
                ViewPage viewPage2 = DialogsActivity.this.viewPages[0];
                this.transitionPage = viewPage2;
                if (viewPage2.animationSupportListView == null) {
                    this.transitionPage.animationSupportListView = new BlurredRecyclerView(this, context) { // from class: org.telegram.ui.DialogsActivity.29.1
                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // org.telegram.ui.Components.BlurredRecyclerView, org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                        public void dispatchDraw(Canvas canvas) {
                        }

                        @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
                        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                            return false;
                        }

                        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                            return false;
                        }

                        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
                        public boolean onTouchEvent(MotionEvent motionEvent) {
                            return false;
                        }
                    };
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setNeedFixEndGap(false);
                    this.transitionPage.animationSupportListView.setLayoutManager(linearLayoutManager);
                    ViewPage viewPage3 = this.transitionPage;
                    viewPage3.animationSupportDialogsAdapter = new DialogsAdapter(DialogsActivity.this, context, viewPage3.dialogsType, DialogsActivity.this.folderId, DialogsActivity.this.onlySelect, DialogsActivity.this.selectedDialogs, ((BaseFragment) DialogsActivity.this).currentAccount, DialogsActivity.this.requestPeerType);
                    this.transitionPage.animationSupportDialogsAdapter.setIsTransitionSupport();
                    this.transitionPage.animationSupportListView.setAdapter(this.transitionPage.animationSupportDialogsAdapter);
                    ViewPage viewPage4 = this.transitionPage;
                    viewPage4.addView(viewPage4.animationSupportListView);
                }
                this.transitionPage.animationSupportDialogsAdapter.setDialogsType(this.transitionPage.dialogsType);
                this.transitionPage.dialogsAdapter.setCollapsedView(false, this.transitionPage.listView);
                this.transitionPage.dialogsAdapter.setDialogsListFrozen(true);
                this.transitionPage.animationSupportDialogsAdapter.setDialogsListFrozen(true);
                this.transitionPage.layoutManager.setNeedFixEndGap(false);
                DialogsActivity.this.setDialogsListFrozen(true);
                DialogsActivity.this.hideFloatingButton(this.anotherFragmentOpened);
                this.transitionPage.dialogsAdapter.notifyDataSetChanged();
                this.transitionPage.animationSupportDialogsAdapter.notifyDataSetChanged();
                this.transitionPage.listView.setAnimationSupportView(this.transitionPage.animationSupportListView, -((BaseFragment) DialogsActivity.this).actionBar.getTranslationY(), z2);
                this.transitionPage.listView.setClipChildren(false);
                ((BaseFragment) DialogsActivity.this).actionBar.setAllowOverlayTitle(false);
                this.transitionPage.listView.stopScroll();
                DialogsActivity.this.updateDrawerSwipeEnabled();
            }

            @Override // org.telegram.ui.RightSlidingDialogContainer
            public void openAnimationFinished() {
                if (!DialogsActivity.this.canShowFilterTabsView && DialogsActivity.this.filterTabsView != null) {
                    DialogsActivity.this.filterTabsView.setVisibility(8);
                }
                this.transitionPage.layoutManager.setNeedFixGap(true);
                this.transitionPage.dialogsAdapter.setCollapsedView(hasFragment(), this.transitionPage.listView);
                this.transitionPage.dialogsAdapter.setDialogsListFrozen(false);
                this.transitionPage.animationSupportDialogsAdapter.setDialogsListFrozen(false);
                DialogsActivity.this.setDialogsListFrozen(false);
                this.transitionPage.listView.setClipChildren(true);
                this.transitionPage.listView.invalidate();
                this.transitionPage.dialogsAdapter.notifyDataSetChanged();
                this.transitionPage.animationSupportDialogsAdapter.notifyDataSetChanged();
                this.transitionPage.listView.setAnimationSupportView(null, 0.0f, hasFragment());
                DialogsActivity.this.rightFragmentTransitionInProgress = false;
                ((BaseFragment) DialogsActivity.this).actionBar.setAllowOverlayTitle(true ^ hasFragment());
                contentView3.requestLayout();
                DialogsActivity.this.setScrollY(0.0f);
                DialogsActivity.this.searchViewPager.updateTabs();
                DialogsActivity.this.updateDrawerSwipeEnabled();
            }

            @Override // org.telegram.ui.RightSlidingDialogContainer
            void setOpenProgress(float f3) {
                boolean z2 = f3 > 0.0f;
                if (this.anotherFragmentOpened != z2) {
                    this.anotherFragmentOpened = z2;
                }
                DialogsActivity.this.filterTabsMoveFrom = AndroidUtilities.dp(44.0f);
                DialogsActivity dialogsActivity = DialogsActivity.this;
                dialogsActivity.filterTabsProgress = dialogsActivity.canShowFilterTabsView ? 1.0f - f3 : 0.0f;
                if (((BaseFragment) DialogsActivity.this).fragmentView != null) {
                    ((BaseFragment) DialogsActivity.this).fragmentView.invalidate();
                }
                DialogsActivity.this.setScrollY(AndroidUtilities.lerp(this.fromScrollYProperty, 0.0f, f3));
                DialogsActivity.this.updateDrawerSwipeEnabled();
                if (DialogsActivity.this.menuDrawable != null && hasFragment()) {
                    DialogsActivity.this.menuDrawable.setRotation(f3, false);
                }
                if (((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView() != null) {
                    ((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView().setAlpha(1.0f - f3);
                    if (((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView().getAlpha() > 0.0f) {
                        ((BaseFragment) DialogsActivity.this).actionBar.getTitleTextView().setVisibility(0);
                    }
                }
                if (DialogsActivity.this.proxyItem != null) {
                    DialogsActivity.this.proxyItem.setAlpha(1.0f - f3);
                }
                if (DialogsActivity.this.downloadsItem != null) {
                    DialogsActivity.this.downloadsItem.setAlpha(1.0f - f3);
                }
                if (DialogsActivity.this.passcodeItem != null) {
                    DialogsActivity.this.passcodeItem.setAlpha(1.0f - f3);
                }
                if (DialogsActivity.this.searchItem != null) {
                    DialogsActivity.this.searchItem.setAlpha(this.anotherFragmentOpened ? 0.0f : 1.0f);
                }
                if (((BaseFragment) DialogsActivity.this).actionBar.getBackButton() != null) {
                    ((BaseFragment) DialogsActivity.this).actionBar.getBackButton().setAlpha(f3 != 1.0f ? 1.0f : 0.0f);
                }
                if (DialogsActivity.this.folderId != 0) {
                    DialogsActivity.this.actionBarDefaultPaint.setColor(ColorUtils.blendARGB(Theme.getColor("actionBarDefaultArchived"), Theme.getColor("actionBarDefault"), f3));
                }
                ViewPage viewPage2 = this.transitionPage;
                if (viewPage2 != null) {
                    viewPage2.listView.setOpenRightFragmentProgress(f3);
                }
            }
        };
        this.rightSlidingDialogContainer = rightSlidingDialogContainer2;
        rightSlidingDialogContainer2.setOpenProgress(0.0f);
        contentView3.addView(this.rightSlidingDialogContainer, LayoutHelper.createFrame(-1, -1.0f));
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(View view) {
        this.filterTabsView.setIsEditing(false);
        showDoneItem(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 4 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        boolean isSpeedItemCreated = false;
        final /* synthetic */ Context val$context;
        final /* synthetic */ ActionBarMenu val$menu;

        4(Context context, ActionBarMenu actionBarMenu) {
            this.val$context = context;
            this.val$menu = actionBarMenu;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onPreToggleSearch() {
            if (this.isSpeedItemCreated) {
                return;
            }
            this.isSpeedItemCreated = true;
            DialogsActivity.this.speedItem = new ActionBarMenuItem(this.val$context, this.val$menu, Theme.getColor("actionBarActionModeDefaultSelector"), Theme.getColor("actionBarActionModeDefaultIcon"));
            DialogsActivity.this.speedItem.setIcon(R.drawable.avd_speed);
            DialogsActivity.this.speedItem.getIconView().setColorFilter(new PorterDuffColorFilter(Theme.getColor("actionBarActionModeDefaultIcon"), PorterDuff.Mode.SRC_IN));
            DialogsActivity.this.speedItem.setTranslationX(AndroidUtilities.dp(32.0f));
            DialogsActivity.this.speedItem.setAlpha(0.0f);
            DialogsActivity.this.speedItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$4$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.4.this.lambda$onPreToggleSearch$0(view);
                }
            });
            DialogsActivity.this.speedItem.setClickable(false);
            DialogsActivity.this.speedItem.setFixBackground(true);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(AndroidUtilities.dp(42.0f), -1);
            int dp = AndroidUtilities.dp(38.0f);
            layoutParams.rightMargin = dp;
            layoutParams.leftMargin = dp;
            layoutParams.gravity = 5;
            ((FrameLayout) DialogsActivity.this.searchItem.getSearchClearButton().getParent()).addView(DialogsActivity.this.speedItem, layoutParams);
            DialogsActivity.this.searchItem.setSearchAdditionalButton(DialogsActivity.this.speedItem);
            DialogsActivity dialogsActivity = DialogsActivity.this;
            dialogsActivity.updateSpeedItem(dialogsActivity.searchViewPager.getCurrentPosition() == 2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPreToggleSearch$0(View view) {
            DialogsActivity.this.showDialog(new PremiumFeatureBottomSheet(DialogsActivity.this, 2, true));
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchExpand() {
            DialogsActivity.this.searching = true;
            if (DialogsActivity.this.switchItem != null) {
                DialogsActivity.this.switchItem.setVisibility(8);
            }
            if (DialogsActivity.this.proxyItem != null && DialogsActivity.this.proxyItemVisible) {
                DialogsActivity.this.proxyItem.setVisibility(8);
            }
            if (DialogsActivity.this.downloadsItem != null && DialogsActivity.this.downloadsItemVisible) {
                DialogsActivity.this.downloadsItem.setVisibility(8);
            }
            if (DialogsActivity.this.viewPages[0] != null) {
                if (DialogsActivity.this.searchString != null) {
                    DialogsActivity.this.viewPages[0].listView.hide();
                    if (DialogsActivity.this.searchViewPager != null) {
                        DialogsActivity.this.searchViewPager.searchListView.show();
                    }
                }
                if (!DialogsActivity.this.onlySelect) {
                    DialogsActivity.this.floatingButtonContainer.setVisibility(8);
                }
            }
            DialogsActivity.this.setScrollY(0.0f);
            DialogsActivity.this.updatePasscodeButton();
            DialogsActivity.this.updateProxyButton(false, false);
            ((BaseFragment) DialogsActivity.this).actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrGoBack", R.string.AccDescrGoBack));
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needCheckSystemBarColors, new Object[0]);
            ((SizeNotifierFrameLayout) ((BaseFragment) DialogsActivity.this).fragmentView).invalidateBlur();
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public boolean canCollapseSearch() {
            if (DialogsActivity.this.switchItem != null) {
                DialogsActivity.this.switchItem.setVisibility(0);
            }
            if (DialogsActivity.this.proxyItem != null && DialogsActivity.this.proxyItemVisible) {
                DialogsActivity.this.proxyItem.setVisibility(0);
            }
            if (DialogsActivity.this.downloadsItem != null && DialogsActivity.this.downloadsItemVisible) {
                DialogsActivity.this.downloadsItem.setVisibility(0);
            }
            if (DialogsActivity.this.searchString != null) {
                DialogsActivity.this.finishFragment();
                return false;
            }
            return true;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchCollapse() {
            DialogsActivity.this.searching = false;
            DialogsActivity.this.searchWas = false;
            if (DialogsActivity.this.viewPages[0] != null) {
                DialogsActivity.this.viewPages[0].listView.setEmptyView(DialogsActivity.this.folderId == 0 ? DialogsActivity.this.viewPages[0].progressView : null);
                if (!DialogsActivity.this.onlySelect) {
                    DialogsActivity.this.floatingButtonContainer.setVisibility(0);
                    DialogsActivity.this.floatingHidden = true;
                    DialogsActivity.this.floatingButtonTranslation = AndroidUtilities.dp(100.0f);
                    DialogsActivity.this.floatingButtonHideProgress = 1.0f;
                    DialogsActivity.this.updateFloatingButtonOffset();
                }
                DialogsActivity.this.showSearch(false, false, true);
            }
            DialogsActivity.this.updateProxyButton(false, false);
            DialogsActivity.this.updatePasscodeButton();
            if (DialogsActivity.this.menuDrawable != null) {
                if (((BaseFragment) DialogsActivity.this).actionBar.getBackButton().getDrawable() != DialogsActivity.this.menuDrawable) {
                    ((BaseFragment) DialogsActivity.this).actionBar.setBackButtonDrawable(DialogsActivity.this.menuDrawable);
                    DialogsActivity.this.menuDrawable.setRotation(0.0f, true);
                }
                ((BaseFragment) DialogsActivity.this).actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrOpenMenu", R.string.AccDescrOpenMenu));
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needCheckSystemBarColors, Boolean.TRUE);
            ((SizeNotifierFrameLayout) ((BaseFragment) DialogsActivity.this).fragmentView).invalidateBlur();
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onTextChanged(EditText editText) {
            String obj = editText.getText().toString();
            if (obj.length() != 0 || ((DialogsActivity.this.searchViewPager.dialogsSearchAdapter != null && DialogsActivity.this.searchViewPager.dialogsSearchAdapter.hasRecentSearch()) || DialogsActivity.this.searchFiltersWasShowed)) {
                DialogsActivity.this.searchWas = true;
                if (!DialogsActivity.this.searchIsShowed) {
                    DialogsActivity.this.showSearch(true, false, true);
                }
            }
            DialogsActivity.this.searchViewPager.onTextChanged(obj);
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchFilterCleared(FiltersView.MediaFilterData mediaFilterData) {
            if (DialogsActivity.this.searchIsShowed) {
                DialogsActivity.this.searchViewPager.removeSearchFilter(mediaFilterData);
                DialogsActivity.this.searchViewPager.onTextChanged(DialogsActivity.this.searchItem.getSearchField().getText().toString());
                DialogsActivity.this.updateFiltersView(true, null, null, false, true);
            }
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public boolean canToggleSearch() {
            return !((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed() && DialogsActivity.this.databaseMigrationHint == null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6() {
        if (this.initialDialogsType != 10) {
            hideFloatingButton(false);
        }
        scrollToTop();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 6 extends FilterTabsView {
        6(Context context) {
            super(context);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            getParent().requestDisallowInterceptTouchEvent(true);
            DialogsActivity.this.maybeStartTracking = false;
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public void setTranslationY(float f) {
            if (getTranslationY() != f) {
                super.setTranslationY(f);
                DialogsActivity.this.updateContextViewPosition();
                if (((BaseFragment) DialogsActivity.this).fragmentView != null) {
                    ((BaseFragment) DialogsActivity.this).fragmentView.invalidate();
                }
            }
        }

        @Override // org.telegram.ui.Components.FilterTabsView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (DialogsActivity.this.scrimView != null) {
                DialogsActivity.this.scrimView.getLocationInWindow(DialogsActivity.this.scrimViewLocation);
                ((BaseFragment) DialogsActivity.this).fragmentView.invalidate();
            }
        }

        @Override // org.telegram.ui.Components.FilterTabsView
        protected void onDefaultTabMoved() {
            if (DialogsActivity.this.getMessagesController().premiumLocked) {
                return;
            }
            try {
                performHapticFeedback(3, 1);
            } catch (Exception unused) {
            }
            DialogsActivity dialogsActivity = DialogsActivity.this;
            dialogsActivity.topBulletin = BulletinFactory.of(dialogsActivity).createSimpleBulletin(R.raw.filter_reorder, AndroidUtilities.replaceTags(LocaleController.formatString("LimitReachedReorderFolder", R.string.LimitReachedReorderFolder, LocaleController.getString(R.string.FilterAllChats))), LocaleController.getString("PremiumMore", R.string.PremiumMore), 5000, new Runnable() { // from class: org.telegram.ui.DialogsActivity$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.6.this.lambda$onDefaultTabMoved$0();
                }
            }).show(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDefaultTabMoved$0() {
            DialogsActivity.this.showDialog(new PremiumFeatureBottomSheet(DialogsActivity.this, 9, true));
            DialogsActivity.this.filterTabsView.setIsEditing(false);
            DialogsActivity.this.showDoneItem(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 7 implements FilterTabsView.FilterTabsViewDelegate {
        final /* synthetic */ Context val$context;

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$showDeleteAlert$0() {
        }

        7(Context context) {
            this.val$context = context;
        }

        private void showDeleteAlert(final MessagesController.DialogFilter dialogFilter) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DialogsActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString("FilterDelete", R.string.FilterDelete));
            builder.setMessage(LocaleController.getString("FilterDeleteAlert", R.string.FilterDeleteAlert));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            builder.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DialogsActivity.7.this.lambda$showDeleteAlert$2(dialogFilter, dialogInterface, i);
                }
            });
            AlertDialog create = builder.create();
            DialogsActivity.this.showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showDeleteAlert$2(MessagesController.DialogFilter dialogFilter, DialogInterface dialogInterface, int i) {
            TLRPC$TL_messages_updateDialogFilter tLRPC$TL_messages_updateDialogFilter = new TLRPC$TL_messages_updateDialogFilter();
            tLRPC$TL_messages_updateDialogFilter.id = dialogFilter.id;
            DialogsActivity.this.getConnectionsManager().sendRequest(tLRPC$TL_messages_updateDialogFilter, DialogsActivity$7$$ExternalSyntheticLambda3.INSTANCE);
            DialogsActivity.this.getMessagesController().removeFilter(dialogFilter);
            DialogsActivity.this.getMessagesStorage().deleteDialogFilter(dialogFilter);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$showDeleteAlert$1(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(DialogsActivity$7$$ExternalSyntheticLambda2.INSTANCE);
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public void onSamePageSelected() {
            DialogsActivity.this.scrollToTop();
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public void onPageReorder(int i, int i2) {
            for (int i3 = 0; i3 < DialogsActivity.this.viewPages.length; i3++) {
                if (DialogsActivity.this.viewPages[i3].selectedType == i) {
                    DialogsActivity.this.viewPages[i3].selectedType = i2;
                } else if (DialogsActivity.this.viewPages[i3].selectedType == i2) {
                    DialogsActivity.this.viewPages[i3].selectedType = i;
                }
            }
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public void onPageSelected(FilterTabsView.Tab tab, boolean z) {
            int i;
            if (DialogsActivity.this.viewPages[0].selectedType == tab.id) {
                return;
            }
            if (tab.isLocked) {
                DialogsActivity.this.filterTabsView.shakeLock(tab.id);
                DialogsActivity dialogsActivity = DialogsActivity.this;
                DialogsActivity dialogsActivity2 = DialogsActivity.this;
                dialogsActivity.showDialog(new LimitReachedBottomSheet(dialogsActivity2, this.val$context, 3, ((BaseFragment) dialogsActivity2).currentAccount));
                return;
            }
            ArrayList<MessagesController.DialogFilter> arrayList = DialogsActivity.this.getMessagesController().dialogFilters;
            if (tab.isDefault || ((i = tab.id) >= 0 && i < arrayList.size())) {
                DialogsActivity dialogsActivity3 = DialogsActivity.this;
                dialogsActivity3.isFirstTab = tab.id == dialogsActivity3.filterTabsView.getFirstTabId();
                DialogsActivity.this.updateDrawerSwipeEnabled();
                DialogsActivity.this.viewPages[1].selectedType = tab.id;
                DialogsActivity.this.viewPages[1].setVisibility(0);
                DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth());
                DialogsActivity.this.showScrollbars(false);
                DialogsActivity.this.switchToCurrentSelectedMode(true);
                DialogsActivity.this.animatingForward = z;
            }
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public boolean canPerformActions() {
            return !DialogsActivity.this.searching;
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public void onPageScrolled(float f) {
            if (f != 1.0f || DialogsActivity.this.viewPages[1].getVisibility() == 0 || DialogsActivity.this.searching) {
                if (DialogsActivity.this.animatingForward) {
                    DialogsActivity.this.viewPages[0].setTranslationX((-f) * DialogsActivity.this.viewPages[0].getMeasuredWidth());
                    DialogsActivity.this.viewPages[1].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth() - (DialogsActivity.this.viewPages[0].getMeasuredWidth() * f));
                } else {
                    DialogsActivity.this.viewPages[0].setTranslationX(DialogsActivity.this.viewPages[0].getMeasuredWidth() * f);
                    DialogsActivity.this.viewPages[1].setTranslationX((DialogsActivity.this.viewPages[0].getMeasuredWidth() * f) - DialogsActivity.this.viewPages[0].getMeasuredWidth());
                }
                if (f == 1.0f) {
                    ViewPage viewPage = DialogsActivity.this.viewPages[0];
                    DialogsActivity.this.viewPages[0] = DialogsActivity.this.viewPages[1];
                    DialogsActivity.this.viewPages[1] = viewPage;
                    DialogsActivity.this.viewPages[1].setVisibility(8);
                    DialogsActivity.this.showScrollbars(true);
                    DialogsActivity.this.updateCounters(false);
                    DialogsActivity dialogsActivity = DialogsActivity.this;
                    dialogsActivity.checkListLoad(dialogsActivity.viewPages[0]);
                    DialogsActivity.this.viewPages[0].dialogsAdapter.resume();
                    DialogsActivity.this.viewPages[1].dialogsAdapter.pause();
                }
            }
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public int getTabCounter(int i) {
            if (i == DialogsActivity.this.filterTabsView.getDefaultTabId()) {
                return DialogsActivity.this.getMessagesStorage().getMainUnreadCount();
            }
            ArrayList<MessagesController.DialogFilter> arrayList = DialogsActivity.this.getMessagesController().dialogFilters;
            if (i < 0 || i >= arrayList.size()) {
                return 0;
            }
            return DialogsActivity.this.getMessagesController().dialogFilters.get(i).unreadCount;
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public boolean didSelectTab(FilterTabsView.TabView tabView, boolean z) {
            ScrollView scrollView;
            if (((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed()) {
                return false;
            }
            if (DialogsActivity.this.scrimPopupWindow != null) {
                DialogsActivity.this.scrimPopupWindow.dismiss();
                DialogsActivity.this.scrimPopupWindow = null;
                DialogsActivity.this.scrimPopupWindowItems = null;
                return false;
            }
            final Rect rect = new Rect();
            final MessagesController.DialogFilter dialogFilter = tabView.getId() != DialogsActivity.this.filterTabsView.getDefaultTabId() ? DialogsActivity.this.getMessagesController().dialogFilters.get(tabView.getId()) : null;
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(DialogsActivity.this.getParentActivity());
            actionBarPopupWindowLayout.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.DialogsActivity.7.1
                private int[] pos = new int[2];

                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() == 0) {
                        if (DialogsActivity.this.scrimPopupWindow != null && DialogsActivity.this.scrimPopupWindow.isShowing()) {
                            View contentView = DialogsActivity.this.scrimPopupWindow.getContentView();
                            contentView.getLocationInWindow(this.pos);
                            Rect rect2 = rect;
                            int[] iArr = this.pos;
                            rect2.set(iArr[0], iArr[1], iArr[0] + contentView.getMeasuredWidth(), this.pos[1] + contentView.getMeasuredHeight());
                            if (!rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                                DialogsActivity.this.scrimPopupWindow.dismiss();
                            }
                        }
                    } else if (motionEvent.getActionMasked() == 4 && DialogsActivity.this.scrimPopupWindow != null && DialogsActivity.this.scrimPopupWindow.isShowing()) {
                        DialogsActivity.this.scrimPopupWindow.dismiss();
                    }
                    return false;
                }
            });
            actionBarPopupWindowLayout.setDispatchKeyEventListener(new ActionBarPopupWindow.OnDispatchKeyEventListener() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda4
                @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
                public final void onDispatchKeyEvent(KeyEvent keyEvent) {
                    DialogsActivity.7.this.lambda$didSelectTab$3(keyEvent);
                }
            });
            Rect rect2 = new Rect();
            Drawable mutate = DialogsActivity.this.getParentActivity().getResources().getDrawable(R.drawable.popup_fixed_alert).mutate();
            mutate.getPadding(rect2);
            actionBarPopupWindowLayout.setBackgroundDrawable(mutate);
            actionBarPopupWindowLayout.setBackgroundColor(Theme.getColor("actionBarDefaultSubmenuBackground"));
            final LinearLayout linearLayout = new LinearLayout(DialogsActivity.this.getParentActivity());
            if (Build.VERSION.SDK_INT >= 21) {
                scrollView = new ScrollView(this, DialogsActivity.this.getParentActivity(), null, 0, R.style.scrollbarShapeStyle) { // from class: org.telegram.ui.DialogsActivity.7.2
                    @Override // android.widget.ScrollView, android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i, int i2) {
                        super.onMeasure(i, i2);
                        setMeasuredDimension(linearLayout.getMeasuredWidth(), getMeasuredHeight());
                    }
                };
            } else {
                scrollView = new ScrollView(DialogsActivity.this.getParentActivity());
            }
            scrollView.setClipToPadding(false);
            actionBarPopupWindowLayout.addView(scrollView, LayoutHelper.createFrame(-2, -2.0f));
            linearLayout.setMinimumWidth(AndroidUtilities.dp(200.0f));
            linearLayout.setOrientation(1);
            DialogsActivity.this.scrimPopupWindowItems = new ActionBarMenuSubItem[3];
            final int i = tabView.getId() == DialogsActivity.this.filterTabsView.getDefaultTabId() ? 2 : 3;
            final int i2 = 0;
            while (i2 < i) {
                ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(DialogsActivity.this.getParentActivity(), i2 == 0, i2 == i + (-1));
                if (i2 == 0) {
                    if (DialogsActivity.this.getMessagesController().dialogFilters.size() <= 1) {
                        i2++;
                    } else {
                        actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("FilterReorder", R.string.FilterReorder), R.drawable.tabs_reorder);
                    }
                } else if (i2 != 1) {
                    actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("FilterDeleteItem", R.string.FilterDeleteItem), R.drawable.msg_delete);
                } else if (i == 2) {
                    actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("FilterEditAll", R.string.FilterEditAll), R.drawable.msg_edit);
                } else {
                    actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("FilterEdit", R.string.FilterEdit), R.drawable.msg_edit);
                }
                DialogsActivity.this.scrimPopupWindowItems[i2] = actionBarMenuSubItem;
                linearLayout.addView(actionBarMenuSubItem);
                actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$7$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        DialogsActivity.7.this.lambda$didSelectTab$4(i2, i, dialogFilter, view);
                    }
                });
                i2++;
            }
            scrollView.addView(linearLayout, LayoutHelper.createScroll(-2, -2, 51));
            DialogsActivity.this.scrimPopupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2) { // from class: org.telegram.ui.DialogsActivity.7.3
                @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
                public void dismiss() {
                    super.dismiss();
                    if (DialogsActivity.this.scrimPopupWindow != this) {
                        return;
                    }
                    DialogsActivity.this.scrimPopupWindow = null;
                    DialogsActivity.this.scrimPopupWindowItems = null;
                    if (DialogsActivity.this.scrimAnimatorSet != null) {
                        DialogsActivity.this.scrimAnimatorSet.cancel();
                        DialogsActivity.this.scrimAnimatorSet = null;
                    }
                    DialogsActivity.this.scrimAnimatorSet = new AnimatorSet();
                    DialogsActivity.this.scrimViewAppearing = false;
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(ObjectAnimator.ofInt(DialogsActivity.this.scrimPaint, AnimationProperties.PAINT_ALPHA, 0));
                    DialogsActivity.this.scrimAnimatorSet.playTogether(arrayList);
                    DialogsActivity.this.scrimAnimatorSet.setDuration(220L);
                    DialogsActivity.this.scrimAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.7.3.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (DialogsActivity.this.scrimView != null) {
                                DialogsActivity.this.scrimView.setBackground(null);
                                DialogsActivity.this.scrimView = null;
                            }
                            if (((BaseFragment) DialogsActivity.this).fragmentView != null) {
                                ((BaseFragment) DialogsActivity.this).fragmentView.invalidate();
                            }
                        }
                    });
                    DialogsActivity.this.scrimAnimatorSet.start();
                    if (Build.VERSION.SDK_INT >= 19) {
                        DialogsActivity.this.getParentActivity().getWindow().getDecorView().setImportantForAccessibility(0);
                    }
                }
            };
            DialogsActivity.this.scrimViewBackground = Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), 0, Theme.getColor("actionBarDefault"));
            DialogsActivity.this.scrimPopupWindow.setDismissAnimationDuration(220);
            DialogsActivity.this.scrimPopupWindow.setOutsideTouchable(true);
            DialogsActivity.this.scrimPopupWindow.setClippingEnabled(true);
            DialogsActivity.this.scrimPopupWindow.setAnimationStyle(R.style.PopupContextAnimation);
            DialogsActivity.this.scrimPopupWindow.setFocusable(true);
            actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
            DialogsActivity.this.scrimPopupWindow.setInputMethodMode(2);
            DialogsActivity.this.scrimPopupWindow.setSoftInputMode(0);
            DialogsActivity.this.scrimPopupWindow.getContentView().setFocusableInTouchMode(true);
            tabView.getLocationInWindow(DialogsActivity.this.scrimViewLocation);
            int dp = (DialogsActivity.this.scrimViewLocation[0] + rect2.left) - AndroidUtilities.dp(16.0f);
            if (dp >= AndroidUtilities.dp(6.0f)) {
                if (dp > (((BaseFragment) DialogsActivity.this).fragmentView.getMeasuredWidth() - AndroidUtilities.dp(6.0f)) - actionBarPopupWindowLayout.getMeasuredWidth()) {
                    dp = (((BaseFragment) DialogsActivity.this).fragmentView.getMeasuredWidth() - AndroidUtilities.dp(6.0f)) - actionBarPopupWindowLayout.getMeasuredWidth();
                }
            } else {
                dp = AndroidUtilities.dp(6.0f);
            }
            DialogsActivity.this.scrimPopupWindow.showAtLocation(((BaseFragment) DialogsActivity.this).fragmentView, 51, dp, (DialogsActivity.this.scrimViewLocation[1] + tabView.getMeasuredHeight()) - AndroidUtilities.dp(12.0f));
            DialogsActivity.this.scrimView = tabView;
            DialogsActivity.this.scrimViewSelected = z;
            ((BaseFragment) DialogsActivity.this).fragmentView.invalidate();
            if (DialogsActivity.this.scrimAnimatorSet != null) {
                DialogsActivity.this.scrimAnimatorSet.cancel();
            }
            DialogsActivity.this.scrimAnimatorSet = new AnimatorSet();
            DialogsActivity.this.scrimViewAppearing = true;
            ArrayList arrayList = new ArrayList();
            arrayList.add(ObjectAnimator.ofInt(DialogsActivity.this.scrimPaint, AnimationProperties.PAINT_ALPHA, 0, 50));
            DialogsActivity.this.scrimAnimatorSet.playTogether(arrayList);
            DialogsActivity.this.scrimAnimatorSet.setDuration(150L);
            DialogsActivity.this.scrimAnimatorSet.start();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didSelectTab$3(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0 && DialogsActivity.this.scrimPopupWindow != null && DialogsActivity.this.scrimPopupWindow.isShowing()) {
                DialogsActivity.this.scrimPopupWindow.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didSelectTab$4(int i, int i2, MessagesController.DialogFilter dialogFilter, View view) {
            if (i == 0) {
                DialogsActivity.this.resetScroll();
                DialogsActivity.this.filterTabsView.setIsEditing(true);
                DialogsActivity.this.showDoneItem(true);
            } else if (i == 1) {
                if (i2 == 2) {
                    DialogsActivity.this.presentFragment(new FiltersSetupActivity());
                } else {
                    DialogsActivity.this.presentFragment(new FilterCreateActivity(dialogFilter));
                }
            } else if (i == 2) {
                showDeleteAlert(dialogFilter);
            }
            if (DialogsActivity.this.scrimPopupWindow != null) {
                DialogsActivity.this.scrimPopupWindow.dismiss();
            }
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public boolean isTabMenuVisible() {
            return DialogsActivity.this.scrimPopupWindow != null && DialogsActivity.this.scrimPopupWindow.isShowing();
        }

        @Override // org.telegram.ui.Components.FilterTabsView.FilterTabsViewDelegate
        public void onDeletePressed(int i) {
            showDeleteAlert(DialogsActivity.this.getMessagesController().dialogFilters.get(i));
        }
    }

    /* loaded from: classes3.dex */
    class 10 extends LinearLayoutManager {
        private boolean fixOffset;
        final /* synthetic */ ViewPage val$viewPage;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        10(Context context, ViewPage viewPage) {
            super(context);
            this.val$viewPage = viewPage;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager
        public void scrollToPositionWithOffset(int i, int i2) {
            if (this.fixOffset) {
                i2 -= this.val$viewPage.listView.getPaddingTop();
            }
            super.scrollToPositionWithOffset(i, i2);
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.ItemTouchHelper.ViewDropHandler
        public void prepareForDrop(View view, View view2, int i, int i2) {
            this.fixOffset = true;
            super.prepareForDrop(view, view2, i, i2);
            this.fixOffset = false;
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
            if (DialogsActivity.this.hasHiddenArchive() && i == 1) {
                super.smoothScrollToPosition(recyclerView, state, i);
                return;
            }
            LinearSmoothScrollerCustom linearSmoothScrollerCustom = new LinearSmoothScrollerCustom(recyclerView.getContext(), 0);
            linearSmoothScrollerCustom.setTargetPosition(i);
            startSmoothScroll(linearSmoothScrollerCustom);
        }

        /* JADX WARN: Removed duplicated region for block: B:56:0x013a  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x013e  */
        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
            int i2;
            float viewOffset;
            int i3;
            View findViewByPosition;
            if (this.val$viewPage.listView.fastScrollAnimationRunning) {
                return 0;
            }
            boolean z = this.val$viewPage.listView.getScrollState() == 1;
            int paddingTop = this.val$viewPage.listView.getPaddingTop();
            if (this.val$viewPage.dialogsType == 0 && !DialogsActivity.this.onlySelect && DialogsActivity.this.folderId == 0 && i < 0 && DialogsActivity.this.getMessagesController().hasHiddenArchive() && this.val$viewPage.archivePullViewState == 2) {
                this.val$viewPage.listView.setOverScrollMode(0);
                int findFirstVisibleItemPosition = this.val$viewPage.layoutManager.findFirstVisibleItemPosition();
                if (findFirstVisibleItemPosition == 0 && (findViewByPosition = this.val$viewPage.layoutManager.findViewByPosition(findFirstVisibleItemPosition)) != null && findViewByPosition.getBottom() - paddingTop <= AndroidUtilities.dp(1.0f)) {
                    findFirstVisibleItemPosition = 1;
                }
                if (!z) {
                    View findViewByPosition2 = this.val$viewPage.layoutManager.findViewByPosition(findFirstVisibleItemPosition);
                    if (findViewByPosition2 != null) {
                        int dp = (-(findViewByPosition2.getTop() - paddingTop)) + ((findFirstVisibleItemPosition - 1) * (AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 78.0f : 72.0f) + 1));
                        if (dp < Math.abs(i)) {
                            i2 = -dp;
                        }
                    }
                } else if (findFirstVisibleItemPosition == 0) {
                    View findViewByPosition3 = this.val$viewPage.layoutManager.findViewByPosition(findFirstVisibleItemPosition);
                    float top = ((findViewByPosition3.getTop() - paddingTop) / findViewByPosition3.getMeasuredHeight()) + 1.0f;
                    if (top > 1.0f) {
                        top = 1.0f;
                    }
                    this.val$viewPage.listView.setOverScrollMode(2);
                    i2 = (int) (i * (0.45f - (top * 0.25f)));
                    if (i2 > -1) {
                        i2 = -1;
                    }
                    if (DialogsActivity.this.undoView[0].getVisibility() == 0) {
                        DialogsActivity.this.undoView[0].hide(true, 1);
                    }
                }
                if (this.val$viewPage.dialogsType == 0 && this.val$viewPage.listView.getViewOffset() != 0.0f && i > 0 && z) {
                    viewOffset = ((int) this.val$viewPage.listView.getViewOffset()) - i;
                    if (viewOffset >= 0.0f) {
                        i3 = (int) viewOffset;
                        viewOffset = 0.0f;
                    } else {
                        i3 = 0;
                    }
                    this.val$viewPage.listView.setViewsOffset(viewOffset);
                    i2 = i3;
                }
                if (this.val$viewPage.dialogsType != 0 && this.val$viewPage.archivePullViewState != 0 && DialogsActivity.this.hasHiddenArchive()) {
                    int scrollVerticallyBy = super.scrollVerticallyBy(i2, recycler, state);
                    if (this.val$viewPage.pullForegroundDrawable != null) {
                        this.val$viewPage.pullForegroundDrawable.scrollDy = scrollVerticallyBy;
                    }
                    int findFirstVisibleItemPosition2 = this.val$viewPage.layoutManager.findFirstVisibleItemPosition();
                    View findViewByPosition4 = findFirstVisibleItemPosition2 == 0 ? this.val$viewPage.layoutManager.findViewByPosition(findFirstVisibleItemPosition2) : null;
                    if (findFirstVisibleItemPosition2 != 0 || findViewByPosition4 == null || findViewByPosition4.getBottom() - paddingTop < AndroidUtilities.dp(4.0f)) {
                        DialogsActivity.this.startArchivePullingTime = 0L;
                        DialogsActivity.this.canShowHiddenArchive = false;
                        r5 = this.val$viewPage.archivePullViewState != 2;
                        this.val$viewPage.archivePullViewState = 2;
                        if (r5 && AndroidUtilities.isAccessibilityScreenReaderEnabled()) {
                            AndroidUtilities.makeAccessibilityAnnouncement(LocaleController.getString(R.string.AccDescrArchivedChatsHidden));
                        }
                        if (this.val$viewPage.pullForegroundDrawable != null) {
                            this.val$viewPage.pullForegroundDrawable.resetText();
                            this.val$viewPage.pullForegroundDrawable.pullProgress = 0.0f;
                            this.val$viewPage.pullForegroundDrawable.setListView(this.val$viewPage.listView);
                        }
                    } else {
                        if (DialogsActivity.this.startArchivePullingTime == 0) {
                            DialogsActivity.this.startArchivePullingTime = System.currentTimeMillis();
                        }
                        if (this.val$viewPage.archivePullViewState == 2 && this.val$viewPage.pullForegroundDrawable != null) {
                            this.val$viewPage.pullForegroundDrawable.showHidden();
                        }
                        float top2 = ((findViewByPosition4.getTop() - paddingTop) / findViewByPosition4.getMeasuredHeight()) + 1.0f;
                        if (top2 > 1.0f) {
                            top2 = 1.0f;
                        }
                        long currentTimeMillis = System.currentTimeMillis() - DialogsActivity.this.startArchivePullingTime;
                        if (top2 > 0.85f && currentTimeMillis > 220) {
                            r5 = true;
                        }
                        if (DialogsActivity.this.canShowHiddenArchive != r5) {
                            DialogsActivity.this.canShowHiddenArchive = r5;
                            if (this.val$viewPage.archivePullViewState == 2) {
                                this.val$viewPage.listView.performHapticFeedback(3, 2);
                                if (this.val$viewPage.pullForegroundDrawable != null) {
                                    this.val$viewPage.pullForegroundDrawable.colorize(r5);
                                }
                            }
                        }
                        if (this.val$viewPage.archivePullViewState == 2 && i2 - scrollVerticallyBy != 0 && i < 0 && z) {
                            this.val$viewPage.listView.setViewsOffset(this.val$viewPage.listView.getViewOffset() - ((i * 0.2f) * (1.0f - (this.val$viewPage.listView.getViewOffset() / PullForegroundDrawable.getMaxOverscroll()))));
                        }
                        if (this.val$viewPage.pullForegroundDrawable != null) {
                            this.val$viewPage.pullForegroundDrawable.pullProgress = top2;
                            this.val$viewPage.pullForegroundDrawable.setListView(this.val$viewPage.listView);
                        }
                    }
                    if (findViewByPosition4 != null) {
                        findViewByPosition4.invalidate();
                    }
                    return scrollVerticallyBy;
                }
                return super.scrollVerticallyBy(i2, recycler, state);
            }
            i2 = i;
            if (this.val$viewPage.dialogsType == 0) {
                viewOffset = ((int) this.val$viewPage.listView.getViewOffset()) - i;
                if (viewOffset >= 0.0f) {
                }
                this.val$viewPage.listView.setViewsOffset(viewOffset);
                i2 = i3;
            }
            if (this.val$viewPage.dialogsType != 0) {
            }
            return super.scrollVerticallyBy(i2, recycler, state);
        }

        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            if (BuildVars.DEBUG_PRIVATE_VERSION) {
                try {
                    super.onLayoutChildren(recycler, state);
                    return;
                } catch (IndexOutOfBoundsException unused) {
                    throw new RuntimeException("Inconsistency detected. dialogsListIsFrozen=" + DialogsActivity.this.dialogsListFrozen + " lastUpdateAction=" + DialogsActivity.this.debugLastUpdateAction);
                }
            }
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                FileLog.e(e);
                final ViewPage viewPage = this.val$viewPage;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$10$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.10.lambda$onLayoutChildren$0(DialogsActivity.ViewPage.this);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onLayoutChildren$0(ViewPage viewPage) {
            viewPage.dialogsAdapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(ViewPage viewPage, View view, int i) {
        int i2 = this.initialDialogsType;
        if (i2 == 15 && (view instanceof TextCell)) {
            viewPage.dialogsAdapter.onCreateGroupForThisClick();
        } else if (i2 == 10) {
            onItemLongClick(viewPage.listView, view, i, 0.0f, 0.0f, viewPage.dialogsType, viewPage.dialogsAdapter);
        } else if ((i2 != 11 && i2 != 13) || i != 1) {
            onItemClick(view, i, viewPage.dialogsAdapter);
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("forImport", true);
            bundle.putLongArray("result", new long[]{getUserConfig().getClientUserId()});
            bundle.putInt("chatType", 4);
            String string = this.arguments.getString("importTitle");
            if (string != null) {
                bundle.putString("title", string);
            }
            GroupCreateFinalActivity groupCreateFinalActivity = new GroupCreateFinalActivity(bundle);
            groupCreateFinalActivity.setDelegate(new GroupCreateFinalActivity.GroupCreateFinalActivityDelegate() { // from class: org.telegram.ui.DialogsActivity.11
                @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
                public void didFailChatCreation() {
                }

                @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
                public void didStartChatCreation() {
                }

                @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
                public void didFinishChatCreation(GroupCreateFinalActivity groupCreateFinalActivity2, long j) {
                    ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
                    arrayList.add(MessagesStorage.TopicKey.of(-j, 0));
                    DialogsActivityDelegate dialogsActivityDelegate = DialogsActivity.this.delegate;
                    if (DialogsActivity.this.closeFragment) {
                        DialogsActivity.this.removeSelfFromStack();
                    }
                    dialogsActivityDelegate.didSelectDialogs(DialogsActivity.this, arrayList, null, true, null);
                }
            });
            presentFragment(groupCreateFinalActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 18 implements DialogsSearchAdapter.DialogsSearchAdapterDelegate {
        18() {
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public void searchStateChanged(boolean z, boolean z2) {
            if (DialogsActivity.this.searchViewPager.emptyView.getVisibility() == 0) {
                z2 = true;
            }
            if (DialogsActivity.this.searching && DialogsActivity.this.searchWas && DialogsActivity.this.searchViewPager.emptyView != null) {
                if (z || DialogsActivity.this.searchViewPager.dialogsSearchAdapter.getItemCount() != 0) {
                    DialogsActivity.this.searchViewPager.emptyView.showProgress(true, z2);
                } else {
                    DialogsActivity.this.searchViewPager.emptyView.showProgress(false, z2);
                }
            }
            if (z && DialogsActivity.this.searchViewPager.dialogsSearchAdapter.getItemCount() == 0) {
                DialogsActivity.this.searchViewPager.cancelEnterAnimation();
            }
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public void didPressedOnSubDialog(long j) {
            if (DialogsActivity.this.onlySelect) {
                if (DialogsActivity.this.validateSlowModeDialog(j)) {
                    if (!DialogsActivity.this.selectedDialogs.isEmpty()) {
                        DialogsActivity.this.findAndUpdateCheckBox(j, DialogsActivity.this.addOrRemoveSelectedDialog(j, null));
                        DialogsActivity.this.updateSelectedCount();
                        ((BaseFragment) DialogsActivity.this).actionBar.closeSearchField();
                        return;
                    }
                    DialogsActivity.this.didSelectResult(j, 0, true, false);
                    return;
                }
                return;
            }
            Bundle bundle = new Bundle();
            if (DialogObject.isUserDialog(j)) {
                bundle.putLong("user_id", j);
            } else {
                bundle.putLong("chat_id", -j);
            }
            DialogsActivity.this.closeSearch();
            if (AndroidUtilities.isTablet() && DialogsActivity.this.viewPages != null) {
                for (int i = 0; i < DialogsActivity.this.viewPages.length; i++) {
                    DialogsAdapter dialogsAdapter = DialogsActivity.this.viewPages[i].dialogsAdapter;
                    DialogsActivity.this.openedDialogId.dialogId = j;
                    dialogsAdapter.setOpenedDialogId(j);
                }
                DialogsActivity.this.updateVisibleRows(MessagesController.UPDATE_MASK_SELECT_DIALOG);
            }
            if (DialogsActivity.this.searchString != null) {
                if (DialogsActivity.this.getMessagesController().checkCanOpenChat(bundle, DialogsActivity.this)) {
                    DialogsActivity.this.getNotificationCenter().postNotificationName(NotificationCenter.closeChats, new Object[0]);
                    DialogsActivity.this.presentFragment(new ChatActivity(bundle));
                }
            } else if (DialogsActivity.this.getMessagesController().checkCanOpenChat(bundle, DialogsActivity.this)) {
                DialogsActivity.this.presentFragment(new ChatActivity(bundle));
            }
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public void needRemoveHint(final long j) {
            TLRPC$User user;
            if (DialogsActivity.this.getParentActivity() == null || (user = DialogsActivity.this.getMessagesController().getUser(Long.valueOf(j))) == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(DialogsActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString("ChatHintsDeleteAlertTitle", R.string.ChatHintsDeleteAlertTitle));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("ChatHintsDeleteAlert", R.string.ChatHintsDeleteAlert, ContactsController.formatName(user.first_name, user.last_name))));
            builder.setPositiveButton(LocaleController.getString("StickersRemove", R.string.StickersRemove), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$18$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DialogsActivity.18.this.lambda$needRemoveHint$0(j, dialogInterface, i);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog create = builder.create();
            DialogsActivity.this.showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$needRemoveHint$0(long j, DialogInterface dialogInterface, int i) {
            DialogsActivity.this.getMediaDataController().removePeer(j);
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public void needClearList() {
            AlertDialog.Builder builder = new AlertDialog.Builder(DialogsActivity.this.getParentActivity());
            if (DialogsActivity.this.searchViewPager.dialogsSearchAdapter.isSearchWas() && DialogsActivity.this.searchViewPager.dialogsSearchAdapter.isRecentSearchDisplayed()) {
                builder.setTitle(LocaleController.getString("ClearSearchAlertPartialTitle", R.string.ClearSearchAlertPartialTitle));
                builder.setMessage(LocaleController.formatPluralString("ClearSearchAlertPartial", DialogsActivity.this.searchViewPager.dialogsSearchAdapter.getRecentResultsCount(), new Object[0]));
                builder.setPositiveButton(LocaleController.getString("Clear", R.string.Clear), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$18$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        DialogsActivity.18.this.lambda$needClearList$1(dialogInterface, i);
                    }
                });
            } else {
                builder.setTitle(LocaleController.getString("ClearSearchAlertTitle", R.string.ClearSearchAlertTitle));
                builder.setMessage(LocaleController.getString("ClearSearchAlert", R.string.ClearSearchAlert));
                builder.setPositiveButton(LocaleController.getString("ClearButton", R.string.ClearButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$18$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        DialogsActivity.18.this.lambda$needClearList$2(dialogInterface, i);
                    }
                });
            }
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog create = builder.create();
            DialogsActivity.this.showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$needClearList$1(DialogInterface dialogInterface, int i) {
            DialogsActivity.this.searchViewPager.dialogsSearchAdapter.clearRecentSearch();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$needClearList$2(DialogInterface dialogInterface, int i) {
            if (DialogsActivity.this.searchViewPager.dialogsSearchAdapter.isRecentSearchDisplayed()) {
                DialogsActivity.this.searchViewPager.dialogsSearchAdapter.clearRecentSearch();
            } else {
                DialogsActivity.this.searchViewPager.dialogsSearchAdapter.clearRecentHashtags();
            }
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public void runResultsEnterAnimation() {
            if (DialogsActivity.this.searchViewPager != null) {
                DialogsActivity.this.searchViewPager.runResultsEnterAnimation();
            }
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public boolean isSelected(long j) {
            return DialogsActivity.this.selectedDialogs.contains(Long.valueOf(j));
        }

        @Override // org.telegram.ui.Adapters.DialogsSearchAdapter.DialogsSearchAdapterDelegate
        public long getSearchForumDialogId() {
            RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer == null || !(rightSlidingDialogContainer.getFragment() instanceof TopicsFragment)) {
                return 0L;
            }
            return ((TopicsFragment) DialogsActivity.this.rightSlidingDialogContainer.getFragment()).getDialogId();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(View view, int i) {
        if (this.initialDialogsType == 10) {
            SearchViewPager searchViewPager = this.searchViewPager;
            onItemLongClick(searchViewPager.searchListView, view, i, 0.0f, 0.0f, -1, searchViewPager.dialogsSearchAdapter);
            return;
        }
        onItemClick(view, i, this.searchViewPager.dialogsSearchAdapter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(boolean z, ArrayList arrayList, ArrayList arrayList2, boolean z2) {
        updateFiltersView(z, arrayList, arrayList2, z2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(View view, int i) {
        this.filtersView.cancelClickRunnables(true);
        addSearchFilter(this.filtersView.getFilterAt(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$11(View view) {
        INavigationLayout iNavigationLayout = this.parentLayout;
        if (iNavigationLayout != null && iNavigationLayout.isInPreviewMode()) {
            finishPreviewFragment();
        } else if (this.initialDialogsType == 10) {
            if (this.delegate == null || this.selectedDialogs.isEmpty()) {
                return;
            }
            ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
            for (int i = 0; i < this.selectedDialogs.size(); i++) {
                arrayList.add(MessagesStorage.TopicKey.of(this.selectedDialogs.get(i).longValue(), 0));
            }
            this.delegate.didSelectDialogs(this, arrayList, null, false, null);
        } else if (this.floatingButton.getVisibility() == 0) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("destroyAfterSelect", true);
            presentFragment(new ContactsActivity(bundle));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$12(Long l) {
        this.cacheSize = l;
        updateDialogsHint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$13(Long l, Long l2) {
        this.deviceSize = l;
        updateDialogsHint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$14(View view) {
        if (this.delegate == null || this.selectedDialogs.isEmpty()) {
            return;
        }
        ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
        for (int i = 0; i < this.selectedDialogs.size(); i++) {
            arrayList.add(MessagesStorage.TopicKey.of(this.selectedDialogs.get(i).longValue(), 0));
        }
        this.delegate.didSelectDialogs(this, arrayList, this.commentView.getFieldText(), false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$15(FrameLayout frameLayout, View view) {
        if (this.isNextButton) {
            return false;
        }
        onSendLongClick(frameLayout);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$16(View view) {
        if (SharedConfig.isAppUpdateAvailable()) {
            AndroidUtilities.openForView(SharedConfig.pendingAppUpdate.document, true, getParentActivity());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 27 extends UndoView {
        27(Context context) {
            super(context);
        }

        @Override // android.view.View
        public void setTranslationY(float f) {
            super.setTranslationY(f);
            if (this != DialogsActivity.this.undoView[0] || DialogsActivity.this.undoView[1].getVisibility() == 0) {
                return;
            }
            DialogsActivity.this.additionalFloatingTranslation = (getMeasuredHeight() + AndroidUtilities.dp(8.0f)) - f;
            if (DialogsActivity.this.additionalFloatingTranslation < 0.0f) {
                DialogsActivity.this.additionalFloatingTranslation = 0.0f;
            }
            if (DialogsActivity.this.floatingHidden) {
                return;
            }
            DialogsActivity.this.updateFloatingButtonOffset();
        }

        @Override // org.telegram.ui.Components.UndoView
        protected boolean canUndo() {
            for (int i = 0; i < DialogsActivity.this.viewPages.length; i++) {
                if (DialogsActivity.this.viewPages[i].dialogsItemAnimator.isRunning()) {
                    return false;
                }
            }
            return true;
        }

        @Override // org.telegram.ui.Components.UndoView
        protected void onRemoveDialogAction(long j, int i) {
            if (i == 1 || i == 27) {
                DialogsActivity.this.debugLastUpdateAction = 1;
                DialogsActivity.this.setDialogsListFrozen(true);
                if (DialogsActivity.this.frozenDialogsList != null) {
                    final int i2 = -1;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= DialogsActivity.this.frozenDialogsList.size()) {
                            break;
                        } else if (((TLRPC$Dialog) DialogsActivity.this.frozenDialogsList.get(i3)).id == j) {
                            i2 = i3;
                            break;
                        } else {
                            i3++;
                        }
                    }
                    if (i2 >= 0) {
                        final TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) DialogsActivity.this.frozenDialogsList.remove(i2);
                        DialogsActivity.this.viewPages[0].dialogsAdapter.notifyDataSetChanged();
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$27$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                DialogsActivity.27.this.lambda$onRemoveDialogAction$0(i2, tLRPC$Dialog);
                            }
                        });
                    } else {
                        DialogsActivity.this.setDialogsListFrozen(false);
                    }
                }
                DialogsActivity.this.checkAnimationFinished();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRemoveDialogAction$0(int i, TLRPC$Dialog tLRPC$Dialog) {
            if (DialogsActivity.this.frozenDialogsList == null || i < 0 || i >= DialogsActivity.this.frozenDialogsList.size()) {
                return;
            }
            DialogsActivity.this.frozenDialogsList.add(i, tLRPC$Dialog);
            DialogsActivity.this.viewPages[0].updateList(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$17(View view) {
        finishPreviewFragment();
    }

    public boolean isPremiumHintVisible() {
        if (MessagesController.getInstance(this.currentAccount).premiumLocked || this.folderId != 0 || ((!(MessagesController.getInstance(this.currentAccount).pendingSuggestions.contains("PREMIUM_UPGRADE") && getUserConfig().isPremium()) && (!MessagesController.getInstance(this.currentAccount).pendingSuggestions.contains("PREMIUM_ANNUAL") || getUserConfig().isPremium())) || (!UserConfig.getInstance(this.currentAccount).isPremium() ? MediaDataController.getInstance(this.currentAccount).getPremiumHintAnnualDiscount(false) != null : !(BuildVars.useInvoiceBilling() || MediaDataController.getInstance(this.currentAccount).getPremiumHintAnnualDiscount(true) == null)))) {
            return false;
        }
        this.isPremiumHintUpgrade = MessagesController.getInstance(this.currentAccount).pendingSuggestions.contains("PREMIUM_UPGRADE");
        return true;
    }

    private boolean isCacheHintVisible() {
        Long l = this.cacheSize;
        if (l == null || this.deviceSize == null) {
            return false;
        }
        if (((float) l.longValue()) / ((float) this.deviceSize.longValue()) >= 0.3f) {
            return System.currentTimeMillis() > MessagesController.getGlobalMainSettings().getLong("cache_hint_showafter", 0L);
        }
        clearCacheHintVisible();
        return false;
    }

    private void resetCacheHintVisible() {
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        long j = globalMainSettings.getLong("cache_hint_period", 604800000L);
        if (j <= 604800000) {
            j = 2592000000L;
        }
        globalMainSettings.edit().putLong("cache_hint_showafter", System.currentTimeMillis() + j).putLong("cache_hint_period", j).apply();
    }

    private void clearCacheHintVisible() {
        MessagesController.getGlobalMainSettings().edit().remove("cache_hint_showafter").remove("cache_hint_period").apply();
    }

    public void showSelectStatusDialog() {
        int i;
        int i2;
        if (this.selectAnimatedEmojiDialog != null || SharedConfig.appLocked) {
            return;
        }
        final SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[1];
        TLRPC$User currentUser = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
        SimpleTextView titleTextView = this.actionBar.getTitleTextView();
        if (titleTextView == null || titleTextView.getRightDrawable() == null) {
            i = 0;
            i2 = 0;
        } else {
            this.statusDrawable.play();
            boolean z = this.statusDrawable.getDrawable() instanceof AnimatedEmojiDrawable;
            Rect rect = AndroidUtilities.rectTmp2;
            rect.set(titleTextView.getRightDrawable().getBounds());
            rect.offset((int) titleTextView.getX(), (int) titleTextView.getY());
            int dp = (-(this.actionBar.getHeight() - rect.centerY())) - AndroidUtilities.dp(16.0f);
            i = rect.centerX() - AndroidUtilities.dp(16.0f);
            DrawerProfileCell.AnimatedStatusView animatedStatusView = this.animatedStatusView;
            if (animatedStatusView != null) {
                animatedStatusView.translate(rect.centerX(), rect.centerY());
            }
            i2 = dp;
        }
        int i3 = i2;
        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = new SelectAnimatedEmojiDialog(this, getContext(), true, Integer.valueOf(i), 0, getResourceProvider()) { // from class: org.telegram.ui.DialogsActivity.30
            @Override // org.telegram.ui.SelectAnimatedEmojiDialog
            protected void onEmojiSelected(View view, Long l, TLRPC$Document tLRPC$Document, Integer num) {
                TLRPC$TL_emojiStatusUntil tLRPC$TL_emojiStatusUntil;
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
                DialogsActivity.this.getMessagesController().updateEmojiStatus(tLRPC$TL_emojiStatusUntil);
                if (l != null) {
                    DialogsActivity.this.animatedStatusView.animateChange(ReactionsLayoutInBubble.VisibleReaction.fromCustomEmoji(l));
                }
                if (selectAnimatedEmojiDialogWindowArr[0] != null) {
                    DialogsActivity.this.selectAnimatedEmojiDialog = null;
                    selectAnimatedEmojiDialogWindowArr[0].dismiss();
                }
            }
        };
        if (currentUser != null) {
            TLRPC$EmojiStatus tLRPC$EmojiStatus = currentUser.emoji_status;
            if ((tLRPC$EmojiStatus instanceof TLRPC$TL_emojiStatusUntil) && ((TLRPC$TL_emojiStatusUntil) tLRPC$EmojiStatus).until > ((int) (System.currentTimeMillis() / 1000))) {
                selectAnimatedEmojiDialog.setExpireDateHint(((TLRPC$TL_emojiStatusUntil) currentUser.emoji_status).until);
            }
        }
        selectAnimatedEmojiDialog.setSelected(this.statusDrawable.getDrawable() instanceof AnimatedEmojiDrawable ? Long.valueOf(((AnimatedEmojiDrawable) this.statusDrawable.getDrawable()).getDocumentId()) : null);
        selectAnimatedEmojiDialog.setSaveState(1);
        selectAnimatedEmojiDialog.setScrimDrawable(this.statusDrawable, titleTextView);
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(selectAnimatedEmojiDialog, -2, -2) { // from class: org.telegram.ui.DialogsActivity.31
            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
            public void dismiss() {
                super.dismiss();
                DialogsActivity.this.selectAnimatedEmojiDialog = null;
            }
        };
        this.selectAnimatedEmojiDialog = selectAnimatedEmojiDialogWindow;
        selectAnimatedEmojiDialogWindowArr[0] = selectAnimatedEmojiDialogWindow;
        selectAnimatedEmojiDialogWindowArr[0].showAsDropDown(this.actionBar, AndroidUtilities.dp(16.0f), i3, 48);
        selectAnimatedEmojiDialogWindowArr[0].dimBehind();
    }

    private void updateDialogsHint() {
        if (this.dialogsHintCell == null) {
            return;
        }
        if (isPremiumHintVisible()) {
            this.dialogsHintCell.setVisibility(0);
            this.dialogsHintCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda30
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.this.lambda$updateDialogsHint$20(view);
                }
            });
            this.dialogsHintCell.setText(AndroidUtilities.replaceSingleTag(LocaleController.formatString(this.isPremiumHintUpgrade ? R.string.SaveOnAnnualPremiumTitle : R.string.UpgradePremiumTitle, MediaDataController.getInstance(this.currentAccount).getPremiumHintAnnualDiscount(false)), "windowBackgroundWhiteValueText", 0, null), LocaleController.getString(this.isPremiumHintUpgrade ? R.string.UpgradePremiumMessage : R.string.SaveOnAnnualPremiumMessage));
        } else if (isCacheHintVisible()) {
            this.dialogsHintCell.setVisibility(0);
            this.dialogsHintCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda28
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.this.lambda$updateDialogsHint$22(view);
                }
            });
            this.dialogsHintCell.setText(AndroidUtilities.replaceSingleTag(LocaleController.formatString(R.string.ClearStorageHintTitle, AndroidUtilities.formatFileSize(this.cacheSize.longValue())), "windowBackgroundWhiteValueText", 0, null), LocaleController.getString(R.string.ClearStorageHintMessage));
        } else {
            this.dialogsHintCell.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$20(View view) {
        presentFragment(new PremiumPreviewFragment("dialogs_hint").setSelectAnnualByDefault());
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda48
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$updateDialogsHint$19();
            }
        }, 250L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$19() {
        MessagesController.getInstance(this.currentAccount).removeSuggestion(0L, this.isPremiumHintUpgrade ? "PREMIUM_UPGRADE" : "PREMIUM_ANNUAL");
        updateDialogsHint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$22(View view) {
        presentFragment(new CacheControlActivity());
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$updateDialogsHint$21();
            }
        }, 250L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateDialogsHint$21() {
        resetCacheHintVisible();
        updateDialogsHint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createGroupForThis() {
        final AlertDialog alertDialog = new AlertDialog(getContext(), 3);
        TLRPC$RequestPeerType tLRPC$RequestPeerType = this.requestPeerType;
        if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeBroadcast) {
            Bundle bundle = new Bundle();
            bundle.putInt("step", 0);
            Boolean bool = this.requestPeerType.has_username;
            if (bool != null) {
                bundle.putBoolean("forcePublic", bool.booleanValue());
            }
            final ChannelCreateActivity channelCreateActivity = new ChannelCreateActivity(bundle);
            channelCreateActivity.setOnFinishListener(new Utilities.Callback2() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda75
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    DialogsActivity.this.lambda$createGroupForThis$32(channelCreateActivity, alertDialog, (BaseFragment) obj, (Long) obj2);
                }
            });
            presentFragment(channelCreateActivity);
        } else if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeChat) {
            Bundle bundle2 = new Bundle();
            Boolean bool2 = this.requestPeerType.bot_participant;
            bundle2.putLongArray("result", (bool2 == null || !bool2.booleanValue()) ? new long[]{getUserConfig().getClientUserId()} : new long[]{getUserConfig().getClientUserId(), this.requestPeerBotId});
            Boolean bool3 = this.requestPeerType.forum;
            bundle2.putInt("chatType", (bool3 == null || !bool3.booleanValue()) ? 4 : 5);
            bundle2.putBoolean("canToggleTopics", false);
            GroupCreateFinalActivity groupCreateFinalActivity = new GroupCreateFinalActivity(bundle2);
            groupCreateFinalActivity.setDelegate(new 32(alertDialog));
            presentFragment(groupCreateFinalActivity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$32(final ChannelCreateActivity channelCreateActivity, final AlertDialog alertDialog, final BaseFragment baseFragment, final Long l) {
        Utilities.doCallbacks(new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda79
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                DialogsActivity.this.lambda$createGroupForThis$24(l, channelCreateActivity, baseFragment, (Runnable) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda80
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                DialogsActivity.this.lambda$createGroupForThis$26(alertDialog, l, (Runnable) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda78
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                DialogsActivity.this.lambda$createGroupForThis$28(l, (Runnable) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda77
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                DialogsActivity.this.lambda$createGroupForThis$30(l, (Runnable) obj);
            }
        }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda81
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                DialogsActivity.this.lambda$createGroupForThis$31(alertDialog, l, channelCreateActivity, baseFragment, (Runnable) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$24(Long l, final ChannelCreateActivity channelCreateActivity, final BaseFragment baseFragment, Runnable runnable) {
        showSendToBotAlert(getMessagesController().getChat(l), runnable, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda62
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$createGroupForThis$23(channelCreateActivity, baseFragment);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$23(ChannelCreateActivity channelCreateActivity, BaseFragment baseFragment) {
        removeSelfFromStack();
        channelCreateActivity.removeSelfFromStack();
        baseFragment.finishFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$26(AlertDialog alertDialog, Long l, final Runnable runnable) {
        alertDialog.showDelayed(150L);
        Boolean bool = this.requestPeerType.bot_participant;
        if (bool != null && bool.booleanValue()) {
            getMessagesController().addUserToChat(l.longValue(), getMessagesController().getUser(Long.valueOf(this.requestPeerBotId)), 0, null, this, false, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda70
                @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                    boolean run;
                    run = runnable.run();
                    return run;
                }
            });
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$28(Long l, final Runnable runnable) {
        if (this.requestPeerType.bot_admin_rights != null) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.requestPeerBotId));
            MessagesController messagesController = getMessagesController();
            long longValue = l.longValue();
            TLRPC$RequestPeerType tLRPC$RequestPeerType = this.requestPeerType;
            TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = tLRPC$RequestPeerType.bot_admin_rights;
            Boolean bool = tLRPC$RequestPeerType.bot_participant;
            messagesController.setUserAdminRole(longValue, user, tLRPC$TL_chatAdminRights, null, false, this, bool == null || !bool.booleanValue(), true, null, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda67
                @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                    boolean run;
                    run = runnable.run();
                    return run;
                }
            });
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$30(Long l, final Runnable runnable) {
        if (this.requestPeerType.user_admin_rights != null) {
            getMessagesController().setUserAdminRole(l.longValue(), getAccountInstance().getUserConfig().getCurrentUser(), ChatRightsEditActivity.rightsOR(getMessagesController().getChat(l).admin_rights, this.requestPeerType.user_admin_rights), null, true, this, false, true, null, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda68
                @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                    boolean run;
                    run = runnable.run();
                    return run;
                }
            });
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGroupForThis$31(AlertDialog alertDialog, Long l, ChannelCreateActivity channelCreateActivity, BaseFragment baseFragment, Runnable runnable) {
        alertDialog.dismiss();
        getMessagesController().loadChannelParticipants(l);
        DialogsActivityDelegate dialogsActivityDelegate = this.delegate;
        removeSelfFromStack();
        channelCreateActivity.removeSelfFromStack();
        baseFragment.finishFragment();
        if (dialogsActivityDelegate != null) {
            ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
            arrayList.add(MessagesStorage.TopicKey.of(-l.longValue(), 0));
            dialogsActivityDelegate.didSelectDialogs(this, arrayList, null, false, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 32 implements GroupCreateFinalActivity.GroupCreateFinalActivityDelegate {
        final /* synthetic */ AlertDialog val$progress;

        @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
        public void didFailChatCreation() {
        }

        @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
        public void didStartChatCreation() {
        }

        32(AlertDialog alertDialog) {
            this.val$progress = alertDialog;
        }

        @Override // org.telegram.ui.GroupCreateFinalActivity.GroupCreateFinalActivityDelegate
        public void didFinishChatCreation(GroupCreateFinalActivity groupCreateFinalActivity, final long j) {
            final BaseFragment[] baseFragmentArr = {groupCreateFinalActivity, null};
            final AlertDialog alertDialog = this.val$progress;
            final AlertDialog alertDialog2 = this.val$progress;
            Utilities.doCallbacks(new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$32$$ExternalSyntheticLambda7
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.32.this.lambda$didFinishChatCreation$1(j, baseFragmentArr, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$32$$ExternalSyntheticLambda8
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.32.this.lambda$didFinishChatCreation$3(j, baseFragmentArr, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$32$$ExternalSyntheticLambda9
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.32.this.lambda$didFinishChatCreation$5(alertDialog, j, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$32$$ExternalSyntheticLambda5
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.32.this.lambda$didFinishChatCreation$7(j, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$32$$ExternalSyntheticLambda6
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.32.this.lambda$didFinishChatCreation$9(j, (Runnable) obj);
                }
            }, new Utilities.Callback() { // from class: org.telegram.ui.DialogsActivity$32$$ExternalSyntheticLambda10
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    DialogsActivity.32.this.lambda$didFinishChatCreation$10(alertDialog2, j, baseFragmentArr, (Runnable) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$1(long j, BaseFragment[] baseFragmentArr, final Runnable runnable) {
            if (DialogsActivity.this.requestPeerType.has_username != null && DialogsActivity.this.requestPeerType.has_username.booleanValue()) {
                Bundle bundle = new Bundle();
                bundle.putInt("step", 1);
                bundle.putLong("chat_id", j);
                bundle.putBoolean("forcePublic", DialogsActivity.this.requestPeerType.has_username.booleanValue());
                ChannelCreateActivity channelCreateActivity = new ChannelCreateActivity(bundle);
                channelCreateActivity.setOnFinishListener(new Utilities.Callback2() { // from class: org.telegram.ui.DialogsActivity$32$$ExternalSyntheticLambda4
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        BaseFragment baseFragment = (BaseFragment) obj;
                        Long l = (Long) obj2;
                        runnable.run();
                    }
                });
                DialogsActivity.this.presentFragment(channelCreateActivity);
                baseFragmentArr[1] = channelCreateActivity;
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$3(long j, final BaseFragment[] baseFragmentArr, Runnable runnable) {
            DialogsActivity.this.showSendToBotAlert(DialogsActivity.this.getMessagesController().getChat(Long.valueOf(j)), runnable, new Runnable() { // from class: org.telegram.ui.DialogsActivity$32$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.32.this.lambda$didFinishChatCreation$2(baseFragmentArr);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$2(BaseFragment[] baseFragmentArr) {
            DialogsActivity.this.removeSelfFromStack();
            if (baseFragmentArr[1] != null) {
                baseFragmentArr[0].removeSelfFromStack();
                baseFragmentArr[1].finishFragment();
                return;
            }
            baseFragmentArr[0].finishFragment();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$5(AlertDialog alertDialog, long j, final Runnable runnable) {
            alertDialog.showDelayed(150L);
            if (DialogsActivity.this.requestPeerType.bot_participant != null && DialogsActivity.this.requestPeerType.bot_participant.booleanValue()) {
                DialogsActivity.this.getMessagesController().addUserToChat(j, DialogsActivity.this.getMessagesController().getUser(Long.valueOf(DialogsActivity.this.requestPeerBotId)), 0, null, DialogsActivity.this, false, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$32$$ExternalSyntheticLambda2
                    @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                    public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                        boolean run;
                        run = runnable.run();
                        return run;
                    }
                });
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$7(long j, final Runnable runnable) {
            if (DialogsActivity.this.requestPeerType.bot_admin_rights != null) {
                TLRPC$User user = DialogsActivity.this.getMessagesController().getUser(Long.valueOf(DialogsActivity.this.requestPeerBotId));
                MessagesController messagesController = DialogsActivity.this.getMessagesController();
                TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = DialogsActivity.this.requestPeerType.bot_admin_rights;
                DialogsActivity dialogsActivity = DialogsActivity.this;
                messagesController.setUserAdminRole(j, user, tLRPC$TL_chatAdminRights, null, false, dialogsActivity, dialogsActivity.requestPeerType.bot_participant == null || !DialogsActivity.this.requestPeerType.bot_participant.booleanValue(), true, null, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$32$$ExternalSyntheticLambda1
                    @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                    public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                        boolean run;
                        run = runnable.run();
                        return run;
                    }
                });
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$9(long j, final Runnable runnable) {
            if (DialogsActivity.this.requestPeerType.user_admin_rights != null) {
                DialogsActivity.this.getMessagesController().setUserAdminRole(j, DialogsActivity.this.getAccountInstance().getUserConfig().getCurrentUser(), ChatRightsEditActivity.rightsOR(DialogsActivity.this.getMessagesController().getChat(Long.valueOf(j)).admin_rights, DialogsActivity.this.requestPeerType.user_admin_rights), null, false, DialogsActivity.this, false, true, null, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$32$$ExternalSyntheticLambda3
                    @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                    public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                        boolean run;
                        run = runnable.run();
                        return run;
                    }
                });
                return;
            }
            runnable.run();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didFinishChatCreation$10(AlertDialog alertDialog, long j, BaseFragment[] baseFragmentArr, Runnable runnable) {
            alertDialog.dismiss();
            DialogsActivity.this.getMessagesController().loadChannelParticipants(Long.valueOf(j));
            DialogsActivityDelegate dialogsActivityDelegate = DialogsActivity.this.delegate;
            DialogsActivity.this.removeSelfFromStack();
            if (baseFragmentArr[1] != null) {
                baseFragmentArr[0].removeSelfFromStack();
                baseFragmentArr[1].finishFragment();
            } else {
                baseFragmentArr[0].finishFragment();
            }
            if (dialogsActivityDelegate != null) {
                ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
                arrayList.add(MessagesStorage.TopicKey.of(-j, 0));
                dialogsActivityDelegate.didSelectDialogs(DialogsActivity.this, arrayList, null, false, null);
            }
        }
    }

    private void updateAppUpdateViews(boolean z) {
        boolean z2;
        if (this.updateLayout == null) {
            return;
        }
        if (SharedConfig.isAppUpdateAvailable()) {
            FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document);
            z2 = getFileLoader().getPathToAttach(SharedConfig.pendingAppUpdate.document, true).exists();
        } else {
            z2 = false;
        }
        if (z2) {
            if (this.updateLayout.getTag() != null) {
                return;
            }
            AnimatorSet animatorSet = this.updateLayoutAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.updateLayout.setVisibility(0);
            this.updateLayout.setTag(1);
            if (z) {
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.updateLayoutAnimator = animatorSet2;
                animatorSet2.setDuration(180L);
                this.updateLayoutAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                this.updateLayoutAnimator.playTogether(ObjectAnimator.ofFloat(this.updateLayout, View.TRANSLATION_Y, 0.0f));
                this.updateLayoutAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.33
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        DialogsActivity.this.updateLayoutAnimator = null;
                    }
                });
                this.updateLayoutAnimator.start();
                return;
            }
            this.updateLayout.setTranslationY(0.0f);
        } else if (this.updateLayout.getTag() == null) {
        } else {
            this.updateLayout.setTag(null);
            if (z) {
                AnimatorSet animatorSet3 = new AnimatorSet();
                this.updateLayoutAnimator = animatorSet3;
                animatorSet3.setDuration(180L);
                this.updateLayoutAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                this.updateLayoutAnimator.playTogether(ObjectAnimator.ofFloat(this.updateLayout, View.TRANSLATION_Y, AndroidUtilities.dp(48.0f)));
                this.updateLayoutAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.34
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (DialogsActivity.this.updateLayout.getTag() == null) {
                            DialogsActivity.this.updateLayout.setVisibility(4);
                        }
                        DialogsActivity.this.updateLayoutAnimator = null;
                    }
                });
                this.updateLayoutAnimator.start();
                return;
            }
            this.updateLayout.setTranslationY(AndroidUtilities.dp(48.0f));
            this.updateLayout.setVisibility(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateContextViewPosition() {
        FilterTabsView filterTabsView = this.filterTabsView;
        float f = 0.0f;
        float measuredHeight = (filterTabsView == null || filterTabsView.getVisibility() == 8) ? 0.0f : this.filterTabsView.getMeasuredHeight();
        ViewPagerFixed.TabsView tabsView = this.searchTabsView;
        float measuredHeight2 = (tabsView == null || tabsView.getVisibility() == 8) ? 0.0f : this.searchTabsView.getMeasuredHeight();
        if (this.fragmentContextView != null) {
            FragmentContextView fragmentContextView = this.fragmentLocationContextView;
            float dp = (fragmentContextView == null || fragmentContextView.getVisibility() != 0) ? 0.0f : AndroidUtilities.dp(36.0f) + 0.0f;
            FragmentContextView fragmentContextView2 = this.fragmentContextView;
            float topPadding = dp + fragmentContextView2.getTopPadding() + this.actionBar.getTranslationY();
            float f2 = this.searchAnimationProgress;
            fragmentContextView2.setTranslationY(topPadding + ((1.0f - f2) * measuredHeight) + (f2 * measuredHeight2) + this.tabsYOffset);
        }
        if (this.fragmentLocationContextView != null) {
            FragmentContextView fragmentContextView3 = this.fragmentContextView;
            if (fragmentContextView3 != null && fragmentContextView3.getVisibility() == 0) {
                f = 0.0f + AndroidUtilities.dp(this.fragmentContextView.getStyleHeight()) + this.fragmentContextView.getTopPadding();
            }
            FragmentContextView fragmentContextView4 = this.fragmentLocationContextView;
            float topPadding2 = f + fragmentContextView4.getTopPadding() + this.actionBar.getTranslationY();
            float f3 = this.searchAnimationProgress;
            fragmentContextView4.setTranslationY(topPadding2 + (measuredHeight * (1.0f - f3)) + (measuredHeight2 * f3) + this.tabsYOffset);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:58:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00b2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateFiltersView(boolean z, ArrayList<Object> arrayList, ArrayList<FiltersView.DateData> arrayList2, boolean z2, boolean z3) {
        boolean z4;
        ViewPagerFixed.TabsView tabsView;
        if (!this.searchIsShowed || this.onlySelect) {
            return;
        }
        ArrayList<FiltersView.MediaFilterData> currentSearchFilters = this.searchViewPager.getCurrentSearchFilters();
        boolean z5 = false;
        boolean z6 = false;
        boolean z7 = false;
        boolean z8 = false;
        for (int i = 0; i < currentSearchFilters.size(); i++) {
            if (currentSearchFilters.get(i).isMedia()) {
                z6 = true;
            } else if (currentSearchFilters.get(i).filterType == 4) {
                z7 = true;
            } else if (currentSearchFilters.get(i).filterType == 6) {
                z8 = true;
            } else if (currentSearchFilters.get(i).filterType == 7) {
                z5 = true;
            }
        }
        if (z5) {
            z2 = false;
        }
        boolean z9 = !(arrayList == null || arrayList.isEmpty()) || !(arrayList2 == null || arrayList2.isEmpty()) || z2;
        if ((z6 || z9 || !z) && z9) {
            arrayList = (arrayList == null || arrayList.isEmpty() || z7) ? null : null;
            arrayList2 = (arrayList2 == null || arrayList2.isEmpty() || z8) ? null : null;
            if (arrayList != null || arrayList2 != null || z2) {
                this.filtersView.setUsersAndDates(arrayList, arrayList2, z2);
                z4 = true;
                if (!z4) {
                    this.filtersView.setUsersAndDates(null, null, false);
                }
                if (!z3) {
                    this.filtersView.getAdapter().notifyDataSetChanged();
                }
                tabsView = this.searchTabsView;
                if (tabsView != null) {
                    tabsView.hide(z4, true);
                }
                this.filtersView.setEnabled(z4);
                this.filtersView.setVisibility(0);
            }
        }
        z4 = false;
        if (!z4) {
        }
        if (!z3) {
        }
        tabsView = this.searchTabsView;
        if (tabsView != null) {
        }
        this.filtersView.setEnabled(z4);
        this.filtersView.setVisibility(0);
    }

    private void addSearchFilter(FiltersView.MediaFilterData mediaFilterData) {
        if (this.searchIsShowed) {
            ArrayList<FiltersView.MediaFilterData> currentSearchFilters = this.searchViewPager.getCurrentSearchFilters();
            if (!currentSearchFilters.isEmpty()) {
                for (int i = 0; i < currentSearchFilters.size(); i++) {
                    if (mediaFilterData.isSameType(currentSearchFilters.get(i))) {
                        return;
                    }
                }
            }
            currentSearchFilters.add(mediaFilterData);
            this.actionBar.setSearchFilter(mediaFilterData);
            this.actionBar.setSearchFieldText("");
            updateFiltersView(true, null, null, false, true);
        }
    }

    public void updateSpeedItem(boolean z) {
        boolean z2;
        if (this.speedItem == null) {
            return;
        }
        Iterator<MessageObject> it = getDownloadController().downloadingFiles.iterator();
        while (true) {
            if (!it.hasNext()) {
                z2 = false;
                break;
            }
            MessageObject next = it.next();
            if (next.getDocument() != null && next.getDocument().size >= 157286400) {
                z2 = true;
                break;
            }
        }
        Iterator<MessageObject> it2 = getDownloadController().recentDownloadingFiles.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            MessageObject next2 = it2.next();
            if (next2.getDocument() != null && next2.getDocument().size >= 157286400) {
                z2 = true;
                break;
            }
        }
        final boolean z3 = !getUserConfig().isPremium() && !getMessagesController().premiumLocked && z2 && z;
        if (z3 != (this.speedItem.getTag() != null)) {
            this.speedItem.setTag(z3 ? Boolean.TRUE : null);
            this.speedItem.setClickable(z3);
            AnimatorSet animatorSet = this.speedAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            animatorSet2.setDuration(180L);
            Animator[] animatorArr = new Animator[3];
            ActionBarMenuItem actionBarMenuItem = this.speedItem;
            Property property = View.ALPHA;
            float[] fArr = new float[1];
            fArr[0] = z3 ? 1.0f : 0.0f;
            animatorArr[0] = ObjectAnimator.ofFloat(actionBarMenuItem, property, fArr);
            ActionBarMenuItem actionBarMenuItem2 = this.speedItem;
            Property property2 = View.SCALE_X;
            float[] fArr2 = new float[1];
            fArr2[0] = z3 ? 1.0f : 0.5f;
            animatorArr[1] = ObjectAnimator.ofFloat(actionBarMenuItem2, property2, fArr2);
            ActionBarMenuItem actionBarMenuItem3 = this.speedItem;
            Property property3 = View.SCALE_Y;
            float[] fArr3 = new float[1];
            fArr3[0] = z3 ? 1.0f : 0.5f;
            animatorArr[2] = ObjectAnimator.ofFloat(actionBarMenuItem3, property3, fArr3);
            animatorSet2.playTogether(animatorArr);
            animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.35
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    int i = Build.VERSION.SDK_INT;
                    if (i >= 21) {
                        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) DialogsActivity.this.speedItem.getIconView().getDrawable();
                        if (!z3) {
                            if (i >= 23) {
                                animatedVectorDrawable.reset();
                                return;
                            } else {
                                animatedVectorDrawable.setVisible(false, true);
                                return;
                            }
                        }
                        animatedVectorDrawable.start();
                        if (SharedConfig.getDevicePerformanceClass() != 0) {
                            TLRPC$TL_help_premiumPromo premiumPromo = MediaDataController.getInstance(((BaseFragment) DialogsActivity.this).currentAccount).getPremiumPromo();
                            String featureTypeToServerString = PremiumPreviewFragment.featureTypeToServerString(2);
                            if (premiumPromo != null) {
                                int i2 = 0;
                                while (true) {
                                    if (i2 >= premiumPromo.video_sections.size()) {
                                        i2 = -1;
                                        break;
                                    } else if (premiumPromo.video_sections.get(i2).equals(featureTypeToServerString)) {
                                        break;
                                    } else {
                                        i2++;
                                    }
                                }
                                if (i2 != -1) {
                                    FileLoader.getInstance(((BaseFragment) DialogsActivity.this).currentAccount).loadFile(premiumPromo.videos.get(i2), premiumPromo, 3, 0);
                                }
                            }
                        }
                    }
                }
            });
            animatorSet2.start();
            this.speedAnimator = animatorSet2;
        }
    }

    private void createActionMode(String str) {
        if (this.actionBar.actionModeIsExist(str)) {
            return;
        }
        ActionBarMenu createActionMode = this.actionBar.createActionMode(false, str);
        createActionMode.setBackgroundColor(0);
        createActionMode.drawBlur = false;
        NumberTextView numberTextView = new NumberTextView(createActionMode.getContext());
        this.selectedDialogsCountTextView = numberTextView;
        numberTextView.setTextSize(18);
        this.selectedDialogsCountTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.selectedDialogsCountTextView.setTextColor(Theme.getColor("actionBarActionModeDefaultIcon"));
        createActionMode.addView(this.selectedDialogsCountTextView, LayoutHelper.createLinear(0, -1, 1.0f, 72, 0, 0, 0));
        this.selectedDialogsCountTextView.setOnTouchListener(DialogsActivity$$ExternalSyntheticLambda38.INSTANCE);
        int i = R.drawable.msg_pin;
        this.pinItem = createActionMode.addItemWithWidth(100, i, AndroidUtilities.dp(54.0f));
        this.muteItem = createActionMode.addItemWithWidth(104, R.drawable.msg_mute, AndroidUtilities.dp(54.0f));
        int i2 = R.drawable.msg_archive;
        this.archive2Item = createActionMode.addItemWithWidth(107, i2, AndroidUtilities.dp(54.0f));
        this.deleteItem = createActionMode.addItemWithWidth(102, R.drawable.msg_delete, AndroidUtilities.dp(54.0f), LocaleController.getString("Delete", R.string.Delete));
        ActionBarMenuItem addItemWithWidth = createActionMode.addItemWithWidth(0, R.drawable.ic_ab_other, AndroidUtilities.dp(54.0f), LocaleController.getString("AccDescrMoreOptions", R.string.AccDescrMoreOptions));
        this.archiveItem = addItemWithWidth.addSubItem(105, i2, LocaleController.getString("Archive", R.string.Archive));
        this.pin2Item = addItemWithWidth.addSubItem(108, i, LocaleController.getString("DialogPin", R.string.DialogPin));
        this.addToFolderItem = addItemWithWidth.addSubItem(109, R.drawable.msg_addfolder, LocaleController.getString("FilterAddTo", R.string.FilterAddTo));
        this.removeFromFolderItem = addItemWithWidth.addSubItem(110, R.drawable.msg_removefolder, LocaleController.getString("FilterRemoveFrom", R.string.FilterRemoveFrom));
        this.readItem = addItemWithWidth.addSubItem(FileLoader.MEDIA_DIR_VIDEO_PUBLIC, R.drawable.msg_markread, LocaleController.getString("MarkAsRead", R.string.MarkAsRead));
        this.clearItem = addItemWithWidth.addSubItem(103, R.drawable.msg_clear, LocaleController.getString("ClearHistory", R.string.ClearHistory));
        this.blockItem = addItemWithWidth.addSubItem(106, R.drawable.msg_block, LocaleController.getString("BlockUser", R.string.BlockUser));
        this.muteItem.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda36
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$createActionMode$34;
                lambda$createActionMode$34 = DialogsActivity.this.lambda$createActionMode$34(view);
                return lambda$createActionMode$34;
            }
        });
        this.actionModeViews.add(this.pinItem);
        this.actionModeViews.add(this.archive2Item);
        this.actionModeViews.add(this.muteItem);
        this.actionModeViews.add(this.deleteItem);
        this.actionModeViews.add(addItemWithWidth);
        if (str == null) {
            this.actionBar.setActionBarMenuOnItemClick(new 36());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createActionMode$34(View view) {
        performSelectedDialogsAction(this.selectedDialogs, 104, true, true);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 36 extends ActionBar.ActionBarMenuOnItemClick {
        36() {
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if ((i == 201 || i == 200 || i == 202 || i == 203) && DialogsActivity.this.searchViewPager != null) {
                DialogsActivity.this.searchViewPager.onActionBarItemClick(i);
            } else if (i == -1) {
                RightSlidingDialogContainer rightSlidingDialogContainer = DialogsActivity.this.rightSlidingDialogContainer;
                if (rightSlidingDialogContainer == null || !rightSlidingDialogContainer.hasFragment()) {
                    if (DialogsActivity.this.filterTabsView == null || !DialogsActivity.this.filterTabsView.isEditing()) {
                        if (((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed()) {
                            if (DialogsActivity.this.searchViewPager == null || DialogsActivity.this.searchViewPager.getVisibility() != 0 || !DialogsActivity.this.searchViewPager.actionModeShowing()) {
                                DialogsActivity.this.hideActionMode(true);
                                return;
                            } else {
                                DialogsActivity.this.searchViewPager.hideActionMode();
                                return;
                            }
                        } else if (!DialogsActivity.this.onlySelect && DialogsActivity.this.folderId == 0) {
                            if (((BaseFragment) DialogsActivity.this).parentLayout != null) {
                                ((BaseFragment) DialogsActivity.this).parentLayout.getDrawerLayoutContainer().openDrawer(false);
                                return;
                            }
                            return;
                        } else {
                            DialogsActivity.this.finishFragment();
                            return;
                        }
                    }
                    DialogsActivity.this.filterTabsView.setIsEditing(false);
                    DialogsActivity.this.showDoneItem(false);
                } else if (((BaseFragment) DialogsActivity.this).actionBar.isActionModeShowed()) {
                    if (DialogsActivity.this.searchViewPager == null || DialogsActivity.this.searchViewPager.getVisibility() != 0 || !DialogsActivity.this.searchViewPager.actionModeShowing()) {
                        DialogsActivity.this.hideActionMode(true);
                    } else {
                        DialogsActivity.this.searchViewPager.hideActionMode();
                    }
                } else {
                    DialogsActivity.this.rightSlidingDialogContainer.lambda$presentFragment$1();
                    DialogsActivity.this.searchViewPager.updateTabs();
                }
            } else if (i == 1) {
                if (DialogsActivity.this.getParentActivity() == null) {
                    return;
                }
                SharedConfig.appLocked = true;
                SharedConfig.saveConfig();
                int[] iArr = new int[2];
                DialogsActivity.this.passcodeItem.getLocationInWindow(iArr);
                ((LaunchActivity) DialogsActivity.this.getParentActivity()).showPasscodeActivity(false, true, iArr[0] + (DialogsActivity.this.passcodeItem.getMeasuredWidth() / 2), iArr[1] + (DialogsActivity.this.passcodeItem.getMeasuredHeight() / 2), new Runnable() { // from class: org.telegram.ui.DialogsActivity$36$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.36.this.lambda$onItemClick$0();
                    }
                }, new Runnable() { // from class: org.telegram.ui.DialogsActivity$36$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.36.this.lambda$onItemClick$1();
                    }
                });
                DialogsActivity.this.getNotificationsController().showNotifications();
                DialogsActivity.this.updatePasscodeButton();
            } else if (i == 2) {
                DialogsActivity.this.presentFragment(new ProxyListActivity());
            } else if (i == 3) {
                DialogsActivity.this.showSearch(true, true, true);
                ((BaseFragment) DialogsActivity.this).actionBar.openSearchField(true);
            } else if (i >= 10 && i < 14) {
                if (DialogsActivity.this.getParentActivity() == null) {
                    return;
                }
                DialogsActivityDelegate dialogsActivityDelegate = DialogsActivity.this.delegate;
                LaunchActivity launchActivity = (LaunchActivity) DialogsActivity.this.getParentActivity();
                launchActivity.switchToAccount(i - 10, true);
                DialogsActivity dialogsActivity = new DialogsActivity(((BaseFragment) DialogsActivity.this).arguments);
                dialogsActivity.setDelegate(dialogsActivityDelegate);
                launchActivity.presentFragment(dialogsActivity, false, true);
            } else if (i == 109) {
                DialogsActivity dialogsActivity2 = DialogsActivity.this;
                FiltersListBottomSheet filtersListBottomSheet = new FiltersListBottomSheet(dialogsActivity2, dialogsActivity2.selectedDialogs);
                filtersListBottomSheet.setDelegate(new FiltersListBottomSheet.FiltersListBottomSheetDelegate() { // from class: org.telegram.ui.DialogsActivity$36$$ExternalSyntheticLambda2
                    @Override // org.telegram.ui.Components.FiltersListBottomSheet.FiltersListBottomSheetDelegate
                    public final void didSelectFilter(MessagesController.DialogFilter dialogFilter) {
                        DialogsActivity.36.this.lambda$onItemClick$2(dialogFilter);
                    }
                });
                DialogsActivity.this.showDialog(filtersListBottomSheet);
            } else if (i != 110) {
                if (i == 100 || i == 101 || i == 102 || i == 103 || i == 104 || i == 105 || i == 106 || i == 107 || i == 108) {
                    DialogsActivity dialogsActivity3 = DialogsActivity.this;
                    dialogsActivity3.performSelectedDialogsAction(dialogsActivity3.selectedDialogs, i, true, false);
                }
            } else {
                MessagesController.DialogFilter dialogFilter = DialogsActivity.this.getMessagesController().dialogFilters.get(DialogsActivity.this.viewPages[0].selectedType);
                DialogsActivity dialogsActivity4 = DialogsActivity.this;
                ArrayList<Long> dialogsCount = FiltersListBottomSheet.getDialogsCount(dialogsActivity4, dialogFilter, dialogsActivity4.selectedDialogs, false, false);
                if ((dialogFilter != null ? dialogFilter.neverShow.size() : 0) + dialogsCount.size() > 100) {
                    DialogsActivity dialogsActivity5 = DialogsActivity.this;
                    dialogsActivity5.showDialog(AlertsCreator.createSimpleAlert(dialogsActivity5.getParentActivity(), LocaleController.getString("FilterAddToAlertFullTitle", R.string.FilterAddToAlertFullTitle), LocaleController.getString("FilterAddToAlertFullText", R.string.FilterAddToAlertFullText)).create());
                    return;
                }
                if (!dialogsCount.isEmpty()) {
                    dialogFilter.neverShow.addAll(dialogsCount);
                    for (int i2 = 0; i2 < dialogsCount.size(); i2++) {
                        Long l = dialogsCount.get(i2);
                        dialogFilter.alwaysShow.remove(l);
                        dialogFilter.pinnedDialogs.delete(l.longValue());
                    }
                    FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, false, false, DialogsActivity.this, null);
                }
                DialogsActivity.this.getUndoView().showWithAction(dialogsCount.size() == 1 ? dialogsCount.get(0).longValue() : 0L, 21, Integer.valueOf(dialogsCount.size()), dialogFilter, (Runnable) null, (Runnable) null);
                DialogsActivity.this.hideActionMode(false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0() {
            DialogsActivity.this.passcodeItem.setAlpha(1.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$1() {
            DialogsActivity.this.passcodeItem.setAlpha(0.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$2(MessagesController.DialogFilter dialogFilter) {
            ArrayList<Long> arrayList;
            ArrayList<Long> arrayList2;
            long j;
            DialogsActivity dialogsActivity = DialogsActivity.this;
            ArrayList<Long> dialogsCount = FiltersListBottomSheet.getDialogsCount(dialogsActivity, dialogFilter, dialogsActivity.selectedDialogs, true, false);
            int size = (dialogFilter != null ? dialogFilter.alwaysShow.size() : 0) + dialogsCount.size();
            if ((size <= DialogsActivity.this.getMessagesController().dialogFiltersChatsLimitDefault || DialogsActivity.this.getUserConfig().isPremium()) && size <= DialogsActivity.this.getMessagesController().dialogFiltersChatsLimitPremium) {
                if (dialogFilter != null) {
                    if (dialogsCount.isEmpty()) {
                        arrayList = dialogsCount;
                    } else {
                        for (int i = 0; i < dialogsCount.size(); i++) {
                            dialogFilter.neverShow.remove(dialogsCount.get(i));
                        }
                        dialogFilter.alwaysShow.addAll(dialogsCount);
                        arrayList = dialogsCount;
                        FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, true, false, DialogsActivity.this, null);
                    }
                    if (arrayList.size() == 1) {
                        arrayList2 = arrayList;
                        j = arrayList2.get(0).longValue();
                    } else {
                        arrayList2 = arrayList;
                        j = 0;
                    }
                    DialogsActivity.this.getUndoView().showWithAction(j, 20, Integer.valueOf(arrayList2.size()), dialogFilter, (Runnable) null, (Runnable) null);
                } else {
                    DialogsActivity.this.presentFragment(new FilterCreateActivity(null, dialogsCount));
                }
                DialogsActivity.this.hideActionMode(true);
                return;
            }
            DialogsActivity dialogsActivity2 = DialogsActivity.this;
            DialogsActivity dialogsActivity3 = DialogsActivity.this;
            dialogsActivity2.showDialog(new LimitReachedBottomSheet(dialogsActivity3, ((BaseFragment) dialogsActivity3).fragmentView.getContext(), 4, ((BaseFragment) DialogsActivity.this).currentAccount));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchToCurrentSelectedMode(boolean z) {
        ViewPage[] viewPageArr;
        int i = 0;
        int i2 = 0;
        while (true) {
            viewPageArr = this.viewPages;
            if (i2 >= viewPageArr.length) {
                break;
            }
            viewPageArr[i2].listView.stopScroll();
            i2++;
        }
        viewPageArr[z ? 1 : 0].listView.getAdapter();
        if (this.viewPages[z ? 1 : 0].selectedType < 0 || this.viewPages[z ? 1 : 0].selectedType >= getMessagesController().dialogFilters.size()) {
            return;
        }
        MessagesController.DialogFilter dialogFilter = getMessagesController().dialogFilters.get(this.viewPages[z ? 1 : 0].selectedType);
        if (dialogFilter.isDefault()) {
            this.viewPages[z ? 1 : 0].dialogsType = 0;
            this.viewPages[z ? 1 : 0].listView.updatePullState();
        } else {
            if (this.viewPages[!z ? 1 : 0].dialogsType == 7) {
                this.viewPages[z ? 1 : 0].dialogsType = 8;
            } else {
                this.viewPages[z ? 1 : 0].dialogsType = 7;
            }
            this.viewPages[z ? 1 : 0].listView.setScrollEnabled(true);
            getMessagesController().selectDialogFilter(dialogFilter, this.viewPages[z ? 1 : 0].dialogsType == 8 ? 1 : 0);
        }
        this.viewPages[1].isLocked = dialogFilter.locked;
        this.viewPages[z ? 1 : 0].dialogsAdapter.setDialogsType(this.viewPages[z ? 1 : 0].dialogsType);
        LinearLayoutManager linearLayoutManager = this.viewPages[z ? 1 : 0].layoutManager;
        if (this.viewPages[z ? 1 : 0].dialogsType == 0 && hasHiddenArchive() && this.viewPages[z ? 1 : 0].archivePullViewState == 2) {
            i = 1;
        }
        linearLayoutManager.scrollToPositionWithOffset(i, (int) this.actionBar.getTranslationY());
        checkListLoad(this.viewPages[z ? 1 : 0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showScrollbars(boolean z) {
        if (this.viewPages == null || this.scrollBarVisible == z) {
            return;
        }
        this.scrollBarVisible = z;
        int i = 0;
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                return;
            }
            if (z) {
                viewPageArr[i].listView.setScrollbarFadingEnabled(false);
            }
            this.viewPages[i].listView.setVerticalScrollBarEnabled(z);
            if (z) {
                this.viewPages[i].listView.setScrollbarFadingEnabled(true);
            }
            i++;
        }
    }

    private void updateFilterTabs(boolean z, boolean z2) {
        boolean z3;
        int i;
        int i2;
        if (this.filterTabsView == null || this.inPreviewMode || this.searchIsShowed) {
            return;
        }
        ActionBarPopupWindow actionBarPopupWindow = this.scrimPopupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
            this.scrimPopupWindow = null;
        }
        ArrayList<MessagesController.DialogFilter> arrayList = getMessagesController().dialogFilters;
        MessagesController.getMainSettings(this.currentAccount);
        if (arrayList.size() > 1) {
            if (z || this.filterTabsView.getVisibility() != 0) {
                boolean z4 = this.filterTabsView.getVisibility() != 0 ? false : z2;
                this.canShowFilterTabsView = true;
                boolean isEmpty = this.filterTabsView.isEmpty();
                updateFilterTabsVisibility(z2);
                int currentTabId = this.filterTabsView.getCurrentTabId();
                int currentTabStableId = this.filterTabsView.getCurrentTabStableId();
                if (currentTabId == this.filterTabsView.getDefaultTabId() || currentTabId < arrayList.size()) {
                    z3 = false;
                } else {
                    this.filterTabsView.resetTabId();
                    z3 = true;
                }
                this.filterTabsView.removeTabs();
                int size = arrayList.size();
                int i3 = 0;
                while (i3 < size) {
                    if (arrayList.get(i3).isDefault()) {
                        i2 = i3;
                        this.filterTabsView.addTab(i3, 0, LocaleController.getString("FilterAllChats", R.string.FilterAllChats), true, arrayList.get(i3).locked);
                    } else {
                        i2 = i3;
                        this.filterTabsView.addTab(i2, arrayList.get(i2).localId, arrayList.get(i2).name, false, arrayList.get(i2).locked);
                    }
                    i3 = i2 + 1;
                }
                if (currentTabStableId >= 0) {
                    if (this.filterTabsView.getStableId(this.viewPages[0].selectedType) != currentTabStableId) {
                        this.viewPages[0].selectedType = currentTabId;
                        isEmpty = true;
                    }
                    if (z3) {
                        this.filterTabsView.selectTabWithStableId(currentTabStableId);
                    }
                }
                int i4 = 0;
                while (true) {
                    ViewPage[] viewPageArr = this.viewPages;
                    if (i4 >= viewPageArr.length) {
                        break;
                    }
                    if (viewPageArr[i4].selectedType >= arrayList.size()) {
                        i = 1;
                        this.viewPages[i4].selectedType = arrayList.size() - 1;
                    } else {
                        i = 1;
                    }
                    this.viewPages[i4].listView.setScrollingTouchSlop(i);
                    i4++;
                }
                this.filterTabsView.finishAddingTabs(z4);
                if (isEmpty) {
                    switchToCurrentSelectedMode(false);
                }
                this.isFirstTab = currentTabId == this.filterTabsView.getFirstTabId();
                updateDrawerSwipeEnabled();
                FilterTabsView filterTabsView = this.filterTabsView;
                if (filterTabsView.isLocked(filterTabsView.getCurrentTabId())) {
                    this.filterTabsView.selectFirstTab();
                }
            }
        } else {
            if (this.filterTabsView.getVisibility() != 8) {
                this.filterTabsView.setIsEditing(false);
                showDoneItem(false);
                this.maybeStartTracking = false;
                if (this.startedTracking) {
                    this.startedTracking = false;
                    this.viewPages[0].setTranslationX(0.0f);
                    ViewPage[] viewPageArr2 = this.viewPages;
                    viewPageArr2[1].setTranslationX(viewPageArr2[0].getMeasuredWidth());
                }
                if (this.viewPages[0].selectedType != this.filterTabsView.getDefaultTabId()) {
                    this.viewPages[0].selectedType = this.filterTabsView.getDefaultTabId();
                    this.viewPages[0].dialogsAdapter.setDialogsType(0);
                    this.viewPages[0].dialogsType = 0;
                    this.viewPages[0].dialogsAdapter.notifyDataSetChanged();
                }
                this.viewPages[1].setVisibility(8);
                this.viewPages[1].selectedType = 0;
                this.viewPages[1].dialogsAdapter.setDialogsType(0);
                this.viewPages[1].dialogsType = 0;
                this.viewPages[1].dialogsAdapter.notifyDataSetChanged();
                this.canShowFilterTabsView = false;
                updateFilterTabsVisibility(z2);
                int i5 = 0;
                while (true) {
                    ViewPage[] viewPageArr3 = this.viewPages;
                    if (i5 >= viewPageArr3.length) {
                        break;
                    }
                    if (viewPageArr3[i5].dialogsType == 0 && this.viewPages[i5].archivePullViewState == 2 && hasHiddenArchive()) {
                        int findFirstVisibleItemPosition = this.viewPages[i5].layoutManager.findFirstVisibleItemPosition();
                        if (findFirstVisibleItemPosition == 0 || findFirstVisibleItemPosition == 1) {
                            this.viewPages[i5].layoutManager.scrollToPositionWithOffset(1, (int) this.actionBar.getTranslationY());
                        }
                    }
                    this.viewPages[i5].listView.setScrollingTouchSlop(0);
                    this.viewPages[i5].listView.requestLayout();
                    this.viewPages[i5].requestLayout();
                    i5++;
                }
            }
            updateDrawerSwipeEnabled();
        }
        updateCounters(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDrawerSwipeEnabled() {
        RightSlidingDialogContainer rightSlidingDialogContainer;
        INavigationLayout iNavigationLayout = this.parentLayout;
        if (iNavigationLayout == null || iNavigationLayout.getDrawerLayoutContainer() == null) {
            return;
        }
        this.parentLayout.getDrawerLayoutContainer().setAllowOpenDrawerBySwipe(((this.isFirstTab && SharedConfig.getChatSwipeAction(this.currentAccount) == 5) || SharedConfig.getChatSwipeAction(this.currentAccount) != 5) && !this.searchIsShowed && ((rightSlidingDialogContainer = this.rightSlidingDialogContainer) == null || !rightSlidingDialogContainer.hasFragment()));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void finishFragment() {
        super.finishFragment();
        ActionBarPopupWindow actionBarPopupWindow = this.scrimPopupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        int i;
        View view;
        super.onResume();
        RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer != null) {
            rightSlidingDialogContainer.onResume();
        }
        if (!this.parentLayout.isInPreviewMode() && (view = this.blurredView) != null && view.getVisibility() == 0) {
            this.blurredView.setVisibility(8);
            this.blurredView.setBackground(null);
        }
        updateDrawerSwipeEnabled();
        if (this.viewPages != null) {
            int i2 = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i2 >= viewPageArr.length) {
                    break;
                }
                viewPageArr[i2].dialogsAdapter.notifyDataSetChanged();
                i2++;
            }
        }
        ChatActivityEnterView chatActivityEnterView = this.commentView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onResume();
        }
        if (!this.onlySelect && this.folderId == 0) {
            getMediaDataController().checkStickers(4);
        }
        SearchViewPager searchViewPager = this.searchViewPager;
        if (searchViewPager != null) {
            searchViewPager.onResume();
        }
        if ((this.afterSignup || getUserConfig().unacceptedTermsOfService == null) && this.checkPermission && !this.onlySelect && (i = Build.VERSION.SDK_INT) >= 23) {
            final Activity parentActivity = getParentActivity();
            if (parentActivity != null) {
                this.checkPermission = false;
                final boolean z = parentActivity.checkSelfPermission("android.permission.READ_CONTACTS") != 0;
                final boolean z2 = (i <= 28 || BuildVars.NO_SCOPED_STORAGE) && parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda64
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.this.lambda$onResume$36(z, z2, parentActivity);
                    }
                }, (this.afterSignup && z) ? 4000L : 0L);
            }
        } else if (!this.onlySelect && XiaomiUtilities.isMIUI() && Build.VERSION.SDK_INT >= 19 && !XiaomiUtilities.isCustomPermissionGranted(XiaomiUtilities.OP_SHOW_WHEN_LOCKED)) {
            if (getParentActivity() == null || MessagesController.getGlobalNotificationsSettings().getBoolean("askedAboutMiuiLockscreen", false)) {
                return;
            }
            showDialog(new AlertDialog.Builder(getParentActivity()).setTopAnimation(R.raw.permission_request_apk, 72, false, Theme.getColor("dialogTopBackground")).setMessage(LocaleController.getString("PermissionXiaomiLockscreen", R.string.PermissionXiaomiLockscreen)).setPositiveButton(LocaleController.getString("PermissionOpenSettings", R.string.PermissionOpenSettings), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda12
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    DialogsActivity.this.lambda$onResume$37(dialogInterface, i3);
                }
            }).setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", R.string.ContactsPermissionAlertNotNow), DialogsActivity$$ExternalSyntheticLambda18.INSTANCE).create());
        }
        showFiltersHint();
        if (this.viewPages != null) {
            int i3 = 0;
            while (true) {
                ViewPage[] viewPageArr2 = this.viewPages;
                if (i3 >= viewPageArr2.length) {
                    break;
                }
                if (viewPageArr2[i3].dialogsType == 0 && this.viewPages[i3].archivePullViewState == 2 && this.viewPages[i3].layoutManager.findFirstVisibleItemPosition() == 0 && hasHiddenArchive()) {
                    this.viewPages[i3].layoutManager.scrollToPositionWithOffset(1, (int) this.actionBar.getTranslationY());
                }
                if (i3 == 0) {
                    this.viewPages[i3].dialogsAdapter.resume();
                } else {
                    this.viewPages[i3].dialogsAdapter.pause();
                }
                i3++;
            }
        }
        showNextSupportedSuggestion();
        Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.DialogsActivity.37
            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean allowLayoutChanges() {
                return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ int getBottomOffset(int i4) {
                return Bulletin.Delegate.-CC.$default$getBottomOffset(this, i4);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onHide(Bulletin bulletin) {
                Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public void onBottomOffsetChange(float f) {
                if (DialogsActivity.this.undoView[0] == null || DialogsActivity.this.undoView[0].getVisibility() != 0) {
                    DialogsActivity.this.additionalFloatingTranslation = f;
                    if (DialogsActivity.this.additionalFloatingTranslation < 0.0f) {
                        DialogsActivity.this.additionalFloatingTranslation = 0.0f;
                    }
                    if (DialogsActivity.this.floatingHidden) {
                        return;
                    }
                    DialogsActivity.this.updateFloatingButtonOffset();
                }
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public void onShow(Bulletin bulletin) {
                if (DialogsActivity.this.undoView[0] == null || DialogsActivity.this.undoView[0].getVisibility() != 0) {
                    return;
                }
                DialogsActivity.this.undoView[0].hide(true, 2);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public int getTopOffset(int i4) {
                int i5 = 0;
                int measuredHeight = (((BaseFragment) DialogsActivity.this).actionBar != null ? ((BaseFragment) DialogsActivity.this).actionBar.getMeasuredHeight() + ((int) ((BaseFragment) DialogsActivity.this).actionBar.getTranslationY()) : 0) + ((DialogsActivity.this.filterTabsView == null || DialogsActivity.this.filterTabsView.getVisibility() != 0) ? 0 : DialogsActivity.this.filterTabsView.getMeasuredHeight()) + ((DialogsActivity.this.fragmentContextView == null || !DialogsActivity.this.fragmentContextView.isCallTypeVisible()) ? 0 : AndroidUtilities.dp(DialogsActivity.this.fragmentContextView.getStyleHeight()));
                if (DialogsActivity.this.dialogsHintCell != null && DialogsActivity.this.dialogsHintCell.getVisibility() == 0) {
                    i5 = DialogsActivity.this.dialogsHintCell.getHeight();
                }
                return measuredHeight + i5;
            }
        });
        if (this.searchIsShowed) {
            AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        }
        updateVisibleRows(0, false);
        updateProxyButton(false, true);
        checkSuggestClearDatabase();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$36(boolean z, boolean z2, Activity activity) {
        this.afterSignup = false;
        if (z || z2) {
            this.askingForPermissions = true;
            if (z && this.askAboutContacts && getUserConfig().syncContacts && activity.shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS")) {
                AlertDialog create = AlertsCreator.createContactsPermissionDialog(activity, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda73
                    @Override // org.telegram.messenger.MessagesStorage.IntCallback
                    public final void run(int i) {
                        DialogsActivity.this.lambda$onResume$35(i);
                    }
                }).create();
                this.permissionDialog = create;
                showDialog(create);
            } else if (z2 && activity.shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
                if (activity instanceof BasePermissionsActivity) {
                    AlertDialog createPermissionErrorAlert = ((BasePermissionsActivity) activity).createPermissionErrorAlert(R.raw.permission_request_folder, LocaleController.getString(R.string.PermissionStorageWithHint));
                    this.permissionDialog = createPermissionErrorAlert;
                    showDialog(createPermissionErrorAlert);
                }
            } else {
                askForPermissons(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$35(int i) {
        this.askAboutContacts = i != 0;
        MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askAboutContacts", this.askAboutContacts).apply();
        askForPermissons(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onResume$37(DialogInterface dialogInterface, int i) {
        Intent permissionManagerIntent = XiaomiUtilities.getPermissionManagerIntent();
        if (permissionManagerIntent != null) {
            try {
                try {
                    getParentActivity().startActivity(permissionManagerIntent);
                } catch (Exception unused) {
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
                    getParentActivity().startActivity(intent);
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onResume$38(DialogInterface dialogInterface, int i) {
        MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askedAboutMiuiLockscreen", true).commit();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean presentFragment(BaseFragment baseFragment) {
        boolean presentFragment = super.presentFragment(baseFragment);
        if (presentFragment && this.viewPages != null) {
            int i = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i >= viewPageArr.length) {
                    break;
                }
                viewPageArr[i].dialogsAdapter.pause();
                i++;
            }
        }
        return presentFragment;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer != null) {
            rightSlidingDialogContainer.onPause();
        }
        ActionBarPopupWindow actionBarPopupWindow = this.scrimPopupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
        }
        ChatActivityEnterView chatActivityEnterView = this.commentView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.onPause();
        }
        UndoView[] undoViewArr = this.undoView;
        int i = 0;
        if (undoViewArr[0] != null) {
            undoViewArr[0].hide(true, 0);
        }
        Bulletin.removeDelegate(this);
        if (this.viewPages == null) {
            return;
        }
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                return;
            }
            viewPageArr[i].dialogsAdapter.pause();
            i++;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (this.rightSlidingDialogContainer.hasFragment()) {
            if (this.rightSlidingDialogContainer.getFragment().onBackPressed()) {
                this.rightSlidingDialogContainer.lambda$presentFragment$1();
                this.searchViewPager.updateTabs();
            }
            return false;
        }
        ActionBarPopupWindow actionBarPopupWindow = this.scrimPopupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
            return false;
        }
        FilterTabsView filterTabsView = this.filterTabsView;
        if (filterTabsView != null && filterTabsView.isEditing()) {
            this.filterTabsView.setIsEditing(false);
            showDoneItem(false);
            return false;
        }
        ActionBar actionBar = this.actionBar;
        if (actionBar != null && actionBar.isActionModeShowed()) {
            if (this.searchViewPager.getVisibility() == 0) {
                this.searchViewPager.hideActionMode();
                hideActionMode(true);
            } else {
                hideActionMode(true);
            }
            return false;
        }
        FilterTabsView filterTabsView2 = this.filterTabsView;
        if (filterTabsView2 != null && filterTabsView2.getVisibility() == 0 && !this.tabsAnimationInProgress && !this.filterTabsView.isAnimatingIndicator() && !this.startedTracking && !this.filterTabsView.isFirstTabSelected()) {
            this.filterTabsView.selectFirstTab();
            return false;
        }
        ChatActivityEnterView chatActivityEnterView = this.commentView;
        if (chatActivityEnterView != null && chatActivityEnterView.isPopupShowing()) {
            this.commentView.hidePopup(true);
            return false;
        }
        return super.onBackPressed();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyHidden() {
        if (this.closeSearchFieldOnHide) {
            ActionBar actionBar = this.actionBar;
            if (actionBar != null) {
                actionBar.closeSearchField();
            }
            TLObject tLObject = this.searchObject;
            if (tLObject != null) {
                this.searchViewPager.dialogsSearchAdapter.putRecentSearch(this.searchDialogId, tLObject);
                this.searchObject = null;
            }
            this.closeSearchFieldOnHide = false;
        }
        FilterTabsView filterTabsView = this.filterTabsView;
        if (filterTabsView != null && filterTabsView.getVisibility() == 0 && this.filterTabsViewIsVisible) {
            int i = (int) (-this.actionBar.getTranslationY());
            int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
            if (i != 0 && i != currentActionBarHeight) {
                if (i >= currentActionBarHeight / 2) {
                    if (this.viewPages[0].listView.canScrollVertically(1)) {
                        setScrollY(-currentActionBarHeight);
                    }
                } else {
                    setScrollY(0.0f);
                }
            }
        }
        UndoView[] undoViewArr = this.undoView;
        if (undoViewArr[0] != null) {
            undoViewArr[0].hide(true, 0);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyVisible() {
        super.onBecomeFullyVisible();
        if (SharedConfig.useLNavigation) {
            if (this.viewPages != null) {
                int i = 0;
                while (true) {
                    ViewPage[] viewPageArr = this.viewPages;
                    if (i >= viewPageArr.length) {
                        break;
                    }
                    if (viewPageArr[i].dialogsType == 0 && this.viewPages[i].archivePullViewState == 2 && this.viewPages[i].layoutManager.findFirstVisibleItemPosition() == 0 && hasHiddenArchive()) {
                        this.viewPages[i].layoutManager.scrollToPositionWithOffset(1, (int) this.actionBar.getTranslationY());
                    }
                    if (i == 0) {
                        this.viewPages[i].dialogsAdapter.resume();
                    } else {
                        this.viewPages[i].dialogsAdapter.pause();
                    }
                    i++;
                }
            }
            if (this.searchIsShowed) {
                AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
            }
            updateVisibleRows(0, false);
            updateProxyButton(false, true);
            checkSuggestClearDatabase();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void setInPreviewMode(boolean z) {
        super.setInPreviewMode(z);
        if (z || this.avatarContainer == null) {
            return;
        }
        this.actionBar.setBackground(null);
        ((ViewGroup.MarginLayoutParams) this.actionBar.getLayoutParams()).topMargin = 0;
        this.actionBar.removeView(this.avatarContainer);
        this.avatarContainer = null;
        updateFilterTabs(false, false);
        this.floatingButton.setVisibility(0);
        ContentView contentView = (ContentView) this.fragmentView;
        FragmentContextView fragmentContextView = this.fragmentContextView;
        if (fragmentContextView != null) {
            contentView.addView(fragmentContextView);
        }
        FragmentContextView fragmentContextView2 = this.fragmentLocationContextView;
        if (fragmentContextView2 != null) {
            contentView.addView(fragmentContextView2);
        }
    }

    public boolean addOrRemoveSelectedDialog(long j, View view) {
        if (this.onlySelect && getMessagesController().isForum(j)) {
            return false;
        }
        if (this.selectedDialogs.contains(Long.valueOf(j))) {
            this.selectedDialogs.remove(Long.valueOf(j));
            if (view instanceof DialogCell) {
                ((DialogCell) view).setChecked(false, true);
            } else if (view instanceof ProfileSearchCell) {
                ((ProfileSearchCell) view).setChecked(false, true);
            }
            return false;
        }
        this.selectedDialogs.add(Long.valueOf(j));
        if (view instanceof DialogCell) {
            ((DialogCell) view).setChecked(true, true);
        } else if (view instanceof ProfileSearchCell) {
            ((ProfileSearchCell) view).setChecked(true, true);
        }
        return true;
    }

    public void search(String str, boolean z) {
        showSearch(true, false, z);
        this.actionBar.openSearchField(str, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSearch(final boolean z, boolean z2, boolean z3) {
        FilterTabsView filterTabsView;
        RightSlidingDialogContainer rightSlidingDialogContainer;
        int i;
        if (!z) {
            updateSpeedItem(false);
        }
        int i2 = this.initialDialogsType;
        boolean z4 = (i2 == 0 || i2 == 3) ? z3 : false;
        AnimatorSet animatorSet = this.searchAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.searchAnimator = null;
        }
        Animator animator = this.tabsAlphaAnimator;
        if (animator != null) {
            animator.cancel();
            this.tabsAlphaAnimator = null;
        }
        this.searchIsShowed = z;
        ((SizeNotifierFrameLayout) this.fragmentView).invalidateBlur();
        if (z) {
            boolean onlyDialogsAdapter = this.searchFiltersWasShowed ? false : onlyDialogsAdapter();
            this.searchViewPager.showOnlyDialogsAdapter(onlyDialogsAdapter);
            boolean z5 = !onlyDialogsAdapter;
            this.whiteActionBar = z5;
            if (z5) {
                this.searchFiltersWasShowed = true;
            }
            ContentView contentView = (ContentView) this.fragmentView;
            ViewPagerFixed.TabsView tabsView = this.searchTabsView;
            if (tabsView == null && !onlyDialogsAdapter) {
                this.searchTabsView = this.searchViewPager.createTabsView(false, 8);
                if (this.filtersView != null) {
                    i = 0;
                    while (i < contentView.getChildCount()) {
                        if (contentView.getChildAt(i) == this.filtersView) {
                            break;
                        }
                        i++;
                    }
                }
                i = -1;
                if (i > 0) {
                    contentView.addView(this.searchTabsView, i, LayoutHelper.createFrame(-1, 44.0f));
                } else {
                    contentView.addView(this.searchTabsView, LayoutHelper.createFrame(-1, 44.0f));
                }
            } else if (tabsView != null && onlyDialogsAdapter) {
                ViewParent parent = tabsView.getParent();
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(this.searchTabsView);
                }
                this.searchTabsView = null;
            }
            EditTextBoldCursor searchField = this.searchItem.getSearchField();
            if (this.whiteActionBar) {
                searchField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                searchField.setHintTextColor(Theme.getColor("player_time"));
                searchField.setCursorColor(Theme.getColor("chat_messagePanelCursor"));
            } else {
                searchField.setCursorColor(Theme.getColor("actionBarDefaultSearch"));
                searchField.setHintTextColor(Theme.getColor("actionBarDefaultSearchPlaceholder"));
                searchField.setTextColor(Theme.getColor("actionBarDefaultSearch"));
            }
            this.searchViewPager.setKeyboardHeight(((ContentView) this.fragmentView).getKeyboardHeight());
            updateDrawerSwipeEnabled();
            this.searchViewPager.clear();
            if (this.folderId != 0 && ((rightSlidingDialogContainer = this.rightSlidingDialogContainer) == null || !rightSlidingDialogContainer.hasFragment())) {
                addSearchFilter(new FiltersView.MediaFilterData(R.drawable.chats_archive, LocaleController.getString("ArchiveSearchFilter", R.string.ArchiveSearchFilter), null, 7));
            }
        } else {
            updateDrawerSwipeEnabled();
        }
        if (z4 && this.searchViewPager.dialogsSearchAdapter.hasRecentSearch()) {
            AndroidUtilities.setAdjustResizeToNothing(getParentActivity(), this.classGuid);
        } else {
            AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        }
        if (!z && (filterTabsView = this.filterTabsView) != null && this.canShowFilterTabsView) {
            filterTabsView.setVisibility(0);
        }
        boolean z6 = SharedConfig.getDevicePerformanceClass() == 0 || SharedConfig.getLiteMode().enabled();
        if (z4) {
            if (!z) {
                this.viewPages[0].listView.setVisibility(0);
                this.viewPages[0].setVisibility(0);
            } else {
                this.searchViewPager.setVisibility(0);
                this.searchViewPager.reset();
                updateFiltersView(true, null, null, false, false);
                ViewPagerFixed.TabsView tabsView2 = this.searchTabsView;
                if (tabsView2 != null) {
                    tabsView2.hide(false, false);
                    this.searchTabsView.setVisibility(0);
                }
            }
            setDialogsListFrozen(true);
            this.viewPages[0].listView.setVerticalScrollBarEnabled(false);
            this.searchViewPager.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.searchAnimator = new AnimatorSet();
            ArrayList arrayList = new ArrayList();
            ViewPage viewPage = this.viewPages[0];
            Property property = View.ALPHA;
            float[] fArr = new float[1];
            fArr[0] = z ? 0.0f : 1.0f;
            arrayList.add(ObjectAnimator.ofFloat(viewPage, property, fArr));
            if (!z6) {
                ViewPage viewPage2 = this.viewPages[0];
                Property property2 = View.SCALE_X;
                float[] fArr2 = new float[1];
                fArr2[0] = z ? 0.9f : 1.0f;
                arrayList.add(ObjectAnimator.ofFloat(viewPage2, property2, fArr2));
                ViewPage viewPage3 = this.viewPages[0];
                Property property3 = View.SCALE_Y;
                float[] fArr3 = new float[1];
                fArr3[0] = z ? 0.9f : 1.0f;
                arrayList.add(ObjectAnimator.ofFloat(viewPage3, property3, fArr3));
            } else {
                this.viewPages[0].setScaleX(1.0f);
                this.viewPages[0].setScaleY(1.0f);
            }
            RightSlidingDialogContainer rightSlidingDialogContainer2 = this.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer2 != null) {
                rightSlidingDialogContainer2.setVisibility(0);
                RightSlidingDialogContainer rightSlidingDialogContainer3 = this.rightSlidingDialogContainer;
                Property property4 = View.ALPHA;
                float[] fArr4 = new float[1];
                fArr4[0] = z ? 0.0f : 1.0f;
                arrayList.add(ObjectAnimator.ofFloat(rightSlidingDialogContainer3, property4, fArr4));
            }
            SearchViewPager searchViewPager = this.searchViewPager;
            Property property5 = View.ALPHA;
            float[] fArr5 = new float[1];
            fArr5[0] = z ? 1.0f : 0.0f;
            arrayList.add(ObjectAnimator.ofFloat(searchViewPager, property5, fArr5));
            if (!z6) {
                SearchViewPager searchViewPager2 = this.searchViewPager;
                Property property6 = View.SCALE_X;
                float[] fArr6 = new float[1];
                fArr6[0] = z ? 1.0f : 1.05f;
                arrayList.add(ObjectAnimator.ofFloat(searchViewPager2, property6, fArr6));
                SearchViewPager searchViewPager3 = this.searchViewPager;
                Property property7 = View.SCALE_Y;
                float[] fArr7 = new float[1];
                fArr7[0] = z ? 1.0f : 1.05f;
                arrayList.add(ObjectAnimator.ofFloat(searchViewPager3, property7, fArr7));
            } else {
                this.searchViewPager.setScaleX(1.0f);
                this.searchViewPager.setScaleY(1.0f);
            }
            ActionBarMenuItem actionBarMenuItem = this.passcodeItem;
            if (actionBarMenuItem != null) {
                RLottieImageView iconView = actionBarMenuItem.getIconView();
                Property property8 = View.ALPHA;
                float[] fArr8 = new float[1];
                fArr8[0] = z ? 0.0f : 1.0f;
                arrayList.add(ObjectAnimator.ofFloat(iconView, property8, fArr8));
            }
            ActionBarMenuItem actionBarMenuItem2 = this.downloadsItem;
            if (actionBarMenuItem2 != null) {
                if (z) {
                    actionBarMenuItem2.setAlpha(0.0f);
                } else {
                    arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem2, View.ALPHA, 1.0f));
                }
                updateProxyButton(false, false);
            }
            FilterTabsView filterTabsView2 = this.filterTabsView;
            if (filterTabsView2 != null && filterTabsView2.getVisibility() == 0) {
                RecyclerListView tabsContainer = this.filterTabsView.getTabsContainer();
                Property property9 = View.ALPHA;
                float[] fArr9 = new float[1];
                fArr9[0] = z ? 0.0f : 1.0f;
                ObjectAnimator duration = ObjectAnimator.ofFloat(tabsContainer, property9, fArr9).setDuration(100L);
                this.tabsAlphaAnimator = duration;
                duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.38
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator2) {
                        DialogsActivity.this.tabsAlphaAnimator = null;
                    }
                });
            }
            float[] fArr10 = new float[2];
            fArr10[0] = this.searchAnimationProgress;
            fArr10[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr10);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    DialogsActivity.this.lambda$showSearch$39(valueAnimator);
                }
            });
            arrayList.add(ofFloat);
            this.searchAnimator.playTogether(arrayList);
            this.searchAnimator.setDuration(z ? 200L : 180L);
            this.searchAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            if (this.filterTabsViewIsVisible) {
                int color = Theme.getColor(this.folderId == 0 ? "actionBarDefault" : "actionBarDefaultArchived");
                int color2 = Theme.getColor("windowBackgroundWhite");
                this.searchAnimationTabsDelayedCrossfade = ((float) ((Math.abs(Color.red(color) - Color.red(color2)) + Math.abs(Color.green(color) - Color.green(color2))) + Math.abs(Color.blue(color) - Color.blue(color2)))) / 255.0f > 0.3f;
            } else {
                this.searchAnimationTabsDelayedCrossfade = true;
            }
            if (!z) {
                this.searchAnimator.setStartDelay(20L);
                Animator animator2 = this.tabsAlphaAnimator;
                if (animator2 != null) {
                    if (this.searchAnimationTabsDelayedCrossfade) {
                        animator2.setStartDelay(80L);
                        this.tabsAlphaAnimator.setDuration(100L);
                    } else {
                        animator2.setDuration(z ? 200L : 180L);
                    }
                }
            }
            this.searchAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.39
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator3) {
                    DialogsActivity.this.getNotificationCenter().onAnimationFinish(DialogsActivity.this.animationIndex);
                    if (DialogsActivity.this.searchAnimator != animator3) {
                        return;
                    }
                    DialogsActivity.this.setDialogsListFrozen(false);
                    if (!z) {
                        DialogsActivity.this.searchItem.collapseSearchFilters();
                        DialogsActivity dialogsActivity = DialogsActivity.this;
                        dialogsActivity.whiteActionBar = false;
                        dialogsActivity.searchViewPager.setVisibility(8);
                        if (DialogsActivity.this.searchTabsView != null) {
                            DialogsActivity.this.searchTabsView.setVisibility(8);
                        }
                        DialogsActivity.this.searchItem.clearSearchFilters();
                        DialogsActivity.this.searchViewPager.clear();
                        DialogsActivity.this.filtersView.setVisibility(8);
                        DialogsActivity.this.viewPages[0].listView.show();
                        if (!DialogsActivity.this.onlySelect) {
                            DialogsActivity.this.hideFloatingButton(false);
                        }
                        DialogsActivity.this.searchWasFullyShowed = false;
                        RightSlidingDialogContainer rightSlidingDialogContainer4 = DialogsActivity.this.rightSlidingDialogContainer;
                        if (rightSlidingDialogContainer4 != null) {
                            rightSlidingDialogContainer4.setVisibility(0);
                        }
                    } else {
                        DialogsActivity.this.viewPages[0].listView.hide();
                        if (DialogsActivity.this.filterTabsView != null) {
                            DialogsActivity.this.filterTabsView.setVisibility(8);
                        }
                        DialogsActivity.this.searchWasFullyShowed = true;
                        AndroidUtilities.requestAdjustResize(DialogsActivity.this.getParentActivity(), ((BaseFragment) DialogsActivity.this).classGuid);
                        DialogsActivity.this.searchItem.setVisibility(8);
                        RightSlidingDialogContainer rightSlidingDialogContainer5 = DialogsActivity.this.rightSlidingDialogContainer;
                        if (rightSlidingDialogContainer5 != null) {
                            rightSlidingDialogContainer5.setVisibility(8);
                        }
                    }
                    if (((BaseFragment) DialogsActivity.this).fragmentView != null) {
                        ((BaseFragment) DialogsActivity.this).fragmentView.requestLayout();
                    }
                    DialogsActivity.this.setSearchAnimationProgress(z ? 1.0f : 0.0f, false);
                    DialogsActivity.this.viewPages[0].listView.setVerticalScrollBarEnabled(true);
                    DialogsActivity.this.searchViewPager.setBackground(null);
                    DialogsActivity.this.searchAnimator = null;
                    if (DialogsActivity.this.downloadsItem != null) {
                        DialogsActivity.this.downloadsItem.setAlpha(z ? 0.0f : 1.0f);
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator3) {
                    DialogsActivity.this.getNotificationCenter().onAnimationFinish(DialogsActivity.this.animationIndex);
                    if (DialogsActivity.this.searchAnimator == animator3) {
                        if (z) {
                            DialogsActivity.this.viewPages[0].listView.hide();
                        } else {
                            DialogsActivity.this.viewPages[0].listView.show();
                        }
                        DialogsActivity.this.searchAnimator = null;
                    }
                }
            });
            this.animationIndex = getNotificationCenter().setAnimationInProgress(this.animationIndex, null);
            this.searchAnimator.start();
            Animator animator3 = this.tabsAlphaAnimator;
            if (animator3 != null) {
                animator3.start();
            }
        } else {
            setDialogsListFrozen(false);
            if (z) {
                this.viewPages[0].listView.hide();
            } else {
                this.viewPages[0].listView.show();
            }
            this.viewPages[0].setAlpha(z ? 0.0f : 1.0f);
            if (!z6) {
                this.viewPages[0].setScaleX(z ? 0.9f : 1.0f);
                this.viewPages[0].setScaleY(z ? 0.9f : 1.0f);
            } else {
                this.viewPages[0].setScaleX(1.0f);
                this.viewPages[0].setScaleY(1.0f);
            }
            this.searchViewPager.setAlpha(z ? 1.0f : 0.0f);
            this.filtersView.setAlpha(z ? 1.0f : 0.0f);
            if (!z6) {
                this.searchViewPager.setScaleX(z ? 1.0f : 1.1f);
                this.searchViewPager.setScaleY(z ? 1.0f : 1.1f);
            } else {
                this.searchViewPager.setScaleX(1.0f);
                this.searchViewPager.setScaleY(1.0f);
            }
            FilterTabsView filterTabsView3 = this.filterTabsView;
            if (filterTabsView3 != null && filterTabsView3.getVisibility() == 0) {
                this.filterTabsView.setTranslationY(z ? -AndroidUtilities.dp(44.0f) : 0.0f);
                this.filterTabsView.getTabsContainer().setAlpha(z ? 0.0f : 1.0f);
            }
            FilterTabsView filterTabsView4 = this.filterTabsView;
            if (filterTabsView4 != null) {
                if (this.canShowFilterTabsView && !z) {
                    filterTabsView4.setVisibility(0);
                } else {
                    filterTabsView4.setVisibility(8);
                }
            }
            this.searchViewPager.setVisibility(z ? 0 : 8);
            setSearchAnimationProgress(z ? 1.0f : 0.0f, false);
            this.fragmentView.invalidate();
            ActionBarMenuItem actionBarMenuItem3 = this.downloadsItem;
            if (actionBarMenuItem3 != null) {
                actionBarMenuItem3.setAlpha(z ? 0.0f : 1.0f);
            }
        }
        int i3 = this.initialSearchType;
        if (i3 >= 0) {
            SearchViewPager searchViewPager4 = this.searchViewPager;
            searchViewPager4.setPosition(searchViewPager4.getPositionForType(i3));
        }
        if (!z) {
            this.initialSearchType = -1;
        }
        if (z && z2) {
            this.searchViewPager.showDownloads();
            updateSpeedItem(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSearch$39(ValueAnimator valueAnimator) {
        setSearchAnimationProgress(((Float) valueAnimator.getAnimatedValue()).floatValue(), false);
    }

    public boolean onlyDialogsAdapter() {
        return this.onlySelect || !this.searchViewPager.dialogsSearchAdapter.hasRecentSearch() || getMessagesController().getTotalDialogsCount() <= 10;
    }

    private void updateFilterTabsVisibility(boolean z) {
        z = (this.isPaused || this.databaseMigrationHint != null) ? false : false;
        if (this.searchIsShowed) {
            ValueAnimator valueAnimator = this.filtersTabAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            boolean z2 = this.canShowFilterTabsView;
            this.filterTabsViewIsVisible = z2;
            this.filterTabsProgress = z2 ? 1.0f : 0.0f;
            return;
        }
        final boolean z3 = this.canShowFilterTabsView;
        if (this.filterTabsViewIsVisible != z3) {
            ValueAnimator valueAnimator2 = this.filtersTabAnimator;
            if (valueAnimator2 != null) {
                valueAnimator2.cancel();
            }
            this.filterTabsViewIsVisible = z3;
            if (z) {
                if (z3) {
                    if (this.filterTabsView.getVisibility() != 0) {
                        this.filterTabsView.setVisibility(0);
                    }
                    this.filtersTabAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
                    this.filterTabsMoveFrom = AndroidUtilities.dp(44.0f);
                } else {
                    this.filtersTabAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
                    this.filterTabsMoveFrom = Math.max(0.0f, AndroidUtilities.dp(44.0f) + this.actionBar.getTranslationY());
                }
                final float translationY = this.actionBar.getTranslationY();
                this.filtersTabAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.40
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        DialogsActivity.this.filtersTabAnimator = null;
                        DialogsActivity.this.scrollAdditionalOffset = AndroidUtilities.dp(44.0f) - DialogsActivity.this.filterTabsMoveFrom;
                        if (!z3) {
                            DialogsActivity.this.filterTabsView.setVisibility(8);
                        }
                        if (((BaseFragment) DialogsActivity.this).fragmentView != null) {
                            ((BaseFragment) DialogsActivity.this).fragmentView.requestLayout();
                        }
                        DialogsActivity.this.getNotificationCenter().onAnimationFinish(DialogsActivity.this.animationIndex);
                    }
                });
                this.filtersTabAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda6
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                        DialogsActivity.this.lambda$updateFilterTabsVisibility$40(z3, translationY, valueAnimator3);
                    }
                });
                this.filtersTabAnimator.setDuration(220L);
                this.filtersTabAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                this.animationIndex = getNotificationCenter().setAnimationInProgress(this.animationIndex, null);
                this.filtersTabAnimator.start();
                this.fragmentView.requestLayout();
                return;
            }
            this.filterTabsProgress = z3 ? 1.0f : 0.0f;
            this.filterTabsView.setVisibility(z3 ? 0 : 8);
            View view = this.fragmentView;
            if (view != null) {
                view.invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateFilterTabsVisibility$40(boolean z, float f, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.filterTabsProgress = floatValue;
        if (!z) {
            setScrollY(f * floatValue);
        }
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
    }

    public void setSearchAnimationProgress(float f, boolean z) {
        this.searchAnimationProgress = f;
        boolean z2 = true;
        if (this.whiteActionBar) {
            this.actionBar.setItemsColor(ColorUtils.blendARGB(Theme.getColor(this.folderId != 0 ? "actionBarDefaultArchivedIcon" : "actionBarDefaultIcon"), Theme.getColor("actionBarActionModeDefaultIcon"), this.searchAnimationProgress), false);
            this.actionBar.setItemsColor(ColorUtils.blendARGB(Theme.getColor("actionBarActionModeDefaultIcon"), Theme.getColor("actionBarActionModeDefaultIcon"), this.searchAnimationProgress), true);
            this.actionBar.setItemsBackgroundColor(ColorUtils.blendARGB(Theme.getColor(this.folderId != 0 ? "actionBarDefaultArchivedSelector" : "actionBarDefaultSelector"), Theme.getColor("actionBarActionModeDefaultSelector"), this.searchAnimationProgress), false);
        }
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
        DialogsHintCell dialogsHintCell = this.dialogsHintCell;
        if (dialogsHintCell != null) {
            dialogsHintCell.setAlpha(1.0f - f);
        }
        if (SharedConfig.getDevicePerformanceClass() != 0 && !SharedConfig.getLiteMode().enabled()) {
            z2 = false;
        }
        if (z) {
            ViewPage[] viewPageArr = this.viewPages;
            if (viewPageArr[0] != null) {
                if (f < 1.0f) {
                    viewPageArr[0].setVisibility(0);
                }
                this.viewPages[0].setAlpha(1.0f - f);
                if (!z2) {
                    float f2 = (0.1f * f) + 0.9f;
                    this.viewPages[0].setScaleX(f2);
                    this.viewPages[0].setScaleY(f2);
                }
            }
            RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer != null) {
                if (f >= 1.0f) {
                    rightSlidingDialogContainer.setVisibility(8);
                } else {
                    rightSlidingDialogContainer.setVisibility(0);
                    this.rightSlidingDialogContainer.setAlpha(1.0f - f);
                }
            }
            SearchViewPager searchViewPager = this.searchViewPager;
            if (searchViewPager != null) {
                searchViewPager.setAlpha(f);
                if (!z2) {
                    float f3 = ((1.0f - f) * 0.05f) + 1.0f;
                    this.searchViewPager.setScaleX(f3);
                    this.searchViewPager.setScaleY(f3);
                }
            }
            ActionBarMenuItem actionBarMenuItem = this.passcodeItem;
            if (actionBarMenuItem != null) {
                actionBarMenuItem.getIconView().setAlpha(1.0f - f);
            }
            ActionBarMenuItem actionBarMenuItem2 = this.downloadsItem;
            if (actionBarMenuItem2 != null) {
                actionBarMenuItem2.setAlpha(1.0f - f);
            }
            FilterTabsView filterTabsView = this.filterTabsView;
            if (filterTabsView != null && filterTabsView.getVisibility() == 0) {
                this.filterTabsView.getTabsContainer().setAlpha(1.0f - f);
            }
        }
        updateContextViewPosition();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void findAndUpdateCheckBox(long j, boolean z) {
        if (this.viewPages == null) {
            return;
        }
        int i = 0;
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                return;
            }
            int childCount = viewPageArr[i].listView.getChildCount();
            int i2 = 0;
            while (true) {
                if (i2 < childCount) {
                    View childAt = this.viewPages[i].listView.getChildAt(i2);
                    if (childAt instanceof DialogCell) {
                        DialogCell dialogCell = (DialogCell) childAt;
                        if (dialogCell.getDialogId() == j) {
                            dialogCell.setChecked(z, true);
                            break;
                        }
                    }
                    i2++;
                }
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkListLoad(ViewPage viewPage) {
        final boolean z;
        final boolean z2;
        final boolean z3;
        final boolean z4;
        if (this.tabsAnimationInProgress || this.startedTracking) {
            return;
        }
        FilterTabsView filterTabsView = this.filterTabsView;
        if (filterTabsView != null && filterTabsView.getVisibility() == 0 && this.filterTabsView.isAnimatingIndicator()) {
            return;
        }
        int findFirstVisibleItemPosition = viewPage.layoutManager.findFirstVisibleItemPosition();
        int findLastVisibleItemPosition = viewPage.layoutManager.findLastVisibleItemPosition();
        int abs = Math.abs(viewPage.layoutManager.findLastVisibleItemPosition() - findFirstVisibleItemPosition) + 1;
        if (findLastVisibleItemPosition != -1) {
            RecyclerView.ViewHolder findViewHolderForAdapterPosition = viewPage.listView.findViewHolderForAdapterPosition(findLastVisibleItemPosition);
            boolean z5 = findViewHolderForAdapterPosition != null && findViewHolderForAdapterPosition.getItemViewType() == 11;
            this.floatingForceVisible = z5;
            if (z5) {
                hideFloatingButton(false);
            }
        } else {
            this.floatingForceVisible = false;
        }
        if (viewPage.dialogsType == 7 || viewPage.dialogsType == 8) {
            ArrayList<MessagesController.DialogFilter> arrayList = getMessagesController().dialogFilters;
            if (viewPage.selectedType >= 0 && viewPage.selectedType < arrayList.size() && (getMessagesController().dialogFilters.get(viewPage.selectedType).flags & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_ARCHIVED) == 0 && ((abs > 0 && findLastVisibleItemPosition >= getDialogsArray(this.currentAccount, viewPage.dialogsType, 1, this.dialogsListFrozen).size() - 10) || (abs == 0 && !getMessagesController().isDialogsEndReached(1)))) {
                boolean z6 = !getMessagesController().isDialogsEndReached(1);
                if (z6 || !getMessagesController().isServerDialogsEndReached(1)) {
                    z = z6;
                    z2 = true;
                } else {
                    z = z6;
                    z2 = false;
                }
                if ((abs > 0 || findLastVisibleItemPosition < getDialogsArray(this.currentAccount, viewPage.dialogsType, this.folderId, this.dialogsListFrozen).size() - 10) && (abs != 0 || (!(viewPage.dialogsType == 7 || viewPage.dialogsType == 8) || getMessagesController().isDialogsEndReached(this.folderId)))) {
                    z3 = false;
                    z4 = false;
                } else {
                    boolean z7 = !getMessagesController().isDialogsEndReached(this.folderId);
                    if (z7 || !getMessagesController().isServerDialogsEndReached(this.folderId)) {
                        z4 = z7;
                        z3 = true;
                    } else {
                        z4 = z7;
                        z3 = false;
                    }
                }
                if (!z3 || z2) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda65
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.this.lambda$checkListLoad$41(z3, z4, z2, z);
                        }
                    });
                }
                return;
            }
        }
        z2 = false;
        z = false;
        if (abs > 0) {
        }
        z3 = false;
        z4 = false;
        if (z3) {
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda65
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$checkListLoad$41(z3, z4, z2, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkListLoad$41(boolean z, boolean z2, boolean z3, boolean z4) {
        if (z) {
            getMessagesController().loadDialogs(this.folderId, -1, 100, z2);
        }
        if (z3) {
            getMessagesController().loadDialogs(1, -1, 100, z4);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:119:0x0205  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x0233 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0234  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x030d  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x0313  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x032c  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x0357  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x03e4  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x03ed  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x040c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void onItemClick(View view, int i, RecyclerView.Adapter adapter) {
        boolean z;
        int i2;
        int i3;
        int i4;
        int i5;
        long j;
        RightSlidingDialogContainer rightSlidingDialogContainer;
        long dialogId;
        int id;
        long j2;
        int i6;
        long j3;
        TLRPC$Document greetingsSticker;
        long j4;
        if (getParentActivity() == null) {
            return;
        }
        boolean z2 = adapter instanceof DialogsAdapter;
        if (z2) {
            DialogsAdapter dialogsAdapter = (DialogsAdapter) adapter;
            int dialogsType = dialogsAdapter.getDialogsType();
            if (dialogsType == 7 || dialogsType == 8) {
                i5 = getMessagesController().selectedDialogFilter[dialogsType == 7 ? (char) 0 : (char) 1].id;
            } else {
                i5 = 0;
            }
            TLObject item = dialogsAdapter.getItem(i);
            if (item instanceof TLRPC$User) {
                j = ((TLRPC$User) item).id;
            } else if (item instanceof TLRPC$Dialog) {
                TLRPC$Dialog tLRPC$Dialog = (TLRPC$Dialog) item;
                i4 = tLRPC$Dialog.folder_id;
                if (tLRPC$Dialog instanceof TLRPC$TL_dialogFolder) {
                    if (this.actionBar.isActionModeShowed(null)) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("folderId", ((TLRPC$TL_dialogFolder) tLRPC$Dialog).folder.id);
                    presentFragment(new DialogsActivity(bundle));
                    return;
                }
                j = tLRPC$Dialog.id;
                if (this.actionBar.isActionModeShowed(null)) {
                    showOrUpdateActionMode(j, view);
                    return;
                }
                z = false;
                i2 = 0;
                i3 = 0;
            } else {
                if (item instanceof TLRPC$TL_recentMeUrlChat) {
                    j4 = ((TLRPC$TL_recentMeUrlChat) item).chat_id;
                } else if (item instanceof TLRPC$TL_recentMeUrlUser) {
                    j = ((TLRPC$TL_recentMeUrlUser) item).user_id;
                } else if (item instanceof TLRPC$TL_recentMeUrlChatInvite) {
                    TLRPC$TL_recentMeUrlChatInvite tLRPC$TL_recentMeUrlChatInvite = (TLRPC$TL_recentMeUrlChatInvite) item;
                    TLRPC$ChatInvite tLRPC$ChatInvite = tLRPC$TL_recentMeUrlChatInvite.chat_invite;
                    TLRPC$Chat tLRPC$Chat = tLRPC$ChatInvite.chat;
                    if ((tLRPC$Chat == null && (!tLRPC$ChatInvite.channel || tLRPC$ChatInvite.megagroup)) || (tLRPC$Chat != null && (!ChatObject.isChannel(tLRPC$Chat) || tLRPC$ChatInvite.chat.megagroup))) {
                        String str = tLRPC$TL_recentMeUrlChatInvite.url;
                        int indexOf = str.indexOf(47);
                        if (indexOf > 0) {
                            str = str.substring(indexOf + 1);
                        }
                        showDialog(new JoinGroupAlert(getParentActivity(), tLRPC$ChatInvite, str, this, null));
                        return;
                    }
                    TLRPC$Chat tLRPC$Chat2 = tLRPC$ChatInvite.chat;
                    if (tLRPC$Chat2 == null) {
                        return;
                    }
                    j4 = tLRPC$Chat2.id;
                } else if (item instanceof TLRPC$TL_recentMeUrlStickerSet) {
                    TLRPC$StickerSet tLRPC$StickerSet = ((TLRPC$TL_recentMeUrlStickerSet) item).set.set;
                    TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID = new TLRPC$TL_inputStickerSetID();
                    tLRPC$TL_inputStickerSetID.id = tLRPC$StickerSet.id;
                    tLRPC$TL_inputStickerSetID.access_hash = tLRPC$StickerSet.access_hash;
                    showDialog(new StickersAlert(getParentActivity(), this, tLRPC$TL_inputStickerSetID, (TLRPC$TL_messages_stickerSet) null, (StickersAlert.StickersAlertDelegate) null));
                    return;
                } else {
                    boolean z3 = item instanceof TLRPC$TL_recentMeUrlUnknown;
                    return;
                }
                j = -j4;
            }
            i4 = 0;
            z = false;
            i2 = 0;
            i3 = 0;
        } else {
            DialogsSearchAdapter dialogsSearchAdapter = this.searchViewPager.dialogsSearchAdapter;
            if (adapter == dialogsSearchAdapter) {
                Object item2 = dialogsSearchAdapter.getItem(i);
                z = this.searchViewPager.dialogsSearchAdapter.isGlobalSearch(i);
                if (item2 instanceof TLRPC$User) {
                    TLRPC$User tLRPC$User = (TLRPC$User) item2;
                    dialogId = tLRPC$User.id;
                    if (!this.onlySelect) {
                        this.searchDialogId = dialogId;
                        this.searchObject = tLRPC$User;
                    }
                } else if (item2 instanceof TLRPC$Chat) {
                    TLRPC$Chat tLRPC$Chat3 = (TLRPC$Chat) item2;
                    dialogId = -tLRPC$Chat3.id;
                    if (!this.onlySelect) {
                        this.searchDialogId = dialogId;
                        this.searchObject = tLRPC$Chat3;
                    }
                } else if (item2 instanceof TLRPC$EncryptedChat) {
                    TLRPC$EncryptedChat tLRPC$EncryptedChat = (TLRPC$EncryptedChat) item2;
                    dialogId = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
                    if (!this.onlySelect) {
                        this.searchDialogId = dialogId;
                        this.searchObject = tLRPC$EncryptedChat;
                    }
                } else if (item2 instanceof MessageObject) {
                    MessageObject messageObject = (MessageObject) item2;
                    dialogId = messageObject.getDialogId();
                    id = messageObject.getId();
                    i2 = ChatObject.isForum(getMessagesController().getChat(Long.valueOf(-dialogId))) ? MessageObject.getTopicId(messageObject.messageOwner, true) : 0;
                    DialogsSearchAdapter dialogsSearchAdapter2 = this.searchViewPager.dialogsSearchAdapter;
                    dialogsSearchAdapter2.addHashtagsFromMessage(dialogsSearchAdapter2.getLastSearchString());
                    if (dialogId == 0 && this.actionBar.isActionModeShowed()) {
                        if (this.actionBar.isActionModeShowed("search_dialogs_action_mode") && id == 0 && !z) {
                            showOrUpdateActionMode(dialogId, view);
                            return;
                        }
                        return;
                    }
                    i3 = id;
                    i4 = 0;
                    j2 = 0;
                    j = dialogId;
                    i5 = 0;
                    if (j == j2) {
                        return;
                    }
                    if (this.onlySelect) {
                        if (validateSlowModeDialog(j)) {
                            if (!getMessagesController().isForum(j) && (!this.selectedDialogs.isEmpty() || (this.initialDialogsType == 3 && this.selectAlertString != null))) {
                                if (this.selectedDialogs.contains(Long.valueOf(j)) || checkCanWrite(j)) {
                                    boolean addOrRemoveSelectedDialog = addOrRemoveSelectedDialog(j, view);
                                    if (adapter == this.searchViewPager.dialogsSearchAdapter) {
                                        this.actionBar.closeSearchField();
                                        findAndUpdateCheckBox(j, addOrRemoveSelectedDialog);
                                    }
                                    updateSelectedCount();
                                    return;
                                }
                                return;
                            } else if (this.canSelectTopics && getMessagesController().isForum(j)) {
                                Bundle bundle2 = new Bundle();
                                bundle2.putLong("chat_id", -j);
                                bundle2.putBoolean("for_select", true);
                                bundle2.putBoolean("forward_to", true);
                                TopicsFragment topicsFragment = new TopicsFragment(bundle2);
                                topicsFragment.setForwardFromDialogFragment(this);
                                presentFragment(topicsFragment);
                                return;
                            } else {
                                didSelectResult(j, 0, true, false);
                                return;
                            }
                        }
                        return;
                    }
                    Bundle bundle3 = new Bundle();
                    if (DialogObject.isEncryptedDialog(j)) {
                        bundle3.putInt("enc_id", DialogObject.getEncryptedChatId(j));
                    } else if (DialogObject.isUserDialog(j)) {
                        bundle3.putLong("user_id", j);
                    } else {
                        if (i3 != 0) {
                            i6 = i4;
                            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j));
                            if (chat != null && chat.migrated_to != null) {
                                bundle3.putLong("migrated_to", j);
                                j3 = -chat.migrated_to.channel_id;
                                bundle3.putLong("chat_id", -j3);
                                if (i3 != 0) {
                                    bundle3.putInt("message_id", i3);
                                } else if (!z) {
                                    closeSearch();
                                } else {
                                    TLObject tLObject = this.searchObject;
                                    if (tLObject != null) {
                                        this.searchViewPager.dialogsSearchAdapter.putRecentSearch(this.searchDialogId, tLObject);
                                        this.searchObject = null;
                                    }
                                }
                                boolean z4 = LocaleController.isRTL && !this.searching && (!AndroidUtilities.isTablet() || i6 == 0) && SharedConfig.getLiteMode().topicsInRightMenuEnabled();
                                bundle3.putInt("dialog_folder_id", i6);
                                bundle3.putInt("dialog_filter_id", i5);
                                if (!AndroidUtilities.isTablet() && ((!getMessagesController().isForum(j) || !z4) && this.openedDialogId.dialogId == j && adapter != this.searchViewPager.dialogsSearchAdapter)) {
                                    if (getParentActivity() instanceof LaunchActivity) {
                                        LaunchActivity launchActivity = (LaunchActivity) getParentActivity();
                                        List<BaseFragment> fragmentStack = launchActivity.getRightActionBarLayout().getFragmentStack();
                                        if (fragmentStack.isEmpty()) {
                                            return;
                                        }
                                        if (fragmentStack.size() == 1 && (fragmentStack.get(fragmentStack.size() - 1) instanceof ChatActivity)) {
                                            ((ChatActivity) fragmentStack.get(fragmentStack.size() - 1)).onPageDownClicked();
                                            return;
                                        } else if (fragmentStack.size() == 2) {
                                            launchActivity.getRightActionBarLayout().closeLastFragment();
                                            return;
                                        } else if (getParentActivity() instanceof LaunchActivity) {
                                            fragmentStack.clear();
                                            fragmentStack.add(fragmentStack.get(0));
                                            launchActivity.getRightActionBarLayout().rebuildFragments(1);
                                            return;
                                        } else {
                                            return;
                                        }
                                    }
                                    return;
                                }
                                if (this.searchViewPager.actionModeShowing()) {
                                    this.searchViewPager.hideActionMode();
                                }
                                if (this.searchString != null) {
                                    if (getMessagesController().checkCanOpenChat(bundle3, this)) {
                                        getNotificationCenter().postNotificationName(NotificationCenter.closeChats, new Object[0]);
                                        presentFragment(new ChatActivity(bundle3));
                                        return;
                                    }
                                    return;
                                }
                                this.slowedReloadAfterDialogClick = true;
                                if (getMessagesController().checkCanOpenChat(bundle3, this)) {
                                    TLRPC$Chat chat2 = getMessagesController().getChat(Long.valueOf(-j));
                                    if (chat2 != null && chat2.forum && i2 == 0) {
                                        if (SharedConfig.getDevicePerformanceClass() == 0 || SharedConfig.getLiteMode().enabled()) {
                                            presentFragment(new TopicsFragment(bundle3));
                                            return;
                                        } else if (!z4) {
                                            presentFragment(new TopicsFragment(bundle3));
                                            return;
                                        } else if (this.searching) {
                                            return;
                                        } else {
                                            BaseFragment baseFragment = this.rightSlidingDialogContainer.currentFragment;
                                            if (baseFragment == null || ((TopicsFragment) baseFragment).getDialogId() != j) {
                                                this.viewPages[0].listView.prepareSelectorForAnimation();
                                                TopicsFragment topicsFragment2 = new TopicsFragment(bundle3);
                                                topicsFragment2.parentDialogsActivity = this;
                                                this.rightSlidingDialogContainer.presentFragment(getParentLayout(), topicsFragment2);
                                            } else {
                                                this.rightSlidingDialogContainer.lambda$presentFragment$1();
                                            }
                                            this.searchViewPager.updateTabs();
                                            return;
                                        }
                                    }
                                    ChatActivity chatActivity = new ChatActivity(bundle3);
                                    if (i2 != 0) {
                                        ForumUtilities.applyTopic(chatActivity, MessagesStorage.TopicKey.of(j, i2));
                                    }
                                    if (z2 && DialogObject.isUserDialog(j) && getMessagesController().dialogs_dict.get(j) == null && (greetingsSticker = getMediaDataController().getGreetingsSticker()) != null) {
                                        chatActivity.setPreloadedSticker(greetingsSticker, true);
                                    }
                                    presentFragment(chatActivity);
                                    return;
                                }
                                return;
                            }
                        } else {
                            i6 = i4;
                        }
                        j3 = j;
                        bundle3.putLong("chat_id", -j3);
                        if (i3 != 0) {
                        }
                        if (LocaleController.isRTL) {
                        }
                        bundle3.putInt("dialog_folder_id", i6);
                        bundle3.putInt("dialog_filter_id", i5);
                        if (!AndroidUtilities.isTablet()) {
                        }
                        if (this.searchViewPager.actionModeShowing()) {
                        }
                        if (this.searchString != null) {
                        }
                    }
                    i6 = i4;
                    if (i3 != 0) {
                    }
                    if (LocaleController.isRTL) {
                    }
                    bundle3.putInt("dialog_folder_id", i6);
                    bundle3.putInt("dialog_filter_id", i5);
                    if (!AndroidUtilities.isTablet()) {
                    }
                    if (this.searchViewPager.actionModeShowing()) {
                    }
                    if (this.searchString != null) {
                    }
                } else {
                    if (item2 instanceof String) {
                        String str2 = (String) item2;
                        if (this.searchViewPager.dialogsSearchAdapter.isHashtagSearch()) {
                            this.actionBar.openSearchField(str2, false);
                        } else if (!str2.equals("section")) {
                            NewContactBottomSheet newContactBottomSheet = new NewContactBottomSheet(this, getContext());
                            newContactBottomSheet.setInitialPhoneNumber(str2, true);
                            newContactBottomSheet.show();
                        }
                    } else if (item2 instanceof ContactsController.Contact) {
                        ContactsController.Contact contact = (ContactsController.Contact) item2;
                        AlertsCreator.createContactInviteDialog(this, contact.first_name, contact.last_name, contact.phones.get(0));
                    } else if ((item2 instanceof TLRPC$TL_forumTopic) && (rightSlidingDialogContainer = this.rightSlidingDialogContainer) != null && (rightSlidingDialogContainer.getFragment() instanceof TopicsFragment)) {
                        dialogId = ((TopicsFragment) this.rightSlidingDialogContainer.getFragment()).getDialogId();
                        i2 = ((TLRPC$TL_forumTopic) item2).id;
                        id = 0;
                        if (dialogId == 0) {
                        }
                        i3 = id;
                        i4 = 0;
                        j2 = 0;
                        j = dialogId;
                        i5 = 0;
                        if (j == j2) {
                        }
                    }
                    i2 = 0;
                    dialogId = 0;
                    id = 0;
                    if (dialogId == 0) {
                    }
                    i3 = id;
                    i4 = 0;
                    j2 = 0;
                    j = dialogId;
                    i5 = 0;
                    if (j == j2) {
                    }
                }
                i2 = 0;
                id = 0;
                if (dialogId == 0) {
                }
                i3 = id;
                i4 = 0;
                j2 = 0;
                j = dialogId;
                i5 = 0;
                if (j == j2) {
                }
            } else {
                z = false;
                i2 = 0;
                i3 = 0;
                i4 = 0;
                i5 = 0;
                j = 0;
            }
        }
        j2 = 0;
        if (j == j2) {
        }
    }

    public void setOpenedDialogId(long j, int i) {
        MessagesStorage.TopicKey topicKey = this.openedDialogId;
        topicKey.dialogId = j;
        topicKey.topicId = i;
        ViewPage[] viewPageArr = this.viewPages;
        if (viewPageArr == null) {
            return;
        }
        for (ViewPage viewPage : viewPageArr) {
            if (viewPage.isDefaultDialogType() && AndroidUtilities.isTablet()) {
                viewPage.dialogsAdapter.setOpenedDialogId(this.openedDialogId.dialogId);
            }
        }
        updateVisibleRows(MessagesController.UPDATE_MASK_SELECT_DIALOG);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onItemLongClick(RecyclerListView recyclerListView, View view, int i, float f, float f2, int i2, RecyclerView.Adapter adapter) {
        TLRPC$Dialog tLRPC$Dialog;
        final long makeEncryptedDialogId;
        if (getParentActivity() == null) {
            return false;
        }
        if (!this.actionBar.isActionModeShowed() && !AndroidUtilities.isTablet() && !this.onlySelect && (view instanceof DialogCell)) {
            DialogCell dialogCell = (DialogCell) view;
            if (!getMessagesController().isForum(dialogCell.getDialogId()) && dialogCell.isPointInsideAvatar(f, f2)) {
                return showChatPreview(dialogCell);
            }
        }
        RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer == null || !rightSlidingDialogContainer.hasFragment()) {
            DialogsSearchAdapter dialogsSearchAdapter = this.searchViewPager.dialogsSearchAdapter;
            if (adapter == dialogsSearchAdapter) {
                Object item = dialogsSearchAdapter.getItem(i);
                if (!this.searchViewPager.dialogsSearchAdapter.isSearchWas()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    builder.setTitle(LocaleController.getString("ClearSearchSingleAlertTitle", R.string.ClearSearchSingleAlertTitle));
                    if (item instanceof TLRPC$Chat) {
                        TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) item;
                        builder.setMessage(LocaleController.formatString("ClearSearchSingleChatAlertText", R.string.ClearSearchSingleChatAlertText, tLRPC$Chat.title));
                        makeEncryptedDialogId = -tLRPC$Chat.id;
                    } else if (item instanceof TLRPC$User) {
                        TLRPC$User tLRPC$User = (TLRPC$User) item;
                        if (tLRPC$User.id == getUserConfig().clientUserId) {
                            builder.setMessage(LocaleController.formatString("ClearSearchSingleChatAlertText", R.string.ClearSearchSingleChatAlertText, LocaleController.getString("SavedMessages", R.string.SavedMessages)));
                        } else {
                            builder.setMessage(LocaleController.formatString("ClearSearchSingleUserAlertText", R.string.ClearSearchSingleUserAlertText, ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name)));
                        }
                        makeEncryptedDialogId = tLRPC$User.id;
                    } else if (!(item instanceof TLRPC$EncryptedChat)) {
                        return false;
                    } else {
                        TLRPC$EncryptedChat tLRPC$EncryptedChat = (TLRPC$EncryptedChat) item;
                        TLRPC$User user = getMessagesController().getUser(Long.valueOf(tLRPC$EncryptedChat.user_id));
                        builder.setMessage(LocaleController.formatString("ClearSearchSingleUserAlertText", R.string.ClearSearchSingleUserAlertText, ContactsController.formatName(user.first_name, user.last_name)));
                        makeEncryptedDialogId = DialogObject.makeEncryptedDialogId(tLRPC$EncryptedChat.id);
                    }
                    builder.setPositiveButton(LocaleController.getString("ClearSearchRemove", R.string.ClearSearchRemove), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda15
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i3) {
                            DialogsActivity.this.lambda$onItemLongClick$42(makeEncryptedDialogId, dialogInterface, i3);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    AlertDialog create = builder.create();
                    showDialog(create);
                    TextView textView = (TextView) create.getButton(-1);
                    if (textView != null) {
                        textView.setTextColor(Theme.getColor("dialogTextRed2"));
                    }
                    return true;
                }
            }
            DialogsSearchAdapter dialogsSearchAdapter2 = this.searchViewPager.dialogsSearchAdapter;
            if (adapter == dialogsSearchAdapter2) {
                if (this.onlySelect) {
                    onItemClick(view, i, adapter);
                    return false;
                }
                long dialogId = (!(view instanceof ProfileSearchCell) || dialogsSearchAdapter2.isGlobalSearch(i)) ? 0L : ((ProfileSearchCell) view).getDialogId();
                if (dialogId != 0) {
                    showOrUpdateActionMode(dialogId, view);
                    return true;
                }
                return false;
            }
            ArrayList<TLRPC$Dialog> dialogsArray = getDialogsArray(this.currentAccount, i2, this.folderId, this.dialogsListFrozen);
            int fixPosition = ((DialogsAdapter) adapter).fixPosition(i);
            if (fixPosition < 0 || fixPosition >= dialogsArray.size() || (tLRPC$Dialog = dialogsArray.get(fixPosition)) == null) {
                return false;
            }
            if (this.onlySelect) {
                int i3 = this.initialDialogsType;
                if ((i3 == 3 || i3 == 10) && validateSlowModeDialog(tLRPC$Dialog.id)) {
                    addOrRemoveSelectedDialog(tLRPC$Dialog.id, view);
                    updateSelectedCount();
                    return true;
                }
                return false;
            } else if (tLRPC$Dialog instanceof TLRPC$TL_dialogFolder) {
                onArchiveLongPress(view);
                return false;
            } else if (this.actionBar.isActionModeShowed() && isDialogPinned(tLRPC$Dialog)) {
                return false;
            } else {
                showOrUpdateActionMode(tLRPC$Dialog.id, view);
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onItemLongClick$42(long j, DialogInterface dialogInterface, int i) {
        this.searchViewPager.dialogsSearchAdapter.removeRecentSearch(j);
    }

    private void onArchiveLongPress(View view) {
        int i;
        String str;
        view.performHapticFeedback(0, 2);
        BottomSheet.Builder builder = new BottomSheet.Builder(getParentActivity());
        boolean z = getMessagesStorage().getArchiveUnreadCount() != 0;
        int[] iArr = new int[2];
        iArr[0] = z ? R.drawable.msg_markread : 0;
        iArr[1] = SharedConfig.archiveHidden ? R.drawable.chats_pin : R.drawable.chats_unpin;
        CharSequence[] charSequenceArr = new CharSequence[2];
        charSequenceArr[0] = z ? LocaleController.getString("MarkAllAsRead", R.string.MarkAllAsRead) : null;
        if (SharedConfig.archiveHidden) {
            i = R.string.PinInTheList;
            str = "PinInTheList";
        } else {
            i = R.string.HideAboveTheList;
            str = "HideAboveTheList";
        }
        charSequenceArr[1] = LocaleController.getString(str, i);
        builder.setItems(charSequenceArr, iArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda11
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                DialogsActivity.this.lambda$onArchiveLongPress$43(dialogInterface, i2);
            }
        });
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onArchiveLongPress$43(DialogInterface dialogInterface, int i) {
        if (i == 0) {
            getMessagesStorage().readAllDialogs(1);
        } else if (i != 1 || this.viewPages == null) {
        } else {
            int i2 = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i2 >= viewPageArr.length) {
                    return;
                }
                if (viewPageArr[i2].dialogsType == 0 && this.viewPages[i2].getVisibility() == 0) {
                    View childAt = this.viewPages[i2].listView.getChildAt(0);
                    DialogCell dialogCell = null;
                    if (childAt instanceof DialogCell) {
                        DialogCell dialogCell2 = (DialogCell) childAt;
                        if (dialogCell2.isFolderCell()) {
                            dialogCell = dialogCell2;
                        }
                    }
                    this.viewPages[i2].listView.toggleArchiveHidden(true, dialogCell);
                }
                i2++;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r1v29 */
    /* JADX WARN: Type inference failed for: r1v30, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v83 */
    public boolean showChatPreview(final DialogCell dialogCell) {
        long j;
        TLRPC$Chat chat;
        final MessagesController.DialogFilter dialogFilter;
        ChatActivity[] chatActivityArr;
        ?? r1;
        Bundle bundle;
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        boolean z2;
        boolean z3;
        ArrayList<TLRPC$Dialog> arrayList;
        int i5;
        if (dialogCell.isDialogFolder()) {
            if (dialogCell.getCurrentDialogFolderId() == 1) {
                onArchiveLongPress(dialogCell);
            }
            return false;
        }
        final long dialogId = dialogCell.getDialogId();
        Bundle bundle2 = new Bundle();
        int messageId = dialogCell.getMessageId();
        if (DialogObject.isEncryptedDialog(dialogId)) {
            return false;
        }
        if (DialogObject.isUserDialog(dialogId)) {
            bundle2.putLong("user_id", dialogId);
        } else {
            if (messageId == 0 || (chat = getMessagesController().getChat(Long.valueOf(-dialogId))) == null || chat.migrated_to == null) {
                j = dialogId;
            } else {
                bundle2.putLong("migrated_to", dialogId);
                j = -chat.migrated_to.channel_id;
            }
            bundle2.putLong("chat_id", -j);
        }
        if (messageId != 0) {
            bundle2.putInt("message_id", messageId);
        }
        final ArrayList arrayList2 = new ArrayList();
        arrayList2.add(Long.valueOf(dialogId));
        ChatActivity[] chatActivityArr2 = new ChatActivity[1];
        ActionBarPopupWindow.ActionBarPopupWindowLayout[] actionBarPopupWindowLayoutArr = {new ActionBarPopupWindow.ActionBarPopupWindowLayout(getParentActivity(), R.drawable.popup_fixed_alert2, getResourceProvider(), 2)};
        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getParentActivity(), true, false);
        if (dialogCell.getHasUnread()) {
            actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("MarkAsRead", R.string.MarkAsRead), R.drawable.msg_markread);
        } else {
            actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("MarkAsUnread", R.string.MarkAsUnread), R.drawable.msg_markunread);
        }
        actionBarMenuSubItem.setMinimumWidth(160);
        actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda35
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DialogsActivity.this.lambda$showChatPreview$44(dialogCell, dialogId, view);
            }
        });
        actionBarPopupWindowLayoutArr[0].addView(actionBarMenuSubItem);
        boolean[] zArr = new boolean[1];
        zArr[0] = true;
        final TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(dialogId);
        boolean z4 = (this.viewPages[0].dialogsType == 7 || this.viewPages[0].dialogsType == 8) && (!this.actionBar.isActionModeShowed() || this.actionBar.isActionModeShowed(null));
        if (z4) {
            dialogFilter = getMessagesController().selectedDialogFilter[this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
        } else {
            dialogFilter = null;
        }
        if (isDialogPinned(tLRPC$Dialog)) {
            chatActivityArr = chatActivityArr2;
            r1 = 0;
        } else {
            ArrayList<TLRPC$Dialog> dialogs = getMessagesController().getDialogs(this.folderId);
            int size = dialogs.size();
            int i6 = 0;
            int i7 = 0;
            int i8 = 0;
            while (true) {
                if (i6 >= size) {
                    chatActivityArr = chatActivityArr2;
                    break;
                }
                TLRPC$Dialog tLRPC$Dialog2 = dialogs.get(i6);
                if (tLRPC$Dialog2 instanceof TLRPC$TL_dialogFolder) {
                    arrayList = dialogs;
                    i5 = size;
                } else if (isDialogPinned(tLRPC$Dialog2)) {
                    arrayList = dialogs;
                    i5 = size;
                    if (DialogObject.isEncryptedDialog(tLRPC$Dialog2.id)) {
                        i8++;
                    } else {
                        i7++;
                    }
                } else {
                    arrayList = dialogs;
                    i5 = size;
                    chatActivityArr = chatActivityArr2;
                    if (!getMessagesController().isPromoDialog(tLRPC$Dialog2.id, false)) {
                        break;
                    }
                    i6++;
                    dialogs = arrayList;
                    size = i5;
                    chatActivityArr2 = chatActivityArr;
                }
                chatActivityArr = chatActivityArr2;
                i6++;
                dialogs = arrayList;
                size = i5;
                chatActivityArr2 = chatActivityArr;
            }
            if (tLRPC$Dialog == null || isDialogPinned(tLRPC$Dialog)) {
                i = 0;
                i2 = 0;
                i3 = 0;
            } else {
                boolean isEncryptedDialog = DialogObject.isEncryptedDialog(dialogId);
                int i9 = !isEncryptedDialog ? 1 : 0;
                if (dialogFilter == null || !dialogFilter.alwaysShow.contains(Long.valueOf(dialogId))) {
                    i3 = i9;
                    i2 = isEncryptedDialog ? 1 : 0;
                    i = 0;
                } else {
                    i3 = i9;
                    i2 = isEncryptedDialog ? 1 : 0;
                    i = 1;
                }
            }
            if (z4 && dialogFilter != null) {
                i4 = 100 - dialogFilter.alwaysShow.size();
            } else if (this.folderId != 0 || dialogFilter != null) {
                i4 = getMessagesController().maxFolderPinnedDialogsCount;
            } else {
                i4 = getMessagesController().maxPinnedDialogsCount;
            }
            if (i2 + i8 > i4 || (i3 + i7) - i > i4) {
                z2 = false;
                z3 = false;
            } else {
                z2 = false;
                z3 = true;
            }
            zArr[z2 ? 1 : 0] = z3;
            r1 = z2;
        }
        if (zArr[r1]) {
            ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(getParentActivity(), r1, r1);
            if (isDialogPinned(tLRPC$Dialog)) {
                actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString("UnpinMessage", R.string.UnpinMessage), R.drawable.msg_unpin);
            } else {
                actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString("PinMessage", R.string.PinMessage), R.drawable.msg_pin);
            }
            actionBarMenuSubItem2.setMinimumWidth(160);
            bundle = null;
            actionBarMenuSubItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda34
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.this.lambda$showChatPreview$46(dialogFilter, tLRPC$Dialog, dialogId, view);
                }
            });
            actionBarPopupWindowLayoutArr[0].addView(actionBarMenuSubItem2);
        } else {
            bundle = null;
        }
        if (DialogObject.isUserDialog(dialogId) && UserObject.isUserSelf(getMessagesController().getUser(Long.valueOf(dialogId)))) {
            z = false;
        } else {
            ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem(getParentActivity(), false, false);
            if (!getMessagesController().isDialogMuted(dialogId, 0)) {
                actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString("Mute", R.string.Mute), R.drawable.msg_mute);
            } else {
                actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString("Unmute", R.string.Unmute), R.drawable.msg_unmute);
            }
            actionBarMenuSubItem3.setMinimumWidth(160);
            actionBarMenuSubItem3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda32
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    DialogsActivity.this.lambda$showChatPreview$47(dialogId, view);
                }
            });
            z = false;
            actionBarPopupWindowLayoutArr[0].addView(actionBarMenuSubItem3);
        }
        ActionBarMenuSubItem actionBarMenuSubItem4 = new ActionBarMenuSubItem(getParentActivity(), z, true);
        actionBarMenuSubItem4.setIconColor(getThemedColor("dialogRedIcon"));
        actionBarMenuSubItem4.setTextColor(getThemedColor("dialogTextRed"));
        actionBarMenuSubItem4.setTextAndIcon(LocaleController.getString("Delete", R.string.Delete), R.drawable.msg_delete);
        actionBarMenuSubItem4.setMinimumWidth(160);
        actionBarMenuSubItem4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda33
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DialogsActivity.this.lambda$showChatPreview$48(arrayList2, view);
            }
        });
        actionBarPopupWindowLayoutArr[0].addView(actionBarMenuSubItem4);
        if (getMessagesController().checkCanOpenChat(bundle2, this)) {
            if (this.searchString != null) {
                getNotificationCenter().postNotificationName(NotificationCenter.closeChats, new Object[0]);
            }
            prepareBlurBitmap();
            this.parentLayout.setHighlightActionButtons(true);
            Point point = AndroidUtilities.displaySize;
            if (point.x > point.y) {
                BaseFragment chatActivity = new ChatActivity(bundle2);
                chatActivityArr[0] = chatActivity;
                presentFragmentAsPreview(chatActivity);
                return true;
            }
            BaseFragment chatActivity2 = new ChatActivity(bundle2);
            chatActivityArr[0] = chatActivity2;
            presentFragmentAsPreviewWithMenu(chatActivity2, actionBarPopupWindowLayoutArr[0]);
            if (chatActivityArr[0] != null) {
                chatActivityArr[0].allowExpandPreviewByClick = true;
                try {
                    chatActivityArr[0].getAvatarContainer().getAvatarImageView().performAccessibilityAction(64, bundle);
                    return true;
                } catch (Exception unused) {
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showChatPreview$44(DialogCell dialogCell, long j, View view) {
        if (dialogCell.getHasUnread()) {
            markAsRead(j);
        } else {
            markAsUnread(j);
        }
        finishPreviewFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showChatPreview$46(final MessagesController.DialogFilter dialogFilter, final TLRPC$Dialog tLRPC$Dialog, final long j, View view) {
        finishPreviewFragment();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda59
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$showChatPreview$45(dialogFilter, tLRPC$Dialog, j);
            }
        }, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showChatPreview$45(MessagesController.DialogFilter dialogFilter, TLRPC$Dialog tLRPC$Dialog, long j) {
        int i;
        boolean z;
        int i2 = ConnectionsManager.DEFAULT_DATACENTER_ID;
        if (dialogFilter == null || !isDialogPinned(tLRPC$Dialog)) {
            i = ConnectionsManager.DEFAULT_DATACENTER_ID;
        } else {
            int size = dialogFilter.pinnedDialogs.size();
            for (int i3 = 0; i3 < size; i3++) {
                i2 = Math.min(i2, dialogFilter.pinnedDialogs.valueAt(i3));
            }
            i = i2 - this.canPinCount;
        }
        TLRPC$EncryptedChat encryptedChat = DialogObject.isEncryptedDialog(j) ? getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j))) : null;
        if (!isDialogPinned(tLRPC$Dialog)) {
            pinDialog(j, true, dialogFilter, i, true);
            getUndoView().showWithAction(0L, 78, (Object) 1, (Object) 1600, (Runnable) null, (Runnable) null);
            if (dialogFilter != null) {
                if (encryptedChat != null) {
                    if (!dialogFilter.alwaysShow.contains(Long.valueOf(encryptedChat.user_id))) {
                        dialogFilter.alwaysShow.add(Long.valueOf(encryptedChat.user_id));
                    }
                } else if (!dialogFilter.alwaysShow.contains(Long.valueOf(j))) {
                    dialogFilter.alwaysShow.add(Long.valueOf(j));
                }
            }
        } else {
            pinDialog(j, false, dialogFilter, i, true);
            getUndoView().showWithAction(0L, 79, (Object) 1, (Object) 1600, (Runnable) null, (Runnable) null);
        }
        if (dialogFilter != null) {
            z = true;
            FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, true, false, this, null);
        } else {
            z = true;
        }
        getMessagesController().reorderPinnedDialogs(this.folderId, null, 0L);
        updateCounters(z);
        if (this.viewPages != null) {
            int i4 = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i4 >= viewPageArr.length) {
                    break;
                }
                viewPageArr[i4].dialogsAdapter.onReorderStateChanged(false);
                i4++;
            }
        }
        updateVisibleRows(MessagesController.UPDATE_MASK_REORDER | MessagesController.UPDATE_MASK_CHECK);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showChatPreview$47(long j, View view) {
        boolean isDialogMuted = getMessagesController().isDialogMuted(j, 0);
        if (!isDialogMuted) {
            getNotificationsController().setDialogNotificationsSettings(j, 0, 3);
        } else {
            getNotificationsController().setDialogNotificationsSettings(j, 0, 4);
        }
        BulletinFactory.createMuteBulletin(this, !isDialogMuted, null).show();
        finishPreviewFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showChatPreview$48(ArrayList arrayList, View view) {
        performSelectedDialogsAction(arrayList, 102, false, false);
        finishPreviewFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFloatingButtonOffset() {
        this.floatingButtonContainer.setTranslationY((this.floatingButtonTranslation - this.floatingButtonPanOffset) - (Math.max(this.additionalFloatingTranslation, this.additionalFloatingTranslation2) * (1.0f - this.floatingButtonHideProgress)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasHiddenArchive() {
        return !this.onlySelect && this.initialDialogsType == 0 && this.folderId == 0 && getMessagesController().hasHiddenArchive();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean waitingForDialogsAnimationEnd(ViewPage viewPage) {
        return viewPage.dialogsItemAnimator.isRunning();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkAnimationFinished() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$checkAnimationFinished$49();
            }
        }, 300L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAnimationFinished$49() {
        ArrayList<TLRPC$Dialog> arrayList;
        if (this.viewPages != null && this.folderId != 0 && ((arrayList = this.frozenDialogsList) == null || arrayList.isEmpty())) {
            int i = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i >= viewPageArr.length) {
                    break;
                }
                viewPageArr[i].listView.setEmptyView(null);
                this.viewPages[i].progressView.setVisibility(4);
                i++;
            }
            finishFragment();
        }
        setDialogsListFrozen(false);
        updateDialogIndices();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScrollY(float f) {
        if (this.fragmentView == null) {
            return;
        }
        View view = this.scrimView;
        if (view != null) {
            view.getLocationInWindow(this.scrimViewLocation);
        }
        this.actionBar.setTranslationY(f);
        Bulletin bulletin = this.topBulletin;
        if (bulletin != null) {
            bulletin.updatePosition();
        }
        FilterTabsView filterTabsView = this.filterTabsView;
        if (filterTabsView != null) {
            filterTabsView.setTranslationY(f);
        }
        DialogsHintCell dialogsHintCell = this.dialogsHintCell;
        if (dialogsHintCell != null) {
            dialogsHintCell.setTranslationY(f);
        }
        DrawerProfileCell.AnimatedStatusView animatedStatusView = this.animatedStatusView;
        if (animatedStatusView != null) {
            animatedStatusView.translateY2((int) f);
            this.animatedStatusView.setAlpha(1.0f - ((-f) / ActionBar.getCurrentActionBarHeight()));
        }
        updateContextViewPosition();
        if (this.viewPages != null) {
            int i = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i >= viewPageArr.length) {
                    break;
                }
                viewPageArr[i].listView.setTopGlowOffset(this.viewPages[i].listView.getPaddingTop() + ((int) f));
                i++;
            }
        }
        this.fragmentView.invalidate();
    }

    private void prepareBlurBitmap() {
        if (this.blurredView == null) {
            return;
        }
        int measuredWidth = (int) (this.fragmentView.getMeasuredWidth() / 6.0f);
        int measuredHeight = (int) (this.fragmentView.getMeasuredHeight() / 6.0f);
        Bitmap createBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.scale(0.16666667f, 0.16666667f);
        this.fragmentView.draw(canvas);
        Utilities.stackBlurBitmap(createBitmap, Math.max(7, Math.max(measuredWidth, measuredHeight) / 180));
        this.blurredView.setBackground(new BitmapDrawable(createBitmap));
        this.blurredView.setAlpha(0.0f);
        this.blurredView.setVisibility(0);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationProgress(boolean z, float f) {
        RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment()) {
            this.rightSlidingDialogContainer.getFragment().onTransitionAnimationProgress(z, f);
            return;
        }
        View view = this.blurredView;
        if (view == null || view.getVisibility() != 0) {
            return;
        }
        if (z) {
            this.blurredView.setAlpha(1.0f - f);
        } else {
            this.blurredView.setAlpha(f);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        View view;
        RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer != null && rightSlidingDialogContainer.hasFragment()) {
            this.rightSlidingDialogContainer.getFragment().onTransitionAnimationEnd(z, z2);
            return;
        }
        if (z && (view = this.blurredView) != null && view.getVisibility() == 0) {
            this.blurredView.setVisibility(8);
            this.blurredView.setBackground(null);
        }
        if (z && this.afterSignup) {
            try {
                this.fragmentView.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            if (getParentActivity() instanceof LaunchActivity) {
                ((LaunchActivity) getParentActivity()).getFireworksOverlay().start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetScroll() {
        if (this.actionBar.getTranslationY() == 0.0f) {
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(this, this.SCROLL_Y, 0.0f));
        animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
        animatorSet.setDuration(250L);
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideActionMode(boolean z) {
        this.actionBar.hideActionMode();
        if (this.menuDrawable != null) {
            this.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrOpenMenu", R.string.AccDescrOpenMenu));
        }
        this.selectedDialogs.clear();
        MenuDrawable menuDrawable = this.menuDrawable;
        if (menuDrawable != null) {
            menuDrawable.setRotation(0.0f, true);
        } else {
            BackDrawable backDrawable = this.backDrawable;
            if (backDrawable != null) {
                backDrawable.setRotation(0.0f, true);
            }
        }
        FilterTabsView filterTabsView = this.filterTabsView;
        if (filterTabsView != null) {
            filterTabsView.animateColorsTo("actionBarTabLine", "actionBarTabActiveText", "actionBarTabUnactiveText", "actionBarTabSelector", "actionBarDefault");
        }
        ValueAnimator valueAnimator = this.actionBarColorAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.progressToActionMode, 0.0f);
        this.actionBarColorAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                DialogsActivity.this.lambda$hideActionMode$50(valueAnimator2);
            }
        });
        this.actionBarColorAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.actionBarColorAnimator.setDuration(200L);
        this.actionBarColorAnimator.start();
        this.allowMoving = false;
        if (!this.movingDialogFilters.isEmpty()) {
            int i = 0;
            for (int size = this.movingDialogFilters.size(); i < size; size = size) {
                MessagesController.DialogFilter dialogFilter = this.movingDialogFilters.get(i);
                FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, true, false, this, null);
                i++;
            }
            this.movingDialogFilters.clear();
        }
        if (this.movingWas) {
            getMessagesController().reorderPinnedDialogs(this.folderId, null, 0L);
            this.movingWas = false;
        }
        updateCounters(true);
        if (this.viewPages != null) {
            int i2 = 0;
            while (true) {
                ViewPage[] viewPageArr = this.viewPages;
                if (i2 >= viewPageArr.length) {
                    break;
                }
                viewPageArr[i2].dialogsAdapter.onReorderStateChanged(false);
                i2++;
            }
        }
        updateVisibleRows(MessagesController.UPDATE_MASK_REORDER | MessagesController.UPDATE_MASK_CHECK | (z ? MessagesController.UPDATE_MASK_CHAT : 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideActionMode$50(ValueAnimator valueAnimator) {
        this.progressToActionMode = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        for (int i = 0; i < this.actionBar.getChildCount(); i++) {
            if (this.actionBar.getChildAt(i).getVisibility() == 0 && this.actionBar.getChildAt(i) != this.actionBar.getActionMode() && this.actionBar.getChildAt(i) != this.actionBar.getBackButton()) {
                this.actionBar.getChildAt(i).setAlpha(1.0f - this.progressToActionMode);
            }
        }
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
    }

    private int getPinnedCount() {
        ArrayList<TLRPC$Dialog> dialogs;
        if ((this.viewPages[0].dialogsType == 7 || this.viewPages[0].dialogsType == 8) && (!this.actionBar.isActionModeShowed() || this.actionBar.isActionModeShowed(null))) {
            dialogs = getDialogsArray(this.currentAccount, this.viewPages[0].dialogsType, this.folderId, this.dialogsListFrozen);
        } else {
            dialogs = getMessagesController().getDialogs(this.folderId);
        }
        int size = dialogs.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            TLRPC$Dialog tLRPC$Dialog = dialogs.get(i2);
            if (!(tLRPC$Dialog instanceof TLRPC$TL_dialogFolder)) {
                if (isDialogPinned(tLRPC$Dialog)) {
                    i++;
                } else if (!getMessagesController().isPromoDialog(tLRPC$Dialog.id, false)) {
                    break;
                }
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDialogPinned(TLRPC$Dialog tLRPC$Dialog) {
        if (tLRPC$Dialog == null) {
            return false;
        }
        MessagesController.DialogFilter dialogFilter = null;
        if ((this.viewPages[0].dialogsType == 7 || this.viewPages[0].dialogsType == 8) && (!this.actionBar.isActionModeShowed() || this.actionBar.isActionModeShowed(null))) {
            dialogFilter = getMessagesController().selectedDialogFilter[this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
        }
        if (dialogFilter != null) {
            return dialogFilter.pinnedDialogs.indexOfKey(tLRPC$Dialog.id) >= 0;
        }
        return tLRPC$Dialog.pinned;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:227:0x048a  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x04e4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void performSelectedDialogsAction(ArrayList<Long> arrayList, final int i, boolean z, boolean z2) {
        MessagesController.DialogFilter dialogFilter;
        boolean z3;
        int i2;
        int i3;
        int i4;
        int i5;
        long j;
        Theme.ResourcesProvider resourcesProvider;
        boolean z4;
        TLRPC$Chat chat;
        TLRPC$EncryptedChat tLRPC$EncryptedChat;
        TLRPC$User tLRPC$User;
        int i6;
        boolean z5;
        String str;
        int i7;
        TLRPC$User tLRPC$TL_userEmpty;
        String string;
        final ArrayList<Long> arrayList2 = arrayList;
        if (getParentActivity() == null) {
            return;
        }
        boolean z6 = false;
        boolean z7 = (this.viewPages[0].dialogsType == 7 || this.viewPages[0].dialogsType == 8) && (!this.actionBar.isActionModeShowed() || this.actionBar.isActionModeShowed(null));
        if (z7) {
            dialogFilter = getMessagesController().selectedDialogFilter[this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
        } else {
            dialogFilter = null;
        }
        int size = arrayList.size();
        if (i == 105 || i == 107) {
            final ArrayList<Long> arrayList3 = new ArrayList<>(arrayList);
            getMessagesController().addDialogToFolder(arrayList3, this.canUnarchiveCount == 0 ? 1 : 0, -1, null, 0L);
            if (this.canUnarchiveCount == 0) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                boolean z8 = globalMainSettings.getBoolean("archivehint_l", false) || SharedConfig.archiveHidden;
                if (z8) {
                    i2 = 1;
                } else {
                    i2 = 1;
                    globalMainSettings.edit().putBoolean("archivehint_l", true).commit();
                }
                if (z8) {
                    i3 = arrayList3.size() > i2 ? 4 : 2;
                } else {
                    i3 = arrayList3.size() > i2 ? 5 : 3;
                }
                getUndoView().showWithAction(0L, i3, null, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda58
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.this.lambda$performSelectedDialogsAction$51(arrayList3);
                    }
                });
            } else {
                ArrayList<TLRPC$Dialog> dialogs = getMessagesController().getDialogs(this.folderId);
                if (this.viewPages != null && dialogs.isEmpty()) {
                    z3 = false;
                    this.viewPages[0].listView.setEmptyView(null);
                    this.viewPages[0].progressView.setVisibility(4);
                    finishFragment();
                    hideActionMode(z3);
                    return;
                }
            }
            z3 = false;
            hideActionMode(z3);
            return;
        }
        String str2 = "Cancel";
        if ((i == 100 || i == 108) && this.canPinCount != 0) {
            ArrayList<TLRPC$Dialog> dialogs2 = getMessagesController().getDialogs(this.folderId);
            int size2 = dialogs2.size();
            int i8 = 0;
            int i9 = 0;
            for (int i10 = 0; i10 < size2; i10++) {
                TLRPC$Dialog tLRPC$Dialog = dialogs2.get(i10);
                if (!(tLRPC$Dialog instanceof TLRPC$TL_dialogFolder)) {
                    if (isDialogPinned(tLRPC$Dialog)) {
                        if (DialogObject.isEncryptedDialog(tLRPC$Dialog.id)) {
                            i9++;
                        } else {
                            i8++;
                        }
                    } else if (!getMessagesController().isPromoDialog(tLRPC$Dialog.id, false)) {
                        break;
                    }
                }
            }
            int i11 = 0;
            int i12 = 0;
            int i13 = 0;
            for (int i14 = 0; i14 < size; i14++) {
                long longValue = arrayList2.get(i14).longValue();
                TLRPC$Dialog tLRPC$Dialog2 = getMessagesController().dialogs_dict.get(longValue);
                if (tLRPC$Dialog2 != null && !isDialogPinned(tLRPC$Dialog2)) {
                    if (DialogObject.isEncryptedDialog(longValue)) {
                        i12++;
                    } else {
                        i11++;
                    }
                    if (dialogFilter != null && dialogFilter.alwaysShow.contains(Long.valueOf(longValue))) {
                        i13++;
                    }
                }
            }
            if (z7) {
                i4 = 100 - dialogFilter.alwaysShow.size();
            } else if (this.folderId != 0 || dialogFilter != null) {
                i4 = getMessagesController().maxFolderPinnedDialogsCount;
            } else {
                i4 = getUserConfig().isPremium() ? getMessagesController().dialogFiltersPinnedLimitPremium : getMessagesController().dialogFiltersPinnedLimitDefault;
            }
            if (i12 + i9 > i4 || (i11 + i8) - i13 > i4) {
                if (this.folderId == 0 && dialogFilter == null) {
                    showDialog(new LimitReachedBottomSheet(this, getParentActivity(), 0, this.currentAccount));
                    return;
                } else {
                    AlertsCreator.showSimpleAlert(this, LocaleController.formatString("PinFolderLimitReached", R.string.PinFolderLimitReached, LocaleController.formatPluralString("Chats", i4, new Object[0])));
                    return;
                }
            }
        } else if ((i == 102 || i == 103) && size > 1 && z) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            if (i == 102) {
                builder.setTitle(LocaleController.formatString("DeleteFewChatsTitle", R.string.DeleteFewChatsTitle, LocaleController.formatPluralString("ChatsSelected", size, new Object[0])));
                builder.setMessage(LocaleController.getString("AreYouSureDeleteFewChats", R.string.AreYouSureDeleteFewChats));
            } else if (this.canClearCacheCount != 0) {
                builder.setTitle(LocaleController.formatString("ClearCacheFewChatsTitle", R.string.ClearCacheFewChatsTitle, LocaleController.formatPluralString("ChatsSelectedClearCache", size, new Object[0])));
                builder.setMessage(LocaleController.getString("AreYouSureClearHistoryCacheFewChats", R.string.AreYouSureClearHistoryCacheFewChats));
            } else {
                builder.setTitle(LocaleController.formatString("ClearFewChatsTitle", R.string.ClearFewChatsTitle, LocaleController.formatPluralString("ChatsSelectedClear", size, new Object[0])));
                builder.setMessage(LocaleController.getString("AreYouSureClearHistoryFewChats", R.string.AreYouSureClearHistoryFewChats));
            }
            if (i == 102) {
                string = LocaleController.getString("Delete", R.string.Delete);
            } else {
                string = this.canClearCacheCount != 0 ? LocaleController.getString("ClearHistoryCache", R.string.ClearHistoryCache) : LocaleController.getString("ClearHistory", R.string.ClearHistory);
            }
            builder.setPositiveButton(string, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda17
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i15) {
                    DialogsActivity.this.lambda$performSelectedDialogsAction$53(arrayList2, i, dialogInterface, i15);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog create = builder.create();
            showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor("dialogTextRed2"));
                return;
            }
            return;
        } else if (i == 106 && z) {
            AlertsCreator.createBlockDialogAlert(this, size, this.canReportSpamCount != 0, size == 1 ? getMessagesController().getUser(Long.valueOf(arrayList2.get(0).longValue())) : null, new AlertsCreator.BlockDialogCallback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda87
                @Override // org.telegram.ui.Components.AlertsCreator.BlockDialogCallback
                public final void run(boolean z9, boolean z10) {
                    DialogsActivity.this.lambda$performSelectedDialogsAction$54(arrayList2, z9, z10);
                }
            });
            return;
        }
        int i15 = ConnectionsManager.DEFAULT_DATACENTER_ID;
        if (dialogFilter != null && ((i == 100 || i == 108) && this.canPinCount != 0)) {
            int size3 = dialogFilter.pinnedDialogs.size();
            for (int i16 = 0; i16 < size3; i16++) {
                i15 = Math.min(i15, dialogFilter.pinnedDialogs.valueAt(i16));
            }
            i15 -= this.canPinCount;
        }
        int i17 = i15;
        int i18 = 0;
        int i19 = 0;
        while (i18 < size) {
            final long longValue2 = arrayList2.get(i18).longValue();
            TLRPC$Dialog tLRPC$Dialog3 = getMessagesController().dialogs_dict.get(longValue2);
            if (tLRPC$Dialog3 != null) {
                if (DialogObject.isEncryptedDialog(longValue2)) {
                    TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(longValue2)));
                    if (encryptedChat != null) {
                        tLRPC$TL_userEmpty = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                    } else {
                        tLRPC$TL_userEmpty = new TLRPC$TL_userEmpty();
                    }
                    tLRPC$User = tLRPC$TL_userEmpty;
                    tLRPC$EncryptedChat = encryptedChat;
                } else if (DialogObject.isUserDialog(longValue2)) {
                    tLRPC$User = getMessagesController().getUser(Long.valueOf(longValue2));
                    tLRPC$EncryptedChat = null;
                } else {
                    chat = getMessagesController().getChat(Long.valueOf(-longValue2));
                    tLRPC$EncryptedChat = null;
                    tLRPC$User = null;
                    if (chat == null || tLRPC$User != null) {
                        if (tLRPC$User == null && tLRPC$User.bot && !MessagesController.isSupportUser(tLRPC$User)) {
                            i6 = 100;
                            z5 = true;
                        } else {
                            i6 = 100;
                            z5 = false;
                        }
                        if (i != i6 || i == 108) {
                            str = str2;
                            if (this.canPinCount == 0) {
                                if (!isDialogPinned(tLRPC$Dialog3)) {
                                    i19++;
                                    TLRPC$EncryptedChat tLRPC$EncryptedChat2 = tLRPC$EncryptedChat;
                                    pinDialog(longValue2, true, dialogFilter, i17, size == 1);
                                    if (dialogFilter != null) {
                                        i17++;
                                        if (tLRPC$EncryptedChat2 != null) {
                                            if (!dialogFilter.alwaysShow.contains(Long.valueOf(tLRPC$EncryptedChat2.user_id))) {
                                                dialogFilter.alwaysShow.add(Long.valueOf(tLRPC$EncryptedChat2.user_id));
                                            }
                                        } else if (!dialogFilter.alwaysShow.contains(Long.valueOf(tLRPC$Dialog3.id))) {
                                            dialogFilter.alwaysShow.add(Long.valueOf(tLRPC$Dialog3.id));
                                        }
                                    }
                                }
                            } else if (isDialogPinned(tLRPC$Dialog3)) {
                                i19++;
                                pinDialog(longValue2, false, dialogFilter, i17, size == 1);
                            }
                        } else if (i == 101) {
                            if (this.canReadCount != 0) {
                                markAsRead(longValue2);
                            } else {
                                markAsUnread(longValue2);
                            }
                        } else {
                            if (i == 102) {
                                i7 = 1;
                            } else if (i == 103) {
                                i7 = 1;
                            } else if (i == 104) {
                                if (size == 1 && this.canMuteCount == 1) {
                                    showDialog(AlertsCreator.createMuteAlert(this, longValue2, z6 ? 1 : 0, (Theme.ResourcesProvider) null), new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda20
                                        @Override // android.content.DialogInterface.OnDismissListener
                                        public final void onDismiss(DialogInterface dialogInterface) {
                                            DialogsActivity.this.lambda$performSelectedDialogsAction$58(dialogInterface);
                                        }
                                    });
                                    return;
                                } else if (this.canUnmuteCount != 0) {
                                    if (getMessagesController().isDialogMuted(longValue2, z6 ? 1 : 0)) {
                                        getNotificationsController().setDialogNotificationsSettings(longValue2, z6 ? 1 : 0, 4);
                                    }
                                } else if (z2) {
                                    showDialog(AlertsCreator.createMuteAlert(this, arrayList2, z6 ? 1 : 0, (Theme.ResourcesProvider) null), new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda19
                                        @Override // android.content.DialogInterface.OnDismissListener
                                        public final void onDismiss(DialogInterface dialogInterface) {
                                            DialogsActivity.this.lambda$performSelectedDialogsAction$59(dialogInterface);
                                        }
                                    });
                                    return;
                                } else if (!getMessagesController().isDialogMuted(longValue2, z6 ? 1 : 0)) {
                                    getNotificationsController().setDialogNotificationsSettings(longValue2, z6 ? 1 : 0, 3);
                                }
                            }
                            if (size == i7) {
                                if (i == 102 && this.canDeletePsaSelected) {
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                                    builder2.setTitle(LocaleController.getString("PsaHideChatAlertTitle", R.string.PsaHideChatAlertTitle));
                                    builder2.setMessage(LocaleController.getString("PsaHideChatAlertText", R.string.PsaHideChatAlertText));
                                    builder2.setPositiveButton(LocaleController.getString("PsaHide", R.string.PsaHide), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda14
                                        @Override // android.content.DialogInterface.OnClickListener
                                        public final void onClick(DialogInterface dialogInterface, int i20) {
                                            DialogsActivity.this.lambda$performSelectedDialogsAction$55(dialogInterface, i20);
                                        }
                                    });
                                    builder2.setNegativeButton(LocaleController.getString(str2, R.string.Cancel), null);
                                    showDialog(builder2.create());
                                    return;
                                }
                                boolean z9 = i == 103;
                                boolean isEncryptedDialog = DialogObject.isEncryptedDialog(tLRPC$Dialog3.id);
                                if (i == 102) {
                                    z6 = true;
                                }
                                TLRPC$User tLRPC$User2 = tLRPC$User;
                                final TLRPC$Chat tLRPC$Chat = chat;
                                final boolean z10 = z5;
                                AlertsCreator.createClearOrDeleteDialogAlert(this, z9, chat, tLRPC$User2, isEncryptedDialog, z6, new MessagesStorage.BooleanCallback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda71
                                    @Override // org.telegram.messenger.MessagesStorage.BooleanCallback
                                    public final void run(boolean z11) {
                                        DialogsActivity.this.lambda$performSelectedDialogsAction$57(i, tLRPC$Chat, longValue2, z10, z11);
                                    }
                                });
                                return;
                            }
                            if (getMessagesController().isPromoDialog(longValue2, true)) {
                                getMessagesController().hidePromoDialog();
                            } else if (i == 103 && this.canClearCacheCount != 0) {
                                getMessagesController().deleteDialog(longValue2, 2, z6);
                            } else {
                                str = str2;
                                lambda$performSelectedDialogsAction$56(i, longValue2, chat, z5, false);
                            }
                            str = str2;
                        }
                        i18++;
                        arrayList2 = arrayList;
                        str2 = str;
                        z6 = false;
                    }
                }
                chat = null;
                if (chat == null) {
                }
                if (tLRPC$User == null) {
                }
                i6 = 100;
                z5 = false;
                if (i != i6) {
                }
                str = str2;
                if (this.canPinCount == 0) {
                }
                i18++;
                arrayList2 = arrayList;
                str2 = str;
                z6 = false;
            }
            str = str2;
            i18++;
            arrayList2 = arrayList;
            str2 = str;
            z6 = false;
        }
        if (i == 104 && (size != 1 || this.canMuteCount != 1)) {
            if (this.canUnmuteCount == 0) {
                resourcesProvider = null;
                z4 = true;
            } else {
                resourcesProvider = null;
                z4 = false;
            }
            BulletinFactory.createMuteBulletin(this, z4, resourcesProvider).show();
        }
        if (i == 100 || i == 108) {
            if (dialogFilter != null) {
                i5 = 100;
                FilterCreateActivity.saveFilterToServer(dialogFilter, dialogFilter.flags, dialogFilter.name, dialogFilter.alwaysShow, dialogFilter.neverShow, dialogFilter.pinnedDialogs, false, false, true, true, false, this, null);
                j = 0;
            } else {
                i5 = 100;
                j = 0;
                getMessagesController().reorderPinnedDialogs(this.folderId, null, 0L);
            }
            if (this.searchIsShowed) {
                getUndoView().showWithAction(j, this.canPinCount != 0 ? 78 : 79, Integer.valueOf(i19));
            }
        } else {
            i5 = 100;
        }
        hideActionMode((i == 108 || i == i5 || i == 102) ? false : true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$51(ArrayList arrayList) {
        getMessagesController().addDialogToFolder(arrayList, this.folderId == 0 ? 0 : 1, -1, null, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$53(ArrayList arrayList, final int i, DialogInterface dialogInterface, int i2) {
        if (arrayList.isEmpty()) {
            return;
        }
        final ArrayList<Long> arrayList2 = new ArrayList<>(arrayList);
        getUndoView().showWithAction(arrayList2, i == 102 ? 27 : 26, (Object) null, (Object) null, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$performSelectedDialogsAction$52(i, arrayList2);
            }
        }, (Runnable) null);
        hideActionMode(i == 103);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$52(int i, ArrayList arrayList) {
        if (i == 102) {
            getMessagesController().setDialogsInTransaction(true);
            performSelectedDialogsAction(arrayList, i, false, false);
            getMessagesController().setDialogsInTransaction(false);
            getMessagesController().checkIfFolderEmpty(this.folderId);
            if (this.folderId == 0 || getDialogsArray(this.currentAccount, this.viewPages[0].dialogsType, this.folderId, false).size() != 0) {
                return;
            }
            this.viewPages[0].listView.setEmptyView(null);
            this.viewPages[0].progressView.setVisibility(4);
            finishFragment();
            return;
        }
        performSelectedDialogsAction(arrayList, i, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$54(ArrayList arrayList, boolean z, boolean z2) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            long longValue = ((Long) arrayList.get(i)).longValue();
            if (z) {
                getMessagesController().reportSpam(longValue, getMessagesController().getUser(Long.valueOf(longValue)), null, null, false);
            }
            if (z2) {
                getMessagesController().deleteDialog(longValue, 0, true);
            }
            getMessagesController().blockPeer(longValue);
        }
        hideActionMode(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$55(DialogInterface dialogInterface, int i) {
        getMessagesController().hidePromoDialog();
        hideActionMode(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$57(final int i, final TLRPC$Chat tLRPC$Chat, final long j, final boolean z, final boolean z2) {
        int i2;
        int i3;
        int i4;
        ArrayList<TLRPC$Dialog> arrayList;
        int i5;
        hideActionMode(false);
        if (i == 103 && ChatObject.isChannel(tLRPC$Chat)) {
            if (!tLRPC$Chat.megagroup || ChatObject.isPublic(tLRPC$Chat)) {
                getMessagesController().deleteDialog(j, 2, z2);
                return;
            }
        }
        if (i == 102 && this.folderId != 0 && getDialogsArray(this.currentAccount, this.viewPages[0].dialogsType, this.folderId, false).size() == 1) {
            this.viewPages[0].progressView.setVisibility(4);
        }
        this.debugLastUpdateAction = 3;
        if (i == 102) {
            setDialogsListFrozen(true);
            if (this.frozenDialogsList != null) {
                i5 = 0;
                while (i5 < this.frozenDialogsList.size()) {
                    if (this.frozenDialogsList.get(i5).id == j) {
                        break;
                    }
                    i5++;
                }
            }
            i5 = -1;
            checkAnimationFinished();
            i2 = i5;
        } else {
            i2 = -1;
        }
        int i6 = i2;
        getUndoView().showWithAction(j, i == 103 ? 0 : 1, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda53
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$performSelectedDialogsAction$56(i, j, tLRPC$Chat, z, z2);
            }
        });
        ArrayList arrayList2 = new ArrayList(getDialogsArray(this.currentAccount, this.viewPages[0].dialogsType, this.folderId, false));
        int i7 = 0;
        while (true) {
            if (i7 >= arrayList2.size()) {
                i3 = 102;
                i4 = -1;
                break;
            } else if (((TLRPC$Dialog) arrayList2.get(i7)).id == j) {
                i4 = i7;
                i3 = 102;
                break;
            } else {
                i7++;
            }
        }
        if (i == i3) {
            if (i6 >= 0 && i4 < 0 && (arrayList = this.frozenDialogsList) != null) {
                arrayList.remove(i6);
                this.viewPages[0].dialogsItemAnimator.prepareForRemove();
                this.viewPages[0].updateList(true);
                return;
            }
            setDialogsListFrozen(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$58(DialogInterface dialogInterface) {
        hideActionMode(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSelectedDialogsAction$59(DialogInterface dialogInterface) {
        hideActionMode(true);
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:47:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void markAsRead(long j) {
        int i;
        TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(j);
        MessagesController.DialogFilter dialogFilter = null;
        if ((this.viewPages[0].dialogsType == 7 || this.viewPages[0].dialogsType == 8) && (!this.actionBar.isActionModeShowed() || this.actionBar.isActionModeShowed(null))) {
            dialogFilter = getMessagesController().selectedDialogFilter[this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
        }
        this.debugLastUpdateAction = 2;
        int i2 = -1;
        if (dialogFilter != null && (dialogFilter.flags & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ) != 0 && !dialogFilter.alwaysShow(this.currentAccount, tLRPC$Dialog)) {
            setDialogsListFrozen(true);
            checkAnimationFinished();
            if (this.frozenDialogsList != null) {
                int i3 = 0;
                while (true) {
                    if (i3 >= this.frozenDialogsList.size()) {
                        break;
                    } else if (this.frozenDialogsList.get(i3).id == j) {
                        i2 = i3;
                        break;
                    } else {
                        i3++;
                    }
                }
                if (i2 < 0) {
                    setDialogsListFrozen(false, false);
                }
                i = i2;
                if (getMessagesController().isForum(j)) {
                    getMessagesController().markAllTopicsAsRead(j);
                }
                getMessagesController().markMentionsAsRead(j, 0);
                MessagesController messagesController = getMessagesController();
                int i4 = tLRPC$Dialog.top_message;
                messagesController.markDialogAsRead(j, i4, i4, tLRPC$Dialog.last_message_date, false, 0, 0, true, 0);
                if (i < 0) {
                    this.frozenDialogsList.remove(i);
                    this.viewPages[0].dialogsItemAnimator.prepareForRemove();
                    this.viewPages[0].updateList(true);
                    return;
                }
                return;
            }
        }
        i = -1;
        if (getMessagesController().isForum(j)) {
        }
        getMessagesController().markMentionsAsRead(j, 0);
        MessagesController messagesController2 = getMessagesController();
        int i42 = tLRPC$Dialog.top_message;
        messagesController2.markDialogAsRead(j, i42, i42, tLRPC$Dialog.last_message_date, false, 0, 0, true, 0);
        if (i < 0) {
        }
    }

    private void markAsUnread(long j) {
        getMessagesController().markDialogAsUnread(j, null, 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: performDeleteOrClearDialogAction */
    public void lambda$performSelectedDialogsAction$56(int i, long j, TLRPC$Chat tLRPC$Chat, boolean z, boolean z2) {
        if (i == 103) {
            getMessagesController().deleteDialog(j, 1, z2);
            return;
        }
        if (tLRPC$Chat != null) {
            if (ChatObject.isNotInChat(tLRPC$Chat)) {
                getMessagesController().deleteDialog(j, 0, z2);
            } else {
                getMessagesController().deleteParticipantFromChat((int) (-j), getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId())), null, z2, false);
            }
        } else {
            getMessagesController().deleteDialog(j, 0, z2);
            if (z) {
                getMessagesController().blockPeer((int) j);
            }
        }
        if (AndroidUtilities.isTablet()) {
            getNotificationCenter().postNotificationName(NotificationCenter.closeChats, Long.valueOf(j));
        }
        getMessagesController().checkIfFolderEmpty(this.folderId);
    }

    /* JADX WARN: Removed duplicated region for block: B:76:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:83:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void pinDialog(long j, boolean z, MessagesController.DialogFilter dialogFilter, int i, boolean z2) {
        int i2;
        boolean z3;
        boolean pinDialog;
        boolean z4 = true;
        int i3 = (this.viewPages[0].dialogsType == 0 && hasHiddenArchive() && this.viewPages[0].archivePullViewState == 2) ? 1 : 0;
        int findFirstVisibleItemPosition = this.viewPages[0].layoutManager.findFirstVisibleItemPosition();
        if (dialogFilter != null) {
            int i4 = dialogFilter.pinnedDialogs.get(j, Integer.MIN_VALUE);
            if (!z && i4 == Integer.MIN_VALUE) {
                return;
            }
        }
        this.debugLastUpdateAction = z ? 4 : 5;
        int i5 = -1;
        if (findFirstVisibleItemPosition > i3 || !z2) {
            i2 = -1;
            z3 = true;
        } else {
            setDialogsListFrozen(true);
            checkAnimationFinished();
            if (this.frozenDialogsList != null) {
                for (int i6 = 0; i6 < this.frozenDialogsList.size(); i6++) {
                    if (this.frozenDialogsList.get(i6).id == j) {
                        i2 = i6;
                        break;
                    }
                }
            }
            i2 = -1;
            z3 = false;
        }
        if (dialogFilter != null) {
            if (z) {
                dialogFilter.pinnedDialogs.put(j, i);
            } else {
                dialogFilter.pinnedDialogs.delete(j);
            }
            if (z2) {
                getMessagesController().onFilterUpdate(dialogFilter);
            }
            pinDialog = true;
        } else {
            pinDialog = getMessagesController().pinDialog(j, z, null, -1L);
        }
        if (pinDialog) {
            if (!z3) {
                ArrayList<TLRPC$Dialog> dialogsArray = getDialogsArray(this.currentAccount, this.viewPages[0].dialogsType, this.folderId, false);
                int i7 = 0;
                while (true) {
                    if (i7 >= dialogsArray.size()) {
                        break;
                    } else if (dialogsArray.get(i7).id == j) {
                        i5 = i7;
                        break;
                    } else {
                        i7++;
                    }
                }
            } else {
                if (this.initialDialogsType != 10) {
                    hideFloatingButton(false);
                }
                scrollToTop();
            }
        }
        if (z3) {
            return;
        }
        if (i2 >= 0) {
            ArrayList<TLRPC$Dialog> arrayList = this.frozenDialogsList;
            if (arrayList != null && i5 >= 0 && i2 != i5) {
                arrayList.add(i5, arrayList.remove(i2));
                this.viewPages[0].dialogsItemAnimator.prepareForRemove();
                this.viewPages[0].updateList(true);
                this.viewPages[0].layoutManager.scrollToPositionWithOffset((this.viewPages[0].dialogsType == 0 && hasHiddenArchive() && this.viewPages[0].archivePullViewState == 2) ? 1 : 0, (int) this.actionBar.getTranslationY());
            } else if (i5 >= 0 && i2 == i5) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda49
                    @Override // java.lang.Runnable
                    public final void run() {
                        DialogsActivity.this.lambda$pinDialog$60();
                    }
                }, 200L);
            }
            if (z4) {
                setDialogsListFrozen(false);
                return;
            }
            return;
        }
        z4 = false;
        if (z4) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pinDialog$60() {
        setDialogsListFrozen(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scrollToTop() {
        int findFirstVisibleItemPosition = this.viewPages[0].layoutManager.findFirstVisibleItemPosition() * AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 78.0f : 72.0f);
        int i = (this.viewPages[0].dialogsType == 0 && hasHiddenArchive() && this.viewPages[0].archivePullViewState == 2) ? 1 : 0;
        this.viewPages[0].listView.getItemAnimator();
        if (findFirstVisibleItemPosition >= this.viewPages[0].listView.getMeasuredHeight() * 1.2f) {
            this.viewPages[0].scrollHelper.setScrollDirection(1);
            this.viewPages[0].scrollHelper.scrollToPosition(i, 0, false, true);
            resetScroll();
            return;
        }
        this.viewPages[0].listView.smoothScrollToPosition(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCounters(boolean z) {
        int i;
        int i2;
        int i3;
        int i4;
        TLRPC$User user;
        this.canDeletePsaSelected = false;
        this.canUnarchiveCount = 0;
        this.canUnmuteCount = 0;
        this.canMuteCount = 0;
        this.canPinCount = 0;
        this.canReadCount = 0;
        this.forumCount = 0;
        this.canClearCacheCount = 0;
        this.canReportSpamCount = 0;
        if (z) {
            return;
        }
        int size = this.selectedDialogs.size();
        long clientUserId = getUserConfig().getClientUserId();
        SharedPreferences notificationsSettings = getNotificationsSettings();
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        while (i5 < size) {
            TLRPC$Dialog tLRPC$Dialog = getMessagesController().dialogs_dict.get(this.selectedDialogs.get(i5).longValue());
            if (tLRPC$Dialog == null) {
                i2 = size;
                i3 = i5;
            } else {
                long j = tLRPC$Dialog.id;
                boolean isDialogPinned = isDialogPinned(tLRPC$Dialog);
                i2 = size;
                boolean z2 = tLRPC$Dialog.unread_count != 0 || tLRPC$Dialog.unread_mark;
                if (getMessagesController().isForum(j)) {
                    i3 = i5;
                    this.forumCount++;
                } else {
                    i3 = i5;
                }
                if (getMessagesController().isDialogMuted(j, 0)) {
                    i4 = 1;
                    this.canUnmuteCount++;
                } else {
                    i4 = 1;
                    this.canMuteCount++;
                }
                if (z2) {
                    this.canReadCount += i4;
                }
                if (this.folderId == i4 || tLRPC$Dialog.folder_id == i4) {
                    this.canUnarchiveCount++;
                } else if (j != clientUserId && j != 777000 && !getMessagesController().isPromoDialog(j, false)) {
                    i8++;
                }
                if (!DialogObject.isUserDialog(j) || j == clientUserId || MessagesController.isSupportUser(getMessagesController().getUser(Long.valueOf(j)))) {
                    i10++;
                } else {
                    if (notificationsSettings.getBoolean("dialog_bar_report" + j, true)) {
                        this.canReportSpamCount++;
                    }
                }
                if (DialogObject.isChannel(tLRPC$Dialog)) {
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j));
                    if (getMessagesController().isPromoDialog(tLRPC$Dialog.id, true)) {
                        this.canClearCacheCount++;
                        if (getMessagesController().promoDialogType == MessagesController.PROMO_TYPE_PSA) {
                            i6++;
                            this.canDeletePsaSelected = true;
                        }
                    } else {
                        if (isDialogPinned) {
                            i9++;
                        } else {
                            this.canPinCount++;
                        }
                        if (chat != null && chat.megagroup) {
                            if (ChatObject.isPublic(chat)) {
                                this.canClearCacheCount++;
                            }
                        } else {
                            this.canClearCacheCount++;
                        }
                        i6++;
                    }
                } else {
                    boolean isChatDialog = DialogObject.isChatDialog(tLRPC$Dialog.id);
                    if (isChatDialog) {
                        getMessagesController().getChat(Long.valueOf(-tLRPC$Dialog.id));
                    }
                    if (DialogObject.isEncryptedDialog(tLRPC$Dialog.id)) {
                        TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(tLRPC$Dialog.id)));
                        if (encryptedChat != null) {
                            user = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
                        } else {
                            user = new TLRPC$TL_userEmpty();
                        }
                    } else {
                        user = (isChatDialog || !DialogObject.isUserDialog(tLRPC$Dialog.id)) ? null : getMessagesController().getUser(Long.valueOf(tLRPC$Dialog.id));
                    }
                    if (user != null && user.bot) {
                        MessagesController.isSupportUser(user);
                    }
                    if (isDialogPinned) {
                        i9++;
                    } else {
                        this.canPinCount++;
                    }
                }
                i7++;
                i6++;
            }
            i5 = i3 + 1;
            size = i2;
        }
        if (i6 != size) {
            this.deleteItem.setVisibility(8);
        } else {
            this.deleteItem.setVisibility(0);
        }
        int i11 = this.canClearCacheCount;
        if ((i11 != 0 && i11 != size) || (i7 != 0 && i7 != size)) {
            this.clearItem.setVisibility(8);
        } else {
            this.clearItem.setVisibility(0);
            if (this.canClearCacheCount != 0) {
                this.clearItem.setText(LocaleController.getString("ClearHistoryCache", R.string.ClearHistoryCache));
            } else {
                this.clearItem.setText(LocaleController.getString("ClearHistory", R.string.ClearHistory));
            }
        }
        if (this.canUnarchiveCount != 0) {
            String string = LocaleController.getString("Unarchive", R.string.Unarchive);
            ActionBarMenuSubItem actionBarMenuSubItem = this.archiveItem;
            int i12 = R.drawable.msg_unarchive;
            actionBarMenuSubItem.setTextAndIcon(string, i12);
            this.archive2Item.setIcon(i12);
            this.archive2Item.setContentDescription(string);
            FilterTabsView filterTabsView = this.filterTabsView;
            if (filterTabsView != null && filterTabsView.getVisibility() == 0) {
                this.archive2Item.setVisibility(0);
                this.archiveItem.setVisibility(8);
            } else {
                this.archiveItem.setVisibility(0);
                this.archive2Item.setVisibility(8);
            }
        } else if (i8 != 0) {
            String string2 = LocaleController.getString("Archive", R.string.Archive);
            ActionBarMenuSubItem actionBarMenuSubItem2 = this.archiveItem;
            int i13 = R.drawable.msg_archive;
            actionBarMenuSubItem2.setTextAndIcon(string2, i13);
            this.archive2Item.setIcon(i13);
            this.archive2Item.setContentDescription(string2);
            FilterTabsView filterTabsView2 = this.filterTabsView;
            if (filterTabsView2 != null && filterTabsView2.getVisibility() == 0) {
                this.archive2Item.setVisibility(0);
                this.archiveItem.setVisibility(8);
            } else {
                this.archiveItem.setVisibility(0);
                this.archive2Item.setVisibility(8);
            }
        } else {
            this.archiveItem.setVisibility(8);
            this.archive2Item.setVisibility(8);
        }
        if (this.canPinCount + i9 != size) {
            this.pinItem.setVisibility(8);
            this.pin2Item.setVisibility(8);
            i = 0;
        } else {
            FilterTabsView filterTabsView3 = this.filterTabsView;
            if (filterTabsView3 != null && filterTabsView3.getVisibility() == 0) {
                i = 0;
                this.pin2Item.setVisibility(0);
                this.pinItem.setVisibility(8);
            } else {
                i = 0;
                this.pinItem.setVisibility(0);
                this.pin2Item.setVisibility(8);
            }
        }
        if (i10 != 0) {
            this.blockItem.setVisibility(8);
        } else {
            this.blockItem.setVisibility(i);
        }
        FilterTabsView filterTabsView4 = this.filterTabsView;
        if (filterTabsView4 == null || filterTabsView4.getVisibility() != 0 || this.filterTabsView.currentTabIsDefault()) {
            this.removeFromFolderItem.setVisibility(8);
        } else {
            this.removeFromFolderItem.setVisibility(i);
        }
        FilterTabsView filterTabsView5 = this.filterTabsView;
        if (filterTabsView5 != null && filterTabsView5.getVisibility() == 0 && this.filterTabsView.currentTabIsDefault() && !FiltersListBottomSheet.getCanAddDialogFilters(this, this.selectedDialogs).isEmpty()) {
            this.addToFolderItem.setVisibility(0);
        } else {
            this.addToFolderItem.setVisibility(8);
        }
        if (this.canUnmuteCount != 0) {
            this.muteItem.setIcon(R.drawable.msg_unmute);
            this.muteItem.setContentDescription(LocaleController.getString("ChatsUnmute", R.string.ChatsUnmute));
        } else {
            this.muteItem.setIcon(R.drawable.msg_mute);
            this.muteItem.setContentDescription(LocaleController.getString("ChatsMute", R.string.ChatsMute));
        }
        if (this.canReadCount != 0) {
            this.readItem.setTextAndIcon(LocaleController.getString("MarkAsRead", R.string.MarkAsRead), R.drawable.msg_markread);
            this.readItem.setVisibility(0);
        } else if (this.forumCount == 0) {
            this.readItem.setTextAndIcon(LocaleController.getString("MarkAsUnread", R.string.MarkAsUnread), R.drawable.msg_markunread);
            this.readItem.setVisibility(0);
        } else {
            this.readItem.setVisibility(8);
        }
        if (this.canPinCount != 0) {
            this.pinItem.setIcon(R.drawable.msg_pin);
            this.pinItem.setContentDescription(LocaleController.getString("PinToTop", R.string.PinToTop));
            this.pin2Item.setText(LocaleController.getString("DialogPin", R.string.DialogPin));
            return;
        }
        this.pinItem.setIcon(R.drawable.msg_unpin);
        this.pinItem.setContentDescription(LocaleController.getString("UnpinFromTop", R.string.UnpinFromTop));
        this.pin2Item.setText(LocaleController.getString("DialogUnpin", R.string.DialogUnpin));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validateSlowModeDialog(long j) {
        TLRPC$Chat chat;
        ChatActivityEnterView chatActivityEnterView;
        if ((this.messagesCount > 1 || !((chatActivityEnterView = this.commentView) == null || chatActivityEnterView.getVisibility() != 0 || TextUtils.isEmpty(this.commentView.getFieldText()))) && DialogObject.isChatDialog(j) && (chat = getMessagesController().getChat(Long.valueOf(-j))) != null && !ChatObject.hasAdminRights(chat) && chat.slowmode_enabled) {
            AlertsCreator.showSimpleAlert(this, LocaleController.getString("Slowmode", R.string.Slowmode), LocaleController.getString("SlowmodeSendError", R.string.SlowmodeSendError));
            return false;
        }
        return true;
    }

    private void showOrUpdateActionMode(long j, View view) {
        addOrRemoveSelectedDialog(j, view);
        boolean z = true;
        if (this.actionBar.isActionModeShowed()) {
            if (this.selectedDialogs.isEmpty()) {
                hideActionMode(true);
                return;
            }
        } else {
            if (this.searchIsShowed) {
                createActionMode("search_dialogs_action_mode");
                if (this.actionBar.getBackButton().getDrawable() instanceof MenuDrawable) {
                    this.actionBar.setBackButtonDrawable(new BackDrawable(false));
                }
            } else {
                createActionMode(null);
            }
            AndroidUtilities.hideKeyboard(this.fragmentView.findFocus());
            this.actionBar.setActionModeOverrideColor(Theme.getColor("windowBackgroundWhite"));
            this.actionBar.showActionMode();
            resetScroll();
            if (this.menuDrawable != null) {
                this.actionBar.setBackButtonContentDescription(LocaleController.getString("AccDescrGoBack", R.string.AccDescrGoBack));
            }
            if (getPinnedCount() > 1) {
                if (this.viewPages != null) {
                    int i = 0;
                    while (true) {
                        ViewPage[] viewPageArr = this.viewPages;
                        if (i >= viewPageArr.length) {
                            break;
                        }
                        viewPageArr[i].dialogsAdapter.onReorderStateChanged(true);
                        i++;
                    }
                }
                updateVisibleRows(MessagesController.UPDATE_MASK_REORDER);
            }
            if (!this.searchIsShowed) {
                AnimatorSet animatorSet = new AnimatorSet();
                ArrayList arrayList = new ArrayList();
                for (int i2 = 0; i2 < this.actionModeViews.size(); i2++) {
                    View view2 = this.actionModeViews.get(i2);
                    view2.setPivotY(ActionBar.getCurrentActionBarHeight() / 2);
                    AndroidUtilities.clearDrawableAnimation(view2);
                    arrayList.add(ObjectAnimator.ofFloat(view2, View.SCALE_Y, 0.1f, 1.0f));
                }
                animatorSet.playTogether(arrayList);
                animatorSet.setDuration(200L);
                animatorSet.start();
            }
            ValueAnimator valueAnimator = this.actionBarColorAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.progressToActionMode, 1.0f);
            this.actionBarColorAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    DialogsActivity.this.lambda$showOrUpdateActionMode$61(valueAnimator2);
                }
            });
            this.actionBarColorAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.actionBarColorAnimator.setDuration(200L);
            this.actionBarColorAnimator.start();
            FilterTabsView filterTabsView = this.filterTabsView;
            if (filterTabsView != null) {
                filterTabsView.animateColorsTo("profile_tabSelectedLine", "profile_tabSelectedText", "profile_tabText", "profile_tabSelector", "actionBarActionModeDefault");
            }
            MenuDrawable menuDrawable = this.menuDrawable;
            if (menuDrawable != null) {
                menuDrawable.setRotateToBack(false);
                this.menuDrawable.setRotation(1.0f, true);
            } else {
                BackDrawable backDrawable = this.backDrawable;
                if (backDrawable != null) {
                    backDrawable.setRotation(1.0f, true);
                }
            }
            z = false;
        }
        updateCounters(false);
        this.selectedDialogsCountTextView.setNumber(this.selectedDialogs.size(), z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showOrUpdateActionMode$61(ValueAnimator valueAnimator) {
        this.progressToActionMode = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        for (int i = 0; i < this.actionBar.getChildCount(); i++) {
            if (this.actionBar.getChildAt(i).getVisibility() == 0 && this.actionBar.getChildAt(i) != this.actionBar.getActionMode() && this.actionBar.getChildAt(i) != this.actionBar.getBackButton()) {
                this.actionBar.getChildAt(i).setAlpha(1.0f - this.progressToActionMode);
            }
        }
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeSearch() {
        if (AndroidUtilities.isTablet()) {
            ActionBar actionBar = this.actionBar;
            if (actionBar != null) {
                actionBar.closeSearchField();
            }
            TLObject tLObject = this.searchObject;
            if (tLObject != null) {
                this.searchViewPager.dialogsSearchAdapter.putRecentSearch(this.searchDialogId, tLObject);
                this.searchObject = null;
                return;
            }
            return;
        }
        this.closeSearchFieldOnHide = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RecyclerListView getListView() {
        return this.viewPages[0].listView;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RecyclerListView getSearchListView() {
        return this.searchViewPager.searchListView;
    }

    public UndoView getUndoView() {
        if (this.undoView[0].getVisibility() == 0) {
            UndoView[] undoViewArr = this.undoView;
            UndoView undoView = undoViewArr[0];
            undoViewArr[0] = undoViewArr[1];
            undoViewArr[1] = undoView;
            undoView.hide(true, 2);
            ContentView contentView = (ContentView) this.fragmentView;
            contentView.removeView(this.undoView[0]);
            contentView.addView(this.undoView[0]);
        }
        return this.undoView[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProxyButton(boolean z, boolean z2) {
        boolean z3;
        ActionBarMenuItem actionBarMenuItem;
        if (this.proxyDrawable != null) {
            ActionBarMenuItem actionBarMenuItem2 = this.doneItem;
            if (actionBarMenuItem2 == null || actionBarMenuItem2.getVisibility() != 0) {
                boolean z4 = false;
                int i = 0;
                while (true) {
                    if (i >= getDownloadController().downloadingFiles.size()) {
                        z3 = false;
                        break;
                    } else if (getFileLoader().isLoadingFile(getDownloadController().downloadingFiles.get(i).getFileName())) {
                        z3 = true;
                        break;
                    } else {
                        i++;
                    }
                }
                if (!this.searching && (getDownloadController().hasUnviewedDownloads() || z3 || (this.downloadsItem.getVisibility() == 0 && this.downloadsItem.getAlpha() == 1.0f && !z2))) {
                    this.downloadsItemVisible = true;
                    this.downloadsItem.setVisibility(0);
                } else {
                    this.downloadsItem.setVisibility(8);
                    this.downloadsItemVisible = false;
                }
                SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                String string = sharedPreferences.getString("proxy_ip", "");
                boolean z5 = sharedPreferences.getBoolean("proxy_enabled", false);
                if ((!this.downloadsItemVisible && !this.searching && z5 && !TextUtils.isEmpty(string)) || (getMessagesController().blockedCountry && !SharedConfig.proxyList.isEmpty())) {
                    if (!this.actionBar.isSearchFieldVisible() && ((actionBarMenuItem = this.doneItem) == null || actionBarMenuItem.getVisibility() != 0)) {
                        this.proxyItem.setVisibility(0);
                    }
                    this.proxyItemVisible = true;
                    ProxyDrawable proxyDrawable = this.proxyDrawable;
                    int i2 = this.currentConnectionState;
                    proxyDrawable.setConnected(z5, (i2 == 3 || i2 == 5) ? true : true, z);
                    return;
                }
                this.proxyItemVisible = false;
                this.proxyItem.setVisibility(8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDoneItem(final boolean z) {
        if (this.doneItem == null) {
            return;
        }
        AnimatorSet animatorSet = this.doneItemAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.doneItemAnimator = null;
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.doneItemAnimator = animatorSet2;
        animatorSet2.setDuration(180L);
        if (z) {
            this.doneItem.setVisibility(0);
        } else {
            this.doneItem.setSelected(false);
            Drawable background = this.doneItem.getBackground();
            if (background != null) {
                background.setState(StateSet.NOTHING);
                background.jumpToCurrentState();
            }
            ActionBarMenuItem actionBarMenuItem = this.searchItem;
            if (actionBarMenuItem != null) {
                actionBarMenuItem.setVisibility(0);
            }
            ActionBarMenuItem actionBarMenuItem2 = this.proxyItem;
            if (actionBarMenuItem2 != null && this.proxyItemVisible) {
                actionBarMenuItem2.setVisibility(0);
            }
            ActionBarMenuItem actionBarMenuItem3 = this.passcodeItem;
            if (actionBarMenuItem3 != null && this.passcodeItemVisible) {
                actionBarMenuItem3.setVisibility(0);
            }
            ActionBarMenuItem actionBarMenuItem4 = this.downloadsItem;
            if (actionBarMenuItem4 != null && this.downloadsItemVisible) {
                actionBarMenuItem4.setVisibility(0);
            }
        }
        ArrayList arrayList = new ArrayList();
        ActionBarMenuItem actionBarMenuItem5 = this.doneItem;
        Property property = View.ALPHA;
        float[] fArr = new float[1];
        fArr[0] = z ? 1.0f : 0.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem5, property, fArr));
        if (this.proxyItemVisible) {
            ActionBarMenuItem actionBarMenuItem6 = this.proxyItem;
            Property property2 = View.ALPHA;
            float[] fArr2 = new float[1];
            fArr2[0] = z ? 0.0f : 1.0f;
            arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem6, property2, fArr2));
        }
        if (this.passcodeItemVisible) {
            ActionBarMenuItem actionBarMenuItem7 = this.passcodeItem;
            Property property3 = View.ALPHA;
            float[] fArr3 = new float[1];
            fArr3[0] = z ? 0.0f : 1.0f;
            arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem7, property3, fArr3));
        }
        ActionBarMenuItem actionBarMenuItem8 = this.searchItem;
        Property property4 = View.ALPHA;
        float[] fArr4 = new float[1];
        fArr4[0] = z ? 0.0f : 1.0f;
        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem8, property4, fArr4));
        this.doneItemAnimator.playTogether(arrayList);
        this.doneItemAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.41
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                DialogsActivity.this.doneItemAnimator = null;
                if (z) {
                    if (DialogsActivity.this.searchItem != null) {
                        DialogsActivity.this.searchItem.setVisibility(4);
                    }
                    if (DialogsActivity.this.proxyItem != null && DialogsActivity.this.proxyItemVisible) {
                        DialogsActivity.this.proxyItem.setVisibility(4);
                    }
                    if (DialogsActivity.this.passcodeItem != null && DialogsActivity.this.passcodeItemVisible) {
                        DialogsActivity.this.passcodeItem.setVisibility(4);
                    }
                    if (DialogsActivity.this.downloadsItem == null || !DialogsActivity.this.downloadsItemVisible) {
                        return;
                    }
                    DialogsActivity.this.downloadsItem.setVisibility(4);
                } else if (DialogsActivity.this.doneItem != null) {
                    DialogsActivity.this.doneItem.setVisibility(8);
                }
            }
        });
        this.doneItemAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSelectedCount() {
        if (this.commentView != null) {
            if (this.selectedDialogs.isEmpty()) {
                if (this.initialDialogsType == 3 && this.selectAlertString == null) {
                    this.actionBar.setTitle(LocaleController.getString("ForwardTo", R.string.ForwardTo));
                } else {
                    this.actionBar.setTitle(LocaleController.getString("SelectChat", R.string.SelectChat));
                }
                if (this.commentView.getTag() != null) {
                    this.commentView.hidePopup(false);
                    this.commentView.closeKeyboard();
                    AnimatorSet animatorSet = this.commentViewAnimator;
                    if (animatorSet != null) {
                        animatorSet.cancel();
                    }
                    this.commentViewAnimator = new AnimatorSet();
                    this.commentView.setTranslationY(0.0f);
                    AnimatorSet animatorSet2 = this.commentViewAnimator;
                    ChatActivityEnterView chatActivityEnterView = this.commentView;
                    animatorSet2.playTogether(ObjectAnimator.ofFloat(chatActivityEnterView, View.TRANSLATION_Y, chatActivityEnterView.getMeasuredHeight()), ObjectAnimator.ofFloat(this.writeButtonContainer, View.SCALE_X, 0.2f), ObjectAnimator.ofFloat(this.writeButtonContainer, View.SCALE_Y, 0.2f), ObjectAnimator.ofFloat(this.writeButtonContainer, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.selectedCountView, View.SCALE_X, 0.2f), ObjectAnimator.ofFloat(this.selectedCountView, View.SCALE_Y, 0.2f), ObjectAnimator.ofFloat(this.selectedCountView, View.ALPHA, 0.0f));
                    this.commentViewAnimator.setDuration(180L);
                    this.commentViewAnimator.setInterpolator(new DecelerateInterpolator());
                    this.commentViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.42
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            DialogsActivity.this.commentView.setVisibility(8);
                            DialogsActivity.this.writeButtonContainer.setVisibility(8);
                        }
                    });
                    this.commentViewAnimator.start();
                    this.commentView.setTag(null);
                    this.fragmentView.requestLayout();
                }
            } else {
                this.selectedCountView.invalidate();
                if (this.commentView.getTag() == null) {
                    this.commentView.setFieldText("");
                    AnimatorSet animatorSet3 = this.commentViewAnimator;
                    if (animatorSet3 != null) {
                        animatorSet3.cancel();
                    }
                    this.commentView.setVisibility(0);
                    this.writeButtonContainer.setVisibility(0);
                    AnimatorSet animatorSet4 = new AnimatorSet();
                    this.commentViewAnimator = animatorSet4;
                    ChatActivityEnterView chatActivityEnterView2 = this.commentView;
                    animatorSet4.playTogether(ObjectAnimator.ofFloat(chatActivityEnterView2, View.TRANSLATION_Y, chatActivityEnterView2.getMeasuredHeight(), 0.0f), ObjectAnimator.ofFloat(this.writeButtonContainer, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.writeButtonContainer, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.writeButtonContainer, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.selectedCountView, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.selectedCountView, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.selectedCountView, View.ALPHA, 1.0f));
                    this.commentViewAnimator.setDuration(180L);
                    this.commentViewAnimator.setInterpolator(new DecelerateInterpolator());
                    this.commentViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.43
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            DialogsActivity.this.commentView.setTag(2);
                            DialogsActivity.this.commentView.requestLayout();
                        }
                    });
                    this.commentViewAnimator.start();
                    this.commentView.setTag(1);
                }
                this.actionBar.setTitle(LocaleController.formatPluralString("Recipient", this.selectedDialogs.size(), new Object[0]));
            }
        } else if (this.initialDialogsType == 10) {
            hideFloatingButton(this.selectedDialogs.isEmpty());
        }
        ArrayList<Long> arrayList = this.selectedDialogs;
        ChatActivityEnterView chatActivityEnterView3 = this.commentView;
        boolean shouldShowNextButton = shouldShowNextButton(this, arrayList, chatActivityEnterView3 != null ? chatActivityEnterView3.getFieldText() : "", false);
        this.isNextButton = shouldShowNextButton;
        AndroidUtilities.updateViewVisibilityAnimated(this.writeButton[0], !shouldShowNextButton, 0.5f, true);
        AndroidUtilities.updateViewVisibilityAnimated(this.writeButton[1], this.isNextButton, 0.5f, true);
    }

    @TargetApi(23)
    private void askForPermissons(boolean z) {
        Activity parentActivity = getParentActivity();
        if (parentActivity == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (getUserConfig().syncContacts && this.askAboutContacts && parentActivity.checkSelfPermission("android.permission.READ_CONTACTS") != 0) {
            if (z) {
                AlertDialog create = AlertsCreator.createContactsPermissionDialog(parentActivity, new MessagesStorage.IntCallback() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda72
                    @Override // org.telegram.messenger.MessagesStorage.IntCallback
                    public final void run(int i) {
                        DialogsActivity.this.lambda$askForPermissons$62(i);
                    }
                }).create();
                this.permissionDialog = create;
                showDialog(create);
                return;
            }
            arrayList.add("android.permission.READ_CONTACTS");
            arrayList.add("android.permission.WRITE_CONTACTS");
            arrayList.add("android.permission.GET_ACCOUNTS");
        }
        if ((Build.VERSION.SDK_INT <= 28 || BuildVars.NO_SCOPED_STORAGE) && parentActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            arrayList.add("android.permission.READ_EXTERNAL_STORAGE");
            arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        if (arrayList.isEmpty()) {
            if (this.askingForPermissions) {
                this.askingForPermissions = false;
                showFiltersHint();
                return;
            }
            return;
        }
        try {
            parentActivity.requestPermissions((String[]) arrayList.toArray(new String[0]), 1);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$askForPermissons$62(int i) {
        this.askAboutContacts = i != 0;
        MessagesController.getGlobalNotificationsSettings().edit().putBoolean("askAboutContacts", this.askAboutContacts).commit();
        askForPermissons(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onDialogDismiss(Dialog dialog) {
        super.onDialogDismiss(dialog);
        AlertDialog alertDialog = this.permissionDialog;
        if (alertDialog == null || dialog != alertDialog || getParentActivity() == null) {
            return;
        }
        askForPermissons(false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        FrameLayout frameLayout;
        super.onConfigurationChanged(configuration);
        ActionBarPopupWindow actionBarPopupWindow = this.scrimPopupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
        }
        if (this.onlySelect || (frameLayout = this.floatingButtonContainer) == null) {
            return;
        }
        frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.DialogsActivity.44
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                DialogsActivity dialogsActivity = DialogsActivity.this;
                dialogsActivity.floatingButtonTranslation = dialogsActivity.floatingHidden ? AndroidUtilities.dp(100.0f) : 0.0f;
                DialogsActivity.this.updateFloatingButtonOffset();
                DialogsActivity.this.floatingButtonContainer.setClickable(!DialogsActivity.this.floatingHidden);
                if (DialogsActivity.this.floatingButtonContainer != null) {
                    DialogsActivity.this.floatingButtonContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        FilesMigrationService.FilesMigrationBottomSheet filesMigrationBottomSheet;
        boolean z = true;
        if (i != 1) {
            if (i == 4) {
                int i2 = 0;
                while (true) {
                    if (i2 >= iArr.length) {
                        break;
                    } else if (iArr[i2] != 0) {
                        z = false;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (!z || Build.VERSION.SDK_INT < 30 || (filesMigrationBottomSheet = FilesMigrationService.filesMigrationBottomSheet) == null) {
                    return;
                }
                filesMigrationBottomSheet.migrateOldFolder();
                return;
            }
            return;
        }
        for (int i3 = 0; i3 < strArr.length; i3++) {
            if (iArr.length > i3) {
                String str = strArr[i3];
                str.hashCode();
                if (str.equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                    if (iArr[i3] == 0) {
                        ImageLoader.getInstance().checkMediaPaths();
                    }
                } else if (str.equals("android.permission.READ_CONTACTS")) {
                    if (iArr[i3] == 0) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda44
                            @Override // java.lang.Runnable
                            public final void run() {
                                DialogsActivity.this.lambda$onRequestPermissionsResultFragment$63();
                            }
                        });
                        getContactsController().forceImportContacts();
                    } else {
                        SharedPreferences.Editor edit = MessagesController.getGlobalNotificationsSettings().edit();
                        this.askAboutContacts = false;
                        edit.putBoolean("askAboutContacts", false).commit();
                    }
                }
            }
        }
        if (this.askingForPermissions) {
            this.askingForPermissions = false;
            showFiltersHint();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRequestPermissionsResultFragment$63() {
        getNotificationCenter().postNotificationName(NotificationCenter.forceImportContactsStart, new Object[0]);
    }

    private void reloadViewPageDialogs(ViewPage viewPage, boolean z) {
        int i;
        int i2;
        if (viewPage.getVisibility() != 0) {
            return;
        }
        int currentCount = viewPage.dialogsAdapter.getCurrentCount();
        if (viewPage.dialogsType == 0 && hasHiddenArchive() && viewPage.listView.getChildCount() == 0 && viewPage.archivePullViewState == 2) {
            ((LinearLayoutManager) viewPage.listView.getLayoutManager()).scrollToPositionWithOffset(1, (int) this.actionBar.getTranslationY());
        }
        if (viewPage.dialogsAdapter.isDataSetChanged() || z) {
            viewPage.dialogsAdapter.updateHasHints();
            int itemCount = viewPage.dialogsAdapter.getItemCount();
            if (itemCount == 1 && currentCount == 1 && viewPage.dialogsAdapter.getItemViewType(0) == 5) {
                viewPage.updateList(true);
            } else {
                viewPage.updateList(false);
                if (itemCount > currentCount && (i = this.initialDialogsType) != 11 && i != 12 && i != 13) {
                    viewPage.recyclerItemsEnterAnimator.showItemsAnimated(currentCount);
                }
            }
        } else {
            updateVisibleRows(MessagesController.UPDATE_MASK_NEW_MESSAGE);
            if (viewPage.dialogsAdapter.getItemCount() > currentCount && (i2 = this.initialDialogsType) != 11 && i2 != 12 && i2 != 13) {
                viewPage.recyclerItemsEnterAnimator.showItemsAnimated(currentCount);
            }
        }
        try {
            viewPage.listView.setEmptyView(this.folderId == 0 ? viewPage.progressView : null);
        } catch (Exception e) {
            FileLog.e(e);
        }
        checkListLoad(viewPage);
    }

    public void setPanTranslationOffset(float f) {
        this.floatingButtonPanOffset = f;
        updateFloatingButtonOffset();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, final Object... objArr) {
        ViewPage[] viewPageArr;
        DialogsSearchAdapter dialogsSearchAdapter;
        DialogsSearchAdapter dialogsSearchAdapter2;
        MessagesController.DialogFilter dialogFilter;
        int i3 = 0;
        if (i == NotificationCenter.dialogsNeedReload) {
            if (this.viewPages == null || this.dialogsListFrozen) {
                return;
            }
            int i4 = 0;
            while (true) {
                ViewPage[] viewPageArr2 = this.viewPages;
                if (i4 >= viewPageArr2.length) {
                    break;
                }
                final ViewPage viewPage = viewPageArr2[i4];
                if (viewPageArr2[0].dialogsType == 7 || this.viewPages[0].dialogsType == 8) {
                    dialogFilter = getMessagesController().selectedDialogFilter[this.viewPages[0].dialogsType == 8 ? (char) 1 : (char) 0];
                } else {
                    dialogFilter = null;
                }
                boolean z = (dialogFilter == null || (dialogFilter.flags & MessagesController.DIALOG_FILTER_FLAG_EXCLUDE_READ) == 0) ? false : true;
                if (this.slowedReloadAfterDialogClick && z) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda63
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.this.lambda$didReceivedNotification$64(viewPage, objArr);
                        }
                    }, 160L);
                } else {
                    reloadViewPageDialogs(viewPage, objArr.length > 0);
                }
                i4++;
            }
            FilterTabsView filterTabsView = this.filterTabsView;
            if (filterTabsView != null && filterTabsView.getVisibility() == 0) {
                this.filterTabsView.checkTabsCounter();
            }
            this.slowedReloadAfterDialogClick = false;
        } else if (i == NotificationCenter.dialogsUnreadCounterChanged) {
            FilterTabsView filterTabsView2 = this.filterTabsView;
            if (filterTabsView2 == null || filterTabsView2.getVisibility() != 0) {
                return;
            }
            FilterTabsView filterTabsView3 = this.filterTabsView;
            filterTabsView3.notifyTabCounterChanged(filterTabsView3.getDefaultTabId());
        } else if (i == NotificationCenter.dialogsUnreadReactionsCounterChanged) {
            updateVisibleRows(0);
        } else if (i == NotificationCenter.emojiLoaded) {
            updateVisibleRows(0);
            FilterTabsView filterTabsView4 = this.filterTabsView;
            if (filterTabsView4 != null) {
                filterTabsView4.getTabsContainer().invalidateViews();
            }
        } else if (i == NotificationCenter.closeSearchByActiveAction) {
            ActionBar actionBar = this.actionBar;
            if (actionBar != null) {
                actionBar.closeSearchField();
            }
        } else if (i == NotificationCenter.proxySettingsChanged) {
            updateProxyButton(false, false);
        } else if (i == NotificationCenter.updateInterfaces) {
            Integer num = (Integer) objArr[0];
            updateVisibleRows(num.intValue());
            FilterTabsView filterTabsView5 = this.filterTabsView;
            if (filterTabsView5 != null && filterTabsView5.getVisibility() == 0 && (num.intValue() & MessagesController.UPDATE_MASK_READ_DIALOG_MESSAGE) != 0) {
                this.filterTabsView.checkTabsCounter();
            }
            if (this.viewPages != null) {
                while (i3 < this.viewPages.length) {
                    if ((num.intValue() & MessagesController.UPDATE_MASK_STATUS) != 0) {
                        this.viewPages[i3].dialogsAdapter.sortOnlineContacts(true);
                    }
                    i3++;
                }
            }
            updateStatus(UserConfig.getInstance(i2).getCurrentUser(), true);
        } else if (i == NotificationCenter.appDidLogout) {
            dialogsLoaded[this.currentAccount] = false;
        } else if (i == NotificationCenter.encryptedChatUpdated) {
            updateVisibleRows(0);
        } else if (i == NotificationCenter.contactsDidLoad) {
            if (this.viewPages == null || this.dialogsListFrozen) {
                return;
            }
            boolean z2 = this.floatingProgressVisible;
            setFloatingProgressVisible(false, true);
            for (ViewPage viewPage2 : this.viewPages) {
                viewPage2.dialogsAdapter.setForceUpdatingContacts(false);
            }
            if (z2) {
                setContactsAlpha(0.0f);
                animateContactsAlpha(1.0f);
            }
            int i5 = 0;
            boolean z3 = false;
            while (true) {
                ViewPage[] viewPageArr3 = this.viewPages;
                if (i5 >= viewPageArr3.length) {
                    break;
                }
                if (!viewPageArr3[i5].isDefaultDialogType() || getMessagesController().getAllFoldersDialogsCount() > 10) {
                    z3 = true;
                } else {
                    this.viewPages[i5].dialogsAdapter.notifyDataSetChanged();
                }
                i5++;
            }
            if (z3) {
                updateVisibleRows(0);
            }
        } else if (i == NotificationCenter.openedChatChanged) {
            if (this.viewPages == null) {
                return;
            }
            int i6 = 0;
            while (true) {
                ViewPage[] viewPageArr4 = this.viewPages;
                if (i6 < viewPageArr4.length) {
                    if (viewPageArr4[i6].isDefaultDialogType() && AndroidUtilities.isTablet()) {
                        boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
                        long longValue = ((Long) objArr[0]).longValue();
                        int intValue = ((Integer) objArr[1]).intValue();
                        if (booleanValue) {
                            MessagesStorage.TopicKey topicKey = this.openedDialogId;
                            if (longValue == topicKey.dialogId && intValue == topicKey.topicId) {
                                topicKey.dialogId = 0L;
                                topicKey.topicId = 0;
                            }
                        } else {
                            MessagesStorage.TopicKey topicKey2 = this.openedDialogId;
                            topicKey2.dialogId = longValue;
                            topicKey2.topicId = intValue;
                        }
                        this.viewPages[i6].dialogsAdapter.setOpenedDialogId(this.openedDialogId.dialogId);
                    }
                    i6++;
                } else {
                    updateVisibleRows(MessagesController.UPDATE_MASK_SELECT_DIALOG);
                    return;
                }
            }
        } else if (i == NotificationCenter.notificationsSettingsUpdated) {
            updateVisibleRows(0);
        } else if (i == NotificationCenter.messageReceivedByAck || i == NotificationCenter.messageReceivedByServer || i == NotificationCenter.messageSendError) {
            updateVisibleRows(MessagesController.UPDATE_MASK_SEND_STATE);
        } else if (i == NotificationCenter.didSetPasscode) {
            updatePasscodeButton();
        } else if (i == NotificationCenter.needReloadRecentDialogsSearch) {
            SearchViewPager searchViewPager = this.searchViewPager;
            if (searchViewPager == null || (dialogsSearchAdapter2 = searchViewPager.dialogsSearchAdapter) == null) {
                return;
            }
            dialogsSearchAdapter2.loadRecentSearch();
        } else if (i == NotificationCenter.replyMessagesDidLoad) {
            updateVisibleRows(MessagesController.UPDATE_MASK_MESSAGE_TEXT);
        } else if (i == NotificationCenter.reloadHints) {
            SearchViewPager searchViewPager2 = this.searchViewPager;
            if (searchViewPager2 == null || (dialogsSearchAdapter = searchViewPager2.dialogsSearchAdapter) == null) {
                return;
            }
            dialogsSearchAdapter.notifyDataSetChanged();
        } else if (i == NotificationCenter.didUpdateConnectionState) {
            int connectionState = AccountInstance.getInstance(i2).getConnectionsManager().getConnectionState();
            if (this.currentConnectionState != connectionState) {
                this.currentConnectionState = connectionState;
                updateProxyButton(true, false);
            }
        } else if (i == NotificationCenter.onDownloadingFilesChanged) {
            updateProxyButton(true, false);
            SearchViewPager searchViewPager3 = this.searchViewPager;
            if (searchViewPager3 != null) {
                updateSpeedItem(searchViewPager3.getCurrentPosition() == 2);
            }
        } else if (i == NotificationCenter.needDeleteDialog) {
            if (this.fragmentView == null || this.isPaused) {
                return;
            }
            final long longValue2 = ((Long) objArr[0]).longValue();
            final TLRPC$User tLRPC$User = (TLRPC$User) objArr[1];
            final TLRPC$Chat tLRPC$Chat = (TLRPC$Chat) objArr[2];
            final boolean booleanValue2 = ((Boolean) objArr[3]).booleanValue();
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda60
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$didReceivedNotification$65(tLRPC$Chat, longValue2, booleanValue2, tLRPC$User);
                }
            };
            if (this.undoView[0] != null) {
                if (!ChatObject.isForum(tLRPC$Chat)) {
                    getUndoView().showWithAction(longValue2, 1, runnable);
                    return;
                } else {
                    runnable.run();
                    return;
                }
            }
            runnable.run();
        } else if (i == NotificationCenter.folderBecomeEmpty) {
            int intValue2 = ((Integer) objArr[0]).intValue();
            int i7 = this.folderId;
            if (i7 != intValue2 || i7 == 0) {
                return;
            }
            finishFragment();
        } else if (i == NotificationCenter.dialogFiltersUpdated) {
            updateFilterTabs(true, true);
        } else if (i == NotificationCenter.filterSettingsUpdated) {
            showFiltersHint();
        } else if (i == NotificationCenter.newSuggestionsAvailable) {
            showNextSupportedSuggestion();
            updateDialogsHint();
        } else if (i == NotificationCenter.forceImportContactsStart) {
            setFloatingProgressVisible(true, true);
            for (ViewPage viewPage3 : this.viewPages) {
                viewPage3.dialogsAdapter.setForceShowEmptyCell(false);
                viewPage3.dialogsAdapter.setForceUpdatingContacts(true);
                viewPage3.dialogsAdapter.notifyDataSetChanged();
            }
        } else if (i == NotificationCenter.messagesDeleted) {
            if (!this.searchIsShowed || this.searchViewPager == null) {
                return;
            }
            this.searchViewPager.messagesDeleted(((Long) objArr[1]).longValue(), (ArrayList) objArr[0]);
        } else if (i == NotificationCenter.didClearDatabase) {
            if (this.viewPages != null) {
                while (true) {
                    ViewPage[] viewPageArr5 = this.viewPages;
                    if (i3 >= viewPageArr5.length) {
                        break;
                    }
                    viewPageArr5[i3].dialogsAdapter.didDatabaseCleared();
                    i3++;
                }
            }
            SuggestClearDatabaseBottomSheet.dismissDialog();
        } else if (i == NotificationCenter.appUpdateAvailable) {
            updateMenuButton(true);
        } else if (i == NotificationCenter.fileLoaded || i == NotificationCenter.fileLoadFailed || i == NotificationCenter.fileLoadProgressChanged) {
            String str = (String) objArr[0];
            if (SharedConfig.isAppUpdateAvailable() && FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document).equals(str)) {
                updateMenuButton(true);
            }
        } else if (i == NotificationCenter.onDatabaseMigration) {
            boolean booleanValue3 = ((Boolean) objArr[0]).booleanValue();
            if (this.fragmentView != null) {
                if (booleanValue3) {
                    if (this.databaseMigrationHint == null) {
                        DatabaseMigrationHint databaseMigrationHint = new DatabaseMigrationHint(this.fragmentView.getContext(), this.currentAccount);
                        this.databaseMigrationHint = databaseMigrationHint;
                        databaseMigrationHint.setAlpha(0.0f);
                        ((ContentView) this.fragmentView).addView(this.databaseMigrationHint);
                        this.databaseMigrationHint.animate().alpha(1.0f).setDuration(300L).setStartDelay(1000L).start();
                    }
                    this.databaseMigrationHint.setTag(1);
                    return;
                }
                View view = this.databaseMigrationHint;
                if (view == null || view.getTag() == null) {
                    return;
                }
                final View view2 = this.databaseMigrationHint;
                view2.animate().setListener(null).cancel();
                view2.animate().setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.45
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (view2.getParent() != null) {
                            ((ViewGroup) view2.getParent()).removeView(view2);
                        }
                        DialogsActivity.this.databaseMigrationHint = null;
                    }
                }).alpha(0.0f).setStartDelay(0L).setDuration(150L).start();
                this.databaseMigrationHint.setTag(null);
            }
        } else if (i == NotificationCenter.onDatabaseOpened) {
            checkSuggestClearDatabase();
        } else if (i == NotificationCenter.userEmojiStatusUpdated) {
            updateStatus((TLRPC$User) objArr[0], true);
        } else if (i == NotificationCenter.currentUserPremiumStatusChanged) {
            updateStatus(UserConfig.getInstance(i2).getCurrentUser(), true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$64(ViewPage viewPage, Object[] objArr) {
        reloadViewPageDialogs(viewPage, objArr.length > 0);
        FilterTabsView filterTabsView = this.filterTabsView;
        if (filterTabsView == null || filterTabsView.getVisibility() != 0) {
            return;
        }
        this.filterTabsView.checkTabsCounter();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$65(TLRPC$Chat tLRPC$Chat, long j, boolean z, TLRPC$User tLRPC$User) {
        if (tLRPC$Chat != null) {
            if (ChatObject.isNotInChat(tLRPC$Chat)) {
                getMessagesController().deleteDialog(j, 0, z);
            } else {
                getMessagesController().deleteParticipantFromChat(-j, getMessagesController().getUser(Long.valueOf(getUserConfig().getClientUserId())), null, z, z);
            }
        } else {
            getMessagesController().deleteDialog(j, 0, z);
            if (tLRPC$User != null && tLRPC$User.bot) {
                getMessagesController().blockPeer(tLRPC$User.id);
            }
        }
        getMessagesController().checkIfFolderEmpty(this.folderId);
    }

    private void checkSuggestClearDatabase() {
        if (getMessagesStorage().showClearDatabaseAlert) {
            getMessagesStorage().showClearDatabaseAlert = false;
            SuggestClearDatabaseBottomSheet.show(this);
        }
    }

    private void updateMenuButton(boolean z) {
        int i;
        if (this.menuDrawable == null || this.updateLayout == null) {
            return;
        }
        float f = 0.0f;
        if (SharedConfig.isAppUpdateAvailable()) {
            String attachFileName = FileLoader.getAttachFileName(SharedConfig.pendingAppUpdate.document);
            if (getFileLoader().isLoadingFile(attachFileName)) {
                i = MenuDrawable.TYPE_UDPATE_DOWNLOADING;
                Float fileProgress = ImageLoader.getInstance().getFileProgress(attachFileName);
                if (fileProgress != null) {
                    f = fileProgress.floatValue();
                }
            } else {
                i = MenuDrawable.TYPE_UDPATE_AVAILABLE;
            }
        } else {
            i = MenuDrawable.TYPE_DEFAULT;
        }
        updateAppUpdateViews(z);
        this.menuDrawable.setType(i, z);
        this.menuDrawable.setUpdateDownloadProgress(f, z);
    }

    private void showNextSupportedSuggestion() {
        if (this.showingSuggestion != null) {
            return;
        }
        for (String str : getMessagesController().pendingSuggestions) {
            if (showSuggestion(str)) {
                this.showingSuggestion = str;
                return;
            }
        }
    }

    private void onSuggestionDismiss() {
        if (this.showingSuggestion == null) {
            return;
        }
        getMessagesController().removeSuggestion(0L, this.showingSuggestion);
        this.showingSuggestion = null;
        showNextSupportedSuggestion();
    }

    private boolean showSuggestion(String str) {
        if ("AUTOARCHIVE_POPULAR".equals(str)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("HideNewChatsAlertTitle", R.string.HideNewChatsAlertTitle));
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.getString("HideNewChatsAlertText", R.string.HideNewChatsAlertText)));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            builder.setPositiveButton(LocaleController.getString("GoToSettings", R.string.GoToSettings), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda13
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    DialogsActivity.this.lambda$showSuggestion$66(dialogInterface, i);
                }
            });
            showDialog(builder.create(), new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda21
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    DialogsActivity.this.lambda$showSuggestion$67(dialogInterface);
                }
            });
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSuggestion$66(DialogInterface dialogInterface, int i) {
        presentFragment(new PrivacySettingsActivity());
        AndroidUtilities.scrollToFragmentRow(this.parentLayout, "newChatsRow");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSuggestion$67(DialogInterface dialogInterface) {
        onSuggestionDismiss();
    }

    private void showFiltersHint() {
        if (this.askingForPermissions || !getMessagesController().dialogFiltersLoaded || !getMessagesController().showFiltersTooltip || this.filterTabsView == null || !getMessagesController().dialogFilters.isEmpty() || this.isPaused || !getUserConfig().filtersLoaded || this.inPreviewMode) {
            return;
        }
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        if (globalMainSettings.getBoolean("filterhint", false)) {
            return;
        }
        globalMainSettings.edit().putBoolean("filterhint", true).commit();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda51
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$showFiltersHint$69();
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showFiltersHint$68() {
        presentFragment(new FiltersSetupActivity());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showFiltersHint$69() {
        getUndoView().showWithAction(0L, 15, null, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$showFiltersHint$68();
            }
        });
    }

    private void setDialogsListFrozen(boolean z, boolean z2) {
        if (this.viewPages == null || this.dialogsListFrozen == z) {
            return;
        }
        if (z) {
            this.frozenDialogsList = new ArrayList<>(getDialogsArray(this.currentAccount, this.viewPages[0].dialogsType, this.folderId, false));
        } else {
            this.frozenDialogsList = null;
        }
        this.dialogsListFrozen = z;
        this.viewPages[0].dialogsAdapter.setDialogsListFrozen(z);
        if (z || !z2) {
            return;
        }
        this.viewPages[0].dialogsAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDialogsListFrozen(boolean z) {
        setDialogsListFrozen(z, true);
    }

    /* loaded from: classes3.dex */
    public class DialogsHeader extends TLRPC$Dialog {
        public int headerType;

        public DialogsHeader(DialogsActivity dialogsActivity, int i) {
            this.headerType = i;
        }
    }

    public ArrayList<TLRPC$Dialog> getDialogsArray(int i, int i2, int i3, boolean z) {
        ArrayList<TLRPC$Dialog> arrayList;
        if (!z || (arrayList = this.frozenDialogsList) == null) {
            MessagesController messagesController = AccountInstance.getInstance(i).getMessagesController();
            if (i2 == 0) {
                return messagesController.getDialogs(i3);
            }
            if (i2 == 1 || i2 == 10 || i2 == 13) {
                return messagesController.dialogsServerOnly;
            }
            if (i2 == 2) {
                ArrayList<TLRPC$Dialog> arrayList2 = new ArrayList<>(messagesController.dialogsCanAddUsers.size() + messagesController.dialogsMyChannels.size() + messagesController.dialogsMyGroups.size() + 2);
                if (messagesController.dialogsMyChannels.size() > 0 && this.allowChannels) {
                    arrayList2.add(new DialogsHeader(this, 0));
                    arrayList2.addAll(messagesController.dialogsMyChannels);
                }
                if (messagesController.dialogsMyGroups.size() > 0 && this.allowGroups) {
                    arrayList2.add(new DialogsHeader(this, 1));
                    arrayList2.addAll(messagesController.dialogsMyGroups);
                }
                if (messagesController.dialogsCanAddUsers.size() > 0) {
                    int size = messagesController.dialogsCanAddUsers.size();
                    for (int i4 = 0; i4 < size; i4++) {
                        TLRPC$Dialog tLRPC$Dialog = messagesController.dialogsCanAddUsers.get(i4);
                        if ((this.allowChannels && ChatObject.isChannelAndNotMegaGroup(-tLRPC$Dialog.id, i)) || (this.allowGroups && (ChatObject.isMegagroup(i, -tLRPC$Dialog.id) || !ChatObject.isChannel(-tLRPC$Dialog.id, i)))) {
                            if (r0 != 0) {
                                arrayList2.add(new DialogsHeader(this, 2));
                                r0 = 0;
                            }
                            arrayList2.add(tLRPC$Dialog);
                        }
                    }
                }
                return arrayList2;
            } else if (i2 == 3) {
                return messagesController.dialogsForward;
            } else {
                if (i2 == 4 || i2 == 12) {
                    return messagesController.dialogsUsersOnly;
                }
                if (i2 == 5) {
                    return messagesController.dialogsChannelsOnly;
                }
                if (i2 == 6 || i2 == 11) {
                    return messagesController.dialogsGroupsOnly;
                }
                if (i2 == 7 || i2 == 8) {
                    MessagesController.DialogFilter dialogFilter = messagesController.selectedDialogFilter[i2 == 7 ? (char) 0 : (char) 1];
                    if (dialogFilter == null) {
                        return messagesController.getDialogs(i3);
                    }
                    return dialogFilter.dialogs;
                } else if (i2 == 9) {
                    return messagesController.dialogsForBlock;
                } else {
                    if (i2 == 14) {
                        ArrayList<TLRPC$Dialog> arrayList3 = new ArrayList<>();
                        if (this.allowUsers || this.allowBots) {
                            Iterator<TLRPC$Dialog> it = messagesController.dialogsUsersOnly.iterator();
                            while (it.hasNext()) {
                                TLRPC$Dialog next = it.next();
                                TLRPC$User user = messagesController.getUser(Long.valueOf(next.id));
                                if (user != null && !UserObject.isUserSelf(user)) {
                                    if (user.bot) {
                                        if (this.allowBots) {
                                            arrayList3.add(next);
                                        }
                                    } else if (this.allowUsers) {
                                        arrayList3.add(next);
                                    }
                                }
                            }
                        }
                        if (this.allowGroups) {
                            arrayList3.addAll(messagesController.dialogsGroupsOnly);
                        }
                        if (this.allowChannels) {
                            arrayList3.addAll(messagesController.dialogsChannelsOnly);
                        }
                        return arrayList3;
                    } else if (i2 == 15) {
                        ArrayList<TLRPC$Dialog> arrayList4 = new ArrayList<>();
                        TLRPC$User user2 = messagesController.getUser(Long.valueOf(this.requestPeerBotId));
                        TLRPC$RequestPeerType tLRPC$RequestPeerType = this.requestPeerType;
                        if (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeUser) {
                            ConcurrentHashMap<Long, TLRPC$User> users = messagesController.getUsers();
                            Iterator<TLRPC$Dialog> it2 = messagesController.dialogsUsersOnly.iterator();
                            while (it2.hasNext()) {
                                TLRPC$Dialog next2 = it2.next();
                                if (meetRequestPeerRequirements(getMessagesController().getUser(Long.valueOf(next2.id)))) {
                                    arrayList4.add(next2);
                                }
                            }
                            for (TLRPC$User tLRPC$User : users.values()) {
                                if (tLRPC$User != null && !messagesController.dialogs_dict.containsKey(tLRPC$User.id) && meetRequestPeerRequirements(tLRPC$User)) {
                                    TLRPC$TL_dialog tLRPC$TL_dialog = new TLRPC$TL_dialog();
                                    TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                                    tLRPC$TL_dialog.peer = tLRPC$TL_peerUser;
                                    long j = tLRPC$User.id;
                                    tLRPC$TL_peerUser.user_id = j;
                                    tLRPC$TL_dialog.id = j;
                                    arrayList4.add(tLRPC$TL_dialog);
                                }
                            }
                        } else if ((tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeChat) || (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeBroadcast)) {
                            ConcurrentHashMap<Long, TLRPC$Chat> chats = messagesController.getChats();
                            Iterator<TLRPC$Dialog> it3 = (this.requestPeerType instanceof TLRPC$TL_requestPeerTypeChat ? messagesController.dialogsGroupsOnly : messagesController.dialogsChannelsOnly).iterator();
                            while (it3.hasNext()) {
                                TLRPC$Dialog next3 = it3.next();
                                if (meetRequestPeerRequirements(user2, getMessagesController().getChat(Long.valueOf(-next3.id)))) {
                                    arrayList4.add(next3);
                                }
                            }
                            for (TLRPC$Chat tLRPC$Chat : chats.values()) {
                                if (tLRPC$Chat != null && !messagesController.dialogs_dict.containsKey(-tLRPC$Chat.id) && meetRequestPeerRequirements(user2, tLRPC$Chat)) {
                                    TLRPC$TL_dialog tLRPC$TL_dialog2 = new TLRPC$TL_dialog();
                                    if (ChatObject.isChannel(tLRPC$Chat)) {
                                        TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
                                        tLRPC$TL_dialog2.peer = tLRPC$TL_peerChannel;
                                        tLRPC$TL_peerChannel.channel_id = tLRPC$Chat.id;
                                    } else {
                                        TLRPC$TL_peerChat tLRPC$TL_peerChat = new TLRPC$TL_peerChat();
                                        tLRPC$TL_dialog2.peer = tLRPC$TL_peerChat;
                                        tLRPC$TL_peerChat.chat_id = tLRPC$Chat.id;
                                    }
                                    tLRPC$TL_dialog2.id = -tLRPC$Chat.id;
                                    arrayList4.add(tLRPC$TL_dialog2);
                                }
                            }
                        }
                        return arrayList4;
                    } else {
                        return new ArrayList<>();
                    }
                }
            }
        }
        return arrayList;
    }

    private boolean meetRequestPeerRequirements(TLRPC$User tLRPC$User) {
        Boolean bool;
        Boolean bool2;
        TLRPC$TL_requestPeerTypeUser tLRPC$TL_requestPeerTypeUser = (TLRPC$TL_requestPeerTypeUser) this.requestPeerType;
        return (tLRPC$User == null || UserObject.isReplyUser(tLRPC$User) || UserObject.isDeleted(tLRPC$User) || ((bool = tLRPC$TL_requestPeerTypeUser.bot) != null && bool.booleanValue() != tLRPC$User.bot) || ((bool2 = tLRPC$TL_requestPeerTypeUser.premium) != null && bool2.booleanValue() != tLRPC$User.premium)) ? false : true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x004b, code lost:
        if (r2.booleanValue() == (org.telegram.messenger.ChatObject.getPublicUsername(r7) != null)) goto L26;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean meetRequestPeerRequirements(TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat) {
        Boolean bool;
        Boolean bool2;
        if (tLRPC$Chat != null) {
            boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
            TLRPC$RequestPeerType tLRPC$RequestPeerType = this.requestPeerType;
            if (isChannelAndNotMegaGroup == (tLRPC$RequestPeerType instanceof TLRPC$TL_requestPeerTypeBroadcast) && (((bool = tLRPC$RequestPeerType.creator) == null || !bool.booleanValue() || tLRPC$Chat.creator) && ((bool2 = this.requestPeerType.bot_participant) == null || !bool2.booleanValue() || getMessagesController().isInChatCached(tLRPC$Chat, tLRPC$User) || ChatObject.canAddBotsToChat(tLRPC$Chat)))) {
                Boolean bool3 = this.requestPeerType.has_username;
                if (bool3 != null) {
                }
                Boolean bool4 = this.requestPeerType.forum;
                if ((bool4 == null || bool4.booleanValue() == ChatObject.isForum(tLRPC$Chat)) && ((this.requestPeerType.user_admin_rights == null || getMessagesController().matchesAdminRights(tLRPC$Chat, getUserConfig().getCurrentUser(), this.requestPeerType.user_admin_rights)) && (this.requestPeerType.bot_admin_rights == null || getMessagesController().matchesAdminRights(tLRPC$Chat, tLRPC$User, this.requestPeerType.bot_admin_rights) || ChatObject.canAddAdmins(tLRPC$Chat)))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setSideMenu(RecyclerView recyclerView) {
        this.sideMenu = recyclerView;
        recyclerView.setBackgroundColor(Theme.getColor("chats_menuBackground"));
        this.sideMenu.setGlowColor(Theme.getColor("chats_menuBackground"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePasscodeButton() {
        if (this.passcodeItem == null) {
            return;
        }
        if (SharedConfig.passcodeHash.length() != 0 && !this.searching) {
            ActionBarMenuItem actionBarMenuItem = this.doneItem;
            if (actionBarMenuItem == null || actionBarMenuItem.getVisibility() != 0) {
                this.passcodeItem.setVisibility(0);
            }
            this.passcodeItem.setIcon(this.passcodeDrawable);
            this.passcodeItemVisible = true;
            return;
        }
        this.passcodeItem.setVisibility(8);
        this.passcodeItemVisible = false;
    }

    private void setFloatingProgressVisible(final boolean z, boolean z2) {
        if (this.floatingButton == null || this.floatingProgressView == null) {
            return;
        }
        if (z2) {
            if (z == this.floatingProgressVisible) {
                return;
            }
            AnimatorSet animatorSet = this.floatingProgressAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.floatingProgressVisible = z;
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.floatingProgressAnimator = animatorSet2;
            Animator[] animatorArr = new Animator[6];
            RLottieImageView rLottieImageView = this.floatingButton;
            Property property = View.ALPHA;
            float[] fArr = new float[1];
            fArr[0] = z ? 0.0f : 1.0f;
            animatorArr[0] = ObjectAnimator.ofFloat(rLottieImageView, property, fArr);
            RLottieImageView rLottieImageView2 = this.floatingButton;
            Property property2 = View.SCALE_X;
            float[] fArr2 = new float[1];
            fArr2[0] = z ? 0.1f : 1.0f;
            animatorArr[1] = ObjectAnimator.ofFloat(rLottieImageView2, property2, fArr2);
            RLottieImageView rLottieImageView3 = this.floatingButton;
            Property property3 = View.SCALE_Y;
            float[] fArr3 = new float[1];
            fArr3[0] = z ? 0.1f : 1.0f;
            animatorArr[2] = ObjectAnimator.ofFloat(rLottieImageView3, property3, fArr3);
            RadialProgressView radialProgressView = this.floatingProgressView;
            Property property4 = View.ALPHA;
            float[] fArr4 = new float[1];
            fArr4[0] = z ? 1.0f : 0.0f;
            animatorArr[3] = ObjectAnimator.ofFloat(radialProgressView, property4, fArr4);
            RadialProgressView radialProgressView2 = this.floatingProgressView;
            Property property5 = View.SCALE_X;
            float[] fArr5 = new float[1];
            fArr5[0] = z ? 1.0f : 0.1f;
            animatorArr[4] = ObjectAnimator.ofFloat(radialProgressView2, property5, fArr5);
            RadialProgressView radialProgressView3 = this.floatingProgressView;
            Property property6 = View.SCALE_Y;
            float[] fArr6 = new float[1];
            fArr6[0] = z ? 1.0f : 0.1f;
            animatorArr[5] = ObjectAnimator.ofFloat(radialProgressView3, property6, fArr6);
            animatorSet2.playTogether(animatorArr);
            this.floatingProgressAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.DialogsActivity.46
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    DialogsActivity.this.floatingProgressView.setVisibility(0);
                    DialogsActivity.this.floatingButton.setVisibility(0);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (animator == DialogsActivity.this.floatingProgressAnimator) {
                        if (z) {
                            if (DialogsActivity.this.floatingButton != null) {
                                DialogsActivity.this.floatingButton.setVisibility(8);
                            }
                        } else if (DialogsActivity.this.floatingButton != null) {
                            DialogsActivity.this.floatingProgressView.setVisibility(8);
                        }
                        DialogsActivity.this.floatingProgressAnimator = null;
                    }
                }
            });
            this.floatingProgressAnimator.setDuration(150L);
            this.floatingProgressAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.floatingProgressAnimator.start();
            return;
        }
        AnimatorSet animatorSet3 = this.floatingProgressAnimator;
        if (animatorSet3 != null) {
            animatorSet3.cancel();
        }
        this.floatingProgressVisible = z;
        if (z) {
            this.floatingButton.setAlpha(0.0f);
            this.floatingButton.setScaleX(0.1f);
            this.floatingButton.setScaleY(0.1f);
            this.floatingButton.setVisibility(8);
            this.floatingProgressView.setAlpha(1.0f);
            this.floatingProgressView.setScaleX(1.0f);
            this.floatingProgressView.setScaleY(1.0f);
            this.floatingProgressView.setVisibility(0);
            return;
        }
        this.floatingButton.setAlpha(1.0f);
        this.floatingButton.setScaleX(1.0f);
        this.floatingButton.setScaleY(1.0f);
        this.floatingButton.setVisibility(0);
        this.floatingProgressView.setAlpha(0.0f);
        this.floatingProgressView.setScaleX(0.1f);
        this.floatingProgressView.setScaleY(0.1f);
        this.floatingProgressView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideFloatingButton(boolean z) {
        if (this.rightSlidingDialogContainer.hasFragment()) {
            z = true;
        }
        if (this.floatingHidden != z) {
            if (z && this.floatingForceVisible) {
                return;
            }
            this.floatingHidden = z;
            AnimatorSet animatorSet = new AnimatorSet();
            float[] fArr = new float[2];
            fArr[0] = this.floatingButtonHideProgress;
            fArr[1] = this.floatingHidden ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    DialogsActivity.this.lambda$hideFloatingButton$70(valueAnimator);
                }
            });
            animatorSet.playTogether(ofFloat);
            animatorSet.setDuration(300L);
            animatorSet.setInterpolator(this.floatingInterpolator);
            this.floatingButtonContainer.setClickable(!z);
            animatorSet.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideFloatingButton$70(ValueAnimator valueAnimator) {
        this.floatingButtonHideProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.floatingButtonTranslation = AndroidUtilities.dp(100.0f) * this.floatingButtonHideProgress;
        updateFloatingButtonOffset();
    }

    public void animateContactsAlpha(float f) {
        ValueAnimator valueAnimator = this.contactsAlphaAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator duration = ValueAnimator.ofFloat(this.contactsAlpha, f).setDuration(250L);
        this.contactsAlphaAnimator = duration;
        duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.contactsAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                DialogsActivity.this.lambda$animateContactsAlpha$71(valueAnimator2);
            }
        });
        this.contactsAlphaAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateContactsAlpha$71(ValueAnimator valueAnimator) {
        setContactsAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public void setContactsAlpha(float f) {
        ViewPage[] viewPageArr;
        this.contactsAlpha = f;
        for (ViewPage viewPage : this.viewPages) {
            DialogsRecyclerView dialogsRecyclerView = viewPage.listView;
            for (int i = 0; i < dialogsRecyclerView.getChildCount(); i++) {
                View childAt = dialogsRecyclerView.getChildAt(i);
                if (childAt != null && dialogsRecyclerView.getChildAdapterPosition(childAt) >= viewPage.dialogsAdapter.getDialogsCount() + 1) {
                    childAt.setAlpha(f);
                }
            }
        }
    }

    public void setScrollDisabled(boolean z) {
        for (ViewPage viewPage : this.viewPages) {
            ((LinearLayoutManager) viewPage.listView.getLayoutManager()).setScrollDisabled(z);
        }
    }

    private void updateDialogIndices() {
        if (this.viewPages == null) {
            return;
        }
        int i = 0;
        while (true) {
            ViewPage[] viewPageArr = this.viewPages;
            if (i >= viewPageArr.length) {
                return;
            }
            if (viewPageArr[i].getVisibility() == 0 && !this.viewPages[i].dialogsAdapter.getDialogsListIsFrozen()) {
                this.viewPages[i].updateList(false);
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVisibleRows(int i) {
        updateVisibleRows(i, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:101:0x014d  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x015a  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x016a A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x010e  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0124  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x012b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateVisibleRows(int i, boolean z) {
        RecyclerListView recyclerListView;
        int i2;
        int childCount;
        int i3;
        ArrayList<Long> arrayList;
        if ((this.dialogsListFrozen && (MessagesController.UPDATE_MASK_REORDER & i) == 0) || this.isPaused) {
            return;
        }
        int i4 = 0;
        while (i4 < 3) {
            ViewPage viewPage = null;
            if (i4 == 2) {
                SearchViewPager searchViewPager = this.searchViewPager;
                recyclerListView = searchViewPager != null ? searchViewPager.searchListView : null;
            } else {
                ViewPage[] viewPageArr = this.viewPages;
                if (viewPageArr != null) {
                    recyclerListView = i4 < viewPageArr.length ? viewPageArr[i4].listView : null;
                    if (recyclerListView == null || this.viewPages[i4].getVisibility() == 0) {
                        if (recyclerListView != null) {
                            viewPage = this.viewPages[i4];
                        }
                    }
                }
                i4++;
            }
            if (recyclerListView != null && recyclerListView.getAdapter() != null) {
                if (((MessagesController.UPDATE_MASK_NEW_MESSAGE & i) != 0 || i == 0) && viewPage != null) {
                    viewPage.updateList(false);
                } else {
                    int childCount2 = recyclerListView.getChildCount();
                    while (true) {
                        if (i2 >= childCount2) {
                            break;
                        }
                        View childAt = recyclerListView.getChildAt(i2);
                        if ((childAt instanceof DialogCell) && recyclerListView.getAdapter() != this.searchViewPager.dialogsSearchAdapter) {
                            DialogCell dialogCell = (DialogCell) childAt;
                            if ((MessagesController.UPDATE_MASK_REORDER & i) != 0) {
                                dialogCell.onReorderStateChanged(this.actionBar.isActionModeShowed(), true);
                                i2 = this.dialogsListFrozen ? i2 + 1 : 0;
                            }
                            if ((MessagesController.UPDATE_MASK_CHECK & i) != 0) {
                                dialogCell.setChecked(false, (MessagesController.UPDATE_MASK_CHAT & i) != 0);
                            } else {
                                if ((MessagesController.UPDATE_MASK_NEW_MESSAGE & i) != 0) {
                                    if (recyclerListView.getAdapter() != null) {
                                        viewPage.updateList(false);
                                        break;
                                    } else if (this.viewPages[i4].isDefaultDialogType() && AndroidUtilities.isTablet()) {
                                        dialogCell.setDialogSelected(dialogCell.getDialogId() == this.openedDialogId.dialogId);
                                    }
                                } else if ((MessagesController.UPDATE_MASK_SELECT_DIALOG & i) != 0) {
                                    if (this.viewPages[i4].isDefaultDialogType() && AndroidUtilities.isTablet()) {
                                        dialogCell.setDialogSelected(dialogCell.getDialogId() == this.openedDialogId.dialogId);
                                    }
                                } else {
                                    if (dialogCell.update(i, z)) {
                                        viewPage.updateList(false);
                                        break;
                                    }
                                    arrayList = this.selectedDialogs;
                                    if (arrayList != null) {
                                        dialogCell.setChecked(arrayList.contains(Long.valueOf(dialogCell.getDialogId())), false);
                                    }
                                    if (childAt instanceof UserCell) {
                                        ((UserCell) childAt).update(i);
                                    } else if (childAt instanceof ProfileSearchCell) {
                                        ProfileSearchCell profileSearchCell = (ProfileSearchCell) childAt;
                                        profileSearchCell.update(i);
                                        ArrayList<Long> arrayList2 = this.selectedDialogs;
                                        if (arrayList2 != null) {
                                            profileSearchCell.setChecked(arrayList2.contains(Long.valueOf(profileSearchCell.getDialogId())), false);
                                        }
                                    }
                                    if (this.dialogsListFrozen && (childAt instanceof RecyclerListView)) {
                                        RecyclerListView recyclerListView2 = (RecyclerListView) childAt;
                                        childCount = recyclerListView2.getChildCount();
                                        for (i3 = 0; i3 < childCount; i3++) {
                                            View childAt2 = recyclerListView2.getChildAt(i3);
                                            if (childAt2 instanceof HintDialogCell) {
                                                ((HintDialogCell) childAt2).update(i);
                                            }
                                        }
                                    }
                                }
                                arrayList = this.selectedDialogs;
                                if (arrayList != null) {
                                }
                                if (childAt instanceof UserCell) {
                                }
                                if (this.dialogsListFrozen) {
                                    RecyclerListView recyclerListView22 = (RecyclerListView) childAt;
                                    childCount = recyclerListView22.getChildCount();
                                    while (i3 < childCount) {
                                    }
                                }
                            }
                        }
                        if (childAt instanceof UserCell) {
                        }
                        if (this.dialogsListFrozen) {
                        }
                    }
                }
            }
            i4++;
        }
    }

    public void setDelegate(DialogsActivityDelegate dialogsActivityDelegate) {
        this.delegate = dialogsActivityDelegate;
    }

    public void setSearchString(String str) {
        this.searchString = str;
    }

    public void setInitialSearchString(String str) {
        this.initialSearchString = str;
    }

    public boolean isMainDialogList() {
        return this.delegate == null && this.searchString == null;
    }

    public boolean isArchive() {
        return this.folderId == 1;
    }

    public void setInitialSearchType(int i) {
        this.initialSearchType = i;
    }

    private boolean checkCanWrite(long j) {
        if (this.addToGroupAlertString == null && this.initialDialogsType != 15 && this.checkCanWrite) {
            if (DialogObject.isChatDialog(j)) {
                long j2 = -j;
                TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(j2));
                if (!ChatObject.isChannel(chat) || chat.megagroup) {
                    return true;
                }
                if (this.cantSendToChannels || !ChatObject.isCanWriteToChannel(j2, this.currentAccount) || this.hasPoll == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    builder.setTitle(LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle));
                    if (this.hasPoll == 2) {
                        builder.setMessage(LocaleController.getString("PublicPollCantForward", R.string.PublicPollCantForward));
                    } else {
                        builder.setMessage(LocaleController.getString("ChannelCantSendMessage", R.string.ChannelCantSendMessage));
                    }
                    builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), null);
                    showDialog(builder.create());
                    return false;
                }
                return true;
            } else if (DialogObject.isEncryptedDialog(j)) {
                if (this.hasPoll != 0 || this.hasInvoice) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
                    builder2.setTitle(LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle));
                    if (this.hasPoll != 0) {
                        builder2.setMessage(LocaleController.getString("PollCantForwardSecretChat", R.string.PollCantForwardSecretChat));
                    } else {
                        builder2.setMessage(LocaleController.getString("InvoiceCantForwardSecretChat", R.string.InvoiceCantForwardSecretChat));
                    }
                    builder2.setNegativeButton(LocaleController.getString("OK", R.string.OK), null);
                    showDialog(builder2.create());
                    return false;
                }
                return true;
            } else {
                return true;
            }
        }
        return true;
    }

    public void didSelectResult(long j, int i, boolean z, boolean z2) {
        didSelectResult(j, i, z, z2, null);
    }

    public void didSelectResult(final long j, final int i, boolean z, final boolean z2, final TopicsFragment topicsFragment) {
        final TLRPC$Chat tLRPC$Chat;
        final TLRPC$User tLRPC$User;
        String string;
        String formatStringSimple;
        String string2;
        TLRPC$TL_forumTopic findTopic;
        if (checkCanWrite(j)) {
            int i2 = this.initialDialogsType;
            if (i2 == 11 || i2 == 12 || i2 == 13) {
                if (this.checkingImportDialog) {
                    return;
                }
                if (DialogObject.isUserDialog(j)) {
                    TLRPC$User user = getMessagesController().getUser(Long.valueOf(j));
                    if (!user.mutual_contact) {
                        getUndoView().showWithAction(j, 45, (Runnable) null);
                        return;
                    } else {
                        tLRPC$User = user;
                        tLRPC$Chat = null;
                    }
                } else {
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-j));
                    if (!ChatObject.hasAdminRights(chat) || !ChatObject.canChangeChatInfo(chat)) {
                        getUndoView().showWithAction(j, 46, (Runnable) null);
                        return;
                    } else {
                        tLRPC$Chat = chat;
                        tLRPC$User = null;
                    }
                }
                final AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
                final TLRPC$TL_messages_checkHistoryImportPeer tLRPC$TL_messages_checkHistoryImportPeer = new TLRPC$TL_messages_checkHistoryImportPeer();
                tLRPC$TL_messages_checkHistoryImportPeer.peer = getMessagesController().getInputPeer(j);
                getConnectionsManager().sendRequest(tLRPC$TL_messages_checkHistoryImportPeer, new RequestDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda82
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        DialogsActivity.this.lambda$didSelectResult$74(alertDialog, tLRPC$User, tLRPC$Chat, j, z2, tLRPC$TL_messages_checkHistoryImportPeer, tLObject, tLRPC$TL_error);
                    }
                });
                try {
                    alertDialog.showDelayed(300L);
                } catch (Exception unused) {
                }
            } else if (!z || ((this.selectAlertString == null || this.selectAlertStringGroup == null) && this.addToGroupAlertString == null)) {
                if (i2 == 15) {
                    final Runnable runnable = new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda55
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.this.lambda$didSelectResult$76(j, i, z2, topicsFragment);
                        }
                    };
                    Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda56
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogsActivity.this.lambda$didSelectResult$78(j, runnable);
                        }
                    };
                    if (j < 0) {
                        showSendToBotAlert(getMessagesController().getChat(Long.valueOf(-j)), runnable2, (Runnable) null);
                    } else {
                        showSendToBotAlert(getMessagesController().getUser(Long.valueOf(j)), runnable2, (Runnable) null);
                    }
                } else if (this.delegate != null) {
                    ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
                    arrayList.add(MessagesStorage.TopicKey.of(j, i));
                    if (this.delegate.didSelectDialogs(this, arrayList, null, z2, topicsFragment) && this.resetDelegate) {
                        this.delegate = null;
                    }
                } else {
                    finishFragment();
                }
            } else if (getParentActivity() == null) {
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                if (DialogObject.isEncryptedDialog(j)) {
                    TLRPC$User user2 = getMessagesController().getUser(Long.valueOf(getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j))).user_id));
                    if (user2 == null) {
                        return;
                    }
                    string = LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle);
                    formatStringSimple = LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(user2));
                    string2 = LocaleController.getString("Send", R.string.Send);
                } else if (DialogObject.isUserDialog(j)) {
                    if (j == getUserConfig().getClientUserId()) {
                        string = LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle);
                        formatStringSimple = LocaleController.formatStringSimple(this.selectAlertStringGroup, LocaleController.getString("SavedMessages", R.string.SavedMessages));
                        string2 = LocaleController.getString("Send", R.string.Send);
                    } else {
                        TLRPC$User user3 = getMessagesController().getUser(Long.valueOf(j));
                        if (user3 == null || this.selectAlertString == null) {
                            return;
                        }
                        string = LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle);
                        formatStringSimple = LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(user3));
                        string2 = LocaleController.getString("Send", R.string.Send);
                    }
                } else {
                    TLRPC$Chat chat2 = getMessagesController().getChat(Long.valueOf(-j));
                    if (chat2 == null) {
                        return;
                    }
                    String str = chat2.title;
                    if (i != 0 && (findTopic = getMessagesController().getTopicsController().findTopic(chat2.id, i)) != null) {
                        str = ((Object) str) + " " + findTopic.title;
                    }
                    if (this.addToGroupAlertString != null) {
                        string = LocaleController.getString("AddToTheGroupAlertTitle", R.string.AddToTheGroupAlertTitle);
                        formatStringSimple = LocaleController.formatStringSimple(this.addToGroupAlertString, str);
                        string2 = LocaleController.getString("Add", R.string.Add);
                    } else {
                        string = LocaleController.getString("SendMessageTitle", R.string.SendMessageTitle);
                        formatStringSimple = LocaleController.formatStringSimple(this.selectAlertStringGroup, str);
                        string2 = LocaleController.getString("Send", R.string.Send);
                    }
                }
                String str2 = string2;
                builder.setTitle(string);
                builder.setMessage(AndroidUtilities.replaceTags(formatStringSimple));
                builder.setPositiveButton(str2, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda16
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i3) {
                        DialogsActivity.this.lambda$didSelectResult$75(j, i, topicsFragment, dialogInterface, i3);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                showDialog(builder.create());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$74(final AlertDialog alertDialog, final TLRPC$User tLRPC$User, final TLRPC$Chat tLRPC$Chat, final long j, final boolean z, final TLRPC$TL_messages_checkHistoryImportPeer tLRPC$TL_messages_checkHistoryImportPeer, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda61
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$didSelectResult$73(alertDialog, tLObject, tLRPC$User, tLRPC$Chat, j, z, tLRPC$TL_error, tLRPC$TL_messages_checkHistoryImportPeer);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$73(AlertDialog alertDialog, TLObject tLObject, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, final long j, final boolean z, TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_messages_checkHistoryImportPeer tLRPC$TL_messages_checkHistoryImportPeer) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.checkingImportDialog = false;
        if (tLObject != null) {
            AlertsCreator.createImportDialogAlert(this, this.arguments.getString("importTitle"), ((TLRPC$TL_messages_checkedHistoryImportPeer) tLObject).confirm_text, tLRPC$User, tLRPC$Chat, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda57
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsActivity.this.lambda$didSelectResult$72(j, z);
                }
            });
            return;
        }
        AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, this, tLRPC$TL_messages_checkHistoryImportPeer, new Object[0]);
        getNotificationCenter().postNotificationName(NotificationCenter.historyImportProgressChanged, Long.valueOf(j), tLRPC$TL_messages_checkHistoryImportPeer, tLRPC$TL_error);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$72(long j, boolean z) {
        setDialogsListFrozen(true);
        ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
        arrayList.add(MessagesStorage.TopicKey.of(j, 0));
        this.delegate.didSelectDialogs(this, arrayList, null, z, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$75(long j, int i, TopicsFragment topicsFragment, DialogInterface dialogInterface, int i2) {
        didSelectResult(j, i, false, false, topicsFragment);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$76(long j, int i, boolean z, TopicsFragment topicsFragment) {
        if (this.delegate != null) {
            ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
            arrayList.add(MessagesStorage.TopicKey.of(j, i));
            this.delegate.didSelectDialogs(this, arrayList, null, z, topicsFragment);
            if (this.resetDelegate) {
                this.delegate = null;
                return;
            }
            return;
        }
        finishFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didSelectResult$78(long j, final Runnable runnable) {
        if (this.requestPeerType.bot_admin_rights != null) {
            getMessagesController().setUserAdminRole(-j, getMessagesController().getUser(Long.valueOf(this.requestPeerBotId)), this.requestPeerType.bot_admin_rights, null, false, this, true, true, null, runnable, new MessagesController.ErrorDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda69
                @Override // org.telegram.messenger.MessagesController.ErrorDelegate
                public final boolean run(TLRPC$TL_error tLRPC$TL_error) {
                    boolean run;
                    run = runnable.run();
                    return run;
                }
            });
            return;
        }
        runnable.run();
    }

    private void showSendToBotAlert(TLRPC$User tLRPC$User, final Runnable runnable, final Runnable runnable2) {
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.requestPeerBotId));
        showDialog(new AlertDialog.Builder(getContext()).setTitle(LocaleController.formatString(R.string.AreYouSureSendChatToBotTitle, UserObject.getFirstName(tLRPC$User), UserObject.getFirstName(user))).setMessage(TextUtils.concat(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.AreYouSureSendChatToBotMessage, UserObject.getFirstName(tLRPC$User), UserObject.getFirstName(user))))).setPositiveButton(LocaleController.formatString("Send", R.string.Send, new Object[0]), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda7
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                runnable.run();
            }
        }).setNegativeButton(LocaleController.formatString("Cancel", R.string.Cancel, new Object[0]), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda10
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                DialogsActivity.lambda$showSendToBotAlert$80(runnable2, dialogInterface, i);
            }
        }).create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSendToBotAlert$80(Runnable runnable, DialogInterface dialogInterface, int i) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSendToBotAlert(TLRPC$Chat tLRPC$Chat, final Runnable runnable, final Runnable runnable2) {
        CharSequence charSequence;
        String formatString;
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.requestPeerBotId));
        boolean isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat);
        AlertDialog.Builder title = new AlertDialog.Builder(getContext()).setTitle(LocaleController.formatString(R.string.AreYouSureSendChatToBotTitle, tLRPC$Chat.title, UserObject.getFirstName(user)));
        CharSequence[] charSequenceArr = new CharSequence[2];
        charSequenceArr[0] = AndroidUtilities.replaceTags(LocaleController.formatString(R.string.AreYouSureSendChatToBotMessage, tLRPC$Chat.title, UserObject.getFirstName(user)));
        Boolean bool = this.requestPeerType.bot_participant;
        if ((bool == null || !bool.booleanValue() || getMessagesController().isInChatCached(tLRPC$Chat, user)) && this.requestPeerType.bot_admin_rights == null) {
            charSequence = "";
        } else {
            CharSequence[] charSequenceArr2 = new CharSequence[2];
            charSequenceArr2[0] = "\n\n";
            if (this.requestPeerType.bot_admin_rights == null) {
                formatString = LocaleController.formatString(R.string.AreYouSureSendChatToBotAdd, UserObject.getFirstName(user), tLRPC$Chat.title);
            } else {
                formatString = LocaleController.formatString(R.string.AreYouSureSendChatToBotAddRights, UserObject.getFirstName(user), tLRPC$Chat.title, RequestPeerRequirementsCell.rightsToString(this.requestPeerType.bot_admin_rights, isChannelAndNotMegaGroup));
            }
            charSequenceArr2[1] = AndroidUtilities.replaceTags(formatString);
            charSequence = TextUtils.concat(charSequenceArr2);
        }
        charSequenceArr[1] = charSequence;
        showDialog(title.setMessage(TextUtils.concat(charSequenceArr)).setPositiveButton(LocaleController.formatString("Send", R.string.Send, new Object[0]), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda8
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                runnable.run();
            }
        }).setNegativeButton(LocaleController.formatString("Cancel", R.string.Cancel, new Object[0]), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda9
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                DialogsActivity.lambda$showSendToBotAlert$82(runnable2, dialogInterface, i);
            }
        }).create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$showSendToBotAlert$82(Runnable runnable, DialogInterface dialogInterface, int i) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public RLottieImageView getFloatingButton() {
        return this.floatingButton;
    }

    private boolean onSendLongClick(View view) {
        Activity parentActivity = getParentActivity();
        Theme.ResourcesProvider resourceProvider = getResourceProvider();
        if (parentActivity == null) {
            return false;
        }
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(parentActivity, resourceProvider);
        actionBarPopupWindowLayout.setAnimationEnabled(false);
        actionBarPopupWindowLayout.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.DialogsActivity.47
            private Rect popupRect = new Rect();

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == 0 && DialogsActivity.this.sendPopupWindow != null && DialogsActivity.this.sendPopupWindow.isShowing()) {
                    view2.getHitRect(this.popupRect);
                    if (this.popupRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        return false;
                    }
                    DialogsActivity.this.sendPopupWindow.dismiss();
                    return false;
                }
                return false;
            }
        });
        actionBarPopupWindowLayout.setDispatchKeyEventListener(new ActionBarPopupWindow.OnDispatchKeyEventListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda83
            @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
            public final void onDispatchKeyEvent(KeyEvent keyEvent) {
                DialogsActivity.this.lambda$onSendLongClick$83(keyEvent);
            }
        });
        actionBarPopupWindowLayout.setShownFromBottom(false);
        actionBarPopupWindowLayout.setupRadialSelectors(getThemedColor("dialogButtonSelector"));
        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem((Context) parentActivity, true, true, resourceProvider);
        actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("SendWithoutSound", R.string.SendWithoutSound), R.drawable.input_notify_off);
        actionBarMenuSubItem.setMinimumWidth(AndroidUtilities.dp(196.0f));
        actionBarPopupWindowLayout.addView((View) actionBarMenuSubItem, LayoutHelper.createLinear(-1, 48));
        actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda23
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                DialogsActivity.this.lambda$onSendLongClick$84(view2);
            }
        });
        linearLayout.addView(actionBarPopupWindowLayout, LayoutHelper.createLinear(-1, -2));
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(linearLayout, -2, -2);
        this.sendPopupWindow = actionBarPopupWindow;
        actionBarPopupWindow.setAnimationEnabled(false);
        this.sendPopupWindow.setAnimationStyle(R.style.PopupContextAnimation2);
        this.sendPopupWindow.setOutsideTouchable(true);
        this.sendPopupWindow.setClippingEnabled(true);
        this.sendPopupWindow.setInputMethodMode(2);
        this.sendPopupWindow.setSoftInputMode(0);
        this.sendPopupWindow.getContentView().setFocusableInTouchMode(true);
        SharedConfig.removeScheduledOrNoSoundHint();
        linearLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
        this.sendPopupWindow.setFocusable(true);
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        this.sendPopupWindow.showAtLocation(view, 51, ((iArr[0] + view.getMeasuredWidth()) - linearLayout.getMeasuredWidth()) + AndroidUtilities.dp(8.0f), (iArr[1] - linearLayout.getMeasuredHeight()) - AndroidUtilities.dp(2.0f));
        this.sendPopupWindow.dimBehind();
        view.performHapticFeedback(3, 2);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$83(KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0 && (actionBarPopupWindow = this.sendPopupWindow) != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$84(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        this.notify = false;
        if (this.delegate == null || this.selectedDialogs.isEmpty()) {
            return;
        }
        ArrayList<MessagesStorage.TopicKey> arrayList = new ArrayList<>();
        for (int i = 0; i < this.selectedDialogs.size(); i++) {
            arrayList.add(MessagesStorage.TopicKey.of(this.selectedDialogs.get(i).longValue(), 0));
        }
        this.delegate.didSelectDialogs(this, arrayList, this.commentView.getFieldText(), false, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:111:0x087a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x03eb  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        RecyclerListView recyclerListView;
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda85
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                DialogsActivity.this.lambda$getThemeDescriptions$85();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        if (this.movingView != null) {
            arrayList.add(new ThemeDescription(this.movingView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
        }
        if (this.doneItem != null) {
            arrayList.add(new ThemeDescription(this.doneItem, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "actionBarDefaultSelector"));
        }
        char c = 0;
        if (this.folderId == 0) {
            if (this.onlySelect) {
                arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
            }
            arrayList.add(new ThemeDescription(this.fragmentView, 0, null, this.actionBarDefaultPaint, null, null, "actionBarDefault"));
            if (this.searchViewPager != null) {
                arrayList.add(new ThemeDescription(this.searchViewPager.searchListView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
            }
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, themeDescriptionDelegate, "actionBarDefaultIcon"));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, new Drawable[]{Theme.dialogs_holidayDrawable}, null, "actionBarDefaultTitle"));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"));
        } else {
            arrayList.add(new ThemeDescription(this.fragmentView, 0, null, this.actionBarDefaultPaint, null, null, "actionBarDefaultArchived"));
            if (this.searchViewPager != null) {
                arrayList.add(new ThemeDescription(this.searchViewPager.searchListView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefaultArchived"));
            }
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultArchivedIcon"));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, new Drawable[]{Theme.dialogs_holidayDrawable}, null, "actionBarDefaultArchivedTitle"));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultArchivedSelector"));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultArchivedSearch"));
            arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchArchivedPlaceholder"));
        }
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_ITEMSCOLOR, null, null, null, null, "actionBarActionModeDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_TOPBACKGROUND, null, null, null, null, "actionBarActionModeDefaultTop"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_SELECTORCOLOR, null, null, null, null, "actionBarActionModeDefaultSelector"));
        arrayList.add(new ThemeDescription(this.selectedDialogsCountTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "actionBarActionModeDefaultIcon"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "actionBarDefaultSubmenuBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "actionBarDefaultSubmenuItem"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "actionBarDefaultSubmenuItemIcon"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "dialogButtonSelector"));
        if (this.filterTabsView != null) {
            if (this.actionBar.isActionModeShowed()) {
                arrayList.add(new ThemeDescription(this.filterTabsView, 0, new Class[]{FilterTabsView.class}, new String[]{"selectorDrawable"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "profile_tabSelectedLine"));
                arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FilterTabsView.TabView.class}, null, null, null, "profile_tabSelectedText"));
                arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FilterTabsView.TabView.class}, null, null, null, "profile_tabText"));
                arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{FilterTabsView.TabView.class}, null, null, null, "profile_tabSelector"));
            } else {
                arrayList.add(new ThemeDescription(this.filterTabsView, 0, new Class[]{FilterTabsView.class}, new String[]{"selectorDrawable"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "actionBarTabLine"));
                arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FilterTabsView.TabView.class}, null, null, null, "actionBarTabActiveText"));
                arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FilterTabsView.TabView.class}, null, null, null, "actionBarTabUnactiveText"));
                arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), ThemeDescription.FLAG_SELECTOR, null, null, null, null, "actionBarTabSelector"));
            }
            arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), 0, new Class[]{FilterTabsView.TabView.class}, null, null, null, "chats_tabUnreadActiveBackground"));
            arrayList.add(new ThemeDescription(this.filterTabsView.getTabsContainer(), 0, new Class[]{FilterTabsView.TabView.class}, null, null, null, "chats_tabUnreadUnactiveBackground"));
        }
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionIcon"));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "chats_actionBackground"));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "chats_actionPressedBackground"));
        arrayList.addAll(SimpleThemeDescription.createThemeDescriptions(new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda86
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                DialogsActivity.this.lambda$getThemeDescriptions$86();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        }, "actionBarActionModeDefault", "actionBarActionModeDefaultIcon"));
        int i = 0;
        while (true) {
            if (i >= 3) {
                break;
            } else if (i == 2) {
                SearchViewPager searchViewPager = this.searchViewPager;
                if (searchViewPager == null) {
                    i++;
                } else {
                    recyclerListView = searchViewPager.searchListView;
                    if (recyclerListView == null) {
                        RecyclerListView recyclerListView2 = recyclerListView;
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, Theme.avatarDrawables, null, "avatar_text"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_countPaint, null, null, "chats_unreadCounter"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_countGrayPaint, null, null, "chats_unreadCounterMuted"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_countTextPaint, null, null, "chats_unreadCounterText"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_lockDrawable}, null, "chats_secretIcon"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_scamDrawable, Theme.dialogs_fakeDrawable}, null, "chats_draft"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_pinnedDrawable, Theme.dialogs_reorderDrawable}, null, "chats_pinnedIcon"));
                        TextPaint[] textPaintArr = Theme.dialogs_namePaint;
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, (String[]) null, new Paint[]{textPaintArr[0], textPaintArr[1], Theme.dialogs_searchNamePaint}, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_name"));
                        TextPaint[] textPaintArr2 = Theme.dialogs_nameEncryptedPaint;
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, (String[]) null, new Paint[]{textPaintArr2[0], textPaintArr2[1], Theme.dialogs_searchNameEncryptedPaint}, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_secretName"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_messagePaint[1], null, null, "chats_message_threeLines"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_messagePaint[0], null, null, "chats_message"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_messageNamePaint, null, null, "chats_nameMessage_threeLines"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, null, null, "chats_draft"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, (String[]) null, Theme.dialogs_messagePrintingPaint, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_actionMessage"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_timePaint, null, null, "chats_date"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_pinnedPaint, null, null, "chats_pinnedOverlay"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_tabletSeletedPaint, null, null, "chats_tabletSelectedOverlay"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_checkDrawable}, null, "chats_sentCheck"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_checkReadDrawable, Theme.dialogs_halfCheckDrawable}, null, "chats_sentReadCheck"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_clockDrawable}, null, "chats_sentClock"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, Theme.dialogs_errorPaint, null, null, "chats_sentError"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_errorDrawable}, null, "chats_sentErrorIcon"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedCheckDrawable}, null, "chats_verifiedCheck"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class, ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedDrawable}, null, "chats_verifiedBackground"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_muteDrawable}, null, "chats_muteIcon"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, new Drawable[]{Theme.dialogs_mentionDrawable}, null, "chats_mentionIcon"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, null, null, "chats_archivePinBackground"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, null, null, "chats_archiveBackground"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, null, null, "chats_onlineCircle"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{DialogCell.class}, null, null, null, "windowBackgroundWhite"));
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_CHECKBOX, new Class[]{DialogCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhite"));
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{DialogCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "checkboxCheck"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "progressCircle"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_offlinePaint, null, null, "windowBackgroundWhiteGrayText3"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_onlinePaint, null, null, "windowBackgroundWhiteBlueText3"));
                        GraySectionCell.createThemeDescriptions(arrayList, recyclerListView);
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{HashtagSearchCell.class}, null, null, null, "windowBackgroundWhiteBlackText"));
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGrayShadow"));
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ShadowSectionCell.class}, null, null, null, "windowBackgroundGray"));
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGray"));
                        arrayList.add(new ThemeDescription(recyclerListView2, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
                        arrayList.add(new ThemeDescription(recyclerListView2, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueText2"));
                    }
                    i++;
                }
            } else {
                ViewPage[] viewPageArr = this.viewPages;
                if (viewPageArr == null) {
                    i++;
                } else {
                    recyclerListView = i < viewPageArr.length ? viewPageArr[i].listView : null;
                    if (recyclerListView == null) {
                    }
                    i++;
                }
            }
        }
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundOrange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundViolet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundGreen"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundCyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundPink"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundSaved"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_background2Red"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_background2Orange"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_background2Violet"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_background2Green"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_background2Cyan"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_background2Blue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_background2Pink"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_background2Saved"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundArchived"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_backgroundArchivedHidden"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "chats_nameMessage"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "chats_draft"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "chats_attachMessage"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "chats_nameArchived"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "chats_nameMessageArchived"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "chats_nameMessageArchived_threeLines"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "chats_messageArchived"));
        if (this.viewPages != null) {
            int i2 = 0;
            while (i2 < this.viewPages.length) {
                if (this.folderId == 0) {
                    arrayList.add(new ThemeDescription(this.viewPages[i2].listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
                } else {
                    arrayList.add(new ThemeDescription(this.viewPages[i2].listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefaultArchived"));
                }
                DialogsRecyclerView dialogsRecyclerView = this.viewPages[i2].listView;
                int i3 = ThemeDescription.FLAG_TEXTCOLOR;
                Class[] clsArr = new Class[1];
                clsArr[c] = DialogsEmptyCell.class;
                arrayList.add(new ThemeDescription(dialogsRecyclerView, i3, clsArr, new String[]{"emptyTextView1"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_nameMessage_threeLines"));
                DialogsRecyclerView dialogsRecyclerView2 = this.viewPages[i2].listView;
                int i4 = ThemeDescription.FLAG_TEXTCOLOR;
                Class[] clsArr2 = new Class[1];
                clsArr2[c] = DialogsEmptyCell.class;
                arrayList.add(new ThemeDescription(dialogsRecyclerView2, i4, clsArr2, new String[]{"emptyTextView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_message"));
                if (SharedConfig.archiveHidden) {
                    DialogsRecyclerView dialogsRecyclerView3 = this.viewPages[i2].listView;
                    Class[] clsArr3 = new Class[1];
                    clsArr3[c] = DialogCell.class;
                    RLottieDrawable[] rLottieDrawableArr = new RLottieDrawable[1];
                    rLottieDrawableArr[c] = Theme.dialogs_archiveAvatarDrawable;
                    arrayList.add(new ThemeDescription(dialogsRecyclerView3, 0, clsArr3, rLottieDrawableArr, "Arrow1", "avatar_backgroundArchivedHidden"));
                    DialogsRecyclerView dialogsRecyclerView4 = this.viewPages[i2].listView;
                    Class[] clsArr4 = new Class[1];
                    clsArr4[c] = DialogCell.class;
                    RLottieDrawable[] rLottieDrawableArr2 = new RLottieDrawable[1];
                    rLottieDrawableArr2[c] = Theme.dialogs_archiveAvatarDrawable;
                    arrayList.add(new ThemeDescription(dialogsRecyclerView4, 0, clsArr4, rLottieDrawableArr2, "Arrow2", "avatar_backgroundArchivedHidden"));
                } else {
                    DialogsRecyclerView dialogsRecyclerView5 = this.viewPages[i2].listView;
                    Class[] clsArr5 = new Class[1];
                    clsArr5[c] = DialogCell.class;
                    RLottieDrawable[] rLottieDrawableArr3 = new RLottieDrawable[1];
                    rLottieDrawableArr3[c] = Theme.dialogs_archiveAvatarDrawable;
                    arrayList.add(new ThemeDescription(dialogsRecyclerView5, 0, clsArr5, rLottieDrawableArr3, "Arrow1", "avatar_backgroundArchived"));
                    DialogsRecyclerView dialogsRecyclerView6 = this.viewPages[i2].listView;
                    Class[] clsArr6 = new Class[1];
                    clsArr6[c] = DialogCell.class;
                    RLottieDrawable[] rLottieDrawableArr4 = new RLottieDrawable[1];
                    rLottieDrawableArr4[c] = Theme.dialogs_archiveAvatarDrawable;
                    arrayList.add(new ThemeDescription(dialogsRecyclerView6, 0, clsArr6, rLottieDrawableArr4, "Arrow2", "avatar_backgroundArchived"));
                }
                DialogsRecyclerView dialogsRecyclerView7 = this.viewPages[i2].listView;
                Class[] clsArr7 = new Class[1];
                clsArr7[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr5 = new RLottieDrawable[1];
                rLottieDrawableArr5[c] = Theme.dialogs_archiveAvatarDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView7, 0, clsArr7, rLottieDrawableArr5, "Box2", "avatar_text"));
                DialogsRecyclerView dialogsRecyclerView8 = this.viewPages[i2].listView;
                Class[] clsArr8 = new Class[1];
                clsArr8[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr6 = new RLottieDrawable[1];
                rLottieDrawableArr6[c] = Theme.dialogs_archiveAvatarDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView8, 0, clsArr8, rLottieDrawableArr6, "Box1", "avatar_text"));
                DialogsRecyclerView dialogsRecyclerView9 = this.viewPages[i2].listView;
                Class[] clsArr9 = new Class[1];
                clsArr9[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr7 = new RLottieDrawable[1];
                rLottieDrawableArr7[c] = Theme.dialogs_pinArchiveDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView9, 0, clsArr9, rLottieDrawableArr7, "Arrow", "chats_archiveIcon"));
                DialogsRecyclerView dialogsRecyclerView10 = this.viewPages[i2].listView;
                Class[] clsArr10 = new Class[1];
                clsArr10[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr8 = new RLottieDrawable[1];
                rLottieDrawableArr8[c] = Theme.dialogs_pinArchiveDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView10, 0, clsArr10, rLottieDrawableArr8, "Line", "chats_archiveIcon"));
                DialogsRecyclerView dialogsRecyclerView11 = this.viewPages[i2].listView;
                Class[] clsArr11 = new Class[1];
                clsArr11[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr9 = new RLottieDrawable[1];
                rLottieDrawableArr9[c] = Theme.dialogs_unpinArchiveDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView11, 0, clsArr11, rLottieDrawableArr9, "Arrow", "chats_archiveIcon"));
                DialogsRecyclerView dialogsRecyclerView12 = this.viewPages[i2].listView;
                Class[] clsArr12 = new Class[1];
                clsArr12[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr10 = new RLottieDrawable[1];
                rLottieDrawableArr10[c] = Theme.dialogs_unpinArchiveDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView12, 0, clsArr12, rLottieDrawableArr10, "Line", "chats_archiveIcon"));
                DialogsRecyclerView dialogsRecyclerView13 = this.viewPages[i2].listView;
                Class[] clsArr13 = new Class[1];
                clsArr13[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr11 = new RLottieDrawable[1];
                rLottieDrawableArr11[c] = Theme.dialogs_archiveDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView13, 0, clsArr13, rLottieDrawableArr11, "Arrow", "chats_archiveBackground"));
                DialogsRecyclerView dialogsRecyclerView14 = this.viewPages[i2].listView;
                Class[] clsArr14 = new Class[1];
                clsArr14[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr12 = new RLottieDrawable[1];
                rLottieDrawableArr12[c] = Theme.dialogs_archiveDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView14, 0, clsArr14, rLottieDrawableArr12, "Box2", "chats_archiveIcon"));
                DialogsRecyclerView dialogsRecyclerView15 = this.viewPages[i2].listView;
                Class[] clsArr15 = new Class[1];
                clsArr15[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr13 = new RLottieDrawable[1];
                rLottieDrawableArr13[c] = Theme.dialogs_archiveDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView15, 0, clsArr15, rLottieDrawableArr13, "Box1", "chats_archiveIcon"));
                DialogsRecyclerView dialogsRecyclerView16 = this.viewPages[i2].listView;
                Class[] clsArr16 = new Class[1];
                clsArr16[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr14 = new RLottieDrawable[1];
                rLottieDrawableArr14[c] = Theme.dialogs_hidePsaDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView16, 0, clsArr16, rLottieDrawableArr14, "Line 1", "chats_archiveBackground"));
                DialogsRecyclerView dialogsRecyclerView17 = this.viewPages[i2].listView;
                Class[] clsArr17 = new Class[1];
                clsArr17[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr15 = new RLottieDrawable[1];
                rLottieDrawableArr15[c] = Theme.dialogs_hidePsaDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView17, 0, clsArr17, rLottieDrawableArr15, "Line 2", "chats_archiveBackground"));
                DialogsRecyclerView dialogsRecyclerView18 = this.viewPages[i2].listView;
                Class[] clsArr18 = new Class[1];
                clsArr18[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr16 = new RLottieDrawable[1];
                rLottieDrawableArr16[c] = Theme.dialogs_hidePsaDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView18, 0, clsArr18, rLottieDrawableArr16, "Line 3", "chats_archiveBackground"));
                DialogsRecyclerView dialogsRecyclerView19 = this.viewPages[i2].listView;
                Class[] clsArr19 = new Class[1];
                clsArr19[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr17 = new RLottieDrawable[1];
                rLottieDrawableArr17[c] = Theme.dialogs_hidePsaDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView19, 0, clsArr19, rLottieDrawableArr17, "Cup Red", "chats_archiveIcon"));
                DialogsRecyclerView dialogsRecyclerView20 = this.viewPages[i2].listView;
                Class[] clsArr20 = new Class[1];
                clsArr20[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr18 = new RLottieDrawable[1];
                rLottieDrawableArr18[c] = Theme.dialogs_hidePsaDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView20, 0, clsArr20, rLottieDrawableArr18, "Box", "chats_archiveIcon"));
                DialogsRecyclerView dialogsRecyclerView21 = this.viewPages[i2].listView;
                Class[] clsArr21 = new Class[1];
                clsArr21[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr19 = new RLottieDrawable[1];
                rLottieDrawableArr19[c] = Theme.dialogs_unarchiveDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView21, 0, clsArr21, rLottieDrawableArr19, "Arrow1", "chats_archiveIcon"));
                DialogsRecyclerView dialogsRecyclerView22 = this.viewPages[i2].listView;
                Class[] clsArr22 = new Class[1];
                clsArr22[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr20 = new RLottieDrawable[1];
                rLottieDrawableArr20[c] = Theme.dialogs_unarchiveDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView22, 0, clsArr22, rLottieDrawableArr20, "Arrow2", "chats_archivePinBackground"));
                DialogsRecyclerView dialogsRecyclerView23 = this.viewPages[i2].listView;
                Class[] clsArr23 = new Class[1];
                clsArr23[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr21 = new RLottieDrawable[1];
                rLottieDrawableArr21[c] = Theme.dialogs_unarchiveDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView23, 0, clsArr23, rLottieDrawableArr21, "Box2", "chats_archiveIcon"));
                DialogsRecyclerView dialogsRecyclerView24 = this.viewPages[i2].listView;
                Class[] clsArr24 = new Class[1];
                clsArr24[c] = DialogCell.class;
                RLottieDrawable[] rLottieDrawableArr22 = new RLottieDrawable[1];
                rLottieDrawableArr22[c] = Theme.dialogs_unarchiveDrawable;
                arrayList.add(new ThemeDescription(dialogsRecyclerView24, 0, clsArr24, rLottieDrawableArr22, "Box1", "chats_archiveIcon"));
                DialogsRecyclerView dialogsRecyclerView25 = this.viewPages[i2].listView;
                Class[] clsArr25 = new Class[1];
                clsArr25[c] = UserCell.class;
                arrayList.add(new ThemeDescription(dialogsRecyclerView25, 0, clsArr25, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
                DialogsRecyclerView dialogsRecyclerView26 = this.viewPages[i2].listView;
                Class[] clsArr26 = new Class[1];
                clsArr26[c] = UserCell.class;
                int i5 = i2;
                arrayList.add(new ThemeDescription(dialogsRecyclerView26, 0, clsArr26, new String[]{"statusColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, "windowBackgroundWhiteGrayText"));
                arrayList.add(new ThemeDescription(this.viewPages[i5].listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, "windowBackgroundWhiteBlueText"));
                arrayList.add(new ThemeDescription(this.viewPages[i5].listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueText4"));
                arrayList.add(new ThemeDescription(this.viewPages[i5].listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueText4"));
                arrayList.add(new ThemeDescription(this.viewPages[i5].progressView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"));
                ViewPager archiveHintCellPager = this.viewPages[i5].dialogsAdapter.getArchiveHintCellPager();
                arrayList.add(new ThemeDescription(archiveHintCellPager, 0, new Class[]{ArchiveHintInnerCell.class}, new String[]{"imageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_nameMessage_threeLines"));
                arrayList.add(new ThemeDescription(archiveHintCellPager, 0, new Class[]{ArchiveHintInnerCell.class}, new String[]{"imageView2"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_unreadCounter"));
                arrayList.add(new ThemeDescription(archiveHintCellPager, 0, new Class[]{ArchiveHintInnerCell.class}, new String[]{"headerTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_nameMessage_threeLines"));
                arrayList.add(new ThemeDescription(archiveHintCellPager, 0, new Class[]{ArchiveHintInnerCell.class}, new String[]{"messageTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_message"));
                arrayList.add(new ThemeDescription(archiveHintCellPager, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefaultArchived"));
                i2 = i5 + 1;
                c = 0;
            }
        }
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "chats_archivePullDownBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "chats_archivePullDownBackgroundActive"));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "chats_menuBackground"));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, "chats_menuName"));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, "chats_menuPhone"));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, "chats_menuPhoneCats"));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, "chats_menuCloudBackgroundCats"));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, "chat_serviceBackground"));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, "chats_menuTopShadow"));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerProfileCell.class}, null, null, null, "chats_menuTopShadowCats"));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{DrawerProfileCell.class}, new String[]{"darkThemeView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_menuName"));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{DrawerProfileCell.class}, null, null, themeDescriptionDelegate, "chats_menuTopBackgroundCats"));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{DrawerProfileCell.class}, null, null, themeDescriptionDelegate, "chats_menuTopBackground"));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{DrawerActionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_menuItemIcon"));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerActionCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_menuItemText"));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerUserCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_menuItemText"));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DrawerUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_unreadCounterText"));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DrawerUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_unreadCounter"));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{DrawerUserCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_menuBackground"));
        arrayList.add(new ThemeDescription(this.sideMenu, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{DrawerAddCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_menuItemIcon"));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DrawerAddCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chats_menuItemText"));
        arrayList.add(new ThemeDescription(this.sideMenu, 0, new Class[]{DividerCell.class}, Theme.dividerPaint, null, null, "divider"));
        if (this.searchViewPager != null) {
            DialogsSearchAdapter dialogsSearchAdapter = this.searchViewPager.dialogsSearchAdapter;
            arrayList.add(new ThemeDescription(dialogsSearchAdapter != null ? dialogsSearchAdapter.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, Theme.dialogs_countPaint, null, null, "chats_unreadCounter"));
            DialogsSearchAdapter dialogsSearchAdapter2 = this.searchViewPager.dialogsSearchAdapter;
            arrayList.add(new ThemeDescription(dialogsSearchAdapter2 != null ? dialogsSearchAdapter2.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, Theme.dialogs_countGrayPaint, null, null, "chats_unreadCounterMuted"));
            DialogsSearchAdapter dialogsSearchAdapter3 = this.searchViewPager.dialogsSearchAdapter;
            arrayList.add(new ThemeDescription(dialogsSearchAdapter3 != null ? dialogsSearchAdapter3.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, Theme.dialogs_countTextPaint, null, null, "chats_unreadCounterText"));
            DialogsSearchAdapter dialogsSearchAdapter4 = this.searchViewPager.dialogsSearchAdapter;
            arrayList.add(new ThemeDescription(dialogsSearchAdapter4 != null ? dialogsSearchAdapter4.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, Theme.dialogs_archiveTextPaint, null, null, "chats_archiveText"));
            DialogsSearchAdapter dialogsSearchAdapter5 = this.searchViewPager.dialogsSearchAdapter;
            arrayList.add(new ThemeDescription(dialogsSearchAdapter5 != null ? dialogsSearchAdapter5.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
            DialogsSearchAdapter dialogsSearchAdapter6 = this.searchViewPager.dialogsSearchAdapter;
            arrayList.add(new ThemeDescription(dialogsSearchAdapter6 != null ? dialogsSearchAdapter6.getInnerListView() : null, 0, new Class[]{HintDialogCell.class}, null, null, null, "chats_onlineCircle"));
        }
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "inappPlayerBackground"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"playButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "inappPlayerPlayPause"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "inappPlayerTitle"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_FASTSCROLL, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "inappPlayerPerformer"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"closeButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "inappPlayerClose"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "returnToCallBackground"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "returnToCallText"));
        for (int i6 = 0; i6 < this.undoView.length; i6++) {
            arrayList.add(new ThemeDescription(this.undoView[i6], ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "undo_background"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"undoImageView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_cancelColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"undoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_cancelColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"infoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"subinfoTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"textPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"progressPaint"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "info1", "undo_background"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "info2", "undo_background"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc12", "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc11", "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc10", "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc9", "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc8", "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc7", "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc6", "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc5", "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc4", "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc3", "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc2", "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "luc1", "undo_infoColor"));
            arrayList.add(new ThemeDescription(this.undoView[i6], 0, new Class[]{UndoView.class}, new String[]{"leftImageView"}, "Oval", "undo_infoColor"));
        }
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogBackgroundGray"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextBlack"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextLink"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogLinkSelection"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextBlue2"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextBlue3"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextBlue4"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextRed"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextRed2"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextGray"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextGray2"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextGray3"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextGray4"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogIcon"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogRedIcon"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogTextHint"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogInputField"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogInputFieldActivated"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogCheckboxSquareBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogCheckboxSquareCheck"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogCheckboxSquareUnchecked"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogCheckboxSquareDisabled"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogRadioBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogRadioBackgroundChecked"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogProgressCircle"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogButton"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogButtonSelector"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogScrollGlow"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogRoundCheckBox"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogRoundCheckBoxCheck"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogBadgeBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogBadgeText"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogLineProgress"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogLineProgressBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogGrayLine"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialog_inlineProgressBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialog_inlineProgress"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogSearchBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogSearchHint"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogSearchIcon"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogSearchText"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogFloatingButton"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogFloatingIcon"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "dialogShadowLine"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "key_sheet_scrollUp"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "key_sheet_other"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "player_actionBar"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "player_actionBarSelector"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "player_actionBarTitle"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "player_actionBarTop"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "player_actionBarSubtitle"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "player_actionBarItems"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "player_background"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "player_time"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "player_progressBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "key_player_progressCachedBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "player_progress"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "player_button"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "player_buttonActive"));
        if (this.commentView != null) {
            arrayList.add(new ThemeDescription(this.commentView, 0, null, Theme.chat_composeBackgroundPaint, null, null, "chat_messagePanelBackground"));
            arrayList.add(new ThemeDescription(this.commentView, 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, "chat_messagePanelShadow"));
            arrayList.add(new ThemeDescription(this.commentView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"messageEditText"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chat_messagePanelText"));
            arrayList.add(new ThemeDescription(this.commentView, ThemeDescription.FLAG_CURSORCOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"messageEditText"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chat_messagePanelCursor"));
            arrayList.add(new ThemeDescription(this.commentView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"messageEditText"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "chat_messagePanelHint"));
        }
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "actionBarTipBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "player_time"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "chat_messagePanelCursor"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "avatar_actionBarIconBlue"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, "groupcreate_spanBackground"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_overlayGreen1"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_overlayGreen2"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_overlayBlue1"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_overlayBlue2"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_topPanelGreen1"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_topPanelGreen2"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_topPanelBlue1"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_topPanelBlue2"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_topPanelGray"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_overlayAlertGradientMuted"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_overlayAlertGradientMuted2"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_overlayAlertGradientUnmuted"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_overlayAlertGradientUnmuted2"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_mutedByAdminGradient"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_mutedByAdminGradient2"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_mutedByAdminGradient3"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "voipgroup_overlayAlertMutedByAdmin"));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, null, "kvoipgroup_overlayAlertMutedByAdmin2"));
        FiltersView filtersView = this.filtersView;
        if (filtersView != null) {
            arrayList.addAll(filtersView.getThemeDescriptions());
            this.filtersView.updateColors();
        }
        SearchViewPager searchViewPager2 = this.searchViewPager;
        if (searchViewPager2 != null) {
            searchViewPager2.getThemeDescriptions(arrayList);
        }
        if (this.speedItem != null) {
            arrayList.addAll(SimpleThemeDescription.createThemeDescriptions(new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda84
                @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
                public final void didSetColor() {
                    DialogsActivity.this.lambda$getThemeDescriptions$87();
                }

                @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
                public /* synthetic */ void onAnimationProgress(float f) {
                    ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
                }
            }, "actionBarActionModeDefaultIcon", "actionBarActionModeDefaultSelector"));
        }
        final DialogsHintCell dialogsHintCell = this.dialogsHintCell;
        if (dialogsHintCell != null) {
            Objects.requireNonNull(dialogsHintCell);
            SimpleThemeDescription.add(arrayList, new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda42
                @Override // java.lang.Runnable
                public final void run() {
                    DialogsHintCell.this.updateColors();
                }
            }, "windowBackgroundWhite", "windowBackgroundWhiteBlackText", "windowBackgroundWhiteGrayText");
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:110:0x004d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0022  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$getThemeDescriptions$85() {
        DialogsSearchAdapter dialogsSearchAdapter;
        RecyclerListView innerListView;
        ViewGroup viewGroup;
        int i = 0;
        while (i < 3) {
            if (i == 2) {
                SearchViewPager searchViewPager = this.searchViewPager;
                if (searchViewPager == null) {
                    i++;
                } else {
                    viewGroup = searchViewPager.searchListView;
                    if (viewGroup == null) {
                        int childCount = viewGroup.getChildCount();
                        for (int i2 = 0; i2 < childCount; i2++) {
                            View childAt = viewGroup.getChildAt(i2);
                            if (childAt instanceof ProfileSearchCell) {
                                ((ProfileSearchCell) childAt).update(0);
                            } else if (childAt instanceof DialogCell) {
                                ((DialogCell) childAt).update(0);
                            } else if (childAt instanceof UserCell) {
                                ((UserCell) childAt).update(0);
                            }
                        }
                    }
                    i++;
                }
            } else {
                ViewPage[] viewPageArr = this.viewPages;
                if (viewPageArr == null) {
                    i++;
                } else {
                    viewGroup = i < viewPageArr.length ? viewPageArr[i].listView : null;
                    if (viewGroup == null) {
                    }
                    i++;
                }
            }
        }
        SearchViewPager searchViewPager2 = this.searchViewPager;
        if (searchViewPager2 != null && (dialogsSearchAdapter = searchViewPager2.dialogsSearchAdapter) != null && (innerListView = dialogsSearchAdapter.getInnerListView()) != null) {
            int childCount2 = innerListView.getChildCount();
            for (int i3 = 0; i3 < childCount2; i3++) {
                View childAt2 = innerListView.getChildAt(i3);
                if (childAt2 instanceof HintDialogCell) {
                    ((HintDialogCell) childAt2).update();
                }
            }
        }
        RecyclerView recyclerView = this.sideMenu;
        if (recyclerView != null) {
            View childAt3 = recyclerView.getChildAt(0);
            if (childAt3 instanceof DrawerProfileCell) {
                DrawerProfileCell drawerProfileCell = (DrawerProfileCell) childAt3;
                drawerProfileCell.applyBackground(true);
                drawerProfileCell.updateColors();
            }
        }
        if (this.viewPages != null) {
            int i4 = 0;
            while (true) {
                ViewPage[] viewPageArr2 = this.viewPages;
                if (i4 >= viewPageArr2.length) {
                    break;
                }
                if (viewPageArr2[i4].pullForegroundDrawable != null) {
                    this.viewPages[i4].pullForegroundDrawable.updateColors();
                }
                i4++;
            }
        }
        ActionBar actionBar = this.actionBar;
        if (actionBar != null) {
            actionBar.setPopupBackgroundColor(Theme.getColor("actionBarDefaultSubmenuBackground"), true);
            this.actionBar.setPopupItemsColor(Theme.getColor("actionBarDefaultSubmenuItem"), false, true);
            this.actionBar.setPopupItemsColor(Theme.getColor("actionBarDefaultSubmenuItemIcon"), true, true);
            this.actionBar.setPopupItemsSelectorColor(Theme.getColor("dialogButtonSelector"), true);
        }
        if (this.statusDrawable != null) {
            updateStatus(UserConfig.getInstance(this.currentAccount).getCurrentUser(), false);
        }
        if (this.scrimPopupWindowItems != null) {
            int i5 = 0;
            while (true) {
                ActionBarMenuSubItem[] actionBarMenuSubItemArr = this.scrimPopupWindowItems;
                if (i5 >= actionBarMenuSubItemArr.length) {
                    break;
                }
                if (actionBarMenuSubItemArr[i5] != null) {
                    actionBarMenuSubItemArr[i5].setColors(Theme.getColor("actionBarDefaultSubmenuItem"), Theme.getColor("actionBarDefaultSubmenuItemIcon"));
                    this.scrimPopupWindowItems[i5].setSelectorColor(Theme.getColor("dialogButtonSelector"));
                }
                i5++;
            }
        }
        ActionBarPopupWindow actionBarPopupWindow = this.scrimPopupWindow;
        if (actionBarPopupWindow != null) {
            View contentView = actionBarPopupWindow.getContentView();
            contentView.setBackgroundColor(Theme.getColor("actionBarDefaultSubmenuBackground"));
            contentView.invalidate();
        }
        ActionBarMenuItem actionBarMenuItem = this.doneItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.setIconColor(Theme.getColor("actionBarDefaultIcon"));
        }
        ChatActivityEnterView chatActivityEnterView = this.commentView;
        if (chatActivityEnterView != null) {
            chatActivityEnterView.updateColors();
        }
        FiltersView filtersView = this.filtersView;
        if (filtersView != null) {
            filtersView.updateColors();
        }
        SearchViewPager searchViewPager3 = this.searchViewPager;
        if (searchViewPager3 != null) {
            searchViewPager3.updateColors();
        }
        ViewPagerFixed.TabsView tabsView = this.searchTabsView;
        if (tabsView != null) {
            tabsView.updateColors();
        }
        View view = this.blurredView;
        if (view != null && Build.VERSION.SDK_INT >= 23) {
            view.setForeground(new ColorDrawable(ColorUtils.setAlphaComponent(getThemedColor("windowBackgroundWhite"), 100)));
        }
        ActionBarMenuItem actionBarMenuItem2 = this.searchItem;
        if (actionBarMenuItem2 != null) {
            EditTextBoldCursor searchField = actionBarMenuItem2.getSearchField();
            if (this.whiteActionBar) {
                searchField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                searchField.setHintTextColor(Theme.getColor("player_time"));
                searchField.setCursorColor(Theme.getColor("chat_messagePanelCursor"));
            } else {
                searchField.setCursorColor(Theme.getColor("actionBarDefaultSearch"));
                searchField.setHintTextColor(Theme.getColor("actionBarDefaultSearchPlaceholder"));
                searchField.setTextColor(Theme.getColor("actionBarDefaultSearch"));
            }
            this.searchItem.updateColor();
        }
        updateFloatingButtonColor();
        setSearchAnimationProgress(this.searchAnimationProgress, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$86() {
        SearchViewPager searchViewPager = this.searchViewPager;
        if (searchViewPager != null) {
            ActionBarMenu actionMode = searchViewPager.getActionMode();
            if (actionMode != null) {
                actionMode.setBackgroundColor(getThemedColor("actionBarActionModeDefault"));
            }
            ActionBarMenuItem speedItem = this.searchViewPager.getSpeedItem();
            if (speedItem != null) {
                speedItem.getIconView().setColorFilter(new PorterDuffColorFilter(getThemedColor("actionBarActionModeDefaultIcon"), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptions$87() {
        this.speedItem.getIconView().setColorFilter(new PorterDuffColorFilter(Theme.getColor("actionBarActionModeDefaultIcon"), PorterDuff.Mode.SRC_IN));
        this.speedItem.setBackground(Theme.createSelectorDrawable(Theme.getColor("actionBarActionModeDefaultSelector")));
    }

    private void updateFloatingButtonColor() {
        if (getParentActivity() == null || this.floatingButtonContainer == null) {
            return;
        }
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
        if (Build.VERSION.SDK_INT < 21) {
            Drawable mutate = ContextCompat.getDrawable(getParentActivity(), R.drawable.floating_shadow).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            createSimpleSelectorCircleDrawable = combinedDrawable;
        }
        this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Animator getCustomSlideTransition(boolean z, boolean z2, float f) {
        if (z2) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.slideFragmentProgress, 1.0f);
            this.slideBackTransitionAnimator = ofFloat;
            return ofFloat;
        }
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.slideFragmentProgress, 1.0f);
        this.slideBackTransitionAnimator = ofFloat2;
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DialogsActivity.this.lambda$getCustomSlideTransition$88(valueAnimator);
            }
        });
        this.slideBackTransitionAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        this.slideBackTransitionAnimator.setDuration((int) (Math.max((int) ((200.0f / getLayoutContainer().getMeasuredWidth()) * f), 80) * 1.2f));
        this.slideBackTransitionAnimator.start();
        return this.slideBackTransitionAnimator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getCustomSlideTransition$88(ValueAnimator valueAnimator) {
        setSlideTransitionProgress(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void prepareFragmentToSlide(boolean z, boolean z2) {
        if (!z && z2) {
            this.isSlideBackTransition = true;
            setFragmentIsSliding(true);
            return;
        }
        this.slideBackTransitionAnimator = null;
        this.isSlideBackTransition = false;
        setFragmentIsSliding(false);
        setSlideTransitionProgress(1.0f);
    }

    private void setFragmentIsSliding(boolean z) {
        if (SharedConfig.getDevicePerformanceClass() <= 1 || SharedConfig.getLiteMode().enabled()) {
            return;
        }
        if (z) {
            ViewPage[] viewPageArr = this.viewPages;
            if (viewPageArr != null && viewPageArr[0] != null) {
                viewPageArr[0].setLayerType(2, null);
                this.viewPages[0].setClipChildren(false);
                this.viewPages[0].setClipToPadding(false);
                this.viewPages[0].listView.setClipChildren(false);
            }
            ActionBar actionBar = this.actionBar;
            if (actionBar != null) {
                actionBar.setLayerType(2, null);
            }
            FilterTabsView filterTabsView = this.filterTabsView;
            if (filterTabsView != null) {
                filterTabsView.getListView().setLayerType(2, null);
            }
            View view = this.fragmentView;
            if (view != null) {
                ((ViewGroup) view).setClipChildren(false);
                this.fragmentView.requestLayout();
                return;
            }
            return;
        }
        int i = 0;
        while (true) {
            ViewPage[] viewPageArr2 = this.viewPages;
            if (i >= viewPageArr2.length) {
                break;
            }
            ViewPage viewPage = viewPageArr2[i];
            if (viewPage != null) {
                viewPage.setLayerType(0, null);
                viewPage.setClipChildren(true);
                viewPage.setClipToPadding(true);
                viewPage.listView.setClipChildren(true);
            }
            i++;
        }
        ActionBar actionBar2 = this.actionBar;
        if (actionBar2 != null) {
            actionBar2.setLayerType(0, null);
        }
        FilterTabsView filterTabsView2 = this.filterTabsView;
        if (filterTabsView2 != null) {
            filterTabsView2.getListView().setLayerType(0, null);
        }
        View view2 = this.fragmentView;
        if (view2 != null) {
            ((ViewGroup) view2).setClipChildren(true);
            this.fragmentView.requestLayout();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onSlideProgress(boolean z, float f) {
        if (SharedConfig.getDevicePerformanceClass() <= 0 || SharedConfig.getLiteMode().enabled() || !this.isSlideBackTransition || this.slideBackTransitionAnimator != null) {
            return;
        }
        setSlideTransitionProgress(f);
    }

    private void setSlideTransitionProgress(float f) {
        if (SharedConfig.getDevicePerformanceClass() <= 0 || SharedConfig.getLiteMode().enabled()) {
            return;
        }
        this.slideFragmentLite = SharedConfig.getDevicePerformanceClass() <= 1;
        this.slideFragmentProgress = f;
        View view = this.fragmentView;
        if (view != null) {
            view.invalidate();
        }
        if (this.slideFragmentLite) {
            FilterTabsView filterTabsView = this.filterTabsView;
            if (filterTabsView != null) {
                filterTabsView.getListView().setTranslationX((this.isDrawerTransition ? 1 : -1) * AndroidUtilities.dp(40.0f) * (1.0f - this.slideFragmentProgress));
                this.filterTabsView.invalidate();
            }
            RightSlidingDialogContainer rightSlidingDialogContainer = this.rightSlidingDialogContainer;
            if (rightSlidingDialogContainer == null || rightSlidingDialogContainer.getFragmentView() == null || this.rightFragmentTransitionInProgress) {
                return;
            }
            this.rightSlidingDialogContainer.getFragmentView().setTranslationX((this.isDrawerTransition ? 1 : -1) * AndroidUtilities.dp(40.0f) * (1.0f - this.slideFragmentProgress));
            return;
        }
        float f2 = 1.0f - ((1.0f - this.slideFragmentProgress) * 0.05f);
        FilterTabsView filterTabsView2 = this.filterTabsView;
        if (filterTabsView2 != null) {
            filterTabsView2.getListView().setScaleX(f2);
            this.filterTabsView.getListView().setScaleY(f2);
            this.filterTabsView.getListView().setTranslationX((this.isDrawerTransition ? AndroidUtilities.dp(4.0f) : -AndroidUtilities.dp(4.0f)) * (1.0f - this.slideFragmentProgress));
            this.filterTabsView.getListView().setPivotX(this.isDrawerTransition ? this.filterTabsView.getMeasuredWidth() : 0.0f);
            this.filterTabsView.getListView().setPivotY(0.0f);
            this.filterTabsView.invalidate();
        }
        RightSlidingDialogContainer rightSlidingDialogContainer2 = this.rightSlidingDialogContainer;
        if (rightSlidingDialogContainer2 == null || rightSlidingDialogContainer2.getFragmentView() == null) {
            return;
        }
        if (!this.rightFragmentTransitionInProgress) {
            this.rightSlidingDialogContainer.getFragmentView().setScaleX(f2);
            this.rightSlidingDialogContainer.getFragmentView().setScaleY(f2);
            this.rightSlidingDialogContainer.getFragmentView().setTranslationX((this.isDrawerTransition ? AndroidUtilities.dp(4.0f) : -AndroidUtilities.dp(4.0f)) * (1.0f - this.slideFragmentProgress));
        }
        this.rightSlidingDialogContainer.getFragmentView().setPivotX(this.isDrawerTransition ? this.rightSlidingDialogContainer.getMeasuredWidth() : 0.0f);
        this.rightSlidingDialogContainer.getFragmentView().setPivotY(0.0f);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public INavigationLayout.BackButtonState getBackButtonState() {
        return (isArchive() || this.rightSlidingDialogContainer.isOpenned) ? INavigationLayout.BackButtonState.BACK : INavigationLayout.BackButtonState.MENU;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void setProgressToDrawerOpened(float f) {
        if (SharedConfig.getDevicePerformanceClass() <= 0 || this.isSlideBackTransition) {
            return;
        }
        boolean z = f > 0.0f;
        if (this.searchIsShowed) {
            f = 0.0f;
            z = false;
        }
        if (z != this.isDrawerTransition) {
            this.isDrawerTransition = z;
            if (z) {
                setFragmentIsSliding(true);
            } else {
                setFragmentIsSliding(false);
            }
            View view = this.fragmentView;
            if (view != null) {
                view.requestLayout();
            }
        }
        setSlideTransitionProgress(1.0f - f);
    }

    public void setShowSearch(String str, int i) {
        if (!this.searching) {
            this.initialSearchType = i;
            this.actionBar.openSearchField(str, false);
            return;
        }
        if (!this.searchItem.getSearchField().getText().toString().equals(str)) {
            this.searchItem.getSearchField().setText(str);
        }
        int positionForType = this.searchViewPager.getPositionForType(i);
        if (positionForType < 0 || this.searchViewPager.getTabsView().getCurrentTabId() == positionForType) {
            return;
        }
        this.searchViewPager.getTabsView().scrollToTab(positionForType, positionForType);
    }

    public ActionBarMenuItem getSearchItem() {
        return this.searchItem;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        RightSlidingDialogContainer rightSlidingDialogContainer;
        if (!this.searching && (rightSlidingDialogContainer = this.rightSlidingDialogContainer) != null && rightSlidingDialogContainer.getFragment() != null) {
            return this.rightSlidingDialogContainer.getFragment().isLightStatusBar();
        }
        int color = Theme.getColor((this.searching && this.whiteActionBar) ? "windowBackgroundWhite" : this.folderId == 0 ? "actionBarDefault" : "actionBarDefaultArchived");
        if (this.actionBar.isActionModeShowed()) {
            color = Theme.getColor("actionBarActionModeDefault");
        }
        return ColorUtils.calculateLuminance(color) > 0.699999988079071d;
    }

    @Override // org.telegram.ui.Components.FloatingDebug.FloatingDebugProvider
    public List<FloatingDebugController.DebugItem> onGetDebugItems() {
        return Arrays.asList(new FloatingDebugController.DebugItem(LocaleController.getString(R.string.DebugDialogsActivity)), new FloatingDebugController.DebugItem(LocaleController.getString(R.string.ClearLocalDatabase), new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$onGetDebugItems$89();
            }
        }), new FloatingDebugController.DebugItem(LocaleController.getString(R.string.DebugClearSendMessageAsPeers), new Runnable() { // from class: org.telegram.ui.DialogsActivity$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                DialogsActivity.this.lambda$onGetDebugItems$90();
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onGetDebugItems$89() {
        getMessagesStorage().clearLocalDatabase();
        Toast.makeText(getContext(), LocaleController.getString(R.string.DebugClearLocalDatabaseSuccess), 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onGetDebugItems$90() {
        getMessagesController().clearSendAsPeers();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean closeLastFragment() {
        if (this.rightSlidingDialogContainer.hasFragment()) {
            this.rightSlidingDialogContainer.lambda$presentFragment$1();
            this.searchViewPager.updateTabs();
            return true;
        }
        return super.closeLastFragment();
    }

    public boolean getAllowGlobalSearch() {
        return this.allowGlobalSearch;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean canBeginSlide() {
        return !this.rightSlidingDialogContainer.hasFragment();
    }
}
