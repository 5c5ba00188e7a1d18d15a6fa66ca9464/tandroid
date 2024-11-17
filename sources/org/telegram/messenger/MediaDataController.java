package org.telegram.messenger;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.URLSpan;
import android.util.Pair;
import android.util.SparseArray;
import androidx.collection.LongSparseArray;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.content.pm.ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0;
import androidx.core.content.pm.ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2;
import androidx.core.graphics.drawable.IconCompat;
import com.android.billingclient.api.ProductDetails;
import j$.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.CodeHighlighting;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationBadge;
import org.telegram.messenger.Timer;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.ringtone.RingtoneDataStore;
import org.telegram.messenger.ringtone.RingtoneUploader;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_bots;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.ChatThemeBottomSheet;
import org.telegram.ui.Components.QuoteSpan;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.StickerSetBulletinLayout;
import org.telegram.ui.Components.StickersArchiveAlert;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;
import org.telegram.ui.LaunchActivity;

/* loaded from: classes3.dex */
public class MediaDataController extends BaseController {
    public static final String ATTACH_MENU_BOT_ANIMATED_ICON_KEY = "android_animated";
    public static final String ATTACH_MENU_BOT_COLOR_DARK_ICON = "dark_icon";
    public static final String ATTACH_MENU_BOT_COLOR_DARK_TEXT = "dark_text";
    public static final String ATTACH_MENU_BOT_COLOR_LIGHT_ICON = "light_icon";
    public static final String ATTACH_MENU_BOT_COLOR_LIGHT_TEXT = "light_text";
    public static final String ATTACH_MENU_BOT_PLACEHOLDER_STATIC_KEY = "placeholder_static";
    public static final String ATTACH_MENU_BOT_SIDE_MENU = "android_side_menu_static";
    public static final String ATTACH_MENU_BOT_SIDE_MENU_ICON_KEY = "android_side_menu_static";
    public static final String ATTACH_MENU_BOT_STATIC_ICON_KEY = "default_static";
    public static final int MEDIA_AUDIO = 2;
    public static final int MEDIA_FILE = 1;
    public static final int MEDIA_GIF = 5;
    public static final int MEDIA_MUSIC = 4;
    public static final int MEDIA_PHOTOS_ONLY = 6;
    public static final int MEDIA_PHOTOVIDEO = 0;
    public static final int MEDIA_STORIES = 8;
    public static final int MEDIA_TYPES_COUNT = 8;
    public static final int MEDIA_URL = 3;
    public static final int MEDIA_VIDEOS_ONLY = 7;
    public static int SHORTCUT_TYPE_ATTACHED_BOT = 0;
    public static int SHORTCUT_TYPE_USER_OR_CHAT = 0;
    public static final int TYPE_EMOJI = 4;
    public static final int TYPE_EMOJIPACKS = 5;
    public static final int TYPE_FAVE = 2;
    public static final int TYPE_FEATURED = 3;
    public static final int TYPE_FEATURED_EMOJIPACKS = 6;
    public static final int TYPE_GREETINGS = 3;
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_MASK = 1;
    public static final int TYPE_PREMIUM_STICKERS = 7;
    private static RectF bitmapRect;
    private static Comparator<TLRPC.MessageEntity> entityComparator;
    private static Paint erasePaint;
    private static Paint roundPaint;
    private static Path roundPath;
    private HashMap<String, ArrayList<TLRPC.Document>> allStickers;
    private HashMap<String, ArrayList<TLRPC.Document>> allStickersFeatured;
    private int[] archivedStickersCount;
    private TLRPC.TL_attachMenuBots attachMenuBots;
    private LongSparseArray botDialogKeyboards;
    private HashMap<String, TL_bots.BotInfo> botInfos;
    private HashMap<MessagesStorage.TopicKey, TLRPC.Message> botKeyboards;
    private LongSparseArray botKeyboardsByMids;
    private boolean cleanedupStickerSetCache;
    private HashMap<String, Boolean> currentFetchingEmoji;
    public final ArrayList<ChatThemeBottomSheet.ChatThemeItem> defaultEmojiThemes;
    private ArrayList<MessageObject> deletedFromResultMessages;
    private LongSparseArray diceEmojiStickerSetsById;
    private HashMap<String, TLRPC.TL_messages_stickerSet> diceStickerSetsByEmoji;
    private String doubleTapReaction;
    private LongSparseArray draftMessages;
    private SharedPreferences draftPreferences;
    public LongSparseArray draftVoices;
    private boolean draftVoicesLoaded;
    private LongSparseArray drafts;
    private LongSparseArray draftsFolderIds;
    private ArrayList<TLRPC.EmojiStatus>[] emojiStatuses;
    private Long[] emojiStatusesFetchDate;
    private boolean[] emojiStatusesFetching;
    private boolean[] emojiStatusesFromCacheFetched;
    private long[] emojiStatusesHash;
    private List<TLRPC.TL_availableReaction> enabledReactionsList;
    private ArrayList<TLRPC.StickerSetCovered>[] featuredStickerSets;
    private LongSparseArray[] featuredStickerSetsById;
    private boolean[] featuredStickersLoaded;
    private HashSet<String> fetchedEmoji;
    private TLRPC.Document greetingsSticker;
    public TLRPC.TL_emojiList groupAvatarConstructorDefault;
    private LongSparseArray groupStickerSets;
    public ArrayList<TLRPC.TL_topPeer> hints;
    private boolean inTransaction;
    public ArrayList<TLRPC.TL_topPeer> inlineBots;
    private ArrayList<Long> installedForceStickerSetsById;
    private LongSparseArray installedStickerSetsById;
    private boolean isLoadingMenuBots;
    private boolean isLoadingPremiumPromo;
    private boolean isLoadingReactions;
    private long lastDialogId;
    private int lastGuid;
    private long lastMergeDialogId;
    private ReactionsLayoutInBubble.VisibleReaction lastReaction;
    private long lastReplyMessageId;
    private int lastReqId;
    private int lastReturnedNum;
    private TLRPC.Chat lastSearchChat;
    private String lastSearchQuery;
    private TLRPC.User lastSearchUser;
    private int[] loadDate;
    private int[] loadFeaturedDate;
    private long[] loadFeaturedHash;
    public boolean loadFeaturedPremium;
    private long[] loadHash;
    boolean loaded;
    private boolean loadedPredirectedSearchLocal;
    boolean loadedRecentReactions;
    boolean loadedSavedReactions;
    boolean loading;
    private boolean loadingDefaultTopicIcons;
    private HashSet<String> loadingDiceStickerSets;
    private boolean loadingDrafts;
    private boolean[] loadingFeaturedStickers;
    private boolean loadingGenericAnimations;
    private boolean loadingMoreSearchMessages;
    private LongSparseArray loadingPinnedMessages;
    private boolean loadingPremiumGiftStickers;
    private boolean loadingRecentGifs;
    boolean loadingRecentReactions;
    private boolean[] loadingRecentStickers;
    boolean loadingSavedReactions;
    private boolean loadingSearchLocal;
    private final HashMap<String, ArrayList<Utilities.Callback2<Boolean, TLRPC.TL_messages_stickerSet>>> loadingStickerSets;
    private final HashSet<String> loadingStickerSetsKeys;
    private boolean[] loadingStickers;
    private int menuBotsUpdateDate;
    private long menuBotsUpdateHash;
    private boolean menuBotsUpdatedLocal;
    private int mergeReqId;
    private int messagesLocalSearchCount;
    private int[] messagesSearchCount;
    private boolean[] messagesSearchEndReached;
    public final ArrayList<TLRPC.Document> premiumPreviewStickers;
    private TLRPC.TL_help_premiumPromo premiumPromo;
    private int premiumPromoUpdateDate;
    boolean previewStickersLoading;
    public TLRPC.TL_emojiList profileAvatarConstructorDefault;
    private boolean reactionsCacheGenerated;
    private List<TLRPC.TL_availableReaction> reactionsList;
    private HashMap<String, TLRPC.TL_availableReaction> reactionsMap;
    private int reactionsUpdateDate;
    private int reactionsUpdateHash;
    private ArrayList<Long>[] readingStickerSets;
    private ArrayList<TLRPC.Document> recentGifs;
    private boolean recentGifsLoaded;
    ArrayList<TLRPC.Reaction> recentReactions;
    private ArrayList<TLRPC.Document>[] recentStickers;
    private boolean[] recentStickersLoaded;
    private LongSparseArray removingStickerSetsUndos;
    public TLRPC.TL_emojiList replyIconsDefault;
    private int reqId;
    public TLRPC.TL_emojiList restrictedStatusEmojis;
    public final RingtoneDataStore ringtoneDataStore;
    public HashMap<String, RingtoneUploader> ringtoneUploaderHashMap;
    ArrayList<TLRPC.Reaction> savedReactions;
    private Runnable[] scheduledLoadStickers;
    public ArrayList<MessageObject> searchLocalResultMessages;
    public ArrayList<MessageObject> searchResultMessages;
    public ArrayList<MessageObject> searchServerResultMessages;
    private SparseArray<MessageObject>[] searchServerResultMessagesMap;
    public final HashMap<String, Utilities.Callback<Boolean>> shortcutCallbacks;
    private TLRPC.TL_messages_stickerSet stickerSetDefaultChannelStatuses;
    private TLRPC.TL_messages_stickerSet stickerSetDefaultStatuses;
    private ArrayList<TLRPC.TL_messages_stickerSet>[] stickerSets;
    private LongSparseArray stickerSetsById;
    private ConcurrentHashMap<String, TLRPC.TL_messages_stickerSet> stickerSetsByName;
    private LongSparseArray stickersByEmoji;
    private LongSparseArray[] stickersByIds;
    private boolean[] stickersLoaded;
    ArrayList<TLRPC.Reaction> topReactions;
    private boolean triedLoadingEmojipacks;
    private ArrayList<Long> uninstalledForceStickerSetsById;
    private ArrayList<Long>[] unreadStickerSets;
    private HashMap<String, ArrayList<TLRPC.Message>> verifyingMessages;
    public ArrayList<TLRPC.TL_topPeer> webapps;
    private static Pattern BOLD_PATTERN = Pattern.compile("\\*\\*(.+?)\\*\\*");
    private static Pattern ITALIC_PATTERN = Pattern.compile("__(.+?)__");
    private static Pattern SPOILER_PATTERN = Pattern.compile("\\|\\|(.+?)\\|\\|");
    private static Pattern STRIKE_PATTERN = Pattern.compile("~~(.+?)~~");
    public static String SHORTCUT_CATEGORY = "org.telegram.messenger.SHORTCUT_SHARE";
    private static volatile MediaDataController[] Instance = new MediaDataController[4];
    private static final Object[] lockObjects = new Object[4];

    class 1 implements Runnable {
        final /* synthetic */ int val$classGuid;
        final /* synthetic */ int val$count;
        final /* synthetic */ int val$fromCache;
        final /* synthetic */ boolean val$isChannel;
        final /* synthetic */ int val$max_id;
        final /* synthetic */ int val$min_id;
        final /* synthetic */ int val$requestIndex;
        final /* synthetic */ ReactionsLayoutInBubble.VisibleReaction val$tag;
        final /* synthetic */ long val$topicId;
        final /* synthetic */ int val$type;
        final /* synthetic */ long val$uid;

        1(int i, long j, int i2, long j2, int i3, ReactionsLayoutInBubble.VisibleReaction visibleReaction, int i4, int i5, int i6, boolean z, int i7) {
            this.val$count = i;
            this.val$uid = j;
            this.val$min_id = i2;
            this.val$topicId = j2;
            this.val$type = i3;
            this.val$tag = visibleReaction;
            this.val$max_id = i4;
            this.val$classGuid = i5;
            this.val$fromCache = i6;
            this.val$isChannel = z;
            this.val$requestIndex = i7;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(Runnable runnable, int i) {
            MediaDataController.this.getMessagesStorage().completeTaskForGuid(runnable, i);
        }

        /* JADX WARN: Removed duplicated region for block: B:103:0x08ee A[Catch: all -> 0x006e, Exception -> 0x0072, TryCatch #0 {Exception -> 0x0072, blocks: (B:4:0x0016, B:6:0x0039, B:8:0x003d, B:10:0x0043, B:11:0x0099, B:13:0x009f, B:16:0x014d, B:17:0x0152, B:19:0x0156, B:21:0x015e, B:22:0x016c, B:23:0x0189, B:25:0x018d, B:27:0x0193, B:28:0x0202, B:30:0x0208, B:31:0x0212, B:35:0x021e, B:37:0x07b8, B:39:0x07bd, B:41:0x07c4, B:43:0x07ca, B:45:0x07d1, B:47:0x07f0, B:48:0x07f7, B:51:0x07ff, B:53:0x0808, B:55:0x0819, B:58:0x0813, B:62:0x0826, B:64:0x0831, B:66:0x0837, B:67:0x083b, B:69:0x0841, B:70:0x084c, B:72:0x0854, B:79:0x0874, B:80:0x088f, B:82:0x0895, B:84:0x08a3, B:91:0x08ce, B:92:0x08d1, B:74:0x0865, B:101:0x08e7, B:103:0x08ee, B:104:0x08f9, B:106:0x08ff, B:107:0x0910, B:109:0x091a, B:111:0x091e, B:117:0x092b, B:121:0x026e, B:125:0x02b8, B:126:0x02ff, B:128:0x01d0, B:129:0x033c, B:131:0x0346, B:133:0x034c, B:134:0x03af, B:136:0x03b5, B:137:0x03bf, B:141:0x03cb, B:143:0x041b, B:147:0x0468, B:148:0x04ac, B:150:0x0383, B:151:0x04ee, B:153:0x04f4, B:154:0x0545, B:156:0x054b, B:157:0x0551, B:161:0x055d, B:162:0x05a3, B:165:0x05e4, B:166:0x061f, B:168:0x0522, B:169:0x0168, B:172:0x00ac, B:174:0x00b5, B:175:0x0103, B:177:0x0109, B:179:0x010f, B:182:0x011a, B:184:0x012d, B:186:0x0136, B:189:0x00e0, B:190:0x0076, B:192:0x0652, B:194:0x065c, B:196:0x0660, B:198:0x06a1, B:200:0x06a5, B:201:0x06e3, B:202:0x0717, B:204:0x071b, B:205:0x0750, B:207:0x0754, B:208:0x0789), top: B:3:0x0016, outer: #1 }] */
        /* JADX WARN: Removed duplicated region for block: B:106:0x08ff A[Catch: all -> 0x006e, Exception -> 0x0072, TryCatch #0 {Exception -> 0x0072, blocks: (B:4:0x0016, B:6:0x0039, B:8:0x003d, B:10:0x0043, B:11:0x0099, B:13:0x009f, B:16:0x014d, B:17:0x0152, B:19:0x0156, B:21:0x015e, B:22:0x016c, B:23:0x0189, B:25:0x018d, B:27:0x0193, B:28:0x0202, B:30:0x0208, B:31:0x0212, B:35:0x021e, B:37:0x07b8, B:39:0x07bd, B:41:0x07c4, B:43:0x07ca, B:45:0x07d1, B:47:0x07f0, B:48:0x07f7, B:51:0x07ff, B:53:0x0808, B:55:0x0819, B:58:0x0813, B:62:0x0826, B:64:0x0831, B:66:0x0837, B:67:0x083b, B:69:0x0841, B:70:0x084c, B:72:0x0854, B:79:0x0874, B:80:0x088f, B:82:0x0895, B:84:0x08a3, B:91:0x08ce, B:92:0x08d1, B:74:0x0865, B:101:0x08e7, B:103:0x08ee, B:104:0x08f9, B:106:0x08ff, B:107:0x0910, B:109:0x091a, B:111:0x091e, B:117:0x092b, B:121:0x026e, B:125:0x02b8, B:126:0x02ff, B:128:0x01d0, B:129:0x033c, B:131:0x0346, B:133:0x034c, B:134:0x03af, B:136:0x03b5, B:137:0x03bf, B:141:0x03cb, B:143:0x041b, B:147:0x0468, B:148:0x04ac, B:150:0x0383, B:151:0x04ee, B:153:0x04f4, B:154:0x0545, B:156:0x054b, B:157:0x0551, B:161:0x055d, B:162:0x05a3, B:165:0x05e4, B:166:0x061f, B:168:0x0522, B:169:0x0168, B:172:0x00ac, B:174:0x00b5, B:175:0x0103, B:177:0x0109, B:179:0x010f, B:182:0x011a, B:184:0x012d, B:186:0x0136, B:189:0x00e0, B:190:0x0076, B:192:0x0652, B:194:0x065c, B:196:0x0660, B:198:0x06a1, B:200:0x06a5, B:201:0x06e3, B:202:0x0717, B:204:0x071b, B:205:0x0750, B:207:0x0754, B:208:0x0789), top: B:3:0x0016, outer: #1 }] */
        /* JADX WARN: Removed duplicated region for block: B:119:0x0930  */
        /* JADX WARN: Removed duplicated region for block: B:120:0x07c3  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x07bd A[Catch: all -> 0x006e, Exception -> 0x0072, TryCatch #0 {Exception -> 0x0072, blocks: (B:4:0x0016, B:6:0x0039, B:8:0x003d, B:10:0x0043, B:11:0x0099, B:13:0x009f, B:16:0x014d, B:17:0x0152, B:19:0x0156, B:21:0x015e, B:22:0x016c, B:23:0x0189, B:25:0x018d, B:27:0x0193, B:28:0x0202, B:30:0x0208, B:31:0x0212, B:35:0x021e, B:37:0x07b8, B:39:0x07bd, B:41:0x07c4, B:43:0x07ca, B:45:0x07d1, B:47:0x07f0, B:48:0x07f7, B:51:0x07ff, B:53:0x0808, B:55:0x0819, B:58:0x0813, B:62:0x0826, B:64:0x0831, B:66:0x0837, B:67:0x083b, B:69:0x0841, B:70:0x084c, B:72:0x0854, B:79:0x0874, B:80:0x088f, B:82:0x0895, B:84:0x08a3, B:91:0x08ce, B:92:0x08d1, B:74:0x0865, B:101:0x08e7, B:103:0x08ee, B:104:0x08f9, B:106:0x08ff, B:107:0x0910, B:109:0x091a, B:111:0x091e, B:117:0x092b, B:121:0x026e, B:125:0x02b8, B:126:0x02ff, B:128:0x01d0, B:129:0x033c, B:131:0x0346, B:133:0x034c, B:134:0x03af, B:136:0x03b5, B:137:0x03bf, B:141:0x03cb, B:143:0x041b, B:147:0x0468, B:148:0x04ac, B:150:0x0383, B:151:0x04ee, B:153:0x04f4, B:154:0x0545, B:156:0x054b, B:157:0x0551, B:161:0x055d, B:162:0x05a3, B:165:0x05e4, B:166:0x061f, B:168:0x0522, B:169:0x0168, B:172:0x00ac, B:174:0x00b5, B:175:0x0103, B:177:0x0109, B:179:0x010f, B:182:0x011a, B:184:0x012d, B:186:0x0136, B:189:0x00e0, B:190:0x0076, B:192:0x0652, B:194:0x065c, B:196:0x0660, B:198:0x06a1, B:200:0x06a5, B:201:0x06e3, B:202:0x0717, B:204:0x071b, B:205:0x0750, B:207:0x0754, B:208:0x0789), top: B:3:0x0016, outer: #1 }] */
        /* JADX WARN: Removed duplicated region for block: B:43:0x07ca A[Catch: all -> 0x006e, Exception -> 0x0072, TryCatch #0 {Exception -> 0x0072, blocks: (B:4:0x0016, B:6:0x0039, B:8:0x003d, B:10:0x0043, B:11:0x0099, B:13:0x009f, B:16:0x014d, B:17:0x0152, B:19:0x0156, B:21:0x015e, B:22:0x016c, B:23:0x0189, B:25:0x018d, B:27:0x0193, B:28:0x0202, B:30:0x0208, B:31:0x0212, B:35:0x021e, B:37:0x07b8, B:39:0x07bd, B:41:0x07c4, B:43:0x07ca, B:45:0x07d1, B:47:0x07f0, B:48:0x07f7, B:51:0x07ff, B:53:0x0808, B:55:0x0819, B:58:0x0813, B:62:0x0826, B:64:0x0831, B:66:0x0837, B:67:0x083b, B:69:0x0841, B:70:0x084c, B:72:0x0854, B:79:0x0874, B:80:0x088f, B:82:0x0895, B:84:0x08a3, B:91:0x08ce, B:92:0x08d1, B:74:0x0865, B:101:0x08e7, B:103:0x08ee, B:104:0x08f9, B:106:0x08ff, B:107:0x0910, B:109:0x091a, B:111:0x091e, B:117:0x092b, B:121:0x026e, B:125:0x02b8, B:126:0x02ff, B:128:0x01d0, B:129:0x033c, B:131:0x0346, B:133:0x034c, B:134:0x03af, B:136:0x03b5, B:137:0x03bf, B:141:0x03cb, B:143:0x041b, B:147:0x0468, B:148:0x04ac, B:150:0x0383, B:151:0x04ee, B:153:0x04f4, B:154:0x0545, B:156:0x054b, B:157:0x0551, B:161:0x055d, B:162:0x05a3, B:165:0x05e4, B:166:0x061f, B:168:0x0522, B:169:0x0168, B:172:0x00ac, B:174:0x00b5, B:175:0x0103, B:177:0x0109, B:179:0x010f, B:182:0x011a, B:184:0x012d, B:186:0x0136, B:189:0x00e0, B:190:0x0076, B:192:0x0652, B:194:0x065c, B:196:0x0660, B:198:0x06a1, B:200:0x06a5, B:201:0x06e3, B:202:0x0717, B:204:0x071b, B:205:0x0750, B:207:0x0754, B:208:0x0789), top: B:3:0x0016, outer: #1 }] */
        /* JADX WARN: Removed duplicated region for block: B:69:0x0841 A[Catch: all -> 0x006e, Exception -> 0x0072, TryCatch #0 {Exception -> 0x0072, blocks: (B:4:0x0016, B:6:0x0039, B:8:0x003d, B:10:0x0043, B:11:0x0099, B:13:0x009f, B:16:0x014d, B:17:0x0152, B:19:0x0156, B:21:0x015e, B:22:0x016c, B:23:0x0189, B:25:0x018d, B:27:0x0193, B:28:0x0202, B:30:0x0208, B:31:0x0212, B:35:0x021e, B:37:0x07b8, B:39:0x07bd, B:41:0x07c4, B:43:0x07ca, B:45:0x07d1, B:47:0x07f0, B:48:0x07f7, B:51:0x07ff, B:53:0x0808, B:55:0x0819, B:58:0x0813, B:62:0x0826, B:64:0x0831, B:66:0x0837, B:67:0x083b, B:69:0x0841, B:70:0x084c, B:72:0x0854, B:79:0x0874, B:80:0x088f, B:82:0x0895, B:84:0x08a3, B:91:0x08ce, B:92:0x08d1, B:74:0x0865, B:101:0x08e7, B:103:0x08ee, B:104:0x08f9, B:106:0x08ff, B:107:0x0910, B:109:0x091a, B:111:0x091e, B:117:0x092b, B:121:0x026e, B:125:0x02b8, B:126:0x02ff, B:128:0x01d0, B:129:0x033c, B:131:0x0346, B:133:0x034c, B:134:0x03af, B:136:0x03b5, B:137:0x03bf, B:141:0x03cb, B:143:0x041b, B:147:0x0468, B:148:0x04ac, B:150:0x0383, B:151:0x04ee, B:153:0x04f4, B:154:0x0545, B:156:0x054b, B:157:0x0551, B:161:0x055d, B:162:0x05a3, B:165:0x05e4, B:166:0x061f, B:168:0x0522, B:169:0x0168, B:172:0x00ac, B:174:0x00b5, B:175:0x0103, B:177:0x0109, B:179:0x010f, B:182:0x011a, B:184:0x012d, B:186:0x0136, B:189:0x00e0, B:190:0x0076, B:192:0x0652, B:194:0x065c, B:196:0x0660, B:198:0x06a1, B:200:0x06a5, B:201:0x06e3, B:202:0x0717, B:204:0x071b, B:205:0x0750, B:207:0x0754, B:208:0x0789), top: B:3:0x0016, outer: #1 }] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            Runnable runnable;
            ArrayList<Long> arrayList;
            ArrayList arrayList2;
            int i;
            SQLiteDatabase database;
            ArrayList<Long> arrayList3;
            ArrayList arrayList4;
            SQLiteCursor queryFinalized;
            boolean z;
            boolean z2;
            boolean z3;
            HashSet hashSet;
            ArrayList arrayList5;
            ArrayList<Long> arrayList6;
            boolean z4;
            Iterator it;
            ArrayList<Long> arrayList7;
            ArrayList<Long> arrayList8;
            ArrayList arrayList9;
            boolean z5;
            String str;
            String str2;
            int i2;
            SQLiteCursor queryFinalized2;
            int i3;
            SQLiteCursor queryFinalized3;
            int i4;
            int i5;
            SQLiteCursor queryFinalized4;
            int i6;
            int intValue;
            int i7;
            long clientUserId = MediaDataController.this.getUserConfig().getClientUserId();
            TLRPC.TL_messages_messages tL_messages_messages = new TLRPC.TL_messages_messages();
            boolean z6 = false;
            try {
                try {
                    arrayList = new ArrayList<>();
                    arrayList2 = new ArrayList();
                    i = this.val$count + 1;
                    database = MediaDataController.this.getMessagesStorage().getDatabase();
                } catch (Exception e) {
                    tL_messages_messages.messages.clear();
                    tL_messages_messages.chats.clear();
                    tL_messages_messages.users.clear();
                    FileLog.e(e);
                    final int i8 = this.val$classGuid;
                    runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaDataController.1.this.lambda$run$0(this, i8);
                        }
                    };
                }
                if (!DialogObject.isEncryptedDialog(this.val$uid)) {
                    if (this.val$min_id == 0) {
                        SQLiteCursor queryFinalized5 = this.val$topicId != 0 ? database.queryFinalized(String.format(Locale.US, "SELECT start FROM media_holes_topics WHERE uid = %d AND topic_id = %d AND type = %d AND start IN (0, 1)", Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$type)), new Object[0]) : database.queryFinalized(String.format(Locale.US, "SELECT start FROM media_holes_v2 WHERE uid = %d AND type = %d AND start IN (0, 1)", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type)), new Object[0]);
                        if (!queryFinalized5.next()) {
                            queryFinalized5.dispose();
                            queryFinalized5 = this.val$topicId != 0 ? database.queryFinalized(String.format(Locale.US, "SELECT min(mid) FROM media_topics WHERE uid = %d AND topic_id = %d AND type = %d AND mid > 0", Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$type)), new Object[0]) : database.queryFinalized(String.format(Locale.US, "SELECT min(mid) FROM media_v4 WHERE uid = %d AND type = %d AND mid > 0", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type)), new Object[0]);
                            if (queryFinalized5.next() && (intValue = queryFinalized5.intValue(0)) != 0) {
                                SQLitePreparedStatement executeFast = database.executeFast(this.val$topicId != 0 ? "REPLACE INTO media_holes_topics VALUES(?, ?, ?, ?, ?)" : "REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
                                executeFast.requery();
                                executeFast.bindLong(1, this.val$uid);
                                long j = this.val$topicId;
                                if (j != 0) {
                                    executeFast.bindLong(2, j);
                                    i7 = 3;
                                } else {
                                    i7 = 2;
                                }
                                executeFast.bindInteger(i7, this.val$type);
                                executeFast.bindInteger(i7 + 1, 0);
                                executeFast.bindInteger(i7 + 2, intValue);
                                executeFast.step();
                                executeFast.dispose();
                            }
                        } else if (queryFinalized5.intValue(0) == 1) {
                            z5 = true;
                            queryFinalized5.dispose();
                        }
                        z5 = false;
                        queryFinalized5.dispose();
                    } else {
                        z5 = false;
                    }
                    ReactionsLayoutInBubble.VisibleReaction visibleReaction = this.val$tag;
                    if (visibleReaction != null) {
                        long hashCode = !TextUtils.isEmpty(visibleReaction.emojicon) ? this.val$tag.emojicon.hashCode() : this.val$tag.documentId;
                        str = "INNER JOIN tag_message_id t ON m.mid = t.mid";
                        str2 = "t.tag = " + hashCode + " AND";
                    } else {
                        str = "";
                        str2 = "";
                    }
                    if (this.val$max_id != 0) {
                        if (this.val$topicId != 0) {
                            z = z5;
                            arrayList3 = arrayList;
                            arrayList4 = arrayList2;
                            queryFinalized4 = database.queryFinalized(String.format(Locale.US, "SELECT start, end FROM media_holes_topics WHERE uid = %d AND topic_id = %d AND type = %d AND start <= %d ORDER BY end DESC LIMIT 1", Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$type), Integer.valueOf(this.val$max_id)), new Object[0]);
                            i5 = 0;
                        } else {
                            z = z5;
                            arrayList3 = arrayList;
                            arrayList4 = arrayList2;
                            i5 = 0;
                            queryFinalized4 = database.queryFinalized(String.format(Locale.US, "SELECT start, end FROM media_holes_v2 WHERE uid = %d AND type = %d AND start <= %d ORDER BY end DESC LIMIT 1", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type), Integer.valueOf(this.val$max_id)), new Object[0]);
                        }
                        if (queryFinalized4.next()) {
                            queryFinalized4.intValue(i5);
                            i6 = queryFinalized4.intValue(1);
                        } else {
                            i6 = 0;
                        }
                        queryFinalized4.dispose();
                        if (this.val$topicId != 0) {
                            if (i6 > 1) {
                                queryFinalized = database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid FROM media_topics m %s WHERE %s m.uid = %d AND m.topic_id = %d AND m.mid > 0 AND m.mid < %d AND m.mid >= %d AND m.type = %d ORDER BY m.date DESC, m.mid DESC LIMIT %d", str, str2, Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$max_id), Integer.valueOf(i6), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]);
                                z3 = false;
                                z2 = false;
                            } else {
                                queryFinalized = database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid FROM media_topics m %s WHERE %s m.uid = %d AND m.topic_id = %d AND m.mid > 0 AND m.mid < %d AND m.type = %d ORDER BY m.date DESC, m.mid DESC LIMIT %d", str, str2, Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$max_id), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]);
                            }
                        } else if (i6 > 1) {
                            queryFinalized = database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid FROM media_v4 m %s WHERE %s m.uid = %d AND m.mid > 0 AND m.mid < %d AND m.mid >= %d AND m.type = %d ORDER BY m.date DESC, m.mid DESC LIMIT %d", str, str2, Long.valueOf(this.val$uid), Integer.valueOf(this.val$max_id), Integer.valueOf(i6), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]);
                            z3 = false;
                            z2 = false;
                        } else {
                            queryFinalized = database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid FROM media_v4 m %s WHERE %s m.uid = %d AND m.mid > 0 AND m.mid < %d AND m.type = %d ORDER BY m.date DESC, m.mid DESC LIMIT %d", str, str2, Long.valueOf(this.val$uid), Integer.valueOf(this.val$max_id), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]);
                        }
                    } else {
                        z = z5;
                        arrayList3 = arrayList;
                        arrayList4 = arrayList2;
                        if (this.val$min_id != 0) {
                            if (this.val$topicId != 0) {
                                queryFinalized3 = database.queryFinalized(String.format(Locale.US, "SELECT start, end FROM media_holes_topics WHERE uid = %d AND topic_id = %d AND type = %d AND end >= %d ORDER BY end ASC LIMIT 1", Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$type), Integer.valueOf(this.val$min_id)), new Object[0]);
                                i3 = 0;
                            } else {
                                i3 = 0;
                                queryFinalized3 = database.queryFinalized(String.format(Locale.US, "SELECT start, end FROM media_holes_v2 WHERE uid = %d AND type = %d AND end >= %d ORDER BY end ASC LIMIT 1", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type), Integer.valueOf(this.val$min_id)), new Object[0]);
                            }
                            if (queryFinalized3.next()) {
                                i4 = queryFinalized3.intValue(i3);
                                queryFinalized3.intValue(1);
                            } else {
                                i4 = 0;
                            }
                            queryFinalized3.dispose();
                            if (this.val$topicId == 0) {
                                int i9 = i4;
                                if (i9 > 1) {
                                    queryFinalized = database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid FROM media_v4 m %s WHERE %s m.uid = %d AND m.mid > 0 AND m.mid >= %d AND m.mid <= %d AND m.type = %d ORDER BY m.date ASC, m.mid ASC LIMIT %d", str, str2, Long.valueOf(this.val$uid), Integer.valueOf(this.val$min_id), Integer.valueOf(i9), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]);
                                    z2 = z;
                                    z3 = true;
                                } else {
                                    queryFinalized = database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid FROM media_v4 m %s WHERE %s m.uid = %d AND m.mid > 0 AND m.mid >= %d AND m.type = %d ORDER BY m.date ASC, m.mid ASC LIMIT %d", str, str2, Long.valueOf(this.val$uid), Integer.valueOf(this.val$min_id), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]);
                                    z = true;
                                    z2 = z;
                                    z3 = true;
                                }
                            } else if (i4 > 1) {
                                queryFinalized = database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid FROM media_topics m %s WHERE %s m.uid = %d AND m.topic_id = %d AND m.mid > 0 AND m.mid >= %d AND m.mid <= %d AND m.type = %d ORDER BY m.date ASC, m.mid ASC LIMIT %d", str, str2, Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$min_id), Integer.valueOf(i4), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]);
                                z2 = z;
                                z3 = true;
                            } else {
                                queryFinalized = database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid FROM media_topics m %s WHERE %s m.uid = %d AND m.topic_id = %d AND m.mid > 0 AND m.mid >= %d AND m.type = %d ORDER BY m.date ASC, m.mid ASC LIMIT %d", str, str2, Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$min_id), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]);
                                z = true;
                                z2 = z;
                                z3 = true;
                            }
                        } else {
                            if (this.val$topicId != 0) {
                                queryFinalized2 = database.queryFinalized(String.format(Locale.US, "SELECT max(end) FROM media_holes_topics WHERE uid = %d AND topic_id = %d AND type = %d", Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$type)), new Object[0]);
                                i2 = 0;
                            } else {
                                i2 = 0;
                                queryFinalized2 = database.queryFinalized(String.format(Locale.US, "SELECT max(end) FROM media_holes_v2 WHERE uid = %d AND type = %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type)), new Object[0]);
                            }
                            int intValue2 = queryFinalized2.next() ? queryFinalized2.intValue(i2) : 0;
                            queryFinalized2.dispose();
                            if (this.val$topicId != 0) {
                                queryFinalized = intValue2 > 1 ? database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid FROM media_topics m %s WHERE %s m.uid = %d AND m.topic_id = %d AND m.mid >= %d AND m.type = %d ORDER BY m.date DESC, m.mid DESC LIMIT %d", str, str2, Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(intValue2), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]) : database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid FROM media_topics m %s WHERE %s m.uid = %d AND m.topic_id = %d AND m.mid > 0 AND m.type = %d ORDER BY m.date DESC, m.mid DESC LIMIT %d", str, str2, Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]);
                            } else {
                                int i10 = intValue2;
                                queryFinalized = i10 > 1 ? database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid FROM media_v4 m %s WHERE %s m.uid = %d AND m.mid >= %d AND m.type = %d ORDER BY m.date DESC, m.mid DESC LIMIT %d", str, str2, Long.valueOf(this.val$uid), Integer.valueOf(i10), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]) : database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid FROM media_v4 m %s WHERE %s m.uid = %d AND m.mid > 0 AND m.type = %d ORDER BY m.date DESC, m.mid DESC LIMIT %d", str, str2, Long.valueOf(this.val$uid), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]);
                            }
                        }
                    }
                    hashSet = this.val$tag == null ? new HashSet() : null;
                    while (queryFinalized.next()) {
                        NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(0);
                        if (byteBufferValue != null) {
                            TLRPC.Message TLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                            TLdeserialize.readAttachPath(byteBufferValue, clientUserId);
                            byteBufferValue.reuse();
                            TLdeserialize.id = queryFinalized.intValue(1);
                            long j2 = this.val$uid;
                            TLdeserialize.dialog_id = j2;
                            if (DialogObject.isEncryptedDialog(j2)) {
                                TLdeserialize.random_id = queryFinalized.longValue(2);
                            }
                            long j3 = TLdeserialize.grouped_id;
                            if (j3 != 0 && hashSet != null) {
                                hashSet.add(Long.valueOf(j3));
                            }
                            if (z3) {
                                tL_messages_messages.messages.add(0, TLdeserialize);
                            } else {
                                tL_messages_messages.messages.add(TLdeserialize);
                            }
                            arrayList8 = arrayList3;
                            arrayList9 = arrayList4;
                            MessagesStorage.addUsersAndChatsFromMessage(TLdeserialize, arrayList8, arrayList9, null);
                        } else {
                            arrayList8 = arrayList3;
                            arrayList9 = arrayList4;
                        }
                        arrayList3 = arrayList8;
                        arrayList4 = arrayList9;
                    }
                    ArrayList<Long> arrayList10 = arrayList3;
                    arrayList5 = arrayList4;
                    queryFinalized.dispose();
                    if (this.val$tag != null && !hashSet.isEmpty()) {
                        it = hashSet.iterator();
                        while (it.hasNext()) {
                            Long l = (Long) it.next();
                            long longValue = l.longValue();
                            int i11 = 0;
                            while (true) {
                                if (i11 >= tL_messages_messages.messages.size()) {
                                    arrayList7 = arrayList10;
                                    i11 = -1;
                                    break;
                                } else {
                                    arrayList7 = arrayList10;
                                    if (tL_messages_messages.messages.get(i11).grouped_id == longValue) {
                                        break;
                                    }
                                    i11++;
                                    arrayList10 = arrayList7;
                                }
                            }
                            if (i11 < 0) {
                                arrayList10 = arrayList7;
                            } else {
                                SQLiteCursor queryFinalized6 = database.queryFinalized("SELECT data, mid FROM messages_v2 WHERE uid = ? AND group_id = ? ORDER BY mid DESC", Long.valueOf(this.val$uid), l);
                                ArrayList arrayList11 = new ArrayList();
                                while (queryFinalized6.next()) {
                                    int intValue3 = queryFinalized6.intValue(1);
                                    NativeByteBuffer byteBufferValue2 = queryFinalized6.byteBufferValue(0);
                                    if (byteBufferValue2 != null) {
                                        Iterator it2 = it;
                                        TLRPC.Message TLdeserialize2 = TLRPC.Message.TLdeserialize(byteBufferValue2, byteBufferValue2.readInt32(false), false);
                                        TLdeserialize2.readAttachPath(byteBufferValue2, clientUserId);
                                        byteBufferValue2.reuse();
                                        TLdeserialize2.id = intValue3;
                                        TLdeserialize2.dialog_id = this.val$uid;
                                        arrayList11.add(TLdeserialize2);
                                        ArrayList<Long> arrayList12 = arrayList7;
                                        MessagesStorage.addUsersAndChatsFromMessage(TLdeserialize2, arrayList12, arrayList5, null);
                                        arrayList7 = arrayList12;
                                        it = it2;
                                    }
                                }
                                Iterator it3 = it;
                                ArrayList<Long> arrayList13 = arrayList7;
                                if (z3) {
                                    Collections.reverse(arrayList11);
                                }
                                tL_messages_messages.messages.remove(i11);
                                tL_messages_messages.messages.addAll(i11, arrayList11);
                                queryFinalized6.dispose();
                                it = it3;
                                arrayList10 = arrayList13;
                            }
                        }
                    }
                    arrayList6 = arrayList10;
                    if (!arrayList6.isEmpty()) {
                        MediaDataController.this.getMessagesStorage().getUsersInternal(arrayList6, tL_messages_messages.users);
                    }
                    if (!arrayList5.isEmpty()) {
                        MediaDataController.this.getMessagesStorage().getChatsInternal(TextUtils.join(",", arrayList5), tL_messages_messages.chats);
                    }
                    if (tL_messages_messages.messages.size() <= this.val$count && this.val$min_id == 0) {
                        ArrayList<TLRPC.Message> arrayList14 = tL_messages_messages.messages;
                        arrayList14.remove(arrayList14.size() - 1);
                    } else if (this.val$min_id == 0) {
                        z4 = z2;
                        final int i12 = this.val$classGuid;
                        runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$1$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                MediaDataController.1.this.lambda$run$0(this, i12);
                            }
                        };
                        z6 = z4;
                        AndroidUtilities.runOnUIThread(runnable);
                        MediaDataController.this.processLoadedMedia(tL_messages_messages, this.val$uid, this.val$count, this.val$max_id, this.val$min_id, this.val$type, this.val$topicId, this.val$fromCache, this.val$classGuid, this.val$isChannel, z6, this.val$requestIndex);
                    }
                    z4 = false;
                    final int i122 = this.val$classGuid;
                    runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaDataController.1.this.lambda$run$0(this, i122);
                        }
                    };
                    z6 = z4;
                    AndroidUtilities.runOnUIThread(runnable);
                    MediaDataController.this.processLoadedMedia(tL_messages_messages, this.val$uid, this.val$count, this.val$max_id, this.val$min_id, this.val$type, this.val$topicId, this.val$fromCache, this.val$classGuid, this.val$isChannel, z6, this.val$requestIndex);
                }
                arrayList3 = arrayList;
                arrayList4 = arrayList2;
                queryFinalized = this.val$topicId != 0 ? this.val$max_id != 0 ? database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid, r.random_id FROM media_topics as m LEFT JOIN randoms_v2 as r ON r.mid = m.mid WHERE m.uid = %d AND m.topic_id = %d AND m.mid > %d AND type = %d ORDER BY m.mid ASC LIMIT %d", Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$max_id), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]) : this.val$min_id != 0 ? database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid, r.random_id FROM media_topics as m LEFT JOIN randoms_v2 as r ON r.mid = m.mid WHERE m.uid = %d AND m.topic_id = %d AND m.mid < %d AND type = %d ORDER BY m.mid DESC LIMIT %d", Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$min_id), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]) : database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid, r.random_id FROM media_topics as m LEFT JOIN randoms_v2 as r ON r.mid = m.mid WHERE m.uid = %d AND m.topic_id = %d AND type = %d ORDER BY m.mid ASC LIMIT %d", Long.valueOf(this.val$uid), Long.valueOf(this.val$topicId), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]) : this.val$max_id != 0 ? database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid, r.random_id FROM media_v4 as m LEFT JOIN randoms_v2 as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid > %d AND type = %d ORDER BY m.mid ASC LIMIT %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$max_id), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]) : this.val$min_id != 0 ? database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid, r.random_id FROM media_v4 as m LEFT JOIN randoms_v2 as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid < %d AND type = %d ORDER BY m.mid DESC LIMIT %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$min_id), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]) : database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid, r.random_id FROM media_v4 as m LEFT JOIN randoms_v2 as r ON r.mid = m.mid WHERE m.uid = %d AND type = %d ORDER BY m.mid ASC LIMIT %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type), Integer.valueOf(i)), new Object[0]);
                z = true;
                z2 = z;
                z3 = false;
                if (this.val$tag == null) {
                }
                while (queryFinalized.next()) {
                }
                ArrayList<Long> arrayList102 = arrayList3;
                arrayList5 = arrayList4;
                queryFinalized.dispose();
                if (this.val$tag != null) {
                    it = hashSet.iterator();
                    while (it.hasNext()) {
                    }
                }
                arrayList6 = arrayList102;
                if (!arrayList6.isEmpty()) {
                }
                if (!arrayList5.isEmpty()) {
                }
                if (tL_messages_messages.messages.size() <= this.val$count) {
                }
                if (this.val$min_id == 0) {
                }
                z4 = false;
                final int i1222 = this.val$classGuid;
                runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.1.this.lambda$run$0(this, i1222);
                    }
                };
                z6 = z4;
                AndroidUtilities.runOnUIThread(runnable);
                MediaDataController.this.processLoadedMedia(tL_messages_messages, this.val$uid, this.val$count, this.val$max_id, this.val$min_id, this.val$type, this.val$topicId, this.val$fromCache, this.val$classGuid, this.val$isChannel, z6, this.val$requestIndex);
            } catch (Throwable th) {
                final int i13 = this.val$classGuid;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.1.this.lambda$run$0(this, i13);
                    }
                });
                MediaDataController.this.processLoadedMedia(tL_messages_messages, this.val$uid, this.val$count, this.val$max_id, this.val$min_id, this.val$type, this.val$topicId, this.val$fromCache, this.val$classGuid, this.val$isChannel, false, this.val$requestIndex);
                throw th;
            }
        }
    }

    class 2 implements Runnable {
        final /* synthetic */ ArrayList val$previewItems;

        2(ArrayList arrayList) {
            this.val$previewItems = arrayList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(ArrayList arrayList) {
            MediaDataController.this.defaultEmojiThemes.clear();
            MediaDataController.this.defaultEmojiThemes.addAll(arrayList);
        }

        @Override // java.lang.Runnable
        public void run() {
            for (int i = 0; i < this.val$previewItems.size(); i++) {
                if (this.val$previewItems.get(i) != null && ((ChatThemeBottomSheet.ChatThemeItem) this.val$previewItems.get(i)).chatTheme != null) {
                    ((ChatThemeBottomSheet.ChatThemeItem) this.val$previewItems.get(i)).chatTheme.loadPreviewColors(0);
                }
            }
            final ArrayList arrayList = this.val$previewItems;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.2.this.lambda$run$0(arrayList);
                }
            });
        }
    }

    class 3 implements Runnable {
        final /* synthetic */ int val$currentAccount;
        final /* synthetic */ ArrayList val$previewItems;

        3(ArrayList arrayList, int i) {
            this.val$previewItems = arrayList;
            this.val$currentAccount = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(ArrayList arrayList) {
            MediaDataController.this.defaultEmojiThemes.clear();
            MediaDataController.this.defaultEmojiThemes.addAll(arrayList);
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.emojiPreviewThemesChanged, new Object[0]);
        }

        @Override // java.lang.Runnable
        public void run() {
            for (int i = 0; i < this.val$previewItems.size(); i++) {
                ((ChatThemeBottomSheet.ChatThemeItem) this.val$previewItems.get(i)).chatTheme.loadPreviewColors(this.val$currentAccount);
            }
            final ArrayList arrayList = this.val$previewItems;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.3.this.lambda$run$0(arrayList);
                }
            });
        }
    }

    public static class DraftVoice {
        public long id;
        public boolean once;
        public String path;
        public short[] recordSamples;
        public long recordTimeCount;
        public long samplesCount;
        public int writedFrame;

        public static DraftVoice fromString(String str) {
            if (str == null) {
                return null;
            }
            try {
                String[] split = str.split("\n");
                if (split.length < 6) {
                    return null;
                }
                DraftVoice draftVoice = new DraftVoice();
                int i = 0;
                draftVoice.path = split[0];
                boolean z = true;
                draftVoice.samplesCount = Long.parseLong(split[1]);
                draftVoice.writedFrame = Integer.parseInt(split[2]);
                draftVoice.recordTimeCount = Long.parseLong(split[3]);
                if (Integer.parseInt(split[4]) == 0) {
                    z = false;
                }
                draftVoice.once = z;
                int length = split.length - 5;
                String[] strArr = new String[length];
                for (int i2 = 0; i2 < length; i2++) {
                    strArr[i2] = split[i2 + 5];
                }
                String join = TextUtils.join("\n", strArr);
                draftVoice.recordSamples = new short[join.length()];
                while (true) {
                    short[] sArr = draftVoice.recordSamples;
                    if (i >= sArr.length) {
                        return draftVoice;
                    }
                    sArr[i] = (short) join.charAt(i);
                    i++;
                }
            } catch (Exception e) {
                FileLog.e(e);
                return null;
            }
        }

        public static DraftVoice of(MediaController mediaController, String str, boolean z) {
            if (mediaController.recordingAudio == null) {
                return null;
            }
            DraftVoice draftVoice = new DraftVoice();
            draftVoice.path = str;
            draftVoice.samplesCount = mediaController.samplesCount;
            draftVoice.writedFrame = mediaController.writedFrame;
            draftVoice.recordTimeCount = mediaController.recordTimeCount;
            draftVoice.id = mediaController.recordingAudio.id;
            draftVoice.recordSamples = mediaController.recordSamples;
            draftVoice.once = z;
            return draftVoice;
        }

        public String toString() {
            char[] cArr = new char[this.recordSamples.length];
            int i = 0;
            while (true) {
                short[] sArr = this.recordSamples;
                if (i >= sArr.length) {
                    return this.path + "\n" + this.samplesCount + "\n" + this.writedFrame + "\n" + this.recordTimeCount + "\n" + (this.once ? 1 : 0) + "\n" + new String(cArr);
                }
                cArr[i] = (char) sArr[i];
                i++;
            }
        }
    }

    public static class KeywordResult {
        public String emoji;
        public String keyword;

        public KeywordResult() {
        }

        public KeywordResult(String str, String str2) {
            this.emoji = str;
            this.keyword = str2;
        }
    }

    public interface KeywordResultCallback {
        void run(ArrayList<KeywordResult> arrayList, String str);
    }

    static {
        for (int i = 0; i < 4; i++) {
            lockObjects[i] = new Object();
        }
        SHORTCUT_TYPE_USER_OR_CHAT = 0;
        SHORTCUT_TYPE_ATTACHED_BOT = 1;
        entityComparator = new Comparator() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda190
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$static$160;
                lambda$static$160 = MediaDataController.lambda$static$160((TLRPC.MessageEntity) obj, (TLRPC.MessageEntity) obj2);
                return lambda$static$160;
            }
        };
    }

    public MediaDataController(int i) {
        super(i);
        Context context;
        String key;
        long longValue;
        SerializedData serializedData;
        boolean z;
        this.attachMenuBots = new TLRPC.TL_attachMenuBots();
        this.reactionsList = new ArrayList();
        this.enabledReactionsList = new ArrayList();
        this.reactionsMap = new HashMap<>();
        this.stickerSets = new ArrayList[]{new ArrayList<>(), new ArrayList<>(), new ArrayList<>(0), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()};
        this.stickersByIds = new LongSparseArray[]{new LongSparseArray(), new LongSparseArray(), new LongSparseArray(), new LongSparseArray(), new LongSparseArray(), new LongSparseArray()};
        this.stickerSetsById = new LongSparseArray();
        this.installedStickerSetsById = new LongSparseArray();
        this.installedForceStickerSetsById = new ArrayList<>();
        this.uninstalledForceStickerSetsById = new ArrayList<>();
        this.groupStickerSets = new LongSparseArray();
        this.stickerSetsByName = new ConcurrentHashMap<>(100, 1.0f, 1);
        this.stickerSetDefaultStatuses = null;
        this.stickerSetDefaultChannelStatuses = null;
        this.diceStickerSetsByEmoji = new HashMap<>();
        this.diceEmojiStickerSetsById = new LongSparseArray();
        this.loadingDiceStickerSets = new HashSet<>();
        this.removingStickerSetsUndos = new LongSparseArray();
        this.scheduledLoadStickers = new Runnable[7];
        this.loadingStickers = new boolean[7];
        this.stickersLoaded = new boolean[7];
        this.loadHash = new long[7];
        this.loadDate = new int[7];
        this.ringtoneUploaderHashMap = new HashMap<>();
        this.verifyingMessages = new HashMap<>();
        this.archivedStickersCount = new int[7];
        this.stickersByEmoji = new LongSparseArray();
        this.allStickers = new HashMap<>();
        this.allStickersFeatured = new HashMap<>();
        this.recentStickers = new ArrayList[]{new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()};
        this.loadingRecentStickers = new boolean[9];
        this.recentStickersLoaded = new boolean[9];
        this.recentGifs = new ArrayList<>();
        this.loadFeaturedHash = new long[2];
        this.loadFeaturedDate = new int[2];
        this.featuredStickerSets = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
        this.featuredStickerSetsById = new LongSparseArray[]{new LongSparseArray(), new LongSparseArray()};
        this.unreadStickerSets = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
        this.readingStickerSets = new ArrayList[]{new ArrayList<>(), new ArrayList<>()};
        this.loadingFeaturedStickers = new boolean[2];
        this.featuredStickersLoaded = new boolean[2];
        this.defaultEmojiThemes = new ArrayList<>();
        this.premiumPreviewStickers = new ArrayList<>();
        this.emojiStatusesHash = new long[4];
        this.emojiStatuses = new ArrayList[4];
        this.emojiStatusesFetchDate = new Long[4];
        this.emojiStatusesFromCacheFetched = new boolean[4];
        this.emojiStatusesFetching = new boolean[4];
        this.loadingStickerSetsKeys = new HashSet<>();
        this.loadingStickerSets = new HashMap<>();
        this.messagesSearchCount = new int[]{0, 0};
        this.messagesSearchEndReached = new boolean[]{false, false};
        this.searchResultMessages = new ArrayList<>();
        this.searchServerResultMessages = new ArrayList<>();
        this.searchLocalResultMessages = new ArrayList<>();
        this.searchServerResultMessagesMap = new SparseArray[]{new SparseArray<>(), new SparseArray<>()};
        this.deletedFromResultMessages = new ArrayList<>();
        this.hints = new ArrayList<>();
        this.inlineBots = new ArrayList<>();
        this.webapps = new ArrayList<>();
        this.shortcutCallbacks = new HashMap<>();
        this.loadingPinnedMessages = new LongSparseArray();
        this.draftsFolderIds = new LongSparseArray();
        this.drafts = new LongSparseArray();
        this.draftMessages = new LongSparseArray();
        this.botInfos = new HashMap<>();
        this.botDialogKeyboards = new LongSparseArray();
        this.botKeyboards = new HashMap<>();
        this.botKeyboardsByMids = new LongSparseArray();
        this.currentFetchingEmoji = new HashMap<>();
        this.fetchedEmoji = new HashSet<>();
        this.triedLoadingEmojipacks = false;
        this.recentReactions = new ArrayList<>();
        this.topReactions = new ArrayList<>();
        this.savedReactions = new ArrayList<>();
        this.draftVoicesLoaded = false;
        this.draftVoices = new LongSparseArray();
        String str = "drafts";
        if (this.currentAccount == 0) {
            context = ApplicationLoader.applicationContext;
        } else {
            context = ApplicationLoader.applicationContext;
            str = "drafts" + this.currentAccount;
        }
        this.draftPreferences = context.getSharedPreferences(str, 0);
        ArrayList<TLRPC.Message> arrayList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : this.draftPreferences.getAll().entrySet()) {
            try {
                key = entry.getKey();
                longValue = Utilities.parseLong(key).longValue();
                serializedData = new SerializedData(Utilities.hexToBytes((String) entry.getValue()));
            } catch (Exception unused) {
            }
            if (key.startsWith("r_")) {
                z = false;
            } else {
                z = key.startsWith("rt_");
                if (!z) {
                    TLRPC.DraftMessage TLdeserialize = TLRPC.DraftMessage.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                    if (TLdeserialize != null) {
                        LongSparseArray longSparseArray = (LongSparseArray) this.drafts.get(longValue);
                        if (longSparseArray == null) {
                            longSparseArray = new LongSparseArray();
                            this.drafts.put(longValue, longSparseArray);
                        }
                        longSparseArray.put(key.startsWith("t_") ? Utilities.parseInt((CharSequence) key.substring(key.lastIndexOf(95) + 1)).intValue() : 0L, TLdeserialize);
                    }
                    serializedData.cleanup();
                }
            }
            TLRPC.Message TLdeserialize2 = TLRPC.Message.TLdeserialize(serializedData, serializedData.readInt32(true), true);
            if (TLdeserialize2 != null) {
                TLdeserialize2.readAttachPath(serializedData, getUserConfig().clientUserId);
                LongSparseArray longSparseArray2 = (LongSparseArray) this.draftMessages.get(longValue);
                if (longSparseArray2 == null) {
                    longSparseArray2 = new LongSparseArray();
                    this.draftMessages.put(longValue, longSparseArray2);
                }
                longSparseArray2.put(z ? Utilities.parseInt((CharSequence) key.substring(key.lastIndexOf(95) + 1)).intValue() : 0, TLdeserialize2);
                if (TLdeserialize2.reply_to != null) {
                    arrayList.add(TLdeserialize2);
                }
            }
            serializedData.cleanup();
        }
        loadRepliesOfDraftReplies(arrayList);
        loadStickersByEmojiOrName(AndroidUtilities.STICKERS_PLACEHOLDER_PACK_NAME, false, true);
        loadEmojiThemes();
        loadRecentAndTopReactions(false);
        loadAvatarConstructor(false);
        loadAvatarConstructor(true);
        this.ringtoneDataStore = new RingtoneDataStore(this.currentAccount);
        this.menuBotsUpdateDate = getMessagesController().getMainSettings().getInt("menuBotsUpdateDate", 0);
    }

    public static void addAnimatedEmojiSpans(ArrayList<TLRPC.MessageEntity> arrayList, CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt) {
        if (!(charSequence instanceof Spannable) || arrayList == null) {
            return;
        }
        Spannable spannable = (Spannable) charSequence;
        for (AnimatedEmojiSpan animatedEmojiSpan : (AnimatedEmojiSpan[]) spannable.getSpans(0, spannable.length(), AnimatedEmojiSpan.class)) {
            if (animatedEmojiSpan != null) {
                spannable.removeSpan(animatedEmojiSpan);
            }
        }
        for (int i = 0; i < arrayList.size(); i++) {
            TLRPC.MessageEntity messageEntity = arrayList.get(i);
            if (messageEntity instanceof TLRPC.TL_messageEntityCustomEmoji) {
                TLRPC.TL_messageEntityCustomEmoji tL_messageEntityCustomEmoji = (TLRPC.TL_messageEntityCustomEmoji) messageEntity;
                int i2 = messageEntity.offset;
                int i3 = messageEntity.length + i2;
                if (i2 < i3 && i3 <= spannable.length()) {
                    spannable.setSpan(tL_messageEntityCustomEmoji.document != null ? new AnimatedEmojiSpan(tL_messageEntityCustomEmoji.document, fontMetricsInt) : new AnimatedEmojiSpan(tL_messageEntityCustomEmoji.document_id, fontMetricsInt), i2, i3, 33);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x008d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void addStyleToText(TextStyleSpan textStyleSpan, int i, int i2, Spannable spannable, boolean z) {
        TextStyleSpan.TextStyleRun textStyleRun;
        int i3;
        try {
            CharacterStyle[] characterStyleArr = (CharacterStyle[]) spannable.getSpans(i, i2, CharacterStyle.class);
            if (characterStyleArr != null && characterStyleArr.length > 0) {
                for (CharacterStyle characterStyle : characterStyleArr) {
                    TextStyleSpan.TextStyleRun textStyleRun2 = textStyleSpan != null ? textStyleSpan.getTextStyleRun() : new TextStyleSpan.TextStyleRun();
                    if (characterStyle instanceof TextStyleSpan) {
                        textStyleRun = ((TextStyleSpan) characterStyle).getTextStyleRun();
                    } else if (characterStyle instanceof URLSpanReplacement) {
                        textStyleRun = ((URLSpanReplacement) characterStyle).getTextStyleRun();
                        if (textStyleRun == null) {
                            textStyleRun = new TextStyleSpan.TextStyleRun();
                        }
                    }
                    if (textStyleRun != null) {
                        int spanStart = spannable.getSpanStart(characterStyle);
                        int spanEnd = spannable.getSpanEnd(characterStyle);
                        spannable.removeSpan(characterStyle);
                        if (spanStart <= i || i2 <= spanEnd) {
                            if (spanStart <= i) {
                                if (spanStart != i) {
                                    spannable.setSpan(createNewSpan(characterStyle, textStyleRun, null, z), spanStart, i, 33);
                                }
                                if (spanEnd > i) {
                                    if (textStyleSpan != null) {
                                        spannable.setSpan(createNewSpan(characterStyle, textStyleRun, textStyleRun2, z), i, Math.min(spanEnd, i2), 33);
                                    }
                                    i3 = spanEnd;
                                    if (spanEnd >= i2) {
                                        if (spanEnd != i2) {
                                            spannable.setSpan(createNewSpan(characterStyle, textStyleRun, null, z), i2, spanEnd, 33);
                                        }
                                        if (i2 > spanStart && spanEnd <= i) {
                                            if (textStyleSpan != null) {
                                                spannable.setSpan(createNewSpan(characterStyle, textStyleRun, textStyleRun2, z), spanStart, Math.min(spanEnd, i2), 33);
                                            }
                                            i2 = spanStart;
                                        }
                                    }
                                    i = i3;
                                }
                            }
                            i3 = i;
                            if (spanEnd >= i2) {
                            }
                            i = i3;
                        } else {
                            spannable.setSpan(createNewSpan(characterStyle, textStyleRun, textStyleRun2, z), spanStart, spanEnd, 33);
                            if (textStyleSpan != null) {
                                spannable.setSpan(new TextStyleSpan(new TextStyleSpan.TextStyleRun(textStyleRun2)), spanEnd, i2, 33);
                            }
                            i2 = spanStart;
                        }
                    }
                }
            }
            if (textStyleSpan == null || i >= i2 || i >= spannable.length()) {
                return;
            }
            spannable.setSpan(textStyleSpan, i, Math.min(spannable.length(), i2), 33);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void addTextStyleRuns(ArrayList<TLRPC.MessageEntity> arrayList, CharSequence charSequence, Spannable spannable) {
        addTextStyleRuns(arrayList, charSequence, spannable, -1);
    }

    public static void addTextStyleRuns(ArrayList<TLRPC.MessageEntity> arrayList, CharSequence charSequence, Spannable spannable, int i) {
        for (TextStyleSpan textStyleSpan : (TextStyleSpan[]) spannable.getSpans(0, spannable.length(), TextStyleSpan.class)) {
            spannable.removeSpan(textStyleSpan);
        }
        Iterator<TextStyleSpan.TextStyleRun> it = getTextStyleRuns(arrayList, charSequence, i).iterator();
        while (it.hasNext()) {
            TextStyleSpan.TextStyleRun next = it.next();
            addStyleToText(new TextStyleSpan(next), next.start, next.end, spannable, true);
        }
    }

    public static void addTextStyleRuns(MessageObject messageObject, Spannable spannable) {
        addTextStyleRuns(messageObject.messageOwner.entities, messageObject.messageText, spannable, -1);
    }

    public static void addTextStyleRuns(MessageObject messageObject, Spannable spannable, int i) {
        addTextStyleRuns(messageObject.messageOwner.entities, messageObject.messageText, spannable, i);
    }

    public static void addTextStyleRuns(TLRPC.DraftMessage draftMessage, Spannable spannable, int i) {
        addTextStyleRuns(draftMessage.entities, draftMessage.message, spannable, i);
    }

    private ArrayList<MessageObject> broadcastPinnedMessage(final ArrayList<TLRPC.Message> arrayList, final ArrayList<TLRPC.User> arrayList2, final ArrayList<TLRPC.Chat> arrayList3, final boolean z, boolean z2) {
        if (arrayList.isEmpty()) {
            return null;
        }
        final LongSparseArray longSparseArray = new LongSparseArray();
        for (int i = 0; i < arrayList2.size(); i++) {
            TLRPC.User user = arrayList2.get(i);
            longSparseArray.put(user.id, user);
        }
        final LongSparseArray longSparseArray2 = new LongSparseArray();
        for (int i2 = 0; i2 < arrayList3.size(); i2++) {
            TLRPC.Chat chat = arrayList3.get(i2);
            longSparseArray2.put(chat.id, chat);
        }
        final ArrayList<MessageObject> arrayList4 = new ArrayList<>();
        if (!z2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda93
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$broadcastPinnedMessage$169(arrayList2, z, arrayList3, arrayList, arrayList4, longSparseArray, longSparseArray2);
                }
            });
            return null;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda92
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$broadcastPinnedMessage$167(arrayList2, z, arrayList3);
            }
        });
        int size = arrayList.size();
        int i3 = 0;
        int i4 = 0;
        while (i4 < size) {
            TLRPC.Message message = arrayList.get(i4);
            if ((MessageObject.getMedia(message) instanceof TLRPC.TL_messageMediaDocument) || (MessageObject.getMedia(message) instanceof TLRPC.TL_messageMediaPhoto)) {
                i3++;
            }
            int i5 = i3;
            arrayList4.add(new MessageObject(this.currentAccount, message, longSparseArray, longSparseArray2, false, i5 < 30));
            i4++;
            i3 = i5;
        }
        return arrayList4;
    }

    private void broadcastReplyMessages(ArrayList<TLRPC.Message> arrayList, final LongSparseArray longSparseArray, final ArrayList<TLRPC.User> arrayList2, final ArrayList<TLRPC.Chat> arrayList3, final long j, final boolean z) {
        LongSparseArray longSparseArray2 = new LongSparseArray();
        for (int i = 0; i < arrayList2.size(); i++) {
            TLRPC.User user = arrayList2.get(i);
            longSparseArray2.put(user.id, user);
        }
        LongSparseArray longSparseArray3 = new LongSparseArray();
        for (int i2 = 0; i2 < arrayList3.size(); i2++) {
            TLRPC.Chat chat = arrayList3.get(i2);
            longSparseArray3.put(chat.id, chat);
        }
        final ArrayList arrayList4 = new ArrayList();
        int size = arrayList.size();
        for (int i3 = 0; i3 < size; i3++) {
            arrayList4.add(new MessageObject(this.currentAccount, arrayList.get(i3), longSparseArray2, longSparseArray3, false, false));
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda154
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$broadcastReplyMessages$179(arrayList2, z, arrayList3, arrayList4, longSparseArray, j);
            }
        });
    }

    public static long calcDocumentsHash(ArrayList<TLRPC.Document> arrayList) {
        return calcDocumentsHash(arrayList, NotificationCenter.storyQualityUpdate);
    }

    public static long calcDocumentsHash(ArrayList<TLRPC.Document> arrayList, int i) {
        long j = 0;
        if (arrayList == null) {
            return 0L;
        }
        int min = Math.min(i, arrayList.size());
        for (int i2 = 0; i2 < min; i2++) {
            TLRPC.Document document = arrayList.get(i2);
            if (document != null) {
                j = calcHash(j, document.id);
            }
        }
        return j;
    }

    private long calcFeaturedStickersHash(boolean z, ArrayList<TLRPC.StickerSetCovered> arrayList) {
        long j = 0;
        if (arrayList != null && !arrayList.isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC.StickerSet stickerSet = arrayList.get(i).set;
                if (!stickerSet.archived) {
                    j = calcHash(j, stickerSet.id);
                    if (this.unreadStickerSets[z ? 1 : 0].contains(Long.valueOf(stickerSet.id))) {
                        j = calcHash(j, 1L);
                    }
                }
            }
        }
        return j;
    }

    public static long calcHash(long j, long j2) {
        long j3 = j ^ (j >>> 21);
        long j4 = j3 ^ (j3 << 35);
        return (j4 ^ (j4 >>> 4)) + j2;
    }

    private static long calcStickersHash(ArrayList<TLRPC.TL_messages_stickerSet> arrayList) {
        long j = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) != null) {
                if (!arrayList.get(i).set.archived) {
                    j = calcHash(j, r3.hash);
                }
            }
        }
        return j;
    }

    public static boolean canAddMessageToMedia(TLRPC.Message message) {
        boolean z = message instanceof TLRPC.TL_message_secret;
        if (!z || (!((MessageObject.getMedia(message) instanceof TLRPC.TL_messageMediaPhoto) || MessageObject.isVideoMessage(message) || MessageObject.isGifMessage(message)) || MessageObject.getMedia(message).ttl_seconds == 0 || MessageObject.getMedia(message).ttl_seconds > 60)) {
            return (z || !(message instanceof TLRPC.TL_message) || (!((MessageObject.getMedia(message) instanceof TLRPC.TL_messageMediaPhoto) || (MessageObject.getMedia(message) instanceof TLRPC.TL_messageMediaDocument)) || MessageObject.getMedia(message).ttl_seconds == 0)) && getMediaType(message) != -1;
        }
        return false;
    }

    public static boolean canShowAttachMenuBot(TLRPC.TL_attachMenuBot tL_attachMenuBot, TLObject tLObject) {
        TLRPC.User user = tLObject instanceof TLRPC.User ? (TLRPC.User) tLObject : null;
        TLRPC.Chat chat = tLObject instanceof TLRPC.Chat ? (TLRPC.Chat) tLObject : null;
        Iterator<TLRPC.AttachMenuPeerType> it = tL_attachMenuBot.peer_types.iterator();
        while (it.hasNext()) {
            TLRPC.AttachMenuPeerType next = it.next();
            if ((next instanceof TLRPC.TL_attachMenuPeerTypeSameBotPM) && user != null && user.bot && user.id == tL_attachMenuBot.bot_id) {
                return true;
            }
            if ((next instanceof TLRPC.TL_attachMenuPeerTypeBotPM) && user != null && user.bot && user.id != tL_attachMenuBot.bot_id) {
                return true;
            }
            if ((next instanceof TLRPC.TL_attachMenuPeerTypePM) && user != null && !user.bot) {
                return true;
            }
            if ((next instanceof TLRPC.TL_attachMenuPeerTypeChat) && chat != null && !ChatObject.isChannelAndNotMegaGroup(chat)) {
                return true;
            }
            if ((next instanceof TLRPC.TL_attachMenuPeerTypeBroadcast) && chat != null && ChatObject.isChannelAndNotMegaGroup(chat)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canShowAttachMenuBotForTarget(TLRPC.TL_attachMenuBot tL_attachMenuBot, String str) {
        Iterator<TLRPC.AttachMenuPeerType> it = tL_attachMenuBot.peer_types.iterator();
        while (it.hasNext()) {
            TLRPC.AttachMenuPeerType next = it.next();
            if (((next instanceof TLRPC.TL_attachMenuPeerTypeSameBotPM) || (next instanceof TLRPC.TL_attachMenuPeerTypeBotPM)) && str.equals("bots")) {
                return true;
            }
            if ((next instanceof TLRPC.TL_attachMenuPeerTypeBroadcast) && str.equals("channels")) {
                return true;
            }
            if ((next instanceof TLRPC.TL_attachMenuPeerTypeChat) && str.equals("groups")) {
                return true;
            }
            if ((next instanceof TLRPC.TL_attachMenuPeerTypePM) && str.equals("users")) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0027 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0029 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static boolean checkInclusion(int i, List<TLRPC.MessageEntity> list, boolean z) {
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            for (int i2 = 0; i2 < size; i2++) {
                TLRPC.MessageEntity messageEntity = list.get(i2);
                int i3 = messageEntity.offset;
                if (z) {
                    if (i3 >= i) {
                        continue;
                    }
                    if (messageEntity.offset + messageEntity.length <= i) {
                        return true;
                    }
                } else {
                    if (i3 > i) {
                        continue;
                    }
                    if (messageEntity.offset + messageEntity.length <= i) {
                    }
                }
            }
        }
        return false;
    }

    private static boolean checkIntersection(int i, int i2, List<TLRPC.MessageEntity> list) {
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            for (int i3 = 0; i3 < size; i3++) {
                TLRPC.MessageEntity messageEntity = list.get(i3);
                int i4 = messageEntity.offset;
                if (i4 > i && i4 + messageEntity.length <= i2) {
                    return true;
                }
            }
        }
        return false;
    }

    private void cleanupStickerSetCache() {
        if (this.cleanedupStickerSetCache) {
            return;
        }
        this.cleanedupStickerSetCache = true;
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda244
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$cleanupStickerSetCache$39();
            }
        });
    }

    private Intent createIntrnalAttachedBotShortcutIntent(long j) {
        if (j != 0 && canCreateAttachedMenuBotShortcut(j)) {
            Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) OpenAttachedMenuBotReceiver.class);
            if (DialogObject.isUserDialog(j)) {
                intent.putExtra("botId", j);
                intent.putExtra("currentAccount", this.currentAccount);
                intent.setAction(OpenAttachedMenuBotReceiver.ACTION + j);
                intent.addFlags(ConnectionsManager.FileTypeFile);
                return intent;
            }
        }
        return null;
    }

    private Intent createIntrnalShortcutIntent(long j) {
        Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) OpenChatReceiver.class);
        if (DialogObject.isEncryptedDialog(j)) {
            int encryptedChatId = DialogObject.getEncryptedChatId(j);
            intent.putExtra("encId", encryptedChatId);
            if (getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChatId)) == null) {
                return null;
            }
        } else if (DialogObject.isUserDialog(j)) {
            intent.putExtra("userId", j);
        } else {
            if (!DialogObject.isChatDialog(j)) {
                return null;
            }
            intent.putExtra("chatId", -j);
        }
        intent.putExtra("currentAccount", this.currentAccount);
        intent.setAction("com.tmessages.openchat" + j);
        intent.addFlags(ConnectionsManager.FileTypeFile);
        return intent;
    }

    private static CharacterStyle createNewSpan(CharacterStyle characterStyle, TextStyleSpan.TextStyleRun textStyleRun, TextStyleSpan.TextStyleRun textStyleRun2, boolean z) {
        TextStyleSpan.TextStyleRun textStyleRun3 = new TextStyleSpan.TextStyleRun(textStyleRun);
        if (textStyleRun2 != null) {
            if (z) {
                textStyleRun3.merge(textStyleRun2);
            } else {
                textStyleRun3.replace(textStyleRun2);
            }
        }
        if (characterStyle instanceof TextStyleSpan) {
            return new TextStyleSpan(textStyleRun3);
        }
        if (characterStyle instanceof URLSpanReplacement) {
            return new URLSpanReplacement(((URLSpanReplacement) characterStyle).getURL(), textStyleRun3);
        }
        return null;
    }

    private void deletePeer(final long j, final int i) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda212
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$deletePeer$159(j, i);
            }
        });
    }

    public static boolean entitiesEqual(ArrayList<TLRPC.MessageEntity> arrayList, ArrayList<TLRPC.MessageEntity> arrayList2) {
        if (arrayList.size() != arrayList2.size()) {
            return false;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (!entitiesEqual(arrayList.get(i), arrayList2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean entitiesEqual(TLRPC.MessageEntity messageEntity, TLRPC.MessageEntity messageEntity2) {
        if (messageEntity.getClass() != messageEntity2.getClass() || messageEntity.offset != messageEntity2.offset || messageEntity.length != messageEntity2.length || !TextUtils.equals(messageEntity.url, messageEntity2.url) || !TextUtils.equals(messageEntity.language, messageEntity2.language)) {
            return false;
        }
        if ((messageEntity instanceof TLRPC.TL_inputMessageEntityMentionName) && ((TLRPC.TL_inputMessageEntityMentionName) messageEntity).user_id != ((TLRPC.TL_inputMessageEntityMentionName) messageEntity2).user_id) {
            return false;
        }
        if (!(messageEntity instanceof TLRPC.TL_messageEntityMentionName) || ((TLRPC.TL_messageEntityMentionName) messageEntity).user_id == ((TLRPC.TL_messageEntityMentionName) messageEntity2).user_id) {
            return !(messageEntity instanceof TLRPC.TL_messageEntityCustomEmoji) || ((TLRPC.TL_messageEntityCustomEmoji) messageEntity).document_id == ((TLRPC.TL_messageEntityCustomEmoji) messageEntity2).document_id;
        }
        return false;
    }

    private void fetchStickerSetInternal(TLRPC.InputStickerSet inputStickerSet, Utilities.Callback2<Boolean, TLRPC.TL_messages_stickerSet> callback2) {
        if (callback2 == null) {
            return;
        }
        final String inputSetKey = inputSetKey(inputStickerSet);
        ArrayList<Utilities.Callback2<Boolean, TLRPC.TL_messages_stickerSet>> arrayList = this.loadingStickerSets.get(inputSetKey);
        if (arrayList != null && arrayList.size() > 0) {
            arrayList.add(callback2);
            return;
        }
        if (arrayList == null) {
            HashMap<String, ArrayList<Utilities.Callback2<Boolean, TLRPC.TL_messages_stickerSet>>> hashMap = this.loadingStickerSets;
            ArrayList<Utilities.Callback2<Boolean, TLRPC.TL_messages_stickerSet>> arrayList2 = new ArrayList<>();
            hashMap.put(inputSetKey, arrayList2);
            arrayList = arrayList2;
        }
        arrayList.add(callback2);
        TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
        tL_messages_getStickerSet.stickerset = inputStickerSet;
        getConnectionsManager().sendRequest(tL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda109
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$fetchStickerSetInternal$42(inputSetKey, tLObject, tL_error);
            }
        });
    }

    public static TLRPC.TL_attachMenuBotIcon getAnimatedAttachMenuBotIcon(TLRPC.TL_attachMenuBot tL_attachMenuBot) {
        Iterator<TLRPC.TL_attachMenuBotIcon> it = tL_attachMenuBot.icons.iterator();
        while (it.hasNext()) {
            TLRPC.TL_attachMenuBotIcon next = it.next();
            if (next.name.equals(ATTACH_MENU_BOT_ANIMATED_ICON_KEY)) {
                return next;
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x006d A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TLRPC.TL_messages_stickerSet getCachedStickerSetInternal(long j, Integer num) {
        SQLiteCursor sQLiteCursor;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        NativeByteBuffer nativeByteBuffer = null;
        try {
        } catch (Throwable th) {
            th = th;
        }
        try {
            sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT data, hash FROM stickersets2 WHERE id = ? LIMIT 1", Long.valueOf(j));
            try {
                if (!sQLiteCursor.next() || sQLiteCursor.isNull(0)) {
                    tL_messages_stickerSet = null;
                } else {
                    NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
                    if (byteBufferValue != null) {
                        try {
                            tL_messages_stickerSet = TLRPC.messages_StickerSet.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                        } catch (Throwable th2) {
                            th = th2;
                            tL_messages_stickerSet = null;
                        }
                        try {
                            int intValue = sQLiteCursor.intValue(1);
                            if (num != null && num.intValue() != 0) {
                                if (num.intValue() != intValue) {
                                    byteBufferValue.reuse();
                                    sQLiteCursor.dispose();
                                    return null;
                                }
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            nativeByteBuffer = byteBufferValue;
                            try {
                                FileLog.e(th);
                            } finally {
                                if (nativeByteBuffer != null) {
                                    nativeByteBuffer.reuse();
                                }
                                if (sQLiteCursor != null) {
                                    sQLiteCursor.dispose();
                                }
                            }
                        }
                    } else {
                        tL_messages_stickerSet = null;
                    }
                    nativeByteBuffer = byteBufferValue;
                }
                if (nativeByteBuffer != null) {
                    nativeByteBuffer.reuse();
                }
            } catch (Throwable th4) {
                th = th4;
                tL_messages_stickerSet = null;
            }
        } catch (Throwable th5) {
            th = th5;
            sQLiteCursor = null;
            tL_messages_stickerSet = null;
            FileLog.e(th);
        }
        return tL_messages_stickerSet;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0069 A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TLRPC.TL_messages_stickerSet getCachedStickerSetInternal(String str, Integer num) {
        SQLiteCursor sQLiteCursor;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        NativeByteBuffer nativeByteBuffer = null;
        try {
            try {
                sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT data, hash FROM stickersets2 WHERE short_name = ? LIMIT 1", str);
                try {
                    if (!sQLiteCursor.next() || sQLiteCursor.isNull(0)) {
                        tL_messages_stickerSet = null;
                    } else {
                        NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
                        if (byteBufferValue != null) {
                            try {
                                tL_messages_stickerSet = TLRPC.messages_StickerSet.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                                try {
                                    int intValue = sQLiteCursor.intValue(1);
                                    if (num != null && num.intValue() != 0) {
                                        if (num.intValue() != intValue) {
                                            byteBufferValue.reuse();
                                            sQLiteCursor.dispose();
                                            return null;
                                        }
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    nativeByteBuffer = byteBufferValue;
                                    try {
                                        FileLog.e(th);
                                    } finally {
                                        if (nativeByteBuffer != null) {
                                            nativeByteBuffer.reuse();
                                        }
                                        if (sQLiteCursor != null) {
                                            sQLiteCursor.dispose();
                                        }
                                    }
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                tL_messages_stickerSet = null;
                            }
                        } else {
                            tL_messages_stickerSet = null;
                        }
                        nativeByteBuffer = byteBufferValue;
                    }
                    if (nativeByteBuffer != null) {
                        nativeByteBuffer.reuse();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    tL_messages_stickerSet = null;
                }
            } catch (Throwable th4) {
                th = th4;
                sQLiteCursor = null;
                tL_messages_stickerSet = null;
                FileLog.e(th);
            }
        } catch (Throwable th5) {
            th = th5;
        }
        return tL_messages_stickerSet;
    }

    public static TLRPC.InputStickerSet getInputStickerSet(TLRPC.Document document) {
        for (int i = 0; i < document.attributes.size(); i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                TLRPC.InputStickerSet inputStickerSet = documentAttribute.stickerset;
                if (inputStickerSet instanceof TLRPC.TL_inputStickerSetEmpty) {
                    return null;
                }
                return inputStickerSet;
            }
        }
        return null;
    }

    public static TLRPC.InputStickerSet getInputStickerSet(TLRPC.StickerSet stickerSet) {
        if (stickerSet == null) {
            return null;
        }
        TLRPC.TL_inputStickerSetID tL_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
        tL_inputStickerSetID.id = stickerSet.id;
        tL_inputStickerSetID.access_hash = stickerSet.access_hash;
        return tL_inputStickerSetID;
    }

    public static TLRPC.TL_inputStickerSetItem getInputStickerSetItem(TLRPC.Document document, String str) {
        TLRPC.TL_inputStickerSetItem tL_inputStickerSetItem = new TLRPC.TL_inputStickerSetItem();
        TLRPC.TL_inputDocument tL_inputDocument = new TLRPC.TL_inputDocument();
        tL_inputStickerSetItem.document = tL_inputDocument;
        tL_inputDocument.id = document.id;
        tL_inputDocument.access_hash = document.access_hash;
        tL_inputDocument.file_reference = document.file_reference;
        tL_inputStickerSetItem.emoji = str;
        return tL_inputStickerSetItem;
    }

    public static MediaDataController getInstance(int i) {
        MediaDataController mediaDataController = Instance[i];
        if (mediaDataController == null) {
            synchronized (lockObjects) {
                try {
                    mediaDataController = Instance[i];
                    if (mediaDataController == null) {
                        MediaDataController[] mediaDataControllerArr = Instance;
                        MediaDataController mediaDataController2 = new MediaDataController(i);
                        mediaDataControllerArr[i] = mediaDataController2;
                        mediaDataController = mediaDataController2;
                    }
                } finally {
                }
            }
        }
        return mediaDataController;
    }

    private void getMediaCountDatabase(final long j, final long j2, final int i, final int i2) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda185
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$getMediaCountDatabase$137(j2, j, i, i2);
            }
        });
    }

    public static int getMediaType(TLRPC.Message message) {
        if (message == null) {
            return -1;
        }
        if (MessageObject.getMedia(message) instanceof TLRPC.TL_messageMediaPhoto) {
            return 0;
        }
        if (!(MessageObject.getMedia(message) instanceof TLRPC.TL_messageMediaDocument)) {
            if (!message.entities.isEmpty()) {
                for (int i = 0; i < message.entities.size(); i++) {
                    TLRPC.MessageEntity messageEntity = message.entities.get(i);
                    if ((messageEntity instanceof TLRPC.TL_messageEntityUrl) || (messageEntity instanceof TLRPC.TL_messageEntityTextUrl) || (messageEntity instanceof TLRPC.TL_messageEntityEmail)) {
                        return 3;
                    }
                }
            }
            return -1;
        }
        TLRPC.Document document = MessageObject.getMedia(message).document;
        if (document == null) {
            return -1;
        }
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        boolean z6 = false;
        for (int i2 = 0; i2 < document.attributes.size(); i2++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i2);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeVideo) {
                z = documentAttribute.round_message;
                z3 = !z;
                z2 = z;
            } else if (documentAttribute instanceof TLRPC.TL_documentAttributeAnimated) {
                z4 = true;
            } else if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                z = documentAttribute.voice;
                z6 = !z;
            } else if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                z5 = true;
            }
        }
        if (z || z2) {
            return 2;
        }
        if (z3 && !z4 && !z5) {
            return 0;
        }
        if (z5) {
            return -1;
        }
        if (z4) {
            return 5;
        }
        return z6 ? 4 : 1;
    }

    public static TLRPC.TL_attachMenuBotIcon getPlaceholderStaticAttachMenuBotIcon(TLRPC.TL_attachMenuBot tL_attachMenuBot) {
        Iterator<TLRPC.TL_attachMenuBotIcon> it = tL_attachMenuBot.icons.iterator();
        while (it.hasNext()) {
            TLRPC.TL_attachMenuBotIcon next = it.next();
            if (next.name.equals(ATTACH_MENU_BOT_PLACEHOLDER_STATIC_KEY)) {
                return next;
            }
        }
        return null;
    }

    public static TLRPC.TL_attachMenuBotIcon getSideAttachMenuBotIcon(TLRPC.TL_attachMenuBot tL_attachMenuBot) {
        Iterator<TLRPC.TL_attachMenuBotIcon> it = tL_attachMenuBot.icons.iterator();
        while (it.hasNext()) {
            TLRPC.TL_attachMenuBotIcon next = it.next();
            if (next.name.equals("android_side_menu_static")) {
                return next;
            }
        }
        return null;
    }

    public static TLRPC.TL_attachMenuBotIcon getSideMenuBotIcon(TLRPC.TL_attachMenuBot tL_attachMenuBot) {
        Iterator<TLRPC.TL_attachMenuBotIcon> it = tL_attachMenuBot.icons.iterator();
        while (it.hasNext()) {
            TLRPC.TL_attachMenuBotIcon next = it.next();
            if (next.name.equals("android_side_menu_static")) {
                return next;
            }
        }
        return null;
    }

    public static TLRPC.TL_attachMenuBotIcon getStaticAttachMenuBotIcon(TLRPC.TL_attachMenuBot tL_attachMenuBot) {
        Iterator<TLRPC.TL_attachMenuBotIcon> it = tL_attachMenuBot.icons.iterator();
        while (it.hasNext()) {
            TLRPC.TL_attachMenuBotIcon next = it.next();
            if (next.name.equals(ATTACH_MENU_BOT_STATIC_ICON_KEY)) {
                return next;
            }
        }
        return null;
    }

    public static long getStickerSetId(TLRPC.Document document) {
        if (document == null) {
            return -1L;
        }
        for (int i = 0; i < document.attributes.size(); i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if ((documentAttribute instanceof TLRPC.TL_documentAttributeSticker) || (documentAttribute instanceof TLRPC.TL_documentAttributeCustomEmoji)) {
                TLRPC.InputStickerSet inputStickerSet = documentAttribute.stickerset;
                if (inputStickerSet instanceof TLRPC.TL_inputStickerSetID) {
                    return inputStickerSet.id;
                }
                return -1L;
            }
        }
        return -1L;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00b3  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0144  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0147 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ArrayList<TextStyleSpan.TextStyleRun> getTextStyleRuns(ArrayList<TLRPC.MessageEntity> arrayList, CharSequence charSequence, int i) {
        int i2;
        int i3;
        int size;
        int i4;
        TextStyleSpan.TextStyleRun textStyleRun;
        ArrayList<TextStyleSpan.TextStyleRun> arrayList2 = new ArrayList<>();
        ArrayList arrayList3 = new ArrayList(arrayList);
        Collections.sort(arrayList3, new Comparator() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda36
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$getTextStyleRuns$180;
                lambda$getTextStyleRuns$180 = MediaDataController.lambda$getTextStyleRuns$180((TLRPC.MessageEntity) obj, (TLRPC.MessageEntity) obj2);
                return lambda$getTextStyleRuns$180;
            }
        });
        int size2 = arrayList3.size();
        for (int i5 = 0; i5 < size2; i5++) {
            TLRPC.MessageEntity messageEntity = (TLRPC.MessageEntity) arrayList3.get(i5);
            if (messageEntity != null && messageEntity.length > 0 && (i2 = messageEntity.offset) >= 0 && i2 < charSequence.length()) {
                if (messageEntity.offset + messageEntity.length > charSequence.length()) {
                    messageEntity.length = charSequence.length() - messageEntity.offset;
                }
                if (!(messageEntity instanceof TLRPC.TL_messageEntityCustomEmoji)) {
                    TextStyleSpan.TextStyleRun textStyleRun2 = new TextStyleSpan.TextStyleRun();
                    int i6 = messageEntity.offset;
                    textStyleRun2.start = i6;
                    textStyleRun2.end = i6 + messageEntity.length;
                    if (messageEntity instanceof TLRPC.TL_messageEntitySpoiler) {
                        i3 = 256;
                    } else if (messageEntity instanceof TLRPC.TL_messageEntityStrike) {
                        i3 = 8;
                    } else if (messageEntity instanceof TLRPC.TL_messageEntityUnderline) {
                        i3 = 16;
                    } else {
                        if (messageEntity instanceof TLRPC.TL_messageEntityBold) {
                            textStyleRun2.flags = 1;
                        } else if (messageEntity instanceof TLRPC.TL_messageEntityItalic) {
                            i3 = 2;
                        } else if ((messageEntity instanceof TLRPC.TL_messageEntityCode) || (messageEntity instanceof TLRPC.TL_messageEntityPre)) {
                            i3 = 4;
                        } else {
                            textStyleRun2.flags = ((messageEntity instanceof TLRPC.TL_messageEntityMentionName) || (messageEntity instanceof TLRPC.TL_inputMessageEntityMentionName)) ? 64 : 128;
                            textStyleRun2.urlEntity = messageEntity;
                        }
                        if (messageEntity instanceof TLRPC.TL_messageEntityTextUrl) {
                            textStyleRun2.flags |= 1024;
                        }
                        textStyleRun2.flags &= i;
                        size = arrayList2.size();
                        i4 = 0;
                        while (i4 < size) {
                            TextStyleSpan.TextStyleRun textStyleRun3 = arrayList2.get(i4);
                            int i7 = textStyleRun2.start;
                            int i8 = textStyleRun3.start;
                            if (i7 > i8) {
                                int i9 = textStyleRun3.end;
                                if (i7 < i9) {
                                    if (textStyleRun2.end < i9) {
                                        TextStyleSpan.TextStyleRun textStyleRun4 = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                        textStyleRun4.merge(textStyleRun3);
                                        arrayList2.add(i4 + 1, textStyleRun4);
                                        textStyleRun = new TextStyleSpan.TextStyleRun(textStyleRun3);
                                        textStyleRun.start = textStyleRun2.end;
                                        i4 += 2;
                                        size += 2;
                                    } else {
                                        textStyleRun = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                        textStyleRun.merge(textStyleRun3);
                                        textStyleRun.end = textStyleRun3.end;
                                        i4++;
                                        size++;
                                    }
                                    arrayList2.add(i4, textStyleRun);
                                    int i10 = textStyleRun2.start;
                                    textStyleRun2.start = textStyleRun3.end;
                                    textStyleRun3.end = i10;
                                }
                            } else {
                                int i11 = textStyleRun2.end;
                                if (i8 < i11) {
                                    int i12 = textStyleRun3.end;
                                    if (i11 != i12) {
                                        if (i11 < i12) {
                                            TextStyleSpan.TextStyleRun textStyleRun5 = new TextStyleSpan.TextStyleRun(textStyleRun3);
                                            textStyleRun5.merge(textStyleRun2);
                                            textStyleRun5.end = textStyleRun2.end;
                                            i4++;
                                            size++;
                                            arrayList2.add(i4, textStyleRun5);
                                            textStyleRun3.start = textStyleRun2.end;
                                            textStyleRun2.end = i8;
                                        } else {
                                            TextStyleSpan.TextStyleRun textStyleRun6 = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                            textStyleRun6.start = textStyleRun3.end;
                                            i4++;
                                            size++;
                                            arrayList2.add(i4, textStyleRun6);
                                        }
                                    }
                                    textStyleRun3.merge(textStyleRun2);
                                    textStyleRun2.end = i8;
                                }
                            }
                            i4++;
                        }
                        if (textStyleRun2.start >= textStyleRun2.end) {
                            arrayList2.add(textStyleRun2);
                        }
                    }
                    textStyleRun2.flags = i3;
                    if (messageEntity instanceof TLRPC.TL_messageEntityTextUrl) {
                    }
                    textStyleRun2.flags &= i;
                    size = arrayList2.size();
                    i4 = 0;
                    while (i4 < size) {
                    }
                    if (textStyleRun2.start >= textStyleRun2.end) {
                    }
                }
            }
        }
        return arrayList2;
    }

    public static String inputSetKey(TLRPC.InputStickerSet inputStickerSet) {
        StringBuilder sb;
        String str;
        if (inputStickerSet instanceof TLRPC.TL_inputStickerSetID) {
            sb = new StringBuilder();
            sb.append("id");
            sb.append(inputStickerSet.id);
            sb.append("access_hash");
            sb.append(inputStickerSet.access_hash);
        } else {
            if (inputStickerSet instanceof TLRPC.TL_inputStickerSetShortName) {
                sb = new StringBuilder();
                sb.append("short");
                str = inputStickerSet.short_name;
            } else {
                if (inputStickerSet instanceof TLRPC.TL_inputStickerSetEmpty) {
                    return "empty";
                }
                if (inputStickerSet instanceof TLRPC.TL_inputStickerSetAnimatedEmoji) {
                    return "animatedEmoji";
                }
                if (inputStickerSet instanceof TLRPC.TL_inputStickerSetEmojiGenericAnimations) {
                    return "emojiGenericAnimations";
                }
                if (inputStickerSet instanceof TLRPC.TL_inputStickerSetEmojiChannelDefaultStatuses) {
                    return "emojiChannelDefaultStatuses";
                }
                if (!(inputStickerSet instanceof TLRPC.TL_inputStickerSetDice)) {
                    return inputStickerSet instanceof TLRPC.TL_inputStickerSetPremiumGifts ? "premiumGifts" : inputStickerSet instanceof TLRPC.TL_inputStickerSetEmojiDefaultTopicIcons ? "defaultTopicIcons" : inputStickerSet instanceof TLRPC.TL_inputStickerSetEmojiDefaultStatuses ? "emojiDefaultStatuses" : "null";
                }
                sb = new StringBuilder();
                sb.append("dice");
                str = ((TLRPC.TL_inputStickerSetDice) inputStickerSet).emoticon;
            }
            sb.append(str);
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addRecentGif$26(TLRPC.Document document) {
        try {
            getMessagesStorage().getDatabase().executeFast("DELETE FROM web_recent_v3 WHERE id = '" + document.id + "' AND type = 2").stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$addRecentGif$27(TLRPC.Document document) {
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 0, document, 7);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addRecentSticker$20() {
        getMediaDataController().loadRecents(2, false, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addRecentSticker$21(Object obj, TLRPC.TL_messages_faveSticker tL_messages_faveSticker, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null || !FileRefController.isFileRefError(tL_error.text) || obj == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda162
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$addRecentSticker$20();
                }
            });
        } else {
            getFileRefController().requestReference(obj, tL_messages_faveSticker);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addRecentSticker$22(Object obj, TLRPC.TL_messages_saveRecentSticker tL_messages_saveRecentSticker, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null || !FileRefController.isFileRefError(tL_error.text) || obj == null) {
            return;
        }
        getFileRefController().requestReference(obj, tL_messages_saveRecentSticker);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addRecentSticker$23(int i, TLRPC.Document document) {
        int i2 = i == 0 ? 3 : i == 1 ? 4 : i == 5 ? 7 : 5;
        try {
            getMessagesStorage().getDatabase().executeFast("DELETE FROM web_recent_v3 WHERE id = '" + document.id + "' AND type = " + i2).stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$broadcastPinnedMessage$167(ArrayList arrayList, boolean z, ArrayList arrayList2) {
        getMessagesController().putUsers(arrayList, z);
        getMessagesController().putChats(arrayList2, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$broadcastPinnedMessage$168(ArrayList arrayList) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didLoadPinnedMessages, Long.valueOf(((MessageObject) arrayList.get(0)).getDialogId()), null, Boolean.TRUE, arrayList, null, 0, -1, Boolean.FALSE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$broadcastPinnedMessage$169(ArrayList arrayList, boolean z, ArrayList arrayList2, ArrayList arrayList3, final ArrayList arrayList4, LongSparseArray longSparseArray, LongSparseArray longSparseArray2) {
        getMessagesController().putUsers(arrayList, z);
        getMessagesController().putChats(arrayList2, z);
        int size = arrayList3.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            TLRPC.Message message = (TLRPC.Message) arrayList3.get(i2);
            if ((MessageObject.getMedia(message) instanceof TLRPC.TL_messageMediaDocument) || (MessageObject.getMedia(message) instanceof TLRPC.TL_messageMediaPhoto)) {
                i++;
            }
            arrayList4.add(new MessageObject(this.currentAccount, message, longSparseArray, longSparseArray2, false, i < 30));
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda84
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$broadcastPinnedMessage$168(arrayList4);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$broadcastReplyMessages$179(ArrayList arrayList, boolean z, ArrayList arrayList2, ArrayList arrayList3, LongSparseArray longSparseArray, long j) {
        ArrayList arrayList4;
        getMessagesController().putUsers(arrayList, z);
        getMessagesController().putChats(arrayList2, z);
        int size = arrayList3.size();
        boolean z2 = false;
        for (int i = 0; i < size; i++) {
            MessageObject messageObject = (MessageObject) arrayList3.get(i);
            SparseArray sparseArray = (SparseArray) longSparseArray.get(messageObject.getDialogId());
            if (sparseArray != null && (arrayList4 = (ArrayList) sparseArray.get(messageObject.getId())) != null) {
                for (int i2 = 0; i2 < arrayList4.size(); i2++) {
                    MessageObject messageObject2 = (MessageObject) arrayList4.get(i2);
                    messageObject2.replyMessageObject = messageObject;
                    messageObject2.applyTimestampsHighlightForReplyMsg();
                    TLRPC.MessageAction messageAction = messageObject2.messageOwner.action;
                    if (messageAction instanceof TLRPC.TL_messageActionPinMessage) {
                        messageObject2.generatePinMessageText(null, null);
                    } else if (messageAction instanceof TLRPC.TL_messageActionGameScore) {
                        messageObject2.generateGameMessageText(null);
                    } else if (messageAction instanceof TLRPC.TL_messageActionPaymentSent) {
                        messageObject2.generatePaymentSentMessageText(null);
                    }
                }
                z2 = true;
            }
        }
        if (z2) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.replyMessagesDidLoad, Long.valueOf(j), arrayList3, longSparseArray);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0276  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x028d  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x02ac A[Catch: all -> 0x02ef, TryCatch #3 {all -> 0x02ef, blocks: (B:3:0x0002, B:5:0x0007, B:6:0x0028, B:9:0x003d, B:10:0x00b5, B:12:0x00fb, B:13:0x0118, B:14:0x0123, B:16:0x0129, B:18:0x0144, B:20:0x016e, B:23:0x017a, B:25:0x0186, B:26:0x019b, B:29:0x0278, B:32:0x028f, B:34:0x02ac, B:36:0x02b1, B:37:0x02be, B:39:0x02c3, B:42:0x02cd, B:44:0x02da, B:45:0x02e5, B:47:0x02e0, B:48:0x02b6, B:67:0x0272, B:73:0x018b, B:75:0x0191, B:80:0x0158, B:85:0x0101, B:87:0x010a, B:88:0x0115, B:89:0x0110, B:90:0x0044, B:92:0x004c, B:94:0x0052, B:95:0x0056, B:97:0x005c, B:100:0x007e, B:102:0x0084, B:104:0x0094, B:106:0x0097, B:109:0x009d, B:111:0x00a3, B:114:0x00aa, B:116:0x00b0), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x02b1 A[Catch: all -> 0x02ef, TryCatch #3 {all -> 0x02ef, blocks: (B:3:0x0002, B:5:0x0007, B:6:0x0028, B:9:0x003d, B:10:0x00b5, B:12:0x00fb, B:13:0x0118, B:14:0x0123, B:16:0x0129, B:18:0x0144, B:20:0x016e, B:23:0x017a, B:25:0x0186, B:26:0x019b, B:29:0x0278, B:32:0x028f, B:34:0x02ac, B:36:0x02b1, B:37:0x02be, B:39:0x02c3, B:42:0x02cd, B:44:0x02da, B:45:0x02e5, B:47:0x02e0, B:48:0x02b6, B:67:0x0272, B:73:0x018b, B:75:0x0191, B:80:0x0158, B:85:0x0101, B:87:0x010a, B:88:0x0115, B:89:0x0110, B:90:0x0044, B:92:0x004c, B:94:0x0052, B:95:0x0056, B:97:0x005c, B:100:0x007e, B:102:0x0084, B:104:0x0094, B:106:0x0097, B:109:0x009d, B:111:0x00a3, B:114:0x00aa, B:116:0x00b0), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x02c3 A[Catch: all -> 0x02ef, TryCatch #3 {all -> 0x02ef, blocks: (B:3:0x0002, B:5:0x0007, B:6:0x0028, B:9:0x003d, B:10:0x00b5, B:12:0x00fb, B:13:0x0118, B:14:0x0123, B:16:0x0129, B:18:0x0144, B:20:0x016e, B:23:0x017a, B:25:0x0186, B:26:0x019b, B:29:0x0278, B:32:0x028f, B:34:0x02ac, B:36:0x02b1, B:37:0x02be, B:39:0x02c3, B:42:0x02cd, B:44:0x02da, B:45:0x02e5, B:47:0x02e0, B:48:0x02b6, B:67:0x0272, B:73:0x018b, B:75:0x0191, B:80:0x0158, B:85:0x0101, B:87:0x010a, B:88:0x0115, B:89:0x0110, B:90:0x0044, B:92:0x004c, B:94:0x0052, B:95:0x0056, B:97:0x005c, B:100:0x007e, B:102:0x0084, B:104:0x0094, B:106:0x0097, B:109:0x009d, B:111:0x00a3, B:114:0x00aa, B:116:0x00b0), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x02cd A[Catch: all -> 0x02ef, TryCatch #3 {all -> 0x02ef, blocks: (B:3:0x0002, B:5:0x0007, B:6:0x0028, B:9:0x003d, B:10:0x00b5, B:12:0x00fb, B:13:0x0118, B:14:0x0123, B:16:0x0129, B:18:0x0144, B:20:0x016e, B:23:0x017a, B:25:0x0186, B:26:0x019b, B:29:0x0278, B:32:0x028f, B:34:0x02ac, B:36:0x02b1, B:37:0x02be, B:39:0x02c3, B:42:0x02cd, B:44:0x02da, B:45:0x02e5, B:47:0x02e0, B:48:0x02b6, B:67:0x0272, B:73:0x018b, B:75:0x0191, B:80:0x0158, B:85:0x0101, B:87:0x010a, B:88:0x0115, B:89:0x0110, B:90:0x0044, B:92:0x004c, B:94:0x0052, B:95:0x0056, B:97:0x005c, B:100:0x007e, B:102:0x0084, B:104:0x0094, B:106:0x0097, B:109:0x009d, B:111:0x00a3, B:114:0x00aa, B:116:0x00b0), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x02b6 A[Catch: all -> 0x02ef, TryCatch #3 {all -> 0x02ef, blocks: (B:3:0x0002, B:5:0x0007, B:6:0x0028, B:9:0x003d, B:10:0x00b5, B:12:0x00fb, B:13:0x0118, B:14:0x0123, B:16:0x0129, B:18:0x0144, B:20:0x016e, B:23:0x017a, B:25:0x0186, B:26:0x019b, B:29:0x0278, B:32:0x028f, B:34:0x02ac, B:36:0x02b1, B:37:0x02be, B:39:0x02c3, B:42:0x02cd, B:44:0x02da, B:45:0x02e5, B:47:0x02e0, B:48:0x02b6, B:67:0x0272, B:73:0x018b, B:75:0x0191, B:80:0x0158, B:85:0x0101, B:87:0x010a, B:88:0x0115, B:89:0x0110, B:90:0x0044, B:92:0x004c, B:94:0x0052, B:95:0x0056, B:97:0x005c, B:100:0x007e, B:102:0x0084, B:104:0x0094, B:106:0x0097, B:109:0x009d, B:111:0x00a3, B:114:0x00aa, B:116:0x00b0), top: B:2:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x01cb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$buildShortcuts$141(boolean z, ArrayList arrayList) {
        TLRPC.Chat chat;
        TLRPC.User user;
        String str;
        TLRPC.FileLocation fileLocation;
        Bitmap bitmap;
        Bitmap bitmap2;
        ArrayList arrayList2 = arrayList;
        try {
            int i = 0;
            if (SharedConfig.directShareHash == null) {
                SharedConfig.directShareHash = UUID.randomUUID().toString();
                ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putString("directShareHash2", SharedConfig.directShareHash).commit();
            }
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            ArrayList arrayList5 = new ArrayList();
            if (z) {
                ShortcutManagerCompat.removeAllDynamicShortcuts(ApplicationLoader.applicationContext);
            } else {
                List dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(ApplicationLoader.applicationContext);
                if (dynamicShortcuts != null && !dynamicShortcuts.isEmpty()) {
                    arrayList4.add("compose");
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        arrayList4.add("did3_" + MessageObject.getPeerId(((TLRPC.TL_topPeer) arrayList2.get(i2)).peer));
                    }
                    for (int i3 = 0; i3 < dynamicShortcuts.size(); i3++) {
                        String id = ((ShortcutInfoCompat) dynamicShortcuts.get(i3)).getId();
                        if (!arrayList4.remove(id)) {
                            arrayList5.add(id);
                        }
                        arrayList3.add(id);
                    }
                    if (arrayList4.isEmpty() && arrayList5.isEmpty()) {
                        return;
                    }
                }
                if (!arrayList5.isEmpty()) {
                    ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList5);
                }
            }
            Intent intent = new Intent(ApplicationLoader.applicationContext, (Class<?>) LaunchActivity.class);
            intent.setAction("new_dialog");
            ArrayList arrayList6 = new ArrayList();
            ShortcutInfoCompat.Builder builder = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, "compose");
            int i4 = R.string.NewConversationShortcut;
            ShortcutInfoCompat build = builder.setShortLabel(LocaleController.getString(i4)).setLongLabel(LocaleController.getString(i4)).setIcon(IconCompat.createWithResource(ApplicationLoader.applicationContext, R.drawable.shortcut_compose)).setRank(0).setIntent(intent).build();
            if (z) {
                ShortcutManagerCompat.pushDynamicShortcut(ApplicationLoader.applicationContext, build);
            } else {
                arrayList6.add(build);
                if (arrayList3.contains("compose")) {
                    ShortcutManagerCompat.updateShortcuts(ApplicationLoader.applicationContext, arrayList6);
                } else {
                    ShortcutManagerCompat.addDynamicShortcuts(ApplicationLoader.applicationContext, arrayList6);
                }
                arrayList6.clear();
            }
            boolean z2 = true;
            HashSet hashSet = new HashSet(1);
            hashSet.add(SHORTCUT_CATEGORY);
            while (i < arrayList.size()) {
                Intent intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) OpenChatReceiver.class);
                long peerId = MessageObject.getPeerId(((TLRPC.TL_topPeer) arrayList2.get(i)).peer);
                if (DialogObject.isUserDialog(peerId)) {
                    intent2.putExtra("userId", peerId);
                    user = getMessagesController().getUser(Long.valueOf(peerId));
                    chat = null;
                } else {
                    long j = -peerId;
                    TLRPC.Chat chat2 = getMessagesController().getChat(Long.valueOf(j));
                    intent2.putExtra("chatId", j);
                    chat = chat2;
                    user = null;
                }
                if ((user != null && !UserObject.isDeleted(user)) || chat != null) {
                    if (user != null) {
                        str = ContactsController.formatName(user.first_name, user.last_name);
                        TLRPC.UserProfilePhoto userProfilePhoto = user.photo;
                        if (userProfilePhoto != null) {
                            fileLocation = userProfilePhoto.photo_small;
                            intent2.putExtra("currentAccount", this.currentAccount);
                            intent2.setAction("com.tmessages.openchat" + peerId);
                            intent2.putExtra("dialogId", peerId);
                            intent2.putExtra("hash", SharedConfig.directShareHash);
                            intent2.addFlags(ConnectionsManager.FileTypeFile);
                            if (fileLocation == null) {
                                try {
                                    Bitmap decodeFile = BitmapFactory.decodeFile(getFileLoader().getPathToAttach(fileLocation, z2).toString());
                                    if (decodeFile != null) {
                                        try {
                                            int dp = AndroidUtilities.dp(48.0f);
                                            Bitmap createBitmap = Bitmap.createBitmap(dp, dp, Bitmap.Config.ARGB_8888);
                                            Canvas canvas = new Canvas(createBitmap);
                                            if (roundPaint == null) {
                                                roundPaint = new Paint(3);
                                                bitmapRect = new RectF();
                                                Paint paint = new Paint(1);
                                                erasePaint = paint;
                                                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                                                Path path = new Path();
                                                roundPath = path;
                                                bitmap2 = createBitmap;
                                                path.addCircle(dp / 2, dp / 2, (dp / 2) - AndroidUtilities.dp(2.0f), Path.Direction.CW);
                                                roundPath.toggleInverseFillType();
                                            } else {
                                                bitmap2 = createBitmap;
                                            }
                                            bitmapRect.set(AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(46.0f), AndroidUtilities.dp(46.0f));
                                            canvas.drawBitmap(decodeFile, (Rect) null, bitmapRect, roundPaint);
                                            canvas.drawPath(roundPath, erasePaint);
                                            try {
                                                canvas.setBitmap(null);
                                            } catch (Exception unused) {
                                            }
                                            bitmap = bitmap2;
                                        } catch (Throwable th) {
                                            th = th;
                                            bitmap = decodeFile;
                                            FileLog.e(th);
                                            String str2 = "did3_" + peerId;
                                            if (TextUtils.isEmpty(str)) {
                                            }
                                            ShortcutInfoCompat.Builder intent3 = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str2).setShortLabel(str).setLongLabel(str).setRank(i + 1).setIntent(intent2);
                                            if (SharedConfig.directShare) {
                                            }
                                            intent3.setIcon(bitmap != null ? IconCompat.createWithBitmap(bitmap) : IconCompat.createWithResource(ApplicationLoader.applicationContext, R.drawable.shortcut_user));
                                            if (z) {
                                            }
                                            i++;
                                            arrayList2 = arrayList;
                                            z2 = true;
                                        }
                                    } else {
                                        bitmap = decodeFile;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    bitmap = null;
                                }
                            } else {
                                bitmap = null;
                            }
                            String str22 = "did3_" + peerId;
                            if (TextUtils.isEmpty(str)) {
                                str = " ";
                            }
                            ShortcutInfoCompat.Builder intent32 = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str22).setShortLabel(str).setLongLabel(str).setRank(i + 1).setIntent(intent2);
                            if (SharedConfig.directShare) {
                                intent32.setCategories(hashSet);
                            }
                            intent32.setIcon(bitmap != null ? IconCompat.createWithBitmap(bitmap) : IconCompat.createWithResource(ApplicationLoader.applicationContext, R.drawable.shortcut_user));
                            if (z) {
                                ShortcutManagerCompat.pushDynamicShortcut(ApplicationLoader.applicationContext, intent32.build());
                            } else {
                                arrayList6.add(intent32.build());
                                if (arrayList3.contains(str22)) {
                                    ShortcutManagerCompat.updateShortcuts(ApplicationLoader.applicationContext, arrayList6);
                                } else {
                                    ShortcutManagerCompat.addDynamicShortcuts(ApplicationLoader.applicationContext, arrayList6);
                                }
                                arrayList6.clear();
                            }
                        }
                        fileLocation = null;
                        intent2.putExtra("currentAccount", this.currentAccount);
                        intent2.setAction("com.tmessages.openchat" + peerId);
                        intent2.putExtra("dialogId", peerId);
                        intent2.putExtra("hash", SharedConfig.directShareHash);
                        intent2.addFlags(ConnectionsManager.FileTypeFile);
                        if (fileLocation == null) {
                        }
                        String str222 = "did3_" + peerId;
                        if (TextUtils.isEmpty(str)) {
                        }
                        ShortcutInfoCompat.Builder intent322 = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str222).setShortLabel(str).setLongLabel(str).setRank(i + 1).setIntent(intent2);
                        if (SharedConfig.directShare) {
                        }
                        intent322.setIcon(bitmap != null ? IconCompat.createWithBitmap(bitmap) : IconCompat.createWithResource(ApplicationLoader.applicationContext, R.drawable.shortcut_user));
                        if (z) {
                        }
                    } else {
                        String str3 = chat.title;
                        TLRPC.ChatPhoto chatPhoto = chat.photo;
                        if (chatPhoto != null) {
                            TLRPC.FileLocation fileLocation2 = chatPhoto.photo_small;
                            str = str3;
                            fileLocation = fileLocation2;
                            intent2.putExtra("currentAccount", this.currentAccount);
                            intent2.setAction("com.tmessages.openchat" + peerId);
                            intent2.putExtra("dialogId", peerId);
                            intent2.putExtra("hash", SharedConfig.directShareHash);
                            intent2.addFlags(ConnectionsManager.FileTypeFile);
                            if (fileLocation == null) {
                            }
                            String str2222 = "did3_" + peerId;
                            if (TextUtils.isEmpty(str)) {
                            }
                            ShortcutInfoCompat.Builder intent3222 = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str2222).setShortLabel(str).setLongLabel(str).setRank(i + 1).setIntent(intent2);
                            if (SharedConfig.directShare) {
                            }
                            intent3222.setIcon(bitmap != null ? IconCompat.createWithBitmap(bitmap) : IconCompat.createWithResource(ApplicationLoader.applicationContext, R.drawable.shortcut_user));
                            if (z) {
                            }
                        } else {
                            str = str3;
                            fileLocation = null;
                            intent2.putExtra("currentAccount", this.currentAccount);
                            intent2.setAction("com.tmessages.openchat" + peerId);
                            intent2.putExtra("dialogId", peerId);
                            intent2.putExtra("hash", SharedConfig.directShareHash);
                            intent2.addFlags(ConnectionsManager.FileTypeFile);
                            if (fileLocation == null) {
                            }
                            String str22222 = "did3_" + peerId;
                            if (TextUtils.isEmpty(str)) {
                            }
                            ShortcutInfoCompat.Builder intent32222 = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str22222).setShortLabel(str).setLongLabel(str).setRank(i + 1).setIntent(intent2);
                            if (SharedConfig.directShare) {
                            }
                            intent32222.setIcon(bitmap != null ? IconCompat.createWithBitmap(bitmap) : IconCompat.createWithResource(ApplicationLoader.applicationContext, R.drawable.shortcut_user));
                            if (z) {
                            }
                        }
                    }
                }
                i++;
                arrayList2 = arrayList;
                z2 = true;
            }
        } catch (Throwable unused2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDefaultTopicIcons$79(TLObject tLObject) {
        if (tLObject instanceof TLRPC.TL_messages_stickerSet) {
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject;
            getUserConfig().defaultTopicIcons = tL_messages_stickerSet.set.short_name;
            getUserConfig().lastUpdatedDefaultTopicIcons = System.currentTimeMillis();
            getUserConfig().saveConfig(false);
            processLoadedDiceStickers(getUserConfig().defaultTopicIcons, false, tL_messages_stickerSet, false, (int) (System.currentTimeMillis() / 1000));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkDefaultTopicIcons$80(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda131
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$checkDefaultTopicIcons$79(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkGenericAnimations$77(TLObject tLObject) {
        if (tLObject instanceof TLRPC.TL_messages_stickerSet) {
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject;
            getUserConfig().genericAnimationsStickerPack = tL_messages_stickerSet.set.short_name;
            getUserConfig().lastUpdatedGenericAnimations = System.currentTimeMillis();
            getUserConfig().saveConfig(false);
            processLoadedDiceStickers(getUserConfig().genericAnimationsStickerPack, false, tL_messages_stickerSet, false, (int) (System.currentTimeMillis() / 1000));
            for (int i = 0; i < tL_messages_stickerSet.documents.size(); i++) {
                if (this.currentAccount == UserConfig.selectedAccount) {
                    preloadImage(ImageLocation.getForDocument(tL_messages_stickerSet.documents.get(i)), 0);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkGenericAnimations$78(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$checkGenericAnimations$77(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPremiumGiftStickers$75(TLObject tLObject) {
        if (tLObject instanceof TLRPC.TL_messages_stickerSet) {
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject;
            getUserConfig().premiumGiftsStickerPack = tL_messages_stickerSet.set.short_name;
            getUserConfig().lastUpdatedPremiumGiftsStickerPack = System.currentTimeMillis();
            getUserConfig().saveConfig(false);
            processLoadedDiceStickers(getUserConfig().premiumGiftsStickerPack, false, tL_messages_stickerSet, false, (int) (System.currentTimeMillis() / 1000));
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didUpdatePremiumGiftStickers, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPremiumGiftStickers$76(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda126
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$checkPremiumGiftStickers$75(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$cleanup$1() {
        try {
            ShortcutManagerCompat.removeAllDynamicShortcuts(ApplicationLoader.applicationContext);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanup$2() {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadHints, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadInlineHints, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadWebappsHints, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cleanupStickerSetCache$39() {
        try {
            long currentTimeMillis = System.currentTimeMillis() - 604800000;
            getMessagesStorage().getDatabase().executeFast("DELETE FROM stickersets2 WHERE date < " + currentTimeMillis).stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearBotKeyboard$193(ArrayList arrayList, MessagesStorage.TopicKey topicKey) {
        if (arrayList == null) {
            if (topicKey != null) {
                this.botKeyboards.remove(topicKey);
                this.botDialogKeyboards.remove(topicKey.dialogId);
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.botKeyboardDidLoad, null, topicKey);
                return;
            }
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            int intValue = ((Integer) arrayList.get(i)).intValue();
            long j = intValue;
            MessagesStorage.TopicKey topicKey2 = (MessagesStorage.TopicKey) this.botKeyboardsByMids.get(j);
            if (topicKey2 != null) {
                this.botKeyboards.remove(topicKey2);
                ArrayList arrayList2 = (ArrayList) this.botDialogKeyboards.get(topicKey2.dialogId);
                if (arrayList2 != null) {
                    int i2 = 0;
                    while (i2 < arrayList2.size()) {
                        TLRPC.Message message = (TLRPC.Message) arrayList2.get(i2);
                        if (message == null || message.id == intValue) {
                            arrayList2.remove(i2);
                            i2--;
                        }
                        i2++;
                    }
                    if (arrayList2.isEmpty()) {
                        this.botDialogKeyboards.remove(topicKey2.dialogId);
                    }
                }
                this.botKeyboardsByMids.remove(j);
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.botKeyboardDidLoad, null, topicKey2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearBotKeyboard$194(long j) {
        ArrayList arrayList = (ArrayList) this.botDialogKeyboards.get(j);
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC.Message message = (TLRPC.Message) arrayList.get(i);
                int i2 = this.currentAccount;
                MessagesStorage.TopicKey of = MessagesStorage.TopicKey.of(j, MessageObject.getTopicId(i2, message, ChatObject.isForum(i2, j)));
                this.botKeyboards.remove(of);
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.botKeyboardDidLoad, null, of);
            }
        }
        this.botDialogKeyboards.remove(j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearRecentEmojiStatuses$229() {
        try {
            getMessagesStorage().getDatabase().executeFast("DELETE FROM emoji_statuses WHERE type = 0").stepThis().dispose();
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearRecentStickers$17() {
        try {
            getMessagesStorage().getDatabase().executeFast("DELETE FROM web_recent_v3 WHERE type = 3").stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearRecentStickers$18(TLObject tLObject) {
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda189
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$clearRecentStickers$17();
                }
            });
            this.recentStickers[0].clear();
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.recentDocumentsDidLoad, Boolean.FALSE, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearRecentStickers$19(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda110
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$clearRecentStickers$18(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearTopPeers$149() {
        try {
            getMessagesStorage().getDatabase().executeFast("DELETE FROM chat_hints WHERE 1").stepThis().dispose();
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deletePeer$159(long j, int i) {
        try {
            getMessagesStorage().getDatabase().executeFast(String.format(Locale.US, "DELETE FROM chat_hints WHERE did = %d AND type = %d", Long.valueOf(j), Integer.valueOf(i))).stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchEmojiStatuses$230() {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.recentEmojiStatusesUpdate, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x007d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$fetchEmojiStatuses$231(int i) {
        boolean z;
        NativeByteBuffer byteBufferValue;
        try {
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT data FROM emoji_statuses WHERE type = " + i + " LIMIT 1", new Object[0]);
            if (!queryFinalized.next() || queryFinalized.getColumnCount() <= 0 || queryFinalized.isNull(0) || (byteBufferValue = queryFinalized.byteBufferValue(0)) == null) {
                z = false;
            } else {
                TLRPC.account_EmojiStatuses TLdeserialize = TLRPC.account_EmojiStatuses.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                if (TLdeserialize instanceof TLRPC.TL_account_emojiStatuses) {
                    this.emojiStatusesHash[i] = TLdeserialize.hash;
                    this.emojiStatuses[i] = TLdeserialize.statuses;
                    z = true;
                } else {
                    z = false;
                }
                try {
                    byteBufferValue.reuse();
                } catch (Exception e) {
                    e = e;
                    FileLog.e(e);
                    this.emojiStatusesFromCacheFetched[i] = true;
                    this.emojiStatusesFetching[i] = false;
                    if (z) {
                    }
                }
            }
            queryFinalized.dispose();
        } catch (Exception e2) {
            e = e2;
            z = false;
        }
        this.emojiStatusesFromCacheFetched[i] = true;
        this.emojiStatusesFetching[i] = false;
        if (z) {
            fetchEmojiStatuses(i, false);
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda48
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$fetchEmojiStatuses$230();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchEmojiStatuses$232() {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.recentEmojiStatusesUpdate, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchEmojiStatuses$233(int i, TLObject tLObject, TLRPC.TL_error tL_error) {
        this.emojiStatusesFetchDate[i] = Long.valueOf(System.currentTimeMillis() / 1000);
        if (tLObject instanceof TLRPC.TL_account_emojiStatusesNotModified) {
            this.emojiStatusesFetching[i] = false;
            return;
        }
        if (tLObject instanceof TLRPC.TL_account_emojiStatuses) {
            TLRPC.TL_account_emojiStatuses tL_account_emojiStatuses = (TLRPC.TL_account_emojiStatuses) tLObject;
            this.emojiStatusesHash[i] = tL_account_emojiStatuses.hash;
            this.emojiStatuses[i] = tL_account_emojiStatuses.statuses;
            updateEmojiStatuses(i, tL_account_emojiStatuses);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda136
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$fetchEmojiStatuses$232();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchNewEmojiKeywords$207(String str) {
        this.currentFetchingEmoji.remove(str);
        this.fetchedEmoji.add(str);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.emojiKeywordsLoaded, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchNewEmojiKeywords$208(String str) {
        this.currentFetchingEmoji.remove(str);
        this.fetchedEmoji.add(str);
        fetchNewEmojiKeywords(new String[]{str});
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.emojiKeywordsLoaded, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchNewEmojiKeywords$209(final String str) {
        try {
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("DELETE FROM emoji_keywords_info_v2 WHERE lang = ?");
            executeFast.bindString(1, str);
            executeFast.step();
            executeFast.dispose();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda40
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$fetchNewEmojiKeywords$208(str);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchNewEmojiKeywords$210() {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.emojiKeywordsLoaded, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchNewEmojiKeywords$211(String str) {
        this.currentFetchingEmoji.remove(str);
        this.fetchedEmoji.add(str);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.emojiKeywordsLoaded, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchNewEmojiKeywords$212(int i, String str, final String str2, TLObject tLObject, TLRPC.TL_error tL_error) {
        Runnable runnable;
        if (tLObject != null) {
            TLRPC.TL_emojiKeywordsDifference tL_emojiKeywordsDifference = (TLRPC.TL_emojiKeywordsDifference) tLObject;
            if (i != -1 && !tL_emojiKeywordsDifference.lang_code.equals(str)) {
                getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda105
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$fetchNewEmojiKeywords$209(str2);
                    }
                });
                return;
            } else {
                putEmojiKeywords(str2, tL_emojiKeywordsDifference);
                runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda106
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$fetchNewEmojiKeywords$210();
                    }
                };
            }
        } else {
            runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda107
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$fetchNewEmojiKeywords$211(str2);
                }
            };
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0064  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$fetchNewEmojiKeywords$213(final String str) {
        final int i;
        TLRPC.TL_messages_getEmojiKeywordsDifference tL_messages_getEmojiKeywordsDifference;
        final String str2 = null;
        long j = 0;
        try {
        } catch (Exception e) {
            e = e;
        }
        try {
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT alias, version, date FROM emoji_keywords_info_v2 WHERE lang = ?", str);
            if (queryFinalized.next()) {
                str2 = queryFinalized.stringValue(0);
                i = queryFinalized.intValue(1);
                try {
                    j = queryFinalized.longValue(2);
                } catch (Exception e2) {
                    e = e2;
                    FileLog.e(e);
                    if (BuildVars.DEBUG_VERSION) {
                    }
                    if (i == -1) {
                    }
                    getConnectionsManager().sendRequest(tL_messages_getEmojiKeywordsDifference, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda24
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            MediaDataController.this.lambda$fetchNewEmojiKeywords$212(i, str2, str, tLObject, tL_error);
                        }
                    });
                }
            } else {
                i = -1;
            }
            queryFinalized.dispose();
        } catch (Exception e3) {
            e = e3;
            i = -1;
            FileLog.e(e);
            if (BuildVars.DEBUG_VERSION) {
            }
            if (i == -1) {
            }
            getConnectionsManager().sendRequest(tL_messages_getEmojiKeywordsDifference, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda24
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$fetchNewEmojiKeywords$212(i, str2, str, tLObject, tL_error);
                }
            });
        }
        if (BuildVars.DEBUG_VERSION && Math.abs(System.currentTimeMillis() - j) < 3600000) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$fetchNewEmojiKeywords$207(str);
                }
            });
            return;
        }
        if (i == -1) {
            TLRPC.TL_messages_getEmojiKeywords tL_messages_getEmojiKeywords = new TLRPC.TL_messages_getEmojiKeywords();
            tL_messages_getEmojiKeywords.lang_code = str;
            tL_messages_getEmojiKeywordsDifference = tL_messages_getEmojiKeywords;
        } else {
            TLRPC.TL_messages_getEmojiKeywordsDifference tL_messages_getEmojiKeywordsDifference2 = new TLRPC.TL_messages_getEmojiKeywordsDifference();
            tL_messages_getEmojiKeywordsDifference2.lang_code = str;
            tL_messages_getEmojiKeywordsDifference2.from_version = i;
            tL_messages_getEmojiKeywordsDifference = tL_messages_getEmojiKeywordsDifference2;
        }
        getConnectionsManager().sendRequest(tL_messages_getEmojiKeywordsDifference, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda24
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$fetchNewEmojiKeywords$212(i, str2, str, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchStickerSetInternal$41(String str, TLObject tLObject) {
        Boolean bool;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        ArrayList<Utilities.Callback2<Boolean, TLRPC.TL_messages_stickerSet>> arrayList = this.loadingStickerSets.get(str);
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                Utilities.Callback2<Boolean, TLRPC.TL_messages_stickerSet> callback2 = arrayList.get(i);
                if (tLObject != null) {
                    bool = Boolean.TRUE;
                    tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject;
                } else {
                    bool = Boolean.FALSE;
                    tL_messages_stickerSet = null;
                }
                callback2.run(bool, tL_messages_stickerSet);
            }
        }
        this.loadingStickerSets.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchStickerSetInternal$42(final String str, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda88
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$fetchStickerSetInternal$41(str, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:40:0x010d A[LOOP:1: B:33:0x00a2->B:40:0x010d, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0114 A[EDGE_INSN: B:41:0x0114->B:42:0x0114 BREAK  A[LOOP:1: B:33:0x00a2->B:40:0x010d], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$fillWithAnimatedEmoji$225(Integer num, ArrayList arrayList, boolean z, boolean z2, ArrayList[] arrayListArr, Runnable runnable) {
        int intValue;
        String str;
        int i;
        ArrayList<TLRPC.StickerSetCovered> arrayList2;
        ArrayList arrayList3;
        ArrayList arrayList4;
        ArrayList arrayList5;
        ArrayList arrayList6;
        TLRPC.StickerSetCovered stickerSetCovered;
        TLRPC.TL_documentAttributeCustomEmoji tL_documentAttributeCustomEmoji;
        TLRPC.StickerSet stickerSet;
        String str2;
        TLRPC.TL_documentAttributeCustomEmoji tL_documentAttributeCustomEmoji2;
        TLRPC.StickerSet stickerSet2;
        String str3;
        ArrayList<TLRPC.StickerSetCovered> arrayList7;
        ArrayList arrayList8;
        String str4;
        TLRPC.Document document;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        MediaDataController mediaDataController = this;
        ArrayList arrayList9 = arrayList;
        ArrayList<TLRPC.StickerSetCovered> featuredEmojiSets = getFeaturedEmojiSets();
        ArrayList arrayList10 = new ArrayList();
        HashSet hashSet = new HashSet();
        ArrayList arrayList11 = new ArrayList();
        boolean z3 = true;
        if (num != null) {
            intValue = num.intValue();
        } else if (arrayList.size() > 5) {
            intValue = 1;
        } else {
            intValue = 2;
            if (arrayList.size() <= 2) {
                intValue = 3;
            }
        }
        int size = arrayList.size();
        if (num == null) {
            size = Math.min(15, size);
        }
        if (!UserConfig.getInstance(mediaDataController.currentAccount).isPremium() && !z) {
            z3 = false;
        }
        if (z2) {
            str = UserConfig.getInstance(mediaDataController.currentAccount).defaultTopicIcons;
            if (arrayListArr[0] != null) {
                if (str != null) {
                    tL_messages_stickerSet = getInstance(mediaDataController.currentAccount).getStickerSetByName(str);
                    if (tL_messages_stickerSet == null) {
                        tL_messages_stickerSet = getInstance(mediaDataController.currentAccount).getStickerSetByEmojiOrName(str);
                    }
                } else {
                    tL_messages_stickerSet = null;
                }
                if (tL_messages_stickerSet != null) {
                    arrayListArr[0].add(tL_messages_stickerSet);
                }
            }
        } else {
            str = null;
        }
        int i2 = 0;
        while (i2 < size) {
            String str5 = ((KeywordResult) arrayList9.get(i2)).emoji;
            if (TextUtils.isEmpty(str5)) {
                arrayList4 = arrayList9;
                arrayList2 = featuredEmojiSets;
                arrayList5 = arrayList10;
                i = size;
            } else {
                arrayList11.clear();
                if (Emoji.recentEmoji != null) {
                    int i3 = 0;
                    while (true) {
                        ArrayList<String> arrayList12 = Emoji.recentEmoji;
                        if (i3 >= arrayList12.size()) {
                            break;
                        }
                        if (arrayList12.get(i3).startsWith("animated_")) {
                            try {
                                i = size;
                                try {
                                    TLRPC.Document findDocument = AnimatedEmojiDrawable.findDocument(mediaDataController.currentAccount, Long.parseLong(arrayList12.get(i3).substring(9)));
                                    String findAnimatedEmojiEmoticon = MessageObject.findAnimatedEmojiEmoticon(findDocument, null);
                                    if (findDocument != null) {
                                        if (findAnimatedEmojiEmoticon != null) {
                                            if (findAnimatedEmojiEmoticon.contains(str5)) {
                                                if (!z3) {
                                                    if (MessageObject.isFreeEmoji(findDocument)) {
                                                    }
                                                }
                                                if (!hashSet.contains(Long.valueOf(findDocument.id))) {
                                                    hashSet.add(Long.valueOf(findDocument.id));
                                                    arrayList11.add(findDocument);
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception unused) {
                                    if (arrayList11.size() < intValue) {
                                    }
                                }
                            } catch (Exception unused2) {
                                i = size;
                            }
                        } else {
                            i = size;
                        }
                        if (arrayList11.size() < intValue) {
                            break;
                        }
                        i3++;
                        size = i;
                    }
                }
                i = size;
                if (arrayList11.size() < intValue) {
                    if (arrayListArr[0] != null) {
                        int i4 = 0;
                        for (char c = 0; i4 < arrayListArr[c].size(); c = 0) {
                            TLRPC.TL_messages_stickerSet tL_messages_stickerSet2 = (TLRPC.TL_messages_stickerSet) arrayListArr[c].get(i4);
                            if (tL_messages_stickerSet2 == null || tL_messages_stickerSet2.packs == null) {
                                arrayList2 = featuredEmojiSets;
                                arrayList3 = arrayList10;
                                if (tL_messages_stickerSet2 != null && tL_messages_stickerSet2.documents != null) {
                                    for (int i5 = 0; i5 < tL_messages_stickerSet2.documents.size(); i5++) {
                                        TLRPC.Document document2 = tL_messages_stickerSet2.documents.get(i5);
                                        if (document2 != null && document2.attributes != null && !arrayList11.contains(document2)) {
                                            int i6 = 0;
                                            while (true) {
                                                if (i6 >= document2.attributes.size()) {
                                                    tL_documentAttributeCustomEmoji2 = null;
                                                    break;
                                                }
                                                TLRPC.DocumentAttribute documentAttribute = document2.attributes.get(i6);
                                                if (documentAttribute instanceof TLRPC.TL_documentAttributeCustomEmoji) {
                                                    tL_documentAttributeCustomEmoji2 = (TLRPC.TL_documentAttributeCustomEmoji) documentAttribute;
                                                    break;
                                                }
                                                i6++;
                                            }
                                            if (tL_documentAttributeCustomEmoji2 != null && !TextUtils.isEmpty(tL_documentAttributeCustomEmoji2.alt) && tL_documentAttributeCustomEmoji2.alt.contains(str5) && ((z3 || tL_documentAttributeCustomEmoji2.free || ((stickerSet2 = tL_messages_stickerSet2.set) != null && (str3 = stickerSet2.short_name) != null && str3.equals(str))) && !hashSet.contains(Long.valueOf(document2.id)))) {
                                                hashSet.add(Long.valueOf(document2.id));
                                                arrayList11.add(document2);
                                                if (arrayList11.size() >= intValue) {
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                int i7 = 0;
                                while (i7 < tL_messages_stickerSet2.packs.size()) {
                                    TLRPC.TL_stickerPack tL_stickerPack = tL_messages_stickerSet2.packs.get(i7);
                                    if (tL_stickerPack == null || (str4 = tL_stickerPack.emoticon) == null || !str4.contains(str5)) {
                                        arrayList7 = featuredEmojiSets;
                                        arrayList8 = arrayList10;
                                    } else {
                                        arrayList8 = arrayList10;
                                        int i8 = 0;
                                        while (true) {
                                            if (i8 >= tL_stickerPack.documents.size()) {
                                                arrayList7 = featuredEmojiSets;
                                                break;
                                            }
                                            long longValue = tL_stickerPack.documents.get(i8).longValue();
                                            TLRPC.TL_stickerPack tL_stickerPack2 = tL_stickerPack;
                                            int i9 = 0;
                                            while (true) {
                                                if (i9 >= tL_messages_stickerSet2.documents.size()) {
                                                    arrayList7 = featuredEmojiSets;
                                                    document = null;
                                                    break;
                                                }
                                                document = tL_messages_stickerSet2.documents.get(i9);
                                                arrayList7 = featuredEmojiSets;
                                                if (document != null && document.id == longValue) {
                                                    break;
                                                }
                                                i9++;
                                                featuredEmojiSets = arrayList7;
                                            }
                                            if (document != null && document.attributes != null && !arrayList11.contains(document) && !hashSet.contains(Long.valueOf(document.id))) {
                                                hashSet.add(Long.valueOf(document.id));
                                                arrayList11.add(document);
                                                if (arrayList11.size() >= intValue) {
                                                    break;
                                                }
                                            }
                                            i8++;
                                            tL_stickerPack = tL_stickerPack2;
                                            featuredEmojiSets = arrayList7;
                                        }
                                    }
                                    i7++;
                                    arrayList10 = arrayList8;
                                    featuredEmojiSets = arrayList7;
                                }
                                arrayList2 = featuredEmojiSets;
                                arrayList3 = arrayList10;
                            }
                            if (arrayList11.size() >= intValue) {
                                break;
                            }
                            i4++;
                            arrayList10 = arrayList3;
                            featuredEmojiSets = arrayList2;
                        }
                    }
                }
                arrayList2 = featuredEmojiSets;
                arrayList3 = arrayList10;
                if (arrayList11.size() < intValue && arrayList2 != null) {
                    for (int i10 = 0; i10 < arrayList2.size(); i10++) {
                        ArrayList<TLRPC.StickerSetCovered> arrayList13 = arrayList2;
                        TLRPC.StickerSetCovered stickerSetCovered2 = arrayList13.get(i10);
                        if (stickerSetCovered2 != null) {
                            ArrayList<TLRPC.Document> arrayList14 = stickerSetCovered2 instanceof TLRPC.TL_stickerSetFullCovered ? ((TLRPC.TL_stickerSetFullCovered) stickerSetCovered2).documents : stickerSetCovered2.covers;
                            if (arrayList14 != null) {
                                int i11 = 0;
                                while (true) {
                                    if (i11 >= arrayList14.size()) {
                                        arrayList2 = arrayList13;
                                        break;
                                    }
                                    TLRPC.Document document3 = arrayList14.get(i11);
                                    if (document3 == null || document3.attributes == null || arrayList11.contains(document3)) {
                                        arrayList2 = arrayList13;
                                    } else {
                                        int i12 = 0;
                                        while (true) {
                                            if (i12 >= document3.attributes.size()) {
                                                arrayList2 = arrayList13;
                                                tL_documentAttributeCustomEmoji = null;
                                                break;
                                            }
                                            TLRPC.DocumentAttribute documentAttribute2 = document3.attributes.get(i12);
                                            arrayList2 = arrayList13;
                                            if (documentAttribute2 instanceof TLRPC.TL_documentAttributeCustomEmoji) {
                                                tL_documentAttributeCustomEmoji = (TLRPC.TL_documentAttributeCustomEmoji) documentAttribute2;
                                                break;
                                            } else {
                                                i12++;
                                                arrayList13 = arrayList2;
                                            }
                                        }
                                        if (tL_documentAttributeCustomEmoji != null && !TextUtils.isEmpty(tL_documentAttributeCustomEmoji.alt) && tL_documentAttributeCustomEmoji.alt.contains(str5) && (z3 || tL_documentAttributeCustomEmoji.free || ((stickerSet = stickerSetCovered2.set) != null && (str2 = stickerSet.short_name) != null && str2.equals(str)))) {
                                            stickerSetCovered = stickerSetCovered2;
                                            if (!hashSet.contains(Long.valueOf(document3.id))) {
                                                hashSet.add(Long.valueOf(document3.id));
                                                arrayList11.add(document3);
                                                if (arrayList11.size() >= intValue) {
                                                    break;
                                                }
                                            } else {
                                                continue;
                                            }
                                            i11++;
                                            stickerSetCovered2 = stickerSetCovered;
                                            arrayList13 = arrayList2;
                                        }
                                    }
                                    stickerSetCovered = stickerSetCovered2;
                                    i11++;
                                    stickerSetCovered2 = stickerSetCovered;
                                    arrayList13 = arrayList2;
                                }
                                if (arrayList11.size() >= intValue) {
                                    break;
                                }
                            }
                        }
                        arrayList2 = arrayList13;
                    }
                }
                if (arrayList11.isEmpty()) {
                    arrayList4 = arrayList;
                } else {
                    arrayList4 = arrayList;
                    String str6 = ((KeywordResult) arrayList4.get(i2)).keyword;
                    int i13 = 0;
                    while (i13 < arrayList11.size()) {
                        TLRPC.Document document4 = (TLRPC.Document) arrayList11.get(i13);
                        if (document4 != null) {
                            KeywordResult keywordResult = new KeywordResult();
                            keywordResult.emoji = "animated_" + document4.id;
                            keywordResult.keyword = str6;
                            arrayList6 = arrayList3;
                            arrayList6.add(keywordResult);
                        } else {
                            arrayList6 = arrayList3;
                        }
                        i13++;
                        arrayList3 = arrayList6;
                    }
                }
                arrayList5 = arrayList3;
            }
            i2++;
            arrayList9 = arrayList4;
            arrayList10 = arrayList5;
            size = i;
            featuredEmojiSets = arrayList2;
            mediaDataController = this;
        }
        arrayList9.addAll(0, arrayList10);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$fillWithAnimatedEmoji$226(boolean[] zArr, ArrayList[] arrayListArr, Runnable runnable, ArrayList arrayList) {
        if (zArr[0]) {
            return;
        }
        arrayListArr[0] = arrayList;
        runnable.run();
        zArr[0] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillWithAnimatedEmoji$227(final boolean[] zArr, final ArrayList[] arrayListArr, final Runnable runnable) {
        loadStickers(5, true, false, false, new Utilities.Callback() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda30
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                MediaDataController.lambda$fillWithAnimatedEmoji$226(zArr, arrayListArr, runnable, (ArrayList) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$fillWithAnimatedEmoji$228(boolean[] zArr, Runnable runnable) {
        if (zArr[0]) {
            return;
        }
        runnable.run();
        zArr[0] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$findStickerSetByNameInCache$29(TLRPC.TL_messages_stickerSet tL_messages_stickerSet, Utilities.Callback callback) {
        putStickerSet(tL_messages_stickerSet, false);
        callback.run(tL_messages_stickerSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$findStickerSetByNameInCache$30(String str, final Utilities.Callback callback) {
        final TLRPC.TL_messages_stickerSet cachedStickerSetInternal = getCachedStickerSetInternal(str.toLowerCase(), (Integer) 0);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda97
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$findStickerSetByNameInCache$29(cachedStickerSetInternal, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getAnimatedEmojiByKeywords$216(String str, ArrayList arrayList, ArrayList arrayList2, Utilities.Callback callback) {
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        String lowerCase = str.toLowerCase();
        for (int i = 0; i < arrayList.size(); i++) {
            if (((TLRPC.TL_messages_stickerSet) arrayList.get(i)).keywords != null) {
                ArrayList<TLRPC.TL_stickerKeyword> arrayList5 = ((TLRPC.TL_messages_stickerSet) arrayList.get(i)).keywords;
                for (int i2 = 0; i2 < arrayList5.size(); i2++) {
                    for (int i3 = 0; i3 < arrayList5.get(i2).keyword.size(); i3++) {
                        String str2 = arrayList5.get(i2).keyword.get(i3);
                        if (lowerCase.equals(str2)) {
                            arrayList3.add(Long.valueOf(arrayList5.get(i2).document_id));
                        } else if (lowerCase.contains(str2) || str2.contains(lowerCase)) {
                            arrayList4.add(Long.valueOf(arrayList5.get(i2).document_id));
                        }
                    }
                }
            }
        }
        for (int i4 = 0; i4 < arrayList2.size(); i4++) {
            if ((arrayList2.get(i4) instanceof TLRPC.TL_stickerSetFullCovered) && ((TLRPC.TL_stickerSetFullCovered) arrayList2.get(i4)).keywords != null) {
                ArrayList<TLRPC.TL_stickerKeyword> arrayList6 = ((TLRPC.TL_stickerSetFullCovered) arrayList2.get(i4)).keywords;
                for (int i5 = 0; i5 < arrayList6.size(); i5++) {
                    for (int i6 = 0; i6 < arrayList6.get(i5).keyword.size(); i6++) {
                        String str3 = arrayList6.get(i5).keyword.get(i6);
                        if (lowerCase.equals(str3)) {
                            arrayList3.add(Long.valueOf(arrayList6.get(i5).document_id));
                        } else if (lowerCase.contains(str3) || str3.contains(lowerCase)) {
                            arrayList4.add(Long.valueOf(arrayList6.get(i5).document_id));
                        }
                    }
                }
            }
        }
        arrayList3.addAll(arrayList4);
        if (callback != null) {
            callback.run(arrayList3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getEmojiNames$218(String[] strArr, String str, final Utilities.Callback callback) {
        SQLiteCursor sQLiteCursor = null;
        try {
            try {
                Object[] objArr = new Object[strArr.length + 1];
                objArr[0] = str;
                String str2 = "1 = 1";
                int i = 0;
                while (i < strArr.length) {
                    if (i == 0) {
                        str2 = "lang = ?";
                    } else {
                        str2 = str2 + " OR lang = ?";
                    }
                    SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT alias FROM emoji_keywords_info_v2 WHERE lang = ?", strArr[i]);
                    if (queryFinalized.next()) {
                        strArr[i] = queryFinalized.stringValue(0);
                    }
                    queryFinalized.dispose();
                    int i2 = i + 1;
                    objArr[i2] = strArr[i];
                    i = i2;
                }
                sQLiteCursor = getMessagesStorage().getDatabase().executeFast("SELECT keyword FROM emoji_keywords_v2 WHERE emoji = ? AND (" + str2 + ")").query(objArr);
                final ArrayList arrayList = new ArrayList();
                while (sQLiteCursor.next()) {
                    arrayList.add(sQLiteCursor.stringValue(0));
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda143
                    @Override // java.lang.Runnable
                    public final void run() {
                        Utilities.Callback.this.run(arrayList);
                    }
                });
            } catch (Exception e) {
                FileLog.e(e);
                if (sQLiteCursor == null) {
                    return;
                }
            }
            sQLiteCursor.dispose();
        } catch (Throwable th) {
            if (sQLiteCursor != null) {
                sQLiteCursor.dispose();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getEmojiSuggestions$219(String[] strArr, KeywordResultCallback keywordResultCallback, ArrayList arrayList) {
        for (String str : strArr) {
            if (this.currentFetchingEmoji.get(str) != null) {
                return;
            }
        }
        keywordResultCallback.run(arrayList, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$getEmojiSuggestions$220(ArrayList arrayList, KeywordResult keywordResult, KeywordResult keywordResult2) {
        int indexOf = arrayList.indexOf(keywordResult.emoji);
        int i = ConnectionsManager.DEFAULT_DATACENTER_ID;
        if (indexOf < 0) {
            indexOf = ConnectionsManager.DEFAULT_DATACENTER_ID;
        }
        int indexOf2 = arrayList.indexOf(keywordResult2.emoji);
        if (indexOf2 >= 0) {
            i = indexOf2;
        }
        if (indexOf < i) {
            return -1;
        }
        if (indexOf > i) {
            return 1;
        }
        int length = keywordResult.keyword.length();
        int length2 = keywordResult2.keyword.length();
        if (length < length2) {
            return -1;
        }
        return length > length2 ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getEmojiSuggestions$222(CountDownLatch countDownLatch, final KeywordResultCallback keywordResultCallback, final ArrayList arrayList, final String str) {
        if (countDownLatch == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda227
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.KeywordResultCallback.this.run(arrayList, str);
                }
            });
        } else {
            keywordResultCallback.run(arrayList, str);
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:84:0x016c  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0183  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$getEmojiSuggestions$224(final String[] strArr, final KeywordResultCallback keywordResultCallback, boolean z, String str, boolean z2, final ArrayList arrayList, boolean z3, Integer num, boolean z4, boolean z5, boolean z6, final CountDownLatch countDownLatch) {
        String str2;
        SQLiteCursor queryFinalized;
        final ArrayList<KeywordResult> arrayList2 = new ArrayList<>();
        HashMap hashMap = new HashMap();
        boolean z7 = false;
        final String str3 = null;
        for (String str4 : strArr) {
            try {
                SQLiteCursor queryFinalized2 = getMessagesStorage().getDatabase().queryFinalized("SELECT alias FROM emoji_keywords_info_v2 WHERE lang = ?", str4);
                if (queryFinalized2.next()) {
                    str3 = queryFinalized2.stringValue(0);
                }
                queryFinalized2.dispose();
                if (str3 != null) {
                    z7 = true;
                }
            } catch (Exception e) {
                e = e;
            }
        }
        try {
        } catch (Exception e2) {
            e = e2;
            FileLog.e(e);
            Collections.sort(arrayList2, new Comparator() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda139
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$getEmojiSuggestions$220;
                    lambda$getEmojiSuggestions$220 = MediaDataController.lambda$getEmojiSuggestions$220(arrayList, (MediaDataController.KeywordResult) obj, (MediaDataController.KeywordResult) obj2);
                    return lambda$getEmojiSuggestions$220;
                }
            });
            if (!z3) {
            }
        }
        if (!z7) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda138
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$getEmojiSuggestions$219(strArr, keywordResultCallback, arrayList2);
                }
            });
            return;
        }
        if (z) {
            int[] iArr = new int[1];
            ArrayList<Emoji.EmojiSpanRange> parseEmojis = Emoji.parseEmojis(str, iArr);
            if (iArr[0] > 0) {
                for (int i = 0; i < parseEmojis.size(); i++) {
                    String charSequence = parseEmojis.get(i).code.toString();
                    int i2 = 0;
                    while (true) {
                        if (i2 >= arrayList2.size()) {
                            KeywordResult keywordResult = new KeywordResult();
                            keywordResult.emoji = charSequence;
                            keywordResult.keyword = "";
                            arrayList2.add(keywordResult);
                            break;
                        }
                        if (TextUtils.equals(arrayList2.get(i2).emoji, charSequence)) {
                            break;
                        } else {
                            i2++;
                        }
                    }
                }
            }
        }
        String lowerCase = str.toLowerCase();
        for (int i3 = 0; i3 < 2; i3++) {
            if (i3 == 1) {
                String translitString = LocaleController.getInstance().getTranslitString(lowerCase, false, false);
                if (!translitString.equals(lowerCase)) {
                    lowerCase = translitString;
                }
            }
            StringBuilder sb = new StringBuilder(lowerCase);
            int length = sb.length();
            while (true) {
                if (length <= 0) {
                    str2 = null;
                    break;
                }
                length--;
                char charAt = (char) (sb.charAt(length) + 1);
                sb.setCharAt(length, charAt);
                if (charAt != 0) {
                    str2 = sb.toString();
                    break;
                }
            }
            if (z2) {
                queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT emoji, keyword FROM emoji_keywords_v2 WHERE keyword = ?", lowerCase);
            } else if (str2 != null) {
                queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT emoji, keyword FROM emoji_keywords_v2 WHERE keyword >= ? AND keyword < ?", lowerCase, str2);
            } else {
                lowerCase = lowerCase + "%";
                queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT emoji, keyword FROM emoji_keywords_v2 WHERE keyword LIKE ?", lowerCase);
            }
            while (queryFinalized.next()) {
                String replace = queryFinalized.stringValue(0).replace("️", "");
                if (hashMap.get(replace) == null) {
                    hashMap.put(replace, Boolean.TRUE);
                    KeywordResult keywordResult2 = new KeywordResult();
                    keywordResult2.emoji = replace;
                    keywordResult2.keyword = queryFinalized.stringValue(1);
                    arrayList2.add(keywordResult2);
                }
            }
            queryFinalized.dispose();
        }
        Collections.sort(arrayList2, new Comparator() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda139
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$getEmojiSuggestions$220;
                lambda$getEmojiSuggestions$220 = MediaDataController.lambda$getEmojiSuggestions$220(arrayList, (MediaDataController.KeywordResult) obj, (MediaDataController.KeywordResult) obj2);
                return lambda$getEmojiSuggestions$220;
            }
        });
        if (!z3) {
            fillWithAnimatedEmoji(arrayList2, num, z4, z5, z6, new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda140
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.lambda$getEmojiSuggestions$222(countDownLatch, keywordResultCallback, arrayList2, str3);
                }
            });
        } else if (countDownLatch == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda141
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.KeywordResultCallback.this.run(arrayList2, str3);
                }
            });
        } else {
            keywordResultCallback.run(arrayList2, str3);
            countDownLatch.countDown();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ TLRPC.MessageEntity lambda$getEntities$181(Void r0) {
        return new TLRPC.TL_messageEntityBold();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ TLRPC.MessageEntity lambda$getEntities$182(Void r0) {
        return new TLRPC.TL_messageEntityItalic();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ TLRPC.MessageEntity lambda$getEntities$183(Void r0) {
        return new TLRPC.TL_messageEntitySpoiler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ TLRPC.MessageEntity lambda$getEntities$184(Void r0) {
        return new TLRPC.TL_messageEntityStrike();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getMediaCount$130(long j, long j2, int i, int i2, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject != null) {
            TLRPC.Vector vector = (TLRPC.Vector) tLObject;
            if (vector.objects.isEmpty()) {
                return;
            }
            processLoadedMediaCount(((TLRPC.TL_messages_searchCounter) vector.objects.get(0)).count, j, j2, i, i2, false, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getMediaCountDatabase$137(long j, long j2, int i, int i2) {
        int i3;
        int i4;
        try {
            SQLiteCursor queryFinalized = j != 0 ? getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT count, old FROM media_counts_topics WHERE uid = %d AND topic_id = %d AND type = %d LIMIT 1", Long.valueOf(j2), Long.valueOf(j), Integer.valueOf(i)), new Object[0]) : getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT count, old FROM media_counts_v2 WHERE uid = %d AND type = %d LIMIT 1", Long.valueOf(j2), Integer.valueOf(i)), new Object[0]);
            if (queryFinalized.next()) {
                i3 = queryFinalized.intValue(0);
                i4 = queryFinalized.intValue(1);
            } else {
                i3 = -1;
                i4 = 0;
            }
            queryFinalized.dispose();
            if (i3 == -1 && DialogObject.isEncryptedDialog(j2)) {
                SQLiteCursor queryFinalized2 = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT COUNT(mid) FROM media_v4 WHERE uid = %d AND type = %d LIMIT 1", Long.valueOf(j2), Integer.valueOf(i)), new Object[0]);
                if (queryFinalized2.next()) {
                    i3 = queryFinalized2.intValue(0);
                }
                queryFinalized2.dispose();
                if (i3 != -1) {
                    putMediaCountDatabase(j2, j, i, i3);
                }
            }
            processLoadedMediaCount(i3, j2, j, i, i2, true, i4);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getMediaCounts$125(long j, long j2, int[] iArr) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.mediaCountsDidLoad, Long.valueOf(j), Long.valueOf(j2), iArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getMediaCounts$126(long j, long j2, int[] iArr) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.mediaCountsDidLoad, Long.valueOf(j), Long.valueOf(j2), iArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getMediaCounts$127(final int[] iArr, final long j, final long j2, TLObject tLObject, TLRPC.TL_error tL_error) {
        int i;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] < 0) {
                iArr[i2] = 0;
            }
        }
        if (tLObject != null) {
            TLRPC.Vector vector = (TLRPC.Vector) tLObject;
            int size = vector.objects.size();
            for (int i3 = 0; i3 < size; i3++) {
                TLRPC.TL_messages_searchCounter tL_messages_searchCounter = (TLRPC.TL_messages_searchCounter) vector.objects.get(i3);
                TLRPC.MessagesFilter messagesFilter = tL_messages_searchCounter.filter;
                if (messagesFilter instanceof TLRPC.TL_inputMessagesFilterPhotoVideo) {
                    i = 0;
                } else if (messagesFilter instanceof TLRPC.TL_inputMessagesFilterDocument) {
                    i = 1;
                } else if (messagesFilter instanceof TLRPC.TL_inputMessagesFilterRoundVoice) {
                    i = 2;
                } else if (messagesFilter instanceof TLRPC.TL_inputMessagesFilterUrl) {
                    i = 3;
                } else if (messagesFilter instanceof TLRPC.TL_inputMessagesFilterMusic) {
                    i = 4;
                } else if (messagesFilter instanceof TLRPC.TL_inputMessagesFilterGif) {
                    i = 5;
                } else if (messagesFilter instanceof TLRPC.TL_inputMessagesFilterPhotos) {
                    i = 6;
                } else if (messagesFilter instanceof TLRPC.TL_inputMessagesFilterVideo) {
                    i = 7;
                }
                int i4 = tL_messages_searchCounter.count;
                iArr[i] = i4;
                putMediaCountDatabase(j, j2, i, i4);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda94
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$getMediaCounts$126(j, j2, iArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getMediaCounts$128(long j, long j2, int[] iArr) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.mediaCountsDidLoad, Long.valueOf(j), Long.valueOf(j2), iArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:67:0x01dd  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01e0 A[Catch: Exception -> 0x007c, TryCatch #0 {Exception -> 0x007c, blocks: (B:3:0x0009, B:5:0x0057, B:7:0x009d, B:9:0x00a3, B:15:0x00ad, B:21:0x00bc, B:25:0x00c8, B:27:0x00cd, B:29:0x00f7, B:30:0x0100, B:32:0x011a, B:33:0x00fe, B:37:0x011e, B:38:0x0230, B:42:0x012d, B:44:0x0141, B:46:0x014d, B:47:0x0162, B:48:0x015c, B:51:0x0169, B:53:0x016d, B:56:0x01e6, B:57:0x0172, B:59:0x0176, B:62:0x017c, B:63:0x0183, B:65:0x01d9, B:69:0x01e0, B:71:0x01e4, B:75:0x018a, B:78:0x0195, B:81:0x01a3, B:82:0x01aa, B:85:0x01b1, B:88:0x01bc, B:91:0x01c7, B:92:0x01cf, B:94:0x01e9, B:96:0x01f2, B:98:0x0219, B:101:0x0223, B:103:0x007f), top: B:2:0x0009 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$getMediaCounts$129(final long j, final long j2, int i) {
        int[] iArr;
        int i2;
        Runnable runnable;
        ArrayList<TLRPC.MessagesFilter> arrayList;
        TLRPC.MessagesFilter tL_inputMessagesFilterVideo;
        ArrayList<TLRPC.MessagesFilter> arrayList2;
        TLRPC.MessagesFilter tL_inputMessagesFilterDocument;
        int i3;
        int i4;
        int i5 = 2;
        try {
            final int[] iArr2 = new int[9];
            iArr2[0] = -1;
            iArr2[1] = -1;
            iArr2[2] = -1;
            iArr2[3] = -1;
            iArr2[4] = -1;
            iArr2[5] = -1;
            iArr2[6] = -1;
            iArr2[7] = -1;
            iArr2[8] = -1;
            int[] iArr3 = new int[9];
            iArr3[0] = -1;
            iArr3[1] = -1;
            iArr3[2] = -1;
            iArr3[3] = -1;
            iArr3[4] = -1;
            iArr3[5] = -1;
            iArr3[6] = -1;
            iArr3[7] = -1;
            iArr3[8] = -1;
            int[] iArr4 = new int[9];
            iArr4[0] = 0;
            iArr4[1] = 0;
            iArr4[2] = 0;
            iArr4[3] = 0;
            iArr4[4] = 0;
            iArr4[5] = 0;
            iArr4[6] = 0;
            iArr4[7] = 0;
            iArr4[8] = 0;
            SQLiteCursor queryFinalized = j != 0 ? getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT type, count, old FROM media_counts_topics WHERE uid = %d AND topic_id = %d", Long.valueOf(j2), Long.valueOf(j)), new Object[0]) : getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT type, count, old FROM media_counts_v2 WHERE uid = %d", Long.valueOf(j2)), new Object[0]);
            while (queryFinalized.next()) {
                int intValue = queryFinalized.intValue(0);
                if (intValue >= 0 && intValue < 8) {
                    int intValue2 = queryFinalized.intValue(1);
                    iArr2[intValue] = intValue2;
                    iArr3[intValue] = intValue2;
                    iArr4[intValue] = queryFinalized.intValue(2);
                }
            }
            queryFinalized.dispose();
            if (DialogObject.isEncryptedDialog(j2)) {
                int i6 = 0;
                while (i6 < 9) {
                    if (iArr2[i6] == -1) {
                        SQLiteDatabase database = getMessagesStorage().getDatabase();
                        Locale locale = Locale.US;
                        Long valueOf = Long.valueOf(j2);
                        Integer valueOf2 = Integer.valueOf(i6);
                        Object[] objArr = new Object[i5];
                        objArr[0] = valueOf;
                        objArr[1] = valueOf2;
                        SQLiteCursor queryFinalized2 = database.queryFinalized(String.format(locale, "SELECT COUNT(mid) FROM media_v4 WHERE uid = %d AND type = %d LIMIT 1", objArr), new Object[0]);
                        if (queryFinalized2.next()) {
                            iArr2[i6] = queryFinalized2.intValue(0);
                        } else {
                            iArr2[i6] = 0;
                        }
                        queryFinalized2.dispose();
                        i4 = i6;
                        putMediaCountDatabase(j2, j, i6, iArr2[i6]);
                    } else {
                        i4 = i6;
                    }
                    i6 = i4 + 1;
                    i5 = 2;
                }
                runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$getMediaCounts$125(j2, j, iArr2);
                    }
                };
            } else {
                TLRPC.TL_messages_getSearchCounters tL_messages_getSearchCounters = new TLRPC.TL_messages_getSearchCounters();
                tL_messages_getSearchCounters.peer = getMessagesController().getInputPeer(j2);
                if (j != 0) {
                    if (j2 == getUserConfig().getClientUserId()) {
                        tL_messages_getSearchCounters.saved_peer_id = getMessagesController().getInputPeer(j);
                        i3 = tL_messages_getSearchCounters.flags | 4;
                    } else {
                        tL_messages_getSearchCounters.top_msg_id = (int) j;
                        i3 = tL_messages_getSearchCounters.flags | 1;
                    }
                    tL_messages_getSearchCounters.flags = i3;
                }
                boolean z = false;
                for (int i7 = 0; i7 < 9; i7++) {
                    if (tL_messages_getSearchCounters.peer == null) {
                        iArr2[i7] = 0;
                    } else if (iArr2[i7] == -1 || iArr4[i7] == 1) {
                        if (i7 == 0) {
                            arrayList2 = tL_messages_getSearchCounters.filters;
                            tL_inputMessagesFilterDocument = new TLRPC.TL_inputMessagesFilterPhotoVideo();
                        } else if (i7 == 1) {
                            arrayList2 = tL_messages_getSearchCounters.filters;
                            tL_inputMessagesFilterDocument = new TLRPC.TL_inputMessagesFilterDocument();
                        } else if (i7 == 2) {
                            tL_messages_getSearchCounters.filters.add(new TLRPC.TL_inputMessagesFilterRoundVoice());
                            if (iArr2[i7] != -1) {
                                z = true;
                            } else if (iArr4[i7] == 1) {
                                iArr2[i7] = -1;
                            }
                        } else {
                            if (i7 == 3) {
                                arrayList = tL_messages_getSearchCounters.filters;
                                tL_inputMessagesFilterVideo = new TLRPC.TL_inputMessagesFilterUrl();
                            } else if (i7 == 4) {
                                arrayList = tL_messages_getSearchCounters.filters;
                                tL_inputMessagesFilterVideo = new TLRPC.TL_inputMessagesFilterMusic();
                            } else if (i7 == 6) {
                                arrayList = tL_messages_getSearchCounters.filters;
                                tL_inputMessagesFilterVideo = new TLRPC.TL_inputMessagesFilterPhotos();
                            } else if (i7 == 7) {
                                arrayList = tL_messages_getSearchCounters.filters;
                                tL_inputMessagesFilterVideo = new TLRPC.TL_inputMessagesFilterVideo();
                            } else {
                                tL_messages_getSearchCounters.filters.add(new TLRPC.TL_inputMessagesFilterGif());
                                if (iArr2[i7] != -1) {
                                }
                            }
                            arrayList.add(tL_inputMessagesFilterVideo);
                            if (iArr2[i7] != -1) {
                            }
                        }
                        arrayList2.add(tL_inputMessagesFilterDocument);
                        if (iArr2[i7] != -1) {
                        }
                    }
                }
                if (tL_messages_getSearchCounters.filters.isEmpty()) {
                    iArr = iArr3;
                    i2 = 3;
                } else {
                    iArr = iArr3;
                    i2 = 3;
                    getConnectionsManager().bindRequestToGuid(getConnectionsManager().sendRequest(tL_messages_getSearchCounters, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda6
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            MediaDataController.this.lambda$getMediaCounts$127(iArr2, j2, j, tLObject, tL_error);
                        }
                    }), i);
                }
                if (z && getConnectionsManager().getConnectionState() == i2) {
                    return;
                }
                final int[] iArr5 = iArr;
                runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$getMediaCounts$128(j2, j, iArr5);
                    }
                };
            }
            AndroidUtilities.runOnUIThread(runnable);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getStickerSet$32(String str, Utilities.Callback callback, Boolean bool, TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        TLRPC.StickerSet stickerSet;
        this.loadingStickerSetsKeys.remove(str);
        if (callback != null) {
            callback.run(tL_messages_stickerSet);
        }
        if (tL_messages_stickerSet == null || (stickerSet = tL_messages_stickerSet.set) == null) {
            return;
        }
        this.stickerSetsById.put(stickerSet.id, tL_messages_stickerSet);
        this.stickerSetsByName.put(tL_messages_stickerSet.set.short_name.toLowerCase(), tL_messages_stickerSet);
        saveStickerSetIntoCache(tL_messages_stickerSet);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupStickersDidLoad, Long.valueOf(tL_messages_stickerSet.set.id), tL_messages_stickerSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getStickerSet$33(TLRPC.TL_messages_stickerSet tL_messages_stickerSet, final String str, final Utilities.Callback callback, boolean z, TLRPC.InputStickerSet inputStickerSet) {
        if (tL_messages_stickerSet == null) {
            if (z) {
                this.loadingStickerSetsKeys.remove(str);
                return;
            } else {
                fetchStickerSetInternal(inputStickerSet, new Utilities.Callback2() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda152
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        MediaDataController.this.lambda$getStickerSet$32(str, callback, (Boolean) obj, (TLRPC.TL_messages_stickerSet) obj2);
                    }
                });
                return;
            }
        }
        this.loadingStickerSetsKeys.remove(str);
        if (callback != null) {
            callback.run(tL_messages_stickerSet);
        }
        TLRPC.StickerSet stickerSet = tL_messages_stickerSet.set;
        if (stickerSet != null) {
            this.stickerSetsById.put(stickerSet.id, tL_messages_stickerSet);
            this.stickerSetsByName.put(tL_messages_stickerSet.set.short_name.toLowerCase(), tL_messages_stickerSet);
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupStickersDidLoad, Long.valueOf(tL_messages_stickerSet.set.id), tL_messages_stickerSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getStickerSet$34(final TLRPC.InputStickerSet inputStickerSet, Integer num, final String str, final Utilities.Callback callback, final boolean z) {
        final TLRPC.TL_messages_stickerSet cachedStickerSetInternal = getCachedStickerSetInternal(inputStickerSet.id, num);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda203
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$getStickerSet$33(cachedStickerSetInternal, str, callback, z, inputStickerSet);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getStickerSet$35(String str, Utilities.Callback callback, Boolean bool, TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        TLRPC.StickerSet stickerSet;
        this.loadingStickerSetsKeys.remove(str);
        if (callback != null) {
            callback.run(tL_messages_stickerSet);
        }
        if (tL_messages_stickerSet == null || (stickerSet = tL_messages_stickerSet.set) == null) {
            return;
        }
        this.stickerSetsById.put(stickerSet.id, tL_messages_stickerSet);
        this.stickerSetsByName.put(tL_messages_stickerSet.set.short_name.toLowerCase(), tL_messages_stickerSet);
        saveStickerSetIntoCache(tL_messages_stickerSet);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupStickersDidLoad, Long.valueOf(tL_messages_stickerSet.set.id), tL_messages_stickerSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getStickerSet$36(TLRPC.TL_messages_stickerSet tL_messages_stickerSet, final String str, final Utilities.Callback callback, boolean z, TLRPC.InputStickerSet inputStickerSet) {
        if (tL_messages_stickerSet == null) {
            if (z) {
                this.loadingStickerSetsKeys.remove(str);
                return;
            } else {
                fetchStickerSetInternal(inputStickerSet, new Utilities.Callback2() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda55
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        MediaDataController.this.lambda$getStickerSet$35(str, callback, (Boolean) obj, (TLRPC.TL_messages_stickerSet) obj2);
                    }
                });
                return;
            }
        }
        this.loadingStickerSetsKeys.remove(str);
        if (callback != null) {
            callback.run(tL_messages_stickerSet);
        }
        TLRPC.StickerSet stickerSet = tL_messages_stickerSet.set;
        if (stickerSet != null) {
            this.stickerSetsById.put(stickerSet.id, tL_messages_stickerSet);
            this.stickerSetsByName.put(tL_messages_stickerSet.set.short_name.toLowerCase(), tL_messages_stickerSet);
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupStickersDidLoad, Long.valueOf(tL_messages_stickerSet.set.id), tL_messages_stickerSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getStickerSet$37(final TLRPC.InputStickerSet inputStickerSet, Integer num, final String str, final Utilities.Callback callback, final boolean z) {
        final TLRPC.TL_messages_stickerSet cachedStickerSetInternal = getCachedStickerSetInternal(inputStickerSet.short_name.toLowerCase(), num);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda108
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$getStickerSet$36(cachedStickerSetInternal, str, callback, z, inputStickerSet);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getStickerSet$38(String str, Utilities.Callback callback, TLRPC.InputStickerSet inputStickerSet, Boolean bool, TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        this.loadingStickerSetsKeys.remove(str);
        if (callback != null) {
            callback.run(tL_messages_stickerSet);
        }
        if (tL_messages_stickerSet != null) {
            TLRPC.StickerSet stickerSet = tL_messages_stickerSet.set;
            if (stickerSet != null) {
                this.stickerSetsById.put(stickerSet.id, tL_messages_stickerSet);
                this.stickerSetsByName.put(tL_messages_stickerSet.set.short_name.toLowerCase(), tL_messages_stickerSet);
                boolean z = inputStickerSet instanceof TLRPC.TL_inputStickerSetEmojiDefaultStatuses;
                if (z) {
                    this.stickerSetDefaultStatuses = tL_messages_stickerSet;
                }
                if (z) {
                    this.stickerSetDefaultChannelStatuses = tL_messages_stickerSet;
                }
            }
            saveStickerSetIntoCache(tL_messages_stickerSet);
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupStickersDidLoad, Long.valueOf(tL_messages_stickerSet.set.id), tL_messages_stickerSet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$getTextStyleRuns$180(TLRPC.MessageEntity messageEntity, TLRPC.MessageEntity messageEntity2) {
        int i = messageEntity.offset;
        int i2 = messageEntity2.offset;
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$increaseInlineRating$150(TLRPC.TL_topPeer tL_topPeer, TLRPC.TL_topPeer tL_topPeer2) {
        double d = tL_topPeer.rating;
        double d2 = tL_topPeer2.rating;
        if (d > d2) {
            return -1;
        }
        return d < d2 ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$increasePeerRaiting$155(TLRPC.TL_topPeer tL_topPeer, TLRPC.TL_topPeer tL_topPeer2) {
        double d = tL_topPeer.rating;
        double d2 = tL_topPeer2.rating;
        if (d > d2) {
            return -1;
        }
        return d < d2 ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$increasePeerRaiting$156(long j, double d) {
        TLRPC.TL_topPeer tL_topPeer;
        int i = 0;
        while (true) {
            if (i >= this.hints.size()) {
                tL_topPeer = null;
                break;
            }
            tL_topPeer = this.hints.get(i);
            if (tL_topPeer.peer.user_id == j) {
                break;
            } else {
                i++;
            }
        }
        if (tL_topPeer == null) {
            tL_topPeer = new TLRPC.TL_topPeer();
            TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
            tL_topPeer.peer = tL_peerUser;
            tL_peerUser.user_id = j;
            this.hints.add(tL_topPeer);
        }
        double d2 = tL_topPeer.rating;
        double d3 = getMessagesController().ratingDecay;
        Double.isNaN(d3);
        tL_topPeer.rating = d2 + Math.exp(d / d3);
        Collections.sort(this.hints, new Comparator() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda43
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$increasePeerRaiting$155;
                lambda$increasePeerRaiting$155 = MediaDataController.lambda$increasePeerRaiting$155((TLRPC.TL_topPeer) obj, (TLRPC.TL_topPeer) obj2);
                return lambda$increasePeerRaiting$155;
            }
        });
        savePeer(j, 0, tL_topPeer.rating);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadHints, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$increasePeerRaiting$157(final long j) {
        double d;
        int i;
        int i2 = 0;
        try {
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT MAX(mid), MAX(date) FROM messages_v2 WHERE uid = %d AND out = 1", Long.valueOf(j)), new Object[0]);
            if (queryFinalized.next()) {
                i2 = queryFinalized.intValue(0);
                i = queryFinalized.intValue(1);
            } else {
                i = 0;
            }
            queryFinalized.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (i2 > 0 && getUserConfig().ratingLoadTime != 0) {
            d = i - getUserConfig().ratingLoadTime;
            final double d2 = d;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda69
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$increasePeerRaiting$156(j, d2);
                }
            });
        }
        d = 0.0d;
        final double d22 = d;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda69
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$increasePeerRaiting$156(j, d22);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$increaseWebappRating$151(TLRPC.TL_topPeer tL_topPeer, TLRPC.TL_topPeer tL_topPeer2) {
        double d = tL_topPeer.rating;
        double d2 = tL_topPeer2.rating;
        if (d > d2) {
            return -1;
        }
        return d < d2 ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadArchivedStickersCount$71(TLRPC.TL_error tL_error, TLObject tLObject, int i) {
        if (tL_error == null) {
            TLRPC.TL_messages_archivedStickers tL_messages_archivedStickers = (TLRPC.TL_messages_archivedStickers) tLObject;
            this.archivedStickersCount[i] = tL_messages_archivedStickers.count;
            MessagesController.getNotificationsSettings(this.currentAccount).edit().putInt("archivedStickersCount" + i, tL_messages_archivedStickers.count).commit();
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.archivedStickersCountDidLoad, Integer.valueOf(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadArchivedStickersCount$72(final int i, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda145
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadArchivedStickersCount$71(tL_error, tLObject, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAttachMenuBots$3() {
        Exception e;
        long j;
        int i;
        TLRPC.TL_attachMenuBots tL_attachMenuBots;
        SQLiteCursor sQLiteCursor;
        Throwable th;
        TLRPC.TL_attachMenuBots tL_attachMenuBots2;
        int i2;
        long j2;
        int i3;
        long j3;
        long j4 = 0;
        try {
            try {
                sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT data, hash, date FROM attach_menu_bots", new Object[0]);
                try {
                    try {
                        if (sQLiteCursor.next()) {
                            NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
                            if (byteBufferValue != null) {
                                TLRPC.AttachMenuBots TLdeserialize = TLRPC.AttachMenuBots.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), true);
                                r0 = TLdeserialize instanceof TLRPC.TL_attachMenuBots ? (TLRPC.TL_attachMenuBots) TLdeserialize : null;
                                byteBufferValue.reuse();
                            }
                            j4 = sQLiteCursor.longValue(1);
                            i3 = sQLiteCursor.intValue(2);
                        } else {
                            i3 = 0;
                        }
                        if (r0 != null) {
                            try {
                                ArrayList<Long> arrayList = new ArrayList<>();
                                for (int i4 = 0; i4 < r0.bots.size(); i4++) {
                                    arrayList.add(Long.valueOf(r0.bots.get(i4).bot_id));
                                }
                                r0.users.addAll(getMessagesStorage().getUsers(arrayList));
                            } catch (Exception e2) {
                                e = e2;
                                j3 = j4;
                                tL_attachMenuBots = r0;
                                r0 = sQLiteCursor;
                                i = i3;
                                j = j3;
                                FileLog.e((Throwable) e, false);
                                if (r0 == null) {
                                    tL_attachMenuBots2 = tL_attachMenuBots;
                                    i2 = i;
                                    j2 = j;
                                    processLoadedMenuBots(tL_attachMenuBots2, j2, i2, true);
                                }
                                long j5 = j;
                                sQLiteCursor = r0;
                                r0 = tL_attachMenuBots;
                                i3 = i;
                                j4 = j5;
                                sQLiteCursor.dispose();
                                tL_attachMenuBots2 = r0;
                                j2 = j4;
                                i2 = i3;
                                processLoadedMenuBots(tL_attachMenuBots2, j2, i2, true);
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (sQLiteCursor != null) {
                            sQLiteCursor.dispose();
                        }
                        throw th;
                    }
                } catch (Exception e3) {
                    e = e3;
                    j3 = j4;
                    tL_attachMenuBots = r0;
                    r0 = sQLiteCursor;
                    i = 0;
                }
            } catch (Throwable th3) {
                sQLiteCursor = r0;
                th = th3;
            }
        } catch (Exception e4) {
            e = e4;
            j = 0;
            i = 0;
            tL_attachMenuBots = null;
        }
        sQLiteCursor.dispose();
        tL_attachMenuBots2 = r0;
        j2 = j4;
        i2 = i3;
        processLoadedMenuBots(tL_attachMenuBots2, j2, i2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:6:0x0022  */
    /* JADX WARN: Removed duplicated region for block: B:9:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadAttachMenuBots$4(Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        TLRPC.TL_attachMenuBots tL_attachMenuBots;
        long j;
        boolean z;
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (!(tLObject instanceof TLRPC.TL_attachMenuBotsNotModified)) {
            if (tLObject instanceof TLRPC.TL_attachMenuBots) {
                tL_attachMenuBots = (TLRPC.TL_attachMenuBots) tLObject;
                j = tL_attachMenuBots.hash;
                z = false;
            }
            if (runnable == null) {
                AndroidUtilities.runOnUIThread(runnable);
                return;
            }
            return;
        }
        j = 0;
        z = false;
        tL_attachMenuBots = null;
        processLoadedMenuBots(tL_attachMenuBots, j, currentTimeMillis, z);
        if (runnable == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAvatarConstructor$241(TLObject tLObject, SharedPreferences sharedPreferences, boolean z) {
        long currentTimeMillis;
        String str;
        if (tLObject instanceof TLRPC.TL_emojiList) {
            SerializedData serializedData = new SerializedData(tLObject.getObjectSize());
            tLObject.serializeToStream(serializedData);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            TLRPC.TL_emojiList tL_emojiList = (TLRPC.TL_emojiList) tLObject;
            if (z) {
                this.profileAvatarConstructorDefault = tL_emojiList;
                edit.putString("profile", Utilities.bytesToHex(serializedData.toByteArray()));
                currentTimeMillis = System.currentTimeMillis();
                str = "profile_last_check";
            } else {
                this.groupAvatarConstructorDefault = tL_emojiList;
                edit.putString("group", Utilities.bytesToHex(serializedData.toByteArray()));
                currentTimeMillis = System.currentTimeMillis();
                str = "group_last_check";
            }
            edit.putLong(str, currentTimeMillis);
            edit.apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAvatarConstructor$242(final SharedPreferences sharedPreferences, final boolean z, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda89
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadAvatarConstructor$241(tLObject, sharedPreferences, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadBotInfo$197(Utilities.Callback callback, TL_bots.BotInfo botInfo, int i) {
        if (callback != null) {
            callback.run(botInfo);
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.botInfoDidLoad, botInfo, Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadBotInfo$198(Utilities.Callback callback) {
        if (callback != null) {
            callback.run(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadBotInfo$199(long j, long j2, final Utilities.Callback callback, final int i) {
        Runnable runnable;
        try {
            final TL_bots.BotInfo loadBotInfoInternal = loadBotInfoInternal(j, j2);
            if (loadBotInfoInternal != null) {
                runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda157
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$loadBotInfo$197(callback, loadBotInfoInternal, i);
                    }
                };
            } else if (callback == null) {
                return;
            } else {
                runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda158
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.lambda$loadBotInfo$198(Utilities.Callback.this);
                    }
                };
            }
            AndroidUtilities.runOnUIThread(runnable);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadBotKeyboard$195(TLRPC.Message message, MessagesStorage.TopicKey topicKey) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.botKeyboardDidLoad, message, topicKey);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadBotKeyboard$196(final MessagesStorage.TopicKey topicKey) {
        final TLRPC.Message message;
        NativeByteBuffer byteBufferValue;
        try {
            SQLiteCursor queryFinalized = topicKey.topicId != 0 ? getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT info FROM bot_keyboard_topics WHERE uid = %d AND tid = %d", Long.valueOf(topicKey.dialogId), Long.valueOf(topicKey.topicId)), new Object[0]) : getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT info FROM bot_keyboard WHERE uid = %d", Long.valueOf(topicKey.dialogId)), new Object[0]);
            if (!queryFinalized.next() || queryFinalized.isNull(0) || (byteBufferValue = queryFinalized.byteBufferValue(0)) == null) {
                message = null;
            } else {
                message = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                byteBufferValue.reuse();
            }
            queryFinalized.dispose();
            if (message != null) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda17
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$loadBotKeyboard$195(message, topicKey);
                    }
                });
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDraftsIfNeed$185() {
        this.loadingDrafts = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDraftsIfNeed$186() {
        this.loadingDrafts = false;
        UserConfig userConfig = getUserConfig();
        userConfig.draftsLoaded = true;
        userConfig.saveConfig(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadDraftsIfNeed$187(TLObject tLObject, TLRPC.TL_error tL_error) {
        Runnable runnable;
        if (tL_error != null) {
            runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadDraftsIfNeed$185();
                }
            };
        } else {
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
            runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadDraftsIfNeed$186();
                }
            };
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00b2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadFeaturedStickers$55(boolean z) {
        SQLiteCursor sQLiteCursor;
        boolean z2;
        int i;
        ArrayList<TLRPC.StickerSetCovered> arrayList;
        boolean z3;
        long j;
        int i2;
        ArrayList<Long> arrayList2 = new ArrayList<>();
        ArrayList<TLRPC.StickerSetCovered> arrayList3 = null;
        int i3 = 0;
        long j2 = 0;
        try {
            SQLiteDatabase database = getMessagesStorage().getDatabase();
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT data, unread, date, hash, premium FROM stickers_featured WHERE emoji = ");
            sb.append(z ? 1 : 0);
            sb.append(" AND id = ");
            z2 = true;
            sb.append(z ? 2 : 1);
            sQLiteCursor = database.queryFinalized(sb.toString(), new Object[0]);
            try {
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            sQLiteCursor = null;
        }
        if (sQLiteCursor.next()) {
            NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
            if (byteBufferValue != null) {
                ArrayList<TLRPC.StickerSetCovered> arrayList4 = new ArrayList<>();
                try {
                    int readInt32 = byteBufferValue.readInt32(false);
                    for (int i4 = 0; i4 < readInt32; i4++) {
                        arrayList4.add(TLRPC.StickerSetCovered.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false));
                    }
                    byteBufferValue.reuse();
                    arrayList3 = arrayList4;
                } catch (Throwable th3) {
                    th = th3;
                    arrayList3 = arrayList4;
                    i2 = 0;
                    try {
                        FileLog.e(th);
                        if (sQLiteCursor == null) {
                        }
                        i3 = i2;
                        z2 = false;
                        sQLiteCursor.dispose();
                        i = i3;
                        arrayList = arrayList3;
                        long j3 = j2;
                        z3 = z2;
                        j = j3;
                        processLoadedFeaturedStickers(z, arrayList, arrayList2, z3, true, i, j);
                    } finally {
                    }
                }
            }
            NativeByteBuffer byteBufferValue2 = sQLiteCursor.byteBufferValue(1);
            if (byteBufferValue2 != null) {
                int readInt322 = byteBufferValue2.readInt32(false);
                for (int i5 = 0; i5 < readInt322; i5++) {
                    arrayList2.add(Long.valueOf(byteBufferValue2.readInt64(false)));
                }
                byteBufferValue2.reuse();
            }
            i2 = sQLiteCursor.intValue(2);
            try {
                j2 = sQLiteCursor.longValue(3);
            } catch (Throwable th4) {
                th = th4;
                FileLog.e(th);
                if (sQLiteCursor == null) {
                    arrayList = arrayList3;
                    i = i2;
                    j = j2;
                    z3 = false;
                    processLoadedFeaturedStickers(z, arrayList, arrayList2, z3, true, i, j);
                }
                i3 = i2;
                z2 = false;
                sQLiteCursor.dispose();
                i = i3;
                arrayList = arrayList3;
                long j32 = j2;
                z3 = z2;
                j = j32;
                processLoadedFeaturedStickers(z, arrayList, arrayList2, z3, true, i, j);
            }
            if (sQLiteCursor.intValue(4) == 1) {
                i3 = i2;
                sQLiteCursor.dispose();
                i = i3;
                arrayList = arrayList3;
                long j322 = j2;
                z3 = z2;
                j = j322;
                processLoadedFeaturedStickers(z, arrayList, arrayList2, z3, true, i, j);
            }
            i3 = i2;
        }
        z2 = false;
        sQLiteCursor.dispose();
        i = i3;
        arrayList = arrayList3;
        long j3222 = j2;
        z3 = z2;
        j = j3222;
        processLoadedFeaturedStickers(z, arrayList, arrayList2, z3, true, i, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFeaturedStickers$56(boolean z, int i) {
        this.loadingFeaturedStickers[z ? 1 : 0] = false;
        this.featuredStickersLoaded[z ? 1 : 0] = true;
        this.loadFeaturedDate[z ? 1 : 0] = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFeaturedStickers$57(TLObject tLObject, final boolean z, long j) {
        if (tLObject instanceof TLRPC.TL_messages_featuredStickers) {
            TLRPC.TL_messages_featuredStickers tL_messages_featuredStickers = (TLRPC.TL_messages_featuredStickers) tLObject;
            processLoadedFeaturedStickers(z, tL_messages_featuredStickers.sets, tL_messages_featuredStickers.unread, tL_messages_featuredStickers.premium, false, (int) (System.currentTimeMillis() / 1000), tL_messages_featuredStickers.hash);
        } else {
            if (!(tLObject instanceof TLRPC.TL_messages_featuredStickersNotModified)) {
                processLoadedFeaturedStickers(z, null, null, false, false, (int) (System.currentTimeMillis() / 1000), j);
                return;
            }
            final int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda83
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadFeaturedStickers$56(z, currentTimeMillis);
                }
            });
            putFeaturedStickersToCache(z, null, null, currentTimeMillis, j, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFeaturedStickers$58(final boolean z, final long j, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda220
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadFeaturedStickers$57(tLObject, z, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadGroupStickerSet$43(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        this.groupStickerSets.put(tL_messages_stickerSet.set.id, tL_messages_stickerSet);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupStickersDidLoad, Long.valueOf(tL_messages_stickerSet.set.id), tL_messages_stickerSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadGroupStickerSet$44(TLRPC.StickerSet stickerSet) {
        final TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        TLRPC.StickerSet stickerSet2;
        NativeByteBuffer byteBufferValue;
        try {
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT document FROM web_recent_v3 WHERE id = 's_" + stickerSet.id + "'", new Object[0]);
            if (!queryFinalized.next() || queryFinalized.isNull(0) || (byteBufferValue = queryFinalized.byteBufferValue(0)) == null) {
                tL_messages_stickerSet = null;
            } else {
                tL_messages_stickerSet = TLRPC.messages_StickerSet.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                byteBufferValue.reuse();
            }
            queryFinalized.dispose();
            if (tL_messages_stickerSet == null || (stickerSet2 = tL_messages_stickerSet.set) == null || stickerSet2.hash != stickerSet.hash) {
                loadGroupStickerSet(stickerSet, false);
            }
            if (tL_messages_stickerSet == null || tL_messages_stickerSet.set == null) {
                return;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda173
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadGroupStickerSet$43(tL_messages_stickerSet);
                }
            });
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadGroupStickerSet$45(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        this.groupStickerSets.put(tL_messages_stickerSet.set.id, tL_messages_stickerSet);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.groupStickersDidLoad, Long.valueOf(tL_messages_stickerSet.set.id), tL_messages_stickerSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadGroupStickerSet$46(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject != null) {
            final TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda125
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadGroupStickerSet$45(tL_messages_stickerSet);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadHints$142(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, ArrayList arrayList5) {
        getMessagesController().putUsers(arrayList, true);
        getMessagesController().putChats(arrayList2, true);
        this.loading = false;
        this.loaded = true;
        this.hints = arrayList3;
        this.inlineBots = arrayList4;
        this.webapps = arrayList5;
        buildShortcuts();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadHints, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadInlineHints, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadWebappsHints, new Object[0]);
        if (Math.abs(getUserConfig().lastHintsSyncTime - ((int) (System.currentTimeMillis() / 1000))) >= 86400 || BuildVars.DEBUG_PRIVATE_VERSION) {
            loadHints(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadHints$143() {
        Long valueOf;
        ArrayList<Long> arrayList;
        final ArrayList arrayList2 = new ArrayList();
        final ArrayList arrayList3 = new ArrayList();
        final ArrayList arrayList4 = new ArrayList();
        final ArrayList<TLRPC.User> arrayList5 = new ArrayList<>();
        final ArrayList<TLRPC.Chat> arrayList6 = new ArrayList<>();
        long clientUserId = getUserConfig().getClientUserId();
        try {
            ArrayList<Long> arrayList7 = new ArrayList<>();
            ArrayList<Long> arrayList8 = new ArrayList<>();
            int i = 0;
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT did, type, rating FROM chat_hints WHERE 1 ORDER BY rating DESC", new Object[0]);
            while (queryFinalized.next()) {
                long longValue = queryFinalized.longValue(i);
                if (longValue != clientUserId) {
                    int intValue = queryFinalized.intValue(1);
                    TLRPC.TL_topPeer tL_topPeer = new TLRPC.TL_topPeer();
                    long j = clientUserId;
                    tL_topPeer.rating = queryFinalized.doubleValue(2);
                    if (longValue > 0) {
                        TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
                        tL_topPeer.peer = tL_peerUser;
                        tL_peerUser.user_id = longValue;
                        valueOf = Long.valueOf(longValue);
                        arrayList = arrayList7;
                    } else {
                        TLRPC.TL_peerChat tL_peerChat = new TLRPC.TL_peerChat();
                        tL_topPeer.peer = tL_peerChat;
                        long j2 = -longValue;
                        tL_peerChat.chat_id = j2;
                        valueOf = Long.valueOf(j2);
                        arrayList = arrayList8;
                    }
                    arrayList.add(valueOf);
                    if (intValue == 0) {
                        arrayList2.add(tL_topPeer);
                    } else if (intValue == 1) {
                        arrayList3.add(tL_topPeer);
                    } else if (intValue == 2) {
                        arrayList4.add(tL_topPeer);
                    }
                    clientUserId = j;
                    i = 0;
                }
            }
            queryFinalized.dispose();
            if (!arrayList7.isEmpty()) {
                getMessagesStorage().getUsersInternal(arrayList7, arrayList5);
            }
            if (!arrayList8.isEmpty()) {
                getMessagesStorage().getChatsInternal(TextUtils.join(",", arrayList8), arrayList6);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda111
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadHints$142(arrayList5, arrayList6, arrayList2, arrayList3, arrayList4);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadHints$144() {
        getUserConfig().suggestContacts = true;
        getUserConfig().lastHintsSyncTime = (int) (System.currentTimeMillis() / 1000);
        getUserConfig().saveConfig(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadHints$145(TLRPC.TL_contacts_topPeers tL_contacts_topPeers) {
        try {
            getMessagesStorage().getDatabase().executeFast("DELETE FROM chat_hints WHERE 1").stepThis().dispose();
            getMessagesStorage().getDatabase().beginTransaction();
            getMessagesStorage().putUsersAndChats(tL_contacts_topPeers.users, tL_contacts_topPeers.chats, false, false);
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO chat_hints VALUES(?, ?, ?, ?)");
            for (int i = 0; i < tL_contacts_topPeers.categories.size(); i++) {
                TLRPC.TL_topPeerCategoryPeers tL_topPeerCategoryPeers = tL_contacts_topPeers.categories.get(i);
                TLRPC.TopPeerCategory topPeerCategory = tL_topPeerCategoryPeers.category;
                int i2 = topPeerCategory instanceof TLRPC.TL_topPeerCategoryBotsInline ? 1 : topPeerCategory instanceof TLRPC.TL_topPeerCategoryBotsApp ? 2 : 0;
                for (int i3 = 0; i3 < tL_topPeerCategoryPeers.peers.size(); i3++) {
                    TLRPC.TL_topPeer tL_topPeer = tL_topPeerCategoryPeers.peers.get(i3);
                    executeFast.requery();
                    executeFast.bindLong(1, MessageObject.getPeerId(tL_topPeer.peer));
                    executeFast.bindInteger(2, i2);
                    executeFast.bindDouble(3, tL_topPeer.rating);
                    executeFast.bindInteger(4, 0);
                    executeFast.step();
                }
            }
            executeFast.dispose();
            getMessagesStorage().getDatabase().commitTransaction();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda33
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadHints$144();
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadHints$146(TLObject tLObject) {
        final TLRPC.TL_contacts_topPeers tL_contacts_topPeers = (TLRPC.TL_contacts_topPeers) tLObject;
        getMessagesController().putUsers(tL_contacts_topPeers.users, false);
        getMessagesController().putChats(tL_contacts_topPeers.chats, false);
        for (int i = 0; i < tL_contacts_topPeers.categories.size(); i++) {
            TLRPC.TL_topPeerCategoryPeers tL_topPeerCategoryPeers = tL_contacts_topPeers.categories.get(i);
            TLRPC.TopPeerCategory topPeerCategory = tL_topPeerCategoryPeers.category;
            if (topPeerCategory instanceof TLRPC.TL_topPeerCategoryBotsInline) {
                this.inlineBots = tL_topPeerCategoryPeers.peers;
                getUserConfig().botRatingLoadTime = (int) (System.currentTimeMillis() / 1000);
            } else {
                boolean z = topPeerCategory instanceof TLRPC.TL_topPeerCategoryBotsApp;
                ArrayList<TLRPC.TL_topPeer> arrayList = tL_topPeerCategoryPeers.peers;
                if (z) {
                    this.webapps = arrayList;
                    getUserConfig().webappRatingLoadTime = (int) (System.currentTimeMillis() / 1000);
                } else {
                    this.hints = arrayList;
                    long clientUserId = getUserConfig().getClientUserId();
                    int i2 = 0;
                    while (true) {
                        if (i2 >= this.hints.size()) {
                            break;
                        }
                        if (this.hints.get(i2).peer.user_id == clientUserId) {
                            this.hints.remove(i2);
                            break;
                        }
                        i2++;
                    }
                    getUserConfig().ratingLoadTime = (int) (System.currentTimeMillis() / 1000);
                }
            }
        }
        getUserConfig().saveConfig(false);
        buildShortcuts();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadHints, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadInlineHints, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadWebappsHints, new Object[0]);
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadHints$145(tL_contacts_topPeers);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadHints$147() {
        getUserConfig().suggestContacts = false;
        getUserConfig().lastHintsSyncTime = (int) (System.currentTimeMillis() / 1000);
        getUserConfig().saveConfig(false);
        clearTopPeers();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadHints$148(final TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject instanceof TLRPC.TL_contacts_topPeers) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadHints$146(tLObject);
                }
            });
        } else if (tLObject instanceof TLRPC.TL_contacts_topPeersDisabled) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadHints$147();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMedia$124(long j, int i, int i2, int i3, int i4, long j2, int i5, boolean z, int i6, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            getMessagesController().removeDeletedMessagesFromArray(j, messages_messages.messages);
            int size = messages_messages.messages.size();
            processLoadedMedia(messages_messages, j, i2, i3, i, i4, j2, 0, i5, z, i == 0 ? size == 0 : size <= 1, i6);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMusic$139(long j, ArrayList arrayList, ArrayList arrayList2) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.musicDidLoad, Long.valueOf(j), arrayList, arrayList2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMusic$140(final long j, long j2, long j3) {
        SQLiteCursor queryFinalized;
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        int i = 0;
        while (i < 2) {
            ArrayList arrayList3 = i == 0 ? arrayList : arrayList2;
            if (i == 0) {
                try {
                    queryFinalized = !DialogObject.isEncryptedDialog(j) ? getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid < %d AND type = %d ORDER BY date DESC, mid DESC LIMIT 1000", Long.valueOf(j), Long.valueOf(j2), 4), new Object[0]) : getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid > %d AND type = %d ORDER BY date DESC, mid DESC LIMIT 1000", Long.valueOf(j), Long.valueOf(j2), 4), new Object[0]);
                } catch (Exception e) {
                    e = e;
                    FileLog.e(e);
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda127
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaDataController.this.lambda$loadMusic$139(j, arrayList, arrayList2);
                        }
                    });
                }
            } else {
                queryFinalized = !DialogObject.isEncryptedDialog(j) ? getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid > %d AND type = %d ORDER BY date DESC, mid DESC LIMIT 1000", Long.valueOf(j), Long.valueOf(j3), 4), new Object[0]) : getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid < %d AND type = %d ORDER BY date DESC, mid DESC LIMIT 1000", Long.valueOf(j), Long.valueOf(j3), 4), new Object[0]);
            }
            while (queryFinalized.next()) {
                NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(0);
                if (byteBufferValue != null) {
                    TLRPC.Message TLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                    TLdeserialize.readAttachPath(byteBufferValue, getUserConfig().clientUserId);
                    byteBufferValue.reuse();
                    if (MessageObject.isMusicMessage(TLdeserialize)) {
                        TLdeserialize.id = queryFinalized.intValue(1);
                        try {
                            TLdeserialize.dialog_id = j;
                            try {
                                arrayList3.add(0, new MessageObject(this.currentAccount, TLdeserialize, false, true));
                            } catch (Exception e2) {
                                e = e2;
                                FileLog.e(e);
                                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda127
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        MediaDataController.this.lambda$loadMusic$139(j, arrayList, arrayList2);
                                    }
                                });
                            }
                        } catch (Exception e3) {
                            e = e3;
                        }
                    }
                }
            }
            queryFinalized.dispose();
            i++;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda127
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadMusic$139(j, arrayList, arrayList2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPinnedMessageInternal$164(long j, long j2, TLRPC.TL_channels_getMessages tL_channels_getMessages, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            removeEmptyMessages(messages_messages.messages);
            if (!messages_messages.messages.isEmpty()) {
                getMessagesController().getChat(Long.valueOf(j));
                ImageLoader.saveMessagesThumbs(messages_messages.messages);
                broadcastPinnedMessage(messages_messages.messages, messages_messages.users, messages_messages.chats, false, false);
                getMessagesStorage().putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
                savePinnedMessages(j2, messages_messages.messages);
                return;
            }
        }
        getMessagesStorage().updatePinnedMessages(j2, tL_channels_getMessages.id, false, -1, 0, false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPinnedMessageInternal$165(long j, TLRPC.TL_messages_getMessages tL_messages_getMessages, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            removeEmptyMessages(messages_messages.messages);
            if (!messages_messages.messages.isEmpty()) {
                ImageLoader.saveMessagesThumbs(messages_messages.messages);
                broadcastPinnedMessage(messages_messages.messages, messages_messages.users, messages_messages.chats, false, false);
                getMessagesStorage().putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
                savePinnedMessages(j, messages_messages.messages);
                return;
            }
        }
        getMessagesStorage().updatePinnedMessages(j, tL_messages_getMessages.id, false, -1, 0, false, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPinnedMessages$161(long j) {
        this.loadingPinnedMessages.remove(j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPinnedMessages$162(int i, TLRPC.TL_messages_search tL_messages_search, final long j, int i2, TLObject tLObject, TLRPC.TL_error tL_error) {
        int i3;
        int i4;
        boolean z;
        ArrayList<Integer> arrayList = new ArrayList<>();
        HashMap<Integer, MessageObject> hashMap = new HashMap<>();
        if (tLObject instanceof TLRPC.messages_Messages) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            LongSparseArray longSparseArray = new LongSparseArray();
            for (int i5 = 0; i5 < messages_messages.users.size(); i5++) {
                TLRPC.User user = messages_messages.users.get(i5);
                longSparseArray.put(user.id, user);
            }
            LongSparseArray longSparseArray2 = new LongSparseArray();
            for (int i6 = 0; i6 < messages_messages.chats.size(); i6++) {
                TLRPC.Chat chat = messages_messages.chats.get(i6);
                longSparseArray2.put(chat.id, chat);
            }
            getMessagesStorage().putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
            getMessagesController().putUsers(messages_messages.users, false);
            getMessagesController().putChats(messages_messages.chats, false);
            int size = messages_messages.messages.size();
            for (int i7 = 0; i7 < size; i7++) {
                TLRPC.Message message = messages_messages.messages.get(i7);
                if (!(message instanceof TLRPC.TL_messageService) && !(message instanceof TLRPC.TL_messageEmpty)) {
                    arrayList.add(Integer.valueOf(message.id));
                    hashMap.put(Integer.valueOf(message.id), new MessageObject(this.currentAccount, message, longSparseArray, longSparseArray2, false, false));
                }
            }
            if (i != 0 && arrayList.isEmpty()) {
                arrayList.add(Integer.valueOf(i));
            }
            boolean z2 = messages_messages.messages.size() < tL_messages_search.limit;
            i4 = Math.max(messages_messages.count, messages_messages.messages.size());
            z = z2;
        } else {
            if (i != 0) {
                arrayList.add(Integer.valueOf(i));
                i3 = 1;
            } else {
                i3 = 0;
            }
            i4 = i3;
            z = false;
        }
        getMessagesStorage().updatePinnedMessages(j, arrayList, true, i4, i2, z, hashMap);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda123
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadPinnedMessages$161(j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPinnedMessages$163(long j, long j2, ArrayList arrayList) {
        loadPinnedMessageInternal(j, j2, arrayList, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPremiumPromo$7() {
        TLRPC.TL_help_premiumPromo tL_help_premiumPromo;
        SQLiteCursor sQLiteCursor;
        SQLiteCursor sQLiteCursor2 = null;
        r1 = null;
        r1 = null;
        TLRPC.TL_help_premiumPromo tL_help_premiumPromo2 = null;
        sQLiteCursor2 = null;
        int i = 0;
        try {
            try {
                sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT data, date FROM premium_promo", new Object[0]);
            } catch (Exception e) {
                e = e;
                tL_help_premiumPromo = null;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            if (sQLiteCursor.next()) {
                NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
                if (byteBufferValue != null) {
                    tL_help_premiumPromo2 = TLRPC.TL_help_premiumPromo.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), true);
                    byteBufferValue.reuse();
                }
                i = sQLiteCursor.intValue(1);
            }
        } catch (Exception e2) {
            e = e2;
            tL_help_premiumPromo = tL_help_premiumPromo2;
            sQLiteCursor2 = sQLiteCursor;
            FileLog.e((Throwable) e, false);
            if (sQLiteCursor2 != null) {
                TLRPC.TL_help_premiumPromo tL_help_premiumPromo3 = tL_help_premiumPromo;
                sQLiteCursor = sQLiteCursor2;
                tL_help_premiumPromo2 = tL_help_premiumPromo3;
                sQLiteCursor.dispose();
                tL_help_premiumPromo = tL_help_premiumPromo2;
            }
            processLoadedPremiumPromo(tL_help_premiumPromo, i, true);
        } catch (Throwable th2) {
            th = th2;
            sQLiteCursor2 = sQLiteCursor;
            if (sQLiteCursor2 != null) {
                sQLiteCursor2.dispose();
            }
            throw th;
        }
        sQLiteCursor.dispose();
        tL_help_premiumPromo = tL_help_premiumPromo2;
        processLoadedPremiumPromo(tL_help_premiumPromo, i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPremiumPromo$8(TLObject tLObject, TLRPC.TL_error tL_error) {
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (tLObject instanceof TLRPC.TL_help_premiumPromo) {
            processLoadedPremiumPromo((TLRPC.TL_help_premiumPromo) tLObject, currentTimeMillis, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadReactions$11(List list, int i, int i2) {
        processLoadedReactions(list, i, i2, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0085  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadReactions$12() {
        SQLiteCursor sQLiteCursor;
        Throwable th;
        final ArrayList arrayList;
        final int i;
        Exception e;
        SQLiteCursor sQLiteCursor2;
        int i2;
        ArrayList arrayList2 = null;
        final int i3 = 0;
        try {
            sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT data, hash, date FROM reactions", new Object[0]);
            try {
                try {
                } catch (Throwable th2) {
                    th = th2;
                    if (sQLiteCursor != null) {
                        sQLiteCursor.dispose();
                    }
                    throw th;
                }
            } catch (Exception e2) {
                i = 0;
                arrayList = arrayList2;
                e = e2;
            }
        } catch (Exception e3) {
            arrayList = null;
            i = 0;
            e = e3;
            sQLiteCursor = null;
        } catch (Throwable th3) {
            sQLiteCursor = null;
            th = th3;
            if (sQLiteCursor != null) {
            }
            throw th;
        }
        if (sQLiteCursor.next()) {
            NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
            if (byteBufferValue != null) {
                int readInt32 = byteBufferValue.readInt32(false);
                ArrayList arrayList3 = new ArrayList(readInt32);
                for (int i4 = 0; i4 < readInt32; i4++) {
                    try {
                        arrayList3.add(TLRPC.TL_availableReaction.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), true));
                    } catch (Exception e4) {
                        e = e4;
                        arrayList = arrayList3;
                        i = 0;
                        FileLog.e((Throwable) e, false);
                        if (sQLiteCursor != null) {
                            arrayList2 = arrayList;
                            i3 = i;
                            sQLiteCursor2 = sQLiteCursor;
                            i2 = 0;
                            sQLiteCursor2.dispose();
                            arrayList = arrayList2;
                            i = i3;
                            i3 = i2;
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda218
                            @Override // java.lang.Runnable
                            public final void run() {
                                MediaDataController.this.lambda$loadReactions$11(arrayList, i, i3);
                            }
                        });
                    }
                }
                byteBufferValue.reuse();
                arrayList2 = arrayList3;
            }
            int intValue = sQLiteCursor.intValue(1);
            try {
                i2 = sQLiteCursor.intValue(2);
                i3 = intValue;
                sQLiteCursor2 = sQLiteCursor;
            } catch (Exception e5) {
                arrayList = arrayList2;
                e = e5;
                i = intValue;
                FileLog.e((Throwable) e, false);
                if (sQLiteCursor != null) {
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda218
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$loadReactions$11(arrayList, i, i3);
                    }
                });
            }
            sQLiteCursor2.dispose();
            arrayList = arrayList2;
            i = i3;
            i3 = i2;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda218
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadReactions$11(arrayList, i, i3);
                }
            });
        }
        sQLiteCursor2 = sQLiteCursor;
        i2 = 0;
        sQLiteCursor2.dispose();
        arrayList = arrayList2;
        i = i3;
        i3 = i2;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda218
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadReactions$11(arrayList, i, i3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadReactions$13(TLObject tLObject) {
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (tLObject instanceof TLRPC.TL_messages_availableReactionsNotModified) {
            processLoadedReactions(null, 0, currentTimeMillis, false);
        } else if (tLObject instanceof TLRPC.TL_messages_availableReactions) {
            TLRPC.TL_messages_availableReactions tL_messages_availableReactions = (TLRPC.TL_messages_availableReactions) tLObject;
            processLoadedReactions(tL_messages_availableReactions.reactions, tL_messages_availableReactions.hash, currentTimeMillis, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadReactions$14(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda149
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadReactions$13(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRecentAndTopReactions$235(TLRPC.TL_error tL_error, TLObject tLObject, SharedPreferences sharedPreferences, boolean[] zArr) {
        if (tL_error == null) {
            if (tLObject instanceof TLRPC.TL_messages_reactions) {
                TLRPC.TL_messages_reactions tL_messages_reactions = (TLRPC.TL_messages_reactions) tLObject;
                this.recentReactions.clear();
                this.recentReactions.addAll(tL_messages_reactions.reactions);
                saveReactionsToPref(sharedPreferences, tL_messages_reactions.hash, tL_messages_reactions.reactions);
            }
            boolean z = tLObject instanceof TLRPC.TL_messages_reactionsNotModified;
        }
        zArr[0] = true;
        if (zArr[1]) {
            this.loadingRecentReactions = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRecentAndTopReactions$236(final SharedPreferences sharedPreferences, final boolean[] zArr, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda68
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadRecentAndTopReactions$235(tL_error, tLObject, sharedPreferences, zArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRecentAndTopReactions$237(TLRPC.TL_error tL_error, TLObject tLObject, SharedPreferences sharedPreferences, boolean[] zArr) {
        if (tL_error == null) {
            if (tLObject instanceof TLRPC.TL_messages_reactions) {
                TLRPC.TL_messages_reactions tL_messages_reactions = (TLRPC.TL_messages_reactions) tLObject;
                this.topReactions.clear();
                this.topReactions.addAll(tL_messages_reactions.reactions);
                saveReactionsToPref(sharedPreferences, tL_messages_reactions.hash, tL_messages_reactions.reactions);
            }
            boolean z = tLObject instanceof TLRPC.TL_messages_reactionsNotModified;
        }
        zArr[1] = true;
        if (zArr[0]) {
            this.loadingRecentReactions = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRecentAndTopReactions$238(final SharedPreferences sharedPreferences, final boolean[] zArr, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda117
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadRecentAndTopReactions$237(tL_error, tLObject, sharedPreferences, zArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$loadRecents$48(boolean z, ArrayList arrayList, int i) {
        if (z) {
            this.recentGifs = arrayList;
            this.loadingRecentGifs = false;
            this.recentGifsLoaded = true;
        } else {
            this.recentStickers[i] = arrayList;
            this.loadingRecentStickers[i] = false;
            this.recentStickersLoaded[i] = true;
        }
        if (i == 3) {
            preloadNextGreetingsSticker();
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.recentDocumentsDidLoad, Boolean.valueOf(z), Integer.valueOf(i));
        loadRecents(i, z, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRecents$49(final boolean z, final int i) {
        int i2;
        NativeByteBuffer byteBufferValue;
        if (z) {
            i2 = 2;
        } else {
            i2 = 3;
            if (i != 0) {
                if (i == 1) {
                    i2 = 4;
                } else if (i == 3) {
                    i2 = 6;
                } else {
                    i2 = 7;
                    if (i != 5) {
                        i2 = i == 7 ? 8 : 5;
                    }
                }
            }
        }
        try {
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT document FROM web_recent_v3 WHERE type = " + i2 + " ORDER BY date DESC", new Object[0]);
            final ArrayList arrayList = new ArrayList();
            while (queryFinalized.next()) {
                if (!queryFinalized.isNull(0) && (byteBufferValue = queryFinalized.byteBufferValue(0)) != null) {
                    TLRPC.Document TLdeserialize = TLRPC.Document.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                    if (TLdeserialize != null) {
                        arrayList.add(TLdeserialize);
                    }
                    byteBufferValue.reuse();
                }
            }
            queryFinalized.dispose();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda74
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadRecents$48(z, arrayList, i);
                }
            });
        } catch (Throwable th) {
            getMessagesStorage().checkSQLException(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRecents$50(int i, TLObject tLObject, TLRPC.TL_error tL_error) {
        processLoadedRecentDocuments(i, tLObject instanceof TLRPC.TL_messages_savedGifs ? ((TLRPC.TL_messages_savedGifs) tLObject).gifs : null, true, 0, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRecents$51(int i, TLObject tLObject, TLRPC.TL_error tL_error) {
        ArrayList<TLRPC.Document> arrayList;
        if (i == 3 || i == 7) {
            if (tLObject instanceof TLRPC.TL_messages_stickers) {
                arrayList = ((TLRPC.TL_messages_stickers) tLObject).stickers;
            }
            arrayList = null;
        } else if (i == 2) {
            if (tLObject instanceof TLRPC.TL_messages_favedStickers) {
                arrayList = ((TLRPC.TL_messages_favedStickers) tLObject).stickers;
            }
            arrayList = null;
        } else {
            if (tLObject instanceof TLRPC.TL_messages_recentStickers) {
                arrayList = ((TLRPC.TL_messages_recentStickers) tLObject).stickers;
            }
            arrayList = null;
        }
        processLoadedRecentDocuments(i, arrayList, false, 0, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRepliesOfDraftReplies$0(ArrayList arrayList) {
        try {
            ArrayList<Long> arrayList2 = new ArrayList<>();
            ArrayList<Long> arrayList3 = new ArrayList<>();
            LongSparseArray longSparseArray = new LongSparseArray();
            LongSparseArray longSparseArray2 = new LongSparseArray();
            for (int i = 0; i < arrayList.size(); i++) {
                try {
                    MessagesStorage.addReplyMessages((TLRPC.Message) arrayList.get(i), longSparseArray, longSparseArray2);
                } catch (Exception e) {
                    getMessagesStorage().checkSQLException(e);
                }
            }
            getMessagesStorage().loadReplyMessages(longSparseArray, longSparseArray2, arrayList2, arrayList3, 0);
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadReplyIcons$243(TLObject tLObject, SharedPreferences sharedPreferences) {
        if (tLObject instanceof TLRPC.TL_emojiList) {
            SerializedData serializedData = new SerializedData(tLObject.getObjectSize());
            tLObject.serializeToStream(serializedData);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            this.replyIconsDefault = (TLRPC.TL_emojiList) tLObject;
            edit.putString("replyicons", Utilities.bytesToHex(serializedData.toByteArray()));
            edit.putLong("replyicons_last_check", System.currentTimeMillis());
            edit.apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadReplyIcons$244(final SharedPreferences sharedPreferences, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadReplyIcons$243(tLObject, sharedPreferences);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadReplyMessagesForMessages$170(Timer.Task task, long j, ArrayList arrayList) {
        Timer.done(task);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.replyMessagesDidLoad, Long.valueOf(j), arrayList, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadReplyMessagesForMessages$171(Timer.Task task, Timer timer, ArrayList arrayList, final long j, LongSparseArray longSparseArray, Runnable runnable) {
        Timer.done(task);
        Timer.Task start = Timer.start(timer, "loadReplyMessagesForMessages: (encrypted) loading those messages from storage");
        try {
            final ArrayList arrayList2 = new ArrayList();
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid, m.date, r.random_id FROM randoms_v2 as r INNER JOIN messages_v2 as m ON r.mid = m.mid AND r.uid = m.uid WHERE r.random_id IN(%s)", TextUtils.join(",", arrayList)), new Object[0]);
            while (queryFinalized.next()) {
                NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(0);
                if (byteBufferValue != null) {
                    TLRPC.Message TLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                    TLdeserialize.readAttachPath(byteBufferValue, getUserConfig().clientUserId);
                    byteBufferValue.reuse();
                    TLdeserialize.id = queryFinalized.intValue(1);
                    TLdeserialize.date = queryFinalized.intValue(2);
                    TLdeserialize.dialog_id = j;
                    long longValue = queryFinalized.longValue(3);
                    ArrayList arrayList3 = (ArrayList) longSparseArray.get(longValue);
                    longSparseArray.remove(longValue);
                    if (arrayList3 != null) {
                        try {
                            MessageObject messageObject = new MessageObject(this.currentAccount, TLdeserialize, false, false);
                            arrayList2.add(messageObject);
                            for (int i = 0; i < arrayList3.size(); i++) {
                                MessageObject messageObject2 = (MessageObject) arrayList3.get(i);
                                messageObject2.replyMessageObject = messageObject;
                                messageObject2.applyTimestampsHighlightForReplyMsg();
                                messageObject2.messageOwner.reply_to = new TLRPC.TL_messageReplyHeader();
                                TLRPC.MessageReplyHeader messageReplyHeader = messageObject2.messageOwner.reply_to;
                                messageReplyHeader.flags |= 16;
                                messageReplyHeader.reply_to_msg_id = messageObject.getRealId();
                            }
                        } catch (Exception e) {
                            e = e;
                            FileLog.e(e);
                            return;
                        }
                    }
                }
            }
            queryFinalized.dispose();
            if (longSparseArray.size() != 0) {
                for (int i2 = 0; i2 < longSparseArray.size(); i2++) {
                    ArrayList arrayList4 = (ArrayList) longSparseArray.valueAt(i2);
                    for (int i3 = 0; i3 < arrayList4.size(); i3++) {
                        TLRPC.MessageReplyHeader messageReplyHeader2 = ((MessageObject) arrayList4.get(i3)).messageOwner.reply_to;
                        if (messageReplyHeader2 != null) {
                            messageReplyHeader2.reply_to_random_id = 0L;
                        }
                    }
                }
            }
            Timer.done(start);
            final Timer.Task start2 = Timer.start(timer, "loadReplyMessagesForMessages (encrypted) runOnUIThread: posting notification");
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda148
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadReplyMessagesForMessages$170(start2, j, arrayList2);
                }
            });
            if (runnable != null) {
                runnable.run();
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadReplyMessagesForMessages$172(AtomicInteger atomicInteger, Runnable runnable) {
        if (atomicInteger.decrementAndGet() != 0 || runnable == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadReplyMessagesForMessages$173(TLRPC.TL_error tL_error, TLRPC.messages_Messages messages_messages, long j, long j2, LongSparseArray longSparseArray, boolean z, TLObject tLObject, TLRPC.TL_error tL_error2) {
        if (tL_error == null) {
            TLRPC.messages_Messages messages_messages2 = (TLRPC.messages_Messages) tLObject;
            messages_messages.messages.addAll(messages_messages2.messages);
            messages_messages.users.addAll(messages_messages2.users);
            messages_messages.chats.addAll(messages_messages2.chats);
            for (int i = 0; i < messages_messages.messages.size(); i++) {
                TLRPC.Message message = messages_messages.messages.get(i);
                if (message.dialog_id == 0) {
                    message.dialog_id = j;
                }
            }
            MessageObject.fixMessagePeer(messages_messages.messages, j2);
            ImageLoader.saveMessagesThumbs(messages_messages.messages);
            broadcastReplyMessages(messages_messages.messages, longSparseArray, messages_messages.users, messages_messages.chats, j, false);
            getMessagesStorage().putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
            saveReplyMessages(longSparseArray, messages_messages.messages, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$loadReplyMessagesForMessages$174(Timer.Task task, TLRPC.TL_messages_getScheduledMessages tL_messages_getScheduledMessages, final long j, final long j2, final LongSparseArray longSparseArray, final boolean z, Timer timer, AtomicInteger atomicInteger, Runnable runnable, TLObject tLObject, final TLRPC.TL_error tL_error) {
        TLRPC.TL_messages_getMessages tL_messages_getMessages;
        Timer.done(task);
        if (tL_error == null) {
            final TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            int i = 0;
            while (i < messages_messages.messages.size()) {
                if (messages_messages.messages.get(i) instanceof TLRPC.TL_messageEmpty) {
                    messages_messages.messages.remove(i);
                    i--;
                }
                i++;
            }
            if (messages_messages.messages.size() < tL_messages_getScheduledMessages.id.size()) {
                if (j != 0) {
                    TLRPC.TL_channels_getMessages tL_channels_getMessages = new TLRPC.TL_channels_getMessages();
                    tL_channels_getMessages.channel = getMessagesController().getInputChannel(j);
                    tL_channels_getMessages.id = tL_messages_getScheduledMessages.id;
                    tL_messages_getMessages = tL_channels_getMessages;
                } else {
                    TLRPC.TL_messages_getMessages tL_messages_getMessages2 = new TLRPC.TL_messages_getMessages();
                    tL_messages_getMessages2.id = tL_messages_getScheduledMessages.id;
                    tL_messages_getMessages = tL_messages_getMessages2;
                }
                getConnectionsManager().sendRequest(tL_messages_getMessages, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda25
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC.TL_error tL_error2) {
                        MediaDataController.this.lambda$loadReplyMessagesForMessages$173(tL_error, messages_messages, j2, j, longSparseArray, z, tLObject2, tL_error2);
                    }
                });
            } else {
                for (int i2 = 0; i2 < messages_messages.messages.size(); i2++) {
                    TLRPC.Message message = messages_messages.messages.get(i2);
                    if (message.dialog_id == 0) {
                        message.dialog_id = j2;
                    }
                }
                MessageObject.fixMessagePeer(messages_messages.messages, j);
                ImageLoader.saveMessagesThumbs(messages_messages.messages);
                broadcastReplyMessages(messages_messages.messages, longSparseArray, messages_messages.users, messages_messages.chats, j2, false);
                getMessagesStorage().putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
                saveReplyMessages(longSparseArray, messages_messages.messages, z);
            }
        } else {
            Timer.log(timer, "getScheduledMessages error: " + tL_error.code + " " + tL_error.text);
        }
        if (atomicInteger.decrementAndGet() != 0 || runnable == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadReplyMessagesForMessages$175(Timer.Task task, long j, long j2, LongSparseArray longSparseArray, boolean z, Timer timer, AtomicInteger atomicInteger, Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        Timer.done(task);
        if (tL_error == null) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            for (int i = 0; i < messages_messages.messages.size(); i++) {
                TLRPC.Message message = messages_messages.messages.get(i);
                if (message.dialog_id == 0) {
                    message.dialog_id = j;
                }
            }
            MessageObject.fixMessagePeer(messages_messages.messages, j2);
            ImageLoader.saveMessagesThumbs(messages_messages.messages);
            broadcastReplyMessages(messages_messages.messages, longSparseArray, messages_messages.users, messages_messages.chats, j, false);
            getMessagesStorage().putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
            saveReplyMessages(longSparseArray, messages_messages.messages, z);
        } else {
            Timer.log(timer, "channels.getMessages error: " + tL_error.code + " " + tL_error.text);
        }
        if (atomicInteger.decrementAndGet() != 0 || runnable == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadReplyMessagesForMessages$176(Timer.Task task, long j, LongSparseArray longSparseArray, boolean z, Timer timer, AtomicInteger atomicInteger, Runnable runnable, TLObject tLObject, TLRPC.TL_error tL_error) {
        Timer.done(task);
        if (tL_error == null) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            for (int i = 0; i < messages_messages.messages.size(); i++) {
                TLRPC.Message message = messages_messages.messages.get(i);
                if (message.dialog_id == 0) {
                    message.dialog_id = j;
                }
            }
            ImageLoader.saveMessagesThumbs(messages_messages.messages);
            broadcastReplyMessages(messages_messages.messages, longSparseArray, messages_messages.users, messages_messages.chats, j, false);
            getMessagesStorage().putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
            saveReplyMessages(longSparseArray, messages_messages.messages, z);
        } else {
            Timer.log(timer, "messages.getMessages error: " + tL_error.code + " " + tL_error.text);
        }
        if (atomicInteger.decrementAndGet() != 0 || runnable == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0101 A[Catch: Exception -> 0x003a, TryCatch #0 {Exception -> 0x003a, blocks: (B:3:0x000f, B:5:0x0029, B:8:0x0036, B:13:0x003e, B:17:0x0068, B:21:0x0176, B:29:0x015f, B:31:0x009a, B:34:0x00fb, B:36:0x0101, B:39:0x0107, B:41:0x0137, B:42:0x013c, B:44:0x0144, B:46:0x0153, B:51:0x0159, B:53:0x00cc, B:58:0x0187, B:60:0x0192, B:61:0x0199, B:63:0x019f, B:64:0x01b1, B:66:0x01c1, B:68:0x01cb, B:70:0x01d1, B:72:0x0219, B:73:0x02c5, B:75:0x02ca, B:79:0x0234, B:81:0x0279, B:83:0x0287, B:85:0x02c1, B:88:0x02d2, B:91:0x02dd), top: B:2:0x000f }] */
    /* JADX WARN: Type inference failed for: r14v19 */
    /* JADX WARN: Type inference failed for: r14v20, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r14v22 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:41:0x0156 -> B:28:0x0157). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadReplyMessagesForMessages$177(Timer.Task task, LongSparseArray longSparseArray, final AtomicInteger atomicInteger, final Runnable runnable, int i, final Timer timer, final LongSparseArray longSparseArray2, LongSparseArray longSparseArray3, final boolean z, final long j) {
        ArrayList<TLRPC.Chat> arrayList;
        int i2;
        int i3;
        int i4;
        int sendRequest;
        ConnectionsManager connectionsManager;
        ArrayList<TLRPC.Chat> arrayList2;
        int i5;
        int i6;
        ArrayList<TLRPC.Chat> arrayList3;
        int i7;
        String str;
        ArrayList arrayList4;
        ?? r14;
        SQLiteCursor sQLiteCursor;
        int i8;
        LongSparseArray longSparseArray4 = longSparseArray2;
        LongSparseArray longSparseArray5 = longSparseArray3;
        long j2 = j;
        Timer.done(task);
        try {
            getMessagesController().getStoriesController().fillMessagesWithStories(longSparseArray, new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda19
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.lambda$loadReplyMessagesForMessages$172(atomicInteger, runnable);
                }
            }, i, timer);
            if (longSparseArray2.isEmpty()) {
                Timer.log(timer, "loadReplyMessagesForMessages: empty replyMessageOwners");
                if (atomicInteger.decrementAndGet() != 0 || runnable == null) {
                    return;
                }
                AndroidUtilities.runOnUIThread(runnable);
                return;
            }
            Timer.Task start = Timer.start(timer, "loadReplyMessagesForMessages: getting reply messages");
            ArrayList<TLRPC.Message> arrayList5 = new ArrayList<>();
            ArrayList<TLRPC.User> arrayList6 = new ArrayList<>();
            ArrayList<TLRPC.Chat> arrayList7 = new ArrayList<>();
            ArrayList<Long> arrayList8 = new ArrayList<>();
            ArrayList arrayList9 = new ArrayList();
            int size = longSparseArray2.size();
            int i9 = 0;
            while (true) {
                String str2 = ",";
                if (i9 >= size) {
                    break;
                }
                long keyAt = longSparseArray4.keyAt(i9);
                ArrayList arrayList10 = (ArrayList) longSparseArray5.get(keyAt);
                if (arrayList10 == null) {
                    arrayList2 = arrayList7;
                    i5 = size;
                    i6 = 1;
                } else {
                    int i10 = 0;
                    for (int i11 = 2; i10 < i11; i11 = 2) {
                        if (i10 == 1 && !z) {
                            str = str2;
                            arrayList3 = arrayList7;
                            i7 = size;
                            arrayList4 = arrayList10;
                            i8 = i10;
                        } else if (i10 == 1) {
                            i7 = size;
                            arrayList3 = arrayList7;
                            str = str2;
                            arrayList4 = arrayList10;
                            SQLiteCursor sQLiteCursor2 = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid, date, uid FROM scheduled_messages_v2 WHERE mid IN(%s) AND uid = %d", TextUtils.join(str2, arrayList10), Long.valueOf(j)), new Object[0]);
                            r14 = 0;
                            sQLiteCursor = sQLiteCursor2;
                            while (sQLiteCursor.next()) {
                                NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(r14);
                                if (byteBufferValue != 0) {
                                    TLRPC.Message TLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(r14), r14);
                                    int i12 = i10;
                                    TLdeserialize.readAttachPath(byteBufferValue, getUserConfig().clientUserId);
                                    byteBufferValue.reuse();
                                    TLdeserialize.id = sQLiteCursor.intValue(1);
                                    TLdeserialize.date = sQLiteCursor.intValue(2);
                                    TLdeserialize.dialog_id = j2;
                                    MessagesStorage.addUsersAndChatsFromMessage(TLdeserialize, arrayList8, arrayList9, null);
                                    arrayList5.add(TLdeserialize);
                                    TLRPC.Peer peer = TLdeserialize.peer_id;
                                    long j3 = peer != null ? peer.channel_id : 0L;
                                    ArrayList arrayList11 = (ArrayList) longSparseArray5.get(j3);
                                    if (arrayList11 != null) {
                                        arrayList11.remove(Integer.valueOf(TLdeserialize.id));
                                        if (arrayList11.isEmpty()) {
                                            longSparseArray5.remove(j3);
                                        }
                                    }
                                    i10 = i12;
                                    sQLiteCursor2 = sQLiteCursor;
                                    r14 = 0;
                                    sQLiteCursor = sQLiteCursor2;
                                    while (sQLiteCursor.next()) {
                                    }
                                }
                            }
                            i8 = i10;
                            sQLiteCursor.dispose();
                        } else {
                            arrayList3 = arrayList7;
                            i7 = size;
                            str = str2;
                            arrayList4 = arrayList10;
                            r14 = 0;
                            sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid, date, uid FROM messages_v2 WHERE mid IN(%s) AND uid = %d", TextUtils.join(str2, arrayList10), Long.valueOf(j)), new Object[0]);
                            while (sQLiteCursor.next()) {
                            }
                            i8 = i10;
                            sQLiteCursor.dispose();
                        }
                        i10 = i8 + 1;
                        size = i7;
                        arrayList7 = arrayList3;
                        arrayList10 = arrayList4;
                        str2 = str;
                    }
                    arrayList2 = arrayList7;
                    i5 = size;
                    i6 = 1;
                }
                i9 += i6;
                size = i5;
                arrayList7 = arrayList2;
                longSparseArray4 = longSparseArray2;
            }
            ArrayList<TLRPC.Chat> arrayList12 = arrayList7;
            if (!arrayList8.isEmpty()) {
                getMessagesStorage().getUsersInternal(arrayList8, arrayList6);
            }
            if (arrayList9.isEmpty()) {
                arrayList = arrayList12;
            } else {
                arrayList = arrayList12;
                getMessagesStorage().getChatsInternal(TextUtils.join(",", arrayList9), arrayList);
            }
            broadcastReplyMessages(arrayList5, longSparseArray2, arrayList6, arrayList, j, true);
            if (longSparseArray3.isEmpty()) {
                Timer.done(start);
                if (atomicInteger.decrementAndGet() != 0 || runnable == null) {
                    return;
                }
                AndroidUtilities.runOnUIThread(runnable);
                return;
            }
            Timer.done(start);
            int size2 = longSparseArray3.size();
            int i13 = 0;
            while (i13 < size2) {
                final long keyAt2 = longSparseArray5.keyAt(i13);
                if (z) {
                    final Timer.Task start2 = Timer.start(timer, "loadReplyMessagesForMessages: load scheduled");
                    final TLRPC.TL_messages_getScheduledMessages tL_messages_getScheduledMessages = new TLRPC.TL_messages_getScheduledMessages();
                    tL_messages_getScheduledMessages.peer = getMessagesController().getInputPeer(j2);
                    tL_messages_getScheduledMessages.id = (ArrayList) longSparseArray5.valueAt(i13);
                    i3 = size2;
                    int i14 = i13;
                    sendRequest = getConnectionsManager().sendRequest(tL_messages_getScheduledMessages, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda20
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            MediaDataController.this.lambda$loadReplyMessagesForMessages$174(start2, tL_messages_getScheduledMessages, keyAt2, j, longSparseArray2, z, timer, atomicInteger, runnable, tLObject, tL_error);
                        }
                    });
                    i2 = i;
                    if (i2 != 0) {
                        connectionsManager = getConnectionsManager();
                        longSparseArray5 = longSparseArray3;
                        i4 = i14;
                        connectionsManager.bindRequestToGuid(sendRequest, i2);
                        i13 = i4 + 1;
                        size2 = i3;
                        j2 = j;
                    } else {
                        longSparseArray5 = longSparseArray3;
                        i4 = i14;
                        i13 = i4 + 1;
                        size2 = i3;
                        j2 = j;
                    }
                } else {
                    i2 = i;
                    i3 = size2;
                    int i15 = i13;
                    if (keyAt2 != 0) {
                        final Timer.Task start3 = Timer.start(timer, "loadReplyMessagesForMessages: load channel messages");
                        TLRPC.TL_channels_getMessages tL_channels_getMessages = new TLRPC.TL_channels_getMessages();
                        tL_channels_getMessages.channel = getMessagesController().getInputChannel(keyAt2);
                        tL_channels_getMessages.id = (ArrayList) longSparseArray3.valueAt(i15);
                        sendRequest = getConnectionsManager().sendRequest(tL_channels_getMessages, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda21
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                MediaDataController.this.lambda$loadReplyMessagesForMessages$175(start3, j, keyAt2, longSparseArray2, z, timer, atomicInteger, runnable, tLObject, tL_error);
                            }
                        });
                        if (i2 != 0) {
                            connectionsManager = getConnectionsManager();
                            longSparseArray5 = longSparseArray3;
                            i4 = i15;
                            connectionsManager.bindRequestToGuid(sendRequest, i2);
                            i13 = i4 + 1;
                            size2 = i3;
                            j2 = j;
                        } else {
                            longSparseArray5 = longSparseArray3;
                            i4 = i15;
                            i13 = i4 + 1;
                            size2 = i3;
                            j2 = j;
                        }
                    } else {
                        final Timer.Task start4 = Timer.start(timer, "loadReplyMessagesForMessages: load messages");
                        TLRPC.TL_messages_getMessages tL_messages_getMessages = new TLRPC.TL_messages_getMessages();
                        longSparseArray5 = longSparseArray3;
                        i4 = i15;
                        tL_messages_getMessages.id = (ArrayList) longSparseArray5.valueAt(i4);
                        sendRequest = getConnectionsManager().sendRequest(tL_messages_getMessages, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda22
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                MediaDataController.this.lambda$loadReplyMessagesForMessages$176(start4, j, longSparseArray2, z, timer, atomicInteger, runnable, tLObject, tL_error);
                            }
                        });
                        if (i2 != 0) {
                            connectionsManager = getConnectionsManager();
                            connectionsManager.bindRequestToGuid(sendRequest, i2);
                        }
                        i13 = i4 + 1;
                        size2 = i3;
                        j2 = j;
                    }
                }
            }
        } catch (Exception e) {
            if (runnable != null) {
                AndroidUtilities.runOnUIThread(runnable);
            }
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRestrictedStatusEmojis$245(TLObject tLObject, SharedPreferences sharedPreferences) {
        if (tLObject instanceof TLRPC.TL_emojiList) {
            SerializedData serializedData = new SerializedData(tLObject.getObjectSize());
            tLObject.serializeToStream(serializedData);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            this.restrictedStatusEmojis = (TLRPC.TL_emojiList) tLObject;
            edit.putString("restrictedstatuses", Utilities.bytesToHex(serializedData.toByteArray()));
            edit.putLong("restrictedstatuses_last_check", System.currentTimeMillis());
            edit.apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadRestrictedStatusEmojis$246(final SharedPreferences sharedPreferences, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda180
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadRestrictedStatusEmojis$245(tLObject, sharedPreferences);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSavedReactions$239(TLRPC.TL_error tL_error, TLObject tLObject, SharedPreferences sharedPreferences) {
        if (tL_error == null) {
            if (tLObject instanceof TLRPC.TL_messages_reactions) {
                TLRPC.TL_messages_reactions tL_messages_reactions = (TLRPC.TL_messages_reactions) tLObject;
                this.savedReactions.clear();
                this.savedReactions.addAll(tL_messages_reactions.reactions);
                saveReactionsToPref(sharedPreferences, tL_messages_reactions.hash, tL_messages_reactions.reactions);
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.savedReactionTagsUpdate, 0L);
            }
            boolean z = tLObject instanceof TLRPC.TL_messages_reactionsNotModified;
        }
        this.loadingSavedReactions = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadSavedReactions$240(final SharedPreferences sharedPreferences, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda118
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadSavedReactions$239(tL_error, tLObject, sharedPreferences);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadStickers$89(int i, boolean z, Utilities.Callback callback) {
        loadStickers(i, false, z, false, callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadStickers$90(Utilities.Callback callback, ArrayList arrayList) {
        if (callback != null) {
            callback.run(arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadStickers$91(int i, final Utilities.Callback callback) {
        final ArrayList<TLRPC.TL_messages_stickerSet> arrayList = new ArrayList<>();
        int i2 = 0;
        long j = 0;
        SQLiteCursor sQLiteCursor = null;
        try {
            sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT data, date, hash FROM stickers_v2 WHERE id = " + (i + 1), new Object[0]);
            if (sQLiteCursor.next()) {
                NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
                if (byteBufferValue != null) {
                    int readInt32 = byteBufferValue.readInt32(false);
                    for (int i3 = 0; i3 < readInt32; i3++) {
                        arrayList.add(TLRPC.messages_StickerSet.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false));
                    }
                    byteBufferValue.reuse();
                }
                i2 = sQLiteCursor.intValue(1);
                j = calcStickersHash(arrayList);
            }
        } catch (Throwable th) {
            try {
                FileLog.e(th);
            } finally {
                if (sQLiteCursor != null) {
                    sQLiteCursor.dispose();
                }
            }
        }
        processLoadedStickers(i, arrayList, true, i2, j, new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda182
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.lambda$loadStickers$90(Utilities.Callback.this, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadStickers$92(Utilities.Callback callback) {
        if (callback != null) {
            callback.run(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadStickers$93(Utilities.Callback callback) {
        if (callback != null) {
            callback.run(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadStickers$94(Utilities.Callback callback) {
        if (callback != null) {
            callback.run(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadStickers$95(int i, final Utilities.Callback callback, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (!(tLObject instanceof TLRPC.TL_messages_stickerSet)) {
            processLoadedStickers(i, null, false, (int) (System.currentTimeMillis() / 1000), 0L, new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda58
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.lambda$loadStickers$94(Utilities.Callback.this);
                }
            });
            return;
        }
        ArrayList<TLRPC.TL_messages_stickerSet> arrayList = new ArrayList<>();
        arrayList.add((TLRPC.TL_messages_stickerSet) tLObject);
        processLoadedStickers(i, arrayList, false, (int) (System.currentTimeMillis() / 1000), calcStickersHash(arrayList), new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda57
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.lambda$loadStickers$93(Utilities.Callback.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadStickers$96(Utilities.Callback callback) {
        if (callback != null) {
            callback.run(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$loadStickers$97(Utilities.Callback callback) {
        if (callback != null) {
            callback.run(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadStickers$98(TLObject tLObject, int i, final Utilities.Callback callback, long j) {
        if (tLObject instanceof TLRPC.TL_messages_allStickers) {
            processLoadStickersResponse(i, (TLRPC.TL_messages_allStickers) tLObject, new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda71
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.lambda$loadStickers$96(Utilities.Callback.this);
                }
            });
        } else {
            processLoadedStickers(i, null, false, (int) (System.currentTimeMillis() / 1000), j, new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda72
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.lambda$loadStickers$97(Utilities.Callback.this);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadStickers$99(final int i, final Utilities.Callback callback, final long j, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda208
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadStickers$98(tLObject, i, callback, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadStickersByEmojiOrName$81(String str, boolean z) {
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet2;
        int i;
        SQLiteCursor sQLiteCursor;
        int i2 = 0;
        SQLiteCursor sQLiteCursor2 = null;
        r2 = null;
        r2 = null;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet3 = null;
        try {
            sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT data, date FROM stickers_dice WHERE emoji = ?", str);
        } catch (Throwable th) {
            th = th;
            tL_messages_stickerSet = null;
        }
        try {
            if (sQLiteCursor.next()) {
                NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
                if (byteBufferValue != null) {
                    tL_messages_stickerSet3 = TLRPC.messages_StickerSet.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                    byteBufferValue.reuse();
                }
                i2 = sQLiteCursor.intValue(1);
            }
        } catch (Throwable th2) {
            th = th2;
            tL_messages_stickerSet = tL_messages_stickerSet3;
            sQLiteCursor2 = sQLiteCursor;
            try {
                FileLog.e(th);
                if (sQLiteCursor2 == null) {
                    tL_messages_stickerSet2 = tL_messages_stickerSet;
                    i = 0;
                    processLoadedDiceStickers(str, z, tL_messages_stickerSet2, true, i);
                }
                TLRPC.TL_messages_stickerSet tL_messages_stickerSet4 = tL_messages_stickerSet;
                sQLiteCursor = sQLiteCursor2;
                tL_messages_stickerSet3 = tL_messages_stickerSet4;
                sQLiteCursor.dispose();
                i = i2;
                tL_messages_stickerSet2 = tL_messages_stickerSet3;
                processLoadedDiceStickers(str, z, tL_messages_stickerSet2, true, i);
            } catch (Throwable th3) {
                if (sQLiteCursor2 != null) {
                    sQLiteCursor2.dispose();
                }
                throw th3;
            }
        }
        sQLiteCursor.dispose();
        i = i2;
        tL_messages_stickerSet2 = tL_messages_stickerSet3;
        processLoadedDiceStickers(str, z, tL_messages_stickerSet2, true, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadStickersByEmojiOrName$82(TLRPC.TL_error tL_error, TLObject tLObject, String str, boolean z) {
        if (tLObject instanceof TLRPC.TL_messages_stickerSet) {
            processLoadedDiceStickers(str, z, (TLRPC.TL_messages_stickerSet) tLObject, false, (int) (System.currentTimeMillis() / 1000));
        } else {
            processLoadedDiceStickers(str, z, null, false, (int) (System.currentTimeMillis() / 1000));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadStickersByEmojiOrName$83(final String str, final boolean z, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda226
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadStickersByEmojiOrName$82(tL_error, tLObject, str, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$markFeaturedStickersAsRead$65(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$markFeaturedStickersByIdAsRead$66(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$markFeaturedStickersByIdAsRead$67(boolean z, long j) {
        this.unreadStickerSets[z ? 1 : 0].remove(Long.valueOf(j));
        this.readingStickerSets[z ? 1 : 0].remove(Long.valueOf(j));
        this.loadFeaturedHash[z ? 1 : 0] = calcFeaturedStickersHash(z, this.featuredStickerSets[z ? 1 : 0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(z ? NotificationCenter.featuredEmojiDidLoad : NotificationCenter.featuredStickersDidLoad, new Object[0]);
        putFeaturedStickersToCache(z, this.featuredStickerSets[z ? 1 : 0], this.unreadStickerSets[z ? 1 : 0], this.loadFeaturedDate[z ? 1 : 0], this.loadFeaturedHash[z ? 1 : 0], this.loadFeaturedPremium);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$preloadPremiumPreviewStickers$205(TLRPC.TL_error tL_error, TLObject tLObject) {
        if (tL_error != null) {
            return;
        }
        this.previewStickersLoading = false;
        this.premiumPreviewStickers.clear();
        this.premiumPreviewStickers.addAll(((TLRPC.TL_messages_stickers) tLObject).stickers);
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.premiumStickersPreviewLoaded, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$preloadPremiumPreviewStickers$206(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda66
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$preloadPremiumPreviewStickers$205(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadStickersResponse$73(TLObject tLObject, ArrayList arrayList, int i, LongSparseArray longSparseArray, TLRPC.StickerSet stickerSet, TLRPC.TL_messages_allStickers tL_messages_allStickers, int i2) {
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) tLObject;
        arrayList.set(i, tL_messages_stickerSet);
        longSparseArray.put(stickerSet.id, tL_messages_stickerSet);
        if (longSparseArray.size() == tL_messages_allStickers.sets.size()) {
            int i3 = 0;
            while (i3 < arrayList.size()) {
                if (arrayList.get(i3) == null) {
                    arrayList.remove(i3);
                    i3--;
                }
                i3++;
            }
            processLoadedStickers(i2, arrayList, false, (int) (System.currentTimeMillis() / 1000), tL_messages_allStickers.hash2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadStickersResponse$74(final ArrayList arrayList, final int i, final LongSparseArray longSparseArray, final TLRPC.StickerSet stickerSet, final TLRPC.TL_messages_allStickers tL_messages_allStickers, final int i2, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda142
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$processLoadStickersResponse$73(tLObject, arrayList, i, longSparseArray, stickerSet, tL_messages_allStickers, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedDiceStickers$84(String str) {
        this.loadingDiceStickerSets.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedDiceStickers$85(String str, boolean z) {
        loadStickersByEmojiOrName(str, z, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedDiceStickers$86(String str, TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        this.diceStickerSetsByEmoji.put(str, tL_messages_stickerSet);
        this.diceEmojiStickerSetsById.put(tL_messages_stickerSet.set.id, str);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.diceStickersDidLoad, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedDiceStickers$87(boolean z, final TLRPC.TL_messages_stickerSet tL_messages_stickerSet, int i, final String str, final boolean z2) {
        if (z) {
            if (tL_messages_stickerSet == null || Math.abs((System.currentTimeMillis() / 1000) - i) >= 86400) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$processLoadedDiceStickers$85(str, z2);
                    }
                }, (tL_messages_stickerSet != null || z) ? 0L : 1000L);
                if (tL_messages_stickerSet == null) {
                    return;
                }
            }
        }
        if (tL_messages_stickerSet != null) {
            if (!z) {
                putDiceStickersToCache(str, tL_messages_stickerSet, i);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedDiceStickers$86(str, tL_messages_stickerSet);
                }
            });
        } else {
            if (z) {
                return;
            }
            putDiceStickersToCache(str, null, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedFeaturedStickers$59(boolean z) {
        this.loadingFeaturedStickers[z ? 1 : 0] = false;
        this.featuredStickersLoaded[z ? 1 : 0] = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedFeaturedStickers$60(ArrayList arrayList, long j, boolean z) {
        if (arrayList != null && j != 0) {
            this.loadFeaturedHash[z ? 1 : 0] = j;
        }
        this.loadingFeaturedStickers[z ? 1 : 0] = false;
        loadFeaturedStickers(z, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$processLoadedFeaturedStickers$61(boolean z, ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2, long j, int i, boolean z2) {
        this.unreadStickerSets[z ? 1 : 0] = arrayList;
        this.featuredStickerSetsById[z ? 1 : 0] = longSparseArray;
        this.featuredStickerSets[z ? 1 : 0] = arrayList2;
        this.loadFeaturedHash[z ? 1 : 0] = j;
        this.loadFeaturedDate[z ? 1 : 0] = i;
        this.loadFeaturedPremium = z2;
        loadStickers(z ? 6 : 3, true, false);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(z ? NotificationCenter.featuredEmojiDidLoad : NotificationCenter.featuredStickersDidLoad, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedFeaturedStickers$62(boolean z, int i) {
        this.loadFeaturedDate[z ? 1 : 0] = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedFeaturedStickers$63(boolean z, final ArrayList arrayList, final int i, final long j, final boolean z2, final ArrayList arrayList2, final boolean z3) {
        long j2 = 0;
        if ((z && (arrayList == null || Math.abs((System.currentTimeMillis() / 1000) - i) >= 3600)) || (!z && arrayList == null && j == 0)) {
            Runnable runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda231
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedFeaturedStickers$60(arrayList, j, z2);
                }
            };
            if (arrayList == null && !z) {
                j2 = 1000;
            }
            AndroidUtilities.runOnUIThread(runnable, j2);
            if (arrayList == null) {
                return;
            }
        }
        if (arrayList == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda233
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedFeaturedStickers$62(z2, i);
                }
            });
            putFeaturedStickersToCache(z2, null, null, i, 0L, z3);
            return;
        }
        try {
            final ArrayList<TLRPC.StickerSetCovered> arrayList3 = new ArrayList<>();
            final LongSparseArray longSparseArray = new LongSparseArray();
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                TLRPC.StickerSetCovered stickerSetCovered = (TLRPC.StickerSetCovered) arrayList.get(i2);
                arrayList3.add(stickerSetCovered);
                longSparseArray.put(stickerSetCovered.set.id, stickerSetCovered);
            }
            if (!z) {
                putFeaturedStickersToCache(z2, arrayList3, arrayList2, i, j, z3);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda232
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedFeaturedStickers$61(z2, arrayList2, longSparseArray, arrayList3, j, i, z3);
                }
            });
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedMedia$131(TLRPC.messages_Messages messages_messages, int i, long j, ArrayList arrayList, int i2, int i3, boolean z, int i4, int i5) {
        int i6 = messages_messages.count;
        getMessagesController().putUsers(messages_messages.users, i != 0);
        getMessagesController().putChats(messages_messages.chats, i != 0);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.mediaDidLoad, Long.valueOf(j), Integer.valueOf(i6), arrayList, Integer.valueOf(i2), Integer.valueOf(i3), Boolean.valueOf(z), Boolean.valueOf(i4 != 0), Integer.valueOf(i5));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedMedia$132(final TLRPC.messages_Messages messages_messages, final int i, final long j, final ArrayList arrayList, final int i2, final int i3, final boolean z, final int i4, final int i5) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda112
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$processLoadedMedia$131(messages_messages, i, j, arrayList, i2, i3, z, i4, i5);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedMedia$133(ArrayList arrayList, Runnable runnable) {
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = (MessageObject) arrayList.get(i);
            TLRPC.Message messageWithCustomParamsOnlyInternal = getMessagesStorage().getMessageWithCustomParamsOnlyInternal(messageObject.getId(), messageObject.getDialogId());
            TLRPC.Message message = messageObject.messageOwner;
            message.translatedToLanguage = messageWithCustomParamsOnlyInternal.translatedToLanguage;
            message.translatedText = messageWithCustomParamsOnlyInternal.translatedText;
            messageObject.updateTranslation();
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedMedia$134(final TLRPC.messages_Messages messages_messages, final int i, final long j, final int i2, final int i3, final boolean z, final int i4, final int i5) {
        LongSparseArray longSparseArray = new LongSparseArray();
        for (int i6 = 0; i6 < messages_messages.users.size(); i6++) {
            TLRPC.User user = messages_messages.users.get(i6);
            longSparseArray.put(user.id, user);
        }
        final ArrayList<MessageObject> arrayList = new ArrayList<>();
        for (int i7 = 0; i7 < messages_messages.messages.size(); i7++) {
            MessageObject messageObject = new MessageObject(this.currentAccount, messages_messages.messages.get(i7), longSparseArray, true, false);
            messageObject.createStrippedThumb();
            arrayList.add(messageObject);
        }
        getFileLoader().checkMediaExistance(arrayList);
        final Runnable runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda177
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$processLoadedMedia$132(messages_messages, i, j, arrayList, i2, i3, z, i4, i5);
            }
        };
        if (getMessagesController().getTranslateController().isFeatureAvailable()) {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda178
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedMedia$133(arrayList, runnable);
                }
            });
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0012, code lost:
    
        if (r25 == 2) goto L11;
     */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0021  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$processLoadedMediaCount$135(long j, boolean z, int i, int i2, int i3, long j2, int i4) {
        boolean z2;
        int i5 = i;
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(j);
        if (z) {
            if (i5 != -1) {
                if (i5 == 0) {
                }
            }
            if (!isEncryptedDialog) {
                z2 = true;
                if (!z2 || (i3 == 1 && !isEncryptedDialog)) {
                    getMediaCount(j, j2, i2, i4, false);
                }
                if (z2) {
                    return;
                }
                if (!z) {
                    putMediaCountDatabase(j, j2, i2, i);
                }
                NotificationCenter notificationCenter = getNotificationCenter();
                int i6 = NotificationCenter.mediaCountDidLoad;
                Long valueOf = Long.valueOf(j);
                Long valueOf2 = Long.valueOf(j2);
                if (z && i5 == -1) {
                    i5 = 0;
                }
                notificationCenter.lambda$postNotificationNameOnUIThread$1(i6, valueOf, valueOf2, Integer.valueOf(i5), Boolean.valueOf(z), Integer.valueOf(i2));
                return;
            }
            z2 = false;
            if (!z2) {
            }
            getMediaCount(j, j2, i2, i4, false);
            if (z2) {
            }
        }
        z2 = false;
        if (!z2) {
        }
        getMediaCount(j, j2, i2, i4, false);
        if (z2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedMenuBots$5() {
        NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.attachMenuBotsDidLoad, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedPremiumPromo$9() {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.premiumPromoUpdated, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedReactions$15() {
        preloadDefaultReactions();
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reactionsDidLoad, new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedRecentDocuments$52(boolean z, int i, ArrayList arrayList, boolean z2, int i2) {
        int i3;
        try {
            SQLiteDatabase database = getMessagesStorage().getDatabase();
            int i4 = 2;
            int i5 = 3;
            if (z) {
                i3 = getMessagesController().maxRecentGifsCount;
            } else {
                if (i != 3 && i != 7) {
                    i3 = i == 2 ? getMessagesController().maxFaveStickersCount : getMessagesController().maxRecentStickersCount;
                }
                i3 = NotificationCenter.storyQualityUpdate;
            }
            database.beginTransaction();
            SQLitePreparedStatement executeFast = database.executeFast("REPLACE INTO web_recent_v3 VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            int size = arrayList.size();
            int i6 = z ? 2 : i == 0 ? 3 : i == 1 ? 4 : i == 3 ? 6 : i == 5 ? 7 : i == 7 ? 8 : 5;
            if (z2) {
                database.executeFast("DELETE FROM web_recent_v3 WHERE type = " + i6).stepThis().dispose();
            }
            int i7 = 0;
            while (i7 < size && i7 != i3) {
                TLRPC.Document document = (TLRPC.Document) arrayList.get(i7);
                executeFast.requery();
                executeFast.bindString(1, "" + document.id);
                executeFast.bindInteger(i4, i6);
                executeFast.bindString(i5, "");
                executeFast.bindString(4, "");
                executeFast.bindString(5, "");
                executeFast.bindInteger(6, 0);
                executeFast.bindInteger(7, 0);
                executeFast.bindInteger(8, 0);
                executeFast.bindInteger(9, i2 != 0 ? i2 : size - i7);
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(document.getObjectSize());
                document.serializeToStream(nativeByteBuffer);
                executeFast.bindByteBuffer(10, nativeByteBuffer);
                executeFast.step();
                nativeByteBuffer.reuse();
                i7++;
                i4 = 2;
                i5 = 3;
            }
            executeFast.dispose();
            database.commitTransaction();
            if (z2 || arrayList.size() < i3) {
                return;
            }
            database.beginTransaction();
            while (i3 < arrayList.size()) {
                database.executeFast("DELETE FROM web_recent_v3 WHERE id = '" + ((TLRPC.Document) arrayList.get(i3)).id + "' AND type = " + i6).stepThis().dispose();
                i3++;
            }
            database.commitTransaction();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$processLoadedRecentDocuments$53(boolean z, int i, ArrayList arrayList) {
        long currentTimeMillis;
        String str;
        SharedPreferences.Editor edit = MessagesController.getEmojiSettings(this.currentAccount).edit();
        if (z) {
            this.loadingRecentGifs = false;
            this.recentGifsLoaded = true;
            currentTimeMillis = System.currentTimeMillis();
            str = "lastGifLoadTime";
        } else {
            this.loadingRecentStickers[i] = false;
            this.recentStickersLoaded[i] = true;
            if (i == 0) {
                currentTimeMillis = System.currentTimeMillis();
                str = "lastStickersLoadTime";
            } else if (i == 1) {
                currentTimeMillis = System.currentTimeMillis();
                str = "lastStickersLoadTimeMask";
            } else if (i == 3) {
                currentTimeMillis = System.currentTimeMillis();
                str = "lastStickersLoadTimeGreet";
            } else if (i == 5) {
                currentTimeMillis = System.currentTimeMillis();
                str = "lastStickersLoadTimeEmojiPacks";
            } else if (i == 7) {
                currentTimeMillis = System.currentTimeMillis();
                str = "lastStickersLoadTimePremiumStickers";
            } else {
                currentTimeMillis = System.currentTimeMillis();
                str = "lastStickersLoadTimeFavs";
            }
        }
        edit.putLong(str, currentTimeMillis).apply();
        if (arrayList != null) {
            if (z) {
                this.recentGifs = arrayList;
            } else {
                this.recentStickers[i] = arrayList;
            }
            if (i == 3) {
                preloadNextGreetingsSticker();
            }
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.recentDocumentsDidLoad, Boolean.valueOf(z), Integer.valueOf(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedStickers$101(int i) {
        this.loadingStickers[i] = false;
        this.stickersLoaded[i] = true;
        Runnable runnable = this.scheduledLoadStickers[i];
        if (runnable != null) {
            runnable.run();
            this.scheduledLoadStickers[i] = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedStickers$102(ArrayList arrayList, long j, int i) {
        if (arrayList != null && j != 0) {
            this.loadHash[i] = j;
        }
        loadStickers(i, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$processLoadedStickers$103(int i, LongSparseArray longSparseArray, HashMap hashMap, ArrayList arrayList, long j, int i2, LongSparseArray longSparseArray2, HashMap hashMap2, LongSparseArray longSparseArray3, Runnable runnable) {
        for (int i3 = 0; i3 < this.stickerSets[i].size(); i3++) {
            TLRPC.StickerSet stickerSet = this.stickerSets[i].get(i3).set;
            this.stickerSetsById.remove(stickerSet.id);
            this.stickerSetsByName.remove(stickerSet.short_name);
            if (i != 3 && i != 6 && i != 4) {
                this.installedStickerSetsById.remove(stickerSet.id);
            }
        }
        for (int i4 = 0; i4 < longSparseArray.size(); i4++) {
            this.stickerSetsById.put(longSparseArray.keyAt(i4), (TLRPC.TL_messages_stickerSet) longSparseArray.valueAt(i4));
            if (i != 3 && i != 6 && i != 4) {
                this.installedStickerSetsById.put(longSparseArray.keyAt(i4), (TLRPC.TL_messages_stickerSet) longSparseArray.valueAt(i4));
            }
        }
        this.stickerSetsByName.putAll(hashMap);
        this.stickerSets[i] = arrayList;
        this.loadHash[i] = j;
        this.loadDate[i] = i2;
        this.stickersByIds[i] = longSparseArray2;
        if (i == 0) {
            this.allStickers = hashMap2;
            this.stickersByEmoji = longSparseArray3;
        } else if (i == 3) {
            this.allStickersFeatured = hashMap2;
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(i), Boolean.TRUE);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedStickers$104(int i, int i2) {
        this.loadDate[i] = i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processLoadedStickers$105(boolean z, final ArrayList arrayList, final int i, final long j, final int i2, final Runnable runnable) {
        int i3;
        String str;
        int i4;
        MediaDataController mediaDataController = this;
        ArrayList arrayList2 = arrayList;
        long j2 = 0;
        if ((z && (arrayList2 == null || BuildVars.DEBUG_PRIVATE_VERSION || Math.abs((System.currentTimeMillis() / 1000) - i) >= 3600)) || (!z && arrayList2 == null && j == 0)) {
            Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda235
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedStickers$102(arrayList, j, i2);
                }
            };
            if (arrayList2 == null && !z) {
                j2 = 1000;
            }
            AndroidUtilities.runOnUIThread(runnable2, j2);
            if (arrayList2 == null) {
                if (runnable != null) {
                    runnable.run();
                    return;
                }
                return;
            }
        }
        if (arrayList2 != null) {
            try {
                final ArrayList<TLRPC.TL_messages_stickerSet> arrayList3 = new ArrayList<>();
                final LongSparseArray longSparseArray = new LongSparseArray();
                final HashMap hashMap = new HashMap();
                final LongSparseArray longSparseArray2 = new LongSparseArray();
                final LongSparseArray longSparseArray3 = new LongSparseArray();
                HashMap hashMap2 = new HashMap();
                int i5 = 0;
                while (i5 < arrayList.size()) {
                    TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) arrayList2.get(i5);
                    if (tL_messages_stickerSet == null || mediaDataController.removingStickerSetsUndos.indexOfKey(tL_messages_stickerSet.set.id) >= 0) {
                        i3 = i5;
                    } else {
                        arrayList3.add(tL_messages_stickerSet);
                        longSparseArray.put(tL_messages_stickerSet.set.id, tL_messages_stickerSet);
                        hashMap.put(tL_messages_stickerSet.set.short_name, tL_messages_stickerSet);
                        int i6 = 0;
                        while (i6 < tL_messages_stickerSet.documents.size()) {
                            TLRPC.Document document = tL_messages_stickerSet.documents.get(i6);
                            if (document == null || (document instanceof TLRPC.TL_documentEmpty)) {
                                i4 = i5;
                            } else {
                                i4 = i5;
                                longSparseArray3.put(document.id, document);
                            }
                            i6++;
                            i5 = i4;
                        }
                        i3 = i5;
                        if (!tL_messages_stickerSet.set.archived) {
                            int i7 = 0;
                            while (i7 < tL_messages_stickerSet.packs.size()) {
                                TLRPC.TL_stickerPack tL_stickerPack = tL_messages_stickerSet.packs.get(i7);
                                if (tL_stickerPack != null && (str = tL_stickerPack.emoticon) != null) {
                                    String replace = str.replace("️", "");
                                    tL_stickerPack.emoticon = replace;
                                    ArrayList arrayList4 = (ArrayList) hashMap2.get(replace);
                                    if (arrayList4 == null) {
                                        arrayList4 = new ArrayList();
                                        hashMap2.put(tL_stickerPack.emoticon, arrayList4);
                                    }
                                    int i8 = 0;
                                    while (i8 < tL_stickerPack.documents.size()) {
                                        Long l = tL_stickerPack.documents.get(i8);
                                        HashMap hashMap3 = hashMap2;
                                        if (longSparseArray2.indexOfKey(l.longValue()) < 0) {
                                            longSparseArray2.put(l.longValue(), tL_stickerPack.emoticon);
                                        }
                                        TLRPC.Document document2 = (TLRPC.Document) longSparseArray3.get(l.longValue());
                                        if (document2 != null) {
                                            arrayList4.add(document2);
                                        }
                                        i8++;
                                        hashMap2 = hashMap3;
                                    }
                                }
                                i7++;
                                hashMap2 = hashMap2;
                            }
                        }
                    }
                    i5 = i3 + 1;
                    mediaDataController = this;
                    arrayList2 = arrayList;
                    hashMap2 = hashMap2;
                }
                final HashMap hashMap4 = hashMap2;
                if (!z) {
                    putStickersToCache(i2, arrayList3, i, j);
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda236
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$processLoadedStickers$103(i2, longSparseArray, hashMap, arrayList3, j, i, longSparseArray3, hashMap4, longSparseArray2, runnable);
                    }
                });
            } catch (Throwable th) {
                FileLog.e(th);
                if (runnable != null) {
                }
            }
            return;
        }
        if (!z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda237
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedStickers$104(i2, i);
                }
            });
            putStickersToCache(i2, null, i, 0L);
            if (runnable == null) {
                return;
            }
        } else if (runnable == null) {
            return;
        }
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putBotInfo$201(TL_bots.BotInfo botInfo, long j) {
        try {
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO bot_info_v2 VALUES(?, ?, ?)");
            executeFast.requery();
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(botInfo.getObjectSize());
            botInfo.serializeToStream(nativeByteBuffer);
            executeFast.bindLong(1, botInfo.user_id);
            executeFast.bindLong(2, j);
            executeFast.bindByteBuffer(3, nativeByteBuffer);
            executeFast.step();
            nativeByteBuffer.reuse();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putBotKeyboard$200(MessagesStorage.TopicKey topicKey, TLRPC.Message message) {
        TLRPC.Message message2 = this.botKeyboards.get(topicKey);
        this.botKeyboards.put(topicKey, message);
        ArrayList arrayList = (ArrayList) this.botDialogKeyboards.get(topicKey.dialogId);
        if (arrayList == null) {
            arrayList = new ArrayList();
        }
        arrayList.add(message);
        this.botDialogKeyboards.put(topicKey.dialogId, arrayList);
        if (MessageObject.getChannelId(message) == 0) {
            if (message2 != null) {
                this.botKeyboardsByMids.delete(message2.id);
            }
            this.botKeyboardsByMids.put(message.id, topicKey);
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.botKeyboardDidLoad, message, topicKey);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putDiceStickersToCache$88(TLRPC.TL_messages_stickerSet tL_messages_stickerSet, String str, int i) {
        SQLitePreparedStatement executeFast;
        try {
            if (tL_messages_stickerSet != null) {
                executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO stickers_dice VALUES(?, ?, ?)");
                executeFast.requery();
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tL_messages_stickerSet.getObjectSize());
                tL_messages_stickerSet.serializeToStream(nativeByteBuffer);
                executeFast.bindString(1, str);
                executeFast.bindByteBuffer(2, nativeByteBuffer);
                executeFast.bindInteger(3, i);
                executeFast.step();
                nativeByteBuffer.reuse();
            } else {
                executeFast = getMessagesStorage().getDatabase().executeFast("UPDATE stickers_dice SET date = ?");
                executeFast.requery();
                executeFast.bindInteger(1, i);
                executeFast.step();
            }
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putEmojiKeywords$214(String str) {
        this.currentFetchingEmoji.remove(str);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.newEmojiSuggestionsAvailable, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putEmojiKeywords$215(TLRPC.TL_emojiKeywordsDifference tL_emojiKeywordsDifference, final String str) {
        try {
            if (!tL_emojiKeywordsDifference.keywords.isEmpty()) {
                SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO emoji_keywords_v2 VALUES(?, ?, ?)");
                SQLitePreparedStatement executeFast2 = getMessagesStorage().getDatabase().executeFast("DELETE FROM emoji_keywords_v2 WHERE lang = ? AND keyword = ? AND emoji = ?");
                getMessagesStorage().getDatabase().beginTransaction();
                int size = tL_emojiKeywordsDifference.keywords.size();
                for (int i = 0; i < size; i++) {
                    TLRPC.EmojiKeyword emojiKeyword = tL_emojiKeywordsDifference.keywords.get(i);
                    if (emojiKeyword instanceof TLRPC.TL_emojiKeyword) {
                        TLRPC.TL_emojiKeyword tL_emojiKeyword = (TLRPC.TL_emojiKeyword) emojiKeyword;
                        String lowerCase = tL_emojiKeyword.keyword.toLowerCase();
                        int size2 = tL_emojiKeyword.emoticons.size();
                        for (int i2 = 0; i2 < size2; i2++) {
                            executeFast.requery();
                            executeFast.bindString(1, tL_emojiKeywordsDifference.lang_code);
                            executeFast.bindString(2, lowerCase);
                            executeFast.bindString(3, tL_emojiKeyword.emoticons.get(i2));
                            executeFast.step();
                        }
                    } else if (emojiKeyword instanceof TLRPC.TL_emojiKeywordDeleted) {
                        TLRPC.TL_emojiKeywordDeleted tL_emojiKeywordDeleted = (TLRPC.TL_emojiKeywordDeleted) emojiKeyword;
                        String lowerCase2 = tL_emojiKeywordDeleted.keyword.toLowerCase();
                        int size3 = tL_emojiKeywordDeleted.emoticons.size();
                        for (int i3 = 0; i3 < size3; i3++) {
                            executeFast2.requery();
                            executeFast2.bindString(1, tL_emojiKeywordsDifference.lang_code);
                            executeFast2.bindString(2, lowerCase2);
                            executeFast2.bindString(3, tL_emojiKeywordDeleted.emoticons.get(i3));
                            executeFast2.step();
                        }
                    }
                }
                getMessagesStorage().getDatabase().commitTransaction();
                executeFast.dispose();
                executeFast2.dispose();
            }
            SQLitePreparedStatement executeFast3 = getMessagesStorage().getDatabase().executeFast("REPLACE INTO emoji_keywords_info_v2 VALUES(?, ?, ?, ?)");
            executeFast3.bindString(1, str);
            executeFast3.bindString(2, tL_emojiKeywordsDifference.lang_code);
            executeFast3.bindInteger(3, tL_emojiKeywordsDifference.version);
            executeFast3.bindLong(4, System.currentTimeMillis());
            executeFast3.step();
            executeFast3.dispose();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$putEmojiKeywords$214(str);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putFeaturedStickersToCache$64(ArrayList arrayList, ArrayList arrayList2, boolean z, int i, long j, boolean z2) {
        SQLitePreparedStatement executeFast;
        try {
            if (arrayList != null) {
                executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO stickers_featured VALUES(?, ?, ?, ?, ?, ?, ?)");
                executeFast.requery();
                int i2 = 4;
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    i2 += ((TLRPC.StickerSetCovered) arrayList.get(i3)).getObjectSize();
                }
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(i2);
                NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer((arrayList2.size() * 8) + 4);
                nativeByteBuffer.writeInt32(arrayList.size());
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    ((TLRPC.StickerSetCovered) arrayList.get(i4)).serializeToStream(nativeByteBuffer);
                }
                nativeByteBuffer2.writeInt32(arrayList2.size());
                for (int i5 = 0; i5 < arrayList2.size(); i5++) {
                    nativeByteBuffer2.writeInt64(((Long) arrayList2.get(i5)).longValue());
                }
                executeFast.bindInteger(1, z ? 2 : 1);
                executeFast.bindByteBuffer(2, nativeByteBuffer);
                executeFast.bindByteBuffer(3, nativeByteBuffer2);
                executeFast.bindInteger(4, i);
                executeFast.bindLong(5, j);
                executeFast.bindInteger(6, z2 ? 1 : 0);
                executeFast.bindInteger(7, z ? 1 : 0);
                executeFast.step();
                nativeByteBuffer.reuse();
                nativeByteBuffer2.reuse();
            } else {
                executeFast = getMessagesStorage().getDatabase().executeFast("UPDATE stickers_featured SET date = ? WHERE id = ? AND emoji = ?");
                executeFast.requery();
                executeFast.bindInteger(1, i);
                executeFast.bindInteger(2, z ? 2 : 1);
                executeFast.bindInteger(3, z ? 1 : 0);
                executeFast.step();
            }
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putMediaCountDatabase$136(long j, long j2, int i, int i2) {
        SQLiteDatabase database;
        String str;
        int i3;
        try {
            if (j != 0) {
                database = getMessagesStorage().getDatabase();
                str = "REPLACE INTO media_counts_topics VALUES(?, ?, ?, ?, ?)";
            } else {
                database = getMessagesStorage().getDatabase();
                str = "REPLACE INTO media_counts_v2 VALUES(?, ?, ?, ?)";
            }
            SQLitePreparedStatement executeFast = database.executeFast(str);
            executeFast.requery();
            executeFast.bindLong(1, j2);
            if (j != 0) {
                executeFast.bindLong(2, j);
                i3 = 3;
            } else {
                i3 = 2;
            }
            executeFast.bindInteger(i3, i);
            executeFast.bindInteger(i3 + 1, i2);
            executeFast.bindInteger(i3 + 2, 0);
            executeFast.step();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putMediaDatabase$138(int i, ArrayList arrayList, boolean z, long j, int i2, int i3, long j2) {
        SQLiteDatabase database;
        String str;
        if (i == 0) {
            try {
                if (arrayList.isEmpty() || z) {
                    getMessagesStorage().doneHolesInMedia(j, i2, i3, j2);
                    if (arrayList.isEmpty()) {
                        return;
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
                return;
            }
        }
        getMessagesStorage().getDatabase().beginTransaction();
        if (j2 != 0) {
            database = getMessagesStorage().getDatabase();
            str = "REPLACE INTO media_topics VALUES(?, ?, ?, ?, ?, ?)";
        } else {
            database = getMessagesStorage().getDatabase();
            str = "REPLACE INTO media_v4 VALUES(?, ?, ?, ?, ?)";
        }
        SQLitePreparedStatement executeFast = database.executeFast(str);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            TLRPC.Message message = (TLRPC.Message) it.next();
            if (canAddMessageToMedia(message)) {
                executeFast.requery();
                MessageObject.normalizeFlags(message);
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(message.getObjectSize());
                message.serializeToStream(nativeByteBuffer);
                executeFast.bindInteger(1, message.id);
                executeFast.bindLong(2, j);
                int i4 = 3;
                if (j2 != 0) {
                    executeFast.bindLong(3, j2);
                    i4 = 4;
                }
                executeFast.bindInteger(i4, message.date);
                executeFast.bindInteger(i4 + 1, i3);
                executeFast.bindByteBuffer(i4 + 2, nativeByteBuffer);
                executeFast.step();
                nativeByteBuffer.reuse();
            }
        }
        executeFast.dispose();
        if (!z || i2 != 0 || i != 0) {
            int i5 = (z && i == 0) ? 1 : ((TLRPC.Message) arrayList.get(arrayList.size() - 1)).id;
            if (i != 0) {
                getMessagesStorage().closeHolesInMedia(j, i5, ((TLRPC.Message) arrayList.get(0)).id, i3, j2);
            } else if (i2 != 0) {
                getMessagesStorage().closeHolesInMedia(j, i5, i2, i3, j2);
            } else {
                getMessagesStorage().closeHolesInMedia(j, i5, ConnectionsManager.DEFAULT_DATACENTER_ID, i3, j2);
            }
        }
        getMessagesStorage().getDatabase().commitTransaction();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putMenuBotsToCache$6(TLRPC.TL_attachMenuBots tL_attachMenuBots, long j, int i) {
        SQLitePreparedStatement executeFast;
        try {
            if (tL_attachMenuBots != null) {
                getMessagesStorage().getDatabase().executeFast("DELETE FROM attach_menu_bots").stepThis().dispose();
                executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO attach_menu_bots VALUES(?, ?, ?)");
                executeFast.requery();
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tL_attachMenuBots.getObjectSize());
                tL_attachMenuBots.serializeToStream(nativeByteBuffer);
                executeFast.bindByteBuffer(1, nativeByteBuffer);
                executeFast.bindLong(2, j);
                executeFast.bindInteger(3, i);
                executeFast.step();
                nativeByteBuffer.reuse();
            } else {
                executeFast = getMessagesStorage().getDatabase().executeFast("UPDATE attach_menu_bots SET date = ?");
                executeFast.requery();
                executeFast.bindLong(1, i);
                executeFast.step();
            }
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putPremiumPromoToCache$10(TLRPC.TL_help_premiumPromo tL_help_premiumPromo, int i) {
        SQLitePreparedStatement executeFast;
        try {
            if (tL_help_premiumPromo != null) {
                getMessagesStorage().getDatabase().executeFast("DELETE FROM premium_promo").stepThis().dispose();
                executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO premium_promo VALUES(?, ?)");
                executeFast.requery();
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tL_help_premiumPromo.getObjectSize());
                tL_help_premiumPromo.serializeToStream(nativeByteBuffer);
                executeFast.bindByteBuffer(1, nativeByteBuffer);
                executeFast.bindInteger(2, i);
                executeFast.step();
                nativeByteBuffer.reuse();
            } else {
                executeFast = getMessagesStorage().getDatabase().executeFast("UPDATE premium_promo SET date = ?");
                executeFast.requery();
                executeFast.bindInteger(1, i);
                executeFast.step();
            }
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putReactionsToCache$16(ArrayList arrayList, int i, int i2) {
        SQLitePreparedStatement executeFast;
        try {
            if (arrayList != null) {
                getMessagesStorage().getDatabase().executeFast("DELETE FROM reactions").stepThis().dispose();
                executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO reactions VALUES(?, ?, ?)");
                executeFast.requery();
                int i3 = 4;
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    i3 += ((TLRPC.TL_availableReaction) arrayList.get(i4)).getObjectSize();
                }
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(i3);
                nativeByteBuffer.writeInt32(arrayList.size());
                for (int i5 = 0; i5 < arrayList.size(); i5++) {
                    ((TLRPC.TL_availableReaction) arrayList.get(i5)).serializeToStream(nativeByteBuffer);
                }
                executeFast.bindByteBuffer(1, nativeByteBuffer);
                executeFast.bindInteger(2, i);
                executeFast.bindInteger(3, i2);
                executeFast.step();
                nativeByteBuffer.reuse();
            } else {
                executeFast = getMessagesStorage().getDatabase().executeFast("UPDATE reactions SET date = ?");
                executeFast.requery();
                executeFast.bindLong(1, i2);
                executeFast.step();
            }
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putSetToCache$47(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        try {
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO web_recent_v3 VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            executeFast.requery();
            executeFast.bindString(1, "s_" + tL_messages_stickerSet.set.id);
            executeFast.bindInteger(2, 6);
            executeFast.bindString(3, "");
            executeFast.bindString(4, "");
            executeFast.bindString(5, "");
            executeFast.bindInteger(6, 0);
            executeFast.bindInteger(7, 0);
            executeFast.bindInteger(8, 0);
            executeFast.bindInteger(9, 0);
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tL_messages_stickerSet.getObjectSize());
            tL_messages_stickerSet.serializeToStream(nativeByteBuffer);
            executeFast.bindByteBuffer(10, nativeByteBuffer);
            executeFast.step();
            nativeByteBuffer.reuse();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putStickersToCache$100(ArrayList arrayList, int i, int i2, long j) {
        SQLitePreparedStatement executeFast;
        try {
            if (arrayList != null) {
                executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO stickers_v2 VALUES(?, ?, ?, ?)");
                executeFast.requery();
                int i3 = 4;
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    i3 += ((TLRPC.TL_messages_stickerSet) arrayList.get(i4)).getObjectSize();
                }
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(i3);
                nativeByteBuffer.writeInt32(arrayList.size());
                for (int i5 = 0; i5 < arrayList.size(); i5++) {
                    ((TLRPC.TL_messages_stickerSet) arrayList.get(i5)).serializeToStream(nativeByteBuffer);
                }
                executeFast.bindInteger(1, i + 1);
                executeFast.bindByteBuffer(2, nativeByteBuffer);
                executeFast.bindInteger(3, i2);
                executeFast.bindLong(4, j);
                executeFast.step();
                nativeByteBuffer.reuse();
            } else {
                executeFast = getMessagesStorage().getDatabase().executeFast("UPDATE stickers_v2 SET date = ?");
                executeFast.requery();
                executeFast.bindLong(1, i2);
                executeFast.step();
            }
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$removeInline$152(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeMultipleStickerSets$108(boolean[] zArr, ArrayList arrayList, int i, int[] iArr) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            markSetUninstalling(((TLRPC.TL_messages_stickerSet) arrayList.get(i2)).set.id, false);
            ((TLRPC.TL_messages_stickerSet) arrayList.get(i2)).set.archived = false;
            this.stickerSets[i].add(iArr[i2], (TLRPC.TL_messages_stickerSet) arrayList.get(i2));
            this.stickerSetsById.put(((TLRPC.TL_messages_stickerSet) arrayList.get(i2)).set.id, (TLRPC.TL_messages_stickerSet) arrayList.get(i2));
            this.installedStickerSetsById.put(((TLRPC.TL_messages_stickerSet) arrayList.get(i2)).set.id, (TLRPC.TL_messages_stickerSet) arrayList.get(i2));
            this.stickerSetsByName.put(((TLRPC.TL_messages_stickerSet) arrayList.get(i2)).set.short_name, (TLRPC.TL_messages_stickerSet) arrayList.get(i2));
            this.removingStickerSetsUndos.remove(((TLRPC.TL_messages_stickerSet) arrayList.get(i2)).set.id);
        }
        ArrayList<TLRPC.TL_messages_stickerSet> arrayList2 = this.stickerSets[i];
        int i3 = this.loadDate[i];
        long[] jArr = this.loadHash;
        long calcStickersHash = calcStickersHash(arrayList2);
        jArr[i] = calcStickersHash;
        putStickersToCache(i, arrayList2, i3, calcStickersHash);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(i), Boolean.TRUE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeMultipleStickerSets$109(boolean[] zArr, ArrayList arrayList, Context context, BaseFragment baseFragment, int i) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            toggleStickerSetInternal(context, 0, baseFragment, true, (TLObject) arrayList.get(i2), ((TLRPC.TL_messages_stickerSet) arrayList.get(i2)).set, i, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$removePeer$154(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeRecentGif$24(TLRPC.TL_messages_saveGif tL_messages_saveGif, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null || !FileRefController.isFileRefError(tL_error.text)) {
            return;
        }
        getFileRefController().requestReference("gif", tL_messages_saveGif);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeRecentGif$25(TLRPC.Document document) {
        try {
            getMessagesStorage().getDatabase().executeFast("DELETE FROM web_recent_v3 WHERE id = '" + document.id + "' AND type = 2").stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$removeWebapp$153(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$reorderStickers$54(ArrayList arrayList, TLRPC.TL_messages_stickerSet tL_messages_stickerSet, TLRPC.TL_messages_stickerSet tL_messages_stickerSet2) {
        int indexOf = arrayList.indexOf(Long.valueOf(tL_messages_stickerSet.set.id));
        int indexOf2 = arrayList.indexOf(Long.valueOf(tL_messages_stickerSet2.set.id));
        if (indexOf > indexOf2) {
            return 1;
        }
        return indexOf < indexOf2 ? -1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$replaceStickerSet$28(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        LongSparseArray stickerByIds = getStickerByIds(4);
        for (int i = 0; i < tL_messages_stickerSet.documents.size(); i++) {
            TLRPC.Document document = tL_messages_stickerSet.documents.get(i);
            stickerByIds.put(document.id, document);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$saveDraft$188(TLObject tLObject, TLRPC.TL_error tL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveDraft$189(long j, long j2, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            if (messages_messages.messages.isEmpty()) {
                return;
            }
            saveDraftReplyMessage(j, j2, messages_messages.messages.get(0));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveDraft$190(long j, long j2, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tL_error == null) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            if (messages_messages.messages.isEmpty()) {
                return;
            }
            saveDraftReplyMessage(j, j2, messages_messages.messages.get(0));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$saveDraft$191(int i, long j, long j2, final long j3, final long j4) {
        TLRPC.Message message;
        RequestDelegate requestDelegate;
        ConnectionsManager connectionsManager;
        TLRPC.TL_messages_getMessages tL_messages_getMessages;
        TLRPC.Message message2;
        NativeByteBuffer byteBufferValue;
        try {
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, replydata FROM messages_v2 WHERE mid = %d and uid = %d", Integer.valueOf(i), Long.valueOf(j)), new Object[0]);
            if (queryFinalized.next()) {
                NativeByteBuffer byteBufferValue2 = queryFinalized.byteBufferValue(0);
                if (byteBufferValue2 != null) {
                    message2 = TLRPC.Message.TLdeserialize(byteBufferValue2, byteBufferValue2.readInt32(false), false);
                    message2.readAttachPath(byteBufferValue2, getUserConfig().clientUserId);
                    byteBufferValue2.reuse();
                } else {
                    message2 = null;
                }
                if (message2 != null) {
                    ArrayList<Long> arrayList = new ArrayList<>();
                    ArrayList<Long> arrayList2 = new ArrayList<>();
                    LongSparseArray longSparseArray = new LongSparseArray();
                    LongSparseArray longSparseArray2 = new LongSparseArray();
                    try {
                        TLRPC.MessageReplyHeader messageReplyHeader = message2.reply_to;
                        if (messageReplyHeader != null && messageReplyHeader.reply_to_msg_id != 0) {
                            if (!queryFinalized.isNull(1) && (byteBufferValue = queryFinalized.byteBufferValue(1)) != null) {
                                TLRPC.Message TLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                                message2.replyMessage = TLdeserialize;
                                TLdeserialize.readAttachPath(byteBufferValue, getUserConfig().clientUserId);
                                byteBufferValue.reuse();
                                TLRPC.Message message3 = message2.replyMessage;
                                if (message3 != null) {
                                    MessagesStorage.addUsersAndChatsFromMessage(message3, arrayList, arrayList2, null);
                                }
                            }
                            if (message2.replyMessage == null) {
                                MessagesStorage.addReplyMessages(message2, longSparseArray, longSparseArray2);
                            }
                        }
                    } catch (Exception e) {
                        getMessagesStorage().checkSQLException(e);
                    }
                    getMessagesStorage().loadReplyMessages(longSparseArray, longSparseArray2, arrayList, arrayList2, 0);
                }
                message = message2;
            } else {
                message = null;
            }
            queryFinalized.dispose();
            if (message != null) {
                saveDraftReplyMessage(j3, j4, message);
                return;
            }
            if (j2 != 0) {
                TLRPC.TL_channels_getMessages tL_channels_getMessages = new TLRPC.TL_channels_getMessages();
                tL_channels_getMessages.channel = getMessagesController().getInputChannel(j2);
                tL_channels_getMessages.id.add(Integer.valueOf(i));
                ConnectionsManager connectionsManager2 = getConnectionsManager();
                requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda204
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        MediaDataController.this.lambda$saveDraft$189(j3, j4, tLObject, tL_error);
                    }
                };
                tL_messages_getMessages = tL_channels_getMessages;
                connectionsManager = connectionsManager2;
            } else {
                TLRPC.TL_messages_getMessages tL_messages_getMessages2 = new TLRPC.TL_messages_getMessages();
                tL_messages_getMessages2.id.add(Integer.valueOf(i));
                ConnectionsManager connectionsManager3 = getConnectionsManager();
                requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda205
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        MediaDataController.this.lambda$saveDraft$190(j3, j4, tLObject, tL_error);
                    }
                };
                tL_messages_getMessages = tL_messages_getMessages2;
                connectionsManager = connectionsManager3;
            }
            connectionsManager.sendRequest(tL_messages_getMessages, requestDelegate);
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveDraftReplyMessage$192(long j, long j2, TLRPC.Message message) {
        TLRPC.InputReplyTo inputReplyTo;
        String str;
        LongSparseArray longSparseArray = (LongSparseArray) this.drafts.get(j);
        TLRPC.DraftMessage draftMessage = longSparseArray != null ? (TLRPC.DraftMessage) longSparseArray.get(j2) : null;
        if (draftMessage == null || (inputReplyTo = draftMessage.reply_to) == null || inputReplyTo.reply_to_msg_id != message.id) {
            return;
        }
        LongSparseArray longSparseArray2 = (LongSparseArray) this.draftMessages.get(j);
        if (longSparseArray2 == null) {
            longSparseArray2 = new LongSparseArray();
            this.draftMessages.put(j, longSparseArray2);
        }
        longSparseArray2.put(j2, message);
        SerializedData serializedData = new SerializedData(message.getObjectSize());
        message.serializeToStream(serializedData);
        SharedPreferences.Editor edit = this.draftPreferences.edit();
        if (j2 == 0) {
            str = "r_" + j;
        } else {
            str = "rt_" + j + "_" + j2;
        }
        edit.putString(str, Utilities.bytesToHex(serializedData.toByteArray())).commit();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.newDraftReceived, Long.valueOf(j));
        serializedData.cleanup();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$savePeer$158(long j, int i, double d) {
        try {
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO chat_hints VALUES(?, ?, ?, ?)");
            executeFast.requery();
            executeFast.bindLong(1, j);
            executeFast.bindInteger(2, i);
            executeFast.bindDouble(3, d);
            executeFast.bindInteger(4, ((int) System.currentTimeMillis()) / 1000);
            executeFast.step();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$savePinnedMessages$166(ArrayList arrayList, long j) {
        try {
            getMessagesStorage().getDatabase().beginTransaction();
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO chat_pinned_v2 VALUES(?, ?, ?)");
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                TLRPC.Message message = (TLRPC.Message) arrayList.get(i);
                MessageObject.normalizeFlags(message);
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(message.getObjectSize());
                message.serializeToStream(nativeByteBuffer);
                executeFast.requery();
                executeFast.bindLong(1, j);
                executeFast.bindInteger(2, message.id);
                executeFast.bindByteBuffer(3, nativeByteBuffer);
                executeFast.step();
                nativeByteBuffer.reuse();
            }
            executeFast.dispose();
            getMessagesStorage().getDatabase().commitTransaction();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveReplyMessages$178(boolean z, ArrayList arrayList, LongSparseArray longSparseArray) {
        SQLitePreparedStatement executeFast;
        SQLitePreparedStatement executeFast2;
        ArrayList arrayList2;
        try {
            getMessagesStorage().getDatabase().beginTransaction();
            if (z) {
                executeFast = getMessagesStorage().getDatabase().executeFast("UPDATE scheduled_messages_v2 SET replydata = ?, reply_to_message_id = ? WHERE mid = ? AND uid = ?");
                executeFast2 = null;
            } else {
                executeFast = getMessagesStorage().getDatabase().executeFast("UPDATE messages_v2 SET replydata = ?, reply_to_message_id = ? WHERE mid = ? AND uid = ?");
                executeFast2 = getMessagesStorage().getDatabase().executeFast("UPDATE messages_topics SET replydata = ?, reply_to_message_id = ? WHERE mid = ? AND uid = ?");
            }
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC.Message message = (TLRPC.Message) arrayList.get(i);
                SparseArray sparseArray = (SparseArray) longSparseArray.get(MessageObject.getDialogId(message));
                if (sparseArray != null && (arrayList2 = (ArrayList) sparseArray.get(message.id)) != null) {
                    MessageObject.normalizeFlags(message);
                    NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(message.getObjectSize());
                    message.serializeToStream(nativeByteBuffer);
                    for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                        MessageObject messageObject = (MessageObject) arrayList2.get(i2);
                        int i3 = 0;
                        while (i3 < 2) {
                            SQLitePreparedStatement sQLitePreparedStatement = i3 == 0 ? executeFast : executeFast2;
                            if (sQLitePreparedStatement != null) {
                                sQLitePreparedStatement.requery();
                                sQLitePreparedStatement.bindByteBuffer(1, nativeByteBuffer);
                                sQLitePreparedStatement.bindInteger(2, message.id);
                                sQLitePreparedStatement.bindInteger(3, messageObject.getId());
                                sQLitePreparedStatement.bindLong(4, messageObject.getDialogId());
                                sQLitePreparedStatement.step();
                            }
                            i3++;
                        }
                    }
                    nativeByteBuffer.reuse();
                }
            }
            executeFast.dispose();
            if (executeFast2 != null) {
                executeFast2.dispose();
            }
            getMessagesStorage().getDatabase().commitTransaction();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveStickerSetIntoCache$40(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        try {
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO stickersets2 VALUES(?, ?, ?, ?, ?)");
            executeFast.requery();
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tL_messages_stickerSet.getObjectSize());
            tL_messages_stickerSet.serializeToStream(nativeByteBuffer);
            executeFast.bindLong(1, tL_messages_stickerSet.set.id);
            executeFast.bindByteBuffer(2, nativeByteBuffer);
            executeFast.bindInteger(3, tL_messages_stickerSet.set.hash);
            executeFast.bindLong(4, System.currentTimeMillis());
            String str = tL_messages_stickerSet.set.short_name;
            executeFast.bindString(5, str == null ? "" : str.toLowerCase());
            executeFast.step();
            nativeByteBuffer.reuse();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveToRingtones$203(TLObject tLObject, TLRPC.Document document) {
        if (tLObject != null) {
            if (tLObject instanceof TLRPC.TL_account_savedRingtoneConverted) {
                this.ringtoneDataStore.addTone(((TLRPC.TL_account_savedRingtoneConverted) tLObject).document);
            } else {
                this.ringtoneDataStore.addTone(document);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveToRingtones$204(final TLRPC.Document document, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda82
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$saveToRingtones$203(tLObject, document);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchMessagesInChat$118(long j, TLObject tLObject, TLRPC.TL_messages_search tL_messages_search, long j2, int i, int i2, long j3, TLRPC.User user, TLRPC.Chat chat, boolean z, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        if (this.lastMergeDialogId == j) {
            this.mergeReqId = 0;
            if (tLObject != null) {
                TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
                this.messagesSearchEndReached[1] = messages_messages.messages.isEmpty();
                this.messagesSearchCount[1] = messages_messages instanceof TLRPC.TL_messages_messagesSlice ? messages_messages.count : messages_messages.messages.size();
            } else {
                this.messagesSearchEndReached[1] = true;
                this.messagesSearchCount[1] = 0;
            }
            searchMessagesInChat(tL_messages_search.q, j2, j, i, i2, j3, true, user, chat, z, visibleReaction);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchMessagesInChat$119(final long j, final TLRPC.TL_messages_search tL_messages_search, final long j2, final int i, final int i2, final long j3, final TLRPC.User user, final TLRPC.Chat chat, final boolean z, final ReactionsLayoutInBubble.VisibleReaction visibleReaction, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda128
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$searchMessagesInChat$118(j, tLObject, tL_messages_search, j2, i, i2, j3, user, chat, z, visibleReaction);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchMessagesInChat$120(int i, int i2, int i3, long j, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4) {
        if (i == this.lastReqId) {
            this.loadedPredirectedSearchLocal = arrayList.size() == i2;
            this.loadingSearchLocal = false;
            getMessagesController().putUsers(arrayList2, true);
            getMessagesController().putChats(arrayList3, true);
            AnimatedEmojiDrawable.getDocumentFetcher(this.currentAccount).processDocuments(arrayList4);
            this.searchLocalResultMessages = arrayList;
            updateSearchResults();
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.chatSearchResultsAvailable, Integer.valueOf(i3), 0, Integer.valueOf(getMask()), Long.valueOf(j), Integer.valueOf(this.lastReturnedNum), Integer.valueOf(getSearchCount()), Boolean.TRUE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchMessagesInChat$121(TLRPC.TL_messages_search tL_messages_search, long j, long j2, int i, TLRPC.messages_Messages messages_messages, ArrayList arrayList, boolean z, boolean z2, long j3, long j4, TLRPC.User user, TLRPC.Chat chat) {
        if (tL_messages_search.offset_id == 0 && j == j2) {
            this.lastReturnedNum = 0;
            this.searchServerResultMessages.clear();
            this.searchServerResultMessagesMap[0].clear();
            this.searchServerResultMessagesMap[1].clear();
            this.messagesSearchCount[0] = 0;
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.chatSearchResultsLoading, Integer.valueOf(i));
        }
        int min = Math.min(messages_messages.messages.size(), tL_messages_search.limit - 1);
        int i2 = 0;
        boolean z3 = false;
        while (i2 < min) {
            MessageObject messageObject = (MessageObject) arrayList.get(i2);
            this.searchServerResultMessages.add(messageObject);
            this.searchServerResultMessagesMap[j == j2 ? (char) 0 : (char) 1].put(messageObject.getId(), messageObject);
            i2++;
            z3 = true;
        }
        updateSearchResults();
        this.messagesSearchEndReached[j == j2 ? (char) 0 : (char) 1] = messages_messages.messages.size() < tL_messages_search.limit;
        this.messagesSearchCount[j == j2 ? (char) 0 : (char) 1] = ((messages_messages instanceof TLRPC.TL_messages_messagesSlice) || (messages_messages instanceof TLRPC.TL_messages_channelMessages)) ? messages_messages.count : messages_messages.messages.size();
        if (this.searchServerResultMessages.isEmpty()) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.chatSearchResultsAvailable, Integer.valueOf(i), 0, Integer.valueOf(getMask()), 0L, 0, 0, Boolean.valueOf(z));
        } else if (z3) {
            if (this.lastReturnedNum >= this.searchResultMessages.size()) {
                this.lastReturnedNum = this.searchResultMessages.size() - 1;
            }
            MessageObject messageObject2 = this.searchResultMessages.get(this.lastReturnedNum);
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.chatSearchResultsAvailable, Integer.valueOf(i), Integer.valueOf(messageObject2.getId()), Integer.valueOf(getMask()), Long.valueOf(messageObject2.getDialogId()), Integer.valueOf(this.lastReturnedNum), Integer.valueOf(getSearchCount()), Boolean.valueOf(z));
        } else if (z2) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.chatSearchResultsAvailable, Integer.valueOf(i), 0, Integer.valueOf(getMask()), Long.valueOf(j2), Integer.valueOf(this.lastReturnedNum), Integer.valueOf(getSearchCount()), Boolean.FALSE);
        }
        if (j == j2) {
            boolean[] zArr = this.messagesSearchEndReached;
            if (!zArr[0] || j3 == 0 || zArr[1]) {
                return;
            }
            searchMessagesInChat(this.lastSearchQuery, j2, j3, i, 0, j4, true, user, chat, z, this.lastReaction);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchMessagesInChat$122(int i, final boolean z, TLObject tLObject, final TLRPC.TL_messages_search tL_messages_search, final long j, final long j2, final int i2, final ArrayList arrayList, final boolean z2, final long j3, final long j4, final TLRPC.User user, final TLRPC.Chat chat) {
        if (i == this.lastReqId) {
            this.reqId = 0;
            if (!z) {
                this.loadingMoreSearchMessages = false;
            }
            if (tLObject != null) {
                final TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
                int i3 = 0;
                while (i3 < messages_messages.messages.size()) {
                    TLRPC.Message message = messages_messages.messages.get(i3);
                    if ((message instanceof TLRPC.TL_messageEmpty) || (message.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                        messages_messages.messages.remove(i3);
                        i3--;
                    }
                    i3++;
                }
                getMessagesStorage().putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
                getMessagesController().putUsers(messages_messages.users, false);
                getMessagesController().putChats(messages_messages.chats, false);
                Runnable runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda114
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$searchMessagesInChat$121(tL_messages_search, j, j2, i2, messages_messages, arrayList, z, z2, j3, j4, user, chat);
                    }
                };
                if (z2) {
                    loadReplyMessagesForMessages(arrayList, j2, 0, this.lastReplyMessageId, runnable, i2, null);
                } else {
                    runnable.run();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchMessagesInChat$123(final TLRPC.TL_messages_search tL_messages_search, final boolean z, String str, boolean z2, final int i, final boolean z3, final long j, final long j2, final int i2, final long j3, final long j4, final TLRPC.User user, final TLRPC.Chat chat, final TLObject tLObject, TLRPC.TL_error tL_error) {
        final ArrayList arrayList = new ArrayList();
        if (tL_error == null) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            int min = Math.min(messages_messages.messages.size(), tL_messages_search.limit - 1);
            for (int i3 = 0; i3 < min; i3++) {
                MessageObject messageObject = new MessageObject(this.currentAccount, messages_messages.messages.get(i3), null, null, null, null, null, true, true, 0L, false, false, z);
                if (messageObject.hasValidGroupId()) {
                    messageObject.isPrimaryGroupMessage = true;
                }
                messageObject.setQuery(str, !z2);
                arrayList.add(messageObject);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda179
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$searchMessagesInChat$122(i, z3, tLObject, tL_messages_search, j, j2, i2, arrayList, z, j3, j4, user, chat);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$setPlaceholderImage$31(String str, BackupImageView backupImageView, String str2, TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        TLRPC.Document document;
        if (tL_messages_stickerSet == null) {
            return;
        }
        int i = 0;
        while (true) {
            if (i >= tL_messages_stickerSet.packs.size()) {
                break;
            }
            if (tL_messages_stickerSet.packs.get(i).documents.isEmpty() || !TextUtils.equals(tL_messages_stickerSet.packs.get(i).emoticon, str)) {
                i++;
            } else {
                long longValue = tL_messages_stickerSet.packs.get(i).documents.get(0).longValue();
                for (int i2 = 0; i2 < tL_messages_stickerSet.documents.size(); i2++) {
                    if (tL_messages_stickerSet.documents.get(i2).id == longValue) {
                        document = tL_messages_stickerSet.documents.get(i2);
                        break;
                    }
                }
            }
        }
        document = null;
        if (document != null) {
            backupImageView.setImage(ImageLocation.getForDocument(document), str2, DocumentObject.getSvgThumb(document, Theme.key_windowBackgroundWhiteGrayIcon, 0.2f, 1.0f, null), 0, document);
            backupImageView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$static$160(TLRPC.MessageEntity messageEntity, TLRPC.MessageEntity messageEntity2) {
        int i = messageEntity.offset;
        int i2 = messageEntity2.offset;
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleStickerSet$106(boolean[] zArr, TLRPC.StickerSet stickerSet, int i, int i2, TLRPC.TL_messages_stickerSet tL_messages_stickerSet, Runnable runnable) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        markSetUninstalling(stickerSet.id, false);
        stickerSet.archived = false;
        this.stickerSets[i].add(i2, tL_messages_stickerSet);
        this.stickerSetsById.put(stickerSet.id, tL_messages_stickerSet);
        this.installedStickerSetsById.put(stickerSet.id, tL_messages_stickerSet);
        String str = stickerSet.short_name;
        if (str != null) {
            this.stickerSetsByName.put(str.toLowerCase(), tL_messages_stickerSet);
        }
        this.removingStickerSetsUndos.remove(stickerSet.id);
        this.loadHash[i] = calcStickersHash(this.stickerSets[i]);
        putStickersToCache(i, this.stickerSets[i], this.loadDate[i], this.loadHash[i]);
        if (runnable != null) {
            runnable.run();
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(i), Boolean.TRUE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleStickerSet$107(boolean[] zArr, Context context, int i, BaseFragment baseFragment, boolean z, TLObject tLObject, TLRPC.StickerSet stickerSet, int i2) {
        if (zArr[0]) {
            return;
        }
        zArr[0] = true;
        toggleStickerSetInternal(context, i, baseFragment, z, tLObject, stickerSet, i2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleStickerSetInternal$110(TLRPC.StickerSet stickerSet, ArrayList arrayList) {
        markSetInstalling(stickerSet.id, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleStickerSetInternal$111(final TLRPC.StickerSet stickerSet, TLObject tLObject, BaseFragment baseFragment, boolean z, int i, TLRPC.TL_error tL_error, boolean z2, Context context, TLObject tLObject2) {
        this.removingStickerSetsUndos.remove(stickerSet.id);
        if (tLObject instanceof TLRPC.TL_messages_stickerSetInstallResultArchive) {
            processStickerSetInstallResultArchive(baseFragment, z, i, (TLRPC.TL_messages_stickerSetInstallResultArchive) tLObject);
        }
        loadStickers(i, false, false, true, new Utilities.Callback() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda129
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                MediaDataController.this.lambda$toggleStickerSetInternal$110(stickerSet, (ArrayList) obj);
            }
        });
        if (tL_error == null && z2 && baseFragment != null) {
            Bulletin.make(baseFragment, new StickerSetBulletinLayout(context, tLObject2, 2, null, baseFragment.getResourceProvider()), 1500).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleStickerSetInternal$112(final TLRPC.StickerSet stickerSet, final BaseFragment baseFragment, final boolean z, final int i, final boolean z2, final Context context, final TLObject tLObject, final TLObject tLObject2, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$toggleStickerSetInternal$111(stickerSet, tLObject2, baseFragment, z, i, tL_error, z2, context, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleStickerSetInternal$113(TLRPC.StickerSet stickerSet, ArrayList arrayList) {
        markSetUninstalling(stickerSet.id, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleStickerSetInternal$114(final TLRPC.StickerSet stickerSet, int i) {
        this.removingStickerSetsUndos.remove(stickerSet.id);
        loadStickers(i, false, true, false, new Utilities.Callback() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda166
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                MediaDataController.this.lambda$toggleStickerSetInternal$113(stickerSet, (ArrayList) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleStickerSetInternal$115(final TLRPC.StickerSet stickerSet, final int i, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda60
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$toggleStickerSetInternal$114(stickerSet, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleStickerSets$116(int i, TLObject tLObject, BaseFragment baseFragment, boolean z, int i2) {
        if (i == 0) {
            loadStickers(i2, false, true);
            return;
        }
        if (tLObject instanceof TLRPC.TL_messages_stickerSetInstallResultArchive) {
            processStickerSetInstallResultArchive(baseFragment, z, i2, (TLRPC.TL_messages_stickerSetInstallResultArchive) tLObject);
        }
        loadStickers(i2, false, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleStickerSets$117(final int i, final BaseFragment baseFragment, final boolean z, final int i2, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda86
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$toggleStickerSets$116(i, tLObject, baseFragment, z, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateBotInfo$202(TLRPC.TL_updateBotCommands tL_updateBotCommands, long j) {
        try {
            TL_bots.BotInfo loadBotInfoInternal = loadBotInfoInternal(tL_updateBotCommands.bot_id, j);
            if (loadBotInfoInternal != null) {
                loadBotInfoInternal.commands = tL_updateBotCommands.commands;
            }
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO bot_info_v2 VALUES(?, ?, ?)");
            executeFast.requery();
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(loadBotInfoInternal.getObjectSize());
            loadBotInfoInternal.serializeToStream(nativeByteBuffer);
            executeFast.bindLong(1, loadBotInfoInternal.user_id);
            executeFast.bindLong(2, j);
            executeFast.bindByteBuffer(3, nativeByteBuffer);
            executeFast.step();
            nativeByteBuffer.reuse();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateEmojiStatuses$234(int i, TLRPC.TL_account_emojiStatuses tL_account_emojiStatuses) {
        try {
            getMessagesStorage().getDatabase().executeFast("DELETE FROM emoji_statuses WHERE type = " + i).stepThis().dispose();
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("INSERT INTO emoji_statuses VALUES(?, ?)");
            executeFast.requery();
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tL_account_emojiStatuses.getObjectSize());
            tL_account_emojiStatuses.serializeToStream(nativeByteBuffer);
            executeFast.bindByteBuffer(1, nativeByteBuffer);
            executeFast.bindInteger(2, i);
            executeFast.step();
            nativeByteBuffer.reuse();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.emojiStatusesFetching[i] = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyAnimatedStickerMessageInternal$69(String str, TLObject tLObject) {
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        ArrayList<TLRPC.Message> arrayList = this.verifyingMessages.get(str);
        if (tLObject != null) {
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet2 = (TLRPC.TL_messages_stickerSet) tLObject;
            storeTempStickerSet(tL_messages_stickerSet2);
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                TLRPC.Message message = arrayList.get(i);
                TLRPC.Document document = MessageObject.getDocument(message);
                int size2 = tL_messages_stickerSet2.documents.size();
                int i2 = 0;
                while (true) {
                    if (i2 >= size2) {
                        tL_messages_stickerSet = tL_messages_stickerSet2;
                        break;
                    }
                    TLRPC.Document document2 = tL_messages_stickerSet2.documents.get(i2);
                    tL_messages_stickerSet = tL_messages_stickerSet2;
                    if (document2.id == document.id && document2.dc_id == document.dc_id) {
                        message.stickerVerified = 1;
                        break;
                    } else {
                        i2++;
                        tL_messages_stickerSet2 = tL_messages_stickerSet;
                    }
                }
                if (message.stickerVerified == 0) {
                    message.stickerVerified = 2;
                }
                i++;
                tL_messages_stickerSet2 = tL_messages_stickerSet;
            }
        } else {
            int size3 = arrayList.size();
            for (int i3 = 0; i3 < size3; i3++) {
                arrayList.get(i3).stickerVerified = 2;
            }
        }
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didVerifyMessagesStickers, arrayList);
        getMessagesStorage().updateMessageVerifyFlags(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyAnimatedStickerMessageInternal$70(final String str, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda113
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$verifyAnimatedStickerMessageInternal$69(str, tLObject);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0077  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void loadAvatarConstructor(final boolean z) {
        String string;
        String str;
        TLRPC.TL_emojiList tL_emojiList;
        Throwable th;
        final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("avatar_constructor" + this.currentAccount, 0);
        TLRPC.TL_emojiList tL_emojiList2 = null;
        if (z) {
            string = sharedPreferences.getString("profile", null);
            str = "profile_last_check";
        } else {
            string = sharedPreferences.getString("group", null);
            str = "group_last_check";
        }
        long j = sharedPreferences.getLong(str, 0L);
        if (string != null) {
            SerializedData serializedData = new SerializedData(Utilities.hexToBytes(string));
            try {
                tL_emojiList = (TLRPC.TL_emojiList) TLRPC.EmojiList.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                try {
                    if (z) {
                        this.profileAvatarConstructorDefault = tL_emojiList;
                    } else {
                        this.groupAvatarConstructorDefault = tL_emojiList;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    FileLog.e(th);
                    tL_emojiList2 = tL_emojiList;
                    if (tL_emojiList2 != null) {
                    }
                    TLRPC.TL_account_getDefaultProfilePhotoEmojis tL_account_getDefaultProfilePhotoEmojis = new TLRPC.TL_account_getDefaultProfilePhotoEmojis();
                    if (tL_emojiList2 != null) {
                    }
                    getConnectionsManager().sendRequest(tL_account_getDefaultProfilePhotoEmojis, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda171
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            MediaDataController.this.lambda$loadAvatarConstructor$242(sharedPreferences, z, tLObject, tL_error);
                        }
                    });
                }
            } catch (Throwable th3) {
                tL_emojiList = null;
                th = th3;
            }
            tL_emojiList2 = tL_emojiList;
        }
        if (tL_emojiList2 != null || System.currentTimeMillis() - j > 86400000 || BuildVars.DEBUG_PRIVATE_VERSION) {
            TLRPC.TL_account_getDefaultProfilePhotoEmojis tL_account_getDefaultProfilePhotoEmojis2 = new TLRPC.TL_account_getDefaultProfilePhotoEmojis();
            if (tL_emojiList2 != null) {
                tL_account_getDefaultProfilePhotoEmojis2.hash = tL_emojiList2.hash;
            }
            getConnectionsManager().sendRequest(tL_account_getDefaultProfilePhotoEmojis2, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda171
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadAvatarConstructor$242(sharedPreferences, z, tLObject, tL_error);
                }
            });
        }
    }

    private TL_bots.BotInfo loadBotInfoInternal(long j, long j2) {
        TL_bots.BotInfo botInfo;
        NativeByteBuffer byteBufferValue;
        SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT info FROM bot_info_v2 WHERE uid = %d AND dialogId = %d", Long.valueOf(j), Long.valueOf(j2)), new Object[0]);
        if (!queryFinalized.next() || queryFinalized.isNull(0) || (byteBufferValue = queryFinalized.byteBufferValue(0)) == null) {
            botInfo = null;
        } else {
            botInfo = TL_bots.BotInfo.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
            byteBufferValue.reuse();
        }
        queryFinalized.dispose();
        return botInfo;
    }

    private void loadDraftVoiceMessages() {
        if (this.draftVoicesLoaded) {
            return;
        }
        Set<Map.Entry<String, ?>> entrySet = ApplicationLoader.applicationContext.getSharedPreferences("2voicedrafts_" + this.currentAccount, 0).getAll().entrySet();
        this.draftVoices.clear();
        for (Map.Entry<String, ?> entry : entrySet) {
            String key = entry.getKey();
            DraftVoice fromString = DraftVoice.fromString((String) entry.getValue());
            if (fromString != null) {
                this.draftVoices.put(Long.parseLong(key), fromString);
            }
        }
        this.draftVoicesLoaded = true;
    }

    private void loadGroupStickerSet(final TLRPC.StickerSet stickerSet, boolean z) {
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda99
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadGroupStickerSet$44(stickerSet);
                }
            });
            return;
        }
        TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
        TLRPC.TL_inputStickerSetID tL_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
        tL_messages_getStickerSet.stickerset = tL_inputStickerSetID;
        tL_inputStickerSetID.id = stickerSet.id;
        tL_inputStickerSetID.access_hash = stickerSet.access_hash;
        getConnectionsManager().sendRequest(tL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda100
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$loadGroupStickerSet$46(tLObject, tL_error);
            }
        });
    }

    private void loadMediaDatabase(long j, int i, int i2, int i3, int i4, long j2, ReactionsLayoutInBubble.VisibleReaction visibleReaction, int i5, boolean z, int i6, int i7) {
        1 r15 = new 1(i, j, i3, j2, i4, visibleReaction, i2, i5, i6, z, i7);
        MessagesStorage messagesStorage = getMessagesStorage();
        messagesStorage.getStorageQueue().postRunnable(r15);
        messagesStorage.bindTaskToGuid(r15, i5);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0184 A[Catch: Exception -> 0x017a, TryCatch #0 {Exception -> 0x017a, blocks: (B:54:0x0173, B:55:0x017e, B:57:0x0184, B:59:0x018a, B:60:0x0196, B:62:0x019c, B:65:0x01af, B:68:0x01c1), top: B:53:0x0173 }] */
    /* JADX WARN: Removed duplicated region for block: B:72:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r13v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v2 */
    /* JADX WARN: Type inference failed for: r13v4, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r3v1, types: [java.lang.Object[]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private ArrayList<MessageObject> loadPinnedMessageInternal(final long j, final long j2, ArrayList<Integer> arrayList, boolean z) {
        ?? join;
        ArrayList<TLRPC.User> arrayList2;
        ArrayList<TLRPC.Chat> arrayList3;
        try {
            ArrayList<Integer> arrayList4 = new ArrayList<>(arrayList);
            if (j2 != 0) {
                join = new StringBuilder();
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    Integer num = arrayList.get(i);
                    if (join.length() != 0) {
                        join.append(",");
                    }
                    join.append(num);
                }
            } else {
                join = TextUtils.join(",", arrayList);
            }
            ArrayList<TLRPC.Message> arrayList5 = new ArrayList<>();
            ArrayList<TLRPC.User> arrayList6 = new ArrayList<>();
            ArrayList<TLRPC.Chat> arrayList7 = new ArrayList<>();
            ArrayList<Long> arrayList8 = new ArrayList<>();
            ArrayList arrayList9 = new ArrayList();
            long j3 = getUserConfig().clientUserId;
            boolean z2 = false;
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid, date FROM messages_v2 WHERE mid IN (%s) AND uid = %d", new Object[]{join, Long.valueOf(j)}), new Object[0]);
            while (queryFinalized.next()) {
                NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(z2 ? 1 : 0);
                if (byteBufferValue != null) {
                    TLRPC.Message TLdeserialize = TLRPC.Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(z2), z2);
                    if (!(TLdeserialize.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                        TLdeserialize.readAttachPath(byteBufferValue, j3);
                        TLdeserialize.id = queryFinalized.intValue(1);
                        TLdeserialize.date = queryFinalized.intValue(2);
                        TLdeserialize.dialog_id = j;
                        MessagesStorage.addUsersAndChatsFromMessage(TLdeserialize, arrayList8, arrayList9, null);
                        arrayList5.add(TLdeserialize);
                        arrayList4.remove(Integer.valueOf(TLdeserialize.id));
                    }
                    byteBufferValue.reuse();
                    z2 = false;
                }
            }
            queryFinalized.dispose();
            if (!arrayList4.isEmpty()) {
                SQLiteCursor queryFinalized2 = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data FROM chat_pinned_v2 WHERE uid = %d AND mid IN (%s)", Long.valueOf(j), TextUtils.join(",", arrayList4)), new Object[0]);
                while (queryFinalized2.next()) {
                    NativeByteBuffer byteBufferValue2 = queryFinalized2.byteBufferValue(0);
                    if (byteBufferValue2 != null) {
                        TLRPC.Message TLdeserialize2 = TLRPC.Message.TLdeserialize(byteBufferValue2, byteBufferValue2.readInt32(false), false);
                        if (!(TLdeserialize2.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                            TLdeserialize2.readAttachPath(byteBufferValue2, j3);
                            TLdeserialize2.dialog_id = j;
                            MessagesStorage.addUsersAndChatsFromMessage(TLdeserialize2, arrayList8, arrayList9, null);
                            arrayList5.add(TLdeserialize2);
                            arrayList4.remove(Integer.valueOf(TLdeserialize2.id));
                        }
                        byteBufferValue2.reuse();
                    }
                }
                queryFinalized2.dispose();
            }
            if (!arrayList4.isEmpty()) {
                if (j2 == 0) {
                    final TLRPC.TL_messages_getMessages tL_messages_getMessages = new TLRPC.TL_messages_getMessages();
                    tL_messages_getMessages.id = arrayList4;
                    try {
                        getConnectionsManager().sendRequest(tL_messages_getMessages, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda122
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                MediaDataController.this.lambda$loadPinnedMessageInternal$165(j, tL_messages_getMessages, tLObject, tL_error);
                            }
                        });
                        if (!arrayList5.isEmpty()) {
                            return null;
                        }
                        if (arrayList8.isEmpty()) {
                            arrayList2 = arrayList6;
                        } else {
                            arrayList2 = arrayList6;
                            getMessagesStorage().getUsersInternal(arrayList8, arrayList2);
                        }
                        if (arrayList9.isEmpty()) {
                            arrayList3 = arrayList7;
                        } else {
                            arrayList3 = arrayList7;
                            getMessagesStorage().getChatsInternal(TextUtils.join(",", arrayList9), arrayList3);
                        }
                        if (z) {
                            return broadcastPinnedMessage(arrayList5, arrayList2, arrayList3, true, true);
                        }
                        broadcastPinnedMessage(arrayList5, arrayList2, arrayList3, true, false);
                        return null;
                    } catch (Exception e) {
                        e = e;
                        FileLog.e(e);
                        return null;
                    }
                }
                final TLRPC.TL_channels_getMessages tL_channels_getMessages = new TLRPC.TL_channels_getMessages();
                tL_channels_getMessages.channel = getMessagesController().getInputChannel(j2);
                tL_channels_getMessages.id = arrayList4;
                getConnectionsManager().sendRequest(tL_channels_getMessages, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda121
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        MediaDataController.this.lambda$loadPinnedMessageInternal$164(j2, j, tL_channels_getMessages, tLObject, tL_error);
                    }
                });
            }
            if (!arrayList5.isEmpty()) {
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public static ArrayList<TLRPC.Reaction> loadReactionsFromPref(SharedPreferences sharedPreferences) {
        int i = sharedPreferences.getInt(NotificationBadge.NewHtcHomeBadger.COUNT, 0);
        ArrayList<TLRPC.Reaction> arrayList = new ArrayList<>(i);
        if (i > 0) {
            for (int i2 = 0; i2 < i; i2++) {
                SerializedData serializedData = new SerializedData(Utilities.hexToBytes(sharedPreferences.getString("object_" + i2, "")));
                try {
                    arrayList.add(TLRPC.Reaction.TLdeserialize(serializedData, serializedData.readInt32(true), true));
                } catch (Throwable th) {
                    FileLog.e(th);
                }
            }
        }
        return arrayList;
    }

    private void loadRepliesOfDraftReplies(final ArrayList<TLRPC.Message> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda156
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadRepliesOfDraftReplies$0(arrayList);
            }
        });
    }

    private void loadStickerSetThumbInternal(TLRPC.PhotoSize photoSize, Object obj, TLRPC.Document document, int i) {
        ImageLocation forSticker = ImageLocation.getForSticker(photoSize, document, i);
        if (forSticker != null) {
            getFileLoader().loadFile(forSticker, obj, forSticker.imageType == 1 ? "tgs" : "webp", 3, 1);
        }
    }

    private CharSequence parsePattern(CharSequence charSequence, Pattern pattern, ArrayList<TLRPC.MessageEntity> arrayList, GenericProvider<Void, TLRPC.MessageEntity> genericProvider) {
        URLSpan[] uRLSpanArr;
        Matcher matcher = pattern.matcher(charSequence);
        int i = 0;
        while (matcher.find()) {
            boolean z = true;
            String group = matcher.group(1);
            if ((charSequence instanceof Spannable) && (uRLSpanArr = (URLSpan[]) ((Spannable) charSequence).getSpans(matcher.start() - i, matcher.end() - i, URLSpan.class)) != null && uRLSpanArr.length > 0) {
                z = false;
            }
            if (z) {
                int i2 = 0;
                while (true) {
                    if (i2 >= arrayList.size()) {
                        break;
                    }
                    TLRPC.MessageEntity messageEntity = arrayList.get(i2);
                    if ((messageEntity instanceof TLRPC.TL_messageEntityPre) || (messageEntity instanceof TLRPC.TL_messageEntityCode)) {
                        int start = matcher.start() - i;
                        int end = matcher.end() - i;
                        int i3 = messageEntity.offset;
                        if (AndroidUtilities.intersect1d(start, end, i3, messageEntity.length + i3)) {
                            z = false;
                            break;
                        }
                    }
                    i2++;
                }
            }
            if (z) {
                charSequence = ((Object) charSequence.subSequence(0, matcher.start() - i)) + group + ((Object) charSequence.subSequence(matcher.end() - i, charSequence.length()));
                TLRPC.MessageEntity provide = genericProvider.provide(null);
                provide.offset = matcher.start() - i;
                int length = group.length();
                provide.length = length;
                int i4 = provide.offset;
                removeOffset4After(i4, length + i4, arrayList);
                arrayList.add(provide);
            }
            i += (matcher.end() - matcher.start()) - group.length();
        }
        return charSequence;
    }

    private void preloadNextGreetingsSticker() {
        if (this.recentStickers[3].isEmpty()) {
            return;
        }
        ArrayList<TLRPC.Document> arrayList = this.recentStickers[3];
        this.greetingsSticker = arrayList.get(Utilities.random.nextInt(arrayList.size()));
        getFileLoader().loadFile(ImageLocation.getForDocument(this.greetingsSticker), this.greetingsSticker, null, 0, 1);
    }

    private void processLoadStickersResponse(int i, TLRPC.TL_messages_allStickers tL_messages_allStickers) {
        processLoadStickersResponse(i, tL_messages_allStickers, null);
    }

    private void processLoadStickersResponse(final int i, final TLRPC.TL_messages_allStickers tL_messages_allStickers, Runnable runnable) {
        final ArrayList<TLRPC.TL_messages_stickerSet> arrayList = new ArrayList<>();
        long j = 1000;
        if (tL_messages_allStickers.sets.isEmpty()) {
            processLoadedStickers(i, arrayList, false, (int) (System.currentTimeMillis() / 1000), tL_messages_allStickers.hash2, runnable);
            return;
        }
        final LongSparseArray longSparseArray = new LongSparseArray();
        int i2 = 0;
        while (i2 < tL_messages_allStickers.sets.size()) {
            final TLRPC.StickerSet stickerSet = tL_messages_allStickers.sets.get(i2);
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) this.stickerSetsById.get(stickerSet.id);
            if (tL_messages_stickerSet != null) {
                TLRPC.StickerSet stickerSet2 = tL_messages_stickerSet.set;
                if (stickerSet2.hash == stickerSet.hash) {
                    stickerSet2.archived = stickerSet.archived;
                    stickerSet2.installed = stickerSet.installed;
                    stickerSet2.official = stickerSet.official;
                    longSparseArray.put(stickerSet2.id, tL_messages_stickerSet);
                    arrayList.add(tL_messages_stickerSet);
                    if (longSparseArray.size() == tL_messages_allStickers.sets.size()) {
                        processLoadedStickers(i, arrayList, false, (int) (System.currentTimeMillis() / j), tL_messages_allStickers.hash2);
                    }
                    i2++;
                    j = 1000;
                }
            }
            arrayList.add(null);
            TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
            TLRPC.TL_inputStickerSetID tL_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
            tL_messages_getStickerSet.stickerset = tL_inputStickerSetID;
            tL_inputStickerSetID.id = stickerSet.id;
            tL_inputStickerSetID.access_hash = stickerSet.access_hash;
            final int i3 = i2;
            getConnectionsManager().sendRequest(tL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda221
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$processLoadStickersResponse$74(arrayList, i3, longSparseArray, stickerSet, tL_messages_allStickers, i, tLObject, tL_error);
                }
            });
            i2++;
            j = 1000;
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    private void processLoadedDiceStickers(final String str, final boolean z, final TLRPC.TL_messages_stickerSet tL_messages_stickerSet, final boolean z2, final int i) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$processLoadedDiceStickers$84(str);
            }
        });
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$processLoadedDiceStickers$87(z2, tL_messages_stickerSet, i, str, z);
            }
        });
    }

    private void processLoadedFeaturedStickers(final boolean z, final ArrayList<TLRPC.StickerSetCovered> arrayList, final ArrayList<Long> arrayList2, final boolean z2, final boolean z3, final int i, final long j) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$processLoadedFeaturedStickers$59(z);
            }
        });
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$processLoadedFeaturedStickers$63(z3, arrayList, i, j, z, arrayList2, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processLoadedMedia(final TLRPC.messages_Messages messages_messages, final long j, int i, int i2, final int i3, final int i4, long j2, final int i5, final int i6, boolean z, final boolean z2, final int i7) {
        ArrayList<TLRPC.Message> arrayList;
        ArrayList<TLRPC.Message> arrayList2;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("process load media messagesCount " + ((messages_messages == null || (arrayList2 = messages_messages.messages) == null) ? 0 : arrayList2.size()) + " did " + j + " topicId " + j2 + " count = " + i + " max_id=" + i2 + " min_id=" + i3 + " type = " + i4 + " cache = " + i5 + " classGuid = " + i6 + " topReached=" + z2);
        }
        if (i5 != 0 && messages_messages != null && (arrayList = messages_messages.messages) != null && (((arrayList.isEmpty() && i3 == 0) || (messages_messages.messages.size() <= 1 && i3 != 0)) && !DialogObject.isEncryptedDialog(j))) {
            if (i5 == 2) {
                return;
            }
            loadMedia(j, i, i2, i3, i4, j2, 0, i6, i7, null, null);
        } else {
            if (i5 == 0) {
                ImageLoader.saveMessagesThumbs(messages_messages.messages);
                getMessagesStorage().putUsersAndChats(messages_messages.users, messages_messages.chats, true, true);
                putMediaDatabase(j, j2, i4, messages_messages.messages, i2, i3, z2);
            }
            Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda87
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedMedia$134(messages_messages, i5, j, i6, i4, z2, i3, i7);
                }
            });
        }
    }

    private void processLoadedMediaCount(final int i, final long j, final long j2, final int i2, final int i3, final boolean z, final int i4) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda49
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$processLoadedMediaCount$135(j, z, i, i2, i4, j2, i3);
            }
        });
    }

    private void processLoadedStickers(int i, ArrayList<TLRPC.TL_messages_stickerSet> arrayList, boolean z, int i2, long j) {
        processLoadedStickers(i, arrayList, z, i2, j, null);
    }

    private void processLoadedStickers(final int i, final ArrayList<TLRPC.TL_messages_stickerSet> arrayList, final boolean z, final int i2, final long j, final Runnable runnable) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda201
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$processLoadedStickers$101(i);
            }
        });
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda202
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$processLoadedStickers$105(z, arrayList, i2, j, i, runnable);
            }
        });
    }

    private void putDiceStickersToCache(final String str, final TLRPC.TL_messages_stickerSet tL_messages_stickerSet, final int i) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda104
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$putDiceStickersToCache$88(tL_messages_stickerSet, str, i);
            }
        });
    }

    private void putEmojiKeywords(final String str, final TLRPC.TL_emojiKeywordsDifference tL_emojiKeywordsDifference) {
        if (tL_emojiKeywordsDifference == null) {
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda194
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$putEmojiKeywords$215(tL_emojiKeywordsDifference, str);
            }
        });
    }

    private void putFeaturedStickersToCache(final boolean z, ArrayList<TLRPC.StickerSetCovered> arrayList, final ArrayList<Long> arrayList2, final int i, final long j, final boolean z2) {
        final ArrayList arrayList3 = arrayList != null ? new ArrayList(arrayList) : null;
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda96
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$putFeaturedStickersToCache$64(arrayList3, arrayList2, z, i, j, z2);
            }
        });
    }

    private void putMediaCountDatabase(final long j, final long j2, final int i, final int i2) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda207
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$putMediaCountDatabase$136(j2, j, i, i2);
            }
        });
    }

    private void putMediaDatabase(final long j, final long j2, final int i, final ArrayList<TLRPC.Message> arrayList, final int i2, final int i3, final boolean z) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda239
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$putMediaDatabase$138(i3, arrayList, z, j, i2, i, j2);
            }
        });
    }

    private void putMenuBotsToCache(final TLRPC.TL_attachMenuBots tL_attachMenuBots, final long j, final int i) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda137
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$putMenuBotsToCache$6(tL_attachMenuBots, j, i);
            }
        });
    }

    private void putPremiumPromoToCache(final TLRPC.TL_help_premiumPromo tL_help_premiumPromo, final int i) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda132
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$putPremiumPromoToCache$10(tL_help_premiumPromo, i);
            }
        });
    }

    private void putReactionsToCache(List<TLRPC.TL_availableReaction> list, final int i, final int i2) {
        final ArrayList arrayList = list != null ? new ArrayList(list) : null;
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda230
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$putReactionsToCache$16(arrayList, i, i2);
            }
        });
    }

    private void putSetToCache(final TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda225
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$putSetToCache$47(tL_messages_stickerSet);
            }
        });
    }

    private void putStickersToCache(final int i, ArrayList<TLRPC.TL_messages_stickerSet> arrayList, final int i2, final long j) {
        final ArrayList arrayList2 = arrayList != null ? new ArrayList(arrayList) : null;
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda130
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$putStickersToCache$100(arrayList2, i, i2, j);
            }
        });
    }

    private static void removeEmptyMessages(ArrayList<TLRPC.Message> arrayList) {
        int i = 0;
        while (i < arrayList.size()) {
            TLRPC.Message message = arrayList.get(i);
            if (message == null || (message instanceof TLRPC.TL_messageEmpty) || (message.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                arrayList.remove(i);
                i--;
            }
            i++;
        }
    }

    private static void removeOffset4After(int i, int i2, ArrayList<TLRPC.MessageEntity> arrayList) {
        int i3;
        int size = arrayList.size();
        for (int i4 = 0; i4 < size; i4++) {
            TLRPC.MessageEntity messageEntity = arrayList.get(i4);
            int i5 = messageEntity.offset;
            if (i5 > i2) {
                i3 = i5 - 4;
            } else if (i5 > i) {
                i3 = i5 - 2;
            }
            messageEntity.offset = i3;
        }
    }

    private static boolean replyToEquals(TLRPC.InputReplyTo inputReplyTo, TLRPC.InputReplyTo inputReplyTo2) {
        if (inputReplyTo == inputReplyTo2) {
            return true;
        }
        boolean z = inputReplyTo instanceof TLRPC.TL_inputReplyToMessage;
        if (z != (inputReplyTo2 instanceof TLRPC.TL_inputReplyToMessage)) {
            return false;
        }
        if (z) {
            return MessageObject.peersEqual(inputReplyTo.reply_to_peer_id, inputReplyTo2.reply_to_peer_id) && TextUtils.equals(inputReplyTo.quote_text, inputReplyTo2.quote_text) && inputReplyTo.reply_to_msg_id == inputReplyTo2.reply_to_msg_id;
        }
        if (inputReplyTo instanceof TLRPC.TL_inputReplyToStory) {
            return MessageObject.peersEqual(inputReplyTo.peer, inputReplyTo2.peer) && inputReplyTo.story_id == inputReplyTo2.story_id;
        }
        return true;
    }

    private void saveDraftReplyMessage(final long j, final long j2, final TLRPC.Message message) {
        if (message == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda85
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$saveDraftReplyMessage$192(j, j2, message);
            }
        });
    }

    private void savePeer(final long j, final int i, final double d) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda119
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$savePeer$158(j, i, d);
            }
        });
    }

    private void savePinnedMessages(final long j, final ArrayList<TLRPC.Message> arrayList) {
        if (arrayList.isEmpty()) {
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda103
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$savePinnedMessages$166(arrayList, j);
            }
        });
    }

    public static void saveReactionsToPref(SharedPreferences sharedPreferences, long j, ArrayList<? extends TLObject> arrayList) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(NotificationBadge.NewHtcHomeBadger.COUNT, arrayList.size());
        edit.putLong("hash", j);
        for (int i = 0; i < arrayList.size(); i++) {
            TLObject tLObject = arrayList.get(i);
            SerializedData serializedData = new SerializedData(tLObject.getObjectSize());
            tLObject.serializeToStream(serializedData);
            edit.putString("object_" + i, Utilities.bytesToHex(serializedData.toByteArray()));
        }
        edit.apply();
    }

    private void saveReplyMessages(final LongSparseArray longSparseArray, final ArrayList<TLRPC.Message> arrayList, final boolean z) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda224
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$saveReplyMessages$178(z, arrayList, longSparseArray);
            }
        });
    }

    private void saveStickerSetIntoCache(final TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        if (tL_messages_stickerSet == null || tL_messages_stickerSet.set == null) {
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda124
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$saveStickerSetIntoCache$40(tL_messages_stickerSet);
            }
        });
        cleanupStickerSetCache();
    }

    private TLRPC.MessageEntity setEntityStartEnd(TLRPC.MessageEntity messageEntity, int i, int i2) {
        messageEntity.offset = i;
        messageEntity.length = i2 - i;
        return messageEntity;
    }

    public static void sortEntities(ArrayList<TLRPC.MessageEntity> arrayList) {
        Collections.sort(arrayList, entityComparator);
    }

    public static boolean stringsEqual(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == null && charSequence2 == null) {
            return true;
        }
        if (charSequence == null || charSequence2 == null || !TextUtils.equals(charSequence, charSequence2)) {
            return false;
        }
        return entitiesEqual(getInstance(UserConfig.selectedAccount).getEntities(new CharSequence[]{new SpannableStringBuilder(charSequence)}, true), getInstance(UserConfig.selectedAccount).getEntities(new CharSequence[]{new SpannableStringBuilder(charSequence2)}, true));
    }

    private static TLRPC.InputReplyTo toInputReplyTo(int i, TLRPC.MessageReplyHeader messageReplyHeader) {
        if (messageReplyHeader instanceof TLRPC.TL_messageReplyStoryHeader) {
            TLRPC.TL_inputReplyToStory tL_inputReplyToStory = new TLRPC.TL_inputReplyToStory();
            tL_inputReplyToStory.peer = MessagesController.getInstance(i).getInputPeer(messageReplyHeader.peer);
            tL_inputReplyToStory.story_id = messageReplyHeader.story_id;
            return tL_inputReplyToStory;
        }
        if (!(messageReplyHeader instanceof TLRPC.TL_messageReplyHeader)) {
            return null;
        }
        TLRPC.TL_inputReplyToMessage tL_inputReplyToMessage = new TLRPC.TL_inputReplyToMessage();
        tL_inputReplyToMessage.reply_to_msg_id = messageReplyHeader.reply_to_msg_id;
        if ((messageReplyHeader.flags & 1) != 0) {
            TLRPC.InputPeer inputPeer = MessagesController.getInstance(i).getInputPeer(messageReplyHeader.reply_to_peer_id);
            tL_inputReplyToMessage.reply_to_peer_id = inputPeer;
            if (inputPeer != null) {
                tL_inputReplyToMessage.flags |= 2;
            }
        }
        int i2 = messageReplyHeader.flags;
        if ((i2 & 2) != 0) {
            tL_inputReplyToMessage.flags |= 1;
            tL_inputReplyToMessage.top_msg_id = messageReplyHeader.reply_to_top_id;
        }
        if ((i2 & 64) != 0) {
            tL_inputReplyToMessage.flags |= 4;
            tL_inputReplyToMessage.quote_text = messageReplyHeader.quote_text;
        }
        if ((i2 & 128) != 0) {
            tL_inputReplyToMessage.flags |= 8;
            tL_inputReplyToMessage.quote_entities = messageReplyHeader.quote_entities;
        }
        return tL_inputReplyToMessage;
    }

    private void toggleStickerSetInternal(final Context context, int i, final BaseFragment baseFragment, final boolean z, final TLObject tLObject, final TLRPC.StickerSet stickerSet, final int i2, final boolean z2) {
        TLRPC.TL_inputStickerSetID tL_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
        tL_inputStickerSetID.access_hash = stickerSet.access_hash;
        long j = stickerSet.id;
        tL_inputStickerSetID.id = j;
        if (i == 0) {
            markSetUninstalling(j, true);
            TLRPC.TL_messages_uninstallStickerSet tL_messages_uninstallStickerSet = new TLRPC.TL_messages_uninstallStickerSet();
            tL_messages_uninstallStickerSet.stickerset = tL_inputStickerSetID;
            getConnectionsManager().sendRequest(tL_messages_uninstallStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda29
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$toggleStickerSetInternal$115(stickerSet, i2, tLObject2, tL_error);
                }
            });
            return;
        }
        TLRPC.TL_messages_installStickerSet tL_messages_installStickerSet = new TLRPC.TL_messages_installStickerSet();
        tL_messages_installStickerSet.stickerset = tL_inputStickerSetID;
        tL_messages_installStickerSet.archived = i == 1;
        markSetInstalling(stickerSet.id, true);
        getConnectionsManager().sendRequest(tL_messages_installStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda28
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$toggleStickerSetInternal$112(stickerSet, baseFragment, z, i2, z2, context, tLObject, tLObject2, tL_error);
            }
        });
    }

    private void updateEmojiStatuses(final int i, final TLRPC.TL_account_emojiStatuses tL_account_emojiStatuses) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda95
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$updateEmojiStatuses$234(i, tL_account_emojiStatuses);
            }
        });
    }

    private void updateSearchResults() {
        MessageObject messageObject;
        ArrayList arrayList = new ArrayList(this.searchResultMessages);
        this.searchResultMessages.clear();
        HashSet hashSet = new HashSet();
        int i = 0;
        while (true) {
            MessageObject messageObject2 = null;
            if (i >= this.searchServerResultMessages.size()) {
                break;
            }
            MessageObject messageObject3 = this.searchServerResultMessages.get(i);
            if ((!messageObject3.hasValidGroupId() || messageObject3.isPrimaryGroupMessage) && !hashSet.contains(Integer.valueOf(messageObject3.getId()))) {
                int i2 = 0;
                while (true) {
                    if (i2 >= arrayList.size()) {
                        break;
                    }
                    if (((MessageObject) arrayList.get(i2)).getId() == messageObject3.getId()) {
                        messageObject2 = (MessageObject) arrayList.get(i2);
                        break;
                    }
                    i2++;
                }
                if (messageObject2 != null) {
                    messageObject3.copyStableParams(messageObject2);
                    messageObject3.mediaExists = messageObject2.mediaExists;
                    messageObject3.attachPathExists = messageObject2.attachPathExists;
                }
                messageObject3.isSavedFiltered = true;
                this.searchResultMessages.add(messageObject3);
                hashSet.add(Integer.valueOf(messageObject3.getId()));
            }
            i++;
        }
        for (int i3 = 0; i3 < this.searchLocalResultMessages.size(); i3++) {
            MessageObject messageObject4 = this.searchLocalResultMessages.get(i3);
            if (!hashSet.contains(Integer.valueOf(messageObject4.getId()))) {
                int i4 = 0;
                while (true) {
                    if (i4 >= arrayList.size()) {
                        messageObject = null;
                        break;
                    } else {
                        if (((MessageObject) arrayList.get(i4)).getId() == messageObject4.getId()) {
                            messageObject = (MessageObject) arrayList.get(i4);
                            break;
                        }
                        i4++;
                    }
                }
                if (messageObject != null) {
                    messageObject4.copyStableParams(messageObject);
                    messageObject4.mediaExists = messageObject.mediaExists;
                    messageObject4.attachPathExists = messageObject.attachPathExists;
                }
                messageObject4.isSavedFiltered = true;
                this.searchResultMessages.add(messageObject4);
                hashSet.add(Integer.valueOf(messageObject4.getId()));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: verifyAnimatedStickerMessageInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$verifyAnimatedStickerMessage$68(TLRPC.Message message, final String str) {
        ArrayList<TLRPC.Message> arrayList = this.verifyingMessages.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.verifyingMessages.put(str, arrayList);
        }
        arrayList.add(message);
        TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
        tL_messages_getStickerSet.stickerset = MessageObject.getInputStickerSet(message);
        getConnectionsManager().sendRequest(tL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda18
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$verifyAnimatedStickerMessageInternal$70(str, tLObject, tL_error);
            }
        });
    }

    public void addNewStickerSet(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        if (this.stickerSetsById.indexOfKey(tL_messages_stickerSet.set.id) >= 0 || this.stickerSetsByName.containsKey(tL_messages_stickerSet.set.short_name)) {
            return;
        }
        TLRPC.StickerSet stickerSet = tL_messages_stickerSet.set;
        int i = stickerSet.masks ? 1 : stickerSet.emojis ? 5 : 0;
        this.stickerSets[i].add(0, tL_messages_stickerSet);
        this.stickerSetsById.put(tL_messages_stickerSet.set.id, tL_messages_stickerSet);
        this.installedStickerSetsById.put(tL_messages_stickerSet.set.id, tL_messages_stickerSet);
        this.stickerSetsByName.put(tL_messages_stickerSet.set.short_name, tL_messages_stickerSet);
        LongSparseArray longSparseArray = new LongSparseArray();
        for (int i2 = 0; i2 < tL_messages_stickerSet.documents.size(); i2++) {
            TLRPC.Document document = tL_messages_stickerSet.documents.get(i2);
            longSparseArray.put(document.id, document);
        }
        for (int i3 = 0; i3 < tL_messages_stickerSet.packs.size(); i3++) {
            TLRPC.TL_stickerPack tL_stickerPack = tL_messages_stickerSet.packs.get(i3);
            String replace = tL_stickerPack.emoticon.replace("️", "");
            tL_stickerPack.emoticon = replace;
            ArrayList<TLRPC.Document> arrayList = this.allStickers.get(replace);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                this.allStickers.put(tL_stickerPack.emoticon, arrayList);
            }
            for (int i4 = 0; i4 < tL_stickerPack.documents.size(); i4++) {
                Long l = tL_stickerPack.documents.get(i4);
                if (this.stickersByEmoji.indexOfKey(l.longValue()) < 0) {
                    this.stickersByEmoji.put(l.longValue(), tL_stickerPack.emoticon);
                }
                TLRPC.Document document2 = (TLRPC.Document) longSparseArray.get(l.longValue());
                if (document2 != null) {
                    arrayList.add(document2);
                }
            }
        }
        this.loadHash[i] = calcStickersHash(this.stickerSets[i]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(i), Boolean.TRUE);
        loadStickers(i, false, true);
    }

    public void addRecentGif(final TLRPC.Document document, int i, boolean z) {
        if (document == null) {
            return;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= this.recentGifs.size()) {
                this.recentGifs.add(0, document);
                break;
            }
            TLRPC.Document document2 = this.recentGifs.get(i2);
            if (document2.id == document.id) {
                this.recentGifs.remove(i2);
                this.recentGifs.add(0, document2);
                break;
            }
            i2++;
        }
        if ((this.recentGifs.size() > getMessagesController().savedGifsLimitDefault && !UserConfig.getInstance(this.currentAccount).isPremium()) || this.recentGifs.size() > getMessagesController().savedGifsLimitPremium) {
            final TLRPC.Document remove = this.recentGifs.remove(r0.size() - 1);
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda52
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$addRecentGif$26(remove);
                }
            });
            if (z) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda53
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.lambda$addRecentGif$27(TLRPC.Document.this);
                    }
                });
            }
        }
        ArrayList<TLRPC.Document> arrayList = new ArrayList<>();
        arrayList.add(document);
        processLoadedRecentDocuments(0, arrayList, true, i, false);
    }

    public void addRecentSticker(final int i, final Object obj, TLRPC.Document document, int i2, boolean z) {
        int i3;
        final TLRPC.Document remove;
        if (i != 3) {
            if (MessageObject.isStickerDocument(document) || MessageObject.isAnimatedStickerDocument(document, true)) {
                int i4 = 0;
                while (true) {
                    if (i4 < this.recentStickers[i].size()) {
                        TLRPC.Document document2 = this.recentStickers[i].get(i4);
                        if (document2.id == document.id) {
                            this.recentStickers[i].remove(i4);
                            if (!z) {
                                this.recentStickers[i].add(0, document2);
                            }
                        } else {
                            i4++;
                        }
                    } else if (!z) {
                        this.recentStickers[i].add(0, document);
                    }
                }
                if (i == 2) {
                    if (z) {
                        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 0, document, 4);
                    } else {
                        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 0, document, Integer.valueOf(this.recentStickers[i].size() > getMessagesController().maxFaveStickersCount ? 6 : 5));
                    }
                    final TLRPC.TL_messages_faveSticker tL_messages_faveSticker = new TLRPC.TL_messages_faveSticker();
                    TLRPC.TL_inputDocument tL_inputDocument = new TLRPC.TL_inputDocument();
                    tL_messages_faveSticker.id = tL_inputDocument;
                    tL_inputDocument.id = document.id;
                    tL_inputDocument.access_hash = document.access_hash;
                    byte[] bArr = document.file_reference;
                    tL_inputDocument.file_reference = bArr;
                    if (bArr == null) {
                        tL_inputDocument.file_reference = new byte[0];
                    }
                    tL_messages_faveSticker.unfave = z;
                    getConnectionsManager().sendRequest(tL_messages_faveSticker, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda159
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            MediaDataController.this.lambda$addRecentSticker$21(obj, tL_messages_faveSticker, tLObject, tL_error);
                        }
                    });
                    i3 = getMessagesController().maxFaveStickersCount;
                } else {
                    if (i == 0 && z) {
                        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 0, document, 3);
                        final TLRPC.TL_messages_saveRecentSticker tL_messages_saveRecentSticker = new TLRPC.TL_messages_saveRecentSticker();
                        TLRPC.TL_inputDocument tL_inputDocument2 = new TLRPC.TL_inputDocument();
                        tL_messages_saveRecentSticker.id = tL_inputDocument2;
                        tL_inputDocument2.id = document.id;
                        tL_inputDocument2.access_hash = document.access_hash;
                        byte[] bArr2 = document.file_reference;
                        tL_inputDocument2.file_reference = bArr2;
                        if (bArr2 == null) {
                            tL_inputDocument2.file_reference = new byte[0];
                        }
                        tL_messages_saveRecentSticker.unsave = true;
                        getConnectionsManager().sendRequest(tL_messages_saveRecentSticker, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda160
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                MediaDataController.this.lambda$addRecentSticker$22(obj, tL_messages_saveRecentSticker, tLObject, tL_error);
                            }
                        });
                    }
                    i3 = getMessagesController().maxRecentStickersCount;
                }
                if (this.recentStickers[i].size() > i3 || z) {
                    if (z) {
                        remove = document;
                    } else {
                        ArrayList<TLRPC.Document> arrayList = this.recentStickers[i];
                        remove = arrayList.remove(arrayList.size() - 1);
                    }
                    getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda161
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaDataController.this.lambda$addRecentSticker$23(i, remove);
                        }
                    });
                }
                if (!z) {
                    ArrayList<TLRPC.Document> arrayList2 = new ArrayList<>();
                    arrayList2.add(document);
                    processLoadedRecentDocuments(i, arrayList2, false, i2, false);
                }
                if (i == 2 || (i == 0 && z)) {
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.recentDocumentsDidLoad, Boolean.FALSE, Integer.valueOf(i));
                }
            }
        }
    }

    public void addStyle(int i, int i2, int i3, ArrayList<TLRPC.MessageEntity> arrayList) {
        if ((i & 256) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC.TL_messageEntitySpoiler(), i2, i3));
        }
        if ((i & 1) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC.TL_messageEntityBold(), i2, i3));
        }
        if ((i & 2) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC.TL_messageEntityItalic(), i2, i3));
        }
        if ((i & 4) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC.TL_messageEntityCode(), i2, i3));
        }
        if ((i & 8) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC.TL_messageEntityStrike(), i2, i3));
        }
        if ((i & 16) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC.TL_messageEntityUnderline(), i2, i3));
        }
    }

    public void applyAttachMenuBot(TLRPC.TL_attachMenuBotsBot tL_attachMenuBotsBot) {
        this.attachMenuBots.bots.add(tL_attachMenuBotsBot.bot);
        loadAttachMenuBots(false, true);
    }

    public boolean areAllTrendingStickerSetsUnread(boolean z) {
        int size = this.featuredStickerSets[z ? 1 : 0].size();
        for (int i = 0; i < size; i++) {
            TLRPC.StickerSetCovered stickerSetCovered = this.featuredStickerSets[z ? 1 : 0].get(i);
            if (!isStickerPackInstalled(stickerSetCovered.set.id) && ((!stickerSetCovered.covers.isEmpty() || stickerSetCovered.cover != null) && !this.unreadStickerSets[z ? 1 : 0].contains(Long.valueOf(stickerSetCovered.set.id)))) {
                return false;
            }
        }
        return true;
    }

    public boolean areStickersLoaded(int i) {
        return this.stickersLoaded[i];
    }

    public void beginTransaction() {
        this.inTransaction = true;
    }

    public boolean botInAttachMenu(long j) {
        for (int i = 0; i < this.attachMenuBots.bots.size(); i++) {
            if (this.attachMenuBots.bots.get(i).bot_id == j) {
                return true;
            }
        }
        return false;
    }

    public void buildShortcuts() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        int maxShortcutCountPerActivity = ShortcutManagerCompat.getMaxShortcutCountPerActivity(ApplicationLoader.applicationContext) - 2;
        if (maxShortcutCountPerActivity <= 0) {
            maxShortcutCountPerActivity = 5;
        }
        final ArrayList arrayList = new ArrayList();
        if (SharedConfig.passcodeHash.length() <= 0) {
            for (int i = 0; i < this.hints.size(); i++) {
                arrayList.add(this.hints.get(i));
                if (arrayList.size() == maxShortcutCountPerActivity - 2) {
                    break;
                }
            }
        }
        final boolean z = Build.VERSION.SDK_INT >= 30;
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda98
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$buildShortcuts$141(z, arrayList);
            }
        });
    }

    public void calcNewHash(int i) {
        this.loadHash[i] = calcStickersHash(this.stickerSets[i]);
    }

    public boolean canAddStickerToFavorites() {
        return (this.stickersLoaded[0] && this.stickerSets[0].size() < 5 && this.recentStickers[2].isEmpty()) ? false : true;
    }

    public boolean canCreateAttachedMenuBotShortcut(long j) {
        return true;
    }

    public boolean cancelRemovingStickerSet(long j) {
        Runnable runnable = (Runnable) this.removingStickerSetsUndos.get(j);
        if (runnable == null) {
            return false;
        }
        runnable.run();
        return true;
    }

    public void checkAllMedia(boolean z) {
        if (z) {
            this.reactionsUpdateDate = 0;
            int[] iArr = this.loadFeaturedDate;
            iArr[0] = 0;
            iArr[1] = 0;
        }
        loadRecents(2, false, true, false);
        loadRecents(3, false, true, false);
        loadRecents(7, false, false, true);
        checkFeaturedStickers();
        checkFeaturedEmoji();
        checkReactions();
        checkMenuBots(true);
        checkPremiumPromo();
        checkPremiumGiftStickers();
        checkGenericAnimations();
        getMessagesController().getAvailableEffects();
    }

    public void checkDefaultTopicIcons() {
        if (getUserConfig().defaultTopicIcons != null) {
            String str = getUserConfig().defaultTopicIcons;
            TLRPC.TL_messages_stickerSet stickerSetByName = getStickerSetByName(str);
            if (stickerSetByName == null) {
                stickerSetByName = getStickerSetByEmojiOrName(str);
            }
            if (stickerSetByName == null) {
                getInstance(this.currentAccount).loadStickersByEmojiOrName(str, false, true);
            }
        }
        if (this.loadingDefaultTopicIcons || System.currentTimeMillis() - getUserConfig().lastUpdatedDefaultTopicIcons < 86400000) {
            return;
        }
        this.loadingDefaultTopicIcons = true;
        TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
        tL_messages_getStickerSet.stickerset = new TLRPC.TL_inputStickerSetEmojiDefaultTopicIcons();
        getConnectionsManager().sendRequest(tL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda80
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$checkDefaultTopicIcons$80(tLObject, tL_error);
            }
        });
    }

    public void checkFeaturedEmoji() {
        if (this.loadingFeaturedStickers[1]) {
            return;
        }
        if (!this.featuredStickersLoaded[1] || Math.abs((System.currentTimeMillis() / 1000) - this.loadFeaturedDate[1]) >= 3600) {
            loadFeaturedStickers(true, true);
        }
    }

    public void checkFeaturedStickers() {
        if (this.loadingFeaturedStickers[0]) {
            return;
        }
        if (!this.featuredStickersLoaded[0] || Math.abs((System.currentTimeMillis() / 1000) - this.loadFeaturedDate[0]) >= 3600) {
            loadFeaturedStickers(false, true);
        }
    }

    public void checkGenericAnimations() {
        if (getUserConfig().genericAnimationsStickerPack != null) {
            String str = getUserConfig().genericAnimationsStickerPack;
            TLRPC.TL_messages_stickerSet stickerSetByName = getStickerSetByName(str);
            if (stickerSetByName == null) {
                stickerSetByName = getStickerSetByEmojiOrName(str);
            }
            if (stickerSetByName == null) {
                getInstance(this.currentAccount).loadStickersByEmojiOrName(str, false, true);
            }
        }
        if (this.loadingGenericAnimations || System.currentTimeMillis() - getUserConfig().lastUpdatedGenericAnimations < 86400000) {
            return;
        }
        this.loadingGenericAnimations = true;
        TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
        tL_messages_getStickerSet.stickerset = new TLRPC.TL_inputStickerSetEmojiGenericAnimations();
        getConnectionsManager().sendRequest(tL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda144
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$checkGenericAnimations$78(tLObject, tL_error);
            }
        });
    }

    public void checkMenuBots(boolean z) {
        if (this.isLoadingMenuBots) {
            return;
        }
        if ((!z || this.menuBotsUpdatedLocal) && Math.abs((System.currentTimeMillis() / 1000) - this.menuBotsUpdateDate) < 3600) {
            return;
        }
        loadAttachMenuBots(true, false);
    }

    public void checkPremiumGiftStickers() {
        if (getUserConfig().premiumGiftsStickerPack != null) {
            String str = getUserConfig().premiumGiftsStickerPack;
            TLRPC.TL_messages_stickerSet stickerSetByName = getStickerSetByName(str);
            if (stickerSetByName == null) {
                stickerSetByName = getStickerSetByEmojiOrName(str);
            }
            if (stickerSetByName == null) {
                getInstance(this.currentAccount).loadStickersByEmojiOrName(str, false, true);
            }
        }
        if (this.loadingPremiumGiftStickers || System.currentTimeMillis() - getUserConfig().lastUpdatedPremiumGiftsStickerPack < 86400000) {
            return;
        }
        this.loadingPremiumGiftStickers = true;
        TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
        tL_messages_getStickerSet.stickerset = new TLRPC.TL_inputStickerSetPremiumGifts();
        getConnectionsManager().sendRequest(tL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda50
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$checkPremiumGiftStickers$76(tLObject, tL_error);
            }
        });
    }

    public void checkPremiumPromo() {
        if (this.isLoadingPremiumPromo) {
            return;
        }
        if (this.premiumPromo == null || Math.abs((System.currentTimeMillis() / 1000) - this.premiumPromoUpdateDate) >= 3600) {
            loadPremiumPromo(true);
        }
    }

    public void checkReactions() {
        if (this.isLoadingReactions || Math.abs((System.currentTimeMillis() / 1000) - this.reactionsUpdateDate) < 3600) {
            return;
        }
        loadReactions(true, null);
    }

    public void checkRingtones(boolean z) {
        this.ringtoneDataStore.loadUserRingtones(z);
    }

    public void checkStickers(int i) {
        if (this.loadingStickers[i]) {
            return;
        }
        if (!this.stickersLoaded[i] || Math.abs((System.currentTimeMillis() / 1000) - this.loadDate[i]) >= 3600) {
            loadStickers(i, true, false);
        }
    }

    public void cleanDraft(long j, long j2, boolean z) {
        LongSparseArray longSparseArray = (LongSparseArray) this.drafts.get(j);
        TLRPC.DraftMessage draftMessage = longSparseArray != null ? (TLRPC.DraftMessage) longSparseArray.get(j2) : null;
        if (draftMessage == null) {
            return;
        }
        if (z) {
            TLRPC.InputReplyTo inputReplyTo = draftMessage.reply_to;
            if (inputReplyTo == null || inputReplyTo.reply_to_msg_id != 0) {
                if (inputReplyTo != null) {
                    inputReplyTo.reply_to_msg_id = 0;
                }
                draftMessage.flags &= -2;
                saveDraft(j, j2, draftMessage.message, draftMessage.entities, null, null, 0L, draftMessage.no_webpage, true);
                return;
            }
            return;
        }
        LongSparseArray longSparseArray2 = (LongSparseArray) this.drafts.get(j);
        if (longSparseArray2 != null) {
            longSparseArray2.remove(j2);
            if (longSparseArray2.size() == 0) {
                this.drafts.remove(j);
            }
        }
        LongSparseArray longSparseArray3 = (LongSparseArray) this.draftMessages.get(j);
        if (longSparseArray3 != null) {
            longSparseArray3.remove(j2);
            if (longSparseArray3.size() == 0) {
                this.draftMessages.remove(j);
            }
        }
        if (j2 == 0) {
            this.draftPreferences.edit().remove("" + j).remove("r_" + j).commit();
            getMessagesController().sortDialogs(null);
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
            return;
        }
        this.draftPreferences.edit().remove("t_" + j + "_" + j2).remove("rt_" + j + "_" + j2).commit();
    }

    public void cleanup() {
        int i = 0;
        while (true) {
            ArrayList<TLRPC.Document>[] arrayListArr = this.recentStickers;
            if (i >= arrayListArr.length) {
                break;
            }
            ArrayList<TLRPC.Document> arrayList = arrayListArr[i];
            if (arrayList != null) {
                arrayList.clear();
            }
            this.loadingRecentStickers[i] = false;
            this.recentStickersLoaded[i] = false;
            i++;
        }
        for (int i2 = 0; i2 < 4; i2++) {
            this.loadHash[i2] = 0;
            this.loadDate[i2] = 0;
            this.stickerSets[i2].clear();
            this.loadingStickers[i2] = false;
            this.stickersLoaded[i2] = false;
        }
        this.loadingPinnedMessages.clear();
        int[] iArr = this.loadFeaturedDate;
        iArr[0] = 0;
        long[] jArr = this.loadFeaturedHash;
        jArr[0] = 0;
        iArr[1] = 0;
        jArr[1] = 0;
        this.allStickers.clear();
        this.allStickersFeatured.clear();
        this.stickersByEmoji.clear();
        this.featuredStickerSetsById[0].clear();
        this.featuredStickerSets[0].clear();
        this.featuredStickerSetsById[1].clear();
        this.featuredStickerSets[1].clear();
        this.unreadStickerSets[0].clear();
        this.unreadStickerSets[1].clear();
        this.recentGifs.clear();
        this.stickerSetsById.clear();
        this.installedStickerSetsById.clear();
        this.stickerSetsByName.clear();
        this.diceStickerSetsByEmoji.clear();
        this.diceEmojiStickerSetsById.clear();
        this.loadingDiceStickerSets.clear();
        boolean[] zArr = this.loadingFeaturedStickers;
        zArr[0] = false;
        boolean[] zArr2 = this.featuredStickersLoaded;
        zArr2[0] = false;
        zArr[1] = false;
        zArr2[1] = false;
        this.loadingRecentGifs = false;
        this.recentGifsLoaded = false;
        this.currentFetchingEmoji.clear();
        if (Build.VERSION.SDK_INT >= 25) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda245
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.lambda$cleanup$1();
                }
            });
        }
        this.verifyingMessages.clear();
        this.loading = false;
        this.loaded = false;
        this.hints.clear();
        this.inlineBots.clear();
        this.webapps.clear();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda246
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$cleanup$2();
            }
        });
        this.drafts.clear();
        this.draftMessages.clear();
        this.draftPreferences.edit().clear().apply();
        this.botInfos.clear();
        this.botKeyboards.clear();
        this.botKeyboardsByMids.clear();
    }

    public void clearAllDrafts(boolean z) {
        this.drafts.clear();
        this.draftMessages.clear();
        this.draftsFolderIds.clear();
        this.draftPreferences.edit().clear().commit();
        if (z) {
            getMessagesController().sortDialogs(null);
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
        }
    }

    public void clearBotKeyboard(final long j) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda73
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$clearBotKeyboard$194(j);
            }
        });
    }

    public void clearBotKeyboard(final MessagesStorage.TopicKey topicKey, final ArrayList<Integer> arrayList) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda247
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$clearBotKeyboard$193(arrayList, topicKey);
            }
        });
    }

    public void clearDraftsFolderIds() {
        this.draftsFolderIds.clear();
    }

    public void clearFoundMessageObjects() {
        this.searchResultMessages.clear();
        this.searchServerResultMessages.clear();
        this.searchLocalResultMessages.clear();
    }

    public ArrayList<TLRPC.EmojiStatus> clearRecentEmojiStatuses() {
        ArrayList<TLRPC.EmojiStatus> arrayList = this.emojiStatuses[0];
        if (arrayList != null) {
            arrayList.clear();
        }
        this.emojiStatusesHash[0] = 0;
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda172
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$clearRecentEmojiStatuses$229();
            }
        });
        return this.emojiStatuses[0];
    }

    public void clearRecentReactions() {
        this.recentReactions.clear();
        ApplicationLoader.applicationContext.getSharedPreferences("recent_reactions_" + this.currentAccount, 0).edit().clear().apply();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_messages_clearRecentReactions(), new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController.4
            @Override // org.telegram.tgnet.RequestDelegate
            public void run(TLObject tLObject, TLRPC.TL_error tL_error) {
            }
        });
    }

    public void clearRecentStickers() {
        getConnectionsManager().sendRequest(new TLRPC.TL_messages_clearRecentStickers(), new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda191
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$clearRecentStickers$19(tLObject, tL_error);
            }
        });
    }

    public void clearTopPeers() {
        this.hints.clear();
        this.inlineBots.clear();
        this.webapps.clear();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadHints, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadInlineHints, new Object[0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadWebappsHints, new Object[0]);
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda81
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$clearTopPeers$149();
            }
        });
        buildShortcuts();
    }

    public void endTransaction() {
        this.inTransaction = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void fetchEmojiStatuses(final int i, boolean z) {
        TLRPC.TL_account_getChannelDefaultEmojiStatuses tL_account_getChannelDefaultEmojiStatuses;
        boolean[] zArr = this.emojiStatusesFetching;
        if (zArr[i]) {
            return;
        }
        zArr[i] = true;
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda31
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$fetchEmojiStatuses$231(i);
                }
            });
            return;
        }
        if (i == 0) {
            TLRPC.TL_account_getRecentEmojiStatuses tL_account_getRecentEmojiStatuses = new TLRPC.TL_account_getRecentEmojiStatuses();
            tL_account_getRecentEmojiStatuses.hash = this.emojiStatusesHash[i];
            tL_account_getChannelDefaultEmojiStatuses = tL_account_getRecentEmojiStatuses;
        } else if (i == 1) {
            TLRPC.TL_account_getDefaultEmojiStatuses tL_account_getDefaultEmojiStatuses = new TLRPC.TL_account_getDefaultEmojiStatuses();
            tL_account_getDefaultEmojiStatuses.hash = this.emojiStatusesHash[i];
            tL_account_getChannelDefaultEmojiStatuses = tL_account_getDefaultEmojiStatuses;
        } else {
            TLRPC.TL_account_getChannelDefaultEmojiStatuses tL_account_getChannelDefaultEmojiStatuses2 = new TLRPC.TL_account_getChannelDefaultEmojiStatuses();
            tL_account_getChannelDefaultEmojiStatuses2.hash = this.emojiStatusesHash[i];
            tL_account_getChannelDefaultEmojiStatuses = tL_account_getChannelDefaultEmojiStatuses2;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_getChannelDefaultEmojiStatuses, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda32
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$fetchEmojiStatuses$233(i, tLObject, tL_error);
            }
        });
    }

    public void fetchNewEmojiKeywords(String[] strArr) {
        fetchNewEmojiKeywords(strArr, false);
    }

    public void fetchNewEmojiKeywords(String[] strArr, boolean z) {
        if (strArr == null) {
            return;
        }
        for (final String str : strArr) {
            if (TextUtils.isEmpty(str) || this.currentFetchingEmoji.get(str) != null) {
                return;
            }
            if (z && this.fetchedEmoji.contains(str)) {
                return;
            }
            this.currentFetchingEmoji.put(str, Boolean.TRUE);
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$fetchNewEmojiKeywords$213(str);
                }
            });
        }
    }

    public void fillWithAnimatedEmoji(final ArrayList<KeywordResult> arrayList, final Integer num, final boolean z, final boolean z2, boolean z3, final Runnable runnable) {
        if (arrayList == null || arrayList.isEmpty()) {
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        final ArrayList[] arrayListArr = {getStickerSets(5)};
        final Runnable runnable2 = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda186
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$fillWithAnimatedEmoji$225(num, arrayList, z2, z, arrayListArr, runnable);
            }
        };
        ArrayList arrayList2 = arrayListArr[0];
        if ((arrayList2 != null && !arrayList2.isEmpty()) || this.triedLoadingEmojipacks) {
            runnable2.run();
            return;
        }
        this.triedLoadingEmojipacks = true;
        final boolean[] zArr = new boolean[1];
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda187
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$fillWithAnimatedEmoji$227(zArr, arrayListArr, runnable2);
            }
        });
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda188
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.lambda$fillWithAnimatedEmoji$228(zArr, runnable2);
            }
        }, 900L);
    }

    public TLRPC.TL_attachMenuBot findBotInAttachMenu(long j) {
        for (int i = 0; i < this.attachMenuBots.bots.size(); i++) {
            if (this.attachMenuBots.bots.get(i).bot_id == j) {
                return this.attachMenuBots.bots.get(i);
            }
        }
        return null;
    }

    public void findStickerSetByNameInCache(final String str, final Utilities.Callback<TLRPC.TL_messages_stickerSet> callback) {
        if (callback == null) {
            return;
        }
        if (str == null) {
            callback.run(null);
        } else {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda248
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$findStickerSetByNameInCache$30(str, callback);
                }
            });
        }
    }

    public void generateEmojiPreviewThemes(ArrayList<TLRPC.TL_theme> arrayList, int i) {
        SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("emojithemes_config_" + i, 0).edit();
        edit.putInt(NotificationBadge.NewHtcHomeBadger.COUNT, arrayList.size());
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            TLRPC.TL_theme tL_theme = arrayList.get(i2);
            SerializedData serializedData = new SerializedData(tL_theme.getObjectSize());
            tL_theme.serializeToStream(serializedData);
            edit.putString("theme_" + i2, Utilities.bytesToHex(serializedData.toByteArray()));
        }
        edit.apply();
        if (arrayList.isEmpty()) {
            this.defaultEmojiThemes.clear();
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.emojiPreviewThemesChanged, new Object[0]);
            return;
        }
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new ChatThemeBottomSheet.ChatThemeItem(EmojiThemes.createHomePreviewTheme(i)));
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            EmojiThemes createPreviewFullTheme = EmojiThemes.createPreviewFullTheme(i, arrayList.get(i3));
            ChatThemeBottomSheet.ChatThemeItem chatThemeItem = new ChatThemeBottomSheet.ChatThemeItem(createPreviewFullTheme);
            if (createPreviewFullTheme.items.size() >= 4) {
                arrayList2.add(chatThemeItem);
            }
        }
        ChatThemeController.chatThemeQueue.postRunnable(new 3(arrayList2, i));
    }

    public HashMap<String, ArrayList<TLRPC.Document>> getAllStickers() {
        return this.allStickers;
    }

    public HashMap<String, ArrayList<TLRPC.Document>> getAllStickersFeatured() {
        return this.allStickersFeatured;
    }

    public void getAnimatedEmojiByKeywords(final String str, final Utilities.Callback<ArrayList<Long>> callback) {
        if (str == null) {
            if (callback != null) {
                callback.run(new ArrayList<>());
            }
        } else {
            final ArrayList<TLRPC.TL_messages_stickerSet> stickerSets = getStickerSets(5);
            final ArrayList<TLRPC.StickerSetCovered> featuredEmojiSets = getFeaturedEmojiSets();
            Utilities.searchQueue.postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda150
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.lambda$getAnimatedEmojiByKeywords$216(str, stickerSets, featuredEmojiSets, callback);
                }
            });
        }
    }

    public int getArchivedStickersCount(int i) {
        return this.archivedStickersCount[i];
    }

    public TLRPC.TL_attachMenuBots getAttachMenuBots() {
        return this.attachMenuBots;
    }

    public TL_bots.BotInfo getBotInfoCached(long j, long j2) {
        return this.botInfos.get(j + "_" + j2);
    }

    public ArrayList<TLRPC.EmojiStatus> getDefaultChannelEmojiStatuses() {
        boolean z;
        if (this.emojiStatusesFromCacheFetched[2]) {
            z = this.emojiStatuses[2] != null && (this.emojiStatusesFetchDate[2] == null || (System.currentTimeMillis() / 1000) - this.emojiStatusesFetchDate[2].longValue() <= 1800);
            return this.emojiStatuses[2];
        }
        fetchEmojiStatuses(2, z);
        return this.emojiStatuses[2];
    }

    public ArrayList<TLRPC.EmojiStatus> getDefaultEmojiStatuses() {
        if (!this.emojiStatusesFromCacheFetched[1]) {
            fetchEmojiStatuses(1, true);
        } else if (this.emojiStatuses[1] == null || (this.emojiStatusesFetchDate[1] != null && (System.currentTimeMillis() / 1000) - this.emojiStatusesFetchDate[1].longValue() > 1800)) {
            fetchEmojiStatuses(1, false);
        }
        return this.emojiStatuses[1];
    }

    public String getDoubleTapReaction() {
        String str = this.doubleTapReaction;
        if (str != null) {
            return str;
        }
        if (getReactionsList().isEmpty()) {
            return null;
        }
        String string = MessagesController.getEmojiSettings(this.currentAccount).getString("reaction_on_double_tap", null);
        if (string == null || (getReactionsMap().get(string) == null && !string.startsWith("animated_"))) {
            return getReactionsList().get(0).reaction;
        }
        this.doubleTapReaction = string;
        return string;
    }

    public TLRPC.DraftMessage getDraft(long j, long j2) {
        LongSparseArray longSparseArray = (LongSparseArray) this.drafts.get(j);
        if (longSparseArray == null) {
            return null;
        }
        return (TLRPC.DraftMessage) longSparseArray.get(j2);
    }

    public int getDraftFolderId(long j) {
        return ((Integer) this.draftsFolderIds.get(j, 0)).intValue();
    }

    public TLRPC.Message getDraftMessage(long j, long j2) {
        LongSparseArray longSparseArray = (LongSparseArray) this.draftMessages.get(j);
        if (longSparseArray == null) {
            return null;
        }
        return (TLRPC.Message) longSparseArray.get(j2);
    }

    public DraftVoice getDraftVoice(long j, long j2) {
        loadDraftVoiceMessages();
        return (DraftVoice) this.draftVoices.get(Objects.hash(Long.valueOf(j), Long.valueOf(j2)));
    }

    public LongSparseArray getDrafts() {
        return this.drafts;
    }

    public TLRPC.Document getEmojiAnimatedSticker(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        String replace = charSequence.toString().replace("️", "");
        ArrayList<TLRPC.TL_messages_stickerSet> stickerSets = getStickerSets(4);
        int size = stickerSets.size();
        for (int i = 0; i < size; i++) {
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet = stickerSets.get(i);
            int size2 = tL_messages_stickerSet.packs.size();
            for (int i2 = 0; i2 < size2; i2++) {
                TLRPC.TL_stickerPack tL_stickerPack = tL_messages_stickerSet.packs.get(i2);
                if (!tL_stickerPack.documents.isEmpty() && TextUtils.equals(tL_stickerPack.emoticon, replace)) {
                    return (TLRPC.Document) getStickerByIds(4).get(tL_stickerPack.documents.get(0).longValue());
                }
            }
        }
        return null;
    }

    public String getEmojiForSticker(long j) {
        String str = (String) this.stickersByEmoji.get(j);
        return str != null ? str : "";
    }

    public void getEmojiNames(final String[] strArr, final String str, final Utilities.Callback<ArrayList<String>> callback) {
        if (callback == null || str == null) {
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda176
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$getEmojiNames$218(strArr, str, callback);
            }
        });
    }

    public void getEmojiSuggestions(String[] strArr, String str, boolean z, KeywordResultCallback keywordResultCallback, CountDownLatch countDownLatch, boolean z2) {
        getEmojiSuggestions(strArr, str, z, keywordResultCallback, countDownLatch, z2, false, false, null);
    }

    public void getEmojiSuggestions(String[] strArr, String str, boolean z, KeywordResultCallback keywordResultCallback, CountDownLatch countDownLatch, boolean z2, boolean z3, boolean z4, Integer num) {
        getEmojiSuggestions(strArr, str, z, keywordResultCallback, countDownLatch, z2, z3, z4, false, num, false);
    }

    public void getEmojiSuggestions(final String[] strArr, final String str, final boolean z, final KeywordResultCallback keywordResultCallback, final CountDownLatch countDownLatch, final boolean z2, final boolean z3, final boolean z4, final boolean z5, final Integer num, final boolean z6) {
        if (keywordResultCallback == null) {
            return;
        }
        if (TextUtils.isEmpty(str) || strArr == null) {
            keywordResultCallback.run(new ArrayList<>(), null);
            return;
        }
        final ArrayList arrayList = new ArrayList(Emoji.recentEmoji);
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda219
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$getEmojiSuggestions$224(strArr, keywordResultCallback, z4, str, z, arrayList, z2, num, z3, z5, z6, countDownLatch);
            }
        });
        if (countDownLatch != null) {
            try {
                countDownLatch.await();
            } catch (Throwable unused) {
            }
        }
    }

    public void getEmojiSuggestions(String[] strArr, String str, boolean z, KeywordResultCallback keywordResultCallback, boolean z2) {
        getEmojiSuggestions(strArr, str, z, keywordResultCallback, null, z2, false, false, null);
    }

    public List<TLRPC.TL_availableReaction> getEnabledReactionsList() {
        return this.enabledReactionsList;
    }

    public ArrayList<TLRPC.MessageEntity> getEntities(CharSequence[] charSequenceArr, boolean z) {
        int i;
        int indexOf;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6 = -1;
        int i7 = 2;
        ArrayList<TLRPC.MessageEntity> arrayList = null;
        if (charSequenceArr != null && charSequenceArr[0] != null) {
            boolean z2 = false;
            int i8 = 0;
            loop0: while (true) {
                i = -1;
                while (true) {
                    indexOf = TextUtils.indexOf(charSequenceArr[0], !z2 ? "`" : "```", i8);
                    if (indexOf == i6) {
                        break loop0;
                    }
                    if (i != i6) {
                        if (arrayList == null) {
                            arrayList = new ArrayList<>();
                        }
                        for (int i9 = (z2 ? 3 : 1) + indexOf; i9 < charSequenceArr[0].length() && charSequenceArr[0].charAt(i9) == '`'; i9++) {
                            indexOf++;
                        }
                        i2 = (z2 ? 3 : 1) + indexOf;
                        if (!z2) {
                            i3 = i + 1;
                            if (i3 == indexOf) {
                                break;
                            }
                            CharSequence charSequence = charSequenceArr[0];
                            if (!(charSequence instanceof Spanned) || ((CodeHighlighting.Span[]) ((Spanned) charSequence).getSpans(Utilities.clamp(i, charSequence.length(), 0), Utilities.clamp(i3, charSequenceArr[0].length(), 0), CodeHighlighting.Span.class)).length <= 0) {
                                break;
                            }
                            i8 = i2;
                            i6 = -1;
                            i7 = 2;
                        } else {
                            char charAt = i > 0 ? charSequenceArr[0].charAt(i - 1) : (char) 0;
                            int i10 = (charAt == ' ' || charAt == '\n') ? 1 : 0;
                            int i11 = i + 3;
                            int indexOf2 = TextUtils.indexOf(charSequenceArr[0], '\n', i11);
                            String substring = (indexOf2 < 0 || indexOf2 - i11 <= 0) ? "" : charSequenceArr[0].toString().substring(i11, indexOf2);
                            CharSequence substring2 = substring(charSequenceArr[0], 0, i - i10);
                            int length = i11 + substring.length() + (!substring.isEmpty());
                            if (length >= 0 && length < charSequenceArr[0].length() && length <= indexOf) {
                                CharSequence substring3 = substring(charSequenceArr[0], length, indexOf);
                                int i12 = indexOf + 3;
                                char charAt2 = i12 < charSequenceArr[0].length() ? charSequenceArr[0].charAt(i12) : (char) 0;
                                CharSequence charSequence2 = charSequenceArr[0];
                                CharSequence substring4 = substring(charSequence2, i12 + ((charAt2 == ' ' || charAt2 == '\n') ? 1 : 0), charSequence2.length());
                                if (substring2.length() != 0) {
                                    i4 = 2;
                                    substring2 = AndroidUtilities.concat(substring2, "\n");
                                } else {
                                    i4 = 2;
                                    i10 = 1;
                                }
                                if (substring4.length() > 0 && substring4.charAt(0) != '\n') {
                                    CharSequence[] charSequenceArr2 = new CharSequence[i4];
                                    charSequenceArr2[0] = "\n";
                                    charSequenceArr2[1] = substring4;
                                    substring4 = AndroidUtilities.concat(charSequenceArr2);
                                }
                                if (substring3.length() <= 0 || substring3.charAt(substring3.length() - 1) != '\n') {
                                    i5 = 0;
                                } else {
                                    substring3 = substring(substring3, 0, substring3.length() - 1);
                                    i5 = 1;
                                }
                                if (!TextUtils.isEmpty(substring3)) {
                                    if (substring3.length() > 1 && substring3.charAt(0) == '\n') {
                                        substring3 = substring3.subSequence(1, substring3.length());
                                        indexOf--;
                                    }
                                    charSequenceArr[0] = AndroidUtilities.concat(substring2, substring3, substring4);
                                    TLRPC.MessageEntity tL_messageEntityPre = new TLRPC.TL_messageEntityPre();
                                    int i13 = i10 ^ 1;
                                    tL_messageEntityPre.offset = i + i13;
                                    tL_messageEntityPre.length = ((((indexOf - i) - 3) - (substring.length() + (!substring.isEmpty()))) + i13) - i5;
                                    if (TextUtils.isEmpty(substring) || substring.trim().length() == 0) {
                                        substring = "";
                                    }
                                    tL_messageEntityPre.language = substring;
                                    arrayList.add(tL_messageEntityPre);
                                    i2 -= 6;
                                }
                            }
                            i8 = i2;
                            i6 = -1;
                            i7 = 2;
                        }
                    } else {
                        z2 = charSequenceArr[0].length() - indexOf > i7 && charSequenceArr[0].charAt(indexOf + 1) == '`' && charSequenceArr[0].charAt(indexOf + 2) == '`';
                        i = indexOf;
                        i8 = (z2 ? 3 : 1) + indexOf;
                    }
                }
                CharSequence substring5 = substring(charSequenceArr[0], 0, i);
                CharSequence substring6 = substring(charSequenceArr[0], i3, indexOf);
                CharSequence charSequence3 = charSequenceArr[0];
                charSequenceArr[0] = AndroidUtilities.concat(substring5, substring6, substring(charSequence3, indexOf + 1, charSequence3.length()));
                TLRPC.MessageEntity tL_messageEntityCode = new TLRPC.TL_messageEntityCode();
                tL_messageEntityCode.offset = i;
                tL_messageEntityCode.length = (indexOf - i) - 1;
                arrayList.add(tL_messageEntityCode);
                i2 -= 2;
                i8 = i2;
                i6 = -1;
                i7 = 2;
                z2 = false;
            }
            if (i != i6 && z2) {
                CharSequence substring7 = substring(charSequenceArr[0], 0, i);
                CharSequence charSequence4 = charSequenceArr[0];
                charSequenceArr[0] = AndroidUtilities.concat(substring7, substring(charSequence4, i + 2, charSequence4.length()));
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                TLRPC.MessageEntity tL_messageEntityCode2 = new TLRPC.TL_messageEntityCode();
                tL_messageEntityCode2.offset = i;
                tL_messageEntityCode2.length = 1;
                arrayList.add(tL_messageEntityCode2);
            }
            CharSequence charSequence5 = charSequenceArr[0];
            if (charSequence5 instanceof Spanned) {
                Spanned spanned = (Spanned) charSequence5;
                TextStyleSpan[] textStyleSpanArr = (TextStyleSpan[]) spanned.getSpans(0, charSequence5.length(), TextStyleSpan.class);
                if (textStyleSpanArr != null && textStyleSpanArr.length > 0) {
                    for (TextStyleSpan textStyleSpan : textStyleSpanArr) {
                        int spanStart = spanned.getSpanStart(textStyleSpan);
                        int spanEnd = spanned.getSpanEnd(textStyleSpan);
                        if (!checkInclusion(spanStart, arrayList, false) && !checkInclusion(spanEnd, arrayList, true) && !checkIntersection(spanStart, spanEnd, arrayList)) {
                            if (arrayList == null) {
                                arrayList = new ArrayList<>();
                            }
                            addStyle(textStyleSpan.getStyleFlags(), spanStart, spanEnd, arrayList);
                        }
                    }
                }
                URLSpanUserMention[] uRLSpanUserMentionArr = (URLSpanUserMention[]) spanned.getSpans(0, charSequenceArr[0].length(), URLSpanUserMention.class);
                if (uRLSpanUserMentionArr != null && uRLSpanUserMentionArr.length > 0) {
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                    }
                    for (int i14 = 0; i14 < uRLSpanUserMentionArr.length; i14++) {
                        TLRPC.TL_inputMessageEntityMentionName tL_inputMessageEntityMentionName = new TLRPC.TL_inputMessageEntityMentionName();
                        TLRPC.InputUser inputUser = getMessagesController().getInputUser(Utilities.parseLong(uRLSpanUserMentionArr[i14].getURL()).longValue());
                        tL_inputMessageEntityMentionName.user_id = inputUser;
                        if (inputUser != null) {
                            tL_inputMessageEntityMentionName.offset = spanned.getSpanStart(uRLSpanUserMentionArr[i14]);
                            int min = Math.min(spanned.getSpanEnd(uRLSpanUserMentionArr[i14]), charSequenceArr[0].length());
                            int i15 = tL_inputMessageEntityMentionName.offset;
                            int i16 = min - i15;
                            tL_inputMessageEntityMentionName.length = i16;
                            if (charSequenceArr[0].charAt((i15 + i16) - 1) == ' ') {
                                tL_inputMessageEntityMentionName.length--;
                            }
                            arrayList.add(tL_inputMessageEntityMentionName);
                        }
                    }
                }
                URLSpanReplacement[] uRLSpanReplacementArr = (URLSpanReplacement[]) spanned.getSpans(0, charSequenceArr[0].length(), URLSpanReplacement.class);
                if (uRLSpanReplacementArr != null && uRLSpanReplacementArr.length > 0) {
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                    }
                    for (int i17 = 0; i17 < uRLSpanReplacementArr.length; i17++) {
                        TLRPC.MessageEntity tL_messageEntityTextUrl = new TLRPC.TL_messageEntityTextUrl();
                        tL_messageEntityTextUrl.offset = spanned.getSpanStart(uRLSpanReplacementArr[i17]);
                        tL_messageEntityTextUrl.length = Math.min(spanned.getSpanEnd(uRLSpanReplacementArr[i17]), charSequenceArr[0].length()) - tL_messageEntityTextUrl.offset;
                        tL_messageEntityTextUrl.url = uRLSpanReplacementArr[i17].getURL();
                        arrayList.add(tL_messageEntityTextUrl);
                        TextStyleSpan.TextStyleRun textStyleRun = uRLSpanReplacementArr[i17].getTextStyleRun();
                        if (textStyleRun != null) {
                            int i18 = textStyleRun.flags;
                            int i19 = tL_messageEntityTextUrl.offset;
                            addStyle(i18, i19, tL_messageEntityTextUrl.length + i19, arrayList);
                        }
                    }
                }
                AnimatedEmojiSpan[] animatedEmojiSpanArr = (AnimatedEmojiSpan[]) spanned.getSpans(0, charSequenceArr[0].length(), AnimatedEmojiSpan.class);
                if (animatedEmojiSpanArr != null && animatedEmojiSpanArr.length > 0) {
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                    }
                    for (AnimatedEmojiSpan animatedEmojiSpan : animatedEmojiSpanArr) {
                        if (animatedEmojiSpan != null) {
                            try {
                                TLRPC.TL_messageEntityCustomEmoji tL_messageEntityCustomEmoji = new TLRPC.TL_messageEntityCustomEmoji();
                                tL_messageEntityCustomEmoji.offset = spanned.getSpanStart(animatedEmojiSpan);
                                tL_messageEntityCustomEmoji.length = Math.min(spanned.getSpanEnd(animatedEmojiSpan), charSequenceArr[0].length()) - tL_messageEntityCustomEmoji.offset;
                                tL_messageEntityCustomEmoji.document_id = animatedEmojiSpan.getDocumentId();
                                tL_messageEntityCustomEmoji.document = animatedEmojiSpan.document;
                                arrayList.add(tL_messageEntityCustomEmoji);
                            } catch (Exception e) {
                                FileLog.e(e);
                            }
                        }
                    }
                }
                CodeHighlighting.Span[] spanArr = (CodeHighlighting.Span[]) spanned.getSpans(0, charSequenceArr[0].length(), CodeHighlighting.Span.class);
                if (spanArr != null && spanArr.length > 0) {
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                    }
                    for (CodeHighlighting.Span span : spanArr) {
                        if (span != null) {
                            try {
                                TLRPC.MessageEntity tL_messageEntityPre2 = new TLRPC.TL_messageEntityPre();
                                tL_messageEntityPre2.offset = spanned.getSpanStart(span);
                                tL_messageEntityPre2.length = Math.min(spanned.getSpanEnd(span), charSequenceArr[0].length()) - tL_messageEntityPre2.offset;
                                tL_messageEntityPre2.language = span.lng;
                                arrayList.add(tL_messageEntityPre2);
                            } catch (Exception e2) {
                                FileLog.e(e2);
                            }
                        }
                    }
                }
                QuoteSpan[] quoteSpanArr = (QuoteSpan[]) spanned.getSpans(0, charSequenceArr[0].length(), QuoteSpan.class);
                if (quoteSpanArr != null && quoteSpanArr.length > 0) {
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                    }
                    for (QuoteSpan quoteSpan : quoteSpanArr) {
                        if (quoteSpan != null) {
                            try {
                                TLRPC.MessageEntity tL_messageEntityBlockquote = new TLRPC.TL_messageEntityBlockquote();
                                tL_messageEntityBlockquote.offset = spanned.getSpanStart(quoteSpan);
                                tL_messageEntityBlockquote.length = Math.min(spanned.getSpanEnd(quoteSpan), charSequenceArr[0].length()) - tL_messageEntityBlockquote.offset;
                                tL_messageEntityBlockquote.collapsed = quoteSpan.isCollapsing;
                                arrayList.add(tL_messageEntityBlockquote);
                            } catch (Exception e3) {
                                FileLog.e(e3);
                            }
                        }
                    }
                }
                if (spanned instanceof Spannable) {
                    Spannable spannable = (Spannable) spanned;
                    AndroidUtilities.addLinksSafe(spannable, 1, false, false);
                    URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, charSequenceArr[0].length(), URLSpan.class);
                    if (uRLSpanArr != null && uRLSpanArr.length > 0) {
                        if (arrayList == null) {
                            arrayList = new ArrayList<>();
                        }
                        for (int i20 = 0; i20 < uRLSpanArr.length; i20++) {
                            URLSpan uRLSpan = uRLSpanArr[i20];
                            if (!(uRLSpan instanceof URLSpanReplacement) && !(uRLSpan instanceof URLSpanUserMention)) {
                                TLRPC.MessageEntity tL_messageEntityUrl = new TLRPC.TL_messageEntityUrl();
                                tL_messageEntityUrl.offset = spanned.getSpanStart(uRLSpanArr[i20]);
                                tL_messageEntityUrl.length = Math.min(spanned.getSpanEnd(uRLSpanArr[i20]), charSequenceArr[0].length()) - tL_messageEntityUrl.offset;
                                tL_messageEntityUrl.url = uRLSpanArr[i20].getURL();
                                arrayList.add(tL_messageEntityUrl);
                                spannable.removeSpan(uRLSpanArr[i20]);
                            }
                        }
                    }
                }
            }
            CharSequence charSequence6 = charSequenceArr[0];
            if (arrayList == null) {
                arrayList = new ArrayList<>();
            }
            CharSequence parsePattern = parsePattern(parsePattern(parsePattern(charSequence6, BOLD_PATTERN, arrayList, new GenericProvider() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda76
                @Override // org.telegram.messenger.GenericProvider
                public final Object provide(Object obj) {
                    TLRPC.MessageEntity lambda$getEntities$181;
                    lambda$getEntities$181 = MediaDataController.lambda$getEntities$181((Void) obj);
                    return lambda$getEntities$181;
                }
            }), ITALIC_PATTERN, arrayList, new GenericProvider() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda77
                @Override // org.telegram.messenger.GenericProvider
                public final Object provide(Object obj) {
                    TLRPC.MessageEntity lambda$getEntities$182;
                    lambda$getEntities$182 = MediaDataController.lambda$getEntities$182((Void) obj);
                    return lambda$getEntities$182;
                }
            }), SPOILER_PATTERN, arrayList, new GenericProvider() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda78
                @Override // org.telegram.messenger.GenericProvider
                public final Object provide(Object obj) {
                    TLRPC.MessageEntity lambda$getEntities$183;
                    lambda$getEntities$183 = MediaDataController.lambda$getEntities$183((Void) obj);
                    return lambda$getEntities$183;
                }
            });
            if (z) {
                parsePattern = parsePattern(parsePattern, STRIKE_PATTERN, arrayList, new GenericProvider() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda79
                    @Override // org.telegram.messenger.GenericProvider
                    public final Object provide(Object obj) {
                        TLRPC.MessageEntity lambda$getEntities$184;
                        lambda$getEntities$184 = MediaDataController.lambda$getEntities$184((Void) obj);
                        return lambda$getEntities$184;
                    }
                });
            }
            while (parsePattern.length() > 0 && (parsePattern.charAt(0) == '\n' || parsePattern.charAt(0) == ' ')) {
                parsePattern = parsePattern.subSequence(1, parsePattern.length());
                for (int i21 = 0; i21 < arrayList.size(); i21++) {
                    TLRPC.MessageEntity messageEntity = arrayList.get(i21);
                    int i22 = messageEntity.offset;
                    if (i22 == 0) {
                        messageEntity.length--;
                    }
                    messageEntity.offset = Math.max(0, i22 - 1);
                }
            }
            while (parsePattern.length() > 0 && (parsePattern.charAt(parsePattern.length() - 1) == '\n' || parsePattern.charAt(parsePattern.length() - 1) == ' ')) {
                parsePattern = parsePattern.subSequence(0, parsePattern.length() - 1);
                for (int i23 = 0; i23 < arrayList.size(); i23++) {
                    TLRPC.MessageEntity messageEntity2 = arrayList.get(i23);
                    if (messageEntity2.offset + messageEntity2.length > parsePattern.length()) {
                        messageEntity2.length--;
                    }
                }
            }
            charSequenceArr[0] = parsePattern;
        }
        return arrayList;
    }

    public ArrayList<TLRPC.StickerSetCovered> getFeaturedEmojiSets() {
        return this.featuredStickerSets[1];
    }

    public ArrayList<TLRPC.StickerSetCovered> getFeaturedStickerSets() {
        return this.featuredStickerSets[0];
    }

    public long getFeaturedStickersHashWithoutUnread(boolean z) {
        long j = 0;
        for (int i = 0; i < this.featuredStickerSets[z ? 1 : 0].size(); i++) {
            TLRPC.StickerSet stickerSet = this.featuredStickerSets[z ? 1 : 0].get(i).set;
            if (!stickerSet.archived) {
                j = calcHash(j, stickerSet.id);
            }
        }
        return j;
    }

    public ArrayList<MessageObject> getFoundMessageObjects() {
        return this.searchResultMessages;
    }

    public TLRPC.Document getGreetingsSticker() {
        TLRPC.Document document = this.greetingsSticker;
        preloadNextGreetingsSticker();
        return document;
    }

    public TLRPC.TL_messages_stickerSet getGroupStickerSetById(TLRPC.StickerSet stickerSet) {
        boolean z;
        TLRPC.StickerSet stickerSet2;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) this.stickerSetsById.get(stickerSet.id);
        if (tL_messages_stickerSet == null) {
            tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) this.groupStickerSets.get(stickerSet.id);
            if (tL_messages_stickerSet != null && (stickerSet2 = tL_messages_stickerSet.set) != null) {
                z = stickerSet2.hash == stickerSet.hash;
            }
            loadGroupStickerSet(stickerSet, z);
        }
        return tL_messages_stickerSet;
    }

    public String getLastSearchQuery() {
        return this.lastSearchQuery;
    }

    public int getMask() {
        int i = 1;
        if (this.lastReturnedNum >= this.searchResultMessages.size() - 1) {
            boolean[] zArr = this.messagesSearchEndReached;
            if (zArr[0] && zArr[1]) {
                i = 0;
            }
        }
        return this.lastReturnedNum > 0 ? i | 2 : i;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x008e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x008f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void getMediaCount(final long j, final long j2, final int i, final int i2, boolean z) {
        ArrayList<TLRPC.MessagesFilter> arrayList;
        TLRPC.MessagesFilter tL_inputMessagesFilterGif;
        TLRPC.InputPeer inputPeer;
        int i3;
        if (z || DialogObject.isEncryptedDialog(j)) {
            getMediaCountDatabase(j, j2, i, i2);
            return;
        }
        TLRPC.TL_messages_getSearchCounters tL_messages_getSearchCounters = new TLRPC.TL_messages_getSearchCounters();
        if (i == 0) {
            arrayList = tL_messages_getSearchCounters.filters;
            tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterPhotoVideo();
        } else if (i == 1) {
            arrayList = tL_messages_getSearchCounters.filters;
            tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterDocument();
        } else if (i == 2) {
            arrayList = tL_messages_getSearchCounters.filters;
            tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterRoundVoice();
        } else if (i == 3) {
            arrayList = tL_messages_getSearchCounters.filters;
            tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterUrl();
        } else {
            if (i != 4) {
                if (i == 5) {
                    arrayList = tL_messages_getSearchCounters.filters;
                    tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterGif();
                }
                if (j2 != 0) {
                    if (j == getUserConfig().getClientUserId()) {
                        tL_messages_getSearchCounters.saved_peer_id = getMessagesController().getInputPeer(j2);
                        i3 = 4 | tL_messages_getSearchCounters.flags;
                    } else {
                        tL_messages_getSearchCounters.top_msg_id = (int) j2;
                        i3 = tL_messages_getSearchCounters.flags | 1;
                    }
                    tL_messages_getSearchCounters.flags = i3;
                }
                inputPeer = getMessagesController().getInputPeer(j);
                tL_messages_getSearchCounters.peer = inputPeer;
                if (inputPeer != null) {
                    return;
                }
                getConnectionsManager().bindRequestToGuid(getConnectionsManager().sendRequest(tL_messages_getSearchCounters, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda153
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        MediaDataController.this.lambda$getMediaCount$130(j, j2, i, i2, tLObject, tL_error);
                    }
                }), i2);
                return;
            }
            arrayList = tL_messages_getSearchCounters.filters;
            tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterMusic();
        }
        arrayList.add(tL_inputMessagesFilterGif);
        if (j2 != 0) {
        }
        inputPeer = getMessagesController().getInputPeer(j);
        tL_messages_getSearchCounters.peer = inputPeer;
        if (inputPeer != null) {
        }
    }

    public void getMediaCounts(final long j, final long j2, final int i) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda155
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$getMediaCounts$129(j2, j, i);
            }
        });
    }

    public Pair<Long, TLRPC.DraftMessage> getOneThreadDraft(long j) {
        LongSparseArray longSparseArray = (LongSparseArray) this.drafts.get(j);
        if (longSparseArray == null || longSparseArray.size() <= 0) {
            return null;
        }
        return new Pair<>(Long.valueOf(longSparseArray.keyAt(0)), longSparseArray.valueAt(0));
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ae A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Integer getPremiumHintAnnualDiscount(boolean z) {
        TLRPC.TL_help_premiumPromo tL_help_premiumPromo;
        int i;
        long j;
        ProductDetails productDetails;
        ProductDetails.SubscriptionOfferDetails subscriptionOfferDetails;
        long j2;
        ProductDetails productDetails2;
        Iterator it;
        ProductDetails.SubscriptionOfferDetails subscriptionOfferDetails2;
        if ((z && (!BillingController.getInstance().isReady() || BillingController.getInstance().getLastPremiumTransaction() == null)) || (tL_help_premiumPromo = this.premiumPromo) == null) {
            return null;
        }
        Iterator<TLRPC.TL_premiumSubscriptionOption> it2 = tL_help_premiumPromo.period_options.iterator();
        double d = 0.0d;
        boolean z2 = false;
        while (true) {
            i = 12;
            if (!it2.hasNext()) {
                break;
            }
            TLRPC.TL_premiumSubscriptionOption next = it2.next();
            if (z) {
                if (next.current && Objects.equals(next.transaction.replaceAll("^(.*?)(?:\\.\\.\\d*|)$", "$1"), BillingController.getInstance().getLastPremiumTransaction())) {
                    if (!BuildVars.useInvoiceBilling() && (productDetails2 = BillingController.PREMIUM_PRODUCT_DETAILS) != null) {
                        it = productDetails2.getSubscriptionOfferDetails().iterator();
                        while (true) {
                            if (it.hasNext()) {
                                subscriptionOfferDetails2 = null;
                                break;
                            }
                            subscriptionOfferDetails2 = (ProductDetails.SubscriptionOfferDetails) it.next();
                            String billingPeriod = ((ProductDetails.PricingPhase) subscriptionOfferDetails2.getPricingPhases().getPricingPhaseList().get(0)).getBillingPeriod();
                            int i2 = next.months;
                            if (i2 == 12) {
                                if (billingPeriod.equals("P1Y")) {
                                    break;
                                }
                            } else if (billingPeriod.equals(String.format(Locale.ROOT, "P%dM", Integer.valueOf(i2)))) {
                                break;
                            }
                        }
                        if (subscriptionOfferDetails2 != null) {
                            j2 = ((ProductDetails.PricingPhase) subscriptionOfferDetails2.getPricingPhases().getPricingPhaseList().get(0)).getPriceAmountMicros();
                            double d2 = j2;
                            double d3 = next.months;
                            Double.isNaN(d2);
                            Double.isNaN(d3);
                            d = d2 / d3;
                            z2 = true;
                        }
                    }
                    j2 = next.amount;
                    double d22 = j2;
                    double d32 = next.months;
                    Double.isNaN(d22);
                    Double.isNaN(d32);
                    d = d22 / d32;
                    z2 = true;
                }
            } else if (next.months == 1) {
                if (!BuildVars.useInvoiceBilling()) {
                    it = productDetails2.getSubscriptionOfferDetails().iterator();
                    while (true) {
                        if (it.hasNext()) {
                        }
                    }
                    if (subscriptionOfferDetails2 != null) {
                    }
                }
                j2 = next.amount;
                double d222 = j2;
                double d322 = next.months;
                Double.isNaN(d222);
                Double.isNaN(d322);
                d = d222 / d322;
                z2 = true;
            }
        }
        Iterator<TLRPC.TL_premiumSubscriptionOption> it3 = this.premiumPromo.period_options.iterator();
        int i3 = 0;
        while (it3.hasNext()) {
            TLRPC.TL_premiumSubscriptionOption next2 = it3.next();
            if (z2 && next2.months == i) {
                if (!BuildVars.useInvoiceBilling() && (productDetails = BillingController.PREMIUM_PRODUCT_DETAILS) != null) {
                    Iterator it4 = productDetails.getSubscriptionOfferDetails().iterator();
                    while (true) {
                        if (!it4.hasNext()) {
                            subscriptionOfferDetails = null;
                            break;
                        }
                        subscriptionOfferDetails = (ProductDetails.SubscriptionOfferDetails) it4.next();
                        String billingPeriod2 = ((ProductDetails.PricingPhase) subscriptionOfferDetails.getPricingPhases().getPricingPhaseList().get(0)).getBillingPeriod();
                        int i4 = next2.months;
                        if (i4 == i) {
                            if (billingPeriod2.equals("P1Y")) {
                                break;
                            }
                        } else {
                            if (billingPeriod2.equals(String.format(Locale.ROOT, "P%dM", Integer.valueOf(i4)))) {
                                break;
                            }
                            i = 12;
                        }
                    }
                    if (subscriptionOfferDetails != null) {
                        j = ((ProductDetails.PricingPhase) subscriptionOfferDetails.getPricingPhases().getPricingPhaseList().get(0)).getPriceAmountMicros();
                        double d4 = j;
                        double d5 = next2.months;
                        Double.isNaN(d4);
                        Double.isNaN(d5);
                        i3 = (int) ((1.0d - ((d4 / d5) / d)) * 100.0d);
                    }
                }
                j = next2.amount;
                double d42 = j;
                double d52 = next2.months;
                Double.isNaN(d42);
                Double.isNaN(d52);
                i3 = (int) ((1.0d - ((d42 / d52) / d)) * 100.0d);
            }
            i = 12;
        }
        if (!z2 || i3 <= 0) {
            return null;
        }
        return Integer.valueOf(i3);
    }

    public TLRPC.TL_help_premiumPromo getPremiumPromo() {
        return this.premiumPromo;
    }

    public List<TLRPC.TL_availableReaction> getReactionsList() {
        return this.reactionsList;
    }

    public HashMap<String, TLRPC.TL_availableReaction> getReactionsMap() {
        return this.reactionsMap;
    }

    public ArrayList<TLRPC.EmojiStatus> getRecentEmojiStatuses() {
        if (!this.emojiStatusesFromCacheFetched[0]) {
            fetchEmojiStatuses(0, true);
        } else if (this.emojiStatuses[0] == null || (this.emojiStatusesFetchDate[0] != null && (System.currentTimeMillis() / 1000) - this.emojiStatusesFetchDate[0].longValue() > 1800)) {
            fetchEmojiStatuses(0, false);
        }
        return this.emojiStatuses[0];
    }

    public ArrayList<TLRPC.Document> getRecentGifs() {
        return new ArrayList<>(this.recentGifs);
    }

    public ArrayList<TLRPC.Reaction> getRecentReactions() {
        return this.recentReactions;
    }

    public ArrayList<TLRPC.Document> getRecentStickers(int i) {
        return getRecentStickers(i, false);
    }

    public ArrayList<TLRPC.Document> getRecentStickers(int i, boolean z) {
        ArrayList<TLRPC.Document> arrayList = this.recentStickers[i];
        if (i == 7) {
            return new ArrayList<>(this.recentStickers[i]);
        }
        ArrayList<TLRPC.Document> arrayList2 = new ArrayList<>(arrayList.subList(0, Math.min(arrayList.size(), 20)));
        if (z && !arrayList2.isEmpty()) {
            arrayList2.add(0, new TLRPC.TL_documentEmpty());
        }
        return arrayList2;
    }

    public ArrayList<TLRPC.Document> getRecentStickersNoCopy(int i) {
        return this.recentStickers[i];
    }

    public ArrayList<TLRPC.Reaction> getSavedReactions() {
        return this.savedReactions;
    }

    public int getSearchCount() {
        if (this.searchServerResultMessages.isEmpty()) {
            int[] iArr = this.messagesSearchCount;
            return Math.max(Math.max(iArr[0] + iArr[1], this.messagesLocalSearchCount), this.searchServerResultMessages.size());
        }
        int[] iArr2 = this.messagesSearchCount;
        return Math.max(iArr2[0] + iArr2[1], this.searchServerResultMessages.size());
    }

    public int getSearchPosition() {
        return this.lastReturnedNum;
    }

    public LongSparseArray getStickerByIds(int i) {
        return this.stickersByIds[i];
    }

    public TLRPC.TL_messages_stickerSet getStickerSet(TLRPC.InputStickerSet inputStickerSet, Integer num, boolean z) {
        return getStickerSet(inputStickerSet, num, z, null);
    }

    public TLRPC.TL_messages_stickerSet getStickerSet(TLRPC.InputStickerSet inputStickerSet, Integer num, boolean z, Utilities.Callback<TLRPC.TL_messages_stickerSet> callback) {
        return getStickerSet(inputStickerSet, num, z, false, callback);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public TLRPC.TL_messages_stickerSet getStickerSet(final TLRPC.InputStickerSet inputStickerSet, final Integer num, final boolean z, boolean z2, final Utilities.Callback<TLRPC.TL_messages_stickerSet> callback) {
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        String str;
        Object obj;
        DispatchQueue storageQueue;
        Runnable runnable;
        if (inputStickerSet == null) {
            return null;
        }
        boolean z3 = inputStickerSet instanceof TLRPC.TL_inputStickerSetID;
        if (z3 && this.stickerSetsById.containsKey(inputStickerSet.id)) {
            obj = this.stickerSetsById.get(inputStickerSet.id);
        } else {
            if (!(inputStickerSet instanceof TLRPC.TL_inputStickerSetShortName) || (str = inputStickerSet.short_name) == null || !this.stickerSetsByName.containsKey(str.toLowerCase())) {
                if ((!(inputStickerSet instanceof TLRPC.TL_inputStickerSetEmojiDefaultStatuses) || (tL_messages_stickerSet = this.stickerSetDefaultStatuses) == null) && (!(inputStickerSet instanceof TLRPC.TL_inputStickerSetEmojiChannelDefaultStatuses) || (tL_messages_stickerSet = this.stickerSetDefaultChannelStatuses) == null)) {
                    tL_messages_stickerSet = null;
                }
                if (tL_messages_stickerSet == null) {
                    if (!z2 && callback != null) {
                        callback.run(tL_messages_stickerSet);
                    }
                    return tL_messages_stickerSet;
                }
                final String inputSetKey = inputSetKey(inputStickerSet);
                if (callback == null && this.loadingStickerSetsKeys.contains(inputSetKey)) {
                    return null;
                }
                this.loadingStickerSetsKeys.add(inputSetKey);
                if (z3) {
                    storageQueue = getMessagesStorage().getStorageQueue();
                    runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda240
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaDataController.this.lambda$getStickerSet$34(inputStickerSet, num, inputSetKey, callback, z);
                        }
                    };
                } else {
                    if (!(inputStickerSet instanceof TLRPC.TL_inputStickerSetShortName)) {
                        if (z) {
                            this.loadingStickerSetsKeys.remove(inputSetKey);
                        } else {
                            fetchStickerSetInternal(inputStickerSet, new Utilities.Callback2() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda242
                                @Override // org.telegram.messenger.Utilities.Callback2
                                public final void run(Object obj2, Object obj3) {
                                    MediaDataController.this.lambda$getStickerSet$38(inputSetKey, callback, inputStickerSet, (Boolean) obj2, (TLRPC.TL_messages_stickerSet) obj3);
                                }
                            });
                        }
                        return null;
                    }
                    storageQueue = getMessagesStorage().getStorageQueue();
                    runnable = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda241
                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaDataController.this.lambda$getStickerSet$37(inputStickerSet, num, inputSetKey, callback, z);
                        }
                    };
                }
                storageQueue.postRunnable(runnable);
                return null;
            }
            obj = this.stickerSetsByName.get(inputStickerSet.short_name.toLowerCase());
        }
        tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) obj;
        if (tL_messages_stickerSet == null) {
        }
    }

    public TLRPC.TL_messages_stickerSet getStickerSet(TLRPC.InputStickerSet inputStickerSet, boolean z) {
        return getStickerSet(inputStickerSet, null, z, null);
    }

    public TLRPC.TL_messages_stickerSet getStickerSetByEmojiOrName(String str) {
        return this.diceStickerSetsByEmoji.get(str);
    }

    public TLRPC.TL_messages_stickerSet getStickerSetById(long j) {
        return (TLRPC.TL_messages_stickerSet) this.stickerSetsById.get(j);
    }

    public TLRPC.TL_messages_stickerSet getStickerSetByName(String str) {
        if (str == null) {
            return null;
        }
        return this.stickerSetsByName.get(str.toLowerCase());
    }

    public String getStickerSetName(long j) {
        TLRPC.StickerSet stickerSet;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) this.stickerSetsById.get(j);
        if (tL_messages_stickerSet != null) {
            stickerSet = tL_messages_stickerSet.set;
        } else {
            TLRPC.StickerSetCovered stickerSetCovered = (TLRPC.StickerSetCovered) this.featuredStickerSetsById[0].get(j);
            if (stickerSetCovered != null) {
                stickerSet = stickerSetCovered.set;
            } else {
                TLRPC.StickerSetCovered stickerSetCovered2 = (TLRPC.StickerSetCovered) this.featuredStickerSetsById[1].get(j);
                if (stickerSetCovered2 == null) {
                    return null;
                }
                stickerSet = stickerSetCovered2.set;
            }
        }
        return stickerSet.short_name;
    }

    public ArrayList<TLRPC.TL_messages_stickerSet> getStickerSets(int i) {
        return i == 3 ? this.stickerSets[2] : this.stickerSets[i];
    }

    public ArrayList<TLRPC.Reaction> getTopReactions() {
        return this.topReactions;
    }

    public ArrayList<Long> getUnreadEmojiSets() {
        return this.unreadStickerSets[1];
    }

    public ArrayList<Long> getUnreadStickerSets() {
        return this.unreadStickerSets[0];
    }

    public boolean hasRecentGif(TLRPC.Document document) {
        for (int i = 0; i < this.recentGifs.size(); i++) {
            TLRPC.Document document2 = this.recentGifs.get(i);
            if (document2.id == document.id) {
                this.recentGifs.remove(i);
                this.recentGifs.add(0, document2);
                return true;
            }
        }
        return false;
    }

    public void increaseInlineRating(long j) {
        TLRPC.TL_topPeer tL_topPeer;
        if (getUserConfig().suggestContacts) {
            int max = getUserConfig().botRatingLoadTime != 0 ? Math.max(1, ((int) (System.currentTimeMillis() / 1000)) - getUserConfig().botRatingLoadTime) : 60;
            int i = 0;
            while (true) {
                if (i >= this.inlineBots.size()) {
                    tL_topPeer = null;
                    break;
                }
                tL_topPeer = this.inlineBots.get(i);
                if (tL_topPeer.peer.user_id == j) {
                    break;
                } else {
                    i++;
                }
            }
            if (tL_topPeer == null) {
                tL_topPeer = new TLRPC.TL_topPeer();
                TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
                tL_topPeer.peer = tL_peerUser;
                tL_peerUser.user_id = j;
                this.inlineBots.add(tL_topPeer);
            }
            tL_topPeer.rating += Math.exp(max / getMessagesController().ratingDecay);
            Collections.sort(this.inlineBots, new Comparator() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda184
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$increaseInlineRating$150;
                    lambda$increaseInlineRating$150 = MediaDataController.lambda$increaseInlineRating$150((TLRPC.TL_topPeer) obj, (TLRPC.TL_topPeer) obj2);
                    return lambda$increaseInlineRating$150;
                }
            });
            if (this.inlineBots.size() > 20) {
                ArrayList<TLRPC.TL_topPeer> arrayList = this.inlineBots;
                arrayList.remove(arrayList.size() - 1);
            }
            savePeer(j, 1, tL_topPeer.rating);
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadInlineHints, new Object[0]);
        }
    }

    public void increasePeerRaiting(final long j) {
        TLRPC.User user;
        if (!getUserConfig().suggestContacts || !DialogObject.isUserDialog(j) || (user = getMessagesController().getUser(Long.valueOf(j))) == null || user.bot || user.self) {
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda238
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$increasePeerRaiting$157(j);
            }
        });
    }

    public void increaseWebappRating(long j) {
        TLRPC.TL_topPeer tL_topPeer;
        TLRPC.User user = getMessagesController().getUser(Long.valueOf(j));
        if (user == null || !user.bot) {
            return;
        }
        int max = getUserConfig().webappRatingLoadTime != 0 ? Math.max(1, ((int) (System.currentTimeMillis() / 1000)) - getUserConfig().webappRatingLoadTime) : 60;
        int i = 0;
        while (true) {
            if (i >= this.inlineBots.size()) {
                tL_topPeer = null;
                break;
            }
            tL_topPeer = this.inlineBots.get(i);
            if (tL_topPeer.peer.user_id == j) {
                break;
            } else {
                i++;
            }
        }
        if (tL_topPeer == null) {
            tL_topPeer = new TLRPC.TL_topPeer();
            TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
            tL_topPeer.peer = tL_peerUser;
            tL_peerUser.user_id = j;
            this.webapps.add(tL_topPeer);
        }
        tL_topPeer.rating += Math.exp(max / getMessagesController().ratingDecay);
        Collections.sort(this.inlineBots, new Comparator() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda228
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$increaseWebappRating$151;
                lambda$increaseWebappRating$151 = MediaDataController.lambda$increaseWebappRating$151((TLRPC.TL_topPeer) obj, (TLRPC.TL_topPeer) obj2);
                return lambda$increaseWebappRating$151;
            }
        });
        if (this.webapps.size() > 20) {
            ArrayList<TLRPC.TL_topPeer> arrayList = this.webapps;
            arrayList.remove(arrayList.size() - 1);
        }
        savePeer(j, 2, tL_topPeer.rating);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadWebappsHints, new Object[0]);
    }

    public void installShortcut(long j, int i) {
        installShortcut(j, i, null);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(17:31|(3:33|(2:35|36)|131)(7:132|(1:134)(2:137|(1:139)(1:140))|135|136|(2:(2:126|127)(1:91)|(9:95|96|97|(4:99|(1:101)(1:116)|102|103)(6:117|(1:119)|120|121|122|123)|104|105|106|107|108)(1:94))(1:40)|41|(10:43|(1:45)(1:71)|46|(1:48)(2:(2:60|(1:62)(1:64))(2:65|(1:70)(1:69))|63)|49|(1:51)(1:58)|52|(1:54)(1:57)|55|56)(4:72|(1:74)(3:(2:78|(1:80)(1:83))(2:84|(1:89)(1:88))|81|82)|75|76))|37|(0)|(0)(0)|(0)|95|96|97|(0)(0)|104|105|106|107|108|41|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x01c8, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x0134, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x0135, code lost:
    
        r17 = "android.intent.extra.shortcut.ICON_RESOURCE";
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x00cc, code lost:
    
        if (r13 != null) goto L53;
     */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0147 A[Catch: all -> 0x0134, TryCatch #0 {all -> 0x0134, blocks: (B:97:0x010c, B:99:0x0120, B:101:0x012b, B:103:0x013e, B:116:0x013a, B:117:0x0147, B:119:0x0154, B:120:0x0162), top: B:96:0x010c }] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x00e7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:142:0x00d1 A[Catch: Exception -> 0x0014, TryCatch #4 {Exception -> 0x0014, blocks: (B:3:0x000a, B:5:0x000e, B:9:0x0020, B:13:0x0026, B:15:0x002c, B:18:0x0040, B:21:0x0046, B:22:0x005f, B:26:0x0081, B:31:0x008b, B:33:0x008f, B:36:0x00ce, B:41:0x01d3, B:43:0x01d9, B:46:0x01e2, B:48:0x0202, B:49:0x022f, B:51:0x0234, B:52:0x0266, B:55:0x0274, B:57:0x0270, B:60:0x0209, B:62:0x020d, B:63:0x0211, B:64:0x0216, B:65:0x021b, B:67:0x0221, B:69:0x0225, B:70:0x022a, B:72:0x0278, B:74:0x027f, B:75:0x02b2, B:78:0x0287, B:80:0x028b, B:81:0x028f, B:82:0x02af, B:83:0x0296, B:84:0x029b, B:86:0x02a1, B:88:0x02a5, B:89:0x02aa, B:112:0x01d0, B:132:0x00a9, B:134:0x00af, B:135:0x00b1, B:137:0x00b6, B:139:0x00bc, B:140:0x00c2, B:142:0x00d1, B:144:0x00d7, B:145:0x0051, B:147:0x0057, B:148:0x0066, B:150:0x006c, B:152:0x02cf, B:155:0x0017), top: B:2:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x008b A[Catch: Exception -> 0x0014, TryCatch #4 {Exception -> 0x0014, blocks: (B:3:0x000a, B:5:0x000e, B:9:0x0020, B:13:0x0026, B:15:0x002c, B:18:0x0040, B:21:0x0046, B:22:0x005f, B:26:0x0081, B:31:0x008b, B:33:0x008f, B:36:0x00ce, B:41:0x01d3, B:43:0x01d9, B:46:0x01e2, B:48:0x0202, B:49:0x022f, B:51:0x0234, B:52:0x0266, B:55:0x0274, B:57:0x0270, B:60:0x0209, B:62:0x020d, B:63:0x0211, B:64:0x0216, B:65:0x021b, B:67:0x0221, B:69:0x0225, B:70:0x022a, B:72:0x0278, B:74:0x027f, B:75:0x02b2, B:78:0x0287, B:80:0x028b, B:81:0x028f, B:82:0x02af, B:83:0x0296, B:84:0x029b, B:86:0x02a1, B:88:0x02a5, B:89:0x02aa, B:112:0x01d0, B:132:0x00a9, B:134:0x00af, B:135:0x00b1, B:137:0x00b6, B:139:0x00bc, B:140:0x00c2, B:142:0x00d1, B:144:0x00d7, B:145:0x0051, B:147:0x0057, B:148:0x0066, B:150:0x006c, B:152:0x02cf, B:155:0x0017), top: B:2:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00dc A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x01d9 A[Catch: Exception -> 0x0014, TryCatch #4 {Exception -> 0x0014, blocks: (B:3:0x000a, B:5:0x000e, B:9:0x0020, B:13:0x0026, B:15:0x002c, B:18:0x0040, B:21:0x0046, B:22:0x005f, B:26:0x0081, B:31:0x008b, B:33:0x008f, B:36:0x00ce, B:41:0x01d3, B:43:0x01d9, B:46:0x01e2, B:48:0x0202, B:49:0x022f, B:51:0x0234, B:52:0x0266, B:55:0x0274, B:57:0x0270, B:60:0x0209, B:62:0x020d, B:63:0x0211, B:64:0x0216, B:65:0x021b, B:67:0x0221, B:69:0x0225, B:70:0x022a, B:72:0x0278, B:74:0x027f, B:75:0x02b2, B:78:0x0287, B:80:0x028b, B:81:0x028f, B:82:0x02af, B:83:0x0296, B:84:0x029b, B:86:0x02a1, B:88:0x02a5, B:89:0x02aa, B:112:0x01d0, B:132:0x00a9, B:134:0x00af, B:135:0x00b1, B:137:0x00b6, B:139:0x00bc, B:140:0x00c2, B:142:0x00d1, B:144:0x00d7, B:145:0x0051, B:147:0x0057, B:148:0x0066, B:150:0x006c, B:152:0x02cf, B:155:0x0017), top: B:2:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0278 A[Catch: Exception -> 0x0014, TryCatch #4 {Exception -> 0x0014, blocks: (B:3:0x000a, B:5:0x000e, B:9:0x0020, B:13:0x0026, B:15:0x002c, B:18:0x0040, B:21:0x0046, B:22:0x005f, B:26:0x0081, B:31:0x008b, B:33:0x008f, B:36:0x00ce, B:41:0x01d3, B:43:0x01d9, B:46:0x01e2, B:48:0x0202, B:49:0x022f, B:51:0x0234, B:52:0x0266, B:55:0x0274, B:57:0x0270, B:60:0x0209, B:62:0x020d, B:63:0x0211, B:64:0x0216, B:65:0x021b, B:67:0x0221, B:69:0x0225, B:70:0x022a, B:72:0x0278, B:74:0x027f, B:75:0x02b2, B:78:0x0287, B:80:0x028b, B:81:0x028f, B:82:0x02af, B:83:0x0296, B:84:0x029b, B:86:0x02a1, B:88:0x02a5, B:89:0x02aa, B:112:0x01d0, B:132:0x00a9, B:134:0x00af, B:135:0x00b1, B:137:0x00b6, B:139:0x00bc, B:140:0x00c2, B:142:0x00d1, B:144:0x00d7, B:145:0x0051, B:147:0x0057, B:148:0x0066, B:150:0x006c, B:152:0x02cf, B:155:0x0017), top: B:2:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x00ff  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0102 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0120 A[Catch: all -> 0x0134, TryCatch #0 {all -> 0x0134, blocks: (B:97:0x010c, B:99:0x0120, B:101:0x012b, B:103:0x013e, B:116:0x013a, B:117:0x0147, B:119:0x0154, B:120:0x0162), top: B:96:0x010c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void installShortcut(long j, int i, Utilities.Callback<Boolean> callback) {
        TLRPC.Chat chat;
        TLRPC.User user;
        MessagesController messagesController;
        Long valueOf;
        String str;
        TLRPC.FileLocation fileLocation;
        boolean z;
        String str2;
        PendingIntent pendingIntent;
        Bitmap bitmap;
        Bitmap bitmap2;
        Context context;
        int i2;
        Context context2;
        int i3;
        IconCompat createWithResource;
        PendingIntent pendingIntent2;
        TLRPC.UserProfilePhoto userProfilePhoto;
        int i4;
        try {
            Intent createIntrnalShortcutIntent = i == SHORTCUT_TYPE_USER_OR_CHAT ? createIntrnalShortcutIntent(j) : createIntrnalAttachedBotShortcutIntent(j);
            if (createIntrnalShortcutIntent == null) {
                if (callback != null) {
                    callback.run(Boolean.FALSE);
                    return;
                }
                return;
            }
            if (DialogObject.isEncryptedDialog(j)) {
                TLRPC.EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j)));
                if (encryptedChat == null) {
                    if (callback != null) {
                        callback.run(Boolean.FALSE);
                        return;
                    }
                    return;
                }
                messagesController = getMessagesController();
                valueOf = Long.valueOf(encryptedChat.user_id);
            } else if (DialogObject.isUserDialog(j)) {
                messagesController = getMessagesController();
                valueOf = Long.valueOf(j);
            } else {
                if (!DialogObject.isChatDialog(j)) {
                    if (callback != null) {
                        callback.run(Boolean.FALSE);
                        return;
                    }
                    return;
                }
                chat = getMessagesController().getChat(Long.valueOf(-j));
                user = null;
                if (user != null && chat == null) {
                    if (callback != null) {
                        callback.run(Boolean.FALSE);
                        return;
                    }
                    return;
                }
                if (user == null) {
                    if (i != SHORTCUT_TYPE_ATTACHED_BOT) {
                        if (UserObject.isReplyUser(user)) {
                            i4 = R.string.RepliesTitle;
                        } else if (UserObject.isUserSelf(user)) {
                            i4 = R.string.SavedMessages;
                        } else {
                            str = ContactsController.formatName(user.first_name, user.last_name);
                            userProfilePhoto = user.photo;
                        }
                        str = LocaleController.getString(i4);
                        fileLocation = null;
                        z = true;
                        if (z && fileLocation == null) {
                            str2 = "android.intent.extra.shortcut.ICON_RESOURCE";
                            pendingIntent = null;
                            bitmap = null;
                        } else {
                            if (z) {
                                try {
                                    bitmap = BitmapFactory.decodeFile(getFileLoader().getPathToAttach(fileLocation, true).toString());
                                } catch (Throwable th) {
                                    th = th;
                                    str2 = "android.intent.extra.shortcut.ICON_RESOURCE";
                                    pendingIntent = null;
                                    bitmap = null;
                                    FileLog.e(th);
                                    if (Build.VERSION.SDK_INT < 26) {
                                    }
                                }
                            } else {
                                bitmap = null;
                            }
                            if (z && bitmap == null) {
                                str2 = "android.intent.extra.shortcut.ICON_RESOURCE";
                                pendingIntent = null;
                            } else {
                                int dp = AndroidUtilities.dp(58.0f);
                                Bitmap createBitmap = Bitmap.createBitmap(dp, dp, Bitmap.Config.ARGB_8888);
                                createBitmap.eraseColor(0);
                                Canvas canvas = new Canvas(createBitmap);
                                if (z) {
                                    bitmap2 = createBitmap;
                                    Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                                    BitmapShader bitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
                                    if (roundPaint == null) {
                                        roundPaint = new Paint(1);
                                        bitmapRect = new RectF();
                                    }
                                    float width = dp / bitmap.getWidth();
                                    canvas.save();
                                    canvas.scale(width, width);
                                    roundPaint.setShader(bitmapShader);
                                    str2 = "android.intent.extra.shortcut.ICON_RESOURCE";
                                    try {
                                        bitmapRect.set(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
                                        canvas.drawRoundRect(bitmapRect, bitmap.getWidth(), bitmap.getHeight(), roundPaint);
                                        canvas.restore();
                                    } catch (Throwable th2) {
                                        th = th2;
                                        pendingIntent = null;
                                        FileLog.e(th);
                                        if (Build.VERSION.SDK_INT < 26) {
                                        }
                                    }
                                } else {
                                    AvatarDrawable avatarDrawable = new AvatarDrawable(user);
                                    if (UserObject.isReplyUser(user)) {
                                        avatarDrawable.setAvatarType(12);
                                    } else {
                                        avatarDrawable.setAvatarType(1);
                                    }
                                    bitmap2 = createBitmap;
                                    avatarDrawable.setBounds(0, 0, dp, dp);
                                    avatarDrawable.draw(canvas);
                                    str2 = "android.intent.extra.shortcut.ICON_RESOURCE";
                                }
                                Drawable drawable = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.book_logo);
                                int dp2 = AndroidUtilities.dp(15.0f);
                                int i5 = dp - dp2;
                                int dp3 = i5 - AndroidUtilities.dp(2.0f);
                                int dp4 = i5 - AndroidUtilities.dp(2.0f);
                                drawable.setBounds(dp3, dp4, dp3 + dp2, dp2 + dp4);
                                drawable.draw(canvas);
                                pendingIntent = null;
                                canvas.setBitmap(null);
                                bitmap = bitmap2;
                            }
                        }
                        if (Build.VERSION.SDK_INT < 26) {
                            Intent intent = new Intent();
                            if (bitmap != null) {
                                intent.putExtra("android.intent.extra.shortcut.ICON", bitmap);
                            } else {
                                if (user != null) {
                                    if (user.bot) {
                                        context = ApplicationLoader.applicationContext;
                                        i2 = R.drawable.book_bot;
                                    } else {
                                        context = ApplicationLoader.applicationContext;
                                        i2 = R.drawable.book_user;
                                    }
                                } else if (!ChatObject.isChannel(chat) || chat.megagroup) {
                                    context = ApplicationLoader.applicationContext;
                                    i2 = R.drawable.book_group;
                                } else {
                                    context = ApplicationLoader.applicationContext;
                                    i2 = R.drawable.book_channel;
                                }
                                intent.putExtra(str2, Intent.ShortcutIconResource.fromContext(context, i2));
                            }
                            intent.putExtra("android.intent.extra.shortcut.INTENT", createIntrnalShortcutIntent);
                            intent.putExtra("android.intent.extra.shortcut.NAME", str);
                            intent.putExtra("duplicate", false);
                            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                            ApplicationLoader.applicationContext.sendBroadcast(intent);
                            return;
                        }
                        String str3 = i == SHORTCUT_TYPE_USER_OR_CHAT ? "sdid_" : "bdid_";
                        ShortcutInfoCompat.Builder intent2 = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str3 + j).setShortLabel(str).setIntent(createIntrnalShortcutIntent);
                        if (bitmap != null) {
                            createWithResource = IconCompat.createWithBitmap(bitmap);
                        } else {
                            if (user != null) {
                                if (user.bot) {
                                    context2 = ApplicationLoader.applicationContext;
                                    i3 = R.drawable.book_bot;
                                } else {
                                    context2 = ApplicationLoader.applicationContext;
                                    i3 = R.drawable.book_user;
                                }
                            } else if (!ChatObject.isChannel(chat) || chat.megagroup) {
                                context2 = ApplicationLoader.applicationContext;
                                i3 = R.drawable.book_group;
                            } else {
                                context2 = ApplicationLoader.applicationContext;
                                i3 = R.drawable.book_channel;
                            }
                            createWithResource = IconCompat.createWithResource(context2, i3);
                        }
                        intent2.setIcon(createWithResource);
                        if (callback != null) {
                            byte[] bArr = new byte[16];
                            Utilities.fastRandom.nextBytes(bArr);
                            String bytesToHex = Utilities.bytesToHex(bArr);
                            Intent intent3 = new Intent(ApplicationLoader.applicationContext, (Class<?>) ShortcutResultReceiver.class);
                            intent3.putExtra("account", this.currentAccount);
                            intent3.putExtra("req_id", bytesToHex);
                            pendingIntent2 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, intent3, 167772160);
                            this.shortcutCallbacks.put(bytesToHex, callback);
                        } else {
                            pendingIntent2 = pendingIntent;
                        }
                        ShortcutManagerCompat.requestPinShortcut(ApplicationLoader.applicationContext, intent2.build(), pendingIntent2 == null ? pendingIntent : pendingIntent2.getIntentSender());
                        return;
                    }
                    str = UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j)));
                    userProfilePhoto = user.photo;
                    if (userProfilePhoto != null) {
                        fileLocation = userProfilePhoto.photo_small;
                    }
                    fileLocation = null;
                    z = false;
                    if (z) {
                    }
                    if (z) {
                    }
                    if (z) {
                    }
                    int dp5 = AndroidUtilities.dp(58.0f);
                    Bitmap createBitmap2 = Bitmap.createBitmap(dp5, dp5, Bitmap.Config.ARGB_8888);
                    createBitmap2.eraseColor(0);
                    Canvas canvas2 = new Canvas(createBitmap2);
                    if (z) {
                    }
                    Drawable drawable2 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.book_logo);
                    int dp22 = AndroidUtilities.dp(15.0f);
                    int i52 = dp5 - dp22;
                    int dp32 = i52 - AndroidUtilities.dp(2.0f);
                    int dp42 = i52 - AndroidUtilities.dp(2.0f);
                    drawable2.setBounds(dp32, dp42, dp32 + dp22, dp22 + dp42);
                    drawable2.draw(canvas2);
                    pendingIntent = null;
                    canvas2.setBitmap(null);
                    bitmap = bitmap2;
                    if (Build.VERSION.SDK_INT < 26) {
                    }
                } else {
                    str = chat.title;
                    TLRPC.ChatPhoto chatPhoto = chat.photo;
                    if (chatPhoto != null) {
                        fileLocation = chatPhoto.photo_small;
                        z = false;
                        if (z) {
                        }
                        if (z) {
                        }
                        if (z) {
                        }
                        int dp52 = AndroidUtilities.dp(58.0f);
                        Bitmap createBitmap22 = Bitmap.createBitmap(dp52, dp52, Bitmap.Config.ARGB_8888);
                        createBitmap22.eraseColor(0);
                        Canvas canvas22 = new Canvas(createBitmap22);
                        if (z) {
                        }
                        Drawable drawable22 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.book_logo);
                        int dp222 = AndroidUtilities.dp(15.0f);
                        int i522 = dp52 - dp222;
                        int dp322 = i522 - AndroidUtilities.dp(2.0f);
                        int dp422 = i522 - AndroidUtilities.dp(2.0f);
                        drawable22.setBounds(dp322, dp422, dp322 + dp222, dp222 + dp422);
                        drawable22.draw(canvas22);
                        pendingIntent = null;
                        canvas22.setBitmap(null);
                        bitmap = bitmap2;
                        if (Build.VERSION.SDK_INT < 26) {
                        }
                    }
                    fileLocation = null;
                    z = false;
                    if (z) {
                    }
                    if (z) {
                    }
                    if (z) {
                    }
                    int dp522 = AndroidUtilities.dp(58.0f);
                    Bitmap createBitmap222 = Bitmap.createBitmap(dp522, dp522, Bitmap.Config.ARGB_8888);
                    createBitmap222.eraseColor(0);
                    Canvas canvas222 = new Canvas(createBitmap222);
                    if (z) {
                    }
                    Drawable drawable222 = ApplicationLoader.applicationContext.getResources().getDrawable(R.drawable.book_logo);
                    int dp2222 = AndroidUtilities.dp(15.0f);
                    int i5222 = dp522 - dp2222;
                    int dp3222 = i5222 - AndroidUtilities.dp(2.0f);
                    int dp4222 = i5222 - AndroidUtilities.dp(2.0f);
                    drawable222.setBounds(dp3222, dp4222, dp3222 + dp2222, dp2222 + dp4222);
                    drawable222.draw(canvas222);
                    pendingIntent = null;
                    canvas222.setBitmap(null);
                    bitmap = bitmap2;
                    if (Build.VERSION.SDK_INT < 26) {
                    }
                }
            }
            user = messagesController.getUser(valueOf);
            chat = null;
            if (user != null) {
            }
            if (user == null) {
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public boolean isLoadingStickers(int i) {
        return this.loadingStickers[i];
    }

    public boolean isMenuBotsUpdatedLocal() {
        return this.menuBotsUpdatedLocal;
    }

    public boolean isMessageFound(int i, boolean z) {
        return this.searchServerResultMessagesMap[z ? 1 : 0].indexOfKey(i) >= 0;
    }

    public boolean isSearchLoading() {
        return this.reqId != 0;
    }

    public boolean isShortcutAdded(long j, int i) {
        if (Build.VERSION.SDK_INT >= 26) {
            String str = (i == SHORTCUT_TYPE_USER_OR_CHAT ? "sdid_" : "bdid_") + j;
            List shortcuts = ShortcutManagerCompat.getShortcuts(ApplicationLoader.applicationContext, 4);
            for (int i2 = 0; i2 < shortcuts.size(); i2++) {
                if (((ShortcutInfoCompat) shortcuts.get(i2)).getId().equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isStickerInFavorites(TLRPC.Document document) {
        if (document == null) {
            return false;
        }
        for (int i = 0; i < this.recentStickers[2].size(); i++) {
            TLRPC.Document document2 = this.recentStickers[2].get(i);
            if (document2.id == document.id && document2.dc_id == document.dc_id) {
                return true;
            }
        }
        return false;
    }

    public boolean isStickerPackInstalled(long j) {
        return isStickerPackInstalled(j, true);
    }

    public boolean isStickerPackInstalled(long j, boolean z) {
        return (this.installedStickerSetsById.indexOfKey(j) >= 0 || (z && this.installedForceStickerSetsById.contains(Long.valueOf(j)))) && !(z && this.uninstalledForceStickerSetsById.contains(Long.valueOf(j)));
    }

    public boolean isStickerPackInstalled(String str) {
        return this.stickerSetsByName.containsKey(str);
    }

    public boolean isStickerPackUnread(boolean z, long j) {
        return this.unreadStickerSets[z ? 1 : 0].contains(Long.valueOf(j));
    }

    public void jumpToSearchedMessage(int i, int i2) {
        if (i2 < 0 || i2 >= this.searchResultMessages.size()) {
            return;
        }
        this.lastReturnedNum = i2;
        MessageObject messageObject = this.searchResultMessages.get(i2);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.chatSearchResultsAvailable, Integer.valueOf(i), Integer.valueOf(messageObject.getId()), Integer.valueOf(getMask()), Long.valueOf(messageObject.getDialogId()), Integer.valueOf(this.lastReturnedNum), Integer.valueOf(getSearchCount()), Boolean.TRUE);
    }

    public void loadArchivedStickersCount(final int i, boolean z) {
        if (!z) {
            TLRPC.TL_messages_getArchivedStickers tL_messages_getArchivedStickers = new TLRPC.TL_messages_getArchivedStickers();
            tL_messages_getArchivedStickers.limit = 0;
            tL_messages_getArchivedStickers.masks = i == 1;
            tL_messages_getArchivedStickers.emojis = i == 5;
            getConnectionsManager().sendRequest(tL_messages_getArchivedStickers, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda151
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadArchivedStickersCount$72(i, tLObject, tL_error);
                }
            });
            return;
        }
        int i2 = MessagesController.getNotificationsSettings(this.currentAccount).getInt("archivedStickersCount" + i, -1);
        if (i2 == -1) {
            loadArchivedStickersCount(i, false);
        } else {
            this.archivedStickersCount[i] = i2;
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.archivedStickersCountDidLoad, Integer.valueOf(i));
        }
    }

    public void loadAttachMenuBots(boolean z, boolean z2) {
        loadAttachMenuBots(z, z2, null);
    }

    public void loadAttachMenuBots(boolean z, boolean z2, final Runnable runnable) {
        this.isLoadingMenuBots = true;
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda64
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadAttachMenuBots$3();
                }
            });
            return;
        }
        TLRPC.TL_messages_getAttachMenuBots tL_messages_getAttachMenuBots = new TLRPC.TL_messages_getAttachMenuBots();
        tL_messages_getAttachMenuBots.hash = z2 ? 0L : this.menuBotsUpdateHash;
        getConnectionsManager().sendRequest(tL_messages_getAttachMenuBots, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda65
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$loadAttachMenuBots$4(runnable, tLObject, tL_error);
            }
        });
    }

    public void loadBotInfo(long j, long j2, boolean z, int i) {
        loadBotInfo(j, j2, z, i, null);
    }

    public void loadBotInfo(final long j, final long j2, boolean z, final int i, final Utilities.Callback<TL_bots.BotInfo> callback) {
        if (z) {
            TL_bots.BotInfo botInfo = this.botInfos.get(j + "_" + j2);
            if (botInfo != null) {
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.botInfoDidLoad, botInfo, Integer.valueOf(i));
                return;
            }
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda102
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadBotInfo$199(j, j2, callback, i);
            }
        });
    }

    public void loadBotKeyboard(final MessagesStorage.TopicKey topicKey) {
        TLRPC.Message message = this.botKeyboards.get(topicKey);
        if (message != null) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.botKeyboardDidLoad, message, topicKey);
        } else {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda209
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadBotKeyboard$196(topicKey);
                }
            });
        }
    }

    public void loadDraftsIfNeed() {
        if (getUserConfig().draftsLoaded || this.loadingDrafts) {
            return;
        }
        this.loadingDrafts = true;
        getConnectionsManager().sendRequest(new TLRPC.TL_messages_getAllDrafts(), new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda59
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$loadDraftsIfNeed$187(tLObject, tL_error);
            }
        });
    }

    public void loadEmojiThemes() {
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("emojithemes_config_" + this.currentAccount, 0);
        int i = sharedPreferences.getInt(NotificationBadge.NewHtcHomeBadger.COUNT, 0);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ChatThemeBottomSheet.ChatThemeItem(EmojiThemes.createHomePreviewTheme(this.currentAccount)));
        for (int i2 = 0; i2 < i; i2++) {
            SerializedData serializedData = new SerializedData(Utilities.hexToBytes(sharedPreferences.getString("theme_" + i2, "")));
            try {
                EmojiThemes createPreviewFullTheme = EmojiThemes.createPreviewFullTheme(this.currentAccount, TLRPC.Theme.TLdeserialize(serializedData, serializedData.readInt32(true), true));
                if (createPreviewFullTheme.items.size() >= 4) {
                    arrayList.add(new ChatThemeBottomSheet.ChatThemeItem(createPreviewFullTheme));
                }
                ChatThemeController.chatThemeQueue.postRunnable(new 2(arrayList));
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void loadFeaturedStickers(final boolean z, boolean z2) {
        final long j;
        TLRPC.TL_messages_getFeaturedStickers tL_messages_getFeaturedStickers;
        boolean[] zArr = this.loadingFeaturedStickers;
        if (zArr[z ? 1 : 0]) {
            return;
        }
        zArr[z ? 1 : 0] = true;
        if (z2) {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda174
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadFeaturedStickers$55(z);
                }
            });
            return;
        }
        if (z) {
            TLRPC.TL_messages_getFeaturedEmojiStickers tL_messages_getFeaturedEmojiStickers = new TLRPC.TL_messages_getFeaturedEmojiStickers();
            j = this.loadFeaturedHash[1];
            tL_messages_getFeaturedEmojiStickers.hash = j;
            tL_messages_getFeaturedStickers = tL_messages_getFeaturedEmojiStickers;
        } else {
            TLRPC.TL_messages_getFeaturedStickers tL_messages_getFeaturedStickers2 = new TLRPC.TL_messages_getFeaturedStickers();
            j = this.loadFeaturedHash[0];
            tL_messages_getFeaturedStickers2.hash = j;
            tL_messages_getFeaturedStickers = tL_messages_getFeaturedStickers2;
        }
        getConnectionsManager().sendRequest(tL_messages_getFeaturedStickers, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda175
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$loadFeaturedStickers$58(z, j, tLObject, tL_error);
            }
        });
    }

    public void loadHints(boolean z) {
        if (this.loading || !getUserConfig().suggestContacts) {
            return;
        }
        if (z) {
            if (this.loaded) {
                return;
            }
            this.loading = true;
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadHints$143();
                }
            });
            this.loaded = true;
            return;
        }
        this.loading = true;
        TLRPC.TL_contacts_getTopPeers tL_contacts_getTopPeers = new TLRPC.TL_contacts_getTopPeers();
        tL_contacts_getTopPeers.hash = 0L;
        tL_contacts_getTopPeers.bots_pm = false;
        tL_contacts_getTopPeers.correspondents = true;
        tL_contacts_getTopPeers.groups = false;
        tL_contacts_getTopPeers.channels = false;
        tL_contacts_getTopPeers.bots_inline = true;
        tL_contacts_getTopPeers.bots_app = true;
        tL_contacts_getTopPeers.offset = 0;
        tL_contacts_getTopPeers.limit = 20;
        getConnectionsManager().sendRequest(tL_contacts_getTopPeers, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda10
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$loadHints$148(tLObject, tL_error);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0141 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x002c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadMedia(final long j, final int i, final int i2, final int i3, final int i4, final long j2, int i5, final int i6, final int i7, ReactionsLayoutInBubble.VisibleReaction visibleReaction, String str) {
        boolean z;
        TLRPC.TL_messages_search tL_messages_search;
        TLRPC.MessagesFilter tL_inputMessagesFilterGif;
        int i8;
        if (DialogObject.isChatDialog(j) && ChatObject.isChannel(-j, this.currentAccount)) {
            z = true;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("load media did " + j + " count = " + i + " max_id " + i2 + " type = " + i4 + " cache = " + i5 + " classGuid = " + i6);
            }
            if ((i5 == 0 && TextUtils.isEmpty(str)) || DialogObject.isEncryptedDialog(j)) {
                loadMediaDatabase(j, i, i2, i3, i4, j2, visibleReaction, i6, z, i5, i7);
                return;
            }
            tL_messages_search = new TLRPC.TL_messages_search();
            tL_messages_search.limit = i;
            if (i3 == 0) {
                tL_messages_search.offset_id = i3;
                tL_messages_search.add_offset = -i;
            } else {
                tL_messages_search.offset_id = i2;
            }
            if (visibleReaction != null) {
                tL_messages_search.flags |= 8;
                tL_messages_search.saved_reaction.add(visibleReaction.toTLReaction());
            }
            if (i4 != 0) {
                tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterPhotoVideo();
            } else if (i4 == 6) {
                tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterPhotos();
            } else if (i4 == 7) {
                tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterVideo();
            } else if (i4 == 1) {
                tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterDocument();
            } else if (i4 == 2) {
                tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterRoundVoice();
            } else if (i4 == 3) {
                tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterUrl();
            } else {
                if (i4 != 4) {
                    if (i4 == 5) {
                        tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterGif();
                    }
                    tL_messages_search.q = !TextUtils.isEmpty(str) ? str : "";
                    tL_messages_search.peer = getMessagesController().getInputPeer(j);
                    if (j2 != 0) {
                        if (j == getUserConfig().getClientUserId()) {
                            tL_messages_search.saved_peer_id = getMessagesController().getInputPeer(j2);
                            i8 = 4 | tL_messages_search.flags;
                        } else {
                            tL_messages_search.top_msg_id = (int) j2;
                            i8 = tL_messages_search.flags | 2;
                        }
                        tL_messages_search.flags = i8;
                    }
                    if (tL_messages_search.peer == null) {
                        return;
                    }
                    final boolean z2 = z;
                    getConnectionsManager().bindRequestToGuid(getConnectionsManager().sendRequest(tL_messages_search, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda164
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            MediaDataController.this.lambda$loadMedia$124(j, i3, i, i2, i4, j2, i6, z2, i7, tLObject, tL_error);
                        }
                    }), i6);
                    return;
                }
                tL_inputMessagesFilterGif = new TLRPC.TL_inputMessagesFilterMusic();
            }
            tL_messages_search.filter = tL_inputMessagesFilterGif;
            tL_messages_search.q = !TextUtils.isEmpty(str) ? str : "";
            tL_messages_search.peer = getMessagesController().getInputPeer(j);
            if (j2 != 0) {
            }
            if (tL_messages_search.peer == null) {
            }
        }
        z = false;
        if (BuildVars.LOGS_ENABLED) {
        }
        if (i5 == 0) {
        }
        tL_messages_search = new TLRPC.TL_messages_search();
        tL_messages_search.limit = i;
        if (i3 == 0) {
        }
        if (visibleReaction != null) {
        }
        if (i4 != 0) {
        }
        tL_messages_search.filter = tL_inputMessagesFilterGif;
        tL_messages_search.q = !TextUtils.isEmpty(str) ? str : "";
        tL_messages_search.peer = getMessagesController().getInputPeer(j);
        if (j2 != 0) {
        }
        if (tL_messages_search.peer == null) {
        }
    }

    public void loadMoreSearchMessages(boolean z) {
        if (this.loadingMoreSearchMessages || this.reqId != 0) {
            return;
        }
        boolean[] zArr = this.messagesSearchEndReached;
        if (zArr[0] && this.lastMergeDialogId == 0 && zArr[1]) {
            return;
        }
        int i = this.lastReturnedNum;
        this.lastReturnedNum = this.searchResultMessages.size();
        this.loadingMoreSearchMessages = true;
        searchMessagesInChat(null, this.lastDialogId, this.lastMergeDialogId, this.lastGuid, 1, this.lastReplyMessageId, false, this.lastSearchUser, this.lastSearchChat, false, this.lastReaction);
        this.lastReturnedNum = i;
    }

    public void loadMusic(final long j, final long j2, final long j3) {
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadMusic$140(j, j2, j3);
            }
        });
    }

    public ArrayList<MessageObject> loadPinnedMessages(final long j, final long j2, final ArrayList<Integer> arrayList, boolean z) {
        if (!z) {
            return loadPinnedMessageInternal(j, j2, arrayList, true);
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda56
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$loadPinnedMessages$163(j, j2, arrayList);
            }
        });
        return null;
    }

    public void loadPinnedMessages(final long j, final int i, final int i2) {
        if (this.loadingPinnedMessages.indexOfKey(j) >= 0) {
            return;
        }
        this.loadingPinnedMessages.put(j, Boolean.TRUE);
        final TLRPC.TL_messages_search tL_messages_search = new TLRPC.TL_messages_search();
        tL_messages_search.peer = getMessagesController().getInputPeer(j);
        tL_messages_search.limit = 40;
        tL_messages_search.offset_id = i;
        tL_messages_search.q = "";
        tL_messages_search.filter = new TLRPC.TL_inputMessagesFilterPinned();
        getConnectionsManager().sendRequest(tL_messages_search, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda63
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$loadPinnedMessages$162(i2, tL_messages_search, j, i, tLObject, tL_error);
            }
        });
    }

    public void loadPremiumPromo(boolean z) {
        this.isLoadingPremiumPromo = true;
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda216
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadPremiumPromo$7();
                }
            });
        } else {
            getConnectionsManager().sendRequest(new TLRPC.TL_help_getPremiumPromo(), new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda217
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadPremiumPromo$8(tLObject, tL_error);
                }
            });
        }
    }

    public void loadReactions(boolean z, Integer num) {
        this.isLoadingReactions = true;
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda41
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadReactions$12();
                }
            });
            return;
        }
        TLRPC.TL_messages_getAvailableReactions tL_messages_getAvailableReactions = new TLRPC.TL_messages_getAvailableReactions();
        tL_messages_getAvailableReactions.hash = num != null ? num.intValue() : this.reactionsUpdateHash;
        getConnectionsManager().sendRequest(tL_messages_getAvailableReactions, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda42
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$loadReactions$14(tLObject, tL_error);
            }
        });
    }

    public void loadRecentAndTopReactions(boolean z) {
        if (this.loadingRecentReactions) {
            return;
        }
        if (!this.loadedRecentReactions || z) {
            final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("recent_reactions_" + this.currentAccount, 0);
            final SharedPreferences sharedPreferences2 = ApplicationLoader.applicationContext.getSharedPreferences("top_reactions_" + this.currentAccount, 0);
            this.recentReactions.clear();
            this.topReactions.clear();
            this.recentReactions.addAll(loadReactionsFromPref(sharedPreferences));
            this.topReactions.addAll(loadReactionsFromPref(sharedPreferences2));
            this.loadingRecentReactions = true;
            this.loadedRecentReactions = true;
            final boolean[] zArr = new boolean[2];
            TLRPC.TL_messages_getRecentReactions tL_messages_getRecentReactions = new TLRPC.TL_messages_getRecentReactions();
            tL_messages_getRecentReactions.hash = sharedPreferences.getLong("hash", 0L);
            tL_messages_getRecentReactions.limit = 50;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getRecentReactions, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda61
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadRecentAndTopReactions$236(sharedPreferences, zArr, tLObject, tL_error);
                }
            });
            TLRPC.TL_messages_getTopReactions tL_messages_getTopReactions = new TLRPC.TL_messages_getTopReactions();
            tL_messages_getTopReactions.hash = sharedPreferences2.getLong("hash", 0L);
            tL_messages_getTopReactions.limit = 100;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getTopReactions, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda62
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadRecentAndTopReactions$238(sharedPreferences2, zArr, tLObject, tL_error);
                }
            });
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:55:0x001f, code lost:
    
        r10 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x001d, code lost:
    
        if (r7.recentStickersLoaded[r8] != false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x000d, code lost:
    
        if (r7.recentGifsLoaded != false) goto L15;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadRecents(final int i, final boolean z, boolean z2, boolean z3) {
        TLRPC.TL_messages_getStickers tL_messages_getStickers;
        ArrayList<TLRPC.Document> arrayList;
        TLRPC.TL_messages_getRecentStickers tL_messages_getRecentStickers;
        RequestDelegate requestDelegate;
        ConnectionsManager connectionsManager;
        TLRPC.TL_messages_getRecentStickers tL_messages_getRecentStickers2;
        if (!z) {
            boolean[] zArr = this.loadingRecentStickers;
            if (zArr[i]) {
                return;
            } else {
                zArr[i] = true;
            }
        } else if (this.loadingRecentGifs) {
            return;
        } else {
            this.loadingRecentGifs = true;
        }
        if (z2) {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda213
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadRecents$49(z, i);
                }
            });
            return;
        }
        SharedPreferences emojiSettings = MessagesController.getEmojiSettings(this.currentAccount);
        if (!z3) {
            if (Math.abs(System.currentTimeMillis() - emojiSettings.getLong(z ? "lastGifLoadTime" : i == 0 ? "lastStickersLoadTime" : i == 1 ? "lastStickersLoadTimeMask" : i == 3 ? "lastStickersLoadTimeGreet" : i == 5 ? "lastStickersLoadTimeEmojiPacks" : i == 7 ? "lastStickersLoadTimePremiumStickers" : "lastStickersLoadTimeFavs", 0L)) < 3600000) {
                if (z) {
                    this.loadingRecentGifs = false;
                    return;
                } else {
                    this.loadingRecentStickers[i] = false;
                    return;
                }
            }
        }
        if (z) {
            TLRPC.TL_messages_getSavedGifs tL_messages_getSavedGifs = new TLRPC.TL_messages_getSavedGifs();
            tL_messages_getSavedGifs.hash = calcDocumentsHash(this.recentGifs);
            ConnectionsManager connectionsManager2 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda214
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadRecents$50(i, tLObject, tL_error);
                }
            };
            tL_messages_getRecentStickers2 = tL_messages_getSavedGifs;
            connectionsManager = connectionsManager2;
        } else {
            if (i == 2) {
                TLRPC.TL_messages_getFavedStickers tL_messages_getFavedStickers = new TLRPC.TL_messages_getFavedStickers();
                tL_messages_getFavedStickers.hash = calcDocumentsHash(this.recentStickers[i]);
                tL_messages_getRecentStickers = tL_messages_getFavedStickers;
            } else {
                if (i == 3) {
                    tL_messages_getStickers = new TLRPC.TL_messages_getStickers();
                    tL_messages_getStickers.emoticon = "👋" + Emoji.fixEmoji("⭐");
                    arrayList = this.recentStickers[i];
                } else if (i == 7) {
                    tL_messages_getStickers = new TLRPC.TL_messages_getStickers();
                    tL_messages_getStickers.emoticon = "📂" + Emoji.fixEmoji("⭐");
                    arrayList = this.recentStickers[i];
                } else {
                    TLRPC.TL_messages_getRecentStickers tL_messages_getRecentStickers3 = new TLRPC.TL_messages_getRecentStickers();
                    tL_messages_getRecentStickers3.hash = calcDocumentsHash(this.recentStickers[i]);
                    tL_messages_getRecentStickers3.attached = i == 1;
                    tL_messages_getRecentStickers = tL_messages_getRecentStickers3;
                }
                tL_messages_getStickers.hash = calcDocumentsHash(arrayList);
                tL_messages_getRecentStickers = tL_messages_getStickers;
            }
            ConnectionsManager connectionsManager3 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda215
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadRecents$51(i, tLObject, tL_error);
                }
            };
            tL_messages_getRecentStickers2 = tL_messages_getRecentStickers;
            connectionsManager = connectionsManager3;
        }
        connectionsManager.sendRequest(tL_messages_getRecentStickers2, requestDelegate);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0066  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadReplyIcons() {
        Throwable th;
        TLRPC.TL_emojiList tL_emojiList;
        final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("replyicons_" + this.currentAccount, 0);
        TLRPC.TL_emojiList tL_emojiList2 = null;
        String string = sharedPreferences.getString("replyicons", null);
        long j = sharedPreferences.getLong("replyicons_last_check", 0L);
        if (string != null) {
            SerializedData serializedData = new SerializedData(Utilities.hexToBytes(string));
            try {
                tL_emojiList = (TLRPC.TL_emojiList) TLRPC.EmojiList.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                try {
                    this.replyIconsDefault = tL_emojiList;
                } catch (Throwable th2) {
                    th = th2;
                    FileLog.e(th);
                    tL_emojiList2 = tL_emojiList;
                    if (tL_emojiList2 != null) {
                    }
                    TLRPC.TL_account_getDefaultBackgroundEmojis tL_account_getDefaultBackgroundEmojis = new TLRPC.TL_account_getDefaultBackgroundEmojis();
                    if (tL_emojiList2 != null) {
                    }
                    getConnectionsManager().sendRequest(tL_account_getDefaultBackgroundEmojis, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda51
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            MediaDataController.this.lambda$loadReplyIcons$244(sharedPreferences, tLObject, tL_error);
                        }
                    });
                }
            } catch (Throwable th3) {
                th = th3;
                tL_emojiList = null;
            }
            tL_emojiList2 = tL_emojiList;
        }
        if (tL_emojiList2 != null || System.currentTimeMillis() - j > 86400000 || BuildVars.DEBUG_PRIVATE_VERSION) {
            TLRPC.TL_account_getDefaultBackgroundEmojis tL_account_getDefaultBackgroundEmojis2 = new TLRPC.TL_account_getDefaultBackgroundEmojis();
            if (tL_emojiList2 != null) {
                tL_account_getDefaultBackgroundEmojis2.hash = tL_emojiList2.hash;
            }
            getConnectionsManager().sendRequest(tL_account_getDefaultBackgroundEmojis2, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda51
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadReplyIcons$244(sharedPreferences, tLObject, tL_error);
                }
            });
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: IfRegionVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r1v29 long, still in use, count: 2, list:
          (r1v29 long) from 0x0214: PHI (r1v30 long) = (r1v29 long), (r1v35 long), (r1v36 long) binds: [B:194:0x0210, B:160:0x0213, B:159:0x0207] A[DONT_GENERATE, DONT_INLINE]
          (r1v29 long) from 0x020e: CMP_L (r1v29 long), (0 long) A[WRAPPED]
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:162)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:127)
        	at jadx.core.dex.visitors.regions.TernaryMod.makeTernaryInsn(TernaryMod.java:125)
        	at jadx.core.dex.visitors.regions.TernaryMod.processRegion(TernaryMod.java:62)
        	at jadx.core.dex.visitors.regions.TernaryMod.enterRegion(TernaryMod.java:45)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:67)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1085)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1085)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1085)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1085)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1085)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1085)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:19)
        	at jadx.core.dex.visitors.regions.TernaryMod.process(TernaryMod.java:35)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.process(IfRegionVisitor.java:34)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.visit(IfRegionVisitor.java:30)
        */
    /* JADX WARN: Removed duplicated region for block: B:112:0x034a  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0369  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x03d3 A[ADDED_TO_REGION, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadReplyMessagesForMessages(java.util.ArrayList<org.telegram.messenger.MessageObject> r21, final long r22, int r24, long r25, final java.lang.Runnable r27, final int r28, final org.telegram.messenger.Timer r29) {
        /*
            Method dump skipped, instructions count: 1056
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MediaDataController.loadReplyMessagesForMessages(java.util.ArrayList, long, int, long, java.lang.Runnable, int, org.telegram.messenger.Timer):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadRestrictedStatusEmojis() {
        Throwable th;
        TLRPC.TL_emojiList tL_emojiList;
        final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("restrictedstatuses_" + this.currentAccount, 0);
        TLRPC.TL_emojiList tL_emojiList2 = null;
        String string = sharedPreferences.getString("restrictedstatuses", null);
        long j = sharedPreferences.getLong("restrictedstatuses_last_check", 0L);
        if (string != null) {
            SerializedData serializedData = new SerializedData(Utilities.hexToBytes(string));
            try {
                tL_emojiList = (TLRPC.TL_emojiList) TLRPC.EmojiList.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                try {
                    this.restrictedStatusEmojis = tL_emojiList;
                } catch (Throwable th2) {
                    th = th2;
                    FileLog.e(th);
                    tL_emojiList2 = tL_emojiList;
                    if (tL_emojiList2 != null) {
                    }
                    TLRPC.TL_account_getChannelRestrictedStatusEmojis tL_account_getChannelRestrictedStatusEmojis = new TLRPC.TL_account_getChannelRestrictedStatusEmojis();
                    if (tL_emojiList2 != null) {
                    }
                    getConnectionsManager().sendRequest(tL_account_getChannelRestrictedStatusEmojis, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda115
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            MediaDataController.this.lambda$loadRestrictedStatusEmojis$246(sharedPreferences, tLObject, tL_error);
                        }
                    });
                }
            } catch (Throwable th3) {
                th = th3;
                tL_emojiList = null;
            }
            tL_emojiList2 = tL_emojiList;
        }
        if (tL_emojiList2 != null || System.currentTimeMillis() - j > 86400000) {
            TLRPC.TL_account_getChannelRestrictedStatusEmojis tL_account_getChannelRestrictedStatusEmojis2 = new TLRPC.TL_account_getChannelRestrictedStatusEmojis();
            if (tL_emojiList2 != null) {
                tL_account_getChannelRestrictedStatusEmojis2.hash = tL_emojiList2.hash;
            }
            getConnectionsManager().sendRequest(tL_account_getChannelRestrictedStatusEmojis2, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda115
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadRestrictedStatusEmojis$246(sharedPreferences, tLObject, tL_error);
                }
            });
        }
    }

    public void loadSavedReactions(boolean z) {
        if (this.loadingSavedReactions) {
            return;
        }
        if (!this.loadedSavedReactions || z) {
            final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("saved_reactions_" + this.currentAccount, 0);
            this.savedReactions.clear();
            this.savedReactions.addAll(loadReactionsFromPref(sharedPreferences));
            this.loadingSavedReactions = true;
            this.loadedSavedReactions = true;
            TLRPC.TL_messages_getDefaultTagReactions tL_messages_getDefaultTagReactions = new TLRPC.TL_messages_getDefaultTagReactions();
            tL_messages_getDefaultTagReactions.hash = sharedPreferences.getLong("hash", 0L);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getDefaultTagReactions, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda146
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadSavedReactions$240(sharedPreferences, tLObject, tL_error);
                }
            });
        }
    }

    public void loadStickers(int i, boolean z, boolean z2) {
        loadStickers(i, z, z2, false, null);
    }

    public void loadStickers(int i, boolean z, boolean z2, boolean z3) {
        loadStickers(i, z, z2, z3, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void loadStickers(final int i, boolean z, final boolean z2, boolean z3, final Utilities.Callback<ArrayList<TLRPC.TL_messages_stickerSet>> callback) {
        long j;
        TLRPC.TL_messages_getMaskStickers tL_messages_getMaskStickers;
        RequestDelegate requestDelegate;
        ConnectionsManager connectionsManager;
        TLRPC.TL_messages_getMaskStickers tL_messages_getMaskStickers2;
        if (this.loadingStickers[i]) {
            if (z3) {
                this.scheduledLoadStickers[i] = new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda195
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$loadStickers$89(i, z2, callback);
                    }
                };
                return;
            } else {
                if (callback != null) {
                    callback.run(null);
                    return;
                }
                return;
            }
        }
        if (i == 3) {
            if (this.featuredStickerSets[0].isEmpty() || !getMessagesController().preloadFeaturedStickers) {
                if (callback != null) {
                    callback.run(null);
                    return;
                }
                return;
            }
        } else if (i == 6) {
            if (this.featuredStickerSets[1].isEmpty() || !getMessagesController().preloadFeaturedStickers) {
                if (callback != null) {
                    callback.run(null);
                    return;
                }
                return;
            }
        } else if (i != 4) {
            loadArchivedStickersCount(i, z);
        }
        this.loadingStickers[i] = true;
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda196
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$loadStickers$91(i, callback);
                }
            });
            return;
        }
        if (i == 3 || i == 6) {
            char c = i != 6 ? (char) 0 : (char) 1;
            TLRPC.TL_messages_allStickers tL_messages_allStickers = new TLRPC.TL_messages_allStickers();
            tL_messages_allStickers.hash2 = this.loadFeaturedHash[c];
            int size = this.featuredStickerSets[c].size();
            for (int i2 = 0; i2 < size; i2++) {
                tL_messages_allStickers.sets.add(this.featuredStickerSets[c].get(i2).set);
            }
            processLoadStickersResponse(i, tL_messages_allStickers, new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda199
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.lambda$loadStickers$92(Utilities.Callback.this);
                }
            });
            return;
        }
        if (i == 4) {
            TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
            tL_messages_getStickerSet.stickerset = new TLRPC.TL_inputStickerSetAnimatedEmoji();
            ConnectionsManager connectionsManager2 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda197
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadStickers$95(i, callback, tLObject, tL_error);
                }
            };
            tL_messages_getMaskStickers2 = tL_messages_getStickerSet;
            connectionsManager = connectionsManager2;
        } else {
            if (i == 0) {
                TLRPC.TL_messages_getAllStickers tL_messages_getAllStickers = new TLRPC.TL_messages_getAllStickers();
                j = z2 ? 0L : this.loadHash[i];
                tL_messages_getAllStickers.hash = j;
                tL_messages_getMaskStickers = tL_messages_getAllStickers;
            } else if (i == 5) {
                TLRPC.TL_messages_getEmojiStickers tL_messages_getEmojiStickers = new TLRPC.TL_messages_getEmojiStickers();
                j = z2 ? 0L : this.loadHash[i];
                tL_messages_getEmojiStickers.hash = j;
                tL_messages_getMaskStickers = tL_messages_getEmojiStickers;
            } else {
                TLRPC.TL_messages_getMaskStickers tL_messages_getMaskStickers3 = new TLRPC.TL_messages_getMaskStickers();
                j = z2 ? 0L : this.loadHash[i];
                tL_messages_getMaskStickers3.hash = j;
                tL_messages_getMaskStickers = tL_messages_getMaskStickers3;
            }
            final long j2 = j;
            ConnectionsManager connectionsManager3 = getConnectionsManager();
            requestDelegate = new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda198
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadStickers$99(i, callback, j2, tLObject, tL_error);
                }
            };
            tL_messages_getMaskStickers2 = tL_messages_getMaskStickers;
            connectionsManager = connectionsManager3;
        }
        connectionsManager.sendRequest(tL_messages_getMaskStickers2, requestDelegate);
    }

    public void loadStickersByEmojiOrName(final String str, final boolean z, boolean z2) {
        TLRPC.InputStickerSet inputStickerSet;
        if (this.loadingDiceStickerSets.contains(str)) {
            return;
        }
        if (!z || this.diceStickerSetsByEmoji.get(str) == null) {
            this.loadingDiceStickerSets.add(str);
            if (z2) {
                getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda210
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$loadStickersByEmojiOrName$81(str, z);
                    }
                });
                return;
            }
            TLRPC.TL_messages_getStickerSet tL_messages_getStickerSet = new TLRPC.TL_messages_getStickerSet();
            if (Objects.equals(getUserConfig().premiumGiftsStickerPack, str)) {
                inputStickerSet = new TLRPC.TL_inputStickerSetPremiumGifts();
            } else if (z) {
                TLRPC.TL_inputStickerSetDice tL_inputStickerSetDice = new TLRPC.TL_inputStickerSetDice();
                tL_inputStickerSetDice.emoticon = str;
                inputStickerSet = tL_inputStickerSetDice;
            } else {
                TLRPC.InputStickerSet tL_inputStickerSetShortName = new TLRPC.TL_inputStickerSetShortName();
                tL_inputStickerSetShortName.short_name = str;
                inputStickerSet = tL_inputStickerSetShortName;
            }
            tL_messages_getStickerSet.stickerset = inputStickerSet;
            getConnectionsManager().sendRequest(tL_messages_getStickerSet, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda211
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$loadStickersByEmojiOrName$83(str, z, tLObject, tL_error);
                }
            });
        }
    }

    public void markFeaturedStickersAsRead(boolean z, boolean z2) {
        if (this.unreadStickerSets[z ? 1 : 0].isEmpty()) {
            return;
        }
        this.unreadStickerSets[z ? 1 : 0].clear();
        this.loadFeaturedHash[z ? 1 : 0] = calcFeaturedStickersHash(z, this.featuredStickerSets[z ? 1 : 0]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(z ? NotificationCenter.featuredEmojiDidLoad : NotificationCenter.featuredStickersDidLoad, new Object[0]);
        putFeaturedStickersToCache(z, this.featuredStickerSets[z ? 1 : 0], this.unreadStickerSets[z ? 1 : 0], this.loadFeaturedDate[z ? 1 : 0], this.loadFeaturedHash[z ? 1 : 0], this.loadFeaturedPremium);
        if (z2) {
            getConnectionsManager().sendRequest(new TLRPC.TL_messages_readFeaturedStickers(), new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda67
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.lambda$markFeaturedStickersAsRead$65(tLObject, tL_error);
                }
            });
        }
    }

    public void markFeaturedStickersByIdAsRead(final boolean z, final long j) {
        if (!this.unreadStickerSets[z ? 1 : 0].contains(Long.valueOf(j)) || this.readingStickerSets[z ? 1 : 0].contains(Long.valueOf(j))) {
            return;
        }
        this.readingStickerSets[z ? 1 : 0].add(Long.valueOf(j));
        TLRPC.TL_messages_readFeaturedStickers tL_messages_readFeaturedStickers = new TLRPC.TL_messages_readFeaturedStickers();
        tL_messages_readFeaturedStickers.id.add(Long.valueOf(j));
        getConnectionsManager().sendRequest(tL_messages_readFeaturedStickers, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda222
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.lambda$markFeaturedStickersByIdAsRead$66(tLObject, tL_error);
            }
        });
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda223
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$markFeaturedStickersByIdAsRead$67(z, j);
            }
        }, 1000L);
    }

    public void markSetInstalling(long j, boolean z) {
        this.uninstalledForceStickerSetsById.remove(Long.valueOf(j));
        if (z && !this.installedForceStickerSetsById.contains(Long.valueOf(j))) {
            this.installedForceStickerSetsById.add(Long.valueOf(j));
        }
        if (z) {
            return;
        }
        this.installedForceStickerSetsById.remove(Long.valueOf(j));
    }

    public void markSetUninstalling(long j, boolean z) {
        this.installedForceStickerSetsById.remove(Long.valueOf(j));
        if (z && !this.uninstalledForceStickerSetsById.contains(Long.valueOf(j))) {
            this.uninstalledForceStickerSetsById.add(Long.valueOf(j));
        }
        if (z) {
            return;
        }
        this.uninstalledForceStickerSetsById.remove(Long.valueOf(j));
    }

    public void moveStickerSetToTop(long j, boolean z, boolean z2) {
        int i = z ? 5 : z2 ? 1 : 0;
        ArrayList<TLRPC.TL_messages_stickerSet> stickerSets = getStickerSets(i);
        if (stickerSets != null) {
            for (int i2 = 0; i2 < stickerSets.size(); i2++) {
                if (stickerSets.get(i2).set.id == j) {
                    TLRPC.TL_messages_stickerSet tL_messages_stickerSet = stickerSets.get(i2);
                    stickerSets.remove(i2);
                    stickerSets.add(0, tL_messages_stickerSet);
                    getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(i), Boolean.FALSE);
                    return;
                }
            }
        }
    }

    public void onRingtoneUploaded(String str, TLRPC.Document document, boolean z) {
        this.ringtoneUploaderHashMap.remove(str);
        this.ringtoneDataStore.onRingtoneUploaded(str, document, z);
    }

    public void portSavedSearchResults(int i, ReactionsLayoutInBubble.VisibleReaction visibleReaction, String str, ArrayList<MessageObject> arrayList, ArrayList<MessageObject> arrayList2, int i2, int i3, boolean z) {
        this.lastReaction = visibleReaction;
        this.lastSearchQuery = str;
        boolean[] zArr = this.messagesSearchEndReached;
        zArr[0] = z;
        zArr[1] = true;
        this.searchServerResultMessages.clear();
        this.searchServerResultMessages.addAll(arrayList2);
        this.searchLocalResultMessages.clear();
        this.searchLocalResultMessages.addAll(arrayList);
        updateSearchResults();
        int[] iArr = this.messagesSearchCount;
        iArr[0] = i3;
        iArr[1] = 0;
        this.lastReturnedNum = i2;
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.chatSearchResultsAvailable, Integer.valueOf(i), 0, Integer.valueOf(getMask()), Long.valueOf(getUserConfig().getClientUserId()), Integer.valueOf(this.lastReturnedNum), Integer.valueOf(getSearchCount()), Boolean.TRUE);
    }

    public void preloadDefaultReactions() {
        if (this.reactionsList == null || this.reactionsCacheGenerated || !LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS) || this.currentAccount != UserConfig.selectedAccount) {
            return;
        }
        this.reactionsCacheGenerated = true;
        ArrayList arrayList = new ArrayList(this.reactionsList);
        int min = Math.min(arrayList.size(), 10);
        for (int i = 0; i < min; i++) {
            TLRPC.TL_availableReaction tL_availableReaction = (TLRPC.TL_availableReaction) arrayList.get(i);
            preloadImage(ImageLocation.getForDocument(tL_availableReaction.activate_animation), 0);
            preloadImage(ImageLocation.getForDocument(tL_availableReaction.appear_animation), 0);
        }
        for (int i2 = 0; i2 < min; i2++) {
            preloadImage(ImageLocation.getForDocument(((TLRPC.TL_availableReaction) arrayList.get(i2)).effect_animation), 0);
        }
    }

    public void preloadImage(ImageLocation imageLocation, int i) {
        getFileLoader().loadFile(imageLocation, null, null, i, 11);
    }

    public void preloadImage(ImageReceiver imageReceiver, ImageLocation imageLocation, String str) {
        if (LiteMode.isEnabled(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS)) {
            imageReceiver.setUniqKeyPrefix("preload");
            imageReceiver.setFileLoadingPriority(0);
            imageReceiver.setImage(imageLocation, str, null, null, 0, 11);
        }
    }

    public void preloadPremiumPreviewStickers() {
        if (!this.previewStickersLoading && this.premiumPreviewStickers.isEmpty()) {
            TLRPC.TL_messages_getStickers tL_messages_getStickers = new TLRPC.TL_messages_getStickers();
            tL_messages_getStickers.emoticon = Emoji.fixEmoji("⭐") + Emoji.fixEmoji("⭐");
            tL_messages_getStickers.hash = 0L;
            this.previewStickersLoading = true;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getStickers, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda27
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    MediaDataController.this.lambda$preloadPremiumPreviewStickers$206(tLObject, tL_error);
                }
            });
            return;
        }
        int i = 0;
        while (i < Math.min(this.premiumPreviewStickers.size(), 3)) {
            ArrayList<TLRPC.Document> arrayList = this.premiumPreviewStickers;
            TLRPC.Document document = arrayList.get(i == 2 ? arrayList.size() - 1 : i);
            if (MessageObject.isPremiumSticker(document)) {
                ImageReceiver imageReceiver = new ImageReceiver();
                imageReceiver.setAllowLoadingOnAttachedOnly(false);
                imageReceiver.setImage(ImageLocation.getForDocument(document), null, null, "webp", null, 1);
                ImageLoader.getInstance().loadImageForImageReceiver(imageReceiver);
                ImageReceiver imageReceiver2 = new ImageReceiver();
                imageReceiver2.setAllowLoadingOnAttachedOnly(false);
                imageReceiver2.setImage(ImageLocation.getForDocument(MessageObject.getPremiumStickerAnimation(document), document), (String) null, (ImageLocation) null, (String) null, "tgs", (Object) null, 1);
                ImageLoader.getInstance().loadImageForImageReceiver(imageReceiver2);
            }
            i++;
        }
    }

    public void preloadStickerSetThumb(TLRPC.StickerSetCovered stickerSetCovered) {
        TLRPC.StickerSet stickerSet;
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        if (stickerSetCovered == null || (stickerSet = stickerSetCovered.set) == null || (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(stickerSet.thumbs, 90)) == null) {
            return;
        }
        TLRPC.Document document = stickerSetCovered.cover;
        if (document == null) {
            if (stickerSetCovered.covers.isEmpty()) {
                return;
            } else {
                document = stickerSetCovered.covers.get(0);
            }
        }
        loadStickerSetThumbInternal(closestPhotoSizeWithSize, stickerSetCovered, document, stickerSetCovered.set.thumb_version);
    }

    public void preloadStickerSetThumb(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        TLRPC.StickerSet stickerSet;
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        ArrayList<TLRPC.Document> arrayList;
        if (tL_messages_stickerSet == null || (stickerSet = tL_messages_stickerSet.set) == null || (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(stickerSet.thumbs, 90)) == null || (arrayList = tL_messages_stickerSet.documents) == null || arrayList.isEmpty()) {
            return;
        }
        loadStickerSetThumbInternal(closestPhotoSizeWithSize, tL_messages_stickerSet, arrayList.get(0), tL_messages_stickerSet.set.thumb_version);
    }

    public boolean processDeletedMessage(int i, long[] jArr) {
        MessageObject messageObject;
        boolean z = false;
        int i2 = 0;
        while (true) {
            if (i2 >= this.deletedFromResultMessages.size()) {
                messageObject = null;
                break;
            }
            if (this.deletedFromResultMessages.get(i2).getId() == i) {
                messageObject = this.deletedFromResultMessages.get(i2);
                break;
            }
            i2++;
        }
        if (messageObject != null && messageObject.getDialogId() == getUserConfig().getClientUserId()) {
            boolean processDeletedReactionTags = getMessagesController().processDeletedReactionTags(messageObject.messageOwner);
            jArr[0] = MessageObject.getSavedDialogId(getUserConfig().getClientUserId(), messageObject.messageOwner);
            z = processDeletedReactionTags;
        }
        this.deletedFromResultMessages.remove(messageObject);
        return z;
    }

    public void processLoadedMenuBots(TLRPC.TL_attachMenuBots tL_attachMenuBots, long j, int i, boolean z) {
        boolean z2;
        if (tL_attachMenuBots != null && i != 0) {
            this.attachMenuBots = tL_attachMenuBots;
            this.menuBotsUpdateHash = j;
        }
        SharedPreferences.Editor edit = getMessagesController().getMainSettings().edit();
        this.menuBotsUpdateDate = i;
        edit.putInt("menuBotsUpdateDate", i).commit();
        this.menuBotsUpdatedLocal = true;
        if (tL_attachMenuBots != null) {
            if (!z) {
                getMessagesStorage().putUsersAndChats(tL_attachMenuBots.users, null, true, true);
            }
            getMessagesController().putUsers(tL_attachMenuBots.users, z);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda147
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedMenuBots$5();
                }
            });
            z2 = false;
            for (int i2 = 0; i2 < tL_attachMenuBots.bots.size(); i2++) {
                if (tL_attachMenuBots.bots.get(i2) instanceof TLRPC.TL_attachMenuBot_layer162) {
                    tL_attachMenuBots.bots.get(i2).show_in_attach_menu = true;
                    z2 = true;
                }
            }
        } else {
            z2 = false;
        }
        if (!z) {
            putMenuBotsToCache(tL_attachMenuBots, j, i);
        } else if (z2 || Math.abs((System.currentTimeMillis() / 1000) - i) >= 3600) {
            loadAttachMenuBots(false, true);
        }
    }

    public void processLoadedPremiumPromo(TLRPC.TL_help_premiumPromo tL_help_premiumPromo, int i, boolean z) {
        if (tL_help_premiumPromo != null) {
            this.premiumPromo = tL_help_premiumPromo;
            this.premiumPromoUpdateDate = i;
            getMessagesController().putUsers(tL_help_premiumPromo.users, z);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda234
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedPremiumPromo$9();
                }
            });
        }
        if (z) {
            if (tL_help_premiumPromo == null || Math.abs((System.currentTimeMillis() / 1000) - i) >= 86400) {
                loadPremiumPromo(false);
                return;
            }
        } else if (tL_help_premiumPromo != null) {
            putPremiumPromoToCache(tL_help_premiumPromo, i);
        }
        this.isLoadingPremiumPromo = false;
    }

    public void processLoadedReactions(List<TLRPC.TL_availableReaction> list, int i, int i2, boolean z) {
        if (list != null && i2 != 0) {
            this.reactionsList.clear();
            this.reactionsMap.clear();
            this.enabledReactionsList.clear();
            this.reactionsList.addAll(list);
            for (int i3 = 0; i3 < this.reactionsList.size(); i3++) {
                this.reactionsList.get(i3).positionInList = i3;
                this.reactionsMap.put(this.reactionsList.get(i3).reaction, this.reactionsList.get(i3));
                if (!this.reactionsList.get(i3).inactive) {
                    this.enabledReactionsList.add(this.reactionsList.get(i3));
                }
            }
            this.reactionsUpdateHash = i;
        }
        this.reactionsUpdateDate = i2;
        if (list != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda165
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedReactions$15();
                }
            });
        }
        this.isLoadingReactions = false;
        if (!z) {
            putReactionsToCache(list, i, i2);
        } else {
            Math.abs((System.currentTimeMillis() / 1000) - i2);
            loadReactions(false, Integer.valueOf(i));
        }
    }

    protected void processLoadedRecentDocuments(final int i, final ArrayList<TLRPC.Document> arrayList, final boolean z, final int i2, final boolean z2) {
        if (arrayList != null) {
            getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda90
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedRecentDocuments$52(z, i, arrayList, z2, i2);
                }
            });
        }
        if (i2 == 0) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda91
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$processLoadedRecentDocuments$53(z, i, arrayList);
                }
            });
        }
    }

    public void processStickerSetInstallResultArchive(BaseFragment baseFragment, boolean z, int i, TLRPC.TL_messages_stickerSetInstallResultArchive tL_messages_stickerSetInstallResultArchive) {
        int size = tL_messages_stickerSetInstallResultArchive.sets.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.installedStickerSetsById.remove(tL_messages_stickerSetInstallResultArchive.sets.get(i2).set.id);
        }
        loadArchivedStickersCount(i, false);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.needAddArchivedStickers, tL_messages_stickerSetInstallResultArchive.sets);
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        baseFragment.showDialog(new StickersArchiveAlert(baseFragment.getParentActivity(), z ? baseFragment : null, tL_messages_stickerSetInstallResultArchive.sets).create());
    }

    public void pushDraftVoiceMessage(long j, long j2, DraftVoice draftVoice) {
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("2voicedrafts_" + this.currentAccount, 0);
        long hash = Objects.hash(Long.valueOf(j), Long.valueOf(j2));
        String str = hash + "";
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (draftVoice == null) {
            edit.remove(str).apply();
            this.draftVoices.remove(hash);
        } else {
            edit.putString(str, draftVoice.toString()).apply();
            this.draftVoices.put(hash, draftVoice);
        }
    }

    public void pushRecentEmojiStatus(TLRPC.EmojiStatus emojiStatus) {
        if (this.emojiStatuses[0] != null) {
            if (emojiStatus instanceof TLRPC.TL_emojiStatus) {
                long j = ((TLRPC.TL_emojiStatus) emojiStatus).document_id;
                int i = 0;
                while (i < this.emojiStatuses[0].size()) {
                    if ((this.emojiStatuses[0].get(i) instanceof TLRPC.TL_emojiStatus) && ((TLRPC.TL_emojiStatus) this.emojiStatuses[0].get(i)).document_id == j) {
                        this.emojiStatuses[0].remove(i);
                        i--;
                    }
                    i++;
                }
            }
            this.emojiStatuses[0].add(0, emojiStatus);
            while (this.emojiStatuses[0].size() > 50) {
                this.emojiStatuses[0].remove(r8.size() - 1);
            }
            TLRPC.TL_account_emojiStatuses tL_account_emojiStatuses = new TLRPC.TL_account_emojiStatuses();
            tL_account_emojiStatuses.hash = this.emojiStatusesHash[0];
            tL_account_emojiStatuses.statuses = this.emojiStatuses[0];
            updateEmojiStatuses(0, tL_account_emojiStatuses);
        }
    }

    public void putBotInfo(final long j, final TL_bots.BotInfo botInfo) {
        if (botInfo == null) {
            return;
        }
        this.botInfos.put(botInfo.user_id + "_" + j, botInfo);
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda181
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$putBotInfo$201(botInfo, j);
            }
        });
    }

    public void putBotKeyboard(final MessagesStorage.TopicKey topicKey, final TLRPC.Message message) {
        SQLiteDatabase database;
        String str;
        if (topicKey == null) {
            return;
        }
        try {
            SQLiteCursor queryFinalized = topicKey.topicId != 0 ? getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT mid FROM bot_keyboard_topics WHERE uid = %d AND tid = %d", Long.valueOf(topicKey.dialogId), Long.valueOf(topicKey.topicId)), new Object[0]) : getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT mid FROM bot_keyboard WHERE uid = %d", Long.valueOf(topicKey.dialogId)), new Object[0]);
            int intValue = queryFinalized.next() ? queryFinalized.intValue(0) : 0;
            queryFinalized.dispose();
            if (intValue >= message.id) {
                return;
            }
            if (topicKey.topicId != 0) {
                database = getMessagesStorage().getDatabase();
                str = "REPLACE INTO bot_keyboard_topics VALUES(?, ?, ?, ?)";
            } else {
                database = getMessagesStorage().getDatabase();
                str = "REPLACE INTO bot_keyboard VALUES(?, ?, ?)";
            }
            SQLitePreparedStatement executeFast = database.executeFast(str);
            executeFast.requery();
            MessageObject.normalizeFlags(message);
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(message.getObjectSize());
            message.serializeToStream(nativeByteBuffer);
            if (topicKey.topicId != 0) {
                executeFast.bindLong(1, topicKey.dialogId);
                executeFast.bindLong(2, topicKey.topicId);
                executeFast.bindInteger(3, message.id);
                executeFast.bindByteBuffer(4, nativeByteBuffer);
            } else {
                executeFast.bindLong(1, topicKey.dialogId);
                executeFast.bindInteger(2, message.id);
                executeFast.bindByteBuffer(3, nativeByteBuffer);
            }
            executeFast.step();
            nativeByteBuffer.reuse();
            executeFast.dispose();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda116
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$putBotKeyboard$200(topicKey, message);
                }
            });
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void putGroupStickerSet(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        this.groupStickerSets.put(tL_messages_stickerSet.set.id, tL_messages_stickerSet);
    }

    public void putStickerSet(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        putStickerSet(tL_messages_stickerSet, true);
    }

    public void putStickerSet(TLRPC.TL_messages_stickerSet tL_messages_stickerSet, boolean z) {
        TLRPC.StickerSet stickerSet;
        TLRPC.StickerSet stickerSet2;
        if (tL_messages_stickerSet == null || (stickerSet = tL_messages_stickerSet.set) == null) {
            return;
        }
        this.stickerSetsById.put(stickerSet.id, tL_messages_stickerSet);
        if (!TextUtils.isEmpty(tL_messages_stickerSet.set.short_name)) {
            this.stickerSetsByName.put(tL_messages_stickerSet.set.short_name.toLowerCase(), tL_messages_stickerSet);
        }
        int i = 0;
        while (true) {
            ArrayList<TLRPC.TL_messages_stickerSet>[] arrayListArr = this.stickerSets;
            if (i >= arrayListArr.length) {
                break;
            }
            ArrayList<TLRPC.TL_messages_stickerSet> arrayList = arrayListArr[i];
            if (arrayList != null) {
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    TLRPC.TL_messages_stickerSet tL_messages_stickerSet2 = arrayList.get(i2);
                    if (tL_messages_stickerSet2 != null && (stickerSet2 = tL_messages_stickerSet2.set) != null && stickerSet2.id == tL_messages_stickerSet.set.id) {
                        arrayList.set(i2, tL_messages_stickerSet);
                    }
                }
            }
            i++;
        }
        if (this.groupStickerSets.containsKey(tL_messages_stickerSet.set.id)) {
            this.groupStickerSets.put(tL_messages_stickerSet.set.id, tL_messages_stickerSet);
        }
        saveStickerSetIntoCache(tL_messages_stickerSet);
        TLRPC.StickerSet stickerSet3 = tL_messages_stickerSet.set;
        int i3 = stickerSet3.masks ? 1 : stickerSet3.emojis ? 5 : 0;
        if (z) {
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(i3), Boolean.TRUE);
        }
    }

    public void removeInline(long j) {
        for (int i = 0; i < this.inlineBots.size(); i++) {
            if (this.inlineBots.get(i).peer.user_id == j) {
                this.inlineBots.remove(i);
                TLRPC.TL_contacts_resetTopPeerRating tL_contacts_resetTopPeerRating = new TLRPC.TL_contacts_resetTopPeerRating();
                tL_contacts_resetTopPeerRating.category = new TLRPC.TL_topPeerCategoryBotsInline();
                tL_contacts_resetTopPeerRating.peer = getMessagesController().getInputPeer(j);
                getConnectionsManager().sendRequest(tL_contacts_resetTopPeerRating, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda163
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        MediaDataController.lambda$removeInline$152(tLObject, tL_error);
                    }
                });
                deletePeer(j, 1);
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadInlineHints, new Object[0]);
                return;
            }
        }
    }

    public void removeMessageFromResults(int i) {
        int i2 = 0;
        int i3 = 0;
        while (i3 < this.searchResultMessages.size()) {
            if (i == this.searchResultMessages.get(i3).getId()) {
                this.deletedFromResultMessages.add(this.searchResultMessages.remove(i3));
                i3--;
            }
            i3++;
        }
        int i4 = 0;
        while (i4 < this.searchServerResultMessages.size()) {
            if (i == this.searchServerResultMessages.get(i4).getId()) {
                this.searchServerResultMessages.remove(i4);
                i4--;
            }
            i4++;
        }
        while (i2 < this.searchLocalResultMessages.size()) {
            if (i == this.searchLocalResultMessages.get(i2).getId()) {
                this.searchLocalResultMessages.remove(i2);
                i2--;
            }
            i2++;
        }
    }

    public void removeMultipleStickerSets(final Context context, final BaseFragment baseFragment, final ArrayList<TLRPC.TL_messages_stickerSet> arrayList) {
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        if (arrayList == null || arrayList.isEmpty() || (tL_messages_stickerSet = arrayList.get(arrayList.size() - 1)) == null) {
            return;
        }
        TLRPC.StickerSet stickerSet = tL_messages_stickerSet.set;
        final int i = stickerSet.masks ? 1 : stickerSet.emojis ? 5 : 0;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList.get(i2).set.archived = false;
        }
        final int[] iArr = new int[arrayList.size()];
        for (int i3 = 0; i3 < this.stickerSets[i].size(); i3++) {
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet2 = this.stickerSets[i].get(i3);
            int i4 = 0;
            while (true) {
                if (i4 >= arrayList.size()) {
                    break;
                }
                if (tL_messages_stickerSet2.set.id == arrayList.get(i4).set.id) {
                    iArr[i4] = i3;
                    this.stickerSets[i].remove(i3);
                    this.stickerSetsById.remove(tL_messages_stickerSet2.set.id);
                    this.installedStickerSetsById.remove(tL_messages_stickerSet2.set.id);
                    this.stickerSetsByName.remove(tL_messages_stickerSet2.set.short_name);
                    break;
                }
                i4++;
            }
        }
        ArrayList<TLRPC.TL_messages_stickerSet> arrayList2 = this.stickerSets[i];
        int i5 = this.loadDate[i];
        long[] jArr = this.loadHash;
        long calcStickersHash = calcStickersHash(arrayList2);
        jArr[i] = calcStickersHash;
        putStickersToCache(i, arrayList2, i5, calcStickersHash);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(i), Boolean.TRUE);
        for (int i6 = 0; i6 < arrayList.size(); i6++) {
            markSetUninstalling(arrayList.get(i6).set.id, true);
        }
        StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(context, tL_messages_stickerSet, arrayList.size(), 0, null, baseFragment.getResourceProvider());
        final boolean[] zArr = new boolean[1];
        final int i7 = i;
        Bulletin.UndoButton delayedAction = new Bulletin.UndoButton(context, false).setUndoAction(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda167
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$removeMultipleStickerSets$108(zArr, arrayList, i7, iArr);
            }
        }).setDelayedAction(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda168
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$removeMultipleStickerSets$109(zArr, arrayList, context, baseFragment, i);
            }
        });
        stickerSetBulletinLayout.setButton(delayedAction);
        for (int i8 = 0; i8 < arrayList.size(); i8++) {
            LongSparseArray longSparseArray = this.removingStickerSetsUndos;
            long j = arrayList.get(i8).set.id;
            Objects.requireNonNull(delayedAction);
            longSparseArray.put(j, new MediaDataController$$ExternalSyntheticLambda15(delayedAction));
        }
        Bulletin.make(baseFragment, stickerSetBulletinLayout, 2750).show();
    }

    public void removePeer(long j) {
        for (int i = 0; i < this.hints.size(); i++) {
            if (this.hints.get(i).peer.user_id == j) {
                this.hints.remove(i);
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadHints, new Object[0]);
                TLRPC.TL_contacts_resetTopPeerRating tL_contacts_resetTopPeerRating = new TLRPC.TL_contacts_resetTopPeerRating();
                tL_contacts_resetTopPeerRating.category = new TLRPC.TL_topPeerCategoryCorrespondents();
                tL_contacts_resetTopPeerRating.peer = getMessagesController().getInputPeer(j);
                deletePeer(j, 0);
                getConnectionsManager().sendRequest(tL_contacts_resetTopPeerRating, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda34
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        MediaDataController.lambda$removePeer$154(tLObject, tL_error);
                    }
                });
                return;
            }
        }
    }

    public void removeRecentGif(final TLRPC.Document document) {
        int size = this.recentGifs.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            if (this.recentGifs.get(i).id == document.id) {
                this.recentGifs.remove(i);
                break;
            }
            i++;
        }
        final TLRPC.TL_messages_saveGif tL_messages_saveGif = new TLRPC.TL_messages_saveGif();
        TLRPC.TL_inputDocument tL_inputDocument = new TLRPC.TL_inputDocument();
        tL_messages_saveGif.id = tL_inputDocument;
        tL_inputDocument.id = document.id;
        tL_inputDocument.access_hash = document.access_hash;
        byte[] bArr = document.file_reference;
        tL_inputDocument.file_reference = bArr;
        if (bArr == null) {
            tL_inputDocument.file_reference = new byte[0];
        }
        tL_messages_saveGif.unsave = true;
        getConnectionsManager().sendRequest(tL_messages_saveGif, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda169
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$removeRecentGif$24(tL_messages_saveGif, tLObject, tL_error);
            }
        });
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda170
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$removeRecentGif$25(document);
            }
        });
    }

    public void removeWebapp(long j) {
        for (int i = 0; i < this.webapps.size(); i++) {
            if (this.webapps.get(i).peer.user_id == j) {
                this.webapps.remove(i);
                TLRPC.TL_contacts_resetTopPeerRating tL_contacts_resetTopPeerRating = new TLRPC.TL_contacts_resetTopPeerRating();
                tL_contacts_resetTopPeerRating.category = new TLRPC.TL_topPeerCategoryBotsApp();
                tL_contacts_resetTopPeerRating.peer = getMessagesController().getInputPeer(j);
                getConnectionsManager().sendRequest(tL_contacts_resetTopPeerRating, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda75
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        MediaDataController.lambda$removeWebapp$153(tLObject, tL_error);
                    }
                });
                deletePeer(j, 2);
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.reloadWebappsHints, new Object[0]);
                return;
            }
        }
    }

    public void reorderStickers(int i, final ArrayList<Long> arrayList, boolean z) {
        Collections.sort(this.stickerSets[i], new Comparator() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda229
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$reorderStickers$54;
                lambda$reorderStickers$54 = MediaDataController.lambda$reorderStickers$54(arrayList, (TLRPC.TL_messages_stickerSet) obj, (TLRPC.TL_messages_stickerSet) obj2);
                return lambda$reorderStickers$54;
            }
        });
        this.loadHash[i] = calcStickersHash(this.stickerSets[i]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(i), Boolean.valueOf(z));
    }

    public void replaceStickerSet(final TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        boolean z;
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet2 = (TLRPC.TL_messages_stickerSet) this.stickerSetsById.get(tL_messages_stickerSet.set.id);
        String str = (String) this.diceEmojiStickerSetsById.get(tL_messages_stickerSet.set.id);
        if (str != null) {
            this.diceStickerSetsByEmoji.put(str, tL_messages_stickerSet);
            putDiceStickersToCache(str, tL_messages_stickerSet, (int) (System.currentTimeMillis() / 1000));
        }
        if (tL_messages_stickerSet2 == null) {
            tL_messages_stickerSet2 = this.stickerSetsByName.get(tL_messages_stickerSet.set.short_name);
        }
        boolean z2 = tL_messages_stickerSet2 == null && (tL_messages_stickerSet2 = (TLRPC.TL_messages_stickerSet) this.groupStickerSets.get(tL_messages_stickerSet.set.id)) != null;
        if (tL_messages_stickerSet2 == null) {
            return;
        }
        if ("AnimatedEmojies".equals(tL_messages_stickerSet.set.short_name)) {
            tL_messages_stickerSet2.documents = tL_messages_stickerSet.documents;
            tL_messages_stickerSet2.packs = tL_messages_stickerSet.packs;
            tL_messages_stickerSet2.set = tL_messages_stickerSet.set;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda243
                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataController.this.lambda$replaceStickerSet$28(tL_messages_stickerSet);
                }
            });
            z = true;
        } else {
            LongSparseArray longSparseArray = new LongSparseArray();
            int size = tL_messages_stickerSet.documents.size();
            for (int i = 0; i < size; i++) {
                TLRPC.Document document = tL_messages_stickerSet.documents.get(i);
                longSparseArray.put(document.id, document);
            }
            int size2 = tL_messages_stickerSet2.documents.size();
            z = false;
            for (int i2 = 0; i2 < size2; i2++) {
                TLRPC.Document document2 = (TLRPC.Document) longSparseArray.get(tL_messages_stickerSet2.documents.get(i2).id);
                if (document2 != null) {
                    tL_messages_stickerSet2.documents.set(i2, document2);
                    z = true;
                }
            }
        }
        if (z) {
            if (z2) {
                putSetToCache(tL_messages_stickerSet2);
                return;
            }
            TLRPC.StickerSet stickerSet = tL_messages_stickerSet.set;
            int i3 = stickerSet.masks ? 1 : stickerSet.emojis ? 5 : 0;
            putStickersToCache(i3, this.stickerSets[i3], this.loadDate[i3], this.loadHash[i3]);
            if ("AnimatedEmojies".equals(tL_messages_stickerSet.set.short_name)) {
                putStickersToCache(4, this.stickerSets[4], this.loadDate[4], this.loadHash[4]);
            }
        }
    }

    public void saveDraft(long j, int i, CharSequence charSequence, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.Message message, boolean z, long j2) {
        saveDraft(j, i, charSequence, arrayList, message, null, j2, z, false);
    }

    public void saveDraft(long j, long j2, CharSequence charSequence, ArrayList<TLRPC.MessageEntity> arrayList, TLRPC.Message message, ChatActivity.ReplyQuote replyQuote, long j3, boolean z, boolean z2) {
        TLRPC.InputReplyTo inputReplyTo;
        TLRPC.Message message2 = (getMessagesController().isForum(j) && j2 == 0) ? null : message;
        TLRPC.DraftMessage tL_draftMessageEmpty = (TextUtils.isEmpty(charSequence) && message2 == null) ? new TLRPC.TL_draftMessageEmpty() : new TLRPC.TL_draftMessage();
        tL_draftMessageEmpty.date = (int) (System.currentTimeMillis() / 1000);
        tL_draftMessageEmpty.message = charSequence == null ? "" : charSequence.toString();
        tL_draftMessageEmpty.no_webpage = z;
        if (j3 != 0) {
            tL_draftMessageEmpty.flags |= 128;
            tL_draftMessageEmpty.effect = j3;
        }
        if (message2 != null) {
            TLRPC.TL_inputReplyToMessage tL_inputReplyToMessage = new TLRPC.TL_inputReplyToMessage();
            tL_draftMessageEmpty.reply_to = tL_inputReplyToMessage;
            tL_draftMessageEmpty.flags |= 16;
            tL_inputReplyToMessage.reply_to_msg_id = message2.id;
            if (replyQuote != null) {
                tL_inputReplyToMessage.quote_text = replyQuote.getText();
                TLRPC.InputReplyTo inputReplyTo2 = tL_draftMessageEmpty.reply_to;
                if (inputReplyTo2.quote_text != null) {
                    inputReplyTo2.flags |= 20;
                    inputReplyTo2.quote_offset = replyQuote.start;
                }
                inputReplyTo2.quote_entities = replyQuote.getEntities();
                ArrayList<TLRPC.MessageEntity> arrayList2 = tL_draftMessageEmpty.reply_to.quote_entities;
                if (arrayList2 != null && !arrayList2.isEmpty()) {
                    tL_draftMessageEmpty.reply_to.quote_entities = new ArrayList<>(tL_draftMessageEmpty.reply_to.quote_entities);
                    tL_draftMessageEmpty.reply_to.flags |= 8;
                }
                MessageObject messageObject = replyQuote.message;
                if (messageObject != null && messageObject.messageOwner != null) {
                    TLRPC.Peer peer = getMessagesController().getPeer(j);
                    TLRPC.Peer peer2 = replyQuote.message.messageOwner.peer_id;
                    if (peer != null && !MessageObject.peersEqual(peer, peer2)) {
                        TLRPC.InputReplyTo inputReplyTo3 = tL_draftMessageEmpty.reply_to;
                        inputReplyTo3.flags |= 2;
                        inputReplyTo3.reply_to_peer_id = getMessagesController().getInputPeer(peer2);
                    }
                }
            } else if (j != MessageObject.getDialogId(message2)) {
                TLRPC.InputReplyTo inputReplyTo4 = tL_draftMessageEmpty.reply_to;
                inputReplyTo4.flags |= 2;
                inputReplyTo4.reply_to_peer_id = getMessagesController().getInputPeer(getMessagesController().getPeer(MessageObject.getDialogId(message2)));
            }
        }
        if (arrayList != null && !arrayList.isEmpty()) {
            tL_draftMessageEmpty.entities = arrayList;
            tL_draftMessageEmpty.flags |= 8;
        }
        LongSparseArray longSparseArray = (LongSparseArray) this.drafts.get(j);
        TLRPC.DraftMessage draftMessage = longSparseArray == null ? null : (TLRPC.DraftMessage) longSparseArray.get(j2);
        if (!z2) {
            if (draftMessage != null) {
                if (draftMessage.message.equals(tL_draftMessageEmpty.message) && replyToEquals(draftMessage.reply_to, tL_draftMessageEmpty.reply_to) && draftMessage.no_webpage == tL_draftMessageEmpty.no_webpage && draftMessage.effect == tL_draftMessageEmpty.effect) {
                    return;
                }
            } else if (TextUtils.isEmpty(tL_draftMessageEmpty.message) && (((inputReplyTo = tL_draftMessageEmpty.reply_to) == null || inputReplyTo.reply_to_msg_id == 0) && tL_draftMessageEmpty.effect == 0)) {
                return;
            }
        }
        saveDraft(j, j2, tL_draftMessageEmpty, message2, false);
        if (j2 == 0 || ChatObject.isForum(this.currentAccount, j)) {
            if (!DialogObject.isEncryptedDialog(j)) {
                TLRPC.TL_messages_saveDraft tL_messages_saveDraft = new TLRPC.TL_messages_saveDraft();
                TLRPC.InputPeer inputPeer = getMessagesController().getInputPeer(j);
                tL_messages_saveDraft.peer = inputPeer;
                if (inputPeer == null) {
                    return;
                }
                tL_messages_saveDraft.message = tL_draftMessageEmpty.message;
                tL_messages_saveDraft.no_webpage = tL_draftMessageEmpty.no_webpage;
                TLRPC.InputReplyTo inputReplyTo5 = tL_draftMessageEmpty.reply_to;
                tL_messages_saveDraft.reply_to = inputReplyTo5;
                if (inputReplyTo5 != null) {
                    tL_messages_saveDraft.flags |= 16;
                }
                int i = tL_draftMessageEmpty.flags;
                if ((i & 8) != 0) {
                    tL_messages_saveDraft.entities = tL_draftMessageEmpty.entities;
                    tL_messages_saveDraft.flags |= 8;
                }
                if ((i & 128) != 0) {
                    tL_messages_saveDraft.effect = tL_draftMessageEmpty.effect;
                    tL_messages_saveDraft.flags |= 128;
                }
                getConnectionsManager().sendRequest(tL_messages_saveDraft, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda101
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        MediaDataController.lambda$saveDraft$188(tLObject, tL_error);
                    }
                });
            }
            getMessagesController().sortDialogs(null);
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.dialogsNeedReload, new Object[0]);
        }
    }

    public void saveDraft(final long j, final long j2, TLRPC.DraftMessage draftMessage, TLRPC.Message message, boolean z) {
        StringBuilder sb;
        SharedPreferences.Editor remove;
        StringBuilder sb2;
        TLRPC.Message message2;
        StringBuilder sb3;
        TLRPC.InputReplyTo inputReplyTo;
        TLRPC.Chat chat;
        StringBuilder sb4;
        StringBuilder sb5;
        if (getMessagesController().isForum(j) && j2 == 0 && TextUtils.isEmpty(draftMessage.message)) {
            TLRPC.InputReplyTo inputReplyTo2 = draftMessage.reply_to;
            if (inputReplyTo2 instanceof TLRPC.TL_inputReplyToMessage) {
                ((TLRPC.TL_inputReplyToMessage) inputReplyTo2).reply_to_msg_id = 0;
            }
        }
        SharedPreferences.Editor edit = this.draftPreferences.edit();
        MessagesController messagesController = getMessagesController();
        if (draftMessage == null || (draftMessage instanceof TLRPC.TL_draftMessageEmpty)) {
            LongSparseArray longSparseArray = (LongSparseArray) this.drafts.get(j);
            if (longSparseArray != null) {
                longSparseArray.remove(j2);
                if (longSparseArray.size() == 0) {
                    this.drafts.remove(j);
                }
            }
            LongSparseArray longSparseArray2 = (LongSparseArray) this.draftMessages.get(j);
            if (longSparseArray2 != null) {
                longSparseArray2.remove(j2);
                if (longSparseArray2.size() == 0) {
                    this.draftMessages.remove(j);
                }
            }
            SharedPreferences.Editor edit2 = this.draftPreferences.edit();
            if (j2 == 0) {
                sb = new StringBuilder();
                sb.append("");
                sb.append(j);
                remove = edit2.remove(sb.toString());
                sb2 = new StringBuilder();
                sb2.append("r_");
                sb2.append(j);
            } else {
                sb = new StringBuilder();
                sb.append("t_");
                sb.append(j);
                sb.append("_");
                sb.append(j2);
                remove = edit2.remove(sb.toString());
                sb2 = new StringBuilder();
                sb2.append("rt_");
                sb2.append(j);
                sb2.append("_");
                sb2.append(j2);
            }
            remove.remove(sb2.toString()).commit();
            messagesController.removeDraftDialogIfNeed(j);
        } else {
            LongSparseArray longSparseArray3 = (LongSparseArray) this.drafts.get(j);
            if (longSparseArray3 == null) {
                longSparseArray3 = new LongSparseArray();
                this.drafts.put(j, longSparseArray3);
            }
            longSparseArray3.put(j2, draftMessage);
            if (j2 == 0) {
                messagesController.putDraftDialogIfNeed(j, draftMessage);
            }
            try {
                SerializedData serializedData = new SerializedData(draftMessage.getObjectSize());
                draftMessage.serializeToStream(serializedData);
                if (j2 == 0) {
                    sb5 = new StringBuilder();
                    sb5.append("");
                    sb5.append(j);
                } else {
                    sb5 = new StringBuilder();
                    sb5.append("t_");
                    sb5.append(j);
                    sb5.append("_");
                    sb5.append(j2);
                }
                edit.putString(sb5.toString(), Utilities.bytesToHex(serializedData.toByteArray()));
                serializedData.cleanup();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        LongSparseArray longSparseArray4 = (LongSparseArray) this.draftMessages.get(j);
        TLRPC.User user = null;
        if (message != null || draftMessage == null || draftMessage.reply_to == null) {
            if (draftMessage != null && draftMessage.reply_to == null) {
                message2 = null;
            }
            message2 = message;
        } else {
            if (longSparseArray4 != null) {
                message2 = (TLRPC.Message) longSparseArray4.get(j2);
            }
            message2 = message;
        }
        if (message2 == null) {
            if (longSparseArray4 != null) {
                longSparseArray4.remove(j2);
                if (longSparseArray4.size() == 0) {
                    this.draftMessages.remove(j);
                }
            }
            if (j2 == 0) {
                sb4 = new StringBuilder();
                sb4.append("r_");
                sb4.append(j);
            } else {
                sb4 = new StringBuilder();
                sb4.append("rt_");
                sb4.append(j);
                sb4.append("_");
                sb4.append(j2);
            }
            edit.remove(sb4.toString());
        } else {
            if (longSparseArray4 == null) {
                longSparseArray4 = new LongSparseArray();
                this.draftMessages.put(j, longSparseArray4);
            }
            longSparseArray4.put(j2, message2);
            try {
                SerializedData serializedData2 = new SerializedData(message2.getObjectSize());
                message2.serializeToStream(serializedData2);
                if (j2 == 0) {
                    sb3 = new StringBuilder();
                    sb3.append("r_");
                    sb3.append(j);
                } else {
                    sb3 = new StringBuilder();
                    sb3.append("rt_");
                    sb3.append(j);
                    sb3.append("_");
                    sb3.append(j2);
                }
                edit.putString(sb3.toString(), Utilities.bytesToHex(serializedData2.toByteArray()));
                serializedData2.cleanup();
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        edit.commit();
        if (z) {
            if (j2 == 0 || getMessagesController().isForum(j)) {
                if (draftMessage != null && (inputReplyTo = draftMessage.reply_to) != null && inputReplyTo.reply_to_msg_id != 0 && (message2 == null || ((message2.reply_to instanceof TLRPC.TL_messageReplyHeader) && message2.replyMessage == null))) {
                    long peerDialogId = (inputReplyTo.flags & 2) != 0 ? DialogObject.getPeerDialogId(inputReplyTo.reply_to_peer_id) : j;
                    if (DialogObject.isUserDialog(peerDialogId)) {
                        user = getMessagesController().getUser(Long.valueOf(peerDialogId));
                        chat = null;
                    } else {
                        chat = getMessagesController().getChat(Long.valueOf(-peerDialogId));
                    }
                    if (user != null || chat != null) {
                        long j3 = ChatObject.isChannel(chat) ? chat.id : 0L;
                        final int i = draftMessage.reply_to.reply_to_msg_id;
                        final long j4 = peerDialogId;
                        final long j5 = j3;
                        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda16
                            @Override // java.lang.Runnable
                            public final void run() {
                                MediaDataController.this.lambda$saveDraft$191(i, j4, j5, j, j2);
                            }
                        });
                    }
                }
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.newDraftReceived, Long.valueOf(j));
            }
        }
    }

    public boolean saveToRingtones(final TLRPC.Document document) {
        if (document == null) {
            return false;
        }
        if (this.ringtoneDataStore.contains(document.id)) {
            return true;
        }
        if (document.size > MessagesController.getInstance(this.currentAccount).ringtoneSizeMax) {
            NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 4, LocaleController.formatString("TooLargeError", R.string.TooLargeError, new Object[0]), LocaleController.formatString("ErrorRingtoneSizeTooBig", R.string.ErrorRingtoneSizeTooBig, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).ringtoneSizeMax / 1024)));
            return false;
        }
        for (int i = 0; i < document.attributes.size(); i++) {
            TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
            if ((documentAttribute instanceof TLRPC.TL_documentAttributeAudio) && documentAttribute.duration > MessagesController.getInstance(this.currentAccount).ringtoneDurationMax) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.showBulletin, 4, LocaleController.formatString("TooLongError", R.string.TooLongError, new Object[0]), LocaleController.formatString("ErrorRingtoneDurationTooLong", R.string.ErrorRingtoneDurationTooLong, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).ringtoneDurationMax)));
                return false;
            }
        }
        TLRPC.TL_account_saveRingtone tL_account_saveRingtone = new TLRPC.TL_account_saveRingtone();
        TLRPC.TL_inputDocument tL_inputDocument = new TLRPC.TL_inputDocument();
        tL_account_saveRingtone.id = tL_inputDocument;
        tL_inputDocument.id = document.id;
        tL_inputDocument.file_reference = document.file_reference;
        tL_inputDocument.access_hash = document.access_hash;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_saveRingtone, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda183
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$saveToRingtones$204(document, tLObject, tL_error);
            }
        });
        return true;
    }

    public boolean searchEndReached() {
        boolean[] zArr = this.messagesSearchEndReached;
        return (zArr[0] && this.lastMergeDialogId == 0 && zArr[1]) || this.loadingSearchLocal || this.loadedPredirectedSearchLocal;
    }

    public void searchMessagesInChat(String str, long j, long j2, int i, int i2, long j3, TLRPC.User user, TLRPC.Chat chat, ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        searchMessagesInChat(str, j, j2, i, i2, j3, false, user, chat, true, visibleReaction);
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x03c8  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0319  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x022b  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0254  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x02c9  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02cc  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0316  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x039b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void searchMessagesInChat(String str, final long j, final long j2, final int i, final int i2, final long j3, boolean z, final TLRPC.User user, final TLRPC.Chat chat, final boolean z2, final ReactionsLayoutInBubble.VisibleReaction visibleReaction) {
        String str2;
        long j4;
        int i3;
        long j5;
        int i4;
        long j6;
        ReactionsLayoutInBubble.VisibleReaction visibleReaction2;
        TLRPC.Chat chat2;
        boolean z3;
        TLRPC.InputPeer inputPeer;
        TLRPC.InputPeer inputPeer2;
        boolean z4;
        TLRPC.TL_messages_search tL_messages_search;
        long j7;
        long j8;
        TLRPC.InputPeer inputPeer3;
        boolean z5 = !z;
        if (this.reqId != 0) {
            this.loadingMoreSearchMessages = false;
            getConnectionsManager().cancelRequest(this.reqId, true);
            this.reqId = 0;
        }
        if (this.mergeReqId != 0) {
            getConnectionsManager().cancelRequest(this.mergeReqId, true);
            this.mergeReqId = 0;
        }
        if (str != null) {
            if (z5) {
                boolean[] zArr = this.messagesSearchEndReached;
                zArr[1] = false;
                zArr[0] = false;
                int[] iArr = this.messagesSearchCount;
                iArr[1] = 0;
                iArr[0] = 0;
                this.searchResultMessages.clear();
                this.searchLocalResultMessages.clear();
                this.searchServerResultMessagesMap[0].clear();
                this.searchServerResultMessagesMap[1].clear();
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.chatSearchResultsLoading, Integer.valueOf(i));
            }
            str2 = str;
            j4 = j;
            i3 = 0;
        } else {
            if (this.searchResultMessages.isEmpty()) {
                this.loadingMoreSearchMessages = false;
                return;
            }
            if (i2 != 1) {
                if (i2 != 2) {
                    this.loadingMoreSearchMessages = false;
                    return;
                }
                int i5 = this.lastReturnedNum - 1;
                this.lastReturnedNum = i5;
                if (i5 < 0) {
                    this.lastReturnedNum = 0;
                    return;
                }
                if (i5 >= this.searchResultMessages.size()) {
                    this.lastReturnedNum = this.searchResultMessages.size() - 1;
                }
                MessageObject messageObject = this.searchResultMessages.get(this.lastReturnedNum);
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.chatSearchResultsAvailable, Integer.valueOf(i), Integer.valueOf(messageObject.getId()), Integer.valueOf(getMask()), Long.valueOf(messageObject.getDialogId()), Integer.valueOf(this.lastReturnedNum), Integer.valueOf(getSearchCount()), Boolean.valueOf(z2));
                this.loadingMoreSearchMessages = false;
                return;
            }
            int i6 = this.lastReturnedNum + 1;
            this.lastReturnedNum = i6;
            if (i6 < this.searchResultMessages.size()) {
                MessageObject messageObject2 = this.searchResultMessages.get(this.lastReturnedNum);
                getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.chatSearchResultsAvailable, Integer.valueOf(i), Integer.valueOf(messageObject2.getId()), Integer.valueOf(getMask()), Long.valueOf(messageObject2.getDialogId()), Integer.valueOf(this.lastReturnedNum), Integer.valueOf(getSearchCount()), Boolean.valueOf(z2));
                this.loadingMoreSearchMessages = false;
                return;
            }
            boolean[] zArr2 = this.messagesSearchEndReached;
            if (zArr2[0] && j2 == 0 && zArr2[1]) {
                this.lastReturnedNum--;
                this.loadingMoreSearchMessages = false;
                return;
            }
            String str3 = this.lastSearchQuery;
            ArrayList<MessageObject> arrayList = this.searchResultMessages;
            MessageObject messageObject3 = arrayList.get(arrayList.size() - 1);
            if (messageObject3.getDialogId() != j || this.messagesSearchEndReached[0]) {
                i3 = messageObject3.getDialogId() == j2 ? messageObject3.getId() : 0;
                this.messagesSearchEndReached[1] = false;
                str2 = str3;
                j4 = j2;
            } else {
                i3 = messageObject3.getId();
                str2 = str3;
                j4 = j;
            }
            z5 = false;
        }
        boolean z6 = str2 != null && (str2.trim().startsWith("#") || str2.trim().startsWith("$"));
        boolean[] zArr3 = this.messagesSearchEndReached;
        if (!zArr3[0] || zArr3[1]) {
            j5 = 0;
        } else {
            j5 = 0;
            if (j2 != 0) {
                i4 = i3;
                j6 = j2;
                if (j6 == j || !z5) {
                    visibleReaction2 = visibleReaction;
                    chat2 = chat;
                    z3 = false;
                } else {
                    if (j2 != j5) {
                        TLRPC.InputPeer inputPeer4 = getMessagesController().getInputPeer(j2);
                        if (inputPeer4 == null) {
                            return;
                        }
                        final TLRPC.TL_messages_search tL_messages_search2 = new TLRPC.TL_messages_search();
                        tL_messages_search2.peer = inputPeer4;
                        this.lastMergeDialogId = j2;
                        tL_messages_search2.limit = 1;
                        tL_messages_search2.q = str2;
                        if (user == null) {
                            if (chat != null) {
                                inputPeer3 = MessagesController.getInputPeer(chat);
                            }
                            if (j3 != 0) {
                                if (j != getUserConfig().getClientUserId()) {
                                    tL_messages_search2.top_msg_id = (int) j3;
                                    tL_messages_search2.flags |= 2;
                                    if (visibleReaction != null) {
                                        tL_messages_search2.saved_reaction.add(visibleReaction.toTLReaction());
                                        tL_messages_search2.flags |= 8;
                                    }
                                    tL_messages_search2.filter = new TLRPC.TL_inputMessagesFilterEmpty();
                                    this.mergeReqId = getConnectionsManager().sendRequest(tL_messages_search2, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda133
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                            MediaDataController.this.lambda$searchMessagesInChat$119(j2, tL_messages_search2, j, i, i2, j3, user, chat, z2, visibleReaction, tLObject, tL_error);
                                        }
                                    }, 2);
                                    return;
                                }
                                tL_messages_search2.saved_peer_id = getMessagesController().getInputPeer(j3);
                                tL_messages_search2.flags |= 4;
                            }
                            if (visibleReaction != null) {
                            }
                            tL_messages_search2.filter = new TLRPC.TL_inputMessagesFilterEmpty();
                            this.mergeReqId = getConnectionsManager().sendRequest(tL_messages_search2, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda133
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    MediaDataController.this.lambda$searchMessagesInChat$119(j2, tL_messages_search2, j, i, i2, j3, user, chat, z2, visibleReaction, tLObject, tL_error);
                                }
                            }, 2);
                            return;
                        }
                        inputPeer3 = MessagesController.getInputPeer(user);
                        tL_messages_search2.from_id = inputPeer3;
                        tL_messages_search2.flags = 1 | tL_messages_search2.flags;
                        if (j3 != 0) {
                        }
                        if (visibleReaction != null) {
                        }
                        tL_messages_search2.filter = new TLRPC.TL_inputMessagesFilterEmpty();
                        this.mergeReqId = getConnectionsManager().sendRequest(tL_messages_search2, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda133
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                MediaDataController.this.lambda$searchMessagesInChat$119(j2, tL_messages_search2, j, i, i2, j3, user, chat, z2, visibleReaction, tLObject, tL_error);
                            }
                        }, 2);
                        return;
                    }
                    visibleReaction2 = visibleReaction;
                    chat2 = chat;
                    this.lastMergeDialogId = 0L;
                    zArr3[1] = true;
                    z3 = false;
                    this.messagesSearchCount[1] = 0;
                }
                TLRPC.TL_messages_search tL_messages_search3 = new TLRPC.TL_messages_search();
                inputPeer = getMessagesController().getInputPeer(j6);
                tL_messages_search3.peer = inputPeer;
                if (inputPeer != null) {
                    this.loadingMoreSearchMessages = z3;
                    return;
                }
                this.lastGuid = i;
                this.lastDialogId = j;
                this.lastSearchUser = user;
                this.lastSearchChat = chat2;
                this.lastReplyMessageId = j3;
                this.lastReaction = visibleReaction2;
                tL_messages_search3.limit = 21;
                tL_messages_search3.q = str2 != null ? str2 : "";
                tL_messages_search3.offset_id = i4;
                if (user == null) {
                    if (chat != null) {
                        inputPeer2 = MessagesController.getInputPeer(chat);
                    }
                    this.loadingSearchLocal = false;
                    this.loadedPredirectedSearchLocal = false;
                    final int i7 = this.lastReqId + 1;
                    this.lastReqId = i7;
                    z4 = j != getUserConfig().getClientUserId();
                    if (z4 || visibleReaction2 == null || !z5) {
                        tL_messages_search = tL_messages_search3;
                        j7 = j6;
                        j8 = 0;
                    } else {
                        this.lastReturnedNum = 0;
                        this.searchServerResultMessages.clear();
                        this.searchServerResultMessagesMap[0].clear();
                        this.searchServerResultMessagesMap[1].clear();
                        long j9 = j6;
                        final int savedTagCount = getMessagesController().getSavedTagCount(this.lastReplyMessageId, visibleReaction2);
                        this.messagesLocalSearchCount = TextUtils.isEmpty(tL_messages_search3.q) ? savedTagCount : 0;
                        this.loadingSearchLocal = true;
                        this.loadedPredirectedSearchLocal = false;
                        MessagesStorage messagesStorage = getMessagesStorage();
                        TLRPC.Reaction tLReaction = visibleReaction.toTLReaction();
                        long j10 = this.lastReplyMessageId;
                        ArrayList<MessageObject> arrayList2 = this.searchLocalResultMessages;
                        j7 = j9;
                        j8 = 0;
                        tL_messages_search = tL_messages_search3;
                        messagesStorage.searchSavedByTag(tLReaction, j10, str2, NotificationCenter.customTypefacesLoaded, arrayList2 == null ? 0 : arrayList2.size(), new Utilities.Callback4() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda134
                            @Override // org.telegram.messenger.Utilities.Callback4
                            public final void run(Object obj, Object obj2, Object obj3, Object obj4) {
                                MediaDataController.this.lambda$searchMessagesInChat$120(i7, savedTagCount, i, j, (ArrayList) obj, (ArrayList) obj2, (ArrayList) obj3, (ArrayList) obj4);
                            }
                        }, true);
                    }
                    if (this.lastReplyMessageId != j8) {
                        if (j7 != getUserConfig().getClientUserId()) {
                            tL_messages_search.top_msg_id = (int) this.lastReplyMessageId;
                            tL_messages_search.flags |= 2;
                            if (visibleReaction2 != null) {
                                tL_messages_search.saved_reaction.add(visibleReaction.toTLReaction());
                                tL_messages_search.flags |= 8;
                            }
                            tL_messages_search.filter = new TLRPC.TL_inputMessagesFilterEmpty();
                            this.lastSearchQuery = str2;
                            final TLRPC.TL_messages_search tL_messages_search4 = tL_messages_search;
                            final boolean z7 = z4;
                            final String str4 = str2;
                            final boolean z8 = z6;
                            final long j11 = j7;
                            this.reqId = getConnectionsManager().sendRequest(tL_messages_search, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda135
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    MediaDataController.this.lambda$searchMessagesInChat$123(tL_messages_search4, z7, str4, z8, i7, z2, j11, j, i, j2, j3, user, chat, tLObject, tL_error);
                                }
                            }, 2);
                            return;
                        }
                        tL_messages_search.saved_peer_id = getMessagesController().getInputPeer(this.lastReplyMessageId);
                        tL_messages_search.flags |= 4;
                    }
                    if (visibleReaction2 != null) {
                    }
                    tL_messages_search.filter = new TLRPC.TL_inputMessagesFilterEmpty();
                    this.lastSearchQuery = str2;
                    final TLRPC.TL_messages_search tL_messages_search42 = tL_messages_search;
                    final boolean z72 = z4;
                    final String str42 = str2;
                    final boolean z82 = z6;
                    final long j112 = j7;
                    this.reqId = getConnectionsManager().sendRequest(tL_messages_search, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda135
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            MediaDataController.this.lambda$searchMessagesInChat$123(tL_messages_search42, z72, str42, z82, i7, z2, j112, j, i, j2, j3, user, chat, tLObject, tL_error);
                        }
                    }, 2);
                    return;
                }
                inputPeer2 = MessagesController.getInputPeer(user);
                tL_messages_search3.from_id = inputPeer2;
                tL_messages_search3.flags |= 1;
                this.loadingSearchLocal = false;
                this.loadedPredirectedSearchLocal = false;
                final int i72 = this.lastReqId + 1;
                this.lastReqId = i72;
                if (j != getUserConfig().getClientUserId()) {
                }
                if (z4) {
                }
                tL_messages_search = tL_messages_search3;
                j7 = j6;
                j8 = 0;
                if (this.lastReplyMessageId != j8) {
                }
                if (visibleReaction2 != null) {
                }
                tL_messages_search.filter = new TLRPC.TL_inputMessagesFilterEmpty();
                this.lastSearchQuery = str2;
                final TLRPC.TL_messages_search tL_messages_search422 = tL_messages_search;
                final boolean z722 = z4;
                final String str422 = str2;
                final boolean z822 = z6;
                final long j1122 = j7;
                this.reqId = getConnectionsManager().sendRequest(tL_messages_search, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda135
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        MediaDataController.this.lambda$searchMessagesInChat$123(tL_messages_search422, z722, str422, z822, i72, z2, j1122, j, i, j2, j3, user, chat, tLObject, tL_error);
                    }
                }, 2);
                return;
            }
        }
        i4 = i3;
        j6 = j4;
        if (j6 == j) {
        }
        visibleReaction2 = visibleReaction;
        chat2 = chat;
        z3 = false;
        TLRPC.TL_messages_search tL_messages_search32 = new TLRPC.TL_messages_search();
        inputPeer = getMessagesController().getInputPeer(j6);
        tL_messages_search32.peer = inputPeer;
        if (inputPeer != null) {
        }
    }

    public void setDoubleTapReaction(String str) {
        MessagesController.getEmojiSettings(this.currentAccount).edit().putString("reaction_on_double_tap", str).apply();
        this.doubleTapReaction = str;
    }

    public void setDraftFolderId(long j, int i) {
        this.draftsFolderIds.put(j, Integer.valueOf(i));
    }

    public void setPlaceholderImage(final BackupImageView backupImageView, String str, final String str2, final String str3) {
        TLRPC.TL_inputStickerSetShortName tL_inputStickerSetShortName = new TLRPC.TL_inputStickerSetShortName();
        tL_inputStickerSetShortName.short_name = str;
        getInstance(this.currentAccount).getStickerSet(tL_inputStickerSetShortName, 0, false, new Utilities.Callback() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda120
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                MediaDataController.lambda$setPlaceholderImage$31(str2, backupImageView, str3, (TLRPC.TL_messages_stickerSet) obj);
            }
        });
    }

    public void setSearchedPosition(int i) {
        if (i < 0 || i >= this.searchResultMessages.size()) {
            return;
        }
        this.lastReturnedNum = i;
    }

    public void storeTempStickerSet(TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
        TLRPC.StickerSet stickerSet;
        if (tL_messages_stickerSet == null || (stickerSet = tL_messages_stickerSet.set) == null) {
            return;
        }
        this.stickerSetsById.put(stickerSet.id, tL_messages_stickerSet);
        String str = tL_messages_stickerSet.set.short_name;
        if (str != null) {
            this.stickerSetsByName.put(str.toLowerCase(), tL_messages_stickerSet);
        }
    }

    public CharSequence substring(CharSequence charSequence, int i, int i2) {
        if (!(charSequence instanceof SpannableStringBuilder) && !(charSequence instanceof SpannedString)) {
            return TextUtils.substring(charSequence, i, i2);
        }
        return charSequence.subSequence(i, i2);
    }

    public void toggleDraftVoiceOnce(long j, long j2, boolean z) {
        DraftVoice draftVoice = getDraftVoice(j, j2);
        if (draftVoice == null || draftVoice.once == z) {
            return;
        }
        draftVoice.once = z;
        ApplicationLoader.applicationContext.getSharedPreferences("2voicedrafts_" + this.currentAccount, 0).edit().putString(Objects.hash(Long.valueOf(j), Long.valueOf(j2)) + "", draftVoice.toString()).apply();
    }

    public void toggleStickerSet(Context context, TLObject tLObject, int i, BaseFragment baseFragment, boolean z, boolean z2) {
        toggleStickerSet(context, tLObject, i, baseFragment, z, z2, null, true);
    }

    public void toggleStickerSet(final Context context, final TLObject tLObject, final int i, final BaseFragment baseFragment, final boolean z, boolean z2, final Runnable runnable, boolean z3) {
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet;
        TLRPC.StickerSet stickerSet;
        final TLRPC.TL_messages_stickerSet tL_messages_stickerSet2;
        int i2;
        MediaDataController mediaDataController;
        Context context2;
        int i3;
        BaseFragment baseFragment2;
        boolean z4;
        TLObject tLObject2;
        TLRPC.StickerSet stickerSet2;
        int i4;
        boolean z5;
        if (tLObject instanceof TLRPC.TL_messages_stickerSet) {
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet3 = (TLRPC.TL_messages_stickerSet) tLObject;
            tL_messages_stickerSet2 = tL_messages_stickerSet3;
            stickerSet = tL_messages_stickerSet3.set;
        } else {
            if (!(tLObject instanceof TLRPC.StickerSetCovered)) {
                throw new IllegalArgumentException("Invalid type of the given stickerSetObject: " + tLObject.getClass());
            }
            TLRPC.StickerSet stickerSet3 = ((TLRPC.StickerSetCovered) tLObject).set;
            if (i != 2) {
                tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) this.stickerSetsById.get(stickerSet3.id);
                if (tL_messages_stickerSet == null) {
                    return;
                }
            } else {
                tL_messages_stickerSet = null;
            }
            stickerSet = stickerSet3;
            tL_messages_stickerSet2 = tL_messages_stickerSet;
        }
        int i5 = stickerSet.masks ? 1 : stickerSet.emojis ? 5 : 0;
        stickerSet.archived = i == 1;
        int i6 = 0;
        while (true) {
            if (i6 >= this.stickerSets[i5].size()) {
                i2 = 0;
                break;
            }
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet4 = this.stickerSets[i5].get(i6);
            if (tL_messages_stickerSet4.set.id == stickerSet.id) {
                this.stickerSets[i5].remove(i6);
                if (i == 2) {
                    tL_messages_stickerSet4.set.title = stickerSet.title;
                    this.stickerSets[i5].add(0, tL_messages_stickerSet4);
                } else if (z3) {
                    this.stickerSetsById.remove(tL_messages_stickerSet4.set.id);
                    this.installedStickerSetsById.remove(tL_messages_stickerSet4.set.id);
                    this.stickerSetsByName.remove(tL_messages_stickerSet4.set.short_name);
                }
                i2 = i6;
            } else {
                i6++;
            }
        }
        this.loadHash[i5] = calcStickersHash(this.stickerSets[i5]);
        putStickersToCache(i5, this.stickerSets[i5], this.loadDate[i5], this.loadHash[i5]);
        if (i == 2) {
            if (!cancelRemovingStickerSet(stickerSet.id)) {
                mediaDataController = this;
                context2 = context;
                i3 = i;
                baseFragment2 = baseFragment;
                z4 = z;
                tLObject2 = tLObject;
                stickerSet2 = stickerSet;
                i4 = i5;
                z5 = z2;
                mediaDataController.toggleStickerSetInternal(context2, i3, baseFragment2, z4, tLObject2, stickerSet2, i4, z5);
            }
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(i5), Boolean.TRUE);
        }
        if (!z2 || baseFragment == null) {
            mediaDataController = this;
            context2 = context;
            i3 = i;
            baseFragment2 = baseFragment;
            z4 = z;
            tLObject2 = tLObject;
            stickerSet2 = stickerSet;
            i4 = i5;
            z5 = false;
            mediaDataController.toggleStickerSetInternal(context2, i3, baseFragment2, z4, tLObject2, stickerSet2, i4, z5);
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(i5), Boolean.TRUE);
        }
        StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(context, tLObject, i, null, baseFragment.getResourceProvider());
        final boolean[] zArr = new boolean[1];
        markSetUninstalling(stickerSet.id, true);
        final TLRPC.StickerSet stickerSet4 = stickerSet;
        final int i7 = i5;
        final int i8 = i2;
        final TLRPC.StickerSet stickerSet5 = stickerSet;
        final int i9 = i5;
        Bulletin.UndoButton delayedAction = new Bulletin.UndoButton(context, false).setUndoAction(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$toggleStickerSet$106(zArr, stickerSet4, i7, i8, tL_messages_stickerSet2, runnable);
            }
        }).setDelayedAction(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$toggleStickerSet$107(zArr, context, i, baseFragment, z, tLObject, stickerSet5, i9);
            }
        });
        stickerSetBulletinLayout.setButton(delayedAction);
        LongSparseArray longSparseArray = this.removingStickerSetsUndos;
        long j = stickerSet.id;
        Objects.requireNonNull(delayedAction);
        longSparseArray.put(j, new MediaDataController$$ExternalSyntheticLambda15(delayedAction));
        Bulletin.make(baseFragment, stickerSetBulletinLayout, 2750).show();
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(i5), Boolean.TRUE);
    }

    public void toggleStickerSets(ArrayList<TLRPC.StickerSet> arrayList, final int i, final int i2, final BaseFragment baseFragment, final boolean z) {
        ArrayList<TLRPC.InputStickerSet> arrayList2;
        int i3 = 1;
        int size = arrayList.size();
        ArrayList<TLRPC.InputStickerSet> arrayList3 = new ArrayList<>(size);
        int i4 = 0;
        while (i4 < size) {
            TLRPC.StickerSet stickerSet = arrayList.get(i4);
            TLRPC.TL_inputStickerSetID tL_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
            tL_inputStickerSetID.access_hash = stickerSet.access_hash;
            tL_inputStickerSetID.id = stickerSet.id;
            arrayList3.add(tL_inputStickerSetID);
            if (i2 != 0) {
                stickerSet.archived = i2 == i3;
            }
            int size2 = this.stickerSets[i].size();
            int i5 = 0;
            while (true) {
                if (i5 >= size2) {
                    arrayList2 = arrayList3;
                    break;
                }
                TLRPC.TL_messages_stickerSet tL_messages_stickerSet = this.stickerSets[i].get(i5);
                arrayList2 = arrayList3;
                if (tL_messages_stickerSet.set.id == tL_inputStickerSetID.id) {
                    this.stickerSets[i].remove(i5);
                    if (i2 == 2) {
                        this.stickerSets[i].add(0, tL_messages_stickerSet);
                    } else {
                        this.stickerSetsById.remove(tL_messages_stickerSet.set.id);
                        this.installedStickerSetsById.remove(tL_messages_stickerSet.set.id);
                        this.stickerSetsByName.remove(tL_messages_stickerSet.set.short_name);
                    }
                    i3 = 1;
                } else {
                    i3 = 1;
                    i5++;
                    arrayList3 = arrayList2;
                }
            }
            i4 += i3;
            arrayList3 = arrayList2;
        }
        this.loadHash[i] = calcStickersHash(this.stickerSets[i]);
        putStickersToCache(i, this.stickerSets[i], this.loadDate[i], this.loadHash[i]);
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stickersDidLoad, Integer.valueOf(i), Boolean.TRUE);
        TLRPC.TL_messages_toggleStickerSets tL_messages_toggleStickerSets = new TLRPC.TL_messages_toggleStickerSets();
        tL_messages_toggleStickerSets.stickersets = arrayList3;
        if (i2 == 0) {
            tL_messages_toggleStickerSets.uninstall = true;
        } else if (i2 == 1) {
            tL_messages_toggleStickerSets.archive = true;
        } else if (i2 == 2) {
            tL_messages_toggleStickerSets.unarchive = true;
        }
        getConnectionsManager().sendRequest(tL_messages_toggleStickerSets, new RequestDelegate() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda70
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                MediaDataController.this.lambda$toggleStickerSets$117(i2, baseFragment, z, i, tLObject, tL_error);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00c4 A[Catch: Exception -> 0x0038, TryCatch #0 {Exception -> 0x0038, blocks: (B:2:0x0000, B:4:0x0006, B:6:0x000f, B:7:0x003b, B:9:0x003f, B:10:0x0053, B:12:0x005c, B:16:0x006f, B:18:0x0076, B:21:0x0089, B:22:0x00a2, B:27:0x00c4, B:29:0x00c8, B:30:0x00dd, B:32:0x00e1, B:33:0x00ea, B:35:0x00e6, B:36:0x00d1, B:38:0x00d5, B:40:0x00db, B:41:0x0094, B:43:0x009a, B:44:0x00aa, B:46:0x00b0), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00e1 A[Catch: Exception -> 0x0038, TryCatch #0 {Exception -> 0x0038, blocks: (B:2:0x0000, B:4:0x0006, B:6:0x000f, B:7:0x003b, B:9:0x003f, B:10:0x0053, B:12:0x005c, B:16:0x006f, B:18:0x0076, B:21:0x0089, B:22:0x00a2, B:27:0x00c4, B:29:0x00c8, B:30:0x00dd, B:32:0x00e1, B:33:0x00ea, B:35:0x00e6, B:36:0x00d1, B:38:0x00d5, B:40:0x00db, B:41:0x0094, B:43:0x009a, B:44:0x00aa, B:46:0x00b0), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00e6 A[Catch: Exception -> 0x0038, TryCatch #0 {Exception -> 0x0038, blocks: (B:2:0x0000, B:4:0x0006, B:6:0x000f, B:7:0x003b, B:9:0x003f, B:10:0x0053, B:12:0x005c, B:16:0x006f, B:18:0x0076, B:21:0x0089, B:22:0x00a2, B:27:0x00c4, B:29:0x00c8, B:30:0x00dd, B:32:0x00e1, B:33:0x00ea, B:35:0x00e6, B:36:0x00d1, B:38:0x00d5, B:40:0x00db, B:41:0x0094, B:43:0x009a, B:44:0x00aa, B:46:0x00b0), top: B:1:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00db A[Catch: Exception -> 0x0038, TryCatch #0 {Exception -> 0x0038, blocks: (B:2:0x0000, B:4:0x0006, B:6:0x000f, B:7:0x003b, B:9:0x003f, B:10:0x0053, B:12:0x005c, B:16:0x006f, B:18:0x0076, B:21:0x0089, B:22:0x00a2, B:27:0x00c4, B:29:0x00c8, B:30:0x00dd, B:32:0x00e1, B:33:0x00ea, B:35:0x00e6, B:36:0x00d1, B:38:0x00d5, B:40:0x00db, B:41:0x0094, B:43:0x009a, B:44:0x00aa, B:46:0x00b0), top: B:1:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void uninstallShortcut(long j, int i) {
        TLRPC.Chat chat;
        MessagesController messagesController;
        Long valueOf;
        Object systemService;
        try {
            int i2 = Build.VERSION.SDK_INT;
            if (i2 >= 26) {
                ArrayList arrayList = new ArrayList();
                if (i == SHORTCUT_TYPE_USER_OR_CHAT) {
                    arrayList.add("sdid_" + j);
                    arrayList.add("ndid_" + j);
                }
                if (i == SHORTCUT_TYPE_ATTACHED_BOT) {
                    arrayList.add("bdid_" + j);
                }
                ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList);
                if (i2 >= 30) {
                    systemService = ApplicationLoader.applicationContext.getSystemService((Class<Object>) ShortcutManagerCompat$$ExternalSyntheticApiModelOutline0.m());
                    ShortcutManagerCompat$$ExternalSyntheticApiModelOutline2.m(systemService).removeLongLivedShortcuts(arrayList);
                    return;
                }
                return;
            }
            TLRPC.User user = null;
            if (DialogObject.isEncryptedDialog(j)) {
                TLRPC.EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j)));
                if (encryptedChat == null) {
                    return;
                }
                messagesController = getMessagesController();
                valueOf = Long.valueOf(encryptedChat.user_id);
            } else {
                if (!DialogObject.isUserDialog(j)) {
                    if (DialogObject.isChatDialog(j)) {
                        chat = getMessagesController().getChat(Long.valueOf(-j));
                        if (user == null || chat != null) {
                            String formatName = user == null ? i == SHORTCUT_TYPE_USER_OR_CHAT ? ContactsController.formatName(user.first_name, user.last_name) : i == SHORTCUT_TYPE_ATTACHED_BOT ? user.first_name : "" : chat.title;
                            Intent createIntrnalShortcutIntent = i != SHORTCUT_TYPE_USER_OR_CHAT ? createIntrnalShortcutIntent(j) : createIntrnalAttachedBotShortcutIntent(j);
                            Intent intent = new Intent();
                            intent.putExtra("android.intent.extra.shortcut.INTENT", createIntrnalShortcutIntent);
                            intent.putExtra("android.intent.extra.shortcut.NAME", formatName);
                            intent.putExtra("duplicate", false);
                            intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
                            ApplicationLoader.applicationContext.sendBroadcast(intent);
                        }
                        return;
                    }
                    return;
                }
                messagesController = getMessagesController();
                valueOf = Long.valueOf(j);
            }
            user = messagesController.getUser(valueOf);
            chat = null;
            if (user == null) {
            }
            if (user == null) {
            }
            if (i != SHORTCUT_TYPE_USER_OR_CHAT) {
            }
            Intent intent2 = new Intent();
            intent2.putExtra("android.intent.extra.shortcut.INTENT", createIntrnalShortcutIntent);
            intent2.putExtra("android.intent.extra.shortcut.NAME", formatName);
            intent2.putExtra("duplicate", false);
            intent2.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
            ApplicationLoader.applicationContext.sendBroadcast(intent2);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void updateAttachMenuBotsInCache() {
        if (getAttachMenuBots() != null) {
            putMenuBotsToCache(getAttachMenuBots(), this.menuBotsUpdateHash, this.menuBotsUpdateDate);
        }
    }

    public void updateBotInfo(final long j, final TLRPC.TL_updateBotCommands tL_updateBotCommands) {
        TL_bots.BotInfo botInfo = this.botInfos.get(tL_updateBotCommands.bot_id + "_" + j);
        if (botInfo != null) {
            botInfo.commands = tL_updateBotCommands.commands;
            getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.botInfoDidLoad, botInfo, 0);
        }
        getMessagesStorage().getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda206
            @Override // java.lang.Runnable
            public final void run() {
                MediaDataController.this.lambda$updateBotInfo$202(tL_updateBotCommands, j);
            }
        });
    }

    public void uploadRingtone(String str) {
        if (this.ringtoneUploaderHashMap.containsKey(str)) {
            return;
        }
        this.ringtoneUploaderHashMap.put(str, new RingtoneUploader(str, this.currentAccount));
        this.ringtoneDataStore.addUploadingTone(str);
    }

    public void verifyAnimatedStickerMessage(TLRPC.Message message) {
        verifyAnimatedStickerMessage(message, false);
    }

    public void verifyAnimatedStickerMessage(final TLRPC.Message message, boolean z) {
        if (message == null) {
            return;
        }
        TLRPC.Document document = MessageObject.getDocument(message);
        final String stickerSetName = MessageObject.getStickerSetName(document);
        if (TextUtils.isEmpty(stickerSetName)) {
            return;
        }
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet = this.stickerSetsByName.get(stickerSetName);
        if (tL_messages_stickerSet == null) {
            if (z) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda200
                    @Override // java.lang.Runnable
                    public final void run() {
                        MediaDataController.this.lambda$verifyAnimatedStickerMessage$68(message, stickerSetName);
                    }
                });
                return;
            } else {
                lambda$verifyAnimatedStickerMessage$68(message, stickerSetName);
                return;
            }
        }
        int size = tL_messages_stickerSet.documents.size();
        for (int i = 0; i < size; i++) {
            TLRPC.Document document2 = tL_messages_stickerSet.documents.get(i);
            if (document2.id == document.id && document2.dc_id == document.dc_id) {
                message.stickerVerified = 1;
                return;
            }
        }
    }
}
