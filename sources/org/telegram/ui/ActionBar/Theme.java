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
import android.graphics.drawable.shapes.RectShape;
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
import android.util.SparseIntArray;
import android.util.StateSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
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
import java.util.Locale;
import java.util.Map;
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
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesController$$ExternalSyntheticLambda242;
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
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AudioVisualizerDrawable;
import org.telegram.ui.Components.BackgroundGradientDrawable;
import org.telegram.ui.Components.BulletinFactory;
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
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.RoundVideoProgressShadow;
import org.telegram.ui.ThemeActivity;
/* loaded from: classes3.dex */
public class Theme {
    public static Paint DEBUG_BLUE;
    public static Paint DEBUG_RED;
    private static Method StateListDrawable_getStateDrawableMethod;
    private static SensorEventListener ambientSensorListener;
    private static HashMap<MessageObject, AudioVisualizerDrawable> animatedOutVisualizerDrawables;
    private static SparseIntArray animatingColors;
    public static float autoNightBrighnessThreshold;
    public static String autoNightCityName;
    public static int autoNightDayEndTime;
    public static int autoNightDayStartTime;
    public static int autoNightLastSunCheckDay;
    public static double autoNightLocationLatitude;
    public static double autoNightLocationLongitude;
    public static boolean autoNightScheduleByLocation;
    public static int autoNightSunriseTime;
    public static int autoNightSunsetTime;
    public static Paint avatar_backgroundPaint;
    private static BackgroundGradientDrawable.Disposable backgroundGradientDisposable;
    public static Drawable calllog_msgCallDownGreenDrawable;
    public static Drawable calllog_msgCallDownRedDrawable;
    public static Drawable calllog_msgCallUpGreenDrawable;
    public static Drawable calllog_msgCallUpRedDrawable;
    private static boolean canStartHolidayAnimation;
    private static boolean changingWallpaper;
    public static Paint chat_actionBackgroundGradientDarkenPaint;
    public static Paint chat_actionBackgroundPaint;
    public static Paint chat_actionBackgroundPaint2;
    public static Paint chat_actionBackgroundSelectedPaint;
    public static TextPaint chat_actionTextPaint;
    public static TextPaint chat_actionTextPaint2;
    public static TextPaint chat_adminPaint;
    public static PorterDuffColorFilter chat_animatedEmojiTextColorFilter;
    public static Drawable chat_attachEmptyDrawable;
    public static TextPaint chat_audioPerformerPaint;
    public static TextPaint chat_audioTimePaint;
    public static TextPaint chat_audioTitlePaint;
    public static TextPaint chat_botButtonPaint;
    public static Drawable chat_botCardDrawable;
    public static Drawable chat_botInlineDrawable;
    public static Drawable chat_botInviteDrawable;
    public static Drawable chat_botLinkDrawable;
    public static Drawable chat_botWebViewDrawable;
    public static Drawable chat_commentArrowDrawable;
    public static Drawable chat_commentDrawable;
    public static Drawable chat_commentStickerDrawable;
    public static TextPaint chat_commentTextPaint;
    public static Paint chat_composeBackgroundPaint;
    public static Drawable chat_composeShadowDrawable;
    public static Drawable chat_composeShadowRoundDrawable;
    public static TextPaint chat_contactNamePaint;
    public static TextPaint chat_contactPhonePaint;
    public static TextPaint chat_contextResult_descriptionTextPaint;
    public static Drawable chat_contextResult_shadowUnderSwitchDrawable;
    public static TextPaint chat_contextResult_titleTextPaint;
    public static Paint chat_deleteProgressPaint;
    public static Paint chat_docBackPaint;
    public static TextPaint chat_docNamePaint;
    public static TextPaint chat_durationPaint;
    public static Drawable chat_flameIcon;
    public static TextPaint chat_forwardNamePaint;
    public static TextPaint chat_gamePaint;
    public static Drawable chat_gifIcon;
    public static Drawable chat_goIconDrawable;
    public static Drawable chat_gradientLeftDrawable;
    public static Drawable chat_gradientRightDrawable;
    public static TextPaint chat_infoPaint;
    public static Drawable chat_inlineResultAudio;
    public static Drawable chat_inlineResultFile;
    public static Drawable chat_inlineResultLocation;
    public static TextPaint chat_instantViewPaint;
    public static Paint chat_instantViewRectPaint;
    public static TextPaint chat_livePaint;
    public static TextPaint chat_locationAddressPaint;
    public static TextPaint chat_locationTitlePaint;
    public static Drawable chat_lockIconDrawable;
    public static Paint chat_messageBackgroundSelectedPaint;
    private static AudioVisualizerDrawable chat_msgAudioVisualizeDrawable;
    public static Drawable chat_msgAvatarLiveLocationDrawable;
    public static TextPaint chat_msgBotButtonPaint;
    public static Drawable chat_msgCallDownGreenDrawable;
    public static Drawable chat_msgCallDownRedDrawable;
    public static Drawable chat_msgCallUpGreenDrawable;
    public static MsgClockDrawable chat_msgClockDrawable;
    public static Drawable chat_msgErrorDrawable;
    public static Paint chat_msgErrorPaint;
    public static TextPaint chat_msgGameTextPaint;
    public static MessageDrawable chat_msgInDrawable;
    public static Drawable chat_msgInInstantDrawable;
    public static MessageDrawable chat_msgInMediaDrawable;
    public static MessageDrawable chat_msgInMediaSelectedDrawable;
    public static Drawable chat_msgInMenuDrawable;
    public static Drawable chat_msgInMenuSelectedDrawable;
    public static Drawable chat_msgInPinnedDrawable;
    public static Drawable chat_msgInPinnedSelectedDrawable;
    public static Drawable chat_msgInRepliesDrawable;
    public static Drawable chat_msgInRepliesSelectedDrawable;
    public static MessageDrawable chat_msgInSelectedDrawable;
    public static Drawable chat_msgInViewsDrawable;
    public static Drawable chat_msgInViewsSelectedDrawable;
    public static Drawable chat_msgMediaCheckDrawable;
    public static Drawable chat_msgMediaHalfCheckDrawable;
    public static Drawable chat_msgMediaMenuDrawable;
    public static Drawable chat_msgMediaPinnedDrawable;
    public static Drawable chat_msgMediaRepliesDrawable;
    public static Drawable chat_msgMediaViewsDrawable;
    public static Drawable chat_msgNoSoundDrawable;
    public static Drawable chat_msgOutCheckDrawable;
    public static Drawable chat_msgOutCheckReadDrawable;
    public static Drawable chat_msgOutCheckReadSelectedDrawable;
    public static Drawable chat_msgOutCheckSelectedDrawable;
    public static MessageDrawable chat_msgOutDrawable;
    public static Drawable chat_msgOutHalfCheckDrawable;
    public static Drawable chat_msgOutHalfCheckSelectedDrawable;
    public static Drawable chat_msgOutInstantDrawable;
    public static MessageDrawable chat_msgOutMediaDrawable;
    public static MessageDrawable chat_msgOutMediaSelectedDrawable;
    public static Drawable chat_msgOutMenuDrawable;
    public static Drawable chat_msgOutMenuSelectedDrawable;
    public static Drawable chat_msgOutPinnedDrawable;
    public static Drawable chat_msgOutPinnedSelectedDrawable;
    public static Drawable chat_msgOutRepliesDrawable;
    public static Drawable chat_msgOutRepliesSelectedDrawable;
    public static MessageDrawable chat_msgOutSelectedDrawable;
    public static Drawable chat_msgOutViewsDrawable;
    public static Drawable chat_msgOutViewsSelectedDrawable;
    public static Drawable chat_msgStickerCheckDrawable;
    public static Drawable chat_msgStickerHalfCheckDrawable;
    public static Drawable chat_msgStickerPinnedDrawable;
    public static Drawable chat_msgStickerRepliesDrawable;
    public static Drawable chat_msgStickerViewsDrawable;
    public static TextPaint chat_msgTextPaint;
    public static TextPaint[] chat_msgTextPaintEmoji;
    public static TextPaint chat_msgTextPaintOneEmoji;
    public static TextPaint chat_msgTextPaintThreeEmoji;
    public static TextPaint chat_msgTextPaintTwoEmoji;
    public static Drawable chat_msgUnlockDrawable;
    public static Drawable chat_muteIconDrawable;
    public static TextPaint chat_namePaint;
    public static Paint chat_outUrlPaint;
    public static Paint chat_pollTimerPaint;
    public static Paint chat_radialProgress2Paint;
    public static Paint chat_radialProgressPaint;
    public static Paint chat_radialProgressPausedSeekbarPaint;
    public static Drawable chat_redLocationIcon;
    public static Drawable chat_replyIconDrawable;
    public static Paint chat_replyLinePaint;
    public static TextPaint chat_replyNamePaint;
    public static TextPaint chat_replyTextPaint;
    public static Drawable chat_roundVideoShadow;
    public static Drawable chat_shareIconDrawable;
    public static TextPaint chat_shipmentPaint;
    public static Paint chat_statusPaint;
    public static Paint chat_statusRecordPaint;
    public static TextPaint chat_stickerCommentCountPaint;
    public static Paint chat_textSearchSelectionPaint;
    public static Paint chat_timeBackgroundPaint;
    public static TextPaint chat_timePaint;
    public static TextPaint chat_topicTextPaint;
    public static TextPaint chat_unlockExtendedMediaTextPaint;
    public static Paint chat_urlPaint;
    public static Paint checkboxSquare_backgroundPaint;
    public static Paint checkboxSquare_checkPaint;
    public static Paint checkboxSquare_eraserPaint;
    public static int colorsCount;
    private static SparseIntArray currentColors;
    private static SparseIntArray currentColorsNoAccent;
    private static ThemeInfo currentDayTheme;
    private static ThemeInfo currentNightTheme;
    private static ThemeInfo currentTheme;
    private static final HashMap<String, Integer> defaultChatDrawableColorKeys;
    private static final HashMap<String, Drawable> defaultChatDrawables;
    private static final HashMap<String, Integer> defaultChatPaintColors;
    private static final HashMap<String, Paint> defaultChatPaints;
    private static int[] defaultColors;
    private static ThemeInfo defaultTheme;
    public static Paint dialogs_actionMessagePaint;
    public static RLottieDrawable dialogs_archiveAvatarDrawable;
    public static boolean dialogs_archiveAvatarDrawableRecolored;
    public static RLottieDrawable dialogs_archiveDrawable;
    public static boolean dialogs_archiveDrawableRecolored;
    public static TextPaint dialogs_archiveTextPaint;
    public static TextPaint dialogs_archiveTextPaintSmall;
    public static Drawable dialogs_checkDrawable;
    public static Drawable dialogs_checkReadDrawable;
    public static Drawable dialogs_clockDrawable;
    public static Paint dialogs_countGrayPaint;
    public static Paint dialogs_countPaint;
    public static TextPaint dialogs_countTextPaint;
    public static Drawable dialogs_errorDrawable;
    public static Paint dialogs_errorPaint;
    public static ScamDrawable dialogs_fakeDrawable;
    public static Drawable dialogs_forum_arrowDrawable;
    public static Drawable dialogs_halfCheckDrawable;
    public static RLottieDrawable dialogs_hidePsaDrawable;
    public static boolean dialogs_hidePsaDrawableRecolored;
    public static Drawable dialogs_holidayDrawable;
    private static int dialogs_holidayDrawableOffsetX;
    private static int dialogs_holidayDrawableOffsetY;
    public static Drawable dialogs_lock2Drawable;
    public static Drawable dialogs_lockDrawable;
    public static Drawable dialogs_mentionDrawable;
    public static TextPaint dialogs_messageNamePaint;
    public static TextPaint[] dialogs_messagePaint;
    public static TextPaint[] dialogs_messagePrintingPaint;
    public static Drawable dialogs_muteDrawable;
    public static TextPaint[] dialogs_nameEncryptedPaint;
    public static TextPaint[] dialogs_namePaint;
    public static TextPaint dialogs_offlinePaint;
    public static Paint dialogs_onlineCirclePaint;
    public static TextPaint dialogs_onlinePaint;
    public static RLottieDrawable dialogs_pinArchiveDrawable;
    public static Drawable dialogs_pinnedDrawable;
    public static Paint dialogs_pinnedPaint;
    public static Drawable dialogs_playDrawable;
    public static Paint dialogs_reactionsCountPaint;
    public static Drawable dialogs_reactionsMentionDrawable;
    public static Drawable dialogs_reorderDrawable;
    public static ScamDrawable dialogs_scamDrawable;
    public static TextPaint dialogs_searchNameEncryptedPaint;
    public static TextPaint dialogs_searchNamePaint;
    public static RLottieDrawable dialogs_swipeDeleteDrawable;
    public static RLottieDrawable dialogs_swipeMuteDrawable;
    public static RLottieDrawable dialogs_swipePinDrawable;
    public static RLottieDrawable dialogs_swipeReadDrawable;
    public static RLottieDrawable dialogs_swipeUnmuteDrawable;
    public static RLottieDrawable dialogs_swipeUnpinDrawable;
    public static RLottieDrawable dialogs_swipeUnreadDrawable;
    public static Paint dialogs_tabletSeletedPaint;
    public static TextPaint dialogs_timePaint;
    public static RLottieDrawable dialogs_unarchiveDrawable;
    public static Drawable dialogs_unmuteDrawable;
    public static RLottieDrawable dialogs_unpinArchiveDrawable;
    public static Drawable dialogs_verifiedCheckDrawable;
    public static Drawable dialogs_verifiedDrawable;
    public static boolean disallowChangeServiceMessageColor;
    public static Paint dividerExtraPaint;
    public static Paint dividerPaint;
    private static SparseIntArray fallbackKeys;
    private static FragmentContextViewWavesDrawable fragmentContextViewWavesDrawable;
    private static boolean hasPreviousTheme;
    private static ThreadLocal<float[]> hsvTemp1Local;
    private static ThreadLocal<float[]> hsvTemp2Local;
    private static ThreadLocal<float[]> hsvTemp3Local;
    private static ThreadLocal<float[]> hsvTemp4Local;
    private static ThreadLocal<float[]> hsvTemp5Local;
    private static boolean isApplyingAccent;
    private static boolean isCustomTheme;
    private static boolean isInNigthMode;
    private static boolean isPatternWallpaper;
    private static boolean isWallpaperMotion;
    public static final int key_actionBarActionModeDefault;
    public static final int key_actionBarActionModeDefaultIcon;
    public static final int key_actionBarActionModeDefaultSelector;
    public static final int key_actionBarActionModeDefaultTop;
    public static final int key_actionBarBrowser;
    public static final int key_actionBarDefault;
    public static final int key_actionBarDefaultArchived;
    public static final int key_actionBarDefaultArchivedIcon;
    public static final int key_actionBarDefaultArchivedSearch;
    public static final int key_actionBarDefaultArchivedSearchPlaceholder;
    public static final int key_actionBarDefaultArchivedSelector;
    public static final int key_actionBarDefaultArchivedTitle;
    public static final int key_actionBarDefaultIcon;
    public static final int key_actionBarDefaultSearch;
    public static final int key_actionBarDefaultSearchPlaceholder;
    public static final int key_actionBarDefaultSelector;
    public static final int key_actionBarDefaultSubmenuBackground;
    public static final int key_actionBarDefaultSubmenuItem;
    public static final int key_actionBarDefaultSubmenuItemIcon;
    public static final int key_actionBarDefaultSubmenuSeparator;
    public static final int key_actionBarDefaultSubtitle;
    public static final int key_actionBarDefaultTitle;
    public static final int key_actionBarTabActiveText;
    public static final int key_actionBarTabLine;
    public static final int key_actionBarTabSelector;
    public static final int key_actionBarTabUnactiveText;
    public static final int key_actionBarWhiteSelector;
    public static final int key_avatar_actionBarIconBlue;
    public static final int key_avatar_actionBarSelectorBlue;
    public static final int key_avatar_background2Blue;
    public static final int key_avatar_background2Cyan;
    public static final int key_avatar_background2Green;
    public static final int key_avatar_background2Orange;
    public static final int key_avatar_background2Pink;
    public static final int key_avatar_background2Red;
    public static final int key_avatar_background2Saved;
    public static final int key_avatar_background2Violet;
    public static final int key_avatar_backgroundActionBarBlue;
    public static final int key_avatar_backgroundArchived;
    public static final int key_avatar_backgroundArchivedHidden;
    public static final int key_avatar_backgroundBlue;
    public static final int key_avatar_backgroundCyan;
    public static final int key_avatar_backgroundGreen;
    public static final int key_avatar_backgroundInProfileBlue;
    public static final int key_avatar_backgroundOrange;
    public static final int key_avatar_backgroundPink;
    public static final int key_avatar_backgroundRed;
    public static final int key_avatar_backgroundSaved;
    public static final int key_avatar_backgroundViolet;
    public static final int key_avatar_nameInMessageBlue;
    public static final int key_avatar_nameInMessageCyan;
    public static final int key_avatar_nameInMessageGreen;
    public static final int key_avatar_nameInMessageOrange;
    public static final int key_avatar_nameInMessagePink;
    public static final int key_avatar_nameInMessageRed;
    public static final int key_avatar_nameInMessageViolet;
    public static final int key_avatar_subtitleInProfileBlue;
    public static final int key_avatar_text;
    public static final int key_calls_callReceivedGreenIcon;
    public static final int key_calls_callReceivedRedIcon;
    public static final int key_changephoneinfo_image2;
    public static final int key_chat_BlurAlpha;
    public static final int key_chat_TextSelectionCursor;
    public static final int key_chat_addContact;
    public static final int key_chat_attachActiveTab;
    public static final int key_chat_attachAudioBackground;
    public static final int key_chat_attachAudioText;
    public static final int key_chat_attachCheckBoxBackground;
    public static final int key_chat_attachCheckBoxCheck;
    public static final int key_chat_attachContactBackground;
    public static final int key_chat_attachContactText;
    public static final int key_chat_attachEmptyImage;
    public static final int key_chat_attachFileBackground;
    public static final int key_chat_attachFileText;
    public static final int key_chat_attachGalleryBackground;
    public static final int key_chat_attachGalleryText;
    public static final int key_chat_attachIcon;
    public static final int key_chat_attachLocationBackground;
    public static final int key_chat_attachLocationText;
    public static final int key_chat_attachPermissionImage;
    public static final int key_chat_attachPermissionMark;
    public static final int key_chat_attachPermissionText;
    public static final int key_chat_attachPhotoBackground;
    public static final int key_chat_attachPollBackground;
    public static final int key_chat_attachPollText;
    public static final int key_chat_attachUnactiveTab;
    public static final int key_chat_botButtonText;
    public static final int key_chat_botKeyboardButtonBackground;
    public static final int key_chat_botKeyboardButtonBackgroundPressed;
    public static final int key_chat_botKeyboardButtonText;
    public static final int key_chat_botSwitchToInlineText;
    public static final int key_chat_emojiBottomPanelIcon;
    public static final int key_chat_emojiPanelBackground;
    public static final int key_chat_emojiPanelBackspace;
    public static final int key_chat_emojiPanelEmptyText;
    public static final int key_chat_emojiPanelIcon;
    public static final int key_chat_emojiPanelIconSelected;
    public static final int key_chat_emojiPanelNewTrending;
    public static final int key_chat_emojiPanelShadowLine;
    public static final int key_chat_emojiPanelStickerPackSelector;
    public static final int key_chat_emojiPanelStickerPackSelectorLine;
    public static final int key_chat_emojiPanelStickerSetName;
    public static final int key_chat_emojiPanelStickerSetNameHighlight;
    public static final int key_chat_emojiPanelStickerSetNameIcon;
    public static final int key_chat_emojiPanelTrendingDescription;
    public static final int key_chat_emojiPanelTrendingTitle;
    public static final int key_chat_emojiSearchBackground;
    public static final int key_chat_emojiSearchIcon;
    public static final int key_chat_fieldOverlayText;
    public static final int key_chat_gifSaveHintBackground;
    public static final int key_chat_gifSaveHintText;
    public static final int key_chat_goDownButton;
    public static final int key_chat_goDownButtonCounter;
    public static final int key_chat_goDownButtonCounterBackground;
    public static final int key_chat_goDownButtonIcon;
    public static final int key_chat_inAdminSelectedText;
    public static final int key_chat_inAdminText;
    public static final int key_chat_inAudioCacheSeekbar;
    public static final int key_chat_inAudioDurationSelectedText;
    public static final int key_chat_inAudioDurationText;
    public static final int key_chat_inAudioPerformerSelectedText;
    public static final int key_chat_inAudioPerformerText;
    public static final int key_chat_inAudioProgress;
    public static final int key_chat_inAudioSeekbar;
    public static final int key_chat_inAudioSeekbarFill;
    public static final int key_chat_inAudioSeekbarSelected;
    public static final int key_chat_inAudioSelectedProgress;
    public static final int key_chat_inAudioTitleText;
    public static final int key_chat_inBubble;
    public static final int key_chat_inBubbleLocationPlaceholder;
    public static final int key_chat_inBubbleSelected;
    public static final int key_chat_inBubbleSelectedOverlay;
    public static final int key_chat_inBubbleShadow;
    public static final int key_chat_inContactBackground;
    public static final int key_chat_inContactIcon;
    public static final int key_chat_inContactNameText;
    public static final int key_chat_inContactPhoneSelectedText;
    public static final int key_chat_inContactPhoneText;
    public static final int key_chat_inFileBackground;
    public static final int key_chat_inFileBackgroundSelected;
    public static final int key_chat_inFileInfoSelectedText;
    public static final int key_chat_inFileInfoText;
    public static final int key_chat_inFileNameText;
    public static final int key_chat_inFileProgress;
    public static final int key_chat_inFileProgressSelected;
    public static final int key_chat_inForwardedNameText;
    public static final int key_chat_inGreenCall;
    public static final int key_chat_inInstant;
    public static final int key_chat_inInstantSelected;
    public static final int key_chat_inLoader;
    public static final int key_chat_inLoaderPhoto;
    public static final int key_chat_inLoaderSelected;
    public static final int key_chat_inLocationBackground;
    public static final int key_chat_inLocationIcon;
    public static final int key_chat_inMediaIcon;
    public static final int key_chat_inMediaIconSelected;
    public static final int key_chat_inMenu;
    public static final int key_chat_inMenuSelected;
    public static final int key_chat_inPollCorrectAnswer;
    public static final int key_chat_inPollWrongAnswer;
    public static final int key_chat_inPreviewInstantText;
    public static final int key_chat_inPreviewLine;
    public static final int key_chat_inPsaNameText;
    public static final int key_chat_inReactionButtonBackground;
    public static final int key_chat_inReactionButtonText;
    public static final int key_chat_inReactionButtonTextSelected;
    public static final int key_chat_inReplyLine;
    public static final int key_chat_inReplyMediaMessageSelectedText;
    public static final int key_chat_inReplyMediaMessageText;
    public static final int key_chat_inReplyMessageText;
    public static final int key_chat_inReplyNameText;
    public static final int key_chat_inSentClock;
    public static final int key_chat_inSentClockSelected;
    public static final int key_chat_inSiteNameText;
    public static final int key_chat_inTextSelectionHighlight;
    public static final int key_chat_inTimeSelectedText;
    public static final int key_chat_inTimeText;
    public static final int key_chat_inVenueInfoSelectedText;
    public static final int key_chat_inVenueInfoText;
    public static final int key_chat_inViaBotNameText;
    public static final int key_chat_inViews;
    public static final int key_chat_inViewsSelected;
    public static final int key_chat_inVoiceSeekbar;
    public static final int key_chat_inVoiceSeekbarFill;
    public static final int key_chat_inVoiceSeekbarSelected;
    public static final int key_chat_inlineResultIcon;
    public static final int key_chat_linkSelectBackground;
    public static final int key_chat_lockIcon;
    public static final int key_chat_mediaInfoText;
    public static final int key_chat_mediaLoaderPhoto;
    public static final int key_chat_mediaLoaderPhotoIcon;
    public static final int key_chat_mediaLoaderPhotoIconSelected;
    public static final int key_chat_mediaLoaderPhotoSelected;
    public static final int key_chat_mediaMenu;
    public static final int key_chat_mediaProgress;
    public static final int key_chat_mediaSentCheck;
    public static final int key_chat_mediaSentClock;
    public static final int key_chat_mediaTimeBackground;
    public static final int key_chat_mediaTimeText;
    public static final int key_chat_mediaViews;
    public static final int key_chat_messageLinkIn;
    public static final int key_chat_messageLinkOut;
    public static final int key_chat_messagePanelBackground;
    public static final int key_chat_messagePanelCancelInlineBot;
    public static final int key_chat_messagePanelCursor;
    public static final int key_chat_messagePanelHint;
    public static final int key_chat_messagePanelIcons;
    public static final int key_chat_messagePanelSend;
    public static final int key_chat_messagePanelShadow;
    public static final int key_chat_messagePanelText;
    public static final int key_chat_messagePanelVoiceBackground;
    public static final int key_chat_messagePanelVoiceDelete;
    public static final int key_chat_messagePanelVoiceDuration;
    public static final int key_chat_messagePanelVoiceLock;
    public static final int key_chat_messagePanelVoiceLockBackground;
    public static final int key_chat_messagePanelVoiceLockShadow;
    public static final int key_chat_messagePanelVoicePressed;
    public static final int key_chat_messageTextIn;
    public static final int key_chat_messageTextOut;
    public static final int key_chat_muteIcon;
    public static final int key_chat_outAdminSelectedText;
    public static final int key_chat_outAdminText;
    public static final int key_chat_outAudioCacheSeekbar;
    public static final int key_chat_outAudioDurationSelectedText;
    public static final int key_chat_outAudioDurationText;
    public static final int key_chat_outAudioPerformerSelectedText;
    public static final int key_chat_outAudioPerformerText;
    public static final int key_chat_outAudioProgress;
    public static final int key_chat_outAudioSeekbar;
    public static final int key_chat_outAudioSeekbarFill;
    public static final int key_chat_outAudioSeekbarSelected;
    public static final int key_chat_outAudioSelectedProgress;
    public static final int key_chat_outAudioTitleText;
    public static final int key_chat_outBubble;
    public static final int key_chat_outBubbleGradient1;
    public static final int key_chat_outBubbleGradient2;
    public static final int key_chat_outBubbleGradient3;
    public static final int key_chat_outBubbleGradientAnimated;
    public static final int key_chat_outBubbleGradientSelectedOverlay;
    public static final int key_chat_outBubbleLocationPlaceholder;
    public static final int key_chat_outBubbleSelected;
    public static final int key_chat_outBubbleSelectedOverlay;
    public static final int key_chat_outBubbleShadow;
    public static final int key_chat_outContactBackground;
    public static final int key_chat_outContactIcon;
    public static final int key_chat_outContactNameText;
    public static final int key_chat_outContactPhoneSelectedText;
    public static final int key_chat_outContactPhoneText;
    public static final int key_chat_outFileBackground;
    public static final int key_chat_outFileBackgroundSelected;
    public static final int key_chat_outFileInfoSelectedText;
    public static final int key_chat_outFileInfoText;
    public static final int key_chat_outFileNameText;
    public static final int key_chat_outFileProgress;
    public static final int key_chat_outFileProgressSelected;
    public static final int key_chat_outForwardedNameText;
    public static final int key_chat_outGreenCall;
    public static final int key_chat_outInstant;
    public static final int key_chat_outInstantSelected;
    public static final int key_chat_outLinkSelectBackground;
    public static final int key_chat_outLoader;
    public static final int key_chat_outLoaderSelected;
    public static final int key_chat_outLocationIcon;
    public static final int key_chat_outMediaIcon;
    public static final int key_chat_outMediaIconSelected;
    public static final int key_chat_outMenu;
    public static final int key_chat_outMenuSelected;
    public static final int key_chat_outPollCorrectAnswer;
    public static final int key_chat_outPollWrongAnswer;
    public static final int key_chat_outPreviewInstantText;
    public static final int key_chat_outPreviewLine;
    public static final int key_chat_outPsaNameText;
    public static final int key_chat_outReactionButtonBackground;
    public static final int key_chat_outReactionButtonText;
    public static final int key_chat_outReactionButtonTextSelected;
    public static final int key_chat_outReplyLine;
    public static final int key_chat_outReplyMediaMessageSelectedText;
    public static final int key_chat_outReplyMediaMessageText;
    public static final int key_chat_outReplyMessageText;
    public static final int key_chat_outReplyNameText;
    public static final int key_chat_outSentCheck;
    public static final int key_chat_outSentCheckRead;
    public static final int key_chat_outSentCheckReadSelected;
    public static final int key_chat_outSentCheckSelected;
    public static final int key_chat_outSentClock;
    public static final int key_chat_outSentClockSelected;
    public static final int key_chat_outSiteNameText;
    public static final int key_chat_outTextSelectionCursor;
    public static final int key_chat_outTextSelectionHighlight;
    public static final int key_chat_outTimeSelectedText;
    public static final int key_chat_outTimeText;
    public static final int key_chat_outVenueInfoSelectedText;
    public static final int key_chat_outVenueInfoText;
    public static final int key_chat_outViaBotNameText;
    public static final int key_chat_outViews;
    public static final int key_chat_outViewsSelected;
    public static final int key_chat_outVoiceSeekbar;
    public static final int key_chat_outVoiceSeekbarFill;
    public static final int key_chat_outVoiceSeekbarSelected;
    public static final int key_chat_previewDurationText;
    public static final int key_chat_previewGameText;
    public static final int key_chat_recordTime;
    public static final int key_chat_recordVoiceCancel;
    public static final int key_chat_recordedVoiceBackground;
    public static final int key_chat_recordedVoiceDot;
    public static final int key_chat_recordedVoicePlayPause;
    public static final int key_chat_recordedVoiceProgress;
    public static final int key_chat_recordedVoiceProgressInner;
    public static final int key_chat_replyPanelClose;
    public static final int key_chat_replyPanelIcons;
    public static final int key_chat_replyPanelLine;
    public static final int key_chat_replyPanelName;
    public static final int key_chat_searchPanelIcons;
    public static final int key_chat_searchPanelText;
    public static final int key_chat_secretChatStatusText;
    public static final int key_chat_secretTimeText;
    public static final int key_chat_selectedBackground;
    public static final int key_chat_sentError;
    public static final int key_chat_sentErrorIcon;
    public static final int key_chat_serviceBackground;
    public static final int key_chat_serviceBackgroundSelected;
    public static final int key_chat_serviceBackgroundSelector;
    public static final int key_chat_serviceIcon;
    public static final int key_chat_serviceLink;
    public static final int key_chat_serviceText;
    public static final int key_chat_status;
    public static final int key_chat_stickerNameText;
    public static final int key_chat_stickerReplyLine;
    public static final int key_chat_stickerReplyMessageText;
    public static final int key_chat_stickerReplyNameText;
    public static final int key_chat_stickerViaBotNameText;
    public static final int key_chat_stickersHintPanel;
    public static final int key_chat_textSelectBackground;
    public static final int key_chat_topPanelBackground;
    public static final int key_chat_topPanelClose;
    public static final int key_chat_topPanelLine;
    public static final int key_chat_topPanelMessage;
    public static final int key_chat_topPanelTitle;
    public static final int key_chat_unreadMessagesStartArrowIcon;
    public static final int key_chat_unreadMessagesStartBackground;
    public static final int key_chat_unreadMessagesStartText;
    public static final int key_chat_wallpaper;
    public static final int key_chat_wallpaper_gradient_rotation;
    public static final int key_chat_wallpaper_gradient_to1;
    public static final int key_chat_wallpaper_gradient_to2;
    public static final int key_chat_wallpaper_gradient_to3;
    public static final int key_chats_actionBackground;
    public static final int key_chats_actionIcon;
    public static final int key_chats_actionMessage;
    public static final int key_chats_actionPressedBackground;
    public static final int key_chats_archiveBackground;
    public static final int key_chats_archiveIcon;
    public static final int key_chats_archivePinBackground;
    public static final int key_chats_archivePullDownBackground;
    public static final int key_chats_archivePullDownBackgroundActive;
    public static final int key_chats_archiveText;
    public static final int key_chats_attachMessage;
    public static final int key_chats_date;
    public static final int key_chats_draft;
    public static final int key_chats_mentionIcon;
    public static final int key_chats_menuBackground;
    public static final int key_chats_menuItemCheck;
    public static final int key_chats_menuItemIcon;
    public static final int key_chats_menuItemText;
    public static final int key_chats_menuName;
    public static final int key_chats_menuPhone;
    public static final int key_chats_menuPhoneCats;
    public static final int key_chats_menuTopBackground;
    public static final int key_chats_menuTopBackgroundCats;
    public static final int key_chats_menuTopShadow;
    public static final int key_chats_menuTopShadowCats;
    public static final int key_chats_message;
    public static final int key_chats_messageArchived;
    public static final int key_chats_message_threeLines;
    public static final int key_chats_muteIcon;
    public static final int key_chats_name;
    public static final int key_chats_nameArchived;
    public static final int key_chats_nameMessage;
    public static final int key_chats_nameMessageArchived;
    public static final int key_chats_nameMessageArchived_threeLines;
    public static final int key_chats_nameMessage_threeLines;
    public static final int key_chats_onlineCircle;
    public static final int key_chats_pinnedIcon;
    public static final int key_chats_pinnedOverlay;
    public static final int key_chats_secretIcon;
    public static final int key_chats_secretName;
    public static final int key_chats_sentCheck;
    public static final int key_chats_sentClock;
    public static final int key_chats_sentError;
    public static final int key_chats_sentErrorIcon;
    public static final int key_chats_sentReadCheck;
    public static final int key_chats_tabUnreadActiveBackground;
    public static final int key_chats_tabUnreadUnactiveBackground;
    public static final int key_chats_tabletSelectedOverlay;
    public static final int key_chats_unreadCounter;
    public static final int key_chats_unreadCounterMuted;
    public static final int key_chats_unreadCounterText;
    public static final int key_chats_verifiedBackground;
    public static final int key_chats_verifiedCheck;
    public static final int key_checkbox;
    public static final int key_checkboxCheck;
    public static final int key_checkboxDisabled;
    public static final int key_checkboxSquareBackground;
    public static final int key_checkboxSquareCheck;
    public static final int key_checkboxSquareDisabled;
    public static final int key_checkboxSquareUnchecked;
    public static final int key_color_blue;
    public static final int key_color_cyan;
    public static final int key_color_green;
    public static final int key_color_lightblue;
    public static final int key_color_lightgreen;
    public static final int key_color_orange;
    public static final int key_color_purple;
    public static final int key_color_red;
    public static final int key_color_yellow;
    public static final int key_contacts_inviteBackground;
    public static final int key_contacts_inviteText;
    public static final int key_contextProgressInner1;
    public static final int key_contextProgressInner2;
    public static final int key_contextProgressInner3;
    public static final int key_contextProgressInner4;
    public static final int key_contextProgressOuter1;
    public static final int key_contextProgressOuter2;
    public static final int key_contextProgressOuter3;
    public static final int key_contextProgressOuter4;
    public static final int key_dialogBackground;
    public static final int key_dialogBackgroundGray;
    public static final int key_dialogButton;
    public static final int key_dialogButtonSelector;
    public static final int key_dialogCameraIcon;
    public static final int key_dialogCheckboxSquareBackground;
    public static final int key_dialogCheckboxSquareCheck;
    public static final int key_dialogCheckboxSquareDisabled;
    public static final int key_dialogCheckboxSquareUnchecked;
    public static final int key_dialogEmptyImage;
    public static final int key_dialogEmptyText;
    public static final int key_dialogFloatingButton;
    public static final int key_dialogFloatingButtonPressed;
    public static final int key_dialogFloatingIcon;
    public static final int key_dialogGrayLine;
    public static final int key_dialogIcon;
    public static final int key_dialogInputField;
    public static final int key_dialogInputFieldActivated;
    public static final int key_dialogLineProgress;
    public static final int key_dialogLineProgressBackground;
    public static final int key_dialogLinkSelection;
    public static final int key_dialogRadioBackground;
    public static final int key_dialogRadioBackgroundChecked;
    public static final int key_dialogReactionMentionBackground;
    public static final int key_dialogRoundCheckBox;
    public static final int key_dialogRoundCheckBoxCheck;
    public static final int key_dialogScrollGlow;
    public static final int key_dialogSearchBackground;
    public static final int key_dialogSearchHint;
    public static final int key_dialogSearchIcon;
    public static final int key_dialogSearchText;
    public static final int key_dialogShadowLine;
    public static final int key_dialogSwipeRemove;
    public static final int key_dialogTextBlack;
    public static final int key_dialogTextBlue;
    public static final int key_dialogTextBlue2;
    public static final int key_dialogTextBlue4;
    public static final int key_dialogTextGray;
    public static final int key_dialogTextGray2;
    public static final int key_dialogTextGray3;
    public static final int key_dialogTextGray4;
    public static final int key_dialogTextHint;
    public static final int key_dialogTextLink;
    public static final int key_dialogTopBackground;
    public static final int key_dialog_inlineProgress;
    public static final int key_dialog_inlineProgressBackground;
    public static final int key_dialog_liveLocationProgress;
    public static final int key_divider;
    public static final int key_emptyListPlaceholder;
    public static final int key_fastScrollActive;
    public static final int key_fastScrollInactive;
    public static final int key_fastScrollText;
    public static final int key_featuredStickers_addButton;
    public static final int key_featuredStickers_addButtonPressed;
    public static final int key_featuredStickers_addedIcon;
    public static final int key_featuredStickers_buttonProgress;
    public static final int key_featuredStickers_buttonText;
    public static final int key_featuredStickers_removeButtonText;
    public static final int key_featuredStickers_unread;
    public static final int key_files_folderIcon;
    public static final int key_files_folderIconBackground;
    public static final int key_files_iconText;
    public static final int key_fill_RedDark;
    public static final int key_fill_RedNormal;
    public static final int key_graySection;
    public static final int key_graySectionText;
    public static final int key_groupcreate_cursor;
    public static final int key_groupcreate_hintText;
    public static final int key_groupcreate_sectionShadow;
    public static final int key_groupcreate_sectionText;
    public static final int key_groupcreate_spanBackground;
    public static final int key_groupcreate_spanDelete;
    public static final int key_groupcreate_spanText;
    public static final int key_inappPlayerBackground;
    public static final int key_inappPlayerClose;
    public static final int key_inappPlayerPerformer;
    public static final int key_inappPlayerPlayPause;
    public static final int key_inappPlayerTitle;
    public static final int key_listSelector;
    public static final int key_location_actionActiveIcon;
    public static final int key_location_actionBackground;
    public static final int key_location_actionIcon;
    public static final int key_location_actionPressedBackground;
    public static final int key_location_liveLocationProgress;
    public static final int key_location_placeLocationBackground;
    public static final int key_location_sendLiveLocationBackground;
    public static final int key_location_sendLiveLocationIcon;
    public static final int key_location_sendLiveLocationText;
    public static final int key_location_sendLocationBackground;
    public static final int key_location_sendLocationIcon;
    public static final int key_location_sendLocationText;
    public static final int key_login_progressInner;
    public static final int key_login_progressOuter;
    public static final int key_passport_authorizeBackground;
    public static final int key_passport_authorizeBackgroundSelected;
    public static final int key_passport_authorizeText;
    public static final int key_picker_badge;
    public static final int key_picker_badgeText;
    public static final int key_picker_disabledButton;
    public static final int key_picker_enabledButton;
    public static final int key_player_actionBarItems;
    public static final int key_player_actionBarSelector;
    public static final int key_player_actionBarSubtitle;
    public static final int key_player_actionBarTitle;
    public static final int key_player_background;
    public static final int key_player_button;
    public static final int key_player_buttonActive;
    public static final int key_player_progress;
    public static final int key_player_progressBackground;
    public static final int key_player_progressCachedBackground;
    public static final int key_player_time;
    public static final int key_premiumGradient0;
    public static final int key_premiumGradient1;
    public static final int key_premiumGradient2;
    public static final int key_premiumGradient3;
    public static final int key_premiumGradient4;
    public static final int key_premiumGradientBackground1;
    public static final int key_premiumGradientBackground2;
    public static final int key_premiumGradientBackground3;
    public static final int key_premiumGradientBackground4;
    public static final int key_premiumGradientBackgroundOverlay;
    public static final int key_premiumGradientBottomSheet1;
    public static final int key_premiumGradientBottomSheet2;
    public static final int key_premiumGradientBottomSheet3;
    public static final int key_premiumStartGradient1;
    public static final int key_premiumStartGradient2;
    public static final int key_premiumStartSmallStarsColor;
    public static final int key_premiumStartSmallStarsColor2;
    public static final int key_profile_actionBackground;
    public static final int key_profile_actionIcon;
    public static final int key_profile_actionPressedBackground;
    public static final int key_profile_creatorIcon;
    public static final int key_profile_status;
    public static final int key_profile_tabSelectedLine;
    public static final int key_profile_tabSelectedText;
    public static final int key_profile_tabSelector;
    public static final int key_profile_tabText;
    public static final int key_profile_title;
    public static final int key_profile_verifiedBackground;
    public static final int key_profile_verifiedCheck;
    public static final int key_progressCircle;
    public static final int key_radioBackground;
    public static final int key_radioBackgroundChecked;
    public static final int key_returnToCallBackground;
    public static final int key_returnToCallMutedBackground;
    public static final int key_returnToCallText;
    public static final int key_sessions_devicesImage;
    public static final int key_sharedMedia_linkPlaceholder;
    public static final int key_sharedMedia_linkPlaceholderText;
    public static final int key_sharedMedia_photoPlaceholder;
    public static final int key_sharedMedia_startStopLoadIcon;
    public static final int key_sheet_other;
    public static final int key_sheet_scrollUp;
    public static final int key_statisticChartActiveLine;
    public static final int key_statisticChartActivePickerChart;
    public static final int key_statisticChartBackZoomColor;
    public static final int key_statisticChartChevronColor;
    public static final int key_statisticChartHintLine;
    public static final int key_statisticChartInactivePickerChart;
    public static final int key_statisticChartLineEmpty;
    public static final int key_statisticChartLine_blue;
    public static final int key_statisticChartLine_cyan;
    public static final int key_statisticChartLine_golden;
    public static final int key_statisticChartLine_green;
    public static final int key_statisticChartLine_indigo;
    public static final int key_statisticChartLine_lightblue;
    public static final int key_statisticChartLine_lightgreen;
    public static final int key_statisticChartLine_orange;
    public static final int key_statisticChartLine_purple;
    public static final int key_statisticChartLine_red;
    public static final int key_statisticChartRipple;
    public static final int key_statisticChartSignature;
    public static final int key_statisticChartSignatureAlpha;
    public static final int key_stickers_menu;
    public static final int key_stickers_menuSelector;
    public static final int key_switch2Track;
    public static final int key_switch2TrackChecked;
    public static final int key_switchTrack;
    public static final int key_switchTrackBlue;
    public static final int key_switchTrackBlueChecked;
    public static final int key_switchTrackBlueSelector;
    public static final int key_switchTrackBlueSelectorChecked;
    public static final int key_switchTrackBlueThumb;
    public static final int key_switchTrackBlueThumbChecked;
    public static final int key_switchTrackChecked;
    public static final int key_text_RedBold;
    public static final int key_text_RedRegular;
    public static final int key_topics_unreadCounter;
    public static final int key_topics_unreadCounterMuted;
    public static final int key_undo_background;
    public static final int key_undo_cancelColor;
    public static final int key_undo_infoColor;
    public static final int key_voipgroup_actionBar;
    public static final int key_voipgroup_actionBarItems;
    public static final int key_voipgroup_actionBarItemsSelector;
    public static final int key_voipgroup_actionBarUnscrolled;
    public static final int key_voipgroup_checkMenu;
    public static final int key_voipgroup_connectingProgress;
    public static final int key_voipgroup_dialogBackground;
    public static final int key_voipgroup_disabledButton;
    public static final int key_voipgroup_disabledButtonActive;
    public static final int key_voipgroup_disabledButtonActiveScrolled;
    public static final int key_voipgroup_inviteMembersBackground;
    public static final int key_voipgroup_lastSeenText;
    public static final int key_voipgroup_lastSeenTextUnscrolled;
    public static final int key_voipgroup_leaveButton;
    public static final int key_voipgroup_leaveButtonScrolled;
    public static final int key_voipgroup_leaveCallMenu;
    public static final int key_voipgroup_listSelector;
    public static final int key_voipgroup_listViewBackground;
    public static final int key_voipgroup_listViewBackgroundUnscrolled;
    public static final int key_voipgroup_listeningText;
    public static final int key_voipgroup_muteButton;
    public static final int key_voipgroup_muteButton2;
    public static final int key_voipgroup_muteButton3;
    public static final int key_voipgroup_mutedByAdminGradient;
    public static final int key_voipgroup_mutedByAdminGradient2;
    public static final int key_voipgroup_mutedByAdminGradient3;
    public static final int key_voipgroup_mutedByAdminIcon;
    public static final int key_voipgroup_mutedByAdminMuteButton;
    public static final int key_voipgroup_mutedByAdminMuteButtonDisabled;
    public static final int key_voipgroup_mutedIcon;
    public static final int key_voipgroup_mutedIconUnscrolled;
    public static final int key_voipgroup_nameText;
    public static final int key_voipgroup_overlayAlertGradientMuted;
    public static final int key_voipgroup_overlayAlertGradientMuted2;
    public static final int key_voipgroup_overlayAlertGradientUnmuted;
    public static final int key_voipgroup_overlayAlertGradientUnmuted2;
    public static final int key_voipgroup_overlayAlertMutedByAdmin;
    public static final int key_voipgroup_overlayAlertMutedByAdmin2;
    public static final int key_voipgroup_overlayBlue1;
    public static final int key_voipgroup_overlayBlue2;
    public static final int key_voipgroup_overlayGreen1;
    public static final int key_voipgroup_overlayGreen2;
    public static final int key_voipgroup_scrollUp;
    public static final int key_voipgroup_searchBackground;
    public static final int key_voipgroup_searchPlaceholder;
    public static final int key_voipgroup_searchText;
    public static final int key_voipgroup_soundButton;
    public static final int key_voipgroup_soundButton2;
    public static final int key_voipgroup_soundButtonActive;
    public static final int key_voipgroup_soundButtonActive2;
    public static final int key_voipgroup_soundButtonActive2Scrolled;
    public static final int key_voipgroup_soundButtonActiveScrolled;
    public static final int key_voipgroup_speakingText;
    public static final int key_voipgroup_topPanelBlue1;
    public static final int key_voipgroup_topPanelBlue2;
    public static final int key_voipgroup_topPanelGray;
    public static final int key_voipgroup_topPanelGreen1;
    public static final int key_voipgroup_topPanelGreen2;
    public static final int key_voipgroup_unmuteButton;
    public static final int key_voipgroup_unmuteButton2;
    public static final int key_voipgroup_windowBackgroundWhiteInputField;
    public static final int key_voipgroup_windowBackgroundWhiteInputFieldActivated;
    public static final int key_wallpaperFileOffset;
    public static final int key_windowBackgroundCheckText;
    public static final int key_windowBackgroundChecked;
    public static final int key_windowBackgroundGray;
    public static final int key_windowBackgroundGrayShadow;
    public static final int key_windowBackgroundUnchecked;
    public static final int key_windowBackgroundWhite;
    public static final int key_windowBackgroundWhiteBlackText;
    public static final int key_windowBackgroundWhiteBlueButton;
    public static final int key_windowBackgroundWhiteBlueHeader;
    public static final int key_windowBackgroundWhiteBlueIcon;
    public static final int key_windowBackgroundWhiteBlueText;
    public static final int key_windowBackgroundWhiteBlueText2;
    public static final int key_windowBackgroundWhiteBlueText3;
    public static final int key_windowBackgroundWhiteBlueText4;
    public static final int key_windowBackgroundWhiteBlueText5;
    public static final int key_windowBackgroundWhiteBlueText6;
    public static final int key_windowBackgroundWhiteBlueText7;
    public static final int key_windowBackgroundWhiteGrayIcon;
    public static final int key_windowBackgroundWhiteGrayText;
    public static final int key_windowBackgroundWhiteGrayText2;
    public static final int key_windowBackgroundWhiteGrayText3;
    public static final int key_windowBackgroundWhiteGrayText4;
    public static final int key_windowBackgroundWhiteGrayText5;
    public static final int key_windowBackgroundWhiteGrayText6;
    public static final int key_windowBackgroundWhiteGrayText7;
    public static final int key_windowBackgroundWhiteGrayText8;
    public static final int key_windowBackgroundWhiteGreenText;
    public static final int key_windowBackgroundWhiteGreenText2;
    public static final int key_windowBackgroundWhiteHintText;
    public static final int key_windowBackgroundWhiteInputField;
    public static final int key_windowBackgroundWhiteInputFieldActivated;
    public static final int key_windowBackgroundWhiteLinkSelection;
    public static final int key_windowBackgroundWhiteLinkText;
    public static final int key_windowBackgroundWhiteValueText;
    public static int[] keys_avatar_background;
    public static int[] keys_avatar_background2;
    public static int[] keys_avatar_nameInMessage;
    public static final int[] keys_colors;
    private static long lastDelayUpdateTime;
    private static long lastHolidayCheckTime;
    private static int lastLoadingCurrentThemeTime;
    private static long lastThemeSwitchTime;
    private static Sensor lightSensor;
    private static boolean lightSensorRegistered;
    public static Paint linkSelectionPaint;
    private static int loadingCurrentTheme;
    public static Drawable moveUpDrawable;
    public static final int myMessagesBubblesEndIndex;
    public static final int myMessagesBubblesStartIndex;
    public static final int myMessagesEndIndex;
    public static final int myMessagesStartIndex;
    private static ArrayList<ThemeInfo> otherThemes;
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
    private static HashSet<Integer> themeAccentExclusionKeys;
    private static Drawable themedWallpaper;
    private static int themedWallpaperFileOffset;
    private static String themedWallpaperLink;
    public static ArrayList<ThemeInfo> themes;
    private static HashMap<String, ThemeInfo> themesDict;
    private static float[] tmpHSV5;
    private static int[] viewPos;
    private static Drawable wallpaper;
    public static Runnable wallpaperLoadTask;
    public static final int default_shadow_color = ColorUtils.setAlphaComponent(-16777216, 27);
    private static final Object sync = new Object();
    private static float lastBrightnessValue = 1.0f;
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
    public static Drawable[] avatarDrawables = new Drawable[13];
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
    public static Drawable[][] chat_fileStatesDrawable = (Drawable[][]) Array.newInstance(Drawable.class, 5, 2);
    public static Path[] chat_filePath = new Path[2];
    public static Path[] chat_updatePath = new Path[3];

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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:167:0x2812 A[Catch: Exception -> 0x29ff, TryCatch #3 {Exception -> 0x29ff, blocks: (B:54:0x24c3, B:56:0x24d9, B:67:0x251a, B:69:0x2528, B:77:0x2553, B:79:0x2557, B:81:0x255f, B:82:0x2571, B:83:0x257d, B:85:0x2583, B:87:0x258d, B:89:0x2591, B:91:0x25bf, B:93:0x25c3, B:165:0x280c, B:167:0x2812, B:168:0x281b, B:170:0x281f, B:172:0x2827, B:174:0x282b, B:176:0x282f, B:177:0x2831, B:179:0x283b, B:148:0x26fa, B:151:0x271b, B:153:0x2726, B:155:0x2732, B:157:0x273e, B:161:0x274a, B:163:0x27f1, B:158:0x2744, B:184:0x285b, B:185:0x2861, B:189:0x286b, B:191:0x28c2, B:193:0x28d0, B:195:0x28da, B:197:0x28e8, B:196:0x28e1, B:192:0x28c9, B:70:0x2536, B:72:0x253e, B:74:0x2547, B:76:0x2551, B:57:0x24e6, B:59:0x24ee, B:61:0x24f6, B:63:0x2500, B:65:0x2508, B:95:0x25d4, B:98:0x25ea, B:100:0x25ff, B:101:0x2605, B:103:0x2617, B:106:0x2627, B:110:0x2633, B:114:0x264a, B:118:0x265b, B:120:0x266a, B:123:0x2673, B:125:0x2686, B:128:0x268f, B:130:0x2696, B:131:0x26a6, B:133:0x26aa, B:134:0x26ae, B:136:0x26b9, B:138:0x26c5, B:115:0x2651, B:111:0x263d), top: B:239:0x24c3 }] */
    /* JADX WARN: Removed duplicated region for block: B:174:0x282b A[Catch: Exception -> 0x29ff, TryCatch #3 {Exception -> 0x29ff, blocks: (B:54:0x24c3, B:56:0x24d9, B:67:0x251a, B:69:0x2528, B:77:0x2553, B:79:0x2557, B:81:0x255f, B:82:0x2571, B:83:0x257d, B:85:0x2583, B:87:0x258d, B:89:0x2591, B:91:0x25bf, B:93:0x25c3, B:165:0x280c, B:167:0x2812, B:168:0x281b, B:170:0x281f, B:172:0x2827, B:174:0x282b, B:176:0x282f, B:177:0x2831, B:179:0x283b, B:148:0x26fa, B:151:0x271b, B:153:0x2726, B:155:0x2732, B:157:0x273e, B:161:0x274a, B:163:0x27f1, B:158:0x2744, B:184:0x285b, B:185:0x2861, B:189:0x286b, B:191:0x28c2, B:193:0x28d0, B:195:0x28da, B:197:0x28e8, B:196:0x28e1, B:192:0x28c9, B:70:0x2536, B:72:0x253e, B:74:0x2547, B:76:0x2551, B:57:0x24e6, B:59:0x24ee, B:61:0x24f6, B:63:0x2500, B:65:0x2508, B:95:0x25d4, B:98:0x25ea, B:100:0x25ff, B:101:0x2605, B:103:0x2617, B:106:0x2627, B:110:0x2633, B:114:0x264a, B:118:0x265b, B:120:0x266a, B:123:0x2673, B:125:0x2686, B:128:0x268f, B:130:0x2696, B:131:0x26a6, B:133:0x26aa, B:134:0x26ae, B:136:0x26b9, B:138:0x26c5, B:115:0x2651, B:111:0x263d), top: B:239:0x24c3 }] */
    /* JADX WARN: Removed duplicated region for block: B:175:0x282e  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x283b A[Catch: Exception -> 0x29ff, TryCatch #3 {Exception -> 0x29ff, blocks: (B:54:0x24c3, B:56:0x24d9, B:67:0x251a, B:69:0x2528, B:77:0x2553, B:79:0x2557, B:81:0x255f, B:82:0x2571, B:83:0x257d, B:85:0x2583, B:87:0x258d, B:89:0x2591, B:91:0x25bf, B:93:0x25c3, B:165:0x280c, B:167:0x2812, B:168:0x281b, B:170:0x281f, B:172:0x2827, B:174:0x282b, B:176:0x282f, B:177:0x2831, B:179:0x283b, B:148:0x26fa, B:151:0x271b, B:153:0x2726, B:155:0x2732, B:157:0x273e, B:161:0x274a, B:163:0x27f1, B:158:0x2744, B:184:0x285b, B:185:0x2861, B:189:0x286b, B:191:0x28c2, B:193:0x28d0, B:195:0x28da, B:197:0x28e8, B:196:0x28e1, B:192:0x28c9, B:70:0x2536, B:72:0x253e, B:74:0x2547, B:76:0x2551, B:57:0x24e6, B:59:0x24ee, B:61:0x24f6, B:63:0x2500, B:65:0x2508, B:95:0x25d4, B:98:0x25ea, B:100:0x25ff, B:101:0x2605, B:103:0x2617, B:106:0x2627, B:110:0x2633, B:114:0x264a, B:118:0x265b, B:120:0x266a, B:123:0x2673, B:125:0x2686, B:128:0x268f, B:130:0x2696, B:131:0x26a6, B:133:0x26aa, B:134:0x26ae, B:136:0x26b9, B:138:0x26c5, B:115:0x2651, B:111:0x263d), top: B:239:0x24c3 }] */
    /* JADX WARN: Removed duplicated region for block: B:253:0x284b A[SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r4v41, types: [boolean] */
    /* JADX WARN: Type inference failed for: r4v47 */
    /* JADX WARN: Type inference failed for: r4v50 */
    static {
        ThemeInfo themeInfo;
        ThemeInfo themeInfo2;
        ThemeInfo themeInfo3;
        String str;
        ThemeInfo themeInfo4;
        SharedPreferences.Editor editor;
        boolean z;
        SparseArray<ThemeAccent> sparseArray;
        ThemeAccent accent;
        boolean z2;
        ThemeInfo themeInfo5;
        SharedPreferences.Editor editor2;
        String str2;
        selectedAutoNightType = 0;
        autoNightBrighnessThreshold = 0.25f;
        autoNightDayStartTime = 1320;
        autoNightDayEndTime = 480;
        autoNightSunsetTime = 1320;
        autoNightLastSunCheckDay = -1;
        autoNightSunriseTime = 480;
        String str3 = "";
        autoNightCityName = "";
        autoNightLocationLatitude = 10000.0d;
        autoNightLocationLongitude = 10000.0d;
        int i = colorsCount;
        int i2 = i + 1;
        colorsCount = i2;
        key_wallpaperFileOffset = i;
        int i3 = i2 + 1;
        colorsCount = i3;
        key_dialogBackground = i2;
        int i4 = i3 + 1;
        colorsCount = i4;
        key_dialogBackgroundGray = i3;
        int i5 = i4 + 1;
        colorsCount = i5;
        key_dialogTextBlack = i4;
        int i6 = i5 + 1;
        colorsCount = i6;
        key_dialogTextLink = i5;
        int i7 = i6 + 1;
        colorsCount = i7;
        key_dialogLinkSelection = i6;
        int i8 = i7 + 1;
        colorsCount = i8;
        key_dialogTextBlue = i7;
        int i9 = i8 + 1;
        colorsCount = i9;
        key_dialogTextBlue2 = i8;
        int i10 = i9 + 1;
        colorsCount = i10;
        key_dialogTextBlue4 = i9;
        int i11 = i10 + 1;
        colorsCount = i11;
        key_dialogTextGray = i10;
        int i12 = i11 + 1;
        colorsCount = i12;
        key_dialogTextGray2 = i11;
        int i13 = i12 + 1;
        colorsCount = i13;
        key_dialogTextGray3 = i12;
        int i14 = i13 + 1;
        colorsCount = i14;
        key_dialogTextGray4 = i13;
        int i15 = i14 + 1;
        colorsCount = i15;
        key_dialogTextHint = i14;
        int i16 = i15 + 1;
        colorsCount = i16;
        key_dialogInputField = i15;
        int i17 = i16 + 1;
        colorsCount = i17;
        key_dialogInputFieldActivated = i16;
        int i18 = i17 + 1;
        colorsCount = i18;
        key_dialogCheckboxSquareBackground = i17;
        int i19 = i18 + 1;
        colorsCount = i19;
        key_dialogCheckboxSquareCheck = i18;
        int i20 = i19 + 1;
        colorsCount = i20;
        key_dialogCheckboxSquareUnchecked = i19;
        int i21 = i20 + 1;
        colorsCount = i21;
        key_dialogCheckboxSquareDisabled = i20;
        int i22 = i21 + 1;
        colorsCount = i22;
        key_dialogScrollGlow = i21;
        int i23 = i22 + 1;
        colorsCount = i23;
        key_dialogRoundCheckBox = i22;
        int i24 = i23 + 1;
        colorsCount = i24;
        key_dialogRoundCheckBoxCheck = i23;
        int i25 = i24 + 1;
        colorsCount = i25;
        key_dialogRadioBackground = i24;
        int i26 = i25 + 1;
        colorsCount = i26;
        key_dialogRadioBackgroundChecked = i25;
        int i27 = i26 + 1;
        colorsCount = i27;
        key_dialogLineProgress = i26;
        int i28 = i27 + 1;
        colorsCount = i28;
        key_dialogLineProgressBackground = i27;
        int i29 = i28 + 1;
        colorsCount = i29;
        key_dialogButton = i28;
        int i30 = i29 + 1;
        colorsCount = i30;
        key_dialogButtonSelector = i29;
        int i31 = i30 + 1;
        colorsCount = i31;
        key_dialogIcon = i30;
        int i32 = i31 + 1;
        colorsCount = i32;
        key_dialogGrayLine = i31;
        int i33 = i32 + 1;
        colorsCount = i33;
        key_dialogTopBackground = i32;
        int i34 = i33 + 1;
        colorsCount = i34;
        key_dialogCameraIcon = i33;
        int i35 = i34 + 1;
        colorsCount = i35;
        key_dialog_inlineProgressBackground = i34;
        int i36 = i35 + 1;
        colorsCount = i36;
        key_dialog_inlineProgress = i35;
        int i37 = i36 + 1;
        colorsCount = i37;
        key_dialogSearchBackground = i36;
        int i38 = i37 + 1;
        colorsCount = i38;
        key_dialogSearchHint = i37;
        int i39 = i38 + 1;
        colorsCount = i39;
        key_dialogSearchIcon = i38;
        int i40 = i39 + 1;
        colorsCount = i40;
        key_dialogSearchText = i39;
        int i41 = i40 + 1;
        colorsCount = i41;
        key_dialogFloatingButton = i40;
        int i42 = i41 + 1;
        colorsCount = i42;
        key_dialogFloatingButtonPressed = i41;
        int i43 = i42 + 1;
        colorsCount = i43;
        key_dialogFloatingIcon = i42;
        int i44 = i43 + 1;
        colorsCount = i44;
        key_dialogShadowLine = i43;
        int i45 = i44 + 1;
        colorsCount = i45;
        key_dialogEmptyImage = i44;
        int i46 = i45 + 1;
        colorsCount = i46;
        key_dialogEmptyText = i45;
        int i47 = i46 + 1;
        colorsCount = i47;
        key_dialogSwipeRemove = i46;
        int i48 = i47 + 1;
        colorsCount = i48;
        key_dialogReactionMentionBackground = i47;
        colorsCount = i48 + 1;
        key_windowBackgroundWhite = i48;
        int i49 = colorsCount;
        colorsCount = i49 + 1;
        key_windowBackgroundUnchecked = i49;
        int i50 = colorsCount;
        colorsCount = i50 + 1;
        key_windowBackgroundChecked = i50;
        int i51 = colorsCount;
        colorsCount = i51 + 1;
        key_windowBackgroundCheckText = i51;
        int i52 = colorsCount;
        colorsCount = i52 + 1;
        key_progressCircle = i52;
        int i53 = colorsCount;
        colorsCount = i53 + 1;
        key_listSelector = i53;
        int i54 = colorsCount;
        colorsCount = i54 + 1;
        key_windowBackgroundWhiteInputField = i54;
        int i55 = colorsCount;
        colorsCount = i55 + 1;
        key_windowBackgroundWhiteInputFieldActivated = i55;
        int i56 = colorsCount;
        colorsCount = i56 + 1;
        key_windowBackgroundWhiteGrayIcon = i56;
        int i57 = colorsCount;
        colorsCount = i57 + 1;
        key_windowBackgroundWhiteBlueText = i57;
        int i58 = colorsCount;
        colorsCount = i58 + 1;
        key_windowBackgroundWhiteBlueText2 = i58;
        int i59 = colorsCount;
        colorsCount = i59 + 1;
        key_windowBackgroundWhiteBlueText3 = i59;
        int i60 = colorsCount;
        colorsCount = i60 + 1;
        key_windowBackgroundWhiteBlueText4 = i60;
        int i61 = colorsCount;
        colorsCount = i61 + 1;
        key_windowBackgroundWhiteBlueText5 = i61;
        int i62 = colorsCount;
        colorsCount = i62 + 1;
        key_windowBackgroundWhiteBlueText6 = i62;
        int i63 = colorsCount;
        colorsCount = i63 + 1;
        key_windowBackgroundWhiteBlueText7 = i63;
        int i64 = colorsCount;
        colorsCount = i64 + 1;
        key_windowBackgroundWhiteBlueButton = i64;
        int i65 = colorsCount;
        colorsCount = i65 + 1;
        key_windowBackgroundWhiteBlueIcon = i65;
        int i66 = colorsCount;
        colorsCount = i66 + 1;
        key_windowBackgroundWhiteGreenText = i66;
        int i67 = colorsCount;
        colorsCount = i67 + 1;
        key_windowBackgroundWhiteGreenText2 = i67;
        int i68 = colorsCount;
        colorsCount = i68 + 1;
        key_windowBackgroundWhiteGrayText = i68;
        int i69 = colorsCount;
        colorsCount = i69 + 1;
        key_windowBackgroundWhiteGrayText2 = i69;
        int i70 = colorsCount;
        colorsCount = i70 + 1;
        key_windowBackgroundWhiteGrayText3 = i70;
        int i71 = colorsCount;
        colorsCount = i71 + 1;
        key_windowBackgroundWhiteGrayText4 = i71;
        int i72 = colorsCount;
        colorsCount = i72 + 1;
        key_windowBackgroundWhiteGrayText5 = i72;
        int i73 = colorsCount;
        colorsCount = i73 + 1;
        key_windowBackgroundWhiteGrayText6 = i73;
        int i74 = colorsCount;
        colorsCount = i74 + 1;
        key_windowBackgroundWhiteGrayText7 = i74;
        int i75 = colorsCount;
        colorsCount = i75 + 1;
        key_windowBackgroundWhiteGrayText8 = i75;
        int i76 = colorsCount;
        colorsCount = i76 + 1;
        key_windowBackgroundWhiteBlackText = i76;
        int i77 = colorsCount;
        colorsCount = i77 + 1;
        key_windowBackgroundWhiteHintText = i77;
        int i78 = colorsCount;
        colorsCount = i78 + 1;
        key_windowBackgroundWhiteValueText = i78;
        int i79 = colorsCount;
        colorsCount = i79 + 1;
        key_windowBackgroundWhiteLinkText = i79;
        int i80 = colorsCount;
        colorsCount = i80 + 1;
        key_windowBackgroundWhiteLinkSelection = i80;
        int i81 = colorsCount;
        colorsCount = i81 + 1;
        key_windowBackgroundWhiteBlueHeader = i81;
        int i82 = colorsCount;
        colorsCount = i82 + 1;
        key_switchTrack = i82;
        int i83 = colorsCount;
        colorsCount = i83 + 1;
        key_switchTrackChecked = i83;
        int i84 = colorsCount;
        colorsCount = i84 + 1;
        key_switchTrackBlue = i84;
        int i85 = colorsCount;
        colorsCount = i85 + 1;
        key_switchTrackBlueChecked = i85;
        int i86 = colorsCount;
        colorsCount = i86 + 1;
        key_switchTrackBlueThumb = i86;
        int i87 = colorsCount;
        colorsCount = i87 + 1;
        key_switchTrackBlueThumbChecked = i87;
        int i88 = colorsCount;
        colorsCount = i88 + 1;
        key_switchTrackBlueSelector = i88;
        int i89 = colorsCount;
        colorsCount = i89 + 1;
        key_switchTrackBlueSelectorChecked = i89;
        int i90 = colorsCount;
        colorsCount = i90 + 1;
        key_switch2Track = i90;
        int i91 = colorsCount;
        colorsCount = i91 + 1;
        key_switch2TrackChecked = i91;
        int i92 = colorsCount;
        colorsCount = i92 + 1;
        key_checkboxSquareBackground = i92;
        int i93 = colorsCount;
        colorsCount = i93 + 1;
        key_checkboxSquareCheck = i93;
        int i94 = colorsCount;
        colorsCount = i94 + 1;
        key_checkboxSquareUnchecked = i94;
        int i95 = colorsCount;
        colorsCount = i95 + 1;
        key_checkboxSquareDisabled = i95;
        int i96 = colorsCount;
        colorsCount = i96 + 1;
        key_windowBackgroundGray = i96;
        int i97 = colorsCount;
        colorsCount = i97 + 1;
        key_windowBackgroundGrayShadow = i97;
        int i98 = colorsCount;
        colorsCount = i98 + 1;
        key_emptyListPlaceholder = i98;
        int i99 = colorsCount;
        colorsCount = i99 + 1;
        key_divider = i99;
        int i100 = colorsCount;
        colorsCount = i100 + 1;
        key_graySection = i100;
        int i101 = colorsCount;
        colorsCount = i101 + 1;
        key_graySectionText = i101;
        int i102 = colorsCount;
        colorsCount = i102 + 1;
        key_radioBackground = i102;
        int i103 = colorsCount;
        colorsCount = i103 + 1;
        key_radioBackgroundChecked = i103;
        int i104 = colorsCount;
        colorsCount = i104 + 1;
        key_checkbox = i104;
        int i105 = colorsCount;
        colorsCount = i105 + 1;
        key_checkboxDisabled = i105;
        int i106 = colorsCount;
        colorsCount = i106 + 1;
        key_checkboxCheck = i106;
        int i107 = colorsCount;
        colorsCount = i107 + 1;
        key_fastScrollActive = i107;
        int i108 = colorsCount;
        colorsCount = i108 + 1;
        key_fastScrollInactive = i108;
        int i109 = colorsCount;
        colorsCount = i109 + 1;
        key_fastScrollText = i109;
        int i110 = colorsCount;
        colorsCount = i110 + 1;
        key_text_RedRegular = i110;
        int i111 = colorsCount;
        colorsCount = i111 + 1;
        key_text_RedBold = i111;
        int i112 = colorsCount;
        colorsCount = i112 + 1;
        key_fill_RedNormal = i112;
        int i113 = colorsCount;
        colorsCount = i113 + 1;
        key_fill_RedDark = i113;
        int i114 = colorsCount;
        colorsCount = i114 + 1;
        key_inappPlayerPerformer = i114;
        int i115 = colorsCount;
        colorsCount = i115 + 1;
        key_inappPlayerTitle = i115;
        int i116 = colorsCount;
        colorsCount = i116 + 1;
        key_inappPlayerBackground = i116;
        int i117 = colorsCount;
        colorsCount = i117 + 1;
        key_inappPlayerPlayPause = i117;
        int i118 = colorsCount;
        colorsCount = i118 + 1;
        key_inappPlayerClose = i118;
        int i119 = colorsCount;
        colorsCount = i119 + 1;
        key_returnToCallBackground = i119;
        int i120 = colorsCount;
        colorsCount = i120 + 1;
        key_returnToCallMutedBackground = i120;
        int i121 = colorsCount;
        colorsCount = i121 + 1;
        key_returnToCallText = i121;
        int i122 = colorsCount;
        colorsCount = i122 + 1;
        key_contextProgressInner1 = i122;
        int i123 = colorsCount;
        colorsCount = i123 + 1;
        key_contextProgressOuter1 = i123;
        int i124 = colorsCount;
        colorsCount = i124 + 1;
        key_contextProgressInner2 = i124;
        int i125 = colorsCount;
        colorsCount = i125 + 1;
        key_contextProgressOuter2 = i125;
        int i126 = colorsCount;
        colorsCount = i126 + 1;
        key_contextProgressInner3 = i126;
        int i127 = colorsCount;
        colorsCount = i127 + 1;
        key_contextProgressOuter3 = i127;
        int i128 = colorsCount;
        colorsCount = i128 + 1;
        key_contextProgressInner4 = i128;
        int i129 = colorsCount;
        colorsCount = i129 + 1;
        key_contextProgressOuter4 = i129;
        int i130 = colorsCount;
        colorsCount = i130 + 1;
        key_avatar_text = i130;
        int i131 = colorsCount;
        colorsCount = i131 + 1;
        key_avatar_backgroundSaved = i131;
        int i132 = colorsCount;
        colorsCount = i132 + 1;
        key_avatar_background2Saved = i132;
        int i133 = colorsCount;
        colorsCount = i133 + 1;
        key_avatar_backgroundArchived = i133;
        int i134 = colorsCount;
        colorsCount = i134 + 1;
        key_avatar_backgroundArchivedHidden = i134;
        int i135 = colorsCount;
        colorsCount = i135 + 1;
        key_avatar_backgroundRed = i135;
        int i136 = colorsCount;
        colorsCount = i136 + 1;
        key_avatar_backgroundOrange = i136;
        int i137 = colorsCount;
        colorsCount = i137 + 1;
        key_avatar_backgroundViolet = i137;
        int i138 = colorsCount;
        colorsCount = i138 + 1;
        key_avatar_backgroundGreen = i138;
        int i139 = colorsCount;
        colorsCount = i139 + 1;
        key_avatar_backgroundCyan = i139;
        int i140 = colorsCount;
        colorsCount = i140 + 1;
        key_avatar_backgroundBlue = i140;
        int i141 = colorsCount;
        colorsCount = i141 + 1;
        key_avatar_backgroundPink = i141;
        int i142 = colorsCount;
        colorsCount = i142 + 1;
        key_avatar_background2Red = i142;
        int i143 = colorsCount;
        colorsCount = i143 + 1;
        key_avatar_background2Orange = i143;
        int i144 = colorsCount;
        colorsCount = i144 + 1;
        key_avatar_background2Violet = i144;
        int i145 = colorsCount;
        colorsCount = i145 + 1;
        key_avatar_background2Green = i145;
        int i146 = colorsCount;
        colorsCount = i146 + 1;
        key_avatar_background2Cyan = i146;
        int i147 = colorsCount;
        colorsCount = i147 + 1;
        key_avatar_background2Blue = i147;
        int i148 = colorsCount;
        colorsCount = i148 + 1;
        key_avatar_background2Pink = i148;
        int i149 = colorsCount;
        colorsCount = i149 + 1;
        key_avatar_backgroundInProfileBlue = i149;
        int i150 = colorsCount;
        colorsCount = i150 + 1;
        key_avatar_backgroundActionBarBlue = i150;
        int i151 = colorsCount;
        colorsCount = i151 + 1;
        key_avatar_actionBarSelectorBlue = i151;
        int i152 = colorsCount;
        colorsCount = i152 + 1;
        key_avatar_actionBarIconBlue = i152;
        int i153 = colorsCount;
        colorsCount = i153 + 1;
        key_avatar_subtitleInProfileBlue = i153;
        int i154 = colorsCount;
        colorsCount = i154 + 1;
        key_avatar_nameInMessageRed = i154;
        int i155 = colorsCount;
        colorsCount = i155 + 1;
        key_avatar_nameInMessageOrange = i155;
        int i156 = colorsCount;
        colorsCount = i156 + 1;
        key_avatar_nameInMessageViolet = i156;
        int i157 = colorsCount;
        colorsCount = i157 + 1;
        key_avatar_nameInMessageGreen = i157;
        int i158 = colorsCount;
        colorsCount = i158 + 1;
        key_avatar_nameInMessageCyan = i158;
        int i159 = colorsCount;
        colorsCount = i159 + 1;
        key_avatar_nameInMessageBlue = i159;
        int i160 = colorsCount;
        colorsCount = i160 + 1;
        key_avatar_nameInMessagePink = i160;
        keys_avatar_background = new int[]{i135, i136, i137, i138, i139, i140, i141};
        keys_avatar_background2 = new int[]{i142, i143, i144, i145, i146, i147, i148};
        keys_avatar_nameInMessage = new int[]{i154, i155, i156, i157, i158, i159, i160};
        int i161 = colorsCount;
        colorsCount = i161 + 1;
        key_actionBarDefault = i161;
        int i162 = colorsCount;
        colorsCount = i162 + 1;
        key_actionBarDefaultSelector = i162;
        int i163 = colorsCount;
        colorsCount = i163 + 1;
        key_actionBarWhiteSelector = i163;
        int i164 = colorsCount;
        colorsCount = i164 + 1;
        key_actionBarDefaultIcon = i164;
        int i165 = colorsCount;
        colorsCount = i165 + 1;
        key_actionBarActionModeDefault = i165;
        int i166 = colorsCount;
        colorsCount = i166 + 1;
        key_actionBarActionModeDefaultTop = i166;
        int i167 = colorsCount;
        colorsCount = i167 + 1;
        key_actionBarActionModeDefaultIcon = i167;
        int i168 = colorsCount;
        colorsCount = i168 + 1;
        key_actionBarActionModeDefaultSelector = i168;
        int i169 = colorsCount;
        colorsCount = i169 + 1;
        key_actionBarDefaultTitle = i169;
        int i170 = colorsCount;
        colorsCount = i170 + 1;
        key_actionBarDefaultSubtitle = i170;
        int i171 = colorsCount;
        colorsCount = i171 + 1;
        key_actionBarDefaultSearch = i171;
        int i172 = colorsCount;
        colorsCount = i172 + 1;
        key_actionBarDefaultSearchPlaceholder = i172;
        int i173 = colorsCount;
        colorsCount = i173 + 1;
        key_actionBarDefaultSubmenuItem = i173;
        int i174 = colorsCount;
        colorsCount = i174 + 1;
        key_actionBarDefaultSubmenuItemIcon = i174;
        int i175 = colorsCount;
        colorsCount = i175 + 1;
        key_actionBarDefaultSubmenuBackground = i175;
        int i176 = colorsCount;
        colorsCount = i176 + 1;
        key_actionBarDefaultSubmenuSeparator = i176;
        int i177 = colorsCount;
        colorsCount = i177 + 1;
        key_actionBarTabActiveText = i177;
        int i178 = colorsCount;
        colorsCount = i178 + 1;
        key_actionBarTabUnactiveText = i178;
        int i179 = colorsCount;
        colorsCount = i179 + 1;
        key_actionBarTabLine = i179;
        int i180 = colorsCount;
        colorsCount = i180 + 1;
        key_actionBarTabSelector = i180;
        int i181 = colorsCount;
        colorsCount = i181 + 1;
        key_actionBarDefaultArchived = i181;
        int i182 = colorsCount;
        colorsCount = i182 + 1;
        key_actionBarDefaultArchivedSelector = i182;
        int i183 = colorsCount;
        colorsCount = i183 + 1;
        key_actionBarDefaultArchivedIcon = i183;
        int i184 = colorsCount;
        colorsCount = i184 + 1;
        key_actionBarDefaultArchivedTitle = i184;
        int i185 = colorsCount;
        colorsCount = i185 + 1;
        key_actionBarDefaultArchivedSearch = i185;
        int i186 = colorsCount;
        colorsCount = i186 + 1;
        key_actionBarDefaultArchivedSearchPlaceholder = i186;
        int i187 = colorsCount;
        colorsCount = i187 + 1;
        key_actionBarBrowser = i187;
        int i188 = colorsCount;
        colorsCount = i188 + 1;
        key_chats_onlineCircle = i188;
        int i189 = colorsCount;
        colorsCount = i189 + 1;
        key_chats_unreadCounter = i189;
        int i190 = colorsCount;
        colorsCount = i190 + 1;
        key_chats_unreadCounterMuted = i190;
        int i191 = colorsCount;
        colorsCount = i191 + 1;
        key_chats_unreadCounterText = i191;
        int i192 = colorsCount;
        colorsCount = i192 + 1;
        key_chats_name = i192;
        int i193 = colorsCount;
        colorsCount = i193 + 1;
        key_chats_nameArchived = i193;
        int i194 = colorsCount;
        colorsCount = i194 + 1;
        key_chats_secretName = i194;
        int i195 = colorsCount;
        colorsCount = i195 + 1;
        key_chats_secretIcon = i195;
        int i196 = colorsCount;
        colorsCount = i196 + 1;
        key_chats_pinnedIcon = i196;
        int i197 = colorsCount;
        colorsCount = i197 + 1;
        key_chats_archiveBackground = i197;
        int i198 = colorsCount;
        colorsCount = i198 + 1;
        key_chats_archivePinBackground = i198;
        int i199 = colorsCount;
        colorsCount = i199 + 1;
        key_chats_archiveIcon = i199;
        int i200 = colorsCount;
        colorsCount = i200 + 1;
        key_chats_archiveText = i200;
        int i201 = colorsCount;
        colorsCount = i201 + 1;
        key_chats_message = i201;
        int i202 = colorsCount;
        colorsCount = i202 + 1;
        key_chats_messageArchived = i202;
        int i203 = colorsCount;
        colorsCount = i203 + 1;
        key_chats_message_threeLines = i203;
        int i204 = colorsCount;
        colorsCount = i204 + 1;
        key_chats_draft = i204;
        int i205 = colorsCount;
        colorsCount = i205 + 1;
        key_chats_nameMessage = i205;
        int i206 = colorsCount;
        colorsCount = i206 + 1;
        key_chats_nameMessageArchived = i206;
        int i207 = colorsCount;
        colorsCount = i207 + 1;
        key_chats_nameMessage_threeLines = i207;
        int i208 = colorsCount;
        colorsCount = i208 + 1;
        key_chats_nameMessageArchived_threeLines = i208;
        int i209 = colorsCount;
        colorsCount = i209 + 1;
        key_chats_attachMessage = i209;
        int i210 = colorsCount;
        colorsCount = i210 + 1;
        key_chats_actionMessage = i210;
        int i211 = colorsCount;
        colorsCount = i211 + 1;
        key_chats_date = i211;
        int i212 = colorsCount;
        colorsCount = i212 + 1;
        key_chats_pinnedOverlay = i212;
        int i213 = colorsCount;
        colorsCount = i213 + 1;
        key_chats_tabletSelectedOverlay = i213;
        int i214 = colorsCount;
        colorsCount = i214 + 1;
        key_chats_sentCheck = i214;
        int i215 = colorsCount;
        colorsCount = i215 + 1;
        key_chats_sentReadCheck = i215;
        int i216 = colorsCount;
        colorsCount = i216 + 1;
        key_chats_sentClock = i216;
        int i217 = colorsCount;
        colorsCount = i217 + 1;
        key_chats_sentError = i217;
        int i218 = colorsCount;
        colorsCount = i218 + 1;
        key_chats_sentErrorIcon = i218;
        int i219 = colorsCount;
        colorsCount = i219 + 1;
        key_chats_verifiedBackground = i219;
        int i220 = colorsCount;
        colorsCount = i220 + 1;
        key_chats_verifiedCheck = i220;
        int i221 = colorsCount;
        colorsCount = i221 + 1;
        key_chats_muteIcon = i221;
        int i222 = colorsCount;
        colorsCount = i222 + 1;
        key_chats_mentionIcon = i222;
        int i223 = colorsCount;
        colorsCount = i223 + 1;
        key_chats_menuTopShadow = i223;
        int i224 = colorsCount;
        colorsCount = i224 + 1;
        key_chats_menuTopShadowCats = i224;
        int i225 = colorsCount;
        colorsCount = i225 + 1;
        key_chats_menuBackground = i225;
        int i226 = colorsCount;
        colorsCount = i226 + 1;
        key_chats_menuItemText = i226;
        int i227 = colorsCount;
        colorsCount = i227 + 1;
        key_chats_menuItemCheck = i227;
        int i228 = colorsCount;
        colorsCount = i228 + 1;
        key_chats_menuItemIcon = i228;
        int i229 = colorsCount;
        colorsCount = i229 + 1;
        key_chats_menuName = i229;
        int i230 = colorsCount;
        colorsCount = i230 + 1;
        key_chats_menuPhone = i230;
        int i231 = colorsCount;
        colorsCount = i231 + 1;
        key_chats_menuPhoneCats = i231;
        int i232 = colorsCount;
        colorsCount = i232 + 1;
        key_chats_menuTopBackgroundCats = i232;
        int i233 = colorsCount;
        colorsCount = i233 + 1;
        key_chats_menuTopBackground = i233;
        int i234 = colorsCount;
        colorsCount = i234 + 1;
        key_chats_actionIcon = i234;
        int i235 = colorsCount;
        colorsCount = i235 + 1;
        key_chats_actionBackground = i235;
        int i236 = colorsCount;
        colorsCount = i236 + 1;
        key_chats_actionPressedBackground = i236;
        int i237 = colorsCount;
        colorsCount = i237 + 1;
        key_chats_archivePullDownBackground = i237;
        int i238 = colorsCount;
        colorsCount = i238 + 1;
        key_chats_archivePullDownBackgroundActive = i238;
        int i239 = colorsCount;
        colorsCount = i239 + 1;
        key_chats_tabUnreadActiveBackground = i239;
        int i240 = colorsCount;
        colorsCount = i240 + 1;
        key_chats_tabUnreadUnactiveBackground = i240;
        int i241 = colorsCount;
        colorsCount = i241 + 1;
        key_chat_attachCheckBoxCheck = i241;
        int i242 = colorsCount;
        colorsCount = i242 + 1;
        key_chat_attachCheckBoxBackground = i242;
        int i243 = colorsCount;
        colorsCount = i243 + 1;
        key_chat_attachPhotoBackground = i243;
        int i244 = colorsCount;
        colorsCount = i244 + 1;
        key_chat_attachActiveTab = i244;
        int i245 = colorsCount;
        colorsCount = i245 + 1;
        key_chat_attachUnactiveTab = i245;
        int i246 = colorsCount;
        colorsCount = i246 + 1;
        key_chat_attachPermissionImage = i246;
        int i247 = colorsCount;
        colorsCount = i247 + 1;
        key_chat_attachPermissionMark = i247;
        int i248 = colorsCount;
        colorsCount = i248 + 1;
        key_chat_attachPermissionText = i248;
        int i249 = colorsCount;
        colorsCount = i249 + 1;
        key_chat_attachEmptyImage = i249;
        int i250 = colorsCount;
        colorsCount = i250 + 1;
        key_chat_inPollCorrectAnswer = i250;
        int i251 = colorsCount;
        colorsCount = i251 + 1;
        key_chat_outPollCorrectAnswer = i251;
        int i252 = colorsCount;
        colorsCount = i252 + 1;
        key_chat_inPollWrongAnswer = i252;
        int i253 = colorsCount;
        colorsCount = i253 + 1;
        key_chat_outPollWrongAnswer = i253;
        int i254 = colorsCount;
        colorsCount = i254 + 1;
        key_chat_attachIcon = i254;
        int i255 = colorsCount;
        colorsCount = i255 + 1;
        key_chat_attachGalleryBackground = i255;
        int i256 = colorsCount;
        colorsCount = i256 + 1;
        key_chat_attachGalleryText = i256;
        int i257 = colorsCount;
        colorsCount = i257 + 1;
        key_chat_attachAudioBackground = i257;
        int i258 = colorsCount;
        colorsCount = i258 + 1;
        key_chat_attachAudioText = i258;
        int i259 = colorsCount;
        colorsCount = i259 + 1;
        key_chat_attachFileBackground = i259;
        int i260 = colorsCount;
        colorsCount = i260 + 1;
        key_chat_attachFileText = i260;
        int i261 = colorsCount;
        colorsCount = i261 + 1;
        key_chat_attachContactBackground = i261;
        int i262 = colorsCount;
        colorsCount = i262 + 1;
        key_chat_attachContactText = i262;
        int i263 = colorsCount;
        colorsCount = i263 + 1;
        key_chat_attachLocationBackground = i263;
        int i264 = colorsCount;
        colorsCount = i264 + 1;
        key_chat_attachLocationText = i264;
        int i265 = colorsCount;
        colorsCount = i265 + 1;
        key_chat_attachPollBackground = i265;
        int i266 = colorsCount;
        colorsCount = i266 + 1;
        key_chat_attachPollText = i266;
        int i267 = colorsCount;
        colorsCount = i267 + 1;
        key_chat_status = i267;
        int i268 = colorsCount;
        colorsCount = i268 + 1;
        key_chat_inGreenCall = i268;
        int i269 = colorsCount;
        colorsCount = i269 + 1;
        key_chat_inBubble = i269;
        int i270 = colorsCount;
        colorsCount = i270 + 1;
        key_chat_inBubbleSelectedOverlay = i270;
        int i271 = colorsCount;
        colorsCount = i271 + 1;
        key_chat_inBubbleShadow = i271;
        myMessagesBubblesStartIndex = colorsCount;
        int i272 = colorsCount;
        colorsCount = i272 + 1;
        key_chat_outBubble = i272;
        int i273 = colorsCount;
        colorsCount = i273 + 1;
        key_chat_outBubbleSelected = i273;
        int i274 = colorsCount;
        colorsCount = i274 + 1;
        key_chat_outBubbleShadow = i274;
        int i275 = colorsCount;
        colorsCount = i275 + 1;
        key_chat_outBubbleGradient1 = i275;
        int i276 = colorsCount;
        colorsCount = i276 + 1;
        key_chat_outBubbleGradient2 = i276;
        int i277 = colorsCount;
        colorsCount = i277 + 1;
        key_chat_outBubbleGradient3 = i277;
        myMessagesBubblesEndIndex = colorsCount;
        myMessagesStartIndex = colorsCount;
        int i278 = colorsCount;
        colorsCount = i278 + 1;
        key_chat_outGreenCall = i278;
        int i279 = colorsCount;
        colorsCount = i279 + 1;
        key_chat_outSentCheck = i279;
        int i280 = colorsCount;
        colorsCount = i280 + 1;
        key_chat_outSentCheckSelected = i280;
        int i281 = colorsCount;
        colorsCount = i281 + 1;
        key_chat_outSentCheckRead = i281;
        int i282 = colorsCount;
        colorsCount = i282 + 1;
        key_chat_outSentCheckReadSelected = i282;
        int i283 = colorsCount;
        colorsCount = i283 + 1;
        key_chat_outSentClock = i283;
        int i284 = colorsCount;
        colorsCount = i284 + 1;
        key_chat_outSentClockSelected = i284;
        int i285 = colorsCount;
        colorsCount = i285 + 1;
        key_chat_outMediaIcon = i285;
        int i286 = colorsCount;
        colorsCount = i286 + 1;
        key_chat_outMediaIconSelected = i286;
        int i287 = colorsCount;
        colorsCount = i287 + 1;
        key_chat_outViews = i287;
        int i288 = colorsCount;
        colorsCount = i288 + 1;
        key_chat_outViewsSelected = i288;
        int i289 = colorsCount;
        colorsCount = i289 + 1;
        key_chat_outMenu = i289;
        int i290 = colorsCount;
        colorsCount = i290 + 1;
        key_chat_outMenuSelected = i290;
        int i291 = colorsCount;
        colorsCount = i291 + 1;
        key_chat_outInstant = i291;
        int i292 = colorsCount;
        colorsCount = i292 + 1;
        key_chat_outInstantSelected = i292;
        int i293 = colorsCount;
        colorsCount = i293 + 1;
        key_chat_outPreviewInstantText = i293;
        int i294 = colorsCount;
        colorsCount = i294 + 1;
        key_chat_outForwardedNameText = i294;
        int i295 = colorsCount;
        colorsCount = i295 + 1;
        key_chat_outViaBotNameText = i295;
        int i296 = colorsCount;
        colorsCount = i296 + 1;
        key_chat_outReplyLine = i296;
        int i297 = colorsCount;
        colorsCount = i297 + 1;
        key_chat_outReplyNameText = i297;
        int i298 = colorsCount;
        colorsCount = i298 + 1;
        key_chat_outReplyMessageText = i298;
        int i299 = colorsCount;
        colorsCount = i299 + 1;
        key_chat_outReplyMediaMessageText = i299;
        int i300 = colorsCount;
        colorsCount = i300 + 1;
        key_chat_outReplyMediaMessageSelectedText = i300;
        int i301 = colorsCount;
        colorsCount = i301 + 1;
        key_chat_outPreviewLine = i301;
        int i302 = colorsCount;
        colorsCount = i302 + 1;
        key_chat_outSiteNameText = i302;
        int i303 = colorsCount;
        colorsCount = i303 + 1;
        key_chat_outContactNameText = i303;
        int i304 = colorsCount;
        colorsCount = i304 + 1;
        key_chat_outContactPhoneText = i304;
        int i305 = colorsCount;
        colorsCount = i305 + 1;
        key_chat_outContactPhoneSelectedText = i305;
        int i306 = colorsCount;
        colorsCount = i306 + 1;
        key_chat_outAudioPerformerText = i306;
        int i307 = colorsCount;
        colorsCount = i307 + 1;
        key_chat_outAudioPerformerSelectedText = i307;
        int i308 = colorsCount;
        colorsCount = i308 + 1;
        key_chat_outTimeSelectedText = i308;
        int i309 = colorsCount;
        colorsCount = i309 + 1;
        key_chat_outAdminText = i309;
        int i310 = colorsCount;
        colorsCount = i310 + 1;
        key_chat_outAdminSelectedText = i310;
        int i311 = colorsCount;
        colorsCount = i311 + 1;
        key_chat_outAudioProgress = i311;
        int i312 = colorsCount;
        colorsCount = i312 + 1;
        key_chat_outAudioSelectedProgress = i312;
        int i313 = colorsCount;
        colorsCount = i313 + 1;
        key_chat_outTimeText = i313;
        int i314 = colorsCount;
        colorsCount = i314 + 1;
        key_chat_outAudioTitleText = i314;
        int i315 = colorsCount;
        colorsCount = i315 + 1;
        key_chat_outAudioDurationText = i315;
        int i316 = colorsCount;
        colorsCount = i316 + 1;
        key_chat_outAudioDurationSelectedText = i316;
        int i317 = colorsCount;
        colorsCount = i317 + 1;
        key_chat_outAudioSeekbar = i317;
        int i318 = colorsCount;
        colorsCount = i318 + 1;
        key_chat_outAudioCacheSeekbar = i318;
        int i319 = colorsCount;
        colorsCount = i319 + 1;
        key_chat_outAudioSeekbarSelected = i319;
        int i320 = colorsCount;
        colorsCount = i320 + 1;
        key_chat_outAudioSeekbarFill = i320;
        int i321 = colorsCount;
        colorsCount = i321 + 1;
        key_chat_outVoiceSeekbar = i321;
        int i322 = colorsCount;
        colorsCount = i322 + 1;
        key_chat_outVoiceSeekbarSelected = i322;
        int i323 = colorsCount;
        colorsCount = i323 + 1;
        key_chat_outVoiceSeekbarFill = i323;
        int i324 = colorsCount;
        colorsCount = i324 + 1;
        key_chat_outFileProgress = i324;
        int i325 = colorsCount;
        colorsCount = i325 + 1;
        key_chat_outFileProgressSelected = i325;
        int i326 = colorsCount;
        colorsCount = i326 + 1;
        key_chat_outFileNameText = i326;
        int i327 = colorsCount;
        colorsCount = i327 + 1;
        key_chat_outFileInfoText = i327;
        int i328 = colorsCount;
        colorsCount = i328 + 1;
        key_chat_outFileInfoSelectedText = i328;
        int i329 = colorsCount;
        colorsCount = i329 + 1;
        key_chat_outFileBackground = i329;
        int i330 = colorsCount;
        colorsCount = i330 + 1;
        key_chat_outFileBackgroundSelected = i330;
        int i331 = colorsCount;
        colorsCount = i331 + 1;
        key_chat_outVenueInfoText = i331;
        int i332 = colorsCount;
        colorsCount = i332 + 1;
        key_chat_outVenueInfoSelectedText = i332;
        int i333 = colorsCount;
        colorsCount = i333 + 1;
        key_chat_outLinkSelectBackground = i333;
        int i334 = colorsCount;
        colorsCount = i334 + 1;
        key_chat_outLoader = i334;
        int i335 = colorsCount;
        colorsCount = i335 + 1;
        key_chat_outLoaderSelected = i335;
        int i336 = colorsCount;
        colorsCount = i336 + 1;
        key_chat_outLocationIcon = i336;
        int i337 = colorsCount;
        colorsCount = i337 + 1;
        key_chat_outContactBackground = i337;
        int i338 = colorsCount;
        colorsCount = i338 + 1;
        key_chat_outContactIcon = i338;
        myMessagesEndIndex = colorsCount;
        int i339 = colorsCount;
        colorsCount = i339 + 1;
        key_chat_outTextSelectionHighlight = i339;
        int i340 = colorsCount;
        colorsCount = i340 + 1;
        key_chat_outTextSelectionCursor = i340;
        int i341 = colorsCount;
        colorsCount = i341 + 1;
        key_chat_outBubbleLocationPlaceholder = i341;
        int i342 = colorsCount;
        colorsCount = i342 + 1;
        key_chat_outBubbleSelectedOverlay = i342;
        int i343 = colorsCount;
        colorsCount = i343 + 1;
        key_chat_outPsaNameText = i343;
        int i344 = colorsCount;
        colorsCount = i344 + 1;
        key_chat_outBubbleGradientAnimated = i344;
        int i345 = colorsCount;
        colorsCount = i345 + 1;
        key_chat_outBubbleGradientSelectedOverlay = i345;
        int i346 = colorsCount;
        colorsCount = i346 + 1;
        key_chat_inBubbleSelected = i346;
        int i347 = colorsCount;
        colorsCount = i347 + 1;
        key_chat_messageTextIn = i347;
        int i348 = colorsCount;
        colorsCount = i348 + 1;
        key_chat_messageTextOut = i348;
        int i349 = colorsCount;
        colorsCount = i349 + 1;
        key_chat_messageLinkIn = i349;
        int i350 = colorsCount;
        colorsCount = i350 + 1;
        key_chat_messageLinkOut = i350;
        int i351 = colorsCount;
        colorsCount = i351 + 1;
        key_chat_serviceText = i351;
        int i352 = colorsCount;
        colorsCount = i352 + 1;
        key_chat_serviceLink = i352;
        int i353 = colorsCount;
        colorsCount = i353 + 1;
        key_chat_serviceIcon = i353;
        int i354 = colorsCount;
        colorsCount = i354 + 1;
        key_chat_serviceBackground = i354;
        int i355 = colorsCount;
        colorsCount = i355 + 1;
        key_chat_serviceBackgroundSelected = i355;
        int i356 = colorsCount;
        colorsCount = i356 + 1;
        key_chat_serviceBackgroundSelector = i356;
        int i357 = colorsCount;
        colorsCount = i357 + 1;
        key_chat_muteIcon = i357;
        int i358 = colorsCount;
        colorsCount = i358 + 1;
        key_chat_lockIcon = i358;
        int i359 = colorsCount;
        colorsCount = i359 + 1;
        key_chat_inSentClock = i359;
        int i360 = colorsCount;
        colorsCount = i360 + 1;
        key_chat_inSentClockSelected = i360;
        int i361 = colorsCount;
        colorsCount = i361 + 1;
        key_chat_mediaSentCheck = i361;
        int i362 = colorsCount;
        colorsCount = i362 + 1;
        key_chat_mediaSentClock = i362;
        int i363 = colorsCount;
        colorsCount = i363 + 1;
        key_chat_inMediaIcon = i363;
        int i364 = colorsCount;
        colorsCount = i364 + 1;
        key_chat_inMediaIconSelected = i364;
        int i365 = colorsCount;
        colorsCount = i365 + 1;
        key_chat_mediaTimeBackground = i365;
        int i366 = colorsCount;
        colorsCount = i366 + 1;
        key_chat_inViews = i366;
        int i367 = colorsCount;
        colorsCount = i367 + 1;
        key_chat_inViewsSelected = i367;
        int i368 = colorsCount;
        colorsCount = i368 + 1;
        key_chat_mediaViews = i368;
        int i369 = colorsCount;
        colorsCount = i369 + 1;
        key_chat_inMenu = i369;
        int i370 = colorsCount;
        colorsCount = i370 + 1;
        key_chat_inMenuSelected = i370;
        int i371 = colorsCount;
        colorsCount = i371 + 1;
        key_chat_mediaMenu = i371;
        int i372 = colorsCount;
        colorsCount = i372 + 1;
        key_chat_inInstant = i372;
        int i373 = colorsCount;
        colorsCount = i373 + 1;
        key_chat_inInstantSelected = i373;
        int i374 = colorsCount;
        colorsCount = i374 + 1;
        key_chat_sentError = i374;
        int i375 = colorsCount;
        colorsCount = i375 + 1;
        key_chat_sentErrorIcon = i375;
        int i376 = colorsCount;
        colorsCount = i376 + 1;
        key_chat_selectedBackground = i376;
        int i377 = colorsCount;
        colorsCount = i377 + 1;
        key_chat_previewDurationText = i377;
        int i378 = colorsCount;
        colorsCount = i378 + 1;
        key_chat_previewGameText = i378;
        int i379 = colorsCount;
        colorsCount = i379 + 1;
        key_chat_inPreviewInstantText = i379;
        int i380 = colorsCount;
        colorsCount = i380 + 1;
        key_chat_secretTimeText = i380;
        int i381 = colorsCount;
        colorsCount = i381 + 1;
        key_chat_stickerNameText = i381;
        int i382 = colorsCount;
        colorsCount = i382 + 1;
        key_chat_botButtonText = i382;
        int i383 = colorsCount;
        colorsCount = i383 + 1;
        key_chat_inForwardedNameText = i383;
        int i384 = colorsCount;
        colorsCount = i384 + 1;
        key_chat_inPsaNameText = i384;
        int i385 = colorsCount;
        colorsCount = i385 + 1;
        key_chat_inViaBotNameText = i385;
        int i386 = colorsCount;
        colorsCount = i386 + 1;
        key_chat_stickerViaBotNameText = i386;
        int i387 = colorsCount;
        colorsCount = i387 + 1;
        key_chat_inReplyLine = i387;
        int i388 = colorsCount;
        colorsCount = i388 + 1;
        key_chat_stickerReplyLine = i388;
        int i389 = colorsCount;
        colorsCount = i389 + 1;
        key_chat_inReplyNameText = i389;
        int i390 = colorsCount;
        colorsCount = i390 + 1;
        key_chat_stickerReplyNameText = i390;
        int i391 = colorsCount;
        colorsCount = i391 + 1;
        key_chat_inReplyMessageText = i391;
        int i392 = colorsCount;
        colorsCount = i392 + 1;
        key_chat_inReplyMediaMessageText = i392;
        int i393 = colorsCount;
        colorsCount = i393 + 1;
        key_chat_inReplyMediaMessageSelectedText = i393;
        int i394 = colorsCount;
        colorsCount = i394 + 1;
        key_chat_stickerReplyMessageText = i394;
        int i395 = colorsCount;
        colorsCount = i395 + 1;
        key_chat_inPreviewLine = i395;
        int i396 = colorsCount;
        colorsCount = i396 + 1;
        key_chat_inSiteNameText = i396;
        int i397 = colorsCount;
        colorsCount = i397 + 1;
        key_chat_inContactNameText = i397;
        int i398 = colorsCount;
        colorsCount = i398 + 1;
        key_chat_inContactPhoneText = i398;
        int i399 = colorsCount;
        colorsCount = i399 + 1;
        key_chat_inContactPhoneSelectedText = i399;
        int i400 = colorsCount;
        colorsCount = i400 + 1;
        key_chat_mediaProgress = i400;
        int i401 = colorsCount;
        colorsCount = i401 + 1;
        key_chat_inAudioProgress = i401;
        int i402 = colorsCount;
        colorsCount = i402 + 1;
        key_chat_inAudioSelectedProgress = i402;
        int i403 = colorsCount;
        colorsCount = i403 + 1;
        key_chat_mediaTimeText = i403;
        int i404 = colorsCount;
        colorsCount = i404 + 1;
        key_chat_inAdminText = i404;
        int i405 = colorsCount;
        colorsCount = i405 + 1;
        key_chat_inAdminSelectedText = i405;
        int i406 = colorsCount;
        colorsCount = i406 + 1;
        key_chat_inTimeText = i406;
        int i407 = colorsCount;
        colorsCount = i407 + 1;
        key_chat_inTimeSelectedText = i407;
        int i408 = colorsCount;
        colorsCount = i408 + 1;
        key_chat_inAudioPerformerText = i408;
        int i409 = colorsCount;
        colorsCount = i409 + 1;
        key_chat_inAudioPerformerSelectedText = i409;
        int i410 = colorsCount;
        colorsCount = i410 + 1;
        key_chat_inAudioTitleText = i410;
        int i411 = colorsCount;
        colorsCount = i411 + 1;
        key_chat_inAudioDurationText = i411;
        int i412 = colorsCount;
        colorsCount = i412 + 1;
        key_chat_inAudioDurationSelectedText = i412;
        int i413 = colorsCount;
        colorsCount = i413 + 1;
        key_chat_inAudioSeekbar = i413;
        int i414 = colorsCount;
        colorsCount = i414 + 1;
        key_chat_inAudioCacheSeekbar = i414;
        int i415 = colorsCount;
        colorsCount = i415 + 1;
        key_chat_inAudioSeekbarSelected = i415;
        int i416 = colorsCount;
        colorsCount = i416 + 1;
        key_chat_inAudioSeekbarFill = i416;
        int i417 = colorsCount;
        colorsCount = i417 + 1;
        key_chat_inVoiceSeekbar = i417;
        int i418 = colorsCount;
        colorsCount = i418 + 1;
        key_chat_inVoiceSeekbarSelected = i418;
        int i419 = colorsCount;
        colorsCount = i419 + 1;
        key_chat_inVoiceSeekbarFill = i419;
        int i420 = colorsCount;
        colorsCount = i420 + 1;
        key_chat_inFileProgress = i420;
        int i421 = colorsCount;
        colorsCount = i421 + 1;
        key_chat_inFileProgressSelected = i421;
        int i422 = colorsCount;
        colorsCount = i422 + 1;
        key_chat_inFileNameText = i422;
        int i423 = colorsCount;
        colorsCount = i423 + 1;
        key_chat_inFileInfoText = i423;
        int i424 = colorsCount;
        colorsCount = i424 + 1;
        key_chat_inFileInfoSelectedText = i424;
        int i425 = colorsCount;
        colorsCount = i425 + 1;
        key_chat_inFileBackground = i425;
        int i426 = colorsCount;
        colorsCount = i426 + 1;
        key_chat_inFileBackgroundSelected = i426;
        int i427 = colorsCount;
        colorsCount = i427 + 1;
        key_chat_inVenueInfoText = i427;
        int i428 = colorsCount;
        colorsCount = i428 + 1;
        key_chat_inVenueInfoSelectedText = i428;
        int i429 = colorsCount;
        colorsCount = i429 + 1;
        key_chat_mediaInfoText = i429;
        int i430 = colorsCount;
        colorsCount = i430 + 1;
        key_chat_linkSelectBackground = i430;
        int i431 = colorsCount;
        colorsCount = i431 + 1;
        key_chat_textSelectBackground = i431;
        int i432 = colorsCount;
        colorsCount = i432 + 1;
        key_chat_wallpaper = i432;
        int i433 = colorsCount;
        colorsCount = i433 + 1;
        key_chat_wallpaper_gradient_to1 = i433;
        int i434 = colorsCount;
        colorsCount = i434 + 1;
        key_chat_wallpaper_gradient_to2 = i434;
        int i435 = colorsCount;
        colorsCount = i435 + 1;
        key_chat_wallpaper_gradient_to3 = i435;
        int i436 = colorsCount;
        colorsCount = i436 + 1;
        key_chat_wallpaper_gradient_rotation = i436;
        int i437 = colorsCount;
        colorsCount = i437 + 1;
        key_chat_messagePanelBackground = i437;
        int i438 = colorsCount;
        colorsCount = i438 + 1;
        key_chat_messagePanelShadow = i438;
        int i439 = colorsCount;
        colorsCount = i439 + 1;
        key_chat_messagePanelText = i439;
        int i440 = colorsCount;
        colorsCount = i440 + 1;
        key_chat_messagePanelHint = i440;
        int i441 = colorsCount;
        colorsCount = i441 + 1;
        key_chat_messagePanelCursor = i441;
        int i442 = colorsCount;
        colorsCount = i442 + 1;
        key_chat_messagePanelIcons = i442;
        int i443 = colorsCount;
        colorsCount = i443 + 1;
        key_chat_messagePanelSend = i443;
        int i444 = colorsCount;
        colorsCount = i444 + 1;
        key_chat_messagePanelVoiceLock = i444;
        int i445 = colorsCount;
        colorsCount = i445 + 1;
        key_chat_messagePanelVoiceLockBackground = i445;
        int i446 = colorsCount;
        colorsCount = i446 + 1;
        key_chat_messagePanelVoiceLockShadow = i446;
        int i447 = colorsCount;
        colorsCount = i447 + 1;
        key_chat_topPanelBackground = i447;
        int i448 = colorsCount;
        colorsCount = i448 + 1;
        key_chat_topPanelClose = i448;
        int i449 = colorsCount;
        colorsCount = i449 + 1;
        key_chat_topPanelLine = i449;
        int i450 = colorsCount;
        colorsCount = i450 + 1;
        key_chat_topPanelTitle = i450;
        int i451 = colorsCount;
        colorsCount = i451 + 1;
        key_chat_topPanelMessage = i451;
        int i452 = colorsCount;
        colorsCount = i452 + 1;
        key_chat_addContact = i452;
        int i453 = colorsCount;
        colorsCount = i453 + 1;
        key_chat_inLoader = i453;
        int i454 = colorsCount;
        colorsCount = i454 + 1;
        key_chat_inLoaderSelected = i454;
        int i455 = colorsCount;
        colorsCount = i455 + 1;
        key_chat_inLoaderPhoto = i455;
        int i456 = colorsCount;
        colorsCount = i456 + 1;
        key_chat_mediaLoaderPhoto = i456;
        int i457 = colorsCount;
        colorsCount = i457 + 1;
        key_chat_mediaLoaderPhotoSelected = i457;
        int i458 = colorsCount;
        colorsCount = i458 + 1;
        key_chat_mediaLoaderPhotoIcon = i458;
        int i459 = colorsCount;
        colorsCount = i459 + 1;
        key_chat_mediaLoaderPhotoIconSelected = i459;
        int i460 = colorsCount;
        colorsCount = i460 + 1;
        key_chat_inLocationBackground = i460;
        int i461 = colorsCount;
        colorsCount = i461 + 1;
        key_chat_inLocationIcon = i461;
        int i462 = colorsCount;
        colorsCount = i462 + 1;
        key_chat_inContactBackground = i462;
        int i463 = colorsCount;
        colorsCount = i463 + 1;
        key_chat_inContactIcon = i463;
        int i464 = colorsCount;
        colorsCount = i464 + 1;
        key_chat_replyPanelIcons = i464;
        int i465 = colorsCount;
        colorsCount = i465 + 1;
        key_chat_replyPanelClose = i465;
        int i466 = colorsCount;
        colorsCount = i466 + 1;
        key_chat_replyPanelName = i466;
        int i467 = colorsCount;
        colorsCount = i467 + 1;
        key_chat_replyPanelLine = i467;
        int i468 = colorsCount;
        colorsCount = i468 + 1;
        key_chat_searchPanelIcons = i468;
        int i469 = colorsCount;
        colorsCount = i469 + 1;
        key_chat_searchPanelText = i469;
        int i470 = colorsCount;
        colorsCount = i470 + 1;
        key_chat_secretChatStatusText = i470;
        int i471 = colorsCount;
        colorsCount = i471 + 1;
        key_chat_fieldOverlayText = i471;
        int i472 = colorsCount;
        colorsCount = i472 + 1;
        key_chat_stickersHintPanel = i472;
        int i473 = colorsCount;
        colorsCount = i473 + 1;
        key_chat_botSwitchToInlineText = i473;
        int i474 = colorsCount;
        colorsCount = i474 + 1;
        key_chat_unreadMessagesStartArrowIcon = i474;
        int i475 = colorsCount;
        colorsCount = i475 + 1;
        key_chat_unreadMessagesStartText = i475;
        int i476 = colorsCount;
        colorsCount = i476 + 1;
        key_chat_unreadMessagesStartBackground = i476;
        int i477 = colorsCount;
        colorsCount = i477 + 1;
        key_chat_inlineResultIcon = i477;
        int i478 = colorsCount;
        colorsCount = i478 + 1;
        key_chat_emojiPanelBackground = i478;
        int i479 = colorsCount;
        colorsCount = i479 + 1;
        key_chat_emojiSearchBackground = i479;
        int i480 = colorsCount;
        colorsCount = i480 + 1;
        key_chat_emojiSearchIcon = i480;
        int i481 = colorsCount;
        colorsCount = i481 + 1;
        key_chat_emojiPanelShadowLine = i481;
        int i482 = colorsCount;
        colorsCount = i482 + 1;
        key_chat_emojiPanelEmptyText = i482;
        int i483 = colorsCount;
        colorsCount = i483 + 1;
        key_chat_emojiPanelIcon = i483;
        int i484 = colorsCount;
        colorsCount = i484 + 1;
        key_chat_emojiBottomPanelIcon = i484;
        int i485 = colorsCount;
        colorsCount = i485 + 1;
        key_chat_emojiPanelIconSelected = i485;
        int i486 = colorsCount;
        colorsCount = i486 + 1;
        key_chat_emojiPanelStickerPackSelector = i486;
        int i487 = colorsCount;
        colorsCount = i487 + 1;
        key_chat_emojiPanelStickerPackSelectorLine = i487;
        int i488 = colorsCount;
        colorsCount = i488 + 1;
        key_chat_emojiPanelBackspace = i488;
        int i489 = colorsCount;
        colorsCount = i489 + 1;
        key_chat_emojiPanelTrendingTitle = i489;
        int i490 = colorsCount;
        colorsCount = i490 + 1;
        key_chat_emojiPanelStickerSetName = i490;
        int i491 = colorsCount;
        colorsCount = i491 + 1;
        key_chat_emojiPanelStickerSetNameHighlight = i491;
        int i492 = colorsCount;
        colorsCount = i492 + 1;
        key_chat_emojiPanelStickerSetNameIcon = i492;
        int i493 = colorsCount;
        colorsCount = i493 + 1;
        key_chat_emojiPanelTrendingDescription = i493;
        int i494 = colorsCount;
        colorsCount = i494 + 1;
        key_chat_botKeyboardButtonText = i494;
        int i495 = colorsCount;
        colorsCount = i495 + 1;
        key_chat_botKeyboardButtonBackground = i495;
        int i496 = colorsCount;
        colorsCount = i496 + 1;
        key_chat_botKeyboardButtonBackgroundPressed = i496;
        int i497 = colorsCount;
        colorsCount = i497 + 1;
        key_chat_emojiPanelNewTrending = i497;
        int i498 = colorsCount;
        colorsCount = i498 + 1;
        key_chat_messagePanelVoicePressed = i498;
        int i499 = colorsCount;
        colorsCount = i499 + 1;
        key_chat_messagePanelVoiceBackground = i499;
        int i500 = colorsCount;
        colorsCount = i500 + 1;
        key_chat_messagePanelVoiceDelete = i500;
        int i501 = colorsCount;
        colorsCount = i501 + 1;
        key_chat_messagePanelVoiceDuration = i501;
        int i502 = colorsCount;
        colorsCount = i502 + 1;
        key_chat_recordedVoicePlayPause = i502;
        int i503 = colorsCount;
        colorsCount = i503 + 1;
        key_chat_recordedVoiceProgress = i503;
        int i504 = colorsCount;
        colorsCount = i504 + 1;
        key_chat_recordedVoiceProgressInner = i504;
        int i505 = colorsCount;
        colorsCount = i505 + 1;
        key_chat_recordedVoiceDot = i505;
        int i506 = colorsCount;
        colorsCount = i506 + 1;
        key_chat_recordedVoiceBackground = i506;
        int i507 = colorsCount;
        colorsCount = i507 + 1;
        key_chat_recordVoiceCancel = i507;
        int i508 = colorsCount;
        colorsCount = i508 + 1;
        key_chat_recordTime = i508;
        int i509 = colorsCount;
        colorsCount = i509 + 1;
        key_chat_messagePanelCancelInlineBot = i509;
        int i510 = colorsCount;
        colorsCount = i510 + 1;
        key_chat_gifSaveHintText = i510;
        int i511 = colorsCount;
        colorsCount = i511 + 1;
        key_chat_gifSaveHintBackground = i511;
        int i512 = colorsCount;
        colorsCount = i512 + 1;
        key_chat_goDownButton = i512;
        int i513 = colorsCount;
        colorsCount = i513 + 1;
        key_chat_goDownButtonIcon = i513;
        int i514 = colorsCount;
        colorsCount = i514 + 1;
        key_chat_goDownButtonCounter = i514;
        int i515 = colorsCount;
        colorsCount = i515 + 1;
        key_chat_goDownButtonCounterBackground = i515;
        int i516 = colorsCount;
        colorsCount = i516 + 1;
        key_chat_inTextSelectionHighlight = i516;
        int i517 = colorsCount;
        colorsCount = i517 + 1;
        key_chat_TextSelectionCursor = i517;
        int i518 = colorsCount;
        colorsCount = i518 + 1;
        key_chat_inBubbleLocationPlaceholder = i518;
        int i519 = colorsCount;
        colorsCount = i519 + 1;
        key_chat_BlurAlpha = i519;
        int i520 = colorsCount;
        colorsCount = i520 + 1;
        key_voipgroup_listSelector = i520;
        int i521 = colorsCount;
        colorsCount = i521 + 1;
        key_voipgroup_inviteMembersBackground = i521;
        int i522 = colorsCount;
        colorsCount = i522 + 1;
        key_voipgroup_actionBar = i522;
        int i523 = colorsCount;
        colorsCount = i523 + 1;
        key_voipgroup_actionBarItems = i523;
        int i524 = colorsCount;
        colorsCount = i524 + 1;
        key_voipgroup_actionBarItemsSelector = i524;
        int i525 = colorsCount;
        colorsCount = i525 + 1;
        key_voipgroup_actionBarUnscrolled = i525;
        int i526 = colorsCount;
        colorsCount = i526 + 1;
        key_voipgroup_listViewBackgroundUnscrolled = i526;
        int i527 = colorsCount;
        colorsCount = i527 + 1;
        key_voipgroup_lastSeenTextUnscrolled = i527;
        int i528 = colorsCount;
        colorsCount = i528 + 1;
        key_voipgroup_mutedIconUnscrolled = i528;
        int i529 = colorsCount;
        colorsCount = i529 + 1;
        key_voipgroup_nameText = i529;
        int i530 = colorsCount;
        colorsCount = i530 + 1;
        key_voipgroup_lastSeenText = i530;
        int i531 = colorsCount;
        colorsCount = i531 + 1;
        key_voipgroup_listeningText = i531;
        int i532 = colorsCount;
        colorsCount = i532 + 1;
        key_voipgroup_speakingText = i532;
        int i533 = colorsCount;
        colorsCount = i533 + 1;
        key_voipgroup_mutedIcon = i533;
        int i534 = colorsCount;
        colorsCount = i534 + 1;
        key_voipgroup_mutedByAdminIcon = i534;
        int i535 = colorsCount;
        colorsCount = i535 + 1;
        key_voipgroup_listViewBackground = i535;
        int i536 = colorsCount;
        colorsCount = i536 + 1;
        key_voipgroup_dialogBackground = i536;
        int i537 = colorsCount;
        colorsCount = i537 + 1;
        key_voipgroup_leaveCallMenu = i537;
        int i538 = colorsCount;
        colorsCount = i538 + 1;
        key_voipgroup_checkMenu = i538;
        int i539 = colorsCount;
        colorsCount = i539 + 1;
        key_voipgroup_soundButton = i539;
        int i540 = colorsCount;
        colorsCount = i540 + 1;
        key_voipgroup_soundButtonActive = i540;
        int i541 = colorsCount;
        colorsCount = i541 + 1;
        key_voipgroup_soundButtonActiveScrolled = i541;
        int i542 = colorsCount;
        colorsCount = i542 + 1;
        key_voipgroup_soundButton2 = i542;
        int i543 = colorsCount;
        colorsCount = i543 + 1;
        key_voipgroup_soundButtonActive2 = i543;
        int i544 = colorsCount;
        colorsCount = i544 + 1;
        key_voipgroup_soundButtonActive2Scrolled = i544;
        int i545 = colorsCount;
        colorsCount = i545 + 1;
        key_voipgroup_leaveButton = i545;
        int i546 = colorsCount;
        colorsCount = i546 + 1;
        key_voipgroup_leaveButtonScrolled = i546;
        int i547 = colorsCount;
        colorsCount = i547 + 1;
        key_voipgroup_muteButton = i547;
        int i548 = colorsCount;
        colorsCount = i548 + 1;
        key_voipgroup_muteButton2 = i548;
        int i549 = colorsCount;
        colorsCount = i549 + 1;
        key_voipgroup_muteButton3 = i549;
        int i550 = colorsCount;
        colorsCount = i550 + 1;
        key_voipgroup_unmuteButton = i550;
        int i551 = colorsCount;
        colorsCount = i551 + 1;
        key_voipgroup_unmuteButton2 = i551;
        int i552 = colorsCount;
        colorsCount = i552 + 1;
        key_voipgroup_disabledButton = i552;
        int i553 = colorsCount;
        colorsCount = i553 + 1;
        key_voipgroup_disabledButtonActive = i553;
        int i554 = colorsCount;
        colorsCount = i554 + 1;
        key_voipgroup_disabledButtonActiveScrolled = i554;
        int i555 = colorsCount;
        colorsCount = i555 + 1;
        key_voipgroup_connectingProgress = i555;
        int i556 = colorsCount;
        colorsCount = i556 + 1;
        key_voipgroup_scrollUp = i556;
        int i557 = colorsCount;
        colorsCount = i557 + 1;
        key_voipgroup_searchPlaceholder = i557;
        int i558 = colorsCount;
        colorsCount = i558 + 1;
        key_voipgroup_searchBackground = i558;
        int i559 = colorsCount;
        colorsCount = i559 + 1;
        key_voipgroup_searchText = i559;
        int i560 = colorsCount;
        colorsCount = i560 + 1;
        key_voipgroup_overlayGreen1 = i560;
        int i561 = colorsCount;
        colorsCount = i561 + 1;
        key_voipgroup_overlayGreen2 = i561;
        int i562 = colorsCount;
        colorsCount = i562 + 1;
        key_voipgroup_overlayBlue1 = i562;
        int i563 = colorsCount;
        colorsCount = i563 + 1;
        key_voipgroup_overlayBlue2 = i563;
        int i564 = colorsCount;
        colorsCount = i564 + 1;
        key_voipgroup_topPanelGreen1 = i564;
        int i565 = colorsCount;
        colorsCount = i565 + 1;
        key_voipgroup_topPanelGreen2 = i565;
        int i566 = colorsCount;
        colorsCount = i566 + 1;
        key_voipgroup_topPanelBlue1 = i566;
        int i567 = colorsCount;
        colorsCount = i567 + 1;
        key_voipgroup_topPanelBlue2 = i567;
        int i568 = colorsCount;
        colorsCount = i568 + 1;
        key_voipgroup_topPanelGray = i568;
        int i569 = colorsCount;
        colorsCount = i569 + 1;
        key_voipgroup_overlayAlertGradientMuted = i569;
        int i570 = colorsCount;
        colorsCount = i570 + 1;
        key_voipgroup_overlayAlertGradientMuted2 = i570;
        int i571 = colorsCount;
        colorsCount = i571 + 1;
        key_voipgroup_overlayAlertGradientUnmuted = i571;
        int i572 = colorsCount;
        colorsCount = i572 + 1;
        key_voipgroup_overlayAlertGradientUnmuted2 = i572;
        int i573 = colorsCount;
        colorsCount = i573 + 1;
        key_voipgroup_overlayAlertMutedByAdmin = i573;
        int i574 = colorsCount;
        colorsCount = i574 + 1;
        key_voipgroup_overlayAlertMutedByAdmin2 = i574;
        int i575 = colorsCount;
        colorsCount = i575 + 1;
        key_voipgroup_mutedByAdminGradient = i575;
        int i576 = colorsCount;
        colorsCount = i576 + 1;
        key_voipgroup_mutedByAdminGradient2 = i576;
        int i577 = colorsCount;
        colorsCount = i577 + 1;
        key_voipgroup_mutedByAdminGradient3 = i577;
        int i578 = colorsCount;
        colorsCount = i578 + 1;
        key_voipgroup_mutedByAdminMuteButton = i578;
        int i579 = colorsCount;
        colorsCount = i579 + 1;
        key_voipgroup_mutedByAdminMuteButtonDisabled = i579;
        int i580 = colorsCount;
        colorsCount = i580 + 1;
        key_voipgroup_windowBackgroundWhiteInputField = i580;
        int i581 = colorsCount;
        colorsCount = i581 + 1;
        key_voipgroup_windowBackgroundWhiteInputFieldActivated = i581;
        int i582 = colorsCount;
        colorsCount = i582 + 1;
        key_passport_authorizeBackground = i582;
        int i583 = colorsCount;
        colorsCount = i583 + 1;
        key_passport_authorizeBackgroundSelected = i583;
        int i584 = colorsCount;
        colorsCount = i584 + 1;
        key_passport_authorizeText = i584;
        int i585 = colorsCount;
        colorsCount = i585 + 1;
        key_profile_creatorIcon = i585;
        int i586 = colorsCount;
        colorsCount = i586 + 1;
        key_profile_title = i586;
        int i587 = colorsCount;
        colorsCount = i587 + 1;
        key_profile_actionIcon = i587;
        int i588 = colorsCount;
        colorsCount = i588 + 1;
        key_profile_actionBackground = i588;
        int i589 = colorsCount;
        colorsCount = i589 + 1;
        key_profile_actionPressedBackground = i589;
        int i590 = colorsCount;
        colorsCount = i590 + 1;
        key_profile_verifiedBackground = i590;
        int i591 = colorsCount;
        colorsCount = i591 + 1;
        key_profile_verifiedCheck = i591;
        int i592 = colorsCount;
        colorsCount = i592 + 1;
        key_profile_status = i592;
        int i593 = colorsCount;
        colorsCount = i593 + 1;
        key_profile_tabText = i593;
        int i594 = colorsCount;
        colorsCount = i594 + 1;
        key_profile_tabSelectedText = i594;
        int i595 = colorsCount;
        colorsCount = i595 + 1;
        key_profile_tabSelectedLine = i595;
        int i596 = colorsCount;
        colorsCount = i596 + 1;
        key_profile_tabSelector = i596;
        int i597 = colorsCount;
        colorsCount = i597 + 1;
        key_sharedMedia_startStopLoadIcon = i597;
        int i598 = colorsCount;
        colorsCount = i598 + 1;
        key_sharedMedia_linkPlaceholder = i598;
        int i599 = colorsCount;
        colorsCount = i599 + 1;
        key_sharedMedia_linkPlaceholderText = i599;
        int i600 = colorsCount;
        colorsCount = i600 + 1;
        key_sharedMedia_photoPlaceholder = i600;
        int i601 = colorsCount;
        colorsCount = i601 + 1;
        key_featuredStickers_addedIcon = i601;
        int i602 = colorsCount;
        colorsCount = i602 + 1;
        key_featuredStickers_buttonProgress = i602;
        int i603 = colorsCount;
        colorsCount = i603 + 1;
        key_featuredStickers_addButton = i603;
        int i604 = colorsCount;
        colorsCount = i604 + 1;
        key_featuredStickers_addButtonPressed = i604;
        int i605 = colorsCount;
        colorsCount = i605 + 1;
        key_featuredStickers_removeButtonText = i605;
        int i606 = colorsCount;
        colorsCount = i606 + 1;
        key_featuredStickers_buttonText = i606;
        int i607 = colorsCount;
        colorsCount = i607 + 1;
        key_featuredStickers_unread = i607;
        int i608 = colorsCount;
        colorsCount = i608 + 1;
        key_stickers_menu = i608;
        int i609 = colorsCount;
        colorsCount = i609 + 1;
        key_stickers_menuSelector = i609;
        int i610 = colorsCount;
        colorsCount = i610 + 1;
        key_changephoneinfo_image2 = i610;
        int i611 = colorsCount;
        colorsCount = i611 + 1;
        key_groupcreate_hintText = i611;
        int i612 = colorsCount;
        colorsCount = i612 + 1;
        key_groupcreate_cursor = i612;
        int i613 = colorsCount;
        colorsCount = i613 + 1;
        key_groupcreate_sectionShadow = i613;
        int i614 = colorsCount;
        colorsCount = i614 + 1;
        key_groupcreate_sectionText = i614;
        int i615 = colorsCount;
        colorsCount = i615 + 1;
        key_groupcreate_spanText = i615;
        int i616 = colorsCount;
        colorsCount = i616 + 1;
        key_groupcreate_spanBackground = i616;
        int i617 = colorsCount;
        colorsCount = i617 + 1;
        key_groupcreate_spanDelete = i617;
        int i618 = colorsCount;
        colorsCount = i618 + 1;
        key_contacts_inviteBackground = i618;
        int i619 = colorsCount;
        colorsCount = i619 + 1;
        key_contacts_inviteText = i619;
        int i620 = colorsCount;
        colorsCount = i620 + 1;
        key_login_progressInner = i620;
        int i621 = colorsCount;
        colorsCount = i621 + 1;
        key_login_progressOuter = i621;
        int i622 = colorsCount;
        colorsCount = i622 + 1;
        key_picker_enabledButton = i622;
        int i623 = colorsCount;
        colorsCount = i623 + 1;
        key_picker_disabledButton = i623;
        int i624 = colorsCount;
        colorsCount = i624 + 1;
        key_picker_badge = i624;
        int i625 = colorsCount;
        colorsCount = i625 + 1;
        key_picker_badgeText = i625;
        int i626 = colorsCount;
        colorsCount = i626 + 1;
        key_location_sendLocationBackground = i626;
        int i627 = colorsCount;
        colorsCount = i627 + 1;
        key_location_sendLocationIcon = i627;
        int i628 = colorsCount;
        colorsCount = i628 + 1;
        key_location_sendLocationText = i628;
        int i629 = colorsCount;
        colorsCount = i629 + 1;
        key_location_sendLiveLocationBackground = i629;
        int i630 = colorsCount;
        colorsCount = i630 + 1;
        key_location_sendLiveLocationIcon = i630;
        int i631 = colorsCount;
        colorsCount = i631 + 1;
        key_location_sendLiveLocationText = i631;
        int i632 = colorsCount;
        colorsCount = i632 + 1;
        key_location_liveLocationProgress = i632;
        int i633 = colorsCount;
        colorsCount = i633 + 1;
        key_location_placeLocationBackground = i633;
        int i634 = colorsCount;
        colorsCount = i634 + 1;
        key_location_actionIcon = i634;
        int i635 = colorsCount;
        colorsCount = i635 + 1;
        key_location_actionActiveIcon = i635;
        int i636 = colorsCount;
        colorsCount = i636 + 1;
        key_location_actionBackground = i636;
        int i637 = colorsCount;
        colorsCount = i637 + 1;
        key_location_actionPressedBackground = i637;
        int i638 = colorsCount;
        colorsCount = i638 + 1;
        key_dialog_liveLocationProgress = i638;
        int i639 = colorsCount;
        colorsCount = i639 + 1;
        key_files_folderIcon = i639;
        int i640 = colorsCount;
        colorsCount = i640 + 1;
        key_files_folderIconBackground = i640;
        int i641 = colorsCount;
        colorsCount = i641 + 1;
        key_files_iconText = i641;
        int i642 = colorsCount;
        colorsCount = i642 + 1;
        key_sessions_devicesImage = i642;
        int i643 = colorsCount;
        colorsCount = i643 + 1;
        key_calls_callReceivedGreenIcon = i643;
        int i644 = colorsCount;
        colorsCount = i644 + 1;
        key_calls_callReceivedRedIcon = i644;
        int i645 = colorsCount;
        colorsCount = i645 + 1;
        key_undo_background = i645;
        int i646 = colorsCount;
        colorsCount = i646 + 1;
        key_undo_cancelColor = i646;
        int i647 = colorsCount;
        colorsCount = i647 + 1;
        key_undo_infoColor = i647;
        int i648 = colorsCount;
        colorsCount = i648 + 1;
        key_sheet_scrollUp = i648;
        int i649 = colorsCount;
        colorsCount = i649 + 1;
        key_sheet_other = i649;
        int i650 = colorsCount;
        colorsCount = i650 + 1;
        key_player_actionBarSelector = i650;
        int i651 = colorsCount;
        colorsCount = i651 + 1;
        key_player_actionBarTitle = i651;
        int i652 = colorsCount;
        colorsCount = i652 + 1;
        key_player_actionBarSubtitle = i652;
        int i653 = colorsCount;
        colorsCount = i653 + 1;
        key_player_actionBarItems = i653;
        int i654 = colorsCount;
        colorsCount = i654 + 1;
        key_player_background = i654;
        int i655 = colorsCount;
        colorsCount = i655 + 1;
        key_player_time = i655;
        int i656 = colorsCount;
        colorsCount = i656 + 1;
        key_player_progressBackground = i656;
        int i657 = colorsCount;
        colorsCount = i657 + 1;
        key_player_progressCachedBackground = i657;
        int i658 = colorsCount;
        colorsCount = i658 + 1;
        key_player_progress = i658;
        int i659 = colorsCount;
        colorsCount = i659 + 1;
        key_player_button = i659;
        int i660 = colorsCount;
        colorsCount = i660 + 1;
        key_player_buttonActive = i660;
        int i661 = colorsCount;
        colorsCount = i661 + 1;
        key_statisticChartSignature = i661;
        int i662 = colorsCount;
        colorsCount = i662 + 1;
        key_statisticChartSignatureAlpha = i662;
        int i663 = colorsCount;
        colorsCount = i663 + 1;
        key_statisticChartHintLine = i663;
        int i664 = colorsCount;
        colorsCount = i664 + 1;
        key_statisticChartActiveLine = i664;
        int i665 = colorsCount;
        colorsCount = i665 + 1;
        key_statisticChartInactivePickerChart = i665;
        int i666 = colorsCount;
        colorsCount = i666 + 1;
        key_statisticChartActivePickerChart = i666;
        int i667 = colorsCount;
        colorsCount = i667 + 1;
        key_statisticChartRipple = i667;
        int i668 = colorsCount;
        colorsCount = i668 + 1;
        key_statisticChartBackZoomColor = i668;
        int i669 = colorsCount;
        colorsCount = i669 + 1;
        key_statisticChartChevronColor = i669;
        int i670 = colorsCount;
        colorsCount = i670 + 1;
        key_statisticChartLine_blue = i670;
        int i671 = colorsCount;
        colorsCount = i671 + 1;
        key_statisticChartLine_green = i671;
        int i672 = colorsCount;
        colorsCount = i672 + 1;
        key_statisticChartLine_red = i672;
        int i673 = colorsCount;
        colorsCount = i673 + 1;
        key_statisticChartLine_golden = i673;
        int i674 = colorsCount;
        colorsCount = i674 + 1;
        key_statisticChartLine_lightblue = i674;
        int i675 = colorsCount;
        colorsCount = i675 + 1;
        key_statisticChartLine_lightgreen = i675;
        int i676 = colorsCount;
        colorsCount = i676 + 1;
        key_statisticChartLine_orange = i676;
        int i677 = colorsCount;
        colorsCount = i677 + 1;
        key_statisticChartLine_indigo = i677;
        int i678 = colorsCount;
        colorsCount = i678 + 1;
        key_statisticChartLine_purple = i678;
        int i679 = colorsCount;
        colorsCount = i679 + 1;
        key_statisticChartLine_cyan = i679;
        int i680 = colorsCount;
        colorsCount = i680 + 1;
        key_statisticChartLineEmpty = i680;
        int i681 = colorsCount;
        colorsCount = i681 + 1;
        key_color_lightblue = i681;
        int i682 = colorsCount;
        colorsCount = i682 + 1;
        key_color_blue = i682;
        int i683 = colorsCount;
        colorsCount = i683 + 1;
        key_color_green = i683;
        int i684 = colorsCount;
        colorsCount = i684 + 1;
        key_color_lightgreen = i684;
        int i685 = colorsCount;
        colorsCount = i685 + 1;
        key_color_red = i685;
        int i686 = colorsCount;
        colorsCount = i686 + 1;
        key_color_orange = i686;
        int i687 = colorsCount;
        colorsCount = i687 + 1;
        key_color_yellow = i687;
        int i688 = colorsCount;
        colorsCount = i688 + 1;
        key_color_purple = i688;
        int i689 = colorsCount;
        colorsCount = i689 + 1;
        key_color_cyan = i689;
        keys_colors = new int[]{i681, i682, i683, i684, i685, i686, i687, i688, i689};
        int i690 = colorsCount;
        colorsCount = i690 + 1;
        key_chat_outReactionButtonBackground = i690;
        int i691 = colorsCount;
        colorsCount = i691 + 1;
        key_chat_inReactionButtonBackground = i691;
        int i692 = colorsCount;
        colorsCount = i692 + 1;
        key_chat_outReactionButtonText = i692;
        int i693 = colorsCount;
        colorsCount = i693 + 1;
        key_chat_inReactionButtonText = i693;
        int i694 = colorsCount;
        colorsCount = i694 + 1;
        key_chat_inReactionButtonTextSelected = i694;
        int i695 = colorsCount;
        colorsCount = i695 + 1;
        key_chat_outReactionButtonTextSelected = i695;
        int i696 = colorsCount;
        colorsCount = i696 + 1;
        key_premiumGradient0 = i696;
        int i697 = colorsCount;
        colorsCount = i697 + 1;
        key_premiumGradient1 = i697;
        int i698 = colorsCount;
        colorsCount = i698 + 1;
        key_premiumGradient2 = i698;
        int i699 = colorsCount;
        colorsCount = i699 + 1;
        key_premiumGradient3 = i699;
        int i700 = colorsCount;
        colorsCount = i700 + 1;
        key_premiumGradient4 = i700;
        int i701 = colorsCount;
        colorsCount = i701 + 1;
        key_premiumGradientBackground1 = i701;
        int i702 = colorsCount;
        colorsCount = i702 + 1;
        key_premiumGradientBackground2 = i702;
        int i703 = colorsCount;
        colorsCount = i703 + 1;
        key_premiumGradientBackground3 = i703;
        int i704 = colorsCount;
        colorsCount = i704 + 1;
        key_premiumGradientBackground4 = i704;
        int i705 = colorsCount;
        colorsCount = i705 + 1;
        key_premiumGradientBackgroundOverlay = i705;
        int i706 = colorsCount;
        colorsCount = i706 + 1;
        key_premiumStartSmallStarsColor = i706;
        int i707 = colorsCount;
        colorsCount = i707 + 1;
        key_premiumStartGradient1 = i707;
        int i708 = colorsCount;
        colorsCount = i708 + 1;
        key_premiumStartGradient2 = i708;
        int i709 = colorsCount;
        colorsCount = i709 + 1;
        key_premiumStartSmallStarsColor2 = i709;
        int i710 = colorsCount;
        colorsCount = i710 + 1;
        key_premiumGradientBottomSheet1 = i710;
        int i711 = colorsCount;
        colorsCount = i711 + 1;
        key_premiumGradientBottomSheet2 = i711;
        int i712 = colorsCount;
        colorsCount = i712 + 1;
        key_premiumGradientBottomSheet3 = i712;
        int i713 = colorsCount;
        colorsCount = i713 + 1;
        key_topics_unreadCounter = i713;
        int i714 = colorsCount;
        colorsCount = i714 + 1;
        key_topics_unreadCounterMuted = i714;
        defaultChatDrawables = new HashMap<>();
        defaultChatDrawableColorKeys = new HashMap<>();
        defaultChatPaints = new HashMap<>();
        defaultChatPaintColors = new HashMap<>();
        fallbackKeys = new SparseIntArray();
        themeAccentExclusionKeys = new HashSet<>();
        hsvTemp1Local = new ThreadLocal<>();
        hsvTemp2Local = new ThreadLocal<>();
        hsvTemp3Local = new ThreadLocal<>();
        hsvTemp4Local = new ThreadLocal<>();
        hsvTemp5Local = new ThreadLocal<>();
        defaultColors = ThemeColors.createDefaultColors();
        fallbackKeys.put(key_chat_inAdminText, key_chat_inTimeText);
        fallbackKeys.put(key_chat_inAdminSelectedText, key_chat_inTimeSelectedText);
        fallbackKeys.put(key_player_progressCachedBackground, key_player_progressBackground);
        fallbackKeys.put(key_chat_inAudioCacheSeekbar, key_chat_inAudioSeekbar);
        fallbackKeys.put(key_chat_outAudioCacheSeekbar, key_chat_outAudioSeekbar);
        fallbackKeys.put(key_chat_emojiSearchBackground, key_chat_emojiPanelStickerPackSelector);
        fallbackKeys.put(key_location_sendLiveLocationIcon, key_location_sendLocationIcon);
        fallbackKeys.put(key_changephoneinfo_image2, key_featuredStickers_addButton);
        fallbackKeys.put(key_graySectionText, key_windowBackgroundWhiteGrayText2);
        fallbackKeys.put(key_chat_inMediaIcon, key_chat_inBubble);
        fallbackKeys.put(key_chat_outMediaIcon, key_chat_outBubble);
        fallbackKeys.put(key_chat_inMediaIconSelected, key_chat_inBubbleSelected);
        fallbackKeys.put(key_chat_outMediaIconSelected, key_chat_outBubbleSelected);
        SparseIntArray sparseIntArray = fallbackKeys;
        int i715 = key_dialog_inlineProgressBackground;
        int i716 = key_windowBackgroundGray;
        sparseIntArray.put(i715, i716);
        fallbackKeys.put(key_dialog_inlineProgress, key_chats_menuItemIcon);
        fallbackKeys.put(key_groupcreate_spanDelete, key_chats_actionIcon);
        fallbackKeys.put(key_sharedMedia_photoPlaceholder, i716);
        fallbackKeys.put(key_chat_attachPollBackground, key_chat_attachAudioBackground);
        fallbackKeys.put(key_chats_onlineCircle, key_windowBackgroundWhiteBlueText);
        SparseIntArray sparseIntArray2 = fallbackKeys;
        int i717 = key_windowBackgroundWhiteBlueButton;
        int i718 = key_windowBackgroundWhiteValueText;
        sparseIntArray2.put(i717, i718);
        fallbackKeys.put(key_windowBackgroundWhiteBlueIcon, i718);
        fallbackKeys.put(key_undo_background, key_chat_gifSaveHintBackground);
        SparseIntArray sparseIntArray3 = fallbackKeys;
        int i719 = key_undo_cancelColor;
        int i720 = key_chat_gifSaveHintText;
        sparseIntArray3.put(i719, i720);
        fallbackKeys.put(key_undo_infoColor, i720);
        SparseIntArray sparseIntArray4 = fallbackKeys;
        int i721 = key_windowBackgroundUnchecked;
        int i722 = key_windowBackgroundWhite;
        sparseIntArray4.put(i721, i722);
        fallbackKeys.put(key_windowBackgroundChecked, i722);
        fallbackKeys.put(key_switchTrackBlue, key_switchTrack);
        fallbackKeys.put(key_switchTrackBlueChecked, key_switchTrackChecked);
        fallbackKeys.put(key_switchTrackBlueThumb, i722);
        fallbackKeys.put(key_switchTrackBlueThumbChecked, i722);
        fallbackKeys.put(key_windowBackgroundCheckText, i722);
        fallbackKeys.put(key_contextProgressInner4, key_contextProgressInner1);
        fallbackKeys.put(key_contextProgressOuter4, key_contextProgressOuter1);
        SparseIntArray sparseIntArray5 = fallbackKeys;
        int i723 = key_switchTrackBlueSelector;
        int i724 = key_listSelector;
        sparseIntArray5.put(i723, i724);
        fallbackKeys.put(key_switchTrackBlueSelectorChecked, i724);
        SparseIntArray sparseIntArray6 = fallbackKeys;
        int i725 = key_chat_emojiBottomPanelIcon;
        int i726 = key_chat_emojiPanelIcon;
        sparseIntArray6.put(i725, i726);
        fallbackKeys.put(key_chat_emojiSearchIcon, i726);
        fallbackKeys.put(key_chat_emojiPanelStickerSetNameHighlight, key_windowBackgroundWhiteBlueText4);
        fallbackKeys.put(key_chat_emojiPanelStickerPackSelectorLine, key_chat_emojiPanelIconSelected);
        SparseIntArray sparseIntArray7 = fallbackKeys;
        int i727 = key_sheet_scrollUp;
        int i728 = key_chat_emojiPanelStickerPackSelector;
        sparseIntArray7.put(i727, i728);
        fallbackKeys.put(key_sheet_other, key_player_actionBarItems);
        fallbackKeys.put(key_dialogSearchBackground, i728);
        fallbackKeys.put(key_dialogSearchHint, i726);
        fallbackKeys.put(key_dialogSearchIcon, i726);
        fallbackKeys.put(key_dialogSearchText, key_windowBackgroundWhiteBlackText);
        SparseIntArray sparseIntArray8 = fallbackKeys;
        int i729 = key_dialogFloatingButton;
        int i730 = key_dialogRoundCheckBox;
        sparseIntArray8.put(i729, i730);
        fallbackKeys.put(key_dialogFloatingButtonPressed, i730);
        fallbackKeys.put(key_dialogFloatingIcon, key_dialogRoundCheckBoxCheck);
        fallbackKeys.put(key_dialogShadowLine, key_chat_emojiPanelShadowLine);
        fallbackKeys.put(key_actionBarDefaultArchived, key_actionBarDefault);
        SparseIntArray sparseIntArray9 = fallbackKeys;
        int i731 = key_actionBarDefaultArchivedSelector;
        int i732 = key_actionBarDefaultSelector;
        sparseIntArray9.put(i731, i732);
        fallbackKeys.put(key_actionBarDefaultArchivedIcon, key_actionBarDefaultIcon);
        SparseIntArray sparseIntArray10 = fallbackKeys;
        int i733 = key_actionBarDefaultArchivedTitle;
        int i734 = key_actionBarDefaultTitle;
        sparseIntArray10.put(i733, i734);
        fallbackKeys.put(key_actionBarDefaultArchivedSearch, key_actionBarDefaultSearch);
        fallbackKeys.put(key_actionBarDefaultArchivedSearchPlaceholder, key_actionBarDefaultSearchPlaceholder);
        SparseIntArray sparseIntArray11 = fallbackKeys;
        int i735 = key_chats_message_threeLines;
        int i736 = key_chats_message;
        sparseIntArray11.put(i735, i736);
        SparseIntArray sparseIntArray12 = fallbackKeys;
        int i737 = key_chats_nameMessage_threeLines;
        int i738 = key_chats_nameMessage;
        sparseIntArray12.put(i737, i738);
        fallbackKeys.put(key_chats_nameArchived, key_chats_name);
        fallbackKeys.put(key_chats_nameMessageArchived, i738);
        fallbackKeys.put(key_chats_nameMessageArchived_threeLines, i738);
        fallbackKeys.put(key_chats_messageArchived, i736);
        SparseIntArray sparseIntArray13 = fallbackKeys;
        int i739 = key_avatar_backgroundArchived;
        int i740 = key_chats_unreadCounterMuted;
        sparseIntArray13.put(i739, i740);
        SparseIntArray sparseIntArray14 = fallbackKeys;
        int i741 = key_chats_archiveBackground;
        int i742 = key_chats_actionBackground;
        sparseIntArray14.put(i741, i742);
        fallbackKeys.put(key_chats_archivePinBackground, i740);
        SparseIntArray sparseIntArray15 = fallbackKeys;
        int i743 = key_chats_archiveIcon;
        int i744 = key_chats_actionIcon;
        sparseIntArray15.put(i743, i744);
        fallbackKeys.put(key_chats_archiveText, i744);
        fallbackKeys.put(key_actionBarDefaultSubmenuItemIcon, key_dialogIcon);
        fallbackKeys.put(key_checkboxDisabled, i740);
        SparseIntArray sparseIntArray16 = fallbackKeys;
        int i745 = key_chat_status;
        int i746 = key_actionBarDefaultSubtitle;
        sparseIntArray16.put(i745, i746);
        SparseIntArray sparseIntArray17 = fallbackKeys;
        int i747 = key_chat_inGreenCall;
        int i748 = key_calls_callReceivedGreenIcon;
        sparseIntArray17.put(i747, i748);
        fallbackKeys.put(key_chat_outGreenCall, i748);
        fallbackKeys.put(key_actionBarTabActiveText, i734);
        fallbackKeys.put(key_actionBarTabUnactiveText, i746);
        fallbackKeys.put(key_actionBarTabLine, i734);
        fallbackKeys.put(key_actionBarTabSelector, i732);
        fallbackKeys.put(key_profile_status, key_avatar_subtitleInProfileBlue);
        fallbackKeys.put(key_chats_menuTopBackgroundCats, key_avatar_backgroundActionBarBlue);
        fallbackKeys.put(key_chat_outLinkSelectBackground, key_chat_linkSelectBackground);
        fallbackKeys.put(key_actionBarDefaultSubmenuSeparator, key_windowBackgroundGray);
        SparseIntArray sparseIntArray18 = fallbackKeys;
        int i749 = key_chat_attachPermissionImage;
        int i750 = key_dialogTextBlack;
        sparseIntArray18.put(i749, i750);
        fallbackKeys.put(key_chat_attachPermissionMark, key_chat_sentError);
        fallbackKeys.put(key_chat_attachPermissionText, i750);
        fallbackKeys.put(key_chat_attachEmptyImage, key_emptyListPlaceholder);
        fallbackKeys.put(key_actionBarBrowser, key_actionBarDefault);
        fallbackKeys.put(key_chats_sentReadCheck, key_chats_sentCheck);
        fallbackKeys.put(key_chat_outSentCheckRead, key_chat_outSentCheck);
        fallbackKeys.put(key_chat_outSentCheckReadSelected, key_chat_outSentCheckSelected);
        fallbackKeys.put(key_chats_archivePullDownBackground, i740);
        fallbackKeys.put(key_chats_archivePullDownBackgroundActive, i742);
        fallbackKeys.put(key_avatar_backgroundArchivedHidden, key_avatar_backgroundSaved);
        fallbackKeys.put(key_featuredStickers_removeButtonText, key_featuredStickers_addButtonPressed);
        SparseIntArray sparseIntArray19 = fallbackKeys;
        int i751 = key_dialogEmptyImage;
        int i752 = key_player_time;
        sparseIntArray19.put(i751, i752);
        fallbackKeys.put(key_dialogEmptyText, i752);
        fallbackKeys.put(key_location_actionIcon, i750);
        SparseIntArray sparseIntArray20 = fallbackKeys;
        int i753 = key_location_actionActiveIcon;
        int i754 = key_windowBackgroundWhiteBlueText7;
        sparseIntArray20.put(i753, i754);
        fallbackKeys.put(key_location_actionBackground, key_dialogBackground);
        fallbackKeys.put(key_location_actionPressedBackground, key_dialogBackgroundGray);
        fallbackKeys.put(key_location_sendLocationText, i754);
        fallbackKeys.put(key_location_sendLiveLocationText, key_windowBackgroundWhiteGreenText);
        SparseIntArray sparseIntArray21 = fallbackKeys;
        int i755 = key_chat_outTextSelectionHighlight;
        int i756 = key_chat_textSelectBackground;
        sparseIntArray21.put(i755, i756);
        fallbackKeys.put(key_chat_inTextSelectionHighlight, i756);
        SparseIntArray sparseIntArray22 = fallbackKeys;
        int i757 = key_chat_TextSelectionCursor;
        sparseIntArray22.put(i757, key_chat_messagePanelCursor);
        fallbackKeys.put(key_chat_outTextSelectionCursor, i757);
        SparseIntArray sparseIntArray23 = fallbackKeys;
        int i758 = key_chat_inPollCorrectAnswer;
        int i759 = key_chat_attachLocationBackground;
        sparseIntArray23.put(i758, i759);
        fallbackKeys.put(key_chat_outPollCorrectAnswer, i759);
        SparseIntArray sparseIntArray24 = fallbackKeys;
        int i760 = key_chat_inPollWrongAnswer;
        int i761 = key_chat_attachAudioBackground;
        sparseIntArray24.put(i760, i761);
        fallbackKeys.put(key_chat_outPollWrongAnswer, i761);
        fallbackKeys.put(key_profile_tabText, key_windowBackgroundWhiteGrayText);
        SparseIntArray sparseIntArray25 = fallbackKeys;
        int i762 = key_profile_tabSelectedText;
        int i763 = key_windowBackgroundWhiteBlueHeader;
        sparseIntArray25.put(i762, i763);
        fallbackKeys.put(key_profile_tabSelectedLine, i763);
        fallbackKeys.put(key_profile_tabSelector, key_listSelector);
        fallbackKeys.put(key_chat_attachGalleryText, key_chat_attachGalleryBackground);
        fallbackKeys.put(key_chat_attachAudioText, i761);
        fallbackKeys.put(key_chat_attachFileText, key_chat_attachFileBackground);
        fallbackKeys.put(key_chat_attachContactText, key_chat_attachContactBackground);
        fallbackKeys.put(key_chat_attachLocationText, i759);
        fallbackKeys.put(key_chat_attachPollText, key_chat_attachPollBackground);
        SparseIntArray sparseIntArray26 = fallbackKeys;
        int i764 = key_chat_inPsaNameText;
        int i765 = key_avatar_nameInMessageGreen;
        sparseIntArray26.put(i764, i765);
        fallbackKeys.put(key_chat_outPsaNameText, i765);
        fallbackKeys.put(key_chat_outAdminText, key_chat_outTimeText);
        fallbackKeys.put(key_chat_outAdminSelectedText, key_chat_outTimeSelectedText);
        SparseIntArray sparseIntArray27 = fallbackKeys;
        int i766 = key_returnToCallMutedBackground;
        int i767 = key_windowBackgroundWhite;
        sparseIntArray27.put(i766, i767);
        SparseIntArray sparseIntArray28 = fallbackKeys;
        int i768 = key_dialogSwipeRemove;
        int i769 = key_avatar_backgroundRed;
        sparseIntArray28.put(i768, i769);
        fallbackKeys.put(key_chat_inReactionButtonBackground, key_chat_inLoader);
        fallbackKeys.put(key_chat_outReactionButtonBackground, key_chat_outLoader);
        fallbackKeys.put(key_chat_inReactionButtonText, key_chat_inPreviewInstantText);
        fallbackKeys.put(key_chat_outReactionButtonText, key_chat_outPreviewInstantText);
        fallbackKeys.put(key_chat_inReactionButtonTextSelected, i767);
        fallbackKeys.put(key_chat_outReactionButtonTextSelected, i767);
        fallbackKeys.put(key_dialogReactionMentionBackground, key_voipgroup_mutedByAdminGradient2);
        fallbackKeys.put(key_topics_unreadCounter, key_chats_unreadCounter);
        fallbackKeys.put(key_topics_unreadCounterMuted, key_chats_message);
        fallbackKeys.put(key_avatar_background2Saved, key_avatar_backgroundSaved);
        fallbackKeys.put(key_avatar_background2Red, i769);
        fallbackKeys.put(key_avatar_background2Orange, key_avatar_backgroundOrange);
        fallbackKeys.put(key_avatar_background2Violet, key_avatar_backgroundViolet);
        fallbackKeys.put(key_avatar_background2Green, key_avatar_backgroundGreen);
        fallbackKeys.put(key_avatar_background2Cyan, key_avatar_backgroundCyan);
        fallbackKeys.put(key_avatar_background2Blue, key_avatar_backgroundBlue);
        fallbackKeys.put(key_avatar_background2Pink, key_avatar_backgroundPink);
        fallbackKeys.put(key_statisticChartLine_orange, key_color_orange);
        fallbackKeys.put(key_statisticChartLine_blue, key_color_blue);
        fallbackKeys.put(key_statisticChartLine_red, key_color_red);
        fallbackKeys.put(key_statisticChartLine_lightblue, key_color_lightblue);
        fallbackKeys.put(key_statisticChartLine_golden, key_color_yellow);
        SparseIntArray sparseIntArray29 = fallbackKeys;
        int i770 = key_statisticChartLine_purple;
        int i771 = key_color_purple;
        sparseIntArray29.put(i770, i771);
        fallbackKeys.put(key_statisticChartLine_indigo, i771);
        fallbackKeys.put(key_statisticChartLine_cyan, key_color_cyan);
        int i772 = 0;
        while (true) {
            int[] iArr = keys_avatar_background;
            if (i772 >= iArr.length) {
                break;
            }
            themeAccentExclusionKeys.add(Integer.valueOf(iArr[i772]));
            i772++;
        }
        int i773 = 0;
        while (true) {
            int[] iArr2 = keys_avatar_background2;
            if (i773 >= iArr2.length) {
                break;
            }
            themeAccentExclusionKeys.add(Integer.valueOf(iArr2[i773]));
            i773++;
        }
        int i774 = 0;
        while (true) {
            int[] iArr3 = keys_avatar_nameInMessage;
            if (i774 >= iArr3.length) {
                break;
            }
            themeAccentExclusionKeys.add(Integer.valueOf(iArr3[i774]));
            i774++;
        }
        int i775 = 0;
        while (true) {
            int[] iArr4 = keys_colors;
            if (i775 >= iArr4.length) {
                break;
            }
            themeAccentExclusionKeys.add(Integer.valueOf(iArr4[i775]));
            i775++;
        }
        themeAccentExclusionKeys.add(Integer.valueOf(key_chat_attachFileBackground));
        themeAccentExclusionKeys.add(Integer.valueOf(key_chat_attachGalleryBackground));
        themeAccentExclusionKeys.add(Integer.valueOf(key_chat_attachFileText));
        themeAccentExclusionKeys.add(Integer.valueOf(key_chat_attachGalleryText));
        themeAccentExclusionKeys.add(Integer.valueOf(key_statisticChartLine_blue));
        themeAccentExclusionKeys.add(Integer.valueOf(key_statisticChartLine_green));
        themeAccentExclusionKeys.add(Integer.valueOf(key_statisticChartLine_red));
        themeAccentExclusionKeys.add(Integer.valueOf(key_statisticChartLine_golden));
        themeAccentExclusionKeys.add(Integer.valueOf(key_statisticChartLine_lightblue));
        themeAccentExclusionKeys.add(Integer.valueOf(key_statisticChartLine_lightgreen));
        themeAccentExclusionKeys.add(Integer.valueOf(key_statisticChartLine_orange));
        themeAccentExclusionKeys.add(Integer.valueOf(key_statisticChartLine_indigo));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_checkMenu));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_muteButton));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_muteButton2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_muteButton3));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_searchText));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_searchPlaceholder));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_searchBackground));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_leaveCallMenu));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_scrollUp));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_soundButton));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_soundButtonActive));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_soundButtonActiveScrolled));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_soundButton2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_soundButtonActive2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_soundButtonActive2Scrolled));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_leaveButton));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_leaveButtonScrolled));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_connectingProgress));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_disabledButton));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_disabledButtonActive));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_disabledButtonActiveScrolled));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_unmuteButton));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_unmuteButton2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_actionBarUnscrolled));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_listViewBackgroundUnscrolled));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_lastSeenTextUnscrolled));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_mutedIconUnscrolled));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_actionBar));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_actionBarItems));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_actionBarItemsSelector));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_mutedByAdminIcon));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_mutedIcon));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_lastSeenText));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_nameText));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_listViewBackground));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_listeningText));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_speakingText));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_listSelector));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_inviteMembersBackground));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_dialogBackground));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_overlayGreen1));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_overlayGreen2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_overlayBlue1));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_overlayBlue2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_topPanelGreen1));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_topPanelGreen2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_topPanelBlue1));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_topPanelBlue2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_topPanelGray));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_overlayAlertGradientMuted));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_overlayAlertGradientMuted2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_overlayAlertGradientUnmuted));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_overlayAlertGradientUnmuted2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_overlayAlertMutedByAdmin));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_overlayAlertMutedByAdmin2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_mutedByAdminGradient));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_mutedByAdminGradient2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_mutedByAdminGradient3));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_mutedByAdminMuteButton));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_mutedByAdminMuteButtonDisabled));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_windowBackgroundWhiteInputField));
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_windowBackgroundWhiteInputFieldActivated));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumGradient0));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumGradient1));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumGradient2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumGradient3));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumGradient4));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumGradientBackground1));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumGradientBackground2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumGradientBackground3));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumGradientBackground4));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumStartSmallStarsColor));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumStartGradient1));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumStartGradient2));
        themes = new ArrayList<>();
        otherThemes = new ArrayList<>();
        themesDict = new HashMap<>();
        currentColorsNoAccent = new SparseIntArray();
        currentColors = new SparseIntArray();
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
        ThemeInfo themeInfo6 = new ThemeInfo();
        themeInfo6.name = "Blue";
        themeInfo6.assetName = "bluebubbles.attheme";
        themeInfo6.previewBackgroundColor = -6963476;
        themeInfo6.previewInColor = -1;
        themeInfo6.previewOutColor = -3086593;
        themeInfo6.firstAccentIsDefault = true;
        themeInfo6.currentAccentId = DEFALT_THEME_ACCENT_ID;
        themeInfo6.sortIndex = 1;
        themeInfo6.setAccentColorOptions(new int[]{-10972987, -14444461, -3252606, -8428605, -14380627, -14050257, -7842636, -13464881, -12342073, -11359164, -3317869, -2981834, -8165684, -3256745, -2904512, -8681301}, new int[]{-4660851, -328756, -1572, -4108434, -3031781, -1335, -198952, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, -853047, -264993, 0, 0, -135756, -198730, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, -2104672, -937328, -2637335, -2639714, -1270157, -3428124, -6570777, -7223828, -6567550, -1793599, -1855875, -4674838, -1336199, -2900876, -6247730}, new int[]{0, -4532067, -1257580, -1524266, -1646910, -1519483, -1324823, -4138509, -4202516, -2040429, -1458474, -1256030, -3814930, -1000039, -1450082, -3485987}, new int[]{0, -1909081, -1592444, -2969879, -2439762, -1137033, -2119471, -6962197, -4857383, -4270699, -3364639, -2117514, -5000734, -1598028, -2045813, -5853742}, new int[]{0, -6371440, -1319256, -1258616, -1712961, -1186647, -1193816, -4467224, -4203544, -3023977, -1061929, -1255788, -2113811, -806526, -1715305, -3485976}, new int[]{99, 9, 10, 11, 12, 13, 14, 0, 1, 2, 3, 4, 5, 6, 7, 8}, new String[]{"", "p-pXcflrmFIBAAAAvXYQk-mCwZU", "JqSUrO0-mFIBAAAAWwTvLzoWGQI", "O-wmAfBPSFADAAAA4zINVfD_bro", "RepJ5uE_SVABAAAAr4d0YhgB850", "-Xc-np9y2VMCAAAARKr0yNNPYW0", "fqv01SQemVIBAAAApND8LDRUhRU", "fqv01SQemVIBAAAApND8LDRUhRU", "RepJ5uE_SVABAAAAr4d0YhgB850", "lp0prF8ISFAEAAAA_p385_CvG0w", "heptcj-hSVACAAAAC9RrMzOa-cs", "PllZ-bf_SFAEAAAA8crRfwZiDNg", "dhf9pceaQVACAAAAbzdVo4SCiZA", "Ujx2TFcJSVACAAAARJ4vLa50MkM", "p-pXcflrmFIBAAAAvXYQk-mCwZU", "dk_wwlghOFACAAAAfz9xrxi6euw"}, new int[]{0, 180, 45, 0, 45, 180, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 52, 46, 57, 45, 64, 52, 35, 36, 41, 50, 50, 35, 38, 37, 30});
        sortAccents(themeInfo6);
        ArrayList<ThemeInfo> arrayList = themes;
        defaultTheme = themeInfo6;
        currentTheme = themeInfo6;
        currentDayTheme = themeInfo6;
        arrayList.add(themeInfo6);
        themesDict.put("Blue", themeInfo6);
        ThemeInfo themeInfo7 = new ThemeInfo();
        themeInfo7.name = "Dark Blue";
        themeInfo7.assetName = "darkblue.attheme";
        themeInfo7.previewBackgroundColor = -10523006;
        themeInfo7.previewInColor = -9009508;
        themeInfo7.previewOutColor = -8214301;
        themeInfo7.sortIndex = 3;
        themeInfo7.setAccentColorOptions(new int[]{-7177260, -9860357, -14440464, -8687151, -9848491, -14053142, -9403671, -10044691, -13203974, -12138259, -10179489, -1344335, -1142742, -6127120, -2931932, -1131212, -8417365, -13270557}, new int[]{-6464359, -10267323, -13532789, -5413850, -11898828, -13410942, -13215889, -10914461, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-10465880, -9937588, -14983040, -6736562, -14197445, -13534568, -13144441, -10587280, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-14213586, -15263198, -16310753, -15724781, -15853551, -16051428, -14868183, -14668758, -15854566, -15326427, -15327979, -14411490, -14345453, -14738135, -14543346, -14212843, -15263205, -15854566}, new int[]{-15659501, -14277074, -15459034, -14542297, -14735336, -15129808, -15591910, -15459810, -15260623, -15853800, -15259879, -14477540, -14674936, -15461604, -13820650, -15067635, -14605528, -15260623}, new int[]{-13951445, -15395557, -15985382, -15855853, -16050417, -15525854, -15260627, -15327189, -15788258, -14799314, -15458796, -13952727, -13754603, -14081231, -14478324, -14081004, -15197667, -15788258}, new int[]{-15330777, -15066858, -15915220, -14213847, -15262439, -15260879, -15657695, -16443625, -15459285, -15589601, -14932454, -14740451, -15002870, -15264997, -13821660, -14805234, -14605784, -15459285}, new int[]{11, 12, 13, 14, 15, 16, 17, 18, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, new String[]{"O-wmAfBPSFADAAAA4zINVfD_bro", "RepJ5uE_SVABAAAAr4d0YhgB850", "dk_wwlghOFACAAAAfz9xrxi6euw", "9LW_RcoOSVACAAAAFTk3DTyXN-M", "PllZ-bf_SFAEAAAA8crRfwZiDNg", "-Xc-np9y2VMCAAAARKr0yNNPYW0", "kO4jyq55SFABAAAA0WEpcLfahXk", "CJNyxPMgSVAEAAAAvW9sMwc51cw", "fqv01SQemVIBAAAApND8LDRUhRU", "RepJ5uE_SVABAAAAr4d0YhgB850", "CJNyxPMgSVAEAAAAvW9sMwc51cw", "9LW_RcoOSVACAAAAFTk3DTyXN-M", "9GcNVISdSVADAAAAUcw5BYjELW4", "F5oWoCs7QFACAAAAgf2bD_mg8Bw", "9ShF73d1MFIIAAAAjWnm8_ZMe8Q", "3rX-PaKbSFACAAAAEiHNvcEm6X4", "dk_wwlghOFACAAAAfz9xrxi6euw", "fqv01SQemVIBAAAApND8LDRUhRU"}, new int[]{225, 45, 225, 135, 45, 225, 45, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{40, 40, 31, 50, 25, 34, 35, 35, 38, 29, 24, 34, 34, 31, 29, 37, 21, 38});
        sortAccents(themeInfo7);
        themes.add(themeInfo7);
        HashMap<String, ThemeInfo> hashMap = themesDict;
        currentNightTheme = themeInfo7;
        hashMap.put("Dark Blue", themeInfo7);
        ThemeInfo themeInfo8 = new ThemeInfo();
        themeInfo8.name = "Arctic Blue";
        themeInfo8.assetName = "arctic.attheme";
        themeInfo8.previewBackgroundColor = -1971728;
        themeInfo8.previewInColor = -1;
        themeInfo8.previewOutColor = -9657877;
        themeInfo8.sortIndex = 5;
        themeInfo8.setAccentColorOptions(new int[]{-12537374, -12472227, -3240928, -11033621, -2194124, -3382903, -13332245, -12342073, -11359164, -3317869, -2981834, -8165684, -3256745, -2904512, -8681301}, new int[]{-13525046, -14113959, -7579073, -13597229, -3581840, -8883763, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-11616542, -9716647, -6400452, -12008744, -2592697, -4297041, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-3808528, -2433367, -2700891, -1838093, -1120848, -1712148, -2037779, -4202261, -4005713, -1058332, -925763, -1975316, -1189672, -1318451, -2302235}, new int[]{-1510157, -4398164, -1647697, -3610898, -1130838, -1980692, -4270093, -4202261, -3415654, -1259815, -1521765, -4341268, -1127744, -1318219, -3945761}, new int[]{-4924688, -3283031, -1523567, -2494477, -1126510, -595210, -2037517, -3478548, -4661623, -927514, -796762, -2696971, -1188403, -1319735, -1577487}, new int[]{-3149585, -5714021, -1978209, -4925720, -1134713, -1718833, -3613709, -5317397, -3218014, -999207, -2116466, -4343054, -931397, -1583186, -3815718}, new int[]{9, 10, 11, 12, 13, 14, 0, 1, 2, 3, 4, 5, 6, 7, 8}, new String[]{"MIo6r0qGSFAFAAAAtL8TsDzNX60", "dhf9pceaQVACAAAAbzdVo4SCiZA", "fqv01SQemVIBAAAApND8LDRUhRU", "p-pXcflrmFIBAAAAvXYQk-mCwZU", "JqSUrO0-mFIBAAAAWwTvLzoWGQI", "F5oWoCs7QFACAAAAgf2bD_mg8Bw", "fqv01SQemVIBAAAApND8LDRUhRU", "RepJ5uE_SVABAAAAr4d0YhgB850", "PllZ-bf_SFAEAAAA8crRfwZiDNg", "pgJfpFNRSFABAAAACDT8s5sEjfc", "ptuUd96JSFACAAAATobI23sPpz0", "dhf9pceaQVACAAAAbzdVo4SCiZA", "JqSUrO0-mFIBAAAAWwTvLzoWGQI", "9iklpvIPQVABAAAAORQXKur_Eyc", "F5oWoCs7QFACAAAAgf2bD_mg8Bw"}, new int[]{315, 315, 225, 315, 0, 180, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{50, 50, 58, 47, 46, 50, 49, 46, 51, 50, 49, 34, 54, 50, 40});
        sortAccents(themeInfo8);
        themes.add(themeInfo8);
        themesDict.put("Arctic Blue", themeInfo8);
        ThemeInfo themeInfo9 = new ThemeInfo();
        themeInfo9.name = "Day";
        themeInfo9.assetName = "day.attheme";
        themeInfo9.previewBackgroundColor = -1;
        themeInfo9.previewInColor = -1315084;
        themeInfo9.previewOutColor = -8604930;
        themeInfo9.sortIndex = 2;
        themeInfo9.setAccentColorOptions(new int[]{-11099447, -3379581, -3109305, -3382174, -7963438, -11759137, -11029287, -11226775, -2506945, -3382174, -3379581, -6587438, -2649788, -8681301}, new int[]{-10125092, -9671214, -3451775, -3978678, -10711329, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-12664362, -3642988, -2383569, -3109317, -11422261, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, null, null, new int[]{9, 10, 11, 12, 13, 0, 1, 2, 3, 4, 5, 6, 7, 8}, new String[]{"", "", "", "", "", "", "", "", "", "", "", "", "", ""}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        sortAccents(themeInfo9);
        themes.add(themeInfo9);
        themesDict.put("Day", themeInfo9);
        ThemeInfo themeInfo10 = new ThemeInfo();
        themeInfo10.name = "Night";
        themeInfo10.assetName = "night.attheme";
        themeInfo10.previewBackgroundColor = -11315623;
        themeInfo10.previewInColor = -9143676;
        themeInfo10.previewOutColor = -9067802;
        themeInfo10.sortIndex = 4;
        themeInfo10.setAccentColorOptions(new int[]{-9781697, -7505693, -2204034, -10913816, -2375398, -12678921, -11881005, -11880383, -2534026, -1934037, -7115558, -3128522, -1528292, -8812381}, new int[]{-7712108, -4953061, -5288081, -14258547, -9154889, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-9939525, -5948598, -10335844, -13659747, -14054507, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-15330532, -14806760, -15791344, -16184308, -16313063, -15921641, -15656164, -15986420, -15856883, -14871025, -16185078, -14937584, -14869736, -15855598}, new int[]{-14673881, -15724781, -15002342, -15458526, -15987697, -16184820, -16118258, -16250616, -15067624, -15527923, -14804447, -15790836, -15987960, -16316665}, new int[]{-15856877, -14608861, -15528430, -15921391, -15722209, -15197144, -15458015, -15591406, -15528431, -15068401, -16053749, -15594229, -15395825, -15724012}, new int[]{-14804694, -15658986, -14609382, -15656421, -16118509, -15855854, -16315381, -16052981, -14544354, -15791092, -15659241, -16316922, -15988214, -16185077}, new int[]{9, 10, 11, 12, 13, 0, 1, 2, 3, 4, 5, 6, 7, 8}, new String[]{"YIxYGEALQVADAAAAA3QbEH0AowY", "9LW_RcoOSVACAAAAFTk3DTyXN-M", "O-wmAfBPSFADAAAA4zINVfD_bro", "F5oWoCs7QFACAAAAgf2bD_mg8Bw", "-Xc-np9y2VMCAAAARKr0yNNPYW0", "fqv01SQemVIBAAAApND8LDRUhRU", "F5oWoCs7QFACAAAAgf2bD_mg8Bw", "ptuUd96JSFACAAAATobI23sPpz0", "p-pXcflrmFIBAAAAvXYQk-mCwZU", "Nl8Pg2rBQVACAAAA25Lxtb8SDp0", "dhf9pceaQVACAAAAbzdVo4SCiZA", "9GcNVISdSVADAAAAUcw5BYjELW4", "9LW_RcoOSVACAAAAFTk3DTyXN-M", "dk_wwlghOFACAAAAfz9xrxi6euw"}, new int[]{45, 135, 0, 180, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{34, 47, 52, 48, 54, 50, 37, 56, 48, 49, 40, 64, 38, 48});
        sortAccents(themeInfo10);
        themes.add(themeInfo10);
        themesDict.put("Night", themeInfo10);
        String str4 = null;
        String string = sharedPreferences.getString("themes2", null);
        if (sharedPreferences.getInt("remote_version", 0) == 1) {
            int i776 = 0;
            while (i776 < 4) {
                long[] jArr = remoteThemesHash;
                StringBuilder sb = new StringBuilder();
                sb.append("2remoteThemesHash");
                sb.append(i776 != 0 ? Integer.valueOf(i776) : "");
                jArr[i776] = sharedPreferences.getLong(sb.toString(), 0L);
                int[] iArr5 = lastLoadingThemesTime;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("lastLoadingThemesTime");
                sb2.append(i776 != 0 ? Integer.valueOf(i776) : "");
                iArr5[i776] = sharedPreferences.getInt(sb2.toString(), 0);
                i776++;
            }
        }
        sharedPreferences.edit().putInt("remote_version", 1).apply();
        if (!TextUtils.isEmpty(string)) {
            try {
                JSONArray jSONArray = new JSONArray(string);
                for (int i777 = 0; i777 < jSONArray.length(); i777++) {
                    ThemeInfo createWithJson = ThemeInfo.createWithJson(jSONArray.getJSONObject(i777));
                    if (createWithJson != null) {
                        otherThemes.add(createWithJson);
                        themes.add(createWithJson);
                        themesDict.put(createWithJson.getKey(), createWithJson);
                        createWithJson.loadWallpapers(sharedPreferences);
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else {
            String string2 = sharedPreferences.getString("themes", null);
            if (!TextUtils.isEmpty(string2)) {
                for (String str5 : string2.split("&")) {
                    ThemeInfo createWithString = ThemeInfo.createWithString(str5);
                    if (createWithString != null) {
                        otherThemes.add(createWithString);
                        themes.add(createWithString);
                        themesDict.put(createWithString.getKey(), createWithString);
                    }
                }
                saveOtherThemes(true, true);
                sharedPreferences.edit().remove("themes").commit();
            }
        }
        sortThemes();
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        try {
            ThemeInfo themeInfo11 = themesDict.get("Dark Blue");
            String string3 = globalMainSettings.getString("theme", null);
            if ("Default".equals(string3)) {
                themeInfo = themesDict.get("Blue");
                themeInfo.currentAccentId = DEFALT_THEME_ACCENT_ID;
            } else if ("Dark".equals(string3)) {
                themeInfo11.currentAccentId = 9;
                themeInfo = themeInfo11;
            } else if (string3 != null) {
                themeInfo = themesDict.get(string3);
                if (themeInfo != null && !sharedPreferences.contains("lastDayTheme")) {
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("lastDayTheme", themeInfo.getKey());
                    edit.commit();
                }
            } else {
                themeInfo = null;
            }
            String string4 = globalMainSettings.getString("nighttheme", null);
            if ("Default".equals(string4)) {
                themeInfo = themesDict.get("Blue");
                themeInfo.currentAccentId = DEFALT_THEME_ACCENT_ID;
            } else if ("Dark".equals(string4)) {
                currentNightTheme = themeInfo11;
                themeInfo11.currentAccentId = 9;
            } else if (string4 != null && (themeInfo2 = themesDict.get(string4)) != null) {
                currentNightTheme = themeInfo2;
            }
            if (currentNightTheme != null && !sharedPreferences.contains("lastDarkTheme")) {
                SharedPreferences.Editor edit2 = sharedPreferences.edit();
                edit2.putString("lastDarkTheme", currentNightTheme.getKey());
                edit2.commit();
            }
            SharedPreferences.Editor editor3 = null;
            SharedPreferences.Editor editor4 = null;
            for (ThemeInfo themeInfo12 : themesDict.values()) {
                if (themeInfo12.assetName == null || themeInfo12.accentBaseColor == 0) {
                    str = str3;
                    themeInfo4 = themeInfo;
                    editor4 = editor4;
                } else {
                    String string5 = sharedPreferences.getString("accents_" + themeInfo12.assetName, str4);
                    themeInfo12.currentAccentId = sharedPreferences.getInt("accent_current_" + themeInfo12.assetName, themeInfo12.firstAccentIsDefault ? DEFALT_THEME_ACCENT_ID : 0);
                    ArrayList arrayList2 = new ArrayList();
                    if (!TextUtils.isEmpty(string5)) {
                        SerializedData serializedData = new SerializedData(Base64.decode(string5, 3));
                        boolean z3 = true;
                        int readInt32 = serializedData.readInt32(true);
                        int readInt322 = serializedData.readInt32(true);
                        int i778 = 0;
                        while (i778 < readInt322) {
                            ThemeAccent themeAccent = new ThemeAccent();
                            themeAccent.id = serializedData.readInt32(z3);
                            themeAccent.accentColor = serializedData.readInt32(z3);
                            if (readInt32 >= 9) {
                                themeAccent.accentColor2 = serializedData.readInt32(z3);
                            }
                            themeAccent.parentTheme = themeInfo12;
                            themeAccent.myMessagesAccentColor = serializedData.readInt32(true);
                            themeAccent.myMessagesGradientAccentColor1 = serializedData.readInt32(true);
                            if (readInt32 >= 7) {
                                themeAccent.myMessagesGradientAccentColor2 = serializedData.readInt32(true);
                                themeAccent.myMessagesGradientAccentColor3 = serializedData.readInt32(true);
                            }
                            if (readInt32 >= 8) {
                                z2 = true;
                                themeAccent.myMessagesAnimated = serializedData.readBool(true);
                            } else {
                                z2 = true;
                            }
                            if (readInt32 >= 3) {
                                themeInfo5 = themeInfo;
                                editor2 = editor4;
                                themeAccent.backgroundOverrideColor = serializedData.readInt64(z2);
                            } else {
                                themeInfo5 = themeInfo;
                                editor2 = editor4;
                                themeAccent.backgroundOverrideColor = serializedData.readInt32(z2);
                            }
                            if (readInt32 >= 2) {
                                themeAccent.backgroundGradientOverrideColor1 = serializedData.readInt64(z2);
                            } else {
                                themeAccent.backgroundGradientOverrideColor1 = serializedData.readInt32(z2);
                            }
                            ?? r4 = z2;
                            if (readInt32 >= 6) {
                                themeAccent.backgroundGradientOverrideColor2 = serializedData.readInt64(z2);
                                themeAccent.backgroundGradientOverrideColor3 = serializedData.readInt64(z2);
                                r4 = 1;
                            }
                            if (readInt32 >= r4) {
                                themeAccent.backgroundRotation = serializedData.readInt32(r4);
                            }
                            if (readInt32 >= 4) {
                                serializedData.readInt64(r4);
                                themeAccent.patternIntensity = (float) serializedData.readDouble(r4);
                                themeAccent.patternMotion = serializedData.readBool(r4);
                                if (readInt32 >= 5) {
                                    themeAccent.patternSlug = serializedData.readString(r4);
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
                            themeInfo12.themeAccentsMap.put(themeAccent.id, themeAccent);
                            TLRPC$TL_theme tLRPC$TL_theme2 = themeAccent.info;
                            if (tLRPC$TL_theme2 != null) {
                                str2 = str3;
                                themeInfo12.accentsByThemeId.put(tLRPC$TL_theme2.id, themeAccent);
                            } else {
                                str2 = str3;
                            }
                            arrayList2.add(themeAccent);
                            themeInfo12.lastAccentId = Math.max(themeInfo12.lastAccentId, themeAccent.id);
                            i778++;
                            themeInfo = themeInfo5;
                            str3 = str2;
                            editor4 = editor2;
                            z3 = true;
                        }
                        str = str3;
                        themeInfo4 = themeInfo;
                        editor = editor4;
                    } else {
                        str = str3;
                        themeInfo4 = themeInfo;
                        editor = editor4;
                        String str6 = "accent_for_" + themeInfo12.assetName;
                        int i779 = globalMainSettings.getInt(str6, 0);
                        if (i779 != 0) {
                            if (editor3 == null) {
                                editor3 = globalMainSettings.edit();
                                editor4 = sharedPreferences.edit();
                            } else {
                                editor4 = editor;
                            }
                            editor3.remove(str6);
                            int size = themeInfo12.themeAccents.size();
                            int i780 = 0;
                            while (true) {
                                if (i780 >= size) {
                                    z = false;
                                    break;
                                }
                                ThemeAccent themeAccent2 = themeInfo12.themeAccents.get(i780);
                                if (themeAccent2.accentColor == i779) {
                                    themeInfo12.currentAccentId = themeAccent2.id;
                                    z = true;
                                    break;
                                }
                                i780++;
                            }
                            if (!z) {
                                ThemeAccent themeAccent3 = new ThemeAccent();
                                themeAccent3.id = 100;
                                themeAccent3.accentColor = i779;
                                themeAccent3.parentTheme = themeInfo12;
                                themeInfo12.themeAccentsMap.put(100, themeAccent3);
                                arrayList2.add(0, themeAccent3);
                                themeInfo12.currentAccentId = 100;
                                themeInfo12.lastAccentId = FileLoader.MEDIA_DIR_VIDEO_PUBLIC;
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
                                editor4.putString("accents_" + themeInfo12.assetName, Base64.encodeToString(serializedData2.toByteArray(), 3));
                            }
                            editor4.putInt("accent_current_" + themeInfo12.assetName, themeInfo12.currentAccentId);
                            if (!arrayList2.isEmpty()) {
                                themeInfo12.themeAccents.addAll(0, arrayList2);
                                sortAccents(themeInfo12);
                            }
                            sparseArray = themeInfo12.themeAccentsMap;
                            if (sparseArray != null && sparseArray.get(themeInfo12.currentAccentId) == null) {
                                themeInfo12.currentAccentId = !themeInfo12.firstAccentIsDefault ? DEFALT_THEME_ACCENT_ID : 0;
                            }
                            themeInfo12.loadWallpapers(sharedPreferences);
                            accent = themeInfo12.getAccent(false);
                            if (accent == null) {
                                themeInfo12.overrideWallpaper = accent.overrideWallpaper;
                            }
                        }
                    }
                    editor4 = editor;
                    if (!arrayList2.isEmpty()) {
                    }
                    sparseArray = themeInfo12.themeAccentsMap;
                    if (sparseArray != null) {
                        themeInfo12.currentAccentId = !themeInfo12.firstAccentIsDefault ? DEFALT_THEME_ACCENT_ID : 0;
                    }
                    themeInfo12.loadWallpapers(sharedPreferences);
                    accent = themeInfo12.getAccent(false);
                    if (accent == null) {
                    }
                }
                themeInfo = themeInfo4;
                str3 = str;
                str4 = null;
            }
            String str7 = str3;
            ThemeInfo themeInfo13 = themeInfo;
            SharedPreferences.Editor editor5 = editor4;
            int i781 = 3;
            if (editor3 != null) {
                editor3.commit();
                editor5.commit();
            }
            if (Build.VERSION.SDK_INT < 29) {
                i781 = 0;
            }
            selectedAutoNightType = globalMainSettings.getInt("selectedAutoNightType", i781);
            autoNightScheduleByLocation = globalMainSettings.getBoolean("autoNightScheduleByLocation", false);
            autoNightBrighnessThreshold = globalMainSettings.getFloat("autoNightBrighnessThreshold", 0.25f);
            autoNightDayStartTime = globalMainSettings.getInt("autoNightDayStartTime", 1320);
            autoNightDayEndTime = globalMainSettings.getInt("autoNightDayEndTime", 480);
            autoNightSunsetTime = globalMainSettings.getInt("autoNightSunsetTime", 1320);
            autoNightSunriseTime = globalMainSettings.getInt("autoNightSunriseTime", 480);
            autoNightCityName = globalMainSettings.getString("autoNightCityName", str7);
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
            if (themeInfo13 == null) {
                themeInfo3 = defaultTheme;
            } else {
                currentDayTheme = themeInfo13;
                themeInfo3 = themeInfo13;
            }
            if (globalMainSettings.contains("overrideThemeWallpaper") || globalMainSettings.contains("selectedBackground2")) {
                boolean z4 = globalMainSettings.getBoolean("overrideThemeWallpaper", false);
                long j3 = globalMainSettings.getLong("selectedBackground2", 1000001L);
                if (j3 == -1 || (z4 && j3 != -2 && j3 != 1000001)) {
                    OverrideWallpaperInfo overrideWallpaperInfo = new OverrideWallpaperInfo();
                    overrideWallpaperInfo.color = globalMainSettings.getInt("selectedColor", 0);
                    overrideWallpaperInfo.slug = globalMainSettings.getString("selectedBackgroundSlug", str7);
                    if (j3 >= -100 && j3 <= -1 && overrideWallpaperInfo.color != 0) {
                        overrideWallpaperInfo.slug = "c";
                        overrideWallpaperInfo.fileName = str7;
                        overrideWallpaperInfo.originalFileName = str7;
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
                themeInfo3 = currentNightTheme;
            }
            applyTheme(themeInfo3, false, false, needSwitchToTheme == 2);
            AndroidUtilities.runOnUIThread(MessagesController$$ExternalSyntheticLambda242.INSTANCE);
            ambientSensorListener = new SensorEventListener() { // from class: org.telegram.ui.ActionBar.Theme.9
                @Override // android.hardware.SensorEventListener
                public void onAccuracyChanged(Sensor sensor, int i782) {
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
            Paint paint = new Paint();
            DEBUG_RED = paint;
            paint.setColor(-65536);
            Paint paint2 = new Paint();
            DEBUG_BLUE = paint2;
            paint2.setColor(-16776961);
        } catch (Exception e2) {
            FileLog.e(e2);
            throw new RuntimeException(e2);
        }
    }

    public static void applyDefaultShadow(Paint paint) {
        paint.setShadowLayer(AndroidUtilities.dpf2(1.0f), 0.0f, AndroidUtilities.dpf2(0.33f), default_shadow_color);
    }

    /* loaded from: classes3.dex */
    public static class MessageDrawable extends Drawable {
        public static MotionBackgroundDrawable[] motionBackground = new MotionBackgroundDrawable[3];
        private int alpha;
        private Drawable[][] backgroundDrawable;
        private int[][] backgroundDrawableColor;
        private Rect backupRect;
        private boolean botButtonsBottom;
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
        private float overrideRounding;
        private Paint paint;
        private Path path;
        PathDrawParams pathDrawCacheParams;
        private RectF rect;
        private ResourcesProvider resourcesProvider;
        private Paint selectedPaint;
        private Drawable[] shadowDrawable;
        private Bitmap[] shadowDrawableBitmap;
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
            this.shadowDrawableBitmap = new Bitmap[4];
            this.shadowDrawable = new Drawable[4];
            this.shadowDrawableColor = new int[]{-1, -1, -1, -1};
            this.currentBackgroundDrawableRadius = new int[][]{new int[]{-1, -1, -1, -1}, new int[]{-1, -1, -1, -1}, new int[]{-1, -1, -1, -1}, new int[]{-1, -1, -1, -1}};
            this.backgroundDrawable = (Drawable[][]) Array.newInstance(Drawable.class, 4, 4);
            this.backgroundDrawableColor = new int[][]{new int[]{-1, -1, -1, -1}, new int[]{-1, -1, -1, -1}, new int[]{-1, -1, -1, -1}, new int[]{-1, -1, -1, -1}};
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
                char c = 2;
                if (this.isCrossfadeBackground && (bitmap = this.crosfadeFromBitmap) != null) {
                    char c2 = this.currentType != 2 ? (char) 0 : (char) 1;
                    float min = 1.0f / Math.min(bitmap.getWidth() / motionBackground[c2].getBounds().width(), this.crosfadeFromBitmap.getHeight() / motionBackground[c2].getBounds().height());
                    this.matrix.postScale(min, min);
                    return;
                }
                if (!this.themePreview) {
                    c = this.currentType != 2 ? (char) 0 : (char) 1;
                }
                Bitmap bitmap2 = motionBackground[c].getBitmap();
                float min2 = 1.0f / Math.min(bitmap2.getWidth() / motionBackground[c].getBounds().width(), bitmap2.getHeight() / motionBackground[c].getBounds().height());
                this.matrix.postScale(min2, min2);
            }
        }

        public Shader getGradientShader() {
            return this.gradientShader;
        }

        public Matrix getMatrix() {
            return this.matrix;
        }

        protected int getColor(int i) {
            if (this.currentType == 2) {
                return Theme.getColor(i);
            }
            ResourcesProvider resourcesProvider = this.resourcesProvider;
            if (resourcesProvider != null) {
                return resourcesProvider.getColor(i);
            }
            return Theme.getColor(i);
        }

        protected int getCurrentColor(int i) {
            if (this.currentType == 2) {
                return Theme.getColor(i);
            }
            ResourcesProvider resourcesProvider = this.resourcesProvider;
            return resourcesProvider != null ? resourcesProvider.getCurrentColor(i) : Theme.currentColors.get(i);
        }

        public void setBotButtonsBottom(boolean z) {
            this.botButtonsBottom = z;
        }

        public void setTop(int i, int i2, int i3, boolean z, boolean z2) {
            setTop(i, i2, i3, i3, 0, 0, z, z2);
        }

        public void setTop(int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2) {
            int color;
            int i7;
            int i8;
            int i9;
            boolean z3;
            char c;
            int i10;
            int i11;
            MessageDrawable messageDrawable = this.crossfadeFromDrawable;
            if (messageDrawable != null) {
                messageDrawable.setTop(i, i2, i3, i4, i5, i6, z, z2);
            }
            if (this.isOut) {
                color = getColor(this.isSelected ? Theme.key_chat_outBubbleSelected : Theme.key_chat_outBubble);
                int currentColor = getCurrentColor(Theme.key_chat_outBubbleGradient1);
                int currentColor2 = getCurrentColor(Theme.key_chat_outBubbleGradient2);
                i7 = currentColor;
                i8 = currentColor2;
                i9 = getCurrentColor(Theme.key_chat_outBubbleGradient3);
                z3 = getCurrentColor(Theme.key_chat_outBubbleGradientAnimated) != 0;
            } else {
                color = getColor(this.isSelected ? Theme.key_chat_inBubbleSelected : Theme.key_chat_inBubble);
                i7 = 0;
                i8 = 0;
                i9 = 0;
                z3 = false;
            }
            if (i7 != 0) {
                color = getColor(Theme.key_chat_outBubble);
            }
            if (this.themePreview) {
                c = 2;
            } else {
                c = this.currentType == 2 ? (char) 1 : (char) 0;
            }
            if (!this.isCrossfadeBackground && i8 != 0 && z3) {
                MotionBackgroundDrawable[] motionBackgroundDrawableArr = motionBackground;
                if (motionBackgroundDrawableArr[c] != null) {
                    int[] colors = motionBackgroundDrawableArr[c].getColors();
                    this.currentColor = colors[0];
                    this.currentGradientColor1 = colors[1];
                    this.currentGradientColor2 = colors[2];
                    this.currentGradientColor3 = colors[3];
                }
            }
            if (this.isCrossfadeBackground && i8 != 0 && z3) {
                if (i3 == this.currentBackgroundHeight && this.crosfadeFromBitmapShader != null && this.currentColor == color && this.currentGradientColor1 == i7 && this.currentGradientColor2 == i8 && this.currentGradientColor3 == i9 && this.currentAnimateGradient == z3) {
                    i11 = -1;
                } else {
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
                    i11 = -1;
                    motionBackground[c].setColors(color, i7, i8, i9, this.crosfadeFromBitmap);
                    this.crosfadeFromBitmapShader.setLocalMatrix(this.matrix);
                }
                Shader shader = this.crosfadeFromBitmapShader;
                this.gradientShader = shader;
                this.paint.setShader(shader);
                this.paint.setColor(i11);
                this.currentColor = color;
                this.currentAnimateGradient = z3;
                this.currentGradientColor1 = i7;
                this.currentGradientColor2 = i8;
                this.currentGradientColor3 = i9;
            } else if (i7 != 0 && (this.gradientShader == null || i3 != this.currentBackgroundHeight || this.currentColor != color || this.currentGradientColor1 != i7 || this.currentGradientColor2 != i8 || this.currentGradientColor3 != i9 || this.currentAnimateGradient != z3)) {
                if (i8 != 0 && z3) {
                    MotionBackgroundDrawable[] motionBackgroundDrawableArr3 = motionBackground;
                    if (motionBackgroundDrawableArr3[c] == null) {
                        motionBackgroundDrawableArr3[c] = new MotionBackgroundDrawable();
                        if (this.currentType != 2) {
                            motionBackground[c].setPostInvalidateParent(true);
                        }
                        motionBackground[c].setRoundRadius(AndroidUtilities.dp(1.0f));
                    }
                    motionBackground[c].setColors(color, i7, i8, i9);
                    this.gradientShader = motionBackground[c].getBitmapShader();
                } else if (i8 == 0) {
                    this.gradientShader = new LinearGradient(0.0f, i5, 0.0f, i3, new int[]{i7, color}, (float[]) null, Shader.TileMode.CLAMP);
                } else if (i9 != 0) {
                    this.gradientShader = new LinearGradient(0.0f, i5, 0.0f, i3, new int[]{i9, i8, i7, color}, (float[]) null, Shader.TileMode.CLAMP);
                } else {
                    this.gradientShader = new LinearGradient(0.0f, i5, 0.0f, i3, new int[]{i8, i7, color}, (float[]) null, Shader.TileMode.CLAMP);
                }
                this.paint.setShader(this.gradientShader);
                this.currentColor = color;
                this.currentAnimateGradient = z3;
                this.currentGradientColor1 = i7;
                this.currentGradientColor2 = i8;
                this.currentGradientColor3 = i9;
                this.paint.setColor(-1);
            } else if (i7 == 0) {
                if (this.gradientShader != null) {
                    this.gradientShader = null;
                    this.paint.setShader(null);
                }
                this.paint.setColor(color);
            }
            if (this.gradientShader instanceof BitmapShader) {
                i10 = 0;
                motionBackground[c].setBounds(0, i5, i2, i3 - i4);
            } else {
                i10 = 0;
            }
            this.currentBackgroundHeight = i3;
            if (this.gradientShader instanceof BitmapShader) {
                i10 = i4;
            }
            this.topY = i - i10;
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

        public Drawable getBackgroundDrawable() {
            char c;
            int color;
            int i = this.overrideRoundRadius;
            boolean z = false;
            if (i == 0) {
                i = this.overrideRounding > 0.0f ? 0 : AndroidUtilities.dp(SharedConfig.bubbleRadius);
            }
            boolean z2 = this.isTopNear;
            char c2 = 3;
            if (z2 && this.isBottomNear) {
                c = 3;
            } else if (z2) {
                c = 2;
            } else {
                c = this.isBottomNear ? (char) 1 : (char) 0;
            }
            boolean z3 = this.isSelected;
            if (!z3 || !this.botButtonsBottom) {
                if (z3) {
                    c2 = 1;
                } else {
                    c2 = this.botButtonsBottom ? (char) 2 : (char) 0;
                }
            }
            boolean z4 = (this.gradientShader != null || z3 || this.isCrossfadeBackground) ? false : true;
            int color2 = getColor(this.isOut ? Theme.key_chat_outBubbleShadow : Theme.key_chat_inBubbleShadow);
            if (this.lastDrawWithShadow != z4 || this.currentBackgroundDrawableRadius[c2][c] != i || (z4 && this.shadowDrawableColor[c] != color2)) {
                this.currentBackgroundDrawableRadius[c2][c] = i;
                try {
                    Bitmap createBitmap = Bitmap.createBitmap(dp(50.0f), dp(40.0f), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    this.backupRect.set(getBounds());
                    if (z4) {
                        this.shadowDrawableColor[c] = color2;
                        Paint paint = new Paint(1);
                        paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, dp(40.0f), new int[]{358573417, 694117737}, (float[]) null, Shader.TileMode.CLAMP));
                        paint.setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.MULTIPLY));
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
                    this.backgroundDrawable[c2][c] = new NinePatchDrawable(createBitmap, getByteBuffer((createBitmap.getWidth() / 2) - 1, (createBitmap.getWidth() / 2) + 1, (createBitmap.getHeight() / 2) - 1, (createBitmap.getHeight() / 2) + 1).array(), new Rect(), null);
                    try {
                        setBounds(this.backupRect);
                    } catch (Throwable unused) {
                    }
                    z = true;
                } catch (Throwable unused2) {
                }
            }
            this.lastDrawWithShadow = z4;
            if (this.isSelected) {
                color = getColor(this.isOut ? Theme.key_chat_outBubbleSelected : Theme.key_chat_inBubbleSelected);
            } else {
                color = getColor(this.isOut ? Theme.key_chat_outBubble : Theme.key_chat_inBubble);
            }
            Drawable[][] drawableArr = this.backgroundDrawable;
            if (drawableArr[c2][c] != null && (this.backgroundDrawableColor[c2][c] != color || z)) {
                drawableArr[c2][c].setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                this.backgroundDrawableColor[c2][c] = color;
            }
            return this.backgroundDrawable[c2][c];
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
                Bitmap[] bitmapArr = this.shadowDrawableBitmap;
                if (bitmapArr[c] != null) {
                    bitmapArr[c].recycle();
                }
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
                    this.shadowDrawableBitmap[c] = createBitmap;
                    this.shadowDrawable[c] = new NinePatchDrawable(createBitmap, getByteBuffer((createBitmap.getWidth() / 2) - 1, (createBitmap.getWidth() / 2) + 1, (createBitmap.getHeight() / 2) - 1, (createBitmap.getHeight() / 2) + 1).array(), new Rect(), null);
                    z2 = true;
                } catch (Throwable unused) {
                }
            }
            int color = getColor(this.isOut ? Theme.key_chat_outBubbleShadow : Theme.key_chat_inBubbleShadow);
            Drawable[] drawableArr = this.shadowDrawable;
            if (drawableArr[c] != null && (this.shadowDrawableColor[c] != color || z2)) {
                drawableArr[c].setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                this.shadowDrawableColor[c] = color;
            }
            return this.shadowDrawable[c];
        }

        protected void finalize() throws Throwable {
            Bitmap[] bitmapArr;
            super.finalize();
            for (Bitmap bitmap : this.shadowDrawableBitmap) {
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
            Arrays.fill(this.shadowDrawableBitmap, (Object) null);
            Arrays.fill(this.shadowDrawable, (Object) null);
            Arrays.fill(this.currentShadowDrawableRadius, -1);
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

        /* JADX WARN: Removed duplicated region for block: B:120:0x0371  */
        /* JADX WARN: Removed duplicated region for block: B:53:0x0100  */
        /* JADX WARN: Removed duplicated region for block: B:54:0x0107  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x011b  */
        /* JADX WARN: Removed duplicated region for block: B:63:0x0122  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void draw(Canvas canvas, Paint paint) {
            int dp;
            boolean z;
            boolean z2;
            PathDrawParams pathDrawParams;
            Path path;
            boolean z3;
            int height;
            int i;
            Drawable backgroundDrawable;
            Canvas canvas2 = canvas;
            Rect bounds = getBounds();
            if (paint == null && this.gradientShader == null && this.overrideRoundRadius == 0 && this.overrideRounding <= 0.0f && (backgroundDrawable = getBackgroundDrawable()) != null) {
                backgroundDrawable.setBounds(bounds);
                backgroundDrawable.draw(canvas2);
                return;
            }
            int dp2 = dp(2.0f);
            int i2 = this.overrideRoundRadius;
            if (i2 != 0) {
                dp = i2;
            } else if (this.overrideRounding > 0.0f) {
                i2 = AndroidUtilities.lerp(dp(SharedConfig.bubbleRadius), Math.min(bounds.width(), bounds.height()) / 2, this.overrideRounding);
                dp = AndroidUtilities.lerp(dp(Math.min(6, SharedConfig.bubbleRadius)), Math.min(bounds.width(), bounds.height()) / 2, this.overrideRounding);
            } else if (this.currentType == 2) {
                i2 = dp(6.0f);
                dp = dp(6.0f);
            } else {
                i2 = dp(SharedConfig.bubbleRadius);
                dp = dp(Math.min(6, SharedConfig.bubbleRadius));
            }
            int dp3 = dp(6.0f);
            Paint paint2 = paint == null ? this.paint : paint;
            if (paint == null && this.gradientShader != null) {
                this.matrix.reset();
                applyMatrixScale();
                this.matrix.postTranslate(0.0f, -this.topY);
                this.gradientShader.setLocalMatrix(this.matrix);
            }
            int max = Math.max(bounds.top, 0);
            if (this.pathDrawCacheParams == null || bounds.height() >= this.currentBackgroundHeight) {
                z = this.currentType != 1 ? (this.topY + bounds.bottom) - i2 < this.currentBackgroundHeight : (this.topY + bounds.bottom) - (dp3 * 2) < this.currentBackgroundHeight;
                if (this.topY + (i2 * 2) < 0) {
                    z2 = false;
                    pathDrawParams = this.pathDrawCacheParams;
                    if (pathDrawParams == null) {
                        path = pathDrawParams.path;
                        z3 = pathDrawParams.invalidatePath(bounds, z, z2);
                    } else {
                        path = this.path;
                        z3 = true;
                    }
                    if (!z3 || this.overrideRoundRadius != 0) {
                        path.rewind();
                        height = (bounds.height() - dp2) >> 1;
                        if (i2 > height) {
                            i2 = height;
                        }
                        if (!this.isOut) {
                            if (this.drawFullBubble || this.currentType == 2 || paint != null || z) {
                                int i3 = this.botButtonsBottom ? dp : i2;
                                if (this.currentType == 1) {
                                    path.moveTo((bounds.right - dp(8.0f)) - i3, bounds.bottom - dp2);
                                } else {
                                    path.moveTo(bounds.right - dp(2.6f), bounds.bottom - dp2);
                                }
                                path.lineTo(bounds.left + dp2 + i3, bounds.bottom - dp2);
                                RectF rectF = this.rect;
                                int i4 = bounds.left;
                                i = dp;
                                int i5 = bounds.bottom;
                                int i6 = i3 * 2;
                                rectF.set(i4 + dp2, (i5 - dp2) - i6, i4 + dp2 + i6, i5 - dp2);
                                path.arcTo(this.rect, 90.0f, 90.0f, false);
                            } else {
                                path.moveTo(bounds.right - dp(8.0f), (max - this.topY) + this.currentBackgroundHeight);
                                path.lineTo(bounds.left + dp2, (max - this.topY) + this.currentBackgroundHeight);
                                i = dp;
                            }
                            if (this.drawFullBubble || this.currentType == 2 || paint != null || z2) {
                                path.lineTo(bounds.left + dp2, bounds.top + dp2 + i2);
                                RectF rectF2 = this.rect;
                                int i7 = bounds.left;
                                int i8 = bounds.top;
                                int i9 = i2 * 2;
                                rectF2.set(i7 + dp2, i8 + dp2, i7 + dp2 + i9, i8 + dp2 + i9);
                                path.arcTo(this.rect, 180.0f, 90.0f, false);
                                int i10 = this.isTopNear ? i : i2;
                                if (this.currentType == 1) {
                                    path.lineTo((bounds.right - dp2) - i10, bounds.top + dp2);
                                    RectF rectF3 = this.rect;
                                    int i11 = bounds.right;
                                    int i12 = i10 * 2;
                                    int i13 = bounds.top;
                                    rectF3.set((i11 - dp2) - i12, i13 + dp2, i11 - dp2, i13 + dp2 + i12);
                                } else {
                                    path.lineTo((bounds.right - dp(8.0f)) - i10, bounds.top + dp2);
                                    int i14 = i10 * 2;
                                    this.rect.set((bounds.right - dp(8.0f)) - i14, bounds.top + dp2, bounds.right - dp(8.0f), bounds.top + dp2 + i14);
                                }
                                path.arcTo(this.rect, 270.0f, 90.0f, false);
                            } else {
                                path.lineTo(bounds.left + dp2, (max - this.topY) - dp(2.0f));
                                if (this.currentType == 1) {
                                    path.lineTo(bounds.right - dp2, (max - this.topY) - dp(2.0f));
                                } else {
                                    path.lineTo(bounds.right - dp(8.0f), (max - this.topY) - dp(2.0f));
                                }
                            }
                            int i15 = this.currentType;
                            if (i15 == 1) {
                                if (paint != null || z) {
                                    int i16 = this.isBottomNear ? i : i2;
                                    path.lineTo(bounds.right - dp2, (bounds.bottom - dp2) - i16);
                                    RectF rectF4 = this.rect;
                                    int i17 = bounds.right;
                                    int i18 = i16 * 2;
                                    int i19 = bounds.bottom;
                                    rectF4.set((i17 - dp2) - i18, (i19 - dp2) - i18, i17 - dp2, i19 - dp2);
                                    path.arcTo(this.rect, 0.0f, 90.0f, false);
                                } else {
                                    path.lineTo(bounds.right - dp2, (max - this.topY) + this.currentBackgroundHeight);
                                }
                            } else if (this.drawFullBubble || i15 == 2 || paint != null || z) {
                                path.lineTo(bounds.right - dp(8.0f), ((bounds.bottom - dp2) - dp3) - dp(3.0f));
                                int i20 = dp3 * 2;
                                this.rect.set(bounds.right - dp(8.0f), ((bounds.bottom - dp2) - i20) - dp(9.0f), (bounds.right - dp(7.0f)) + i20, (bounds.bottom - dp2) - dp(1.0f));
                                path.arcTo(this.rect, 180.0f, -83.0f, false);
                            } else {
                                path.lineTo(bounds.right - dp(8.0f), (max - this.topY) + this.currentBackgroundHeight);
                            }
                        } else {
                            int i21 = dp;
                            if (this.drawFullBubble || this.currentType == 2 || paint != null || z) {
                                int i22 = this.botButtonsBottom ? i21 : i2;
                                if (this.currentType == 1) {
                                    path.moveTo(bounds.left + dp(8.0f) + i22, bounds.bottom - dp2);
                                } else {
                                    path.moveTo(bounds.left + dp(2.6f), bounds.bottom - dp2);
                                }
                                path.lineTo((bounds.right - dp2) - i22, bounds.bottom - dp2);
                                RectF rectF5 = this.rect;
                                int i23 = bounds.right;
                                int i24 = i22 * 2;
                                int i25 = bounds.bottom;
                                rectF5.set((i23 - dp2) - i24, (i25 - dp2) - i24, i23 - dp2, i25 - dp2);
                                path.arcTo(this.rect, 90.0f, -90.0f, false);
                            } else {
                                path.moveTo(bounds.left + dp(8.0f), (max - this.topY) + this.currentBackgroundHeight);
                                path.lineTo(bounds.right - dp2, (max - this.topY) + this.currentBackgroundHeight);
                            }
                            if (this.drawFullBubble || this.currentType == 2 || paint != null || z2) {
                                path.lineTo(bounds.right - dp2, bounds.top + dp2 + i2);
                                RectF rectF6 = this.rect;
                                int i26 = bounds.right;
                                int i27 = i2 * 2;
                                int i28 = bounds.top;
                                rectF6.set((i26 - dp2) - i27, i28 + dp2, i26 - dp2, i28 + dp2 + i27);
                                path.arcTo(this.rect, 0.0f, -90.0f, false);
                                int i29 = this.isTopNear ? i21 : i2;
                                if (this.currentType == 1) {
                                    path.lineTo(bounds.left + dp2 + i29, bounds.top + dp2);
                                    RectF rectF7 = this.rect;
                                    int i30 = bounds.left;
                                    int i31 = bounds.top;
                                    int i32 = i29 * 2;
                                    rectF7.set(i30 + dp2, i31 + dp2, i30 + dp2 + i32, i31 + dp2 + i32);
                                } else {
                                    path.lineTo(bounds.left + dp(8.0f) + i29, bounds.top + dp2);
                                    int i33 = i29 * 2;
                                    this.rect.set(bounds.left + dp(8.0f), bounds.top + dp2, bounds.left + dp(8.0f) + i33, bounds.top + dp2 + i33);
                                }
                                path.arcTo(this.rect, 270.0f, -90.0f, false);
                            } else {
                                path.lineTo(bounds.right - dp2, (max - this.topY) - dp(2.0f));
                                if (this.currentType == 1) {
                                    path.lineTo(bounds.left + dp2, (max - this.topY) - dp(2.0f));
                                } else {
                                    path.lineTo(bounds.left + dp(8.0f), (max - this.topY) - dp(2.0f));
                                }
                            }
                            int i34 = this.currentType;
                            if (i34 == 1) {
                                if (paint != null || z) {
                                    int i35 = this.isBottomNear ? i21 : i2;
                                    path.lineTo(bounds.left + dp2, (bounds.bottom - dp2) - i35);
                                    RectF rectF8 = this.rect;
                                    int i36 = bounds.left;
                                    int i37 = bounds.bottom;
                                    int i38 = i35 * 2;
                                    rectF8.set(i36 + dp2, (i37 - dp2) - i38, i36 + dp2 + i38, i37 - dp2);
                                    path.arcTo(this.rect, 180.0f, -90.0f, false);
                                } else {
                                    path.lineTo(bounds.left + dp2, (max - this.topY) + this.currentBackgroundHeight);
                                }
                            } else if (this.drawFullBubble || i34 == 2 || paint != null || z) {
                                path.lineTo(bounds.left + dp(8.0f), ((bounds.bottom - dp2) - dp3) - dp(3.0f));
                                int i39 = dp3 * 2;
                                this.rect.set((bounds.left + dp(7.0f)) - i39, ((bounds.bottom - dp2) - i39) - dp(9.0f), bounds.left + dp(8.0f), (bounds.bottom - dp2) - dp(1.0f));
                                path.arcTo(this.rect, 0.0f, 83.0f, false);
                            } else {
                                path.lineTo(bounds.left + dp(8.0f), (max - this.topY) + this.currentBackgroundHeight);
                            }
                        }
                        path.close();
                        canvas2 = canvas;
                    }
                    canvas2.drawPath(path, paint2);
                    if (this.gradientShader == null && this.isSelected && paint == null) {
                        int color = getColor(Theme.key_chat_outBubbleGradientSelectedOverlay);
                        this.selectedPaint.setColor(ColorUtils.setAlphaComponent(color, (int) ((Color.alpha(color) * this.alpha) / 255.0f)));
                        canvas2.drawPath(path, this.selectedPaint);
                        return;
                    }
                    return;
                }
            } else {
                z = true;
            }
            z2 = true;
            pathDrawParams = this.pathDrawCacheParams;
            if (pathDrawParams == null) {
            }
            if (!z3) {
            }
            path.rewind();
            height = (bounds.height() - dp2) >> 1;
            if (i2 > height) {
            }
            if (!this.isOut) {
            }
            path.close();
            canvas2 = canvas;
            canvas2.drawPath(path, paint2);
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
                    this.selectedPaint.setAlpha((int) (Color.alpha(getColor(Theme.key_chat_outBubbleGradientSelectedOverlay)) * (i / 255.0f)));
                }
            }
            if (this.gradientShader == null) {
                Drawable backgroundDrawable = getBackgroundDrawable();
                if (Build.VERSION.SDK_INT >= 19) {
                    if (backgroundDrawable.getAlpha() != i) {
                        backgroundDrawable.setAlpha(i);
                        return;
                    }
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

        public void setRoundingRadius(float f) {
            this.overrideRounding = f;
        }

        public void setResourceProvider(ResourcesProvider resourcesProvider) {
            this.resourcesProvider = resourcesProvider;
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

            public Path getPath() {
                return this.path;
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class PatternsLoader implements NotificationCenter.NotificationCenterDelegate {
        private static PatternsLoader loader;
        private int account = UserConfig.selectedAccount;
        private HashMap<String, LoadingPattern> watingForLoad;

        /* JADX INFO: Access modifiers changed from: private */
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

        /* JADX INFO: Access modifiers changed from: private */
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

        /* JADX INFO: Access modifiers changed from: private */
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
                            1 r4 = bitmap;
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
                                r4 = 0;
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

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: checkCurrentWallpaperInternal */
        public void lambda$checkCurrentWallpaper$2(ArrayList<ThemeAccent> arrayList, boolean z) {
            if (arrayList != null && Theme.currentTheme.themeAccents != null && !Theme.currentTheme.themeAccents.isEmpty() && arrayList.contains(Theme.currentTheme.getAccent(false))) {
                Theme.reloadWallpaper(true);
            }
            if (z) {
                if (this.watingForLoad != null) {
                    NotificationCenter.getInstance(this.account).addObserver(this, NotificationCenter.fileLoaded);
                    NotificationCenter.getInstance(this.account).addObserver(this, NotificationCenter.fileLoadFailed);
                    for (Map.Entry<String, LoadingPattern> entry : this.watingForLoad.entrySet()) {
                        FileLoader.getInstance(this.account).loadFile(ImageLocation.getForDocument(entry.getValue().pattern.document), "wallpaper", null, 0, 1);
                    }
                    return;
                }
                return;
            }
            HashMap<String, LoadingPattern> hashMap = this.watingForLoad;
            if (hashMap == null || hashMap.isEmpty()) {
                NotificationCenter.getInstance(this.account).removeObserver(this, NotificationCenter.fileLoaded);
                NotificationCenter.getInstance(this.account).removeObserver(this, NotificationCenter.fileLoadFailed);
            }
        }

        private Bitmap createWallpaperForAccent(Bitmap bitmap, boolean z, File file, ThemeAccent themeAccent) {
            Bitmap bitmap2;
            File pathToWallpaper;
            Drawable drawable;
            int patternColor;
            Bitmap loadScreenSizedBitmap;
            int i;
            int i2;
            int i3;
            try {
                pathToWallpaper = themeAccent.getPathToWallpaper();
                drawable = null;
            } catch (Throwable th) {
                th = th;
                bitmap2 = bitmap;
            }
            if (pathToWallpaper == null) {
                return null;
            }
            ThemeInfo themeInfo = themeAccent.parentTheme;
            SparseIntArray themeFileValues = Theme.getThemeFileValues(null, themeInfo.assetName, null);
            Theme.checkIsDark(themeFileValues, themeInfo);
            int i4 = themeAccent.accentColor;
            int i5 = (int) themeAccent.backgroundOverrideColor;
            long j = themeAccent.backgroundGradientOverrideColor1;
            int i6 = (int) j;
            if (i6 == 0 && j == 0) {
                if (i5 != 0) {
                    i4 = i5;
                }
                int i7 = themeFileValues.get(Theme.key_chat_wallpaper_gradient_to1);
                if (i7 != 0) {
                    i6 = Theme.changeColorAccent(themeInfo, i4, i7);
                }
            } else {
                i4 = 0;
            }
            long j2 = themeAccent.backgroundGradientOverrideColor2;
            int i8 = (int) j2;
            if (i8 == 0 && j2 == 0 && (i3 = themeFileValues.get(Theme.key_chat_wallpaper_gradient_to2)) != 0) {
                i8 = Theme.changeColorAccent(themeInfo, i4, i3);
            }
            long j3 = themeAccent.backgroundGradientOverrideColor3;
            int i9 = (int) j3;
            if (i9 == 0 && j3 == 0 && (i2 = themeFileValues.get(Theme.key_chat_wallpaper_gradient_to3)) != 0) {
                i9 = Theme.changeColorAccent(themeInfo, i4, i2);
            }
            if (i5 == 0 && (i = themeFileValues.get(Theme.key_chat_wallpaper)) != 0) {
                i5 = Theme.changeColorAccent(themeInfo, i4, i);
            }
            if (i8 != 0) {
                patternColor = MotionBackgroundDrawable.getPatternColor(i5, i6, i8, i9);
            } else if (i6 != 0) {
                Drawable backgroundGradientDrawable = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(themeAccent.backgroundRotation), new int[]{i5, i6});
                patternColor = AndroidUtilities.getPatternColor(AndroidUtilities.getAverageColor(i5, i6));
                drawable = backgroundGradientDrawable;
            } else {
                drawable = new ColorDrawable(i5);
                patternColor = AndroidUtilities.getPatternColor(i5);
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
                    paint.setColorFilter(new PorterDuffColorFilter(patternColor, PorterDuff.Mode.SRC_IN));
                    paint.setAlpha((int) (Math.abs(themeAccent.patternIntensity) * 255.0f));
                    canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
                    createBitmap.compress(Bitmap.CompressFormat.JPEG, 87, new FileOutputStream(pathToWallpaper));
                } else {
                    FileOutputStream fileOutputStream = new FileOutputStream(pathToWallpaper);
                    bitmap2.compress(Bitmap.CompressFormat.PNG, 87, fileOutputStream);
                    fileOutputStream.close();
                }
            } catch (Throwable th2) {
                th = th2;
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
                if (remove != null) {
                    Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$PatternsLoader$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            Theme.PatternsLoader.this.lambda$didReceivedNotification$3(remove);
                        }
                    });
                }
            } else if (i != NotificationCenter.fileLoadFailed || hashMap.remove((String) objArr[0]) == null) {
            } else {
                checkCurrentWallpaper(null, false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
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

        public boolean fillAccentColors(SparseIntArray sparseIntArray, SparseIntArray sparseIntArray2) {
            int valueAt;
            boolean useBlackText;
            boolean z;
            int valueAt2;
            int valueAt3;
            int i;
            boolean useBlackText2;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            float[] tempHsv = Theme.getTempHsv(1);
            float[] tempHsv2 = Theme.getTempHsv(2);
            Color.colorToHSV(this.parentTheme.accentBaseColor, tempHsv);
            Color.colorToHSV(this.accentColor, tempHsv2);
            boolean isDark = this.parentTheme.isDark();
            if (this.accentColor != this.parentTheme.accentBaseColor || this.accentColor2 != 0) {
                for (int i7 = 0; i7 < Theme.defaultColors.length; i7++) {
                    if (!Theme.themeAccentExclusionKeys.contains(Integer.valueOf(i7))) {
                        int indexOfKey = sparseIntArray.indexOfKey(i7);
                        if (indexOfKey < 0) {
                            int i8 = Theme.fallbackKeys.get(i7, -1);
                            if (i8 < 0 || sparseIntArray.indexOfKey(i8) < 0) {
                                valueAt = Theme.defaultColors[i7];
                            }
                        } else {
                            valueAt = sparseIntArray.valueAt(indexOfKey);
                        }
                        int changeColorAccent = Theme.changeColorAccent(tempHsv, tempHsv2, valueAt, isDark);
                        if (changeColorAccent != valueAt) {
                            sparseIntArray2.put(i7, changeColorAccent);
                        }
                    }
                }
            }
            int i9 = this.myMessagesAccentColor;
            if ((i9 == 0 && this.accentColor == 0) || this.myMessagesGradientAccentColor1 == 0) {
                z = false;
            } else {
                if (i9 == 0) {
                    i9 = this.accentColor;
                }
                int i10 = Theme.key_chat_outBubble;
                int i11 = sparseIntArray.get(i10);
                if (i11 == 0) {
                    i11 = Theme.defaultColors[i10];
                }
                int colorDistance = AndroidUtilities.getColorDistance(i9, Theme.changeColorAccent(tempHsv, tempHsv2, i11, isDark));
                int colorDistance2 = AndroidUtilities.getColorDistance(i9, this.myMessagesGradientAccentColor1);
                if (this.myMessagesGradientAccentColor2 == 0) {
                    useBlackText = Theme.useBlackText(this.myMessagesAccentColor, this.myMessagesGradientAccentColor1);
                } else {
                    int averageColor = AndroidUtilities.getAverageColor(AndroidUtilities.getAverageColor(this.myMessagesAccentColor, this.myMessagesGradientAccentColor1), this.myMessagesGradientAccentColor2);
                    int i12 = this.myMessagesGradientAccentColor3;
                    if (i12 != 0) {
                        averageColor = AndroidUtilities.getAverageColor(averageColor, i12);
                    }
                    useBlackText = AndroidUtilities.computePerceivedBrightness(averageColor) > 0.705f;
                }
                z = useBlackText && colorDistance <= 35000 && colorDistance2 <= 35000;
                i9 = Theme.getAccentColor(tempHsv, i11, i9);
            }
            boolean z2 = (i9 == 0 || (((i5 = this.parentTheme.accentBaseColor) == 0 || i9 == i5) && ((i6 = this.accentColor) == 0 || i6 == i9))) ? false : true;
            if (z2 || this.accentColor2 != 0) {
                int i13 = this.accentColor2;
                if (i13 != 0) {
                    Color.colorToHSV(i13, tempHsv2);
                } else {
                    Color.colorToHSV(i9, tempHsv2);
                }
                for (int i14 = Theme.myMessagesStartIndex; i14 < Theme.myMessagesEndIndex; i14++) {
                    int indexOfKey2 = sparseIntArray.indexOfKey(i14);
                    if (indexOfKey2 < 0) {
                        int i15 = Theme.fallbackKeys.get(i14, -1);
                        if (i15 < 0 || sparseIntArray.get(i15, -1) < 0) {
                            valueAt3 = Theme.defaultColors[i14];
                        }
                    } else {
                        valueAt3 = sparseIntArray.valueAt(indexOfKey2);
                    }
                    int changeColorAccent2 = Theme.changeColorAccent(tempHsv, tempHsv2, valueAt3, isDark);
                    if (changeColorAccent2 != valueAt3) {
                        sparseIntArray2.put(i14, changeColorAccent2);
                    }
                }
                if (z2) {
                    Color.colorToHSV(i9, tempHsv2);
                    for (int i16 = Theme.myMessagesBubblesStartIndex; i16 < Theme.myMessagesBubblesEndIndex; i16++) {
                        int indexOfKey3 = sparseIntArray.indexOfKey(i16);
                        if (indexOfKey3 < 0) {
                            int i17 = Theme.fallbackKeys.get(i16, -1);
                            if (i17 < 0 || sparseIntArray.get(i17, -1) < 0) {
                                valueAt2 = Theme.defaultColors[i16];
                            }
                        } else {
                            valueAt2 = sparseIntArray.valueAt(indexOfKey3);
                        }
                        int changeColorAccent3 = Theme.changeColorAccent(tempHsv, tempHsv2, valueAt2, isDark);
                        if (changeColorAccent3 != valueAt2) {
                            sparseIntArray2.put(i16, changeColorAccent3);
                        }
                    }
                }
            }
            if (!z && (i = this.myMessagesGradientAccentColor1) != 0) {
                if (this.myMessagesGradientAccentColor2 == 0) {
                    useBlackText2 = Theme.useBlackText(this.myMessagesAccentColor, i);
                } else {
                    int averageColor2 = AndroidUtilities.getAverageColor(AndroidUtilities.getAverageColor(this.myMessagesAccentColor, i), this.myMessagesGradientAccentColor2);
                    int i18 = this.myMessagesGradientAccentColor3;
                    if (i18 != 0) {
                        averageColor2 = AndroidUtilities.getAverageColor(averageColor2, i18);
                    }
                    useBlackText2 = AndroidUtilities.computePerceivedBrightness(averageColor2) > 0.705f;
                }
                if (useBlackText2) {
                    i4 = -14606047;
                    i2 = -11184811;
                    i3 = 1291845632;
                } else {
                    i2 = -1118482;
                    i3 = 1308622847;
                    i4 = -1;
                }
                if (this.accentColor2 == 0) {
                    sparseIntArray2.put(Theme.key_chat_outAudioProgress, i3);
                    sparseIntArray2.put(Theme.key_chat_outAudioSelectedProgress, i3);
                    sparseIntArray2.put(Theme.key_chat_outAudioSeekbar, i3);
                    sparseIntArray2.put(Theme.key_chat_outAudioCacheSeekbar, i3);
                    sparseIntArray2.put(Theme.key_chat_outAudioSeekbarSelected, i3);
                    sparseIntArray2.put(Theme.key_chat_outAudioSeekbarFill, i4);
                    sparseIntArray2.put(Theme.key_chat_outVoiceSeekbar, i3);
                    sparseIntArray2.put(Theme.key_chat_outVoiceSeekbarSelected, i3);
                    sparseIntArray2.put(Theme.key_chat_outVoiceSeekbarFill, i4);
                    sparseIntArray2.put(Theme.key_chat_messageLinkOut, i4);
                    sparseIntArray2.put(Theme.key_chat_outForwardedNameText, i4);
                    sparseIntArray2.put(Theme.key_chat_outViaBotNameText, i4);
                    sparseIntArray2.put(Theme.key_chat_outReplyLine, i4);
                    sparseIntArray2.put(Theme.key_chat_outReplyNameText, i4);
                    sparseIntArray2.put(Theme.key_chat_outPreviewLine, i4);
                    sparseIntArray2.put(Theme.key_chat_outSiteNameText, i4);
                    sparseIntArray2.put(Theme.key_chat_outInstant, i4);
                    sparseIntArray2.put(Theme.key_chat_outInstantSelected, i4);
                    sparseIntArray2.put(Theme.key_chat_outPreviewInstantText, i4);
                    sparseIntArray2.put(Theme.key_chat_outViews, i4);
                    sparseIntArray2.put(Theme.key_chat_outViewsSelected, i4);
                    sparseIntArray2.put(Theme.key_chat_outAudioTitleText, i4);
                    sparseIntArray2.put(Theme.key_chat_outFileNameText, i4);
                    sparseIntArray2.put(Theme.key_chat_outContactNameText, i4);
                    sparseIntArray2.put(Theme.key_chat_outAudioPerformerText, i4);
                    sparseIntArray2.put(Theme.key_chat_outAudioPerformerSelectedText, i4);
                    sparseIntArray2.put(Theme.key_chat_outSentCheck, i4);
                    sparseIntArray2.put(Theme.key_chat_outSentCheckSelected, i4);
                    sparseIntArray2.put(Theme.key_chat_outSentCheckRead, i4);
                    sparseIntArray2.put(Theme.key_chat_outSentCheckReadSelected, i4);
                    sparseIntArray2.put(Theme.key_chat_outSentClock, i4);
                    sparseIntArray2.put(Theme.key_chat_outSentClockSelected, i4);
                    sparseIntArray2.put(Theme.key_chat_outMenu, i4);
                    sparseIntArray2.put(Theme.key_chat_outMenuSelected, i4);
                    sparseIntArray2.put(Theme.key_chat_outTimeText, i4);
                    sparseIntArray2.put(Theme.key_chat_outTimeSelectedText, i4);
                    sparseIntArray2.put(Theme.key_chat_outAudioDurationText, i2);
                    sparseIntArray2.put(Theme.key_chat_outAudioDurationSelectedText, i2);
                    sparseIntArray2.put(Theme.key_chat_outContactPhoneText, i2);
                    sparseIntArray2.put(Theme.key_chat_outContactPhoneSelectedText, i2);
                    sparseIntArray2.put(Theme.key_chat_outFileInfoText, i2);
                    sparseIntArray2.put(Theme.key_chat_outFileInfoSelectedText, i2);
                    sparseIntArray2.put(Theme.key_chat_outVenueInfoText, i2);
                    sparseIntArray2.put(Theme.key_chat_outVenueInfoSelectedText, i2);
                    sparseIntArray2.put(Theme.key_chat_outLoader, i4);
                    sparseIntArray2.put(Theme.key_chat_outLoaderSelected, i4);
                    sparseIntArray2.put(Theme.key_chat_outFileProgress, this.myMessagesAccentColor);
                    sparseIntArray2.put(Theme.key_chat_outFileProgressSelected, this.myMessagesAccentColor);
                    sparseIntArray2.put(Theme.key_chat_outMediaIcon, this.myMessagesAccentColor);
                    sparseIntArray2.put(Theme.key_chat_outMediaIconSelected, this.myMessagesAccentColor);
                }
                sparseIntArray2.put(Theme.key_chat_outReplyMessageText, i4);
                sparseIntArray2.put(Theme.key_chat_outReplyMediaMessageText, i4);
                sparseIntArray2.put(Theme.key_chat_outReplyMediaMessageSelectedText, i4);
                sparseIntArray2.put(Theme.key_chat_messageTextOut, i4);
            }
            if (z) {
                int i19 = Theme.key_chat_outLoader;
                if (AndroidUtilities.getColorDistance(-1, sparseIntArray2.indexOfKey(i19) >= 0 ? sparseIntArray2.get(i19) : 0) < 5000) {
                    z = false;
                }
            }
            int i20 = this.myMessagesAccentColor;
            if (i20 != 0 && this.myMessagesGradientAccentColor1 != 0) {
                sparseIntArray2.put(Theme.key_chat_outBubble, i20);
                sparseIntArray2.put(Theme.key_chat_outBubbleGradient1, this.myMessagesGradientAccentColor1);
                int i21 = this.myMessagesGradientAccentColor2;
                if (i21 != 0) {
                    sparseIntArray2.put(Theme.key_chat_outBubbleGradient2, i21);
                    int i22 = this.myMessagesGradientAccentColor3;
                    if (i22 != 0) {
                        sparseIntArray2.put(Theme.key_chat_outBubbleGradient3, i22);
                    }
                }
                sparseIntArray2.put(Theme.key_chat_outBubbleGradientAnimated, this.myMessagesAnimated ? 1 : 0);
            }
            long j = this.backgroundOverrideColor;
            int i23 = (int) j;
            if (i23 != 0) {
                sparseIntArray2.put(Theme.key_chat_wallpaper, i23);
            } else if (j != 0) {
                sparseIntArray2.delete(Theme.key_chat_wallpaper);
            }
            long j2 = this.backgroundGradientOverrideColor1;
            int i24 = (int) j2;
            if (i24 != 0) {
                sparseIntArray2.put(Theme.key_chat_wallpaper_gradient_to1, i24);
            } else if (j2 != 0) {
                sparseIntArray2.delete(Theme.key_chat_wallpaper_gradient_to1);
            }
            long j3 = this.backgroundGradientOverrideColor2;
            int i25 = (int) j3;
            if (i25 != 0) {
                sparseIntArray2.put(Theme.key_chat_wallpaper_gradient_to2, i25);
            } else if (j3 != 0) {
                sparseIntArray2.delete(Theme.key_chat_wallpaper_gradient_to2);
            }
            long j4 = this.backgroundGradientOverrideColor3;
            int i26 = (int) j4;
            if (i26 != 0) {
                sparseIntArray2.put(Theme.key_chat_wallpaper_gradient_to3, i26);
            } else if (j4 != 0) {
                sparseIntArray2.delete(Theme.key_chat_wallpaper_gradient_to3);
            }
            int i27 = this.backgroundRotation;
            if (i27 != 45) {
                sparseIntArray2.put(Theme.key_chat_wallpaper_gradient_rotation, i27);
            }
            int i28 = Theme.key_chat_outBubble;
            int i29 = sparseIntArray2.get(i28);
            if (i29 == 0) {
                i29 = Theme.getColor(i28);
            }
            int i30 = Theme.key_chat_inBubble;
            int i31 = sparseIntArray2.get(i30);
            if (i31 == 0) {
                i31 = Theme.getColor(i30);
            }
            TLRPC$TL_theme tLRPC$TL_theme = this.info;
            if (tLRPC$TL_theme != null && tLRPC$TL_theme.emoticon != null && !isDark) {
                sparseIntArray2.delete(Theme.key_chat_selectedBackground);
                int averageColor3 = averageColor(sparseIntArray2, Theme.key_chat_wallpaper_gradient_to1, Theme.key_chat_wallpaper_gradient_to2, Theme.key_chat_wallpaper_gradient_to3);
                if (averageColor3 == 0) {
                    averageColor3 = averageColor(sparseIntArray2, Theme.key_chat_wallpaper);
                }
                if (averageColor3 == 0) {
                    averageColor3 = this.accentColor;
                }
                int bubbleSelectedOverlay = bubbleSelectedOverlay(i29, averageColor3);
                sparseIntArray2.put(Theme.key_chat_outBubbleSelectedOverlay, bubbleSelectedOverlay);
                sparseIntArray2.put(Theme.key_chat_outBubbleGradientSelectedOverlay, bubbleSelectedOverlay);
                sparseIntArray2.put(Theme.key_chat_outBubbleSelected, Theme.blendOver(i29, bubbleSelectedOverlay));
                int bubbleSelectedOverlay2 = bubbleSelectedOverlay(i31, this.accentColor);
                sparseIntArray2.put(Theme.key_chat_inBubbleSelectedOverlay, bubbleSelectedOverlay2);
                sparseIntArray2.put(Theme.key_chat_inBubbleSelected, Theme.blendOver(i31, bubbleSelectedOverlay2));
            }
            if (!isDark) {
                sparseIntArray2.put(Theme.key_chat_inTextSelectionHighlight, textSelectionBackground(false, i31, this.accentColor));
                sparseIntArray2.put(Theme.key_chat_outTextSelectionHighlight, textSelectionBackground(true, i29, this.accentColor));
                sparseIntArray2.put(Theme.key_chat_outTextSelectionCursor, textSelectionHandle(i29, this.accentColor));
            }
            float hue = getHue(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText));
            sparseIntArray2.put(Theme.key_chat_outBubbleLocationPlaceholder, locationPlaceholderColor(hue, i29, isDark));
            sparseIntArray2.put(Theme.key_chat_inBubbleLocationPlaceholder, locationPlaceholderColor(hue, i31, isDark));
            int i32 = Theme.key_chat_messageLinkIn;
            int i33 = sparseIntArray2.get(i32);
            if (i33 == 0) {
                i33 = Theme.getColor(i32);
            }
            int i34 = Theme.key_chat_messageLinkOut;
            int i35 = sparseIntArray2.get(i34);
            if (i35 == 0) {
                i35 = Theme.getColor(i34);
            }
            sparseIntArray2.put(Theme.key_chat_linkSelectBackground, linkSelectionBackground(i33, i31, isDark));
            sparseIntArray2.put(Theme.key_chat_outLinkSelectBackground, linkSelectionBackground(i35, i29, isDark));
            int i36 = Theme.key_actionBarDefaultSubmenuBackground;
            int i37 = sparseIntArray2.get(i36);
            if (i37 == 0) {
                i37 = Theme.getColor(i36);
            }
            sparseIntArray2.put(Theme.key_actionBarDefaultSubmenuSeparator, Color.argb(Color.alpha(i37), Math.max(0, Color.red(i37) - 10), Math.max(0, Color.green(i37) - 10), Math.max(0, Color.blue(i37) - 10)));
            return !z;
        }

        private float getHue(int i) {
            Color.colorToHSV(i, this.tempHSV);
            return this.tempHSV[0];
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
            fArr[1] = Math.max(0.0f, Math.min(1.0f, fArr[1] - 0.1f));
            float[] fArr2 = this.tempHSV;
            fArr2[2] = Math.max(0.0f, Math.min(1.0f, fArr2[2] + (z ? 0.1f : 0.0f)));
            return Color.HSVToColor(51, this.tempHSV);
        }

        private int locationPlaceholderColor(float f, int i, boolean z) {
            if (z) {
                return 520093695;
            }
            Color.colorToHSV(i, this.tempHSV);
            float[] fArr = this.tempHSV;
            if (fArr[1] <= 0.0f || fArr[2] >= 1.0f || fArr[2] <= 0.0f) {
                fArr[0] = f;
                fArr[1] = 0.2f;
            } else {
                fArr[0] = MathUtils.clamp(fArr[0] + 0.22f, 0.0f, 1.0f);
                float[] fArr2 = this.tempHSV;
                fArr2[1] = MathUtils.clamp(fArr2[1] - 0.35f, 0.0f, 1.0f);
            }
            float[] fArr3 = this.tempHSV;
            fArr3[2] = MathUtils.clamp(fArr3[2] - 0.65f, 0.0f, 1.0f);
            return Color.HSVToColor(90, this.tempHSV);
        }

        private int averageColor(SparseIntArray sparseIntArray, int... iArr) {
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < iArr.length; i5++) {
                if (sparseIntArray.indexOfKey(iArr[i5]) >= 0) {
                    try {
                        int i6 = sparseIntArray.get(iArr[i5]);
                        i2 += Color.red(i6);
                        i3 += Color.green(i6);
                        i4 += Color.blue(i6);
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

        /* JADX WARN: Removed duplicated region for block: B:96:0x0274 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:79:0x026b -> B:93:0x026f). Please submit an issue!!! */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public File saveToFile() {
            String str;
            Throwable th;
            FileOutputStream fileOutputStream;
            FileOutputStream fileOutputStream2;
            File sharingDirectory = AndroidUtilities.getSharingDirectory();
            sharingDirectory.mkdirs();
            File file = new File(sharingDirectory, String.format(Locale.US, "%s_%d.attheme", this.parentTheme.getKey(), Integer.valueOf(this.id)));
            SparseIntArray themeFileValues = Theme.getThemeFileValues(null, this.parentTheme.assetName, null);
            SparseIntArray clone = themeFileValues.clone();
            fillAccentColors(themeFileValues, clone);
            if (TextUtils.isEmpty(this.patternSlug)) {
                str = null;
            } else {
                StringBuilder sb = new StringBuilder();
                if (this.patternMotion) {
                    sb.append("motion");
                }
                int i = clone.get(Theme.key_chat_wallpaper);
                if (i == 0) {
                    i = -1;
                }
                int i2 = clone.get(Theme.key_chat_wallpaper_gradient_to1);
                if (i2 == 0) {
                    i2 = 0;
                }
                int i3 = clone.get(Theme.key_chat_wallpaper_gradient_to2);
                if (i3 == 0) {
                    i3 = 0;
                }
                int i4 = clone.get(Theme.key_chat_wallpaper_gradient_to3);
                if (i4 == 0) {
                    i4 = 0;
                }
                int i5 = clone.get(Theme.key_chat_wallpaper_gradient_rotation);
                if (i5 == 0) {
                    i5 = 45;
                }
                String lowerCase = String.format("%02x%02x%02x", Integer.valueOf(((byte) (i >> 16)) & 255), Integer.valueOf(((byte) (i >> 8)) & 255), Byte.valueOf((byte) (i & 255))).toLowerCase();
                String lowerCase2 = i2 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i2 >> 16)) & 255), Integer.valueOf(((byte) (i2 >> 8)) & 255), Byte.valueOf((byte) (i2 & 255))).toLowerCase() : null;
                String lowerCase3 = i3 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i3 >> 16)) & 255), Integer.valueOf(((byte) (i3 >> 8)) & 255), Byte.valueOf((byte) (i3 & 255))).toLowerCase() : null;
                String lowerCase4 = i4 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i4 >> 16)) & 255), Integer.valueOf(((byte) (i4 >> 8)) & 255), Byte.valueOf((byte) (i4 & 255))).toLowerCase() : null;
                if (lowerCase2 == null || lowerCase3 == null) {
                    if (lowerCase2 != null) {
                        lowerCase = (lowerCase + "-" + lowerCase2) + "&rotation=" + i5;
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
            }
            StringBuilder sb2 = new StringBuilder();
            for (int i6 = 0; i6 < clone.size(); i6++) {
                try {
                    int keyAt = clone.keyAt(i6);
                    int valueAt = clone.valueAt(i6);
                    if (str == null || (Theme.key_chat_wallpaper != keyAt && Theme.key_chat_wallpaper_gradient_to1 != keyAt && Theme.key_chat_wallpaper_gradient_to2 != keyAt && Theme.key_chat_wallpaper_gradient_to3 != keyAt)) {
                        sb2.append(keyAt);
                        sb2.append("=");
                        sb2.append(valueAt);
                        sb2.append("\n");
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            try {
                fileOutputStream2 = new FileOutputStream(file);
            } catch (Exception e2) {
                e = e2;
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
            } catch (Exception e3) {
                e = e3;
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
                        } catch (Exception e4) {
                            FileLog.e(e4);
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

    public static int adaptHue(int i, int i2) {
        float[] tempHsv = getTempHsv(5);
        Color.colorToHSV(i2, tempHsv);
        float f = tempHsv[0];
        float f2 = tempHsv[1];
        Color.colorToHSV(i, tempHsv);
        tempHsv[0] = f;
        tempHsv[1] = AndroidUtilities.lerp(tempHsv[1], f2, 0.25f);
        return Color.HSVToColor(Color.alpha(i), tempHsv);
    }

    public static int adaptHSV(int i, float f, float f2) {
        float[] tempHsv = getTempHsv(5);
        Color.colorToHSV(i, tempHsv);
        tempHsv[1] = MathUtils.clamp(tempHsv[1] + f, 0.0f, 1.0f);
        tempHsv[2] = MathUtils.clamp(tempHsv[2] + f2, 0.0f, 1.0f);
        return Color.HSVToColor(Color.alpha(i), tempHsv);
    }

    public static int percentSV(int i, int i2, float f, float f2) {
        float[] tempHsv = getTempHsv(5);
        Color.colorToHSV(i2, tempHsv);
        float f3 = tempHsv[1];
        float f4 = tempHsv[2];
        Color.colorToHSV(i, tempHsv);
        tempHsv[1] = MathUtils.clamp(AndroidUtilities.lerp(tempHsv[1], f3, f), 0.0f, 1.0f);
        tempHsv[2] = MathUtils.clamp(AndroidUtilities.lerp(tempHsv[2], f4, f2), 0.0f, 1.0f);
        return Color.HSVToColor(AndroidUtilities.lerp(Color.alpha(i), Color.alpha(i2), 0.85f), tempHsv);
    }

    public static int multAlpha(int i, float f) {
        return ColorUtils.setAlphaComponent(i, MathUtils.clamp((int) (Color.alpha(i) * f), 0, 255));
    }

    /* loaded from: classes3.dex */
    public static class OverrideWallpaperInfo {
        public long accessHash;
        public int color;
        public long dialogId;
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
        public TLRPC$WallPaper prevUserWallpaper;
        public ArrayList<Integer> requestIds;
        public int rotation;
        public String slug;
        public float uploadingProgress;
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
                if (themeAccent != null || themeInfo.overrideWallpaper == this) {
                    if (themeAccent == null || themeAccent.overrideWallpaper == this) {
                        save();
                    }
                }
            }
        }

        private String getKey() {
            if (this.parentAccent != null) {
                return this.parentTheme.name + "_" + this.parentAccent.id + "_owp";
            }
            return this.parentTheme.name + "_owp";
        }

        /* JADX INFO: Access modifiers changed from: private */
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

        /* JADX INFO: Access modifiers changed from: private */
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

        /* JADX INFO: Access modifiers changed from: private */
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
                if (jSONObject.has("wallId") && jSONObject.getLong("wallId") == 1000001) {
                    overrideWallpaperInfo.slug = "d";
                }
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
            if (accent != null) {
                accent.overrideWallpaper = overrideWallpaperInfo;
            }
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
            if (this.firstAccentIsDefault && this.currentAccentId == Theme.DEFALT_THEME_ACCENT_ID) {
                return -1;
            }
            return this.previewInColor;
        }

        public int getPreviewOutColor() {
            if (this.firstAccentIsDefault && this.currentAccentId == Theme.DEFALT_THEME_ACCENT_ID) {
                return -983328;
            }
            return this.previewOutColor;
        }

        public int getPreviewBackgroundColor() {
            if (this.firstAccentIsDefault && this.currentAccentId == Theme.DEFALT_THEME_ACCENT_ID) {
                return -3155485;
            }
            return this.previewBackgroundColor;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isDefaultMyMessagesBubbles() {
            if (this.firstAccentIsDefault) {
                int i = this.currentAccentId;
                int i2 = Theme.DEFALT_THEME_ACCENT_ID;
                if (i == i2) {
                    return true;
                }
                ThemeAccent themeAccent = this.themeAccentsMap.get(i2);
                ThemeAccent themeAccent2 = this.themeAccentsMap.get(this.currentAccentId);
                return themeAccent != null && themeAccent2 != null && themeAccent.myMessagesAccentColor == themeAccent2.myMessagesAccentColor && themeAccent.myMessagesGradientAccentColor1 == themeAccent2.myMessagesGradientAccentColor1 && themeAccent.myMessagesGradientAccentColor2 == themeAccent2.myMessagesGradientAccentColor2 && themeAccent.myMessagesGradientAccentColor3 == themeAccent2.myMessagesGradientAccentColor3 && themeAccent.myMessagesAnimated == themeAccent2.myMessagesAnimated;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isDefaultMyMessages() {
            if (this.firstAccentIsDefault) {
                int i = this.currentAccentId;
                int i2 = Theme.DEFALT_THEME_ACCENT_ID;
                if (i == i2) {
                    return true;
                }
                ThemeAccent themeAccent = this.themeAccentsMap.get(i2);
                ThemeAccent themeAccent2 = this.themeAccentsMap.get(this.currentAccentId);
                return themeAccent != null && themeAccent2 != null && themeAccent.accentColor2 == themeAccent2.accentColor2 && themeAccent.myMessagesAccentColor == themeAccent2.myMessagesAccentColor && themeAccent.myMessagesGradientAccentColor1 == themeAccent2.myMessagesGradientAccentColor1 && themeAccent.myMessagesGradientAccentColor2 == themeAccent2.myMessagesGradientAccentColor2 && themeAccent.myMessagesGradientAccentColor3 == themeAccent2.myMessagesGradientAccentColor3 && themeAccent.myMessagesAnimated == themeAccent2.myMessagesAnimated;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isDefaultMainAccent() {
            if (this.firstAccentIsDefault) {
                int i = this.currentAccentId;
                int i2 = Theme.DEFALT_THEME_ACCENT_ID;
                if (i == i2) {
                    return true;
                }
                ThemeAccent themeAccent = this.themeAccentsMap.get(i2);
                ThemeAccent themeAccent2 = this.themeAccentsMap.get(this.currentAccentId);
                return (themeAccent2 == null || themeAccent == null || themeAccent.accentColor != themeAccent2.accentColor) ? false : true;
            }
            return false;
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

        /* JADX INFO: Access modifiers changed from: private */
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
                if ((Theme.isHome(themeAccent) && this.name.equals("Dark Blue")) || this.name.equals("Night")) {
                    themeAccent.myMessagesAccentColor = -10128392;
                    themeAccent.myMessagesGradientAccentColor1 = -9026357;
                    themeAccent.myMessagesGradientAccentColor2 = -7845452;
                    themeAccent.myMessagesGradientAccentColor3 = -5811800;
                    if (this.name.equals("Night")) {
                        themeAccent.patternIntensity = -0.57f;
                        themeAccent.backgroundOverrideColor = -9666650L;
                        themeAccent.backgroundGradientOverrideColor1 = -13749173L;
                        themeAccent.backgroundGradientOverrideColor2 = -8883033L;
                        themeAccent.backgroundGradientOverrideColor3 = -13421992L;
                    }
                }
                this.themeAccentsMap.put(themeAccent.id, themeAccent);
                this.themeAccents.add(themeAccent);
            }
            this.accentBaseColor = this.themeAccentsMap.get(0).accentColor;
        }

        /* JADX INFO: Access modifiers changed from: private */
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

        /* JADX INFO: Access modifiers changed from: private */
        public void removeObservers() {
            NotificationCenter.getInstance(this.account).removeObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.account).removeObserver(this, NotificationCenter.fileLoadFailed);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onFinishLoadingRemoteTheme() {
            this.loaded = true;
            this.previewParsed = false;
            Theme.saveOtherThemes(true);
            if (this == Theme.currentTheme && Theme.previousTheme == null) {
                NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
                int i = NotificationCenter.needSetDayNightTheme;
                Object[] objArr = new Object[5];
                objArr[0] = this;
                objArr[1] = Boolean.valueOf(this == Theme.currentNightTheme);
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
            String str;
            float f;
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
                j = 0;
                j2 = 0;
                i = 0;
                str = null;
                f = 0.0f;
                i2 = 0;
            } else {
                i2 = Theme.getWallpaperColor(tLRPC$WallPaperSettings.background_color);
                long wallpaperColor = tLRPC$ThemeSettings.wallpaper.settings.second_background_color == 0 ? 4294967296L : Theme.getWallpaperColor(i3);
                long wallpaperColor2 = tLRPC$ThemeSettings.wallpaper.settings.third_background_color == 0 ? 4294967296L : Theme.getWallpaperColor(i4);
                long wallpaperColor3 = tLRPC$ThemeSettings.wallpaper.settings.fourth_background_color == 0 ? 4294967296L : Theme.getWallpaperColor(i5);
                int wallpaperRotation = AndroidUtilities.getWallpaperRotation(tLRPC$ThemeSettings.wallpaper.settings.rotation, false);
                TLRPC$WallPaper tLRPC$WallPaper2 = tLRPC$ThemeSettings.wallpaper;
                if ((tLRPC$WallPaper2 instanceof TLRPC$TL_wallPaperNoFile) || !tLRPC$WallPaper2.pattern) {
                    i = wallpaperRotation;
                    j3 = wallpaperColor2;
                    str = null;
                    f = 0.0f;
                } else {
                    j3 = wallpaperColor2;
                    f = tLRPC$WallPaper2.settings.intensity / 100.0f;
                    str = tLRPC$WallPaper2.slug;
                    i = wallpaperRotation;
                }
                long j5 = wallpaperColor3;
                j4 = wallpaperColor;
                j = j3;
                j2 = j5;
            }
            return tLRPC$ThemeSettings.accent_color == themeAccent.accentColor && tLRPC$ThemeSettings.outbox_accent_color == themeAccent.accentColor2 && intValue == themeAccent.myMessagesAccentColor && intValue2 == themeAccent.myMessagesGradientAccentColor1 && intValue3 == themeAccent.myMessagesGradientAccentColor2 && intValue4 == themeAccent.myMessagesGradientAccentColor3 && tLRPC$ThemeSettings.message_colors_animated == themeAccent.myMessagesAnimated && ((long) i2) == themeAccent.backgroundOverrideColor && j4 == themeAccent.backgroundGradientOverrideColor1 && j == themeAccent.backgroundGradientOverrideColor2 && j2 == themeAccent.backgroundGradientOverrideColor3 && i == themeAccent.backgroundRotation && TextUtils.equals(str, themeAccent.patternSlug) && ((double) Math.abs(f - themeAccent.patternIntensity)) < 0.001d;
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
            if (tLRPC$TL_theme == null) {
                return null;
            }
            TLRPC$ThemeSettings tLRPC$ThemeSettings = i2 < tLRPC$TL_theme.settings.size() ? tLRPC$TL_theme.settings.get(i2) : null;
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
            if (z) {
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
            return themeAccent;
        }

        public int getAccentColor(int i) {
            ThemeAccent themeAccent = this.themeAccentsMap.get(i);
            if (themeAccent != null) {
                return themeAccent.accentColor;
            }
            return 0;
        }

        public boolean createBackground(File file, String str) {
            int patternColor;
            try {
                Bitmap scaledBitmap = AndroidUtilities.getScaledBitmap(AndroidUtilities.dp(640.0f), AndroidUtilities.dp(360.0f), file.getAbsolutePath(), null, 0);
                if (scaledBitmap != null && this.patternBgColor != 0) {
                    Bitmap createBitmap = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), scaledBitmap.getConfig());
                    Canvas canvas = new Canvas(createBitmap);
                    int i = this.patternBgGradientColor2;
                    if (i != 0) {
                        patternColor = MotionBackgroundDrawable.getPatternColor(this.patternBgColor, this.patternBgGradientColor1, i, this.patternBgGradientColor3);
                    } else {
                        int i2 = this.patternBgGradientColor1;
                        if (i2 != 0) {
                            patternColor = AndroidUtilities.getAverageColor(this.patternBgColor, i2);
                            GradientDrawable gradientDrawable = new GradientDrawable(BackgroundGradientDrawable.getGradientOrientation(this.patternBgGradientRotation), new int[]{this.patternBgColor, this.patternBgGradientColor1});
                            gradientDrawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                            gradientDrawable.draw(canvas);
                        } else {
                            patternColor = AndroidUtilities.getPatternColor(this.patternBgColor);
                            canvas.drawColor(this.patternBgColor);
                        }
                    }
                    Paint paint = new Paint(2);
                    paint.setColorFilter(new PorterDuffColorFilter(patternColor, PorterDuff.Mode.SRC_IN));
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
                } else if (str.equals(FileLoader.getAttachFileName(this.info.document))) {
                    removeObservers();
                    if (i == i3) {
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
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didReceivedNotification$0(File file) {
            createBackground(file, this.newPathToWallpaper);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$ThemeInfo$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Theme.ThemeInfo.this.onFinishLoadingRemoteTheme();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didReceivedNotification$2(final ThemeInfo themeInfo, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$ThemeInfo$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    Theme.ThemeInfo.this.lambda$didReceivedNotification$1(tLObject, themeInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
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

        boolean contains(int i);

        int getColor(int i);

        int getColorOrDefault(int i);

        int getCurrentColor(int i);

        Drawable getDrawable(String str);

        Paint getPaint(String str);

        boolean hasGradientService();

        void setAnimatedColor(int i, int i2);

        /* loaded from: classes3.dex */
        public final /* synthetic */ class -CC {
            public static Drawable $default$getDrawable(ResourcesProvider resourcesProvider, String str) {
                return null;
            }

            public static Paint $default$getPaint(ResourcesProvider resourcesProvider, String str) {
                return null;
            }

            public static boolean $default$hasGradientService(ResourcesProvider resourcesProvider) {
                return false;
            }

            public static void $default$setAnimatedColor(ResourcesProvider resourcesProvider, int i, int i2) {
            }

            public static int $default$getColorOrDefault(ResourcesProvider _this, int i) {
                if (_this.contains(i)) {
                    return _this.getColor(i);
                }
                return Theme.getColor(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sortAccents(ThemeInfo themeInfo) {
        Collections.sort(themeInfo.themeAccents, Theme$$ExternalSyntheticLambda10.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    /* JADX INFO: Access modifiers changed from: private */
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
        return createEditTextDrawable(context, getColor(z ? key_dialogInputField : key_windowBackgroundWhiteInputField), getColor(z ? key_dialogInputFieldActivated : key_windowBackgroundWhiteInputFieldActivated));
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
            if (i != 9 || i2 < 30) {
                return (i == 10 && i2 == 1 && i3 < 12) ? 2 : -1;
            }
            return 2;
        }
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0056, code lost:
        if (r2 <= 31) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x005a, code lost:
        if (r2 == 1) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x005c, code lost:
        org.telegram.ui.ActionBar.Theme.dialogs_holidayDrawable = org.telegram.messenger.ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.newyear);
        org.telegram.ui.ActionBar.Theme.dialogs_holidayDrawableOffsetX = -org.telegram.messenger.AndroidUtilities.dp(3.0f);
        org.telegram.ui.ActionBar.Theme.dialogs_holidayDrawableOffsetY = -org.telegram.messenger.AndroidUtilities.dp(-7.0f);
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
        Drawable background;
        if (drawable instanceof CombinedDrawable) {
            if (z) {
                background = ((CombinedDrawable) drawable).getIcon();
            } else {
                background = ((CombinedDrawable) drawable).getBackground();
            }
            if (background instanceof ColorDrawable) {
                ((ColorDrawable) background).setColor(i);
            } else {
                background.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
            }
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
        return getSelectorDrawable(getColor(key_listSelector), z);
    }

    public static Drawable getSelectorDrawable(int i, boolean z) {
        if (z) {
            return getSelectorDrawable(i, key_windowBackgroundWhite);
        }
        return createSelectorDrawable(i, 2);
    }

    public static Drawable getSelectorDrawable(int i, int i2) {
        if (i2 >= 0) {
            if (Build.VERSION.SDK_INT >= 21) {
                return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), new ColorDrawable(getColor(i2)), new ColorDrawable(-1));
            }
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{16842919}, new ColorDrawable(i));
            stateListDrawable.addState(new int[]{16842913}, new ColorDrawable(i));
            stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(getColor(i2)));
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

    /* JADX WARN: Removed duplicated region for block: B:26:0x004f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Drawable createSelectorDrawable(int i, final int i2, final int i3) {
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
                                    i5 = i3;
                                    if (i5 <= 0) {
                                        i5 = AndroidUtilities.dp(20.0f);
                                    }
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
                            int i7 = i3;
                            if (i7 <= 0) {
                                i7 = AndroidUtilities.dp(6.0f);
                            }
                            float f = i7;
                            canvas.drawRoundRect(this.rect, f, f, Theme.maskPaint);
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
        private static final int defaultBackgroundColorKey = Theme.key_windowBackgroundWhite;
        private static float[] tempHSV;

        public static Drawable circle(int i) {
            return circle(i, -1.0f);
        }

        public static Drawable circle(int i, float f) {
            return createCircle(calcRippleColor(i), f);
        }

        public static Drawable rect() {
            return rect(Theme.getColor(defaultBackgroundColorKey));
        }

        public static Drawable rectByKey(int i, float... fArr) {
            return rect(Theme.getColor(i), fArr);
        }

        public static Drawable rect(int i) {
            return rect(i, 0.0f);
        }

        public static Drawable rect(int i, float... fArr) {
            return createRect(0, calcRippleColor(i), fArr);
        }

        public static Drawable filledRect() {
            return filledRect(Theme.getColor(defaultBackgroundColorKey), 0.0f);
        }

        public static Drawable filledRectByKey(int i, float... fArr) {
            return filledRect(Theme.getColor(i), fArr);
        }

        public static Drawable filledRect(int i, float... fArr) {
            return createRect(i, calcRippleColor(i), fArr);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static Drawable createRect(int i, int i2, float... fArr) {
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

        private static Drawable createRect(Drawable drawable, int i, float... fArr) {
            ShapeDrawable shapeDrawable;
            ShapeDrawable shapeDrawable2;
            if (Build.VERSION.SDK_INT >= 21) {
                if (hasNonzeroRadii(fArr)) {
                    shapeDrawable2 = new ShapeDrawable(new RoundRectShape(calcRadii(fArr), null, null));
                    shapeDrawable2.getPaint().setColor(-1);
                } else {
                    shapeDrawable2 = new ShapeDrawable(new RectShape());
                    shapeDrawable2.getPaint().setColor(-1);
                }
                return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), drawable, shapeDrawable2);
            }
            StateListDrawable stateListDrawable = new StateListDrawable();
            if (hasNonzeroRadii(fArr)) {
                shapeDrawable = new ShapeDrawable(new RoundRectShape(calcRadii(fArr), null, null));
                shapeDrawable.getPaint().setColor(i);
            } else {
                shapeDrawable = new ShapeDrawable(new RectShape());
                shapeDrawable.getPaint().setColor(i);
            }
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawable, shapeDrawable});
            stateListDrawable.addState(new int[]{16842919}, layerDrawable);
            stateListDrawable.addState(new int[]{16842913}, layerDrawable);
            stateListDrawable.addState(StateSet.WILD_CARD, drawable);
            return stateListDrawable;
        }

        private static Drawable createCircle(int i, float f) {
            return createCircle(0, i, f);
        }

        private static Drawable createCircle(int i, int i2, float f) {
            return createCircle(i == 0 ? null : new CircleDrawable(f, i), i2, f);
        }

        private static Drawable createCircle(Drawable drawable, int i, float f) {
            if (Build.VERSION.SDK_INT >= 21) {
                return new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), drawable, new CircleDrawable(f));
            }
            StateListDrawable stateListDrawable = new StateListDrawable();
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawable, new CircleDrawable(f, i)});
            stateListDrawable.addState(new int[]{16842919}, layerDrawable);
            stateListDrawable.addState(new int[]{16842913}, layerDrawable);
            stateListDrawable.addState(StateSet.WILD_CARD, drawable);
            return stateListDrawable;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class CircleDrawable extends Drawable {
            private static Paint maskPaint;
            private Paint paint;
            private float radius;

            @Override // android.graphics.drawable.Drawable
            @Deprecated
            public int getOpacity() {
                return -2;
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int i) {
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }

            public CircleDrawable(float f) {
                this.radius = f;
                if (maskPaint == null) {
                    Paint paint = new Paint(1);
                    maskPaint = paint;
                    paint.setColor(-1);
                }
                this.paint = maskPaint;
            }

            public CircleDrawable(float f, int i) {
                this.radius = f;
                Paint paint = new Paint(1);
                this.paint = paint;
                paint.setColor(i);
            }

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                int dp;
                Rect bounds = getBounds();
                if (Math.abs(this.radius - (-1.0f)) < 0.01f) {
                    dp = Math.max(bounds.width(), bounds.height()) / 2;
                } else if (Math.abs(this.radius - (-2.0f)) < 0.01f) {
                    dp = (int) Math.ceil(Math.sqrt(((bounds.left - bounds.centerX()) * (bounds.left - bounds.centerX())) + ((bounds.top - bounds.centerY()) * (bounds.top - bounds.centerY()))));
                } else {
                    dp = AndroidUtilities.dp(this.radius);
                }
                canvas.drawCircle(bounds.centerX(), bounds.centerY(), dp, this.paint);
            }
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

        public static int calcRippleColor(int i) {
            if (tempHSV == null) {
                tempHSV = new float[3];
            }
            Color.colorToHSV(i, tempHSV);
            float[] fArr = tempHSV;
            if (fArr[1] > 0.01f) {
                fArr[1] = Math.min(1.0f, Math.max(0.0f, fArr[1] + (Theme.isCurrentThemeDark() ? 0.25f : -0.25f)));
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

        public void setRadius(float f, float f2) {
            float[] fArr = this.radii;
            float dp = AndroidUtilities.dp(f);
            fArr[3] = dp;
            fArr[2] = dp;
            fArr[1] = dp;
            fArr[0] = dp;
            float[] fArr2 = this.radii;
            float dp2 = AndroidUtilities.dp(f2);
            fArr2[7] = dp2;
            fArr2[6] = dp2;
            fArr2[5] = dp2;
            fArr2[4] = dp2;
            this.invalidatePath = true;
            invalidateSelf();
        }

        public void setRadius(float f, float f2, float f3, float f4) {
            float[] fArr = this.radii;
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
            this.invalidatePath = true;
            invalidateSelf();
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
                Drawable drawable2 = rippleDrawable.getDrawable(i3);
                if (drawable2 instanceof RippleRadMaskDrawable) {
                    ((RippleRadMaskDrawable) drawable2).setRadius(i, i2);
                    return;
                }
            }
        }
    }

    public static void setMaskDrawableRad(Drawable drawable, float f, float f2, float f3, float f4) {
        if (Build.VERSION.SDK_INT >= 21 && (drawable instanceof RippleDrawable)) {
            RippleDrawable rippleDrawable = (RippleDrawable) drawable;
            int numberOfLayers = rippleDrawable.getNumberOfLayers();
            for (int i = 0; i < numberOfLayers; i++) {
                Drawable drawable2 = rippleDrawable.getDrawable(i);
                if (drawable2 instanceof RippleRadMaskDrawable) {
                    ((RippleRadMaskDrawable) drawable2).setRadius(f, f2, f3, f4);
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
        Collections.sort(themes, Theme$$ExternalSyntheticLambda11.INSTANCE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$sortThemes$1(ThemeInfo themeInfo, ThemeInfo themeInfo2) {
        if (themeInfo.pathToFile == null && themeInfo.assetName == null) {
            return -1;
        }
        if (themeInfo2.pathToFile == null && themeInfo2.assetName == null) {
            return 1;
        }
        return themeInfo.name.compareTo(themeInfo2.name);
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
            reloadWallpaper(true);
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
        File file2;
        String str2;
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

    public static void applyThemeInBackground(ThemeInfo themeInfo, boolean z, Runnable runnable) {
        applyThemeInBackground(themeInfo, true, true, z, runnable);
    }

    public static void applyTheme(ThemeInfo themeInfo, boolean z, boolean z2) {
        applyTheme(themeInfo, z, true, z2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x003c, code lost:
        if (r8 == false) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x003e, code lost:
        r0 = org.telegram.messenger.MessagesController.getGlobalMainSettings().edit();
        r0.putString("theme", r7.getKey());
        r0.apply();
     */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01c8 A[Catch: Exception -> 0x01df, TryCatch #6 {Exception -> 0x01df, blocks: (B:8:0x000d, B:11:0x0014, B:16:0x001d, B:17:0x002b, B:81:0x01bc, B:83:0x01c0, B:85:0x01c8, B:86:0x01d9, B:20:0x003e, B:21:0x0050, B:23:0x0057, B:25:0x006b, B:27:0x007e, B:33:0x00ba, B:79:0x01b6, B:24:0x005e, B:34:0x00bc, B:36:0x00d2, B:38:0x00de, B:41:0x00e2, B:43:0x00e5, B:45:0x00ef, B:49:0x00fe, B:46:0x00f2, B:48:0x00fc, B:50:0x0101, B:51:0x0112, B:53:0x011e, B:55:0x0136, B:57:0x0140, B:58:0x014c, B:60:0x0154, B:62:0x015e, B:63:0x016b, B:65:0x0173, B:67:0x017d, B:68:0x018a, B:70:0x0196), top: B:108:0x000d }] */
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
                edit.commit();
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
                    AndroidUtilities.runOnUIThread(MessagesController$$ExternalSyntheticLambda242.INSTANCE, 2100L);
                }
            }
            currentTheme = themeInfo;
            refreshThemeColors();
            if (previousTheme == null || !z || switchingNightTheme) {
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
        themedWallpaperFileOffset = currentColorsNoAccent.get(key_wallpaperFileOffset, -1);
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
        if (previousTheme == null) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x0064, code lost:
        if (r10 == false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0066, code lost:
        r0 = org.telegram.messenger.MessagesController.getGlobalMainSettings().edit();
        r0.putString("theme", r9.getKey());
        r0.apply();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static void applyThemeInBackground(final ThemeInfo themeInfo, final boolean z, boolean z2, final boolean z3, final Runnable runnable) {
        if (themeInfo == null) {
            if (runnable != null) {
                runnable.run();
                return;
            }
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
                    AndroidUtilities.runOnUIThread(MessagesController$$ExternalSyntheticLambda242.INSTANCE, 2100L);
                }
            }
            currentTheme = themeInfo;
            refreshThemeColors();
            if (previousTheme == null && z && !switchingNightTheme) {
                MessagesController.getInstance(themeInfo.account).saveTheme(themeInfo, themeInfo.getAccent(false), z3, false);
            }
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        final String[] strArr = new String[1];
        final Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$applyThemeInBackground$2(strArr, themeInfo, z3, z, runnable);
            }
        };
        String str = themeInfo.assetName;
        if (str != null) {
            getThemeFileValuesInBackground(null, str, null, new Utilities.Callback() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda13
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    Theme.lambda$applyThemeInBackground$3(runnable2, (SparseIntArray) obj);
                }
            });
        } else {
            getThemeFileValuesInBackground(new File(themeInfo.pathToFile), null, strArr, new Utilities.Callback() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda12
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    Theme.lambda$applyThemeInBackground$4(runnable2, (SparseIntArray) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$applyThemeInBackground$2(String[] strArr, ThemeInfo themeInfo, boolean z, boolean z2, Runnable runnable) {
        String[] split;
        try {
            themedWallpaperFileOffset = currentColorsNoAccent.get(key_wallpaperFileOffset, -1);
            if (!TextUtils.isEmpty(strArr[0])) {
                themedWallpaperLink = strArr[0];
                File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                String absolutePath = new File(filesDirFixed, Utilities.MD5(themedWallpaperLink) + ".wp").getAbsolutePath();
                try {
                    String str = themeInfo.pathToWallpaper;
                    if (str != null && !str.equals(absolutePath)) {
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
            if (!z && previousTheme == null) {
                currentDayTheme = themeInfo;
                if (isCurrentThemeNight()) {
                    switchNightThemeDelay = 2000;
                    lastDelayUpdateTime = SystemClock.elapsedRealtime();
                    AndroidUtilities.runOnUIThread(MessagesController$$ExternalSyntheticLambda242.INSTANCE, 2100L);
                }
            }
            currentTheme = themeInfo;
            refreshThemeColors();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (previousTheme == null && z2 && !switchingNightTheme) {
            MessagesController.getInstance(themeInfo.account).saveTheme(themeInfo, themeInfo.getAccent(false), z, false);
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$applyThemeInBackground$3(Runnable runnable, SparseIntArray sparseIntArray) {
        currentColorsNoAccent = sparseIntArray;
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$applyThemeInBackground$4(Runnable runnable, SparseIntArray sparseIntArray) {
        currentColorsNoAccent = sparseIntArray;
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
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
        currentColors = currentColorsNoAccent.clone();
        shouldDrawGradientIcons = true;
        ThemeAccent accent = currentTheme.getAccent(false);
        if (accent != null) {
            shouldDrawGradientIcons = accent.fillAccentColors(currentColorsNoAccent, currentColors);
        }
        if (!z2) {
            reloadWallpaper(!(LaunchActivity.getLastFragment() instanceof ChatActivity));
        }
        applyCommonTheme();
        applyDialogsTheme();
        applyProfileTheme();
        applyChatTheme(false, z);
        final boolean z3 = !hasPreviousTheme;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$refreshThemeColors$5(z3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$refreshThemeColors$5(boolean z) {
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

    /* JADX INFO: Access modifiers changed from: private */
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

    /* JADX INFO: Access modifiers changed from: private */
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
        ThemeAccent accent = currentTheme.getAccent(false);
        return changeColorAccent(currentTheme, accent != null ? accent.accentColor : 0, i);
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
        return Color.argb(Color.alpha(i), red < 0 ? 0 : Math.min(red, 255), green < 0 ? 0 : Math.min(green, 255), blue >= 0 ? Math.min(blue, 255) : 0);
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

    /* JADX INFO: Access modifiers changed from: private */
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
            sb.append(i3 != 0 ? Integer.valueOf(i3) : "");
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

    public static int[] getDefaultColors() {
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

    /* JADX INFO: Access modifiers changed from: private */
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
            if (lightSensorRegistered) {
                lastBrightnessValue = 1.0f;
                sensorManager.unregisterListener(ambientSensorListener, lightSensor);
                lightSensorRegistered = false;
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("light sensor unregistered");
                }
            }
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
                i = autoNightSunsetTime;
                i2 = autoNightSunriseTime;
            } else {
                i = autoNightDayStartTime;
                i2 = autoNightDayEndTime;
            }
            return i < i2 ? (i > i4 || i4 > i2) ? 1 : 2 : ((i > i4 || i4 > 1440) && (i4 < 0 || i4 > i2)) ? 1 : 2;
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
        if (z) {
            return;
        }
        checkAutoNightThemeConditions(false);
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
            applyDayNightThemeMaybe(needSwitchToTheme == 2);
        }
        if (z) {
            lastThemeSwitchTime = 0L;
        }
    }

    public static void applyDayNightThemeMaybe(boolean z) {
        if (previousTheme != null) {
            return;
        }
        if (z) {
            ThemeInfo themeInfo = currentTheme;
            ThemeInfo themeInfo2 = currentNightTheme;
            if (themeInfo != themeInfo2) {
                if (themeInfo == null || !(themeInfo2 == null || themeInfo.isDark() == currentNightTheme.isDark())) {
                    isInNigthMode = true;
                    lastThemeSwitchTime = SystemClock.elapsedRealtime();
                    switchingNightTheme = true;
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, currentNightTheme, Boolean.TRUE, null, -1);
                    switchingNightTheme = false;
                    return;
                }
                return;
            }
            return;
        }
        ThemeInfo themeInfo3 = currentTheme;
        ThemeInfo themeInfo4 = currentDayTheme;
        if (themeInfo3 != themeInfo4) {
            if (themeInfo3 == null || !(themeInfo4 == null || themeInfo3.isLight() == currentDayTheme.isLight())) {
                isInNigthMode = false;
                lastThemeSwitchTime = SystemClock.elapsedRealtime();
                switchingNightTheme = true;
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needSetDayNightTheme, currentDayTheme, Boolean.TRUE, null, -1);
                switchingNightTheme = false;
            }
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
            String lowerCase4 = i4 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i4 >> 16)) & 255), Integer.valueOf(((byte) (overrideWallpaperInfo.gradientColor3 >> 8)) & 255), Byte.valueOf((byte) (overrideWallpaperInfo.gradientColor3 & 255))).toLowerCase() : null;
            if (lowerCase2 == null || lowerCase3 == null) {
                if (lowerCase2 != null) {
                    lowerCase = (lowerCase + "-" + lowerCase2) + "&rotation=" + overrideWallpaperInfo.rotation;
                }
            } else if (lowerCase4 != null) {
                lowerCase = lowerCase + "~" + lowerCase2 + "~" + lowerCase3 + "~" + lowerCase4;
            } else {
                lowerCase = lowerCase + "~" + lowerCase2 + "~" + lowerCase3;
            }
            str = "https://attheme.org?slug=" + overrideWallpaperInfo.slug + "&intensity=" + ((int) (overrideWallpaperInfo.intensity * 100.0f)) + "&bg_color=" + lowerCase;
        }
        if (sb.length() > 0) {
            return str + "&mode=" + sb.toString();
        }
        return str;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(12:(12:47|(2:48|(3:50|(2:53|54)|55)(0))|69|71|72|(1:78)|79|(2:81|(2:102|103))(2:107|(3:109|(1:111)|(1:114)))|(6:84|(1:86)|87|(1:89)|(1:91)|92)|94|95|(2:97|98)(1:100))(2:135|(4:138|(2:141|142)|143|136))|68|69|71|72|(3:74|76|78)|79|(0)(0)|(0)|94|95|(0)(0)) */
    /* JADX WARN: Can't wrap try/catch for region: R(24:1|(1:3)(1:159)|(1:5)(1:158)|(1:8)|9|(1:157)(1:13)|14|(1:16)(1:156)|17|(5:(1:20)(1:44)|(1:22)(1:43)|(1:24)(1:42)|(1:26)(1:41)|(5:29|(2:31|(1:33))|34|(1:40)(1:38)|39))|45|(12:47|(2:48|(3:50|(2:53|54)|55)(0))|69|71|72|(1:78)|79|(2:81|(2:102|103))(2:107|(3:109|(1:111)|(1:114)))|(6:84|(1:86)|87|(1:89)|(1:91)|92)|94|95|(2:97|98)(1:100))(2:135|(4:138|(2:141|142)|143|136))|68|69|71|72|(3:74|76|78)|79|(0)(0)|(0)|94|95|(0)(0)|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x020c, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x020d, code lost:
        r1 = r0;
        r7 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x0210, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x0211, code lost:
        r7 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x0216, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x0217, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x021a, code lost:
        if (r7 != null) goto L119;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x021c, code lost:
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x0220, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x0221, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0235, code lost:
        if (r7 != null) goto L125;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x0237, code lost:
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x023b, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x023c, code lost:
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0241, code lost:
        throw r1;
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0176 A[Catch: all -> 0x020c, Exception -> 0x0210, TryCatch #7 {Exception -> 0x0210, all -> 0x020c, blocks: (B:87:0x00fa, B:89:0x0100, B:91:0x0104, B:93:0x010a, B:94:0x010f, B:96:0x0122, B:111:0x01bb, B:113:0x01c7, B:114:0x01e0, B:116:0x01e6, B:118:0x01ea, B:119:0x01f2, B:101:0x0172, B:102:0x0176, B:104:0x017a, B:106:0x0184, B:109:0x01b4), top: B:155:0x00fa }] */
    /* JADX WARN: Removed duplicated region for block: B:111:0x01bb A[Catch: all -> 0x020c, Exception -> 0x0210, TryCatch #7 {Exception -> 0x0210, all -> 0x020c, blocks: (B:87:0x00fa, B:89:0x0100, B:91:0x0104, B:93:0x010a, B:94:0x010f, B:96:0x0122, B:111:0x01bb, B:113:0x01c7, B:114:0x01e0, B:116:0x01e6, B:118:0x01ea, B:119:0x01f2, B:101:0x0172, B:102:0x0176, B:104:0x017a, B:106:0x0184, B:109:0x01b4), top: B:155:0x00fa }] */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0227  */
    /* JADX WARN: Removed duplicated region for block: B:167:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0122 A[Catch: all -> 0x020c, Exception -> 0x0210, TRY_LEAVE, TryCatch #7 {Exception -> 0x0210, all -> 0x020c, blocks: (B:87:0x00fa, B:89:0x0100, B:91:0x0104, B:93:0x010a, B:94:0x010f, B:96:0x0122, B:111:0x01bb, B:113:0x01c7, B:114:0x01e0, B:116:0x01e6, B:118:0x01ea, B:119:0x01f2, B:101:0x0172, B:102:0x0176, B:104:0x017a, B:106:0x0184, B:109:0x01b4), top: B:155:0x00fa }] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:134:0x0221 -> B:146:0x0225). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void saveCurrentTheme(ThemeInfo themeInfo, boolean z, boolean z2, boolean z3) {
        String str;
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
        boolean z4 = currentTheme.firstAccentIsDefault && accent.id == DEFALT_THEME_ACCENT_ID;
        FileOutputStream fileOutputStream = null;
        SparseIntArray sparseIntArray = z4 ? null : currentColors;
        StringBuilder sb = new StringBuilder();
        if (!z4) {
            int i = accent != null ? accent.myMessagesAccentColor : 0;
            int i2 = accent != null ? accent.myMessagesGradientAccentColor1 : 0;
            int i3 = accent != null ? accent.myMessagesGradientAccentColor2 : 0;
            int i4 = accent != null ? accent.myMessagesGradientAccentColor3 : 0;
            if (i != 0 && i2 != 0) {
                sparseIntArray.put(key_chat_outBubble, i);
                sparseIntArray.put(key_chat_outBubbleGradient1, i2);
                if (i3 != 0) {
                    sparseIntArray.put(key_chat_outBubbleGradient2, i3);
                    if (i4 != 0) {
                        sparseIntArray.put(key_chat_outBubbleGradient3, i4);
                    }
                }
                sparseIntArray.put(key_chat_outBubbleGradientAnimated, (accent == null || !accent.myMessagesAnimated) ? 0 : 1);
            }
        }
        try {
            if (!z4) {
                for (int i5 = 0; i5 < sparseIntArray.size(); i5++) {
                    int keyAt = sparseIntArray.keyAt(i5);
                    int valueAt = sparseIntArray.valueAt(i5);
                    if ((!(drawable instanceof BitmapDrawable) && str == null) || (key_chat_wallpaper != keyAt && key_chat_wallpaper_gradient_to1 != keyAt && key_chat_wallpaper_gradient_to2 != keyAt && key_chat_wallpaper_gradient_to3 != keyAt)) {
                        sb.append(ThemeColors.getStringName(keyAt));
                        sb.append("=");
                        sb.append(valueAt);
                        sb.append("\n");
                    }
                }
            } else {
                int i6 = 0;
                while (true) {
                    int[] iArr = defaultColors;
                    if (i6 < iArr.length) {
                        int i7 = iArr[i6];
                        if ((!(drawable instanceof BitmapDrawable) && str == null) || (key_chat_wallpaper != i6 && key_chat_wallpaper_gradient_to1 != i6 && key_chat_wallpaper_gradient_to2 != i6 && key_chat_wallpaper_gradient_to3 != i6)) {
                            sb.append(ThemeColors.getStringName(i6));
                            sb.append("=");
                            sb.append(i7);
                            sb.append("\n");
                        }
                        i6++;
                    }
                }
                FileOutputStream fileOutputStream2 = new FileOutputStream(themeInfo.pathToFile);
                if (sb.length() == 0 && !(drawable instanceof BitmapDrawable) && TextUtils.isEmpty(str)) {
                    sb.append(' ');
                }
                fileOutputStream2.write(AndroidUtilities.getStringBytes(sb.toString()));
                if (TextUtils.isEmpty(str)) {
                    fileOutputStream2.write(AndroidUtilities.getStringBytes("WLS=" + str + "\n"));
                    if (z2) {
                        try {
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                            FileOutputStream fileOutputStream3 = new FileOutputStream(new File(filesDirFixed, Utilities.MD5(str) + ".wp"));
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream3);
                            fileOutputStream3.close();
                        } catch (Throwable th) {
                            FileLog.e(th);
                        }
                    }
                } else if (drawable instanceof BitmapDrawable) {
                    Bitmap bitmap2 = ((BitmapDrawable) drawable).getBitmap();
                    if (bitmap2 != null) {
                        fileOutputStream2.write(new byte[]{87, 80, 83, 10});
                        bitmap2.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream2);
                        fileOutputStream2.write(new byte[]{10, 87, 80, 69, 10});
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
                    if (z4) {
                        currentColorsNoAccent.clear();
                        refreshThemeColors();
                    }
                    SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
                    edit.putString("theme", currentDayTheme.getKey());
                    edit.apply();
                }
                fileOutputStream2.close();
                if (z) {
                    MessagesController.getInstance(themeInfo.account).saveThemeToServer(themeInfo, themeInfo.getAccent(false));
                    return;
                }
                return;
            }
            FileOutputStream fileOutputStream22 = new FileOutputStream(themeInfo.pathToFile);
            if (sb.length() == 0) {
                sb.append(' ');
            }
            fileOutputStream22.write(AndroidUtilities.getStringBytes(sb.toString()));
            if (TextUtils.isEmpty(str)) {
            }
            if (!z3) {
            }
            fileOutputStream22.close();
            if (z) {
            }
        } catch (Throwable th2) {
            Throwable th3 = th2;
        }
    }

    public static void checkCurrentRemoteTheme(boolean z) {
        int i;
        if (loadingCurrentTheme == 0) {
            if (z || Math.abs((System.currentTimeMillis() / 1000) - lastLoadingCurrentThemeTime) >= 3600) {
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
                            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_getTheme, new RequestDelegate() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda15
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    Theme.lambda$checkCurrentRemoteTheme$7(Theme.ThemeAccent.this, themeInfo, tLRPC$TL_theme, tLObject, tLRPC$TL_error);
                                }
                            });
                        }
                    }
                    i2++;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkCurrentRemoteTheme$7(final ThemeAccent themeAccent, final ThemeInfo themeInfo, final TLRPC$TL_theme tLRPC$TL_theme, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$checkCurrentRemoteTheme$6(TLObject.this, themeAccent, themeInfo, tLRPC$TL_theme);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:47:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$checkCurrentRemoteTheme$6(TLObject tLObject, ThemeAccent themeAccent, ThemeInfo themeInfo, TLRPC$TL_theme tLRPC$TL_theme) {
        boolean z;
        TLRPC$WallPaperSettings tLRPC$WallPaperSettings;
        boolean z2 = true;
        loadingCurrentTheme--;
        if (tLObject instanceof TLRPC$TL_theme) {
            TLRPC$TL_theme tLRPC$TL_theme2 = (TLRPC$TL_theme) tLObject;
            TLRPC$ThemeSettings tLRPC$ThemeSettings = tLRPC$TL_theme2.settings.size() > 0 ? tLRPC$TL_theme2.settings.get(0) : null;
            if (themeAccent != null && tLRPC$ThemeSettings != null) {
                if (ThemeInfo.accentEquals(themeAccent, tLRPC$ThemeSettings)) {
                    z = false;
                } else {
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
                }
                TLRPC$WallPaper tLRPC$WallPaper = tLRPC$ThemeSettings.wallpaper;
                themeAccent.patternMotion = (tLRPC$WallPaper == null || (tLRPC$WallPaperSettings = tLRPC$WallPaper.settings) == null || !tLRPC$WallPaperSettings.motion) ? false : false;
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
            if (loadingCurrentTheme != 0) {
                lastLoadingCurrentThemeTime = (int) (System.currentTimeMillis() / 1000);
                saveOtherThemes(z2);
                return;
            }
            return;
        }
        z2 = false;
        if (loadingCurrentTheme != 0) {
        }
    }

    public static void loadRemoteThemes(final int i, boolean z) {
        if (loadingRemoteThemes[i]) {
            return;
        }
        if ((z || Math.abs((System.currentTimeMillis() / 1000) - lastLoadingThemesTime[i]) >= 3600) && UserConfig.getInstance(i).isClientActivated()) {
            loadingRemoteThemes[i] = true;
            TLRPC$TL_account_getThemes tLRPC$TL_account_getThemes = new TLRPC$TL_account_getThemes();
            tLRPC$TL_account_getThemes.format = "android";
            if (!MediaDataController.getInstance(i).defaultEmojiThemes.isEmpty()) {
                tLRPC$TL_account_getThemes.hash = remoteThemesHash[i];
            }
            if (BuildVars.LOGS_ENABLED) {
                Log.i("theme", "loading remote themes, hash " + tLRPC$TL_account_getThemes.hash);
            }
            ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_account_getThemes, new RequestDelegate() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda14
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    Theme.lambda$loadRemoteThemes$9(i, tLObject, tLRPC$TL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadRemoteThemes$9(final int i, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$loadRemoteThemes$8(i, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:103:0x020d  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0219  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$loadRemoteThemes$8(int i, TLObject tLObject) {
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
                                    if (TextUtils.isEmpty(createNewAccent.patternSlug)) {
                                        themeAccent2 = createNewAccent;
                                    } else {
                                        themeAccent2 = createNewAccent;
                                        z2 = true;
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
        if (tLRPC$BaseTheme instanceof TLRPC$TL_baseThemeNight) {
            return "Night";
        }
        return null;
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
        if ("Night".equals(str)) {
            return new TLRPC$TL_baseThemeNight();
        }
        return null;
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

    public static int getPreviewColor(SparseIntArray sparseIntArray, int i) {
        int indexOfKey = sparseIntArray.indexOfKey(i);
        if (indexOfKey >= 0) {
            return sparseIntArray.valueAt(indexOfKey);
        }
        return defaultColors[i];
    }

    /* JADX WARN: Can't wrap try/catch for region: R(16:170|(4:171|172|173|(6:174|175|176|(3:230|231|(1:233))|178|179))|(4:181|182|183|(18:185|186|187|188|189|190|192|193|194|(4:196|197|198|(5:200|201|202|203|204)(1:213))(1:217)|205|206|40|(5:42|(1:44)(1:52)|45|(2:47|48)(2:50|51)|49)|53|54|55|(17:137|138|(2:142|(4:(1:144)|146|147|(16:149|(1:159)(3:152|153|154)|155|156|112|(1:64)(1:81)|65|(1:67)|68|(1:70)|71|(1:73)|(1:75)|76|77|78))(2:147|(0)))|162|156|112|(0)(0)|65|(0)|68|(0)|71|(0)|(0)|76|77|78)(2:57|(14:(1:60)(1:(1:83)(15:84|(1:86)|87|62|(0)(0)|65|(0)|68|(0)|71|(0)|(0)|76|77|78))|61|62|(0)(0)|65|(0)|68|(0)|71|(0)|(0)|76|77|78)(20:(1:89)|92|93|(1:95)(3:132|133|134)|96|(2:100|(5:(1:102)|104|105|(1:107)(1:130)|(15:109|110|(2:113|114)|112|(0)(0)|65|(0)|68|(0)|71|(0)|(0)|76|77|78))(3:105|(0)(0)|(0)))|131|(0)|112|(0)(0)|65|(0)|68|(0)|71|(0)|(0)|76|77|78))))|228|192|193|194|(0)(0)|205|206|40|(0)|53|54|55|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x01b9, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x03a6, code lost:
        if (android.text.TextUtils.isEmpty(r6[0]) == false) goto L92;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:106:0x01b2  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0249 A[Catch: all -> 0x064b, TryCatch #5 {all -> 0x064b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x007e, B:13:0x008d, B:22:0x00a0, B:26:0x00ab, B:33:0x00be, B:37:0x00c7, B:44:0x00d5, B:48:0x00de, B:56:0x00f2, B:58:0x00fb, B:60:0x0109, B:63:0x0113, B:65:0x0124, B:68:0x012e, B:70:0x0138, B:74:0x014c, B:78:0x0156, B:80:0x0160, B:82:0x0168, B:84:0x016f, B:91:0x017b, B:95:0x0185, B:97:0x018f, B:99:0x01a0, B:125:0x01f3, B:127:0x0249, B:131:0x0255, B:135:0x0265, B:136:0x0272, B:220:0x04ab, B:222:0x04ca, B:224:0x04ee, B:227:0x050a, B:228:0x052d, B:230:0x05b7, B:232:0x05d7, B:233:0x05fe, B:163:0x0342, B:167:0x034d, B:175:0x0388, B:170:0x0357, B:171:0x0363, B:174:0x036e, B:177:0x039f, B:201:0x0487, B:205:0x048e, B:213:0x049d, B:217:0x04a3, B:122:0x01e1, B:234:0x0626, B:211:0x0498), top: B:258:0x0008, inners: #1, #13, #16 }] */
    /* JADX WARN: Removed duplicated region for block: B:164:0x0347  */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0423 A[Catch: all -> 0x0493, TryCatch #7 {all -> 0x0493, blocks: (B:184:0x03f9, B:186:0x03fd, B:188:0x0401, B:190:0x0412, B:192:0x041e, B:194:0x0423, B:197:0x043b, B:195:0x042c, B:183:0x03ec), top: B:262:0x03ec }] */
    /* JADX WARN: Removed duplicated region for block: B:195:0x042c A[Catch: all -> 0x0493, TryCatch #7 {all -> 0x0493, blocks: (B:184:0x03f9, B:186:0x03fd, B:188:0x0401, B:190:0x0412, B:192:0x041e, B:194:0x0423, B:197:0x043b, B:195:0x042c, B:183:0x03ec), top: B:262:0x03ec }] */
    /* JADX WARN: Removed duplicated region for block: B:197:0x043b A[Catch: all -> 0x0493, TRY_LEAVE, TryCatch #7 {all -> 0x0493, blocks: (B:184:0x03f9, B:186:0x03fd, B:188:0x0401, B:190:0x0412, B:192:0x041e, B:194:0x0423, B:197:0x043b, B:195:0x042c, B:183:0x03ec), top: B:262:0x03ec }] */
    /* JADX WARN: Removed duplicated region for block: B:220:0x04ab A[Catch: all -> 0x064b, TryCatch #5 {all -> 0x064b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x007e, B:13:0x008d, B:22:0x00a0, B:26:0x00ab, B:33:0x00be, B:37:0x00c7, B:44:0x00d5, B:48:0x00de, B:56:0x00f2, B:58:0x00fb, B:60:0x0109, B:63:0x0113, B:65:0x0124, B:68:0x012e, B:70:0x0138, B:74:0x014c, B:78:0x0156, B:80:0x0160, B:82:0x0168, B:84:0x016f, B:91:0x017b, B:95:0x0185, B:97:0x018f, B:99:0x01a0, B:125:0x01f3, B:127:0x0249, B:131:0x0255, B:135:0x0265, B:136:0x0272, B:220:0x04ab, B:222:0x04ca, B:224:0x04ee, B:227:0x050a, B:228:0x052d, B:230:0x05b7, B:232:0x05d7, B:233:0x05fe, B:163:0x0342, B:167:0x034d, B:175:0x0388, B:170:0x0357, B:171:0x0363, B:174:0x036e, B:177:0x039f, B:201:0x0487, B:205:0x048e, B:213:0x049d, B:217:0x04a3, B:122:0x01e1, B:234:0x0626, B:211:0x0498), top: B:258:0x0008, inners: #1, #13, #16 }] */
    /* JADX WARN: Removed duplicated region for block: B:221:0x04c9  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x04ee A[Catch: all -> 0x064b, TryCatch #5 {all -> 0x064b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x007e, B:13:0x008d, B:22:0x00a0, B:26:0x00ab, B:33:0x00be, B:37:0x00c7, B:44:0x00d5, B:48:0x00de, B:56:0x00f2, B:58:0x00fb, B:60:0x0109, B:63:0x0113, B:65:0x0124, B:68:0x012e, B:70:0x0138, B:74:0x014c, B:78:0x0156, B:80:0x0160, B:82:0x0168, B:84:0x016f, B:91:0x017b, B:95:0x0185, B:97:0x018f, B:99:0x01a0, B:125:0x01f3, B:127:0x0249, B:131:0x0255, B:135:0x0265, B:136:0x0272, B:220:0x04ab, B:222:0x04ca, B:224:0x04ee, B:227:0x050a, B:228:0x052d, B:230:0x05b7, B:232:0x05d7, B:233:0x05fe, B:163:0x0342, B:167:0x034d, B:175:0x0388, B:170:0x0357, B:171:0x0363, B:174:0x036e, B:177:0x039f, B:201:0x0487, B:205:0x048e, B:213:0x049d, B:217:0x04a3, B:122:0x01e1, B:234:0x0626, B:211:0x0498), top: B:258:0x0008, inners: #1, #13, #16 }] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x050a A[Catch: all -> 0x064b, TryCatch #5 {all -> 0x064b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x007e, B:13:0x008d, B:22:0x00a0, B:26:0x00ab, B:33:0x00be, B:37:0x00c7, B:44:0x00d5, B:48:0x00de, B:56:0x00f2, B:58:0x00fb, B:60:0x0109, B:63:0x0113, B:65:0x0124, B:68:0x012e, B:70:0x0138, B:74:0x014c, B:78:0x0156, B:80:0x0160, B:82:0x0168, B:84:0x016f, B:91:0x017b, B:95:0x0185, B:97:0x018f, B:99:0x01a0, B:125:0x01f3, B:127:0x0249, B:131:0x0255, B:135:0x0265, B:136:0x0272, B:220:0x04ab, B:222:0x04ca, B:224:0x04ee, B:227:0x050a, B:228:0x052d, B:230:0x05b7, B:232:0x05d7, B:233:0x05fe, B:163:0x0342, B:167:0x034d, B:175:0x0388, B:170:0x0357, B:171:0x0363, B:174:0x036e, B:177:0x039f, B:201:0x0487, B:205:0x048e, B:213:0x049d, B:217:0x04a3, B:122:0x01e1, B:234:0x0626, B:211:0x0498), top: B:258:0x0008, inners: #1, #13, #16 }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00a0 A[Catch: all -> 0x064b, TryCatch #5 {all -> 0x064b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x007e, B:13:0x008d, B:22:0x00a0, B:26:0x00ab, B:33:0x00be, B:37:0x00c7, B:44:0x00d5, B:48:0x00de, B:56:0x00f2, B:58:0x00fb, B:60:0x0109, B:63:0x0113, B:65:0x0124, B:68:0x012e, B:70:0x0138, B:74:0x014c, B:78:0x0156, B:80:0x0160, B:82:0x0168, B:84:0x016f, B:91:0x017b, B:95:0x0185, B:97:0x018f, B:99:0x01a0, B:125:0x01f3, B:127:0x0249, B:131:0x0255, B:135:0x0265, B:136:0x0272, B:220:0x04ab, B:222:0x04ca, B:224:0x04ee, B:227:0x050a, B:228:0x052d, B:230:0x05b7, B:232:0x05d7, B:233:0x05fe, B:163:0x0342, B:167:0x034d, B:175:0x0388, B:170:0x0357, B:171:0x0363, B:174:0x036e, B:177:0x039f, B:201:0x0487, B:205:0x048e, B:213:0x049d, B:217:0x04a3, B:122:0x01e1, B:234:0x0626, B:211:0x0498), top: B:258:0x0008, inners: #1, #13, #16 }] */
    /* JADX WARN: Removed duplicated region for block: B:230:0x05b7 A[Catch: all -> 0x064b, TryCatch #5 {all -> 0x064b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x007e, B:13:0x008d, B:22:0x00a0, B:26:0x00ab, B:33:0x00be, B:37:0x00c7, B:44:0x00d5, B:48:0x00de, B:56:0x00f2, B:58:0x00fb, B:60:0x0109, B:63:0x0113, B:65:0x0124, B:68:0x012e, B:70:0x0138, B:74:0x014c, B:78:0x0156, B:80:0x0160, B:82:0x0168, B:84:0x016f, B:91:0x017b, B:95:0x0185, B:97:0x018f, B:99:0x01a0, B:125:0x01f3, B:127:0x0249, B:131:0x0255, B:135:0x0265, B:136:0x0272, B:220:0x04ab, B:222:0x04ca, B:224:0x04ee, B:227:0x050a, B:228:0x052d, B:230:0x05b7, B:232:0x05d7, B:233:0x05fe, B:163:0x0342, B:167:0x034d, B:175:0x0388, B:170:0x0357, B:171:0x0363, B:174:0x036e, B:177:0x039f, B:201:0x0487, B:205:0x048e, B:213:0x049d, B:217:0x04a3, B:122:0x01e1, B:234:0x0626, B:211:0x0498), top: B:258:0x0008, inners: #1, #13, #16 }] */
    /* JADX WARN: Removed duplicated region for block: B:232:0x05d7 A[Catch: all -> 0x064b, TryCatch #5 {all -> 0x064b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x007e, B:13:0x008d, B:22:0x00a0, B:26:0x00ab, B:33:0x00be, B:37:0x00c7, B:44:0x00d5, B:48:0x00de, B:56:0x00f2, B:58:0x00fb, B:60:0x0109, B:63:0x0113, B:65:0x0124, B:68:0x012e, B:70:0x0138, B:74:0x014c, B:78:0x0156, B:80:0x0160, B:82:0x0168, B:84:0x016f, B:91:0x017b, B:95:0x0185, B:97:0x018f, B:99:0x01a0, B:125:0x01f3, B:127:0x0249, B:131:0x0255, B:135:0x0265, B:136:0x0272, B:220:0x04ab, B:222:0x04ca, B:224:0x04ee, B:227:0x050a, B:228:0x052d, B:230:0x05b7, B:232:0x05d7, B:233:0x05fe, B:163:0x0342, B:167:0x034d, B:175:0x0388, B:170:0x0357, B:171:0x0363, B:174:0x036e, B:177:0x039f, B:201:0x0487, B:205:0x048e, B:213:0x049d, B:217:0x04a3, B:122:0x01e1, B:234:0x0626, B:211:0x0498), top: B:258:0x0008, inners: #1, #13, #16 }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x0487 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:254:0x00fb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:264:0x02bf A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:275:0x0286 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00be A[Catch: all -> 0x064b, TryCatch #5 {all -> 0x064b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x007e, B:13:0x008d, B:22:0x00a0, B:26:0x00ab, B:33:0x00be, B:37:0x00c7, B:44:0x00d5, B:48:0x00de, B:56:0x00f2, B:58:0x00fb, B:60:0x0109, B:63:0x0113, B:65:0x0124, B:68:0x012e, B:70:0x0138, B:74:0x014c, B:78:0x0156, B:80:0x0160, B:82:0x0168, B:84:0x016f, B:91:0x017b, B:95:0x0185, B:97:0x018f, B:99:0x01a0, B:125:0x01f3, B:127:0x0249, B:131:0x0255, B:135:0x0265, B:136:0x0272, B:220:0x04ab, B:222:0x04ca, B:224:0x04ee, B:227:0x050a, B:228:0x052d, B:230:0x05b7, B:232:0x05d7, B:233:0x05fe, B:163:0x0342, B:167:0x034d, B:175:0x0388, B:170:0x0357, B:171:0x0363, B:174:0x036e, B:177:0x039f, B:201:0x0487, B:205:0x048e, B:213:0x049d, B:217:0x04a3, B:122:0x01e1, B:234:0x0626, B:211:0x0498), top: B:258:0x0008, inners: #1, #13, #16 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00c5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00d2  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00d5 A[Catch: all -> 0x064b, TryCatch #5 {all -> 0x064b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001a, B:8:0x007e, B:13:0x008d, B:22:0x00a0, B:26:0x00ab, B:33:0x00be, B:37:0x00c7, B:44:0x00d5, B:48:0x00de, B:56:0x00f2, B:58:0x00fb, B:60:0x0109, B:63:0x0113, B:65:0x0124, B:68:0x012e, B:70:0x0138, B:74:0x014c, B:78:0x0156, B:80:0x0160, B:82:0x0168, B:84:0x016f, B:91:0x017b, B:95:0x0185, B:97:0x018f, B:99:0x01a0, B:125:0x01f3, B:127:0x0249, B:131:0x0255, B:135:0x0265, B:136:0x0272, B:220:0x04ab, B:222:0x04ca, B:224:0x04ee, B:227:0x050a, B:228:0x052d, B:230:0x05b7, B:232:0x05d7, B:233:0x05fe, B:163:0x0342, B:167:0x034d, B:175:0x0388, B:170:0x0357, B:171:0x0363, B:174:0x036e, B:177:0x039f, B:201:0x0487, B:205:0x048e, B:213:0x049d, B:217:0x04a3, B:122:0x01e1, B:234:0x0626, B:211:0x0498), top: B:258:0x0008, inners: #1, #13, #16 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00dc A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00ee  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00f0  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0183  */
    /* JADX WARN: Type inference failed for: r3v28 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String createThemePreviewImage(String str, String str2, ThemeAccent themeAccent) {
        Paint paint;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        Canvas canvas;
        int i14;
        int i15;
        String queryParameter;
        int i16;
        int i17;
        int i18;
        int i19;
        BitmapFactory.Options options;
        int i20;
        Canvas canvas2;
        boolean z;
        boolean z2;
        int i21;
        Bitmap decodeFile;
        int i22;
        int i23;
        Canvas canvas3;
        Canvas canvas4;
        FileInputStream fileInputStream;
        BitmapFactory.Options options2;
        FileInputStream fileInputStream2;
        File file;
        int i24;
        int i25;
        Bitmap decodeStream;
        int i26;
        Drawable createDitheredGradientBitmapDrawable;
        int i27;
        Canvas canvas5 = themeAccent;
        try {
            String[] strArr = new String[1];
            final SparseIntArray themeFileValues = getThemeFileValues(new File(str), null, strArr);
            if (canvas5 != null) {
                checkIsDark(themeFileValues, canvas5.parentTheme);
            }
            int i28 = currentColorsNoAccent.get(key_wallpaperFileOffset, -1);
            Bitmap createBitmap = Bitmaps.createBitmap(560, 678, Bitmap.Config.ARGB_8888);
            Canvas canvas6 = new Canvas(createBitmap);
            Paint paint2 = new Paint();
            int previewColor = getPreviewColor(themeFileValues, key_actionBarDefault);
            int previewColor2 = getPreviewColor(themeFileValues, key_actionBarDefaultIcon);
            int previewColor3 = getPreviewColor(themeFileValues, key_chat_messagePanelBackground);
            int previewColor4 = getPreviewColor(themeFileValues, key_chat_messagePanelIcons);
            int previewColor5 = getPreviewColor(themeFileValues, key_chat_inBubble);
            int previewColor6 = getPreviewColor(themeFileValues, key_chat_outBubble);
            themeFileValues.get(key_chat_outBubbleGradient1);
            int i29 = themeFileValues.get(key_chat_wallpaper);
            int i30 = themeFileValues.get(key_chat_wallpaper_gradient_to1);
            int i31 = themeFileValues.get(key_chat_wallpaper_gradient_to2);
            int i32 = themeFileValues.get(key_chat_wallpaper_gradient_to3);
            if (canvas5 != null) {
                paint = paint2;
                i = (int) canvas5.backgroundOverrideColor;
            } else {
                paint = paint2;
                i = 0;
            }
            if (i != 0 || canvas5 == null) {
                i2 = i32;
            } else {
                i2 = i32;
                if (canvas5.backgroundOverrideColor != 0) {
                    i3 = 0;
                    if (canvas5 == null) {
                        i4 = i3;
                        i5 = (int) canvas5.backgroundGradientOverrideColor1;
                    } else {
                        i4 = i3;
                        i5 = 0;
                    }
                    if (i5 == 0 || canvas5 == null) {
                        i6 = i2;
                        i7 = i4;
                    } else {
                        i6 = i2;
                        i7 = i4;
                        if (canvas5.backgroundGradientOverrideColor1 != 0) {
                            i30 = 0;
                            i8 = canvas5 != null ? (int) canvas5.backgroundGradientOverrideColor2 : 0;
                            if (i8 != 0 && canvas5 != null && canvas5.backgroundGradientOverrideColor2 != 0) {
                                i8 = 0;
                            } else if (i8 == 0) {
                                i8 = i31;
                            }
                            i9 = canvas5 != null ? (int) canvas5.backgroundGradientOverrideColor3 : 0;
                            if (i9 == 0 || canvas5 == null) {
                                i10 = i6;
                                i11 = i8;
                            } else {
                                i10 = i6;
                                i11 = i8;
                                if (canvas5.backgroundGradientOverrideColor3 != 0) {
                                    i12 = 0;
                                    if (TextUtils.isEmpty(strArr[0])) {
                                        try {
                                            queryParameter = Uri.parse(strArr[0]).getQueryParameter("bg_color");
                                        } catch (Exception e) {
                                            e = e;
                                            i13 = i12;
                                        }
                                        if (canvas5 != null) {
                                            if (!TextUtils.isEmpty(queryParameter)) {
                                                i13 = i12;
                                                try {
                                                    i14 = Integer.parseInt(queryParameter.substring(0, 6), 16) | (-16777216);
                                                    canvas = canvas6;
                                                    try {
                                                        canvas5.backgroundOverrideColor = i14;
                                                        if (queryParameter.length() >= 13) {
                                                            try {
                                                                if (AndroidUtilities.isValidWallChar(queryParameter.charAt(6))) {
                                                                    i30 = Integer.parseInt(queryParameter.substring(7, 13), 16) | (-16777216);
                                                                    canvas5.backgroundGradientOverrideColor1 = i30;
                                                                }
                                                            } catch (Exception e2) {
                                                                e = e2;
                                                                FileLog.e(e);
                                                                i15 = i30;
                                                                i17 = i11;
                                                                int i33 = i13;
                                                                Drawable mutate = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                                                setDrawableColor(mutate, previewColor2);
                                                                Drawable mutate2 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                                                setDrawableColor(mutate2, previewColor2);
                                                                Drawable mutate3 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                                                setDrawableColor(mutate3, previewColor4);
                                                                Drawable mutate4 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                                                setDrawableColor(mutate4, previewColor4);
                                                                MessageDrawable[] messageDrawableArr = new MessageDrawable[2];
                                                                i19 = 0;
                                                                for (i18 = 2; i19 < i18; i18 = 2) {
                                                                }
                                                                Drawable drawable = mutate4;
                                                                Drawable drawable2 = mutate3;
                                                                RectF rectF = new RectF();
                                                                if (str2 != null) {
                                                                }
                                                            }
                                                        }
                                                    } catch (Exception e3) {
                                                        e = e3;
                                                    }
                                                } catch (Exception e4) {
                                                    e = e4;
                                                    canvas = canvas6;
                                                    i14 = i7;
                                                    FileLog.e(e);
                                                    i15 = i30;
                                                    i17 = i11;
                                                    int i332 = i13;
                                                    Drawable mutate5 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                                    setDrawableColor(mutate5, previewColor2);
                                                    Drawable mutate22 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                                    setDrawableColor(mutate22, previewColor2);
                                                    Drawable mutate32 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                                    setDrawableColor(mutate32, previewColor4);
                                                    Drawable mutate42 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                                    setDrawableColor(mutate42, previewColor4);
                                                    MessageDrawable[] messageDrawableArr2 = new MessageDrawable[2];
                                                    i19 = 0;
                                                    while (i19 < i18) {
                                                    }
                                                    Drawable drawable3 = mutate42;
                                                    Drawable drawable22 = mutate32;
                                                    RectF rectF2 = new RectF();
                                                    if (str2 != null) {
                                                    }
                                                }
                                                if (queryParameter.length() >= 20) {
                                                    try {
                                                    } catch (Exception e5) {
                                                        e = e5;
                                                        FileLog.e(e);
                                                        i15 = i30;
                                                        i17 = i11;
                                                        int i3322 = i13;
                                                        Drawable mutate52 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                                        setDrawableColor(mutate52, previewColor2);
                                                        Drawable mutate222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                                        setDrawableColor(mutate222, previewColor2);
                                                        Drawable mutate322 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                                        setDrawableColor(mutate322, previewColor4);
                                                        Drawable mutate422 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                                        setDrawableColor(mutate422, previewColor4);
                                                        MessageDrawable[] messageDrawableArr22 = new MessageDrawable[2];
                                                        i19 = 0;
                                                        while (i19 < i18) {
                                                        }
                                                        Drawable drawable32 = mutate422;
                                                        Drawable drawable222 = mutate322;
                                                        RectF rectF22 = new RectF();
                                                        if (str2 != null) {
                                                        }
                                                    }
                                                    if (AndroidUtilities.isValidWallChar(queryParameter.charAt(13))) {
                                                        i16 = Integer.parseInt(queryParameter.substring(14, 20), 16) | (-16777216);
                                                        try {
                                                            canvas5.backgroundGradientOverrideColor2 = i16;
                                                            if (queryParameter.length() != 27) {
                                                                try {
                                                                    if (AndroidUtilities.isValidWallChar(queryParameter.charAt(20))) {
                                                                        int parseInt = Integer.parseInt(queryParameter.substring(21), 16) | (-16777216);
                                                                        i7 = i14;
                                                                        try {
                                                                            canvas5.backgroundGradientOverrideColor3 = parseInt;
                                                                            i13 = parseInt;
                                                                        } catch (Exception e6) {
                                                                            e = e6;
                                                                            i11 = i16;
                                                                            i13 = parseInt;
                                                                            i14 = i7;
                                                                            FileLog.e(e);
                                                                            i15 = i30;
                                                                            i17 = i11;
                                                                            int i33222 = i13;
                                                                            Drawable mutate522 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                                                            setDrawableColor(mutate522, previewColor2);
                                                                            Drawable mutate2222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                                                            setDrawableColor(mutate2222, previewColor2);
                                                                            Drawable mutate3222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                                                            setDrawableColor(mutate3222, previewColor4);
                                                                            Drawable mutate4222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                                                            setDrawableColor(mutate4222, previewColor4);
                                                                            MessageDrawable[] messageDrawableArr222 = new MessageDrawable[2];
                                                                            i19 = 0;
                                                                            while (i19 < i18) {
                                                                            }
                                                                            Drawable drawable322 = mutate4222;
                                                                            Drawable drawable2222 = mutate3222;
                                                                            RectF rectF222 = new RectF();
                                                                            if (str2 != null) {
                                                                            }
                                                                        }
                                                                    } else {
                                                                        i7 = i14;
                                                                    }
                                                                } catch (Exception e7) {
                                                                    e = e7;
                                                                    i11 = i16;
                                                                    FileLog.e(e);
                                                                    i15 = i30;
                                                                    i17 = i11;
                                                                    int i332222 = i13;
                                                                    Drawable mutate5222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                                                    setDrawableColor(mutate5222, previewColor2);
                                                                    Drawable mutate22222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                                                    setDrawableColor(mutate22222, previewColor2);
                                                                    Drawable mutate32222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                                                    setDrawableColor(mutate32222, previewColor4);
                                                                    Drawable mutate42222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                                                    setDrawableColor(mutate42222, previewColor4);
                                                                    MessageDrawable[] messageDrawableArr2222 = new MessageDrawable[2];
                                                                    i19 = 0;
                                                                    while (i19 < i18) {
                                                                    }
                                                                    Drawable drawable3222 = mutate42222;
                                                                    Drawable drawable22222 = mutate32222;
                                                                    RectF rectF2222 = new RectF();
                                                                    if (str2 != null) {
                                                                    }
                                                                }
                                                            } else {
                                                                i7 = i14;
                                                            }
                                                            i14 = i7;
                                                            i15 = i30;
                                                            i17 = i16;
                                                        } catch (Exception e8) {
                                                            e = e8;
                                                            i11 = i16;
                                                            FileLog.e(e);
                                                            i15 = i30;
                                                            i17 = i11;
                                                            int i3322222 = i13;
                                                            Drawable mutate52222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                                            setDrawableColor(mutate52222, previewColor2);
                                                            Drawable mutate222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                                            setDrawableColor(mutate222222, previewColor2);
                                                            Drawable mutate322222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                                            setDrawableColor(mutate322222, previewColor4);
                                                            Drawable mutate422222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                                            setDrawableColor(mutate422222, previewColor4);
                                                            MessageDrawable[] messageDrawableArr22222 = new MessageDrawable[2];
                                                            i19 = 0;
                                                            while (i19 < i18) {
                                                            }
                                                            Drawable drawable32222 = mutate422222;
                                                            Drawable drawable222222 = mutate322222;
                                                            RectF rectF22222 = new RectF();
                                                            if (str2 != null) {
                                                            }
                                                        }
                                                        int i33222222 = i13;
                                                        Drawable mutate522222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                                        setDrawableColor(mutate522222, previewColor2);
                                                        Drawable mutate2222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                                        setDrawableColor(mutate2222222, previewColor2);
                                                        Drawable mutate3222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                                        setDrawableColor(mutate3222222, previewColor4);
                                                        Drawable mutate4222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                                        setDrawableColor(mutate4222222, previewColor4);
                                                        MessageDrawable[] messageDrawableArr222222 = new MessageDrawable[2];
                                                        i19 = 0;
                                                        while (i19 < i18) {
                                                            Drawable drawable4 = mutate4222222;
                                                            Drawable drawable5 = mutate3222222;
                                                            messageDrawableArr222222[i19] = new MessageDrawable(2, i19 == 1, false) { // from class: org.telegram.ui.ActionBar.Theme.10
                                                                @Override // org.telegram.ui.ActionBar.Theme.MessageDrawable
                                                                protected int getColor(int i34) {
                                                                    int indexOfKey = themeFileValues.indexOfKey(i34);
                                                                    if (indexOfKey <= 0) {
                                                                        return Theme.defaultColors[i34];
                                                                    }
                                                                    return themeFileValues.valueAt(indexOfKey);
                                                                }

                                                                @Override // org.telegram.ui.ActionBar.Theme.MessageDrawable
                                                                protected int getCurrentColor(int i34) {
                                                                    return themeFileValues.get(i34);
                                                                }
                                                            };
                                                            setDrawableColor(messageDrawableArr222222[i19], i19 == 0 ? previewColor5 : previewColor6);
                                                            i19++;
                                                            mutate4222222 = drawable4;
                                                            mutate3222222 = drawable5;
                                                        }
                                                        Drawable drawable322222 = mutate4222222;
                                                        Drawable drawable2222222 = mutate3222222;
                                                        RectF rectF222222 = new RectF();
                                                        if (str2 != null) {
                                                            try {
                                                                options = new BitmapFactory.Options();
                                                                options.inJustDecodeBounds = true;
                                                                BitmapFactory.decodeFile(str2, options);
                                                                i20 = options.outWidth;
                                                            } catch (Throwable th) {
                                                                th = th;
                                                                canvas5 = canvas;
                                                            }
                                                            if (i20 > 0 && (i21 = options.outHeight) > 0) {
                                                                float min = Math.min(i20 / 560.0f, i21 / 560.0f);
                                                                options.inSampleSize = 1;
                                                                if (min > 1.0f) {
                                                                    do {
                                                                        i22 = options.inSampleSize * 2;
                                                                        options.inSampleSize = i22;
                                                                    } while (i22 < min);
                                                                    options.inJustDecodeBounds = false;
                                                                    decodeFile = BitmapFactory.decodeFile(str2, options);
                                                                    if (decodeFile != null) {
                                                                        try {
                                                                            if (i17 != 0 && canvas5 != null) {
                                                                                MotionBackgroundDrawable motionBackgroundDrawable = new MotionBackgroundDrawable(i14, i15, i17, i33222222, true);
                                                                                motionBackgroundDrawable.setPatternBitmap((int) (canvas5.patternIntensity * 100.0f), decodeFile);
                                                                                motionBackgroundDrawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                                                                                Canvas canvas7 = canvas;
                                                                                motionBackgroundDrawable.draw(canvas7);
                                                                                canvas5 = canvas7;
                                                                            } else {
                                                                                Canvas canvas8 = canvas;
                                                                                Paint paint3 = new Paint();
                                                                                paint3.setFilterBitmap(true);
                                                                                float min2 = Math.min(decodeFile.getWidth() / 560.0f, decodeFile.getHeight() / 560.0f);
                                                                                rectF222222.set(0.0f, 0.0f, decodeFile.getWidth() / min2, decodeFile.getHeight() / min2);
                                                                                rectF222222.offset((createBitmap.getWidth() - rectF222222.width()) / 2.0f, (createBitmap.getHeight() - rectF222222.height()) / 2.0f);
                                                                                canvas8.drawBitmap(decodeFile, (Rect) null, rectF222222, paint3);
                                                                                canvas5 = canvas8;
                                                                            }
                                                                            z = true;
                                                                            canvas2 = canvas5;
                                                                            z2 = z;
                                                                            i23 = 80;
                                                                            canvas4 = canvas2;
                                                                        } catch (Throwable th2) {
                                                                            th = th2;
                                                                            FileLog.e(th);
                                                                            canvas3 = canvas5;
                                                                            i23 = 80;
                                                                            z2 = false;
                                                                            canvas4 = canvas3;
                                                                            if (z2) {
                                                                            }
                                                                            Paint paint4 = paint;
                                                                            paint4.setColor(previewColor);
                                                                            canvas4.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint4);
                                                                            if (mutate522222 != null) {
                                                                            }
                                                                            if (mutate2222222 != null) {
                                                                            }
                                                                            messageDrawableArr222222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                                            messageDrawableArr222222[1].setTop(0, 560, 522, false, false);
                                                                            messageDrawableArr222222[1].draw(canvas4);
                                                                            messageDrawableArr222222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                                            messageDrawableArr222222[1].setTop(430, 560, 522, false, false);
                                                                            messageDrawableArr222222[1].draw(canvas4);
                                                                            messageDrawableArr222222[0].setBounds(20, 323, 399, 415);
                                                                            messageDrawableArr222222[0].setTop(323, 560, 522, false, false);
                                                                            messageDrawableArr222222[0].draw(canvas4);
                                                                            paint4.setColor(previewColor3);
                                                                            canvas4.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint4);
                                                                            if (drawable2222222 != null) {
                                                                            }
                                                                            if (drawable322222 != null) {
                                                                            }
                                                                            canvas4.setBitmap(null);
                                                                            File file2 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                            createBitmap.compress(Bitmap.CompressFormat.JPEG, i23, new FileOutputStream(file2));
                                                                            SharedConfig.saveConfig();
                                                                            return file2.getAbsolutePath();
                                                                        }
                                                                        if (z2) {
                                                                            Drawable createDefaultWallpaper = createDefaultWallpaper(createBitmap.getWidth(), createBitmap.getHeight() - 120);
                                                                            createDefaultWallpaper.setBounds(0, 120, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                                                                            createDefaultWallpaper.draw(canvas4);
                                                                        }
                                                                        Paint paint42 = paint;
                                                                        paint42.setColor(previewColor);
                                                                        canvas4.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint42);
                                                                        if (mutate522222 != null) {
                                                                            int intrinsicHeight = (120 - mutate522222.getIntrinsicHeight()) / 2;
                                                                            mutate522222.setBounds(13, intrinsicHeight, mutate522222.getIntrinsicWidth() + 13, mutate522222.getIntrinsicHeight() + intrinsicHeight);
                                                                            mutate522222.draw(canvas4);
                                                                        }
                                                                        if (mutate2222222 != null) {
                                                                            int width = (createBitmap.getWidth() - mutate2222222.getIntrinsicWidth()) - 10;
                                                                            int intrinsicHeight2 = (120 - mutate2222222.getIntrinsicHeight()) / 2;
                                                                            mutate2222222.setBounds(width, intrinsicHeight2, mutate2222222.getIntrinsicWidth() + width, mutate2222222.getIntrinsicHeight() + intrinsicHeight2);
                                                                            mutate2222222.draw(canvas4);
                                                                        }
                                                                        messageDrawableArr222222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                                        messageDrawableArr222222[1].setTop(0, 560, 522, false, false);
                                                                        messageDrawableArr222222[1].draw(canvas4);
                                                                        messageDrawableArr222222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                                        messageDrawableArr222222[1].setTop(430, 560, 522, false, false);
                                                                        messageDrawableArr222222[1].draw(canvas4);
                                                                        messageDrawableArr222222[0].setBounds(20, 323, 399, 415);
                                                                        messageDrawableArr222222[0].setTop(323, 560, 522, false, false);
                                                                        messageDrawableArr222222[0].draw(canvas4);
                                                                        paint42.setColor(previewColor3);
                                                                        canvas4.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint42);
                                                                        if (drawable2222222 != null) {
                                                                            int height = (createBitmap.getHeight() - 120) + ((120 - drawable2222222.getIntrinsicHeight()) / 2);
                                                                            drawable2222222.setBounds(22, height, drawable2222222.getIntrinsicWidth() + 22, drawable2222222.getIntrinsicHeight() + height);
                                                                            drawable2222222.draw(canvas4);
                                                                        }
                                                                        if (drawable322222 != null) {
                                                                            int width2 = (createBitmap.getWidth() - drawable322222.getIntrinsicWidth()) - 22;
                                                                            int height2 = (createBitmap.getHeight() - 120) + ((120 - drawable322222.getIntrinsicHeight()) / 2);
                                                                            drawable322222.setBounds(width2, height2, drawable322222.getIntrinsicWidth() + width2, drawable322222.getIntrinsicHeight() + height2);
                                                                            drawable322222.draw(canvas4);
                                                                        }
                                                                        canvas4.setBitmap(null);
                                                                        File file22 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                        createBitmap.compress(Bitmap.CompressFormat.JPEG, i23, new FileOutputStream(file22));
                                                                        SharedConfig.saveConfig();
                                                                        return file22.getAbsolutePath();
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
                                                            i23 = 80;
                                                            canvas4 = canvas2;
                                                            if (z2) {
                                                            }
                                                            Paint paint422 = paint;
                                                            paint422.setColor(previewColor);
                                                            canvas4.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint422);
                                                            if (mutate522222 != null) {
                                                            }
                                                            if (mutate2222222 != null) {
                                                            }
                                                            messageDrawableArr222222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                            messageDrawableArr222222[1].setTop(0, 560, 522, false, false);
                                                            messageDrawableArr222222[1].draw(canvas4);
                                                            messageDrawableArr222222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                            messageDrawableArr222222[1].setTop(430, 560, 522, false, false);
                                                            messageDrawableArr222222[1].draw(canvas4);
                                                            messageDrawableArr222222[0].setBounds(20, 323, 399, 415);
                                                            messageDrawableArr222222[0].setTop(323, 560, 522, false, false);
                                                            messageDrawableArr222222[0].draw(canvas4);
                                                            paint422.setColor(previewColor3);
                                                            canvas4.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint422);
                                                            if (drawable2222222 != null) {
                                                            }
                                                            if (drawable322222 != null) {
                                                            }
                                                            canvas4.setBitmap(null);
                                                            File file222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                            createBitmap.compress(Bitmap.CompressFormat.JPEG, i23, new FileOutputStream(file222));
                                                            SharedConfig.saveConfig();
                                                            return file222.getAbsolutePath();
                                                        }
                                                        canvas2 = canvas;
                                                        if (i14 != 0) {
                                                            if (i15 == 0) {
                                                                createDitheredGradientBitmapDrawable = new ColorDrawable(i14);
                                                            } else if (i17 != 0) {
                                                                createDitheredGradientBitmapDrawable = new MotionBackgroundDrawable(i14, i15, i17, i33222222, true);
                                                            } else {
                                                                int i34 = themeFileValues.get(key_chat_wallpaper_gradient_rotation, -1);
                                                                if (i34 == -1) {
                                                                    i34 = 45;
                                                                }
                                                                createDitheredGradientBitmapDrawable = BackgroundGradientDrawable.createDitheredGradientBitmapDrawable(i34, new int[]{i14, i31}, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                                                                i27 = 90;
                                                                createDitheredGradientBitmapDrawable.setBounds(0, 120, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                                                                createDitheredGradientBitmapDrawable.draw(canvas2);
                                                                i23 = i27;
                                                                z2 = true;
                                                                canvas4 = canvas2;
                                                                if (z2) {
                                                                }
                                                                Paint paint4222 = paint;
                                                                paint4222.setColor(previewColor);
                                                                canvas4.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint4222);
                                                                if (mutate522222 != null) {
                                                                }
                                                                if (mutate2222222 != null) {
                                                                }
                                                                messageDrawableArr222222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                                messageDrawableArr222222[1].setTop(0, 560, 522, false, false);
                                                                messageDrawableArr222222[1].draw(canvas4);
                                                                messageDrawableArr222222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                                messageDrawableArr222222[1].setTop(430, 560, 522, false, false);
                                                                messageDrawableArr222222[1].draw(canvas4);
                                                                messageDrawableArr222222[0].setBounds(20, 323, 399, 415);
                                                                messageDrawableArr222222[0].setTop(323, 560, 522, false, false);
                                                                messageDrawableArr222222[0].draw(canvas4);
                                                                paint4222.setColor(previewColor3);
                                                                canvas4.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint4222);
                                                                if (drawable2222222 != null) {
                                                                }
                                                                if (drawable322222 != null) {
                                                                }
                                                                canvas4.setBitmap(null);
                                                                File file2222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                createBitmap.compress(Bitmap.CompressFormat.JPEG, i23, new FileOutputStream(file2222));
                                                                SharedConfig.saveConfig();
                                                                return file2222.getAbsolutePath();
                                                            }
                                                            i27 = 80;
                                                            createDitheredGradientBitmapDrawable.setBounds(0, 120, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                                                            createDitheredGradientBitmapDrawable.draw(canvas2);
                                                            i23 = i27;
                                                            z2 = true;
                                                            canvas4 = canvas2;
                                                            if (z2) {
                                                            }
                                                            Paint paint42222 = paint;
                                                            paint42222.setColor(previewColor);
                                                            canvas4.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint42222);
                                                            if (mutate522222 != null) {
                                                            }
                                                            if (mutate2222222 != null) {
                                                            }
                                                            messageDrawableArr222222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                            messageDrawableArr222222[1].setTop(0, 560, 522, false, false);
                                                            messageDrawableArr222222[1].draw(canvas4);
                                                            messageDrawableArr222222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                            messageDrawableArr222222[1].setTop(430, 560, 522, false, false);
                                                            messageDrawableArr222222[1].draw(canvas4);
                                                            messageDrawableArr222222[0].setBounds(20, 323, 399, 415);
                                                            messageDrawableArr222222[0].setTop(323, 560, 522, false, false);
                                                            messageDrawableArr222222[0].draw(canvas4);
                                                            paint42222.setColor(previewColor3);
                                                            canvas4.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint42222);
                                                            if (drawable2222222 != null) {
                                                            }
                                                            if (drawable322222 != null) {
                                                            }
                                                            canvas4.setBitmap(null);
                                                            File file22222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                            createBitmap.compress(Bitmap.CompressFormat.JPEG, i23, new FileOutputStream(file22222));
                                                            SharedConfig.saveConfig();
                                                            return file22222.getAbsolutePath();
                                                        }
                                                        if (i28 < 0) {
                                                            canvas3 = canvas2;
                                                        }
                                                        try {
                                                            options2 = new BitmapFactory.Options();
                                                            options2.inJustDecodeBounds = true;
                                                            if (!TextUtils.isEmpty(strArr[0])) {
                                                                File file3 = new File(ApplicationLoader.getFilesDirFixed(), Utilities.MD5(strArr[0]) + ".wp");
                                                                BitmapFactory.decodeFile(file3.getAbsolutePath(), options2);
                                                                file = file3;
                                                                fileInputStream2 = null;
                                                            } else {
                                                                fileInputStream2 = new FileInputStream(str);
                                                                try {
                                                                    fileInputStream2.getChannel().position(i28);
                                                                    BitmapFactory.decodeStream(fileInputStream2, null, options2);
                                                                    file = null;
                                                                } catch (Throwable th3) {
                                                                    th = th3;
                                                                    fileInputStream = fileInputStream2;
                                                                    FileLog.e(th);
                                                                    canvas3 = canvas2;
                                                                    if (fileInputStream != null) {
                                                                        try {
                                                                            fileInputStream.close();
                                                                            canvas3 = canvas2;
                                                                        } catch (Exception e9) {
                                                                            FileLog.e(e9);
                                                                            canvas3 = canvas2;
                                                                        }
                                                                    }
                                                                    i23 = 80;
                                                                    z2 = false;
                                                                    canvas4 = canvas3;
                                                                    if (z2) {
                                                                    }
                                                                    Paint paint422222 = paint;
                                                                    paint422222.setColor(previewColor);
                                                                    canvas4.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint422222);
                                                                    if (mutate522222 != null) {
                                                                    }
                                                                    if (mutate2222222 != null) {
                                                                    }
                                                                    messageDrawableArr222222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                                    messageDrawableArr222222[1].setTop(0, 560, 522, false, false);
                                                                    messageDrawableArr222222[1].draw(canvas4);
                                                                    messageDrawableArr222222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                                    messageDrawableArr222222[1].setTop(430, 560, 522, false, false);
                                                                    messageDrawableArr222222[1].draw(canvas4);
                                                                    messageDrawableArr222222[0].setBounds(20, 323, 399, 415);
                                                                    messageDrawableArr222222[0].setTop(323, 560, 522, false, false);
                                                                    messageDrawableArr222222[0].draw(canvas4);
                                                                    paint422222.setColor(previewColor3);
                                                                    canvas4.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint422222);
                                                                    if (drawable2222222 != null) {
                                                                    }
                                                                    if (drawable322222 != null) {
                                                                    }
                                                                    canvas4.setBitmap(null);
                                                                    File file222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                    createBitmap.compress(Bitmap.CompressFormat.JPEG, i23, new FileOutputStream(file222222));
                                                                    SharedConfig.saveConfig();
                                                                    return file222222.getAbsolutePath();
                                                                }
                                                            }
                                                            i24 = options2.outWidth;
                                                        } catch (Throwable th4) {
                                                            th = th4;
                                                            fileInputStream = null;
                                                        }
                                                        if (i24 > 0 && (i25 = options2.outHeight) > 0) {
                                                            float min3 = Math.min(i24 / 560.0f, i25 / 560.0f);
                                                            options2.inSampleSize = 1;
                                                            if (min3 > 1.0f) {
                                                                do {
                                                                    i26 = options2.inSampleSize * 2;
                                                                    options2.inSampleSize = i26;
                                                                } while (i26 < min3);
                                                                options2.inJustDecodeBounds = false;
                                                                if (file == null) {
                                                                    decodeStream = BitmapFactory.decodeFile(file.getAbsolutePath(), options2);
                                                                } else {
                                                                    fileInputStream2.getChannel().position(i28);
                                                                    decodeStream = BitmapFactory.decodeStream(fileInputStream2, null, options2);
                                                                }
                                                                if (decodeStream != null) {
                                                                    Paint paint5 = new Paint();
                                                                    paint5.setFilterBitmap(true);
                                                                    float min4 = Math.min(decodeStream.getWidth() / 560.0f, decodeStream.getHeight() / 560.0f);
                                                                    rectF222222.set(0.0f, 0.0f, decodeStream.getWidth() / min4, decodeStream.getHeight() / min4);
                                                                    rectF222222.offset((createBitmap.getWidth() - rectF222222.width()) / 2.0f, (createBitmap.getHeight() - rectF222222.height()) / 2.0f);
                                                                    canvas2.drawBitmap(decodeStream, (Rect) null, rectF222222, paint5);
                                                                    z2 = true;
                                                                    if (fileInputStream2 != null) {
                                                                        try {
                                                                            fileInputStream2.close();
                                                                        } catch (Exception e10) {
                                                                            FileLog.e(e10);
                                                                        }
                                                                    }
                                                                    i23 = 80;
                                                                    canvas4 = canvas2;
                                                                    if (z2) {
                                                                    }
                                                                    Paint paint4222222 = paint;
                                                                    paint4222222.setColor(previewColor);
                                                                    canvas4.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint4222222);
                                                                    if (mutate522222 != null) {
                                                                    }
                                                                    if (mutate2222222 != null) {
                                                                    }
                                                                    messageDrawableArr222222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                                    messageDrawableArr222222[1].setTop(0, 560, 522, false, false);
                                                                    messageDrawableArr222222[1].draw(canvas4);
                                                                    messageDrawableArr222222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                                    messageDrawableArr222222[1].setTop(430, 560, 522, false, false);
                                                                    messageDrawableArr222222[1].draw(canvas4);
                                                                    messageDrawableArr222222[0].setBounds(20, 323, 399, 415);
                                                                    messageDrawableArr222222[0].setTop(323, 560, 522, false, false);
                                                                    messageDrawableArr222222[0].draw(canvas4);
                                                                    paint4222222.setColor(previewColor3);
                                                                    canvas4.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint4222222);
                                                                    if (drawable2222222 != null) {
                                                                    }
                                                                    if (drawable322222 != null) {
                                                                    }
                                                                    canvas4.setBitmap(null);
                                                                    File file2222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                    createBitmap.compress(Bitmap.CompressFormat.JPEG, i23, new FileOutputStream(file2222222));
                                                                    SharedConfig.saveConfig();
                                                                    return file2222222.getAbsolutePath();
                                                                }
                                                            } else {
                                                                options2.inJustDecodeBounds = false;
                                                                if (file == null) {
                                                                }
                                                                if (decodeStream != null) {
                                                                }
                                                            }
                                                        }
                                                        z2 = false;
                                                        if (fileInputStream2 != null) {
                                                        }
                                                        i23 = 80;
                                                        canvas4 = canvas2;
                                                        if (z2) {
                                                        }
                                                        Paint paint42222222 = paint;
                                                        paint42222222.setColor(previewColor);
                                                        canvas4.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint42222222);
                                                        if (mutate522222 != null) {
                                                        }
                                                        if (mutate2222222 != null) {
                                                        }
                                                        messageDrawableArr222222[1].setBounds(161, 216, createBitmap.getWidth() - 20, 308);
                                                        messageDrawableArr222222[1].setTop(0, 560, 522, false, false);
                                                        messageDrawableArr222222[1].draw(canvas4);
                                                        messageDrawableArr222222[1].setBounds(161, 430, createBitmap.getWidth() - 20, 522);
                                                        messageDrawableArr222222[1].setTop(430, 560, 522, false, false);
                                                        messageDrawableArr222222[1].draw(canvas4);
                                                        messageDrawableArr222222[0].setBounds(20, 323, 399, 415);
                                                        messageDrawableArr222222[0].setTop(323, 560, 522, false, false);
                                                        messageDrawableArr222222[0].draw(canvas4);
                                                        paint42222222.setColor(previewColor3);
                                                        canvas4.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint42222222);
                                                        if (drawable2222222 != null) {
                                                        }
                                                        if (drawable322222 != null) {
                                                        }
                                                        canvas4.setBitmap(null);
                                                        File file22222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                        createBitmap.compress(Bitmap.CompressFormat.JPEG, i23, new FileOutputStream(file22222222));
                                                        SharedConfig.saveConfig();
                                                        return file22222222.getAbsolutePath();
                                                    }
                                                }
                                                i16 = i11;
                                                if (queryParameter.length() != 27) {
                                                }
                                                i14 = i7;
                                                i15 = i30;
                                                i17 = i16;
                                                int i332222222 = i13;
                                                Drawable mutate5222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                                setDrawableColor(mutate5222222, previewColor2);
                                                Drawable mutate22222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                                setDrawableColor(mutate22222222, previewColor2);
                                                Drawable mutate32222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                                setDrawableColor(mutate32222222, previewColor4);
                                                Drawable mutate42222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                                setDrawableColor(mutate42222222, previewColor4);
                                                MessageDrawable[] messageDrawableArr2222222 = new MessageDrawable[2];
                                                i19 = 0;
                                                while (i19 < i18) {
                                                }
                                                Drawable drawable3222222 = mutate42222222;
                                                Drawable drawable22222222 = mutate32222222;
                                                RectF rectF2222222 = new RectF();
                                                if (str2 != null) {
                                                }
                                            }
                                        }
                                        i13 = i12;
                                        canvas = canvas6;
                                        i14 = i7;
                                        i16 = i11;
                                        i15 = i30;
                                        i17 = i16;
                                        int i3322222222 = i13;
                                        Drawable mutate52222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                        setDrawableColor(mutate52222222, previewColor2);
                                        Drawable mutate222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                        setDrawableColor(mutate222222222, previewColor2);
                                        Drawable mutate322222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                        setDrawableColor(mutate322222222, previewColor4);
                                        Drawable mutate422222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                        setDrawableColor(mutate422222222, previewColor4);
                                        MessageDrawable[] messageDrawableArr22222222 = new MessageDrawable[2];
                                        i19 = 0;
                                        while (i19 < i18) {
                                        }
                                        Drawable drawable32222222 = mutate422222222;
                                        Drawable drawable222222222 = mutate322222222;
                                        RectF rectF22222222 = new RectF();
                                        if (str2 != null) {
                                        }
                                    } else {
                                        i13 = i12;
                                        canvas = canvas6;
                                        i15 = i30;
                                        i14 = i7;
                                    }
                                    i17 = i11;
                                    int i33222222222 = i13;
                                    Drawable mutate522222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                                    setDrawableColor(mutate522222222, previewColor2);
                                    Drawable mutate2222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                                    setDrawableColor(mutate2222222222, previewColor2);
                                    Drawable mutate3222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                                    setDrawableColor(mutate3222222222, previewColor4);
                                    Drawable mutate4222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                                    setDrawableColor(mutate4222222222, previewColor4);
                                    MessageDrawable[] messageDrawableArr222222222 = new MessageDrawable[2];
                                    i19 = 0;
                                    while (i19 < i18) {
                                    }
                                    Drawable drawable322222222 = mutate4222222222;
                                    Drawable drawable2222222222 = mutate3222222222;
                                    RectF rectF222222222 = new RectF();
                                    if (str2 != null) {
                                    }
                                }
                            }
                            i12 = i9 != 0 ? i9 : i10;
                            if (TextUtils.isEmpty(strArr[0])) {
                            }
                            i17 = i11;
                            int i332222222222 = i13;
                            Drawable mutate5222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                            setDrawableColor(mutate5222222222, previewColor2);
                            Drawable mutate22222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                            setDrawableColor(mutate22222222222, previewColor2);
                            Drawable mutate32222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                            setDrawableColor(mutate32222222222, previewColor4);
                            Drawable mutate42222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                            setDrawableColor(mutate42222222222, previewColor4);
                            MessageDrawable[] messageDrawableArr2222222222 = new MessageDrawable[2];
                            i19 = 0;
                            while (i19 < i18) {
                            }
                            Drawable drawable3222222222 = mutate42222222222;
                            Drawable drawable22222222222 = mutate32222222222;
                            RectF rectF2222222222 = new RectF();
                            if (str2 != null) {
                            }
                        }
                    }
                    if (i5 != 0) {
                        i30 = i5;
                    }
                    if (canvas5 != null) {
                    }
                    if (i8 != 0) {
                    }
                    if (i8 == 0) {
                    }
                    if (canvas5 != null) {
                    }
                    if (i9 == 0) {
                    }
                    i10 = i6;
                    i11 = i8;
                    i12 = i9 != 0 ? i9 : i10;
                    if (TextUtils.isEmpty(strArr[0])) {
                    }
                    i17 = i11;
                    int i3322222222222 = i13;
                    Drawable mutate52222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
                    setDrawableColor(mutate52222222222, previewColor2);
                    Drawable mutate222222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
                    setDrawableColor(mutate222222222222, previewColor2);
                    Drawable mutate322222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
                    setDrawableColor(mutate322222222222, previewColor4);
                    Drawable mutate422222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
                    setDrawableColor(mutate422222222222, previewColor4);
                    MessageDrawable[] messageDrawableArr22222222222 = new MessageDrawable[2];
                    i19 = 0;
                    while (i19 < i18) {
                    }
                    Drawable drawable32222222222 = mutate422222222222;
                    Drawable drawable222222222222 = mutate322222222222;
                    RectF rectF22222222222 = new RectF();
                    if (str2 != null) {
                    }
                }
            }
            i3 = i != 0 ? i : i29;
            if (canvas5 == null) {
            }
            if (i5 == 0) {
            }
            i6 = i2;
            i7 = i4;
            if (i5 != 0) {
            }
            if (canvas5 != null) {
            }
            if (i8 != 0) {
            }
            if (i8 == 0) {
            }
            if (canvas5 != null) {
            }
            if (i9 == 0) {
            }
            i10 = i6;
            i11 = i8;
            i12 = i9 != 0 ? i9 : i10;
            if (TextUtils.isEmpty(strArr[0])) {
            }
            i17 = i11;
            int i33222222222222 = i13;
            Drawable mutate522222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_back).mutate();
            setDrawableColor(mutate522222222222, previewColor2);
            Drawable mutate2222222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_dots).mutate();
            setDrawableColor(mutate2222222222222, previewColor2);
            Drawable mutate3222222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_smile).mutate();
            setDrawableColor(mutate3222222222222, previewColor4);
            Drawable mutate4222222222222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.preview_mic).mutate();
            setDrawableColor(mutate4222222222222, previewColor4);
            MessageDrawable[] messageDrawableArr222222222222 = new MessageDrawable[2];
            i19 = 0;
            while (i19 < i18) {
            }
            Drawable drawable322222222222 = mutate4222222222222;
            Drawable drawable2222222222222 = mutate3222222222222;
            RectF rectF222222222222 = new RectF();
            if (str2 != null) {
            }
        } catch (Throwable th5) {
            FileLog.e(th5);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkIsDark(SparseIntArray sparseIntArray, ThemeInfo themeInfo) {
        if (themeInfo == null || sparseIntArray == null || themeInfo.isDark != -1) {
            return;
        }
        int i = key_windowBackgroundWhite;
        if (ColorUtils.calculateLuminance(ColorUtils.blendARGB(getPreviewColor(sparseIntArray, i), getPreviewColor(sparseIntArray, i), 0.5f)) < 0.5d) {
            themeInfo.isDark = 1;
        } else {
            themeInfo.isDark = 0;
        }
    }

    public static void getThemeFileValuesInBackground(final File file, final String str, final String[] strArr, final Utilities.Callback<SparseIntArray> callback) {
        Utilities.themeQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$getThemeFileValuesInBackground$10(Utilities.Callback.this, file, str, strArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getThemeFileValuesInBackground$10(Utilities.Callback callback, File file, String str, String[] strArr) {
        callback.run(getThemeFileValues(file, str, strArr));
    }

    public static SparseIntArray getThemeFileValues(File file, String str, String[] strArr) {
        int intValue;
        SparseIntArray sparseIntArray = new SparseIntArray();
        FileInputStream fileInputStream = null;
        try {
            try {
                byte[] bArr = new byte[1024];
                FileInputStream fileInputStream2 = new FileInputStream(str != null ? getAssetFile(str) : file);
                int i = -1;
                int i2 = 0;
                int i3 = -1;
                boolean z = false;
                while (true) {
                    try {
                        int read = fileInputStream2.read(bArr);
                        if (read == i) {
                            break;
                        }
                        int i4 = i2;
                        int i5 = 0;
                        int i6 = 0;
                        while (true) {
                            if (i5 >= read) {
                                break;
                            }
                            if (bArr[i5] == 10) {
                                int i7 = (i5 - i6) + 1;
                                String str2 = new String(bArr, i6, i7 - 1);
                                if (str2.startsWith("WLS=")) {
                                    if (strArr != null && strArr.length > 0) {
                                        strArr[0] = str2.substring(4);
                                    }
                                } else if (str2.startsWith("WPS")) {
                                    i3 = i7 + i4;
                                    z = true;
                                    break;
                                } else {
                                    int indexOf = str2.indexOf(61);
                                    if (indexOf != i) {
                                        String substring = str2.substring(0, indexOf);
                                        String substring2 = str2.substring(indexOf + 1);
                                        if (substring2.length() > 0 && substring2.charAt(0) == '#') {
                                            try {
                                                intValue = Color.parseColor(substring2);
                                            } catch (Exception unused) {
                                                intValue = Utilities.parseInt((CharSequence) substring2).intValue();
                                            }
                                        } else {
                                            intValue = Utilities.parseInt((CharSequence) substring2).intValue();
                                        }
                                        int stringKeyToInt = ThemeColors.stringKeyToInt(substring);
                                        if (stringKeyToInt >= 0) {
                                            sparseIntArray.put(stringKeyToInt, intValue);
                                        }
                                    }
                                }
                                i6 += i7;
                                i4 += i7;
                            }
                            i5++;
                            i = -1;
                        }
                        if (i2 == i4) {
                            break;
                        }
                        fileInputStream2.getChannel().position(i4);
                        if (z) {
                            break;
                        }
                        i2 = i4;
                        i = -1;
                    } catch (Throwable th) {
                        th = th;
                        fileInputStream = fileInputStream2;
                        try {
                            FileLog.e(th);
                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                            return sparseIntArray;
                        } catch (Throwable th2) {
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (Exception e) {
                                    FileLog.e(e);
                                }
                            }
                            throw th2;
                        }
                    }
                }
                sparseIntArray.put(key_wallpaperFileOffset, i3);
                fileInputStream2.close();
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        } catch (Throwable th3) {
            th = th3;
        }
        return sparseIntArray;
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
            Drawable[] drawableArr = avatarDrawables;
            int i = R.drawable.msg_folders_private;
            drawableArr[2] = resources.getDrawable(i);
            avatarDrawables[3] = resources.getDrawable(R.drawable.msg_folders_requests);
            avatarDrawables[4] = resources.getDrawable(R.drawable.msg_folders_groups);
            avatarDrawables[5] = resources.getDrawable(R.drawable.msg_folders_channels);
            avatarDrawables[6] = resources.getDrawable(R.drawable.msg_folders_bots);
            avatarDrawables[7] = resources.getDrawable(R.drawable.msg_folders_muted);
            avatarDrawables[8] = resources.getDrawable(R.drawable.msg_folders_read);
            avatarDrawables[9] = resources.getDrawable(R.drawable.msg_folders_archive);
            avatarDrawables[10] = resources.getDrawable(i);
            avatarDrawables[11] = resources.getDrawable(R.drawable.chats_replies);
            avatarDrawables[12] = resources.getDrawable(R.drawable.other_chats);
            RLottieDrawable rLottieDrawable = dialogs_archiveAvatarDrawable;
            if (rLottieDrawable != null) {
                rLottieDrawable.setCallback(null);
                dialogs_archiveAvatarDrawable.recycle(false);
            }
            RLottieDrawable rLottieDrawable2 = dialogs_archiveDrawable;
            if (rLottieDrawable2 != null) {
                rLottieDrawable2.recycle(false);
            }
            RLottieDrawable rLottieDrawable3 = dialogs_unarchiveDrawable;
            if (rLottieDrawable3 != null) {
                rLottieDrawable3.recycle(false);
            }
            RLottieDrawable rLottieDrawable4 = dialogs_pinArchiveDrawable;
            if (rLottieDrawable4 != null) {
                rLottieDrawable4.recycle(false);
            }
            RLottieDrawable rLottieDrawable5 = dialogs_unpinArchiveDrawable;
            if (rLottieDrawable5 != null) {
                rLottieDrawable5.recycle(false);
            }
            RLottieDrawable rLottieDrawable6 = dialogs_hidePsaDrawable;
            if (rLottieDrawable6 != null) {
                rLottieDrawable6.recycle(false);
            }
            dialogs_archiveAvatarDrawable = new RLottieDrawable(R.raw.chats_archiveavatar, "chats_archiveavatar", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_archiveDrawable = new RLottieDrawable(R.raw.chats_archive, "chats_archive", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_unarchiveDrawable = new RLottieDrawable(R.raw.chats_unarchive, "chats_unarchive", AndroidUtilities.dp(AndroidUtilities.dp(36.0f)), AndroidUtilities.dp(36.0f), false, null);
            dialogs_pinArchiveDrawable = new RLottieDrawable(R.raw.chats_hide, "chats_hide", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_unpinArchiveDrawable = new RLottieDrawable(R.raw.chats_unhide, "chats_unhide", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_hidePsaDrawable = new RLottieDrawable(R.raw.chat_audio_record_delete, "chats_psahide", AndroidUtilities.dp(30.0f), AndroidUtilities.dp(30.0f), false, null);
            dialogs_swipeMuteDrawable = new RLottieDrawable(R.raw.swipe_mute, "swipe_mute", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_swipeUnmuteDrawable = new RLottieDrawable(R.raw.swipe_unmute, "swipe_unmute", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_swipeReadDrawable = new RLottieDrawable(R.raw.swipe_read, "swipe_read", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_swipeUnreadDrawable = new RLottieDrawable(R.raw.swipe_unread, "swipe_unread", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_swipeDeleteDrawable = new RLottieDrawable(R.raw.swipe_delete, "swipe_delete", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_swipeUnpinDrawable = new RLottieDrawable(R.raw.swipe_unpin, "swipe_unpin", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_swipePinDrawable = new RLottieDrawable(R.raw.swipe_pin, "swipe_pin", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            applyCommonTheme();
        }
    }

    public static void applyCommonTheme() {
        Paint paint = dividerPaint;
        if (paint == null) {
            return;
        }
        paint.setColor(getColor(key_divider));
        linkSelectionPaint.setColor(getColor(key_windowBackgroundWhiteLinkSelection));
        int i = 0;
        while (true) {
            Drawable[] drawableArr = avatarDrawables;
            if (i < drawableArr.length) {
                setDrawableColorByKey(drawableArr[i], key_avatar_text);
                i++;
            } else {
                dialogs_archiveAvatarDrawable.beginApplyLayerColors();
                RLottieDrawable rLottieDrawable = dialogs_archiveAvatarDrawable;
                int i2 = key_avatar_backgroundArchived;
                rLottieDrawable.setLayerColor("Arrow1.**", getNonAnimatedColor(i2));
                dialogs_archiveAvatarDrawable.setLayerColor("Arrow2.**", getNonAnimatedColor(i2));
                RLottieDrawable rLottieDrawable2 = dialogs_archiveAvatarDrawable;
                int i3 = key_avatar_text;
                rLottieDrawable2.setLayerColor("Box2.**", getNonAnimatedColor(i3));
                dialogs_archiveAvatarDrawable.setLayerColor("Box1.**", getNonAnimatedColor(i3));
                dialogs_archiveAvatarDrawable.commitApplyLayerColors();
                dialogs_archiveAvatarDrawableRecolored = false;
                dialogs_archiveAvatarDrawable.setAllowDecodeSingleFrame(true);
                dialogs_pinArchiveDrawable.beginApplyLayerColors();
                RLottieDrawable rLottieDrawable3 = dialogs_pinArchiveDrawable;
                int i4 = key_chats_archiveIcon;
                rLottieDrawable3.setLayerColor("Arrow.**", getNonAnimatedColor(i4));
                dialogs_pinArchiveDrawable.setLayerColor("Line.**", getNonAnimatedColor(i4));
                dialogs_pinArchiveDrawable.commitApplyLayerColors();
                dialogs_unpinArchiveDrawable.beginApplyLayerColors();
                dialogs_unpinArchiveDrawable.setLayerColor("Arrow.**", getNonAnimatedColor(i4));
                dialogs_unpinArchiveDrawable.setLayerColor("Line.**", getNonAnimatedColor(i4));
                dialogs_unpinArchiveDrawable.commitApplyLayerColors();
                dialogs_hidePsaDrawable.beginApplyLayerColors();
                RLottieDrawable rLottieDrawable4 = dialogs_hidePsaDrawable;
                int i5 = key_chats_archiveBackground;
                rLottieDrawable4.setLayerColor("Line 1.**", getNonAnimatedColor(i5));
                dialogs_hidePsaDrawable.setLayerColor("Line 2.**", getNonAnimatedColor(i5));
                dialogs_hidePsaDrawable.setLayerColor("Line 3.**", getNonAnimatedColor(i5));
                dialogs_hidePsaDrawable.setLayerColor("Cup Red.**", getNonAnimatedColor(i4));
                dialogs_hidePsaDrawable.setLayerColor("Box.**", getNonAnimatedColor(i4));
                dialogs_hidePsaDrawable.commitApplyLayerColors();
                dialogs_hidePsaDrawableRecolored = false;
                dialogs_archiveDrawable.beginApplyLayerColors();
                dialogs_archiveDrawable.setLayerColor("Arrow.**", getNonAnimatedColor(i5));
                dialogs_archiveDrawable.setLayerColor("Box2.**", getNonAnimatedColor(i4));
                dialogs_archiveDrawable.setLayerColor("Box1.**", getNonAnimatedColor(i4));
                dialogs_archiveDrawable.commitApplyLayerColors();
                dialogs_archiveDrawableRecolored = false;
                dialogs_unarchiveDrawable.beginApplyLayerColors();
                dialogs_unarchiveDrawable.setLayerColor("Arrow1.**", getNonAnimatedColor(i4));
                dialogs_unarchiveDrawable.setLayerColor("Arrow2.**", getNonAnimatedColor(key_chats_archivePinBackground));
                dialogs_unarchiveDrawable.setLayerColor("Box2.**", getNonAnimatedColor(i4));
                dialogs_unarchiveDrawable.setLayerColor("Box1.**", getNonAnimatedColor(i4));
                dialogs_unarchiveDrawable.commitApplyLayerColors();
                chat_animatedEmojiTextColorFilter = new PorterDuffColorFilter(getColor(key_windowBackgroundWhiteBlackText), PorterDuff.Mode.SRC_IN);
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
            dialogs_lock2Drawable = resources.getDrawable(R.drawable.msg_mini_lock2);
            int i2 = R.drawable.list_check;
            dialogs_checkDrawable = resources.getDrawable(i2).mutate();
            dialogs_playDrawable = resources.getDrawable(R.drawable.minithumb_play).mutate();
            dialogs_checkReadDrawable = resources.getDrawable(i2).mutate();
            dialogs_halfCheckDrawable = resources.getDrawable(R.drawable.list_halfcheck);
            dialogs_clockDrawable = new MsgClockDrawable();
            dialogs_errorDrawable = resources.getDrawable(R.drawable.list_warning_sign);
            dialogs_reorderDrawable = resources.getDrawable(R.drawable.list_reorder).mutate();
            dialogs_muteDrawable = resources.getDrawable(R.drawable.list_mute).mutate();
            dialogs_unmuteDrawable = resources.getDrawable(R.drawable.list_unmute).mutate();
            dialogs_verifiedDrawable = resources.getDrawable(R.drawable.verified_area).mutate();
            dialogs_scamDrawable = new ScamDrawable(11, 0);
            dialogs_fakeDrawable = new ScamDrawable(11, 1);
            dialogs_verifiedCheckDrawable = resources.getDrawable(R.drawable.verified_check).mutate();
            dialogs_mentionDrawable = resources.getDrawable(R.drawable.mentionchatslist);
            dialogs_reactionsMentionDrawable = resources.getDrawable(R.drawable.reactionchatslist);
            dialogs_pinnedDrawable = resources.getDrawable(R.drawable.list_pin);
            dialogs_forum_arrowDrawable = resources.getDrawable(R.drawable.msg_mini_forumarrow);
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
            dialogs_namePaint[i].setColor(getColor(key_chats_name));
            dialogs_nameEncryptedPaint[i].setColor(getColor(key_chats_secretName));
            TextPaint[] textPaintArr = dialogs_messagePaint;
            TextPaint textPaint = textPaintArr[i];
            TextPaint textPaint2 = textPaintArr[i];
            int color = getColor(key_chats_message);
            textPaint2.linkColor = color;
            textPaint.setColor(color);
            dialogs_messagePrintingPaint[i].setColor(getColor(key_chats_actionMessage));
        }
        dialogs_searchNamePaint.setColor(getColor(key_chats_name));
        dialogs_searchNameEncryptedPaint.setColor(getColor(key_chats_secretName));
        TextPaint textPaint3 = dialogs_messageNamePaint;
        int color2 = getColor(key_chats_nameMessage_threeLines);
        textPaint3.linkColor = color2;
        textPaint3.setColor(color2);
        dialogs_tabletSeletedPaint.setColor(getColor(key_chats_tabletSelectedOverlay));
        dialogs_pinnedPaint.setColor(getColor(key_chats_pinnedOverlay));
        dialogs_timePaint.setColor(getColor(key_chats_date));
        dialogs_countTextPaint.setColor(getColor(key_chats_unreadCounterText));
        TextPaint textPaint4 = dialogs_archiveTextPaint;
        int i2 = key_chats_archiveText;
        textPaint4.setColor(getColor(i2));
        dialogs_archiveTextPaintSmall.setColor(getColor(i2));
        dialogs_countPaint.setColor(getColor(key_chats_unreadCounter));
        dialogs_reactionsCountPaint.setColor(getColor(key_dialogReactionMentionBackground));
        dialogs_countGrayPaint.setColor(getColor(key_chats_unreadCounterMuted));
        dialogs_actionMessagePaint.setColor(getColor(key_chats_actionMessage));
        dialogs_errorPaint.setColor(getColor(key_chats_sentError));
        dialogs_onlinePaint.setColor(getColor(key_windowBackgroundWhiteBlueText3));
        dialogs_offlinePaint.setColor(getColor(key_windowBackgroundWhiteGrayText3));
        setDrawableColorByKey(dialogs_lockDrawable, key_chats_secretIcon);
        Drawable drawable = dialogs_lock2Drawable;
        int i3 = key_chats_pinnedIcon;
        setDrawableColorByKey(drawable, i3);
        setDrawableColorByKey(dialogs_checkDrawable, key_chats_sentCheck);
        Drawable drawable2 = dialogs_checkReadDrawable;
        int i4 = key_chats_sentReadCheck;
        setDrawableColorByKey(drawable2, i4);
        setDrawableColorByKey(dialogs_halfCheckDrawable, i4);
        setDrawableColorByKey(dialogs_clockDrawable, key_chats_sentClock);
        setDrawableColorByKey(dialogs_errorDrawable, key_chats_sentErrorIcon);
        setDrawableColorByKey(dialogs_pinnedDrawable, i3);
        setDrawableColorByKey(dialogs_reorderDrawable, i3);
        Drawable drawable3 = dialogs_muteDrawable;
        int i5 = key_chats_muteIcon;
        setDrawableColorByKey(drawable3, i5);
        setDrawableColorByKey(dialogs_unmuteDrawable, i5);
        Drawable drawable4 = dialogs_mentionDrawable;
        int i6 = key_chats_mentionIcon;
        setDrawableColorByKey(drawable4, i6);
        setDrawableColorByKey(dialogs_forum_arrowDrawable, key_chats_message);
        setDrawableColorByKey(dialogs_reactionsMentionDrawable, i6);
        setDrawableColorByKey(dialogs_verifiedDrawable, key_chats_verifiedBackground);
        setDrawableColorByKey(dialogs_verifiedCheckDrawable, key_chats_verifiedCheck);
        setDrawableColorByKey(dialogs_holidayDrawable, key_actionBarDefaultTitle);
        ScamDrawable scamDrawable = dialogs_scamDrawable;
        int i7 = key_chats_draft;
        setDrawableColorByKey(scamDrawable, i7);
        setDrawableColorByKey(dialogs_fakeDrawable, i7);
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
                chat_msgTextPaintEmoji = new TextPaint[6];
                chat_msgTextPaintOneEmoji = new TextPaint(1);
                chat_msgTextPaintTwoEmoji = new TextPaint(1);
                chat_msgTextPaintThreeEmoji = new TextPaint(1);
                TextPaint textPaint = new TextPaint(1);
                chat_msgBotButtonPaint = textPaint;
                textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                TextPaint textPaint2 = new TextPaint(1);
                chat_namePaint = textPaint2;
                textPaint2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                TextPaint textPaint3 = new TextPaint(1);
                chat_replyNamePaint = textPaint3;
                textPaint3.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                chat_replyTextPaint = new TextPaint(1);
                TextPaint textPaint4 = new TextPaint(1);
                chat_topicTextPaint = textPaint4;
                textPaint4.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                chat_forwardNamePaint = new TextPaint(1);
                chat_adminPaint = new TextPaint(1);
                chat_timePaint = new TextPaint(1);
            }
            int i = 0;
            float[] fArr = {0.68f, 0.46f, 0.34f, 0.28f, 0.22f, 0.19f};
            while (true) {
                TextPaint[] textPaintArr = chat_msgTextPaintEmoji;
                if (i < textPaintArr.length) {
                    textPaintArr[i] = new TextPaint(1);
                    chat_msgTextPaintEmoji[i].setTextSize(AndroidUtilities.dp(fArr[i] * 120.0f));
                    i++;
                } else {
                    chat_msgTextPaintOneEmoji.setTextSize(AndroidUtilities.dp(46.0f));
                    chat_msgTextPaintTwoEmoji.setTextSize(AndroidUtilities.dp(38.0f));
                    chat_msgTextPaintThreeEmoji.setTextSize(AndroidUtilities.dp(30.0f));
                    chat_msgTextPaint.setTextSize(AndroidUtilities.dp(SharedConfig.fontSize));
                    chat_msgGameTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
                    chat_msgBotButtonPaint.setTextSize(AndroidUtilities.dp(15.0f));
                    float f = ((SharedConfig.fontSize * 2) + 10) / 3.0f;
                    chat_namePaint.setTextSize(AndroidUtilities.dp(f));
                    chat_replyNamePaint.setTextSize(AndroidUtilities.dp(f));
                    chat_replyTextPaint.setTextSize(AndroidUtilities.dp(f));
                    float f2 = f - 1.0f;
                    chat_topicTextPaint.setTextSize(AndroidUtilities.dp(f2));
                    chat_forwardNamePaint.setTextSize(AndroidUtilities.dp(f));
                    chat_adminPaint.setTextSize(AndroidUtilities.dp(f2));
                }
            }
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
            TextPaint textPaint3 = new TextPaint(1);
            chat_locationTitlePaint = textPaint3;
            textPaint3.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            chat_locationAddressPaint = new TextPaint(1);
            Paint paint = new Paint();
            chat_urlPaint = paint;
            paint.setPathEffect(LinkPath.getRoundedEffect());
            Paint paint2 = new Paint();
            chat_outUrlPaint = paint2;
            paint2.setPathEffect(LinkPath.getRoundedEffect());
            Paint paint3 = new Paint();
            chat_textSearchSelectionPaint = paint3;
            paint3.setPathEffect(LinkPath.getRoundedEffect());
            Paint paint4 = new Paint(1);
            chat_radialProgressPaint = paint4;
            paint4.setStrokeCap(Paint.Cap.ROUND);
            chat_radialProgressPaint.setStyle(Paint.Style.STROKE);
            chat_radialProgressPaint.setColor(-1610612737);
            Paint paint5 = new Paint(1);
            chat_radialProgress2Paint = paint5;
            paint5.setStrokeCap(Paint.Cap.ROUND);
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
            chat_topicTextPaint = textPaint11;
            textPaint11.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            chat_commentTextPaint = new TextPaint(1);
            TextPaint textPaint12 = new TextPaint(1);
            chat_instantViewPaint = textPaint12;
            textPaint12.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            Paint paint6 = new Paint(1);
            chat_instantViewRectPaint = paint6;
            paint6.setStyle(Paint.Style.STROKE);
            chat_instantViewRectPaint.setStrokeCap(Paint.Cap.ROUND);
            Paint paint7 = new Paint(1);
            chat_pollTimerPaint = paint7;
            paint7.setStyle(Paint.Style.STROKE);
            chat_pollTimerPaint.setStrokeCap(Paint.Cap.ROUND);
            chat_replyLinePaint = new Paint(1);
            chat_msgErrorPaint = new Paint(1);
            chat_statusPaint = new Paint(1);
            Paint paint8 = new Paint(1);
            chat_statusRecordPaint = paint8;
            paint8.setStyle(Paint.Style.STROKE);
            chat_statusRecordPaint.setStrokeCap(Paint.Cap.ROUND);
            chat_actionTextPaint = new TextPaint(1);
            chat_actionTextPaint2 = new TextPaint(1);
            chat_actionTextPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            TextPaint textPaint13 = new TextPaint(1);
            chat_unlockExtendedMediaTextPaint = textPaint13;
            textPaint13.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            Paint paint9 = new Paint(1);
            chat_actionBackgroundGradientDarkenPaint = paint9;
            paint9.setColor(704643072);
            chat_timeBackgroundPaint = new Paint(1);
            TextPaint textPaint14 = new TextPaint(1);
            chat_contextResult_titleTextPaint = textPaint14;
            textPaint14.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            chat_contextResult_descriptionTextPaint = new TextPaint(1);
            chat_composeBackgroundPaint = new Paint();
            new Paint(1);
            chat_radialProgressPausedSeekbarPaint = new Paint(1);
            chat_messageBackgroundSelectedPaint = new Paint(1);
            chat_actionBackgroundPaint = new Paint(1);
            chat_actionBackgroundSelectedPaint = new Paint(1);
            chat_actionBackgroundPaint2 = new Paint(1);
            new Paint(1);
            addChatPaint("paintChatMessageBackgroundSelected", chat_messageBackgroundSelectedPaint, key_chat_selectedBackground);
            addChatPaint("paintChatActionBackground", chat_actionBackgroundPaint, key_chat_serviceBackground);
            addChatPaint("paintChatActionBackgroundSelected", chat_actionBackgroundSelectedPaint, key_chat_serviceBackgroundSelected);
            TextPaint textPaint15 = chat_actionTextPaint;
            int i = key_chat_serviceText;
            addChatPaint("paintChatActionText", textPaint15, i);
            addChatPaint("paintChatActionText2", chat_actionTextPaint2, i);
            addChatPaint("paintChatBotButton", chat_botButtonPaint, key_chat_botButtonText);
            addChatPaint("paintChatComposeBackground", chat_composeBackgroundPaint, key_chat_messagePanelBackground);
            addChatPaint("paintChatTimeBackground", chat_timeBackgroundPaint, key_chat_mediaTimeBackground);
        }
    }

    public static void createChatResources(Context context, boolean z) {
        TextPaint textPaint;
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
            int i = R.drawable.msg_check_s;
            chat_msgOutCheckDrawable = resources.getDrawable(i).mutate();
            chat_msgOutCheckSelectedDrawable = resources.getDrawable(i).mutate();
            chat_msgOutCheckReadDrawable = resources.getDrawable(i).mutate();
            chat_msgOutCheckReadSelectedDrawable = resources.getDrawable(i).mutate();
            chat_msgMediaCheckDrawable = resources.getDrawable(i).mutate();
            chat_msgStickerCheckDrawable = resources.getDrawable(i).mutate();
            int i2 = R.drawable.msg_halfcheck;
            chat_msgOutHalfCheckDrawable = resources.getDrawable(i2).mutate();
            chat_msgOutHalfCheckSelectedDrawable = resources.getDrawable(i2).mutate();
            int i3 = R.drawable.msg_halfcheck_s;
            chat_msgMediaHalfCheckDrawable = resources.getDrawable(i3).mutate();
            chat_msgStickerHalfCheckDrawable = resources.getDrawable(i3).mutate();
            chat_msgClockDrawable = new MsgClockDrawable();
            int i4 = R.drawable.ic_lock_header;
            chat_msgUnlockDrawable = resources.getDrawable(i4).mutate();
            int i5 = R.drawable.msg_views;
            chat_msgInViewsDrawable = resources.getDrawable(i5).mutate();
            chat_msgInViewsSelectedDrawable = resources.getDrawable(i5).mutate();
            chat_msgOutViewsDrawable = resources.getDrawable(i5).mutate();
            chat_msgOutViewsSelectedDrawable = resources.getDrawable(i5).mutate();
            int i6 = R.drawable.msg_reply_small;
            chat_msgInRepliesDrawable = resources.getDrawable(i6).mutate();
            chat_msgInRepliesSelectedDrawable = resources.getDrawable(i6).mutate();
            chat_msgOutRepliesDrawable = resources.getDrawable(i6).mutate();
            chat_msgOutRepliesSelectedDrawable = resources.getDrawable(i6).mutate();
            int i7 = R.drawable.msg_pin_mini;
            chat_msgInPinnedDrawable = resources.getDrawable(i7).mutate();
            chat_msgInPinnedSelectedDrawable = resources.getDrawable(i7).mutate();
            chat_msgOutPinnedDrawable = resources.getDrawable(i7).mutate();
            chat_msgOutPinnedSelectedDrawable = resources.getDrawable(i7).mutate();
            chat_msgMediaPinnedDrawable = resources.getDrawable(i7).mutate();
            chat_msgStickerPinnedDrawable = resources.getDrawable(i7).mutate();
            chat_msgMediaViewsDrawable = resources.getDrawable(i5).mutate();
            chat_msgMediaRepliesDrawable = resources.getDrawable(i6).mutate();
            chat_msgStickerViewsDrawable = resources.getDrawable(i5).mutate();
            chat_msgStickerRepliesDrawable = resources.getDrawable(i6).mutate();
            int i8 = R.drawable.msg_actions;
            chat_msgInMenuDrawable = resources.getDrawable(i8).mutate();
            chat_msgInMenuSelectedDrawable = resources.getDrawable(i8).mutate();
            chat_msgOutMenuDrawable = resources.getDrawable(i8).mutate();
            chat_msgOutMenuSelectedDrawable = resources.getDrawable(i8).mutate();
            chat_msgMediaMenuDrawable = resources.getDrawable(R.drawable.video_actions);
            int i9 = R.drawable.msg_instant;
            chat_msgInInstantDrawable = resources.getDrawable(i9).mutate();
            chat_msgOutInstantDrawable = resources.getDrawable(i9).mutate();
            chat_msgErrorDrawable = resources.getDrawable(R.drawable.msg_warning);
            chat_muteIconDrawable = resources.getDrawable(R.drawable.list_mute).mutate();
            chat_lockIconDrawable = resources.getDrawable(i4);
            Drawable[] drawableArr = chat_msgInCallDrawable;
            int i10 = R.drawable.chat_calls_voice;
            drawableArr[0] = resources.getDrawable(i10).mutate();
            chat_msgInCallSelectedDrawable[0] = resources.getDrawable(i10).mutate();
            chat_msgOutCallDrawable[0] = resources.getDrawable(i10).mutate();
            chat_msgOutCallSelectedDrawable[0] = resources.getDrawable(i10).mutate();
            Drawable[] drawableArr2 = chat_msgInCallDrawable;
            int i11 = R.drawable.chat_calls_video;
            drawableArr2[1] = resources.getDrawable(i11).mutate();
            chat_msgInCallSelectedDrawable[1] = resources.getDrawable(i11).mutate();
            chat_msgOutCallDrawable[1] = resources.getDrawable(i11).mutate();
            chat_msgOutCallSelectedDrawable[1] = resources.getDrawable(i11).mutate();
            chat_msgCallUpGreenDrawable = resources.getDrawable(R.drawable.chat_calls_outgoing).mutate();
            int i12 = R.drawable.chat_calls_incoming;
            chat_msgCallDownRedDrawable = resources.getDrawable(i12).mutate();
            chat_msgCallDownGreenDrawable = resources.getDrawable(i12).mutate();
            for (int i13 = 0; i13 < 2; i13++) {
                chat_pollCheckDrawable[i13] = resources.getDrawable(R.drawable.poll_right).mutate();
                chat_pollCrossDrawable[i13] = resources.getDrawable(R.drawable.poll_wrong).mutate();
                chat_pollHintDrawable[i13] = resources.getDrawable(R.drawable.msg_emoji_objects).mutate();
                chat_psaHelpDrawable[i13] = resources.getDrawable(R.drawable.msg_psa).mutate();
            }
            int i14 = R.drawable.ic_call_made_green_18dp;
            calllog_msgCallUpRedDrawable = resources.getDrawable(i14).mutate();
            calllog_msgCallUpGreenDrawable = resources.getDrawable(i14).mutate();
            int i15 = R.drawable.ic_call_received_green_18dp;
            calllog_msgCallDownRedDrawable = resources.getDrawable(i15).mutate();
            calllog_msgCallDownGreenDrawable = resources.getDrawable(i15).mutate();
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
            chat_gradientLeftDrawable = resources.getDrawable(R.drawable.gradient_left);
            chat_gradientRightDrawable = resources.getDrawable(R.drawable.gradient_right);
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
            int i16 = dp * 2;
            rectF.set(AndroidUtilities.dp(21.0f) - i16, AndroidUtilities.dp(19.0f) - dp, AndroidUtilities.dp(21.0f), AndroidUtilities.dp(19.0f) + dp);
            chat_filePath[0].arcTo(rectF, 0.0f, 90.0f, false);
            chat_filePath[0].lineTo(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(21.0f));
            rectF.set(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(19.0f) - dp, AndroidUtilities.dp(5.0f) + i16, AndroidUtilities.dp(19.0f) + dp);
            chat_filePath[0].arcTo(rectF, 90.0f, 90.0f, false);
            chat_filePath[0].lineTo(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(4.0f));
            rectF.set(AndroidUtilities.dp(5.0f), AndroidUtilities.dp(3.0f), AndroidUtilities.dp(5.0f) + i16, AndroidUtilities.dp(3.0f) + i16);
            chat_filePath[0].arcTo(rectF, 180.0f, 90.0f, false);
            chat_filePath[0].close();
            chat_filePath[1] = new Path();
            chat_filePath[1].moveTo(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(5.0f));
            chat_filePath[1].lineTo(AndroidUtilities.dp(19.0f), AndroidUtilities.dp(10.0f));
            chat_filePath[1].lineTo(AndroidUtilities.dp(14.0f), AndroidUtilities.dp(10.0f));
            chat_filePath[1].close();
            chat_flameIcon = resources.getDrawable(R.drawable.burn).mutate();
            chat_gifIcon = resources.getDrawable(R.drawable.msg_round_gif_m).mutate();
            Drawable[] drawableArr3 = chat_fileStatesDrawable[0];
            int dp2 = AndroidUtilities.dp(44.0f);
            int i17 = R.drawable.msg_round_play_m;
            drawableArr3[0] = createCircleDrawableWithIcon(dp2, i17);
            chat_fileStatesDrawable[0][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), i17);
            Drawable[] drawableArr4 = chat_fileStatesDrawable[1];
            int dp3 = AndroidUtilities.dp(44.0f);
            int i18 = R.drawable.msg_round_pause_m;
            drawableArr4[0] = createCircleDrawableWithIcon(dp3, i18);
            chat_fileStatesDrawable[1][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), i18);
            Drawable[] drawableArr5 = chat_fileStatesDrawable[2];
            int dp4 = AndroidUtilities.dp(44.0f);
            int i19 = R.drawable.msg_round_load_m;
            drawableArr5[0] = createCircleDrawableWithIcon(dp4, i19);
            chat_fileStatesDrawable[2][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), i19);
            Drawable[] drawableArr6 = chat_fileStatesDrawable[3];
            int dp5 = AndroidUtilities.dp(44.0f);
            int i20 = R.drawable.msg_round_file_s;
            drawableArr6[0] = createCircleDrawableWithIcon(dp5, i20);
            chat_fileStatesDrawable[3][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), i20);
            Drawable[] drawableArr7 = chat_fileStatesDrawable[4];
            int dp6 = AndroidUtilities.dp(44.0f);
            int i21 = R.drawable.msg_round_cancel_m;
            drawableArr7[0] = createCircleDrawableWithIcon(dp6, i21);
            chat_fileStatesDrawable[4][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), i21);
            Drawable[] drawableArr8 = chat_contactDrawable;
            int dp7 = AndroidUtilities.dp(44.0f);
            int i22 = R.drawable.msg_contact;
            drawableArr8[0] = createCircleDrawableWithIcon(dp7, i22);
            chat_contactDrawable[1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), i22);
            Drawable[] drawableArr9 = chat_locationDrawable;
            int i23 = R.drawable.msg_location;
            drawableArr9[0] = resources.getDrawable(i23).mutate();
            chat_locationDrawable[1] = resources.getDrawable(i23).mutate();
            chat_composeShadowDrawable = context.getResources().getDrawable(R.drawable.compose_panel_shadow).mutate();
            chat_composeShadowRoundDrawable = context.getResources().getDrawable(R.drawable.sheet_shadow_round).mutate();
            try {
                int dp8 = AndroidUtilities.roundMessageSize + AndroidUtilities.dp(6.0f);
                Bitmap createBitmap = Bitmap.createBitmap(dp8, dp8, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                Paint paint = new Paint(1);
                paint.setColor(0);
                paint.setStyle(Paint.Style.FILL);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                Paint paint2 = new Paint(1);
                paint2.setShadowLayer(AndroidUtilities.dp(4.0f), 0.0f, 0.0f, 1593835520);
                int i24 = 0;
                while (i24 < 2) {
                    canvas.drawCircle(dp8 / 2, dp8 / 2, (AndroidUtilities.roundMessageSize / 2) - AndroidUtilities.dp(1.0f), i24 == 0 ? paint2 : paint);
                    i24++;
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
            Drawable drawable = chat_botInlineDrawable;
            int i25 = key_chat_serviceIcon;
            addChatDrawable("drawableBotInline", drawable, i25);
            addChatDrawable("drawableBotWebView", chat_botWebViewDrawable, i25);
            addChatDrawable("drawableBotLink", chat_botLinkDrawable, i25);
            addChatDrawable("drawable_botInvite", chat_botInviteDrawable, i25);
            addChatDrawable("drawableGoIcon", chat_goIconDrawable, i25);
            addChatDrawable("drawableCommentSticker", chat_commentStickerDrawable, i25);
            addChatDrawable("drawableMsgError", chat_msgErrorDrawable, key_chat_sentErrorIcon);
            addChatDrawable("drawableMsgIn", chat_msgInDrawable, -1);
            addChatDrawable("drawableMsgInSelected", chat_msgInSelectedDrawable, -1);
            addChatDrawable("drawableMsgInMedia", chat_msgInMediaDrawable, -1);
            addChatDrawable("drawableMsgInMediaSelected", chat_msgInMediaSelectedDrawable, -1);
            addChatDrawable("drawableMsgInInstant", chat_msgInInstantDrawable, key_chat_inInstant);
            addChatDrawable("drawableMsgOut", chat_msgOutDrawable, -1);
            addChatDrawable("drawableMsgOutSelected", chat_msgOutSelectedDrawable, -1);
            addChatDrawable("drawableMsgOutMedia", chat_msgOutMediaDrawable, -1);
            addChatDrawable("drawableMsgOutMediaSelected", chat_msgOutMediaSelectedDrawable, -1);
            Drawable drawable2 = chat_msgOutCallDrawable[0];
            int i26 = key_chat_outInstant;
            addChatDrawable("drawableMsgOutCallAudio", drawable2, i26);
            Drawable drawable3 = chat_msgOutCallSelectedDrawable[0];
            int i27 = key_chat_outInstantSelected;
            addChatDrawable("drawableMsgOutCallAudioSelected", drawable3, i27);
            addChatDrawable("drawableMsgOutCallVideo", chat_msgOutCallDrawable[1], i26);
            addChatDrawable("drawableMsgOutCallVideo", chat_msgOutCallSelectedDrawable[1], i27);
            addChatDrawable("drawableMsgOutCheck", chat_msgOutCheckDrawable, key_chat_outSentCheck);
            addChatDrawable("drawableMsgOutCheckSelected", chat_msgOutCheckSelectedDrawable, key_chat_outSentCheckSelected);
            Drawable drawable4 = chat_msgOutCheckReadDrawable;
            int i28 = key_chat_outSentCheckRead;
            addChatDrawable("drawableMsgOutCheckRead", drawable4, i28);
            Drawable drawable5 = chat_msgOutCheckReadSelectedDrawable;
            int i29 = key_chat_outSentCheckReadSelected;
            addChatDrawable("drawableMsgOutCheckReadSelected", drawable5, i29);
            addChatDrawable("drawableMsgOutHalfCheck", chat_msgOutHalfCheckDrawable, i28);
            addChatDrawable("drawableMsgOutHalfCheckSelected", chat_msgOutHalfCheckSelectedDrawable, i29);
            addChatDrawable("drawableMsgOutInstant", chat_msgOutInstantDrawable, i26);
            addChatDrawable("drawableMsgOutMenu", chat_msgOutMenuDrawable, key_chat_outMenu);
            addChatDrawable("drawableMsgOutMenuSelected", chat_msgOutMenuSelectedDrawable, key_chat_outMenuSelected);
            Drawable drawable6 = chat_msgOutPinnedDrawable;
            int i30 = key_chat_outViews;
            addChatDrawable("drawableMsgOutPinned", drawable6, i30);
            Drawable drawable7 = chat_msgOutPinnedSelectedDrawable;
            int i31 = key_chat_outViewsSelected;
            addChatDrawable("drawableMsgOutPinnedSelected", drawable7, i31);
            addChatDrawable("drawableMsgOutReplies", chat_msgOutRepliesDrawable, i30);
            addChatDrawable("drawableMsgOutReplies", chat_msgOutRepliesSelectedDrawable, i31);
            addChatDrawable("drawableMsgOutViews", chat_msgOutViewsDrawable, i30);
            addChatDrawable("drawableMsgOutViewsSelected", chat_msgOutViewsSelectedDrawable, i31);
            Drawable drawable8 = chat_msgStickerCheckDrawable;
            int i32 = key_chat_serviceText;
            addChatDrawable("drawableMsgStickerCheck", drawable8, i32);
            addChatDrawable("drawableMsgStickerHalfCheck", chat_msgStickerHalfCheckDrawable, i32);
            addChatDrawable("drawableMsgStickerPinned", chat_msgStickerPinnedDrawable, i32);
            addChatDrawable("drawableMsgStickerReplies", chat_msgStickerRepliesDrawable, i32);
            addChatDrawable("drawableMsgStickerViews", chat_msgStickerViewsDrawable, i32);
            addChatDrawable("drawableReplyIcon", chat_replyIconDrawable, i25);
            addChatDrawable("drawableShareIcon", chat_shareIconDrawable, i25);
            addChatDrawable("drawableMuteIcon", chat_muteIconDrawable, key_chat_muteIcon);
            addChatDrawable("drawableLockIcon", chat_lockIconDrawable, key_chat_lockIcon);
            addChatDrawable("drawable_chat_pollHintDrawableOut", chat_pollHintDrawable[1], key_chat_outPreviewInstantText);
            addChatDrawable("drawable_chat_pollHintDrawableIn", chat_pollHintDrawable[0], key_chat_inPreviewInstantText);
            applyChatTheme(z, false);
        }
        if (z || (textPaint = chat_infoPaint) == null) {
            return;
        }
        textPaint.setTextSize(AndroidUtilities.dp(12.0f));
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
        float f = ((SharedConfig.fontSize * 2) + 10) / 3.0f;
        chat_namePaint.setTextSize(AndroidUtilities.dp(f));
        chat_replyNamePaint.setTextSize(AndroidUtilities.dp(f));
        chat_replyTextPaint.setTextSize(AndroidUtilities.dp(f));
        float f2 = f - 1.0f;
        chat_topicTextPaint.setTextSize(AndroidUtilities.dp(f2));
        chat_forwardNamePaint.setTextSize(AndroidUtilities.dp(f));
        chat_adminPaint.setTextSize(AndroidUtilities.dp(f2));
        int i33 = SharedConfig.fontSize;
        chat_timePaint.setTextSize(AndroidUtilities.dp(12.0f));
        chat_gamePaint.setTextSize(AndroidUtilities.dp(13.0f));
        chat_shipmentPaint.setTextSize(AndroidUtilities.dp(13.0f));
        chat_instantViewPaint.setTextSize(AndroidUtilities.dp(13.0f));
        chat_instantViewRectPaint.setStrokeWidth(AndroidUtilities.dp(1.0f));
        chat_pollTimerPaint.setStrokeWidth(AndroidUtilities.dp(1.1f));
        chat_actionTextPaint.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize) - 2));
        chat_actionTextPaint2.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize) - 2));
        chat_unlockExtendedMediaTextPaint.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize)));
        chat_contextResult_titleTextPaint.setTextSize(AndroidUtilities.dp(15.0f));
        chat_contextResult_descriptionTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
        chat_radialProgressPaint.setStrokeWidth(AndroidUtilities.dp(3.0f));
        chat_radialProgress2Paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
        chat_commentTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
        chat_commentTextPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
    }

    public static void refreshAttachButtonsColors() {
        int i = 0;
        while (true) {
            RLottieDrawable[] rLottieDrawableArr = chat_attachButtonDrawables;
            if (i >= rLottieDrawableArr.length) {
                return;
            }
            if (rLottieDrawableArr[i] != null) {
                rLottieDrawableArr[i].beginApplyLayerColors();
                if (i == 0) {
                    RLottieDrawable rLottieDrawable = chat_attachButtonDrawables[i];
                    int i2 = key_chat_attachGalleryBackground;
                    rLottieDrawable.setLayerColor("Color_Mount.**", getNonAnimatedColor(i2));
                    chat_attachButtonDrawables[i].setLayerColor("Color_PhotoShadow.**", getNonAnimatedColor(i2));
                    RLottieDrawable rLottieDrawable2 = chat_attachButtonDrawables[i];
                    int i3 = key_chat_attachIcon;
                    rLottieDrawable2.setLayerColor("White_Photo.**", getNonAnimatedColor(i3));
                    chat_attachButtonDrawables[i].setLayerColor("White_BackPhoto.**", getNonAnimatedColor(i3));
                } else if (i == 1) {
                    RLottieDrawable rLottieDrawable3 = chat_attachButtonDrawables[i];
                    int i4 = key_chat_attachIcon;
                    rLottieDrawable3.setLayerColor("White_Play1.**", getNonAnimatedColor(i4));
                    chat_attachButtonDrawables[i].setLayerColor("White_Play2.**", getNonAnimatedColor(i4));
                } else if (i == 2) {
                    chat_attachButtonDrawables[i].setLayerColor("Color_Corner.**", getNonAnimatedColor(key_chat_attachFileBackground));
                    chat_attachButtonDrawables[i].setLayerColor("White_List.**", getNonAnimatedColor(key_chat_attachIcon));
                } else if (i == 3) {
                    RLottieDrawable rLottieDrawable4 = chat_attachButtonDrawables[i];
                    int i5 = key_chat_attachIcon;
                    rLottieDrawable4.setLayerColor("White_User1.**", getNonAnimatedColor(i5));
                    chat_attachButtonDrawables[i].setLayerColor("White_User2.**", getNonAnimatedColor(i5));
                } else if (i == 4) {
                    chat_attachButtonDrawables[i].setLayerColor("Color_Oval.**", getNonAnimatedColor(key_chat_attachLocationBackground));
                    chat_attachButtonDrawables[i].setLayerColor("White_Pin.**", getNonAnimatedColor(key_chat_attachIcon));
                } else if (i == 5) {
                    RLottieDrawable rLottieDrawable5 = chat_attachButtonDrawables[i];
                    int i6 = key_chat_attachIcon;
                    rLottieDrawable5.setLayerColor("White_Column 1.**", getNonAnimatedColor(i6));
                    chat_attachButtonDrawables[i].setLayerColor("White_Column 2.**", getNonAnimatedColor(i6));
                    chat_attachButtonDrawables[i].setLayerColor("White_Column 3.**", getNonAnimatedColor(i6));
                }
                chat_attachButtonDrawables[i].commitApplyLayerColors();
            }
            i++;
        }
    }

    public static void applyChatTheme(boolean z, boolean z2) {
        if (chat_msgTextPaint == null || chat_msgInDrawable == null || z) {
            return;
        }
        chat_gamePaint.setColor(getColor(key_chat_previewGameText));
        chat_durationPaint.setColor(getColor(key_chat_previewDurationText));
        chat_botButtonPaint.setColor(getColor(key_chat_botButtonText));
        chat_urlPaint.setColor(getColor(key_chat_linkSelectBackground));
        chat_outUrlPaint.setColor(getColor(key_chat_outLinkSelectBackground));
        chat_deleteProgressPaint.setColor(getColor(key_chat_secretTimeText));
        chat_textSearchSelectionPaint.setColor(getColor(key_chat_textSelectBackground));
        chat_msgErrorPaint.setColor(getColor(key_chat_sentError));
        Paint paint = chat_statusPaint;
        int i = key_chat_status;
        paint.setColor(getColor(i));
        chat_statusRecordPaint.setColor(getColor(i));
        TextPaint textPaint = chat_actionTextPaint;
        int i2 = key_chat_serviceText;
        textPaint.setColor(getColor(i2));
        chat_actionTextPaint2.setColor(getColor(i2));
        chat_actionTextPaint.linkColor = getColor(key_chat_serviceLink);
        chat_unlockExtendedMediaTextPaint.setColor(getColor(i2));
        chat_contextResult_titleTextPaint.setColor(getColor(key_windowBackgroundWhiteBlackText));
        chat_composeBackgroundPaint.setColor(getColor(key_chat_messagePanelBackground));
        chat_timeBackgroundPaint.setColor(getColor(key_chat_mediaTimeBackground));
        setDrawableColorByKey(chat_msgNoSoundDrawable, key_chat_mediaTimeText);
        MessageDrawable messageDrawable = chat_msgInDrawable;
        int i3 = key_chat_inBubble;
        setDrawableColorByKey(messageDrawable, i3);
        MessageDrawable messageDrawable2 = chat_msgInSelectedDrawable;
        int i4 = key_chat_inBubbleSelected;
        setDrawableColorByKey(messageDrawable2, i4);
        setDrawableColorByKey(chat_msgInMediaDrawable, i3);
        setDrawableColorByKey(chat_msgInMediaSelectedDrawable, i4);
        setDrawableColorByKey(chat_msgOutCheckDrawable, key_chat_outSentCheck);
        setDrawableColorByKey(chat_msgOutCheckSelectedDrawable, key_chat_outSentCheckSelected);
        Drawable drawable = chat_msgOutCheckReadDrawable;
        int i5 = key_chat_outSentCheckRead;
        setDrawableColorByKey(drawable, i5);
        Drawable drawable2 = chat_msgOutCheckReadSelectedDrawable;
        int i6 = key_chat_outSentCheckReadSelected;
        setDrawableColorByKey(drawable2, i6);
        setDrawableColorByKey(chat_msgOutHalfCheckDrawable, i5);
        setDrawableColorByKey(chat_msgOutHalfCheckSelectedDrawable, i6);
        Drawable drawable3 = chat_msgMediaCheckDrawable;
        int i7 = key_chat_mediaSentCheck;
        setDrawableColorByKey(drawable3, i7);
        setDrawableColorByKey(chat_msgMediaHalfCheckDrawable, i7);
        setDrawableColorByKey(chat_msgStickerCheckDrawable, i2);
        setDrawableColorByKey(chat_msgStickerHalfCheckDrawable, i2);
        setDrawableColorByKey(chat_msgStickerViewsDrawable, i2);
        setDrawableColorByKey(chat_msgStickerRepliesDrawable, i2);
        setDrawableColorByKey(chat_msgUnlockDrawable, i2);
        Drawable drawable4 = chat_shareIconDrawable;
        int i8 = key_chat_serviceIcon;
        setDrawableColorByKey(drawable4, i8);
        setDrawableColorByKey(chat_replyIconDrawable, i8);
        setDrawableColorByKey(chat_goIconDrawable, i8);
        setDrawableColorByKey(chat_botInlineDrawable, i8);
        setDrawableColorByKey(chat_botWebViewDrawable, i8);
        setDrawableColorByKey(chat_botInviteDrawable, i8);
        setDrawableColorByKey(chat_botLinkDrawable, i8);
        Drawable drawable5 = chat_msgInViewsDrawable;
        int i9 = key_chat_inViews;
        setDrawableColorByKey(drawable5, i9);
        Drawable drawable6 = chat_msgInViewsSelectedDrawable;
        int i10 = key_chat_inViewsSelected;
        setDrawableColorByKey(drawable6, i10);
        Drawable drawable7 = chat_msgOutViewsDrawable;
        int i11 = key_chat_outViews;
        setDrawableColorByKey(drawable7, i11);
        Drawable drawable8 = chat_msgOutViewsSelectedDrawable;
        int i12 = key_chat_outViewsSelected;
        setDrawableColorByKey(drawable8, i12);
        setDrawableColorByKey(chat_msgInRepliesDrawable, i9);
        setDrawableColorByKey(chat_msgInRepliesSelectedDrawable, i10);
        setDrawableColorByKey(chat_msgOutRepliesDrawable, i11);
        setDrawableColorByKey(chat_msgOutRepliesSelectedDrawable, i12);
        setDrawableColorByKey(chat_msgInPinnedDrawable, i9);
        setDrawableColorByKey(chat_msgInPinnedSelectedDrawable, i10);
        setDrawableColorByKey(chat_msgOutPinnedDrawable, i11);
        setDrawableColorByKey(chat_msgOutPinnedSelectedDrawable, i12);
        Drawable drawable9 = chat_msgMediaPinnedDrawable;
        int i13 = key_chat_mediaViews;
        setDrawableColorByKey(drawable9, i13);
        setDrawableColorByKey(chat_msgStickerPinnedDrawable, i2);
        setDrawableColorByKey(chat_msgMediaViewsDrawable, i13);
        setDrawableColorByKey(chat_msgMediaRepliesDrawable, i13);
        setDrawableColorByKey(chat_msgInMenuDrawable, key_chat_inMenu);
        setDrawableColorByKey(chat_msgInMenuSelectedDrawable, key_chat_inMenuSelected);
        setDrawableColorByKey(chat_msgOutMenuDrawable, key_chat_outMenu);
        setDrawableColorByKey(chat_msgOutMenuSelectedDrawable, key_chat_outMenuSelected);
        setDrawableColorByKey(chat_msgMediaMenuDrawable, key_chat_mediaMenu);
        setDrawableColorByKey(chat_msgOutInstantDrawable, key_chat_outInstant);
        Drawable drawable10 = chat_msgInInstantDrawable;
        int i14 = key_chat_inInstant;
        setDrawableColorByKey(drawable10, i14);
        setDrawableColorByKey(chat_msgErrorDrawable, key_chat_sentErrorIcon);
        setDrawableColorByKey(chat_muteIconDrawable, key_chat_muteIcon);
        setDrawableColorByKey(chat_lockIconDrawable, key_chat_lockIcon);
        Drawable drawable11 = chat_inlineResultFile;
        int i15 = key_chat_inlineResultIcon;
        setDrawableColorByKey(drawable11, i15);
        setDrawableColorByKey(chat_inlineResultAudio, i15);
        setDrawableColorByKey(chat_inlineResultLocation, i15);
        setDrawableColorByKey(chat_commentDrawable, i14);
        setDrawableColorByKey(chat_commentStickerDrawable, i8);
        setDrawableColorByKey(chat_commentArrowDrawable, i14);
        Drawable drawable12 = chat_gradientLeftDrawable;
        int i16 = key_chat_stickersHintPanel;
        setDrawableColorByKey(drawable12, i16);
        setDrawableColorByKey(chat_gradientRightDrawable, i16);
        for (int i17 = 0; i17 < 2; i17++) {
            setDrawableColorByKey(chat_msgInCallDrawable[i17], key_chat_inInstant);
            setDrawableColorByKey(chat_msgInCallSelectedDrawable[i17], key_chat_inInstantSelected);
            setDrawableColorByKey(chat_msgOutCallDrawable[i17], key_chat_outInstant);
            setDrawableColorByKey(chat_msgOutCallSelectedDrawable[i17], key_chat_outInstantSelected);
        }
        setDrawableColorByKey(chat_msgCallUpGreenDrawable, key_chat_outGreenCall);
        Drawable drawable13 = chat_msgCallDownRedDrawable;
        int i18 = key_fill_RedNormal;
        setDrawableColorByKey(drawable13, i18);
        setDrawableColorByKey(chat_msgCallDownGreenDrawable, key_chat_inGreenCall);
        setDrawableColorByKey(calllog_msgCallUpRedDrawable, i18);
        Drawable drawable14 = calllog_msgCallUpGreenDrawable;
        int i19 = key_calls_callReceivedGreenIcon;
        setDrawableColorByKey(drawable14, i19);
        setDrawableColorByKey(calllog_msgCallDownRedDrawable, i18);
        setDrawableColorByKey(calllog_msgCallDownGreenDrawable, i19);
        int i20 = 0;
        while (true) {
            StatusDrawable[] statusDrawableArr = chat_status_drawables;
            if (i20 >= statusDrawableArr.length) {
                break;
            }
            setDrawableColorByKey(statusDrawableArr[i20], key_chats_actionMessage);
            i20++;
        }
        for (int i21 = 0; i21 < 5; i21++) {
            setCombinedDrawableColor(chat_fileStatesDrawable[i21][0], getColor(key_chat_inLoader), false);
            setCombinedDrawableColor(chat_fileStatesDrawable[i21][0], getColor(key_chat_inMediaIcon), true);
            setCombinedDrawableColor(chat_fileStatesDrawable[i21][1], getColor(key_chat_inLoaderSelected), false);
            setCombinedDrawableColor(chat_fileStatesDrawable[i21][1], getColor(key_chat_inMediaIconSelected), true);
        }
        setCombinedDrawableColor(chat_contactDrawable[0], getColor(key_chat_inContactBackground), false);
        setCombinedDrawableColor(chat_contactDrawable[0], getColor(key_chat_inContactIcon), true);
        setCombinedDrawableColor(chat_contactDrawable[1], getColor(key_chat_outContactBackground), false);
        setCombinedDrawableColor(chat_contactDrawable[1], getColor(key_chat_outContactIcon), true);
        setDrawableColor(chat_locationDrawable[0], getColor(key_chat_inLocationIcon));
        setDrawableColor(chat_locationDrawable[1], getColor(key_chat_outLocationIcon));
        setDrawableColor(chat_pollHintDrawable[0], getColor(key_chat_inPreviewInstantText));
        setDrawableColor(chat_pollHintDrawable[1], getColor(key_chat_outPreviewInstantText));
        setDrawableColor(chat_psaHelpDrawable[0], getColor(key_chat_inViews));
        setDrawableColor(chat_psaHelpDrawable[1], getColor(key_chat_outViews));
        setDrawableColorByKey(chat_composeShadowDrawable, key_chat_messagePanelShadow);
        setDrawableColorByKey(chat_composeShadowRoundDrawable, key_chat_messagePanelBackground);
        int color = getColor(key_chat_outAudioSeekbarFill) == -1 ? getColor(key_chat_outBubble) : -1;
        setDrawableColor(chat_pollCheckDrawable[1], color);
        setDrawableColor(chat_pollCrossDrawable[1], color);
        setDrawableColor(chat_attachEmptyDrawable, getColor(key_chat_attachEmptyImage));
        if (!z2 && !disallowChangeServiceMessageColor) {
            applyChatServiceMessageColor();
            applyChatMessageSelectedBackgroundColor();
        }
        refreshAttachButtonsColors();
    }

    public static void applyChatServiceMessageColor() {
        Drawable drawable = wallpaper;
        if (drawable != null) {
            applyChatServiceMessageColor(null, null, drawable);
        }
    }

    public static boolean hasGradientService() {
        return serviceBitmapShader != null;
    }

    public static void applyServiceShaderMatrixForView(View view, View view2) {
        applyServiceShaderMatrixForView(view, view2, null);
    }

    public static void applyServiceShaderMatrixForView(View view, View view2, ResourcesProvider resourcesProvider) {
        if (view == null || view2 == null) {
            return;
        }
        view.getLocationOnScreen(viewPos);
        int[] iArr = viewPos;
        int i = iArr[0];
        int i2 = iArr[1];
        view2.getLocationOnScreen(iArr);
        if (resourcesProvider != null) {
            resourcesProvider.applyServiceShaderMatrix(view2.getMeasuredWidth(), view2.getMeasuredHeight(), i, i2 - viewPos[1]);
        } else {
            applyServiceShaderMatrix(view2.getMeasuredWidth(), view2.getMeasuredHeight(), i, i2 - viewPos[1]);
        }
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
        int i;
        int i2;
        int i3;
        if (chat_actionBackgroundPaint == null) {
            return;
        }
        serviceMessageColor = serviceMessageColorBackup;
        serviceSelectedMessageColor = serviceSelectedMessageColorBackup;
        boolean z = true;
        if (iArr != null && iArr.length >= 2) {
            i = iArr[0];
            i3 = iArr[1];
            serviceMessageColor = iArr[0];
            serviceSelectedMessageColor = iArr[1];
            i2 = i;
        } else {
            int indexOfKey = currentColors.indexOfKey(key_chat_serviceBackground);
            if (indexOfKey >= 0) {
                i2 = currentColors.valueAt(indexOfKey);
                i = i2;
            } else {
                i = serviceMessageColor;
                i2 = serviceMessage2Color;
            }
            int indexOfKey2 = currentColors.indexOfKey(key_chat_serviceBackgroundSelected);
            if (indexOfKey2 >= 0) {
                i3 = currentColors.valueAt(indexOfKey2);
            } else {
                i3 = serviceSelectedMessageColor;
            }
        }
        if (drawable == null) {
            drawable = drawable2;
        }
        boolean z2 = drawable instanceof MotionBackgroundDrawable;
        if ((z2 && SharedConfig.getDevicePerformanceClass() != 0 && LiteMode.isEnabled(32)) ? false : false) {
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
            chat_actionTextPaint2.setColor(-1);
            chat_actionTextPaint.linkColor = -1;
            chat_unlockExtendedMediaTextPaint.setColor(-1);
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
            Drawable drawable3 = chat_msgStickerPinnedDrawable;
            int i4 = key_chat_serviceText;
            setDrawableColorByKey(drawable3, i4);
            setDrawableColorByKey(chat_msgStickerCheckDrawable, i4);
            setDrawableColorByKey(chat_msgStickerHalfCheckDrawable, i4);
            setDrawableColorByKey(chat_msgStickerViewsDrawable, i4);
            setDrawableColorByKey(chat_msgStickerRepliesDrawable, i4);
            chat_actionTextPaint.setColor(getColor(i4));
            chat_actionTextPaint2.setColor(getColor(i4));
            chat_actionTextPaint.linkColor = getColor(key_chat_serviceLink);
            chat_unlockExtendedMediaTextPaint.setColor(getColor(i4));
            Drawable drawable4 = chat_commentStickerDrawable;
            int i5 = key_chat_serviceIcon;
            setDrawableColorByKey(drawable4, i5);
            setDrawableColorByKey(chat_shareIconDrawable, i5);
            setDrawableColorByKey(chat_replyIconDrawable, i5);
            setDrawableColorByKey(chat_goIconDrawable, i5);
            setDrawableColorByKey(chat_botInlineDrawable, i5);
            setDrawableColorByKey(chat_botWebViewDrawable, i5);
            setDrawableColorByKey(chat_botInviteDrawable, i5);
            setDrawableColorByKey(chat_botLinkDrawable, i5);
            chat_botButtonPaint.setColor(getColor(key_chat_botButtonText));
        }
        chat_actionBackgroundPaint.setColor(i);
        chat_actionBackgroundSelectedPaint.setColor(i3);
        chat_actionBackgroundPaint2.setColor(i2);
        if (serviceBitmapShader != null && (currentColors.indexOfKey(key_chat_serviceBackground) < 0 || z2)) {
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
        int i = currentColors.get(key_chat_selectedBackground);
        if (drawable == null) {
            drawable = drawable2;
        }
        boolean z = (drawable instanceof MotionBackgroundDrawable) && SharedConfig.getDevicePerformanceClass() != 0 && i == 0;
        if (z && serviceBitmap != (bitmap = ((MotionBackgroundDrawable) drawable).getBitmap())) {
            serviceBitmap = bitmap;
            Bitmap bitmap2 = serviceBitmap;
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            serviceBitmapShader = new BitmapShader(bitmap2, tileMode, tileMode);
            if (serviceBitmapMatrix == null) {
                serviceBitmapMatrix = new Matrix();
            }
        }
        if (serviceBitmapShader != null && i == 0 && z) {
            ColorMatrix colorMatrix = new ColorMatrix();
            AndroidUtilities.adjustSaturationColorMatrix(colorMatrix, 2.5f);
            AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, 0.75f);
            chat_messageBackgroundSelectedPaint.setShader(serviceBitmapShader);
            chat_messageBackgroundSelectedPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            chat_messageBackgroundSelectedPaint.setAlpha(64);
            return;
        }
        Paint paint = chat_messageBackgroundSelectedPaint;
        if (i == 0) {
            i = 1073741824;
        }
        paint.setColor(i);
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
        profile_aboutTextPaint.setColor(getColor(key_windowBackgroundWhiteBlackText));
        profile_aboutTextPaint.linkColor = getColor(key_windowBackgroundWhiteLinkText);
        setDrawableColorByKey(profile_verifiedDrawable, key_profile_verifiedBackground);
        setDrawableColorByKey(profile_verifiedCheckDrawable, key_profile_verifiedCheck);
    }

    public static Drawable getThemedDrawableByKey(Context context, int i, int i2, ResourcesProvider resourcesProvider) {
        return getThemedDrawable(context, i, getColor(i2, resourcesProvider));
    }

    public static Drawable getThemedDrawableByKey(Context context, int i, int i2) {
        return getThemedDrawable(context, i, getColor(i2));
    }

    public static Drawable getThemedDrawable(Context context, int i, int i2) {
        if (context == null) {
            return null;
        }
        Drawable mutate = context.getResources().getDrawable(i).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.MULTIPLY));
        return mutate;
    }

    public static int getDefaultColor(int i) {
        int i2 = defaultColors[i];
        return i2 == 0 ? (isMyMessagesBubbles(i) || i == key_chats_menuTopShadow || i == key_chats_menuTopBackground || i == key_chats_menuTopShadowCats || i == key_chat_wallpaper_gradient_to2 || i == key_chat_wallpaper_gradient_to3) ? 0 : -65536 : i2;
    }

    public static boolean hasThemeKey(int i) {
        return currentColors.indexOfKey(i) >= 0;
    }

    public static void setAnimatingColor(boolean z) {
        animatingColors = z ? new SparseIntArray() : null;
    }

    public static boolean isAnimatingColor() {
        return animatingColors != null;
    }

    public static void setAnimatedColor(int i, int i2) {
        SparseIntArray sparseIntArray = animatingColors;
        if (sparseIntArray == null) {
            return;
        }
        sparseIntArray.put(i, i2);
    }

    public static int getDefaultAccentColor(int i) {
        int indexOfKey = currentColorsNoAccent.indexOfKey(i);
        if (indexOfKey >= 0) {
            int valueAt = currentColorsNoAccent.valueAt(indexOfKey);
            ThemeAccent accent = currentTheme.getAccent(false);
            if (accent == null) {
                return 0;
            }
            float[] tempHsv = getTempHsv(1);
            float[] tempHsv2 = getTempHsv(2);
            Color.colorToHSV(currentTheme.accentBaseColor, tempHsv);
            Color.colorToHSV(accent.accentColor, tempHsv2);
            return changeColorAccent(tempHsv, tempHsv2, valueAt, currentTheme.isDark());
        }
        return 0;
    }

    public static int getNonAnimatedColor(int i) {
        return getColor(i, null, true);
    }

    public static int getColor(int i, ResourcesProvider resourcesProvider) {
        if (resourcesProvider != null) {
            return resourcesProvider.getColor(i);
        }
        return getColor(i);
    }

    public static int getColor(int i) {
        return getColor(i, null, false);
    }

    public static int getColor(int i, boolean[] zArr) {
        return getColor(i, zArr, false);
    }

    public static int getColor(int i, boolean[] zArr, boolean z) {
        int indexOfKey;
        boolean isDefaultMyMessages;
        SparseIntArray sparseIntArray;
        int indexOfKey2;
        if (!z && (sparseIntArray = animatingColors) != null && (indexOfKey2 = sparseIntArray.indexOfKey(i)) >= 0) {
            return animatingColors.valueAt(indexOfKey2);
        }
        if (serviceBitmapShader == null || !(key_chat_serviceText == i || key_chat_serviceLink == i || key_chat_serviceIcon == i || key_chat_stickerReplyLine == i || key_chat_stickerReplyNameText == i || key_chat_stickerReplyMessageText == i)) {
            if (currentTheme == defaultTheme) {
                if (isMyMessagesBubbles(i)) {
                    isDefaultMyMessages = currentTheme.isDefaultMyMessagesBubbles();
                } else {
                    isDefaultMyMessages = isMyMessages(i) ? currentTheme.isDefaultMyMessages() : (key_chat_wallpaper == i || key_chat_wallpaper_gradient_to1 == i || key_chat_wallpaper_gradient_to2 == i || key_chat_wallpaper_gradient_to3 == i) ? false : currentTheme.isDefaultMainAccent();
                }
                if (isDefaultMyMessages) {
                    if (i == key_chat_serviceBackground) {
                        return serviceMessageColor;
                    }
                    if (i == key_chat_serviceBackgroundSelected) {
                        return serviceSelectedMessageColor;
                    }
                    return getDefaultColor(i);
                }
            }
            int indexOfKey3 = currentColors.indexOfKey(i);
            if (indexOfKey3 < 0) {
                int i2 = fallbackKeys.get(i, -1);
                if (i2 != -1 && (indexOfKey = currentColors.indexOfKey(i2)) >= 0) {
                    return currentColors.valueAt(indexOfKey);
                }
                if (zArr != null) {
                    zArr[0] = true;
                }
                if (i == key_chat_serviceBackground) {
                    return serviceMessageColor;
                }
                if (i == key_chat_serviceBackgroundSelected) {
                    return serviceSelectedMessageColor;
                }
                return getDefaultColor(i);
            }
            int valueAt = currentColors.valueAt(indexOfKey3);
            return (key_windowBackgroundWhite == i || key_windowBackgroundGray == i || key_actionBarDefault == i || key_actionBarDefaultArchived == i) ? valueAt | (-16777216) : valueAt;
        }
        return -1;
    }

    private static boolean isMyMessagesBubbles(int i) {
        return i >= myMessagesBubblesStartIndex && i < myMessagesBubblesEndIndex;
    }

    private static boolean isMyMessages(int i) {
        return i >= myMessagesStartIndex && i < myMessagesEndIndex;
    }

    public static void setColor(int i, int i2, boolean z) {
        int i3 = key_chat_wallpaper;
        if (i == i3 || i == key_chat_wallpaper_gradient_to1 || i == key_chat_wallpaper_gradient_to2 || i == key_chat_wallpaper_gradient_to3 || i == key_windowBackgroundWhite || i == key_windowBackgroundGray || i == key_actionBarDefault || i == key_actionBarDefaultArchived) {
            i2 |= -16777216;
        }
        if (z) {
            currentColors.delete(i);
        } else {
            currentColors.put(i, i2);
        }
        if (i == key_chat_selectedBackground) {
            applyChatMessageSelectedBackgroundColor();
        } else if (i == key_chat_serviceBackground || i == key_chat_serviceBackgroundSelected) {
            applyChatServiceMessageColor();
        } else if (i == i3 || i == key_chat_wallpaper_gradient_to1 || i == key_chat_wallpaper_gradient_to2 || i == key_chat_wallpaper_gradient_to3 || i == key_chat_wallpaper_gradient_rotation) {
            reloadWallpaper(true);
        } else if (i == key_actionBarDefault) {
            if (Build.VERSION.SDK_INT >= 23) {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needCheckSystemBarColors, new Object[0]);
            }
        } else if (i != key_windowBackgroundGray || Build.VERSION.SDK_INT < 26) {
        } else {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needCheckSystemBarColors, new Object[0]);
        }
    }

    public static void setDefaultColor(int i, int i2) {
        defaultColors[i] = i2;
    }

    public static void setThemeWallpaper(ThemeInfo themeInfo, Bitmap bitmap, File file) {
        currentColors.delete(key_chat_wallpaper);
        currentColors.delete(key_chat_wallpaper_gradient_to1);
        currentColors.delete(key_chat_wallpaper_gradient_to2);
        currentColors.delete(key_chat_wallpaper_gradient_to3);
        currentColors.delete(key_chat_wallpaper_gradient_rotation);
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
        reloadWallpaper(true);
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

    public static void setDrawableColorByKey(Drawable drawable, int i) {
        setDrawableColor(drawable, getColor(i));
    }

    public static void setEmojiDrawableColor(Drawable drawable, int i, boolean z) {
        Drawable stateDrawable;
        if (drawable instanceof StateListDrawable) {
            try {
                if (z) {
                    stateDrawable = getStateDrawable(drawable, 0);
                } else {
                    stateDrawable = getStateDrawable(drawable, 1);
                }
                if (stateDrawable instanceof ShapeDrawable) {
                    ((ShapeDrawable) stateDrawable).getPaint().setColor(i);
                } else {
                    stateDrawable.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
                }
            } catch (Throwable unused) {
            }
        }
    }

    @SuppressLint({"DiscouragedPrivateApi"})
    @TargetApi(21)
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
        Drawable stateDrawable;
        if (drawable instanceof StateListDrawable) {
            try {
                if (z) {
                    Drawable stateDrawable2 = getStateDrawable(drawable, 0);
                    if (stateDrawable2 instanceof ShapeDrawable) {
                        ((ShapeDrawable) stateDrawable2).getPaint().setColor(i);
                    } else {
                        stateDrawable2.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
                    }
                    stateDrawable = getStateDrawable(drawable, 1);
                } else {
                    stateDrawable = getStateDrawable(drawable, 2);
                }
                if (stateDrawable instanceof ShapeDrawable) {
                    ((ShapeDrawable) stateDrawable).getPaint().setColor(i);
                } else {
                    stateDrawable.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
                }
            } catch (Throwable unused) {
            }
        } else if (Build.VERSION.SDK_INT < 21 || !(drawable instanceof RippleDrawable)) {
        } else {
            RippleDrawable rippleDrawable = (RippleDrawable) drawable;
            if (z) {
                rippleDrawable.setColor(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}));
            } else if (rippleDrawable.getNumberOfLayers() > 0) {
                Drawable drawable2 = rippleDrawable.getDrawable(0);
                if (drawable2 instanceof ShapeDrawable) {
                    ((ShapeDrawable) drawable2).getPaint().setColor(i);
                } else {
                    drawable2.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
                }
            }
        }
    }

    public static boolean isThemeWallpaperPublic() {
        return !TextUtils.isEmpty(themedWallpaperLink);
    }

    public static boolean hasWallpaperFromTheme() {
        ThemeInfo themeInfo = currentTheme;
        if (themeInfo.firstAccentIsDefault && themeInfo.currentAccentId == DEFALT_THEME_ACCENT_ID) {
            return false;
        }
        return currentColors.indexOfKey(key_chat_wallpaper) >= 0 || themedWallpaperFileOffset > 0 || !TextUtils.isEmpty(themedWallpaperLink);
    }

    public static boolean isCustomTheme() {
        return isCustomTheme;
    }

    public static void reloadWallpaper(boolean z) {
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
        loadWallpaper(z);
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
        int indexOfKey = currentColors.indexOfKey(key_chat_serviceBackground);
        if (indexOfKey >= 0) {
            return currentColors.valueAt(indexOfKey);
        }
        return serviceMessageColor;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0078  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void loadWallpaper(boolean z) {
        final File file;
        final TLRPC$Document tLRPC$Document;
        final boolean z2;
        float f;
        float f2;
        TLRPC$WallPaper tLRPC$WallPaper;
        if (wallpaper != null) {
            return;
        }
        ThemeInfo themeInfo = currentTheme;
        boolean z3 = themeInfo.firstAccentIsDefault && themeInfo.currentAccentId == DEFALT_THEME_ACCENT_ID;
        ThemeAccent accent = themeInfo.getAccent(false);
        if (accent != null) {
            File pathToWallpaper = accent.getPathToWallpaper();
            boolean z4 = accent.patternMotion;
            TLRPC$TL_theme tLRPC$TL_theme = accent.info;
            TLRPC$ThemeSettings tLRPC$ThemeSettings = (tLRPC$TL_theme == null || tLRPC$TL_theme.settings.size() <= 0) ? null : accent.info.settings.get(0);
            z2 = z4;
            tLRPC$Document = (accent.info == null || tLRPC$ThemeSettings == null || (tLRPC$WallPaper = tLRPC$ThemeSettings.wallpaper) == null) ? null : tLRPC$WallPaper.document;
            file = pathToWallpaper;
        } else {
            file = null;
            tLRPC$Document = null;
            z2 = false;
        }
        ThemeInfo themeInfo2 = currentTheme;
        final OverrideWallpaperInfo overrideWallpaperInfo = themeInfo2.overrideWallpaper;
        if (overrideWallpaperInfo != null) {
            f2 = overrideWallpaperInfo.intensity;
        } else if (accent == null) {
            f = themeInfo2.patternIntensity;
            final int i = (int) f;
            if (!z) {
                DispatchQueue dispatchQueue = Utilities.themeQueue;
                final boolean z5 = z3;
                Runnable runnable = new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        Theme.lambda$loadWallpaper$12(Theme.OverrideWallpaperInfo.this, file, i, z2, tLRPC$Document, z5);
                    }
                };
                wallpaperLoadTask = runnable;
                dispatchQueue.postRunnable(runnable);
                return;
            }
            Drawable loadWallpaperInternal = loadWallpaperInternal(overrideWallpaperInfo, file, i, z2, tLRPC$Document, z3);
            createCommonChatResources();
            if (!disallowChangeServiceMessageColor) {
                applyChatServiceMessageColor(null, null, loadWallpaperInternal);
                applyChatMessageSelectedBackgroundColor(null, loadWallpaperInternal);
            }
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper, new Object[0]);
            return;
        } else {
            f2 = accent.patternIntensity;
        }
        f = f2 * 100.0f;
        final int i2 = (int) f;
        if (!z) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadWallpaper$12(OverrideWallpaperInfo overrideWallpaperInfo, File file, int i, boolean z, TLRPC$Document tLRPC$Document, boolean z2) {
        final Drawable loadWallpaperInternal = loadWallpaperInternal(overrideWallpaperInfo, file, i, z, tLRPC$Document, z2);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$loadWallpaper$11(loadWallpaperInternal);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadWallpaper$11(Drawable drawable) {
        wallpaperLoadTask = null;
        createCommonChatResources();
        if (!disallowChangeServiceMessageColor) {
            applyChatServiceMessageColor(null, null, drawable);
            applyChatMessageSelectedBackgroundColor(null, drawable);
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper, new Object[0]);
    }

    private static Drawable loadWallpaperInternal(OverrideWallpaperInfo overrideWallpaperInfo, File file, int i, boolean z, TLRPC$Document tLRPC$Document, boolean z2) {
        BackgroundDrawableSettings createBackgroundDrawable = createBackgroundDrawable(currentTheme, overrideWallpaperInfo, currentColors, file, themedWallpaperLink, themedWallpaperFileOffset, i, previousPhase, z2, hasPreviousTheme, isApplyingAccent, z, tLRPC$Document);
        Boolean bool = createBackgroundDrawable.isWallpaperMotion;
        isWallpaperMotion = bool != null ? bool.booleanValue() : isWallpaperMotion;
        Boolean bool2 = createBackgroundDrawable.isPatternWallpaper;
        isPatternWallpaper = bool2 != null ? bool2.booleanValue() : isPatternWallpaper;
        Boolean bool3 = createBackgroundDrawable.isCustomTheme;
        isCustomTheme = bool3 != null ? bool3.booleanValue() : isCustomTheme;
        Drawable drawable = createBackgroundDrawable.wallpaper;
        wallpaper = drawable != null ? drawable : wallpaper;
        calcBackgroundColor(drawable, 1);
        return drawable;
    }

    public static BackgroundDrawableSettings createBackgroundDrawable(ThemeInfo themeInfo, SparseIntArray sparseIntArray, String str, int i) {
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
            return createBackgroundDrawable(themeInfo, overrideWallpaperInfo, sparseIntArray, pathToWallpaper, str, currentColorsNoAccent.get(key_wallpaperFileOffset, -1), (int) f, i, z, false, false, z2, null);
        }
        f = f2 * 100.0f;
        return createBackgroundDrawable(themeInfo, overrideWallpaperInfo, sparseIntArray, pathToWallpaper, str, currentColorsNoAccent.get(key_wallpaperFileOffset, -1), (int) f, i, z, false, false, z2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:139:0x0302  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BackgroundDrawableSettings createBackgroundDrawable(ThemeInfo themeInfo, OverrideWallpaperInfo overrideWallpaperInfo, SparseIntArray sparseIntArray, File file, String str, int i, int i2, int i3, boolean z, boolean z2, boolean z3, boolean z4, TLRPC$Document tLRPC$Document) {
        int height;
        int i4;
        Bitmap loadScreenSizedBitmap;
        Bitmap loadScreenSizedBitmap2;
        File file2;
        BackgroundDrawableSettings backgroundDrawableSettings = new BackgroundDrawableSettings();
        backgroundDrawableSettings.wallpaper = wallpaper;
        boolean z5 = (!z2 || z3) && overrideWallpaperInfo != null;
        if (overrideWallpaperInfo != null) {
            backgroundDrawableSettings.isWallpaperMotion = Boolean.valueOf(overrideWallpaperInfo.isMotion);
            backgroundDrawableSettings.isPatternWallpaper = Boolean.valueOf((overrideWallpaperInfo.color == 0 || overrideWallpaperInfo.isDefault() || overrideWallpaperInfo.isColor()) ? false : true);
        } else {
            backgroundDrawableSettings.isWallpaperMotion = Boolean.valueOf(themeInfo.isMotion);
            backgroundDrawableSettings.isPatternWallpaper = Boolean.valueOf(themeInfo.patternBgColor != 0);
        }
        if (!z5) {
            int i5 = z ? 0 : sparseIntArray.get(key_chat_wallpaper);
            int i6 = sparseIntArray.get(key_chat_wallpaper_gradient_to3);
            int i7 = sparseIntArray.get(key_chat_wallpaper_gradient_to2);
            int i8 = sparseIntArray.get(key_chat_wallpaper_gradient_to1);
            if (file != null && file.exists()) {
                try {
                    if (i5 != 0 && i8 != 0 && i7 != 0) {
                        MotionBackgroundDrawable motionBackgroundDrawable = new MotionBackgroundDrawable(i5, i8, i7, i6, false);
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
            } else if (i5 != 0) {
                int i9 = sparseIntArray.get(key_chat_wallpaper_gradient_rotation, -1);
                if (i9 == -1) {
                    i9 = 45;
                }
                if (i8 != 0 && i7 != 0) {
                    MotionBackgroundDrawable motionBackgroundDrawable2 = new MotionBackgroundDrawable(i5, i8, i7, i6, false);
                    Bitmap bitmap = null;
                    if (file != null && tLRPC$Document != null) {
                        Bitmap bitmap2 = SvgHelper.getBitmap(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tLRPC$Document, true), AndroidUtilities.dp(360.0f), AndroidUtilities.dp(640.0f), false);
                        if (bitmap2 != null) {
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                bitmap2.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                                fileOutputStream.close();
                            } catch (Exception e) {
                                FileLog.e(e);
                                e.printStackTrace();
                            }
                        }
                        bitmap = bitmap2;
                    }
                    motionBackgroundDrawable2.setPatternBitmap(i2, bitmap);
                    motionBackgroundDrawable2.setPhase(i3);
                    backgroundDrawableSettings.wallpaper = motionBackgroundDrawable2;
                } else if (i8 == 0 || i8 == i5) {
                    backgroundDrawableSettings.wallpaper = new ColorDrawable(i5);
                } else {
                    BackgroundGradientDrawable backgroundGradientDrawable = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(i9), new int[]{i5, i8});
                    backgroundGradientDisposable = backgroundGradientDrawable.startDithering(BackgroundGradientDrawable.Sizes.ofDeviceScreen(), new BackgroundGradientDrawable.ListenerAdapter() { // from class: org.telegram.ui.ActionBar.Theme.11
                        @Override // org.telegram.ui.Components.BackgroundGradientDrawable.ListenerAdapter, org.telegram.ui.Components.BackgroundGradientDrawable.Listener
                        public void onSizeReady(int i10, int i11) {
                            Point point = AndroidUtilities.displaySize;
                            if ((point.x <= point.y) == (i10 <= i11)) {
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
                    Bitmap loadScreenSizedBitmap3 = loadScreenSizedBitmap(new FileInputStream(new File(filesDirFixed, Utilities.MD5(str) + ".wp")), 0);
                    if (loadScreenSizedBitmap3 != null) {
                        backgroundDrawableSettings.wallpaper = new BitmapDrawable(loadScreenSizedBitmap3);
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
                    Bitmap loadScreenSizedBitmap4 = loadScreenSizedBitmap(new FileInputStream(file2), i);
                    if (loadScreenSizedBitmap4 != null) {
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(loadScreenSizedBitmap4);
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
            int i10 = overrideWallpaperInfo != null ? overrideWallpaperInfo.color : 0;
            if (overrideWallpaperInfo != null) {
                if (!overrideWallpaperInfo.isDefault()) {
                    if (!overrideWallpaperInfo.isColor() || overrideWallpaperInfo.gradientColor1 != 0) {
                        if (i10 != 0 && (!isPatternWallpaper || overrideWallpaperInfo.gradientColor2 != 0)) {
                            if (overrideWallpaperInfo.gradientColor1 != 0 && overrideWallpaperInfo.gradientColor2 != 0) {
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
                            } else if (backgroundDrawableSettings.isPatternWallpaper.booleanValue()) {
                                File file4 = new File(ApplicationLoader.getFilesDirFixed(), overrideWallpaperInfo.fileName);
                                if (file4.exists() && (loadScreenSizedBitmap2 = loadScreenSizedBitmap(new FileInputStream(file4), 0)) != null) {
                                    BitmapDrawable bitmapDrawable2 = new BitmapDrawable(loadScreenSizedBitmap2);
                                    backgroundDrawableSettings.wallpaper = bitmapDrawable2;
                                    bitmapDrawable2.setFilterBitmap(true);
                                    backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
                                }
                            } else {
                                int i11 = overrideWallpaperInfo.gradientColor1;
                                if (i11 != 0) {
                                    BackgroundGradientDrawable backgroundGradientDrawable2 = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(overrideWallpaperInfo.rotation), new int[]{i10, i11});
                                    backgroundGradientDisposable = backgroundGradientDrawable2.startDithering(BackgroundGradientDrawable.Sizes.ofDeviceScreen(), new BackgroundGradientDrawable.ListenerAdapter() { // from class: org.telegram.ui.ActionBar.Theme.12
                                        @Override // org.telegram.ui.Components.BackgroundGradientDrawable.ListenerAdapter, org.telegram.ui.Components.BackgroundGradientDrawable.Listener
                                        public void onSizeReady(int i12, int i13) {
                                            Point point = AndroidUtilities.displaySize;
                                            if ((point.x <= point.y) == (i12 <= i13)) {
                                                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewWallpapper, new Object[0]);
                                            }
                                        }
                                    }, 100L);
                                    backgroundDrawableSettings.wallpaper = backgroundGradientDrawable2;
                                } else {
                                    backgroundDrawableSettings.wallpaper = new ColorDrawable(i10);
                                }
                            }
                        } else {
                            File file5 = new File(ApplicationLoader.getFilesDirFixed(), overrideWallpaperInfo.fileName);
                            if (file5.exists() && (loadScreenSizedBitmap = loadScreenSizedBitmap(new FileInputStream(file5), 0)) != null) {
                                BitmapDrawable bitmapDrawable3 = new BitmapDrawable(loadScreenSizedBitmap);
                                backgroundDrawableSettings.wallpaper = bitmapDrawable3;
                                bitmapDrawable3.setFilterBitmap(true);
                                backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
                            }
                            if (backgroundDrawableSettings.wallpaper == null) {
                                backgroundDrawableSettings.wallpaper = createDefaultWallpaper();
                                backgroundDrawableSettings.isCustomTheme = Boolean.FALSE;
                            }
                        }
                    }
                    if (backgroundDrawableSettings.wallpaper == null) {
                        if (i10 == 0) {
                            i10 = -2693905;
                        }
                        backgroundDrawableSettings.wallpaper = new ColorDrawable(i10);
                    }
                }
            }
            backgroundDrawableSettings.wallpaper = createDefaultWallpaper();
            backgroundDrawableSettings.isCustomTheme = Boolean.FALSE;
            if (backgroundDrawableSettings.wallpaper == null) {
            }
        }
        if (!LiteMode.isEnabled(32)) {
            Drawable drawable = backgroundDrawableSettings.wallpaper;
            if (drawable instanceof MotionBackgroundDrawable) {
                MotionBackgroundDrawable motionBackgroundDrawable4 = (MotionBackgroundDrawable) drawable;
                if (motionBackgroundDrawable4.getPatternBitmap() == null) {
                    Point point = AndroidUtilities.displaySize;
                    i4 = Math.min(point.x, point.y);
                    Point point2 = AndroidUtilities.displaySize;
                    height = Math.max(point2.x, point2.y);
                } else {
                    int width = motionBackgroundDrawable4.getPatternBitmap().getWidth();
                    height = motionBackgroundDrawable4.getPatternBitmap().getHeight();
                    i4 = width;
                }
                Bitmap createBitmap = Bitmap.createBitmap(i4, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                backgroundDrawableSettings.wallpaper.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                backgroundDrawableSettings.wallpaper.draw(canvas);
                backgroundDrawableSettings.wallpaper = new BitmapDrawable(createBitmap);
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
        motionBackgroundDrawable.setPatternBitmap(34, SvgHelper.getBitmap(R.raw.default_pattern, i, i2, -16777216));
        motionBackgroundDrawable.setPatternColorFilter(motionBackgroundDrawable.getPatternColor());
        return motionBackgroundDrawable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bitmap loadScreenSizedBitmap(FileInputStream fileInputStream, int i) {
        float min;
        try {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                options.inJustDecodeBounds = true;
                long j = i;
                fileInputStream.getChannel().position(j);
                BitmapFactory.decodeStream(fileInputStream, null, options);
                float f = options.outWidth;
                float f2 = options.outHeight;
                Point point = AndroidUtilities.displaySize;
                int min2 = Math.min(point.x, point.y);
                Point point2 = AndroidUtilities.displaySize;
                int max = Math.max(point2.x, point2.y);
                if (min2 >= max && f > f2) {
                    min = Math.max(f / min2, f2 / max);
                } else {
                    min = Math.min(f / min2, f2 / max);
                }
                if (min < 1.2f) {
                    min = 1.0f;
                }
                options.inJustDecodeBounds = false;
                if (min > 1.0f && (f > min2 || f2 > max)) {
                    int i2 = 1;
                    do {
                        i2 *= 2;
                    } while (i2 * 2 < min);
                    options.inSampleSize = i2;
                } else {
                    options.inSampleSize = (int) min;
                }
                fileInputStream.getChannel().position(j);
                Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream, null, options);
                if (decodeStream.getWidth() < min2 || decodeStream.getHeight() < max) {
                    float max2 = Math.max(min2 / decodeStream.getWidth(), max / decodeStream.getHeight());
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

    /* JADX WARN: Can't wrap try/catch for region: R(7:1|(3:3|(1:5)|(2:7|8)(4:10|(1:97)(1:20)|(2:22|(1:24))(1:(5:88|(1:90)(1:95)|(1:92)|93|94)(1:96))|25))(3:98|(2:100|(6:104|(1:106)(1:108)|107|27|(7:29|30|31|32|(2:34|(2:35|(1:41)(1:39)))(0)|43|(6:45|(1:47)(1:55)|48|49|50|51)(1:(4:57|58|59|60)(2:64|65)))|84))|109)|26|27|(0)|84|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0142, code lost:
        r10 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0143, code lost:
        org.telegram.messenger.FileLog.e(r10);
     */
    /* JADX WARN: Removed duplicated region for block: B:108:0x00c2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Drawable getThemedWallpaper(final boolean z, final View view) {
        MotionBackgroundDrawable motionBackgroundDrawable;
        File file;
        int i;
        FileInputStream fileInputStream;
        Bitmap decodeStream;
        MotionBackgroundDrawable motionBackgroundDrawable2;
        File pathToWallpaper;
        int i2 = currentColors.get(key_chat_wallpaper);
        int i3 = 1;
        if (i2 != 0) {
            int i4 = currentColors.get(key_chat_wallpaper_gradient_to1);
            int i5 = currentColors.get(key_chat_wallpaper_gradient_to2);
            int i6 = currentColors.get(key_chat_wallpaper_gradient_to3);
            int i7 = currentColors.get(key_chat_wallpaper_gradient_rotation, -1);
            if (i7 == -1) {
                i7 = 45;
            }
            if (i4 == 0) {
                return new ColorDrawable(i2);
            }
            ThemeAccent accent = currentTheme.getAccent(false);
            file = (accent == null || TextUtils.isEmpty(accent.patternSlug) || previousTheme != null || (pathToWallpaper = accent.getPathToWallpaper()) == null || !pathToWallpaper.exists()) ? null : pathToWallpaper;
            if (i5 != 0) {
                motionBackgroundDrawable2 = new MotionBackgroundDrawable(i2, i4, i5, i6, true);
                if (file == null) {
                    return motionBackgroundDrawable2;
                }
            } else if (file == null) {
                BackgroundGradientDrawable backgroundGradientDrawable = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(i7), new int[]{i2, i4});
                backgroundGradientDrawable.startDithering(!z ? BackgroundGradientDrawable.Sizes.ofDeviceScreen() : BackgroundGradientDrawable.Sizes.ofDeviceScreen(0.125f, BackgroundGradientDrawable.Sizes.Orientation.PORTRAIT), view != null ? new BackgroundGradientDrawable.ListenerAdapter() { // from class: org.telegram.ui.ActionBar.Theme.13
                    @Override // org.telegram.ui.Components.BackgroundGradientDrawable.ListenerAdapter, org.telegram.ui.Components.BackgroundGradientDrawable.Listener
                    public void onSizeReady(int i8, int i9) {
                        if (!z) {
                            Point point = AndroidUtilities.displaySize;
                            if ((point.x <= point.y) == (i8 <= i9)) {
                                view.invalidate();
                                return;
                            }
                            return;
                        }
                        view.invalidate();
                    }
                } : null);
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
                                        i3 *= 2;
                                        f /= 2.0f;
                                        f2 /= 2.0f;
                                    }
                                }
                                options.inJustDecodeBounds = false;
                                options.inSampleSize = i3;
                                decodeStream = BitmapFactory.decodeStream(fileInputStream, null, options);
                            } catch (Throwable th) {
                                th = th;
                                try {
                                    FileLog.e(th);
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                    return null;
                                } catch (Throwable th2) {
                                    if (fileInputStream != null) {
                                        try {
                                            fileInputStream.close();
                                        } catch (Exception e) {
                                            FileLog.e(e);
                                        }
                                    }
                                    throw th2;
                                }
                            }
                        } catch (Throwable th3) {
                            th = th3;
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
                Theme.lambda$unrefAudioVisualizeDrawable$13(MessageObject.this);
            }
        }, 200L);
        chat_msgAudioVisualizeDrawable = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$unrefAudioVisualizeDrawable$13(MessageObject messageObject) {
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
        statusDrawable2.setColor(getColor(key_chats_actionMessage));
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

    public static SparseIntArray getFallbackKeys() {
        return fallbackKeys;
    }

    public static int getFallbackKey(int i) {
        return fallbackKeys.get(i);
    }

    public static Map<String, Drawable> getThemeDrawablesMap() {
        return defaultChatDrawables;
    }

    public static Drawable getThemeDrawable(String str) {
        return defaultChatDrawables.get(str);
    }

    public static int getThemeDrawableColorKey(String str) {
        return defaultChatDrawableColorKeys.get(str).intValue();
    }

    public static Map<String, Paint> getThemePaintsMap() {
        return defaultChatPaints;
    }

    public static Paint getThemePaint(String str) {
        return defaultChatPaints.get(str);
    }

    public static int getThemePaintColorKey(String str) {
        return defaultChatPaintColors.get(str).intValue();
    }

    private static void addChatDrawable(String str, Drawable drawable, int i) {
        defaultChatDrawables.put(str, drawable);
        defaultChatDrawableColorKeys.put(str, Integer.valueOf(i));
    }

    private static void addChatPaint(String str, Paint paint, int i) {
        defaultChatPaints.put(str, paint);
        defaultChatPaintColors.put(str, Integer.valueOf(i));
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

    public static void turnOffAutoNight(final BaseFragment baseFragment) {
        String string;
        if (selectedAutoNightType != 0) {
            if (baseFragment != null) {
                try {
                    BulletinFactory of = BulletinFactory.of(baseFragment);
                    int i = R.raw.auto_night_off;
                    if (selectedAutoNightType == 3) {
                        string = LocaleController.getString("AutoNightSystemModeOff", R.string.AutoNightSystemModeOff);
                    } else {
                        string = LocaleController.getString("AutoNightModeOff", R.string.AutoNightModeOff);
                    }
                    of.createSimpleBulletin(i, string, LocaleController.getString("Settings", R.string.Settings), 5000, new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda6
                        @Override // java.lang.Runnable
                        public final void run() {
                            Theme.lambda$turnOffAutoNight$14(BaseFragment.this);
                        }
                    }).show();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            selectedAutoNightType = 0;
            saveAutoNightThemeConfig();
            cancelAutoNightThemeCallbacks();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$turnOffAutoNight$14(BaseFragment baseFragment) {
        baseFragment.presentFragment(new ThemeActivity(1));
    }

    public static void turnOffAutoNight(FrameLayout frameLayout, Runnable runnable) {
        String string;
        if (selectedAutoNightType != 0) {
            if (frameLayout != null && runnable != null) {
                try {
                    BulletinFactory of = BulletinFactory.of(frameLayout, null);
                    int i = R.raw.auto_night_off;
                    if (selectedAutoNightType == 3) {
                        string = LocaleController.getString("AutoNightSystemModeOff", R.string.AutoNightSystemModeOff);
                    } else {
                        string = LocaleController.getString("AutoNightModeOff", R.string.AutoNightModeOff);
                    }
                    of.createSimpleBulletin(i, string, LocaleController.getString("Settings", R.string.Settings), 5000, runnable).show();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            selectedAutoNightType = 0;
            saveAutoNightThemeConfig();
            cancelAutoNightThemeCallbacks();
        }
    }
}
