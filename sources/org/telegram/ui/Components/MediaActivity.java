package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Property;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$ChatParticipant;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.BottomPagerTabs;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.FloatingDebug.FloatingDebugController;
import org.telegram.ui.Components.FloatingDebug.FloatingDebugProvider;
import org.telegram.ui.Components.Paint.ShapeDetector;
import org.telegram.ui.Components.SharedMediaLayout;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stories.StoryViewer;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.Stories.recorder.StoryRecorder;
/* loaded from: classes4.dex */
public class MediaActivity extends BaseFragment implements SharedMediaLayout.SharedMediaPreloaderDelegate, FloatingDebugProvider, NotificationCenter.NotificationCenterDelegate {
    private SparseArray<MessageObject> actionModeMessageObjects;
    private Runnable applyBulletin;
    ProfileActivity.AvatarImageView avatarImageView;
    private BackDrawable backDrawable;
    private ButtonWithCounterView button;
    private FrameLayout buttonContainer;
    private ActionBarMenuSubItem calendarItem;
    private TLRPC$ChatFull currentChatInfo;
    private TLRPC$UserFull currentUserInfo;
    private ActionBarMenuItem deleteItem;
    private long dialogId;
    private boolean filterPhotos;
    private boolean filterVideos;
    private final boolean[] firstSubtitleCheck;
    private AnimatorSet floatingAnimator;
    private ImageView floatingButton;
    private FrameLayout floatingButtonContainer;
    private float floatingButtonHideProgress;
    private float floatingButtonTranslation;
    private float floatingButtonTranslation1;
    private float floatingButtonTranslation2;
    private boolean floatingHidden;
    private int initialTab;
    private int lastTab;
    private SimpleTextView[] nameTextView;
    private ActionBarMenuItem optionsItem;
    private AnimatedTextView selectedTextView;
    SharedMediaLayout sharedMediaLayout;
    private SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader;
    private int shiftDp;
    private ActionBarMenuSubItem showPhotosItem;
    private ActionBarMenuSubItem showVideosItem;
    private final ValueAnimator[] subtitleAnimator;
    private final boolean[] subtitleShown;
    private final float[] subtitleT;
    private AnimatedTextView[] subtitleTextView;
    private StoriesTabsView tabsView;
    private FrameLayout[] titles;
    private FrameLayout titlesContainer;
    private int type;
    private ActionBarMenuSubItem zoomInItem;
    private ActionBarMenuSubItem zoomOutItem;

