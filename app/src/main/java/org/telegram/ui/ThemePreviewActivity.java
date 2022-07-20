package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
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
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$KeyboardButton;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_account_getWallPaper;
import org.telegram.tgnet.TLRPC$TL_account_getWallPapers;
import org.telegram.tgnet.TLRPC$TL_account_wallPapers;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC$TL_inputWallPaperSlug;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageEntityTextUrl;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageReplyHeader;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_photo;
import org.telegram.tgnet.TLRPC$TL_photoSize;
import org.telegram.tgnet.TLRPC$TL_premiumGiftOption;
import org.telegram.tgnet.TLRPC$TL_reactionCount;
import org.telegram.tgnet.TLRPC$TL_replyInlineMarkup;
import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.tgnet.TLRPC$TL_user;
import org.telegram.tgnet.TLRPC$TL_wallPaper;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$WallPaper;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
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
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BackgroundGradientDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ColorPicker;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.HintView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.RadialProgress2;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.Components.WallpaperCheckBoxView;
import org.telegram.ui.Components.WallpaperParallaxEffect;
import org.telegram.ui.WallpapersListActivity;
/* loaded from: classes3.dex */
public class ThemePreviewActivity extends BaseFragment implements DownloadController.FileDownloadProgressListener, NotificationCenter.NotificationCenterDelegate {
    private int TAG;
    private Theme.ThemeAccent accent;
    private ActionBar actionBar2;
    private HintView animationHint;
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
    private BackupImageView backgroundImage;
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
    private FrameLayout bottomOverlayChat;
    private TextView bottomOverlayChatText;
    private TextView cancelButton;
    private int checkColor;
    private ColorPicker colorPicker;
    private int colorType;
    private float currentIntensity;
    private Object currentWallpaper;
    private Bitmap currentWallpaperBitmap;
    private WallpaperActivityDelegate delegate;
    private boolean deleteOnCancel;
    private DialogsAdapter dialogsAdapter;
    private TextView doneButton;
    private View dotsContainer;
    private TextView dropDown;
    private ActionBarMenuItem dropDownContainer;
    private boolean editingTheme;
    private ImageView floatingButton;
    private FrameLayout frameLayout;
    private String imageFilter;
    private HeaderCell intensityCell;
    private SeekBarView intensitySeekBar;
    private boolean isBlurred;
    private boolean isMotion;
    private int lastPickedColor;
    private int lastPickedColorNum;
    private TLRPC$TL_wallPaper lastSelectedPattern;
    private RecyclerListView listView;
    private RecyclerListView listView2;
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
    private Bitmap originalBitmap;
    private FrameLayout page1;
    private FrameLayout page2;
    private WallpaperParallaxEffect parallaxEffect;
    private float parallaxScale;
    private int patternColor;
    private FrameLayout[] patternLayout;
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
    private boolean progressVisible;
    private RadialProgress2 radialProgress;
    private boolean removeBackgroundOverride;
    private boolean rotatePreview;
    private FrameLayout saveButtonsContainer;
    private ActionBarMenuItem saveItem;
    private final int screenType;
    private TLRPC$TL_wallPaper selectedPattern;
    private Drawable sheetDrawable;
    private boolean showColor;
    private List<ThemeDescription> themeDescriptions;
    private UndoView undoView;
    public boolean useDefaultThemeForButtons;
    private ViewPager viewPager;
    private boolean wasScroll;
    private long watchForKeyboardEndTime;

    /* loaded from: classes3.dex */
    public interface WallpaperActivityDelegate {
        void didSetNewBackground();
    }

    public static /* synthetic */ void lambda$createView$1(View view, int i) {
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return false;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
    }

    public /* synthetic */ void lambda$new$0() {
        this.applyColorScheduled = false;
        applyColor(this.lastPickedColor, this.lastPickedColorNum);
        this.lastPickedColorNum = -1;
    }

    public ThemePreviewActivity(Object obj, Bitmap bitmap) {
        this(obj, bitmap, false, false);
    }

