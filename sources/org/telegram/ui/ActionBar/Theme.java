package org.telegram.ui.ActionBar;

import android.R;
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
import android.graphics.Typeface;
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
import androidx.appcompat.widget.AppCompatImageHelper$$ExternalSyntheticApiModelOutline0;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
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
import org.telegram.messenger.MessagesController$$ExternalSyntheticLambda91;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.time.SunDate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.BlurSettingsBottomSheet;
import org.telegram.ui.Cells.BaseCell;
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
import org.telegram.ui.ThemePreviewActivity;

/* loaded from: classes4.dex */
public abstract class Theme {
    public static Paint DEBUG_BLUE;
    public static Paint DEBUG_RED;
    private static Method StateListDrawable_getStateDrawableMethod;
    private static SensorEventListener ambientSensorListener;
    private static HashMap animatedOutVisualizerDrawables;
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
    private static Bitmap blurredBitmap;
    public static Drawable calllog_msgCallDownGreenDrawable;
    public static Drawable calllog_msgCallDownRedDrawable;
    public static Drawable calllog_msgCallUpGreenDrawable;
    public static Drawable calllog_msgCallUpRedDrawable;
    private static boolean canStartHolidayAnimation;
    private static boolean changingWallpaper;
    public static Paint chat_actionBackgroundGradientDarkenPaint;
    public static Paint chat_actionBackgroundPaint;
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
    public static Drawable chat_closeIconDrawable;
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
    public static TextPaint chat_infoBoldPaint;
    public static TextPaint chat_infoPaint;
    public static Drawable chat_inlineResultAudio;
    public static Drawable chat_inlineResultFile;
    public static Drawable chat_inlineResultLocation;
    public static Paint chat_instantViewButtonPaint;
    public static TextPaint chat_instantViewPaint;
    public static Paint chat_instantViewRectPaint;
    public static TextPaint chat_livePaint;
    public static TextPaint chat_locationAddressPaint;
    public static TextPaint chat_locationTitlePaint;
    public static Drawable chat_lockIconDrawable;
    public static Paint chat_messageBackgroundSelectedPaint;
    public static Drawable chat_moreIconDrawable;
    private static AudioVisualizerDrawable chat_msgAudioVisualizeDrawable;
    public static TextPaint chat_msgBotButtonPaint;
    public static Drawable chat_msgCallDownGreenDrawable;
    public static Drawable chat_msgCallDownRedDrawable;
    public static Drawable chat_msgCallUpGreenDrawable;
    public static MsgClockDrawable chat_msgClockDrawable;
    public static TextPaint chat_msgCodeBgPaint;
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
    public static TextPaint chat_msgTextCode2Paint;
    public static TextPaint chat_msgTextCode3Paint;
    public static TextPaint chat_msgTextCodePaint;
    public static TextPaint chat_msgTextPaint;
    public static TextPaint[] chat_msgTextPaintEmoji;
    public static TextPaint chat_msgTextPaintOneEmoji;
    public static TextPaint chat_msgTextPaintThreeEmoji;
    public static TextPaint chat_msgTextPaintTwoEmoji;
    public static Drawable chat_msgUnlockDrawable;
    public static Drawable chat_muteIconDrawable;
    public static TextPaint chat_namePaint;
    public static PorterDuffColorFilter chat_outAnimatedEmojiTextColorFilter;
    public static Paint chat_outUrlPaint;
    public static Paint chat_pollTimerPaint;
    public static TextPaint chat_quoteTextPaint;
    public static Paint chat_radialProgress2Paint;
    public static Paint chat_radialProgressPaint;
    public static Paint chat_radialProgressPausedPaint;
    public static Paint chat_radialProgressPausedSeekbarPaint;
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
    public static TextPaint chat_titleLabelTextPaint;
    public static TextPaint chat_topicTextPaint;
    public static TextPaint chat_unlockExtendedMediaTextPaint;
    public static Paint chat_urlPaint;
    public static Paint checkboxSquare_backgroundPaint;
    public static Paint checkboxSquare_checkPaint;
    public static Paint checkboxSquare_eraserPaint;
    public static int colorsCount;
    public static int currentColor;
    private static SparseIntArray currentColors;
    private static SparseIntArray currentColorsNoAccent;
    private static ThemeInfo currentDayTheme;
    private static ThemeInfo currentNightTheme;
    private static ThemeInfo currentTheme;
    private static final HashMap defaultChatDrawableColorKeys;
    private static final HashMap defaultChatDrawables;
    private static final HashMap defaultChatPaintColors;
    private static final HashMap defaultChatPaints;
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
    public static Paint dialogs_tagPaint;
    public static TextPaint dialogs_tagTextPaint;
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
    private static final ThreadLocal hsvTemp1Local;
    private static final ThreadLocal hsvTemp2Local;
    private static final ThreadLocal hsvTemp3Local;
    private static final ThreadLocal hsvTemp4Local;
    private static final ThreadLocal hsvTemp5Local;
    private static boolean isApplyingAccent;
    private static boolean isCustomTheme;
    private static boolean isInNigthMode;
    private static boolean isPatternWallpaper;
    private static boolean isWallpaperMotion;
    public static final int key_actionBarActionModeDefault;
    public static final int key_actionBarActionModeDefaultIcon;
    public static final int key_actionBarActionModeDefaultSelector;
    public static final int key_actionBarActionModeDefaultTop;
    public static final int key_actionBarActionModeReaction;
    public static final int key_actionBarActionModeReactionDot;
    public static final int key_actionBarActionModeReactionText;
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
    public static final int key_avatar_backgroundGray;
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
    public static final int key_chat_BlurAlphaSlow;
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
    public static final int key_chat_editMediaButton;
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
    public static final int key_chat_inCodeBackground;
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
    public static final int key_chat_inQuote;
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
    public static final int key_chat_outCodeBackground;
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
    public static final int key_chat_outQuote;
    public static final int key_chat_outReactionButtonBackground;
    public static final int key_chat_outReactionButtonText;
    public static final int key_chat_outReactionButtonTextSelected;
    public static final int key_chat_outReplyLine;
    public static final int key_chat_outReplyLine2;
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
    public static final int key_code_comment;
    public static final int key_code_constant;
    public static final int key_code_function;
    public static final int key_code_keyword;
    public static final int key_code_number;
    public static final int key_code_operator;
    public static final int key_code_string;
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
    public static final int key_dialogCardShadow;
    public static final int key_dialogCheckboxSquareBackground;
    public static final int key_dialogCheckboxSquareCheck;
    public static final int key_dialogCheckboxSquareDisabled;
    public static final int key_dialogCheckboxSquareUnchecked;
    public static final int key_dialogEmptyImage;
    public static final int key_dialogEmptyText;
    public static final int key_dialogFloatingButton;
    public static final int key_dialogFloatingButtonPressed;
    public static final int key_dialogFloatingIcon;
    public static final int key_dialogGiftsBackground;
    public static final int key_dialogGiftsTabText;
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
    public static final int key_iv_ab_progress;
    public static final int key_iv_background;
    public static final int key_iv_backgroundGray;
    public static final int key_iv_navigationBackground;
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
    public static final int key_premiumCoinGradient1;
    public static final int key_premiumCoinGradient2;
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
    public static final int key_premiumStarGradient1;
    public static final int key_premiumStarGradient2;
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
    public static final int key_reactionStarSelector;
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
    public static final int key_starsGradient1;
    public static final int key_starsGradient2;
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
    public static final int key_stories_circle1;
    public static final int key_stories_circle2;
    public static final int key_stories_circle_closeFriends1;
    public static final int key_stories_circle_closeFriends2;
    public static final int key_stories_circle_dialog1;
    public static final int key_stories_circle_dialog2;
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
    public static final int key_table_background;
    public static final int key_table_border;
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
    public static final int key_voipgroup_rtmpButton;
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
    private static WeakReference lastDrawableToBlur;
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
    private static ArrayList otherThemes;
    private static int patternIntensity;
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
    private static int serviceMessageColor;
    public static int serviceMessageColorBackup;
    private static int serviceSelectedMessageColor;
    public static int serviceSelectedMessageColorBackup;
    private static boolean shouldDrawGradientIcons;
    private static boolean switchDayRunnableScheduled;
    private static boolean switchNightRunnableScheduled;
    private static int switchNightThemeDelay;
    private static boolean switchingNightTheme;
    private static HashSet themeAccentExclusionKeys;
    private static Drawable themedWallpaper;
    private static int themedWallpaperFileOffset;
    private static String themedWallpaperLink;
    public static ArrayList themes;
    private static HashMap themesDict;
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
    public static Drawable[] avatarDrawables = new Drawable[24];
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
    public static Drawable[][] chat_fileStatesDrawable = (Drawable[][]) Array.newInstance((Class<?>) Drawable.class, 5, 2);
    public static Path[] chat_filePath = new Path[2];
    public static Path[] chat_updatePath = new Path[3];

    /* loaded from: classes4.dex */
    public static class AdaptiveRipple {
        private static final int defaultBackgroundColorKey = Theme.key_windowBackgroundWhite;
        private static float[] tempHSV;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class CircleDrawable extends Drawable {
            private static Paint maskPaint;
            private Paint paint;
            private float radius;

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
                Rect bounds = getBounds();
                canvas.drawCircle(bounds.centerX(), bounds.centerY(), Math.abs(this.radius - (-1.0f)) < 0.01f ? Math.max(bounds.width(), bounds.height()) / 2 : Math.abs(this.radius - (-2.0f)) < 0.01f ? (int) Math.ceil(Math.sqrt(((bounds.left - bounds.centerX()) * (bounds.left - bounds.centerX())) + ((bounds.top - bounds.centerY()) * (bounds.top - bounds.centerY())))) : AndroidUtilities.dp(this.radius), this.paint);
            }

            @Override // android.graphics.drawable.Drawable
            public int getOpacity() {
                return -2;
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int i) {
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }
        }

        private static float[] calcRadii(float... fArr) {
            return fArr.length == 0 ? new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f} : fArr.length == 1 ? new float[]{AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0])} : fArr.length == 2 ? new float[]{AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[1])} : fArr.length == 3 ? new float[]{AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[2]), AndroidUtilities.dp(fArr[2]), AndroidUtilities.dp(fArr[2]), AndroidUtilities.dp(fArr[2])} : fArr.length < 8 ? new float[]{AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[2]), AndroidUtilities.dp(fArr[2]), AndroidUtilities.dp(fArr[3]), AndroidUtilities.dp(fArr[3])} : new float[]{AndroidUtilities.dp(fArr[0]), AndroidUtilities.dp(fArr[1]), AndroidUtilities.dp(fArr[2]), AndroidUtilities.dp(fArr[3]), AndroidUtilities.dp(fArr[4]), AndroidUtilities.dp(fArr[5]), AndroidUtilities.dp(fArr[6]), AndroidUtilities.dp(fArr[7])};
        }

        public static int calcRippleColor(int i) {
            if (tempHSV == null) {
                tempHSV = new float[3];
            }
            Color.colorToHSV(i, tempHSV);
            float[] fArr = tempHSV;
            float f = fArr[1];
            if (f > 0.01f) {
                fArr[1] = Math.min(1.0f, Math.max(0.0f, f + (Theme.isCurrentThemeDark() ? 0.25f : -0.25f)));
                float[] fArr2 = tempHSV;
                fArr2[2] = Math.min(1.0f, Math.max(0.0f, fArr2[2] + (Theme.isCurrentThemeDark() ? 0.05f : -0.05f)));
            } else {
                fArr[2] = Math.min(1.0f, Math.max(0.0f, fArr[2] + (Theme.isCurrentThemeDark() ? 0.1f : -0.1f)));
            }
            return Color.HSVToColor(NotificationCenter.dialogTranslate, tempHSV);
        }

        public static Drawable circle(int i) {
            return circle(i, -1.0f);
        }

        public static Drawable circle(int i, float f) {
            return createCircle(calcRippleColor(i), f);
        }

        private static Drawable createCircle(int i, float f) {
            return createCircle(0, i, f);
        }

        private static Drawable createCircle(int i, int i2, float f) {
            return createCircle(i == 0 ? null : new CircleDrawable(f, i), i2, f);
        }

        private static Drawable createCircle(Drawable drawable, int i, float f) {
            if (Build.VERSION.SDK_INT >= 21) {
                return new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), drawable, new CircleDrawable(f));
            }
            StateListDrawable stateListDrawable = new StateListDrawable();
            Drawable circleDrawable = new CircleDrawable(f, i);
            if (drawable != null) {
                circleDrawable = new LayerDrawable(new Drawable[]{drawable, circleDrawable});
            }
            stateListDrawable.addState(new int[]{R.attr.state_pressed}, circleDrawable);
            stateListDrawable.addState(new int[]{R.attr.state_selected}, circleDrawable);
            stateListDrawable.addState(StateSet.WILD_CARD, drawable);
            return stateListDrawable;
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

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r7v6, types: [android.graphics.drawable.LayerDrawable] */
        private static Drawable createRect(Drawable drawable, int i, float... fArr) {
            if (Build.VERSION.SDK_INT >= 21) {
                ShapeDrawable shapeDrawable = hasNonzeroRadii(fArr) ? new ShapeDrawable(new RoundRectShape(calcRadii(fArr), null, null)) : new ShapeDrawable(new RectShape());
                shapeDrawable.getPaint().setColor(-1);
                return new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), drawable, shapeDrawable);
            }
            StateListDrawable stateListDrawable = new StateListDrawable();
            ShapeDrawable shapeDrawable2 = hasNonzeroRadii(fArr) ? new ShapeDrawable(new RoundRectShape(calcRadii(fArr), null, null)) : new ShapeDrawable(new RectShape());
            shapeDrawable2.getPaint().setColor(i);
            if (drawable != null) {
                shapeDrawable2 = new LayerDrawable(new Drawable[]{drawable, shapeDrawable2});
            }
            stateListDrawable.addState(new int[]{R.attr.state_pressed}, shapeDrawable2);
            stateListDrawable.addState(new int[]{R.attr.state_selected}, shapeDrawable2);
            stateListDrawable.addState(StateSet.WILD_CARD, drawable);
            return stateListDrawable;
        }

        public static Drawable filledCircle() {
            return filledCircle(null, Theme.getColor(defaultBackgroundColorKey), -1.0f);
        }

        public static Drawable filledCircle(Drawable drawable, int i, float f) {
            return createCircle(drawable, calcRippleColor(i), f);
        }

        public static Drawable filledRect() {
            return filledRect(Theme.getColor(defaultBackgroundColorKey), 0.0f);
        }

        public static Drawable filledRect(int i) {
            return createRect(i, calcRippleColor(i), new float[0]);
        }

        public static Drawable filledRect(int i, float... fArr) {
            return createRect(i, calcRippleColor(i), fArr);
        }

        public static Drawable filledRectByKey(int i) {
            return filledRect(Theme.getColor(i));
        }

        public static Drawable filledRectByKey(int i, float... fArr) {
            return filledRect(Theme.getColor(i), fArr);
        }

        private static boolean hasNonzeroRadii(float... fArr) {
            for (int i = 0; i < Math.min(8, fArr.length); i++) {
                if (fArr[i] > 0.0f) {
                    return true;
                }
            }
            return false;
        }

        public static Drawable rect(int i) {
            return rect(i, 0.0f);
        }

        public static Drawable rect(int i, float... fArr) {
            return createRect(0, calcRippleColor(i), fArr);
        }

        public static Drawable rectByKey(int i, float... fArr) {
            return rect(Theme.getColor(i), fArr);
        }
    }

    /* loaded from: classes4.dex */
    public static class BackgroundDrawableSettings {
        public Boolean isCustomTheme;
        public Boolean isPatternWallpaper;
        public Boolean isWallpaperMotion;
        public Drawable themedWallpaper;
        public Drawable wallpaper;
    }

    /* loaded from: classes4.dex */
    public interface Colorable {
        void updateColors();
    }

    /* loaded from: classes4.dex */
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

        /* loaded from: classes4.dex */
        public static class PathDrawParams {
            boolean lastDrawFullBottom;
            boolean lastDrawFullTop;
            Path path = new Path();
            Rect lastRect = new Rect();

            public Path getPath() {
                return this.path;
            }

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
            this.backgroundDrawable = (Drawable[][]) Array.newInstance((Class<?>) Drawable.class, 4, 4);
            this.backgroundDrawableColor = new int[][]{new int[]{-1, -1, -1, -1}, new int[]{-1, -1, -1, -1}, new int[]{-1, -1, -1, -1}, new int[]{-1, -1, -1, -1}};
            this.resourcesProvider = resourcesProvider;
            this.isOut = z;
            this.currentType = i;
            this.isSelected = z2;
            this.path = new Path();
            this.selectedPaint = new Paint(1);
            this.alpha = NotificationCenter.closeSearchByActiveAction;
        }

        private int dp(float f) {
            return this.currentType == 2 ? (int) Math.ceil(f * 3.0f) : AndroidUtilities.dp(f);
        }

        private void generatePath(Path path, Rect rect, int i, int i2, int i3, int i4, int i5, boolean z, boolean z2, boolean z3) {
            RectF rectF;
            float f;
            float f2;
            int dp;
            path.rewind();
            int height = (rect.height() - i) >> 1;
            int i6 = i2;
            if (i6 > height) {
                i6 = height;
            }
            if (this.isOut) {
                if (this.drawFullBubble || this.currentType == 2 || z3 || z) {
                    int i7 = this.botButtonsBottom ? i4 : i6;
                    path.moveTo(this.currentType == 1 ? (rect.right - dp(8.0f)) - i7 : rect.right - dp(2.6f), rect.bottom - i);
                    path.lineTo(rect.left + i + i7, rect.bottom - i);
                    int i8 = i7 * 2;
                    this.rect.set(rect.left + i, r10 - i8, r11 + i8, rect.bottom - i);
                    path.arcTo(this.rect, 90.0f, 90.0f, false);
                } else {
                    path.moveTo(rect.right - dp(8.0f), (i5 - this.topY) + this.currentBackgroundHeight);
                    path.lineTo(rect.left + i, (i5 - this.topY) + this.currentBackgroundHeight);
                }
                if (this.drawFullBubble || this.currentType == 2 || z3 || z2) {
                    path.lineTo(rect.left + i, rect.top + i + i6);
                    int i9 = i6 * 2;
                    this.rect.set(rect.left + i, rect.top + i, r9 + i9, r11 + i9);
                    path.arcTo(this.rect, 180.0f, 90.0f, false);
                    int i10 = this.isTopNear ? i4 : i6;
                    if (this.currentType == 1) {
                        path.lineTo((rect.right - i) - i10, rect.top + i);
                        RectF rectF2 = this.rect;
                        int i11 = rect.right - i;
                        int i12 = i10 * 2;
                        rectF2.set(i11 - i12, rect.top + i, i11, r12 + i12);
                    } else {
                        path.lineTo((rect.right - dp(8.0f)) - i10, rect.top + i);
                        int i13 = i10 * 2;
                        this.rect.set((rect.right - dp(8.0f)) - i13, rect.top + i, rect.right - dp(8.0f), rect.top + i + i13);
                    }
                    path.arcTo(this.rect, 270.0f, 90.0f, false);
                } else {
                    path.lineTo(rect.left + i, (i5 - this.topY) - dp(2.0f));
                    path.lineTo(this.currentType == 1 ? rect.right - i : rect.right - dp(8.0f), (i5 - this.topY) - dp(2.0f));
                }
                int i14 = this.currentType;
                if (i14 == 1) {
                    if (z3 || z) {
                        if (this.isBottomNear) {
                            i6 = i4;
                        }
                        path.lineTo(rect.right - i, (rect.bottom - i) - i6);
                        int i15 = i6 * 2;
                        this.rect.set(r4 - i15, r2 - i15, rect.right - i, rect.bottom - i);
                        path.arcTo(this.rect, 0.0f, 90.0f, false);
                    } else {
                        dp = rect.right - i;
                    }
                } else if (this.drawFullBubble || i14 == 2 || z3 || z) {
                    path.lineTo(rect.right - dp(8.0f), ((rect.bottom - i) - i3) - dp(3.0f));
                    int i16 = i3 * 2;
                    this.rect.set(rect.right - dp(8.0f), ((rect.bottom - i) - i16) - dp(9.0f), (rect.right - dp(7.0f)) + i16, (rect.bottom - i) - dp(1.0f));
                    rectF = this.rect;
                    f = -83.0f;
                    f2 = 180.0f;
                    path.arcTo(rectF, f2, f, false);
                } else {
                    dp = rect.right - dp(8.0f);
                }
                path.lineTo(dp, (i5 - this.topY) + this.currentBackgroundHeight);
            } else {
                if (this.drawFullBubble || this.currentType == 2 || z3 || z) {
                    int i17 = this.botButtonsBottom ? i4 : i6;
                    path.moveTo(this.currentType == 1 ? rect.left + dp(8.0f) + i17 : rect.left + dp(2.6f), rect.bottom - i);
                    path.lineTo((rect.right - i) - i17, rect.bottom - i);
                    int i18 = i17 * 2;
                    this.rect.set(r11 - i18, r13 - i18, rect.right - i, rect.bottom - i);
                    path.arcTo(this.rect, 90.0f, -90.0f, false);
                } else {
                    path.moveTo(rect.left + dp(8.0f), (i5 - this.topY) + this.currentBackgroundHeight);
                    path.lineTo(rect.right - i, (i5 - this.topY) + this.currentBackgroundHeight);
                }
                if (this.drawFullBubble || this.currentType == 2 || z3 || z2) {
                    path.lineTo(rect.right - i, rect.top + i + i6);
                    RectF rectF3 = this.rect;
                    int i19 = rect.right - i;
                    int i20 = i6 * 2;
                    rectF3.set(i19 - i20, rect.top + i, i19, r13 + i20);
                    path.arcTo(this.rect, 0.0f, -90.0f, false);
                    int i21 = this.isTopNear ? i4 : i6;
                    if (this.currentType == 1) {
                        path.lineTo(rect.left + i + i21, rect.top + i);
                        int i22 = i21 * 2;
                        this.rect.set(rect.left + i, rect.top + i, r11 + i22, r13 + i22);
                    } else {
                        path.lineTo(rect.left + dp(8.0f) + i21, rect.top + i);
                        int i23 = i21 * 2;
                        this.rect.set(rect.left + dp(8.0f), rect.top + i, rect.left + dp(8.0f) + i23, rect.top + i + i23);
                    }
                    path.arcTo(this.rect, 270.0f, -90.0f, false);
                } else {
                    path.lineTo(rect.right - i, (i5 - this.topY) - dp(2.0f));
                    path.lineTo(this.currentType == 1 ? rect.left + i : rect.left + dp(8.0f), (i5 - this.topY) - dp(2.0f));
                }
                int i24 = this.currentType;
                if (i24 == 1) {
                    if (z3 || z) {
                        if (this.isBottomNear || this.botButtonsBottom) {
                            i6 = i4;
                        }
                        path.lineTo(rect.left + i, (rect.bottom - i) - i6);
                        int i25 = i6 * 2;
                        this.rect.set(rect.left + i, r2 - i25, r4 + i25, rect.bottom - i);
                        path.arcTo(this.rect, 180.0f, -90.0f, false);
                    } else {
                        dp = rect.left + i;
                    }
                } else if (this.drawFullBubble || i24 == 2 || z3 || z) {
                    path.lineTo(rect.left + dp(8.0f), ((rect.bottom - i) - i3) - dp(3.0f));
                    int i26 = i3 * 2;
                    this.rect.set((rect.left + dp(7.0f)) - i26, ((rect.bottom - i) - i26) - dp(9.0f), rect.left + dp(8.0f), (rect.bottom - i) - dp(1.0f));
                    rectF = this.rect;
                    f = 83.0f;
                    f2 = 0.0f;
                    path.arcTo(rectF, f2, f, false);
                } else {
                    dp = rect.left + dp(8.0f);
                }
                path.lineTo(dp, (i5 - this.topY) + this.currentBackgroundHeight);
            }
            path.close();
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

        public void applyMatrixScale() {
            float min;
            Bitmap bitmap;
            if (this.gradientShader instanceof BitmapShader) {
                if (!this.isCrossfadeBackground || (bitmap = this.crosfadeFromBitmap) == null) {
                    if (this.themePreview) {
                        r2 = 2;
                    } else if (this.currentType == 2) {
                        r2 = 1;
                    }
                    Bitmap bitmap2 = motionBackground[r2].getBitmap();
                    min = Math.min(bitmap2.getWidth() / motionBackground[r2].getBounds().width(), bitmap2.getHeight() / motionBackground[r2].getBounds().height());
                } else {
                    r2 = this.currentType == 2 ? (char) 1 : (char) 0;
                    min = Math.min(bitmap.getWidth() / motionBackground[r2].getBounds().width(), this.crosfadeFromBitmap.getHeight() / motionBackground[r2].getBounds().height());
                }
                float f = 1.0f / min;
                this.matrix.postScale(f, f);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            MessageDrawable messageDrawable = this.crossfadeFromDrawable;
            if (messageDrawable == null) {
                draw(canvas, null);
                return;
            }
            messageDrawable.draw(canvas);
            setAlpha((int) (this.crossfadeProgress * 255.0f));
            draw(canvas, null);
            setAlpha(NotificationCenter.closeSearchByActiveAction);
        }

        /* JADX WARN: Removed duplicated region for block: B:32:0x0106  */
        /* JADX WARN: Removed duplicated region for block: B:47:0x0118  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x011a  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x010d  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void draw(Canvas canvas, Paint paint) {
            int dp;
            int dp2;
            int i;
            int i2;
            boolean z;
            boolean z2;
            PathDrawParams pathDrawParams;
            Path path;
            boolean z3;
            Drawable backgroundDrawable;
            Rect bounds = getBounds();
            if (paint == null && this.gradientShader == null && this.overrideRoundRadius == 0 && this.overrideRounding <= 0.0f && (backgroundDrawable = getBackgroundDrawable()) != null) {
                backgroundDrawable.setBounds(bounds);
                backgroundDrawable.draw(canvas);
                return;
            }
            int dp3 = dp(2.0f);
            int i3 = this.overrideRoundRadius;
            if (i3 != 0) {
                i2 = i3;
                i = i2;
            } else {
                if (this.overrideRounding > 0.0f) {
                    dp = AndroidUtilities.lerp(dp(SharedConfig.bubbleRadius), Math.min(bounds.width(), bounds.height()) / 2, this.overrideRounding);
                    dp2 = AndroidUtilities.lerp(dp(Math.min(6, SharedConfig.bubbleRadius)), Math.min(bounds.width(), bounds.height()) / 2, this.overrideRounding);
                } else if (this.currentType == 2) {
                    dp = dp(6.0f);
                    dp2 = dp(6.0f);
                } else {
                    dp = dp(SharedConfig.bubbleRadius);
                    dp2 = dp(Math.min(6, SharedConfig.bubbleRadius));
                }
                i = dp2;
                i2 = dp;
            }
            int dp4 = dp(6.0f);
            Paint paint2 = paint == null ? this.paint : paint;
            if (paint == null && this.gradientShader != null) {
                this.matrix.reset();
                applyMatrixScale();
                this.matrix.postTranslate(0.0f, -this.topY);
                this.gradientShader.setLocalMatrix(this.matrix);
            }
            int max = Math.max(bounds.top, 0);
            if (this.pathDrawCacheParams == null || bounds.height() >= this.currentBackgroundHeight) {
                boolean z4 = this.currentType != 1 ? (this.topY + bounds.bottom) - i2 < this.currentBackgroundHeight : (this.topY + bounds.bottom) - (dp4 * 2) < this.currentBackgroundHeight;
                if (this.topY + (i2 * 2) < 0) {
                    z = z4;
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
                        generatePath(path, bounds, dp3, i2, dp4, i, max, z, z2, paint == null);
                    }
                    canvas.drawPath(path, paint2);
                    if (this.gradientShader == null && this.isSelected && paint == null) {
                        this.selectedPaint.setColor(ColorUtils.setAlphaComponent(getColor(Theme.key_chat_outBubbleGradientSelectedOverlay), (int) ((Color.alpha(r0) * this.alpha) / 255.0f)));
                        canvas.drawPath(path, this.selectedPaint);
                        return;
                    }
                    return;
                }
                z = z4;
            } else {
                z = true;
            }
            z2 = true;
            pathDrawParams = this.pathDrawCacheParams;
            if (pathDrawParams == null) {
            }
            if (!z3) {
            }
            generatePath(path, bounds, dp3, i2, dp4, i, max, z, z2, paint == null);
            canvas.drawPath(path, paint2);
            if (this.gradientShader == null) {
            }
        }

        public void drawCached(Canvas canvas, PathDrawParams pathDrawParams) {
            drawCached(canvas, pathDrawParams, null);
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

        protected void finalize() {
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

        /* JADX WARN: Removed duplicated region for block: B:35:0x0186  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x01a5  */
        /* JADX WARN: Removed duplicated region for block: B:48:0x0190  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Drawable getBackgroundDrawable() {
            int width;
            int height;
            int i;
            int i2;
            boolean z;
            int color;
            Drawable drawable;
            int i3 = this.overrideRoundRadius;
            if (i3 == 0) {
                i3 = this.overrideRounding > 0.0f ? 0 : dp(SharedConfig.bubbleRadius);
            }
            boolean z2 = this.isTopNear;
            char c = (z2 && this.isBottomNear) ? (char) 3 : z2 ? (char) 2 : this.isBottomNear ? (char) 1 : (char) 0;
            boolean z3 = this.isSelected;
            char c2 = (z3 && this.botButtonsBottom) ? (char) 3 : z3 ? (char) 1 : this.botButtonsBottom ? (char) 2 : (char) 0;
            boolean z4 = (this.gradientShader != null || z3 || this.isCrossfadeBackground) ? false : true;
            int color2 = getColor(this.isOut ? Theme.key_chat_outBubbleShadow : Theme.key_chat_inBubbleShadow);
            if (this.lastDrawWithShadow != z4 || this.currentBackgroundDrawableRadius[c2][c] != i3 || (z4 && this.shadowDrawableColor[c] != color2)) {
                this.currentBackgroundDrawableRadius[c2][c] = i3;
                try {
                    Bitmap createBitmap = Bitmap.createBitmap(dp(50.0f), dp(40.0f), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    this.backupRect.set(getBounds());
                    if (z4) {
                        try {
                            this.shadowDrawableColor[c] = color2;
                            Paint paint = new Paint(1);
                            paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, dp(40.0f), new int[]{358573417, 694117737}, (float[]) null, Shader.TileMode.CLAMP));
                            paint.setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.MULTIPLY));
                            paint.setShadowLayer(2.0f, 0.0f, 1.0f, -1);
                            if (AndroidUtilities.density > 1.0f) {
                                width = createBitmap.getWidth() + 1;
                                height = createBitmap.getHeight() + 1;
                                i = -1;
                                i2 = -1;
                            } else {
                                width = createBitmap.getWidth();
                                height = createBitmap.getHeight();
                                i = 0;
                                i2 = 0;
                            }
                            setBounds(i, i2, width, height);
                            draw(canvas, paint);
                            if (AndroidUtilities.density > 1.0f) {
                                paint.setColor(0);
                                paint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
                                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                                setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                                draw(canvas, paint);
                            }
                        } catch (Throwable unused) {
                        }
                    }
                    Paint paint2 = new Paint(1);
                    paint2.setColor(-1);
                    try {
                        setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
                        draw(canvas, paint2);
                        this.backgroundDrawable[c2][c] = new NinePatchDrawable(createBitmap, getByteBuffer((createBitmap.getWidth() / 2) - 1, (createBitmap.getWidth() / 2) + 1, (createBitmap.getHeight() / 2) - 1, (createBitmap.getHeight() / 2) + 1).array(), new Rect(), null);
                        try {
                            setBounds(this.backupRect);
                        } catch (Throwable unused2) {
                        }
                        z = true;
                    } catch (Throwable unused3) {
                        z = false;
                        this.lastDrawWithShadow = z4;
                        color = getColor(this.isSelected ? this.isOut ? Theme.key_chat_outBubbleSelected : Theme.key_chat_inBubbleSelected : this.isOut ? Theme.key_chat_outBubble : Theme.key_chat_inBubble);
                        drawable = this.backgroundDrawable[c2][c];
                        if (drawable != null) {
                        }
                        return this.backgroundDrawable[c2][c];
                    }
                } catch (Throwable unused4) {
                }
                this.lastDrawWithShadow = z4;
                color = getColor(this.isSelected ? this.isOut ? Theme.key_chat_outBubbleSelected : Theme.key_chat_inBubbleSelected : this.isOut ? Theme.key_chat_outBubble : Theme.key_chat_inBubble);
                drawable = this.backgroundDrawable[c2][c];
                if (drawable != null && (this.backgroundDrawableColor[c2][c] != color || z)) {
                    drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                    this.backgroundDrawableColor[c2][c] = color;
                }
                return this.backgroundDrawable[c2][c];
            }
            z = false;
            this.lastDrawWithShadow = z4;
            color = getColor(this.isSelected ? this.isOut ? Theme.key_chat_outBubbleSelected : Theme.key_chat_inBubbleSelected : this.isOut ? Theme.key_chat_outBubble : Theme.key_chat_inBubble);
            drawable = this.backgroundDrawable[c2][c];
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                this.backgroundDrawableColor[c2][c] = color;
            }
            return this.backgroundDrawable[c2][c];
        }

        protected int getColor(int i) {
            ResourcesProvider resourcesProvider;
            if (this.currentType != 2 && (resourcesProvider = this.resourcesProvider) != null) {
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

        public Shader getGradientShader() {
            return this.gradientShader;
        }

        public Matrix getMatrix() {
            return this.matrix;
        }

        public MotionBackgroundDrawable getMotionBackgroundDrawable() {
            if (this.themePreview) {
                return motionBackground[2];
            }
            return motionBackground[this.currentType == 2 ? (char) 1 : (char) 0];
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        public Paint getPaint() {
            return this.paint;
        }

        public Drawable getShadowDrawable() {
            int width;
            int height;
            int i;
            if (this.isCrossfadeBackground) {
                return null;
            }
            if (this.gradientShader == null && !this.isSelected && this.crossfadeFromDrawable == null) {
                return null;
            }
            int dp = dp(SharedConfig.bubbleRadius);
            boolean z = this.isTopNear;
            boolean z2 = false;
            char c = (z && this.isBottomNear) ? (char) 3 : z ? (char) 2 : this.isBottomNear ? (char) 1 : (char) 0;
            int[] iArr = this.currentShadowDrawableRadius;
            if (iArr[c] != dp) {
                iArr[c] = dp;
                Bitmap bitmap = this.shadowDrawableBitmap[c];
                if (bitmap != null) {
                    bitmap.recycle();
                }
                try {
                    Bitmap createBitmap = Bitmap.createBitmap(dp(50.0f), dp(40.0f), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    Paint paint = new Paint(1);
                    paint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, dp(40.0f), new int[]{358573417, 694117737}, (float[]) null, Shader.TileMode.CLAMP));
                    int i2 = -1;
                    paint.setShadowLayer(2.0f, 0.0f, 1.0f, -1);
                    if (AndroidUtilities.density > 1.0f) {
                        width = createBitmap.getWidth() + 1;
                        height = createBitmap.getHeight() + 1;
                        i = -1;
                    } else {
                        width = createBitmap.getWidth();
                        height = createBitmap.getHeight();
                        i2 = 0;
                        i = 0;
                    }
                    setBounds(i2, i, width, height);
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
            Drawable drawable = this.shadowDrawable[c];
            if (drawable != null && (this.shadowDrawableColor[c] != color || z2)) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
                this.shadowDrawableColor[c] = color;
            }
            return this.shadowDrawable[c];
        }

        public Drawable[] getShadowDrawables() {
            return this.shadowDrawable;
        }

        public int getTopY() {
            return this.topY;
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

        public boolean hasGradient() {
            return this.gradientShader != null && Theme.shouldDrawGradientIcons;
        }

        public Path makePath() {
            return makePath(this.pathDrawCacheParams);
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x00b9  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x00c3  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Path makePath(PathDrawParams pathDrawParams) {
            int dp;
            int dp2;
            int i;
            int i2;
            boolean z;
            boolean z2;
            Path path;
            Rect bounds = getBounds();
            int dp3 = dp(2.0f);
            int i3 = this.overrideRoundRadius;
            if (i3 != 0) {
                i2 = i3;
                i = i2;
            } else {
                if (this.overrideRounding > 0.0f) {
                    dp = AndroidUtilities.lerp(dp(SharedConfig.bubbleRadius), Math.min(bounds.width(), bounds.height()) / 2, this.overrideRounding);
                    dp2 = AndroidUtilities.lerp(dp(Math.min(6, SharedConfig.bubbleRadius)), Math.min(bounds.width(), bounds.height()) / 2, this.overrideRounding);
                } else if (this.currentType == 2) {
                    dp = dp(6.0f);
                    dp2 = dp(6.0f);
                } else {
                    dp = dp(SharedConfig.bubbleRadius);
                    dp2 = dp(Math.min(6, SharedConfig.bubbleRadius));
                }
                i = dp2;
                i2 = dp;
            }
            int dp4 = dp(6.0f);
            int max = Math.max(bounds.top, 0);
            boolean z3 = true;
            if (pathDrawParams == null || bounds.height() >= this.currentBackgroundHeight) {
                z = this.currentType != 1 ? (this.topY + bounds.bottom) - i2 < this.currentBackgroundHeight : (this.topY + bounds.bottom) - (dp4 * 2) < this.currentBackgroundHeight;
                if (this.topY + (i2 * 2) < 0) {
                    z2 = false;
                    if (pathDrawParams == null) {
                        Path path2 = pathDrawParams.path;
                        z3 = pathDrawParams.invalidatePath(bounds, z, z2);
                        path = path2;
                    } else {
                        path = this.path;
                    }
                    if (!z3 || this.overrideRoundRadius != 0) {
                        generatePath(path, bounds, dp3, i2, dp4, i, max, z, z2, true);
                    }
                    return path;
                }
            } else {
                z = true;
            }
            z2 = true;
            if (pathDrawParams == null) {
            }
            if (!z3) {
            }
            generatePath(path, bounds, dp3, i2, dp4, i, max, z, z2, true);
            return path;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            if (this.alpha != i || this.paint.getAlpha() != i) {
                this.alpha = i;
                this.paint.setAlpha(i);
                if (this.isOut) {
                    this.selectedPaint.setAlpha((int) (Color.alpha(getColor(Theme.key_chat_outBubbleGradientSelectedOverlay)) * (i / 255.0f)));
                }
            }
            if (this.gradientShader == null) {
                Drawable backgroundDrawable = getBackgroundDrawable();
                if (backgroundDrawable.getAlpha() != i) {
                    backgroundDrawable.setAlpha(i);
                }
            }
        }

        public void setBotButtonsBottom(boolean z) {
            this.botButtonsBottom = z;
        }

        @Override // android.graphics.drawable.Drawable
        public void setBounds(int i, int i2, int i3, int i4) {
            super.setBounds(i, i2, i3, i4);
            MessageDrawable messageDrawable = this.crossfadeFromDrawable;
            if (messageDrawable != null) {
                messageDrawable.setBounds(i, i2, i3, i4);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(int i, PorterDuff.Mode mode) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public void setDrawFullBubble(boolean z) {
            this.drawFullBubble = z;
        }

        public void setResourceProvider(ResourcesProvider resourcesProvider) {
            this.resourcesProvider = resourcesProvider;
        }

        public void setRoundRadius(int i) {
            this.overrideRoundRadius = i;
        }

        public void setRoundingRadius(float f) {
            this.overrideRounding = f;
        }

        /* JADX WARN: Removed duplicated region for block: B:101:0x0072  */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0064  */
        /* JADX WARN: Removed duplicated region for block: B:16:0x006f  */
        /* JADX WARN: Removed duplicated region for block: B:46:0x0210  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x0226  */
        /* JADX WARN: Removed duplicated region for block: B:53:0x021d  */
        /* JADX WARN: Removed duplicated region for block: B:97:0x01f9  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void setTop(int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2) {
            int color;
            int i7;
            int i8;
            int i9;
            boolean z3;
            Shader linearGradient;
            int i10;
            int i11;
            MotionBackgroundDrawable motionBackgroundDrawable;
            MessageDrawable messageDrawable = this.crossfadeFromDrawable;
            if (messageDrawable != null) {
                messageDrawable.setTop(i, i2, i3, i4, i5, i6, z, z2);
            }
            if (this.isOut) {
                color = getColor(this.isSelected ? Theme.key_chat_outBubbleSelected : Theme.key_chat_outBubble);
                i7 = getCurrentColor(Theme.key_chat_outBubbleGradient1);
                i8 = getCurrentColor(Theme.key_chat_outBubbleGradient2);
                i9 = getCurrentColor(Theme.key_chat_outBubbleGradient3);
                if (getCurrentColor(Theme.key_chat_outBubbleGradientAnimated) != 0) {
                    z3 = true;
                    if (i7 != 0) {
                        color = getColor(Theme.key_chat_outBubble);
                    }
                    char c = !this.themePreview ? (char) 2 : this.currentType == 2 ? (char) 1 : (char) 0;
                    if (!this.isCrossfadeBackground && i8 != 0 && z3 && (motionBackgroundDrawable = motionBackground[c]) != null) {
                        int[] colors = motionBackgroundDrawable.getColors();
                        this.currentColor = colors[0];
                        this.currentGradientColor1 = colors[1];
                        this.currentGradientColor2 = colors[2];
                        this.currentGradientColor3 = colors[3];
                    }
                    if (!this.isCrossfadeBackground && i8 != 0 && z3) {
                        if (i3 == this.currentBackgroundHeight && this.crosfadeFromBitmapShader != null && this.currentColor == color && this.currentGradientColor1 == i7 && this.currentGradientColor2 == i8 && this.currentGradientColor3 == i9 && this.currentAnimateGradient == z3) {
                            i11 = -1;
                        } else {
                            if (this.crosfadeFromBitmap == null) {
                                this.crosfadeFromBitmap = Bitmap.createBitmap(60, 80, Bitmap.Config.ARGB_8888);
                                Bitmap bitmap = this.crosfadeFromBitmap;
                                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                                this.crosfadeFromBitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
                            }
                            MotionBackgroundDrawable[] motionBackgroundDrawableArr = motionBackground;
                            if (motionBackgroundDrawableArr[c] == null) {
                                motionBackgroundDrawableArr[c] = new MotionBackgroundDrawable();
                                if (this.currentType != 2) {
                                    motionBackground[c].setPostInvalidateParent(true);
                                }
                                motionBackground[c].setRoundRadius(dp(1.0f));
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
                    } else if (i7 == 0 && (this.gradientShader == null || i3 != this.currentBackgroundHeight || this.currentColor != color || this.currentGradientColor1 != i7 || this.currentGradientColor2 != i8 || this.currentGradientColor3 != i9 || this.currentAnimateGradient != z3)) {
                        if (i8 == 0 || !z3) {
                            linearGradient = i8 != 0 ? i9 != 0 ? new LinearGradient(0.0f, i5, 0.0f, i3, new int[]{i9, i8, i7, color}, (float[]) null, Shader.TileMode.CLAMP) : new LinearGradient(0.0f, i5, 0.0f, i3, new int[]{i8, i7, color}, (float[]) null, Shader.TileMode.CLAMP) : new LinearGradient(0.0f, i5, 0.0f, i3, new int[]{i7, color}, (float[]) null, Shader.TileMode.CLAMP);
                        } else {
                            MotionBackgroundDrawable[] motionBackgroundDrawableArr2 = motionBackground;
                            if (motionBackgroundDrawableArr2[c] == null) {
                                motionBackgroundDrawableArr2[c] = new MotionBackgroundDrawable();
                                if (this.currentType != 2) {
                                    motionBackground[c].setPostInvalidateParent(true);
                                }
                                motionBackground[c].setRoundRadius(dp(1.0f));
                            }
                            motionBackground[c].setColors(color, i7, i8, i9);
                            linearGradient = motionBackground[c].getBitmapShader();
                        }
                        this.gradientShader = linearGradient;
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
                    } else {
                        i10 = 0;
                        motionBackground[c].setBounds(0, i5, i2, i3 - i4);
                    }
                    this.currentBackgroundHeight = i3;
                    if (this.gradientShader instanceof BitmapShader) {
                        i10 = i4;
                    }
                    this.topY = i - i10;
                    this.isTopNear = z;
                    this.isBottomNear = z2;
                }
            } else {
                color = getColor(this.isSelected ? Theme.key_chat_inBubbleSelected : Theme.key_chat_inBubble);
                i7 = 0;
                i8 = 0;
                i9 = 0;
            }
            z3 = false;
            if (i7 != 0) {
            }
            if (!this.themePreview) {
            }
            if (!this.isCrossfadeBackground) {
                int[] colors2 = motionBackgroundDrawable.getColors();
                this.currentColor = colors2[0];
                this.currentGradientColor1 = colors2[1];
                this.currentGradientColor2 = colors2[2];
                this.currentGradientColor3 = colors2[3];
            }
            if (!this.isCrossfadeBackground) {
            }
            if (i7 == 0) {
            }
            if (i7 == 0) {
            }
            if (this.gradientShader instanceof BitmapShader) {
            }
            this.currentBackgroundHeight = i3;
            if (this.gradientShader instanceof BitmapShader) {
            }
            this.topY = i - i10;
            this.isTopNear = z;
            this.isBottomNear = z2;
        }

        public void setTop(int i, int i2, int i3, boolean z, boolean z2) {
            setTop(i, i2, i3, i3, 0, 0, z, z2);
        }
    }

    /* loaded from: classes4.dex */
    public static class OverrideWallpaperInfo {
        public long accessHash;
        public int color;
        public long dialogId;
        public String fileName;
        public boolean forBoth;
        public int gradientColor1;
        public int gradientColor2;
        public int gradientColor3;
        public float intensity;
        public boolean isBlurred;
        public boolean isMotion;
        public String originalFileName;
        public ThemeAccent parentAccent;
        public ThemeInfo parentTheme;
        public TLRPC.WallPaper prevUserWallpaper;
        public ArrayList requestIds;
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
            if (TextUtils.isEmpty(overrideWallpaperInfo.fileName)) {
                this.fileName = "";
            } else {
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
            }
            if (TextUtils.isEmpty(overrideWallpaperInfo.originalFileName)) {
                this.originalFileName = "";
                return;
            }
            if (overrideWallpaperInfo.originalFileName.equals(overrideWallpaperInfo.fileName)) {
                this.originalFileName = this.fileName;
                return;
            }
            try {
                File file2 = new File(ApplicationLoader.getFilesDirFixed(), overrideWallpaperInfo.originalFileName);
                File filesDirFixed2 = ApplicationLoader.getFilesDirFixed();
                String generateWallpaperName2 = this.parentTheme.generateWallpaperName(this.parentAccent, true);
                this.originalFileName = generateWallpaperName2;
                AndroidUtilities.copyFile(file2, new File(filesDirFixed2, generateWallpaperName2));
            } catch (Exception e2) {
                this.originalFileName = "";
                FileLog.e(e2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void delete() {
            ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0).edit().remove(getKey()).commit();
            new File(ApplicationLoader.getFilesDirFixed(), this.fileName).delete();
            new File(ApplicationLoader.getFilesDirFixed(), this.originalFileName).delete();
        }

        private String getKey() {
            StringBuilder sb;
            if (this.parentAccent != null) {
                sb = new StringBuilder();
                sb.append(this.parentTheme.name);
                sb.append("_");
                sb.append(this.parentAccent.id);
            } else {
                sb = new StringBuilder();
                sb.append(this.parentTheme.name);
            }
            sb.append("_owp");
            return sb.toString();
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

        public boolean isColor() {
            return "c".equals(this.slug);
        }

        public boolean isDefault() {
            return "d".equals(this.slug);
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
    }

    /* loaded from: classes4.dex */
    public static class PatternsLoader implements NotificationCenter.NotificationCenterDelegate {
        private static PatternsLoader loader;
        private int account = UserConfig.selectedAccount;
        private HashMap watingForLoad;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class LoadingPattern {
            public ArrayList accents;
            public TLRPC.TL_wallPaper pattern;

            private LoadingPattern() {
                this.accents = new ArrayList();
            }
        }

        private PatternsLoader(final ArrayList arrayList) {
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

        private void checkCurrentWallpaper(final ArrayList arrayList, final boolean z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$PatternsLoader$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    Theme.PatternsLoader.this.lambda$checkCurrentWallpaper$2(arrayList, z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: checkCurrentWallpaperInternal, reason: merged with bridge method [inline-methods] */
        public void lambda$checkCurrentWallpaper$2(ArrayList arrayList, boolean z) {
            if (arrayList != null && Theme.currentTheme.themeAccents != null && !Theme.currentTheme.themeAccents.isEmpty() && arrayList.contains(Theme.currentTheme.getAccent(false))) {
                Theme.reloadWallpaper(true);
            }
            HashMap hashMap = this.watingForLoad;
            if (!z) {
                if (hashMap == null || hashMap.isEmpty()) {
                    NotificationCenter.getInstance(this.account).removeObserver(this, NotificationCenter.fileLoaded);
                    NotificationCenter.getInstance(this.account).removeObserver(this, NotificationCenter.fileLoadFailed);
                    return;
                }
                return;
            }
            if (hashMap != null) {
                NotificationCenter.getInstance(this.account).addObserver(this, NotificationCenter.fileLoaded);
                NotificationCenter.getInstance(this.account).addObserver(this, NotificationCenter.fileLoadFailed);
                Iterator it = this.watingForLoad.entrySet().iterator();
                while (it.hasNext()) {
                    FileLoader.getInstance(this.account).loadFile(ImageLocation.getForDocument(((LoadingPattern) ((Map.Entry) it.next()).getValue()).pattern.document), "wallpaper", null, 0, 1);
                }
            }
        }

        public static void createLoader(boolean z) {
            ArrayList arrayList;
            if (loader == null || z) {
                ArrayList arrayList2 = null;
                int i = 0;
                while (i < 5) {
                    ThemeInfo themeInfo = (ThemeInfo) Theme.themesDict.get(i != 0 ? i != 1 ? i != 2 ? i != 3 ? "Night" : "Day" : "Arctic Blue" : "Dark Blue" : "Blue");
                    if (themeInfo != null && (arrayList = themeInfo.themeAccents) != null && !arrayList.isEmpty()) {
                        int size = themeInfo.themeAccents.size();
                        for (int i2 = 0; i2 < size; i2++) {
                            ThemeAccent themeAccent = (ThemeAccent) themeInfo.themeAccents.get(i2);
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

        /* JADX WARN: Can't wrap try/catch for region: R(15:7|(1:58)(3:(1:12)|13|(1:15))|16|(1:22)|23|(1:29)|(1:33)|(1:35)(1:(7:56|(3:38|(1:40)(1:53)|41)(1:54)|42|43|(1:45)(1:49)|46|47)(1:57))|36|(0)(0)|42|43|(0)(0)|46|47) */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x0121, code lost:
        
            r0 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x0131, code lost:
        
            org.telegram.messenger.FileLog.e(r0);
         */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00a7 A[Catch: all -> 0x0038, TryCatch #1 {all -> 0x0038, blocks: (B:3:0x0004, B:7:0x000c, B:13:0x002b, B:15:0x0033, B:16:0x003e, B:20:0x0047, B:22:0x004f, B:23:0x0053, B:27:0x005c, B:29:0x0064, B:31:0x006a, B:33:0x0072, B:35:0x0078, B:38:0x00a7, B:40:0x00bd, B:53:0x00c2, B:56:0x007f, B:57:0x0097), top: B:2:0x0004 }] */
        /* JADX WARN: Removed duplicated region for block: B:45:0x00d3 A[Catch: all -> 0x0121, TRY_ENTER, TryCatch #0 {all -> 0x0121, blocks: (B:45:0x00d3, B:49:0x0123), top: B:43:0x00d1 }] */
        /* JADX WARN: Removed duplicated region for block: B:49:0x0123 A[Catch: all -> 0x0121, TRY_LEAVE, TryCatch #0 {all -> 0x0121, blocks: (B:45:0x00d3, B:49:0x0123), top: B:43:0x00d1 }] */
        /* JADX WARN: Removed duplicated region for block: B:54:0x00cd  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private Bitmap createWallpaperForAccent(Bitmap bitmap, boolean z, File file, ThemeAccent themeAccent) {
            Bitmap bitmap2;
            File pathToWallpaper;
            Drawable drawable;
            int patternColor;
            Drawable backgroundGradientDrawable;
            int patternColor2;
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
            } else {
                if (i6 != 0) {
                    backgroundGradientDrawable = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(themeAccent.backgroundRotation), new int[]{i5, i6});
                    patternColor2 = AndroidUtilities.getPatternColor(AndroidUtilities.getAverageColor(i5, i6));
                    if (bitmap != null) {
                        Point point = AndroidUtilities.displaySize;
                        int min = Math.min(point.x, point.y);
                        Point point2 = AndroidUtilities.displaySize;
                        bitmap2 = z ? SvgHelper.getBitmap(file, min, Math.max(point2.x, point2.y), false) : Theme.loadScreenSizedBitmap(new FileInputStream(file), 0);
                    } else {
                        bitmap2 = bitmap;
                    }
                    if (backgroundGradientDrawable == null) {
                        Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(createBitmap);
                        backgroundGradientDrawable.setBounds(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
                        backgroundGradientDrawable.draw(canvas);
                        Paint paint = new Paint(2);
                        paint.setColorFilter(new PorterDuffColorFilter(patternColor2, PorterDuff.Mode.SRC_IN));
                        paint.setAlpha((int) (Math.abs(themeAccent.patternIntensity) * 255.0f));
                        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
                        createBitmap.compress(Bitmap.CompressFormat.JPEG, 87, new FileOutputStream(pathToWallpaper));
                    } else {
                        FileOutputStream fileOutputStream = new FileOutputStream(pathToWallpaper);
                        bitmap2.compress(Bitmap.CompressFormat.PNG, 87, fileOutputStream);
                        fileOutputStream.close();
                    }
                    return bitmap2;
                }
                drawable = new ColorDrawable(i5);
                patternColor = AndroidUtilities.getPatternColor(i5);
            }
            int i10 = patternColor;
            backgroundGradientDrawable = drawable;
            patternColor2 = i10;
            if (bitmap != null) {
            }
            if (backgroundGradientDrawable == null) {
            }
            return bitmap2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didReceivedNotification$3(LoadingPattern loadingPattern) {
            TLRPC.TL_wallPaper tL_wallPaper = loadingPattern.pattern;
            File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tL_wallPaper.document, true);
            int size = loadingPattern.accents.size();
            Bitmap bitmap = null;
            ArrayList arrayList = null;
            for (int i = 0; i < size; i++) {
                ThemeAccent themeAccent = (ThemeAccent) loadingPattern.accents.get(i);
                if (themeAccent.patternSlug.equals(tL_wallPaper.slug)) {
                    bitmap = createWallpaperForAccent(bitmap, "application/x-tgwallpattern".equals(tL_wallPaper.document.mime_type), pathToAttach, themeAccent);
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                        arrayList.add(themeAccent);
                    }
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
            checkCurrentWallpaper(arrayList, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r12v0 */
        /* JADX WARN: Type inference failed for: r12v1 */
        /* JADX WARN: Type inference failed for: r12v2 */
        /* JADX WARN: Type inference failed for: r12v3, types: [java.lang.Boolean] */
        /* JADX WARN: Type inference failed for: r12v5 */
        /* JADX WARN: Type inference failed for: r12v6 */
        /* JADX WARN: Type inference failed for: r12v7 */
        /* JADX WARN: Type inference failed for: r4v2, types: [org.telegram.ui.ActionBar.Theme$1] */
        /* JADX WARN: Type inference failed for: r4v3 */
        /* JADX WARN: Type inference failed for: r4v7 */
        public /* synthetic */ void lambda$new$0(ArrayList arrayList, TLObject tLObject, TLRPC.TL_error tL_error) {
            if (tLObject instanceof TLRPC.Vector) {
                TLRPC.Vector vector = (TLRPC.Vector) tLObject;
                int size = vector.objects.size();
                Bitmap bitmap = null;
                ArrayList arrayList2 = null;
                int i = 0;
                while (i < size) {
                    TLRPC.WallPaper wallPaper = (TLRPC.WallPaper) vector.objects.get(i);
                    if (wallPaper instanceof TLRPC.TL_wallPaper) {
                        TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) wallPaper;
                        if (tL_wallPaper.pattern) {
                            File pathToAttach = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(tL_wallPaper.document, true);
                            int size2 = arrayList.size();
                            Bitmap bitmap2 = bitmap;
                            ?? r12 = bitmap2;
                            int i2 = 0;
                            ?? r4 = bitmap;
                            while (i2 < size2) {
                                ThemeAccent themeAccent = (ThemeAccent) arrayList.get(i2);
                                r12 = r12;
                                if (themeAccent.patternSlug.equals(tL_wallPaper.slug)) {
                                    if (r12 == 0) {
                                        r12 = Boolean.valueOf(pathToAttach.exists());
                                    }
                                    if (bitmap2 != null || r12.booleanValue()) {
                                        bitmap2 = createWallpaperForAccent(bitmap2, "application/x-tgwallpattern".equals(tL_wallPaper.document.mime_type), pathToAttach, themeAccent);
                                        if (arrayList2 == null) {
                                            arrayList2 = new ArrayList();
                                        }
                                        arrayList2.add(themeAccent);
                                    } else {
                                        String attachFileName = FileLoader.getAttachFileName(tL_wallPaper.document);
                                        if (this.watingForLoad == null) {
                                            this.watingForLoad = new HashMap();
                                        }
                                        LoadingPattern loadingPattern = (LoadingPattern) this.watingForLoad.get(attachFileName);
                                        if (loadingPattern == null) {
                                            loadingPattern = new LoadingPattern();
                                            loadingPattern.pattern = tL_wallPaper;
                                            this.watingForLoad.put(attachFileName, loadingPattern);
                                        }
                                        loadingPattern.accents.add(themeAccent);
                                    }
                                }
                                i2++;
                                r4 = 0;
                                r12 = r12;
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

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(final ArrayList arrayList) {
            int size = arrayList.size();
            ArrayList arrayList2 = null;
            int i = 0;
            while (i < size) {
                ThemeAccent themeAccent = (ThemeAccent) arrayList.get(i);
                File pathToWallpaper = themeAccent.getPathToWallpaper();
                if (pathToWallpaper == null || pathToWallpaper.length() <= 0) {
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                    }
                    if (!arrayList2.contains(themeAccent.patternSlug)) {
                        arrayList2.add(themeAccent.patternSlug);
                    }
                } else {
                    arrayList.remove(i);
                    i--;
                    size--;
                }
                i++;
            }
            if (arrayList2 == null) {
                return;
            }
            TLRPC.TL_account_getMultiWallPapers tL_account_getMultiWallPapers = new TLRPC.TL_account_getMultiWallPapers();
            int size2 = arrayList2.size();
            for (int i2 = 0; i2 < size2; i2++) {
                TLRPC.TL_inputWallPaperSlug tL_inputWallPaperSlug = new TLRPC.TL_inputWallPaperSlug();
                tL_inputWallPaperSlug.slug = (String) arrayList2.get(i2);
                tL_account_getMultiWallPapers.wallpapers.add(tL_inputWallPaperSlug);
            }
            ConnectionsManager.getInstance(this.account).sendRequest(tL_account_getMultiWallPapers, new RequestDelegate() { // from class: org.telegram.ui.ActionBar.Theme$PatternsLoader$$ExternalSyntheticLambda3
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    Theme.PatternsLoader.this.lambda$new$0(arrayList, tLObject, tL_error);
                }
            });
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            HashMap hashMap = this.watingForLoad;
            if (hashMap == null) {
                return;
            }
            if (i == NotificationCenter.fileLoaded) {
                final LoadingPattern loadingPattern = (LoadingPattern) hashMap.remove((String) objArr[0]);
                if (loadingPattern != null) {
                    Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$PatternsLoader$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            Theme.PatternsLoader.this.lambda$didReceivedNotification$3(loadingPattern);
                        }
                    });
                    return;
                }
                return;
            }
            if (i != NotificationCenter.fileLoadFailed || hashMap.remove((String) objArr[0]) == null) {
                return;
            }
            checkCurrentWallpaper(null, false);
        }
    }

    /* loaded from: classes.dex */
    public interface ResourcesProvider {

        /* loaded from: classes4.dex */
        public abstract /* synthetic */ class -CC {
            public static Drawable $default$getDrawable(ResourcesProvider resourcesProvider, String str) {
                return null;
            }

            public static boolean $default$hasGradientService(ResourcesProvider resourcesProvider) {
                return false;
            }

            public static void $default$setAnimatedColor(ResourcesProvider resourcesProvider, int i, int i2) {
            }
        }

        void applyServiceShaderMatrix(int i, int i2, float f, float f2);

        ColorFilter getAnimatedEmojiColorFilter();

        int getColor(int i);

        int getColorOrDefault(int i);

        int getCurrentColor(int i);

        Drawable getDrawable(String str);

        Paint getPaint(String str);

        boolean hasGradientService();

        boolean isDark();

        void setAnimatedColor(int i, int i2);
    }

    /* loaded from: classes4.dex */
    public static class RippleRadMaskDrawable extends Drawable {
        private float[] radii;
        private Path path = new Path();
        boolean invalidatePath = true;

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

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return 0;
        }

        @Override // android.graphics.drawable.Drawable
        protected void onBoundsChange(Rect rect) {
            this.invalidatePath = true;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
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
    }

    /* loaded from: classes4.dex */
    public static class ThemeAccent {
        public int accentColor;
        public int accentColor2;
        public int account;
        public long backgroundGradientOverrideColor1;
        public long backgroundGradientOverrideColor2;
        public long backgroundGradientOverrideColor3;
        public long backgroundOverrideColor;
        public int id;
        public TLRPC.TL_theme info;
        public boolean isDefault;
        public int myMessagesAccentColor;
        public boolean myMessagesAnimated;
        public int myMessagesGradientAccentColor1;
        public int myMessagesGradientAccentColor2;
        public int myMessagesGradientAccentColor3;
        public OverrideWallpaperInfo overrideWallpaper;
        public ThemeInfo parentTheme;
        public TLRPC.TL_wallPaper pattern;
        public float patternIntensity;
        public boolean patternMotion;
        public TLRPC.InputFile uploadedFile;
        public TLRPC.InputFile uploadedThumb;
        public String uploadingFile;
        public String uploadingThumb;
        public int backgroundRotation = 45;
        public String patternSlug = "";
        private float[] tempHSV = new float[3];

        ThemeAccent() {
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
                        i4 += Color.red(i6);
                        i3 += Color.green(i6);
                        i2 += Color.blue(i6);
                        i++;
                    } catch (Exception unused) {
                    }
                }
            }
            if (i == 0) {
                return 0;
            }
            return Color.argb(NotificationCenter.closeSearchByActiveAction, i4 / i, i3 / i, i2 / i);
        }

        private int bubbleSelectedOverlay(int i, int i2) {
            Color.colorToHSV(i2, this.tempHSV);
            float[] fArr = this.tempHSV;
            float f = fArr[0];
            Color.colorToHSV(i, fArr);
            float[] fArr2 = this.tempHSV;
            float f2 = fArr2[1];
            if (f2 <= 0.0f) {
                fArr2[0] = f;
            }
            fArr2[1] = Math.max(0.0f, Math.min(1.0f, f2 + 0.6f));
            float[] fArr3 = this.tempHSV;
            fArr3[2] = Math.max(0.0f, Math.min(1.0f, fArr3[2] - 0.05f));
            return Color.HSVToColor(30, this.tempHSV);
        }

        private int codeBackground(int i, boolean z) {
            int i2;
            Color.colorToHSV(i, this.tempHSV);
            if (z) {
                float[] fArr = this.tempHSV;
                fArr[1] = Utilities.clamp(fArr[1] - 0.08f, 1.0f, 0.0f);
                this.tempHSV[2] = 0.03f;
                i2 = 64;
            } else {
                float[] fArr2 = this.tempHSV;
                float f = fArr2[1];
                if (f > 0.0f) {
                    float f2 = fArr2[2];
                    if (f2 < 1.0f && f2 > 0.0f) {
                        fArr2[1] = Math.max(0.0f, Math.min(1.0f, f + 0.28f));
                        this.tempHSV[2] = Math.max(0.0f, Math.min(1.0f, r7[2] - 0.1f));
                        i2 = 32;
                    }
                }
                fArr2[2] = Math.max(0.0f, Math.min(1.0f, fArr2[2] - 0.2f));
                i2 = 32;
            }
            return Color.HSVToColor(i2, this.tempHSV);
        }

        private float getHue(int i) {
            Color.colorToHSV(i, this.tempHSV);
            return this.tempHSV[0];
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
            if (fArr[1] > 0.0f) {
                float f2 = fArr[2];
                if (f2 < 1.0f && f2 > 0.0f) {
                    fArr[0] = MathUtils.clamp(fArr[0] + 0.22f, 0.0f, 1.0f);
                    float[] fArr2 = this.tempHSV;
                    fArr2[1] = MathUtils.clamp(fArr2[1] - 0.35f, 0.0f, 1.0f);
                    float[] fArr3 = this.tempHSV;
                    fArr3[2] = MathUtils.clamp(fArr3[2] - 0.65f, 0.0f, 1.0f);
                    return Color.HSVToColor(90, this.tempHSV);
                }
            }
            fArr[0] = f;
            fArr[1] = 0.2f;
            float[] fArr32 = this.tempHSV;
            fArr32[2] = MathUtils.clamp(fArr32[2] - 0.65f, 0.0f, 1.0f);
            return Color.HSVToColor(90, this.tempHSV);
        }

        /* JADX WARN: Code restructure failed: missing block: B:6:0x0023, code lost:
        
            if (r3 < 85.0f) goto L8;
         */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0034  */
        /* JADX WARN: Removed duplicated region for block: B:9:0x0031  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private int textSelectionBackground(boolean z, int i, int i2) {
            Color.colorToHSV(i2, this.tempHSV);
            float[] fArr = this.tempHSV;
            float f = fArr[0];
            Color.colorToHSV(i, fArr);
            float[] fArr2 = this.tempHSV;
            float f2 = fArr2[1];
            if (f2 > 0.0f) {
                float f3 = fArr2[0];
                if (f3 > 45.0f) {
                }
                fArr2[1] = Math.max(0.0f, Math.min(1.0f, f2 + (fArr2[2] <= 0.85f ? 0.25f : 0.45f)));
                float[] fArr3 = this.tempHSV;
                fArr3[2] = Math.max(0.0f, Math.min(1.0f, fArr3[2] - 0.15f));
                return Color.HSVToColor(80, this.tempHSV);
            }
            fArr2[0] = f;
            fArr2[1] = Math.max(0.0f, Math.min(1.0f, f2 + (fArr2[2] <= 0.85f ? 0.25f : 0.45f)));
            float[] fArr32 = this.tempHSV;
            fArr32[2] = Math.max(0.0f, Math.min(1.0f, fArr32[2] - 0.15f));
            return Color.HSVToColor(80, this.tempHSV);
        }

        /* JADX WARN: Code restructure failed: missing block: B:6:0x0023, code lost:
        
            if (r5 < 85.0f) goto L8;
         */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0046  */
        /* JADX WARN: Removed duplicated region for block: B:9:0x0043  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private int textSelectionHandle(int i, int i2) {
            Color.colorToHSV(i2, this.tempHSV);
            float[] fArr = this.tempHSV;
            float f = fArr[0];
            Color.colorToHSV(i, fArr);
            float[] fArr2 = this.tempHSV;
            float f2 = fArr2[1];
            if (f2 > 0.0f) {
                float f3 = fArr2[0];
                if (f3 > 45.0f) {
                }
                fArr2[1] = Math.max(0.0f, Math.min(1.0f, f2 + 0.6f));
                float[] fArr3 = this.tempHSV;
                float f4 = fArr3[2];
                fArr3[2] = Math.max(0.0f, Math.min(1.0f, f4 - (f4 <= 0.7f ? 0.25f : 0.125f)));
                return Theme.blendOver(i, Color.HSVToColor(NotificationCenter.closeSearchByActiveAction, this.tempHSV));
            }
            fArr2[0] = f;
            fArr2[1] = Math.max(0.0f, Math.min(1.0f, f2 + 0.6f));
            float[] fArr32 = this.tempHSV;
            float f42 = fArr32[2];
            fArr32[2] = Math.max(0.0f, Math.min(1.0f, f42 - (f42 <= 0.7f ? 0.25f : 0.125f)));
            return Theme.blendOver(i, Color.HSVToColor(NotificationCenter.closeSearchByActiveAction, this.tempHSV));
        }

        public boolean fillAccentColors(SparseIntArray sparseIntArray, SparseIntArray sparseIntArray2) {
            int valueAt;
            boolean useBlackText;
            boolean z;
            int valueAt2;
            int valueAt3;
            int i;
            int codeBackground;
            int i2;
            boolean useBlackText2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            float[] tempHsv = Theme.getTempHsv(1);
            float[] tempHsv2 = Theme.getTempHsv(2);
            Color.colorToHSV(this.parentTheme.accentBaseColor, tempHsv);
            Color.colorToHSV(this.accentColor, tempHsv2);
            boolean isDark = this.parentTheme.isDark();
            if (this.accentColor != this.parentTheme.accentBaseColor || this.accentColor2 != 0) {
                for (int i8 = 0; i8 < Theme.defaultColors.length; i8++) {
                    if (!Theme.themeAccentExclusionKeys.contains(Integer.valueOf(i8))) {
                        int indexOfKey = sparseIntArray.indexOfKey(i8);
                        if (indexOfKey < 0) {
                            int i9 = Theme.fallbackKeys.get(i8, -1);
                            if (i9 < 0 || sparseIntArray.indexOfKey(i9) < 0) {
                                valueAt = Theme.defaultColors[i8];
                            }
                        } else {
                            valueAt = sparseIntArray.valueAt(indexOfKey);
                        }
                        int changeColorAccent = Theme.changeColorAccent(tempHsv, tempHsv2, valueAt, isDark, valueAt);
                        if (changeColorAccent != valueAt) {
                            sparseIntArray2.put(i8, changeColorAccent);
                        }
                    }
                }
            }
            int i10 = this.myMessagesAccentColor;
            if ((i10 == 0 && this.accentColor == 0) || this.myMessagesGradientAccentColor1 == 0) {
                z = false;
            } else {
                if (i10 == 0) {
                    i10 = this.accentColor;
                }
                int i11 = Theme.key_chat_outBubble;
                int i12 = sparseIntArray.get(i11);
                if (i12 == 0) {
                    i12 = Theme.defaultColors[i11];
                }
                int colorDistance = AndroidUtilities.getColorDistance(i10, Theme.changeColorAccent(tempHsv, tempHsv2, i12, isDark, i12));
                int colorDistance2 = AndroidUtilities.getColorDistance(i10, this.myMessagesGradientAccentColor1);
                if (this.myMessagesGradientAccentColor2 != 0) {
                    int averageColor = AndroidUtilities.getAverageColor(AndroidUtilities.getAverageColor(this.myMessagesAccentColor, this.myMessagesGradientAccentColor1), this.myMessagesGradientAccentColor2);
                    int i13 = this.myMessagesGradientAccentColor3;
                    if (i13 != 0) {
                        averageColor = AndroidUtilities.getAverageColor(averageColor, i13);
                    }
                    useBlackText = AndroidUtilities.computePerceivedBrightness(averageColor) > 0.705f;
                } else {
                    useBlackText = Theme.useBlackText(this.myMessagesAccentColor, this.myMessagesGradientAccentColor1);
                }
                z = useBlackText && colorDistance <= 35000 && colorDistance2 <= 35000;
                i10 = Theme.getAccentColor(tempHsv, i12, i10);
            }
            boolean z2 = (i10 == 0 || (((i6 = this.parentTheme.accentBaseColor) == 0 || i10 == i6) && ((i7 = this.accentColor) == 0 || i7 == i10))) ? false : true;
            if (z2 || this.accentColor2 != 0) {
                int i14 = this.accentColor2;
                if (i14 != 0) {
                    Color.colorToHSV(i14, tempHsv2);
                } else {
                    Color.colorToHSV(i10, tempHsv2);
                }
                for (int i15 = Theme.myMessagesStartIndex; i15 < Theme.myMessagesEndIndex; i15++) {
                    int indexOfKey2 = sparseIntArray.indexOfKey(i15);
                    if (indexOfKey2 < 0) {
                        int i16 = Theme.fallbackKeys.get(i15, -1);
                        if (i16 < 0 || sparseIntArray.get(i16, -1) < 0) {
                            valueAt3 = Theme.defaultColors[i15];
                        }
                    } else {
                        valueAt3 = sparseIntArray.valueAt(indexOfKey2);
                    }
                    int changeColorAccent2 = Theme.changeColorAccent(tempHsv, tempHsv2, valueAt3, isDark, valueAt3);
                    if (changeColorAccent2 != valueAt3) {
                        sparseIntArray2.put(i15, changeColorAccent2);
                    }
                }
                if (z2) {
                    Color.colorToHSV(i10, tempHsv2);
                    for (int i17 = Theme.myMessagesBubblesStartIndex; i17 < Theme.myMessagesBubblesEndIndex; i17++) {
                        int indexOfKey3 = sparseIntArray.indexOfKey(i17);
                        if (indexOfKey3 < 0) {
                            int i18 = Theme.fallbackKeys.get(i17, -1);
                            if (i18 < 0 || sparseIntArray.get(i18, -1) < 0) {
                                valueAt2 = Theme.defaultColors[i17];
                            }
                        } else {
                            valueAt2 = sparseIntArray.valueAt(indexOfKey3);
                        }
                        int changeColorAccent3 = Theme.changeColorAccent(tempHsv, tempHsv2, valueAt2, isDark, valueAt2);
                        if (changeColorAccent3 != valueAt2) {
                            sparseIntArray2.put(i17, changeColorAccent3);
                        }
                    }
                }
            }
            if (!z && (i2 = this.myMessagesGradientAccentColor1) != 0) {
                if (this.myMessagesGradientAccentColor2 != 0) {
                    int averageColor2 = AndroidUtilities.getAverageColor(AndroidUtilities.getAverageColor(this.myMessagesAccentColor, i2), this.myMessagesGradientAccentColor2);
                    int i19 = this.myMessagesGradientAccentColor3;
                    if (i19 != 0) {
                        averageColor2 = AndroidUtilities.getAverageColor(averageColor2, i19);
                    }
                    useBlackText2 = AndroidUtilities.computePerceivedBrightness(averageColor2) > 0.705f;
                } else {
                    useBlackText2 = Theme.useBlackText(this.myMessagesAccentColor, i2);
                }
                if (useBlackText2) {
                    i5 = -14606047;
                    i3 = -11184811;
                    i4 = 1291845632;
                } else {
                    i3 = -1118482;
                    i4 = 1308622847;
                    i5 = -1;
                }
                if (this.accentColor2 == 0) {
                    sparseIntArray2.put(Theme.key_chat_outAudioProgress, i4);
                    sparseIntArray2.put(Theme.key_chat_outAudioSelectedProgress, i4);
                    sparseIntArray2.put(Theme.key_chat_outAudioSeekbar, i4);
                    sparseIntArray2.put(Theme.key_chat_outAudioCacheSeekbar, i4);
                    sparseIntArray2.put(Theme.key_chat_outAudioSeekbarSelected, i4);
                    sparseIntArray2.put(Theme.key_chat_outAudioSeekbarFill, i5);
                    sparseIntArray2.put(Theme.key_chat_outVoiceSeekbar, i4);
                    sparseIntArray2.put(Theme.key_chat_outVoiceSeekbarSelected, i4);
                    sparseIntArray2.put(Theme.key_chat_outVoiceSeekbarFill, i5);
                    sparseIntArray2.put(Theme.key_chat_messageLinkOut, i5);
                    sparseIntArray2.put(Theme.key_chat_outForwardedNameText, i5);
                    sparseIntArray2.put(Theme.key_chat_outViaBotNameText, i5);
                    sparseIntArray2.put(Theme.key_chat_outReplyLine, i5);
                    sparseIntArray2.put(Theme.key_chat_outReplyLine2, i5);
                    sparseIntArray2.put(Theme.key_chat_outReplyNameText, i5);
                    sparseIntArray2.put(Theme.key_chat_outPreviewLine, i5);
                    sparseIntArray2.put(Theme.key_chat_outSiteNameText, i5);
                    sparseIntArray2.put(Theme.key_chat_outInstant, i5);
                    sparseIntArray2.put(Theme.key_chat_outInstantSelected, i5);
                    sparseIntArray2.put(Theme.key_chat_outPreviewInstantText, i5);
                    sparseIntArray2.put(Theme.key_chat_outViews, i5);
                    sparseIntArray2.put(Theme.key_chat_outViewsSelected, i5);
                    sparseIntArray2.put(Theme.key_chat_outAudioTitleText, i5);
                    sparseIntArray2.put(Theme.key_chat_outFileNameText, i5);
                    sparseIntArray2.put(Theme.key_chat_outContactNameText, i5);
                    sparseIntArray2.put(Theme.key_chat_outAudioPerformerText, i5);
                    sparseIntArray2.put(Theme.key_chat_outAudioPerformerSelectedText, i5);
                    sparseIntArray2.put(Theme.key_chat_outSentCheck, i5);
                    sparseIntArray2.put(Theme.key_chat_outSentCheckSelected, i5);
                    sparseIntArray2.put(Theme.key_chat_outSentCheckRead, i5);
                    sparseIntArray2.put(Theme.key_chat_outSentCheckReadSelected, i5);
                    sparseIntArray2.put(Theme.key_chat_outSentClock, i5);
                    sparseIntArray2.put(Theme.key_chat_outSentClockSelected, i5);
                    sparseIntArray2.put(Theme.key_chat_outMenu, i5);
                    sparseIntArray2.put(Theme.key_chat_outMenuSelected, i5);
                    sparseIntArray2.put(Theme.key_chat_outTimeText, i5);
                    sparseIntArray2.put(Theme.key_chat_outTimeSelectedText, i5);
                    sparseIntArray2.put(Theme.key_chat_outAudioDurationText, i3);
                    sparseIntArray2.put(Theme.key_chat_outAudioDurationSelectedText, i3);
                    sparseIntArray2.put(Theme.key_chat_outContactPhoneText, i3);
                    sparseIntArray2.put(Theme.key_chat_outContactPhoneSelectedText, i3);
                    sparseIntArray2.put(Theme.key_chat_outFileInfoText, i3);
                    sparseIntArray2.put(Theme.key_chat_outFileInfoSelectedText, i3);
                    sparseIntArray2.put(Theme.key_chat_outVenueInfoText, i3);
                    sparseIntArray2.put(Theme.key_chat_outVenueInfoSelectedText, i3);
                    sparseIntArray2.put(Theme.key_chat_outLoader, i5);
                    sparseIntArray2.put(Theme.key_chat_outLoaderSelected, i5);
                    sparseIntArray2.put(Theme.key_chat_outFileProgress, this.myMessagesAccentColor);
                    sparseIntArray2.put(Theme.key_chat_outFileProgressSelected, this.myMessagesAccentColor);
                    sparseIntArray2.put(Theme.key_chat_outMediaIcon, this.myMessagesAccentColor);
                    sparseIntArray2.put(Theme.key_chat_outMediaIconSelected, this.myMessagesAccentColor);
                }
                sparseIntArray2.put(Theme.key_chat_outReplyMessageText, i5);
                sparseIntArray2.put(Theme.key_chat_outReplyMediaMessageText, i5);
                sparseIntArray2.put(Theme.key_chat_outReplyMediaMessageSelectedText, i5);
                sparseIntArray2.put(Theme.key_chat_messageTextOut, i5);
            }
            if (z) {
                int i20 = Theme.key_chat_outLoader;
                if (AndroidUtilities.getColorDistance(-1, sparseIntArray2.indexOfKey(i20) >= 0 ? sparseIntArray2.get(i20) : 0) < 5000) {
                    z = false;
                }
            }
            int i21 = this.myMessagesAccentColor;
            if (i21 != 0 && this.myMessagesGradientAccentColor1 != 0) {
                sparseIntArray2.put(Theme.key_chat_outBubble, i21);
                sparseIntArray2.put(Theme.key_chat_outBubbleGradient1, this.myMessagesGradientAccentColor1);
                int i22 = this.myMessagesGradientAccentColor2;
                if (i22 != 0) {
                    sparseIntArray2.put(Theme.key_chat_outBubbleGradient2, i22);
                    int i23 = this.myMessagesGradientAccentColor3;
                    if (i23 != 0) {
                        sparseIntArray2.put(Theme.key_chat_outBubbleGradient3, i23);
                    }
                }
                sparseIntArray2.put(Theme.key_chat_outBubbleGradientAnimated, this.myMessagesAnimated ? 1 : 0);
            }
            long j = this.backgroundOverrideColor;
            int i24 = (int) j;
            if (i24 != 0) {
                sparseIntArray2.put(Theme.key_chat_wallpaper, i24);
            } else if (j != 0) {
                sparseIntArray2.delete(Theme.key_chat_wallpaper);
            }
            long j2 = this.backgroundGradientOverrideColor1;
            int i25 = (int) j2;
            if (i25 != 0) {
                sparseIntArray2.put(Theme.key_chat_wallpaper_gradient_to1, i25);
            } else if (j2 != 0) {
                sparseIntArray2.delete(Theme.key_chat_wallpaper_gradient_to1);
            }
            long j3 = this.backgroundGradientOverrideColor2;
            int i26 = (int) j3;
            if (i26 != 0) {
                sparseIntArray2.put(Theme.key_chat_wallpaper_gradient_to2, i26);
            } else if (j3 != 0) {
                sparseIntArray2.delete(Theme.key_chat_wallpaper_gradient_to2);
            }
            long j4 = this.backgroundGradientOverrideColor3;
            int i27 = (int) j4;
            if (i27 != 0) {
                sparseIntArray2.put(Theme.key_chat_wallpaper_gradient_to3, i27);
            } else if (j4 != 0) {
                sparseIntArray2.delete(Theme.key_chat_wallpaper_gradient_to3);
            }
            int i28 = this.backgroundRotation;
            if (i28 != 45) {
                sparseIntArray2.put(Theme.key_chat_wallpaper_gradient_rotation, i28);
            }
            int i29 = Theme.key_chat_outBubble;
            int i30 = sparseIntArray2.get(i29);
            if (i30 == 0) {
                i30 = Theme.getColor(i29);
            }
            int i31 = Theme.key_chat_inBubble;
            int i32 = sparseIntArray2.get(i31);
            if (i32 == 0) {
                i32 = Theme.getColor(i31);
            }
            TLRPC.TL_theme tL_theme = this.info;
            if (tL_theme != null && tL_theme.emoticon != null && !isDark) {
                sparseIntArray2.delete(Theme.key_chat_selectedBackground);
                int averageColor3 = averageColor(sparseIntArray2, Theme.key_chat_wallpaper_gradient_to1, Theme.key_chat_wallpaper_gradient_to2, Theme.key_chat_wallpaper_gradient_to3);
                if (averageColor3 == 0) {
                    averageColor3 = averageColor(sparseIntArray2, Theme.key_chat_wallpaper);
                }
                if (averageColor3 == 0) {
                    averageColor3 = this.accentColor;
                }
                int bubbleSelectedOverlay = bubbleSelectedOverlay(i30, averageColor3);
                sparseIntArray2.put(Theme.key_chat_outBubbleSelectedOverlay, bubbleSelectedOverlay);
                sparseIntArray2.put(Theme.key_chat_outBubbleGradientSelectedOverlay, bubbleSelectedOverlay);
                sparseIntArray2.put(Theme.key_chat_outBubbleSelected, Theme.blendOver(i30, bubbleSelectedOverlay));
                int bubbleSelectedOverlay2 = bubbleSelectedOverlay(i32, this.accentColor);
                sparseIntArray2.put(Theme.key_chat_inBubbleSelectedOverlay, bubbleSelectedOverlay2);
                sparseIntArray2.put(Theme.key_chat_inBubbleSelected, Theme.blendOver(i32, bubbleSelectedOverlay2));
            }
            if (!isDark) {
                sparseIntArray2.put(Theme.key_chat_inTextSelectionHighlight, textSelectionBackground(false, i32, this.accentColor));
                sparseIntArray2.put(Theme.key_chat_outTextSelectionHighlight, textSelectionBackground(true, i30, this.accentColor));
                sparseIntArray2.put(Theme.key_chat_outTextSelectionCursor, textSelectionHandle(i30, this.accentColor));
            }
            float hue = getHue(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText));
            sparseIntArray2.put(Theme.key_chat_outBubbleLocationPlaceholder, locationPlaceholderColor(hue, i30, isDark));
            sparseIntArray2.put(Theme.key_chat_inBubbleLocationPlaceholder, locationPlaceholderColor(hue, i32, isDark));
            int i33 = Theme.key_chat_messageLinkIn;
            int i34 = sparseIntArray2.get(i33);
            if (i34 == 0) {
                i34 = Theme.getColor(i33);
            }
            int i35 = Theme.key_chat_messageLinkOut;
            int i36 = sparseIntArray2.get(i35);
            if (i36 == 0) {
                i36 = Theme.getColor(i35);
            }
            sparseIntArray2.put(Theme.key_chat_linkSelectBackground, linkSelectionBackground(i34, i32, isDark));
            sparseIntArray2.put(Theme.key_chat_outLinkSelectBackground, linkSelectionBackground(i36, i30, isDark));
            int i37 = Theme.key_actionBarDefaultSubmenuBackground;
            int i38 = sparseIntArray2.get(i37);
            if (i38 == 0) {
                i38 = Theme.getColor(i37);
            }
            sparseIntArray2.put(Theme.key_actionBarDefaultSubmenuSeparator, Color.argb(Color.alpha(i38), Math.max(0, Color.red(i38) - 10), Math.max(0, Color.green(i38) - 10), Math.max(0, Color.blue(i38) - 10)));
            if (isDark) {
                int i39 = Theme.key_chat_outBubbleGradient1;
                if (sparseIntArray2.get(i39) != 0) {
                    Color.colorToHSV(averageColor(sparseIntArray2, i39, Theme.key_chat_outBubbleGradient2, Theme.key_chat_outBubbleGradient3), this.tempHSV);
                    float[] fArr = this.tempHSV;
                    fArr[1] = Utilities.clamp(fArr[1] + 0.1f, 1.0f, 0.0f);
                    float[] fArr2 = this.tempHSV;
                    fArr2[2] = Utilities.clamp(fArr2[2] - 0.8f, 1.0f, 0.0f);
                    i = Theme.key_chat_outCodeBackground;
                    codeBackground = Color.HSVToColor(64, this.tempHSV);
                    sparseIntArray2.put(i, codeBackground);
                    return !z;
                }
            }
            i = Theme.key_chat_outCodeBackground;
            codeBackground = codeBackground(i30, isDark);
            sparseIntArray2.put(i, codeBackground);
            return !z;
        }

        public File getPathToWallpaper() {
            if (this.id < 100) {
                if (TextUtils.isEmpty(this.patternSlug)) {
                    return null;
                }
                return new File(ApplicationLoader.getFilesDirFixed(), String.format(Locale.US, "%s_%d_%s_v5.jpg", this.parentTheme.getKey(), Integer.valueOf(this.id), this.patternSlug));
            }
            if (TextUtils.isEmpty(this.patternSlug)) {
                return null;
            }
            return new File(ApplicationLoader.getFilesDirFixed(), String.format(Locale.US, "%s_%d_%s_v8_debug.jpg", this.parentTheme.getKey(), Integer.valueOf(this.id), this.patternSlug));
        }

        /* JADX WARN: Code restructure failed: missing block: B:73:0x0267, code lost:
        
            if (r4 == null) goto L82;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r4v24 */
        /* JADX WARN: Type inference failed for: r4v25 */
        /* JADX WARN: Type inference failed for: r4v26 */
        /* JADX WARN: Type inference failed for: r4v3, types: [int] */
        /* JADX WARN: Type inference failed for: r4v4 */
        /* JADX WARN: Type inference failed for: r4v5, types: [java.io.FileOutputStream] */
        /* JADX WARN: Type inference failed for: r4v6 */
        /* JADX WARN: Type inference failed for: r4v9, types: [java.io.FileOutputStream] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public File saveToFile() {
            String str;
            ?? size;
            FileOutputStream fileOutputStream;
            File sharingDirectory = AndroidUtilities.getSharingDirectory();
            sharingDirectory.mkdirs();
            int i = 0;
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
                int i2 = clone.get(Theme.key_chat_wallpaper);
                if (i2 == 0) {
                    i2 = -1;
                }
                int i3 = clone.get(Theme.key_chat_wallpaper_gradient_to1);
                if (i3 == 0) {
                    i3 = 0;
                }
                int i4 = clone.get(Theme.key_chat_wallpaper_gradient_to2);
                if (i4 == 0) {
                    i4 = 0;
                }
                int i5 = clone.get(Theme.key_chat_wallpaper_gradient_to3);
                if (i5 == 0) {
                    i5 = 0;
                }
                int i6 = clone.get(Theme.key_chat_wallpaper_gradient_rotation);
                if (i6 == 0) {
                    i6 = 45;
                }
                String lowerCase = String.format("%02x%02x%02x", Integer.valueOf(((byte) (i2 >> 16)) & 255), Integer.valueOf(((byte) (i2 >> 8)) & 255), Byte.valueOf((byte) (i2 & NotificationCenter.closeSearchByActiveAction))).toLowerCase();
                String lowerCase2 = i3 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i3 >> 16)) & 255), Integer.valueOf(((byte) (i3 >> 8)) & 255), Byte.valueOf((byte) (i3 & NotificationCenter.closeSearchByActiveAction))).toLowerCase() : null;
                String lowerCase3 = i4 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i4 >> 16)) & 255), Integer.valueOf(((byte) (i4 >> 8)) & 255), Byte.valueOf((byte) (i4 & NotificationCenter.closeSearchByActiveAction))).toLowerCase() : null;
                String lowerCase4 = i5 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i5 >> 16)) & 255), Integer.valueOf(((byte) (i5 >> 8)) & 255), Byte.valueOf((byte) (i5 & NotificationCenter.closeSearchByActiveAction))).toLowerCase() : null;
                if (lowerCase2 == null || lowerCase3 == null) {
                    if (lowerCase2 != null) {
                        lowerCase = (lowerCase + "-" + lowerCase2) + "&rotation=" + i6;
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
            while (true) {
                size = clone.size();
                try {
                    if (i < size) {
                        int keyAt = clone.keyAt(i);
                        int valueAt = clone.valueAt(i);
                        if (str == null || (Theme.key_chat_wallpaper != keyAt && Theme.key_chat_wallpaper_gradient_to1 != keyAt && Theme.key_chat_wallpaper_gradient_to2 != keyAt && Theme.key_chat_wallpaper_gradient_to3 != keyAt)) {
                            sb2.append(keyAt);
                            sb2.append("=");
                            sb2.append(valueAt);
                            sb2.append("\n");
                        }
                        i++;
                    } else {
                        try {
                            break;
                        } catch (Exception e) {
                            e = e;
                            fileOutputStream = null;
                        } catch (Throwable th) {
                            th = th;
                            size = 0;
                            Throwable th2 = th;
                            if (size == 0) {
                                throw th2;
                            }
                            try {
                                size.close();
                                throw th2;
                            } catch (Exception e2) {
                                FileLog.e(e2);
                                throw th2;
                            }
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }
            fileOutputStream = new FileOutputStream(file);
            try {
                fileOutputStream.write(AndroidUtilities.getStringBytes(sb2.toString()));
                size = fileOutputStream;
                if (!TextUtils.isEmpty(str)) {
                    fileOutputStream.write(AndroidUtilities.getStringBytes("WLS=" + str + "\n"));
                    size = fileOutputStream;
                }
            } catch (Exception e3) {
                e = e3;
                FileLog.e(e);
                size = fileOutputStream;
            }
            try {
                size.close();
            } catch (Exception e4) {
                FileLog.e(e4);
            }
            return file;
        }
    }

    /* loaded from: classes4.dex */
    public static class ThemeInfo implements NotificationCenter.NotificationCenterDelegate {
        public int accentBaseColor;
        public LongSparseArray accentsByThemeId;
        public int account;
        public String assetName;
        public boolean badWallpaper;
        public LongSparseArray chatAccentsByThemeId;
        public int currentAccentId;
        public int defaultAccentCount;
        public boolean firstAccentIsDefault;
        public TLRPC.TL_theme info;
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
        public ArrayList themeAccents;
        public SparseArray themeAccentsMap;
        public boolean themeLoaded;
        public TLRPC.InputFile uploadedFile;
        public TLRPC.InputFile uploadedThumb;
        public String uploadingFile;
        public String uploadingThumb;

        ThemeInfo() {
            this.patternBgGradientRotation = 45;
            this.loaded = true;
            this.themeLoaded = true;
            this.prevAccentId = -1;
            this.chatAccentsByThemeId = new LongSparseArray();
            this.lastChatThemeId = 0;
            this.lastAccentId = 100;
            this.isDark = -1;
        }

        public ThemeInfo(ThemeInfo themeInfo) {
            this.patternBgGradientRotation = 45;
            this.loaded = true;
            this.themeLoaded = true;
            this.prevAccentId = -1;
            this.chatAccentsByThemeId = new LongSparseArray();
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

        public static boolean accentEquals(ThemeAccent themeAccent, TLRPC.ThemeSettings themeSettings) {
            long j;
            long j2;
            long j3;
            String str;
            int i;
            int i2;
            float f;
            TLRPC.WallPaperSettings wallPaperSettings;
            int intValue = themeSettings.message_colors.size() > 0 ? themeSettings.message_colors.get(0).intValue() | (-16777216) : 0;
            int intValue2 = themeSettings.message_colors.size() > 1 ? themeSettings.message_colors.get(1).intValue() | (-16777216) : 0;
            if (intValue == intValue2) {
                intValue2 = 0;
            }
            int intValue3 = themeSettings.message_colors.size() > 2 ? themeSettings.message_colors.get(2).intValue() | (-16777216) : 0;
            int intValue4 = themeSettings.message_colors.size() > 3 ? (-16777216) | themeSettings.message_colors.get(3).intValue() : 0;
            TLRPC.WallPaper wallPaper = themeSettings.wallpaper;
            if (wallPaper == null || (wallPaperSettings = wallPaper.settings) == null) {
                j = 0;
                j2 = 0;
                j3 = 0;
                str = null;
                i = 0;
                i2 = 0;
            } else {
                i = Theme.getWallpaperColor(wallPaperSettings.background_color);
                j = themeSettings.wallpaper.settings.second_background_color == 0 ? 4294967296L : Theme.getWallpaperColor(r11);
                j2 = themeSettings.wallpaper.settings.third_background_color == 0 ? 4294967296L : Theme.getWallpaperColor(r11);
                j3 = themeSettings.wallpaper.settings.fourth_background_color != 0 ? Theme.getWallpaperColor(r11) : 4294967296L;
                i2 = AndroidUtilities.getWallpaperRotation(themeSettings.wallpaper.settings.rotation, false);
                TLRPC.WallPaper wallPaper2 = themeSettings.wallpaper;
                if (!(wallPaper2 instanceof TLRPC.TL_wallPaperNoFile) && wallPaper2.pattern) {
                    str = wallPaper2.slug;
                    f = wallPaper2.settings.intensity / 100.0f;
                    return themeSettings.accent_color != themeAccent.accentColor && themeSettings.outbox_accent_color == themeAccent.accentColor2 && intValue == themeAccent.myMessagesAccentColor && intValue2 == themeAccent.myMessagesGradientAccentColor1 && intValue3 == themeAccent.myMessagesGradientAccentColor2 && intValue4 == themeAccent.myMessagesGradientAccentColor3 && themeSettings.message_colors_animated == themeAccent.myMessagesAnimated && ((long) i) == themeAccent.backgroundOverrideColor && j == themeAccent.backgroundGradientOverrideColor1 && j2 == themeAccent.backgroundGradientOverrideColor2 && j3 == themeAccent.backgroundGradientOverrideColor3 && i2 == themeAccent.backgroundRotation && TextUtils.equals(str, themeAccent.patternSlug) && ((double) Math.abs(f - themeAccent.patternIntensity)) < 0.001d;
                }
                str = null;
            }
            f = 0.0f;
            if (themeSettings.accent_color != themeAccent.accentColor) {
            }
        }

        private void addObservers() {
            NotificationCenter.getInstance(this.account).addObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.account).addObserver(this, NotificationCenter.fileLoadFailed);
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
                    try {
                        SerializedData serializedData = new SerializedData(Utilities.hexToBytes(jSONObject.getString("info")));
                        themeInfo.info = TLRPC.Theme.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                    } catch (Throwable th) {
                        FileLog.e(th);
                    }
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

        public static void fillAccentValues(ThemeAccent themeAccent, TLRPC.ThemeSettings themeSettings) {
            TLRPC.WallPaperSettings wallPaperSettings;
            themeAccent.accentColor = themeSettings.accent_color;
            themeAccent.accentColor2 = themeSettings.outbox_accent_color;
            themeAccent.myMessagesAccentColor = themeSettings.message_colors.size() > 0 ? themeSettings.message_colors.get(0).intValue() | (-16777216) : 0;
            int intValue = themeSettings.message_colors.size() > 1 ? themeSettings.message_colors.get(1).intValue() | (-16777216) : 0;
            themeAccent.myMessagesGradientAccentColor1 = intValue;
            if (themeAccent.myMessagesAccentColor == intValue) {
                themeAccent.myMessagesGradientAccentColor1 = 0;
            }
            themeAccent.myMessagesGradientAccentColor2 = themeSettings.message_colors.size() > 2 ? themeSettings.message_colors.get(2).intValue() | (-16777216) : 0;
            themeAccent.myMessagesGradientAccentColor3 = themeSettings.message_colors.size() > 3 ? themeSettings.message_colors.get(3).intValue() | (-16777216) : 0;
            themeAccent.myMessagesAnimated = themeSettings.message_colors_animated;
            TLRPC.WallPaper wallPaper = themeSettings.wallpaper;
            if (wallPaper == null || (wallPaperSettings = wallPaper.settings) == null) {
                return;
            }
            if (wallPaperSettings.background_color == 0) {
                themeAccent.backgroundOverrideColor = 4294967296L;
            } else {
                themeAccent.backgroundOverrideColor = Theme.getWallpaperColor(r0);
            }
            TLRPC.WallPaperSettings wallPaperSettings2 = themeSettings.wallpaper.settings;
            if ((wallPaperSettings2.flags & 16) == 0 || wallPaperSettings2.second_background_color != 0) {
                themeAccent.backgroundGradientOverrideColor1 = Theme.getWallpaperColor(wallPaperSettings2.second_background_color);
            } else {
                themeAccent.backgroundGradientOverrideColor1 = 4294967296L;
            }
            TLRPC.WallPaperSettings wallPaperSettings3 = themeSettings.wallpaper.settings;
            if ((wallPaperSettings3.flags & 32) == 0 || wallPaperSettings3.third_background_color != 0) {
                themeAccent.backgroundGradientOverrideColor2 = Theme.getWallpaperColor(wallPaperSettings3.third_background_color);
            } else {
                themeAccent.backgroundGradientOverrideColor2 = 4294967296L;
            }
            TLRPC.WallPaperSettings wallPaperSettings4 = themeSettings.wallpaper.settings;
            if ((wallPaperSettings4.flags & 64) == 0 || wallPaperSettings4.fourth_background_color != 0) {
                themeAccent.backgroundGradientOverrideColor3 = Theme.getWallpaperColor(wallPaperSettings4.fourth_background_color);
            } else {
                themeAccent.backgroundGradientOverrideColor3 = 4294967296L;
            }
            themeAccent.backgroundRotation = AndroidUtilities.getWallpaperRotation(themeSettings.wallpaper.settings.rotation, false);
            TLRPC.WallPaper wallPaper2 = themeSettings.wallpaper;
            if ((wallPaper2 instanceof TLRPC.TL_wallPaperNoFile) || !wallPaper2.pattern) {
                return;
            }
            themeAccent.patternSlug = wallPaper2.slug;
            TLRPC.WallPaperSettings wallPaperSettings5 = wallPaper2.settings;
            themeAccent.patternIntensity = wallPaperSettings5.intensity / 100.0f;
            themeAccent.patternMotion = wallPaperSettings5.motion;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isDefaultMainAccent() {
            if (!this.firstAccentIsDefault) {
                return false;
            }
            int i = this.currentAccentId;
            int i2 = Theme.DEFALT_THEME_ACCENT_ID;
            if (i == i2) {
                return true;
            }
            ThemeAccent themeAccent = (ThemeAccent) this.themeAccentsMap.get(i2);
            ThemeAccent themeAccent2 = (ThemeAccent) this.themeAccentsMap.get(this.currentAccentId);
            return (themeAccent2 == null || themeAccent == null || themeAccent.accentColor != themeAccent2.accentColor) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isDefaultMyMessages() {
            if (!this.firstAccentIsDefault) {
                return false;
            }
            int i = this.currentAccentId;
            int i2 = Theme.DEFALT_THEME_ACCENT_ID;
            if (i == i2) {
                return true;
            }
            ThemeAccent themeAccent = (ThemeAccent) this.themeAccentsMap.get(i2);
            ThemeAccent themeAccent2 = (ThemeAccent) this.themeAccentsMap.get(this.currentAccentId);
            return themeAccent != null && themeAccent2 != null && themeAccent.accentColor2 == themeAccent2.accentColor2 && themeAccent.myMessagesAccentColor == themeAccent2.myMessagesAccentColor && themeAccent.myMessagesGradientAccentColor1 == themeAccent2.myMessagesGradientAccentColor1 && themeAccent.myMessagesGradientAccentColor2 == themeAccent2.myMessagesGradientAccentColor2 && themeAccent.myMessagesGradientAccentColor3 == themeAccent2.myMessagesGradientAccentColor3 && themeAccent.myMessagesAnimated == themeAccent2.myMessagesAnimated;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isDefaultMyMessagesBubbles() {
            if (!this.firstAccentIsDefault) {
                return false;
            }
            int i = this.currentAccentId;
            int i2 = Theme.DEFALT_THEME_ACCENT_ID;
            if (i == i2) {
                return true;
            }
            ThemeAccent themeAccent = (ThemeAccent) this.themeAccentsMap.get(i2);
            ThemeAccent themeAccent2 = (ThemeAccent) this.themeAccentsMap.get(this.currentAccentId);
            return themeAccent != null && themeAccent2 != null && themeAccent.myMessagesAccentColor == themeAccent2.myMessagesAccentColor && themeAccent.myMessagesGradientAccentColor1 == themeAccent2.myMessagesGradientAccentColor1 && themeAccent.myMessagesGradientAccentColor2 == themeAccent2.myMessagesGradientAccentColor2 && themeAccent.myMessagesGradientAccentColor3 == themeAccent2.myMessagesGradientAccentColor3 && themeAccent.myMessagesAnimated == themeAccent2.myMessagesAnimated;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didReceivedNotification$0(File file) {
            createBackground(file, this.newPathToWallpaper);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$ThemeInfo$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    Theme.ThemeInfo.this.onFinishLoadingRemoteTheme();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didReceivedNotification$1(TLObject tLObject, ThemeInfo themeInfo) {
            if (!(tLObject instanceof TLRPC.TL_wallPaper)) {
                onFinishLoadingRemoteTheme();
                return;
            }
            TLRPC.TL_wallPaper tL_wallPaper = (TLRPC.TL_wallPaper) tLObject;
            this.loadingThemeWallpaperName = FileLoader.getAttachFileName(tL_wallPaper.document);
            addObservers();
            FileLoader.getInstance(themeInfo.account).loadFile(tL_wallPaper.document, tL_wallPaper, 1, 1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didReceivedNotification$2(final ThemeInfo themeInfo, final TLObject tLObject, TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$ThemeInfo$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    Theme.ThemeInfo.this.lambda$didReceivedNotification$1(tLObject, themeInfo);
                }
            });
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

        /* JADX INFO: Access modifiers changed from: private */
        public void loadThemeDocument() {
            this.loaded = false;
            this.loadingThemeWallpaperName = null;
            this.newPathToWallpaper = null;
            addObservers();
            FileLoader fileLoader = FileLoader.getInstance(this.account);
            TLRPC.TL_theme tL_theme = this.info;
            fileLoader.loadFile(tL_theme.document, tL_theme, 1, 1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void loadWallpapers(SharedPreferences sharedPreferences) {
            ArrayList arrayList = this.themeAccents;
            if (arrayList == null || arrayList.isEmpty()) {
                loadOverrideWallpaper(sharedPreferences, null, this.name + "_owp");
                return;
            }
            int size = this.themeAccents.size();
            for (int i = 0; i < size; i++) {
                ThemeAccent themeAccent = (ThemeAccent) this.themeAccents.get(i);
                loadOverrideWallpaper(sharedPreferences, themeAccent, this.name + "_" + themeAccent.id + "_owp");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onFinishLoadingRemoteTheme() {
            this.loaded = true;
            this.previewParsed = false;
            Theme.saveOtherThemes(true);
            if (this == Theme.currentTheme && Theme.previousTheme == null) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, this, Boolean.valueOf(this == Theme.currentNightTheme), null, -1, Theme.fallbackKeys);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeObservers() {
            NotificationCenter.getInstance(this.account).removeObserver(this, NotificationCenter.fileLoaded);
            NotificationCenter.getInstance(this.account).removeObserver(this, NotificationCenter.fileLoadFailed);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setAccentColorOptions(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5, int[] iArr6, int[] iArr7, int[] iArr8, String[] strArr, int[] iArr9, int[] iArr10) {
            this.defaultAccentCount = iArr.length;
            this.themeAccents = new ArrayList();
            this.themeAccentsMap = new SparseArray();
            this.accentsByThemeId = new LongSparseArray();
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
                    long j = iArr4[i];
                    themeAccent.backgroundOverrideColor = j;
                    if (this.firstAccentIsDefault && themeAccent.id == Theme.DEFALT_THEME_ACCENT_ID) {
                        themeAccent.backgroundOverrideColor = 4294967296L;
                    } else {
                        themeAccent.backgroundOverrideColor = j;
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
                    themeAccent.backgroundGradientOverrideColor3 = (this.firstAccentIsDefault && themeAccent.id == Theme.DEFALT_THEME_ACCENT_ID) ? 4294967296L : iArr7[i];
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
            this.accentBaseColor = ((ThemeAccent) this.themeAccentsMap.get(0)).accentColor;
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

        public ThemeAccent createNewAccent(TLRPC.TL_theme tL_theme, int i) {
            return createNewAccent(tL_theme, i, false, 0);
        }

        public ThemeAccent createNewAccent(TLRPC.TL_theme tL_theme, int i, boolean z, int i2) {
            if (tL_theme == null) {
                return null;
            }
            TLRPC.ThemeSettings themeSettings = i2 < tL_theme.settings.size() ? tL_theme.settings.get(i2) : null;
            if (z) {
                ThemeAccent themeAccent = (ThemeAccent) this.chatAccentsByThemeId.get(tL_theme.id);
                if (themeAccent != null) {
                    return themeAccent;
                }
                int i3 = this.lastChatThemeId + 1;
                this.lastChatThemeId = i3;
                ThemeAccent createNewAccent = createNewAccent(themeSettings);
                createNewAccent.id = i3;
                createNewAccent.info = tL_theme;
                createNewAccent.account = i;
                this.chatAccentsByThemeId.put(i3, createNewAccent);
                return createNewAccent;
            }
            ThemeAccent themeAccent2 = (ThemeAccent) this.accentsByThemeId.get(tL_theme.id);
            if (themeAccent2 != null) {
                return themeAccent2;
            }
            int i4 = this.lastAccentId + 1;
            this.lastAccentId = i4;
            ThemeAccent createNewAccent2 = createNewAccent(themeSettings);
            createNewAccent2.id = i4;
            createNewAccent2.info = tL_theme;
            createNewAccent2.account = i;
            this.themeAccentsMap.put(i4, createNewAccent2);
            this.themeAccents.add(0, createNewAccent2);
            Theme.sortAccents(this);
            this.accentsByThemeId.put(tL_theme.id, createNewAccent2);
            return createNewAccent2;
        }

        public ThemeAccent createNewAccent(TLRPC.ThemeSettings themeSettings) {
            ThemeAccent themeAccent = new ThemeAccent();
            fillAccentValues(themeAccent, themeSettings);
            themeAccent.parentTheme = this;
            return themeAccent;
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            int i3 = NotificationCenter.fileLoaded;
            if (i == i3 || i == NotificationCenter.fileLoadFailed) {
                String str = (String) objArr[0];
                TLRPC.TL_theme tL_theme = this.info;
                if (tL_theme == null || tL_theme.document == null) {
                    return;
                }
                if (str.equals(this.loadingThemeWallpaperName)) {
                    this.loadingThemeWallpaperName = null;
                    final File file = (File) objArr[1];
                    Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$ThemeInfo$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            Theme.ThemeInfo.this.lambda$didReceivedNotification$0(file);
                        }
                    });
                    return;
                }
                if (str.equals(FileLoader.getAttachFileName(this.info.document))) {
                    removeObservers();
                    if (i == i3) {
                        File file2 = new File(this.pathToFile);
                        TLRPC.TL_theme tL_theme2 = this.info;
                        final ThemeInfo fillThemeValues = Theme.fillThemeValues(file2, tL_theme2.title, tL_theme2);
                        if (fillThemeValues == null || fillThemeValues.pathToWallpaper == null || new File(fillThemeValues.pathToWallpaper).exists()) {
                            onFinishLoadingRemoteTheme();
                            return;
                        }
                        this.patternBgColor = fillThemeValues.patternBgColor;
                        this.patternBgGradientColor1 = fillThemeValues.patternBgGradientColor1;
                        this.patternBgGradientColor2 = fillThemeValues.patternBgGradientColor2;
                        this.patternBgGradientColor3 = fillThemeValues.patternBgGradientColor3;
                        this.patternBgGradientRotation = fillThemeValues.patternBgGradientRotation;
                        this.isBlured = fillThemeValues.isBlured;
                        this.patternIntensity = fillThemeValues.patternIntensity;
                        this.newPathToWallpaper = fillThemeValues.pathToWallpaper;
                        TLRPC.TL_account_getWallPaper tL_account_getWallPaper = new TLRPC.TL_account_getWallPaper();
                        TLRPC.TL_inputWallPaperSlug tL_inputWallPaperSlug = new TLRPC.TL_inputWallPaperSlug();
                        tL_inputWallPaperSlug.slug = fillThemeValues.slug;
                        tL_account_getWallPaper.wallpaper = tL_inputWallPaperSlug;
                        ConnectionsManager.getInstance(fillThemeValues.account).sendRequest(tL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.ActionBar.Theme$ThemeInfo$$ExternalSyntheticLambda1
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                Theme.ThemeInfo.this.lambda$didReceivedNotification$2(fillThemeValues, tLObject, tL_error);
                            }
                        });
                    }
                }
            }
        }

        public String generateWallpaperName(ThemeAccent themeAccent, boolean z) {
            StringBuilder sb;
            StringBuilder sb2;
            if (themeAccent == null) {
                themeAccent = getAccent(false);
            }
            if (themeAccent == null) {
                StringBuilder sb3 = new StringBuilder();
                if (z) {
                    sb = new StringBuilder();
                    sb.append(this.name);
                    sb.append("_wp_o");
                } else {
                    sb = new StringBuilder();
                    sb.append(this.name);
                    sb.append("_wp");
                }
                sb3.append(sb.toString());
                sb3.append(Utilities.random.nextInt());
                sb3.append(".jpg");
                return sb3.toString();
            }
            StringBuilder sb4 = new StringBuilder();
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
            sb4.append(sb2.toString());
            sb4.append(Utilities.random.nextInt());
            sb4.append(".jpg");
            return sb4.toString();
        }

        public ThemeAccent getAccent(boolean z) {
            ThemeAccent themeAccent;
            if (this.themeAccents == null || (themeAccent = (ThemeAccent) this.themeAccentsMap.get(this.currentAccentId)) == null) {
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
            ThemeAccent themeAccent = (ThemeAccent) this.themeAccentsMap.get(i);
            if (themeAccent != null) {
                return themeAccent.accentColor;
            }
            return 0;
        }

        public String getKey() {
            if (this.info == null) {
                return this.name;
            }
            return "remote" + this.info.id;
        }

        public String getName() {
            int i;
            if ("Blue".equals(this.name)) {
                i = org.telegram.messenger.R.string.ThemeClassic;
            } else if ("Dark Blue".equals(this.name)) {
                i = org.telegram.messenger.R.string.ThemeDark;
            } else if ("Arctic Blue".equals(this.name)) {
                i = org.telegram.messenger.R.string.ThemeArcticBlue;
            } else if ("Day".equals(this.name)) {
                i = org.telegram.messenger.R.string.ThemeDay;
            } else {
                if (!"Night".equals(this.name)) {
                    TLRPC.TL_theme tL_theme = this.info;
                    return tL_theme != null ? tL_theme.title : this.name;
                }
                i = org.telegram.messenger.R.string.ThemeNight;
            }
            return LocaleController.getString(i);
        }

        public int getPreviewBackgroundColor() {
            if (this.firstAccentIsDefault && this.currentAccentId == Theme.DEFALT_THEME_ACCENT_ID) {
                return -3155485;
            }
            return this.previewBackgroundColor;
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

        JSONObject getSaveJson() {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("name", this.name);
                jSONObject.put("path", this.pathToFile);
                jSONObject.put("account", this.account);
                TLRPC.TL_theme tL_theme = this.info;
                if (tL_theme != null) {
                    SerializedData serializedData = new SerializedData(tL_theme.getObjectSize());
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

        public void setCurrentAccentId(int i) {
            this.currentAccentId = i;
            ThemeAccent accent = getAccent(false);
            if (accent != null) {
                this.overrideWallpaper = accent.overrideWallpaper;
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

        public void setPreviewBackgroundColor(int i) {
            this.previewBackgroundColor = i;
        }

        public void setPreviewInColor(int i) {
            this.previewInColor = i;
        }

        public void setPreviewOutColor(int i) {
            this.previewOutColor = i;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:85:0x294d A[Catch: Exception -> 0x261c, TryCatch #1 {Exception -> 0x261c, blocks: (B:56:0x25f9, B:58:0x260f, B:59:0x2653, B:61:0x2661, B:62:0x268c, B:64:0x2690, B:66:0x2698, B:67:0x26aa, B:68:0x26b6, B:70:0x26bc, B:72:0x26c6, B:74:0x26ca, B:76:0x26f8, B:77:0x26fc, B:83:0x2947, B:85:0x294d, B:86:0x2956, B:88:0x295a, B:90:0x2962, B:92:0x2966, B:93:0x296a, B:95:0x296c, B:97:0x2976, B:79:0x2838, B:102:0x2859, B:103:0x2864, B:105:0x2870, B:109:0x287c, B:110:0x292b, B:107:0x2886, B:112:0x2889, B:172:0x282f, B:173:0x2837, B:180:0x2999, B:181:0x299f, B:184:0x29aa, B:186:0x29ff, B:187:0x2a0d, B:189:0x2a17, B:190:0x2a25, B:226:0x2a1e, B:227:0x2a06, B:229:0x266f, B:231:0x2677, B:233:0x2680, B:235:0x268a, B:236:0x261f, B:238:0x2627, B:240:0x262f, B:242:0x2639, B:244:0x2641, B:115:0x270d, B:165:0x281c, B:166:0x2821, B:119:0x2722, B:121:0x2737, B:122:0x2741, B:124:0x2753, B:127:0x2763, B:130:0x2770, B:131:0x2786, B:133:0x278b, B:134:0x2795, B:136:0x279a, B:139:0x27a9, B:142:0x27b2, B:144:0x27c6, B:147:0x27d2, B:149:0x27d9, B:150:0x27e9, B:152:0x27ed, B:153:0x27f1, B:155:0x27fc, B:156:0x2803, B:159:0x2790, B:160:0x277d), top: B:55:0x25f9, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x2966 A[Catch: Exception -> 0x261c, TryCatch #1 {Exception -> 0x261c, blocks: (B:56:0x25f9, B:58:0x260f, B:59:0x2653, B:61:0x2661, B:62:0x268c, B:64:0x2690, B:66:0x2698, B:67:0x26aa, B:68:0x26b6, B:70:0x26bc, B:72:0x26c6, B:74:0x26ca, B:76:0x26f8, B:77:0x26fc, B:83:0x2947, B:85:0x294d, B:86:0x2956, B:88:0x295a, B:90:0x2962, B:92:0x2966, B:93:0x296a, B:95:0x296c, B:97:0x2976, B:79:0x2838, B:102:0x2859, B:103:0x2864, B:105:0x2870, B:109:0x287c, B:110:0x292b, B:107:0x2886, B:112:0x2889, B:172:0x282f, B:173:0x2837, B:180:0x2999, B:181:0x299f, B:184:0x29aa, B:186:0x29ff, B:187:0x2a0d, B:189:0x2a17, B:190:0x2a25, B:226:0x2a1e, B:227:0x2a06, B:229:0x266f, B:231:0x2677, B:233:0x2680, B:235:0x268a, B:236:0x261f, B:238:0x2627, B:240:0x262f, B:242:0x2639, B:244:0x2641, B:115:0x270d, B:165:0x281c, B:166:0x2821, B:119:0x2722, B:121:0x2737, B:122:0x2741, B:124:0x2753, B:127:0x2763, B:130:0x2770, B:131:0x2786, B:133:0x278b, B:134:0x2795, B:136:0x279a, B:139:0x27a9, B:142:0x27b2, B:144:0x27c6, B:147:0x27d2, B:149:0x27d9, B:150:0x27e9, B:152:0x27ed, B:153:0x27f1, B:155:0x27fc, B:156:0x2803, B:159:0x2790, B:160:0x277d), top: B:55:0x25f9, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x2969  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x2976 A[Catch: Exception -> 0x261c, TryCatch #1 {Exception -> 0x261c, blocks: (B:56:0x25f9, B:58:0x260f, B:59:0x2653, B:61:0x2661, B:62:0x268c, B:64:0x2690, B:66:0x2698, B:67:0x26aa, B:68:0x26b6, B:70:0x26bc, B:72:0x26c6, B:74:0x26ca, B:76:0x26f8, B:77:0x26fc, B:83:0x2947, B:85:0x294d, B:86:0x2956, B:88:0x295a, B:90:0x2962, B:92:0x2966, B:93:0x296a, B:95:0x296c, B:97:0x2976, B:79:0x2838, B:102:0x2859, B:103:0x2864, B:105:0x2870, B:109:0x287c, B:110:0x292b, B:107:0x2886, B:112:0x2889, B:172:0x282f, B:173:0x2837, B:180:0x2999, B:181:0x299f, B:184:0x29aa, B:186:0x29ff, B:187:0x2a0d, B:189:0x2a17, B:190:0x2a25, B:226:0x2a1e, B:227:0x2a06, B:229:0x266f, B:231:0x2677, B:233:0x2680, B:235:0x268a, B:236:0x261f, B:238:0x2627, B:240:0x262f, B:242:0x2639, B:244:0x2641, B:115:0x270d, B:165:0x281c, B:166:0x2821, B:119:0x2722, B:121:0x2737, B:122:0x2741, B:124:0x2753, B:127:0x2763, B:130:0x2770, B:131:0x2786, B:133:0x278b, B:134:0x2795, B:136:0x279a, B:139:0x27a9, B:142:0x27b2, B:144:0x27c6, B:147:0x27d2, B:149:0x27d9, B:150:0x27e9, B:152:0x27ed, B:153:0x27f1, B:155:0x27fc, B:156:0x2803, B:159:0x2790, B:160:0x277d), top: B:55:0x25f9, inners: #0 }] */
    static {
        ThemeInfo themeInfo;
        ThemeInfo themeInfo2;
        ThemeInfo themeInfo3;
        ThemeInfo themeInfo4;
        SharedPreferences.Editor editor;
        String str;
        RuntimeException runtimeException;
        boolean z;
        ThemeInfo themeInfo5;
        SharedPreferences.Editor editor2;
        long readInt32;
        String str2;
        SparseArray sparseArray;
        ThemeAccent accent;
        SharedPreferences.Editor editor3;
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
        int i = 3;
        int i2 = colorsCount;
        key_wallpaperFileOffset = i2;
        key_dialogBackground = i2 + 1;
        key_dialogBackgroundGray = i2 + 2;
        key_dialogTextBlack = i2 + 3;
        key_dialogTextLink = i2 + 4;
        key_dialogLinkSelection = i2 + 5;
        key_dialogTextBlue = i2 + 6;
        key_dialogTextBlue2 = i2 + 7;
        key_dialogTextBlue4 = i2 + 8;
        key_dialogTextGray = i2 + 9;
        key_dialogTextGray2 = i2 + 10;
        key_dialogTextGray3 = i2 + 11;
        key_dialogTextGray4 = i2 + 12;
        key_dialogTextHint = i2 + 13;
        key_dialogInputField = i2 + 14;
        key_dialogInputFieldActivated = i2 + 15;
        key_dialogCheckboxSquareBackground = i2 + 16;
        key_dialogCheckboxSquareCheck = i2 + 17;
        key_dialogCheckboxSquareUnchecked = i2 + 18;
        key_dialogCheckboxSquareDisabled = i2 + 19;
        key_dialogScrollGlow = i2 + 20;
        key_dialogRoundCheckBox = i2 + 21;
        key_dialogRoundCheckBoxCheck = i2 + 22;
        key_dialogRadioBackground = i2 + 23;
        key_dialogRadioBackgroundChecked = i2 + 24;
        key_dialogLineProgress = i2 + 25;
        key_dialogLineProgressBackground = i2 + 26;
        key_dialogButton = i2 + 27;
        key_dialogButtonSelector = i2 + 28;
        key_dialogIcon = i2 + 29;
        key_dialogGrayLine = i2 + 30;
        key_dialogTopBackground = i2 + 31;
        key_dialogCameraIcon = i2 + 32;
        key_dialog_inlineProgressBackground = i2 + 33;
        key_dialog_inlineProgress = i2 + 34;
        key_dialogSearchBackground = i2 + 35;
        key_dialogSearchHint = i2 + 36;
        key_dialogSearchIcon = i2 + 37;
        key_dialogSearchText = i2 + 38;
        key_dialogFloatingButton = i2 + 39;
        key_dialogFloatingButtonPressed = i2 + 40;
        key_dialogFloatingIcon = i2 + 41;
        colorsCount = i2 + 43;
        key_dialogShadowLine = i2 + 42;
        int i3 = colorsCount;
        colorsCount = i3 + 1;
        key_dialogEmptyImage = i3;
        int i4 = colorsCount;
        colorsCount = i4 + 1;
        key_dialogEmptyText = i4;
        int i5 = colorsCount;
        colorsCount = i5 + 1;
        key_dialogSwipeRemove = i5;
        int i6 = colorsCount;
        colorsCount = i6 + 1;
        key_dialogReactionMentionBackground = i6;
        int i7 = colorsCount;
        colorsCount = i7 + 1;
        key_dialogCardShadow = i7;
        int i8 = colorsCount;
        colorsCount = i8 + 1;
        key_dialogGiftsBackground = i8;
        int i9 = colorsCount;
        colorsCount = i9 + 1;
        key_dialogGiftsTabText = i9;
        int i10 = colorsCount;
        colorsCount = i10 + 1;
        key_windowBackgroundWhite = i10;
        int i11 = colorsCount;
        colorsCount = i11 + 1;
        key_windowBackgroundUnchecked = i11;
        int i12 = colorsCount;
        colorsCount = i12 + 1;
        key_windowBackgroundChecked = i12;
        int i13 = colorsCount;
        colorsCount = i13 + 1;
        key_windowBackgroundCheckText = i13;
        int i14 = colorsCount;
        colorsCount = i14 + 1;
        key_progressCircle = i14;
        int i15 = colorsCount;
        colorsCount = i15 + 1;
        key_listSelector = i15;
        int i16 = colorsCount;
        colorsCount = i16 + 1;
        key_windowBackgroundWhiteInputField = i16;
        int i17 = colorsCount;
        colorsCount = i17 + 1;
        key_windowBackgroundWhiteInputFieldActivated = i17;
        int i18 = colorsCount;
        colorsCount = i18 + 1;
        key_windowBackgroundWhiteGrayIcon = i18;
        int i19 = colorsCount;
        colorsCount = i19 + 1;
        key_windowBackgroundWhiteBlueText = i19;
        int i20 = colorsCount;
        colorsCount = i20 + 1;
        key_windowBackgroundWhiteBlueText2 = i20;
        int i21 = colorsCount;
        colorsCount = i21 + 1;
        key_windowBackgroundWhiteBlueText3 = i21;
        int i22 = colorsCount;
        colorsCount = i22 + 1;
        key_windowBackgroundWhiteBlueText4 = i22;
        int i23 = colorsCount;
        colorsCount = i23 + 1;
        key_windowBackgroundWhiteBlueText5 = i23;
        int i24 = colorsCount;
        colorsCount = i24 + 1;
        key_windowBackgroundWhiteBlueText6 = i24;
        int i25 = colorsCount;
        colorsCount = i25 + 1;
        key_windowBackgroundWhiteBlueText7 = i25;
        int i26 = colorsCount;
        colorsCount = i26 + 1;
        key_windowBackgroundWhiteBlueButton = i26;
        int i27 = colorsCount;
        colorsCount = i27 + 1;
        key_windowBackgroundWhiteBlueIcon = i27;
        int i28 = colorsCount;
        colorsCount = i28 + 1;
        key_windowBackgroundWhiteGreenText = i28;
        int i29 = colorsCount;
        colorsCount = i29 + 1;
        key_windowBackgroundWhiteGreenText2 = i29;
        int i30 = colorsCount;
        colorsCount = i30 + 1;
        key_windowBackgroundWhiteGrayText = i30;
        int i31 = colorsCount;
        colorsCount = i31 + 1;
        key_windowBackgroundWhiteGrayText2 = i31;
        int i32 = colorsCount;
        colorsCount = i32 + 1;
        key_windowBackgroundWhiteGrayText3 = i32;
        int i33 = colorsCount;
        colorsCount = i33 + 1;
        key_windowBackgroundWhiteGrayText4 = i33;
        int i34 = colorsCount;
        colorsCount = i34 + 1;
        key_windowBackgroundWhiteGrayText5 = i34;
        int i35 = colorsCount;
        colorsCount = i35 + 1;
        key_windowBackgroundWhiteGrayText6 = i35;
        int i36 = colorsCount;
        colorsCount = i36 + 1;
        key_windowBackgroundWhiteGrayText7 = i36;
        int i37 = colorsCount;
        colorsCount = i37 + 1;
        key_windowBackgroundWhiteGrayText8 = i37;
        int i38 = colorsCount;
        colorsCount = i38 + 1;
        key_windowBackgroundWhiteBlackText = i38;
        int i39 = colorsCount;
        colorsCount = i39 + 1;
        key_windowBackgroundWhiteHintText = i39;
        int i40 = colorsCount;
        colorsCount = i40 + 1;
        key_windowBackgroundWhiteValueText = i40;
        int i41 = colorsCount;
        colorsCount = i41 + 1;
        key_windowBackgroundWhiteLinkText = i41;
        int i42 = colorsCount;
        colorsCount = i42 + 1;
        key_windowBackgroundWhiteLinkSelection = i42;
        int i43 = colorsCount;
        colorsCount = i43 + 1;
        key_windowBackgroundWhiteBlueHeader = i43;
        int i44 = colorsCount;
        colorsCount = i44 + 1;
        key_switchTrack = i44;
        int i45 = colorsCount;
        colorsCount = i45 + 1;
        key_switchTrackChecked = i45;
        int i46 = colorsCount;
        colorsCount = i46 + 1;
        key_switchTrackBlue = i46;
        int i47 = colorsCount;
        colorsCount = i47 + 1;
        key_switchTrackBlueChecked = i47;
        int i48 = colorsCount;
        colorsCount = i48 + 1;
        key_switchTrackBlueThumb = i48;
        int i49 = colorsCount;
        colorsCount = i49 + 1;
        key_switchTrackBlueThumbChecked = i49;
        int i50 = colorsCount;
        colorsCount = i50 + 1;
        key_switchTrackBlueSelector = i50;
        int i51 = colorsCount;
        colorsCount = i51 + 1;
        key_switchTrackBlueSelectorChecked = i51;
        int i52 = colorsCount;
        colorsCount = i52 + 1;
        key_switch2Track = i52;
        int i53 = colorsCount;
        colorsCount = i53 + 1;
        key_switch2TrackChecked = i53;
        int i54 = colorsCount;
        colorsCount = i54 + 1;
        key_checkboxSquareBackground = i54;
        int i55 = colorsCount;
        colorsCount = i55 + 1;
        key_checkboxSquareCheck = i55;
        int i56 = colorsCount;
        colorsCount = i56 + 1;
        key_checkboxSquareUnchecked = i56;
        int i57 = colorsCount;
        colorsCount = i57 + 1;
        key_checkboxSquareDisabled = i57;
        int i58 = colorsCount;
        colorsCount = i58 + 1;
        key_windowBackgroundGray = i58;
        int i59 = colorsCount;
        colorsCount = i59 + 1;
        key_windowBackgroundGrayShadow = i59;
        int i60 = colorsCount;
        colorsCount = i60 + 1;
        key_emptyListPlaceholder = i60;
        int i61 = colorsCount;
        colorsCount = i61 + 1;
        key_divider = i61;
        int i62 = colorsCount;
        colorsCount = i62 + 1;
        key_graySection = i62;
        int i63 = colorsCount;
        colorsCount = i63 + 1;
        key_graySectionText = i63;
        int i64 = colorsCount;
        colorsCount = i64 + 1;
        key_radioBackground = i64;
        int i65 = colorsCount;
        colorsCount = i65 + 1;
        key_radioBackgroundChecked = i65;
        int i66 = colorsCount;
        colorsCount = i66 + 1;
        key_checkbox = i66;
        int i67 = colorsCount;
        colorsCount = i67 + 1;
        key_checkboxDisabled = i67;
        int i68 = colorsCount;
        colorsCount = i68 + 1;
        key_checkboxCheck = i68;
        int i69 = colorsCount;
        colorsCount = i69 + 1;
        key_fastScrollActive = i69;
        int i70 = colorsCount;
        colorsCount = i70 + 1;
        key_fastScrollInactive = i70;
        int i71 = colorsCount;
        colorsCount = i71 + 1;
        key_fastScrollText = i71;
        int i72 = colorsCount;
        colorsCount = i72 + 1;
        key_text_RedRegular = i72;
        int i73 = colorsCount;
        colorsCount = i73 + 1;
        key_text_RedBold = i73;
        int i74 = colorsCount;
        colorsCount = i74 + 1;
        key_fill_RedNormal = i74;
        int i75 = colorsCount;
        colorsCount = i75 + 1;
        key_fill_RedDark = i75;
        int i76 = colorsCount;
        colorsCount = i76 + 1;
        key_inappPlayerPerformer = i76;
        int i77 = colorsCount;
        colorsCount = i77 + 1;
        key_inappPlayerTitle = i77;
        int i78 = colorsCount;
        colorsCount = i78 + 1;
        key_inappPlayerBackground = i78;
        int i79 = colorsCount;
        colorsCount = i79 + 1;
        key_inappPlayerPlayPause = i79;
        int i80 = colorsCount;
        colorsCount = i80 + 1;
        key_inappPlayerClose = i80;
        int i81 = colorsCount;
        colorsCount = i81 + 1;
        key_returnToCallBackground = i81;
        int i82 = colorsCount;
        colorsCount = i82 + 1;
        key_returnToCallMutedBackground = i82;
        int i83 = colorsCount;
        colorsCount = i83 + 1;
        key_returnToCallText = i83;
        int i84 = colorsCount;
        colorsCount = i84 + 1;
        key_contextProgressInner1 = i84;
        int i85 = colorsCount;
        colorsCount = i85 + 1;
        key_contextProgressOuter1 = i85;
        int i86 = colorsCount;
        colorsCount = i86 + 1;
        key_contextProgressInner2 = i86;
        int i87 = colorsCount;
        colorsCount = i87 + 1;
        key_contextProgressOuter2 = i87;
        int i88 = colorsCount;
        colorsCount = i88 + 1;
        key_contextProgressInner3 = i88;
        int i89 = colorsCount;
        colorsCount = i89 + 1;
        key_contextProgressOuter3 = i89;
        int i90 = colorsCount;
        colorsCount = i90 + 1;
        key_contextProgressInner4 = i90;
        int i91 = colorsCount;
        colorsCount = i91 + 1;
        key_contextProgressOuter4 = i91;
        int i92 = colorsCount;
        colorsCount = i92 + 1;
        key_avatar_text = i92;
        int i93 = colorsCount;
        colorsCount = i93 + 1;
        key_avatar_backgroundSaved = i93;
        int i94 = colorsCount;
        colorsCount = i94 + 1;
        key_avatar_background2Saved = i94;
        int i95 = colorsCount;
        colorsCount = i95 + 1;
        key_avatar_backgroundArchived = i95;
        int i96 = colorsCount;
        colorsCount = i96 + 1;
        key_avatar_backgroundArchivedHidden = i96;
        int i97 = colorsCount;
        colorsCount = i97 + 1;
        key_avatar_backgroundRed = i97;
        int i98 = colorsCount;
        colorsCount = i98 + 1;
        key_avatar_backgroundOrange = i98;
        int i99 = colorsCount;
        colorsCount = i99 + 1;
        key_avatar_backgroundViolet = i99;
        int i100 = colorsCount;
        colorsCount = i100 + 1;
        key_avatar_backgroundGreen = i100;
        int i101 = colorsCount;
        colorsCount = i101 + 1;
        key_avatar_backgroundCyan = i101;
        int i102 = colorsCount;
        colorsCount = i102 + 1;
        key_avatar_backgroundBlue = i102;
        int i103 = colorsCount;
        colorsCount = i103 + 1;
        key_avatar_backgroundPink = i103;
        int i104 = colorsCount;
        colorsCount = i104 + 1;
        key_avatar_background2Red = i104;
        int i105 = colorsCount;
        colorsCount = i105 + 1;
        key_avatar_background2Orange = i105;
        int i106 = colorsCount;
        colorsCount = i106 + 1;
        key_avatar_background2Violet = i106;
        int i107 = colorsCount;
        colorsCount = i107 + 1;
        key_avatar_background2Green = i107;
        int i108 = colorsCount;
        colorsCount = i108 + 1;
        key_avatar_background2Cyan = i108;
        int i109 = colorsCount;
        colorsCount = i109 + 1;
        key_avatar_background2Blue = i109;
        int i110 = colorsCount;
        colorsCount = i110 + 1;
        key_avatar_background2Pink = i110;
        int i111 = colorsCount;
        colorsCount = i111 + 1;
        key_avatar_backgroundGray = i111;
        int i112 = colorsCount;
        colorsCount = i112 + 1;
        key_avatar_backgroundInProfileBlue = i112;
        int i113 = colorsCount;
        colorsCount = i113 + 1;
        key_avatar_backgroundActionBarBlue = i113;
        int i114 = colorsCount;
        colorsCount = i114 + 1;
        key_avatar_actionBarSelectorBlue = i114;
        int i115 = colorsCount;
        colorsCount = i115 + 1;
        key_avatar_actionBarIconBlue = i115;
        int i116 = colorsCount;
        colorsCount = i116 + 1;
        key_avatar_subtitleInProfileBlue = i116;
        int i117 = colorsCount;
        colorsCount = i117 + 1;
        key_avatar_nameInMessageRed = i117;
        int i118 = colorsCount;
        colorsCount = i118 + 1;
        key_avatar_nameInMessageOrange = i118;
        int i119 = colorsCount;
        colorsCount = i119 + 1;
        key_avatar_nameInMessageViolet = i119;
        int i120 = colorsCount;
        colorsCount = i120 + 1;
        key_avatar_nameInMessageGreen = i120;
        int i121 = colorsCount;
        colorsCount = i121 + 1;
        key_avatar_nameInMessageCyan = i121;
        int i122 = colorsCount;
        colorsCount = i122 + 1;
        key_avatar_nameInMessageBlue = i122;
        int i123 = colorsCount;
        colorsCount = i123 + 1;
        key_avatar_nameInMessagePink = i123;
        keys_avatar_background = new int[]{i97, i98, i99, i100, i101, i102, i103};
        keys_avatar_background2 = new int[]{i104, i105, i106, i107, i108, i109, i110};
        keys_avatar_nameInMessage = new int[]{i117, i118, i119, i120, i121, i122, i123};
        int i124 = colorsCount;
        colorsCount = i124 + 1;
        key_actionBarDefault = i124;
        int i125 = colorsCount;
        colorsCount = i125 + 1;
        key_actionBarDefaultSelector = i125;
        int i126 = colorsCount;
        colorsCount = i126 + 1;
        key_actionBarWhiteSelector = i126;
        int i127 = colorsCount;
        colorsCount = i127 + 1;
        key_actionBarDefaultIcon = i127;
        int i128 = colorsCount;
        colorsCount = i128 + 1;
        key_actionBarActionModeDefault = i128;
        int i129 = colorsCount;
        colorsCount = i129 + 1;
        key_actionBarActionModeDefaultTop = i129;
        int i130 = colorsCount;
        colorsCount = i130 + 1;
        key_actionBarActionModeDefaultIcon = i130;
        int i131 = colorsCount;
        colorsCount = i131 + 1;
        key_actionBarActionModeDefaultSelector = i131;
        int i132 = colorsCount;
        colorsCount = i132 + 1;
        key_actionBarDefaultTitle = i132;
        int i133 = colorsCount;
        colorsCount = i133 + 1;
        key_actionBarDefaultSubtitle = i133;
        int i134 = colorsCount;
        colorsCount = i134 + 1;
        key_actionBarDefaultSearch = i134;
        int i135 = colorsCount;
        colorsCount = i135 + 1;
        key_actionBarDefaultSearchPlaceholder = i135;
        int i136 = colorsCount;
        colorsCount = i136 + 1;
        key_actionBarDefaultSubmenuItem = i136;
        int i137 = colorsCount;
        colorsCount = i137 + 1;
        key_actionBarDefaultSubmenuItemIcon = i137;
        int i138 = colorsCount;
        colorsCount = i138 + 1;
        key_actionBarDefaultSubmenuBackground = i138;
        int i139 = colorsCount;
        colorsCount = i139 + 1;
        key_actionBarDefaultSubmenuSeparator = i139;
        int i140 = colorsCount;
        colorsCount = i140 + 1;
        key_actionBarTabActiveText = i140;
        int i141 = colorsCount;
        colorsCount = i141 + 1;
        key_actionBarTabUnactiveText = i141;
        int i142 = colorsCount;
        colorsCount = i142 + 1;
        key_actionBarTabLine = i142;
        int i143 = colorsCount;
        colorsCount = i143 + 1;
        key_actionBarTabSelector = i143;
        int i144 = colorsCount;
        colorsCount = i144 + 1;
        key_actionBarDefaultArchived = i144;
        int i145 = colorsCount;
        colorsCount = i145 + 1;
        key_actionBarDefaultArchivedSelector = i145;
        int i146 = colorsCount;
        colorsCount = i146 + 1;
        key_actionBarDefaultArchivedIcon = i146;
        int i147 = colorsCount;
        colorsCount = i147 + 1;
        key_actionBarDefaultArchivedTitle = i147;
        int i148 = colorsCount;
        colorsCount = i148 + 1;
        key_actionBarDefaultArchivedSearch = i148;
        int i149 = colorsCount;
        colorsCount = i149 + 1;
        key_actionBarDefaultArchivedSearchPlaceholder = i149;
        int i150 = colorsCount;
        colorsCount = i150 + 1;
        key_actionBarBrowser = i150;
        int i151 = colorsCount;
        colorsCount = i151 + 1;
        key_chats_onlineCircle = i151;
        int i152 = colorsCount;
        colorsCount = i152 + 1;
        key_chats_unreadCounter = i152;
        int i153 = colorsCount;
        colorsCount = i153 + 1;
        key_chats_unreadCounterMuted = i153;
        int i154 = colorsCount;
        colorsCount = i154 + 1;
        key_chats_unreadCounterText = i154;
        int i155 = colorsCount;
        colorsCount = i155 + 1;
        key_chats_name = i155;
        int i156 = colorsCount;
        colorsCount = i156 + 1;
        key_chats_nameArchived = i156;
        int i157 = colorsCount;
        colorsCount = i157 + 1;
        key_chats_secretName = i157;
        int i158 = colorsCount;
        colorsCount = i158 + 1;
        key_chats_secretIcon = i158;
        int i159 = colorsCount;
        colorsCount = i159 + 1;
        key_chats_pinnedIcon = i159;
        int i160 = colorsCount;
        colorsCount = i160 + 1;
        key_chats_archiveBackground = i160;
        int i161 = colorsCount;
        colorsCount = i161 + 1;
        key_chats_archivePinBackground = i161;
        int i162 = colorsCount;
        colorsCount = i162 + 1;
        key_chats_archiveIcon = i162;
        int i163 = colorsCount;
        colorsCount = i163 + 1;
        key_chats_archiveText = i163;
        int i164 = colorsCount;
        colorsCount = i164 + 1;
        key_chats_message = i164;
        int i165 = colorsCount;
        colorsCount = i165 + 1;
        key_chats_messageArchived = i165;
        int i166 = colorsCount;
        colorsCount = i166 + 1;
        key_chats_message_threeLines = i166;
        int i167 = colorsCount;
        colorsCount = i167 + 1;
        key_chats_draft = i167;
        int i168 = colorsCount;
        colorsCount = i168 + 1;
        key_chats_nameMessage = i168;
        int i169 = colorsCount;
        colorsCount = i169 + 1;
        key_chats_nameMessageArchived = i169;
        int i170 = colorsCount;
        colorsCount = i170 + 1;
        key_chats_nameMessage_threeLines = i170;
        int i171 = colorsCount;
        colorsCount = i171 + 1;
        key_chats_nameMessageArchived_threeLines = i171;
        int i172 = colorsCount;
        colorsCount = i172 + 1;
        key_chats_attachMessage = i172;
        int i173 = colorsCount;
        colorsCount = i173 + 1;
        key_chats_actionMessage = i173;
        int i174 = colorsCount;
        colorsCount = i174 + 1;
        key_chats_date = i174;
        int i175 = colorsCount;
        colorsCount = i175 + 1;
        key_chats_pinnedOverlay = i175;
        int i176 = colorsCount;
        colorsCount = i176 + 1;
        key_chats_tabletSelectedOverlay = i176;
        int i177 = colorsCount;
        colorsCount = i177 + 1;
        key_chats_sentCheck = i177;
        int i178 = colorsCount;
        colorsCount = i178 + 1;
        key_chats_sentReadCheck = i178;
        int i179 = colorsCount;
        colorsCount = i179 + 1;
        key_chats_sentClock = i179;
        int i180 = colorsCount;
        colorsCount = i180 + 1;
        key_chats_sentError = i180;
        int i181 = colorsCount;
        colorsCount = i181 + 1;
        key_chats_sentErrorIcon = i181;
        int i182 = colorsCount;
        colorsCount = i182 + 1;
        key_chats_verifiedBackground = i182;
        int i183 = colorsCount;
        colorsCount = i183 + 1;
        key_chats_verifiedCheck = i183;
        int i184 = colorsCount;
        colorsCount = i184 + 1;
        key_chats_muteIcon = i184;
        int i185 = colorsCount;
        colorsCount = i185 + 1;
        key_chats_mentionIcon = i185;
        int i186 = colorsCount;
        colorsCount = i186 + 1;
        key_chats_menuTopShadow = i186;
        int i187 = colorsCount;
        colorsCount = i187 + 1;
        key_chats_menuTopShadowCats = i187;
        int i188 = colorsCount;
        colorsCount = i188 + 1;
        key_chats_menuBackground = i188;
        int i189 = colorsCount;
        colorsCount = i189 + 1;
        key_chats_menuItemText = i189;
        int i190 = colorsCount;
        colorsCount = i190 + 1;
        key_chats_menuItemCheck = i190;
        int i191 = colorsCount;
        colorsCount = i191 + 1;
        key_chats_menuItemIcon = i191;
        int i192 = colorsCount;
        colorsCount = i192 + 1;
        key_chats_menuName = i192;
        int i193 = colorsCount;
        colorsCount = i193 + 1;
        key_chats_menuPhone = i193;
        int i194 = colorsCount;
        colorsCount = i194 + 1;
        key_chats_menuPhoneCats = i194;
        int i195 = colorsCount;
        colorsCount = i195 + 1;
        key_chats_menuTopBackgroundCats = i195;
        int i196 = colorsCount;
        colorsCount = i196 + 1;
        key_chats_menuTopBackground = i196;
        int i197 = colorsCount;
        colorsCount = i197 + 1;
        key_chats_actionIcon = i197;
        int i198 = colorsCount;
        colorsCount = i198 + 1;
        key_chats_actionBackground = i198;
        int i199 = colorsCount;
        colorsCount = i199 + 1;
        key_chats_actionPressedBackground = i199;
        int i200 = colorsCount;
        colorsCount = i200 + 1;
        key_chats_archivePullDownBackground = i200;
        int i201 = colorsCount;
        colorsCount = i201 + 1;
        key_chats_archivePullDownBackgroundActive = i201;
        int i202 = colorsCount;
        colorsCount = i202 + 1;
        key_chats_tabUnreadActiveBackground = i202;
        int i203 = colorsCount;
        colorsCount = i203 + 1;
        key_chats_tabUnreadUnactiveBackground = i203;
        int i204 = colorsCount;
        colorsCount = i204 + 1;
        key_chat_attachCheckBoxCheck = i204;
        int i205 = colorsCount;
        colorsCount = i205 + 1;
        key_chat_attachCheckBoxBackground = i205;
        int i206 = colorsCount;
        colorsCount = i206 + 1;
        key_chat_attachPhotoBackground = i206;
        int i207 = colorsCount;
        colorsCount = i207 + 1;
        key_chat_attachActiveTab = i207;
        int i208 = colorsCount;
        colorsCount = i208 + 1;
        key_chat_attachUnactiveTab = i208;
        int i209 = colorsCount;
        colorsCount = i209 + 1;
        key_chat_attachPermissionImage = i209;
        int i210 = colorsCount;
        colorsCount = i210 + 1;
        key_chat_attachPermissionMark = i210;
        int i211 = colorsCount;
        colorsCount = i211 + 1;
        key_chat_attachPermissionText = i211;
        int i212 = colorsCount;
        colorsCount = i212 + 1;
        key_chat_attachEmptyImage = i212;
        int i213 = colorsCount;
        colorsCount = i213 + 1;
        key_chat_inPollCorrectAnswer = i213;
        int i214 = colorsCount;
        colorsCount = i214 + 1;
        key_chat_outPollCorrectAnswer = i214;
        int i215 = colorsCount;
        colorsCount = i215 + 1;
        key_chat_inPollWrongAnswer = i215;
        int i216 = colorsCount;
        colorsCount = i216 + 1;
        key_chat_outPollWrongAnswer = i216;
        int i217 = colorsCount;
        colorsCount = i217 + 1;
        key_chat_attachIcon = i217;
        int i218 = colorsCount;
        colorsCount = i218 + 1;
        key_chat_attachGalleryBackground = i218;
        int i219 = colorsCount;
        colorsCount = i219 + 1;
        key_chat_attachGalleryText = i219;
        int i220 = colorsCount;
        colorsCount = i220 + 1;
        key_chat_attachAudioBackground = i220;
        int i221 = colorsCount;
        colorsCount = i221 + 1;
        key_chat_attachAudioText = i221;
        int i222 = colorsCount;
        colorsCount = i222 + 1;
        key_chat_attachFileBackground = i222;
        int i223 = colorsCount;
        colorsCount = i223 + 1;
        key_chat_attachFileText = i223;
        int i224 = colorsCount;
        colorsCount = i224 + 1;
        key_chat_attachContactBackground = i224;
        int i225 = colorsCount;
        colorsCount = i225 + 1;
        key_chat_attachContactText = i225;
        int i226 = colorsCount;
        colorsCount = i226 + 1;
        key_chat_attachLocationBackground = i226;
        int i227 = colorsCount;
        colorsCount = i227 + 1;
        key_chat_attachLocationText = i227;
        int i228 = colorsCount;
        colorsCount = i228 + 1;
        key_chat_attachPollBackground = i228;
        int i229 = colorsCount;
        colorsCount = i229 + 1;
        key_chat_attachPollText = i229;
        int i230 = colorsCount;
        colorsCount = i230 + 1;
        key_chat_status = i230;
        int i231 = colorsCount;
        colorsCount = i231 + 1;
        key_chat_inGreenCall = i231;
        int i232 = colorsCount;
        colorsCount = i232 + 1;
        key_chat_inBubble = i232;
        int i233 = colorsCount;
        colorsCount = i233 + 1;
        key_chat_inBubbleSelectedOverlay = i233;
        int i234 = colorsCount;
        colorsCount = i234 + 1;
        key_chat_inBubbleShadow = i234;
        int i235 = colorsCount;
        colorsCount = i235 + 1;
        key_actionBarActionModeReaction = i235;
        int i236 = colorsCount;
        colorsCount = i236 + 1;
        key_actionBarActionModeReactionText = i236;
        int i237 = colorsCount;
        colorsCount = i237 + 1;
        key_actionBarActionModeReactionDot = i237;
        myMessagesBubblesStartIndex = colorsCount;
        int i238 = colorsCount;
        colorsCount = i238 + 1;
        key_chat_outBubble = i238;
        int i239 = colorsCount;
        colorsCount = i239 + 1;
        key_chat_outBubbleSelected = i239;
        int i240 = colorsCount;
        colorsCount = i240 + 1;
        key_chat_outBubbleShadow = i240;
        int i241 = colorsCount;
        colorsCount = i241 + 1;
        key_chat_outBubbleGradient1 = i241;
        int i242 = colorsCount;
        colorsCount = i242 + 1;
        key_chat_outBubbleGradient2 = i242;
        int i243 = colorsCount;
        colorsCount = i243 + 1;
        key_chat_outBubbleGradient3 = i243;
        myMessagesBubblesEndIndex = colorsCount;
        myMessagesStartIndex = colorsCount;
        int i244 = colorsCount;
        colorsCount = i244 + 1;
        key_chat_outGreenCall = i244;
        int i245 = colorsCount;
        colorsCount = i245 + 1;
        key_chat_outSentCheck = i245;
        int i246 = colorsCount;
        colorsCount = i246 + 1;
        key_chat_outSentCheckSelected = i246;
        int i247 = colorsCount;
        colorsCount = i247 + 1;
        key_chat_outSentCheckRead = i247;
        int i248 = colorsCount;
        colorsCount = i248 + 1;
        key_chat_outSentCheckReadSelected = i248;
        int i249 = colorsCount;
        colorsCount = i249 + 1;
        key_chat_outSentClock = i249;
        int i250 = colorsCount;
        colorsCount = i250 + 1;
        key_chat_outSentClockSelected = i250;
        int i251 = colorsCount;
        colorsCount = i251 + 1;
        key_chat_outMediaIcon = i251;
        int i252 = colorsCount;
        colorsCount = i252 + 1;
        key_chat_outMediaIconSelected = i252;
        int i253 = colorsCount;
        colorsCount = i253 + 1;
        key_chat_outViews = i253;
        int i254 = colorsCount;
        colorsCount = i254 + 1;
        key_chat_outViewsSelected = i254;
        int i255 = colorsCount;
        colorsCount = i255 + 1;
        key_chat_outMenu = i255;
        int i256 = colorsCount;
        colorsCount = i256 + 1;
        key_chat_outMenuSelected = i256;
        int i257 = colorsCount;
        colorsCount = i257 + 1;
        key_chat_outInstant = i257;
        int i258 = colorsCount;
        colorsCount = i258 + 1;
        key_chat_outInstantSelected = i258;
        int i259 = colorsCount;
        colorsCount = i259 + 1;
        key_chat_outPreviewInstantText = i259;
        int i260 = colorsCount;
        colorsCount = i260 + 1;
        key_chat_outForwardedNameText = i260;
        int i261 = colorsCount;
        colorsCount = i261 + 1;
        key_chat_outViaBotNameText = i261;
        int i262 = colorsCount;
        colorsCount = i262 + 1;
        key_chat_outReplyLine = i262;
        int i263 = colorsCount;
        colorsCount = i263 + 1;
        key_chat_outReplyLine2 = i263;
        int i264 = colorsCount;
        colorsCount = i264 + 1;
        key_chat_outReplyNameText = i264;
        int i265 = colorsCount;
        colorsCount = i265 + 1;
        key_chat_outReplyMessageText = i265;
        int i266 = colorsCount;
        colorsCount = i266 + 1;
        key_chat_outReplyMediaMessageText = i266;
        int i267 = colorsCount;
        colorsCount = i267 + 1;
        key_chat_outReplyMediaMessageSelectedText = i267;
        int i268 = colorsCount;
        colorsCount = i268 + 1;
        key_chat_outPreviewLine = i268;
        int i269 = colorsCount;
        colorsCount = i269 + 1;
        key_chat_outSiteNameText = i269;
        int i270 = colorsCount;
        colorsCount = i270 + 1;
        key_chat_outContactNameText = i270;
        int i271 = colorsCount;
        colorsCount = i271 + 1;
        key_chat_outContactPhoneText = i271;
        int i272 = colorsCount;
        colorsCount = i272 + 1;
        key_chat_outContactPhoneSelectedText = i272;
        int i273 = colorsCount;
        colorsCount = i273 + 1;
        key_chat_outAudioPerformerText = i273;
        int i274 = colorsCount;
        colorsCount = i274 + 1;
        key_chat_outAudioPerformerSelectedText = i274;
        int i275 = colorsCount;
        colorsCount = i275 + 1;
        key_chat_outTimeSelectedText = i275;
        int i276 = colorsCount;
        colorsCount = i276 + 1;
        key_chat_outAdminText = i276;
        int i277 = colorsCount;
        colorsCount = i277 + 1;
        key_chat_outAdminSelectedText = i277;
        int i278 = colorsCount;
        colorsCount = i278 + 1;
        key_chat_outAudioProgress = i278;
        int i279 = colorsCount;
        colorsCount = i279 + 1;
        key_chat_outAudioSelectedProgress = i279;
        int i280 = colorsCount;
        colorsCount = i280 + 1;
        key_chat_outTimeText = i280;
        int i281 = colorsCount;
        colorsCount = i281 + 1;
        key_chat_outAudioTitleText = i281;
        int i282 = colorsCount;
        colorsCount = i282 + 1;
        key_chat_outAudioDurationText = i282;
        int i283 = colorsCount;
        colorsCount = i283 + 1;
        key_chat_outAudioDurationSelectedText = i283;
        int i284 = colorsCount;
        colorsCount = i284 + 1;
        key_chat_outAudioSeekbar = i284;
        int i285 = colorsCount;
        colorsCount = i285 + 1;
        key_chat_outAudioCacheSeekbar = i285;
        int i286 = colorsCount;
        colorsCount = i286 + 1;
        key_chat_outAudioSeekbarSelected = i286;
        int i287 = colorsCount;
        colorsCount = i287 + 1;
        key_chat_outAudioSeekbarFill = i287;
        int i288 = colorsCount;
        colorsCount = i288 + 1;
        key_chat_outVoiceSeekbar = i288;
        int i289 = colorsCount;
        colorsCount = i289 + 1;
        key_chat_outVoiceSeekbarSelected = i289;
        int i290 = colorsCount;
        colorsCount = i290 + 1;
        key_chat_outVoiceSeekbarFill = i290;
        int i291 = colorsCount;
        colorsCount = i291 + 1;
        key_chat_outFileProgress = i291;
        int i292 = colorsCount;
        colorsCount = i292 + 1;
        key_chat_outFileProgressSelected = i292;
        int i293 = colorsCount;
        colorsCount = i293 + 1;
        key_chat_outFileNameText = i293;
        int i294 = colorsCount;
        colorsCount = i294 + 1;
        key_chat_outFileInfoText = i294;
        int i295 = colorsCount;
        colorsCount = i295 + 1;
        key_chat_outFileInfoSelectedText = i295;
        int i296 = colorsCount;
        colorsCount = i296 + 1;
        key_chat_outFileBackground = i296;
        int i297 = colorsCount;
        colorsCount = i297 + 1;
        key_chat_outFileBackgroundSelected = i297;
        int i298 = colorsCount;
        colorsCount = i298 + 1;
        key_chat_outVenueInfoText = i298;
        int i299 = colorsCount;
        colorsCount = i299 + 1;
        key_chat_outVenueInfoSelectedText = i299;
        int i300 = colorsCount;
        colorsCount = i300 + 1;
        key_chat_outLinkSelectBackground = i300;
        int i301 = colorsCount;
        colorsCount = i301 + 1;
        key_chat_outLoader = i301;
        int i302 = colorsCount;
        colorsCount = i302 + 1;
        key_chat_outLoaderSelected = i302;
        int i303 = colorsCount;
        colorsCount = i303 + 1;
        key_chat_outLocationIcon = i303;
        int i304 = colorsCount;
        colorsCount = i304 + 1;
        key_chat_outContactBackground = i304;
        int i305 = colorsCount;
        colorsCount = i305 + 1;
        key_chat_outContactIcon = i305;
        int i306 = colorsCount;
        colorsCount = i306 + 1;
        key_chat_outReactionButtonBackground = i306;
        myMessagesEndIndex = colorsCount;
        int i307 = colorsCount;
        colorsCount = i307 + 1;
        key_chat_outTextSelectionHighlight = i307;
        int i308 = colorsCount;
        colorsCount = i308 + 1;
        key_chat_outTextSelectionCursor = i308;
        int i309 = colorsCount;
        colorsCount = i309 + 1;
        key_chat_outBubbleLocationPlaceholder = i309;
        int i310 = colorsCount;
        colorsCount = i310 + 1;
        key_chat_outBubbleSelectedOverlay = i310;
        int i311 = colorsCount;
        colorsCount = i311 + 1;
        key_chat_outPsaNameText = i311;
        int i312 = colorsCount;
        colorsCount = i312 + 1;
        key_chat_outBubbleGradientAnimated = i312;
        int i313 = colorsCount;
        colorsCount = i313 + 1;
        key_chat_outBubbleGradientSelectedOverlay = i313;
        int i314 = colorsCount;
        colorsCount = i314 + 1;
        key_chat_inBubbleSelected = i314;
        int i315 = colorsCount;
        colorsCount = i315 + 1;
        key_chat_messageTextIn = i315;
        int i316 = colorsCount;
        colorsCount = i316 + 1;
        key_chat_messageTextOut = i316;
        int i317 = colorsCount;
        colorsCount = i317 + 1;
        key_chat_messageLinkIn = i317;
        int i318 = colorsCount;
        colorsCount = i318 + 1;
        key_chat_messageLinkOut = i318;
        int i319 = colorsCount;
        colorsCount = i319 + 1;
        key_chat_serviceText = i319;
        int i320 = colorsCount;
        colorsCount = i320 + 1;
        key_chat_serviceLink = i320;
        int i321 = colorsCount;
        colorsCount = i321 + 1;
        key_chat_serviceIcon = i321;
        int i322 = colorsCount;
        colorsCount = i322 + 1;
        key_chat_serviceBackground = i322;
        int i323 = colorsCount;
        colorsCount = i323 + 1;
        key_chat_serviceBackgroundSelected = i323;
        int i324 = colorsCount;
        colorsCount = i324 + 1;
        key_chat_serviceBackgroundSelector = i324;
        int i325 = colorsCount;
        colorsCount = i325 + 1;
        key_chat_muteIcon = i325;
        int i326 = colorsCount;
        colorsCount = i326 + 1;
        key_chat_lockIcon = i326;
        int i327 = colorsCount;
        colorsCount = i327 + 1;
        key_chat_inSentClock = i327;
        int i328 = colorsCount;
        colorsCount = i328 + 1;
        key_chat_inSentClockSelected = i328;
        int i329 = colorsCount;
        colorsCount = i329 + 1;
        key_chat_mediaSentCheck = i329;
        int i330 = colorsCount;
        colorsCount = i330 + 1;
        key_chat_mediaSentClock = i330;
        int i331 = colorsCount;
        colorsCount = i331 + 1;
        key_chat_inMediaIcon = i331;
        int i332 = colorsCount;
        colorsCount = i332 + 1;
        key_chat_inMediaIconSelected = i332;
        int i333 = colorsCount;
        colorsCount = i333 + 1;
        key_chat_mediaTimeBackground = i333;
        int i334 = colorsCount;
        colorsCount = i334 + 1;
        key_chat_inViews = i334;
        int i335 = colorsCount;
        colorsCount = i335 + 1;
        key_chat_inViewsSelected = i335;
        int i336 = colorsCount;
        colorsCount = i336 + 1;
        key_chat_mediaViews = i336;
        int i337 = colorsCount;
        colorsCount = i337 + 1;
        key_chat_inMenu = i337;
        int i338 = colorsCount;
        colorsCount = i338 + 1;
        key_chat_inMenuSelected = i338;
        int i339 = colorsCount;
        colorsCount = i339 + 1;
        key_chat_mediaMenu = i339;
        int i340 = colorsCount;
        colorsCount = i340 + 1;
        key_chat_inInstant = i340;
        int i341 = colorsCount;
        colorsCount = i341 + 1;
        key_chat_inInstantSelected = i341;
        int i342 = colorsCount;
        colorsCount = i342 + 1;
        key_chat_sentError = i342;
        int i343 = colorsCount;
        colorsCount = i343 + 1;
        key_chat_sentErrorIcon = i343;
        int i344 = colorsCount;
        colorsCount = i344 + 1;
        key_chat_selectedBackground = i344;
        int i345 = colorsCount;
        colorsCount = i345 + 1;
        key_chat_previewDurationText = i345;
        int i346 = colorsCount;
        colorsCount = i346 + 1;
        key_chat_previewGameText = i346;
        int i347 = colorsCount;
        colorsCount = i347 + 1;
        key_chat_inPreviewInstantText = i347;
        int i348 = colorsCount;
        colorsCount = i348 + 1;
        key_chat_secretTimeText = i348;
        int i349 = colorsCount;
        colorsCount = i349 + 1;
        key_chat_stickerNameText = i349;
        int i350 = colorsCount;
        colorsCount = i350 + 1;
        key_chat_botButtonText = i350;
        int i351 = colorsCount;
        colorsCount = i351 + 1;
        key_chat_inForwardedNameText = i351;
        int i352 = colorsCount;
        colorsCount = i352 + 1;
        key_chat_inPsaNameText = i352;
        int i353 = colorsCount;
        colorsCount = i353 + 1;
        key_chat_inViaBotNameText = i353;
        int i354 = colorsCount;
        colorsCount = i354 + 1;
        key_chat_stickerViaBotNameText = i354;
        int i355 = colorsCount;
        colorsCount = i355 + 1;
        key_chat_inQuote = i355;
        int i356 = colorsCount;
        colorsCount = i356 + 1;
        key_chat_outQuote = i356;
        int i357 = colorsCount;
        colorsCount = i357 + 1;
        key_chat_inReplyLine = i357;
        int i358 = colorsCount;
        colorsCount = i358 + 1;
        key_chat_stickerReplyLine = i358;
        int i359 = colorsCount;
        colorsCount = i359 + 1;
        key_chat_inReplyNameText = i359;
        int i360 = colorsCount;
        colorsCount = i360 + 1;
        key_chat_stickerReplyNameText = i360;
        int i361 = colorsCount;
        colorsCount = i361 + 1;
        key_chat_inReplyMessageText = i361;
        int i362 = colorsCount;
        colorsCount = i362 + 1;
        key_chat_inReplyMediaMessageText = i362;
        int i363 = colorsCount;
        colorsCount = i363 + 1;
        key_chat_inReplyMediaMessageSelectedText = i363;
        int i364 = colorsCount;
        colorsCount = i364 + 1;
        key_chat_stickerReplyMessageText = i364;
        int i365 = colorsCount;
        colorsCount = i365 + 1;
        key_chat_inPreviewLine = i365;
        int i366 = colorsCount;
        colorsCount = i366 + 1;
        key_chat_inSiteNameText = i366;
        int i367 = colorsCount;
        colorsCount = i367 + 1;
        key_chat_inContactNameText = i367;
        int i368 = colorsCount;
        colorsCount = i368 + 1;
        key_chat_inContactPhoneText = i368;
        int i369 = colorsCount;
        colorsCount = i369 + 1;
        key_chat_inContactPhoneSelectedText = i369;
        int i370 = colorsCount;
        colorsCount = i370 + 1;
        key_chat_mediaProgress = i370;
        int i371 = colorsCount;
        colorsCount = i371 + 1;
        key_chat_inAudioProgress = i371;
        int i372 = colorsCount;
        colorsCount = i372 + 1;
        key_chat_inAudioSelectedProgress = i372;
        int i373 = colorsCount;
        colorsCount = i373 + 1;
        key_chat_mediaTimeText = i373;
        int i374 = colorsCount;
        colorsCount = i374 + 1;
        key_chat_inAdminText = i374;
        int i375 = colorsCount;
        colorsCount = i375 + 1;
        key_chat_inAdminSelectedText = i375;
        int i376 = colorsCount;
        colorsCount = i376 + 1;
        key_chat_inTimeText = i376;
        int i377 = colorsCount;
        colorsCount = i377 + 1;
        key_chat_inTimeSelectedText = i377;
        int i378 = colorsCount;
        colorsCount = i378 + 1;
        key_chat_inAudioPerformerText = i378;
        int i379 = colorsCount;
        colorsCount = i379 + 1;
        key_chat_inAudioPerformerSelectedText = i379;
        int i380 = colorsCount;
        colorsCount = i380 + 1;
        key_chat_inAudioTitleText = i380;
        int i381 = colorsCount;
        colorsCount = i381 + 1;
        key_chat_inAudioDurationText = i381;
        int i382 = colorsCount;
        colorsCount = i382 + 1;
        key_chat_inAudioDurationSelectedText = i382;
        int i383 = colorsCount;
        colorsCount = i383 + 1;
        key_chat_inAudioSeekbar = i383;
        int i384 = colorsCount;
        colorsCount = i384 + 1;
        key_chat_inAudioCacheSeekbar = i384;
        int i385 = colorsCount;
        colorsCount = i385 + 1;
        key_chat_inAudioSeekbarSelected = i385;
        int i386 = colorsCount;
        colorsCount = i386 + 1;
        key_chat_inAudioSeekbarFill = i386;
        int i387 = colorsCount;
        colorsCount = i387 + 1;
        key_chat_inVoiceSeekbar = i387;
        int i388 = colorsCount;
        colorsCount = i388 + 1;
        key_chat_inVoiceSeekbarSelected = i388;
        int i389 = colorsCount;
        colorsCount = i389 + 1;
        key_chat_inVoiceSeekbarFill = i389;
        int i390 = colorsCount;
        colorsCount = i390 + 1;
        key_chat_inFileProgress = i390;
        int i391 = colorsCount;
        colorsCount = i391 + 1;
        key_chat_inFileProgressSelected = i391;
        int i392 = colorsCount;
        colorsCount = i392 + 1;
        key_chat_inFileNameText = i392;
        int i393 = colorsCount;
        colorsCount = i393 + 1;
        key_chat_inFileInfoText = i393;
        int i394 = colorsCount;
        colorsCount = i394 + 1;
        key_chat_inFileInfoSelectedText = i394;
        int i395 = colorsCount;
        colorsCount = i395 + 1;
        key_chat_inFileBackground = i395;
        int i396 = colorsCount;
        colorsCount = i396 + 1;
        key_chat_inFileBackgroundSelected = i396;
        int i397 = colorsCount;
        colorsCount = i397 + 1;
        key_chat_inVenueInfoText = i397;
        int i398 = colorsCount;
        colorsCount = i398 + 1;
        key_chat_inVenueInfoSelectedText = i398;
        int i399 = colorsCount;
        colorsCount = i399 + 1;
        key_chat_mediaInfoText = i399;
        int i400 = colorsCount;
        colorsCount = i400 + 1;
        key_chat_linkSelectBackground = i400;
        int i401 = colorsCount;
        colorsCount = i401 + 1;
        key_chat_textSelectBackground = i401;
        int i402 = colorsCount;
        colorsCount = i402 + 1;
        key_chat_wallpaper = i402;
        int i403 = colorsCount;
        colorsCount = i403 + 1;
        key_chat_wallpaper_gradient_to1 = i403;
        int i404 = colorsCount;
        colorsCount = i404 + 1;
        key_chat_wallpaper_gradient_to2 = i404;
        int i405 = colorsCount;
        colorsCount = i405 + 1;
        key_chat_wallpaper_gradient_to3 = i405;
        int i406 = colorsCount;
        colorsCount = i406 + 1;
        key_chat_wallpaper_gradient_rotation = i406;
        int i407 = colorsCount;
        colorsCount = i407 + 1;
        key_chat_messagePanelBackground = i407;
        int i408 = colorsCount;
        colorsCount = i408 + 1;
        key_chat_messagePanelShadow = i408;
        int i409 = colorsCount;
        colorsCount = i409 + 1;
        key_chat_messagePanelText = i409;
        int i410 = colorsCount;
        colorsCount = i410 + 1;
        key_chat_messagePanelHint = i410;
        int i411 = colorsCount;
        colorsCount = i411 + 1;
        key_chat_messagePanelCursor = i411;
        int i412 = colorsCount;
        colorsCount = i412 + 1;
        key_chat_messagePanelIcons = i412;
        int i413 = colorsCount;
        colorsCount = i413 + 1;
        key_chat_messagePanelSend = i413;
        int i414 = colorsCount;
        colorsCount = i414 + 1;
        key_chat_messagePanelVoiceLock = i414;
        int i415 = colorsCount;
        colorsCount = i415 + 1;
        key_chat_messagePanelVoiceLockBackground = i415;
        int i416 = colorsCount;
        colorsCount = i416 + 1;
        key_chat_messagePanelVoiceLockShadow = i416;
        int i417 = colorsCount;
        colorsCount = i417 + 1;
        key_chat_topPanelBackground = i417;
        int i418 = colorsCount;
        colorsCount = i418 + 1;
        key_chat_topPanelClose = i418;
        int i419 = colorsCount;
        colorsCount = i419 + 1;
        key_chat_topPanelLine = i419;
        int i420 = colorsCount;
        colorsCount = i420 + 1;
        key_chat_topPanelTitle = i420;
        int i421 = colorsCount;
        colorsCount = i421 + 1;
        key_chat_topPanelMessage = i421;
        int i422 = colorsCount;
        colorsCount = i422 + 1;
        key_chat_addContact = i422;
        int i423 = colorsCount;
        colorsCount = i423 + 1;
        key_chat_inLoader = i423;
        int i424 = colorsCount;
        colorsCount = i424 + 1;
        key_chat_inLoaderSelected = i424;
        int i425 = colorsCount;
        colorsCount = i425 + 1;
        key_chat_inLoaderPhoto = i425;
        int i426 = colorsCount;
        colorsCount = i426 + 1;
        key_chat_mediaLoaderPhoto = i426;
        int i427 = colorsCount;
        colorsCount = i427 + 1;
        key_chat_mediaLoaderPhotoSelected = i427;
        int i428 = colorsCount;
        colorsCount = i428 + 1;
        key_chat_mediaLoaderPhotoIcon = i428;
        int i429 = colorsCount;
        colorsCount = i429 + 1;
        key_chat_mediaLoaderPhotoIconSelected = i429;
        int i430 = colorsCount;
        colorsCount = i430 + 1;
        key_chat_inLocationBackground = i430;
        int i431 = colorsCount;
        colorsCount = i431 + 1;
        key_chat_inLocationIcon = i431;
        int i432 = colorsCount;
        colorsCount = i432 + 1;
        key_chat_inContactBackground = i432;
        int i433 = colorsCount;
        colorsCount = i433 + 1;
        key_chat_inContactIcon = i433;
        int i434 = colorsCount;
        colorsCount = i434 + 1;
        key_chat_replyPanelIcons = i434;
        int i435 = colorsCount;
        colorsCount = i435 + 1;
        key_chat_replyPanelClose = i435;
        int i436 = colorsCount;
        colorsCount = i436 + 1;
        key_chat_replyPanelName = i436;
        int i437 = colorsCount;
        colorsCount = i437 + 1;
        key_chat_replyPanelLine = i437;
        int i438 = colorsCount;
        colorsCount = i438 + 1;
        key_chat_searchPanelIcons = i438;
        int i439 = colorsCount;
        colorsCount = i439 + 1;
        key_chat_searchPanelText = i439;
        int i440 = colorsCount;
        colorsCount = i440 + 1;
        key_chat_secretChatStatusText = i440;
        int i441 = colorsCount;
        colorsCount = i441 + 1;
        key_chat_fieldOverlayText = i441;
        int i442 = colorsCount;
        colorsCount = i442 + 1;
        key_chat_stickersHintPanel = i442;
        int i443 = colorsCount;
        colorsCount = i443 + 1;
        key_chat_botSwitchToInlineText = i443;
        int i444 = colorsCount;
        colorsCount = i444 + 1;
        key_chat_unreadMessagesStartArrowIcon = i444;
        int i445 = colorsCount;
        colorsCount = i445 + 1;
        key_chat_unreadMessagesStartText = i445;
        int i446 = colorsCount;
        colorsCount = i446 + 1;
        key_chat_unreadMessagesStartBackground = i446;
        int i447 = colorsCount;
        colorsCount = i447 + 1;
        key_chat_inlineResultIcon = i447;
        int i448 = colorsCount;
        colorsCount = i448 + 1;
        key_chat_emojiPanelBackground = i448;
        int i449 = colorsCount;
        colorsCount = i449 + 1;
        key_chat_emojiSearchBackground = i449;
        int i450 = colorsCount;
        colorsCount = i450 + 1;
        key_chat_emojiSearchIcon = i450;
        int i451 = colorsCount;
        colorsCount = i451 + 1;
        key_chat_emojiPanelShadowLine = i451;
        int i452 = colorsCount;
        colorsCount = i452 + 1;
        key_chat_emojiPanelEmptyText = i452;
        int i453 = colorsCount;
        colorsCount = i453 + 1;
        key_chat_emojiPanelIcon = i453;
        int i454 = colorsCount;
        colorsCount = i454 + 1;
        key_chat_emojiBottomPanelIcon = i454;
        int i455 = colorsCount;
        colorsCount = i455 + 1;
        key_chat_emojiPanelIconSelected = i455;
        int i456 = colorsCount;
        colorsCount = i456 + 1;
        key_chat_emojiPanelStickerPackSelector = i456;
        int i457 = colorsCount;
        colorsCount = i457 + 1;
        key_chat_emojiPanelStickerPackSelectorLine = i457;
        int i458 = colorsCount;
        colorsCount = i458 + 1;
        key_chat_emojiPanelBackspace = i458;
        int i459 = colorsCount;
        colorsCount = i459 + 1;
        key_chat_emojiPanelTrendingTitle = i459;
        int i460 = colorsCount;
        colorsCount = i460 + 1;
        key_chat_emojiPanelStickerSetName = i460;
        int i461 = colorsCount;
        colorsCount = i461 + 1;
        key_chat_emojiPanelStickerSetNameHighlight = i461;
        int i462 = colorsCount;
        colorsCount = i462 + 1;
        key_chat_emojiPanelStickerSetNameIcon = i462;
        int i463 = colorsCount;
        colorsCount = i463 + 1;
        key_chat_emojiPanelTrendingDescription = i463;
        int i464 = colorsCount;
        colorsCount = i464 + 1;
        key_chat_botKeyboardButtonText = i464;
        int i465 = colorsCount;
        colorsCount = i465 + 1;
        key_chat_botKeyboardButtonBackground = i465;
        int i466 = colorsCount;
        colorsCount = i466 + 1;
        key_chat_botKeyboardButtonBackgroundPressed = i466;
        int i467 = colorsCount;
        colorsCount = i467 + 1;
        key_chat_emojiPanelNewTrending = i467;
        int i468 = colorsCount;
        colorsCount = i468 + 1;
        key_chat_messagePanelVoicePressed = i468;
        int i469 = colorsCount;
        colorsCount = i469 + 1;
        key_chat_messagePanelVoiceBackground = i469;
        int i470 = colorsCount;
        colorsCount = i470 + 1;
        key_chat_messagePanelVoiceDelete = i470;
        int i471 = colorsCount;
        colorsCount = i471 + 1;
        key_chat_messagePanelVoiceDuration = i471;
        int i472 = colorsCount;
        colorsCount = i472 + 1;
        key_chat_recordedVoicePlayPause = i472;
        int i473 = colorsCount;
        colorsCount = i473 + 1;
        key_chat_recordedVoiceProgress = i473;
        int i474 = colorsCount;
        colorsCount = i474 + 1;
        key_chat_recordedVoiceProgressInner = i474;
        int i475 = colorsCount;
        colorsCount = i475 + 1;
        key_chat_recordedVoiceDot = i475;
        int i476 = colorsCount;
        colorsCount = i476 + 1;
        key_chat_recordedVoiceBackground = i476;
        int i477 = colorsCount;
        colorsCount = i477 + 1;
        key_chat_recordVoiceCancel = i477;
        int i478 = colorsCount;
        colorsCount = i478 + 1;
        key_chat_recordTime = i478;
        int i479 = colorsCount;
        colorsCount = i479 + 1;
        key_chat_messagePanelCancelInlineBot = i479;
        int i480 = colorsCount;
        colorsCount = i480 + 1;
        key_chat_gifSaveHintText = i480;
        int i481 = colorsCount;
        colorsCount = i481 + 1;
        key_chat_gifSaveHintBackground = i481;
        int i482 = colorsCount;
        colorsCount = i482 + 1;
        key_chat_goDownButton = i482;
        int i483 = colorsCount;
        colorsCount = i483 + 1;
        key_chat_goDownButtonIcon = i483;
        int i484 = colorsCount;
        colorsCount = i484 + 1;
        key_chat_goDownButtonCounter = i484;
        int i485 = colorsCount;
        colorsCount = i485 + 1;
        key_chat_goDownButtonCounterBackground = i485;
        int i486 = colorsCount;
        colorsCount = i486 + 1;
        key_chat_inTextSelectionHighlight = i486;
        int i487 = colorsCount;
        colorsCount = i487 + 1;
        key_chat_TextSelectionCursor = i487;
        int i488 = colorsCount;
        colorsCount = i488 + 1;
        key_chat_inBubbleLocationPlaceholder = i488;
        int i489 = colorsCount;
        colorsCount = i489 + 1;
        key_chat_BlurAlpha = i489;
        int i490 = colorsCount;
        colorsCount = i490 + 1;
        key_chat_BlurAlphaSlow = i490;
        int i491 = colorsCount;
        colorsCount = i491 + 1;
        key_chat_editMediaButton = i491;
        int i492 = colorsCount;
        colorsCount = i492 + 1;
        key_voipgroup_listSelector = i492;
        int i493 = colorsCount;
        colorsCount = i493 + 1;
        key_voipgroup_inviteMembersBackground = i493;
        int i494 = colorsCount;
        colorsCount = i494 + 1;
        key_voipgroup_actionBar = i494;
        int i495 = colorsCount;
        colorsCount = i495 + 1;
        key_voipgroup_actionBarItems = i495;
        int i496 = colorsCount;
        colorsCount = i496 + 1;
        key_voipgroup_actionBarItemsSelector = i496;
        int i497 = colorsCount;
        colorsCount = i497 + 1;
        key_voipgroup_actionBarUnscrolled = i497;
        int i498 = colorsCount;
        colorsCount = i498 + 1;
        key_voipgroup_listViewBackgroundUnscrolled = i498;
        int i499 = colorsCount;
        colorsCount = i499 + 1;
        key_voipgroup_lastSeenTextUnscrolled = i499;
        int i500 = colorsCount;
        colorsCount = i500 + 1;
        key_voipgroup_mutedIconUnscrolled = i500;
        int i501 = colorsCount;
        colorsCount = i501 + 1;
        key_voipgroup_nameText = i501;
        int i502 = colorsCount;
        colorsCount = i502 + 1;
        key_voipgroup_lastSeenText = i502;
        int i503 = colorsCount;
        colorsCount = i503 + 1;
        key_voipgroup_listeningText = i503;
        int i504 = colorsCount;
        colorsCount = i504 + 1;
        key_voipgroup_speakingText = i504;
        int i505 = colorsCount;
        colorsCount = i505 + 1;
        key_voipgroup_mutedIcon = i505;
        int i506 = colorsCount;
        colorsCount = i506 + 1;
        key_voipgroup_mutedByAdminIcon = i506;
        int i507 = colorsCount;
        colorsCount = i507 + 1;
        key_voipgroup_listViewBackground = i507;
        int i508 = colorsCount;
        colorsCount = i508 + 1;
        key_voipgroup_dialogBackground = i508;
        int i509 = colorsCount;
        colorsCount = i509 + 1;
        key_voipgroup_leaveCallMenu = i509;
        int i510 = colorsCount;
        colorsCount = i510 + 1;
        key_voipgroup_checkMenu = i510;
        int i511 = colorsCount;
        colorsCount = i511 + 1;
        key_voipgroup_soundButton = i511;
        int i512 = colorsCount;
        colorsCount = i512 + 1;
        key_voipgroup_soundButtonActive = i512;
        int i513 = colorsCount;
        colorsCount = i513 + 1;
        key_voipgroup_soundButtonActiveScrolled = i513;
        int i514 = colorsCount;
        colorsCount = i514 + 1;
        key_voipgroup_soundButton2 = i514;
        int i515 = colorsCount;
        colorsCount = i515 + 1;
        key_voipgroup_soundButtonActive2 = i515;
        int i516 = colorsCount;
        colorsCount = i516 + 1;
        key_voipgroup_soundButtonActive2Scrolled = i516;
        int i517 = colorsCount;
        colorsCount = i517 + 1;
        key_voipgroup_leaveButton = i517;
        int i518 = colorsCount;
        colorsCount = i518 + 1;
        key_voipgroup_leaveButtonScrolled = i518;
        int i519 = colorsCount;
        colorsCount = i519 + 1;
        key_voipgroup_muteButton = i519;
        int i520 = colorsCount;
        colorsCount = i520 + 1;
        key_voipgroup_muteButton2 = i520;
        int i521 = colorsCount;
        colorsCount = i521 + 1;
        key_voipgroup_muteButton3 = i521;
        int i522 = colorsCount;
        colorsCount = i522 + 1;
        key_voipgroup_unmuteButton = i522;
        int i523 = colorsCount;
        colorsCount = i523 + 1;
        key_voipgroup_unmuteButton2 = i523;
        int i524 = colorsCount;
        colorsCount = i524 + 1;
        key_voipgroup_disabledButton = i524;
        int i525 = colorsCount;
        colorsCount = i525 + 1;
        key_voipgroup_rtmpButton = i525;
        int i526 = colorsCount;
        colorsCount = i526 + 1;
        key_voipgroup_disabledButtonActive = i526;
        int i527 = colorsCount;
        colorsCount = i527 + 1;
        key_voipgroup_disabledButtonActiveScrolled = i527;
        int i528 = colorsCount;
        colorsCount = i528 + 1;
        key_voipgroup_connectingProgress = i528;
        int i529 = colorsCount;
        colorsCount = i529 + 1;
        key_voipgroup_scrollUp = i529;
        int i530 = colorsCount;
        colorsCount = i530 + 1;
        key_voipgroup_searchPlaceholder = i530;
        int i531 = colorsCount;
        colorsCount = i531 + 1;
        key_voipgroup_searchBackground = i531;
        int i532 = colorsCount;
        colorsCount = i532 + 1;
        key_voipgroup_searchText = i532;
        int i533 = colorsCount;
        colorsCount = i533 + 1;
        key_voipgroup_overlayGreen1 = i533;
        int i534 = colorsCount;
        colorsCount = i534 + 1;
        key_voipgroup_overlayGreen2 = i534;
        int i535 = colorsCount;
        colorsCount = i535 + 1;
        key_voipgroup_overlayBlue1 = i535;
        int i536 = colorsCount;
        colorsCount = i536 + 1;
        key_voipgroup_overlayBlue2 = i536;
        int i537 = colorsCount;
        colorsCount = i537 + 1;
        key_voipgroup_topPanelGreen1 = i537;
        int i538 = colorsCount;
        colorsCount = i538 + 1;
        key_voipgroup_topPanelGreen2 = i538;
        int i539 = colorsCount;
        colorsCount = i539 + 1;
        key_voipgroup_topPanelBlue1 = i539;
        int i540 = colorsCount;
        colorsCount = i540 + 1;
        key_voipgroup_topPanelBlue2 = i540;
        int i541 = colorsCount;
        colorsCount = i541 + 1;
        key_voipgroup_topPanelGray = i541;
        int i542 = colorsCount;
        colorsCount = i542 + 1;
        key_voipgroup_overlayAlertGradientMuted = i542;
        int i543 = colorsCount;
        colorsCount = i543 + 1;
        key_voipgroup_overlayAlertGradientMuted2 = i543;
        int i544 = colorsCount;
        colorsCount = i544 + 1;
        key_voipgroup_overlayAlertGradientUnmuted = i544;
        int i545 = colorsCount;
        colorsCount = i545 + 1;
        key_voipgroup_overlayAlertGradientUnmuted2 = i545;
        int i546 = colorsCount;
        colorsCount = i546 + 1;
        key_voipgroup_overlayAlertMutedByAdmin = i546;
        int i547 = colorsCount;
        colorsCount = i547 + 1;
        key_voipgroup_overlayAlertMutedByAdmin2 = i547;
        int i548 = colorsCount;
        colorsCount = i548 + 1;
        key_voipgroup_mutedByAdminGradient = i548;
        int i549 = colorsCount;
        colorsCount = i549 + 1;
        key_voipgroup_mutedByAdminGradient2 = i549;
        int i550 = colorsCount;
        colorsCount = i550 + 1;
        key_voipgroup_mutedByAdminGradient3 = i550;
        int i551 = colorsCount;
        colorsCount = i551 + 1;
        key_voipgroup_mutedByAdminMuteButton = i551;
        int i552 = colorsCount;
        colorsCount = i552 + 1;
        key_voipgroup_mutedByAdminMuteButtonDisabled = i552;
        int i553 = colorsCount;
        colorsCount = i553 + 1;
        key_voipgroup_windowBackgroundWhiteInputField = i553;
        int i554 = colorsCount;
        colorsCount = i554 + 1;
        key_voipgroup_windowBackgroundWhiteInputFieldActivated = i554;
        int i555 = colorsCount;
        colorsCount = i555 + 1;
        key_table_background = i555;
        int i556 = colorsCount;
        colorsCount = i556 + 1;
        key_table_border = i556;
        int i557 = colorsCount;
        colorsCount = i557 + 1;
        key_passport_authorizeBackground = i557;
        int i558 = colorsCount;
        colorsCount = i558 + 1;
        key_passport_authorizeBackgroundSelected = i558;
        int i559 = colorsCount;
        colorsCount = i559 + 1;
        key_passport_authorizeText = i559;
        int i560 = colorsCount;
        colorsCount = i560 + 1;
        key_profile_creatorIcon = i560;
        int i561 = colorsCount;
        colorsCount = i561 + 1;
        key_profile_title = i561;
        int i562 = colorsCount;
        colorsCount = i562 + 1;
        key_profile_actionIcon = i562;
        int i563 = colorsCount;
        colorsCount = i563 + 1;
        key_profile_actionBackground = i563;
        int i564 = colorsCount;
        colorsCount = i564 + 1;
        key_profile_actionPressedBackground = i564;
        int i565 = colorsCount;
        colorsCount = i565 + 1;
        key_profile_verifiedBackground = i565;
        int i566 = colorsCount;
        colorsCount = i566 + 1;
        key_profile_verifiedCheck = i566;
        int i567 = colorsCount;
        colorsCount = i567 + 1;
        key_profile_status = i567;
        int i568 = colorsCount;
        colorsCount = i568 + 1;
        key_profile_tabText = i568;
        int i569 = colorsCount;
        colorsCount = i569 + 1;
        key_profile_tabSelectedText = i569;
        int i570 = colorsCount;
        colorsCount = i570 + 1;
        key_profile_tabSelectedLine = i570;
        int i571 = colorsCount;
        colorsCount = i571 + 1;
        key_profile_tabSelector = i571;
        int i572 = colorsCount;
        colorsCount = i572 + 1;
        key_sharedMedia_startStopLoadIcon = i572;
        int i573 = colorsCount;
        colorsCount = i573 + 1;
        key_sharedMedia_linkPlaceholder = i573;
        int i574 = colorsCount;
        colorsCount = i574 + 1;
        key_sharedMedia_linkPlaceholderText = i574;
        int i575 = colorsCount;
        colorsCount = i575 + 1;
        key_sharedMedia_photoPlaceholder = i575;
        int i576 = colorsCount;
        colorsCount = i576 + 1;
        key_featuredStickers_addedIcon = i576;
        int i577 = colorsCount;
        colorsCount = i577 + 1;
        key_featuredStickers_buttonProgress = i577;
        int i578 = colorsCount;
        colorsCount = i578 + 1;
        key_featuredStickers_addButton = i578;
        int i579 = colorsCount;
        colorsCount = i579 + 1;
        key_featuredStickers_addButtonPressed = i579;
        int i580 = colorsCount;
        colorsCount = i580 + 1;
        key_featuredStickers_removeButtonText = i580;
        int i581 = colorsCount;
        colorsCount = i581 + 1;
        key_featuredStickers_buttonText = i581;
        int i582 = colorsCount;
        colorsCount = i582 + 1;
        key_featuredStickers_unread = i582;
        int i583 = colorsCount;
        colorsCount = i583 + 1;
        key_stickers_menu = i583;
        int i584 = colorsCount;
        colorsCount = i584 + 1;
        key_stickers_menuSelector = i584;
        int i585 = colorsCount;
        colorsCount = i585 + 1;
        key_changephoneinfo_image2 = i585;
        int i586 = colorsCount;
        colorsCount = i586 + 1;
        key_groupcreate_hintText = i586;
        int i587 = colorsCount;
        colorsCount = i587 + 1;
        key_groupcreate_cursor = i587;
        int i588 = colorsCount;
        colorsCount = i588 + 1;
        key_groupcreate_sectionShadow = i588;
        int i589 = colorsCount;
        colorsCount = i589 + 1;
        key_groupcreate_sectionText = i589;
        int i590 = colorsCount;
        colorsCount = i590 + 1;
        key_groupcreate_spanText = i590;
        int i591 = colorsCount;
        colorsCount = i591 + 1;
        key_groupcreate_spanBackground = i591;
        int i592 = colorsCount;
        colorsCount = i592 + 1;
        key_groupcreate_spanDelete = i592;
        int i593 = colorsCount;
        colorsCount = i593 + 1;
        key_contacts_inviteBackground = i593;
        int i594 = colorsCount;
        colorsCount = i594 + 1;
        key_contacts_inviteText = i594;
        int i595 = colorsCount;
        colorsCount = i595 + 1;
        key_login_progressInner = i595;
        int i596 = colorsCount;
        colorsCount = i596 + 1;
        key_login_progressOuter = i596;
        int i597 = colorsCount;
        colorsCount = i597 + 1;
        key_picker_enabledButton = i597;
        int i598 = colorsCount;
        colorsCount = i598 + 1;
        key_picker_disabledButton = i598;
        int i599 = colorsCount;
        colorsCount = i599 + 1;
        key_picker_badge = i599;
        int i600 = colorsCount;
        colorsCount = i600 + 1;
        key_picker_badgeText = i600;
        int i601 = colorsCount;
        colorsCount = i601 + 1;
        key_location_sendLocationBackground = i601;
        int i602 = colorsCount;
        colorsCount = i602 + 1;
        key_location_sendLocationIcon = i602;
        int i603 = colorsCount;
        colorsCount = i603 + 1;
        key_location_sendLocationText = i603;
        int i604 = colorsCount;
        colorsCount = i604 + 1;
        key_location_sendLiveLocationBackground = i604;
        int i605 = colorsCount;
        colorsCount = i605 + 1;
        key_location_sendLiveLocationIcon = i605;
        int i606 = colorsCount;
        colorsCount = i606 + 1;
        key_location_sendLiveLocationText = i606;
        int i607 = colorsCount;
        colorsCount = i607 + 1;
        key_location_liveLocationProgress = i607;
        int i608 = colorsCount;
        colorsCount = i608 + 1;
        key_location_placeLocationBackground = i608;
        int i609 = colorsCount;
        colorsCount = i609 + 1;
        key_location_actionIcon = i609;
        int i610 = colorsCount;
        colorsCount = i610 + 1;
        key_location_actionActiveIcon = i610;
        int i611 = colorsCount;
        colorsCount = i611 + 1;
        key_location_actionBackground = i611;
        int i612 = colorsCount;
        colorsCount = i612 + 1;
        key_location_actionPressedBackground = i612;
        int i613 = colorsCount;
        colorsCount = i613 + 1;
        key_dialog_liveLocationProgress = i613;
        int i614 = colorsCount;
        colorsCount = i614 + 1;
        key_files_folderIcon = i614;
        int i615 = colorsCount;
        colorsCount = i615 + 1;
        key_files_folderIconBackground = i615;
        int i616 = colorsCount;
        colorsCount = i616 + 1;
        key_files_iconText = i616;
        int i617 = colorsCount;
        colorsCount = i617 + 1;
        key_sessions_devicesImage = i617;
        int i618 = colorsCount;
        colorsCount = i618 + 1;
        key_calls_callReceivedGreenIcon = i618;
        int i619 = colorsCount;
        colorsCount = i619 + 1;
        key_calls_callReceivedRedIcon = i619;
        int i620 = colorsCount;
        colorsCount = i620 + 1;
        key_undo_background = i620;
        int i621 = colorsCount;
        colorsCount = i621 + 1;
        key_undo_cancelColor = i621;
        int i622 = colorsCount;
        colorsCount = i622 + 1;
        key_undo_infoColor = i622;
        int i623 = colorsCount;
        colorsCount = i623 + 1;
        key_sheet_scrollUp = i623;
        int i624 = colorsCount;
        colorsCount = i624 + 1;
        key_sheet_other = i624;
        int i625 = colorsCount;
        colorsCount = i625 + 1;
        key_player_actionBarSelector = i625;
        int i626 = colorsCount;
        colorsCount = i626 + 1;
        key_player_actionBarTitle = i626;
        int i627 = colorsCount;
        colorsCount = i627 + 1;
        key_player_actionBarSubtitle = i627;
        int i628 = colorsCount;
        colorsCount = i628 + 1;
        key_player_actionBarItems = i628;
        int i629 = colorsCount;
        colorsCount = i629 + 1;
        key_player_background = i629;
        int i630 = colorsCount;
        colorsCount = i630 + 1;
        key_player_time = i630;
        int i631 = colorsCount;
        colorsCount = i631 + 1;
        key_player_progressBackground = i631;
        int i632 = colorsCount;
        colorsCount = i632 + 1;
        key_player_progressCachedBackground = i632;
        int i633 = colorsCount;
        colorsCount = i633 + 1;
        key_player_progress = i633;
        int i634 = colorsCount;
        colorsCount = i634 + 1;
        key_player_button = i634;
        int i635 = colorsCount;
        colorsCount = i635 + 1;
        key_player_buttonActive = i635;
        int i636 = colorsCount;
        colorsCount = i636 + 1;
        key_statisticChartSignature = i636;
        int i637 = colorsCount;
        colorsCount = i637 + 1;
        key_statisticChartSignatureAlpha = i637;
        int i638 = colorsCount;
        colorsCount = i638 + 1;
        key_statisticChartHintLine = i638;
        int i639 = colorsCount;
        colorsCount = i639 + 1;
        key_statisticChartActiveLine = i639;
        int i640 = colorsCount;
        colorsCount = i640 + 1;
        key_statisticChartInactivePickerChart = i640;
        int i641 = colorsCount;
        colorsCount = i641 + 1;
        key_statisticChartActivePickerChart = i641;
        int i642 = colorsCount;
        colorsCount = i642 + 1;
        key_statisticChartRipple = i642;
        int i643 = colorsCount;
        colorsCount = i643 + 1;
        key_statisticChartBackZoomColor = i643;
        int i644 = colorsCount;
        colorsCount = i644 + 1;
        key_statisticChartChevronColor = i644;
        int i645 = colorsCount;
        colorsCount = i645 + 1;
        key_statisticChartLine_blue = i645;
        int i646 = colorsCount;
        colorsCount = i646 + 1;
        key_statisticChartLine_green = i646;
        int i647 = colorsCount;
        colorsCount = i647 + 1;
        key_statisticChartLine_red = i647;
        int i648 = colorsCount;
        colorsCount = i648 + 1;
        key_statisticChartLine_golden = i648;
        int i649 = colorsCount;
        colorsCount = i649 + 1;
        key_statisticChartLine_lightblue = i649;
        int i650 = colorsCount;
        colorsCount = i650 + 1;
        key_statisticChartLine_lightgreen = i650;
        int i651 = colorsCount;
        colorsCount = i651 + 1;
        key_statisticChartLine_orange = i651;
        int i652 = colorsCount;
        colorsCount = i652 + 1;
        key_statisticChartLine_indigo = i652;
        int i653 = colorsCount;
        colorsCount = i653 + 1;
        key_statisticChartLine_purple = i653;
        int i654 = colorsCount;
        colorsCount = i654 + 1;
        key_statisticChartLine_cyan = i654;
        int i655 = colorsCount;
        colorsCount = i655 + 1;
        key_statisticChartLineEmpty = i655;
        int i656 = colorsCount;
        colorsCount = i656 + 1;
        key_color_lightblue = i656;
        int i657 = colorsCount;
        colorsCount = i657 + 1;
        key_color_blue = i657;
        int i658 = colorsCount;
        colorsCount = i658 + 1;
        key_color_green = i658;
        int i659 = colorsCount;
        colorsCount = i659 + 1;
        key_color_lightgreen = i659;
        int i660 = colorsCount;
        colorsCount = i660 + 1;
        key_color_red = i660;
        int i661 = colorsCount;
        colorsCount = i661 + 1;
        key_color_orange = i661;
        int i662 = colorsCount;
        colorsCount = i662 + 1;
        key_color_yellow = i662;
        int i663 = colorsCount;
        colorsCount = i663 + 1;
        key_color_purple = i663;
        int i664 = colorsCount;
        colorsCount = i664 + 1;
        key_color_cyan = i664;
        keys_colors = new int[]{i656, i657, i658, i659, i660, i661, i662, i663, i664};
        int i665 = colorsCount;
        colorsCount = i665 + 1;
        key_chat_inReactionButtonBackground = i665;
        int i666 = colorsCount;
        colorsCount = i666 + 1;
        key_chat_outReactionButtonText = i666;
        int i667 = colorsCount;
        colorsCount = i667 + 1;
        key_chat_inReactionButtonText = i667;
        int i668 = colorsCount;
        colorsCount = i668 + 1;
        key_chat_inReactionButtonTextSelected = i668;
        int i669 = colorsCount;
        colorsCount = i669 + 1;
        key_chat_outReactionButtonTextSelected = i669;
        int i670 = colorsCount;
        colorsCount = i670 + 1;
        key_reactionStarSelector = i670;
        int i671 = colorsCount;
        colorsCount = i671 + 1;
        key_premiumGradient0 = i671;
        int i672 = colorsCount;
        colorsCount = i672 + 1;
        key_premiumGradient1 = i672;
        int i673 = colorsCount;
        colorsCount = i673 + 1;
        key_premiumGradient2 = i673;
        int i674 = colorsCount;
        colorsCount = i674 + 1;
        key_premiumGradient3 = i674;
        int i675 = colorsCount;
        colorsCount = i675 + 1;
        key_premiumGradient4 = i675;
        int i676 = colorsCount;
        colorsCount = i676 + 1;
        key_premiumGradientBackground1 = i676;
        int i677 = colorsCount;
        colorsCount = i677 + 1;
        key_premiumGradientBackground2 = i677;
        int i678 = colorsCount;
        colorsCount = i678 + 1;
        key_premiumGradientBackground3 = i678;
        int i679 = colorsCount;
        colorsCount = i679 + 1;
        key_premiumGradientBackground4 = i679;
        int i680 = colorsCount;
        colorsCount = i680 + 1;
        key_premiumGradientBackgroundOverlay = i680;
        int i681 = colorsCount;
        colorsCount = i681 + 1;
        key_premiumStartSmallStarsColor = i681;
        int i682 = colorsCount;
        colorsCount = i682 + 1;
        key_premiumStarGradient1 = i682;
        int i683 = colorsCount;
        colorsCount = i683 + 1;
        key_premiumStarGradient2 = i683;
        int i684 = colorsCount;
        colorsCount = i684 + 1;
        key_premiumCoinGradient1 = i684;
        int i685 = colorsCount;
        colorsCount = i685 + 1;
        key_premiumCoinGradient2 = i685;
        int i686 = colorsCount;
        colorsCount = i686 + 1;
        key_premiumStartSmallStarsColor2 = i686;
        int i687 = colorsCount;
        colorsCount = i687 + 1;
        key_premiumGradientBottomSheet1 = i687;
        int i688 = colorsCount;
        colorsCount = i688 + 1;
        key_premiumGradientBottomSheet2 = i688;
        int i689 = colorsCount;
        colorsCount = i689 + 1;
        key_premiumGradientBottomSheet3 = i689;
        int i690 = colorsCount;
        colorsCount = i690 + 1;
        key_topics_unreadCounter = i690;
        int i691 = colorsCount;
        colorsCount = i691 + 1;
        key_topics_unreadCounterMuted = i691;
        int i692 = colorsCount;
        colorsCount = i692 + 1;
        key_starsGradient1 = i692;
        int i693 = colorsCount;
        colorsCount = i693 + 1;
        key_starsGradient2 = i693;
        int i694 = colorsCount;
        colorsCount = i694 + 1;
        key_stories_circle1 = i694;
        int i695 = colorsCount;
        colorsCount = i695 + 1;
        key_stories_circle2 = i695;
        int i696 = colorsCount;
        colorsCount = i696 + 1;
        key_stories_circle_dialog1 = i696;
        int i697 = colorsCount;
        colorsCount = i697 + 1;
        key_stories_circle_dialog2 = i697;
        int i698 = colorsCount;
        colorsCount = i698 + 1;
        key_stories_circle_closeFriends1 = i698;
        int i699 = colorsCount;
        colorsCount = i699 + 1;
        key_stories_circle_closeFriends2 = i699;
        int i700 = colorsCount;
        colorsCount = i700 + 1;
        key_chat_inCodeBackground = i700;
        int i701 = colorsCount;
        colorsCount = i701 + 1;
        key_chat_outCodeBackground = i701;
        int i702 = colorsCount;
        colorsCount = i702 + 1;
        key_code_keyword = i702;
        int i703 = colorsCount;
        colorsCount = i703 + 1;
        key_code_operator = i703;
        int i704 = colorsCount;
        colorsCount = i704 + 1;
        key_code_constant = i704;
        int i705 = colorsCount;
        colorsCount = i705 + 1;
        key_code_string = i705;
        int i706 = colorsCount;
        colorsCount = i706 + 1;
        key_code_number = i706;
        int i707 = colorsCount;
        colorsCount = i707 + 1;
        key_code_comment = i707;
        int i708 = colorsCount;
        colorsCount = i708 + 1;
        key_code_function = i708;
        int i709 = colorsCount;
        colorsCount = i709 + 1;
        key_iv_background = i709;
        int i710 = colorsCount;
        colorsCount = i710 + 1;
        key_iv_backgroundGray = i710;
        int i711 = colorsCount;
        colorsCount = i711 + 1;
        key_iv_ab_progress = i711;
        int i712 = colorsCount;
        colorsCount = i712 + 1;
        key_iv_navigationBackground = i712;
        defaultChatDrawables = new HashMap();
        defaultChatDrawableColorKeys = new HashMap();
        defaultChatPaints = new HashMap();
        defaultChatPaintColors = new HashMap();
        fallbackKeys = new SparseIntArray();
        themeAccentExclusionKeys = new HashSet();
        hsvTemp1Local = new ThreadLocal();
        hsvTemp2Local = new ThreadLocal();
        hsvTemp3Local = new ThreadLocal();
        hsvTemp4Local = new ThreadLocal();
        hsvTemp5Local = new ThreadLocal();
        defaultColors = ThemeColors.createDefaultColors();
        fallbackKeys.put(i709, key_windowBackgroundWhite);
        SparseIntArray sparseIntArray = fallbackKeys;
        int i713 = key_windowBackgroundGray;
        sparseIntArray.put(i710, i713);
        fallbackKeys.put(i712, i713);
        SparseIntArray sparseIntArray2 = fallbackKeys;
        int i714 = key_featuredStickers_addButton;
        sparseIntArray2.put(i711, i714);
        fallbackKeys.put(key_dialogGiftsBackground, i713);
        SparseIntArray sparseIntArray3 = fallbackKeys;
        int i715 = key_dialogGiftsTabText;
        int i716 = key_windowBackgroundWhiteGrayText2;
        sparseIntArray3.put(i715, i716);
        fallbackKeys.put(key_chat_inQuote, key_featuredStickers_addButtonPressed);
        SparseIntArray sparseIntArray4 = fallbackKeys;
        int i717 = key_chat_outQuote;
        int i718 = key_chat_outReplyLine;
        sparseIntArray4.put(i717, i718);
        fallbackKeys.put(key_chat_outReplyLine2, i718);
        fallbackKeys.put(key_chat_inAdminText, key_chat_inTimeText);
        fallbackKeys.put(key_chat_inAdminSelectedText, key_chat_inTimeSelectedText);
        fallbackKeys.put(key_player_progressCachedBackground, key_player_progressBackground);
        fallbackKeys.put(key_chat_inAudioCacheSeekbar, key_chat_inAudioSeekbar);
        fallbackKeys.put(key_chat_outAudioCacheSeekbar, key_chat_outAudioSeekbar);
        fallbackKeys.put(key_chat_emojiSearchBackground, key_chat_emojiPanelStickerPackSelector);
        fallbackKeys.put(key_location_sendLiveLocationIcon, key_location_sendLocationIcon);
        fallbackKeys.put(key_changephoneinfo_image2, i714);
        fallbackKeys.put(key_graySectionText, i716);
        fallbackKeys.put(key_chat_inMediaIcon, key_chat_inBubble);
        fallbackKeys.put(key_chat_outMediaIcon, key_chat_outBubble);
        fallbackKeys.put(key_chat_inMediaIconSelected, key_chat_inBubbleSelected);
        fallbackKeys.put(key_chat_outMediaIconSelected, key_chat_outBubbleSelected);
        fallbackKeys.put(key_dialog_inlineProgressBackground, i713);
        fallbackKeys.put(key_dialog_inlineProgress, key_chats_menuItemIcon);
        fallbackKeys.put(key_groupcreate_spanDelete, key_chats_actionIcon);
        fallbackKeys.put(key_sharedMedia_photoPlaceholder, i713);
        fallbackKeys.put(key_chat_attachPollBackground, key_chat_attachAudioBackground);
        fallbackKeys.put(key_chats_onlineCircle, key_windowBackgroundWhiteBlueText);
        SparseIntArray sparseIntArray5 = fallbackKeys;
        int i719 = key_windowBackgroundWhiteBlueButton;
        int i720 = key_windowBackgroundWhiteValueText;
        sparseIntArray5.put(i719, i720);
        fallbackKeys.put(key_windowBackgroundWhiteBlueIcon, i720);
        fallbackKeys.put(key_undo_background, key_chat_gifSaveHintBackground);
        SparseIntArray sparseIntArray6 = fallbackKeys;
        int i721 = key_undo_cancelColor;
        int i722 = key_chat_gifSaveHintText;
        sparseIntArray6.put(i721, i722);
        fallbackKeys.put(key_undo_infoColor, i722);
        SparseIntArray sparseIntArray7 = fallbackKeys;
        int i723 = key_windowBackgroundUnchecked;
        int i724 = key_windowBackgroundWhite;
        sparseIntArray7.put(i723, i724);
        fallbackKeys.put(key_windowBackgroundChecked, i724);
        fallbackKeys.put(key_switchTrackBlue, key_switchTrack);
        fallbackKeys.put(key_switchTrackBlueChecked, key_switchTrackChecked);
        fallbackKeys.put(key_switchTrackBlueThumb, i724);
        fallbackKeys.put(key_switchTrackBlueThumbChecked, i724);
        fallbackKeys.put(key_windowBackgroundCheckText, i724);
        fallbackKeys.put(key_contextProgressInner4, key_contextProgressInner1);
        fallbackKeys.put(key_contextProgressOuter4, key_contextProgressOuter1);
        SparseIntArray sparseIntArray8 = fallbackKeys;
        int i725 = key_switchTrackBlueSelector;
        int i726 = key_listSelector;
        sparseIntArray8.put(i725, i726);
        fallbackKeys.put(key_switchTrackBlueSelectorChecked, i726);
        SparseIntArray sparseIntArray9 = fallbackKeys;
        int i727 = key_chat_emojiBottomPanelIcon;
        int i728 = key_chat_emojiPanelIcon;
        sparseIntArray9.put(i727, i728);
        fallbackKeys.put(key_chat_emojiSearchIcon, i728);
        fallbackKeys.put(key_chat_emojiPanelStickerSetNameHighlight, key_windowBackgroundWhiteBlueText4);
        fallbackKeys.put(key_chat_emojiPanelStickerPackSelectorLine, key_chat_emojiPanelIconSelected);
        SparseIntArray sparseIntArray10 = fallbackKeys;
        int i729 = key_sheet_scrollUp;
        int i730 = key_chat_emojiPanelStickerPackSelector;
        sparseIntArray10.put(i729, i730);
        fallbackKeys.put(key_sheet_other, key_player_actionBarItems);
        fallbackKeys.put(key_dialogSearchBackground, i730);
        fallbackKeys.put(key_dialogSearchHint, i728);
        fallbackKeys.put(key_dialogSearchIcon, i728);
        fallbackKeys.put(key_dialogSearchText, key_windowBackgroundWhiteBlackText);
        SparseIntArray sparseIntArray11 = fallbackKeys;
        int i731 = key_dialogFloatingButton;
        int i732 = key_dialogRoundCheckBox;
        sparseIntArray11.put(i731, i732);
        fallbackKeys.put(key_dialogFloatingButtonPressed, i732);
        fallbackKeys.put(key_dialogFloatingIcon, key_dialogRoundCheckBoxCheck);
        fallbackKeys.put(key_dialogShadowLine, key_chat_emojiPanelShadowLine);
        fallbackKeys.put(key_actionBarDefaultArchived, key_actionBarDefault);
        SparseIntArray sparseIntArray12 = fallbackKeys;
        int i733 = key_actionBarDefaultArchivedSelector;
        int i734 = key_actionBarDefaultSelector;
        sparseIntArray12.put(i733, i734);
        fallbackKeys.put(key_actionBarDefaultArchivedIcon, key_actionBarDefaultIcon);
        SparseIntArray sparseIntArray13 = fallbackKeys;
        int i735 = key_actionBarDefaultArchivedTitle;
        int i736 = key_actionBarDefaultTitle;
        sparseIntArray13.put(i735, i736);
        fallbackKeys.put(key_actionBarDefaultArchivedSearch, key_actionBarDefaultSearch);
        fallbackKeys.put(key_actionBarDefaultArchivedSearchPlaceholder, key_actionBarDefaultSearchPlaceholder);
        SparseIntArray sparseIntArray14 = fallbackKeys;
        int i737 = key_chats_message_threeLines;
        int i738 = key_chats_message;
        sparseIntArray14.put(i737, i738);
        SparseIntArray sparseIntArray15 = fallbackKeys;
        int i739 = key_chats_nameMessage_threeLines;
        int i740 = key_chats_nameMessage;
        sparseIntArray15.put(i739, i740);
        fallbackKeys.put(key_chats_nameArchived, key_chats_name);
        fallbackKeys.put(key_chats_nameMessageArchived, i740);
        fallbackKeys.put(key_chats_nameMessageArchived_threeLines, i740);
        fallbackKeys.put(key_chats_messageArchived, i738);
        SparseIntArray sparseIntArray16 = fallbackKeys;
        int i741 = key_avatar_backgroundArchived;
        int i742 = key_chats_unreadCounterMuted;
        sparseIntArray16.put(i741, i742);
        SparseIntArray sparseIntArray17 = fallbackKeys;
        int i743 = key_chats_archiveBackground;
        int i744 = key_chats_actionBackground;
        sparseIntArray17.put(i743, i744);
        fallbackKeys.put(key_chats_archivePinBackground, i742);
        SparseIntArray sparseIntArray18 = fallbackKeys;
        int i745 = key_chats_archiveIcon;
        int i746 = key_chats_actionIcon;
        sparseIntArray18.put(i745, i746);
        fallbackKeys.put(key_chats_archiveText, i746);
        fallbackKeys.put(key_actionBarDefaultSubmenuItemIcon, key_dialogIcon);
        fallbackKeys.put(key_checkboxDisabled, i742);
        SparseIntArray sparseIntArray19 = fallbackKeys;
        int i747 = key_chat_status;
        int i748 = key_actionBarDefaultSubtitle;
        sparseIntArray19.put(i747, i748);
        SparseIntArray sparseIntArray20 = fallbackKeys;
        int i749 = key_chat_inGreenCall;
        int i750 = key_calls_callReceivedGreenIcon;
        sparseIntArray20.put(i749, i750);
        fallbackKeys.put(key_chat_outGreenCall, i750);
        fallbackKeys.put(key_actionBarTabActiveText, i736);
        fallbackKeys.put(key_actionBarTabUnactiveText, i748);
        fallbackKeys.put(key_actionBarTabLine, i736);
        fallbackKeys.put(key_actionBarTabSelector, i734);
        fallbackKeys.put(key_profile_status, key_avatar_subtitleInProfileBlue);
        fallbackKeys.put(key_chats_menuTopBackgroundCats, key_avatar_backgroundActionBarBlue);
        fallbackKeys.put(key_chat_outLinkSelectBackground, key_chat_linkSelectBackground);
        fallbackKeys.put(key_actionBarDefaultSubmenuSeparator, key_windowBackgroundGray);
        SparseIntArray sparseIntArray21 = fallbackKeys;
        int i751 = key_chat_attachPermissionImage;
        int i752 = key_dialogTextBlack;
        sparseIntArray21.put(i751, i752);
        fallbackKeys.put(key_chat_attachPermissionMark, key_chat_sentError);
        fallbackKeys.put(key_chat_attachPermissionText, i752);
        fallbackKeys.put(key_chat_attachEmptyImage, key_emptyListPlaceholder);
        fallbackKeys.put(key_actionBarBrowser, key_actionBarDefault);
        fallbackKeys.put(key_chats_sentReadCheck, key_chats_sentCheck);
        fallbackKeys.put(key_chat_outSentCheckRead, key_chat_outSentCheck);
        fallbackKeys.put(key_chat_outSentCheckReadSelected, key_chat_outSentCheckSelected);
        fallbackKeys.put(key_chats_archivePullDownBackground, i742);
        fallbackKeys.put(key_chats_archivePullDownBackgroundActive, i744);
        fallbackKeys.put(key_avatar_backgroundArchivedHidden, key_avatar_backgroundSaved);
        fallbackKeys.put(key_featuredStickers_removeButtonText, key_featuredStickers_addButtonPressed);
        SparseIntArray sparseIntArray22 = fallbackKeys;
        int i753 = key_dialogEmptyImage;
        int i754 = key_player_time;
        sparseIntArray22.put(i753, i754);
        fallbackKeys.put(key_dialogEmptyText, i754);
        fallbackKeys.put(key_location_actionIcon, i752);
        SparseIntArray sparseIntArray23 = fallbackKeys;
        int i755 = key_location_actionActiveIcon;
        int i756 = key_windowBackgroundWhiteBlueText7;
        sparseIntArray23.put(i755, i756);
        fallbackKeys.put(key_location_actionBackground, key_dialogBackground);
        fallbackKeys.put(key_location_actionPressedBackground, key_dialogBackgroundGray);
        fallbackKeys.put(key_location_sendLocationText, i756);
        fallbackKeys.put(key_location_sendLiveLocationText, key_windowBackgroundWhiteGreenText);
        SparseIntArray sparseIntArray24 = fallbackKeys;
        int i757 = key_chat_outTextSelectionHighlight;
        int i758 = key_chat_textSelectBackground;
        sparseIntArray24.put(i757, i758);
        fallbackKeys.put(key_chat_inTextSelectionHighlight, i758);
        SparseIntArray sparseIntArray25 = fallbackKeys;
        int i759 = key_chat_TextSelectionCursor;
        sparseIntArray25.put(i759, key_chat_messagePanelCursor);
        fallbackKeys.put(key_chat_outTextSelectionCursor, i759);
        SparseIntArray sparseIntArray26 = fallbackKeys;
        int i760 = key_chat_inPollCorrectAnswer;
        int i761 = key_chat_attachLocationBackground;
        sparseIntArray26.put(i760, i761);
        fallbackKeys.put(key_chat_outPollCorrectAnswer, i761);
        SparseIntArray sparseIntArray27 = fallbackKeys;
        int i762 = key_chat_inPollWrongAnswer;
        int i763 = key_chat_attachAudioBackground;
        sparseIntArray27.put(i762, i763);
        fallbackKeys.put(key_chat_outPollWrongAnswer, i763);
        fallbackKeys.put(key_chat_editMediaButton, key_dialogFloatingButton);
        fallbackKeys.put(key_chat_attachCheckBoxBackground, key_dialogRoundCheckBox);
        fallbackKeys.put(key_profile_tabText, key_windowBackgroundWhiteGrayText);
        SparseIntArray sparseIntArray28 = fallbackKeys;
        int i764 = key_profile_tabSelectedText;
        int i765 = key_windowBackgroundWhiteBlueHeader;
        sparseIntArray28.put(i764, i765);
        fallbackKeys.put(key_profile_tabSelectedLine, i765);
        fallbackKeys.put(key_profile_tabSelector, key_listSelector);
        fallbackKeys.put(key_chat_attachGalleryText, key_chat_attachGalleryBackground);
        fallbackKeys.put(key_chat_attachAudioText, i763);
        fallbackKeys.put(key_chat_attachFileText, key_chat_attachFileBackground);
        fallbackKeys.put(key_chat_attachContactText, key_chat_attachContactBackground);
        fallbackKeys.put(key_chat_attachLocationText, i761);
        fallbackKeys.put(key_chat_attachPollText, key_chat_attachPollBackground);
        SparseIntArray sparseIntArray29 = fallbackKeys;
        int i766 = key_chat_inPsaNameText;
        int i767 = key_avatar_nameInMessageGreen;
        sparseIntArray29.put(i766, i767);
        fallbackKeys.put(key_chat_outPsaNameText, i767);
        fallbackKeys.put(key_chat_outAdminText, key_chat_outTimeText);
        fallbackKeys.put(key_chat_outAdminSelectedText, key_chat_outTimeSelectedText);
        SparseIntArray sparseIntArray30 = fallbackKeys;
        int i768 = key_returnToCallMutedBackground;
        int i769 = key_windowBackgroundWhite;
        sparseIntArray30.put(i768, i769);
        SparseIntArray sparseIntArray31 = fallbackKeys;
        int i770 = key_dialogSwipeRemove;
        int i771 = key_avatar_backgroundRed;
        sparseIntArray31.put(i770, i771);
        fallbackKeys.put(key_chat_inReactionButtonBackground, key_chat_inLoader);
        fallbackKeys.put(key_chat_outReactionButtonBackground, key_chat_outLoader);
        SparseIntArray sparseIntArray32 = fallbackKeys;
        int i772 = key_chat_inReactionButtonText;
        sparseIntArray32.put(i772, key_chat_inPreviewInstantText);
        fallbackKeys.put(key_chat_outReactionButtonText, key_chat_outPreviewInstantText);
        fallbackKeys.put(key_chat_inReactionButtonTextSelected, i769);
        fallbackKeys.put(key_chat_outReactionButtonTextSelected, i769);
        fallbackKeys.put(key_dialogReactionMentionBackground, key_voipgroup_mutedByAdminGradient2);
        fallbackKeys.put(key_topics_unreadCounter, key_chats_unreadCounter);
        fallbackKeys.put(key_topics_unreadCounterMuted, key_chats_message);
        fallbackKeys.put(key_avatar_background2Saved, key_avatar_backgroundSaved);
        fallbackKeys.put(key_avatar_background2Red, i771);
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
        SparseIntArray sparseIntArray33 = fallbackKeys;
        int i773 = key_statisticChartLine_purple;
        int i774 = key_color_purple;
        sparseIntArray33.put(i773, i774);
        fallbackKeys.put(key_statisticChartLine_indigo, i774);
        fallbackKeys.put(key_statisticChartLine_cyan, key_color_cyan);
        fallbackKeys.put(key_actionBarActionModeReaction, key_windowBackgroundGray);
        fallbackKeys.put(key_actionBarActionModeReactionText, i772);
        fallbackKeys.put(key_table_background, key_graySection);
        fallbackKeys.put(key_table_border, key_divider);
        int i775 = 0;
        while (true) {
            int[] iArr = keys_avatar_background;
            if (i775 >= iArr.length) {
                break;
            }
            themeAccentExclusionKeys.add(Integer.valueOf(iArr[i775]));
            i775++;
        }
        int i776 = 0;
        while (true) {
            int[] iArr2 = keys_avatar_background2;
            if (i776 >= iArr2.length) {
                break;
            }
            themeAccentExclusionKeys.add(Integer.valueOf(iArr2[i776]));
            i776++;
        }
        int i777 = 0;
        while (true) {
            int[] iArr3 = keys_avatar_nameInMessage;
            if (i777 >= iArr3.length) {
                break;
            }
            themeAccentExclusionKeys.add(Integer.valueOf(iArr3[i777]));
            i777++;
        }
        int i778 = 0;
        while (true) {
            int[] iArr4 = keys_colors;
            if (i778 >= iArr4.length) {
                break;
            }
            themeAccentExclusionKeys.add(Integer.valueOf(iArr4[i778]));
            i778++;
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
        themeAccentExclusionKeys.add(Integer.valueOf(key_chat_inCodeBackground));
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
        themeAccentExclusionKeys.add(Integer.valueOf(key_voipgroup_rtmpButton));
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
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumStarGradient1));
        themeAccentExclusionKeys.add(Integer.valueOf(key_premiumStarGradient2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_stories_circle1));
        themeAccentExclusionKeys.add(Integer.valueOf(key_stories_circle2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_stories_circle_dialog1));
        themeAccentExclusionKeys.add(Integer.valueOf(key_stories_circle_dialog2));
        themeAccentExclusionKeys.add(Integer.valueOf(key_stories_circle_closeFriends1));
        themeAccentExclusionKeys.add(Integer.valueOf(key_stories_circle_closeFriends2));
        themes = new ArrayList();
        otherThemes = new ArrayList();
        themesDict = new HashMap();
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
        themeInfo6.setAccentColorOptions(new int[]{-10972987, -14444461, -3252606, -8428605, -14380627, -14050257, -7842636, -13464881, -12342073, -11359164, -3317869, -2981834, -8165684, -3256745, -2904512, -8681301}, new int[]{-4660851, -328756, -1572, -4108434, -3031781, -1335, -198952, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, -853047, -264993, 0, 0, -135756, -198730, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, -2104672, -937328, -2637335, -2639714, -1270157, -3428124, -6570777, -7223828, -6567550, -1793599, -1855875, -4674838, -1336199, -2900876, -6247730}, new int[]{0, -4532067, -1257580, -1524266, -1646910, -1519483, -1324823, -4138509, -4202516, -2040429, -1458474, -1256030, -3814930, -1000039, -1450082, -3485987}, new int[]{0, -1909081, -1592444, -2969879, -2439762, -1137033, -2119471, -6962197, -4857383, -4270699, -3364639, -2117514, -5000734, -1598028, -2045813, -5853742}, new int[]{0, -6371440, -1319256, -1258616, -1712961, -1186647, -1193816, -4467224, -4203544, -3023977, -1061929, -1255788, -2113811, -806526, -1715305, -3485976}, new int[]{99, 9, 10, 11, 12, 13, 14, 0, 1, 2, 3, 4, 5, 6, 7, 8}, new String[]{"", "p-pXcflrmFIBAAAAvXYQk-mCwZU", "JqSUrO0-mFIBAAAAWwTvLzoWGQI", "O-wmAfBPSFADAAAA4zINVfD_bro", "RepJ5uE_SVABAAAAr4d0YhgB850", "-Xc-np9y2VMCAAAARKr0yNNPYW0", "fqv01SQemVIBAAAApND8LDRUhRU", "fqv01SQemVIBAAAApND8LDRUhRU", "RepJ5uE_SVABAAAAr4d0YhgB850", "lp0prF8ISFAEAAAA_p385_CvG0w", "heptcj-hSVACAAAAC9RrMzOa-cs", "PllZ-bf_SFAEAAAA8crRfwZiDNg", "dhf9pceaQVACAAAAbzdVo4SCiZA", "Ujx2TFcJSVACAAAARJ4vLa50MkM", "p-pXcflrmFIBAAAAvXYQk-mCwZU", "dk_wwlghOFACAAAAfz9xrxi6euw"}, new int[]{0, NotificationCenter.updateBotMenuButton, 45, 0, 45, NotificationCenter.updateBotMenuButton, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 52, 46, 57, 45, 64, 52, 35, 36, 41, 50, 50, 35, 38, 37, 30});
        sortAccents(themeInfo6);
        ArrayList arrayList = themes;
        defaultTheme = themeInfo6;
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
        themeInfo7.setAccentColorOptions(new int[]{-7177260, -9860357, -14440464, -8687151, -9848491, -14053142, -9403671, -10044691, -13203974, -12138259, -10179489, -1344335, -1142742, -6127120, -2931932, -1131212, -8417365, -13270557}, new int[]{-6464359, -10267323, -13532789, -5413850, -11898828, -13410942, -13215889, -10914461, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-10465880, -9937588, -14983040, -6736562, -14197445, -13534568, -13144441, -10587280, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-14213586, -15263198, -16310753, -15724781, -15853551, -16051428, -14868183, -14668758, -15854566, -15326427, -15327979, -14411490, -14345453, -14738135, -14543346, -14212843, -15263205, -15854566}, new int[]{-15659501, -14277074, -15459034, -14542297, -14735336, -15129808, -15591910, -15459810, -15260623, -15853800, -15259879, -14477540, -14674936, -15461604, -13820650, -15067635, -14605528, -15260623}, new int[]{-13951445, -15395557, -15985382, -15855853, -16050417, -15525854, -15260627, -15327189, -15788258, -14799314, -15458796, -13952727, -13754603, -14081231, -14478324, -14081004, -15197667, -15788258}, new int[]{-15330777, -15066858, -15915220, -14213847, -15262439, -15260879, -15657695, -16443625, -15459285, -15589601, -14932454, -14740451, -15002870, -15264997, -13821660, -14805234, -14605784, -15459285}, new int[]{11, 12, 13, 14, 15, 16, 17, 18, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, new String[]{"O-wmAfBPSFADAAAA4zINVfD_bro", "RepJ5uE_SVABAAAAr4d0YhgB850", "dk_wwlghOFACAAAAfz9xrxi6euw", "9LW_RcoOSVACAAAAFTk3DTyXN-M", "PllZ-bf_SFAEAAAA8crRfwZiDNg", "-Xc-np9y2VMCAAAARKr0yNNPYW0", "kO4jyq55SFABAAAA0WEpcLfahXk", "CJNyxPMgSVAEAAAAvW9sMwc51cw", "fqv01SQemVIBAAAApND8LDRUhRU", "RepJ5uE_SVABAAAAr4d0YhgB850", "CJNyxPMgSVAEAAAAvW9sMwc51cw", "9LW_RcoOSVACAAAAFTk3DTyXN-M", "9GcNVISdSVADAAAAUcw5BYjELW4", "F5oWoCs7QFACAAAAgf2bD_mg8Bw", "9ShF73d1MFIIAAAAjWnm8_ZMe8Q", "3rX-PaKbSFACAAAAEiHNvcEm6X4", "dk_wwlghOFACAAAAfz9xrxi6euw", "fqv01SQemVIBAAAApND8LDRUhRU"}, new int[]{NotificationCenter.wallpapersDidLoad, 45, NotificationCenter.wallpapersDidLoad, NotificationCenter.fileUploadFailed, 45, NotificationCenter.wallpapersDidLoad, 45, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{40, 40, 31, 50, 25, 34, 35, 35, 38, 29, 24, 34, 34, 31, 29, 37, 21, 38});
        sortAccents(themeInfo7);
        themes.add(themeInfo7);
        HashMap hashMap = themesDict;
        currentNightTheme = themeInfo7;
        hashMap.put("Dark Blue", themeInfo7);
        ThemeInfo themeInfo8 = new ThemeInfo();
        themeInfo8.name = "Arctic Blue";
        themeInfo8.assetName = "arctic.attheme";
        themeInfo8.previewBackgroundColor = -1971728;
        themeInfo8.previewInColor = -1;
        themeInfo8.previewOutColor = -9657877;
        themeInfo8.sortIndex = 5;
        themeInfo8.setAccentColorOptions(new int[]{-12537374, -12472227, -3240928, -11033621, -2194124, -3382903, -13332245, -12342073, -11359164, -3317869, -2981834, -8165684, -3256745, -2904512, -8681301}, new int[]{-13525046, -14113959, -7579073, -13597229, -3581840, -8883763, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-11616542, -9716647, -6400452, -12008744, -2592697, -4297041, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-3808528, -2433367, -2700891, -1838093, -1120848, -1712148, -2037779, -4202261, -4005713, -1058332, -925763, -1975316, -1189672, -1318451, -2302235}, new int[]{-1510157, -4398164, -1647697, -3610898, -1130838, -1980692, -4270093, -4202261, -3415654, -1259815, -1521765, -4341268, -1127744, -1318219, -3945761}, new int[]{-4924688, -3283031, -1523567, -2494477, -1126510, -595210, -2037517, -3478548, -4661623, -927514, -796762, -2696971, -1188403, -1319735, -1577487}, new int[]{-3149585, -5714021, -1978209, -4925720, -1134713, -1718833, -3613709, -5317397, -3218014, -999207, -2116466, -4343054, -931397, -1583186, -3815718}, new int[]{9, 10, 11, 12, 13, 14, 0, 1, 2, 3, 4, 5, 6, 7, 8}, new String[]{"MIo6r0qGSFAFAAAAtL8TsDzNX60", "dhf9pceaQVACAAAAbzdVo4SCiZA", "fqv01SQemVIBAAAApND8LDRUhRU", "p-pXcflrmFIBAAAAvXYQk-mCwZU", "JqSUrO0-mFIBAAAAWwTvLzoWGQI", "F5oWoCs7QFACAAAAgf2bD_mg8Bw", "fqv01SQemVIBAAAApND8LDRUhRU", "RepJ5uE_SVABAAAAr4d0YhgB850", "PllZ-bf_SFAEAAAA8crRfwZiDNg", "pgJfpFNRSFABAAAACDT8s5sEjfc", "ptuUd96JSFACAAAATobI23sPpz0", "dhf9pceaQVACAAAAbzdVo4SCiZA", "JqSUrO0-mFIBAAAAWwTvLzoWGQI", "9iklpvIPQVABAAAAORQXKur_Eyc", "F5oWoCs7QFACAAAAgf2bD_mg8Bw"}, new int[]{315, 315, NotificationCenter.wallpapersDidLoad, 315, 0, NotificationCenter.updateBotMenuButton, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{50, 50, 58, 47, 46, 50, 49, 46, 51, 50, 49, 34, 54, 50, 40});
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
        themeInfo10.setAccentColorOptions(new int[]{-9781697, -7505693, -2204034, -10913816, -2375398, -12678921, -11881005, -11880383, -2534026, -1934037, -7115558, -3128522, -1528292, -8812381}, new int[]{-7712108, -4953061, -5288081, -14258547, -9154889, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-9939525, -5948598, -10335844, -13659747, -14054507, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{-15330532, -14806760, -15791344, -16184308, -16313063, -15921641, -15656164, -15986420, -15856883, -14871025, -16185078, -14937584, -14869736, -15855598}, new int[]{-14673881, -15724781, -15002342, -15458526, -15987697, -16184820, -16118258, -16250616, -15067624, -15527923, -14804447, -15790836, -15987960, -16316665}, new int[]{-15856877, -14608861, -15528430, -15921391, -15722209, -15197144, -15458015, -15591406, -15528431, -15068401, -16053749, -15594229, -15395825, -15724012}, new int[]{-14804694, -15658986, -14609382, -15656421, -16118509, -15855854, -16315381, -16052981, -14544354, -15791092, -15659241, -16316922, -15988214, -16185077}, new int[]{9, 10, 11, 12, 13, 0, 1, 2, 3, 4, 5, 6, 7, 8}, new String[]{"YIxYGEALQVADAAAAA3QbEH0AowY", "9LW_RcoOSVACAAAAFTk3DTyXN-M", "O-wmAfBPSFADAAAA4zINVfD_bro", "F5oWoCs7QFACAAAAgf2bD_mg8Bw", "-Xc-np9y2VMCAAAARKr0yNNPYW0", "fqv01SQemVIBAAAApND8LDRUhRU", "F5oWoCs7QFACAAAAgf2bD_mg8Bw", "ptuUd96JSFACAAAATobI23sPpz0", "p-pXcflrmFIBAAAAvXYQk-mCwZU", "Nl8Pg2rBQVACAAAA25Lxtb8SDp0", "dhf9pceaQVACAAAAbzdVo4SCiZA", "9GcNVISdSVADAAAAUcw5BYjELW4", "9LW_RcoOSVACAAAAFTk3DTyXN-M", "dk_wwlghOFACAAAAfz9xrxi6euw"}, new int[]{45, NotificationCenter.fileUploadFailed, 0, NotificationCenter.updateBotMenuButton, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{34, 47, 52, 48, 54, 50, 37, 56, 48, 49, 40, 64, 38, 48});
        sortAccents(themeInfo10);
        themes.add(themeInfo10);
        themesDict.put("Night", themeInfo10);
        String str4 = null;
        String string = sharedPreferences.getString("themes2", null);
        int i779 = 1;
        if (sharedPreferences.getInt("remote_version", 0) == 1) {
            int i780 = 0;
            while (i780 < 4) {
                long[] jArr = remoteThemesHash;
                StringBuilder sb = new StringBuilder();
                sb.append("2remoteThemesHash");
                sb.append(i780 != 0 ? Integer.valueOf(i780) : "");
                jArr[i780] = sharedPreferences.getLong(sb.toString(), 0L);
                int[] iArr5 = lastLoadingThemesTime;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("lastLoadingThemesTime");
                sb2.append(i780 != 0 ? Integer.valueOf(i780) : "");
                iArr5[i780] = sharedPreferences.getInt(sb2.toString(), 0);
                i780++;
            }
            i779 = 1;
        }
        sharedPreferences.edit().putInt("remote_version", i779).apply();
        if (TextUtils.isEmpty(string)) {
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
        } else {
            try {
                JSONArray jSONArray = new JSONArray(string);
                for (int i781 = 0; i781 < jSONArray.length(); i781++) {
                    ThemeInfo createWithJson = ThemeInfo.createWithJson(jSONArray.getJSONObject(i781));
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
        }
        sortThemes();
        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        try {
            ThemeInfo themeInfo11 = (ThemeInfo) themesDict.get("Dark Blue");
            String string3 = globalMainSettings.getString("theme", null);
            if ("Default".equals(string3)) {
                themeInfo = (ThemeInfo) themesDict.get("Blue");
                themeInfo.currentAccentId = DEFALT_THEME_ACCENT_ID;
            } else if ("Dark".equals(string3)) {
                themeInfo11.currentAccentId = 9;
                themeInfo = themeInfo11;
            } else if (string3 != null) {
                themeInfo = (ThemeInfo) themesDict.get(string3);
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
                themeInfo = (ThemeInfo) themesDict.get("Blue");
                themeInfo.currentAccentId = DEFALT_THEME_ACCENT_ID;
            } else if ("Dark".equals(string4)) {
                currentNightTheme = themeInfo11;
                themeInfo11.currentAccentId = 9;
            } else if (string4 != null && (themeInfo2 = (ThemeInfo) themesDict.get(string4)) != null) {
                currentNightTheme = themeInfo2;
            }
            if (currentNightTheme != null && !sharedPreferences.contains("lastDarkTheme")) {
                SharedPreferences.Editor edit2 = sharedPreferences.edit();
                edit2.putString("lastDarkTheme", currentNightTheme.getKey());
                edit2.commit();
            }
            SharedPreferences.Editor editor4 = null;
            SharedPreferences.Editor editor5 = null;
            for (ThemeInfo themeInfo12 : themesDict.values()) {
                if (themeInfo12.assetName == null || themeInfo12.accentBaseColor == 0) {
                    str3 = str3;
                    themeInfo = themeInfo;
                    editor5 = editor5;
                } else {
                    String string5 = sharedPreferences.getString("accents_" + themeInfo12.assetName, str4);
                    themeInfo12.currentAccentId = sharedPreferences.getInt("accent_current_" + themeInfo12.assetName, themeInfo12.firstAccentIsDefault ? DEFALT_THEME_ACCENT_ID : 0);
                    ArrayList arrayList2 = new ArrayList();
                    if (TextUtils.isEmpty(string5)) {
                        themeInfo4 = themeInfo;
                        editor = editor5;
                        str = str3;
                        String str6 = "accent_for_" + themeInfo12.assetName;
                        int i782 = globalMainSettings.getInt(str6, 0);
                        if (i782 != 0) {
                            if (editor4 == null) {
                                editor4 = globalMainSettings.edit();
                                editor3 = sharedPreferences.edit();
                            } else {
                                editor3 = editor;
                            }
                            editor4.remove(str6);
                            int size = themeInfo12.themeAccents.size();
                            int i783 = 0;
                            while (true) {
                                if (i783 >= size) {
                                    ThemeAccent themeAccent = new ThemeAccent();
                                    themeAccent.id = 100;
                                    themeAccent.accentColor = i782;
                                    themeAccent.parentTheme = themeInfo12;
                                    themeInfo12.themeAccentsMap.put(100, themeAccent);
                                    arrayList2.add(0, themeAccent);
                                    themeInfo12.currentAccentId = 100;
                                    themeInfo12.lastAccentId = 101;
                                    SerializedData serializedData = new SerializedData(72);
                                    serializedData.writeInt32(9);
                                    serializedData.writeInt32(1);
                                    serializedData.writeInt32(themeAccent.id);
                                    serializedData.writeInt32(themeAccent.accentColor);
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
                                    serializedData.writeBool(false);
                                    editor3.putString("accents_" + themeInfo12.assetName, Base64.encodeToString(serializedData.toByteArray(), 3));
                                    break;
                                }
                                ThemeAccent themeAccent2 = (ThemeAccent) themeInfo12.themeAccents.get(i783);
                                if (themeAccent2.accentColor == i782) {
                                    themeInfo12.currentAccentId = themeAccent2.id;
                                    break;
                                }
                                i783++;
                            }
                            editor3.putInt("accent_current_" + themeInfo12.assetName, themeInfo12.currentAccentId);
                            editor5 = editor3;
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
                            if (accent != null) {
                                themeInfo12.overrideWallpaper = accent.overrideWallpaper;
                            }
                            str3 = str;
                            themeInfo = themeInfo4;
                        }
                    } else {
                        try {
                            SerializedData serializedData2 = new SerializedData(Base64.decode(string5, i));
                            boolean z2 = true;
                            int readInt322 = serializedData2.readInt32(true);
                            int readInt323 = serializedData2.readInt32(true);
                            int i784 = 0;
                            while (i784 < readInt323) {
                                try {
                                    ThemeAccent themeAccent3 = new ThemeAccent();
                                    themeAccent3.id = serializedData2.readInt32(z2);
                                    themeAccent3.accentColor = serializedData2.readInt32(z2);
                                    if (readInt322 >= 9) {
                                        themeAccent3.accentColor2 = serializedData2.readInt32(z2);
                                    }
                                    themeAccent3.parentTheme = themeInfo12;
                                    themeAccent3.myMessagesAccentColor = serializedData2.readInt32(true);
                                    themeAccent3.myMessagesGradientAccentColor1 = serializedData2.readInt32(true);
                                    if (readInt322 >= 7) {
                                        themeAccent3.myMessagesGradientAccentColor2 = serializedData2.readInt32(true);
                                        themeAccent3.myMessagesGradientAccentColor3 = serializedData2.readInt32(true);
                                    }
                                    if (readInt322 >= 8) {
                                        z = true;
                                        themeAccent3.myMessagesAnimated = serializedData2.readBool(true);
                                    } else {
                                        z = true;
                                    }
                                    if (readInt322 >= 3) {
                                        themeInfo5 = themeInfo;
                                        editor2 = editor5;
                                        readInt32 = serializedData2.readInt64(z);
                                    } else {
                                        themeInfo5 = themeInfo;
                                        editor2 = editor5;
                                        readInt32 = serializedData2.readInt32(z);
                                    }
                                    themeAccent3.backgroundOverrideColor = readInt32;
                                    themeAccent3.backgroundGradientOverrideColor1 = readInt322 >= 2 ? serializedData2.readInt64(z) : serializedData2.readInt32(z);
                                    if (readInt322 >= 6) {
                                        themeAccent3.backgroundGradientOverrideColor2 = serializedData2.readInt64(z);
                                        themeAccent3.backgroundGradientOverrideColor3 = serializedData2.readInt64(z);
                                    }
                                    if (readInt322 >= 1) {
                                        themeAccent3.backgroundRotation = serializedData2.readInt32(true);
                                    }
                                    if (readInt322 >= 4) {
                                        serializedData2.readInt64(true);
                                        str2 = str3;
                                        themeAccent3.patternIntensity = (float) serializedData2.readDouble(true);
                                        themeAccent3.patternMotion = serializedData2.readBool(true);
                                        if (readInt322 >= 5) {
                                            themeAccent3.patternSlug = serializedData2.readString(true);
                                        }
                                    } else {
                                        str2 = str3;
                                    }
                                    if (readInt322 >= 5 && serializedData2.readBool(true)) {
                                        themeAccent3.account = serializedData2.readInt32(true);
                                        themeAccent3.info = TLRPC.Theme.TLdeserialize(serializedData2, serializedData2.readInt32(true), true);
                                    }
                                    TLRPC.TL_theme tL_theme = themeAccent3.info;
                                    if (tL_theme != null) {
                                        themeAccent3.isDefault = tL_theme.isDefault;
                                    }
                                    themeInfo12.themeAccentsMap.put(themeAccent3.id, themeAccent3);
                                    TLRPC.TL_theme tL_theme2 = themeAccent3.info;
                                    if (tL_theme2 != null) {
                                        themeInfo12.accentsByThemeId.put(tL_theme2.id, themeAccent3);
                                    }
                                    arrayList2.add(themeAccent3);
                                    themeInfo12.lastAccentId = Math.max(themeInfo12.lastAccentId, themeAccent3.id);
                                    i784++;
                                    str3 = str2;
                                    themeInfo = themeInfo5;
                                    editor5 = editor2;
                                    z2 = true;
                                } finally {
                                }
                            }
                            themeInfo4 = themeInfo;
                            editor = editor5;
                            str = str3;
                        } finally {
                            FileLog.e(th);
                            RuntimeException runtimeException2 = new RuntimeException(th);
                        }
                    }
                    editor5 = editor;
                    if (!arrayList2.isEmpty()) {
                    }
                    sparseArray = themeInfo12.themeAccentsMap;
                    if (sparseArray != null) {
                        themeInfo12.currentAccentId = !themeInfo12.firstAccentIsDefault ? DEFALT_THEME_ACCENT_ID : 0;
                    }
                    themeInfo12.loadWallpapers(sharedPreferences);
                    accent = themeInfo12.getAccent(false);
                    if (accent != null) {
                    }
                    str3 = str;
                    themeInfo = themeInfo4;
                }
                str4 = null;
                i = 3;
            }
            ThemeInfo themeInfo13 = themeInfo;
            SharedPreferences.Editor editor6 = editor5;
            String str7 = str3;
            if (editor4 != null) {
                editor4.commit();
                editor6.commit();
            }
            selectedAutoNightType = globalMainSettings.getInt("selectedAutoNightType", Build.VERSION.SDK_INT >= 29 ? 3 : 0);
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
                boolean z3 = globalMainSettings.getBoolean("overrideThemeWallpaper", false);
                long j3 = globalMainSettings.getLong("selectedBackground2", 1000001L);
                if (j3 == -1 || (z3 && j3 != -2 && j3 != 1000001)) {
                    OverrideWallpaperInfo overrideWallpaperInfo = new OverrideWallpaperInfo();
                    overrideWallpaperInfo.color = globalMainSettings.getInt("selectedColor", 0);
                    overrideWallpaperInfo.slug = globalMainSettings.getString("selectedBackgroundSlug", str7);
                    if (j3 < -100 || j3 > -1 || overrideWallpaperInfo.color == 0) {
                        overrideWallpaperInfo.fileName = "wallpaper.jpg";
                        overrideWallpaperInfo.originalFileName = "wallpaper_original.jpg";
                    } else {
                        overrideWallpaperInfo.slug = "c";
                        overrideWallpaperInfo.fileName = str7;
                        overrideWallpaperInfo.originalFileName = str7;
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
            AndroidUtilities.runOnUIThread(new MessagesController$$ExternalSyntheticLambda91());
            ambientSensorListener = new SensorEventListener() { // from class: org.telegram.ui.ActionBar.Theme.9
                @Override // android.hardware.SensorEventListener
                public void onAccuracyChanged(Sensor sensor, int i785) {
                }

                @Override // android.hardware.SensorEventListener
                public void onSensorChanged(SensorEvent sensorEvent) {
                    Runnable runnable;
                    float f = sensorEvent.values[0];
                    if (f <= 0.0f) {
                        f = 0.1f;
                    }
                    if (ApplicationLoader.mainInterfacePaused || !ApplicationLoader.isScreenOn) {
                        return;
                    }
                    float unused = Theme.lastBrightnessValue = f > 500.0f ? 1.0f : ((float) Math.ceil((Math.log(f) * 9.932299613952637d) + 27.05900001525879d)) / 100.0f;
                    if (Theme.lastBrightnessValue > Theme.autoNightBrighnessThreshold) {
                        if (Theme.switchNightRunnableScheduled) {
                            boolean unused2 = Theme.switchNightRunnableScheduled = false;
                            AndroidUtilities.cancelRunOnUIThread(Theme.switchNightBrightnessRunnable);
                        }
                        if (Theme.switchDayRunnableScheduled) {
                            return;
                        }
                        boolean unused3 = Theme.switchDayRunnableScheduled = true;
                        runnable = Theme.switchDayBrightnessRunnable;
                    } else {
                        if (MediaController.getInstance().isRecordingOrListeningByProximity()) {
                            return;
                        }
                        if (Theme.switchDayRunnableScheduled) {
                            boolean unused4 = Theme.switchDayRunnableScheduled = false;
                            AndroidUtilities.cancelRunOnUIThread(Theme.switchDayBrightnessRunnable);
                        }
                        if (Theme.switchNightRunnableScheduled) {
                            return;
                        }
                        boolean unused5 = Theme.switchNightRunnableScheduled = true;
                        runnable = Theme.switchNightBrightnessRunnable;
                    }
                    AndroidUtilities.runOnUIThread(runnable, Theme.access$3000());
                }
            };
            viewPos = new int[2];
            Paint paint = new Paint();
            DEBUG_RED = paint;
            paint.setColor(-65536);
            Paint paint2 = new Paint();
            DEBUG_BLUE = paint2;
            paint2.setColor(-16776961);
        } catch (Exception th) {
            throw new RuntimeException(th);
        }
    }

    private static float abs(float f) {
        return f > 0.0f ? f : -f;
    }

    static /* synthetic */ long access$3000() {
        return getAutoNightSwitchThemeDelay();
    }

    public static int adaptHSV(int i, float f, float f2) {
        float[] tempHsv = getTempHsv(5);
        Color.colorToHSV(i, tempHsv);
        float f3 = tempHsv[1];
        if (f3 > 0.1f && f3 < 0.9f) {
            tempHsv[1] = MathUtils.clamp(f3 + f, 0.0f, 1.0f);
        }
        tempHsv[2] = MathUtils.clamp(tempHsv[2] + f2, 0.0f, 1.0f);
        return Color.HSVToColor(Color.alpha(i), tempHsv);
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

    private static void addChatDrawable(String str, Drawable drawable, int i) {
        defaultChatDrawables.put(str, drawable);
        defaultChatDrawableColorKeys.put(str, Integer.valueOf(i));
    }

    private static void addChatPaint(String str, Paint paint, int i) {
        defaultChatPaints.put(str, paint);
        defaultChatPaintColors.put(str, Integer.valueOf(i));
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

    public static void applyChatServiceMessageColor() {
        Drawable drawable = wallpaper;
        if (drawable != null) {
            applyChatServiceMessageColor(null, null, drawable);
        }
    }

    public static void applyChatServiceMessageColor(int[] iArr, Drawable drawable, Drawable drawable2) {
        int valueAt;
        int valueAt2;
        float f;
        if (chat_actionBackgroundPaint == null) {
            return;
        }
        serviceMessageColor = serviceMessageColorBackup;
        serviceSelectedMessageColor = serviceSelectedMessageColorBackup;
        if (iArr == null || iArr.length < 2) {
            int indexOfKey = currentColors.indexOfKey(key_chat_serviceBackground);
            valueAt = indexOfKey >= 0 ? currentColors.valueAt(indexOfKey) : serviceMessageColor;
            int indexOfKey2 = currentColors.indexOfKey(key_chat_serviceBackgroundSelected);
            valueAt2 = indexOfKey2 >= 0 ? currentColors.valueAt(indexOfKey2) : serviceSelectedMessageColor;
        } else {
            valueAt = iArr[0];
            valueAt2 = iArr[1];
            serviceMessageColor = valueAt;
            serviceSelectedMessageColor = valueAt2;
        }
        if (drawable == null) {
            drawable = drawable2;
        }
        boolean z = drawable instanceof MotionBackgroundDrawable;
        if ((z || (drawable instanceof BitmapDrawable)) && SharedConfig.getDevicePerformanceClass() != 0 && LiteMode.isEnabled(32)) {
            Bitmap bitmap = z ? ((MotionBackgroundDrawable) drawable).getBitmap() : drawable instanceof BitmapDrawable ? checkBlur(drawable) : null;
            if (serviceBitmap != bitmap) {
                serviceBitmap = bitmap;
                Bitmap bitmap2 = serviceBitmap;
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                serviceBitmapShader = new BitmapShader(bitmap2, tileMode, tileMode);
                if (Build.VERSION.SDK_INT >= 33) {
                    serviceBitmapShader.setFilterMode(2);
                }
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
            int i = key_chat_serviceText;
            setDrawableColorByKey(drawable3, i);
            setDrawableColorByKey(chat_msgStickerCheckDrawable, i);
            setDrawableColorByKey(chat_msgStickerHalfCheckDrawable, i);
            setDrawableColorByKey(chat_msgStickerViewsDrawable, i);
            setDrawableColorByKey(chat_msgStickerRepliesDrawable, i);
            chat_actionTextPaint.setColor(getColor(i));
            chat_actionTextPaint2.setColor(getColor(i));
            chat_actionTextPaint.linkColor = getColor(key_chat_serviceLink);
            chat_unlockExtendedMediaTextPaint.setColor(getColor(i));
            Drawable drawable4 = chat_commentStickerDrawable;
            int i2 = key_chat_serviceIcon;
            setDrawableColorByKey(drawable4, i2);
            setDrawableColorByKey(chat_shareIconDrawable, i2);
            setDrawableColorByKey(chat_replyIconDrawable, i2);
            setDrawableColorByKey(chat_goIconDrawable, i2);
            setDrawableColorByKey(chat_botInlineDrawable, i2);
            setDrawableColorByKey(chat_botWebViewDrawable, i2);
            setDrawableColorByKey(chat_botInviteDrawable, i2);
            setDrawableColorByKey(chat_botLinkDrawable, i2);
            chat_botButtonPaint.setColor(getColor(key_chat_botButtonText));
        }
        chat_actionBackgroundPaint.setColor(valueAt);
        chat_actionBackgroundSelectedPaint.setColor(valueAt2);
        currentColor = valueAt;
        if (serviceBitmapShader == null || !(currentColors.indexOfKey(key_chat_serviceBackground) < 0 || z || (drawable instanceof BitmapDrawable))) {
            chat_actionBackgroundPaint.setColorFilter(null);
            chat_actionBackgroundPaint.setShader(null);
            chat_actionBackgroundSelectedPaint.setColorFilter(null);
            chat_actionBackgroundSelectedPaint.setShader(null);
            chat_actionBackgroundGradientDarkenPaint.setAlpha(21);
            return;
        }
        ColorMatrix colorMatrix = new ColorMatrix();
        if (!z) {
            colorMatrix.setSaturation(1.6f);
            AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, isCurrentThemeDark() ? 0.9f : 0.84f);
            f = isCurrentThemeDark() ? -0.04f : 0.06f;
        } else if (((MotionBackgroundDrawable) drawable).getIntensity() >= 0.0f) {
            colorMatrix.setSaturation(1.6f);
            AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, isCurrentThemeDark() ? 0.97f : 0.92f);
            if (isCurrentThemeDark()) {
                f = 0.12f;
            }
            f = -0.06f;
        } else {
            colorMatrix.setSaturation(1.1f);
            AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix, isCurrentThemeDark() ? 0.4f : 0.8f);
            if (isCurrentThemeDark()) {
                f = 0.08f;
            }
            f = -0.06f;
        }
        AndroidUtilities.adjustBrightnessColorMatrix(colorMatrix, f);
        chat_actionBackgroundPaint.setFilterBitmap(true);
        chat_actionBackgroundPaint.setShader(serviceBitmapShader);
        chat_actionBackgroundPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        chat_actionBackgroundPaint.setAlpha(NotificationCenter.closeSearchByActiveAction);
        chat_actionBackgroundSelectedPaint.setFilterBitmap(true);
        chat_actionBackgroundSelectedPaint.setShader(serviceBitmapShader);
        ColorMatrix colorMatrix2 = new ColorMatrix(colorMatrix);
        AndroidUtilities.adjustSaturationColorMatrix(colorMatrix2, 0.26f);
        isCurrentThemeDark();
        AndroidUtilities.multiplyBrightnessColorMatrix(colorMatrix2, 0.92f);
        chat_actionBackgroundSelectedPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix2));
        chat_actionBackgroundSelectedPaint.setAlpha(NotificationCenter.closeSearchByActiveAction);
        chat_actionBackgroundGradientDarkenPaint.setAlpha(0);
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
            if (i >= drawableArr.length) {
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
                int color = getColor(key_windowBackgroundWhiteBlackText);
                PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
                chat_animatedEmojiTextColorFilter = new PorterDuffColorFilter(color, mode);
                chat_outAnimatedEmojiTextColorFilter = new PorterDuffColorFilter(getColor(key_chat_messageTextOut), mode);
                PremiumGradient.getInstance().checkIconColors();
                return;
            }
            setDrawableColorByKey(drawableArr[i], key_avatar_text);
            i++;
        }
    }

    public static void applyDayNightThemeMaybe(boolean z) {
        if (previousTheme != null) {
            return;
        }
        if (z) {
            ThemeInfo themeInfo = currentTheme;
            ThemeInfo themeInfo2 = currentNightTheme;
            if (themeInfo == themeInfo2) {
                return;
            }
            if (themeInfo != null && (themeInfo2 == null || themeInfo.isDark() == currentNightTheme.isDark())) {
                return;
            }
            isInNigthMode = true;
            lastThemeSwitchTime = SystemClock.elapsedRealtime();
            switchingNightTheme = true;
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, currentNightTheme, Boolean.TRUE, null, -1);
        } else {
            ThemeInfo themeInfo3 = currentTheme;
            ThemeInfo themeInfo4 = currentDayTheme;
            if (themeInfo3 == themeInfo4) {
                return;
            }
            if (themeInfo3 != null && (themeInfo4 == null || themeInfo3.isLight() == currentDayTheme.isLight())) {
                return;
            }
            isInNigthMode = false;
            lastThemeSwitchTime = SystemClock.elapsedRealtime();
            switchingNightTheme = true;
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needSetDayNightTheme, currentDayTheme, Boolean.TRUE, null, -1);
        }
        switchingNightTheme = false;
    }

    public static void applyDefaultShadow(Paint paint) {
        paint.setShadowLayer(AndroidUtilities.dpf2(1.0f), 0.0f, AndroidUtilities.dpf2(0.33f), default_shadow_color);
    }

    public static void applyDialogsTheme() {
        if (dialogs_namePaint == null) {
            return;
        }
        for (int i = 0; i < 2; i++) {
            dialogs_namePaint[i].setColor(getColor(key_chats_name));
            dialogs_nameEncryptedPaint[i].setColor(getColor(key_chats_secretName));
            TextPaint textPaint = dialogs_messagePaint[i];
            int color = getColor(key_chats_message);
            textPaint.linkColor = color;
            textPaint.setColor(color);
            dialogs_messagePrintingPaint[i].setColor(getColor(key_chats_actionMessage));
        }
        dialogs_searchNamePaint.setColor(getColor(key_chats_name));
        dialogs_searchNameEncryptedPaint.setColor(getColor(key_chats_secretName));
        TextPaint textPaint2 = dialogs_messageNamePaint;
        int color2 = getColor(key_chats_nameMessage_threeLines);
        textPaint2.linkColor = color2;
        textPaint2.setColor(color2);
        dialogs_tabletSeletedPaint.setColor(getColor(key_chats_tabletSelectedOverlay));
        dialogs_pinnedPaint.setColor(getColor(key_chats_pinnedOverlay));
        dialogs_timePaint.setColor(getColor(key_chats_date));
        dialogs_countTextPaint.setColor(getColor(key_chats_unreadCounterText));
        TextPaint textPaint3 = dialogs_archiveTextPaint;
        int i2 = key_chats_archiveText;
        textPaint3.setColor(getColor(i2));
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

    public static void applyProfileTheme() {
        if (profile_verifiedDrawable == null) {
            return;
        }
        profile_aboutTextPaint.setColor(getColor(key_windowBackgroundWhiteBlackText));
        profile_aboutTextPaint.linkColor = getColor(key_windowBackgroundWhiteLinkText);
        setDrawableColorByKey(profile_verifiedDrawable, key_profile_verifiedBackground);
        setDrawableColorByKey(profile_verifiedCheckDrawable, key_profile_verifiedCheck);
    }

    public static void applyServiceShaderMatrix(int i, int i2, float f, float f2) {
        applyServiceShaderMatrix(serviceBitmap, serviceBitmapShader, serviceBitmapMatrix, i, i2, f, f2);
    }

    public static void applyServiceShaderMatrix(Bitmap bitmap, BitmapShader bitmapShader, Matrix matrix, int i, int i2, float f, float f2) {
        if (bitmapShader == null || matrix == null) {
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

    public static void applyServiceShaderMatrixForView(View view, View view2) {
        applyServiceShaderMatrixForView(view, view2, null);
    }

    public static void applyServiceShaderMatrixForView(View view, View view2, ResourcesProvider resourcesProvider) {
        int i;
        if (view == null || view2 == null) {
            return;
        }
        view.getLocationOnScreen(viewPos);
        int[] iArr = viewPos;
        int i2 = iArr[0];
        int i3 = iArr[1];
        view2.getLocationOnScreen(iArr);
        if (view2 instanceof ThemePreviewActivity.BackgroundView) {
            Bitmap bitmap = serviceBitmap;
            if (bitmap != null) {
                float width = bitmap.getWidth();
                i = (int) (i2 + (((view2.getMeasuredWidth() - (width * Math.max(view2.getMeasuredWidth() / width, view2.getMeasuredHeight() / serviceBitmap.getHeight()))) / 2.0f) - ((ThemePreviewActivity.BackgroundView) view2).tx));
            } else {
                i = (int) (i2 + (-((ThemePreviewActivity.BackgroundView) view2).tx));
            }
            i2 = i;
            i3 = (int) (i3 + (-((ThemePreviewActivity.BackgroundView) view2).ty));
        }
        int measuredWidth = view2.getMeasuredWidth();
        int measuredHeight = view2.getMeasuredHeight();
        if (resourcesProvider != null) {
            resourcesProvider.applyServiceShaderMatrix(measuredWidth, measuredHeight, i2, i3 - viewPos[1]);
        } else {
            applyServiceShaderMatrix(measuredWidth, measuredHeight, i2, i3 - viewPos[1]);
        }
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

    /* JADX WARN: Code restructure failed: missing block: B:38:0x0040, code lost:
    
        if (r8 == false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0042, code lost:
    
        r0 = org.telegram.messenger.MessagesController.getGlobalMainSettings().edit();
        r0.putString("theme", r7.getKey());
        r0.apply();
     */
    /* JADX WARN: Removed duplicated region for block: B:24:0x01ca A[Catch: Exception -> 0x002c, TryCatch #2 {Exception -> 0x002c, blocks: (B:9:0x000d, B:12:0x0014, B:17:0x001d, B:18:0x002f, B:20:0x01be, B:22:0x01c2, B:24:0x01ca, B:25:0x01de, B:39:0x0042, B:40:0x0054, B:42:0x005b, B:43:0x006b, B:45:0x007e, B:53:0x00ba, B:104:0x01a6, B:111:0x01b8, B:113:0x0060, B:55:0x00bc, B:57:0x00d2, B:59:0x00de, B:62:0x00e2, B:64:0x00e5, B:66:0x00ef, B:68:0x0101, B:69:0x00f5, B:71:0x00ff, B:75:0x0104, B:77:0x0115, B:79:0x0121, B:81:0x0139, B:83:0x0143, B:84:0x014f, B:86:0x0157, B:88:0x0161, B:89:0x016e, B:91:0x0176, B:93:0x0180, B:96:0x018d, B:98:0x0199), top: B:8:0x000d, inners: #0 }] */
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
                    AndroidUtilities.runOnUIThread(new MessagesController$$ExternalSyntheticLambda91(), 2100L);
                }
            }
            currentTheme = themeInfo;
            refreshThemeColors();
            BlurSettingsBottomSheet.onThemeApplyed();
            if (previousTheme == null || !z || switchingNightTheme) {
                return;
            }
            MessagesController.getInstance(themeInfo.account).saveTheme(themeInfo, themeInfo.getAccent(false), z3, false);
            return;
        }
        String[] strArr = new String[1];
        String str = themeInfo.assetName;
        SparseIntArray themeFileValues = str != null ? getThemeFileValues(null, str, null) : getThemeFileValues(new File(themeInfo.pathToFile), null, strArr);
        currentColorsNoAccent = themeFileValues;
        themedWallpaperFileOffset = themeFileValues.get(key_wallpaperFileOffset, -1);
        if (TextUtils.isEmpty(strArr[0])) {
            try {
                if (themeInfo.pathToWallpaper != null) {
                    new File(themeInfo.pathToWallpaper).delete();
                }
            } catch (Exception unused) {
            }
            themeInfo.pathToWallpaper = null;
            themedWallpaperLink = null;
        } else {
            themedWallpaperLink = strArr[0];
            String absolutePath = new File(ApplicationLoader.getFilesDirFixed(), Utilities.MD5(themedWallpaperLink) + ".wp").getAbsolutePath();
            try {
                String str2 = themeInfo.pathToWallpaper;
                if (str2 != null && !str2.equals(absolutePath)) {
                    new File(themeInfo.pathToWallpaper).delete();
                }
            } catch (Exception unused2) {
            }
            themeInfo.pathToWallpaper = absolutePath;
            try {
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
                } catch (Exception unused3) {
                }
                try {
                    String queryParameter3 = parse.getQueryParameter("rotation");
                    if (!TextUtils.isEmpty(queryParameter3)) {
                        themeInfo.patternBgGradientRotation = Utilities.parseInt((CharSequence) queryParameter3).intValue();
                    }
                } catch (Exception unused4) {
                }
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
        if (!z3) {
            currentDayTheme = themeInfo;
            if (isCurrentThemeNight()) {
            }
        }
        currentTheme = themeInfo;
        refreshThemeColors();
        BlurSettingsBottomSheet.onThemeApplyed();
        if (previousTheme == null) {
        }
    }

    public static ThemeInfo applyThemeFile(File file, String str, TLRPC.TL_theme tL_theme, boolean z) {
        File file2;
        String str2;
        try {
            if (!str.toLowerCase().endsWith(".attheme")) {
                str = str + ".attheme";
            }
            if (z) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.goingToPreviewTheme, new Object[0]);
                ThemeInfo themeInfo = new ThemeInfo();
                themeInfo.name = str;
                themeInfo.info = tL_theme;
                themeInfo.pathToFile = file.getAbsolutePath();
                themeInfo.account = UserConfig.selectedAccount;
                applyThemeTemporary(themeInfo, false);
                return themeInfo;
            }
            if (tL_theme != null) {
                str2 = "remote" + tL_theme.id;
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
            ThemeInfo themeInfo2 = (ThemeInfo) themesDict.get(str2);
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
            themeInfo2.info = tL_theme;
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

    public static void applyThemeInBackground(ThemeInfo themeInfo, boolean z, Runnable runnable) {
        applyThemeInBackground(themeInfo, true, true, z, runnable);
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x006b, code lost:
    
        if (r10 == false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x006d, code lost:
    
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
                    AndroidUtilities.runOnUIThread(new MessagesController$$ExternalSyntheticLambda91(), 2100L);
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
        final Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$applyThemeInBackground$2(strArr, themeInfo, z3, z, runnable);
            }
        };
        String str = themeInfo.assetName;
        if (str != null) {
            getThemeFileValuesInBackground(null, str, null, new Utilities.Callback() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda21
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    Theme.lambda$applyThemeInBackground$3(runnable2, (SparseIntArray) obj);
                }
            });
        } else {
            getThemeFileValuesInBackground(new File(themeInfo.pathToFile), null, strArr, new Utilities.Callback() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda22
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    Theme.lambda$applyThemeInBackground$4(runnable2, (SparseIntArray) obj);
                }
            });
        }
    }

    public static void applyThemeTemporary(ThemeInfo themeInfo, boolean z) {
        previousTheme = getCurrentTheme();
        hasPreviousTheme = true;
        isApplyingAccent = z;
        applyTheme(themeInfo, false, false, false);
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

    private static void calcBackgroundColor(Drawable drawable, int i) {
        if (i != 2) {
            int[] calcDrawableColor = AndroidUtilities.calcDrawableColor(drawable);
            int i2 = calcDrawableColor[0];
            serviceMessageColorBackup = i2;
            serviceMessageColor = i2;
            int i3 = calcDrawableColor[1];
            serviceSelectedMessageColorBackup = i3;
            serviceSelectedMessageColor = i3;
        }
    }

    public static boolean canStartHolidayAnimation() {
        return canStartHolidayAnimation;
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

    private static int changeBrightness(int i, float f) {
        int red = (int) (Color.red(i) * f);
        int green = (int) (Color.green(i) * f);
        int blue = (int) (Color.blue(i) * f);
        return Color.argb(Color.alpha(i), red < 0 ? 0 : Math.min(red, NotificationCenter.closeSearchByActiveAction), green < 0 ? 0 : Math.min(green, NotificationCenter.closeSearchByActiveAction), blue >= 0 ? Math.min(blue, NotificationCenter.closeSearchByActiveAction) : 0);
    }

    public static int changeColorAccent(int i) {
        ThemeAccent accent = currentTheme.getAccent(false);
        return changeColorAccent(currentTheme, accent != null ? accent.accentColor : 0, i);
    }

    public static int changeColorAccent(int i, int i2, int i3, boolean z, int i4) {
        float[] tempHsv = getTempHsv(3);
        float[] tempHsv2 = getTempHsv(4);
        Color.colorToHSV(i, tempHsv);
        Color.colorToHSV(i2, tempHsv2);
        return changeColorAccent(tempHsv, tempHsv2, i3, z, i4);
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
        return changeColorAccent(tempHsv, tempHsv2, i2, themeInfo.isDark(), i2);
    }

    public static int changeColorAccent(float[] fArr, float[] fArr2, int i, boolean z, int i2) {
        if (tmpHSV5 == null) {
            tmpHSV5 = new float[3];
        }
        float[] fArr3 = tmpHSV5;
        Color.colorToHSV(i, fArr3);
        if (Math.min(abs(fArr3[0] - fArr[0]), abs((fArr3[0] - fArr[0]) - 360.0f)) > 30.0f) {
            return i2;
        }
        float min = Math.min((fArr3[1] * 1.5f) / fArr[1], 1.0f);
        fArr3[0] = (fArr3[0] + fArr2[0]) - fArr[0];
        fArr3[1] = (fArr3[1] * fArr2[1]) / fArr[1];
        fArr3[2] = fArr3[2] * ((1.0f - min) + ((min * fArr2[2]) / fArr[2]));
        int HSVToColor = Color.HSVToColor(Color.alpha(i), fArr3);
        float computePerceivedBrightness = AndroidUtilities.computePerceivedBrightness(i);
        float computePerceivedBrightness2 = AndroidUtilities.computePerceivedBrightness(HSVToColor);
        if (z) {
            if (computePerceivedBrightness <= computePerceivedBrightness2) {
                return HSVToColor;
            }
        } else if (computePerceivedBrightness >= computePerceivedBrightness2) {
            return HSVToColor;
        }
        return changeBrightness(HSVToColor, ((computePerceivedBrightness * 0.39999998f) / computePerceivedBrightness2) + 0.6f);
    }

    public static void checkAutoNightThemeConditions() {
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

    private static Bitmap checkBlur(Drawable drawable) {
        ColorFilter colorFilter;
        WeakReference weakReference = lastDrawableToBlur;
        if (weakReference != null && weakReference.get() == drawable) {
            return blurredBitmap;
        }
        WeakReference weakReference2 = lastDrawableToBlur;
        if (weakReference2 != null) {
            weakReference2.clear();
        }
        lastDrawableToBlur = null;
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            blurredBitmap = null;
            return null;
        }
        lastDrawableToBlur = new WeakReference(drawable);
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
        blurredBitmap = createBitmap;
        return createBitmap;
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
                        final TLRPC.TL_theme tL_theme = themeInfo.info;
                        if (tL_theme != null) {
                            i = themeInfo.account;
                        } else if (accent != null && (tL_theme = accent.info) != null) {
                            i = UserConfig.selectedAccount;
                        }
                        if (tL_theme.document != null) {
                            loadingCurrentTheme++;
                            TLRPC.TL_account_getTheme tL_account_getTheme = new TLRPC.TL_account_getTheme();
                            tL_account_getTheme.document_id = tL_theme.document.id;
                            tL_account_getTheme.format = "android";
                            TLRPC.TL_inputTheme tL_inputTheme = new TLRPC.TL_inputTheme();
                            tL_inputTheme.access_hash = tL_theme.access_hash;
                            tL_inputTheme.id = tL_theme.id;
                            tL_account_getTheme.theme = tL_inputTheme;
                            ConnectionsManager.getInstance(i).sendRequest(tL_account_getTheme, new RequestDelegate() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda15
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    Theme.lambda$checkCurrentRemoteTheme$7(Theme.ThemeAccent.this, themeInfo, tL_theme, tLObject, tL_error);
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
    public static void checkIsDark(SparseIntArray sparseIntArray, ThemeInfo themeInfo) {
        if (themeInfo == null || sparseIntArray == null || themeInfo.isDark != -1) {
            return;
        }
        int i = key_windowBackgroundWhite;
        themeInfo.isDark = ColorUtils.calculateLuminance(ColorUtils.blendARGB(getPreviewColor(sparseIntArray, i), getPreviewColor(sparseIntArray, i), 0.5f)) < 0.5d ? 1 : 0;
    }

    public static void clearPreviousTheme() {
        if (previousTheme == null) {
            return;
        }
        hasPreviousTheme = false;
        isApplyingAccent = false;
        previousTheme = null;
    }

    public static BackgroundDrawableSettings createBackgroundDrawable(ThemeInfo themeInfo, SparseIntArray sparseIntArray, String str, int i, boolean z) {
        float f;
        float f2;
        boolean z2 = themeInfo.firstAccentIsDefault && themeInfo.currentAccentId == DEFALT_THEME_ACCENT_ID;
        ThemeAccent accent = themeInfo.getAccent(false);
        File pathToWallpaper = accent != null ? accent.getPathToWallpaper() : null;
        boolean z3 = accent != null && accent.patternMotion;
        OverrideWallpaperInfo overrideWallpaperInfo = themeInfo.overrideWallpaper;
        if (overrideWallpaperInfo != null) {
            f2 = overrideWallpaperInfo.intensity;
        } else {
            if (accent == null) {
                f = themeInfo.patternIntensity;
                return createBackgroundDrawable(themeInfo, overrideWallpaperInfo, sparseIntArray, pathToWallpaper, str, currentColorsNoAccent.get(key_wallpaperFileOffset, -1), (int) f, i, z2, false, false, z3, null, z);
            }
            f2 = accent.patternIntensity;
        }
        f = f2 * 100.0f;
        return createBackgroundDrawable(themeInfo, overrideWallpaperInfo, sparseIntArray, pathToWallpaper, str, currentColorsNoAccent.get(key_wallpaperFileOffset, -1), (int) f, i, z2, false, false, z3, null, z);
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0040, code lost:
    
        if (r25.isColor() == false) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0042, code lost:
    
        r13 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x0056, code lost:
    
        if (r24.patternBgColor != 0) goto L19;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0225  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0340  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x0354  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00fa  */
    /* JADX WARN: Type inference failed for: r1v35, types: [org.telegram.ui.Components.MotionBackgroundDrawable] */
    /* JADX WARN: Type inference failed for: r32v0, types: [boolean] */
    /* JADX WARN: Type inference failed for: r32v11 */
    /* JADX WARN: Type inference failed for: r32v3 */
    /* JADX WARN: Type inference failed for: r32v4 */
    /* JADX WARN: Type inference failed for: r32v5 */
    /* JADX WARN: Type inference failed for: r32v6 */
    /* JADX WARN: Type inference failed for: r32v7 */
    /* JADX WARN: Type inference failed for: r32v8 */
    /* JADX WARN: Type inference failed for: r32v9 */
    /* JADX WARN: Type inference failed for: r3v14, types: [org.telegram.ui.Components.MotionBackgroundDrawable] */
    /* JADX WARN: Type inference failed for: r3v3, types: [org.telegram.ui.Components.BackgroundGradientDrawable] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static BackgroundDrawableSettings createBackgroundDrawable(ThemeInfo themeInfo, OverrideWallpaperInfo overrideWallpaperInfo, SparseIntArray sparseIntArray, File file, String str, int i, int i2, int i3, boolean z, boolean z2, boolean z3, boolean z4, TLRPC.Document document, boolean z5) {
        boolean z6;
        int height;
        int i4;
        Bitmap loadScreenSizedBitmap;
        ColorDrawable colorDrawable;
        Bitmap loadScreenSizedBitmap2;
        Boolean bool;
        int i5;
        boolean z7;
        BackgroundGradientDrawable backgroundGradientDrawable;
        Bitmap bitmap;
        MotionBackgroundDrawable motionBackgroundDrawable;
        Bitmap decodeFile;
        BackgroundDrawableSettings backgroundDrawableSettings = new BackgroundDrawableSettings();
        backgroundDrawableSettings.wallpaper = z5 ? null : wallpaper;
        boolean z8 = (!z2 || z3) && overrideWallpaperInfo != null;
        if (overrideWallpaperInfo != null) {
            backgroundDrawableSettings.isWallpaperMotion = Boolean.valueOf(overrideWallpaperInfo.isMotion);
            if (overrideWallpaperInfo.color != 0) {
                if (!overrideWallpaperInfo.isDefault()) {
                }
            }
            z6 = false;
        } else {
            backgroundDrawableSettings.isWallpaperMotion = Boolean.valueOf(themeInfo.isMotion);
        }
        backgroundDrawableSettings.isPatternWallpaper = Boolean.valueOf(z6);
        if (!z8) {
            int i6 = z != 0 ? 0 : sparseIntArray.get(key_chat_wallpaper);
            int i7 = sparseIntArray.get(key_chat_wallpaper_gradient_to3);
            int i8 = sparseIntArray.get(key_chat_wallpaper_gradient_to2);
            int i9 = sparseIntArray.get(key_chat_wallpaper_gradient_to1);
            if (file == null || !file.exists()) {
                i5 = i6;
                z7 = false;
            } else {
                try {
                    if (i6 == 0 || i9 == 0 || i8 == 0) {
                        z = i6;
                        backgroundDrawableSettings.wallpaper = Drawable.createFromPath(file.getAbsolutePath());
                        z7 = true;
                    } else {
                        try {
                            z = i6;
                            motionBackgroundDrawable = new MotionBackgroundDrawable(z == true ? 1 : 0, i9, i8, i7, false);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            Bitmap.Config config = Bitmap.Config.ALPHA_8;
                            options.inPreferredConfig = config;
                            decodeFile = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                            if (decodeFile != null && decodeFile.getConfig() != config) {
                                Bitmap copy = decodeFile.copy(config, false);
                                decodeFile.recycle();
                                decodeFile = copy;
                            }
                            z7 = decodeFile != null;
                        } catch (Throwable th) {
                            th = th;
                            z = i6;
                            z7 = true;
                            FileLog.e(th);
                            i5 = z;
                            if (!z7) {
                            }
                            if (backgroundDrawableSettings.wallpaper == null) {
                            }
                            if (!LiteMode.isEnabled(32)) {
                            }
                            return backgroundDrawableSettings;
                        }
                        try {
                            motionBackgroundDrawable.setPatternBitmap(i2, decodeFile);
                            motionBackgroundDrawable.setPatternColorFilter(motionBackgroundDrawable.getPatternColor());
                            backgroundDrawableSettings.wallpaper = motionBackgroundDrawable;
                            z = z;
                        } catch (Throwable th2) {
                            th = th2;
                            FileLog.e(th);
                            i5 = z;
                            if (!z7) {
                            }
                            if (backgroundDrawableSettings.wallpaper == null) {
                            }
                            if (!LiteMode.isEnabled(32)) {
                            }
                            return backgroundDrawableSettings;
                        }
                    }
                    backgroundDrawableSettings.isWallpaperMotion = Boolean.valueOf(z4);
                    Boolean bool2 = Boolean.TRUE;
                    backgroundDrawableSettings.isPatternWallpaper = bool2;
                    backgroundDrawableSettings.isCustomTheme = bool2;
                    i5 = z;
                } catch (Throwable th3) {
                    th = th3;
                }
            }
            if (!z7) {
                int i10 = i5;
                if (i10 != 0) {
                    int i11 = sparseIntArray.get(key_chat_wallpaper_gradient_rotation, -1);
                    if (i11 == -1) {
                        i11 = 45;
                    }
                    if (i9 != 0 && i8 != 0) {
                        ?? motionBackgroundDrawable2 = new MotionBackgroundDrawable(i10, i9, i8, i7, false);
                        if (file != null) {
                            bitmap = document != null ? SvgHelper.getBitmap(FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(document, true), AndroidUtilities.dp(360.0f), AndroidUtilities.dp(640.0f), false) : SvgHelper.getBitmap(org.telegram.messenger.R.raw.default_pattern, AndroidUtilities.dp(360.0f), AndroidUtilities.dp(640.0f), -1);
                            if (bitmap != null) {
                                try {
                                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                                    Bitmap copy2 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                                    copy2.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                                    copy2.recycle();
                                    fileOutputStream.close();
                                } catch (Exception e) {
                                    FileLog.e(e);
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            bitmap = null;
                        }
                        motionBackgroundDrawable2.setPatternBitmap(i2, bitmap);
                        motionBackgroundDrawable2.setPhase(i3);
                        backgroundGradientDrawable = motionBackgroundDrawable2;
                    } else if (i9 == 0 || i9 == i10) {
                        backgroundDrawableSettings.wallpaper = new ColorDrawable(i10);
                        backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
                    } else {
                        BackgroundGradientDrawable backgroundGradientDrawable2 = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(i11), new int[]{i10, i9});
                        backgroundGradientDisposable = backgroundGradientDrawable2.startDithering(BackgroundGradientDrawable.Sizes.ofDeviceScreen(), new BackgroundGradientDrawable.ListenerAdapter() { // from class: org.telegram.ui.ActionBar.Theme.11
                            @Override // org.telegram.ui.Components.BackgroundGradientDrawable.ListenerAdapter, org.telegram.ui.Components.BackgroundGradientDrawable.Listener
                            public void onSizeReady(int i12, int i13) {
                                Point point = AndroidUtilities.displaySize;
                                if ((point.x <= point.y) == (i12 <= i13)) {
                                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetNewWallpapper, new Object[0]);
                                }
                            }
                        }, 100L);
                        backgroundGradientDrawable = backgroundGradientDrawable2;
                    }
                    backgroundDrawableSettings.wallpaper = backgroundGradientDrawable;
                    backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
                } else if (str != null) {
                    try {
                        Bitmap loadScreenSizedBitmap3 = loadScreenSizedBitmap(new FileInputStream(new File(ApplicationLoader.getFilesDirFixed(), Utilities.MD5(str) + ".wp")), 0);
                        if (loadScreenSizedBitmap3 != null) {
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(loadScreenSizedBitmap3);
                            backgroundDrawableSettings.wallpaper = bitmapDrawable;
                            backgroundDrawableSettings.themedWallpaper = bitmapDrawable;
                            backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
                        }
                    } catch (Exception e2) {
                        th = e2;
                        FileLog.e(th);
                        if (backgroundDrawableSettings.wallpaper == null) {
                        }
                        if (!LiteMode.isEnabled(32)) {
                        }
                        return backgroundDrawableSettings;
                    }
                } else if (i > 0 && (themeInfo.pathToFile != null || themeInfo.assetName != null)) {
                    try {
                        String str2 = themeInfo.assetName;
                        Bitmap loadScreenSizedBitmap4 = loadScreenSizedBitmap(new FileInputStream(str2 != null ? getAssetFile(str2) : new File(themeInfo.pathToFile)), i);
                        if (loadScreenSizedBitmap4 != null) {
                            BitmapDrawable bitmapDrawable2 = new BitmapDrawable(loadScreenSizedBitmap4);
                            wallpaper = bitmapDrawable2;
                            backgroundDrawableSettings.themedWallpaper = bitmapDrawable2;
                            backgroundDrawableSettings.wallpaper = bitmapDrawable2;
                            bitmapDrawable2.setFilterBitmap(true);
                            backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        FileLog.e(th);
                        if (backgroundDrawableSettings.wallpaper == null) {
                        }
                        if (!LiteMode.isEnabled(32)) {
                        }
                        return backgroundDrawableSettings;
                    }
                }
            }
        }
        if (backgroundDrawableSettings.wallpaper == null) {
            int i12 = overrideWallpaperInfo != null ? overrideWallpaperInfo.color : 0;
            if (overrideWallpaperInfo != null) {
                if (!overrideWallpaperInfo.isDefault()) {
                    if (overrideWallpaperInfo.isColor()) {
                        if (overrideWallpaperInfo.gradientColor1 != 0) {
                        }
                        if (backgroundDrawableSettings.wallpaper == null) {
                            if (i12 == 0) {
                                i12 = -2693905;
                            }
                            backgroundDrawableSettings.wallpaper = new ColorDrawable(i12);
                        }
                    }
                    if (i12 == 0 || (isPatternWallpaper && overrideWallpaperInfo.gradientColor2 == 0)) {
                        File file2 = new File(ApplicationLoader.getFilesDirFixed(), overrideWallpaperInfo.fileName);
                        if (file2.exists() && (loadScreenSizedBitmap = loadScreenSizedBitmap(new FileInputStream(file2), 0)) != null) {
                            BitmapDrawable bitmapDrawable3 = new BitmapDrawable(loadScreenSizedBitmap);
                            backgroundDrawableSettings.wallpaper = bitmapDrawable3;
                            bitmapDrawable3.setFilterBitmap(true);
                            backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
                        }
                        if (backgroundDrawableSettings.wallpaper == null) {
                            backgroundDrawableSettings.wallpaper = createDefaultWallpaper();
                            bool = Boolean.FALSE;
                            backgroundDrawableSettings.isCustomTheme = bool;
                        }
                    } else {
                        if (overrideWallpaperInfo.gradientColor1 != 0 && overrideWallpaperInfo.gradientColor2 != 0) {
                            ?? motionBackgroundDrawable3 = new MotionBackgroundDrawable(overrideWallpaperInfo.color, overrideWallpaperInfo.gradientColor1, overrideWallpaperInfo.gradientColor2, overrideWallpaperInfo.gradientColor3, false);
                            motionBackgroundDrawable3.setPhase(i3);
                            colorDrawable = motionBackgroundDrawable3;
                            if (backgroundDrawableSettings.isPatternWallpaper.booleanValue()) {
                                File file3 = new File(ApplicationLoader.getFilesDirFixed(), overrideWallpaperInfo.fileName);
                                colorDrawable = motionBackgroundDrawable3;
                                if (file3.exists()) {
                                    motionBackgroundDrawable3.setPatternBitmap((int) (overrideWallpaperInfo.intensity * 100.0f), loadScreenSizedBitmap(new FileInputStream(file3), 0));
                                    backgroundDrawableSettings.isCustomTheme = Boolean.TRUE;
                                    colorDrawable = motionBackgroundDrawable3;
                                }
                            }
                        } else if (backgroundDrawableSettings.isPatternWallpaper.booleanValue()) {
                            File file4 = new File(ApplicationLoader.getFilesDirFixed(), overrideWallpaperInfo.fileName);
                            if (file4.exists() && (loadScreenSizedBitmap2 = loadScreenSizedBitmap(new FileInputStream(file4), 0)) != null) {
                                BitmapDrawable bitmapDrawable4 = new BitmapDrawable(loadScreenSizedBitmap2);
                                backgroundDrawableSettings.wallpaper = bitmapDrawable4;
                                bitmapDrawable4.setFilterBitmap(true);
                                bool = Boolean.TRUE;
                                backgroundDrawableSettings.isCustomTheme = bool;
                            }
                        } else {
                            int i13 = overrideWallpaperInfo.gradientColor1;
                            if (i13 != 0) {
                                ?? backgroundGradientDrawable3 = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(overrideWallpaperInfo.rotation), new int[]{i12, i13});
                                backgroundGradientDisposable = backgroundGradientDrawable3.startDithering(BackgroundGradientDrawable.Sizes.ofDeviceScreen(), new BackgroundGradientDrawable.ListenerAdapter() { // from class: org.telegram.ui.ActionBar.Theme.12
                                    @Override // org.telegram.ui.Components.BackgroundGradientDrawable.ListenerAdapter, org.telegram.ui.Components.BackgroundGradientDrawable.Listener
                                    public void onSizeReady(int i14, int i15) {
                                        Point point = AndroidUtilities.displaySize;
                                        if ((point.x <= point.y) == (i14 <= i15)) {
                                            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetNewWallpapper, new Object[0]);
                                        }
                                    }
                                }, 100L);
                                colorDrawable = backgroundGradientDrawable3;
                            } else {
                                colorDrawable = new ColorDrawable(i12);
                            }
                        }
                        backgroundDrawableSettings.wallpaper = colorDrawable;
                    }
                    if (backgroundDrawableSettings.wallpaper == null) {
                    }
                }
            }
            backgroundDrawableSettings.wallpaper = createDefaultWallpaper();
            bool = Boolean.FALSE;
            backgroundDrawableSettings.isCustomTheme = bool;
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

    public static void createChatResources(Context context, boolean z) {
        TextPaint textPaint;
        createCommonChatResources();
        if (!z && chat_msgInDrawable == null) {
            Resources resources = context.getResources();
            chat_msgNoSoundDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.video_muted);
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
            int i = org.telegram.messenger.R.drawable.msg_check_s;
            chat_msgOutCheckDrawable = resources.getDrawable(i).mutate();
            chat_msgOutCheckSelectedDrawable = resources.getDrawable(i).mutate();
            chat_msgOutCheckReadDrawable = resources.getDrawable(i).mutate();
            chat_msgOutCheckReadSelectedDrawable = resources.getDrawable(i).mutate();
            chat_msgMediaCheckDrawable = resources.getDrawable(i).mutate();
            chat_msgStickerCheckDrawable = resources.getDrawable(i).mutate();
            int i2 = org.telegram.messenger.R.drawable.msg_halfcheck;
            chat_msgOutHalfCheckDrawable = resources.getDrawable(i2).mutate();
            chat_msgOutHalfCheckSelectedDrawable = resources.getDrawable(i2).mutate();
            int i3 = org.telegram.messenger.R.drawable.msg_halfcheck_s;
            chat_msgMediaHalfCheckDrawable = resources.getDrawable(i3).mutate();
            chat_msgStickerHalfCheckDrawable = resources.getDrawable(i3).mutate();
            chat_msgClockDrawable = new MsgClockDrawable();
            int i4 = org.telegram.messenger.R.drawable.ic_lock_header;
            chat_msgUnlockDrawable = resources.getDrawable(i4).mutate();
            int i5 = org.telegram.messenger.R.drawable.msg_views;
            chat_msgInViewsDrawable = resources.getDrawable(i5).mutate();
            chat_msgInViewsSelectedDrawable = resources.getDrawable(i5).mutate();
            chat_msgOutViewsDrawable = resources.getDrawable(i5).mutate();
            chat_msgOutViewsSelectedDrawable = resources.getDrawable(i5).mutate();
            int i6 = org.telegram.messenger.R.drawable.msg_reply_small;
            chat_msgInRepliesDrawable = resources.getDrawable(i6).mutate();
            chat_msgInRepliesSelectedDrawable = resources.getDrawable(i6).mutate();
            chat_msgOutRepliesDrawable = resources.getDrawable(i6).mutate();
            chat_msgOutRepliesSelectedDrawable = resources.getDrawable(i6).mutate();
            int i7 = org.telegram.messenger.R.drawable.msg_pin_mini;
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
            int i8 = org.telegram.messenger.R.drawable.msg_actions;
            chat_msgInMenuDrawable = resources.getDrawable(i8).mutate();
            chat_msgInMenuSelectedDrawable = resources.getDrawable(i8).mutate();
            chat_msgOutMenuDrawable = resources.getDrawable(i8).mutate();
            chat_msgOutMenuSelectedDrawable = resources.getDrawable(i8).mutate();
            chat_msgMediaMenuDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.video_actions);
            int i9 = org.telegram.messenger.R.drawable.msg_instant;
            chat_msgInInstantDrawable = resources.getDrawable(i9).mutate();
            chat_msgOutInstantDrawable = resources.getDrawable(i9).mutate();
            chat_msgErrorDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.msg_warning);
            chat_muteIconDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.list_mute).mutate();
            chat_lockIconDrawable = resources.getDrawable(i4);
            Drawable[] drawableArr = chat_msgInCallDrawable;
            int i10 = org.telegram.messenger.R.drawable.chat_calls_voice;
            drawableArr[0] = resources.getDrawable(i10).mutate();
            chat_msgInCallSelectedDrawable[0] = resources.getDrawable(i10).mutate();
            chat_msgOutCallDrawable[0] = resources.getDrawable(i10).mutate();
            chat_msgOutCallSelectedDrawable[0] = resources.getDrawable(i10).mutate();
            Drawable[] drawableArr2 = chat_msgInCallDrawable;
            int i11 = org.telegram.messenger.R.drawable.chat_calls_video;
            drawableArr2[1] = resources.getDrawable(i11).mutate();
            chat_msgInCallSelectedDrawable[1] = resources.getDrawable(i11).mutate();
            chat_msgOutCallDrawable[1] = resources.getDrawable(i11).mutate();
            chat_msgOutCallSelectedDrawable[1] = resources.getDrawable(i11).mutate();
            chat_msgCallUpGreenDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.chat_calls_outgoing).mutate();
            int i12 = org.telegram.messenger.R.drawable.chat_calls_incoming;
            chat_msgCallDownRedDrawable = resources.getDrawable(i12).mutate();
            chat_msgCallDownGreenDrawable = resources.getDrawable(i12).mutate();
            for (int i13 = 0; i13 < 2; i13++) {
                chat_pollCheckDrawable[i13] = resources.getDrawable(org.telegram.messenger.R.drawable.poll_right).mutate();
                chat_pollCrossDrawable[i13] = resources.getDrawable(org.telegram.messenger.R.drawable.poll_wrong).mutate();
                chat_pollHintDrawable[i13] = resources.getDrawable(org.telegram.messenger.R.drawable.msg_emoji_objects).mutate();
                chat_psaHelpDrawable[i13] = resources.getDrawable(org.telegram.messenger.R.drawable.msg_psa).mutate();
            }
            int i14 = org.telegram.messenger.R.drawable.ic_call_made_green_18dp;
            calllog_msgCallUpRedDrawable = resources.getDrawable(i14).mutate();
            calllog_msgCallUpGreenDrawable = resources.getDrawable(i14).mutate();
            int i15 = org.telegram.messenger.R.drawable.ic_call_received_green_18dp;
            calllog_msgCallDownRedDrawable = resources.getDrawable(i15).mutate();
            calllog_msgCallDownGreenDrawable = resources.getDrawable(i15).mutate();
            chat_inlineResultFile = resources.getDrawable(org.telegram.messenger.R.drawable.bot_file);
            chat_inlineResultAudio = resources.getDrawable(org.telegram.messenger.R.drawable.bot_music);
            chat_inlineResultLocation = resources.getDrawable(org.telegram.messenger.R.drawable.bot_location);
            chat_botLinkDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.bot_link);
            chat_botInlineDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.bot_lines);
            chat_botCardDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.bot_card);
            chat_botWebViewDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.bot_webview);
            chat_botInviteDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.bot_invite);
            chat_commentDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.msg_msgbubble);
            chat_commentStickerDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.msg_msgbubble2);
            chat_commentArrowDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.msg_arrowright);
            chat_gradientLeftDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.gradient_left);
            chat_gradientRightDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.gradient_right);
            chat_contextResult_shadowUnderSwitchDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.header_shadow).mutate();
            chat_attachButtonDrawables[0] = new RLottieDrawable(org.telegram.messenger.R.raw.attach_gallery, "attach_gallery", AndroidUtilities.dp(26.0f), AndroidUtilities.dp(26.0f));
            chat_attachButtonDrawables[1] = new RLottieDrawable(org.telegram.messenger.R.raw.attach_music, "attach_music", AndroidUtilities.dp(26.0f), AndroidUtilities.dp(26.0f));
            chat_attachButtonDrawables[2] = new RLottieDrawable(org.telegram.messenger.R.raw.attach_file, "attach_file", AndroidUtilities.dp(26.0f), AndroidUtilities.dp(26.0f));
            chat_attachButtonDrawables[3] = new RLottieDrawable(org.telegram.messenger.R.raw.attach_contact, "attach_contact", AndroidUtilities.dp(26.0f), AndroidUtilities.dp(26.0f));
            chat_attachButtonDrawables[4] = new RLottieDrawable(org.telegram.messenger.R.raw.attach_location, "attach_location", AndroidUtilities.dp(26.0f), AndroidUtilities.dp(26.0f));
            chat_attachButtonDrawables[5] = new RLottieDrawable(org.telegram.messenger.R.raw.attach_poll, "attach_poll", AndroidUtilities.dp(26.0f), AndroidUtilities.dp(26.0f));
            chat_attachEmptyDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.nophotos3);
            chat_shareIconDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.filled_button_share).mutate();
            chat_replyIconDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.filled_button_reply);
            chat_closeIconDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.msg_voiceclose).mutate();
            chat_moreIconDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.media_more).mutate();
            chat_goIconDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.filled_open_message);
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
            chat_flameIcon = resources.getDrawable(org.telegram.messenger.R.drawable.filled_fire).mutate();
            chat_gifIcon = resources.getDrawable(org.telegram.messenger.R.drawable.msg_round_gif_m).mutate();
            Drawable[] drawableArr3 = chat_fileStatesDrawable[0];
            int dp2 = AndroidUtilities.dp(44.0f);
            int i17 = org.telegram.messenger.R.drawable.msg_round_play_m;
            drawableArr3[0] = createCircleDrawableWithIcon(dp2, i17);
            chat_fileStatesDrawable[0][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), i17);
            Drawable[] drawableArr4 = chat_fileStatesDrawable[1];
            int dp3 = AndroidUtilities.dp(44.0f);
            int i18 = org.telegram.messenger.R.drawable.msg_round_pause_m;
            drawableArr4[0] = createCircleDrawableWithIcon(dp3, i18);
            chat_fileStatesDrawable[1][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), i18);
            Drawable[] drawableArr5 = chat_fileStatesDrawable[2];
            int dp4 = AndroidUtilities.dp(44.0f);
            int i19 = org.telegram.messenger.R.drawable.msg_round_load_m;
            drawableArr5[0] = createCircleDrawableWithIcon(dp4, i19);
            chat_fileStatesDrawable[2][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), i19);
            Drawable[] drawableArr6 = chat_fileStatesDrawable[3];
            int dp5 = AndroidUtilities.dp(44.0f);
            int i20 = org.telegram.messenger.R.drawable.msg_round_file_s;
            drawableArr6[0] = createCircleDrawableWithIcon(dp5, i20);
            chat_fileStatesDrawable[3][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), i20);
            Drawable[] drawableArr7 = chat_fileStatesDrawable[4];
            int dp6 = AndroidUtilities.dp(44.0f);
            int i21 = org.telegram.messenger.R.drawable.msg_round_cancel_m;
            drawableArr7[0] = createCircleDrawableWithIcon(dp6, i21);
            chat_fileStatesDrawable[4][1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), i21);
            Drawable[] drawableArr8 = chat_contactDrawable;
            int dp7 = AndroidUtilities.dp(44.0f);
            int i22 = org.telegram.messenger.R.drawable.msg_contact;
            drawableArr8[0] = createCircleDrawableWithIcon(dp7, i22);
            chat_contactDrawable[1] = createCircleDrawableWithIcon(AndroidUtilities.dp(44.0f), i22);
            Drawable[] drawableArr9 = chat_locationDrawable;
            int i23 = org.telegram.messenger.R.drawable.msg_location;
            drawableArr9[0] = resources.getDrawable(i23).mutate();
            chat_locationDrawable[1] = resources.getDrawable(i23).mutate();
            chat_composeShadowDrawable = context.getResources().getDrawable(org.telegram.messenger.R.drawable.compose_panel_shadow).mutate();
            chat_composeShadowRoundDrawable = context.getResources().getDrawable(org.telegram.messenger.R.drawable.sheet_shadow_round).mutate();
            chat_roundVideoShadow = new RoundVideoShadow();
            defaultChatDrawables.clear();
            defaultChatDrawableColorKeys.clear();
            Drawable drawable = chat_botInlineDrawable;
            int i24 = key_chat_serviceIcon;
            addChatDrawable("drawableBotInline", drawable, i24);
            addChatDrawable("drawableBotWebView", chat_botWebViewDrawable, i24);
            addChatDrawable("drawableBotLink", chat_botLinkDrawable, i24);
            addChatDrawable("drawable_botInvite", chat_botInviteDrawable, i24);
            addChatDrawable("drawableGoIcon", chat_goIconDrawable, i24);
            addChatDrawable("drawableCommentSticker", chat_commentStickerDrawable, i24);
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
            int i25 = key_chat_outInstant;
            addChatDrawable("drawableMsgOutCallAudio", drawable2, i25);
            Drawable drawable3 = chat_msgOutCallSelectedDrawable[0];
            int i26 = key_chat_outInstantSelected;
            addChatDrawable("drawableMsgOutCallAudioSelected", drawable3, i26);
            addChatDrawable("drawableMsgOutCallVideo", chat_msgOutCallDrawable[1], i25);
            addChatDrawable("drawableMsgOutCallVideo", chat_msgOutCallSelectedDrawable[1], i26);
            addChatDrawable("drawableMsgOutCheck", chat_msgOutCheckDrawable, key_chat_outSentCheck);
            addChatDrawable("drawableMsgOutCheckSelected", chat_msgOutCheckSelectedDrawable, key_chat_outSentCheckSelected);
            Drawable drawable4 = chat_msgOutCheckReadDrawable;
            int i27 = key_chat_outSentCheckRead;
            addChatDrawable("drawableMsgOutCheckRead", drawable4, i27);
            Drawable drawable5 = chat_msgOutCheckReadSelectedDrawable;
            int i28 = key_chat_outSentCheckReadSelected;
            addChatDrawable("drawableMsgOutCheckReadSelected", drawable5, i28);
            addChatDrawable("drawableMsgOutHalfCheck", chat_msgOutHalfCheckDrawable, i27);
            addChatDrawable("drawableMsgOutHalfCheckSelected", chat_msgOutHalfCheckSelectedDrawable, i28);
            addChatDrawable("drawableMsgOutInstant", chat_msgOutInstantDrawable, i25);
            addChatDrawable("drawableMsgOutMenu", chat_msgOutMenuDrawable, key_chat_outMenu);
            addChatDrawable("drawableMsgOutMenuSelected", chat_msgOutMenuSelectedDrawable, key_chat_outMenuSelected);
            Drawable drawable6 = chat_msgOutPinnedDrawable;
            int i29 = key_chat_outViews;
            addChatDrawable("drawableMsgOutPinned", drawable6, i29);
            Drawable drawable7 = chat_msgOutPinnedSelectedDrawable;
            int i30 = key_chat_outViewsSelected;
            addChatDrawable("drawableMsgOutPinnedSelected", drawable7, i30);
            addChatDrawable("drawableMsgOutReplies", chat_msgOutRepliesDrawable, i29);
            addChatDrawable("drawableMsgOutReplies", chat_msgOutRepliesSelectedDrawable, i30);
            addChatDrawable("drawableMsgOutViews", chat_msgOutViewsDrawable, i29);
            addChatDrawable("drawableMsgOutViewsSelected", chat_msgOutViewsSelectedDrawable, i30);
            Drawable drawable8 = chat_msgStickerCheckDrawable;
            int i31 = key_chat_serviceText;
            addChatDrawable("drawableMsgStickerCheck", drawable8, i31);
            addChatDrawable("drawableMsgStickerHalfCheck", chat_msgStickerHalfCheckDrawable, i31);
            addChatDrawable("drawableMsgStickerPinned", chat_msgStickerPinnedDrawable, i31);
            addChatDrawable("drawableMsgStickerReplies", chat_msgStickerRepliesDrawable, i31);
            addChatDrawable("drawableMsgStickerViews", chat_msgStickerViewsDrawable, i31);
            addChatDrawable("drawableReplyIcon", chat_replyIconDrawable, i24);
            addChatDrawable("drawableCloseIcon", chat_closeIconDrawable, i24);
            addChatDrawable("drawableMoreIcon", chat_moreIconDrawable, i24);
            addChatDrawable("drawableShareIcon", chat_shareIconDrawable, i24);
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
        chat_infoBoldPaint.setTextSize(AndroidUtilities.dp(12.0f));
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
        chat_namePaint.setTextSize(AndroidUtilities.dp(r0));
        chat_replyNamePaint.setTextSize(AndroidUtilities.dp(r0));
        chat_replyTextPaint.setTextSize(AndroidUtilities.dp(r0));
        float f = (((SharedConfig.fontSize * 2) + 10) / 3.0f) - 1.0f;
        chat_topicTextPaint.setTextSize(AndroidUtilities.dp(f));
        chat_forwardNamePaint.setTextSize(AndroidUtilities.dp(r0));
        chat_adminPaint.setTextSize(AndroidUtilities.dp(f));
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
        chat_radialProgress2Paint.setStrokeWidth(AndroidUtilities.dp(2.33f));
        chat_commentTextPaint.setTextSize(AndroidUtilities.dp(14.0f));
        chat_commentTextPaint.setTypeface(AndroidUtilities.bold());
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

    public static ShapeDrawable createCircleDrawable(int i, int i2, int i3) {
        OvalShape ovalShape = new OvalShape();
        float f = i;
        ovalShape.resize(f, f);
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.setIntrinsicWidth(i);
        shapeDrawable.setIntrinsicHeight(i);
        shapeDrawable.getPaint().setShader(new LinearGradient(0.0f, 0.0f, 0.0f, f, i2, i3, Shader.TileMode.CLAMP));
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

    public static Drawable createCircleSelectorDrawable(int i, final int i2, final int i3) {
        if (Build.VERSION.SDK_INT >= 21) {
            maskPaint.setColor(-1);
            return new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, new Drawable() { // from class: org.telegram.ui.ActionBar.Theme.8
                @Override // android.graphics.drawable.Drawable
                public void draw(Canvas canvas) {
                    Rect bounds = getBounds();
                    canvas.drawCircle((bounds.centerX() - i2) + i3, bounds.centerY(), (Math.max(bounds.width(), bounds.height()) / 2) + i2 + i3, Theme.maskPaint);
                }

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
            });
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{R.attr.state_pressed}, new ColorDrawable(i));
        stateListDrawable.addState(new int[]{R.attr.state_selected}, new ColorDrawable(i));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0));
        return stateListDrawable;
    }

    public static void createCommonChatResources() {
        createCommonMessageResources();
        if (chat_infoPaint == null) {
            chat_infoPaint = new TextPaint(1);
            TextPaint textPaint = new TextPaint(1);
            chat_infoBoldPaint = textPaint;
            textPaint.setTypeface(AndroidUtilities.bold());
            TextPaint textPaint2 = new TextPaint(1);
            chat_stickerCommentCountPaint = textPaint2;
            textPaint2.setTypeface(AndroidUtilities.bold());
            TextPaint textPaint3 = new TextPaint(1);
            chat_docNamePaint = textPaint3;
            textPaint3.setTypeface(AndroidUtilities.bold());
            chat_docBackPaint = new Paint(1);
            Paint paint = new Paint(1);
            chat_deleteProgressPaint = paint;
            Paint.Style style = Paint.Style.STROKE;
            paint.setStyle(style);
            Paint paint2 = chat_deleteProgressPaint;
            Paint.Cap cap = Paint.Cap.ROUND;
            paint2.setStrokeCap(cap);
            TextPaint textPaint4 = new TextPaint(1);
            chat_locationTitlePaint = textPaint4;
            textPaint4.setTypeface(AndroidUtilities.bold());
            chat_locationAddressPaint = new TextPaint(1);
            Paint paint3 = new Paint();
            chat_urlPaint = paint3;
            paint3.setPathEffect(LinkPath.getRoundedEffect());
            Paint paint4 = new Paint();
            chat_outUrlPaint = paint4;
            paint4.setPathEffect(LinkPath.getRoundedEffect());
            Paint paint5 = new Paint();
            chat_textSearchSelectionPaint = paint5;
            paint5.setPathEffect(LinkPath.getRoundedEffect());
            Paint paint6 = new Paint(1);
            chat_radialProgressPaint = paint6;
            paint6.setStrokeCap(cap);
            chat_radialProgressPaint.setStyle(style);
            chat_radialProgressPaint.setColor(-1610612737);
            Paint paint7 = new Paint(1);
            chat_radialProgress2Paint = paint7;
            paint7.setStrokeCap(cap);
            chat_radialProgress2Paint.setStyle(style);
            chat_audioTimePaint = new TextPaint(1);
            TextPaint textPaint5 = new TextPaint(1);
            chat_livePaint = textPaint5;
            textPaint5.setTypeface(Typeface.DEFAULT_BOLD);
            TextPaint textPaint6 = new TextPaint(1);
            chat_audioTitlePaint = textPaint6;
            textPaint6.setTypeface(AndroidUtilities.bold());
            chat_audioPerformerPaint = new TextPaint(1);
            TextPaint textPaint7 = new TextPaint(1);
            chat_botButtonPaint = textPaint7;
            textPaint7.setTypeface(AndroidUtilities.bold());
            TextPaint textPaint8 = new TextPaint(1);
            chat_contactNamePaint = textPaint8;
            textPaint8.setTypeface(AndroidUtilities.bold());
            chat_contactPhonePaint = new TextPaint(1);
            chat_durationPaint = new TextPaint(1);
            TextPaint textPaint9 = new TextPaint(1);
            chat_gamePaint = textPaint9;
            textPaint9.setTypeface(AndroidUtilities.bold());
            chat_shipmentPaint = new TextPaint(1);
            chat_timePaint = new TextPaint(1);
            chat_adminPaint = new TextPaint(1);
            TextPaint textPaint10 = new TextPaint(1);
            chat_namePaint = textPaint10;
            textPaint10.setTypeface(AndroidUtilities.bold());
            chat_forwardNamePaint = new TextPaint(1);
            TextPaint textPaint11 = new TextPaint(1);
            chat_replyNamePaint = textPaint11;
            textPaint11.setTypeface(AndroidUtilities.bold());
            chat_replyTextPaint = new TextPaint(1);
            TextPaint textPaint12 = new TextPaint(1);
            chat_topicTextPaint = textPaint12;
            textPaint12.setTypeface(AndroidUtilities.bold());
            chat_titleLabelTextPaint = new TextPaint(1);
            chat_commentTextPaint = new TextPaint(1);
            TextPaint textPaint13 = new TextPaint(1);
            chat_instantViewPaint = textPaint13;
            textPaint13.setTypeface(AndroidUtilities.bold());
            Paint paint8 = new Paint(1);
            chat_instantViewRectPaint = paint8;
            paint8.setStyle(style);
            chat_instantViewRectPaint.setStrokeCap(cap);
            chat_instantViewButtonPaint = new Paint(1);
            Paint paint9 = new Paint(1);
            chat_pollTimerPaint = paint9;
            paint9.setStyle(style);
            chat_pollTimerPaint.setStrokeCap(cap);
            chat_replyLinePaint = new Paint(1);
            chat_msgErrorPaint = new Paint(1);
            chat_statusPaint = new Paint(1);
            Paint paint10 = new Paint(1);
            chat_statusRecordPaint = paint10;
            paint10.setStyle(style);
            chat_statusRecordPaint.setStrokeCap(cap);
            chat_actionTextPaint = new TextPaint(1);
            chat_actionTextPaint2 = new TextPaint(1);
            chat_actionTextPaint.setTypeface(AndroidUtilities.bold());
            TextPaint textPaint14 = new TextPaint(1);
            chat_unlockExtendedMediaTextPaint = textPaint14;
            textPaint14.setTypeface(AndroidUtilities.bold());
            Paint paint11 = new Paint(1);
            chat_actionBackgroundGradientDarkenPaint = paint11;
            paint11.setColor(352321536);
            chat_timeBackgroundPaint = new Paint(1);
            TextPaint textPaint15 = new TextPaint(1);
            chat_contextResult_titleTextPaint = textPaint15;
            textPaint15.setTypeface(AndroidUtilities.bold());
            chat_contextResult_descriptionTextPaint = new TextPaint(1);
            chat_composeBackgroundPaint = new Paint();
            chat_radialProgressPausedPaint = new Paint(1);
            chat_radialProgressPausedSeekbarPaint = new Paint(1);
            chat_messageBackgroundSelectedPaint = new Paint(1);
            chat_actionBackgroundPaint = new Paint(7);
            chat_actionBackgroundSelectedPaint = new Paint(7);
            addChatPaint("paintChatMessageBackgroundSelected", chat_messageBackgroundSelectedPaint, key_chat_selectedBackground);
            Paint paint12 = chat_actionBackgroundPaint;
            int i = key_chat_serviceBackground;
            addChatPaint("paintChatActionBackground", paint12, i);
            addChatPaint("paintChatActionBackgroundDarken", chat_actionBackgroundGradientDarkenPaint, i);
            addChatPaint("paintChatActionBackgroundSelected", chat_actionBackgroundSelectedPaint, key_chat_serviceBackgroundSelected);
            TextPaint textPaint16 = chat_actionTextPaint;
            int i2 = key_chat_serviceText;
            addChatPaint("paintChatActionText", textPaint16, i2);
            addChatPaint("paintChatActionText2", chat_actionTextPaint2, i2);
            addChatPaint("paintChatBotButton", chat_botButtonPaint, key_chat_botButtonText);
            addChatPaint("paintChatComposeBackground", chat_composeBackgroundPaint, key_chat_messagePanelBackground);
            addChatPaint("paintChatTimeBackground", chat_timeBackgroundPaint, key_chat_mediaTimeBackground);
        }
    }

    public static void createCommonDialogResources(Context context) {
        if (dialogs_countTextPaint == null) {
            TextPaint textPaint = new TextPaint(1);
            dialogs_countTextPaint = textPaint;
            textPaint.setTypeface(AndroidUtilities.bold());
            dialogs_countPaint = new Paint(1);
            dialogs_reactionsCountPaint = new Paint(1);
            dialogs_onlineCirclePaint = new Paint(1);
            dialogs_tagPaint = new Paint(1);
        }
        dialogs_countTextPaint.setTextSize(AndroidUtilities.dp(13.0f));
    }

    public static void createCommonMessageResources() {
        synchronized (sync) {
            try {
                if (chat_msgTextPaint == null) {
                    chat_msgTextPaint = new TextPaint(1);
                    chat_msgGameTextPaint = new TextPaint(1);
                    chat_msgTextPaintEmoji = new TextPaint[6];
                    chat_msgTextPaintOneEmoji = new TextPaint(1);
                    chat_msgTextPaintTwoEmoji = new TextPaint(1);
                    chat_msgTextPaintThreeEmoji = new TextPaint(1);
                    TextPaint textPaint = new TextPaint(1);
                    chat_msgBotButtonPaint = textPaint;
                    textPaint.setTypeface(AndroidUtilities.bold());
                    TextPaint textPaint2 = new TextPaint(1);
                    chat_namePaint = textPaint2;
                    textPaint2.setTypeface(AndroidUtilities.bold());
                    TextPaint textPaint3 = new TextPaint(1);
                    chat_replyNamePaint = textPaint3;
                    textPaint3.setTypeface(AndroidUtilities.bold());
                    chat_replyTextPaint = new TextPaint(1);
                    chat_quoteTextPaint = new TextPaint(1);
                    chat_titleLabelTextPaint = new TextPaint(1);
                    TextPaint textPaint4 = new TextPaint(1);
                    chat_topicTextPaint = textPaint4;
                    textPaint4.setTypeface(AndroidUtilities.bold());
                    chat_forwardNamePaint = new TextPaint(1);
                    chat_adminPaint = new TextPaint(1);
                    chat_timePaint = new TextPaint(1);
                    TextPaint textPaint5 = new TextPaint(1);
                    chat_msgTextCodePaint = textPaint5;
                    Typeface typeface = Typeface.MONOSPACE;
                    textPaint5.setTypeface(typeface);
                    TextPaint textPaint6 = new TextPaint(1);
                    chat_msgTextCode2Paint = textPaint6;
                    textPaint6.setTypeface(typeface);
                    TextPaint textPaint7 = new TextPaint(1);
                    chat_msgTextCode3Paint = textPaint7;
                    textPaint7.setTypeface(typeface);
                    chat_msgCodeBgPaint = new TextPaint(1);
                }
                float[] fArr = {0.68f, 0.46f, 0.34f, 0.28f, 0.22f, 0.19f};
                int i = 0;
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
                        chat_namePaint.setTextSize(AndroidUtilities.dp(r0));
                        chat_replyNamePaint.setTextSize(AndroidUtilities.dp(r0));
                        chat_replyTextPaint.setTextSize(AndroidUtilities.dp(r0));
                        float f = (((SharedConfig.fontSize * 2) + 10) / 3.0f) - 1.0f;
                        chat_quoteTextPaint.setTextSize(AndroidUtilities.dp(f));
                        chat_topicTextPaint.setTextSize(AndroidUtilities.dp(f));
                        chat_titleLabelTextPaint.setTextSize(AndroidUtilities.dp(r0 - 2.0f));
                        chat_forwardNamePaint.setTextSize(AndroidUtilities.dp(r0));
                        chat_adminPaint.setTextSize(AndroidUtilities.dp(f));
                        chat_msgTextCodePaint.setTextSize(AndroidUtilities.dp(Math.max(Math.min(10, SharedConfig.fontSize - 1), SharedConfig.fontSize - 2)));
                        chat_msgTextCode2Paint.setTextSize(AndroidUtilities.dp(Math.max(Math.min(10, SharedConfig.fontSize - 2), SharedConfig.fontSize - 3)));
                        chat_msgTextCode3Paint.setTextSize(AndroidUtilities.dp(Math.max(Math.min(10, SharedConfig.fontSize - 2), SharedConfig.fontSize - 5)));
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
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
            avatarDrawables[0] = resources.getDrawable(org.telegram.messenger.R.drawable.chats_saved);
            avatarDrawables[1] = resources.getDrawable(org.telegram.messenger.R.drawable.ghost);
            Drawable[] drawableArr = avatarDrawables;
            int i = org.telegram.messenger.R.drawable.msg_folders_private;
            drawableArr[2] = resources.getDrawable(i);
            avatarDrawables[3] = resources.getDrawable(org.telegram.messenger.R.drawable.msg_folders_requests);
            avatarDrawables[4] = resources.getDrawable(org.telegram.messenger.R.drawable.msg_folders_groups);
            avatarDrawables[5] = resources.getDrawable(org.telegram.messenger.R.drawable.msg_folders_channels);
            avatarDrawables[6] = resources.getDrawable(org.telegram.messenger.R.drawable.msg_folders_bots);
            avatarDrawables[7] = resources.getDrawable(org.telegram.messenger.R.drawable.msg_folders_muted);
            avatarDrawables[8] = resources.getDrawable(org.telegram.messenger.R.drawable.msg_folders_read);
            avatarDrawables[9] = resources.getDrawable(org.telegram.messenger.R.drawable.msg_folders_archive);
            avatarDrawables[10] = resources.getDrawable(i);
            avatarDrawables[11] = resources.getDrawable(org.telegram.messenger.R.drawable.chats_replies);
            avatarDrawables[12] = resources.getDrawable(org.telegram.messenger.R.drawable.other_chats);
            avatarDrawables[13] = resources.getDrawable(org.telegram.messenger.R.drawable.msg_stories_closefriends);
            avatarDrawables[14] = resources.getDrawable(org.telegram.messenger.R.drawable.filled_gift_premium);
            avatarDrawables[15] = resources.getDrawable(org.telegram.messenger.R.drawable.filled_unknown);
            avatarDrawables[16] = resources.getDrawable(org.telegram.messenger.R.drawable.filled_unclaimed);
            avatarDrawables[17] = resources.getDrawable(org.telegram.messenger.R.drawable.large_repost_story);
            avatarDrawables[18] = resources.getDrawable(org.telegram.messenger.R.drawable.large_hidden);
            avatarDrawables[19] = resources.getDrawable(org.telegram.messenger.R.drawable.large_notes);
            avatarDrawables[20] = resources.getDrawable(org.telegram.messenger.R.drawable.filled_folder_new);
            avatarDrawables[21] = resources.getDrawable(org.telegram.messenger.R.drawable.filled_folder_existing);
            avatarDrawables[22] = resources.getDrawable(org.telegram.messenger.R.drawable.filled_giveaway_premium);
            avatarDrawables[23] = resources.getDrawable(org.telegram.messenger.R.drawable.filled_giveaway_stars);
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
            dialogs_archiveAvatarDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.chats_archiveavatar, "chats_archiveavatar", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_archiveDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.chats_archive, "chats_archive", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_unarchiveDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.chats_unarchive, "chats_unarchive", AndroidUtilities.dp(AndroidUtilities.dp(36.0f)), AndroidUtilities.dp(36.0f), false, null);
            dialogs_pinArchiveDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.chats_hide, "chats_hide", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_unpinArchiveDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.chats_unhide, "chats_unhide", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_hidePsaDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.chat_audio_record_delete, "chats_psahide", AndroidUtilities.dp(30.0f), AndroidUtilities.dp(30.0f), false, null);
            dialogs_swipeMuteDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.swipe_mute, "swipe_mute", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_swipeUnmuteDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.swipe_unmute, "swipe_unmute", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_swipeReadDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.swipe_read, "swipe_read", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_swipeUnreadDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.swipe_unread, "swipe_unread", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_swipeDeleteDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.swipe_delete, "swipe_delete", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_swipeUnpinDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.swipe_unpin, "swipe_unpin", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            dialogs_swipePinDrawable = new RLottieDrawable(org.telegram.messenger.R.raw.swipe_pin, "swipe_pin", AndroidUtilities.dp(36.0f), AndroidUtilities.dp(36.0f), false, null);
            applyCommonTheme();
        }
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
        motionBackgroundDrawable.setPatternBitmap(34, SvgHelper.getBitmap(org.telegram.messenger.R.raw.default_pattern, i, i2, -16777216));
        motionBackgroundDrawable.setPatternColorFilter(motionBackgroundDrawable.getPatternColor());
        return motionBackgroundDrawable;
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
                dialogs_namePaint[i].setTypeface(AndroidUtilities.bold());
                dialogs_nameEncryptedPaint[i] = new TextPaint(1);
                dialogs_nameEncryptedPaint[i].setTypeface(AndroidUtilities.bold());
                dialogs_messagePaint[i] = new TextPaint(1);
                dialogs_messagePrintingPaint[i] = new TextPaint(1);
            }
            TextPaint textPaint = new TextPaint(1);
            dialogs_searchNamePaint = textPaint;
            textPaint.setTypeface(AndroidUtilities.bold());
            TextPaint textPaint2 = new TextPaint(1);
            dialogs_searchNameEncryptedPaint = textPaint2;
            textPaint2.setTypeface(AndroidUtilities.bold());
            TextPaint textPaint3 = new TextPaint(1);
            dialogs_messageNamePaint = textPaint3;
            textPaint3.setTypeface(AndroidUtilities.bold());
            dialogs_timePaint = new TextPaint(1);
            TextPaint textPaint4 = new TextPaint(1);
            dialogs_archiveTextPaint = textPaint4;
            textPaint4.setTypeface(AndroidUtilities.bold());
            TextPaint textPaint5 = new TextPaint(1);
            dialogs_archiveTextPaintSmall = textPaint5;
            textPaint5.setTypeface(AndroidUtilities.bold());
            dialogs_onlinePaint = new TextPaint(1);
            dialogs_offlinePaint = new TextPaint(1);
            TextPaint textPaint6 = new TextPaint(1);
            dialogs_tagTextPaint = textPaint6;
            textPaint6.setTypeface(AndroidUtilities.bold());
            dialogs_tabletSeletedPaint = new Paint();
            dialogs_pinnedPaint = new Paint(1);
            dialogs_countGrayPaint = new Paint(1);
            dialogs_errorPaint = new Paint(1);
            dialogs_actionMessagePaint = new Paint(1);
            dialogs_lockDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.list_secret);
            dialogs_lock2Drawable = resources.getDrawable(org.telegram.messenger.R.drawable.msg_mini_lock2);
            int i2 = org.telegram.messenger.R.drawable.list_check;
            dialogs_checkDrawable = resources.getDrawable(i2).mutate();
            dialogs_playDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.minithumb_play).mutate();
            dialogs_checkReadDrawable = resources.getDrawable(i2).mutate();
            dialogs_halfCheckDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.list_halfcheck);
            dialogs_clockDrawable = new MsgClockDrawable();
            dialogs_errorDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.list_warning_sign);
            dialogs_reorderDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.list_reorder).mutate();
            dialogs_muteDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.list_mute).mutate();
            dialogs_unmuteDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.list_unmute).mutate();
            dialogs_verifiedDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.verified_area).mutate();
            dialogs_scamDrawable = new ScamDrawable(11, 0);
            dialogs_fakeDrawable = new ScamDrawable(11, 1);
            dialogs_verifiedCheckDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.verified_check).mutate();
            dialogs_mentionDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.mentionchatslist);
            dialogs_reactionsMentionDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.reactionchatslist);
            dialogs_pinnedDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.list_pin);
            dialogs_forum_arrowDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.msg_mini_forumarrow);
            moveUpDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.preview_arrow);
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
        dialogs_tagTextPaint.setTextSize(AndroidUtilities.dp(10.0f));
        dialogs_searchNamePaint.setTextSize(AndroidUtilities.dp(16.0f));
        dialogs_searchNameEncryptedPaint.setTextSize(AndroidUtilities.dp(16.0f));
    }

    public static Drawable createEditTextDrawable(Context context, int i, int i2) {
        Resources resources = context.getResources();
        Drawable mutate = resources.getDrawable(org.telegram.messenger.R.drawable.search_dark).mutate();
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
        mutate.setColorFilter(new PorterDuffColorFilter(i, mode));
        Drawable mutate2 = resources.getDrawable(org.telegram.messenger.R.drawable.search_dark_activated).mutate();
        mutate2.setColorFilter(new PorterDuffColorFilter(i2, mode));
        StateListDrawable stateListDrawable = new StateListDrawable() { // from class: org.telegram.ui.ActionBar.Theme.4
            /* JADX WARN: Removed duplicated region for block: B:9:0x002d  */
            @Override // android.graphics.drawable.DrawableContainer
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean selectDrawable(int i3) {
                ColorFilter colorFilter;
                Paint paint;
                if (Build.VERSION.SDK_INT >= 21) {
                    return super.selectDrawable(i3);
                }
                Drawable stateDrawable = Theme.getStateDrawable(this, i3);
                if (stateDrawable instanceof BitmapDrawable) {
                    paint = ((BitmapDrawable) stateDrawable).getPaint();
                } else {
                    if (!(stateDrawable instanceof NinePatchDrawable)) {
                        colorFilter = null;
                        boolean selectDrawable = super.selectDrawable(i3);
                        if (colorFilter != null) {
                            stateDrawable.setColorFilter(colorFilter);
                        }
                        return selectDrawable;
                    }
                    paint = ((NinePatchDrawable) stateDrawable).getPaint();
                }
                colorFilter = paint.getColorFilter();
                boolean selectDrawable2 = super.selectDrawable(i3);
                if (colorFilter != null) {
                }
                return selectDrawable2;
            }
        };
        stateListDrawable.addState(new int[]{R.attr.state_enabled, R.attr.state_focused}, mutate2);
        stateListDrawable.addState(new int[]{R.attr.state_focused}, mutate2);
        stateListDrawable.addState(StateSet.WILD_CARD, mutate);
        return stateListDrawable;
    }

    public static Drawable createEditTextDrawable(Context context, boolean z) {
        return createEditTextDrawable(context, getColor(z ? key_dialogInputField : key_windowBackgroundWhiteInputField), getColor(z ? key_dialogInputFieldActivated : key_windowBackgroundWhiteInputFieldActivated));
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
            /* JADX WARN: Removed duplicated region for block: B:9:0x002d  */
            @Override // android.graphics.drawable.DrawableContainer
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean selectDrawable(int i4) {
                ColorFilter colorFilter;
                Paint paint;
                if (Build.VERSION.SDK_INT >= 21) {
                    return super.selectDrawable(i4);
                }
                Drawable stateDrawable = Theme.getStateDrawable(this, i4);
                if (stateDrawable instanceof BitmapDrawable) {
                    paint = ((BitmapDrawable) stateDrawable).getPaint();
                } else {
                    if (!(stateDrawable instanceof NinePatchDrawable)) {
                        colorFilter = null;
                        boolean selectDrawable = super.selectDrawable(i4);
                        if (colorFilter != null) {
                            stateDrawable.setColorFilter(colorFilter);
                        }
                        return selectDrawable;
                    }
                    paint = ((NinePatchDrawable) stateDrawable).getPaint();
                }
                colorFilter = paint.getColorFilter();
                boolean selectDrawable2 = super.selectDrawable(i4);
                if (colorFilter != null) {
                }
                return selectDrawable2;
            }
        };
        stateListDrawable.setEnterFadeDuration(1);
        stateListDrawable.setExitFadeDuration(NotificationCenter.storyQualityUpdate);
        stateListDrawable.addState(new int[]{R.attr.state_selected}, mutate2);
        stateListDrawable.addState(new int[0], mutate);
        return stateListDrawable;
    }

    public static ThemeInfo createNewTheme(String str) {
        ThemeInfo themeInfo = new ThemeInfo();
        themeInfo.pathToFile = new File(ApplicationLoader.getFilesDirFixed(), "theme" + Utilities.random.nextLong() + ".attheme").getAbsolutePath();
        themeInfo.name = str;
        themedWallpaperLink = getWallpaperUrl(currentTheme.overrideWallpaper);
        themeInfo.account = UserConfig.selectedAccount;
        saveCurrentTheme(themeInfo, true, true, false);
        return themeInfo;
    }

    public static void createProfileResources(Context context) {
        if (profile_verifiedDrawable == null) {
            profile_aboutTextPaint = new TextPaint(1);
            Resources resources = context.getResources();
            profile_verifiedDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.verified_area).mutate();
            profile_verifiedCheckDrawable = resources.getDrawable(org.telegram.messenger.R.drawable.verified_check).mutate();
            applyProfileTheme();
        }
        profile_aboutTextPaint.setTextSize(AndroidUtilities.dp(16.0f));
    }

    public static Drawable createRadSelectorDrawable(int i, int i2, int i3) {
        if (Build.VERSION.SDK_INT >= 21) {
            maskPaint.setColor(-1);
            return new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, new RippleRadMaskDrawable(i2, i3));
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{R.attr.state_pressed}, new ColorDrawable(i));
        stateListDrawable.addState(new int[]{R.attr.state_selected}, new ColorDrawable(i));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0));
        return stateListDrawable;
    }

    public static Drawable createRadSelectorDrawable(int i, int i2, int i3, int i4) {
        if (Build.VERSION.SDK_INT >= 21) {
            maskPaint.setColor(-1);
            float f = i3;
            float f2 = i4;
            return new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i2}), createRoundRectDrawable(AndroidUtilities.dp(f), AndroidUtilities.dp(f2), i), new RippleRadMaskDrawable(f, f2));
        }
        float f3 = i3;
        float f4 = i4;
        ShapeDrawable createRoundRectDrawable = createRoundRectDrawable(AndroidUtilities.dp(f3), AndroidUtilities.dp(f4), i);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{createRoundRectDrawable, createRoundRectDrawable(AndroidUtilities.dp(f3), AndroidUtilities.dp(f4), i2)});
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{R.attr.state_pressed}, layerDrawable);
        stateListDrawable.addState(new int[]{R.attr.state_selected}, layerDrawable);
        stateListDrawable.addState(StateSet.WILD_CARD, createRoundRectDrawable);
        return stateListDrawable;
    }

    public static Drawable createRadSelectorDrawable(int i, int i2, int i3, int i4, int i5) {
        if (Build.VERSION.SDK_INT >= 21) {
            maskPaint.setColor(-1);
            return new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, new RippleRadMaskDrawable(i2, i3, i4, i5));
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{R.attr.state_pressed}, new ColorDrawable(i));
        stateListDrawable.addState(new int[]{R.attr.state_selected}, new ColorDrawable(i));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0));
        return stateListDrawable;
    }

    public static ShapeDrawable createRoundRectDrawable(int i, int i2) {
        float f = i;
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f, f, f, f}, null, null));
        shapeDrawable.getPaint().setColor(i2);
        return shapeDrawable;
    }

    public static ShapeDrawable createRoundRectDrawable(int i, int i2, int i3) {
        float f = i;
        float f2 = i2;
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{f, f, f, f, f2, f2, f2, f2}, null, null));
        shapeDrawable.getPaint().setColor(i3);
        return shapeDrawable;
    }

    public static Drawable createSelectorDrawable(int i) {
        return createSelectorDrawable(i, 1, -1);
    }

    public static Drawable createSelectorDrawable(int i, int i2) {
        return createSelectorDrawable(i, i2, -1);
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x004f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Drawable createSelectorDrawable(int i, final int i2, final int i3) {
        Drawable drawable;
        int i4 = Build.VERSION.SDK_INT;
        if (i4 < 21) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{R.attr.state_pressed}, new ColorDrawable(i));
            stateListDrawable.addState(new int[]{R.attr.state_selected}, new ColorDrawable(i));
            stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0));
            return stateListDrawable;
        }
        if ((i2 != 1 && i2 != 5) || i4 < 23) {
            if (i2 == 1 || i2 == 3 || i2 == 4 || i2 == 5 || i2 == 6 || i2 == 7) {
                maskPaint.setColor(-1);
                drawable = new Drawable() { // from class: org.telegram.ui.ActionBar.Theme.7
                    RectF rect;

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
                            } else {
                                i5 = i6 == 3 ? Math.max(bounds.width(), bounds.height()) / 2 : (int) Math.ceil(Math.sqrt(((bounds.left - bounds.centerX()) * (bounds.left - bounds.centerX())) + ((bounds.top - bounds.centerY()) * (bounds.top - bounds.centerY()))));
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
                };
            } else if (i2 == 2) {
                drawable = new ColorDrawable(-1);
            }
            BaseCell.RippleDrawableSafe rippleDrawableSafe = new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, drawable);
            if (i4 >= 23) {
                if (i2 == 1) {
                    if (i3 <= 0) {
                        i3 = AndroidUtilities.dp(20.0f);
                    }
                    rippleDrawableSafe.setRadius(i3);
                } else if (i2 == 5) {
                    rippleDrawableSafe.setRadius(-1);
                }
            }
            return rippleDrawableSafe;
        }
        drawable = null;
        BaseCell.RippleDrawableSafe rippleDrawableSafe2 = new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), null, drawable);
        if (i4 >= 23) {
        }
        return rippleDrawableSafe2;
    }

    public static Drawable createSelectorDrawableFromDrawables(Drawable drawable, Drawable drawable2) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{R.attr.state_pressed}, drawable2);
        stateListDrawable.addState(new int[]{R.attr.state_selected}, drawable2);
        stateListDrawable.addState(StateSet.WILD_CARD, drawable);
        return stateListDrawable;
    }

    public static Drawable createSelectorWithBackgroundDrawable(int i, int i2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i2}), new ColorDrawable(i), new ColorDrawable(i));
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{R.attr.state_pressed}, new ColorDrawable(i2));
        stateListDrawable.addState(new int[]{R.attr.state_selected}, new ColorDrawable(i2));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(i));
        return stateListDrawable;
    }

    public static Drawable createServiceDrawable(int i, View view, View view2) {
        return createServiceDrawable(i, view, view2, chat_actionBackgroundPaint);
    }

    public static Drawable createServiceDrawable(int i, View view, View view2, Paint paint) {
        return createServiceDrawable(i, view, view2, paint, null);
    }

    public static Drawable createServiceDrawable(final int i, final View view, final View view2, final Paint paint, final ResourcesProvider resourcesProvider) {
        return new Drawable() { // from class: org.telegram.ui.ActionBar.Theme.6
            private RectF rect = new RectF();

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                Rect bounds = getBounds();
                this.rect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
                Theme.applyServiceShaderMatrixForView(view, view2);
                RectF rectF = this.rect;
                float f = i;
                Paint paint2 = paint;
                if (paint2 == null) {
                    paint2 = Theme.getThemePaint("paintChatActionBackground", resourcesProvider);
                }
                canvas.drawRoundRect(rectF, f, f, paint2);
                ResourcesProvider resourcesProvider2 = resourcesProvider;
                if (resourcesProvider2 != null) {
                    if (!resourcesProvider2.hasGradientService()) {
                        return;
                    }
                } else if (!Theme.hasGradientService()) {
                    return;
                }
                RectF rectF2 = this.rect;
                float f2 = i;
                canvas.drawRoundRect(rectF2, f2, f2, Theme.getThemePaint("paintChatActionBackgroundDarken", resourcesProvider));
            }

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
        };
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
            return new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i3}), shapeDrawable, shapeDrawable2);
        }
        shapeDrawable2.getPaint().setColor(i3);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{R.attr.state_pressed}, shapeDrawable2);
        stateListDrawable.addState(new int[]{R.attr.state_focused}, shapeDrawable2);
        stateListDrawable.addState(StateSet.WILD_CARD, shapeDrawable);
        return stateListDrawable;
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
            return new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i3}), shapeDrawable, shapeDrawable2);
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{R.attr.state_pressed}, shapeDrawable2);
        stateListDrawable.addState(new int[]{R.attr.state_selected}, shapeDrawable2);
        stateListDrawable.addState(StateSet.WILD_CARD, shapeDrawable);
        return stateListDrawable;
    }

    /* JADX WARN: Code restructure failed: missing block: B:107:0x03f7, code lost:
    
        if (r9 > 1.0f) goto L178;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x03f9, code lost:
    
        r2 = r3.inSampleSize * 2;
        r3.inSampleSize = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0403, code lost:
    
        if (r2 < r9) goto L270;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x0405, code lost:
    
        r3.inJustDecodeBounds = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x0408, code lost:
    
        if (r6 == null) goto L183;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x040a, code lost:
    
        r2 = android.graphics.BitmapFactory.decodeFile(r6.getAbsolutePath(), r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0421, code lost:
    
        if (r2 == null) goto L187;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0423, code lost:
    
        r3 = new android.graphics.Paint();
        r3.setFilterBitmap(true);
        r6 = java.lang.Math.min(r2.getWidth() / 560.0f, r2.getHeight() / 560.0f);
        r0.set(0.0f, 0.0f, r2.getWidth() / r6, r2.getHeight() / r6);
        r0.offset((r12.getWidth() - r0.width()) / 2.0f, (r12.getHeight() - r0.height()) / 2.0f);
        r15.drawBitmap(r2, (android.graphics.Rect) null, r0, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x0469, code lost:
    
        r2 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x0413, code lost:
    
        r5.getChannel().position(r8);
        r2 = android.graphics.BitmapFactory.decodeStream(r5, null, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0282, code lost:
    
        if (r6 > 1.0f) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0284, code lost:
    
        r2 = r5.inSampleSize * 2;
        r5.inSampleSize = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x028e, code lost:
    
        if (r2 < r6) goto L272;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0294, code lost:
    
        r2 = android.graphics.Bitmap.Config.ALPHA_8;
        r5.inPreferredConfig = r2;
        r5.inJustDecodeBounds = false;
        r5 = android.graphics.BitmapFactory.decodeFile(r34, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x029f, code lost:
    
        if (r5 == null) goto L145;
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x02a1, code lost:
    
        if (r29 == 0) goto L143;
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x02a3, code lost:
    
        if (r35 == null) goto L143;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x02a5, code lost:
    
        r0 = new org.telegram.ui.Components.MotionBackgroundDrawable(r14, r28, r29, r30, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x02b0, code lost:
    
        if (r10 == null) goto L142;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x02b6, code lost:
    
        if (r10.getConfig() == r2) goto L142;
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x02b8, code lost:
    
        r2 = r10.copy(r2, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x02bd, code lost:
    
        r10.recycle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x02c0, code lost:
    
        r10 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x02c2, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x02c3, code lost:
    
        r10 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x0329, code lost:
    
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x02c5, code lost:
    
        r0.setPatternBitmap((int) (r35.patternIntensity * 100.0f), r5);
        r0.setBounds(0, 0, r10.getWidth(), r10.getHeight());
        r0.draw(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x02e0, code lost:
    
        r2 = new android.graphics.Paint();
        r2.setFilterBitmap(true);
        r3 = java.lang.Math.min(r5.getWidth() / 560.0f, r5.getHeight() / 560.0f);
        r0.set(0.0f, 0.0f, r5.getWidth() / r3, r5.getHeight() / r3);
        r0.offset((r10.getWidth() - r0.width()) / 2.0f, (r10.getHeight() - r0.height()) / 2.0f);
        r15.drawBitmap(r5, (android.graphics.Rect) null, r0, r2);
     */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0481 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0262 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x00a2 A[Catch: all -> 0x001b, TryCatch #14 {all -> 0x001b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001e, B:8:0x0082, B:12:0x0091, B:16:0x00a2, B:19:0x00ab, B:23:0x00be, B:26:0x00c7, B:30:0x00d5, B:33:0x00de, B:36:0x00f1, B:194:0x00fb, B:196:0x0109, B:198:0x010f, B:201:0x0120, B:206:0x012b, B:208:0x0135, B:212:0x01bb, B:40:0x01cd, B:42:0x021f, B:45:0x022f, B:49:0x023b, B:54:0x0248, B:188:0x0329, B:64:0x0490, B:66:0x04b6, B:68:0x04d9, B:70:0x04f3, B:71:0x0518, B:73:0x05a6, B:75:0x05c8, B:76:0x05f0, B:83:0x062b, B:59:0x0335, B:61:0x036b, B:87:0x033f, B:88:0x034b, B:91:0x0356, B:93:0x0382, B:121:0x046e, B:124:0x0487, B:131:0x0481, B:140:0x0634, B:146:0x063d, B:145:0x063a, B:213:0x014b, B:215:0x0153, B:217:0x015f, B:220:0x016e, B:221:0x017f, B:223:0x0187, B:225:0x0193, B:228:0x01a2, B:78:0x0618, B:128:0x047c), top: B:2:0x0008, inners: #2, #6, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:193:0x00fb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00be A[Catch: all -> 0x001b, TryCatch #14 {all -> 0x001b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001e, B:8:0x0082, B:12:0x0091, B:16:0x00a2, B:19:0x00ab, B:23:0x00be, B:26:0x00c7, B:30:0x00d5, B:33:0x00de, B:36:0x00f1, B:194:0x00fb, B:196:0x0109, B:198:0x010f, B:201:0x0120, B:206:0x012b, B:208:0x0135, B:212:0x01bb, B:40:0x01cd, B:42:0x021f, B:45:0x022f, B:49:0x023b, B:54:0x0248, B:188:0x0329, B:64:0x0490, B:66:0x04b6, B:68:0x04d9, B:70:0x04f3, B:71:0x0518, B:73:0x05a6, B:75:0x05c8, B:76:0x05f0, B:83:0x062b, B:59:0x0335, B:61:0x036b, B:87:0x033f, B:88:0x034b, B:91:0x0356, B:93:0x0382, B:121:0x046e, B:124:0x0487, B:131:0x0481, B:140:0x0634, B:146:0x063d, B:145:0x063a, B:213:0x014b, B:215:0x0153, B:217:0x015f, B:220:0x016e, B:221:0x017f, B:223:0x0187, B:225:0x0193, B:228:0x01a2, B:78:0x0618, B:128:0x047c), top: B:2:0x0008, inners: #2, #6, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:247:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x00d2  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00c5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00d5 A[Catch: all -> 0x001b, TryCatch #14 {all -> 0x001b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001e, B:8:0x0082, B:12:0x0091, B:16:0x00a2, B:19:0x00ab, B:23:0x00be, B:26:0x00c7, B:30:0x00d5, B:33:0x00de, B:36:0x00f1, B:194:0x00fb, B:196:0x0109, B:198:0x010f, B:201:0x0120, B:206:0x012b, B:208:0x0135, B:212:0x01bb, B:40:0x01cd, B:42:0x021f, B:45:0x022f, B:49:0x023b, B:54:0x0248, B:188:0x0329, B:64:0x0490, B:66:0x04b6, B:68:0x04d9, B:70:0x04f3, B:71:0x0518, B:73:0x05a6, B:75:0x05c8, B:76:0x05f0, B:83:0x062b, B:59:0x0335, B:61:0x036b, B:87:0x033f, B:88:0x034b, B:91:0x0356, B:93:0x0382, B:121:0x046e, B:124:0x0487, B:131:0x0481, B:140:0x0634, B:146:0x063d, B:145:0x063a, B:213:0x014b, B:215:0x0153, B:217:0x015f, B:220:0x016e, B:221:0x017f, B:223:0x0187, B:225:0x0193, B:228:0x01a2, B:78:0x0618, B:128:0x047c), top: B:2:0x0008, inners: #2, #6, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00dc A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x021f A[Catch: all -> 0x001b, TryCatch #14 {all -> 0x001b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001e, B:8:0x0082, B:12:0x0091, B:16:0x00a2, B:19:0x00ab, B:23:0x00be, B:26:0x00c7, B:30:0x00d5, B:33:0x00de, B:36:0x00f1, B:194:0x00fb, B:196:0x0109, B:198:0x010f, B:201:0x0120, B:206:0x012b, B:208:0x0135, B:212:0x01bb, B:40:0x01cd, B:42:0x021f, B:45:0x022f, B:49:0x023b, B:54:0x0248, B:188:0x0329, B:64:0x0490, B:66:0x04b6, B:68:0x04d9, B:70:0x04f3, B:71:0x0518, B:73:0x05a6, B:75:0x05c8, B:76:0x05f0, B:83:0x062b, B:59:0x0335, B:61:0x036b, B:87:0x033f, B:88:0x034b, B:91:0x0356, B:93:0x0382, B:121:0x046e, B:124:0x0487, B:131:0x0481, B:140:0x0634, B:146:0x063d, B:145:0x063a, B:213:0x014b, B:215:0x0153, B:217:0x015f, B:220:0x016e, B:221:0x017f, B:223:0x0187, B:225:0x0193, B:228:0x01a2, B:78:0x0618, B:128:0x047c), top: B:2:0x0008, inners: #2, #6, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0331  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0490 A[Catch: all -> 0x001b, TryCatch #14 {all -> 0x001b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001e, B:8:0x0082, B:12:0x0091, B:16:0x00a2, B:19:0x00ab, B:23:0x00be, B:26:0x00c7, B:30:0x00d5, B:33:0x00de, B:36:0x00f1, B:194:0x00fb, B:196:0x0109, B:198:0x010f, B:201:0x0120, B:206:0x012b, B:208:0x0135, B:212:0x01bb, B:40:0x01cd, B:42:0x021f, B:45:0x022f, B:49:0x023b, B:54:0x0248, B:188:0x0329, B:64:0x0490, B:66:0x04b6, B:68:0x04d9, B:70:0x04f3, B:71:0x0518, B:73:0x05a6, B:75:0x05c8, B:76:0x05f0, B:83:0x062b, B:59:0x0335, B:61:0x036b, B:87:0x033f, B:88:0x034b, B:91:0x0356, B:93:0x0382, B:121:0x046e, B:124:0x0487, B:131:0x0481, B:140:0x0634, B:146:0x063d, B:145:0x063a, B:213:0x014b, B:215:0x0153, B:217:0x015f, B:220:0x016e, B:221:0x017f, B:223:0x0187, B:225:0x0193, B:228:0x01a2, B:78:0x0618, B:128:0x047c), top: B:2:0x0008, inners: #2, #6, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x04d9 A[Catch: all -> 0x001b, TryCatch #14 {all -> 0x001b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001e, B:8:0x0082, B:12:0x0091, B:16:0x00a2, B:19:0x00ab, B:23:0x00be, B:26:0x00c7, B:30:0x00d5, B:33:0x00de, B:36:0x00f1, B:194:0x00fb, B:196:0x0109, B:198:0x010f, B:201:0x0120, B:206:0x012b, B:208:0x0135, B:212:0x01bb, B:40:0x01cd, B:42:0x021f, B:45:0x022f, B:49:0x023b, B:54:0x0248, B:188:0x0329, B:64:0x0490, B:66:0x04b6, B:68:0x04d9, B:70:0x04f3, B:71:0x0518, B:73:0x05a6, B:75:0x05c8, B:76:0x05f0, B:83:0x062b, B:59:0x0335, B:61:0x036b, B:87:0x033f, B:88:0x034b, B:91:0x0356, B:93:0x0382, B:121:0x046e, B:124:0x0487, B:131:0x0481, B:140:0x0634, B:146:0x063d, B:145:0x063a, B:213:0x014b, B:215:0x0153, B:217:0x015f, B:220:0x016e, B:221:0x017f, B:223:0x0187, B:225:0x0193, B:228:0x01a2, B:78:0x0618, B:128:0x047c), top: B:2:0x0008, inners: #2, #6, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x04f3 A[Catch: all -> 0x001b, TryCatch #14 {all -> 0x001b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001e, B:8:0x0082, B:12:0x0091, B:16:0x00a2, B:19:0x00ab, B:23:0x00be, B:26:0x00c7, B:30:0x00d5, B:33:0x00de, B:36:0x00f1, B:194:0x00fb, B:196:0x0109, B:198:0x010f, B:201:0x0120, B:206:0x012b, B:208:0x0135, B:212:0x01bb, B:40:0x01cd, B:42:0x021f, B:45:0x022f, B:49:0x023b, B:54:0x0248, B:188:0x0329, B:64:0x0490, B:66:0x04b6, B:68:0x04d9, B:70:0x04f3, B:71:0x0518, B:73:0x05a6, B:75:0x05c8, B:76:0x05f0, B:83:0x062b, B:59:0x0335, B:61:0x036b, B:87:0x033f, B:88:0x034b, B:91:0x0356, B:93:0x0382, B:121:0x046e, B:124:0x0487, B:131:0x0481, B:140:0x0634, B:146:0x063d, B:145:0x063a, B:213:0x014b, B:215:0x0153, B:217:0x015f, B:220:0x016e, B:221:0x017f, B:223:0x0187, B:225:0x0193, B:228:0x01a2, B:78:0x0618, B:128:0x047c), top: B:2:0x0008, inners: #2, #6, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x05a6 A[Catch: all -> 0x001b, TryCatch #14 {all -> 0x001b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001e, B:8:0x0082, B:12:0x0091, B:16:0x00a2, B:19:0x00ab, B:23:0x00be, B:26:0x00c7, B:30:0x00d5, B:33:0x00de, B:36:0x00f1, B:194:0x00fb, B:196:0x0109, B:198:0x010f, B:201:0x0120, B:206:0x012b, B:208:0x0135, B:212:0x01bb, B:40:0x01cd, B:42:0x021f, B:45:0x022f, B:49:0x023b, B:54:0x0248, B:188:0x0329, B:64:0x0490, B:66:0x04b6, B:68:0x04d9, B:70:0x04f3, B:71:0x0518, B:73:0x05a6, B:75:0x05c8, B:76:0x05f0, B:83:0x062b, B:59:0x0335, B:61:0x036b, B:87:0x033f, B:88:0x034b, B:91:0x0356, B:93:0x0382, B:121:0x046e, B:124:0x0487, B:131:0x0481, B:140:0x0634, B:146:0x063d, B:145:0x063a, B:213:0x014b, B:215:0x0153, B:217:0x015f, B:220:0x016e, B:221:0x017f, B:223:0x0187, B:225:0x0193, B:228:0x01a2, B:78:0x0618, B:128:0x047c), top: B:2:0x0008, inners: #2, #6, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x05c8 A[Catch: all -> 0x001b, TryCatch #14 {all -> 0x001b, blocks: (B:3:0x0008, B:5:0x0015, B:6:0x001e, B:8:0x0082, B:12:0x0091, B:16:0x00a2, B:19:0x00ab, B:23:0x00be, B:26:0x00c7, B:30:0x00d5, B:33:0x00de, B:36:0x00f1, B:194:0x00fb, B:196:0x0109, B:198:0x010f, B:201:0x0120, B:206:0x012b, B:208:0x0135, B:212:0x01bb, B:40:0x01cd, B:42:0x021f, B:45:0x022f, B:49:0x023b, B:54:0x0248, B:188:0x0329, B:64:0x0490, B:66:0x04b6, B:68:0x04d9, B:70:0x04f3, B:71:0x0518, B:73:0x05a6, B:75:0x05c8, B:76:0x05f0, B:83:0x062b, B:59:0x0335, B:61:0x036b, B:87:0x033f, B:88:0x034b, B:91:0x0356, B:93:0x0382, B:121:0x046e, B:124:0x0487, B:131:0x0481, B:140:0x0634, B:146:0x063d, B:145:0x063a, B:213:0x014b, B:215:0x0153, B:217:0x015f, B:220:0x016e, B:221:0x017f, B:223:0x0187, B:225:0x0193, B:228:0x01a2, B:78:0x0618, B:128:0x047c), top: B:2:0x0008, inners: #2, #6, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x04b2  */
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
        char c;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        String queryParameter;
        int i16;
        Drawable mutate;
        int i17;
        int i18;
        Drawable drawable;
        Drawable drawable2;
        Drawable drawable3;
        int i19;
        int i20;
        boolean z;
        Bitmap bitmap;
        FileInputStream fileInputStream;
        File file;
        int i21;
        int i22;
        Drawable createDitheredGradientBitmapDrawable;
        try {
            String[] strArr = new String[1];
            final SparseIntArray themeFileValues = getThemeFileValues(new File(str), null, strArr);
            if (themeAccent != null) {
                checkIsDark(themeFileValues, themeAccent.parentTheme);
            }
            int i23 = currentColorsNoAccent.get(key_wallpaperFileOffset, -1);
            Bitmap createBitmap = Bitmaps.createBitmap(560, 678, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint2 = new Paint();
            int previewColor = getPreviewColor(themeFileValues, key_actionBarDefault);
            int previewColor2 = getPreviewColor(themeFileValues, key_actionBarDefaultIcon);
            int previewColor3 = getPreviewColor(themeFileValues, key_chat_messagePanelBackground);
            int previewColor4 = getPreviewColor(themeFileValues, key_chat_messagePanelIcons);
            int previewColor5 = getPreviewColor(themeFileValues, key_chat_inBubble);
            int previewColor6 = getPreviewColor(themeFileValues, key_chat_outBubble);
            themeFileValues.get(key_chat_outBubbleGradient1);
            int i24 = themeFileValues.get(key_chat_wallpaper);
            int i25 = themeFileValues.get(key_chat_wallpaper_gradient_to1);
            int i26 = themeFileValues.get(key_chat_wallpaper_gradient_to2);
            int i27 = themeFileValues.get(key_chat_wallpaper_gradient_to3);
            if (themeAccent != null) {
                paint = paint2;
                i = (int) themeAccent.backgroundOverrideColor;
            } else {
                paint = paint2;
                i = 0;
            }
            try {
                if (i != 0 || themeAccent == null) {
                    i2 = i27;
                } else {
                    i2 = i27;
                    if (themeAccent.backgroundOverrideColor != 0) {
                        i = 0;
                        i3 = themeAccent == null ? (int) themeAccent.backgroundGradientOverrideColor1 : 0;
                        if (i3 == 0 || themeAccent == null) {
                            i4 = i2;
                            i5 = i;
                        } else {
                            i4 = i2;
                            i5 = i;
                            if (themeAccent.backgroundGradientOverrideColor1 != 0) {
                                i25 = 0;
                                i6 = themeAccent != null ? (int) themeAccent.backgroundGradientOverrideColor2 : 0;
                                if (i6 != 0 && themeAccent != null && themeAccent.backgroundGradientOverrideColor2 != 0) {
                                    i6 = 0;
                                } else if (i6 == 0) {
                                    i6 = i26;
                                }
                                i7 = themeAccent != null ? (int) themeAccent.backgroundGradientOverrideColor3 : 0;
                                if (i7 == 0 || themeAccent == null) {
                                    i8 = i4;
                                    i9 = i6;
                                } else {
                                    i8 = i4;
                                    i9 = i6;
                                    if (themeAccent.backgroundGradientOverrideColor3 != 0) {
                                        c = 0;
                                        i8 = 0;
                                        int i28 = i8;
                                        if (!TextUtils.isEmpty(strArr[c])) {
                                            try {
                                                queryParameter = Uri.parse(strArr[c]).getQueryParameter("bg_color");
                                            } catch (Exception e) {
                                                e = e;
                                                i10 = i5;
                                            }
                                            if (themeAccent != null && !TextUtils.isEmpty(queryParameter)) {
                                                i10 = Integer.parseInt(queryParameter.substring(0, 6), 16) | (-16777216);
                                                try {
                                                    themeAccent.backgroundOverrideColor = i10;
                                                    if (queryParameter.length() >= 13) {
                                                        try {
                                                            if (AndroidUtilities.isValidWallChar(queryParameter.charAt(6))) {
                                                                i25 = Integer.parseInt(queryParameter.substring(7, 13), 16) | (-16777216);
                                                                themeAccent.backgroundGradientOverrideColor1 = i25;
                                                            }
                                                        } catch (Exception e2) {
                                                            e = e2;
                                                            i11 = i28;
                                                            i12 = i9;
                                                            FileLog.e(e);
                                                            i13 = i12;
                                                            i14 = i25;
                                                            i15 = i11;
                                                            mutate = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_back).mutate();
                                                            setDrawableColor(mutate, previewColor2);
                                                            Drawable mutate2 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_dots).mutate();
                                                            setDrawableColor(mutate2, previewColor2);
                                                            Drawable mutate3 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_smile).mutate();
                                                            setDrawableColor(mutate3, previewColor4);
                                                            Drawable mutate4 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_mic).mutate();
                                                            setDrawableColor(mutate4, previewColor4);
                                                            MessageDrawable[] messageDrawableArr = new MessageDrawable[2];
                                                            i18 = 0;
                                                            for (i17 = 2; i18 < i17; i17 = 2) {
                                                            }
                                                            drawable = mutate2;
                                                            drawable2 = mutate3;
                                                            drawable3 = mutate4;
                                                            RectF rectF = new RectF();
                                                            int i29 = 80;
                                                            if (str2 == null) {
                                                            }
                                                            i20 = 80;
                                                            z = false;
                                                            if (z) {
                                                            }
                                                            Paint paint3 = paint;
                                                            paint3.setColor(previewColor);
                                                            canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint3);
                                                            if (mutate != null) {
                                                            }
                                                            if (drawable != null) {
                                                            }
                                                            messageDrawableArr[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                                            messageDrawableArr[1].setTop(0, 560, 522, false, false);
                                                            messageDrawableArr[1].draw(canvas);
                                                            messageDrawableArr[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                                            messageDrawableArr[1].setTop(430, 560, 522, false, false);
                                                            messageDrawableArr[1].draw(canvas);
                                                            messageDrawableArr[0].setBounds(20, 323, 399, 415);
                                                            messageDrawableArr[0].setTop(323, 560, 522, false, false);
                                                            messageDrawableArr[0].draw(canvas);
                                                            paint3.setColor(previewColor3);
                                                            canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint3);
                                                            if (drawable2 != null) {
                                                            }
                                                            if (drawable3 != null) {
                                                            }
                                                            canvas.setBitmap(null);
                                                            File file2 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                            createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file2));
                                                            SharedConfig.saveConfig();
                                                            return file2.getAbsolutePath();
                                                        }
                                                    }
                                                    if (queryParameter.length() < 20 || !AndroidUtilities.isValidWallChar(queryParameter.charAt(13))) {
                                                        i16 = i10;
                                                        i12 = i9;
                                                    } else {
                                                        i12 = Integer.parseInt(queryParameter.substring(14, 20), 16) | (-16777216);
                                                        i16 = i10;
                                                        try {
                                                            themeAccent.backgroundGradientOverrideColor2 = i12;
                                                        } catch (Exception e3) {
                                                            e = e3;
                                                            i11 = i28;
                                                            i10 = i16;
                                                            FileLog.e(e);
                                                            i13 = i12;
                                                            i14 = i25;
                                                            i15 = i11;
                                                            mutate = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_back).mutate();
                                                            setDrawableColor(mutate, previewColor2);
                                                            Drawable mutate22 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_dots).mutate();
                                                            setDrawableColor(mutate22, previewColor2);
                                                            Drawable mutate32 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_smile).mutate();
                                                            setDrawableColor(mutate32, previewColor4);
                                                            Drawable mutate42 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_mic).mutate();
                                                            setDrawableColor(mutate42, previewColor4);
                                                            MessageDrawable[] messageDrawableArr2 = new MessageDrawable[2];
                                                            i18 = 0;
                                                            while (i18 < i17) {
                                                            }
                                                            drawable = mutate22;
                                                            drawable2 = mutate32;
                                                            drawable3 = mutate42;
                                                            RectF rectF2 = new RectF();
                                                            int i292 = 80;
                                                            if (str2 == null) {
                                                            }
                                                            i20 = 80;
                                                            z = false;
                                                            if (z) {
                                                            }
                                                            Paint paint32 = paint;
                                                            paint32.setColor(previewColor);
                                                            canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint32);
                                                            if (mutate != null) {
                                                            }
                                                            if (drawable != null) {
                                                            }
                                                            messageDrawableArr2[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                                            messageDrawableArr2[1].setTop(0, 560, 522, false, false);
                                                            messageDrawableArr2[1].draw(canvas);
                                                            messageDrawableArr2[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                                            messageDrawableArr2[1].setTop(430, 560, 522, false, false);
                                                            messageDrawableArr2[1].draw(canvas);
                                                            messageDrawableArr2[0].setBounds(20, 323, 399, 415);
                                                            messageDrawableArr2[0].setTop(323, 560, 522, false, false);
                                                            messageDrawableArr2[0].draw(canvas);
                                                            paint32.setColor(previewColor3);
                                                            canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint32);
                                                            if (drawable2 != null) {
                                                            }
                                                            if (drawable3 != null) {
                                                            }
                                                            canvas.setBitmap(null);
                                                            File file22 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                            createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file22));
                                                            SharedConfig.saveConfig();
                                                            return file22.getAbsolutePath();
                                                        }
                                                    }
                                                    if (queryParameter.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter.charAt(20))) {
                                                        int parseInt = Integer.parseInt(queryParameter.substring(21), 16) | (-16777216);
                                                        try {
                                                            themeAccent.backgroundGradientOverrideColor3 = parseInt;
                                                            i13 = i12;
                                                            i15 = parseInt;
                                                            i14 = i25;
                                                        } catch (Exception e4) {
                                                            e = e4;
                                                            i11 = parseInt;
                                                            i10 = i16;
                                                            FileLog.e(e);
                                                            i13 = i12;
                                                            i14 = i25;
                                                            i15 = i11;
                                                            mutate = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_back).mutate();
                                                            setDrawableColor(mutate, previewColor2);
                                                            Drawable mutate222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_dots).mutate();
                                                            setDrawableColor(mutate222, previewColor2);
                                                            Drawable mutate322 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_smile).mutate();
                                                            setDrawableColor(mutate322, previewColor4);
                                                            Drawable mutate422 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_mic).mutate();
                                                            setDrawableColor(mutate422, previewColor4);
                                                            MessageDrawable[] messageDrawableArr22 = new MessageDrawable[2];
                                                            i18 = 0;
                                                            while (i18 < i17) {
                                                            }
                                                            drawable = mutate222;
                                                            drawable2 = mutate322;
                                                            drawable3 = mutate422;
                                                            RectF rectF22 = new RectF();
                                                            int i2922 = 80;
                                                            if (str2 == null) {
                                                            }
                                                            i20 = 80;
                                                            z = false;
                                                            if (z) {
                                                            }
                                                            Paint paint322 = paint;
                                                            paint322.setColor(previewColor);
                                                            canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint322);
                                                            if (mutate != null) {
                                                            }
                                                            if (drawable != null) {
                                                            }
                                                            messageDrawableArr22[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                                            messageDrawableArr22[1].setTop(0, 560, 522, false, false);
                                                            messageDrawableArr22[1].draw(canvas);
                                                            messageDrawableArr22[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                                            messageDrawableArr22[1].setTop(430, 560, 522, false, false);
                                                            messageDrawableArr22[1].draw(canvas);
                                                            messageDrawableArr22[0].setBounds(20, 323, 399, 415);
                                                            messageDrawableArr22[0].setTop(323, 560, 522, false, false);
                                                            messageDrawableArr22[0].draw(canvas);
                                                            paint322.setColor(previewColor3);
                                                            canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint322);
                                                            if (drawable2 != null) {
                                                            }
                                                            if (drawable3 != null) {
                                                            }
                                                            canvas.setBitmap(null);
                                                            File file222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                            createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file222));
                                                            SharedConfig.saveConfig();
                                                            return file222.getAbsolutePath();
                                                        }
                                                    } else {
                                                        i13 = i12;
                                                        i14 = i25;
                                                        i15 = i28;
                                                    }
                                                    i10 = i16;
                                                } catch (Exception e5) {
                                                    e = e5;
                                                }
                                                mutate = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_back).mutate();
                                                setDrawableColor(mutate, previewColor2);
                                                Drawable mutate2222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_dots).mutate();
                                                setDrawableColor(mutate2222, previewColor2);
                                                Drawable mutate3222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_smile).mutate();
                                                setDrawableColor(mutate3222, previewColor4);
                                                Drawable mutate4222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_mic).mutate();
                                                setDrawableColor(mutate4222, previewColor4);
                                                MessageDrawable[] messageDrawableArr222 = new MessageDrawable[2];
                                                i18 = 0;
                                                while (i18 < i17) {
                                                    Drawable drawable4 = mutate4222;
                                                    Drawable drawable5 = mutate2222;
                                                    Drawable drawable6 = mutate3222;
                                                    MessageDrawable messageDrawable = new MessageDrawable(2, i18 == 1, false) { // from class: org.telegram.ui.ActionBar.Theme.10
                                                        @Override // org.telegram.ui.ActionBar.Theme.MessageDrawable
                                                        protected int getColor(int i30) {
                                                            int indexOfKey = themeFileValues.indexOfKey(i30);
                                                            return indexOfKey > 0 ? themeFileValues.valueAt(indexOfKey) : Theme.defaultColors[i30];
                                                        }

                                                        @Override // org.telegram.ui.ActionBar.Theme.MessageDrawable
                                                        protected int getCurrentColor(int i30) {
                                                            return themeFileValues.get(i30);
                                                        }
                                                    };
                                                    messageDrawableArr222[i18] = messageDrawable;
                                                    setDrawableColor(messageDrawable, i18 == 0 ? previewColor5 : previewColor6);
                                                    i18++;
                                                    mutate4222 = drawable4;
                                                    mutate3222 = drawable6;
                                                    mutate2222 = drawable5;
                                                }
                                                drawable = mutate2222;
                                                drawable2 = mutate3222;
                                                drawable3 = mutate4222;
                                                RectF rectF222 = new RectF();
                                                int i29222 = 80;
                                                if (str2 == null) {
                                                    try {
                                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                                        options.inJustDecodeBounds = true;
                                                        BitmapFactory.decodeFile(str2, options);
                                                        int i30 = options.outWidth;
                                                        if (i30 > 0 && (i19 = options.outHeight) > 0) {
                                                            float min = Math.min(i30 / 560.0f, i19 / 560.0f);
                                                            options.inSampleSize = 1;
                                                        }
                                                        bitmap = createBitmap;
                                                        createBitmap = bitmap;
                                                    } catch (Throwable th) {
                                                        th = th;
                                                    }
                                                } else {
                                                    if (i10 != 0) {
                                                        if (i14 == 0) {
                                                            createDitheredGradientBitmapDrawable = new ColorDrawable(i10);
                                                        } else {
                                                            if (i13 == 0) {
                                                                int i31 = themeFileValues.get(key_chat_wallpaper_gradient_rotation, -1);
                                                                if (i31 == -1) {
                                                                    i31 = 45;
                                                                }
                                                                i22 = 120;
                                                                createDitheredGradientBitmapDrawable = BackgroundGradientDrawable.createDitheredGradientBitmapDrawable(i31, new int[]{i10, i26}, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                                                                i29222 = 90;
                                                                createDitheredGradientBitmapDrawable.setBounds(0, i22, createBitmap.getWidth(), createBitmap.getHeight() - i22);
                                                                createDitheredGradientBitmapDrawable.draw(canvas);
                                                                i20 = i29222;
                                                                z = true;
                                                                if (z) {
                                                                    Drawable createDefaultWallpaper = createDefaultWallpaper(createBitmap.getWidth(), createBitmap.getHeight() - 120);
                                                                    createDefaultWallpaper.setBounds(0, 120, createBitmap.getWidth(), createBitmap.getHeight() - 120);
                                                                    createDefaultWallpaper.draw(canvas);
                                                                }
                                                                Paint paint3222 = paint;
                                                                paint3222.setColor(previewColor);
                                                                canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint3222);
                                                                if (mutate != null) {
                                                                    int intrinsicHeight = (120 - mutate.getIntrinsicHeight()) / 2;
                                                                    mutate.setBounds(13, intrinsicHeight, mutate.getIntrinsicWidth() + 13, mutate.getIntrinsicHeight() + intrinsicHeight);
                                                                    mutate.draw(canvas);
                                                                }
                                                                if (drawable != null) {
                                                                    int width = (createBitmap.getWidth() - drawable.getIntrinsicWidth()) - 10;
                                                                    int intrinsicHeight2 = (120 - drawable.getIntrinsicHeight()) / 2;
                                                                    drawable.setBounds(width, intrinsicHeight2, drawable.getIntrinsicWidth() + width, drawable.getIntrinsicHeight() + intrinsicHeight2);
                                                                    drawable.draw(canvas);
                                                                }
                                                                messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                                                messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                                                messageDrawableArr222[1].draw(canvas);
                                                                messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                                                messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                                                messageDrawableArr222[1].draw(canvas);
                                                                messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                                                messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                                                messageDrawableArr222[0].draw(canvas);
                                                                paint3222.setColor(previewColor3);
                                                                canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint3222);
                                                                if (drawable2 != null) {
                                                                    int height = (createBitmap.getHeight() - 120) + ((120 - drawable2.getIntrinsicHeight()) / 2);
                                                                    drawable2.setBounds(22, height, drawable2.getIntrinsicWidth() + 22, drawable2.getIntrinsicHeight() + height);
                                                                    drawable2.draw(canvas);
                                                                }
                                                                if (drawable3 != null) {
                                                                    int width2 = (createBitmap.getWidth() - drawable3.getIntrinsicWidth()) - 22;
                                                                    int height2 = (createBitmap.getHeight() - 120) + ((120 - drawable3.getIntrinsicHeight()) / 2);
                                                                    drawable3.setBounds(width2, height2, drawable3.getIntrinsicWidth() + width2, drawable3.getIntrinsicHeight() + height2);
                                                                    drawable3.draw(canvas);
                                                                }
                                                                canvas.setBitmap(null);
                                                                File file2222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file2222));
                                                                SharedConfig.saveConfig();
                                                                return file2222.getAbsolutePath();
                                                            }
                                                            createDitheredGradientBitmapDrawable = new MotionBackgroundDrawable(i10, i14, i13, i15, true);
                                                        }
                                                        i22 = 120;
                                                        createDitheredGradientBitmapDrawable.setBounds(0, i22, createBitmap.getWidth(), createBitmap.getHeight() - i22);
                                                        createDitheredGradientBitmapDrawable.draw(canvas);
                                                        i20 = i29222;
                                                        z = true;
                                                        if (z) {
                                                        }
                                                        Paint paint32222 = paint;
                                                        paint32222.setColor(previewColor);
                                                        canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint32222);
                                                        if (mutate != null) {
                                                        }
                                                        if (drawable != null) {
                                                        }
                                                        messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                                        messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                                        messageDrawableArr222[1].draw(canvas);
                                                        messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                                        messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                                        messageDrawableArr222[1].draw(canvas);
                                                        messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                                        messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                                        messageDrawableArr222[0].draw(canvas);
                                                        paint32222.setColor(previewColor3);
                                                        canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint32222);
                                                        if (drawable2 != null) {
                                                        }
                                                        if (drawable3 != null) {
                                                        }
                                                        canvas.setBitmap(null);
                                                        File file22222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                        createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file22222));
                                                        SharedConfig.saveConfig();
                                                        return file22222.getAbsolutePath();
                                                    }
                                                    if (i23 >= 0 || !TextUtils.isEmpty(strArr[0])) {
                                                        try {
                                                            BitmapFactory.Options options2 = new BitmapFactory.Options();
                                                            options2.inJustDecodeBounds = true;
                                                            if (TextUtils.isEmpty(strArr[0])) {
                                                                fileInputStream = new FileInputStream(str);
                                                                try {
                                                                    bitmap = createBitmap;
                                                                    try {
                                                                        fileInputStream.getChannel().position(i23);
                                                                        BitmapFactory.decodeStream(fileInputStream, null, options2);
                                                                        file = null;
                                                                    } catch (Throwable th2) {
                                                                        th = th2;
                                                                        try {
                                                                            FileLog.e(th);
                                                                            if (fileInputStream != null) {
                                                                                try {
                                                                                    fileInputStream.close();
                                                                                } catch (Exception e6) {
                                                                                    e = e6;
                                                                                    z = false;
                                                                                    FileLog.e(e);
                                                                                    createBitmap = bitmap;
                                                                                    i20 = 80;
                                                                                    if (z) {
                                                                                    }
                                                                                    Paint paint322222 = paint;
                                                                                    paint322222.setColor(previewColor);
                                                                                    canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint322222);
                                                                                    if (mutate != null) {
                                                                                    }
                                                                                    if (drawable != null) {
                                                                                    }
                                                                                    messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                                                                    messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                                                                    messageDrawableArr222[1].draw(canvas);
                                                                                    messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                                                                    messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                                                                    messageDrawableArr222[1].draw(canvas);
                                                                                    messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                                                                    messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                                                                    messageDrawableArr222[0].draw(canvas);
                                                                                    paint322222.setColor(previewColor3);
                                                                                    canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint322222);
                                                                                    if (drawable2 != null) {
                                                                                    }
                                                                                    if (drawable3 != null) {
                                                                                    }
                                                                                    canvas.setBitmap(null);
                                                                                    File file222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                                    createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file222222));
                                                                                    SharedConfig.saveConfig();
                                                                                    return file222222.getAbsolutePath();
                                                                                }
                                                                            }
                                                                            createBitmap = bitmap;
                                                                            i20 = 80;
                                                                            z = false;
                                                                            if (z) {
                                                                            }
                                                                            Paint paint3222222 = paint;
                                                                            paint3222222.setColor(previewColor);
                                                                            canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint3222222);
                                                                            if (mutate != null) {
                                                                            }
                                                                            if (drawable != null) {
                                                                            }
                                                                            messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                                                            messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                                                            messageDrawableArr222[1].draw(canvas);
                                                                            messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                                                            messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                                                            messageDrawableArr222[1].draw(canvas);
                                                                            messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                                                            messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                                                            messageDrawableArr222[0].draw(canvas);
                                                                            paint3222222.setColor(previewColor3);
                                                                            canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint3222222);
                                                                            if (drawable2 != null) {
                                                                            }
                                                                            if (drawable3 != null) {
                                                                            }
                                                                            canvas.setBitmap(null);
                                                                            File file2222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                            createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file2222222));
                                                                            SharedConfig.saveConfig();
                                                                            return file2222222.getAbsolutePath();
                                                                        } finally {
                                                                        }
                                                                    }
                                                                } catch (Throwable th3) {
                                                                    th = th3;
                                                                    bitmap = createBitmap;
                                                                    FileLog.e(th);
                                                                    if (fileInputStream != null) {
                                                                    }
                                                                    createBitmap = bitmap;
                                                                    i20 = 80;
                                                                    z = false;
                                                                    if (z) {
                                                                    }
                                                                    Paint paint32222222 = paint;
                                                                    paint32222222.setColor(previewColor);
                                                                    canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint32222222);
                                                                    if (mutate != null) {
                                                                    }
                                                                    if (drawable != null) {
                                                                    }
                                                                    messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                                                    messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                                                    messageDrawableArr222[1].draw(canvas);
                                                                    messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                                                    messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                                                    messageDrawableArr222[1].draw(canvas);
                                                                    messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                                                    messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                                                    messageDrawableArr222[0].draw(canvas);
                                                                    paint32222222.setColor(previewColor3);
                                                                    canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint32222222);
                                                                    if (drawable2 != null) {
                                                                    }
                                                                    if (drawable3 != null) {
                                                                    }
                                                                    canvas.setBitmap(null);
                                                                    File file22222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                    createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file22222222));
                                                                    SharedConfig.saveConfig();
                                                                    return file22222222.getAbsolutePath();
                                                                }
                                                            } else {
                                                                File file3 = new File(ApplicationLoader.getFilesDirFixed(), Utilities.MD5(strArr[0]) + ".wp");
                                                                BitmapFactory.decodeFile(file3.getAbsolutePath(), options2);
                                                                file = file3;
                                                                bitmap = createBitmap;
                                                                fileInputStream = null;
                                                            }
                                                            int i32 = options2.outWidth;
                                                            if (i32 > 0 && (i21 = options2.outHeight) > 0) {
                                                                float min2 = Math.min(i32 / 560.0f, i21 / 560.0f);
                                                                options2.inSampleSize = 1;
                                                            }
                                                            z = false;
                                                            if (fileInputStream != null) {
                                                                try {
                                                                    fileInputStream.close();
                                                                } catch (Exception e7) {
                                                                    e = e7;
                                                                    FileLog.e(e);
                                                                    createBitmap = bitmap;
                                                                    i20 = 80;
                                                                    if (z) {
                                                                    }
                                                                    Paint paint322222222 = paint;
                                                                    paint322222222.setColor(previewColor);
                                                                    canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint322222222);
                                                                    if (mutate != null) {
                                                                    }
                                                                    if (drawable != null) {
                                                                    }
                                                                    messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                                                    messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                                                    messageDrawableArr222[1].draw(canvas);
                                                                    messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                                                    messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                                                    messageDrawableArr222[1].draw(canvas);
                                                                    messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                                                    messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                                                    messageDrawableArr222[0].draw(canvas);
                                                                    paint322222222.setColor(previewColor3);
                                                                    canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint322222222);
                                                                    if (drawable2 != null) {
                                                                    }
                                                                    if (drawable3 != null) {
                                                                    }
                                                                    canvas.setBitmap(null);
                                                                    File file222222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                                    createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file222222222));
                                                                    SharedConfig.saveConfig();
                                                                    return file222222222.getAbsolutePath();
                                                                }
                                                            }
                                                            createBitmap = bitmap;
                                                            i20 = 80;
                                                        } catch (Throwable th4) {
                                                            th = th4;
                                                            bitmap = createBitmap;
                                                            fileInputStream = null;
                                                        }
                                                        if (z) {
                                                        }
                                                        Paint paint3222222222 = paint;
                                                        paint3222222222.setColor(previewColor);
                                                        canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint3222222222);
                                                        if (mutate != null) {
                                                        }
                                                        if (drawable != null) {
                                                        }
                                                        messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                                        messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                                        messageDrawableArr222[1].draw(canvas);
                                                        messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                                        messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                                        messageDrawableArr222[1].draw(canvas);
                                                        messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                                        messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                                        messageDrawableArr222[0].draw(canvas);
                                                        paint3222222222.setColor(previewColor3);
                                                        canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint3222222222);
                                                        if (drawable2 != null) {
                                                        }
                                                        if (drawable3 != null) {
                                                        }
                                                        canvas.setBitmap(null);
                                                        File file2222222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                        createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file2222222222));
                                                        SharedConfig.saveConfig();
                                                        return file2222222222.getAbsolutePath();
                                                    }
                                                    bitmap = createBitmap;
                                                    createBitmap = bitmap;
                                                }
                                                i20 = 80;
                                                z = false;
                                                if (z) {
                                                }
                                                Paint paint32222222222 = paint;
                                                paint32222222222.setColor(previewColor);
                                                canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint32222222222);
                                                if (mutate != null) {
                                                }
                                                if (drawable != null) {
                                                }
                                                messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                                messageDrawableArr222[1].setTop(0, 560, 522, false, false);
                                                messageDrawableArr222[1].draw(canvas);
                                                messageDrawableArr222[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                                messageDrawableArr222[1].setTop(430, 560, 522, false, false);
                                                messageDrawableArr222[1].draw(canvas);
                                                messageDrawableArr222[0].setBounds(20, 323, 399, 415);
                                                messageDrawableArr222[0].setTop(323, 560, 522, false, false);
                                                messageDrawableArr222[0].draw(canvas);
                                                paint32222222222.setColor(previewColor3);
                                                canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint32222222222);
                                                if (drawable2 != null) {
                                                }
                                                if (drawable3 != null) {
                                                }
                                                canvas.setBitmap(null);
                                                File file22222222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                                createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file22222222222));
                                                SharedConfig.saveConfig();
                                                return file22222222222.getAbsolutePath();
                                            }
                                        }
                                        i14 = i25;
                                        i10 = i5;
                                        i15 = i28;
                                        i13 = i9;
                                        mutate = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_back).mutate();
                                        setDrawableColor(mutate, previewColor2);
                                        Drawable mutate22222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_dots).mutate();
                                        setDrawableColor(mutate22222, previewColor2);
                                        Drawable mutate32222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_smile).mutate();
                                        setDrawableColor(mutate32222, previewColor4);
                                        Drawable mutate42222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_mic).mutate();
                                        setDrawableColor(mutate42222, previewColor4);
                                        MessageDrawable[] messageDrawableArr2222 = new MessageDrawable[2];
                                        i18 = 0;
                                        while (i18 < i17) {
                                        }
                                        drawable = mutate22222;
                                        drawable2 = mutate32222;
                                        drawable3 = mutate42222;
                                        RectF rectF2222 = new RectF();
                                        int i292222 = 80;
                                        if (str2 == null) {
                                        }
                                        i20 = 80;
                                        z = false;
                                        if (z) {
                                        }
                                        Paint paint322222222222 = paint;
                                        paint322222222222.setColor(previewColor);
                                        canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint322222222222);
                                        if (mutate != null) {
                                        }
                                        if (drawable != null) {
                                        }
                                        messageDrawableArr2222[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                        messageDrawableArr2222[1].setTop(0, 560, 522, false, false);
                                        messageDrawableArr2222[1].draw(canvas);
                                        messageDrawableArr2222[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                        messageDrawableArr2222[1].setTop(430, 560, 522, false, false);
                                        messageDrawableArr2222[1].draw(canvas);
                                        messageDrawableArr2222[0].setBounds(20, 323, 399, 415);
                                        messageDrawableArr2222[0].setTop(323, 560, 522, false, false);
                                        messageDrawableArr2222[0].draw(canvas);
                                        paint322222222222.setColor(previewColor3);
                                        canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint322222222222);
                                        if (drawable2 != null) {
                                        }
                                        if (drawable3 != null) {
                                        }
                                        canvas.setBitmap(null);
                                        File file222222222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                        createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file222222222222));
                                        SharedConfig.saveConfig();
                                        return file222222222222.getAbsolutePath();
                                    }
                                }
                                if (i7 != 0) {
                                    i8 = i7;
                                }
                                c = 0;
                                int i282 = i8;
                                if (!TextUtils.isEmpty(strArr[c])) {
                                }
                                i14 = i25;
                                i10 = i5;
                                i15 = i282;
                                i13 = i9;
                                mutate = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_back).mutate();
                                setDrawableColor(mutate, previewColor2);
                                Drawable mutate222222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_dots).mutate();
                                setDrawableColor(mutate222222, previewColor2);
                                Drawable mutate322222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_smile).mutate();
                                setDrawableColor(mutate322222, previewColor4);
                                Drawable mutate422222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_mic).mutate();
                                setDrawableColor(mutate422222, previewColor4);
                                MessageDrawable[] messageDrawableArr22222 = new MessageDrawable[2];
                                i18 = 0;
                                while (i18 < i17) {
                                }
                                drawable = mutate222222;
                                drawable2 = mutate322222;
                                drawable3 = mutate422222;
                                RectF rectF22222 = new RectF();
                                int i2922222 = 80;
                                if (str2 == null) {
                                }
                                i20 = 80;
                                z = false;
                                if (z) {
                                }
                                Paint paint3222222222222 = paint;
                                paint3222222222222.setColor(previewColor);
                                canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint3222222222222);
                                if (mutate != null) {
                                }
                                if (drawable != null) {
                                }
                                messageDrawableArr22222[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                                messageDrawableArr22222[1].setTop(0, 560, 522, false, false);
                                messageDrawableArr22222[1].draw(canvas);
                                messageDrawableArr22222[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                                messageDrawableArr22222[1].setTop(430, 560, 522, false, false);
                                messageDrawableArr22222[1].draw(canvas);
                                messageDrawableArr22222[0].setBounds(20, 323, 399, 415);
                                messageDrawableArr22222[0].setTop(323, 560, 522, false, false);
                                messageDrawableArr22222[0].draw(canvas);
                                paint3222222222222.setColor(previewColor3);
                                canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint3222222222222);
                                if (drawable2 != null) {
                                }
                                if (drawable3 != null) {
                                }
                                canvas.setBitmap(null);
                                File file2222222222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                                createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file2222222222222));
                                SharedConfig.saveConfig();
                                return file2222222222222.getAbsolutePath();
                            }
                        }
                        if (i3 != 0) {
                            i25 = i3;
                        }
                        if (themeAccent != null) {
                        }
                        if (i6 != 0) {
                        }
                        if (i6 == 0) {
                        }
                        if (themeAccent != null) {
                        }
                        if (i7 == 0) {
                        }
                        i8 = i4;
                        i9 = i6;
                        if (i7 != 0) {
                        }
                        c = 0;
                        int i2822 = i8;
                        if (!TextUtils.isEmpty(strArr[c])) {
                        }
                        i14 = i25;
                        i10 = i5;
                        i15 = i2822;
                        i13 = i9;
                        mutate = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_back).mutate();
                        setDrawableColor(mutate, previewColor2);
                        Drawable mutate2222222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_dots).mutate();
                        setDrawableColor(mutate2222222, previewColor2);
                        Drawable mutate3222222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_smile).mutate();
                        setDrawableColor(mutate3222222, previewColor4);
                        Drawable mutate4222222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_mic).mutate();
                        setDrawableColor(mutate4222222, previewColor4);
                        MessageDrawable[] messageDrawableArr222222 = new MessageDrawable[2];
                        i18 = 0;
                        while (i18 < i17) {
                        }
                        drawable = mutate2222222;
                        drawable2 = mutate3222222;
                        drawable3 = mutate4222222;
                        RectF rectF222222 = new RectF();
                        int i29222222 = 80;
                        if (str2 == null) {
                        }
                        i20 = 80;
                        z = false;
                        if (z) {
                        }
                        Paint paint32222222222222 = paint;
                        paint32222222222222.setColor(previewColor);
                        canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint32222222222222);
                        if (mutate != null) {
                        }
                        if (drawable != null) {
                        }
                        messageDrawableArr222222[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
                        messageDrawableArr222222[1].setTop(0, 560, 522, false, false);
                        messageDrawableArr222222[1].draw(canvas);
                        messageDrawableArr222222[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
                        messageDrawableArr222222[1].setTop(430, 560, 522, false, false);
                        messageDrawableArr222222[1].draw(canvas);
                        messageDrawableArr222222[0].setBounds(20, 323, 399, 415);
                        messageDrawableArr222222[0].setTop(323, 560, 522, false, false);
                        messageDrawableArr222222[0].draw(canvas);
                        paint32222222222222.setColor(previewColor3);
                        canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint32222222222222);
                        if (drawable2 != null) {
                        }
                        if (drawable3 != null) {
                        }
                        canvas.setBitmap(null);
                        File file22222222222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
                        createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file22222222222222));
                        SharedConfig.saveConfig();
                        return file22222222222222.getAbsolutePath();
                    }
                }
                createBitmap.compress(Bitmap.CompressFormat.JPEG, i20, new FileOutputStream(file22222222222222));
                SharedConfig.saveConfig();
                return file22222222222222.getAbsolutePath();
            } catch (Throwable th5) {
                FileLog.e(th5);
                return null;
            }
            if (i == 0) {
                i = i24;
            }
            if (themeAccent == null) {
            }
            if (i3 == 0) {
            }
            i4 = i2;
            i5 = i;
            if (i3 != 0) {
            }
            if (themeAccent != null) {
            }
            if (i6 != 0) {
            }
            if (i6 == 0) {
            }
            if (themeAccent != null) {
            }
            if (i7 == 0) {
            }
            i8 = i4;
            i9 = i6;
            if (i7 != 0) {
            }
            c = 0;
            int i28222 = i8;
            if (!TextUtils.isEmpty(strArr[c])) {
            }
            i14 = i25;
            i10 = i5;
            i15 = i28222;
            i13 = i9;
            mutate = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_back).mutate();
            setDrawableColor(mutate, previewColor2);
            Drawable mutate22222222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_dots).mutate();
            setDrawableColor(mutate22222222, previewColor2);
            Drawable mutate32222222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_smile).mutate();
            setDrawableColor(mutate32222222, previewColor4);
            Drawable mutate42222222 = ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.preview_mic).mutate();
            setDrawableColor(mutate42222222, previewColor4);
            MessageDrawable[] messageDrawableArr2222222 = new MessageDrawable[2];
            i18 = 0;
            while (i18 < i17) {
            }
            drawable = mutate22222222;
            drawable2 = mutate32222222;
            drawable3 = mutate42222222;
            RectF rectF2222222 = new RectF();
            int i292222222 = 80;
            if (str2 == null) {
            }
            i20 = 80;
            z = false;
            if (z) {
            }
            Paint paint322222222222222 = paint;
            paint322222222222222.setColor(previewColor);
            canvas.drawRect(0.0f, 0.0f, createBitmap.getWidth(), 120.0f, paint322222222222222);
            if (mutate != null) {
            }
            if (drawable != null) {
            }
            messageDrawableArr2222222[1].setBounds(NotificationCenter.didStartedCall, NotificationCenter.botStarsUpdated, createBitmap.getWidth() - 20, 308);
            messageDrawableArr2222222[1].setTop(0, 560, 522, false, false);
            messageDrawableArr2222222[1].draw(canvas);
            messageDrawableArr2222222[1].setBounds(NotificationCenter.didStartedCall, 430, createBitmap.getWidth() - 20, 522);
            messageDrawableArr2222222[1].setTop(430, 560, 522, false, false);
            messageDrawableArr2222222[1].draw(canvas);
            messageDrawableArr2222222[0].setBounds(20, 323, 399, 415);
            messageDrawableArr2222222[0].setTop(323, 560, 522, false, false);
            messageDrawableArr2222222[0].draw(canvas);
            paint322222222222222.setColor(previewColor3);
            canvas.drawRect(0.0f, createBitmap.getHeight() - 120, createBitmap.getWidth(), createBitmap.getHeight(), paint322222222222222);
            if (drawable2 != null) {
            }
            if (drawable3 != null) {
            }
            canvas.setBitmap(null);
            File file222222222222222 = new File(FileLoader.getDirectory(4), "-2147483648_" + SharedConfig.getLastLocalId() + ".jpg");
        } catch (Throwable th6) {
            FileLog.e(th6);
            return null;
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
            currentNightTheme = (ThemeInfo) themesDict.get("Dark Blue");
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
        TLRPC.TL_theme tL_theme = themeAccent.info;
        if (tL_theme != null) {
            themeInfo.accentsByThemeId.remove(tL_theme.id);
        }
        OverrideWallpaperInfo overrideWallpaperInfo = themeAccent.overrideWallpaper;
        if (overrideWallpaperInfo != null) {
            overrideWallpaperInfo.delete();
        }
        if (z3) {
            themeInfo.setCurrentAccentId(((ThemeAccent) themeInfo.themeAccents.get(0)).id);
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

    public static void destroyResources() {
    }

    public static ThemeInfo fillThemeValues(File file, String str, TLRPC.TL_theme tL_theme) {
        String[] split;
        try {
            ThemeInfo themeInfo = new ThemeInfo();
            themeInfo.name = str;
            themeInfo.info = tL_theme;
            themeInfo.pathToFile = file.getAbsolutePath();
            themeInfo.account = UserConfig.selectedAccount;
            String[] strArr = new String[1];
            checkIsDark(getThemeFileValues(new File(themeInfo.pathToFile), null, strArr), themeInfo);
            if (TextUtils.isEmpty(strArr[0])) {
                themedWallpaperLink = null;
            } else {
                String str2 = strArr[0];
                themeInfo.pathToWallpaper = new File(ApplicationLoader.getFilesDirFixed(), Utilities.MD5(str2) + ".wp").getAbsolutePath();
                try {
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
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
            return themeInfo;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
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
        float f = ((((tempHsv2[2] / tempHsv[2]) + min) - 1.0f) * fArr[2]) / min;
        tempHsv[2] = f;
        return f < 0.3f ? i2 : Color.HSVToColor(NotificationCenter.closeSearchByActiveAction, tempHsv);
    }

    public static ThemeInfo getActiveTheme() {
        return currentTheme;
    }

    public static ColorFilter getAnimatedEmojiColorFilter(ResourcesProvider resourcesProvider) {
        return resourcesProvider != null ? resourcesProvider.getAnimatedEmojiColorFilter() : chat_animatedEmojiTextColorFilter;
    }

    public static AudioVisualizerDrawable getAnimatedOutAudioVisualizerDrawable(MessageObject messageObject) {
        HashMap hashMap = animatedOutVisualizerDrawables;
        if (hashMap == null || messageObject == null) {
            return null;
        }
        return (AudioVisualizerDrawable) hashMap.get(messageObject);
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
                try {
                    AndroidUtilities.copyFile(open2, file);
                    if (open2 != null) {
                        open2.close();
                    }
                } finally {
                }
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        return file;
    }

    private static long getAutoNightSwitchThemeDelay() {
        return Math.abs(lastThemeSwitchTime - SystemClock.elapsedRealtime()) >= 12000 ? 1800L : 12000L;
    }

    public static TLRPC.BaseTheme getBaseThemeByKey(String str) {
        if ("Blue".equals(str)) {
            return new TLRPC.TL_baseThemeClassic();
        }
        if ("Day".equals(str)) {
            return new TLRPC.TL_baseThemeDay();
        }
        if ("Dark Blue".equals(str)) {
            return new TLRPC.TL_baseThemeTinted();
        }
        if ("Arctic Blue".equals(str)) {
            return new TLRPC.TL_baseThemeArctic();
        }
        if ("Night".equals(str)) {
            return new TLRPC.TL_baseThemeNight();
        }
        return null;
    }

    public static String getBaseThemeKey(TLRPC.ThemeSettings themeSettings) {
        TLRPC.BaseTheme baseTheme = themeSettings.base_theme;
        if (baseTheme instanceof TLRPC.TL_baseThemeClassic) {
            return "Blue";
        }
        if (baseTheme instanceof TLRPC.TL_baseThemeDay) {
            return "Day";
        }
        if (baseTheme instanceof TLRPC.TL_baseThemeTinted) {
            return "Dark Blue";
        }
        if (baseTheme instanceof TLRPC.TL_baseThemeArctic) {
            return "Arctic Blue";
        }
        if (baseTheme instanceof TLRPC.TL_baseThemeNight) {
            return "Night";
        }
        return null;
    }

    public static Drawable getCachedWallpaper() {
        Drawable cachedWallpaperNonBlocking = getCachedWallpaperNonBlocking();
        if (cachedWallpaperNonBlocking != null || wallpaperLoadTask == null) {
            return cachedWallpaperNonBlocking;
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Utilities.themeQueue.postRunnable(new Theme$$ExternalSyntheticLambda18(countDownLatch));
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

    public static int getColor(int i) {
        return getColor(i, null, false);
    }

    public static int getColor(int i, ResourcesProvider resourcesProvider) {
        return resourcesProvider != null ? resourcesProvider.getColor(i) : getColor(i);
    }

    public static int getColor(int i, boolean[] zArr) {
        return getColor(i, zArr, false);
    }

    public static int getColor(int i, boolean[] zArr, boolean z) {
        int indexOfKey;
        SparseIntArray sparseIntArray;
        SparseIntArray sparseIntArray2;
        if (!z && (sparseIntArray2 = animatingColors) != null && (indexOfKey = sparseIntArray2.indexOfKey(i)) >= 0) {
            sparseIntArray = animatingColors;
        } else {
            if (serviceBitmapShader != null && (key_chat_serviceText == i || key_chat_serviceLink == i || key_chat_serviceIcon == i || key_chat_stickerReplyLine == i || key_chat_stickerReplyNameText == i || key_chat_stickerReplyMessageText == i)) {
                return -1;
            }
            if (currentTheme == defaultTheme) {
                if (isMyMessagesBubbles(i) ? currentTheme.isDefaultMyMessagesBubbles() : isMyMessages(i) ? currentTheme.isDefaultMyMessages() : (key_chat_wallpaper == i || key_chat_wallpaper_gradient_to1 == i || key_chat_wallpaper_gradient_to2 == i || key_chat_wallpaper_gradient_to3 == i) ? false : currentTheme.isDefaultMainAccent()) {
                    return i == key_chat_serviceBackground ? serviceMessageColor : i == key_chat_serviceBackgroundSelected ? serviceSelectedMessageColor : getDefaultColor(i);
                }
            }
            int indexOfKey2 = currentColors.indexOfKey(i);
            if (indexOfKey2 >= 0) {
                int valueAt = currentColors.valueAt(indexOfKey2);
                return (key_windowBackgroundWhite == i || key_windowBackgroundGray == i || key_actionBarDefault == i || key_actionBarDefaultArchived == i) ? valueAt | (-16777216) : valueAt;
            }
            int i2 = fallbackKeys.get(i, -1);
            if (i2 == -1 || (indexOfKey = currentColors.indexOfKey(i2)) < 0) {
                if (zArr != null) {
                    zArr[0] = true;
                }
                return i == key_chat_serviceBackground ? serviceMessageColor : i == key_chat_serviceBackgroundSelected ? serviceSelectedMessageColor : getDefaultColor(i);
            }
            sparseIntArray = currentColors;
        }
        return sparseIntArray.valueAt(indexOfKey);
    }

    public static AudioVisualizerDrawable getCurrentAudiVisualizerDrawable() {
        if (chat_msgAudioVisualizeDrawable == null) {
            chat_msgAudioVisualizeDrawable = new AudioVisualizerDrawable();
        }
        return chat_msgAudioVisualizeDrawable;
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

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0056, code lost:
    
        if (r2 <= 31) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x005c, code lost:
    
        org.telegram.ui.ActionBar.Theme.dialogs_holidayDrawable = org.telegram.messenger.ApplicationLoader.applicationContext.getResources().getDrawable(org.telegram.messenger.R.drawable.newyear);
        org.telegram.ui.ActionBar.Theme.dialogs_holidayDrawableOffsetX = -org.telegram.messenger.AndroidUtilities.dp(3.0f);
        org.telegram.ui.ActionBar.Theme.dialogs_holidayDrawableOffsetY = -org.telegram.messenger.AndroidUtilities.dp(-7.0f);
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x005a, code lost:
    
        if (r2 == 1) goto L22;
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

    public static ThemeInfo getCurrentNightTheme() {
        return currentNightTheme;
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

    public static int getDefaultAccentColor(int i) {
        int indexOfKey = currentColorsNoAccent.indexOfKey(i);
        if (indexOfKey < 0) {
            return 0;
        }
        int valueAt = currentColorsNoAccent.valueAt(indexOfKey);
        ThemeAccent accent = currentTheme.getAccent(false);
        if (accent == null) {
            return 0;
        }
        float[] tempHsv = getTempHsv(1);
        float[] tempHsv2 = getTempHsv(2);
        Color.colorToHSV(currentTheme.accentBaseColor, tempHsv);
        Color.colorToHSV(accent.accentColor, tempHsv2);
        return changeColorAccent(tempHsv, tempHsv2, valueAt, currentTheme.isDark(), valueAt);
    }

    public static int getDefaultColor(int i) {
        int i2 = defaultColors[i];
        return i2 == 0 ? (isMyMessagesBubbles(i) || i == key_chats_menuTopShadow || i == key_chats_menuTopBackground || i == key_chats_menuTopShadowCats || i == key_chat_wallpaper_gradient_to2 || i == key_chat_wallpaper_gradient_to3) ? 0 : -65536 : i2;
    }

    public static int[] getDefaultColors() {
        return defaultColors;
    }

    public static int getEventType() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i = calendar.get(2);
        int i2 = calendar.get(5);
        calendar.get(12);
        int i3 = calendar.get(11);
        if ((i == 11 && i2 >= 24 && i2 <= 31) || (i == 0 && i2 == 1)) {
            return 0;
        }
        if (i == 1 && i2 == 14) {
            return 1;
        }
        if (i != 9 || i2 < 30) {
            return (i == 10 && i2 == 1 && i3 < 12) ? 2 : -1;
        }
        return 2;
    }

    public static int getFallbackKey(int i) {
        return fallbackKeys.get(i);
    }

    public static SparseIntArray getFallbackKeys() {
        return fallbackKeys;
    }

    public static FragmentContextViewWavesDrawable getFragmentContextViewWavesDrawable() {
        if (fragmentContextViewWavesDrawable == null) {
            fragmentContextViewWavesDrawable = new FragmentContextViewWavesDrawable();
        }
        return fragmentContextViewWavesDrawable;
    }

    public static int getNonAnimatedColor(int i) {
        return getColor(i, null, true);
    }

    public static int getPreviewColor(SparseIntArray sparseIntArray, int i) {
        int indexOfKey = sparseIntArray.indexOfKey(i);
        return indexOfKey >= 0 ? sparseIntArray.valueAt(indexOfKey) : defaultColors[i];
    }

    public static ThemeInfo getPreviousTheme() {
        return previousTheme;
    }

    public static RoundVideoProgressShadow getRadialSeekbarShadowDrawable() {
        if (roundPlayDrawable == null) {
            roundPlayDrawable = new RoundVideoProgressShadow();
        }
        return roundPlayDrawable;
    }

    public static Drawable getRoundRectSelectorDrawable(int i) {
        return getRoundRectSelectorDrawable(AndroidUtilities.dp(3.0f), i);
    }

    public static Drawable getRoundRectSelectorDrawable(int i, int i2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{(i2 & 16777215) | 419430400}), null, createRoundRectDrawable(i, -1));
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        int i3 = (i2 & 16777215) | 419430400;
        stateListDrawable.addState(new int[]{R.attr.state_pressed}, createRoundRectDrawable(i, i3));
        stateListDrawable.addState(new int[]{R.attr.state_selected}, createRoundRectDrawable(i, i3));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0));
        return stateListDrawable;
    }

    public static String getSelectedBackgroundSlug() {
        OverrideWallpaperInfo overrideWallpaperInfo = currentTheme.overrideWallpaper;
        return overrideWallpaperInfo != null ? overrideWallpaperInfo.slug : hasWallpaperFromTheme() ? "t" : "d";
    }

    public static Drawable getSelectorDrawable(int i, int i2) {
        if (i2 < 0) {
            return createSelectorDrawable(i, 2);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return new BaseCell.RippleDrawableSafe(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}), new ColorDrawable(getColor(i2)), new ColorDrawable(-1));
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{R.attr.state_pressed}, new ColorDrawable(i));
        stateListDrawable.addState(new int[]{R.attr.state_selected}, new ColorDrawable(i));
        stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(getColor(i2)));
        return stateListDrawable;
    }

    public static Drawable getSelectorDrawable(int i, boolean z) {
        return z ? getSelectorDrawable(i, key_windowBackgroundWhite) : createSelectorDrawable(i, 2);
    }

    public static Drawable getSelectorDrawable(boolean z) {
        return getSelectorDrawable(getColor(key_listSelector), z);
    }

    public static Drawable getSelectorDrawable(boolean z, ResourcesProvider resourcesProvider) {
        return getSelectorDrawable(getColor(key_listSelector, resourcesProvider), z);
    }

    public static int getServiceMessageColor() {
        int indexOfKey = currentColors.indexOfKey(key_chat_serviceBackground);
        return indexOfKey >= 0 ? currentColors.valueAt(indexOfKey) : serviceMessageColor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Drawable getStateDrawable(Drawable drawable, int i) {
        Drawable stateDrawable;
        if (Build.VERSION.SDK_INT >= 29 && (drawable instanceof StateListDrawable)) {
            stateDrawable = ((StateListDrawable) drawable).getStateDrawable(i);
            return stateDrawable;
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

    public static float[] getTempHsv(int i) {
        ThreadLocal threadLocal = i != 1 ? i != 2 ? i != 3 ? i != 4 ? hsvTemp5Local : hsvTemp4Local : hsvTemp3Local : hsvTemp2Local : hsvTemp1Local;
        float[] fArr = (float[]) threadLocal.get();
        if (fArr != null) {
            return fArr;
        }
        float[] fArr2 = new float[3];
        threadLocal.set(fArr2);
        return fArr2;
    }

    public static ThemeInfo getTheme(String str) {
        return (ThemeInfo) themesDict.get(str);
    }

    public static Drawable getThemeDrawable(String str) {
        return (Drawable) defaultChatDrawables.get(str);
    }

    public static int getThemeDrawableColorKey(String str) {
        return ((Integer) defaultChatDrawableColorKeys.get(str)).intValue();
    }

    public static Map getThemeDrawablesMap() {
        return defaultChatDrawables;
    }

    /* JADX WARN: Code restructure failed: missing block: B:67:0x00bc, code lost:
    
        if (r4 == null) goto L59;
     */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0092 A[Catch: all -> 0x0050, TryCatch #0 {all -> 0x0050, blocks: (B:9:0x0021, B:13:0x002c, B:15:0x0032, B:18:0x0045, B:20:0x0048, B:21:0x0095, B:24:0x0053, B:42:0x005b, B:26:0x005f, B:28:0x0067, B:30:0x0077, B:34:0x007f, B:35:0x008c, B:37:0x0092, B:40:0x0084, B:23:0x0097, B:44:0x009e, B:49:0x00ad), top: B:8:0x0021 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static SparseIntArray getThemeFileValues(File file, String str, String[] strArr) {
        FileInputStream fileInputStream;
        int intValue;
        int stringKeyToInt;
        SparseIntArray sparseIntArray = new SparseIntArray();
        try {
            byte[] bArr = new byte[1024];
            fileInputStream = new FileInputStream(str != null ? getAssetFile(str) : file);
            int i = -1;
            int i2 = 0;
            int i3 = -1;
            boolean z = false;
            while (true) {
                try {
                    int read = fileInputStream.read(bArr);
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
                            int i7 = i5 - i6;
                            int i8 = i7 + 1;
                            String str2 = new String(bArr, i6, i7);
                            if (str2.startsWith("WLS=")) {
                                if (strArr != null && strArr.length > 0) {
                                    strArr[0] = str2.substring(4);
                                }
                            } else {
                                if (str2.startsWith("WPS")) {
                                    i3 = i4 + i8;
                                    z = true;
                                    break;
                                }
                                int indexOf = str2.indexOf(61);
                                if (indexOf != i) {
                                    String substring = str2.substring(0, indexOf);
                                    String substring2 = str2.substring(indexOf + 1);
                                    if (substring2.length() > 0 && substring2.charAt(0) == '#') {
                                        try {
                                            intValue = Color.parseColor(substring2);
                                        } catch (Exception unused) {
                                        }
                                        stringKeyToInt = ThemeColors.stringKeyToInt(substring);
                                        if (stringKeyToInt >= 0) {
                                            sparseIntArray.put(stringKeyToInt, intValue);
                                        }
                                    }
                                    intValue = Utilities.parseInt((CharSequence) substring2).intValue();
                                    stringKeyToInt = ThemeColors.stringKeyToInt(substring);
                                    if (stringKeyToInt >= 0) {
                                    }
                                }
                            }
                            i6 += i8;
                            i4 += i8;
                        }
                        i5++;
                        i = -1;
                    }
                    if (i2 == i4) {
                        break;
                    }
                    fileInputStream.getChannel().position(i4);
                    if (z) {
                        break;
                    }
                    i2 = i4;
                    i = -1;
                } catch (Throwable th) {
                    th = th;
                    try {
                        FileLog.e(th);
                    } finally {
                    }
                }
            }
            sparseIntArray.put(key_wallpaperFileOffset, i3);
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
        }
        try {
            fileInputStream.close();
        } catch (Exception e) {
            FileLog.e(e);
        }
        return sparseIntArray;
    }

    public static void getThemeFileValuesInBackground(final File file, final String str, final String[] strArr, final Utilities.Callback callback) {
        Utilities.themeQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$getThemeFileValuesInBackground$10(Utilities.Callback.this, file, str, strArr);
            }
        });
    }

    public static float getThemeIntensity(float f) {
        return (f >= 0.0f || getActiveTheme().isDark()) ? f : -f;
    }

    public static Paint getThemePaint(String str) {
        return Objects.equals(str, "paintDivider") ? dividerPaint : (Paint) defaultChatPaints.get(str);
    }

    public static Paint getThemePaint(String str, ResourcesProvider resourcesProvider) {
        Paint paint;
        return (resourcesProvider == null || (paint = resourcesProvider.getPaint(str)) == null) ? getThemePaint(str) : paint;
    }

    public static int getThemePaintColorKey(String str) {
        return ((Integer) defaultChatPaintColors.get(str)).intValue();
    }

    public static Map getThemePaintsMap() {
        return defaultChatPaints;
    }

    public static Drawable getThemedDrawable(Context context, int i, int i2) {
        if (context == null) {
            return null;
        }
        Drawable mutate = context.getResources().getDrawable(i).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.MULTIPLY));
        return mutate;
    }

    public static Drawable getThemedDrawableByKey(Context context, int i, int i2) {
        return getThemedDrawable(context, i, getColor(i2));
    }

    public static Drawable getThemedDrawableByKey(Context context, int i, int i2, ResourcesProvider resourcesProvider) {
        return getThemedDrawable(context, i, getColor(i2, resourcesProvider));
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x00c1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static Drawable getThemedWallpaper(final boolean z, final View view) {
        MotionBackgroundDrawable motionBackgroundDrawable;
        File file;
        int i;
        FileInputStream fileInputStream;
        Bitmap.Config config;
        Bitmap decodeStream;
        File pathToWallpaper;
        int i2 = currentColors.get(key_chat_wallpaper);
        if (i2 != 0) {
            int i3 = currentColors.get(key_chat_wallpaper_gradient_to1);
            int i4 = currentColors.get(key_chat_wallpaper_gradient_to2);
            int i5 = currentColors.get(key_chat_wallpaper_gradient_to3);
            int i6 = currentColors.get(key_chat_wallpaper_gradient_rotation, -1);
            if (i6 == -1) {
                i6 = 45;
            }
            if (i3 == 0) {
                return new ColorDrawable(i2);
            }
            ThemeAccent accent = currentTheme.getAccent(false);
            file = (accent == null || TextUtils.isEmpty(accent.patternSlug) || previousTheme != null || (pathToWallpaper = accent.getPathToWallpaper()) == null || !pathToWallpaper.exists()) ? null : pathToWallpaper;
            if (i4 != 0) {
                MotionBackgroundDrawable motionBackgroundDrawable2 = new MotionBackgroundDrawable(i2, i3, i4, i5, true);
                if (file == null) {
                    return motionBackgroundDrawable2;
                }
                motionBackgroundDrawable = motionBackgroundDrawable2;
            } else {
                if (file == null) {
                    BackgroundGradientDrawable backgroundGradientDrawable = new BackgroundGradientDrawable(BackgroundGradientDrawable.getGradientOrientation(i6), new int[]{i2, i3});
                    backgroundGradientDrawable.startDithering(!z ? BackgroundGradientDrawable.Sizes.ofDeviceScreen() : BackgroundGradientDrawable.Sizes.ofDeviceScreen(0.125f, BackgroundGradientDrawable.Sizes.Orientation.PORTRAIT), view != null ? new BackgroundGradientDrawable.ListenerAdapter() { // from class: org.telegram.ui.ActionBar.Theme.13
                        @Override // org.telegram.ui.Components.BackgroundGradientDrawable.ListenerAdapter, org.telegram.ui.Components.BackgroundGradientDrawable.Listener
                        public void onSizeReady(int i7, int i8) {
                            if (!z) {
                                Point point = AndroidUtilities.displaySize;
                                if ((point.x <= point.y) != (i7 <= i8)) {
                                    return;
                                }
                            }
                            view.invalidate();
                        }
                    } : null);
                    return backgroundGradientDrawable;
                }
                motionBackgroundDrawable = null;
            }
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
                                int i7 = 1;
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
                                        i7 *= 2;
                                        f /= 2.0f;
                                        f2 /= 2.0f;
                                    }
                                }
                                config = Bitmap.Config.ALPHA_8;
                                options.inPreferredConfig = config;
                                options.inJustDecodeBounds = false;
                                options.inSampleSize = i7;
                                decodeStream = BitmapFactory.decodeStream(fileInputStream, null, options);
                            } catch (Throwable th) {
                                th = th;
                                try {
                                    FileLog.e(th);
                                } finally {
                                    if (fileInputStream != null) {
                                        try {
                                            fileInputStream.close();
                                        } catch (Exception e) {
                                            FileLog.e(e);
                                        }
                                    }
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            fileInputStream = null;
                        }
                        if (motionBackgroundDrawable != null) {
                            ThemeAccent accent2 = currentTheme.getAccent(false);
                            int i8 = accent2 != null ? (int) (accent2.patternIntensity * 100.0f) : 100;
                            if (decodeStream != null && decodeStream.getConfig() != config) {
                                Bitmap copy = decodeStream.copy(config, false);
                                decodeStream.recycle();
                                decodeStream = copy;
                            }
                            motionBackgroundDrawable.setPatternBitmap(i8, decodeStream);
                            motionBackgroundDrawable.setPatternColorFilter(motionBackgroundDrawable.getPatternColor());
                            try {
                                fileInputStream.close();
                            } catch (Exception e2) {
                                FileLog.e(e2);
                            }
                            return motionBackgroundDrawable;
                        }
                        if (decodeStream != null) {
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(decodeStream);
                            try {
                                fileInputStream.close();
                            } catch (Exception e3) {
                                FileLog.e(e3);
                            }
                            return bitmapDrawable;
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

    public static int getWallpaperColor(int i) {
        if (i == 0) {
            return 0;
        }
        return i | (-16777216);
    }

    private static String getWallpaperUrl(OverrideWallpaperInfo overrideWallpaperInfo) {
        StringBuilder sb;
        StringBuilder sb2;
        if (overrideWallpaperInfo == null || TextUtils.isEmpty(overrideWallpaperInfo.slug) || overrideWallpaperInfo.slug.equals("d")) {
            return null;
        }
        StringBuilder sb3 = new StringBuilder();
        if (overrideWallpaperInfo.isBlurred) {
            sb3.append("blur");
        }
        if (overrideWallpaperInfo.isMotion) {
            if (sb3.length() > 0) {
                sb3.append("+");
            }
            sb3.append("motion");
        }
        int i = overrideWallpaperInfo.color;
        if (i == 0) {
            sb2 = new StringBuilder();
            sb2.append("https://attheme.org?slug=");
            sb2.append(overrideWallpaperInfo.slug);
        } else {
            String lowerCase = String.format("%02x%02x%02x", Integer.valueOf(((byte) (i >> 16)) & 255), Integer.valueOf(((byte) (overrideWallpaperInfo.color >> 8)) & 255), Byte.valueOf((byte) (overrideWallpaperInfo.color & NotificationCenter.closeSearchByActiveAction))).toLowerCase();
            int i2 = overrideWallpaperInfo.gradientColor1;
            String lowerCase2 = i2 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i2 >> 16)) & 255), Integer.valueOf(((byte) (overrideWallpaperInfo.gradientColor1 >> 8)) & 255), Byte.valueOf((byte) (overrideWallpaperInfo.gradientColor1 & NotificationCenter.closeSearchByActiveAction))).toLowerCase() : null;
            int i3 = overrideWallpaperInfo.gradientColor2;
            String lowerCase3 = i3 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i3 >> 16)) & 255), Integer.valueOf(((byte) (overrideWallpaperInfo.gradientColor2 >> 8)) & 255), Byte.valueOf((byte) (overrideWallpaperInfo.gradientColor2 & NotificationCenter.closeSearchByActiveAction))).toLowerCase() : null;
            int i4 = overrideWallpaperInfo.gradientColor3;
            String lowerCase4 = i4 != 0 ? String.format("%02x%02x%02x", Integer.valueOf(((byte) (i4 >> 16)) & 255), Integer.valueOf(((byte) (overrideWallpaperInfo.gradientColor3 >> 8)) & 255), Byte.valueOf((byte) (overrideWallpaperInfo.gradientColor3 & NotificationCenter.closeSearchByActiveAction))).toLowerCase() : null;
            if (lowerCase2 == null || lowerCase3 == null) {
                if (lowerCase2 != null) {
                    sb = new StringBuilder();
                    sb.append(lowerCase + "-" + lowerCase2);
                    sb.append("&rotation=");
                    sb.append(overrideWallpaperInfo.rotation);
                }
                sb2 = new StringBuilder();
                sb2.append("https://attheme.org?slug=");
                sb2.append(overrideWallpaperInfo.slug);
                sb2.append("&intensity=");
                sb2.append((int) (overrideWallpaperInfo.intensity * 100.0f));
                sb2.append("&bg_color=");
                sb2.append(lowerCase);
            } else if (lowerCase4 != null) {
                sb = new StringBuilder();
                sb.append(lowerCase);
                sb.append("~");
                sb.append(lowerCase2);
                sb.append("~");
                sb.append(lowerCase3);
                sb.append("~");
                sb.append(lowerCase4);
            } else {
                sb = new StringBuilder();
                sb.append(lowerCase);
                sb.append("~");
                sb.append(lowerCase2);
                sb.append("~");
                sb.append(lowerCase3);
            }
            lowerCase = sb.toString();
            sb2 = new StringBuilder();
            sb2.append("https://attheme.org?slug=");
            sb2.append(overrideWallpaperInfo.slug);
            sb2.append("&intensity=");
            sb2.append((int) (overrideWallpaperInfo.intensity * 100.0f));
            sb2.append("&bg_color=");
            sb2.append(lowerCase);
        }
        String sb4 = sb2.toString();
        if (sb3.length() <= 0) {
            return sb4;
        }
        return sb4 + "&mode=" + sb3.toString();
    }

    public static boolean hasCustomWallpaper() {
        return isApplyingAccent && currentTheme.overrideWallpaper != null;
    }

    public static boolean hasGradientService() {
        return serviceBitmapShader != null;
    }

    public static boolean hasHue(int i) {
        float[] tempHsv = getTempHsv(3);
        Color.colorToHSV(i, tempHsv);
        float f = tempHsv[1];
        return f > 0.1f && f < 0.9f;
    }

    public static boolean hasThemeKey(int i) {
        return currentColors.indexOfKey(i) >= 0;
    }

    public static boolean hasWallpaperFromTheme() {
        ThemeInfo themeInfo = currentTheme;
        if (themeInfo.firstAccentIsDefault && themeInfo.currentAccentId == DEFALT_THEME_ACCENT_ID) {
            return false;
        }
        return currentColors.indexOfKey(key_chat_wallpaper) >= 0 || themedWallpaperFileOffset > 0 || !TextUtils.isEmpty(themedWallpaperLink);
    }

    public static boolean isAnimatingColor() {
        return animatingColors != null;
    }

    public static boolean isCurrentThemeDark() {
        return currentTheme.isDark();
    }

    public static boolean isCurrentThemeDay() {
        return !getActiveTheme().isDark();
    }

    public static boolean isCurrentThemeNight() {
        return currentTheme == currentNightTheme;
    }

    public static boolean isCustomTheme() {
        return isCustomTheme;
    }

    public static boolean isCustomWallpaperColor() {
        return hasCustomWallpaper() && currentTheme.overrideWallpaper.color != 0;
    }

    public static boolean isHome(ThemeAccent themeAccent) {
        ThemeInfo themeInfo = themeAccent.parentTheme;
        if (themeInfo == null) {
            return false;
        }
        if (themeInfo.getKey().equals("Blue") && themeAccent.id == 99) {
            return true;
        }
        if (themeAccent.parentTheme.getKey().equals("Day") && themeAccent.id == 9) {
            return true;
        }
        return (themeAccent.parentTheme.getKey().equals("Night") || themeAccent.parentTheme.getKey().equals("Dark Blue")) && themeAccent.id == 0;
    }

    private static boolean isMyMessages(int i) {
        return i >= myMessagesStartIndex && i < myMessagesEndIndex;
    }

    private static boolean isMyMessagesBubbles(int i) {
        return i >= myMessagesBubblesStartIndex && i < myMessagesBubblesEndIndex;
    }

    public static boolean isPatternWallpaper() {
        String selectedBackgroundSlug = getSelectedBackgroundSlug();
        return isPatternWallpaper || "CJz3BZ6YGEYBAAAABboWp6SAv04".equals(selectedBackgroundSlug) || "qeZWES8rGVIEAAAARfWlK1lnfiI".equals(selectedBackgroundSlug);
    }

    public static boolean isThemeInstalled(ThemeInfo themeInfo) {
        return (themeInfo == null || themesDict.get(themeInfo.getKey()) == null) ? false : true;
    }

    public static boolean isThemeWallpaperPublic() {
        return !TextUtils.isEmpty(themedWallpaperLink);
    }

    public static boolean isWallpaperMotion() {
        return isWallpaperMotion;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$applyThemeInBackground$2(String[] strArr, ThemeInfo themeInfo, boolean z, boolean z2, Runnable runnable) {
        String[] split;
        try {
            themedWallpaperFileOffset = currentColorsNoAccent.get(key_wallpaperFileOffset, -1);
            if (TextUtils.isEmpty(strArr[0])) {
                try {
                    if (themeInfo.pathToWallpaper != null) {
                        new File(themeInfo.pathToWallpaper).delete();
                    }
                } catch (Exception unused) {
                }
                themeInfo.pathToWallpaper = null;
                themedWallpaperLink = null;
            } else {
                themedWallpaperLink = strArr[0];
                String absolutePath = new File(ApplicationLoader.getFilesDirFixed(), Utilities.MD5(themedWallpaperLink) + ".wp").getAbsolutePath();
                try {
                    String str = themeInfo.pathToWallpaper;
                    if (str != null && !str.equals(absolutePath)) {
                        new File(themeInfo.pathToWallpaper).delete();
                    }
                } catch (Exception unused2) {
                }
                themeInfo.pathToWallpaper = absolutePath;
                try {
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
                    } catch (Exception unused3) {
                    }
                    try {
                        String queryParameter3 = parse.getQueryParameter("rotation");
                        if (!TextUtils.isEmpty(queryParameter3)) {
                            themeInfo.patternBgGradientRotation = Utilities.parseInt((CharSequence) queryParameter3).intValue();
                        }
                    } catch (Exception unused4) {
                    }
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
            if (!z && previousTheme == null) {
                currentDayTheme = themeInfo;
                if (isCurrentThemeNight()) {
                    switchNightThemeDelay = 2000;
                    lastDelayUpdateTime = SystemClock.elapsedRealtime();
                    AndroidUtilities.runOnUIThread(new MessagesController$$ExternalSyntheticLambda91(), 2100L);
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
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$checkCurrentRemoteTheme$6(TLObject tLObject, ThemeAccent themeAccent, ThemeInfo themeInfo, TLRPC.TL_theme tL_theme) {
        boolean z;
        TLRPC.WallPaperSettings wallPaperSettings;
        loadingCurrentTheme--;
        if (tLObject instanceof TLRPC.TL_theme) {
            TLRPC.TL_theme tL_theme2 = (TLRPC.TL_theme) tLObject;
            TLRPC.ThemeSettings themeSettings = tL_theme2.settings.size() > 0 ? tL_theme2.settings.get(0) : null;
            if (themeAccent == null || themeSettings == null) {
                TLRPC.Document document = tL_theme2.document;
                if (document != null && document.id != tL_theme.document.id) {
                    if (themeAccent != null) {
                        themeAccent.info = tL_theme2;
                    } else {
                        themeInfo.info = tL_theme2;
                        themeInfo.loadThemeDocument();
                    }
                }
            } else {
                if (ThemeInfo.accentEquals(themeAccent, themeSettings)) {
                    z = false;
                } else {
                    File pathToWallpaper = themeAccent.getPathToWallpaper();
                    if (pathToWallpaper != null) {
                        pathToWallpaper.delete();
                    }
                    ThemeInfo.fillAccentValues(themeAccent, themeSettings);
                    ThemeInfo themeInfo2 = currentTheme;
                    if (themeInfo2 == themeInfo && themeInfo2.currentAccentId == themeAccent.id) {
                        refreshThemeColors();
                        createChatResources(ApplicationLoader.applicationContext, false);
                        NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
                        int i = NotificationCenter.needSetDayNightTheme;
                        ThemeInfo themeInfo3 = currentTheme;
                        globalInstance.lambda$postNotificationNameOnUIThread$1(i, themeInfo3, Boolean.valueOf(currentNightTheme == themeInfo3), null, -1);
                    }
                    PatternsLoader.createLoader(true);
                    z = true;
                }
                TLRPC.WallPaper wallPaper = themeSettings.wallpaper;
                themeAccent.patternMotion = (wallPaper == null || (wallPaperSettings = wallPaper.settings) == null || !wallPaperSettings.motion) ? false : true;
                r1 = z;
            }
            if (loadingCurrentTheme != 0) {
                lastLoadingCurrentThemeTime = (int) (System.currentTimeMillis() / 1000);
                saveOtherThemes(r1);
                return;
            }
            return;
        }
        r1 = false;
        if (loadingCurrentTheme != 0) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkCurrentRemoteTheme$7(final ThemeAccent themeAccent, final ThemeInfo themeInfo, final TLRPC.TL_theme tL_theme, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$checkCurrentRemoteTheme$6(TLObject.this, themeAccent, themeInfo, tL_theme);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getThemeFileValuesInBackground$10(Utilities.Callback callback, File file, String str, String[] strArr) {
        callback.run(getThemeFileValues(file, str, strArr));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0216  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$loadRemoteThemes$8(int i, TLObject tLObject) {
        int i2;
        boolean z;
        ArrayList arrayList;
        ArrayList arrayList2;
        String baseThemeKey;
        ThemeInfo themeInfo;
        TLRPC.WallPaperSettings wallPaperSettings;
        loadingRemoteThemes[i] = false;
        if (tLObject instanceof TLRPC.TL_account_themes) {
            TLRPC.TL_account_themes tL_account_themes = (TLRPC.TL_account_themes) tLObject;
            remoteThemesHash[i] = tL_account_themes.hash;
            lastLoadingThemesTime[i] = (int) (System.currentTimeMillis() / 1000);
            ArrayList<TLRPC.TL_theme> arrayList3 = new ArrayList<>();
            ArrayList arrayList4 = new ArrayList();
            int size = themes.size();
            for (int i3 = 0; i3 < size; i3++) {
                ThemeInfo themeInfo2 = (ThemeInfo) themes.get(i3);
                if (themeInfo2.info != null && themeInfo2.account == i) {
                    arrayList4.add(themeInfo2);
                } else if (themeInfo2.themeAccents != null) {
                    for (int i4 = 0; i4 < themeInfo2.themeAccents.size(); i4++) {
                        ThemeAccent themeAccent = (ThemeAccent) themeInfo2.themeAccents.get(i4);
                        if (themeAccent.info != null && themeAccent.account == i) {
                            arrayList4.add(themeAccent);
                        }
                    }
                }
            }
            int size2 = tL_account_themes.themes.size();
            int i5 = 0;
            boolean z2 = false;
            boolean z3 = false;
            while (true) {
                int i6 = -1;
                if (i5 >= size2) {
                    break;
                }
                TLRPC.TL_theme tL_theme = tL_account_themes.themes.get(i5);
                if (tL_theme instanceof TLRPC.TL_theme) {
                    if (tL_theme.isDefault) {
                        arrayList3.add(tL_theme);
                    }
                    ArrayList<TLRPC.ThemeSettings> arrayList5 = tL_theme.settings;
                    if (arrayList5 == null || arrayList5.size() <= 0) {
                        arrayList = arrayList4;
                        String str = "remote" + tL_theme.id;
                        ThemeInfo themeInfo3 = (ThemeInfo) themesDict.get(str);
                        if (themeInfo3 == null) {
                            themeInfo3 = new ThemeInfo();
                            themeInfo3.account = i;
                            themeInfo3.pathToFile = new File(ApplicationLoader.getFilesDirFixed(), str + ".attheme").getAbsolutePath();
                            themes.add(themeInfo3);
                            otherThemes.add(themeInfo3);
                            z3 = true;
                        } else {
                            arrayList.remove(themeInfo3);
                        }
                        themeInfo3.name = tL_theme.title;
                        themeInfo3.info = tL_theme;
                        themesDict.put(themeInfo3.getKey(), themeInfo3);
                        i5++;
                        arrayList4 = arrayList;
                    } else {
                        int i7 = 0;
                        while (i7 < tL_theme.settings.size()) {
                            TLRPC.ThemeSettings themeSettings = tL_theme.settings.get(i7);
                            if (themeSettings == null || (baseThemeKey = getBaseThemeKey(themeSettings)) == null || (themeInfo = (ThemeInfo) themesDict.get(baseThemeKey)) == null || themeInfo.themeAccents == null) {
                                arrayList2 = arrayList4;
                            } else {
                                ArrayList arrayList6 = arrayList4;
                                ThemeAccent themeAccent2 = (ThemeAccent) themeInfo.accentsByThemeId.get(tL_theme.id);
                                if (themeAccent2 != null) {
                                    if (!ThemeInfo.accentEquals(themeAccent2, themeSettings)) {
                                        File pathToWallpaper = themeAccent2.getPathToWallpaper();
                                        if (pathToWallpaper != null) {
                                            pathToWallpaper.delete();
                                        }
                                        ThemeInfo.fillAccentValues(themeAccent2, themeSettings);
                                        ThemeInfo themeInfo4 = currentTheme;
                                        if (themeInfo4 == themeInfo && themeInfo4.currentAccentId == themeAccent2.id) {
                                            refreshThemeColors();
                                            NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
                                            int i8 = NotificationCenter.needSetDayNightTheme;
                                            ThemeInfo themeInfo5 = currentTheme;
                                            globalInstance.lambda$postNotificationNameOnUIThread$1(i8, themeInfo5, Boolean.valueOf(currentNightTheme == themeInfo5), null, Integer.valueOf(i6));
                                        }
                                        z2 = true;
                                        z3 = true;
                                    }
                                    TLRPC.WallPaper wallPaper = themeSettings.wallpaper;
                                    themeAccent2.patternMotion = (wallPaper == null || (wallPaperSettings = wallPaper.settings) == null || !wallPaperSettings.motion) ? false : true;
                                    arrayList2 = arrayList6;
                                    arrayList2.remove(themeAccent2);
                                } else {
                                    arrayList2 = arrayList6;
                                    ThemeAccent createNewAccent = themeInfo.createNewAccent(tL_theme, i, false, i7);
                                    if (TextUtils.isEmpty(createNewAccent.patternSlug)) {
                                        themeAccent2 = createNewAccent;
                                    } else {
                                        themeAccent2 = createNewAccent;
                                        z2 = true;
                                    }
                                }
                                themeAccent2.isDefault = tL_theme.isDefault;
                            }
                            i7++;
                            arrayList4 = arrayList2;
                            i6 = -1;
                        }
                    }
                }
                arrayList = arrayList4;
                i5++;
                arrayList4 = arrayList;
            }
            ArrayList arrayList7 = arrayList4;
            int size3 = arrayList7.size();
            int i9 = 0;
            while (i9 < size3) {
                Object obj = arrayList7.get(i9);
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
                        currentNightTheme = (ThemeInfo) themesDict.get("Dark Blue");
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
                        int i10 = NotificationCenter.needSetDayNightTheme;
                        ThemeInfo themeInfo7 = currentTheme;
                        boolean z4 = currentNightTheme == themeInfo7;
                        i2 = 1;
                        globalInstance2.lambda$postNotificationNameOnUIThread$1(i10, themeInfo7, Boolean.valueOf(z4), null, -1);
                        i9 += i2;
                    }
                }
                i2 = 1;
                i9 += i2;
            }
            saveOtherThemes(true);
            sortThemes();
            if (z3) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.themeListUpdated, new Object[0]);
            }
            if (z2) {
                PatternsLoader.createLoader(true);
            }
            MediaDataController.getInstance(i).generateEmojiPreviewThemes(arrayList3, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadRemoteThemes$9(final int i, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$loadRemoteThemes$8(i, tLObject);
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
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetNewWallpapper, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadWallpaper$12(OverrideWallpaperInfo overrideWallpaperInfo, File file, int i, boolean z, TLRPC.Document document, boolean z2) {
        final Drawable loadWallpaperInternal = loadWallpaperInternal(overrideWallpaperInfo, file, i, z, document, z2);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$loadWallpaper$11(loadWallpaperInternal);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$refreshThemeColors$5(boolean z) {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetNewTheme, Boolean.FALSE, Boolean.valueOf(z));
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
        }
        int i = themeAccent.id;
        int i2 = themeAccent2.id;
        if (r0 != 0) {
            if (i > i2) {
                return 1;
            }
            return i < i2 ? -1 : 0;
        }
        if (i > i2) {
            return -1;
        }
        return i < i2 ? 1 : 0;
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$turnOffAutoNight$14(BaseFragment baseFragment) {
        baseFragment.presentFragment(new ThemeActivity(1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$unrefAudioVisualizeDrawable$13(MessageObject messageObject) {
        AudioVisualizerDrawable audioVisualizerDrawable = (AudioVisualizerDrawable) animatedOutVisualizerDrawables.remove(messageObject);
        if (audioVisualizerDrawable != null) {
            audioVisualizerDrawable.setParentView(null);
        }
    }

    public static void loadRemoteThemes(final int i, boolean z) {
        if (loadingRemoteThemes[i]) {
            return;
        }
        if ((z || Math.abs((System.currentTimeMillis() / 1000) - lastLoadingThemesTime[i]) >= 3600) && UserConfig.getInstance(i).isClientActivated()) {
            loadingRemoteThemes[i] = true;
            TLRPC.TL_account_getThemes tL_account_getThemes = new TLRPC.TL_account_getThemes();
            tL_account_getThemes.format = "android";
            if (!MediaDataController.getInstance(i).defaultEmojiThemes.isEmpty()) {
                tL_account_getThemes.hash = remoteThemesHash[i];
            }
            if (BuildVars.LOGS_ENABLED) {
                Log.i("theme", "loading remote themes, hash " + tL_account_getThemes.hash);
            }
            ConnectionsManager.getInstance(i).sendRequest(tL_account_getThemes, new RequestDelegate() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda14
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    Theme.lambda$loadRemoteThemes$9(i, tLObject, tL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bitmap loadScreenSizedBitmap(FileInputStream fileInputStream, int i) {
        int i2;
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
                int min = Math.min(point.x, point.y);
                Point point2 = AndroidUtilities.displaySize;
                int max = Math.max(point2.x, point2.y);
                float min2 = (min < max || f <= f2) ? Math.min(f / min, f2 / max) : Math.max(f / min, f2 / max);
                if (min2 < 1.2f) {
                    min2 = 1.0f;
                }
                options.inJustDecodeBounds = false;
                if (min2 <= 1.0f || (f <= min && f2 <= max)) {
                    options.inSampleSize = (int) min2;
                } else {
                    int i3 = 1;
                    while (true) {
                        i2 = i3 * 2;
                        if (i3 * 4 >= min2) {
                            break;
                        }
                        i3 = i2;
                    }
                    options.inSampleSize = i2;
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
            } catch (Throwable th) {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception unused3) {
                    }
                }
                throw th;
            }
        } catch (Exception e) {
            FileLog.e(e);
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception unused4) {
                }
            }
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x007a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void loadWallpaper(boolean z) {
        final File file;
        final TLRPC.Document document;
        final boolean z2;
        float f;
        float f2;
        TLRPC.WallPaper wallPaper;
        if (wallpaper != null) {
            return;
        }
        ThemeInfo themeInfo = currentTheme;
        boolean z3 = themeInfo.firstAccentIsDefault && themeInfo.currentAccentId == DEFALT_THEME_ACCENT_ID;
        ThemeAccent accent = themeInfo.getAccent(false);
        if (accent != null) {
            File pathToWallpaper = accent.getPathToWallpaper();
            boolean z4 = accent.patternMotion;
            TLRPC.TL_theme tL_theme = accent.info;
            TLRPC.ThemeSettings themeSettings = (tL_theme == null || tL_theme.settings.size() <= 0) ? null : accent.info.settings.get(0);
            if (accent.info == null || themeSettings == null || (wallPaper = themeSettings.wallpaper) == null) {
                file = pathToWallpaper;
                z2 = z4;
                document = null;
            } else {
                z2 = z4;
                document = wallPaper.document;
                file = pathToWallpaper;
            }
        } else {
            file = null;
            document = null;
            z2 = false;
        }
        ThemeInfo themeInfo2 = currentTheme;
        final OverrideWallpaperInfo overrideWallpaperInfo = themeInfo2.overrideWallpaper;
        if (overrideWallpaperInfo != null) {
            f2 = overrideWallpaperInfo.intensity;
        } else {
            if (accent == null) {
                f = themeInfo2.patternIntensity;
                final int i = (int) f;
                if (!z) {
                    DispatchQueue dispatchQueue = Utilities.themeQueue;
                    final boolean z5 = z3;
                    Runnable runnable = new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda9
                        @Override // java.lang.Runnable
                        public final void run() {
                            Theme.lambda$loadWallpaper$12(Theme.OverrideWallpaperInfo.this, file, i, z2, document, z5);
                        }
                    };
                    wallpaperLoadTask = runnable;
                    dispatchQueue.postRunnable(runnable);
                    return;
                }
                Drawable loadWallpaperInternal = loadWallpaperInternal(overrideWallpaperInfo, file, i, z2, document, z3);
                createCommonChatResources();
                if (!disallowChangeServiceMessageColor) {
                    applyChatServiceMessageColor(null, null, loadWallpaperInternal);
                    applyChatMessageSelectedBackgroundColor(null, loadWallpaperInternal);
                }
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetNewWallpapper, new Object[0]);
                return;
            }
            f2 = accent.patternIntensity;
        }
        f = f2 * 100.0f;
        final int i2 = (int) f;
        if (!z) {
        }
    }

    private static Drawable loadWallpaperInternal(OverrideWallpaperInfo overrideWallpaperInfo, File file, int i, boolean z, TLRPC.Document document, boolean z2) {
        BackgroundDrawableSettings createBackgroundDrawable = createBackgroundDrawable(currentTheme, overrideWallpaperInfo, currentColors, file, themedWallpaperLink, themedWallpaperFileOffset, i, previousPhase, z2, hasPreviousTheme, isApplyingAccent, z, document, false);
        Boolean bool = createBackgroundDrawable.isWallpaperMotion;
        isWallpaperMotion = bool != null ? bool.booleanValue() : isWallpaperMotion;
        Boolean bool2 = createBackgroundDrawable.isPatternWallpaper;
        isPatternWallpaper = bool2 != null ? bool2.booleanValue() : isPatternWallpaper;
        Boolean bool3 = createBackgroundDrawable.isCustomTheme;
        isCustomTheme = bool3 != null ? bool3.booleanValue() : isCustomTheme;
        patternIntensity = i;
        Drawable drawable = createBackgroundDrawable.wallpaper;
        wallpaper = drawable != null ? drawable : wallpaper;
        calcBackgroundColor(drawable, 1);
        applyChatServiceMessageColor();
        return drawable;
    }

    public static int multAlpha(int i, float f) {
        return f == 1.0f ? i : ColorUtils.setAlphaComponent(i, MathUtils.clamp((int) (Color.alpha(i) * f), 0, NotificationCenter.closeSearchByActiveAction));
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

    public static void refreshAttachButtonsColors() {
        RLottieDrawable rLottieDrawable;
        int nonAnimatedColor;
        String str;
        int i = 0;
        while (true) {
            RLottieDrawable[] rLottieDrawableArr = chat_attachButtonDrawables;
            if (i >= rLottieDrawableArr.length) {
                return;
            }
            RLottieDrawable rLottieDrawable2 = rLottieDrawableArr[i];
            if (rLottieDrawable2 != null) {
                rLottieDrawable2.beginApplyLayerColors();
                if (i == 0) {
                    RLottieDrawable rLottieDrawable3 = chat_attachButtonDrawables[i];
                    int i2 = key_chat_attachGalleryBackground;
                    rLottieDrawable3.setLayerColor("Color_Mount.**", getNonAnimatedColor(i2));
                    chat_attachButtonDrawables[i].setLayerColor("Color_PhotoShadow.**", getNonAnimatedColor(i2));
                    RLottieDrawable rLottieDrawable4 = chat_attachButtonDrawables[i];
                    int i3 = key_chat_attachIcon;
                    rLottieDrawable4.setLayerColor("White_Photo.**", getNonAnimatedColor(i3));
                    rLottieDrawable = chat_attachButtonDrawables[i];
                    nonAnimatedColor = getNonAnimatedColor(i3);
                    str = "White_BackPhoto.**";
                } else if (i == 1) {
                    RLottieDrawable rLottieDrawable5 = chat_attachButtonDrawables[i];
                    int i4 = key_chat_attachIcon;
                    rLottieDrawable5.setLayerColor("White_Play1.**", getNonAnimatedColor(i4));
                    rLottieDrawable = chat_attachButtonDrawables[i];
                    nonAnimatedColor = getNonAnimatedColor(i4);
                    str = "White_Play2.**";
                } else if (i == 2) {
                    chat_attachButtonDrawables[i].setLayerColor("Color_Corner.**", getNonAnimatedColor(key_chat_attachFileBackground));
                    rLottieDrawable = chat_attachButtonDrawables[i];
                    nonAnimatedColor = getNonAnimatedColor(key_chat_attachIcon);
                    str = "White_List.**";
                } else if (i == 3) {
                    RLottieDrawable rLottieDrawable6 = chat_attachButtonDrawables[i];
                    int i5 = key_chat_attachIcon;
                    rLottieDrawable6.setLayerColor("White_User1.**", getNonAnimatedColor(i5));
                    rLottieDrawable = chat_attachButtonDrawables[i];
                    nonAnimatedColor = getNonAnimatedColor(i5);
                    str = "White_User2.**";
                } else if (i == 4) {
                    chat_attachButtonDrawables[i].setLayerColor("Color_Oval.**", getNonAnimatedColor(key_chat_attachLocationBackground));
                    rLottieDrawable = chat_attachButtonDrawables[i];
                    nonAnimatedColor = getNonAnimatedColor(key_chat_attachIcon);
                    str = "White_Pin.**";
                } else {
                    if (i == 5) {
                        RLottieDrawable rLottieDrawable7 = chat_attachButtonDrawables[i];
                        int i6 = key_chat_attachIcon;
                        rLottieDrawable7.setLayerColor("White_Column 1.**", getNonAnimatedColor(i6));
                        chat_attachButtonDrawables[i].setLayerColor("White_Column 2.**", getNonAnimatedColor(i6));
                        rLottieDrawable = chat_attachButtonDrawables[i];
                        nonAnimatedColor = getNonAnimatedColor(i6);
                        str = "White_Column 3.**";
                    }
                    chat_attachButtonDrawables[i].commitApplyLayerColors();
                }
                rLottieDrawable.setLayerColor(str, nonAnimatedColor);
                chat_attachButtonDrawables[i].commitApplyLayerColors();
            }
            i++;
        }
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$refreshThemeColors$5(z3);
            }
        });
    }

    public static void reloadAllResources(Context context) {
        destroyResources();
        if (chat_msgInDrawable != null) {
            chat_msgInDrawable = null;
            currentColor = 0;
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

    public static void reloadWallpaper(boolean z) {
        BackgroundGradientDrawable.Disposable disposable = backgroundGradientDisposable;
        if (disposable != null) {
            disposable.dispose();
            backgroundGradientDisposable = null;
        }
        Drawable drawable = wallpaper;
        previousPhase = drawable instanceof MotionBackgroundDrawable ? ((MotionBackgroundDrawable) drawable).getPhase() : 0;
        wallpaper = null;
        themedWallpaper = null;
        loadWallpaper(z);
    }

    public static void resetCustomWallpaper(boolean z) {
        if (!z) {
            currentTheme.setOverrideWallpaper(null);
        } else {
            isApplyingAccent = false;
            reloadWallpaper(true);
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

    /* JADX WARN: Can't wrap try/catch for region: R(12:(12:47|(2:48|(3:50|(2:53|54)|55)(0))|69|71|72|(1:78)|79|(2:81|(2:102|103))(2:107|(3:109|(1:111)|(1:114)))|(6:84|(1:86)|87|(1:89)|(1:91)|92)|94|95|(2:97|98)(1:100))(2:139|(4:142|(2:145|146)|147|140))|68|69|71|72|(3:74|76|78)|79|(0)(0)|(0)|94|95|(0)(0)) */
    /* JADX WARN: Can't wrap try/catch for region: R(24:1|(1:3)(1:163)|(1:5)(1:162)|(1:8)|9|(1:161)(1:13)|14|(1:16)(1:160)|17|(5:(1:20)(1:44)|(1:22)(1:43)|(1:24)(1:42)|(1:26)(1:41)|(5:29|(2:31|(1:33))|34|(1:40)(1:38)|39))|45|(12:47|(2:48|(3:50|(2:53|54)|55)(0))|69|71|72|(1:78)|79|(2:81|(2:102|103))(2:107|(3:109|(1:111)|(1:114)))|(6:84|(1:86)|87|(1:89)|(1:91)|92)|94|95|(2:97|98)(1:100))(2:139|(4:142|(2:145|146)|147|140))|68|69|71|72|(3:74|76|78)|79|(0)(0)|(0)|94|95|(0)(0)|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x019b, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x01a0, code lost:
    
        r7 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x0204, code lost:
    
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x0207, code lost:
    
        if (r7 != null) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x0209, code lost:
    
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x0199, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x019d, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x0222, code lost:
    
        if (r4 != null) goto L154;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x0224, code lost:
    
        r4.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:?, code lost:
    
        throw r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x0228, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x0229, code lost:
    
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x022e, code lost:
    
        throw r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:?, code lost:
    
        throw r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x01ff, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x020d, code lost:
    
        org.telegram.messenger.FileLog.e(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0203, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Removed duplicated region for block: B:100:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0174 A[Catch: all -> 0x0199, Exception -> 0x019b, TryCatch #7 {Exception -> 0x019b, all -> 0x0199, blocks: (B:72:0x00f8, B:74:0x00fe, B:76:0x0102, B:78:0x0108, B:79:0x010d, B:81:0x0120, B:84:0x01ae, B:86:0x01ba, B:87:0x01d3, B:89:0x01d9, B:91:0x01dd, B:92:0x01e5, B:106:0x0170, B:107:0x0174, B:109:0x0178, B:111:0x0181, B:114:0x01a6), top: B:71:0x00f8 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0120 A[Catch: all -> 0x0199, Exception -> 0x019b, TRY_LEAVE, TryCatch #7 {Exception -> 0x019b, all -> 0x0199, blocks: (B:72:0x00f8, B:74:0x00fe, B:76:0x0102, B:78:0x0108, B:79:0x010d, B:81:0x0120, B:84:0x01ae, B:86:0x01ba, B:87:0x01d3, B:89:0x01d9, B:91:0x01dd, B:92:0x01e5, B:106:0x0170, B:107:0x0174, B:109:0x0178, B:111:0x0181, B:114:0x01a6), top: B:71:0x00f8 }] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01ae A[Catch: all -> 0x0199, Exception -> 0x019b, TryCatch #7 {Exception -> 0x019b, all -> 0x0199, blocks: (B:72:0x00f8, B:74:0x00fe, B:76:0x0102, B:78:0x0108, B:79:0x010d, B:81:0x0120, B:84:0x01ae, B:86:0x01ba, B:87:0x01d3, B:89:0x01d9, B:91:0x01dd, B:92:0x01e5, B:106:0x0170, B:107:0x0174, B:109:0x0178, B:111:0x0181, B:114:0x01a6), top: B:71:0x00f8 }] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0212  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:127:0x020d -> B:90:0x0210). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void saveCurrentTheme(ThemeInfo themeInfo, boolean z, boolean z2, boolean z3) {
        FileOutputStream fileOutputStream;
        OverrideWallpaperInfo overrideWallpaperInfo = themeInfo.overrideWallpaper;
        String wallpaperUrl = overrideWallpaperInfo != null ? getWallpaperUrl(overrideWallpaperInfo) : themedWallpaperLink;
        Drawable drawable = z2 ? wallpaper : themedWallpaper;
        if (z2 && drawable != null) {
            themedWallpaper = wallpaper;
        }
        ThemeAccent accent = currentTheme.getAccent(false);
        boolean z4 = currentTheme.firstAccentIsDefault && accent.id == DEFALT_THEME_ACCENT_ID;
        FileOutputStream fileOutputStream2 = null;
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
            if (z4) {
                int i5 = 0;
                while (true) {
                    int[] iArr = defaultColors;
                    if (i5 < iArr.length) {
                        int i6 = iArr[i5];
                        if ((!(drawable instanceof BitmapDrawable) && wallpaperUrl == null) || (key_chat_wallpaper != i5 && key_chat_wallpaper_gradient_to1 != i5 && key_chat_wallpaper_gradient_to2 != i5 && key_chat_wallpaper_gradient_to3 != i5)) {
                            sb.append(ThemeColors.getStringName(i5));
                            sb.append("=");
                            sb.append(i6);
                            sb.append("\n");
                        }
                        i5++;
                    }
                }
                fileOutputStream = new FileOutputStream(themeInfo.pathToFile);
                if (sb.length() == 0 && !(drawable instanceof BitmapDrawable) && TextUtils.isEmpty(wallpaperUrl)) {
                    sb.append(' ');
                }
                fileOutputStream.write(AndroidUtilities.getStringBytes(sb.toString()));
                if (TextUtils.isEmpty(wallpaperUrl)) {
                    fileOutputStream.write(AndroidUtilities.getStringBytes("WLS=" + wallpaperUrl + "\n"));
                    if (z2) {
                        try {
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            FileOutputStream fileOutputStream3 = new FileOutputStream(new File(ApplicationLoader.getFilesDirFixed(), Utilities.MD5(wallpaperUrl) + ".wp"));
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 87, fileOutputStream3);
                            fileOutputStream3.close();
                        } catch (Throwable th) {
                            FileLog.e(th);
                        }
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
                    if (z4) {
                        currentColorsNoAccent.clear();
                        refreshThemeColors();
                    }
                    SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
                    edit.putString("theme", currentDayTheme.getKey());
                    edit.apply();
                }
                fileOutputStream.close();
                if (z) {
                    MessagesController.getInstance(themeInfo.account).saveThemeToServer(themeInfo, themeInfo.getAccent(false));
                    return;
                }
                return;
            }
            for (int i7 = 0; i7 < sparseIntArray.size(); i7++) {
                int keyAt = sparseIntArray.keyAt(i7);
                int valueAt = sparseIntArray.valueAt(i7);
                if ((!(drawable instanceof BitmapDrawable) && wallpaperUrl == null) || (key_chat_wallpaper != keyAt && key_chat_wallpaper_gradient_to1 != keyAt && key_chat_wallpaper_gradient_to2 != keyAt && key_chat_wallpaper_gradient_to3 != keyAt)) {
                    sb.append(ThemeColors.getStringName(keyAt));
                    sb.append("=");
                    sb.append(valueAt);
                    sb.append("\n");
                }
            }
            fileOutputStream = new FileOutputStream(themeInfo.pathToFile);
            if (sb.length() == 0) {
                sb.append(' ');
            }
            fileOutputStream.write(AndroidUtilities.getStringBytes(sb.toString()));
            if (TextUtils.isEmpty(wallpaperUrl)) {
            }
            if (!z3) {
            }
            fileOutputStream.close();
            if (z) {
            }
        } catch (Throwable th2) {
            Throwable th3 = th2;
            fileOutputStream = fileOutputStream2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void saveOtherThemes(boolean z) {
        saveOtherThemes(z, false);
    }

    private static void saveOtherThemes(boolean z, boolean z2) {
        ArrayList arrayList;
        int i = 0;
        SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0).edit();
        if (z) {
            JSONArray jSONArray = new JSONArray();
            for (int i2 = 0; i2 < otherThemes.size(); i2++) {
                JSONObject saveJson = ((ThemeInfo) otherThemes.get(i2)).getSaveJson();
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
                ThemeInfo themeInfo = (ThemeInfo) themesDict.get(i != 0 ? i != 1 ? i != 2 ? i != 3 ? "Night" : "Day" : "Arctic Blue" : "Dark Blue" : "Blue");
                if (themeInfo != null && (arrayList = themeInfo.themeAccents) != null && !arrayList.isEmpty()) {
                    saveThemeAccents(themeInfo, true, false, false, false, z2);
                }
                i++;
            }
        }
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
                    ThemeAccent themeAccent = (ThemeAccent) themeInfo.themeAccents.get(i);
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
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.themeAccentListUpdated, new Object[0]);
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
                    ThemeAccent themeAccent2 = (ThemeAccent) themeInfo.themeAccentsMap.get(themeInfo.currentAccentId);
                    themeInfo.themeAccentsMap.remove(themeAccent2.id);
                    themeInfo.themeAccents.remove(themeAccent2);
                    TLRPC.TL_theme tL_theme = themeAccent2.info;
                    if (tL_theme != null) {
                        themeInfo.accentsByThemeId.remove(tL_theme.id);
                    }
                }
                themeInfo.currentAccentId = themeInfo.prevAccentId;
                ThemeAccent accent = themeInfo.getAccent(false);
                themeInfo.overrideWallpaper = accent != null ? accent.overrideWallpaper : null;
            }
            if (currentTheme == themeInfo) {
                refreshThemeColors();
            }
        }
        themeInfo.prevAccentId = -1;
    }

    public static void setAnimatedColor(int i, int i2) {
        SparseIntArray sparseIntArray = animatingColors;
        if (sparseIntArray == null) {
            return;
        }
        sparseIntArray.put(i, i2);
    }

    public static void setAnimatingColor(boolean z) {
        animatingColors = z ? new SparseIntArray() : null;
    }

    public static void setChangingWallpaper(boolean z) {
        changingWallpaper = z;
        if (z) {
            return;
        }
        checkAutoNightThemeConditions(false);
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
            return;
        }
        if (i == key_chat_serviceBackground || i == key_chat_serviceBackgroundSelected) {
            applyChatServiceMessageColor();
            return;
        }
        if (i == i3 || i == key_chat_wallpaper_gradient_to1 || i == key_chat_wallpaper_gradient_to2 || i == key_chat_wallpaper_gradient_to3 || i == key_chat_wallpaper_gradient_rotation) {
            reloadWallpaper(true);
            return;
        }
        if (i == key_actionBarDefault) {
            if (Build.VERSION.SDK_INT >= 23) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needCheckSystemBarColors, new Object[0]);
            }
        } else {
            if (i != key_windowBackgroundGray || Build.VERSION.SDK_INT < 26) {
                return;
            }
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needCheckSystemBarColors, new Object[0]);
        }
    }

    public static void setCombinedDrawableColor(Drawable drawable, int i, boolean z) {
        if (drawable instanceof CombinedDrawable) {
            CombinedDrawable combinedDrawable = (CombinedDrawable) drawable;
            Drawable icon = z ? combinedDrawable.getIcon() : combinedDrawable.getBackground();
            if (icon instanceof ColorDrawable) {
                ((ColorDrawable) icon).setColor(i);
            } else {
                icon.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
            }
        }
    }

    public static void setCurrentNightTheme(ThemeInfo themeInfo) {
        boolean z = currentTheme == currentNightTheme;
        currentNightTheme = themeInfo;
        if (z) {
            applyDayNightThemeMaybe(true);
        }
    }

    public static void setDefaultColor(int i, int i2) {
        defaultColors[i] = i2;
    }

    public static void setDrawableColor(Drawable drawable, int i) {
        if (drawable == null) {
            return;
        }
        if (drawable instanceof StatusDrawable) {
            ((StatusDrawable) drawable).setColor(i);
            return;
        }
        if (drawable instanceof MsgClockDrawable) {
            ((MsgClockDrawable) drawable).setColor(i);
            return;
        }
        if (drawable instanceof ShapeDrawable) {
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
        if (drawable instanceof StateListDrawable) {
            try {
                Drawable stateDrawable = z ? getStateDrawable(drawable, 0) : getStateDrawable(drawable, 1);
                if (stateDrawable instanceof ShapeDrawable) {
                    ((ShapeDrawable) stateDrawable).getPaint().setColor(i);
                } else {
                    stateDrawable.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
                }
            } catch (Throwable unused) {
            }
        }
    }

    public static void setMaskDrawableRad(Drawable drawable, float f, float f2, float f3, float f4) {
        int numberOfLayers;
        Drawable drawable2;
        if (Build.VERSION.SDK_INT >= 21 && AppCompatImageHelper$$ExternalSyntheticApiModelOutline0.m(drawable)) {
            RippleDrawable m = Theme$$ExternalSyntheticApiModelOutline2.m(drawable);
            numberOfLayers = m.getNumberOfLayers();
            for (int i = 0; i < numberOfLayers; i++) {
                drawable2 = m.getDrawable(i);
                if (drawable2 instanceof RippleRadMaskDrawable) {
                    ((RippleRadMaskDrawable) drawable2).setRadius(f, f2, f3, f4);
                    return;
                }
            }
        }
    }

    public static void setMaskDrawableRad(Drawable drawable, int i, int i2) {
        int numberOfLayers;
        Drawable drawable2;
        if (Build.VERSION.SDK_INT >= 21 && AppCompatImageHelper$$ExternalSyntheticApiModelOutline0.m(drawable)) {
            RippleDrawable m = Theme$$ExternalSyntheticApiModelOutline2.m(drawable);
            numberOfLayers = m.getNumberOfLayers();
            for (int i3 = 0; i3 < numberOfLayers; i3++) {
                drawable2 = m.getDrawable(i3);
                if (drawable2 instanceof RippleRadMaskDrawable) {
                    ((RippleRadMaskDrawable) drawable2).setRadius(i, i2);
                    return;
                }
            }
        }
    }

    public static void setRippleDrawableForceSoftware(RippleDrawable rippleDrawable) {
        if (rippleDrawable == null) {
            return;
        }
        try {
            Theme$$ExternalSyntheticApiModelOutline8.m().getDeclaredMethod("setForceSoftware", Boolean.TYPE).invoke(rippleDrawable, Boolean.TRUE);
        } catch (Throwable unused) {
        }
    }

    public static void setSelectorDrawableColor(Drawable drawable, int i, boolean z) {
        Drawable stateDrawable;
        int numberOfLayers;
        Drawable drawable2;
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
                    return;
                } else {
                    stateDrawable.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
                    return;
                }
            } catch (Throwable unused) {
                return;
            }
        }
        if (Build.VERSION.SDK_INT < 21 || !AppCompatImageHelper$$ExternalSyntheticApiModelOutline0.m(drawable)) {
            return;
        }
        RippleDrawable m = Theme$$ExternalSyntheticApiModelOutline2.m(drawable);
        if (z) {
            m.setColor(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{i}));
            return;
        }
        numberOfLayers = m.getNumberOfLayers();
        if (numberOfLayers > 0) {
            drawable2 = m.getDrawable(0);
            if (drawable2 instanceof ShapeDrawable) {
                ((ShapeDrawable) drawable2).getPaint().setColor(i);
            } else {
                drawable2.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
            }
        }
    }

    public static void setThemeFileReference(TLRPC.TL_theme tL_theme) {
        TLRPC.Document document;
        int size = themes.size();
        for (int i = 0; i < size; i++) {
            TLRPC.TL_theme tL_theme2 = ((ThemeInfo) themes.get(i)).info;
            if (tL_theme2 != null && tL_theme2.id == tL_theme.id) {
                TLRPC.Document document2 = tL_theme2.document;
                if (document2 == null || (document = tL_theme.document) == null) {
                    return;
                }
                document2.file_reference = document.file_reference;
                saveOtherThemes(true);
                return;
            }
        }
    }

    public static void setThemeUploadInfo(ThemeInfo themeInfo, ThemeAccent themeAccent, TLRPC.TL_theme tL_theme, int i, boolean z) {
        String str;
        TLRPC.WallPaperSettings wallPaperSettings;
        if (tL_theme == null) {
            return;
        }
        TLRPC.ThemeSettings themeSettings = tL_theme.settings.size() > 0 ? tL_theme.settings.get(0) : null;
        if (themeSettings != null) {
            if (themeInfo == null) {
                String baseThemeKey = getBaseThemeKey(themeSettings);
                if (baseThemeKey == null || (themeInfo = (ThemeInfo) themesDict.get(baseThemeKey)) == null) {
                    return;
                } else {
                    themeAccent = (ThemeAccent) themeInfo.accentsByThemeId.get(tL_theme.id);
                }
            }
            if (themeAccent == null) {
                return;
            }
            TLRPC.TL_theme tL_theme2 = themeAccent.info;
            if (tL_theme2 != null) {
                themeInfo.accentsByThemeId.remove(tL_theme2.id);
            }
            themeAccent.info = tL_theme;
            themeAccent.account = i;
            themeInfo.accentsByThemeId.put(tL_theme.id, themeAccent);
            if (!ThemeInfo.accentEquals(themeAccent, themeSettings)) {
                File pathToWallpaper = themeAccent.getPathToWallpaper();
                if (pathToWallpaper != null) {
                    pathToWallpaper.delete();
                }
                ThemeInfo.fillAccentValues(themeAccent, themeSettings);
                ThemeInfo themeInfo2 = currentTheme;
                if (themeInfo2 == themeInfo && themeInfo2.currentAccentId == themeAccent.id) {
                    refreshThemeColors();
                    NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
                    int i2 = NotificationCenter.needSetDayNightTheme;
                    ThemeInfo themeInfo3 = currentTheme;
                    globalInstance.lambda$postNotificationNameOnUIThread$1(i2, themeInfo3, Boolean.valueOf(currentNightTheme == themeInfo3), null, -1);
                }
                PatternsLoader.createLoader(true);
            }
            TLRPC.WallPaper wallPaper = themeSettings.wallpaper;
            themeAccent.patternMotion = (wallPaper == null || (wallPaperSettings = wallPaper.settings) == null || !wallPaperSettings.motion) ? false : true;
            themeInfo.previewParsed = false;
        } else {
            if (themeInfo != null) {
                HashMap hashMap = themesDict;
                str = themeInfo.getKey();
                hashMap.remove(str);
            } else {
                HashMap hashMap2 = themesDict;
                str = "remote" + tL_theme.id;
                themeInfo = (ThemeInfo) hashMap2.get(str);
            }
            if (themeInfo == null) {
                return;
            }
            themeInfo.info = tL_theme;
            themeInfo.name = tL_theme.title;
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

    public static void setThemeWallpaper(ThemeInfo themeInfo, Bitmap bitmap, File file) {
        currentColors.delete(key_chat_wallpaper);
        currentColors.delete(key_chat_wallpaper_gradient_to1);
        currentColors.delete(key_chat_wallpaper_gradient_to2);
        currentColors.delete(key_chat_wallpaper_gradient_to3);
        currentColors.delete(key_chat_wallpaper_gradient_rotation);
        themedWallpaperLink = null;
        themeInfo.setOverrideWallpaper(null);
        if (bitmap == null) {
            themedWallpaper = null;
            wallpaper = null;
            saveCurrentTheme(themeInfo, false, false, false);
            reloadWallpaper(true);
            return;
        }
        themedWallpaper = new BitmapDrawable(bitmap);
        saveCurrentTheme(themeInfo, false, false, false);
        calcBackgroundColor(themedWallpaper, 0);
        applyChatServiceMessageColor();
        applyChatMessageSelectedBackgroundColor();
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetNewWallpapper, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sortAccents(ThemeInfo themeInfo) {
        Collections.sort(themeInfo.themeAccents, new Comparator() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda12
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$sortAccents$0;
                lambda$sortAccents$0 = Theme.lambda$sortAccents$0((Theme.ThemeAccent) obj, (Theme.ThemeAccent) obj2);
                return lambda$sortAccents$0;
            }
        });
    }

    private static void sortThemes() {
        Collections.sort(themes, new Comparator() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda10
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$sortThemes$1;
                lambda$sortThemes$1 = Theme.lambda$sortThemes$1((Theme.ThemeInfo) obj, (Theme.ThemeInfo) obj2);
                return lambda$sortThemes$1;
            }
        });
    }

    public static void turnOffAutoNight(FrameLayout frameLayout, Runnable runnable) {
        String str;
        int i;
        if (selectedAutoNightType != 0) {
            if (frameLayout != null && runnable != null) {
                try {
                    BulletinFactory of = BulletinFactory.of(frameLayout, null);
                    int i2 = org.telegram.messenger.R.raw.auto_night_off;
                    if (selectedAutoNightType == 3) {
                        str = "AutoNightSystemModeOff";
                        i = org.telegram.messenger.R.string.AutoNightSystemModeOff;
                    } else {
                        str = "AutoNightModeOff";
                        i = org.telegram.messenger.R.string.AutoNightModeOff;
                    }
                    of.createSimpleBulletin(i2, LocaleController.getString(str, i), LocaleController.getString("Settings", org.telegram.messenger.R.string.Settings), 5000, runnable).show();
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            selectedAutoNightType = 0;
            saveAutoNightThemeConfig();
            cancelAutoNightThemeCallbacks();
        }
    }

    public static void turnOffAutoNight(final BaseFragment baseFragment) {
        String str;
        int i;
        if (selectedAutoNightType != 0) {
            if (baseFragment != null) {
                try {
                    BulletinFactory of = BulletinFactory.of(baseFragment);
                    int i2 = org.telegram.messenger.R.raw.auto_night_off;
                    if (selectedAutoNightType == 3) {
                        str = "AutoNightSystemModeOff";
                        i = org.telegram.messenger.R.string.AutoNightSystemModeOff;
                    } else {
                        str = "AutoNightModeOff";
                        i = org.telegram.messenger.R.string.AutoNightModeOff;
                    }
                    of.createSimpleBulletin(i2, LocaleController.getString(str, i), LocaleController.getString("Settings", org.telegram.messenger.R.string.Settings), 5000, new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda23
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
            animatedOutVisualizerDrawables = new HashMap();
        }
        animatedOutVisualizerDrawables.put(messageObject, chat_msgAudioVisualizeDrawable);
        chat_msgAudioVisualizeDrawable.setWaveform(false, true, null);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ActionBar.Theme$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                Theme.lambda$unrefAudioVisualizeDrawable$13(MessageObject.this);
            }
        }, 200L);
        chat_msgAudioVisualizeDrawable = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean useBlackText(int i, int i2) {
        float red = Color.red(i) / 255.0f;
        float green = Color.green(i) / 255.0f;
        float blue = Color.blue(i) / 255.0f;
        return ((((red * 0.5f) + ((Color.red(i2) / 255.0f) * 0.5f)) * 0.2126f) + (((green * 0.5f) + ((Color.green(i2) / 255.0f) * 0.5f)) * 0.7152f)) + (((blue * 0.5f) + ((((float) Color.blue(i2)) / 255.0f) * 0.5f)) * 0.0722f) > 0.705f || ((red * 0.2126f) + (green * 0.7152f)) + (blue * 0.0722f) > 0.705f;
    }
}