    public MediaActivity(Bundle bundle, SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader) {
        super(bundle);
        this.titles = new FrameLayout[2];
        this.nameTextView = new SimpleTextView[2];
        this.subtitleTextView = new AnimatedTextView[2];
        this.filterPhotos = true;
        this.filterVideos = true;
        this.shiftDp = -12;
        this.subtitleShown = new boolean[2];
        this.subtitleT = new float[2];
        this.firstSubtitleCheck = new boolean[]{true, true};
        this.subtitleAnimator = new ValueAnimator[2];
        this.sharedMediaPreloader = sharedMediaPreloader;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        this.type = getArguments().getInt("type", 0);
        this.dialogId = getArguments().getLong("dialog_id");
        int i = this.type;
        this.initialTab = getArguments().getInt("start_from", i == 2 ? 9 : i == 1 ? 8 : 0);
        getNotificationCenter().addObserver(this, NotificationCenter.userInfoDidLoad);
        getNotificationCenter().addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        getNotificationCenter().addObserver(this, NotificationCenter.storiesEnabledUpdate);
        if (DialogObject.isUserDialog(this.dialogId)) {
            TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.dialogId));
            if (UserObject.isUserSelf(user)) {
                getMessagesController().loadUserInfo(user, false, this.classGuid);
                this.currentUserInfo = getMessagesController().getUserFull(this.dialogId);
            }
        }
        if (this.sharedMediaPreloader == null) {
            SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader = new SharedMediaLayout.SharedMediaPreloader(this);
            this.sharedMediaPreloader = sharedMediaPreloader;
            sharedMediaPreloader.addDelegate(this);
        }
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        getNotificationCenter().removeObserver(this, NotificationCenter.userInfoDidLoad);
        getNotificationCenter().removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        getNotificationCenter().removeObserver(this, NotificationCenter.storiesEnabledUpdate);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.userInfoDidLoad) {
            if (((Long) objArr[0]).longValue() == this.dialogId) {
                TLRPC$UserFull tLRPC$UserFull = (TLRPC$UserFull) objArr[1];
                this.currentUserInfo = tLRPC$UserFull;
                SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
                if (sharedMediaLayout != null) {
                    sharedMediaLayout.setUserInfo(tLRPC$UserFull);
                }
            }
        } else if ((i == NotificationCenter.currentUserPremiumStatusChanged || i == NotificationCenter.storiesEnabledUpdate) && !getMessagesController().storiesEnabled()) {
            hideFloatingButton(true, true);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0692  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x06a9  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x06be  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x06e6  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0538  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0540  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0545  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0581  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x058b  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0590  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x059e  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x05ac  */
    /* JADX WARN: Type inference failed for: r6v18 */
    /* JADX WARN: Type inference failed for: r6v19, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r6v20 */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        SizeNotifierFrameLayout sizeNotifierFrameLayout;
        int i;
        ?? r6;
        FrameLayout frameLayout;
        StoriesTabsView storiesTabsView;
        int i2;
        FrameLayout frameLayout2;
        int i3;
        AvatarDrawable avatarDrawable;
        TLRPC$Chat tLRPC$Chat;
        TLRPC$User user;
        boolean z;
        ActionBar actionBar = this.actionBar;
        BackDrawable backDrawable = new BackDrawable(false);
        this.backDrawable = backDrawable;
        actionBar.setBackButtonDrawable(backDrawable);
        this.backDrawable.setAnimationTime(240.0f);
        this.actionBar.setCastShadows(false);
        this.actionBar.setAddToContainer(false);
        this.actionBar.setActionBarMenuOnItemClick(new 1());
        this.actionBar.setColorFilterMode(PorterDuff.Mode.SRC_IN);
        final FrameLayout frameLayout3 = new FrameLayout(context);
        final SizeNotifierFrameLayout sizeNotifierFrameLayout2 = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.Components.MediaActivity.2
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i4, int i5) {
                ((FrameLayout.LayoutParams) MediaActivity.this.sharedMediaLayout.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight() + (((BaseFragment) MediaActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) frameLayout3.getLayoutParams();
                layoutParams.topMargin = ((BaseFragment) MediaActivity.this).actionBar.getOccupyStatusBar() ? AndroidUtilities.statusBarHeight : 0;
                layoutParams.height = ActionBar.getCurrentActionBarHeight();
                for (int i6 = 0; i6 < 2; i6++) {
                    if (MediaActivity.this.nameTextView[i6] != null) {
                        ((FrameLayout.LayoutParams) MediaActivity.this.nameTextView[i6].getLayoutParams()).topMargin = (((ActionBar.getCurrentActionBarHeight() / 2) - AndroidUtilities.dp(22.0f)) / 2) + AndroidUtilities.dp((AndroidUtilities.isTablet() || getResources().getConfiguration().orientation != 2) ? 5.0f : 4.0f);
                    }
                    if (MediaActivity.this.subtitleTextView[i6] != null) {
                        ((FrameLayout.LayoutParams) MediaActivity.this.subtitleTextView[i6].getLayoutParams()).topMargin = ((ActionBar.getCurrentActionBarHeight() / 2) + (((ActionBar.getCurrentActionBarHeight() / 2) - AndroidUtilities.dp(19.0f)) / 2)) - AndroidUtilities.dp(7.0f);
                    }
                }
                ((FrameLayout.LayoutParams) MediaActivity.this.avatarImageView.getLayoutParams()).topMargin = (ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(42.0f)) / 2;
                super.onMeasure(i4, i5);
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                SharedMediaLayout sharedMediaLayout = MediaActivity.this.sharedMediaLayout;
                if (sharedMediaLayout != null && sharedMediaLayout.isInFastScroll()) {
                    return MediaActivity.this.sharedMediaLayout.dispatchFastScrollEvent(motionEvent);
                }
                SharedMediaLayout sharedMediaLayout2 = MediaActivity.this.sharedMediaLayout;
                if (sharedMediaLayout2 == null || !sharedMediaLayout2.checkPinchToZoom(motionEvent)) {
                    return super.dispatchTouchEvent(motionEvent);
                }
                return true;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout
            public void drawList(Canvas canvas, boolean z2) {
                MediaActivity.this.sharedMediaLayout.drawListForBlur(canvas);
            }
        };
        sizeNotifierFrameLayout2.needBlur = true;
        this.fragmentView = sizeNotifierFrameLayout2;
        final ActionBarMenu createMenu = this.actionBar.createMenu();
        int i4 = this.type;
        if (i4 == 1 || i4 == 2) {
            FrameLayout frameLayout4 = new FrameLayout(context);
            this.actionBar.addView(frameLayout4, LayoutHelper.createFrame(56, 56, 85));
            int i5 = Theme.key_actionBarActionModeDefaultSelector;
            int themedColor = getThemedColor(i5);
            int i6 = Theme.key_windowBackgroundWhiteBlackText;
            ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, createMenu, themedColor, getThemedColor(i6));
            this.deleteItem = actionBarMenuItem;
            actionBarMenuItem.setIcon(R.drawable.msg_delete);
            this.deleteItem.setVisibility(8);
            this.deleteItem.setAlpha(0.0f);
            this.deleteItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ActionBarMenu.this.onItemClick(2);
                }
            });
            frameLayout4.addView(this.deleteItem);
            ActionBarMenuItem actionBarMenuItem2 = new ActionBarMenuItem(context, createMenu, getThemedColor(i5), getThemedColor(i6));
            this.optionsItem = actionBarMenuItem2;
            actionBarMenuItem2.setIcon(R.drawable.ic_ab_other);
            this.optionsItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MediaActivity.this.lambda$createView$1(view);
                }
            });
            this.optionsItem.setVisibility(8);
            this.optionsItem.setAlpha(0.0f);
            frameLayout4.addView(this.optionsItem);
            ActionBarMenuSubItem addSubItem = this.optionsItem.addSubItem(8, R.drawable.msg_zoomin, LocaleController.getString("MediaZoomIn", R.string.MediaZoomIn));
            this.zoomInItem = addSubItem;
            addSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MediaActivity.this.lambda$createView$2(view);
                }
            });
            ActionBarMenuSubItem addSubItem2 = this.optionsItem.addSubItem(9, R.drawable.msg_zoomout, LocaleController.getString("MediaZoomOut", R.string.MediaZoomOut));
            this.zoomOutItem = addSubItem2;
            addSubItem2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MediaActivity.this.lambda$createView$3(view);
                }
            });
            ActionBarMenuSubItem addSubItem3 = this.optionsItem.addSubItem(10, R.drawable.msg_calendar2, LocaleController.getString("Calendar", R.string.Calendar));
            this.calendarItem = addSubItem3;
            addSubItem3.setEnabled(false);
            this.calendarItem.setAlpha(0.5f);
            this.optionsItem.addColoredGap();
            ActionBarMenuSubItem addSubItem4 = this.optionsItem.addSubItem(6, 0, (CharSequence) LocaleController.getString("MediaShowPhotos", R.string.MediaShowPhotos), true);
            this.showPhotosItem = addSubItem4;
            addSubItem4.setChecked(this.filterPhotos);
            this.showPhotosItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MediaActivity.this.lambda$createView$4(view);
                }
            });
            ActionBarMenuSubItem addSubItem5 = this.optionsItem.addSubItem(7, 0, (CharSequence) LocaleController.getString("MediaShowVideos", R.string.MediaShowVideos), true);
            this.showVideosItem = addSubItem5;
            addSubItem5.setChecked(this.filterVideos);
            this.showVideosItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda9
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MediaActivity.this.lambda$createView$5(view);
                }
            });
        }
        boolean z2 = this.type == 0;
        FrameLayout frameLayout5 = new FrameLayout(context);
        this.titlesContainer = frameLayout5;
        frameLayout3.addView(frameLayout5, LayoutHelper.createFrame(-1, -1, 119));
        int i7 = 0;
        while (true) {
            if (i7 >= (this.type == 1 ? 2 : 1)) {
                break;
            }
            this.titles[i7] = new FrameLayout(context);
            this.titlesContainer.addView(this.titles[i7], LayoutHelper.createFrame(-1, -1, 119));
            this.nameTextView[i7] = new SimpleTextView(context);
            this.nameTextView[i7].setPivotX(0.0f);
            this.nameTextView[i7].setPivotY(AndroidUtilities.dp(9.0f));
            this.nameTextView[i7].setTextSize(18);
            this.nameTextView[i7].setGravity(3);
            this.nameTextView[i7].setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.nameTextView[i7].setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3f));
            this.nameTextView[i7].setScrollNonFitText(true);
            this.nameTextView[i7].setImportantForAccessibility(2);
            this.titles[i7].addView(this.nameTextView[i7], LayoutHelper.createFrame(-2, -2.0f, 51, z2 ? 118.0f : 72.0f, 0.0f, 56.0f, 0.0f));
            this.subtitleTextView[i7] = new AnimatedTextView(context, true, true, true);
            this.subtitleTextView[i7].setAnimationProperties(0.4f, 0L, 320L, CubicBezierInterpolator.EASE_OUT_QUINT);
            this.subtitleTextView[i7].setTextSize(AndroidUtilities.dp(14.0f));
            this.subtitleTextView[i7].setTextColor(Theme.getColor(Theme.key_player_actionBarSubtitle));
            this.titles[i7].addView(this.subtitleTextView[i7], LayoutHelper.createFrame(-2, -2.0f, 51, z2 ? 118.0f : 72.0f, 0.0f, 56.0f, 0.0f));
            if (i7 != 0) {
                this.titles[i7].setAlpha(0.0f);
            }
            i7++;
        }
        ProfileActivity.AvatarImageView avatarImageView = new ProfileActivity.AvatarImageView(this, context) { // from class: org.telegram.ui.Components.MediaActivity.3
            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                if (getImageReceiver().hasNotThumb()) {
                    accessibilityNodeInfo.setText(LocaleController.getString("AccDescrProfilePicture", R.string.AccDescrProfilePicture));
                    if (Build.VERSION.SDK_INT >= 21) {
                        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, LocaleController.getString("Open", R.string.Open)));
                        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(32, LocaleController.getString("AccDescrOpenInPhotoViewer", R.string.AccDescrOpenInPhotoViewer)));
                        return;
                    }
                    return;
                }
                accessibilityNodeInfo.setVisibleToUser(false);
            }
        };
        this.avatarImageView = avatarImageView;
        avatarImageView.getImageReceiver().setAllowDecodeSingleFrame(true);
        this.avatarImageView.setRoundRadius(AndroidUtilities.dp(21.0f));
        this.avatarImageView.setPivotX(0.0f);
        this.avatarImageView.setPivotY(0.0f);
        AvatarDrawable avatarDrawable2 = new AvatarDrawable();
        avatarDrawable2.setProfile(true);
        this.avatarImageView.setVisibility(z2 ? 0 : 8);
        this.avatarImageView.setImageDrawable(avatarDrawable2);
        frameLayout3.addView(this.avatarImageView, LayoutHelper.createFrame(42, 42.0f, 51, 64.0f, 0.0f, 0.0f, 0.0f));
        AnimatedTextView animatedTextView = new AnimatedTextView(context, true, true, true);
        this.selectedTextView = animatedTextView;
        animatedTextView.setAnimationProperties(0.4f, 0L, 320L, CubicBezierInterpolator.EASE_OUT_QUINT);
        this.selectedTextView.setTextSize(AndroidUtilities.dp(20.0f));
        this.selectedTextView.setGravity(3);
        this.selectedTextView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        this.selectedTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        frameLayout3.addView(this.selectedTextView, LayoutHelper.createFrame(-2, -1.0f, 23, (z2 ? 48 : 0) + 72, -2.0f, 72.0f, 0.0f));
        if (this.type == 1) {
            StoriesTabsView storiesTabsView2 = new StoriesTabsView(this, context, getResourceProvider());
            this.tabsView = storiesTabsView2;
            storiesTabsView2.setOnTabClick(new Utilities.Callback() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda15
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    MediaActivity.this.lambda$createView$6((Integer) obj);
                }
            });
            FrameLayout frameLayout6 = new FrameLayout(context);
            this.buttonContainer = frameLayout6;
            frameLayout6.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(8.0f));
            this.buttonContainer.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
            ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, getResourceProvider());
            this.button = buttonWithCounterView;
            buttonWithCounterView.setText(LocaleController.getString("SaveToProfile", R.string.SaveToProfile), false);
            this.button.setShowZero(true);
            this.button.setCount(0, false);
            this.button.setEnabled(false);
            this.button.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MediaActivity.this.lambda$createView$10(view);
                }
            });
            this.buttonContainer.addView(this.button);
            this.buttonContainer.setAlpha(0.0f);
            this.buttonContainer.setTranslationY(AndroidUtilities.dp(100.0f));
            Bulletin.addDelegate(this, new Bulletin.Delegate(this) { // from class: org.telegram.ui.Components.MediaActivity.4
                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean allowLayoutChanges() {
                    return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean clipWithGradient(int i8) {
                    return Bulletin.Delegate.-CC.$default$clipWithGradient(this, i8);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ int getTopOffset(int i8) {
                    return Bulletin.Delegate.-CC.$default$getTopOffset(this, i8);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ void onBottomOffsetChange(float f) {
                    Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ void onHide(Bulletin bulletin) {
                    Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ void onShow(Bulletin bulletin) {
                    Bulletin.Delegate.-CC.$default$onShow(this, bulletin);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public int getBottomOffset(int i8) {
                    return AndroidUtilities.dp(64.0f);
                }
            });
            FrameLayout frameLayout7 = new FrameLayout(context);
            this.floatingButtonContainer = frameLayout7;
            frameLayout7.setVisibility(0);
            this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MediaActivity.this.lambda$createView$11(view);
                }
            });
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.floatingButton = rLottieImageView;
            rLottieImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
            this.floatingButton.setImageResource(R.drawable.story_camera);
            this.floatingButtonContainer.setContentDescription(LocaleController.getString("AccDescrCaptureStory", R.string.AccDescrCaptureStory));
            int dp = AndroidUtilities.dp(56.0f);
            int i8 = Theme.key_chats_actionBackground;
            int color = Theme.getColor(i8);
            int i9 = Theme.key_chats_actionPressedBackground;
            Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(dp, color, Theme.getColor(i9));
            int i10 = Build.VERSION.SDK_INT;
            if (i10 < 21) {
                Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
                mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0).setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            }
            this.floatingButtonContainer.addView(this.floatingButton, LayoutHelper.createFrame(56, 56, 17));
            if (this.floatingButtonContainer != null) {
                Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(i8), Theme.getColor(i9));
                if (i10 < 21) {
                    Drawable mutate2 = ContextCompat.getDrawable(getParentActivity(), R.drawable.floating_shadow).mutate();
                    mutate2.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                    z = false;
                    CombinedDrawable combinedDrawable = new CombinedDrawable(mutate2, createSimpleSelectorCircleDrawable2, 0, 0);
                    combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    createSimpleSelectorCircleDrawable2 = combinedDrawable;
                } else {
                    z = false;
                }
                this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable2);
            } else {
                z = false;
            }
            hideFloatingButton(true, z);
        }
        SharedMediaLayout sharedMediaLayout = new SharedMediaLayout(context, this.dialogId, this.sharedMediaPreloader, 0, null, this.currentChatInfo, this.currentUserInfo, false, this, new SharedMediaLayout.Delegate() { // from class: org.telegram.ui.Components.MediaActivity.5
            @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
            public boolean canSearchMembers() {
                return false;
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
            public TLRPC$Chat getCurrentChat() {
                return null;
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
            public RecyclerListView getListView() {
                return null;
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
            public boolean isFragmentOpened() {
                return true;
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
            public boolean onMemberClick(TLRPC$ChatParticipant tLRPC$ChatParticipant, boolean z3, boolean z4, View view) {
                return false;
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
            public void scrollToSharedMedia() {
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout.Delegate
            public void updateSelectedMediaTabText() {
                MediaActivity.this.updateMediaCount();
            }
        }, 0, getResourceProvider()) { // from class: org.telegram.ui.Components.MediaActivity.6
            private AnimatorSet actionModeAnimation;

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected boolean canShowSearchItem() {
                return false;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.SharedMediaLayout
            public void onSelectedTabChanged() {
                super.onSelectedTabChanged();
                MediaActivity.this.updateMediaCount();
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected void onSearchStateChanged(boolean z3) {
                AndroidUtilities.removeAdjustResize(MediaActivity.this.getParentActivity(), ((BaseFragment) MediaActivity.this).classGuid);
                AndroidUtilities.updateViewVisibilityAnimated(frameLayout3, !z3, 0.95f, true);
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected void drawBackgroundWithBlur(Canvas canvas, float f, android.graphics.Rect rect, Paint paint) {
                sizeNotifierFrameLayout2.drawBlurRect(canvas, getY() + f, rect, paint, true);
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected void invalidateBlur() {
                sizeNotifierFrameLayout2.invalidateBlur();
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected boolean isStoriesView() {
                return MediaActivity.this.type == 1 || MediaActivity.this.type == 2;
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected boolean includeStories() {
                return MediaActivity.this.type == 1 || MediaActivity.this.type == 2;
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected boolean isArchivedOnlyStoriesView() {
                return MediaActivity.this.type == 2;
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected int getInitialTab() {
                return MediaActivity.this.initialTab;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.SharedMediaLayout
            public void showActionMode(final boolean z3) {
                if (MediaActivity.this.type == 0) {
                    super.showActionMode(z3);
                } else if (this.isActionModeShowed == z3) {
                } else {
                    this.isActionModeShowed = z3;
                    AnimatorSet animatorSet = this.actionModeAnimation;
                    if (animatorSet != null) {
                        animatorSet.cancel();
                    }
                    if (MediaActivity.this.type == 1 || MediaActivity.this.type == 2) {
                        disableScroll(z3);
                    }
                    if (z3) {
                        MediaActivity.this.selectedTextView.setVisibility(0);
                        if (MediaActivity.this.buttonContainer != null) {
                            MediaActivity.this.buttonContainer.setVisibility(0);
                        }
                    } else {
                        MediaActivity.this.titlesContainer.setVisibility(0);
                    }
                    float f = 0.0f;
                    MediaActivity.this.backDrawable.setRotation(z3 ? 1.0f : 0.0f, true);
                    this.actionModeAnimation = new AnimatorSet();
                    ArrayList arrayList = new ArrayList();
                    AnimatedTextView animatedTextView2 = MediaActivity.this.selectedTextView;
                    Property property = View.ALPHA;
                    float[] fArr = new float[1];
                    fArr[0] = z3 ? 1.0f : 0.0f;
                    arrayList.add(ObjectAnimator.ofFloat(animatedTextView2, property, fArr));
                    FrameLayout frameLayout8 = MediaActivity.this.titlesContainer;
                    Property property2 = View.ALPHA;
                    float[] fArr2 = new float[1];
                    fArr2[0] = z3 ? 0.0f : 1.0f;
                    arrayList.add(ObjectAnimator.ofFloat(frameLayout8, property2, fArr2));
                    if (MediaActivity.this.buttonContainer != null) {
                        FrameLayout frameLayout9 = MediaActivity.this.buttonContainer;
                        Property property3 = View.ALPHA;
                        float[] fArr3 = new float[1];
                        fArr3[0] = z3 ? 1.0f : 0.0f;
                        arrayList.add(ObjectAnimator.ofFloat(frameLayout9, property3, fArr3));
                        FrameLayout frameLayout10 = MediaActivity.this.buttonContainer;
                        Property property4 = View.TRANSLATION_Y;
                        float[] fArr4 = new float[1];
                        fArr4[0] = z3 ? 0.0f : MediaActivity.this.buttonContainer.getMeasuredHeight();
                        arrayList.add(ObjectAnimator.ofFloat(frameLayout10, property4, fArr4));
                    }
                    if (MediaActivity.this.deleteItem != null) {
                        MediaActivity.this.deleteItem.setVisibility(0);
                        ActionBarMenuItem actionBarMenuItem3 = MediaActivity.this.deleteItem;
                        Property property5 = View.ALPHA;
                        float[] fArr5 = new float[1];
                        fArr5[0] = z3 ? 1.0f : 0.0f;
                        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem3, property5, fArr5));
                    }
                    final boolean z4 = getStoriesCount(getClosestTab()) == 0;
                    if (MediaActivity.this.optionsItem != null) {
                        MediaActivity.this.optionsItem.setVisibility(0);
                        ActionBarMenuItem actionBarMenuItem4 = MediaActivity.this.optionsItem;
                        Property property6 = View.ALPHA;
                        float[] fArr6 = new float[1];
                        if (!z3 && !z4) {
                            f = 1.0f;
                        }
                        fArr6[0] = f;
                        arrayList.add(ObjectAnimator.ofFloat(actionBarMenuItem4, property6, fArr6));
                    }
                    if (MediaActivity.this.tabsView != null) {
                        StoriesTabsView storiesTabsView3 = MediaActivity.this.tabsView;
                        Property property7 = View.ALPHA;
                        float[] fArr7 = new float[1];
                        fArr7[0] = z3 ? 0.4f : 1.0f;
                        arrayList.add(ObjectAnimator.ofFloat(storiesTabsView3, property7, fArr7));
                    }
                    this.actionModeAnimation.playTogether(arrayList);
                    this.actionModeAnimation.setDuration(300L);
                    this.actionModeAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                    this.actionModeAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.MediaActivity.6.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator) {
                            6.this.actionModeAnimation = null;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (6.this.actionModeAnimation == null) {
                                return;
                            }
                            6.this.actionModeAnimation = null;
                            if (!z3) {
                                MediaActivity.this.selectedTextView.setVisibility(4);
                                if (MediaActivity.this.buttonContainer != null) {
                                    MediaActivity.this.buttonContainer.setVisibility(4);
                                }
                                if (MediaActivity.this.deleteItem != null) {
                                    MediaActivity.this.deleteItem.setVisibility(8);
                                }
                                if (!z4 || MediaActivity.this.optionsItem == null) {
                                    return;
                                }
                                MediaActivity.this.optionsItem.setVisibility(8);
                                return;
                            }
                            MediaActivity.this.titlesContainer.setVisibility(4);
                            if (MediaActivity.this.optionsItem != null) {
                                MediaActivity.this.optionsItem.setVisibility(8);
                            }
                        }
                    });
                    this.actionModeAnimation.start();
                }
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected void onActionModeSelectedUpdate(SparseArray<MessageObject> sparseArray) {
                int size = sparseArray.size();
                MediaActivity.this.actionModeMessageObjects = sparseArray;
                if (MediaActivity.this.type == 1 || MediaActivity.this.type == 2) {
                    MediaActivity.this.selectedTextView.cancelAnimation();
                    MediaActivity.this.selectedTextView.setText(LocaleController.formatPluralString("StoriesSelected", size, new Object[0]), !LocaleController.isRTL);
                    if (MediaActivity.this.button != null) {
                        MediaActivity.this.button.setEnabled(size > 0);
                        MediaActivity.this.button.setCount(size, true);
                        if (MediaActivity.this.sharedMediaLayout.getClosestTab() == 8) {
                            MediaActivity.this.button.setText(LocaleController.formatPluralString("ArchiveStories", size, new Object[0]), true);
                        }
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.SharedMediaLayout
            public void onTabProgress(float f) {
                if (MediaActivity.this.type != 1) {
                    return;
                }
                float f2 = f - 8.0f;
                if (MediaActivity.this.tabsView != null) {
                    MediaActivity.this.tabsView.setProgress(f2);
                }
                float f3 = 1.0f - f2;
                MediaActivity.this.titles[0].setAlpha(f3);
                MediaActivity.this.titles[0].setTranslationX(AndroidUtilities.dp(-12.0f) * f2);
                MediaActivity.this.titles[1].setAlpha(f2);
                MediaActivity.this.titles[1].setTranslationX(AndroidUtilities.dp(12.0f) * f3);
            }

            @Override // org.telegram.ui.Components.SharedMediaLayout
            protected void onTabScroll(boolean z3) {
                if (MediaActivity.this.tabsView != null) {
                    MediaActivity.this.tabsView.setScrolling(z3);
                }
            }
        };
        this.sharedMediaLayout = sharedMediaLayout;
        sharedMediaLayout.setPinnedToTop(true);
        this.sharedMediaLayout.getSearchItem().setTranslationY(0.0f);
        this.sharedMediaLayout.photoVideoOptionsItem.setTranslationY(0.0f);
        int i11 = this.type;
        if (i11 != 1) {
            i = 2;
            if (i11 != 2) {
                sizeNotifierFrameLayout = sizeNotifierFrameLayout2;
                sizeNotifierFrameLayout.addView(this.sharedMediaLayout);
                sizeNotifierFrameLayout.addView(this.actionBar);
                sizeNotifierFrameLayout.addView(frameLayout3);
                sizeNotifierFrameLayout.blurBehindViews.add(this.sharedMediaLayout);
                if (this.type != 1) {
                    r6 = 0;
                    showSubtitle(0, false, false);
                    showSubtitle(1, false, false);
                } else {
                    r6 = 0;
                }
                frameLayout = this.floatingButtonContainer;
                if (frameLayout != null) {
                    int i12 = Build.VERSION.SDK_INT;
                    int i13 = i12 >= 21 ? 56 : 60;
                    float f = i12 >= 21 ? 56 : 60;
                    boolean z3 = LocaleController.isRTL;
                    sizeNotifierFrameLayout.addView(frameLayout, LayoutHelper.createFrame(i13, f, (z3 ? 3 : 5) | 80, z3 ? 14.0f : 0.0f, 0.0f, z3 ? 0.0f : 14.0f, 78.0f));
                }
                storiesTabsView = this.tabsView;
                if (storiesTabsView == null) {
                    i2 = -1;
                    sizeNotifierFrameLayout.addView(storiesTabsView, LayoutHelper.createFrame(-1, -2, 87));
                } else {
                    i2 = -1;
                }
                frameLayout2 = this.buttonContainer;
                if (frameLayout2 != null) {
                    sizeNotifierFrameLayout.addView(frameLayout2, LayoutHelper.createFrame(i2, 64, 87));
                }
                TLObject tLObject = null;
                i3 = this.type;
                if (i3 != i) {
                    this.nameTextView[r6].setText(LocaleController.getString("ProfileStoriesArchive"));
                } else if (i3 == 1) {
                    this.nameTextView[r6].setText(LocaleController.getString("ProfileMyStories"));
                    this.nameTextView[1].setText(LocaleController.getString("ProfileStoriesArchive"));
                } else {
                    if (DialogObject.isEncryptedDialog(this.dialogId)) {
                        TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(this.dialogId)));
                        if (encryptedChat != null && (user = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id))) != null) {
                            this.nameTextView[r6].setText(ContactsController.formatName(user.first_name, user.last_name));
                            avatarDrawable = avatarDrawable2;
                            avatarDrawable.setInfo(user);
                            tLRPC$Chat = user;
                        }
                    } else {
                        avatarDrawable = avatarDrawable2;
                        if (DialogObject.isUserDialog(this.dialogId)) {
                            TLRPC$User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
                            if (user2 != null) {
                                if (user2.self) {
                                    this.nameTextView[r6].setText(LocaleController.getString("SavedMessages", R.string.SavedMessages));
                                    avatarDrawable.setAvatarType(1);
                                    avatarDrawable.setScaleSize(0.8f);
                                } else {
                                    this.nameTextView[r6].setText(ContactsController.formatName(user2.first_name, user2.last_name));
                                    avatarDrawable.setInfo(user2);
                                    tLRPC$Chat = user2;
                                }
                            }
                        } else {
                            TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
                            if (chat != null) {
                                this.nameTextView[r6].setText(chat.title);
                                avatarDrawable.setInfo(chat);
                                tLRPC$Chat = chat;
                            }
                        }
                        this.avatarImageView.setImage(ImageLocation.getForUserOrChat(tLObject, 1), "50_50", avatarDrawable, tLObject);
                        if (TextUtils.isEmpty(this.nameTextView[r6].getText())) {
                            this.nameTextView[r6].setText(LocaleController.getString("SharedContentTitle", R.string.SharedContentTitle));
                        }
                        if (this.sharedMediaLayout.isSearchItemVisible() && this.type != 1) {
                            this.sharedMediaLayout.getSearchItem().setVisibility(r6);
                        }
                        if (!this.sharedMediaLayout.isCalendarItemVisible() && this.type != 1) {
                            this.sharedMediaLayout.photoVideoOptionsItem.setVisibility(r6);
                        } else {
                            this.sharedMediaLayout.photoVideoOptionsItem.setVisibility(4);
                        }
                        this.actionBar.setDrawBlurBackground(sizeNotifierFrameLayout);
                        AndroidUtilities.updateViewVisibilityAnimated(frameLayout3, true, 1.0f, r6);
                        updateMediaCount();
                        updateColors();
                        if (this.type == 1 && this.initialTab == 9) {
                            this.sharedMediaLayout.onTabProgress(9.0f);
                        }
                        return sizeNotifierFrameLayout;
                    }
                    tLObject = tLRPC$Chat;
                    this.avatarImageView.setImage(ImageLocation.getForUserOrChat(tLObject, 1), "50_50", avatarDrawable, tLObject);
                    if (TextUtils.isEmpty(this.nameTextView[r6].getText())) {
                    }
                    if (this.sharedMediaLayout.isSearchItemVisible()) {
                        this.sharedMediaLayout.getSearchItem().setVisibility(r6);
                    }
                    if (!this.sharedMediaLayout.isCalendarItemVisible()) {
                    }
                    this.sharedMediaLayout.photoVideoOptionsItem.setVisibility(4);
                    this.actionBar.setDrawBlurBackground(sizeNotifierFrameLayout);
                    AndroidUtilities.updateViewVisibilityAnimated(frameLayout3, true, 1.0f, r6);
                    updateMediaCount();
                    updateColors();
                    if (this.type == 1) {
                        this.sharedMediaLayout.onTabProgress(9.0f);
                    }
                    return sizeNotifierFrameLayout;
                }
                avatarDrawable = avatarDrawable2;
                this.avatarImageView.setImage(ImageLocation.getForUserOrChat(tLObject, 1), "50_50", avatarDrawable, tLObject);
                if (TextUtils.isEmpty(this.nameTextView[r6].getText())) {
                }
                if (this.sharedMediaLayout.isSearchItemVisible()) {
                }
                if (!this.sharedMediaLayout.isCalendarItemVisible()) {
                }
                this.sharedMediaLayout.photoVideoOptionsItem.setVisibility(4);
                this.actionBar.setDrawBlurBackground(sizeNotifierFrameLayout);
                AndroidUtilities.updateViewVisibilityAnimated(frameLayout3, true, 1.0f, r6);
                updateMediaCount();
                updateColors();
                if (this.type == 1) {
                }
                return sizeNotifierFrameLayout;
            }
            sizeNotifierFrameLayout = sizeNotifierFrameLayout2;
        } else {
            sizeNotifierFrameLayout = sizeNotifierFrameLayout2;
            i = 2;
        }
        sizeNotifierFrameLayout.addView(this.sharedMediaLayout, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 64.0f));
        sizeNotifierFrameLayout.addView(this.actionBar);
        sizeNotifierFrameLayout.addView(frameLayout3);
        sizeNotifierFrameLayout.blurBehindViews.add(this.sharedMediaLayout);
        if (this.type != 1) {
        }
        frameLayout = this.floatingButtonContainer;
        if (frameLayout != null) {
        }
        storiesTabsView = this.tabsView;
        if (storiesTabsView == null) {
        }
        frameLayout2 = this.buttonContainer;
        if (frameLayout2 != null) {
        }
        TLObject tLObject2 = null;
        i3 = this.type;
        if (i3 != i) {
        }
        avatarDrawable = avatarDrawable2;
        this.avatarImageView.setImage(ImageLocation.getForUserOrChat(tLObject2, 1), "50_50", avatarDrawable, tLObject2);
        if (TextUtils.isEmpty(this.nameTextView[r6].getText())) {
        }
        if (this.sharedMediaLayout.isSearchItemVisible()) {
        }
        if (!this.sharedMediaLayout.isCalendarItemVisible()) {
        }
        this.sharedMediaLayout.photoVideoOptionsItem.setVisibility(4);
        this.actionBar.setDrawBlurBackground(sizeNotifierFrameLayout);
        AndroidUtilities.updateViewVisibilityAnimated(frameLayout3, true, 1.0f, r6);
        updateMediaCount();
        updateColors();
        if (this.type == 1) {
        }
        return sizeNotifierFrameLayout;
    }

    /* loaded from: classes4.dex */
    class 1 extends ActionBar.ActionBarMenuOnItemClick {
        1() {
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            int i2;
            String str;
            if (i == -1) {
                if (MediaActivity.this.sharedMediaLayout.closeActionMode(true)) {
                    return;
                }
                MediaActivity.this.finishFragment();
            } else if (i != 2) {
                if (i == 10) {
                    SharedMediaLayout sharedMediaLayout = MediaActivity.this.sharedMediaLayout;
                    sharedMediaLayout.showMediaCalendar(sharedMediaLayout.getClosestTab(), false);
                }
            } else if (MediaActivity.this.actionModeMessageObjects != null) {
                final ArrayList arrayList = new ArrayList();
                for (int i3 = 0; i3 < MediaActivity.this.actionModeMessageObjects.size(); i3++) {
                    TL_stories$StoryItem tL_stories$StoryItem = ((MessageObject) MediaActivity.this.actionModeMessageObjects.valueAt(i3)).storyItem;
                    if (tL_stories$StoryItem != null) {
                        arrayList.add(tL_stories$StoryItem);
                    }
                }
                if (arrayList.isEmpty()) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MediaActivity.this.getContext(), MediaActivity.this.getResourceProvider());
                if (arrayList.size() > 1) {
                    i2 = R.string.DeleteStoriesTitle;
                    str = "DeleteStoriesTitle";
                } else {
                    i2 = R.string.DeleteStoryTitle;
                    str = "DeleteStoryTitle";
                }
                builder.setTitle(LocaleController.getString(str, i2));
                builder.setMessage(LocaleController.formatPluralString("DeleteStoriesSubtitle", arrayList.size(), new Object[0]));
                builder.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.MediaActivity.1.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i4) {
                        MediaActivity.this.getMessagesController().getStoriesController().deleteStories(arrayList);
                        MediaActivity.this.sharedMediaLayout.closeActionMode(false);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), MediaActivity$1$$ExternalSyntheticLambda0.INSTANCE);
                AlertDialog create = builder.create();
                create.show();
                create.redPositive();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(View view) {
        this.optionsItem.toggleSubMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(View view) {
        Boolean zoomIn = this.sharedMediaLayout.zoomIn();
        if (zoomIn == null) {
            return;
        }
        boolean booleanValue = zoomIn.booleanValue();
        this.zoomOutItem.setEnabled(true);
        this.zoomOutItem.animate().alpha(this.zoomOutItem.isEnabled() ? 1.0f : 0.5f).start();
        this.zoomInItem.setEnabled(booleanValue);
        this.zoomInItem.animate().alpha(this.zoomInItem.isEnabled() ? 1.0f : 0.5f).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        Boolean zoomOut = this.sharedMediaLayout.zoomOut();
        if (zoomOut == null) {
            return;
        }
        this.zoomOutItem.setEnabled(zoomOut.booleanValue());
        this.zoomOutItem.animate().alpha(this.zoomOutItem.isEnabled() ? 1.0f : 0.5f).start();
        this.zoomInItem.setEnabled(true);
        this.zoomInItem.animate().alpha(this.zoomInItem.isEnabled() ? 1.0f : 0.5f).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(View view) {
        boolean z = this.filterPhotos;
        if (z && !this.filterVideos) {
            BotWebViewVibrationEffect.APP_ERROR.vibrate();
            ActionBarMenuSubItem actionBarMenuSubItem = this.showPhotosItem;
            int i = -this.shiftDp;
            this.shiftDp = i;
            AndroidUtilities.shakeViewSpring(actionBarMenuSubItem, i);
            return;
        }
        ActionBarMenuSubItem actionBarMenuSubItem2 = this.showPhotosItem;
        boolean z2 = !z;
        this.filterPhotos = z2;
        actionBarMenuSubItem2.setChecked(z2);
        this.sharedMediaLayout.setStoriesFilter(this.filterPhotos, this.filterVideos);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(View view) {
        boolean z = this.filterVideos;
        if (z && !this.filterPhotos) {
            BotWebViewVibrationEffect.APP_ERROR.vibrate();
            ActionBarMenuSubItem actionBarMenuSubItem = this.showVideosItem;
            int i = -this.shiftDp;
            this.shiftDp = i;
            AndroidUtilities.shakeViewSpring(actionBarMenuSubItem, i);
            return;
        }
        ActionBarMenuSubItem actionBarMenuSubItem2 = this.showVideosItem;
        boolean z2 = !z;
        this.filterVideos = z2;
        actionBarMenuSubItem2.setChecked(z2);
        this.sharedMediaLayout.setStoriesFilter(this.filterPhotos, this.filterVideos);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(Integer num) {
        this.sharedMediaLayout.scrollToPage(num.intValue() + 8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(View view) {
        int i;
        Bulletin show;
        Runnable runnable = this.applyBulletin;
        if (runnable != null) {
            runnable.run();
            this.applyBulletin = null;
        }
        Bulletin.hideVisible();
        final boolean z = this.sharedMediaLayout.getClosestTab() == 9;
        final ArrayList arrayList = new ArrayList();
        if (this.actionModeMessageObjects != null) {
            i = 0;
            for (int i2 = 0; i2 < this.actionModeMessageObjects.size(); i2++) {
                TL_stories$StoryItem tL_stories$StoryItem = this.actionModeMessageObjects.valueAt(i2).storyItem;
                if (tL_stories$StoryItem != null) {
                    arrayList.add(tL_stories$StoryItem);
                    i++;
                }
            }
        } else {
            i = 0;
        }
        this.sharedMediaLayout.closeActionMode(false);
        this.sharedMediaLayout.disableScroll(false);
        if (z) {
            this.sharedMediaLayout.scrollToPage(8);
        }
        if (arrayList.isEmpty()) {
            return;
        }
        final boolean[] zArr = new boolean[arrayList.size()];
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            TL_stories$StoryItem tL_stories$StoryItem2 = (TL_stories$StoryItem) arrayList.get(i3);
            zArr[i3] = tL_stories$StoryItem2.pinned;
            tL_stories$StoryItem2.pinned = z;
        }
        getMessagesController().getStoriesController().updateStoriesInLists(this.dialogId, arrayList);
        final boolean[] zArr2 = {false};
        this.applyBulletin = new Runnable() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                MediaActivity.this.lambda$createView$7(arrayList, z);
            }
        };
        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                MediaActivity.this.lambda$createView$8(zArr2, arrayList, zArr);
            }
        };
        if (z) {
            show = BulletinFactory.of(this).createSimpleBulletin(R.raw.contact_check, LocaleController.formatPluralString("StorySavedTitle", i, new Object[0]), LocaleController.getString("StorySavedSubtitle"), LocaleController.getString("Undo"), runnable2).show();
        } else {
            show = BulletinFactory.of(this).createSimpleBulletin(R.raw.chats_archived, LocaleController.formatPluralString("StoryArchived", i, new Object[0]), LocaleController.getString("Undo"), 5000, runnable2).show();
        }
        show.setOnHideListener(new Runnable() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                MediaActivity.this.lambda$createView$9(zArr2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(ArrayList arrayList, boolean z) {
        getMessagesController().getStoriesController().updateStoriesPinned(this.dialogId, arrayList, z, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(boolean[] zArr, ArrayList arrayList, boolean[] zArr2) {
        zArr[0] = true;
        AndroidUtilities.cancelRunOnUIThread(this.applyBulletin);
        for (int i = 0; i < arrayList.size(); i++) {
            ((TL_stories$StoryItem) arrayList.get(i)).pinned = zArr2[i];
        }
        getMessagesController().getStoriesController().updateStoriesInLists(this.dialogId, arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(boolean[] zArr) {
        Runnable runnable;
        if (!zArr[0] && (runnable = this.applyBulletin) != null) {
            runnable.run();
        }
        this.applyBulletin = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$11(View view) {
        StoryRecorder.getInstance(getParentActivity(), getCurrentAccount()).open(StoryRecorder.SourceView.fromFloatingButton(this.floatingButtonContainer));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (closeStoryViewer()) {
            return false;
        }
        if (this.sharedMediaLayout.isActionModeShown()) {
            this.sharedMediaLayout.closeActionMode(false);
            return false;
        }
        return super.onBackPressed();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        if (this.sharedMediaLayout.isSwipeBackEnabled()) {
            return this.sharedMediaLayout.isCurrentTabFirst();
        }
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean canBeginSlide() {
        if (this.sharedMediaLayout.isSwipeBackEnabled()) {
            return super.canBeginSlide();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMediaCount() {
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout == null) {
            return;
        }
        int closestTab = sharedMediaLayout.getClosestTab();
        int[] lastMediaCount = this.sharedMediaPreloader.getLastMediaCount();
        boolean z = !LocaleController.isRTL;
        int i = (this.type == 1 && closestTab != 8) ? 1 : 0;
        if (closestTab == 8 || closestTab == 9) {
            ActionBarMenuSubItem actionBarMenuSubItem = this.zoomOutItem;
            if (actionBarMenuSubItem != null) {
                actionBarMenuSubItem.setEnabled(this.sharedMediaLayout.canZoomOut());
                ActionBarMenuSubItem actionBarMenuSubItem2 = this.zoomOutItem;
                actionBarMenuSubItem2.setAlpha(actionBarMenuSubItem2.isEnabled() ? 1.0f : 0.5f);
            }
            ActionBarMenuSubItem actionBarMenuSubItem3 = this.zoomInItem;
            if (actionBarMenuSubItem3 != null) {
                actionBarMenuSubItem3.setEnabled(this.sharedMediaLayout.canZoomIn());
                ActionBarMenuSubItem actionBarMenuSubItem4 = this.zoomInItem;
                actionBarMenuSubItem4.setAlpha(actionBarMenuSubItem4.isEnabled() ? 1.0f : 0.5f);
            }
            int storiesCount = this.sharedMediaLayout.getStoriesCount(8);
            if (storiesCount > 0) {
                showSubtitle(0, true, true);
                this.subtitleTextView[0].setText(LocaleController.formatPluralString("ProfileMyStoriesCount", storiesCount, new Object[0]), z);
            } else {
                showSubtitle(0, false, true);
            }
            if (this.type == 1) {
                int storiesCount2 = this.sharedMediaLayout.getStoriesCount(9);
                if (storiesCount2 > 0) {
                    showSubtitle(1, true, true);
                    this.subtitleTextView[1].setText(LocaleController.formatPluralString("ProfileStoriesArchiveCount", storiesCount2, new Object[0]), z);
                } else {
                    showSubtitle(1, false, true);
                }
                hideFloatingButton(closestTab != 9 || this.sharedMediaLayout.getStoriesCount(9) > 0, true);
            }
            if (this.optionsItem != null) {
                SharedMediaLayout sharedMediaLayout2 = this.sharedMediaLayout;
                final boolean z2 = sharedMediaLayout2.getStoriesCount(sharedMediaLayout2.getClosestTab()) <= 0;
                if (!z2) {
                    this.optionsItem.setVisibility(0);
                }
                this.optionsItem.animate().alpha(z2 ? 0.0f : 1.0f).withEndAction(new Runnable() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaActivity.this.lambda$updateMediaCount$12(z2);
                    }
                }).setDuration(220L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
            }
            ButtonWithCounterView buttonWithCounterView = this.button;
            if (buttonWithCounterView != null) {
                boolean z3 = z && this.lastTab == closestTab;
                if (closestTab == 8) {
                    SparseArray<MessageObject> sparseArray = this.actionModeMessageObjects;
                    buttonWithCounterView.setText(LocaleController.formatPluralString("ArchiveStories", sparseArray == null ? 0 : sparseArray.size(), new Object[0]), z3);
                } else {
                    buttonWithCounterView.setText(LocaleController.getString("SaveToProfile", R.string.SaveToProfile), z3);
                }
                this.lastTab = closestTab;
            }
            if (this.calendarItem != null) {
                boolean z4 = this.sharedMediaLayout.getStoriesCount(closestTab) > 0;
                this.calendarItem.setEnabled(z4);
                this.calendarItem.setAlpha(z4 ? 1.0f : 0.5f);
            }
        } else if (closestTab < 0 || lastMediaCount[closestTab] < 0) {
        } else {
            if (closestTab == 0) {
                showSubtitle(i, true, true);
                if (this.sharedMediaLayout.getPhotosVideosTypeFilter() == 1) {
                    this.subtitleTextView[i].setText(LocaleController.formatPluralString("Photos", lastMediaCount[6], new Object[0]), z);
                } else if (this.sharedMediaLayout.getPhotosVideosTypeFilter() == 2) {
                    this.subtitleTextView[i].setText(LocaleController.formatPluralString("Videos", lastMediaCount[7], new Object[0]), z);
                } else {
                    this.subtitleTextView[i].setText(LocaleController.formatPluralString("Media", lastMediaCount[0], new Object[0]), z);
                }
            } else if (closestTab == 1) {
                showSubtitle(i, true, true);
                this.subtitleTextView[i].setText(LocaleController.formatPluralString("Files", lastMediaCount[1], new Object[0]), z);
            } else if (closestTab == 2) {
                showSubtitle(i, true, true);
                this.subtitleTextView[i].setText(LocaleController.formatPluralString("Voice", lastMediaCount[2], new Object[0]), z);
            } else if (closestTab == 3) {
                showSubtitle(i, true, true);
                this.subtitleTextView[i].setText(LocaleController.formatPluralString("Links", lastMediaCount[3], new Object[0]), z);
            } else if (closestTab == 4) {
                showSubtitle(i, true, true);
                this.subtitleTextView[i].setText(LocaleController.formatPluralString("MusicFiles", lastMediaCount[4], new Object[0]), z);
            } else if (closestTab == 5) {
                showSubtitle(i, true, true);
                this.subtitleTextView[i].setText(LocaleController.formatPluralString("GIFs", lastMediaCount[5], new Object[0]), z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateMediaCount$12(boolean z) {
        if (z) {
            this.optionsItem.setVisibility(8);
        }
    }

    public void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        this.currentChatInfo = tLRPC$ChatFull;
    }

    public long getDialogId() {
        return this.dialogId;
    }

    private void updateFloatingButtonOffset() {
        FrameLayout frameLayout = this.floatingButtonContainer;
        if (frameLayout == null) {
            return;
        }
        frameLayout.setTranslationY(this.floatingButtonTranslation + this.floatingButtonTranslation1 + this.floatingButtonTranslation2);
    }

    private void hideFloatingButton(boolean z, boolean z2) {
        if (this.floatingButtonContainer == null) {
            return;
        }
        if (!getMessagesController().storiesEnabled()) {
            z = true;
        }
        if (this.floatingHidden == z) {
            return;
        }
        this.floatingHidden = z;
        AnimatorSet animatorSet = this.floatingAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (z2) {
            this.floatingButtonContainer.setVisibility(0);
            this.floatingAnimator = new AnimatorSet();
            float[] fArr = new float[2];
            fArr[0] = this.floatingButtonHideProgress;
            fArr[1] = this.floatingHidden ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    MediaActivity.this.lambda$hideFloatingButton$13(valueAnimator);
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.MediaActivity.7
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (MediaActivity.this.floatingHidden) {
                        MediaActivity.this.floatingButtonContainer.setVisibility(8);
                    }
                }
            });
            this.floatingAnimator.playTogether(ofFloat);
            this.floatingAnimator.setDuration(300L);
            this.floatingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            this.floatingButtonContainer.setClickable(!z);
            this.floatingAnimator.start();
            return;
        }
        this.floatingButtonHideProgress = z ? 1.0f : 0.0f;
        this.floatingButtonTranslation = AndroidUtilities.dp(100.0f) * this.floatingButtonHideProgress;
        updateFloatingButtonOffset();
        this.floatingButtonContainer.setVisibility(z ? 8 : 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideFloatingButton$13(ValueAnimator valueAnimator) {
        this.floatingButtonHideProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.floatingButtonTranslation = AndroidUtilities.dp(100.0f) * this.floatingButtonHideProgress;
        updateFloatingButtonOffset();
    }

    private void showSubtitle(final int i, final boolean z, boolean z2) {
        if (i == 1 && this.type == 2) {
            return;
        }
        boolean[] zArr = this.subtitleShown;
        if (zArr[i] != z || this.firstSubtitleCheck[i]) {
            boolean[] zArr2 = this.firstSubtitleCheck;
            boolean z3 = !zArr2[i] && z2;
            zArr2[i] = false;
            zArr[i] = z;
            ValueAnimator[] valueAnimatorArr = this.subtitleAnimator;
            if (valueAnimatorArr[i] != null) {
                valueAnimatorArr[i].cancel();
                this.subtitleAnimator[i] = null;
            }
            if (z3) {
                this.subtitleTextView[i].setVisibility(0);
                ValueAnimator[] valueAnimatorArr2 = this.subtitleAnimator;
                float[] fArr = new float[2];
                fArr[0] = this.subtitleT[i];
                fArr[1] = z ? 1.0f : 0.0f;
                valueAnimatorArr2[i] = ValueAnimator.ofFloat(fArr);
                this.subtitleAnimator[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        MediaActivity.this.lambda$showSubtitle$14(i, valueAnimator);
                    }
                });
                this.subtitleAnimator[i].addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.MediaActivity.8
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        MediaActivity.this.subtitleT[i] = z ? 1.0f : 0.0f;
                        MediaActivity.this.nameTextView[i].setScaleX(z ? 1.0f : 1.111f);
                        MediaActivity.this.nameTextView[i].setScaleY(z ? 1.0f : 1.111f);
                        MediaActivity.this.nameTextView[i].setTranslationY(z ? 0.0f : AndroidUtilities.dp(8.0f));
                        MediaActivity.this.subtitleTextView[i].setAlpha(z ? 1.0f : 0.0f);
                        if (z) {
                            return;
                        }
                        MediaActivity.this.subtitleTextView[i].setVisibility(8);
                    }
                });
                this.subtitleAnimator[i].setDuration(320L);
                this.subtitleAnimator[i].setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.subtitleAnimator[i].start();
                return;
            }
            this.subtitleT[i] = z ? 1.0f : 0.0f;
            this.nameTextView[i].setScaleX(z ? 1.0f : 1.111f);
            this.nameTextView[i].setScaleY(z ? 1.0f : 1.111f);
            this.nameTextView[i].setTranslationY(z ? 0.0f : AndroidUtilities.dp(8.0f));
            this.subtitleTextView[i].setAlpha(z ? 1.0f : 0.0f);
            this.subtitleTextView[i].setVisibility(z ? 0 : 8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSubtitle$14(int i, ValueAnimator valueAnimator) {
        this.subtitleT[i] = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.nameTextView[i].setScaleX(AndroidUtilities.lerp(1.111f, 1.0f, this.subtitleT[i]));
        this.nameTextView[i].setScaleY(AndroidUtilities.lerp(1.111f, 1.0f, this.subtitleT[i]));
        this.nameTextView[i].setTranslationY(AndroidUtilities.lerp(AndroidUtilities.dp(8.0f), 0, this.subtitleT[i]));
        this.subtitleTextView[i].setAlpha(this.subtitleT[i]);
    }

    @Override // org.telegram.ui.Components.SharedMediaLayout.SharedMediaPreloaderDelegate
    public void mediaCountUpdated() {
        SharedMediaLayout.SharedMediaPreloader sharedMediaPreloader;
        SharedMediaLayout sharedMediaLayout = this.sharedMediaLayout;
        if (sharedMediaLayout != null && (sharedMediaPreloader = this.sharedMediaPreloader) != null) {
            sharedMediaLayout.setNewMediaCounts(sharedMediaPreloader.getLastMediaCount());
        }
        updateMediaCount();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateColors() {
        this.actionBar.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        ActionBar actionBar = this.actionBar;
        int i = Theme.key_windowBackgroundWhiteBlackText;
        actionBar.setItemsColor(Theme.getColor(i), false);
        this.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarActionModeDefaultSelector), false);
        this.actionBar.setTitleColor(Theme.getColor(i));
        this.nameTextView[0].setTextColor(Theme.getColor(i));
        SimpleTextView[] simpleTextViewArr = this.nameTextView;
        if (simpleTextViewArr[1] != null) {
            simpleTextViewArr[1].setTextColor(Theme.getColor(i));
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda16
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                MediaActivity.this.updateColors();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_actionBarActionModeDefaultSelector));
        arrayList.add(new ThemeDescription(null, 0, null, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.addAll(this.sharedMediaLayout.getThemeDescriptions());
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        StoryViewer storyViewer = this.storyViewer;
        if (storyViewer == null || !storyViewer.isShown()) {
            int color = Theme.getColor(Theme.key_windowBackgroundWhite);
            if (this.actionBar.isActionModeShowed()) {
                color = Theme.getColor(Theme.key_actionBarActionModeDefault);
            }
            return ColorUtils.calculateLuminance(color) > 0.699999988079071d;
        }
        return false;
    }

    @Override // org.telegram.ui.Components.FloatingDebug.FloatingDebugProvider
    public List<FloatingDebugController.DebugItem> onGetDebugItems() {
        FloatingDebugController.DebugItem[] debugItemArr = new FloatingDebugController.DebugItem[1];
        StringBuilder sb = new StringBuilder();
        sb.append(ShapeDetector.isLearning(getContext()) ? "Disable" : "Enable");
        sb.append(" shape detector learning debug");
        debugItemArr[0] = new FloatingDebugController.DebugItem(sb.toString(), new Runnable() { // from class: org.telegram.ui.Components.MediaActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                MediaActivity.this.lambda$onGetDebugItems$15();
            }
        });
        return Arrays.asList(debugItemArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onGetDebugItems$15() {
        ShapeDetector.setLearning(getContext(), !ShapeDetector.isLearning(getContext()));
    }

    /* loaded from: classes4.dex */
    private class StoriesTabsView extends BottomPagerTabs {
        public StoriesTabsView(MediaActivity mediaActivity, Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
        }

        @Override // org.telegram.ui.Components.BottomPagerTabs
        public BottomPagerTabs.Tab[] createTabs() {
            BottomPagerTabs.Tab[] tabArr = {new BottomPagerTabs.Tab(0, R.raw.msg_stories_saved, LocaleController.getString("ProfileMyStoriesTab", R.string.ProfileMyStoriesTab)), new BottomPagerTabs.Tab(1, R.raw.msg_stories_archive, LocaleController.getString("ProfileStoriesArchiveTab", R.string.ProfileStoriesArchiveTab))};
            tabArr[0].customEndFrameMid = 20;
            tabArr[0].customEndFrameEnd = 40;
            return tabArr;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public int getNavigationBarColor() {
        int themedColor = getThemedColor(Theme.key_windowBackgroundWhite);
        StoryViewer storyViewer = this.storyViewer;
        return (storyViewer == null || !storyViewer.attachedToParent()) ? themedColor : this.storyViewer.getNavigationBarColor(themedColor);
    }
}
