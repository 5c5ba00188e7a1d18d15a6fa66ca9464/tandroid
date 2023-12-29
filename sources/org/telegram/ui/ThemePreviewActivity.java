package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.util.Property;
import android.util.SparseIntArray;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.exoplayer2.util.Consumer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChannelBoostsController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ChatThemeController;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$KeyboardButton;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$ReactionCount;
import org.telegram.tgnet.TLRPC$TL_account_getWallPaper;
import org.telegram.tgnet.TLRPC$TL_account_getWallPapers;
import org.telegram.tgnet.TLRPC$TL_account_wallPapers;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_inputWallPaperSlug;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageActionSetChatWallPaper;
import org.telegram.tgnet.TLRPC$TL_messageEntityTextUrl;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageReplyHeader;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.tgnet.TLRPC$TL_photoSize;
import org.telegram.tgnet.TLRPC$TL_photoStrippedSize;
import org.telegram.tgnet.TLRPC$TL_premiumGiftOption;
import org.telegram.tgnet.TLRPC$TL_replyInlineMarkup;
import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.tgnet.TLRPC$TL_user;
import org.telegram.tgnet.TLRPC$TL_wallPaper;
import org.telegram.tgnet.TLRPC$TL_wallPaperSettings;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$WallPaper;
import org.telegram.tgnet.TLRPC$WallPaperSettings;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.tgnet.tl.TL_stories$TL_premium_boostsStatus;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.MenuDrawable;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.PatternCell;
import org.telegram.ui.Cells.TextSelectionHelper;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.BackgroundGradientDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CircularProgressDrawable;
import org.telegram.ui.Components.ColorPicker;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.GestureDetector2;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.Components.WallpaperCheckBoxView;
import org.telegram.ui.Components.WallpaperParallaxEffect;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.Stories.recorder.PreviewView;
import org.telegram.ui.Stories.recorder.SliderView;
import org.telegram.ui.ThemePreviewActivity;
import org.telegram.ui.WallpapersListActivity;
/* loaded from: classes3.dex */
public class ThemePreviewActivity extends BaseFragment implements DownloadController.FileDownloadProgressListener, NotificationCenter.NotificationCenterDelegate {
    private int TAG;
    private Theme.ThemeAccent accent;
    private ActionBar actionBar2;
    private HintView animationHint;
    private BlurButton applyButton1;
    private BlurButton applyButton2;
    private Runnable applyColorAction;
    private boolean applyColorScheduled;
    private Theme.ThemeInfo applyingTheme;
    private FrameLayout backgroundButtonsContainer;
    private WallpaperCheckBoxView[] backgroundCheckBoxView;
    private int backgroundColor;
    private int backgroundGradientColor1;
    private int backgroundGradientColor2;
    private int backgroundGradientColor3;
    private BackgroundGradientDrawable.Disposable backgroundGradientDisposable;
    private BackgroundView backgroundImage;
    private BackgroundView[] backgroundImages;
    private ImageView backgroundPlayAnimationImageView;
    private FrameLayout backgroundPlayAnimationView;
    private AnimatorSet backgroundPlayViewAnimator;
    private int backgroundRotation;
    private int backupAccentColor;
    private int backupAccentColor2;
    private long backupBackgroundGradientOverrideColor1;
    private long backupBackgroundGradientOverrideColor2;
    private long backupBackgroundGradientOverrideColor3;
    private long backupBackgroundOverrideColor;
    private int backupBackgroundRotation;
    private float backupIntensity;
    private int backupMyMessagesAccentColor;
    private boolean backupMyMessagesAnimated;
    private int backupMyMessagesGradientAccentColor1;
    private int backupMyMessagesGradientAccentColor2;
    private int backupMyMessagesGradientAccentColor3;
    private String backupSlug;
    private final PorterDuff.Mode blendMode;
    private Bitmap blurredBitmap;
    private BitmapDrawable blurredDrawable;
    public TL_stories$TL_premium_boostsStatus boostsStatus;
    private FrameLayout bottomOverlayChat;
    private TextView cancelButton;
    private View changeDayNightView;
    private ValueAnimator changeDayNightViewAnimator;
    private ValueAnimator changeDayNightViewAnimator2;
    private float changeDayNightViewProgress;
    private int checkColor;
    private boolean checkedBoostsLevel;
    private boolean checkingBoostsLevel;
    private ColorPicker colorPicker;
    private int colorType;
    float croppedWidth;
    private float currentIntensity;
    float currentScrollOffset;
    private Object currentWallpaper;
    private Bitmap currentWallpaperBitmap;
    private ActionBarMenuItem dayNightItem;
    float defaultScrollOffset;
    private WallpaperActivityDelegate delegate;
    private boolean deleteOnCancel;
    long dialogId;
    private DialogsAdapter dialogsAdapter;
    private float dimAmount;
    private SliderView dimmingSlider;
    private FrameLayout dimmingSliderContainer;
    private TextView doneButton;
    private View dotsContainer;
    private TextView dropDown;
    private ActionBarMenuItem dropDownContainer;
    private boolean editingTheme;
    private ImageView floatingButton;
    private FrameLayout frameLayout;
    GestureDetector2 gestureDetector2;
    private boolean hasScrollingBackground;
    private String imageFilter;
    private HeaderCell intensityCell;
    private SeekBarView intensitySeekBar;
    private boolean isBlurred;
    private boolean isMotion;
    private WeakReference<Drawable> lastDrawableToBlur;
    private int lastPickedColor;
    private int lastPickedColorNum;
    private TLRPC$TL_wallPaper lastSelectedPattern;
    private int lastSizeHash;
    private RecyclerListView listView;
    private RecyclerListView listView2;
    private ColoredImageSpan lockSpan;
    float maxScrollOffset;
    private int maxWallpaperSize;
    private MessagesAdapter messagesAdapter;
    private FrameLayout messagesButtonsContainer;
    private WallpaperCheckBoxView[] messagesCheckBoxView;
    private ImageView messagesPlayAnimationImageView;
    private FrameLayout messagesPlayAnimationView;
    private AnimatorSet messagesPlayViewAnimator;
    private AnimatorSet motionAnimation;
    Theme.MessageDrawable msgOutDrawable;
    Theme.MessageDrawable msgOutDrawableSelected;
    Theme.MessageDrawable msgOutMediaDrawable;
    Theme.MessageDrawable msgOutMediaDrawableSelected;
    private boolean nightTheme;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    DayNightSwitchDelegate onSwitchDayNightDelegate;
    private Bitmap originalBitmap;
    private FrameLayout page1;
    private FrameLayout page2;
    private WallpaperParallaxEffect parallaxEffect;
    private float parallaxScale;
    private int patternColor;
    private FrameLayout[] patternLayout;
    private TextView patternTitleView;
    private AnimatorSet patternViewAnimation;
    private ArrayList<Object> patterns;
    private PatternsAdapter patternsAdapter;
    private FrameLayout[] patternsButtonsContainer;
    private TextView[] patternsCancelButton;
    private HashMap<Long, Object> patternsDict;
    private LinearLayoutManager patternsLayoutManager;
    private RecyclerListView patternsListView;
    private TextView[] patternsSaveButton;
    private int previousBackgroundColor;
    private int previousBackgroundGradientColor1;
    private int previousBackgroundGradientColor2;
    private int previousBackgroundGradientColor3;
    private int previousBackgroundRotation;
    private float previousIntensity;
    private TLRPC$TL_wallPaper previousSelectedPattern;
    private float progressToDarkTheme;
    private boolean progressVisible;
    private boolean removeBackgroundOverride;
    private boolean rotatePreview;
    private FrameLayout saveButtonsContainer;
    private ActionBarMenuItem saveItem;
    private final int screenType;
    private Scroller scroller;
    private TLRPC$TL_wallPaper selectedPattern;
    boolean self;
    MessageObject serverWallpaper;
    private boolean setupFinished;
    private Drawable sheetDrawable;
    private boolean shouldShowBrightnessControll;
    private boolean shouldShowDayNightIcon;
    private boolean showColor;
    private RLottieDrawable sunDrawable;
    public final ThemeDelegate themeDelegate;
    private List<ThemeDescription> themeDescriptions;
    private UndoView undoView;
    public boolean useDefaultThemeForButtons;
    private ValueAnimator valueAnimator;
    private ViewPager viewPager;
    private boolean wasScroll;
    private long watchForKeyboardEndTime;

    /* loaded from: classes3.dex */
    public interface DayNightSwitchDelegate {
        boolean isDark();

        boolean supportsAnimation();

        void switchDayNight(boolean z);
    }

    /* loaded from: classes3.dex */
    public interface WallpaperActivityDelegate {
        void didSetNewBackground(TLRPC$WallPaper tLRPC$WallPaper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$applyWallpaperBackground$21() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$3(View view, int i) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$toggleTheme$34(View view, MotionEvent motionEvent) {
        return true;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void setResourceProvider(Theme.ResourcesProvider resourcesProvider) {
        this.themeDelegate.parentProvider = resourcesProvider;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Theme.ResourcesProvider getResourceProvider() {
        return this.themeDelegate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.applyColorScheduled = false;
        applyColor(this.lastPickedColor, this.lastPickedColorNum);
        this.lastPickedColorNum = -1;
    }

    private void checkBoostsLevel() {
        if (this.dialogId >= 0 || this.checkingBoostsLevel || this.checkedBoostsLevel || this.boostsStatus != null) {
            return;
        }
        this.checkingBoostsLevel = true;
        getMessagesController().getBoostsController().getBoostsStats(this.dialogId, new Consumer() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda18
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                ThemePreviewActivity.this.lambda$checkBoostsLevel$1((TL_stories$TL_premium_boostsStatus) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkBoostsLevel$1(TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus) {
        this.boostsStatus = tL_stories$TL_premium_boostsStatus;
        this.checkedBoostsLevel = true;
        updateApplyButton1(true);
        this.checkingBoostsLevel = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void showFor(final ChatActivity chatActivity, MessageObject messageObject) {
        TLRPC$WallPaper tLRPC$WallPaper;
        TLRPC$MessageAction tLRPC$MessageAction = messageObject.messageOwner.action;
        if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatWallPaper) {
            TLRPC$WallPaper tLRPC$WallPaper2 = ((TLRPC$TL_messageActionSetChatWallPaper) tLRPC$MessageAction).wallpaper;
            if (tLRPC$WallPaper2.pattern || tLRPC$WallPaper2.document == null) {
                String str = tLRPC$WallPaper2.slug;
                TLRPC$WallPaperSettings tLRPC$WallPaperSettings = tLRPC$WallPaper2.settings;
                int i = tLRPC$WallPaperSettings.background_color;
                int i2 = tLRPC$WallPaperSettings.second_background_color;
                int i3 = tLRPC$WallPaperSettings.third_background_color;
                int i4 = tLRPC$WallPaperSettings.fourth_background_color;
                int wallpaperRotation = AndroidUtilities.getWallpaperRotation(tLRPC$WallPaperSettings.rotation, false);
                TLRPC$WallPaperSettings tLRPC$WallPaperSettings2 = tLRPC$WallPaper2.settings;
                WallpapersListActivity.ColorWallpaper colorWallpaper = new WallpapersListActivity.ColorWallpaper(str, i, i2, i3, i4, wallpaperRotation, tLRPC$WallPaperSettings2.intensity / 100.0f, tLRPC$WallPaperSettings2.motion, null);
                if (tLRPC$WallPaper2 instanceof TLRPC$TL_wallPaper) {
                    colorWallpaper.pattern = (TLRPC$TL_wallPaper) tLRPC$WallPaper2;
                }
                tLRPC$WallPaper = colorWallpaper;
            } else {
                tLRPC$WallPaper = tLRPC$WallPaper2;
            }
            final boolean isDark = Theme.getActiveTheme().isDark();
            ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(tLRPC$WallPaper, null, true, false) { // from class: org.telegram.ui.ThemePreviewActivity.3
                @Override // org.telegram.ui.ActionBar.BaseFragment
                public void onFragmentClosed() {
                    super.onFragmentClosed();
                    ChatActivity.ThemeDelegate themeDelegate = chatActivity.themeDelegate;
                    themeDelegate.setCurrentTheme(themeDelegate.getCurrentTheme(), chatActivity.themeDelegate.getCurrentWallpaper(), false, Boolean.valueOf(isDark));
                }
            };
            TLRPC$WallPaperSettings tLRPC$WallPaperSettings3 = tLRPC$WallPaper2.settings;
            if (tLRPC$WallPaperSettings3 != null) {
                themePreviewActivity.setInitialModes(tLRPC$WallPaperSettings3.blur, tLRPC$WallPaperSettings3.motion, tLRPC$WallPaperSettings3.intensity / 100.0f);
            }
            themePreviewActivity.setCurrentServerWallpaper(messageObject);
            themePreviewActivity.setDialogId(messageObject.getDialogId());
            themePreviewActivity.setResourceProvider(chatActivity.themeDelegate);
            themePreviewActivity.setOnSwitchDayNightDelegate(new DayNightSwitchDelegate(isDark, chatActivity) { // from class: org.telegram.ui.ThemePreviewActivity.4
                boolean forceDark;
                final /* synthetic */ ChatActivity val$chatActivity;
                final /* synthetic */ boolean val$initialIsDark;

                @Override // org.telegram.ui.ThemePreviewActivity.DayNightSwitchDelegate
                public boolean supportsAnimation() {
                    return true;
                }

                {
                    this.val$initialIsDark = isDark;
                    this.val$chatActivity = chatActivity;
                    this.forceDark = isDark;
                }

                @Override // org.telegram.ui.ThemePreviewActivity.DayNightSwitchDelegate
                public boolean isDark() {
                    return this.forceDark;
                }

                @Override // org.telegram.ui.ThemePreviewActivity.DayNightSwitchDelegate
                public void switchDayNight(boolean z) {
                    this.forceDark = !this.forceDark;
                    ChatActivity.ThemeDelegate themeDelegate = this.val$chatActivity.themeDelegate;
                    themeDelegate.setCurrentTheme(themeDelegate.getCurrentTheme(), this.val$chatActivity.themeDelegate.getCurrentWallpaper(), z, Boolean.valueOf(this.forceDark));
                }
            });
            chatActivity.presentFragment(themePreviewActivity);
        }
    }

    private void setCurrentServerWallpaper(MessageObject messageObject) {
        this.serverWallpaper = messageObject;
    }

    public void setDialogId(long j) {
        this.dialogId = j;
        this.self = j == 0 || j == getUserConfig().getClientUserId();
    }

    public void setOnSwitchDayNightDelegate(DayNightSwitchDelegate dayNightSwitchDelegate) {
        this.onSwitchDayNightDelegate = dayNightSwitchDelegate;
    }

    public ThemePreviewActivity(Object obj, Bitmap bitmap) {
        this(obj, bitmap, false, false);
    }

    public ThemePreviewActivity(Object obj, Bitmap bitmap, boolean z, boolean z2) {
        this.themeDelegate = new ThemeDelegate() { // from class: org.telegram.ui.ThemePreviewActivity.1
            @Override // org.telegram.ui.ThemePreviewActivity.ThemeDelegate, org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public boolean isDark() {
                DayNightSwitchDelegate dayNightSwitchDelegate = ThemePreviewActivity.this.onSwitchDayNightDelegate;
                if (dayNightSwitchDelegate != null) {
                    return dayNightSwitchDelegate.isDark();
                }
                return super.isDark();
            }
        };
        this.useDefaultThemeForButtons = true;
        this.colorType = 1;
        this.msgOutDrawable = new MessageDrawable(0, true, false);
        this.msgOutDrawableSelected = new MessageDrawable(0, true, true);
        this.msgOutMediaDrawable = new MessageDrawable(1, true, false);
        this.msgOutMediaDrawableSelected = new MessageDrawable(1, true, true);
        this.lastPickedColorNum = -1;
        this.applyColorAction = new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                ThemePreviewActivity.this.lambda$new$0();
            }
        };
        this.backgroundImages = new BackgroundView[2];
        this.patternLayout = new FrameLayout[2];
        this.patternsCancelButton = new TextView[2];
        this.patternsSaveButton = new TextView[2];
        this.patternsButtonsContainer = new FrameLayout[2];
        this.patternsDict = new HashMap<>();
        this.currentIntensity = 0.5f;
        this.dimAmount = 0.0f;
        this.blendMode = PorterDuff.Mode.SRC_IN;
        this.parallaxScale = 1.0f;
        this.imageFilter = "640_360";
        this.maxWallpaperSize = 1920;
        this.self = true;
        this.gestureDetector2 = new GestureDetector2(getContext(), new GestureDetector2.OnGestureListener() { // from class: org.telegram.ui.ThemePreviewActivity.2
            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public void onShowPress(MotionEvent motionEvent) {
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public void onUp(MotionEvent motionEvent) {
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                if (ThemePreviewActivity.this.scroller != null) {
                    ThemePreviewActivity.this.scroller.abortAnimation();
                    return true;
                }
                return true;
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (ThemePreviewActivity.this.scroller != null) {
                    ThemePreviewActivity.this.scroller.abortAnimation();
                }
                ThemePreviewActivity themePreviewActivity = ThemePreviewActivity.this;
                themePreviewActivity.currentScrollOffset = Utilities.clamp(themePreviewActivity.currentScrollOffset + f, themePreviewActivity.maxScrollOffset, 0.0f);
                ThemePreviewActivity.this.invalidateBlur();
                ThemePreviewActivity.this.backgroundImage.invalidate();
                return true;
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (ThemePreviewActivity.this.scroller != null) {
                    ThemePreviewActivity.this.scroller.abortAnimation();
                    ThemePreviewActivity.this.scroller.fling((int) ThemePreviewActivity.this.currentScrollOffset, 0, Math.round(-f), Math.round(f2), 0, (int) ThemePreviewActivity.this.maxScrollOffset, 0, ConnectionsManager.DEFAULT_DATACENTER_ID);
                    ThemePreviewActivity.this.backgroundImage.postInvalidate();
                    return true;
                }
                return true;
            }
        });
        this.checkingBoostsLevel = false;
        this.checkedBoostsLevel = false;
        this.screenType = 2;
        this.showColor = z2;
        this.currentWallpaper = obj;
        this.currentWallpaperBitmap = bitmap;
        this.rotatePreview = z;
        if (obj instanceof WallpapersListActivity.ColorWallpaper) {
            WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper) obj;
            this.isMotion = colorWallpaper.motion;
            TLRPC$TL_wallPaper tLRPC$TL_wallPaper = colorWallpaper.pattern;
            this.selectedPattern = tLRPC$TL_wallPaper;
            if (tLRPC$TL_wallPaper != null) {
                float f = colorWallpaper.intensity;
                this.currentIntensity = f;
                if (f < 0.0f && !Theme.getActiveTheme().isDark()) {
                    this.currentIntensity *= -1.0f;
                }
            }
        }
        this.msgOutDrawable.themePreview = true;
        this.msgOutMediaDrawable.themePreview = true;
        this.msgOutDrawableSelected.themePreview = true;
        this.msgOutMediaDrawableSelected.themePreview = true;
    }

    public ThemePreviewActivity(Theme.ThemeInfo themeInfo) {
        this(themeInfo, false, 0, false, false);
    }

    public ThemePreviewActivity(Theme.ThemeInfo themeInfo, boolean z, int i, boolean z2, boolean z3) {
        this.themeDelegate = new ThemeDelegate() { // from class: org.telegram.ui.ThemePreviewActivity.1
            @Override // org.telegram.ui.ThemePreviewActivity.ThemeDelegate, org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public boolean isDark() {
                DayNightSwitchDelegate dayNightSwitchDelegate = ThemePreviewActivity.this.onSwitchDayNightDelegate;
                if (dayNightSwitchDelegate != null) {
                    return dayNightSwitchDelegate.isDark();
                }
                return super.isDark();
            }
        };
        this.useDefaultThemeForButtons = true;
        this.colorType = 1;
        this.msgOutDrawable = new MessageDrawable(0, true, false);
        this.msgOutDrawableSelected = new MessageDrawable(0, true, true);
        this.msgOutMediaDrawable = new MessageDrawable(1, true, false);
        this.msgOutMediaDrawableSelected = new MessageDrawable(1, true, true);
        this.lastPickedColorNum = -1;
        this.applyColorAction = new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                ThemePreviewActivity.this.lambda$new$0();
            }
        };
        this.backgroundImages = new BackgroundView[2];
        this.patternLayout = new FrameLayout[2];
        this.patternsCancelButton = new TextView[2];
        this.patternsSaveButton = new TextView[2];
        this.patternsButtonsContainer = new FrameLayout[2];
        this.patternsDict = new HashMap<>();
        this.currentIntensity = 0.5f;
        this.dimAmount = 0.0f;
        this.blendMode = PorterDuff.Mode.SRC_IN;
        this.parallaxScale = 1.0f;
        this.imageFilter = "640_360";
        this.maxWallpaperSize = 1920;
        this.self = true;
        this.gestureDetector2 = new GestureDetector2(getContext(), new GestureDetector2.OnGestureListener() { // from class: org.telegram.ui.ThemePreviewActivity.2
            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public void onShowPress(MotionEvent motionEvent) {
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public void onUp(MotionEvent motionEvent) {
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                if (ThemePreviewActivity.this.scroller != null) {
                    ThemePreviewActivity.this.scroller.abortAnimation();
                    return true;
                }
                return true;
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (ThemePreviewActivity.this.scroller != null) {
                    ThemePreviewActivity.this.scroller.abortAnimation();
                }
                ThemePreviewActivity themePreviewActivity = ThemePreviewActivity.this;
                themePreviewActivity.currentScrollOffset = Utilities.clamp(themePreviewActivity.currentScrollOffset + f, themePreviewActivity.maxScrollOffset, 0.0f);
                ThemePreviewActivity.this.invalidateBlur();
                ThemePreviewActivity.this.backgroundImage.invalidate();
                return true;
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (ThemePreviewActivity.this.scroller != null) {
                    ThemePreviewActivity.this.scroller.abortAnimation();
                    ThemePreviewActivity.this.scroller.fling((int) ThemePreviewActivity.this.currentScrollOffset, 0, Math.round(-f), Math.round(f2), 0, (int) ThemePreviewActivity.this.maxScrollOffset, 0, ConnectionsManager.DEFAULT_DATACENTER_ID);
                    ThemePreviewActivity.this.backgroundImage.postInvalidate();
                    return true;
                }
                return true;
            }
        });
        this.checkingBoostsLevel = false;
        this.checkedBoostsLevel = false;
        this.screenType = i;
        this.nightTheme = z3;
        this.applyingTheme = themeInfo;
        this.deleteOnCancel = z;
        this.editingTheme = z2;
        if (i == 1) {
            Theme.ThemeAccent accent = themeInfo.getAccent(!z2);
            this.accent = accent;
            if (accent != null) {
                this.useDefaultThemeForButtons = false;
                this.backupAccentColor = accent.accentColor;
                this.backupAccentColor2 = accent.accentColor2;
                this.backupMyMessagesAccentColor = accent.myMessagesAccentColor;
                this.backupMyMessagesGradientAccentColor1 = accent.myMessagesGradientAccentColor1;
                this.backupMyMessagesGradientAccentColor2 = accent.myMessagesGradientAccentColor2;
                this.backupMyMessagesGradientAccentColor3 = accent.myMessagesGradientAccentColor3;
                this.backupMyMessagesAnimated = accent.myMessagesAnimated;
                this.backupBackgroundOverrideColor = accent.backgroundOverrideColor;
                this.backupBackgroundGradientOverrideColor1 = accent.backgroundGradientOverrideColor1;
                this.backupBackgroundGradientOverrideColor2 = accent.backgroundGradientOverrideColor2;
                this.backupBackgroundGradientOverrideColor3 = accent.backgroundGradientOverrideColor3;
                this.backupIntensity = accent.patternIntensity;
                this.backupSlug = accent.patternSlug;
                this.backupBackgroundRotation = accent.backgroundRotation;
            }
        } else {
            if (i == 0) {
                this.useDefaultThemeForButtons = false;
            }
            Theme.ThemeAccent accent2 = themeInfo.getAccent(false);
            this.accent = accent2;
            if (accent2 != null) {
                this.selectedPattern = accent2.pattern;
            }
        }
        Theme.ThemeAccent themeAccent = this.accent;
        if (themeAccent != null) {
            this.isMotion = themeAccent.patternMotion;
            if (!TextUtils.isEmpty(themeAccent.patternSlug)) {
                this.currentIntensity = this.accent.patternIntensity;
            }
            Theme.applyThemeTemporary(this.applyingTheme, true);
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.goingToPreviewTheme, new Object[0]);
        this.msgOutDrawable.themePreview = true;
        this.msgOutMediaDrawable.themePreview = true;
        this.msgOutDrawableSelected.themePreview = true;
        this.msgOutMediaDrawableSelected.themePreview = true;
    }

    public void setInitialModes(boolean z, boolean z2, float f) {
        this.isBlurred = z;
        this.isMotion = z2;
        this.dimAmount = f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:102:0x036e, code lost:
        if ("d".equals(((org.telegram.ui.WallpapersListActivity.ColorWallpaper) r1).slug) == false) goto L411;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0374, code lost:
        if ((r37.currentWallpaper instanceof org.telegram.tgnet.TLRPC$TL_wallPaper) != false) goto L411;
     */
    /* JADX WARN: Removed duplicated region for block: B:138:0x05a4  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x05d1  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0603  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0605  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x060d  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x0633  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x066d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0675  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x086e  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x08a7  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x098e  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x0992  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x0a7f  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x0bb6  */
    /* JADX WARN: Removed duplicated region for block: B:348:0x0bc2  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0112  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:412:0x0f6c  */
    /* JADX WARN: Removed duplicated region for block: B:415:0x0f77  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:420:0x0fdf  */
    /* JADX WARN: Removed duplicated region for block: B:421:0x0fe4  */
    /* JADX WARN: Removed duplicated region for block: B:424:0x101a  */
    /* JADX WARN: Removed duplicated region for block: B:427:0x114d  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0123  */
    /* JADX WARN: Removed duplicated region for block: B:436:0x1176  */
    /* JADX WARN: Removed duplicated region for block: B:439:0x1188  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0167  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x01b6  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x021f  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0224  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x022c  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x022f  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0237  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0239  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x023e  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0241  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0247  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x024a  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x027b  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0297 A[LOOP:0: B:73:0x0295->B:74:0x0297, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x02d8  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02f3  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x030d  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    @SuppressLint({"Recycle"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        Object obj;
        final int i8;
        FrameLayout.LayoutParams createFrame;
        float f;
        int i9;
        int i10;
        final int i11;
        Theme.ThemeAccent themeAccent;
        INavigationLayout iNavigationLayout;
        Theme.ThemeAccent themeAccent2;
        this.msgOutDrawable.setResourceProvider(getResourceProvider());
        this.msgOutDrawableSelected.setResourceProvider(getResourceProvider());
        this.msgOutMediaDrawable.setResourceProvider(getResourceProvider());
        this.msgOutMediaDrawableSelected.setResourceProvider(getResourceProvider());
        this.hasOwnBackground = true;
        DayNightSwitchDelegate dayNightSwitchDelegate = this.onSwitchDayNightDelegate;
        boolean z2 = (dayNightSwitchDelegate == null || this.dialogId == 0) ? false : true;
        this.shouldShowDayNightIcon = z2;
        if (z2) {
            Object obj2 = this.currentWallpaper;
            if ((obj2 instanceof WallpapersListActivity.FileWallpaper) || ((obj2 instanceof TLRPC$TL_wallPaper) && ((TLRPC$TL_wallPaper) obj2).document != null && !((TLRPC$TL_wallPaper) obj2).pattern)) {
                z = true;
                this.shouldShowBrightnessControll = z;
                if (z) {
                    this.progressToDarkTheme = dayNightSwitchDelegate.isDark() ? 1.0f : 0.0f;
                }
                if (AndroidUtilities.isTablet()) {
                    this.actionBar.setOccupyStatusBar(false);
                }
                this.page1 = new FrameLayout(context);
                if (this.shouldShowBrightnessControll && SharedConfig.dayNightWallpaperSwitchHint < 3) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda20
                        @Override // java.lang.Runnable
                        public final void run() {
                            ThemePreviewActivity.this.lambda$createView$2();
                        }
                    }, 2000L);
                }
                this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener(this) { // from class: org.telegram.ui.ThemePreviewActivity.5
                    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                    public boolean canCollapseSearch() {
                        return true;
                    }

                    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                    public void onSearchCollapse() {
                    }

                    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                    public void onSearchExpand() {
                    }

                    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
                    public void onTextChanged(EditText editText) {
                    }
                }).setSearchFieldHint(LocaleController.getString("Search", R.string.Search));
                this.actionBar.setBackButtonDrawable(new MenuDrawable());
                this.actionBar.setAddToContainer(false);
                this.actionBar.setTitle(LocaleController.getString("ThemePreview", R.string.ThemePreview));
                FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.6
                    @Override // android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i12, int i13) {
                        int size = View.MeasureSpec.getSize(i12);
                        int size2 = View.MeasureSpec.getSize(i13);
                        setMeasuredDimension(size, size2);
                        measureChildWithMargins(((BaseFragment) ThemePreviewActivity.this).actionBar, i12, 0, i13, 0);
                        int measuredHeight = ((BaseFragment) ThemePreviewActivity.this).actionBar.getMeasuredHeight();
                        if (((BaseFragment) ThemePreviewActivity.this).actionBar.getVisibility() == 0) {
                            size2 -= measuredHeight;
                        }
                        ((FrameLayout.LayoutParams) ThemePreviewActivity.this.listView.getLayoutParams()).topMargin = measuredHeight;
                        ThemePreviewActivity.this.listView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                        measureChildWithMargins(ThemePreviewActivity.this.floatingButton, i12, 0, i13, 0);
                    }

                    @Override // android.view.ViewGroup
                    protected boolean drawChild(Canvas canvas, View view, long j) {
                        boolean drawChild = super.drawChild(canvas, view, j);
                        if (view == ((BaseFragment) ThemePreviewActivity.this).actionBar && ((BaseFragment) ThemePreviewActivity.this).parentLayout != null) {
                            ((BaseFragment) ThemePreviewActivity.this).parentLayout.drawHeaderShadow(canvas, ((BaseFragment) ThemePreviewActivity.this).actionBar.getVisibility() == 0 ? ((BaseFragment) ThemePreviewActivity.this).actionBar.getMeasuredHeight() : 0);
                        }
                        return drawChild;
                    }
                };
                this.page1 = frameLayout;
                frameLayout.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                this.page1.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
                RecyclerListView recyclerListView = new RecyclerListView(context);
                this.listView = recyclerListView;
                recyclerListView.setVerticalScrollBarEnabled(true);
                this.listView.setItemAnimator(null);
                this.listView.setLayoutAnimation(null);
                this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
                this.listView.setVerticalScrollbarPosition(!LocaleController.isRTL ? 1 : 2);
                this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(this.screenType == 0 ? 12.0f : 0.0f));
                this.listView.setOnItemClickListener(ThemePreviewActivity$$ExternalSyntheticLambda34.INSTANCE);
                this.page1.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
                ImageView imageView = new ImageView(context);
                this.floatingButton = imageView;
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), getThemedColor(Theme.key_chats_actionBackground), getThemedColor(Theme.key_chats_actionPressedBackground));
                i = Build.VERSION.SDK_INT;
                if (i < 21) {
                    Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
                    mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                    CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
                    combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    createSimpleSelectorCircleDrawable = combinedDrawable;
                }
                this.floatingButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable);
                this.floatingButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
                this.floatingButton.setImageResource(R.drawable.floating_pencil);
                if (i >= 21) {
                    StateListAnimator stateListAnimator = new StateListAnimator();
                    ImageView imageView2 = this.floatingButton;
                    Property property = View.TRANSLATION_Z;
                    stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(imageView2, property, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                    stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButton, property, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                    this.floatingButton.setStateListAnimator(stateListAnimator);
                    this.floatingButton.setOutlineProvider(new ViewOutlineProvider(this) { // from class: org.telegram.ui.ThemePreviewActivity.7
                        @Override // android.view.ViewOutlineProvider
                        @SuppressLint({"NewApi"})
                        public void getOutline(View view, Outline outline) {
                            outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                        }
                    });
                }
                FrameLayout frameLayout2 = this.page1;
                ImageView imageView3 = this.floatingButton;
                int i12 = i < 21 ? 56 : 60;
                float f2 = i < 21 ? 56.0f : 60.0f;
                boolean z3 = LocaleController.isRTL;
                frameLayout2.addView(imageView3, LayoutHelper.createFrame(i12, f2, (!z3 ? 3 : 5) | 80, !z3 ? 14.0f : 0.0f, 0.0f, !z3 ? 0.0f : 14.0f, 14.0f));
                DialogsAdapter dialogsAdapter = new DialogsAdapter(context);
                this.dialogsAdapter = dialogsAdapter;
                this.listView.setAdapter(dialogsAdapter);
                this.page2 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.8
                    private boolean ignoreLayout;

                    @Override // android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i13, int i14) {
                        float f3;
                        int size = View.MeasureSpec.getSize(i13);
                        int size2 = View.MeasureSpec.getSize(i14);
                        setMeasuredDimension(size, size2);
                        if (ThemePreviewActivity.this.dropDownContainer != null) {
                            this.ignoreLayout = true;
                            if (!AndroidUtilities.isTablet()) {
                                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ThemePreviewActivity.this.dropDownContainer.getLayoutParams();
                                layoutParams.topMargin = Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0;
                                ThemePreviewActivity.this.dropDownContainer.setLayoutParams(layoutParams);
                            }
                            if (AndroidUtilities.isTablet() || ApplicationLoader.applicationContext.getResources().getConfiguration().orientation != 2) {
                                ThemePreviewActivity.this.dropDown.setTextSize(1, 20.0f);
                            } else {
                                ThemePreviewActivity.this.dropDown.setTextSize(1, 18.0f);
                            }
                            this.ignoreLayout = false;
                        }
                        measureChildWithMargins(ThemePreviewActivity.this.actionBar2, i13, 0, i14, 0);
                        int measuredHeight = ThemePreviewActivity.this.actionBar2.getMeasuredHeight();
                        if (ThemePreviewActivity.this.actionBar2.getVisibility() == 0) {
                            size2 -= measuredHeight;
                        }
                        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.listView2.getLayoutParams();
                        layoutParams2.topMargin = measuredHeight;
                        int i15 = 58;
                        if (ThemePreviewActivity.this.screenType == 2) {
                            RecyclerListView recyclerListView2 = ThemePreviewActivity.this.listView2;
                            int dp = AndroidUtilities.dp(4.0f);
                            ThemePreviewActivity themePreviewActivity = ThemePreviewActivity.this;
                            recyclerListView2.setPadding(0, dp, 0, (AndroidUtilities.dp(((themePreviewActivity.self || themePreviewActivity.dialogId <= 0) ? 0 : 58) + 72) - 12) + (ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0));
                        }
                        ThemePreviewActivity.this.listView2.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - layoutParams2.bottomMargin, 1073741824));
                        ((FrameLayout.LayoutParams) ThemePreviewActivity.this.backgroundImage.getLayoutParams()).topMargin = measuredHeight;
                        ThemePreviewActivity.this.backgroundImage.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                        if (ThemePreviewActivity.this.dimmingSliderContainer != null) {
                            ((FrameLayout.LayoutParams) ThemePreviewActivity.this.dimmingSliderContainer.getLayoutParams()).topMargin = measuredHeight;
                            ThemePreviewActivity.this.dimmingSliderContainer.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(222.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(76.0f), 1073741824));
                        }
                        if (ThemePreviewActivity.this.bottomOverlayChat != null) {
                            ThemePreviewActivity.this.bottomOverlayChat.setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f) + (ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0));
                            FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.bottomOverlayChat.getLayoutParams();
                            ThemePreviewActivity themePreviewActivity2 = ThemePreviewActivity.this;
                            layoutParams3.height = AndroidUtilities.dp(72 + ((themePreviewActivity2.self || themePreviewActivity2.dialogId <= 0) ? 0 : 0)) + (ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0);
                            measureChildWithMargins(ThemePreviewActivity.this.bottomOverlayChat, i13, 0, i14, 0);
                        }
                        if (ThemePreviewActivity.this.sheetDrawable != null) {
                            ThemePreviewActivity.this.sheetDrawable.getPadding(AndroidUtilities.rectTmp2);
                        }
                        int i16 = 0;
                        while (i16 < ThemePreviewActivity.this.patternLayout.length) {
                            if (ThemePreviewActivity.this.patternLayout[i16] != null) {
                                FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.patternLayout[i16].getLayoutParams();
                                if (i16 == 0) {
                                    f3 = ThemePreviewActivity.this.screenType == 2 ? 321 : 273;
                                } else {
                                    f3 = 316.0f;
                                }
                                layoutParams4.height = AndroidUtilities.dp(f3);
                                if (ThemePreviewActivity.this.insideBottomSheet()) {
                                    layoutParams4.height += AndroidUtilities.navigationBarHeight;
                                }
                                if (i16 == 0) {
                                    layoutParams4.height += AndroidUtilities.dp(12.0f) + AndroidUtilities.rectTmp2.top;
                                }
                                ThemePreviewActivity.this.patternLayout[i16].setPadding(0, i16 == 0 ? AndroidUtilities.dp(12.0f) + AndroidUtilities.rectTmp2.top : 0, 0, ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0);
                                measureChildWithMargins(ThemePreviewActivity.this.patternLayout[i16], i13, 0, i14, 0);
                            }
                            i16++;
                        }
                    }

                    @Override // android.view.ViewGroup
                    protected boolean drawChild(Canvas canvas, View view, long j) {
                        boolean drawChild = super.drawChild(canvas, view, j);
                        if (view == ThemePreviewActivity.this.actionBar2 && ((BaseFragment) ThemePreviewActivity.this).parentLayout != null) {
                            ((BaseFragment) ThemePreviewActivity.this).parentLayout.drawHeaderShadow(canvas, ThemePreviewActivity.this.actionBar2.getVisibility() == 0 ? (int) (ThemePreviewActivity.this.actionBar2.getMeasuredHeight() + ThemePreviewActivity.this.actionBar2.getTranslationY()) : 0);
                        }
                        return drawChild;
                    }

                    @Override // android.view.View, android.view.ViewParent
                    public void requestLayout() {
                        if (this.ignoreLayout) {
                            return;
                        }
                        super.requestLayout();
                    }
                };
                this.messagesAdapter = new MessagesAdapter(context);
                this.actionBar2 = createActionBar(context);
                if (AndroidUtilities.isTablet()) {
                    this.actionBar2.setOccupyStatusBar(false);
                }
                this.actionBar2.setBackButtonDrawable(new BackDrawable(false));
                this.actionBar2.setActionBarMenuOnItemClick(new 9());
                for (i2 = 0; i2 < 2; i2++) {
                    this.backgroundImages[i2] = new BackgroundView(getContext());
                    this.page2.addView(this.backgroundImages[i2], LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
                }
                BackgroundView backgroundView = this.backgroundImages[0];
                this.backgroundImage = backgroundView;
                backgroundView.setVisibility(0);
                this.backgroundImages[1].setVisibility(8);
                if (this.screenType == 2) {
                    this.backgroundImage.getImageReceiver().setDelegate(new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda28
                        @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                        public final void didSetImage(ImageReceiver imageReceiver, boolean z4, boolean z5, boolean z6) {
                            ThemePreviewActivity.this.lambda$createView$4(imageReceiver, z4, z5, z6);
                        }

                        @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                        public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
                            ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver);
                        }
                    });
                }
                if (!this.messagesAdapter.showSecretMessages) {
                    this.actionBar2.setTitle("Telegram Beta Chat");
                    this.actionBar2.setSubtitle(LocaleController.formatPluralString("Members", 505, new Object[0]));
                } else {
                    int i13 = this.screenType;
                    if (i13 == 2) {
                        if (this.dialogId != 0) {
                            this.actionBar2.setTitle(LocaleController.getString("WallpaperPreview", R.string.WallpaperPreview));
                        } else {
                            this.actionBar2.setTitle(LocaleController.getString("BackgroundPreview", R.string.BackgroundPreview));
                        }
                        ActionBarMenu createMenu = this.actionBar2.createMenu();
                        Object obj3 = this.currentWallpaper;
                        if ((obj3 instanceof WallpapersListActivity.FileWallpaper) && ((WallpapersListActivity.FileWallpaper) obj3).originalPath != null) {
                            createMenu.addItem(7, R.drawable.msg_header_draw);
                        }
                        if (this.dialogId == 0) {
                            if (!BuildVars.DEBUG_PRIVATE_VERSION || Theme.getActiveTheme().getAccent(false) == null) {
                                Object obj4 = this.currentWallpaper;
                                if (obj4 instanceof WallpapersListActivity.ColorWallpaper) {
                                }
                            }
                            createMenu.addItem(5, R.drawable.msg_header_share);
                        }
                        if (this.dialogId != 0 && this.shouldShowDayNightIcon) {
                            int i14 = R.raw.sun;
                            RLottieDrawable rLottieDrawable = new RLottieDrawable(i14, "" + i14, AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
                            this.sunDrawable = rLottieDrawable;
                            this.dayNightItem = createMenu.addItem(6, rLottieDrawable);
                            this.sunDrawable.setPlayInDirectionOfCustomEndFrame(true);
                            DayNightSwitchDelegate dayNightSwitchDelegate2 = this.onSwitchDayNightDelegate;
                            if (dayNightSwitchDelegate2 != null && !dayNightSwitchDelegate2.isDark()) {
                                this.sunDrawable.setCustomEndFrame(0);
                                this.sunDrawable.setCurrentFrame(0);
                            } else {
                                this.sunDrawable.setCurrentFrame(35);
                                this.sunDrawable.setCustomEndFrame(36);
                            }
                            this.sunDrawable.beginApplyLayerColors();
                            int color = Theme.getColor(Theme.key_chats_menuName);
                            this.sunDrawable.setLayerColor("Sunny.**", color);
                            this.sunDrawable.setLayerColor("Path 6.**", color);
                            this.sunDrawable.setLayerColor("Path.**", color);
                            this.sunDrawable.setLayerColor("Path 5.**", color);
                            this.sunDrawable.commitApplyLayerColors();
                        }
                    } else {
                        if (i13 == 1) {
                            ActionBarMenu createMenu2 = this.actionBar2.createMenu();
                            this.saveItem = createMenu2.addItem(4, LocaleController.getString("Save", R.string.Save).toUpperCase());
                            i3 = 2;
                            ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, createMenu2, 0, 0) { // from class: org.telegram.ui.ThemePreviewActivity.10
                                @Override // org.telegram.ui.ActionBar.ActionBarMenuItem, android.view.View
                                public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                                    super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                                    accessibilityNodeInfo.setText(ThemePreviewActivity.this.dropDown.getText());
                                }
                            };
                            this.dropDownContainer = actionBarMenuItem;
                            actionBarMenuItem.setSubMenuOpenSide(1);
                            this.dropDownContainer.addSubItem(2, LocaleController.getString("ColorPickerBackground", R.string.ColorPickerBackground));
                            ActionBarMenuItem actionBarMenuItem2 = this.dropDownContainer;
                            int i15 = R.string.ColorPickerMainColor;
                            actionBarMenuItem2.addSubItem(1, LocaleController.getString("ColorPickerMainColor", i15));
                            this.dropDownContainer.addSubItem(3, LocaleController.getString("ColorPickerMyMessages", R.string.ColorPickerMyMessages));
                            this.dropDownContainer.setAllowCloseAnimation(false);
                            this.dropDownContainer.setForceSmoothKeyboard(true);
                            this.actionBar2.addView(this.dropDownContainer, LayoutHelper.createFrame(-2, -1.0f, 51, AndroidUtilities.isTablet() ? 64.0f : 56.0f, 0.0f, 40.0f, 0.0f));
                            this.dropDownContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda6
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    ThemePreviewActivity.this.lambda$createView$5(view);
                                }
                            });
                            TextView textView = new TextView(context);
                            this.dropDown = textView;
                            textView.setImportantForAccessibility(2);
                            this.dropDown.setGravity(3);
                            this.dropDown.setSingleLine(true);
                            this.dropDown.setLines(1);
                            this.dropDown.setMaxLines(1);
                            this.dropDown.setEllipsize(TextUtils.TruncateAt.END);
                            TextView textView2 = this.dropDown;
                            int i16 = Theme.key_actionBarDefaultTitle;
                            textView2.setTextColor(getThemedColor(i16));
                            this.dropDown.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                            this.dropDown.setText(LocaleController.getString("ColorPickerMainColor", i15));
                            Drawable mutate2 = context.getResources().getDrawable(R.drawable.ic_arrow_drop_down).mutate();
                            mutate2.setColorFilter(new PorterDuffColorFilter(getThemedColor(i16), PorterDuff.Mode.MULTIPLY));
                            this.dropDown.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, mutate2, (Drawable) null);
                            this.dropDown.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
                            this.dropDown.setPadding(0, 0, AndroidUtilities.dp(10.0f), 0);
                            this.dropDownContainer.addView(this.dropDown, LayoutHelper.createFrame(-2, -2.0f, 16, 16.0f, 0.0f, 0.0f, 1.0f));
                        } else {
                            i3 = 2;
                            Theme.ThemeInfo themeInfo = this.applyingTheme;
                            TLRPC$TL_theme tLRPC$TL_theme = themeInfo.info;
                            String name = tLRPC$TL_theme != null ? tLRPC$TL_theme.title : themeInfo.getName();
                            int lastIndexOf = name.lastIndexOf(".attheme");
                            if (lastIndexOf >= 0) {
                                name = name.substring(0, lastIndexOf);
                            }
                            this.actionBar2.setTitle(name);
                            TLRPC$TL_theme tLRPC$TL_theme2 = this.applyingTheme.info;
                            if (tLRPC$TL_theme2 != null && (i4 = tLRPC$TL_theme2.installs_count) > 0) {
                                this.actionBar2.setSubtitle(LocaleController.formatPluralString("ThemeInstallCount", i4, new Object[0]));
                            } else {
                                this.actionBar2.setSubtitle(LocaleController.formatDateOnline((System.currentTimeMillis() / 1000) - 3600, null));
                            }
                        }
                        this.listView2 = new RecyclerListView(context) { // from class: org.telegram.ui.ThemePreviewActivity.11
                            boolean scrollingBackground;
                            float startX;

                            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                            public boolean drawChild(Canvas canvas, View view, long j) {
                                RecyclerView.ViewHolder childViewHolder;
                                boolean drawChild = super.drawChild(canvas, view, j);
                                if (view instanceof ChatMessageCell) {
                                    ChatMessageCell chatMessageCell = (ChatMessageCell) view;
                                    chatMessageCell.getMessageObject();
                                    ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                                    if (avatarImage != null) {
                                        int top = view.getTop();
                                        if (chatMessageCell.isPinnedBottom() && (childViewHolder = ThemePreviewActivity.this.listView2.getChildViewHolder(view)) != null) {
                                            if (ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder.getAdapterPosition() - 1) != null) {
                                                avatarImage.setImageY(-AndroidUtilities.dp(1000.0f));
                                                avatarImage.draw(canvas);
                                                return drawChild;
                                            }
                                        }
                                        float translationX = chatMessageCell.getTranslationX();
                                        int top2 = view.getTop() + chatMessageCell.getLayoutHeight();
                                        int measuredHeight = ThemePreviewActivity.this.listView2.getMeasuredHeight() - ThemePreviewActivity.this.listView2.getPaddingBottom();
                                        if (top2 > measuredHeight) {
                                            top2 = measuredHeight;
                                        }
                                        if (chatMessageCell.isPinnedTop() && (r9 = ThemePreviewActivity.this.listView2.getChildViewHolder(view)) != null) {
                                            int i17 = 0;
                                            while (i17 < 20) {
                                                i17++;
                                                RecyclerView.ViewHolder childViewHolder2 = ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder2.getAdapterPosition() + 1);
                                                if (childViewHolder2 == null) {
                                                    break;
                                                }
                                                top = childViewHolder2.itemView.getTop();
                                                if (top2 - AndroidUtilities.dp(48.0f) < childViewHolder2.itemView.getBottom()) {
                                                    translationX = Math.min(childViewHolder2.itemView.getTranslationX(), translationX);
                                                }
                                                View view2 = childViewHolder2.itemView;
                                                if (!(view2 instanceof ChatMessageCell)) {
                                                    break;
                                                } else if (!((ChatMessageCell) view2).isPinnedTop()) {
                                                    break;
                                                }
                                            }
                                        }
                                        if (top2 - AndroidUtilities.dp(48.0f) < top) {
                                            top2 = top + AndroidUtilities.dp(48.0f);
                                        }
                                        if (translationX != 0.0f) {
                                            canvas.save();
                                            canvas.translate(translationX, 0.0f);
                                        }
                                        avatarImage.setImageY(top2 - AndroidUtilities.dp(44.0f));
                                        avatarImage.draw(canvas);
                                        if (translationX != 0.0f) {
                                            canvas.restore();
                                        }
                                    }
                                }
                                return drawChild;
                            }

                            @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
                            public void setTranslationY(float f3) {
                                super.setTranslationY(f3);
                                if (ThemePreviewActivity.this.backgroundCheckBoxView != null) {
                                    for (int i17 = 0; i17 < ThemePreviewActivity.this.backgroundCheckBoxView.length; i17++) {
                                        ThemePreviewActivity.this.backgroundCheckBoxView[i17].invalidate();
                                    }
                                }
                                if (ThemePreviewActivity.this.messagesCheckBoxView != null) {
                                    for (int i18 = 0; i18 < ThemePreviewActivity.this.messagesCheckBoxView.length; i18++) {
                                        ThemePreviewActivity.this.messagesCheckBoxView[i18].invalidate();
                                    }
                                }
                                if (ThemePreviewActivity.this.backgroundPlayAnimationView != null) {
                                    ThemePreviewActivity.this.backgroundPlayAnimationView.invalidate();
                                }
                                if (ThemePreviewActivity.this.messagesPlayAnimationView != null) {
                                    ThemePreviewActivity.this.messagesPlayAnimationView.invalidate();
                                }
                            }

                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // org.telegram.ui.Components.RecyclerListView
                            public void onChildPressed(View view, float f3, float f4, boolean z4) {
                                if (z4 && (view instanceof ChatMessageCell) && !((ChatMessageCell) view).isInsideBackground(f3, f4)) {
                                    return;
                                }
                                super.onChildPressed(view, f3, f4, z4);
                            }

                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // org.telegram.ui.Components.RecyclerListView
                            public boolean allowSelectChildAtPosition(View view) {
                                RecyclerView.ViewHolder findContainingViewHolder = ThemePreviewActivity.this.listView2.findContainingViewHolder(view);
                                if (findContainingViewHolder == null || findContainingViewHolder.getItemViewType() != 2) {
                                    return super.allowSelectChildAtPosition(view);
                                }
                                return false;
                            }

                            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
                            public boolean onTouchEvent(MotionEvent motionEvent) {
                                checkMotionEvent(motionEvent);
                                if (ThemePreviewActivity.this.hasScrollingBackground) {
                                    if (motionEvent.getAction() == 0) {
                                        this.startX = motionEvent.getX();
                                        motionEvent.getY();
                                        if (getParent() != null) {
                                            getParent().requestDisallowInterceptTouchEvent(true);
                                        }
                                        this.scrollingBackground = true;
                                    } else if (motionEvent.getAction() == 2) {
                                        if (!this.scrollingBackground && Math.abs(this.startX - motionEvent.getX()) > AndroidUtilities.touchSlop) {
                                            if (getParent() != null) {
                                                getParent().requestDisallowInterceptTouchEvent(true);
                                            }
                                            this.scrollingBackground = true;
                                        }
                                    } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                                        this.scrollingBackground = false;
                                        if (getParent() != null) {
                                            getParent().requestDisallowInterceptTouchEvent(false);
                                        }
                                    }
                                    ThemePreviewActivity.this.gestureDetector2.onTouchEvent(motionEvent);
                                }
                                return this.scrollingBackground || super.onTouchEvent(motionEvent);
                            }

                            private void checkMotionEvent(MotionEvent motionEvent) {
                                if (motionEvent.getAction() == 1) {
                                    if (!ThemePreviewActivity.this.wasScroll && (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) && ThemePreviewActivity.this.patternLayout[0].getVisibility() == 0) {
                                        ThemePreviewActivity.this.showPatternsView(0, false, true);
                                    }
                                    ThemePreviewActivity.this.wasScroll = false;
                                }
                            }

                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
                            public void onLayout(boolean z4, int i17, int i18, int i19, int i20) {
                                super.onLayout(z4, i17, i18, i19, i20);
                                ThemePreviewActivity.this.invalidateBlur();
                            }
                        };
                        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator() { // from class: org.telegram.ui.ThemePreviewActivity.12
                            /* JADX INFO: Access modifiers changed from: protected */
                            @Override // androidx.recyclerview.widget.DefaultItemAnimator
                            public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                                ThemePreviewActivity.this.listView2.invalidateViews();
                            }
                        };
                        defaultItemAnimator.setDelayAnimations(false);
                        this.listView2.setItemAnimator(defaultItemAnimator);
                        this.listView2.setVerticalScrollBarEnabled(true);
                        this.listView2.setOverScrollMode(i3);
                        i5 = this.screenType;
                        if (i5 == i3) {
                            this.listView2.setPadding(0, AndroidUtilities.dp(4.0f), 0, (AndroidUtilities.dp(72 + ((this.self || this.dialogId <= 0) ? 0 : 58)) - 12) + (insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0));
                        } else if (i5 == 1) {
                            this.listView2.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(16.0f));
                        } else {
                            this.listView2.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
                        }
                        this.listView2.setClipToPadding(false);
                        this.listView2.setLayoutManager(new LinearLayoutManager(context, 1, true));
                        this.listView2.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
                        if (this.screenType == 1) {
                            this.page2.addView(this.listView2, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 273.0f));
                            this.listView2.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda35
                                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                                public /* synthetic */ boolean hasDoubleTap(View view, int i17) {
                                    return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view, i17);
                                }

                                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                                public /* synthetic */ void onDoubleTap(View view, int i17, float f3, float f4) {
                                    RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view, i17, f3, f4);
                                }

                                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                                public final void onItemClick(View view, int i17, float f3, float f4) {
                                    ThemePreviewActivity.this.lambda$createView$6(view, i17, f3, f4);
                                }
                            });
                            i6 = -1;
                        } else {
                            i6 = -1;
                            this.page2.addView(this.listView2, LayoutHelper.createFrame(-1, -1, 51));
                        }
                        this.listView2.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ThemePreviewActivity.13
                            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                            public void onScrolled(RecyclerView recyclerView, int i17, int i18) {
                                ThemePreviewActivity.this.listView2.invalidateViews();
                                ThemePreviewActivity.this.wasScroll = true;
                            }

                            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                            public void onScrollStateChanged(RecyclerView recyclerView, int i17) {
                                if (i17 == 0) {
                                    ThemePreviewActivity.this.wasScroll = false;
                                }
                            }
                        });
                        this.page2.addView(this.actionBar2, LayoutHelper.createFrame(i6, -2.0f));
                        WallpaperParallaxEffect wallpaperParallaxEffect = new WallpaperParallaxEffect(context);
                        this.parallaxEffect = wallpaperParallaxEffect;
                        wallpaperParallaxEffect.setCallback(new WallpaperParallaxEffect.Callback() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda36
                            @Override // org.telegram.ui.Components.WallpaperParallaxEffect.Callback
                            public final void onOffsetsChanged(int i17, int i18, float f3) {
                                ThemePreviewActivity.this.lambda$createView$7(i17, i18, f3);
                            }
                        });
                        i7 = this.screenType;
                        if (i7 != 1 || i7 == i3) {
                            if (i7 == i3) {
                                final boolean insideBottomSheet = insideBottomSheet();
                                FrameLayout frameLayout3 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.14
                                    private final ColorFilter colorFilter;
                                    private LinearGradient gradient;
                                    private int gradientHeight;
                                    private final Paint gradientPaint;

                                    {
                                        Paint paint = new Paint(3);
                                        this.gradientPaint = paint;
                                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                                        ColorMatrix colorMatrix = new ColorMatrix();
                                        AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, 0.4f);
                                        AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, 0.65f);
                                        this.colorFilter = new ColorMatrixColorFilter(colorMatrix);
                                    }

                                    @Override // android.view.ViewGroup, android.view.View
                                    protected void dispatchDraw(Canvas canvas) {
                                        if (insideBottomSheet) {
                                            RectF rectF = AndroidUtilities.rectTmp;
                                            rectF.set(0.0f, 0.0f, getWidth(), getHeight());
                                            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), 255, 31);
                                            Theme.applyServiceShaderMatrixForView(this, ThemePreviewActivity.this.backgroundImage, ThemePreviewActivity.this.themeDelegate);
                                            Paint paint = ThemePreviewActivity.this.themeDelegate.getPaint("paintChatActionBackground");
                                            ColorFilter colorFilter = paint.getColorFilter();
                                            paint.setColorFilter(this.colorFilter);
                                            float f3 = 1.0f;
                                            if (ThemePreviewActivity.this.backgroundImage != null && (ThemePreviewActivity.this.backgroundImage.getBackground() instanceof MotionBackgroundDrawable) && ThemePreviewActivity.this.currentIntensity < 0.0f) {
                                                f3 = 0.33f;
                                            }
                                            int alpha = paint.getAlpha();
                                            paint.setAlpha((int) (alpha * f3));
                                            canvas.drawRect(rectF, paint);
                                            paint.setAlpha(alpha);
                                            paint.setColorFilter(colorFilter);
                                            if (ThemePreviewActivity.this.shouldShowBrightnessControll && ThemePreviewActivity.this.dimAmount > 0.0f) {
                                                canvas.drawColor(ColorUtils.setAlphaComponent(-16777216, (int) (ThemePreviewActivity.this.dimAmount * 255.0f * ThemePreviewActivity.this.progressToDarkTheme)));
                                            }
                                            canvas.save();
                                            if (this.gradient == null || this.gradientHeight != getHeight()) {
                                                int height = getHeight();
                                                this.gradientHeight = height;
                                                LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, height, new int[]{-1, 0}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                                                this.gradient = linearGradient;
                                                this.gradientPaint.setShader(linearGradient);
                                            }
                                            canvas.drawRect(rectF, this.gradientPaint);
                                            canvas.restore();
                                            canvas.restore();
                                        }
                                        super.dispatchDraw(canvas);
                                    }

                                    @Override // android.widget.FrameLayout, android.view.View
                                    protected void onMeasure(int i17, int i18) {
                                        super.onMeasure(i17, i18);
                                        for (int i19 = 0; i19 < getChildCount(); i19++) {
                                            View childAt = getChildAt(i19);
                                            if (childAt.getMeasuredWidth() > AndroidUtilities.dp(420.0f)) {
                                                childAt.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(420.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getMeasuredHeight(), 1073741824));
                                            }
                                        }
                                    }
                                };
                                this.bottomOverlayChat = frameLayout3;
                                frameLayout3.setWillNotDraw(false);
                                this.bottomOverlayChat.setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f) + (insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0));
                                this.page2.addView(this.bottomOverlayChat, LayoutHelper.createFrame(-1, 0, 81));
                                BlurButton blurButton = new BlurButton(context);
                                this.applyButton1 = blurButton;
                                ScaleStateListAnimator.apply(blurButton, 0.033f, 1.2f);
                                updateApplyButton1(false);
                                this.applyButton1.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda7
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view) {
                                        ThemePreviewActivity.this.lambda$createView$8(view);
                                    }
                                });
                                if (this.dialogId > 0 && !this.self && this.serverWallpaper == null) {
                                    BlurButton blurButton2 = new BlurButton(context);
                                    this.applyButton2 = blurButton2;
                                    ScaleStateListAnimator.apply(blurButton2, 0.033f, 1.2f);
                                    TLRPC$User user = getMessagesController().getUser(Long.valueOf(this.dialogId));
                                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("");
                                    if (!getUserConfig().isPremium()) {
                                        spannableStringBuilder.append((CharSequence) "l ");
                                        spannableStringBuilder.setSpan(new ColoredImageSpan(R.drawable.msg_mini_lock3), 0, 1, 33);
                                    }
                                    spannableStringBuilder.append((CharSequence) LocaleController.formatString(R.string.ApplyWallpaperForMeAndPeer, UserObject.getUserName(user)));
                                    this.applyButton2.setText(spannableStringBuilder);
                                    try {
                                        BlurButton blurButton3 = this.applyButton2;
                                        blurButton3.setText(Emoji.replaceEmoji(blurButton3.getText(), this.applyButton2.text.getFontMetricsInt(), false));
                                    } catch (Exception unused) {
                                    }
                                    this.applyButton2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda8
                                        @Override // android.view.View.OnClickListener
                                        public final void onClick(View view) {
                                            ThemePreviewActivity.this.lambda$createView$9(view);
                                        }
                                    });
                                    this.bottomOverlayChat.addView(this.applyButton1, LayoutHelper.createFrame(-1, 48.0f, 81, 0.0f, 0.0f, 0.0f, 58.0f));
                                    this.bottomOverlayChat.addView(this.applyButton2, LayoutHelper.createFrame(-1, 48.0f, 81, 0.0f, 0.0f, 0.0f, 0.0f));
                                } else {
                                    this.bottomOverlayChat.addView(this.applyButton1, LayoutHelper.createFrame(-1, 48.0f, 81, 0.0f, 0.0f, 0.0f, 0.0f));
                                }
                                if (this.shouldShowBrightnessControll) {
                                    FrameLayout frameLayout4 = new FrameLayout(getContext()) { // from class: org.telegram.ui.ThemePreviewActivity.15
                                        private final Paint shadowPaint = new Paint(1);
                                        private final Paint dimPaint = new Paint(1);
                                        private final Paint dimPaint2 = new Paint(1);

                                        @Override // android.view.ViewGroup, android.view.View
                                        protected void dispatchDraw(Canvas canvas) {
                                            RectF rectF = AndroidUtilities.rectTmp;
                                            rectF.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
                                            float dp = AndroidUtilities.dp(8.0f);
                                            this.shadowPaint.setColor(0);
                                            this.shadowPaint.setShadowLayer(AndroidUtilities.dpf2(1.0f), 0.0f, AndroidUtilities.dpf2(0.33f), ColorUtils.setAlphaComponent(-16777216, (int) (ThemePreviewActivity.this.dimmingSlider.getAlpha() * 27.0f)));
                                            canvas.drawRoundRect(rectF, dp, dp, this.shadowPaint);
                                            Theme.applyServiceShaderMatrixForView(this, ThemePreviewActivity.this.backgroundImage, ThemePreviewActivity.this.themeDelegate);
                                            Paint paint = ThemePreviewActivity.this.themeDelegate.getPaint("paintChatActionBackground");
                                            int alpha = paint.getAlpha();
                                            paint.setAlpha((int) (alpha * ThemePreviewActivity.this.dimmingSlider.getAlpha()));
                                            canvas.drawRoundRect(rectF, dp, dp, paint);
                                            paint.setAlpha(alpha);
                                            if (ThemePreviewActivity.this.shouldShowBrightnessControll && ThemePreviewActivity.this.dimAmount > 0.0f) {
                                                this.dimPaint2.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (ThemePreviewActivity.this.dimAmount * 255.0f * ThemePreviewActivity.this.progressToDarkTheme)));
                                                canvas.drawRoundRect(rectF, dp, dp, this.dimPaint2);
                                            }
                                            this.dimPaint.setColor(520093695);
                                            this.dimPaint.setAlpha((int) (ThemePreviewActivity.this.dimmingSlider.getAlpha() * 30.0f));
                                            canvas.drawRoundRect(rectF, dp, dp, this.dimPaint);
                                            super.dispatchDraw(canvas);
                                        }
                                    };
                                    this.dimmingSliderContainer = frameLayout4;
                                    frameLayout4.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                    this.page2.addView(this.dimmingSliderContainer, LayoutHelper.createFrame(222, 76, 49));
                                    SliderView sliderView = new SliderView(this, getContext(), 3) { // from class: org.telegram.ui.ThemePreviewActivity.16
                                        @Override // org.telegram.ui.Stories.recorder.SliderView, android.view.View
                                        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                                            if (getParent() != null) {
                                                getParent().requestDisallowInterceptTouchEvent(true);
                                            }
                                            return super.dispatchTouchEvent(motionEvent);
                                        }
                                    };
                                    this.dimmingSlider = sliderView;
                                    sliderView.setValue(this.dimAmount);
                                    this.dimmingSlider.setMinMax(0.0f, 0.9f);
                                    this.dimmingSlider.setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda29
                                        @Override // org.telegram.messenger.Utilities.Callback
                                        public final void run(Object obj5) {
                                            ThemePreviewActivity.this.lambda$createView$10((Float) obj5);
                                        }
                                    });
                                    this.dimmingSliderContainer.addView(this.dimmingSlider);
                                    DayNightSwitchDelegate dayNightSwitchDelegate3 = this.onSwitchDayNightDelegate;
                                    if (dayNightSwitchDelegate3 != null) {
                                        this.dimmingSlider.setVisibility(dayNightSwitchDelegate3.isDark() ? 0 : 8);
                                        this.dimmingSlider.setAlpha(this.onSwitchDayNightDelegate.isDark() ? 1.0f : 0.0f);
                                        this.dimmingSlider.setValue(this.onSwitchDayNightDelegate.isDark() ? this.dimAmount : 0.0f);
                                    }
                                }
                            }
                            final Rect rect = new Rect();
                            Drawable mutate3 = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
                            this.sheetDrawable = mutate3;
                            mutate3.getPadding(rect);
                            this.sheetDrawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundWhite), PorterDuff.Mode.MULTIPLY));
                            TextPaint textPaint = new TextPaint(1);
                            textPaint.setTextSize(AndroidUtilities.dp(14.0f));
                            textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                            obj = this.currentWallpaper;
                            if (!(obj instanceof WallpapersListActivity.EmojiWallpaper)) {
                                if (this.screenType == 1 || (obj instanceof WallpapersListActivity.ColorWallpaper)) {
                                    if (!(obj instanceof WallpapersListActivity.ColorWallpaper) || !"d".equals(((WallpapersListActivity.ColorWallpaper) obj).slug)) {
                                        i9 = 3;
                                        String[] strArr = new String[i9];
                                        int[] iArr = new int[i9];
                                        this.backgroundCheckBoxView = new WallpaperCheckBoxView[i9];
                                        if (i9 == 0) {
                                            this.backgroundButtonsContainer = new FrameLayout(context);
                                            if (this.screenType == 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                                                strArr[0] = LocaleController.getString("BackgroundColors", R.string.BackgroundColors);
                                                strArr[1] = LocaleController.getString("BackgroundPattern", R.string.BackgroundPattern);
                                                strArr[i3] = LocaleController.getString("BackgroundMotion", R.string.BackgroundMotion);
                                            } else {
                                                strArr[0] = LocaleController.getString("BackgroundBlurred", R.string.BackgroundBlurred);
                                                strArr[1] = LocaleController.getString("BackgroundMotion", R.string.BackgroundMotion);
                                            }
                                            i10 = 0;
                                            for (int i17 = 0; i17 < i9; i17++) {
                                                iArr[i17] = (int) Math.ceil(textPaint.measureText(strArr[i17]));
                                                i10 = Math.max(i10, iArr[i17]);
                                            }
                                            FrameLayout frameLayout5 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.17
                                                private RectF rect = new RectF();

                                                @Override // android.view.View
                                                protected void onDraw(Canvas canvas) {
                                                    this.rect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                                                    Theme.applyServiceShaderMatrixForView(ThemePreviewActivity.this.backgroundPlayAnimationView, ThemePreviewActivity.this.backgroundImage, ThemePreviewActivity.this.themeDelegate);
                                                    canvas.drawRoundRect(this.rect, getMeasuredHeight() / 2, getMeasuredHeight() / 2, ThemePreviewActivity.this.themeDelegate.getPaint("paintChatActionBackground"));
                                                    if (Theme.hasGradientService()) {
                                                        canvas.drawRoundRect(this.rect, getMeasuredHeight() / 2, getMeasuredHeight() / 2, ThemePreviewActivity.this.themeDelegate.getPaint("paintChatActionBackgroundDarken"));
                                                    }
                                                }
                                            };
                                            this.backgroundPlayAnimationView = frameLayout5;
                                            frameLayout5.setWillNotDraw(false);
                                            this.backgroundPlayAnimationView.setVisibility(this.backgroundGradientColor1 != 0 ? 0 : 4);
                                            this.backgroundPlayAnimationView.setScaleX(this.backgroundGradientColor1 != 0 ? 1.0f : 0.1f);
                                            this.backgroundPlayAnimationView.setScaleY(this.backgroundGradientColor1 != 0 ? 1.0f : 0.1f);
                                            this.backgroundPlayAnimationView.setAlpha(this.backgroundGradientColor1 != 0 ? 1.0f : 0.0f);
                                            this.backgroundPlayAnimationView.setTag(this.backgroundGradientColor1 != 0 ? 1 : null);
                                            this.backgroundButtonsContainer.addView(this.backgroundPlayAnimationView, LayoutHelper.createFrame(48, 48, 17));
                                            this.backgroundPlayAnimationView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity.18
                                                int rotation = 0;

                                                @Override // android.view.View.OnClickListener
                                                public void onClick(View view) {
                                                    ThemePreviewActivity.this.backgroundPlayAnimationImageView.setRotation(this.rotation);
                                                    this.rotation -= 45;
                                                    ThemePreviewActivity.this.backgroundPlayAnimationImageView.animate().rotationBy(-45.0f).setDuration(300L).setInterpolator(CubicBezierInterpolator.EASE_OUT).start();
                                                    if (ThemePreviewActivity.this.backgroundImages[0] != null) {
                                                        Drawable background = ThemePreviewActivity.this.backgroundImages[0].getBackground();
                                                        if (!(background instanceof MotionBackgroundDrawable)) {
                                                            ThemePreviewActivity.this.onColorsRotate();
                                                        } else {
                                                            ((MotionBackgroundDrawable) background).switchToNextPosition();
                                                        }
                                                    }
                                                    if (ThemePreviewActivity.this.backgroundImages[1] != null) {
                                                        Drawable background2 = ThemePreviewActivity.this.backgroundImages[1].getBackground();
                                                        if (background2 instanceof MotionBackgroundDrawable) {
                                                            ((MotionBackgroundDrawable) background2).switchToNextPosition();
                                                        }
                                                    }
                                                }
                                            });
                                            ImageView imageView4 = new ImageView(context);
                                            this.backgroundPlayAnimationImageView = imageView4;
                                            imageView4.setScaleType(ImageView.ScaleType.CENTER);
                                            this.backgroundPlayAnimationImageView.setImageResource(R.drawable.bg_rotate_large);
                                            this.backgroundPlayAnimationView.addView(this.backgroundPlayAnimationImageView, LayoutHelper.createFrame(-2, -2, 17));
                                        } else {
                                            i10 = 0;
                                        }
                                        i11 = 0;
                                        while (i11 < i9) {
                                            this.backgroundCheckBoxView[i11] = new WallpaperCheckBoxView(context, ((this.screenType == 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) && i11 == 0) ? false : true, this.backgroundImage, this.themeDelegate);
                                            this.backgroundCheckBoxView[i11].setBackgroundColor(this.backgroundColor);
                                            this.backgroundCheckBoxView[i11].setText(strArr[i11], iArr[i11], i10);
                                            if (this.screenType != 1 && !(this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                                                this.backgroundCheckBoxView[i11].setChecked(i11 == 0 ? this.isBlurred : this.isMotion, false);
                                            } else if (i11 == 1) {
                                                this.backgroundCheckBoxView[i11].setChecked((this.selectedPattern == null && ((themeAccent = this.accent) == null || TextUtils.isEmpty(themeAccent.patternSlug))) ? false : true, false);
                                            } else if (i11 == 2) {
                                                this.backgroundCheckBoxView[i11].setChecked(this.isMotion, false);
                                            }
                                            int dp = AndroidUtilities.dp(56.0f) + i10;
                                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dp, -2);
                                            layoutParams.gravity = 17;
                                            if (i9 == 3) {
                                                if (i11 == 0 || i11 == 2) {
                                                    layoutParams.leftMargin = (dp / 2) + AndroidUtilities.dp(10.0f);
                                                } else {
                                                    layoutParams.rightMargin = (dp / 2) + AndroidUtilities.dp(10.0f);
                                                }
                                            } else if (i11 == 1) {
                                                layoutParams.leftMargin = (dp / 2) + AndroidUtilities.dp(10.0f);
                                            } else {
                                                layoutParams.rightMargin = (dp / 2) + AndroidUtilities.dp(10.0f);
                                            }
                                            this.backgroundButtonsContainer.addView(this.backgroundCheckBoxView[i11], layoutParams);
                                            WallpaperCheckBoxView[] wallpaperCheckBoxViewArr = this.backgroundCheckBoxView;
                                            final WallpaperCheckBoxView wallpaperCheckBoxView = wallpaperCheckBoxViewArr[i11];
                                            wallpaperCheckBoxViewArr[i11].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda13
                                                @Override // android.view.View.OnClickListener
                                                public final void onClick(View view) {
                                                    ThemePreviewActivity.this.lambda$createView$11(i11, wallpaperCheckBoxView, view);
                                                }
                                            });
                                            if (i11 == 2) {
                                                this.backgroundCheckBoxView[i11].setAlpha(0.0f);
                                                this.backgroundCheckBoxView[i11].setVisibility(4);
                                            }
                                            i11++;
                                        }
                                    }
                                    i9 = 0;
                                    String[] strArr2 = new String[i9];
                                    int[] iArr2 = new int[i9];
                                    this.backgroundCheckBoxView = new WallpaperCheckBoxView[i9];
                                    if (i9 == 0) {
                                    }
                                    i11 = 0;
                                    while (i11 < i9) {
                                    }
                                } else {
                                    if (!(obj instanceof WallpapersListActivity.FileWallpaper) || !"t".equals(((WallpapersListActivity.FileWallpaper) obj).slug)) {
                                        i9 = 2;
                                        String[] strArr22 = new String[i9];
                                        int[] iArr22 = new int[i9];
                                        this.backgroundCheckBoxView = new WallpaperCheckBoxView[i9];
                                        if (i9 == 0) {
                                        }
                                        i11 = 0;
                                        while (i11 < i9) {
                                        }
                                    }
                                    i9 = 0;
                                    String[] strArr222 = new String[i9];
                                    int[] iArr222 = new int[i9];
                                    this.backgroundCheckBoxView = new WallpaperCheckBoxView[i9];
                                    if (i9 == 0) {
                                    }
                                    i11 = 0;
                                    while (i11 < i9) {
                                    }
                                }
                            }
                            if (this.screenType == 1) {
                                int[] iArr3 = new int[2];
                                this.messagesCheckBoxView = new WallpaperCheckBoxView[2];
                                this.messagesButtonsContainer = new FrameLayout(context);
                                String[] strArr3 = {LocaleController.getString("BackgroundAnimate", R.string.BackgroundAnimate), LocaleController.getString("BackgroundColors", R.string.BackgroundColors)};
                                int i18 = 0;
                                for (int i19 = 0; i19 < 2; i19++) {
                                    iArr3[i19] = (int) Math.ceil(textPaint.measureText(strArr3[i19]));
                                    i18 = Math.max(i18, iArr3[i19]);
                                }
                                if (this.accent != null) {
                                    FrameLayout frameLayout6 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.19
                                        private RectF rect = new RectF();

                                        @Override // android.view.View
                                        protected void onDraw(Canvas canvas) {
                                            this.rect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                                            Theme.applyServiceShaderMatrixForView(ThemePreviewActivity.this.messagesPlayAnimationView, ThemePreviewActivity.this.backgroundImage, ThemePreviewActivity.this.themeDelegate);
                                            canvas.drawRoundRect(this.rect, getMeasuredHeight() / 2, getMeasuredHeight() / 2, ThemePreviewActivity.this.themeDelegate.getPaint("paintChatActionBackground"));
                                            if (Theme.hasGradientService()) {
                                                canvas.drawRoundRect(this.rect, getMeasuredHeight() / 2, getMeasuredHeight() / 2, ThemePreviewActivity.this.themeDelegate.getPaint("paintChatActionBackgroundDarken"));
                                            }
                                        }
                                    };
                                    this.messagesPlayAnimationView = frameLayout6;
                                    frameLayout6.setWillNotDraw(false);
                                    this.messagesPlayAnimationView.setVisibility(this.accent.myMessagesGradientAccentColor1 != 0 ? 0 : 4);
                                    this.messagesPlayAnimationView.setScaleX(this.accent.myMessagesGradientAccentColor1 != 0 ? 1.0f : 0.1f);
                                    this.messagesPlayAnimationView.setScaleY(this.accent.myMessagesGradientAccentColor1 != 0 ? 1.0f : 0.1f);
                                    this.messagesPlayAnimationView.setAlpha(this.accent.myMessagesGradientAccentColor1 != 0 ? 1.0f : 0.0f);
                                    this.messagesButtonsContainer.addView(this.messagesPlayAnimationView, LayoutHelper.createFrame(48, 48, 17));
                                    this.messagesPlayAnimationView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity.20
                                        int rotation = 0;

                                        @Override // android.view.View.OnClickListener
                                        public void onClick(View view) {
                                            ThemePreviewActivity.this.messagesPlayAnimationImageView.setRotation(this.rotation);
                                            this.rotation -= 45;
                                            ThemePreviewActivity.this.messagesPlayAnimationImageView.animate().rotationBy(-45.0f).setDuration(300L).setInterpolator(CubicBezierInterpolator.EASE_OUT).start();
                                            if (!ThemePreviewActivity.this.accent.myMessagesAnimated) {
                                                if (ThemePreviewActivity.this.accent.myMessagesGradientAccentColor3 != 0) {
                                                    int i20 = ThemePreviewActivity.this.accent.myMessagesAccentColor != 0 ? ThemePreviewActivity.this.accent.myMessagesAccentColor : ThemePreviewActivity.this.accent.accentColor;
                                                    ThemePreviewActivity.this.accent.myMessagesAccentColor = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1;
                                                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1 = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2;
                                                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2 = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor3;
                                                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor3 = i20;
                                                } else {
                                                    int i21 = ThemePreviewActivity.this.accent.myMessagesAccentColor != 0 ? ThemePreviewActivity.this.accent.myMessagesAccentColor : ThemePreviewActivity.this.accent.accentColor;
                                                    ThemePreviewActivity.this.accent.myMessagesAccentColor = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1;
                                                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1 = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2;
                                                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2 = i21;
                                                }
                                                ThemePreviewActivity.this.colorPicker.setColor(ThemePreviewActivity.this.accent.myMessagesGradientAccentColor3, 3);
                                                ThemePreviewActivity.this.colorPicker.setColor(ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2, 2);
                                                ThemePreviewActivity.this.colorPicker.setColor(ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1, 1);
                                                ThemePreviewActivity.this.colorPicker.setColor(ThemePreviewActivity.this.accent.myMessagesAccentColor != 0 ? ThemePreviewActivity.this.accent.myMessagesAccentColor : ThemePreviewActivity.this.accent.accentColor, 0);
                                                ThemePreviewActivity.this.messagesCheckBoxView[1].setColor(0, ThemePreviewActivity.this.accent.myMessagesAccentColor);
                                                ThemePreviewActivity.this.messagesCheckBoxView[1].setColor(1, ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1);
                                                ThemePreviewActivity.this.messagesCheckBoxView[1].setColor(2, ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2);
                                                ThemePreviewActivity.this.messagesCheckBoxView[1].setColor(3, ThemePreviewActivity.this.accent.myMessagesGradientAccentColor3);
                                                Theme.refreshThemeColors(true, true);
                                                ThemePreviewActivity.this.listView2.invalidateViews();
                                            } else if (ThemePreviewActivity.this.msgOutDrawable.getMotionBackgroundDrawable() != null) {
                                                ThemePreviewActivity.this.msgOutDrawable.getMotionBackgroundDrawable().switchToNextPosition();
                                            }
                                        }
                                    });
                                    ImageView imageView5 = new ImageView(context);
                                    this.messagesPlayAnimationImageView = imageView5;
                                    imageView5.setScaleType(ImageView.ScaleType.CENTER);
                                    this.messagesPlayAnimationImageView.setImageResource(R.drawable.bg_rotate_large);
                                    this.messagesPlayAnimationView.addView(this.messagesPlayAnimationImageView, LayoutHelper.createFrame(-2, -2, 17));
                                    final int i20 = 0;
                                    while (i20 < 2) {
                                        this.messagesCheckBoxView[i20] = new WallpaperCheckBoxView(context, i20 == 0, this.backgroundImage, this.themeDelegate);
                                        this.messagesCheckBoxView[i20].setText(strArr3[i20], iArr3[i20], i18);
                                        if (i20 == 0) {
                                            this.messagesCheckBoxView[i20].setChecked(this.accent.myMessagesAnimated, false);
                                        }
                                        int dp2 = AndroidUtilities.dp(56.0f) + i18;
                                        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(dp2, -2);
                                        layoutParams2.gravity = 17;
                                        if (i20 == 1) {
                                            layoutParams2.leftMargin = (dp2 / 2) + AndroidUtilities.dp(10.0f);
                                        } else {
                                            layoutParams2.rightMargin = (dp2 / 2) + AndroidUtilities.dp(10.0f);
                                        }
                                        this.messagesButtonsContainer.addView(this.messagesCheckBoxView[i20], layoutParams2);
                                        WallpaperCheckBoxView[] wallpaperCheckBoxViewArr2 = this.messagesCheckBoxView;
                                        final WallpaperCheckBoxView wallpaperCheckBoxView2 = wallpaperCheckBoxViewArr2[i20];
                                        wallpaperCheckBoxViewArr2[i20].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda14
                                            @Override // android.view.View.OnClickListener
                                            public final void onClick(View view) {
                                                ThemePreviewActivity.this.lambda$createView$12(i20, wallpaperCheckBoxView2, view);
                                            }
                                        });
                                        i20++;
                                    }
                                }
                            }
                            if (this.screenType != 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                                this.isBlurred = false;
                                i8 = 0;
                                while (i8 < 2) {
                                    this.patternLayout[i8] = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.21
                                        @Override // android.view.View
                                        public void onDraw(Canvas canvas) {
                                            if (i8 == 0) {
                                                ThemePreviewActivity.this.sheetDrawable.setBounds(ThemePreviewActivity.this.colorPicker.getLeft() - rect.left, 0, ThemePreviewActivity.this.colorPicker.getRight() + rect.right, getMeasuredHeight());
                                            } else {
                                                ThemePreviewActivity.this.sheetDrawable.setBounds(-rect.left, 0, getMeasuredWidth() + rect.right, getMeasuredHeight());
                                            }
                                            ThemePreviewActivity.this.sheetDrawable.draw(canvas);
                                        }
                                    };
                                    if (i8 == 1 || this.screenType == 2) {
                                        this.patternLayout[i8].setVisibility(4);
                                    }
                                    this.patternLayout[i8].setWillNotDraw(false);
                                    if (this.screenType == 2) {
                                        createFrame = LayoutHelper.createFrame(-1, i8 == 0 ? 321 : 316, 83);
                                    } else {
                                        createFrame = LayoutHelper.createFrame(-1, i8 == 0 ? 273 : 316, 83);
                                    }
                                    if (i8 == 0) {
                                        f = this.screenType == 2 ? 321 : 273;
                                    } else {
                                        f = 316.0f;
                                    }
                                    createFrame.height = AndroidUtilities.dp(f);
                                    if (insideBottomSheet()) {
                                        createFrame.height += AndroidUtilities.navigationBarHeight;
                                    }
                                    if (i8 == 0) {
                                        Drawable drawable = this.sheetDrawable;
                                        Rect rect2 = AndroidUtilities.rectTmp2;
                                        drawable.getPadding(rect2);
                                        createFrame.height += AndroidUtilities.dp(12.0f) + rect2.top;
                                    }
                                    this.patternLayout[i8].setPadding(0, i8 == 0 ? AndroidUtilities.dp(12.0f) + rect.top : 0, 0, insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0);
                                    this.page2.addView(this.patternLayout[i8], createFrame);
                                    if (i8 == 1 || this.screenType == 2) {
                                        this.patternsButtonsContainer[i8] = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.22
                                            Paint paint = new Paint();

                                            @Override // android.view.View
                                            public void onDraw(Canvas canvas) {
                                                int intrinsicHeight = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                                                Theme.chat_composeShadowDrawable.setBounds(0, 0, getMeasuredWidth(), intrinsicHeight);
                                                Theme.chat_composeShadowDrawable.draw(canvas);
                                                this.paint.setColor(ThemePreviewActivity.this.getThemedColor(Theme.key_chat_messagePanelBackground));
                                                canvas.drawRect(0.0f, intrinsicHeight, getMeasuredWidth(), getMeasuredHeight(), this.paint);
                                            }
                                        };
                                        this.patternsButtonsContainer[i8].setWillNotDraw(false);
                                        this.patternsButtonsContainer[i8].setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
                                        this.patternsButtonsContainer[i8].setClickable(true);
                                        this.patternLayout[i8].addView(this.patternsButtonsContainer[i8], LayoutHelper.createFrame(-1, 51, 80));
                                        this.patternsCancelButton[i8] = new TextView(context);
                                        this.patternsCancelButton[i8].setTextSize(1, 15.0f);
                                        this.patternsCancelButton[i8].setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                                        TextView textView3 = this.patternsCancelButton[i8];
                                        int i21 = Theme.key_chat_fieldOverlayText;
                                        textView3.setTextColor(getThemedColor(i21));
                                        this.patternsCancelButton[i8].setText(LocaleController.getString("Cancel", R.string.Cancel).toUpperCase());
                                        this.patternsCancelButton[i8].setGravity(17);
                                        this.patternsCancelButton[i8].setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
                                        TextView textView4 = this.patternsCancelButton[i8];
                                        int i22 = Theme.key_listSelector;
                                        textView4.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(i22), 0));
                                        this.patternsButtonsContainer[i8].addView(this.patternsCancelButton[i8], LayoutHelper.createFrame(-2, -1, 51));
                                        this.patternsCancelButton[i8].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda11
                                            @Override // android.view.View.OnClickListener
                                            public final void onClick(View view) {
                                                ThemePreviewActivity.this.lambda$createView$13(i8, view);
                                            }
                                        });
                                        this.patternsSaveButton[i8] = new TextView(context);
                                        this.patternsSaveButton[i8].setTextSize(1, 15.0f);
                                        this.patternsSaveButton[i8].setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                                        this.patternsSaveButton[i8].setTextColor(getThemedColor(i21));
                                        this.patternsSaveButton[i8].setText(LocaleController.getString("ApplyTheme", R.string.ApplyTheme).toUpperCase());
                                        this.patternsSaveButton[i8].setGravity(17);
                                        this.patternsSaveButton[i8].setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
                                        this.patternsSaveButton[i8].setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(i22), 0));
                                        this.patternsButtonsContainer[i8].addView(this.patternsSaveButton[i8], LayoutHelper.createFrame(-2, -1, 53));
                                        this.patternsSaveButton[i8].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda12
                                            @Override // android.view.View.OnClickListener
                                            public final void onClick(View view) {
                                                ThemePreviewActivity.this.lambda$createView$14(i8, view);
                                            }
                                        });
                                    }
                                    if (i8 == 1) {
                                        TextView textView5 = new TextView(context);
                                        this.patternTitleView = textView5;
                                        textView5.setLines(1);
                                        this.patternTitleView.setSingleLine(true);
                                        this.patternTitleView.setText(LocaleController.getString("BackgroundChoosePattern", R.string.BackgroundChoosePattern));
                                        this.patternTitleView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                                        this.patternTitleView.setTextSize(1, 20.0f);
                                        this.patternTitleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                                        this.patternTitleView.setPadding(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(21.0f), AndroidUtilities.dp(8.0f));
                                        this.patternTitleView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                                        this.patternTitleView.setGravity(16);
                                        this.patternLayout[i8].addView(this.patternTitleView, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 21.0f, 0.0f, 0.0f));
                                        RecyclerListView recyclerListView2 = new RecyclerListView(this, context) { // from class: org.telegram.ui.ThemePreviewActivity.23
                                            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
                                            public boolean onTouchEvent(MotionEvent motionEvent) {
                                                if (motionEvent.getAction() == 0) {
                                                    getParent().requestDisallowInterceptTouchEvent(true);
                                                }
                                                return super.onTouchEvent(motionEvent);
                                            }
                                        };
                                        this.patternsListView = recyclerListView2;
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 0, false);
                                        this.patternsLayoutManager = linearLayoutManager;
                                        recyclerListView2.setLayoutManager(linearLayoutManager);
                                        RecyclerListView recyclerListView3 = this.patternsListView;
                                        PatternsAdapter patternsAdapter = new PatternsAdapter(context);
                                        this.patternsAdapter = patternsAdapter;
                                        recyclerListView3.setAdapter(patternsAdapter);
                                        this.patternsListView.addItemDecoration(new RecyclerView.ItemDecoration(this) { // from class: org.telegram.ui.ThemePreviewActivity.24
                                            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                                            public void getItemOffsets(Rect rect3, View view, RecyclerView recyclerView, RecyclerView.State state) {
                                                int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
                                                rect3.left = AndroidUtilities.dp(12.0f);
                                                rect3.top = 0;
                                                rect3.bottom = 0;
                                                if (childAdapterPosition == state.getItemCount() - 1) {
                                                    rect3.right = AndroidUtilities.dp(12.0f);
                                                }
                                            }
                                        });
                                        this.patternLayout[i8].addView(this.patternsListView, LayoutHelper.createFrame(-1, 100.0f, 51, 0.0f, 76.0f, 0.0f, 0.0f));
                                        this.patternsListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda33
                                            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                                            public final void onItemClick(View view, int i23) {
                                                ThemePreviewActivity.this.lambda$createView$15(view, i23);
                                            }
                                        });
                                        HeaderCell headerCell = new HeaderCell(context);
                                        this.intensityCell = headerCell;
                                        headerCell.setText(LocaleController.getString("BackgroundIntensity", R.string.BackgroundIntensity));
                                        this.patternLayout[i8].addView(this.intensityCell, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 175.0f, 0.0f, 0.0f));
                                        SeekBarView seekBarView = new SeekBarView(this, context, getResourceProvider()) { // from class: org.telegram.ui.ThemePreviewActivity.25
                                            @Override // org.telegram.ui.Components.SeekBarView, android.view.View
                                            public boolean onTouchEvent(MotionEvent motionEvent) {
                                                if (motionEvent.getAction() == 0) {
                                                    getParent().requestDisallowInterceptTouchEvent(true);
                                                }
                                                return super.onTouchEvent(motionEvent);
                                            }
                                        };
                                        this.intensitySeekBar = seekBarView;
                                        seekBarView.setProgress(this.currentIntensity);
                                        this.intensitySeekBar.setReportChanges(true);
                                        this.intensitySeekBar.setDelegate(new SeekBarView.SeekBarViewDelegate() { // from class: org.telegram.ui.ThemePreviewActivity.26
                                            @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                                            public /* synthetic */ CharSequence getContentDescription() {
                                                return SeekBarView.SeekBarViewDelegate.-CC.$default$getContentDescription(this);
                                            }

                                            @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                                            public /* synthetic */ int getStepsCount() {
                                                return SeekBarView.SeekBarViewDelegate.-CC.$default$getStepsCount(this);
                                            }

                                            @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                                            public void onSeekBarPressed(boolean z4) {
                                            }

                                            @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                                            public void onSeekBarDrag(boolean z4, float f3) {
                                                ThemePreviewActivity.this.currentIntensity = f3;
                                                ThemePreviewActivity.this.updateIntensity();
                                            }
                                        });
                                        this.patternLayout[i8].addView(this.intensitySeekBar, LayoutHelper.createFrame(-1, 38.0f, 51, 5.0f, 211.0f, 5.0f, 0.0f));
                                    } else {
                                        ColorPicker colorPicker = new ColorPicker(context, this.editingTheme, new 27());
                                        this.colorPicker = colorPicker;
                                        colorPicker.setResourcesProvider(getResourceProvider());
                                        if (this.screenType == 1) {
                                            this.patternLayout[i8].addView(this.colorPicker, LayoutHelper.createFrame(-1, -1, 1));
                                            if (this.applyingTheme.isDark()) {
                                                this.colorPicker.setMinBrightness(0.2f);
                                            } else {
                                                this.colorPicker.setMinBrightness(0.05f);
                                                this.colorPicker.setMaxBrightness(0.8f);
                                            }
                                            Theme.ThemeAccent themeAccent3 = this.accent;
                                            if (themeAccent3 != null) {
                                                this.colorPicker.setType(1, hasChanges(1), 2, themeAccent3.accentColor2 != 0 ? 2 : 1, false, 0, false);
                                                this.colorPicker.setColor(this.accent.accentColor, 0);
                                                int i23 = this.accent.accentColor2;
                                                if (i23 != 0) {
                                                    this.colorPicker.setColor(i23, 1);
                                                }
                                            }
                                        } else {
                                            this.patternLayout[i8].addView(this.colorPicker, LayoutHelper.createFrame(-1, -1.0f, 1, 0.0f, 0.0f, 0.0f, 48.0f));
                                        }
                                    }
                                    i8++;
                                }
                            }
                            updateButtonState(false, false);
                            if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
                                this.page2.setBackgroundColor(-16777216);
                            }
                            if (this.screenType != 1 && !(this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                                this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(true);
                            }
                        }
                        this.listView2.setAdapter(this.messagesAdapter);
                        FrameLayout frameLayout7 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.28
                            private int[] loc = new int[2];

                            @Override // android.view.View
                            public void invalidate() {
                                super.invalidate();
                                if (ThemePreviewActivity.this.page2 != null) {
                                    ThemePreviewActivity.this.page2.invalidate();
                                }
                            }

                            @Override // android.view.View
                            protected void onDraw(Canvas canvas) {
                                if (AndroidUtilities.usingHardwareInput) {
                                    return;
                                }
                                getLocationInWindow(this.loc);
                                if (Build.VERSION.SDK_INT < 21 && !AndroidUtilities.isTablet()) {
                                    int[] iArr4 = this.loc;
                                    iArr4[1] = iArr4[1] - AndroidUtilities.statusBarHeight;
                                }
                                if (ThemePreviewActivity.this.actionBar2.getTranslationY() != this.loc[1]) {
                                    ThemePreviewActivity.this.actionBar2.setTranslationY(-this.loc[1]);
                                    ThemePreviewActivity.this.page2.invalidate();
                                }
                                if (SystemClock.elapsedRealtime() < ThemePreviewActivity.this.watchForKeyboardEndTime) {
                                    invalidate();
                                }
                            }
                        };
                        this.frameLayout = frameLayout7;
                        frameLayout7.setWillNotDraw(false);
                        FrameLayout frameLayout8 = this.frameLayout;
                        this.fragmentView = frameLayout8;
                        ViewTreeObserver viewTreeObserver = frameLayout8.getViewTreeObserver();
                        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda16
                            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                            public final void onGlobalLayout() {
                                ThemePreviewActivity.this.lambda$createView$16();
                            }
                        };
                        this.onGlobalLayoutListener = onGlobalLayoutListener;
                        viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener);
                        ViewPager viewPager = new ViewPager(context);
                        this.viewPager = viewPager;
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: org.telegram.ui.ThemePreviewActivity.29
                            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                            public void onPageScrollStateChanged(int i24) {
                            }

                            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                            public void onPageScrolled(int i24, float f3, int i25) {
                            }

                            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                            public void onPageSelected(int i24) {
                                ThemePreviewActivity.this.dotsContainer.invalidate();
                            }
                        });
                        this.viewPager.setAdapter(new PagerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.30
                            @Override // androidx.viewpager.widget.PagerAdapter
                            public int getItemPosition(Object obj5) {
                                return -1;
                            }

                            @Override // androidx.viewpager.widget.PagerAdapter
                            public boolean isViewFromObject(View view, Object obj5) {
                                return obj5 == view;
                            }

                            @Override // androidx.viewpager.widget.PagerAdapter
                            public int getCount() {
                                return ThemePreviewActivity.this.screenType != 0 ? 1 : 2;
                            }

                            @Override // androidx.viewpager.widget.PagerAdapter
                            public Object instantiateItem(ViewGroup viewGroup, int i24) {
                                FrameLayout frameLayout9 = i24 == 0 ? ThemePreviewActivity.this.page2 : ThemePreviewActivity.this.page1;
                                viewGroup.addView(frameLayout9);
                                return frameLayout9;
                            }

                            @Override // androidx.viewpager.widget.PagerAdapter
                            public void destroyItem(ViewGroup viewGroup, int i24, Object obj5) {
                                viewGroup.removeView((View) obj5);
                            }
                        });
                        AndroidUtilities.setViewPagerEdgeEffectColor(this.viewPager, getThemedColor(Theme.key_actionBarDefault));
                        this.frameLayout.addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.screenType == 0 ? 48.0f : 0.0f));
                        UndoView undoView = new UndoView(context, this);
                        this.undoView = undoView;
                        undoView.setAdditionalTranslationY(AndroidUtilities.dp(51.0f));
                        this.frameLayout.addView(this.undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
                        if (this.screenType == 0) {
                            View view = new View(context);
                            view.setBackgroundColor(getThemedColor(Theme.key_dialogShadowLine));
                            FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(-1, 1, 83);
                            layoutParams3.bottomMargin = AndroidUtilities.dp(48.0f);
                            this.frameLayout.addView(view, layoutParams3);
                            FrameLayout frameLayout9 = new FrameLayout(context);
                            this.saveButtonsContainer = frameLayout9;
                            frameLayout9.setBackgroundColor(getButtonsColor(Theme.key_windowBackgroundWhite));
                            this.frameLayout.addView(this.saveButtonsContainer, LayoutHelper.createFrame(-1, 48, 83));
                            View view2 = new View(context) { // from class: org.telegram.ui.ThemePreviewActivity.31
                                private Paint paint = new Paint(1);

                                @Override // android.view.View
                                protected void onDraw(Canvas canvas) {
                                    int currentItem = ThemePreviewActivity.this.viewPager.getCurrentItem();
                                    this.paint.setColor(ThemePreviewActivity.this.getButtonsColor(Theme.key_chat_fieldOverlayText));
                                    int i24 = 0;
                                    while (i24 < 2) {
                                        this.paint.setAlpha(i24 == currentItem ? 255 : 127);
                                        canvas.drawCircle(AndroidUtilities.dp((i24 * 15) + 3), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(3.0f), this.paint);
                                        i24++;
                                    }
                                }
                            };
                            this.dotsContainer = view2;
                            this.saveButtonsContainer.addView(view2, LayoutHelper.createFrame(22, 8, 17));
                            TextView textView6 = new TextView(context);
                            this.cancelButton = textView6;
                            textView6.setTextSize(1, 14.0f);
                            TextView textView7 = this.cancelButton;
                            int i24 = Theme.key_chat_fieldOverlayText;
                            textView7.setTextColor(getButtonsColor(i24));
                            this.cancelButton.setGravity(17);
                            this.cancelButton.setBackgroundDrawable(Theme.createSelectorDrawable(AndroidUtilities.LIGHT_STATUS_BAR_OVERLAY, 0));
                            this.cancelButton.setPadding(AndroidUtilities.dp(29.0f), 0, AndroidUtilities.dp(29.0f), 0);
                            this.cancelButton.setText(LocaleController.getString("Cancel", R.string.Cancel).toUpperCase());
                            this.cancelButton.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                            this.saveButtonsContainer.addView(this.cancelButton, LayoutHelper.createFrame(-2, -1, 51));
                            this.cancelButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda9
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view3) {
                                    ThemePreviewActivity.this.lambda$createView$17(view3);
                                }
                            });
                            TextView textView8 = new TextView(context);
                            this.doneButton = textView8;
                            textView8.setTextSize(1, 14.0f);
                            this.doneButton.setTextColor(getButtonsColor(i24));
                            this.doneButton.setGravity(17);
                            this.doneButton.setBackgroundDrawable(Theme.createSelectorDrawable(AndroidUtilities.LIGHT_STATUS_BAR_OVERLAY, 0));
                            this.doneButton.setPadding(AndroidUtilities.dp(29.0f), 0, AndroidUtilities.dp(29.0f), 0);
                            this.doneButton.setText(LocaleController.getString("ApplyTheme", R.string.ApplyTheme).toUpperCase());
                            this.doneButton.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                            this.saveButtonsContainer.addView(this.doneButton, LayoutHelper.createFrame(-2, -1, 53));
                            this.doneButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda10
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view3) {
                                    ThemePreviewActivity.this.lambda$createView$18(view3);
                                }
                            });
                        }
                        if (this.screenType == 1 && !Theme.hasCustomWallpaper() && (themeAccent2 = this.accent) != null && themeAccent2.backgroundOverrideColor != 4294967296L) {
                            selectColorType(2);
                        }
                        this.themeDescriptions = getThemeDescriptionsInternal();
                        setCurrentImage(true);
                        updatePlayAnimationView(false);
                        if (this.showColor) {
                            showPatternsView(0, true, false);
                        }
                        this.scroller = new Scroller(getContext());
                        iNavigationLayout = this.parentLayout;
                        if (iNavigationLayout != null && iNavigationLayout.getBottomSheet() != null) {
                            this.parentLayout.getBottomSheet().fixNavigationBar(getThemedColor(Theme.key_dialogBackground));
                            if (this.screenType == 2 && this.dialogId != 0) {
                                this.parentLayout.getBottomSheet().setOverlayNavBarColor(-16777216);
                            }
                        }
                        return this.fragmentView;
                    }
                }
                i3 = 2;
                this.listView2 = new RecyclerListView(context) { // from class: org.telegram.ui.ThemePreviewActivity.11
                    boolean scrollingBackground;
                    float startX;

                    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                    public boolean drawChild(Canvas canvas, View view3, long j) {
                        RecyclerView.ViewHolder childViewHolder;
                        boolean drawChild = super.drawChild(canvas, view3, j);
                        if (view3 instanceof ChatMessageCell) {
                            ChatMessageCell chatMessageCell = (ChatMessageCell) view3;
                            chatMessageCell.getMessageObject();
                            ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                            if (avatarImage != null) {
                                int top = view3.getTop();
                                if (chatMessageCell.isPinnedBottom() && (childViewHolder = ThemePreviewActivity.this.listView2.getChildViewHolder(view3)) != null) {
                                    if (ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder.getAdapterPosition() - 1) != null) {
                                        avatarImage.setImageY(-AndroidUtilities.dp(1000.0f));
                                        avatarImage.draw(canvas);
                                        return drawChild;
                                    }
                                }
                                float translationX = chatMessageCell.getTranslationX();
                                int top2 = view3.getTop() + chatMessageCell.getLayoutHeight();
                                int measuredHeight = ThemePreviewActivity.this.listView2.getMeasuredHeight() - ThemePreviewActivity.this.listView2.getPaddingBottom();
                                if (top2 > measuredHeight) {
                                    top2 = measuredHeight;
                                }
                                if (chatMessageCell.isPinnedTop() && (childViewHolder2 = ThemePreviewActivity.this.listView2.getChildViewHolder(view3)) != null) {
                                    int i172 = 0;
                                    while (i172 < 20) {
                                        i172++;
                                        RecyclerView.ViewHolder childViewHolder2 = ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder2.getAdapterPosition() + 1);
                                        if (childViewHolder2 == null) {
                                            break;
                                        }
                                        top = childViewHolder2.itemView.getTop();
                                        if (top2 - AndroidUtilities.dp(48.0f) < childViewHolder2.itemView.getBottom()) {
                                            translationX = Math.min(childViewHolder2.itemView.getTranslationX(), translationX);
                                        }
                                        View view22 = childViewHolder2.itemView;
                                        if (!(view22 instanceof ChatMessageCell)) {
                                            break;
                                        } else if (!((ChatMessageCell) view22).isPinnedTop()) {
                                            break;
                                        }
                                    }
                                }
                                if (top2 - AndroidUtilities.dp(48.0f) < top) {
                                    top2 = top + AndroidUtilities.dp(48.0f);
                                }
                                if (translationX != 0.0f) {
                                    canvas.save();
                                    canvas.translate(translationX, 0.0f);
                                }
                                avatarImage.setImageY(top2 - AndroidUtilities.dp(44.0f));
                                avatarImage.draw(canvas);
                                if (translationX != 0.0f) {
                                    canvas.restore();
                                }
                            }
                        }
                        return drawChild;
                    }

                    @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
                    public void setTranslationY(float f3) {
                        super.setTranslationY(f3);
                        if (ThemePreviewActivity.this.backgroundCheckBoxView != null) {
                            for (int i172 = 0; i172 < ThemePreviewActivity.this.backgroundCheckBoxView.length; i172++) {
                                ThemePreviewActivity.this.backgroundCheckBoxView[i172].invalidate();
                            }
                        }
                        if (ThemePreviewActivity.this.messagesCheckBoxView != null) {
                            for (int i182 = 0; i182 < ThemePreviewActivity.this.messagesCheckBoxView.length; i182++) {
                                ThemePreviewActivity.this.messagesCheckBoxView[i182].invalidate();
                            }
                        }
                        if (ThemePreviewActivity.this.backgroundPlayAnimationView != null) {
                            ThemePreviewActivity.this.backgroundPlayAnimationView.invalidate();
                        }
                        if (ThemePreviewActivity.this.messagesPlayAnimationView != null) {
                            ThemePreviewActivity.this.messagesPlayAnimationView.invalidate();
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.telegram.ui.Components.RecyclerListView
                    public void onChildPressed(View view3, float f3, float f4, boolean z4) {
                        if (z4 && (view3 instanceof ChatMessageCell) && !((ChatMessageCell) view3).isInsideBackground(f3, f4)) {
                            return;
                        }
                        super.onChildPressed(view3, f3, f4, z4);
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.telegram.ui.Components.RecyclerListView
                    public boolean allowSelectChildAtPosition(View view3) {
                        RecyclerView.ViewHolder findContainingViewHolder = ThemePreviewActivity.this.listView2.findContainingViewHolder(view3);
                        if (findContainingViewHolder == null || findContainingViewHolder.getItemViewType() != 2) {
                            return super.allowSelectChildAtPosition(view3);
                        }
                        return false;
                    }

                    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
                    public boolean onTouchEvent(MotionEvent motionEvent) {
                        checkMotionEvent(motionEvent);
                        if (ThemePreviewActivity.this.hasScrollingBackground) {
                            if (motionEvent.getAction() == 0) {
                                this.startX = motionEvent.getX();
                                motionEvent.getY();
                                if (getParent() != null) {
                                    getParent().requestDisallowInterceptTouchEvent(true);
                                }
                                this.scrollingBackground = true;
                            } else if (motionEvent.getAction() == 2) {
                                if (!this.scrollingBackground && Math.abs(this.startX - motionEvent.getX()) > AndroidUtilities.touchSlop) {
                                    if (getParent() != null) {
                                        getParent().requestDisallowInterceptTouchEvent(true);
                                    }
                                    this.scrollingBackground = true;
                                }
                            } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                                this.scrollingBackground = false;
                                if (getParent() != null) {
                                    getParent().requestDisallowInterceptTouchEvent(false);
                                }
                            }
                            ThemePreviewActivity.this.gestureDetector2.onTouchEvent(motionEvent);
                        }
                        return this.scrollingBackground || super.onTouchEvent(motionEvent);
                    }

                    private void checkMotionEvent(MotionEvent motionEvent) {
                        if (motionEvent.getAction() == 1) {
                            if (!ThemePreviewActivity.this.wasScroll && (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) && ThemePreviewActivity.this.patternLayout[0].getVisibility() == 0) {
                                ThemePreviewActivity.this.showPatternsView(0, false, true);
                            }
                            ThemePreviewActivity.this.wasScroll = false;
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
                    public void onLayout(boolean z4, int i172, int i182, int i192, int i202) {
                        super.onLayout(z4, i172, i182, i192, i202);
                        ThemePreviewActivity.this.invalidateBlur();
                    }
                };
                DefaultItemAnimator defaultItemAnimator2 = new DefaultItemAnimator() { // from class: org.telegram.ui.ThemePreviewActivity.12
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // androidx.recyclerview.widget.DefaultItemAnimator
                    public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                        ThemePreviewActivity.this.listView2.invalidateViews();
                    }
                };
                defaultItemAnimator2.setDelayAnimations(false);
                this.listView2.setItemAnimator(defaultItemAnimator2);
                this.listView2.setVerticalScrollBarEnabled(true);
                this.listView2.setOverScrollMode(i3);
                i5 = this.screenType;
                if (i5 == i3) {
                }
                this.listView2.setClipToPadding(false);
                this.listView2.setLayoutManager(new LinearLayoutManager(context, 1, true));
                this.listView2.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
                if (this.screenType == 1) {
                }
                this.listView2.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ThemePreviewActivity.13
                    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                    public void onScrolled(RecyclerView recyclerView, int i172, int i182) {
                        ThemePreviewActivity.this.listView2.invalidateViews();
                        ThemePreviewActivity.this.wasScroll = true;
                    }

                    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                    public void onScrollStateChanged(RecyclerView recyclerView, int i172) {
                        if (i172 == 0) {
                            ThemePreviewActivity.this.wasScroll = false;
                        }
                    }
                });
                this.page2.addView(this.actionBar2, LayoutHelper.createFrame(i6, -2.0f));
                WallpaperParallaxEffect wallpaperParallaxEffect2 = new WallpaperParallaxEffect(context);
                this.parallaxEffect = wallpaperParallaxEffect2;
                wallpaperParallaxEffect2.setCallback(new WallpaperParallaxEffect.Callback() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda36
                    @Override // org.telegram.ui.Components.WallpaperParallaxEffect.Callback
                    public final void onOffsetsChanged(int i172, int i182, float f3) {
                        ThemePreviewActivity.this.lambda$createView$7(i172, i182, f3);
                    }
                });
                i7 = this.screenType;
                if (i7 != 1) {
                }
                if (i7 == i3) {
                }
                final Rect rect3 = new Rect();
                Drawable mutate32 = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
                this.sheetDrawable = mutate32;
                mutate32.getPadding(rect3);
                this.sheetDrawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundWhite), PorterDuff.Mode.MULTIPLY));
                TextPaint textPaint2 = new TextPaint(1);
                textPaint2.setTextSize(AndroidUtilities.dp(14.0f));
                textPaint2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                obj = this.currentWallpaper;
                if (!(obj instanceof WallpapersListActivity.EmojiWallpaper)) {
                }
                if (this.screenType == 1) {
                }
                if (this.screenType != 1) {
                }
                this.isBlurred = false;
                i8 = 0;
                while (i8 < 2) {
                }
                updateButtonState(false, false);
                if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
                }
                if (this.screenType != 1) {
                    this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(true);
                }
                this.listView2.setAdapter(this.messagesAdapter);
                FrameLayout frameLayout72 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.28
                    private int[] loc = new int[2];

                    @Override // android.view.View
                    public void invalidate() {
                        super.invalidate();
                        if (ThemePreviewActivity.this.page2 != null) {
                            ThemePreviewActivity.this.page2.invalidate();
                        }
                    }

                    @Override // android.view.View
                    protected void onDraw(Canvas canvas) {
                        if (AndroidUtilities.usingHardwareInput) {
                            return;
                        }
                        getLocationInWindow(this.loc);
                        if (Build.VERSION.SDK_INT < 21 && !AndroidUtilities.isTablet()) {
                            int[] iArr4 = this.loc;
                            iArr4[1] = iArr4[1] - AndroidUtilities.statusBarHeight;
                        }
                        if (ThemePreviewActivity.this.actionBar2.getTranslationY() != this.loc[1]) {
                            ThemePreviewActivity.this.actionBar2.setTranslationY(-this.loc[1]);
                            ThemePreviewActivity.this.page2.invalidate();
                        }
                        if (SystemClock.elapsedRealtime() < ThemePreviewActivity.this.watchForKeyboardEndTime) {
                            invalidate();
                        }
                    }
                };
                this.frameLayout = frameLayout72;
                frameLayout72.setWillNotDraw(false);
                FrameLayout frameLayout82 = this.frameLayout;
                this.fragmentView = frameLayout82;
                ViewTreeObserver viewTreeObserver2 = frameLayout82.getViewTreeObserver();
                ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener2 = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda16
                    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                    public final void onGlobalLayout() {
                        ThemePreviewActivity.this.lambda$createView$16();
                    }
                };
                this.onGlobalLayoutListener = onGlobalLayoutListener2;
                viewTreeObserver2.addOnGlobalLayoutListener(onGlobalLayoutListener2);
                ViewPager viewPager2 = new ViewPager(context);
                this.viewPager = viewPager2;
                viewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: org.telegram.ui.ThemePreviewActivity.29
                    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                    public void onPageScrollStateChanged(int i242) {
                    }

                    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                    public void onPageScrolled(int i242, float f3, int i25) {
                    }

                    @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                    public void onPageSelected(int i242) {
                        ThemePreviewActivity.this.dotsContainer.invalidate();
                    }
                });
                this.viewPager.setAdapter(new PagerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.30
                    @Override // androidx.viewpager.widget.PagerAdapter
                    public int getItemPosition(Object obj5) {
                        return -1;
                    }

                    @Override // androidx.viewpager.widget.PagerAdapter
                    public boolean isViewFromObject(View view3, Object obj5) {
                        return obj5 == view3;
                    }

                    @Override // androidx.viewpager.widget.PagerAdapter
                    public int getCount() {
                        return ThemePreviewActivity.this.screenType != 0 ? 1 : 2;
                    }

                    @Override // androidx.viewpager.widget.PagerAdapter
                    public Object instantiateItem(ViewGroup viewGroup, int i242) {
                        FrameLayout frameLayout92 = i242 == 0 ? ThemePreviewActivity.this.page2 : ThemePreviewActivity.this.page1;
                        viewGroup.addView(frameLayout92);
                        return frameLayout92;
                    }

                    @Override // androidx.viewpager.widget.PagerAdapter
                    public void destroyItem(ViewGroup viewGroup, int i242, Object obj5) {
                        viewGroup.removeView((View) obj5);
                    }
                });
                AndroidUtilities.setViewPagerEdgeEffectColor(this.viewPager, getThemedColor(Theme.key_actionBarDefault));
                this.frameLayout.addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.screenType == 0 ? 48.0f : 0.0f));
                UndoView undoView2 = new UndoView(context, this);
                this.undoView = undoView2;
                undoView2.setAdditionalTranslationY(AndroidUtilities.dp(51.0f));
                this.frameLayout.addView(this.undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
                if (this.screenType == 0) {
                }
                if (this.screenType == 1) {
                    selectColorType(2);
                }
                this.themeDescriptions = getThemeDescriptionsInternal();
                setCurrentImage(true);
                updatePlayAnimationView(false);
                if (this.showColor) {
                }
                this.scroller = new Scroller(getContext());
                iNavigationLayout = this.parentLayout;
                if (iNavigationLayout != null) {
                    this.parentLayout.getBottomSheet().fixNavigationBar(getThemedColor(Theme.key_dialogBackground));
                    if (this.screenType == 2) {
                        this.parentLayout.getBottomSheet().setOverlayNavBarColor(-16777216);
                    }
                }
                return this.fragmentView;
            }
        }
        z = false;
        this.shouldShowBrightnessControll = z;
        if (z) {
        }
        if (AndroidUtilities.isTablet()) {
        }
        this.page1 = new FrameLayout(context);
        if (this.shouldShowBrightnessControll) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    ThemePreviewActivity.this.lambda$createView$2();
                }
            }, 2000L);
        }
        this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener(this) { // from class: org.telegram.ui.ThemePreviewActivity.5
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public boolean canCollapseSearch() {
                return true;
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchCollapse() {
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchExpand() {
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onTextChanged(EditText editText) {
            }
        }).setSearchFieldHint(LocaleController.getString("Search", R.string.Search));
        this.actionBar.setBackButtonDrawable(new MenuDrawable());
        this.actionBar.setAddToContainer(false);
        this.actionBar.setTitle(LocaleController.getString("ThemePreview", R.string.ThemePreview));
        FrameLayout frameLayout10 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.6
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i122, int i132) {
                int size = View.MeasureSpec.getSize(i122);
                int size2 = View.MeasureSpec.getSize(i132);
                setMeasuredDimension(size, size2);
                measureChildWithMargins(((BaseFragment) ThemePreviewActivity.this).actionBar, i122, 0, i132, 0);
                int measuredHeight = ((BaseFragment) ThemePreviewActivity.this).actionBar.getMeasuredHeight();
                if (((BaseFragment) ThemePreviewActivity.this).actionBar.getVisibility() == 0) {
                    size2 -= measuredHeight;
                }
                ((FrameLayout.LayoutParams) ThemePreviewActivity.this.listView.getLayoutParams()).topMargin = measuredHeight;
                ThemePreviewActivity.this.listView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                measureChildWithMargins(ThemePreviewActivity.this.floatingButton, i122, 0, i132, 0);
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view3, long j) {
                boolean drawChild = super.drawChild(canvas, view3, j);
                if (view3 == ((BaseFragment) ThemePreviewActivity.this).actionBar && ((BaseFragment) ThemePreviewActivity.this).parentLayout != null) {
                    ((BaseFragment) ThemePreviewActivity.this).parentLayout.drawHeaderShadow(canvas, ((BaseFragment) ThemePreviewActivity.this).actionBar.getVisibility() == 0 ? ((BaseFragment) ThemePreviewActivity.this).actionBar.getMeasuredHeight() : 0);
                }
                return drawChild;
            }
        };
        this.page1 = frameLayout10;
        frameLayout10.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        this.page1.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        RecyclerListView recyclerListView4 = new RecyclerListView(context);
        this.listView = recyclerListView4;
        recyclerListView4.setVerticalScrollBarEnabled(true);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollbarPosition(!LocaleController.isRTL ? 1 : 2);
        this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(this.screenType == 0 ? 12.0f : 0.0f));
        this.listView.setOnItemClickListener(ThemePreviewActivity$$ExternalSyntheticLambda34.INSTANCE);
        this.page1.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        ImageView imageView6 = new ImageView(context);
        this.floatingButton = imageView6;
        imageView6.setScaleType(ImageView.ScaleType.CENTER);
        Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), getThemedColor(Theme.key_chats_actionBackground), getThemedColor(Theme.key_chats_actionPressedBackground));
        i = Build.VERSION.SDK_INT;
        if (i < 21) {
        }
        this.floatingButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable2);
        this.floatingButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chats_actionIcon), PorterDuff.Mode.MULTIPLY));
        this.floatingButton.setImageResource(R.drawable.floating_pencil);
        if (i >= 21) {
        }
        FrameLayout frameLayout22 = this.page1;
        ImageView imageView32 = this.floatingButton;
        if (i < 21) {
        }
        if (i < 21) {
        }
        boolean z32 = LocaleController.isRTL;
        frameLayout22.addView(imageView32, LayoutHelper.createFrame(i12, f2, (!z32 ? 3 : 5) | 80, !z32 ? 14.0f : 0.0f, 0.0f, !z32 ? 0.0f : 14.0f, 14.0f));
        DialogsAdapter dialogsAdapter2 = new DialogsAdapter(context);
        this.dialogsAdapter = dialogsAdapter2;
        this.listView.setAdapter(dialogsAdapter2);
        this.page2 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.8
            private boolean ignoreLayout;

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i132, int i142) {
                float f3;
                int size = View.MeasureSpec.getSize(i132);
                int size2 = View.MeasureSpec.getSize(i142);
                setMeasuredDimension(size, size2);
                if (ThemePreviewActivity.this.dropDownContainer != null) {
                    this.ignoreLayout = true;
                    if (!AndroidUtilities.isTablet()) {
                        FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.dropDownContainer.getLayoutParams();
                        layoutParams4.topMargin = Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0;
                        ThemePreviewActivity.this.dropDownContainer.setLayoutParams(layoutParams4);
                    }
                    if (AndroidUtilities.isTablet() || ApplicationLoader.applicationContext.getResources().getConfiguration().orientation != 2) {
                        ThemePreviewActivity.this.dropDown.setTextSize(1, 20.0f);
                    } else {
                        ThemePreviewActivity.this.dropDown.setTextSize(1, 18.0f);
                    }
                    this.ignoreLayout = false;
                }
                measureChildWithMargins(ThemePreviewActivity.this.actionBar2, i132, 0, i142, 0);
                int measuredHeight = ThemePreviewActivity.this.actionBar2.getMeasuredHeight();
                if (ThemePreviewActivity.this.actionBar2.getVisibility() == 0) {
                    size2 -= measuredHeight;
                }
                FrameLayout.LayoutParams layoutParams22 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.listView2.getLayoutParams();
                layoutParams22.topMargin = measuredHeight;
                int i152 = 58;
                if (ThemePreviewActivity.this.screenType == 2) {
                    RecyclerListView recyclerListView22 = ThemePreviewActivity.this.listView2;
                    int dp3 = AndroidUtilities.dp(4.0f);
                    ThemePreviewActivity themePreviewActivity = ThemePreviewActivity.this;
                    recyclerListView22.setPadding(0, dp3, 0, (AndroidUtilities.dp(((themePreviewActivity.self || themePreviewActivity.dialogId <= 0) ? 0 : 58) + 72) - 12) + (ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0));
                }
                ThemePreviewActivity.this.listView2.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - layoutParams22.bottomMargin, 1073741824));
                ((FrameLayout.LayoutParams) ThemePreviewActivity.this.backgroundImage.getLayoutParams()).topMargin = measuredHeight;
                ThemePreviewActivity.this.backgroundImage.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                if (ThemePreviewActivity.this.dimmingSliderContainer != null) {
                    ((FrameLayout.LayoutParams) ThemePreviewActivity.this.dimmingSliderContainer.getLayoutParams()).topMargin = measuredHeight;
                    ThemePreviewActivity.this.dimmingSliderContainer.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(222.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(76.0f), 1073741824));
                }
                if (ThemePreviewActivity.this.bottomOverlayChat != null) {
                    ThemePreviewActivity.this.bottomOverlayChat.setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f) + (ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0));
                    FrameLayout.LayoutParams layoutParams32 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.bottomOverlayChat.getLayoutParams();
                    ThemePreviewActivity themePreviewActivity2 = ThemePreviewActivity.this;
                    layoutParams32.height = AndroidUtilities.dp(72 + ((themePreviewActivity2.self || themePreviewActivity2.dialogId <= 0) ? 0 : 0)) + (ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0);
                    measureChildWithMargins(ThemePreviewActivity.this.bottomOverlayChat, i132, 0, i142, 0);
                }
                if (ThemePreviewActivity.this.sheetDrawable != null) {
                    ThemePreviewActivity.this.sheetDrawable.getPadding(AndroidUtilities.rectTmp2);
                }
                int i162 = 0;
                while (i162 < ThemePreviewActivity.this.patternLayout.length) {
                    if (ThemePreviewActivity.this.patternLayout[i162] != null) {
                        FrameLayout.LayoutParams layoutParams42 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.patternLayout[i162].getLayoutParams();
                        if (i162 == 0) {
                            f3 = ThemePreviewActivity.this.screenType == 2 ? 321 : 273;
                        } else {
                            f3 = 316.0f;
                        }
                        layoutParams42.height = AndroidUtilities.dp(f3);
                        if (ThemePreviewActivity.this.insideBottomSheet()) {
                            layoutParams42.height += AndroidUtilities.navigationBarHeight;
                        }
                        if (i162 == 0) {
                            layoutParams42.height += AndroidUtilities.dp(12.0f) + AndroidUtilities.rectTmp2.top;
                        }
                        ThemePreviewActivity.this.patternLayout[i162].setPadding(0, i162 == 0 ? AndroidUtilities.dp(12.0f) + AndroidUtilities.rectTmp2.top : 0, 0, ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0);
                        measureChildWithMargins(ThemePreviewActivity.this.patternLayout[i162], i132, 0, i142, 0);
                    }
                    i162++;
                }
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view3, long j) {
                boolean drawChild = super.drawChild(canvas, view3, j);
                if (view3 == ThemePreviewActivity.this.actionBar2 && ((BaseFragment) ThemePreviewActivity.this).parentLayout != null) {
                    ((BaseFragment) ThemePreviewActivity.this).parentLayout.drawHeaderShadow(canvas, ThemePreviewActivity.this.actionBar2.getVisibility() == 0 ? (int) (ThemePreviewActivity.this.actionBar2.getMeasuredHeight() + ThemePreviewActivity.this.actionBar2.getTranslationY()) : 0);
                }
                return drawChild;
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        this.messagesAdapter = new MessagesAdapter(context);
        this.actionBar2 = createActionBar(context);
        if (AndroidUtilities.isTablet()) {
        }
        this.actionBar2.setBackButtonDrawable(new BackDrawable(false));
        this.actionBar2.setActionBarMenuOnItemClick(new 9());
        while (i2 < 2) {
        }
        BackgroundView backgroundView2 = this.backgroundImages[0];
        this.backgroundImage = backgroundView2;
        backgroundView2.setVisibility(0);
        this.backgroundImages[1].setVisibility(8);
        if (this.screenType == 2) {
        }
        if (!this.messagesAdapter.showSecretMessages) {
        }
        i3 = 2;
        this.listView2 = new RecyclerListView(context) { // from class: org.telegram.ui.ThemePreviewActivity.11
            boolean scrollingBackground;
            float startX;

            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view3, long j) {
                RecyclerView.ViewHolder childViewHolder;
                boolean drawChild = super.drawChild(canvas, view3, j);
                if (view3 instanceof ChatMessageCell) {
                    ChatMessageCell chatMessageCell = (ChatMessageCell) view3;
                    chatMessageCell.getMessageObject();
                    ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                    if (avatarImage != null) {
                        int top = view3.getTop();
                        if (chatMessageCell.isPinnedBottom() && (childViewHolder = ThemePreviewActivity.this.listView2.getChildViewHolder(view3)) != null) {
                            if (ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder.getAdapterPosition() - 1) != null) {
                                avatarImage.setImageY(-AndroidUtilities.dp(1000.0f));
                                avatarImage.draw(canvas);
                                return drawChild;
                            }
                        }
                        float translationX = chatMessageCell.getTranslationX();
                        int top2 = view3.getTop() + chatMessageCell.getLayoutHeight();
                        int measuredHeight = ThemePreviewActivity.this.listView2.getMeasuredHeight() - ThemePreviewActivity.this.listView2.getPaddingBottom();
                        if (top2 > measuredHeight) {
                            top2 = measuredHeight;
                        }
                        if (chatMessageCell.isPinnedTop() && (childViewHolder2 = ThemePreviewActivity.this.listView2.getChildViewHolder(view3)) != null) {
                            int i172 = 0;
                            while (i172 < 20) {
                                i172++;
                                RecyclerView.ViewHolder childViewHolder2 = ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder2.getAdapterPosition() + 1);
                                if (childViewHolder2 == null) {
                                    break;
                                }
                                top = childViewHolder2.itemView.getTop();
                                if (top2 - AndroidUtilities.dp(48.0f) < childViewHolder2.itemView.getBottom()) {
                                    translationX = Math.min(childViewHolder2.itemView.getTranslationX(), translationX);
                                }
                                View view22 = childViewHolder2.itemView;
                                if (!(view22 instanceof ChatMessageCell)) {
                                    break;
                                } else if (!((ChatMessageCell) view22).isPinnedTop()) {
                                    break;
                                }
                            }
                        }
                        if (top2 - AndroidUtilities.dp(48.0f) < top) {
                            top2 = top + AndroidUtilities.dp(48.0f);
                        }
                        if (translationX != 0.0f) {
                            canvas.save();
                            canvas.translate(translationX, 0.0f);
                        }
                        avatarImage.setImageY(top2 - AndroidUtilities.dp(44.0f));
                        avatarImage.draw(canvas);
                        if (translationX != 0.0f) {
                            canvas.restore();
                        }
                    }
                }
                return drawChild;
            }

            @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
            public void setTranslationY(float f3) {
                super.setTranslationY(f3);
                if (ThemePreviewActivity.this.backgroundCheckBoxView != null) {
                    for (int i172 = 0; i172 < ThemePreviewActivity.this.backgroundCheckBoxView.length; i172++) {
                        ThemePreviewActivity.this.backgroundCheckBoxView[i172].invalidate();
                    }
                }
                if (ThemePreviewActivity.this.messagesCheckBoxView != null) {
                    for (int i182 = 0; i182 < ThemePreviewActivity.this.messagesCheckBoxView.length; i182++) {
                        ThemePreviewActivity.this.messagesCheckBoxView[i182].invalidate();
                    }
                }
                if (ThemePreviewActivity.this.backgroundPlayAnimationView != null) {
                    ThemePreviewActivity.this.backgroundPlayAnimationView.invalidate();
                }
                if (ThemePreviewActivity.this.messagesPlayAnimationView != null) {
                    ThemePreviewActivity.this.messagesPlayAnimationView.invalidate();
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView
            public void onChildPressed(View view3, float f3, float f4, boolean z4) {
                if (z4 && (view3 instanceof ChatMessageCell) && !((ChatMessageCell) view3).isInsideBackground(f3, f4)) {
                    return;
                }
                super.onChildPressed(view3, f3, f4, z4);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView
            public boolean allowSelectChildAtPosition(View view3) {
                RecyclerView.ViewHolder findContainingViewHolder = ThemePreviewActivity.this.listView2.findContainingViewHolder(view3);
                if (findContainingViewHolder == null || findContainingViewHolder.getItemViewType() != 2) {
                    return super.allowSelectChildAtPosition(view3);
                }
                return false;
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                checkMotionEvent(motionEvent);
                if (ThemePreviewActivity.this.hasScrollingBackground) {
                    if (motionEvent.getAction() == 0) {
                        this.startX = motionEvent.getX();
                        motionEvent.getY();
                        if (getParent() != null) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        this.scrollingBackground = true;
                    } else if (motionEvent.getAction() == 2) {
                        if (!this.scrollingBackground && Math.abs(this.startX - motionEvent.getX()) > AndroidUtilities.touchSlop) {
                            if (getParent() != null) {
                                getParent().requestDisallowInterceptTouchEvent(true);
                            }
                            this.scrollingBackground = true;
                        }
                    } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                        this.scrollingBackground = false;
                        if (getParent() != null) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                    ThemePreviewActivity.this.gestureDetector2.onTouchEvent(motionEvent);
                }
                return this.scrollingBackground || super.onTouchEvent(motionEvent);
            }

            private void checkMotionEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    if (!ThemePreviewActivity.this.wasScroll && (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) && ThemePreviewActivity.this.patternLayout[0].getVisibility() == 0) {
                        ThemePreviewActivity.this.showPatternsView(0, false, true);
                    }
                    ThemePreviewActivity.this.wasScroll = false;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
            public void onLayout(boolean z4, int i172, int i182, int i192, int i202) {
                super.onLayout(z4, i172, i182, i192, i202);
                ThemePreviewActivity.this.invalidateBlur();
            }
        };
        DefaultItemAnimator defaultItemAnimator22 = new DefaultItemAnimator() { // from class: org.telegram.ui.ThemePreviewActivity.12
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            public void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                ThemePreviewActivity.this.listView2.invalidateViews();
            }
        };
        defaultItemAnimator22.setDelayAnimations(false);
        this.listView2.setItemAnimator(defaultItemAnimator22);
        this.listView2.setVerticalScrollBarEnabled(true);
        this.listView2.setOverScrollMode(i3);
        i5 = this.screenType;
        if (i5 == i3) {
        }
        this.listView2.setClipToPadding(false);
        this.listView2.setLayoutManager(new LinearLayoutManager(context, 1, true));
        this.listView2.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        if (this.screenType == 1) {
        }
        this.listView2.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ThemePreviewActivity.13
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i172, int i182) {
                ThemePreviewActivity.this.listView2.invalidateViews();
                ThemePreviewActivity.this.wasScroll = true;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i172) {
                if (i172 == 0) {
                    ThemePreviewActivity.this.wasScroll = false;
                }
            }
        });
        this.page2.addView(this.actionBar2, LayoutHelper.createFrame(i6, -2.0f));
        WallpaperParallaxEffect wallpaperParallaxEffect22 = new WallpaperParallaxEffect(context);
        this.parallaxEffect = wallpaperParallaxEffect22;
        wallpaperParallaxEffect22.setCallback(new WallpaperParallaxEffect.Callback() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda36
            @Override // org.telegram.ui.Components.WallpaperParallaxEffect.Callback
            public final void onOffsetsChanged(int i172, int i182, float f3) {
                ThemePreviewActivity.this.lambda$createView$7(i172, i182, f3);
            }
        });
        i7 = this.screenType;
        if (i7 != 1) {
        }
        if (i7 == i3) {
        }
        final Rect rect32 = new Rect();
        Drawable mutate322 = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
        this.sheetDrawable = mutate322;
        mutate322.getPadding(rect32);
        this.sheetDrawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundWhite), PorterDuff.Mode.MULTIPLY));
        TextPaint textPaint22 = new TextPaint(1);
        textPaint22.setTextSize(AndroidUtilities.dp(14.0f));
        textPaint22.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        obj = this.currentWallpaper;
        if (!(obj instanceof WallpapersListActivity.EmojiWallpaper)) {
        }
        if (this.screenType == 1) {
        }
        if (this.screenType != 1) {
        }
        this.isBlurred = false;
        i8 = 0;
        while (i8 < 2) {
        }
        updateButtonState(false, false);
        if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
        }
        if (this.screenType != 1) {
        }
        this.listView2.setAdapter(this.messagesAdapter);
        FrameLayout frameLayout722 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.28
            private int[] loc = new int[2];

            @Override // android.view.View
            public void invalidate() {
                super.invalidate();
                if (ThemePreviewActivity.this.page2 != null) {
                    ThemePreviewActivity.this.page2.invalidate();
                }
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                if (AndroidUtilities.usingHardwareInput) {
                    return;
                }
                getLocationInWindow(this.loc);
                if (Build.VERSION.SDK_INT < 21 && !AndroidUtilities.isTablet()) {
                    int[] iArr4 = this.loc;
                    iArr4[1] = iArr4[1] - AndroidUtilities.statusBarHeight;
                }
                if (ThemePreviewActivity.this.actionBar2.getTranslationY() != this.loc[1]) {
                    ThemePreviewActivity.this.actionBar2.setTranslationY(-this.loc[1]);
                    ThemePreviewActivity.this.page2.invalidate();
                }
                if (SystemClock.elapsedRealtime() < ThemePreviewActivity.this.watchForKeyboardEndTime) {
                    invalidate();
                }
            }
        };
        this.frameLayout = frameLayout722;
        frameLayout722.setWillNotDraw(false);
        FrameLayout frameLayout822 = this.frameLayout;
        this.fragmentView = frameLayout822;
        ViewTreeObserver viewTreeObserver22 = frameLayout822.getViewTreeObserver();
        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener22 = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda16
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public final void onGlobalLayout() {
                ThemePreviewActivity.this.lambda$createView$16();
            }
        };
        this.onGlobalLayoutListener = onGlobalLayoutListener22;
        viewTreeObserver22.addOnGlobalLayoutListener(onGlobalLayoutListener22);
        ViewPager viewPager22 = new ViewPager(context);
        this.viewPager = viewPager22;
        viewPager22.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: org.telegram.ui.ThemePreviewActivity.29
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i242) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i242, float f3, int i25) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i242) {
                ThemePreviewActivity.this.dotsContainer.invalidate();
            }
        });
        this.viewPager.setAdapter(new PagerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.30
            @Override // androidx.viewpager.widget.PagerAdapter
            public int getItemPosition(Object obj5) {
                return -1;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public boolean isViewFromObject(View view3, Object obj5) {
                return obj5 == view3;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getCount() {
                return ThemePreviewActivity.this.screenType != 0 ? 1 : 2;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public Object instantiateItem(ViewGroup viewGroup, int i242) {
                FrameLayout frameLayout92 = i242 == 0 ? ThemePreviewActivity.this.page2 : ThemePreviewActivity.this.page1;
                viewGroup.addView(frameLayout92);
                return frameLayout92;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public void destroyItem(ViewGroup viewGroup, int i242, Object obj5) {
                viewGroup.removeView((View) obj5);
            }
        });
        AndroidUtilities.setViewPagerEdgeEffectColor(this.viewPager, getThemedColor(Theme.key_actionBarDefault));
        this.frameLayout.addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.screenType == 0 ? 48.0f : 0.0f));
        UndoView undoView22 = new UndoView(context, this);
        this.undoView = undoView22;
        undoView22.setAdditionalTranslationY(AndroidUtilities.dp(51.0f));
        this.frameLayout.addView(this.undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        if (this.screenType == 0) {
        }
        if (this.screenType == 1) {
        }
        this.themeDescriptions = getThemeDescriptionsInternal();
        setCurrentImage(true);
        updatePlayAnimationView(false);
        if (this.showColor) {
        }
        this.scroller = new Scroller(getContext());
        iNavigationLayout = this.parentLayout;
        if (iNavigationLayout != null) {
        }
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2() {
        if (getParentActivity() == null || getContext() == null) {
            return;
        }
        SharedConfig.increaseDayNightWallpaperSiwtchHint();
        HintView hintView = new HintView(getContext(), 7, true);
        hintView.setAlpha(0.0f);
        hintView.setVisibility(4);
        hintView.setShowingDuration(4000L);
        this.frameLayout.addView(hintView, LayoutHelper.createFrame(-2, -2.0f, 51, 4.0f, 0.0f, 4.0f, 0.0f));
        if (this.onSwitchDayNightDelegate.isDark()) {
            hintView.setText(LocaleController.getString("PreviewWallpaperDay", R.string.PreviewWallpaperDay));
        } else {
            hintView.setText(LocaleController.getString("PreviewWallpaperNight", R.string.PreviewWallpaperNight));
        }
        hintView.setBackgroundColor(-366530760, -1);
        hintView.showForView(this.dayNightItem, true);
        hintView.setExtraTranslationY(-AndroidUtilities.dp(14.0f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 9 extends ActionBar.ActionBarMenuOnItemClick {
        9() {
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            File file;
            Theme.ThemeAccent accent;
            String url;
            String str;
            int i2 = 0;
            if (i == -1) {
                if (ThemePreviewActivity.this.checkDiscard()) {
                    ThemePreviewActivity.this.cancelThemeApply(false);
                }
            } else if (i >= 1 && i <= 3) {
                ThemePreviewActivity.this.selectColorType(i);
            } else if (i == 4) {
                if (ThemePreviewActivity.this.removeBackgroundOverride) {
                    Theme.resetCustomWallpaper(false);
                }
                File pathToWallpaper = ThemePreviewActivity.this.accent.getPathToWallpaper();
                if (pathToWallpaper != null) {
                    pathToWallpaper.delete();
                }
                ThemePreviewActivity.this.accent.patternSlug = ThemePreviewActivity.this.selectedPattern != null ? ThemePreviewActivity.this.selectedPattern.slug : "";
                ThemePreviewActivity.this.accent.patternIntensity = ThemePreviewActivity.this.currentIntensity;
                ThemePreviewActivity.this.accent.patternMotion = ThemePreviewActivity.this.isMotion;
                if (((int) ThemePreviewActivity.this.accent.backgroundOverrideColor) == 0) {
                    ThemePreviewActivity.this.accent.backgroundOverrideColor = 4294967296L;
                }
                if (((int) ThemePreviewActivity.this.accent.backgroundGradientOverrideColor1) == 0) {
                    ThemePreviewActivity.this.accent.backgroundGradientOverrideColor1 = 4294967296L;
                }
                if (((int) ThemePreviewActivity.this.accent.backgroundGradientOverrideColor2) == 0) {
                    ThemePreviewActivity.this.accent.backgroundGradientOverrideColor2 = 4294967296L;
                }
                if (((int) ThemePreviewActivity.this.accent.backgroundGradientOverrideColor3) == 0) {
                    ThemePreviewActivity.this.accent.backgroundGradientOverrideColor3 = 4294967296L;
                }
                ThemePreviewActivity.this.saveAccentWallpaper();
                NotificationCenter.getGlobalInstance().removeObserver(ThemePreviewActivity.this, NotificationCenter.wallpapersDidLoad);
                Theme.saveThemeAccents(ThemePreviewActivity.this.applyingTheme, true, false, false, true);
                Theme.applyPreviousTheme();
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, ThemePreviewActivity.this.applyingTheme, Boolean.valueOf(ThemePreviewActivity.this.nightTheme), null, -1);
                ThemePreviewActivity.this.finishFragment();
            } else if (i == 5) {
                if (ThemePreviewActivity.this.getParentActivity() == null) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                if (ThemePreviewActivity.this.isBlurred) {
                    sb.append("blur");
                }
                if (ThemePreviewActivity.this.isMotion) {
                    if (sb.length() > 0) {
                        sb.append("+");
                    }
                    sb.append("motion");
                }
                if (!(ThemePreviewActivity.this.currentWallpaper instanceof TLRPC$TL_wallPaper)) {
                    if (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                        WallpapersListActivity.ColorWallpaper colorWallpaper = new WallpapersListActivity.ColorWallpaper(ThemePreviewActivity.this.selectedPattern != null ? ThemePreviewActivity.this.selectedPattern.slug : "c", ThemePreviewActivity.this.backgroundColor, ThemePreviewActivity.this.backgroundGradientColor1, ThemePreviewActivity.this.backgroundGradientColor2, ThemePreviewActivity.this.backgroundGradientColor3, ThemePreviewActivity.this.backgroundRotation, ThemePreviewActivity.this.currentIntensity, ThemePreviewActivity.this.isMotion, null);
                        colorWallpaper.pattern = ThemePreviewActivity.this.selectedPattern;
                        url = colorWallpaper.getUrl();
                    } else if (!BuildVars.DEBUG_PRIVATE_VERSION || (accent = Theme.getActiveTheme().getAccent(false)) == null) {
                        return;
                    } else {
                        WallpapersListActivity.ColorWallpaper colorWallpaper2 = new WallpapersListActivity.ColorWallpaper(accent.patternSlug, (int) accent.backgroundOverrideColor, (int) accent.backgroundGradientOverrideColor1, (int) accent.backgroundGradientOverrideColor2, (int) accent.backgroundGradientOverrideColor3, accent.backgroundRotation, accent.patternIntensity, accent.patternMotion, null);
                        int size = ThemePreviewActivity.this.patterns.size();
                        while (true) {
                            if (i2 >= size) {
                                break;
                            }
                            TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) ThemePreviewActivity.this.patterns.get(i2);
                            if (tLRPC$TL_wallPaper.pattern && accent.patternSlug.equals(tLRPC$TL_wallPaper.slug)) {
                                colorWallpaper2.pattern = tLRPC$TL_wallPaper;
                                break;
                            }
                            i2++;
                        }
                        url = colorWallpaper2.getUrl();
                    }
                    str = url;
                } else {
                    String str2 = "https://" + MessagesController.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).linkPrefix + "/bg/" + ((TLRPC$TL_wallPaper) ThemePreviewActivity.this.currentWallpaper).slug;
                    if (sb.length() > 0) {
                        str2 = str2 + "?mode=" + sb.toString();
                    }
                    str = str2;
                }
                ThemePreviewActivity.this.showDialog(new ShareAlert(ThemePreviewActivity.this.getParentActivity(), null, str, false, str, false) { // from class: org.telegram.ui.ThemePreviewActivity.9.1
                    @Override // org.telegram.ui.Components.ShareAlert
                    protected void onSend(LongSparseArray<TLRPC$Dialog> longSparseArray, int i3, TLRPC$TL_forumTopic tLRPC$TL_forumTopic) {
                        if (longSparseArray.size() == 1) {
                            ThemePreviewActivity.this.undoView.showWithAction(longSparseArray.valueAt(0).id, 61, Integer.valueOf(i3));
                        } else {
                            ThemePreviewActivity.this.undoView.showWithAction(0L, 61, Integer.valueOf(i3), Integer.valueOf(longSparseArray.size()), (Runnable) null, (Runnable) null);
                        }
                    }
                });
            } else if (i != 6) {
                if (i == 7 && (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.FileWallpaper) && (file = ((WallpapersListActivity.FileWallpaper) ThemePreviewActivity.this.currentWallpaper).originalPath) != null) {
                    final MediaController.PhotoEntry photoEntry = new MediaController.PhotoEntry(0, 0, 0L, file.getAbsolutePath(), 0, false, 0, 0, 0L);
                    photoEntry.isVideo = false;
                    photoEntry.thumbPath = null;
                    ArrayList<Object> arrayList = new ArrayList<>();
                    arrayList.add(photoEntry);
                    PhotoViewer.getInstance().setParentActivity(ThemePreviewActivity.this.getParentActivity());
                    PhotoViewer.getInstance().openPhotoForSelect(arrayList, 0, 3, false, new PhotoViewer.EmptyPhotoViewerProvider() { // from class: org.telegram.ui.ThemePreviewActivity.9.3
                        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
                        public boolean allowCaption() {
                            return false;
                        }

                        @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
                        public void sendButtonPressed(int i3, VideoEditedInfo videoEditedInfo, boolean z, int i4, boolean z2) {
                            if (photoEntry.imagePath != null) {
                                File directory = FileLoader.getDirectory(4);
                                File file2 = new File(directory, Utilities.random.nextInt() + ".jpg");
                                Point realScreenSize = AndroidUtilities.getRealScreenSize();
                                Bitmap loadBitmap = ImageLoader.loadBitmap(photoEntry.imagePath, null, (float) realScreenSize.x, (float) realScreenSize.y, true);
                                try {
                                    loadBitmap.compress(Bitmap.CompressFormat.JPEG, 87, new FileOutputStream(file2));
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                File file3 = new File(photoEntry.imagePath);
                                ThemePreviewActivity.this.currentWallpaper = new WallpapersListActivity.FileWallpaper("", file3, file3);
                                ThemePreviewActivity.this.currentWallpaperBitmap = loadBitmap;
                                ThemePreviewActivity.this.lastSizeHash = 0;
                                ThemePreviewActivity.this.backgroundImage.requestLayout();
                                ThemePreviewActivity.this.setCurrentImage(false);
                                ThemePreviewActivity.this.blurredBitmap = null;
                                ThemePreviewActivity.this.updateBlurred();
                            }
                        }
                    }, null);
                }
            } else {
                if (SharedConfig.dayNightWallpaperSwitchHint <= 3) {
                    SharedConfig.dayNightWallpaperSwitchHint = 10;
                    SharedConfig.increaseDayNightWallpaperSiwtchHint();
                }
                boolean isDark = ThemePreviewActivity.this.onSwitchDayNightDelegate.isDark();
                DayNightSwitchDelegate dayNightSwitchDelegate = ThemePreviewActivity.this.onSwitchDayNightDelegate;
                if (dayNightSwitchDelegate != null) {
                    if (!dayNightSwitchDelegate.supportsAnimation()) {
                        ThemePreviewActivity.this.toggleTheme();
                        return;
                    }
                    ThemePreviewActivity.this.onSwitchDayNightDelegate.switchDayNight(true);
                    ThemePreviewActivity.this.sunDrawable.setPlayInDirectionOfCustomEndFrame(true);
                    if (isDark) {
                        ThemePreviewActivity.this.sunDrawable.setCustomEndFrame(0);
                    } else {
                        ThemePreviewActivity.this.sunDrawable.setCustomEndFrame(36);
                    }
                    ThemePreviewActivity.this.sunDrawable.start();
                    if (ThemePreviewActivity.this.shouldShowBrightnessControll) {
                        DayNightSwitchDelegate dayNightSwitchDelegate2 = ThemePreviewActivity.this.onSwitchDayNightDelegate;
                        if (dayNightSwitchDelegate2 == null || !dayNightSwitchDelegate2.isDark()) {
                            ThemePreviewActivity.this.dimmingSlider.animateValueTo(0.0f);
                        } else {
                            ThemePreviewActivity.this.dimmingSlider.setVisibility(0);
                            ThemePreviewActivity.this.dimmingSlider.animateValueTo(ThemePreviewActivity.this.dimAmount);
                        }
                        if (ThemePreviewActivity.this.changeDayNightViewAnimator2 != null) {
                            ThemePreviewActivity.this.changeDayNightViewAnimator2.removeAllListeners();
                            ThemePreviewActivity.this.changeDayNightViewAnimator2.cancel();
                        }
                        ThemePreviewActivity themePreviewActivity = ThemePreviewActivity.this;
                        float[] fArr = new float[2];
                        fArr[0] = themePreviewActivity.progressToDarkTheme;
                        fArr[1] = ThemePreviewActivity.this.onSwitchDayNightDelegate.isDark() ? 1.0f : 0.0f;
                        themePreviewActivity.changeDayNightViewAnimator2 = ValueAnimator.ofFloat(fArr);
                        ThemePreviewActivity.this.changeDayNightViewAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ThemePreviewActivity$9$$ExternalSyntheticLambda0
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                ThemePreviewActivity.9.this.lambda$onItemClick$0(valueAnimator);
                            }
                        });
                        ThemePreviewActivity.this.changeDayNightViewAnimator2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.9.2
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                if (ThemePreviewActivity.this.onSwitchDayNightDelegate.isDark()) {
                                    return;
                                }
                                ThemePreviewActivity.this.dimmingSlider.setVisibility(8);
                            }
                        });
                        ThemePreviewActivity.this.changeDayNightViewAnimator2.setDuration(250L);
                        ThemePreviewActivity.this.changeDayNightViewAnimator2.setInterpolator(CubicBezierInterpolator.DEFAULT);
                        ThemePreviewActivity.this.changeDayNightViewAnimator2.start();
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(ValueAnimator valueAnimator) {
            ThemePreviewActivity.this.progressToDarkTheme = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            ThemePreviewActivity.this.backgroundImage.invalidate();
            ThemePreviewActivity.this.bottomOverlayChat.invalidate();
            ThemePreviewActivity.this.dimmingSlider.setAlpha(ThemePreviewActivity.this.progressToDarkTheme);
            ThemePreviewActivity.this.dimmingSliderContainer.invalidate();
            ThemePreviewActivity.this.invalidateBlur();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
            return;
        }
        Drawable drawable = imageReceiver.getDrawable();
        if (!z || drawable == null) {
            return;
        }
        this.themeDelegate.applyChatServiceMessageColor(AndroidUtilities.calcDrawableColor(drawable), checkBlur(drawable), drawable, Float.valueOf(this.currentIntensity));
        if (!z2 && this.isBlurred && this.blurredBitmap == null) {
            this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(false);
            updateBlurred();
            this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(true);
        }
        invalidateBlur();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(View view) {
        this.dropDownContainer.toggleSubMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(View view, int i, float f, float f2) {
        if (view instanceof ChatMessageCell) {
            ChatMessageCell chatMessageCell = (ChatMessageCell) view;
            if (chatMessageCell.isInsideBackground(f, f2)) {
                if (chatMessageCell.getMessageObject().isOutOwner()) {
                    selectColorType(3);
                    return;
                } else {
                    selectColorType(1);
                    return;
                }
            }
            selectColorType(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(int i, int i2, float f) {
        if (this.isMotion) {
            this.backgroundImage.getBackground();
            float scaleX = this.motionAnimation != null ? (this.backgroundImage.getScaleX() - 1.0f) / (this.parallaxScale - 1.0f) : 1.0f;
            this.backgroundImage.setTranslationX(i * scaleX);
            this.backgroundImage.setTranslationY(i2 * scaleX);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(View view) {
        applyWallpaperBackground(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(View view) {
        applyWallpaperBackground(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(Float f) {
        this.dimAmount = f.floatValue();
        this.backgroundImage.invalidate();
        invalidateBlur();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$11(int i, WallpaperCheckBoxView wallpaperCheckBoxView, View view) {
        if (this.backgroundButtonsContainer.getAlpha() == 1.0f && this.patternViewAnimation == null) {
            int i2 = this.screenType;
            if ((i2 == 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) && i == 2) {
                wallpaperCheckBoxView.setChecked(!wallpaperCheckBoxView.isChecked(), true);
                boolean isChecked = wallpaperCheckBoxView.isChecked();
                this.isMotion = isChecked;
                this.parallaxEffect.setEnabled(isChecked);
                animateMotionChange();
                return;
            }
            if (i == 1 && (i2 == 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper))) {
                if (this.backgroundCheckBoxView[1].isChecked()) {
                    this.lastSelectedPattern = this.selectedPattern;
                    this.backgroundImage.setImageDrawable(null);
                    this.selectedPattern = null;
                    this.isMotion = false;
                    updateButtonState(false, true);
                    animateMotionChange();
                    if (this.patternLayout[1].getVisibility() == 0) {
                        if (this.screenType == 1) {
                            showPatternsView(0, true, true);
                        } else {
                            showPatternsView(i, this.patternLayout[i].getVisibility() != 0, true);
                        }
                    }
                } else {
                    selectPattern(this.lastSelectedPattern != null ? -1 : 0);
                    if (this.screenType == 1) {
                        showPatternsView(1, true, true);
                    } else {
                        showPatternsView(i, this.patternLayout[i].getVisibility() != 0, true);
                    }
                }
                this.backgroundCheckBoxView[1].setChecked(this.selectedPattern != null, true);
                updateSelectedPattern(true);
                this.patternsListView.invalidateViews();
                updateMotionButton();
            } else if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                showPatternsView(i, this.patternLayout[i].getVisibility() != 0, true);
            } else if (i2 != 1) {
                wallpaperCheckBoxView.setChecked(!wallpaperCheckBoxView.isChecked(), true);
                if (i == 0) {
                    boolean isChecked2 = wallpaperCheckBoxView.isChecked();
                    this.isBlurred = isChecked2;
                    if (isChecked2) {
                        this.backgroundImage.getImageReceiver().setForceCrossfade(true);
                    }
                    updateBlurred();
                    return;
                }
                boolean isChecked3 = wallpaperCheckBoxView.isChecked();
                this.isMotion = isChecked3;
                this.parallaxEffect.setEnabled(isChecked3);
                animateMotionChange();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$12(int i, WallpaperCheckBoxView wallpaperCheckBoxView, View view) {
        if (this.messagesButtonsContainer.getAlpha() == 1.0f && i == 0) {
            wallpaperCheckBoxView.setChecked(!wallpaperCheckBoxView.isChecked(), true);
            this.accent.myMessagesAnimated = wallpaperCheckBoxView.isChecked();
            Theme.refreshThemeColors(true, true);
            this.listView2.invalidateViews();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$13(int i, View view) {
        if (this.patternViewAnimation != null) {
            return;
        }
        if (i == 0) {
            this.backgroundRotation = this.previousBackgroundRotation;
            setBackgroundColor(this.previousBackgroundGradientColor3, 3, true, true);
            setBackgroundColor(this.previousBackgroundGradientColor2, 2, true, true);
            setBackgroundColor(this.previousBackgroundGradientColor1, 1, true, true);
            setBackgroundColor(this.previousBackgroundColor, 0, true, true);
        } else {
            TLRPC$TL_wallPaper tLRPC$TL_wallPaper = this.previousSelectedPattern;
            this.selectedPattern = tLRPC$TL_wallPaper;
            if (tLRPC$TL_wallPaper == null) {
                this.backgroundImage.setImageDrawable(null);
            } else {
                BackgroundView backgroundView = this.backgroundImage;
                ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$TL_wallPaper.document);
                String str = this.imageFilter;
                TLRPC$TL_wallPaper tLRPC$TL_wallPaper2 = this.selectedPattern;
                backgroundView.setImage(forDocument, str, null, null, "jpg", tLRPC$TL_wallPaper2.document.size, 1, tLRPC$TL_wallPaper2);
            }
            this.backgroundCheckBoxView[1].setChecked(this.selectedPattern != null, false);
            float f = this.previousIntensity;
            this.currentIntensity = f;
            this.intensitySeekBar.setProgress(f);
            this.backgroundImage.getImageReceiver().setAlpha(this.currentIntensity);
            updateButtonState(false, true);
            updateSelectedPattern(true);
        }
        if (this.screenType == 2) {
            showPatternsView(i, false, true);
            return;
        }
        if (this.selectedPattern == null) {
            if (this.isMotion) {
                this.isMotion = false;
                this.backgroundCheckBoxView[0].setChecked(false, true);
                animateMotionChange();
            }
            updateMotionButton();
        }
        showPatternsView(0, true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$14(int i, View view) {
        if (this.patternViewAnimation != null) {
            return;
        }
        if (this.screenType == 2) {
            showPatternsView(i, false, true);
        } else {
            showPatternsView(0, true, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$15(View view, int i) {
        boolean z = this.selectedPattern != null;
        selectPattern(i);
        if (z == (this.selectedPattern == null)) {
            animateMotionChange();
            updateMotionButton();
        }
        updateSelectedPattern(true);
        this.backgroundCheckBoxView[1].setChecked(this.selectedPattern != null, true);
        this.patternsListView.invalidateViews();
        int left = view.getLeft();
        int right = view.getRight();
        int dp = AndroidUtilities.dp(52.0f);
        int i2 = left - dp;
        if (i2 < 0) {
            this.patternsListView.smoothScrollBy(i2, 0);
            return;
        }
        int i3 = right + dp;
        if (i3 > this.patternsListView.getMeasuredWidth()) {
            RecyclerListView recyclerListView = this.patternsListView;
            recyclerListView.smoothScrollBy(i3 - recyclerListView.getMeasuredWidth(), 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 27 implements ColorPicker.ColorPickerDelegate {
        27() {
        }

        @Override // org.telegram.ui.Components.ColorPicker.ColorPickerDelegate
        public void setColor(int i, int i2, boolean z) {
            if (ThemePreviewActivity.this.screenType == 2) {
                ThemePreviewActivity.this.setBackgroundColor(i, i2, z, true);
            } else {
                ThemePreviewActivity.this.scheduleApplyColor(i, i2, z);
            }
        }

        @Override // org.telegram.ui.Components.ColorPicker.ColorPickerDelegate
        public void openThemeCreate(boolean z) {
            if (z) {
                if (ThemePreviewActivity.this.accent.info == null) {
                    ThemePreviewActivity.this.finishFragment();
                    MessagesController.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).saveThemeToServer(ThemePreviewActivity.this.accent.parentTheme, ThemePreviewActivity.this.accent);
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needShareTheme, ThemePreviewActivity.this.accent.parentTheme, ThemePreviewActivity.this.accent);
                    return;
                }
                String str = "https://" + MessagesController.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).linkPrefix + "/addtheme/" + ThemePreviewActivity.this.accent.info.slug;
                ThemePreviewActivity.this.showDialog(new ShareAlert(ThemePreviewActivity.this.getParentActivity(), null, str, false, str, false));
                return;
            }
            AlertsCreator.createThemeCreateDialog(ThemePreviewActivity.this, 1, null, null);
        }

        @Override // org.telegram.ui.Components.ColorPicker.ColorPickerDelegate
        public void deleteTheme() {
            if (ThemePreviewActivity.this.getParentActivity() == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ThemePreviewActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString("DeleteThemeTitle", R.string.DeleteThemeTitle));
            builder.setMessage(LocaleController.getString("DeleteThemeAlert", R.string.DeleteThemeAlert));
            builder.setPositiveButton(LocaleController.getString("Delete", R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$27$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ThemePreviewActivity.27.this.lambda$deleteTheme$0(dialogInterface, i);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog create = builder.create();
            ThemePreviewActivity.this.showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(ThemePreviewActivity.this.getThemedColor(Theme.key_text_RedBold));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$deleteTheme$0(DialogInterface dialogInterface, int i) {
            Theme.deleteThemeAccent(ThemePreviewActivity.this.applyingTheme, ThemePreviewActivity.this.accent, true);
            Theme.applyPreviousTheme();
            Theme.refreshThemeColors();
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, ThemePreviewActivity.this.applyingTheme, Boolean.valueOf(ThemePreviewActivity.this.nightTheme), null, -1);
            ThemePreviewActivity.this.finishFragment();
        }

        @Override // org.telegram.ui.Components.ColorPicker.ColorPickerDelegate
        public int getDefaultColor(int i) {
            Theme.ThemeAccent themeAccent;
            if (ThemePreviewActivity.this.colorType == 3 && ThemePreviewActivity.this.applyingTheme.firstAccentIsDefault && i == 0 && (themeAccent = ThemePreviewActivity.this.applyingTheme.themeAccentsMap.get(Theme.DEFALT_THEME_ACCENT_ID)) != null) {
                return themeAccent.myMessagesAccentColor;
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$16() {
        this.watchForKeyboardEndTime = SystemClock.elapsedRealtime() + 1500;
        this.frameLayout.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$17(View view) {
        cancelThemeApply(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$18(View view) {
        Theme.ThemeAccent accent;
        Theme.ThemeInfo previousTheme = Theme.getPreviousTheme();
        if (previousTheme == null) {
            return;
        }
        int i = previousTheme.prevAccentId;
        if (i >= 0) {
            accent = previousTheme.themeAccentsMap.get(i);
        } else {
            accent = previousTheme.getAccent(false);
        }
        if (this.accent != null) {
            saveAccentWallpaper();
            Theme.saveThemeAccents(this.applyingTheme, true, false, false, false);
            Theme.clearPreviousTheme();
            Theme.applyTheme(this.applyingTheme, this.nightTheme);
            this.parentLayout.rebuildAllFragmentViews(false, false);
        } else {
            this.parentLayout.rebuildAllFragmentViews(false, false);
            File file = new File(this.applyingTheme.pathToFile);
            Theme.ThemeInfo themeInfo = this.applyingTheme;
            Theme.applyThemeFile(file, themeInfo.name, themeInfo.info, false);
            MessagesController.getInstance(this.applyingTheme.account).saveTheme(this.applyingTheme, null, false, false);
            SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0).edit();
            edit.putString("lastDayTheme", this.applyingTheme.getKey());
            edit.commit();
        }
        BaseFragment baseFragment = getParentLayout().getFragmentStack().get(Math.max(0, getParentLayout().getFragmentStack().size() - 2));
        finishFragment();
        if (this.screenType == 0) {
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didApplyNewTheme, previousTheme, accent, Boolean.valueOf(this.deleteOnCancel));
        }
        Theme.turnOffAutoNight(baseFragment);
    }

    public boolean insideBottomSheet() {
        INavigationLayout iNavigationLayout = this.parentLayout;
        return (iNavigationLayout == null || iNavigationLayout.getBottomSheet() == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateIntensity() {
        this.backgroundImage.getImageReceiver().setAlpha(Math.abs(this.currentIntensity));
        this.backgroundImage.invalidate();
        this.patternsListView.invalidateViews();
        if (this.currentIntensity >= 0.0f) {
            this.backgroundImage.getImageReceiver().setGradientBitmap(null);
        } else {
            if (Build.VERSION.SDK_INT >= 29) {
                this.backgroundImage.getImageReceiver().setBlendMode(null);
            }
            if (this.backgroundImage.getBackground() instanceof MotionBackgroundDrawable) {
                this.backgroundImage.getImageReceiver().setGradientBitmap(((MotionBackgroundDrawable) this.backgroundImage.getBackground()).getBitmap());
            }
        }
        ThemeDelegate themeDelegate = this.themeDelegate;
        int i = this.checkColor;
        themeDelegate.applyChatServiceMessageColor(new int[]{i, i, i, i}, this.backgroundImage.getBackground(), this.backgroundImage.getBackground(), Float.valueOf(this.currentIntensity));
        invalidateBlur();
    }

    private void updateApplyButton1(boolean z) {
        long j = this.dialogId;
        if (j > 0) {
            this.applyButton1.setText(LocaleController.getString(R.string.ApplyWallpaperForMe));
        } else if (j < 0) {
            TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
            if (chat != null) {
                this.applyButton1.setText(LocaleController.formatString(R.string.ApplyWallpaperForChannel, chat.title));
                TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus = this.boostsStatus;
                if (tL_stories$TL_premium_boostsStatus != null && tL_stories$TL_premium_boostsStatus.level < getMessagesController().channelCustomWallpaperLevelMin) {
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("l");
                    if (this.lockSpan == null) {
                        ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_switch_lock);
                        this.lockSpan = coloredImageSpan;
                        coloredImageSpan.setTopOffset(1);
                    }
                    spannableStringBuilder.setSpan(this.lockSpan, 0, 1, 33);
                    spannableStringBuilder.append((CharSequence) " ").append((CharSequence) LocaleController.formatPluralString("ReactionLevelRequiredBtn", getMessagesController().channelCustomWallpaperLevelMin, new Object[0]));
                    this.applyButton1.setSubText(spannableStringBuilder, z);
                    return;
                } else if (this.boostsStatus == null) {
                    checkBoostsLevel();
                    return;
                } else {
                    return;
                }
            }
            this.applyButton1.setText(LocaleController.formatString(R.string.ApplyWallpaperForChannel, LocaleController.getString(R.string.AccDescrChannel).toLowerCase()));
        } else {
            this.applyButton1.setText(LocaleController.getString(R.string.ApplyWallpaper));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:129:0x02aa  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x02bc  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x035e  */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0372  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x03e4  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x03fe  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0416  */
    /* JADX WARN: Removed duplicated region for block: B:237:0x0599  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x059f  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x027f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:263:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00d2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void applyWallpaperBackground(boolean z) {
        File file;
        boolean z2;
        File httpFilePath;
        boolean z3;
        File file2;
        Object obj;
        boolean z4;
        String str;
        File file3;
        File file4;
        int i;
        int i2;
        int i3;
        int i4;
        TLRPC$TL_wallPaper tLRPC$TL_wallPaper;
        int i5;
        int i6;
        int i7;
        Object obj2;
        long j;
        TLRPC$TL_wallPaper tLRPC$TL_wallPaper2;
        boolean z5;
        float f;
        TLRPC$UserFull userFull;
        if (this.dialogId < 0) {
            TL_stories$TL_premium_boostsStatus tL_stories$TL_premium_boostsStatus = this.boostsStatus;
            if (tL_stories$TL_premium_boostsStatus != null && tL_stories$TL_premium_boostsStatus.level < getMessagesController().channelCustomWallpaperLevelMin) {
                getMessagesController().getBoostsController().userCanBoostChannel(this.dialogId, this.boostsStatus, new Consumer() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda17
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj3) {
                        ThemePreviewActivity.this.lambda$applyWallpaperBackground$20((ChannelBoostsController.CanApplyBoost) obj3);
                    }
                });
                return;
            } else if (this.boostsStatus == null) {
                return;
            }
        }
        if (!getUserConfig().isPremium() && z) {
            showDialog(new PremiumFeatureBottomSheet(this, 22, true));
        } else if (this.currentWallpaper instanceof WallpapersListActivity.EmojiWallpaper) {
            WallpaperActivityDelegate wallpaperActivityDelegate = this.delegate;
            if (wallpaperActivityDelegate != null) {
                wallpaperActivityDelegate.didSetNewBackground(null);
            }
            finishFragment();
        } else {
            Theme.ThemeInfo activeTheme = Theme.getActiveTheme();
            String generateWallpaperName = activeTheme.generateWallpaperName(null, this.isBlurred);
            int i8 = 0;
            String generateWallpaperName2 = this.isBlurred ? activeTheme.generateWallpaperName(null, false) : generateWallpaperName;
            File file5 = new File(ApplicationLoader.getFilesDirFixed(), generateWallpaperName);
            Object obj3 = this.currentWallpaper;
            if (obj3 instanceof TLRPC$TL_wallPaper) {
                if (this.originalBitmap != null) {
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file5);
                        this.originalBitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream);
                        fileOutputStream.close();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                } else {
                    ImageReceiver imageReceiver = this.backgroundImage.getImageReceiver();
                    if (imageReceiver.hasNotThumb() || imageReceiver.hasStaticThumb()) {
                        Bitmap bitmap = imageReceiver.getBitmap();
                        try {
                            FileOutputStream fileOutputStream2 = new FileOutputStream(file5);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream2);
                            fileOutputStream2.close();
                        } catch (Exception e2) {
                            FileLog.e(e2);
                        }
                    }
                    z2 = false;
                    if (!z2) {
                        try {
                            z2 = AndroidUtilities.copyFile(FileLoader.getInstance(this.currentAccount).getPathToAttach(((TLRPC$TL_wallPaper) this.currentWallpaper).document, true), file5);
                        } catch (Exception e3) {
                            FileLog.e(e3);
                            z2 = false;
                        }
                    }
                    file = null;
                }
                z2 = true;
                if (!z2) {
                }
                file = null;
            } else if (obj3 instanceof WallpapersListActivity.ColorWallpaper) {
                if (this.selectedPattern != null) {
                    try {
                        WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper) obj3;
                        Bitmap bitmap2 = this.backgroundImage.getImageReceiver().getBitmap();
                        Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(createBitmap);
                        if (this.backgroundGradientColor2 == 0) {
                            if (this.backgroundGradientColor1 != 0) {
                                GradientDrawable gradientDrawable = new GradientDrawable(BackgroundGradientDrawable.getGradientOrientation(this.backgroundRotation), new int[]{this.backgroundColor, this.backgroundGradientColor1});
                                gradientDrawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                                gradientDrawable.draw(canvas);
                            } else {
                                canvas.drawColor(this.backgroundColor);
                            }
                        }
                        Paint paint = new Paint(2);
                        paint.setColorFilter(new PorterDuffColorFilter(this.patternColor, this.blendMode));
                        paint.setAlpha((int) (Math.abs(this.currentIntensity) * 255.0f));
                        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
                        FileOutputStream fileOutputStream3 = new FileOutputStream(file5);
                        if (this.backgroundGradientColor2 != 0) {
                            createBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream3);
                        } else {
                            createBitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream3);
                        }
                        fileOutputStream3.close();
                        z2 = true;
                    } catch (Throwable th) {
                        FileLog.e(th);
                        z2 = false;
                    }
                    z3 = false;
                    file = null;
                    if (this.isBlurred) {
                        try {
                            FileOutputStream fileOutputStream4 = new FileOutputStream(new File(ApplicationLoader.getFilesDirFixed(), generateWallpaperName2));
                            this.blurredBitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream4);
                            fileOutputStream4.close();
                            z2 = true;
                        } catch (Throwable th2) {
                            FileLog.e(th2);
                            z2 = false;
                        }
                    }
                    obj = this.currentWallpaper;
                    if (obj instanceof TLRPC$TL_wallPaper) {
                        TLRPC$TL_wallPaper tLRPC$TL_wallPaper3 = (TLRPC$TL_wallPaper) obj;
                        str = tLRPC$TL_wallPaper3.slug;
                        z4 = z3;
                        tLRPC$TL_wallPaper = tLRPC$TL_wallPaper3;
                        file3 = file5;
                        file4 = file;
                        i = 45;
                        i2 = 0;
                        i3 = 0;
                        i4 = 0;
                    } else if (obj instanceof WallpapersListActivity.ColorWallpaper) {
                        if ("d".equals(((WallpapersListActivity.ColorWallpaper) obj).slug)) {
                            str = "d";
                            i7 = 0;
                            i5 = 0;
                            i4 = 0;
                            i6 = 45;
                        } else {
                            TLRPC$TL_wallPaper tLRPC$TL_wallPaper4 = this.selectedPattern;
                            String str2 = tLRPC$TL_wallPaper4 != null ? tLRPC$TL_wallPaper4.slug : "c";
                            int i9 = this.backgroundColor;
                            i4 = this.backgroundGradientColor1;
                            i5 = this.backgroundGradientColor2;
                            int i10 = this.backgroundGradientColor3;
                            i6 = this.backgroundRotation;
                            i7 = i10;
                            i8 = i9;
                            str = str2;
                        }
                        z4 = z3;
                        file3 = file5;
                        i = i6;
                        tLRPC$TL_wallPaper = null;
                        i2 = i7;
                        file4 = null;
                        int i11 = i5;
                        i3 = i8;
                        i8 = i11;
                    } else {
                        if (obj instanceof WallpapersListActivity.FileWallpaper) {
                            WallpapersListActivity.FileWallpaper fileWallpaper = (WallpapersListActivity.FileWallpaper) obj;
                            str = fileWallpaper.slug;
                            file4 = fileWallpaper.path;
                        } else if (obj instanceof MediaController.SearchImage) {
                            MediaController.SearchImage searchImage = (MediaController.SearchImage) obj;
                            TLRPC$Photo tLRPC$Photo = searchImage.photo;
                            if (tLRPC$Photo != null) {
                                file4 = FileLoader.getInstance(this.currentAccount).getPathToAttach(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, this.maxWallpaperSize, true), true);
                            } else {
                                file4 = ImageLoader.getHttpFilePath(searchImage.imageUrl, "jpg");
                            }
                            str = "";
                        } else {
                            z4 = z3;
                            str = "d";
                            file3 = file5;
                            file4 = null;
                            i = 45;
                            i8 = 0;
                            i2 = 0;
                            i3 = 0;
                            i4 = 0;
                            tLRPC$TL_wallPaper = null;
                        }
                        z4 = z3;
                        file3 = file5;
                        i = 45;
                        i8 = 0;
                        i2 = 0;
                        i3 = 0;
                        i4 = 0;
                        tLRPC$TL_wallPaper = null;
                    }
                    Theme.OverrideWallpaperInfo overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
                    overrideWallpaperInfo.fileName = generateWallpaperName2;
                    overrideWallpaperInfo.originalFileName = generateWallpaperName;
                    overrideWallpaperInfo.slug = str;
                    overrideWallpaperInfo.isBlurred = this.isBlurred;
                    overrideWallpaperInfo.isMotion = this.isMotion;
                    overrideWallpaperInfo.color = i3;
                    overrideWallpaperInfo.gradientColor1 = i4;
                    overrideWallpaperInfo.gradientColor2 = i8;
                    overrideWallpaperInfo.gradientColor3 = i2;
                    overrideWallpaperInfo.rotation = i;
                    if (this.shouldShowBrightnessControll) {
                        float f2 = this.dimAmount;
                        if (f2 >= 0.0f) {
                            overrideWallpaperInfo.intensity = f2;
                            obj2 = this.currentWallpaper;
                            if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                                WallpapersListActivity.ColorWallpaper colorWallpaper2 = (WallpapersListActivity.ColorWallpaper) obj2;
                                String str3 = ("c".equals(str) || "t".equals(str) || "d".equals(str)) ? null : str;
                                float f3 = colorWallpaper2.intensity;
                                if (f3 < 0.0f && !Theme.getActiveTheme().isDark()) {
                                    f3 *= -1.0f;
                                }
                                if (colorWallpaper2.parentWallpaper != null && colorWallpaper2.color == i3 && colorWallpaper2.gradientColor1 == i4 && colorWallpaper2.gradientColor2 == i8 && colorWallpaper2.gradientColor3 == i2 && TextUtils.equals(colorWallpaper2.slug, str3) && colorWallpaper2.gradientRotation == i && (this.selectedPattern == null || Math.abs(f3 - this.currentIntensity) < 0.001f)) {
                                    TLRPC$WallPaper tLRPC$WallPaper = colorWallpaper2.parentWallpaper;
                                    overrideWallpaperInfo.wallpaperId = tLRPC$WallPaper.id;
                                    overrideWallpaperInfo.accessHash = tLRPC$WallPaper.access_hash;
                                }
                            }
                            j = this.dialogId;
                            overrideWallpaperInfo.dialogId = j;
                            if (j != 0 && (userFull = getMessagesController().getUserFull(this.dialogId)) != null) {
                                overrideWallpaperInfo.prevUserWallpaper = userFull.wallpaper;
                            }
                            overrideWallpaperInfo.forBoth = z;
                            MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo, str == null && this.dialogId == 0, 0L);
                            if (z2) {
                                if (this.dialogId != 0) {
                                    if (file4 != null && getMessagesController().uploadingWallpaperInfo == overrideWallpaperInfo) {
                                        TLRPC$TL_wallPaper tLRPC$TL_wallPaper5 = new TLRPC$TL_wallPaper();
                                        TLRPC$TL_wallPaperSettings tLRPC$TL_wallPaperSettings = new TLRPC$TL_wallPaperSettings();
                                        tLRPC$TL_wallPaper5.settings = tLRPC$TL_wallPaperSettings;
                                        tLRPC$TL_wallPaperSettings.intensity = (int) (overrideWallpaperInfo.intensity * 100.0f);
                                        tLRPC$TL_wallPaperSettings.blur = overrideWallpaperInfo.isBlurred;
                                        tLRPC$TL_wallPaperSettings.motion = overrideWallpaperInfo.isMotion;
                                        tLRPC$TL_wallPaper5.uploadingImage = file4.getAbsolutePath();
                                        Bitmap createBitmap2 = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
                                        Canvas canvas2 = new Canvas(createBitmap2);
                                        float max = Math.max(50.0f / this.backgroundImage.getMeasuredWidth(), 50.0f / this.backgroundImage.getMeasuredHeight());
                                        canvas2.scale(max, max);
                                        if (this.backgroundImage.getMeasuredHeight() > this.backgroundImage.getMeasuredWidth()) {
                                            f = 0.0f;
                                            canvas2.translate(0.0f, (-(this.backgroundImage.getMeasuredHeight() - this.backgroundImage.getMeasuredWidth())) / 2.0f);
                                        } else {
                                            f = 0.0f;
                                            canvas2.translate((-(this.backgroundImage.getMeasuredWidth() - this.backgroundImage.getMeasuredHeight())) / 2.0f, 0.0f);
                                        }
                                        float f4 = this.dimAmount;
                                        this.dimAmount = f;
                                        this.backgroundImage.draw(canvas2);
                                        this.dimAmount = f4;
                                        Utilities.blurBitmap(createBitmap2, 3, 1, createBitmap2.getWidth(), createBitmap2.getHeight(), createBitmap2.getRowBytes());
                                        tLRPC$TL_wallPaper5.stripedThumb = createBitmap2;
                                        createServiceMessageLocal(tLRPC$TL_wallPaper5, z);
                                        if (this.dialogId >= 0) {
                                            TLRPC$UserFull userFull2 = getMessagesController().getUserFull(this.dialogId);
                                            if (userFull2 != null) {
                                                userFull2.wallpaper = tLRPC$TL_wallPaper5;
                                                NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.userInfoDidLoad, Long.valueOf(this.dialogId), userFull2);
                                            }
                                        } else {
                                            TLRPC$ChatFull chatFull = getMessagesController().getChatFull(-this.dialogId);
                                            if (chatFull != null) {
                                                chatFull.wallpaper = tLRPC$TL_wallPaper5;
                                                NotificationCenter notificationCenter = NotificationCenter.getInstance(this.currentAccount);
                                                int i12 = NotificationCenter.chatInfoDidLoad;
                                                Boolean bool = Boolean.FALSE;
                                                notificationCenter.lambda$postNotificationNameOnUIThread$1(i12, chatFull, 0, bool, bool);
                                                tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper5;
                                            }
                                        }
                                        tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper5;
                                    } else {
                                        ChatThemeController.getInstance(this.currentAccount).setWallpaperToPeer(this.dialogId, null, overrideWallpaperInfo, this.serverWallpaper, ThemePreviewActivity$$ExternalSyntheticLambda26.INSTANCE);
                                        tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper;
                                    }
                                    this.setupFinished = true;
                                    WallpaperActivityDelegate wallpaperActivityDelegate2 = this.delegate;
                                    if (wallpaperActivityDelegate2 != null) {
                                        wallpaperActivityDelegate2.didSetNewBackground(tLRPC$TL_wallPaper2);
                                    }
                                    finishFragment();
                                    z5 = false;
                                    if (z5) {
                                        WallpaperActivityDelegate wallpaperActivityDelegate3 = this.delegate;
                                        if (wallpaperActivityDelegate3 != null) {
                                            wallpaperActivityDelegate3.didSetNewBackground(tLRPC$TL_wallPaper2);
                                        }
                                        finishFragment();
                                        return;
                                    }
                                    return;
                                }
                                Theme.serviceMessageColorBackup = getThemedColor(Theme.key_chat_serviceBackground);
                                if ("t".equals(overrideWallpaperInfo.slug)) {
                                    overrideWallpaperInfo = null;
                                }
                                Theme.getActiveTheme().setOverrideWallpaper(overrideWallpaperInfo);
                                Theme.reloadWallpaper(true);
                                if (!z4) {
                                    ImageLoader.getInstance().removeImage(ImageLoader.getHttpFileName(file3.getAbsolutePath()) + "@100_100");
                                }
                            }
                            tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper;
                            z5 = true;
                            if (z5) {
                            }
                        }
                    }
                    overrideWallpaperInfo.intensity = this.currentIntensity;
                    obj2 = this.currentWallpaper;
                    if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                    }
                    j = this.dialogId;
                    overrideWallpaperInfo.dialogId = j;
                    if (j != 0) {
                        overrideWallpaperInfo.prevUserWallpaper = userFull.wallpaper;
                    }
                    overrideWallpaperInfo.forBoth = z;
                    MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo, str == null && this.dialogId == 0, 0L);
                    if (z2) {
                    }
                    tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper;
                    z5 = true;
                    if (z5) {
                    }
                } else {
                    file = null;
                    z2 = true;
                }
            } else if (obj3 instanceof WallpapersListActivity.FileWallpaper) {
                WallpapersListActivity.FileWallpaper fileWallpaper2 = (WallpapersListActivity.FileWallpaper) obj3;
                if (fileWallpaper2.resId != 0 || "t".equals(fileWallpaper2.slug)) {
                    file = null;
                    z2 = true;
                } else {
                    try {
                    } catch (Exception e4) {
                        e = e4;
                        file = null;
                    }
                    try {
                        if (this.hasScrollingBackground && this.currentScrollOffset != this.defaultScrollOffset) {
                            Bitmap createBitmap3 = Bitmap.createBitmap((int) this.croppedWidth, this.currentWallpaperBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas3 = new Canvas(createBitmap3);
                            canvas3.translate(-((this.currentScrollOffset / this.maxScrollOffset) * (this.currentWallpaperBitmap.getWidth() - createBitmap3.getWidth())), 0.0f);
                            file = null;
                            canvas3.drawBitmap(this.currentWallpaperBitmap, 0.0f, 0.0f, (Paint) null);
                            fileWallpaper2.path = new File(FileLoader.getDirectory(4), Utilities.random.nextInt() + ".jpg");
                            FileOutputStream fileOutputStream5 = new FileOutputStream(fileWallpaper2.path);
                            createBitmap3.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream5);
                            fileOutputStream5.close();
                            createBitmap3.recycle();
                            file2 = fileWallpaper2.path;
                        } else {
                            file = null;
                            File file6 = fileWallpaper2.originalPath;
                            file2 = file6 != null ? file6 : fileWallpaper2.path;
                        }
                        z3 = file2.equals(file5);
                        if (z3) {
                            z2 = true;
                        } else {
                            try {
                                z2 = AndroidUtilities.copyFile(file2, file5);
                            } catch (Exception e5) {
                                e = e5;
                                FileLog.e(e);
                                z2 = false;
                                if (this.isBlurred) {
                                }
                                obj = this.currentWallpaper;
                                if (obj instanceof TLRPC$TL_wallPaper) {
                                }
                                Theme.OverrideWallpaperInfo overrideWallpaperInfo2 = new Theme.OverrideWallpaperInfo();
                                overrideWallpaperInfo2.fileName = generateWallpaperName2;
                                overrideWallpaperInfo2.originalFileName = generateWallpaperName;
                                overrideWallpaperInfo2.slug = str;
                                overrideWallpaperInfo2.isBlurred = this.isBlurred;
                                overrideWallpaperInfo2.isMotion = this.isMotion;
                                overrideWallpaperInfo2.color = i3;
                                overrideWallpaperInfo2.gradientColor1 = i4;
                                overrideWallpaperInfo2.gradientColor2 = i8;
                                overrideWallpaperInfo2.gradientColor3 = i2;
                                overrideWallpaperInfo2.rotation = i;
                                if (this.shouldShowBrightnessControll) {
                                }
                                overrideWallpaperInfo2.intensity = this.currentIntensity;
                                obj2 = this.currentWallpaper;
                                if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                                }
                                j = this.dialogId;
                                overrideWallpaperInfo2.dialogId = j;
                                if (j != 0) {
                                }
                                overrideWallpaperInfo2.forBoth = z;
                                MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo2, str == null && this.dialogId == 0, 0L);
                                if (z2) {
                                }
                                tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper;
                                z5 = true;
                                if (z5) {
                                }
                            }
                        }
                    } catch (Exception e6) {
                        e = e6;
                        z3 = false;
                        FileLog.e(e);
                        z2 = false;
                        if (this.isBlurred) {
                        }
                        obj = this.currentWallpaper;
                        if (obj instanceof TLRPC$TL_wallPaper) {
                        }
                        Theme.OverrideWallpaperInfo overrideWallpaperInfo22 = new Theme.OverrideWallpaperInfo();
                        overrideWallpaperInfo22.fileName = generateWallpaperName2;
                        overrideWallpaperInfo22.originalFileName = generateWallpaperName;
                        overrideWallpaperInfo22.slug = str;
                        overrideWallpaperInfo22.isBlurred = this.isBlurred;
                        overrideWallpaperInfo22.isMotion = this.isMotion;
                        overrideWallpaperInfo22.color = i3;
                        overrideWallpaperInfo22.gradientColor1 = i4;
                        overrideWallpaperInfo22.gradientColor2 = i8;
                        overrideWallpaperInfo22.gradientColor3 = i2;
                        overrideWallpaperInfo22.rotation = i;
                        if (this.shouldShowBrightnessControll) {
                        }
                        overrideWallpaperInfo22.intensity = this.currentIntensity;
                        obj2 = this.currentWallpaper;
                        if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                        }
                        j = this.dialogId;
                        overrideWallpaperInfo22.dialogId = j;
                        if (j != 0) {
                        }
                        overrideWallpaperInfo22.forBoth = z;
                        MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo22, str == null && this.dialogId == 0, 0L);
                        if (z2) {
                        }
                        tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper;
                        z5 = true;
                        if (z5) {
                        }
                    }
                    if (this.isBlurred) {
                    }
                    obj = this.currentWallpaper;
                    if (obj instanceof TLRPC$TL_wallPaper) {
                    }
                    Theme.OverrideWallpaperInfo overrideWallpaperInfo222 = new Theme.OverrideWallpaperInfo();
                    overrideWallpaperInfo222.fileName = generateWallpaperName2;
                    overrideWallpaperInfo222.originalFileName = generateWallpaperName;
                    overrideWallpaperInfo222.slug = str;
                    overrideWallpaperInfo222.isBlurred = this.isBlurred;
                    overrideWallpaperInfo222.isMotion = this.isMotion;
                    overrideWallpaperInfo222.color = i3;
                    overrideWallpaperInfo222.gradientColor1 = i4;
                    overrideWallpaperInfo222.gradientColor2 = i8;
                    overrideWallpaperInfo222.gradientColor3 = i2;
                    overrideWallpaperInfo222.rotation = i;
                    if (this.shouldShowBrightnessControll) {
                    }
                    overrideWallpaperInfo222.intensity = this.currentIntensity;
                    obj2 = this.currentWallpaper;
                    if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                    }
                    j = this.dialogId;
                    overrideWallpaperInfo222.dialogId = j;
                    if (j != 0) {
                    }
                    overrideWallpaperInfo222.forBoth = z;
                    MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo222, str == null && this.dialogId == 0, 0L);
                    if (z2) {
                    }
                    tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper;
                    z5 = true;
                    if (z5) {
                    }
                }
            } else {
                file = null;
                if (obj3 instanceof MediaController.SearchImage) {
                    MediaController.SearchImage searchImage2 = (MediaController.SearchImage) obj3;
                    TLRPC$Photo tLRPC$Photo2 = searchImage2.photo;
                    if (tLRPC$Photo2 != null) {
                        httpFilePath = FileLoader.getInstance(this.currentAccount).getPathToAttach(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo2.sizes, this.maxWallpaperSize, true), true);
                    } else {
                        httpFilePath = ImageLoader.getHttpFilePath(searchImage2.imageUrl, "jpg");
                    }
                    try {
                        z2 = AndroidUtilities.copyFile(httpFilePath, file5);
                    } catch (Exception e7) {
                        FileLog.e(e7);
                    }
                }
                z2 = false;
            }
            z3 = false;
            if (this.isBlurred) {
            }
            obj = this.currentWallpaper;
            if (obj instanceof TLRPC$TL_wallPaper) {
            }
            Theme.OverrideWallpaperInfo overrideWallpaperInfo2222 = new Theme.OverrideWallpaperInfo();
            overrideWallpaperInfo2222.fileName = generateWallpaperName2;
            overrideWallpaperInfo2222.originalFileName = generateWallpaperName;
            overrideWallpaperInfo2222.slug = str;
            overrideWallpaperInfo2222.isBlurred = this.isBlurred;
            overrideWallpaperInfo2222.isMotion = this.isMotion;
            overrideWallpaperInfo2222.color = i3;
            overrideWallpaperInfo2222.gradientColor1 = i4;
            overrideWallpaperInfo2222.gradientColor2 = i8;
            overrideWallpaperInfo2222.gradientColor3 = i2;
            overrideWallpaperInfo2222.rotation = i;
            if (this.shouldShowBrightnessControll) {
            }
            overrideWallpaperInfo2222.intensity = this.currentIntensity;
            obj2 = this.currentWallpaper;
            if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
            }
            j = this.dialogId;
            overrideWallpaperInfo2222.dialogId = j;
            if (j != 0) {
            }
            overrideWallpaperInfo2222.forBoth = z;
            MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo2222, str == null && this.dialogId == 0, 0L);
            if (z2) {
            }
            tLRPC$TL_wallPaper2 = tLRPC$TL_wallPaper;
            z5 = true;
            if (z5) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyWallpaperBackground$20(ChannelBoostsController.CanApplyBoost canApplyBoost) {
        if (getContext() == null) {
            return;
        }
        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(this, getContext(), 23, this.currentAccount, getResourceProvider());
        limitReachedBottomSheet.setCanApplyBoost(canApplyBoost);
        limitReachedBottomSheet.setBoostsStats(this.boostsStatus, true);
        limitReachedBottomSheet.setDialogId(this.dialogId);
        if (!insideBottomSheet()) {
            limitReachedBottomSheet.showStatisticButtonInLink(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    ThemePreviewActivity.this.lambda$applyWallpaperBackground$19();
                }
            });
        }
        showDialog(limitReachedBottomSheet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyWallpaperBackground$19() {
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
        Bundle bundle = new Bundle();
        bundle.putLong("chat_id", -this.dialogId);
        bundle.putBoolean("is_megagroup", chat.megagroup);
        bundle.putBoolean("start_from_boosts", true);
        TLRPC$ChatFull chatFull = getMessagesController().getChatFull(-this.dialogId);
        if (chatFull == null || !chatFull.can_view_stats) {
            bundle.putBoolean("only_boosts", true);
        }
        presentFragment(new StatisticActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onColorsRotate() {
        if (this.screenType == 2) {
            this.backgroundRotation += 45;
            while (true) {
                int i = this.backgroundRotation;
                if (i >= 360) {
                    this.backgroundRotation = i - 360;
                } else {
                    setBackgroundColor(this.backgroundColor, 0, true, true);
                    return;
                }
            }
        } else {
            Theme.ThemeAccent themeAccent = this.accent;
            if (themeAccent == null) {
                return;
            }
            themeAccent.backgroundRotation += 45;
            while (true) {
                Theme.ThemeAccent themeAccent2 = this.accent;
                int i2 = themeAccent2.backgroundRotation;
                if (i2 >= 360) {
                    themeAccent2.backgroundRotation = i2 - 360;
                } else {
                    Theme.refreshThemeColors();
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectColorType(int i) {
        selectColorType(i, true);
    }

    private void selectColorType(int i, boolean z) {
        int i2;
        if (getParentActivity() == null || this.colorType == i || this.patternViewAnimation != null || this.accent == null) {
            return;
        }
        if (z && i == 2 && (Theme.hasCustomWallpaper() || this.accent.backgroundOverrideColor == 4294967296L)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("ChangeChatBackground", R.string.ChangeChatBackground));
            if (!Theme.hasCustomWallpaper() || Theme.isCustomWallpaperColor()) {
                builder.setMessage(LocaleController.getString("ChangeColorToColor", R.string.ChangeColorToColor));
                builder.setPositiveButton(LocaleController.getString("Reset", R.string.Reset), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda5
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i3) {
                        ThemePreviewActivity.this.lambda$selectColorType$22(dialogInterface, i3);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Continue", R.string.Continue), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda2
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i3) {
                        ThemePreviewActivity.this.lambda$selectColorType$23(dialogInterface, i3);
                    }
                });
            } else {
                builder.setMessage(LocaleController.getString("ChangeWallpaperToColor", R.string.ChangeWallpaperToColor));
                builder.setPositiveButton(LocaleController.getString("Change", R.string.Change), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i3) {
                        ThemePreviewActivity.this.lambda$selectColorType$24(dialogInterface, i3);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            }
            showDialog(builder.create());
            return;
        }
        int i3 = this.colorType;
        this.colorType = i;
        if (i == 1) {
            this.dropDown.setText(LocaleController.getString("ColorPickerMainColor", R.string.ColorPickerMainColor));
            this.colorPicker.setType(1, hasChanges(1), 2, this.accent.accentColor2 != 0 ? 2 : 1, false, 0, false);
            this.colorPicker.setColor(this.accent.accentColor, 0);
            int i4 = this.accent.accentColor2;
            if (i4 != 0) {
                this.colorPicker.setColor(i4, 1);
            }
            if (i3 == 2 || (i3 == 3 && this.accent.myMessagesGradientAccentColor2 != 0)) {
                this.messagesAdapter.notifyItemRemoved(0);
            }
        } else if (i == 2) {
            this.dropDown.setText(LocaleController.getString("ColorPickerBackground", R.string.ColorPickerBackground));
            int themedColor = getThemedColor(Theme.key_chat_wallpaper);
            int i5 = Theme.key_chat_wallpaper_gradient_to1;
            int themedColor2 = Theme.hasThemeKey(i5) ? getThemedColor(i5) : 0;
            int i6 = Theme.key_chat_wallpaper_gradient_to2;
            int themedColor3 = Theme.hasThemeKey(i6) ? getThemedColor(i6) : 0;
            int i7 = Theme.key_chat_wallpaper_gradient_to3;
            int themedColor4 = Theme.hasThemeKey(i7) ? getThemedColor(i7) : 0;
            Theme.ThemeAccent themeAccent = this.accent;
            long j = themeAccent.backgroundGradientOverrideColor1;
            int i8 = (int) j;
            if (i8 == 0 && j != 0) {
                themedColor2 = 0;
            }
            long j2 = themeAccent.backgroundGradientOverrideColor2;
            int i9 = (int) j2;
            if (i9 == 0 && j2 != 0) {
                themedColor3 = 0;
            }
            long j3 = themeAccent.backgroundGradientOverrideColor3;
            int i10 = (int) j3;
            if (i10 == 0 && j3 != 0) {
                themedColor4 = 0;
            }
            int i11 = (int) themeAccent.backgroundOverrideColor;
            this.colorPicker.setType(2, hasChanges(2), 4, (i8 == 0 && themedColor2 == 0) ? 1 : (i10 == 0 && themedColor4 == 0) ? (i9 == 0 && themedColor3 == 0) ? 2 : 3 : 4, false, this.accent.backgroundRotation, false);
            ColorPicker colorPicker = this.colorPicker;
            if (i10 == 0) {
                i10 = themedColor4;
            }
            colorPicker.setColor(i10, 3);
            ColorPicker colorPicker2 = this.colorPicker;
            if (i9 == 0) {
                i9 = themedColor3;
            }
            colorPicker2.setColor(i9, 2);
            ColorPicker colorPicker3 = this.colorPicker;
            if (i8 == 0) {
                i8 = themedColor2;
            }
            colorPicker3.setColor(i8, 1);
            ColorPicker colorPicker4 = this.colorPicker;
            if (i11 != 0) {
                themedColor = i11;
            }
            colorPicker4.setColor(themedColor, 0);
            if (i3 == 1 || this.accent.myMessagesGradientAccentColor2 == 0) {
                this.messagesAdapter.notifyItemInserted(0);
            } else {
                this.messagesAdapter.notifyItemChanged(0);
            }
            this.listView2.smoothScrollBy(0, AndroidUtilities.dp(60.0f));
        } else if (i == 3) {
            this.dropDown.setText(LocaleController.getString("ColorPickerMyMessages", R.string.ColorPickerMyMessages));
            Theme.ThemeAccent themeAccent2 = this.accent;
            if (themeAccent2.myMessagesGradientAccentColor1 == 0) {
                i2 = 1;
            } else if (themeAccent2.myMessagesGradientAccentColor3 != 0) {
                i2 = 4;
            } else {
                i2 = themeAccent2.myMessagesGradientAccentColor2 != 0 ? 3 : 2;
            }
            this.colorPicker.setType(2, hasChanges(3), 4, i2, true, 0, false);
            this.colorPicker.setColor(this.accent.myMessagesGradientAccentColor3, 3);
            this.colorPicker.setColor(this.accent.myMessagesGradientAccentColor2, 2);
            this.colorPicker.setColor(this.accent.myMessagesGradientAccentColor1, 1);
            ColorPicker colorPicker5 = this.colorPicker;
            Theme.ThemeAccent themeAccent3 = this.accent;
            int i12 = themeAccent3.myMessagesAccentColor;
            if (i12 == 0) {
                i12 = themeAccent3.accentColor;
            }
            colorPicker5.setColor(i12, 0);
            this.messagesCheckBoxView[1].setColor(0, this.accent.myMessagesAccentColor);
            this.messagesCheckBoxView[1].setColor(1, this.accent.myMessagesGradientAccentColor1);
            this.messagesCheckBoxView[1].setColor(2, this.accent.myMessagesGradientAccentColor2);
            this.messagesCheckBoxView[1].setColor(3, this.accent.myMessagesGradientAccentColor3);
            if (this.accent.myMessagesGradientAccentColor2 != 0) {
                if (i3 == 1) {
                    this.messagesAdapter.notifyItemInserted(0);
                } else {
                    this.messagesAdapter.notifyItemChanged(0);
                }
            } else if (i3 == 2) {
                this.messagesAdapter.notifyItemRemoved(0);
            }
            this.listView2.smoothScrollBy(0, AndroidUtilities.dp(60.0f));
            showAnimationHint();
        }
        if (i == 1 || i == 3) {
            if (i3 == 2 && this.patternLayout[1].getVisibility() == 0) {
                showPatternsView(0, true, true);
            }
            if (i == 1) {
                if (this.applyingTheme.isDark()) {
                    this.colorPicker.setMinBrightness(0.2f);
                    return;
                }
                this.colorPicker.setMinBrightness(0.05f);
                this.colorPicker.setMaxBrightness(0.8f);
                return;
            }
            this.colorPicker.setMinBrightness(0.0f);
            this.colorPicker.setMaxBrightness(1.0f);
            return;
        }
        this.colorPicker.setMinBrightness(0.0f);
        this.colorPicker.setMaxBrightness(1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$selectColorType$22(DialogInterface dialogInterface, int i) {
        Theme.ThemeAccent themeAccent = this.accent;
        if (themeAccent.backgroundOverrideColor == 4294967296L) {
            themeAccent.backgroundOverrideColor = 0L;
            themeAccent.backgroundGradientOverrideColor1 = 0L;
            themeAccent.backgroundGradientOverrideColor2 = 0L;
            themeAccent.backgroundGradientOverrideColor3 = 0L;
            updatePlayAnimationView(false);
            Theme.refreshThemeColors();
        }
        this.removeBackgroundOverride = true;
        Theme.resetCustomWallpaper(true);
        selectColorType(2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$selectColorType$23(DialogInterface dialogInterface, int i) {
        if (Theme.isCustomWallpaperColor()) {
            Theme.ThemeAccent themeAccent = this.accent;
            Theme.OverrideWallpaperInfo overrideWallpaperInfo = themeAccent.overrideWallpaper;
            themeAccent.backgroundOverrideColor = overrideWallpaperInfo.color;
            themeAccent.backgroundGradientOverrideColor1 = overrideWallpaperInfo.gradientColor1;
            themeAccent.backgroundGradientOverrideColor2 = overrideWallpaperInfo.gradientColor2;
            themeAccent.backgroundGradientOverrideColor3 = overrideWallpaperInfo.gradientColor3;
            themeAccent.backgroundRotation = overrideWallpaperInfo.rotation;
            String str = overrideWallpaperInfo.slug;
            themeAccent.patternSlug = str;
            float f = overrideWallpaperInfo.intensity;
            themeAccent.patternIntensity = f;
            this.currentIntensity = f;
            if (str != null && !"c".equals(str)) {
                int size = this.patterns.size();
                int i2 = 0;
                while (true) {
                    if (i2 >= size) {
                        break;
                    }
                    TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) this.patterns.get(i2);
                    if (tLRPC$TL_wallPaper.pattern && this.accent.patternSlug.equals(tLRPC$TL_wallPaper.slug)) {
                        this.selectedPattern = tLRPC$TL_wallPaper;
                        break;
                    }
                    i2++;
                }
            } else {
                this.selectedPattern = null;
            }
            this.removeBackgroundOverride = true;
            this.backgroundCheckBoxView[1].setChecked(this.selectedPattern != null, true);
            updatePlayAnimationView(false);
            Theme.refreshThemeColors();
        }
        Drawable background = this.backgroundImage.getBackground();
        if (background instanceof MotionBackgroundDrawable) {
            MotionBackgroundDrawable motionBackgroundDrawable = (MotionBackgroundDrawable) background;
            motionBackgroundDrawable.setPatternBitmap(100, null);
            if (Theme.getActiveTheme().isDark()) {
                if (this.currentIntensity < 0.0f) {
                    this.backgroundImage.getImageReceiver().setGradientBitmap(motionBackgroundDrawable.getBitmap());
                }
                SeekBarView seekBarView = this.intensitySeekBar;
                if (seekBarView != null) {
                    seekBarView.setTwoSided(true);
                }
            } else {
                float f2 = this.currentIntensity;
                if (f2 < 0.0f) {
                    this.currentIntensity = -f2;
                }
            }
        }
        SeekBarView seekBarView2 = this.intensitySeekBar;
        if (seekBarView2 != null) {
            seekBarView2.setProgress(this.currentIntensity);
        }
        Theme.resetCustomWallpaper(true);
        selectColorType(2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$selectColorType$24(DialogInterface dialogInterface, int i) {
        Theme.ThemeAccent themeAccent = this.accent;
        if (themeAccent.backgroundOverrideColor == 4294967296L) {
            themeAccent.backgroundOverrideColor = 0L;
            themeAccent.backgroundGradientOverrideColor1 = 0L;
            themeAccent.backgroundGradientOverrideColor2 = 0L;
            themeAccent.backgroundGradientOverrideColor3 = 0L;
            updatePlayAnimationView(false);
            Theme.refreshThemeColors();
        }
        this.removeBackgroundOverride = true;
        Theme.resetCustomWallpaper(true);
        selectColorType(2, false);
    }

    private void selectPattern(int i) {
        TLRPC$TL_wallPaper tLRPC$TL_wallPaper;
        if (i >= 0 && i < this.patterns.size()) {
            tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) this.patterns.get(i);
        } else {
            tLRPC$TL_wallPaper = this.lastSelectedPattern;
        }
        if (tLRPC$TL_wallPaper == null) {
            return;
        }
        ValueAnimator valueAnimator = this.valueAnimator;
        if (valueAnimator != null) {
            valueAnimator.removeAllListeners();
            this.valueAnimator.cancel();
        }
        BackgroundView[] backgroundViewArr = this.backgroundImages;
        BackgroundView backgroundView = backgroundViewArr[0];
        backgroundViewArr[0] = backgroundViewArr[1];
        backgroundViewArr[1] = backgroundView;
        this.page2.removeView(backgroundViewArr[0]);
        this.page2.addView(this.backgroundImages[0], this.page2.indexOfChild(this.backgroundImages[1]) + 1);
        BackgroundView[] backgroundViewArr2 = this.backgroundImages;
        BackgroundView backgroundView2 = backgroundViewArr2[0];
        this.backgroundImage = backgroundView2;
        backgroundView2.setBackground(backgroundViewArr2[1].getBackground());
        updateIntensity();
        this.backgroundImages[1].setVisibility(0);
        this.backgroundImages[1].setAlpha(1.0f);
        this.backgroundImage.setVisibility(0);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.valueAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ThemePreviewActivity.32
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ThemePreviewActivity.this.backgroundImage.setAlpha(((Float) valueAnimator2.getAnimatedValue()).floatValue());
            }
        });
        this.valueAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.33
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                ThemePreviewActivity.this.backgroundImage.invalidate();
                ThemePreviewActivity.this.backgroundImages[1].setVisibility(8);
                ThemePreviewActivity.this.valueAnimator = null;
            }
        });
        this.valueAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.valueAnimator.setDuration(300L);
        this.valueAnimator.start();
        this.backgroundImage.getImageReceiver().setCrossfadeDuration(300);
        this.backgroundImage.getImageReceiver().setImage(ImageLocation.getForDocument(tLRPC$TL_wallPaper.document), this.imageFilter, null, null, null, tLRPC$TL_wallPaper.document.size, "jpg", tLRPC$TL_wallPaper, 1);
        this.backgroundImage.onNewImageSet();
        this.selectedPattern = tLRPC$TL_wallPaper;
        this.isMotion = this.backgroundCheckBoxView[2].isChecked();
        updateButtonState(false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveAccentWallpaper() {
        Theme.ThemeAccent themeAccent = this.accent;
        if (themeAccent == null || TextUtils.isEmpty(themeAccent.patternSlug)) {
            return;
        }
        try {
            File pathToWallpaper = this.accent.getPathToWallpaper();
            Drawable background = this.backgroundImage.getBackground();
            Bitmap bitmap = this.backgroundImage.getImageReceiver().getBitmap();
            if (background instanceof MotionBackgroundDrawable) {
                FileOutputStream fileOutputStream = new FileOutputStream(pathToWallpaper);
                bitmap.compress(Bitmap.CompressFormat.PNG, 87, fileOutputStream);
                fileOutputStream.close();
            } else {
                Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                background.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                background.draw(canvas);
                Paint paint = new Paint(2);
                paint.setColorFilter(new PorterDuffColorFilter(this.patternColor, this.blendMode));
                paint.setAlpha((int) (this.currentIntensity * 255.0f));
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
                FileOutputStream fileOutputStream2 = new FileOutputStream(pathToWallpaper);
                createBitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream2);
                fileOutputStream2.close();
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    private boolean hasChanges(int i) {
        int defaultAccentColor;
        long j;
        if (this.editingTheme) {
            return false;
        }
        if (i == 1 || i == 2) {
            long j2 = this.backupBackgroundOverrideColor;
            if (j2 != 0) {
                if (j2 != this.accent.backgroundOverrideColor) {
                    return true;
                }
            } else {
                int defaultAccentColor2 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper);
                int i2 = (int) this.accent.backgroundOverrideColor;
                if (i2 == 0) {
                    i2 = defaultAccentColor2;
                }
                if (i2 != defaultAccentColor2) {
                    return true;
                }
            }
            long j3 = this.backupBackgroundGradientOverrideColor1;
            if (j3 == 0 && this.backupBackgroundGradientOverrideColor2 == 0 && this.backupBackgroundGradientOverrideColor3 == 0) {
                for (int i3 = 0; i3 < 3; i3++) {
                    if (i3 == 0) {
                        defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to1);
                        j = this.accent.backgroundGradientOverrideColor1;
                    } else if (i3 == 1) {
                        defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to2);
                        j = this.accent.backgroundGradientOverrideColor2;
                    } else {
                        defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to3);
                        j = this.accent.backgroundGradientOverrideColor3;
                    }
                    int i4 = (int) j;
                    if (i4 == 0 && j != 0) {
                        i4 = 0;
                    } else if (i4 == 0) {
                        i4 = defaultAccentColor;
                    }
                    if (i4 != defaultAccentColor) {
                        return true;
                    }
                }
            } else {
                Theme.ThemeAccent themeAccent = this.accent;
                if (j3 != themeAccent.backgroundGradientOverrideColor1 || this.backupBackgroundGradientOverrideColor2 != themeAccent.backgroundGradientOverrideColor2 || this.backupBackgroundGradientOverrideColor3 != themeAccent.backgroundGradientOverrideColor3) {
                    return true;
                }
            }
            if (this.accent.backgroundRotation != this.backupBackgroundRotation) {
                return true;
            }
        }
        if (i == 1 || i == 3) {
            int i5 = this.backupAccentColor;
            Theme.ThemeAccent themeAccent2 = this.accent;
            if (i5 != themeAccent2.accentColor2) {
                return true;
            }
            int i6 = this.backupMyMessagesAccentColor;
            if (i6 != 0) {
                if (i6 != themeAccent2.myMessagesAccentColor) {
                    return true;
                }
            } else {
                int i7 = themeAccent2.myMessagesAccentColor;
                if (i7 != 0 && i7 != themeAccent2.accentColor) {
                    return true;
                }
            }
            int i8 = this.backupMyMessagesGradientAccentColor1;
            if (i8 != 0) {
                if (i8 != themeAccent2.myMessagesGradientAccentColor1) {
                    return true;
                }
            } else if (themeAccent2.myMessagesGradientAccentColor1 != 0) {
                return true;
            }
            int i9 = this.backupMyMessagesGradientAccentColor2;
            if (i9 != 0) {
                if (i9 != themeAccent2.myMessagesGradientAccentColor2) {
                    return true;
                }
            } else if (themeAccent2.myMessagesGradientAccentColor2 != 0) {
                return true;
            }
            int i10 = this.backupMyMessagesGradientAccentColor3;
            if (i10 != 0) {
                if (i10 != themeAccent2.myMessagesGradientAccentColor3) {
                    return true;
                }
            } else if (themeAccent2.myMessagesGradientAccentColor3 != 0) {
                return true;
            }
            if (this.backupMyMessagesAnimated != themeAccent2.myMessagesAnimated) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0084, code lost:
        if (r7.accent.patternMotion == r7.isMotion) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0090, code lost:
        if (r7.accent.patternIntensity == r7.currentIntensity) goto L45;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean checkDiscard() {
        if (this.screenType == 1) {
            Theme.ThemeAccent themeAccent = this.accent;
            if (themeAccent.accentColor == this.backupAccentColor && themeAccent.accentColor2 == this.backupAccentColor2 && themeAccent.myMessagesAccentColor == this.backupMyMessagesAccentColor && themeAccent.myMessagesGradientAccentColor1 == this.backupMyMessagesGradientAccentColor1 && themeAccent.myMessagesGradientAccentColor2 == this.backupMyMessagesGradientAccentColor2 && themeAccent.myMessagesGradientAccentColor3 == this.backupMyMessagesGradientAccentColor3 && themeAccent.myMessagesAnimated == this.backupMyMessagesAnimated && themeAccent.backgroundOverrideColor == this.backupBackgroundOverrideColor && themeAccent.backgroundGradientOverrideColor1 == this.backupBackgroundGradientOverrideColor1 && themeAccent.backgroundGradientOverrideColor2 == this.backupBackgroundGradientOverrideColor2 && themeAccent.backgroundGradientOverrideColor3 == this.backupBackgroundGradientOverrideColor3 && Math.abs(themeAccent.patternIntensity - this.backupIntensity) <= 0.001f) {
                Theme.ThemeAccent themeAccent2 = this.accent;
                if (themeAccent2.backgroundRotation == this.backupBackgroundRotation) {
                    String str = themeAccent2.patternSlug;
                    TLRPC$TL_wallPaper tLRPC$TL_wallPaper = this.selectedPattern;
                    if (str.equals(tLRPC$TL_wallPaper != null ? tLRPC$TL_wallPaper.slug : "")) {
                        TLRPC$TL_wallPaper tLRPC$TL_wallPaper2 = this.selectedPattern;
                        if (tLRPC$TL_wallPaper2 != null) {
                        }
                        if (tLRPC$TL_wallPaper2 != null) {
                        }
                    }
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("SaveChangesAlertTitle", R.string.SaveChangesAlertTitle));
            builder.setMessage(LocaleController.getString("SaveChangesAlertText", R.string.SaveChangesAlertText));
            builder.setPositiveButton(LocaleController.getString("Save", R.string.Save), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ThemePreviewActivity.this.lambda$checkDiscard$25(dialogInterface, i);
                }
            });
            builder.setNegativeButton(LocaleController.getString("PassportDiscard", R.string.PassportDiscard), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ThemePreviewActivity.this.lambda$checkDiscard$26(dialogInterface, i);
                }
            });
            showDialog(builder.create());
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$25(DialogInterface dialogInterface, int i) {
        this.actionBar2.getActionBarMenuOnItemClick().onItemClick(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$26(DialogInterface dialogInterface, int i) {
        cancelThemeApply(false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.invalidateMotionBackground);
        getNotificationCenter().addObserver(this, NotificationCenter.wallpaperSettedToUser);
        int i = this.screenType;
        if (i == 1 || i == 0) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        }
        int i2 = this.screenType;
        if (i2 == 2 || i2 == 1) {
            Theme.setChangingWallpaper(true);
        }
        if (this.screenType != 0 || this.accent != null) {
            Point point = AndroidUtilities.displaySize;
            int min = Math.min(point.x, point.y);
            Point point2 = AndroidUtilities.displaySize;
            int max = Math.max(point2.x, point2.y);
            this.imageFilter = ((int) (min / AndroidUtilities.density)) + "_" + ((int) (max / AndroidUtilities.density)) + "_f";
            Point point3 = AndroidUtilities.displaySize;
            this.maxWallpaperSize = Math.max(point3.x, point3.y);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersNeedReload);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersDidLoad);
            this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
            if (this.patterns == null) {
                this.patterns = new ArrayList<>();
                MessagesStorage.getInstance(this.currentAccount).getWallpapers();
            }
        } else {
            this.isMotion = Theme.isWallpaperMotion();
        }
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.invalidateMotionBackground);
        getNotificationCenter().removeObserver(this, NotificationCenter.wallpaperSettedToUser);
        FrameLayout frameLayout = this.frameLayout;
        if (frameLayout != null && this.onGlobalLayoutListener != null) {
            frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
        }
        int i = this.screenType;
        if ((i == 2 || i == 1) && this.onSwitchDayNightDelegate == null) {
            AndroidUtilities.runOnUIThread(ThemePreviewActivity$$ExternalSyntheticLambda27.INSTANCE);
        }
        int i2 = this.screenType;
        if (i2 == 2) {
            Bitmap bitmap = this.blurredBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.blurredBitmap = null;
            }
            this.themeDelegate.applyChatServiceMessageColor();
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetNewWallpapper, new Object[0]);
        } else if (i2 == 1 || i2 == 0) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        }
        if (this.screenType != 0 || this.accent != null) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersNeedReload);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersDidLoad);
        }
        super.onFragmentDestroy();
        checkBlur(null);
    }

    private BitmapDrawable checkBlur(Drawable drawable) {
        WeakReference<Drawable> weakReference = this.lastDrawableToBlur;
        if (weakReference != null && weakReference.get() == drawable) {
            return this.blurredDrawable;
        }
        WeakReference<Drawable> weakReference2 = this.lastDrawableToBlur;
        if (weakReference2 != null) {
            weakReference2.clear();
        }
        this.lastDrawableToBlur = null;
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            this.blurredDrawable = null;
            return null;
        }
        this.lastDrawableToBlur = new WeakReference<>(drawable);
        int intrinsicWidth = (int) ((drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight()) * 24.0f);
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, 24, Bitmap.Config.ARGB_8888);
        drawable.setBounds(0, 0, intrinsicWidth, 24);
        if (Build.VERSION.SDK_INT >= 21) {
            ColorFilter colorFilter = drawable.getColorFilter();
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(1.3f);
            AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, 0.94f);
            drawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            drawable.draw(new Canvas(createBitmap));
            drawable.setColorFilter(colorFilter);
        } else {
            drawable.draw(new Canvas(createBitmap));
        }
        Utilities.blurBitmap(createBitmap, 3, 1, createBitmap.getWidth(), createBitmap.getHeight(), createBitmap.getRowBytes());
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getContext().getResources(), createBitmap);
        this.blurredDrawable = bitmapDrawable;
        bitmapDrawable.setFilterBitmap(true);
        return this.blurredDrawable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateBlur() {
        FrameLayout frameLayout = this.dimmingSliderContainer;
        if (frameLayout != null) {
            frameLayout.invalidate();
        }
        FrameLayout frameLayout2 = this.backgroundButtonsContainer;
        if (frameLayout2 != null) {
            int childCount = frameLayout2.getChildCount();
            for (int i = 0; i < childCount; i++) {
                this.backgroundButtonsContainer.getChildAt(i).invalidate();
            }
        }
        FrameLayout frameLayout3 = this.messagesButtonsContainer;
        if (frameLayout3 != null) {
            int childCount2 = frameLayout3.getChildCount();
            for (int i2 = 0; i2 < childCount2; i2++) {
                this.messagesButtonsContainer.getChildAt(i2).invalidate();
            }
        }
        if (this.backgroundCheckBoxView != null) {
            int i3 = 0;
            while (true) {
                WallpaperCheckBoxView[] wallpaperCheckBoxViewArr = this.backgroundCheckBoxView;
                if (i3 >= wallpaperCheckBoxViewArr.length) {
                    break;
                }
                if (wallpaperCheckBoxViewArr[i3] != null) {
                    wallpaperCheckBoxViewArr[i3].setDimAmount(this.shouldShowBrightnessControll ? this.dimAmount * this.progressToDarkTheme : 0.0f);
                    this.backgroundCheckBoxView[i3].invalidate();
                }
                i3++;
            }
        }
        if (this.listView != null) {
            for (int i4 = 0; i4 < this.listView.getChildCount(); i4++) {
                View childAt = this.listView.getChildAt(i4);
                if (childAt instanceof ChatActionCell) {
                    setVisiblePart((ChatActionCell) childAt);
                    childAt.invalidate();
                }
            }
        }
        if (this.listView2 != null) {
            for (int i5 = 0; i5 < this.listView2.getChildCount(); i5++) {
                View childAt2 = this.listView2.getChildAt(i5);
                if (childAt2 instanceof ChatActionCell) {
                    setVisiblePart((ChatActionCell) childAt2);
                    childAt2.invalidate();
                }
            }
        }
        BlurButton blurButton = this.applyButton1;
        if (blurButton != null) {
            blurButton.invalidate();
        }
        BlurButton blurButton2 = this.applyButton2;
        if (blurButton2 != null) {
            blurButton2.invalidate();
        }
        FrameLayout frameLayout4 = this.bottomOverlayChat;
        if (frameLayout4 != null) {
            frameLayout4.invalidate();
        }
    }

    private void setVisiblePart(ChatActionCell chatActionCell) {
        float f;
        float f2;
        BackgroundView backgroundView = this.backgroundImage;
        if (backgroundView == null) {
            return;
        }
        if (backgroundView != null) {
            if (this.themeDelegate.serviceBitmap == null) {
                f2 = this.currentScrollOffset + 0.0f;
            } else {
                float width = this.themeDelegate.serviceBitmap.getWidth();
                f2 = ((this.backgroundImage.getMeasuredWidth() - (width * Math.max(this.backgroundImage.getMeasuredWidth() / width, this.backgroundImage.getMeasuredHeight() / this.themeDelegate.serviceBitmap.getHeight()))) / 2.0f) + this.currentScrollOffset + 0.0f;
            }
            f = (-this.backgroundImage.ty) + 0.0f;
        } else {
            f = 0.0f;
            f2 = 0.0f;
        }
        chatActionCell.setVisiblePart(chatActionCell.getY() - f, f2, this.backgroundImage.getMeasuredHeight(), this.shouldShowBrightnessControll ? this.dimAmount * this.progressToDarkTheme : 0.0f);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationStart(boolean z, boolean z2) {
        super.onTransitionAnimationStart(z, z2);
        if (z || this.screenType != 2) {
            return;
        }
        this.themeDelegate.applyChatServiceMessageColor();
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetNewWallpapper, new Object[0]);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBottomSheetCreated() {
        super.onBottomSheetCreated();
        INavigationLayout iNavigationLayout = this.parentLayout;
        if (iNavigationLayout == null || iNavigationLayout.getBottomSheet() == null) {
            return;
        }
        this.parentLayout.getBottomSheet().fixNavigationBar(getThemedColor(Theme.key_dialogBackground));
        if (this.screenType != 2 || this.dialogId == 0) {
            return;
        }
        this.parentLayout.getBottomSheet().setOverlayNavBarColor(-16777216);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        DialogsAdapter dialogsAdapter = this.dialogsAdapter;
        if (dialogsAdapter != null) {
            dialogsAdapter.notifyDataSetChanged();
        }
        MessagesAdapter messagesAdapter = this.messagesAdapter;
        if (messagesAdapter != null) {
            messagesAdapter.notifyDataSetChanged();
        }
        if (this.isMotion) {
            this.parallaxEffect.setEnabled(true);
        }
        Theme.disallowChangeServiceMessageColor = true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        if (this.isMotion) {
            this.parallaxEffect.setEnabled(false);
        }
        Theme.disallowChangeServiceMessageColor = false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        if (this.screenType != 2) {
            return false;
        }
        return !this.hasScrollingBackground || motionEvent == null || motionEvent.getY() <= ((float) (AndroidUtilities.statusBarHeight + ActionBar.getCurrentActionBarHeight()));
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
        updateButtonState(true, z);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
        updateButtonState(false, true);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBlurred() {
        if (this.isBlurred && this.blurredBitmap == null) {
            Bitmap bitmap = this.currentWallpaperBitmap;
            if (bitmap != null) {
                this.originalBitmap = bitmap;
                this.blurredBitmap = Utilities.blurWallpaper(bitmap);
            } else {
                ImageReceiver imageReceiver = this.backgroundImage.getImageReceiver();
                if (imageReceiver.hasNotThumb() || imageReceiver.hasStaticThumb()) {
                    this.originalBitmap = imageReceiver.getBitmap();
                    this.blurredBitmap = Utilities.blurWallpaper(imageReceiver.getBitmap());
                }
            }
        }
        if (this.isBlurred) {
            Bitmap bitmap2 = this.blurredBitmap;
            if (bitmap2 != null) {
                this.backgroundImage.setImageBitmap(bitmap2);
                return;
            }
            return;
        }
        setCurrentImage(false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (checkDiscard()) {
            cancelThemeApply(true);
            return true;
        }
        return false;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TLRPC$TL_wallPaper tLRPC$TL_wallPaper;
        String str;
        if (i == NotificationCenter.emojiLoaded) {
            RecyclerListView recyclerListView = this.listView;
            if (recyclerListView == null) {
                return;
            }
            int childCount = recyclerListView.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = this.listView.getChildAt(i3);
                if (childAt instanceof DialogCell) {
                    ((DialogCell) childAt).update(0);
                }
            }
        } else if (i == NotificationCenter.invalidateMotionBackground) {
            RecyclerListView recyclerListView2 = this.listView2;
            if (recyclerListView2 != null) {
                recyclerListView2.invalidateViews();
            }
        } else if (i == NotificationCenter.didSetNewWallpapper) {
            if (this.page2 != null) {
                setCurrentImage(true);
            }
        } else if (i == NotificationCenter.wallpapersNeedReload) {
            Object obj = this.currentWallpaper;
            if (obj instanceof WallpapersListActivity.FileWallpaper) {
                WallpapersListActivity.FileWallpaper fileWallpaper = (WallpapersListActivity.FileWallpaper) obj;
                if (fileWallpaper.slug == null) {
                    fileWallpaper.slug = (String) objArr[0];
                }
            }
        } else {
            long j = 0;
            if (i == NotificationCenter.wallpapersDidLoad) {
                ArrayList arrayList = (ArrayList) objArr[0];
                this.patterns.clear();
                this.patternsDict.clear();
                int size = arrayList.size();
                boolean z = false;
                for (int i4 = 0; i4 < size; i4++) {
                    TLRPC$WallPaper tLRPC$WallPaper = (TLRPC$WallPaper) arrayList.get(i4);
                    if ((tLRPC$WallPaper instanceof TLRPC$TL_wallPaper) && tLRPC$WallPaper.pattern) {
                        TLRPC$Document tLRPC$Document = tLRPC$WallPaper.document;
                        if (tLRPC$Document != null && !this.patternsDict.containsKey(Long.valueOf(tLRPC$Document.id))) {
                            this.patterns.add(tLRPC$WallPaper);
                            this.patternsDict.put(Long.valueOf(tLRPC$WallPaper.document.id), tLRPC$WallPaper);
                        }
                        Theme.ThemeAccent themeAccent = this.accent;
                        if (themeAccent != null && (str = themeAccent.patternSlug) != null && str.equals(tLRPC$WallPaper.slug)) {
                            this.selectedPattern = (TLRPC$TL_wallPaper) tLRPC$WallPaper;
                            setCurrentImage(false);
                            updateButtonState(false, false);
                        } else if (this.accent == null) {
                            TLRPC$TL_wallPaper tLRPC$TL_wallPaper2 = this.selectedPattern;
                            if (tLRPC$TL_wallPaper2 != null) {
                                String str2 = tLRPC$TL_wallPaper2.slug;
                                if (str2 != null) {
                                    if (!str2.equals(tLRPC$WallPaper.slug)) {
                                    }
                                }
                            }
                        }
                        z = true;
                    }
                }
                if (!z && (tLRPC$TL_wallPaper = this.selectedPattern) != null) {
                    this.patterns.add(0, tLRPC$TL_wallPaper);
                }
                PatternsAdapter patternsAdapter = this.patternsAdapter;
                if (patternsAdapter != null) {
                    patternsAdapter.notifyDataSetChanged();
                }
                int size2 = arrayList.size();
                for (int i5 = 0; i5 < size2; i5++) {
                    TLRPC$WallPaper tLRPC$WallPaper2 = (TLRPC$WallPaper) arrayList.get(i5);
                    if (tLRPC$WallPaper2 instanceof TLRPC$TL_wallPaper) {
                        j = MediaDataController.calcHash(j, tLRPC$WallPaper2.id);
                    }
                }
                TLRPC$TL_account_getWallPapers tLRPC$TL_account_getWallPapers = new TLRPC$TL_account_getWallPapers();
                tLRPC$TL_account_getWallPapers.hash = j;
                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getWallPapers, new RequestDelegate() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda31
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        ThemePreviewActivity.this.lambda$didReceivedNotification$31(tLObject, tLRPC$TL_error);
                    }
                }), this.classGuid);
            } else if (i == NotificationCenter.wallpaperSettedToUser && this.dialogId != 0) {
                finishFragment();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$31(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                ThemePreviewActivity.this.lambda$didReceivedNotification$30(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$30(TLObject tLObject) {
        Theme.ThemeAccent themeAccent;
        TLRPC$TL_wallPaper tLRPC$TL_wallPaper;
        String str;
        if (tLObject instanceof TLRPC$TL_account_wallPapers) {
            TLRPC$TL_account_wallPapers tLRPC$TL_account_wallPapers = (TLRPC$TL_account_wallPapers) tLObject;
            this.patterns.clear();
            this.patternsDict.clear();
            int size = tLRPC$TL_account_wallPapers.wallpapers.size();
            boolean z = false;
            for (int i = 0; i < size; i++) {
                if (tLRPC$TL_account_wallPapers.wallpapers.get(i) instanceof TLRPC$TL_wallPaper) {
                    TLRPC$TL_wallPaper tLRPC$TL_wallPaper2 = (TLRPC$TL_wallPaper) tLRPC$TL_account_wallPapers.wallpapers.get(i);
                    if (tLRPC$TL_wallPaper2.pattern) {
                        TLRPC$Document tLRPC$Document = tLRPC$TL_wallPaper2.document;
                        if (tLRPC$Document != null && !this.patternsDict.containsKey(Long.valueOf(tLRPC$Document.id))) {
                            this.patterns.add(tLRPC$TL_wallPaper2);
                            this.patternsDict.put(Long.valueOf(tLRPC$TL_wallPaper2.document.id), tLRPC$TL_wallPaper2);
                        }
                        Theme.ThemeAccent themeAccent2 = this.accent;
                        if (themeAccent2 != null && (str = themeAccent2.patternSlug) != null && str.equals(tLRPC$TL_wallPaper2.slug)) {
                            this.selectedPattern = tLRPC$TL_wallPaper2;
                            setCurrentImage(false);
                            updateButtonState(false, false);
                        } else if (this.accent == null) {
                            TLRPC$TL_wallPaper tLRPC$TL_wallPaper3 = this.selectedPattern;
                            if (tLRPC$TL_wallPaper3 != null) {
                                String str2 = tLRPC$TL_wallPaper3.slug;
                                if (str2 != null) {
                                    if (!str2.equals(tLRPC$TL_wallPaper2.slug)) {
                                    }
                                }
                            }
                        }
                        z = true;
                    }
                }
            }
            if (!z && (tLRPC$TL_wallPaper = this.selectedPattern) != null) {
                this.patterns.add(0, tLRPC$TL_wallPaper);
            }
            PatternsAdapter patternsAdapter = this.patternsAdapter;
            if (patternsAdapter != null) {
                patternsAdapter.notifyDataSetChanged();
            }
            MessagesStorage.getInstance(this.currentAccount).putWallpapers(tLRPC$TL_account_wallPapers.wallpapers, 1);
        }
        if (this.selectedPattern != null || (themeAccent = this.accent) == null || TextUtils.isEmpty(themeAccent.patternSlug)) {
            return;
        }
        TLRPC$TL_account_getWallPaper tLRPC$TL_account_getWallPaper = new TLRPC$TL_account_getWallPaper();
        TLRPC$TL_inputWallPaperSlug tLRPC$TL_inputWallPaperSlug = new TLRPC$TL_inputWallPaperSlug();
        tLRPC$TL_inputWallPaperSlug.slug = this.accent.patternSlug;
        tLRPC$TL_account_getWallPaper.wallpaper = tLRPC$TL_inputWallPaperSlug;
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(getConnectionsManager().sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda30
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
                ThemePreviewActivity.this.lambda$didReceivedNotification$29(tLObject2, tLRPC$TL_error);
            }
        }), this.classGuid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$29(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                ThemePreviewActivity.this.lambda$didReceivedNotification$28(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$28(TLObject tLObject) {
        if (tLObject instanceof TLRPC$TL_wallPaper) {
            TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) tLObject;
            if (tLRPC$TL_wallPaper.pattern) {
                this.selectedPattern = tLRPC$TL_wallPaper;
                setCurrentImage(false);
                updateButtonState(false, false);
                this.patterns.add(0, this.selectedPattern);
                PatternsAdapter patternsAdapter = this.patternsAdapter;
                if (patternsAdapter != null) {
                    patternsAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelThemeApply(boolean z) {
        if (this.screenType == 2) {
            if (z) {
                return;
            }
            finishFragment();
            return;
        }
        Theme.applyPreviousTheme();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        if (this.screenType == 1) {
            if (this.editingTheme) {
                Theme.ThemeAccent themeAccent = this.accent;
                themeAccent.accentColor = this.backupAccentColor;
                themeAccent.accentColor2 = this.backupAccentColor2;
                themeAccent.myMessagesAccentColor = this.backupMyMessagesAccentColor;
                themeAccent.myMessagesGradientAccentColor1 = this.backupMyMessagesGradientAccentColor1;
                themeAccent.myMessagesGradientAccentColor2 = this.backupMyMessagesGradientAccentColor2;
                themeAccent.myMessagesGradientAccentColor3 = this.backupMyMessagesGradientAccentColor3;
                themeAccent.myMessagesAnimated = this.backupMyMessagesAnimated;
                themeAccent.backgroundOverrideColor = this.backupBackgroundOverrideColor;
                themeAccent.backgroundGradientOverrideColor1 = this.backupBackgroundGradientOverrideColor1;
                themeAccent.backgroundGradientOverrideColor2 = this.backupBackgroundGradientOverrideColor2;
                themeAccent.backgroundGradientOverrideColor3 = this.backupBackgroundGradientOverrideColor3;
                themeAccent.backgroundRotation = this.backupBackgroundRotation;
                themeAccent.patternSlug = this.backupSlug;
                themeAccent.patternIntensity = this.backupIntensity;
            }
            Theme.saveThemeAccents(this.applyingTheme, false, true, false, false);
        } else {
            if (this.accent != null) {
                Theme.saveThemeAccents(this.applyingTheme, false, this.deleteOnCancel, false, false);
            }
            this.parentLayout.rebuildAllFragmentViews(false, false);
            if (this.deleteOnCancel) {
                Theme.ThemeInfo themeInfo = this.applyingTheme;
                if (themeInfo.pathToFile != null && !Theme.isThemeInstalled(themeInfo)) {
                    new File(this.applyingTheme.pathToFile).delete();
                }
            }
        }
        if (z) {
            return;
        }
        finishFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getButtonsColor(int i) {
        return this.useDefaultThemeForButtons ? Theme.getDefaultColor(i) : getThemedColor(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleApplyColor(int i, int i2, boolean z) {
        if (i2 == -1) {
            int i3 = this.colorType;
            if (i3 == 1 || i3 == 2) {
                long j = this.backupBackgroundOverrideColor;
                if (j != 0) {
                    this.accent.backgroundOverrideColor = j;
                } else {
                    this.accent.backgroundOverrideColor = 0L;
                }
                long j2 = this.backupBackgroundGradientOverrideColor1;
                if (j2 != 0) {
                    this.accent.backgroundGradientOverrideColor1 = j2;
                } else {
                    this.accent.backgroundGradientOverrideColor1 = 0L;
                }
                long j3 = this.backupBackgroundGradientOverrideColor2;
                if (j3 != 0) {
                    this.accent.backgroundGradientOverrideColor2 = j3;
                } else {
                    this.accent.backgroundGradientOverrideColor2 = 0L;
                }
                long j4 = this.backupBackgroundGradientOverrideColor3;
                if (j4 != 0) {
                    this.accent.backgroundGradientOverrideColor3 = j4;
                } else {
                    this.accent.backgroundGradientOverrideColor3 = 0L;
                }
                this.accent.backgroundRotation = this.backupBackgroundRotation;
                if (i3 == 2) {
                    int defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper);
                    int defaultAccentColor2 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to1);
                    int defaultAccentColor3 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to2);
                    int defaultAccentColor4 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to3);
                    Theme.ThemeAccent themeAccent = this.accent;
                    int i4 = (int) themeAccent.backgroundGradientOverrideColor1;
                    int i5 = (int) themeAccent.backgroundGradientOverrideColor2;
                    int i6 = (int) themeAccent.backgroundGradientOverrideColor3;
                    int i7 = (int) themeAccent.backgroundOverrideColor;
                    ColorPicker colorPicker = this.colorPicker;
                    if (i6 != 0) {
                        defaultAccentColor4 = i6;
                    }
                    colorPicker.setColor(defaultAccentColor4, 3);
                    ColorPicker colorPicker2 = this.colorPicker;
                    if (i5 != 0) {
                        defaultAccentColor3 = i5;
                    }
                    colorPicker2.setColor(defaultAccentColor3, 2);
                    ColorPicker colorPicker3 = this.colorPicker;
                    if (i4 != 0) {
                        defaultAccentColor2 = i4;
                    }
                    colorPicker3.setColor(defaultAccentColor2, 1);
                    ColorPicker colorPicker4 = this.colorPicker;
                    if (i7 != 0) {
                        defaultAccentColor = i7;
                    }
                    colorPicker4.setColor(defaultAccentColor, 0);
                }
            }
            int i8 = this.colorType;
            if (i8 == 1 || i8 == 3) {
                int i9 = this.backupMyMessagesAccentColor;
                if (i9 != 0) {
                    this.accent.myMessagesAccentColor = i9;
                } else {
                    this.accent.myMessagesAccentColor = 0;
                }
                int i10 = this.backupMyMessagesGradientAccentColor1;
                if (i10 != 0) {
                    this.accent.myMessagesGradientAccentColor1 = i10;
                } else {
                    this.accent.myMessagesGradientAccentColor1 = 0;
                }
                int i11 = this.backupMyMessagesGradientAccentColor2;
                if (i11 != 0) {
                    this.accent.myMessagesGradientAccentColor2 = i11;
                } else {
                    this.accent.myMessagesGradientAccentColor2 = 0;
                }
                int i12 = this.backupMyMessagesGradientAccentColor3;
                if (i12 != 0) {
                    this.accent.myMessagesGradientAccentColor3 = i12;
                } else {
                    this.accent.myMessagesGradientAccentColor3 = 0;
                }
                if (i8 == 3) {
                    this.colorPicker.setColor(this.accent.myMessagesGradientAccentColor3, 3);
                    this.colorPicker.setColor(this.accent.myMessagesGradientAccentColor2, 2);
                    this.colorPicker.setColor(this.accent.myMessagesGradientAccentColor1, 1);
                    ColorPicker colorPicker5 = this.colorPicker;
                    Theme.ThemeAccent themeAccent2 = this.accent;
                    int i13 = themeAccent2.myMessagesAccentColor;
                    if (i13 == 0) {
                        i13 = themeAccent2.accentColor;
                    }
                    colorPicker5.setColor(i13, 0);
                }
            }
            Theme.refreshThemeColors();
            this.listView2.invalidateViews();
            return;
        }
        int i14 = this.lastPickedColorNum;
        if (i14 != -1 && i14 != i2) {
            this.applyColorAction.run();
        }
        this.lastPickedColor = i;
        this.lastPickedColorNum = i2;
        if (z) {
            this.applyColorAction.run();
        } else if (this.applyColorScheduled) {
        } else {
            this.applyColorScheduled = true;
            this.fragmentView.postDelayed(this.applyColorAction, 16L);
        }
    }

    private void applyColor(int i, int i2) {
        int i3 = this.colorType;
        if (i3 == 1) {
            if (i2 == 0) {
                this.accent.accentColor = i;
                Theme.refreshThemeColors();
            } else if (i2 == 1) {
                this.accent.accentColor2 = i;
                Theme.refreshThemeColors(true, true);
                this.listView2.invalidateViews();
                this.colorPicker.setHasChanges(hasChanges(this.colorType));
                updatePlayAnimationView(true);
            }
        } else if (i3 == 2) {
            if (this.lastPickedColorNum == 0) {
                this.accent.backgroundOverrideColor = i;
            } else if (i2 == 1) {
                int defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to1);
                if (i == 0 && defaultAccentColor != 0) {
                    this.accent.backgroundGradientOverrideColor1 = 4294967296L;
                } else {
                    this.accent.backgroundGradientOverrideColor1 = i;
                }
            } else if (i2 == 2) {
                int defaultAccentColor2 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to2);
                if (i == 0 && defaultAccentColor2 != 0) {
                    this.accent.backgroundGradientOverrideColor2 = 4294967296L;
                } else {
                    this.accent.backgroundGradientOverrideColor2 = i;
                }
            } else if (i2 == 3) {
                int defaultAccentColor3 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to3);
                if (i == 0 && defaultAccentColor3 != 0) {
                    this.accent.backgroundGradientOverrideColor3 = 4294967296L;
                } else {
                    this.accent.backgroundGradientOverrideColor3 = i;
                }
            }
            Theme.refreshThemeColors(true, false);
            this.colorPicker.setHasChanges(hasChanges(this.colorType));
            updatePlayAnimationView(true);
        } else if (i3 == 3) {
            int i4 = this.lastPickedColorNum;
            if (i4 == 0) {
                this.accent.myMessagesAccentColor = i;
            } else if (i4 == 1) {
                this.accent.myMessagesGradientAccentColor1 = i;
            } else if (i4 == 2) {
                Theme.ThemeAccent themeAccent = this.accent;
                int i5 = themeAccent.myMessagesGradientAccentColor2;
                themeAccent.myMessagesGradientAccentColor2 = i;
                if (i5 != 0 && i == 0) {
                    this.messagesAdapter.notifyItemRemoved(0);
                } else if (i5 == 0 && i != 0) {
                    this.messagesAdapter.notifyItemInserted(0);
                    showAnimationHint();
                }
            } else {
                this.accent.myMessagesGradientAccentColor3 = i;
            }
            int i6 = this.lastPickedColorNum;
            if (i6 >= 0) {
                this.messagesCheckBoxView[1].setColor(i6, i);
            }
            Theme.refreshThemeColors(true, true);
            this.listView2.invalidateViews();
            this.colorPicker.setHasChanges(hasChanges(this.colorType));
            updatePlayAnimationView(true);
        }
        int size = this.themeDescriptions.size();
        for (int i7 = 0; i7 < size; i7++) {
            ThemeDescription themeDescription = this.themeDescriptions.get(i7);
            themeDescription.setColor(getThemedColor(themeDescription.getCurrentKey()), false, false);
        }
        this.listView.invalidateViews();
        this.listView2.invalidateViews();
        View view = this.dotsContainer;
        if (view != null) {
            view.invalidate();
        }
    }

    private void updateButtonState(boolean z, boolean z2) {
        File httpFilePath;
        String name;
        int i;
        long j;
        FrameLayout frameLayout;
        Object obj = this.selectedPattern;
        if (obj == null) {
            obj = this.currentWallpaper;
        }
        boolean z3 = obj instanceof TLRPC$TL_wallPaper;
        if (z3 || (obj instanceof MediaController.SearchImage)) {
            if (z3) {
                TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) obj;
                name = FileLoader.getAttachFileName(tLRPC$TL_wallPaper.document);
                if (TextUtils.isEmpty(name)) {
                    return;
                }
                httpFilePath = FileLoader.getInstance(this.currentAccount).getPathToAttach(tLRPC$TL_wallPaper.document, true);
                j = tLRPC$TL_wallPaper.document.size;
            } else {
                MediaController.SearchImage searchImage = (MediaController.SearchImage) obj;
                TLRPC$Photo tLRPC$Photo = searchImage.photo;
                if (tLRPC$Photo != null) {
                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, this.maxWallpaperSize, true);
                    httpFilePath = FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, true);
                    name = FileLoader.getAttachFileName(closestPhotoSizeWithSize);
                    i = closestPhotoSizeWithSize.size;
                } else {
                    httpFilePath = ImageLoader.getHttpFilePath(searchImage.imageUrl, "jpg");
                    name = httpFilePath.getName();
                    i = searchImage.size;
                }
                j = i;
                if (TextUtils.isEmpty(name)) {
                    return;
                }
            }
            boolean exists = httpFilePath.exists();
            if (exists) {
                DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                this.backgroundImage.invalidate();
                if (this.screenType == 2) {
                    if (j != 0 && this.dialogId == 0) {
                        this.actionBar2.setSubtitle(AndroidUtilities.formatFileSize(j));
                    } else {
                        this.actionBar2.setSubtitle(null);
                    }
                }
            } else {
                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(name, null, this);
                if (this.screenType == 2 && this.dialogId == 0) {
                    this.actionBar2.setSubtitle(LocaleController.getString("LoadingFullImage", R.string.LoadingFullImage));
                }
                this.backgroundImage.invalidate();
            }
            if (this.selectedPattern == null && (frameLayout = this.backgroundButtonsContainer) != null) {
                frameLayout.setAlpha(exists ? 1.0f : 0.5f);
            }
            int i2 = this.screenType;
            if (i2 == 0) {
                this.doneButton.setEnabled(exists);
                this.doneButton.setAlpha(exists ? 1.0f : 0.5f);
            } else if (i2 == 2) {
                this.bottomOverlayChat.setEnabled(exists);
                BlurButton blurButton = this.applyButton1;
                if (blurButton != null) {
                    blurButton.setAlpha(exists ? 1.0f : 0.5f);
                }
                BlurButton blurButton2 = this.applyButton2;
                if (blurButton2 != null) {
                    blurButton2.setAlpha(exists ? 1.0f : 0.5f);
                }
            } else {
                this.saveItem.setEnabled(exists);
                this.saveItem.setAlpha(exists ? 1.0f : 0.5f);
            }
        }
    }

    public void setDelegate(WallpaperActivityDelegate wallpaperActivityDelegate) {
        this.delegate = wallpaperActivityDelegate;
    }

    public void setPatterns(ArrayList<Object> arrayList) {
        this.patterns = arrayList;
        if (this.screenType == 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
            WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper) this.currentWallpaper;
            if (colorWallpaper.patternId != 0) {
                int i = 0;
                int size = arrayList.size();
                while (true) {
                    if (i >= size) {
                        break;
                    }
                    TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) this.patterns.get(i);
                    if (tLRPC$TL_wallPaper.id == colorWallpaper.patternId) {
                        this.selectedPattern = tLRPC$TL_wallPaper;
                        break;
                    }
                    i++;
                }
                this.currentIntensity = colorWallpaper.intensity;
            }
        }
    }

    private void showAnimationHint() {
        if (this.page2 == null || this.messagesCheckBoxView == null || this.accent.myMessagesGradientAccentColor2 == 0) {
            return;
        }
        final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        if (globalMainSettings.getBoolean("bganimationhint", false)) {
            return;
        }
        if (this.animationHint == null) {
            HintView hintView = new HintView(getParentActivity(), 8);
            this.animationHint = hintView;
            hintView.setShowingDuration(5000L);
            this.animationHint.setAlpha(0.0f);
            this.animationHint.setVisibility(4);
            this.animationHint.setText(LocaleController.getString("BackgroundAnimateInfo", R.string.BackgroundAnimateInfo));
            this.animationHint.setExtraTranslationY(AndroidUtilities.dp(6.0f));
            this.frameLayout.addView(this.animationHint, LayoutHelper.createFrame(-2, -2.0f, 51, 10.0f, 0.0f, 10.0f, 0.0f));
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                ThemePreviewActivity.this.lambda$showAnimationHint$32(globalMainSettings);
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAnimationHint$32(SharedPreferences sharedPreferences) {
        if (this.colorType != 3) {
            return;
        }
        sharedPreferences.edit().putBoolean("bganimationhint", true).commit();
        this.animationHint.showForView(this.messagesCheckBoxView[0], true);
    }

    private void updateSelectedPattern(boolean z) {
        int childCount = this.patternsListView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.patternsListView.getChildAt(i);
            if (childAt instanceof PatternCell) {
                ((PatternCell) childAt).updateSelected(z);
            }
        }
    }

    private void updateMotionButton() {
        int i = this.screenType;
        if (i == 1 || i == 2) {
            if (this.selectedPattern == null && (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                this.backgroundCheckBoxView[2].setChecked(false, true);
            }
            this.backgroundCheckBoxView[this.selectedPattern != null ? (char) 2 : (char) 0].setVisibility(0);
            AnimatorSet animatorSet = new AnimatorSet();
            Animator[] animatorArr = new Animator[2];
            WallpaperCheckBoxView wallpaperCheckBoxView = this.backgroundCheckBoxView[2];
            Property property = View.ALPHA;
            float[] fArr = new float[1];
            fArr[0] = this.selectedPattern != null ? 1.0f : 0.0f;
            animatorArr[0] = ObjectAnimator.ofFloat(wallpaperCheckBoxView, property, fArr);
            WallpaperCheckBoxView wallpaperCheckBoxView2 = this.backgroundCheckBoxView[0];
            Property property2 = View.ALPHA;
            float[] fArr2 = new float[1];
            fArr2[0] = this.selectedPattern != null ? 0.0f : 1.0f;
            animatorArr[1] = ObjectAnimator.ofFloat(wallpaperCheckBoxView2, property2, fArr2);
            animatorSet.playTogether(animatorArr);
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.34
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ThemePreviewActivity.this.backgroundCheckBoxView[ThemePreviewActivity.this.selectedPattern != null ? (char) 0 : (char) 2].setVisibility(4);
                }
            });
            animatorSet.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            animatorSet.setDuration(200L);
            animatorSet.start();
            return;
        }
        boolean isEnabled = this.backgroundCheckBoxView[0].isEnabled();
        TLRPC$TL_wallPaper tLRPC$TL_wallPaper = this.selectedPattern;
        if (isEnabled == (tLRPC$TL_wallPaper != null)) {
            return;
        }
        if (tLRPC$TL_wallPaper == null) {
            this.backgroundCheckBoxView[0].setChecked(false, true);
        }
        this.backgroundCheckBoxView[0].setEnabled(this.selectedPattern != null);
        if (this.selectedPattern != null) {
            this.backgroundCheckBoxView[0].setVisibility(0);
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        int dp = (((FrameLayout.LayoutParams) this.backgroundCheckBoxView[1].getLayoutParams()).width + AndroidUtilities.dp(9.0f)) / 2;
        Animator[] animatorArr2 = new Animator[1];
        WallpaperCheckBoxView wallpaperCheckBoxView3 = this.backgroundCheckBoxView[0];
        Property property3 = View.ALPHA;
        float[] fArr3 = new float[1];
        fArr3[0] = this.selectedPattern == null ? 0.0f : 1.0f;
        animatorArr2[0] = ObjectAnimator.ofFloat(wallpaperCheckBoxView3, property3, fArr3);
        animatorSet2.playTogether(animatorArr2);
        Animator[] animatorArr3 = new Animator[1];
        WallpaperCheckBoxView wallpaperCheckBoxView4 = this.backgroundCheckBoxView[0];
        Property property4 = View.TRANSLATION_X;
        float[] fArr4 = new float[1];
        fArr4[0] = this.selectedPattern != null ? 0.0f : dp;
        animatorArr3[0] = ObjectAnimator.ofFloat(wallpaperCheckBoxView4, property4, fArr4);
        animatorSet2.playTogether(animatorArr3);
        Animator[] animatorArr4 = new Animator[1];
        WallpaperCheckBoxView wallpaperCheckBoxView5 = this.backgroundCheckBoxView[1];
        Property property5 = View.TRANSLATION_X;
        float[] fArr5 = new float[1];
        fArr5[0] = this.selectedPattern == null ? -dp : 0.0f;
        animatorArr4[0] = ObjectAnimator.ofFloat(wallpaperCheckBoxView5, property5, fArr5);
        animatorSet2.playTogether(animatorArr4);
        animatorSet2.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        animatorSet2.setDuration(200L);
        animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.35
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (ThemePreviewActivity.this.selectedPattern == null) {
                    ThemePreviewActivity.this.backgroundCheckBoxView[0].setVisibility(4);
                }
            }
        });
        animatorSet2.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPatternsView(final int i, final boolean z, boolean z2) {
        int indexOf;
        final boolean z3 = z && i == 1 && this.selectedPattern != null;
        if (z) {
            if (i == 0) {
                if (this.screenType == 2) {
                    this.previousBackgroundColor = this.backgroundColor;
                    int i2 = this.backgroundGradientColor1;
                    this.previousBackgroundGradientColor1 = i2;
                    int i3 = this.backgroundGradientColor2;
                    this.previousBackgroundGradientColor2 = i3;
                    int i4 = this.backgroundGradientColor3;
                    this.previousBackgroundGradientColor3 = i4;
                    int i5 = this.backupBackgroundRotation;
                    this.previousBackgroundRotation = i5;
                    this.colorPicker.setType(0, false, 4, i4 != 0 ? 4 : i3 != 0 ? 3 : i2 != 0 ? 2 : 1, false, i5, false);
                    this.colorPicker.setColor(this.backgroundGradientColor3, 3);
                    this.colorPicker.setColor(this.backgroundGradientColor2, 2);
                    this.colorPicker.setColor(this.backgroundGradientColor1, 1);
                    this.colorPicker.setColor(this.backgroundColor, 0);
                }
            } else {
                this.previousSelectedPattern = this.selectedPattern;
                this.previousIntensity = this.currentIntensity;
                this.patternsAdapter.notifyDataSetChanged();
                ArrayList<Object> arrayList = this.patterns;
                if (arrayList != null) {
                    TLRPC$TL_wallPaper tLRPC$TL_wallPaper = this.selectedPattern;
                    if (tLRPC$TL_wallPaper == null) {
                        indexOf = 0;
                    } else {
                        indexOf = arrayList.indexOf(tLRPC$TL_wallPaper) + (this.screenType == 2 ? 1 : 0);
                    }
                    this.patternsLayoutManager.scrollToPositionWithOffset(indexOf, (this.patternsListView.getMeasuredWidth() - AndroidUtilities.dp(124.0f)) / 2);
                }
            }
        }
        int i6 = this.screenType;
        if (i6 == 1 || i6 == 2) {
            this.backgroundCheckBoxView[z3 ? (char) 2 : (char) 0].setVisibility(0);
        }
        if (i == 1 && !this.intensitySeekBar.isTwoSided()) {
            float f = this.currentIntensity;
            if (f < 0.0f) {
                float f2 = -f;
                this.currentIntensity = f2;
                this.intensitySeekBar.setProgress(f2);
            }
        }
        if (z2) {
            this.patternViewAnimation = new AnimatorSet();
            ArrayList arrayList2 = new ArrayList();
            int i7 = i == 0 ? 1 : 0;
            if (z) {
                this.patternLayout[i].setVisibility(0);
                int i8 = this.screenType;
                if (i8 == 1) {
                    RecyclerListView recyclerListView = this.listView2;
                    Property property = View.TRANSLATION_Y;
                    float[] fArr = new float[1];
                    fArr[0] = i == 1 ? -AndroidUtilities.dp(21.0f) : 0.0f;
                    arrayList2.add(ObjectAnimator.ofFloat(recyclerListView, property, fArr));
                    WallpaperCheckBoxView wallpaperCheckBoxView = this.backgroundCheckBoxView[2];
                    Property property2 = View.ALPHA;
                    float[] fArr2 = new float[1];
                    fArr2[0] = z3 ? 1.0f : 0.0f;
                    arrayList2.add(ObjectAnimator.ofFloat(wallpaperCheckBoxView, property2, fArr2));
                    WallpaperCheckBoxView wallpaperCheckBoxView2 = this.backgroundCheckBoxView[0];
                    Property property3 = View.ALPHA;
                    float[] fArr3 = new float[1];
                    fArr3[0] = z3 ? 0.0f : 1.0f;
                    arrayList2.add(ObjectAnimator.ofFloat(wallpaperCheckBoxView2, property3, fArr3));
                    if (i == 1) {
                        arrayList2.add(ObjectAnimator.ofFloat(this.patternLayout[i], View.ALPHA, 0.0f, 1.0f));
                    } else {
                        this.patternLayout[i].setAlpha(1.0f);
                        arrayList2.add(ObjectAnimator.ofFloat(this.patternLayout[i7], View.ALPHA, 0.0f));
                    }
                    this.colorPicker.hideKeyboard();
                } else if (i8 == 2) {
                    RecyclerListView recyclerListView2 = this.listView2;
                    Property property4 = View.TRANSLATION_Y;
                    float[] fArr4 = new float[1];
                    fArr4[0] = (-this.patternLayout[i].getMeasuredHeight()) + AndroidUtilities.dp((this.applyButton2 == null ? 0 : 58) + 72) + (insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0);
                    arrayList2.add(ObjectAnimator.ofFloat(recyclerListView2, property4, fArr4));
                    WallpaperCheckBoxView wallpaperCheckBoxView3 = this.backgroundCheckBoxView[2];
                    Property property5 = View.ALPHA;
                    float[] fArr5 = new float[1];
                    fArr5[0] = z3 ? 1.0f : 0.0f;
                    arrayList2.add(ObjectAnimator.ofFloat(wallpaperCheckBoxView3, property5, fArr5));
                    WallpaperCheckBoxView wallpaperCheckBoxView4 = this.backgroundCheckBoxView[0];
                    Property property6 = View.ALPHA;
                    float[] fArr6 = new float[1];
                    fArr6[0] = z3 ? 0.0f : 1.0f;
                    arrayList2.add(ObjectAnimator.ofFloat(wallpaperCheckBoxView4, property6, fArr6));
                    if (this.patternLayout[i7].getVisibility() == 0) {
                        arrayList2.add(ObjectAnimator.ofFloat(this.patternLayout[i7], View.ALPHA, 0.0f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.patternLayout[i], View.ALPHA, 0.0f, 1.0f));
                        this.patternLayout[i].setTranslationY(0.0f);
                    } else {
                        FrameLayout[] frameLayoutArr = this.patternLayout;
                        arrayList2.add(ObjectAnimator.ofFloat(frameLayoutArr[i], View.TRANSLATION_Y, frameLayoutArr[i].getMeasuredHeight(), 0.0f));
                    }
                } else {
                    if (i == 1) {
                        arrayList2.add(ObjectAnimator.ofFloat(this.patternLayout[i], View.ALPHA, 0.0f, 1.0f));
                    } else {
                        this.patternLayout[i].setAlpha(1.0f);
                        arrayList2.add(ObjectAnimator.ofFloat(this.patternLayout[i7], View.ALPHA, 0.0f));
                    }
                    this.colorPicker.hideKeyboard();
                }
            } else {
                arrayList2.add(ObjectAnimator.ofFloat(this.listView2, View.TRANSLATION_Y, 0.0f));
                FrameLayout[] frameLayoutArr2 = this.patternLayout;
                arrayList2.add(ObjectAnimator.ofFloat(frameLayoutArr2[i], View.TRANSLATION_Y, frameLayoutArr2[i].getMeasuredHeight()));
                arrayList2.add(ObjectAnimator.ofFloat(this.backgroundCheckBoxView[0], View.ALPHA, 1.0f));
                arrayList2.add(ObjectAnimator.ofFloat(this.backgroundCheckBoxView[2], View.ALPHA, 0.0f));
                arrayList2.add(ObjectAnimator.ofFloat(this.backgroundImage, View.ALPHA, 1.0f));
            }
            this.patternViewAnimation.playTogether(arrayList2);
            final int i9 = i7;
            this.patternViewAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.36
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ThemePreviewActivity.this.patternViewAnimation = null;
                    if (z && ThemePreviewActivity.this.patternLayout[i9].getVisibility() == 0) {
                        ThemePreviewActivity.this.patternLayout[i9].setAlpha(1.0f);
                        ThemePreviewActivity.this.patternLayout[i9].setVisibility(4);
                    } else if (!z) {
                        ThemePreviewActivity.this.patternLayout[i].setVisibility(4);
                    }
                    if (ThemePreviewActivity.this.screenType == 1 || ThemePreviewActivity.this.screenType == 2) {
                        ThemePreviewActivity.this.backgroundCheckBoxView[z3 ? (char) 0 : (char) 2].setVisibility(4);
                    } else if (i == 1) {
                        ThemePreviewActivity.this.patternLayout[i9].setAlpha(0.0f);
                    }
                }
            });
            this.patternViewAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            this.patternViewAnimation.setDuration(200L);
            this.patternViewAnimation.start();
            return;
        }
        char c = i == 0 ? (char) 1 : (char) 0;
        if (z) {
            this.patternLayout[i].setVisibility(0);
            int i10 = this.screenType;
            if (i10 == 1) {
                this.listView2.setTranslationY(i == 1 ? -AndroidUtilities.dp(21.0f) : 0.0f);
                this.backgroundCheckBoxView[2].setAlpha(z3 ? 1.0f : 0.0f);
                this.backgroundCheckBoxView[0].setAlpha(z3 ? 0.0f : 1.0f);
                if (i == 1) {
                    this.patternLayout[i].setAlpha(1.0f);
                } else {
                    this.patternLayout[i].setAlpha(1.0f);
                    this.patternLayout[c].setAlpha(0.0f);
                }
                this.colorPicker.hideKeyboard();
            } else if (i10 == 2) {
                this.listView2.setTranslationY((-AndroidUtilities.dp(i == 0 ? 343.0f : 316.0f)) + AndroidUtilities.dp((this.applyButton2 == null ? 0 : 58) + 72) + (insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0));
                this.backgroundCheckBoxView[2].setAlpha(z3 ? 1.0f : 0.0f);
                this.backgroundCheckBoxView[0].setAlpha(z3 ? 0.0f : 1.0f);
                if (this.patternLayout[c].getVisibility() == 0) {
                    this.patternLayout[c].setAlpha(0.0f);
                    this.patternLayout[i].setAlpha(1.0f);
                    this.patternLayout[i].setTranslationY(0.0f);
                } else {
                    this.patternLayout[i].setTranslationY(0.0f);
                }
            } else {
                if (i == 1) {
                    this.patternLayout[i].setAlpha(1.0f);
                } else {
                    this.patternLayout[i].setAlpha(1.0f);
                    this.patternLayout[c].setAlpha(0.0f);
                }
                this.colorPicker.hideKeyboard();
            }
        } else {
            this.listView2.setTranslationY(0.0f);
            FrameLayout[] frameLayoutArr3 = this.patternLayout;
            frameLayoutArr3[i].setTranslationY(frameLayoutArr3[i].getMeasuredHeight());
            this.backgroundCheckBoxView[0].setAlpha(1.0f);
            this.backgroundCheckBoxView[2].setAlpha(1.0f);
            this.backgroundImage.setAlpha(1.0f);
        }
        if (z && this.patternLayout[c].getVisibility() == 0) {
            this.patternLayout[c].setAlpha(1.0f);
            this.patternLayout[c].setVisibility(4);
        } else if (!z) {
            this.patternLayout[i].setVisibility(4);
        }
        int i11 = this.screenType;
        if (i11 == 1 || i11 == 2) {
            this.backgroundCheckBoxView[z3 ? (char) 0 : (char) 2].setVisibility(4);
        } else if (i == 1) {
            this.patternLayout[c].setAlpha(0.0f);
        }
    }

    private void animateMotionChange() {
        AnimatorSet animatorSet = this.motionAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.motionAnimation = animatorSet2;
        if (this.isMotion) {
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this.backgroundImage, View.SCALE_X, this.parallaxScale), ObjectAnimator.ofFloat(this.backgroundImage, View.SCALE_Y, this.parallaxScale));
        } else {
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this.backgroundImage, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.backgroundImage, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.backgroundImage, View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.backgroundImage, View.TRANSLATION_Y, 0.0f));
        }
        this.motionAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        this.motionAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.37
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ThemePreviewActivity.this.motionAnimation = null;
            }
        });
        this.motionAnimation.start();
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0076, code lost:
        if (r19.backgroundGradientColor1 != 0) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0078, code lost:
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0092, code lost:
        if (r1 != 0) goto L31;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updatePlayAnimationView(boolean z) {
        boolean z2;
        int i;
        if (Build.VERSION.SDK_INT >= 29) {
            int i2 = this.screenType;
            if (i2 == 0) {
                Theme.ThemeAccent themeAccent = this.accent;
                if (themeAccent != null) {
                    i = (int) themeAccent.backgroundGradientOverrideColor2;
                } else {
                    i = getThemedColor(Theme.key_chat_wallpaper_gradient_to2);
                }
            } else if (i2 == 1) {
                i = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to2);
                long j = this.accent.backgroundGradientOverrideColor2;
                int i3 = (int) j;
                if (i3 != 0 || j == 0) {
                    if (i3 != 0) {
                        i = i3;
                    }
                }
                i = 0;
            } else {
                Object obj = this.currentWallpaper;
                if (obj instanceof WallpapersListActivity.ColorWallpaper) {
                    WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper) obj;
                    i = this.backgroundGradientColor2;
                }
                i = 0;
            }
            if (i != 0 && this.currentIntensity >= 0.0f) {
                this.backgroundImage.getImageReceiver().setBlendMode(BlendMode.SOFT_LIGHT);
            } else {
                this.backgroundImage.getImageReceiver().setBlendMode(null);
            }
        }
        if (this.backgroundPlayAnimationView != null) {
            int i4 = this.screenType;
            if (i4 != 2) {
                if (i4 == 1) {
                    int defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to1);
                    long j2 = this.accent.backgroundGradientOverrideColor1;
                    int i5 = (int) j2;
                    if (i5 == 0 && j2 != 0) {
                        defaultAccentColor = 0;
                    } else if (i5 != 0) {
                        defaultAccentColor = i5;
                    }
                }
                z2 = false;
            }
            boolean z3 = this.backgroundPlayAnimationView.getTag() != null;
            this.backgroundPlayAnimationView.setTag(z2 ? 1 : null);
            if (z3 != z2) {
                if (z2) {
                    this.backgroundPlayAnimationView.setVisibility(0);
                }
                AnimatorSet animatorSet = this.backgroundPlayViewAnimator;
                if (animatorSet != null) {
                    animatorSet.cancel();
                }
                if (z) {
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    this.backgroundPlayViewAnimator = animatorSet2;
                    Animator[] animatorArr = new Animator[6];
                    FrameLayout frameLayout = this.backgroundPlayAnimationView;
                    Property property = View.ALPHA;
                    float[] fArr = new float[1];
                    fArr[0] = z2 ? 1.0f : 0.0f;
                    animatorArr[0] = ObjectAnimator.ofFloat(frameLayout, property, fArr);
                    FrameLayout frameLayout2 = this.backgroundPlayAnimationView;
                    Property property2 = View.SCALE_X;
                    float[] fArr2 = new float[1];
                    fArr2[0] = z2 ? 1.0f : 0.0f;
                    animatorArr[1] = ObjectAnimator.ofFloat(frameLayout2, property2, fArr2);
                    FrameLayout frameLayout3 = this.backgroundPlayAnimationView;
                    Property property3 = View.SCALE_Y;
                    float[] fArr3 = new float[1];
                    fArr3[0] = z2 ? 1.0f : 0.0f;
                    animatorArr[2] = ObjectAnimator.ofFloat(frameLayout3, property3, fArr3);
                    WallpaperCheckBoxView wallpaperCheckBoxView = this.backgroundCheckBoxView[0];
                    Property property4 = View.TRANSLATION_X;
                    float[] fArr4 = new float[1];
                    fArr4[0] = z2 ? AndroidUtilities.dp(34.0f) : 0.0f;
                    animatorArr[3] = ObjectAnimator.ofFloat(wallpaperCheckBoxView, property4, fArr4);
                    WallpaperCheckBoxView wallpaperCheckBoxView2 = this.backgroundCheckBoxView[1];
                    Property property5 = View.TRANSLATION_X;
                    float[] fArr5 = new float[1];
                    fArr5[0] = z2 ? -AndroidUtilities.dp(34.0f) : 0.0f;
                    animatorArr[4] = ObjectAnimator.ofFloat(wallpaperCheckBoxView2, property5, fArr5);
                    WallpaperCheckBoxView wallpaperCheckBoxView3 = this.backgroundCheckBoxView[2];
                    Property property6 = View.TRANSLATION_X;
                    float[] fArr6 = new float[1];
                    fArr6[0] = z2 ? AndroidUtilities.dp(34.0f) : 0.0f;
                    animatorArr[5] = ObjectAnimator.ofFloat(wallpaperCheckBoxView3, property6, fArr6);
                    animatorSet2.playTogether(animatorArr);
                    this.backgroundPlayViewAnimator.setDuration(180L);
                    this.backgroundPlayViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.38
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (ThemePreviewActivity.this.backgroundPlayAnimationView.getTag() == null) {
                                ThemePreviewActivity.this.backgroundPlayAnimationView.setVisibility(4);
                            }
                            ThemePreviewActivity.this.backgroundPlayViewAnimator = null;
                        }
                    });
                    this.backgroundPlayViewAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                    this.backgroundPlayViewAnimator.start();
                } else {
                    this.backgroundPlayAnimationView.setAlpha(z2 ? 1.0f : 0.0f);
                    this.backgroundPlayAnimationView.setScaleX(z2 ? 1.0f : 0.0f);
                    this.backgroundPlayAnimationView.setScaleY(z2 ? 1.0f : 0.0f);
                    this.backgroundCheckBoxView[0].setTranslationX(z2 ? AndroidUtilities.dp(34.0f) : 0.0f);
                    this.backgroundCheckBoxView[1].setTranslationX(z2 ? -AndroidUtilities.dp(34.0f) : 0.0f);
                    this.backgroundCheckBoxView[2].setTranslationX(z2 ? AndroidUtilities.dp(34.0f) : 0.0f);
                }
            }
        }
        FrameLayout frameLayout4 = this.messagesPlayAnimationView;
        if (frameLayout4 != null) {
            boolean z4 = frameLayout4.getTag() != null;
            this.messagesPlayAnimationView.setTag(1);
            if (!z4) {
                this.messagesPlayAnimationView.setVisibility(0);
                AnimatorSet animatorSet3 = this.messagesPlayViewAnimator;
                if (animatorSet3 != null) {
                    animatorSet3.cancel();
                }
                if (z) {
                    AnimatorSet animatorSet4 = new AnimatorSet();
                    this.messagesPlayViewAnimator = animatorSet4;
                    animatorSet4.playTogether(ObjectAnimator.ofFloat(this.messagesPlayAnimationView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.messagesPlayAnimationView, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.messagesPlayAnimationView, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.messagesCheckBoxView[0], View.TRANSLATION_X, -AndroidUtilities.dp(34.0f)), ObjectAnimator.ofFloat(this.messagesCheckBoxView[1], View.TRANSLATION_X, AndroidUtilities.dp(34.0f)));
                    this.messagesPlayViewAnimator.setDuration(180L);
                    this.messagesPlayViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.39
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (ThemePreviewActivity.this.messagesPlayAnimationView.getTag() == null) {
                                ThemePreviewActivity.this.messagesPlayAnimationView.setVisibility(4);
                            }
                            ThemePreviewActivity.this.messagesPlayViewAnimator = null;
                        }
                    });
                    this.messagesPlayViewAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                    this.messagesPlayViewAnimator.start();
                    return;
                }
                this.messagesPlayAnimationView.setAlpha(1.0f);
                this.messagesPlayAnimationView.setScaleX(1.0f);
                this.messagesPlayAnimationView.setScaleY(1.0f);
                this.messagesCheckBoxView[0].setTranslationX(-AndroidUtilities.dp(34.0f));
                this.messagesCheckBoxView[1].setTranslationX(AndroidUtilities.dp(34.0f));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBackgroundColor(int i, int i2, boolean z, boolean z2) {
        MotionBackgroundDrawable motionBackgroundDrawable;
        if (i2 == 0) {
            this.backgroundColor = i;
        } else if (i2 == 1) {
            this.backgroundGradientColor1 = i;
        } else if (i2 == 2) {
            this.backgroundGradientColor2 = i;
        } else if (i2 == 3) {
            this.backgroundGradientColor3 = i;
        }
        updatePlayAnimationView(z2);
        if (this.backgroundCheckBoxView != null) {
            int i3 = 0;
            while (true) {
                WallpaperCheckBoxView[] wallpaperCheckBoxViewArr = this.backgroundCheckBoxView;
                if (i3 >= wallpaperCheckBoxViewArr.length) {
                    break;
                }
                if (wallpaperCheckBoxViewArr[i3] != null) {
                    wallpaperCheckBoxViewArr[i3].setColor(i2, i);
                }
                i3++;
            }
        }
        if (this.backgroundGradientColor2 != 0) {
            if (this.intensitySeekBar != null && Theme.getActiveTheme().isDark()) {
                this.intensitySeekBar.setTwoSided(true);
            }
            Drawable background = this.backgroundImage.getBackground();
            if (background instanceof MotionBackgroundDrawable) {
                motionBackgroundDrawable = (MotionBackgroundDrawable) background;
            } else {
                motionBackgroundDrawable = new MotionBackgroundDrawable();
                motionBackgroundDrawable.setParentView(this.backgroundImage);
                if (this.rotatePreview) {
                    motionBackgroundDrawable.rotatePreview(false);
                }
            }
            motionBackgroundDrawable.setColors(this.backgroundColor, this.backgroundGradientColor1, this.backgroundGradientColor2, this.backgroundGradientColor3);
            this.backgroundImage.setBackground(motionBackgroundDrawable);
            this.patternColor = motionBackgroundDrawable.getPatternColor();
            this.checkColor = 754974720;
        } else if (this.backgroundGradientColor1 != 0) {
            this.backgroundImage.setBackground(new GradientDrawable(BackgroundGradientDrawable.getGradientOrientation(this.backgroundRotation), new int[]{this.backgroundColor, this.backgroundGradientColor1}));
            int patternColor = AndroidUtilities.getPatternColor(AndroidUtilities.getAverageColor(this.backgroundColor, this.backgroundGradientColor1));
            this.checkColor = patternColor;
            this.patternColor = patternColor;
        } else {
            this.backgroundImage.setBackgroundColor(this.backgroundColor);
            int patternColor2 = AndroidUtilities.getPatternColor(this.backgroundColor);
            this.checkColor = patternColor2;
            this.patternColor = patternColor2;
        }
        int i4 = Theme.key_chat_serviceBackground;
        if (!Theme.hasThemeKey(i4) || (this.backgroundImage.getBackground() instanceof MotionBackgroundDrawable)) {
            ThemeDelegate themeDelegate = this.themeDelegate;
            int i5 = this.checkColor;
            themeDelegate.applyChatServiceMessageColor(new int[]{i5, i5, i5, i5}, this.backgroundImage.getBackground(), this.backgroundImage.getBackground(), Float.valueOf(this.currentIntensity));
        } else if (Theme.getCachedWallpaperNonBlocking() instanceof MotionBackgroundDrawable) {
            int themedColor = getThemedColor(i4);
            this.themeDelegate.applyChatServiceMessageColor(new int[]{themedColor, themedColor, themedColor, themedColor}, this.backgroundImage.getBackground(), this.backgroundImage.getBackground(), Float.valueOf(this.currentIntensity));
        }
        ImageView imageView = this.backgroundPlayAnimationImageView;
        if (imageView != null) {
            imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_serviceText), PorterDuff.Mode.MULTIPLY));
        }
        ImageView imageView2 = this.messagesPlayAnimationImageView;
        if (imageView2 != null) {
            imageView2.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_serviceText), PorterDuff.Mode.MULTIPLY));
        }
        BackgroundView backgroundView = this.backgroundImage;
        if (backgroundView != null) {
            backgroundView.getImageReceiver().setColorFilter(new PorterDuffColorFilter(this.patternColor, this.blendMode));
            this.backgroundImage.getImageReceiver().setAlpha(Math.abs(this.currentIntensity));
            this.backgroundImage.invalidate();
            if (Theme.getActiveTheme().isDark() && (this.backgroundImage.getBackground() instanceof MotionBackgroundDrawable)) {
                SeekBarView seekBarView = this.intensitySeekBar;
                if (seekBarView != null) {
                    seekBarView.setTwoSided(true);
                }
                if (this.currentIntensity < 0.0f) {
                    this.backgroundImage.getImageReceiver().setGradientBitmap(((MotionBackgroundDrawable) this.backgroundImage.getBackground()).getBitmap());
                }
            } else {
                this.backgroundImage.getImageReceiver().setGradientBitmap(null);
                SeekBarView seekBarView2 = this.intensitySeekBar;
                if (seekBarView2 != null) {
                    seekBarView2.setTwoSided(false);
                }
            }
            SeekBarView seekBarView3 = this.intensitySeekBar;
            if (seekBarView3 != null) {
                seekBarView3.setProgress(this.currentIntensity);
            }
        }
        RecyclerListView recyclerListView = this.listView2;
        if (recyclerListView != null) {
            recyclerListView.invalidateViews();
        }
        FrameLayout frameLayout = this.backgroundButtonsContainer;
        if (frameLayout != null) {
            int childCount = frameLayout.getChildCount();
            for (int i6 = 0; i6 < childCount; i6++) {
                this.backgroundButtonsContainer.getChildAt(i6).invalidate();
            }
        }
        FrameLayout frameLayout2 = this.messagesButtonsContainer;
        if (frameLayout2 != null) {
            int childCount2 = frameLayout2.getChildCount();
            for (int i7 = 0; i7 < childCount2; i7++) {
                this.messagesButtonsContainer.getChildAt(i7).invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void setCurrentImage(boolean z) {
        MotionBackgroundDrawable motionBackgroundDrawable;
        MotionBackgroundDrawable motionBackgroundDrawable2;
        int i = this.screenType;
        if (i == 0 && this.accent == null) {
            this.backgroundImage.setBackground(Theme.getCachedWallpaper());
        } else {
            if (i == 2) {
                Object obj = this.currentWallpaper;
                if (obj instanceof TLRPC$TL_wallPaper) {
                    TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) obj;
                    TLRPC$PhotoSize closestPhotoSizeWithSize = z ? FileLoader.getClosestPhotoSizeWithSize(tLRPC$TL_wallPaper.document.thumbs, 100) : null;
                    this.backgroundImage.setImage(ImageLocation.getForDocument(tLRPC$TL_wallPaper.document), this.imageFilter, ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$TL_wallPaper.document), "100_100_b", closestPhotoSizeWithSize instanceof TLRPC$TL_photoStrippedSize ? new BitmapDrawable(ImageLoader.getStrippedPhotoBitmap(closestPhotoSizeWithSize.bytes, "b")) : null, "jpg", tLRPC$TL_wallPaper.document.size, 1, tLRPC$TL_wallPaper);
                } else if (obj instanceof WallpapersListActivity.ColorWallpaper) {
                    WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper) obj;
                    this.backgroundRotation = colorWallpaper.gradientRotation;
                    setBackgroundColor(colorWallpaper.color, 0, true, false);
                    int i2 = colorWallpaper.gradientColor1;
                    if (i2 != 0) {
                        setBackgroundColor(i2, 1, true, false);
                    }
                    setBackgroundColor(colorWallpaper.gradientColor2, 2, true, false);
                    setBackgroundColor(colorWallpaper.gradientColor3, 3, true, false);
                    TLRPC$TL_wallPaper tLRPC$TL_wallPaper2 = this.selectedPattern;
                    if (tLRPC$TL_wallPaper2 != null) {
                        BackgroundView backgroundView = this.backgroundImage;
                        ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$TL_wallPaper2.document);
                        String str = this.imageFilter;
                        TLRPC$TL_wallPaper tLRPC$TL_wallPaper3 = this.selectedPattern;
                        backgroundView.setImage(forDocument, str, null, null, "jpg", tLRPC$TL_wallPaper3.document.size, 1, tLRPC$TL_wallPaper3);
                    } else if ("d".equals(colorWallpaper.slug)) {
                        Point point = AndroidUtilities.displaySize;
                        int min = Math.min(point.x, point.y);
                        Point point2 = AndroidUtilities.displaySize;
                        this.backgroundImage.setImageBitmap(SvgHelper.getBitmap(R.raw.default_pattern, min, Math.max(point2.x, point2.y), Build.VERSION.SDK_INT >= 29 ? 1459617792 : MotionBackgroundDrawable.getPatternColor(colorWallpaper.color, colorWallpaper.gradientColor1, colorWallpaper.gradientColor2, colorWallpaper.gradientColor3)));
                    }
                } else if (obj instanceof WallpapersListActivity.FileWallpaper) {
                    Bitmap bitmap = this.currentWallpaperBitmap;
                    if (bitmap != null) {
                        this.backgroundImage.setImageBitmap(bitmap);
                    } else {
                        WallpapersListActivity.FileWallpaper fileWallpaper = (WallpapersListActivity.FileWallpaper) obj;
                        File file = fileWallpaper.originalPath;
                        if (file != null) {
                            this.backgroundImage.setImage(file.getAbsolutePath(), this.imageFilter, null);
                        } else {
                            File file2 = fileWallpaper.path;
                            if (file2 != null) {
                                this.backgroundImage.setImage(file2.getAbsolutePath(), this.imageFilter, null);
                            } else if ("t".equals(fileWallpaper.slug)) {
                                BackgroundView backgroundView2 = this.backgroundImage;
                                backgroundView2.setImageDrawable(Theme.getThemedWallpaper(false, backgroundView2));
                            } else {
                                int i3 = fileWallpaper.resId;
                                if (i3 != 0) {
                                    this.backgroundImage.setImageResource(i3);
                                }
                            }
                        }
                    }
                } else if (obj instanceof MediaController.SearchImage) {
                    MediaController.SearchImage searchImage = (MediaController.SearchImage) obj;
                    TLRPC$Photo tLRPC$Photo = searchImage.photo;
                    if (tLRPC$Photo != null) {
                        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, 100);
                        TLRPC$PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(searchImage.photo.sizes, this.maxWallpaperSize, true);
                        TLRPC$PhotoSize tLRPC$PhotoSize = closestPhotoSizeWithSize3 != closestPhotoSizeWithSize2 ? closestPhotoSizeWithSize3 : null;
                        this.backgroundImage.setImage(ImageLocation.getForPhoto(tLRPC$PhotoSize, searchImage.photo), this.imageFilter, ImageLocation.getForPhoto(closestPhotoSizeWithSize2, searchImage.photo), "100_100_b", "jpg", tLRPC$PhotoSize != null ? tLRPC$PhotoSize.size : 0, 1, searchImage);
                    } else {
                        this.backgroundImage.setImage(searchImage.imageUrl, this.imageFilter, searchImage.thumbUrl, "100_100_b");
                    }
                } else if (obj instanceof WallpapersListActivity.EmojiWallpaper) {
                    DayNightSwitchDelegate dayNightSwitchDelegate = this.onSwitchDayNightDelegate;
                    Drawable backgroundDrawableFromTheme = PreviewView.getBackgroundDrawableFromTheme(this.currentAccount, ((WallpapersListActivity.EmojiWallpaper) this.currentWallpaper).emoticon, dayNightSwitchDelegate != null ? dayNightSwitchDelegate.isDark() : Theme.isCurrentThemeDark());
                    this.backgroundImage.setBackground(backgroundDrawableFromTheme);
                    this.themeDelegate.applyChatServiceMessageColor(AndroidUtilities.calcDrawableColor(backgroundDrawableFromTheme), checkBlur(backgroundDrawableFromTheme), backgroundDrawableFromTheme, Float.valueOf(this.currentIntensity));
                }
            } else {
                BackgroundGradientDrawable.Disposable disposable = this.backgroundGradientDisposable;
                if (disposable != null) {
                    disposable.dispose();
                    this.backgroundGradientDisposable = null;
                }
                int defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper);
                int i4 = (int) this.accent.backgroundOverrideColor;
                if (i4 != 0) {
                    defaultAccentColor = i4;
                }
                int defaultAccentColor2 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to1);
                long j = this.accent.backgroundGradientOverrideColor1;
                int i5 = (int) j;
                if (i5 == 0 && j != 0) {
                    defaultAccentColor2 = 0;
                } else if (i5 != 0) {
                    defaultAccentColor2 = i5;
                }
                int defaultAccentColor3 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to2);
                long j2 = this.accent.backgroundGradientOverrideColor2;
                int i6 = (int) j2;
                if (i6 == 0 && j2 != 0) {
                    defaultAccentColor3 = 0;
                } else if (i6 != 0) {
                    defaultAccentColor3 = i6;
                }
                int defaultAccentColor4 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to3);
                Theme.ThemeAccent themeAccent = this.accent;
                long j3 = themeAccent.backgroundGradientOverrideColor3;
                int i7 = (int) j3;
                if (i7 == 0 && j3 != 0) {
                    defaultAccentColor4 = 0;
                } else if (i7 != 0) {
                    defaultAccentColor4 = i7;
                }
                if (!TextUtils.isEmpty(themeAccent.patternSlug) && !Theme.hasCustomWallpaper()) {
                    if (defaultAccentColor3 != 0) {
                        Drawable background = this.backgroundImage.getBackground();
                        if (background instanceof MotionBackgroundDrawable) {
                            motionBackgroundDrawable2 = (MotionBackgroundDrawable) background;
                        } else {
                            MotionBackgroundDrawable motionBackgroundDrawable3 = new MotionBackgroundDrawable();
                            motionBackgroundDrawable3.setParentView(this.backgroundImage);
                            motionBackgroundDrawable2 = motionBackgroundDrawable3;
                            if (this.rotatePreview) {
                                motionBackgroundDrawable3.rotatePreview(false);
                                motionBackgroundDrawable2 = motionBackgroundDrawable3;
                            }
                        }
                        motionBackgroundDrawable2.setColors(defaultAccentColor, defaultAccentColor2, defaultAccentColor3, defaultAccentColor4);
                        motionBackgroundDrawable = motionBackgroundDrawable2;
                    } else if (defaultAccentColor2 != 0) {
                        BackgroundGradientDrawable backgroundGradientDrawable = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(this.accent.backgroundRotation), new int[]{defaultAccentColor, defaultAccentColor2});
                        this.backgroundGradientDisposable = backgroundGradientDrawable.startDithering(BackgroundGradientDrawable.Sizes.ofDeviceScreen(), new BackgroundGradientDrawable.ListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.40
                            @Override // org.telegram.ui.Components.BackgroundGradientDrawable.ListenerAdapter, org.telegram.ui.Components.BackgroundGradientDrawable.Listener
                            public void onSizeReady(int i8, int i9) {
                                Point point3 = AndroidUtilities.displaySize;
                                if ((point3.x <= point3.y) == (i8 <= i9)) {
                                    ThemePreviewActivity.this.backgroundImage.invalidate();
                                }
                            }
                        }, 100L);
                        motionBackgroundDrawable = backgroundGradientDrawable;
                    } else {
                        motionBackgroundDrawable = new ColorDrawable(defaultAccentColor);
                    }
                    this.backgroundImage.setBackground(motionBackgroundDrawable);
                    TLRPC$TL_wallPaper tLRPC$TL_wallPaper4 = this.selectedPattern;
                    if (tLRPC$TL_wallPaper4 != null) {
                        BackgroundView backgroundView3 = this.backgroundImage;
                        ImageLocation forDocument2 = ImageLocation.getForDocument(tLRPC$TL_wallPaper4.document);
                        String str2 = this.imageFilter;
                        TLRPC$TL_wallPaper tLRPC$TL_wallPaper5 = this.selectedPattern;
                        backgroundView3.setImage(forDocument2, str2, null, null, "jpg", tLRPC$TL_wallPaper5.document.size, 1, tLRPC$TL_wallPaper5);
                    }
                } else {
                    Drawable cachedWallpaperNonBlocking = Theme.getCachedWallpaperNonBlocking();
                    if (cachedWallpaperNonBlocking != null) {
                        if (cachedWallpaperNonBlocking instanceof MotionBackgroundDrawable) {
                            ((MotionBackgroundDrawable) cachedWallpaperNonBlocking).setParentView(this.backgroundImage);
                        }
                        this.backgroundImage.setBackground(cachedWallpaperNonBlocking);
                    }
                }
                if (defaultAccentColor2 == 0) {
                    int patternColor = AndroidUtilities.getPatternColor(defaultAccentColor);
                    this.checkColor = patternColor;
                    this.patternColor = patternColor;
                } else if (defaultAccentColor3 != 0) {
                    this.patternColor = MotionBackgroundDrawable.getPatternColor(defaultAccentColor, defaultAccentColor2, defaultAccentColor3, defaultAccentColor4);
                    this.checkColor = 754974720;
                } else {
                    int patternColor2 = AndroidUtilities.getPatternColor(AndroidUtilities.getAverageColor(defaultAccentColor, defaultAccentColor2));
                    this.checkColor = patternColor2;
                    this.patternColor = patternColor2;
                }
                BackgroundView backgroundView4 = this.backgroundImage;
                if (backgroundView4 != null) {
                    backgroundView4.getImageReceiver().setColorFilter(new PorterDuffColorFilter(this.patternColor, this.blendMode));
                    this.backgroundImage.getImageReceiver().setAlpha(Math.abs(this.currentIntensity));
                    this.backgroundImage.invalidate();
                    if (Theme.getActiveTheme().isDark() && (this.backgroundImage.getBackground() instanceof MotionBackgroundDrawable)) {
                        SeekBarView seekBarView = this.intensitySeekBar;
                        if (seekBarView != null) {
                            seekBarView.setTwoSided(true);
                        }
                        if (this.currentIntensity < 0.0f) {
                            this.backgroundImage.getImageReceiver().setGradientBitmap(((MotionBackgroundDrawable) this.backgroundImage.getBackground()).getBitmap());
                        }
                    } else {
                        this.backgroundImage.getImageReceiver().setGradientBitmap(null);
                        SeekBarView seekBarView2 = this.intensitySeekBar;
                        if (seekBarView2 != null) {
                            seekBarView2.setTwoSided(false);
                        }
                    }
                    SeekBarView seekBarView3 = this.intensitySeekBar;
                    if (seekBarView3 != null) {
                        seekBarView3.setProgress(this.currentIntensity);
                    }
                }
                if (this.backgroundCheckBoxView != null) {
                    int i8 = 0;
                    while (true) {
                        WallpaperCheckBoxView[] wallpaperCheckBoxViewArr = this.backgroundCheckBoxView;
                        if (i8 >= wallpaperCheckBoxViewArr.length) {
                            break;
                        }
                        wallpaperCheckBoxViewArr[i8].setColor(0, defaultAccentColor);
                        this.backgroundCheckBoxView[i8].setColor(1, defaultAccentColor2);
                        this.backgroundCheckBoxView[i8].setColor(2, defaultAccentColor3);
                        this.backgroundCheckBoxView[i8].setColor(3, defaultAccentColor4);
                        i8++;
                    }
                }
                ImageView imageView = this.backgroundPlayAnimationImageView;
                if (imageView != null) {
                    imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_serviceText), PorterDuff.Mode.MULTIPLY));
                }
                ImageView imageView2 = this.messagesPlayAnimationImageView;
                if (imageView2 != null) {
                    imageView2.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_serviceText), PorterDuff.Mode.MULTIPLY));
                }
                invalidateBlur();
            }
        }
        this.rotatePreview = false;
    }

    /* loaded from: classes3.dex */
    public static class DialogsAdapter extends RecyclerListView.SelectionAdapter {
        private ArrayList<DialogCell.CustomDialog> dialogs = new ArrayList<>();
        private Context mContext;

        public DialogsAdapter(Context context) {
            this.mContext = context;
            int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
            DialogCell.CustomDialog customDialog = new DialogCell.CustomDialog();
            customDialog.name = LocaleController.getString("ThemePreviewDialog1", R.string.ThemePreviewDialog1);
            customDialog.message = LocaleController.getString("ThemePreviewDialogMessage1", R.string.ThemePreviewDialogMessage1);
            customDialog.id = 0;
            customDialog.unread_count = 0;
            customDialog.pinned = true;
            customDialog.muted = false;
            customDialog.type = 0;
            customDialog.date = currentTimeMillis;
            customDialog.verified = false;
            customDialog.isMedia = false;
            customDialog.sent = 2;
            this.dialogs.add(customDialog);
            DialogCell.CustomDialog customDialog2 = new DialogCell.CustomDialog();
            customDialog2.name = LocaleController.getString("ThemePreviewDialog2", R.string.ThemePreviewDialog2);
            customDialog2.message = LocaleController.getString("ThemePreviewDialogMessage2", R.string.ThemePreviewDialogMessage2);
            customDialog2.id = 1;
            customDialog2.unread_count = 2;
            customDialog2.pinned = false;
            customDialog2.muted = false;
            customDialog2.type = 0;
            customDialog2.date = currentTimeMillis - 3600;
            customDialog2.verified = false;
            customDialog2.isMedia = false;
            customDialog2.sent = -1;
            this.dialogs.add(customDialog2);
            DialogCell.CustomDialog customDialog3 = new DialogCell.CustomDialog();
            customDialog3.name = LocaleController.getString("ThemePreviewDialog3", R.string.ThemePreviewDialog3);
            customDialog3.message = LocaleController.getString("ThemePreviewDialogMessage3", R.string.ThemePreviewDialogMessage3);
            customDialog3.id = 2;
            customDialog3.unread_count = 3;
            customDialog3.pinned = false;
            customDialog3.muted = true;
            customDialog3.type = 0;
            customDialog3.date = currentTimeMillis - 7200;
            customDialog3.verified = false;
            customDialog3.isMedia = true;
            customDialog3.sent = -1;
            this.dialogs.add(customDialog3);
            DialogCell.CustomDialog customDialog4 = new DialogCell.CustomDialog();
            customDialog4.name = LocaleController.getString("ThemePreviewDialog4", R.string.ThemePreviewDialog4);
            customDialog4.message = LocaleController.getString("ThemePreviewDialogMessage4", R.string.ThemePreviewDialogMessage4);
            customDialog4.id = 3;
            customDialog4.unread_count = 0;
            customDialog4.pinned = false;
            customDialog4.muted = false;
            customDialog4.type = 2;
            customDialog4.date = currentTimeMillis - 10800;
            customDialog4.verified = false;
            customDialog4.isMedia = false;
            customDialog4.sent = -1;
            this.dialogs.add(customDialog4);
            DialogCell.CustomDialog customDialog5 = new DialogCell.CustomDialog();
            customDialog5.name = LocaleController.getString("ThemePreviewDialog5", R.string.ThemePreviewDialog5);
            customDialog5.message = LocaleController.getString("ThemePreviewDialogMessage5", R.string.ThemePreviewDialogMessage5);
            customDialog5.id = 4;
            customDialog5.unread_count = 0;
            customDialog5.pinned = false;
            customDialog5.muted = false;
            customDialog5.type = 1;
            customDialog5.date = currentTimeMillis - 14400;
            customDialog5.verified = false;
            customDialog5.isMedia = false;
            customDialog5.sent = 2;
            this.dialogs.add(customDialog5);
            DialogCell.CustomDialog customDialog6 = new DialogCell.CustomDialog();
            customDialog6.name = LocaleController.getString("ThemePreviewDialog6", R.string.ThemePreviewDialog6);
            customDialog6.message = LocaleController.getString("ThemePreviewDialogMessage6", R.string.ThemePreviewDialogMessage6);
            customDialog6.id = 5;
            customDialog6.unread_count = 0;
            customDialog6.pinned = false;
            customDialog6.muted = false;
            customDialog6.type = 0;
            customDialog6.date = currentTimeMillis - 18000;
            customDialog6.verified = false;
            customDialog6.isMedia = false;
            customDialog6.sent = -1;
            this.dialogs.add(customDialog6);
            DialogCell.CustomDialog customDialog7 = new DialogCell.CustomDialog();
            customDialog7.name = LocaleController.getString("ThemePreviewDialog7", R.string.ThemePreviewDialog7);
            customDialog7.message = LocaleController.getString("ThemePreviewDialogMessage7", R.string.ThemePreviewDialogMessage7);
            customDialog7.id = 6;
            customDialog7.unread_count = 0;
            customDialog7.pinned = false;
            customDialog7.muted = false;
            customDialog7.type = 0;
            customDialog7.date = currentTimeMillis - 21600;
            customDialog7.verified = true;
            customDialog7.isMedia = false;
            customDialog7.sent = -1;
            this.dialogs.add(customDialog7);
            DialogCell.CustomDialog customDialog8 = new DialogCell.CustomDialog();
            customDialog8.name = LocaleController.getString("ThemePreviewDialog8", R.string.ThemePreviewDialog8);
            customDialog8.message = LocaleController.getString("ThemePreviewDialogMessage8", R.string.ThemePreviewDialogMessage8);
            customDialog8.id = 0;
            customDialog8.unread_count = 0;
            customDialog8.pinned = false;
            customDialog8.muted = false;
            customDialog8.type = 0;
            customDialog8.date = currentTimeMillis - 25200;
            customDialog8.verified = true;
            customDialog8.isMedia = false;
            customDialog8.sent = -1;
            this.dialogs.add(customDialog8);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.dialogs.size();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View loadingCell;
            if (i == 0) {
                loadingCell = new DialogCell(null, this.mContext, false, false);
            } else {
                loadingCell = new LoadingCell(this.mContext);
            }
            loadingCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(loadingCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0) {
                DialogCell dialogCell = (DialogCell) viewHolder.itemView;
                dialogCell.useSeparator = i != getItemCount() - 1;
                dialogCell.setDialog(this.dialogs.get(i));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return i == this.dialogs.size() ? 1 : 0;
        }
    }

    /* loaded from: classes3.dex */
    public class MessagesAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        private ArrayList<MessageObject> messages;
        private boolean showSecretMessages;

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        public MessagesAdapter(Context context) {
            String str;
            long j;
            MessageObject messageObject;
            this.showSecretMessages = ThemePreviewActivity.this.screenType == 0 && Utilities.random.nextInt(100) <= 1;
            this.mContext = context;
            this.messages = new ArrayList<>();
            int currentTimeMillis = ((int) (System.currentTimeMillis() / 1000)) - 3600;
            if (ThemePreviewActivity.this.screenType != 2) {
                if (ThemePreviewActivity.this.screenType == 1) {
                    TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
                    TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument = new TLRPC$TL_messageMediaDocument();
                    tLRPC$TL_message.media = tLRPC$TL_messageMediaDocument;
                    tLRPC$TL_messageMediaDocument.document = new TLRPC$TL_document();
                    TLRPC$Document tLRPC$Document = tLRPC$TL_message.media.document;
                    tLRPC$Document.mime_type = "audio/mp3";
                    tLRPC$Document.file_reference = new byte[0];
                    tLRPC$Document.id = -2147483648L;
                    tLRPC$Document.size = 2621440L;
                    tLRPC$Document.dc_id = Integer.MIN_VALUE;
                    TLRPC$TL_documentAttributeFilename tLRPC$TL_documentAttributeFilename = new TLRPC$TL_documentAttributeFilename();
                    tLRPC$TL_documentAttributeFilename.file_name = LocaleController.getString("NewThemePreviewReply2", R.string.NewThemePreviewReply2) + ".mp3";
                    tLRPC$TL_message.media.document.attributes.add(tLRPC$TL_documentAttributeFilename);
                    int i = currentTimeMillis + 60;
                    tLRPC$TL_message.date = i;
                    tLRPC$TL_message.dialog_id = 1L;
                    tLRPC$TL_message.flags = 259;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                    tLRPC$TL_message.from_id = tLRPC$TL_peerUser;
                    tLRPC$TL_peerUser.user_id = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                    tLRPC$TL_message.id = 1;
                    tLRPC$TL_message.out = true;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message.peer_id = tLRPC$TL_peerUser2;
                    tLRPC$TL_peerUser2.user_id = 0L;
                    MessageObject messageObject2 = new MessageObject(UserConfig.selectedAccount, tLRPC$TL_message, true, false);
                    if (BuildVars.DEBUG_PRIVATE_VERSION) {
                        TLRPC$TL_message tLRPC$TL_message2 = new TLRPC$TL_message();
                        tLRPC$TL_message2.message = "this is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text";
                        tLRPC$TL_message2.date = currentTimeMillis + 960;
                        str = "audio/ogg";
                        tLRPC$TL_message2.dialog_id = 1L;
                        tLRPC$TL_message2.flags = 259;
                        TLRPC$TL_peerUser tLRPC$TL_peerUser3 = new TLRPC$TL_peerUser();
                        tLRPC$TL_message2.from_id = tLRPC$TL_peerUser3;
                        tLRPC$TL_peerUser3.user_id = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                        tLRPC$TL_message2.id = 1;
                        tLRPC$TL_message2.media = new TLRPC$TL_messageMediaEmpty();
                        tLRPC$TL_message2.out = true;
                        TLRPC$TL_peerUser tLRPC$TL_peerUser4 = new TLRPC$TL_peerUser();
                        tLRPC$TL_message2.peer_id = tLRPC$TL_peerUser4;
                        tLRPC$TL_peerUser4.user_id = 0L;
                        MessageObject messageObject3 = new MessageObject(UserConfig.selectedAccount, tLRPC$TL_message2, true, false);
                        messageObject3.resetLayout();
                        messageObject3.eventId = 1L;
                        this.messages.add(messageObject3);
                    } else {
                        str = "audio/ogg";
                    }
                    TLRPC$TL_message tLRPC$TL_message3 = new TLRPC$TL_message();
                    String string = LocaleController.getString("NewThemePreviewLine3", R.string.NewThemePreviewLine3);
                    StringBuilder sb = new StringBuilder(string);
                    int indexOf = string.indexOf(42);
                    int lastIndexOf = string.lastIndexOf(42);
                    if (indexOf != -1 && lastIndexOf != -1) {
                        sb.replace(lastIndexOf, lastIndexOf + 1, "");
                        sb.replace(indexOf, indexOf + 1, "");
                        TLRPC$TL_messageEntityTextUrl tLRPC$TL_messageEntityTextUrl = new TLRPC$TL_messageEntityTextUrl();
                        tLRPC$TL_messageEntityTextUrl.offset = indexOf;
                        tLRPC$TL_messageEntityTextUrl.length = (lastIndexOf - indexOf) - 1;
                        tLRPC$TL_messageEntityTextUrl.url = "https://telegram.org";
                        tLRPC$TL_message3.entities.add(tLRPC$TL_messageEntityTextUrl);
                    }
                    tLRPC$TL_message3.message = sb.toString();
                    tLRPC$TL_message3.date = currentTimeMillis + 960;
                    tLRPC$TL_message3.dialog_id = 1L;
                    tLRPC$TL_message3.flags = 259;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser5 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message3.from_id = tLRPC$TL_peerUser5;
                    tLRPC$TL_peerUser5.user_id = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                    tLRPC$TL_message3.id = 1;
                    tLRPC$TL_message3.media = new TLRPC$TL_messageMediaEmpty();
                    tLRPC$TL_message3.out = true;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser6 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message3.peer_id = tLRPC$TL_peerUser6;
                    tLRPC$TL_peerUser6.user_id = 0L;
                    MessageObject messageObject4 = new MessageObject(UserConfig.selectedAccount, tLRPC$TL_message3, true, false);
                    messageObject4.resetLayout();
                    messageObject4.eventId = 1L;
                    this.messages.add(messageObject4);
                    TLRPC$TL_message tLRPC$TL_message4 = new TLRPC$TL_message();
                    tLRPC$TL_message4.message = LocaleController.getString("NewThemePreviewLine1", R.string.NewThemePreviewLine1);
                    tLRPC$TL_message4.date = i;
                    tLRPC$TL_message4.dialog_id = 1L;
                    tLRPC$TL_message4.flags = 265;
                    tLRPC$TL_message4.from_id = new TLRPC$TL_peerUser();
                    tLRPC$TL_message4.id = 1;
                    TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                    tLRPC$TL_message4.reply_to = tLRPC$TL_messageReplyHeader;
                    tLRPC$TL_messageReplyHeader.flags |= 16;
                    tLRPC$TL_messageReplyHeader.reply_to_msg_id = 5;
                    tLRPC$TL_message4.media = new TLRPC$TL_messageMediaEmpty();
                    tLRPC$TL_message4.out = false;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser7 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message4.peer_id = tLRPC$TL_peerUser7;
                    tLRPC$TL_peerUser7.user_id = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                    MessageObject messageObject5 = new MessageObject(UserConfig.selectedAccount, tLRPC$TL_message4, true, false);
                    messageObject5.customReplyName = LocaleController.getString("NewThemePreviewName", R.string.NewThemePreviewName);
                    messageObject4.customReplyName = "Test User";
                    messageObject5.eventId = 1L;
                    messageObject5.resetLayout();
                    messageObject5.replyMessageObject = messageObject2;
                    messageObject4.replyMessageObject = messageObject5;
                    this.messages.add(messageObject5);
                    this.messages.add(messageObject2);
                    TLRPC$TL_message tLRPC$TL_message5 = new TLRPC$TL_message();
                    tLRPC$TL_message5.date = currentTimeMillis + 120;
                    tLRPC$TL_message5.dialog_id = 1L;
                    tLRPC$TL_message5.flags = 259;
                    tLRPC$TL_message5.out = false;
                    tLRPC$TL_message5.from_id = new TLRPC$TL_peerUser();
                    tLRPC$TL_message5.id = 1;
                    TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument2 = new TLRPC$TL_messageMediaDocument();
                    tLRPC$TL_message5.media = tLRPC$TL_messageMediaDocument2;
                    tLRPC$TL_messageMediaDocument2.flags |= 3;
                    tLRPC$TL_messageMediaDocument2.document = new TLRPC$TL_document();
                    TLRPC$Document tLRPC$Document2 = tLRPC$TL_message5.media.document;
                    tLRPC$Document2.mime_type = str;
                    tLRPC$Document2.file_reference = new byte[0];
                    TLRPC$TL_documentAttributeAudio tLRPC$TL_documentAttributeAudio = new TLRPC$TL_documentAttributeAudio();
                    tLRPC$TL_documentAttributeAudio.flags = 1028;
                    tLRPC$TL_documentAttributeAudio.duration = 3.0d;
                    tLRPC$TL_documentAttributeAudio.voice = true;
                    tLRPC$TL_documentAttributeAudio.waveform = new byte[]{0, 4, 17, -50, -93, 86, -103, -45, -12, -26, 63, -25, -3, 109, -114, -54, -4, -1, -1, -1, -1, -29, -1, -1, -25, -1, -1, -97, -43, 57, -57, -108, 1, -91, -4, -47, 21, 99, 10, 97, 43, 45, 115, -112, -77, 51, -63, 66, 40, 34, -122, -116, 48, -124, 16, 66, -120, 16, 68, 16, 33, 4, 1};
                    tLRPC$TL_message5.media.document.attributes.add(tLRPC$TL_documentAttributeAudio);
                    tLRPC$TL_message5.out = true;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser8 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message5.peer_id = tLRPC$TL_peerUser8;
                    tLRPC$TL_peerUser8.user_id = 0L;
                    MessageObject messageObject6 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message5, true, false);
                    messageObject6.audioProgressSec = 1;
                    messageObject6.audioProgress = 0.3f;
                    messageObject6.useCustomPhoto = true;
                    this.messages.add(messageObject6);
                    return;
                } else if (this.showSecretMessages) {
                    TLRPC$TL_user tLRPC$TL_user = new TLRPC$TL_user();
                    tLRPC$TL_user.id = 2147483647L;
                    tLRPC$TL_user.first_name = "Me";
                    TLRPC$TL_user tLRPC$TL_user2 = new TLRPC$TL_user();
                    tLRPC$TL_user2.id = 2147483646L;
                    tLRPC$TL_user2.first_name = "Serj";
                    ArrayList<TLRPC$User> arrayList = new ArrayList<>();
                    arrayList.add(tLRPC$TL_user);
                    arrayList.add(tLRPC$TL_user2);
                    MessagesController.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).putUsers(arrayList, true);
                    TLRPC$TL_message tLRPC$TL_message6 = new TLRPC$TL_message();
                    tLRPC$TL_message6.message = "Guess why Half-Life 3 was never released.";
                    int i2 = currentTimeMillis + 960;
                    tLRPC$TL_message6.date = i2;
                    tLRPC$TL_message6.dialog_id = -1L;
                    tLRPC$TL_message6.flags = 259;
                    tLRPC$TL_message6.id = 2147483646;
                    tLRPC$TL_message6.media = new TLRPC$TL_messageMediaEmpty();
                    tLRPC$TL_message6.out = false;
                    TLRPC$TL_peerChat tLRPC$TL_peerChat = new TLRPC$TL_peerChat();
                    tLRPC$TL_message6.peer_id = tLRPC$TL_peerChat;
                    tLRPC$TL_peerChat.chat_id = 1L;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser9 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message6.from_id = tLRPC$TL_peerUser9;
                    tLRPC$TL_peerUser9.user_id = tLRPC$TL_user2.id;
                    this.messages.add(new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message6, true, false));
                    TLRPC$TL_message tLRPC$TL_message7 = new TLRPC$TL_message();
                    tLRPC$TL_message7.message = "No.\nAnd every unnecessary ping of the dev delays the release for 10 days.\nEvery request for ETA delays the release for 2 weeks.";
                    tLRPC$TL_message7.date = i2;
                    tLRPC$TL_message7.dialog_id = -1L;
                    tLRPC$TL_message7.flags = 259;
                    tLRPC$TL_message7.id = 1;
                    tLRPC$TL_message7.media = new TLRPC$TL_messageMediaEmpty();
                    tLRPC$TL_message7.out = false;
                    TLRPC$TL_peerChat tLRPC$TL_peerChat2 = new TLRPC$TL_peerChat();
                    tLRPC$TL_message7.peer_id = tLRPC$TL_peerChat2;
                    tLRPC$TL_peerChat2.chat_id = 1L;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser10 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message7.from_id = tLRPC$TL_peerUser10;
                    tLRPC$TL_peerUser10.user_id = tLRPC$TL_user2.id;
                    this.messages.add(new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message7, true, false));
                    TLRPC$TL_message tLRPC$TL_message8 = new TLRPC$TL_message();
                    tLRPC$TL_message8.message = "Is source code for Android coming anytime soon?";
                    tLRPC$TL_message8.date = currentTimeMillis + 600;
                    tLRPC$TL_message8.dialog_id = -1L;
                    tLRPC$TL_message8.flags = 259;
                    tLRPC$TL_message8.id = 1;
                    tLRPC$TL_message8.media = new TLRPC$TL_messageMediaEmpty();
                    tLRPC$TL_message8.out = false;
                    TLRPC$TL_peerChat tLRPC$TL_peerChat3 = new TLRPC$TL_peerChat();
                    tLRPC$TL_message8.peer_id = tLRPC$TL_peerChat3;
                    tLRPC$TL_peerChat3.chat_id = 1L;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser11 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message8.from_id = tLRPC$TL_peerUser11;
                    tLRPC$TL_peerUser11.user_id = tLRPC$TL_user.id;
                    this.messages.add(new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message8, true, false));
                    return;
                } else {
                    TLRPC$TL_message tLRPC$TL_message9 = new TLRPC$TL_message();
                    tLRPC$TL_message9.message = LocaleController.getString("ThemePreviewLine1", R.string.ThemePreviewLine1);
                    int i3 = currentTimeMillis + 60;
                    tLRPC$TL_message9.date = i3;
                    tLRPC$TL_message9.dialog_id = 1L;
                    tLRPC$TL_message9.flags = 259;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser12 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message9.from_id = tLRPC$TL_peerUser12;
                    tLRPC$TL_peerUser12.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                    tLRPC$TL_message9.id = 1;
                    tLRPC$TL_message9.media = new TLRPC$TL_messageMediaEmpty();
                    tLRPC$TL_message9.out = true;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser13 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message9.peer_id = tLRPC$TL_peerUser13;
                    tLRPC$TL_peerUser13.user_id = 0L;
                    MessageObject messageObject7 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message9, true, false);
                    TLRPC$TL_message tLRPC$TL_message10 = new TLRPC$TL_message();
                    tLRPC$TL_message10.message = LocaleController.getString("ThemePreviewLine2", R.string.ThemePreviewLine2);
                    tLRPC$TL_message10.date = currentTimeMillis + 960;
                    tLRPC$TL_message10.dialog_id = 1L;
                    tLRPC$TL_message10.flags = 259;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser14 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message10.from_id = tLRPC$TL_peerUser14;
                    tLRPC$TL_peerUser14.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                    tLRPC$TL_message10.id = 1;
                    tLRPC$TL_message10.media = new TLRPC$TL_messageMediaEmpty();
                    tLRPC$TL_message10.out = true;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser15 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message10.peer_id = tLRPC$TL_peerUser15;
                    tLRPC$TL_peerUser15.user_id = 0L;
                    this.messages.add(new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message10, true, false));
                    TLRPC$TL_message tLRPC$TL_message11 = new TLRPC$TL_message();
                    tLRPC$TL_message11.date = currentTimeMillis + 130;
                    tLRPC$TL_message11.dialog_id = 1L;
                    tLRPC$TL_message11.flags = 259;
                    tLRPC$TL_message11.from_id = new TLRPC$TL_peerUser();
                    tLRPC$TL_message11.id = 5;
                    TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument3 = new TLRPC$TL_messageMediaDocument();
                    tLRPC$TL_message11.media = tLRPC$TL_messageMediaDocument3;
                    tLRPC$TL_messageMediaDocument3.flags |= 3;
                    tLRPC$TL_messageMediaDocument3.document = new TLRPC$TL_document();
                    TLRPC$Document tLRPC$Document3 = tLRPC$TL_message11.media.document;
                    tLRPC$Document3.mime_type = "audio/mp4";
                    tLRPC$Document3.file_reference = new byte[0];
                    TLRPC$TL_documentAttributeAudio tLRPC$TL_documentAttributeAudio2 = new TLRPC$TL_documentAttributeAudio();
                    tLRPC$TL_documentAttributeAudio2.duration = 243.0d;
                    tLRPC$TL_documentAttributeAudio2.performer = LocaleController.getString("ThemePreviewSongPerformer", R.string.ThemePreviewSongPerformer);
                    tLRPC$TL_documentAttributeAudio2.title = LocaleController.getString("ThemePreviewSongTitle", R.string.ThemePreviewSongTitle);
                    tLRPC$TL_message11.media.document.attributes.add(tLRPC$TL_documentAttributeAudio2);
                    tLRPC$TL_message11.out = false;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser16 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message11.peer_id = tLRPC$TL_peerUser16;
                    tLRPC$TL_peerUser16.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                    this.messages.add(new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message11, true, false));
                    TLRPC$TL_message tLRPC$TL_message12 = new TLRPC$TL_message();
                    tLRPC$TL_message12.message = LocaleController.getString("ThemePreviewLine3", R.string.ThemePreviewLine3);
                    tLRPC$TL_message12.date = i3;
                    tLRPC$TL_message12.dialog_id = 1L;
                    tLRPC$TL_message12.flags = 265;
                    tLRPC$TL_message12.from_id = new TLRPC$TL_peerUser();
                    tLRPC$TL_message12.id = 1;
                    TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader2 = new TLRPC$TL_messageReplyHeader();
                    tLRPC$TL_message12.reply_to = tLRPC$TL_messageReplyHeader2;
                    tLRPC$TL_messageReplyHeader2.flags |= 16;
                    tLRPC$TL_messageReplyHeader2.reply_to_msg_id = 5;
                    tLRPC$TL_message12.media = new TLRPC$TL_messageMediaEmpty();
                    tLRPC$TL_message12.out = false;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser17 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message12.peer_id = tLRPC$TL_peerUser17;
                    tLRPC$TL_peerUser17.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                    MessageObject messageObject8 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message12, true, false);
                    messageObject8.customReplyName = LocaleController.getString("ThemePreviewLine3Reply", R.string.ThemePreviewLine3Reply);
                    messageObject8.replyMessageObject = messageObject7;
                    this.messages.add(messageObject8);
                    TLRPC$TL_message tLRPC$TL_message13 = new TLRPC$TL_message();
                    tLRPC$TL_message13.date = currentTimeMillis + 120;
                    tLRPC$TL_message13.dialog_id = 1L;
                    tLRPC$TL_message13.flags = 259;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser18 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message13.from_id = tLRPC$TL_peerUser18;
                    tLRPC$TL_peerUser18.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                    tLRPC$TL_message13.id = 1;
                    TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument4 = new TLRPC$TL_messageMediaDocument();
                    tLRPC$TL_message13.media = tLRPC$TL_messageMediaDocument4;
                    tLRPC$TL_messageMediaDocument4.flags |= 3;
                    tLRPC$TL_messageMediaDocument4.document = new TLRPC$TL_document();
                    TLRPC$Document tLRPC$Document4 = tLRPC$TL_message13.media.document;
                    tLRPC$Document4.mime_type = "audio/ogg";
                    tLRPC$Document4.file_reference = new byte[0];
                    TLRPC$TL_documentAttributeAudio tLRPC$TL_documentAttributeAudio3 = new TLRPC$TL_documentAttributeAudio();
                    tLRPC$TL_documentAttributeAudio3.flags = 1028;
                    tLRPC$TL_documentAttributeAudio3.duration = 3.0d;
                    tLRPC$TL_documentAttributeAudio3.voice = true;
                    tLRPC$TL_documentAttributeAudio3.waveform = new byte[]{0, 4, 17, -50, -93, 86, -103, -45, -12, -26, 63, -25, -3, 109, -114, -54, -4, -1, -1, -1, -1, -29, -1, -1, -25, -1, -1, -97, -43, 57, -57, -108, 1, -91, -4, -47, 21, 99, 10, 97, 43, 45, 115, -112, -77, 51, -63, 66, 40, 34, -122, -116, 48, -124, 16, 66, -120, 16, 68, 16, 33, 4, 1};
                    tLRPC$TL_message13.media.document.attributes.add(tLRPC$TL_documentAttributeAudio3);
                    tLRPC$TL_message13.out = true;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser19 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message13.peer_id = tLRPC$TL_peerUser19;
                    tLRPC$TL_peerUser19.user_id = 0L;
                    MessageObject messageObject9 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message13, true, false);
                    messageObject9.audioProgressSec = 1;
                    messageObject9.audioProgress = 0.3f;
                    messageObject9.useCustomPhoto = true;
                    this.messages.add(messageObject9);
                    this.messages.add(messageObject7);
                    TLRPC$TL_message tLRPC$TL_message14 = new TLRPC$TL_message();
                    tLRPC$TL_message14.date = currentTimeMillis + 10;
                    tLRPC$TL_message14.dialog_id = 1L;
                    tLRPC$TL_message14.flags = 257;
                    tLRPC$TL_message14.from_id = new TLRPC$TL_peerUser();
                    tLRPC$TL_message14.id = 1;
                    TLRPC$TL_messageMediaPhoto tLRPC$TL_messageMediaPhoto = new TLRPC$TL_messageMediaPhoto();
                    tLRPC$TL_message14.media = tLRPC$TL_messageMediaPhoto;
                    tLRPC$TL_messageMediaPhoto.flags |= 3;
                    tLRPC$TL_messageMediaPhoto.photo = new TLRPC$TL_photo();
                    TLRPC$Photo tLRPC$Photo = tLRPC$TL_message14.media.photo;
                    tLRPC$Photo.file_reference = new byte[0];
                    tLRPC$Photo.has_stickers = false;
                    tLRPC$Photo.id = 1L;
                    tLRPC$Photo.access_hash = 0L;
                    tLRPC$Photo.date = currentTimeMillis;
                    TLRPC$TL_photoSize tLRPC$TL_photoSize = new TLRPC$TL_photoSize();
                    tLRPC$TL_photoSize.size = 0;
                    tLRPC$TL_photoSize.w = 500;
                    tLRPC$TL_photoSize.h = 302;
                    tLRPC$TL_photoSize.type = "s";
                    tLRPC$TL_photoSize.location = new TLRPC$TL_fileLocationUnavailable();
                    tLRPC$TL_message14.media.photo.sizes.add(tLRPC$TL_photoSize);
                    tLRPC$TL_message14.message = LocaleController.getString("ThemePreviewLine4", R.string.ThemePreviewLine4);
                    tLRPC$TL_message14.out = false;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser20 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message14.peer_id = tLRPC$TL_peerUser20;
                    tLRPC$TL_peerUser20.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                    MessageObject messageObject10 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message14, true, false);
                    messageObject10.useCustomPhoto = true;
                    this.messages.add(messageObject10);
                    return;
                }
            }
            if (ThemePreviewActivity.this.dialogId >= 0) {
                TLRPC$TL_message tLRPC$TL_message15 = new TLRPC$TL_message();
                if (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                    tLRPC$TL_message15.message = LocaleController.getString(R.string.BackgroundColorSinglePreviewLine2);
                } else {
                    tLRPC$TL_message15.message = LocaleController.getString(R.string.BackgroundPreviewLine2);
                }
                tLRPC$TL_message15.date = currentTimeMillis + 60;
                tLRPC$TL_message15.dialog_id = 1L;
                tLRPC$TL_message15.flags = 259;
                tLRPC$TL_message15.id = 1;
                tLRPC$TL_message15.media = new TLRPC$TL_messageMediaEmpty();
                tLRPC$TL_message15.out = true;
                TLRPC$TL_peerUser tLRPC$TL_peerUser21 = new TLRPC$TL_peerUser();
                tLRPC$TL_message15.from_id = tLRPC$TL_peerUser21;
                tLRPC$TL_peerUser21.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                TLRPC$TL_peerUser tLRPC$TL_peerUser22 = new TLRPC$TL_peerUser();
                tLRPC$TL_message15.peer_id = tLRPC$TL_peerUser22;
                tLRPC$TL_peerUser22.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                MessageObject messageObject11 = new MessageObject(this, ((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message15, true, false, ThemePreviewActivity.this) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.1
                    @Override // org.telegram.messenger.MessageObject
                    public boolean needDrawAvatar() {
                        return false;
                    }
                };
                messageObject11.eventId = 1L;
                messageObject11.resetLayout();
                this.messages.add(messageObject11);
            }
            TLRPC$TL_message tLRPC$TL_message16 = new TLRPC$TL_message();
            TLRPC$Chat chat = ThemePreviewActivity.this.dialogId < 0 ? ThemePreviewActivity.this.getMessagesController().getChat(Long.valueOf(-ThemePreviewActivity.this.dialogId)) : null;
            if (chat != null) {
                tLRPC$TL_message16.message = LocaleController.getString(R.string.ChannelBackgroundMessagePreview);
                TLRPC$TL_message tLRPC$TL_message17 = new TLRPC$TL_message();
                tLRPC$TL_message17.message = LocaleController.getString(R.string.ChannelBackgroundMessageReplyText);
                j = 0;
                messageObject = new MessageObject(this, ((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message17, true, false, ThemePreviewActivity.this) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.2
                    @Override // org.telegram.messenger.MessageObject
                    public boolean needDrawAvatar() {
                        return false;
                    }
                };
                TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
                tLRPC$TL_message16.from_id = tLRPC$TL_peerChannel;
                tLRPC$TL_peerChannel.channel_id = chat.id;
                TLRPC$TL_peerChannel tLRPC$TL_peerChannel2 = new TLRPC$TL_peerChannel();
                tLRPC$TL_message16.peer_id = tLRPC$TL_peerChannel2;
                tLRPC$TL_peerChannel2.channel_id = chat.id;
            } else {
                j = 0;
                if (ThemePreviewActivity.this.dialogId == 0) {
                    if (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                        tLRPC$TL_message16.message = LocaleController.getString(R.string.BackgroundColorSinglePreviewLine1);
                    } else {
                        tLRPC$TL_message16.message = LocaleController.getString(R.string.BackgroundPreviewLine1);
                    }
                } else {
                    tLRPC$TL_message16.message = LocaleController.getString(R.string.BackgroundColorSinglePreviewLine3);
                }
                tLRPC$TL_message16.from_id = new TLRPC$TL_peerUser();
                TLRPC$TL_peerUser tLRPC$TL_peerUser23 = new TLRPC$TL_peerUser();
                tLRPC$TL_message16.peer_id = tLRPC$TL_peerUser23;
                tLRPC$TL_peerUser23.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                messageObject = null;
            }
            int i4 = currentTimeMillis + 60;
            tLRPC$TL_message16.date = i4;
            tLRPC$TL_message16.dialog_id = 1L;
            tLRPC$TL_message16.flags = 265;
            tLRPC$TL_message16.id = 1;
            tLRPC$TL_message16.media = new TLRPC$TL_messageMediaEmpty();
            tLRPC$TL_message16.out = false;
            MessageObject messageObject12 = new MessageObject(this, ((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message16, messageObject, true, false, ThemePreviewActivity.this) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.3
                @Override // org.telegram.messenger.MessageObject
                public boolean needDrawAvatar() {
                    return false;
                }
            };
            if (messageObject != null) {
                messageObject12.customReplyName = LocaleController.getString(R.string.ChannelBackgroundMessageReplyName);
            }
            messageObject12.eventId = 1L;
            messageObject12.resetLayout();
            this.messages.add(messageObject12);
            if (ThemePreviewActivity.this.dialogId == j || ThemePreviewActivity.this.serverWallpaper != null) {
                return;
            }
            TLRPC$User user = ThemePreviewActivity.this.getMessagesController().getUser(Long.valueOf(ThemePreviewActivity.this.dialogId));
            TLRPC$TL_message tLRPC$TL_message18 = new TLRPC$TL_message();
            tLRPC$TL_message18.message = "";
            MessageObject messageObject13 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message18, true, false);
            messageObject13.eventId = 1L;
            messageObject13.contentType = 5;
            this.messages.add(messageObject13);
            TLRPC$TL_message tLRPC$TL_message19 = new TLRPC$TL_message();
            if (user != null) {
                tLRPC$TL_message19.message = LocaleController.formatString(R.string.ChatBackgroundHint, UserObject.getFirstName(user));
            } else {
                tLRPC$TL_message19.message = LocaleController.getString(R.string.ChannelBackgroundHint);
            }
            tLRPC$TL_message19.date = i4;
            tLRPC$TL_message19.dialog_id = 1L;
            tLRPC$TL_message19.flags = 265;
            tLRPC$TL_message19.from_id = new TLRPC$TL_peerUser();
            tLRPC$TL_message19.id = 1;
            tLRPC$TL_message19.media = new TLRPC$TL_messageMediaEmpty();
            tLRPC$TL_message19.out = false;
            TLRPC$TL_peerUser tLRPC$TL_peerUser24 = new TLRPC$TL_peerUser();
            tLRPC$TL_message19.peer_id = tLRPC$TL_peerUser24;
            tLRPC$TL_peerUser24.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
            MessageObject messageObject14 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tLRPC$TL_message19, true, false);
            messageObject14.eventId = 1L;
            messageObject14.resetLayout();
            messageObject14.contentType = 1;
            this.messages.add(messageObject14);
        }

        private boolean hasButtons() {
            if (ThemePreviewActivity.this.messagesButtonsContainer == null || ThemePreviewActivity.this.screenType != 1 || ThemePreviewActivity.this.colorType != 3 || ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2 == 0) {
                if (ThemePreviewActivity.this.backgroundButtonsContainer != null) {
                    if (ThemePreviewActivity.this.screenType == 2) {
                        return true;
                    }
                    if (ThemePreviewActivity.this.screenType == 1 && ThemePreviewActivity.this.colorType == 2) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int size = this.messages.size();
            return hasButtons() ? size + 1 : size;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r6v10, types: [org.telegram.ui.Cells.ChatMessageCell] */
        /* JADX WARN: Type inference failed for: r6v4, types: [org.telegram.ui.ThemePreviewActivity$MessagesAdapter$8] */
        /* JADX WARN: Type inference failed for: r6v8, types: [org.telegram.ui.Cells.ChatActionCell] */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            FrameLayout frameLayout;
            if (i == 0) {
                ?? chatMessageCell = new ChatMessageCell(this.mContext, false, null, new Theme.ResourcesProvider() { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.4
                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public /* synthetic */ ColorFilter getAnimatedEmojiColorFilter() {
                        ColorFilter colorFilter;
                        colorFilter = Theme.chat_animatedEmojiTextColorFilter;
                        return colorFilter;
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public /* synthetic */ void setAnimatedColor(int i2, int i3) {
                        Theme.ResourcesProvider.-CC.$default$setAnimatedColor(this, i2, i3);
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public int getColor(int i2) {
                        return ThemePreviewActivity.this.themeDelegate.getColor(i2);
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public Drawable getDrawable(String str) {
                        if (str.equals("drawableMsgOut")) {
                            return ThemePreviewActivity.this.msgOutDrawable;
                        }
                        if (str.equals("drawableMsgOutSelected")) {
                            return ThemePreviewActivity.this.msgOutDrawableSelected;
                        }
                        if (str.equals("drawableMsgOutMedia")) {
                            return ThemePreviewActivity.this.msgOutMediaDrawable;
                        }
                        if (str.equals("drawableMsgOutMediaSelected")) {
                            return ThemePreviewActivity.this.msgOutMediaDrawableSelected;
                        }
                        ThemeDelegate themeDelegate = ThemePreviewActivity.this.themeDelegate;
                        if (themeDelegate != null) {
                            return themeDelegate.getDrawable(str);
                        }
                        return Theme.getThemeDrawable(str);
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public boolean isDark() {
                        return ThemePreviewActivity.this.themeDelegate.isDark();
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public int getCurrentColor(int i2) {
                        return ThemePreviewActivity.this.themeDelegate.getCurrentColor(i2);
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public int getColorOrDefault(int i2) {
                        return ThemePreviewActivity.this.themeDelegate.getColorOrDefault(i2);
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public Paint getPaint(String str) {
                        return ThemePreviewActivity.this.themeDelegate.getPaint(str);
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public boolean hasGradientService() {
                        return ThemePreviewActivity.this.themeDelegate.hasGradientService();
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public void applyServiceShaderMatrix(int i2, int i3, float f, float f2) {
                        ThemeDelegate themeDelegate = ThemePreviewActivity.this.themeDelegate;
                        if (themeDelegate == null) {
                            Theme.applyServiceShaderMatrix(i2, i3, f, f2);
                        } else {
                            themeDelegate.applyServiceShaderMatrix(i2, i3, f, f2);
                        }
                    }
                });
                chatMessageCell.setDelegate(new ChatMessageCell.ChatMessageCellDelegate(this) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.5
                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean canDrawOutboundsContent() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$canDrawOutboundsContent(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean canPerformActions() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$canPerformActions(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didLongPress(ChatMessageCell chatMessageCell2, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPress(this, chatMessageCell2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didLongPressBotButton(ChatMessageCell chatMessageCell2, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressBotButton(this, chatMessageCell2, tLRPC$KeyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean didLongPressChannelAvatar(ChatMessageCell chatMessageCell2, TLRPC$Chat tLRPC$Chat, int i2, float f, float f2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressChannelAvatar(this, chatMessageCell2, tLRPC$Chat, i2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean didLongPressUserAvatar(ChatMessageCell chatMessageCell2, TLRPC$User tLRPC$User, float f, float f2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressUserAvatar(this, chatMessageCell2, tLRPC$User, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean didPressAnimatedEmoji(ChatMessageCell chatMessageCell2, AnimatedEmojiSpan animatedEmojiSpan) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressAnimatedEmoji(this, chatMessageCell2, animatedEmojiSpan);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressBotButton(ChatMessageCell chatMessageCell2, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressBotButton(this, chatMessageCell2, tLRPC$KeyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressCancelSendButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCancelSendButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressChannelAvatar(ChatMessageCell chatMessageCell2, TLRPC$Chat tLRPC$Chat, int i2, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelAvatar(this, chatMessageCell2, tLRPC$Chat, i2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressChannelRecommendation(ChatMessageCell chatMessageCell2, TLRPC$Chat tLRPC$Chat, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelRecommendation(this, chatMessageCell2, tLRPC$Chat, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressChannelRecommendationsClose(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelRecommendationsClose(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressCodeCopy(ChatMessageCell chatMessageCell2, MessageObject.TextLayoutBlock textLayoutBlock) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCodeCopy(this, chatMessageCell2, textLayoutBlock);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressCommentButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCommentButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressExtendedMediaPreview(ChatMessageCell chatMessageCell2, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressExtendedMediaPreview(this, chatMessageCell2, tLRPC$KeyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressGiveawayChatButton(ChatMessageCell chatMessageCell2, int i2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressGiveawayChatButton(this, chatMessageCell2, i2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressHiddenForward(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressHiddenForward(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressHint(ChatMessageCell chatMessageCell2, int i2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressHint(this, chatMessageCell2, i2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressImage(ChatMessageCell chatMessageCell2, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressImage(this, chatMessageCell2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressInstantButton(ChatMessageCell chatMessageCell2, int i2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressInstantButton(this, chatMessageCell2, i2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressMoreChannelRecommendations(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressMoreChannelRecommendations(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressOther(ChatMessageCell chatMessageCell2, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressOther(this, chatMessageCell2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressReaction(ChatMessageCell chatMessageCell2, TLRPC$ReactionCount tLRPC$ReactionCount, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReaction(this, chatMessageCell2, tLRPC$ReactionCount, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressReplyMessage(ChatMessageCell chatMessageCell2, int i2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReplyMessage(this, chatMessageCell2, i2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSideButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSideButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSponsoredClose() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSponsoredClose(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressTime(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressTime(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressTopicButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressTopicButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressUrl(ChatMessageCell chatMessageCell2, CharacterStyle characterStyle, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUrl(this, chatMessageCell2, characterStyle, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressUserAvatar(ChatMessageCell chatMessageCell2, TLRPC$User tLRPC$User, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserAvatar(this, chatMessageCell2, tLRPC$User, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressUserStatus(ChatMessageCell chatMessageCell2, TLRPC$User tLRPC$User, TLRPC$Document tLRPC$Document) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserStatus(this, chatMessageCell2, tLRPC$User, tLRPC$Document);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressViaBot(ChatMessageCell chatMessageCell2, String str) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressViaBot(this, chatMessageCell2, str);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressViaBotNotInline(ChatMessageCell chatMessageCell2, long j) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressViaBotNotInline(this, chatMessageCell2, j);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressVoteButtons(ChatMessageCell chatMessageCell2, ArrayList arrayList, int i2, int i3, int i4) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressVoteButtons(this, chatMessageCell2, arrayList, i2, i3, i4);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressWebPage(ChatMessageCell chatMessageCell2, TLRPC$WebPage tLRPC$WebPage, String str, boolean z) {
                        Browser.openUrl(chatMessageCell2.getContext(), str);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didStartVideoStream(MessageObject messageObject) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didStartVideoStream(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ String getAdminRank(long j) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getAdminRank(this, j);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ PinchToZoomHelper getPinchToZoomHelper() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getPinchToZoomHelper(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ String getProgressLoadingBotButtonUrl(ChatMessageCell chatMessageCell2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getProgressLoadingBotButtonUrl(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ CharacterStyle getProgressLoadingLink(ChatMessageCell chatMessageCell2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getProgressLoadingLink(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ TextSelectionHelper.ChatListTextSelectionHelper getTextSelectionHelper() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$getTextSelectionHelper(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean hasSelectedMessages() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$hasSelectedMessages(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void invalidateBlur() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$invalidateBlur(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean isLandscape() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isLandscape(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean isProgressLoading(ChatMessageCell chatMessageCell2, int i2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isProgressLoading(this, chatMessageCell2, i2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean isReplyOrSelf() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$isReplyOrSelf(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean keyboardIsOpened() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$keyboardIsOpened(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void needOpenWebView(MessageObject messageObject, String str, String str2, String str3, String str4, int i2, int i3) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needOpenWebView(this, messageObject, str, str2, str3, str4, i2, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean needPlayMessage(ChatMessageCell chatMessageCell2, MessageObject messageObject, boolean z) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needPlayMessage(this, chatMessageCell2, messageObject, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void needReloadPolls() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needReloadPolls(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void needShowPremiumBulletin(int i2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$needShowPremiumBulletin(this, i2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean onAccessibilityAction(int i2, Bundle bundle) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$onAccessibilityAction(this, i2, bundle);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void onDiceFinished() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$onDiceFinished(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void setShouldNotRepeatSticker(MessageObject messageObject) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$setShouldNotRepeatSticker(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldDrawThreadProgress(ChatMessageCell chatMessageCell2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldDrawThreadProgress(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldRepeatSticker(MessageObject messageObject) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldRepeatSticker(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldShowTopicButton() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldShowTopicButton(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void videoTimerReached() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$videoTimerReached(this);
                    }
                });
                frameLayout = chatMessageCell;
            } else if (i == 1) {
                ?? chatActionCell = new ChatActionCell(this.mContext, false, ThemePreviewActivity.this.themeDelegate);
                chatActionCell.setDelegate(new ChatActionCell.ChatActionCellDelegate(this) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.6
                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ boolean canDrawOutboundsContent() {
                        return ChatActionCell.ChatActionCellDelegate.-CC.$default$canDrawOutboundsContent(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void didClickButton(ChatActionCell chatActionCell2) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$didClickButton(this, chatActionCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void didClickImage(ChatActionCell chatActionCell2) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$didClickImage(this, chatActionCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ boolean didLongPress(ChatActionCell chatActionCell2, float f, float f2) {
                        return ChatActionCell.ChatActionCellDelegate.-CC.$default$didLongPress(this, chatActionCell2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void didOpenPremiumGift(ChatActionCell chatActionCell2, TLRPC$TL_premiumGiftOption tLRPC$TL_premiumGiftOption, String str, boolean z) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$didOpenPremiumGift(this, chatActionCell2, tLRPC$TL_premiumGiftOption, str, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void didOpenPremiumGiftChannel(ChatActionCell chatActionCell2, String str, boolean z) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$didOpenPremiumGiftChannel(this, chatActionCell2, str, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void didPressReplyMessage(ChatActionCell chatActionCell2, int i2) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$didPressReplyMessage(this, chatActionCell2, i2);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ BaseFragment getBaseFragment() {
                        return ChatActionCell.ChatActionCellDelegate.-CC.$default$getBaseFragment(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ long getDialogId() {
                        return ChatActionCell.ChatActionCellDelegate.-CC.$default$getDialogId(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ int getTopicId() {
                        return ChatActionCell.ChatActionCellDelegate.-CC.$default$getTopicId(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void needOpenInviteLink(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$needOpenInviteLink(this, tLRPC$TL_chatInviteExported);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void needOpenUserProfile(long j) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$needOpenUserProfile(this, j);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void needShowEffectOverlay(ChatActionCell chatActionCell2, TLRPC$Document tLRPC$Document, TLRPC$VideoSize tLRPC$VideoSize) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$needShowEffectOverlay(this, chatActionCell2, tLRPC$Document, tLRPC$VideoSize);
                    }
                });
                frameLayout = chatActionCell;
            } else if (i == 2) {
                if (ThemePreviewActivity.this.backgroundButtonsContainer.getParent() != null) {
                    ((ViewGroup) ThemePreviewActivity.this.backgroundButtonsContainer.getParent()).removeView(ThemePreviewActivity.this.backgroundButtonsContainer);
                }
                FrameLayout frameLayout2 = new FrameLayout(this, this.mContext) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.7
                    @Override // android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i2, int i3) {
                        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60.0f), 1073741824));
                    }
                };
                frameLayout2.addView(ThemePreviewActivity.this.backgroundButtonsContainer, LayoutHelper.createFrame(-1, 76, 17));
                frameLayout = frameLayout2;
            } else if (i != 5) {
                if (ThemePreviewActivity.this.messagesButtonsContainer.getParent() != null) {
                    ((ViewGroup) ThemePreviewActivity.this.messagesButtonsContainer.getParent()).removeView(ThemePreviewActivity.this.messagesButtonsContainer);
                }
                FrameLayout frameLayout3 = new FrameLayout(this, this.mContext) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.9
                    @Override // android.widget.FrameLayout, android.view.View
                    protected void onMeasure(int i2, int i3) {
                        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60.0f), 1073741824));
                    }
                };
                frameLayout3.addView(ThemePreviewActivity.this.messagesButtonsContainer, LayoutHelper.createFrame(-1, 76, 17));
                frameLayout = frameLayout3;
            } else {
                frameLayout = new View(this, ThemePreviewActivity.this.getContext()) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.8
                    @Override // android.view.View
                    protected void onMeasure(int i2, int i3) {
                        super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(4.0f), 1073741824));
                    }
                };
            }
            frameLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(frameLayout);
        }

        /* JADX WARN: Removed duplicated region for block: B:35:0x00a3  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            boolean z;
            boolean z2;
            MessageObject messageObject;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 2 || itemViewType == 3) {
                return;
            }
            if (hasButtons()) {
                i--;
            }
            MessageObject messageObject2 = this.messages.get(i);
            View view = viewHolder.itemView;
            if (view instanceof ChatMessageCell) {
                ChatMessageCell chatMessageCell = (ChatMessageCell) view;
                boolean z3 = false;
                chatMessageCell.isChat = false;
                int i2 = i - 1;
                int itemViewType2 = getItemViewType(i2);
                int i3 = i + 1;
                int itemViewType3 = getItemViewType(i3);
                if (!(messageObject2.messageOwner.reply_markup instanceof TLRPC$TL_replyInlineMarkup) && itemViewType2 == viewHolder.getItemViewType()) {
                    MessageObject messageObject3 = this.messages.get(i2);
                    if (messageObject3.isOutOwner() == messageObject2.isOutOwner() && Math.abs(messageObject3.messageOwner.date - messageObject2.messageOwner.date) <= 300) {
                        z = true;
                        if (itemViewType3 == viewHolder.getItemViewType() && i3 < this.messages.size()) {
                            messageObject = this.messages.get(i3);
                            if (!(messageObject.messageOwner.reply_markup instanceof TLRPC$TL_replyInlineMarkup) && messageObject.isOutOwner() == messageObject2.isOutOwner() && Math.abs(messageObject.messageOwner.date - messageObject2.messageOwner.date) <= 300) {
                                z2 = true;
                                chatMessageCell.isChat = (!this.showSecretMessages || ThemePreviewActivity.this.dialogId < 0) ? true : true;
                                chatMessageCell.setFullyDraw(true);
                                chatMessageCell.setMessageObject(messageObject2, null, z, z2);
                            }
                        }
                        z2 = false;
                        chatMessageCell.isChat = (!this.showSecretMessages || ThemePreviewActivity.this.dialogId < 0) ? true : true;
                        chatMessageCell.setFullyDraw(true);
                        chatMessageCell.setMessageObject(messageObject2, null, z, z2);
                    }
                }
                z = false;
                if (itemViewType3 == viewHolder.getItemViewType()) {
                    messageObject = this.messages.get(i3);
                    if (!(messageObject.messageOwner.reply_markup instanceof TLRPC$TL_replyInlineMarkup)) {
                        z2 = true;
                        chatMessageCell.isChat = (!this.showSecretMessages || ThemePreviewActivity.this.dialogId < 0) ? true : true;
                        chatMessageCell.setFullyDraw(true);
                        chatMessageCell.setMessageObject(messageObject2, null, z, z2);
                    }
                }
                z2 = false;
                chatMessageCell.isChat = (!this.showSecretMessages || ThemePreviewActivity.this.dialogId < 0) ? true : true;
                chatMessageCell.setFullyDraw(true);
                chatMessageCell.setMessageObject(messageObject2, null, z, z2);
            } else if (view instanceof ChatActionCell) {
                ChatActionCell chatActionCell = (ChatActionCell) view;
                chatActionCell.setMessageObject(messageObject2);
                chatActionCell.setAlpha(1.0f);
                ThemePreviewActivity.this.invalidateBlur();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (hasButtons()) {
                if (i == 0) {
                    return ThemePreviewActivity.this.colorType == 3 ? 3 : 2;
                }
                i--;
            }
            if (i < 0 || i >= this.messages.size()) {
                return 4;
            }
            return this.messages.get(i).contentType;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class PatternsAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        public PatternsAdapter(Context context) {
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (ThemePreviewActivity.this.patterns != null) {
                return ThemePreviewActivity.this.patterns.size();
            }
            return 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RecyclerListView.Holder(new PatternCell(this.mContext, ThemePreviewActivity.this.maxWallpaperSize, new PatternCell.PatternCellDelegate() { // from class: org.telegram.ui.ThemePreviewActivity.PatternsAdapter.1
                @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
                public TLRPC$TL_wallPaper getSelectedPattern() {
                    return ThemePreviewActivity.this.selectedPattern;
                }

                @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
                public int getCheckColor() {
                    return ThemePreviewActivity.this.checkColor;
                }

                @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
                public int getBackgroundColor() {
                    if (ThemePreviewActivity.this.screenType == 2) {
                        return ThemePreviewActivity.this.backgroundColor;
                    }
                    int defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper);
                    int i2 = (int) ThemePreviewActivity.this.accent.backgroundOverrideColor;
                    return i2 != 0 ? i2 : defaultAccentColor;
                }

                @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
                public int getBackgroundGradientColor1() {
                    if (ThemePreviewActivity.this.screenType == 2) {
                        return ThemePreviewActivity.this.backgroundGradientColor1;
                    }
                    int defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to1);
                    int i2 = (int) ThemePreviewActivity.this.accent.backgroundGradientOverrideColor1;
                    return i2 != 0 ? i2 : defaultAccentColor;
                }

                @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
                public int getBackgroundGradientColor2() {
                    if (ThemePreviewActivity.this.screenType == 2) {
                        return ThemePreviewActivity.this.backgroundGradientColor2;
                    }
                    int defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to2);
                    int i2 = (int) ThemePreviewActivity.this.accent.backgroundGradientOverrideColor2;
                    return i2 != 0 ? i2 : defaultAccentColor;
                }

                @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
                public int getBackgroundGradientColor3() {
                    if (ThemePreviewActivity.this.screenType == 2) {
                        return ThemePreviewActivity.this.backgroundGradientColor3;
                    }
                    int defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to3);
                    int i2 = (int) ThemePreviewActivity.this.accent.backgroundGradientOverrideColor3;
                    return i2 != 0 ? i2 : defaultAccentColor;
                }

                @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
                public int getBackgroundGradientAngle() {
                    return ThemePreviewActivity.this.screenType == 2 ? ThemePreviewActivity.this.backgroundRotation : ThemePreviewActivity.this.accent.backgroundRotation;
                }

                @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
                public float getIntensity() {
                    return ThemePreviewActivity.this.currentIntensity;
                }

                @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
                public int getPatternColor() {
                    return ThemePreviewActivity.this.patternColor;
                }
            }));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            PatternCell patternCell = (PatternCell) viewHolder.itemView;
            patternCell.setPattern((TLRPC$TL_wallPaper) ThemePreviewActivity.this.patterns.get(i));
            patternCell.getImageReceiver().setColorFilter(new PorterDuffColorFilter(ThemePreviewActivity.this.patternColor, ThemePreviewActivity.this.blendMode));
            if (Build.VERSION.SDK_INT >= 29) {
                int i2 = 0;
                if (ThemePreviewActivity.this.screenType != 1) {
                    if (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                        i2 = ThemePreviewActivity.this.backgroundGradientColor2;
                    }
                } else {
                    int defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to2);
                    int i3 = (int) ThemePreviewActivity.this.accent.backgroundGradientOverrideColor2;
                    if (i3 != 0 || ThemePreviewActivity.this.accent.backgroundGradientOverrideColor2 == 0) {
                        if (i3 != 0) {
                            defaultAccentColor = i3;
                        }
                        i2 = defaultAccentColor;
                    }
                }
                if (i2 != 0 && ThemePreviewActivity.this.currentIntensity >= 0.0f) {
                    ThemePreviewActivity.this.backgroundImage.getImageReceiver().setBlendMode(BlendMode.SOFT_LIGHT);
                } else {
                    patternCell.getImageReceiver().setBlendMode(null);
                }
            }
        }
    }

    public ArrayList<ThemeDescription> getThemeDescriptionsInternal() {
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda32
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                ThemePreviewActivity.this.lambda$getThemeDescriptionsInternal$33();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        FrameLayout frameLayout = this.page1;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(frameLayout, i, null, null, null, themeDescriptionDelegate, i2));
        ViewPager viewPager = this.viewPager;
        int i3 = ThemeDescription.FLAG_LISTGLOWCOLOR;
        int i4 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(viewPager, i3, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i4));
        ActionBar actionBar = this.actionBar;
        int i5 = ThemeDescription.FLAG_AB_SELECTORCOLOR;
        int i6 = Theme.key_actionBarDefaultSelector;
        arrayList.add(new ThemeDescription(actionBar, i5, null, null, null, null, i6));
        ActionBar actionBar2 = this.actionBar;
        int i7 = ThemeDescription.FLAG_AB_TITLECOLOR;
        int i8 = Theme.key_actionBarDefaultTitle;
        arrayList.add(new ThemeDescription(actionBar2, i7, null, null, null, null, i8));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder));
        arrayList.add(new ThemeDescription(this.actionBar2, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.actionBar2, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, i8));
        arrayList.add(new ThemeDescription(this.actionBar2, ThemeDescription.FLAG_AB_SUBTITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultSubtitle));
        arrayList.add(new ThemeDescription(this.actionBar2, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, i6));
        arrayList.add(new ThemeDescription(this.actionBar2, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, themeDescriptionDelegate, Theme.key_actionBarDefaultSubmenuBackground));
        arrayList.add(new ThemeDescription(this.actionBar2, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, themeDescriptionDelegate, Theme.key_actionBarDefaultSubmenuItem));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.listView2, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chats_actionIcon));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chats_actionBackground));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_chats_actionPressedBackground));
        if (!this.useDefaultThemeForButtons) {
            arrayList.add(new ThemeDescription(this.saveButtonsContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, i2));
            TextView textView = this.cancelButton;
            int i9 = ThemeDescription.FLAG_TEXTCOLOR;
            int i10 = Theme.key_chat_fieldOverlayText;
            arrayList.add(new ThemeDescription(textView, i9, null, null, null, null, i10));
            arrayList.add(new ThemeDescription(this.doneButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i10));
        }
        ColorPicker colorPicker = this.colorPicker;
        if (colorPicker != null) {
            colorPicker.provideThemeDescriptions(arrayList);
        }
        if (this.patternLayout != null) {
            for (int i11 = 0; i11 < this.patternLayout.length; i11++) {
                arrayList.add(new ThemeDescription(this.patternLayout[i11], 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, Theme.key_chat_messagePanelShadow));
                arrayList.add(new ThemeDescription(this.patternLayout[i11], 0, null, Theme.chat_composeBackgroundPaint, null, null, Theme.key_chat_messagePanelBackground));
            }
            for (int i12 = 0; i12 < this.patternsButtonsContainer.length; i12++) {
                arrayList.add(new ThemeDescription(this.patternsButtonsContainer[i12], 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, Theme.key_chat_messagePanelShadow));
                arrayList.add(new ThemeDescription(this.patternsButtonsContainer[i12], 0, null, Theme.chat_composeBackgroundPaint, null, null, Theme.key_chat_messagePanelBackground));
            }
            arrayList.add(new ThemeDescription(this.bottomOverlayChat, 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, Theme.key_chat_messagePanelShadow));
            arrayList.add(new ThemeDescription(this.bottomOverlayChat, 0, null, Theme.chat_composeBackgroundPaint, null, null, Theme.key_chat_messagePanelBackground));
            for (int i13 = 0; i13 < this.patternsSaveButton.length; i13++) {
                arrayList.add(new ThemeDescription(this.patternsSaveButton[i13], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_fieldOverlayText));
            }
            for (int i14 = 0; i14 < this.patternsCancelButton.length; i14++) {
                arrayList.add(new ThemeDescription(this.patternsCancelButton[i14], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_fieldOverlayText));
            }
            arrayList.add(new ThemeDescription(this.intensitySeekBar, 0, new Class[]{SeekBarView.class}, new String[]{"innerPaint1"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_player_progressBackground));
            arrayList.add(new ThemeDescription(this.intensitySeekBar, 0, new Class[]{SeekBarView.class}, new String[]{"outerPaint1"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_player_progress));
            arrayList.add(new ThemeDescription(this.intensityCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable}, null, Theme.key_chat_inBubble));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable}, null, Theme.key_chat_inBubbleSelected));
            Drawable[] shadowDrawables = Theme.chat_msgInDrawable.getShadowDrawables();
            int i15 = Theme.key_chat_inBubbleShadow;
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, shadowDrawables, null, i15));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, Theme.chat_msgInMediaDrawable.getShadowDrawables(), null, i15));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{this.msgOutDrawable, this.msgOutMediaDrawable}, null, Theme.key_chat_outBubble));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{this.msgOutDrawable, this.msgOutMediaDrawable}, null, Theme.key_chat_outBubbleGradient1));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{this.msgOutDrawable, this.msgOutMediaDrawable}, null, Theme.key_chat_outBubbleGradient2));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{this.msgOutDrawable, this.msgOutMediaDrawable}, null, Theme.key_chat_outBubbleGradient3));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable}, null, Theme.key_chat_outBubbleSelected));
            Drawable[] shadowDrawables2 = Theme.chat_msgOutDrawable.getShadowDrawables();
            int i16 = Theme.key_chat_outBubbleShadow;
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, shadowDrawables2, null, i16));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, Theme.chat_msgOutMediaDrawable.getShadowDrawables(), null, i16));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_messageTextIn));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_messageTextOut));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckDrawable}, null, Theme.key_chat_outSentCheck));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckSelectedDrawable}, null, Theme.key_chat_outSentCheckSelected));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckReadDrawable, Theme.chat_msgOutHalfCheckDrawable}, null, Theme.key_chat_outSentCheckRead));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckReadSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable}, null, Theme.key_chat_outSentCheckReadSelected));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable}, null, Theme.key_chat_mediaSentCheck));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inReplyLine));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outReplyLine));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inReplyNameText));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outReplyNameText));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inReplyMessageText));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outReplyMessageText));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inReplyMediaMessageSelectedText));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outReplyMediaMessageSelectedText));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inTimeText));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outTimeText));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inTimeSelectedText));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outTimeSelectedText));
        }
        arrayList.add(new ThemeDescription((View) null, 0, (Class[]) null, (String[]) null, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_divider));
        arrayList.add(new ThemeDescription((View) null, 0, (Class[]) null, (String[]) null, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_dialogBackground));
        arrayList.add(new ThemeDescription((View) null, 0, (Class[]) null, (String[]) null, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription((View) null, 0, (Class[]) null, (String[]) null, (Paint[]) null, (Drawable[]) null, themeDescriptionDelegate, Theme.key_dialogBackgroundGray));
        for (int i17 = 0; i17 < arrayList.size(); i17++) {
            arrayList.get(i17).resourcesProvider = getResourceProvider();
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getThemeDescriptionsInternal$33() {
        ActionBarMenuItem actionBarMenuItem = this.dropDownContainer;
        int i = 0;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.redrawPopup(getThemedColor(Theme.key_actionBarDefaultSubmenuBackground));
            this.dropDownContainer.setPopupItemsColor(getThemedColor(Theme.key_actionBarDefaultSubmenuItem), false);
        }
        Drawable drawable = this.sheetDrawable;
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundWhite), PorterDuff.Mode.MULTIPLY));
        }
        FrameLayout frameLayout = this.bottomOverlayChat;
        if (frameLayout != null) {
            frameLayout.invalidate();
        }
        if (this.onSwitchDayNightDelegate != null) {
            INavigationLayout iNavigationLayout = this.parentLayout;
            if (iNavigationLayout != null && iNavigationLayout.getBottomSheet() != null) {
                this.parentLayout.getBottomSheet().fixNavigationBar(getThemedColor(Theme.key_dialogBackground));
                if (this.screenType == 2 && this.dialogId != 0) {
                    this.parentLayout.getBottomSheet().setOverlayNavBarColor(-16777216);
                }
            } else {
                setNavigationBarColor(getThemedColor(Theme.key_dialogBackground));
            }
        }
        if (this.backgroundCheckBoxView != null) {
            int i2 = 0;
            while (true) {
                WallpaperCheckBoxView[] wallpaperCheckBoxViewArr = this.backgroundCheckBoxView;
                if (i2 >= wallpaperCheckBoxViewArr.length) {
                    break;
                }
                if (wallpaperCheckBoxViewArr[i2] != null) {
                    wallpaperCheckBoxViewArr[i2].invalidate();
                }
                i2++;
            }
        }
        if (this.messagesCheckBoxView != null) {
            while (true) {
                WallpaperCheckBoxView[] wallpaperCheckBoxViewArr2 = this.messagesCheckBoxView;
                if (i >= wallpaperCheckBoxViewArr2.length) {
                    break;
                }
                if (wallpaperCheckBoxViewArr2[i] != null) {
                    wallpaperCheckBoxViewArr2[i].invalidate();
                }
                i++;
            }
        }
        TextView textView = this.patternTitleView;
        if (textView != null) {
            textView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
        }
        ColorPicker colorPicker = this.colorPicker;
        if (colorPicker != null) {
            colorPicker.invalidate();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        if (this.shouldShowDayNightIcon) {
            return getThemeDescriptionsInternal();
        }
        return super.getThemeDescriptions();
    }

    private void createServiceMessageLocal(TLRPC$WallPaper tLRPC$WallPaper, boolean z) {
        TLRPC$TL_messageService tLRPC$TL_messageService = new TLRPC$TL_messageService();
        tLRPC$TL_messageService.random_id = SendMessagesHelper.getInstance(this.currentAccount).getNextRandomId();
        tLRPC$TL_messageService.dialog_id = this.dialogId;
        tLRPC$TL_messageService.unread = true;
        tLRPC$TL_messageService.out = true;
        int newMessageId = getUserConfig().getNewMessageId();
        tLRPC$TL_messageService.id = newMessageId;
        tLRPC$TL_messageService.local_id = newMessageId;
        TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
        if (ChatObject.isChannel(chat)) {
            TLRPC$TL_peerChannel tLRPC$TL_peerChannel = new TLRPC$TL_peerChannel();
            tLRPC$TL_messageService.from_id = tLRPC$TL_peerChannel;
            tLRPC$TL_peerChannel.channel_id = chat.id;
            TLRPC$TL_peerChannel tLRPC$TL_peerChannel2 = new TLRPC$TL_peerChannel();
            tLRPC$TL_messageService.peer_id = tLRPC$TL_peerChannel2;
            tLRPC$TL_peerChannel2.channel_id = chat.id;
        } else {
            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
            tLRPC$TL_messageService.from_id = tLRPC$TL_peerUser;
            tLRPC$TL_peerUser.user_id = getUserConfig().getClientUserId();
            TLRPC$TL_peerUser tLRPC$TL_peerUser2 = new TLRPC$TL_peerUser();
            tLRPC$TL_messageService.peer_id = tLRPC$TL_peerUser2;
            tLRPC$TL_peerUser2.user_id = this.dialogId;
        }
        tLRPC$TL_messageService.flags |= LiteMode.FLAG_CHAT_BLUR;
        tLRPC$TL_messageService.date = getConnectionsManager().getCurrentTime();
        TLRPC$TL_messageActionSetChatWallPaper tLRPC$TL_messageActionSetChatWallPaper = new TLRPC$TL_messageActionSetChatWallPaper();
        tLRPC$TL_messageService.action = tLRPC$TL_messageActionSetChatWallPaper;
        tLRPC$TL_messageActionSetChatWallPaper.wallpaper = tLRPC$WallPaper;
        tLRPC$TL_messageActionSetChatWallPaper.for_both = z;
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        arrayList.add(new MessageObject(this.currentAccount, tLRPC$TL_messageService, false, false));
        new ArrayList().add(tLRPC$TL_messageService);
        MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(this.dialogId, arrayList, false);
    }

    /* loaded from: classes3.dex */
    public class BackgroundView extends BackupImageView {
        public Drawable background;
        boolean drawBackground;
        public float tx;
        public float ty;

        public BackgroundView(Context context) {
            super(context);
            this.drawBackground = true;
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            ThemePreviewActivity themePreviewActivity = ThemePreviewActivity.this;
            themePreviewActivity.parallaxScale = themePreviewActivity.parallaxEffect.getScale(getMeasuredWidth(), getMeasuredHeight());
            if (ThemePreviewActivity.this.isMotion) {
                setScaleX(ThemePreviewActivity.this.parallaxScale);
                setScaleY(ThemePreviewActivity.this.parallaxScale);
            }
            ThemePreviewActivity themePreviewActivity2 = ThemePreviewActivity.this;
            themePreviewActivity2.progressVisible = themePreviewActivity2.screenType == 2 && getMeasuredWidth() <= getMeasuredHeight();
            int measuredWidth = getMeasuredWidth() + (getMeasuredHeight() << 16);
            if (ThemePreviewActivity.this.lastSizeHash != measuredWidth) {
                ThemePreviewActivity.this.hasScrollingBackground = false;
                if (ThemePreviewActivity.this.currentWallpaperBitmap != null) {
                    int width = (int) (ThemePreviewActivity.this.currentWallpaperBitmap.getWidth() * (getMeasuredHeight() / ThemePreviewActivity.this.currentWallpaperBitmap.getHeight()));
                    if (width - getMeasuredWidth() > 100) {
                        ThemePreviewActivity.this.hasScrollingBackground = true;
                        ThemePreviewActivity.this.croppedWidth = (int) (getMeasuredWidth() * (ThemePreviewActivity.this.currentWallpaperBitmap.getHeight() / getMeasuredHeight()));
                        ThemePreviewActivity themePreviewActivity3 = ThemePreviewActivity.this;
                        float measuredWidth2 = (width - getMeasuredWidth()) / 2.0f;
                        themePreviewActivity3.currentScrollOffset = measuredWidth2;
                        themePreviewActivity3.defaultScrollOffset = measuredWidth2;
                        ThemePreviewActivity themePreviewActivity4 = ThemePreviewActivity.this;
                        themePreviewActivity4.maxScrollOffset = themePreviewActivity4.currentScrollOffset * 2.0f;
                        setSize(width, getMeasuredHeight());
                        this.drawFromStart = true;
                        ThemePreviewActivity.this.invalidateBlur();
                    }
                }
                if (!ThemePreviewActivity.this.hasScrollingBackground) {
                    setSize(-1, -1);
                    this.drawFromStart = false;
                }
            }
            ThemePreviewActivity.this.lastSizeHash = measuredWidth;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onDraw(Canvas canvas) {
            this.tx = 0.0f;
            this.ty = 0.0f;
            if (this.drawBackground) {
                Drawable drawable = this.background;
                if ((drawable instanceof ColorDrawable) || (drawable instanceof GradientDrawable) || (drawable instanceof MotionBackgroundDrawable)) {
                    drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                    this.background.draw(canvas);
                } else if (drawable instanceof BitmapDrawable) {
                    if (((BitmapDrawable) drawable).getTileModeX() == Shader.TileMode.REPEAT) {
                        canvas.save();
                        float f = 2.0f / AndroidUtilities.density;
                        canvas.scale(f, f);
                        this.background.setBounds(0, 0, (int) Math.ceil(getMeasuredWidth() / f), (int) Math.ceil(getMeasuredHeight() / f));
                        this.background.draw(canvas);
                        canvas.restore();
                    } else {
                        int measuredHeight = getMeasuredHeight();
                        float max = Math.max(getMeasuredWidth() / this.background.getIntrinsicWidth(), measuredHeight / this.background.getIntrinsicHeight());
                        int ceil = (int) Math.ceil(this.background.getIntrinsicWidth() * max * ThemePreviewActivity.this.parallaxScale);
                        int ceil2 = (int) Math.ceil(this.background.getIntrinsicHeight() * max * ThemePreviewActivity.this.parallaxScale);
                        int measuredWidth = (getMeasuredWidth() - ceil) / 2;
                        int i = (measuredHeight - ceil2) / 2;
                        this.ty = i;
                        this.background.setBounds(measuredWidth, i, ceil + measuredWidth, ceil2 + i);
                        this.background.draw(canvas);
                    }
                }
            }
            if (ThemePreviewActivity.this.hasScrollingBackground) {
                if (!ThemePreviewActivity.this.scroller.isFinished() && ThemePreviewActivity.this.scroller.computeScrollOffset()) {
                    ThemePreviewActivity themePreviewActivity = ThemePreviewActivity.this;
                    if (ThemePreviewActivity.this.scroller.getStartX() < themePreviewActivity.maxScrollOffset && themePreviewActivity.scroller.getStartX() > 0) {
                        ThemePreviewActivity themePreviewActivity2 = ThemePreviewActivity.this;
                        themePreviewActivity2.currentScrollOffset = themePreviewActivity2.scroller.getCurrX();
                    }
                    ThemePreviewActivity.this.invalidateBlur();
                    invalidate();
                }
                canvas.save();
                float f2 = ThemePreviewActivity.this.currentScrollOffset;
                this.tx = -f2;
                canvas.translate(-f2, 0.0f);
                super.onDraw(canvas);
                canvas.restore();
            } else {
                super.onDraw(canvas);
            }
            if (!ThemePreviewActivity.this.shouldShowBrightnessControll || ThemePreviewActivity.this.dimAmount <= 0.0f) {
                return;
            }
            canvas.drawColor(ColorUtils.setAlphaComponent(-16777216, (int) (ThemePreviewActivity.this.dimAmount * 255.0f * ThemePreviewActivity.this.progressToDarkTheme)));
        }

        @Override // android.view.View
        public Drawable getBackground() {
            return this.background;
        }

        @Override // android.view.View
        public void setBackground(Drawable drawable) {
            this.background = drawable;
            if (drawable != null) {
                drawable.setCallback(this);
            }
        }

        @Override // android.view.View
        protected boolean verifyDrawable(Drawable drawable) {
            return this.background == drawable || super.verifyDrawable(drawable);
        }
    }

    /* loaded from: classes3.dex */
    private class MessageDrawable extends Theme.MessageDrawable {
        public MessageDrawable(int i, boolean z, boolean z2) {
            super(i, z, z2);
        }

        @Override // org.telegram.ui.ActionBar.Theme.MessageDrawable
        public void setTop(int i, int i2, int i3, boolean z, boolean z2) {
            if (ThemePreviewActivity.this.setupFinished) {
                return;
            }
            super.setTop(i, i2, i3, z, z2);
        }

        @Override // org.telegram.ui.ActionBar.Theme.MessageDrawable
        public void setTop(int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2) {
            if (ThemePreviewActivity.this.setupFinished) {
                return;
            }
            super.setTop(i, i2, i3, i4, i5, i6, z, z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class BlurButton extends View {
        private final ColorFilter colorFilter;
        private final Paint dimPaint;
        private final Paint dimPaint2;
        private CircularProgressDrawable loadingDrawable;
        private float loadingT;
        private final Drawable rippleDrawable;
        private Text subtext;
        private boolean subtextShown;
        private AnimatedFloat subtextShownT;
        private Text text;

        public BlurButton(Context context) {
            super(context);
            this.subtextShownT = new AnimatedFloat(this, 0L, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
            Drawable createRadSelectorDrawable = Theme.createRadSelectorDrawable(285212671, 8, 8);
            this.rippleDrawable = createRadSelectorDrawable;
            this.dimPaint = new Paint(1);
            this.dimPaint2 = new Paint(1);
            this.loadingT = 0.0f;
            createRadSelectorDrawable.setCallback(this);
            ColorMatrix colorMatrix = new ColorMatrix();
            AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, 0.35f);
            AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, 0.9f);
            this.colorFilter = new ColorMatrixColorFilter(colorMatrix);
        }

        public void setText(CharSequence charSequence) {
            this.text = new Text(charSequence, 14.0f, AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        }

        public void setSubText(CharSequence charSequence, boolean z) {
            if (charSequence != null) {
                this.subtext = new Text(charSequence, 12.0f);
            }
            boolean z2 = charSequence != null;
            this.subtextShown = z2;
            if (!z) {
                this.subtextShownT.set(z2, true);
            }
            invalidate();
        }

        public CharSequence getText() {
            Text text = this.text;
            if (text != null) {
                return text.getText();
            }
            return null;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            Text text;
            float dp = AndroidUtilities.dp(8.0f);
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, 0.0f, getWidth(), getHeight());
            Theme.applyServiceShaderMatrixForView(this, ThemePreviewActivity.this.backgroundImage, ThemePreviewActivity.this.themeDelegate);
            Paint paint = ThemePreviewActivity.this.themeDelegate.getPaint("paintChatActionBackground");
            ColorFilter colorFilter = paint.getColorFilter();
            paint.setColorFilter(this.colorFilter);
            canvas.drawRoundRect(rectF, dp, dp, paint);
            paint.setColorFilter(colorFilter);
            if (ThemePreviewActivity.this.shouldShowBrightnessControll && ThemePreviewActivity.this.dimAmount > 0.0f) {
                this.dimPaint2.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (ThemePreviewActivity.this.dimAmount * 255.0f * ThemePreviewActivity.this.progressToDarkTheme)));
                canvas.drawRoundRect(rectF, dp, dp, this.dimPaint2);
            }
            this.dimPaint.setColor(520093695);
            canvas.drawRoundRect(rectF, dp, dp, this.dimPaint);
            if (this.loadingT > 0.0f) {
                if (this.loadingDrawable == null) {
                    this.loadingDrawable = new CircularProgressDrawable(-1);
                }
                int dp2 = (int) ((1.0f - this.loadingT) * AndroidUtilities.dp(-24.0f));
                this.loadingDrawable.setBounds(0, dp2, getWidth(), getHeight() + dp2);
                this.loadingDrawable.setAlpha((int) (this.loadingT * 255.0f));
                this.loadingDrawable.draw(canvas);
                invalidate();
            }
            float f = this.subtextShownT.set(this.subtextShown);
            if (this.loadingT < 1.0f && (text = this.text) != null) {
                text.ellipsize(getWidth() - AndroidUtilities.dp(14.0f)).draw(canvas, (getWidth() - this.text.getWidth()) / 2.0f, ((getHeight() / 2.0f) + (this.loadingT * AndroidUtilities.dp(24.0f))) - (AndroidUtilities.dp(7.0f) * f), -1, 1.0f - this.loadingT);
            }
            if (this.loadingT < 1.0f && this.subtext != null) {
                canvas.save();
                canvas.scale(f, f, getWidth() / 2.0f, (getHeight() / 2.0f) + AndroidUtilities.dp(11.0f));
                this.subtext.ellipsize(getWidth() - AndroidUtilities.dp(14.0f)).draw(canvas, (getWidth() - this.subtext.getWidth()) / 2.0f, (getHeight() / 2.0f) + (this.loadingT * AndroidUtilities.dp(24.0f)) + AndroidUtilities.dp(11.0f), Theme.multAlpha(-1, 0.75f), 1.0f - this.loadingT);
                canvas.restore();
            }
            this.rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
            this.rippleDrawable.draw(canvas);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean z;
            if (motionEvent.getAction() == 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    this.rippleDrawable.setHotspot(motionEvent.getX(), motionEvent.getY());
                }
                this.rippleDrawable.setState(new int[]{16842910, 16842919});
                z = true;
            } else {
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    this.rippleDrawable.setState(StateSet.NOTHING);
                }
                z = false;
            }
            return super.onTouchEvent(motionEvent) || z;
        }

        @Override // android.view.View
        protected boolean verifyDrawable(Drawable drawable) {
            return drawable == this.rippleDrawable || super.verifyDrawable(drawable);
        }
    }

    /* loaded from: classes3.dex */
    public class ThemeDelegate implements Theme.ResourcesProvider {
        public final Paint chat_actionBackgroundGradientDarkenPaint;
        public final TextPaint chat_actionTextPaint;
        public final TextPaint chat_actionTextPaint2;
        public final TextPaint chat_botButtonPaint;
        public Theme.ResourcesProvider parentProvider;
        private Bitmap serviceBitmap;
        private Matrix serviceBitmapMatrix;
        public BitmapShader serviceBitmapShader;
        private final SparseIntArray currentColors = new SparseIntArray();
        public final Paint chat_actionBackgroundPaint = new Paint(3);
        public final Paint chat_actionBackgroundSelectedPaint = new Paint(3);

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ ColorFilter getAnimatedEmojiColorFilter() {
            ColorFilter colorFilter;
            colorFilter = Theme.chat_animatedEmojiTextColorFilter;
            return colorFilter;
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ int getColorOrDefault(int i) {
            int color;
            color = getColor(i);
            return color;
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ void setAnimatedColor(int i, int i2) {
            Theme.ResourcesProvider.-CC.$default$setAnimatedColor(this, i, i2);
        }

        public ThemeDelegate() {
            Paint paint = new Paint(3);
            this.chat_actionBackgroundGradientDarkenPaint = paint;
            TextPaint textPaint = new TextPaint();
            this.chat_actionTextPaint = textPaint;
            TextPaint textPaint2 = new TextPaint();
            this.chat_actionTextPaint2 = textPaint2;
            TextPaint textPaint3 = new TextPaint();
            this.chat_botButtonPaint = textPaint3;
            textPaint.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize) - 2));
            textPaint2.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize) - 2));
            textPaint3.setTextSize(AndroidUtilities.dp(15.0f));
            textPaint3.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            paint.setColor(352321536);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public int getColor(int i) {
            Theme.ResourcesProvider resourcesProvider = this.parentProvider;
            if (resourcesProvider != null) {
                return resourcesProvider.getColor(i);
            }
            return Theme.getColor(i);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public Drawable getDrawable(String str) {
            Theme.ResourcesProvider resourcesProvider = this.parentProvider;
            if (resourcesProvider != null) {
                return resourcesProvider.getDrawable(str);
            }
            return Theme.getThemeDrawable(str);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public int getCurrentColor(int i) {
            int color;
            Theme.ResourcesProvider resourcesProvider = this.parentProvider;
            if (resourcesProvider == null) {
                color = getColor(i);
                return color;
            }
            return resourcesProvider.getCurrentColor(i);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public Paint getPaint(String str) {
            Paint themePaint;
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1490966183:
                    if (str.equals("paintChatActionText2")) {
                        c = 0;
                        break;
                    }
                    break;
                case 561929466:
                    if (str.equals("paintChatActionBackground")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1712385955:
                    if (str.equals("paintChatBotButton")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1790254137:
                    if (str.equals("paintChatActionBackgroundDarken")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1897339317:
                    if (str.equals("paintChatActionBackgroundSelected")) {
                        c = 4;
                        break;
                    }
                    break;
                case 2030114297:
                    if (str.equals("paintChatActionText")) {
                        c = 5;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return this.chat_actionTextPaint2;
                case 1:
                    return this.chat_actionBackgroundPaint;
                case 2:
                    return this.chat_botButtonPaint;
                case 3:
                    return this.chat_actionBackgroundGradientDarkenPaint;
                case 4:
                    return this.chat_actionBackgroundSelectedPaint;
                case 5:
                    return this.chat_actionTextPaint;
                default:
                    Theme.ResourcesProvider resourcesProvider = this.parentProvider;
                    if (resourcesProvider == null) {
                        themePaint = Theme.getThemePaint(str);
                        return themePaint;
                    }
                    return resourcesProvider.getPaint(str);
            }
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public boolean hasGradientService() {
            Theme.ResourcesProvider resourcesProvider = this.parentProvider;
            if (resourcesProvider != null) {
                return resourcesProvider.hasGradientService();
            }
            return Theme.hasGradientService();
        }

        public void applyChatServiceMessageColor() {
            applyChatServiceMessageColor(null, null, null, null);
        }

        public void applyChatServiceMessageColor(int[] iArr, Drawable drawable, Drawable drawable2, Float f) {
            Bitmap bitmap;
            int i = Theme.key_chat_serviceBackground;
            int color = getColor(i);
            int color2 = getColor(Theme.key_chat_serviceBackgroundSelected);
            if (drawable == null) {
                drawable = drawable2;
            }
            boolean z = drawable instanceof MotionBackgroundDrawable;
            if ((z || (drawable instanceof BitmapDrawable)) && SharedConfig.getDevicePerformanceClass() != 0 && LiteMode.isEnabled(32)) {
                if (z) {
                    bitmap = ((MotionBackgroundDrawable) drawable).getBitmap();
                } else {
                    bitmap = drawable instanceof BitmapDrawable ? ((BitmapDrawable) drawable).getBitmap() : null;
                }
                if (this.serviceBitmap != bitmap) {
                    this.serviceBitmap = bitmap;
                    Bitmap bitmap2 = this.serviceBitmap;
                    Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                    this.serviceBitmapShader = new BitmapShader(bitmap2, tileMode, tileMode);
                    if (this.serviceBitmapMatrix == null) {
                        this.serviceBitmapMatrix = new Matrix();
                    }
                }
                this.chat_actionTextPaint.setColor(-1);
                this.chat_actionTextPaint2.setColor(-1);
                this.chat_actionTextPaint.linkColor = -1;
                this.chat_botButtonPaint.setColor(-1);
            } else {
                this.serviceBitmap = null;
                this.serviceBitmapShader = null;
                TextPaint textPaint = this.chat_actionTextPaint;
                int i2 = Theme.key_chat_serviceText;
                textPaint.setColor(getColor(i2));
                this.chat_actionTextPaint2.setColor(getColor(i2));
                this.chat_actionTextPaint.linkColor = getColor(Theme.key_chat_serviceLink);
            }
            this.chat_actionBackgroundPaint.setColor(color);
            this.chat_actionBackgroundSelectedPaint.setColor(color2);
            if (this.serviceBitmapShader != null && (this.currentColors.indexOfKey(i) < 0 || z || (drawable instanceof BitmapDrawable))) {
                ColorMatrix colorMatrix = new ColorMatrix();
                if (z) {
                    if (((MotionBackgroundDrawable) drawable).getIntensity() >= 0.0f) {
                        colorMatrix.setSaturation(1.6f);
                        AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, isDark() ? 0.97f : 0.92f);
                        AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, isDark() ? 0.12f : -0.06f);
                    } else {
                        colorMatrix.setSaturation(1.1f);
                        AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, isDark() ? 0.4f : 0.8f);
                        AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, isDark() ? 0.08f : -0.06f);
                    }
                } else {
                    colorMatrix.setSaturation(1.6f);
                    AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, isDark() ? 0.9f : 0.84f);
                    AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, isDark() ? -0.04f : 0.06f);
                }
                if (z) {
                    float intensity = ((MotionBackgroundDrawable) drawable).getIntensity();
                    if (f != null) {
                        intensity = f.floatValue();
                    }
                    if (intensity >= 0.0f) {
                        colorMatrix.setSaturation(1.8f);
                        AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, 0.97f);
                        AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, 0.03f);
                    } else {
                        colorMatrix.setSaturation(0.5f);
                        AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, 0.35f);
                        AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, 0.03f);
                    }
                } else {
                    colorMatrix.setSaturation(1.6f);
                    AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, 0.97f);
                    AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, 0.06f);
                }
                this.chat_actionBackgroundPaint.setShader(this.serviceBitmapShader);
                this.chat_actionBackgroundPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
                this.chat_actionBackgroundPaint.setAlpha(255);
                this.chat_actionBackgroundSelectedPaint.setShader(this.serviceBitmapShader);
                ColorMatrix colorMatrix2 = new ColorMatrix(colorMatrix);
                AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix2, 0.85f);
                this.chat_actionBackgroundSelectedPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix2));
                this.chat_actionBackgroundSelectedPaint.setAlpha(255);
                return;
            }
            this.chat_actionBackgroundPaint.setColorFilter(null);
            this.chat_actionBackgroundPaint.setShader(null);
            this.chat_actionBackgroundSelectedPaint.setColorFilter(null);
            this.chat_actionBackgroundSelectedPaint.setShader(null);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public void applyServiceShaderMatrix(int i, int i2, float f, float f2) {
            BitmapShader bitmapShader;
            Bitmap bitmap = this.serviceBitmap;
            if (bitmap == null || (bitmapShader = this.serviceBitmapShader) == null) {
                Theme.applyServiceShaderMatrix(i, i2, f, f2);
            } else {
                Theme.applyServiceShaderMatrix(bitmap, bitmapShader, this.serviceBitmapMatrix, i, i2, f, f2);
            }
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public boolean isDark() {
            DayNightSwitchDelegate dayNightSwitchDelegate = ThemePreviewActivity.this.onSwitchDayNightDelegate;
            if (dayNightSwitchDelegate != null) {
                return dayNightSwitchDelegate.isDark();
            }
            Theme.ResourcesProvider resourcesProvider = this.parentProvider;
            return resourcesProvider != null ? resourcesProvider.isDark() : Theme.isCurrentThemeDark();
        }
    }

    @SuppressLint({"NotifyDataSetChanged"})
    public void toggleTheme() {
        if (this.changeDayNightView != null) {
            return;
        }
        FrameLayout frameLayout = (FrameLayout) (insideBottomSheet() ? this.parentLayout.getBottomSheet().getWindow() : getParentActivity().getWindow()).getDecorView();
        final Bitmap createBitmap = Bitmap.createBitmap(frameLayout.getWidth(), frameLayout.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(createBitmap);
        this.dayNightItem.setAlpha(0.0f);
        frameLayout.draw(canvas);
        this.dayNightItem.setAlpha(1.0f);
        final Paint paint = new Paint(1);
        paint.setColor(-16777216);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        final Paint paint2 = new Paint(1);
        paint2.setFilterBitmap(true);
        int[] iArr = new int[2];
        this.dayNightItem.getLocationInWindow(iArr);
        final float f = iArr[0];
        final float f2 = iArr[1];
        final float measuredWidth = f + (this.dayNightItem.getMeasuredWidth() / 2.0f);
        final float measuredHeight = f2 + (this.dayNightItem.getMeasuredHeight() / 2.0f);
        final float max = Math.max(createBitmap.getHeight(), createBitmap.getWidth()) + AndroidUtilities.navigationBarHeight;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        paint2.setShader(new BitmapShader(createBitmap, tileMode, tileMode));
        View view = new View(getContext()) { // from class: org.telegram.ui.ThemePreviewActivity.41
            @Override // android.view.View
            protected void onDraw(Canvas canvas2) {
                super.onDraw(canvas2);
                if (ThemePreviewActivity.this.themeDelegate.isDark()) {
                    if (ThemePreviewActivity.this.changeDayNightViewProgress > 0.0f) {
                        canvas.drawCircle(measuredWidth, measuredHeight, max * ThemePreviewActivity.this.changeDayNightViewProgress, paint);
                    }
                    canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, paint2);
                } else {
                    canvas2.drawCircle(measuredWidth, measuredHeight, max * (1.0f - ThemePreviewActivity.this.changeDayNightViewProgress), paint2);
                }
                canvas2.save();
                canvas2.translate(f, f2);
                ThemePreviewActivity.this.dayNightItem.draw(canvas2);
                canvas2.restore();
            }
        };
        this.changeDayNightView = view;
        view.setOnTouchListener(ThemePreviewActivity$$ExternalSyntheticLambda15.INSTANCE);
        this.changeDayNightViewProgress = 0.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.changeDayNightViewAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ThemePreviewActivity.42
            boolean changedNavigationBarColor = false;

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ThemePreviewActivity.this.changeDayNightViewProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                ThemePreviewActivity.this.changeDayNightView.invalidate();
                if (this.changedNavigationBarColor || ThemePreviewActivity.this.changeDayNightViewProgress <= 0.5f) {
                    return;
                }
                this.changedNavigationBarColor = true;
            }
        });
        this.changeDayNightViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.43
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (ThemePreviewActivity.this.changeDayNightView != null) {
                    if (ThemePreviewActivity.this.changeDayNightView.getParent() != null) {
                        ((ViewGroup) ThemePreviewActivity.this.changeDayNightView.getParent()).removeView(ThemePreviewActivity.this.changeDayNightView);
                    }
                    ThemePreviewActivity.this.changeDayNightView = null;
                }
                ThemePreviewActivity.this.changeDayNightViewAnimator = null;
                super.onAnimationEnd(animator);
            }
        });
        this.changeDayNightViewAnimator.setDuration(400L);
        this.changeDayNightViewAnimator.setInterpolator(Easings.easeInOutQuad);
        this.changeDayNightViewAnimator.start();
        frameLayout.addView(this.changeDayNightView, new ViewGroup.LayoutParams(-1, -1));
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                ThemePreviewActivity.this.lambda$toggleTheme$36();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleTheme$36() {
        this.onSwitchDayNightDelegate.switchDayNight(false);
        setForceDark(this.themeDelegate.isDark(), true);
        setCurrentImage(false);
        invalidateBlur();
        updateBlurred();
        if (this.themeDescriptions != null) {
            for (int i = 0; i < this.themeDescriptions.size(); i++) {
                this.themeDescriptions.get(i).setColor(getThemedColor(this.themeDescriptions.get(i).getCurrentKey()), false, false);
            }
        }
        if (this.shouldShowBrightnessControll) {
            DayNightSwitchDelegate dayNightSwitchDelegate = this.onSwitchDayNightDelegate;
            if (dayNightSwitchDelegate != null && dayNightSwitchDelegate.isDark()) {
                this.dimmingSlider.setVisibility(0);
                this.dimmingSlider.animateValueTo(this.dimAmount);
            } else {
                this.dimmingSlider.animateValueTo(0.0f);
            }
            ValueAnimator valueAnimator = this.changeDayNightViewAnimator2;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.changeDayNightViewAnimator2.cancel();
            }
            float[] fArr = new float[2];
            fArr[0] = this.progressToDarkTheme;
            fArr[1] = this.onSwitchDayNightDelegate.isDark() ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.changeDayNightViewAnimator2 = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ThemePreviewActivity.this.lambda$toggleTheme$35(valueAnimator2);
                }
            });
            this.changeDayNightViewAnimator2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.44
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (ThemePreviewActivity.this.onSwitchDayNightDelegate.isDark()) {
                        return;
                    }
                    ThemePreviewActivity.this.dimmingSlider.setVisibility(8);
                }
            });
            this.changeDayNightViewAnimator2.setDuration(250L);
            this.changeDayNightViewAnimator2.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.changeDayNightViewAnimator2.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleTheme$35(ValueAnimator valueAnimator) {
        this.progressToDarkTheme = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.backgroundImage.invalidate();
        this.bottomOverlayChat.invalidate();
        this.dimmingSlider.setAlpha(this.progressToDarkTheme);
        this.dimmingSliderContainer.invalidate();
        invalidateBlur();
    }

    public void setForceDark(boolean z, boolean z2) {
        if (z2) {
            RLottieDrawable rLottieDrawable = this.sunDrawable;
            rLottieDrawable.setCustomEndFrame(z ? rLottieDrawable.getFramesCount() : 0);
            RLottieDrawable rLottieDrawable2 = this.sunDrawable;
            if (rLottieDrawable2 != null) {
                rLottieDrawable2.start();
                return;
            }
            return;
        }
        int framesCount = z ? this.sunDrawable.getFramesCount() - 1 : 0;
        this.sunDrawable.setCurrentFrame(framesCount, false, true);
        this.sunDrawable.setCustomEndFrame(framesCount);
        ActionBarMenuItem actionBarMenuItem = this.dayNightItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.invalidate();
        }
    }
}
