package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
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
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
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
import org.telegram.ui.Stories.recorder.SliderView;
import org.telegram.ui.ThemePreviewActivity;
import org.telegram.ui.WallpapersListActivity;

/* loaded from: classes4.dex */
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
    public TL_stories.TL_premium_boostsStatus boostsStatus;
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
    private WeakReference lastDrawableToBlur;
    private int lastPickedColor;
    private int lastPickedColorNum;
    private TLRPC.TL_wallPaper lastSelectedPattern;
    private int lastSizeHash;
    private RecyclerListView listView;
    private RecyclerListView listView2;
    private String loadingFile;
    private File loadingFileObject;
    private TLRPC.PhotoSize loadingSize;
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
    private ArrayList patterns;
    private PatternsAdapter patternsAdapter;
    private FrameLayout[] patternsButtonsContainer;
    private TextView[] patternsCancelButton;
    private HashMap patternsDict;
    private LinearLayoutManager patternsLayoutManager;
    private RecyclerListView patternsListView;
    private TextView[] patternsSaveButton;
    private int previousBackgroundColor;
    private int previousBackgroundGradientColor1;
    private int previousBackgroundGradientColor2;
    private int previousBackgroundGradientColor3;
    private int previousBackgroundRotation;
    private float previousIntensity;
    private TLRPC.TL_wallPaper previousSelectedPattern;
    private float progressToDarkTheme;
    private boolean progressVisible;
    private boolean removeBackgroundOverride;
    private boolean rotatePreview;
    private FrameLayout saveButtonsContainer;
    private ActionBarMenuItem saveItem;
    private final int screenType;
    private Scroller scroller;
    private TLRPC.TL_wallPaper selectedPattern;
    boolean self;
    MessageObject serverWallpaper;
    private boolean setupFinished;
    private Drawable sheetDrawable;
    private boolean shouldShowBrightnessControll;
    private boolean shouldShowDayNightIcon;
    private boolean showColor;
    private RLottieDrawable sunDrawable;
    public final ThemeDelegate themeDelegate;
    private List themeDescriptions;
    private UndoView undoView;
    public boolean useDefaultThemeForButtons;
    private ValueAnimator valueAnimator;
    private ViewPager viewPager;
    private boolean wasScroll;
    private long watchForKeyboardEndTime;

    class 27 implements ColorPicker.ColorPickerDelegate {
        27() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$deleteTheme$0(DialogInterface dialogInterface, int i) {
            Theme.deleteThemeAccent(ThemePreviewActivity.this.applyingTheme, ThemePreviewActivity.this.accent, true);
            Theme.applyPreviousTheme();
            Theme.refreshThemeColors();
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, ThemePreviewActivity.this.applyingTheme, Boolean.valueOf(ThemePreviewActivity.this.nightTheme), null, -1);
            ThemePreviewActivity.this.lambda$onBackPressed$321();
        }

        @Override // org.telegram.ui.Components.ColorPicker.ColorPickerDelegate
        public void deleteTheme() {
            if (ThemePreviewActivity.this.getParentActivity() == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ThemePreviewActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString(R.string.DeleteThemeTitle));
            builder.setMessage(LocaleController.getString(R.string.DeleteThemeAlert));
            builder.setPositiveButton(LocaleController.getString(R.string.Delete), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$27$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ThemePreviewActivity.27.this.lambda$deleteTheme$0(dialogInterface, i);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            AlertDialog create = builder.create();
            ThemePreviewActivity.this.showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(ThemePreviewActivity.this.getThemedColor(Theme.key_text_RedBold));
            }
        }

        @Override // org.telegram.ui.Components.ColorPicker.ColorPickerDelegate
        public int getDefaultColor(int i) {
            Theme.ThemeAccent themeAccent;
            if (ThemePreviewActivity.this.colorType == 3 && ThemePreviewActivity.this.applyingTheme.firstAccentIsDefault && i == 0 && (themeAccent = (Theme.ThemeAccent) ThemePreviewActivity.this.applyingTheme.themeAccentsMap.get(Theme.DEFALT_THEME_ACCENT_ID)) != null) {
                return themeAccent.myMessagesAccentColor;
            }
            return 0;
        }

        @Override // org.telegram.ui.Components.ColorPicker.ColorPickerDelegate
        public void openThemeCreate(boolean z) {
            if (!z) {
                AlertsCreator.createThemeCreateDialog(ThemePreviewActivity.this, 1, null, null);
                return;
            }
            if (ThemePreviewActivity.this.accent.info == null) {
                ThemePreviewActivity.this.lambda$onBackPressed$321();
                MessagesController.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).saveThemeToServer(ThemePreviewActivity.this.accent.parentTheme, ThemePreviewActivity.this.accent);
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needShareTheme, ThemePreviewActivity.this.accent.parentTheme, ThemePreviewActivity.this.accent);
                return;
            }
            String str = "https://" + MessagesController.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).linkPrefix + "/addtheme/" + ThemePreviewActivity.this.accent.info.slug;
            ThemePreviewActivity.this.showDialog(new ShareAlert(ThemePreviewActivity.this.getParentActivity(), null, str, false, str, false));
        }

        @Override // org.telegram.ui.Components.ColorPicker.ColorPickerDelegate
        public void setColor(int i, int i2, boolean z) {
            if (ThemePreviewActivity.this.screenType == 2) {
                ThemePreviewActivity.this.setBackgroundColor(i, i2, z, true);
            } else {
                ThemePreviewActivity.this.scheduleApplyColor(i, i2, z);
            }
        }
    }

    class 9 extends ActionBar.ActionBarMenuOnItemClick {
        9() {
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
                    return;
                }
                return;
            }
            if (i >= 1 && i <= 3) {
                ThemePreviewActivity.this.selectColorType(i);
                return;
            }
            if (i == 4) {
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
                ThemePreviewActivity.this.lambda$onBackPressed$321();
                return;
            }
            if (i == 5) {
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
                if (ThemePreviewActivity.this.currentWallpaper instanceof TLRPC.TL_wallPaper) {
                    String str2 = "https://" + MessagesController.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).linkPrefix + "/bg/" + ((TLRPC.TL_wallPaper) ThemePreviewActivity.this.currentWallpaper).slug;
                    if (sb.length() <= 0) {
                        str = str2;
                        ThemePreviewActivity.this.showDialog(new ShareAlert(ThemePreviewActivity.this.getParentActivity(), null, str, false, str, false) { // from class: org.telegram.ui.ThemePreviewActivity.9.1
                            @Override // org.telegram.ui.Components.ShareAlert
                            protected void onSend(LongSparseArray longSparseArray, int i3, TLRPC.TL_forumTopic tL_forumTopic) {
                                if (longSparseArray.size() == 1) {
                                    ThemePreviewActivity.this.undoView.showWithAction(((TLRPC.Dialog) longSparseArray.valueAt(0)).id, 61, Integer.valueOf(i3));
                                } else {
                                    ThemePreviewActivity.this.undoView.showWithAction(0L, 61, Integer.valueOf(i3), Integer.valueOf(longSparseArray.size()), (Runnable) null, (Runnable) null);
                                }
                            }
                        });
                        return;
                    } else {
                        url = str2 + "?mode=" + sb.toString();
                    }
                } else if (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                    WallpapersListActivity.ColorWallpaper colorWallpaper = new WallpapersListActivity.ColorWallpaper(ThemePreviewActivity.this.selectedPattern != null ? ThemePreviewActivity.this.selectedPattern.slug : "c", ThemePreviewActivity.this.backgroundColor, ThemePreviewActivity.this.backgroundGradientColor1, ThemePreviewActivity.this.backgroundGradientColor2, ThemePreviewActivity.this.backgroundGradientColor3, ThemePreviewActivity.this.backgroundRotation, ThemePreviewActivity.this.currentIntensity, ThemePreviewActivity.this.isMotion, null);
                    colorWallpaper.pattern = ThemePreviewActivity.this.selectedPattern;
                    url = colorWallpaper.getUrl();
                } else {
                    if (!BuildVars.DEBUG_PRIVATE_VERSION || (accent = Theme.getActiveTheme().getAccent(false)) == null) {
                        return;
                    }
                    WallpapersListActivity.ColorWallpaper colorWallpaper2 = new WallpapersListActivity.ColorWallpaper(accent.patternSlug, (int) accent.backgroundOverrideColor, (int) accent.backgroundGradientOverrideColor1, (int) accent.backgroundGradientOverrideColor2, (int) accent.backgroundGradientOverrideColor3, accent.backgroundRotation, accent.patternIntensity, accent.patternMotion, null);
                    int size = ThemePreviewActivity.this.patterns.size();
                    while (true) {
                        if (i2 >= size) {
                            break;
                        }
                        TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) ThemePreviewActivity.this.patterns.get(i2);
                        if (tL_wallPaper.pattern && accent.patternSlug.equals(tL_wallPaper.slug)) {
                            colorWallpaper2.pattern = tL_wallPaper;
                            break;
                        }
                        i2++;
                    }
                    url = colorWallpaper2.getUrl();
                }
                str = url;
                ThemePreviewActivity.this.showDialog(new ShareAlert(ThemePreviewActivity.this.getParentActivity(), null, str, false, str, false) { // from class: org.telegram.ui.ThemePreviewActivity.9.1
                    @Override // org.telegram.ui.Components.ShareAlert
                    protected void onSend(LongSparseArray longSparseArray, int i3, TLRPC.TL_forumTopic tL_forumTopic) {
                        if (longSparseArray.size() == 1) {
                            ThemePreviewActivity.this.undoView.showWithAction(((TLRPC.Dialog) longSparseArray.valueAt(0)).id, 61, Integer.valueOf(i3));
                        } else {
                            ThemePreviewActivity.this.undoView.showWithAction(0L, 61, Integer.valueOf(i3), Integer.valueOf(longSparseArray.size()), (Runnable) null, (Runnable) null);
                        }
                    }
                });
                return;
            }
            if (i != 6) {
                if (i == 7 && (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.FileWallpaper) && (file = ((WallpapersListActivity.FileWallpaper) ThemePreviewActivity.this.currentWallpaper).originalPath) != null) {
                    final MediaController.PhotoEntry photoEntry = new MediaController.PhotoEntry(0, 0, 0L, file.getAbsolutePath(), 0, false, 0, 0, 0L);
                    photoEntry.isVideo = false;
                    photoEntry.thumbPath = null;
                    ArrayList arrayList = new ArrayList();
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
                                File file2 = new File(FileLoader.getDirectory(4), Utilities.random.nextInt() + ".jpg");
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
                    return;
                }
                return;
            }
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
                    themePreviewActivity.changeDayNightViewAnimator2 = ValueAnimator.ofFloat(themePreviewActivity.progressToDarkTheme, ThemePreviewActivity.this.onSwitchDayNightDelegate.isDark() ? 1.0f : 0.0f);
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
        public Drawable getBackground() {
            return this.background;
        }

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        protected void onDraw(Canvas canvas) {
            this.tx = 0.0f;
            this.ty = 0.0f;
            if (this.drawBackground) {
                Drawable drawable = this.background;
                if ((drawable instanceof ColorDrawable) || (drawable instanceof GradientDrawable) || (drawable instanceof MotionBackgroundDrawable)) {
                    drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
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
                    }
                }
                this.background.draw(canvas);
            }
            if (ThemePreviewActivity.this.hasScrollingBackground) {
                if (!ThemePreviewActivity.this.scroller.isFinished() && ThemePreviewActivity.this.scroller.computeScrollOffset()) {
                    float startX = ThemePreviewActivity.this.scroller.getStartX();
                    ThemePreviewActivity themePreviewActivity = ThemePreviewActivity.this;
                    if (startX < themePreviewActivity.maxScrollOffset && themePreviewActivity.scroller.getStartX() > 0) {
                        ThemePreviewActivity.this.currentScrollOffset = r1.scroller.getCurrX();
                    }
                    ThemePreviewActivity.this.invalidateBlur();
                    invalidate();
                }
                canvas.save();
                float f2 = -ThemePreviewActivity.this.currentScrollOffset;
                this.tx = f2;
                canvas.translate(f2, 0.0f);
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

    private class BlurButton extends View {
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
                this.rippleDrawable.setState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed});
                z = true;
            } else {
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    this.rippleDrawable.setState(StateSet.NOTHING);
                }
                z = false;
            }
            return super.onTouchEvent(motionEvent) || z;
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

        public void setText(CharSequence charSequence) {
            this.text = new Text(charSequence, 14.0f, AndroidUtilities.bold());
        }

        @Override // android.view.View
        protected boolean verifyDrawable(Drawable drawable) {
            return drawable == this.rippleDrawable || super.verifyDrawable(drawable);
        }
    }

    public interface DayNightSwitchDelegate {
        boolean isDark();

        boolean supportsAnimation();

        void switchDayNight(boolean z);
    }

    public static class DialogsAdapter extends RecyclerListView.SelectionAdapter {
        private ArrayList dialogs = new ArrayList();
        private Context mContext;

        public DialogsAdapter(Context context) {
            this.mContext = context;
            int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
            DialogCell.CustomDialog customDialog = new DialogCell.CustomDialog();
            customDialog.name = LocaleController.getString(R.string.ThemePreviewDialog1);
            customDialog.message = LocaleController.getString(R.string.ThemePreviewDialogMessage1);
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
            customDialog2.name = LocaleController.getString(R.string.ThemePreviewDialog2);
            customDialog2.message = LocaleController.getString(R.string.ThemePreviewDialogMessage2);
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
            customDialog3.name = LocaleController.getString(R.string.ThemePreviewDialog3);
            customDialog3.message = LocaleController.getString(R.string.ThemePreviewDialogMessage3);
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
            customDialog4.name = LocaleController.getString(R.string.ThemePreviewDialog4);
            customDialog4.message = LocaleController.getString(R.string.ThemePreviewDialogMessage4);
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
            customDialog5.name = LocaleController.getString(R.string.ThemePreviewDialog5);
            customDialog5.message = LocaleController.getString(R.string.ThemePreviewDialogMessage5);
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
            customDialog6.name = LocaleController.getString(R.string.ThemePreviewDialog6);
            customDialog6.message = LocaleController.getString(R.string.ThemePreviewDialogMessage6);
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
            customDialog7.name = LocaleController.getString(R.string.ThemePreviewDialog7);
            customDialog7.message = LocaleController.getString(R.string.ThemePreviewDialogMessage7);
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
            customDialog8.name = LocaleController.getString(R.string.ThemePreviewDialog8);
            customDialog8.message = LocaleController.getString(R.string.ThemePreviewDialogMessage8);
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

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return i == this.dialogs.size() ? 1 : 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() != 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0) {
                DialogCell dialogCell = (DialogCell) viewHolder.itemView;
                dialogCell.useSeparator = i != getItemCount() - 1;
                dialogCell.setDialog((DialogCell.CustomDialog) this.dialogs.get(i));
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View dialogCell = i == 0 ? new DialogCell(null, this.mContext, false, false) : new LoadingCell(this.mContext);
            dialogCell.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(dialogCell);
        }
    }

    private class MessageDrawable extends Theme.MessageDrawable {
        public MessageDrawable(int i, boolean z, boolean z2) {
            super(i, z, z2);
        }

        @Override // org.telegram.ui.ActionBar.Theme.MessageDrawable
        public void setTop(int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2) {
            if (ThemePreviewActivity.this.setupFinished) {
                return;
            }
            super.setTop(i, i2, i3, i4, i5, i6, z, z2);
        }

        @Override // org.telegram.ui.ActionBar.Theme.MessageDrawable
        public void setTop(int i, int i2, int i3, boolean z, boolean z2) {
            if (ThemePreviewActivity.this.setupFinished) {
                return;
            }
            super.setTop(i, i2, i3, z, z2);
        }
    }

    public class MessagesAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        private ArrayList messages;
        private boolean showSecretMessages;

        public MessagesAdapter(Context context) {
            MessageObject messageObject;
            ArrayList arrayList;
            MessageObject messageObject2;
            this.showSecretMessages = ThemePreviewActivity.this.screenType == 0 && Utilities.random.nextInt(100) <= 1;
            this.mContext = context;
            this.messages = new ArrayList();
            int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
            int i = currentTimeMillis - 3600;
            if (ThemePreviewActivity.this.screenType == 2) {
                if (ThemePreviewActivity.this.dialogId >= 0) {
                    TLRPC.TL_message tL_message = new TLRPC.TL_message();
                    tL_message.message = LocaleController.getString(ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper ? R.string.BackgroundColorSinglePreviewLine2 : R.string.BackgroundPreviewLine2);
                    tL_message.date = currentTimeMillis - 3540;
                    tL_message.dialog_id = 1L;
                    tL_message.flags = NotificationCenter.closeSearchByActiveAction;
                    tL_message.id = 1;
                    tL_message.media = new TLRPC.TL_messageMediaEmpty();
                    tL_message.out = true;
                    TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
                    tL_message.from_id = tL_peerUser;
                    tL_peerUser.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                    TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
                    tL_message.peer_id = tL_peerUser2;
                    tL_peerUser2.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                    MessageObject messageObject3 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message, true, false) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.1
                        @Override // org.telegram.messenger.MessageObject
                        public boolean needDrawAvatar() {
                            return false;
                        }
                    };
                    messageObject3.eventId = 1L;
                    messageObject3.resetLayout();
                    this.messages.add(messageObject3);
                }
                TLRPC.TL_message tL_message2 = new TLRPC.TL_message();
                TLRPC.Chat chat = ThemePreviewActivity.this.dialogId < 0 ? ThemePreviewActivity.this.getMessagesController().getChat(Long.valueOf(-ThemePreviewActivity.this.dialogId)) : null;
                if (chat != null) {
                    tL_message2.message = LocaleController.getString(R.string.ChannelBackgroundMessagePreview);
                    TLRPC.TL_message tL_message3 = new TLRPC.TL_message();
                    tL_message3.message = LocaleController.getString(R.string.ChannelBackgroundMessageReplyText);
                    TLRPC.Chat chat2 = chat;
                    messageObject2 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message3, true, false) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.2
                        @Override // org.telegram.messenger.MessageObject
                        public boolean needDrawAvatar() {
                            return false;
                        }
                    };
                    TLRPC.TL_peerChannel tL_peerChannel = new TLRPC.TL_peerChannel();
                    tL_message2.from_id = tL_peerChannel;
                    tL_peerChannel.channel_id = chat2.id;
                    TLRPC.TL_peerChannel tL_peerChannel2 = new TLRPC.TL_peerChannel();
                    tL_message2.peer_id = tL_peerChannel2;
                    tL_peerChannel2.channel_id = chat2.id;
                } else {
                    tL_message2.message = LocaleController.getString(ThemePreviewActivity.this.dialogId != 0 ? R.string.BackgroundColorSinglePreviewLine3 : ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper ? R.string.BackgroundColorSinglePreviewLine1 : R.string.BackgroundPreviewLine1);
                    tL_message2.from_id = new TLRPC.TL_peerUser();
                    TLRPC.TL_peerUser tL_peerUser3 = new TLRPC.TL_peerUser();
                    tL_message2.peer_id = tL_peerUser3;
                    tL_peerUser3.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                    messageObject2 = null;
                }
                int i2 = currentTimeMillis - 3540;
                tL_message2.date = i2;
                tL_message2.dialog_id = 1L;
                tL_message2.flags = NotificationCenter.webRtcSpeakerAmplitudeEvent;
                tL_message2.id = 1;
                tL_message2.media = new TLRPC.TL_messageMediaEmpty();
                tL_message2.out = false;
                MessageObject messageObject4 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message2, messageObject2, true, false) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.3
                    @Override // org.telegram.messenger.MessageObject
                    public boolean needDrawAvatar() {
                        return false;
                    }
                };
                if (messageObject2 != null) {
                    messageObject4.customReplyName = LocaleController.getString(R.string.ChannelBackgroundMessageReplyName);
                }
                messageObject4.eventId = 1L;
                messageObject4.resetLayout();
                this.messages.add(messageObject4);
                if (ThemePreviewActivity.this.dialogId == 0 || ThemePreviewActivity.this.serverWallpaper != null) {
                    return;
                }
                TLRPC.User user = ThemePreviewActivity.this.getMessagesController().getUser(Long.valueOf(ThemePreviewActivity.this.dialogId));
                TLRPC.TL_message tL_message4 = new TLRPC.TL_message();
                tL_message4.message = "";
                MessageObject messageObject5 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message4, true, false);
                messageObject5.eventId = 1L;
                messageObject5.contentType = 5;
                this.messages.add(messageObject5);
                TLRPC.TL_message tL_message5 = new TLRPC.TL_message();
                tL_message5.message = user != null ? LocaleController.formatString(R.string.ChatBackgroundHint, UserObject.getFirstName(user)) : LocaleController.getString(R.string.ChannelBackgroundHint);
                tL_message5.date = i2;
                tL_message5.dialog_id = 1L;
                tL_message5.flags = NotificationCenter.webRtcSpeakerAmplitudeEvent;
                tL_message5.from_id = new TLRPC.TL_peerUser();
                tL_message5.id = 1;
                tL_message5.media = new TLRPC.TL_messageMediaEmpty();
                tL_message5.out = false;
                TLRPC.TL_peerUser tL_peerUser4 = new TLRPC.TL_peerUser();
                tL_message5.peer_id = tL_peerUser4;
                tL_peerUser4.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                messageObject = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message5, true, false);
                messageObject.eventId = 1L;
                messageObject.resetLayout();
                messageObject.contentType = 1;
            } else {
                if (ThemePreviewActivity.this.screenType == 1) {
                    TLRPC.TL_message tL_message6 = new TLRPC.TL_message();
                    TLRPC.TL_messageMediaDocument tL_messageMediaDocument = new TLRPC.TL_messageMediaDocument();
                    tL_message6.media = tL_messageMediaDocument;
                    tL_messageMediaDocument.document = new TLRPC.TL_document();
                    TLRPC.Document document = tL_message6.media.document;
                    document.mime_type = "audio/mp3";
                    document.file_reference = new byte[0];
                    document.id = -2147483648L;
                    document.size = 2621440L;
                    document.dc_id = Integer.MIN_VALUE;
                    TLRPC.TL_documentAttributeFilename tL_documentAttributeFilename = new TLRPC.TL_documentAttributeFilename();
                    tL_documentAttributeFilename.file_name = LocaleController.getString(R.string.NewThemePreviewReply2) + ".mp3";
                    tL_message6.media.document.attributes.add(tL_documentAttributeFilename);
                    int i3 = currentTimeMillis + (-3540);
                    tL_message6.date = i3;
                    tL_message6.dialog_id = 1L;
                    tL_message6.flags = NotificationCenter.closeSearchByActiveAction;
                    TLRPC.TL_peerUser tL_peerUser5 = new TLRPC.TL_peerUser();
                    tL_message6.from_id = tL_peerUser5;
                    tL_peerUser5.user_id = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                    tL_message6.id = 1;
                    tL_message6.out = true;
                    TLRPC.TL_peerUser tL_peerUser6 = new TLRPC.TL_peerUser();
                    tL_message6.peer_id = tL_peerUser6;
                    tL_peerUser6.user_id = 0L;
                    MessageObject messageObject6 = new MessageObject(UserConfig.selectedAccount, tL_message6, true, false);
                    if (BuildVars.DEBUG_PRIVATE_VERSION) {
                        TLRPC.TL_message tL_message7 = new TLRPC.TL_message();
                        tL_message7.message = "this is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text\nthis is very very long text";
                        tL_message7.date = currentTimeMillis - 2640;
                        tL_message7.dialog_id = 1L;
                        tL_message7.flags = NotificationCenter.closeSearchByActiveAction;
                        TLRPC.TL_peerUser tL_peerUser7 = new TLRPC.TL_peerUser();
                        tL_message7.from_id = tL_peerUser7;
                        tL_peerUser7.user_id = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                        tL_message7.id = 1;
                        tL_message7.media = new TLRPC.TL_messageMediaEmpty();
                        tL_message7.out = true;
                        TLRPC.TL_peerUser tL_peerUser8 = new TLRPC.TL_peerUser();
                        tL_message7.peer_id = tL_peerUser8;
                        tL_peerUser8.user_id = 0L;
                        MessageObject messageObject7 = new MessageObject(UserConfig.selectedAccount, tL_message7, true, false);
                        messageObject7.resetLayout();
                        messageObject7.eventId = 1L;
                        this.messages.add(messageObject7);
                    }
                    TLRPC.TL_message tL_message8 = new TLRPC.TL_message();
                    String string = LocaleController.getString(R.string.NewThemePreviewLine3);
                    StringBuilder sb = new StringBuilder(string);
                    int indexOf = string.indexOf(42);
                    int lastIndexOf = string.lastIndexOf(42);
                    if (indexOf != -1 && lastIndexOf != -1) {
                        sb.replace(lastIndexOf, lastIndexOf + 1, "");
                        sb.replace(indexOf, indexOf + 1, "");
                        TLRPC.TL_messageEntityTextUrl tL_messageEntityTextUrl = new TLRPC.TL_messageEntityTextUrl();
                        tL_messageEntityTextUrl.offset = indexOf;
                        tL_messageEntityTextUrl.length = (lastIndexOf - indexOf) - 1;
                        tL_messageEntityTextUrl.url = "https://telegram.org";
                        tL_message8.entities.add(tL_messageEntityTextUrl);
                    }
                    tL_message8.message = sb.toString();
                    tL_message8.date = currentTimeMillis - 2640;
                    tL_message8.dialog_id = 1L;
                    tL_message8.flags = NotificationCenter.closeSearchByActiveAction;
                    TLRPC.TL_peerUser tL_peerUser9 = new TLRPC.TL_peerUser();
                    tL_message8.from_id = tL_peerUser9;
                    tL_peerUser9.user_id = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                    tL_message8.id = 1;
                    tL_message8.media = new TLRPC.TL_messageMediaEmpty();
                    tL_message8.out = true;
                    TLRPC.TL_peerUser tL_peerUser10 = new TLRPC.TL_peerUser();
                    tL_message8.peer_id = tL_peerUser10;
                    tL_peerUser10.user_id = 0L;
                    MessageObject messageObject8 = new MessageObject(UserConfig.selectedAccount, tL_message8, true, false);
                    messageObject8.resetLayout();
                    messageObject8.eventId = 1L;
                    this.messages.add(messageObject8);
                    TLRPC.TL_message tL_message9 = new TLRPC.TL_message();
                    tL_message9.message = LocaleController.getString(R.string.NewThemePreviewLine1);
                    tL_message9.date = i3;
                    tL_message9.dialog_id = 1L;
                    tL_message9.flags = NotificationCenter.webRtcSpeakerAmplitudeEvent;
                    tL_message9.from_id = new TLRPC.TL_peerUser();
                    tL_message9.id = 1;
                    TLRPC.TL_messageReplyHeader tL_messageReplyHeader = new TLRPC.TL_messageReplyHeader();
                    tL_message9.reply_to = tL_messageReplyHeader;
                    tL_messageReplyHeader.flags |= 16;
                    tL_messageReplyHeader.reply_to_msg_id = 5;
                    tL_message9.media = new TLRPC.TL_messageMediaEmpty();
                    tL_message9.out = false;
                    TLRPC.TL_peerUser tL_peerUser11 = new TLRPC.TL_peerUser();
                    tL_message9.peer_id = tL_peerUser11;
                    tL_peerUser11.user_id = UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId();
                    MessageObject messageObject9 = new MessageObject(UserConfig.selectedAccount, tL_message9, true, false);
                    messageObject9.customReplyName = LocaleController.getString(R.string.NewThemePreviewName);
                    messageObject8.customReplyName = "Test User";
                    messageObject9.eventId = 1L;
                    messageObject9.resetLayout();
                    messageObject9.replyMessageObject = messageObject6;
                    messageObject8.replyMessageObject = messageObject9;
                    this.messages.add(messageObject9);
                    this.messages.add(messageObject6);
                    TLRPC.TL_message tL_message10 = new TLRPC.TL_message();
                    tL_message10.date = currentTimeMillis - 3480;
                    tL_message10.dialog_id = 1L;
                    tL_message10.flags = NotificationCenter.closeSearchByActiveAction;
                    tL_message10.out = false;
                    tL_message10.from_id = new TLRPC.TL_peerUser();
                    tL_message10.id = 1;
                    TLRPC.TL_messageMediaDocument tL_messageMediaDocument2 = new TLRPC.TL_messageMediaDocument();
                    tL_message10.media = tL_messageMediaDocument2;
                    tL_messageMediaDocument2.flags |= 3;
                    tL_messageMediaDocument2.document = new TLRPC.TL_document();
                    TLRPC.Document document2 = tL_message10.media.document;
                    document2.mime_type = "audio/ogg";
                    document2.file_reference = new byte[0];
                    TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio = new TLRPC.TL_documentAttributeAudio();
                    tL_documentAttributeAudio.flags = 1028;
                    tL_documentAttributeAudio.duration = 3.0d;
                    tL_documentAttributeAudio.voice = true;
                    tL_documentAttributeAudio.waveform = new byte[]{0, 4, 17, -50, -93, 86, -103, -45, -12, -26, 63, -25, -3, 109, -114, -54, -4, -1, -1, -1, -1, -29, -1, -1, -25, -1, -1, -97, -43, 57, -57, -108, 1, -91, -4, -47, 21, 99, 10, 97, 43, 45, 115, -112, -77, 51, -63, 66, 40, 34, -122, -116, 48, -124, 16, 66, -120, 16, 68, 16, 33, 4, 1};
                    tL_message10.media.document.attributes.add(tL_documentAttributeAudio);
                    tL_message10.out = true;
                    TLRPC.TL_peerUser tL_peerUser12 = new TLRPC.TL_peerUser();
                    tL_message10.peer_id = tL_peerUser12;
                    tL_peerUser12.user_id = 0L;
                    MessageObject messageObject10 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message10, true, false);
                    messageObject10.audioProgressSec = 1;
                    messageObject10.audioProgress = 0.3f;
                    messageObject10.useCustomPhoto = true;
                    this.messages.add(messageObject10);
                    return;
                }
                if (this.showSecretMessages) {
                    TLRPC.TL_user tL_user = new TLRPC.TL_user();
                    tL_user.id = 2147483647L;
                    tL_user.first_name = "Me";
                    TLRPC.TL_user tL_user2 = new TLRPC.TL_user();
                    tL_user2.id = 2147483646L;
                    tL_user2.first_name = "Serj";
                    ArrayList<TLRPC.User> arrayList2 = new ArrayList<>();
                    arrayList2.add(tL_user);
                    arrayList2.add(tL_user2);
                    MessagesController.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).putUsers(arrayList2, true);
                    TLRPC.TL_message tL_message11 = new TLRPC.TL_message();
                    tL_message11.message = "Guess why Half-Life 3 was never released.";
                    int i4 = currentTimeMillis - 2640;
                    tL_message11.date = i4;
                    tL_message11.dialog_id = -1L;
                    tL_message11.flags = NotificationCenter.closeSearchByActiveAction;
                    tL_message11.id = 2147483646;
                    tL_message11.media = new TLRPC.TL_messageMediaEmpty();
                    tL_message11.out = false;
                    TLRPC.TL_peerChat tL_peerChat = new TLRPC.TL_peerChat();
                    tL_message11.peer_id = tL_peerChat;
                    tL_peerChat.chat_id = 1L;
                    TLRPC.TL_peerUser tL_peerUser13 = new TLRPC.TL_peerUser();
                    tL_message11.from_id = tL_peerUser13;
                    tL_peerUser13.user_id = tL_user2.id;
                    this.messages.add(new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message11, true, false));
                    TLRPC.TL_message tL_message12 = new TLRPC.TL_message();
                    tL_message12.message = "No.\nAnd every unnecessary ping of the dev delays the release for 10 days.\nEvery request for ETA delays the release for 2 weeks.";
                    tL_message12.date = i4;
                    tL_message12.dialog_id = -1L;
                    tL_message12.flags = NotificationCenter.closeSearchByActiveAction;
                    tL_message12.id = 1;
                    tL_message12.media = new TLRPC.TL_messageMediaEmpty();
                    tL_message12.out = false;
                    TLRPC.TL_peerChat tL_peerChat2 = new TLRPC.TL_peerChat();
                    tL_message12.peer_id = tL_peerChat2;
                    tL_peerChat2.chat_id = 1L;
                    TLRPC.TL_peerUser tL_peerUser14 = new TLRPC.TL_peerUser();
                    tL_message12.from_id = tL_peerUser14;
                    tL_peerUser14.user_id = tL_user2.id;
                    this.messages.add(new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message12, true, false));
                    TLRPC.TL_message tL_message13 = new TLRPC.TL_message();
                    tL_message13.message = "Is source code for Android coming anytime soon?";
                    tL_message13.date = currentTimeMillis - 3000;
                    tL_message13.dialog_id = -1L;
                    tL_message13.flags = NotificationCenter.closeSearchByActiveAction;
                    tL_message13.id = 1;
                    tL_message13.media = new TLRPC.TL_messageMediaEmpty();
                    tL_message13.out = false;
                    TLRPC.TL_peerChat tL_peerChat3 = new TLRPC.TL_peerChat();
                    tL_message13.peer_id = tL_peerChat3;
                    tL_peerChat3.chat_id = 1L;
                    TLRPC.TL_peerUser tL_peerUser15 = new TLRPC.TL_peerUser();
                    tL_message13.from_id = tL_peerUser15;
                    tL_peerUser15.user_id = tL_user.id;
                    arrayList = this.messages;
                    messageObject = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message13, true, false);
                    arrayList.add(messageObject);
                }
                TLRPC.TL_message tL_message14 = new TLRPC.TL_message();
                tL_message14.message = LocaleController.getString(R.string.ThemePreviewLine1);
                int i5 = currentTimeMillis - 3540;
                tL_message14.date = i5;
                tL_message14.dialog_id = 1L;
                tL_message14.flags = NotificationCenter.closeSearchByActiveAction;
                TLRPC.TL_peerUser tL_peerUser16 = new TLRPC.TL_peerUser();
                tL_message14.from_id = tL_peerUser16;
                tL_peerUser16.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                tL_message14.id = 1;
                tL_message14.media = new TLRPC.TL_messageMediaEmpty();
                tL_message14.out = true;
                TLRPC.TL_peerUser tL_peerUser17 = new TLRPC.TL_peerUser();
                tL_message14.peer_id = tL_peerUser17;
                tL_peerUser17.user_id = 0L;
                MessageObject messageObject11 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message14, true, false);
                TLRPC.TL_message tL_message15 = new TLRPC.TL_message();
                tL_message15.message = LocaleController.getString(R.string.ThemePreviewLine2);
                tL_message15.date = currentTimeMillis - 2640;
                tL_message15.dialog_id = 1L;
                tL_message15.flags = NotificationCenter.closeSearchByActiveAction;
                TLRPC.TL_peerUser tL_peerUser18 = new TLRPC.TL_peerUser();
                tL_message15.from_id = tL_peerUser18;
                tL_peerUser18.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                tL_message15.id = 1;
                tL_message15.media = new TLRPC.TL_messageMediaEmpty();
                tL_message15.out = true;
                TLRPC.TL_peerUser tL_peerUser19 = new TLRPC.TL_peerUser();
                tL_message15.peer_id = tL_peerUser19;
                tL_peerUser19.user_id = 0L;
                this.messages.add(new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message15, true, false));
                TLRPC.TL_message tL_message16 = new TLRPC.TL_message();
                tL_message16.date = currentTimeMillis - 3470;
                tL_message16.dialog_id = 1L;
                tL_message16.flags = NotificationCenter.closeSearchByActiveAction;
                tL_message16.from_id = new TLRPC.TL_peerUser();
                tL_message16.id = 5;
                TLRPC.TL_messageMediaDocument tL_messageMediaDocument3 = new TLRPC.TL_messageMediaDocument();
                tL_message16.media = tL_messageMediaDocument3;
                tL_messageMediaDocument3.flags |= 3;
                tL_messageMediaDocument3.document = new TLRPC.TL_document();
                TLRPC.Document document3 = tL_message16.media.document;
                document3.mime_type = "audio/mp4";
                document3.file_reference = new byte[0];
                TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio2 = new TLRPC.TL_documentAttributeAudio();
                tL_documentAttributeAudio2.duration = 243.0d;
                tL_documentAttributeAudio2.performer = LocaleController.getString(R.string.ThemePreviewSongPerformer);
                tL_documentAttributeAudio2.title = LocaleController.getString(R.string.ThemePreviewSongTitle);
                tL_message16.media.document.attributes.add(tL_documentAttributeAudio2);
                tL_message16.out = false;
                TLRPC.TL_peerUser tL_peerUser20 = new TLRPC.TL_peerUser();
                tL_message16.peer_id = tL_peerUser20;
                tL_peerUser20.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                this.messages.add(new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message16, true, false));
                TLRPC.TL_message tL_message17 = new TLRPC.TL_message();
                tL_message17.message = LocaleController.getString(R.string.ThemePreviewLine3);
                tL_message17.date = i5;
                tL_message17.dialog_id = 1L;
                tL_message17.flags = NotificationCenter.webRtcSpeakerAmplitudeEvent;
                tL_message17.from_id = new TLRPC.TL_peerUser();
                tL_message17.id = 1;
                TLRPC.TL_messageReplyHeader tL_messageReplyHeader2 = new TLRPC.TL_messageReplyHeader();
                tL_message17.reply_to = tL_messageReplyHeader2;
                tL_messageReplyHeader2.flags |= 16;
                tL_messageReplyHeader2.reply_to_msg_id = 5;
                tL_message17.media = new TLRPC.TL_messageMediaEmpty();
                tL_message17.out = false;
                TLRPC.TL_peerUser tL_peerUser21 = new TLRPC.TL_peerUser();
                tL_message17.peer_id = tL_peerUser21;
                tL_peerUser21.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                MessageObject messageObject12 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message17, true, false);
                messageObject12.customReplyName = LocaleController.getString(R.string.ThemePreviewLine3Reply);
                messageObject12.replyMessageObject = messageObject11;
                this.messages.add(messageObject12);
                TLRPC.TL_message tL_message18 = new TLRPC.TL_message();
                tL_message18.date = currentTimeMillis - 3480;
                tL_message18.dialog_id = 1L;
                tL_message18.flags = NotificationCenter.closeSearchByActiveAction;
                TLRPC.TL_peerUser tL_peerUser22 = new TLRPC.TL_peerUser();
                tL_message18.from_id = tL_peerUser22;
                tL_peerUser22.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                tL_message18.id = 1;
                TLRPC.TL_messageMediaDocument tL_messageMediaDocument4 = new TLRPC.TL_messageMediaDocument();
                tL_message18.media = tL_messageMediaDocument4;
                tL_messageMediaDocument4.flags |= 3;
                tL_messageMediaDocument4.document = new TLRPC.TL_document();
                TLRPC.Document document4 = tL_message18.media.document;
                document4.mime_type = "audio/ogg";
                document4.file_reference = new byte[0];
                TLRPC.TL_documentAttributeAudio tL_documentAttributeAudio3 = new TLRPC.TL_documentAttributeAudio();
                tL_documentAttributeAudio3.flags = 1028;
                tL_documentAttributeAudio3.duration = 3.0d;
                tL_documentAttributeAudio3.voice = true;
                tL_documentAttributeAudio3.waveform = new byte[]{0, 4, 17, -50, -93, 86, -103, -45, -12, -26, 63, -25, -3, 109, -114, -54, -4, -1, -1, -1, -1, -29, -1, -1, -25, -1, -1, -97, -43, 57, -57, -108, 1, -91, -4, -47, 21, 99, 10, 97, 43, 45, 115, -112, -77, 51, -63, 66, 40, 34, -122, -116, 48, -124, 16, 66, -120, 16, 68, 16, 33, 4, 1};
                tL_message18.media.document.attributes.add(tL_documentAttributeAudio3);
                tL_message18.out = true;
                TLRPC.TL_peerUser tL_peerUser23 = new TLRPC.TL_peerUser();
                tL_message18.peer_id = tL_peerUser23;
                tL_peerUser23.user_id = 0L;
                MessageObject messageObject13 = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message18, true, false);
                messageObject13.audioProgressSec = 1;
                messageObject13.audioProgress = 0.3f;
                messageObject13.useCustomPhoto = true;
                this.messages.add(messageObject13);
                this.messages.add(messageObject11);
                TLRPC.TL_message tL_message19 = new TLRPC.TL_message();
                tL_message19.date = currentTimeMillis - 3590;
                tL_message19.dialog_id = 1L;
                tL_message19.flags = NotificationCenter.notificationsCountUpdated;
                tL_message19.from_id = new TLRPC.TL_peerUser();
                tL_message19.id = 1;
                TLRPC.TL_messageMediaPhoto tL_messageMediaPhoto = new TLRPC.TL_messageMediaPhoto();
                tL_message19.media = tL_messageMediaPhoto;
                tL_messageMediaPhoto.flags |= 3;
                tL_messageMediaPhoto.photo = new TLRPC.TL_photo();
                TLRPC.Photo photo = tL_message19.media.photo;
                photo.file_reference = new byte[0];
                photo.has_stickers = false;
                photo.id = 1L;
                photo.access_hash = 0L;
                photo.date = i;
                TLRPC.TL_photoSize tL_photoSize = new TLRPC.TL_photoSize();
                tL_photoSize.size = 0;
                tL_photoSize.w = 500;
                tL_photoSize.h = NotificationCenter.customTypefacesLoaded;
                tL_photoSize.type = "s";
                tL_photoSize.location = new TLRPC.TL_fileLocationUnavailable();
                tL_message19.media.photo.sizes.add(tL_photoSize);
                tL_message19.message = LocaleController.getString(R.string.ThemePreviewLine4);
                tL_message19.out = false;
                TLRPC.TL_peerUser tL_peerUser24 = new TLRPC.TL_peerUser();
                tL_message19.peer_id = tL_peerUser24;
                tL_peerUser24.user_id = UserConfig.getInstance(((BaseFragment) ThemePreviewActivity.this).currentAccount).getClientUserId();
                messageObject = new MessageObject(((BaseFragment) ThemePreviewActivity.this).currentAccount, tL_message19, true, false);
                messageObject.useCustomPhoto = true;
            }
            arrayList = this.messages;
            arrayList.add(messageObject);
        }

        private boolean hasButtons() {
            if (ThemePreviewActivity.this.messagesButtonsContainer != null && ThemePreviewActivity.this.screenType == 1 && ThemePreviewActivity.this.colorType == 3 && ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2 != 0) {
                return true;
            }
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

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int size = this.messages.size();
            return hasButtons() ? size + 1 : size;
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
            return ((MessageObject) this.messages.get(i)).contentType;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        /* JADX WARN: Removed duplicated region for block: B:32:0x00a3  */
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
            MessageObject messageObject2 = (MessageObject) this.messages.get(i);
            View view = viewHolder.itemView;
            if (!(view instanceof ChatMessageCell)) {
                if (view instanceof ChatActionCell) {
                    ChatActionCell chatActionCell = (ChatActionCell) view;
                    chatActionCell.setMessageObject(messageObject2);
                    chatActionCell.setAlpha(1.0f);
                    ThemePreviewActivity.this.invalidateBlur();
                    return;
                }
                return;
            }
            ChatMessageCell chatMessageCell = (ChatMessageCell) view;
            chatMessageCell.isChat = false;
            int i2 = i - 1;
            int itemViewType2 = getItemViewType(i2);
            int i3 = i + 1;
            int itemViewType3 = getItemViewType(i3);
            if (!(messageObject2.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) && itemViewType2 == viewHolder.getItemViewType()) {
                MessageObject messageObject3 = (MessageObject) this.messages.get(i2);
                if (messageObject3.isOutOwner() == messageObject2.isOutOwner() && Math.abs(messageObject3.messageOwner.date - messageObject2.messageOwner.date) <= 300) {
                    z = true;
                    if (itemViewType3 == viewHolder.getItemViewType() && i3 < this.messages.size()) {
                        messageObject = (MessageObject) this.messages.get(i3);
                        if (!(messageObject.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) && messageObject.isOutOwner() == messageObject2.isOutOwner() && Math.abs(messageObject.messageOwner.date - messageObject2.messageOwner.date) <= 300) {
                            z2 = true;
                            chatMessageCell.isChat = !this.showSecretMessages || ThemePreviewActivity.this.dialogId < 0;
                            chatMessageCell.setFullyDraw(true);
                            chatMessageCell.setMessageObject(messageObject2, null, z, z2);
                        }
                    }
                    z2 = false;
                    chatMessageCell.isChat = !this.showSecretMessages || ThemePreviewActivity.this.dialogId < 0;
                    chatMessageCell.setFullyDraw(true);
                    chatMessageCell.setMessageObject(messageObject2, null, z, z2);
                }
            }
            z = false;
            if (itemViewType3 == viewHolder.getItemViewType()) {
                messageObject = (MessageObject) this.messages.get(i3);
                if (!(messageObject.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup)) {
                    z2 = true;
                    chatMessageCell.isChat = !this.showSecretMessages || ThemePreviewActivity.this.dialogId < 0;
                    chatMessageCell.setFullyDraw(true);
                    chatMessageCell.setMessageObject(messageObject2, null, z, z2);
                }
            }
            z2 = false;
            chatMessageCell.isChat = !this.showSecretMessages || ThemePreviewActivity.this.dialogId < 0;
            chatMessageCell.setFullyDraw(true);
            chatMessageCell.setMessageObject(messageObject2, null, z, z2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r8v19, types: [org.telegram.ui.Cells.ChatActionCell] */
        /* JADX WARN: Type inference failed for: r8v21, types: [org.telegram.ui.Cells.ChatMessageCell] */
        /* JADX WARN: Type inference failed for: r8v9, types: [org.telegram.ui.ThemePreviewActivity$MessagesAdapter$8] */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            FrameLayout frameLayout;
            FrameLayout frameLayout2;
            FrameLayout frameLayout3;
            if (i == 0) {
                ?? chatMessageCell = new ChatMessageCell(this.mContext, ((BaseFragment) ThemePreviewActivity.this).currentAccount, false, null, new Theme.ResourcesProvider() { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.4
                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public void applyServiceShaderMatrix(int i2, int i3, float f, float f2) {
                        ThemeDelegate themeDelegate = ThemePreviewActivity.this.themeDelegate;
                        if (themeDelegate != null) {
                            themeDelegate.applyServiceShaderMatrix(i2, i3, f, f2);
                        } else {
                            Theme.applyServiceShaderMatrix(i2, i3, f, f2);
                        }
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public /* synthetic */ ColorFilter getAnimatedEmojiColorFilter() {
                        ColorFilter colorFilter;
                        colorFilter = Theme.chat_animatedEmojiTextColorFilter;
                        return colorFilter;
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public int getColor(int i2) {
                        return ThemePreviewActivity.this.themeDelegate.getColor(i2);
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public int getColorOrDefault(int i2) {
                        return ThemePreviewActivity.this.themeDelegate.getColorOrDefault(i2);
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public int getCurrentColor(int i2) {
                        return ThemePreviewActivity.this.themeDelegate.getCurrentColor(i2);
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
                        return themeDelegate != null ? themeDelegate.getDrawable(str) : Theme.getThemeDrawable(str);
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
                    public boolean isDark() {
                        return ThemePreviewActivity.this.themeDelegate.isDark();
                    }

                    @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
                    public /* synthetic */ void setAnimatedColor(int i2, int i3) {
                        Theme.ResourcesProvider.-CC.$default$setAnimatedColor(this, i2, i3);
                    }
                });
                chatMessageCell.setDelegate(new ChatMessageCell.ChatMessageCellDelegate() { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.5
                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean canDrawOutboundsContent() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$canDrawOutboundsContent(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean canPerformActions() {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$canPerformActions(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean canPerformReply() {
                        boolean canPerformActions;
                        canPerformActions = canPerformActions();
                        return canPerformActions;
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didLongPress(ChatMessageCell chatMessageCell2, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPress(this, chatMessageCell2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didLongPressBotButton(ChatMessageCell chatMessageCell2, TLRPC.KeyboardButton keyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressBotButton(this, chatMessageCell2, keyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean didLongPressChannelAvatar(ChatMessageCell chatMessageCell2, TLRPC.Chat chat, int i2, float f, float f2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressChannelAvatar(this, chatMessageCell2, chat, i2, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean didLongPressUserAvatar(ChatMessageCell chatMessageCell2, TLRPC.User user, float f, float f2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didLongPressUserAvatar(this, chatMessageCell2, user, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressAboutRevenueSharingAds() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressAboutRevenueSharingAds(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean didPressAnimatedEmoji(ChatMessageCell chatMessageCell2, AnimatedEmojiSpan animatedEmojiSpan) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressAnimatedEmoji(this, chatMessageCell2, animatedEmojiSpan);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressBoostCounter(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressBoostCounter(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressBotButton(ChatMessageCell chatMessageCell2, TLRPC.KeyboardButton keyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressBotButton(this, chatMessageCell2, keyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressCancelSendButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressCancelSendButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressChannelAvatar(ChatMessageCell chatMessageCell2, TLRPC.Chat chat, int i2, float f, float f2, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelAvatar(this, chatMessageCell2, chat, i2, f, f2, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressChannelRecommendation(ChatMessageCell chatMessageCell2, TLRPC.Chat chat, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressChannelRecommendation(this, chatMessageCell2, chat, z);
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
                    public /* synthetic */ void didPressDialogButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressDialogButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressEffect(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressEffect(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressExtendedMediaPreview(ChatMessageCell chatMessageCell2, TLRPC.KeyboardButton keyboardButton) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressExtendedMediaPreview(this, chatMessageCell2, keyboardButton);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressFactCheck(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressFactCheck(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressFactCheckWhat(ChatMessageCell chatMessageCell2, int i2, int i3) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressFactCheckWhat(this, chatMessageCell2, i2, i3);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressGiveawayChatButton(ChatMessageCell chatMessageCell2, int i2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressGiveawayChatButton(this, chatMessageCell2, i2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressGroupImage(ChatMessageCell chatMessageCell2, ImageReceiver imageReceiver, TLRPC.MessageExtendedMedia messageExtendedMedia, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressGroupImage(this, chatMessageCell2, imageReceiver, messageExtendedMedia, f, f2);
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
                    public /* synthetic */ void didPressReaction(ChatMessageCell chatMessageCell2, TLRPC.ReactionCount reactionCount, boolean z, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReaction(this, chatMessageCell2, reactionCount, z, f, f2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressReplyMessage(ChatMessageCell chatMessageCell2, int i2, float f, float f2, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressReplyMessage(this, chatMessageCell2, i2, f, f2, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressRevealSensitiveContent(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressRevealSensitiveContent(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSideButton(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSideButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSponsoredClose(ChatMessageCell chatMessageCell2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSponsoredClose(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressSponsoredInfo(ChatMessageCell chatMessageCell2, float f, float f2) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressSponsoredInfo(this, chatMessageCell2, f, f2);
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
                    public /* synthetic */ void didPressUserAvatar(ChatMessageCell chatMessageCell2, TLRPC.User user, float f, float f2, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserAvatar(this, chatMessageCell2, user, f, f2, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didPressUserStatus(ChatMessageCell chatMessageCell2, TLRPC.User user, TLRPC.Document document) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didPressUserStatus(this, chatMessageCell2, user, document);
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
                    public /* synthetic */ void didPressWebPage(ChatMessageCell chatMessageCell2, TLRPC.WebPage webPage, String str, boolean z) {
                        Browser.openUrl(chatMessageCell2.getContext(), str);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void didStartVideoStream(MessageObject messageObject) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$didStartVideoStream(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean doNotShowLoadingReply(MessageObject messageObject) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$doNotShowLoadingReply(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void forceUpdate(ChatMessageCell chatMessageCell2, boolean z) {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$forceUpdate(this, chatMessageCell2, z);
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
                    public /* synthetic */ boolean shouldDrawThreadProgress(ChatMessageCell chatMessageCell2, boolean z) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldDrawThreadProgress(this, chatMessageCell2, z);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldRepeatSticker(MessageObject messageObject) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldRepeatSticker(this, messageObject);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldShowDialogButton(ChatMessageCell chatMessageCell2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldShowDialogButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ boolean shouldShowTopicButton(ChatMessageCell chatMessageCell2) {
                        return ChatMessageCell.ChatMessageCellDelegate.-CC.$default$shouldShowTopicButton(this, chatMessageCell2);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate
                    public /* synthetic */ void videoTimerReached() {
                        ChatMessageCell.ChatMessageCellDelegate.-CC.$default$videoTimerReached(this);
                    }
                });
                frameLayout3 = chatMessageCell;
            } else if (i == 1) {
                ?? chatActionCell = new ChatActionCell(this.mContext, false, ThemePreviewActivity.this.themeDelegate);
                chatActionCell.setDelegate(new ChatActionCell.ChatActionCellDelegate() { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.6
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
                    public /* synthetic */ void didOpenPremiumGift(ChatActionCell chatActionCell2, TLRPC.TL_premiumGiftOption tL_premiumGiftOption, String str, boolean z) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$didOpenPremiumGift(this, chatActionCell2, tL_premiumGiftOption, str, z);
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
                    public /* synthetic */ void forceUpdate(ChatActionCell chatActionCell2, boolean z) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$forceUpdate(this, chatActionCell2, z);
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
                    public /* synthetic */ long getTopicId() {
                        return ChatActionCell.ChatActionCellDelegate.-CC.$default$getTopicId(this);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void needOpenInviteLink(TLRPC.TL_chatInviteExported tL_chatInviteExported) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$needOpenInviteLink(this, tL_chatInviteExported);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void needOpenUserProfile(long j) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$needOpenUserProfile(this, j);
                    }

                    @Override // org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate
                    public /* synthetic */ void needShowEffectOverlay(ChatActionCell chatActionCell2, TLRPC.Document document, TLRPC.VideoSize videoSize) {
                        ChatActionCell.ChatActionCellDelegate.-CC.$default$needShowEffectOverlay(this, chatActionCell2, document, videoSize);
                    }
                });
                frameLayout3 = chatActionCell;
            } else {
                if (i == 2) {
                    if (ThemePreviewActivity.this.backgroundButtonsContainer.getParent() != null) {
                        ((ViewGroup) ThemePreviewActivity.this.backgroundButtonsContainer.getParent()).removeView(ThemePreviewActivity.this.backgroundButtonsContainer);
                    }
                    FrameLayout frameLayout4 = new FrameLayout(this.mContext) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.7
                        @Override // android.widget.FrameLayout, android.view.View
                        protected void onMeasure(int i2, int i3) {
                            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60.0f), 1073741824));
                        }
                    };
                    frameLayout = ThemePreviewActivity.this.backgroundButtonsContainer;
                    frameLayout2 = frameLayout4;
                } else if (i == 5) {
                    frameLayout3 = new View(ThemePreviewActivity.this.getContext()) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.8
                        @Override // android.view.View
                        protected void onMeasure(int i2, int i3) {
                            super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(4.0f), 1073741824));
                        }
                    };
                } else {
                    if (ThemePreviewActivity.this.messagesButtonsContainer.getParent() != null) {
                        ((ViewGroup) ThemePreviewActivity.this.messagesButtonsContainer.getParent()).removeView(ThemePreviewActivity.this.messagesButtonsContainer);
                    }
                    FrameLayout frameLayout5 = new FrameLayout(this.mContext) { // from class: org.telegram.ui.ThemePreviewActivity.MessagesAdapter.9
                        @Override // android.widget.FrameLayout, android.view.View
                        protected void onMeasure(int i2, int i3) {
                            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(60.0f), 1073741824));
                        }
                    };
                    frameLayout = ThemePreviewActivity.this.messagesButtonsContainer;
                    frameLayout2 = frameLayout5;
                }
                frameLayout2.addView(frameLayout, LayoutHelper.createFrame(-1, 76, 17));
                frameLayout3 = frameLayout2;
            }
            frameLayout3.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder(frameLayout3);
        }
    }

    private class PatternsAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

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
        public int getItemViewType(int i) {
            return 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ImageReceiver imageReceiver;
            BlendMode blendMode;
            PatternCell patternCell = (PatternCell) viewHolder.itemView;
            patternCell.setPattern((TLRPC.TL_wallPaper) ThemePreviewActivity.this.patterns.get(i));
            patternCell.getImageReceiver().setColorFilter(new PorterDuffColorFilter(ThemePreviewActivity.this.patternColor, ThemePreviewActivity.this.blendMode));
            if (Build.VERSION.SDK_INT >= 29) {
                int i2 = 0;
                if (ThemePreviewActivity.this.screenType == 1) {
                    int defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to2);
                    int i3 = (int) ThemePreviewActivity.this.accent.backgroundGradientOverrideColor2;
                    if (i3 != 0 || ThemePreviewActivity.this.accent.backgroundGradientOverrideColor2 == 0) {
                        i2 = i3 != 0 ? i3 : defaultAccentColor;
                    }
                } else if (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                    i2 = ThemePreviewActivity.this.backgroundGradientColor2;
                }
                if (i2 == 0 || ThemePreviewActivity.this.currentIntensity < 0.0f) {
                    imageReceiver = patternCell.getImageReceiver();
                    blendMode = null;
                } else {
                    imageReceiver = ThemePreviewActivity.this.backgroundImage.getImageReceiver();
                    blendMode = BlendMode.SOFT_LIGHT;
                }
                imageReceiver.setBlendMode(blendMode);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RecyclerListView.Holder(new PatternCell(this.mContext, ThemePreviewActivity.this.maxWallpaperSize, new PatternCell.PatternCellDelegate() { // from class: org.telegram.ui.ThemePreviewActivity.PatternsAdapter.1
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
                public int getBackgroundGradientAngle() {
                    return ThemePreviewActivity.this.screenType == 2 ? ThemePreviewActivity.this.backgroundRotation : ThemePreviewActivity.this.accent.backgroundRotation;
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
                public int getCheckColor() {
                    return ThemePreviewActivity.this.checkColor;
                }

                @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
                public float getIntensity() {
                    return ThemePreviewActivity.this.currentIntensity;
                }

                @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
                public int getPatternColor() {
                    return ThemePreviewActivity.this.patternColor;
                }

                @Override // org.telegram.ui.Cells.PatternCell.PatternCellDelegate
                public TLRPC.TL_wallPaper getSelectedPattern() {
                    return ThemePreviewActivity.this.selectedPattern;
                }
            }));
        }
    }

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
            textPaint3.setTypeface(AndroidUtilities.bold());
            paint.setColor(352321536);
        }

        public void applyChatServiceMessageColor() {
            applyChatServiceMessageColor(null, null, null, null);
        }

        public void applyChatServiceMessageColor(int[] iArr, Drawable drawable, Drawable drawable2, Float f) {
            float f2;
            int i = Theme.key_chat_serviceBackground;
            int color = getColor(i);
            int color2 = getColor(Theme.key_chat_serviceBackgroundSelected);
            if (drawable == null) {
                drawable = drawable2;
            }
            boolean z = drawable instanceof MotionBackgroundDrawable;
            if ((z || (drawable instanceof BitmapDrawable)) && SharedConfig.getDevicePerformanceClass() != 0 && LiteMode.isEnabled(32)) {
                Bitmap bitmap = z ? ((MotionBackgroundDrawable) drawable).getBitmap() : drawable instanceof BitmapDrawable ? ((BitmapDrawable) drawable).getBitmap() : null;
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
            if (this.serviceBitmapShader == null || !(this.currentColors.indexOfKey(i) < 0 || z || (drawable instanceof BitmapDrawable))) {
                this.chat_actionBackgroundPaint.setColorFilter(null);
                this.chat_actionBackgroundPaint.setShader(null);
                this.chat_actionBackgroundSelectedPaint.setColorFilter(null);
                this.chat_actionBackgroundSelectedPaint.setShader(null);
                return;
            }
            ColorMatrix colorMatrix = new ColorMatrix();
            if (!z) {
                colorMatrix.setSaturation(1.6f);
                AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, isDark() ? 0.9f : 0.84f);
                f2 = isDark() ? -0.04f : 0.06f;
            } else if (((MotionBackgroundDrawable) drawable).getIntensity() >= 0.0f) {
                colorMatrix.setSaturation(1.6f);
                AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, isDark() ? 0.97f : 0.92f);
                if (isDark()) {
                    f2 = 0.12f;
                }
                f2 = -0.06f;
            } else {
                colorMatrix.setSaturation(1.1f);
                AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, isDark() ? 0.4f : 0.8f);
                if (isDark()) {
                    f2 = 0.08f;
                }
                f2 = -0.06f;
            }
            AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, f2);
            if (z) {
                float intensity = ((MotionBackgroundDrawable) drawable).getIntensity();
                if (f != null) {
                    intensity = f.floatValue();
                }
                if (intensity >= 0.0f) {
                    colorMatrix.setSaturation(1.8f);
                    AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, 0.97f);
                } else {
                    colorMatrix.setSaturation(0.5f);
                    AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, 0.35f);
                }
                AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, 0.03f);
            } else {
                colorMatrix.setSaturation(1.6f);
                AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, 0.97f);
                AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, 0.06f);
            }
            this.chat_actionBackgroundPaint.setShader(this.serviceBitmapShader);
            this.chat_actionBackgroundPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            this.chat_actionBackgroundPaint.setAlpha(NotificationCenter.newLocationAvailable);
            this.chat_actionBackgroundSelectedPaint.setShader(this.serviceBitmapShader);
            ColorMatrix colorMatrix2 = new ColorMatrix(colorMatrix);
            AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix2, 0.85f);
            this.chat_actionBackgroundSelectedPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix2));
            this.chat_actionBackgroundSelectedPaint.setAlpha(NotificationCenter.newLocationAvailable);
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
        public /* synthetic */ ColorFilter getAnimatedEmojiColorFilter() {
            ColorFilter colorFilter;
            colorFilter = Theme.chat_animatedEmojiTextColorFilter;
            return colorFilter;
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public int getColor(int i) {
            Theme.ResourcesProvider resourcesProvider = this.parentProvider;
            return resourcesProvider != null ? resourcesProvider.getColor(i) : Theme.getColor(i);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ int getColorOrDefault(int i) {
            int color;
            color = getColor(i);
            return color;
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public int getCurrentColor(int i) {
            int color;
            Theme.ResourcesProvider resourcesProvider = this.parentProvider;
            if (resourcesProvider != null) {
                return resourcesProvider.getCurrentColor(i);
            }
            color = getColor(i);
            return color;
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public Drawable getDrawable(String str) {
            Theme.ResourcesProvider resourcesProvider = this.parentProvider;
            return resourcesProvider != null ? resourcesProvider.getDrawable(str) : Theme.getThemeDrawable(str);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public Paint getPaint(String str) {
            Paint themePaint;
            str.hashCode();
            switch (str) {
                case "paintChatActionText2":
                    return this.chat_actionTextPaint2;
                case "paintChatActionBackground":
                    return this.chat_actionBackgroundPaint;
                case "paintChatBotButton":
                    return this.chat_botButtonPaint;
                case "paintChatActionBackgroundDarken":
                    return this.chat_actionBackgroundGradientDarkenPaint;
                case "paintChatActionBackgroundSelected":
                    return this.chat_actionBackgroundSelectedPaint;
                case "paintChatActionText":
                    return this.chat_actionTextPaint;
                default:
                    Theme.ResourcesProvider resourcesProvider = this.parentProvider;
                    if (resourcesProvider != null) {
                        return resourcesProvider.getPaint(str);
                    }
                    themePaint = Theme.getThemePaint(str);
                    return themePaint;
            }
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public boolean hasGradientService() {
            Theme.ResourcesProvider resourcesProvider = this.parentProvider;
            return resourcesProvider != null ? resourcesProvider.hasGradientService() : Theme.hasGradientService();
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

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ void setAnimatedColor(int i, int i2) {
            Theme.ResourcesProvider.-CC.$default$setAnimatedColor(this, i, i2);
        }
    }

    public interface WallpaperActivityDelegate {
        void didSetNewBackground(TLRPC.WallPaper wallPaper);
    }

    public ThemePreviewActivity(Object obj, Bitmap bitmap) {
        this(obj, bitmap, false, false);
    }

    public ThemePreviewActivity(Object obj, Bitmap bitmap, boolean z, boolean z2) {
        this.themeDelegate = new ThemeDelegate() { // from class: org.telegram.ui.ThemePreviewActivity.1
            @Override // org.telegram.ui.ThemePreviewActivity.ThemeDelegate, org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public boolean isDark() {
                DayNightSwitchDelegate dayNightSwitchDelegate = ThemePreviewActivity.this.onSwitchDayNightDelegate;
                return dayNightSwitchDelegate != null ? dayNightSwitchDelegate.isDark() : super.isDark();
            }
        };
        this.useDefaultThemeForButtons = true;
        this.colorType = 1;
        this.msgOutDrawable = new MessageDrawable(0, true, false);
        this.msgOutDrawableSelected = new MessageDrawable(0, true, true);
        this.msgOutMediaDrawable = new MessageDrawable(1, true, false);
        this.msgOutMediaDrawableSelected = new MessageDrawable(1, true, true);
        this.lastPickedColorNum = -1;
        this.applyColorAction = new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda0
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
        this.patternsDict = new HashMap();
        this.currentIntensity = 0.5f;
        this.dimAmount = 0.0f;
        this.blendMode = PorterDuff.Mode.SRC_IN;
        this.parallaxScale = 1.0f;
        this.loadingFile = null;
        this.loadingFileObject = null;
        this.loadingSize = null;
        this.imageFilter = "640_360";
        this.maxWallpaperSize = 1920;
        this.self = true;
        this.gestureDetector2 = new GestureDetector2(getContext(), new GestureDetector2.OnGestureListener() { // from class: org.telegram.ui.ThemePreviewActivity.2
            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                if (ThemePreviewActivity.this.scroller == null) {
                    return true;
                }
                ThemePreviewActivity.this.scroller.abortAnimation();
                return true;
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (ThemePreviewActivity.this.scroller == null) {
                    return true;
                }
                ThemePreviewActivity.this.scroller.abortAnimation();
                ThemePreviewActivity.this.scroller.fling((int) ThemePreviewActivity.this.currentScrollOffset, 0, Math.round(-f), Math.round(f2), 0, (int) ThemePreviewActivity.this.maxScrollOffset, 0, ConnectionsManager.DEFAULT_DATACENTER_ID);
                ThemePreviewActivity.this.backgroundImage.postInvalidate();
                return true;
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
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
            public void onShowPress(MotionEvent motionEvent) {
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public void onUp(MotionEvent motionEvent) {
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
            TLRPC.TL_wallPaper tL_wallPaper = colorWallpaper.pattern;
            this.selectedPattern = tL_wallPaper;
            if (tL_wallPaper != null) {
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
                return dayNightSwitchDelegate != null ? dayNightSwitchDelegate.isDark() : super.isDark();
            }
        };
        this.useDefaultThemeForButtons = true;
        this.colorType = 1;
        this.msgOutDrawable = new MessageDrawable(0, true, false);
        this.msgOutDrawableSelected = new MessageDrawable(0, true, true);
        this.msgOutMediaDrawable = new MessageDrawable(1, true, false);
        this.msgOutMediaDrawableSelected = new MessageDrawable(1, true, true);
        this.lastPickedColorNum = -1;
        this.applyColorAction = new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda0
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
        this.patternsDict = new HashMap();
        this.currentIntensity = 0.5f;
        this.dimAmount = 0.0f;
        this.blendMode = PorterDuff.Mode.SRC_IN;
        this.parallaxScale = 1.0f;
        this.loadingFile = null;
        this.loadingFileObject = null;
        this.loadingSize = null;
        this.imageFilter = "640_360";
        this.maxWallpaperSize = 1920;
        this.self = true;
        this.gestureDetector2 = new GestureDetector2(getContext(), new GestureDetector2.OnGestureListener() { // from class: org.telegram.ui.ThemePreviewActivity.2
            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                if (ThemePreviewActivity.this.scroller == null) {
                    return true;
                }
                ThemePreviewActivity.this.scroller.abortAnimation();
                return true;
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (ThemePreviewActivity.this.scroller == null) {
                    return true;
                }
                ThemePreviewActivity.this.scroller.abortAnimation();
                ThemePreviewActivity.this.scroller.fling((int) ThemePreviewActivity.this.currentScrollOffset, 0, Math.round(-f), Math.round(f2), 0, (int) ThemePreviewActivity.this.maxScrollOffset, 0, ConnectionsManager.DEFAULT_DATACENTER_ID);
                ThemePreviewActivity.this.backgroundImage.postInvalidate();
                return true;
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
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
            public void onShowPress(MotionEvent motionEvent) {
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override // org.telegram.ui.Components.GestureDetector2.OnGestureListener
            public void onUp(MotionEvent motionEvent) {
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

    private void animateMotionChange() {
        AnimatorSet animatorSet = this.motionAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.motionAnimation = animatorSet2;
        if (this.isMotion) {
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this.backgroundImage, (Property<BackgroundView, Float>) View.SCALE_X, this.parallaxScale), ObjectAnimator.ofFloat(this.backgroundImage, (Property<BackgroundView, Float>) View.SCALE_Y, this.parallaxScale));
        } else {
            animatorSet2.playTogether(ObjectAnimator.ofFloat(this.backgroundImage, (Property<BackgroundView, Float>) View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.backgroundImage, (Property<BackgroundView, Float>) View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.backgroundImage, (Property<BackgroundView, Float>) View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.backgroundImage, (Property<BackgroundView, Float>) View.TRANSLATION_Y, 0.0f));
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

    /* JADX WARN: Removed duplicated region for block: B:11:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x00d5 A[LOOP:0: B:6:0x00d3->B:7:0x00d5, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void applyColor(int i, int i2) {
        int size;
        int i3;
        View view;
        int i4 = this.colorType;
        if (i4 == 1) {
            if (i2 == 0) {
                this.accent.accentColor = i;
                Theme.refreshThemeColors();
            } else if (i2 == 1) {
                this.accent.accentColor2 = i;
                Theme.refreshThemeColors(true, true);
                this.listView2.invalidateViews();
            }
            size = this.themeDescriptions.size();
            for (i3 = 0; i3 < size; i3++) {
                ThemeDescription themeDescription = (ThemeDescription) this.themeDescriptions.get(i3);
                themeDescription.setColor(getThemedColor(themeDescription.getCurrentKey()), false, false);
            }
            this.listView.invalidateViews();
            this.listView2.invalidateViews();
            view = this.dotsContainer;
            if (view != null) {
                view.invalidate();
                return;
            }
            return;
        }
        if (i4 == 2) {
            if (this.lastPickedColorNum == 0) {
                this.accent.backgroundOverrideColor = i;
            } else if (i2 == 1) {
                int defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to1);
                if (i != 0 || defaultAccentColor == 0) {
                    this.accent.backgroundGradientOverrideColor1 = i;
                } else {
                    this.accent.backgroundGradientOverrideColor1 = 4294967296L;
                }
            } else if (i2 == 2) {
                int defaultAccentColor2 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to2);
                if (i != 0 || defaultAccentColor2 == 0) {
                    this.accent.backgroundGradientOverrideColor2 = i;
                } else {
                    this.accent.backgroundGradientOverrideColor2 = 4294967296L;
                }
            } else if (i2 == 3) {
                int defaultAccentColor3 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to3);
                if (i != 0 || defaultAccentColor3 == 0) {
                    this.accent.backgroundGradientOverrideColor3 = i;
                } else {
                    this.accent.backgroundGradientOverrideColor3 = 4294967296L;
                }
            }
            Theme.refreshThemeColors(true, false);
        } else {
            if (i4 == 3) {
                int i5 = this.lastPickedColorNum;
                if (i5 == 0) {
                    this.accent.myMessagesAccentColor = i;
                } else if (i5 == 1) {
                    this.accent.myMessagesGradientAccentColor1 = i;
                } else if (i5 == 2) {
                    Theme.ThemeAccent themeAccent = this.accent;
                    int i6 = themeAccent.myMessagesGradientAccentColor2;
                    themeAccent.myMessagesGradientAccentColor2 = i;
                    if (i6 != 0 && i == 0) {
                        this.messagesAdapter.notifyItemRemoved(0);
                    } else if (i6 == 0 && i != 0) {
                        this.messagesAdapter.notifyItemInserted(0);
                        showAnimationHint();
                    }
                } else {
                    this.accent.myMessagesGradientAccentColor3 = i;
                }
                int i7 = this.lastPickedColorNum;
                if (i7 >= 0) {
                    this.messagesCheckBoxView[1].setColor(i7, i);
                }
                Theme.refreshThemeColors(true, true);
                this.listView2.invalidateViews();
            }
            size = this.themeDescriptions.size();
            while (i3 < size) {
            }
            this.listView.invalidateViews();
            this.listView2.invalidateViews();
            view = this.dotsContainer;
            if (view != null) {
            }
        }
        this.colorPicker.setHasChanges(hasChanges(this.colorType));
        updatePlayAnimationView(true);
        size = this.themeDescriptions.size();
        while (i3 < size) {
        }
        this.listView.invalidateViews();
        this.listView2.invalidateViews();
        view = this.dotsContainer;
        if (view != null) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x033b, code lost:
    
        if (r7 >= 0.0f) goto L147;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0579  */
    /* JADX WARN: Removed duplicated region for block: B:120:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0573  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x0298  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x025a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:221:0x0216 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0287  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0336  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0349  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x03bb  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x03d5  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x03ed  */
    /* JADX WARN: Type inference failed for: r10v23 */
    /* JADX WARN: Type inference failed for: r10v24, types: [java.io.File] */
    /* JADX WARN: Type inference failed for: r10v40 */
    /* JADX WARN: Type inference failed for: r10v41 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void applyWallpaperBackground(boolean z) {
        TLRPC.TL_wallPaper tL_wallPaper;
        boolean copyFile;
        TLRPC.TL_wallPaper tL_wallPaper2;
        boolean z2;
        TLRPC.TL_wallPaper tL_wallPaper3;
        File file;
        TLRPC.TL_wallPaper tL_wallPaper4;
        TLRPC.TL_wallPaper tL_wallPaper5;
        TLRPC.TL_wallPaper tL_wallPaper6;
        Object obj;
        boolean z3;
        File file2;
        String str;
        int i;
        int i2;
        File file3;
        File pathToAttach;
        int i3;
        int i4;
        TLRPC.TL_wallPaper tL_wallPaper7;
        File file4;
        Theme.OverrideWallpaperInfo overrideWallpaperInfo;
        float f;
        Object obj2;
        long j;
        TLRPC.TL_wallPaper tL_wallPaper8;
        boolean z4;
        float f2;
        TLRPC.UserFull userFull;
        TLRPC.TL_wallPaper tL_wallPaper9;
        TLRPC.TL_wallPaper tL_wallPaper10;
        int i5 = 0;
        if (this.dialogId < 0) {
            TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = this.boostsStatus;
            if (tL_premium_boostsStatus != null && tL_premium_boostsStatus.level < getCustomWallpaperLevelMin()) {
                getMessagesController().getBoostsController().userCanBoostChannel(this.dialogId, this.boostsStatus, new Consumer() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda27
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
            return;
        }
        Theme.ThemeInfo activeTheme = Theme.getActiveTheme();
        String generateWallpaperName = activeTheme.generateWallpaperName(null, this.isBlurred);
        String generateWallpaperName2 = this.isBlurred ? activeTheme.generateWallpaperName(null, false) : generateWallpaperName;
        File file5 = new File(ApplicationLoader.getFilesDirFixed(), generateWallpaperName);
        Object obj3 = this.currentWallpaper;
        if (obj3 instanceof TLRPC.TL_wallPaper) {
            try {
            } catch (Exception e) {
                FileLog.e(e);
            }
            if (this.originalBitmap != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(file5);
                this.originalBitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream);
                fileOutputStream.close();
            } else {
                ImageReceiver imageReceiver = this.backgroundImage.getImageReceiver();
                if (imageReceiver.hasNotThumb() || imageReceiver.hasStaticThumb()) {
                    Bitmap bitmap = imageReceiver.getBitmap();
                    FileOutputStream fileOutputStream2 = new FileOutputStream(file5);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream2);
                    fileOutputStream2.close();
                }
                copyFile = false;
                if (!copyFile) {
                    try {
                        copyFile = AndroidUtilities.copyFile(FileLoader.getInstance(this.currentAccount).getPathToAttach(((TLRPC.TL_wallPaper) this.currentWallpaper).document, true), file5);
                    } catch (Exception e2) {
                        e = e2;
                        tL_wallPaper10 = null;
                        FileLog.e(e);
                        tL_wallPaper = tL_wallPaper10;
                        copyFile = false;
                        tL_wallPaper9 = tL_wallPaper;
                        z2 = false;
                        tL_wallPaper6 = tL_wallPaper9;
                        if (this.isBlurred) {
                        }
                        obj = this.currentWallpaper;
                        if (obj instanceof TLRPC.TL_wallPaper) {
                        }
                        i = 0;
                        i2 = 45;
                        i3 = 0;
                        i4 = 0;
                        file4 = tL_wallPaper6;
                        overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
                        overrideWallpaperInfo.fileName = generateWallpaperName2;
                        overrideWallpaperInfo.originalFileName = generateWallpaperName;
                        overrideWallpaperInfo.slug = str;
                        overrideWallpaperInfo.isBlurred = this.isBlurred;
                        overrideWallpaperInfo.isMotion = this.isMotion;
                        overrideWallpaperInfo.color = i;
                        overrideWallpaperInfo.gradientColor1 = i4;
                        overrideWallpaperInfo.gradientColor2 = i5;
                        overrideWallpaperInfo.gradientColor3 = i3;
                        overrideWallpaperInfo.rotation = i2;
                        if (this.shouldShowBrightnessControll) {
                        }
                        f = this.currentIntensity;
                        overrideWallpaperInfo.intensity = f;
                        obj2 = this.currentWallpaper;
                        if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                        }
                        j = this.dialogId;
                        overrideWallpaperInfo.dialogId = j;
                        if (j != 0) {
                        }
                        overrideWallpaperInfo.forBoth = z;
                        MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo, str == null && this.dialogId == 0, 0L);
                        if (copyFile) {
                        }
                        tL_wallPaper8 = tL_wallPaper7;
                        z4 = true;
                        if (z4) {
                        }
                    }
                }
                tL_wallPaper9 = null;
            }
            copyFile = true;
            if (!copyFile) {
            }
            tL_wallPaper9 = null;
        } else {
            if (obj3 instanceof WallpapersListActivity.ColorWallpaper) {
                if (this.selectedPattern != null) {
                    try {
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
                    } catch (Throwable th) {
                        FileLog.e(th);
                        copyFile = false;
                        z2 = false;
                        tL_wallPaper6 = null;
                    }
                } else {
                    tL_wallPaper4 = null;
                    z2 = false;
                    tL_wallPaper5 = tL_wallPaper4;
                    copyFile = true;
                    tL_wallPaper6 = tL_wallPaper5;
                    if (this.isBlurred) {
                        try {
                            FileOutputStream fileOutputStream4 = new FileOutputStream(new File(ApplicationLoader.getFilesDirFixed(), generateWallpaperName2));
                            this.blurredBitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream4);
                            fileOutputStream4.close();
                            copyFile = true;
                        } catch (Throwable th2) {
                            FileLog.e(th2);
                            copyFile = false;
                        }
                    }
                    obj = this.currentWallpaper;
                    if (obj instanceof TLRPC.TL_wallPaper) {
                        if (!(obj instanceof WallpapersListActivity.ColorWallpaper)) {
                            if (obj instanceof WallpapersListActivity.FileWallpaper) {
                                WallpapersListActivity.FileWallpaper fileWallpaper = (WallpapersListActivity.FileWallpaper) obj;
                                str = fileWallpaper.slug;
                                pathToAttach = fileWallpaper.path;
                            } else if (obj instanceof MediaController.SearchImage) {
                                MediaController.SearchImage searchImage = (MediaController.SearchImage) obj;
                                TLRPC.Photo photo = searchImage.photo;
                                pathToAttach = photo != null ? FileLoader.getInstance(this.currentAccount).getPathToAttach(FileLoader.getClosestPhotoSizeWithSize(photo.sizes, this.maxWallpaperSize, true), true) : ImageLoader.getHttpFilePath(searchImage.imageUrl, "jpg");
                                str = "";
                            } else {
                                z3 = z2;
                                file2 = file5;
                                str = "d";
                                i = 0;
                                i2 = 45;
                                i5 = 0;
                                file3 = null;
                                i3 = 0;
                                i4 = 0;
                                tL_wallPaper7 = null;
                                file4 = file3;
                            }
                            file3 = pathToAttach;
                            z3 = z2;
                            file2 = file5;
                            i = 0;
                            i2 = 45;
                            i5 = 0;
                            i3 = 0;
                            i4 = 0;
                            tL_wallPaper7 = null;
                            file4 = file3;
                        } else if ("d".equals(((WallpapersListActivity.ColorWallpaper) obj).slug)) {
                            z3 = z2;
                            tL_wallPaper7 = tL_wallPaper6;
                            file2 = file5;
                            str = "d";
                        } else {
                            TLRPC.TL_wallPaper tL_wallPaper11 = this.selectedPattern;
                            str = tL_wallPaper11 != null ? tL_wallPaper11.slug : "c";
                            i = this.backgroundColor;
                            i4 = this.backgroundGradientColor1;
                            int i6 = this.backgroundGradientColor2;
                            int i7 = this.backgroundGradientColor3;
                            i2 = this.backgroundRotation;
                            z3 = z2;
                            file2 = file5;
                            tL_wallPaper7 = null;
                            i3 = i7;
                            i5 = i6;
                            file4 = 0;
                        }
                        overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
                        overrideWallpaperInfo.fileName = generateWallpaperName2;
                        overrideWallpaperInfo.originalFileName = generateWallpaperName;
                        overrideWallpaperInfo.slug = str;
                        overrideWallpaperInfo.isBlurred = this.isBlurred;
                        overrideWallpaperInfo.isMotion = this.isMotion;
                        overrideWallpaperInfo.color = i;
                        overrideWallpaperInfo.gradientColor1 = i4;
                        overrideWallpaperInfo.gradientColor2 = i5;
                        overrideWallpaperInfo.gradientColor3 = i3;
                        overrideWallpaperInfo.rotation = i2;
                        if (this.shouldShowBrightnessControll) {
                            f = this.dimAmount;
                        }
                        f = this.currentIntensity;
                        overrideWallpaperInfo.intensity = f;
                        obj2 = this.currentWallpaper;
                        if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                            WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper) obj2;
                            String str2 = ("c".equals(str) || "t".equals(str) || "d".equals(str)) ? null : str;
                            float f3 = colorWallpaper.intensity;
                            if (f3 < 0.0f && !Theme.getActiveTheme().isDark()) {
                                f3 *= -1.0f;
                            }
                            if (colorWallpaper.parentWallpaper != null && colorWallpaper.color == i && colorWallpaper.gradientColor1 == i4 && colorWallpaper.gradientColor2 == i5 && colorWallpaper.gradientColor3 == i3 && TextUtils.equals(colorWallpaper.slug, str2) && colorWallpaper.gradientRotation == i2 && (this.selectedPattern == null || Math.abs(f3 - this.currentIntensity) < 0.001f)) {
                                TLRPC.WallPaper wallPaper = colorWallpaper.parentWallpaper;
                                overrideWallpaperInfo.wallpaperId = wallPaper.id;
                                overrideWallpaperInfo.accessHash = wallPaper.access_hash;
                            }
                        }
                        j = this.dialogId;
                        overrideWallpaperInfo.dialogId = j;
                        if (j != 0 && (userFull = getMessagesController().getUserFull(this.dialogId)) != null) {
                            overrideWallpaperInfo.prevUserWallpaper = userFull.wallpaper;
                        }
                        overrideWallpaperInfo.forBoth = z;
                        MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo, str == null && this.dialogId == 0, 0L);
                        if (copyFile) {
                            if (this.dialogId != 0) {
                                if (file4 == 0 || getMessagesController().uploadingWallpaperInfo != overrideWallpaperInfo) {
                                    z4 = false;
                                    ChatThemeController.getInstance(this.currentAccount).setWallpaperToPeer(this.dialogId, null, overrideWallpaperInfo, this.serverWallpaper, new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda28
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            ThemePreviewActivity.lambda$applyWallpaperBackground$21();
                                        }
                                    });
                                    tL_wallPaper8 = tL_wallPaper7;
                                } else {
                                    tL_wallPaper8 = new TLRPC.TL_wallPaper();
                                    TLRPC.TL_wallPaperSettings tL_wallPaperSettings = new TLRPC.TL_wallPaperSettings();
                                    tL_wallPaper8.settings = tL_wallPaperSettings;
                                    tL_wallPaperSettings.intensity = (int) (overrideWallpaperInfo.intensity * 100.0f);
                                    tL_wallPaperSettings.blur = overrideWallpaperInfo.isBlurred;
                                    tL_wallPaperSettings.motion = overrideWallpaperInfo.isMotion;
                                    tL_wallPaper8.uploadingImage = file4.getAbsolutePath();
                                    Bitmap createBitmap2 = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
                                    Canvas canvas2 = new Canvas(createBitmap2);
                                    float max = Math.max(50.0f / this.backgroundImage.getMeasuredWidth(), 50.0f / this.backgroundImage.getMeasuredHeight());
                                    canvas2.scale(max, max);
                                    if (this.backgroundImage.getMeasuredHeight() > this.backgroundImage.getMeasuredWidth()) {
                                        f2 = 0.0f;
                                        canvas2.translate(0.0f, (-(this.backgroundImage.getMeasuredHeight() - this.backgroundImage.getMeasuredWidth())) / 2.0f);
                                    } else {
                                        f2 = 0.0f;
                                        canvas2.translate((-(this.backgroundImage.getMeasuredWidth() - this.backgroundImage.getMeasuredHeight())) / 2.0f, 0.0f);
                                    }
                                    float f4 = this.dimAmount;
                                    this.dimAmount = f2;
                                    this.backgroundImage.draw(canvas2);
                                    this.dimAmount = f4;
                                    Utilities.blurBitmap(createBitmap2, 3, 1, createBitmap2.getWidth(), createBitmap2.getHeight(), createBitmap2.getRowBytes());
                                    tL_wallPaper8.stripedThumb = createBitmap2;
                                    createServiceMessageLocal(tL_wallPaper8, z);
                                    if (this.dialogId >= 0) {
                                        TLRPC.UserFull userFull2 = getMessagesController().getUserFull(this.dialogId);
                                        if (userFull2 != null) {
                                            userFull2.wallpaper = tL_wallPaper8;
                                            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.userInfoDidLoad, Long.valueOf(this.dialogId), userFull2);
                                        }
                                    } else {
                                        TLRPC.ChatFull chatFull = getMessagesController().getChatFull(-this.dialogId);
                                        if (chatFull != null) {
                                            chatFull.wallpaper = tL_wallPaper8;
                                            NotificationCenter notificationCenter = NotificationCenter.getInstance(this.currentAccount);
                                            int i8 = NotificationCenter.chatInfoDidLoad;
                                            z4 = false;
                                            Boolean bool = Boolean.FALSE;
                                            notificationCenter.lambda$postNotificationNameOnUIThread$1(i8, chatFull, 0, bool, bool);
                                        }
                                    }
                                    z4 = false;
                                }
                                this.setupFinished = true;
                                WallpaperActivityDelegate wallpaperActivityDelegate = this.delegate;
                                if (wallpaperActivityDelegate != null) {
                                    wallpaperActivityDelegate.didSetNewBackground(tL_wallPaper8);
                                }
                                lambda$onBackPressed$321();
                                if (z4) {
                                    return;
                                }
                                WallpaperActivityDelegate wallpaperActivityDelegate2 = this.delegate;
                                if (wallpaperActivityDelegate2 != null) {
                                    wallpaperActivityDelegate2.didSetNewBackground(tL_wallPaper8);
                                }
                                lambda$onBackPressed$321();
                                return;
                            }
                            Theme.serviceMessageColorBackup = getThemedColor(Theme.key_chat_serviceBackground);
                            Theme.getActiveTheme().setOverrideWallpaper("t".equals(overrideWallpaperInfo.slug) ? null : overrideWallpaperInfo);
                            Theme.reloadWallpaper(true);
                            if (!z3) {
                                ImageLoader.getInstance().removeImage(ImageLoader.getHttpFileName(file2.getAbsolutePath()) + "@100_100");
                            }
                        }
                        tL_wallPaper8 = tL_wallPaper7;
                        z4 = true;
                        if (z4) {
                        }
                    } else {
                        TLRPC.TL_wallPaper tL_wallPaper12 = (TLRPC.TL_wallPaper) obj;
                        str = tL_wallPaper12.slug;
                        tL_wallPaper7 = tL_wallPaper12;
                        z3 = z2;
                        file2 = file5;
                    }
                    i = 0;
                    i2 = 45;
                    i3 = 0;
                    i4 = 0;
                    file4 = tL_wallPaper6;
                    overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
                    overrideWallpaperInfo.fileName = generateWallpaperName2;
                    overrideWallpaperInfo.originalFileName = generateWallpaperName;
                    overrideWallpaperInfo.slug = str;
                    overrideWallpaperInfo.isBlurred = this.isBlurred;
                    overrideWallpaperInfo.isMotion = this.isMotion;
                    overrideWallpaperInfo.color = i;
                    overrideWallpaperInfo.gradientColor1 = i4;
                    overrideWallpaperInfo.gradientColor2 = i5;
                    overrideWallpaperInfo.gradientColor3 = i3;
                    overrideWallpaperInfo.rotation = i2;
                    if (this.shouldShowBrightnessControll) {
                    }
                    f = this.currentIntensity;
                    overrideWallpaperInfo.intensity = f;
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
                    if (copyFile) {
                    }
                    tL_wallPaper8 = tL_wallPaper7;
                    z4 = true;
                    if (z4) {
                    }
                }
            } else if (obj3 instanceof WallpapersListActivity.FileWallpaper) {
                WallpapersListActivity.FileWallpaper fileWallpaper2 = (WallpapersListActivity.FileWallpaper) obj3;
                if (fileWallpaper2.resId == 0 && !"t".equals(fileWallpaper2.slug)) {
                    try {
                    } catch (Exception e3) {
                        e = e3;
                        tL_wallPaper2 = null;
                    }
                    try {
                    } catch (Exception e4) {
                        e = e4;
                        z2 = false;
                        FileLog.e(e);
                        copyFile = false;
                        tL_wallPaper6 = tL_wallPaper2;
                        if (this.isBlurred) {
                        }
                        obj = this.currentWallpaper;
                        if (obj instanceof TLRPC.TL_wallPaper) {
                        }
                        i = 0;
                        i2 = 45;
                        i3 = 0;
                        i4 = 0;
                        file4 = tL_wallPaper6;
                        overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
                        overrideWallpaperInfo.fileName = generateWallpaperName2;
                        overrideWallpaperInfo.originalFileName = generateWallpaperName;
                        overrideWallpaperInfo.slug = str;
                        overrideWallpaperInfo.isBlurred = this.isBlurred;
                        overrideWallpaperInfo.isMotion = this.isMotion;
                        overrideWallpaperInfo.color = i;
                        overrideWallpaperInfo.gradientColor1 = i4;
                        overrideWallpaperInfo.gradientColor2 = i5;
                        overrideWallpaperInfo.gradientColor3 = i3;
                        overrideWallpaperInfo.rotation = i2;
                        if (this.shouldShowBrightnessControll) {
                        }
                        f = this.currentIntensity;
                        overrideWallpaperInfo.intensity = f;
                        obj2 = this.currentWallpaper;
                        if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                        }
                        j = this.dialogId;
                        overrideWallpaperInfo.dialogId = j;
                        if (j != 0) {
                        }
                        overrideWallpaperInfo.forBoth = z;
                        MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo, str == null && this.dialogId == 0, 0L);
                        if (copyFile) {
                        }
                        tL_wallPaper8 = tL_wallPaper7;
                        z4 = true;
                        if (z4) {
                        }
                    }
                    if (!this.hasScrollingBackground || this.currentScrollOffset == this.defaultScrollOffset) {
                        tL_wallPaper3 = null;
                        tL_wallPaper2 = null;
                        file = fileWallpaper2.originalPath;
                        if (file != null) {
                            z2 = file.equals(file5);
                            tL_wallPaper5 = tL_wallPaper2;
                            if (!z2) {
                                try {
                                    copyFile = AndroidUtilities.copyFile(file, file5);
                                    tL_wallPaper6 = tL_wallPaper2;
                                } catch (Exception e5) {
                                    e = e5;
                                    FileLog.e(e);
                                    copyFile = false;
                                    tL_wallPaper6 = tL_wallPaper2;
                                    if (this.isBlurred) {
                                    }
                                    obj = this.currentWallpaper;
                                    if (obj instanceof TLRPC.TL_wallPaper) {
                                    }
                                    i = 0;
                                    i2 = 45;
                                    i3 = 0;
                                    i4 = 0;
                                    file4 = tL_wallPaper6;
                                    overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
                                    overrideWallpaperInfo.fileName = generateWallpaperName2;
                                    overrideWallpaperInfo.originalFileName = generateWallpaperName;
                                    overrideWallpaperInfo.slug = str;
                                    overrideWallpaperInfo.isBlurred = this.isBlurred;
                                    overrideWallpaperInfo.isMotion = this.isMotion;
                                    overrideWallpaperInfo.color = i;
                                    overrideWallpaperInfo.gradientColor1 = i4;
                                    overrideWallpaperInfo.gradientColor2 = i5;
                                    overrideWallpaperInfo.gradientColor3 = i3;
                                    overrideWallpaperInfo.rotation = i2;
                                    if (this.shouldShowBrightnessControll) {
                                    }
                                    f = this.currentIntensity;
                                    overrideWallpaperInfo.intensity = f;
                                    obj2 = this.currentWallpaper;
                                    if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                                    }
                                    j = this.dialogId;
                                    overrideWallpaperInfo.dialogId = j;
                                    if (j != 0) {
                                    }
                                    overrideWallpaperInfo.forBoth = z;
                                    MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo, str == null && this.dialogId == 0, 0L);
                                    if (copyFile) {
                                    }
                                    tL_wallPaper8 = tL_wallPaper7;
                                    z4 = true;
                                    if (z4) {
                                    }
                                }
                                if (this.isBlurred) {
                                }
                                obj = this.currentWallpaper;
                                if (obj instanceof TLRPC.TL_wallPaper) {
                                }
                                i = 0;
                                i2 = 45;
                                i3 = 0;
                                i4 = 0;
                                file4 = tL_wallPaper6;
                                overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
                                overrideWallpaperInfo.fileName = generateWallpaperName2;
                                overrideWallpaperInfo.originalFileName = generateWallpaperName;
                                overrideWallpaperInfo.slug = str;
                                overrideWallpaperInfo.isBlurred = this.isBlurred;
                                overrideWallpaperInfo.isMotion = this.isMotion;
                                overrideWallpaperInfo.color = i;
                                overrideWallpaperInfo.gradientColor1 = i4;
                                overrideWallpaperInfo.gradientColor2 = i5;
                                overrideWallpaperInfo.gradientColor3 = i3;
                                overrideWallpaperInfo.rotation = i2;
                                if (this.shouldShowBrightnessControll) {
                                }
                                f = this.currentIntensity;
                                overrideWallpaperInfo.intensity = f;
                                obj2 = this.currentWallpaper;
                                if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                                }
                                j = this.dialogId;
                                overrideWallpaperInfo.dialogId = j;
                                if (j != 0) {
                                }
                                overrideWallpaperInfo.forBoth = z;
                                MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo, str == null && this.dialogId == 0, 0L);
                                if (copyFile) {
                                }
                                tL_wallPaper8 = tL_wallPaper7;
                                z4 = true;
                                if (z4) {
                                }
                            }
                            copyFile = true;
                            tL_wallPaper6 = tL_wallPaper5;
                            if (this.isBlurred) {
                            }
                            obj = this.currentWallpaper;
                            if (obj instanceof TLRPC.TL_wallPaper) {
                            }
                            i = 0;
                            i2 = 45;
                            i3 = 0;
                            i4 = 0;
                            file4 = tL_wallPaper6;
                            overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
                            overrideWallpaperInfo.fileName = generateWallpaperName2;
                            overrideWallpaperInfo.originalFileName = generateWallpaperName;
                            overrideWallpaperInfo.slug = str;
                            overrideWallpaperInfo.isBlurred = this.isBlurred;
                            overrideWallpaperInfo.isMotion = this.isMotion;
                            overrideWallpaperInfo.color = i;
                            overrideWallpaperInfo.gradientColor1 = i4;
                            overrideWallpaperInfo.gradientColor2 = i5;
                            overrideWallpaperInfo.gradientColor3 = i3;
                            overrideWallpaperInfo.rotation = i2;
                            if (this.shouldShowBrightnessControll) {
                            }
                            f = this.currentIntensity;
                            overrideWallpaperInfo.intensity = f;
                            obj2 = this.currentWallpaper;
                            if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                            }
                            j = this.dialogId;
                            overrideWallpaperInfo.dialogId = j;
                            if (j != 0) {
                            }
                            overrideWallpaperInfo.forBoth = z;
                            MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo, str == null && this.dialogId == 0, 0L);
                            if (copyFile) {
                            }
                            tL_wallPaper8 = tL_wallPaper7;
                            z4 = true;
                            if (z4) {
                            }
                        }
                    } else {
                        Bitmap createBitmap3 = Bitmap.createBitmap((int) this.croppedWidth, this.currentWallpaperBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas3 = new Canvas(createBitmap3);
                        canvas3.translate(-((this.currentScrollOffset / this.maxScrollOffset) * (this.currentWallpaperBitmap.getWidth() - createBitmap3.getWidth())), 0.0f);
                        tL_wallPaper3 = null;
                        canvas3.drawBitmap(this.currentWallpaperBitmap, 0.0f, 0.0f, (Paint) null);
                        fileWallpaper2.path = new File(FileLoader.getDirectory(4), Utilities.random.nextInt() + ".jpg");
                        FileOutputStream fileOutputStream5 = new FileOutputStream(fileWallpaper2.path);
                        createBitmap3.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream5);
                        fileOutputStream5.close();
                        createBitmap3.recycle();
                    }
                    file = fileWallpaper2.path;
                    tL_wallPaper2 = tL_wallPaper3;
                    z2 = file.equals(file5);
                    tL_wallPaper5 = tL_wallPaper2;
                    if (!z2) {
                    }
                    copyFile = true;
                    tL_wallPaper6 = tL_wallPaper5;
                    if (this.isBlurred) {
                    }
                    obj = this.currentWallpaper;
                    if (obj instanceof TLRPC.TL_wallPaper) {
                    }
                    i = 0;
                    i2 = 45;
                    i3 = 0;
                    i4 = 0;
                    file4 = tL_wallPaper6;
                    overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
                    overrideWallpaperInfo.fileName = generateWallpaperName2;
                    overrideWallpaperInfo.originalFileName = generateWallpaperName;
                    overrideWallpaperInfo.slug = str;
                    overrideWallpaperInfo.isBlurred = this.isBlurred;
                    overrideWallpaperInfo.isMotion = this.isMotion;
                    overrideWallpaperInfo.color = i;
                    overrideWallpaperInfo.gradientColor1 = i4;
                    overrideWallpaperInfo.gradientColor2 = i5;
                    overrideWallpaperInfo.gradientColor3 = i3;
                    overrideWallpaperInfo.rotation = i2;
                    if (this.shouldShowBrightnessControll) {
                    }
                    f = this.currentIntensity;
                    overrideWallpaperInfo.intensity = f;
                    obj2 = this.currentWallpaper;
                    if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                    }
                    j = this.dialogId;
                    overrideWallpaperInfo.dialogId = j;
                    if (j != 0) {
                    }
                    overrideWallpaperInfo.forBoth = z;
                    MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo, str == null && this.dialogId == 0, 0L);
                    if (copyFile) {
                    }
                    tL_wallPaper8 = tL_wallPaper7;
                    z4 = true;
                    if (z4) {
                    }
                }
            } else {
                tL_wallPaper = null;
                tL_wallPaper9 = null;
                tL_wallPaper10 = null;
                if (obj3 instanceof MediaController.SearchImage) {
                    MediaController.SearchImage searchImage2 = (MediaController.SearchImage) obj3;
                    TLRPC.Photo photo2 = searchImage2.photo;
                    try {
                        copyFile = AndroidUtilities.copyFile(photo2 != null ? FileLoader.getInstance(this.currentAccount).getPathToAttach(FileLoader.getClosestPhotoSizeWithSize(photo2.sizes, this.maxWallpaperSize, true), true) : ImageLoader.getHttpFilePath(searchImage2.imageUrl, "jpg"), file5);
                    } catch (Exception e6) {
                        e = e6;
                        FileLog.e(e);
                        tL_wallPaper = tL_wallPaper10;
                        copyFile = false;
                        tL_wallPaper9 = tL_wallPaper;
                        z2 = false;
                        tL_wallPaper6 = tL_wallPaper9;
                        if (this.isBlurred) {
                        }
                        obj = this.currentWallpaper;
                        if (obj instanceof TLRPC.TL_wallPaper) {
                        }
                        i = 0;
                        i2 = 45;
                        i3 = 0;
                        i4 = 0;
                        file4 = tL_wallPaper6;
                        overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
                        overrideWallpaperInfo.fileName = generateWallpaperName2;
                        overrideWallpaperInfo.originalFileName = generateWallpaperName;
                        overrideWallpaperInfo.slug = str;
                        overrideWallpaperInfo.isBlurred = this.isBlurred;
                        overrideWallpaperInfo.isMotion = this.isMotion;
                        overrideWallpaperInfo.color = i;
                        overrideWallpaperInfo.gradientColor1 = i4;
                        overrideWallpaperInfo.gradientColor2 = i5;
                        overrideWallpaperInfo.gradientColor3 = i3;
                        overrideWallpaperInfo.rotation = i2;
                        if (this.shouldShowBrightnessControll) {
                        }
                        f = this.currentIntensity;
                        overrideWallpaperInfo.intensity = f;
                        obj2 = this.currentWallpaper;
                        if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
                        }
                        j = this.dialogId;
                        overrideWallpaperInfo.dialogId = j;
                        if (j != 0) {
                        }
                        overrideWallpaperInfo.forBoth = z;
                        MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo, str == null && this.dialogId == 0, 0L);
                        if (copyFile) {
                        }
                        tL_wallPaper8 = tL_wallPaper7;
                        z4 = true;
                        if (z4) {
                        }
                    }
                }
                copyFile = false;
                tL_wallPaper9 = tL_wallPaper;
            }
            tL_wallPaper4 = null;
            z2 = false;
            tL_wallPaper5 = tL_wallPaper4;
            copyFile = true;
            tL_wallPaper6 = tL_wallPaper5;
            if (this.isBlurred) {
            }
            obj = this.currentWallpaper;
            if (obj instanceof TLRPC.TL_wallPaper) {
            }
            i = 0;
            i2 = 45;
            i3 = 0;
            i4 = 0;
            file4 = tL_wallPaper6;
            overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
            overrideWallpaperInfo.fileName = generateWallpaperName2;
            overrideWallpaperInfo.originalFileName = generateWallpaperName;
            overrideWallpaperInfo.slug = str;
            overrideWallpaperInfo.isBlurred = this.isBlurred;
            overrideWallpaperInfo.isMotion = this.isMotion;
            overrideWallpaperInfo.color = i;
            overrideWallpaperInfo.gradientColor1 = i4;
            overrideWallpaperInfo.gradientColor2 = i5;
            overrideWallpaperInfo.gradientColor3 = i3;
            overrideWallpaperInfo.rotation = i2;
            if (this.shouldShowBrightnessControll) {
            }
            f = this.currentIntensity;
            overrideWallpaperInfo.intensity = f;
            obj2 = this.currentWallpaper;
            if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
            }
            j = this.dialogId;
            overrideWallpaperInfo.dialogId = j;
            if (j != 0) {
            }
            overrideWallpaperInfo.forBoth = z;
            MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo, str == null && this.dialogId == 0, 0L);
            if (copyFile) {
            }
            tL_wallPaper8 = tL_wallPaper7;
            z4 = true;
            if (z4) {
            }
        }
        z2 = false;
        tL_wallPaper6 = tL_wallPaper9;
        if (this.isBlurred) {
        }
        obj = this.currentWallpaper;
        if (obj instanceof TLRPC.TL_wallPaper) {
        }
        i = 0;
        i2 = 45;
        i3 = 0;
        i4 = 0;
        file4 = tL_wallPaper6;
        overrideWallpaperInfo = new Theme.OverrideWallpaperInfo();
        overrideWallpaperInfo.fileName = generateWallpaperName2;
        overrideWallpaperInfo.originalFileName = generateWallpaperName;
        overrideWallpaperInfo.slug = str;
        overrideWallpaperInfo.isBlurred = this.isBlurred;
        overrideWallpaperInfo.isMotion = this.isMotion;
        overrideWallpaperInfo.color = i;
        overrideWallpaperInfo.gradientColor1 = i4;
        overrideWallpaperInfo.gradientColor2 = i5;
        overrideWallpaperInfo.gradientColor3 = i3;
        overrideWallpaperInfo.rotation = i2;
        if (this.shouldShowBrightnessControll) {
        }
        f = this.currentIntensity;
        overrideWallpaperInfo.intensity = f;
        obj2 = this.currentWallpaper;
        if (obj2 instanceof WallpapersListActivity.ColorWallpaper) {
        }
        j = this.dialogId;
        overrideWallpaperInfo.dialogId = j;
        if (j != 0) {
        }
        overrideWallpaperInfo.forBoth = z;
        MessagesController.getInstance(this.currentAccount).saveWallpaperToServer(file4, overrideWallpaperInfo, str == null && this.dialogId == 0, 0L);
        if (copyFile) {
        }
        tL_wallPaper8 = tL_wallPaper7;
        z4 = true;
        if (z4) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelThemeApply(boolean z) {
        if (this.screenType == 2) {
            if (z) {
                return;
            }
            lambda$onBackPressed$321();
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
        lambda$onBackPressed$321();
    }

    private BitmapDrawable checkBlur(Drawable drawable) {
        ColorFilter colorFilter;
        WeakReference weakReference = this.lastDrawableToBlur;
        if (weakReference != null && weakReference.get() == drawable) {
            return this.blurredDrawable;
        }
        WeakReference weakReference2 = this.lastDrawableToBlur;
        if (weakReference2 != null) {
            weakReference2.clear();
        }
        this.lastDrawableToBlur = null;
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            this.blurredDrawable = null;
            return null;
        }
        this.lastDrawableToBlur = new WeakReference(drawable);
        int intrinsicWidth = (int) ((drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight()) * 24.0f);
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, 24, Bitmap.Config.ARGB_8888);
        drawable.setBounds(0, 0, intrinsicWidth, 24);
        if (Build.VERSION.SDK_INT >= 21) {
            colorFilter = drawable.getColorFilter();
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

    private void checkBoostsLevel() {
        if (this.dialogId >= 0 || this.checkingBoostsLevel || this.checkedBoostsLevel || this.boostsStatus != null) {
            return;
        }
        this.checkingBoostsLevel = true;
        getMessagesController().getBoostsController().getBoostsStats(this.dialogId, new Consumer() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda25
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                ThemePreviewActivity.this.lambda$checkBoostsLevel$1((TL_stories.TL_premium_boostsStatus) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0084, code lost:
    
        if (r7.accent.patternMotion == r7.isMotion) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0090, code lost:
    
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
                    TLRPC.TL_wallPaper tL_wallPaper = this.selectedPattern;
                    if (str.equals(tL_wallPaper != null ? tL_wallPaper.slug : "")) {
                        TLRPC.TL_wallPaper tL_wallPaper2 = this.selectedPattern;
                        if (tL_wallPaper2 != null) {
                        }
                        if (tL_wallPaper2 != null) {
                        }
                    }
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString(R.string.SaveChangesAlertTitle));
            builder.setMessage(LocaleController.getString(R.string.SaveChangesAlertText));
            builder.setPositiveButton(LocaleController.getString(R.string.Save), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ThemePreviewActivity.this.lambda$checkDiscard$25(dialogInterface, i);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.PassportDiscard), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda4
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

    private void createServiceMessageLocal(TLRPC.WallPaper wallPaper, boolean z) {
        TLRPC.TL_messageService tL_messageService = new TLRPC.TL_messageService();
        tL_messageService.random_id = SendMessagesHelper.getInstance(this.currentAccount).getNextRandomId();
        tL_messageService.dialog_id = this.dialogId;
        tL_messageService.unread = true;
        tL_messageService.out = true;
        int newMessageId = getUserConfig().getNewMessageId();
        tL_messageService.id = newMessageId;
        tL_messageService.local_id = newMessageId;
        TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
        if (ChatObject.isChannel(chat)) {
            TLRPC.TL_peerChannel tL_peerChannel = new TLRPC.TL_peerChannel();
            tL_messageService.from_id = tL_peerChannel;
            tL_peerChannel.channel_id = chat.id;
            TLRPC.TL_peerChannel tL_peerChannel2 = new TLRPC.TL_peerChannel();
            tL_messageService.peer_id = tL_peerChannel2;
            tL_peerChannel2.channel_id = chat.id;
        } else {
            TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
            tL_messageService.from_id = tL_peerUser;
            tL_peerUser.user_id = getUserConfig().getClientUserId();
            TLRPC.TL_peerUser tL_peerUser2 = new TLRPC.TL_peerUser();
            tL_messageService.peer_id = tL_peerUser2;
            tL_peerUser2.user_id = this.dialogId;
        }
        tL_messageService.flags |= 256;
        tL_messageService.date = getConnectionsManager().getCurrentTime();
        TLRPC.TL_messageActionSetChatWallPaper tL_messageActionSetChatWallPaper = new TLRPC.TL_messageActionSetChatWallPaper();
        tL_messageService.action = tL_messageActionSetChatWallPaper;
        tL_messageActionSetChatWallPaper.wallpaper = wallPaper;
        tL_messageActionSetChatWallPaper.for_both = z;
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        arrayList.add(new MessageObject(this.currentAccount, tL_messageService, false, false));
        new ArrayList().add(tL_messageService);
        MessagesController.getInstance(this.currentAccount).updateInterfaceWithMessages(this.dialogId, arrayList, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getButtonsColor(int i) {
        return this.useDefaultThemeForButtons ? Theme.getDefaultColor(i) : getThemedColor(i);
    }

    private int getCustomWallpaperLevelMin() {
        return ChatObject.isChannelAndNotMegaGroup(-this.dialogId, this.currentAccount) ? getMessagesController().channelCustomWallpaperLevelMin : getMessagesController().groupCustomWallpaperLevelMin;
    }

    private boolean hasChanges(int i) {
        int defaultAccentColor;
        long j;
        if (this.editingTheme) {
            return false;
        }
        if (i == 1 || i == 2) {
            long j2 = this.backupBackgroundOverrideColor;
            if (j2 == 0) {
                int defaultAccentColor2 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper);
                int i2 = (int) this.accent.backgroundOverrideColor;
                if (i2 == 0) {
                    i2 = defaultAccentColor2;
                }
                if (i2 != defaultAccentColor2) {
                    return true;
                }
            } else if (j2 != this.accent.backgroundOverrideColor) {
                return true;
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
            if (i6 == 0) {
                int i7 = themeAccent2.myMessagesAccentColor;
                if (i7 != 0 && i7 != themeAccent2.accentColor) {
                    return true;
                }
            } else if (i6 != themeAccent2.myMessagesAccentColor) {
                return true;
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
                WallpaperCheckBoxView wallpaperCheckBoxView = wallpaperCheckBoxViewArr[i3];
                if (wallpaperCheckBoxView != null) {
                    wallpaperCheckBoxView.setDimAmount(this.shouldShowBrightnessControll ? this.dimAmount * this.progressToDarkTheme : 0.0f);
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyWallpaperBackground$19() {
        presentFragment(StatisticActivity.create(getMessagesController().getChat(Long.valueOf(-this.dialogId))));
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
            limitReachedBottomSheet.showStatisticButtonInLink(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    ThemePreviewActivity.this.lambda$applyWallpaperBackground$19();
                }
            });
        }
        showDialog(limitReachedBottomSheet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$applyWallpaperBackground$21() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkBoostsLevel$1(TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus) {
        this.boostsStatus = tL_premium_boostsStatus;
        this.checkedBoostsLevel = true;
        updateApplyButton1(true);
        this.checkingBoostsLevel = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$25(DialogInterface dialogInterface, int i) {
        this.actionBar2.getActionBarMenuOnItemClick().onItemClick(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDiscard$26(DialogInterface dialogInterface, int i) {
        cancelThemeApply(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$10(Float f) {
        this.dimAmount = f.floatValue();
        this.backgroundImage.invalidate();
        invalidateBlur();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x007b, code lost:
    
        if (r3.patternLayout[r4].getVisibility() != 0) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x009c, code lost:
    
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x009a, code lost:
    
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0098, code lost:
    
        if (r3.patternLayout[r4].getVisibility() != 0) goto L39;
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00a8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$11(int i, WallpaperCheckBoxView wallpaperCheckBoxView, View view) {
        boolean z;
        if (this.backgroundButtonsContainer.getAlpha() == 1.0f && this.patternViewAnimation == null) {
            int i2 = this.screenType;
            if ((i2 == 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) && i == 2) {
                wallpaperCheckBoxView.setChecked(!wallpaperCheckBoxView.isChecked(), true);
            } else {
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
                            }
                        }
                        this.backgroundCheckBoxView[1].setChecked(this.selectedPattern != null, true);
                        updateSelectedPattern(true);
                        this.patternsListView.invalidateViews();
                        updateMotionButton();
                        return;
                    }
                    selectPattern(this.lastSelectedPattern != null ? -1 : 0);
                    if (this.screenType == 1) {
                        showPatternsView(1, true, true);
                        this.backgroundCheckBoxView[1].setChecked(this.selectedPattern != null, true);
                        updateSelectedPattern(true);
                        this.patternsListView.invalidateViews();
                        updateMotionButton();
                        return;
                    }
                    showPatternsView(i, z, true);
                    this.backgroundCheckBoxView[1].setChecked(this.selectedPattern != null, true);
                    updateSelectedPattern(true);
                    this.patternsListView.invalidateViews();
                    updateMotionButton();
                    return;
                }
                if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                    showPatternsView(i, this.patternLayout[i].getVisibility() != 0, true);
                    return;
                }
                if (i2 == 1) {
                    return;
                }
                wallpaperCheckBoxView.setChecked(!wallpaperCheckBoxView.isChecked(), true);
                if (i == 0) {
                    boolean isChecked = wallpaperCheckBoxView.isChecked();
                    this.isBlurred = isChecked;
                    if (isChecked) {
                        this.backgroundImage.getImageReceiver().setForceCrossfade(true);
                    }
                    updateBlurred();
                    return;
                }
            }
            boolean isChecked2 = wallpaperCheckBoxView.isChecked();
            this.isMotion = isChecked2;
            this.parallaxEffect.setEnabled(isChecked2);
            animateMotionChange();
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
            TLRPC.TL_wallPaper tL_wallPaper = this.previousSelectedPattern;
            this.selectedPattern = tL_wallPaper;
            if (tL_wallPaper == null) {
                this.backgroundImage.setImageDrawable(null);
            } else {
                BackgroundView backgroundView = this.backgroundImage;
                ImageLocation forDocument = ImageLocation.getForDocument(tL_wallPaper.document);
                String str = this.imageFilter;
                TLRPC.TL_wallPaper tL_wallPaper2 = this.selectedPattern;
                backgroundView.setImage(forDocument, str, null, null, "jpg", tL_wallPaper2.document.size, 1, tL_wallPaper2);
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
        Theme.ThemeInfo previousTheme = Theme.getPreviousTheme();
        if (previousTheme == null) {
            return;
        }
        int i = previousTheme.prevAccentId;
        Theme.ThemeAccent accent = i >= 0 ? (Theme.ThemeAccent) previousTheme.themeAccentsMap.get(i) : previousTheme.getAccent(false);
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
        BaseFragment baseFragment = (BaseFragment) getParentLayout().getFragmentStack().get(Math.max(0, getParentLayout().getFragmentStack().size() - 2));
        lambda$onBackPressed$321();
        if (this.screenType == 0) {
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didApplyNewTheme, previousTheme, accent, Boolean.valueOf(this.deleteOnCancel));
        }
        Theme.turnOffAutoNight(baseFragment);
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
        hintView.setText(LocaleController.getString(this.onSwitchDayNightDelegate.isDark() ? R.string.PreviewWallpaperDay : R.string.PreviewWallpaperNight));
        hintView.setBackgroundColor(-366530760, -1);
        hintView.showForView(this.dayNightItem, true);
        hintView.setExtraTranslationY(-AndroidUtilities.dp(14.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$3(View view, int i) {
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
            selectColorType(chatMessageCell.isInsideBackground(f, f2) ? chatMessageCell.getMessageObject().isOutOwner() ? 3 : 1 : 2);
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
    public /* synthetic */ void lambda$didReceivedNotification$28(TLObject tLObject) {
        if (tLObject instanceof TLRPC.TL_wallPaper) {
            TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) tLObject;
            if (tL_wallPaper.pattern) {
                this.selectedPattern = tL_wallPaper;
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
    public /* synthetic */ void lambda$didReceivedNotification$29(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                ThemePreviewActivity.this.lambda$didReceivedNotification$28(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$30(TLObject tLObject) {
        Theme.ThemeAccent themeAccent;
        TLRPC.TL_wallPaper tL_wallPaper;
        String str;
        if (tLObject instanceof TLRPC.TL_account_wallPapers) {
            TLRPC.TL_account_wallPapers tL_account_wallPapers = (TLRPC.TL_account_wallPapers) tLObject;
            this.patterns.clear();
            this.patternsDict.clear();
            int size = tL_account_wallPapers.wallpapers.size();
            boolean z = false;
            for (int i = 0; i < size; i++) {
                if (tL_account_wallPapers.wallpapers.get(i) instanceof TLRPC.TL_wallPaper) {
                    TLRPC.TL_wallPaper tL_wallPaper2 = (TLRPC.TL_wallPaper) tL_account_wallPapers.wallpapers.get(i);
                    if (tL_wallPaper2.pattern) {
                        TLRPC.Document document = tL_wallPaper2.document;
                        if (document != null && !this.patternsDict.containsKey(Long.valueOf(document.id))) {
                            this.patterns.add(tL_wallPaper2);
                            this.patternsDict.put(Long.valueOf(tL_wallPaper2.document.id), tL_wallPaper2);
                        }
                        Theme.ThemeAccent themeAccent2 = this.accent;
                        if (themeAccent2 != null && (str = themeAccent2.patternSlug) != null && str.equals(tL_wallPaper2.slug)) {
                            this.selectedPattern = tL_wallPaper2;
                            setCurrentImage(false);
                            updateButtonState(false, false);
                        } else if (this.accent == null) {
                            TLRPC.TL_wallPaper tL_wallPaper3 = this.selectedPattern;
                            if (tL_wallPaper3 != null) {
                                String str2 = tL_wallPaper3.slug;
                                if (str2 != null) {
                                    if (!str2.equals(tL_wallPaper2.slug)) {
                                    }
                                }
                            }
                        }
                        z = true;
                    }
                }
            }
            if (!z && (tL_wallPaper = this.selectedPattern) != null) {
                this.patterns.add(0, tL_wallPaper);
            }
            PatternsAdapter patternsAdapter = this.patternsAdapter;
            if (patternsAdapter != null) {
                patternsAdapter.notifyDataSetChanged();
            }
            MessagesStorage.getInstance(this.currentAccount).putWallpapers(tL_account_wallPapers.wallpapers, 1);
        }
        if (this.selectedPattern != null || (themeAccent = this.accent) == null || TextUtils.isEmpty(themeAccent.patternSlug)) {
            return;
        }
        TLRPC.TL_account_getWallPaper tL_account_getWallPaper = new TLRPC.TL_account_getWallPaper();
        TLRPC.TL_inputWallPaperSlug tL_inputWallPaperSlug = new TLRPC.TL_inputWallPaperSlug();
        tL_inputWallPaperSlug.slug = this.accent.patternSlug;
        tL_account_getWallPaper.wallpaper = tL_inputWallPaperSlug;
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(getConnectionsManager().sendRequest(tL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda26
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                ThemePreviewActivity.this.lambda$didReceivedNotification$29(tLObject2, tL_error);
            }
        }), this.classGuid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didReceivedNotification$31(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                ThemePreviewActivity.this.lambda$didReceivedNotification$30(tLObject);
            }
        });
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
            if (iNavigationLayout == null || iNavigationLayout.getBottomSheet() == null) {
                setNavigationBarColor(getThemedColor(Theme.key_dialogBackground));
            } else {
                this.parentLayout.getBottomSheet().fixNavigationBar(getThemedColor(Theme.key_dialogBackground));
                if (this.screenType == 2 && this.dialogId != 0) {
                    this.parentLayout.getBottomSheet().setOverlayNavBarColor(-16777216);
                }
            }
        }
        if (this.backgroundCheckBoxView != null) {
            int i2 = 0;
            while (true) {
                WallpaperCheckBoxView[] wallpaperCheckBoxViewArr = this.backgroundCheckBoxView;
                if (i2 >= wallpaperCheckBoxViewArr.length) {
                    break;
                }
                WallpaperCheckBoxView wallpaperCheckBoxView = wallpaperCheckBoxViewArr[i2];
                if (wallpaperCheckBoxView != null) {
                    wallpaperCheckBoxView.invalidate();
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
                WallpaperCheckBoxView wallpaperCheckBoxView2 = wallpaperCheckBoxViewArr2[i];
                if (wallpaperCheckBoxView2 != null) {
                    wallpaperCheckBoxView2.invalidate();
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.applyColorScheduled = false;
        applyColor(this.lastPickedColor, this.lastPickedColorNum);
        this.lastPickedColorNum = -1;
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
                    TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) this.patterns.get(i2);
                    if (tL_wallPaper.pattern && this.accent.patternSlug.equals(tL_wallPaper.slug)) {
                        this.selectedPattern = tL_wallPaper;
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showAnimationHint$32(SharedPreferences sharedPreferences) {
        if (this.colorType != 3) {
            return;
        }
        sharedPreferences.edit().putBoolean("bganimationhint", true).commit();
        this.animationHint.showForView(this.messagesCheckBoxView[0], true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$toggleTheme$34(View view, MotionEvent motionEvent) {
        return true;
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleTheme$36() {
        this.onSwitchDayNightDelegate.switchDayNight(false);
        setForceDark(this.themeDelegate.isDark(), true);
        setCurrentImage(false);
        invalidateBlur();
        updateBlurred();
        if (this.themeDescriptions != null) {
            for (int i = 0; i < this.themeDescriptions.size(); i++) {
                ((ThemeDescription) this.themeDescriptions.get(i)).setColor(getThemedColor(((ThemeDescription) this.themeDescriptions.get(i)).getCurrentKey()), false, false);
            }
        }
        if (this.shouldShowBrightnessControll) {
            DayNightSwitchDelegate dayNightSwitchDelegate = this.onSwitchDayNightDelegate;
            if (dayNightSwitchDelegate == null || !dayNightSwitchDelegate.isDark()) {
                this.dimmingSlider.animateValueTo(0.0f);
            } else {
                this.dimmingSlider.setVisibility(0);
                this.dimmingSlider.animateValueTo(this.dimAmount);
            }
            ValueAnimator valueAnimator = this.changeDayNightViewAnimator2;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.changeDayNightViewAnimator2.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.progressToDarkTheme, this.onSwitchDayNightDelegate.isDark() ? 1.0f : 0.0f);
            this.changeDayNightViewAnimator2 = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda36
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
    public void onColorsRotate() {
        if (this.screenType == 2) {
            int i = this.backgroundRotation + 45;
            while (true) {
                this.backgroundRotation = i;
                int i2 = this.backgroundRotation;
                if (i2 < 360) {
                    setBackgroundColor(this.backgroundColor, 0, true, true);
                    return;
                }
                i = i2 - 360;
            }
        } else {
            Theme.ThemeAccent themeAccent = this.accent;
            if (themeAccent == null) {
                return;
            }
            int i3 = themeAccent.backgroundRotation + 45;
            while (true) {
                themeAccent.backgroundRotation = i3;
                themeAccent = this.accent;
                int i4 = themeAccent.backgroundRotation;
                if (i4 < 360) {
                    Theme.refreshThemeColors();
                    return;
                }
                i3 = i4 - 360;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveAccentWallpaper() {
        FileOutputStream fileOutputStream;
        Theme.ThemeAccent themeAccent = this.accent;
        if (themeAccent == null || TextUtils.isEmpty(themeAccent.patternSlug)) {
            return;
        }
        try {
            File pathToWallpaper = this.accent.getPathToWallpaper();
            Drawable background = this.backgroundImage.getBackground();
            Bitmap bitmap = this.backgroundImage.getImageReceiver().getBitmap();
            if (background instanceof MotionBackgroundDrawable) {
                fileOutputStream = new FileOutputStream(pathToWallpaper);
                bitmap.compress(Bitmap.CompressFormat.PNG, 87, fileOutputStream);
            } else {
                Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                background.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                background.draw(canvas);
                Paint paint = new Paint(2);
                paint.setColorFilter(new PorterDuffColorFilter(this.patternColor, this.blendMode));
                paint.setAlpha((int) (this.currentIntensity * 255.0f));
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
                fileOutputStream = new FileOutputStream(pathToWallpaper);
                createBitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream);
            }
            fileOutputStream.close();
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleApplyColor(int i, int i2, boolean z) {
        if (i2 != -1) {
            int i3 = this.lastPickedColorNum;
            if (i3 != -1 && i3 != i2) {
                this.applyColorAction.run();
            }
            this.lastPickedColor = i;
            this.lastPickedColorNum = i2;
            if (z) {
                this.applyColorAction.run();
                return;
            } else {
                if (this.applyColorScheduled) {
                    return;
                }
                this.applyColorScheduled = true;
                this.fragmentView.postDelayed(this.applyColorAction, 16L);
                return;
            }
        }
        int i4 = this.colorType;
        if (i4 == 1 || i4 == 2) {
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
            if (i4 == 2) {
                int defaultAccentColor = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper);
                int defaultAccentColor2 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to1);
                int defaultAccentColor3 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to2);
                int defaultAccentColor4 = Theme.getDefaultAccentColor(Theme.key_chat_wallpaper_gradient_to3);
                Theme.ThemeAccent themeAccent = this.accent;
                int i5 = (int) themeAccent.backgroundGradientOverrideColor1;
                int i6 = (int) themeAccent.backgroundGradientOverrideColor2;
                int i7 = (int) themeAccent.backgroundGradientOverrideColor3;
                int i8 = (int) themeAccent.backgroundOverrideColor;
                ColorPicker colorPicker = this.colorPicker;
                if (i7 != 0) {
                    defaultAccentColor4 = i7;
                }
                colorPicker.setColor(defaultAccentColor4, 3);
                ColorPicker colorPicker2 = this.colorPicker;
                if (i6 != 0) {
                    defaultAccentColor3 = i6;
                }
                colorPicker2.setColor(defaultAccentColor3, 2);
                ColorPicker colorPicker3 = this.colorPicker;
                if (i5 != 0) {
                    defaultAccentColor2 = i5;
                }
                colorPicker3.setColor(defaultAccentColor2, 1);
                ColorPicker colorPicker4 = this.colorPicker;
                if (i8 != 0) {
                    defaultAccentColor = i8;
                }
                colorPicker4.setColor(defaultAccentColor, 0);
            }
        }
        int i9 = this.colorType;
        if (i9 == 1 || i9 == 3) {
            int i10 = this.backupMyMessagesAccentColor;
            if (i10 != 0) {
                this.accent.myMessagesAccentColor = i10;
            } else {
                this.accent.myMessagesAccentColor = 0;
            }
            int i11 = this.backupMyMessagesGradientAccentColor1;
            if (i11 != 0) {
                this.accent.myMessagesGradientAccentColor1 = i11;
            } else {
                this.accent.myMessagesGradientAccentColor1 = 0;
            }
            int i12 = this.backupMyMessagesGradientAccentColor2;
            if (i12 != 0) {
                this.accent.myMessagesGradientAccentColor2 = i12;
            } else {
                this.accent.myMessagesGradientAccentColor2 = 0;
            }
            int i13 = this.backupMyMessagesGradientAccentColor3;
            if (i13 != 0) {
                this.accent.myMessagesGradientAccentColor3 = i13;
            } else {
                this.accent.myMessagesGradientAccentColor3 = 0;
            }
            if (i9 == 3) {
                this.colorPicker.setColor(this.accent.myMessagesGradientAccentColor3, 3);
                this.colorPicker.setColor(this.accent.myMessagesGradientAccentColor2, 2);
                this.colorPicker.setColor(this.accent.myMessagesGradientAccentColor1, 1);
                ColorPicker colorPicker5 = this.colorPicker;
                Theme.ThemeAccent themeAccent2 = this.accent;
                int i14 = themeAccent2.myMessagesAccentColor;
                if (i14 == 0) {
                    i14 = themeAccent2.accentColor;
                }
                colorPicker5.setColor(i14, 0);
            }
        }
        Theme.refreshThemeColors();
        this.listView2.invalidateViews();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectColorType(int i) {
        selectColorType(i, true);
    }

    private void selectColorType(int i, boolean z) {
        ColorPicker colorPicker;
        float f;
        String string;
        DialogInterface.OnClickListener onClickListener;
        if (getParentActivity() == null || this.colorType == i || this.patternViewAnimation != null || this.accent == null) {
            return;
        }
        if (z && i == 2 && (Theme.hasCustomWallpaper() || this.accent.backgroundOverrideColor == 4294967296L)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString(R.string.ChangeChatBackground));
            if (!Theme.hasCustomWallpaper() || Theme.isCustomWallpaperColor()) {
                builder.setMessage(LocaleController.getString(R.string.ChangeColorToColor));
                builder.setPositiveButton(LocaleController.getString(R.string.Reset), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda30
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        ThemePreviewActivity.this.lambda$selectColorType$22(dialogInterface, i2);
                    }
                });
                string = LocaleController.getString(R.string.Continue);
                onClickListener = new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda31
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        ThemePreviewActivity.this.lambda$selectColorType$23(dialogInterface, i2);
                    }
                };
            } else {
                builder.setMessage(LocaleController.getString(R.string.ChangeWallpaperToColor));
                builder.setPositiveButton(LocaleController.getString(R.string.Change), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda29
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        ThemePreviewActivity.this.lambda$selectColorType$24(dialogInterface, i2);
                    }
                });
                string = LocaleController.getString(R.string.Cancel);
                onClickListener = null;
            }
            builder.setNegativeButton(string, onClickListener);
            showDialog(builder.create());
            return;
        }
        int i2 = this.colorType;
        this.colorType = i;
        if (i == 1) {
            this.dropDown.setText(LocaleController.getString(R.string.ColorPickerMainColor));
            this.colorPicker.setType(1, hasChanges(1), 2, this.accent.accentColor2 != 0 ? 2 : 1, false, 0, false);
            this.colorPicker.setColor(this.accent.accentColor, 0);
            int i3 = this.accent.accentColor2;
            if (i3 != 0) {
                this.colorPicker.setColor(i3, 1);
            }
            if (i2 == 2 || (i2 == 3 && this.accent.myMessagesGradientAccentColor2 != 0)) {
                this.messagesAdapter.notifyItemRemoved(0);
            }
        } else if (i == 2) {
            this.dropDown.setText(LocaleController.getString(R.string.ColorPickerBackground));
            int themedColor = getThemedColor(Theme.key_chat_wallpaper);
            int i4 = Theme.key_chat_wallpaper_gradient_to1;
            int themedColor2 = Theme.hasThemeKey(i4) ? getThemedColor(i4) : 0;
            int i5 = Theme.key_chat_wallpaper_gradient_to2;
            int themedColor3 = Theme.hasThemeKey(i5) ? getThemedColor(i5) : 0;
            int i6 = Theme.key_chat_wallpaper_gradient_to3;
            int themedColor4 = Theme.hasThemeKey(i6) ? getThemedColor(i6) : 0;
            Theme.ThemeAccent themeAccent = this.accent;
            long j = themeAccent.backgroundGradientOverrideColor1;
            int i7 = (int) j;
            if (i7 == 0 && j != 0) {
                themedColor2 = 0;
            }
            long j2 = themeAccent.backgroundGradientOverrideColor2;
            int i8 = (int) j2;
            if (i8 == 0 && j2 != 0) {
                themedColor3 = 0;
            }
            long j3 = themeAccent.backgroundGradientOverrideColor3;
            int i9 = (int) j3;
            if (i9 == 0 && j3 != 0) {
                themedColor4 = 0;
            }
            int i10 = (int) themeAccent.backgroundOverrideColor;
            this.colorPicker.setType(2, hasChanges(2), 4, (i7 == 0 && themedColor2 == 0) ? 1 : (i9 == 0 && themedColor4 == 0) ? (i8 == 0 && themedColor3 == 0) ? 2 : 3 : 4, false, this.accent.backgroundRotation, false);
            ColorPicker colorPicker2 = this.colorPicker;
            if (i9 == 0) {
                i9 = themedColor4;
            }
            colorPicker2.setColor(i9, 3);
            ColorPicker colorPicker3 = this.colorPicker;
            if (i8 == 0) {
                i8 = themedColor3;
            }
            colorPicker3.setColor(i8, 2);
            ColorPicker colorPicker4 = this.colorPicker;
            if (i7 == 0) {
                i7 = themedColor2;
            }
            colorPicker4.setColor(i7, 1);
            ColorPicker colorPicker5 = this.colorPicker;
            if (i10 != 0) {
                themedColor = i10;
            }
            colorPicker5.setColor(themedColor, 0);
            if (i2 == 1 || this.accent.myMessagesGradientAccentColor2 == 0) {
                this.messagesAdapter.notifyItemInserted(0);
            } else {
                this.messagesAdapter.notifyItemChanged(0);
            }
            this.listView2.smoothScrollBy(0, AndroidUtilities.dp(60.0f));
        } else if (i == 3) {
            this.dropDown.setText(LocaleController.getString(R.string.ColorPickerMyMessages));
            Theme.ThemeAccent themeAccent2 = this.accent;
            this.colorPicker.setType(2, hasChanges(3), 4, themeAccent2.myMessagesGradientAccentColor1 != 0 ? themeAccent2.myMessagesGradientAccentColor3 != 0 ? 4 : themeAccent2.myMessagesGradientAccentColor2 != 0 ? 3 : 2 : 1, true, 0, false);
            this.colorPicker.setColor(this.accent.myMessagesGradientAccentColor3, 3);
            this.colorPicker.setColor(this.accent.myMessagesGradientAccentColor2, 2);
            this.colorPicker.setColor(this.accent.myMessagesGradientAccentColor1, 1);
            ColorPicker colorPicker6 = this.colorPicker;
            Theme.ThemeAccent themeAccent3 = this.accent;
            int i11 = themeAccent3.myMessagesAccentColor;
            if (i11 == 0) {
                i11 = themeAccent3.accentColor;
            }
            colorPicker6.setColor(i11, 0);
            this.messagesCheckBoxView[1].setColor(0, this.accent.myMessagesAccentColor);
            this.messagesCheckBoxView[1].setColor(1, this.accent.myMessagesGradientAccentColor1);
            this.messagesCheckBoxView[1].setColor(2, this.accent.myMessagesGradientAccentColor2);
            this.messagesCheckBoxView[1].setColor(3, this.accent.myMessagesGradientAccentColor3);
            if (this.accent.myMessagesGradientAccentColor2 != 0) {
                MessagesAdapter messagesAdapter = this.messagesAdapter;
                if (i2 == 1) {
                    messagesAdapter.notifyItemInserted(0);
                } else {
                    messagesAdapter.notifyItemChanged(0);
                }
            } else if (i2 == 2) {
                this.messagesAdapter.notifyItemRemoved(0);
            }
            this.listView2.smoothScrollBy(0, AndroidUtilities.dp(60.0f));
            showAnimationHint();
        }
        if (i == 1 || i == 3) {
            if (i2 == 2 && this.patternLayout[1].getVisibility() == 0) {
                showPatternsView(0, true, true);
            }
            if (i == 1) {
                if (this.applyingTheme.isDark()) {
                    this.colorPicker.setMinBrightness(0.2f);
                    return;
                }
                this.colorPicker.setMinBrightness(0.05f);
                colorPicker = this.colorPicker;
                f = 0.8f;
                colorPicker.setMaxBrightness(f);
            }
        }
        this.colorPicker.setMinBrightness(0.0f);
        colorPicker = this.colorPicker;
        f = 1.0f;
        colorPicker.setMaxBrightness(f);
    }

    private void selectPattern(int i) {
        TLRPC.TL_wallPaper tL_wallPaper = (i < 0 || i >= this.patterns.size()) ? this.lastSelectedPattern : (TLRPC.TL_wallPaper) this.patterns.get(i);
        if (tL_wallPaper == null) {
            return;
        }
        ValueAnimator valueAnimator = this.valueAnimator;
        if (valueAnimator != null) {
            valueAnimator.removeAllListeners();
            this.valueAnimator.cancel();
        }
        BackgroundView[] backgroundViewArr = this.backgroundImages;
        BackgroundView backgroundView = backgroundViewArr[0];
        BackgroundView backgroundView2 = backgroundViewArr[1];
        backgroundViewArr[0] = backgroundView2;
        backgroundViewArr[1] = backgroundView;
        this.page2.removeView(backgroundView2);
        this.page2.addView(this.backgroundImages[0], this.page2.indexOfChild(this.backgroundImages[1]) + 1);
        BackgroundView[] backgroundViewArr2 = this.backgroundImages;
        BackgroundView backgroundView3 = backgroundViewArr2[0];
        this.backgroundImage = backgroundView3;
        backgroundView3.setBackground(backgroundViewArr2[1].getBackground());
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
        this.backgroundImage.getImageReceiver().setCrossfadeDuration(NotificationCenter.uploadStoryProgress);
        this.backgroundImage.getImageReceiver().setImage(ImageLocation.getForDocument(tL_wallPaper.document), this.imageFilter, null, null, null, tL_wallPaper.document.size, "jpg", tL_wallPaper, 1);
        this.backgroundImage.onNewImageSet();
        this.selectedPattern = tL_wallPaper;
        this.isMotion = this.backgroundCheckBoxView[2].isChecked();
        updateButtonState(false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBackgroundColor(int i, int i2, boolean z, boolean z2) {
        int i3;
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
            int i4 = 0;
            while (true) {
                WallpaperCheckBoxView[] wallpaperCheckBoxViewArr = this.backgroundCheckBoxView;
                if (i4 >= wallpaperCheckBoxViewArr.length) {
                    break;
                }
                WallpaperCheckBoxView wallpaperCheckBoxView = wallpaperCheckBoxViewArr[i4];
                if (wallpaperCheckBoxView != null) {
                    wallpaperCheckBoxView.setColor(i2, i);
                }
                i4++;
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
        } else {
            if (this.backgroundGradientColor1 != 0) {
                this.backgroundImage.setBackground(new GradientDrawable(BackgroundGradientDrawable.getGradientOrientation(this.backgroundRotation), new int[]{this.backgroundColor, this.backgroundGradientColor1}));
                i3 = AndroidUtilities.getAverageColor(this.backgroundColor, this.backgroundGradientColor1);
            } else {
                this.backgroundImage.setBackgroundColor(this.backgroundColor);
                i3 = this.backgroundColor;
            }
            int patternColor = AndroidUtilities.getPatternColor(i3);
            this.checkColor = patternColor;
            this.patternColor = patternColor;
        }
        int i5 = Theme.key_chat_serviceBackground;
        if (!Theme.hasThemeKey(i5) || (this.backgroundImage.getBackground() instanceof MotionBackgroundDrawable)) {
            ThemeDelegate themeDelegate = this.themeDelegate;
            int i6 = this.checkColor;
            themeDelegate.applyChatServiceMessageColor(new int[]{i6, i6, i6, i6}, this.backgroundImage.getBackground(), this.backgroundImage.getBackground(), Float.valueOf(this.currentIntensity));
        } else if (Theme.getCachedWallpaperNonBlocking() instanceof MotionBackgroundDrawable) {
            int themedColor = getThemedColor(i5);
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
            for (int i7 = 0; i7 < childCount; i7++) {
                this.backgroundButtonsContainer.getChildAt(i7).invalidate();
            }
        }
        FrameLayout frameLayout2 = this.messagesButtonsContainer;
        if (frameLayout2 != null) {
            int childCount2 = frameLayout2.getChildCount();
            for (int i8 = 0; i8 < childCount2; i8++) {
                this.messagesButtonsContainer.getChildAt(i8).invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:104:0x02a7  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0320  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0347  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x035b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setCurrentImage(boolean z) {
        int patternColor;
        BackgroundView backgroundView;
        ImageView imageView;
        ImageView imageView2;
        ColorDrawable colorDrawable;
        MotionBackgroundDrawable motionBackgroundDrawable;
        int i = this.screenType;
        if (i != 0 || this.accent != null) {
            if (i == 2) {
                Object obj = this.currentWallpaper;
                if (obj instanceof TLRPC.TL_wallPaper) {
                    TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) obj;
                    TLRPC.PhotoSize closestPhotoSizeWithSize = z ? FileLoader.getClosestPhotoSizeWithSize(tL_wallPaper.document.thumbs, 100) : null;
                    this.backgroundImage.setImage(ImageLocation.getForDocument(tL_wallPaper.document), this.imageFilter, ImageLocation.getForDocument(closestPhotoSizeWithSize, tL_wallPaper.document), "100_100_b", closestPhotoSizeWithSize instanceof TLRPC.TL_photoStrippedSize ? new BitmapDrawable(ImageLoader.getStrippedPhotoBitmap(closestPhotoSizeWithSize.bytes, "b")) : null, "jpg", tL_wallPaper.document.size, 1, tL_wallPaper);
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
                    TLRPC.TL_wallPaper tL_wallPaper2 = this.selectedPattern;
                    if (tL_wallPaper2 != null) {
                        BackgroundView backgroundView2 = this.backgroundImage;
                        ImageLocation forDocument = ImageLocation.getForDocument(tL_wallPaper2.document);
                        String str = this.imageFilter;
                        TLRPC.TL_wallPaper tL_wallPaper3 = this.selectedPattern;
                        backgroundView2.setImage(forDocument, str, null, null, "jpg", tL_wallPaper3.document.size, 1, tL_wallPaper3);
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
                        if (file != null || (file = fileWallpaper.path) != null) {
                            this.backgroundImage.setImage(file.getAbsolutePath(), this.imageFilter, null);
                        } else if ("t".equals(fileWallpaper.slug)) {
                            BackgroundView backgroundView3 = this.backgroundImage;
                            backgroundView3.setImageDrawable(Theme.getThemedWallpaper(false, backgroundView3));
                        } else {
                            int i3 = fileWallpaper.resId;
                            if (i3 != 0) {
                                this.backgroundImage.setImageResource(i3);
                            }
                        }
                    }
                } else if (obj instanceof MediaController.SearchImage) {
                    MediaController.SearchImage searchImage = (MediaController.SearchImage) obj;
                    TLRPC.Photo photo = searchImage.photo;
                    if (photo != null) {
                        TLRPC.PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 100);
                        TLRPC.PhotoSize closestPhotoSizeWithSize3 = FileLoader.getClosestPhotoSizeWithSize(searchImage.photo.sizes, this.maxWallpaperSize, true);
                        TLRPC.PhotoSize photoSize = closestPhotoSizeWithSize3 != closestPhotoSizeWithSize2 ? closestPhotoSizeWithSize3 : null;
                        this.backgroundImage.setImage(ImageLocation.getForPhoto(photoSize, searchImage.photo), this.imageFilter, ImageLocation.getForPhoto(closestPhotoSizeWithSize2, searchImage.photo), "100_100_b", "jpg", photoSize != null ? photoSize.size : 0, 1, searchImage);
                    } else {
                        this.backgroundImage.setImage(searchImage.imageUrl, this.imageFilter, searchImage.thumbUrl, "100_100_b");
                    }
                }
            } else if (this.accent != null) {
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
                if (TextUtils.isEmpty(themeAccent.patternSlug) || Theme.hasCustomWallpaper()) {
                    Drawable cachedWallpaperNonBlocking = Theme.getCachedWallpaperNonBlocking();
                    if (cachedWallpaperNonBlocking != null) {
                        if (cachedWallpaperNonBlocking instanceof MotionBackgroundDrawable) {
                            ((MotionBackgroundDrawable) cachedWallpaperNonBlocking).setParentView(this.backgroundImage);
                        }
                        this.backgroundImage.setBackground(cachedWallpaperNonBlocking);
                    }
                } else {
                    if (defaultAccentColor3 != 0) {
                        Drawable background = this.backgroundImage.getBackground();
                        if (background instanceof MotionBackgroundDrawable) {
                            motionBackgroundDrawable = (MotionBackgroundDrawable) background;
                        } else {
                            MotionBackgroundDrawable motionBackgroundDrawable2 = new MotionBackgroundDrawable();
                            motionBackgroundDrawable2.setParentView(this.backgroundImage);
                            motionBackgroundDrawable = motionBackgroundDrawable2;
                            if (this.rotatePreview) {
                                motionBackgroundDrawable2.rotatePreview(false);
                                motionBackgroundDrawable = motionBackgroundDrawable2;
                            }
                        }
                        motionBackgroundDrawable.setColors(defaultAccentColor, defaultAccentColor2, defaultAccentColor3, defaultAccentColor4);
                        colorDrawable = motionBackgroundDrawable;
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
                        colorDrawable = backgroundGradientDrawable;
                    } else {
                        colorDrawable = new ColorDrawable(defaultAccentColor);
                    }
                    this.backgroundImage.setBackground(colorDrawable);
                    TLRPC.TL_wallPaper tL_wallPaper4 = this.selectedPattern;
                    if (tL_wallPaper4 != null) {
                        BackgroundView backgroundView4 = this.backgroundImage;
                        ImageLocation forDocument2 = ImageLocation.getForDocument(tL_wallPaper4.document);
                        String str2 = this.imageFilter;
                        TLRPC.TL_wallPaper tL_wallPaper5 = this.selectedPattern;
                        backgroundView4.setImage(forDocument2, str2, null, null, "jpg", tL_wallPaper5.document.size, 1, tL_wallPaper5);
                    }
                }
                if (defaultAccentColor2 == 0) {
                    patternColor = AndroidUtilities.getPatternColor(defaultAccentColor);
                } else if (defaultAccentColor3 != 0) {
                    this.patternColor = MotionBackgroundDrawable.getPatternColor(defaultAccentColor, defaultAccentColor2, defaultAccentColor3, defaultAccentColor4);
                    this.checkColor = 754974720;
                    backgroundView = this.backgroundImage;
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
                    imageView = this.backgroundPlayAnimationImageView;
                    if (imageView != null) {
                        imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_serviceText), PorterDuff.Mode.MULTIPLY));
                    }
                    imageView2 = this.messagesPlayAnimationImageView;
                    if (imageView2 != null) {
                        imageView2.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_serviceText), PorterDuff.Mode.MULTIPLY));
                    }
                    invalidateBlur();
                } else {
                    patternColor = AndroidUtilities.getPatternColor(AndroidUtilities.getAverageColor(defaultAccentColor, defaultAccentColor2));
                }
                this.checkColor = patternColor;
                this.patternColor = patternColor;
                backgroundView = this.backgroundImage;
                if (backgroundView != null) {
                }
                if (this.backgroundCheckBoxView != null) {
                }
                imageView = this.backgroundPlayAnimationImageView;
                if (imageView != null) {
                }
                imageView2 = this.messagesPlayAnimationImageView;
                if (imageView2 != null) {
                }
                invalidateBlur();
            }
            this.rotatePreview = false;
        }
        this.backgroundImage.setBackground(Theme.getCachedWallpaper());
        this.rotatePreview = false;
    }

    private void setCurrentServerWallpaper(MessageObject messageObject) {
        this.serverWallpaper = messageObject;
    }

    private void setVisiblePart(ChatActionCell chatActionCell) {
        float f;
        if (this.backgroundImage == null) {
            return;
        }
        if (this.themeDelegate.serviceBitmap != null) {
            float width = this.themeDelegate.serviceBitmap.getWidth();
            f = ((this.backgroundImage.getMeasuredWidth() - (width * Math.max(this.backgroundImage.getMeasuredWidth() / width, this.backgroundImage.getMeasuredHeight() / this.themeDelegate.serviceBitmap.getHeight()))) / 2.0f) + this.currentScrollOffset + 0.0f;
        } else {
            f = this.currentScrollOffset + 0.0f;
        }
        chatActionCell.setVisiblePart(chatActionCell.getY() - ((-this.backgroundImage.ty) + 0.0f), f, this.backgroundImage.getMeasuredHeight(), this.shouldShowBrightnessControll ? this.dimAmount * this.progressToDarkTheme : 0.0f);
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
            this.animationHint.setText(LocaleController.getString(R.string.BackgroundAnimateInfo));
            this.animationHint.setExtraTranslationY(AndroidUtilities.dp(6.0f));
            this.frameLayout.addView(this.animationHint, LayoutHelper.createFrame(-2, -2.0f, 51, 10.0f, 0.0f, 10.0f, 0.0f));
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ThemePreviewActivity.this.lambda$showAnimationHint$32(globalMainSettings);
            }
        }, 500L);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void showFor(final ChatActivity chatActivity, MessageObject messageObject) {
        TLRPC.WallPaper wallPaper;
        TLRPC.MessageAction messageAction = messageObject.messageOwner.action;
        if (messageAction instanceof TLRPC.TL_messageActionSetChatWallPaper) {
            TLRPC.WallPaper wallPaper2 = ((TLRPC.TL_messageActionSetChatWallPaper) messageAction).wallpaper;
            if (wallPaper2.pattern || wallPaper2.document == null) {
                String str = wallPaper2.slug;
                TLRPC.WallPaperSettings wallPaperSettings = wallPaper2.settings;
                WallpapersListActivity.ColorWallpaper colorWallpaper = new WallpapersListActivity.ColorWallpaper(str, wallPaperSettings.background_color, wallPaperSettings.second_background_color, wallPaperSettings.third_background_color, wallPaperSettings.fourth_background_color, AndroidUtilities.getWallpaperRotation(wallPaperSettings.rotation, false), r3.intensity / 100.0f, wallPaper2.settings.motion, null);
                if (wallPaper2 instanceof TLRPC.TL_wallPaper) {
                    colorWallpaper.pattern = (TLRPC.TL_wallPaper) wallPaper2;
                }
                wallPaper = colorWallpaper;
            } else {
                wallPaper = wallPaper2;
            }
            final boolean isDark = Theme.getActiveTheme().isDark();
            ThemePreviewActivity themePreviewActivity = new ThemePreviewActivity(wallPaper, null, true, false) { // from class: org.telegram.ui.ThemePreviewActivity.3
                @Override // org.telegram.ui.ActionBar.BaseFragment
                public void onFragmentClosed() {
                    super.onFragmentClosed();
                    ChatActivity.ThemeDelegate themeDelegate = chatActivity.themeDelegate;
                    themeDelegate.setCurrentTheme(themeDelegate.getCurrentTheme(), chatActivity.themeDelegate.getCurrentWallpaper(), false, Boolean.valueOf(isDark));
                }
            };
            TLRPC.WallPaperSettings wallPaperSettings2 = wallPaper2.settings;
            if (wallPaperSettings2 != null) {
                themePreviewActivity.setInitialModes(wallPaperSettings2.blur, wallPaperSettings2.motion, wallPaperSettings2.intensity / 100.0f);
            }
            themePreviewActivity.setCurrentServerWallpaper(messageObject);
            themePreviewActivity.setDialogId(messageObject.getDialogId());
            themePreviewActivity.setResourceProvider(chatActivity.themeDelegate);
            themePreviewActivity.setOnSwitchDayNightDelegate(new DayNightSwitchDelegate(isDark, chatActivity) { // from class: org.telegram.ui.ThemePreviewActivity.4
                boolean forceDark;
                final /* synthetic */ ChatActivity val$chatActivity;
                final /* synthetic */ boolean val$initialIsDark;

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
                public boolean supportsAnimation() {
                    return true;
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

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:129:0x03e6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void showPatternsView(final int i, final boolean z, boolean z2) {
        FrameLayout frameLayout;
        int i2;
        FrameLayout frameLayout2;
        FrameLayout frameLayout3;
        FrameLayout frameLayout4;
        ObjectAnimator ofFloat;
        ObjectAnimator ofFloat2;
        int indexOf;
        final boolean z3 = z && i == 1 && this.selectedPattern != null;
        if (z) {
            if (i != 0) {
                this.previousSelectedPattern = this.selectedPattern;
                this.previousIntensity = this.currentIntensity;
                this.patternsAdapter.notifyDataSetChanged();
                ArrayList arrayList = this.patterns;
                if (arrayList != null) {
                    TLRPC.TL_wallPaper tL_wallPaper = this.selectedPattern;
                    if (tL_wallPaper == null) {
                        indexOf = 0;
                    } else {
                        indexOf = arrayList.indexOf(tL_wallPaper) + (this.screenType == 2 ? 1 : 0);
                    }
                    this.patternsLayoutManager.scrollToPositionWithOffset(indexOf, (this.patternsListView.getMeasuredWidth() - AndroidUtilities.dp(124.0f)) / 2);
                }
            } else if (this.screenType == 2) {
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
        if (!z2) {
            char c = i == 0 ? (char) 1 : (char) 0;
            if (z) {
                this.patternLayout[i].setVisibility(0);
                int i8 = this.screenType;
                if (i8 == 1) {
                    this.listView2.setTranslationY(i == 1 ? -AndroidUtilities.dp(21.0f) : 0.0f);
                    this.backgroundCheckBoxView[2].setAlpha(z3 ? 1.0f : 0.0f);
                    this.backgroundCheckBoxView[0].setAlpha(z3 ? 0.0f : 1.0f);
                    FrameLayout[] frameLayoutArr = this.patternLayout;
                    if (i == 1) {
                        frameLayout3 = frameLayoutArr[i];
                        frameLayout3.setAlpha(1.0f);
                    } else {
                        frameLayoutArr[i].setAlpha(1.0f);
                        frameLayout2 = this.patternLayout[c];
                        frameLayout2.setAlpha(0.0f);
                    }
                } else if (i8 == 2) {
                    this.listView2.setTranslationY((-AndroidUtilities.dp(i == 0 ? 343.0f : 316.0f)) + AndroidUtilities.dp((this.applyButton2 == null ? 0 : 58) + 72) + (insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0));
                    this.backgroundCheckBoxView[2].setAlpha(z3 ? 1.0f : 0.0f);
                    this.backgroundCheckBoxView[0].setAlpha(z3 ? 0.0f : 1.0f);
                    if (this.patternLayout[c].getVisibility() == 0) {
                        this.patternLayout[c].setAlpha(0.0f);
                        this.patternLayout[i].setAlpha(1.0f);
                        frameLayout4 = this.patternLayout[i];
                    } else {
                        frameLayout4 = this.patternLayout[i];
                    }
                    frameLayout4.setTranslationY(0.0f);
                } else {
                    FrameLayout[] frameLayoutArr2 = this.patternLayout;
                    if (i == 1) {
                        frameLayout3 = frameLayoutArr2[i];
                        frameLayout3.setAlpha(1.0f);
                    } else {
                        frameLayoutArr2[i].setAlpha(1.0f);
                        frameLayout2 = this.patternLayout[c];
                        frameLayout2.setAlpha(0.0f);
                    }
                }
                this.colorPicker.hideKeyboard();
            } else {
                this.listView2.setTranslationY(0.0f);
                this.patternLayout[i].setTranslationY(r8.getMeasuredHeight());
                this.backgroundCheckBoxView[0].setAlpha(1.0f);
                this.backgroundCheckBoxView[2].setAlpha(1.0f);
                this.backgroundImage.setAlpha(1.0f);
            }
            if (!z || this.patternLayout[c].getVisibility() != 0) {
                if (!z) {
                    frameLayout = this.patternLayout[i];
                }
                i2 = this.screenType;
                if (i2 != 1 || i2 == 2) {
                    this.backgroundCheckBoxView[z3 ? (char) 0 : (char) 2].setVisibility(4);
                    return;
                } else {
                    if (i == 1) {
                        this.patternLayout[c].setAlpha(0.0f);
                        return;
                    }
                    return;
                }
            }
            this.patternLayout[c].setAlpha(1.0f);
            frameLayout = this.patternLayout[c];
            frameLayout.setVisibility(4);
            i2 = this.screenType;
            if (i2 != 1) {
            }
            this.backgroundCheckBoxView[z3 ? (char) 0 : (char) 2].setVisibility(4);
            return;
        }
        this.patternViewAnimation = new AnimatorSet();
        ArrayList arrayList2 = new ArrayList();
        int i9 = i == 0 ? 1 : 0;
        if (z) {
            this.patternLayout[i].setVisibility(0);
            int i10 = this.screenType;
            if (i10 == 1) {
                arrayList2.add(ObjectAnimator.ofFloat(this.listView2, (Property<RecyclerListView, Float>) View.TRANSLATION_Y, i == 1 ? -AndroidUtilities.dp(21.0f) : 0.0f));
                WallpaperCheckBoxView wallpaperCheckBoxView = this.backgroundCheckBoxView[2];
                Property property = View.ALPHA;
                arrayList2.add(ObjectAnimator.ofFloat(wallpaperCheckBoxView, (Property<WallpaperCheckBoxView, Float>) property, z3 ? 1.0f : 0.0f));
                arrayList2.add(ObjectAnimator.ofFloat(this.backgroundCheckBoxView[0], (Property<WallpaperCheckBoxView, Float>) property, z3 ? 0.0f : 1.0f));
                if (i == 1) {
                    ofFloat2 = ObjectAnimator.ofFloat(this.patternLayout[i], (Property<FrameLayout, Float>) property, 0.0f, 1.0f);
                } else {
                    this.patternLayout[i].setAlpha(1.0f);
                    ofFloat2 = ObjectAnimator.ofFloat(this.patternLayout[i9], (Property<FrameLayout, Float>) property, 0.0f);
                }
            } else if (i10 == 2) {
                RecyclerListView recyclerListView = this.listView2;
                Property property2 = View.TRANSLATION_Y;
                arrayList2.add(ObjectAnimator.ofFloat(recyclerListView, (Property<RecyclerListView, Float>) property2, (-this.patternLayout[i].getMeasuredHeight()) + AndroidUtilities.dp((this.applyButton2 == null ? 0 : 58) + 72) + (insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0)));
                WallpaperCheckBoxView wallpaperCheckBoxView2 = this.backgroundCheckBoxView[2];
                Property property3 = View.ALPHA;
                arrayList2.add(ObjectAnimator.ofFloat(wallpaperCheckBoxView2, (Property<WallpaperCheckBoxView, Float>) property3, z3 ? 1.0f : 0.0f));
                arrayList2.add(ObjectAnimator.ofFloat(this.backgroundCheckBoxView[0], (Property<WallpaperCheckBoxView, Float>) property3, z3 ? 0.0f : 1.0f));
                if (this.patternLayout[i9].getVisibility() == 0) {
                    arrayList2.add(ObjectAnimator.ofFloat(this.patternLayout[i9], (Property<FrameLayout, Float>) property3, 0.0f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.patternLayout[i], (Property<FrameLayout, Float>) property3, 0.0f, 1.0f));
                    this.patternLayout[i].setTranslationY(0.0f);
                    this.patternViewAnimation.playTogether(arrayList2);
                    final int i11 = i9;
                    this.patternViewAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.36
                        /* JADX WARN: Removed duplicated region for block: B:19:0x0077  */
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                        */
                        public void onAnimationEnd(Animator animator) {
                            FrameLayout frameLayout5;
                            ThemePreviewActivity.this.patternViewAnimation = null;
                            if (!z || ThemePreviewActivity.this.patternLayout[i11].getVisibility() != 0) {
                                if (!z) {
                                    frameLayout5 = ThemePreviewActivity.this.patternLayout[i];
                                }
                                if (ThemePreviewActivity.this.screenType != 1 || ThemePreviewActivity.this.screenType == 2) {
                                    ThemePreviewActivity.this.backgroundCheckBoxView[z3 ? (char) 0 : (char) 2].setVisibility(4);
                                } else {
                                    if (i == 1) {
                                        ThemePreviewActivity.this.patternLayout[i11].setAlpha(0.0f);
                                        return;
                                    }
                                    return;
                                }
                            }
                            ThemePreviewActivity.this.patternLayout[i11].setAlpha(1.0f);
                            frameLayout5 = ThemePreviewActivity.this.patternLayout[i11];
                            frameLayout5.setVisibility(4);
                            if (ThemePreviewActivity.this.screenType != 1) {
                            }
                            ThemePreviewActivity.this.backgroundCheckBoxView[z3 ? (char) 0 : (char) 2].setVisibility(4);
                        }
                    });
                    this.patternViewAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                    this.patternViewAnimation.setDuration(200L);
                    this.patternViewAnimation.start();
                }
                ofFloat = ObjectAnimator.ofFloat(this.patternLayout[i], (Property<FrameLayout, Float>) property2, r8.getMeasuredHeight(), 0.0f);
            } else if (i == 1) {
                ofFloat2 = ObjectAnimator.ofFloat(this.patternLayout[i], (Property<FrameLayout, Float>) View.ALPHA, 0.0f, 1.0f);
            } else {
                this.patternLayout[i].setAlpha(1.0f);
                ofFloat2 = ObjectAnimator.ofFloat(this.patternLayout[i9], (Property<FrameLayout, Float>) View.ALPHA, 0.0f);
            }
            arrayList2.add(ofFloat2);
            this.colorPicker.hideKeyboard();
            this.patternViewAnimation.playTogether(arrayList2);
            final int i112 = i9;
            this.patternViewAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.36
                /* JADX WARN: Removed duplicated region for block: B:19:0x0077  */
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void onAnimationEnd(Animator animator) {
                    FrameLayout frameLayout5;
                    ThemePreviewActivity.this.patternViewAnimation = null;
                    if (!z || ThemePreviewActivity.this.patternLayout[i112].getVisibility() != 0) {
                        if (!z) {
                            frameLayout5 = ThemePreviewActivity.this.patternLayout[i];
                        }
                        if (ThemePreviewActivity.this.screenType != 1 || ThemePreviewActivity.this.screenType == 2) {
                            ThemePreviewActivity.this.backgroundCheckBoxView[z3 ? (char) 0 : (char) 2].setVisibility(4);
                        } else {
                            if (i == 1) {
                                ThemePreviewActivity.this.patternLayout[i112].setAlpha(0.0f);
                                return;
                            }
                            return;
                        }
                    }
                    ThemePreviewActivity.this.patternLayout[i112].setAlpha(1.0f);
                    frameLayout5 = ThemePreviewActivity.this.patternLayout[i112];
                    frameLayout5.setVisibility(4);
                    if (ThemePreviewActivity.this.screenType != 1) {
                    }
                    ThemePreviewActivity.this.backgroundCheckBoxView[z3 ? (char) 0 : (char) 2].setVisibility(4);
                }
            });
            this.patternViewAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            this.patternViewAnimation.setDuration(200L);
            this.patternViewAnimation.start();
        }
        RecyclerListView recyclerListView2 = this.listView2;
        Property property4 = View.TRANSLATION_Y;
        arrayList2.add(ObjectAnimator.ofFloat(recyclerListView2, (Property<RecyclerListView, Float>) property4, 0.0f));
        arrayList2.add(ObjectAnimator.ofFloat(this.patternLayout[i], (Property<FrameLayout, Float>) property4, r8.getMeasuredHeight()));
        WallpaperCheckBoxView wallpaperCheckBoxView3 = this.backgroundCheckBoxView[0];
        Property property5 = View.ALPHA;
        arrayList2.add(ObjectAnimator.ofFloat(wallpaperCheckBoxView3, (Property<WallpaperCheckBoxView, Float>) property5, 1.0f));
        arrayList2.add(ObjectAnimator.ofFloat(this.backgroundCheckBoxView[2], (Property<WallpaperCheckBoxView, Float>) property5, 0.0f));
        ofFloat = ObjectAnimator.ofFloat(this.backgroundImage, (Property<BackgroundView, Float>) property5, 1.0f);
        arrayList2.add(ofFloat);
        this.patternViewAnimation.playTogether(arrayList2);
        final int i1122 = i9;
        this.patternViewAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.36
            /* JADX WARN: Removed duplicated region for block: B:19:0x0077  */
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void onAnimationEnd(Animator animator) {
                FrameLayout frameLayout5;
                ThemePreviewActivity.this.patternViewAnimation = null;
                if (!z || ThemePreviewActivity.this.patternLayout[i1122].getVisibility() != 0) {
                    if (!z) {
                        frameLayout5 = ThemePreviewActivity.this.patternLayout[i];
                    }
                    if (ThemePreviewActivity.this.screenType != 1 || ThemePreviewActivity.this.screenType == 2) {
                        ThemePreviewActivity.this.backgroundCheckBoxView[z3 ? (char) 0 : (char) 2].setVisibility(4);
                    } else {
                        if (i == 1) {
                            ThemePreviewActivity.this.patternLayout[i1122].setAlpha(0.0f);
                            return;
                        }
                        return;
                    }
                }
                ThemePreviewActivity.this.patternLayout[i1122].setAlpha(1.0f);
                frameLayout5 = ThemePreviewActivity.this.patternLayout[i1122];
                frameLayout5.setVisibility(4);
                if (ThemePreviewActivity.this.screenType != 1) {
                }
                ThemePreviewActivity.this.backgroundCheckBoxView[z3 ? (char) 0 : (char) 2].setVisibility(4);
            }
        });
        this.patternViewAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
        this.patternViewAnimation.setDuration(200L);
        this.patternViewAnimation.start();
    }

    private void updateApplyButton1(boolean z) {
        BlurButton blurButton;
        int i;
        String formatString;
        long j = this.dialogId;
        if (j > 0) {
            blurButton = this.applyButton1;
            i = R.string.ApplyWallpaperForMe;
        } else {
            if (j < 0) {
                TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
                if (chat == null) {
                    blurButton = this.applyButton1;
                    formatString = LocaleController.formatString(R.string.ApplyWallpaperForChannel, LocaleController.getString(R.string.AccDescrChannel).toLowerCase());
                    blurButton.setText(formatString);
                }
                this.applyButton1.setText(LocaleController.formatString(R.string.ApplyWallpaperForChannel, chat.title));
                TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = this.boostsStatus;
                if (tL_premium_boostsStatus == null || tL_premium_boostsStatus.level >= getCustomWallpaperLevelMin()) {
                    if (this.boostsStatus == null) {
                        checkBoostsLevel();
                        return;
                    } else {
                        this.applyButton1.setSubText(null, z);
                        return;
                    }
                }
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("l");
                if (this.lockSpan == null) {
                    ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_switch_lock);
                    this.lockSpan = coloredImageSpan;
                    coloredImageSpan.setTopOffset(1);
                }
                spannableStringBuilder.setSpan(this.lockSpan, 0, 1, 33);
                spannableStringBuilder.append((CharSequence) " ").append((CharSequence) LocaleController.formatPluralString("ReactionLevelRequiredBtn", getCustomWallpaperLevelMin(), new Object[0]));
                this.applyButton1.setSubText(spannableStringBuilder, z);
                return;
            }
            blurButton = this.applyButton1;
            i = R.string.ApplyWallpaper;
        }
        formatString = LocaleController.getString(i);
        blurButton.setText(formatString);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBlurred() {
        if (this.isBlurred && this.blurredBitmap == null) {
            Bitmap bitmap = this.currentWallpaperBitmap;
            if (bitmap != null) {
                this.originalBitmap = bitmap;
            } else {
                ImageReceiver imageReceiver = this.backgroundImage.getImageReceiver();
                if (imageReceiver.hasNotThumb() || imageReceiver.hasStaticThumb()) {
                    this.originalBitmap = imageReceiver.getBitmap();
                    bitmap = imageReceiver.getBitmap();
                }
            }
            this.blurredBitmap = Utilities.blurWallpaper(bitmap);
        }
        if (!this.isBlurred) {
            setCurrentImage(false);
            return;
        }
        Bitmap bitmap2 = this.blurredBitmap;
        if (bitmap2 != null) {
            this.backgroundImage.setImageBitmap(bitmap2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x00e2, code lost:
    
        if (r10 != false) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00e4, code lost:
    
        r0 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00e6, code lost:
    
        r11.setAlpha(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0103, code lost:
    
        if (r10 != false) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x010d, code lost:
    
        if (r10 != false) goto L53;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateButtonState(boolean z, boolean z2) {
        File httpFilePath;
        String name;
        int i;
        long j;
        File file;
        String str;
        View view;
        FrameLayout frameLayout;
        ActionBar actionBar;
        Object obj = this.selectedPattern;
        if (obj == null) {
            obj = this.currentWallpaper;
        }
        boolean z3 = obj instanceof TLRPC.TL_wallPaper;
        if (!z3 && !(obj instanceof MediaController.SearchImage)) {
            return;
        }
        if (z3) {
            TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) obj;
            str = FileLoader.getAttachFileName(tL_wallPaper.document);
            if (TextUtils.isEmpty(str)) {
                return;
            }
            file = FileLoader.getInstance(this.currentAccount).getPathToAttach(tL_wallPaper.document, true);
            j = tL_wallPaper.document.size;
        } else {
            MediaController.SearchImage searchImage = (MediaController.SearchImage) obj;
            TLRPC.Photo photo = searchImage.photo;
            if (photo != null) {
                TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, this.maxWallpaperSize, true);
                httpFilePath = FileLoader.getInstance(this.currentAccount).getPathToAttach(closestPhotoSizeWithSize, true);
                name = FileLoader.getAttachFileName(closestPhotoSizeWithSize);
                i = closestPhotoSizeWithSize.size;
            } else {
                httpFilePath = ImageLoader.getHttpFilePath(searchImage.imageUrl, "jpg");
                name = httpFilePath.getName();
                i = searchImage.size;
            }
            j = i;
            String str2 = name;
            file = httpFilePath;
            str = str2;
            if (TextUtils.isEmpty(str)) {
                return;
            }
        }
        boolean exists = file.exists();
        String str3 = null;
        if (exists) {
            DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
            this.backgroundImage.invalidate();
            if (this.screenType == 2) {
                if (j == 0 || this.dialogId != 0) {
                    actionBar = this.actionBar2;
                } else {
                    actionBar = this.actionBar2;
                    str3 = AndroidUtilities.formatFileSize(j);
                }
                actionBar.setSubtitle(str3);
            }
        } else {
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(str, null, this);
            if (this.screenType == 2 && this.dialogId == 0) {
                this.actionBar2.setSubtitle(LocaleController.getString(R.string.LoadingFullImage));
            }
            this.backgroundImage.invalidate();
        }
        float f = 0.5f;
        if (this.selectedPattern == null && (frameLayout = this.backgroundButtonsContainer) != null) {
            frameLayout.setAlpha(exists ? 1.0f : 0.5f);
        }
        int i2 = this.screenType;
        if (i2 == 0) {
            this.doneButton.setEnabled(exists);
            view = this.doneButton;
        } else if (i2 == 2) {
            this.bottomOverlayChat.setEnabled(exists);
            BlurButton blurButton = this.applyButton1;
            if (blurButton != null) {
                blurButton.setAlpha(exists ? 1.0f : 0.5f);
            }
            view = this.applyButton2;
            if (view == null) {
            }
        } else {
            this.saveItem.setEnabled(exists);
            view = this.saveItem;
        }
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

    private void updateMotionButton() {
        int i = this.screenType;
        if (i == 1 || i == 2) {
            if (this.selectedPattern == null && (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                this.backgroundCheckBoxView[2].setChecked(false, true);
            }
            this.backgroundCheckBoxView[this.selectedPattern != null ? (char) 2 : (char) 0].setVisibility(0);
            AnimatorSet animatorSet = new AnimatorSet();
            WallpaperCheckBoxView wallpaperCheckBoxView = this.backgroundCheckBoxView[2];
            Property property = View.ALPHA;
            animatorSet.playTogether(ObjectAnimator.ofFloat(wallpaperCheckBoxView, (Property<WallpaperCheckBoxView, Float>) property, this.selectedPattern != null ? 1.0f : 0.0f), ObjectAnimator.ofFloat(this.backgroundCheckBoxView[0], (Property<WallpaperCheckBoxView, Float>) property, this.selectedPattern != null ? 0.0f : 1.0f));
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
        TLRPC.TL_wallPaper tL_wallPaper = this.selectedPattern;
        if (isEnabled == (tL_wallPaper != null)) {
            return;
        }
        if (tL_wallPaper == null) {
            this.backgroundCheckBoxView[0].setChecked(false, true);
        }
        this.backgroundCheckBoxView[0].setEnabled(this.selectedPattern != null);
        if (this.selectedPattern != null) {
            this.backgroundCheckBoxView[0].setVisibility(0);
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.backgroundCheckBoxView[1].getLayoutParams();
        AnimatorSet animatorSet2 = new AnimatorSet();
        int dp = (layoutParams.width + AndroidUtilities.dp(9.0f)) / 2;
        animatorSet2.playTogether(ObjectAnimator.ofFloat(this.backgroundCheckBoxView[0], (Property<WallpaperCheckBoxView, Float>) View.ALPHA, this.selectedPattern == null ? 0.0f : 1.0f));
        WallpaperCheckBoxView wallpaperCheckBoxView2 = this.backgroundCheckBoxView[0];
        Property property2 = View.TRANSLATION_X;
        animatorSet2.playTogether(ObjectAnimator.ofFloat(wallpaperCheckBoxView2, (Property<WallpaperCheckBoxView, Float>) property2, this.selectedPattern != null ? 0.0f : dp));
        animatorSet2.playTogether(ObjectAnimator.ofFloat(this.backgroundCheckBoxView[1], (Property<WallpaperCheckBoxView, Float>) property2, this.selectedPattern == null ? -dp : 0.0f));
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

    /* JADX WARN: Code restructure failed: missing block: B:105:0x008d, code lost:
    
        if (r5 != 0) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0072, code lost:
    
        if (r19.backgroundGradientColor1 != 0) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x008f, code lost:
    
        r5 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updatePlayAnimationView(boolean z) {
        boolean z2;
        int i;
        BlendMode blendMode;
        if (Build.VERSION.SDK_INT >= 29) {
            int i2 = this.screenType;
            if (i2 == 0) {
                Theme.ThemeAccent themeAccent = this.accent;
                i = themeAccent != null ? (int) themeAccent.backgroundGradientOverrideColor2 : getThemedColor(Theme.key_chat_wallpaper_gradient_to2);
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
                if (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) {
                    i = this.backgroundGradientColor2;
                }
                i = 0;
            }
            if (i == 0 || this.currentIntensity < 0.0f) {
                this.backgroundImage.getImageReceiver().setBlendMode(null);
            } else {
                ImageReceiver imageReceiver = this.backgroundImage.getImageReceiver();
                blendMode = BlendMode.SOFT_LIGHT;
                imageReceiver.setBlendMode(blendMode);
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
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.backgroundPlayAnimationView, (Property<FrameLayout, Float>) View.ALPHA, z2 ? 1.0f : 0.0f);
                    ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.backgroundPlayAnimationView, (Property<FrameLayout, Float>) View.SCALE_X, z2 ? 1.0f : 0.0f);
                    ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.backgroundPlayAnimationView, (Property<FrameLayout, Float>) View.SCALE_Y, z2 ? 1.0f : 0.0f);
                    WallpaperCheckBoxView wallpaperCheckBoxView = this.backgroundCheckBoxView[0];
                    Property property = View.TRANSLATION_X;
                    animatorSet2.playTogether(ofFloat, ofFloat2, ofFloat3, ObjectAnimator.ofFloat(wallpaperCheckBoxView, (Property<WallpaperCheckBoxView, Float>) property, z2 ? AndroidUtilities.dp(34.0f) : 0.0f), ObjectAnimator.ofFloat(this.backgroundCheckBoxView[1], (Property<WallpaperCheckBoxView, Float>) property, z2 ? -AndroidUtilities.dp(34.0f) : 0.0f), ObjectAnimator.ofFloat(this.backgroundCheckBoxView[2], (Property<WallpaperCheckBoxView, Float>) property, z2 ? AndroidUtilities.dp(34.0f) : 0.0f));
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
        FrameLayout frameLayout = this.messagesPlayAnimationView;
        if (frameLayout != null) {
            boolean z4 = frameLayout.getTag() != null;
            this.messagesPlayAnimationView.setTag(1);
            if (!z4) {
                this.messagesPlayAnimationView.setVisibility(0);
                AnimatorSet animatorSet3 = this.messagesPlayViewAnimator;
                if (animatorSet3 != null) {
                    animatorSet3.cancel();
                }
                if (!z) {
                    this.messagesPlayAnimationView.setAlpha(1.0f);
                    this.messagesPlayAnimationView.setScaleX(1.0f);
                    this.messagesPlayAnimationView.setScaleY(1.0f);
                    this.messagesCheckBoxView[0].setTranslationX(-AndroidUtilities.dp(34.0f));
                    this.messagesCheckBoxView[1].setTranslationX(AndroidUtilities.dp(34.0f));
                    return;
                }
                AnimatorSet animatorSet4 = new AnimatorSet();
                this.messagesPlayViewAnimator = animatorSet4;
                ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.messagesPlayAnimationView, (Property<FrameLayout, Float>) View.ALPHA, 1.0f);
                ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(this.messagesPlayAnimationView, (Property<FrameLayout, Float>) View.SCALE_X, 1.0f);
                ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(this.messagesPlayAnimationView, (Property<FrameLayout, Float>) View.SCALE_Y, 1.0f);
                WallpaperCheckBoxView wallpaperCheckBoxView2 = this.messagesCheckBoxView[0];
                Property property2 = View.TRANSLATION_X;
                animatorSet4.playTogether(ofFloat4, ofFloat5, ofFloat6, ObjectAnimator.ofFloat(wallpaperCheckBoxView2, (Property<WallpaperCheckBoxView, Float>) property2, -AndroidUtilities.dp(34.0f)), ObjectAnimator.ofFloat(this.messagesCheckBoxView[1], (Property<WallpaperCheckBoxView, Float>) property2, AndroidUtilities.dp(34.0f)));
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
            }
        }
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

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0061, code lost:
    
        if (r1.pattern == false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x0a11, code lost:
    
        if (r4 != 2) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x0a27, code lost:
    
        r14.rightMargin = (r13 / r9) + org.telegram.messenger.AndroidUtilities.dp(10.0f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x0a19, code lost:
    
        if (r4 == 1) goto L299;
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x0374, code lost:
    
        if ("d".equals(((org.telegram.ui.WallpapersListActivity.ColorWallpaper) r2).slug) == false) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x037a, code lost:
    
        if ((r38.currentWallpaper instanceof org.telegram.tgnet.TLRPC.TL_wallPaper) != false) goto L108;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:102:0x111a  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x1144  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x1156  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0fb7  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x067b  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x086d  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x08a0  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x098a  */
    /* JADX WARN: Removed duplicated region for block: B:218:0x0a0e  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0a48  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0a5a  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x0a17  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x0a6a  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:293:0x0b97  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x0f48  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x0f54  */
    /* JADX WARN: Removed duplicated region for block: B:305:0x0ba3  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x011f  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x0982  */
    /* JADX WARN: Removed duplicated region for block: B:378:0x0889  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:381:0x0639  */
    /* JADX WARN: Removed duplicated region for block: B:382:0x060b  */
    /* JADX WARN: Removed duplicated region for block: B:385:0x05d9  */
    /* JADX WARN: Removed duplicated region for block: B:390:0x0320  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0177  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x01c2  */
    /* JADX WARN: Removed duplicated region for block: B:447:0x0257  */
    /* JADX WARN: Removed duplicated region for block: B:448:0x0250  */
    /* JADX WARN: Removed duplicated region for block: B:449:0x0245  */
    /* JADX WARN: Removed duplicated region for block: B:450:0x0239  */
    /* JADX WARN: Removed duplicated region for block: B:451:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:452:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:453:0x0121  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x022b  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0236  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0241  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x024d  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0254  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x028a  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x02a7 A[LOOP:0: B:62:0x02a4->B:64:0x02a7, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x02ea  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0303  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x05ac  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0609  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0613  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0677 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0fb2  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0ff3  */
    /* JADX WARN: Type inference failed for: r1v103 */
    /* JADX WARN: Type inference failed for: r1v104, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r1v110 */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v3, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r8v4 */
    /* JADX WARN: Type inference failed for: r8v8 */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        boolean z;
        String str;
        int i;
        int i2;
        String str2;
        ?? r8;
        float f;
        int i3;
        boolean z2;
        int i4;
        int dp;
        int dp2;
        int i5;
        int i6;
        Object obj;
        int i7;
        int i8;
        int i9;
        int i10;
        final int i11;
        final int i12;
        int i13;
        int i14;
        ?? r1;
        FrameLayout frameLayout;
        View view;
        float f2;
        float f3;
        int i15;
        float f4;
        int i16;
        float f5;
        float f6;
        boolean z3;
        Theme.ThemeAccent themeAccent;
        int i17;
        WallpaperCheckBoxView wallpaperCheckBoxView;
        FrameLayout frameLayout2;
        BlurButton blurButton;
        float f7;
        float f8;
        int i18;
        float f9;
        int i19;
        float f10;
        float f11;
        int i20;
        INavigationLayout iNavigationLayout;
        Theme.ThemeAccent themeAccent2;
        Property property;
        this.msgOutDrawable.setResourceProvider(getResourceProvider());
        this.msgOutDrawableSelected.setResourceProvider(getResourceProvider());
        this.msgOutMediaDrawable.setResourceProvider(getResourceProvider());
        this.msgOutMediaDrawableSelected.setResourceProvider(getResourceProvider());
        this.hasOwnBackground = true;
        DayNightSwitchDelegate dayNightSwitchDelegate = this.onSwitchDayNightDelegate;
        boolean z4 = (dayNightSwitchDelegate == null || this.dialogId == 0) ? false : true;
        this.shouldShowDayNightIcon = z4;
        if (z4) {
            Object obj2 = this.currentWallpaper;
            if (!(obj2 instanceof WallpapersListActivity.FileWallpaper)) {
                if (obj2 instanceof TLRPC.TL_wallPaper) {
                    TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) obj2;
                    if (tL_wallPaper.document != null) {
                    }
                }
            }
            z = true;
            this.shouldShowBrightnessControll = z;
            if (z) {
                this.progressToDarkTheme = dayNightSwitchDelegate.isDark() ? 1.0f : 0.0f;
            }
            if (AndroidUtilities.isTablet()) {
                this.actionBar.setOccupyStatusBar(false);
            }
            this.page1 = new FrameLayout(context);
            if (this.shouldShowBrightnessControll || SharedConfig.dayNightWallpaperSwitchHint >= 3) {
                str = "";
            } else {
                str = "";
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        ThemePreviewActivity.this.lambda$createView$2();
                    }
                }, 2000L);
            }
            this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.ThemePreviewActivity.5
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
            }).setSearchFieldHint(LocaleController.getString(R.string.Search));
            this.actionBar.setBackButtonDrawable(new MenuDrawable());
            this.actionBar.setAddToContainer(false);
            this.actionBar.setTitle(LocaleController.getString(R.string.ThemePreview));
            FrameLayout frameLayout3 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.6
                @Override // android.view.ViewGroup
                protected boolean drawChild(Canvas canvas, View view2, long j) {
                    boolean drawChild = super.drawChild(canvas, view2, j);
                    if (view2 == ((BaseFragment) ThemePreviewActivity.this).actionBar && ((BaseFragment) ThemePreviewActivity.this).parentLayout != null) {
                        ((BaseFragment) ThemePreviewActivity.this).parentLayout.drawHeaderShadow(canvas, ((BaseFragment) ThemePreviewActivity.this).actionBar.getVisibility() == 0 ? ((BaseFragment) ThemePreviewActivity.this).actionBar.getMeasuredHeight() : 0);
                    }
                    return drawChild;
                }

                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i21, int i22) {
                    int size = View.MeasureSpec.getSize(i21);
                    int size2 = View.MeasureSpec.getSize(i22);
                    setMeasuredDimension(size, size2);
                    measureChildWithMargins(((BaseFragment) ThemePreviewActivity.this).actionBar, i21, 0, i22, 0);
                    int measuredHeight = ((BaseFragment) ThemePreviewActivity.this).actionBar.getMeasuredHeight();
                    if (((BaseFragment) ThemePreviewActivity.this).actionBar.getVisibility() == 0) {
                        size2 -= measuredHeight;
                    }
                    ((FrameLayout.LayoutParams) ThemePreviewActivity.this.listView.getLayoutParams()).topMargin = measuredHeight;
                    ThemePreviewActivity.this.listView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                    measureChildWithMargins(ThemePreviewActivity.this.floatingButton, i21, 0, i22, 0);
                }
            };
            this.page1 = frameLayout3;
            frameLayout3.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
            this.page1.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
            RecyclerListView recyclerListView = new RecyclerListView(context);
            this.listView = recyclerListView;
            recyclerListView.setVerticalScrollBarEnabled(true);
            this.listView.setItemAnimator(null);
            this.listView.setLayoutAnimation(null);
            this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
            this.listView.setVerticalScrollbarPosition(!LocaleController.isRTL ? 1 : 2);
            this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(this.screenType == 0 ? 12.0f : 0.0f));
            this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda18
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view2, int i21) {
                    ThemePreviewActivity.lambda$createView$3(view2, i21);
                }
            });
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
                int[] iArr = {android.R.attr.state_pressed};
                ImageView imageView2 = this.floatingButton;
                property = View.TRANSLATION_Z;
                stateListAnimator.addState(iArr, ObjectAnimator.ofFloat(imageView2, (Property<ImageView, Float>) property, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButton, (Property<ImageView, Float>) property, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                this.floatingButton.setStateListAnimator(stateListAnimator);
                this.floatingButton.setOutlineProvider(new ViewOutlineProvider() { // from class: org.telegram.ui.ThemePreviewActivity.7
                    @Override // android.view.ViewOutlineProvider
                    public void getOutline(View view2, Outline outline) {
                        outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    }
                });
            }
            FrameLayout frameLayout4 = this.page1;
            ImageView imageView3 = this.floatingButton;
            int i21 = i < 21 ? 56 : 60;
            float f12 = i < 21 ? 56.0f : 60.0f;
            boolean z5 = LocaleController.isRTL;
            frameLayout4.addView(imageView3, LayoutHelper.createFrame(i21, f12, (!z5 ? 3 : 5) | 80, !z5 ? 14.0f : 0.0f, 0.0f, !z5 ? 0.0f : 14.0f, 14.0f));
            DialogsAdapter dialogsAdapter = new DialogsAdapter(context);
            this.dialogsAdapter = dialogsAdapter;
            this.listView.setAdapter(dialogsAdapter);
            this.page2 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.8
                private boolean ignoreLayout;

                @Override // android.view.ViewGroup
                protected boolean drawChild(Canvas canvas, View view2, long j) {
                    boolean drawChild = super.drawChild(canvas, view2, j);
                    if (view2 == ThemePreviewActivity.this.actionBar2 && ((BaseFragment) ThemePreviewActivity.this).parentLayout != null) {
                        ((BaseFragment) ThemePreviewActivity.this).parentLayout.drawHeaderShadow(canvas, ThemePreviewActivity.this.actionBar2.getVisibility() == 0 ? (int) (ThemePreviewActivity.this.actionBar2.getMeasuredHeight() + ThemePreviewActivity.this.actionBar2.getTranslationY()) : 0);
                    }
                    return drawChild;
                }

                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i22, int i23) {
                    float f13;
                    TextView textView;
                    float f14;
                    int size = View.MeasureSpec.getSize(i22);
                    int size2 = View.MeasureSpec.getSize(i23);
                    setMeasuredDimension(size, size2);
                    if (ThemePreviewActivity.this.dropDownContainer != null) {
                        this.ignoreLayout = true;
                        if (!AndroidUtilities.isTablet()) {
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ThemePreviewActivity.this.dropDownContainer.getLayoutParams();
                            layoutParams.topMargin = Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0;
                            ThemePreviewActivity.this.dropDownContainer.setLayoutParams(layoutParams);
                        }
                        if (AndroidUtilities.isTablet() || ApplicationLoader.applicationContext.getResources().getConfiguration().orientation != 2) {
                            textView = ThemePreviewActivity.this.dropDown;
                            f14 = 20.0f;
                        } else {
                            textView = ThemePreviewActivity.this.dropDown;
                            f14 = 18.0f;
                        }
                        textView.setTextSize(1, f14);
                        this.ignoreLayout = false;
                    }
                    measureChildWithMargins(ThemePreviewActivity.this.actionBar2, i22, 0, i23, 0);
                    int measuredHeight = ThemePreviewActivity.this.actionBar2.getMeasuredHeight();
                    if (ThemePreviewActivity.this.actionBar2.getVisibility() == 0) {
                        size2 -= measuredHeight;
                    }
                    FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.listView2.getLayoutParams();
                    layoutParams2.topMargin = measuredHeight;
                    if (ThemePreviewActivity.this.screenType == 2) {
                        RecyclerListView recyclerListView2 = ThemePreviewActivity.this.listView2;
                        int dp3 = AndroidUtilities.dp(4.0f);
                        ThemePreviewActivity themePreviewActivity = ThemePreviewActivity.this;
                        recyclerListView2.setPadding(0, dp3, 0, (AndroidUtilities.dp(((themePreviewActivity.self || themePreviewActivity.dialogId <= 0) ? 0 : 58) + 72) - 12) + (ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0));
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
                        layoutParams3.height = AndroidUtilities.dp(72 + ((themePreviewActivity2.self || themePreviewActivity2.dialogId <= 0) ? 0 : 58)) + (ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0);
                        measureChildWithMargins(ThemePreviewActivity.this.bottomOverlayChat, i22, 0, i23, 0);
                    }
                    if (ThemePreviewActivity.this.sheetDrawable != null) {
                        ThemePreviewActivity.this.sheetDrawable.getPadding(AndroidUtilities.rectTmp2);
                    }
                    int i24 = 0;
                    while (i24 < ThemePreviewActivity.this.patternLayout.length) {
                        if (ThemePreviewActivity.this.patternLayout[i24] != null) {
                            FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.patternLayout[i24].getLayoutParams();
                            if (i24 == 0) {
                                f13 = ThemePreviewActivity.this.screenType == 2 ? 321 : NotificationCenter.chatAvailableReactionsUpdated;
                            } else {
                                f13 = 316.0f;
                            }
                            layoutParams4.height = AndroidUtilities.dp(f13);
                            if (ThemePreviewActivity.this.insideBottomSheet()) {
                                layoutParams4.height += AndroidUtilities.navigationBarHeight;
                            }
                            if (i24 == 0) {
                                layoutParams4.height += AndroidUtilities.dp(12.0f) + AndroidUtilities.rectTmp2.top;
                            }
                            ThemePreviewActivity.this.patternLayout[i24].setPadding(0, i24 == 0 ? AndroidUtilities.dp(12.0f) + AndroidUtilities.rectTmp2.top : 0, 0, ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0);
                            measureChildWithMargins(ThemePreviewActivity.this.patternLayout[i24], i22, 0, i23, 0);
                        }
                        i24++;
                    }
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
                this.backgroundImage.getImageReceiver().setDelegate(new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda21
                    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                    public final void didSetImage(ImageReceiver imageReceiver, boolean z6, boolean z7, boolean z8) {
                        ThemePreviewActivity.this.lambda$createView$4(imageReceiver, z6, z7, z8);
                    }

                    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                    public /* synthetic */ void didSetImageBitmap(int i22, String str3, Drawable drawable) {
                        ImageReceiver.ImageReceiverDelegate.-CC.$default$didSetImageBitmap(this, i22, str3, drawable);
                    }

                    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                    public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
                        ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver);
                    }
                });
            }
            if (this.messagesAdapter.showSecretMessages) {
                int i22 = this.screenType;
                if (i22 != 2) {
                    str2 = str;
                    if (i22 == 1) {
                        ActionBarMenu createMenu = this.actionBar2.createMenu();
                        this.saveItem = createMenu.addItem(4, LocaleController.getString(R.string.Save).toUpperCase());
                        z2 = false;
                        f = 0.0f;
                        ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, createMenu, 0, 0) { // from class: org.telegram.ui.ThemePreviewActivity.10
                            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem, android.view.View
                            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                                accessibilityNodeInfo.setText(ThemePreviewActivity.this.dropDown.getText());
                            }
                        };
                        this.dropDownContainer = actionBarMenuItem;
                        actionBarMenuItem.setSubMenuOpenSide(1);
                        this.dropDownContainer.addSubItem(2, LocaleController.getString(R.string.ColorPickerBackground));
                        ActionBarMenuItem actionBarMenuItem2 = this.dropDownContainer;
                        int i23 = R.string.ColorPickerMainColor;
                        actionBarMenuItem2.addSubItem(1, LocaleController.getString(i23));
                        this.dropDownContainer.addSubItem(3, LocaleController.getString(R.string.ColorPickerMyMessages));
                        this.dropDownContainer.setAllowCloseAnimation(false);
                        this.dropDownContainer.setForceSmoothKeyboard(true);
                        this.actionBar2.addView(this.dropDownContainer, LayoutHelper.createFrame(-2, -1.0f, 51, AndroidUtilities.isTablet() ? 64.0f : 56.0f, 0.0f, 40.0f, 0.0f));
                        this.dropDownContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda22
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                ThemePreviewActivity.this.lambda$createView$5(view2);
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
                        int i24 = Theme.key_actionBarDefaultTitle;
                        textView2.setTextColor(getThemedColor(i24));
                        this.dropDown.setTypeface(AndroidUtilities.bold());
                        this.dropDown.setText(LocaleController.getString(i23));
                        Drawable mutate2 = context.getResources().getDrawable(R.drawable.ic_arrow_drop_down).mutate();
                        mutate2.setColorFilter(new PorterDuffColorFilter(getThemedColor(i24), PorterDuff.Mode.MULTIPLY));
                        this.dropDown.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, mutate2, (Drawable) null);
                        this.dropDown.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
                        this.dropDown.setPadding(0, 0, AndroidUtilities.dp(10.0f), 0);
                        this.dropDownContainer.addView(this.dropDown, LayoutHelper.createFrame(-2, -2.0f, 16, 16.0f, 0.0f, 0.0f, 1.0f));
                    } else {
                        r8 = 0;
                        z2 = false;
                        f = 0.0f;
                        Theme.ThemeInfo themeInfo = this.applyingTheme;
                        TLRPC.TL_theme tL_theme = themeInfo.info;
                        String name = tL_theme != null ? tL_theme.title : themeInfo.getName();
                        int lastIndexOf = name.lastIndexOf(".attheme");
                        if (lastIndexOf >= 0) {
                            name = name.substring(0, lastIndexOf);
                        }
                        this.actionBar2.setTitle(name);
                        TLRPC.TL_theme tL_theme2 = this.applyingTheme.info;
                        if (tL_theme2 == null || (i3 = tL_theme2.installs_count) <= 0) {
                            this.actionBar2.setSubtitle(LocaleController.formatDateOnline((System.currentTimeMillis() / 1000) - 3600, null));
                            this.listView2 = new RecyclerListView(context) { // from class: org.telegram.ui.ThemePreviewActivity.11
                                float lastX;
                                float lastY;
                                boolean scrollingBackground;
                                float startX;
                                float startY;

                                private void checkMotionEvent(MotionEvent motionEvent) {
                                    if (motionEvent.getAction() == 1) {
                                        if (!ThemePreviewActivity.this.wasScroll && (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) && ThemePreviewActivity.this.patternLayout[0].getVisibility() == 0) {
                                            ThemePreviewActivity.this.showPatternsView(0, false, true);
                                        }
                                        ThemePreviewActivity.this.wasScroll = false;
                                    }
                                }

                                @Override // org.telegram.ui.Components.RecyclerListView
                                protected boolean allowSelectChildAtPosition(View view2) {
                                    RecyclerView.ViewHolder findContainingViewHolder = ThemePreviewActivity.this.listView2.findContainingViewHolder(view2);
                                    if (findContainingViewHolder == null || findContainingViewHolder.getItemViewType() != 2) {
                                        return super.allowSelectChildAtPosition(view2);
                                    }
                                    return false;
                                }

                                @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                                public boolean drawChild(Canvas canvas, View view2, long j) {
                                    RecyclerView.ViewHolder childViewHolder;
                                    boolean drawChild = super.drawChild(canvas, view2, j);
                                    if (view2 instanceof ChatMessageCell) {
                                        ChatMessageCell chatMessageCell = (ChatMessageCell) view2;
                                        chatMessageCell.getMessageObject();
                                        ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                                        if (avatarImage != null) {
                                            int top = view2.getTop();
                                            if (chatMessageCell.isPinnedBottom() && (childViewHolder = ThemePreviewActivity.this.listView2.getChildViewHolder(view2)) != null) {
                                                if (ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder.getAdapterPosition() - 1) != null) {
                                                    avatarImage.setImageY(-AndroidUtilities.dp(1000.0f));
                                                    avatarImage.draw(canvas);
                                                    return drawChild;
                                                }
                                            }
                                            float translationX = chatMessageCell.getTranslationX();
                                            int top2 = view2.getTop() + chatMessageCell.getLayoutHeight();
                                            int measuredHeight = ThemePreviewActivity.this.listView2.getMeasuredHeight() - ThemePreviewActivity.this.listView2.getPaddingBottom();
                                            if (top2 > measuredHeight) {
                                                top2 = measuredHeight;
                                            }
                                            if (chatMessageCell.isPinnedTop() && (r9 = ThemePreviewActivity.this.listView2.getChildViewHolder(view2)) != null) {
                                                int i25 = 0;
                                                while (i25 < 20) {
                                                    i25++;
                                                    RecyclerView.ViewHolder childViewHolder2 = ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder2.getAdapterPosition() + 1);
                                                    if (childViewHolder2 == null) {
                                                        break;
                                                    }
                                                    top = childViewHolder2.itemView.getTop();
                                                    if (top2 - AndroidUtilities.dp(48.0f) < childViewHolder2.itemView.getBottom()) {
                                                        translationX = Math.min(childViewHolder2.itemView.getTranslationX(), translationX);
                                                    }
                                                    View view3 = childViewHolder2.itemView;
                                                    if (!(view3 instanceof ChatMessageCell) || !((ChatMessageCell) view3).isPinnedTop()) {
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

                                @Override // org.telegram.ui.Components.RecyclerListView
                                protected void onChildPressed(View view2, float f13, float f14, boolean z6) {
                                    if (z6 && (view2 instanceof ChatMessageCell) && !((ChatMessageCell) view2).isInsideBackground(f13, f14)) {
                                        return;
                                    }
                                    super.onChildPressed(view2, f13, f14, z6);
                                }

                                @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
                                protected void onLayout(boolean z6, int i25, int i26, int i27, int i28) {
                                    super.onLayout(z6, i25, i26, i27, i28);
                                    ThemePreviewActivity.this.invalidateBlur();
                                }

                                /* JADX WARN: Code restructure failed: missing block: B:17:0x004a, code lost:
                                
                                    if (getParent() != null) goto L17;
                                 */
                                /* JADX WARN: Code restructure failed: missing block: B:6:0x0027, code lost:
                                
                                    if (getParent() != null) goto L17;
                                 */
                                /* JADX WARN: Code restructure failed: missing block: B:7:0x0053, code lost:
                                
                                    r4.scrollingBackground = true;
                                 */
                                /* JADX WARN: Code restructure failed: missing block: B:9:0x004c, code lost:
                                
                                    getParent().requestDisallowInterceptTouchEvent(true);
                                 */
                                @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
                                /*
                                    Code decompiled incorrectly, please refer to instructions dump.
                                */
                                public boolean onTouchEvent(MotionEvent motionEvent) {
                                    checkMotionEvent(motionEvent);
                                    if (ThemePreviewActivity.this.hasScrollingBackground) {
                                        if (motionEvent.getAction() == 0) {
                                            float x = motionEvent.getX();
                                            this.startX = x;
                                            this.lastX = x;
                                            float y = motionEvent.getY();
                                            this.startY = y;
                                            this.lastY = y;
                                        } else {
                                            if (motionEvent.getAction() == 2) {
                                                if (!this.scrollingBackground && Math.abs(this.startX - motionEvent.getX()) > AndroidUtilities.touchSlop) {
                                                }
                                            } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                                                this.scrollingBackground = false;
                                                if (getParent() != null) {
                                                    getParent().requestDisallowInterceptTouchEvent(false);
                                                }
                                            }
                                            ThemePreviewActivity.this.gestureDetector2.onTouchEvent(motionEvent);
                                        }
                                    }
                                    return this.scrollingBackground || super.onTouchEvent(motionEvent);
                                }

                                @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
                                public void setTranslationY(float f13) {
                                    super.setTranslationY(f13);
                                    if (ThemePreviewActivity.this.backgroundCheckBoxView != null) {
                                        for (int i25 = 0; i25 < ThemePreviewActivity.this.backgroundCheckBoxView.length; i25++) {
                                            ThemePreviewActivity.this.backgroundCheckBoxView[i25].invalidate();
                                        }
                                    }
                                    if (ThemePreviewActivity.this.messagesCheckBoxView != null) {
                                        for (int i26 = 0; i26 < ThemePreviewActivity.this.messagesCheckBoxView.length; i26++) {
                                            ThemePreviewActivity.this.messagesCheckBoxView[i26].invalidate();
                                        }
                                    }
                                    if (ThemePreviewActivity.this.backgroundPlayAnimationView != null) {
                                        ThemePreviewActivity.this.backgroundPlayAnimationView.invalidate();
                                    }
                                    if (ThemePreviewActivity.this.messagesPlayAnimationView != null) {
                                        ThemePreviewActivity.this.messagesPlayAnimationView.invalidate();
                                    }
                                }
                            };
                            DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator() { // from class: org.telegram.ui.ThemePreviewActivity.12
                                @Override // androidx.recyclerview.widget.DefaultItemAnimator
                                protected void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                                    ThemePreviewActivity.this.listView2.invalidateViews();
                                }
                            };
                            defaultItemAnimator.setDelayAnimations(r8);
                            this.listView2.setItemAnimator(defaultItemAnimator);
                            this.listView2.setVerticalScrollBarEnabled(true);
                            this.listView2.setOverScrollMode(2);
                            i4 = this.screenType;
                            if (i4 == 2) {
                            }
                            this.listView2.setClipToPadding(r8);
                            this.listView2.setLayoutManager(new LinearLayoutManager(context, 1, true));
                            this.listView2.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
                            if (this.screenType == 1) {
                            }
                            this.listView2.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ThemePreviewActivity.13
                                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                                public void onScrollStateChanged(RecyclerView recyclerView, int i25) {
                                    if (i25 == 0) {
                                        ThemePreviewActivity.this.wasScroll = false;
                                    }
                                }

                                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                                public void onScrolled(RecyclerView recyclerView, int i25, int i26) {
                                    ThemePreviewActivity.this.listView2.invalidateViews();
                                    ThemePreviewActivity.this.wasScroll = true;
                                }
                            });
                            this.page2.addView(this.actionBar2, LayoutHelper.createFrame(i5, -2.0f));
                            WallpaperParallaxEffect wallpaperParallaxEffect = new WallpaperParallaxEffect(context);
                            this.parallaxEffect = wallpaperParallaxEffect;
                            wallpaperParallaxEffect.setCallback(new WallpaperParallaxEffect.Callback() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda8
                                @Override // org.telegram.ui.Components.WallpaperParallaxEffect.Callback
                                public final void onOffsetsChanged(int i25, int i26, float f13) {
                                    ThemePreviewActivity.this.lambda$createView$7(i25, i26, f13);
                                }
                            });
                            i6 = this.screenType;
                            if (i6 != 1) {
                            }
                            if (i6 == 2) {
                            }
                            final Rect rect = new Rect();
                            Drawable mutate3 = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
                            this.sheetDrawable = mutate3;
                            mutate3.getPadding(rect);
                            this.sheetDrawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundWhite), PorterDuff.Mode.MULTIPLY));
                            TextPaint textPaint = new TextPaint(1);
                            textPaint.setTextSize(AndroidUtilities.dp(14.0f));
                            textPaint.setTypeface(AndroidUtilities.bold());
                            obj = this.currentWallpaper;
                            if (this.screenType != 1) {
                            }
                            if (obj instanceof WallpapersListActivity.ColorWallpaper) {
                            }
                            i7 = 3;
                            String[] strArr = new String[i7];
                            int[] iArr2 = new int[i7];
                            this.backgroundCheckBoxView = new WallpaperCheckBoxView[i7];
                            if (i7 == 0) {
                            }
                            i10 = i8;
                            i11 = 0;
                            while (i11 < i10) {
                            }
                            if (this.screenType == 1) {
                            }
                            if (this.screenType != 1) {
                            }
                            this.isBlurred = r8;
                            i12 = 0;
                            while (i12 < i13) {
                            }
                            updateButtonState(r8, r8);
                            if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
                            }
                            if (this.screenType != 1) {
                            }
                            this.listView2.setAdapter(this.messagesAdapter);
                            FrameLayout frameLayout5 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.28
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
                                        int[] iArr3 = this.loc;
                                        iArr3[1] = iArr3[1] - AndroidUtilities.statusBarHeight;
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
                            this.frameLayout = frameLayout5;
                            frameLayout5.setWillNotDraw(r8);
                            FrameLayout frameLayout6 = this.frameLayout;
                            this.fragmentView = frameLayout6;
                            ViewTreeObserver viewTreeObserver = frameLayout6.getViewTreeObserver();
                            ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda17
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
                                public void onPageScrollStateChanged(int i25) {
                                }

                                @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                                public void onPageScrolled(int i25, float f13, int i26) {
                                }

                                @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                                public void onPageSelected(int i25) {
                                    ThemePreviewActivity.this.dotsContainer.invalidate();
                                }
                            });
                            this.viewPager.setAdapter(new PagerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.30
                                @Override // androidx.viewpager.widget.PagerAdapter
                                public void destroyItem(ViewGroup viewGroup, int i25, Object obj3) {
                                    viewGroup.removeView((View) obj3);
                                }

                                @Override // androidx.viewpager.widget.PagerAdapter
                                public int getCount() {
                                    return ThemePreviewActivity.this.screenType != 0 ? 1 : 2;
                                }

                                @Override // androidx.viewpager.widget.PagerAdapter
                                public int getItemPosition(Object obj3) {
                                    return -1;
                                }

                                @Override // androidx.viewpager.widget.PagerAdapter
                                public Object instantiateItem(ViewGroup viewGroup, int i25) {
                                    FrameLayout frameLayout7 = i25 == 0 ? ThemePreviewActivity.this.page2 : ThemePreviewActivity.this.page1;
                                    viewGroup.addView(frameLayout7);
                                    return frameLayout7;
                                }

                                @Override // androidx.viewpager.widget.PagerAdapter
                                public boolean isViewFromObject(View view2, Object obj3) {
                                    return obj3 == view2;
                                }
                            });
                            AndroidUtilities.setViewPagerEdgeEffectColor(this.viewPager, getThemedColor(Theme.key_actionBarDefault));
                            this.frameLayout.addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.screenType == 0 ? 48.0f : 0.0f));
                            UndoView undoView = new UndoView(context, this);
                            this.undoView = undoView;
                            undoView.setAdditionalTranslationY(AndroidUtilities.dp(51.0f));
                            this.frameLayout.addView(this.undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
                            if (this.screenType == 0) {
                            }
                            if (this.screenType == 1) {
                                selectColorType(2);
                            }
                            this.themeDescriptions = getThemeDescriptionsInternal();
                            setCurrentImage(true);
                            updatePlayAnimationView(r8);
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
                        this.actionBar2.setSubtitle(LocaleController.formatPluralString("ThemeInstallCount", i3, new Object[0]));
                    }
                    r8 = z2;
                    this.listView2 = new RecyclerListView(context) { // from class: org.telegram.ui.ThemePreviewActivity.11
                        float lastX;
                        float lastY;
                        boolean scrollingBackground;
                        float startX;
                        float startY;

                        private void checkMotionEvent(MotionEvent motionEvent) {
                            if (motionEvent.getAction() == 1) {
                                if (!ThemePreviewActivity.this.wasScroll && (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) && ThemePreviewActivity.this.patternLayout[0].getVisibility() == 0) {
                                    ThemePreviewActivity.this.showPatternsView(0, false, true);
                                }
                                ThemePreviewActivity.this.wasScroll = false;
                            }
                        }

                        @Override // org.telegram.ui.Components.RecyclerListView
                        protected boolean allowSelectChildAtPosition(View view2) {
                            RecyclerView.ViewHolder findContainingViewHolder = ThemePreviewActivity.this.listView2.findContainingViewHolder(view2);
                            if (findContainingViewHolder == null || findContainingViewHolder.getItemViewType() != 2) {
                                return super.allowSelectChildAtPosition(view2);
                            }
                            return false;
                        }

                        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                        public boolean drawChild(Canvas canvas, View view2, long j) {
                            RecyclerView.ViewHolder childViewHolder;
                            boolean drawChild = super.drawChild(canvas, view2, j);
                            if (view2 instanceof ChatMessageCell) {
                                ChatMessageCell chatMessageCell = (ChatMessageCell) view2;
                                chatMessageCell.getMessageObject();
                                ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                                if (avatarImage != null) {
                                    int top = view2.getTop();
                                    if (chatMessageCell.isPinnedBottom() && (childViewHolder = ThemePreviewActivity.this.listView2.getChildViewHolder(view2)) != null) {
                                        if (ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder.getAdapterPosition() - 1) != null) {
                                            avatarImage.setImageY(-AndroidUtilities.dp(1000.0f));
                                            avatarImage.draw(canvas);
                                            return drawChild;
                                        }
                                    }
                                    float translationX = chatMessageCell.getTranslationX();
                                    int top2 = view2.getTop() + chatMessageCell.getLayoutHeight();
                                    int measuredHeight = ThemePreviewActivity.this.listView2.getMeasuredHeight() - ThemePreviewActivity.this.listView2.getPaddingBottom();
                                    if (top2 > measuredHeight) {
                                        top2 = measuredHeight;
                                    }
                                    if (chatMessageCell.isPinnedTop() && (childViewHolder2 = ThemePreviewActivity.this.listView2.getChildViewHolder(view2)) != null) {
                                        int i25 = 0;
                                        while (i25 < 20) {
                                            i25++;
                                            RecyclerView.ViewHolder childViewHolder2 = ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder2.getAdapterPosition() + 1);
                                            if (childViewHolder2 == null) {
                                                break;
                                            }
                                            top = childViewHolder2.itemView.getTop();
                                            if (top2 - AndroidUtilities.dp(48.0f) < childViewHolder2.itemView.getBottom()) {
                                                translationX = Math.min(childViewHolder2.itemView.getTranslationX(), translationX);
                                            }
                                            View view3 = childViewHolder2.itemView;
                                            if (!(view3 instanceof ChatMessageCell) || !((ChatMessageCell) view3).isPinnedTop()) {
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

                        @Override // org.telegram.ui.Components.RecyclerListView
                        protected void onChildPressed(View view2, float f13, float f14, boolean z6) {
                            if (z6 && (view2 instanceof ChatMessageCell) && !((ChatMessageCell) view2).isInsideBackground(f13, f14)) {
                                return;
                            }
                            super.onChildPressed(view2, f13, f14, z6);
                        }

                        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
                        protected void onLayout(boolean z6, int i25, int i26, int i27, int i28) {
                            super.onLayout(z6, i25, i26, i27, i28);
                            ThemePreviewActivity.this.invalidateBlur();
                        }

                        /* JADX WARN: Code restructure failed: missing block: B:17:0x004a, code lost:
                        
                            if (getParent() != null) goto L17;
                         */
                        /* JADX WARN: Code restructure failed: missing block: B:6:0x0027, code lost:
                        
                            if (getParent() != null) goto L17;
                         */
                        /* JADX WARN: Code restructure failed: missing block: B:7:0x0053, code lost:
                        
                            r4.scrollingBackground = true;
                         */
                        /* JADX WARN: Code restructure failed: missing block: B:9:0x004c, code lost:
                        
                            getParent().requestDisallowInterceptTouchEvent(true);
                         */
                        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                        */
                        public boolean onTouchEvent(MotionEvent motionEvent) {
                            checkMotionEvent(motionEvent);
                            if (ThemePreviewActivity.this.hasScrollingBackground) {
                                if (motionEvent.getAction() == 0) {
                                    float x = motionEvent.getX();
                                    this.startX = x;
                                    this.lastX = x;
                                    float y = motionEvent.getY();
                                    this.startY = y;
                                    this.lastY = y;
                                } else {
                                    if (motionEvent.getAction() == 2) {
                                        if (!this.scrollingBackground && Math.abs(this.startX - motionEvent.getX()) > AndroidUtilities.touchSlop) {
                                        }
                                    } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                                        this.scrollingBackground = false;
                                        if (getParent() != null) {
                                            getParent().requestDisallowInterceptTouchEvent(false);
                                        }
                                    }
                                    ThemePreviewActivity.this.gestureDetector2.onTouchEvent(motionEvent);
                                }
                            }
                            return this.scrollingBackground || super.onTouchEvent(motionEvent);
                        }

                        @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
                        public void setTranslationY(float f13) {
                            super.setTranslationY(f13);
                            if (ThemePreviewActivity.this.backgroundCheckBoxView != null) {
                                for (int i25 = 0; i25 < ThemePreviewActivity.this.backgroundCheckBoxView.length; i25++) {
                                    ThemePreviewActivity.this.backgroundCheckBoxView[i25].invalidate();
                                }
                            }
                            if (ThemePreviewActivity.this.messagesCheckBoxView != null) {
                                for (int i26 = 0; i26 < ThemePreviewActivity.this.messagesCheckBoxView.length; i26++) {
                                    ThemePreviewActivity.this.messagesCheckBoxView[i26].invalidate();
                                }
                            }
                            if (ThemePreviewActivity.this.backgroundPlayAnimationView != null) {
                                ThemePreviewActivity.this.backgroundPlayAnimationView.invalidate();
                            }
                            if (ThemePreviewActivity.this.messagesPlayAnimationView != null) {
                                ThemePreviewActivity.this.messagesPlayAnimationView.invalidate();
                            }
                        }
                    };
                    DefaultItemAnimator defaultItemAnimator2 = new DefaultItemAnimator() { // from class: org.telegram.ui.ThemePreviewActivity.12
                        @Override // androidx.recyclerview.widget.DefaultItemAnimator
                        protected void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                            ThemePreviewActivity.this.listView2.invalidateViews();
                        }
                    };
                    defaultItemAnimator2.setDelayAnimations(r8);
                    this.listView2.setItemAnimator(defaultItemAnimator2);
                    this.listView2.setVerticalScrollBarEnabled(true);
                    this.listView2.setOverScrollMode(2);
                    i4 = this.screenType;
                    if (i4 == 2) {
                    }
                    this.listView2.setClipToPadding(r8);
                    this.listView2.setLayoutManager(new LinearLayoutManager(context, 1, true));
                    this.listView2.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
                    if (this.screenType == 1) {
                    }
                    this.listView2.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ThemePreviewActivity.13
                        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                        public void onScrollStateChanged(RecyclerView recyclerView, int i25) {
                            if (i25 == 0) {
                                ThemePreviewActivity.this.wasScroll = false;
                            }
                        }

                        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                        public void onScrolled(RecyclerView recyclerView, int i25, int i26) {
                            ThemePreviewActivity.this.listView2.invalidateViews();
                            ThemePreviewActivity.this.wasScroll = true;
                        }
                    });
                    this.page2.addView(this.actionBar2, LayoutHelper.createFrame(i5, -2.0f));
                    WallpaperParallaxEffect wallpaperParallaxEffect2 = new WallpaperParallaxEffect(context);
                    this.parallaxEffect = wallpaperParallaxEffect2;
                    wallpaperParallaxEffect2.setCallback(new WallpaperParallaxEffect.Callback() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda8
                        @Override // org.telegram.ui.Components.WallpaperParallaxEffect.Callback
                        public final void onOffsetsChanged(int i25, int i26, float f13) {
                            ThemePreviewActivity.this.lambda$createView$7(i25, i26, f13);
                        }
                    });
                    i6 = this.screenType;
                    if (i6 != 1) {
                    }
                    if (i6 == 2) {
                    }
                    final Rect rect2 = new Rect();
                    Drawable mutate32 = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
                    this.sheetDrawable = mutate32;
                    mutate32.getPadding(rect2);
                    this.sheetDrawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundWhite), PorterDuff.Mode.MULTIPLY));
                    TextPaint textPaint2 = new TextPaint(1);
                    textPaint2.setTextSize(AndroidUtilities.dp(14.0f));
                    textPaint2.setTypeface(AndroidUtilities.bold());
                    obj = this.currentWallpaper;
                    if (this.screenType != 1) {
                    }
                    if (obj instanceof WallpapersListActivity.ColorWallpaper) {
                    }
                    i7 = 3;
                    String[] strArr2 = new String[i7];
                    int[] iArr22 = new int[i7];
                    this.backgroundCheckBoxView = new WallpaperCheckBoxView[i7];
                    if (i7 == 0) {
                    }
                    i10 = i8;
                    i11 = 0;
                    while (i11 < i10) {
                    }
                    if (this.screenType == 1) {
                    }
                    if (this.screenType != 1) {
                    }
                    this.isBlurred = r8;
                    i12 = 0;
                    while (i12 < i13) {
                    }
                    updateButtonState(r8, r8);
                    if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
                    }
                    if (this.screenType != 1) {
                    }
                    this.listView2.setAdapter(this.messagesAdapter);
                    FrameLayout frameLayout52 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.28
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
                                int[] iArr3 = this.loc;
                                iArr3[1] = iArr3[1] - AndroidUtilities.statusBarHeight;
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
                    this.frameLayout = frameLayout52;
                    frameLayout52.setWillNotDraw(r8);
                    FrameLayout frameLayout62 = this.frameLayout;
                    this.fragmentView = frameLayout62;
                    ViewTreeObserver viewTreeObserver2 = frameLayout62.getViewTreeObserver();
                    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener2 = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda17
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
                        public void onPageScrollStateChanged(int i25) {
                        }

                        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                        public void onPageScrolled(int i25, float f13, int i26) {
                        }

                        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                        public void onPageSelected(int i25) {
                            ThemePreviewActivity.this.dotsContainer.invalidate();
                        }
                    });
                    this.viewPager.setAdapter(new PagerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.30
                        @Override // androidx.viewpager.widget.PagerAdapter
                        public void destroyItem(ViewGroup viewGroup, int i25, Object obj3) {
                            viewGroup.removeView((View) obj3);
                        }

                        @Override // androidx.viewpager.widget.PagerAdapter
                        public int getCount() {
                            return ThemePreviewActivity.this.screenType != 0 ? 1 : 2;
                        }

                        @Override // androidx.viewpager.widget.PagerAdapter
                        public int getItemPosition(Object obj3) {
                            return -1;
                        }

                        @Override // androidx.viewpager.widget.PagerAdapter
                        public Object instantiateItem(ViewGroup viewGroup, int i25) {
                            FrameLayout frameLayout7 = i25 == 0 ? ThemePreviewActivity.this.page2 : ThemePreviewActivity.this.page1;
                            viewGroup.addView(frameLayout7);
                            return frameLayout7;
                        }

                        @Override // androidx.viewpager.widget.PagerAdapter
                        public boolean isViewFromObject(View view2, Object obj3) {
                            return obj3 == view2;
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
                    }
                    this.themeDescriptions = getThemeDescriptionsInternal();
                    setCurrentImage(true);
                    updatePlayAnimationView(r8);
                    if (this.showColor) {
                    }
                    this.scroller = new Scroller(getContext());
                    iNavigationLayout = this.parentLayout;
                    if (iNavigationLayout != null) {
                    }
                    return this.fragmentView;
                }
                this.actionBar2.setTitle(LocaleController.getString(this.dialogId != 0 ? R.string.WallpaperPreview : R.string.BackgroundPreview));
                ActionBarMenu createMenu2 = this.actionBar2.createMenu();
                Object obj3 = this.currentWallpaper;
                if ((obj3 instanceof WallpapersListActivity.FileWallpaper) && ((WallpapersListActivity.FileWallpaper) obj3).originalPath != null) {
                    createMenu2.addItem(7, R.drawable.msg_header_draw);
                }
                if (this.dialogId == 0) {
                    if (!BuildVars.DEBUG_PRIVATE_VERSION || Theme.getActiveTheme().getAccent(false) == null) {
                        Object obj4 = this.currentWallpaper;
                        if (obj4 instanceof WallpapersListActivity.ColorWallpaper) {
                        }
                    }
                    createMenu2.addItem(5, R.drawable.msg_header_share);
                }
                if (this.dialogId != 0 && this.shouldShowDayNightIcon) {
                    int i25 = R.raw.sun;
                    StringBuilder sb = new StringBuilder();
                    str2 = str;
                    sb.append(str2);
                    sb.append(i25);
                    RLottieDrawable rLottieDrawable = new RLottieDrawable(i25, sb.toString(), AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
                    this.sunDrawable = rLottieDrawable;
                    this.dayNightItem = createMenu2.addItem(6, rLottieDrawable);
                    this.sunDrawable.setPlayInDirectionOfCustomEndFrame(true);
                    DayNightSwitchDelegate dayNightSwitchDelegate2 = this.onSwitchDayNightDelegate;
                    if (dayNightSwitchDelegate2 == null || dayNightSwitchDelegate2.isDark()) {
                        this.sunDrawable.setCurrentFrame(35);
                        this.sunDrawable.setCustomEndFrame(36);
                    } else {
                        this.sunDrawable.setCustomEndFrame(0);
                        this.sunDrawable.setCurrentFrame(0);
                    }
                    this.sunDrawable.beginApplyLayerColors();
                    int color = Theme.getColor(Theme.key_chats_menuName);
                    this.sunDrawable.setLayerColor("Sunny.**", color);
                    this.sunDrawable.setLayerColor("Path 6.**", color);
                    this.sunDrawable.setLayerColor("Path.**", color);
                    this.sunDrawable.setLayerColor("Path 5.**", color);
                    this.sunDrawable.commitApplyLayerColors();
                    r8 = 0;
                    f = 0.0f;
                    this.listView2 = new RecyclerListView(context) { // from class: org.telegram.ui.ThemePreviewActivity.11
                        float lastX;
                        float lastY;
                        boolean scrollingBackground;
                        float startX;
                        float startY;

                        private void checkMotionEvent(MotionEvent motionEvent) {
                            if (motionEvent.getAction() == 1) {
                                if (!ThemePreviewActivity.this.wasScroll && (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) && ThemePreviewActivity.this.patternLayout[0].getVisibility() == 0) {
                                    ThemePreviewActivity.this.showPatternsView(0, false, true);
                                }
                                ThemePreviewActivity.this.wasScroll = false;
                            }
                        }

                        @Override // org.telegram.ui.Components.RecyclerListView
                        protected boolean allowSelectChildAtPosition(View view2) {
                            RecyclerView.ViewHolder findContainingViewHolder = ThemePreviewActivity.this.listView2.findContainingViewHolder(view2);
                            if (findContainingViewHolder == null || findContainingViewHolder.getItemViewType() != 2) {
                                return super.allowSelectChildAtPosition(view2);
                            }
                            return false;
                        }

                        @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                        public boolean drawChild(Canvas canvas, View view2, long j) {
                            RecyclerView.ViewHolder childViewHolder;
                            boolean drawChild = super.drawChild(canvas, view2, j);
                            if (view2 instanceof ChatMessageCell) {
                                ChatMessageCell chatMessageCell = (ChatMessageCell) view2;
                                chatMessageCell.getMessageObject();
                                ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                                if (avatarImage != null) {
                                    int top = view2.getTop();
                                    if (chatMessageCell.isPinnedBottom() && (childViewHolder = ThemePreviewActivity.this.listView2.getChildViewHolder(view2)) != null) {
                                        if (ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder.getAdapterPosition() - 1) != null) {
                                            avatarImage.setImageY(-AndroidUtilities.dp(1000.0f));
                                            avatarImage.draw(canvas);
                                            return drawChild;
                                        }
                                    }
                                    float translationX = chatMessageCell.getTranslationX();
                                    int top2 = view2.getTop() + chatMessageCell.getLayoutHeight();
                                    int measuredHeight = ThemePreviewActivity.this.listView2.getMeasuredHeight() - ThemePreviewActivity.this.listView2.getPaddingBottom();
                                    if (top2 > measuredHeight) {
                                        top2 = measuredHeight;
                                    }
                                    if (chatMessageCell.isPinnedTop() && (childViewHolder2 = ThemePreviewActivity.this.listView2.getChildViewHolder(view2)) != null) {
                                        int i252 = 0;
                                        while (i252 < 20) {
                                            i252++;
                                            RecyclerView.ViewHolder childViewHolder2 = ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder2.getAdapterPosition() + 1);
                                            if (childViewHolder2 == null) {
                                                break;
                                            }
                                            top = childViewHolder2.itemView.getTop();
                                            if (top2 - AndroidUtilities.dp(48.0f) < childViewHolder2.itemView.getBottom()) {
                                                translationX = Math.min(childViewHolder2.itemView.getTranslationX(), translationX);
                                            }
                                            View view3 = childViewHolder2.itemView;
                                            if (!(view3 instanceof ChatMessageCell) || !((ChatMessageCell) view3).isPinnedTop()) {
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

                        @Override // org.telegram.ui.Components.RecyclerListView
                        protected void onChildPressed(View view2, float f13, float f14, boolean z6) {
                            if (z6 && (view2 instanceof ChatMessageCell) && !((ChatMessageCell) view2).isInsideBackground(f13, f14)) {
                                return;
                            }
                            super.onChildPressed(view2, f13, f14, z6);
                        }

                        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
                        protected void onLayout(boolean z6, int i252, int i26, int i27, int i28) {
                            super.onLayout(z6, i252, i26, i27, i28);
                            ThemePreviewActivity.this.invalidateBlur();
                        }

                        /* JADX WARN: Code restructure failed: missing block: B:17:0x004a, code lost:
                        
                            if (getParent() != null) goto L17;
                         */
                        /* JADX WARN: Code restructure failed: missing block: B:6:0x0027, code lost:
                        
                            if (getParent() != null) goto L17;
                         */
                        /* JADX WARN: Code restructure failed: missing block: B:7:0x0053, code lost:
                        
                            r4.scrollingBackground = true;
                         */
                        /* JADX WARN: Code restructure failed: missing block: B:9:0x004c, code lost:
                        
                            getParent().requestDisallowInterceptTouchEvent(true);
                         */
                        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                        */
                        public boolean onTouchEvent(MotionEvent motionEvent) {
                            checkMotionEvent(motionEvent);
                            if (ThemePreviewActivity.this.hasScrollingBackground) {
                                if (motionEvent.getAction() == 0) {
                                    float x = motionEvent.getX();
                                    this.startX = x;
                                    this.lastX = x;
                                    float y = motionEvent.getY();
                                    this.startY = y;
                                    this.lastY = y;
                                } else {
                                    if (motionEvent.getAction() == 2) {
                                        if (!this.scrollingBackground && Math.abs(this.startX - motionEvent.getX()) > AndroidUtilities.touchSlop) {
                                        }
                                    } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                                        this.scrollingBackground = false;
                                        if (getParent() != null) {
                                            getParent().requestDisallowInterceptTouchEvent(false);
                                        }
                                    }
                                    ThemePreviewActivity.this.gestureDetector2.onTouchEvent(motionEvent);
                                }
                            }
                            return this.scrollingBackground || super.onTouchEvent(motionEvent);
                        }

                        @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
                        public void setTranslationY(float f13) {
                            super.setTranslationY(f13);
                            if (ThemePreviewActivity.this.backgroundCheckBoxView != null) {
                                for (int i252 = 0; i252 < ThemePreviewActivity.this.backgroundCheckBoxView.length; i252++) {
                                    ThemePreviewActivity.this.backgroundCheckBoxView[i252].invalidate();
                                }
                            }
                            if (ThemePreviewActivity.this.messagesCheckBoxView != null) {
                                for (int i26 = 0; i26 < ThemePreviewActivity.this.messagesCheckBoxView.length; i26++) {
                                    ThemePreviewActivity.this.messagesCheckBoxView[i26].invalidate();
                                }
                            }
                            if (ThemePreviewActivity.this.backgroundPlayAnimationView != null) {
                                ThemePreviewActivity.this.backgroundPlayAnimationView.invalidate();
                            }
                            if (ThemePreviewActivity.this.messagesPlayAnimationView != null) {
                                ThemePreviewActivity.this.messagesPlayAnimationView.invalidate();
                            }
                        }
                    };
                    DefaultItemAnimator defaultItemAnimator22 = new DefaultItemAnimator() { // from class: org.telegram.ui.ThemePreviewActivity.12
                        @Override // androidx.recyclerview.widget.DefaultItemAnimator
                        protected void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                            ThemePreviewActivity.this.listView2.invalidateViews();
                        }
                    };
                    defaultItemAnimator22.setDelayAnimations(r8);
                    this.listView2.setItemAnimator(defaultItemAnimator22);
                    this.listView2.setVerticalScrollBarEnabled(true);
                    this.listView2.setOverScrollMode(2);
                    i4 = this.screenType;
                    if (i4 == 2) {
                        this.listView2.setPadding(r8, AndroidUtilities.dp(4.0f), r8, (AndroidUtilities.dp(72 + ((this.self || this.dialogId <= 0) ? 0 : 58)) - 12) + (insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0));
                    } else {
                        RecyclerListView recyclerListView2 = this.listView2;
                        if (i4 == 1) {
                            dp = AndroidUtilities.dp(4.0f);
                            dp2 = AndroidUtilities.dp(16.0f);
                        } else {
                            dp = AndroidUtilities.dp(4.0f);
                            dp2 = AndroidUtilities.dp(4.0f);
                        }
                        recyclerListView2.setPadding(r8, dp, r8, dp2);
                    }
                    this.listView2.setClipToPadding(r8);
                    this.listView2.setLayoutManager(new LinearLayoutManager(context, 1, true));
                    this.listView2.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
                    if (this.screenType == 1) {
                        this.page2.addView(this.listView2, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, 273.0f));
                        this.listView2.setOnItemClickListener(new RecyclerListView.OnItemClickListenerExtended() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda23
                            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                            public /* synthetic */ boolean hasDoubleTap(View view2, int i26) {
                                return RecyclerListView.OnItemClickListenerExtended.-CC.$default$hasDoubleTap(this, view2, i26);
                            }

                            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                            public /* synthetic */ void onDoubleTap(View view2, int i26, float f13, float f14) {
                                RecyclerListView.OnItemClickListenerExtended.-CC.$default$onDoubleTap(this, view2, i26, f13, f14);
                            }

                            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended
                            public final void onItemClick(View view2, int i26, float f13, float f14) {
                                ThemePreviewActivity.this.lambda$createView$6(view2, i26, f13, f14);
                            }
                        });
                        i5 = -1;
                    } else {
                        i5 = -1;
                        this.page2.addView(this.listView2, LayoutHelper.createFrame(-1, -1, 51));
                    }
                    this.listView2.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ThemePreviewActivity.13
                        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                        public void onScrollStateChanged(RecyclerView recyclerView, int i252) {
                            if (i252 == 0) {
                                ThemePreviewActivity.this.wasScroll = false;
                            }
                        }

                        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                        public void onScrolled(RecyclerView recyclerView, int i252, int i26) {
                            ThemePreviewActivity.this.listView2.invalidateViews();
                            ThemePreviewActivity.this.wasScroll = true;
                        }
                    });
                    this.page2.addView(this.actionBar2, LayoutHelper.createFrame(i5, -2.0f));
                    WallpaperParallaxEffect wallpaperParallaxEffect22 = new WallpaperParallaxEffect(context);
                    this.parallaxEffect = wallpaperParallaxEffect22;
                    wallpaperParallaxEffect22.setCallback(new WallpaperParallaxEffect.Callback() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda8
                        @Override // org.telegram.ui.Components.WallpaperParallaxEffect.Callback
                        public final void onOffsetsChanged(int i252, int i26, float f13) {
                            ThemePreviewActivity.this.lambda$createView$7(i252, i26, f13);
                        }
                    });
                    i6 = this.screenType;
                    if (i6 != 1 || i6 == 2) {
                        if (i6 == 2) {
                            final boolean insideBottomSheet = insideBottomSheet();
                            FrameLayout frameLayout7 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.14
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
                                        canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), NotificationCenter.newLocationAvailable, 31);
                                        Theme.applyServiceShaderMatrixForView(this, ThemePreviewActivity.this.backgroundImage, ThemePreviewActivity.this.themeDelegate);
                                        Paint paint = ThemePreviewActivity.this.themeDelegate.getPaint("paintChatActionBackground");
                                        ColorFilter colorFilter = paint.getColorFilter();
                                        paint.setColorFilter(this.colorFilter);
                                        float f13 = 1.0f;
                                        if (ThemePreviewActivity.this.backgroundImage != null && (ThemePreviewActivity.this.backgroundImage.getBackground() instanceof MotionBackgroundDrawable) && ThemePreviewActivity.this.currentIntensity < 0.0f) {
                                            f13 = 0.33f;
                                        }
                                        int alpha = paint.getAlpha();
                                        paint.setAlpha((int) (alpha * f13));
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
                                protected void onMeasure(int i26, int i27) {
                                    super.onMeasure(i26, i27);
                                    for (int i28 = 0; i28 < getChildCount(); i28++) {
                                        View childAt = getChildAt(i28);
                                        if (childAt.getMeasuredWidth() > AndroidUtilities.dp(420.0f)) {
                                            childAt.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(420.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getMeasuredHeight(), 1073741824));
                                        }
                                    }
                                }
                            };
                            this.bottomOverlayChat = frameLayout7;
                            frameLayout7.setWillNotDraw(r8);
                            this.bottomOverlayChat.setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f) + (insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0));
                            this.page2.addView(this.bottomOverlayChat, LayoutHelper.createFrame(-1, (int) r8, 81));
                            BlurButton blurButton2 = new BlurButton(context);
                            this.applyButton1 = blurButton2;
                            ScaleStateListAnimator.apply(blurButton2, 0.033f, 1.2f);
                            updateApplyButton1(r8);
                            this.applyButton1.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda9
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view2) {
                                    ThemePreviewActivity.this.lambda$createView$8(view2);
                                }
                            });
                            if (this.dialogId <= 0 || this.self || this.serverWallpaper != null) {
                                frameLayout2 = this.bottomOverlayChat;
                                blurButton = this.applyButton1;
                                f7 = 0.0f;
                                f8 = 0.0f;
                                i18 = -1;
                                f9 = 48.0f;
                                i19 = 81;
                                f10 = 0.0f;
                                f11 = 0.0f;
                            } else {
                                BlurButton blurButton3 = new BlurButton(context);
                                this.applyButton2 = blurButton3;
                                ScaleStateListAnimator.apply(blurButton3, 0.033f, 1.2f);
                                TLRPC.User user = getMessagesController().getUser(Long.valueOf(this.dialogId));
                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str2);
                                if (getUserConfig().isPremium()) {
                                    i20 = 1;
                                } else {
                                    spannableStringBuilder.append((CharSequence) "l ");
                                    i20 = 1;
                                    spannableStringBuilder.setSpan(new ColoredImageSpan(R.drawable.msg_mini_lock3), r8, 1, 33);
                                }
                                int i26 = R.string.ApplyWallpaperForMeAndPeer;
                                Object[] objArr = new Object[i20];
                                objArr[r8] = UserObject.getUserName(user);
                                spannableStringBuilder.append((CharSequence) LocaleController.formatString(i26, objArr));
                                this.applyButton2.setText(spannableStringBuilder);
                                try {
                                    BlurButton blurButton4 = this.applyButton2;
                                    blurButton4.setText(Emoji.replaceEmoji(blurButton4.getText(), this.applyButton2.text.getFontMetricsInt(), r8));
                                } catch (Exception unused) {
                                }
                                this.applyButton2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda10
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view2) {
                                        ThemePreviewActivity.this.lambda$createView$9(view2);
                                    }
                                });
                                f7 = 0.0f;
                                i18 = -1;
                                f9 = 48.0f;
                                i19 = 81;
                                f10 = 0.0f;
                                f11 = 0.0f;
                                this.bottomOverlayChat.addView(this.applyButton1, LayoutHelper.createFrame(-1, 48.0f, 81, 0.0f, 0.0f, 0.0f, 58.0f));
                                frameLayout2 = this.bottomOverlayChat;
                                blurButton = this.applyButton2;
                                f8 = 0.0f;
                            }
                            frameLayout2.addView(blurButton, LayoutHelper.createFrame(i18, f9, i19, f10, f11, f7, f8));
                            if (this.shouldShowBrightnessControll) {
                                FrameLayout frameLayout8 = new FrameLayout(getContext()) { // from class: org.telegram.ui.ThemePreviewActivity.15
                                    private final Paint shadowPaint = new Paint(1);
                                    private final Paint dimPaint = new Paint(1);
                                    private final Paint dimPaint2 = new Paint(1);

                                    @Override // android.view.ViewGroup, android.view.View
                                    protected void dispatchDraw(Canvas canvas) {
                                        RectF rectF = AndroidUtilities.rectTmp;
                                        rectF.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
                                        float dp3 = AndroidUtilities.dp(8.0f);
                                        this.shadowPaint.setColor(0);
                                        this.shadowPaint.setShadowLayer(AndroidUtilities.dpf2(1.0f), 0.0f, AndroidUtilities.dpf2(0.33f), ColorUtils.setAlphaComponent(-16777216, (int) (ThemePreviewActivity.this.dimmingSlider.getAlpha() * 27.0f)));
                                        canvas.drawRoundRect(rectF, dp3, dp3, this.shadowPaint);
                                        Theme.applyServiceShaderMatrixForView(this, ThemePreviewActivity.this.backgroundImage, ThemePreviewActivity.this.themeDelegate);
                                        Paint paint = ThemePreviewActivity.this.themeDelegate.getPaint("paintChatActionBackground");
                                        int alpha = paint.getAlpha();
                                        paint.setAlpha((int) (alpha * ThemePreviewActivity.this.dimmingSlider.getAlpha()));
                                        canvas.drawRoundRect(rectF, dp3, dp3, paint);
                                        paint.setAlpha(alpha);
                                        if (ThemePreviewActivity.this.shouldShowBrightnessControll && ThemePreviewActivity.this.dimAmount > 0.0f) {
                                            this.dimPaint2.setColor(ColorUtils.setAlphaComponent(-16777216, (int) (ThemePreviewActivity.this.dimAmount * 255.0f * ThemePreviewActivity.this.progressToDarkTheme)));
                                            canvas.drawRoundRect(rectF, dp3, dp3, this.dimPaint2);
                                        }
                                        this.dimPaint.setColor(520093695);
                                        this.dimPaint.setAlpha((int) (ThemePreviewActivity.this.dimmingSlider.getAlpha() * 30.0f));
                                        canvas.drawRoundRect(rectF, dp3, dp3, this.dimPaint);
                                        super.dispatchDraw(canvas);
                                    }
                                };
                                this.dimmingSliderContainer = frameLayout8;
                                frameLayout8.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                this.page2.addView(this.dimmingSliderContainer, LayoutHelper.createFrame(NotificationCenter.starUserGiftsLoaded, 76, 49));
                                SliderView sliderView = new SliderView(getContext(), 3) { // from class: org.telegram.ui.ThemePreviewActivity.16
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
                                this.dimmingSlider.setMinMax(f, 0.9f);
                                this.dimmingSlider.setOnValueChange(new Utilities.Callback() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda11
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
                        final Rect rect22 = new Rect();
                        Drawable mutate322 = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
                        this.sheetDrawable = mutate322;
                        mutate322.getPadding(rect22);
                        this.sheetDrawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundWhite), PorterDuff.Mode.MULTIPLY));
                        TextPaint textPaint22 = new TextPaint(1);
                        textPaint22.setTextSize(AndroidUtilities.dp(14.0f));
                        textPaint22.setTypeface(AndroidUtilities.bold());
                        obj = this.currentWallpaper;
                        if (this.screenType != 1 || (obj instanceof WallpapersListActivity.ColorWallpaper)) {
                            if ((obj instanceof WallpapersListActivity.ColorWallpaper) || !"d".equals(((WallpapersListActivity.ColorWallpaper) obj).slug)) {
                                i7 = 3;
                                String[] strArr22 = new String[i7];
                                int[] iArr222 = new int[i7];
                                this.backgroundCheckBoxView = new WallpaperCheckBoxView[i7];
                                if (i7 == 0) {
                                    this.backgroundButtonsContainer = new FrameLayout(context);
                                    if (this.screenType == 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                                        strArr22[r8] = LocaleController.getString(R.string.BackgroundColors);
                                        strArr22[1] = LocaleController.getString(R.string.BackgroundPattern);
                                        strArr22[2] = LocaleController.getString(R.string.BackgroundMotion);
                                    } else {
                                        strArr22[r8] = LocaleController.getString(R.string.BackgroundBlurred);
                                        strArr22[1] = LocaleController.getString(R.string.BackgroundMotion);
                                    }
                                    i9 = 0;
                                    int i27 = 0;
                                    while (i27 < i7) {
                                        int ceil = (int) Math.ceil(textPaint22.measureText(strArr22[i27]));
                                        iArr222[i27] = ceil;
                                        i9 = Math.max(i9, ceil);
                                        i27++;
                                        i7 = i7;
                                    }
                                    i8 = i7;
                                    FrameLayout frameLayout9 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.17
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
                                    this.backgroundPlayAnimationView = frameLayout9;
                                    frameLayout9.setWillNotDraw(r8);
                                    this.backgroundPlayAnimationView.setVisibility(this.backgroundGradientColor1 != 0 ? 0 : 4);
                                    this.backgroundPlayAnimationView.setScaleX(this.backgroundGradientColor1 != 0 ? 1.0f : 0.1f);
                                    this.backgroundPlayAnimationView.setScaleY(this.backgroundGradientColor1 != 0 ? 1.0f : 0.1f);
                                    this.backgroundPlayAnimationView.setAlpha(this.backgroundGradientColor1 != 0 ? 1.0f : 0.0f);
                                    this.backgroundPlayAnimationView.setTag(this.backgroundGradientColor1 != 0 ? 1 : null);
                                    this.backgroundButtonsContainer.addView(this.backgroundPlayAnimationView, LayoutHelper.createFrame(48, 48, 17));
                                    this.backgroundPlayAnimationView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity.18
                                        int rotation = 0;

                                        @Override // android.view.View.OnClickListener
                                        public void onClick(View view2) {
                                            ThemePreviewActivity.this.backgroundPlayAnimationImageView.setRotation(this.rotation);
                                            this.rotation -= 45;
                                            ThemePreviewActivity.this.backgroundPlayAnimationImageView.animate().rotationBy(-45.0f).setDuration(300L).setInterpolator(CubicBezierInterpolator.EASE_OUT).start();
                                            if (ThemePreviewActivity.this.backgroundImages[0] != null) {
                                                Drawable background = ThemePreviewActivity.this.backgroundImages[0].getBackground();
                                                if (background instanceof MotionBackgroundDrawable) {
                                                    ((MotionBackgroundDrawable) background).switchToNextPosition();
                                                } else {
                                                    ThemePreviewActivity.this.onColorsRotate();
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
                                    i8 = i7;
                                    i9 = 0;
                                }
                                i10 = i8;
                                i11 = 0;
                                while (i11 < i10) {
                                    this.backgroundCheckBoxView[i11] = new WallpaperCheckBoxView(context, ((this.screenType == 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) && i11 == 0) ? false : true, this.backgroundImage, this.themeDelegate);
                                    this.backgroundCheckBoxView[i11].setBackgroundColor(this.backgroundColor);
                                    this.backgroundCheckBoxView[i11].setText(strArr22[i11], iArr222[i11], i9);
                                    if (this.screenType != 1 && !(this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                                        WallpaperCheckBoxView wallpaperCheckBoxView2 = this.backgroundCheckBoxView[i11];
                                        if (i11 == 0) {
                                            z3 = this.isBlurred;
                                            wallpaperCheckBoxView = wallpaperCheckBoxView2;
                                        } else {
                                            z3 = this.isMotion;
                                            wallpaperCheckBoxView = wallpaperCheckBoxView2;
                                        }
                                    } else if (i11 == 1) {
                                        WallpaperCheckBoxView wallpaperCheckBoxView3 = this.backgroundCheckBoxView[i11];
                                        if (this.selectedPattern == null && ((themeAccent = this.accent) == null || TextUtils.isEmpty(themeAccent.patternSlug))) {
                                            z3 = false;
                                            wallpaperCheckBoxView = wallpaperCheckBoxView3;
                                        } else {
                                            z3 = true;
                                            wallpaperCheckBoxView = wallpaperCheckBoxView3;
                                        }
                                    } else {
                                        if (i11 == 2) {
                                            this.backgroundCheckBoxView[i11].setChecked(this.isMotion, r8);
                                        }
                                        int dp3 = AndroidUtilities.dp(56.0f) + i9;
                                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dp3, -2);
                                        layoutParams.gravity = 17;
                                        if (i10 != 3) {
                                            i17 = 2;
                                            if (i11 != 0) {
                                            }
                                            layoutParams.leftMargin = (dp3 / i17) + AndroidUtilities.dp(10.0f);
                                        } else {
                                            i17 = 2;
                                        }
                                        this.backgroundButtonsContainer.addView(this.backgroundCheckBoxView[i11], layoutParams);
                                        final WallpaperCheckBoxView wallpaperCheckBoxView4 = this.backgroundCheckBoxView[i11];
                                        wallpaperCheckBoxView4.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda12
                                            @Override // android.view.View.OnClickListener
                                            public final void onClick(View view2) {
                                                ThemePreviewActivity.this.lambda$createView$11(i11, wallpaperCheckBoxView4, view2);
                                            }
                                        });
                                        if (i11 != 2) {
                                            this.backgroundCheckBoxView[i11].setAlpha(0.0f);
                                            this.backgroundCheckBoxView[i11].setVisibility(4);
                                        }
                                        i11++;
                                    }
                                    wallpaperCheckBoxView.setChecked(z3, r8);
                                    int dp32 = AndroidUtilities.dp(56.0f) + i9;
                                    FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(dp32, -2);
                                    layoutParams2.gravity = 17;
                                    if (i10 != 3) {
                                    }
                                    this.backgroundButtonsContainer.addView(this.backgroundCheckBoxView[i11], layoutParams2);
                                    final WallpaperCheckBoxView wallpaperCheckBoxView42 = this.backgroundCheckBoxView[i11];
                                    wallpaperCheckBoxView42.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda12
                                        @Override // android.view.View.OnClickListener
                                        public final void onClick(View view2) {
                                            ThemePreviewActivity.this.lambda$createView$11(i11, wallpaperCheckBoxView42, view2);
                                        }
                                    });
                                    if (i11 != 2) {
                                    }
                                    i11++;
                                }
                                if (this.screenType == 1) {
                                    int[] iArr3 = new int[2];
                                    this.messagesCheckBoxView = new WallpaperCheckBoxView[2];
                                    this.messagesButtonsContainer = new FrameLayout(context);
                                    String[] strArr3 = {LocaleController.getString(R.string.BackgroundAnimate), LocaleController.getString(R.string.BackgroundColors)};
                                    int i28 = 0;
                                    int i29 = 0;
                                    for (int i30 = 2; i28 < i30; i30 = 2) {
                                        int ceil2 = (int) Math.ceil(textPaint22.measureText(strArr3[i28]));
                                        iArr3[i28] = ceil2;
                                        i29 = Math.max(i29, ceil2);
                                        i28++;
                                    }
                                    if (this.accent != null) {
                                        FrameLayout frameLayout10 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.19
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
                                        this.messagesPlayAnimationView = frameLayout10;
                                        frameLayout10.setWillNotDraw(r8);
                                        this.messagesPlayAnimationView.setVisibility(this.accent.myMessagesGradientAccentColor1 != 0 ? 0 : 4);
                                        this.messagesPlayAnimationView.setScaleX(this.accent.myMessagesGradientAccentColor1 != 0 ? 1.0f : 0.1f);
                                        this.messagesPlayAnimationView.setScaleY(this.accent.myMessagesGradientAccentColor1 != 0 ? 1.0f : 0.1f);
                                        this.messagesPlayAnimationView.setAlpha(this.accent.myMessagesGradientAccentColor1 != 0 ? 1.0f : 0.0f);
                                        this.messagesButtonsContainer.addView(this.messagesPlayAnimationView, LayoutHelper.createFrame(48, 48, 17));
                                        this.messagesPlayAnimationView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity.20
                                            int rotation = 0;

                                            @Override // android.view.View.OnClickListener
                                            public void onClick(View view2) {
                                                ThemePreviewActivity.this.messagesPlayAnimationImageView.setRotation(this.rotation);
                                                this.rotation -= 45;
                                                ThemePreviewActivity.this.messagesPlayAnimationImageView.animate().rotationBy(-45.0f).setDuration(300L).setInterpolator(CubicBezierInterpolator.EASE_OUT).start();
                                                if (ThemePreviewActivity.this.accent.myMessagesAnimated) {
                                                    if (ThemePreviewActivity.this.msgOutDrawable.getMotionBackgroundDrawable() != null) {
                                                        ThemePreviewActivity.this.msgOutDrawable.getMotionBackgroundDrawable().switchToNextPosition();
                                                        return;
                                                    }
                                                    return;
                                                }
                                                if (ThemePreviewActivity.this.accent.myMessagesGradientAccentColor3 != 0) {
                                                    int i31 = ThemePreviewActivity.this.accent.myMessagesAccentColor != 0 ? ThemePreviewActivity.this.accent.myMessagesAccentColor : ThemePreviewActivity.this.accent.accentColor;
                                                    ThemePreviewActivity.this.accent.myMessagesAccentColor = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1;
                                                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1 = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2;
                                                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2 = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor3;
                                                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor3 = i31;
                                                } else {
                                                    int i32 = ThemePreviewActivity.this.accent.myMessagesAccentColor != 0 ? ThemePreviewActivity.this.accent.myMessagesAccentColor : ThemePreviewActivity.this.accent.accentColor;
                                                    ThemePreviewActivity.this.accent.myMessagesAccentColor = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1;
                                                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor1 = ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2;
                                                    ThemePreviewActivity.this.accent.myMessagesGradientAccentColor2 = i32;
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
                                            }
                                        });
                                        ImageView imageView5 = new ImageView(context);
                                        this.messagesPlayAnimationImageView = imageView5;
                                        imageView5.setScaleType(ImageView.ScaleType.CENTER);
                                        this.messagesPlayAnimationImageView.setImageResource(R.drawable.bg_rotate_large);
                                        this.messagesPlayAnimationView.addView(this.messagesPlayAnimationImageView, LayoutHelper.createFrame(-2, -2, 17));
                                        final int i31 = 0;
                                        while (i31 < 2) {
                                            this.messagesCheckBoxView[i31] = new WallpaperCheckBoxView(context, i31 == 0, this.backgroundImage, this.themeDelegate);
                                            this.messagesCheckBoxView[i31].setText(strArr3[i31], iArr3[i31], i29);
                                            if (i31 == 0) {
                                                this.messagesCheckBoxView[i31].setChecked(this.accent.myMessagesAnimated, r8);
                                            }
                                            int dp4 = AndroidUtilities.dp(56.0f) + i29;
                                            FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(dp4, -2);
                                            layoutParams3.gravity = 17;
                                            if (i31 == 1) {
                                                layoutParams3.leftMargin = (dp4 / 2) + AndroidUtilities.dp(10.0f);
                                            } else {
                                                layoutParams3.rightMargin = (dp4 / 2) + AndroidUtilities.dp(10.0f);
                                            }
                                            this.messagesButtonsContainer.addView(this.messagesCheckBoxView[i31], layoutParams3);
                                            final WallpaperCheckBoxView wallpaperCheckBoxView5 = this.messagesCheckBoxView[i31];
                                            wallpaperCheckBoxView5.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda13
                                                @Override // android.view.View.OnClickListener
                                                public final void onClick(View view2) {
                                                    ThemePreviewActivity.this.lambda$createView$12(i31, wallpaperCheckBoxView5, view2);
                                                }
                                            });
                                            i31++;
                                        }
                                    }
                                }
                                if (this.screenType != 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                                    this.isBlurred = r8;
                                    i12 = 0;
                                    for (i13 = 2; i12 < i13; i13 = 2) {
                                        this.patternLayout[i12] = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.21
                                            @Override // android.view.View
                                            public void onDraw(Canvas canvas) {
                                                Drawable drawable;
                                                int i32;
                                                int measuredWidth;
                                                if (i12 == 0) {
                                                    drawable = ThemePreviewActivity.this.sheetDrawable;
                                                    i32 = ThemePreviewActivity.this.colorPicker.getLeft() - rect22.left;
                                                    measuredWidth = ThemePreviewActivity.this.colorPicker.getRight();
                                                } else {
                                                    drawable = ThemePreviewActivity.this.sheetDrawable;
                                                    i32 = -rect22.left;
                                                    measuredWidth = getMeasuredWidth();
                                                }
                                                drawable.setBounds(i32, 0, measuredWidth + rect22.right, getMeasuredHeight());
                                                ThemePreviewActivity.this.sheetDrawable.draw(canvas);
                                            }
                                        };
                                        if (i12 == 1 || this.screenType == i13) {
                                            this.patternLayout[i12].setVisibility(4);
                                        }
                                        this.patternLayout[i12].setWillNotDraw(r8);
                                        if (this.screenType == 2) {
                                            if (i12 == 0) {
                                                i14 = 321;
                                            }
                                            i14 = 316;
                                        } else {
                                            if (i12 == 0) {
                                                i14 = NotificationCenter.chatAvailableReactionsUpdated;
                                            }
                                            i14 = 316;
                                        }
                                        FrameLayout.LayoutParams createFrame = LayoutHelper.createFrame(-1, i14, 83);
                                        createFrame.height = AndroidUtilities.dp(i12 == 0 ? this.screenType == 2 ? 321 : NotificationCenter.chatAvailableReactionsUpdated : 316.0f);
                                        if (insideBottomSheet()) {
                                            createFrame.height += AndroidUtilities.navigationBarHeight;
                                        }
                                        if (i12 == 0) {
                                            Drawable drawable = this.sheetDrawable;
                                            Rect rect3 = AndroidUtilities.rectTmp2;
                                            drawable.getPadding(rect3);
                                            createFrame.height += AndroidUtilities.dp(12.0f) + rect3.top;
                                        }
                                        this.patternLayout[i12].setPadding(r8, i12 == 0 ? AndroidUtilities.dp(12.0f) + rect22.top : 0, r8, insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0);
                                        this.page2.addView(this.patternLayout[i12], createFrame);
                                        if (i12 == 1 || this.screenType == 2) {
                                            this.patternsButtonsContainer[i12] = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.22
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
                                            this.patternsButtonsContainer[i12].setWillNotDraw(r8);
                                            this.patternsButtonsContainer[i12].setPadding(r8, AndroidUtilities.dp(3.0f), r8, r8);
                                            this.patternsButtonsContainer[i12].setClickable(true);
                                            this.patternLayout[i12].addView(this.patternsButtonsContainer[i12], LayoutHelper.createFrame(-1, 51, 80));
                                            this.patternsCancelButton[i12] = new TextView(context);
                                            this.patternsCancelButton[i12].setTextSize(1, 15.0f);
                                            this.patternsCancelButton[i12].setTypeface(AndroidUtilities.bold());
                                            TextView textView3 = this.patternsCancelButton[i12];
                                            int i32 = Theme.key_chat_fieldOverlayText;
                                            textView3.setTextColor(getThemedColor(i32));
                                            this.patternsCancelButton[i12].setText(LocaleController.getString(R.string.Cancel).toUpperCase());
                                            this.patternsCancelButton[i12].setGravity(17);
                                            this.patternsCancelButton[i12].setPadding(AndroidUtilities.dp(21.0f), r8, AndroidUtilities.dp(21.0f), r8);
                                            TextView textView4 = this.patternsCancelButton[i12];
                                            int i33 = Theme.key_listSelector;
                                            textView4.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(i33), r8));
                                            this.patternsButtonsContainer[i12].addView(this.patternsCancelButton[i12], LayoutHelper.createFrame(-2, -1, 51));
                                            this.patternsCancelButton[i12].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda14
                                                @Override // android.view.View.OnClickListener
                                                public final void onClick(View view2) {
                                                    ThemePreviewActivity.this.lambda$createView$13(i12, view2);
                                                }
                                            });
                                            this.patternsSaveButton[i12] = new TextView(context);
                                            this.patternsSaveButton[i12].setTextSize(1, 15.0f);
                                            this.patternsSaveButton[i12].setTypeface(AndroidUtilities.bold());
                                            this.patternsSaveButton[i12].setTextColor(getThemedColor(i32));
                                            this.patternsSaveButton[i12].setText(LocaleController.getString(R.string.ApplyTheme).toUpperCase());
                                            this.patternsSaveButton[i12].setGravity(17);
                                            this.patternsSaveButton[i12].setPadding(AndroidUtilities.dp(21.0f), r8, AndroidUtilities.dp(21.0f), r8);
                                            this.patternsSaveButton[i12].setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(i33), r8));
                                            this.patternsButtonsContainer[i12].addView(this.patternsSaveButton[i12], LayoutHelper.createFrame(-2, -1, 53));
                                            this.patternsSaveButton[i12].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda15
                                                @Override // android.view.View.OnClickListener
                                                public final void onClick(View view2) {
                                                    ThemePreviewActivity.this.lambda$createView$14(i12, view2);
                                                }
                                            });
                                            r1 = 1;
                                        } else {
                                            r1 = 1;
                                        }
                                        if (i12 == r1) {
                                            TextView textView5 = new TextView(context);
                                            this.patternTitleView = textView5;
                                            textView5.setLines(r1);
                                            this.patternTitleView.setSingleLine(r1);
                                            this.patternTitleView.setText(LocaleController.getString(R.string.BackgroundChoosePattern));
                                            this.patternTitleView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                                            this.patternTitleView.setTextSize(r1, 20.0f);
                                            this.patternTitleView.setTypeface(AndroidUtilities.bold());
                                            this.patternTitleView.setPadding(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(21.0f), AndroidUtilities.dp(8.0f));
                                            this.patternTitleView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                                            this.patternTitleView.setGravity(16);
                                            f3 = 0.0f;
                                            i15 = -1;
                                            i16 = 51;
                                            this.patternLayout[i12].addView(this.patternTitleView, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 21.0f, 0.0f, 0.0f));
                                            RecyclerListView recyclerListView3 = new RecyclerListView(context) { // from class: org.telegram.ui.ThemePreviewActivity.23
                                                @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
                                                public boolean onTouchEvent(MotionEvent motionEvent) {
                                                    if (motionEvent.getAction() == 0) {
                                                        getParent().requestDisallowInterceptTouchEvent(true);
                                                    }
                                                    return super.onTouchEvent(motionEvent);
                                                }
                                            };
                                            this.patternsListView = recyclerListView3;
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, r8, r8);
                                            this.patternsLayoutManager = linearLayoutManager;
                                            recyclerListView3.setLayoutManager(linearLayoutManager);
                                            RecyclerListView recyclerListView4 = this.patternsListView;
                                            PatternsAdapter patternsAdapter = new PatternsAdapter(context);
                                            this.patternsAdapter = patternsAdapter;
                                            recyclerListView4.setAdapter(patternsAdapter);
                                            this.patternsListView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.ThemePreviewActivity.24
                                                @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
                                                public void getItemOffsets(Rect rect4, View view2, RecyclerView recyclerView, RecyclerView.State state) {
                                                    int childAdapterPosition = recyclerView.getChildAdapterPosition(view2);
                                                    rect4.left = AndroidUtilities.dp(12.0f);
                                                    rect4.top = 0;
                                                    rect4.bottom = 0;
                                                    if (childAdapterPosition == state.getItemCount() - 1) {
                                                        rect4.right = AndroidUtilities.dp(12.0f);
                                                    }
                                                }
                                            });
                                            this.patternLayout[i12].addView(this.patternsListView, LayoutHelper.createFrame(-1, 100.0f, 51, 0.0f, 76.0f, 0.0f, 0.0f));
                                            this.patternsListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda16
                                                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                                                public final void onItemClick(View view2, int i34) {
                                                    ThemePreviewActivity.this.lambda$createView$15(view2, i34);
                                                }
                                            });
                                            HeaderCell headerCell = new HeaderCell(context);
                                            this.intensityCell = headerCell;
                                            headerCell.setText(LocaleController.getString(R.string.BackgroundIntensity));
                                            this.patternLayout[i12].addView(this.intensityCell, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 175.0f, 0.0f, 0.0f));
                                            SeekBarView seekBarView = new SeekBarView(context, getResourceProvider()) { // from class: org.telegram.ui.ThemePreviewActivity.25
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
                                                public void onSeekBarDrag(boolean z6, float f13) {
                                                    ThemePreviewActivity.this.currentIntensity = f13;
                                                    ThemePreviewActivity.this.updateIntensity();
                                                }

                                                @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
                                                public void onSeekBarPressed(boolean z6) {
                                                }
                                            });
                                            frameLayout = this.patternLayout[i12];
                                            view = this.intensitySeekBar;
                                            f2 = 5.0f;
                                            f4 = 38.0f;
                                            f5 = 5.0f;
                                            f6 = 211.0f;
                                        } else {
                                            ColorPicker colorPicker = new ColorPicker(context, this.editingTheme, new 27());
                                            this.colorPicker = colorPicker;
                                            colorPicker.setResourcesProvider(getResourceProvider());
                                            if (this.screenType == 1) {
                                                this.patternLayout[i12].addView(this.colorPicker, LayoutHelper.createFrame(-1, -1, 1));
                                                if (this.applyingTheme.isDark()) {
                                                    this.colorPicker.setMinBrightness(0.2f);
                                                } else {
                                                    this.colorPicker.setMinBrightness(0.05f);
                                                    this.colorPicker.setMaxBrightness(0.8f);
                                                }
                                                Theme.ThemeAccent themeAccent3 = this.accent;
                                                if (themeAccent3 != null) {
                                                    this.colorPicker.setType(1, hasChanges(1), 2, themeAccent3.accentColor2 != 0 ? 2 : 1, false, 0, false);
                                                    this.colorPicker.setColor(this.accent.accentColor, r8);
                                                    int i34 = this.accent.accentColor2;
                                                    if (i34 != 0) {
                                                        this.colorPicker.setColor(i34, 1);
                                                    }
                                                }
                                                i12++;
                                            } else {
                                                frameLayout = this.patternLayout[i12];
                                                view = this.colorPicker;
                                                f2 = 0.0f;
                                                f3 = 48.0f;
                                                i15 = -1;
                                                f4 = -1.0f;
                                                i16 = 1;
                                                f5 = 0.0f;
                                                f6 = 0.0f;
                                            }
                                        }
                                        frameLayout.addView(view, LayoutHelper.createFrame(i15, f4, i16, f5, f6, f2, f3));
                                        i12++;
                                    }
                                }
                                updateButtonState(r8, r8);
                                if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
                                    this.page2.setBackgroundColor(-16777216);
                                }
                                if (this.screenType != 1 && !(this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
                                    this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(true);
                                }
                            }
                            i7 = 0;
                            String[] strArr222 = new String[i7];
                            int[] iArr2222 = new int[i7];
                            this.backgroundCheckBoxView = new WallpaperCheckBoxView[i7];
                            if (i7 == 0) {
                            }
                            i10 = i8;
                            i11 = 0;
                            while (i11 < i10) {
                            }
                            if (this.screenType == 1) {
                            }
                            if (this.screenType != 1) {
                            }
                            this.isBlurred = r8;
                            i12 = 0;
                            while (i12 < i13) {
                            }
                            updateButtonState(r8, r8);
                            if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
                            }
                            if (this.screenType != 1) {
                                this.backgroundImage.getImageReceiver().setCrossfadeWithOldImage(true);
                            }
                        } else {
                            if (!(obj instanceof WallpapersListActivity.FileWallpaper) || !"t".equals(((WallpapersListActivity.FileWallpaper) obj).slug)) {
                                i7 = 2;
                                String[] strArr2222 = new String[i7];
                                int[] iArr22222 = new int[i7];
                                this.backgroundCheckBoxView = new WallpaperCheckBoxView[i7];
                                if (i7 == 0) {
                                }
                                i10 = i8;
                                i11 = 0;
                                while (i11 < i10) {
                                }
                                if (this.screenType == 1) {
                                }
                                if (this.screenType != 1) {
                                }
                                this.isBlurred = r8;
                                i12 = 0;
                                while (i12 < i13) {
                                }
                                updateButtonState(r8, r8);
                                if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
                                }
                                if (this.screenType != 1) {
                                }
                            }
                            i7 = 0;
                            String[] strArr22222 = new String[i7];
                            int[] iArr222222 = new int[i7];
                            this.backgroundCheckBoxView = new WallpaperCheckBoxView[i7];
                            if (i7 == 0) {
                            }
                            i10 = i8;
                            i11 = 0;
                            while (i11 < i10) {
                            }
                            if (this.screenType == 1) {
                            }
                            if (this.screenType != 1) {
                            }
                            this.isBlurred = r8;
                            i12 = 0;
                            while (i12 < i13) {
                            }
                            updateButtonState(r8, r8);
                            if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
                            }
                            if (this.screenType != 1) {
                            }
                        }
                    }
                    this.listView2.setAdapter(this.messagesAdapter);
                    FrameLayout frameLayout522 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.28
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
                                int[] iArr32 = this.loc;
                                iArr32[1] = iArr32[1] - AndroidUtilities.statusBarHeight;
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
                    this.frameLayout = frameLayout522;
                    frameLayout522.setWillNotDraw(r8);
                    FrameLayout frameLayout622 = this.frameLayout;
                    this.fragmentView = frameLayout622;
                    ViewTreeObserver viewTreeObserver22 = frameLayout622.getViewTreeObserver();
                    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener22 = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda17
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
                        public void onPageScrollStateChanged(int i252) {
                        }

                        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                        public void onPageScrolled(int i252, float f13, int i262) {
                        }

                        @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                        public void onPageSelected(int i252) {
                            ThemePreviewActivity.this.dotsContainer.invalidate();
                        }
                    });
                    this.viewPager.setAdapter(new PagerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.30
                        @Override // androidx.viewpager.widget.PagerAdapter
                        public void destroyItem(ViewGroup viewGroup, int i252, Object obj32) {
                            viewGroup.removeView((View) obj32);
                        }

                        @Override // androidx.viewpager.widget.PagerAdapter
                        public int getCount() {
                            return ThemePreviewActivity.this.screenType != 0 ? 1 : 2;
                        }

                        @Override // androidx.viewpager.widget.PagerAdapter
                        public int getItemPosition(Object obj32) {
                            return -1;
                        }

                        @Override // androidx.viewpager.widget.PagerAdapter
                        public Object instantiateItem(ViewGroup viewGroup, int i252) {
                            FrameLayout frameLayout72 = i252 == 0 ? ThemePreviewActivity.this.page2 : ThemePreviewActivity.this.page1;
                            viewGroup.addView(frameLayout72);
                            return frameLayout72;
                        }

                        @Override // androidx.viewpager.widget.PagerAdapter
                        public boolean isViewFromObject(View view2, Object obj32) {
                            return obj32 == view2;
                        }
                    });
                    AndroidUtilities.setViewPagerEdgeEffectColor(this.viewPager, getThemedColor(Theme.key_actionBarDefault));
                    this.frameLayout.addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.screenType == 0 ? 48.0f : 0.0f));
                    UndoView undoView22 = new UndoView(context, this);
                    this.undoView = undoView22;
                    undoView22.setAdditionalTranslationY(AndroidUtilities.dp(51.0f));
                    this.frameLayout.addView(this.undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
                    if (this.screenType == 0) {
                        View view2 = new View(context);
                        view2.setBackgroundColor(getThemedColor(Theme.key_dialogShadowLine));
                        FrameLayout.LayoutParams layoutParams4 = new FrameLayout.LayoutParams(-1, 1, 83);
                        layoutParams4.bottomMargin = AndroidUtilities.dp(48.0f);
                        this.frameLayout.addView(view2, layoutParams4);
                        FrameLayout frameLayout11 = new FrameLayout(context);
                        this.saveButtonsContainer = frameLayout11;
                        frameLayout11.setBackgroundColor(getButtonsColor(Theme.key_windowBackgroundWhite));
                        this.frameLayout.addView(this.saveButtonsContainer, LayoutHelper.createFrame(-1, 48, 83));
                        View view3 = new View(context) { // from class: org.telegram.ui.ThemePreviewActivity.31
                            private Paint paint = new Paint(1);

                            @Override // android.view.View
                            protected void onDraw(Canvas canvas) {
                                int currentItem = ThemePreviewActivity.this.viewPager.getCurrentItem();
                                this.paint.setColor(ThemePreviewActivity.this.getButtonsColor(Theme.key_chat_fieldOverlayText));
                                int i35 = 0;
                                while (i35 < 2) {
                                    this.paint.setAlpha(i35 == currentItem ? NotificationCenter.newLocationAvailable : NotificationCenter.dialogTranslate);
                                    canvas.drawCircle(AndroidUtilities.dp((i35 * 15) + 3), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(3.0f), this.paint);
                                    i35++;
                                }
                            }
                        };
                        this.dotsContainer = view3;
                        this.saveButtonsContainer.addView(view3, LayoutHelper.createFrame(22, 8, 17));
                        TextView textView6 = new TextView(context);
                        this.cancelButton = textView6;
                        textView6.setTextSize(1, 14.0f);
                        TextView textView7 = this.cancelButton;
                        int i35 = Theme.key_chat_fieldOverlayText;
                        textView7.setTextColor(getButtonsColor(i35));
                        this.cancelButton.setGravity(17);
                        this.cancelButton.setBackgroundDrawable(Theme.createSelectorDrawable(AndroidUtilities.LIGHT_STATUS_BAR_OVERLAY, r8));
                        this.cancelButton.setPadding(AndroidUtilities.dp(29.0f), r8, AndroidUtilities.dp(29.0f), r8);
                        this.cancelButton.setText(LocaleController.getString(R.string.Cancel).toUpperCase());
                        this.cancelButton.setTypeface(AndroidUtilities.bold());
                        this.saveButtonsContainer.addView(this.cancelButton, LayoutHelper.createFrame(-2, -1, 51));
                        this.cancelButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda19
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view4) {
                                ThemePreviewActivity.this.lambda$createView$17(view4);
                            }
                        });
                        TextView textView8 = new TextView(context);
                        this.doneButton = textView8;
                        textView8.setTextSize(1, 14.0f);
                        this.doneButton.setTextColor(getButtonsColor(i35));
                        this.doneButton.setGravity(17);
                        this.doneButton.setBackgroundDrawable(Theme.createSelectorDrawable(AndroidUtilities.LIGHT_STATUS_BAR_OVERLAY, r8));
                        this.doneButton.setPadding(AndroidUtilities.dp(29.0f), r8, AndroidUtilities.dp(29.0f), r8);
                        this.doneButton.setText(LocaleController.getString(R.string.ApplyTheme).toUpperCase());
                        this.doneButton.setTypeface(AndroidUtilities.bold());
                        this.saveButtonsContainer.addView(this.doneButton, LayoutHelper.createFrame(-2, -1, 53));
                        this.doneButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda20
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view4) {
                                ThemePreviewActivity.this.lambda$createView$18(view4);
                            }
                        });
                    }
                    if (this.screenType == 1 && !Theme.hasCustomWallpaper() && (themeAccent2 = this.accent) != null && themeAccent2.backgroundOverrideColor != 4294967296L) {
                        selectColorType(2);
                    }
                    this.themeDescriptions = getThemeDescriptionsInternal();
                    setCurrentImage(true);
                    updatePlayAnimationView(r8);
                    if (this.showColor) {
                        showPatternsView(r8, true, r8);
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
            } else {
                this.actionBar2.setTitle("Telegram Beta Chat");
                this.actionBar2.setSubtitle(LocaleController.formatPluralString("Members", 505, new Object[0]));
            }
            str2 = str;
            r8 = 0;
            f = 0.0f;
            this.listView2 = new RecyclerListView(context) { // from class: org.telegram.ui.ThemePreviewActivity.11
                float lastX;
                float lastY;
                boolean scrollingBackground;
                float startX;
                float startY;

                private void checkMotionEvent(MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 1) {
                        if (!ThemePreviewActivity.this.wasScroll && (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) && ThemePreviewActivity.this.patternLayout[0].getVisibility() == 0) {
                            ThemePreviewActivity.this.showPatternsView(0, false, true);
                        }
                        ThemePreviewActivity.this.wasScroll = false;
                    }
                }

                @Override // org.telegram.ui.Components.RecyclerListView
                protected boolean allowSelectChildAtPosition(View view22) {
                    RecyclerView.ViewHolder findContainingViewHolder = ThemePreviewActivity.this.listView2.findContainingViewHolder(view22);
                    if (findContainingViewHolder == null || findContainingViewHolder.getItemViewType() != 2) {
                        return super.allowSelectChildAtPosition(view22);
                    }
                    return false;
                }

                @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
                public boolean drawChild(Canvas canvas, View view22, long j) {
                    RecyclerView.ViewHolder childViewHolder;
                    boolean drawChild = super.drawChild(canvas, view22, j);
                    if (view22 instanceof ChatMessageCell) {
                        ChatMessageCell chatMessageCell = (ChatMessageCell) view22;
                        chatMessageCell.getMessageObject();
                        ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                        if (avatarImage != null) {
                            int top = view22.getTop();
                            if (chatMessageCell.isPinnedBottom() && (childViewHolder = ThemePreviewActivity.this.listView2.getChildViewHolder(view22)) != null) {
                                if (ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder.getAdapterPosition() - 1) != null) {
                                    avatarImage.setImageY(-AndroidUtilities.dp(1000.0f));
                                    avatarImage.draw(canvas);
                                    return drawChild;
                                }
                            }
                            float translationX = chatMessageCell.getTranslationX();
                            int top2 = view22.getTop() + chatMessageCell.getLayoutHeight();
                            int measuredHeight = ThemePreviewActivity.this.listView2.getMeasuredHeight() - ThemePreviewActivity.this.listView2.getPaddingBottom();
                            if (top2 > measuredHeight) {
                                top2 = measuredHeight;
                            }
                            if (chatMessageCell.isPinnedTop() && (childViewHolder2 = ThemePreviewActivity.this.listView2.getChildViewHolder(view22)) != null) {
                                int i252 = 0;
                                while (i252 < 20) {
                                    i252++;
                                    RecyclerView.ViewHolder childViewHolder2 = ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder2.getAdapterPosition() + 1);
                                    if (childViewHolder2 == null) {
                                        break;
                                    }
                                    top = childViewHolder2.itemView.getTop();
                                    if (top2 - AndroidUtilities.dp(48.0f) < childViewHolder2.itemView.getBottom()) {
                                        translationX = Math.min(childViewHolder2.itemView.getTranslationX(), translationX);
                                    }
                                    View view32 = childViewHolder2.itemView;
                                    if (!(view32 instanceof ChatMessageCell) || !((ChatMessageCell) view32).isPinnedTop()) {
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

                @Override // org.telegram.ui.Components.RecyclerListView
                protected void onChildPressed(View view22, float f13, float f14, boolean z6) {
                    if (z6 && (view22 instanceof ChatMessageCell) && !((ChatMessageCell) view22).isInsideBackground(f13, f14)) {
                        return;
                    }
                    super.onChildPressed(view22, f13, f14, z6);
                }

                @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
                protected void onLayout(boolean z6, int i252, int i262, int i272, int i282) {
                    super.onLayout(z6, i252, i262, i272, i282);
                    ThemePreviewActivity.this.invalidateBlur();
                }

                /* JADX WARN: Code restructure failed: missing block: B:17:0x004a, code lost:
                
                    if (getParent() != null) goto L17;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:6:0x0027, code lost:
                
                    if (getParent() != null) goto L17;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:7:0x0053, code lost:
                
                    r4.scrollingBackground = true;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:9:0x004c, code lost:
                
                    getParent().requestDisallowInterceptTouchEvent(true);
                 */
                @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public boolean onTouchEvent(MotionEvent motionEvent) {
                    checkMotionEvent(motionEvent);
                    if (ThemePreviewActivity.this.hasScrollingBackground) {
                        if (motionEvent.getAction() == 0) {
                            float x = motionEvent.getX();
                            this.startX = x;
                            this.lastX = x;
                            float y = motionEvent.getY();
                            this.startY = y;
                            this.lastY = y;
                        } else {
                            if (motionEvent.getAction() == 2) {
                                if (!this.scrollingBackground && Math.abs(this.startX - motionEvent.getX()) > AndroidUtilities.touchSlop) {
                                }
                            } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                                this.scrollingBackground = false;
                                if (getParent() != null) {
                                    getParent().requestDisallowInterceptTouchEvent(false);
                                }
                            }
                            ThemePreviewActivity.this.gestureDetector2.onTouchEvent(motionEvent);
                        }
                    }
                    return this.scrollingBackground || super.onTouchEvent(motionEvent);
                }

                @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
                public void setTranslationY(float f13) {
                    super.setTranslationY(f13);
                    if (ThemePreviewActivity.this.backgroundCheckBoxView != null) {
                        for (int i252 = 0; i252 < ThemePreviewActivity.this.backgroundCheckBoxView.length; i252++) {
                            ThemePreviewActivity.this.backgroundCheckBoxView[i252].invalidate();
                        }
                    }
                    if (ThemePreviewActivity.this.messagesCheckBoxView != null) {
                        for (int i262 = 0; i262 < ThemePreviewActivity.this.messagesCheckBoxView.length; i262++) {
                            ThemePreviewActivity.this.messagesCheckBoxView[i262].invalidate();
                        }
                    }
                    if (ThemePreviewActivity.this.backgroundPlayAnimationView != null) {
                        ThemePreviewActivity.this.backgroundPlayAnimationView.invalidate();
                    }
                    if (ThemePreviewActivity.this.messagesPlayAnimationView != null) {
                        ThemePreviewActivity.this.messagesPlayAnimationView.invalidate();
                    }
                }
            };
            DefaultItemAnimator defaultItemAnimator222 = new DefaultItemAnimator() { // from class: org.telegram.ui.ThemePreviewActivity.12
                @Override // androidx.recyclerview.widget.DefaultItemAnimator
                protected void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                    ThemePreviewActivity.this.listView2.invalidateViews();
                }
            };
            defaultItemAnimator222.setDelayAnimations(r8);
            this.listView2.setItemAnimator(defaultItemAnimator222);
            this.listView2.setVerticalScrollBarEnabled(true);
            this.listView2.setOverScrollMode(2);
            i4 = this.screenType;
            if (i4 == 2) {
            }
            this.listView2.setClipToPadding(r8);
            this.listView2.setLayoutManager(new LinearLayoutManager(context, 1, true));
            this.listView2.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
            if (this.screenType == 1) {
            }
            this.listView2.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ThemePreviewActivity.13
                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrollStateChanged(RecyclerView recyclerView, int i252) {
                    if (i252 == 0) {
                        ThemePreviewActivity.this.wasScroll = false;
                    }
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i252, int i262) {
                    ThemePreviewActivity.this.listView2.invalidateViews();
                    ThemePreviewActivity.this.wasScroll = true;
                }
            });
            this.page2.addView(this.actionBar2, LayoutHelper.createFrame(i5, -2.0f));
            WallpaperParallaxEffect wallpaperParallaxEffect222 = new WallpaperParallaxEffect(context);
            this.parallaxEffect = wallpaperParallaxEffect222;
            wallpaperParallaxEffect222.setCallback(new WallpaperParallaxEffect.Callback() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda8
                @Override // org.telegram.ui.Components.WallpaperParallaxEffect.Callback
                public final void onOffsetsChanged(int i252, int i262, float f13) {
                    ThemePreviewActivity.this.lambda$createView$7(i252, i262, f13);
                }
            });
            i6 = this.screenType;
            if (i6 != 1) {
            }
            if (i6 == 2) {
            }
            final Rect rect222 = new Rect();
            Drawable mutate3222 = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
            this.sheetDrawable = mutate3222;
            mutate3222.getPadding(rect222);
            this.sheetDrawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundWhite), PorterDuff.Mode.MULTIPLY));
            TextPaint textPaint222 = new TextPaint(1);
            textPaint222.setTextSize(AndroidUtilities.dp(14.0f));
            textPaint222.setTypeface(AndroidUtilities.bold());
            obj = this.currentWallpaper;
            if (this.screenType != 1) {
            }
            if (obj instanceof WallpapersListActivity.ColorWallpaper) {
            }
            i7 = 3;
            String[] strArr222222 = new String[i7];
            int[] iArr2222222 = new int[i7];
            this.backgroundCheckBoxView = new WallpaperCheckBoxView[i7];
            if (i7 == 0) {
            }
            i10 = i8;
            i11 = 0;
            while (i11 < i10) {
            }
            if (this.screenType == 1) {
            }
            if (this.screenType != 1) {
            }
            this.isBlurred = r8;
            i12 = 0;
            while (i12 < i13) {
            }
            updateButtonState(r8, r8);
            if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
            }
            if (this.screenType != 1) {
            }
            this.listView2.setAdapter(this.messagesAdapter);
            FrameLayout frameLayout5222 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.28
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
                        int[] iArr32 = this.loc;
                        iArr32[1] = iArr32[1] - AndroidUtilities.statusBarHeight;
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
            this.frameLayout = frameLayout5222;
            frameLayout5222.setWillNotDraw(r8);
            FrameLayout frameLayout6222 = this.frameLayout;
            this.fragmentView = frameLayout6222;
            ViewTreeObserver viewTreeObserver222 = frameLayout6222.getViewTreeObserver();
            ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener222 = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda17
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public final void onGlobalLayout() {
                    ThemePreviewActivity.this.lambda$createView$16();
                }
            };
            this.onGlobalLayoutListener = onGlobalLayoutListener222;
            viewTreeObserver222.addOnGlobalLayoutListener(onGlobalLayoutListener222);
            ViewPager viewPager222 = new ViewPager(context);
            this.viewPager = viewPager222;
            viewPager222.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: org.telegram.ui.ThemePreviewActivity.29
                @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                public void onPageScrollStateChanged(int i252) {
                }

                @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                public void onPageScrolled(int i252, float f13, int i262) {
                }

                @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
                public void onPageSelected(int i252) {
                    ThemePreviewActivity.this.dotsContainer.invalidate();
                }
            });
            this.viewPager.setAdapter(new PagerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.30
                @Override // androidx.viewpager.widget.PagerAdapter
                public void destroyItem(ViewGroup viewGroup, int i252, Object obj32) {
                    viewGroup.removeView((View) obj32);
                }

                @Override // androidx.viewpager.widget.PagerAdapter
                public int getCount() {
                    return ThemePreviewActivity.this.screenType != 0 ? 1 : 2;
                }

                @Override // androidx.viewpager.widget.PagerAdapter
                public int getItemPosition(Object obj32) {
                    return -1;
                }

                @Override // androidx.viewpager.widget.PagerAdapter
                public Object instantiateItem(ViewGroup viewGroup, int i252) {
                    FrameLayout frameLayout72 = i252 == 0 ? ThemePreviewActivity.this.page2 : ThemePreviewActivity.this.page1;
                    viewGroup.addView(frameLayout72);
                    return frameLayout72;
                }

                @Override // androidx.viewpager.widget.PagerAdapter
                public boolean isViewFromObject(View view22, Object obj32) {
                    return obj32 == view22;
                }
            });
            AndroidUtilities.setViewPagerEdgeEffectColor(this.viewPager, getThemedColor(Theme.key_actionBarDefault));
            this.frameLayout.addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.screenType == 0 ? 48.0f : 0.0f));
            UndoView undoView222 = new UndoView(context, this);
            this.undoView = undoView222;
            undoView222.setAdditionalTranslationY(AndroidUtilities.dp(51.0f));
            this.frameLayout.addView(this.undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
            if (this.screenType == 0) {
            }
            if (this.screenType == 1) {
            }
            this.themeDescriptions = getThemeDescriptionsInternal();
            setCurrentImage(true);
            updatePlayAnimationView(r8);
            if (this.showColor) {
            }
            this.scroller = new Scroller(getContext());
            iNavigationLayout = this.parentLayout;
            if (iNavigationLayout != null) {
            }
            return this.fragmentView;
        }
        z = false;
        this.shouldShowBrightnessControll = z;
        if (z) {
        }
        if (AndroidUtilities.isTablet()) {
        }
        this.page1 = new FrameLayout(context);
        if (this.shouldShowBrightnessControll) {
        }
        str = "";
        this.actionBar.createMenu().addItem(0, R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: org.telegram.ui.ThemePreviewActivity.5
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
        }).setSearchFieldHint(LocaleController.getString(R.string.Search));
        this.actionBar.setBackButtonDrawable(new MenuDrawable());
        this.actionBar.setAddToContainer(false);
        this.actionBar.setTitle(LocaleController.getString(R.string.ThemePreview));
        FrameLayout frameLayout32 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.6
            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view22, long j) {
                boolean drawChild = super.drawChild(canvas, view22, j);
                if (view22 == ((BaseFragment) ThemePreviewActivity.this).actionBar && ((BaseFragment) ThemePreviewActivity.this).parentLayout != null) {
                    ((BaseFragment) ThemePreviewActivity.this).parentLayout.drawHeaderShadow(canvas, ((BaseFragment) ThemePreviewActivity.this).actionBar.getVisibility() == 0 ? ((BaseFragment) ThemePreviewActivity.this).actionBar.getMeasuredHeight() : 0);
                }
                return drawChild;
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i212, int i222) {
                int size = View.MeasureSpec.getSize(i212);
                int size2 = View.MeasureSpec.getSize(i222);
                setMeasuredDimension(size, size2);
                measureChildWithMargins(((BaseFragment) ThemePreviewActivity.this).actionBar, i212, 0, i222, 0);
                int measuredHeight = ((BaseFragment) ThemePreviewActivity.this).actionBar.getMeasuredHeight();
                if (((BaseFragment) ThemePreviewActivity.this).actionBar.getVisibility() == 0) {
                    size2 -= measuredHeight;
                }
                ((FrameLayout.LayoutParams) ThemePreviewActivity.this.listView.getLayoutParams()).topMargin = measuredHeight;
                ThemePreviewActivity.this.listView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
                measureChildWithMargins(ThemePreviewActivity.this.floatingButton, i212, 0, i222, 0);
            }
        };
        this.page1 = frameLayout32;
        frameLayout32.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        this.page1.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        RecyclerListView recyclerListView5 = new RecyclerListView(context);
        this.listView = recyclerListView5;
        recyclerListView5.setVerticalScrollBarEnabled(true);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation(null);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollbarPosition(!LocaleController.isRTL ? 1 : 2);
        this.listView.setPadding(0, 0, 0, AndroidUtilities.dp(this.screenType == 0 ? 12.0f : 0.0f));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda18
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view22, int i212) {
                ThemePreviewActivity.lambda$createView$3(view22, i212);
            }
        });
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
        FrameLayout frameLayout42 = this.page1;
        ImageView imageView32 = this.floatingButton;
        if (i < 21) {
        }
        if (i < 21) {
        }
        boolean z52 = LocaleController.isRTL;
        frameLayout42.addView(imageView32, LayoutHelper.createFrame(i21, f12, (!z52 ? 3 : 5) | 80, !z52 ? 14.0f : 0.0f, 0.0f, !z52 ? 0.0f : 14.0f, 14.0f));
        DialogsAdapter dialogsAdapter2 = new DialogsAdapter(context);
        this.dialogsAdapter = dialogsAdapter2;
        this.listView.setAdapter(dialogsAdapter2);
        this.page2 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.8
            private boolean ignoreLayout;

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view22, long j) {
                boolean drawChild = super.drawChild(canvas, view22, j);
                if (view22 == ThemePreviewActivity.this.actionBar2 && ((BaseFragment) ThemePreviewActivity.this).parentLayout != null) {
                    ((BaseFragment) ThemePreviewActivity.this).parentLayout.drawHeaderShadow(canvas, ThemePreviewActivity.this.actionBar2.getVisibility() == 0 ? (int) (ThemePreviewActivity.this.actionBar2.getMeasuredHeight() + ThemePreviewActivity.this.actionBar2.getTranslationY()) : 0);
                }
                return drawChild;
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i222, int i232) {
                float f13;
                TextView textView9;
                float f14;
                int size = View.MeasureSpec.getSize(i222);
                int size2 = View.MeasureSpec.getSize(i232);
                setMeasuredDimension(size, size2);
                if (ThemePreviewActivity.this.dropDownContainer != null) {
                    this.ignoreLayout = true;
                    if (!AndroidUtilities.isTablet()) {
                        FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.dropDownContainer.getLayoutParams();
                        layoutParams5.topMargin = Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0;
                        ThemePreviewActivity.this.dropDownContainer.setLayoutParams(layoutParams5);
                    }
                    if (AndroidUtilities.isTablet() || ApplicationLoader.applicationContext.getResources().getConfiguration().orientation != 2) {
                        textView9 = ThemePreviewActivity.this.dropDown;
                        f14 = 20.0f;
                    } else {
                        textView9 = ThemePreviewActivity.this.dropDown;
                        f14 = 18.0f;
                    }
                    textView9.setTextSize(1, f14);
                    this.ignoreLayout = false;
                }
                measureChildWithMargins(ThemePreviewActivity.this.actionBar2, i222, 0, i232, 0);
                int measuredHeight = ThemePreviewActivity.this.actionBar2.getMeasuredHeight();
                if (ThemePreviewActivity.this.actionBar2.getVisibility() == 0) {
                    size2 -= measuredHeight;
                }
                FrameLayout.LayoutParams layoutParams22 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.listView2.getLayoutParams();
                layoutParams22.topMargin = measuredHeight;
                if (ThemePreviewActivity.this.screenType == 2) {
                    RecyclerListView recyclerListView22 = ThemePreviewActivity.this.listView2;
                    int dp33 = AndroidUtilities.dp(4.0f);
                    ThemePreviewActivity themePreviewActivity = ThemePreviewActivity.this;
                    recyclerListView22.setPadding(0, dp33, 0, (AndroidUtilities.dp(((themePreviewActivity.self || themePreviewActivity.dialogId <= 0) ? 0 : 58) + 72) - 12) + (ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0));
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
                    layoutParams32.height = AndroidUtilities.dp(72 + ((themePreviewActivity2.self || themePreviewActivity2.dialogId <= 0) ? 0 : 58)) + (ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0);
                    measureChildWithMargins(ThemePreviewActivity.this.bottomOverlayChat, i222, 0, i232, 0);
                }
                if (ThemePreviewActivity.this.sheetDrawable != null) {
                    ThemePreviewActivity.this.sheetDrawable.getPadding(AndroidUtilities.rectTmp2);
                }
                int i242 = 0;
                while (i242 < ThemePreviewActivity.this.patternLayout.length) {
                    if (ThemePreviewActivity.this.patternLayout[i242] != null) {
                        FrameLayout.LayoutParams layoutParams42 = (FrameLayout.LayoutParams) ThemePreviewActivity.this.patternLayout[i242].getLayoutParams();
                        if (i242 == 0) {
                            f13 = ThemePreviewActivity.this.screenType == 2 ? 321 : NotificationCenter.chatAvailableReactionsUpdated;
                        } else {
                            f13 = 316.0f;
                        }
                        layoutParams42.height = AndroidUtilities.dp(f13);
                        if (ThemePreviewActivity.this.insideBottomSheet()) {
                            layoutParams42.height += AndroidUtilities.navigationBarHeight;
                        }
                        if (i242 == 0) {
                            layoutParams42.height += AndroidUtilities.dp(12.0f) + AndroidUtilities.rectTmp2.top;
                        }
                        ThemePreviewActivity.this.patternLayout[i242].setPadding(0, i242 == 0 ? AndroidUtilities.dp(12.0f) + AndroidUtilities.rectTmp2.top : 0, 0, ThemePreviewActivity.this.insideBottomSheet() ? AndroidUtilities.navigationBarHeight : 0);
                        measureChildWithMargins(ThemePreviewActivity.this.patternLayout[i242], i222, 0, i232, 0);
                    }
                    i242++;
                }
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
        if (this.messagesAdapter.showSecretMessages) {
        }
        str2 = str;
        r8 = 0;
        f = 0.0f;
        this.listView2 = new RecyclerListView(context) { // from class: org.telegram.ui.ThemePreviewActivity.11
            float lastX;
            float lastY;
            boolean scrollingBackground;
            float startX;
            float startY;

            private void checkMotionEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    if (!ThemePreviewActivity.this.wasScroll && (ThemePreviewActivity.this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper) && ThemePreviewActivity.this.patternLayout[0].getVisibility() == 0) {
                        ThemePreviewActivity.this.showPatternsView(0, false, true);
                    }
                    ThemePreviewActivity.this.wasScroll = false;
                }
            }

            @Override // org.telegram.ui.Components.RecyclerListView
            protected boolean allowSelectChildAtPosition(View view22) {
                RecyclerView.ViewHolder findContainingViewHolder = ThemePreviewActivity.this.listView2.findContainingViewHolder(view22);
                if (findContainingViewHolder == null || findContainingViewHolder.getItemViewType() != 2) {
                    return super.allowSelectChildAtPosition(view22);
                }
                return false;
            }

            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view22, long j) {
                RecyclerView.ViewHolder childViewHolder;
                boolean drawChild = super.drawChild(canvas, view22, j);
                if (view22 instanceof ChatMessageCell) {
                    ChatMessageCell chatMessageCell = (ChatMessageCell) view22;
                    chatMessageCell.getMessageObject();
                    ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                    if (avatarImage != null) {
                        int top = view22.getTop();
                        if (chatMessageCell.isPinnedBottom() && (childViewHolder = ThemePreviewActivity.this.listView2.getChildViewHolder(view22)) != null) {
                            if (ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder.getAdapterPosition() - 1) != null) {
                                avatarImage.setImageY(-AndroidUtilities.dp(1000.0f));
                                avatarImage.draw(canvas);
                                return drawChild;
                            }
                        }
                        float translationX = chatMessageCell.getTranslationX();
                        int top2 = view22.getTop() + chatMessageCell.getLayoutHeight();
                        int measuredHeight = ThemePreviewActivity.this.listView2.getMeasuredHeight() - ThemePreviewActivity.this.listView2.getPaddingBottom();
                        if (top2 > measuredHeight) {
                            top2 = measuredHeight;
                        }
                        if (chatMessageCell.isPinnedTop() && (childViewHolder2 = ThemePreviewActivity.this.listView2.getChildViewHolder(view22)) != null) {
                            int i252 = 0;
                            while (i252 < 20) {
                                i252++;
                                RecyclerView.ViewHolder childViewHolder2 = ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(childViewHolder2.getAdapterPosition() + 1);
                                if (childViewHolder2 == null) {
                                    break;
                                }
                                top = childViewHolder2.itemView.getTop();
                                if (top2 - AndroidUtilities.dp(48.0f) < childViewHolder2.itemView.getBottom()) {
                                    translationX = Math.min(childViewHolder2.itemView.getTranslationX(), translationX);
                                }
                                View view32 = childViewHolder2.itemView;
                                if (!(view32 instanceof ChatMessageCell) || !((ChatMessageCell) view32).isPinnedTop()) {
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

            @Override // org.telegram.ui.Components.RecyclerListView
            protected void onChildPressed(View view22, float f13, float f14, boolean z6) {
                if (z6 && (view22 instanceof ChatMessageCell) && !((ChatMessageCell) view22).isInsideBackground(f13, f14)) {
                    return;
                }
                super.onChildPressed(view22, f13, f14, z6);
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z6, int i252, int i262, int i272, int i282) {
                super.onLayout(z6, i252, i262, i272, i282);
                ThemePreviewActivity.this.invalidateBlur();
            }

            /* JADX WARN: Code restructure failed: missing block: B:17:0x004a, code lost:
            
                if (getParent() != null) goto L17;
             */
            /* JADX WARN: Code restructure failed: missing block: B:6:0x0027, code lost:
            
                if (getParent() != null) goto L17;
             */
            /* JADX WARN: Code restructure failed: missing block: B:7:0x0053, code lost:
            
                r4.scrollingBackground = true;
             */
            /* JADX WARN: Code restructure failed: missing block: B:9:0x004c, code lost:
            
                getParent().requestDisallowInterceptTouchEvent(true);
             */
            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean onTouchEvent(MotionEvent motionEvent) {
                checkMotionEvent(motionEvent);
                if (ThemePreviewActivity.this.hasScrollingBackground) {
                    if (motionEvent.getAction() == 0) {
                        float x = motionEvent.getX();
                        this.startX = x;
                        this.lastX = x;
                        float y = motionEvent.getY();
                        this.startY = y;
                        this.lastY = y;
                    } else {
                        if (motionEvent.getAction() == 2) {
                            if (!this.scrollingBackground && Math.abs(this.startX - motionEvent.getX()) > AndroidUtilities.touchSlop) {
                            }
                        } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                            this.scrollingBackground = false;
                            if (getParent() != null) {
                                getParent().requestDisallowInterceptTouchEvent(false);
                            }
                        }
                        ThemePreviewActivity.this.gestureDetector2.onTouchEvent(motionEvent);
                    }
                }
                return this.scrollingBackground || super.onTouchEvent(motionEvent);
            }

            @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
            public void setTranslationY(float f13) {
                super.setTranslationY(f13);
                if (ThemePreviewActivity.this.backgroundCheckBoxView != null) {
                    for (int i252 = 0; i252 < ThemePreviewActivity.this.backgroundCheckBoxView.length; i252++) {
                        ThemePreviewActivity.this.backgroundCheckBoxView[i252].invalidate();
                    }
                }
                if (ThemePreviewActivity.this.messagesCheckBoxView != null) {
                    for (int i262 = 0; i262 < ThemePreviewActivity.this.messagesCheckBoxView.length; i262++) {
                        ThemePreviewActivity.this.messagesCheckBoxView[i262].invalidate();
                    }
                }
                if (ThemePreviewActivity.this.backgroundPlayAnimationView != null) {
                    ThemePreviewActivity.this.backgroundPlayAnimationView.invalidate();
                }
                if (ThemePreviewActivity.this.messagesPlayAnimationView != null) {
                    ThemePreviewActivity.this.messagesPlayAnimationView.invalidate();
                }
            }
        };
        DefaultItemAnimator defaultItemAnimator2222 = new DefaultItemAnimator() { // from class: org.telegram.ui.ThemePreviewActivity.12
            @Override // androidx.recyclerview.widget.DefaultItemAnimator
            protected void onMoveAnimationUpdate(RecyclerView.ViewHolder viewHolder) {
                ThemePreviewActivity.this.listView2.invalidateViews();
            }
        };
        defaultItemAnimator2222.setDelayAnimations(r8);
        this.listView2.setItemAnimator(defaultItemAnimator2222);
        this.listView2.setVerticalScrollBarEnabled(true);
        this.listView2.setOverScrollMode(2);
        i4 = this.screenType;
        if (i4 == 2) {
        }
        this.listView2.setClipToPadding(r8);
        this.listView2.setLayoutManager(new LinearLayoutManager(context, 1, true));
        this.listView2.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        if (this.screenType == 1) {
        }
        this.listView2.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.ThemePreviewActivity.13
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i252) {
                if (i252 == 0) {
                    ThemePreviewActivity.this.wasScroll = false;
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i252, int i262) {
                ThemePreviewActivity.this.listView2.invalidateViews();
                ThemePreviewActivity.this.wasScroll = true;
            }
        });
        this.page2.addView(this.actionBar2, LayoutHelper.createFrame(i5, -2.0f));
        WallpaperParallaxEffect wallpaperParallaxEffect2222 = new WallpaperParallaxEffect(context);
        this.parallaxEffect = wallpaperParallaxEffect2222;
        wallpaperParallaxEffect2222.setCallback(new WallpaperParallaxEffect.Callback() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda8
            @Override // org.telegram.ui.Components.WallpaperParallaxEffect.Callback
            public final void onOffsetsChanged(int i252, int i262, float f13) {
                ThemePreviewActivity.this.lambda$createView$7(i252, i262, f13);
            }
        });
        i6 = this.screenType;
        if (i6 != 1) {
        }
        if (i6 == 2) {
        }
        final Rect rect2222 = new Rect();
        Drawable mutate32222 = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
        this.sheetDrawable = mutate32222;
        mutate32222.getPadding(rect2222);
        this.sheetDrawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundWhite), PorterDuff.Mode.MULTIPLY));
        TextPaint textPaint2222 = new TextPaint(1);
        textPaint2222.setTextSize(AndroidUtilities.dp(14.0f));
        textPaint2222.setTypeface(AndroidUtilities.bold());
        obj = this.currentWallpaper;
        if (this.screenType != 1) {
        }
        if (obj instanceof WallpapersListActivity.ColorWallpaper) {
        }
        i7 = 3;
        String[] strArr2222222 = new String[i7];
        int[] iArr22222222 = new int[i7];
        this.backgroundCheckBoxView = new WallpaperCheckBoxView[i7];
        if (i7 == 0) {
        }
        i10 = i8;
        i11 = 0;
        while (i11 < i10) {
        }
        if (this.screenType == 1) {
        }
        if (this.screenType != 1) {
        }
        this.isBlurred = r8;
        i12 = 0;
        while (i12 < i13) {
        }
        updateButtonState(r8, r8);
        if (!this.backgroundImage.getImageReceiver().hasBitmapImage()) {
        }
        if (this.screenType != 1) {
        }
        this.listView2.setAdapter(this.messagesAdapter);
        FrameLayout frameLayout52222 = new FrameLayout(context) { // from class: org.telegram.ui.ThemePreviewActivity.28
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
                    int[] iArr32 = this.loc;
                    iArr32[1] = iArr32[1] - AndroidUtilities.statusBarHeight;
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
        this.frameLayout = frameLayout52222;
        frameLayout52222.setWillNotDraw(r8);
        FrameLayout frameLayout62222 = this.frameLayout;
        this.fragmentView = frameLayout62222;
        ViewTreeObserver viewTreeObserver2222 = frameLayout62222.getViewTreeObserver();
        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener2222 = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda17
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public final void onGlobalLayout() {
                ThemePreviewActivity.this.lambda$createView$16();
            }
        };
        this.onGlobalLayoutListener = onGlobalLayoutListener2222;
        viewTreeObserver2222.addOnGlobalLayoutListener(onGlobalLayoutListener2222);
        ViewPager viewPager2222 = new ViewPager(context);
        this.viewPager = viewPager2222;
        viewPager2222.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: org.telegram.ui.ThemePreviewActivity.29
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i252) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i252, float f13, int i262) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i252) {
                ThemePreviewActivity.this.dotsContainer.invalidate();
            }
        });
        this.viewPager.setAdapter(new PagerAdapter() { // from class: org.telegram.ui.ThemePreviewActivity.30
            @Override // androidx.viewpager.widget.PagerAdapter
            public void destroyItem(ViewGroup viewGroup, int i252, Object obj32) {
                viewGroup.removeView((View) obj32);
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getCount() {
                return ThemePreviewActivity.this.screenType != 0 ? 1 : 2;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getItemPosition(Object obj32) {
                return -1;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public Object instantiateItem(ViewGroup viewGroup, int i252) {
                FrameLayout frameLayout72 = i252 == 0 ? ThemePreviewActivity.this.page2 : ThemePreviewActivity.this.page1;
                viewGroup.addView(frameLayout72);
                return frameLayout72;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public boolean isViewFromObject(View view22, Object obj32) {
                return obj32 == view22;
            }
        });
        AndroidUtilities.setViewPagerEdgeEffectColor(this.viewPager, getThemedColor(Theme.key_actionBarDefault));
        this.frameLayout.addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.screenType == 0 ? 48.0f : 0.0f));
        UndoView undoView2222 = new UndoView(context, this);
        this.undoView = undoView2222;
        undoView2222.setAdditionalTranslationY(AndroidUtilities.dp(51.0f));
        this.frameLayout.addView(this.undoView, LayoutHelper.createFrame(-1, -2.0f, 83, 8.0f, 0.0f, 8.0f, 8.0f));
        if (this.screenType == 0) {
        }
        if (this.screenType == 1) {
        }
        this.themeDescriptions = getThemeDescriptionsInternal();
        setCurrentImage(true);
        updatePlayAnimationView(r8);
        if (this.showColor) {
        }
        this.scroller = new Scroller(getContext());
        iNavigationLayout = this.parentLayout;
        if (iNavigationLayout != null) {
        }
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TLRPC.TL_wallPaper tL_wallPaper;
        String str;
        if (i == NotificationCenter.chatWasBoostedByUser) {
            if (this.dialogId == ((Long) objArr[2]).longValue()) {
                this.boostsStatus = (TL_stories.TL_premium_boostsStatus) objArr[0];
                updateApplyButton1(true);
                return;
            }
            return;
        }
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
            return;
        }
        if (i == NotificationCenter.invalidateMotionBackground) {
            RecyclerListView recyclerListView2 = this.listView2;
            if (recyclerListView2 != null) {
                recyclerListView2.invalidateViews();
                return;
            }
            return;
        }
        if (i == NotificationCenter.didSetNewWallpapper) {
            if (this.page2 != null) {
                setCurrentImage(true);
                return;
            }
            return;
        }
        if (i == NotificationCenter.wallpapersNeedReload) {
            Object obj = this.currentWallpaper;
            if (obj instanceof WallpapersListActivity.FileWallpaper) {
                WallpapersListActivity.FileWallpaper fileWallpaper = (WallpapersListActivity.FileWallpaper) obj;
                if (fileWallpaper.slug == null) {
                    fileWallpaper.slug = (String) objArr[0];
                    return;
                }
                return;
            }
            return;
        }
        long j = 0;
        if (i != NotificationCenter.wallpapersDidLoad) {
            if (i != NotificationCenter.wallpaperSettedToUser || this.dialogId == 0) {
                return;
            }
            lambda$onBackPressed$321();
            return;
        }
        ArrayList arrayList = (ArrayList) objArr[0];
        this.patterns.clear();
        this.patternsDict.clear();
        int size = arrayList.size();
        boolean z = false;
        for (int i4 = 0; i4 < size; i4++) {
            TLRPC.WallPaper wallPaper = (TLRPC.WallPaper) arrayList.get(i4);
            if ((wallPaper instanceof TLRPC.TL_wallPaper) && wallPaper.pattern) {
                TLRPC.Document document = wallPaper.document;
                if (document != null && !this.patternsDict.containsKey(Long.valueOf(document.id))) {
                    this.patterns.add(wallPaper);
                    this.patternsDict.put(Long.valueOf(wallPaper.document.id), wallPaper);
                }
                Theme.ThemeAccent themeAccent = this.accent;
                if (themeAccent != null && (str = themeAccent.patternSlug) != null && str.equals(wallPaper.slug)) {
                    this.selectedPattern = (TLRPC.TL_wallPaper) wallPaper;
                    setCurrentImage(false);
                    updateButtonState(false, false);
                } else if (this.accent == null) {
                    TLRPC.TL_wallPaper tL_wallPaper2 = this.selectedPattern;
                    if (tL_wallPaper2 != null) {
                        String str2 = tL_wallPaper2.slug;
                        if (str2 != null) {
                            if (!str2.equals(wallPaper.slug)) {
                            }
                        }
                    }
                }
                z = true;
            }
        }
        if (!z && (tL_wallPaper = this.selectedPattern) != null) {
            this.patterns.add(0, tL_wallPaper);
        }
        PatternsAdapter patternsAdapter = this.patternsAdapter;
        if (patternsAdapter != null) {
            patternsAdapter.notifyDataSetChanged();
        }
        int size2 = arrayList.size();
        for (int i5 = 0; i5 < size2; i5++) {
            TLRPC.WallPaper wallPaper2 = (TLRPC.WallPaper) arrayList.get(i5);
            if (wallPaper2 instanceof TLRPC.TL_wallPaper) {
                j = MediaDataController.calcHash(j, wallPaper2.id);
            }
        }
        TLRPC.TL_account_getWallPapers tL_account_getWallPapers = new TLRPC.TL_account_getWallPapers();
        tL_account_getWallPapers.hash = j;
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_getWallPapers, new RequestDelegate() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda2
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                ThemePreviewActivity.this.lambda$didReceivedNotification$31(tLObject, tL_error);
            }
        }), this.classGuid);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Theme.ResourcesProvider getResourceProvider() {
        return this.themeDelegate;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        return this.shouldShowDayNightIcon ? getThemeDescriptionsInternal() : super.getThemeDescriptions();
    }

    public ArrayList getThemeDescriptionsInternal() {
        ThemeDescription.ThemeDescriptionDelegate themeDescriptionDelegate = new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda6
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                ThemePreviewActivity.this.lambda$getThemeDescriptionsInternal$33();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        };
        ArrayList arrayList = new ArrayList();
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
            ((ThemeDescription) arrayList.get(i17)).resourcesProvider = getResourceProvider();
        }
        return arrayList;
    }

    public boolean insideBottomSheet() {
        INavigationLayout iNavigationLayout = this.parentLayout;
        return (iNavigationLayout == null || iNavigationLayout.getBottomSheet() == null) ? false : true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        if (this.screenType != 2) {
            return false;
        }
        return !this.hasScrollingBackground || motionEvent == null || motionEvent.getY() <= ((float) (AndroidUtilities.statusBarHeight + ActionBar.getCurrentActionBarHeight()));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (!checkDiscard()) {
            return false;
        }
        cancelThemeApply(true);
        return true;
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

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
        updateButtonState(true, z);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatWasBoostedByUser);
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
        if (this.screenType == 0 && this.accent == null) {
            this.isMotion = Theme.isWallpaperMotion();
        } else {
            Point point = AndroidUtilities.displaySize;
            int min = Math.min(point.x, point.y);
            Point point2 = AndroidUtilities.displaySize;
            this.imageFilter = ((int) (min / AndroidUtilities.density)) + "_" + ((int) (Math.max(point2.x, point2.y) / AndroidUtilities.density)) + "_f";
            Point point3 = AndroidUtilities.displaySize;
            this.maxWallpaperSize = Math.max(point3.x, point3.y);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersNeedReload);
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.wallpapersDidLoad);
            this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
            if (this.patterns == null) {
                this.patterns = new ArrayList();
                MessagesStorage.getInstance(this.currentAccount).getWallpapers();
            }
        }
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatWasBoostedByUser);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.invalidateMotionBackground);
        getNotificationCenter().removeObserver(this, NotificationCenter.wallpaperSettedToUser);
        FrameLayout frameLayout = this.frameLayout;
        if (frameLayout != null && this.onGlobalLayoutListener != null) {
            frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
        }
        int i = this.screenType;
        if ((i == 2 || i == 1) && this.onSwitchDayNightDelegate == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    Theme.setChangingWallpaper(false);
                }
            });
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

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        if (this.isMotion) {
            this.parallaxEffect.setEnabled(false);
        }
        Theme.disallowChangeServiceMessageColor = false;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
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

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
        updateButtonState(false, true);
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

    public void setDelegate(WallpaperActivityDelegate wallpaperActivityDelegate) {
        this.delegate = wallpaperActivityDelegate;
    }

    public void setDialogId(long j) {
        this.dialogId = j;
        this.self = j == 0 || j == getUserConfig().getClientUserId();
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

    public void setInitialModes(boolean z, boolean z2, float f) {
        this.isBlurred = z;
        this.isMotion = z2;
        this.dimAmount = f;
    }

    public void setOnSwitchDayNightDelegate(DayNightSwitchDelegate dayNightSwitchDelegate) {
        this.onSwitchDayNightDelegate = dayNightSwitchDelegate;
    }

    public void setPatterns(ArrayList arrayList) {
        this.patterns = arrayList;
        if (this.screenType == 1 || (this.currentWallpaper instanceof WallpapersListActivity.ColorWallpaper)) {
            WallpapersListActivity.ColorWallpaper colorWallpaper = (WallpapersListActivity.ColorWallpaper) this.currentWallpaper;
            if (colorWallpaper.patternId != 0) {
                int size = arrayList.size();
                int i = 0;
                while (true) {
                    if (i >= size) {
                        break;
                    }
                    TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) this.patterns.get(i);
                    if (tL_wallPaper.id == colorWallpaper.patternId) {
                        this.selectedPattern = tL_wallPaper;
                        break;
                    }
                    i++;
                }
                this.currentIntensity = colorWallpaper.intensity;
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void setResourceProvider(Theme.ResourcesProvider resourcesProvider) {
        this.themeDelegate.parentProvider = resourcesProvider;
    }

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
        view.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda33
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                boolean lambda$toggleTheme$34;
                lambda$toggleTheme$34 = ThemePreviewActivity.lambda$toggleTheme$34(view2, motionEvent);
                return lambda$toggleTheme$34;
            }
        });
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ThemePreviewActivity$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                ThemePreviewActivity.this.lambda$toggleTheme$36();
            }
        });
    }
}