    public ThemePreviewActivity(Object obj, Bitmap bitmap, boolean z, boolean z2) {
        this.useDefaultThemeForButtons = true;
        this.colorType = 1;
        this.msgOutDrawable = new Theme.MessageDrawable(0, true, false);
        this.msgOutDrawableSelected = new Theme.MessageDrawable(0, true, true);
        this.msgOutMediaDrawable = new Theme.MessageDrawable(1, true, false);
        this.msgOutMediaDrawableSelected = new Theme.MessageDrawable(1, true, true);
        this.lastPickedColorNum = -1;
        this.applyColorAction = new ThemePreviewActivity$$ExternalSyntheticLambda14(this);
        this.patternLayout = new FrameLayout[2];
        this.patternsCancelButton = new TextView[2];
        this.patternsSaveButton = new TextView[2];
        this.patternsButtonsContainer = new FrameLayout[2];
        this.patternsDict = new HashMap<>();
        this.currentIntensity = 0.5f;
        this.blendMode = PorterDuff.Mode.SRC_IN;
        this.parallaxScale = 1.0f;
        this.imageFilter = "640_360";
        this.maxWallpaperSize = 1920;
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
        this.useDefaultThemeForButtons = true;
        this.colorType = 1;
        this.msgOutDrawable = new Theme.MessageDrawable(0, true, false);
        this.msgOutDrawableSelected = new Theme.MessageDrawable(0, true, true);
        this.msgOutMediaDrawable = new Theme.MessageDrawable(1, true, false);
        this.msgOutMediaDrawableSelected = new Theme.MessageDrawable(1, true, true);
        this.lastPickedColorNum = -1;
        this.applyColorAction = new ThemePreviewActivity$$ExternalSyntheticLambda14(this);
        this.patternLayout = new FrameLayout[2];
        this.patternsCancelButton = new TextView[2];
        this.patternsSaveButton = new TextView[2];
        this.patternsButtonsContainer = new FrameLayout[2];
        this.patternsDict = new HashMap<>();
        this.currentIntensity = 0.5f;
        this.blendMode = PorterDuff.Mode.SRC_IN;
        this.parallaxScale = 1.0f;
        this.imageFilter = "640_360";
        this.maxWallpaperSize = 1920;
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
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.goingToPreviewTheme, new Object[0]);
        this.msgOutDrawable.themePreview = true;
        this.msgOutMediaDrawable.themePreview = true;
        this.msgOutDrawableSelected.themePreview = true;
        this.msgOutMediaDrawableSelected.themePreview = true;
    }

    public void setInitialModes(boolean z, boolean z2) {
        this.isBlurred = z;
        this.isMotion = z2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:56:0x02a5, code lost:
        if ("d".equals(((org.telegram.ui.WallpapersListActivity.ColorWallpaper) r0).slug) == false) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x02ab, code lost:
        if ((r37.currentWallpaper instanceof org.telegram.tgnet.TLRPC$TL_wallPaper) != false) goto L59;
     */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0605  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x06f8  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x06fe  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x07eb  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x0923  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x092f  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x0c7e  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x0c89  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    @SuppressLint({"Recycle"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        int i;
        String str;
        int i2;
        int i3;
        int i4;
        int i5;
        FrameLayout.LayoutParams layoutParams;
        Theme.ThemeAccent themeAccent;
        int i6;
        this.hasOwnBackground = true;
        if (AndroidUtilities.isTablet()) {
            this.actionBar.setOccupyStatusBar(false);
        }
        this.page1 = new FrameLayout(context);
        this.actionBar.createMenu().addItem(0, 2131165456).setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass1(this)).setSearchFieldHint(LocaleController.getString("Search", 2131628154));
        this.actionBar.setBackButtonDrawable(new MenuDrawable());
        this.actionBar.setAddToContainer(false);
        this.actionBar.setTitle(LocaleController.getString("ThemePreview", 2131628688));
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.page1 = anonymousClass2;
        anonymousClass2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        this.page1.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        this.listView = recyclerListView;
        recyclerListView.setVerticalScrollBarEnabled(true);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(this.screenType != 0 ? 12.0f : 0.0f));
        this.listView.setOnItemClickListener(ThemePreviewActivity$$ExternalSyntheticLambda24.INSTANCE);
        this.page1.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        ImageView imageView = new ImageView(context);
        this.floatingButton = imageView;
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
        int i7 = Build.VERSION.SDK_INT;
        if (i7 < 21) {
            Drawable mutate = context.getResources().getDrawable(2131165414).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            createSimpleSelectorCircleDrawable = combinedDrawable;
        }
        this.floatingButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable);
        this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), PorterDuff.Mode.MULTIPLY));
        this.floatingButton.setImageResource(2131165413);
        if (i7 >= 21) {
            StateListAnimator stateListAnimator = new StateListAnimator();
            ImageView imageView2 = this.floatingButton;
            Property property = View.TRANSLATION_Z;
            stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(imageView2, property, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButton, property, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
            this.floatingButton.setStateListAnimator(stateListAnimator);
            this.floatingButton.setOutlineProvider(new AnonymousClass3(this));
        }
        FrameLayout frameLayout = this.page1;
        ImageView imageView3 = this.floatingButton;
        int i8 = i7 >= 21 ? 56 : 60;
        float f = i7 >= 21 ? 56.0f : 60.0f;
        boolean z = LocaleController.isRTL;
        frameLayout.addView(imageView3, LayoutHelper.createFrame(i8, f, (z ? 3 : 5) | 80, z ? 14.0f : 0.0f, 0.0f, z ? 0.0f : 14.0f, 14.0f));
        DialogsAdapter dialogsAdapter = new DialogsAdapter(context);
        this.dialogsAdapter = dialogsAdapter;
        this.listView.setAdapter(dialogsAdapter);
        this.page2 = new AnonymousClass4(context);
        this.messagesAdapter = new MessagesAdapter(context);
        this.actionBar2 = createActionBar(context);
        if (AndroidUtilities.isTablet()) {
            this.actionBar2.setOccupyStatusBar(false);
        }
        this.actionBar2.setBackButtonDrawable(new BackDrawable(false));
        this.actionBar2.setActionBarMenuOnItemClick(new AnonymousClass5());
        AnonymousClass6 anonymousClass6 = new AnonymousClass6(context);
        this.backgroundImage = anonymousClass6;
        this.page2.addView(anonymousClass6, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 48.0f));
        if (this.screenType == 2) {
            this.backgroundImage.getImageReceiver().setDelegate(new ThemePreviewActivity$$ExternalSyntheticLambda19(this));
        }
        if (this.messagesAdapter.showSecretMessages) {
            this.actionBar2.setTitle("Telegram Beta Chat");
            this.actionBar2.setSubtitle(LocaleController.formatPluralString("Members", 505, new Object[0]));
        } else {
            int i9 = this.screenType;
            if (i9 == 2) {
                this.actionBar2.setTitle(LocaleController.getString("BackgroundPreview", 2131624662));
                if (!BuildVars.DEBUG_PRIVATE_VERSION || Theme.getActiveTheme().getAccent(false) == null) {
                    Object obj = this.currentWallpaper;
                    if (obj instanceof WallpapersListActivity.ColorWallpaper) {
                    }
                }
                this.actionBar2.createMenu().addItem(5, 2131165940);
            } else if (i9 == 1) {
                ActionBarMenu createMenu = this.actionBar2.createMenu();
                this.saveItem = createMenu.addItem(4, LocaleController.getString("Save", 2131628122).toUpperCase());
                AnonymousClass7 anonymousClass7 = new AnonymousClass7(context, createMenu, 0, 0);
                this.dropDownContainer = anonymousClass7;
                anonymousClass7.setSubMenuOpenSide(1);
                this.dropDownContainer.addSubItem(2, LocaleController.getString("ColorPickerBackground", 2131625193));
                this.dropDownContainer.addSubItem(1, LocaleController.getString("ColorPickerMainColor", 2131625194));
                this.dropDownContainer.addSubItem(3, LocaleController.getString("ColorPickerMyMessages", 2131625195));
                this.dropDownContainer.setAllowCloseAnimation(false);
                this.dropDownContainer.setForceSmoothKeyboard(true);
                this.actionBar2.addView(this.dropDownContainer, LayoutHelper.createFrame(-2, -1.0f, 51, AndroidUtilities.isTablet() ? 64.0f : 56.0f, 0.0f, 40.0f, 0.0f));
                this.dropDownContainer.setOnClickListener(new ThemePreviewActivity$$ExternalSyntheticLambda8(this));
                TextView textView = new TextView(context);
                this.dropDown = textView;
                textView.setImportantForAccessibility(2);
                this.dropDown.setGravity(3);
                this.dropDown.setSingleLine(true);
                this.dropDown.setLines(1);
                this.dropDown.setMaxLines(1);
                this.dropDown.setEllipsize(TextUtils.TruncateAt.END);
                this.dropDown.setTextColor(Theme.getColor("actionBarDefaultTitle"));
                this.dropDown.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                this.dropDown.setText(LocaleController.getString("ColorPickerMainColor", 2131625194));
                Drawable mutate2 = context.getResources().getDrawable(2131165463).mutate();
                mutate2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("actionBarDefaultTitle"), PorterDuff.Mode.MULTIPLY));
                this.dropDown.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, mutate2, (Drawable) null);
                this.dropDown.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
                this.dropDown.setPadding(0, 0, AndroidUtilities.dp(10.0f), 0);
                this.dropDownContainer.addView(this.dropDown, LayoutHelper.createFrame(-2, -2.0f, 16, 16.0f, 0.0f, 0.0f, 1.0f));
            } else {
                Theme.ThemeInfo themeInfo = this.applyingTheme;
                TLRPC$TL_theme tLRPC$TL_theme = themeInfo.info;
                String name = tLRPC$TL_theme != null ? tLRPC$TL_theme.title : themeInfo.getName();
                int lastIndexOf = name.lastIndexOf(".attheme");
                if (lastIndexOf >= 0) {
                    name = name.substring(0, lastIndexOf);
                }
                this.actionBar2.setTitle(name);
                TLRPC$TL_theme tLRPC$TL_theme2 = this.applyingTheme.info;
                if (tLRPC$TL_theme2 != null && (i6 = tLRPC$TL_theme2.installs_count) > 0) {
                    this.actionBar2.setSubtitle(LocaleController.formatPluralString("ThemeInstallCount", i6, new Object[0]));
                } else {
                    this.actionBar2.setSubtitle(LocaleController.formatDateOnline((System.currentTimeMillis() / 1000) - 3600, null));
                }
            }
        }
        this.listView2 = new AnonymousClass8(context);
        AnonymousClass9 anonymousClass9 = new AnonymousClass9();
        anonymousClass9.setDelayAnimations(false);
        this.listView2.setItemAnimator(anonymousClass9);
        this.listView2.setVerticalScrollBarEnabled(true);
        this.listView2.setOverScrollMode(2);
        int i10 = this.screenType;
        if (i10 == 2) {
            this.listView2.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(52.0f));
        } else if (i10 == 1) {
            this.listView2.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(16.0f));
        } else {
            this.listView2.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
        }
        this.listView2.setClipToPadding(false);
        this.listView2.setLayoutManager(new LinearLayoutManager(context, 1, true));
        this.listView2.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        if (this.screenType == 1) {
            this.page2.addView(this.listView2, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 273.0f));
            this.listView2.setOnItemClickListener(new ThemePreviewActivity$$ExternalSyntheticLambda25(this));
            i = -1;
        } else {
            i = -1;
            this.page2.addView(this.listView2, LayoutHelper.createFrame(-1, -1, 51));
        }
        this.listView2.setOnScrollListener(new AnonymousClass10());
        this.page2.addView(this.actionBar2, LayoutHelper.createFrame(i, -2.0f));
        WallpaperParallaxEffect wallpaperParallaxEffect = new WallpaperParallaxEffect(context);
        this.parallaxEffect = wallpaperParallaxEffect;
        wallpaperParallaxEffect.setCallback(new ThemePreviewActivity$$ExternalSyntheticLambda26(this));
        int i11 = this.screenType;
        String str2 = "chat_fieldOverlayText";
        if (i11 == 1 || i11 == 2) {
            RadialProgress2 radialProgress2 = new RadialProgress2(this.backgroundImage);
            this.radialProgress = radialProgress2;
            radialProgress2.setColors("chat_serviceBackground", "chat_serviceBackground", "chat_serviceText", "chat_serviceText");
            if (this.screenType == 2) {
                AnonymousClass11 anonymousClass11 = new AnonymousClass11(this, context);
                this.bottomOverlayChat = anonymousClass11;
                anonymousClass11.setWillNotDraw(false);
                this.bottomOverlayChat.setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
                this.page2.addView(this.bottomOverlayChat, LayoutHelper.createFrame(-1, 51, 80));
                this.bottomOverlayChat.setOnClickListener(new ThemePreviewActivity$$ExternalSyntheticLambda7(this));
                TextView textView2 = new TextView(context);
                this.bottomOverlayChatText = textView2;
                textView2.setTextSize(1, 15.0f);
                this.bottomOverlayChatText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                this.bottomOverlayChatText.setTextColor(Theme.getColor(str2));
                this.bottomOverlayChatText.setText(LocaleController.getString("SetBackground", 2131628303));
                this.bottomOverlayChat.addView(this.bottomOverlayChatText, LayoutHelper.createFrame(-2, -2, 17));
            }
            Rect rect = new Rect();
            Drawable mutate3 = context.getResources().getDrawable(2131166140).mutate();
            this.sheetDrawable = mutate3;
            mutate3.getPadding(rect);
            this.sheetDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhite"), PorterDuff.Mode.MULTIPLY));
            TextPaint textPaint = new TextPaint(1);
            textPaint.setTextSize(AndroidUtilities.dp(14.0f));
            textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            if (this.screenType != 1) {
                Object obj2 = this.currentWallpaper;
                if (!(obj2 instanceof WallpapersListActivity.ColorWallpaper)) {
                    if (!(obj2 instanceof WallpapersListActivity.FileWallpaper) || !"t".equals(((WallpapersListActivity.FileWallpaper) obj2).slug)) {
                        i2 = 2;
                        String[] strArr = new String[i2];
                        int[] iArr = new int[i2];
                        this.backgroundCheckBoxView = new WallpaperCheckBoxView[i2];
                        if (i2 == 0) {
                            this.backgroundButtonsContainer = new FrameLayout(context);
                            if (this.screenType == 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                                strArr[0] = LocaleController.getString("BackgroundColors", 2131624657);
                                strArr[1] = LocaleController.getString("BackgroundPattern", 2131624661);
                                strArr[2] = LocaleController.getString("BackgroundMotion", 2131624660);
                            } else {
                                strArr[0] = LocaleController.getString("BackgroundBlurred", 2131624650);
                                strArr[1] = LocaleController.getString("BackgroundMotion", 2131624660);
                            }
                            int i12 = 0;
                            i3 = 0;
                            while (i12 < i2) {
                                iArr[i12] = (int) Math.ceil(textPaint.measureText(strArr[i12]));
                                i3 = Math.max(i3, iArr[i12]);
                                i12++;
                                str2 = str2;
                            }
                            str = str2;
                            AnonymousClass12 anonymousClass12 = new AnonymousClass12(context);
                            this.backgroundPlayAnimationView = anonymousClass12;
                            anonymousClass12.setWillNotDraw(false);
                            this.backgroundPlayAnimationView.setVisibility(this.backgroundGradientColor1 != 0 ? 0 : 4);
                            this.backgroundPlayAnimationView.setScaleX(this.backgroundGradientColor1 != 0 ? 1.0f : 0.1f);
                            this.backgroundPlayAnimationView.setScaleY(this.backgroundGradientColor1 != 0 ? 1.0f : 0.1f);
                            this.backgroundPlayAnimationView.setAlpha(this.backgroundGradientColor1 != 0 ? 1.0f : 0.0f);
                            this.backgroundPlayAnimationView.setTag(this.backgroundGradientColor1 != 0 ? 1 : null);
                            this.backgroundButtonsContainer.addView(this.backgroundPlayAnimationView, LayoutHelper.createFrame(48, 48, 17));
                            this.backgroundPlayAnimationView.setOnClickListener(new AnonymousClass13());
                            ImageView imageView4 = new ImageView(context);
                            this.backgroundPlayAnimationImageView = imageView4;
                            imageView4.setScaleType(ImageView.ScaleType.CENTER);
                            this.backgroundPlayAnimationImageView.setImageResource(2131165272);
                            this.backgroundPlayAnimationView.addView(this.backgroundPlayAnimationImageView, LayoutHelper.createFrame(-2, -2, 17));
                        } else {
                            str = str2;
                            i3 = 0;
                        }
                        i4 = 0;
                        while (i4 < i2) {
                            this.backgroundCheckBoxView[i4] = new WallpaperCheckBoxView(context, (this.screenType != 1 && !(this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) || i4 != 0, this.backgroundImage);
                            this.backgroundCheckBoxView[i4].setBackgroundColor(this.backgroundColor);
                            this.backgroundCheckBoxView[i4].setText(strArr[i4], iArr[i4], i3);
                            if (this.screenType != 1 && !(this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                                this.backgroundCheckBoxView[i4].setChecked(i4 == 0 ? this.isBlurred : this.isMotion, false);
                            } else if (i4 == 1) {
                                this.backgroundCheckBoxView[i4].setChecked(this.selectedPattern != null || ((themeAccent = this.accent) != null && !TextUtils.isEmpty(themeAccent.patternSlug)), false);
                            } else if (i4 == 2) {
                                this.backgroundCheckBoxView[i4].setChecked(this.isMotion, false);
                            }
                            int dp = AndroidUtilities.dp(56.0f) + i3;
                            FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(dp, -2);
                            layoutParams2.gravity = 17;
                            if (i2 == 3) {
                                if (i4 == 0 || i4 == 2) {
                                    layoutParams2.leftMargin = (dp / 2) + AndroidUtilities.dp(10.0f);
                                } else {
                                    layoutParams2.rightMargin = (dp / 2) + AndroidUtilities.dp(10.0f);
                                }
                            } else if (i4 == 1) {
                                layoutParams2.leftMargin = (dp / 2) + AndroidUtilities.dp(10.0f);
                            } else {
                                layoutParams2.rightMargin = (dp / 2) + AndroidUtilities.dp(10.0f);
                            }
                            this.backgroundButtonsContainer.addView(this.backgroundCheckBoxView[i4], layoutParams2);
                            WallpaperCheckBoxView[] wallpaperCheckBoxViewArr = this.backgroundCheckBoxView;
                            wallpaperCheckBoxViewArr[i4].setOnClickListener(new ThemePreviewActivity$$ExternalSyntheticLambda11(this, i4, wallpaperCheckBoxViewArr[i4]));
                            if (i4 == 2) {
                                this.backgroundCheckBoxView[i4].setAlpha(0.0f);
                                this.backgroundCheckBoxView[i4].setVisibility(4);
                            }
                            i4++;
                        }
                        if (this.screenType == 1) {
                            int[] iArr2 = new int[2];
                            this.messagesCheckBoxView = new WallpaperCheckBoxView[2];
                            this.messagesButtonsContainer = new FrameLayout(context);
                            String[] strArr2 = {LocaleController.getString("BackgroundAnimate", 2131624648), LocaleController.getString("BackgroundColors", 2131624657)};
                            int i13 = 0;
                            for (int i14 = 0; i14 < 2; i14++) {
                                iArr2[i14] = (int) Math.ceil(textPaint.measureText(strArr2[i14]));
                                i13 = Math.max(i13, iArr2[i14]);
                            }
                            if (this.accent != null) {
                                AnonymousClass14 anonymousClass14 = new AnonymousClass14(context);
                                this.messagesPlayAnimationView = anonymousClass14;
                                anonymousClass14.setWillNotDraw(false);
                                this.messagesPlayAnimationView.setVisibility(this.accent.myMessagesGradientAccentColor1 != 0 ? 0 : 4);
                                this.messagesPlayAnimationView.setScaleX(this.accent.myMessagesGradientAccentColor1 != 0 ? 1.0f : 0.1f);
                                this.messagesPlayAnimationView.setScaleY(this.accent.myMessagesGradientAccentColor1 != 0 ? 1.0f : 0.1f);
                                this.messagesPlayAnimationView.setAlpha(this.accent.myMessagesGradientAccentColor1 != 0 ? 1.0f : 0.0f);
                                this.messagesButtonsContainer.addView(this.messagesPlayAnimationView, LayoutHelper.createFrame(48, 48, 17));
                                this.messagesPlayAnimationView.setOnClickListener(new AnonymousClass15());
                                ImageView imageView5 = new ImageView(context);
                                this.messagesPlayAnimationImageView = imageView5;
                                imageView5.setScaleType(ImageView.ScaleType.CENTER);
                                this.messagesPlayAnimationImageView.setImageResource(2131165272);
                                this.messagesPlayAnimationView.addView(this.messagesPlayAnimationImageView, LayoutHelper.createFrame(-2, -2, 17));
                                int i15 = 0;
                                while (i15 < 2) {
                                    this.messagesCheckBoxView[i15] = new WallpaperCheckBoxView(context, i15 == 0, this.backgroundImage);
                                    this.messagesCheckBoxView[i15].setText(strArr2[i15], iArr2[i15], i13);
                                    if (i15 == 0) {
                                        this.messagesCheckBoxView[i15].setChecked(this.accent.myMessagesAnimated, false);
                                    }
                                    int dp2 = AndroidUtilities.dp(56.0f) + i13;
                                    FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(dp2, -2);
                                    layoutParams3.gravity = 17;
                                    if (i15 == 1) {
                                        layoutParams3.leftMargin = (dp2 / 2) + AndroidUtilities.dp(10.0f);
                                    } else {
                                        layoutParams3.rightMargin = (dp2 / 2) + AndroidUtilities.dp(10.0f);
                                    }
                                    this.messagesButtonsContainer.addView(this.messagesCheckBoxView[i15], layoutParams3);
                                    WallpaperCheckBoxView[] wallpaperCheckBoxViewArr2 = this.messagesCheckBoxView;
                                    wallpaperCheckBoxViewArr2[i15].setOnClickListener(new ThemePreviewActivity$$ExternalSyntheticLambda12(this, i15, wallpaperCheckBoxViewArr2[i15]));
                                    i15++;
                                }
                            }
                        }
                        if (this.screenType != 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                            this.isBlurred = false;
                            i5 = 0;
                            while (i5 < 2) {
                                this.patternLayout[i5] = new AnonymousClass16(context, i5, rect);
                                if (i5 == 1 || this.screenType == 2) {
                                    this.patternLayout[i5].setVisibility(4);
                                }
                                this.patternLayout[i5].setWillNotDraw(false);
                                if (this.screenType == 2) {
                                    layoutParams = LayoutHelper.createFrame(-1, i5 == 0 ? 321 : 316, 83);
                                } else {
                                    layoutParams = LayoutHelper.createFrame(-1, i5 == 0 ? 273 : 316, 83);
                                }
                                if (i5 == 0) {
                                    layoutParams.height += AndroidUtilities.dp(12.0f) + rect.top;
                                    this.patternLayout[i5].setPadding(0, AndroidUtilities.dp(12.0f) + rect.top, 0, 0);
                                }
                                this.page2.addView(this.patternLayout[i5], layoutParams);
                                if (i5 == 1 || this.screenType == 2) {
                                    this.patternsButtonsContainer[i5] = new AnonymousClass17(this, context);
                                    this.patternsButtonsContainer[i5].setWillNotDraw(false);
                                    this.patternsButtonsContainer[i5].setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
                                    this.patternsButtonsContainer[i5].setClickable(true);
                                    this.patternLayout[i5].addView(this.patternsButtonsContainer[i5], LayoutHelper.createFrame(-1, 51, 80));
                                    this.patternsCancelButton[i5] = new TextView(context);
                                    this.patternsCancelButton[i5].setTextSize(1, 15.0f);
                                    this.patternsCancelButton[i5].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                                    this.patternsCancelButton[i5].setTextColor(Theme.getColor(str));
                                    this.patternsCancelButton[i5].setText(LocaleController.getString("Cancel", 2131624832).toUpperCase());
                                    this.patternsCancelButton[i5].setGravity(17);
                                    this.patternsCancelButton[i5].setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
                                    this.patternsCancelButton[i5].setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 0));
                                    this.patternsButtonsContainer[i5].addView(this.patternsCancelButton[i5], LayoutHelper.createFrame(-2, -1, 51));
                                    this.patternsCancelButton[i5].setOnClickListener(new ThemePreviewActivity$$ExternalSyntheticLambda10(this, i5));
                                    this.patternsSaveButton[i5] = new TextView(context);
                                    this.patternsSaveButton[i5].setTextSize(1, 15.0f);
                                    this.patternsSaveButton[i5].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                                    this.patternsSaveButton[i5].setTextColor(Theme.getColor(str));
                                    this.patternsSaveButton[i5].setText(LocaleController.getString("ApplyTheme", 2131624396).toUpperCase());
                                    this.patternsSaveButton[i5].setGravity(17);
                                    this.patternsSaveButton[i5].setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
                                    this.patternsSaveButton[i5].setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21"), 0));
                                    this.patternsButtonsContainer[i5].addView(this.patternsSaveButton[i5], LayoutHelper.createFrame(-2, -1, 53));
                                    this.patternsSaveButton[i5].setOnClickListener(new ThemePreviewActivity$$ExternalSyntheticLambda9(this, i5));
                                }
                                if (i5 == 1) {
                                    TextView textView3 = new TextView(context);
                                    textView3.setLines(1);
                                    textView3.setSingleLine(true);
                                    textView3.setText(LocaleController.getString("BackgroundChoosePattern", 2131624652));
                                    textView3.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                                    textView3.setTextSize(1, 20.0f);
                                    textView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                                    textView3.setPadding(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(21.0f), AndroidUtilities.dp(8.0f));
                                    textView3.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                                    textView3.setGravity(16);
                                    this.patternLayout[i5].addView(textView3, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 21.0f, 0.0f, 0.0f));
                                    AnonymousClass18 anonymousClass18 = new AnonymousClass18(this, context);
                                    this.patternsListView = anonymousClass18;
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 0, false);
                                    this.patternsLayoutManager = linearLayoutManager;
                                    anonymousClass18.setLayoutManager(linearLayoutManager);
                                    RecyclerListView recyclerListView2 = this.patternsListView;
                                    PatternsAdapter patternsAdapter = new PatternsAdapter(context);
                                    this.patternsAdapter = patternsAdapter;
                                    recyclerListView2.setAdapter(patternsAdapter);
                                    this.patternsListView.addItemDecoration(new AnonymousClass19(this));
                                    this.patternLayout[i5].addView(this.patternsListView, LayoutHelper.createFrame(-1, 100.0f, 51, 0.0f, 76.0f, 0.0f, 0.0f));
                                    this.patternsListView.setOnItemClickListener(new ThemePreviewActivity$$ExternalSyntheticLambda23(this));
                                    HeaderCell headerCell = new HeaderCell(context);
                                    this.intensityCell = headerCell;
                                    headerCell.setText(LocaleController.getString("BackgroundIntensity", 2131624659));
                                    this.patternLayout[i5].addView(this.intensityCell, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 175.0f, 0.0f, 0.0f));
                                    AnonymousClass20 anonymousClass20 = new AnonymousClass20(this, context);
                                    this.intensitySeekBar = anonymousClass20;
                                    anonymousClass20.setProgress(this.currentIntensity);
                                    this.intensitySeekBar.setReportChanges(true);
                                    this.intensitySeekBar.setDelegate(new AnonymousClass21());
                                    this.patternLayout[i5].addView(this.intensitySeekBar, LayoutHelper.createFrame(-1, 38.0f, 51, 5.0f, 211.0f, 5.0f, 0.0f));
                                } else {
                                    ColorPicker colorPicker = new ColorPicker(context, this.editingTheme, new AnonymousClass22());
                                    this.colorPicker = colorPicker;
                                    if (this.screenType == 1) {
                                        this.patternLayout[i5].addView(colorPicker, LayoutHelper.createFrame(-1, -1, 1));
                                        if (this.applyingTheme.isDark()) {
                                            this.colorPicker.setMinBrightness(0.2f);
                                        } else {
                                            this.colorPicker.setMinBrightness(0.05f);
                                            this.colorPicker.setMaxBrightness(0.8f);
                                        }
                                        this.colorPicker.setType(1, hasChanges(1), 2, this.accent.accentColor2 != 0 ? 2 : 1, false, 0, false);
                                        this.colorPicker.setColor(this.accent.accentColor, 0);
                                        int i16 = this.accent.accentColor2;
                                        if (i16 != 0) {
                                            this.colorPicker.setColor(i16, 1);
                                        }
                                    } else {
                                        this.patternLayout[i5].addView(colorPicker, LayoutHelper.createFrame(-1, -1.0f, 1, 0.0f, 0.0f, 0.0f, 48.0f));
                                    }
                                }
                                i5++;
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
                    i2 = 0;
                    String[] strArr3 = new String[i2];
                    int[] iArr3 = new int[i2];
                    this.backgroundCheckBoxView = new WallpaperCheckBoxView[i2];
                    if (i2 == 0) {
                    }
                    i4 = 0;
                    while (i4 < i2) {
                    }
                    if (this.screenType == 1) {
                    }
                    if (this.screenType != 1) {
                    }
                    this.isBlurred = false;
                    i5 = 0;
                    while (i5 < 2) {
                    }
                    updateButtonState(false, false);
                    if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
                    }
                    if (this.screenType != 1) {
                        this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(true);
                    }
                }
            }
            Object obj3 = this.currentWallpaper;
            if (!(obj3 instanceof WallpapersListActivity.ColorWallpaper) || !"d".equals(((WallpapersListActivity.ColorWallpaper) obj3).slug)) {
                i2 = 3;
                String[] strArr32 = new String[i2];
                int[] iArr32 = new int[i2];
                this.backgroundCheckBoxView = new WallpaperCheckBoxView[i2];
                if (i2 == 0) {
                }
                i4 = 0;
                while (i4 < i2) {
                }
                if (this.screenType == 1) {
                }
                if (this.screenType != 1) {
                }
                this.isBlurred = false;
                i5 = 0;
                while (i5 < 2) {
                }
                updateButtonState(false, false);
                if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
                }
                if (this.screenType != 1) {
                }
            }
            i2 = 0;
            String[] strArr322 = new String[i2];
            int[] iArr322 = new int[i2];
            this.backgroundCheckBoxView = new WallpaperCheckBoxView[i2];
            if (i2 == 0) {
            }
            i4 = 0;
            while (i4 < i2) {
            }
            if (this.screenType == 1) {
            }
            if (this.screenType != 1) {
            }
            this.isBlurred = false;
            i5 = 0;
            while (i5 < 2) {
            }
            updateButtonState(false, false);
            if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
            }
            if (this.screenType != 1) {
            }
        } else {
            str = str2;
        }
        this.listView2.setAdapter(this.messagesAdapter);
        AnonymousClass23 anonymousClass23 = new AnonymousClass23(context);
        this.frameLayout = anonymousClass23;
        anonymousClass23.setWillNotDraw(false);
        FrameLayout frameLayout2 = this.frameLayout;
        this.fragmentView = frameLayout2;
        ViewTreeObserver viewTreeObserver = frameLayout2.getViewTreeObserver();
        ThemePreviewActivity$$ExternalSyntheticLambda13 themePreviewActivity$$ExternalSyntheticLambda13 = new ThemePreviewActivity$$ExternalSyntheticLambda13(this);
        this.onGlobalLayoutListener = themePreviewActivity$$ExternalSyntheticLambda13;
        viewTreeObserver.addOnGlobalLayoutListener(themePreviewActivity$$ExternalSyntheticLambda13);
        ViewPager viewPager = new ViewPager(context);
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(new AnonymousClass24());
        this.viewPager.setAdapter(new AnonymousClass25());
        AndroidUtilities.setViewPagerEdgeEffectColor(this.viewPager, Theme.getColor("actionBarDefault"));
        this.frameLayout.addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.screenType == 0 ? 48.0f : 0.0f));
        UndoView undoView = new UndoView(context, this);
        this.undoView = undoView;
        undoView.setAdditionalTranslationY(AndroidUtilities.dp(51.0f));
        this.frameLayout.addView(this.undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        if (this.screenType == 0) {
            View view = new View(context);
            view.setBackgroundColor(Theme.getColor("dialogShadowLine"));
            FrameLayout.LayoutParams layoutParams4 = new FrameLayout.LayoutParams(-1, 1, 83);
            layoutParams4.bottomMargin = AndroidUtilities.dp(48.0f);
            this.frameLayout.addView(view, layoutParams4);
            FrameLayout frameLayout3 = new FrameLayout(context);
            this.saveButtonsContainer = frameLayout3;
            frameLayout3.setBackgroundColor(getButtonsColor("windowBackgroundWhite"));
            this.frameLayout.addView(this.saveButtonsContainer, LayoutHelper.createFrame(-1, 48, 83));
            AnonymousClass26 anonymousClass26 = new AnonymousClass26(context);
            this.dotsContainer = anonymousClass26;
            this.saveButtonsContainer.addView(anonymousClass26, LayoutHelper.createFrame(22, 8, 17));
            TextView textView4 = new TextView(context);
            this.cancelButton = textView4;
            textView4.setTextSize(1, 14.0f);
            String str3 = str;
            this.cancelButton.setTextColor(getButtonsColor(str3));
            this.cancelButton.setGravity(17);
            this.cancelButton.setBackgroundDrawable(Theme.createSelectorDrawable(251658240, 0));
            this.cancelButton.setPadding(AndroidUtilities.dp(29.0f), 0, AndroidUtilities.dp(29.0f), 0);
            this.cancelButton.setText(LocaleController.getString("Cancel", 2131624832).toUpperCase());
            this.cancelButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.saveButtonsContainer.addView(this.cancelButton, LayoutHelper.createFrame(-2, -1, 51));
            this.cancelButton.setOnClickListener(new ThemePreviewActivity$$ExternalSyntheticLambda5(this));
            TextView textView5 = new TextView(context);
            this.doneButton = textView5;
            textView5.setTextSize(1, 14.0f);
            this.doneButton.setTextColor(getButtonsColor(str3));
            this.doneButton.setGravity(17);
            this.doneButton.setBackgroundDrawable(Theme.createSelectorDrawable(251658240, 0));
            this.doneButton.setPadding(AndroidUtilities.dp(29.0f), 0, AndroidUtilities.dp(29.0f), 0);
            this.doneButton.setText(LocaleController.getString("ApplyTheme", 2131624396).toUpperCase());
            this.doneButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.saveButtonsContainer.addView(this.doneButton, LayoutHelper.createFrame(-2, -1, 53));
            this.doneButton.setOnClickListener(new ThemePreviewActivity$$ExternalSyntheticLambda6(this));
        }
        if (this.screenType == 1 && !Theme.hasCustomWallpaper() && this.accent.backgroundOverrideColor != 4294967296L) {
            selectColorType(2);
        }
        this.themeDescriptions = getThemeDescriptionsInternal();
        setCurrentImage(true);
        updatePlayAnimationView(false);
        if (this.showColor) {
            showPatternsView(0, true, false);
        }
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
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

        AnonymousClass1(ThemePreviewActivity themePreviewActivity) {
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            ThemePreviewActivity.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            setMeasuredDimension(size, size2);
            measureChildWithMargins(((BaseFragment) ThemePreviewActivity.this).actionBar, i, 0, i2, 0);
            int measuredHeight = ((BaseFragment) ThemePreviewActivity.this).actionBar.getMeasuredHeight();
            if (((BaseFragment) ThemePreviewActivity.this).actionBar.getVisibility() == 0) {
                size2 -= measuredHeight;
            }
            ((FrameLayout.LayoutParams) ThemePreviewActivity.this.listView.getLayoutParams()).topMargin = measuredHeight;
            ThemePreviewActivity.this.listView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            measureChildWithMargins(ThemePreviewActivity.this.floatingButton, i, 0, i2, 0);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            boolean drawChild = super.drawChild(canvas, view, j);
            if (view == ((BaseFragment) ThemePreviewActivity.this).actionBar && ((BaseFragment) ThemePreviewActivity.this).parentLayout != null) {
                ((BaseFragment) ThemePreviewActivity.this).parentLayout.drawHeaderShadow(canvas, ((BaseFragment) ThemePreviewActivity.this).actionBar.getVisibility() == 0 ? ((BaseFragment) ThemePreviewActivity.this).actionBar.getMeasuredHeight() : 0);
            }
            return drawChild;
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends ViewOutlineProvider {
        AnonymousClass3(ThemePreviewActivity themePreviewActivity) {
        }

        @Override // android.view.ViewOutlineProvider
        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends FrameLayout {
        private boolean ignoreLayout;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context) {
            super(context);
            ThemePreviewActivity.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
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
            measureChildWithMargins(ThemePreviewActivity.this.actionBar2, i, 0, i2, 0);
            int measuredHeight = ThemePreviewActivity.this.actionBar2.getMeasuredHeight();
            if (ThemePreviewActivity.this.actionBar2.getVisibility() == 0) {
                size2 -= measuredHeight;
            }
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.listView2.getLayoutParams();
            layoutParams2.topMargin = measuredHeight;
            ThemePreviewActivity.this.listView2.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2 - layoutParams2.bottomMargin, 1073741824));
            ((FrameLayout.LayoutParams) ThemePreviewActivity.this.backgroundImage.getLayoutParams()).topMargin = measuredHeight;
            ThemePreviewActivity.this.backgroundImage.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            if (ThemePreviewActivity.this.bottomOverlayChat != null) {
                measureChildWithMargins(ThemePreviewActivity.this.bottomOverlayChat, i, 0, i2, 0);
            }
            for (int i3 = 0; i3 < ThemePreviewActivity.this.patternLayout.length; i3++) {
                if (ThemePreviewActivity.this.patternLayout[i3] != null) {
                    measureChildWithMargins(ThemePreviewActivity.this.patternLayout[i3], i, 0, i2, 0);
                }
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
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass5() {
            ThemePreviewActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            Theme.ThemeAccent accent;
            String url;
            int i2 = 0;
            if (i == -1) {
                if (!ThemePreviewActivity.this.checkDiscard()) {
                    return;
                }
                ThemePreviewActivity.this.cancelThemeApply(false);
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
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, ThemePreviewActivity.this.applyingTheme, Boolean.valueOf(ThemePreviewActivity.this.nightTheme), null, -1);
                ThemePreviewActivity.this.finishFragment();
            } else if (i != 5 || ThemePreviewActivity.this.getParentActivity() == null) {
            } else {
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
                } else {
                    url = "https://" + MessagesController.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).linkPrefix + "/bg/" + ((TLRPC$TL_wallPaper) ThemePreviewActivity.this.currentWallpaper).slug;
                    if (sb.length() > 0) {
                        url = url + "?mode=" + sb.toString();
                    }
                }
                String str = url;
                ThemePreviewActivity.this.showDialog(new AnonymousClass1(ThemePreviewActivity.this.getParentActivity(), null, str, false, str, false));
            }
        }

        /* renamed from: org.telegram.ui.ThemePreviewActivity$5$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends ShareAlert {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, ArrayList arrayList, String str, boolean z, String str2, boolean z2) {
                super(context, arrayList, str, z, str2, z2);
                AnonymousClass5.this = r8;
            }

            @Override // org.telegram.ui.Components.ShareAlert
            protected void onSend(LongSparseArray<TLRPC$Dialog> longSparseArray, int i) {
                if (longSparseArray.size() == 1) {
                    ThemePreviewActivity.this.undoView.showWithAction(longSparseArray.valueAt(0).id, 61, Integer.valueOf(i));
                } else {
                    ThemePreviewActivity.this.undoView.showWithAction(0L, 61, Integer.valueOf(i), Integer.valueOf(longSparseArray.size()), (Runnable) null, (Runnable) null);
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 extends BackupImageView {
        private Drawable background;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass6(Context context) {
            super(context);
            ThemePreviewActivity.this = r1;
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
            if (ThemePreviewActivity.this.radialProgress != null) {
                int dp = AndroidUtilities.dp(44.0f);
                int measuredWidth = (getMeasuredWidth() - dp) / 2;
                int measuredHeight = (getMeasuredHeight() - dp) / 2;
                ThemePreviewActivity.this.radialProgress.setProgressRect(measuredWidth, measuredHeight, measuredWidth + dp, dp + measuredHeight);
            }
            ThemePreviewActivity themePreviewActivity2 = ThemePreviewActivity.this;
            themePreviewActivity2.progressVisible = themePreviewActivity2.screenType == 2 && getMeasuredWidth() <= getMeasuredHeight();
        }

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onDraw(Canvas canvas) {
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
                    this.background.setBounds(measuredWidth, i, ceil + measuredWidth, ceil2 + i);
                    this.background.draw(canvas);
                }
            }
            super.onDraw(canvas);
            if (!ThemePreviewActivity.this.progressVisible || ThemePreviewActivity.this.radialProgress == null) {
                return;
            }
            ThemePreviewActivity.this.radialProgress.draw(canvas);
        }

        @Override // android.view.View
        public Drawable getBackground() {
            return this.background;
        }

        @Override // android.view.View
        public void setBackground(Drawable drawable) {
            this.background = drawable;
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            if (ThemePreviewActivity.this.radialProgress != null) {
                ThemePreviewActivity.this.radialProgress.setOverrideAlpha(f);
            }
        }
    }

    public /* synthetic */ void lambda$createView$2(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
        if (!(this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
            Drawable drawable = imageReceiver.getDrawable();
            if (!z || drawable == null) {
                return;
            }
            if (!Theme.hasThemeKey("chat_serviceBackground") || (this.backgroundImage.getBackground() instanceof MotionBackgroundDrawable)) {
                Theme.applyChatServiceMessageColor(AndroidUtilities.calcDrawableColor(drawable), drawable);
            }
            this.listView2.invalidateViews();
            FrameLayout frameLayout = this.backgroundButtonsContainer;
            if (frameLayout != null) {
                int childCount = frameLayout.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    this.backgroundButtonsContainer.getChildAt(i).invalidate();
                }
            }
            FrameLayout frameLayout2 = this.messagesButtonsContainer;
            if (frameLayout2 != null) {
                int childCount2 = frameLayout2.getChildCount();
                for (int i2 = 0; i2 < childCount2; i2++) {
                    this.messagesButtonsContainer.getChildAt(i2).invalidate();
                }
            }
            RadialProgress2 radialProgress2 = this.radialProgress;
            if (radialProgress2 != null) {
                radialProgress2.setColors("chat_serviceBackground", "chat_serviceBackground", "chat_serviceText", "chat_serviceText");
            }
            if (z2 || !this.isBlurred || this.blurredBitmap != null) {
                return;
            }
            this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(false);
            updateBlurred();
            this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(true);
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$7 */
    /* loaded from: classes3.dex */
    class AnonymousClass7 extends ActionBarMenuItem {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass7(Context context, ActionBarMenu actionBarMenu, int i, int i2) {
            super(context, actionBarMenu, i, i2);
            ThemePreviewActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem, android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setText(ThemePreviewActivity.this.dropDown.getText());
        }
    }

    public /* synthetic */ void lambda$createView$3(View view) {
        this.dropDownContainer.toggleSubMenu();
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$8 */
    /* loaded from: classes3.dex */
    class AnonymousClass8 extends RecyclerListView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass8(Context context) {
            super(context);
            ThemePreviewActivity.this = r1;
        }

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
                        int i = 0;
                        while (i < 20) {
                            i++;
                            RecyclerView.ViewHolder childViewHolder2 = ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder2.getAdapterPosition() + 1);
                            if (childViewHolder2 == null) {
                                break;
                            }
                            top = childViewHolder2.itemView.getTop();
                            if (top2 - AndroidUtilities.dp(48.0f) < childViewHolder2.itemView.getBottom()) {
                                translationX = Math.min(childViewHolder2.itemView.getTranslationX(), translationX);
                            }
                            View view2 = childViewHolder2.itemView;
                            if (view2 instanceof ChatMessageCell) {
                                if (!((ChatMessageCell) view2).isPinnedTop()) {
                                    break;
                                }
                            } else {
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
        public void setTranslationY(float f) {
            super.setTranslationY(f);
            if (ThemePreviewActivity.this.backgroundCheckBoxView != null) {
                for (int i = 0; i < ThemePreviewActivity.this.backgroundCheckBoxView.length; i++) {
                    ThemePreviewActivity.this.backgroundCheckBoxView[i].invalidate();
                }
            }
            if (ThemePreviewActivity.this.messagesCheckBoxView != null) {
                for (int i2 = 0; i2 < ThemePreviewActivity.this.messagesCheckBoxView.length; i2++) {
                    ThemePreviewActivity.this.messagesCheckBoxView[i2].invalidate();
                }
            }
            if (ThemePreviewActivity.this.backgroundPlayAnimationView != null) {
                ThemePreviewActivity.this.backgroundPlayAnimationView.invalidate();
            }
            if (ThemePreviewActivity.this.messagesPlayAnimationView != null) {
                ThemePreviewActivity.this.messagesPlayAnimationView.invalidate();
            }
        }

        @Override // org.telegram.ui.Components.RecyclerListView
        public void onChildPressed(View view, float f, float f2, boolean z) {
            if (!z || !(view instanceof ChatMessageCell) || ((ChatMessageCell) view).isInsideBackground(f, f2)) {
                super.onChildPressed(view, f, f2, z);
            }
        }

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
            return super.onTouchEvent(motionEvent);
        }

        private void checkMotionEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 1) {
                if (!ThemePreviewActivity.this.wasScroll && (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) && ThemePreviewActivity.this.patternLayout[0].getVisibility() == 0) {
                    ThemePreviewActivity.this.showPatternsView(0, false, true);
                }
                ThemePreviewActivity.this.wasScroll = false;
            }
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$9 */
    /* loaded from: classes3.dex */
    class AnonymousClass9 extends DefaultItemAnimator {
        AnonymousClass9() {
            ThemePreviewActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.DefaultItemAnimator
        protected void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
            ThemePreviewActivity.this.listView2.invalidateViews();
        }
    }

    public /* synthetic */ void lambda$createView$4(View view, int i, float f, float f2) {
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

    /* renamed from: org.telegram.ui.ThemePreviewActivity$10 */
    /* loaded from: classes3.dex */
    class AnonymousClass10 extends RecyclerView.OnScrollListener {
        AnonymousClass10() {
            ThemePreviewActivity.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            ThemePreviewActivity.this.listView2.invalidateViews();
            ThemePreviewActivity.this.wasScroll = true;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 0) {
                ThemePreviewActivity.this.wasScroll = false;
            }
        }
    }

    public /* synthetic */ void lambda$createView$5(int i, int i2, float f) {
        if (!this.isMotion) {
            return;
        }
        this.backgroundImage.getBackground();
        float f2 = 1.0f;
        if (this.motionAnimation != null) {
            f2 = (this.backgroundImage.getScaleX() - 1.0f) / (this.parallaxScale - 1.0f);
        }
        this.backgroundImage.setTranslationX(i * f2);
        this.backgroundImage.setTranslationY(i2 * f2);
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$11 */
    /* loaded from: classes3.dex */
    class AnonymousClass11 extends FrameLayout {
        AnonymousClass11(ThemePreviewActivity themePreviewActivity, Context context) {
            super(context);
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            int intrinsicHeight = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
            Theme.chat_composeShadowDrawable.setBounds(0, 0, getMeasuredWidth(), intrinsicHeight);
            Theme.chat_composeShadowDrawable.draw(canvas);
            canvas.drawRect(0.0f, intrinsicHeight, getMeasuredWidth(), getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:111:0x025a  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x02ca  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x02cd  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x02da  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x031d  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0198 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01c5  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x01d0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$6(View view) {
        boolean z;
        boolean z2;
        Object obj;
        File file;
        int i;
        int i2;
        int i3;
        String str;
        Object obj2;
        WallpaperActivityDelegate wallpaperActivityDelegate;
        File file2;
        File file3;
        Exception e;
        Theme.ThemeInfo activeTheme = Theme.getActiveTheme();
        String generateWallpaperName = activeTheme.generateWallpaperName(null, this.isBlurred);
        int i4 = 0;
        String generateWallpaperName2 = this.isBlurred ? activeTheme.generateWallpaperName(null, false) : generateWallpaperName;
        File file4 = new File(ApplicationLoader.getFilesDirFixed(), generateWallpaperName);
        Object obj3 = this.currentWallpaper;
        if (obj3 instanceof TLRPC$TL_wallPaper) {
            if (this.originalBitmap != null) {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file4);
                    this.originalBitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream);
                    fileOutputStream.close();
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            } else {
                ImageReceiver imageReceiver = this.backgroundImage.getImageReceiver();
                if (imageReceiver.hasNotThumb() || imageReceiver.hasStaticThumb()) {
                    Bitmap bitmap = imageReceiver.getBitmap();
                    try {
                        FileOutputStream fileOutputStream2 = new FileOutputStream(file4);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream2);
                        fileOutputStream2.close();
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                }
                z2 = false;
                if (!z2) {
                    try {
                        z2 = AndroidUtilities.copyFile(FileLoader.getInstance(this.currentAccount).getPathToAttach(((TLRPC$TL_wallPaper) this.currentWallpaper).document, true), file4);
                    } catch (Exception e4) {
                        FileLog.e(e4);
                    }
                }
            }
            z2 = true;
            if (!z2) {
            }
        } else {
            if (obj3 instanceof WallpapersListActivity.ColorWallpaper) {
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
                        FileOutputStream fileOutputStream3 = new FileOutputStream(file4);
                        if (this.backgroundGradientColor2 != 0) {
                            createBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream3);
                        } else {
                            createBitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream3);
                        }
                        fileOutputStream3.close();
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
                }
            } else if (obj3 instanceof WallpapersListActivity.FileWallpaper) {
                WallpapersListActivity.FileWallpaper fileWallpaper = (WallpapersListActivity.FileWallpaper) obj3;
                if (fileWallpaper.resId == 0 && !"t".equals(fileWallpaper.slug)) {
                    try {
                        File file5 = fileWallpaper.originalPath;
                        if (file5 == null) {
                            file5 = fileWallpaper.path;
                        }
                        z = file5.equals(file4);
                        if (z) {
                            z2 = true;
                        } else {
                            try {
                                z2 = AndroidUtilities.copyFile(file5, file4);
                            } catch (Exception e5) {
                                e = e5;
                                FileLog.e(e);
                                z2 = false;
                                if (this.isBlurred) {
                                }
                                int i5 = 45;
                                obj = this.currentWallpaper;
                                if (obj instanceof TLRPC$TL_wallPaper) {
                                }
                                i3 = 0;
                                i2 = 0;
                                i = 0;
                                file = null;
                                Theme.OverrideWallpaperInfo overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
                                overrideWallpaperInfo.fileName = generateWallpaperName2;
                                overrideWallpaperInfo.originalFileName = generateWallpaperName;
                                overrideWallpaperInfo.slug = str;
                                overrideWallpaperInfo.isBlurred = this.isBlurred;
                                overrideWallpaperInfo.isMotion = this.isMotion;
                                overrideWallpaperInfo.color = i4;
                                overrideWallpaperInfo.gradientColor1 = i3;
                                overrideWallpaperInfo.gradientColor2 = i2;
                                overrideWallpaperInfo.gradientColor3 = i;
                                overrideWallpaperInfo.rotation = i5;
                                overrideWallpaperInfo.intensity = this.currentIntensity;
                                obj2 = this.currentWallpaper;
                                if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                                }
                                MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file, overrideWallpaperInfo, str != null, 0L);
                                if (z2) {
                                }
                                wallpaperActivityDelegate = this.delegate;
                                if (wallpaperActivityDelegate != null) {
                                }
                                finishFragment();
                            }
                        }
                    } catch (Exception e6) {
                        e = e6;
                        z = false;
                    }
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
                    int i52 = 45;
                    obj = this.currentWallpaper;
                    if (obj instanceof TLRPC$TL_wallPaper) {
                        str = ((TLRPC$TL_wallPaper) obj).slug;
                    } else if (obj instanceof WallpapersListActivity.ColorWallpaper) {
                        if (!"d".equals(((WallpapersListActivity.ColorWallpaper) obj).slug)) {
                            TLRPC$TL_wallPaper tLRPC$TL_wallPaper = this.selectedPattern;
                            String str2 = tLRPC$TL_wallPaper != null ? tLRPC$TL_wallPaper.slug : "c";
                            int i6 = this.backgroundColor;
                            i3 = this.backgroundGradientColor1;
                            i2 = this.backgroundGradientColor2;
                            i = this.backgroundGradientColor3;
                            str = str2;
                            i52 = this.backgroundRotation;
                            i4 = i6;
                            file = null;
                            Theme.OverrideWallpaperInfo overrideWallpaperInfo2 = new Theme.OverrideWallpaperInfo();
                            overrideWallpaperInfo2.fileName = generateWallpaperName2;
                            overrideWallpaperInfo2.originalFileName = generateWallpaperName;
                            overrideWallpaperInfo2.slug = str;
                            overrideWallpaperInfo2.isBlurred = this.isBlurred;
                            overrideWallpaperInfo2.isMotion = this.isMotion;
                            overrideWallpaperInfo2.color = i4;
                            overrideWallpaperInfo2.gradientColor1 = i3;
                            overrideWallpaperInfo2.gradientColor2 = i2;
                            overrideWallpaperInfo2.gradientColor3 = i;
                            overrideWallpaperInfo2.rotation = i52;
                            overrideWallpaperInfo2.intensity = this.currentIntensity;
                            obj2 = this.currentWallpaper;
                            if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                                WallpapersListActivity.ColorWallpaper colorWallpaper2 = (WallpapersListActivity.ColorWallpaper) obj2;
                                String str3 = ("c".equals(str) || "t".equals(str) || "d".equals(str)) ? null : str;
                                float f = colorWallpaper2.intensity;
                                if (f < 0.0f && !Theme.getActiveTheme().isDark()) {
                                    f *= -1.0f;
                                }
                                if (colorWallpaper2.parentWallpaper != null && colorWallpaper2.color == i4 && colorWallpaper2.gradientColor1 == i3 && colorWallpaper2.gradientColor2 == i2 && colorWallpaper2.gradientColor3 == i && TextUtils.equals(colorWallpaper2.slug, str3) && colorWallpaper2.gradientRotation == i52 && (this.selectedPattern == null || Math.abs(f - this.currentIntensity) < 0.001f)) {
                                    TLRPC$WallPaper tLRPC$WallPaper = colorWallpaper2.parentWallpaper;
                                    overrideWallpaperInfo2.wallpaperId = tLRPC$WallPaper.id;
                                    overrideWallpaperInfo2.accessHash = tLRPC$WallPaper.access_hash;
                                }
                            }
                            MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file, overrideWallpaperInfo2, str != null, 0L);
                            if (z2) {
                                Theme.serviceMessageColorBackup = Theme.getColor("chat_serviceBackground");
                                Theme.getActiveTheme().setOverrideWallpaper("t".equals(overrideWallpaperInfo2.slug) ? null : overrideWallpaperInfo2);
                                Theme.reloadWallpaper();
                                if (!z) {
                                    ImageLoader imageLoader = ImageLoader.getInstance();
                                    imageLoader.removeImage(ImageLoader.getHttpFileName(file4.getAbsolutePath()) + "@100_100");
                                }
                            }
                            wallpaperActivityDelegate = this.delegate;
                            if (wallpaperActivityDelegate != null) {
                                wallpaperActivityDelegate.didSetNewBackground();
                            }
                            finishFragment();
                        }
                        str = "d";
                    } else {
                        if (obj instanceof WallpapersListActivity.FileWallpaper) {
                            WallpapersListActivity.FileWallpaper fileWallpaper2 = (WallpapersListActivity.FileWallpaper) obj;
                            str = fileWallpaper2.slug;
                            file2 = fileWallpaper2.path;
                        } else if (obj instanceof MediaController.SearchImage) {
                            MediaController.SearchImage searchImage = (MediaController.SearchImage) obj;
                            TLRPC$Photo tLRPC$Photo = searchImage.photo;
                            if (tLRPC$Photo != null) {
                                file2 = FileLoader.getInstance(this.currentAccount).getPathToAttach(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, this.maxWallpaperSize, true), true);
                            } else {
                                file2 = ImageLoader.getHttpFilePath(searchImage.imageUrl, "jpg");
                            }
                            str = "";
                        } else {
                            str = "d";
                            i4 = 0;
                        }
                        file = file2;
                        i4 = 0;
                        i3 = 0;
                        i2 = 0;
                        i = 0;
                        Theme.OverrideWallpaperInfo overrideWallpaperInfo22 = new Theme.OverrideWallpaperInfo();
                        overrideWallpaperInfo22.fileName = generateWallpaperName2;
                        overrideWallpaperInfo22.originalFileName = generateWallpaperName;
                        overrideWallpaperInfo22.slug = str;
                        overrideWallpaperInfo22.isBlurred = this.isBlurred;
                        overrideWallpaperInfo22.isMotion = this.isMotion;
                        overrideWallpaperInfo22.color = i4;
                        overrideWallpaperInfo22.gradientColor1 = i3;
                        overrideWallpaperInfo22.gradientColor2 = i2;
                        overrideWallpaperInfo22.gradientColor3 = i;
                        overrideWallpaperInfo22.rotation = i52;
                        overrideWallpaperInfo22.intensity = this.currentIntensity;
                        obj2 = this.currentWallpaper;
                        if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                        }
                        MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file, overrideWallpaperInfo22, str != null, 0L);
                        if (z2) {
                        }
                        wallpaperActivityDelegate = this.delegate;
                        if (wallpaperActivityDelegate != null) {
                        }
                        finishFragment();
                    }
                    i3 = 0;
                    i2 = 0;
                    i = 0;
                    file = null;
                    Theme.OverrideWallpaperInfo overrideWallpaperInfo222 = new Theme.OverrideWallpaperInfo();
                    overrideWallpaperInfo222.fileName = generateWallpaperName2;
                    overrideWallpaperInfo222.originalFileName = generateWallpaperName;
                    overrideWallpaperInfo222.slug = str;
                    overrideWallpaperInfo222.isBlurred = this.isBlurred;
                    overrideWallpaperInfo222.isMotion = this.isMotion;
                    overrideWallpaperInfo222.color = i4;
                    overrideWallpaperInfo222.gradientColor1 = i3;
                    overrideWallpaperInfo222.gradientColor2 = i2;
                    overrideWallpaperInfo222.gradientColor3 = i;
                    overrideWallpaperInfo222.rotation = i52;
                    overrideWallpaperInfo222.intensity = this.currentIntensity;
                    obj2 = this.currentWallpaper;
                    if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                    }
                    MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file, overrideWallpaperInfo222, str != null, 0L);
                    if (z2) {
                    }
                    wallpaperActivityDelegate = this.delegate;
                    if (wallpaperActivityDelegate != null) {
                    }
                    finishFragment();
                }
            } else {
                if (obj3 instanceof MediaController.SearchImage) {
                    MediaController.SearchImage searchImage2 = (MediaController.SearchImage) obj3;
                    TLRPC$Photo tLRPC$Photo2 = searchImage2.photo;
                    if (tLRPC$Photo2 != null) {
                        file3 = FileLoader.getInstance(this.currentAccount).getPathToAttach(FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo2.sizes, this.maxWallpaperSize, true), true);
                    } else {
                        file3 = ImageLoader.getHttpFilePath(searchImage2.imageUrl, "jpg");
                    }
                    try {
                        z2 = AndroidUtilities.copyFile(file3, file4);
                    } catch (Exception e7) {
                        FileLog.e(e7);
                    }
                }
                z2 = false;
            }
            z2 = true;
        }
        z = false;
        if (this.isBlurred) {
        }
        int i522 = 45;
        obj = this.currentWallpaper;
        if (obj instanceof TLRPC$TL_wallPaper) {
        }
        i3 = 0;
        i2 = 0;
        i = 0;
        file = null;
        Theme.OverrideWallpaperInfo overrideWallpaperInfo2222 = new Theme.OverrideWallpaperInfo();
        overrideWallpaperInfo2222.fileName = generateWallpaperName2;
        overrideWallpaperInfo2222.originalFileName = generateWallpaperName;
        overrideWallpaperInfo2222.slug = str;
        overrideWallpaperInfo2222.isBlurred = this.isBlurred;
        overrideWallpaperInfo2222.isMotion = this.isMotion;
        overrideWallpaperInfo2222.color = i4;
        overrideWallpaperInfo2222.gradientColor1 = i3;
        overrideWallpaperInfo2222.gradientColor2 = i2;
        overrideWallpaperInfo2222.gradientColor3 = i;
        overrideWallpaperInfo2222.rotation = i522;
        overrideWallpaperInfo2222.intensity = this.currentIntensity;
        obj2 = this.currentWallpaper;
        if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
        }
        MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file, overrideWallpaperInfo2222, str != null, 0L);
        if (z2) {
        }
        wallpaperActivityDelegate = this.delegate;
        if (wallpaperActivityDelegate != null) {
        }
        finishFragment();
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$12 */
    /* loaded from: classes3.dex */
    class AnonymousClass12 extends FrameLayout {
        private RectF rect = new RectF();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass12(Context context) {
            super(context);
            ThemePreviewActivity.this = r1;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            this.rect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            Theme.applyServiceShaderMatrixForView(ThemePreviewActivity.this.backgroundPlayAnimationView, ThemePreviewActivity.this.backgroundImage);
            canvas.drawRoundRect(this.rect, getMeasuredHeight() / 2, getMeasuredHeight() / 2, Theme.chat_actionBackgroundPaint);
            if (Theme.hasGradientService()) {
                canvas.drawRoundRect(this.rect, getMeasuredHeight() / 2, getMeasuredHeight() / 2, Theme.chat_actionBackgroundGradientDarkenPaint);
            }
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$13 */
    /* loaded from: classes3.dex */
    class AnonymousClass13 implements View.OnClickListener {
        int rotation = 0;

        AnonymousClass13() {
            ThemePreviewActivity.this = r1;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Drawable background = ThemePreviewActivity.this.backgroundImage.getBackground();
            ThemePreviewActivity.this.backgroundPlayAnimationImageView.setRotation(this.rotation);
            this.rotation -= 45;
            ThemePreviewActivity.this.backgroundPlayAnimationImageView.animate().rotationBy(-45.0f).setDuration(300L).setInterpolator(CubicBezierInterpolator.EASE_OUT).start();
            if (!(background instanceof MotionBackgroundDrawable)) {
                ThemePreviewActivity.this.onColorsRotate();
            } else {
                ((MotionBackgroundDrawable) background).switchToNextPosition();
            }
        }
    }

    public /* synthetic */ void lambda$createView$7(int i, WallpaperCheckBoxView wallpaperCheckBoxView, View view) {
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
            boolean z = false;
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
                WallpaperCheckBoxView wallpaperCheckBoxView2 = this.backgroundCheckBoxView[1];
                if (this.selectedPattern != null) {
                    z = true;
                }
                wallpaperCheckBoxView2.setChecked(z, true);
                updateSelectedPattern(true);
                this.patternsListView.invalidateViews();
                updateMotionButton();
            } else if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                if (this.patternLayout[i].getVisibility() != 0) {
                    z = true;
                }
                showPatternsView(i, z, true);
            } else if (i2 == 1) {
            } else {
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

    /* renamed from: org.telegram.ui.ThemePreviewActivity$14 */
    /* loaded from: classes3.dex */
    class AnonymousClass14 extends FrameLayout {
        private RectF rect = new RectF();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass14(Context context) {
            super(context);
            ThemePreviewActivity.this = r1;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            this.rect.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            Theme.applyServiceShaderMatrixForView(ThemePreviewActivity.this.messagesPlayAnimationView, ThemePreviewActivity.this.backgroundImage);
            canvas.drawRoundRect(this.rect, getMeasuredHeight() / 2, getMeasuredHeight() / 2, Theme.chat_actionBackgroundPaint);
            if (Theme.hasGradientService()) {
                canvas.drawRoundRect(this.rect, getMeasuredHeight() / 2, getMeasuredHeight() / 2, Theme.chat_actionBackgroundGradientDarkenPaint);
            }
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$15 */
    /* loaded from: classes3.dex */
    class AnonymousClass15 implements View.OnClickListener {
        int rotation = 0;

        AnonymousClass15() {
            ThemePreviewActivity.this = r1;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ThemePreviewActivity.this.messagesPlayAnimationImageView.setRotation(this.rotation);
            this.rotation -= 45;
            ThemePreviewActivity.this.messagesPlayAnimationImageView.animate().rotationBy(-45.0f).setDuration(300L).setInterpolator(CubicBezierInterpolator.EASE_OUT).start();
            if (!ThemePreviewActivity.this.accent.myMessagesAnimated) {
                if (ThemePreviewActivity.this.accent.myMessagesGradientAccentColor3 != 0) {
                    int i = ThemePreviewActivity.this.accent.myMessagesAccentColor != 0 ? ThemePreviewActivity.this.accent.myMessagesAccentColor : ThemePreviewActivity.this.accent.accentColor;
                    ThemePreviewActivity.this.accent.myMessagesAccentColor = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1;
                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1 = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2;
                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2 = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor3;
                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor3 = i;
                } else {
                    int i2 = ThemePreviewActivity.this.accent.myMessagesAccentColor != 0 ? ThemePreviewActivity.this.accent.myMessagesAccentColor : ThemePreviewActivity.this.accent.accentColor;
                    ThemePreviewActivity.this.accent.myMessagesAccentColor = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1;
                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1 = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2;
                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2 = i2;
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
            } else if (ThemePreviewActivity.this.msgOutDrawable.getMotionBackgroundDrawable() == null) {
            } else {
                ThemePreviewActivity.this.msgOutDrawable.getMotionBackgroundDrawable().switchToNextPosition();
            }
        }
    }

    public /* synthetic */ void lambda$createView$8(int i, WallpaperCheckBoxView wallpaperCheckBoxView, View view) {
        if (this.messagesButtonsContainer.getAlpha() == 1.0f && i == 0) {
            wallpaperCheckBoxView.setChecked(!wallpaperCheckBoxView.isChecked(), true);
            this.accent.myMessagesAnimated = wallpaperCheckBoxView.isChecked();
            Theme.refreshThemeColors(true, true);
            this.listView2.invalidateViews();
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$16 */
    /* loaded from: classes3.dex */
    class AnonymousClass16 extends FrameLayout {
        final /* synthetic */ int val$num;
        final /* synthetic */ Rect val$paddings;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass16(Context context, int i, Rect rect) {
            super(context);
            ThemePreviewActivity.this = r1;
            this.val$num = i;
            this.val$paddings = rect;
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            if (this.val$num == 0) {
                ThemePreviewActivity.this.sheetDrawable.setBounds(ThemePreviewActivity.this.colorPicker.getLeft() - this.val$paddings.left, 0, ThemePreviewActivity.this.colorPicker.getRight() + this.val$paddings.right, getMeasuredHeight());
            } else {
                ThemePreviewActivity.this.sheetDrawable.setBounds(-this.val$paddings.left, 0, getMeasuredWidth() + this.val$paddings.right, getMeasuredHeight());
            }
            ThemePreviewActivity.this.sheetDrawable.draw(canvas);
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$17 */
    /* loaded from: classes3.dex */
    class AnonymousClass17 extends FrameLayout {
        AnonymousClass17(ThemePreviewActivity themePreviewActivity, Context context) {
            super(context);
        }

        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            int intrinsicHeight = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
            Theme.chat_composeShadowDrawable.setBounds(0, 0, getMeasuredWidth(), intrinsicHeight);
            Theme.chat_composeShadowDrawable.draw(canvas);
            canvas.drawRect(0.0f, intrinsicHeight, getMeasuredWidth(), getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
        }
    }

    public /* synthetic */ void lambda$createView$9(int i, View view) {
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
                BackupImageView backupImageView = this.backgroundImage;
                ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$TL_wallPaper.document);
                String str = this.imageFilter;
                TLRPC$TL_wallPaper tLRPC$TL_wallPaper2 = this.selectedPattern;
                backupImageView.setImage(forDocument, str, null, null, "jpg", tLRPC$TL_wallPaper2.document.size, 1, tLRPC$TL_wallPaper2);
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

    public /* synthetic */ void lambda$createView$10(int i, View view) {
        if (this.patternViewAnimation != null) {
            return;
        }
        if (this.screenType == 2) {
            showPatternsView(i, false, true);
        } else {
            showPatternsView(0, true, true);
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$18 */
    /* loaded from: classes3.dex */
    class AnonymousClass18 extends RecyclerListView {
        AnonymousClass18(ThemePreviewActivity themePreviewActivity, Context context) {
            super(context);
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            return super.onTouchEvent(motionEvent);
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$19 */
    /* loaded from: classes3.dex */
    class AnonymousClass19 extends RecyclerView.ItemDecoration {
        AnonymousClass19(ThemePreviewActivity themePreviewActivity) {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
            rect.left = AndroidUtilities.dp(12.0f);
            rect.top = 0;
            rect.bottom = 0;
            if (childAdapterPosition == state.getItemCount() - 1) {
                rect.right = AndroidUtilities.dp(12.0f);
            }
        }
    }

    public /* synthetic */ void lambda$createView$11(View view, int i) {
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
        if (i3 <= this.patternsListView.getMeasuredWidth()) {
            return;
        }
        RecyclerListView recyclerListView = this.patternsListView;
        recyclerListView.smoothScrollBy(i3 - recyclerListView.getMeasuredWidth(), 0);
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$20 */
    /* loaded from: classes3.dex */
    class AnonymousClass20 extends SeekBarView {
        AnonymousClass20(ThemePreviewActivity themePreviewActivity, Context context) {
            super(context);
        }

        @Override // org.telegram.ui.Components.SeekBarView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            return super.onTouchEvent(motionEvent);
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$21 */
    /* loaded from: classes3.dex */
    class AnonymousClass21 implements SeekBarView.SeekBarViewDelegate {
        @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
        public /* synthetic */ CharSequence getContentDescription() {
            return SeekBarView.SeekBarViewDelegate.CC.$default$getContentDescription(this);
        }

        @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
        public /* synthetic */ int getStepsCount() {
            return SeekBarView.SeekBarViewDelegate.CC.$default$getStepsCount(this);
        }

        @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
        public void onSeekBarPressed(boolean z) {
        }

        AnonymousClass21() {
            ThemePreviewActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
        public void onSeekBarDrag(boolean z, float f) {
            ThemePreviewActivity.this.currentIntensity = f;
            ThemePreviewActivity.this.backgroundImage.getImageReceiver().setAlpha(Math.abs(ThemePreviewActivity.this.currentIntensity));
            ThemePreviewActivity.this.backgroundImage.invalidate();
            ThemePreviewActivity.this.patternsListView.invalidateViews();
            if (ThemePreviewActivity.this.currentIntensity >= 0.0f) {
                if (Build.VERSION.SDK_INT >= 29 && (ThemePreviewActivity.this.backgroundImage.getBackground() instanceof MotionBackgroundDrawable)) {
                    ThemePreviewActivity.this.backgroundImage.getImageReceiver().setBlendMode(BlendMode.SOFT_LIGHT);
                }
                ThemePreviewActivity.this.backgroundImage.getImageReceiver().setGradientBitmap(null);
                return;
            }
            if (Build.VERSION.SDK_INT >= 29) {
                ThemePreviewActivity.this.backgroundImage.getImageReceiver().setBlendMode(null);
            }
            if (!(ThemePreviewActivity.this.backgroundImage.getBackground() instanceof MotionBackgroundDrawable)) {
                return;
            }
            ThemePreviewActivity.this.backgroundImage.getImageReceiver().setGradientBitmap(((MotionBackgroundDrawable) ThemePreviewActivity.this.backgroundImage.getBackground()).getBitmap());
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$22 */
    /* loaded from: classes3.dex */
    public class AnonymousClass22 implements ColorPicker.ColorPickerDelegate {
        AnonymousClass22() {
            ThemePreviewActivity.this = r1;
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
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needShareTheme, ThemePreviewActivity.this.accent.parentTheme, ThemePreviewActivity.this.accent);
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
            builder.setTitle(LocaleController.getString("DeleteThemeTitle", 2131625452));
            builder.setMessage(LocaleController.getString("DeleteThemeAlert", 2131625451));
            builder.setPositiveButton(LocaleController.getString("Delete", 2131625384), new ThemePreviewActivity$22$$ExternalSyntheticLambda0(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
            AlertDialog create = builder.create();
            ThemePreviewActivity.this.showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView == null) {
                return;
            }
            textView.setTextColor(Theme.getColor("dialogTextRed2"));
        }

        public /* synthetic */ void lambda$deleteTheme$0(DialogInterface dialogInterface, int i) {
            Theme.deleteThemeAccent(ThemePreviewActivity.this.applyingTheme, ThemePreviewActivity.this.accent, true);
            Theme.applyPreviousTheme();
            Theme.refreshThemeColors();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, ThemePreviewActivity.this.applyingTheme, Boolean.valueOf(ThemePreviewActivity.this.nightTheme), null, -1);
            ThemePreviewActivity.this.finishFragment();
        }

        @Override // org.telegram.ui.Components.ColorPicker.ColorPickerDelegate
        public int getDefaultColor(int i) {
            Theme.ThemeAccent themeAccent;
            if (ThemePreviewActivity.this.colorType != 3 || !ThemePreviewActivity.this.applyingTheme.firstAccentIsDefault || i != 0 || (themeAccent = ThemePreviewActivity.this.applyingTheme.themeAccentsMap.get(Theme.DEFALT_THEME_ACCENT_ID)) == null) {
                return 0;
            }
            return themeAccent.myMessagesAccentColor;
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$23 */
    /* loaded from: classes3.dex */
    class AnonymousClass23 extends FrameLayout {
        private int[] loc = new int[2];

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass23(Context context) {
            super(context);
            ThemePreviewActivity.this = r1;
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            if (ThemePreviewActivity.this.page2 != null) {
                ThemePreviewActivity.this.page2.invalidate();
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (!AndroidUtilities.usingHardwareInput) {
                getLocationInWindow(this.loc);
                if (Build.VERSION.SDK_INT < 21 && !AndroidUtilities.isTablet()) {
                    int[] iArr = this.loc;
                    iArr[1] = iArr[1] - AndroidUtilities.statusBarHeight;
                }
                if (ThemePreviewActivity.this.actionBar2.getTranslationY() != this.loc[1]) {
                    ThemePreviewActivity.this.actionBar2.setTranslationY(-this.loc[1]);
                    ThemePreviewActivity.this.page2.invalidate();
                }
                if (SystemClock.elapsedRealtime() >= ThemePreviewActivity.this.watchForKeyboardEndTime) {
                    return;
                }
                invalidate();
            }
        }
    }

    public /* synthetic */ void lambda$createView$12() {
        this.watchForKeyboardEndTime = SystemClock.elapsedRealtime() + 1500;
        this.frameLayout.invalidate();
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$24 */
    /* loaded from: classes3.dex */
    class AnonymousClass24 implements ViewPager.OnPageChangeListener {
        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
        }

        AnonymousClass24() {
            ThemePreviewActivity.this = r1;
        }

        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
            ThemePreviewActivity.this.dotsContainer.invalidate();
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$25 */
    /* loaded from: classes3.dex */
    class AnonymousClass25 extends PagerAdapter {
        @Override // androidx.viewpager.widget.PagerAdapter
        public int getItemPosition(Object obj) {
            return -1;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return obj == view;
        }

        AnonymousClass25() {
            ThemePreviewActivity.this = r1;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return ThemePreviewActivity.this.screenType != 0 ? 1 : 2;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            FrameLayout frameLayout = i == 0 ? ThemePreviewActivity.this.page2 : ThemePreviewActivity.this.page1;
            viewGroup.addView(frameLayout);
            return frameLayout;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$26 */
    /* loaded from: classes3.dex */
    class AnonymousClass26 extends View {
        private Paint paint = new Paint(1);

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass26(Context context) {
            super(context);
            ThemePreviewActivity.this = r1;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int currentItem = ThemePreviewActivity.this.viewPager.getCurrentItem();
            this.paint.setColor(ThemePreviewActivity.this.getButtonsColor("chat_fieldOverlayText"));
            int i = 0;
            while (i < 2) {
                this.paint.setAlpha(i == currentItem ? 255 : 127);
                canvas.drawCircle(AndroidUtilities.dp((i * 15) + 3), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(3.0f), this.paint);
                i++;
            }
        }
    }

    public /* synthetic */ void lambda$createView$13(View view) {
        cancelThemeApply(false);
    }

    public /* synthetic */ void lambda$createView$14(View view) {
        Theme.ThemeAccent themeAccent;
        Theme.ThemeInfo previousTheme = Theme.getPreviousTheme();
        if (previousTheme == null) {
            return;
        }
        int i = previousTheme.prevAccentId;
        if (i >= 0) {
            themeAccent = previousTheme.themeAccentsMap.get(i);
        } else {
            themeAccent = previousTheme.getAccent(false);
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
        finishFragment();
        if (this.screenType != 0) {
            return;
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didApplyNewTheme, previousTheme, themeAccent, Boolean.valueOf(this.deleteOnCancel));
    }

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
            this.accent.backgroundRotation += 45;
            while (true) {
                Theme.ThemeAccent themeAccent = this.accent;
                int i2 = themeAccent.backgroundRotation;
                if (i2 >= 360) {
                    themeAccent.backgroundRotation = i2 - 360;
                } else {
                    Theme.refreshThemeColors();
                    return;
                }
            }
        }
    }

    public void selectColorType(int i) {
        selectColorType(i, true);
    }

    private void selectColorType(int i, boolean z) {
        int i2;
        if (getParentActivity() == null || this.colorType == i || this.patternViewAnimation != null) {
            return;
        }
        if (z && i == 2 && (Theme.hasCustomWallpaper() || this.accent.backgroundOverrideColor == 4294967296L)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("ChangeChatBackground", 2131624861));
            if (!Theme.hasCustomWallpaper() || Theme.isCustomWallpaperColor()) {
                builder.setMessage(LocaleController.getString("ChangeColorToColor", 2131624862));
                builder.setPositiveButton(LocaleController.getString("Reset", 2131628042), new ThemePreviewActivity$$ExternalSyntheticLambda4(this));
                builder.setNegativeButton(LocaleController.getString("Continue", 2131625262), new ThemePreviewActivity$$ExternalSyntheticLambda3(this));
            } else {
                builder.setMessage(LocaleController.getString("ChangeWallpaperToColor", 2131624879));
                builder.setPositiveButton(LocaleController.getString("Change", 2131624860), new ThemePreviewActivity$$ExternalSyntheticLambda0(this));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
            }
            showDialog(builder.create());
            return;
        }
        int i3 = this.colorType;
        this.colorType = i;
        if (i == 1) {
            this.dropDown.setText(LocaleController.getString("ColorPickerMainColor", 2131625194));
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
            this.dropDown.setText(LocaleController.getString("ColorPickerBackground", 2131625193));
            int color = Theme.getColor("chat_wallpaper");
            int color2 = Theme.hasThemeKey("chat_wallpaper_gradient_to") ? Theme.getColor("chat_wallpaper_gradient_to") : 0;
            int color3 = Theme.hasThemeKey("key_chat_wallpaper_gradient_to2") ? Theme.getColor("key_chat_wallpaper_gradient_to2") : 0;
            int color4 = Theme.hasThemeKey("key_chat_wallpaper_gradient_to3") ? Theme.getColor("key_chat_wallpaper_gradient_to3") : 0;
            Theme.ThemeAccent themeAccent = this.accent;
            long j = themeAccent.backgroundGradientOverrideColor1;
            int i5 = (int) j;
            if (i5 == 0 && j != 0) {
                color2 = 0;
            }
            long j2 = themeAccent.backgroundGradientOverrideColor2;
            int i6 = (int) j2;
            if (i6 == 0 && j2 != 0) {
                color3 = 0;
            }
            long j3 = themeAccent.backgroundGradientOverrideColor3;
            int i7 = (int) j3;
            if (i7 == 0 && j3 != 0) {
                color4 = 0;
            }
            int i8 = (int) themeAccent.backgroundOverrideColor;
            this.colorPicker.setType(2, hasChanges(2), 4, (i5 == 0 && color2 == 0) ? 1 : (i7 == 0 && color4 == 0) ? (i6 == 0 && color3 == 0) ? 2 : 3 : 4, false, this.accent.backgroundRotation, false);
            ColorPicker colorPicker = this.colorPicker;
            if (i7 == 0) {
                i7 = color4;
            }
            colorPicker.setColor(i7, 3);
            ColorPicker colorPicker2 = this.colorPicker;
            if (i6 == 0) {
                i6 = color3;
            }
            colorPicker2.setColor(i6, 2);
            ColorPicker colorPicker3 = this.colorPicker;
            if (i5 == 0) {
                i5 = color2;
            }
            colorPicker3.setColor(i5, 1);
            ColorPicker colorPicker4 = this.colorPicker;
            if (i8 != 0) {
                color = i8;
            }
            colorPicker4.setColor(color, 0);
            if (i3 == 1 || this.accent.myMessagesGradientAccentColor2 == 0) {
                this.messagesAdapter.notifyItemInserted(0);
            } else {
                this.messagesAdapter.notifyItemChanged(0);
            }
            this.listView2.smoothScrollBy(0, AndroidUtilities.dp(60.0f));
        } else if (i == 3) {
            this.dropDown.setText(LocaleController.getString("ColorPickerMyMessages", 2131625195));
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
            int i9 = themeAccent3.myMessagesAccentColor;
            if (i9 == 0) {
                i9 = themeAccent3.accentColor;
            }
            colorPicker5.setColor(i9, 0);
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

    public /* synthetic */ void lambda$selectColorType$15(DialogInterface dialogInterface, int i) {
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

    public /* synthetic */ void lambda$selectColorType$16(DialogInterface dialogInterface, int i) {
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

    public /* synthetic */ void lambda$selectColorType$17(DialogInterface dialogInterface, int i) {
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
        this.backgroundImage.setImage(ImageLocation.getForDocument(tLRPC$TL_wallPaper.document), this.imageFilter, null, null, "jpg", tLRPC$TL_wallPaper.document.size, 1, tLRPC$TL_wallPaper);
        this.selectedPattern = tLRPC$TL_wallPaper;
        this.isMotion = this.backgroundCheckBoxView[2].isChecked();
        updateButtonState(false, true);
    }

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
        long j;
        int i2;
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
                int defaultAccentColor = Theme.getDefaultAccentColor("chat_wallpaper");
                int i3 = (int) this.accent.backgroundOverrideColor;
                if (i3 == 0) {
                    i3 = defaultAccentColor;
                }
                if (i3 != defaultAccentColor) {
                    return true;
                }
            }
            long j3 = this.backupBackgroundGradientOverrideColor1;
            if (j3 == 0 && this.backupBackgroundGradientOverrideColor2 == 0 && this.backupBackgroundGradientOverrideColor3 == 0) {
                for (int i4 = 0; i4 < 3; i4++) {
                    if (i4 == 0) {
                        i2 = Theme.getDefaultAccentColor("chat_wallpaper_gradient_to");
                        j = this.accent.backgroundGradientOverrideColor1;
                    } else if (i4 == 1) {
                        i2 = Theme.getDefaultAccentColor("key_chat_wallpaper_gradient_to2");
                        j = this.accent.backgroundGradientOverrideColor2;
                    } else {
                        i2 = Theme.getDefaultAccentColor("key_chat_wallpaper_gradient_to3");
                        j = this.accent.backgroundGradientOverrideColor3;
                    }
                    int i5 = (int) j;
                    if (i5 == 0 && j != 0) {
                        i5 = 0;
                    } else if (i5 == 0) {
                        i5 = i2;
                    }
                    if (i5 != i2) {
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
            int i6 = this.backupAccentColor;
            Theme.ThemeAccent themeAccent2 = this.accent;
            if (i6 != themeAccent2.accentColor2) {
                return true;
            }
            int i7 = this.backupMyMessagesAccentColor;
            if (i7 != 0) {
                if (i7 != themeAccent2.myMessagesAccentColor) {
                    return true;
                }
            } else {
                int i8 = themeAccent2.myMessagesAccentColor;
                if (i8 != 0 && i8 != themeAccent2.accentColor) {
                    return true;
                }
            }
            int i9 = this.backupMyMessagesGradientAccentColor1;
            if (i9 != 0) {
                if (i9 != themeAccent2.myMessagesGradientAccentColor1) {
                    return true;
                }
            } else if (themeAccent2.myMessagesGradientAccentColor1 != 0) {
                return true;
            }
            int i10 = this.backupMyMessagesGradientAccentColor2;
            if (i10 != 0) {
                if (i10 != themeAccent2.myMessagesGradientAccentColor2) {
                    return true;
                }
            } else if (themeAccent2.myMessagesGradientAccentColor2 != 0) {
                return true;
            }
            int i11 = this.backupMyMessagesGradientAccentColor3;
            if (i11 != 0) {
                if (i11 != themeAccent2.myMessagesGradientAccentColor3) {
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

    /* JADX WARN: Code restructure failed: missing block: B:39:0x0084, code lost:
        if (r7.accent.patternMotion == r7.isMotion) goto L40;
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
            builder.setTitle(LocaleController.getString("SaveChangesAlertTitle", 2131628124));
            builder.setMessage(LocaleController.getString("SaveChangesAlertText", 2131628123));
            builder.setPositiveButton(LocaleController.getString("Save", 2131628122), new ThemePreviewActivity$$ExternalSyntheticLambda1(this));
            builder.setNegativeButton(LocaleController.getString("PassportDiscard", 2131627272), new ThemePreviewActivity$$ExternalSyntheticLambda2(this));
            showDialog(builder.create());
            return false;
        }
        return true;
    }

    public /* synthetic */ void lambda$checkDiscard$18(DialogInterface dialogInterface, int i) {
        this.actionBar2.getActionBarMenuOnItemClick().onItemClick(4);
    }

    public /* synthetic */ void lambda$checkDiscard$19(DialogInterface dialogInterface, int i) {
        cancelThemeApply(false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.invalidateMotionBackground);
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
        FrameLayout frameLayout = this.frameLayout;
        if (frameLayout != null && this.onGlobalLayoutListener != null) {
            frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
        }
        int i = this.screenType;
        if (i == 2 || i == 1) {
            AndroidUtilities.runOnUIThread(ThemePreviewActivity$$ExternalSyntheticLambda18.INSTANCE);
        }
        int i2 = this.screenType;
        if (i2 == 2) {
            Bitmap bitmap = this.blurredBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.blurredBitmap = null;
            }
            Theme.applyChatServiceMessageColor();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper, new Object[0]);
        } else if (i2 == 1 || i2 == 0) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        }
        if (this.screenType != 0 || this.accent != null) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersNeedReload);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.wallpapersDidLoad);
        }
        super.onFragmentDestroy();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationStart(boolean z, boolean z2) {
        super.onTransitionAnimationStart(z, z2);
        if (z || this.screenType != 2) {
            return;
        }
        Theme.applyChatServiceMessageColor();
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper, new Object[0]);
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
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        if (this.isMotion) {
            this.parallaxEffect.setEnabled(false);
        }
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
        updateButtonState(true, z);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
        RadialProgress2 radialProgress2 = this.radialProgress;
        if (radialProgress2 != null) {
            radialProgress2.setProgress(1.0f, this.progressVisible);
        }
        updateButtonState(false, true);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
        RadialProgress2 radialProgress2 = this.radialProgress;
        if (radialProgress2 != null) {
            radialProgress2.setProgress(Math.min(1.0f, ((float) j) / ((float) j2)), this.progressVisible);
            if (this.radialProgress.getIcon() == 10) {
                return;
            }
            updateButtonState(false, true);
        }
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    private void updateBlurred() {
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
            if (bitmap2 == null) {
                return;
            }
            this.backgroundImage.setImageBitmap(bitmap2);
            return;
        }
        setCurrentImage(false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (!checkDiscard()) {
            return false;
        }
        cancelThemeApply(true);
        return true;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TLRPC$TL_wallPaper tLRPC$TL_wallPaper;
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
            if (recyclerListView2 == null) {
                return;
            }
            recyclerListView2.invalidateViews();
        } else if (i == NotificationCenter.didSetNewWallpapper) {
            if (this.page2 == null) {
                return;
            }
            setCurrentImage(true);
        } else if (i == NotificationCenter.wallpapersNeedReload) {
            Object obj = this.currentWallpaper;
            if (!(obj instanceof WallpapersListActivity.FileWallpaper)) {
                return;
            }
            WallpapersListActivity.FileWallpaper fileWallpaper = (WallpapersListActivity.FileWallpaper) obj;
            if (fileWallpaper.slug != null) {
                return;
            }
            fileWallpaper.slug = (String) objArr[0];
        } else if (i == NotificationCenter.wallpapersDidLoad) {
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
                    if (themeAccent != null && themeAccent.patternSlug.equals(tLRPC$WallPaper.slug)) {
                        this.selectedPattern = (TLRPC$TL_wallPaper) tLRPC$WallPaper;
                        setCurrentImage(false);
                        updateButtonState(false, false);
                    } else if (this.accent == null) {
                        TLRPC$TL_wallPaper tLRPC$TL_wallPaper2 = this.selectedPattern;
                        if (tLRPC$TL_wallPaper2 != null) {
                            if (!tLRPC$TL_wallPaper2.slug.equals(tLRPC$WallPaper.slug)) {
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
            long j = 0;
            int size2 = arrayList.size();
            for (int i5 = 0; i5 < size2; i5++) {
                TLRPC$WallPaper tLRPC$WallPaper2 = (TLRPC$WallPaper) arrayList.get(i5);
                if (tLRPC$WallPaper2 instanceof TLRPC$TL_wallPaper) {
                    j = MediaDataController.calcHash(j, tLRPC$WallPaper2.id);
                }
            }
            TLRPC$TL_account_getWallPapers tLRPC$TL_account_getWallPapers = new TLRPC$TL_account_getWallPapers();
            tLRPC$TL_account_getWallPapers.hash = j;
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getWallPapers, new ThemePreviewActivity$$ExternalSyntheticLambda21(this)), this.classGuid);
        }
    }

    public /* synthetic */ void lambda$didReceivedNotification$24(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ThemePreviewActivity$$ExternalSyntheticLambda16(this, tLObject));
    }

    public /* synthetic */ void lambda$didReceivedNotification$23(TLObject tLObject) {
        Theme.ThemeAccent themeAccent;
        TLRPC$TL_wallPaper tLRPC$TL_wallPaper;
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
                        if (themeAccent2 != null && themeAccent2.patternSlug.equals(tLRPC$TL_wallPaper2.slug)) {
                            this.selectedPattern = tLRPC$TL_wallPaper2;
                            setCurrentImage(false);
                            updateButtonState(false, false);
                        } else if (this.accent == null) {
                            TLRPC$TL_wallPaper tLRPC$TL_wallPaper3 = this.selectedPattern;
                            if (tLRPC$TL_wallPaper3 != null) {
                                if (!tLRPC$TL_wallPaper3.slug.equals(tLRPC$TL_wallPaper2.slug)) {
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
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(getConnectionsManager().sendRequest(tLRPC$TL_account_getWallPaper, new ThemePreviewActivity$$ExternalSyntheticLambda20(this)), this.classGuid);
    }

    public /* synthetic */ void lambda$didReceivedNotification$22(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new ThemePreviewActivity$$ExternalSyntheticLambda17(this, tLObject));
    }

    public /* synthetic */ void lambda$didReceivedNotification$21(TLObject tLObject) {
        if (tLObject instanceof TLRPC$TL_wallPaper) {
            TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) tLObject;
            if (!tLRPC$TL_wallPaper.pattern) {
                return;
            }
            this.selectedPattern = tLRPC$TL_wallPaper;
            setCurrentImage(false);
            updateButtonState(false, false);
            this.patterns.add(0, this.selectedPattern);
            PatternsAdapter patternsAdapter = this.patternsAdapter;
            if (patternsAdapter == null) {
                return;
            }
            patternsAdapter.notifyDataSetChanged();
        }
    }

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

    public int getButtonsColor(String str) {
        return this.useDefaultThemeForButtons ? Theme.getDefaultColor(str) : Theme.getColor(str);
    }

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
                    int defaultAccentColor = Theme.getDefaultAccentColor("chat_wallpaper");
                    int defaultAccentColor2 = Theme.getDefaultAccentColor("chat_wallpaper_gradient_to");
                    int defaultAccentColor3 = Theme.getDefaultAccentColor("key_chat_wallpaper_gradient_to2");
                    int defaultAccentColor4 = Theme.getDefaultAccentColor("key_chat_wallpaper_gradient_to3");
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
                int defaultAccentColor = Theme.getDefaultAccentColor("chat_wallpaper_gradient_to");
                if (i == 0 && defaultAccentColor != 0) {
                    this.accent.backgroundGradientOverrideColor1 = 4294967296L;
                } else {
                    this.accent.backgroundGradientOverrideColor1 = i;
                }
            } else if (i2 == 2) {
                int defaultAccentColor2 = Theme.getDefaultAccentColor("key_chat_wallpaper_gradient_to2");
                if (i == 0 && defaultAccentColor2 != 0) {
                    this.accent.backgroundGradientOverrideColor2 = 4294967296L;
                } else {
                    this.accent.backgroundGradientOverrideColor2 = i;
                }
            } else if (i2 == 3) {
                int defaultAccentColor3 = Theme.getDefaultAccentColor("key_chat_wallpaper_gradient_to3");
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
            themeDescription.setColor(Theme.getColor(themeDescription.getCurrentKey()), false, false);
        }
        this.listView.invalidateViews();
        this.listView2.invalidateViews();
        View view = this.dotsContainer;
        if (view != null) {
            view.invalidate();
        }
    }

    private void updateButtonState(boolean z, boolean z2) {
        long j;
        File file;
        String str;
        FrameLayout frameLayout;
        String str2;
        File file2;
        int i;
        Object obj = this.selectedPattern;
        if (obj == null) {
            obj = this.currentWallpaper;
        }
        boolean z3 = obj instanceof TLRPC$TL_wallPaper;
        if (z3 || (obj instanceof MediaController.SearchImage)) {
            if (z2 && !this.progressVisible) {
                z2 = false;
            }
            if (z3) {
                TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) obj;
                str = FileLoader.getAttachFileName(tLRPC$TL_wallPaper.document);
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                file = FileLoader.getInstance(this.currentAccount).getPathToAttach(tLRPC$TL_wallPaper.document, true);
                j = tLRPC$TL_wallPaper.document.size;
            } else {
                MediaController.SearchImage searchImage = (MediaController.SearchImage) obj;
                TLRPC$Photo tLRPC$Photo = searchImage.photo;
                if (tLRPC$Photo != null) {
                    TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, this.maxWallpaperSize, true);
                    file2 = FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, true);
                    str2 = FileLoader.getAttachFileName(closestPhotoSizeWithSize);
                    i = closestPhotoSizeWithSize.size;
                } else {
                    file2 = ImageLoader.getHttpFilePath(searchImage.imageUrl, "jpg");
                    str2 = file2.getName();
                    i = searchImage.size;
                }
                j = i;
                String str3 = str2;
                file = file2;
                str = str3;
                if (TextUtils.isEmpty(str)) {
                    return;
                }
            }
            boolean exists = file.exists();
            float f = 1.0f;
            if (exists) {
                DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                RadialProgress2 radialProgress2 = this.radialProgress;
                if (radialProgress2 != null) {
                    radialProgress2.setProgress(1.0f, z2);
                    this.radialProgress.setIcon(4, z, z2);
                }
                this.backgroundImage.invalidate();
                if (this.screenType == 2) {
                    if (j != 0) {
                        this.actionBar2.setSubtitle(AndroidUtilities.formatFileSize(j));
                    } else {
                        this.actionBar2.setSubtitle(null);
                    }
                }
            } else {
                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(str, null, this);
                if (this.radialProgress != null) {
                    FileLoader.getInstance(this.currentAccount).isLoadingFile(str);
                    Float fileProgress = ImageLoader.getInstance().getFileProgress(str);
                    if (fileProgress != null) {
                        this.radialProgress.setProgress(fileProgress.floatValue(), z2);
                    } else {
                        this.radialProgress.setProgress(0.0f, z2);
                    }
                    this.radialProgress.setIcon(10, z, z2);
                }
                if (this.screenType == 2) {
                    this.actionBar2.setSubtitle(LocaleController.getString("LoadingFullImage", 2131626521));
                }
                this.backgroundImage.invalidate();
            }
            if (this.selectedPattern == null && (frameLayout = this.backgroundButtonsContainer) != null) {
                frameLayout.setAlpha(exists ? 1.0f : 0.5f);
            }
            int i2 = this.screenType;
            if (i2 == 0) {
                this.doneButton.setEnabled(exists);
                TextView textView = this.doneButton;
                if (!exists) {
                    f = 0.5f;
                }
                textView.setAlpha(f);
                return;
            } else if (i2 == 2) {
                this.bottomOverlayChat.setEnabled(exists);
                TextView textView2 = this.bottomOverlayChatText;
                if (!exists) {
                    f = 0.5f;
                }
                textView2.setAlpha(f);
                return;
            } else {
                this.saveItem.setEnabled(exists);
                ActionBarMenuItem actionBarMenuItem = this.saveItem;
                if (!exists) {
                    f = 0.5f;
                }
                actionBarMenuItem.setAlpha(f);
                return;
            }
        }
        RadialProgress2 radialProgress22 = this.radialProgress;
        if (radialProgress22 == null) {
            return;
        }
        radialProgress22.setIcon(4, z, z2);
    }

    public void setDelegate(WallpaperActivityDelegate wallpaperActivityDelegate) {
        this.delegate = wallpaperActivityDelegate;
    }

    public void setPatterns(ArrayList<Object> arrayList) {
        this.patterns = arrayList;
        if (this.screenType == 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
            WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper) this.currentWallpaper;
            if (colorWallpaper.patternId == 0) {
                return;
            }
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

    private void showAnimationHint() {
        if (this.page2 == null || this.messagesCheckBoxView == null || this.accent.myMessagesGradientAccentColor2 == 0) {
            return;
        }
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        if (globalMainSettings.getBoolean("bganimationhint", false)) {
            return;
        }
        if (this.animationHint == null) {
            HintView hintView = new HintView(getParentActivity(), 8);
            this.animationHint = hintView;
            hintView.setShowingDuration(5000L);
            this.animationHint.setAlpha(0.0f);
            this.animationHint.setVisibility(4);
            this.animationHint.setText(LocaleController.getString("BackgroundAnimateInfo", 2131624649));
            this.animationHint.setExtraTranslationY(AndroidUtilities.dp(6.0f));
            this.frameLayout.addView(this.animationHint, LayoutHelper.createFrame(-2, -2.0f, 51, 10.0f, 0.0f, 10.0f, 0.0f));
        }
        AndroidUtilities.runOnUIThread(new ThemePreviewActivity$$ExternalSyntheticLambda15(this, globalMainSettings), 500L);
    }

    public /* synthetic */ void lambda$showAnimationHint$25(SharedPreferences sharedPreferences) {
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
        float f = 1.0f;
        float f2 = 0.0f;
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
            if (this.selectedPattern != null) {
                f = 0.0f;
            }
            fArr2[0] = f;
            animatorArr[1] = ObjectAnimator.ofFloat(wallpaperCheckBoxView2, property2, fArr2);
            animatorSet.playTogether(animatorArr);
            animatorSet.addListener(new AnonymousClass27());
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
        if (this.selectedPattern == null) {
            f = 0.0f;
        }
        fArr3[0] = f;
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
        if (this.selectedPattern == null) {
            f2 = -dp;
        }
        fArr5[0] = f2;
        animatorArr4[0] = ObjectAnimator.ofFloat(wallpaperCheckBoxView5, property5, fArr5);
        animatorSet2.playTogether(animatorArr4);
        animatorSet2.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        animatorSet2.setDuration(200L);
        animatorSet2.addListener(new AnonymousClass28());
        animatorSet2.start();
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$27 */
    /* loaded from: classes3.dex */
    public class AnonymousClass27 extends AnimatorListenerAdapter {
        AnonymousClass27() {
            ThemePreviewActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ThemePreviewActivity.this.backgroundCheckBoxView[ThemePreviewActivity.this.selectedPattern != null ? (char) 0 : (char) 2].setVisibility(4);
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$28 */
    /* loaded from: classes3.dex */
    public class AnonymousClass28 extends AnimatorListenerAdapter {
        AnonymousClass28() {
            ThemePreviewActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ThemePreviewActivity.this.selectedPattern == null) {
                ThemePreviewActivity.this.backgroundCheckBoxView[0].setVisibility(4);
            }
        }
    }

    public void showPatternsView(int i, boolean z, boolean z2) {
        int i2;
        char c = 0;
        boolean z3 = z && i == 1 && this.selectedPattern != null;
        if (z) {
            if (i == 0) {
                if (this.screenType == 2) {
                    this.previousBackgroundColor = this.backgroundColor;
                    int i3 = this.backgroundGradientColor1;
                    this.previousBackgroundGradientColor1 = i3;
                    int i4 = this.backgroundGradientColor2;
                    this.previousBackgroundGradientColor2 = i4;
                    int i5 = this.backgroundGradientColor3;
                    this.previousBackgroundGradientColor3 = i5;
                    int i6 = this.backupBackgroundRotation;
                    this.previousBackgroundRotation = i6;
                    this.colorPicker.setType(0, false, 4, i5 != 0 ? 4 : i4 != 0 ? 3 : i3 != 0 ? 2 : 1, false, i6, false);
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
                        i2 = 0;
                    } else {
                        i2 = arrayList.indexOf(tLRPC$TL_wallPaper) + (this.screenType == 2 ? 1 : 0);
                    }
                    this.patternsLayoutManager.scrollToPositionWithOffset(i2, (this.patternsListView.getMeasuredWidth() - AndroidUtilities.dp(124.0f)) / 2);
                }
            }
        }
        int i7 = this.screenType;
        if (i7 == 1 || i7 == 2) {
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
        float f3 = 1.0f;
        if (z2) {
            this.patternViewAnimation = new AnimatorSet();
            ArrayList arrayList2 = new ArrayList();
            int i8 = i == 0 ? 1 : 0;
            if (z) {
                this.patternLayout[i].setVisibility(0);
                int i9 = this.screenType;
                if (i9 == 1) {
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
                        arrayList2.add(ObjectAnimator.ofFloat(this.patternLayout[i8], View.ALPHA, 0.0f));
                    }
                    this.colorPicker.hideKeyboard();
                } else if (i9 == 2) {
                    arrayList2.add(ObjectAnimator.ofFloat(this.listView2, View.TRANSLATION_Y, (-this.patternLayout[i].getMeasuredHeight()) + AndroidUtilities.dp(48.0f)));
                    WallpaperCheckBoxView wallpaperCheckBoxView3 = this.backgroundCheckBoxView[2];
                    Property property4 = View.ALPHA;
                    float[] fArr4 = new float[1];
                    fArr4[0] = z3 ? 1.0f : 0.0f;
                    arrayList2.add(ObjectAnimator.ofFloat(wallpaperCheckBoxView3, property4, fArr4));
                    WallpaperCheckBoxView wallpaperCheckBoxView4 = this.backgroundCheckBoxView[0];
                    Property property5 = View.ALPHA;
                    float[] fArr5 = new float[1];
                    if (z3) {
                        f3 = 0.0f;
                    }
                    fArr5[0] = f3;
                    arrayList2.add(ObjectAnimator.ofFloat(wallpaperCheckBoxView4, property5, fArr5));
                    arrayList2.add(ObjectAnimator.ofFloat(this.backgroundImage, View.ALPHA, 0.0f));
                    if (this.patternLayout[i8].getVisibility() == 0) {
                        arrayList2.add(ObjectAnimator.ofFloat(this.patternLayout[i8], View.ALPHA, 0.0f));
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
                        arrayList2.add(ObjectAnimator.ofFloat(this.patternLayout[i8], View.ALPHA, 0.0f));
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
            this.patternViewAnimation.addListener(new AnonymousClass29(z, i8, i, z3));
            this.patternViewAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            this.patternViewAnimation.setDuration(200L);
            this.patternViewAnimation.start();
            return;
        }
        char c2 = i == 0 ? (char) 1 : (char) 0;
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
                    this.patternLayout[c2].setAlpha(0.0f);
                }
                this.colorPicker.hideKeyboard();
            } else if (i10 == 2) {
                this.listView2.setTranslationY((-AndroidUtilities.dp(i == 0 ? 343.0f : 316.0f)) + AndroidUtilities.dp(48.0f));
                this.backgroundCheckBoxView[2].setAlpha(z3 ? 1.0f : 0.0f);
                this.backgroundCheckBoxView[0].setAlpha(z3 ? 0.0f : 1.0f);
                this.backgroundImage.setAlpha(0.0f);
                if (this.patternLayout[c2].getVisibility() == 0) {
                    this.patternLayout[c2].setAlpha(0.0f);
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
                    this.patternLayout[c2].setAlpha(0.0f);
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
        if (z && this.patternLayout[c2].getVisibility() == 0) {
            this.patternLayout[c2].setAlpha(1.0f);
            this.patternLayout[c2].setVisibility(4);
        } else if (!z) {
            this.patternLayout[i].setVisibility(4);
        }
        int i11 = this.screenType;
        if (i11 != 1 && i11 != 2) {
            if (i != 1) {
                return;
            }
            this.patternLayout[c2].setAlpha(0.0f);
            return;
        }
        WallpaperCheckBoxView[] wallpaperCheckBoxViewArr = this.backgroundCheckBoxView;
        if (!z3) {
            c = 2;
        }
        wallpaperCheckBoxViewArr[c].setVisibility(4);
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$29 */
    /* loaded from: classes3.dex */
    public class AnonymousClass29 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$num;
        final /* synthetic */ int val$otherNum;
        final /* synthetic */ boolean val$show;
        final /* synthetic */ boolean val$showMotion;

        AnonymousClass29(boolean z, int i, int i2, boolean z2) {
            ThemePreviewActivity.this = r1;
            this.val$show = z;
            this.val$otherNum = i;
            this.val$num = i2;
            this.val$showMotion = z2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ThemePreviewActivity.this.patternViewAnimation = null;
            if (this.val$show && ThemePreviewActivity.this.patternLayout[this.val$otherNum].getVisibility() == 0) {
                ThemePreviewActivity.this.patternLayout[this.val$otherNum].setAlpha(1.0f);
                ThemePreviewActivity.this.patternLayout[this.val$otherNum].setVisibility(4);
            } else if (!this.val$show) {
                ThemePreviewActivity.this.patternLayout[this.val$num].setVisibility(4);
            }
            char c = 2;
            if (ThemePreviewActivity.this.screenType == 1 || ThemePreviewActivity.this.screenType == 2) {
                WallpaperCheckBoxView[] wallpaperCheckBoxViewArr = ThemePreviewActivity.this.backgroundCheckBoxView;
                if (this.val$showMotion) {
                    c = 0;
                }
                wallpaperCheckBoxViewArr[c].setVisibility(4);
            } else if (this.val$num != 1) {
            } else {
                ThemePreviewActivity.this.patternLayout[this.val$otherNum].setAlpha(0.0f);
            }
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
        this.motionAnimation.addListener(new AnonymousClass30());
        this.motionAnimation.start();
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$30 */
    /* loaded from: classes3.dex */
    public class AnonymousClass30 extends AnimatorListenerAdapter {
        AnonymousClass30() {
            ThemePreviewActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ThemePreviewActivity.this.motionAnimation = null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0074, code lost:
        if (r19.backgroundGradientColor1 != 0) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0076, code lost:
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0090, code lost:
        if (r1 != 0) goto L32;
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
                    i = Theme.getColor("key_chat_wallpaper_gradient_to2");
                }
            } else if (i2 == 1) {
                i = Theme.getDefaultAccentColor("key_chat_wallpaper_gradient_to2");
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
                    int defaultAccentColor = Theme.getDefaultAccentColor("chat_wallpaper_gradient_to");
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
                    this.backgroundPlayViewAnimator.addListener(new AnonymousClass31());
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
            if (z4) {
                return;
            }
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
                this.messagesPlayViewAnimator.addListener(new AnonymousClass32());
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

    /* renamed from: org.telegram.ui.ThemePreviewActivity$31 */
    /* loaded from: classes3.dex */
    public class AnonymousClass31 extends AnimatorListenerAdapter {
        AnonymousClass31() {
            ThemePreviewActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ThemePreviewActivity.this.backgroundPlayAnimationView.getTag() == null) {
                ThemePreviewActivity.this.backgroundPlayAnimationView.setVisibility(4);
            }
            ThemePreviewActivity.this.backgroundPlayViewAnimator = null;
        }
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$32 */
    /* loaded from: classes3.dex */
    public class AnonymousClass32 extends AnimatorListenerAdapter {
        AnonymousClass32() {
            ThemePreviewActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ThemePreviewActivity.this.messagesPlayAnimationView.getTag() == null) {
                ThemePreviewActivity.this.messagesPlayAnimationView.setVisibility(4);
            }
            ThemePreviewActivity.this.messagesPlayViewAnimator = null;
        }
    }

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
        if (!Theme.hasThemeKey("chat_serviceBackground") || (this.backgroundImage.getBackground() instanceof MotionBackgroundDrawable)) {
            int i4 = this.checkColor;
            Theme.applyChatServiceMessageColor(new int[]{i4, i4, i4, i4}, this.backgroundImage.getBackground());
        } else if (Theme.getCachedWallpaperNonBlocking() instanceof MotionBackgroundDrawable) {
            int color = Theme.getColor("chat_serviceBackground");
            Theme.applyChatServiceMessageColor(new int[]{color, color, color, color}, this.backgroundImage.getBackground());
        }
        ImageView imageView = this.backgroundPlayAnimationImageView;
        if (imageView != null) {
            imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_serviceText"), PorterDuff.Mode.MULTIPLY));
        }
        ImageView imageView2 = this.messagesPlayAnimationImageView;
        if (imageView2 != null) {
            imageView2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_serviceText"), PorterDuff.Mode.MULTIPLY));
        }
        BackupImageView backupImageView = this.backgroundImage;
        if (backupImageView != null) {
            backupImageView.getImageReceiver().setColorFilter(new PorterDuffColorFilter(this.patternColor, this.blendMode));
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
            for (int i5 = 0; i5 < childCount; i5++) {
                this.backgroundButtonsContainer.getChildAt(i5).invalidate();
            }
        }
        FrameLayout frameLayout2 = this.messagesButtonsContainer;
        if (frameLayout2 != null) {
            int childCount2 = frameLayout2.getChildCount();
            for (int i6 = 0; i6 < childCount2; i6++) {
                this.messagesButtonsContainer.getChildAt(i6).invalidate();
            }
        }
        RadialProgress2 radialProgress2 = this.radialProgress;
        if (radialProgress2 != null) {
            radialProgress2.setColors("chat_serviceBackground", "chat_serviceBackground", "chat_serviceText", "chat_serviceText");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setCurrentImage(boolean z) {
        MotionBackgroundDrawable motionBackgroundDrawable;
        MotionBackgroundDrawable motionBackgroundDrawable2;
        int i = this.screenType;
        if (i == 0 && this.accent == null) {
            this.backgroundImage.setBackground(Theme.getCachedWallpaper());
        } else {
            TLRPC$PhotoSize tLRPC$PhotoSize = null;
            if (i == 2) {
                Object obj = this.currentWallpaper;
                if (obj instanceof TLRPC$TL_wallPaper) {
                    TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) obj;
                    if (z) {
                        tLRPC$PhotoSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$TL_wallPaper.document.thumbs, 100);
                    }
                    this.backgroundImage.setImage(ImageLocation.getForDocument(tLRPC$TL_wallPaper.document), this.imageFilter, ImageLocation.getForDocument(tLRPC$PhotoSize, tLRPC$TL_wallPaper.document), "100_100_b", "jpg", tLRPC$TL_wallPaper.document.size, 1, tLRPC$TL_wallPaper);
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
                        BackupImageView backupImageView = this.backgroundImage;
                        ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$TL_wallPaper2.document);
                        String str = this.imageFilter;
                        TLRPC$TL_wallPaper tLRPC$TL_wallPaper3 = this.selectedPattern;
                        backupImageView.setImage(forDocument, str, null, null, "jpg", tLRPC$TL_wallPaper3.document.size, 1, tLRPC$TL_wallPaper3);
                    } else if ("d".equals(colorWallpaper.slug)) {
                        Point point = AndroidUtilities.displaySize;
                        int min = Math.min(point.x, point.y);
                        Point point2 = AndroidUtilities.displaySize;
                        this.backgroundImage.setImageBitmap(SvgHelper.getBitmap(2131558436, min, Math.max(point2.x, point2.y), Build.VERSION.SDK_INT >= 29 ? 1459617792 : MotionBackgroundDrawable.getPatternColor(colorWallpaper.color, colorWallpaper.gradientColor1, colorWallpaper.gradientColor2, colorWallpaper.gradientColor3)));
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
                                BackupImageView backupImageView2 = this.backgroundImage;
                                backupImageView2.setImageDrawable(Theme.getThemedWallpaper(false, backupImageView2));
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
                        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Photo.sizes, 100);
                        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(searchImage.photo.sizes, this.maxWallpaperSize, true);
                        if (closestPhotoSizeWithSize2 != closestPhotoSizeWithSize) {
                            tLRPC$PhotoSize = closestPhotoSizeWithSize2;
                        }
                        this.backgroundImage.setImage(ImageLocation.getForPhoto(tLRPC$PhotoSize, searchImage.photo), this.imageFilter, ImageLocation.getForPhoto(closestPhotoSizeWithSize, searchImage.photo), "100_100_b", "jpg", tLRPC$PhotoSize != null ? tLRPC$PhotoSize.size : 0, 1, searchImage);
                    } else {
                        this.backgroundImage.setImage(searchImage.imageUrl, this.imageFilter, searchImage.thumbUrl, "100_100_b");
                    }
                }
            } else {
                BackgroundGradientDrawable.Disposable disposable = this.backgroundGradientDisposable;
                if (disposable != null) {
                    disposable.dispose();
                    this.backgroundGradientDisposable = null;
                }
                int defaultAccentColor = Theme.getDefaultAccentColor("chat_wallpaper");
                int i4 = (int) this.accent.backgroundOverrideColor;
                if (i4 != 0) {
                    defaultAccentColor = i4;
                }
                int defaultAccentColor2 = Theme.getDefaultAccentColor("chat_wallpaper_gradient_to");
                long j = this.accent.backgroundGradientOverrideColor1;
                int i5 = (int) j;
                if (i5 == 0 && j != 0) {
                    defaultAccentColor2 = 0;
                } else if (i5 != 0) {
                    defaultAccentColor2 = i5;
                }
                int defaultAccentColor3 = Theme.getDefaultAccentColor("key_chat_wallpaper_gradient_to2");
                long j2 = this.accent.backgroundGradientOverrideColor2;
                int i6 = (int) j2;
                if (i6 == 0 && j2 != 0) {
                    defaultAccentColor3 = 0;
                } else if (i6 != 0) {
                    defaultAccentColor3 = i6;
                }
                int defaultAccentColor4 = Theme.getDefaultAccentColor("key_chat_wallpaper_gradient_to3");
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
                        this.backgroundGradientDisposable = backgroundGradientDrawable.startDithering(BackgroundGradientDrawable.Sizes.ofDeviceScreen(), new AnonymousClass33(), 100L);
                        motionBackgroundDrawable = backgroundGradientDrawable;
                    } else {
                        motionBackgroundDrawable = new ColorDrawable(defaultAccentColor);
                    }
                    this.backgroundImage.setBackground(motionBackgroundDrawable);
                    TLRPC$TL_wallPaper tLRPC$TL_wallPaper4 = this.selectedPattern;
                    if (tLRPC$TL_wallPaper4 != null) {
                        BackupImageView backupImageView3 = this.backgroundImage;
                        ImageLocation forDocument2 = ImageLocation.getForDocument(tLRPC$TL_wallPaper4.document);
                        String str2 = this.imageFilter;
                        TLRPC$TL_wallPaper tLRPC$TL_wallPaper5 = this.selectedPattern;
                        backupImageView3.setImage(forDocument2, str2, null, null, "jpg", tLRPC$TL_wallPaper5.document.size, 1, tLRPC$TL_wallPaper5);
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
                BackupImageView backupImageView4 = this.backgroundImage;
                if (backupImageView4 != null) {
                    backupImageView4.getImageReceiver().setColorFilter(new PorterDuffColorFilter(this.patternColor, this.blendMode));
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
                    imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_serviceText"), PorterDuff.Mode.MULTIPLY));
                }
                ImageView imageView2 = this.messagesPlayAnimationImageView;
                if (imageView2 != null) {
                    imageView2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_serviceText"), PorterDuff.Mode.MULTIPLY));
                }
                FrameLayout frameLayout = this.backgroundButtonsContainer;
                if (frameLayout != null) {
                    int childCount = frameLayout.getChildCount();
                    for (int i9 = 0; i9 < childCount; i9++) {
                        this.backgroundButtonsContainer.getChildAt(i9).invalidate();
                    }
                }
                FrameLayout frameLayout2 = this.messagesButtonsContainer;
                if (frameLayout2 != null) {
                    int childCount2 = frameLayout2.getChildCount();
                    for (int i10 = 0; i10 < childCount2; i10++) {
                        this.messagesButtonsContainer.getChildAt(i10).invalidate();
                    }
                }
            }
        }
        this.rotatePreview = false;
    }

    /* renamed from: org.telegram.ui.ThemePreviewActivity$33 */
    /* loaded from: classes3.dex */
    public class AnonymousClass33 extends BackgroundGradientDrawable.ListenerAdapter {
        AnonymousClass33() {
            ThemePreviewActivity.this = r1;
        }

        @Override // org.telegram.ui.Components.BackgroundGradientDrawable.ListenerAdapter, org.telegram.ui.Components.BackgroundGradientDrawable.Listener
        public void onSizeReady(int i, int i2) {
            Point point = AndroidUtilities.displaySize;
            boolean z = true;
            boolean z2 = point.x <= point.y;
            if (i > i2) {
                z = false;
            }
            if (z2 == z) {
                ThemePreviewActivity.this.backgroundImage.invalidate();
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class DialogsAdapter extends RecyclerListView.SelectionAdapter {
        private ArrayList<DialogCell.CustomDialog> dialogs = new ArrayList<>();
        private Context mContext;

        public DialogsAdapter(Context context) {
            this.mContext = context;
            int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
            DialogCell.CustomDialog customDialog = new DialogCell.CustomDialog();
            customDialog.name = LocaleController.getString("ThemePreviewDialog1", 2131628689);
            customDialog.message = LocaleController.getString("ThemePreviewDialogMessage1", 2131628697);
            customDialog.id = 0;
            customDialog.unread_count = 0;
            customDialog.pinned = true;
            customDialog.muted = false;
            customDialog.type = 0;
            customDialog.date = currentTimeMillis;
            customDialog.verified = false;
            customDialog.isMedia = false;
            customDialog.sent = true;
            this.dialogs.add(customDialog);
            DialogCell.CustomDialog customDialog2 = new DialogCell.CustomDialog();
            customDialog2.name = LocaleController.getString("ThemePreviewDialog2", 2131628690);
            customDialog2.message = LocaleController.getString("ThemePreviewDialogMessage2", 2131628698);
            customDialog2.id = 1;
            customDialog2.unread_count = 2;
            customDialog2.pinned = false;
            customDialog2.muted = false;
            customDialog2.type = 0;
            customDialog2.date = currentTimeMillis - 3600;
            customDialog2.verified = false;
            customDialog2.isMedia = false;
            customDialog2.sent = false;
            this.dialogs.add(customDialog2);
            DialogCell.CustomDialog customDialog3 = new DialogCell.CustomDialog();
            customDialog3.name = LocaleController.getString("ThemePreviewDialog3", 2131628691);
            customDialog3.message = LocaleController.getString("ThemePreviewDialogMessage3", 2131628699);
            customDialog3.id = 2;
            customDialog3.unread_count = 3;
            customDialog3.pinned = false;
            customDialog3.muted = true;
            customDialog3.type = 0;
            customDialog3.date = currentTimeMillis - 7200;
            customDialog3.verified = false;
            customDialog3.isMedia = true;
            customDialog3.sent = false;
            this.dialogs.add(customDialog3);
            DialogCell.CustomDialog customDialog4 = new DialogCell.CustomDialog();
            customDialog4.name = LocaleController.getString("ThemePreviewDialog4", 2131628692);
            customDialog4.message = LocaleController.getString("ThemePreviewDialogMessage4", 2131628700);
            customDialog4.id = 3;
            customDialog4.unread_count = 0;
            customDialog4.pinned = false;
            customDialog4.muted = false;
            customDialog4.type = 2;
            customDialog4.date = currentTimeMillis - 10800;
            customDialog4.verified = false;
            customDialog4.isMedia = false;
            customDialog4.sent = false;
            this.dialogs.add(customDialog4);
            DialogCell.CustomDialog customDialog5 = new DialogCell.CustomDialog();
            customDialog5.name = LocaleController.getString("ThemePreviewDialog5", 2131628693);
            customDialog5.message = LocaleController.getString("ThemePreviewDialogMessage5", 2131628701);
            customDialog5.id = 4;
            customDialog5.unread_count = 0;
            customDialog5.pinned = false;
            customDialog5.muted = false;
            customDialog5.type = 1;
            customDialog5.date = currentTimeMillis - 14400;
            customDialog5.verified = false;
            customDialog5.isMedia = false;
            customDialog5.sent = true;
            this.dialogs.add(customDialog5);
            DialogCell.CustomDialog customDialog6 = new DialogCell.CustomDialog();
            customDialog6.name = LocaleController.getString("ThemePreviewDialog6", 2131628694);
            customDialog6.message = LocaleController.getString("ThemePreviewDialogMessage6", 2131628702);
            customDialog6.id = 5;
            customDialog6.unread_count = 0;
            customDialog6.pinned = false;
            customDialog6.muted = false;
            customDialog6.type = 0;
            customDialog6.date = currentTimeMillis - 18000;
            customDialog6.verified = false;
            customDialog6.isMedia = false;
            customDialog6.sent = false;
            this.dialogs.add(customDialog6);
            DialogCell.CustomDialog customDialog7 = new DialogCell.CustomDialog();
            customDialog7.name = LocaleController.getString("ThemePreviewDialog7", 2131628695);
            customDialog7.message = LocaleController.getString("ThemePreviewDialogMessage7", 2131628703);
            customDialog7.id = 6;
            customDialog7.unread_count = 0;
            customDialog7.pinned = false;
            customDialog7.muted = false;
            customDialog7.type = 0;
            customDialog7.date = currentTimeMillis - 21600;
            customDialog7.verified = true;
            customDialog7.isMedia = false;
            customDialog7.sent = false;
            this.dialogs.add(customDialog7);
            DialogCell.CustomDialog customDialog8 = new DialogCell.CustomDialog();
            customDialog8.name = LocaleController.getString("ThemePreviewDialog8", 2131628696);
            customDialog8.message = LocaleController.getString("ThemePreviewDialogMessage8", 2131628704);
            customDialog8.id = 0;
            customDialog8.unread_count = 0;
            customDialog8.pinned = false;
            customDialog8.muted = false;
            customDialog8.type = 0;
            customDialog8.date = currentTimeMillis - 25200;
            customDialog8.verified = true;
            customDialog8.isMedia = false;
            customDialog8.sent = false;
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
            View view;
            if (i == 0) {
                view = new DialogCell(null, this.mContext, false, false);
            } else {
                view = new LoadingCell(this.mContext);
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0) {
                DialogCell dialogCell = (DialogCell) viewHolder.itemView;
                boolean z = true;
                if (i == getItemCount() - 1) {
                    z = false;
                }
                dialogCell.useSeparator = z;
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
            ThemePreviewActivity.this = r17;
            this.showSecretMessages = r17.screenType == 0 && Utilities.random.nextInt(100) <= 1;
            this.mContext = context;
            this.messages = new ArrayList<>();
            int currentTimeMillis = ((int) (System.currentTimeMillis() / 1000)) - 3600;
            if (r17.screenType != 2) {
                if (r17.screenType == 1) {
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
                    tLRPC$TL_documentAttributeFilename.file_name = LocaleController.getString("NewThemePreviewReply2", 2131626851) + ".mp3";
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
                    MessageObject messageObject = new MessageObject(UserConfig.selectedAccount, tLRPC$TL_message, true, false);
                    if (BuildVars.DEBUG_PRIVATE_VERSION) {
                        TLRPC$TL_message tLRPC$TL_message2 = new TLRPC$TL_message();
                        tLRPC$TL_message2.message = "this is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text";
                        tLRPC$TL_message2.date = currentTimeMillis + 960;
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
                        MessageObject messageObject2 = new MessageObject(UserConfig.selectedAccount, tLRPC$TL_message2, true, false);
                        messageObject2.resetLayout();
                        messageObject2.eventId = 1L;
                        this.messages.add(messageObject2);
                    }
                    TLRPC$TL_message tLRPC$TL_message3 = new TLRPC$TL_message();
                    String string = LocaleController.getString("NewThemePreviewLine3", 2131626848);
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
                    MessageObject messageObject3 = new MessageObject(UserConfig.selectedAccount, tLRPC$TL_message3, true, false);
                    messageObject3.resetLayout();
                    messageObject3.eventId = 1L;
                    this.messages.add(messageObject3);
                    TLRPC$TL_message tLRPC$TL_message4 = new TLRPC$TL_message();
                    tLRPC$TL_message4.message = LocaleController.getString("NewThemePreviewLine1", 2131626847);
                    tLRPC$TL_message4.date = i;
                    tLRPC$TL_message4.dialog_id = 1L;
                    tLRPC$TL_message4.flags = 265;
                    tLRPC$TL_message4.from_id = new TLRPC$TL_peerUser();
                    tLRPC$TL_message4.id = 1;
                    TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = new TLRPC$TL_messageReplyHeader();
                    tLRPC$TL_message4.reply_to = tLRPC$TL_messageReplyHeader;
                    tLRPC$TL_messageReplyHeader.reply_to_msg_id = 5;
                    tLRPC$TL_message4.media = new TLRPC$TL_messageMediaEmpty();
                    tLRPC$TL_message4.out = false;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser7 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message4.peer_id = tLRPC$TL_peerUser7;
                    tLRPC$TL_peerUser7.user_id = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                    MessageObject messageObject4 = new MessageObject(UserConfig.selectedAccount, tLRPC$TL_message4, true, false);
                    messageObject4.customReplyName = LocaleController.getString("NewThemePreviewName", 2131626849);
                    messageObject3.customReplyName = "Test User";
                    messageObject4.eventId = 1L;
                    messageObject4.resetLayout();
                    messageObject4.replyMessageObject = messageObject;
                    messageObject3.replyMessageObject = messageObject4;
                    this.messages.add(messageObject4);
                    this.messages.add(messageObject);
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
                    tLRPC$Document2.mime_type = "audio/ogg";
                    tLRPC$Document2.file_reference = new byte[0];
                    TLRPC$TL_documentAttributeAudio tLRPC$TL_documentAttributeAudio = new TLRPC$TL_documentAttributeAudio();
                    tLRPC$TL_documentAttributeAudio.flags = 1028;
                    tLRPC$TL_documentAttributeAudio.duration = 3;
                    tLRPC$TL_documentAttributeAudio.voice = true;
                    tLRPC$TL_documentAttributeAudio.waveform = new byte[]{0, 4, 17, -50, -93, 86, -103, -45, -12, -26, 63, -25, -3, 109, -114, -54, -4, -1, -1, -1, -1, -29, -1, -1, -25, -1, -1, -97, -43, 57, -57, -108, 1, -91, -4, -47, 21, 99, 10, 97, 43, 45, 115, -112, -77, 51, -63, 66, 40, 34, -122, -116, 48, -124, 16, 66, -120, 16, 68, 16, 33, 4, 1};
                    tLRPC$TL_message5.media.document.attributes.add(tLRPC$TL_documentAttributeAudio);
                    tLRPC$TL_message5.out = true;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser8 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message5.peer_id = tLRPC$TL_peerUser8;
                    tLRPC$TL_peerUser8.user_id = 0L;
                    MessageObject messageObject5 = new MessageObject(((BaseFragment) r17).currentAccount, tLRPC$TL_message5, true, false);
                    messageObject5.audioProgressSec = 1;
                    messageObject5.audioProgress = 0.3f;
                    messageObject5.useCustomPhoto = true;
                    this.messages.add(messageObject5);
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
                    MessagesController.getInstance(((BaseFragment) r17).currentAccount).putUsers(arrayList, true);
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
                    this.messages.add(new MessageObject(((BaseFragment) r17).currentAccount, tLRPC$TL_message6, true, false));
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
                    this.messages.add(new MessageObject(((BaseFragment) r17).currentAccount, tLRPC$TL_message7, true, false));
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
                    this.messages.add(new MessageObject(((BaseFragment) r17).currentAccount, tLRPC$TL_message8, true, false));
                    return;
                } else {
                    TLRPC$TL_message tLRPC$TL_message9 = new TLRPC$TL_message();
                    tLRPC$TL_message9.message = LocaleController.getString("ThemePreviewLine1", 2131628705);
                    int i3 = currentTimeMillis + 60;
                    tLRPC$TL_message9.date = i3;
                    tLRPC$TL_message9.dialog_id = 1L;
                    tLRPC$TL_message9.flags = 259;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser12 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message9.from_id = tLRPC$TL_peerUser12;
                    tLRPC$TL_peerUser12.user_id = UserConfig.getInstance(((BaseFragment) r17).currentAccount).getClientUserId();
                    tLRPC$TL_message9.id = 1;
                    tLRPC$TL_message9.media = new TLRPC$TL_messageMediaEmpty();
                    tLRPC$TL_message9.out = true;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser13 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message9.peer_id = tLRPC$TL_peerUser13;
                    tLRPC$TL_peerUser13.user_id = 0L;
                    MessageObject messageObject6 = new MessageObject(((BaseFragment) r17).currentAccount, tLRPC$TL_message9, true, false);
                    TLRPC$TL_message tLRPC$TL_message10 = new TLRPC$TL_message();
                    tLRPC$TL_message10.message = LocaleController.getString("ThemePreviewLine2", 2131628706);
                    tLRPC$TL_message10.date = currentTimeMillis + 960;
                    tLRPC$TL_message10.dialog_id = 1L;
                    tLRPC$TL_message10.flags = 259;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser14 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message10.from_id = tLRPC$TL_peerUser14;
                    tLRPC$TL_peerUser14.user_id = UserConfig.getInstance(((BaseFragment) r17).currentAccount).getClientUserId();
                    tLRPC$TL_message10.id = 1;
                    tLRPC$TL_message10.media = new TLRPC$TL_messageMediaEmpty();
                    tLRPC$TL_message10.out = true;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser15 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message10.peer_id = tLRPC$TL_peerUser15;
                    tLRPC$TL_peerUser15.user_id = 0L;
                    this.messages.add(new MessageObject(((BaseFragment) r17).currentAccount, tLRPC$TL_message10, true, false));
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
                    tLRPC$TL_documentAttributeAudio2.duration = 243;
                    tLRPC$TL_documentAttributeAudio2.performer = LocaleController.getString("ThemePreviewSongPerformer", 2131628710);
                    tLRPC$TL_documentAttributeAudio2.title = LocaleController.getString("ThemePreviewSongTitle", 2131628711);
                    tLRPC$TL_message11.media.document.attributes.add(tLRPC$TL_documentAttributeAudio2);
                    tLRPC$TL_message11.out = false;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser16 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message11.peer_id = tLRPC$TL_peerUser16;
                    tLRPC$TL_peerUser16.user_id = UserConfig.getInstance(((BaseFragment) r17).currentAccount).getClientUserId();
                    this.messages.add(new MessageObject(((BaseFragment) r17).currentAccount, tLRPC$TL_message11, true, false));
                    TLRPC$TL_message tLRPC$TL_message12 = new TLRPC$TL_message();
                    tLRPC$TL_message12.message = LocaleController.getString("ThemePreviewLine3", 2131628707);
                    tLRPC$TL_message12.date = i3;
                    tLRPC$TL_message12.dialog_id = 1L;
                    tLRPC$TL_message12.flags = 265;
                    tLRPC$TL_message12.from_id = new TLRPC$TL_peerUser();
                    tLRPC$TL_message12.id = 1;
                    TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader2 = new TLRPC$TL_messageReplyHeader();
                    tLRPC$TL_message12.reply_to = tLRPC$TL_messageReplyHeader2;
                    tLRPC$TL_messageReplyHeader2.reply_to_msg_id = 5;
                    tLRPC$TL_message12.media = new TLRPC$TL_messageMediaEmpty();
                    tLRPC$TL_message12.out = false;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser17 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message12.peer_id = tLRPC$TL_peerUser17;
                    tLRPC$TL_peerUser17.user_id = UserConfig.getInstance(((BaseFragment) r17).currentAccount).getClientUserId();
                    MessageObject messageObject7 = new MessageObject(((BaseFragment) r17).currentAccount, tLRPC$TL_message12, true, false);
                    messageObject7.customReplyName = LocaleController.getString("ThemePreviewLine3Reply", 2131628708);
                    messageObject7.replyMessageObject = messageObject6;
                    this.messages.add(messageObject7);
                    TLRPC$TL_message tLRPC$TL_message13 = new TLRPC$TL_message();
                    tLRPC$TL_message13.date = currentTimeMillis + 120;
                    tLRPC$TL_message13.dialog_id = 1L;
                    tLRPC$TL_message13.flags = 259;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser18 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message13.from_id = tLRPC$TL_peerUser18;
                    tLRPC$TL_peerUser18.user_id = UserConfig.getInstance(((BaseFragment) r17).currentAccount).getClientUserId();
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
                    tLRPC$TL_documentAttributeAudio3.duration = 3;
                    tLRPC$TL_documentAttributeAudio3.voice = true;
                    tLRPC$TL_documentAttributeAudio3.waveform = new byte[]{0, 4, 17, -50, -93, 86, -103, -45, -12, -26, 63, -25, -3, 109, -114, -54, -4, -1, -1, -1, -1, -29, -1, -1, -25, -1, -1, -97, -43, 57, -57, -108, 1, -91, -4, -47, 21, 99, 10, 97, 43, 45, 115, -112, -77, 51, -63, 66, 40, 34, -122, -116, 48, -124, 16, 66, -120, 16, 68, 16, 33, 4, 1};
                    tLRPC$TL_message13.media.document.attributes.add(tLRPC$TL_documentAttributeAudio3);
                    tLRPC$TL_message13.out = true;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser19 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message13.peer_id = tLRPC$TL_peerUser19;
                    tLRPC$TL_peerUser19.user_id = 0L;
                    MessageObject messageObject8 = new MessageObject(((BaseFragment) r17).currentAccount, tLRPC$TL_message13, true, false);
                    messageObject8.audioProgressSec = 1;
                    messageObject8.audioProgress = 0.3f;
                    messageObject8.useCustomPhoto = true;
                    this.messages.add(messageObject8);
                    this.messages.add(messageObject6);
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
                    tLRPC$TL_message14.message = LocaleController.getString("ThemePreviewLine4", 2131628709);
                    tLRPC$TL_message14.out = false;
                    TLRPC$TL_peerUser tLRPC$TL_peerUser20 = new TLRPC$TL_peerUser();
                    tLRPC$TL_message14.peer_id = tLRPC$TL_peerUser20;
                    tLRPC$TL_peerUser20.user_id = UserConfig.getInstance(((BaseFragment) r17).currentAccount).getClientUserId();
                    MessageObject messageObject9 = new MessageObject(((BaseFragment) r17).currentAccount, tLRPC$TL_message14, true, false);
                    messageObject9.useCustomPhoto = true;
                    this.messages.add(messageObject9);
                    return;
                }
            }
            TLRPC$TL_message tLRPC$TL_message15 = new TLRPC$TL_message();
            if (r17.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                tLRPC$TL_message15.message = LocaleController.getString("BackgroundColorSinglePreviewLine2", 2131624656);
            } else {
                tLRPC$TL_message15.message = LocaleController.getString("BackgroundPreviewLine2", 2131624664);
            }
            int i4 = currentTimeMillis + 60;
            tLRPC$TL_message15.date = i4;
            tLRPC$TL_message15.dialog_id = 1L;
            tLRPC$TL_message15.flags = 259;
            TLRPC$TL_peerUser tLRPC$TL_peerUser21 = new TLRPC$TL_peerUser();
            tLRPC$TL_message15.from_id = tLRPC$TL_peerUser21;
            tLRPC$TL_peerUser21.user_id = UserConfig.getInstance(((BaseFragment) r17).currentAccount).getClientUserId();
            tLRPC$TL_message15.id = 1;
            tLRPC$TL_message15.media = new TLRPC$TL_messageMediaEmpty();
            tLRPC$TL_message15.out = true;
            TLRPC$TL_peerUser tLRPC$TL_peerUser22 = new TLRPC$TL_peerUser();
            tLRPC$TL_message15.peer_id = tLRPC$TL_peerUser22;
            tLRPC$TL_peerUser22.user_id = 0L;
            MessageObject messageObject10 = new MessageObject(((BaseFragment) r17).currentAccount, tLRPC$TL_message15, true, false);
            messageObject10.eventId = 1L;
            messageObject10.resetLayout();
            this.messages.add(messageObject10);
            TLRPC$TL_message tLRPC$TL_message16 = new TLRPC$TL_message();
            if (r17.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                tLRPC$TL_message16.message = LocaleController.getString("BackgroundColorSinglePreviewLine1", 2131624655);
            } else {
                tLRPC$TL_message16.message = LocaleController.getString("BackgroundPreviewLine1", 2131624663);
            }
            tLRPC$TL_message16.date = i4;
            tLRPC$TL_message16.dialog_id = 1L;
            tLRPC$TL_message16.flags = 265;
            tLRPC$TL_message16.from_id = new TLRPC$TL_peerUser();
            tLRPC$TL_message16.id = 1;
            tLRPC$TL_message16.media = new TLRPC$TL_messageMediaEmpty();
            tLRPC$TL_message16.out = false;
            TLRPC$TL_peerUser tLRPC$TL_peerUser23 = new TLRPC$TL_peerUser();
            tLRPC$TL_message16.peer_id = tLRPC$TL_peerUser23;
            tLRPC$TL_peerUser23.user_id = UserConfig.getInstance(((BaseFragment) r17).currentAccount).getClientUserId();
            MessageObject messageObject11 = new MessageObject(((BaseFragment) r17).currentAccount, tLRPC$TL_message16, true, false);
            messageObject11.eventId = 1L;
            messageObject11.resetLayout();
            this.messages.add(messageObject11);
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

        /* renamed from: org.telegram.ui.ThemePreviewActivity$MessagesAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 implements Theme.ResourcesProvider {
            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public /* synthetic */ void applyServiceShaderMatrix(int i, int i2, float f, float f2) {
                Theme.applyServiceShaderMatrix(i, i2, f, f2);
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public /* synthetic */ int getColorOrDefault(String str) {
                return getColor(str);
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public /* synthetic */ Integer getCurrentColor(String str) {
                Integer color;
                color = getColor(str);
                return color;
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public /* synthetic */ Paint getPaint(String str) {
                return Theme.ResourcesProvider.CC.$default$getPaint(this, str);
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public /* synthetic */ boolean hasGradientService() {
                return Theme.ResourcesProvider.CC.$default$hasGradientService(this);
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public /* synthetic */ void setAnimatedColor(String str, int i) {
                Theme.ResourcesProvider.CC.$default$setAnimatedColor(this, str, i);
            }

            AnonymousClass1() {
                MessagesAdapter.this = r1;
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public Integer getColor(String str) {
                return Integer.valueOf(Theme.getColor(str));
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
                return Theme.getThemeDrawable(str);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            FrameLayout frameLayout;
            if (i == 0) {
                ChatMessageCell chatMessageCell = new ChatMessageCell(this.mContext, false, new AnonymousClass1());
                chatMessageCell.setDelegate(new AnonymousClass2(this));
                frameLayout = chatMessageCell;
            } else if (i == 1) {
                ChatActionCell chatActionCell = new ChatActionCell(this.mContext);
                chatActionCell.setDelegate(new AnonymousClass3(this));
                frameLayout = chatActionCell;
            } else if (i == 2) {
                if (ThemePreviewActivity.this.backgroundButtonsContainer.getParent() != null) {
                    ((ViewGroup) ThemePreviewActivity.this.backgroundButtonsContainer.getParent()).removeView(ThemePreviewActivity.this.backgroundButtonsContainer);
                }
                FrameLayout anonymousClass4 = new AnonymousClass4(this, this.mContext);
                anonymousClass4.addView(ThemePreviewActivity.this.backgroundButtonsContainer, LayoutHelper.createFrame(-1, 76, 17));
                frameLayout = anonymousClass4;
            } else {
                if (ThemePreviewActivity.this.messagesButtonsContainer.getParent() != null) {
                    ((ViewGroup) ThemePreviewActivity.this.messagesButtonsContainer.getParent()).removeView(ThemePreviewActivity.this.messagesButtonsContainer);
                }
                FrameLayout anonymousClass5 = new AnonymousClass5(this, this.mContext);
                anonymousClass5.addView(ThemePreviewActivity.this.messagesButtonsContainer, LayoutHelper.createFrame(-1, 76, 17));
                frameLayout = anonymousClass5;
            }
            frameLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(frameLayout);
        }

        /* renamed from: org.telegram.ui.ThemePreviewActivity$MessagesAdapter$2 */
        /* loaded from: classes3.dex */
        class AnonymousClass2 implements ChatMessageCell.ChatMessageCellDelegate {
            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean canDrawOutboundsContent() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$canDrawOutboundsContent(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean canPerformActions() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$canPerformActions(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didLongPress(ChatMessageCell chatMessageCell, float f, float f2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didLongPress(this, chatMessageCell, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didLongPressBotButton(ChatMessageCell chatMessageCell, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didLongPressBotButton(this, chatMessageCell, tLRPC$KeyboardButton);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean didLongPressChannelAvatar(ChatMessageCell chatMessageCell, TLRPC$Chat tLRPC$Chat, int i, float f, float f2) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$didLongPressChannelAvatar(this, chatMessageCell, tLRPC$Chat, i, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean didLongPressUserAvatar(ChatMessageCell chatMessageCell, TLRPC$User tLRPC$User, float f, float f2) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$didLongPressUserAvatar(this, chatMessageCell, tLRPC$User, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressBotButton(ChatMessageCell chatMessageCell, TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressBotButton(this, chatMessageCell, tLRPC$KeyboardButton);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressCancelSendButton(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressCancelSendButton(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressChannelAvatar(ChatMessageCell chatMessageCell, TLRPC$Chat tLRPC$Chat, int i, float f, float f2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressChannelAvatar(this, chatMessageCell, tLRPC$Chat, i, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressCommentButton(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressCommentButton(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressHiddenForward(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressHiddenForward(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressHint(ChatMessageCell chatMessageCell, int i) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressHint(this, chatMessageCell, i);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressImage(ChatMessageCell chatMessageCell, float f, float f2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressImage(this, chatMessageCell, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressInstantButton(ChatMessageCell chatMessageCell, int i) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressInstantButton(this, chatMessageCell, i);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressOther(ChatMessageCell chatMessageCell, float f, float f2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressOther(this, chatMessageCell, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressReaction(ChatMessageCell chatMessageCell, TLRPC$TL_reactionCount tLRPC$TL_reactionCount, boolean z) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressReaction(this, chatMessageCell, tLRPC$TL_reactionCount, z);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressReplyMessage(ChatMessageCell chatMessageCell, int i) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressReplyMessage(this, chatMessageCell, i);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressSideButton(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressSideButton(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressTime(ChatMessageCell chatMessageCell) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressTime(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressUrl(ChatMessageCell chatMessageCell, CharacterStyle characterStyle, boolean z) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressUrl(this, chatMessageCell, characterStyle, z);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressUserAvatar(ChatMessageCell chatMessageCell, TLRPC$User tLRPC$User, float f, float f2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressUserAvatar(this, chatMessageCell, tLRPC$User, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressViaBot(ChatMessageCell chatMessageCell, String str) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressViaBot(this, chatMessageCell, str);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressViaBotNotInline(ChatMessageCell chatMessageCell, long j) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressViaBotNotInline(this, chatMessageCell, j);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didPressVoteButtons(ChatMessageCell chatMessageCell, ArrayList arrayList, int i, int i2, int i3) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didPressVoteButtons(this, chatMessageCell, arrayList, i, i2, i3);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void didStartVideoStream(MessageObject messageObject) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$didStartVideoStream(this, messageObject);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ String getAdminRank(long j) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$getAdminRank(this, j);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ PinchToZoomHelper getPinchToZoomHelper() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$getPinchToZoomHelper(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ TextSelectionHelper.ChatListTextSelectionHelper getTextSelectionHelper() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$getTextSelectionHelper(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean hasSelectedMessages() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$hasSelectedMessages(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void invalidateBlur() {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$invalidateBlur(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean isLandscape() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$isLandscape(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean keyboardIsOpened() {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$keyboardIsOpened(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void needOpenWebView(MessageObject messageObject, String str, String str2, String str3, String str4, int i, int i2) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$needOpenWebView(this, messageObject, str, str2, str3, str4, i, i2);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean needPlayMessage(MessageObject messageObject) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$needPlayMessage(this, messageObject);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void needReloadPolls() {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$needReloadPolls(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void needShowPremiumFeatures(String str) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$needShowPremiumFeatures(this, str);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean onAccessibilityAction(int i, Bundle bundle) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$onAccessibilityAction(this, i, bundle);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void onDiceFinished() {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$onDiceFinished(this);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void setShouldNotRepeatSticker(MessageObject messageObject) {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$setShouldNotRepeatSticker(this, messageObject);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean shouldDrawThreadProgress(ChatMessageCell chatMessageCell) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$shouldDrawThreadProgress(this, chatMessageCell);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ boolean shouldRepeatSticker(MessageObject messageObject) {
                return ChatMessageCell.ChatMessageCellDelegate.CC.$default$shouldRepeatSticker(this, messageObject);
            }

            @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
            public /* synthetic */ void videoTimerReached() {
                ChatMessageCell.ChatMessageCellDelegate.CC.$default$videoTimerReached(this);
            }

            AnonymousClass2(MessagesAdapter messagesAdapter) {
            }
        }

        /* renamed from: org.telegram.ui.ThemePreviewActivity$MessagesAdapter$3 */
        /* loaded from: classes3.dex */
        class AnonymousClass3 implements ChatActionCell.ChatActionCellDelegate {
            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void didClickImage(ChatActionCell chatActionCell) {
                ChatActionCell.ChatActionCellDelegate.CC.$default$didClickImage(this, chatActionCell);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ boolean didLongPress(ChatActionCell chatActionCell, float f, float f2) {
                return ChatActionCell.ChatActionCellDelegate.CC.$default$didLongPress(this, chatActionCell, f, f2);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void didOpenPremiumGift(ChatActionCell chatActionCell, TLRPC$TL_premiumGiftOption tLRPC$TL_premiumGiftOption, boolean z) {
                ChatActionCell.ChatActionCellDelegate.CC.$default$didOpenPremiumGift(this, chatActionCell, tLRPC$TL_premiumGiftOption, z);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void didPressReplyMessage(ChatActionCell chatActionCell, int i) {
                ChatActionCell.ChatActionCellDelegate.CC.$default$didPressReplyMessage(this, chatActionCell, i);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void needOpenInviteLink(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported) {
                ChatActionCell.ChatActionCellDelegate.CC.$default$needOpenInviteLink(this, tLRPC$TL_chatInviteExported);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void needOpenUserProfile(long j) {
                ChatActionCell.ChatActionCellDelegate.CC.$default$needOpenUserProfile(this, j);
            }

            @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
            public /* synthetic */ void needShowEffectOverlay(ChatActionCell chatActionCell, TLRPC$Document tLRPC$Document, TLRPC$VideoSize tLRPC$VideoSize) {
                ChatActionCell.ChatActionCellDelegate.CC.$default$needShowEffectOverlay(this, chatActionCell, tLRPC$Document, tLRPC$VideoSize);
            }

            AnonymousClass3(MessagesAdapter messagesAdapter) {
            }
        }

        /* renamed from: org.telegram.ui.ThemePreviewActivity$MessagesAdapter$4 */
        /* loaded from: classes3.dex */
        class AnonymousClass4 extends FrameLayout {
            AnonymousClass4(MessagesAdapter messagesAdapter, Context context) {
                super(context);
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60.0f), 1073741824));
            }
        }

        /* renamed from: org.telegram.ui.ThemePreviewActivity$MessagesAdapter$5 */
        /* loaded from: classes3.dex */
        class AnonymousClass5 extends FrameLayout {
            AnonymousClass5(MessagesAdapter messagesAdapter, Context context) {
                super(context);
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60.0f), 1073741824));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            boolean z;
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
                boolean z2 = false;
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
                            }
                        }
                        chatMessageCell.isChat = this.showSecretMessages;
                        chatMessageCell.setFullyDraw(true);
                        chatMessageCell.setMessageObject(messageObject2, null, z, z2);
                    }
                }
                z = false;
                if (itemViewType3 == viewHolder.getItemViewType()) {
                    messageObject = this.messages.get(i3);
                    if (!(messageObject.messageOwner.reply_markup instanceof TLRPC$TL_replyInlineMarkup)) {
                        z2 = true;
                    }
                }
                chatMessageCell.isChat = this.showSecretMessages;
                chatMessageCell.setFullyDraw(true);
                chatMessageCell.setMessageObject(messageObject2, null, z, z2);
            } else if (!(view instanceof ChatActionCell)) {
            } else {
                ChatActionCell chatActionCell = (ChatActionCell) view;
                chatActionCell.setMessageObject(messageObject2);
                chatActionCell.setAlpha(1.0f);
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
            ThemePreviewActivity.this = r1;
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (ThemePreviewActivity.this.patterns != null) {
                return ThemePreviewActivity.this.patterns.size();
            }
            return 0;
        }

        /* renamed from: org.telegram.ui.ThemePreviewActivity$PatternsAdapter$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 implements PatternCell.PatternCellDelegate {
            AnonymousClass1() {
                PatternsAdapter.this = r1;
            }

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
                int defaultAccentColor = Theme.getDefaultAccentColor("chat_wallpaper");
                int i = (int) ThemePreviewActivity.this.accent.backgroundOverrideColor;
                return i != 0 ? i : defaultAccentColor;
            }

            @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
            public int getBackgroundGradientColor1() {
                if (ThemePreviewActivity.this.screenType == 2) {
                    return ThemePreviewActivity.this.backgroundGradientColor1;
                }
                int defaultAccentColor = Theme.getDefaultAccentColor("chat_wallpaper_gradient_to");
                int i = (int) ThemePreviewActivity.this.accent.backgroundGradientOverrideColor1;
                return i != 0 ? i : defaultAccentColor;
            }

            @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
            public int getBackgroundGradientColor2() {
                if (ThemePreviewActivity.this.screenType == 2) {
                    return ThemePreviewActivity.this.backgroundGradientColor2;
                }
                int defaultAccentColor = Theme.getDefaultAccentColor("key_chat_wallpaper_gradient_to2");
                int i = (int) ThemePreviewActivity.this.accent.backgroundGradientOverrideColor2;
                return i != 0 ? i : defaultAccentColor;
            }

            @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
            public int getBackgroundGradientColor3() {
                if (ThemePreviewActivity.this.screenType == 2) {
                    return ThemePreviewActivity.this.backgroundGradientColor3;
                }
                int defaultAccentColor = Theme.getDefaultAccentColor("key_chat_wallpaper_gradient_to3");
                int i = (int) ThemePreviewActivity.this.accent.backgroundGradientOverrideColor3;
                return i != 0 ? i : defaultAccentColor;
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
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RecyclerListView.Holder(new PatternCell(this.mContext, ThemePreviewActivity.this.maxWallpaperSize, new AnonymousClass1()));
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
                    int defaultAccentColor = Theme.getDefaultAccentColor("key_chat_wallpaper_gradient_to2");
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

    private List<ThemeDescription> getThemeDescriptionsInternal() {
        ThemePreviewActivity$$ExternalSyntheticLambda22 themePreviewActivity$$ExternalSyntheticLambda22 = new ThemePreviewActivity$$ExternalSyntheticLambda22(this);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.page1, ThemeDescription.FLAG_BACKGROUND, null, null, null, themePreviewActivity$$ExternalSyntheticLambda22, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.viewPager, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"));
        arrayList.add(new ThemeDescription(this.actionBar2, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar2, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar2, ThemeDescription.FLAG_AB_SUBTITLECOLOR, null, null, null, null, "actionBarDefaultSubtitle"));
        arrayList.add(new ThemeDescription(this.actionBar2, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.actionBar2, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, themePreviewActivity$$ExternalSyntheticLambda22, "actionBarDefaultSubmenuBackground"));
        arrayList.add(new ThemeDescription(this.actionBar2, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, themePreviewActivity$$ExternalSyntheticLambda22, "actionBarDefaultSubmenuItem"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView2, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionIcon"));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "chats_actionBackground"));
        arrayList.add(new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "chats_actionPressedBackground"));
        if (!this.useDefaultThemeForButtons) {
            arrayList.add(new ThemeDescription(this.saveButtonsContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"));
            arrayList.add(new ThemeDescription(this.cancelButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "chat_fieldOverlayText"));
            arrayList.add(new ThemeDescription(this.doneButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "chat_fieldOverlayText"));
        }
        ColorPicker colorPicker = this.colorPicker;
        if (colorPicker != null) {
            colorPicker.provideThemeDescriptions(arrayList);
        }
        if (this.patternLayout != null) {
            for (int i = 0; i < this.patternLayout.length; i++) {
                arrayList.add(new ThemeDescription(this.patternLayout[i], 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, "chat_messagePanelShadow"));
                arrayList.add(new ThemeDescription(this.patternLayout[i], 0, null, Theme.chat_composeBackgroundPaint, null, null, "chat_messagePanelBackground"));
            }
            for (int i2 = 0; i2 < this.patternsButtonsContainer.length; i2++) {
                arrayList.add(new ThemeDescription(this.patternsButtonsContainer[i2], 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, "chat_messagePanelShadow"));
                arrayList.add(new ThemeDescription(this.patternsButtonsContainer[i2], 0, null, Theme.chat_composeBackgroundPaint, null, null, "chat_messagePanelBackground"));
            }
            arrayList.add(new ThemeDescription(this.bottomOverlayChat, 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, "chat_messagePanelShadow"));
            arrayList.add(new ThemeDescription(this.bottomOverlayChat, 0, null, Theme.chat_composeBackgroundPaint, null, null, "chat_messagePanelBackground"));
            arrayList.add(new ThemeDescription(this.bottomOverlayChatText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "chat_fieldOverlayText"));
            for (int i3 = 0; i3 < this.patternsSaveButton.length; i3++) {
                arrayList.add(new ThemeDescription(this.patternsSaveButton[i3], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "chat_fieldOverlayText"));
            }
            for (int i4 = 0; i4 < this.patternsCancelButton.length; i4++) {
                arrayList.add(new ThemeDescription(this.patternsCancelButton[i4], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "chat_fieldOverlayText"));
            }
            arrayList.add(new ThemeDescription(this.intensitySeekBar, 0, new Class[]{SeekBarView.class}, new String[]{"innerPaint1"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "player_progressBackground"));
            arrayList.add(new ThemeDescription(this.intensitySeekBar, 0, new Class[]{SeekBarView.class}, new String[]{"outerPaint1"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "player_progress"));
            arrayList.add(new ThemeDescription(this.intensityCell, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlueHeader"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable}, null, "chat_inBubble"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable}, null, "chat_inBubbleSelected"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, Theme.chat_msgInDrawable.getShadowDrawables(), null, "chat_inBubbleShadow"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, Theme.chat_msgInMediaDrawable.getShadowDrawables(), null, "chat_inBubbleShadow"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{this.msgOutDrawable, this.msgOutMediaDrawable}, null, "chat_outBubble"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{this.msgOutDrawable, this.msgOutMediaDrawable}, null, "chat_outBubbleGradient"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{this.msgOutDrawable, this.msgOutMediaDrawable}, null, "chat_outBubbleGradient2"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{this.msgOutDrawable, this.msgOutMediaDrawable}, null, "chat_outBubbleGradient3"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable}, null, "chat_outBubbleSelected"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, Theme.chat_msgOutDrawable.getShadowDrawables(), null, "chat_outBubbleShadow"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, Theme.chat_msgOutMediaDrawable.getShadowDrawables(), null, "chat_outBubbleShadow"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_messageTextIn"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_messageTextOut"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckDrawable}, null, "chat_outSentCheck"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckSelectedDrawable}, null, "chat_outSentCheckSelected"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckReadDrawable, Theme.chat_msgOutHalfCheckDrawable}, null, "chat_outSentCheckRead"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckReadSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable}, null, "chat_outSentCheckReadSelected"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable}, null, "chat_mediaSentCheck"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_inReplyLine"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_outReplyLine"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_inReplyNameText"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_outReplyNameText"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_inReplyMessageText"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_outReplyMessageText"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_inReplyMediaMessageSelectedText"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_outReplyMediaMessageSelectedText"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_inTimeText"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_outTimeText"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_inTimeSelectedText"));
            arrayList.add(new ThemeDescription(this.listView2, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_outTimeSelectedText"));
        }
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptionsInternal$26() {
        ActionBarMenuItem actionBarMenuItem = this.dropDownContainer;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.redrawPopup(Theme.getColor("actionBarDefaultSubmenuBackground"));
            this.dropDownContainer.setPopupItemsColor(Theme.getColor("actionBarDefaultSubmenuItem"), false);
        }
        Drawable drawable = this.sheetDrawable;
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhite"), PorterDuff.Mode.MULTIPLY));
        }
    }
}
