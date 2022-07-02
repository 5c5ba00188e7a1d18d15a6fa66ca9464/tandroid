package org.telegram.ui.ActionBar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
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
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.StateSet;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesController$$ExternalSyntheticLambda219;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.time.SunDate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BaseTheme;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$TL_account_getMultiWallPapers;
import org.telegram.tgnet.TLRPC$TL_account_getTheme;
import org.telegram.tgnet.TLRPC$TL_account_getThemes;
import org.telegram.tgnet.TLRPC$TL_account_getWallPaper;
import org.telegram.tgnet.TLRPC$TL_account_themes;
import org.telegram.tgnet.TLRPC$TL_baseThemeArctic;
import org.telegram.tgnet.TLRPC$TL_baseThemeClassic;
import org.telegram.tgnet.TLRPC$TL_baseThemeDay;
import org.telegram.tgnet.TLRPC$TL_baseThemeNight;
import org.telegram.tgnet.TLRPC$TL_baseThemeTinted;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputTheme;
import org.telegram.tgnet.TLRPC$TL_inputWallPaperSlug;
import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.tgnet.TLRPC$TL_wallPaper;
import org.telegram.tgnet.TLRPC$TL_wallPaperNoFile;
import org.telegram.tgnet.TLRPC$Theme;
import org.telegram.tgnet.TLRPC$ThemeSettings;
import org.telegram.tgnet.TLRPC$Vector;
import org.telegram.tgnet.TLRPC$WallPaper;
import org.telegram.tgnet.TLRPC$WallPaperSettings;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AudioVisualizerDrawable;
import org.telegram.ui.Components.BackgroundGradientDrawable;
import org.telegram.ui.Components.ChoosingStickerStatusDrawable;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.FragmentContextViewWavesDrawable;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.MotionBackgroundDrawable;
import org.telegram.ui.Components.MsgClockDrawable;
import org.telegram.ui.Components.PathAnimator;
import org.telegram.ui.Components.PlayingGameDrawable;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RecordStatusDrawable;
import org.telegram.ui.Components.RoundStatusDrawable;
import org.telegram.ui.Components.ScamDrawable;
import org.telegram.ui.Components.SendingFileDrawable;
import org.telegram.ui.Components.StatusDrawable;
import org.telegram.ui.Components.ThemeEditorView;
import org.telegram.ui.Components.TypingDotsDrawable;
import org.telegram.ui.RoundVideoProgressShadow;
/* loaded from: classes3.dex */
public class Theme {
    private static Method StateListDrawable_getStateDrawableMethod = null;
    private static SensorEventListener ambientSensorListener = null;
    private static HashMap<MessageObject, AudioVisualizerDrawable> animatedOutVisualizerDrawables = null;
    private static HashMap<String, Integer> animatingColors = null;
    public static float autoNightBrighnessThreshold = 0.0f;
    public static String autoNightCityName = null;
    public static int autoNightDayEndTime = 0;
    public static int autoNightDayStartTime = 0;
    public static int autoNightLastSunCheckDay = 0;
    public static double autoNightLocationLatitude = 0.0d;
    public static double autoNightLocationLongitude = 0.0d;
    public static boolean autoNightScheduleByLocation = false;
    public static int autoNightSunriseTime = 0;
    public static int autoNightSunsetTime = 0;
    public static Paint avatar_backgroundPaint = null;
    private static BackgroundGradientDrawable.Disposable backgroundGradientDisposable = null;
    public static Drawable calllog_msgCallDownGreenDrawable = null;
    public static Drawable calllog_msgCallDownRedDrawable = null;
    public static Drawable calllog_msgCallUpGreenDrawable = null;
    public static Drawable calllog_msgCallUpRedDrawable = null;
    private static boolean canStartHolidayAnimation = false;
    private static boolean changingWallpaper = false;
    public static Paint chat_actionBackgroundGradientDarkenPaint = null;
    public static Paint chat_actionBackgroundPaint = null;
    public static Paint chat_actionBackgroundPaint2 = null;
    public static Paint chat_actionBackgroundSelectedPaint = null;
    public static TextPaint chat_actionTextPaint = null;
    public static TextPaint chat_adminPaint = null;
    public static Drawable chat_attachEmptyDrawable = null;
    public static TextPaint chat_audioPerformerPaint = null;
    public static TextPaint chat_audioTimePaint = null;
    public static TextPaint chat_audioTitlePaint = null;
    public static TextPaint chat_botButtonPaint = null;
    public static Drawable chat_botCardDrawable = null;
    public static Drawable chat_botInlineDrawable = null;
    public static Drawable chat_botInviteDrawable = null;
    public static Drawable chat_botLinkDrawable = null;
    public static Paint chat_botProgressPaint = null;
    public static Drawable chat_botWebViewDrawable = null;
    public static Drawable chat_commentArrowDrawable = null;
    public static Drawable chat_commentDrawable = null;
    public static Drawable chat_commentStickerDrawable = null;
    public static Paint chat_composeBackgroundPaint = null;
    public static Drawable chat_composeShadowDrawable = null;
    public static Drawable chat_composeShadowRoundDrawable = null;
    public static TextPaint chat_contactNamePaint = null;
    public static TextPaint chat_contactPhonePaint = null;
    public static TextPaint chat_contextResult_descriptionTextPaint = null;
    public static Drawable chat_contextResult_shadowUnderSwitchDrawable = null;
    public static TextPaint chat_contextResult_titleTextPaint = null;
    public static Paint chat_deleteProgressPaint = null;
    public static Paint chat_docBackPaint = null;
    public static TextPaint chat_docNamePaint = null;
    public static TextPaint chat_durationPaint = null;
    public static Drawable chat_flameIcon = null;
    public static TextPaint chat_forwardNamePaint = null;
    public static TextPaint chat_gamePaint = null;
    public static Drawable chat_gifIcon = null;
    public static Drawable chat_goIconDrawable = null;
    public static TextPaint chat_infoPaint = null;
    public static Drawable chat_inlineResultAudio = null;
    public static Drawable chat_inlineResultFile = null;
    public static Drawable chat_inlineResultLocation = null;
    public static TextPaint chat_instantViewPaint = null;
    public static Paint chat_instantViewRectPaint = null;
    public static TextPaint chat_livePaint = null;
    public static TextPaint chat_locationAddressPaint = null;
    public static TextPaint chat_locationTitlePaint = null;
    public static Drawable chat_lockIconDrawable = null;
    public static Paint chat_messageBackgroundSelectedPaint = null;
    private static AudioVisualizerDrawable chat_msgAudioVisualizeDrawable = null;
    public static Drawable chat_msgAvatarLiveLocationDrawable = null;
    public static TextPaint chat_msgBotButtonPaint = null;
    public static Drawable chat_msgCallDownGreenDrawable = null;
    public static Drawable chat_msgCallDownRedDrawable = null;
    public static Drawable chat_msgCallUpGreenDrawable = null;
    public static MsgClockDrawable chat_msgClockDrawable = null;
    public static Drawable chat_msgErrorDrawable = null;
    public static Paint chat_msgErrorPaint = null;
    public static TextPaint chat_msgGameTextPaint = null;
    public static MessageDrawable chat_msgInDrawable = null;
    public static Drawable chat_msgInInstantDrawable = null;
    public static MessageDrawable chat_msgInMediaDrawable = null;
    public static MessageDrawable chat_msgInMediaSelectedDrawable = null;
    public static Drawable chat_msgInMenuDrawable = null;
    public static Drawable chat_msgInMenuSelectedDrawable = null;
    public static Drawable chat_msgInPinnedDrawable = null;
    public static Drawable chat_msgInPinnedSelectedDrawable = null;
    public static Drawable chat_msgInRepliesDrawable = null;
    public static Drawable chat_msgInRepliesSelectedDrawable = null;
    public static MessageDrawable chat_msgInSelectedDrawable = null;
    public static Drawable chat_msgInViewsDrawable = null;
    public static Drawable chat_msgInViewsSelectedDrawable = null;
    public static Drawable chat_msgMediaCheckDrawable = null;
    public static Drawable chat_msgMediaHalfCheckDrawable = null;
    public static Drawable chat_msgMediaMenuDrawable = null;
    public static Drawable chat_msgMediaPinnedDrawable = null;
    public static Drawable chat_msgMediaRepliesDrawable = null;
    public static Drawable chat_msgMediaViewsDrawable = null;
    public static Drawable chat_msgNoSoundDrawable = null;
    public static Drawable chat_msgOutCheckDrawable = null;
    public static Drawable chat_msgOutCheckReadDrawable = null;
    public static Drawable chat_msgOutCheckReadSelectedDrawable = null;
    public static Drawable chat_msgOutCheckSelectedDrawable = null;
    public static MessageDrawable chat_msgOutDrawable = null;
    public static Drawable chat_msgOutHalfCheckDrawable = null;
    public static Drawable chat_msgOutHalfCheckSelectedDrawable = null;
    public static Drawable chat_msgOutInstantDrawable = null;
    public static MessageDrawable chat_msgOutMediaDrawable = null;
    public static MessageDrawable chat_msgOutMediaSelectedDrawable = null;
    public static Drawable chat_msgOutMenuDrawable = null;
    public static Drawable chat_msgOutMenuSelectedDrawable = null;
    public static Drawable chat_msgOutPinnedDrawable = null;
    public static Drawable chat_msgOutPinnedSelectedDrawable = null;
    public static Drawable chat_msgOutRepliesDrawable = null;
    public static Drawable chat_msgOutRepliesSelectedDrawable = null;
    public static MessageDrawable chat_msgOutSelectedDrawable = null;
    public static Drawable chat_msgOutViewsDrawable = null;
    public static Drawable chat_msgOutViewsSelectedDrawable = null;
    public static Drawable chat_msgStickerCheckDrawable = null;
    public static Drawable chat_msgStickerHalfCheckDrawable = null;
    public static Drawable chat_msgStickerPinnedDrawable = null;
    public static Drawable chat_msgStickerRepliesDrawable = null;
    public static Drawable chat_msgStickerViewsDrawable = null;
    public static TextPaint chat_msgTextPaint = null;
    public static TextPaint chat_msgTextPaintOneEmoji = null;
    public static TextPaint chat_msgTextPaintThreeEmoji = null;
    public static TextPaint chat_msgTextPaintTwoEmoji = null;
    public static Drawable chat_muteIconDrawable = null;
    public static TextPaint chat_namePaint = null;
    public static Paint chat_outUrlPaint = null;
    public static Paint chat_pollTimerPaint = null;
    public static Paint chat_radialProgress2Paint = null;
    public static Paint chat_radialProgressPaint = null;
    public static Paint chat_radialProgressPausedSeekbarPaint = null;
    public static Drawable chat_redLocationIcon = null;
    public static Drawable chat_replyIconDrawable = null;
    public static Paint chat_replyLinePaint = null;
    public static TextPaint chat_replyNamePaint = null;
    public static TextPaint chat_replyTextPaint = null;
    public static Drawable chat_roundVideoShadow = null;
    public static Drawable chat_shareIconDrawable = null;
    public static TextPaint chat_shipmentPaint = null;
    public static Paint chat_statusPaint = null;
    public static Paint chat_statusRecordPaint = null;
    public static TextPaint chat_stickerCommentCountPaint = null;
    public static Paint chat_textSearchSelectionPaint = null;
    public static Paint chat_timeBackgroundPaint = null;
    public static TextPaint chat_timePaint = null;
    public static Paint chat_urlPaint = null;
    public static Paint checkboxSquare_backgroundPaint = null;
    public static Paint checkboxSquare_checkPaint = null;
    public static Paint checkboxSquare_eraserPaint = null;
    private static ThemeInfo currentDayTheme = null;
    private static ThemeInfo currentNightTheme = null;
    private static ThemeInfo currentTheme = null;
    private static ThemeInfo defaultTheme = null;
    public static Paint dialogs_actionMessagePaint = null;
    public static RLottieDrawable dialogs_archiveAvatarDrawable = null;
    public static boolean dialogs_archiveAvatarDrawableRecolored = false;
    public static RLottieDrawable dialogs_archiveDrawable = null;
    public static boolean dialogs_archiveDrawableRecolored = false;
    public static TextPaint dialogs_archiveTextPaint = null;
    public static TextPaint dialogs_archiveTextPaintSmall = null;
    public static Drawable dialogs_checkDrawable = null;
    public static Drawable dialogs_checkReadDrawable = null;
    public static Drawable dialogs_clockDrawable = null;
    public static Paint dialogs_countGrayPaint = null;
    public static Paint dialogs_countPaint = null;
    public static TextPaint dialogs_countTextPaint = null;
    public static Drawable dialogs_errorDrawable = null;
    public static Paint dialogs_errorPaint = null;
    public static ScamDrawable dialogs_fakeDrawable = null;
    public static Drawable dialogs_halfCheckDrawable = null;
    public static RLottieDrawable dialogs_hidePsaDrawable = null;
    public static boolean dialogs_hidePsaDrawableRecolored = false;
    public static Drawable dialogs_holidayDrawable = null;
    private static int dialogs_holidayDrawableOffsetX = 0;
    private static int dialogs_holidayDrawableOffsetY = 0;
    public static Drawable dialogs_lockDrawable = null;
    public static Drawable dialogs_mentionDrawable = null;
    public static TextPaint dialogs_messageNamePaint = null;
    public static TextPaint[] dialogs_messagePaint = null;
    public static TextPaint[] dialogs_messagePrintingPaint = null;
    public static Drawable dialogs_muteDrawable = null;
    public static TextPaint[] dialogs_nameEncryptedPaint = null;
    public static TextPaint[] dialogs_namePaint = null;
    public static TextPaint dialogs_offlinePaint = null;
    public static Paint dialogs_onlineCirclePaint = null;
    public static TextPaint dialogs_onlinePaint = null;
    public static RLottieDrawable dialogs_pinArchiveDrawable = null;
    public static Drawable dialogs_pinnedDrawable = null;
    public static Paint dialogs_pinnedPaint = null;
    public static Drawable dialogs_playDrawable = null;
    public static Paint dialogs_reactionsCountPaint = null;
    public static Drawable dialogs_reactionsMentionDrawable = null;
    public static Drawable dialogs_reorderDrawable = null;
    public static ScamDrawable dialogs_scamDrawable = null;
    public static TextPaint dialogs_searchNameEncryptedPaint = null;
    public static TextPaint dialogs_searchNamePaint = null;
    public static RLottieDrawable dialogs_swipeDeleteDrawable = null;
    public static RLottieDrawable dialogs_swipeMuteDrawable = null;
    public static RLottieDrawable dialogs_swipePinDrawable = null;
    public static RLottieDrawable dialogs_swipeReadDrawable = null;
    public static RLottieDrawable dialogs_swipeUnmuteDrawable = null;
    public static RLottieDrawable dialogs_swipeUnpinDrawable = null;
    public static RLottieDrawable dialogs_swipeUnreadDrawable = null;
    public static Paint dialogs_tabletSeletedPaint = null;
    public static TextPaint dialogs_timePaint = null;
    public static RLottieDrawable dialogs_unarchiveDrawable = null;
    public static RLottieDrawable dialogs_unpinArchiveDrawable = null;
    public static Drawable dialogs_verifiedCheckDrawable = null;
    public static Drawable dialogs_verifiedDrawable = null;
    public static Paint dividerExtraPaint = null;
    public static Paint dividerPaint = null;
    private static FragmentContextViewWavesDrawable fragmentContextViewWavesDrawable = null;
    private static boolean hasPreviousTheme = false;
    private static boolean isApplyingAccent = false;
    private static boolean isCustomTheme = false;
    private static boolean isInNigthMode = false;
    private static boolean isPatternWallpaper = false;
    private static boolean isWallpaperMotion = false;
    private static float lastBrightnessValue = 1.0f;
    private static long lastDelayUpdateTime;
    private static long lastHolidayCheckTime;
    private static int lastLoadingCurrentThemeTime;
    private static long lastThemeSwitchTime;
    private static Sensor lightSensor;
    private static boolean lightSensorRegistered;
    public static Paint linkSelectionPaint;
    private static int loadingCurrentTheme;
    public static Drawable moveUpDrawable;
    public static PathAnimator playPauseAnimator;
    private static int previousPhase;
    private static ThemeInfo previousTheme;
    public static TextPaint profile_aboutTextPaint;
    public static Drawable profile_verifiedCheckDrawable;
    public static Drawable profile_verifiedDrawable;
    private static RoundVideoProgressShadow roundPlayDrawable;
    public static int selectedAutoNightType;
    private static SensorManager sensorManager;
    private static Bitmap serviceBitmap;
    private static Matrix serviceBitmapMatrix;
    public static BitmapShader serviceBitmapShader;
    private static int serviceMessage2Color;
    private static int serviceMessageColor;
    public static int serviceMessageColorBackup;
    private static int serviceSelectedMessage2Color;
    private static int serviceSelectedMessageColor;
    public static int serviceSelectedMessageColorBackup;
    private static boolean shouldDrawGradientIcons;
    private static boolean switchDayRunnableScheduled;
    private static boolean switchNightRunnableScheduled;
    private static int switchNightThemeDelay;
    private static boolean switchingNightTheme;
    private static Drawable themedWallpaper;
    private static int themedWallpaperFileOffset;
    private static String themedWallpaperLink;
    private static float[] tmpHSV5;
    private static int[] viewPos;
    private static Drawable wallpaper;
    public static Runnable wallpaperLoadTask;
    private static final Object sync = new Object();
    private static Runnable switchDayBrightnessRunnable = new Runnable() { // from class: org.telegram.ui.ActionBar.Theme.1
        @Override // java.lang.Runnable
        public void run() {
            boolean unused = Theme.switchDayRunnableScheduled = false;
            Theme.applyDayNightThemeMaybe(false);
        }
    };
    private static Runnable switchNightBrightnessRunnable = new Runnable() { // from class: org.telegram.ui.ActionBar.Theme.2
        @Override // java.lang.Runnable
        public void run() {
            boolean unused = Theme.switchNightRunnableScheduled = false;
            Theme.applyDayNightThemeMaybe(true);
        }
    };
    public static int DEFALT_THEME_ACCENT_ID = 99;
    private static Paint maskPaint = new Paint(1);
    private static boolean[] loadingRemoteThemes = new boolean[4];
    private static int[] lastLoadingThemesTime = new int[4];
    private static long[] remoteThemesHash = new long[4];
    public static Drawable[] avatarDrawables = new Drawable[12];
    private static StatusDrawable[] chat_status_drawables = new StatusDrawable[6];
    public static Drawable[] chat_msgInCallDrawable = new Drawable[2];
    public static Drawable[] chat_msgInCallSelectedDrawable = new Drawable[2];
    public static Drawable[] chat_msgOutCallDrawable = new Drawable[2];
    public static Drawable[] chat_msgOutCallSelectedDrawable = new Drawable[2];
    public static Drawable[] chat_pollCheckDrawable = new Drawable[2];
    public static Drawable[] chat_pollCrossDrawable = new Drawable[2];
    public static Drawable[] chat_pollHintDrawable = new Drawable[2];
    public static Drawable[] chat_psaHelpDrawable = new Drawable[2];
    public static RLottieDrawable[] chat_attachButtonDrawables = new RLottieDrawable[6];
    public static Drawable[] chat_locationDrawable = new Drawable[2];
    public static Drawable[] chat_contactDrawable = new Drawable[2];
    public static Drawable[][] chat_fileStatesDrawable = (Drawable[][]) Array.newInstance(Drawable.class, 10, 2);
    public static Drawable[][] chat_photoStatesDrawables = (Drawable[][]) Array.newInstance(Drawable.class, 13, 2);
    public static Path[] chat_filePath = new Path[2];
    public static Path[] chat_updatePath = new Path[3];
    public static String[] keys_avatar_background = {"avatar_backgroundRed", "avatar_backgroundOrange", "avatar_backgroundViolet", "avatar_backgroundGreen", "avatar_backgroundCyan", "avatar_backgroundBlue", "avatar_backgroundPink"};
    public static String[] keys_avatar_nameInMessage = {"avatar_nameInMessageRed", "avatar_nameInMessageOrange", "avatar_nameInMessageViolet", "avatar_nameInMessageGreen", "avatar_nameInMessageCyan", "avatar_nameInMessageBlue", "avatar_nameInMessagePink"};
    private static final HashMap<String, Drawable> defaultChatDrawables = new HashMap<>();
    private static final HashMap<String, String> defaultChatDrawableColorKeys = new HashMap<>();
    private static final HashMap<String, Paint> defaultChatPaints = new HashMap<>();
    private static final HashMap<String, String> defaultChatPaintColors = new HashMap<>();
    private static HashSet<String> myMessagesColorKeys = new HashSet<>();
    private static HashSet<String> myMessagesBubblesColorKeys = new HashSet<>();
    private static HashMap<String, Integer> defaultColors = new HashMap<>();
    private static HashMap<String, String> fallbackKeys = new HashMap<>();
    private static HashSet<String> themeAccentExclusionKeys = new HashSet<>();
    private static ThreadLocal<float[]> hsvTemp1Local = new ThreadLocal<>();
    private static ThreadLocal<float[]> hsvTemp2Local = new ThreadLocal<>();
    private static ThreadLocal<float[]> hsvTemp3Local = new ThreadLocal<>();
    private static ThreadLocal<float[]> hsvTemp4Local = new ThreadLocal<>();
    private static ThreadLocal<float[]> hsvTemp5Local = new ThreadLocal<>();
    public static ArrayList<ThemeInfo> themes = new ArrayList<>();
    private static ArrayList<ThemeInfo> otherThemes = new ArrayList<>();
    private static HashMap<String, ThemeInfo> themesDict = new HashMap<>();
    private static HashMap<String, Integer> currentColorsNoAccent = new HashMap<>();
    private static HashMap<String, Integer> currentColors = new HashMap<>();

    /* loaded from: classes3.dex */
    public static class BackgroundDrawableSettings {
        public Boolean isCustomTheme;
        public Boolean isPatternWallpaper;
        public Boolean isWallpaperMotion;
        public Drawable wallpaper;
    }

    private static float abs(float f) {
        return f > 0.0f ? f : -f;
    }

    public static void destroyResources() {
    }

    public static int getWallpaperColor(int i) {
        if (i == 0) {
            return 0;
        }
        return i | (-16777216);
    }

    /* loaded from: classes3.dex */
    public static class MessageDrawable extends Drawable {
        public static MotionBackgroundDrawable[] motionBackground = new MotionBackgroundDrawable[3];
        private int alpha;
        private Drawable[][] backgroundDrawable;
        private int[][] backgroundDrawableColor;
        private Rect backupRect;
        private Bitmap crosfadeFromBitmap;
        private Shader crosfadeFromBitmapShader;
        public MessageDrawable crossfadeFromDrawable;
        public float crossfadeProgress;
        private boolean currentAnimateGradient;
        private int[][] currentBackgroundDrawableRadius;
        private int currentBackgroundHeight;
        private int currentColor;
        private int currentGradientColor1;
        private int currentGradientColor2;
        private int currentGradientColor3;
        private int[] currentShadowDrawableRadius;
        private int currentType;
        private boolean drawFullBubble;
        private Shader gradientShader;
        private boolean isBottomNear;
        public boolean isCrossfadeBackground;
        private final boolean isOut;
        public boolean isSelected;
        private boolean isTopNear;
        public boolean lastDrawWithShadow;
        private Matrix matrix;
        private int overrideRoundRadius;
        private Paint paint;
        private Path path;
        PathDrawParams pathDrawCacheParams;
        private RectF rect;
        private final ResourcesProvider resourcesProvider;
        private Paint selectedPaint;
        private Drawable[] shadowDrawable;
        private int[] shadowDrawableColor;
        public boolean themePreview;
        private int topY;
        Drawable transitionDrawable;
        int transitionDrawableColor;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(int i, PorterDuff.Mode mode) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public MessageDrawable(int i, boolean z, boolean z2) {
            this(i, z, z2, null);
        }

        public MessageDrawable(int i, boolean z, boolean z2, ResourcesProvider resourcesProvider) {
            this.paint = new Paint(1);
            this.rect = new RectF();
            this.matrix = new Matrix();
            this.backupRect = new Rect();
            this.currentShadowDrawableRadius = new int[]{-1, -1, -1, -1};
            this.shadowDrawable = new Drawable[4];
            this.shadowDrawableColor = new int[]{-1, -1, -1, -1};
            this.currentBackgroundDrawableRadius = new int[][]{new int[]{-1, -1, -1, -1}, new int[]{-1, -1, -1, -1}};
            this.backgroundDrawable = (Drawable[][]) Array.newInstance(Drawable.class, 2, 4);
            this.backgroundDrawableColor = new int[][]{new int[]{-1, -1, -1, -1}, new int[]{-1, -1, -1, -1}};
            this.resourcesProvider = resourcesProvider;
            this.isOut = z;
            this.currentType = i;
            this.isSelected = z2;
            this.path = new Path();
            this.selectedPaint = new Paint(1);
            this.alpha = 255;
        }

        public boolean hasGradient() {
            return this.gradientShader != null && Theme.shouldDrawGradientIcons;
        }

        public void applyMatrixScale() {
            Bitmap bitmap;
            if (this.gradientShader instanceof BitmapShader) {
                char c = 1;
                char c2 = 2;
                if (this.isCrossfadeBackground && (bitmap = this.crosfadeFromBitmap) != null) {
                    if (this.currentType != 2) {
                        c = 0;
                    }
                    float min = 1.0f / Math.min(bitmap.getWidth() / motionBackground[c].getBounds().width(), this.crosfadeFromBitmap.getHeight() / motionBackground[c].getBounds().height());
                    this.matrix.postScale(min, min);
                    return;
                }
                if (!this.themePreview) {
                    if (this.currentType != 2) {
                        c = 0;
                    }
                    c2 = c;
                }
                Bitmap bitmap2 = motionBackground[c2].getBitmap();
                float min2 = 1.0f / Math.min(bitmap2.getWidth() / motionBackground[c2].getBounds().width(), bitmap2.getHeight() / motionBackground[c2].getBounds().height());
                this.matrix.postScale(min2, min2);
            }
        }

        public Shader getGradientShader() {
            return this.gradientShader;
        }

        public Matrix getMatrix() {
            return this.matrix;
        }

        protected int getColor(String str) {
            if (this.currentType == 2) {
                return Theme.getColor(str);
            }
            ResourcesProvider resourcesProvider = this.resourcesProvider;
            Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
            return color != null ? color.intValue() : Theme.getColor(str);
        }

        protected Integer getCurrentColor(String str) {
            if (this.currentType == 2) {
                return Integer.valueOf(Theme.getColor(str));
            }
            ResourcesProvider resourcesProvider = this.resourcesProvider;
            return resourcesProvider != null ? resourcesProvider.getCurrentColor(str) : (Integer) Theme.currentColors.get(str);
        }

        public void setTop(int i, int i2, int i3, boolean z, boolean z2) {
            setTop(i, i2, i3, i3, 0, 0, z, z2);
        }

        /* JADX WARN: Removed duplicated region for block: B:111:0x029b  */
        /* JADX WARN: Removed duplicated region for block: B:117:0x02b2  */
        /* JADX WARN: Removed duplicated region for block: B:118:0x02bf  */
        /* JADX WARN: Removed duplicated region for block: B:121:0x02c8  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0066  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x006c  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x0072  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x0078  */
        /* JADX WARN: Removed duplicated region for block: B:32:0x0081  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0083  */
        /* JADX WARN: Removed duplicated region for block: B:44:0x009d  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void setTop(int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2) {
            boolean z3;
            Integer num;
            Integer num2;
            Integer num3;
            int i7;
            char c;
            int i8;
            MotionBackgroundDrawable[] motionBackgroundDrawableArr;
            MessageDrawable messageDrawable = this.crossfadeFromDrawable;
            if (messageDrawable != null) {
                messageDrawable.setTop(i, i2, i3, i4, i5, i6, z, z2);
            }
            if (this.isOut) {
                i7 = getColor(this.isSelected ? "chat_outBubbleSelected" : "chat_outBubble");
                num3 = getCurrentColor("chat_outBubbleGradient");
                num2 = getCurrentColor("chat_outBubbleGradient2");
                num = getCurrentColor("chat_outBubbleGradient3");
                Integer currentColor = getCurrentColor("chat_outBubbleGradientAnimated");
                if (currentColor != null && currentColor.intValue() != 0) {
                    z3 = true;
                    if (num3 != null) {
                        i7 = getColor("chat_outBubble");
                    }
                    if (num3 == null) {
                        num3 = 0;
                    }
                    if (num2 == null) {
                        num2 = 0;
                    }
                    if (num == null) {
                        num = 0;
                    }
                    if (!this.themePreview) {
                        c = 2;
                    } else {
                        c = this.currentType == 2 ? (char) 1 : (char) 0;
                    }
                    if (!this.isCrossfadeBackground && num2.intValue() != 0 && z3) {
                        motionBackgroundDrawableArr = motionBackground;
                        if (motionBackgroundDrawableArr[c] != null) {
                            int[] colors = motionBackgroundDrawableArr[c].getColors();
                            this.currentColor = colors[0];
                            this.currentGradientColor1 = colors[1];
                            this.currentGradientColor2 = colors[2];
                            this.currentGradientColor3 = colors[3];
                        }
                    }
                    if (!this.isCrossfadeBackground && num2.intValue() != 0 && z3) {
                        if (i3 != this.currentBackgroundHeight || this.crosfadeFromBitmapShader == null || this.currentColor != i7 || this.currentGradientColor1 != num3.intValue() || this.currentGradientColor2 != num2.intValue() || this.currentGradientColor3 != num.intValue() || this.currentAnimateGradient != z3) {
                            if (this.crosfadeFromBitmap == null) {
                                this.crosfadeFromBitmap = Bitmap.createBitmap(60, 80, Bitmap.Config.ARGB_8888);
                                Bitmap bitmap = this.crosfadeFromBitmap;
                                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                                this.crosfadeFromBitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
                            }
                            MotionBackgroundDrawable[] motionBackgroundDrawableArr2 = motionBackground;
                            if (motionBackgroundDrawableArr2[c] == null) {
                                motionBackgroundDrawableArr2[c] = new MotionBackgroundDrawable();
                                if (this.currentType != 2) {
                                    motionBackground[c].setPostInvalidateParent(true);
                                }
                                motionBackground[c].setRoundRadius(AndroidUtilities.dp(1.0f));
                            }
                            motionBackground[c].setColors(i7, num3.intValue(), num2.intValue(), num.intValue(), this.crosfadeFromBitmap);
                            this.crosfadeFromBitmapShader.setLocalMatrix(this.matrix);
                        }
                        Shader shader = this.crosfadeFromBitmapShader;
                        this.gradientShader = shader;
                        this.paint.setShader(shader);
                        this.paint.setColor(-1);
                        this.currentColor = i7;
                        this.currentAnimateGradient = z3;
                        this.currentGradientColor1 = num3.intValue();
                        this.currentGradientColor2 = num2.intValue();
                        this.currentGradientColor3 = num.intValue();
                    } else if (num3.intValue() == 0 && (this.gradientShader == null || i3 != this.currentBackgroundHeight || this.currentColor != i7 || this.currentGradientColor1 != num3.intValue() || this.currentGradientColor2 != num2.intValue() || this.currentGradientColor3 != num.intValue() || this.currentAnimateGradient != z3)) {
                        if (num2.intValue() != 0 && z3) {
                            MotionBackgroundDrawable[] motionBackgroundDrawableArr3 = motionBackground;
                            if (motionBackgroundDrawableArr3[c] == null) {
                                motionBackgroundDrawableArr3[c] = new MotionBackgroundDrawable();
                                if (this.currentType != 2) {
                                    motionBackground[c].setPostInvalidateParent(true);
                                }
                                motionBackground[c].setRoundRadius(AndroidUtilities.dp(1.0f));
                            }
                            motionBackground[c].setColors(i7, num3.intValue(), num2.intValue(), num.intValue());
                            this.gradientShader = motionBackground[c].getBitmapShader();
                        } else if (num2.intValue() != 0) {
                            if (num.intValue() != 0) {
                                this.gradientShader = new LinearGradient(0.0f, i5, 0.0f, i3, new int[]{num.intValue(), num2.intValue(), num3.intValue(), i7}, (float[]) null, Shader.TileMode.CLAMP);
                            } else {
                                this.gradientShader = new LinearGradient(0.0f, i5, 0.0f, i3, new int[]{num2.intValue(), num3.intValue(), i7}, (float[]) null, Shader.TileMode.CLAMP);
                            }
                        } else {
                            this.gradientShader = new LinearGradient(0.0f, i5, 0.0f, i3, new int[]{num3.intValue(), i7}, (float[]) null, Shader.TileMode.CLAMP);
                        }
                        this.paint.setShader(this.gradientShader);
                        this.currentColor = i7;
                        this.currentAnimateGradient = z3;
                        this.currentGradientColor1 = num3.intValue();
                        this.currentGradientColor2 = num2.intValue();
                        this.currentGradientColor3 = num.intValue();
                        this.paint.setColor(-1);
                    } else if (num3.intValue() == 0) {
                        if (this.gradientShader != null) {
                            this.gradientShader = null;
                            this.paint.setShader(null);
                        }
                        this.paint.setColor(i7);
                    }
                    if (!(this.gradientShader instanceof BitmapShader)) {
                        i8 = 0;
                        motionBackground[c].setBounds(0, i5, i2, i3 - i4);
                    } else {
                        i8 = 0;
                    }
                    this.currentBackgroundHeight = i3;
                    if (this.gradientShader instanceof BitmapShader) {
                        i8 = i4;
                    }
                    this.topY = i - i8;
                    this.isTopNear = z;
                    this.isBottomNear = z2;
                }
            } else {
                i7 = getColor(this.isSelected ? "chat_inBubbleSelected" : "chat_inBubble");
                num3 = null;
                num2 = null;
                num = null;
            }
            z3 = false;
            if (num3 != null) {
            }
            if (num3 == null) {
            }
            if (num2 == null) {
            }
            if (num == null) {
            }
            if (!this.themePreview) {
            }
            if (!this.isCrossfadeBackground) {
                motionBackgroundDrawableArr = motionBackground;
                if (motionBackgroundDrawableArr[c] != null) {
                }
            }
            if (!this.isCrossfadeBackground) {
            }
            if (num3.intValue() == 0) {
            }
            if (num3.intValue() == 0) {
            }
            if (!(this.gradientShader instanceof BitmapShader)) {
            }
            this.currentBackgroundHeight = i3;
            if (this.gradientShader instanceof BitmapShader) {
            }
            this.topY = i - i8;
            this.isTopNear = z;
            this.isBottomNear = z2;
        }

        public int getTopY() {
            return this.topY;
        }

        private int dp(float f) {
            if (this.currentType == 2) {
                return (int) Math.ceil(f * 3.0f);
            }
            return AndroidUtilities.dp(f);
        }

        public Paint getPaint() {
            return this.paint;
        }

        public Drawable[] getShadowDrawables() {
            return this.shadowDrawable;
        }

        /* JADX WARN: Removed duplicated region for block: B:47:0x0160  */
        /* JADX WARN: Removed duplicated region for block: B:52:0x016e  */
        /* JADX WARN: Type inference failed for: r6v0, types: [boolean] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Drawable getBackgroundDrawable() {
            char c;
            int i;
            Drawable[][] drawableArr;
            int dp = AndroidUtilities.dp(SharedConfig.bubbleRadius);
            boolean z = this.isTopNear;
            boolean z2 = true;
            if (z && this.isBottomNear) {
                c = 3;
            } else if (z) {
                c = 2;
            } else {
                c = this.isBottomNear ? (char) 1 : (char) 0;
            }
            ?? r6 = this.isSelected;
            boolean z3 = this.gradientShader == null && r6 == 0 && !this.isCrossfadeBackground;
            int color = getColor(this.isOut ? "chat_outBubbleShadow" : "chat_inBubbleShadow");
            if (this.lastDrawWithShadow != z3 || this.currentBackgroundDrawableRadius[r6][c] != dp || (z3 && this.shadowDrawableColor[c] != color)) {
                this.currentBackgroundDrawableRadius[r6][c] = dp;
                try {
                    Bitmap createBitmap = Bitmap.createBitmap(dp(50.0f), dp(40.0f), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    this.backupRect.set(getBounds());
                    if (z3) {
                        this.shadowDrawableColor[c] = color;
                        Paint paint = new Paint(1);
                        paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, dp(40.0f), new int[]{358573417, 694117737}, (float[]) null, Shader.TileMode.CLAMP));
                        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                        paint.setShadowLayer(2.0f, 0.0f, 1.0f, -1);
                        if (AndroidUtilities.density > 1.0f) {
                            setBounds(-1, -1, createBitmap.getWidth() + 1, createBitmap.getHeight() + 1);
                        } else {
                            setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                        }
                        draw(canvas, paint);
                        if (AndroidUtilities.density > 1.0f) {
                            paint.setColor(0);
                            paint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
                            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                            setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                            draw(canvas, paint);
                        }
                    }
                    Paint paint2 = new Paint(1);
                    paint2.setColor(-1);
                    setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                    draw(canvas, paint2);
                    this.backgroundDrawable[r6][c] = new NinePatchDrawable(createBitmap, getByteBuffer((createBitmap.getWidth() / 2) - 1, (createBitmap.getWidth() / 2) + 1, (createBitmap.getHeight() / 2) - 1, (createBitmap.getHeight() / 2) + 1).array(), new Rect(), null);
                    try {
                        setBounds(this.backupRect);
                    } catch (Throwable unused) {
                    }
                } catch (Throwable unused2) {
                }
                this.lastDrawWithShadow = z3;
                if (!this.isSelected) {
                    i = getColor(this.isOut ? "chat_outBubbleSelected" : "chat_inBubbleSelected");
                } else {
                    i = getColor(this.isOut ? "chat_outBubble" : "chat_inBubble");
                }
                drawableArr = this.backgroundDrawable;
                if (drawableArr[r6 == true ? 1 : 0][c] != null && (this.backgroundDrawableColor[r6][c] != i || z2)) {
                    drawableArr[r6][c].setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
                    this.backgroundDrawableColor[r6][c] = i;
                }
                return this.backgroundDrawable[r6][c];
            }
            z2 = false;
            this.lastDrawWithShadow = z3;
            if (!this.isSelected) {
            }
            drawableArr = this.backgroundDrawable;
            if (drawableArr[r6 == true ? 1 : 0][c] != null) {
                drawableArr[r6][c].setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
                this.backgroundDrawableColor[r6][c] = i;
            }
            return this.backgroundDrawable[r6][c];
        }

        public Drawable getTransitionDrawable(int i) {
            if (this.transitionDrawable == null) {
                Bitmap createBitmap = Bitmap.createBitmap(dp(50.0f), dp(40.0f), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                this.backupRect.set(getBounds());
                Paint paint = new Paint(1);
                paint.setColor(-1);
                setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                draw(canvas, paint);
                this.transitionDrawable = new NinePatchDrawable(createBitmap, getByteBuffer((createBitmap.getWidth() / 2) - 1, (createBitmap.getWidth() / 2) + 1, (createBitmap.getHeight() / 2) - 1, (createBitmap.getHeight() / 2) + 1).array(), new Rect(), null);
                setBounds(this.backupRect);
            }
            if (this.transitionDrawableColor != i) {
                this.transitionDrawableColor = i;
                this.transitionDrawable.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
            }
            return this.transitionDrawable;
        }

        public MotionBackgroundDrawable getMotionBackgroundDrawable() {
            if (this.themePreview) {
                return motionBackground[2];
            }
            return motionBackground[this.currentType == 2 ? (char) 1 : (char) 0];
        }

        public Drawable getShadowDrawable() {
            char c;
            if (this.isCrossfadeBackground) {
                return null;
            }
            if (this.gradientShader == null && !this.isSelected && this.crossfadeFromDrawable == null) {
                return null;
            }
            int dp = AndroidUtilities.dp(SharedConfig.bubbleRadius);
            boolean z = this.isTopNear;
            boolean z2 = false;
            if (z && this.isBottomNear) {
                c = 3;
            } else if (z) {
                c = 2;
            } else {
                c = this.isBottomNear ? (char) 1 : (char) 0;
            }
            int[] iArr = this.currentShadowDrawableRadius;
            if (iArr[c] != dp) {
                iArr[c] = dp;
                try {
                    Bitmap createBitmap = Bitmap.createBitmap(dp(50.0f), dp(40.0f), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    Paint paint = new Paint(1);
                    paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, dp(40.0f), new int[]{358573417, 694117737}, (float[]) null, Shader.TileMode.CLAMP));
                    paint.setShadowLayer(2.0f, 0.0f, 1.0f, -1);
                    if (AndroidUtilities.density > 1.0f) {
                        setBounds(-1, -1, createBitmap.getWidth() + 1, createBitmap.getHeight() + 1);
                    } else {
                        setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                    }
                    draw(canvas, paint);
                    if (AndroidUtilities.density > 1.0f) {
                        paint.setColor(0);
                        paint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                        draw(canvas, paint);
                    }
                    this.shadowDrawable[c] = new NinePatchDrawable(createBitmap, getByteBuffer((createBitmap.getWidth() / 2) - 1, (createBitmap.getWidth() / 2) + 1, (createBitmap.getHeight() / 2) - 1, (createBitmap.getHeight() / 2) + 1).array(), new Rect(), null);
                    z2 = true;
                } catch (Throwable unused) {
                }
            }
            int color = getColor(this.isOut ? "chat_outBubbleShadow" : "chat_inBubbleShadow");
            Drawable[] drawableArr = this.shadowDrawable;
            if (drawableArr[c] != null && (this.shadowDrawableColor[c] != color || z2)) {
                drawableArr[c].setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                this.shadowDrawableColor[c] = color;
            }
            return this.shadowDrawable[c];
        }

        private static ByteBuffer getByteBuffer(int i, int i2, int i3, int i4) {
            ByteBuffer order = ByteBuffer.allocate(84).order(ByteOrder.nativeOrder());
            order.put((byte) 1);
            order.put((byte) 2);
            order.put((byte) 2);
            order.put((byte) 9);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(0);
            order.putInt(i);
            order.putInt(i2);
            order.putInt(i3);
            order.putInt(i4);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            order.putInt(1);
            return order;
        }

        public void drawCached(Canvas canvas, PathDrawParams pathDrawParams, Paint paint) {
            this.pathDrawCacheParams = pathDrawParams;
            MessageDrawable messageDrawable = this.crossfadeFromDrawable;
            if (messageDrawable != null) {
                messageDrawable.pathDrawCacheParams = pathDrawParams;
            }
            draw(canvas, paint);
            this.pathDrawCacheParams = null;
            MessageDrawable messageDrawable2 = this.crossfadeFromDrawable;
            if (messageDrawable2 != null) {
                messageDrawable2.pathDrawCacheParams = null;
            }
        }

        public void drawCached(Canvas canvas, PathDrawParams pathDrawParams) {
            drawCached(canvas, pathDrawParams, null);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            MessageDrawable messageDrawable = this.crossfadeFromDrawable;
            if (messageDrawable != null) {
                messageDrawable.draw(canvas);
                setAlpha((int) (this.crossfadeProgress * 255.0f));
                draw(canvas, null);
                setAlpha(255);
                return;
            }
            draw(canvas, null);
        }

        /* JADX WARN: Removed duplicated region for block: B:45:0x00b7  */
        /* JADX WARN: Removed duplicated region for block: B:46:0x00be  */
        /* JADX WARN: Removed duplicated region for block: B:48:0x00c3  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void draw(Canvas canvas, Paint paint) {
            int i;
            boolean z;
            boolean z2;
            PathDrawParams pathDrawParams;
            boolean z3;
            Path path;
            int i2;
            Drawable backgroundDrawable;
            Rect bounds = getBounds();
            if (paint == null && this.gradientShader == null && (backgroundDrawable = getBackgroundDrawable()) != null) {
                backgroundDrawable.setBounds(bounds);
                backgroundDrawable.draw(canvas);
                return;
            }
            int dp = dp(2.0f);
            int i3 = this.overrideRoundRadius;
            if (i3 != 0) {
                i = i3;
            } else if (this.currentType == 2) {
                i3 = dp(6.0f);
                i = dp(6.0f);
            } else {
                i3 = dp(SharedConfig.bubbleRadius);
                i = dp(Math.min(5, SharedConfig.bubbleRadius));
            }
            int dp2 = dp(6.0f);
            Paint paint2 = paint == null ? this.paint : paint;
            if (paint == null && this.gradientShader != null) {
                this.matrix.reset();
                applyMatrixScale();
                this.matrix.postTranslate(0.0f, -this.topY);
                this.gradientShader.setLocalMatrix(this.matrix);
            }
            int max = Math.max(bounds.top, 0);
            if (this.pathDrawCacheParams == null || bounds.height() >= this.currentBackgroundHeight) {
                z2 = this.currentType != 1 ? (this.topY + bounds.bottom) - i3 < this.currentBackgroundHeight : (this.topY + bounds.bottom) - (dp2 * 2) < this.currentBackgroundHeight;
                if (this.topY + (i3 * 2) < 0) {
                    z = false;
                    pathDrawParams = this.pathDrawCacheParams;
                    if (pathDrawParams == null) {
                        path = pathDrawParams.path;
                        z3 = pathDrawParams.invalidatePath(bounds, z2, z);
                    } else {
                        path = this.path;
                        z3 = true;
                    }
                    if (z3) {
                        path.reset();
                        if (this.isOut) {
                            if (this.drawFullBubble || this.currentType == 2 || paint != null || z2) {
                                if (this.currentType == 1) {
                                    path.moveTo((bounds.right - dp(8.0f)) - i3, bounds.bottom - dp);
                                } else {
                                    path.moveTo(bounds.right - dp(2.6f), bounds.bottom - dp);
                                }
                                path.lineTo(bounds.left + dp + i3, bounds.bottom - dp);
                                RectF rectF = this.rect;
                                int i4 = bounds.left;
                                int i5 = bounds.bottom;
                                int i6 = i3 * 2;
                                i2 = i;
                                rectF.set(i4 + dp, (i5 - dp) - i6, i4 + dp + i6, i5 - dp);
                                path.arcTo(this.rect, 90.0f, 90.0f, false);
                            } else {
                                path.moveTo(bounds.right - dp(8.0f), (max - this.topY) + this.currentBackgroundHeight);
                                path.lineTo(bounds.left + dp, (max - this.topY) + this.currentBackgroundHeight);
                                i2 = i;
                            }
                            if (this.drawFullBubble || this.currentType == 2 || paint != null || z) {
                                path.lineTo(bounds.left + dp, bounds.top + dp + i3);
                                RectF rectF2 = this.rect;
                                int i7 = bounds.left;
                                int i8 = bounds.top;
                                int i9 = i3 * 2;
                                rectF2.set(i7 + dp, i8 + dp, i7 + dp + i9, i8 + dp + i9);
                                path.arcTo(this.rect, 180.0f, 90.0f, false);
                                int i10 = this.isTopNear ? i2 : i3;
                                if (this.currentType == 1) {
                                    path.lineTo((bounds.right - dp) - i10, bounds.top + dp);
                                    RectF rectF3 = this.rect;
                                    int i11 = bounds.right;
                                    int i12 = i10 * 2;
                                    int i13 = bounds.top;
                                    rectF3.set((i11 - dp) - i12, i13 + dp, i11 - dp, i13 + dp + i12);
                                } else {
                                    path.lineTo((bounds.right - dp(8.0f)) - i10, bounds.top + dp);
                                    int i14 = i10 * 2;
                                    this.rect.set((bounds.right - dp(8.0f)) - i14, bounds.top + dp, bounds.right - dp(8.0f), bounds.top + dp + i14);
                                }
                                path.arcTo(this.rect, 270.0f, 90.0f, false);
                            } else {
                                path.lineTo(bounds.left + dp, (max - this.topY) - dp(2.0f));
                                if (this.currentType == 1) {
                                    path.lineTo(bounds.right - dp, (max - this.topY) - dp(2.0f));
                                } else {
                                    path.lineTo(bounds.right - dp(8.0f), (max - this.topY) - dp(2.0f));
                                }
                            }
                            int i15 = this.currentType;
                            if (i15 == 1) {
                                if (paint != null || z2) {
                                    if (this.isBottomNear) {
                                        i3 = i2;
                                    }
                                    path.lineTo(bounds.right - dp, (bounds.bottom - dp) - i3);
                                    RectF rectF4 = this.rect;
                                    int i16 = bounds.right;
                                    int i17 = i3 * 2;
                                    int i18 = bounds.bottom;
                                    rectF4.set((i16 - dp) - i17, (i18 - dp) - i17, i16 - dp, i18 - dp);
                                    path.arcTo(this.rect, 0.0f, 90.0f, false);
                                } else {
                                    path.lineTo(bounds.right - dp, (max - this.topY) + this.currentBackgroundHeight);
                                }
                            } else if (this.drawFullBubble || i15 == 2 || paint != null || z2) {
                                path.lineTo(bounds.right - dp(8.0f), ((bounds.bottom - dp) - dp2) - dp(3.0f));
                                int i19 = dp2 * 2;
                                this.rect.set(bounds.right - dp(8.0f), ((bounds.bottom - dp) - i19) - dp(9.0f), (bounds.right - dp(7.0f)) + i19, (bounds.bottom - dp) - dp(1.0f));
                                path.arcTo(this.rect, 180.0f, -83.0f, false);
                            } else {
                                path.lineTo(bounds.right - dp(8.0f), (max - this.topY) + this.currentBackgroundHeight);
                            }
                        } else {
                            int i20 = i;
                            if (this.drawFullBubble || this.currentType == 2 || paint != null || z2) {
                                if (this.currentType == 1) {
                                    path.moveTo(bounds.left + dp(8.0f) + i3, bounds.bottom - dp);
                                } else {
                                    path.moveTo(bounds.left + dp(2.6f), bounds.bottom - dp);
                                }
                                path.lineTo((bounds.right - dp) - i3, bounds.bottom - dp);
                                RectF rectF5 = this.rect;
                                int i21 = bounds.right;
                                int i22 = i3 * 2;
                                int i23 = bounds.bottom;
                                rectF5.set((i21 - dp) - i22, (i23 - dp) - i22, i21 - dp, i23 - dp);
                                path.arcTo(this.rect, 90.0f, -90.0f, false);
                            } else {
                                path.moveTo(bounds.left + dp(8.0f), (max - this.topY) + this.currentBackgroundHeight);
                                path.lineTo(bounds.right - dp, (max - this.topY) + this.currentBackgroundHeight);
                            }
                            if (this.drawFullBubble || this.currentType == 2 || paint != null || z) {
                                path.lineTo(bounds.right - dp, bounds.top + dp + i3);
                                RectF rectF6 = this.rect;
                                int i24 = bounds.right;
                                int i25 = i3 * 2;
                                int i26 = bounds.top;
                                rectF6.set((i24 - dp) - i25, i26 + dp, i24 - dp, i26 + dp + i25);
                                path.arcTo(this.rect, 0.0f, -90.0f, false);
                                int i27 = this.isTopNear ? i20 : i3;
                                if (this.currentType == 1) {
                                    path.lineTo(bounds.left + dp + i27, bounds.top + dp);
                                    RectF rectF7 = this.rect;
                                    int i28 = bounds.left;
                                    int i29 = bounds.top;
                                    int i30 = i27 * 2;
                                    rectF7.set(i28 + dp, i29 + dp, i28 + dp + i30, i29 + dp + i30);
                                } else {
                                    path.lineTo(bounds.left + dp(8.0f) + i27, bounds.top + dp);
                                    int i31 = i27 * 2;
                                    this.rect.set(bounds.left + dp(8.0f), bounds.top + dp, bounds.left + dp(8.0f) + i31, bounds.top + dp + i31);
                                }
                                path.arcTo(this.rect, 270.0f, -90.0f, false);
                            } else {
                                path.lineTo(bounds.right - dp, (max - this.topY) - dp(2.0f));
                                if (this.currentType == 1) {
                                    path.lineTo(bounds.left + dp, (max - this.topY) - dp(2.0f));
                                } else {
                                    path.lineTo(bounds.left + dp(8.0f), (max - this.topY) - dp(2.0f));
                                }
                            }
                            int i32 = this.currentType;
                            if (i32 == 1) {
                                if (paint != null || z2) {
                                    if (this.isBottomNear) {
                                        i3 = i20;
                                    }
                                    path.lineTo(bounds.left + dp, (bounds.bottom - dp) - i3);
                                    RectF rectF8 = this.rect;
                                    int i33 = bounds.left;
                                    int i34 = bounds.bottom;
                                    int i35 = i3 * 2;
                                    rectF8.set(i33 + dp, (i34 - dp) - i35, i33 + dp + i35, i34 - dp);
                                    path.arcTo(this.rect, 180.0f, -90.0f, false);
                                } else {
                                    path.lineTo(bounds.left + dp, (max - this.topY) + this.currentBackgroundHeight);
                                }
                            } else if (this.drawFullBubble || i32 == 2 || paint != null || z2) {
                                path.lineTo(bounds.left + dp(8.0f), ((bounds.bottom - dp) - dp2) - dp(3.0f));
                                int i36 = dp2 * 2;
                                this.rect.set((bounds.left + dp(7.0f)) - i36, ((bounds.bottom - dp) - i36) - dp(9.0f), bounds.left + dp(8.0f), (bounds.bottom - dp) - dp(1.0f));
                                path.arcTo(this.rect, 0.0f, 83.0f, false);
                            } else {
                                path.lineTo(bounds.left + dp(8.0f), (max - this.topY) + this.currentBackgroundHeight);
                            }
                        }
                        path.close();
                    }
                    canvas.drawPath(path, paint2);
                    if (this.gradientShader == null || !this.isSelected || paint != null) {
                        return;
                    }
                    int color = getColor("chat_outBubbleGradientSelectedOverlay");
                    this.selectedPaint.setColor(ColorUtils.setAlphaComponent(color, (int) ((Color.alpha(color) * this.alpha) / 255.0f)));
                    canvas.drawPath(path, this.selectedPaint);
                    return;
                }
            } else {
                z2 = true;
            }
            z = true;
            pathDrawParams = this.pathDrawCacheParams;
            if (pathDrawParams == null) {
            }
            if (z3) {
            }
            canvas.drawPath(path, paint2);
            if (this.gradientShader == null) {
            }
        }

        public void setDrawFullBubble(boolean z) {
            this.drawFullBubble = z;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            if (this.alpha != i) {
                this.alpha = i;
                this.paint.setAlpha(i);
                if (this.isOut) {
                    this.selectedPaint.setAlpha((int) (Color.alpha(getColor("chat_outBubbleGradientSelectedOverlay")) * (i / 255.0f)));
                }
            }
            if (this.gradientShader == null) {
                Drawable backgroundDrawable = getBackgroundDrawable();
                if (Build.VERSION.SDK_INT >= 19) {
                    if (backgroundDrawable.getAlpha() == i) {
                        return;
                    }
                    backgroundDrawable.setAlpha(i);
                    return;
                }
                backgroundDrawable.setAlpha(i);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void setBounds(int i, int i2, int i3, int i4) {
            super.setBounds(i, i2, i3, i4);
            MessageDrawable messageDrawable = this.crossfadeFromDrawable;
            if (messageDrawable != null) {
                messageDrawable.setBounds(i, i2, i3, i4);
            }
        }

        public void setRoundRadius(int i) {
            this.overrideRoundRadius = i;
        }

        /* loaded from: classes3.dex */
        public static class PathDrawParams {
            boolean lastDrawFullBottom;
            boolean lastDrawFullTop;
            Path path = new Path();
            Rect lastRect = new Rect();

            public boolean invalidatePath(Rect rect, boolean z, boolean z2) {
                boolean z3;
                if (!this.lastRect.isEmpty()) {
                    Rect rect2 = this.lastRect;
                    if (rect2.top == rect.top && rect2.bottom == rect.bottom && rect2.right == rect.right && rect2.left == rect.left && this.lastDrawFullTop == z2 && this.lastDrawFullBottom == z && z2 && z) {
                        z3 = false;
                        this.lastDrawFullTop = z2;
                        this.lastDrawFullBottom = z;
                        this.lastRect.set(rect);
                        return z3;
                    }
                }
                z3 = true;
                this.lastDrawFullTop = z2;
                this.lastDrawFullBottom = z;
                this.lastRect.set(rect);
                return z3;
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class PatternsLoader implements NotificationCenter.NotificationCenterDelegate {
        private static PatternsLoader loader;
        private int account = UserConfig.selectedAccount;
        private HashMap<String, LoadingPattern> watingForLoad;

        /* loaded from: classes3.dex */
        public static class LoadingPattern {
            public ArrayList<ThemeAccent> accents;
            public TLRPC$TL_wallPaper pattern;

            private LoadingPattern() {
                this.accents = new ArrayList<>();
            }
        }

        public static void createLoader(boolean z) {
            ArrayList<ThemeAccent> arrayList;
            if (loader == null || z) {
                ArrayList arrayList2 = null;
                int i = 0;
                while (i < 5) {
                    ThemeInfo themeInfo = (ThemeInfo) Theme.themesDict.get(i != 0 ? i != 1 ? i != 2 ? i != 3 ? "Night" : "Day" : "Arctic Blue" : "Dark Blue" : "Blue");
                    if (themeInfo != null && (arrayList = themeInfo.themeAccents) != null && !arrayList.isEmpty()) {
                        int size = themeInfo.themeAccents.size();
                        for (int i2 = 0; i2 < size; i2++) {
                            ThemeAccent themeAccent = themeInfo.themeAccents.get(i2);
                            if (themeAccent.id != Theme.DEFALT_THEME_ACCENT_ID && !TextUtils.isEmpty(themeAccent.patternSlug)) {
                                if (arrayList2 == null) {
                                    arrayList2 = new ArrayList();
                                }
                                arrayList2.add(themeAccent);
                            }
                        }
                    }
                    i++;
                }
                loader = new PatternsLoader(arrayList2);
            }
        }

        private PatternsLoader(final ArrayList<ThemeAccent> arrayList) {
            if (arrayList == null) {
                return;
            }
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$PatternsLoader$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Theme.PatternsLoader.this.lambda$new$1(arrayList);
                }
            });
        }

        public /* synthetic */ void lambda$new$1(final ArrayList arrayList) {
            int size = arrayList.size();
            ArrayList arrayList2 = null;
            int i = 0;
            while (i < size) {
                ThemeAccent themeAccent = (ThemeAccent) arrayList.get(i);
                File pathToWallpaper = themeAccent.getPathToWallpaper();
                if (pathToWallpaper != null && pathToWallpaper.exists()) {
                    arrayList.remove(i);
                    i--;
                    size--;
                } else {
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                    }
                    if (!arrayList2.contains(themeAccent.patternSlug)) {
                        arrayList2.add(themeAccent.patternSlug);
                    }
                }
                i++;
            }
            if (arrayList2 == null) {
                return;
            }
            TLRPC$TL_account_getMultiWallPapers tLRPC$TL_account_getMultiWallPapers = new TLRPC$TL_account_getMultiWallPapers();
            int size2 = arrayList2.size();
            for (int i2 = 0; i2 < size2; i2++) {
                TLRPC$TL_inputWallPaperSlug tLRPC$TL_inputWallPaperSlug = new TLRPC$TL_inputWallPaperSlug();
                tLRPC$TL_inputWallPaperSlug.slug = (String) arrayList2.get(i2);
                tLRPC$TL_account_getMultiWallPapers.wallpapers.add(tLRPC$TL_inputWallPaperSlug);
            }
            ConnectionsManager.getInstance(this.account).sendRequest(tLRPC$TL_account_getMultiWallPapers, new RequestDelegate() { // from class: org.telegram.ui.ActionBar.Theme$PatternsLoader$$ExternalSyntheticLambda3
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    Theme.PatternsLoader.this.lambda$new$0(arrayList, tLObject, tLRPC$TL_error);
                }
            });
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r12v2 */
        public /* synthetic */ void lambda$new$0(ArrayList arrayList, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            if (tLObject instanceof TLRPC$Vector) {
                TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
                int size = tLRPC$Vector.objects.size();
                Bitmap bitmap = null;
                ArrayList<ThemeAccent> arrayList2 = null;
                int i = 0;
                while (i < size) {
                    TLRPC$WallPaper tLRPC$WallPaper = (TLRPC$WallPaper) tLRPC$Vector.objects.get(i);
                    if (tLRPC$WallPaper instanceof TLRPC$TL_wallPaper) {
                        TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) tLRPC$WallPaper;
                        if (tLRPC$TL_wallPaper.pattern) {
                            File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$TL_wallPaper.document, true);
                            int size2 = arrayList.size();
                            Bitmap bitmap2 = bitmap;
                            Boolean bool = bitmap2;
                            int i2 = 0;
                            AnonymousClass1 anonymousClass1 = bitmap;
                            while (i2 < size2) {
                                ThemeAccent themeAccent = (ThemeAccent) arrayList.get(i2);
                                bool = bool;
                                if (themeAccent.patternSlug.equals(tLRPC$TL_wallPaper.slug)) {
                                    if (bool == 0) {
                                        bool = Boolean.valueOf(pathToAttach.exists());
                                    }
                                    if (bitmap2 != null || bool.booleanValue()) {
                                        bitmap2 = createWallpaperForAccent(bitmap2, "application/x-tgwallpattern".equals(tLRPC$TL_wallPaper.document.mime_type), pathToAttach, themeAccent);
                                        if (arrayList2 == null) {
                                            arrayList2 = new ArrayList<>();
                                        }
                                        arrayList2.add(themeAccent);
                                    } else {
                                        String attachFileName = FileLoader.getAttachFileName(tLRPC$TL_wallPaper.document);
                                        if (this.watingForLoad == null) {
                                            this.watingForLoad = new HashMap<>();
                                        }
                                        LoadingPattern loadingPattern = this.watingForLoad.get(attachFileName);
                                        if (loadingPattern == null) {
                                            loadingPattern = new LoadingPattern();
                                            loadingPattern.pattern = tLRPC$TL_wallPaper;
                                            this.watingForLoad.put(attachFileName, loadingPattern);
                                        }
                                        loadingPattern.accents.add(themeAccent);
                                    }
                                }
                                i2++;
                                anonymousClass1 = 0;
                                bool = bool;
                            }
                            if (bitmap2 != null) {
                                bitmap2.recycle();
                            }
                            i++;
                            bitmap = null;
                        }
                    }
                    i++;
                    bitmap = null;
                }
                checkCurrentWallpaper(arrayList2, true);
            }
        }

        private void checkCurrentWallpaper(final ArrayList<ThemeAccent> arrayList, final boolean z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$PatternsLoader$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    Theme.PatternsLoader.this.lambda$checkCurrentWallpaper$2(arrayList, z);
                }
            });
        }

        /* renamed from: checkCurrentWallpaperInternal */
        public void lambda$checkCurrentWallpaper$2(ArrayList<ThemeAccent> arrayList, boolean z) {
            if (arrayList != null && Theme.currentTheme.themeAccents != null && !Theme.currentTheme.themeAccents.isEmpty() && arrayList.contains(Theme.currentTheme.getAccent(false))) {
                Theme.reloadWallpaper();
            }
            if (z) {
                if (this.watingForLoad == null) {
                    return;
                }
                NotificationCenter.getInstance(this.account).addObserver(this, NotificationCenter.fileLoaded);
                NotificationCenter.getInstance(this.account).addObserver(this, NotificationCenter.fileLoadFailed);
                for (Map.Entry<String, LoadingPattern> entry : this.watingForLoad.entrySet()) {
                    FileLoader.getInstance(this.account).loadFile(ImageLocation.getForDocument(entry.getValue().pattern.document), "wallpaper", null, 0, 1);
                }
                return;
            }
            HashMap<String, LoadingPattern> hashMap = this.watingForLoad;
            if (hashMap != null && !hashMap.isEmpty()) {
                return;
            }
            NotificationCenter.getInstance(this.account).removeObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.account).removeObserver(this, NotificationCenter.fileLoadFailed);
        }

        private Bitmap createWallpaperForAccent(Bitmap bitmap, boolean z, File file, ThemeAccent themeAccent) {
            Bitmap bitmap2;
            Throwable th;
            File pathToWallpaper;
            Drawable drawable;
            int i;
            Bitmap loadScreenSizedBitmap;
            Integer num;
            Integer num2;
            Integer num3;
            try {
                pathToWallpaper = themeAccent.getPathToWallpaper();
                drawable = null;
            } catch (Throwable th2) {
                th = th2;
                bitmap2 = bitmap;
            }
            if (pathToWallpaper == null) {
                return null;
            }
            ThemeInfo themeInfo = themeAccent.parentTheme;
            HashMap<String, Integer> themeFileValues = Theme.getThemeFileValues(null, themeInfo.assetName, null);
            Theme.checkIsDark(themeFileValues, themeInfo);
            int i2 = themeAccent.accentColor;
            int i3 = (int) themeAccent.backgroundOverrideColor;
            long j = themeAccent.backgroundGradientOverrideColor1;
            int i4 = (int) j;
            if (i4 == 0 && j == 0) {
                if (i3 != 0) {
                    i2 = i3;
                }
                Integer num4 = themeFileValues.get("chat_wallpaper_gradient_to");
                if (num4 != null) {
                    i4 = Theme.changeColorAccent(themeInfo, i2, num4.intValue());
                }
            } else {
                i2 = 0;
            }
            long j2 = themeAccent.backgroundGradientOverrideColor2;
            int i5 = (int) j2;
            if (i5 == 0 && j2 == 0 && (num3 = themeFileValues.get("key_chat_wallpaper_gradient_to2")) != null) {
                i5 = Theme.changeColorAccent(themeInfo, i2, num3.intValue());
            }
            long j3 = themeAccent.backgroundGradientOverrideColor3;
            int i6 = (int) j3;
            if (i6 == 0 && j3 == 0 && (num2 = themeFileValues.get("key_chat_wallpaper_gradient_to3")) != null) {
                i6 = Theme.changeColorAccent(themeInfo, i2, num2.intValue());
            }
            if (i3 == 0 && (num = themeFileValues.get("chat_wallpaper")) != null) {
                i3 = Theme.changeColorAccent(themeInfo, i2, num.intValue());
            }
            if (i5 != 0) {
                i = MotionBackgroundDrawable.getPatternColor(i3, i4, i5, i6);
            } else if (i4 != 0) {
                Drawable backgroundGradientDrawable = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(themeAccent.backgroundRotation), new int[]{i3, i4});
                i = AndroidUtilities.getPatternColor(AndroidUtilities.getAverageColor(i3, i4));
                drawable = backgroundGradientDrawable;
            } else {
                drawable = new ColorDrawable(i3);
                i = AndroidUtilities.getPatternColor(i3);
            }
            if (bitmap == null) {
                Point point = AndroidUtilities.displaySize;
                int min = Math.min(point.x, point.y);
                Point point2 = AndroidUtilities.displaySize;
                int max = Math.max(point2.x, point2.y);
                if (!z) {
                    loadScreenSizedBitmap = Theme.loadScreenSizedBitmap(new FileInputStream(file), 0);
                } else {
                    loadScreenSizedBitmap = SvgHelper.getBitmap(file, min, max, false);
                }
                bitmap2 = loadScreenSizedBitmap;
            } else {
                bitmap2 = bitmap;
            }
            try {
                if (drawable != null) {
                    Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    drawable.setBounds(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
                    drawable.draw(canvas);
                    Paint paint = new Paint(2);
                    paint.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
                    paint.setAlpha((int) (Math.abs(themeAccent.patternIntensity) * 255.0f));
                    canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
                    createBitmap.compress(Bitmap.CompressFormat.JPEG, 87, new FileOutputStream(pathToWallpaper));
                } else {
                    FileOutputStream fileOutputStream = new FileOutputStream(pathToWallpaper);
                    bitmap2.compress(Bitmap.CompressFormat.PNG, 87, fileOutputStream);
                    fileOutputStream.close();
                }
            } catch (Throwable th3) {
                th = th3;
                FileLog.e(th);
                return bitmap2;
            }
            return bitmap2;
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            HashMap<String, LoadingPattern> hashMap = this.watingForLoad;
            if (hashMap == null) {
                return;
            }
            if (i == NotificationCenter.fileLoaded) {
                final LoadingPattern remove = hashMap.remove((String) objArr[0]);
                if (remove == null) {
                    return;
                }
                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$PatternsLoader$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        Theme.PatternsLoader.this.lambda$didReceivedNotification$3(remove);
                    }
                });
            } else if (i != NotificationCenter.fileLoadFailed || hashMap.remove((String) objArr[0]) == null) {
            } else {
                checkCurrentWallpaper(null, false);
            }
        }

        public /* synthetic */ void lambda$didReceivedNotification$3(LoadingPattern loadingPattern) {
            TLRPC$TL_wallPaper tLRPC$TL_wallPaper = loadingPattern.pattern;
            File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$TL_wallPaper.document, true);
            int size = loadingPattern.accents.size();
            Bitmap bitmap = null;
            ArrayList<ThemeAccent> arrayList = null;
            for (int i = 0; i < size; i++) {
                ThemeAccent themeAccent = loadingPattern.accents.get(i);
                if (themeAccent.patternSlug.equals(tLRPC$TL_wallPaper.slug)) {
                    bitmap = createWallpaperForAccent(bitmap, "application/x-tgwallpattern".equals(tLRPC$TL_wallPaper.document.mime_type), pathToAttach, themeAccent);
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                        arrayList.add(themeAccent);
                    }
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
            checkCurrentWallpaper(arrayList, false);
        }
    }

    /* loaded from: classes3.dex */
    public static class ThemeAccent {
        public int accentColor;
        public int accentColor2;
        public int account;
        public long backgroundGradientOverrideColor1;
        public long backgroundGradientOverrideColor2;
        public long backgroundGradientOverrideColor3;
        public long backgroundOverrideColor;
        public int id;
        public TLRPC$TL_theme info;
        public boolean isDefault;
        public int myMessagesAccentColor;
        public boolean myMessagesAnimated;
        public int myMessagesGradientAccentColor1;
        public int myMessagesGradientAccentColor2;
        public int myMessagesGradientAccentColor3;
        public OverrideWallpaperInfo overrideWallpaper;
        public ThemeInfo parentTheme;
        public TLRPC$TL_wallPaper pattern;
        public float patternIntensity;
        public boolean patternMotion;
        public TLRPC$InputFile uploadedFile;
        public TLRPC$InputFile uploadedThumb;
        public String uploadingFile;
        public String uploadingThumb;
        public int backgroundRotation = 45;
        public String patternSlug = "";
        private float[] tempHSV = new float[3];

        ThemeAccent() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:16:0x0082, code lost:
            r10 = (java.lang.Integer) org.telegram.ui.ActionBar.Theme.defaultColors.get(r9);
         */
        /* JADX WARN: Code restructure failed: missing block: B:92:0x01d1, code lost:
            r12 = (java.lang.Integer) org.telegram.ui.ActionBar.Theme.defaultColors.get(r8);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean fillAccentColors(HashMap<String, Integer> hashMap, HashMap<String, Integer> hashMap2) {
            boolean z;
            int i;
            boolean z2;
            int i2;
            int i3;
            int i4;
            int changeColorAccent;
            String str;
            int changeColorAccent2;
            String str2;
            int i5;
            int i6;
            boolean z3;
            String str3;
            float[] tempHsv = Theme.getTempHsv(1);
            float[] tempHsv2 = Theme.getTempHsv(2);
            Color.colorToHSV(this.parentTheme.accentBaseColor, tempHsv);
            Color.colorToHSV(this.accentColor, tempHsv2);
            boolean isDark = this.parentTheme.isDark();
            if (this.accentColor != this.parentTheme.accentBaseColor || this.accentColor2 != 0) {
                Set<String> keySet = hashMap.keySet();
                Set keySet2 = Theme.defaultColors.keySet();
                HashSet hashSet = new HashSet(keySet.size() + keySet2.size());
                hashSet.addAll(keySet);
                hashSet.addAll(keySet2);
                hashSet.removeAll(Theme.themeAccentExclusionKeys);
                Iterator it = hashSet.iterator();
                while (it.hasNext()) {
                    String str4 = (String) it.next();
                    Integer num = hashMap.get(str4);
                    if (num != null || (str3 = (String) Theme.fallbackKeys.get(str4)) == null || hashMap.get(str3) == null) {
                        int changeColorAccent3 = Theme.changeColorAccent(tempHsv, tempHsv2, num.intValue(), isDark);
                        if (changeColorAccent3 != num.intValue()) {
                            hashMap2.put(str4, Integer.valueOf(changeColorAccent3));
                        }
                    }
                }
            }
            int i7 = this.myMessagesAccentColor;
            if ((i7 == 0 && this.accentColor == 0) || this.myMessagesGradientAccentColor1 == 0) {
                z = false;
            } else {
                if (i7 == 0) {
                    i7 = this.accentColor;
                }
                Integer num2 = hashMap.get("chat_outBubble");
                if (num2 == null) {
                    num2 = (Integer) Theme.defaultColors.get("chat_outBubble");
                }
                int colorDistance = AndroidUtilities.getColorDistance(i7, Theme.changeColorAccent(tempHsv, tempHsv2, num2.intValue(), isDark));
                int colorDistance2 = AndroidUtilities.getColorDistance(i7, this.myMessagesGradientAccentColor1);
                if (this.myMessagesGradientAccentColor2 == 0) {
                    z3 = Theme.useBlackText(this.myMessagesAccentColor, this.myMessagesGradientAccentColor1);
                } else {
                    int averageColor = AndroidUtilities.getAverageColor(AndroidUtilities.getAverageColor(this.myMessagesAccentColor, this.myMessagesGradientAccentColor1), this.myMessagesGradientAccentColor2);
                    int i8 = this.myMessagesGradientAccentColor3;
                    if (i8 != 0) {
                        averageColor = AndroidUtilities.getAverageColor(averageColor, i8);
                    }
                    z3 = AndroidUtilities.computePerceivedBrightness(averageColor) > 0.705f;
                }
                z = z3 && colorDistance <= 35000 && colorDistance2 <= 35000;
                i7 = Theme.getAccentColor(tempHsv, num2.intValue(), i7);
            }
            boolean z4 = (i7 == 0 || (((i5 = this.parentTheme.accentBaseColor) == 0 || i7 == i5) && ((i6 = this.accentColor) == 0 || i6 == i7))) ? false : true;
            if (z4 || this.accentColor2 != 0) {
                int i9 = this.accentColor2;
                if (i9 != 0) {
                    Color.colorToHSV(i9, tempHsv2);
                } else {
                    Color.colorToHSV(i7, tempHsv2);
                }
                Iterator it2 = Theme.myMessagesColorKeys.iterator();
                while (it2.hasNext()) {
                    String str5 = (String) it2.next();
                    Integer num3 = hashMap.get(str5);
                    if (num3 != null || (str2 = (String) Theme.fallbackKeys.get(str5)) == null || hashMap.get(str2) == null) {
                        if (num3 == null) {
                            num3 = (Integer) Theme.defaultColors.get(str5);
                        }
                        if (num3 != null && (changeColorAccent2 = Theme.changeColorAccent(tempHsv, tempHsv2, num3.intValue(), isDark)) != num3.intValue()) {
                            hashMap2.put(str5, Integer.valueOf(changeColorAccent2));
                        }
                    }
                }
                if (z4) {
                    Color.colorToHSV(i7, tempHsv2);
                    Iterator it3 = Theme.myMessagesBubblesColorKeys.iterator();
                    while (it3.hasNext()) {
                        String str6 = (String) it3.next();
                        Integer num4 = hashMap.get(str6);
                        if (num4 != null || (str = (String) Theme.fallbackKeys.get(str6)) == null || hashMap.get(str) == null) {
                            if (num4 != null && (changeColorAccent = Theme.changeColorAccent(tempHsv, tempHsv2, num4.intValue(), isDark)) != num4.intValue()) {
                                hashMap2.put(str6, Integer.valueOf(changeColorAccent));
                            }
                        }
                    }
                }
            }
            if (!z && (i = this.myMessagesGradientAccentColor1) != 0) {
                if (this.myMessagesGradientAccentColor2 == 0) {
                    z2 = Theme.useBlackText(this.myMessagesAccentColor, i);
                } else {
                    int averageColor2 = AndroidUtilities.getAverageColor(AndroidUtilities.getAverageColor(this.myMessagesAccentColor, i), this.myMessagesGradientAccentColor2);
                    int i10 = this.myMessagesGradientAccentColor3;
                    if (i10 != 0) {
                        averageColor2 = AndroidUtilities.getAverageColor(averageColor2, i10);
                    }
                    z2 = AndroidUtilities.computePerceivedBrightness(averageColor2) > 0.705f;
                }
                if (z2) {
                    i4 = -14606047;
                    i3 = -11184811;
                    i2 = 1291845632;
                } else {
                    i3 = -1118482;
                    i2 = 1308622847;
                    i4 = -1;
                }
                if (this.accentColor2 == 0) {
                    hashMap2.put("chat_outAudioProgress", Integer.valueOf(i2));
                    hashMap2.put("chat_outAudioSelectedProgress", Integer.valueOf(i2));
                    hashMap2.put("chat_outAudioSeekbar", Integer.valueOf(i2));
                    hashMap2.put("chat_outAudioCacheSeekbar", Integer.valueOf(i2));
                    hashMap2.put("chat_outAudioSeekbarSelected", Integer.valueOf(i2));
                    hashMap2.put("chat_outAudioSeekbarFill", Integer.valueOf(i4));
                    hashMap2.put("chat_outVoiceSeekbar", Integer.valueOf(i2));
                    hashMap2.put("chat_outVoiceSeekbarSelected", Integer.valueOf(i2));
                    hashMap2.put("chat_outVoiceSeekbarFill", Integer.valueOf(i4));
                    hashMap2.put("chat_messageLinkOut", Integer.valueOf(i4));
                    hashMap2.put("chat_outForwardedNameText", Integer.valueOf(i4));
                    hashMap2.put("chat_outViaBotNameText", Integer.valueOf(i4));
                    hashMap2.put("chat_outReplyLine", Integer.valueOf(i4));
                    hashMap2.put("chat_outReplyNameText", Integer.valueOf(i4));
                    hashMap2.put("chat_outPreviewLine", Integer.valueOf(i4));
                    hashMap2.put("chat_outSiteNameText", Integer.valueOf(i4));
                    hashMap2.put("chat_outInstant", Integer.valueOf(i4));
                    hashMap2.put("chat_outInstantSelected", Integer.valueOf(i4));
                    hashMap2.put("chat_outPreviewInstantText", Integer.valueOf(i4));
                    hashMap2.put("chat_outPreviewInstantSelectedText", Integer.valueOf(i4));
                    hashMap2.put("chat_outViews", Integer.valueOf(i4));
                    hashMap2.put("chat_outViewsSelected", Integer.valueOf(i4));
                    hashMap2.put("chat_outAudioTitleText", Integer.valueOf(i4));
                    hashMap2.put("chat_outFileNameText", Integer.valueOf(i4));
                    hashMap2.put("chat_outContactNameText", Integer.valueOf(i4));
                    hashMap2.put("chat_outAudioPerfomerText", Integer.valueOf(i4));
                    hashMap2.put("chat_outAudioPerfomerSelectedText", Integer.valueOf(i4));
                    hashMap2.put("chat_outSentCheck", Integer.valueOf(i4));
                    hashMap2.put("chat_outSentCheckSelected", Integer.valueOf(i4));
                    hashMap2.put("chat_outSentCheckRead", Integer.valueOf(i4));
                    hashMap2.put("chat_outSentCheckReadSelected", Integer.valueOf(i4));
                    hashMap2.put("chat_outSentClock", Integer.valueOf(i4));
                    hashMap2.put("chat_outSentClockSelected", Integer.valueOf(i4));
                    hashMap2.put("chat_outMenu", Integer.valueOf(i4));
                    hashMap2.put("chat_outMenuSelected", Integer.valueOf(i4));
                    hashMap2.put("chat_outTimeText", Integer.valueOf(i4));
                    hashMap2.put("chat_outTimeSelectedText", Integer.valueOf(i4));
                    hashMap2.put("chat_outAudioDurationText", Integer.valueOf(i3));
                    hashMap2.put("chat_outAudioDurationSelectedText", Integer.valueOf(i3));
                    hashMap2.put("chat_outContactPhoneText", Integer.valueOf(i3));
                    hashMap2.put("chat_outContactPhoneSelectedText", Integer.valueOf(i3));
                    hashMap2.put("chat_outFileInfoText", Integer.valueOf(i3));
                    hashMap2.put("chat_outFileInfoSelectedText", Integer.valueOf(i3));
                    hashMap2.put("chat_outVenueInfoText", Integer.valueOf(i3));
                    hashMap2.put("chat_outVenueInfoSelectedText", Integer.valueOf(i3));
                    hashMap2.put("chat_outLoader", Integer.valueOf(i4));
                    hashMap2.put("chat_outLoaderSelected", Integer.valueOf(i4));
                    hashMap2.put("chat_outFileProgress", Integer.valueOf(this.myMessagesAccentColor));
                    hashMap2.put("chat_outFileProgressSelected", Integer.valueOf(this.myMessagesAccentColor));
                    hashMap2.put("chat_outMediaIcon", Integer.valueOf(this.myMessagesAccentColor));
                    hashMap2.put("chat_outMediaIconSelected", Integer.valueOf(this.myMessagesAccentColor));
                }
                hashMap2.put("chat_outReplyMessageText", Integer.valueOf(i4));
                hashMap2.put("chat_outReplyMediaMessageText", Integer.valueOf(i4));
                hashMap2.put("chat_outReplyMediaMessageSelectedText", Integer.valueOf(i4));
                hashMap2.put("chat_messageTextOut", Integer.valueOf(i4));
            }
            if (z) {
                if (AndroidUtilities.getColorDistance(-1, hashMap2.containsKey("chat_outLoader") ? hashMap2.get("chat_outLoader").intValue() : 0) < 5000) {
                    z = false;
                }
            }
            int i11 = this.myMessagesAccentColor;
            if (i11 != 0 && this.myMessagesGradientAccentColor1 != 0) {
                hashMap2.put("chat_outBubble", Integer.valueOf(i11));
                hashMap2.put("chat_outBubbleGradient", Integer.valueOf(this.myMessagesGradientAccentColor1));
                int i12 = this.myMessagesGradientAccentColor2;
                if (i12 != 0) {
                    hashMap2.put("chat_outBubbleGradient2", Integer.valueOf(i12));
                    int i13 = this.myMessagesGradientAccentColor3;
                    if (i13 != 0) {
                        hashMap2.put("chat_outBubbleGradient3", Integer.valueOf(i13));
                    }
                }
                hashMap2.put("chat_outBubbleGradientAnimated", Integer.valueOf(this.myMessagesAnimated ? 1 : 0));
            }
            long j = this.backgroundOverrideColor;
            int i14 = (int) j;
            if (i14 != 0) {
                hashMap2.put("chat_wallpaper", Integer.valueOf(i14));
            } else if (j != 0) {
                hashMap2.remove("chat_wallpaper");
            }
            long j2 = this.backgroundGradientOverrideColor1;
            int i15 = (int) j2;
            if (i15 != 0) {
                hashMap2.put("chat_wallpaper_gradient_to", Integer.valueOf(i15));
            } else if (j2 != 0) {
                hashMap2.remove("chat_wallpaper_gradient_to");
            }
            long j3 = this.backgroundGradientOverrideColor2;
            int i16 = (int) j3;
            if (i16 != 0) {
                hashMap2.put("key_chat_wallpaper_gradient_to2", Integer.valueOf(i16));
            } else if (j3 != 0) {
                hashMap2.remove("key_chat_wallpaper_gradient_to2");
            }
            long j4 = this.backgroundGradientOverrideColor3;
            int i17 = (int) j4;
            if (i17 != 0) {
                hashMap2.put("key_chat_wallpaper_gradient_to3", Integer.valueOf(i17));
            } else if (j4 != 0) {
                hashMap2.remove("key_chat_wallpaper_gradient_to3");
            }
            int i18 = this.backgroundRotation;
            if (i18 != 45) {
                hashMap2.put("chat_wallpaper_gradient_rotation", Integer.valueOf(i18));
            }
            Integer num5 = hashMap2.get("chat_outBubble");
            if (num5 == null) {
                num5 = Integer.valueOf(Theme.getColor("chat_outBubble"));
            }
            Integer num6 = hashMap2.get("chat_inBubble");
            if (num6 == null) {
                num6 = Integer.valueOf(Theme.getColor("chat_inBubble"));
            }
            num5.intValue();
            TLRPC$TL_theme tLRPC$TL_theme = this.info;
            if (tLRPC$TL_theme != null && tLRPC$TL_theme.emoticon != null && !isDark) {
                hashMap2.remove("chat_selectedBackground");
                int averageColor3 = averageColor(hashMap2, "chat_wallpaper_gradient_to", "key_chat_wallpaper_gradient_to2", "key_chat_wallpaper_gradient_to3");
                if (averageColor3 == 0) {
                    averageColor3 = averageColor(hashMap2, "chat_wallpaper");
                }
                if (averageColor3 == 0) {
                    averageColor3 = this.accentColor;
                }
                int bubbleSelectedOverlay = bubbleSelectedOverlay(num5.intValue(), averageColor3);
                hashMap2.put("chat_outBubbleSelectedOverlay", Integer.valueOf(bubbleSelectedOverlay));
                hashMap2.put("chat_outBubbleGradientSelectedOverlay", Integer.valueOf(bubbleSelectedOverlay));
                hashMap2.put("chat_outBubbleSelected", Integer.valueOf(Theme.blendOver(num5.intValue(), bubbleSelectedOverlay)));
                int bubbleSelectedOverlay2 = bubbleSelectedOverlay(num6.intValue(), this.accentColor);
                hashMap2.put("chat_inBubbleSelectedOverlay", Integer.valueOf(bubbleSelectedOverlay2));
                hashMap2.put("chat_inBubbleSelected", Integer.valueOf(Theme.blendOver(num6.intValue(), bubbleSelectedOverlay2)));
            }
            if (!isDark) {
                hashMap2.put("chat_inTextSelectionHighlight", Integer.valueOf(textSelectionBackground(false, num6.intValue(), this.accentColor)));
                hashMap2.put("chat_outTextSelectionHighlight", Integer.valueOf(textSelectionBackground(true, num5.intValue(), this.accentColor)));
                hashMap2.put("chat_outTextSelectionCursor", Integer.valueOf(textSelectionHandle(num5.intValue(), this.accentColor)));
            }
            Integer num7 = hashMap2.get("chat_messageLinkIn");
            if (num7 == null) {
                num7 = Integer.valueOf(Theme.getColor("chat_messageLinkIn"));
            }
            Integer num8 = hashMap2.get("chat_messageLinkOut");
            if (num8 == null) {
                num8 = Integer.valueOf(Theme.getColor("chat_messageLinkOut"));
            }
            hashMap2.put("chat_linkSelectBackground", Integer.valueOf(linkSelectionBackground(num7.intValue(), num6.intValue(), isDark)));
            hashMap2.put("chat_outLinkSelectBackground", Integer.valueOf(linkSelectionBackground(num8.intValue(), num5.intValue(), isDark)));
            Integer num9 = hashMap2.get("actionBarDefaultSubmenuBackground");
            if (num9 == null) {
                num9 = Integer.valueOf(Theme.getColor("actionBarDefaultSubmenuBackground"));
            }
            hashMap2.put("actionBarDefaultSubmenuSeparator", Integer.valueOf(Color.argb(Color.alpha(num9.intValue()), Math.max(0, Color.red(num9.intValue()) - 10), Math.max(0, Color.green(num9.intValue()) - 10), Math.max(0, Color.blue(num9.intValue()) - 10))));
            return true ^ z;
        }

        private int bubbleSelectedOverlay(int i, int i2) {
            Color.colorToHSV(i2, this.tempHSV);
            float[] fArr = this.tempHSV;
            float f = fArr[0];
            Color.colorToHSV(i, fArr);
            float[] fArr2 = this.tempHSV;
            if (fArr2[1] <= 0.0f) {
                fArr2[0] = f;
            }
            fArr2[1] = Math.max(0.0f, Math.min(1.0f, fArr2[1] + 0.6f));
            float[] fArr3 = this.tempHSV;
            fArr3[2] = Math.max(0.0f, Math.min(1.0f, fArr3[2] - 0.05f));
            return Color.HSVToColor(30, this.tempHSV);
        }

        private int textSelectionBackground(boolean z, int i, int i2) {
            Color.colorToHSV(i2, this.tempHSV);
            float[] fArr = this.tempHSV;
            float f = fArr[0];
            Color.colorToHSV(i, fArr);
            float[] fArr2 = this.tempHSV;
            if (fArr2[1] <= 0.0f || (fArr2[0] > 45.0f && fArr2[0] < 85.0f)) {
                fArr2[0] = f;
            }
            fArr2[1] = Math.max(0.0f, Math.min(1.0f, fArr2[1] + (fArr2[2] > 0.85f ? 0.25f : 0.45f)));
            float[] fArr3 = this.tempHSV;
            fArr3[2] = Math.max(0.0f, Math.min(1.0f, fArr3[2] - 0.15f));
            return Color.HSVToColor(80, this.tempHSV);
        }

        private int textSelectionHandle(int i, int i2) {
            Color.colorToHSV(i2, this.tempHSV);
            float[] fArr = this.tempHSV;
            float f = fArr[0];
            Color.colorToHSV(i, fArr);
            float[] fArr2 = this.tempHSV;
            if (fArr2[1] <= 0.0f || (fArr2[0] > 45.0f && fArr2[0] < 85.0f)) {
                fArr2[0] = f;
            }
            fArr2[1] = Math.max(0.0f, Math.min(1.0f, fArr2[1] + 0.6f));
            float[] fArr3 = this.tempHSV;
            fArr3[2] = Math.max(0.0f, Math.min(1.0f, fArr3[2] - (fArr3[2] > 0.7f ? 0.25f : 0.125f)));
            return Theme.blendOver(i, Color.HSVToColor(255, this.tempHSV));
        }

        private int linkSelectionBackground(int i, int i2, boolean z) {
            Color.colorToHSV(ColorUtils.blendARGB(i, i2, 0.25f), this.tempHSV);
            float[] fArr = this.tempHSV;
            float f = 0.1f;
            fArr[1] = Math.max(0.0f, Math.min(1.0f, fArr[1] - 0.1f));
            float[] fArr2 = this.tempHSV;
            float f2 = fArr2[2];
            if (!z) {
                f = 0.0f;
            }
            fArr2[2] = Math.max(0.0f, Math.min(1.0f, f2 + f));
            return Color.HSVToColor(51, this.tempHSV);
        }

        private int averageColor(HashMap<String, Integer> hashMap, String... strArr) {
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < strArr.length; i5++) {
                if (hashMap.containsKey(strArr[i5])) {
                    try {
                        int intValue = hashMap.get(strArr[i5]).intValue();
                        i2 += Color.red(intValue);
                        i3 += Color.green(intValue);
                        i4 += Color.blue(intValue);
                        i++;
                    } catch (Exception unused) {
                    }
                }
            }
            if (i == 0) {
                return 0;
            }
            return Color.argb(255, i2 / i, i3 / i, i4 / i);
        }

        public File getPathToWallpaper() {
            if (this.id < 100) {
                if (TextUtils.isEmpty(this.patternSlug)) {
                    return null;
                }
                return new File(ApplicationLoader.getFilesDirFixed(), String.format(Locale.US, "%s_%d_%s_v5.jpg", this.parentTheme.getKey(), Integer.valueOf(this.id), this.patternSlug));
            } else if (TextUtils.isEmpty(this.patternSlug)) {
                return null;
            } else {
                return new File(ApplicationLoader.getFilesDirFixed(), String.format(Locale.US, "%s_%d_%s_v8_debug.jpg", this.parentTheme.getKey(), Integer.valueOf(this.id), this.patternSlug));
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:91:0x02f4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:77:0x02eb -> B:88:0x02ef). Please submit an issue!!! */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public File saveToFile() {
            String str;
            FileOutputStream fileOutputStream;
            Exception e;
            Throwable th;
            FileOutputStream fileOutputStream2;
            File sharingDirectory = AndroidUtilities.getSharingDirectory();
            sharingDirectory.mkdirs();
            Integer num = 0;
            File file = new File(sharingDirectory, String.format(Locale.US, "%s_%d.attheme", this.parentTheme.getKey(), Integer.valueOf(this.id)));
            HashMap<String, Integer> themeFileValues = Theme.getThemeFileValues(null, this.parentTheme.assetName, null);
            HashMap<String, Integer> hashMap = new HashMap<>(themeFileValues);
            fillAccentColors(themeFileValues, hashMap);
            if (!TextUtils.isEmpty(this.patternSlug)) {
                StringBuilder sb = new StringBuilder();
                if (this.patternMotion) {
                    sb.append("motion");
                }
                Integer num2 = hashMap.get("chat_wallpaper");
                if (num2 == null) {
                    num2 = -1;
                }
                Integer num3 = hashMap.get("chat_wallpaper_gradient_to");
                if (num3 == null) {
                    num3 = num;
                }
                Integer num4 = hashMap.get("key_chat_wallpaper_gradient_to2");
                if (num4 == null) {
                    num4 = num;
                }
                Integer num5 = hashMap.get("key_chat_wallpaper_gradient_to3");
                if (num5 != null) {
                    num = num5;
                }
                Integer num6 = hashMap.get("chat_wallpaper_gradient_rotation");
                if (num6 == null) {
                    num6 = 45;
                }
                String lowerCase = String.format("%02x%02x%02x", Integer.valueOf(((byte) (num2.intValue() >> 16)) & 255), Integer.valueOf(((byte) (num2.intValue() >> 8)) & 255), Byte.valueOf((byte) (num2.intValue() & 255))).toLowerCase();
                String lowerCase2 = num3.intValue() != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (num3.intValue() >> 16)) & 255), Integer.valueOf(((byte) (num3.intValue() >> 8)) & 255), Byte.valueOf((byte) (num3.intValue() & 255))).toLowerCase() : null;
                String lowerCase3 = num4.intValue() != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (num4.intValue() >> 16)) & 255), Integer.valueOf(((byte) (num4.intValue() >> 8)) & 255), Byte.valueOf((byte) (num4.intValue() & 255))).toLowerCase() : null;
                String lowerCase4 = num.intValue() != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (num.intValue() >> 16)) & 255), Integer.valueOf(((byte) (num.intValue() >> 8)) & 255), Byte.valueOf((byte) (num.intValue() & 255))).toLowerCase() : null;
                if (lowerCase2 == null || lowerCase3 == null) {
                    if (lowerCase2 != null) {
                        lowerCase = (lowerCase + "-" + lowerCase2) + "&rotation=" + num6;
                    }
                } else if (lowerCase4 != null) {
                    lowerCase = lowerCase + "~" + lowerCase2 + "~" + lowerCase3 + "~" + lowerCase4;
                } else {
                    lowerCase = lowerCase + "~" + lowerCase2 + "~" + lowerCase3;
                }
                str = "https://attheme.org?slug=" + this.patternSlug + "&intensity=" + ((int) (this.patternIntensity * 100.0f)) + "&bg_color=" + lowerCase;
                if (sb.length() > 0) {
                    str = str + "&mode=" + sb.toString();
                }
            } else {
                str = null;
            }
            StringBuilder sb2 = new StringBuilder();
            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                try {
                    String key = entry.getKey();
                    if (str == null || (!"chat_wallpaper".equals(key) && !"chat_wallpaper_gradient_to".equals(key) && !"key_chat_wallpaper_gradient_to2".equals(key) && !"key_chat_wallpaper_gradient_to3".equals(key))) {
                        sb2.append(key);
                        sb2.append("=");
                        sb2.append(entry.getValue());
                        sb2.append("\n");
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }
            try {
                fileOutputStream2 = new FileOutputStream(file);
            } catch (Exception e3) {
                e = e3;
                fileOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = null;
            }
            try {
                fileOutputStream2.write(AndroidUtilities.getStringBytes(sb2.toString()));
                if (!TextUtils.isEmpty(str)) {
                    fileOutputStream2.write(AndroidUtilities.getStringBytes("WLS=" + str + "\n"));
                }
                fileOutputStream2.close();
            } catch (Exception e4) {
                e = e4;
                fileOutputStream = fileOutputStream2;
                try {
                    FileLog.e(e);
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    return file;
                } catch (Throwable th3) {
                    th = th3;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (Exception e5) {
                            FileLog.e(e5);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                fileOutputStream = fileOutputStream2;
                if (fileOutputStream != null) {
                }
                throw th;
            }
            return file;
        }
    }

    public static int blendOver(int i, int i2) {
        float alpha = Color.alpha(i2) / 255.0f;
        float alpha2 = Color.alpha(i) / 255.0f;
        float f = 1.0f - alpha;
        float f2 = (alpha2 * f) + alpha;
        if (f2 == 0.0f) {
            return 0;
        }
        return Color.argb((int) (255.0f * f2), (int) (((Color.red(i2) * alpha) + ((Color.red(i) * alpha2) * f)) / f2), (int) (((Color.green(i2) * alpha) + ((Color.green(i) * alpha2) * f)) / f2), (int) (((Color.blue(i2) * alpha) + ((Color.blue(i) * alpha2) * f)) / f2));
    }

    /* loaded from: classes3.dex */
    public static class OverrideWallpaperInfo {
        public long accessHash;
        public int color;
        public String fileName;
        public int gradientColor1;
        public int gradientColor2;
        public int gradientColor3;
        public float intensity;
        public boolean isBlurred;
        public boolean isMotion;
        public String originalFileName;
        public ThemeAccent parentAccent;
        public ThemeInfo parentTheme;
        public int rotation;
        public String slug;
        public long wallpaperId;

        public OverrideWallpaperInfo() {
            this.fileName = "";
            this.originalFileName = "";
            this.slug = "";
        }

        public OverrideWallpaperInfo(OverrideWallpaperInfo overrideWallpaperInfo, ThemeInfo themeInfo, ThemeAccent themeAccent) {
            this.fileName = "";
            this.originalFileName = "";
            this.slug = "";
            this.slug = overrideWallpaperInfo.slug;
            this.color = overrideWallpaperInfo.color;
            this.gradientColor1 = overrideWallpaperInfo.gradientColor1;
            this.gradientColor2 = overrideWallpaperInfo.gradientColor2;
            this.gradientColor3 = overrideWallpaperInfo.gradientColor3;
            this.rotation = overrideWallpaperInfo.rotation;
            this.isBlurred = overrideWallpaperInfo.isBlurred;
            this.isMotion = overrideWallpaperInfo.isMotion;
            this.intensity = overrideWallpaperInfo.intensity;
            this.parentTheme = themeInfo;
            this.parentAccent = themeAccent;
            if (!TextUtils.isEmpty(overrideWallpaperInfo.fileName)) {
                try {
                    File file = new File(ApplicationLoader.getFilesDirFixed(), overrideWallpaperInfo.fileName);
                    File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                    String generateWallpaperName = this.parentTheme.generateWallpaperName(this.parentAccent, false);
                    this.fileName = generateWallpaperName;
                    AndroidUtilities.copyFile(file, new File(filesDirFixed, generateWallpaperName));
                } catch (Exception e) {
                    this.fileName = "";
                    FileLog.e(e);
                }
            } else {
                this.fileName = "";
            }
            if (!TextUtils.isEmpty(overrideWallpaperInfo.originalFileName)) {
                if (!overrideWallpaperInfo.originalFileName.equals(overrideWallpaperInfo.fileName)) {
                    try {
                        File file2 = new File(ApplicationLoader.getFilesDirFixed(), overrideWallpaperInfo.originalFileName);
                        File filesDirFixed2 = ApplicationLoader.getFilesDirFixed();
                        String generateWallpaperName2 = this.parentTheme.generateWallpaperName(this.parentAccent, true);
                        this.originalFileName = generateWallpaperName2;
                        AndroidUtilities.copyFile(file2, new File(filesDirFixed2, generateWallpaperName2));
                        return;
                    } catch (Exception e2) {
                        this.originalFileName = "";
                        FileLog.e(e2);
                        return;
                    }
                }
                this.originalFileName = this.fileName;
                return;
            }
            this.originalFileName = "";
        }

        public boolean isDefault() {
            return "d".equals(this.slug);
        }

        public boolean isColor() {
            return "c".equals(this.slug);
        }

        public boolean isTheme() {
            return "t".equals(this.slug);
        }

        public void saveOverrideWallpaper() {
            ThemeInfo themeInfo = this.parentTheme;
            if (themeInfo != null) {
                ThemeAccent themeAccent = this.parentAccent;
                if (themeAccent == null && themeInfo.overrideWallpaper != this) {
                    return;
                }
                if (themeAccent != null && themeAccent.overrideWallpaper != this) {
                    return;
                }
                save();
            }
        }

        private String getKey() {
            if (this.parentAccent != null) {
                return this.parentTheme.name + "_" + this.parentAccent.id + "_owp";
            }
            return this.parentTheme.name + "_owp";
        }

        public void save() {
            try {
                String key = getKey();
                SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0).edit();
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("wall", this.fileName);
                jSONObject.put("owall", this.originalFileName);
                jSONObject.put("pColor", this.color);
                jSONObject.put("pGrColor", this.gradientColor1);
                jSONObject.put("pGrColor2", this.gradientColor2);
                jSONObject.put("pGrColor3", this.gradientColor3);
                jSONObject.put("pGrAngle", this.rotation);
                String str = this.slug;
                if (str == null) {
                    str = "";
                }
                jSONObject.put("wallSlug", str);
                jSONObject.put("wBlur", this.isBlurred);
                jSONObject.put("wMotion", this.isMotion);
                jSONObject.put("pIntensity", this.intensity);
                edit.putString(key, jSONObject.toString());
                edit.commit();
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }

        public void delete() {
            ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0).edit().remove(getKey()).commit();
            new File(ApplicationLoader.getFilesDirFixed(), this.fileName).delete();
            new File(ApplicationLoader.getFilesDirFixed(), this.originalFileName).delete();
        }
    }

    /* loaded from: classes3.dex */
    public static class ThemeInfo implements NotificationCenter.NotificationCenterDelegate {
        public int accentBaseColor;
        public LongSparseArray<ThemeAccent> accentsByThemeId;
        public int account;
        public String assetName;
        public boolean badWallpaper;
        public LongSparseArray<ThemeAccent> chatAccentsByThemeId;
        public int currentAccentId;
        public int defaultAccentCount;
        public boolean firstAccentIsDefault;
        public TLRPC$TL_theme info;
        public boolean isBlured;
        private int isDark;
        public boolean isMotion;
        public int lastAccentId;
        public int lastChatThemeId;
        public boolean loaded;
        private String loadingThemeWallpaperName;
        public String name;
        private String newPathToWallpaper;
        public OverrideWallpaperInfo overrideWallpaper;
        public String pathToFile;
        public String pathToWallpaper;
        public int patternBgColor;
        public int patternBgGradientColor1;
        public int patternBgGradientColor2;
        public int patternBgGradientColor3;
        public int patternBgGradientRotation;
        public int patternIntensity;
        public int prevAccentId;
        private int previewBackgroundColor;
        public int previewBackgroundGradientColor1;
        public int previewBackgroundGradientColor2;
        public int previewBackgroundGradientColor3;
        private int previewInColor;
        private int previewOutColor;
        public boolean previewParsed;
        public int previewWallpaperOffset;
        public String slug;
        public int sortIndex;
        public ArrayList<ThemeAccent> themeAccents;
        public SparseArray<ThemeAccent> themeAccentsMap;
        public boolean themeLoaded;
        public TLRPC$InputFile uploadedFile;
        public TLRPC$InputFile uploadedThumb;
        public String uploadingFile;
        public String uploadingThumb;

        ThemeInfo() {
            this.patternBgGradientRotation = 45;
            this.loaded = true;
            this.themeLoaded = true;
            this.prevAccentId = -1;
            this.chatAccentsByThemeId = new LongSparseArray<>();
            this.lastChatThemeId = 0;
            this.lastAccentId = 100;
            this.isDark = -1;
        }

        public ThemeInfo(ThemeInfo themeInfo) {
            this.patternBgGradientRotation = 45;
            this.loaded = true;
            this.themeLoaded = true;
            this.prevAccentId = -1;
            this.chatAccentsByThemeId = new LongSparseArray<>();
            this.lastChatThemeId = 0;
            this.lastAccentId = 100;
            this.isDark = -1;
            this.name = themeInfo.name;
            this.pathToFile = themeInfo.pathToFile;
            this.pathToWallpaper = themeInfo.pathToWallpaper;
            this.assetName = themeInfo.assetName;
            this.slug = themeInfo.slug;
            this.badWallpaper = themeInfo.badWallpaper;
            this.isBlured = themeInfo.isBlured;
            this.isMotion = themeInfo.isMotion;
            this.patternBgColor = themeInfo.patternBgColor;
            this.patternBgGradientColor1 = themeInfo.patternBgGradientColor1;
            this.patternBgGradientColor2 = themeInfo.patternBgGradientColor2;
            this.patternBgGradientColor3 = themeInfo.patternBgGradientColor3;
            this.patternBgGradientRotation = themeInfo.patternBgGradientRotation;
            this.patternIntensity = themeInfo.patternIntensity;
            this.account = themeInfo.account;
            this.info = themeInfo.info;
            this.loaded = themeInfo.loaded;
            this.uploadingThumb = themeInfo.uploadingThumb;
            this.uploadingFile = themeInfo.uploadingFile;
            this.uploadedThumb = themeInfo.uploadedThumb;
            this.uploadedFile = themeInfo.uploadedFile;
            this.previewBackgroundColor = themeInfo.previewBackgroundColor;
            this.previewBackgroundGradientColor1 = themeInfo.previewBackgroundGradientColor1;
            this.previewBackgroundGradientColor2 = themeInfo.previewBackgroundGradientColor2;
            this.previewBackgroundGradientColor3 = themeInfo.previewBackgroundGradientColor3;
            this.previewWallpaperOffset = themeInfo.previewWallpaperOffset;
            this.previewInColor = themeInfo.previewInColor;
            this.previewOutColor = themeInfo.previewOutColor;
            this.firstAccentIsDefault = themeInfo.firstAccentIsDefault;
            this.previewParsed = themeInfo.previewParsed;
            this.themeLoaded = themeInfo.themeLoaded;
            this.sortIndex = themeInfo.sortIndex;
            this.defaultAccentCount = themeInfo.defaultAccentCount;
            this.accentBaseColor = themeInfo.accentBaseColor;
            this.currentAccentId = themeInfo.currentAccentId;
            this.prevAccentId = themeInfo.prevAccentId;
            this.themeAccentsMap = themeInfo.themeAccentsMap;
            this.themeAccents = themeInfo.themeAccents;
            this.accentsByThemeId = themeInfo.accentsByThemeId;
            this.lastAccentId = themeInfo.lastAccentId;
            this.loadingThemeWallpaperName = themeInfo.loadingThemeWallpaperName;
            this.newPathToWallpaper = themeInfo.newPathToWallpaper;
            this.overrideWallpaper = themeInfo.overrideWallpaper;
        }

        JSONObject getSaveJson() {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("name", this.name);
                jSONObject.put("path", this.pathToFile);
                jSONObject.put("account", this.account);
                TLRPC$TL_theme tLRPC$TL_theme = this.info;
                if (tLRPC$TL_theme != null) {
                    SerializedData serializedData = new SerializedData(tLRPC$TL_theme.getObjectSize());
                    this.info.serializeToStream(serializedData);
                    jSONObject.put("info", Utilities.bytesToHex(serializedData.toByteArray()));
                }
                jSONObject.put("loaded", this.loaded);
                return jSONObject;
            } catch (Exception e) {
                FileLog.e(e);
                return null;
            }
        }

        public void loadWallpapers(SharedPreferences sharedPreferences) {
            ArrayList<ThemeAccent> arrayList = this.themeAccents;
            if (arrayList != null && !arrayList.isEmpty()) {
                int size = this.themeAccents.size();
                for (int i = 0; i < size; i++) {
                    ThemeAccent themeAccent = this.themeAccents.get(i);
                    loadOverrideWallpaper(sharedPreferences, themeAccent, this.name + "_" + themeAccent.id + "_owp");
                }
                return;
            }
            loadOverrideWallpaper(sharedPreferences, null, this.name + "_owp");
        }

        private void loadOverrideWallpaper(SharedPreferences sharedPreferences, ThemeAccent themeAccent, String str) {
            try {
                String string = sharedPreferences.getString(str, null);
                if (TextUtils.isEmpty(string)) {
                    return;
                }
                JSONObject jSONObject = new JSONObject(string);
                OverrideWallpaperInfo overrideWallpaperInfo = new OverrideWallpaperInfo();
                overrideWallpaperInfo.fileName = jSONObject.getString("wall");
                overrideWallpaperInfo.originalFileName = jSONObject.getString("owall");
                overrideWallpaperInfo.color = jSONObject.getInt("pColor");
                overrideWallpaperInfo.gradientColor1 = jSONObject.getInt("pGrColor");
                overrideWallpaperInfo.gradientColor2 = jSONObject.optInt("pGrColor2");
                overrideWallpaperInfo.gradientColor3 = jSONObject.optInt("pGrColor3");
                overrideWallpaperInfo.rotation = jSONObject.getInt("pGrAngle");
                overrideWallpaperInfo.slug = jSONObject.getString("wallSlug");
                overrideWallpaperInfo.isBlurred = jSONObject.getBoolean("wBlur");
                overrideWallpaperInfo.isMotion = jSONObject.getBoolean("wMotion");
                overrideWallpaperInfo.intensity = (float) jSONObject.getDouble("pIntensity");
                overrideWallpaperInfo.parentTheme = this;
                overrideWallpaperInfo.parentAccent = themeAccent;
                if (themeAccent != null) {
                    themeAccent.overrideWallpaper = overrideWallpaperInfo;
                } else {
                    this.overrideWallpaper = overrideWallpaperInfo;
                }
                if (!jSONObject.has("wallId") || jSONObject.getLong("wallId") != 1000001) {
                    return;
                }
                overrideWallpaperInfo.slug = "d";
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }

        public void setOverrideWallpaper(OverrideWallpaperInfo overrideWallpaperInfo) {
            if (this.overrideWallpaper == overrideWallpaperInfo) {
                return;
            }
            ThemeAccent accent = getAccent(false);
            OverrideWallpaperInfo overrideWallpaperInfo2 = this.overrideWallpaper;
            if (overrideWallpaperInfo2 != null) {
                overrideWallpaperInfo2.delete();
            }
            if (overrideWallpaperInfo != null) {
                overrideWallpaperInfo.parentAccent = accent;
                overrideWallpaperInfo.parentTheme = this;
                overrideWallpaperInfo.save();
            }
            this.overrideWallpaper = overrideWallpaperInfo;
            if (accent == null) {
                return;
            }
            accent.overrideWallpaper = overrideWallpaperInfo;
        }

        public String getName() {
            if ("Blue".equals(this.name)) {
                return LocaleController.getString("ThemeClassic", R.string.ThemeClassic);
            }
            if ("Dark Blue".equals(this.name)) {
                return LocaleController.getString("ThemeDark", R.string.ThemeDark);
            }
            if ("Arctic Blue".equals(this.name)) {
                return LocaleController.getString("ThemeArcticBlue", R.string.ThemeArcticBlue);
            }
            if ("Day".equals(this.name)) {
                return LocaleController.getString("ThemeDay", R.string.ThemeDay);
            }
            if ("Night".equals(this.name)) {
                return LocaleController.getString("ThemeNight", R.string.ThemeNight);
            }
            TLRPC$TL_theme tLRPC$TL_theme = this.info;
            return tLRPC$TL_theme != null ? tLRPC$TL_theme.title : this.name;
        }

        public void setCurrentAccentId(int i) {
            this.currentAccentId = i;
            ThemeAccent accent = getAccent(false);
            if (accent != null) {
                this.overrideWallpaper = accent.overrideWallpaper;
            }
        }

        public String generateWallpaperName(ThemeAccent themeAccent, boolean z) {
            StringBuilder sb;
            StringBuilder sb2;
            if (themeAccent == null) {
                themeAccent = getAccent(false);
            }
            if (themeAccent != null) {
                StringBuilder sb3 = new StringBuilder();
                if (z) {
                    sb2 = new StringBuilder();
                    sb2.append(this.name);
                    sb2.append("_");
                    sb2.append(themeAccent.id);
                    sb2.append("_wp_o");
                } else {
                    sb2 = new StringBuilder();
                    sb2.append(this.name);
                    sb2.append("_");
                    sb2.append(themeAccent.id);
                    sb2.append("_wp");
                }
                sb3.append(sb2.toString());
                sb3.append(Utilities.random.nextInt());
                sb3.append(".jpg");
                return sb3.toString();
            }
            StringBuilder sb4 = new StringBuilder();
            if (z) {
                sb = new StringBuilder();
                sb.append(this.name);
                sb.append("_wp_o");
            } else {
                sb = new StringBuilder();
                sb.append(this.name);
                sb.append("_wp");
            }
            sb4.append(sb.toString());
            sb4.append(Utilities.random.nextInt());
            sb4.append(".jpg");
            return sb4.toString();
        }

        public void setPreviewInColor(int i) {
            this.previewInColor = i;
        }

        public void setPreviewOutColor(int i) {
            this.previewOutColor = i;
        }

        public void setPreviewBackgroundColor(int i) {
            this.previewBackgroundColor = i;
        }

        public int getPreviewInColor() {
            if (!this.firstAccentIsDefault || this.currentAccentId != Theme.DEFALT_THEME_ACCENT_ID) {
                return this.previewInColor;
            }
            return -1;
        }

        public int getPreviewOutColor() {
            if (!this.firstAccentIsDefault || this.currentAccentId != Theme.DEFALT_THEME_ACCENT_ID) {
                return this.previewOutColor;
            }
            return -983328;
        }

        public int getPreviewBackgroundColor() {
            if (!this.firstAccentIsDefault || this.currentAccentId != Theme.DEFALT_THEME_ACCENT_ID) {
                return this.previewBackgroundColor;
            }
            return -3155485;
        }

        public boolean isDefaultMyMessagesBubbles() {
            if (!this.firstAccentIsDefault) {
                return false;
            }
            int i = this.currentAccentId;
            int i2 = Theme.DEFALT_THEME_ACCENT_ID;
            if (i == i2) {
                return true;
            }
            ThemeAccent themeAccent = this.themeAccentsMap.get(i2);
            ThemeAccent themeAccent2 = this.themeAccentsMap.get(this.currentAccentId);
            return themeAccent != null && themeAccent2 != null && themeAccent.myMessagesAccentColor == themeAccent2.myMessagesAccentColor && themeAccent.myMessagesGradientAccentColor1 == themeAccent2.myMessagesGradientAccentColor1 && themeAccent.myMessagesGradientAccentColor2 == themeAccent2.myMessagesGradientAccentColor2 && themeAccent.myMessagesGradientAccentColor3 == themeAccent2.myMessagesGradientAccentColor3 && themeAccent.myMessagesAnimated == themeAccent2.myMessagesAnimated;
        }

        public boolean isDefaultMyMessages() {
            if (!this.firstAccentIsDefault) {
                return false;
            }
            int i = this.currentAccentId;
            int i2 = Theme.DEFALT_THEME_ACCENT_ID;
            if (i == i2) {
                return true;
            }
            ThemeAccent themeAccent = this.themeAccentsMap.get(i2);
            ThemeAccent themeAccent2 = this.themeAccentsMap.get(this.currentAccentId);
            return themeAccent != null && themeAccent2 != null && themeAccent.accentColor2 == themeAccent2.accentColor2 && themeAccent.myMessagesAccentColor == themeAccent2.myMessagesAccentColor && themeAccent.myMessagesGradientAccentColor1 == themeAccent2.myMessagesGradientAccentColor1 && themeAccent.myMessagesGradientAccentColor2 == themeAccent2.myMessagesGradientAccentColor2 && themeAccent.myMessagesGradientAccentColor3 == themeAccent2.myMessagesGradientAccentColor3 && themeAccent.myMessagesAnimated == themeAccent2.myMessagesAnimated;
        }

        public boolean isDefaultMainAccent() {
            if (!this.firstAccentIsDefault) {
                return false;
            }
            int i = this.currentAccentId;
            int i2 = Theme.DEFALT_THEME_ACCENT_ID;
            if (i == i2) {
                return true;
            }
            ThemeAccent themeAccent = this.themeAccentsMap.get(i2);
            ThemeAccent themeAccent2 = this.themeAccentsMap.get(this.currentAccentId);
            return (themeAccent2 == null || themeAccent == null || themeAccent.accentColor != themeAccent2.accentColor) ? false : true;
        }

        public boolean hasAccentColors() {
            return this.defaultAccentCount != 0;
        }

        public boolean isDark() {
            int i = this.isDark;
            if (i != -1) {
                return i == 1;
            }
            if ("Dark Blue".equals(this.name) || "Night".equals(this.name)) {
                this.isDark = 1;
            } else if ("Blue".equals(this.name) || "Arctic Blue".equals(this.name) || "Day".equals(this.name)) {
                this.isDark = 0;
            }
            if (this.isDark == -1) {
                Theme.checkIsDark(Theme.getThemeFileValues(new File(this.pathToFile), null, new String[1]), this);
            }
            return this.isDark == 1;
        }

        public boolean isLight() {
            return this.pathToFile == null && !isDark();
        }

        public String getKey() {
            if (this.info != null) {
                return "remote" + this.info.id;
            }
            return this.name;
        }

        static ThemeInfo createWithJson(JSONObject jSONObject) {
            if (jSONObject == null) {
                return null;
            }
            try {
                ThemeInfo themeInfo = new ThemeInfo();
                themeInfo.name = jSONObject.getString("name");
                themeInfo.pathToFile = jSONObject.getString("path");
                if (jSONObject.has("account")) {
                    themeInfo.account = jSONObject.getInt("account");
                }
                if (jSONObject.has("info")) {
                    SerializedData serializedData = new SerializedData(Utilities.hexToBytes(jSONObject.getString("info")));
                    themeInfo.info = TLRPC$Theme.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                }
                if (jSONObject.has("loaded")) {
                    themeInfo.loaded = jSONObject.getBoolean("loaded");
                }
                return themeInfo;
            } catch (Exception e) {
                FileLog.e(e);
                return null;
            }
        }

        static ThemeInfo createWithString(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            String[] split = str.split("\\|");
            if (split.length != 2) {
                return null;
            }
            ThemeInfo themeInfo = new ThemeInfo();
            themeInfo.name = split[0];
            themeInfo.pathToFile = split[1];
            return themeInfo;
        }

        public void setAccentColorOptions(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5, int[] iArr6, int[] iArr7, int[] iArr8, String[] strArr, int[] iArr9, int[] iArr10) {
            this.defaultAccentCount = iArr.length;
            this.themeAccents = new ArrayList<>();
            this.themeAccentsMap = new SparseArray<>();
            this.accentsByThemeId = new LongSparseArray<>();
            for (int i = 0; i < iArr.length; i++) {
                ThemeAccent themeAccent = new ThemeAccent();
                themeAccent.id = iArr8 != null ? iArr8[i] : i;
                if (Theme.isHome(themeAccent)) {
                    themeAccent.isDefault = true;
                }
                themeAccent.accentColor = iArr[i];
                themeAccent.parentTheme = this;
                if (iArr2 != null) {
                    themeAccent.myMessagesAccentColor = iArr2[i];
                }
                if (iArr3 != null) {
                    themeAccent.myMessagesGradientAccentColor1 = iArr3[i];
                }
                if (iArr4 != null) {
                    themeAccent.backgroundOverrideColor = iArr4[i];
                    if (this.firstAccentIsDefault && themeAccent.id == Theme.DEFALT_THEME_ACCENT_ID) {
                        themeAccent.backgroundOverrideColor = 4294967296L;
                    } else {
                        themeAccent.backgroundOverrideColor = iArr4[i];
                    }
                }
                if (iArr5 != null) {
                    if (this.firstAccentIsDefault && themeAccent.id == Theme.DEFALT_THEME_ACCENT_ID) {
                        themeAccent.backgroundGradientOverrideColor1 = 4294967296L;
                    } else {
                        themeAccent.backgroundGradientOverrideColor1 = iArr5[i];
                    }
                }
                if (iArr6 != null) {
                    if (this.firstAccentIsDefault && themeAccent.id == Theme.DEFALT_THEME_ACCENT_ID) {
                        themeAccent.backgroundGradientOverrideColor2 = 4294967296L;
                    } else {
                        themeAccent.backgroundGradientOverrideColor2 = iArr6[i];
                    }
                }
                if (iArr7 != null) {
                    if (this.firstAccentIsDefault && themeAccent.id == Theme.DEFALT_THEME_ACCENT_ID) {
                        themeAccent.backgroundGradientOverrideColor3 = 4294967296L;
                    } else {
                        themeAccent.backgroundGradientOverrideColor3 = iArr7[i];
                    }
                }
                if (strArr != null) {
                    themeAccent.patternIntensity = iArr10[i] / 100.0f;
                    themeAccent.backgroundRotation = iArr9[i];
                    themeAccent.patternSlug = strArr[i];
                }
                this.themeAccentsMap.put(themeAccent.id, themeAccent);
                this.themeAccents.add(themeAccent);
            }
            this.accentBaseColor = this.themeAccentsMap.get(0).accentColor;
        }

        public void loadThemeDocument() {
            this.loaded = false;
            this.loadingThemeWallpaperName = null;
            this.newPathToWallpaper = null;
            addObservers();
            FileLoader fileLoader = FileLoader.getInstance(this.account);
            TLRPC$TL_theme tLRPC$TL_theme = this.info;
            fileLoader.loadFile(tLRPC$TL_theme.document, tLRPC$TL_theme, 1, 1);
        }

        private void addObservers() {
            NotificationCenter.getInstance(this.account).addObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.account).addObserver(this, NotificationCenter.fileLoadFailed);
        }

        public void removeObservers() {
            NotificationCenter.getInstance(this.account).removeObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.account).removeObserver(this, NotificationCenter.fileLoadFailed);
        }

        public void onFinishLoadingRemoteTheme() {
            this.loaded = true;
            boolean z = false;
            this.previewParsed = false;
            Theme.saveOtherThemes(true);
            if (this == Theme.currentTheme && Theme.previousTheme == null) {
                NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
                int i = NotificationCenter.needSetDayNightTheme;
                Object[] objArr = new Object[5];
                objArr[0] = this;
                if (this == Theme.currentNightTheme) {
                    z = true;
                }
                objArr[1] = Boolean.valueOf(z);
                objArr[2] = null;
                objArr[3] = -1;
                objArr[4] = Theme.fallbackKeys;
                globalInstance.postNotificationName(i, objArr);
            }
        }

        public static boolean accentEquals(ThemeAccent themeAccent, TLRPC$ThemeSettings tLRPC$ThemeSettings) {
            long j;
            long j2;
            int i;
            float f;
            String str;
            int i2;
            TLRPC$WallPaperSettings tLRPC$WallPaperSettings;
            int i3;
            int i4;
            int i5;
            long j3;
            int intValue = tLRPC$ThemeSettings.message_colors.size() > 0 ? tLRPC$ThemeSettings.message_colors.get(0).intValue() | (-16777216) : 0;
            int intValue2 = tLRPC$ThemeSettings.message_colors.size() > 1 ? tLRPC$ThemeSettings.message_colors.get(1).intValue() | (-16777216) : 0;
            if (intValue == intValue2) {
                intValue2 = 0;
            }
            int intValue3 = tLRPC$ThemeSettings.message_colors.size() > 2 ? tLRPC$ThemeSettings.message_colors.get(2).intValue() | (-16777216) : 0;
            int intValue4 = tLRPC$ThemeSettings.message_colors.size() > 3 ? (-16777216) | tLRPC$ThemeSettings.message_colors.get(3).intValue() : 0;
            TLRPC$WallPaper tLRPC$WallPaper = tLRPC$ThemeSettings.wallpaper;
            long j4 = 0;
            if (tLRPC$WallPaper == null || (tLRPC$WallPaperSettings = tLRPC$WallPaper.settings) == null) {
                j2 = 0;
                j = 0;
                i2 = 0;
                str = null;
                f = 0.0f;
                i = 0;
            } else {
                i = Theme.getWallpaperColor(tLRPC$WallPaperSettings.background_color);
                long wallpaperColor = tLRPC$ThemeSettings.wallpaper.settings.second_background_color == 0 ? 4294967296L : Theme.getWallpaperColor(i3);
                long wallpaperColor2 = tLRPC$ThemeSettings.wallpaper.settings.third_background_color == 0 ? 4294967296L : Theme.getWallpaperColor(i4);
                long wallpaperColor3 = tLRPC$ThemeSettings.wallpaper.settings.fourth_background_color == 0 ? 4294967296L : Theme.getWallpaperColor(i5);
                int wallpaperRotation = AndroidUtilities.getWallpaperRotation(tLRPC$ThemeSettings.wallpaper.settings.rotation, false);
                TLRPC$WallPaper tLRPC$WallPaper2 = tLRPC$ThemeSettings.wallpaper;
                if ((tLRPC$WallPaper2 instanceof TLRPC$TL_wallPaperNoFile) || !tLRPC$WallPaper2.pattern) {
                    i2 = wallpaperRotation;
                    j3 = wallpaperColor2;
                    str = null;
                    f = 0.0f;
                } else {
                    j3 = wallpaperColor2;
                    f = tLRPC$WallPaper2.settings.intensity / 100.0f;
                    str = tLRPC$WallPaper2.slug;
                    i2 = wallpaperRotation;
                }
                long j5 = wallpaperColor3;
                j4 = wallpaperColor;
                j2 = j3;
                j = j5;
            }
            return tLRPC$ThemeSettings.accent_color == themeAccent.accentColor && tLRPC$ThemeSettings.outbox_accent_color == themeAccent.accentColor2 && intValue == themeAccent.myMessagesAccentColor && intValue2 == themeAccent.myMessagesGradientAccentColor1 && intValue3 == themeAccent.myMessagesGradientAccentColor2 && intValue4 == themeAccent.myMessagesGradientAccentColor3 && tLRPC$ThemeSettings.message_colors_animated == themeAccent.myMessagesAnimated && ((long) i) == themeAccent.backgroundOverrideColor && j4 == themeAccent.backgroundGradientOverrideColor1 && j2 == themeAccent.backgroundGradientOverrideColor2 && j == themeAccent.backgroundGradientOverrideColor3 && i2 == themeAccent.backgroundRotation && TextUtils.equals(str, themeAccent.patternSlug) && ((double) Math.abs(f - themeAccent.patternIntensity)) < 0.001d;
        }

        public static void fillAccentValues(ThemeAccent themeAccent, TLRPC$ThemeSettings tLRPC$ThemeSettings) {
            TLRPC$WallPaperSettings tLRPC$WallPaperSettings;
            themeAccent.accentColor = tLRPC$ThemeSettings.accent_color;
            themeAccent.accentColor2 = tLRPC$ThemeSettings.outbox_accent_color;
            themeAccent.myMessagesAccentColor = tLRPC$ThemeSettings.message_colors.size() > 0 ? tLRPC$ThemeSettings.message_colors.get(0).intValue() | (-16777216) : 0;
            int intValue = tLRPC$ThemeSettings.message_colors.size() > 1 ? tLRPC$ThemeSettings.message_colors.get(1).intValue() | (-16777216) : 0;
            themeAccent.myMessagesGradientAccentColor1 = intValue;
            if (themeAccent.myMessagesAccentColor == intValue) {
                themeAccent.myMessagesGradientAccentColor1 = 0;
            }
            themeAccent.myMessagesGradientAccentColor2 = tLRPC$ThemeSettings.message_colors.size() > 2 ? tLRPC$ThemeSettings.message_colors.get(2).intValue() | (-16777216) : 0;
            themeAccent.myMessagesGradientAccentColor3 = tLRPC$ThemeSettings.message_colors.size() > 3 ? tLRPC$ThemeSettings.message_colors.get(3).intValue() | (-16777216) : 0;
            themeAccent.myMessagesAnimated = tLRPC$ThemeSettings.message_colors_animated;
            TLRPC$WallPaper tLRPC$WallPaper = tLRPC$ThemeSettings.wallpaper;
            if (tLRPC$WallPaper == null || (tLRPC$WallPaperSettings = tLRPC$WallPaper.settings) == null) {
                return;
            }
            int i = tLRPC$WallPaperSettings.background_color;
            if (i == 0) {
                themeAccent.backgroundOverrideColor = 4294967296L;
            } else {
                themeAccent.backgroundOverrideColor = Theme.getWallpaperColor(i);
            }
            TLRPC$WallPaperSettings tLRPC$WallPaperSettings2 = tLRPC$ThemeSettings.wallpaper.settings;
            if ((tLRPC$WallPaperSettings2.flags & 16) != 0 && tLRPC$WallPaperSettings2.second_background_color == 0) {
                themeAccent.backgroundGradientOverrideColor1 = 4294967296L;
            } else {
                themeAccent.backgroundGradientOverrideColor1 = Theme.getWallpaperColor(tLRPC$WallPaperSettings2.second_background_color);
            }
            TLRPC$WallPaperSettings tLRPC$WallPaperSettings3 = tLRPC$ThemeSettings.wallpaper.settings;
            if ((tLRPC$WallPaperSettings3.flags & 32) != 0 && tLRPC$WallPaperSettings3.third_background_color == 0) {
                themeAccent.backgroundGradientOverrideColor2 = 4294967296L;
            } else {
                themeAccent.backgroundGradientOverrideColor2 = Theme.getWallpaperColor(tLRPC$WallPaperSettings3.third_background_color);
            }
            TLRPC$WallPaperSettings tLRPC$WallPaperSettings4 = tLRPC$ThemeSettings.wallpaper.settings;
            if ((tLRPC$WallPaperSettings4.flags & 64) != 0 && tLRPC$WallPaperSettings4.fourth_background_color == 0) {
                themeAccent.backgroundGradientOverrideColor3 = 4294967296L;
            } else {
                themeAccent.backgroundGradientOverrideColor3 = Theme.getWallpaperColor(tLRPC$WallPaperSettings4.fourth_background_color);
            }
            themeAccent.backgroundRotation = AndroidUtilities.getWallpaperRotation(tLRPC$ThemeSettings.wallpaper.settings.rotation, false);
            TLRPC$WallPaper tLRPC$WallPaper2 = tLRPC$ThemeSettings.wallpaper;
            if ((tLRPC$WallPaper2 instanceof TLRPC$TL_wallPaperNoFile) || !tLRPC$WallPaper2.pattern) {
                return;
            }
            themeAccent.patternSlug = tLRPC$WallPaper2.slug;
            TLRPC$WallPaperSettings tLRPC$WallPaperSettings5 = tLRPC$WallPaper2.settings;
            themeAccent.patternIntensity = tLRPC$WallPaperSettings5.intensity / 100.0f;
            themeAccent.patternMotion = tLRPC$WallPaperSettings5.motion;
        }

        public ThemeAccent createNewAccent(TLRPC$ThemeSettings tLRPC$ThemeSettings) {
            ThemeAccent themeAccent = new ThemeAccent();
            fillAccentValues(themeAccent, tLRPC$ThemeSettings);
            themeAccent.parentTheme = this;
            return themeAccent;
        }

        public ThemeAccent createNewAccent(TLRPC$TL_theme tLRPC$TL_theme, int i) {
            return createNewAccent(tLRPC$TL_theme, i, false, 0);
        }

        public ThemeAccent createNewAccent(TLRPC$TL_theme tLRPC$TL_theme, int i, boolean z, int i2) {
            TLRPC$ThemeSettings tLRPC$ThemeSettings = null;
            if (tLRPC$TL_theme == null) {
                return null;
            }
            if (i2 < tLRPC$TL_theme.settings.size()) {
                tLRPC$ThemeSettings = tLRPC$TL_theme.settings.get(i2);
            }
            if (z) {
                ThemeAccent themeAccent = this.chatAccentsByThemeId.get(tLRPC$TL_theme.id);
                if (themeAccent != null) {
                    return themeAccent;
                }
                int i3 = this.lastChatThemeId + 1;
                this.lastChatThemeId = i3;
                ThemeAccent createNewAccent = createNewAccent(tLRPC$ThemeSettings);
                createNewAccent.id = i3;
                createNewAccent.info = tLRPC$TL_theme;
                createNewAccent.account = i;
                this.chatAccentsByThemeId.put(i3, createNewAccent);
                return createNewAccent;
            }
            ThemeAccent themeAccent2 = this.accentsByThemeId.get(tLRPC$TL_theme.id);
            if (themeAccent2 != null) {
                return themeAccent2;
            }
            int i4 = this.lastAccentId + 1;
            this.lastAccentId = i4;
            ThemeAccent createNewAccent2 = createNewAccent(tLRPC$ThemeSettings);
            createNewAccent2.id = i4;
            createNewAccent2.info = tLRPC$TL_theme;
            createNewAccent2.account = i;
            this.themeAccentsMap.put(i4, createNewAccent2);
            this.themeAccents.add(0, createNewAccent2);
            Theme.sortAccents(this);
            this.accentsByThemeId.put(tLRPC$TL_theme.id, createNewAccent2);
            return createNewAccent2;
        }

        public ThemeAccent getAccent(boolean z) {
            ThemeAccent themeAccent;
            if (this.themeAccents == null || (themeAccent = this.themeAccentsMap.get(this.currentAccentId)) == null) {
                return null;
            }
            if (!z) {
                return themeAccent;
            }
            int i = this.lastAccentId + 1;
            this.lastAccentId = i;
            ThemeAccent themeAccent2 = new ThemeAccent();
            themeAccent2.accentColor = themeAccent.accentColor;
            themeAccent2.accentColor2 = themeAccent.accentColor2;
            themeAccent2.myMessagesAccentColor = themeAccent.myMessagesAccentColor;
            themeAccent2.myMessagesGradientAccentColor1 = themeAccent.myMessagesGradientAccentColor1;
            themeAccent2.myMessagesGradientAccentColor2 = themeAccent.myMessagesGradientAccentColor2;
            themeAccent2.myMessagesGradientAccentColor3 = themeAccent.myMessagesGradientAccentColor3;
            themeAccent2.myMessagesAnimated = themeAccent.myMessagesAnimated;
            themeAccent2.backgroundOverrideColor = themeAccent.backgroundOverrideColor;
            themeAccent2.backgroundGradientOverrideColor1 = themeAccent.backgroundGradientOverrideColor1;
            themeAccent2.backgroundGradientOverrideColor2 = themeAccent.backgroundGradientOverrideColor2;
            themeAccent2.backgroundGradientOverrideColor3 = themeAccent.backgroundGradientOverrideColor3;
            themeAccent2.backgroundRotation = themeAccent.backgroundRotation;
            themeAccent2.patternSlug = themeAccent.patternSlug;
            themeAccent2.patternIntensity = themeAccent.patternIntensity;
            themeAccent2.patternMotion = themeAccent.patternMotion;
            themeAccent2.parentTheme = this;
            OverrideWallpaperInfo overrideWallpaperInfo = this.overrideWallpaper;
            if (overrideWallpaperInfo != null) {
                themeAccent2.overrideWallpaper = new OverrideWallpaperInfo(overrideWallpaperInfo, this, themeAccent2);
            }
            this.prevAccentId = this.currentAccentId;
            themeAccent2.id = i;
            this.currentAccentId = i;
            this.overrideWallpaper = themeAccent2.overrideWallpaper;
            this.themeAccentsMap.put(i, themeAccent2);
            this.themeAccents.add(0, themeAccent2);
            Theme.sortAccents(this);
            return themeAccent2;
        }

        public int getAccentColor(int i) {
            ThemeAccent themeAccent = this.themeAccentsMap.get(i);
            if (themeAccent != null) {
                return themeAccent.accentColor;
            }
            return 0;
        }

        public boolean createBackground(File file, String str) {
            int i;
            try {
                Bitmap scaledBitmap = AndroidUtilities.getScaledBitmap(AndroidUtilities.dp(640.0f), AndroidUtilities.dp(360.0f), file.getAbsolutePath(), null, 0);
                if (scaledBitmap != null && this.patternBgColor != 0) {
                    Bitmap createBitmap = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), scaledBitmap.getConfig());
                    Canvas canvas = new Canvas(createBitmap);
                    int i2 = this.patternBgGradientColor2;
                    if (i2 != 0) {
                        i = MotionBackgroundDrawable.getPatternColor(this.patternBgColor, this.patternBgGradientColor1, i2, this.patternBgGradientColor3);
                    } else {
                        int i3 = this.patternBgGradientColor1;
                        if (i3 != 0) {
                            i = AndroidUtilities.getAverageColor(this.patternBgColor, i3);
                            GradientDrawable gradientDrawable = new GradientDrawable(BackgroundGradientDrawable.getGradientOrientation(this.patternBgGradientRotation), new int[]{this.patternBgColor, this.patternBgGradientColor1});
                            gradientDrawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                            gradientDrawable.draw(canvas);
                        } else {
                            i = AndroidUtilities.getPatternColor(this.patternBgColor);
                            canvas.drawColor(this.patternBgColor);
                        }
                    }
                    Paint paint = new Paint(2);
                    paint.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
                    paint.setAlpha((int) ((this.patternIntensity / 100.0f) * 255.0f));
                    canvas.drawBitmap(scaledBitmap, 0.0f, 0.0f, paint);
                    canvas.setBitmap(null);
                    scaledBitmap = createBitmap;
                }
                if (this.isBlured) {
                    scaledBitmap = Utilities.blurWallpaper(scaledBitmap);
                }
                FileOutputStream fileOutputStream = new FileOutputStream(str);
                scaledBitmap.compress(this.patternBgGradientColor2 != 0 ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 87, fileOutputStream);
                fileOutputStream.close();
                return true;
            } catch (Throwable th) {
                FileLog.e(th);
                return false;
            }
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            int i3 = NotificationCenter.fileLoaded;
            if (i == i3 || i == NotificationCenter.fileLoadFailed) {
                String str = (String) objArr[0];
                TLRPC$TL_theme tLRPC$TL_theme = this.info;
                if (tLRPC$TL_theme == null || tLRPC$TL_theme.document == null) {
                    return;
                }
                if (str.equals(this.loadingThemeWallpaperName)) {
                    this.loadingThemeWallpaperName = null;
                    final File file = (File) objArr[1];
                    Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$ThemeInfo$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            Theme.ThemeInfo.this.lambda$didReceivedNotification$0(file);
                        }
                    });
                } else if (!str.equals(FileLoader.getAttachFileName(this.info.document))) {
                } else {
                    removeObservers();
                    if (i != i3) {
                        return;
                    }
                    File file2 = new File(this.pathToFile);
                    TLRPC$TL_theme tLRPC$TL_theme2 = this.info;
                    final ThemeInfo fillThemeValues = Theme.fillThemeValues(file2, tLRPC$TL_theme2.title, tLRPC$TL_theme2);
                    if (fillThemeValues != null && fillThemeValues.pathToWallpaper != null && !new File(fillThemeValues.pathToWallpaper).exists()) {
                        this.patternBgColor = fillThemeValues.patternBgColor;
                        this.patternBgGradientColor1 = fillThemeValues.patternBgGradientColor1;
                        this.patternBgGradientColor2 = fillThemeValues.patternBgGradientColor2;
                        this.patternBgGradientColor3 = fillThemeValues.patternBgGradientColor3;
                        this.patternBgGradientRotation = fillThemeValues.patternBgGradientRotation;
                        this.isBlured = fillThemeValues.isBlured;
                        this.patternIntensity = fillThemeValues.patternIntensity;
                        this.newPathToWallpaper = fillThemeValues.pathToWallpaper;
                        TLRPC$TL_account_getWallPaper tLRPC$TL_account_getWallPaper = new TLRPC$TL_account_getWallPaper();
                        TLRPC$TL_inputWallPaperSlug tLRPC$TL_inputWallPaperSlug = new TLRPC$TL_inputWallPaperSlug();
                        tLRPC$TL_inputWallPaperSlug.slug = fillThemeValues.slug;
                        tLRPC$TL_account_getWallPaper.wallpaper = tLRPC$TL_inputWallPaperSlug;
                        ConnectionsManager.getInstance(fillThemeValues.account).sendRequest(tLRPC$TL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.ActionBar.Theme$ThemeInfo$$ExternalSyntheticLambda3
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                Theme.ThemeInfo.this.lambda$didReceivedNotification$2(fillThemeValues, tLObject, tLRPC$TL_error);
                            }
                        });
                        return;
                    }
                    onFinishLoadingRemoteTheme();
                }
            }
        }

        public /* synthetic */ void lambda$didReceivedNotification$0(File file) {
            createBackground(file, this.newPathToWallpaper);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$ThemeInfo$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Theme.ThemeInfo.this.onFinishLoadingRemoteTheme();
                }
            });
        }

        public /* synthetic */ void lambda$didReceivedNotification$2(final ThemeInfo themeInfo, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$ThemeInfo$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    Theme.ThemeInfo.this.lambda$didReceivedNotification$1(tLObject, themeInfo);
                }
            });
        }

        public /* synthetic */ void lambda$didReceivedNotification$1(TLObject tLObject, ThemeInfo themeInfo) {
            if (tLObject instanceof TLRPC$TL_wallPaper) {
                TLRPC$TL_wallPaper tLRPC$TL_wallPaper = (TLRPC$TL_wallPaper) tLObject;
                this.loadingThemeWallpaperName = FileLoader.getAttachFileName(tLRPC$TL_wallPaper.document);
                addObservers();
                FileLoader.getInstance(themeInfo.account).loadFile(tLRPC$TL_wallPaper.document, tLRPC$TL_wallPaper, 1, 1);
                return;
            }
            onFinishLoadingRemoteTheme();
        }
    }

    /* loaded from: classes3.dex */
    public interface ResourcesProvider {
        void applyServiceShaderMatrix(int i, int i2, float f, float f2);

        Integer getColor(String str);

        int getColorOrDefault(String str);

        Integer getCurrentColor(String str);

        Drawable getDrawable(String str);

        Paint getPaint(String str);

        boolean hasGradientService();

        void setAnimatedColor(String str, int i);

        /* renamed from: org.telegram.ui.ActionBar.Theme$ResourcesProvider$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
            public static Drawable $default$getDrawable(ResourcesProvider resourcesProvider, String str) {
                return null;
            }

            public static Paint $default$getPaint(ResourcesProvider resourcesProvider, String str) {
                return null;
            }

            public static boolean $default$hasGradientService(ResourcesProvider resourcesProvider) {
                return false;
            }

            public static void $default$setAnimatedColor(ResourcesProvider resourcesProvider, String str, int i) {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:144:0x37a6 A[Catch: Exception -> 0x3977, TryCatch #2 {Exception -> 0x3977, blocks: (B:38:0x3452, B:40:0x346b, B:41:0x347a, B:43:0x3482, B:45:0x348a, B:47:0x3494, B:49:0x349c, B:50:0x34ac, B:52:0x34bb, B:53:0x34ca, B:55:0x34d2, B:57:0x34db, B:59:0x34e5, B:60:0x34e7, B:62:0x34eb, B:64:0x34f3, B:65:0x3505, B:66:0x3511, B:68:0x3517, B:70:0x3521, B:72:0x3525, B:74:0x3554, B:76:0x3558, B:125:0x368c, B:128:0x36af, B:130:0x36ba, B:132:0x36c6, B:134:0x36d2, B:135:0x36d8, B:138:0x36de, B:140:0x3785, B:142:0x37a0, B:144:0x37a6, B:145:0x37af, B:147:0x37b3, B:149:0x37bb, B:151:0x37bf, B:153:0x37c3, B:154:0x37c5, B:156:0x37cf, B:161:0x37e7, B:162:0x37ed, B:166:0x37f8, B:168:0x384f, B:169:0x3856, B:170:0x385d, B:172:0x386b, B:173:0x3872, B:174:0x3879, B:78:0x3569, B:81:0x357f, B:83:0x3594, B:84:0x359a, B:86:0x35ac, B:89:0x35bc, B:93:0x35c8, B:94:0x35d2, B:97:0x35df, B:98:0x35e6, B:101:0x35f0, B:103:0x35ff, B:106:0x3608, B:108:0x361b, B:111:0x3624, B:113:0x362b, B:114:0x363b, B:116:0x363f, B:117:0x3643, B:119:0x364e, B:121:0x365a), top: B:212:0x3452 }] */
    /* JADX WARN: Removed duplicated region for block: B:151:0x37bf A[Catch: Exception -> 0x3977, TryCatch #2 {Exception -> 0x3977, blocks: (B:38:0x3452, B:40:0x346b, B:41:0x347a, B:43:0x3482, B:45:0x348a, B:47:0x3494, B:49:0x349c, B:50:0x34ac, B:52:0x34bb, B:53:0x34ca, B:55:0x34d2, B:57:0x34db, B:59:0x34e5, B:60:0x34e7, B:62:0x34eb, B:64:0x34f3, B:65:0x3505, B:66:0x3511, B:68:0x3517, B:70:0x3521, B:72:0x3525, B:74:0x3554, B:76:0x3558, B:125:0x368c, B:128:0x36af, B:130:0x36ba, B:132:0x36c6, B:134:0x36d2, B:135:0x36d8, B:138:0x36de, B:140:0x3785, B:142:0x37a0, B:144:0x37a6, B:145:0x37af, B:147:0x37b3, B:149:0x37bb, B:151:0x37bf, B:153:0x37c3, B:154:0x37c5, B:156:0x37cf, B:161:0x37e7, B:162:0x37ed, B:166:0x37f8, B:168:0x384f, B:169:0x3856, B:170:0x385d, B:172:0x386b, B:173:0x3872, B:174:0x3879, B:78:0x3569, B:81:0x357f, B:83:0x3594, B:84:0x359a, B:86:0x35ac, B:89:0x35bc, B:93:0x35c8, B:94:0x35d2, B:97:0x35df, B:98:0x35e6, B:101:0x35f0, B:103:0x35ff, B:106:0x3608, B:108:0x361b, B:111:0x3624, B:113:0x362b, B:114:0x363b, B:116:0x363f, B:117:0x3643, B:119:0x364e, B:121:0x365a), top: B:212:0x3452 }] */
    /* JADX WARN: Removed duplicated region for block: B:152:0x37c2  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x37cf A[Catch: Exception -> 0x3977, TryCatch #2 {Exception -> 0x3977, blocks: (B:38:0x3452, B:40:0x346b, B:41:0x347a, B:43:0x3482, B:45:0x348a, B:47:0x3494, B:49:0x349c, B:50:0x34ac, B:52:0x34bb, B:53:0x34ca, B:55:0x34d2, B:57:0x34db, B:59:0x34e5, B:60:0x34e7, B:62:0x34eb, B:64:0x34f3, B:65:0x3505, B:66:0x3511, B:68:0x3517, B:70:0x3521, B:72:0x3525, B:74:0x3554, B:76:0x3558, B:125:0x368c, B:128:0x36af, B:130:0x36ba, B:132:0x36c6, B:134:0x36d2, B:135:0x36d8, B:138:0x36de, B:140:0x3785, B:142:0x37a0, B:144:0x37a6, B:145:0x37af, B:147:0x37b3, B:149:0x37bb, B:151:0x37bf, B:153:0x37c3, B:154:0x37c5, B:156:0x37cf, B:161:0x37e7, B:162:0x37ed, B:166:0x37f8, B:168:0x384f, B:169:0x3856, B:170:0x385d, B:172:0x386b, B:173:0x3872, B:174:0x3879, B:78:0x3569, B:81:0x357f, B:83:0x3594, B:84:0x359a, B:86:0x35ac, B:89:0x35bc, B:93:0x35c8, B:94:0x35d2, B:97:0x35df, B:98:0x35e6, B:101:0x35f0, B:103:0x35ff, B:106:0x3608, B:108:0x361b, B:111:0x3624, B:113:0x362b, B:114:0x363b, B:116:0x363f, B:117:0x3643, B:119:0x364e, B:121:0x365a), top: B:212:0x3452 }] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x37dd A[SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v72, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v78 */
    /* JADX WARN: Type inference failed for: r1v82 */
    static {
        boolean z;
        boolean z2;
        Iterator<ThemeInfo> it;
        SparseArray<ThemeAccent> sparseArray;
        ThemeAccent accent;
        SharedPreferences.Editor editor;
        boolean z3;
        boolean z4;
        SharedPreferences.Editor editor2;
        Iterator<ThemeInfo> it2;
        SharedPreferences sharedPreferences;
        ThemeInfo themeInfo;
        selectedAutoNightType = 0;
        autoNightBrighnessThreshold = 0.25f;
        autoNightDayStartTime = 1320;
        autoNightDayEndTime = 480;
        autoNightSunsetTime = 1320;
        autoNightLastSunCheckDay = -1;
        autoNightSunriseTime = 480;
        autoNightCityName = "";
        autoNightLocationLatitude = 10000.0d;
        autoNightLocationLongitude = 10000.0d;
        new HashSet();
        defaultColors.put("dialogBackground", -1);
        defaultColors.put("dialogBackgroundGray", -986896);
        defaultColors.put("dialogTextBlack", -14540254);
        defaultColors.put("dialogTextLink", -14255946);
        defaultColors.put("dialogLinkSelection", 862104035);
        defaultColors.put("dialogTextRed", -3319206);
        defaultColors.put("dialogTextRed2", -2213318);
        defaultColors.put("dialogTextBlue", -13660983);
        defaultColors.put("dialogTextBlue2", -12937771);
        defaultColors.put("dialogTextBlue3", -12664327);
        defaultColors.put("dialogTextBlue4", -15095832);
        defaultColors.put("dialogTextGray", -13333567);
        defaultColors.put("dialogTextGray2", -9079435);
        defaultColors.put("dialogTextGray3", -6710887);
        defaultColors.put("dialogTextGray4", -5000269);
        defaultColors.put("dialogTextHint", -6842473);
        defaultColors.put("dialogIcon", -9999504);
        defaultColors.put("dialogRedIcon", -2011827);
        defaultColors.put("dialogGrayLine", -2960686);
        defaultColors.put("dialogTopBackground", -9456923);
        defaultColors.put("dialogInputField", -2368549);
        defaultColors.put("dialogInputFieldActivated", -13129232);
        defaultColors.put("dialogCheckboxSquareBackground", -12345121);
        defaultColors.put("dialogCheckboxSquareCheck", -1);
        defaultColors.put("dialogCheckboxSquareUnchecked", -9211021);
        defaultColors.put("dialogCheckboxSquareDisabled", -5197648);
        defaultColors.put("dialogRadioBackground", -5000269);
        defaultColors.put("dialogRadioBackgroundChecked", -13129232);
        defaultColors.put("dialogProgressCircle", -14115349);
        defaultColors.put("dialogLineProgress", -11371101);
        defaultColors.put("dialogLineProgressBackground", -2368549);
        defaultColors.put("dialogButton", -11955764);
        defaultColors.put("dialogButtonSelector", Integer.valueOf((int) AndroidUtilities.LIGHT_STATUS_BAR_OVERLAY));
        defaultColors.put("dialogScrollGlow", -657673);
        defaultColors.put("dialogRoundCheckBox", -11750155);
        defaultColors.put("dialogRoundCheckBoxCheck", -1);
        defaultColors.put("dialogBadgeBackground", -12664327);
        defaultColors.put("dialogBadgeText", -1);
        defaultColors.put("dialogCameraIcon", -1);
        defaultColors.put("dialog_inlineProgressBackground", -151981323);
        defaultColors.put("dialog_inlineProgress", -9735304);
        defaultColors.put("dialogSearchBackground", -854795);
        defaultColors.put("dialogSearchHint", -6774617);
        defaultColors.put("dialogSearchIcon", -6182737);
        defaultColors.put("dialogSearchText", -14540254);
        defaultColors.put("dialogFloatingButton", -11750155);
        defaultColors.put("dialogFloatingButtonPressed", Integer.valueOf((int) AndroidUtilities.LIGHT_STATUS_BAR_OVERLAY));
        defaultColors.put("dialogFloatingIcon", -1);
        defaultColors.put("dialogShadowLine", 301989888);
        defaultColors.put("dialogEmptyImage", -6314840);
        defaultColors.put("dialogEmptyText", -7565164);
        defaultColors.put("dialogSwipeRemove", -1743531);
        defaultColors.put("dialogSwipeRemove", -1743531);
        defaultColors.put("dialogReactionMentionBackground", -1026983);
        defaultColors.put("windowBackgroundWhite", -1);
        defaultColors.put("windowBackgroundUnchecked", -6445135);
        defaultColors.put("windowBackgroundChecked", -11034919);
        defaultColors.put("windowBackgroundCheckText", -1);
        defaultColors.put("progressCircle", -14904349);
        defaultColors.put("windowBackgroundWhiteGrayIcon", -8288629);
        defaultColors.put("windowBackgroundWhiteBlueText", -12545331);
        defaultColors.put("windowBackgroundWhiteBlueText2", -12937771);
        defaultColors.put("windowBackgroundWhiteBlueText3", -14255946);
        defaultColors.put("windowBackgroundWhiteBlueText4", -14904349);
        defaultColors.put("windowBackgroundWhiteBlueText5", -11759926);
        defaultColors.put("windowBackgroundWhiteBlueText6", -12940081);
        defaultColors.put("windowBackgroundWhiteBlueText7", -13141330);
        defaultColors.put("windowBackgroundWhiteBlueButton", -14776109);
        defaultColors.put("windowBackgroundWhiteBlueIcon", -13132315);
        defaultColors.put("windowBackgroundWhiteGreenText", -14248148);
        defaultColors.put("windowBackgroundWhiteGreenText2", -13129704);
        defaultColors.put("windowBackgroundWhiteRedText", -3319206);
        defaultColors.put("windowBackgroundWhiteRedText2", -2404015);
        defaultColors.put("windowBackgroundWhiteRedText3", -2995895);
        defaultColors.put("windowBackgroundWhiteRedText4", -3198928);
        defaultColors.put("windowBackgroundWhiteRedText5", -1230535);
        defaultColors.put("windowBackgroundWhiteRedText6", -39322);
        defaultColors.put("windowBackgroundWhiteGrayText", -8156010);
        defaultColors.put("windowBackgroundWhiteGrayText2", -8223094);
        defaultColors.put("windowBackgroundWhiteGrayText3", -6710887);
        defaultColors.put("windowBackgroundWhiteGrayText4", -8355712);
        defaultColors.put("windowBackgroundWhiteGrayText5", -6052957);
        defaultColors.put("windowBackgroundWhiteGrayText6", -9079435);
        defaultColors.put("windowBackgroundWhiteGrayText7", -3750202);
        defaultColors.put("windowBackgroundWhiteGrayText8", -9605774);
        defaultColors.put("windowBackgroundWhiteGrayLine", -2368549);
        defaultColors.put("windowBackgroundWhiteBlackText", -14540254);
        defaultColors.put("windowBackgroundWhiteHintText", -5723992);
        defaultColors.put("windowBackgroundWhiteValueText", -12937771);
        defaultColors.put("windowBackgroundWhiteLinkText", -14255946);
        defaultColors.put("windowBackgroundWhiteLinkSelection", 862104035);
        defaultColors.put("windowBackgroundWhiteBlueHeader", -12937771);
        defaultColors.put("windowBackgroundWhiteInputField", -2368549);
        defaultColors.put("windowBackgroundWhiteInputFieldActivated", -13129232);
        defaultColors.put("switchTrack", -5196358);
        defaultColors.put("switchTrackChecked", -11358743);
        defaultColors.put("switchTrackBlue", -8221031);
        defaultColors.put("switchTrackBlueChecked", -12810041);
        defaultColors.put("switchTrackBlueThumb", -1);
        defaultColors.put("switchTrackBlueThumbChecked", -1);
        defaultColors.put("switchTrackBlueSelector", 390089299);
        defaultColors.put("switchTrackBlueSelectorChecked", 553797505);
        defaultColors.put("switch2Track", -688514);
        defaultColors.put("switch2TrackChecked", -11358743);
        defaultColors.put("checkboxSquareBackground", -12345121);
        defaultColors.put("checkboxSquareCheck", -1);
        defaultColors.put("checkboxSquareUnchecked", -9211021);
        defaultColors.put("checkboxSquareDisabled", -5197648);
        defaultColors.put("listSelectorSDK21", Integer.valueOf((int) AndroidUtilities.LIGHT_STATUS_BAR_OVERLAY));
        defaultColors.put("radioBackground", -5000269);
        defaultColors.put("radioBackgroundChecked", -13129232);
        defaultColors.put("windowBackgroundGray", -986896);
        defaultColors.put("windowBackgroundGrayShadow", -16777216);
        defaultColors.put("emptyListPlaceholder", -6974059);
        defaultColors.put("divider", -2500135);
        defaultColors.put("graySection", -657931);
        defaultColors.put("key_graySectionText", -8222838);
        defaultColors.put("contextProgressInner1", -4202506);
        defaultColors.put("contextProgressOuter1", -13920542);
        defaultColors.put("contextProgressInner2", -4202506);
        defaultColors.put("contextProgressOuter2", -1);
        defaultColors.put("contextProgressInner3", -5000269);
        defaultColors.put("contextProgressOuter3", -1);
        defaultColors.put("contextProgressInner4", -3486256);
        defaultColors.put("contextProgressOuter4", -13683656);
        defaultColors.put("fastScrollActive", -11361317);
        defaultColors.put("fastScrollInactive", -3551791);
        defaultColors.put("fastScrollText", -1);
        defaultColors.put("avatar_text", -1);
        defaultColors.put("avatar_backgroundSaved", -10043398);
        defaultColors.put("avatar_backgroundArchived", -5654847);
        defaultColors.put("avatar_backgroundArchivedHidden", -10043398);
        defaultColors.put("avatar_backgroundRed", -1743531);
        defaultColors.put("avatar_backgroundOrange", -881592);
        defaultColors.put("avatar_backgroundViolet", -7436818);
        defaultColors.put("avatar_backgroundGreen", -8992691);
        defaultColors.put("avatar_backgroundCyan", -10502443);
        defaultColors.put("avatar_backgroundBlue", -11232035);
        defaultColors.put("avatar_backgroundPink", -887654);
        defaultColors.put("avatar_backgroundInProfileBlue", -11500111);
        defaultColors.put("avatar_backgroundActionBarBlue", -10907718);
        defaultColors.put("avatar_subtitleInProfileBlue", -2626822);
        defaultColors.put("avatar_actionBarSelectorBlue", -11959891);
        defaultColors.put("avatar_actionBarIconBlue", -1);
        defaultColors.put("avatar_nameInMessageRed", -3516848);
        defaultColors.put("avatar_nameInMessageOrange", -2589911);
        defaultColors.put("avatar_nameInMessageViolet", -11627828);
        defaultColors.put("avatar_nameInMessageGreen", -11488718);
        defaultColors.put("avatar_nameInMessageCyan", -13132104);
        defaultColors.put("avatar_nameInMessageBlue", -11627828);
        defaultColors.put("avatar_nameInMessagePink", -11627828);
        defaultColors.put("actionBarDefault", -11371101);
        defaultColors.put("actionBarDefaultIcon", -1);
        defaultColors.put("actionBarActionModeDefault", -1);
        defaultColors.put("actionBarActionModeDefaultTop", 268435456);
        defaultColors.put("actionBarActionModeDefaultIcon", -9999761);
        defaultColors.put("actionBarDefaultTitle", -1);
        defaultColors.put("actionBarDefaultSubtitle", -2758409);
        defaultColors.put("actionBarDefaultSelector", -12554860);
        defaultColors.put("actionBarWhiteSelector", 486539264);
        defaultColors.put("actionBarDefaultSearch", -1);
        defaultColors.put("actionBarDefaultSearchPlaceholder", -1996488705);
        defaultColors.put("actionBarDefaultSubmenuItem", -14540254);
        defaultColors.put("actionBarDefaultSubmenuItemIcon", -9999504);
        defaultColors.put("actionBarDefaultSubmenuBackground", -1);
        defaultColors.put("actionBarDefaultSubmenuSeparator", -657931);
        defaultColors.put("actionBarActionModeDefaultSelector", -1907998);
        defaultColors.put("actionBarTabActiveText", -1);
        defaultColors.put("actionBarTabUnactiveText", -2758409);
        defaultColors.put("actionBarTabLine", -1);
        defaultColors.put("actionBarTabSelector", -12554860);
        defaultColors.put("actionBarBrowser", -1);
        defaultColors.put("actionBarDefaultArchived", -9471353);
        defaultColors.put("actionBarDefaultArchivedSelector", -10590350);
        defaultColors.put("actionBarDefaultArchivedIcon", -1);
        defaultColors.put("actionBarDefaultArchivedTitle", -1);
        defaultColors.put("actionBarDefaultArchivedSearch", -1);
        defaultColors.put("actionBarDefaultSearchArchivedPlaceholder", -1996488705);
        defaultColors.put("chats_onlineCircle", -11810020);
        defaultColors.put("chats_unreadCounter", -11613090);
        defaultColors.put("chats_unreadCounterMuted", -3749428);
        defaultColors.put("chats_unreadCounterText", -1);
        defaultColors.put("chats_archiveBackground", -10049056);
        defaultColors.put("chats_archivePinBackground", -6313293);
        defaultColors.put("chats_archiveIcon", -1);
        defaultColors.put("chats_archiveText", -1);
        defaultColors.put("chats_name", -14540254);
        defaultColors.put("chats_nameArchived", -11382190);
        defaultColors.put("chats_secretName", -16734706);
        defaultColors.put("chats_secretIcon", -15093466);
        defaultColors.put("chats_nameIcon", -14408668);
        defaultColors.put("chats_pinnedIcon", -5723992);
        defaultColors.put("chats_message", -7631473);
        defaultColors.put("chats_messageArchived", -7237231);
        defaultColors.put("chats_message_threeLines", -7434095);
        defaultColors.put("chats_draft", -2274503);
        defaultColors.put("chats_nameMessage", -12812624);
        defaultColors.put("chats_nameMessageArchived", -7631473);
        defaultColors.put("chats_nameMessage_threeLines", -12434359);
        defaultColors.put("chats_nameMessageArchived_threeLines", -10592674);
        defaultColors.put("chats_attachMessage", -12812624);
        defaultColors.put("chats_actionMessage", -12812624);
        defaultColors.put("chats_date", -6973028);
        defaultColors.put("chats_pinnedOverlay", 134217728);
        defaultColors.put("chats_tabletSelectedOverlay", Integer.valueOf((int) AndroidUtilities.LIGHT_STATUS_BAR_OVERLAY));
        defaultColors.put("chats_sentCheck", -12146122);
        defaultColors.put("chats_sentReadCheck", -12146122);
        defaultColors.put("chats_sentClock", -9061026);
        defaultColors.put("chats_sentError", -2796974);
        defaultColors.put("chats_sentErrorIcon", -1);
        defaultColors.put("chats_verifiedBackground", -13391642);
        defaultColors.put("chats_verifiedCheck", -1);
        defaultColors.put("chats_muteIcon", -4341308);
        defaultColors.put("chats_mentionIcon", -1);
        defaultColors.put("chats_menuBackground", -1);
        defaultColors.put("chats_menuItemText", -12303292);
        defaultColors.put("chats_menuItemCheck", -10907718);
        defaultColors.put("chats_menuItemIcon", -7827048);
        defaultColors.put("chats_menuName", -1);
        defaultColors.put("chats_menuPhone", -1);
        defaultColors.put("chats_menuPhoneCats", -4004353);
        defaultColors.put("chats_menuCloud", -1);
        defaultColors.put("chats_menuCloudBackgroundCats", -12420183);
        defaultColors.put("chats_actionIcon", -1);
        defaultColors.put("chats_actionBackground", -10114592);
        defaultColors.put("chats_actionPressedBackground", -11100714);
        defaultColors.put("chats_actionUnreadIcon", -9211021);
        defaultColors.put("chats_actionUnreadBackground", -1);
        defaultColors.put("chats_actionUnreadPressedBackground", -855310);
        defaultColors.put("chats_menuTopBackgroundCats", -10907718);
        defaultColors.put("chats_archivePullDownBackground", -3749428);
        defaultColors.put("chats_archivePullDownBackgroundActive", -10049056);
        defaultColors.put("chat_attachMediaBanBackground", -12171706);
        defaultColors.put("chat_attachMediaBanText", -1);
        defaultColors.put("chat_attachCheckBoxCheck", -1);
        defaultColors.put("chat_attachCheckBoxBackground", -12995849);
        defaultColors.put("chat_attachPhotoBackground", 201326592);
        defaultColors.put("chat_attachActiveTab", -13391883);
        defaultColors.put("chat_attachUnactiveTab", -7169634);
        defaultColors.put("chat_attachPermissionImage", -13421773);
        defaultColors.put("chat_attachPermissionMark", -1945520);
        defaultColors.put("chat_attachPermissionText", -9472134);
        defaultColors.put("chat_attachEmptyImage", -3355444);
        defaultColors.put("chat_attachGalleryBackground", -12214795);
        defaultColors.put("chat_attachGalleryText", -13726231);
        defaultColors.put("chat_attachGalleryIcon", -1);
        defaultColors.put("chat_attachAudioBackground", -1351584);
        defaultColors.put("chat_attachAudioText", -2209977);
        defaultColors.put("chat_attachAudioIcon", -1);
        defaultColors.put("chat_attachFileBackground", -13321743);
        defaultColors.put("chat_attachFileText", -15423260);
        defaultColors.put("chat_attachFileIcon", -1);
        defaultColors.put("chat_attachContactBackground", -868277);
        defaultColors.put("chat_attachContactText", -2121728);
        defaultColors.put("chat_attachContactIcon", -1);
        defaultColors.put("chat_attachLocationBackground", -10436011);
        defaultColors.put("chat_attachLocationText", -12801233);
        defaultColors.put("chat_attachLocationIcon", -1);
        defaultColors.put("chat_attachPollBackground", -868277);
        defaultColors.put("chat_attachPollText", -2121728);
        defaultColors.put("chat_attachPollIcon", -1);
        defaultColors.put("chat_inPollCorrectAnswer", -10436011);
        defaultColors.put("chat_outPollCorrectAnswer", -10436011);
        defaultColors.put("chat_inPollWrongAnswer", -1351584);
        defaultColors.put("chat_outPollWrongAnswer", -1351584);
        defaultColors.put("chat_status", -2758409);
        defaultColors.put("chat_inDownCall", -16725933);
        defaultColors.put("chat_inUpCall", -47032);
        defaultColors.put("chat_outUpCall", -16725933);
        defaultColors.put("chat_lockIcon", -1);
        defaultColors.put("chat_muteIcon", -5124893);
        defaultColors.put("chat_inBubble", -1);
        defaultColors.put("chat_inBubbleSelected", -1247235);
        defaultColors.put("chat_inBubbleShadow", -14862509);
        defaultColors.put("chat_outBubble", -1048610);
        defaultColors.put("chat_outBubbleGradientSelectedOverlay", 335544320);
        defaultColors.put("chat_outBubbleSelected", -2492475);
        defaultColors.put("chat_outBubbleShadow", -14781172);
        defaultColors.put("chat_inMediaIcon", -1);
        defaultColors.put("chat_inMediaIconSelected", -1050370);
        defaultColors.put("chat_outMediaIcon", -1048610);
        defaultColors.put("chat_outMediaIconSelected", -1967921);
        defaultColors.put("chat_messageTextIn", -16777216);
        defaultColors.put("chat_messageTextOut", -16777216);
        defaultColors.put("chat_messageLinkIn", -14255946);
        defaultColors.put("chat_messageLinkOut", -14255946);
        defaultColors.put("chat_serviceText", -1);
        defaultColors.put("chat_serviceLink", -1);
        defaultColors.put("chat_serviceIcon", -1);
        defaultColors.put("chat_mediaTimeBackground", 1711276032);
        defaultColors.put("chat_outSentCheck", -10637232);
        defaultColors.put("chat_outSentCheckSelected", -10637232);
        defaultColors.put("chat_outSentCheckRead", -10637232);
        defaultColors.put("chat_outSentCheckReadSelected", -10637232);
        defaultColors.put("chat_outSentClock", -9061026);
        defaultColors.put("chat_outSentClockSelected", -9061026);
        defaultColors.put("chat_inSentClock", -6182221);
        defaultColors.put("chat_inSentClockSelected", -7094838);
        defaultColors.put("chat_mediaSentCheck", -1);
        defaultColors.put("chat_mediaSentClock", -1);
        defaultColors.put("chat_inViews", -6182221);
        defaultColors.put("chat_inViewsSelected", -7094838);
        defaultColors.put("chat_outViews", -9522601);
        defaultColors.put("chat_outViewsSelected", -9522601);
        defaultColors.put("chat_mediaViews", -1);
        defaultColors.put("chat_inMenu", -4801083);
        defaultColors.put("chat_inMenuSelected", -6766130);
        defaultColors.put("chat_outMenu", -7221634);
        defaultColors.put("chat_outMenuSelected", -7221634);
        defaultColors.put("chat_mediaMenu", -1);
        defaultColors.put("chat_outInstant", -11162801);
        defaultColors.put("chat_outInstantSelected", -12019389);
        defaultColors.put("chat_inInstant", -12940081);
        defaultColors.put("chat_inInstantSelected", -13600331);
        defaultColors.put("chat_sentError", -2411211);
        defaultColors.put("chat_sentErrorIcon", -1);
        defaultColors.put("chat_selectedBackground", 671781104);
        defaultColors.put("chat_previewDurationText", -1);
        defaultColors.put("chat_previewGameText", -1);
        defaultColors.put("chat_inPreviewInstantText", -12940081);
        defaultColors.put("chat_outPreviewInstantText", -11162801);
        defaultColors.put("chat_inPreviewInstantSelectedText", -13600331);
        defaultColors.put("chat_outPreviewInstantSelectedText", -12019389);
        defaultColors.put("chat_secretTimeText", -1776928);
        defaultColors.put("chat_stickerNameText", -1);
        defaultColors.put("chat_botButtonText", -1);
        defaultColors.put("chat_botProgress", -1);
        defaultColors.put("chat_inForwardedNameText", -13072697);
        defaultColors.put("chat_outForwardedNameText", -11162801);
        defaultColors.put("chat_inPsaNameText", -10838983);
        defaultColors.put("chat_outPsaNameText", -10838983);
        defaultColors.put("chat_inViaBotNameText", -12940081);
        defaultColors.put("chat_outViaBotNameText", -11162801);
        defaultColors.put("chat_stickerViaBotNameText", -1);
        defaultColors.put("chat_inReplyLine", -10903592);
        defaultColors.put("chat_outReplyLine", -9520791);
        defaultColors.put("chat_stickerReplyLine", -1);
        defaultColors.put("chat_inReplyNameText", -12940081);
        defaultColors.put("chat_outReplyNameText", -11162801);
        defaultColors.put("chat_stickerReplyNameText", -1);
        defaultColors.put("chat_inReplyMessageText", -16777216);
        defaultColors.put("chat_outReplyMessageText", -16777216);
        defaultColors.put("chat_inReplyMediaMessageText", -6182221);
        defaultColors.put("chat_outReplyMediaMessageText", -10112933);
        defaultColors.put("chat_inReplyMediaMessageSelectedText", -7752511);
        defaultColors.put("chat_outReplyMediaMessageSelectedText", -10112933);
        defaultColors.put("chat_stickerReplyMessageText", -1);
        defaultColors.put("chat_inPreviewLine", -9390872);
        defaultColors.put("chat_outPreviewLine", -7812741);
        defaultColors.put("chat_inSiteNameText", -12940081);
        defaultColors.put("chat_outSiteNameText", -11162801);
        defaultColors.put("chat_inContactNameText", -11625772);
        defaultColors.put("chat_outContactNameText", -11162801);
        defaultColors.put("chat_inContactPhoneText", -13683656);
        defaultColors.put("chat_inContactPhoneSelectedText", -13683656);
        defaultColors.put("chat_outContactPhoneText", -13286860);
        defaultColors.put("chat_outContactPhoneSelectedText", -13286860);
        defaultColors.put("chat_mediaProgress", -1);
        defaultColors.put("chat_inAudioProgress", -1);
        defaultColors.put("chat_outAudioProgress", -1048610);
        defaultColors.put("chat_inAudioSelectedProgress", -1050370);
        defaultColors.put("chat_outAudioSelectedProgress", -1967921);
        defaultColors.put("chat_mediaTimeText", -1);
        defaultColors.put("chat_adminText", -4143413);
        defaultColors.put("chat_adminSelectedText", -7752511);
        defaultColors.put("chat_outAdminText", -9391780);
        defaultColors.put("chat_outAdminSelectedText", -9391780);
        defaultColors.put("chat_inTimeText", -6182221);
        defaultColors.put("chat_inTimeSelectedText", -7752511);
        defaultColors.put("chat_outTimeText", -9391780);
        defaultColors.put("chat_outTimeSelectedText", -9391780);
        defaultColors.put("chat_inAudioPerfomerText", -13683656);
        defaultColors.put("chat_inAudioPerfomerSelectedText", -13683656);
        defaultColors.put("chat_outAudioPerfomerText", -13286860);
        defaultColors.put("chat_outAudioPerfomerSelectedText", -13286860);
        defaultColors.put("chat_inAudioTitleText", -11625772);
        defaultColors.put("chat_outAudioTitleText", -11162801);
        defaultColors.put("chat_inAudioDurationText", -6182221);
        defaultColors.put("chat_outAudioDurationText", -10112933);
        defaultColors.put("chat_inAudioDurationSelectedText", -7752511);
        defaultColors.put("chat_outAudioDurationSelectedText", -10112933);
        defaultColors.put("chat_inAudioSeekbar", -1774864);
        defaultColors.put("chat_inAudioCacheSeekbar", 1071966960);
        defaultColors.put("chat_outAudioSeekbar", -4463700);
        defaultColors.put("chat_outAudioCacheSeekbar", 1069278124);
        defaultColors.put("chat_inAudioSeekbarSelected", -4399384);
        defaultColors.put("chat_outAudioSeekbarSelected", -5644906);
        defaultColors.put("chat_inAudioSeekbarFill", -9259544);
        defaultColors.put("chat_outAudioSeekbarFill", -8863118);
        defaultColors.put("chat_inVoiceSeekbar", -2169365);
        defaultColors.put("chat_outVoiceSeekbar", -4463700);
        defaultColors.put("chat_inVoiceSeekbarSelected", -4399384);
        defaultColors.put("chat_outVoiceSeekbarSelected", -5644906);
        defaultColors.put("chat_inVoiceSeekbarFill", -9259544);
        defaultColors.put("chat_outVoiceSeekbarFill", -8863118);
        defaultColors.put("chat_inFileProgress", -1314571);
        defaultColors.put("chat_outFileProgress", -2427453);
        defaultColors.put("chat_inFileProgressSelected", -3413258);
        defaultColors.put("chat_outFileProgressSelected", -3806041);
        defaultColors.put("chat_inFileNameText", -11625772);
        defaultColors.put("chat_outFileNameText", -11162801);
        defaultColors.put("chat_inFileInfoText", -6182221);
        defaultColors.put("chat_outFileInfoText", -10112933);
        defaultColors.put("chat_inFileInfoSelectedText", -7752511);
        defaultColors.put("chat_outFileInfoSelectedText", -10112933);
        defaultColors.put("chat_inFileBackground", -1314571);
        defaultColors.put("chat_outFileBackground", -2427453);
        defaultColors.put("chat_inFileBackgroundSelected", -3413258);
        defaultColors.put("chat_outFileBackgroundSelected", -3806041);
        defaultColors.put("chat_inVenueInfoText", -6182221);
        defaultColors.put("chat_outVenueInfoText", -10112933);
        defaultColors.put("chat_inVenueInfoSelectedText", -7752511);
        defaultColors.put("chat_outVenueInfoSelectedText", -10112933);
        defaultColors.put("chat_mediaInfoText", -1);
        defaultColors.put("chat_linkSelectBackground", 862104035);
        defaultColors.put("chat_outLinkSelectBackground", 862104035);
        defaultColors.put("chat_textSelectBackground", 1717742051);
        defaultColors.put("chat_emojiPanelBackground", -986379);
        defaultColors.put("chat_emojiPanelBadgeBackground", -11688214);
        defaultColors.put("chat_emojiPanelBadgeText", -1);
        defaultColors.put("chat_emojiSearchBackground", -1709586);
        defaultColors.put("chat_emojiSearchIcon", -7036497);
        defaultColors.put("chat_emojiPanelShadowLine", 301989888);
        defaultColors.put("chat_emojiPanelEmptyText", -7038047);
        defaultColors.put("chat_emojiPanelIcon", -6445909);
        defaultColors.put("chat_emojiBottomPanelIcon", -7564905);
        defaultColors.put("chat_emojiPanelIconSelected", -13920286);
        defaultColors.put("chat_emojiPanelStickerPackSelector", -1907225);
        defaultColors.put("chat_emojiPanelStickerPackSelectorLine", -11097104);
        defaultColors.put("chat_emojiPanelBackspace", -7564905);
        defaultColors.put("chat_emojiPanelMasksIcon", -1);
        defaultColors.put("chat_emojiPanelMasksIconSelected", -10305560);
        defaultColors.put("chat_emojiPanelTrendingTitle", -14540254);
        defaultColors.put("chat_emojiPanelStickerSetName", -8221804);
        defaultColors.put("chat_emojiPanelStickerSetNameHighlight", -14184997);
        defaultColors.put("chat_emojiPanelStickerSetNameIcon", -5130564);
        defaultColors.put("chat_emojiPanelTrendingDescription", -7697782);
        defaultColors.put("chat_botKeyboardButtonText", -13220017);
        defaultColors.put("chat_botKeyboardButtonBackground", -1775639);
        defaultColors.put("chat_botKeyboardButtonBackgroundPressed", -3354156);
        defaultColors.put("chat_unreadMessagesStartArrowIcon", -6113849);
        defaultColors.put("chat_unreadMessagesStartText", -11102772);
        defaultColors.put("chat_unreadMessagesStartBackground", -1);
        defaultColors.put("chat_inFileIcon", -6113849);
        defaultColors.put("chat_inFileSelectedIcon", -7883067);
        defaultColors.put("chat_outFileIcon", -8011912);
        defaultColors.put("chat_outFileSelectedIcon", -8011912);
        defaultColors.put("chat_inLocationBackground", -1314571);
        defaultColors.put("chat_inLocationIcon", -6113849);
        defaultColors.put("chat_outLocationBackground", -2427453);
        defaultColors.put("chat_outLocationIcon", -7880840);
        defaultColors.put("chat_inContactBackground", -9259544);
        defaultColors.put("chat_inContactIcon", -1);
        defaultColors.put("chat_outContactBackground", -8863118);
        defaultColors.put("chat_outContactIcon", -1048610);
        defaultColors.put("chat_outBroadcast", -12146122);
        defaultColors.put("chat_mediaBroadcast", -1);
        defaultColors.put("chat_searchPanelIcons", -9999761);
        defaultColors.put("chat_searchPanelText", -9999761);
        defaultColors.put("chat_secretChatStatusText", -8421505);
        defaultColors.put("chat_fieldOverlayText", -12940081);
        defaultColors.put("chat_stickersHintPanel", -1);
        defaultColors.put("chat_replyPanelIcons", -11032346);
        defaultColors.put("chat_replyPanelClose", -7432805);
        defaultColors.put("chat_replyPanelName", -12940081);
        defaultColors.put("chat_replyPanelMessage", -14540254);
        defaultColors.put("chat_replyPanelLine", -1513240);
        defaultColors.put("chat_messagePanelBackground", -1);
        defaultColors.put("chat_messagePanelText", -16777216);
        defaultColors.put("chat_messagePanelHint", -5985101);
        defaultColors.put("chat_messagePanelCursor", -11230757);
        defaultColors.put("chat_messagePanelShadow", -16777216);
        defaultColors.put("chat_messagePanelIcons", -7432805);
        defaultColors.put("chat_recordedVoicePlayPause", -1);
        defaultColors.put("chat_recordedVoiceDot", -2468275);
        defaultColors.put("chat_recordedVoiceBackground", -10637848);
        defaultColors.put("chat_recordedVoiceProgress", -5120257);
        defaultColors.put("chat_recordedVoiceProgressInner", -1);
        defaultColors.put("chat_recordVoiceCancel", -12937772);
        defaultColors.put("key_chat_recordedVoiceHighlight", 1694498815);
        defaultColors.put("chat_messagePanelSend", -10309397);
        defaultColors.put("key_chat_messagePanelVoiceLock", -5987164);
        defaultColors.put("key_chat_messagePanelVoiceLockBackground", -1);
        defaultColors.put("key_chat_messagePanelVoiceLockShadow", -16777216);
        defaultColors.put("chat_recordTime", -7432805);
        defaultColors.put("chat_emojiPanelNewTrending", -11688214);
        defaultColors.put("chat_gifSaveHintText", -1);
        defaultColors.put("chat_gifSaveHintBackground", -871296751);
        defaultColors.put("chat_goDownButton", -1);
        defaultColors.put("chat_goDownButtonShadow", -16777216);
        defaultColors.put("chat_goDownButtonIcon", -7432805);
        defaultColors.put("chat_goDownButtonCounter", -1);
        defaultColors.put("chat_goDownButtonCounterBackground", -11689240);
        defaultColors.put("chat_messagePanelCancelInlineBot", -5395027);
        defaultColors.put("chat_messagePanelVoicePressed", -1);
        defaultColors.put("chat_messagePanelVoiceBackground", -10639650);
        defaultColors.put("chat_messagePanelVoiceDelete", -9211021);
        defaultColors.put("chat_messagePanelVoiceDuration", -1);
        defaultColors.put("chat_inlineResultIcon", -11037236);
        defaultColors.put("chat_topPanelBackground", -1);
        defaultColors.put("chat_topPanelClose", -7629157);
        defaultColors.put("chat_topPanelLine", -9658414);
        defaultColors.put("chat_topPanelTitle", -12940081);
        defaultColors.put("chat_topPanelMessage", -7893359);
        defaultColors.put("chat_reportSpam", -3188393);
        defaultColors.put("chat_addContact", -11894091);
        defaultColors.put("chat_inLoader", -9259544);
        defaultColors.put("chat_inLoaderSelected", -10114080);
        defaultColors.put("chat_outLoader", -8863118);
        defaultColors.put("chat_outLoaderSelected", -9783964);
        defaultColors.put("chat_inLoaderPhoto", -6113080);
        defaultColors.put("chat_inLoaderPhotoSelected", -6113849);
        defaultColors.put("chat_inLoaderPhotoIcon", -197380);
        defaultColors.put("chat_inLoaderPhotoIconSelected", -1314571);
        defaultColors.put("chat_outLoaderPhoto", -8011912);
        defaultColors.put("chat_outLoaderPhotoSelected", -8538000);
        defaultColors.put("chat_outLoaderPhotoIcon", -2427453);
        defaultColors.put("chat_outLoaderPhotoIconSelected", -4134748);
        defaultColors.put("chat_mediaLoaderPhoto", 1711276032);
        defaultColors.put("chat_mediaLoaderPhotoSelected", 2130706432);
        defaultColors.put("chat_mediaLoaderPhotoIcon", -1);
        defaultColors.put("chat_mediaLoaderPhotoIconSelected", -2500135);
        defaultColors.put("chat_secretTimerBackground", -868326258);
        defaultColors.put("chat_secretTimerText", -1);
        defaultColors.put("profile_creatorIcon", -12937771);
        defaultColors.put("profile_actionIcon", -8288630);
        defaultColors.put("profile_actionBackground", -1);
        defaultColors.put("profile_actionPressedBackground", -855310);
        defaultColors.put("profile_verifiedBackground", -5056776);
        defaultColors.put("profile_verifiedCheck", -11959368);
        defaultColors.put("profile_title", -1);
        defaultColors.put("profile_status", -2626822);
        defaultColors.put("profile_tabText", -7893872);
        defaultColors.put("profile_tabSelectedText", -12937771);
        defaultColors.put("profile_tabSelectedLine", -11557143);
        defaultColors.put("profile_tabSelector", Integer.valueOf((int) AndroidUtilities.LIGHT_STATUS_BAR_OVERLAY));
        defaultColors.put("player_actionBar", -1);
        defaultColors.put("player_actionBarSelector", Integer.valueOf((int) AndroidUtilities.LIGHT_STATUS_BAR_OVERLAY));
        defaultColors.put("player_actionBarTitle", -13683656);
        defaultColors.put("player_actionBarTop", -1728053248);
        defaultColors.put("player_actionBarSubtitle", -7697782);
        defaultColors.put("player_actionBarItems", -7697782);
        defaultColors.put("player_background", -1);
        defaultColors.put("player_time", -7564650);
        defaultColors.put("player_progressBackground", -1315344);
        defaultColors.put("player_progressBackground2", -3353637);
        defaultColors.put("key_player_progressCachedBackground", -3810064);
        defaultColors.put("player_progress", -11228437);
        defaultColors.put("player_button", -13421773);
        defaultColors.put("player_buttonActive", -11753238);
        defaultColors.put("key_sheet_scrollUp", -1973016);
        defaultColors.put("key_sheet_other", -3551789);
        defaultColors.put("files_folderIcon", -1);
        defaultColors.put("files_folderIconBackground", -10637333);
        defaultColors.put("files_iconText", -1);
        defaultColors.put("sessions_devicesImage", -6908266);
        defaultColors.put("passport_authorizeBackground", -12211217);
        defaultColors.put("passport_authorizeBackgroundSelected", -12542501);
        defaultColors.put("passport_authorizeText", -1);
        defaultColors.put("location_sendLocationBackground", -12149258);
        defaultColors.put("location_sendLocationIcon", -1);
        defaultColors.put("location_sendLocationText", -14906664);
        defaultColors.put("location_sendLiveLocationBackground", -11550140);
        defaultColors.put("location_sendLiveLocationIcon", -1);
        defaultColors.put("location_sendLiveLocationText", -13194460);
        defaultColors.put("location_liveLocationProgress", -13262875);
        defaultColors.put("location_placeLocationBackground", -11753238);
        defaultColors.put("location_actionIcon", -12959675);
        defaultColors.put("location_actionActiveIcon", -12414746);
        defaultColors.put("location_actionBackground", -1);
        defaultColors.put("location_actionPressedBackground", -855310);
        defaultColors.put("dialog_liveLocationProgress", -13262875);
        defaultColors.put("calls_callReceivedGreenIcon", -16725933);
        defaultColors.put("calls_callReceivedRedIcon", -47032);
        defaultColors.put("featuredStickers_addedIcon", -11491093);
        defaultColors.put("featuredStickers_buttonProgress", -1);
        defaultColors.put("featuredStickers_addButton", -11491093);
        defaultColors.put("featuredStickers_addButtonPressed", -12346402);
        defaultColors.put("featuredStickers_removeButtonText", -11496493);
        defaultColors.put("featuredStickers_buttonText", -1);
        defaultColors.put("featuredStickers_unread", -11688214);
        defaultColors.put("inappPlayerPerformer", -13683656);
        defaultColors.put("inappPlayerTitle", -13683656);
        defaultColors.put("inappPlayerBackground", -1);
        defaultColors.put("inappPlayerPlayPause", -10309397);
        defaultColors.put("inappPlayerClose", -7629157);
        defaultColors.put("returnToCallBackground", -12279325);
        defaultColors.put("returnToCallMutedBackground", -6445135);
        defaultColors.put("returnToCallText", -1);
        defaultColors.put("sharedMedia_startStopLoadIcon", -13196562);
        defaultColors.put("sharedMedia_linkPlaceholder", -986123);
        defaultColors.put("sharedMedia_linkPlaceholderText", -4735293);
        defaultColors.put("sharedMedia_photoPlaceholder", -1182729);
        defaultColors.put("sharedMedia_actionMode", -12154957);
        defaultColors.put("checkbox", -10567099);
        defaultColors.put("checkboxCheck", -1);
        defaultColors.put("checkboxDisabled", -5195326);
        defaultColors.put("stickers_menu", -4801083);
        defaultColors.put("stickers_menuSelector", Integer.valueOf((int) AndroidUtilities.LIGHT_STATUS_BAR_OVERLAY));
        defaultColors.put("changephoneinfo_image", -4669499);
        defaultColors.put("changephoneinfo_image2", -11491350);
        defaultColors.put("groupcreate_hintText", -6182221);
        defaultColors.put("groupcreate_cursor", -11361317);
        defaultColors.put("groupcreate_sectionShadow", -16777216);
        defaultColors.put("groupcreate_sectionText", -8617336);
        defaultColors.put("groupcreate_spanText", -14540254);
        defaultColors.put("groupcreate_spanBackground", -855310);
        defaultColors.put("groupcreate_spanDelete", -1);
        defaultColors.put("contacts_inviteBackground", -11157919);
        defaultColors.put("contacts_inviteText", -1);
        defaultColors.put("login_progressInner", -1971470);
        defaultColors.put("login_progressOuter", -10313520);
        defaultColors.put("musicPicker_checkbox", -14043401);
        defaultColors.put("musicPicker_checkboxCheck", -1);
        defaultColors.put("musicPicker_buttonBackground", -10702870);
        defaultColors.put("musicPicker_buttonIcon", -1);
        defaultColors.put("picker_enabledButton", -15095832);
        defaultColors.put("picker_disabledButton", -6710887);
        defaultColors.put("picker_badge", -14043401);
        defaultColors.put("picker_badgeText", -1);
        defaultColors.put("chat_botSwitchToInlineText", -12348980);
        defaultColors.put("undo_background", -366530760);
        defaultColors.put("undo_cancelColor", -8008961);
        defaultColors.put("undo_infoColor", -1);
        defaultColors.put("wallet_blackBackground", -16777216);
        defaultColors.put("wallet_graySettingsBackground", -986896);
        defaultColors.put("wallet_grayBackground", -14079703);
        defaultColors.put("wallet_whiteBackground", -1);
        defaultColors.put("wallet_blackBackgroundSelector", 1090519039);
        defaultColors.put("wallet_whiteText", -1);
        defaultColors.put("wallet_blackText", -14540254);
        defaultColors.put("wallet_statusText", -8355712);
        defaultColors.put("wallet_grayText", -8947849);
        defaultColors.put("wallet_grayText2", -10066330);
        defaultColors.put("wallet_greenText", -13129704);
        defaultColors.put("wallet_redText", -2408384);
        defaultColors.put("wallet_dateText", -6710887);
        defaultColors.put("wallet_commentText", -6710887);
        defaultColors.put("wallet_releaseBackground", -13599557);
        defaultColors.put("wallet_pullBackground", -14606047);
        defaultColors.put("wallet_buttonBackground", -12082714);
        defaultColors.put("wallet_buttonPressedBackground", -13923114);
        defaultColors.put("wallet_buttonText", -1);
        defaultColors.put("wallet_addressConfirmBackground", 218103808);
        defaultColors.put("chat_outTextSelectionHighlight", 775919907);
        defaultColors.put("chat_inTextSelectionHighlight", 1348643299);
        defaultColors.put("chat_TextSelectionCursor", -12476440);
        defaultColors.put("chat_outTextSelectionCursor", -12476440);
        defaultColors.put("chat_BlurAlpha", -16777216);
        defaultColors.put("statisticChartSignature", 2133140777);
        defaultColors.put("statisticChartSignatureAlpha", 2133140777);
        defaultColors.put("statisticChartHintLine", 437792059);
        defaultColors.put("statisticChartActiveLine", Integer.valueOf((int) AndroidUtilities.DARK_STATUS_BAR_OVERLAY));
        defaultColors.put("statisticChartInactivePickerChart", -1713180935);
        defaultColors.put("statisticChartActivePickerChart", -658846503);
        defaultColors.put("statisticChartRipple", 746495415);
        defaultColors.put("statisticChartBackZoomColor", -15692829);
        defaultColors.put("statisticChartCheckboxInactive", -4342339);
        defaultColors.put("statisticChartNightIconColor", -7434605);
        defaultColors.put("statisticChartChevronColor", -2959913);
        defaultColors.put("statisticChartHighlightColor", 552398060);
        defaultColors.put("statisticChartPopupBackground", -1);
        defaultColors.put("statisticChartLine_blue", -13467675);
        defaultColors.put("statisticChartLine_green", -10369198);
        defaultColors.put("statisticChartLine_red", -2075818);
        defaultColors.put("statisticChartLine_golden", -2180600);
        defaultColors.put("statisticChartLine_lightblue", -10966803);
        defaultColors.put("statisticChartLine_lightgreen", -7352519);
        defaultColors.put("statisticChartLine_orange", -1853657);
        defaultColors.put("statisticChartLine_indigo", -8422925);
        defaultColors.put("statisticChartLineEmpty", -1118482);
        defaultColors.put("actionBarTipBackground", -12292204);
        defaultColors.put("voipgroup_checkMenu", -9718023);
        defaultColors.put("voipgroup_muteButton", -8919716);
        defaultColors.put("voipgroup_muteButton2", -8528726);
        defaultColors.put("voipgroup_muteButton3", -11089922);
        defaultColors.put("voipgroup_searchText", -1);
        defaultColors.put("voipgroup_searchPlaceholder", -8024684);
        defaultColors.put("voipgroup_searchBackground", -13616313);
        defaultColors.put("voipgroup_leaveCallMenu", -35467);
        defaultColors.put("voipgroup_scrollUp", -13023660);
        defaultColors.put("voipgroup_soundButton", 2100052301);
        defaultColors.put("voipgroup_soundButtonActive", 2099422443);
        defaultColors.put("voipgroup_soundButtonActiveScrolled", -2110540545);
        defaultColors.put("voipgroup_soundButton2", 2099796282);
        defaultColors.put("voipgroup_soundButtonActive2", 2098771793);
        defaultColors.put("voipgroup_soundButtonActive2Scrolled", -2111520954);
        defaultColors.put("voipgroup_leaveButton", 2113363036);
        defaultColors.put("voipgroup_leaveButtonScrolled", -2100212396);
        defaultColors.put("voipgroup_connectingProgress", -14107905);
        defaultColors.put("voipgroup_disabledButton", -14933463);
        defaultColors.put("voipgroup_disabledButtonActive", -13878715);
        defaultColors.put("voipgroup_disabledButtonActiveScrolled", -2106088964);
        defaultColors.put("voipgroup_unmuteButton", -11297032);
        defaultColors.put("voipgroup_unmuteButton2", -10038021);
        defaultColors.put("voipgroup_actionBarUnscrolled", -15130842);
        defaultColors.put("voipgroup_listViewBackgroundUnscrolled", -14538189);
        defaultColors.put("voipgroup_lastSeenTextUnscrolled", -8024684);
        defaultColors.put("voipgroup_mutedIconUnscrolled", -8485236);
        defaultColors.put("voipgroup_actionBar", -15789289);
        defaultColors.put("voipgroup_emptyView", -15065823);
        defaultColors.put("voipgroup_actionBarItems", -1);
        defaultColors.put("voipgroup_actionBarSubtitle", -7697782);
        defaultColors.put("voipgroup_actionBarItemsSelector", 515562495);
        defaultColors.put("voipgroup_mutedByAdminIcon", -36752);
        defaultColors.put("voipgroup_mutedIcon", -9471616);
        defaultColors.put("voipgroup_lastSeenText", -8813686);
        defaultColors.put("voipgroup_nameText", -1);
        defaultColors.put("voipgroup_listViewBackground", -14933463);
        defaultColors.put("voipgroup_dialogBackground", -14933463);
        defaultColors.put("voipgroup_listeningText", -11683585);
        defaultColors.put("voipgroup_speakingText", -8917379);
        defaultColors.put("voipgroup_listSelector", 251658239);
        defaultColors.put("voipgroup_inviteMembersBackground", -14538189);
        defaultColors.put("voipgroup_overlayBlue1", -13906177);
        defaultColors.put("voipgroup_overlayBlue2", -16156957);
        defaultColors.put("voipgroup_overlayGreen1", -15551198);
        defaultColors.put("voipgroup_overlayGreen2", -16722239);
        defaultColors.put("voipgroup_topPanelBlue1", -10434565);
        defaultColors.put("voipgroup_topPanelBlue2", -11427847);
        defaultColors.put("voipgroup_topPanelGreen1", -11350435);
        defaultColors.put("voipgroup_topPanelGreen2", -16731712);
        defaultColors.put("voipgroup_topPanelGray", -8021590);
        defaultColors.put("voipgroup_overlayAlertGradientMuted", -14455406);
        defaultColors.put("voipgroup_overlayAlertGradientMuted2", -13873813);
        defaultColors.put("voipgroup_overlayAlertGradientUnmuted", -15955316);
        defaultColors.put("voipgroup_overlayAlertGradientUnmuted2", -14136203);
        defaultColors.put("voipgroup_mutedByAdminGradient", -11033346);
        defaultColors.put("voipgroup_mutedByAdminGradient2", -1026983);
        defaultColors.put("voipgroup_mutedByAdminGradient3", -9015575);
        defaultColors.put("voipgroup_overlayAlertMutedByAdmin", -9998178);
        defaultColors.put("kvoipgroup_overlayAlertMutedByAdmin2", -13676424);
        defaultColors.put("voipgroup_mutedByAdminMuteButton", 2138612735);
        defaultColors.put("voipgroup_mutedByAdminMuteButtonDisabled", 863544319);
        defaultColors.put("voipgroup_windowBackgroundWhiteInputField", -2368549);
        defaultColors.put("voipgroup_windowBackgroundWhiteInputFieldActivated", -13129232);
        defaultColors.put("chat_outReactionButtonBackground", -8863118);
        defaultColors.put("chat_inReactionButtonBackground", -9259544);
        defaultColors.put("chat_inReactionButtonText", -12940081);
        defaultColors.put("chat_outReactionButtonText", -11162801);
        defaultColors.put("chat_inReactionButtonTextSelected", -1);
        defaultColors.put("chat_outReactionButtonTextSelected", -1);
        defaultColors.put("premiumGradient1", -11164161);
        defaultColors.put("premiumGradient2", -5806081);
        defaultColors.put("premiumGradient3", -2401123);
        defaultColors.put("premiumGradient4", -816858);
        defaultColors.put("premiumGradientBackground1", -11164161);
        defaultColors.put("premiumGradientBackground2", -5806081);
        defaultColors.put("premiumGradientBackground3", -2401123);
        defaultColors.put("premiumGradientBackground4", -816858);
        defaultColors.put("premiumGradientBackgroundOverlay", -1);
        defaultColors.put("premiumStarGradient1", -1);
        defaultColors.put("premiumStarGradient2", -1839878);
        defaultColors.put("premiumStartSmallStarsColor", Integer.valueOf(ColorUtils.setAlphaComponent(-1, 90)));
        defaultColors.put("premiumStartSmallStarsColor2", Integer.valueOf(ColorUtils.setAlphaComponent(-1, 90)));
        defaultColors.put("premiumGradientBottomSheet1", -10773017);
        defaultColors.put("premiumGradientBottomSheet2", -5535779);
        defaultColors.put("premiumGradientBottomSheet3", -1600322);
        fallbackKeys.put("chat_adminText", "chat_inTimeText");
        fallbackKeys.put("chat_adminSelectedText", "chat_inTimeSelectedText");
        fallbackKeys.put("key_player_progressCachedBackground", "player_progressBackground");
        fallbackKeys.put("chat_inAudioCacheSeekbar", "chat_inAudioSeekbar");
        fallbackKeys.put("chat_outAudioCacheSeekbar", "chat_outAudioSeekbar");
        fallbackKeys.put("chat_emojiSearchBackground", "chat_emojiPanelStickerPackSelector");
        fallbackKeys.put("location_sendLiveLocationIcon", "location_sendLocationIcon");
        fallbackKeys.put("changephoneinfo_image2", "featuredStickers_addButton");
        fallbackKeys.put("key_graySectionText", "windowBackgroundWhiteGrayText2");
        fallbackKeys.put("chat_inMediaIcon", "chat_inBubble");
        fallbackKeys.put("chat_outMediaIcon", "chat_outBubble");
        fallbackKeys.put("chat_inMediaIconSelected", "chat_inBubbleSelected");
        fallbackKeys.put("chat_outMediaIconSelected", "chat_outBubbleSelected");
        fallbackKeys.put("chats_actionUnreadIcon", "profile_actionIcon");
        fallbackKeys.put("chats_actionUnreadBackground", "profile_actionBackground");
        fallbackKeys.put("chats_actionUnreadPressedBackground", "profile_actionPressedBackground");
        fallbackKeys.put("dialog_inlineProgressBackground", "windowBackgroundGray");
        fallbackKeys.put("dialog_inlineProgress", "chats_menuItemIcon");
        fallbackKeys.put("groupcreate_spanDelete", "chats_actionIcon");
        fallbackKeys.put("sharedMedia_photoPlaceholder", "windowBackgroundGray");
        fallbackKeys.put("chat_attachPollBackground", "chat_attachAudioBackground");
        fallbackKeys.put("chat_attachPollIcon", "chat_attachAudioIcon");
        fallbackKeys.put("chats_onlineCircle", "windowBackgroundWhiteBlueText");
        fallbackKeys.put("windowBackgroundWhiteBlueButton", "windowBackgroundWhiteValueText");
        fallbackKeys.put("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteValueText");
        fallbackKeys.put("undo_background", "chat_gifSaveHintBackground");
        fallbackKeys.put("undo_cancelColor", "chat_gifSaveHintText");
        fallbackKeys.put("undo_infoColor", "chat_gifSaveHintText");
        fallbackKeys.put("windowBackgroundUnchecked", "windowBackgroundWhite");
        fallbackKeys.put("windowBackgroundChecked", "windowBackgroundWhite");
        fallbackKeys.put("switchTrackBlue", "switchTrack");
        fallbackKeys.put("switchTrackBlueChecked", "switchTrackChecked");
        fallbackKeys.put("switchTrackBlueThumb", "windowBackgroundWhite");
        fallbackKeys.put("switchTrackBlueThumbChecked", "windowBackgroundWhite");
        fallbackKeys.put("windowBackgroundCheckText", "windowBackgroundWhite");
        fallbackKeys.put("contextProgressInner4", "contextProgressInner1");
        fallbackKeys.put("contextProgressOuter4", "contextProgressOuter1");
        fallbackKeys.put("switchTrackBlueSelector", "listSelectorSDK21");
        fallbackKeys.put("switchTrackBlueSelectorChecked", "listSelectorSDK21");
        fallbackKeys.put("chat_emojiBottomPanelIcon", "chat_emojiPanelIcon");
        fallbackKeys.put("chat_emojiSearchIcon", "chat_emojiPanelIcon");
        fallbackKeys.put("chat_emojiPanelStickerSetNameHighlight", "windowBackgroundWhiteBlueText4");
        fallbackKeys.put("chat_emojiPanelStickerPackSelectorLine", "chat_emojiPanelIconSelected");
        fallbackKeys.put("sharedMedia_actionMode", "actionBarDefault");
        fallbackKeys.put("key_sheet_scrollUp", "chat_emojiPanelStickerPackSelector");
        fallbackKeys.put("key_sheet_other", "player_actionBarItems");
        fallbackKeys.put("dialogSearchBackground", "chat_emojiPanelStickerPackSelector");
        fallbackKeys.put("dialogSearchHint", "chat_emojiPanelIcon");
        fallbackKeys.put("dialogSearchIcon", "chat_emojiPanelIcon");
        fallbackKeys.put("dialogSearchText", "windowBackgroundWhiteBlackText");
        fallbackKeys.put("dialogFloatingButton", "dialogRoundCheckBox");
        fallbackKeys.put("dialogFloatingButtonPressed", "dialogRoundCheckBox");
        fallbackKeys.put("dialogFloatingIcon", "dialogRoundCheckBoxCheck");
        fallbackKeys.put("dialogShadowLine", "chat_emojiPanelShadowLine");
        fallbackKeys.put("actionBarDefaultArchived", "actionBarDefault");
        fallbackKeys.put("actionBarDefaultArchivedSelector", "actionBarDefaultSelector");
        fallbackKeys.put("actionBarDefaultArchivedIcon", "actionBarDefaultIcon");
        fallbackKeys.put("actionBarDefaultArchivedTitle", "actionBarDefaultTitle");
        fallbackKeys.put("actionBarDefaultArchivedSearch", "actionBarDefaultSearch");
        fallbackKeys.put("actionBarDefaultSearchArchivedPlaceholder", "actionBarDefaultSearchPlaceholder");
        fallbackKeys.put("chats_message_threeLines", "chats_message");
        fallbackKeys.put("chats_nameMessage_threeLines", "chats_nameMessage");
        fallbackKeys.put("chats_nameArchived", "chats_name");
        fallbackKeys.put("chats_nameMessageArchived", "chats_nameMessage");
        fallbackKeys.put("chats_nameMessageArchived_threeLines", "chats_nameMessage");
        fallbackKeys.put("chats_messageArchived", "chats_message");
        fallbackKeys.put("avatar_backgroundArchived", "chats_unreadCounterMuted");
        fallbackKeys.put("chats_archiveBackground", "chats_actionBackground");
        fallbackKeys.put("chats_archivePinBackground", "chats_unreadCounterMuted");
        fallbackKeys.put("chats_archiveIcon", "chats_actionIcon");
        fallbackKeys.put("chats_archiveText", "chats_actionIcon");
        fallbackKeys.put("actionBarDefaultSubmenuItemIcon", "dialogIcon");
        fallbackKeys.put("checkboxDisabled", "chats_unreadCounterMuted");
        fallbackKeys.put("chat_status", "actionBarDefaultSubtitle");
        fallbackKeys.put("chat_inDownCall", "calls_callReceivedGreenIcon");
        fallbackKeys.put("chat_inUpCall", "calls_callReceivedRedIcon");
        fallbackKeys.put("chat_outUpCall", "calls_callReceivedGreenIcon");
        fallbackKeys.put("actionBarTabActiveText", "actionBarDefaultTitle");
        fallbackKeys.put("actionBarTabUnactiveText", "actionBarDefaultSubtitle");
        fallbackKeys.put("actionBarTabLine", "actionBarDefaultTitle");
        fallbackKeys.put("actionBarTabSelector", "actionBarDefaultSelector");
        fallbackKeys.put("profile_status", "avatar_subtitleInProfileBlue");
        fallbackKeys.put("chats_menuTopBackgroundCats", "avatar_backgroundActionBarBlue");
        fallbackKeys.put("chat_outLinkSelectBackground", "chat_linkSelectBackground");
        fallbackKeys.put("actionBarDefaultSubmenuSeparator", "windowBackgroundGray");
        fallbackKeys.put("chat_attachPermissionImage", "dialogTextBlack");
        fallbackKeys.put("chat_attachPermissionMark", "chat_sentError");
        fallbackKeys.put("chat_attachPermissionText", "dialogTextBlack");
        fallbackKeys.put("chat_attachEmptyImage", "emptyListPlaceholder");
        fallbackKeys.put("actionBarBrowser", "actionBarDefault");
        fallbackKeys.put("chats_sentReadCheck", "chats_sentCheck");
        fallbackKeys.put("chat_outSentCheckRead", "chat_outSentCheck");
        fallbackKeys.put("chat_outSentCheckReadSelected", "chat_outSentCheckSelected");
        fallbackKeys.put("chats_archivePullDownBackground", "chats_unreadCounterMuted");
        fallbackKeys.put("chats_archivePullDownBackgroundActive", "chats_actionBackground");
        fallbackKeys.put("avatar_backgroundArchivedHidden", "avatar_backgroundSaved");
        fallbackKeys.put("featuredStickers_removeButtonText", "featuredStickers_addButtonPressed");
        fallbackKeys.put("dialogEmptyImage", "player_time");
        fallbackKeys.put("dialogEmptyText", "player_time");
        fallbackKeys.put("location_actionIcon", "dialogTextBlack");
        fallbackKeys.put("location_actionActiveIcon", "windowBackgroundWhiteBlueText7");
        fallbackKeys.put("location_actionBackground", "dialogBackground");
        fallbackKeys.put("location_actionPressedBackground", "dialogBackgroundGray");
        fallbackKeys.put("location_sendLocationText", "windowBackgroundWhiteBlueText7");
        fallbackKeys.put("location_sendLiveLocationText", "windowBackgroundWhiteGreenText");
        fallbackKeys.put("chat_outTextSelectionHighlight", "chat_textSelectBackground");
        fallbackKeys.put("chat_inTextSelectionHighlight", "chat_textSelectBackground");
        fallbackKeys.put("chat_TextSelectionCursor", "chat_messagePanelCursor");
        fallbackKeys.put("chat_outTextSelectionCursor", "chat_TextSelectionCursor");
        fallbackKeys.put("chat_inPollCorrectAnswer", "chat_attachLocationBackground");
        fallbackKeys.put("chat_outPollCorrectAnswer", "chat_attachLocationBackground");
        fallbackKeys.put("chat_inPollWrongAnswer", "chat_attachAudioBackground");
        fallbackKeys.put("chat_outPollWrongAnswer", "chat_attachAudioBackground");
        fallbackKeys.put("profile_tabText", "windowBackgroundWhiteGrayText");
        fallbackKeys.put("profile_tabSelectedText", "windowBackgroundWhiteBlueHeader");
        fallbackKeys.put("profile_tabSelectedLine", "windowBackgroundWhiteBlueHeader");
        fallbackKeys.put("profile_tabSelector", "listSelectorSDK21");
        fallbackKeys.put("statisticChartPopupBackground", "dialogBackground");
        fallbackKeys.put("chat_attachGalleryText", "chat_attachGalleryBackground");
        fallbackKeys.put("chat_attachAudioText", "chat_attachAudioBackground");
        fallbackKeys.put("chat_attachFileText", "chat_attachFileBackground");
        fallbackKeys.put("chat_attachContactText", "chat_attachContactBackground");
        fallbackKeys.put("chat_attachLocationText", "chat_attachLocationBackground");
        fallbackKeys.put("chat_attachPollText", "chat_attachPollBackground");
        fallbackKeys.put("chat_inPsaNameText", "avatar_nameInMessageGreen");
        fallbackKeys.put("chat_outPsaNameText", "avatar_nameInMessageGreen");
        fallbackKeys.put("chat_outAdminText", "chat_outTimeText");
        fallbackKeys.put("chat_outAdminSelectedText", "chat_outTimeSelectedText");
        fallbackKeys.put("returnToCallMutedBackground", "windowBackgroundWhite");
        fallbackKeys.put("dialogSwipeRemove", "avatar_backgroundRed");
        fallbackKeys.put("chat_inReactionButtonBackground", "chat_inLoader");
        fallbackKeys.put("chat_outReactionButtonBackground", "chat_outLoader");
        fallbackKeys.put("chat_inReactionButtonText", "chat_inPreviewInstantText");
        fallbackKeys.put("chat_outReactionButtonText", "chat_outPreviewInstantText");
        fallbackKeys.put("chat_inReactionButtonTextSelected", "windowBackgroundWhite");
        fallbackKeys.put("chat_outReactionButtonTextSelected", "windowBackgroundWhite");
        fallbackKeys.put("dialogReactionMentionBackground", "voipgroup_mutedByAdminGradient2");
        themeAccentExclusionKeys.addAll(Arrays.asList(keys_avatar_background));
        themeAccentExclusionKeys.addAll(Arrays.asList(keys_avatar_nameInMessage));
        themeAccentExclusionKeys.add("chat_attachFileBackground");
        themeAccentExclusionKeys.add("chat_attachGalleryBackground");
        themeAccentExclusionKeys.add("chat_attachFileText");
        themeAccentExclusionKeys.add("chat_attachGalleryText");
        themeAccentExclusionKeys.add("statisticChartLine_blue");
        themeAccentExclusionKeys.add("statisticChartLine_green");
        themeAccentExclusionKeys.add("statisticChartLine_red");
        themeAccentExclusionKeys.add("statisticChartLine_golden");
        themeAccentExclusionKeys.add("statisticChartLine_lightblue");
        themeAccentExclusionKeys.add("statisticChartLine_lightgreen");
        themeAccentExclusionKeys.add("statisticChartLine_orange");
        themeAccentExclusionKeys.add("statisticChartLine_indigo");
        themeAccentExclusionKeys.add("voipgroup_checkMenu");
        themeAccentExclusionKeys.add("voipgroup_muteButton");
        themeAccentExclusionKeys.add("voipgroup_muteButton2");
        themeAccentExclusionKeys.add("voipgroup_muteButton3");
        themeAccentExclusionKeys.add("voipgroup_searchText");
        themeAccentExclusionKeys.add("voipgroup_searchPlaceholder");
        themeAccentExclusionKeys.add("voipgroup_searchBackground");
        themeAccentExclusionKeys.add("voipgroup_leaveCallMenu");
        themeAccentExclusionKeys.add("voipgroup_scrollUp");
        themeAccentExclusionKeys.add("voipgroup_blueText");
        themeAccentExclusionKeys.add("voipgroup_soundButton");
        themeAccentExclusionKeys.add("voipgroup_soundButtonActive");
        themeAccentExclusionKeys.add("voipgroup_soundButtonActiveScrolled");
        themeAccentExclusionKeys.add("voipgroup_soundButton2");
        themeAccentExclusionKeys.add("voipgroup_soundButtonActive2");
        themeAccentExclusionKeys.add("voipgroup_soundButtonActive2Scrolled");
        themeAccentExclusionKeys.add("voipgroup_leaveButton");
        themeAccentExclusionKeys.add("voipgroup_leaveButtonScrolled");
        themeAccentExclusionKeys.add("voipgroup_connectingProgress");
        themeAccentExclusionKeys.add("voipgroup_disabledButton");
        themeAccentExclusionKeys.add("voipgroup_disabledButtonActive");
        themeAccentExclusionKeys.add("voipgroup_disabledButtonActiveScrolled");
        themeAccentExclusionKeys.add("voipgroup_unmuteButton");
        themeAccentExclusionKeys.add("voipgroup_unmuteButton2");
        themeAccentExclusionKeys.add("voipgroup_actionBarUnscrolled");
        themeAccentExclusionKeys.add("voipgroup_listViewBackgroundUnscrolled");
        themeAccentExclusionKeys.add("voipgroup_lastSeenTextUnscrolled");
        themeAccentExclusionKeys.add("voipgroup_mutedIconUnscrolled");
        themeAccentExclusionKeys.add("voipgroup_actionBar");
        themeAccentExclusionKeys.add("voipgroup_emptyView");
        themeAccentExclusionKeys.add("voipgroup_actionBarItems");
        themeAccentExclusionKeys.add("voipgroup_actionBarSubtitle");
        themeAccentExclusionKeys.add("voipgroup_actionBarItemsSelector");
        themeAccentExclusionKeys.add("voipgroup_mutedByAdminIcon");
        themeAccentExclusionKeys.add("voipgroup_mutedIcon");
        themeAccentExclusionKeys.add("voipgroup_lastSeenText");
        themeAccentExclusionKeys.add("voipgroup_nameText");
        themeAccentExclusionKeys.add("voipgroup_listViewBackground");
        themeAccentExclusionKeys.add("voipgroup_listeningText");
        themeAccentExclusionKeys.add("voipgroup_speakingText");
        themeAccentExclusionKeys.add("voipgroup_listSelector");
        themeAccentExclusionKeys.add("voipgroup_inviteMembersBackground");
        themeAccentExclusionKeys.add("voipgroup_dialogBackground");
        themeAccentExclusionKeys.add("voipgroup_overlayGreen1");
        themeAccentExclusionKeys.add("voipgroup_overlayGreen2");
        themeAccentExclusionKeys.add("voipgroup_overlayBlue1");
        themeAccentExclusionKeys.add("voipgroup_overlayBlue2");
        themeAccentExclusionKeys.add("voipgroup_topPanelGreen1");
        themeAccentExclusionKeys.add("voipgroup_topPanelGreen2");
        themeAccentExclusionKeys.add("voipgroup_topPanelBlue1");
        themeAccentExclusionKeys.add("voipgroup_topPanelBlue2");
        themeAccentExclusionKeys.add("voipgroup_topPanelGray");
        themeAccentExclusionKeys.add("voipgroup_overlayAlertGradientMuted");
        themeAccentExclusionKeys.add("voipgroup_overlayAlertGradientMuted2");
        themeAccentExclusionKeys.add("voipgroup_overlayAlertGradientUnmuted");
        themeAccentExclusionKeys.add("voipgroup_overlayAlertGradientUnmuted2");
        themeAccentExclusionKeys.add("voipgroup_overlayAlertMutedByAdmin");
        themeAccentExclusionKeys.add("kvoipgroup_overlayAlertMutedByAdmin2");
        themeAccentExclusionKeys.add("voipgroup_mutedByAdminGradient");
        themeAccentExclusionKeys.add("voipgroup_mutedByAdminGradient2");
        themeAccentExclusionKeys.add("voipgroup_mutedByAdminGradient3");
        themeAccentExclusionKeys.add("voipgroup_mutedByAdminMuteButton");
        themeAccentExclusionKeys.add("voipgroup_mutedByAdminMuteButtonDisabled");
        themeAccentExclusionKeys.add("voipgroup_windowBackgroundWhiteInputField");
        themeAccentExclusionKeys.add("voipgroup_windowBackgroundWhiteInputFieldActivated");
        themeAccentExclusionKeys.add("premiumGradient1");
        themeAccentExclusionKeys.add("premiumGradient2");
        themeAccentExclusionKeys.add("premiumGradient3");
        themeAccentExclusionKeys.add("premiumGradient4");
        myMessagesBubblesColorKeys.add("chat_outBubble");
        myMessagesBubblesColorKeys.add("chat_outBubbleSelected");
        myMessagesBubblesColorKeys.add("chat_outBubbleShadow");
        myMessagesBubblesColorKeys.add("chat_outBubbleGradient");
        myMessagesColorKeys.add("chat_outUpCall");
        myMessagesColorKeys.add("chat_outSentCheck");
        myMessagesColorKeys.add("chat_outSentCheckSelected");
        myMessagesColorKeys.add("chat_outSentCheckRead");
        myMessagesColorKeys.add("chat_outSentCheckReadSelected");
        myMessagesColorKeys.add("chat_outSentClock");
        myMessagesColorKeys.add("chat_outSentClockSelected");
        myMessagesColorKeys.add("chat_outMediaIcon");
        myMessagesColorKeys.add("chat_outMediaIconSelected");
        myMessagesColorKeys.add("chat_outViews");
        myMessagesColorKeys.add("chat_outViewsSelected");
        myMessagesColorKeys.add("chat_outMenu");
        myMessagesColorKeys.add("chat_outMenuSelected");
        myMessagesColorKeys.add("chat_outInstant");
        myMessagesColorKeys.add("chat_outInstantSelected");
        myMessagesColorKeys.add("chat_outPreviewInstantText");
        myMessagesColorKeys.add("chat_outPreviewInstantSelectedText");
        myMessagesColorKeys.add("chat_outForwardedNameText");
        myMessagesColorKeys.add("chat_outViaBotNameText");
        myMessagesColorKeys.add("chat_outReplyLine");
        myMessagesColorKeys.add("chat_outReplyNameText");
        myMessagesColorKeys.add("chat_outReplyMessageText");
        myMessagesColorKeys.add("chat_outReplyMediaMessageText");
        myMessagesColorKeys.add("chat_outReplyMediaMessageSelectedText");
        myMessagesColorKeys.add("chat_outPreviewLine");
        myMessagesColorKeys.add("chat_outSiteNameText");
        myMessagesColorKeys.add("chat_outContactNameText");
        myMessagesColorKeys.add("chat_outContactPhoneText");
        myMessagesColorKeys.add("chat_outContactPhoneSelectedText");
        myMessagesColorKeys.add("chat_outAudioProgress");
        myMessagesColorKeys.add("chat_outAudioSelectedProgress");
        myMessagesColorKeys.add("chat_outTimeText");
        myMessagesColorKeys.add("chat_outTimeSelectedText");
        myMessagesColorKeys.add("chat_outAudioPerfomerText");
        myMessagesColorKeys.add("chat_outAudioPerfomerSelectedText");
        myMessagesColorKeys.add("chat_outAudioTitleText");
        myMessagesColorKeys.add("chat_outAudioDurationText");
        myMessagesColorKeys.add("chat_outAudioDurationSelectedText");
        myMessagesColorKeys.add("chat_outAudioSeekbar");
        myMessagesColorKeys.add("chat_outAudioCacheSeekbar");
        myMessagesColorKeys.add("chat_outAudioSeekbarSelected");
        myMessagesColorKeys.add("chat_outAudioSeekbarFill");
        myMessagesColorKeys.add("chat_outVoiceSeekbar");
        myMessagesColorKeys.add("chat_outVoiceSeekbarSelected");
        myMessagesColorKeys.add("chat_outVoiceSeekbarFill");
        myMessagesColorKeys.add("chat_outFileProgress");
        myMessagesColorKeys.add("chat_outFileProgressSelected");
        myMessagesColorKeys.add("chat_outFileNameText");
        myMessagesColorKeys.add("chat_outFileInfoText");
        myMessagesColorKeys.add("chat_outFileInfoSelectedText");
        myMessagesColorKeys.add("chat_outFileBackground");
        myMessagesColorKeys.add("chat_outFileBackgroundSelected");
        myMessagesColorKeys.add("chat_outVenueInfoText");
        myMessagesColorKeys.add("chat_outVenueInfoSelectedText");
        myMessagesColorKeys.add("chat_outLoader");
        myMessagesColorKeys.add("chat_outLoaderSelected");
        myMessagesColorKeys.add("chat_outLoaderPhoto");
        myMessagesColorKeys.add("chat_outLoaderPhotoSelected");
        myMessagesColorKeys.add("chat_outLoaderPhotoIcon");
        myMessagesColorKeys.add("chat_outLoaderPhotoIconSelected");
        myMessagesColorKeys.add("chat_outLocationBackground");
        myMessagesColorKeys.add("chat_outLocationIcon");
        myMessagesColorKeys.add("chat_outContactBackground");
        myMessagesColorKeys.add("chat_outContactIcon");
        myMessagesColorKeys.add("chat_outFileIcon");
        myMessagesColorKeys.add("chat_outFileSelectedIcon");
        myMessagesColorKeys.add("chat_outBroadcast");
        myMessagesColorKeys.add("chat_messageTextOut");
        myMessagesColorKeys.add("chat_messageLinkOut");
        SharedPreferences sharedPreferences2 = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
        ThemeInfo themeInfo2 = new ThemeInfo();
        themeInfo2.name = "Blue";
        themeInfo2.assetName = "bluebubbles.attheme";
        themeInfo2.previewBackgroundColor = -6963476;
        themeInfo2.previewInColor = -1;
        themeInfo2.previewOutColor = -3086593;
        themeInfo2.firstAccentIsDefault = true;
        themeInfo2.currentAccentId = DEFALT_THEME_ACCENT_ID;
        themeInfo2.sortIndex = 1;
        themeInfo2.setAccentColorOptions(new int[]{-10972987, -14444461, -3252606, -8428605, -14380627, -14050257, -7842636, -13464881, -12342073, -11359164, -3317869, -2981834, -8165684, -3256745, -2904512, -8681301}, new int[]{-4660851, -328756, -1572, -4108434, -3031781, -1335, -198952, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, -853047, -264993, 0, 0, -135756, -198730, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, -2104672, -937328, -2637335, -2639714, -1270157, -3428124, -6570777, -7223828, -6567550, -1793599, -1855875, -4674838, -1336199, -2900876, -6247730}, new int[]{0, -4532067, -1257580, -1524266, -1646910, -1519483, -1324823, -4138509, -4202516, -2040429, -1458474, -1256030, -3814930, -1000039, -1450082, -3485987}, new int[]{0, -1909081, -1592444, -2969879, -2439762, -1137033, -2119471, -6962197, -4857383, -4270699, -3364639, -2117514, -5000734, -1598028, -2045813, -5853742}, new int[]{0, -6371440, -1319256, -1258616, -1712961, -1186647, -1193816, -4467224, -4203544, -3023977, -1061929, -1255788, -2113811, -806526, -1715305, -3485976}, new int[]{99, 9, 10, 11, 12, 13, 14, 0, 1, 2, 3, 4, 5, 6, 7, 8}, new String[]{"", "p-pXcflrmFIBAAAAvXYQk-mCwZU", "JqSUrO0-mFIBAAAAWwTvLzoWGQI", "O-wmAfBPSFADAAAA4zINVfD_bro", "RepJ5uE_SVABAAAAr4d0YhgB850", "-Xc-np9y2VMCAAAARKr0yNNPYW0", "fqv01SQemVIBAAAApND8LDRUhRU", "fqv01SQemVIBAAAApND8LDRUhRU", "RepJ5uE_SVABAAAAr4d0YhgB850", "lp0prF8ISFAEAAAA_p385_CvG0w", "heptcj-hSVACAAAAC9RrMzOa-cs", "PllZ-bf_SFAEAAAA8crRfwZiDNg", "dhf9pceaQVACAAAAbzdVo4SCiZA", "Ujx2TFcJSVACAAAARJ4vLa50MkM", "p-pXcflrmFIBAAAAvXYQk-mCwZU", "dk_wwlghOFACAAAAfz9xrxi6euw"}, new int[]{0, 180, 45, 0, 45, 180, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 52, 46, 57, 45, 64, 52, 35, 36, 41, 50, 50, 35, 38, 37, 30});
        sortAccents(themeInfo2);
        ArrayList<ThemeInfo> arrayList = themes;
        defaultTheme = themeInfo2;
        currentTheme = themeInfo2;
        currentDayTheme = themeInfo2;
        arrayList.add(themeInfo2);
        themesDict.put("Blue", themeInfo2);
        ThemeInfo themeInfo3 = new ThemeInfo();
        themeInfo3.name = "Dark Blue";
        themeInfo3.assetName = "darkblue.attheme";
        themeInfo3.previewBackgroundColor = -10523006;
        themeInfo3.previewInColor = -9009508;
        themeInfo3.previewOutColor = -8214301;
        themeInfo3.sortIndex = 3;
        themeInfo3.setAccentColorOptions(new int[]{-7177260, -9860357, -14440464, -8687151, -9848491, -14053142, -9403671, -10044691, -13203974, -12138259, -10179489, -1344335, -1142742, -6127120, -2931932, -1131212, -8417365, -13270557}, new int[]{-6464359, -10267323, -13532789, -5413850, -11898828, -13410942, -13215889, -10914461, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-10465880, -9937588, -14983040, -6736562, -14197445, -13534568, -13144441, -10587280, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-14213586, -15263198, -16310753, -15724781, -15853551, -16051428, -14868183, -14668758, -15854566, -15326427, -15327979, -14411490, -14345453, -14738135, -14543346, -14212843, -15263205, -15854566}, new int[]{-15659501, -14277074, -15459034, -14542297, -14735336, -15129808, -15591910, -15459810, -15260623, -15853800, -15259879, -14477540, -14674936, -15461604, -13820650, -15067635, -14605528, -15260623}, new int[]{-13951445, -15395557, -15985382, -15855853, -16050417, -15525854, -15260627, -15327189, -15788258, -14799314, -15458796, -13952727, -13754603, -14081231, -14478324, -14081004, -15197667, -15788258}, new int[]{-15330777, -15066858, -15915220, -14213847, -15262439, -15260879, -15657695, -16443625, -15459285, -15589601, -14932454, -14740451, -15002870, -15264997, -13821660, -14805234, -14605784, -15459285}, new int[]{11, 12, 13, 14, 15, 16, 17, 18, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, new String[]{"O-wmAfBPSFADAAAA4zINVfD_bro", "RepJ5uE_SVABAAAAr4d0YhgB850", "dk_wwlghOFACAAAAfz9xrxi6euw", "9LW_RcoOSVACAAAAFTk3DTyXN-M", "PllZ-bf_SFAEAAAA8crRfwZiDNg", "-Xc-np9y2VMCAAAARKr0yNNPYW0", "kO4jyq55SFABAAAA0WEpcLfahXk", "CJNyxPMgSVAEAAAAvW9sMwc51cw", "fqv01SQemVIBAAAApND8LDRUhRU", "RepJ5uE_SVABAAAAr4d0YhgB850", "CJNyxPMgSVAEAAAAvW9sMwc51cw", "9LW_RcoOSVACAAAAFTk3DTyXN-M", "9GcNVISdSVADAAAAUcw5BYjELW4", "F5oWoCs7QFACAAAAgf2bD_mg8Bw", "9ShF73d1MFIIAAAAjWnm8_ZMe8Q", "3rX-PaKbSFACAAAAEiHNvcEm6X4", "dk_wwlghOFACAAAAfz9xrxi6euw", "fqv01SQemVIBAAAApND8LDRUhRU"}, new int[]{225, 45, 225, 135, 45, 225, 45, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{40, 40, 31, 50, 25, 34, 35, 35, 38, 29, 24, 34, 34, 31, 29, 37, 21, 38});
        sortAccents(themeInfo3);
        themes.add(themeInfo3);
        HashMap<String, ThemeInfo> hashMap = themesDict;
        currentNightTheme = themeInfo3;
        hashMap.put("Dark Blue", themeInfo3);
        ThemeInfo themeInfo4 = new ThemeInfo();
        themeInfo4.name = "Arctic Blue";
        themeInfo4.assetName = "arctic.attheme";
        themeInfo4.previewBackgroundColor = -1971728;
        themeInfo4.previewInColor = -1;
        themeInfo4.previewOutColor = -9657877;
        themeInfo4.sortIndex = 5;
        themeInfo4.setAccentColorOptions(new int[]{-12537374, -12472227, -3240928, -11033621, -2194124, -3382903, -13332245, -12342073, -11359164, -3317869, -2981834, -8165684, -3256745, -2904512, -8681301}, new int[]{-13525046, -14113959, -7579073, -13597229, -3581840, -8883763, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-11616542, -9716647, -6400452, -12008744, -2592697, -4297041, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-3808528, -2433367, -2700891, -1838093, -1120848, -1712148, -2037779, -4202261, -4005713, -1058332, -925763, -1975316, -1189672, -1318451, -2302235}, new int[]{-1510157, -4398164, -1647697, -3610898, -1130838, -1980692, -4270093, -4202261, -3415654, -1259815, -1521765, -4341268, -1127744, -1318219, -3945761}, new int[]{-4924688, -3283031, -1523567, -2494477, -1126510, -595210, -2037517, -3478548, -4661623, -927514, -796762, -2696971, -1188403, -1319735, -1577487}, new int[]{-3149585, -5714021, -1978209, -4925720, -1134713, -1718833, -3613709, -5317397, -3218014, -999207, -2116466, -4343054, -931397, -1583186, -3815718}, new int[]{9, 10, 11, 12, 13, 14, 0, 1, 2, 3, 4, 5, 6, 7, 8}, new String[]{"MIo6r0qGSFAFAAAAtL8TsDzNX60", "dhf9pceaQVACAAAAbzdVo4SCiZA", "fqv01SQemVIBAAAApND8LDRUhRU", "p-pXcflrmFIBAAAAvXYQk-mCwZU", "JqSUrO0-mFIBAAAAWwTvLzoWGQI", "F5oWoCs7QFACAAAAgf2bD_mg8Bw", "fqv01SQemVIBAAAApND8LDRUhRU", "RepJ5uE_SVABAAAAr4d0YhgB850", "PllZ-bf_SFAEAAAA8crRfwZiDNg", "pgJfpFNRSFABAAAACDT8s5sEjfc", "ptuUd96JSFACAAAATobI23sPpz0", "dhf9pceaQVACAAAAbzdVo4SCiZA", "JqSUrO0-mFIBAAAAWwTvLzoWGQI", "9iklpvIPQVABAAAAORQXKur_Eyc", "F5oWoCs7QFACAAAAgf2bD_mg8Bw"}, new int[]{315, 315, 225, 315, 0, 180, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{50, 50, 58, 47, 46, 50, 49, 46, 51, 50, 49, 34, 54, 50, 40});
        sortAccents(themeInfo4);
        themes.add(themeInfo4);
        themesDict.put("Arctic Blue", themeInfo4);
        ThemeInfo themeInfo5 = new ThemeInfo();
        themeInfo5.name = "Day";
        themeInfo5.assetName = "day.attheme";
        themeInfo5.previewBackgroundColor = -1;
        themeInfo5.previewInColor = -1315084;
        themeInfo5.previewOutColor = -8604930;
        themeInfo5.sortIndex = 2;
        themeInfo5.setAccentColorOptions(new int[]{-11099447, -3379581, -3109305, -3382174, -7963438, -11759137, -11029287, -11226775, -2506945, -3382174, -3379581, -6587438, -2649788, -8681301}, new int[]{-10125092, -9671214, -3451775, -3978678, -10711329, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-12664362, -3642988, -2383569, -3109317, -11422261, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, null, null, new int[]{9, 10, 11, 12, 13, 0, 1, 2, 3, 4, 5, 6, 7, 8}, new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", ""}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        sortAccents(themeInfo5);
        themes.add(themeInfo5);
        themesDict.put("Day", themeInfo5);
        ThemeInfo themeInfo6 = new ThemeInfo();
        themeInfo6.name = "Night";
        themeInfo6.assetName = "night.attheme";
        themeInfo6.previewBackgroundColor = -11315623;
        themeInfo6.previewInColor = -9143676;
        themeInfo6.previewOutColor = -9067802;
        themeInfo6.sortIndex = 4;
        themeInfo6.setAccentColorOptions(new int[]{-9781697, -7505693, -2204034, -10913816, -2375398, -12678921, -11881005, -11880383, -2534026, -1934037, -7115558, -3128522, -1528292, -8812381}, new int[]{-7712108, -4953061, -5288081, -14258547, -9154889, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-9939525, -5948598, -10335844, -13659747, -14054507, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-15330532, -14806760, -15791344, -16184308, -16313063, -15921641, -15656164, -15986420, -15856883, -14871025, -16185078, -14937584, -14869736, -15855598}, new int[]{-14673881, -15724781, -15002342, -15458526, -15987697, -16184820, -16118258, -16250616, -15067624, -15527923, -14804447, -15790836, -15987960, -16316665}, new int[]{-15856877, -14608861, -15528430, -15921391, -15722209, -15197144, -15458015, -15591406, -15528431, -15068401, -16053749, -15594229, -15395825, -15724012}, new int[]{-14804694, -15658986, -14609382, -15656421, -16118509, -15855854, -16315381, -16052981, -14544354, -15791092, -15659241, -16316922, -15988214, -16185077}, new int[]{9, 10, 11, 12, 13, 0, 1, 2, 3, 4, 5, 6, 7, 8}, new String[]{"YIxYGEALQVADAAAAA3QbEH0AowY", "9LW_RcoOSVACAAAAFTk3DTyXN-M", "O-wmAfBPSFADAAAA4zINVfD_bro", "F5oWoCs7QFACAAAAgf2bD_mg8Bw", "-Xc-np9y2VMCAAAARKr0yNNPYW0", "fqv01SQemVIBAAAApND8LDRUhRU", "F5oWoCs7QFACAAAAgf2bD_mg8Bw", "ptuUd96JSFACAAAATobI23sPpz0", "p-pXcflrmFIBAAAAvXYQk-mCwZU", "Nl8Pg2rBQVACAAAA25Lxtb8SDp0", "dhf9pceaQVACAAAAbzdVo4SCiZA", "9GcNVISdSVADAAAAUcw5BYjELW4", "9LW_RcoOSVACAAAAFTk3DTyXN-M", "dk_wwlghOFACAAAAfz9xrxi6euw"}, new int[]{45, 135, 0, 180, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{34, 47, 52, 48, 54, 50, 37, 56, 48, 49, 40, 64, 38, 48});
        sortAccents(themeInfo6);
        themes.add(themeInfo6);
        themesDict.put("Night", themeInfo6);
        String string = sharedPreferences2.getString("themes2", null);
        if (sharedPreferences2.getInt("remote_version", 0) == 1) {
            int i = 0;
            while (i < 4) {
                long[] jArr = remoteThemesHash;
                StringBuilder sb = new StringBuilder();
                sb.append("2remoteThemesHash");
                sb.append(i != 0 ? Integer.valueOf(i) : "");
                jArr[i] = sharedPreferences2.getLong(sb.toString(), 0L);
                int[] iArr = lastLoadingThemesTime;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("lastLoadingThemesTime");
                sb2.append(i != 0 ? Integer.valueOf(i) : "");
                iArr[i] = sharedPreferences2.getInt(sb2.toString(), 0);
                i++;
            }
        }
        sharedPreferences2.edit().putInt("remote_version", 1).apply();
        if (!TextUtils.isEmpty(string)) {
            try {
                JSONArray jSONArray = new JSONArray(string);
                for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                    ThemeInfo createWithJson = ThemeInfo.createWithJson(jSONArray.getJSONObject(i2));
                    if (createWithJson != null) {
                        otherThemes.add(createWithJson);
                        themes.add(createWithJson);
                        themesDict.put(createWithJson.getKey(), createWithJson);
                        createWithJson.loadWallpapers(sharedPreferences2);
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else {
            String string2 = sharedPreferences2.getString("themes", null);
            if (!TextUtils.isEmpty(string2)) {
                for (String str : string2.split("&")) {
                    ThemeInfo createWithString = ThemeInfo.createWithString(str);
                    if (createWithString != null) {
                        otherThemes.add(createWithString);
                        themes.add(createWithString);
                        themesDict.put(createWithString.getKey(), createWithString);
                    }
                }
                saveOtherThemes(true, true);
                sharedPreferences2.edit().remove("themes").commit();
            }
        }
        sortThemes();
        ThemeInfo themeInfo7 = null;
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        try {
            ThemeInfo themeInfo8 = themesDict.get("Dark Blue");
            String string3 = globalMainSettings.getString("theme", null);
            if ("Default".equals(string3)) {
                themeInfo7 = themesDict.get("Blue");
                themeInfo7.currentAccentId = DEFALT_THEME_ACCENT_ID;
            } else if ("Dark".equals(string3)) {
                themeInfo8.currentAccentId = 9;
                themeInfo7 = themeInfo8;
            } else if (string3 != null && (themeInfo7 = themesDict.get(string3)) != null && !sharedPreferences2.contains("lastDayTheme")) {
                SharedPreferences.Editor edit = sharedPreferences2.edit();
                edit.putString("lastDayTheme", themeInfo7.getKey());
                edit.commit();
            }
            String string4 = globalMainSettings.getString("nighttheme", null);
            if ("Default".equals(string4)) {
                themeInfo7 = themesDict.get("Blue");
                themeInfo7.currentAccentId = DEFALT_THEME_ACCENT_ID;
            } else if ("Dark".equals(string4)) {
                currentNightTheme = themeInfo8;
                themeInfo8.currentAccentId = 9;
            } else if (string4 != null && (themeInfo = themesDict.get(string4)) != null) {
                currentNightTheme = themeInfo;
            }
            if (currentNightTheme != null && !sharedPreferences2.contains("lastDarkTheme")) {
                SharedPreferences.Editor edit2 = sharedPreferences2.edit();
                edit2.putString("lastDarkTheme", currentNightTheme.getKey());
                edit2.commit();
            }
            SharedPreferences.Editor editor3 = null;
            SharedPreferences.Editor editor4 = null;
            Iterator<ThemeInfo> it3 = themesDict.values().iterator();
            while (it3.hasNext()) {
                ThemeInfo next = it3.next();
                if (next.assetName == null || next.accentBaseColor == 0) {
                    it = it3;
                    editor4 = editor4;
                } else {
                    String string5 = sharedPreferences2.getString("accents_" + next.assetName, null);
                    next.currentAccentId = sharedPreferences2.getInt("accent_current_" + next.assetName, next.firstAccentIsDefault ? DEFALT_THEME_ACCENT_ID : 0);
                    ArrayList arrayList2 = new ArrayList();
                    if (!TextUtils.isEmpty(string5)) {
                        SerializedData serializedData = new SerializedData(Base64.decode(string5, 3));
                        boolean z5 = true;
                        int readInt32 = serializedData.readInt32(true);
                        int readInt322 = serializedData.readInt32(true);
                        int i3 = 0;
                        while (i3 < readInt322) {
                            ThemeAccent themeAccent = new ThemeAccent();
                            themeAccent.id = serializedData.readInt32(z5);
                            themeAccent.accentColor = serializedData.readInt32(z5);
                            if (readInt32 >= 9) {
                                themeAccent.accentColor2 = serializedData.readInt32(z5);
                            }
                            themeAccent.parentTheme = next;
                            themeAccent.myMessagesAccentColor = serializedData.readInt32(true);
                            themeAccent.myMessagesGradientAccentColor1 = serializedData.readInt32(true);
                            if (readInt32 >= 7) {
                                themeAccent.myMessagesGradientAccentColor2 = serializedData.readInt32(true);
                                themeAccent.myMessagesGradientAccentColor3 = serializedData.readInt32(true);
                            }
                            if (readInt32 >= 8) {
                                z4 = true;
                                themeAccent.myMessagesAnimated = serializedData.readBool(true);
                            } else {
                                z4 = true;
                            }
                            if (readInt32 >= 3) {
                                editor2 = editor4;
                                it2 = it3;
                                themeAccent.backgroundOverrideColor = serializedData.readInt64(z4);
                            } else {
                                editor2 = editor4;
                                it2 = it3;
                                themeAccent.backgroundOverrideColor = serializedData.readInt32(z4);
                            }
                            if (readInt32 >= 2) {
                                themeAccent.backgroundGradientOverrideColor1 = serializedData.readInt64(z4);
                            } else {
                                themeAccent.backgroundGradientOverrideColor1 = serializedData.readInt32(z4);
                            }
                            ?? r1 = z4;
                            if (readInt32 >= 6) {
                                themeAccent.backgroundGradientOverrideColor2 = serializedData.readInt64(z4);
                                themeAccent.backgroundGradientOverrideColor3 = serializedData.readInt64(z4);
                                r1 = 1;
                            }
                            if (readInt32 >= r1) {
                                themeAccent.backgroundRotation = serializedData.readInt32(r1);
                            }
                            if (readInt32 >= 4) {
                                serializedData.readInt64(r1);
                                themeAccent.patternIntensity = (float) serializedData.readDouble(r1);
                                themeAccent.patternMotion = serializedData.readBool(r1);
                                if (readInt32 >= 5) {
                                    themeAccent.patternSlug = serializedData.readString(r1);
                                }
                            }
                            if (readInt32 >= 5 && serializedData.readBool(true)) {
                                themeAccent.account = serializedData.readInt32(true);
                                themeAccent.info = TLRPC$Theme.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                            }
                            TLRPC$TL_theme tLRPC$TL_theme = themeAccent.info;
                            if (tLRPC$TL_theme != null) {
                                themeAccent.isDefault = tLRPC$TL_theme.isDefault;
                            }
                            next.themeAccentsMap.put(themeAccent.id, themeAccent);
                            TLRPC$TL_theme tLRPC$TL_theme2 = themeAccent.info;
                            if (tLRPC$TL_theme2 != null) {
                                sharedPreferences = globalMainSettings;
                                next.accentsByThemeId.put(tLRPC$TL_theme2.id, themeAccent);
                            } else {
                                sharedPreferences = globalMainSettings;
                            }
                            arrayList2.add(themeAccent);
                            next.lastAccentId = Math.max(next.lastAccentId, themeAccent.id);
                            i3++;
                            it3 = it2;
                            globalMainSettings = sharedPreferences;
                            editor4 = editor2;
                            z5 = true;
                        }
                        editor = editor4;
                        it = it3;
                    } else {
                        editor = editor4;
                        it = it3;
                        String str2 = "accent_for_" + next.assetName;
                        globalMainSettings = globalMainSettings;
                        int i4 = globalMainSettings.getInt(str2, 0);
                        if (i4 != 0) {
                            if (editor3 == null) {
                                editor3 = globalMainSettings.edit();
                                editor4 = sharedPreferences2.edit();
                            } else {
                                editor4 = editor;
                            }
                            editor3.remove(str2);
                            int size = next.themeAccents.size();
                            int i5 = 0;
                            while (true) {
                                if (i5 >= size) {
                                    z3 = false;
                                    break;
                                }
                                ThemeAccent themeAccent2 = next.themeAccents.get(i5);
                                if (themeAccent2.accentColor == i4) {
                                    next.currentAccentId = themeAccent2.id;
                                    z3 = true;
                                    break;
                                }
                                i5++;
                            }
                            if (!z3) {
                                ThemeAccent themeAccent3 = new ThemeAccent();
                                themeAccent3.id = 100;
                                themeAccent3.accentColor = i4;
                                themeAccent3.parentTheme = next;
                                next.themeAccentsMap.put(100, themeAccent3);
                                arrayList2.add(0, themeAccent3);
                                next.currentAccentId = 100;
                                next.lastAccentId = FileLoader.MEDIA_DIR_VIDEO_PUBLIC;
                                SerializedData serializedData2 = new SerializedData(72);
                                serializedData2.writeInt32(9);
                                serializedData2.writeInt32(1);
                                serializedData2.writeInt32(themeAccent3.id);
                                serializedData2.writeInt32(themeAccent3.accentColor);
                                serializedData2.writeInt32(themeAccent3.myMessagesAccentColor);
                                serializedData2.writeInt32(themeAccent3.myMessagesGradientAccentColor1);
                                serializedData2.writeInt32(themeAccent3.myMessagesGradientAccentColor2);
                                serializedData2.writeInt32(themeAccent3.myMessagesGradientAccentColor3);
                                serializedData2.writeBool(themeAccent3.myMessagesAnimated);
                                serializedData2.writeInt64(themeAccent3.backgroundOverrideColor);
                                serializedData2.writeInt64(themeAccent3.backgroundGradientOverrideColor1);
                                serializedData2.writeInt64(themeAccent3.backgroundGradientOverrideColor2);
                                serializedData2.writeInt64(themeAccent3.backgroundGradientOverrideColor3);
                                serializedData2.writeInt32(themeAccent3.backgroundRotation);
                                serializedData2.writeInt64(0L);
                                serializedData2.writeDouble(themeAccent3.patternIntensity);
                                serializedData2.writeBool(themeAccent3.patternMotion);
                                serializedData2.writeString(themeAccent3.patternSlug);
                                serializedData2.writeBool(false);
                                editor4.putString("accents_" + next.assetName, Base64.encodeToString(serializedData2.toByteArray(), 3));
                            }
                            editor4.putInt("accent_current_" + next.assetName, next.currentAccentId);
                            if (!arrayList2.isEmpty()) {
                                next.themeAccents.addAll(0, arrayList2);
                                sortAccents(next);
                            }
                            sparseArray = next.themeAccentsMap;
                            if (sparseArray != null && sparseArray.get(next.currentAccentId) == null) {
                                next.currentAccentId = !next.firstAccentIsDefault ? DEFALT_THEME_ACCENT_ID : 0;
                            }
                            next.loadWallpapers(sharedPreferences2);
                            accent = next.getAccent(false);
                            if (accent == null) {
                                next.overrideWallpaper = accent.overrideWallpaper;
                            }
                        }
                    }
                    editor4 = editor;
                    if (!arrayList2.isEmpty()) {
                    }
                    sparseArray = next.themeAccentsMap;
                    if (sparseArray != null) {
                        next.currentAccentId = !next.firstAccentIsDefault ? DEFALT_THEME_ACCENT_ID : 0;
                    }
                    next.loadWallpapers(sharedPreferences2);
                    accent = next.getAccent(false);
                    if (accent == null) {
                    }
                }
                it3 = it;
            }
            SharedPreferences.Editor editor5 = editor4;
            if (editor3 != null) {
                editor3.commit();
                editor5.commit();
            }
            selectedAutoNightType = globalMainSettings.getInt("selectedAutoNightType", Build.VERSION.SDK_INT >= 29 ? 3 : 0);
            autoNightScheduleByLocation = globalMainSettings.getBoolean("autoNightScheduleByLocation", false);
            autoNightBrighnessThreshold = globalMainSettings.getFloat("autoNightBrighnessThreshold", 0.25f);
            autoNightDayStartTime = globalMainSettings.getInt("autoNightDayStartTime", 1320);
            autoNightDayEndTime = globalMainSettings.getInt("autoNightDayEndTime", 480);
            autoNightSunsetTime = globalMainSettings.getInt("autoNightSunsetTime", 1320);
            autoNightSunriseTime = globalMainSettings.getInt("autoNightSunriseTime", 480);
            autoNightCityName = globalMainSettings.getString("autoNightCityName", "");
            long j = globalMainSettings.getLong("autoNightLocationLatitude3", 10000L);
            if (j != 10000) {
                autoNightLocationLatitude = Double.longBitsToDouble(j);
            } else {
                autoNightLocationLatitude = 10000.0d;
            }
            long j2 = globalMainSettings.getLong("autoNightLocationLongitude3", 10000L);
            if (j2 != 10000) {
                autoNightLocationLongitude = Double.longBitsToDouble(j2);
            } else {
                autoNightLocationLongitude = 10000.0d;
            }
            autoNightLastSunCheckDay = globalMainSettings.getInt("autoNightLastSunCheckDay", -1);
            if (themeInfo7 == null) {
                themeInfo7 = defaultTheme;
            } else {
                currentDayTheme = themeInfo7;
            }
            if (globalMainSettings.contains("overrideThemeWallpaper") || globalMainSettings.contains("selectedBackground2")) {
                boolean z6 = globalMainSettings.getBoolean("overrideThemeWallpaper", false);
                long j3 = globalMainSettings.getLong("selectedBackground2", 1000001L);
                if (j3 == -1 || (z6 && j3 != -2 && j3 != 1000001)) {
                    OverrideWallpaperInfo overrideWallpaperInfo = new OverrideWallpaperInfo();
                    overrideWallpaperInfo.color = globalMainSettings.getInt("selectedColor", 0);
                    overrideWallpaperInfo.slug = globalMainSettings.getString("selectedBackgroundSlug", "");
                    if (j3 >= -100 && j3 <= -1 && overrideWallpaperInfo.color != 0) {
                        overrideWallpaperInfo.slug = "c";
                        overrideWallpaperInfo.fileName = "";
                        overrideWallpaperInfo.originalFileName = "";
                    } else {
                        overrideWallpaperInfo.fileName = "wallpaper.jpg";
                        overrideWallpaperInfo.originalFileName = "wallpaper_original.jpg";
                    }
                    overrideWallpaperInfo.gradientColor1 = globalMainSettings.getInt("selectedGradientColor", 0);
                    overrideWallpaperInfo.gradientColor2 = globalMainSettings.getInt("selectedGradientColor2", 0);
                    overrideWallpaperInfo.gradientColor3 = globalMainSettings.getInt("selectedGradientColor3", 0);
                    overrideWallpaperInfo.rotation = globalMainSettings.getInt("selectedGradientRotation", 45);
                    overrideWallpaperInfo.isBlurred = globalMainSettings.getBoolean("selectedBackgroundBlurred", false);
                    overrideWallpaperInfo.isMotion = globalMainSettings.getBoolean("selectedBackgroundMotion", false);
                    overrideWallpaperInfo.intensity = globalMainSettings.getFloat("selectedIntensity", 0.5f);
                    currentDayTheme.setOverrideWallpaper(overrideWallpaperInfo);
                    if (selectedAutoNightType != 0) {
                        currentNightTheme.setOverrideWallpaper(overrideWallpaperInfo);
                    }
                }
                globalMainSettings.edit().remove("overrideThemeWallpaper").remove("selectedBackground2").commit();
            }
            int needSwitchToTheme = needSwitchToTheme();
            if (needSwitchToTheme == 2) {
                themeInfo7 = currentNightTheme;
            }
            if (needSwitchToTheme == 2) {
                z2 = false;
                z = true;
            } else {
                z2 = false;
                z = false;
            }
            applyTheme(themeInfo7, z2, z2, z);
            AndroidUtilities.runOnUIThread(MessagesController$$ExternalSyntheticLambda219.INSTANCE);
            ambientSensorListener = new SensorEventListener() { // from class: org.telegram.ui.ActionBar.Theme.9
                @Override // android.hardware.SensorEventListener
                public void onAccuracyChanged(Sensor sensor, int i6) {
                }

                @Override // android.hardware.SensorEventListener
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float f = sensorEvent.values[0];
                    if (f <= 0.0f) {
                        f = 0.1f;
                    }
                    if (ApplicationLoader.mainInterfacePaused || !ApplicationLoader.isScreenOn) {
                        return;
                    }
                    if (f > 500.0f) {
                        float unused = Theme.lastBrightnessValue = 1.0f;
                    } else {
                        float unused2 = Theme.lastBrightnessValue = ((float) Math.ceil((Math.log(f) * 9.932299613952637d) + 27.05900001525879d)) / 100.0f;
                    }
                    if (Theme.lastBrightnessValue > Theme.autoNightBrighnessThreshold) {
                        if (Theme.switchNightRunnableScheduled) {
                            boolean unused3 = Theme.switchNightRunnableScheduled = false;
                            AndroidUtilities.cancelRunOnUIThread(Theme.switchNightBrightnessRunnable);
                        }
                        if (Theme.switchDayRunnableScheduled) {
                            return;
                        }
                        boolean unused4 = Theme.switchDayRunnableScheduled = true;
                        AndroidUtilities.runOnUIThread(Theme.switchDayBrightnessRunnable, Theme.getAutoNightSwitchThemeDelay());
                    } else if (MediaController.getInstance().isRecordingOrListeningByProximity()) {
                    } else {
                        if (Theme.switchDayRunnableScheduled) {
                            boolean unused5 = Theme.switchDayRunnableScheduled = false;
                            AndroidUtilities.cancelRunOnUIThread(Theme.switchDayBrightnessRunnable);
                        }
                        if (Theme.switchNightRunnableScheduled) {
                            return;
                        }
                        boolean unused6 = Theme.switchNightRunnableScheduled = true;
                        AndroidUtilities.runOnUIThread(Theme.switchNightBrightnessRunnable, Theme.getAutoNightSwitchThemeDelay());
                    }
                }
            };
            viewPos = new int[2];
        } catch (Exception e2) {
            FileLog.e(e2);
            throw new RuntimeException(e2);
        }
    }

    public static void sortAccents(ThemeInfo themeInfo) {
        Collections.sort(themeInfo.themeAccents, Theme$$ExternalSyntheticLambda7.INSTANCE);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r3v0, types: [boolean] */
    public static /* synthetic */ int lambda$sortAccents$0(ThemeAccent themeAccent, ThemeAccent themeAccent2) {
        if (isHome(themeAccent)) {
            return -1;
        }
        if (isHome(themeAccent2)) {
            return 1;
        }
        ?? r0 = themeAccent.isDefault;
        ?? r3 = themeAccent2.isDefault;
        if (r0 != r3) {
            return r0 > r3 ? -1 : 1;
        } else if (r0 != 0) {
            int i = themeAccent.id;
            int i2 = themeAccent2.id;
            if (i > i2) {
                return 1;
            }
            return i < i2 ? -1 : 0;
        } else {
            int i3 = themeAccent.id;
            int i4 = themeAccent2.id;
            if (i3 > i4) {
                return -1;
            }
            return i3 < i4 ? 1 : 0;
        }
    }

    public static void saveAutoNightThemeConfig() {
        SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("selectedAutoNightType", selectedAutoNightType);
        edit.putBoolean("autoNightScheduleByLocation", autoNightScheduleByLocation);
        edit.putFloat("autoNightBrighnessThreshold", autoNightBrighnessThreshold);
        edit.putInt("autoNightDayStartTime", autoNightDayStartTime);
        edit.putInt("autoNightDayEndTime", autoNightDayEndTime);
        edit.putInt("autoNightSunriseTime", autoNightSunriseTime);
        edit.putString("autoNightCityName", autoNightCityName);
        edit.putInt("autoNightSunsetTime", autoNightSunsetTime);
        edit.putLong("autoNightLocationLatitude3", Double.doubleToRawLongBits(autoNightLocationLatitude));
        edit.putLong("autoNightLocationLongitude3", Double.doubleToRawLongBits(autoNightLocationLongitude));
        edit.putInt("autoNightLastSunCheckDay", autoNightLastSunCheckDay);
        ThemeInfo themeInfo = currentNightTheme;
        if (themeInfo != null) {
            edit.putString("nighttheme", themeInfo.getKey());
        } else {
            edit.remove("nighttheme");
        }
        edit.commit();
    }

    @SuppressLint({"PrivateApi"})
    public static Drawable getStateDrawable(Drawable drawable, int i) {
        if (Build.VERSION.SDK_INT >= 29 && (drawable instanceof StateListDrawable)) {
            return ((StateListDrawable) drawable).getStateDrawable(i);
        }
        if (StateListDrawable_getStateDrawableMethod == null) {
            try {
                StateListDrawable_getStateDrawableMethod = StateListDrawable.class.getDeclaredMethod("getStateDrawable", Integer.TYPE);
            } catch (Throwable unused) {
            }
        }
        Method method = StateListDrawable_getStateDrawableMethod;
        if (method == null) {
            return null;
        }
        try {
            return (Drawable) method.invoke(drawable, Integer.valueOf(i));
        } catch (Exception unused2) {
            return null;
        }
    }

    public static Drawable createEmojiIconSelectorDrawable(Context context, int i, int i2, int i3) {
        Resources resources = context.getResources();
        Drawable mutate = resources.getDrawable(i).mutate();
        if (i2 != 0) {
            mutate.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.MULTIPLY));
        }
        Drawable mutate2 = resources.getDrawable(i).mutate();
        if (i3 != 0) {
            mutate2.setColorFilter(new PorterDuffColorFilter(i3, PorterDuff.Mode.MULTIPLY));
        }
        StateListDrawable stateListDrawable = new StateListDrawable() { // from class: org.telegram.ui.ActionBar.Theme.3
            @Override // android.graphics.drawable.DrawableContainer
            public boolean selectDrawable(int i4) {
                if (Build.VERSION.SDK_INT < 21) {
                    Drawable stateDrawable = Theme.getStateDrawable(this, i4);
                    ColorFilter colorFilter = null;
                    if (stateDrawable instanceof BitmapDrawable) {
                        colorFilter = ((BitmapDrawable) stateDrawable).getPaint().getColorFilter();
                    } else if (stateDrawable instanceof NinePatchDrawable) {
                        colorFilter = ((NinePatchDrawable) stateDrawable).getPaint().getColorFilter();
                    }
                    boolean selectDrawable = super.selectDrawable(i4);
                    if (colorFilter != null) {
                        stateDrawable.setColorFilter(colorFilter);
                    }
                    return selectDrawable;
                }
                return super.selectDrawable(i4);
            }
        };
        stateListDrawable.setEnterFadeDuration(1);
        stateListDrawable.setExitFadeDuration(200);
        stateListDrawable.addState(new int[]{16842913}, mutate2);
        stateListDrawable.addState(new int[0], mutate);
        return stateListDrawable;
    }

    public static Drawable createEditTextDrawable(Context context, boolean z) {
        return createEditTextDrawable(context, getColor(z ? "dialogInputField" : "windowBackgroundWhiteInputField"), getColor(z ? "dialogInputFieldActivated" : "windowBackgroundWhiteInputFieldActivated"));
    }

    public static Drawable createEditTextDrawable(Context context, int i, int i2) {
        Resources resources = context.getResources();
        Drawable mutate = resources.getDrawable(R.drawable.search_dark).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
        Drawable mutate2 = resources.getDrawable(R.drawable.search_dark_activated).mutate();
        mutate2.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.MULTIPLY));
        StateListDrawable stateListDrawable = new StateListDrawable() { // from class: org.telegram.ui.ActionBar.Theme.4
            @Override // android.graphics.drawable.DrawableContainer
            public boolean selectDrawable(int i3) {
                if (Build.VERSION.SDK_INT < 21) {
                    Drawable stateDrawable = Theme.getStateDrawable(this, i3);
                    ColorFilter colorFilter = null;
                    if (stateDrawable instanceof BitmapDrawable) {
                        colorFilter = ((BitmapDrawable) stateDrawable).getPaint().getColorFilter();
                    } else if (stateDrawable instanceof NinePatchDrawable) {
                        colorFilter = ((NinePatchDrawable) stateDrawable).getPaint().getColorFilter();
                    }
                    boolean selectDrawable = super.selectDrawable(i3);
                    if (colorFilter != null) {
                        stateDrawable.setColorFilter(colorFilter);
                    }
                    return selectDrawable;
                }
                return super.selectDrawable(i3);
            }
        };
        stateListDrawable.addState(new int[]{16842910, 16842908}, mutate2);
        stateListDrawable.addState(new int[]{16842908}, mutate2);
        stateListDrawable.addState(StateSet.WILD_CARD, mutate);
        return stateListDrawable;
    }

    public static boolean canStartHolidayAnimation() {
        return canStartHolidayAnimation;
    }

    public static int getEventType() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i = calendar.get(2);
        int i2 = calendar.get(5);
        calendar.get(12);
        int i3 = calendar.get(11);
        if ((i != 11 || i2 < 24 || i2 > 31) && !(i == 0 && i2 == 1)) {
            if (i == 1 && i2 == 14) {
                return 1;
            }
            if (i == 9 && i2 >= 30) {
                return 2;
            }
            return (i == 10 && i2 == 1 && i3 < 12) ? 2 : -1;
        }
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0056, code lost:
        if (r2 <= 31) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x005a, code lost:
        if (r2 == 1) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x005c, code lost:
        org.telegram.ui.ActionBar.Theme.dialogs_holidayDrawable = org.telegram.messenger.ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.newyear);
        org.telegram.ui.ActionBar.Theme.dialogs_holidayDrawableOffsetX = -org.telegram.messenger.AndroidUtilities.dp(3.0f);
        org.telegram.ui.ActionBar.Theme.dialogs_holidayDrawableOffsetY = -org.telegram.messenger.AndroidUtilities.dp(1.0f);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Drawable getCurrentHolidayDrawable() {
        if (System.currentTimeMillis() - lastHolidayCheckTime >= 60000) {
            lastHolidayCheckTime = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int i = calendar.get(2);
            int i2 = calendar.get(5);
            calendar.get(12);
            int i3 = calendar.get(11);
            if (i == 0 && i2 == 1 && i3 <= 23) {
                canStartHolidayAnimation = true;
            } else {
                canStartHolidayAnimation = false;
            }
            if (dialogs_holidayDrawable == null) {
                if (i == 11) {
                    if (i2 >= (BuildVars.DEBUG_PRIVATE_VERSION ? 29 : 31)) {
                    }
                }
                if (i == 0) {
                }
            }
        }
        return dialogs_holidayDrawable;
    }

    public static int getCurrentHolidayDrawableXOffset() {
        return dialogs_holidayDrawableOffsetX;
    }

    public static int getCurrentHolidayDrawableYOffset() {
        return dialogs_holidayDrawableOffsetY;
    }

    public static ShapeDrawable createCircleDrawable(int i, int i2) {
        OvalShape ovalShape = new OvalShape();
        float f = i;
        ovalShape.resize(f, f);
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.setIntrinsicWidth(i);
        shapeDrawable.setIntrinsicHeight(i);
        shapeDrawable.getPaint().setColor(i2);
        return shapeDrawable;
    }

    public static CombinedDrawable createCircleDrawableWithIcon(int i, int i2) {
        return createCircleDrawableWithIcon(i, i2, 0);
    }

    public static CombinedDrawable createCircleDrawableWithIcon(int i, int i2, int i3) {
        return createCircleDrawableWithIcon(i, i2 != 0 ? ApplicationLoader.applicationContext.getResources().getDrawable(i2).mutate() : null, i3);
    }

    public static CombinedDrawable createCircleDrawableWithIcon(int i, Drawable drawable, int i2) {
        OvalShape ovalShape = new OvalShape();
        float f = i;
        ovalShape.resize(f, f);
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        Paint paint = shapeDrawable.getPaint();
        paint.setColor(-1);
        if (i2 == 1) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        } else if (i2 == 2) {
            paint.setAlpha(0);
        }
        CombinedDrawable combinedDrawable = new CombinedDrawable(shapeDrawable, drawable);
        combinedDrawable.setCustomSize(i, i);
        return combinedDrawable;
    }

    public static float getThemeIntensity(float f) {
        return (f >= 0.0f || getActiveTheme().isDark()) ? f : -f;
    }

    public static void setCombinedDrawableColor(Drawable drawable, int i, boolean z) {
        Drawable drawable2;
        if (!(drawable instanceof CombinedDrawable)) {
            return;
        }
        if (z) {
            drawable2 = ((CombinedDrawable) drawable).getIcon();
        } else {
            drawable2 = ((CombinedDrawable) drawable).getBackground();
        }
        if (drawable2 instanceof ColorDrawable) {
            ((ColorDrawable) drawable2).setColor(i);
        } else {
            drawable2.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
        }
    }

    public static Drawable createSimpleSelectorCircleDrawable(int i, int i2, int i3) {
        OvalShape ovalShape = new OvalShape();
        float f = i;
        ovalShape.resize(f, f);
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(i2);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(ovalShape);
        if (Build.VERSION.SDK_INT >= 21) {
            shapeDrawable2.getPaint().setColor(-1);
            return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i3}), shapeDrawable, shapeDrawable2);
        }
        shapeDrawable2.getPaint().setColor(i3);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, shapeDrawable2);
        stateListDrawable.addState(new int[]{16842908}, shapeDrawable2);
        stateListDrawable.addState(StateSet.WILD_CARD, shapeDrawable);
        return stateListDrawable;
    }

    public static Drawable createRoundRectDrawable(int i, int i2) {
        float f = i;
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
        shapeDrawable.getPaint().setColor(i2);
        return shapeDrawable;
    }

    public static Drawable createRoundRectDrawable(int i, int i2, int i3) {
        float f = i;
        float f2 = i2;
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f2, f2, f2, f2}, null, null));
        shapeDrawable.getPaint().setColor(i3);
        return shapeDrawable;
    }

    public static Drawable createServiceDrawable(int i, View view, View view2) {
        return createServiceDrawable(i, view, view2, chat_actionBackgroundPaint);
    }

    public static Drawable createServiceDrawable(final int i, final View view, final View view2, final Paint paint) {
        return new Drawable() { // from class: org.telegram.ui.ActionBar.Theme.6
            private RectF rect = new RectF();

            @Override // android.graphics.drawable.Drawable
            public int getOpacity() {
                return -2;
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int i2) {
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                Rect bounds = getBounds();
                this.rect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
                Theme.applyServiceShaderMatrixForView(view, view2);
                RectF rectF = this.rect;
                int i2 = i;
                canvas.drawRoundRect(rectF, i2, i2, paint);
                if (Theme.hasGradientService()) {
                    RectF rectF2 = this.rect;
                    int i3 = i;
                    canvas.drawRoundRect(rectF2, i3, i3, Theme.chat_actionBackgroundGradientDarkenPaint);
                }
            }
        };
    }

    public static Drawable createSimpleSelectorRoundRectDrawable(int i, int i2, int i3) {
        return createSimpleSelectorRoundRectDrawable(i, i2, i3, i3);
    }

    public static Drawable createSimpleSelectorRoundRectDrawable(int i, int i2, int i3, int i4) {
        float f = i;
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
        shapeDrawable.getPaint().setColor(i2);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
        shapeDrawable2.getPaint().setColor(i4);
        if (Build.VERSION.SDK_INT >= 21) {
            return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i3}), shapeDrawable, shapeDrawable2);
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, shapeDrawable2);
        stateListDrawable.addState(new int[]{16842913}, shapeDrawable2);
        stateListDrawable.addState(StateSet.WILD_CARD, shapeDrawable);
        return stateListDrawable;
    }

    public static Drawable createSelectorDrawableFromDrawables(Drawable drawable, Drawable drawable2) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, drawable2);
        stateListDrawable.addState(new int[]{16842913}, drawable2);
        stateListDrawable.addState(StateSet.WILD_CARD, drawable);
        return stateListDrawable;
    }

    public static Drawable getRoundRectSelectorDrawable(int i) {
        return getRoundRectSelectorDrawable(AndroidUtilities.dp(3.0f), i);
    }

    public static Drawable getRoundRectSelectorDrawable(int i, int i2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{(i2 & 16777215) | 419430400}), null, createRoundRectDrawable(i, -1));
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        int i3 = (i2 & 16777215) | 419430400;
        stateListDrawable.addState(new int[]{16842919}, createRoundRectDrawable(i, i3));
        stateListDrawable.addState(new int[]{16842913}, createRoundRectDrawable(i, i3));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0));
        return stateListDrawable;
    }

    public static Drawable createSelectorWithBackgroundDrawable(int i, int i2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i2}), new ColorDrawable(i), new ColorDrawable(i));
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, new ColorDrawable(i2));
        stateListDrawable.addState(new int[]{16842913}, new ColorDrawable(i2));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(i));
        return stateListDrawable;
    }

    public static Drawable getSelectorDrawable(boolean z) {
        return getSelectorDrawable(getColor("listSelectorSDK21"), z);
    }

    public static Drawable getSelectorDrawable(int i, boolean z) {
        if (z) {
            return getSelectorDrawable(i, "windowBackgroundWhite");
        }
        return createSelectorDrawable(i, 2);
    }

    public static Drawable getSelectorDrawable(int i, String str) {
        if (str != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), new ColorDrawable(getColor(str)), new ColorDrawable(-1));
            }
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{16842919}, new ColorDrawable(i));
            stateListDrawable.addState(new int[]{16842913}, new ColorDrawable(i));
            stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(getColor(str)));
            return stateListDrawable;
        }
        return createSelectorDrawable(i, 2);
    }

    public static Drawable createSelectorDrawable(int i) {
        return createSelectorDrawable(i, 1, -1);
    }

    public static Drawable createSelectorDrawable(int i, int i2) {
        return createSelectorDrawable(i, i2, -1);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x004f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Drawable createSelectorDrawable(int i, final int i2, int i3) {
        Drawable drawable;
        int i4 = Build.VERSION.SDK_INT;
        if (i4 >= 21) {
            if ((i2 != 1 && i2 != 5) || i4 < 23) {
                if (i2 == 1 || i2 == 3 || i2 == 4 || i2 == 5 || i2 == 6 || i2 == 7) {
                    maskPaint.setColor(-1);
                    drawable = new Drawable() { // from class: org.telegram.ui.ActionBar.Theme.7
                        RectF rect;

                        @Override // android.graphics.drawable.Drawable
                        public int getOpacity() {
                            return 0;
                        }

                        @Override // android.graphics.drawable.Drawable
                        public void setAlpha(int i5) {
                        }

                        @Override // android.graphics.drawable.Drawable
                        public void setColorFilter(ColorFilter colorFilter) {
                        }

                        @Override // android.graphics.drawable.Drawable
                        public void draw(Canvas canvas) {
                            int i5;
                            Rect bounds = getBounds();
                            int i6 = i2;
                            if (i6 != 7) {
                                if (i6 == 1 || i6 == 6) {
                                    i5 = AndroidUtilities.dp(20.0f);
                                } else if (i6 == 3) {
                                    i5 = Math.max(bounds.width(), bounds.height()) / 2;
                                } else {
                                    i5 = (int) Math.ceil(Math.sqrt(((bounds.left - bounds.centerX()) * (bounds.left - bounds.centerX())) + ((bounds.top - bounds.centerY()) * (bounds.top - bounds.centerY()))));
                                }
                                canvas.drawCircle(bounds.centerX(), bounds.centerY(), i5, Theme.maskPaint);
                                return;
                            }
                            if (this.rect == null) {
                                this.rect = new RectF();
                            }
                            this.rect.set(bounds);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(6.0f), Theme.maskPaint);
                        }
                    };
                } else if (i2 == 2) {
                    drawable = new ColorDrawable(-1);
                }
                RippleDrawable rippleDrawable = new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, drawable);
                if (i4 >= 23) {
                    if (i2 == 1) {
                        if (i3 <= 0) {
                            i3 = AndroidUtilities.dp(20.0f);
                        }
                        rippleDrawable.setRadius(i3);
                    } else if (i2 == 5) {
                        rippleDrawable.setRadius(-1);
                    }
                }
                return rippleDrawable;
            }
            drawable = null;
            RippleDrawable rippleDrawable2 = new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, drawable);
            if (i4 >= 23) {
            }
            return rippleDrawable2;
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, new ColorDrawable(i));
        stateListDrawable.addState(new int[]{16842913}, new ColorDrawable(i));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0));
        return stateListDrawable;
    }

    public static Drawable createCircleSelectorDrawable(int i, final int i2, final int i3) {
        if (Build.VERSION.SDK_INT >= 21) {
            maskPaint.setColor(-1);
            return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, new Drawable() { // from class: org.telegram.ui.ActionBar.Theme.8
                @Override // android.graphics.drawable.Drawable
                public int getOpacity() {
                    return 0;
                }

                @Override // android.graphics.drawable.Drawable
                public void setAlpha(int i4) {
                }

                @Override // android.graphics.drawable.Drawable
                public void setColorFilter(ColorFilter colorFilter) {
                }

                @Override // android.graphics.drawable.Drawable
                public void draw(Canvas canvas) {
                    Rect bounds = getBounds();
                    canvas.drawCircle((bounds.centerX() - i2) + i3, bounds.centerY(), (Math.max(bounds.width(), bounds.height()) / 2) + i2 + i3, Theme.maskPaint);
                }
            });
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, new ColorDrawable(i));
        stateListDrawable.addState(new int[]{16842913}, new ColorDrawable(i));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0));
        return stateListDrawable;
    }

    /* loaded from: classes3.dex */
    public static class AdaptiveRipple {
        private static float[] tempHSV;

        public static Drawable rect() {
            return rect(Theme.getColor("windowBackgroundWhite"));
        }

        public static Drawable rect(int i) {
            return rect(i, 0.0f);
        }

        public static Drawable rect(int i, float... fArr) {
            return createRect(0, calcRippleColor(i), fArr);
        }

        public static Drawable filledRect(String str, float... fArr) {
            return filledRect(Theme.getColor(str), fArr);
        }

        public static Drawable filledRect(int i) {
            return createRect(i, calcRippleColor(i), new float[0]);
        }

        public static Drawable filledRect(int i, float... fArr) {
            return createRect(i, calcRippleColor(i), fArr);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static Drawable createRect(int i, int i2, float... fArr) {
            ColorDrawable colorDrawable = null;
            if (i != 0) {
                if (hasNonzeroRadii(fArr)) {
                    ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(calcRadii(fArr), null, null));
                    shapeDrawable.getPaint().setColor(i);
                    colorDrawable = shapeDrawable;
                } else {
                    colorDrawable = new ColorDrawable(i);
                }
            }
            return createRect(colorDrawable, i2, fArr);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static Drawable createRect(Drawable drawable, int i, float... fArr) {
            ColorDrawable colorDrawable;
            ShapeDrawable shapeDrawable = null;
            if (Build.VERSION.SDK_INT >= 21) {
                if (hasNonzeroRadii(fArr)) {
                    ShapeDrawable shapeDrawable2 = new ShapeDrawable(new RoundRectShape(calcRadii(fArr), null, null));
                    shapeDrawable2.getPaint().setColor(-1);
                    shapeDrawable = shapeDrawable2;
                }
                return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), drawable, shapeDrawable);
            }
            StateListDrawable stateListDrawable = new StateListDrawable();
            if (hasNonzeroRadii(fArr)) {
                ShapeDrawable shapeDrawable3 = new ShapeDrawable(new RoundRectShape(calcRadii(fArr), null, null));
                shapeDrawable3.getPaint().setColor(i);
                colorDrawable = shapeDrawable3;
            } else {
                colorDrawable = new ColorDrawable(i);
            }
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawable, colorDrawable});
            stateListDrawable.addState(new int[]{16842919}, layerDrawable);
            stateListDrawable.addState(new int[]{16842913}, layerDrawable);
            stateListDrawable.addState(StateSet.WILD_CARD, drawable);
            return stateListDrawable;
        }

        private static float[] calcRadii(float... fArr) {
            return fArr.length == 0 ? new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f} : fArr.length == 1 ? new float[]{AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0])} : fArr.length == 2 ? new float[]{AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[1])} : fArr.length == 3 ? new float[]{AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[2]), AndroidUtilities.dp(fArr[2]), AndroidUtilities.dp(fArr[2]), AndroidUtilities.dp(fArr[2])} : fArr.length < 8 ? new float[]{AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[2]), AndroidUtilities.dp(fArr[2]), AndroidUtilities.dp(fArr[3]), AndroidUtilities.dp(fArr[3])} : new float[]{AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[2]), AndroidUtilities.dp(fArr[3]), AndroidUtilities.dp(fArr[4]), AndroidUtilities.dp(fArr[5]), AndroidUtilities.dp(fArr[6]), AndroidUtilities.dp(fArr[7])};
        }

        private static boolean hasNonzeroRadii(float... fArr) {
            for (int i = 0; i < Math.min(8, fArr.length); i++) {
                if (fArr[i] > 0.0f) {
                    return true;
                }
            }
            return false;
        }

        private static int calcRippleColor(int i) {
            if (tempHSV == null) {
                tempHSV = new float[3];
            }
            Color.colorToHSV(i, tempHSV);
            float[] fArr = tempHSV;
            if (fArr[1] > 0.01f) {
                fArr[1] = Math.min(1.0f, Math.max(0.0f, fArr[1] + (Theme.isCurrentThemeDark() ? -0.25f : 0.25f)));
                float[] fArr2 = tempHSV;
                fArr2[2] = Math.min(1.0f, Math.max(0.0f, fArr2[2] + (Theme.isCurrentThemeDark() ? 0.05f : -0.05f)));
            } else {
                fArr[2] = Math.min(1.0f, Math.max(0.0f, fArr[2] + (Theme.isCurrentThemeDark() ? 0.1f : -0.1f)));
            }
            return Color.HSVToColor(127, tempHSV);
        }
    }

    /* loaded from: classes3.dex */
    public static class RippleRadMaskDrawable extends Drawable {
        private float[] radii;
        private Path path = new Path();
        boolean invalidatePath = true;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return 0;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public RippleRadMaskDrawable(float f, float f2) {
            this.radii = r0;
            float dp = AndroidUtilities.dp(f);
            float[] fArr = {dp, dp, dp, dp};
            float[] fArr2 = this.radii;
            float dp2 = AndroidUtilities.dp(f2);
            fArr2[7] = dp2;
            fArr2[6] = dp2;
            fArr2[5] = dp2;
            fArr2[4] = dp2;
        }

        public RippleRadMaskDrawable(float f, float f2, float f3, float f4) {
            float[] fArr = new float[8];
            this.radii = fArr;
            float dp = AndroidUtilities.dp(f);
            fArr[1] = dp;
            fArr[0] = dp;
            float[] fArr2 = this.radii;
            float dp2 = AndroidUtilities.dp(f2);
            fArr2[3] = dp2;
            fArr2[2] = dp2;
            float[] fArr3 = this.radii;
            float dp3 = AndroidUtilities.dp(f3);
            fArr3[5] = dp3;
            fArr3[4] = dp3;
            float[] fArr4 = this.radii;
            float dp4 = AndroidUtilities.dp(f4);
            fArr4[7] = dp4;
            fArr4[6] = dp4;
        }

        @Override // android.graphics.drawable.Drawable
        protected void onBoundsChange(Rect rect) {
            this.invalidatePath = true;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            if (this.invalidatePath) {
                this.invalidatePath = false;
                this.path.reset();
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(getBounds());
                this.path.addRoundRect(rectF, this.radii, Path.Direction.CW);
            }
            canvas.drawPath(this.path, Theme.maskPaint);
        }
    }

    public static void setMaskDrawableRad(Drawable drawable, int i, int i2) {
        if (Build.VERSION.SDK_INT >= 21 && (drawable instanceof RippleDrawable)) {
            RippleDrawable rippleDrawable = (RippleDrawable) drawable;
            int numberOfLayers = rippleDrawable.getNumberOfLayers();
            for (int i3 = 0; i3 < numberOfLayers; i3++) {
                if (rippleDrawable.getDrawable(i3) instanceof RippleRadMaskDrawable) {
                    rippleDrawable.setDrawableByLayerId(16908334, new RippleRadMaskDrawable(i, i2));
                    return;
                }
            }
        }
    }

    public static Drawable createRadSelectorDrawable(int i, int i2, int i3) {
        if (Build.VERSION.SDK_INT >= 21) {
            maskPaint.setColor(-1);
            return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, new RippleRadMaskDrawable(i2, i3));
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, new ColorDrawable(i));
        stateListDrawable.addState(new int[]{16842913}, new ColorDrawable(i));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0));
        return stateListDrawable;
    }

    public static Drawable createRadSelectorDrawable(int i, int i2, int i3, int i4, int i5) {
        if (Build.VERSION.SDK_INT >= 21) {
            maskPaint.setColor(-1);
            return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, new RippleRadMaskDrawable(i2, i3, i4, i5));
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842919}, new ColorDrawable(i));
        stateListDrawable.addState(new int[]{16842913}, new ColorDrawable(i));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0));
        return stateListDrawable;
    }

    public static void applyPreviousTheme() {
        ThemeInfo themeInfo;
        ThemeInfo themeInfo2 = previousTheme;
        if (themeInfo2 == null) {
            return;
        }
        hasPreviousTheme = false;
        if (isInNigthMode && (themeInfo = currentNightTheme) != null) {
            applyTheme(themeInfo, true, false, true);
        } else if (!isApplyingAccent) {
            applyTheme(themeInfo2, true, false, false);
        }
        isApplyingAccent = false;
        previousTheme = null;
        checkAutoNightThemeConditions();
    }

    public static void clearPreviousTheme() {
        if (previousTheme == null) {
            return;
        }
        hasPreviousTheme = false;
        isApplyingAccent = false;
        previousTheme = null;
    }

    private static void sortThemes() {
        Collections.sort(themes, Theme$$ExternalSyntheticLambda8.INSTANCE);
    }

    public static /* synthetic */ int lambda$sortThemes$1(ThemeInfo themeInfo, ThemeInfo themeInfo2) {
        if (themeInfo.pathToFile == null && themeInfo.assetName == null) {
            return -1;
        }
        if (themeInfo2.pathToFile != null || themeInfo2.assetName != null) {
            return themeInfo.name.compareTo(themeInfo2.name);
        }
        return 1;
    }

    public static void applyThemeTemporary(ThemeInfo themeInfo, boolean z) {
        previousTheme = getCurrentTheme();
        hasPreviousTheme = true;
        isApplyingAccent = z;
        applyTheme(themeInfo, false, false, false);
    }

    public static boolean hasCustomWallpaper() {
        return isApplyingAccent && currentTheme.overrideWallpaper != null;
    }

    public static boolean isCustomWallpaperColor() {
        return hasCustomWallpaper() && currentTheme.overrideWallpaper.color != 0;
    }

    public static void resetCustomWallpaper(boolean z) {
        if (z) {
            isApplyingAccent = false;
            reloadWallpaper();
            return;
        }
        currentTheme.setOverrideWallpaper(null);
    }

    public static ThemeInfo fillThemeValues(File file, String str, TLRPC$TL_theme tLRPC$TL_theme) {
        String[] split;
        try {
            ThemeInfo themeInfo = new ThemeInfo();
            themeInfo.name = str;
            themeInfo.info = tLRPC$TL_theme;
            themeInfo.pathToFile = file.getAbsolutePath();
            themeInfo.account = UserConfig.selectedAccount;
            String[] strArr = new String[1];
            checkIsDark(getThemeFileValues(new File(themeInfo.pathToFile), null, strArr), themeInfo);
            if (!TextUtils.isEmpty(strArr[0])) {
                String str2 = strArr[0];
                File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                themeInfo.pathToWallpaper = new File(filesDirFixed, Utilities.MD5(str2) + ".wp").getAbsolutePath();
                Uri parse = Uri.parse(str2);
                themeInfo.slug = parse.getQueryParameter("slug");
                String queryParameter = parse.getQueryParameter("mode");
                if (queryParameter != null && (split = queryParameter.toLowerCase().split(" ")) != null && split.length > 0) {
                    for (int i = 0; i < split.length; i++) {
                        if ("blur".equals(split[i])) {
                            themeInfo.isBlured = true;
                        } else if ("motion".equals(split[i])) {
                            themeInfo.isMotion = true;
                        }
                    }
                }
                String queryParameter2 = parse.getQueryParameter("intensity");
                if (!TextUtils.isEmpty(queryParameter2)) {
                    try {
                        String queryParameter3 = parse.getQueryParameter("bg_color");
                        if (!TextUtils.isEmpty(queryParameter3)) {
                            themeInfo.patternBgColor = Integer.parseInt(queryParameter3.substring(0, 6), 16) | (-16777216);
                            if (queryParameter3.length() >= 13 && AndroidUtilities.isValidWallChar(queryParameter3.charAt(6))) {
                                themeInfo.patternBgGradientColor1 = Integer.parseInt(queryParameter3.substring(7, 13), 16) | (-16777216);
                            }
                            if (queryParameter3.length() >= 20 && AndroidUtilities.isValidWallChar(queryParameter3.charAt(13))) {
                                themeInfo.patternBgGradientColor2 = Integer.parseInt(queryParameter3.substring(14, 20), 16) | (-16777216);
                            }
                            if (queryParameter3.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter3.charAt(20))) {
                                themeInfo.patternBgGradientColor3 = Integer.parseInt(queryParameter3.substring(21), 16) | (-16777216);
                            }
                        }
                    } catch (Exception unused) {
                    }
                    try {
                        String queryParameter4 = parse.getQueryParameter("rotation");
                        if (!TextUtils.isEmpty(queryParameter4)) {
                            themeInfo.patternBgGradientRotation = Utilities.parseInt((CharSequence) queryParameter4).intValue();
                        }
                    } catch (Exception unused2) {
                    }
                    if (!TextUtils.isEmpty(queryParameter2)) {
                        themeInfo.patternIntensity = Utilities.parseInt((CharSequence) queryParameter2).intValue();
                    }
                    if (themeInfo.patternIntensity == 0) {
                        themeInfo.patternIntensity = 50;
                    }
                }
            } else {
                themedWallpaperLink = null;
            }
            return themeInfo;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static ThemeInfo applyThemeFile(File file, String str, TLRPC$TL_theme tLRPC$TL_theme, boolean z) {
        String str2;
        File file2;
        try {
            if (!str.toLowerCase().endsWith(".attheme")) {
                str = str + ".attheme";
            }
            if (z) {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.goingToPreviewTheme, new Object[0]);
                ThemeInfo themeInfo = new ThemeInfo();
                themeInfo.name = str;
                themeInfo.info = tLRPC$TL_theme;
                themeInfo.pathToFile = file.getAbsolutePath();
                themeInfo.account = UserConfig.selectedAccount;
                applyThemeTemporary(themeInfo, false);
                return themeInfo;
            }
            if (tLRPC$TL_theme != null) {
                str2 = "remote" + tLRPC$TL_theme.id;
                file2 = new File(ApplicationLoader.getFilesDirFixed(), str2 + ".attheme");
            } else {
                file2 = new File(ApplicationLoader.getFilesDirFixed(), str);
                str2 = str;
            }
            if (!AndroidUtilities.copyFile(file, file2)) {
                applyPreviousTheme();
                return null;
            }
            previousTheme = null;
            hasPreviousTheme = false;
            isApplyingAccent = false;
            ThemeInfo themeInfo2 = themesDict.get(str2);
            if (themeInfo2 == null) {
                themeInfo2 = new ThemeInfo();
                themeInfo2.name = str;
                themeInfo2.account = UserConfig.selectedAccount;
                themes.add(themeInfo2);
                otherThemes.add(themeInfo2);
                sortThemes();
            } else {
                themesDict.remove(str2);
            }
            themeInfo2.info = tLRPC$TL_theme;
            themeInfo2.pathToFile = file2.getAbsolutePath();
            themesDict.put(themeInfo2.getKey(), themeInfo2);
            saveOtherThemes(true);
            applyTheme(themeInfo2, true, true, false);
            return themeInfo2;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static ThemeInfo getTheme(String str) {
        return themesDict.get(str);
    }

    public static void applyTheme(ThemeInfo themeInfo) {
        applyTheme(themeInfo, true, true, false);
    }

    public static void applyTheme(ThemeInfo themeInfo, boolean z) {
        applyTheme(themeInfo, true, z);
    }

    public static void applyTheme(ThemeInfo themeInfo, boolean z, boolean z2) {
        applyTheme(themeInfo, z, true, z2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x003c, code lost:
        if (r8 == false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x003e, code lost:
        r0 = org.telegram.messenger.MessagesController.getGlobalMainSettings().edit();
        r0.putString("theme", r7.getKey());
        r0.apply();
     */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01d1 A[Catch: Exception -> 0x01e8, TryCatch #2 {Exception -> 0x01e8, blocks: (B:8:0x000d, B:11:0x0014, B:16:0x001d, B:17:0x002b, B:20:0x003e, B:21:0x0050, B:23:0x0057, B:24:0x005e, B:25:0x006b, B:27:0x0077, B:29:0x007d, B:31:0x0087, B:37:0x00c3, B:79:0x01bf, B:81:0x01c5, B:83:0x01c9, B:85:0x01d1, B:86:0x01e2, B:38:0x00c5, B:40:0x00db, B:42:0x00e7, B:45:0x00eb, B:47:0x00ee, B:49:0x00f8, B:50:0x00fb, B:52:0x0105, B:53:0x0107, B:54:0x010a, B:55:0x011b, B:57:0x0127, B:59:0x013f, B:61:0x0149, B:62:0x0155, B:64:0x015d, B:66:0x0167, B:67:0x0174, B:69:0x017c, B:71:0x0186, B:72:0x0193, B:74:0x019f), top: B:101:0x000d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void applyTheme(ThemeInfo themeInfo, boolean z, boolean z2, boolean z3) {
        String[] split;
        if (themeInfo == null) {
            return;
        }
        ThemeEditorView themeEditorView = ThemeEditorView.getInstance();
        if (themeEditorView != null) {
            themeEditorView.destroy();
        }
        try {
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (themeInfo.pathToFile == null && themeInfo.assetName == null) {
            if (!z3 && z) {
                SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
                edit.remove("theme");
                edit.apply();
            }
            currentColorsNoAccent.clear();
            themedWallpaperFileOffset = 0;
            themedWallpaperLink = null;
            wallpaper = null;
            themedWallpaper = null;
            if (!z3 && previousTheme == null) {
                currentDayTheme = themeInfo;
                if (isCurrentThemeNight()) {
                    switchNightThemeDelay = 2000;
                    lastDelayUpdateTime = SystemClock.elapsedRealtime();
                    AndroidUtilities.runOnUIThread(MessagesController$$ExternalSyntheticLambda219.INSTANCE, 2100L);
                }
            }
            currentTheme = themeInfo;
            refreshThemeColors();
            if (previousTheme != null || !z || switchingNightTheme) {
                return;
            }
            MessagesController.getInstance(themeInfo.account).saveTheme(themeInfo, themeInfo.getAccent(false), z3, false);
            return;
        }
        String[] strArr = new String[1];
        String str = themeInfo.assetName;
        if (str != null) {
            currentColorsNoAccent = getThemeFileValues(null, str, null);
        } else {
            currentColorsNoAccent = getThemeFileValues(new File(themeInfo.pathToFile), null, strArr);
        }
        Integer num = currentColorsNoAccent.get("wallpaperFileOffset");
        themedWallpaperFileOffset = num != null ? num.intValue() : -1;
        if (!TextUtils.isEmpty(strArr[0])) {
            themedWallpaperLink = strArr[0];
            File filesDirFixed = ApplicationLoader.getFilesDirFixed();
            String absolutePath = new File(filesDirFixed, Utilities.MD5(themedWallpaperLink) + ".wp").getAbsolutePath();
            try {
                String str2 = themeInfo.pathToWallpaper;
                if (str2 != null && !str2.equals(absolutePath)) {
                    new File(themeInfo.pathToWallpaper).delete();
                }
            } catch (Exception unused) {
            }
            themeInfo.pathToWallpaper = absolutePath;
            Uri parse = Uri.parse(themedWallpaperLink);
            themeInfo.slug = parse.getQueryParameter("slug");
            String queryParameter = parse.getQueryParameter("mode");
            if (queryParameter != null && (split = queryParameter.toLowerCase().split(" ")) != null && split.length > 0) {
                for (int i = 0; i < split.length; i++) {
                    if ("blur".equals(split[i])) {
                        themeInfo.isBlured = true;
                    } else if ("motion".equals(split[i])) {
                        themeInfo.isMotion = true;
                    }
                }
            }
            Utilities.parseInt((CharSequence) parse.getQueryParameter("intensity")).intValue();
            themeInfo.patternBgGradientRotation = 45;
            try {
                String queryParameter2 = parse.getQueryParameter("bg_color");
                if (!TextUtils.isEmpty(queryParameter2)) {
                    themeInfo.patternBgColor = Integer.parseInt(queryParameter2.substring(0, 6), 16) | (-16777216);
                    if (queryParameter2.length() >= 13 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(6))) {
                        themeInfo.patternBgGradientColor1 = Integer.parseInt(queryParameter2.substring(7, 13), 16) | (-16777216);
                    }
                    if (queryParameter2.length() >= 20 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(13))) {
                        themeInfo.patternBgGradientColor2 = Integer.parseInt(queryParameter2.substring(14, 20), 16) | (-16777216);
                    }
                    if (queryParameter2.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(20))) {
                        themeInfo.patternBgGradientColor3 = Integer.parseInt(queryParameter2.substring(21), 16) | (-16777216);
                    }
                }
            } catch (Exception unused2) {
            }
            try {
                String queryParameter3 = parse.getQueryParameter("rotation");
                if (!TextUtils.isEmpty(queryParameter3)) {
                    themeInfo.patternBgGradientRotation = Utilities.parseInt((CharSequence) queryParameter3).intValue();
                }
            } catch (Exception unused3) {
            }
        } else {
            try {
                if (themeInfo.pathToWallpaper != null) {
                    new File(themeInfo.pathToWallpaper).delete();
                }
            } catch (Exception unused4) {
            }
            themeInfo.pathToWallpaper = null;
            themedWallpaperLink = null;
        }
        if (!z3) {
            currentDayTheme = themeInfo;
            if (isCurrentThemeNight()) {
            }
        }
        currentTheme = themeInfo;
        refreshThemeColors();
        if (previousTheme != null) {
        }
    }

    public static boolean useBlackText(int i, int i2) {
        float red = Color.red(i) / 255.0f;
        float green = Color.green(i) / 255.0f;
        float blue = Color.blue(i) / 255.0f;
        return ((((red * 0.5f) + ((((float) Color.red(i2)) / 255.0f) * 0.5f)) * 0.2126f) + (((green * 0.5f) + ((((float) Color.green(i2)) / 255.0f) * 0.5f)) * 0.7152f)) + (((blue * 0.5f) + ((((float) Color.blue(i2)) / 255.0f) * 0.5f)) * 0.0722f) > 0.705f || ((red * 0.2126f) + (green * 0.7152f)) + (blue * 0.0722f) > 0.705f;
    }

    public static void refreshThemeColors() {
        refreshThemeColors(false, false);
    }

    public static void refreshThemeColors(boolean z, boolean z2) {
        currentColors.clear();
        currentColors.putAll(currentColorsNoAccent);
        shouldDrawGradientIcons = true;
        ThemeAccent accent = currentTheme.getAccent(false);
        if (accent != null) {
            shouldDrawGradientIcons = accent.fillAccentColors(currentColorsNoAccent, currentColors);
        }
        if (!z2) {
            reloadWallpaper();
        }
        applyCommonTheme();
        applyDialogsTheme();
        applyProfileTheme();
        applyChatTheme(false, z);
        final boolean z3 = !hasPreviousTheme;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$refreshThemeColors$2(z3);
            }
        });
    }

    public static /* synthetic */ void lambda$refreshThemeColors$2(boolean z) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewTheme, Boolean.FALSE, Boolean.valueOf(z));
    }

    public static int changeColorAccent(ThemeInfo themeInfo, int i, int i2) {
        int i3;
        if (i == 0 || (i3 = themeInfo.accentBaseColor) == 0 || i == i3 || (themeInfo.firstAccentIsDefault && themeInfo.currentAccentId == DEFALT_THEME_ACCENT_ID)) {
            return i2;
        }
        float[] tempHsv = getTempHsv(3);
        float[] tempHsv2 = getTempHsv(4);
        Color.colorToHSV(themeInfo.accentBaseColor, tempHsv);
        Color.colorToHSV(i, tempHsv2);
        return changeColorAccent(tempHsv, tempHsv2, i2, themeInfo.isDark());
    }

    public static float[] getTempHsv(int i) {
        ThreadLocal<float[]> threadLocal;
        if (i == 1) {
            threadLocal = hsvTemp1Local;
        } else if (i == 2) {
            threadLocal = hsvTemp2Local;
        } else if (i == 3) {
            threadLocal = hsvTemp3Local;
        } else if (i == 4) {
            threadLocal = hsvTemp4Local;
        } else {
            threadLocal = hsvTemp5Local;
        }
        float[] fArr = threadLocal.get();
        if (fArr == null) {
            float[] fArr2 = new float[3];
            threadLocal.set(fArr2);
            return fArr2;
        }
        return fArr;
    }

    public static int getAccentColor(float[] fArr, int i, int i2) {
        float[] tempHsv = getTempHsv(3);
        float[] tempHsv2 = getTempHsv(4);
        Color.colorToHSV(i, tempHsv);
        Color.colorToHSV(i2, tempHsv2);
        float min = Math.min((tempHsv[1] * 1.5f) / fArr[1], 1.0f);
        tempHsv[0] = (tempHsv2[0] - tempHsv[0]) + fArr[0];
        tempHsv[1] = (tempHsv2[1] * fArr[1]) / tempHsv[1];
        tempHsv[2] = ((((tempHsv2[2] / tempHsv[2]) + min) - 1.0f) * fArr[2]) / min;
        return tempHsv[2] < 0.3f ? i2 : Color.HSVToColor(255, tempHsv);
    }

    public static int changeColorAccent(int i) {
        int i2 = 0;
        ThemeAccent accent = currentTheme.getAccent(false);
        ThemeInfo themeInfo = currentTheme;
        if (accent != null) {
            i2 = accent.accentColor;
        }
        return changeColorAccent(themeInfo, i2, i);
    }

    public static int changeColorAccent(float[] fArr, float[] fArr2, int i, boolean z) {
        if (tmpHSV5 == null) {
            tmpHSV5 = new float[3];
        }
        float[] fArr3 = tmpHSV5;
        Color.colorToHSV(i, fArr3);
        boolean z2 = false;
        if (Math.min(abs(fArr3[0] - fArr[0]), abs((fArr3[0] - fArr[0]) - 360.0f)) > 30.0f) {
            return i;
        }
        float min = Math.min((fArr3[1] * 1.5f) / fArr[1], 1.0f);
        fArr3[0] = (fArr3[0] + fArr2[0]) - fArr[0];
        fArr3[1] = (fArr3[1] * fArr2[1]) / fArr[1];
        fArr3[2] = fArr3[2] * ((1.0f - min) + ((min * fArr2[2]) / fArr[2]));
        int HSVToColor = Color.HSVToColor(Color.alpha(i), fArr3);
        float computePerceivedBrightness = AndroidUtilities.computePerceivedBrightness(i);
        float computePerceivedBrightness2 = AndroidUtilities.computePerceivedBrightness(HSVToColor);
        if (!z ? computePerceivedBrightness < computePerceivedBrightness2 : computePerceivedBrightness > computePerceivedBrightness2) {
            z2 = true;
        }
        return z2 ? changeBrightness(HSVToColor, ((0.39999998f * computePerceivedBrightness) / computePerceivedBrightness2) + 0.6f) : HSVToColor;
    }

    private static int changeBrightness(int i, float f) {
        int red = (int) (Color.red(i) * f);
        int green = (int) (Color.green(i) * f);
        int blue = (int) (Color.blue(i) * f);
        int i2 = 0;
        int min = red < 0 ? 0 : Math.min(red, 255);
        int min2 = green < 0 ? 0 : Math.min(green, 255);
        if (blue >= 0) {
            i2 = Math.min(blue, 255);
        }
        return Color.argb(Color.alpha(i), min, min2, i2);
    }

    public static boolean deleteThemeAccent(ThemeInfo themeInfo, ThemeAccent themeAccent, boolean z) {
        boolean z2 = false;
        if (themeAccent == null || themeInfo == null || themeInfo.themeAccents == null) {
            return false;
        }
        boolean z3 = themeAccent.id == themeInfo.currentAccentId;
        File pathToWallpaper = themeAccent.getPathToWallpaper();
        if (pathToWallpaper != null) {
            pathToWallpaper.delete();
        }
        themeInfo.themeAccentsMap.remove(themeAccent.id);
        themeInfo.themeAccents.remove(themeAccent);
        TLRPC$TL_theme tLRPC$TL_theme = themeAccent.info;
        if (tLRPC$TL_theme != null) {
            themeInfo.accentsByThemeId.remove(tLRPC$TL_theme.id);
        }
        OverrideWallpaperInfo overrideWallpaperInfo = themeAccent.overrideWallpaper;
        if (overrideWallpaperInfo != null) {
            overrideWallpaperInfo.delete();
        }
        if (z3) {
            themeInfo.setCurrentAccentId(themeInfo.themeAccents.get(0).id);
        }
        if (z) {
            saveThemeAccents(themeInfo, true, false, false, false);
            if (themeAccent.info != null) {
                MessagesController messagesController = MessagesController.getInstance(themeAccent.account);
                if (z3 && themeInfo == currentNightTheme) {
                    z2 = true;
                }
                messagesController.saveTheme(themeInfo, themeAccent, z2, true);
            }
        }
        return z3;
    }

    public static void saveThemeAccents(ThemeInfo themeInfo, boolean z, boolean z2, boolean z3, boolean z4) {
        saveThemeAccents(themeInfo, z, z2, z3, z4, false);
    }

    public static void saveThemeAccents(ThemeInfo themeInfo, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        if (z) {
            SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0).edit();
            if (!z3) {
                int size = themeInfo.themeAccents.size();
                int max = Math.max(0, size - themeInfo.defaultAccentCount);
                SerializedData serializedData = new SerializedData(((max * 16) + 2) * 4);
                serializedData.writeInt32(9);
                serializedData.writeInt32(max);
                for (int i = 0; i < size; i++) {
                    ThemeAccent themeAccent = themeInfo.themeAccents.get(i);
                    int i2 = themeAccent.id;
                    if (i2 >= 100) {
                        serializedData.writeInt32(i2);
                        serializedData.writeInt32(themeAccent.accentColor);
                        serializedData.writeInt32(themeAccent.accentColor2);
                        serializedData.writeInt32(themeAccent.myMessagesAccentColor);
                        serializedData.writeInt32(themeAccent.myMessagesGradientAccentColor1);
                        serializedData.writeInt32(themeAccent.myMessagesGradientAccentColor2);
                        serializedData.writeInt32(themeAccent.myMessagesGradientAccentColor3);
                        serializedData.writeBool(themeAccent.myMessagesAnimated);
                        serializedData.writeInt64(themeAccent.backgroundOverrideColor);
                        serializedData.writeInt64(themeAccent.backgroundGradientOverrideColor1);
                        serializedData.writeInt64(themeAccent.backgroundGradientOverrideColor2);
                        serializedData.writeInt64(themeAccent.backgroundGradientOverrideColor3);
                        serializedData.writeInt32(themeAccent.backgroundRotation);
                        serializedData.writeInt64(0L);
                        serializedData.writeDouble(themeAccent.patternIntensity);
                        serializedData.writeBool(themeAccent.patternMotion);
                        serializedData.writeString(themeAccent.patternSlug);
                        serializedData.writeBool(themeAccent.info != null);
                        if (themeAccent.info != null) {
                            serializedData.writeInt32(themeAccent.account);
                            themeAccent.info.serializeToStream(serializedData);
                        }
                    }
                }
                edit.putString("accents_" + themeInfo.assetName, Base64.encodeToString(serializedData.toByteArray(), 3));
                if (!z5) {
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.themeAccentListUpdated, new Object[0]);
                }
                if (z4) {
                    MessagesController.getInstance(UserConfig.selectedAccount).saveThemeToServer(themeInfo, themeInfo.getAccent(false));
                }
            }
            edit.putInt("accent_current_" + themeInfo.assetName, themeInfo.currentAccentId);
            edit.commit();
        } else {
            if (themeInfo.prevAccentId != -1) {
                if (z2) {
                    ThemeAccent themeAccent2 = themeInfo.themeAccentsMap.get(themeInfo.currentAccentId);
                    themeInfo.themeAccentsMap.remove(themeAccent2.id);
                    themeInfo.themeAccents.remove(themeAccent2);
                    TLRPC$TL_theme tLRPC$TL_theme = themeAccent2.info;
                    if (tLRPC$TL_theme != null) {
                        themeInfo.accentsByThemeId.remove(tLRPC$TL_theme.id);
                    }
                }
                themeInfo.currentAccentId = themeInfo.prevAccentId;
                ThemeAccent accent = themeInfo.getAccent(false);
                if (accent != null) {
                    themeInfo.overrideWallpaper = accent.overrideWallpaper;
                } else {
                    themeInfo.overrideWallpaper = null;
                }
            }
            if (currentTheme == themeInfo) {
                refreshThemeColors();
            }
        }
        themeInfo.prevAccentId = -1;
    }

    public static void saveOtherThemes(boolean z) {
        saveOtherThemes(z, false);
    }

    private static void saveOtherThemes(boolean z, boolean z2) {
        ArrayList<ThemeAccent> arrayList;
        int i = 0;
        SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0).edit();
        if (z) {
            JSONArray jSONArray = new JSONArray();
            for (int i2 = 0; i2 < otherThemes.size(); i2++) {
                JSONObject saveJson = otherThemes.get(i2).getSaveJson();
                if (saveJson != null) {
                    jSONArray.put(saveJson);
                }
            }
            edit.putString("themes2", jSONArray.toString());
        }
        int i3 = 0;
        while (i3 < 4) {
            StringBuilder sb = new StringBuilder();
            sb.append("2remoteThemesHash");
            Object obj = "";
            sb.append(i3 != 0 ? Integer.valueOf(i3) : obj);
            edit.putLong(sb.toString(), remoteThemesHash[i3]);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("lastLoadingThemesTime");
            if (i3 != 0) {
                obj = Integer.valueOf(i3);
            }
            sb2.append(obj);
            edit.putInt(sb2.toString(), lastLoadingThemesTime[i3]);
            i3++;
        }
        edit.putInt("lastLoadingCurrentThemeTime", lastLoadingCurrentThemeTime);
        edit.commit();
        if (z) {
            while (i < 5) {
                ThemeInfo themeInfo = themesDict.get(i != 0 ? i != 1 ? i != 2 ? i != 3 ? "Night" : "Day" : "Arctic Blue" : "Dark Blue" : "Blue");
                if (themeInfo != null && (arrayList = themeInfo.themeAccents) != null && !arrayList.isEmpty()) {
                    saveThemeAccents(themeInfo, true, false, false, false, z2);
                }
                i++;
            }
        }
    }

    public static HashMap<String, Integer> getDefaultColors() {
        return defaultColors;
    }

    public static ThemeInfo getPreviousTheme() {
        return previousTheme;
    }

    public static String getCurrentNightThemeName() {
        ThemeInfo themeInfo = currentNightTheme;
        if (themeInfo == null) {
            return "";
        }
        String name = themeInfo.getName();
        return name.toLowerCase().endsWith(".attheme") ? name.substring(0, name.lastIndexOf(46)) : name;
    }

    public static ThemeInfo getCurrentTheme() {
        ThemeInfo themeInfo = currentDayTheme;
        return themeInfo != null ? themeInfo : defaultTheme;
    }

    public static ThemeInfo getCurrentNightTheme() {
        return currentNightTheme;
    }

    public static boolean isCurrentThemeNight() {
        return currentTheme == currentNightTheme;
    }

    public static boolean isCurrentThemeDark() {
        return currentTheme.isDark();
    }

    public static ThemeInfo getActiveTheme() {
        return currentTheme;
    }

    public static long getAutoNightSwitchThemeDelay() {
        return Math.abs(lastThemeSwitchTime - SystemClock.elapsedRealtime()) >= 12000 ? 1800L : 12000L;
    }

    public static void setCurrentNightTheme(ThemeInfo themeInfo) {
        boolean z = currentTheme == currentNightTheme;
        currentNightTheme = themeInfo;
        if (z) {
            applyDayNightThemeMaybe(true);
        }
    }

    public static void checkAutoNightThemeConditions() {
        checkAutoNightThemeConditions(false);
    }

    public static void cancelAutoNightThemeCallbacks() {
        if (selectedAutoNightType != 2) {
            if (switchNightRunnableScheduled) {
                switchNightRunnableScheduled = false;
                AndroidUtilities.cancelRunOnUIThread(switchNightBrightnessRunnable);
            }
            if (switchDayRunnableScheduled) {
                switchDayRunnableScheduled = false;
                AndroidUtilities.cancelRunOnUIThread(switchDayBrightnessRunnable);
            }
            if (!lightSensorRegistered) {
                return;
            }
            lastBrightnessValue = 1.0f;
            sensorManager.unregisterListener(ambientSensorListener, lightSensor);
            lightSensorRegistered = false;
            if (!BuildVars.LOGS_ENABLED) {
                return;
            }
            FileLog.d("light sensor unregistered");
        }
    }

    private static int needSwitchToTheme() {
        Sensor sensor;
        SensorEventListener sensorEventListener;
        int i;
        int i2;
        int i3 = selectedAutoNightType;
        if (i3 == 1) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int i4 = (calendar.get(11) * 60) + calendar.get(12);
            if (autoNightScheduleByLocation) {
                int i5 = calendar.get(5);
                if (autoNightLastSunCheckDay != i5) {
                    double d = autoNightLocationLatitude;
                    if (d != 10000.0d) {
                        double d2 = autoNightLocationLongitude;
                        if (d2 != 10000.0d) {
                            int[] calculateSunriseSunset = SunDate.calculateSunriseSunset(d, d2);
                            autoNightSunriseTime = calculateSunriseSunset[0];
                            autoNightSunsetTime = calculateSunriseSunset[1];
                            autoNightLastSunCheckDay = i5;
                            saveAutoNightThemeConfig();
                        }
                    }
                }
                i2 = autoNightSunsetTime;
                i = autoNightSunriseTime;
            } else {
                i2 = autoNightDayStartTime;
                i = autoNightDayEndTime;
            }
            return i2 < i ? (i2 > i4 || i4 > i) ? 1 : 2 : ((i2 > i4 || i4 > 1440) && (i4 < 0 || i4 > i)) ? 1 : 2;
        }
        if (i3 == 2) {
            if (lightSensor == null) {
                SensorManager sensorManager2 = (SensorManager) ApplicationLoader.applicationContext.getSystemService("sensor");
                sensorManager = sensorManager2;
                lightSensor = sensorManager2.getDefaultSensor(5);
            }
            if (!lightSensorRegistered && (sensor = lightSensor) != null && (sensorEventListener = ambientSensorListener) != null) {
                sensorManager.registerListener(sensorEventListener, sensor, 500000);
                lightSensorRegistered = true;
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("light sensor registered");
                }
            }
            if (lastBrightnessValue <= autoNightBrighnessThreshold) {
                if (!switchNightRunnableScheduled) {
                    return 2;
                }
            } else if (!switchDayRunnableScheduled) {
                return 1;
            }
        } else if (i3 == 3) {
            int i6 = ApplicationLoader.applicationContext.getResources().getConfiguration().uiMode & 48;
            if (i6 == 0 || i6 == 16) {
                return 1;
            }
            if (i6 == 32) {
                return 2;
            }
        } else if (i3 == 0) {
            return 1;
        }
        return 0;
    }

    public static void setChangingWallpaper(boolean z) {
        changingWallpaper = z;
        if (!z) {
            checkAutoNightThemeConditions(false);
        }
    }

    public static void checkAutoNightThemeConditions(boolean z) {
        if (previousTheme != null || changingWallpaper) {
            return;
        }
        if (!z && switchNightThemeDelay > 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j = elapsedRealtime - lastDelayUpdateTime;
            lastDelayUpdateTime = elapsedRealtime;
            int i = (int) (switchNightThemeDelay - j);
            switchNightThemeDelay = i;
            if (i > 0) {
                return;
            }
        }
        boolean z2 = false;
        if (z) {
            if (switchNightRunnableScheduled) {
                switchNightRunnableScheduled = false;
                AndroidUtilities.cancelRunOnUIThread(switchNightBrightnessRunnable);
            }
            if (switchDayRunnableScheduled) {
                switchDayRunnableScheduled = false;
                AndroidUtilities.cancelRunOnUIThread(switchDayBrightnessRunnable);
            }
        }
        cancelAutoNightThemeCallbacks();
        int needSwitchToTheme = needSwitchToTheme();
        if (needSwitchToTheme != 0) {
            if (needSwitchToTheme == 2) {
                z2 = true;
            }
            applyDayNightThemeMaybe(z2);
        }
        if (!z) {
            return;
        }
        lastThemeSwitchTime = 0L;
    }

    public static void applyDayNightThemeMaybe(boolean z) {
        if (previousTheme != null) {
            return;
        }
        if (z) {
            if (currentTheme == currentNightTheme) {
                return;
            }
            isInNigthMode = true;
            lastThemeSwitchTime = SystemClock.elapsedRealtime();
            switchingNightTheme = true;
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, currentNightTheme, Boolean.TRUE, null, -1);
            switchingNightTheme = false;
        } else if (currentTheme == currentDayTheme) {
        } else {
            isInNigthMode = false;
            lastThemeSwitchTime = SystemClock.elapsedRealtime();
            switchingNightTheme = true;
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, currentDayTheme, Boolean.TRUE, null, -1);
            switchingNightTheme = false;
        }
    }

    public static boolean deleteTheme(ThemeInfo themeInfo) {
        boolean z = false;
        if (themeInfo.pathToFile == null) {
            return false;
        }
        if (currentTheme == themeInfo) {
            applyTheme(defaultTheme, true, false, false);
            z = true;
        }
        if (themeInfo == currentNightTheme) {
            currentNightTheme = themesDict.get("Dark Blue");
        }
        themeInfo.removeObservers();
        otherThemes.remove(themeInfo);
        themesDict.remove(themeInfo.name);
        OverrideWallpaperInfo overrideWallpaperInfo = themeInfo.overrideWallpaper;
        if (overrideWallpaperInfo != null) {
            overrideWallpaperInfo.delete();
        }
        themes.remove(themeInfo);
        new File(themeInfo.pathToFile).delete();
        saveOtherThemes(true);
        return z;
    }

    public static ThemeInfo createNewTheme(String str) {
        ThemeInfo themeInfo = new ThemeInfo();
        File filesDirFixed = ApplicationLoader.getFilesDirFixed();
        themeInfo.pathToFile = new File(filesDirFixed, "theme" + Utilities.random.nextLong() + ".attheme").getAbsolutePath();
        themeInfo.name = str;
        themedWallpaperLink = getWallpaperUrl(currentTheme.overrideWallpaper);
        themeInfo.account = UserConfig.selectedAccount;
        saveCurrentTheme(themeInfo, true, true, false);
        return themeInfo;
    }

    private static String getWallpaperUrl(OverrideWallpaperInfo overrideWallpaperInfo) {
        String str;
        String str2 = null;
        if (overrideWallpaperInfo == null || TextUtils.isEmpty(overrideWallpaperInfo.slug) || overrideWallpaperInfo.slug.equals("d")) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (overrideWallpaperInfo.isBlurred) {
            sb.append("blur");
        }
        if (overrideWallpaperInfo.isMotion) {
            if (sb.length() > 0) {
                sb.append("+");
            }
            sb.append("motion");
        }
        int i = overrideWallpaperInfo.color;
        if (i == 0) {
            str = "https://attheme.org?slug=" + overrideWallpaperInfo.slug;
        } else {
            String lowerCase = String.format("%02x%02x%02x", Integer.valueOf(((byte) (i >> 16)) & 255), Integer.valueOf(((byte) (overrideWallpaperInfo.color >> 8)) & 255), Byte.valueOf((byte) (overrideWallpaperInfo.color & 255))).toLowerCase();
            int i2 = overrideWallpaperInfo.gradientColor1;
            String lowerCase2 = i2 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i2 >> 16)) & 255), Integer.valueOf(((byte) (overrideWallpaperInfo.gradientColor1 >> 8)) & 255), Byte.valueOf((byte) (overrideWallpaperInfo.gradientColor1 & 255))).toLowerCase() : null;
            int i3 = overrideWallpaperInfo.gradientColor2;
            String lowerCase3 = i3 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i3 >> 16)) & 255), Integer.valueOf(((byte) (overrideWallpaperInfo.gradientColor2 >> 8)) & 255), Byte.valueOf((byte) (overrideWallpaperInfo.gradientColor2 & 255))).toLowerCase() : null;
            int i4 = overrideWallpaperInfo.gradientColor3;
            if (i4 != 0) {
                str2 = String.format("%02x%02x%02x", Integer.valueOf(((byte) (i4 >> 16)) & 255), Integer.valueOf(((byte) (overrideWallpaperInfo.gradientColor3 >> 8)) & 255), Byte.valueOf((byte) (overrideWallpaperInfo.gradientColor3 & 255))).toLowerCase();
            }
            if (lowerCase2 == null || lowerCase3 == null) {
                if (lowerCase2 != null) {
                    lowerCase = (lowerCase + "-" + lowerCase2) + "&rotation=" + overrideWallpaperInfo.rotation;
                }
            } else if (str2 != null) {
                lowerCase = lowerCase + "~" + lowerCase2 + "~" + lowerCase3 + "~" + str2;
            } else {
                lowerCase = lowerCase + "~" + lowerCase2 + "~" + lowerCase3;
            }
            str = "https://attheme.org?slug=" + overrideWallpaperInfo.slug + "&intensity=" + ((int) (overrideWallpaperInfo.intensity * 100.0f)) + "&bg_color=" + lowerCase;
        }
        if (sb.length() <= 0) {
            return str;
        }
        return str + "&mode=" + sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:106:0x021c  */
    /* JADX WARN: Removed duplicated region for block: B:135:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:104:0x0217 -> B:117:0x021a). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void saveCurrentTheme(ThemeInfo themeInfo, boolean z, boolean z2, boolean z3) {
        String str;
        Throwable th;
        Exception e;
        FileOutputStream fileOutputStream;
        OverrideWallpaperInfo overrideWallpaperInfo = themeInfo.overrideWallpaper;
        if (overrideWallpaperInfo != null) {
            str = getWallpaperUrl(overrideWallpaperInfo);
        } else {
            str = themedWallpaperLink;
        }
        Drawable drawable = z2 ? wallpaper : themedWallpaper;
        if (z2 && drawable != null) {
            themedWallpaper = wallpaper;
        }
        ThemeAccent accent = currentTheme.getAccent(false);
        HashMap<String, Integer> hashMap = (!currentTheme.firstAccentIsDefault || accent.id != DEFALT_THEME_ACCENT_ID) ? currentColors : defaultColors;
        StringBuilder sb = new StringBuilder();
        if (hashMap != defaultColors) {
            int i = accent != null ? accent.myMessagesAccentColor : 0;
            int i2 = accent != null ? accent.myMessagesGradientAccentColor1 : 0;
            int i3 = accent != null ? accent.myMessagesGradientAccentColor2 : 0;
            int i4 = accent != null ? accent.myMessagesGradientAccentColor3 : 0;
            if (i != 0 && i2 != 0) {
                hashMap.put("chat_outBubble", Integer.valueOf(i));
                hashMap.put("chat_outBubbleGradient", Integer.valueOf(i2));
                if (i3 != 0) {
                    hashMap.put("chat_outBubbleGradient2", Integer.valueOf(i3));
                    if (i4 != 0) {
                        hashMap.put("chat_outBubbleGradient3", Integer.valueOf(i4));
                    }
                }
                hashMap.put("chat_outBubbleGradientAnimated", Integer.valueOf((accent == null || !accent.myMessagesAnimated) ? 0 : 1));
            }
        }
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            if ((!(drawable instanceof BitmapDrawable) && str == null) || (!"chat_wallpaper".equals(key) && !"chat_wallpaper_gradient_to".equals(key) && !"key_chat_wallpaper_gradient_to2".equals(key) && !"key_chat_wallpaper_gradient_to3".equals(key))) {
                sb.append(key);
                sb.append("=");
                sb.append(entry.getValue());
                sb.append("\n");
            }
        }
        FileOutputStream fileOutputStream2 = null;
        try {
            try {
                try {
                    fileOutputStream = new FileOutputStream(themeInfo.pathToFile);
                } catch (Exception e2) {
                    e = e2;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        try {
            if (sb.length() == 0 && !(drawable instanceof BitmapDrawable) && TextUtils.isEmpty(str)) {
                sb.append(' ');
            }
            fileOutputStream.write(AndroidUtilities.getStringBytes(sb.toString()));
            if (!TextUtils.isEmpty(str)) {
                fileOutputStream.write(AndroidUtilities.getStringBytes("WLS=" + str + "\n"));
                if (z2) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                    FileOutputStream fileOutputStream3 = new FileOutputStream(new File(filesDirFixed, Utilities.MD5(str) + ".wp"));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream3);
                    fileOutputStream3.close();
                }
            } else if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap2 = ((BitmapDrawable) drawable).getBitmap();
                if (bitmap2 != null) {
                    fileOutputStream.write(new byte[]{87, 80, 83, 10});
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream);
                    fileOutputStream.write(new byte[]{10, 87, 80, 69, 10});
                }
                if (z && !z3) {
                    wallpaper = drawable;
                    calcBackgroundColor(drawable, 2);
                }
            }
            if (!z3) {
                if (themesDict.get(themeInfo.getKey()) == null) {
                    themes.add(themeInfo);
                    themesDict.put(themeInfo.getKey(), themeInfo);
                    otherThemes.add(themeInfo);
                    saveOtherThemes(true);
                    sortThemes();
                }
                currentTheme = themeInfo;
                if (themeInfo != currentNightTheme) {
                    currentDayTheme = themeInfo;
                }
                if (hashMap == defaultColors) {
                    currentColorsNoAccent.clear();
                    refreshThemeColors();
                }
                SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
                edit.putString("theme", currentDayTheme.getKey());
                edit.commit();
            }
            fileOutputStream.close();
        } catch (Exception e4) {
            e = e4;
            fileOutputStream2 = fileOutputStream;
            FileLog.e(e);
            if (fileOutputStream2 != null) {
                fileOutputStream2.close();
            }
            if (!z) {
            }
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream2 = fileOutputStream;
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.close();
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
            }
            throw th;
        }
        if (!z) {
            MessagesController.getInstance(themeInfo.account).saveThemeToServer(themeInfo, themeInfo.getAccent(false));
        }
    }

    public static void checkCurrentRemoteTheme(boolean z) {
        int i;
        if (loadingCurrentTheme == 0) {
            if (!z && Math.abs((System.currentTimeMillis() / 1000) - lastLoadingCurrentThemeTime) < 3600) {
                return;
            }
            int i2 = 0;
            while (i2 < 2) {
                final ThemeInfo themeInfo = i2 == 0 ? currentDayTheme : currentNightTheme;
                if (themeInfo != null && UserConfig.getInstance(themeInfo.account).isClientActivated()) {
                    final ThemeAccent accent = themeInfo.getAccent(false);
                    final TLRPC$TL_theme tLRPC$TL_theme = themeInfo.info;
                    if (tLRPC$TL_theme != null) {
                        i = themeInfo.account;
                    } else if (accent != null && (tLRPC$TL_theme = accent.info) != null) {
                        i = UserConfig.selectedAccount;
                    }
                    if (tLRPC$TL_theme != null && tLRPC$TL_theme.document != null) {
                        loadingCurrentTheme++;
                        TLRPC$TL_account_getTheme tLRPC$TL_account_getTheme = new TLRPC$TL_account_getTheme();
                        tLRPC$TL_account_getTheme.document_id = tLRPC$TL_theme.document.id;
                        tLRPC$TL_account_getTheme.format = "android";
                        TLRPC$TL_inputTheme tLRPC$TL_inputTheme = new TLRPC$TL_inputTheme();
                        tLRPC$TL_inputTheme.access_hash = tLRPC$TL_theme.access_hash;
                        tLRPC$TL_inputTheme.id = tLRPC$TL_theme.id;
                        tLRPC$TL_account_getTheme.theme = tLRPC$TL_inputTheme;
                        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_getTheme, new RequestDelegate() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda10
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                Theme.lambda$checkCurrentRemoteTheme$4(Theme.ThemeAccent.this, themeInfo, tLRPC$TL_theme, tLObject, tLRPC$TL_error);
                            }
                        });
                    }
                }
                i2++;
            }
        }
    }

    public static /* synthetic */ void lambda$checkCurrentRemoteTheme$4(final ThemeAccent themeAccent, final ThemeInfo themeInfo, final TLRPC$TL_theme tLRPC$TL_theme, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$checkCurrentRemoteTheme$3(TLObject.this, themeAccent, themeInfo, tLRPC$TL_theme);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$checkCurrentRemoteTheme$3(TLObject tLObject, ThemeAccent themeAccent, ThemeInfo themeInfo, TLRPC$TL_theme tLRPC$TL_theme) {
        boolean z;
        TLRPC$WallPaperSettings tLRPC$WallPaperSettings;
        boolean z2 = true;
        loadingCurrentTheme--;
        if (tLObject instanceof TLRPC$TL_theme) {
            TLRPC$TL_theme tLRPC$TL_theme2 = (TLRPC$TL_theme) tLObject;
            TLRPC$ThemeSettings tLRPC$ThemeSettings = tLRPC$TL_theme2.settings.size() > 0 ? tLRPC$TL_theme2.settings.get(0) : null;
            if (themeAccent != null && tLRPC$ThemeSettings != null) {
                if (!ThemeInfo.accentEquals(themeAccent, tLRPC$ThemeSettings)) {
                    File pathToWallpaper = themeAccent.getPathToWallpaper();
                    if (pathToWallpaper != null) {
                        pathToWallpaper.delete();
                    }
                    ThemeInfo.fillAccentValues(themeAccent, tLRPC$ThemeSettings);
                    ThemeInfo themeInfo2 = currentTheme;
                    if (themeInfo2 == themeInfo && themeInfo2.currentAccentId == themeAccent.id) {
                        refreshThemeColors();
                        createChatResources(ApplicationLoader.applicationContext, false);
                        NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
                        int i = NotificationCenter.needSetDayNightTheme;
                        Object[] objArr = new Object[4];
                        ThemeInfo themeInfo3 = currentTheme;
                        objArr[0] = themeInfo3;
                        objArr[1] = Boolean.valueOf(currentNightTheme == themeInfo3);
                        objArr[2] = null;
                        objArr[3] = -1;
                        globalInstance.postNotificationName(i, objArr);
                    }
                    PatternsLoader.createLoader(true);
                    z = true;
                } else {
                    z = false;
                }
                TLRPC$WallPaper tLRPC$WallPaper = tLRPC$ThemeSettings.wallpaper;
                if (tLRPC$WallPaper == null || (tLRPC$WallPaperSettings = tLRPC$WallPaper.settings) == null || !tLRPC$WallPaperSettings.motion) {
                    z2 = false;
                }
                themeAccent.patternMotion = z2;
                z2 = z;
            } else {
                TLRPC$Document tLRPC$Document = tLRPC$TL_theme2.document;
                if (tLRPC$Document != null && tLRPC$Document.id != tLRPC$TL_theme.document.id) {
                    if (themeAccent != null) {
                        themeAccent.info = tLRPC$TL_theme2;
                    } else {
                        themeInfo.info = tLRPC$TL_theme2;
                        themeInfo.loadThemeDocument();
                    }
                }
            }
            if (loadingCurrentTheme == 0) {
                return;
            }
            lastLoadingCurrentThemeTime = (int) (System.currentTimeMillis() / 1000);
            saveOtherThemes(z2);
            return;
        }
        z2 = false;
        if (loadingCurrentTheme == 0) {
        }
    }

    public static void loadRemoteThemes(final int i, boolean z) {
        if (!loadingRemoteThemes[i]) {
            if ((!z && Math.abs((System.currentTimeMillis() / 1000) - lastLoadingThemesTime[i]) < 3600) || !UserConfig.getInstance(i).isClientActivated()) {
                return;
            }
            loadingRemoteThemes[i] = true;
            TLRPC$TL_account_getThemes tLRPC$TL_account_getThemes = new TLRPC$TL_account_getThemes();
            tLRPC$TL_account_getThemes.format = "android";
            if (!MediaDataController.getInstance(i).defaultEmojiThemes.isEmpty()) {
                tLRPC$TL_account_getThemes.hash = remoteThemesHash[i];
            }
            if (BuildVars.LOGS_ENABLED) {
                Log.i("theme", "loading remote themes, hash " + tLRPC$TL_account_getThemes.hash);
            }
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_getThemes, new RequestDelegate() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda9
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    Theme.lambda$loadRemoteThemes$6(i, tLObject, tLRPC$TL_error);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$loadRemoteThemes$6(final int i, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$loadRemoteThemes$5(i, tLObject);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x020d  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0219  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$loadRemoteThemes$5(int i, TLObject tLObject) {
        boolean z;
        String baseThemeKey;
        ThemeInfo themeInfo;
        TLRPC$WallPaperSettings tLRPC$WallPaperSettings;
        loadingRemoteThemes[i] = false;
        if (tLObject instanceof TLRPC$TL_account_themes) {
            TLRPC$TL_account_themes tLRPC$TL_account_themes = (TLRPC$TL_account_themes) tLObject;
            remoteThemesHash[i] = tLRPC$TL_account_themes.hash;
            lastLoadingThemesTime[i] = (int) (System.currentTimeMillis() / 1000);
            ArrayList<TLRPC$TL_theme> arrayList = new ArrayList<>();
            ArrayList arrayList2 = new ArrayList();
            int size = themes.size();
            for (int i2 = 0; i2 < size; i2++) {
                ThemeInfo themeInfo2 = themes.get(i2);
                if (themeInfo2.info != null && themeInfo2.account == i) {
                    arrayList2.add(themeInfo2);
                } else if (themeInfo2.themeAccents != null) {
                    for (int i3 = 0; i3 < themeInfo2.themeAccents.size(); i3++) {
                        ThemeAccent themeAccent = themeInfo2.themeAccents.get(i3);
                        if (themeAccent.info != null && themeAccent.account == i) {
                            arrayList2.add(themeAccent);
                        }
                    }
                }
            }
            int size2 = tLRPC$TL_account_themes.themes.size();
            boolean z2 = false;
            boolean z3 = false;
            for (int i4 = 0; i4 < size2; i4++) {
                TLRPC$TL_theme tLRPC$TL_theme = tLRPC$TL_account_themes.themes.get(i4);
                if (tLRPC$TL_theme instanceof TLRPC$TL_theme) {
                    if (tLRPC$TL_theme.isDefault) {
                        arrayList.add(tLRPC$TL_theme);
                    }
                    ArrayList<TLRPC$ThemeSettings> arrayList3 = tLRPC$TL_theme.settings;
                    if (arrayList3 != null && arrayList3.size() > 0) {
                        for (int i5 = 0; i5 < tLRPC$TL_theme.settings.size(); i5++) {
                            TLRPC$ThemeSettings tLRPC$ThemeSettings = tLRPC$TL_theme.settings.get(i5);
                            if (tLRPC$ThemeSettings != null && (baseThemeKey = getBaseThemeKey(tLRPC$ThemeSettings)) != null && (themeInfo = themesDict.get(baseThemeKey)) != null && themeInfo.themeAccents != null) {
                                ArrayList arrayList4 = arrayList2;
                                ThemeAccent themeAccent2 = themeInfo.accentsByThemeId.get(tLRPC$TL_theme.id);
                                if (themeAccent2 != null) {
                                    if (!ThemeInfo.accentEquals(themeAccent2, tLRPC$ThemeSettings)) {
                                        File pathToWallpaper = themeAccent2.getPathToWallpaper();
                                        if (pathToWallpaper != null) {
                                            pathToWallpaper.delete();
                                        }
                                        ThemeInfo.fillAccentValues(themeAccent2, tLRPC$ThemeSettings);
                                        ThemeInfo themeInfo3 = currentTheme;
                                        if (themeInfo3 == themeInfo && themeInfo3.currentAccentId == themeAccent2.id) {
                                            refreshThemeColors();
                                            NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
                                            int i6 = NotificationCenter.needSetDayNightTheme;
                                            Object[] objArr = new Object[4];
                                            ThemeInfo themeInfo4 = currentTheme;
                                            objArr[0] = themeInfo4;
                                            objArr[1] = Boolean.valueOf(currentNightTheme == themeInfo4);
                                            objArr[2] = null;
                                            objArr[3] = -1;
                                            globalInstance.postNotificationName(i6, objArr);
                                        }
                                        z2 = true;
                                        z3 = true;
                                    }
                                    TLRPC$WallPaper tLRPC$WallPaper = tLRPC$ThemeSettings.wallpaper;
                                    themeAccent2.patternMotion = (tLRPC$WallPaper == null || (tLRPC$WallPaperSettings = tLRPC$WallPaper.settings) == null || !tLRPC$WallPaperSettings.motion) ? false : true;
                                    arrayList2 = arrayList4;
                                    arrayList2.remove(themeAccent2);
                                } else {
                                    arrayList2 = arrayList4;
                                    ThemeAccent createNewAccent = themeInfo.createNewAccent(tLRPC$TL_theme, i, false, i5);
                                    if (!TextUtils.isEmpty(createNewAccent.patternSlug)) {
                                        themeAccent2 = createNewAccent;
                                        z2 = true;
                                    } else {
                                        themeAccent2 = createNewAccent;
                                    }
                                }
                                themeAccent2.isDefault = tLRPC$TL_theme.isDefault;
                            }
                        }
                    } else {
                        String str = "remote" + tLRPC$TL_theme.id;
                        ThemeInfo themeInfo5 = themesDict.get(str);
                        if (themeInfo5 == null) {
                            themeInfo5 = new ThemeInfo();
                            themeInfo5.account = i;
                            themeInfo5.pathToFile = new File(ApplicationLoader.getFilesDirFixed(), str + ".attheme").getAbsolutePath();
                            themes.add(themeInfo5);
                            otherThemes.add(themeInfo5);
                            z3 = true;
                        } else {
                            arrayList2.remove(themeInfo5);
                        }
                        themeInfo5.name = tLRPC$TL_theme.title;
                        themeInfo5.info = tLRPC$TL_theme;
                        themesDict.put(themeInfo5.getKey(), themeInfo5);
                    }
                }
            }
            int size3 = arrayList2.size();
            for (int i7 = 0; i7 < size3; i7++) {
                Object obj = arrayList2.get(i7);
                if (obj instanceof ThemeInfo) {
                    ThemeInfo themeInfo6 = (ThemeInfo) obj;
                    themeInfo6.removeObservers();
                    otherThemes.remove(themeInfo6);
                    themesDict.remove(themeInfo6.name);
                    OverrideWallpaperInfo overrideWallpaperInfo = themeInfo6.overrideWallpaper;
                    if (overrideWallpaperInfo != null) {
                        overrideWallpaperInfo.delete();
                    }
                    themes.remove(themeInfo6);
                    new File(themeInfo6.pathToFile).delete();
                    if (currentDayTheme == themeInfo6) {
                        currentDayTheme = defaultTheme;
                    } else if (currentNightTheme == themeInfo6) {
                        currentNightTheme = themesDict.get("Dark Blue");
                        z = true;
                        if (currentTheme == themeInfo6) {
                            applyTheme(z ? currentNightTheme : currentDayTheme, true, false, z);
                        }
                    }
                    z = false;
                    if (currentTheme == themeInfo6) {
                    }
                } else if (obj instanceof ThemeAccent) {
                    ThemeAccent themeAccent3 = (ThemeAccent) obj;
                    if (deleteThemeAccent(themeAccent3.parentTheme, themeAccent3, false) && currentTheme == themeAccent3.parentTheme) {
                        refreshThemeColors();
                        NotificationCenter globalInstance2 = NotificationCenter.getGlobalInstance();
                        int i8 = NotificationCenter.needSetDayNightTheme;
                        Object[] objArr2 = new Object[4];
                        ThemeInfo themeInfo7 = currentTheme;
                        objArr2[0] = themeInfo7;
                        objArr2[1] = Boolean.valueOf(currentNightTheme == themeInfo7);
                        objArr2[2] = null;
                        objArr2[3] = -1;
                        globalInstance2.postNotificationName(i8, objArr2);
                    }
                }
            }
            saveOtherThemes(true);
            sortThemes();
            if (z3) {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.themeListUpdated, new Object[0]);
            }
            if (z2) {
                PatternsLoader.createLoader(true);
            }
            MediaDataController.getInstance(i).generateEmojiPreviewThemes(arrayList, i);
        }
    }

    public static String getBaseThemeKey(TLRPC$ThemeSettings tLRPC$ThemeSettings) {
        TLRPC$BaseTheme tLRPC$BaseTheme = tLRPC$ThemeSettings.base_theme;
        if (tLRPC$BaseTheme instanceof TLRPC$TL_baseThemeClassic) {
            return "Blue";
        }
        if (tLRPC$BaseTheme instanceof TLRPC$TL_baseThemeDay) {
            return "Day";
        }
        if (tLRPC$BaseTheme instanceof TLRPC$TL_baseThemeTinted) {
            return "Dark Blue";
        }
        if (tLRPC$BaseTheme instanceof TLRPC$TL_baseThemeArctic) {
            return "Arctic Blue";
        }
        if (!(tLRPC$BaseTheme instanceof TLRPC$TL_baseThemeNight)) {
            return null;
        }
        return "Night";
    }

    public static TLRPC$BaseTheme getBaseThemeByKey(String str) {
        if ("Blue".equals(str)) {
            return new TLRPC$TL_baseThemeClassic();
        }
        if ("Day".equals(str)) {
            return new TLRPC$TL_baseThemeDay();
        }
        if ("Dark Blue".equals(str)) {
            return new TLRPC$TL_baseThemeTinted();
        }
        if ("Arctic Blue".equals(str)) {
            return new TLRPC$TL_baseThemeArctic();
        }
        if (!"Night".equals(str)) {
            return null;
        }
        return new TLRPC$TL_baseThemeNight();
    }

    public static void setThemeFileReference(TLRPC$TL_theme tLRPC$TL_theme) {
        TLRPC$Document tLRPC$Document;
        int size = themes.size();
        for (int i = 0; i < size; i++) {
            TLRPC$TL_theme tLRPC$TL_theme2 = themes.get(i).info;
            if (tLRPC$TL_theme2 != null && tLRPC$TL_theme2.id == tLRPC$TL_theme.id) {
                TLRPC$Document tLRPC$Document2 = tLRPC$TL_theme2.document;
                if (tLRPC$Document2 == null || (tLRPC$Document = tLRPC$TL_theme.document) == null) {
                    return;
                }
                tLRPC$Document2.file_reference = tLRPC$Document.file_reference;
                saveOtherThemes(true);
                return;
            }
        }
    }

    public static boolean isThemeInstalled(ThemeInfo themeInfo) {
        return (themeInfo == null || themesDict.get(themeInfo.getKey()) == null) ? false : true;
    }

    public static void setThemeUploadInfo(ThemeInfo themeInfo, ThemeAccent themeAccent, TLRPC$TL_theme tLRPC$TL_theme, int i, boolean z) {
        String str;
        TLRPC$WallPaperSettings tLRPC$WallPaperSettings;
        if (tLRPC$TL_theme == null) {
            return;
        }
        TLRPC$ThemeSettings tLRPC$ThemeSettings = tLRPC$TL_theme.settings.size() > 0 ? tLRPC$TL_theme.settings.get(0) : null;
        if (tLRPC$ThemeSettings != null) {
            if (themeInfo == null) {
                String baseThemeKey = getBaseThemeKey(tLRPC$ThemeSettings);
                if (baseThemeKey == null || (themeInfo = themesDict.get(baseThemeKey)) == null) {
                    return;
                }
                themeAccent = themeInfo.accentsByThemeId.get(tLRPC$TL_theme.id);
            }
            if (themeAccent == null) {
                return;
            }
            TLRPC$TL_theme tLRPC$TL_theme2 = themeAccent.info;
            if (tLRPC$TL_theme2 != null) {
                themeInfo.accentsByThemeId.remove(tLRPC$TL_theme2.id);
            }
            themeAccent.info = tLRPC$TL_theme;
            themeAccent.account = i;
            themeInfo.accentsByThemeId.put(tLRPC$TL_theme.id, themeAccent);
            if (!ThemeInfo.accentEquals(themeAccent, tLRPC$ThemeSettings)) {
                File pathToWallpaper = themeAccent.getPathToWallpaper();
                if (pathToWallpaper != null) {
                    pathToWallpaper.delete();
                }
                ThemeInfo.fillAccentValues(themeAccent, tLRPC$ThemeSettings);
                ThemeInfo themeInfo2 = currentTheme;
                if (themeInfo2 == themeInfo && themeInfo2.currentAccentId == themeAccent.id) {
                    refreshThemeColors();
                    NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
                    int i2 = NotificationCenter.needSetDayNightTheme;
                    Object[] objArr = new Object[4];
                    ThemeInfo themeInfo3 = currentTheme;
                    objArr[0] = themeInfo3;
                    objArr[1] = Boolean.valueOf(currentNightTheme == themeInfo3);
                    objArr[2] = null;
                    objArr[3] = -1;
                    globalInstance.postNotificationName(i2, objArr);
                }
                PatternsLoader.createLoader(true);
            }
            TLRPC$WallPaper tLRPC$WallPaper = tLRPC$ThemeSettings.wallpaper;
            themeAccent.patternMotion = (tLRPC$WallPaper == null || (tLRPC$WallPaperSettings = tLRPC$WallPaper.settings) == null || !tLRPC$WallPaperSettings.motion) ? false : true;
            themeInfo.previewParsed = false;
        } else {
            if (themeInfo != null) {
                HashMap<String, ThemeInfo> hashMap = themesDict;
                str = themeInfo.getKey();
                hashMap.remove(str);
            } else {
                str = "remote" + tLRPC$TL_theme.id;
                themeInfo = themesDict.get(str);
            }
            if (themeInfo == null) {
                return;
            }
            themeInfo.info = tLRPC$TL_theme;
            themeInfo.name = tLRPC$TL_theme.title;
            File file = new File(themeInfo.pathToFile);
            File file2 = new File(ApplicationLoader.getFilesDirFixed(), str + ".attheme");
            if (!file.equals(file2)) {
                try {
                    AndroidUtilities.copyFile(file, file2);
                    themeInfo.pathToFile = file2.getAbsolutePath();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            if (z) {
                themeInfo.loadThemeDocument();
            } else {
                themeInfo.previewParsed = false;
            }
            themesDict.put(themeInfo.getKey(), themeInfo);
        }
        saveOtherThemes(true);
    }

    public static File getAssetFile(String str) {
        long j;
        File file = new File(ApplicationLoader.getFilesDirFixed(), str);
        try {
            InputStream open = ApplicationLoader.applicationContext.getAssets().open(str);
            j = open.available();
            open.close();
        } catch (Exception e) {
            FileLog.e(e);
            j = 0;
        }
        if (!file.exists() || (j != 0 && file.length() != j)) {
            try {
                InputStream open2 = ApplicationLoader.applicationContext.getAssets().open(str);
                AndroidUtilities.copyFile(open2, file);
                if (open2 != null) {
                    open2.close();
                }
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        return file;
    }

    public static int getPreviewColor(HashMap<String, Integer> hashMap, String str) {
        Integer num = hashMap.get(str);
        if (num == null) {
            num = defaultColors.get(str);
        }
        return num.intValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:112:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x024b A[Catch: all -> 0x0680, TryCatch #18 {all -> 0x0680, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x0089, B:11:0x0091, B:16:0x00a0, B:25:0x00b2, B:28:0x00ba, B:32:0x00c3, B:39:0x00d1, B:42:0x00d9, B:46:0x00e2, B:53:0x00f7, B:56:0x00ff, B:60:0x010a, B:67:0x011a, B:69:0x0123, B:71:0x0131, B:74:0x013b, B:76:0x014b, B:78:0x0155, B:80:0x015f, B:81:0x016e, B:83:0x0176, B:85:0x0182, B:87:0x0192, B:92:0x019d, B:94:0x01a5, B:96:0x01b1, B:98:0x01bf, B:110:0x01e0, B:113:0x01ef, B:115:0x024b, B:119:0x0257, B:123:0x0267, B:124:0x0272, B:150:0x0347, B:155:0x0353, B:157:0x035b, B:158:0x0367, B:160:0x0371, B:163:0x037b, B:164:0x037f, B:165:0x039b, B:167:0x03b0, B:169:0x03b6, B:203:0x04b0, B:206:0x04b6, B:213:0x04c7, B:216:0x04cd, B:219:0x04e4, B:221:0x0503, B:223:0x052c, B:225:0x0545, B:226:0x0567, B:228:0x05f7, B:231:0x0619, B:232:0x0641, B:211:0x04c2, B:233:0x0669), top: B:264:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:152:0x034d  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x04e4 A[Catch: all -> 0x0680, TryCatch #18 {all -> 0x0680, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x0089, B:11:0x0091, B:16:0x00a0, B:25:0x00b2, B:28:0x00ba, B:32:0x00c3, B:39:0x00d1, B:42:0x00d9, B:46:0x00e2, B:53:0x00f7, B:56:0x00ff, B:60:0x010a, B:67:0x011a, B:69:0x0123, B:71:0x0131, B:74:0x013b, B:76:0x014b, B:78:0x0155, B:80:0x015f, B:81:0x016e, B:83:0x0176, B:85:0x0182, B:87:0x0192, B:92:0x019d, B:94:0x01a5, B:96:0x01b1, B:98:0x01bf, B:110:0x01e0, B:113:0x01ef, B:115:0x024b, B:119:0x0257, B:123:0x0267, B:124:0x0272, B:150:0x0347, B:155:0x0353, B:157:0x035b, B:158:0x0367, B:160:0x0371, B:163:0x037b, B:164:0x037f, B:165:0x039b, B:167:0x03b0, B:169:0x03b6, B:203:0x04b0, B:206:0x04b6, B:213:0x04c7, B:216:0x04cd, B:219:0x04e4, B:221:0x0503, B:223:0x052c, B:225:0x0545, B:226:0x0567, B:228:0x05f7, B:231:0x0619, B:232:0x0641, B:211:0x04c2, B:233:0x0669), top: B:264:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:220:0x0502  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x052c A[Catch: all -> 0x0680, TryCatch #18 {all -> 0x0680, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x0089, B:11:0x0091, B:16:0x00a0, B:25:0x00b2, B:28:0x00ba, B:32:0x00c3, B:39:0x00d1, B:42:0x00d9, B:46:0x00e2, B:53:0x00f7, B:56:0x00ff, B:60:0x010a, B:67:0x011a, B:69:0x0123, B:71:0x0131, B:74:0x013b, B:76:0x014b, B:78:0x0155, B:80:0x015f, B:81:0x016e, B:83:0x0176, B:85:0x0182, B:87:0x0192, B:92:0x019d, B:94:0x01a5, B:96:0x01b1, B:98:0x01bf, B:110:0x01e0, B:113:0x01ef, B:115:0x024b, B:119:0x0257, B:123:0x0267, B:124:0x0272, B:150:0x0347, B:155:0x0353, B:157:0x035b, B:158:0x0367, B:160:0x0371, B:163:0x037b, B:164:0x037f, B:165:0x039b, B:167:0x03b0, B:169:0x03b6, B:203:0x04b0, B:206:0x04b6, B:213:0x04c7, B:216:0x04cd, B:219:0x04e4, B:221:0x0503, B:223:0x052c, B:225:0x0545, B:226:0x0567, B:228:0x05f7, B:231:0x0619, B:232:0x0641, B:211:0x04c2, B:233:0x0669), top: B:264:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0545 A[Catch: all -> 0x0680, TryCatch #18 {all -> 0x0680, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x0089, B:11:0x0091, B:16:0x00a0, B:25:0x00b2, B:28:0x00ba, B:32:0x00c3, B:39:0x00d1, B:42:0x00d9, B:46:0x00e2, B:53:0x00f7, B:56:0x00ff, B:60:0x010a, B:67:0x011a, B:69:0x0123, B:71:0x0131, B:74:0x013b, B:76:0x014b, B:78:0x0155, B:80:0x015f, B:81:0x016e, B:83:0x0176, B:85:0x0182, B:87:0x0192, B:92:0x019d, B:94:0x01a5, B:96:0x01b1, B:98:0x01bf, B:110:0x01e0, B:113:0x01ef, B:115:0x024b, B:119:0x0257, B:123:0x0267, B:124:0x0272, B:150:0x0347, B:155:0x0353, B:157:0x035b, B:158:0x0367, B:160:0x0371, B:163:0x037b, B:164:0x037f, B:165:0x039b, B:167:0x03b0, B:169:0x03b6, B:203:0x04b0, B:206:0x04b6, B:213:0x04c7, B:216:0x04cd, B:219:0x04e4, B:221:0x0503, B:223:0x052c, B:225:0x0545, B:226:0x0567, B:228:0x05f7, B:231:0x0619, B:232:0x0641, B:211:0x04c2, B:233:0x0669), top: B:264:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:228:0x05f7 A[Catch: all -> 0x0680, TryCatch #18 {all -> 0x0680, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x0089, B:11:0x0091, B:16:0x00a0, B:25:0x00b2, B:28:0x00ba, B:32:0x00c3, B:39:0x00d1, B:42:0x00d9, B:46:0x00e2, B:53:0x00f7, B:56:0x00ff, B:60:0x010a, B:67:0x011a, B:69:0x0123, B:71:0x0131, B:74:0x013b, B:76:0x014b, B:78:0x0155, B:80:0x015f, B:81:0x016e, B:83:0x0176, B:85:0x0182, B:87:0x0192, B:92:0x019d, B:94:0x01a5, B:96:0x01b1, B:98:0x01bf, B:110:0x01e0, B:113:0x01ef, B:115:0x024b, B:119:0x0257, B:123:0x0267, B:124:0x0272, B:150:0x0347, B:155:0x0353, B:157:0x035b, B:158:0x0367, B:160:0x0371, B:163:0x037b, B:164:0x037f, B:165:0x039b, B:167:0x03b0, B:169:0x03b6, B:203:0x04b0, B:206:0x04b6, B:213:0x04c7, B:216:0x04cd, B:219:0x04e4, B:221:0x0503, B:223:0x052c, B:225:0x0545, B:226:0x0567, B:228:0x05f7, B:231:0x0619, B:232:0x0641, B:211:0x04c2, B:233:0x0669), top: B:264:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0619 A[Catch: all -> 0x0680, TryCatch #18 {all -> 0x0680, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x0089, B:11:0x0091, B:16:0x00a0, B:25:0x00b2, B:28:0x00ba, B:32:0x00c3, B:39:0x00d1, B:42:0x00d9, B:46:0x00e2, B:53:0x00f7, B:56:0x00ff, B:60:0x010a, B:67:0x011a, B:69:0x0123, B:71:0x0131, B:74:0x013b, B:76:0x014b, B:78:0x0155, B:80:0x015f, B:81:0x016e, B:83:0x0176, B:85:0x0182, B:87:0x0192, B:92:0x019d, B:94:0x01a5, B:96:0x01b1, B:98:0x01bf, B:110:0x01e0, B:113:0x01ef, B:115:0x024b, B:119:0x0257, B:123:0x0267, B:124:0x0272, B:150:0x0347, B:155:0x0353, B:157:0x035b, B:158:0x0367, B:160:0x0371, B:163:0x037b, B:164:0x037f, B:165:0x039b, B:167:0x03b0, B:169:0x03b6, B:203:0x04b0, B:206:0x04b6, B:213:0x04c7, B:216:0x04cd, B:219:0x04e4, B:221:0x0503, B:223:0x052c, B:225:0x0545, B:226:0x0567, B:228:0x05f7, B:231:0x0619, B:232:0x0641, B:211:0x04c2, B:233:0x0669), top: B:264:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:238:0x028a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:240:0x04c7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0123 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:250:0x04b0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:254:0x02c3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00b2 A[Catch: all -> 0x0680, TryCatch #18 {all -> 0x0680, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x0089, B:11:0x0091, B:16:0x00a0, B:25:0x00b2, B:28:0x00ba, B:32:0x00c3, B:39:0x00d1, B:42:0x00d9, B:46:0x00e2, B:53:0x00f7, B:56:0x00ff, B:60:0x010a, B:67:0x011a, B:69:0x0123, B:71:0x0131, B:74:0x013b, B:76:0x014b, B:78:0x0155, B:80:0x015f, B:81:0x016e, B:83:0x0176, B:85:0x0182, B:87:0x0192, B:92:0x019d, B:94:0x01a5, B:96:0x01b1, B:98:0x01bf, B:110:0x01e0, B:113:0x01ef, B:115:0x024b, B:119:0x0257, B:123:0x0267, B:124:0x0272, B:150:0x0347, B:155:0x0353, B:157:0x035b, B:158:0x0367, B:160:0x0371, B:163:0x037b, B:164:0x037f, B:165:0x039b, B:167:0x03b0, B:169:0x03b6, B:203:0x04b0, B:206:0x04b6, B:213:0x04c7, B:216:0x04cd, B:219:0x04e4, B:221:0x0503, B:223:0x052c, B:225:0x0545, B:226:0x0567, B:228:0x05f7, B:231:0x0619, B:232:0x0641, B:211:0x04c2, B:233:0x0669), top: B:264:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00ba A[Catch: all -> 0x0680, TryCatch #18 {all -> 0x0680, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x0089, B:11:0x0091, B:16:0x00a0, B:25:0x00b2, B:28:0x00ba, B:32:0x00c3, B:39:0x00d1, B:42:0x00d9, B:46:0x00e2, B:53:0x00f7, B:56:0x00ff, B:60:0x010a, B:67:0x011a, B:69:0x0123, B:71:0x0131, B:74:0x013b, B:76:0x014b, B:78:0x0155, B:80:0x015f, B:81:0x016e, B:83:0x0176, B:85:0x0182, B:87:0x0192, B:92:0x019d, B:94:0x01a5, B:96:0x01b1, B:98:0x01bf, B:110:0x01e0, B:113:0x01ef, B:115:0x024b, B:119:0x0257, B:123:0x0267, B:124:0x0272, B:150:0x0347, B:155:0x0353, B:157:0x035b, B:158:0x0367, B:160:0x0371, B:163:0x037b, B:164:0x037f, B:165:0x039b, B:167:0x03b0, B:169:0x03b6, B:203:0x04b0, B:206:0x04b6, B:213:0x04c7, B:216:0x04cd, B:219:0x04e4, B:221:0x0503, B:223:0x052c, B:225:0x0545, B:226:0x0567, B:228:0x05f7, B:231:0x0619, B:232:0x0641, B:211:0x04c2, B:233:0x0669), top: B:264:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00d1 A[Catch: all -> 0x0680, TryCatch #18 {all -> 0x0680, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x0089, B:11:0x0091, B:16:0x00a0, B:25:0x00b2, B:28:0x00ba, B:32:0x00c3, B:39:0x00d1, B:42:0x00d9, B:46:0x00e2, B:53:0x00f7, B:56:0x00ff, B:60:0x010a, B:67:0x011a, B:69:0x0123, B:71:0x0131, B:74:0x013b, B:76:0x014b, B:78:0x0155, B:80:0x015f, B:81:0x016e, B:83:0x0176, B:85:0x0182, B:87:0x0192, B:92:0x019d, B:94:0x01a5, B:96:0x01b1, B:98:0x01bf, B:110:0x01e0, B:113:0x01ef, B:115:0x024b, B:119:0x0257, B:123:0x0267, B:124:0x0272, B:150:0x0347, B:155:0x0353, B:157:0x035b, B:158:0x0367, B:160:0x0371, B:163:0x037b, B:164:0x037f, B:165:0x039b, B:167:0x03b0, B:169:0x03b6, B:203:0x04b0, B:206:0x04b6, B:213:0x04c7, B:216:0x04cd, B:219:0x04e4, B:221:0x0503, B:223:0x052c, B:225:0x0545, B:226:0x0567, B:228:0x05f7, B:231:0x0619, B:232:0x0641, B:211:0x04c2, B:233:0x0669), top: B:264:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00d9 A[Catch: all -> 0x0680, TryCatch #18 {all -> 0x0680, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x0089, B:11:0x0091, B:16:0x00a0, B:25:0x00b2, B:28:0x00ba, B:32:0x00c3, B:39:0x00d1, B:42:0x00d9, B:46:0x00e2, B:53:0x00f7, B:56:0x00ff, B:60:0x010a, B:67:0x011a, B:69:0x0123, B:71:0x0131, B:74:0x013b, B:76:0x014b, B:78:0x0155, B:80:0x015f, B:81:0x016e, B:83:0x0176, B:85:0x0182, B:87:0x0192, B:92:0x019d, B:94:0x01a5, B:96:0x01b1, B:98:0x01bf, B:110:0x01e0, B:113:0x01ef, B:115:0x024b, B:119:0x0257, B:123:0x0267, B:124:0x0272, B:150:0x0347, B:155:0x0353, B:157:0x035b, B:158:0x0367, B:160:0x0371, B:163:0x037b, B:164:0x037f, B:165:0x039b, B:167:0x03b0, B:169:0x03b6, B:203:0x04b0, B:206:0x04b6, B:213:0x04c7, B:216:0x04cd, B:219:0x04e4, B:221:0x0503, B:223:0x052c, B:225:0x0545, B:226:0x0567, B:228:0x05f7, B:231:0x0619, B:232:0x0641, B:211:0x04c2, B:233:0x0669), top: B:264:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00dd  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00f7 A[Catch: all -> 0x0680, TryCatch #18 {all -> 0x0680, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x0089, B:11:0x0091, B:16:0x00a0, B:25:0x00b2, B:28:0x00ba, B:32:0x00c3, B:39:0x00d1, B:42:0x00d9, B:46:0x00e2, B:53:0x00f7, B:56:0x00ff, B:60:0x010a, B:67:0x011a, B:69:0x0123, B:71:0x0131, B:74:0x013b, B:76:0x014b, B:78:0x0155, B:80:0x015f, B:81:0x016e, B:83:0x0176, B:85:0x0182, B:87:0x0192, B:92:0x019d, B:94:0x01a5, B:96:0x01b1, B:98:0x01bf, B:110:0x01e0, B:113:0x01ef, B:115:0x024b, B:119:0x0257, B:123:0x0267, B:124:0x0272, B:150:0x0347, B:155:0x0353, B:157:0x035b, B:158:0x0367, B:160:0x0371, B:163:0x037b, B:164:0x037f, B:165:0x039b, B:167:0x03b0, B:169:0x03b6, B:203:0x04b0, B:206:0x04b6, B:213:0x04c7, B:216:0x04cd, B:219:0x04e4, B:221:0x0503, B:223:0x052c, B:225:0x0545, B:226:0x0567, B:228:0x05f7, B:231:0x0619, B:232:0x0641, B:211:0x04c2, B:233:0x0669), top: B:264:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00ff A[Catch: all -> 0x0680, TryCatch #18 {all -> 0x0680, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x0089, B:11:0x0091, B:16:0x00a0, B:25:0x00b2, B:28:0x00ba, B:32:0x00c3, B:39:0x00d1, B:42:0x00d9, B:46:0x00e2, B:53:0x00f7, B:56:0x00ff, B:60:0x010a, B:67:0x011a, B:69:0x0123, B:71:0x0131, B:74:0x013b, B:76:0x014b, B:78:0x0155, B:80:0x015f, B:81:0x016e, B:83:0x0176, B:85:0x0182, B:87:0x0192, B:92:0x019d, B:94:0x01a5, B:96:0x01b1, B:98:0x01bf, B:110:0x01e0, B:113:0x01ef, B:115:0x024b, B:119:0x0257, B:123:0x0267, B:124:0x0272, B:150:0x0347, B:155:0x0353, B:157:0x035b, B:158:0x0367, B:160:0x0371, B:163:0x037b, B:164:0x037f, B:165:0x039b, B:167:0x03b0, B:169:0x03b6, B:203:0x04b0, B:206:0x04b6, B:213:0x04c7, B:216:0x04cd, B:219:0x04e4, B:221:0x0503, B:223:0x052c, B:225:0x0545, B:226:0x0567, B:228:0x05f7, B:231:0x0619, B:232:0x0641, B:211:0x04c2, B:233:0x0669), top: B:264:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0108 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0118  */
    /* JADX WARN: Type inference failed for: r3v19 */
    /* JADX WARN: Type inference failed for: r3v20 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String createThemePreviewImage(String str, String str2, ThemeAccent themeAccent) {
        int i;
        int i2;
        int i3;
        int intValue;
        int i4;
        int i5;
        int intValue2;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        Paint paint;
        int i13;
        Canvas canvas;
        int i14;
        int i15;
        Exception e;
        String queryParameter;
        Drawable mutate;
        Drawable mutate2;
        int i16;
        int i17;
        Drawable drawable;
        Drawable drawable2;
        Throwable th;
        Canvas canvas2;
        boolean z;
        boolean z2;
        int i18;
        int i19;
        Bitmap decodeFile;
        int i20;
        Canvas canvas3;
        FileInputStream fileInputStream;
        Throwable th2;
        File file;
        int i21;
        Bitmap bitmap;
        int i22;
        Drawable drawable3;
        Canvas canvas4;
        int i23;
        int i24;
        Canvas canvas5 = themeAccent;
        try {
            String[] strArr = new String[1];
            final HashMap<String, Integer> themeFileValues = getThemeFileValues(new File(str), null, strArr);
            if (canvas5 != null) {
                checkIsDark(themeFileValues, canvas5.parentTheme);
            }
            Integer num = themeFileValues.get("wallpaperFileOffset");
            Bitmap createBitmap = Bitmaps.createBitmap(560, 678, Bitmap.Config.ARGB_8888);
            Canvas canvas6 = new Canvas(createBitmap);
            Paint paint2 = new Paint();
            int previewColor = getPreviewColor(themeFileValues, "actionBarDefault");
            int previewColor2 = getPreviewColor(themeFileValues, "actionBarDefaultIcon");
            int previewColor3 = getPreviewColor(themeFileValues, "chat_messagePanelBackground");
            int previewColor4 = getPreviewColor(themeFileValues, "chat_messagePanelIcons");
            int previewColor5 = getPreviewColor(themeFileValues, "chat_inBubble");
            int previewColor6 = getPreviewColor(themeFileValues, "chat_outBubble");
            themeFileValues.get("chat_outBubbleGradient");
            Integer num2 = themeFileValues.get("chat_wallpaper");
            Integer num3 = themeFileValues.get("chat_wallpaper_gradient_to");
            Integer num4 = themeFileValues.get("key_chat_wallpaper_gradient_to2");
            Integer num5 = themeFileValues.get("key_chat_wallpaper_gradient_to3");
            int intValue3 = num2 != null ? num2.intValue() : 0;
            if (canvas5 != null) {
                i = previewColor2;
                i2 = (int) canvas5.backgroundOverrideColor;
            } else {
                i = previewColor2;
                i2 = 0;
            }
            if (i2 != 0 || canvas5 == 0) {
                i24 = intValue3;
            } else {
                i24 = intValue3;
                if (canvas5.backgroundOverrideColor != 0) {
                    i3 = 0;
                    intValue = num3 == null ? num3.intValue() : 0;
                    i4 = canvas5 == null ? (int) canvas5.backgroundGradientOverrideColor1 : 0;
                    if (i4 == 0 || canvas5 == 0 || canvas5.backgroundGradientOverrideColor1 == 0) {
                        if (i4 != 0) {
                            intValue = i4;
                        }
                        i5 = intValue;
                    } else {
                        i5 = 0;
                    }
                    intValue2 = num4 == null ? num4.intValue() : 0;
                    i6 = canvas5 == null ? (int) canvas5.backgroundGradientOverrideColor2 : 0;
                    if (i6 == 0 || canvas5 == 0) {
                        i7 = intValue2;
                        i8 = i3;
                    } else {
                        i7 = intValue2;
                        i8 = i3;
                        if (canvas5.backgroundGradientOverrideColor2 != 0) {
                            i7 = 0;
                            int intValue4 = num5 != null ? num5.intValue() : 0;
                            if (canvas5 != null) {
                                i9 = intValue4;
                                i10 = (int) canvas5.backgroundGradientOverrideColor3;
                            } else {
                                i9 = intValue4;
                                i10 = 0;
                            }
                            if (i10 == 0 || canvas5 == 0) {
                                i11 = i8;
                                i23 = i9;
                            } else {
                                i11 = i8;
                                i23 = i9;
                                if (canvas5.backgroundGradientOverrideColor3 != 0) {
                                    i12 = 0;
                                    if (TextUtils.isEmpty(strArr[0])) {
                                        try {
                                            queryParameter = Uri.parse(strArr[0]).getQueryParameter("bg_color");
                                        } catch (Exception e2) {
                                            e = e2;
                                            i13 = i12;
                                        }
                                        if (canvas5 != null) {
                                            if (!TextUtils.isEmpty(queryParameter)) {
                                                i13 = i12;
                                                try {
                                                    i14 = Integer.parseInt(queryParameter.substring(0, 6), 16) | (-16777216);
                                                    paint = paint2;
                                                    try {
                                                        canvas5.backgroundOverrideColor = i14;
                                                        if (queryParameter.length() >= 13 && AndroidUtilities.isValidWallChar(queryParameter.charAt(6))) {
                                                            i5 = Integer.parseInt(queryParameter.substring(7, 13), 16) | (-16777216);
                                                            canvas5.backgroundGradientOverrideColor1 = i5;
                                                        }
                                                        if (queryParameter.length() < 20 || !AndroidUtilities.isValidWallChar(queryParameter.charAt(13))) {
                                                            canvas = canvas6;
                                                        } else {
                                                            int parseInt = Integer.parseInt(queryParameter.substring(14, 20), 16) | (-16777216);
                                                            canvas = canvas6;
                                                            try {
                                                                canvas5.backgroundGradientOverrideColor2 = parseInt;
                                                                i7 = parseInt;
                                                            } catch (Exception e3) {
                                                                e = e3;
                                                                i7 = parseInt;
                                                                FileLog.e(e);
                                                                i15 = i5;
                                                                int i25 = i7;
                                                                int i26 = i13;
                                                                mutate = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                                                int i27 = i;
                                                                setDrawableColor(mutate, i27);
                                                                mutate2 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                                                setDrawableColor(mutate2, i27);
                                                                Drawable mutate3 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                                                setDrawableColor(mutate3, previewColor4);
                                                                Drawable mutate4 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                                                setDrawableColor(mutate4, previewColor4);
                                                                MessageDrawable[] messageDrawableArr = new MessageDrawable[2];
                                                                i17 = 0;
                                                                for (i16 = 2; i17 < i16; i16 = 2) {
                                                                }
                                                                drawable = mutate4;
                                                                drawable2 = mutate3;
                                                                RectF rectF = new RectF();
                                                                int i28 = 80;
                                                                if (str2 != null) {
                                                                }
                                                                if (!z2) {
                                                                }
                                                                Paint paint3 = paint;
                                                                paint3.setColor(previewColor);
                                                                canvas3.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint3);
                                                                if (mutate != null) {
                                                                }
                                                                if (mutate2 != null) {
                                                                }
                                                                messageDrawableArr[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                                messageDrawableArr[1].setTop(0, 560, 522, false, false);
                                                                messageDrawableArr[1].draw(canvas3);
                                                                messageDrawableArr[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                                messageDrawableArr[1].setTop(430, 560, 522, false, false);
                                                                messageDrawableArr[1].draw(canvas3);
                                                                messageDrawableArr[0].setBounds(20, 323, 399, 415);
                                                                messageDrawableArr[0].setTop(323, 560, 522, false, false);
                                                                messageDrawableArr[0].draw(canvas3);
                                                                paint3.setColor(previewColor3);
                                                                canvas3.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint3);
                                                                if (drawable2 != null) {
                                                                }
                                                                if (drawable != null) {
                                                                }
                                                                canvas3.setBitmap(null);
                                                                File file2 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                createBitmap.compress(Bitmap.CompressFormat.JPEG, i18, new FileOutputStream(file2));
                                                                SharedConfig.saveConfig();
                                                                return file2.getAbsolutePath();
                                                            }
                                                        }
                                                        try {
                                                            if (queryParameter.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter.charAt(20))) {
                                                                int parseInt2 = (-16777216) | Integer.parseInt(queryParameter.substring(21), 16);
                                                                try {
                                                                    canvas5.backgroundGradientOverrideColor3 = parseInt2;
                                                                    i13 = parseInt2;
                                                                } catch (Exception e4) {
                                                                    e = e4;
                                                                    i13 = parseInt2;
                                                                    FileLog.e(e);
                                                                    i15 = i5;
                                                                    int i252 = i7;
                                                                    int i262 = i13;
                                                                    mutate = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                                                    int i272 = i;
                                                                    setDrawableColor(mutate, i272);
                                                                    mutate2 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                                                    setDrawableColor(mutate2, i272);
                                                                    Drawable mutate32 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                                                    setDrawableColor(mutate32, previewColor4);
                                                                    Drawable mutate42 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                                                    setDrawableColor(mutate42, previewColor4);
                                                                    MessageDrawable[] messageDrawableArr2 = new MessageDrawable[2];
                                                                    i17 = 0;
                                                                    while (i17 < i16) {
                                                                    }
                                                                    drawable = mutate42;
                                                                    drawable2 = mutate32;
                                                                    RectF rectF2 = new RectF();
                                                                    int i282 = 80;
                                                                    if (str2 != null) {
                                                                    }
                                                                    if (!z2) {
                                                                    }
                                                                    Paint paint32 = paint;
                                                                    paint32.setColor(previewColor);
                                                                    canvas3.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint32);
                                                                    if (mutate != null) {
                                                                    }
                                                                    if (mutate2 != null) {
                                                                    }
                                                                    messageDrawableArr2[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                                    messageDrawableArr2[1].setTop(0, 560, 522, false, false);
                                                                    messageDrawableArr2[1].draw(canvas3);
                                                                    messageDrawableArr2[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                                    messageDrawableArr2[1].setTop(430, 560, 522, false, false);
                                                                    messageDrawableArr2[1].draw(canvas3);
                                                                    messageDrawableArr2[0].setBounds(20, 323, 399, 415);
                                                                    messageDrawableArr2[0].setTop(323, 560, 522, false, false);
                                                                    messageDrawableArr2[0].draw(canvas3);
                                                                    paint32.setColor(previewColor3);
                                                                    canvas3.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint32);
                                                                    if (drawable2 != null) {
                                                                    }
                                                                    if (drawable != null) {
                                                                    }
                                                                    canvas3.setBitmap(null);
                                                                    File file22 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                    createBitmap.compress(Bitmap.CompressFormat.JPEG, i18, new FileOutputStream(file22));
                                                                    SharedConfig.saveConfig();
                                                                    return file22.getAbsolutePath();
                                                                }
                                                            }
                                                        } catch (Exception e5) {
                                                            e = e5;
                                                        }
                                                    } catch (Exception e6) {
                                                        e = e6;
                                                        canvas = canvas6;
                                                    }
                                                } catch (Exception e7) {
                                                    e = e7;
                                                    paint = paint2;
                                                    canvas = canvas6;
                                                    i14 = i11;
                                                    FileLog.e(e);
                                                    i15 = i5;
                                                    int i2522 = i7;
                                                    int i2622 = i13;
                                                    mutate = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                                    int i2722 = i;
                                                    setDrawableColor(mutate, i2722);
                                                    mutate2 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                                    setDrawableColor(mutate2, i2722);
                                                    Drawable mutate322 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                                    setDrawableColor(mutate322, previewColor4);
                                                    Drawable mutate422 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                                    setDrawableColor(mutate422, previewColor4);
                                                    MessageDrawable[] messageDrawableArr22 = new MessageDrawable[2];
                                                    i17 = 0;
                                                    while (i17 < i16) {
                                                    }
                                                    drawable = mutate422;
                                                    drawable2 = mutate322;
                                                    RectF rectF22 = new RectF();
                                                    int i2822 = 80;
                                                    if (str2 != null) {
                                                    }
                                                    if (!z2) {
                                                    }
                                                    Paint paint322 = paint;
                                                    paint322.setColor(previewColor);
                                                    canvas3.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint322);
                                                    if (mutate != null) {
                                                    }
                                                    if (mutate2 != null) {
                                                    }
                                                    messageDrawableArr22[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                    messageDrawableArr22[1].setTop(0, 560, 522, false, false);
                                                    messageDrawableArr22[1].draw(canvas3);
                                                    messageDrawableArr22[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                    messageDrawableArr22[1].setTop(430, 560, 522, false, false);
                                                    messageDrawableArr22[1].draw(canvas3);
                                                    messageDrawableArr22[0].setBounds(20, 323, 399, 415);
                                                    messageDrawableArr22[0].setTop(323, 560, 522, false, false);
                                                    messageDrawableArr22[0].draw(canvas3);
                                                    paint322.setColor(previewColor3);
                                                    canvas3.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint322);
                                                    if (drawable2 != null) {
                                                    }
                                                    if (drawable != null) {
                                                    }
                                                    canvas3.setBitmap(null);
                                                    File file222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                    createBitmap.compress(Bitmap.CompressFormat.JPEG, i18, new FileOutputStream(file222));
                                                    SharedConfig.saveConfig();
                                                    return file222.getAbsolutePath();
                                                }
                                                i15 = i5;
                                            }
                                        }
                                        i13 = i12;
                                        paint = paint2;
                                        canvas = canvas6;
                                        i14 = i11;
                                        i15 = i5;
                                    } else {
                                        i13 = i12;
                                        paint = paint2;
                                        canvas = canvas6;
                                        i15 = i5;
                                        i14 = i11;
                                    }
                                    int i25222 = i7;
                                    int i26222 = i13;
                                    mutate = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                    int i27222 = i;
                                    setDrawableColor(mutate, i27222);
                                    mutate2 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                    setDrawableColor(mutate2, i27222);
                                    Drawable mutate3222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                    setDrawableColor(mutate3222, previewColor4);
                                    Drawable mutate4222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                    setDrawableColor(mutate4222, previewColor4);
                                    MessageDrawable[] messageDrawableArr222 = new MessageDrawable[2];
                                    i17 = 0;
                                    while (i17 < i16) {
                                        Drawable drawable4 = mutate4222;
                                        Drawable drawable5 = mutate3222;
                                        messageDrawableArr222[i17] = new MessageDrawable(2, i17 == 1, false) { // from class: org.telegram.ui.ActionBar.Theme.10
                                            @Override // org.telegram.ui.ActionBar.Theme.MessageDrawable
                                            protected int getColor(String str3) {
                                                Integer num6 = (Integer) themeFileValues.get(str3);
                                                if (num6 == null) {
                                                    num6 = (Integer) Theme.defaultColors.get(str3);
                                                }
                                                return num6.intValue();
                                            }

                                            @Override // org.telegram.ui.ActionBar.Theme.MessageDrawable
                                            protected Integer getCurrentColor(String str3) {
                                                return (Integer) themeFileValues.get(str3);
                                            }
                                        };
                                        setDrawableColor(messageDrawableArr222[i17], i17 == 0 ? previewColor5 : previewColor6);
                                        i17++;
                                        mutate4222 = drawable4;
                                        mutate3222 = drawable5;
                                    }
                                    drawable = mutate4222;
                                    drawable2 = mutate3222;
                                    RectF rectF222 = new RectF();
                                    int i28222 = 80;
                                    if (str2 != null) {
                                        try {
                                            BitmapFactory.Options options = new BitmapFactory.Options();
                                            options.inJustDecodeBounds = true;
                                            BitmapFactory.decodeFile(str2, options);
                                            int i29 = options.outWidth;
                                            if (i29 > 0 && (i19 = options.outHeight) > 0) {
                                                float min = Math.min(i29 / 560.0f, i19 / 560.0f);
                                                options.inSampleSize = 1;
                                                if (min > 1.0f) {
                                                    do {
                                                        i20 = options.inSampleSize * 2;
                                                        options.inSampleSize = i20;
                                                    } while (i20 < min);
                                                    options.inJustDecodeBounds = false;
                                                    decodeFile = BitmapFactory.decodeFile(str2, options);
                                                    if (decodeFile != null) {
                                                        try {
                                                            if (i25222 != 0 && canvas5 != 0) {
                                                                MotionBackgroundDrawable motionBackgroundDrawable = new MotionBackgroundDrawable(i14, i15, i25222, i26222, true);
                                                                motionBackgroundDrawable.setPatternBitmap((int) (canvas5.patternIntensity * 100.0f), decodeFile);
                                                                motionBackgroundDrawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                                                                Canvas canvas7 = canvas;
                                                                motionBackgroundDrawable.draw(canvas7);
                                                                canvas5 = canvas7;
                                                            } else {
                                                                Canvas canvas8 = canvas;
                                                                Paint paint4 = new Paint();
                                                                paint4.setFilterBitmap(true);
                                                                float min2 = Math.min(decodeFile.getWidth() / 560.0f, decodeFile.getHeight() / 560.0f);
                                                                rectF222.set(0.0f, 0.0f, decodeFile.getWidth() / min2, decodeFile.getHeight() / min2);
                                                                rectF222.offset((createBitmap.getWidth() - rectF222.width()) / 2.0f, (createBitmap.getHeight() - rectF222.height()) / 2.0f);
                                                                canvas8.drawBitmap(decodeFile, (Rect) null, rectF222, paint4);
                                                                canvas5 = canvas8;
                                                            }
                                                            z = true;
                                                            canvas2 = canvas5;
                                                            z2 = z;
                                                            i18 = 80;
                                                            canvas4 = canvas2;
                                                            canvas3 = canvas4;
                                                        } catch (Throwable th3) {
                                                            th = th3;
                                                            FileLog.e(th);
                                                            i18 = 80;
                                                            z2 = false;
                                                            canvas3 = canvas5;
                                                            if (!z2) {
                                                            }
                                                            Paint paint3222 = paint;
                                                            paint3222.setColor(previewColor);
                                                            canvas3.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint3222);
                                                            if (mutate != null) {
                                                            }
                                                            if (mutate2 != null) {
                                                            }
                                                            messageDrawableArr222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                            messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                                            messageDrawableArr222[1].draw(canvas3);
                                                            messageDrawableArr222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                            messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                                            messageDrawableArr222[1].draw(canvas3);
                                                            messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                                            messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                                            messageDrawableArr222[0].draw(canvas3);
                                                            paint3222.setColor(previewColor3);
                                                            canvas3.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint3222);
                                                            if (drawable2 != null) {
                                                            }
                                                            if (drawable != null) {
                                                            }
                                                            canvas3.setBitmap(null);
                                                            File file2222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                            createBitmap.compress(Bitmap.CompressFormat.JPEG, i18, new FileOutputStream(file2222));
                                                            SharedConfig.saveConfig();
                                                            return file2222.getAbsolutePath();
                                                        }
                                                    }
                                                } else {
                                                    options.inJustDecodeBounds = false;
                                                    decodeFile = BitmapFactory.decodeFile(str2, options);
                                                    if (decodeFile != null) {
                                                    }
                                                }
                                            }
                                            canvas2 = canvas;
                                            z = false;
                                            z2 = z;
                                            i18 = 80;
                                            canvas4 = canvas2;
                                            canvas3 = canvas4;
                                        } catch (Throwable th4) {
                                            th = th4;
                                            canvas5 = canvas;
                                        }
                                    } else {
                                        canvas5 = canvas;
                                        if (i14 != 0) {
                                            if (i15 == 0) {
                                                drawable3 = new ColorDrawable(i14);
                                            } else if (i25222 != 0) {
                                                drawable3 = new MotionBackgroundDrawable(i14, i15, i25222, i26222, true);
                                            } else {
                                                Integer num6 = themeFileValues.get("chat_wallpaper_gradient_rotation");
                                                if (num6 == null) {
                                                    num6 = 45;
                                                }
                                                drawable3 = BackgroundGradientDrawable.createDitheredGradientBitmapDrawable(num6.intValue(), new int[]{i14, num4 == null ? 0 : num4.intValue()}, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                                                i28222 = 90;
                                            }
                                            drawable3.setBounds(0, 120, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                                            drawable3.draw(canvas5);
                                            i18 = i28222;
                                            z2 = true;
                                            canvas4 = canvas5;
                                            canvas3 = canvas4;
                                        } else {
                                            if ((num != null && num.intValue() >= 0) || !TextUtils.isEmpty(strArr[0])) {
                                                try {
                                                    BitmapFactory.Options options2 = new BitmapFactory.Options();
                                                    options2.inJustDecodeBounds = true;
                                                    if (!TextUtils.isEmpty(strArr[0])) {
                                                        try {
                                                            File file3 = new File(ApplicationLoader.getFilesDirFixed(), Utilities.MD5(strArr[0]) + ".wp");
                                                            BitmapFactory.decodeFile(file3.getAbsolutePath(), options2);
                                                            file = file3;
                                                            fileInputStream = null;
                                                        } catch (Throwable th5) {
                                                            th2 = th5;
                                                            fileInputStream = null;
                                                            FileLog.e(th2);
                                                            if (fileInputStream != null) {
                                                            }
                                                            i18 = 80;
                                                            z2 = false;
                                                            canvas3 = canvas5;
                                                            if (!z2) {
                                                            }
                                                            Paint paint32222 = paint;
                                                            paint32222.setColor(previewColor);
                                                            canvas3.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint32222);
                                                            if (mutate != null) {
                                                            }
                                                            if (mutate2 != null) {
                                                            }
                                                            messageDrawableArr222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                            messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                                            messageDrawableArr222[1].draw(canvas3);
                                                            messageDrawableArr222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                            messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                                            messageDrawableArr222[1].draw(canvas3);
                                                            messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                                            messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                                            messageDrawableArr222[0].draw(canvas3);
                                                            paint32222.setColor(previewColor3);
                                                            canvas3.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint32222);
                                                            if (drawable2 != null) {
                                                            }
                                                            if (drawable != null) {
                                                            }
                                                            canvas3.setBitmap(null);
                                                            File file22222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                            createBitmap.compress(Bitmap.CompressFormat.JPEG, i18, new FileOutputStream(file22222));
                                                            SharedConfig.saveConfig();
                                                            return file22222.getAbsolutePath();
                                                        }
                                                    } else {
                                                        fileInputStream = new FileInputStream(str);
                                                        try {
                                                            fileInputStream.getChannel().position(num.intValue());
                                                            BitmapFactory.decodeStream(fileInputStream, null, options2);
                                                            file = null;
                                                        } catch (Throwable th6) {
                                                            th2 = th6;
                                                            FileLog.e(th2);
                                                            if (fileInputStream != null) {
                                                            }
                                                            i18 = 80;
                                                            z2 = false;
                                                            canvas3 = canvas5;
                                                            if (!z2) {
                                                            }
                                                            Paint paint322222 = paint;
                                                            paint322222.setColor(previewColor);
                                                            canvas3.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint322222);
                                                            if (mutate != null) {
                                                            }
                                                            if (mutate2 != null) {
                                                            }
                                                            messageDrawableArr222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                            messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                                            messageDrawableArr222[1].draw(canvas3);
                                                            messageDrawableArr222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                            messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                                            messageDrawableArr222[1].draw(canvas3);
                                                            messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                                            messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                                            messageDrawableArr222[0].draw(canvas3);
                                                            paint322222.setColor(previewColor3);
                                                            canvas3.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint322222);
                                                            if (drawable2 != null) {
                                                            }
                                                            if (drawable != null) {
                                                            }
                                                            canvas3.setBitmap(null);
                                                            File file222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                            createBitmap.compress(Bitmap.CompressFormat.JPEG, i18, new FileOutputStream(file222222));
                                                            SharedConfig.saveConfig();
                                                            return file222222.getAbsolutePath();
                                                        }
                                                    }
                                                    int i30 = options2.outWidth;
                                                    if (i30 > 0 && (i21 = options2.outHeight) > 0) {
                                                        float min3 = Math.min(i30 / 560.0f, i21 / 560.0f);
                                                        options2.inSampleSize = 1;
                                                        if (min3 > 1.0f) {
                                                            do {
                                                                i22 = options2.inSampleSize * 2;
                                                                try {
                                                                    options2.inSampleSize = i22;
                                                                } catch (Throwable th7) {
                                                                    th2 = th7;
                                                                    FileLog.e(th2);
                                                                    if (fileInputStream != null) {
                                                                        try {
                                                                            fileInputStream.close();
                                                                        } catch (Exception e8) {
                                                                            FileLog.e(e8);
                                                                        }
                                                                    }
                                                                    i18 = 80;
                                                                    z2 = false;
                                                                    canvas3 = canvas5;
                                                                    if (!z2) {
                                                                    }
                                                                    Paint paint3222222 = paint;
                                                                    paint3222222.setColor(previewColor);
                                                                    canvas3.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint3222222);
                                                                    if (mutate != null) {
                                                                    }
                                                                    if (mutate2 != null) {
                                                                    }
                                                                    messageDrawableArr222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                                    messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                                                    messageDrawableArr222[1].draw(canvas3);
                                                                    messageDrawableArr222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                                    messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                                                    messageDrawableArr222[1].draw(canvas3);
                                                                    messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                                                    messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                                                    messageDrawableArr222[0].draw(canvas3);
                                                                    paint3222222.setColor(previewColor3);
                                                                    canvas3.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint3222222);
                                                                    if (drawable2 != null) {
                                                                    }
                                                                    if (drawable != null) {
                                                                    }
                                                                    canvas3.setBitmap(null);
                                                                    File file2222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                    createBitmap.compress(Bitmap.CompressFormat.JPEG, i18, new FileOutputStream(file2222222));
                                                                    SharedConfig.saveConfig();
                                                                    return file2222222.getAbsolutePath();
                                                                }
                                                            } while (i22 < min3);
                                                        }
                                                        options2.inJustDecodeBounds = false;
                                                        if (file != null) {
                                                            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options2);
                                                        } else {
                                                            fileInputStream.getChannel().position(num.intValue());
                                                            bitmap = BitmapFactory.decodeStream(fileInputStream, null, options2);
                                                        }
                                                        if (bitmap != null) {
                                                            Paint paint5 = new Paint();
                                                            paint5.setFilterBitmap(true);
                                                            float min4 = Math.min(bitmap.getWidth() / 560.0f, bitmap.getHeight() / 560.0f);
                                                            rectF222.set(0.0f, 0.0f, bitmap.getWidth() / min4, bitmap.getHeight() / min4);
                                                            rectF222.offset((createBitmap.getWidth() - rectF222.width()) / 2.0f, (createBitmap.getHeight() - rectF222.height()) / 2.0f);
                                                            canvas5.drawBitmap(bitmap, (Rect) null, rectF222, paint5);
                                                            z2 = true;
                                                            if (fileInputStream != null) {
                                                                try {
                                                                    fileInputStream.close();
                                                                } catch (Exception e9) {
                                                                    FileLog.e(e9);
                                                                }
                                                            }
                                                            i18 = 80;
                                                            canvas3 = canvas5;
                                                        }
                                                    }
                                                    z2 = false;
                                                    if (fileInputStream != null) {
                                                    }
                                                    i18 = 80;
                                                    canvas3 = canvas5;
                                                } catch (Throwable th8) {
                                                    th2 = th8;
                                                    fileInputStream = null;
                                                }
                                            }
                                            i18 = 80;
                                            z2 = false;
                                            canvas3 = canvas5;
                                        }
                                    }
                                    if (!z2) {
                                        Drawable createDefaultWallpaper = createDefaultWallpaper(createBitmap.getWidth(), createBitmap.getHeight() - 120);
                                        createDefaultWallpaper.setBounds(0, 120, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                                        createDefaultWallpaper.draw(canvas3);
                                    }
                                    Paint paint32222222 = paint;
                                    paint32222222.setColor(previewColor);
                                    canvas3.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint32222222);
                                    if (mutate != null) {
                                        int intrinsicHeight = (120 - mutate.getIntrinsicHeight()) / 2;
                                        mutate.setBounds(13, intrinsicHeight, mutate.getIntrinsicWidth() + 13, mutate.getIntrinsicHeight() + intrinsicHeight);
                                        mutate.draw(canvas3);
                                    }
                                    if (mutate2 != null) {
                                        int width = (createBitmap.getWidth() - mutate2.getIntrinsicWidth()) - 10;
                                        int intrinsicHeight2 = (120 - mutate2.getIntrinsicHeight()) / 2;
                                        mutate2.setBounds(width, intrinsicHeight2, mutate2.getIntrinsicWidth() + width, mutate2.getIntrinsicHeight() + intrinsicHeight2);
                                        mutate2.draw(canvas3);
                                    }
                                    messageDrawableArr222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                    messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                    messageDrawableArr222[1].draw(canvas3);
                                    messageDrawableArr222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                    messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                    messageDrawableArr222[1].draw(canvas3);
                                    messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                    messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                    messageDrawableArr222[0].draw(canvas3);
                                    paint32222222.setColor(previewColor3);
                                    canvas3.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint32222222);
                                    if (drawable2 != null) {
                                        int height = (createBitmap.getHeight() - 120) + ((120 - drawable2.getIntrinsicHeight()) / 2);
                                        drawable2.setBounds(22, height, drawable2.getIntrinsicWidth() + 22, drawable2.getIntrinsicHeight() + height);
                                        drawable2.draw(canvas3);
                                    }
                                    if (drawable != null) {
                                        int width2 = (createBitmap.getWidth() - drawable.getIntrinsicWidth()) - 22;
                                        int height2 = (createBitmap.getHeight() - 120) + ((120 - drawable.getIntrinsicHeight()) / 2);
                                        drawable.setBounds(width2, height2, drawable.getIntrinsicWidth() + width2, drawable.getIntrinsicHeight() + height2);
                                        drawable.draw(canvas3);
                                    }
                                    canvas3.setBitmap(null);
                                    File file22222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                    createBitmap.compress(Bitmap.CompressFormat.JPEG, i18, new FileOutputStream(file22222222));
                                    SharedConfig.saveConfig();
                                    return file22222222.getAbsolutePath();
                                }
                            }
                            if (i10 != 0) {
                                i23 = i10;
                            }
                            i12 = i23;
                            if (TextUtils.isEmpty(strArr[0])) {
                            }
                            int i252222 = i7;
                            int i262222 = i13;
                            mutate = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                            int i272222 = i;
                            setDrawableColor(mutate, i272222);
                            mutate2 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                            setDrawableColor(mutate2, i272222);
                            Drawable mutate32222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                            setDrawableColor(mutate32222, previewColor4);
                            Drawable mutate42222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                            setDrawableColor(mutate42222, previewColor4);
                            MessageDrawable[] messageDrawableArr2222 = new MessageDrawable[2];
                            i17 = 0;
                            while (i17 < i16) {
                            }
                            drawable = mutate42222;
                            drawable2 = mutate32222;
                            RectF rectF2222 = new RectF();
                            int i282222 = 80;
                            if (str2 != null) {
                            }
                            if (!z2) {
                            }
                            Paint paint322222222 = paint;
                            paint322222222.setColor(previewColor);
                            canvas3.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint322222222);
                            if (mutate != null) {
                            }
                            if (mutate2 != null) {
                            }
                            messageDrawableArr2222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                            messageDrawableArr2222[1].setTop(0, 560, 522, false, false);
                            messageDrawableArr2222[1].draw(canvas3);
                            messageDrawableArr2222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                            messageDrawableArr2222[1].setTop(430, 560, 522, false, false);
                            messageDrawableArr2222[1].draw(canvas3);
                            messageDrawableArr2222[0].setBounds(20, 323, 399, 415);
                            messageDrawableArr2222[0].setTop(323, 560, 522, false, false);
                            messageDrawableArr2222[0].draw(canvas3);
                            paint322222222.setColor(previewColor3);
                            canvas3.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint322222222);
                            if (drawable2 != null) {
                            }
                            if (drawable != null) {
                            }
                            canvas3.setBitmap(null);
                            File file222222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                            createBitmap.compress(Bitmap.CompressFormat.JPEG, i18, new FileOutputStream(file222222222));
                            SharedConfig.saveConfig();
                            return file222222222.getAbsolutePath();
                        }
                    }
                    if (i6 != 0) {
                        i7 = i6;
                    }
                    if (num5 != null) {
                    }
                    if (canvas5 != null) {
                    }
                    if (i10 == 0) {
                    }
                    i11 = i8;
                    i23 = i9;
                    if (i10 != 0) {
                    }
                    i12 = i23;
                    if (TextUtils.isEmpty(strArr[0])) {
                    }
                    int i2522222 = i7;
                    int i2622222 = i13;
                    mutate = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                    int i2722222 = i;
                    setDrawableColor(mutate, i2722222);
                    mutate2 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                    setDrawableColor(mutate2, i2722222);
                    Drawable mutate322222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                    setDrawableColor(mutate322222, previewColor4);
                    Drawable mutate422222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                    setDrawableColor(mutate422222, previewColor4);
                    MessageDrawable[] messageDrawableArr22222 = new MessageDrawable[2];
                    i17 = 0;
                    while (i17 < i16) {
                    }
                    drawable = mutate422222;
                    drawable2 = mutate322222;
                    RectF rectF22222 = new RectF();
                    int i2822222 = 80;
                    if (str2 != null) {
                    }
                    if (!z2) {
                    }
                    Paint paint3222222222 = paint;
                    paint3222222222.setColor(previewColor);
                    canvas3.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint3222222222);
                    if (mutate != null) {
                    }
                    if (mutate2 != null) {
                    }
                    messageDrawableArr22222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                    messageDrawableArr22222[1].setTop(0, 560, 522, false, false);
                    messageDrawableArr22222[1].draw(canvas3);
                    messageDrawableArr22222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                    messageDrawableArr22222[1].setTop(430, 560, 522, false, false);
                    messageDrawableArr22222[1].draw(canvas3);
                    messageDrawableArr22222[0].setBounds(20, 323, 399, 415);
                    messageDrawableArr22222[0].setTop(323, 560, 522, false, false);
                    messageDrawableArr22222[0].draw(canvas3);
                    paint3222222222.setColor(previewColor3);
                    canvas3.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint3222222222);
                    if (drawable2 != null) {
                    }
                    if (drawable != null) {
                    }
                    canvas3.setBitmap(null);
                    File file2222222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                    createBitmap.compress(Bitmap.CompressFormat.JPEG, i18, new FileOutputStream(file2222222222));
                    SharedConfig.saveConfig();
                    return file2222222222.getAbsolutePath();
                }
            }
            i3 = i2 != 0 ? i2 : i24;
            if (num3 == null) {
            }
            if (canvas5 == null) {
            }
            if (i4 == 0) {
            }
            if (i4 != 0) {
            }
            i5 = intValue;
            if (num4 == null) {
            }
            if (canvas5 == null) {
            }
            if (i6 == 0) {
            }
            i7 = intValue2;
            i8 = i3;
            if (i6 != 0) {
            }
            if (num5 != null) {
            }
            if (canvas5 != null) {
            }
            if (i10 == 0) {
            }
            i11 = i8;
            i23 = i9;
            if (i10 != 0) {
            }
            i12 = i23;
            if (TextUtils.isEmpty(strArr[0])) {
            }
            int i25222222 = i7;
            int i26222222 = i13;
            mutate = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
            int i27222222 = i;
            setDrawableColor(mutate, i27222222);
            mutate2 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
            setDrawableColor(mutate2, i27222222);
            Drawable mutate3222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
            setDrawableColor(mutate3222222, previewColor4);
            Drawable mutate4222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
            setDrawableColor(mutate4222222, previewColor4);
            MessageDrawable[] messageDrawableArr222222 = new MessageDrawable[2];
            i17 = 0;
            while (i17 < i16) {
            }
            drawable = mutate4222222;
            drawable2 = mutate3222222;
            RectF rectF222222 = new RectF();
            int i28222222 = 80;
            if (str2 != null) {
            }
            if (!z2) {
            }
            Paint paint32222222222 = paint;
            paint32222222222.setColor(previewColor);
            canvas3.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint32222222222);
            if (mutate != null) {
            }
            if (mutate2 != null) {
            }
            messageDrawableArr222222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
            messageDrawableArr222222[1].setTop(0, 560, 522, false, false);
            messageDrawableArr222222[1].draw(canvas3);
            messageDrawableArr222222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
            messageDrawableArr222222[1].setTop(430, 560, 522, false, false);
            messageDrawableArr222222[1].draw(canvas3);
            messageDrawableArr222222[0].setBounds(20, 323, 399, 415);
            messageDrawableArr222222[0].setTop(323, 560, 522, false, false);
            messageDrawableArr222222[0].draw(canvas3);
            paint32222222222.setColor(previewColor3);
            canvas3.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint32222222222);
            if (drawable2 != null) {
            }
            if (drawable != null) {
            }
            canvas3.setBitmap(null);
            File file22222222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
            createBitmap.compress(Bitmap.CompressFormat.JPEG, i18, new FileOutputStream(file22222222222));
            SharedConfig.saveConfig();
            return file22222222222.getAbsolutePath();
        } catch (Throwable th9) {
            FileLog.e(th9);
            return null;
        }
    }

    public static void checkIsDark(HashMap<String, Integer> hashMap, ThemeInfo themeInfo) {
        if (themeInfo == null || hashMap == null || themeInfo.isDark != -1) {
            return;
        }
        if (ColorUtils.calculateLuminance(ColorUtils.blendARGB(getPreviewColor(hashMap, "windowBackgroundWhite"), getPreviewColor(hashMap, "windowBackgroundWhite"), 0.5f)) < 0.5d) {
            themeInfo.isDark = 1;
        } else {
            themeInfo.isDark = 0;
        }
    }

    public static HashMap<String, Integer> getThemeFileValues(File file, String str, String[] strArr) {
        Throwable th;
        int i;
        HashMap<String, Integer> hashMap = new HashMap<>(500);
        FileInputStream fileInputStream = null;
        try {
            try {
                byte[] bArr = new byte[1024];
                FileInputStream fileInputStream2 = new FileInputStream(str != null ? getAssetFile(str) : file);
                int i2 = -1;
                int i3 = 0;
                int i4 = 0;
                int i5 = -1;
                boolean z = false;
                while (true) {
                    try {
                        int read = fileInputStream2.read(bArr);
                        if (read == i2) {
                            break;
                        }
                        int i6 = i4;
                        int i7 = 0;
                        int i8 = 0;
                        while (true) {
                            if (i7 >= read) {
                                break;
                            }
                            if (bArr[i7] == 10) {
                                int i9 = (i7 - i8) + 1;
                                String str2 = new String(bArr, i8, i9 - 1);
                                if (str2.startsWith("WLS=")) {
                                    if (strArr != null && strArr.length > 0) {
                                        strArr[i3] = str2.substring(4);
                                    }
                                } else if (str2.startsWith("WPS")) {
                                    i5 = i9 + i6;
                                    z = true;
                                    break;
                                } else {
                                    int indexOf = str2.indexOf(61);
                                    if (indexOf != i2) {
                                        String substring = str2.substring(i3, indexOf);
                                        String substring2 = str2.substring(indexOf + 1);
                                        if (substring2.length() > 0 && substring2.charAt(i3) == '#') {
                                            try {
                                                i = Color.parseColor(substring2);
                                            } catch (Exception unused) {
                                                i = Utilities.parseInt((CharSequence) substring2).intValue();
                                            }
                                        } else {
                                            i = Utilities.parseInt((CharSequence) substring2).intValue();
                                        }
                                        hashMap.put(substring, Integer.valueOf(i));
                                    }
                                }
                                i8 += i9;
                                i6 += i9;
                            }
                            i7++;
                            i2 = -1;
                            i3 = 0;
                        }
                        if (i4 == i6) {
                            break;
                        }
                        fileInputStream2.getChannel().position(i6);
                        if (z) {
                            break;
                        }
                        i4 = i6;
                        i2 = -1;
                        i3 = 0;
                    } catch (Throwable th2) {
                        th = th2;
                        fileInputStream = fileInputStream2;
                        try {
                            FileLog.e(th);
                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                            return hashMap;
                        } catch (Throwable th3) {
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                            }
                            throw th3;
                        }
                    }
                }
                hashMap.put("wallpaperFileOffset", Integer.valueOf(i5));
                fileInputStream2.close();
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        } catch (Throwable th4) {
            th = th4;
        }
        return hashMap;
    }

    public static void createCommonResources(Context context) {
        if (dividerPaint == null) {
            Paint paint = new Paint();
            dividerPaint = paint;
            paint.setStrokeWidth(1.0f);
            Paint paint2 = new Paint();
            dividerExtraPaint = paint2;
            paint2.setStrokeWidth(1.0f);
            avatar_backgroundPaint = new Paint(1);
            Paint paint3 = new Paint(1);
            checkboxSquare_checkPaint = paint3;
            paint3.setStyle(Paint.Style.STROKE);
            checkboxSquare_checkPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
            checkboxSquare_checkPaint.setStrokeCap(Paint.Cap.ROUND);
            Paint paint4 = new Paint(1);
            checkboxSquare_eraserPaint = paint4;
            paint4.setColor(0);
            checkboxSquare_eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            checkboxSquare_backgroundPaint = new Paint(1);
            Paint paint5 = new Paint();
            linkSelectionPaint = paint5;
            paint5.setPathEffect(LinkPath.getRoundedEffect());
            Resources resources = context.getResources();
            avatarDrawables[0] = resources.getDrawable(R.drawable.chats_saved);
            avatarDrawables[1] = resources.getDrawable(R.drawable.ghost);
            avatarDrawables[2] = resources.getDrawable(R.drawable.msg_folders_private);
            avatarDrawables[3] = resources.getDrawable(R.drawable.msg_folders_requests);
            avatarDrawables[4] = resources.getDrawable(R.drawable.msg_folders_groups);
            avatarDrawables[5] = resources.getDrawable(R.drawable.msg_folders_channels);
            avatarDrawables[6] = resources.getDrawable(R.drawable.msg_folders_bots);
            avatarDrawables[7] = resources.getDrawable(R.drawable.msg_folders_muted);
            avatarDrawables[8] = resources.getDrawable(R.drawable.msg_folders_read);
            avatarDrawables[9] = resources.getDrawable(R.drawable.msg_folders_archive);
            avatarDrawables[10] = resources.getDrawable(R.drawable.msg_folders_private);
            avatarDrawables[11] = resources.getDrawable(R.drawable.chats_replies);
            RLottieDrawable rLottieDrawable = dialogs_archiveAvatarDrawable;
            if (rLottieDrawable != null) {
                rLottieDrawable.setCallback(null);
                dialogs_archiveAvatarDrawable.recycle();
            }
            RLottieDrawable rLottieDrawable2 = dialogs_archiveDrawable;
            if (rLottieDrawable2 != null) {
                rLottieDrawable2.recycle();
            }
            RLottieDrawable rLottieDrawable3 = dialogs_unarchiveDrawable;
            if (rLottieDrawable3 != null) {
                rLottieDrawable3.recycle();
            }
            RLottieDrawable rLottieDrawable4 = dialogs_pinArchiveDrawable;
            if (rLottieDrawable4 != null) {
                rLottieDrawable4.recycle();
            }
            RLottieDrawable rLottieDrawable5 = dialogs_unpinArchiveDrawable;
            if (rLottieDrawable5 != null) {
                rLottieDrawable5.recycle();
            }
            RLottieDrawable rLottieDrawable6 = dialogs_hidePsaDrawable;
            if (rLottieDrawable6 != null) {
                rLottieDrawable6.recycle();
            }
            dialogs_archiveAvatarDrawable = new RLottieDrawable(R.raw.chats_archiveavatar, "chats_archiveavatar", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_archiveDrawable = new RLottieDrawable(R.raw.chats_archive, "chats_archive", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
            dialogs_unarchiveDrawable = new RLottieDrawable(R.raw.chats_unarchive, "chats_unarchive", AndroidUtilities.dp(AndroidUtilities.dp(36.0f)), AndroidUtilities.dp(36.0f));
            dialogs_pinArchiveDrawable = new RLottieDrawable(R.raw.chats_hide, "chats_hide", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
            dialogs_unpinArchiveDrawable = new RLottieDrawable(R.raw.chats_unhide, "chats_unhide", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
            dialogs_hidePsaDrawable = new RLottieDrawable(R.raw.chat_audio_record_delete, "chats_psahide", AndroidUtilities.dp(30.0f), AndroidUtilities.dp(30.0f));
            dialogs_swipeMuteDrawable = new RLottieDrawable(R.raw.swipe_mute, "swipe_mute", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
            dialogs_swipeUnmuteDrawable = new RLottieDrawable(R.raw.swipe_unmute, "swipe_unmute", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
            dialogs_swipeReadDrawable = new RLottieDrawable(R.raw.swipe_read, "swipe_read", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
            dialogs_swipeUnreadDrawable = new RLottieDrawable(R.raw.swipe_unread, "swipe_unread", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
            dialogs_swipeDeleteDrawable = new RLottieDrawable(R.raw.swipe_delete, "swipe_delete", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
            dialogs_swipeUnpinDrawable = new RLottieDrawable(R.raw.swipe_unpin, "swipe_unpin", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
            dialogs_swipePinDrawable = new RLottieDrawable(R.raw.swipe_pin, "swipe_pin", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f));
            applyCommonTheme();
        }
    }

    public static void applyCommonTheme() {
        Paint paint = dividerPaint;
        if (paint == null) {
            return;
        }
        paint.setColor(getColor("divider"));
        linkSelectionPaint.setColor(getColor("windowBackgroundWhiteLinkSelection"));
        int i = 0;
        while (true) {
            Drawable[] drawableArr = avatarDrawables;
            if (i < drawableArr.length) {
                setDrawableColorByKey(drawableArr[i], "avatar_text");
                i++;
            } else {
                dialogs_archiveAvatarDrawable.beginApplyLayerColors();
                dialogs_archiveAvatarDrawable.setLayerColor("Arrow1.**", getNonAnimatedColor("avatar_backgroundArchived"));
                dialogs_archiveAvatarDrawable.setLayerColor("Arrow2.**", getNonAnimatedColor("avatar_backgroundArchived"));
                dialogs_archiveAvatarDrawable.setLayerColor("Box2.**", getNonAnimatedColor("avatar_text"));
                dialogs_archiveAvatarDrawable.setLayerColor("Box1.**", getNonAnimatedColor("avatar_text"));
                dialogs_archiveAvatarDrawable.commitApplyLayerColors();
                dialogs_archiveAvatarDrawableRecolored = false;
                dialogs_archiveAvatarDrawable.setAllowDecodeSingleFrame(true);
                dialogs_pinArchiveDrawable.beginApplyLayerColors();
                dialogs_pinArchiveDrawable.setLayerColor("Arrow.**", getNonAnimatedColor("chats_archiveIcon"));
                dialogs_pinArchiveDrawable.setLayerColor("Line.**", getNonAnimatedColor("chats_archiveIcon"));
                dialogs_pinArchiveDrawable.commitApplyLayerColors();
                dialogs_unpinArchiveDrawable.beginApplyLayerColors();
                dialogs_unpinArchiveDrawable.setLayerColor("Arrow.**", getNonAnimatedColor("chats_archiveIcon"));
                dialogs_unpinArchiveDrawable.setLayerColor("Line.**", getNonAnimatedColor("chats_archiveIcon"));
                dialogs_unpinArchiveDrawable.commitApplyLayerColors();
                dialogs_hidePsaDrawable.beginApplyLayerColors();
                dialogs_hidePsaDrawable.setLayerColor("Line 1.**", getNonAnimatedColor("chats_archiveBackground"));
                dialogs_hidePsaDrawable.setLayerColor("Line 2.**", getNonAnimatedColor("chats_archiveBackground"));
                dialogs_hidePsaDrawable.setLayerColor("Line 3.**", getNonAnimatedColor("chats_archiveBackground"));
                dialogs_hidePsaDrawable.setLayerColor("Cup Red.**", getNonAnimatedColor("chats_archiveIcon"));
                dialogs_hidePsaDrawable.setLayerColor("Box.**", getNonAnimatedColor("chats_archiveIcon"));
                dialogs_hidePsaDrawable.commitApplyLayerColors();
                dialogs_hidePsaDrawableRecolored = false;
                dialogs_archiveDrawable.beginApplyLayerColors();
                dialogs_archiveDrawable.setLayerColor("Arrow.**", getNonAnimatedColor("chats_archiveBackground"));
                dialogs_archiveDrawable.setLayerColor("Box2.**", getNonAnimatedColor("chats_archiveIcon"));
                dialogs_archiveDrawable.setLayerColor("Box1.**", getNonAnimatedColor("chats_archiveIcon"));
                dialogs_archiveDrawable.commitApplyLayerColors();
                dialogs_archiveDrawableRecolored = false;
                dialogs_unarchiveDrawable.beginApplyLayerColors();
                dialogs_unarchiveDrawable.setLayerColor("Arrow1.**", getNonAnimatedColor("chats_archiveIcon"));
                dialogs_unarchiveDrawable.setLayerColor("Arrow2.**", getNonAnimatedColor("chats_archivePinBackground"));
                dialogs_unarchiveDrawable.setLayerColor("Box2.**", getNonAnimatedColor("chats_archiveIcon"));
                dialogs_unarchiveDrawable.setLayerColor("Box1.**", getNonAnimatedColor("chats_archiveIcon"));
                dialogs_unarchiveDrawable.commitApplyLayerColors();
                PremiumGradient.getInstance().checkIconColors();
                return;
            }
        }
    }

    public static void createCommonDialogResources(Context context) {
        if (dialogs_countTextPaint == null) {
            TextPaint textPaint = new TextPaint(1);
            dialogs_countTextPaint = textPaint;
            textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            dialogs_countPaint = new Paint(1);
            dialogs_reactionsCountPaint = new Paint(1);
            dialogs_onlineCirclePaint = new Paint(1);
        }
        dialogs_countTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
    }

    public static void createDialogsResources(Context context) {
        createCommonResources(context);
        createCommonDialogResources(context);
        if (dialogs_namePaint == null) {
            Resources resources = context.getResources();
            dialogs_namePaint = new TextPaint[2];
            dialogs_nameEncryptedPaint = new TextPaint[2];
            dialogs_messagePaint = new TextPaint[2];
            dialogs_messagePrintingPaint = new TextPaint[2];
            for (int i = 0; i < 2; i++) {
                dialogs_namePaint[i] = new TextPaint(1);
                dialogs_namePaint[i].setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                dialogs_nameEncryptedPaint[i] = new TextPaint(1);
                dialogs_nameEncryptedPaint[i].setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                dialogs_messagePaint[i] = new TextPaint(1);
                dialogs_messagePrintingPaint[i] = new TextPaint(1);
            }
            TextPaint textPaint = new TextPaint(1);
            dialogs_searchNamePaint = textPaint;
            textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            TextPaint textPaint2 = new TextPaint(1);
            dialogs_searchNameEncryptedPaint = textPaint2;
            textPaint2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            TextPaint textPaint3 = new TextPaint(1);
            dialogs_messageNamePaint = textPaint3;
            textPaint3.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            dialogs_timePaint = new TextPaint(1);
            TextPaint textPaint4 = new TextPaint(1);
            dialogs_archiveTextPaint = textPaint4;
            textPaint4.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            TextPaint textPaint5 = new TextPaint(1);
            dialogs_archiveTextPaintSmall = textPaint5;
            textPaint5.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            dialogs_onlinePaint = new TextPaint(1);
            dialogs_offlinePaint = new TextPaint(1);
            dialogs_tabletSeletedPaint = new Paint();
            dialogs_pinnedPaint = new Paint(1);
            dialogs_countGrayPaint = new Paint(1);
            dialogs_errorPaint = new Paint(1);
            dialogs_actionMessagePaint = new Paint(1);
            dialogs_lockDrawable = resources.getDrawable(R.drawable.list_secret);
            dialogs_checkDrawable = resources.getDrawable(R.drawable.list_check).mutate();
            dialogs_playDrawable = resources.getDrawable(R.drawable.minithumb_play).mutate();
            dialogs_checkReadDrawable = resources.getDrawable(R.drawable.list_check).mutate();
            dialogs_halfCheckDrawable = resources.getDrawable(R.drawable.list_halfcheck);
            dialogs_clockDrawable = new MsgClockDrawable();
            dialogs_errorDrawable = resources.getDrawable(R.drawable.list_warning_sign);
            dialogs_reorderDrawable = resources.getDrawable(R.drawable.list_reorder).mutate();
            dialogs_muteDrawable = resources.getDrawable(R.drawable.list_mute).mutate();
            dialogs_verifiedDrawable = resources.getDrawable(R.drawable.verified_area).mutate();
            dialogs_scamDrawable = new ScamDrawable(11, 0);
            dialogs_fakeDrawable = new ScamDrawable(11, 1);
            dialogs_verifiedCheckDrawable = resources.getDrawable(R.drawable.verified_check).mutate();
            dialogs_mentionDrawable = resources.getDrawable(R.drawable.mentionchatslist);
            dialogs_reactionsMentionDrawable = resources.getDrawable(R.drawable.reactionchatslist);
            dialogs_pinnedDrawable = resources.getDrawable(R.drawable.list_pin);
            moveUpDrawable = resources.getDrawable(R.drawable.preview_arrow);
            RectF rectF = new RectF();
            chat_updatePath[0] = new Path();
            chat_updatePath[2] = new Path();
            float dp = AndroidUtilities.dp(12.0f);
            float dp2 = AndroidUtilities.dp(12.0f);
            rectF.set(dp - AndroidUtilities.dp(5.0f), dp2 - AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f) + dp, AndroidUtilities.dp(5.0f) + dp2);
            chat_updatePath[2].arcTo(rectF, -160.0f, -110.0f, true);
            chat_updatePath[2].arcTo(rectF, 20.0f, -110.0f, true);
            chat_updatePath[0].moveTo(dp, AndroidUtilities.dp(8.0f) + dp2);
            chat_updatePath[0].lineTo(dp, AndroidUtilities.dp(2.0f) + dp2);
            chat_updatePath[0].lineTo(AndroidUtilities.dp(3.0f) + dp, AndroidUtilities.dp(5.0f) + dp2);
            chat_updatePath[0].close();
            chat_updatePath[0].moveTo(dp, dp2 - AndroidUtilities.dp(8.0f));
            chat_updatePath[0].lineTo(dp, dp2 - AndroidUtilities.dp(2.0f));
            chat_updatePath[0].lineTo(dp - AndroidUtilities.dp(3.0f), dp2 - AndroidUtilities.dp(5.0f));
            chat_updatePath[0].close();
            applyDialogsTheme();
        }
        dialogs_messageNamePaint.setTextSize(AndroidUtilities.dp(14.0f));
        dialogs_timePaint.setTextSize(AndroidUtilities.dp(13.0f));
        dialogs_archiveTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
        dialogs_archiveTextPaintSmall.setTextSize(AndroidUtilities.dp(11.0f));
        dialogs_onlinePaint.setTextSize(AndroidUtilities.dp(15.0f));
        dialogs_offlinePaint.setTextSize(AndroidUtilities.dp(15.0f));
        dialogs_searchNamePaint.setTextSize(AndroidUtilities.dp(16.0f));
        dialogs_searchNameEncryptedPaint.setTextSize(AndroidUtilities.dp(16.0f));
    }

    public static void applyDialogsTheme() {
        if (dialogs_namePaint == null) {
            return;
        }
        for (int i = 0; i < 2; i++) {
            dialogs_namePaint[i].setColor(getColor("chats_name"));
            dialogs_nameEncryptedPaint[i].setColor(getColor("chats_secretName"));
            TextPaint[] textPaintArr = dialogs_messagePaint;
            TextPaint textPaint = textPaintArr[i];
            TextPaint textPaint2 = textPaintArr[i];
            int color = getColor("chats_message");
            textPaint2.linkColor = color;
            textPaint.setColor(color);
            dialogs_messagePrintingPaint[i].setColor(getColor("chats_actionMessage"));
        }
        dialogs_searchNamePaint.setColor(getColor("chats_name"));
        dialogs_searchNameEncryptedPaint.setColor(getColor("chats_secretName"));
        TextPaint textPaint3 = dialogs_messageNamePaint;
        int color2 = getColor("chats_nameMessage_threeLines");
        textPaint3.linkColor = color2;
        textPaint3.setColor(color2);
        dialogs_tabletSeletedPaint.setColor(getColor("chats_tabletSelectedOverlay"));
        dialogs_pinnedPaint.setColor(getColor("chats_pinnedOverlay"));
        dialogs_timePaint.setColor(getColor("chats_date"));
        dialogs_countTextPaint.setColor(getColor("chats_unreadCounterText"));
        dialogs_archiveTextPaint.setColor(getColor("chats_archiveText"));
        dialogs_archiveTextPaintSmall.setColor(getColor("chats_archiveText"));
        dialogs_countPaint.setColor(getColor("chats_unreadCounter"));
        dialogs_reactionsCountPaint.setColor(getColor("dialogReactionMentionBackground"));
        dialogs_countGrayPaint.setColor(getColor("chats_unreadCounterMuted"));
        dialogs_actionMessagePaint.setColor(getColor("chats_actionMessage"));
        dialogs_errorPaint.setColor(getColor("chats_sentError"));
        dialogs_onlinePaint.setColor(getColor("windowBackgroundWhiteBlueText3"));
        dialogs_offlinePaint.setColor(getColor("windowBackgroundWhiteGrayText3"));
        setDrawableColorByKey(dialogs_lockDrawable, "chats_secretIcon");
        setDrawableColorByKey(dialogs_checkDrawable, "chats_sentCheck");
        setDrawableColorByKey(dialogs_checkReadDrawable, "chats_sentReadCheck");
        setDrawableColorByKey(dialogs_halfCheckDrawable, "chats_sentReadCheck");
        setDrawableColorByKey(dialogs_clockDrawable, "chats_sentClock");
        setDrawableColorByKey(dialogs_errorDrawable, "chats_sentErrorIcon");
        setDrawableColorByKey(dialogs_pinnedDrawable, "chats_pinnedIcon");
        setDrawableColorByKey(dialogs_reorderDrawable, "chats_pinnedIcon");
        setDrawableColorByKey(dialogs_muteDrawable, "chats_muteIcon");
        setDrawableColorByKey(dialogs_mentionDrawable, "chats_mentionIcon");
        setDrawableColorByKey(dialogs_reactionsMentionDrawable, "chats_mentionIcon");
        setDrawableColorByKey(dialogs_verifiedDrawable, "chats_verifiedBackground");
        setDrawableColorByKey(dialogs_verifiedCheckDrawable, "chats_verifiedCheck");
        setDrawableColorByKey(dialogs_holidayDrawable, "actionBarDefaultTitle");
        setDrawableColorByKey(dialogs_scamDrawable, "chats_draft");
        setDrawableColorByKey(dialogs_fakeDrawable, "chats_draft");
    }

    public static void reloadAllResources(Context context) {
        destroyResources();
        if (chat_msgInDrawable != null) {
            chat_msgInDrawable = null;
            createChatResources(context, false);
        }
        if (dialogs_namePaint != null) {
            dialogs_namePaint = null;
            createDialogsResources(context);
        }
        if (profile_verifiedDrawable != null) {
            profile_verifiedDrawable = null;
            createProfileResources(context);
        }
    }

    public static void createCommonMessageResources() {
        synchronized (sync) {
            if (chat_msgTextPaint == null) {
                chat_msgTextPaint = new TextPaint(1);
                chat_msgGameTextPaint = new TextPaint(1);
                chat_msgTextPaintOneEmoji = new TextPaint(1);
                chat_msgTextPaintTwoEmoji = new TextPaint(1);
                chat_msgTextPaintThreeEmoji = new TextPaint(1);
                TextPaint textPaint = new TextPaint(1);
                chat_msgBotButtonPaint = textPaint;
                textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            }
            chat_msgTextPaintOneEmoji.setTextSize(AndroidUtilities.dp(28.0f));
            chat_msgTextPaintTwoEmoji.setTextSize(AndroidUtilities.dp(24.0f));
            chat_msgTextPaintThreeEmoji.setTextSize(AndroidUtilities.dp(20.0f));
            chat_msgTextPaint.setTextSize(AndroidUtilities.dp(SharedConfig.fontSize));
            chat_msgGameTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
            chat_msgBotButtonPaint.setTextSize(AndroidUtilities.dp(15.0f));
        }
    }

    public static void createCommonChatResources() {
        createCommonMessageResources();
        if (chat_infoPaint == null) {
            chat_infoPaint = new TextPaint(1);
            TextPaint textPaint = new TextPaint(1);
            chat_stickerCommentCountPaint = textPaint;
            textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            TextPaint textPaint2 = new TextPaint(1);
            chat_docNamePaint = textPaint2;
            textPaint2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            chat_docBackPaint = new Paint(1);
            chat_deleteProgressPaint = new Paint(1);
            Paint paint = new Paint(1);
            chat_botProgressPaint = paint;
            paint.setStrokeCap(Paint.Cap.ROUND);
            chat_botProgressPaint.setStyle(Paint.Style.STROKE);
            TextPaint textPaint3 = new TextPaint(1);
            chat_locationTitlePaint = textPaint3;
            textPaint3.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            chat_locationAddressPaint = new TextPaint(1);
            Paint paint2 = new Paint();
            chat_urlPaint = paint2;
            paint2.setPathEffect(LinkPath.getRoundedEffect());
            Paint paint3 = new Paint();
            chat_outUrlPaint = paint3;
            paint3.setPathEffect(LinkPath.getRoundedEffect());
            Paint paint4 = new Paint();
            chat_textSearchSelectionPaint = paint4;
            paint4.setPathEffect(LinkPath.getRoundedEffect());
            Paint paint5 = new Paint(1);
            chat_radialProgressPaint = paint5;
            paint5.setStrokeCap(Paint.Cap.ROUND);
            chat_radialProgressPaint.setStyle(Paint.Style.STROKE);
            chat_radialProgressPaint.setColor(-1610612737);
            Paint paint6 = new Paint(1);
            chat_radialProgress2Paint = paint6;
            paint6.setStrokeCap(Paint.Cap.ROUND);
            chat_radialProgress2Paint.setStyle(Paint.Style.STROKE);
            chat_audioTimePaint = new TextPaint(1);
            TextPaint textPaint4 = new TextPaint(1);
            chat_livePaint = textPaint4;
            textPaint4.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            TextPaint textPaint5 = new TextPaint(1);
            chat_audioTitlePaint = textPaint5;
            textPaint5.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            chat_audioPerformerPaint = new TextPaint(1);
            TextPaint textPaint6 = new TextPaint(1);
            chat_botButtonPaint = textPaint6;
            textPaint6.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            TextPaint textPaint7 = new TextPaint(1);
            chat_contactNamePaint = textPaint7;
            textPaint7.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            chat_contactPhonePaint = new TextPaint(1);
            chat_durationPaint = new TextPaint(1);
            TextPaint textPaint8 = new TextPaint(1);
            chat_gamePaint = textPaint8;
            textPaint8.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            chat_shipmentPaint = new TextPaint(1);
            chat_timePaint = new TextPaint(1);
            chat_adminPaint = new TextPaint(1);
            TextPaint textPaint9 = new TextPaint(1);
            chat_namePaint = textPaint9;
            textPaint9.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            chat_forwardNamePaint = new TextPaint(1);
            TextPaint textPaint10 = new TextPaint(1);
            chat_replyNamePaint = textPaint10;
            textPaint10.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            chat_replyTextPaint = new TextPaint(1);
            TextPaint textPaint11 = new TextPaint(1);
            chat_instantViewPaint = textPaint11;
            textPaint11.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            Paint paint7 = new Paint(1);
            chat_instantViewRectPaint = paint7;
            paint7.setStyle(Paint.Style.STROKE);
            chat_instantViewRectPaint.setStrokeCap(Paint.Cap.ROUND);
            Paint paint8 = new Paint(1);
            chat_pollTimerPaint = paint8;
            paint8.setStyle(Paint.Style.STROKE);
            chat_pollTimerPaint.setStrokeCap(Paint.Cap.ROUND);
            chat_replyLinePaint = new Paint(1);
            chat_msgErrorPaint = new Paint(1);
            chat_statusPaint = new Paint(1);
            Paint paint9 = new Paint(1);
            chat_statusRecordPaint = paint9;
            paint9.setStyle(Paint.Style.STROKE);
            chat_statusRecordPaint.setStrokeCap(Paint.Cap.ROUND);
            TextPaint textPaint12 = new TextPaint(1);
            chat_actionTextPaint = textPaint12;
            textPaint12.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            Paint paint10 = new Paint(1);
            chat_actionBackgroundGradientDarkenPaint = paint10;
            paint10.setColor(704643072);
            chat_timeBackgroundPaint = new Paint(1);
            TextPaint textPaint13 = new TextPaint(1);
            chat_contextResult_titleTextPaint = textPaint13;
            textPaint13.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            chat_contextResult_descriptionTextPaint = new TextPaint(1);
            chat_composeBackgroundPaint = new Paint();
            new Paint(1);
            chat_radialProgressPausedSeekbarPaint = new Paint(1);
            chat_messageBackgroundSelectedPaint = new Paint(1);
            chat_actionBackgroundPaint = new Paint(1);
            chat_actionBackgroundSelectedPaint = new Paint(1);
            chat_actionBackgroundPaint2 = new Paint(1);
            new Paint(1);
            addChatPaint("paintChatMessageBackgroundSelected", chat_messageBackgroundSelectedPaint, "chat_selectedBackground");
            addChatPaint("paintChatActionBackground", chat_actionBackgroundPaint, "chat_serviceBackground");
            addChatPaint("paintChatActionBackgroundSelected", chat_actionBackgroundSelectedPaint, "chat_serviceBackgroundSelected");
            addChatPaint("paintChatActionText", chat_actionTextPaint, "chat_serviceText");
            addChatPaint("paintChatBotButton", chat_botButtonPaint, "chat_botButtonText");
            addChatPaint("paintChatComposeBackground", chat_composeBackgroundPaint, "chat_messagePanelBackground");
            addChatPaint("paintChatTimeBackground", chat_timeBackgroundPaint, "chat_mediaTimeBackground");
        }
    }

    public static void createChatResources(Context context, boolean z) {
        Paint paint;
        createCommonChatResources();
        if (!z && chat_msgInDrawable == null) {
            Resources resources = context.getResources();
            chat_msgNoSoundDrawable = resources.getDrawable(R.drawable.video_muted);
            chat_msgInDrawable = new MessageDrawable(0, false, false);
            chat_msgInSelectedDrawable = new MessageDrawable(0, false, true);
            chat_msgOutDrawable = new MessageDrawable(0, true, false);
            chat_msgOutSelectedDrawable = new MessageDrawable(0, true, true);
            chat_msgInMediaDrawable = new MessageDrawable(1, false, false);
            chat_msgInMediaSelectedDrawable = new MessageDrawable(1, false, true);
            chat_msgOutMediaDrawable = new MessageDrawable(1, true, false);
            chat_msgOutMediaSelectedDrawable = new MessageDrawable(1, true, true);
            PathAnimator pathAnimator = new PathAnimator(0.293f, -26.0f, -28.0f, 1.0f);
            playPauseAnimator = pathAnimator;
            pathAnimator.addSvgKeyFrame("M 34.141 16.042 C 37.384 17.921 40.886 20.001 44.211 21.965 C 46.139 23.104 49.285 24.729 49.586 25.917 C 50.289 28.687 48.484 30 46.274 30 L 6 30.021 C 3.79 30.021 2.075 30.023 2 26.021 L 2.009 3.417 C 2.009 0.417 5.326 -0.58 7.068 0.417 C 10.545 2.406 25.024 10.761 34.141 16.042 Z", 166.0f);
            playPauseAnimator.addSvgKeyFrame("M 37.843 17.769 C 41.143 19.508 44.131 21.164 47.429 23.117 C 48.542 23.775 49.623 24.561 49.761 25.993 C 50.074 28.708 48.557 30 46.347 30 L 6 30.012 C 3.79 30.012 2 28.222 2 26.012 L 2.009 4.609 C 2.009 1.626 5.276 0.664 7.074 1.541 C 10.608 3.309 28.488 12.842 37.843 17.769 Z", 200.0f);
            playPauseAnimator.addSvgKeyFrame("M 40.644 18.756 C 43.986 20.389 49.867 23.108 49.884 25.534 C 49.897 27.154 49.88 24.441 49.894 26.059 C 49.911 28.733 48.6 30 46.39 30 L 6 30.013 C 3.79 30.013 2 28.223 2 26.013 L 2.008 5.52 C 2.008 2.55 5.237 1.614 7.079 2.401 C 10.656 4 31.106 14.097 40.644 18.756 Z", 217.0f);
            playPauseAnimator.addSvgKeyFrame("M 43.782 19.218 C 47.117 20.675 50.075 21.538 50.041 24.796 C 50.022 26.606 50.038 24.309 50.039 26.104 C 50.038 28.736 48.663 30 46.453 30 L 6 29.986 C 3.79 29.986 2 28.196 2 25.986 L 2.008 6.491 C 2.008 3.535 5.196 2.627 7.085 3.316 C 10.708 4.731 33.992 14.944 43.782 19.218 Z", 234.0f);
            playPauseAnimator.addSvgKeyFrame("M 47.421 16.941 C 50.544 18.191 50.783 19.91 50.769 22.706 C 50.761 24.484 50.76 23.953 50.79 26.073 C 50.814 27.835 49.334 30 47.124 30 L 5 30.01 C 2.79 30.01 1 28.22 1 26.01 L 1.001 10.823 C 1.001 8.218 3.532 6.895 5.572 7.26 C 7.493 8.01 47.421 16.941 47.421 16.941 Z", 267.0f);
            playPauseAnimator.addSvgKeyFrame("M 47.641 17.125 C 50.641 18.207 51.09 19.935 51.078 22.653 C 51.07 24.191 51.062 21.23 51.088 23.063 C 51.109 24.886 49.587 27 47.377 27 L 5 27.009 C 2.79 27.009 1 25.219 1 23.009 L 0.983 11.459 C 0.983 8.908 3.414 7.522 5.476 7.838 C 7.138 8.486 47.641 17.125 47.641 17.125 Z", 300.0f);
            playPauseAnimator.addSvgKeyFrame("M 48 7 C 50.21 7 52 8.79 52 11 C 52 19 52 19 52 19 C 52 21.21 50.21 23 48 23 L 4 23 C 1.79 23 0 21.21 0 19 L 0 11 C 0 8.79 1.79 7 4 7 C 48 7 48 7 48 7 Z", 383.0f);
            chat_msgOutCheckDrawable = resources.getDrawable(R.drawable.msg_check_s).mutate();
            chat_msgOutCheckSelectedDrawable = resources.getDrawable(R.drawable.msg_check_s).mutate();
            chat_msgOutCheckReadDrawable = resources.getDrawable(R.drawable.msg_check_s).mutate();
            chat_msgOutCheckReadSelectedDrawable = resources.getDrawable(R.drawable.msg_check_s).mutate();
            chat_msgMediaCheckDrawable = resources.getDrawable(R.drawable.msg_check_s).mutate();
            chat_msgStickerCheckDrawable = resources.getDrawable(R.drawable.msg_check_s).mutate();
            chat_msgOutHalfCheckDrawable = resources.getDrawable(R.drawable.msg_halfcheck).mutate();
            chat_msgOutHalfCheckSelectedDrawable = resources.getDrawable(R.drawable.msg_halfcheck).mutate();
            chat_msgMediaHalfCheckDrawable = resources.getDrawable(R.drawable.msg_halfcheck_s).mutate();
            chat_msgStickerHalfCheckDrawable = resources.getDrawable(R.drawable.msg_halfcheck_s).mutate();
            chat_msgClockDrawable = new MsgClockDrawable();
            chat_msgInViewsDrawable = resources.getDrawable(R.drawable.msg_views).mutate();
            chat_msgInViewsSelectedDrawable = resources.getDrawable(R.drawable.msg_views).mutate();
            chat_msgOutViewsDrawable = resources.getDrawable(R.drawable.msg_views).mutate();
            chat_msgOutViewsSelectedDrawable = resources.getDrawable(R.drawable.msg_views).mutate();
            chat_msgInRepliesDrawable = resources.getDrawable(R.drawable.msg_reply_small).mutate();
            chat_msgInRepliesSelectedDrawable = resources.getDrawable(R.drawable.msg_reply_small).mutate();
            chat_msgOutRepliesDrawable = resources.getDrawable(R.drawable.msg_reply_small).mutate();
            chat_msgOutRepliesSelectedDrawable = resources.getDrawable(R.drawable.msg_reply_small).mutate();
            chat_msgInPinnedDrawable = resources.getDrawable(R.drawable.msg_pin_mini).mutate();
            chat_msgInPinnedSelectedDrawable = resources.getDrawable(R.drawable.msg_pin_mini).mutate();
            chat_msgOutPinnedDrawable = resources.getDrawable(R.drawable.msg_pin_mini).mutate();
            chat_msgOutPinnedSelectedDrawable = resources.getDrawable(R.drawable.msg_pin_mini).mutate();
            chat_msgMediaPinnedDrawable = resources.getDrawable(R.drawable.msg_pin_mini).mutate();
            chat_msgStickerPinnedDrawable = resources.getDrawable(R.drawable.msg_pin_mini).mutate();
            chat_msgMediaViewsDrawable = resources.getDrawable(R.drawable.msg_views).mutate();
            chat_msgMediaRepliesDrawable = resources.getDrawable(R.drawable.msg_reply_small).mutate();
            chat_msgStickerViewsDrawable = resources.getDrawable(R.drawable.msg_views).mutate();
            chat_msgStickerRepliesDrawable = resources.getDrawable(R.drawable.msg_reply_small).mutate();
            chat_msgInMenuDrawable = resources.getDrawable(R.drawable.msg_actions).mutate();
            chat_msgInMenuSelectedDrawable = resources.getDrawable(R.drawable.msg_actions).mutate();
            chat_msgOutMenuDrawable = resources.getDrawable(R.drawable.msg_actions).mutate();
            chat_msgOutMenuSelectedDrawable = resources.getDrawable(R.drawable.msg_actions).mutate();
            chat_msgMediaMenuDrawable = resources.getDrawable(R.drawable.video_actions);
            chat_msgInInstantDrawable = resources.getDrawable(R.drawable.msg_instant).mutate();
            chat_msgOutInstantDrawable = resources.getDrawable(R.drawable.msg_instant).mutate();
            chat_msgErrorDrawable = resources.getDrawable(R.drawable.msg_warning);
            chat_muteIconDrawable = resources.getDrawable(R.drawable.list_mute).mutate();
            chat_lockIconDrawable = resources.getDrawable(R.drawable.ic_lock_header);
            chat_msgInCallDrawable[0] = resources.getDrawable(R.drawable.chat_calls_voice).mutate();
            chat_msgInCallSelectedDrawable[0] = resources.getDrawable(R.drawable.chat_calls_voice).mutate();
            chat_msgOutCallDrawable[0] = resources.getDrawable(R.drawable.chat_calls_voice).mutate();
            chat_msgOutCallSelectedDrawable[0] = resources.getDrawable(R.drawable.chat_calls_voice).mutate();
            chat_msgInCallDrawable[1] = resources.getDrawable(R.drawable.chat_calls_video).mutate();
            chat_msgInCallSelectedDrawable[1] = resources.getDrawable(R.drawable.chat_calls_video).mutate();
            chat_msgOutCallDrawable[1] = resources.getDrawable(R.drawable.chat_calls_video).mutate();
            chat_msgOutCallSelectedDrawable[1] = resources.getDrawable(R.drawable.chat_calls_video).mutate();
            chat_msgCallUpGreenDrawable = resources.getDrawable(R.drawable.chat_calls_outgoing).mutate();
            chat_msgCallDownRedDrawable = resources.getDrawable(R.drawable.chat_calls_incoming).mutate();
            chat_msgCallDownGreenDrawable = resources.getDrawable(R.drawable.chat_calls_incoming).mutate();
            for (int i = 0; i < 2; i++) {
                chat_pollCheckDrawable[i] = resources.getDrawable(R.drawable.poll_right).mutate();
                chat_pollCrossDrawable[i] = resources.getDrawable(R.drawable.poll_wrong).mutate();
                chat_pollHintDrawable[i] = resources.getDrawable(R.drawable.msg_emoji_objects).mutate();
                chat_psaHelpDrawable[i] = resources.getDrawable(R.drawable.msg_psa).mutate();
            }
            calllog_msgCallUpRedDrawable = resources.getDrawable(R.drawable.ic_call_made_green_18dp).mutate();
            calllog_msgCallUpGreenDrawable = resources.getDrawable(R.drawable.ic_call_made_green_18dp).mutate();
            calllog_msgCallDownRedDrawable = resources.getDrawable(R.drawable.ic_call_received_green_18dp).mutate();
            calllog_msgCallDownGreenDrawable = resources.getDrawable(R.drawable.ic_call_received_green_18dp).mutate();
            chat_msgAvatarLiveLocationDrawable = resources.getDrawable(R.drawable.livepin).mutate();
            chat_inlineResultFile = resources.getDrawable(R.drawable.bot_file);
            chat_inlineResultAudio = resources.getDrawable(R.drawable.bot_music);
            chat_inlineResultLocation = resources.getDrawable(R.drawable.bot_location);
            chat_redLocationIcon = resources.getDrawable(R.drawable.map_pin).mutate();
            chat_botLinkDrawable = resources.getDrawable(R.drawable.bot_link);
            chat_botInlineDrawable = resources.getDrawable(R.drawable.bot_lines);
            chat_botCardDrawable = resources.getDrawable(R.drawable.bot_card);
            chat_botWebViewDrawable = resources.getDrawable(R.drawable.bot_webview);
            chat_botInviteDrawable = resources.getDrawable(R.drawable.bot_invite);
            chat_commentDrawable = resources.getDrawable(R.drawable.msg_msgbubble);
            chat_commentStickerDrawable = resources.getDrawable(R.drawable.msg_msgbubble2);
            chat_commentArrowDrawable = resources.getDrawable(R.drawable.msg_arrowright);
            chat_contextResult_shadowUnderSwitchDrawable = resources.getDrawable(R.drawable.header_shadow).mutate();
            chat_attachButtonDrawables[0] = new RLottieDrawable(R.raw.attach_gallery, "attach_gallery", AndroidUtilities.dp(26.0f), AndroidUtilities.dp(26.0f));
            chat_attachButtonDrawables[1] = new RLottieDrawable(R.raw.attach_music, "attach_music", AndroidUtilities.dp(26.0f), AndroidUtilities.dp(26.0f));
            chat_attachButtonDrawables[2] = new RLottieDrawable(R.raw.attach_file, "attach_file", AndroidUtilities.dp(26.0f), AndroidUtilities.dp(26.0f));
            chat_attachButtonDrawables[3] = new RLottieDrawable(R.raw.attach_contact, "attach_contact", AndroidUtilities.dp(26.0f), AndroidUtilities.dp(26.0f));
            chat_attachButtonDrawables[4] = new RLottieDrawable(R.raw.attach_location, "attach_location", AndroidUtilities.dp(26.0f), AndroidUtilities.dp(26.0f));
            chat_attachButtonDrawables[5] = new RLottieDrawable(R.raw.attach_poll, "attach_poll", AndroidUtilities.dp(26.0f), AndroidUtilities.dp(26.0f));
            chat_attachEmptyDrawable = resources.getDrawable(R.drawable.nophotos3);
            chat_shareIconDrawable = resources.getDrawable(R.drawable.share_arrow).mutate();
            chat_replyIconDrawable = resources.getDrawable(R.drawable.fast_reply);
            chat_goIconDrawable = resources.getDrawable(R.drawable.message_arrow);
            int dp = AndroidUtilities.dp(2.0f);
            RectF rectF = new RectF();
            chat_filePath[0] = new Path();
            chat_filePath[0].moveTo(AndroidUtilities.dp(7.0f), AndroidUtilities.dp(3.0f));
            chat_filePath[0].lineTo(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(3.0f));
            chat_filePath[0].lineTo(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(10.0f));
            chat_filePath[0].lineTo(AndroidUtilities.dp(21.0f), AndroidUtilities.dp(20.0f));
            int i2 = dp * 2;
            rectF.set(AndroidUtilities.dp(21.0f) - i2, AndroidUtilities.dp(19.0f) - dp, AndroidUtilities.dp(21.0f), AndroidUtilities.dp(19.0f) + dp);
            chat_filePath[0].arcTo(rectF, 0.0f, 90.0f, false);
            chat_filePath[0].lineTo(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(21.0f));
            rectF.set(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(19.0f) - dp, AndroidUtilities.dp(5.0f) + i2, AndroidUtilities.dp(19.0f) + dp);
            chat_filePath[0].arcTo(rectF, 90.0f, 90.0f, false);
            chat_filePath[0].lineTo(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(4.0f));
            rectF.set(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(3.0f), AndroidUtilities.dp(5.0f) + i2, AndroidUtilities.dp(3.0f) + i2);
            chat_filePath[0].arcTo(rectF, 180.0f, 90.0f, false);
            chat_filePath[0].close();
            chat_filePath[1] = new Path();
            chat_filePath[1].moveTo(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(5.0f));
            chat_filePath[1].lineTo(AndroidUtilities.dp(19.0f), AndroidUtilities.dp(10.0f));
            chat_filePath[1].lineTo(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(10.0f));
            chat_filePath[1].close();
            chat_flameIcon = resources.getDrawable(R.drawable.burn).mutate();
            chat_gifIcon = resources.getDrawable(R.drawable.msg_round_gif_m).mutate();
            chat_fileStatesDrawable[0][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_play_m);
            chat_fileStatesDrawable[0][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_play_m);
            chat_fileStatesDrawable[1][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_pause_m);
            chat_fileStatesDrawable[1][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_pause_m);
            chat_fileStatesDrawable[2][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_load_m);
            chat_fileStatesDrawable[2][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_load_m);
            chat_fileStatesDrawable[3][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_file_s);
            chat_fileStatesDrawable[3][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_file_s);
            chat_fileStatesDrawable[4][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_cancel_m);
            chat_fileStatesDrawable[4][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_cancel_m);
            chat_fileStatesDrawable[5][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_play_m);
            chat_fileStatesDrawable[5][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_play_m);
            chat_fileStatesDrawable[6][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_pause_m);
            chat_fileStatesDrawable[6][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_pause_m);
            chat_fileStatesDrawable[7][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_load_m);
            chat_fileStatesDrawable[7][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_load_m);
            chat_fileStatesDrawable[8][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_file_s);
            chat_fileStatesDrawable[8][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_file_s);
            chat_fileStatesDrawable[9][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_cancel_m);
            chat_fileStatesDrawable[9][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_round_cancel_m);
            chat_photoStatesDrawables[0][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_load_m);
            chat_photoStatesDrawables[0][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_load_m);
            chat_photoStatesDrawables[1][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_cancel_m);
            chat_photoStatesDrawables[1][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_cancel_m);
            chat_photoStatesDrawables[2][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_gif_m);
            chat_photoStatesDrawables[2][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_gif_m);
            chat_photoStatesDrawables[3][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_play_m);
            chat_photoStatesDrawables[3][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_play_m);
            Drawable[][] drawableArr = chat_photoStatesDrawables;
            Drawable[] drawableArr2 = drawableArr[4];
            Drawable[] drawableArr3 = drawableArr[4];
            Drawable drawable = resources.getDrawable(R.drawable.burn);
            drawableArr3[1] = drawable;
            drawableArr2[0] = drawable;
            Drawable[][] drawableArr4 = chat_photoStatesDrawables;
            Drawable[] drawableArr5 = drawableArr4[5];
            Drawable[] drawableArr6 = drawableArr4[5];
            Drawable drawable2 = resources.getDrawable(R.drawable.circle);
            drawableArr6[1] = drawable2;
            drawableArr5[0] = drawable2;
            Drawable[][] drawableArr7 = chat_photoStatesDrawables;
            Drawable[] drawableArr8 = drawableArr7[6];
            Drawable[] drawableArr9 = drawableArr7[6];
            Drawable drawable3 = resources.getDrawable(R.drawable.photocheck);
            drawableArr9[1] = drawable3;
            drawableArr8[0] = drawable3;
            chat_photoStatesDrawables[7][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_load_m);
            chat_photoStatesDrawables[7][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_load_m);
            chat_photoStatesDrawables[8][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_cancel_m);
            chat_photoStatesDrawables[8][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_cancel_m);
            chat_photoStatesDrawables[10][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_load_m);
            chat_photoStatesDrawables[10][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_load_m);
            chat_photoStatesDrawables[11][0] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_cancel_m);
            chat_photoStatesDrawables[11][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(48.0f), R.drawable.msg_round_cancel_m);
            chat_contactDrawable[0] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_contact);
            chat_contactDrawable[1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), R.drawable.msg_contact);
            chat_locationDrawable[0] = resources.getDrawable(R.drawable.msg_location).mutate();
            chat_locationDrawable[1] = resources.getDrawable(R.drawable.msg_location).mutate();
            chat_composeShadowDrawable = context.getResources().getDrawable(R.drawable.compose_panel_shadow).mutate();
            chat_composeShadowRoundDrawable = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
            try {
                int dp2 = AndroidUtilities.roundMessageSize + AndroidUtilities.dp(6.0f);
                Bitmap createBitmap = Bitmap.createBitmap(dp2, dp2, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                Paint paint2 = new Paint(1);
                paint2.setColor(0);
                paint2.setStyle(Paint.Style.FILL);
                paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                Paint paint3 = new Paint(1);
                paint3.setShadowLayer(AndroidUtilities.dp(4.0f), 0.0f, 0.0f, 1593835520);
                int i3 = 0;
                while (i3 < 2) {
                    canvas.drawCircle(dp2 / 2, dp2 / 2, (AndroidUtilities.roundMessageSize / 2) - AndroidUtilities.dp(1.0f), i3 == 0 ? paint3 : paint2);
                    i3++;
                }
                try {
                    canvas.setBitmap(null);
                } catch (Exception unused) {
                }
                chat_roundVideoShadow = new BitmapDrawable(createBitmap);
            } catch (Throwable unused2) {
            }
            defaultChatDrawables.clear();
            defaultChatDrawableColorKeys.clear();
            addChatDrawable("drawableBotInline", chat_botInlineDrawable, "chat_serviceIcon");
            addChatDrawable("drawableBotWebView", chat_botWebViewDrawable, "chat_serviceIcon");
            addChatDrawable("drawableBotLink", chat_botLinkDrawable, "chat_serviceIcon");
            addChatDrawable("drawable_botInvite", chat_botInviteDrawable, "chat_serviceIcon");
            addChatDrawable("drawableGoIcon", chat_goIconDrawable, "chat_serviceIcon");
            addChatDrawable("drawableCommentSticker", chat_commentStickerDrawable, "chat_serviceIcon");
            addChatDrawable("drawableMsgError", chat_msgErrorDrawable, "chat_sentErrorIcon");
            addChatDrawable("drawableMsgIn", chat_msgInDrawable, null);
            addChatDrawable("drawableMsgInSelected", chat_msgInSelectedDrawable, null);
            addChatDrawable("drawableMsgInMedia", chat_msgInMediaDrawable, null);
            addChatDrawable("drawableMsgInMediaSelected", chat_msgInMediaSelectedDrawable, null);
            addChatDrawable("drawableMsgOut", chat_msgOutDrawable, null);
            addChatDrawable("drawableMsgOutSelected", chat_msgOutSelectedDrawable, null);
            addChatDrawable("drawableMsgOutMedia", chat_msgOutMediaDrawable, null);
            addChatDrawable("drawableMsgOutMediaSelected", chat_msgOutMediaSelectedDrawable, null);
            addChatDrawable("drawableMsgOutCallAudio", chat_msgOutCallDrawable[0], "chat_outInstant");
            addChatDrawable("drawableMsgOutCallAudioSelected", chat_msgOutCallSelectedDrawable[0], "chat_outInstantSelected");
            addChatDrawable("drawableMsgOutCallVideo", chat_msgOutCallDrawable[1], "chat_outInstant");
            addChatDrawable("drawableMsgOutCallVideo", chat_msgOutCallSelectedDrawable[1], "chat_outInstantSelected");
            addChatDrawable("drawableMsgOutCheck", chat_msgOutCheckDrawable, "chat_outSentCheck");
            addChatDrawable("drawableMsgOutCheckSelected", chat_msgOutCheckSelectedDrawable, "chat_outSentCheckSelected");
            addChatDrawable("drawableMsgOutCheckRead", chat_msgOutCheckReadDrawable, "chat_outSentCheckRead");
            addChatDrawable("drawableMsgOutCheckReadSelected", chat_msgOutCheckReadSelectedDrawable, "chat_outSentCheckReadSelected");
            addChatDrawable("drawableMsgOutHalfCheck", chat_msgOutHalfCheckDrawable, "chat_outSentCheckRead");
            addChatDrawable("drawableMsgOutHalfCheckSelected", chat_msgOutHalfCheckSelectedDrawable, "chat_outSentCheckReadSelected");
            addChatDrawable("drawableMsgOutInstant", chat_msgOutInstantDrawable, "chat_outInstant");
            addChatDrawable("drawableMsgOutMenu", chat_msgOutMenuDrawable, "chat_outMenu");
            addChatDrawable("drawableMsgOutMenuSelected", chat_msgOutMenuSelectedDrawable, "chat_outMenuSelected");
            addChatDrawable("drawableMsgOutPinned", chat_msgOutPinnedDrawable, "chat_outViews");
            addChatDrawable("drawableMsgOutPinnedSelected", chat_msgOutPinnedSelectedDrawable, "chat_outViewsSelected");
            addChatDrawable("drawableMsgOutReplies", chat_msgOutRepliesDrawable, "chat_outViews");
            addChatDrawable("drawableMsgOutReplies", chat_msgOutRepliesSelectedDrawable, "chat_outViewsSelected");
            addChatDrawable("drawableMsgOutViews", chat_msgOutViewsDrawable, "chat_outViews");
            addChatDrawable("drawableMsgOutViewsSelected", chat_msgOutViewsSelectedDrawable, "chat_outViewsSelected");
            addChatDrawable("drawableMsgStickerCheck", chat_msgStickerCheckDrawable, "chat_serviceText");
            addChatDrawable("drawableMsgStickerHalfCheck", chat_msgStickerHalfCheckDrawable, "chat_serviceText");
            addChatDrawable("drawableMsgStickerPinned", chat_msgStickerPinnedDrawable, "chat_serviceText");
            addChatDrawable("drawableMsgStickerReplies", chat_msgStickerRepliesDrawable, "chat_serviceText");
            addChatDrawable("drawableMsgStickerViews", chat_msgStickerViewsDrawable, "chat_serviceText");
            addChatDrawable("drawableReplyIcon", chat_replyIconDrawable, "chat_serviceIcon");
            addChatDrawable("drawableShareIcon", chat_shareIconDrawable, "chat_serviceIcon");
            addChatDrawable("drawableMuteIcon", chat_muteIconDrawable, "chat_muteIcon");
            addChatDrawable("drawableLockIcon", chat_lockIconDrawable, "chat_lockIcon");
            addChatDrawable("drawable_chat_pollHintDrawableOut", chat_pollHintDrawable[1], "chat_outPreviewInstantText");
            addChatDrawable("drawable_chat_pollHintDrawableIn", chat_pollHintDrawable[0], "chat_inPreviewInstantText");
            applyChatTheme(z, false);
        }
        if (z || (paint = chat_botProgressPaint) == null) {
            return;
        }
        paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        chat_infoPaint.setTextSize(AndroidUtilities.dp(12.0f));
        chat_stickerCommentCountPaint.setTextSize(AndroidUtilities.dp(11.0f));
        chat_docNamePaint.setTextSize(AndroidUtilities.dp(15.0f));
        chat_locationTitlePaint.setTextSize(AndroidUtilities.dp(15.0f));
        chat_locationAddressPaint.setTextSize(AndroidUtilities.dp(13.0f));
        chat_audioTimePaint.setTextSize(AndroidUtilities.dp(12.0f));
        chat_livePaint.setTextSize(AndroidUtilities.dp(12.0f));
        chat_audioTitlePaint.setTextSize(AndroidUtilities.dp(16.0f));
        chat_audioPerformerPaint.setTextSize(AndroidUtilities.dp(15.0f));
        chat_botButtonPaint.setTextSize(AndroidUtilities.dp(15.0f));
        chat_contactNamePaint.setTextSize(AndroidUtilities.dp(15.0f));
        chat_contactPhonePaint.setTextSize(AndroidUtilities.dp(13.0f));
        chat_durationPaint.setTextSize(AndroidUtilities.dp(12.0f));
        chat_timePaint.setTextSize(AndroidUtilities.dp(12.0f));
        chat_adminPaint.setTextSize(AndroidUtilities.dp(13.0f));
        chat_namePaint.setTextSize(AndroidUtilities.dp(14.0f));
        chat_forwardNamePaint.setTextSize(AndroidUtilities.dp(14.0f));
        chat_replyNamePaint.setTextSize(AndroidUtilities.dp(14.0f));
        chat_replyTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
        chat_gamePaint.setTextSize(AndroidUtilities.dp(13.0f));
        chat_shipmentPaint.setTextSize(AndroidUtilities.dp(13.0f));
        chat_instantViewPaint.setTextSize(AndroidUtilities.dp(13.0f));
        chat_instantViewRectPaint.setStrokeWidth(AndroidUtilities.dp(1.0f));
        chat_pollTimerPaint.setStrokeWidth(AndroidUtilities.dp(1.1f));
        chat_actionTextPaint.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize) - 2));
        chat_contextResult_titleTextPaint.setTextSize(AndroidUtilities.dp(15.0f));
        chat_contextResult_descriptionTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
        chat_radialProgressPaint.setStrokeWidth(AndroidUtilities.dp(3.0f));
        chat_radialProgress2Paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
    }

    public static void refreshAttachButtonsColors() {
        int i = 0;
        while (true) {
            RLottieDrawable[] rLottieDrawableArr = chat_attachButtonDrawables;
            if (i < rLottieDrawableArr.length) {
                if (rLottieDrawableArr[i] != null) {
                    rLottieDrawableArr[i].beginApplyLayerColors();
                    if (i == 0) {
                        chat_attachButtonDrawables[i].setLayerColor("Color_Mount.**", getNonAnimatedColor("chat_attachGalleryBackground"));
                        chat_attachButtonDrawables[i].setLayerColor("Color_PhotoShadow.**", getNonAnimatedColor("chat_attachGalleryBackground"));
                        chat_attachButtonDrawables[i].setLayerColor("White_Photo.**", getNonAnimatedColor("chat_attachGalleryIcon"));
                        chat_attachButtonDrawables[i].setLayerColor("White_BackPhoto.**", getNonAnimatedColor("chat_attachGalleryIcon"));
                    } else if (i == 1) {
                        chat_attachButtonDrawables[i].setLayerColor("White_Play1.**", getNonAnimatedColor("chat_attachAudioIcon"));
                        chat_attachButtonDrawables[i].setLayerColor("White_Play2.**", getNonAnimatedColor("chat_attachAudioIcon"));
                    } else if (i == 2) {
                        chat_attachButtonDrawables[i].setLayerColor("Color_Corner.**", getNonAnimatedColor("chat_attachFileBackground"));
                        chat_attachButtonDrawables[i].setLayerColor("White_List.**", getNonAnimatedColor("chat_attachFileIcon"));
                    } else if (i == 3) {
                        chat_attachButtonDrawables[i].setLayerColor("White_User1.**", getNonAnimatedColor("chat_attachContactIcon"));
                        chat_attachButtonDrawables[i].setLayerColor("White_User2.**", getNonAnimatedColor("chat_attachContactIcon"));
                    } else if (i == 4) {
                        chat_attachButtonDrawables[i].setLayerColor("Color_Oval.**", getNonAnimatedColor("chat_attachLocationBackground"));
                        chat_attachButtonDrawables[i].setLayerColor("White_Pin.**", getNonAnimatedColor("chat_attachLocationIcon"));
                    } else if (i == 5) {
                        chat_attachButtonDrawables[i].setLayerColor("White_Column 1.**", getNonAnimatedColor("chat_attachPollIcon"));
                        chat_attachButtonDrawables[i].setLayerColor("White_Column 2.**", getNonAnimatedColor("chat_attachPollIcon"));
                        chat_attachButtonDrawables[i].setLayerColor("White_Column 3.**", getNonAnimatedColor("chat_attachPollIcon"));
                    }
                    chat_attachButtonDrawables[i].commitApplyLayerColors();
                }
                i++;
            } else {
                return;
            }
        }
    }

    public static void applyChatTheme(boolean z, boolean z2) {
        if (chat_msgTextPaint == null || chat_msgInDrawable == null || z) {
            return;
        }
        chat_gamePaint.setColor(getColor("chat_previewGameText"));
        chat_durationPaint.setColor(getColor("chat_previewDurationText"));
        chat_botButtonPaint.setColor(getColor("chat_botButtonText"));
        chat_urlPaint.setColor(getColor("chat_linkSelectBackground"));
        chat_outUrlPaint.setColor(getColor("chat_outLinkSelectBackground"));
        chat_botProgressPaint.setColor(getColor("chat_botProgress"));
        chat_deleteProgressPaint.setColor(getColor("chat_secretTimeText"));
        chat_textSearchSelectionPaint.setColor(getColor("chat_textSelectBackground"));
        chat_msgErrorPaint.setColor(getColor("chat_sentError"));
        chat_statusPaint.setColor(getColor("chat_status"));
        chat_statusRecordPaint.setColor(getColor("chat_status"));
        chat_actionTextPaint.setColor(getColor("chat_serviceText"));
        chat_actionTextPaint.linkColor = getColor("chat_serviceLink");
        chat_contextResult_titleTextPaint.setColor(getColor("windowBackgroundWhiteBlackText"));
        chat_composeBackgroundPaint.setColor(getColor("chat_messagePanelBackground"));
        chat_timeBackgroundPaint.setColor(getColor("chat_mediaTimeBackground"));
        setDrawableColorByKey(chat_msgNoSoundDrawable, "chat_mediaTimeText");
        setDrawableColorByKey(chat_msgInDrawable, "chat_inBubble");
        setDrawableColorByKey(chat_msgInSelectedDrawable, "chat_inBubbleSelected");
        setDrawableColorByKey(chat_msgInMediaDrawable, "chat_inBubble");
        setDrawableColorByKey(chat_msgInMediaSelectedDrawable, "chat_inBubbleSelected");
        setDrawableColorByKey(chat_msgOutCheckDrawable, "chat_outSentCheck");
        setDrawableColorByKey(chat_msgOutCheckSelectedDrawable, "chat_outSentCheckSelected");
        setDrawableColorByKey(chat_msgOutCheckReadDrawable, "chat_outSentCheckRead");
        setDrawableColorByKey(chat_msgOutCheckReadSelectedDrawable, "chat_outSentCheckReadSelected");
        setDrawableColorByKey(chat_msgOutHalfCheckDrawable, "chat_outSentCheckRead");
        setDrawableColorByKey(chat_msgOutHalfCheckSelectedDrawable, "chat_outSentCheckReadSelected");
        setDrawableColorByKey(chat_msgMediaCheckDrawable, "chat_mediaSentCheck");
        setDrawableColorByKey(chat_msgMediaHalfCheckDrawable, "chat_mediaSentCheck");
        setDrawableColorByKey(chat_msgStickerCheckDrawable, "chat_serviceText");
        setDrawableColorByKey(chat_msgStickerHalfCheckDrawable, "chat_serviceText");
        setDrawableColorByKey(chat_msgStickerViewsDrawable, "chat_serviceText");
        setDrawableColorByKey(chat_msgStickerRepliesDrawable, "chat_serviceText");
        setDrawableColorByKey(chat_shareIconDrawable, "chat_serviceIcon");
        setDrawableColorByKey(chat_replyIconDrawable, "chat_serviceIcon");
        setDrawableColorByKey(chat_goIconDrawable, "chat_serviceIcon");
        setDrawableColorByKey(chat_botInlineDrawable, "chat_serviceIcon");
        setDrawableColorByKey(chat_botWebViewDrawable, "chat_serviceIcon");
        setDrawableColorByKey(chat_botInviteDrawable, "chat_serviceIcon");
        setDrawableColorByKey(chat_botLinkDrawable, "chat_serviceIcon");
        setDrawableColorByKey(chat_msgInViewsDrawable, "chat_inViews");
        setDrawableColorByKey(chat_msgInViewsSelectedDrawable, "chat_inViewsSelected");
        setDrawableColorByKey(chat_msgOutViewsDrawable, "chat_outViews");
        setDrawableColorByKey(chat_msgOutViewsSelectedDrawable, "chat_outViewsSelected");
        setDrawableColorByKey(chat_msgInRepliesDrawable, "chat_inViews");
        setDrawableColorByKey(chat_msgInRepliesSelectedDrawable, "chat_inViewsSelected");
        setDrawableColorByKey(chat_msgOutRepliesDrawable, "chat_outViews");
        setDrawableColorByKey(chat_msgOutRepliesSelectedDrawable, "chat_outViewsSelected");
        setDrawableColorByKey(chat_msgInPinnedDrawable, "chat_inViews");
        setDrawableColorByKey(chat_msgInPinnedSelectedDrawable, "chat_inViewsSelected");
        setDrawableColorByKey(chat_msgOutPinnedDrawable, "chat_outViews");
        setDrawableColorByKey(chat_msgOutPinnedSelectedDrawable, "chat_outViewsSelected");
        setDrawableColorByKey(chat_msgMediaPinnedDrawable, "chat_mediaViews");
        setDrawableColorByKey(chat_msgStickerPinnedDrawable, "chat_serviceText");
        setDrawableColorByKey(chat_msgMediaViewsDrawable, "chat_mediaViews");
        setDrawableColorByKey(chat_msgMediaRepliesDrawable, "chat_mediaViews");
        setDrawableColorByKey(chat_msgInMenuDrawable, "chat_inMenu");
        setDrawableColorByKey(chat_msgInMenuSelectedDrawable, "chat_inMenuSelected");
        setDrawableColorByKey(chat_msgOutMenuDrawable, "chat_outMenu");
        setDrawableColorByKey(chat_msgOutMenuSelectedDrawable, "chat_outMenuSelected");
        setDrawableColorByKey(chat_msgMediaMenuDrawable, "chat_mediaMenu");
        setDrawableColorByKey(chat_msgOutInstantDrawable, "chat_outInstant");
        setDrawableColorByKey(chat_msgInInstantDrawable, "chat_inInstant");
        setDrawableColorByKey(chat_msgErrorDrawable, "chat_sentErrorIcon");
        setDrawableColorByKey(chat_muteIconDrawable, "chat_muteIcon");
        setDrawableColorByKey(chat_lockIconDrawable, "chat_lockIcon");
        setDrawableColorByKey(chat_inlineResultFile, "chat_inlineResultIcon");
        setDrawableColorByKey(chat_inlineResultAudio, "chat_inlineResultIcon");
        setDrawableColorByKey(chat_inlineResultLocation, "chat_inlineResultIcon");
        setDrawableColorByKey(chat_commentDrawable, "chat_inInstant");
        setDrawableColorByKey(chat_commentStickerDrawable, "chat_serviceIcon");
        setDrawableColorByKey(chat_commentArrowDrawable, "chat_inInstant");
        for (int i = 0; i < 2; i++) {
            setDrawableColorByKey(chat_msgInCallDrawable[i], "chat_inInstant");
            setDrawableColorByKey(chat_msgInCallSelectedDrawable[i], "chat_inInstantSelected");
            setDrawableColorByKey(chat_msgOutCallDrawable[i], "chat_outInstant");
            setDrawableColorByKey(chat_msgOutCallSelectedDrawable[i], "chat_outInstantSelected");
        }
        setDrawableColorByKey(chat_msgCallUpGreenDrawable, "chat_outUpCall");
        setDrawableColorByKey(chat_msgCallDownRedDrawable, "chat_inUpCall");
        setDrawableColorByKey(chat_msgCallDownGreenDrawable, "chat_inDownCall");
        setDrawableColorByKey(calllog_msgCallUpRedDrawable, "calls_callReceivedRedIcon");
        setDrawableColorByKey(calllog_msgCallUpGreenDrawable, "calls_callReceivedGreenIcon");
        setDrawableColorByKey(calllog_msgCallDownRedDrawable, "calls_callReceivedRedIcon");
        setDrawableColorByKey(calllog_msgCallDownGreenDrawable, "calls_callReceivedGreenIcon");
        int i2 = 0;
        while (true) {
            StatusDrawable[] statusDrawableArr = chat_status_drawables;
            if (i2 >= statusDrawableArr.length) {
                break;
            }
            setDrawableColorByKey(statusDrawableArr[i2], "chats_actionMessage");
            i2++;
        }
        for (int i3 = 0; i3 < 5; i3++) {
            setCombinedDrawableColor(chat_fileStatesDrawable[i3][0], getColor("chat_outLoader"), false);
            setCombinedDrawableColor(chat_fileStatesDrawable[i3][0], getColor("chat_outMediaIcon"), true);
            setCombinedDrawableColor(chat_fileStatesDrawable[i3][1], getColor("chat_outLoaderSelected"), false);
            setCombinedDrawableColor(chat_fileStatesDrawable[i3][1], getColor("chat_outMediaIconSelected"), true);
            int i4 = i3 + 5;
            setCombinedDrawableColor(chat_fileStatesDrawable[i4][0], getColor("chat_inLoader"), false);
            setCombinedDrawableColor(chat_fileStatesDrawable[i4][0], getColor("chat_inMediaIcon"), true);
            setCombinedDrawableColor(chat_fileStatesDrawable[i4][1], getColor("chat_inLoaderSelected"), false);
            setCombinedDrawableColor(chat_fileStatesDrawable[i4][1], getColor("chat_inMediaIconSelected"), true);
        }
        for (int i5 = 0; i5 < 4; i5++) {
            setCombinedDrawableColor(chat_photoStatesDrawables[i5][0], getColor("chat_mediaLoaderPhoto"), false);
            setCombinedDrawableColor(chat_photoStatesDrawables[i5][0], getColor("chat_mediaLoaderPhotoIcon"), true);
            setCombinedDrawableColor(chat_photoStatesDrawables[i5][1], getColor("chat_mediaLoaderPhotoSelected"), false);
            setCombinedDrawableColor(chat_photoStatesDrawables[i5][1], getColor("chat_mediaLoaderPhotoIconSelected"), true);
        }
        for (int i6 = 0; i6 < 2; i6++) {
            int i7 = i6 + 7;
            setCombinedDrawableColor(chat_photoStatesDrawables[i7][0], getColor("chat_outLoaderPhoto"), false);
            setCombinedDrawableColor(chat_photoStatesDrawables[i7][0], getColor("chat_outLoaderPhotoIcon"), true);
            setCombinedDrawableColor(chat_photoStatesDrawables[i7][1], getColor("chat_outLoaderPhotoSelected"), false);
            setCombinedDrawableColor(chat_photoStatesDrawables[i7][1], getColor("chat_outLoaderPhotoIconSelected"), true);
            int i8 = i6 + 10;
            setCombinedDrawableColor(chat_photoStatesDrawables[i8][0], getColor("chat_inLoaderPhoto"), false);
            setCombinedDrawableColor(chat_photoStatesDrawables[i8][0], getColor("chat_inLoaderPhotoIcon"), true);
            setCombinedDrawableColor(chat_photoStatesDrawables[i8][1], getColor("chat_inLoaderPhotoSelected"), false);
            setCombinedDrawableColor(chat_photoStatesDrawables[i8][1], getColor("chat_inLoaderPhotoIconSelected"), true);
        }
        setCombinedDrawableColor(chat_contactDrawable[0], getColor("chat_inContactBackground"), false);
        setCombinedDrawableColor(chat_contactDrawable[0], getColor("chat_inContactIcon"), true);
        setCombinedDrawableColor(chat_contactDrawable[1], getColor("chat_outContactBackground"), false);
        setCombinedDrawableColor(chat_contactDrawable[1], getColor("chat_outContactIcon"), true);
        setDrawableColor(chat_locationDrawable[0], getColor("chat_inLocationIcon"));
        setDrawableColor(chat_locationDrawable[1], getColor("chat_outLocationIcon"));
        setDrawableColor(chat_pollHintDrawable[0], getColor("chat_inPreviewInstantText"));
        setDrawableColor(chat_pollHintDrawable[1], getColor("chat_outPreviewInstantText"));
        setDrawableColor(chat_psaHelpDrawable[0], getColor("chat_inViews"));
        setDrawableColor(chat_psaHelpDrawable[1], getColor("chat_outViews"));
        setDrawableColorByKey(chat_composeShadowDrawable, "chat_messagePanelShadow");
        setDrawableColorByKey(chat_composeShadowRoundDrawable, "chat_messagePanelBackground");
        int color = getColor("chat_outAudioSeekbarFill") == -1 ? getColor("chat_outBubble") : -1;
        setDrawableColor(chat_pollCheckDrawable[1], color);
        setDrawableColor(chat_pollCrossDrawable[1], color);
        setDrawableColor(chat_attachEmptyDrawable, getColor("chat_attachEmptyImage"));
        if (!z2) {
            applyChatServiceMessageColor();
            applyChatMessageSelectedBackgroundColor();
        }
        refreshAttachButtonsColors();
    }

    public static void applyChatServiceMessageColor() {
        applyChatServiceMessageColor(null, null, wallpaper);
    }

    public static boolean hasGradientService() {
        return serviceBitmapShader != null;
    }

    public static void applyServiceShaderMatrixForView(View view, View view2) {
        if (view == null || view2 == null) {
            return;
        }
        view.getLocationOnScreen(viewPos);
        int[] iArr = viewPos;
        int i = iArr[0];
        int i2 = iArr[1];
        view2.getLocationOnScreen(iArr);
        applyServiceShaderMatrix(view2.getMeasuredWidth(), view2.getMeasuredHeight(), i, i2 - viewPos[1]);
    }

    public static void applyServiceShaderMatrix(int i, int i2, float f, float f2) {
        applyServiceShaderMatrix(serviceBitmap, serviceBitmapShader, serviceBitmapMatrix, i, i2, f, f2);
    }

    public static void applyServiceShaderMatrix(Bitmap bitmap, BitmapShader bitmapShader, Matrix matrix, int i, int i2, float f, float f2) {
        if (bitmapShader == null) {
            return;
        }
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float f3 = i;
        float f4 = i2;
        float max = Math.max(f3 / width, f4 / height);
        matrix.reset();
        matrix.setTranslate(((f3 - (width * max)) / 2.0f) - f, ((f4 - (height * max)) / 2.0f) - f2);
        matrix.preScale(max, max);
        bitmapShader.setLocalMatrix(matrix);
    }

    public static void applyChatServiceMessageColor(int[] iArr, Drawable drawable) {
        applyChatServiceMessageColor(iArr, drawable, wallpaper);
    }

    public static void applyChatServiceMessageColor(int[] iArr, Drawable drawable, Drawable drawable2) {
        Integer num;
        Integer num2;
        Integer num3;
        if (chat_actionBackgroundPaint == null) {
            return;
        }
        serviceMessageColor = serviceMessageColorBackup;
        serviceSelectedMessageColor = serviceSelectedMessageColorBackup;
        boolean z = true;
        if (iArr != null && iArr.length >= 2) {
            num2 = Integer.valueOf(iArr[0]);
            num = Integer.valueOf(iArr[1]);
            serviceMessageColor = iArr[0];
            serviceSelectedMessageColor = iArr[1];
        } else {
            num2 = currentColors.get("chat_serviceBackground");
            num = currentColors.get("chat_serviceBackgroundSelected");
        }
        if (num2 == null) {
            num2 = Integer.valueOf(serviceMessageColor);
            num3 = Integer.valueOf(serviceMessage2Color);
        } else {
            num3 = num2;
        }
        if (num == null) {
            num = Integer.valueOf(serviceSelectedMessageColor);
        }
        if (drawable == null) {
            drawable = drawable2;
        }
        boolean z2 = drawable instanceof MotionBackgroundDrawable;
        if (!z2 || SharedConfig.getDevicePerformanceClass() == 0) {
            z = false;
        }
        if (z) {
            Bitmap bitmap = ((MotionBackgroundDrawable) drawable).getBitmap();
            if (serviceBitmap != bitmap) {
                serviceBitmap = bitmap;
                Bitmap bitmap2 = serviceBitmap;
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                serviceBitmapShader = new BitmapShader(bitmap2, tileMode, tileMode);
                if (serviceBitmapMatrix == null) {
                    serviceBitmapMatrix = new Matrix();
                }
            }
            setDrawableColor(chat_msgStickerPinnedDrawable, -1);
            setDrawableColor(chat_msgStickerCheckDrawable, -1);
            setDrawableColor(chat_msgStickerHalfCheckDrawable, -1);
            setDrawableColor(chat_msgStickerViewsDrawable, -1);
            setDrawableColor(chat_msgStickerRepliesDrawable, -1);
            chat_actionTextPaint.setColor(-1);
            chat_actionTextPaint.linkColor = -1;
            chat_botButtonPaint.setColor(-1);
            setDrawableColor(chat_commentStickerDrawable, -1);
            setDrawableColor(chat_shareIconDrawable, -1);
            setDrawableColor(chat_replyIconDrawable, -1);
            setDrawableColor(chat_goIconDrawable, -1);
            setDrawableColor(chat_botInlineDrawable, -1);
            setDrawableColor(chat_botWebViewDrawable, -1);
            setDrawableColor(chat_botInviteDrawable, -1);
            setDrawableColor(chat_botLinkDrawable, -1);
        } else {
            serviceBitmap = null;
            serviceBitmapShader = null;
            setDrawableColorByKey(chat_msgStickerPinnedDrawable, "chat_serviceText");
            setDrawableColorByKey(chat_msgStickerCheckDrawable, "chat_serviceText");
            setDrawableColorByKey(chat_msgStickerHalfCheckDrawable, "chat_serviceText");
            setDrawableColorByKey(chat_msgStickerViewsDrawable, "chat_serviceText");
            setDrawableColorByKey(chat_msgStickerRepliesDrawable, "chat_serviceText");
            chat_actionTextPaint.setColor(getColor("chat_serviceText"));
            chat_actionTextPaint.linkColor = getColor("chat_serviceLink");
            setDrawableColorByKey(chat_commentStickerDrawable, "chat_serviceIcon");
            setDrawableColorByKey(chat_shareIconDrawable, "chat_serviceIcon");
            setDrawableColorByKey(chat_replyIconDrawable, "chat_serviceIcon");
            setDrawableColorByKey(chat_goIconDrawable, "chat_serviceIcon");
            setDrawableColorByKey(chat_botInlineDrawable, "chat_serviceIcon");
            setDrawableColorByKey(chat_botWebViewDrawable, "chat_serviceIcon");
            setDrawableColorByKey(chat_botInviteDrawable, "chat_serviceIcon");
            setDrawableColorByKey(chat_botLinkDrawable, "chat_serviceIcon");
            chat_botButtonPaint.setColor(getColor("chat_botButtonText"));
        }
        chat_actionBackgroundPaint.setColor(num2.intValue());
        chat_actionBackgroundSelectedPaint.setColor(num.intValue());
        chat_actionBackgroundPaint2.setColor(num3.intValue());
        num2.intValue();
        if (serviceBitmapShader != null && (currentColors.get("chat_serviceBackground") == null || z2)) {
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(((MotionBackgroundDrawable) drawable).getIntensity() >= 0 ? 1.8f : 0.5f);
            chat_actionBackgroundPaint.setShader(serviceBitmapShader);
            chat_actionBackgroundPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            chat_actionBackgroundPaint.setAlpha(127);
            chat_actionBackgroundSelectedPaint.setShader(serviceBitmapShader);
            chat_actionBackgroundSelectedPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            chat_actionBackgroundSelectedPaint.setAlpha(200);
            return;
        }
        chat_actionBackgroundPaint.setColorFilter(null);
        chat_actionBackgroundPaint.setShader(null);
        chat_actionBackgroundSelectedPaint.setColorFilter(null);
        chat_actionBackgroundSelectedPaint.setShader(null);
    }

    public static void applyChatMessageSelectedBackgroundColor() {
        applyChatMessageSelectedBackgroundColor(null, wallpaper);
    }

    public static void applyChatMessageSelectedBackgroundColor(Drawable drawable, Drawable drawable2) {
        Bitmap bitmap;
        if (chat_messageBackgroundSelectedPaint == null) {
            return;
        }
        Integer num = currentColors.get("chat_selectedBackground");
        if (drawable == null) {
            drawable = drawable2;
        }
        boolean z = (drawable instanceof MotionBackgroundDrawable) && SharedConfig.getDevicePerformanceClass() != 0 && num == null;
        if (z && serviceBitmap != (bitmap = ((MotionBackgroundDrawable) drawable).getBitmap())) {
            serviceBitmap = bitmap;
            Bitmap bitmap2 = serviceBitmap;
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            serviceBitmapShader = new BitmapShader(bitmap2, tileMode, tileMode);
            if (serviceBitmapMatrix == null) {
                serviceBitmapMatrix = new Matrix();
            }
        }
        if (serviceBitmapShader != null && num == null && z) {
            ColorMatrix colorMatrix = new ColorMatrix();
            AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, 2.5f);
            AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, 0.75f);
            chat_messageBackgroundSelectedPaint.setShader(serviceBitmapShader);
            chat_messageBackgroundSelectedPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            chat_messageBackgroundSelectedPaint.setAlpha(64);
            return;
        }
        chat_messageBackgroundSelectedPaint.setColor(num == null ? 1073741824 : num.intValue());
        chat_messageBackgroundSelectedPaint.setColorFilter(null);
        chat_messageBackgroundSelectedPaint.setShader(null);
    }

    public static void createProfileResources(Context context) {
        if (profile_verifiedDrawable == null) {
            profile_aboutTextPaint = new TextPaint(1);
            Resources resources = context.getResources();
            profile_verifiedDrawable = resources.getDrawable(R.drawable.verified_area).mutate();
            profile_verifiedCheckDrawable = resources.getDrawable(R.drawable.verified_check).mutate();
            applyProfileTheme();
        }
        profile_aboutTextPaint.setTextSize(AndroidUtilities.dp(16.0f));
    }

    public static void applyProfileTheme() {
        if (profile_verifiedDrawable == null) {
            return;
        }
        profile_aboutTextPaint.setColor(getColor("windowBackgroundWhiteBlackText"));
        profile_aboutTextPaint.linkColor = getColor("windowBackgroundWhiteLinkText");
        setDrawableColorByKey(profile_verifiedDrawable, "profile_verifiedBackground");
        setDrawableColorByKey(profile_verifiedCheckDrawable, "profile_verifiedCheck");
    }

    public static Drawable getThemedDrawable(Context context, int i, String str) {
        return getThemedDrawable(context, i, getColor(str));
    }

    public static Drawable getThemedDrawable(Context context, int i, int i2) {
        if (context == null) {
            return null;
        }
        Drawable mutate = context.getResources().getDrawable(i).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.MULTIPLY));
        return mutate;
    }

    public static int getDefaultColor(String str) {
        Integer num = defaultColors.get(str);
        if (num == null) {
            return (str.equals("chats_menuTopShadow") || str.equals("chats_menuTopBackground") || str.equals("chats_menuTopShadowCats") || str.equals("key_chat_wallpaper_gradient_to2") || str.equals("key_chat_wallpaper_gradient_to3")) ? 0 : -65536;
        }
        return num.intValue();
    }

    public static boolean hasThemeKey(String str) {
        return currentColors.containsKey(str);
    }

    public static Integer getColorOrNull(String str) {
        Integer num = currentColors.get(str);
        if (num == null) {
            if (fallbackKeys.get(str) != null) {
                num = currentColors.get(str);
            }
            if (num == null) {
                num = defaultColors.get(str);
            }
        }
        return num != null ? ("windowBackgroundWhite".equals(str) || "windowBackgroundGray".equals(str) || "actionBarDefault".equals(str) || "actionBarDefaultArchived".equals(str)) ? Integer.valueOf(num.intValue() | (-16777216)) : num : num;
    }

    public static void setAnimatingColor(boolean z) {
        animatingColors = z ? new HashMap<>() : null;
    }

    public static boolean isAnimatingColor() {
        return animatingColors != null;
    }

    public static void setAnimatedColor(String str, int i) {
        HashMap<String, Integer> hashMap = animatingColors;
        if (hashMap == null) {
            return;
        }
        hashMap.put(str, Integer.valueOf(i));
    }

    public static int getDefaultAccentColor(String str) {
        ThemeAccent accent;
        Integer num = currentColorsNoAccent.get(str);
        if (num == null || (accent = currentTheme.getAccent(false)) == null) {
            return 0;
        }
        float[] tempHsv = getTempHsv(1);
        float[] tempHsv2 = getTempHsv(2);
        Color.colorToHSV(currentTheme.accentBaseColor, tempHsv);
        Color.colorToHSV(accent.accentColor, tempHsv2);
        return changeColorAccent(tempHsv, tempHsv2, num.intValue(), currentTheme.isDark());
    }

    public static int getNonAnimatedColor(String str) {
        return getColor(str, null, true);
    }

    public static int getColor(String str, ResourcesProvider resourcesProvider) {
        Integer color;
        if (resourcesProvider != null && (color = resourcesProvider.getColor(str)) != null) {
            return color.intValue();
        }
        return getColor(str);
    }

    public static int getColor(String str) {
        return getColor(str, null, false);
    }

    public static int getColor(String str, boolean[] zArr) {
        return getColor(str, zArr, false);
    }

    public static int getColor(String str, boolean[] zArr, boolean z) {
        boolean z2;
        HashMap<String, Integer> hashMap;
        Integer num;
        if (!z && (hashMap = animatingColors) != null && (num = hashMap.get(str)) != null) {
            return num.intValue();
        }
        if (serviceBitmapShader != null && ("chat_serviceText".equals(str) || "chat_serviceLink".equals(str) || "chat_serviceIcon".equals(str) || "chat_stickerReplyLine".equals(str) || "chat_stickerReplyNameText".equals(str) || "chat_stickerReplyMessageText".equals(str))) {
            return -1;
        }
        if (currentTheme == defaultTheme) {
            if (myMessagesBubblesColorKeys.contains(str)) {
                z2 = currentTheme.isDefaultMyMessagesBubbles();
            } else {
                z2 = myMessagesColorKeys.contains(str) ? currentTheme.isDefaultMyMessages() : ("chat_wallpaper".equals(str) || "chat_wallpaper_gradient_to".equals(str) || "key_chat_wallpaper_gradient_to2".equals(str) || "key_chat_wallpaper_gradient_to3".equals(str)) ? false : currentTheme.isDefaultMainAccent();
            }
            if (z2) {
                if (str.equals("chat_serviceBackground")) {
                    return serviceMessageColor;
                }
                if (str.equals("chat_serviceBackgroundSelected")) {
                    return serviceSelectedMessageColor;
                }
                return getDefaultColor(str);
            }
        }
        Integer num2 = currentColors.get(str);
        if (num2 == null) {
            String str2 = fallbackKeys.get(str);
            if (str2 != null) {
                num2 = currentColors.get(str2);
            }
            if (num2 == null) {
                if (zArr != null) {
                    zArr[0] = true;
                }
                if (str.equals("chat_serviceBackground")) {
                    return serviceMessageColor;
                }
                if (str.equals("chat_serviceBackgroundSelected")) {
                    return serviceSelectedMessageColor;
                }
                return getDefaultColor(str);
            }
        }
        if ("windowBackgroundWhite".equals(str) || "windowBackgroundGray".equals(str) || "actionBarDefault".equals(str) || "actionBarDefaultArchived".equals(str)) {
            num2 = Integer.valueOf(num2.intValue() | (-16777216));
        }
        return num2.intValue();
    }

    public static void setColor(String str, int i, boolean z) {
        if (str.equals("chat_wallpaper") || str.equals("chat_wallpaper_gradient_to") || str.equals("key_chat_wallpaper_gradient_to2") || str.equals("key_chat_wallpaper_gradient_to3") || str.equals("windowBackgroundWhite") || str.equals("windowBackgroundGray") || str.equals("actionBarDefault") || str.equals("actionBarDefaultArchived")) {
            i |= -16777216;
        }
        if (z) {
            currentColors.remove(str);
        } else {
            currentColors.put(str, Integer.valueOf(i));
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -2095843767:
                if (str.equals("chat_wallpaper_gradient_rotation")) {
                    c = 0;
                    break;
                }
                break;
            case -1625862693:
                if (str.equals("chat_wallpaper")) {
                    c = 1;
                    break;
                }
                break;
            case -1397026623:
                if (str.equals("windowBackgroundGray")) {
                    c = 2;
                    break;
                }
                break;
            case -633951866:
                if (str.equals("chat_wallpaper_gradient_to")) {
                    c = 3;
                    break;
                }
                break;
            case -552118908:
                if (str.equals("actionBarDefault")) {
                    c = 4;
                    break;
                }
                break;
            case -391617936:
                if (str.equals("chat_selectedBackground")) {
                    c = 5;
                    break;
                }
                break;
            case 426061980:
                if (str.equals("chat_serviceBackground")) {
                    c = 6;
                    break;
                }
                break;
            case 1381936524:
                if (str.equals("key_chat_wallpaper_gradient_to2")) {
                    c = 7;
                    break;
                }
                break;
            case 1381936525:
                if (str.equals("key_chat_wallpaper_gradient_to3")) {
                    c = '\b';
                    break;
                }
                break;
            case 1573464919:
                if (str.equals("chat_serviceBackgroundSelected")) {
                    c = '\t';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 3:
            case 7:
            case '\b':
                reloadWallpaper();
                return;
            case 2:
                if (Build.VERSION.SDK_INT < 26) {
                    return;
                }
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needCheckSystemBarColors, new Object[0]);
                return;
            case 4:
                if (Build.VERSION.SDK_INT < 23) {
                    return;
                }
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needCheckSystemBarColors, new Object[0]);
                return;
            case 5:
                applyChatMessageSelectedBackgroundColor();
                return;
            case 6:
            case '\t':
                applyChatServiceMessageColor();
                return;
            default:
                return;
        }
    }

    public static void setDefaultColor(String str, int i) {
        defaultColors.put(str, Integer.valueOf(i));
    }

    public static void setThemeWallpaper(ThemeInfo themeInfo, Bitmap bitmap, File file) {
        currentColors.remove("chat_wallpaper");
        currentColors.remove("chat_wallpaper_gradient_to");
        currentColors.remove("key_chat_wallpaper_gradient_to2");
        currentColors.remove("key_chat_wallpaper_gradient_to3");
        currentColors.remove("chat_wallpaper_gradient_rotation");
        themedWallpaperLink = null;
        themeInfo.setOverrideWallpaper(null);
        if (bitmap != null) {
            themedWallpaper = new BitmapDrawable(bitmap);
            saveCurrentTheme(themeInfo, false, false, false);
            calcBackgroundColor(themedWallpaper, 0);
            applyChatServiceMessageColor();
            applyChatMessageSelectedBackgroundColor();
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper, new Object[0]);
            return;
        }
        themedWallpaper = null;
        wallpaper = null;
        saveCurrentTheme(themeInfo, false, false, false);
        reloadWallpaper();
    }

    public static void setDrawableColor(Drawable drawable, int i) {
        if (drawable == null) {
            return;
        }
        if (drawable instanceof StatusDrawable) {
            ((StatusDrawable) drawable).setColor(i);
        } else if (drawable instanceof MsgClockDrawable) {
            ((MsgClockDrawable) drawable).setColor(i);
        } else if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable) drawable).getPaint().setColor(i);
        } else if (drawable instanceof ScamDrawable) {
            ((ScamDrawable) drawable).setColor(i);
        } else {
            drawable.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
        }
    }

    public static void setDrawableColorByKey(Drawable drawable, String str) {
        if (str == null) {
            return;
        }
        setDrawableColor(drawable, getColor(str));
    }

    public static void setEmojiDrawableColor(Drawable drawable, int i, boolean z) {
        Drawable drawable2;
        if (drawable instanceof StateListDrawable) {
            try {
                if (z) {
                    drawable2 = getStateDrawable(drawable, 0);
                } else {
                    drawable2 = getStateDrawable(drawable, 1);
                }
                if (drawable2 instanceof ShapeDrawable) {
                    ((ShapeDrawable) drawable2).getPaint().setColor(i);
                } else {
                    drawable2.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
                }
            } catch (Throwable unused) {
            }
        }
    }

    @SuppressLint({"DiscouragedPrivateApi"})
    @TargetApi(R.styleable.MapAttrs_uiZoomGestures)
    public static void setRippleDrawableForceSoftware(RippleDrawable rippleDrawable) {
        if (rippleDrawable == null) {
            return;
        }
        try {
            RippleDrawable.class.getDeclaredMethod("setForceSoftware", Boolean.TYPE).invoke(rippleDrawable, Boolean.TRUE);
        } catch (Throwable unused) {
        }
    }

    public static void setSelectorDrawableColor(Drawable drawable, int i, boolean z) {
        Drawable drawable2;
        if (drawable instanceof StateListDrawable) {
            try {
                if (z) {
                    Drawable stateDrawable = getStateDrawable(drawable, 0);
                    if (stateDrawable instanceof ShapeDrawable) {
                        ((ShapeDrawable) stateDrawable).getPaint().setColor(i);
                    } else {
                        stateDrawable.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
                    }
                    drawable2 = getStateDrawable(drawable, 1);
                } else {
                    drawable2 = getStateDrawable(drawable, 2);
                }
                if (drawable2 instanceof ShapeDrawable) {
                    ((ShapeDrawable) drawable2).getPaint().setColor(i);
                } else {
                    drawable2.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
                }
            } catch (Throwable unused) {
            }
        } else if (Build.VERSION.SDK_INT < 21 || !(drawable instanceof RippleDrawable)) {
        } else {
            RippleDrawable rippleDrawable = (RippleDrawable) drawable;
            if (z) {
                rippleDrawable.setColor(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}));
            } else if (rippleDrawable.getNumberOfLayers() <= 0) {
            } else {
                Drawable drawable3 = rippleDrawable.getDrawable(0);
                if (drawable3 instanceof ShapeDrawable) {
                    ((ShapeDrawable) drawable3).getPaint().setColor(i);
                } else {
                    drawable3.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
                }
            }
        }
    }

    public static boolean isThemeWallpaperPublic() {
        return !TextUtils.isEmpty(themedWallpaperLink);
    }

    public static boolean hasWallpaperFromTheme() {
        ThemeInfo themeInfo = currentTheme;
        if (!themeInfo.firstAccentIsDefault || themeInfo.currentAccentId != DEFALT_THEME_ACCENT_ID) {
            return currentColors.containsKey("chat_wallpaper") || themedWallpaperFileOffset > 0 || !TextUtils.isEmpty(themedWallpaperLink);
        }
        return false;
    }

    public static boolean isCustomTheme() {
        return isCustomTheme;
    }

    public static void reloadWallpaper() {
        BackgroundGradientDrawable.Disposable disposable = backgroundGradientDisposable;
        if (disposable != null) {
            disposable.dispose();
            backgroundGradientDisposable = null;
        }
        Drawable drawable = wallpaper;
        if (drawable instanceof MotionBackgroundDrawable) {
            previousPhase = ((MotionBackgroundDrawable) drawable).getPhase();
        } else {
            previousPhase = 0;
        }
        wallpaper = null;
        themedWallpaper = null;
        loadWallpaper();
    }

    private static void calcBackgroundColor(Drawable drawable, int i) {
        if (i != 2) {
            int[] calcDrawableColor = AndroidUtilities.calcDrawableColor(drawable);
            int i2 = calcDrawableColor[0];
            serviceMessageColorBackup = i2;
            serviceMessageColor = i2;
            int i3 = calcDrawableColor[1];
            serviceSelectedMessageColorBackup = i3;
            serviceSelectedMessageColor = i3;
            serviceMessage2Color = calcDrawableColor[2];
            serviceSelectedMessage2Color = calcDrawableColor[3];
        }
    }

    public static int getServiceMessageColor() {
        Integer num = currentColors.get("chat_serviceBackground");
        return num == null ? serviceMessageColor : num.intValue();
    }

    public static void loadWallpaper() {
        final TLRPC$Document tLRPC$Document;
        final boolean z;
        final File file;
        float f;
        float f2;
        TLRPC$WallPaper tLRPC$WallPaper;
        if (wallpaper != null) {
            return;
        }
        ThemeInfo themeInfo = currentTheme;
        final boolean z2 = themeInfo.firstAccentIsDefault && themeInfo.currentAccentId == DEFALT_THEME_ACCENT_ID;
        ThemeAccent accent = themeInfo.getAccent(false);
        TLRPC$Document tLRPC$Document2 = null;
        if (accent != null) {
            File pathToWallpaper = accent.getPathToWallpaper();
            boolean z3 = accent.patternMotion;
            TLRPC$TL_theme tLRPC$TL_theme = accent.info;
            TLRPC$ThemeSettings tLRPC$ThemeSettings = (tLRPC$TL_theme == null || tLRPC$TL_theme.settings.size() <= 0) ? null : accent.info.settings.get(0);
            if (accent.info != null && tLRPC$ThemeSettings != null && (tLRPC$WallPaper = tLRPC$ThemeSettings.wallpaper) != null) {
                tLRPC$Document2 = tLRPC$WallPaper.document;
            }
            tLRPC$Document = tLRPC$Document2;
            file = pathToWallpaper;
            z = z3;
        } else {
            file = null;
            tLRPC$Document = null;
            z = false;
        }
        ThemeInfo themeInfo2 = currentTheme;
        final OverrideWallpaperInfo overrideWallpaperInfo = themeInfo2.overrideWallpaper;
        if (overrideWallpaperInfo != null) {
            f2 = overrideWallpaperInfo.intensity;
        } else if (accent != null) {
            f2 = accent.patternIntensity;
        } else {
            f = themeInfo2.patternIntensity;
            final int i = (int) f;
            DispatchQueue dispatchQueue = Utilities.themeQueue;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    Theme.lambda$loadWallpaper$8(Theme.OverrideWallpaperInfo.this, file, i, z2, z, tLRPC$Document);
                }
            };
            wallpaperLoadTask = runnable;
            dispatchQueue.postRunnable(runnable);
        }
        f = f2 * 100.0f;
        final int i2 = (int) f;
        DispatchQueue dispatchQueue2 = Utilities.themeQueue;
        Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$loadWallpaper$8(Theme.OverrideWallpaperInfo.this, file, i2, z2, z, tLRPC$Document);
            }
        };
        wallpaperLoadTask = runnable2;
        dispatchQueue2.postRunnable(runnable2);
    }

    public static /* synthetic */ void lambda$loadWallpaper$8(OverrideWallpaperInfo overrideWallpaperInfo, File file, int i, boolean z, boolean z2, TLRPC$Document tLRPC$Document) {
        BackgroundDrawableSettings createBackgroundDrawable = createBackgroundDrawable(currentTheme, overrideWallpaperInfo, currentColors, file, themedWallpaperLink, themedWallpaperFileOffset, i, previousPhase, z, hasPreviousTheme, isApplyingAccent, z2, tLRPC$Document);
        Boolean bool = createBackgroundDrawable.isWallpaperMotion;
        isWallpaperMotion = bool != null ? bool.booleanValue() : isWallpaperMotion;
        Boolean bool2 = createBackgroundDrawable.isPatternWallpaper;
        isPatternWallpaper = bool2 != null ? bool2.booleanValue() : isPatternWallpaper;
        Boolean bool3 = createBackgroundDrawable.isCustomTheme;
        isCustomTheme = bool3 != null ? bool3.booleanValue() : isCustomTheme;
        final Drawable drawable = createBackgroundDrawable.wallpaper;
        wallpaper = drawable != null ? drawable : wallpaper;
        calcBackgroundColor(drawable, 1);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$loadWallpaper$7(drawable);
            }
        });
    }

    public static /* synthetic */ void lambda$loadWallpaper$7(Drawable drawable) {
        wallpaperLoadTask = null;
        createCommonChatResources();
        applyChatServiceMessageColor(null, null, drawable);
        applyChatMessageSelectedBackgroundColor(null, drawable);
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper, new Object[0]);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x004d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BackgroundDrawableSettings createBackgroundDrawable(ThemeInfo themeInfo, HashMap<String, Integer> hashMap, String str, int i) {
        float f;
        float f2;
        boolean z = themeInfo.firstAccentIsDefault && themeInfo.currentAccentId == DEFALT_THEME_ACCENT_ID;
        ThemeAccent accent = themeInfo.getAccent(false);
        File pathToWallpaper = accent != null ? accent.getPathToWallpaper() : null;
        boolean z2 = accent != null && accent.patternMotion;
        OverrideWallpaperInfo overrideWallpaperInfo = themeInfo.overrideWallpaper;
        if (overrideWallpaperInfo != null) {
            f2 = overrideWallpaperInfo.intensity;
        } else if (accent != null) {
            f2 = accent.patternIntensity;
        } else {
            f = themeInfo.patternIntensity;
            int i2 = (int) f;
            Integer num = currentColorsNoAccent.get("wallpaperFileOffset");
            return createBackgroundDrawable(themeInfo, overrideWallpaperInfo, hashMap, pathToWallpaper, str, num == null ? num.intValue() : -1, i2, i, z, false, false, z2, null);
        }
        f = f2 * 100.0f;
        int i22 = (int) f;
        Integer num2 = currentColorsNoAccent.get("wallpaperFileOffset");
        return createBackgroundDrawable(themeInfo, overrideWallpaperInfo, hashMap, pathToWallpaper, str, num2 == null ? num2.intValue() : -1, i22, i, z, false, false, z2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:128:0x0313  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BackgroundDrawableSettings createBackgroundDrawable(ThemeInfo themeInfo, OverrideWallpaperInfo overrideWallpaperInfo, HashMap<String, Integer> hashMap, File file, String str, int i, int i2, int i3, boolean z, boolean z2, boolean z3, boolean z4, TLRPC$Document tLRPC$Document) {
        Bitmap loadScreenSizedBitmap;
        File file2;
        Bitmap bitmap;
        BackgroundDrawableSettings backgroundDrawableSettings = new BackgroundDrawableSettings();
        backgroundDrawableSettings.wallpaper = wallpaper;
        boolean z5 = (!z2 || z3) && overrideWallpaperInfo != null;
        if (overrideWallpaperInfo != null) {
            backgroundDrawableSettings.isWallpaperMotion = Boolean.valueOf(overrideWallpaperInfo.isMotion);
            backgroundDrawableSettings.isPatternWallpaper = Boolean.valueOf(overrideWallpaperInfo.color != 0 && !overrideWallpaperInfo.isDefault() && !overrideWallpaperInfo.isColor());
        } else {
            backgroundDrawableSettings.isWallpaperMotion = Boolean.valueOf(themeInfo.isMotion);
            backgroundDrawableSettings.isPatternWallpaper = Boolean.valueOf(themeInfo.patternBgColor != 0);
        }
        if (!z5) {
            Integer num = z ? null : hashMap.get("chat_wallpaper");
            Integer num2 = hashMap.get("key_chat_wallpaper_gradient_to3");
            if (num2 == null) {
                num2 = 0;
            }
            hashMap.get("key_chat_wallpaper_gradient_to2");
            Integer num3 = hashMap.get("key_chat_wallpaper_gradient_to2");
            Integer num4 = hashMap.get("chat_wallpaper_gradient_to");
            if (file != null && file.exists()) {
                try {
                    if (num != null && num4 != null && num3 != null) {
                        MotionBackgroundDrawable motionBackgroundDrawable = new MotionBackgroundDrawable(num.intValue(), num4.intValue(), num3.intValue(), num2.intValue(), false);
                        motionBackgroundDrawable.setPatternBitmap(i2, BitmapFactory.decodeFile(file.getAbsolutePath()));
                        motionBackgroundDrawable.setPatternColorFilter(motionBackgroundDrawable.getPatternColor());
                        backgroundDrawableSettings.wallpaper = motionBackgroundDrawable;
                    } else {
                        backgroundDrawableSettings.wallpaper = Drawable.createFromPath(file.getAbsolutePath());
                    }
                    backgroundDrawableSettings.isWallpaperMotion = Boolean.valueOf(z4);
                    Boolean bool = Boolean.TRUE;
                    backgroundDrawableSettings.isPatternWallpaper = bool;
                    backgroundDrawableSettings.isCustomTheme = bool;
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            } else if (num != null) {
                Integer num5 = hashMap.get("chat_wallpaper_gradient_rotation");
                if (num5 == null) {
                    num5 = 45;
                }
                if (num4 != null && num3 != null) {
                    MotionBackgroundDrawable motionBackgroundDrawable2 = new MotionBackgroundDrawable(num.intValue(), num4.intValue(), num3.intValue(), num2.intValue(), false);
                    if (file == null || tLRPC$Document == null) {
                        bitmap = null;
                    } else {
                        bitmap = SvgHelper.getBitmap(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$Document, true), AndroidUtilities.dp(360.0f), AndroidUtilities.dp(640.0f), false);
                        if (bitmap != null) {
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                                fileOutputStream.close();
                            } catch (Exception e) {
                                FileLog.e(e);
                                e.printStackTrace();
                            }
                        }
                    }
                    motionBackgroundDrawable2.setPatternBitmap(i2, bitmap);
                    motionBackgroundDrawable2.setPhase(i3);
                    backgroundDrawableSettings.wallpaper = motionBackgroundDrawable2;
                } else if (num4 == null || num4.equals(num)) {
                    backgroundDrawableSettings.wallpaper = new ColorDrawable(num.intValue());
                } else {
                    BackgroundGradientDrawable backgroundGradientDrawable = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(num5.intValue()), new int[]{num.intValue(), num4.intValue()});
                    backgroundGradientDisposable = backgroundGradientDrawable.startDithering(BackgroundGradientDrawable.Sizes.ofDeviceScreen(), new BackgroundGradientDrawable.ListenerAdapter() { // from class: org.telegram.ui.ActionBar.Theme.11
                        @Override // org.telegram.ui.Components.BackgroundGradientDrawable.ListenerAdapter, org.telegram.ui.Components.BackgroundGradientDrawable.Listener
                        public void onSizeReady(int i4, int i5) {
                            Point point = AndroidUtilities.displaySize;
                            boolean z6 = true;
                            boolean z7 = point.x <= point.y;
                            if (i4 > i5) {
                                z6 = false;
                            }
                            if (z7 == z6) {
                                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper, new Object[0]);
                            }
                        }
                    }, 100L);
                    backgroundDrawableSettings.wallpaper = backgroundGradientDrawable;
                }
                backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
            } else if (str != null) {
                try {
                    File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                    Bitmap loadScreenSizedBitmap2 = loadScreenSizedBitmap(new FileInputStream(new File(filesDirFixed, Utilities.MD5(str) + ".wp")), 0);
                    if (loadScreenSizedBitmap2 != null) {
                        backgroundDrawableSettings.wallpaper = new BitmapDrawable(loadScreenSizedBitmap2);
                        backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            } else if (i > 0 && (themeInfo.pathToFile != null || themeInfo.assetName != null)) {
                try {
                    String str2 = themeInfo.assetName;
                    if (str2 != null) {
                        file2 = getAssetFile(str2);
                    } else {
                        file2 = new File(themeInfo.pathToFile);
                    }
                    Bitmap loadScreenSizedBitmap3 = loadScreenSizedBitmap(new FileInputStream(file2), i);
                    if (loadScreenSizedBitmap3 != null) {
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(loadScreenSizedBitmap3);
                        wallpaper = bitmapDrawable;
                        backgroundDrawableSettings.wallpaper = bitmapDrawable;
                        bitmapDrawable.setFilterBitmap(true);
                        backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
                    }
                } catch (Throwable th2) {
                    FileLog.e(th2);
                }
            }
        }
        if (backgroundDrawableSettings.wallpaper == null) {
            int i4 = overrideWallpaperInfo != null ? overrideWallpaperInfo.color : 0;
            if (overrideWallpaperInfo != null) {
                if (!overrideWallpaperInfo.isDefault()) {
                    if (!overrideWallpaperInfo.isColor() || overrideWallpaperInfo.gradientColor1 != 0) {
                        if (i4 != 0 && (!isPatternWallpaper || overrideWallpaperInfo.gradientColor2 != 0)) {
                            int i5 = overrideWallpaperInfo.gradientColor1;
                            if (i5 != 0 && overrideWallpaperInfo.gradientColor2 != 0) {
                                MotionBackgroundDrawable motionBackgroundDrawable3 = new MotionBackgroundDrawable(overrideWallpaperInfo.color, overrideWallpaperInfo.gradientColor1, overrideWallpaperInfo.gradientColor2, overrideWallpaperInfo.gradientColor3, false);
                                motionBackgroundDrawable3.setPhase(i3);
                                if (backgroundDrawableSettings.isPatternWallpaper.booleanValue()) {
                                    File file3 = new File(ApplicationLoader.getFilesDirFixed(), overrideWallpaperInfo.fileName);
                                    if (file3.exists()) {
                                        motionBackgroundDrawable3.setPatternBitmap((int) (overrideWallpaperInfo.intensity * 100.0f), loadScreenSizedBitmap(new FileInputStream(file3), 0));
                                        backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
                                    }
                                }
                                backgroundDrawableSettings.wallpaper = motionBackgroundDrawable3;
                            } else if (i5 != 0) {
                                BackgroundGradientDrawable backgroundGradientDrawable2 = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(overrideWallpaperInfo.rotation), new int[]{i4, i5});
                                backgroundGradientDisposable = backgroundGradientDrawable2.startDithering(BackgroundGradientDrawable.Sizes.ofDeviceScreen(), new BackgroundGradientDrawable.ListenerAdapter() { // from class: org.telegram.ui.ActionBar.Theme.12
                                    @Override // org.telegram.ui.Components.BackgroundGradientDrawable.ListenerAdapter, org.telegram.ui.Components.BackgroundGradientDrawable.Listener
                                    public void onSizeReady(int i6, int i7) {
                                        Point point = AndroidUtilities.displaySize;
                                        boolean z6 = true;
                                        boolean z7 = point.x <= point.y;
                                        if (i6 > i7) {
                                            z6 = false;
                                        }
                                        if (z7 == z6) {
                                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper, new Object[0]);
                                        }
                                    }
                                }, 100L);
                                backgroundDrawableSettings.wallpaper = backgroundGradientDrawable2;
                            } else {
                                backgroundDrawableSettings.wallpaper = new ColorDrawable(i4);
                            }
                        } else {
                            File file4 = new File(ApplicationLoader.getFilesDirFixed(), overrideWallpaperInfo.fileName);
                            if (file4.exists() && (loadScreenSizedBitmap = loadScreenSizedBitmap(new FileInputStream(file4), 0)) != null) {
                                BitmapDrawable bitmapDrawable2 = new BitmapDrawable(loadScreenSizedBitmap);
                                backgroundDrawableSettings.wallpaper = bitmapDrawable2;
                                bitmapDrawable2.setFilterBitmap(true);
                                backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
                            }
                            if (backgroundDrawableSettings.wallpaper == null) {
                                backgroundDrawableSettings.wallpaper = createDefaultWallpaper();
                                backgroundDrawableSettings.isCustomTheme = Boolean.FALSE;
                            }
                        }
                    }
                    if (backgroundDrawableSettings.wallpaper == null) {
                        if (i4 == 0) {
                            i4 = -2693905;
                        }
                        backgroundDrawableSettings.wallpaper = new ColorDrawable(i4);
                    }
                }
            }
            backgroundDrawableSettings.wallpaper = createDefaultWallpaper();
            backgroundDrawableSettings.isCustomTheme = Boolean.FALSE;
            if (backgroundDrawableSettings.wallpaper == null) {
            }
        }
        return backgroundDrawableSettings;
    }

    public static Drawable createDefaultWallpaper() {
        return createDefaultWallpaper(0, 0);
    }

    public static Drawable createDefaultWallpaper(int i, int i2) {
        MotionBackgroundDrawable motionBackgroundDrawable = new MotionBackgroundDrawable(-2368069, -9722489, -2762611, -7817084, i != 0);
        if (i <= 0 || i2 <= 0) {
            Point point = AndroidUtilities.displaySize;
            i = Math.min(point.x, point.y);
            Point point2 = AndroidUtilities.displaySize;
            i2 = Math.max(point2.x, point2.y);
        }
        motionBackgroundDrawable.setPatternBitmap(34, SvgHelper.getBitmap((int) R.raw.default_pattern, i, i2, -16777216));
        motionBackgroundDrawable.setPatternColorFilter(motionBackgroundDrawable.getPatternColor());
        return motionBackgroundDrawable;
    }

    public static Bitmap loadScreenSizedBitmap(FileInputStream fileInputStream, int i) {
        float f;
        try {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                options.inJustDecodeBounds = true;
                long j = i;
                fileInputStream.getChannel().position(j);
                BitmapFactory.decodeStream(fileInputStream, null, options);
                float f2 = options.outWidth;
                float f3 = options.outHeight;
                Point point = AndroidUtilities.displaySize;
                int min = Math.min(point.x, point.y);
                Point point2 = AndroidUtilities.displaySize;
                int max = Math.max(point2.x, point2.y);
                if (min >= max && f2 > f3) {
                    f = Math.max(f2 / min, f3 / max);
                } else {
                    f = Math.min(f2 / min, f3 / max);
                }
                if (f < 1.2f) {
                    f = 1.0f;
                }
                options.inJustDecodeBounds = false;
                if (f > 1.0f && (f2 > min || f3 > max)) {
                    int i2 = 1;
                    do {
                        i2 *= 2;
                    } while (i2 * 2 < f);
                    options.inSampleSize = i2;
                } else {
                    options.inSampleSize = (int) f;
                }
                fileInputStream.getChannel().position(j);
                Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream, null, options);
                if (decodeStream.getWidth() < min || decodeStream.getHeight() < max) {
                    float max2 = Math.max(min / decodeStream.getWidth(), max / decodeStream.getHeight());
                    if (max2 >= 1.02f) {
                        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeStream, (int) (decodeStream.getWidth() * max2), (int) (decodeStream.getHeight() * max2), true);
                        decodeStream.recycle();
                        try {
                            fileInputStream.close();
                        } catch (Exception unused) {
                        }
                        return createScaledBitmap;
                    }
                }
                try {
                    fileInputStream.close();
                } catch (Exception unused2) {
                }
                return decodeStream;
            } catch (Exception e) {
                FileLog.e(e);
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception unused3) {
                    }
                }
                return null;
            }
        } catch (Throwable th) {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception unused4) {
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:2|(3:4|(1:6)|(2:8|9)(4:10|(1:20)|(4:22|(1:24)(1:25)|26|(1:28))(1:(5:30|(1:32)(1:33)|(1:35)|36|37)(1:38))|39))(3:40|(2:42|(6:46|(1:48)(1:49)|50|100|(7:109|54|106|55|(2:57|(2:58|(1:62)(1:111)))(0)|63|(6:65|(1:67)(1:68)|69|107|70|73)(1:(4:75|104|76|79)(2:80|99)))|96))|51)|52|100|(0)|96|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x017a, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x017b, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:109:0x00f8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Drawable getThemedWallpaper(final boolean z, final View view) {
        File file;
        MotionBackgroundDrawable motionBackgroundDrawable;
        int i;
        FileInputStream fileInputStream;
        Throwable th;
        Bitmap decodeStream;
        MotionBackgroundDrawable motionBackgroundDrawable2;
        Integer num = currentColors.get("chat_wallpaper");
        int i2 = 1;
        BackgroundGradientDrawable.ListenerAdapter listenerAdapter = null;
        if (num != null) {
            Integer num2 = currentColors.get("chat_wallpaper_gradient_to");
            Integer num3 = currentColors.get("key_chat_wallpaper_gradient_to2");
            Integer num4 = currentColors.get("key_chat_wallpaper_gradient_to3");
            Integer num5 = currentColors.get("chat_wallpaper_gradient_rotation");
            if (num5 == null) {
                num5 = 45;
            }
            if (num2 == null) {
                return new ColorDrawable(num.intValue());
            }
            ThemeAccent accent = currentTheme.getAccent(false);
            if (accent == null || TextUtils.isEmpty(accent.patternSlug) || previousTheme != null || (file = accent.getPathToWallpaper()) == null || !file.exists()) {
                file = null;
            }
            if (num3 != null) {
                motionBackgroundDrawable2 = new MotionBackgroundDrawable(num.intValue(), num2.intValue(), num3.intValue(), num4 != null ? num4.intValue() : 0, true);
                if (file == null) {
                    return motionBackgroundDrawable2;
                }
            } else if (file == null) {
                BackgroundGradientDrawable backgroundGradientDrawable = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(num5.intValue()), new int[]{num.intValue(), num2.intValue()});
                BackgroundGradientDrawable.Sizes ofDeviceScreen = !z ? BackgroundGradientDrawable.Sizes.ofDeviceScreen() : BackgroundGradientDrawable.Sizes.ofDeviceScreen(0.125f, BackgroundGradientDrawable.Sizes.Orientation.PORTRAIT);
                if (view != null) {
                    listenerAdapter = new BackgroundGradientDrawable.ListenerAdapter() { // from class: org.telegram.ui.ActionBar.Theme.13
                        @Override // org.telegram.ui.Components.BackgroundGradientDrawable.ListenerAdapter, org.telegram.ui.Components.BackgroundGradientDrawable.Listener
                        public void onSizeReady(int i3, int i4) {
                            if (!z) {
                                Point point = AndroidUtilities.displaySize;
                                boolean z2 = true;
                                boolean z3 = point.x <= point.y;
                                if (i3 > i4) {
                                    z2 = false;
                                }
                                if (z3 != z2) {
                                    return;
                                }
                                view.invalidate();
                                return;
                            }
                            view.invalidate();
                        }
                    };
                }
                backgroundGradientDrawable.startDithering(ofDeviceScreen, listenerAdapter);
                return backgroundGradientDrawable;
            } else {
                motionBackgroundDrawable2 = null;
            }
            motionBackgroundDrawable = motionBackgroundDrawable2;
        } else {
            if (themedWallpaperFileOffset > 0) {
                ThemeInfo themeInfo = currentTheme;
                if (themeInfo.pathToFile != null || themeInfo.assetName != null) {
                    String str = themeInfo.assetName;
                    file = str != null ? getAssetFile(str) : new File(currentTheme.pathToFile);
                    i = themedWallpaperFileOffset;
                    motionBackgroundDrawable = null;
                    if (file != null) {
                        try {
                            fileInputStream = new FileInputStream(file);
                            try {
                                fileInputStream.getChannel().position(i);
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                if (z) {
                                    options.inJustDecodeBounds = true;
                                    float f = options.outWidth;
                                    float f2 = options.outHeight;
                                    int dp = AndroidUtilities.dp(100.0f);
                                    while (true) {
                                        float f3 = dp;
                                        if (f <= f3 && f2 <= f3) {
                                            break;
                                        }
                                        i2 *= 2;
                                        f /= 2.0f;
                                        f2 /= 2.0f;
                                    }
                                }
                                options.inJustDecodeBounds = false;
                                options.inSampleSize = i2;
                                decodeStream = BitmapFactory.decodeStream(fileInputStream, null, options);
                            } catch (Throwable th2) {
                                th = th2;
                                try {
                                    FileLog.e(th);
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    return null;
                                } catch (Throwable th3) {
                                    if (fileInputStream != null) {
                                        try {
                                            fileInputStream.close();
                                        } catch (Exception e) {
                                            FileLog.e(e);
                                        }
                                    }
                                    throw th3;
                                }
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            fileInputStream = null;
                        }
                        if (motionBackgroundDrawable != null) {
                            ThemeAccent accent2 = currentTheme.getAccent(false);
                            motionBackgroundDrawable.setPatternBitmap(accent2 != null ? (int) (accent2.patternIntensity * 100.0f) : 100, decodeStream);
                            motionBackgroundDrawable.setPatternColorFilter(motionBackgroundDrawable.getPatternColor());
                            try {
                                fileInputStream.close();
                            } catch (Exception e2) {
                                FileLog.e(e2);
                            }
                            return motionBackgroundDrawable;
                        } else if (decodeStream != null) {
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(decodeStream);
                            try {
                                fileInputStream.close();
                            } catch (Exception e3) {
                                FileLog.e(e3);
                            }
                            return bitmapDrawable;
                        } else {
                            fileInputStream.close();
                        }
                    }
                    return null;
                }
            }
            motionBackgroundDrawable = null;
            file = null;
        }
        i = 0;
        if (file != null) {
        }
        return null;
    }

    public static String getSelectedBackgroundSlug() {
        OverrideWallpaperInfo overrideWallpaperInfo = currentTheme.overrideWallpaper;
        if (overrideWallpaperInfo != null) {
            return overrideWallpaperInfo.slug;
        }
        return hasWallpaperFromTheme() ? "t" : "d";
    }

    public static Drawable getCachedWallpaper() {
        Drawable cachedWallpaperNonBlocking = getCachedWallpaperNonBlocking();
        if (cachedWallpaperNonBlocking != null || wallpaperLoadTask == null) {
            return cachedWallpaperNonBlocking;
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Utilities.themeQueue.postRunnable(new Theme$$ExternalSyntheticLambda2(countDownLatch));
        try {
            countDownLatch.await();
        } catch (Exception e) {
            FileLog.e(e);
        }
        return getCachedWallpaperNonBlocking();
    }

    public static Drawable getCachedWallpaperNonBlocking() {
        Drawable drawable = themedWallpaper;
        return drawable != null ? drawable : wallpaper;
    }

    public static boolean isWallpaperMotion() {
        return isWallpaperMotion;
    }

    public static boolean isPatternWallpaper() {
        String selectedBackgroundSlug = getSelectedBackgroundSlug();
        return isPatternWallpaper || "CJz3BZ6YGEYBAAAABboWp6SAv04".equals(selectedBackgroundSlug) || "qeZWES8rGVIEAAAARfWlK1lnfiI".equals(selectedBackgroundSlug);
    }

    public static BackgroundGradientDrawable getCurrentGradientWallpaper() {
        int i;
        int i2;
        OverrideWallpaperInfo overrideWallpaperInfo = currentTheme.overrideWallpaper;
        if (overrideWallpaperInfo == null || (i = overrideWallpaperInfo.color) == 0 || (i2 = overrideWallpaperInfo.gradientColor1) == 0) {
            return null;
        }
        return new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(overrideWallpaperInfo.rotation), new int[]{i, i2});
    }

    public static AudioVisualizerDrawable getCurrentAudiVisualizerDrawable() {
        if (chat_msgAudioVisualizeDrawable == null) {
            chat_msgAudioVisualizeDrawable = new AudioVisualizerDrawable();
        }
        return chat_msgAudioVisualizeDrawable;
    }

    public static void unrefAudioVisualizeDrawable(final MessageObject messageObject) {
        AudioVisualizerDrawable audioVisualizerDrawable = chat_msgAudioVisualizeDrawable;
        if (audioVisualizerDrawable == null) {
            return;
        }
        if (audioVisualizerDrawable.getParentView() == null || messageObject == null) {
            chat_msgAudioVisualizeDrawable.setParentView(null);
            return;
        }
        if (animatedOutVisualizerDrawables == null) {
            animatedOutVisualizerDrawables = new HashMap<>();
        }
        animatedOutVisualizerDrawables.put(messageObject, chat_msgAudioVisualizeDrawable);
        chat_msgAudioVisualizeDrawable.setWaveform(false, true, null);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$unrefAudioVisualizeDrawable$9(MessageObject.this);
            }
        }, 200L);
        chat_msgAudioVisualizeDrawable = null;
    }

    public static /* synthetic */ void lambda$unrefAudioVisualizeDrawable$9(MessageObject messageObject) {
        AudioVisualizerDrawable remove = animatedOutVisualizerDrawables.remove(messageObject);
        if (remove != null) {
            remove.setParentView(null);
        }
    }

    public static AudioVisualizerDrawable getAnimatedOutAudioVisualizerDrawable(MessageObject messageObject) {
        HashMap<MessageObject, AudioVisualizerDrawable> hashMap = animatedOutVisualizerDrawables;
        if (hashMap == null || messageObject == null) {
            return null;
        }
        return hashMap.get(messageObject);
    }

    public static StatusDrawable getChatStatusDrawable(int i) {
        if (i < 0 || i > 5) {
            return null;
        }
        StatusDrawable[] statusDrawableArr = chat_status_drawables;
        StatusDrawable statusDrawable = statusDrawableArr[i];
        if (statusDrawable != null) {
            return statusDrawable;
        }
        if (i == 0) {
            statusDrawableArr[0] = new TypingDotsDrawable(true);
        } else if (i == 1) {
            statusDrawableArr[1] = new RecordStatusDrawable(true);
        } else if (i == 2) {
            statusDrawableArr[2] = new SendingFileDrawable(true);
        } else if (i == 3) {
            statusDrawableArr[3] = new PlayingGameDrawable(true, null);
        } else if (i == 4) {
            statusDrawableArr[4] = new RoundStatusDrawable(true);
        } else if (i == 5) {
            statusDrawableArr[5] = new ChoosingStickerStatusDrawable(true);
        }
        StatusDrawable statusDrawable2 = chat_status_drawables[i];
        statusDrawable2.start();
        statusDrawable2.setColor(getColor("chats_actionMessage"));
        return statusDrawable2;
    }

    public static FragmentContextViewWavesDrawable getFragmentContextViewWavesDrawable() {
        if (fragmentContextViewWavesDrawable == null) {
            fragmentContextViewWavesDrawable = new FragmentContextViewWavesDrawable();
        }
        return fragmentContextViewWavesDrawable;
    }

    public static RoundVideoProgressShadow getRadialSeekbarShadowDrawable() {
        if (roundPlayDrawable == null) {
            roundPlayDrawable = new RoundVideoProgressShadow();
        }
        return roundPlayDrawable;
    }

    public static HashMap<String, String> getFallbackKeys() {
        return fallbackKeys;
    }

    public static String getFallbackKey(String str) {
        return fallbackKeys.get(str);
    }

    public static Map<String, Drawable> getThemeDrawablesMap() {
        return defaultChatDrawables;
    }

    public static Drawable getThemeDrawable(String str) {
        return defaultChatDrawables.get(str);
    }

    public static String getThemeDrawableColorKey(String str) {
        return defaultChatDrawableColorKeys.get(str);
    }

    public static Map<String, Paint> getThemePaintsMap() {
        return defaultChatPaints;
    }

    public static Paint getThemePaint(String str) {
        return defaultChatPaints.get(str);
    }

    public static String getThemePaintColorKey(String str) {
        return defaultChatPaintColors.get(str);
    }

    private static void addChatDrawable(String str, Drawable drawable, String str2) {
        defaultChatDrawables.put(str, drawable);
        if (str2 != null) {
            defaultChatDrawableColorKeys.put(str, str2);
        }
    }

    private static void addChatPaint(String str, Paint paint, String str2) {
        defaultChatPaints.put(str, paint);
        if (str2 != null) {
            defaultChatPaintColors.put(str, str2);
        }
    }

    public static boolean isCurrentThemeDay() {
        return !getActiveTheme().isDark();
    }

    public static boolean isHome(ThemeAccent themeAccent) {
        ThemeInfo themeInfo = themeAccent.parentTheme;
        if (themeInfo != null) {
            if (themeInfo.getKey().equals("Blue") && themeAccent.id == 99) {
                return true;
            }
            if (themeAccent.parentTheme.getKey().equals("Day") && themeAccent.id == 9) {
                return true;
            }
            return (themeAccent.parentTheme.getKey().equals("Night") || themeAccent.parentTheme.getKey().equals("Dark Blue")) && themeAccent.id == 0;
        }
        return false;
    }
}
