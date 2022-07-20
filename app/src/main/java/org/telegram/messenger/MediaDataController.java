package org.telegram.messenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutManager;
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
import android.util.SparseArray;
import androidx.collection.LongSparseArray;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
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
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLiteException;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.ringtone.RingtoneDataStore;
import org.telegram.messenger.ringtone.RingtoneUploader;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$AttachMenuBots;
import org.telegram.tgnet.TLRPC$AttachMenuPeerType;
import org.telegram.tgnet.TLRPC$BotInfo;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatPhoto;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$DraftMessage;
import org.telegram.tgnet.TLRPC$EmojiKeyword;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$InputUser;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$MessagesFilter;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_account_saveRingtone;
import org.telegram.tgnet.TLRPC$TL_account_savedRingtoneConverted;
import org.telegram.tgnet.TLRPC$TL_attachMenuBot;
import org.telegram.tgnet.TLRPC$TL_attachMenuBotIcon;
import org.telegram.tgnet.TLRPC$TL_attachMenuBots;
import org.telegram.tgnet.TLRPC$TL_attachMenuBotsNotModified;
import org.telegram.tgnet.TLRPC$TL_attachMenuPeerTypeBotPM;
import org.telegram.tgnet.TLRPC$TL_attachMenuPeerTypeBroadcast;
import org.telegram.tgnet.TLRPC$TL_attachMenuPeerTypeChat;
import org.telegram.tgnet.TLRPC$TL_attachMenuPeerTypePM;
import org.telegram.tgnet.TLRPC$TL_attachMenuPeerTypeSameBotPM;
import org.telegram.tgnet.TLRPC$TL_availableReaction;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_channels_getMessages;
import org.telegram.tgnet.TLRPC$TL_contacts_getTopPeers;
import org.telegram.tgnet.TLRPC$TL_contacts_resetTopPeerRating;
import org.telegram.tgnet.TLRPC$TL_contacts_topPeers;
import org.telegram.tgnet.TLRPC$TL_contacts_topPeersDisabled;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAnimated;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC$TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC$TL_documentEmpty;
import org.telegram.tgnet.TLRPC$TL_draftMessage;
import org.telegram.tgnet.TLRPC$TL_draftMessageEmpty;
import org.telegram.tgnet.TLRPC$TL_emojiKeyword;
import org.telegram.tgnet.TLRPC$TL_emojiKeywordDeleted;
import org.telegram.tgnet.TLRPC$TL_emojiKeywordsDifference;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_help_getPremiumPromo;
import org.telegram.tgnet.TLRPC$TL_help_premiumPromo;
import org.telegram.tgnet.TLRPC$TL_inputDocument;
import org.telegram.tgnet.TLRPC$TL_inputMessageEntityMentionName;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterDocument;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterEmpty;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterGif;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterMusic;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterPhotoVideo;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterPhotos;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterPinned;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterRoundVoice;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterUrl;
import org.telegram.tgnet.TLRPC$TL_inputMessagesFilterVideo;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetAnimatedEmoji;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetDice;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetEmpty;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageActionGameScore;
import org.telegram.tgnet.TLRPC$TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent;
import org.telegram.tgnet.TLRPC$TL_messageActionPinMessage;
import org.telegram.tgnet.TLRPC$TL_messageEmpty;
import org.telegram.tgnet.TLRPC$TL_messageEntityBlockquote;
import org.telegram.tgnet.TLRPC$TL_messageEntityBold;
import org.telegram.tgnet.TLRPC$TL_messageEntityCode;
import org.telegram.tgnet.TLRPC$TL_messageEntityEmail;
import org.telegram.tgnet.TLRPC$TL_messageEntityItalic;
import org.telegram.tgnet.TLRPC$TL_messageEntityMentionName;
import org.telegram.tgnet.TLRPC$TL_messageEntityPre;
import org.telegram.tgnet.TLRPC$TL_messageEntitySpoiler;
import org.telegram.tgnet.TLRPC$TL_messageEntityStrike;
import org.telegram.tgnet.TLRPC$TL_messageEntityTextUrl;
import org.telegram.tgnet.TLRPC$TL_messageEntityUnderline;
import org.telegram.tgnet.TLRPC$TL_messageEntityUrl;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageReplyHeader;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_message_secret;
import org.telegram.tgnet.TLRPC$TL_messages_allStickers;
import org.telegram.tgnet.TLRPC$TL_messages_archivedStickers;
import org.telegram.tgnet.TLRPC$TL_messages_availableReactions;
import org.telegram.tgnet.TLRPC$TL_messages_availableReactionsNotModified;
import org.telegram.tgnet.TLRPC$TL_messages_channelMessages;
import org.telegram.tgnet.TLRPC$TL_messages_clearRecentStickers;
import org.telegram.tgnet.TLRPC$TL_messages_faveSticker;
import org.telegram.tgnet.TLRPC$TL_messages_favedStickers;
import org.telegram.tgnet.TLRPC$TL_messages_featuredStickers;
import org.telegram.tgnet.TLRPC$TL_messages_getAllDrafts;
import org.telegram.tgnet.TLRPC$TL_messages_getAllStickers;
import org.telegram.tgnet.TLRPC$TL_messages_getArchivedStickers;
import org.telegram.tgnet.TLRPC$TL_messages_getAttachMenuBots;
import org.telegram.tgnet.TLRPC$TL_messages_getAvailableReactions;
import org.telegram.tgnet.TLRPC$TL_messages_getEmojiKeywords;
import org.telegram.tgnet.TLRPC$TL_messages_getEmojiKeywordsDifference;
import org.telegram.tgnet.TLRPC$TL_messages_getFeaturedStickers;
import org.telegram.tgnet.TLRPC$TL_messages_getMaskStickers;
import org.telegram.tgnet.TLRPC$TL_messages_getMessages;
import org.telegram.tgnet.TLRPC$TL_messages_getScheduledMessages;
import org.telegram.tgnet.TLRPC$TL_messages_getSearchCounters;
import org.telegram.tgnet.TLRPC$TL_messages_getStickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_getStickers;
import org.telegram.tgnet.TLRPC$TL_messages_installStickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_messages;
import org.telegram.tgnet.TLRPC$TL_messages_messagesSlice;
import org.telegram.tgnet.TLRPC$TL_messages_readFeaturedStickers;
import org.telegram.tgnet.TLRPC$TL_messages_recentStickers;
import org.telegram.tgnet.TLRPC$TL_messages_saveDraft;
import org.telegram.tgnet.TLRPC$TL_messages_saveGif;
import org.telegram.tgnet.TLRPC$TL_messages_saveRecentSticker;
import org.telegram.tgnet.TLRPC$TL_messages_savedGifs;
import org.telegram.tgnet.TLRPC$TL_messages_search;
import org.telegram.tgnet.TLRPC$TL_messages_searchCounter;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSetInstallResultArchive;
import org.telegram.tgnet.TLRPC$TL_messages_stickers;
import org.telegram.tgnet.TLRPC$TL_messages_toggleStickerSets;
import org.telegram.tgnet.TLRPC$TL_messages_uninstallStickerSet;
import org.telegram.tgnet.TLRPC$TL_peerChat;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_stickerPack;
import org.telegram.tgnet.TLRPC$TL_theme;
import org.telegram.tgnet.TLRPC$TL_topPeer;
import org.telegram.tgnet.TLRPC$TL_topPeerCategoryBotsInline;
import org.telegram.tgnet.TLRPC$TL_topPeerCategoryCorrespondents;
import org.telegram.tgnet.TLRPC$TL_topPeerCategoryPeers;
import org.telegram.tgnet.TLRPC$TL_updateBotCommands;
import org.telegram.tgnet.TLRPC$Theme;
import org.telegram.tgnet.TLRPC$Updates;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserProfilePhoto;
import org.telegram.tgnet.TLRPC$Vector;
import org.telegram.tgnet.TLRPC$messages_Messages;
import org.telegram.tgnet.TLRPC$messages_StickerSet;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.ChatThemeBottomSheet;
import org.telegram.ui.Components.Reactions.ReactionsEffectOverlay;
import org.telegram.ui.Components.StickerSetBulletinLayout;
import org.telegram.ui.Components.StickersArchiveAlert;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes.dex */
public class MediaDataController extends BaseController {
    public static final String ATTACH_MENU_BOT_ANIMATED_ICON_KEY = "android_animated";
    public static final String ATTACH_MENU_BOT_COLOR_DARK_ICON = "dark_icon";
    public static final String ATTACH_MENU_BOT_COLOR_DARK_TEXT = "dark_text";
    public static final String ATTACH_MENU_BOT_COLOR_LIGHT_ICON = "light_icon";
    public static final String ATTACH_MENU_BOT_COLOR_LIGHT_TEXT = "light_text";
    public static final String ATTACH_MENU_BOT_PLACEHOLDER_STATIC_KEY = "placeholder_static";
    public static final String ATTACH_MENU_BOT_STATIC_ICON_KEY = "default_static";
    public static final int MEDIA_AUDIO = 2;
    public static final int MEDIA_FILE = 1;
    public static final int MEDIA_GIF = 5;
    public static final int MEDIA_MUSIC = 4;
    public static final int MEDIA_PHOTOS_ONLY = 6;
    public static final int MEDIA_PHOTOVIDEO = 0;
    public static final int MEDIA_TYPES_COUNT = 8;
    public static final int MEDIA_URL = 3;
    public static final int MEDIA_VIDEOS_ONLY = 7;
    public static final int TYPE_EMOJI = 4;
    public static final int TYPE_FAVE = 2;
    public static final int TYPE_FEATURED = 3;
    public static final int TYPE_GREETINGS = 3;
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_MASK = 1;
    private static RectF bitmapRect;
    private static Comparator<TLRPC$MessageEntity> entityComparator;
    private static Paint erasePaint;
    private static Paint roundPaint;
    private static Path roundPath;
    private String doubleTapReaction;
    private SharedPreferences draftPreferences;
    private boolean featuredStickersLoaded;
    private TLRPC$Document greetingsSticker;
    private boolean inTransaction;
    private boolean isLoadingMenuBots;
    private boolean isLoadingPremiumPromo;
    private boolean isLoadingReactions;
    private long lastDialogId;
    private int lastGuid;
    private long lastMergeDialogId;
    private int lastReplyMessageId;
    private int lastReqId;
    private int lastReturnedNum;
    private TLRPC$Chat lastSearchChat;
    private String lastSearchQuery;
    private TLRPC$User lastSearchUser;
    private int loadFeaturedDate;
    private long loadFeaturedHash;
    public boolean loadFeaturedPremium;
    boolean loaded;
    boolean loading;
    private boolean loadingDrafts;
    private boolean loadingFeaturedStickers;
    private boolean loadingMoreSearchMessages;
    private boolean loadingRecentGifs;
    private int menuBotsUpdateDate;
    private long menuBotsUpdateHash;
    private int mergeReqId;
    private TLRPC$TL_help_premiumPromo premiumPromo;
    private int premiumPromoUpdateDate;
    boolean previewStickersLoading;
    private int reactionsUpdateDate;
    private int reactionsUpdateHash;
    private boolean recentGifsLoaded;
    private int reqId;
    public final RingtoneDataStore ringtoneDataStore;
    private static Pattern BOLD_PATTERN = Pattern.compile("\\*\\*(.+?)\\*\\*");
    private static Pattern ITALIC_PATTERN = Pattern.compile("__(.+?)__");
    private static Pattern SPOILER_PATTERN = Pattern.compile("\\|\\|(.+?)\\|\\|");
    private static Pattern STRIKE_PATTERN = Pattern.compile("~~(.+?)~~");
    public static String SHORTCUT_CATEGORY = "org.telegram.messenger.SHORTCUT_SHARE";
    private static volatile MediaDataController[] Instance = new MediaDataController[4];
    private static final Object[] lockObjects = new Object[4];
    private TLRPC$TL_attachMenuBots attachMenuBots = new TLRPC$TL_attachMenuBots();
    private List<TLRPC$TL_availableReaction> reactionsList = new ArrayList();
    private List<TLRPC$TL_availableReaction> enabledReactionsList = new ArrayList();
    private HashMap<String, TLRPC$TL_availableReaction> reactionsMap = new HashMap<>();
    private ArrayList<TLRPC$TL_messages_stickerSet>[] stickerSets = {new ArrayList<>(), new ArrayList<>(), new ArrayList<>(0), new ArrayList<>(), new ArrayList<>()};
    private LongSparseArray<TLRPC$Document>[] stickersByIds = {new LongSparseArray<>(), new LongSparseArray<>(), new LongSparseArray<>(), new LongSparseArray<>(), new LongSparseArray<>()};
    private LongSparseArray<TLRPC$TL_messages_stickerSet> stickerSetsById = new LongSparseArray<>();
    private LongSparseArray<TLRPC$TL_messages_stickerSet> installedStickerSetsById = new LongSparseArray<>();
    private LongSparseArray<TLRPC$TL_messages_stickerSet> groupStickerSets = new LongSparseArray<>();
    private ConcurrentHashMap<String, TLRPC$TL_messages_stickerSet> stickerSetsByName = new ConcurrentHashMap<>(100, 1.0f, 1);
    private HashMap<String, TLRPC$TL_messages_stickerSet> diceStickerSetsByEmoji = new HashMap<>();
    private LongSparseArray<String> diceEmojiStickerSetsById = new LongSparseArray<>();
    private HashSet<String> loadingDiceStickerSets = new HashSet<>();
    private LongSparseArray<Runnable> removingStickerSetsUndos = new LongSparseArray<>();
    private Runnable[] scheduledLoadStickers = new Runnable[5];
    private boolean[] loadingStickers = new boolean[5];
    private boolean[] stickersLoaded = new boolean[5];
    private long[] loadHash = new long[5];
    private int[] loadDate = new int[5];
    public HashMap<String, RingtoneUploader> ringtoneUploaderHashMap = new HashMap<>();
    private HashMap<String, ArrayList<TLRPC$Message>> verifyingMessages = new HashMap<>();
    private int[] archivedStickersCount = new int[2];
    private LongSparseArray<String> stickersByEmoji = new LongSparseArray<>();
    private HashMap<String, ArrayList<TLRPC$Document>> allStickers = new HashMap<>();
    private HashMap<String, ArrayList<TLRPC$Document>> allStickersFeatured = new HashMap<>();
    private ArrayList<TLRPC$Document>[] recentStickers = {new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()};
    private boolean[] loadingRecentStickers = new boolean[4];
    private boolean[] recentStickersLoaded = new boolean[4];
    private ArrayList<TLRPC$Document> recentGifs = new ArrayList<>();
    private ArrayList<TLRPC$StickerSetCovered> featuredStickerSets = new ArrayList<>();
    private LongSparseArray<TLRPC$StickerSetCovered> featuredStickerSetsById = new LongSparseArray<>();
    private ArrayList<Long> unreadStickerSets = new ArrayList<>();
    private ArrayList<Long> readingStickerSets = new ArrayList<>();
    public final ArrayList<ChatThemeBottomSheet.ChatThemeItem> defaultEmojiThemes = new ArrayList<>();
    public final ArrayList<TLRPC$Document> premiumPreviewStickers = new ArrayList<>();
    private int[] messagesSearchCount = {0, 0};
    private boolean[] messagesSearchEndReached = {false, false};
    private ArrayList<MessageObject> searchResultMessages = new ArrayList<>();
    private SparseArray<MessageObject>[] searchResultMessagesMap = {new SparseArray<>(), new SparseArray<>()};
    public ArrayList<TLRPC$TL_topPeer> hints = new ArrayList<>();
    public ArrayList<TLRPC$TL_topPeer> inlineBots = new ArrayList<>();
    private LongSparseArray<Boolean> loadingPinnedMessages = new LongSparseArray<>();
    private LongSparseArray<Integer> draftsFolderIds = new LongSparseArray<>();
    private LongSparseArray<SparseArray<TLRPC$DraftMessage>> drafts = new LongSparseArray<>();
    private LongSparseArray<SparseArray<TLRPC$Message>> draftMessages = new LongSparseArray<>();
    private HashMap<String, TLRPC$BotInfo> botInfos = new HashMap<>();
    private LongSparseArray<TLRPC$Message> botKeyboards = new LongSparseArray<>();
    private SparseLongArray botKeyboardsByMids = new SparseLongArray();
    private HashMap<String, Boolean> currentFetchingEmoji = new HashMap<>();

    /* loaded from: classes.dex */
    public static class KeywordResult {
        public String emoji;
        public String keyword;
    }

    /* loaded from: classes.dex */
    public interface KeywordResultCallback {
        void run(ArrayList<KeywordResult> arrayList, String str);
    }

    public static long calcHash(long j, long j2) {
        return (((j ^ (j2 >> 21)) ^ (j2 << 35)) ^ (j2 >> 4)) + j2;
    }

    public static /* synthetic */ void lambda$markFaturedStickersAsRead$47(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public static /* synthetic */ void lambda$markFaturedStickersByIdAsRead$48(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public static /* synthetic */ void lambda$removeInline$113(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public static /* synthetic */ void lambda$removePeer$114(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public static /* synthetic */ void lambda$saveDraft$146(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    static {
        for (int i = 0; i < 4; i++) {
            lockObjects[i] = new Object();
        }
        entityComparator = MediaDataController$$ExternalSyntheticLambda126.INSTANCE;
    }

    public static MediaDataController getInstance(int i) {
        MediaDataController mediaDataController = Instance[i];
        if (mediaDataController == null) {
            synchronized (lockObjects) {
                mediaDataController = Instance[i];
                if (mediaDataController == null) {
                    MediaDataController[] mediaDataControllerArr = Instance;
                    MediaDataController mediaDataController2 = new MediaDataController(i);
                    mediaDataControllerArr[i] = mediaDataController2;
                    mediaDataController = mediaDataController2;
                }
            }
        }
        return mediaDataController;
    }

    public MediaDataController(int i) {
        super(i);
        String key;
        long longValue;
        SerializedData serializedData;
        boolean z;
        if (this.currentAccount == 0) {
            this.draftPreferences = ApplicationLoader.applicationContext.getSharedPreferences("drafts", 0);
        } else {
            Context context = ApplicationLoader.applicationContext;
            this.draftPreferences = context.getSharedPreferences("drafts" + this.currentAccount, 0);
        }
        for (Map.Entry<String, ?> entry : this.draftPreferences.getAll().entrySet()) {
            try {
                key = entry.getKey();
                longValue = Utilities.parseLong(key).longValue();
                serializedData = new SerializedData(Utilities.hexToBytes((String) entry.getValue()));
            } catch (Exception unused) {
            }
            if (!key.startsWith("r_")) {
                z = key.startsWith("rt_");
                if (!z) {
                    TLRPC$DraftMessage TLdeserialize = TLRPC$DraftMessage.TLdeserialize(serializedData, serializedData.readInt32(true), true);
                    if (TLdeserialize != null) {
                        SparseArray<TLRPC$DraftMessage> sparseArray = this.drafts.get(longValue);
                        if (sparseArray == null) {
                            sparseArray = new SparseArray<>();
                            this.drafts.put(longValue, sparseArray);
                        }
                        sparseArray.put(key.startsWith("t_") ? Utilities.parseInt((CharSequence) key.substring(key.lastIndexOf(95) + 1)).intValue() : 0, TLdeserialize);
                    }
                    serializedData.cleanup();
                }
            } else {
                z = false;
            }
            TLRPC$Message TLdeserialize2 = TLRPC$Message.TLdeserialize(serializedData, serializedData.readInt32(true), true);
            if (TLdeserialize2 != null) {
                TLdeserialize2.readAttachPath(serializedData, getUserConfig().clientUserId);
                SparseArray<TLRPC$Message> sparseArray2 = this.draftMessages.get(longValue);
                if (sparseArray2 == null) {
                    sparseArray2 = new SparseArray<>();
                    this.draftMessages.put(longValue, sparseArray2);
                }
                sparseArray2.put(z ? Utilities.parseInt((CharSequence) key.substring(key.lastIndexOf(95) + 1)).intValue() : 0, TLdeserialize2);
            }
            serializedData.cleanup();
        }
        loadStickersByEmojiOrName("tg_placeholders_android", false, true);
        loadEmojiThemes();
        this.ringtoneDataStore = new RingtoneDataStore(this.currentAccount);
    }

    public void cleanup() {
        int i = 0;
        while (true) {
            ArrayList<TLRPC$Document>[] arrayListArr = this.recentStickers;
            if (i >= arrayListArr.length) {
                break;
            }
            arrayListArr[i].clear();
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
        this.loadFeaturedDate = 0;
        this.loadFeaturedHash = 0L;
        this.allStickers.clear();
        this.allStickersFeatured.clear();
        this.stickersByEmoji.clear();
        this.featuredStickerSetsById.clear();
        this.featuredStickerSets.clear();
        this.unreadStickerSets.clear();
        this.recentGifs.clear();
        this.stickerSetsById.clear();
        this.installedStickerSetsById.clear();
        this.stickerSetsByName.clear();
        this.diceStickerSetsByEmoji.clear();
        this.diceEmojiStickerSetsById.clear();
        this.loadingDiceStickerSets.clear();
        this.loadingFeaturedStickers = false;
        this.featuredStickersLoaded = false;
        this.loadingRecentGifs = false;
        this.recentGifsLoaded = false;
        this.currentFetchingEmoji.clear();
        if (Build.VERSION.SDK_INT >= 25) {
            Utilities.globalQueue.postRunnable(MediaDataController$$ExternalSyntheticLambda122.INSTANCE);
        }
        this.verifyingMessages.clear();
        this.loading = false;
        this.loaded = false;
        this.hints.clear();
        this.inlineBots.clear();
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda3(this));
        this.drafts.clear();
        this.draftMessages.clear();
        this.draftPreferences.edit().clear().apply();
        this.botInfos.clear();
        this.botKeyboards.clear();
        this.botKeyboardsByMids.clear();
    }

    public static /* synthetic */ void lambda$cleanup$0() {
        try {
            ShortcutManagerCompat.removeAllDynamicShortcuts(ApplicationLoader.applicationContext);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$cleanup$1() {
        getNotificationCenter().postNotificationName(NotificationCenter.reloadHints, new Object[0]);
        getNotificationCenter().postNotificationName(NotificationCenter.reloadInlineHints, new Object[0]);
    }

    public void checkStickers(int i) {
        if (!this.loadingStickers[i]) {
            if (this.stickersLoaded[i] && Math.abs((System.currentTimeMillis() / 1000) - this.loadDate[i]) < 3600) {
                return;
            }
            loadStickers(i, true, false);
        }
    }

    public void checkReactions() {
        if (this.isLoadingReactions || Math.abs((System.currentTimeMillis() / 1000) - this.reactionsUpdateDate) < 3600) {
            return;
        }
        loadReactions(true, false);
    }

    public void checkMenuBots() {
        if (this.isLoadingMenuBots || Math.abs((System.currentTimeMillis() / 1000) - this.menuBotsUpdateDate) < 3600) {
            return;
        }
        loadAttachMenuBots(true, false);
    }

    public void checkPremiumPromo() {
        if (this.isLoadingPremiumPromo || Math.abs((System.currentTimeMillis() / 1000) - this.premiumPromoUpdateDate) < 3600) {
            return;
        }
        loadPremiumPromo(true);
    }

    public TLRPC$TL_help_premiumPromo getPremiumPromo() {
        return this.premiumPromo;
    }

    public TLRPC$TL_attachMenuBots getAttachMenuBots() {
        return this.attachMenuBots;
    }

    public void loadAttachMenuBots(boolean z, boolean z2) {
        this.isLoadingMenuBots = true;
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda6(this));
            return;
        }
        TLRPC$TL_messages_getAttachMenuBots tLRPC$TL_messages_getAttachMenuBots = new TLRPC$TL_messages_getAttachMenuBots();
        tLRPC$TL_messages_getAttachMenuBots.hash = z2 ? 0L : this.menuBotsUpdateHash;
        getConnectionsManager().sendRequest(tLRPC$TL_messages_getAttachMenuBots, new MediaDataController$$ExternalSyntheticLambda136(this));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v5, types: [org.telegram.tgnet.TLRPC$TL_attachMenuBots] */
    public /* synthetic */ void lambda$loadAttachMenuBots$2() {
        SQLiteCursor sQLiteCursor;
        Throwable th;
        int i;
        long j;
        TLRPC$TL_attachMenuBots tLRPC$TL_attachMenuBots;
        Exception e;
        long j2;
        SQLiteCursor sQLiteCursor2;
        SQLiteCursor sQLiteCursor3 = null;
        int i2 = 0;
        long j3 = 0;
        try {
            try {
                sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT data, hash, date FROM attach_menu_bots", new Object[0]);
            } catch (Exception e2) {
                e = e2;
                j2 = 0;
                sQLiteCursor2 = null;
            }
        } catch (Throwable th2) {
            sQLiteCursor = sQLiteCursor3;
            th = th2;
        }
        try {
            if (sQLiteCursor.next()) {
                NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
                if (byteBufferValue != null) {
                    TLRPC$AttachMenuBots TLdeserialize = TLRPC$AttachMenuBots.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), true);
                    if (TLdeserialize instanceof TLRPC$TL_attachMenuBots) {
                        sQLiteCursor3 = (TLRPC$TL_attachMenuBots) TLdeserialize;
                    }
                    byteBufferValue.reuse();
                }
                j3 = sQLiteCursor.longValue(1);
                i2 = sQLiteCursor.intValue(2);
            }
            sQLiteCursor.dispose();
            tLRPC$TL_attachMenuBots = sQLiteCursor3;
            i = i2;
            j = j3;
        } catch (Exception e3) {
            e = e3;
            long j4 = j3;
            sQLiteCursor2 = sQLiteCursor3;
            sQLiteCursor3 = sQLiteCursor;
            j2 = j4;
            FileLog.e((Throwable) e, false);
            if (sQLiteCursor3 != null) {
                sQLiteCursor3.dispose();
            }
            tLRPC$TL_attachMenuBots = sQLiteCursor2;
            j = j2;
            i = 0;
            processLoadedMenuBots(tLRPC$TL_attachMenuBots, j, i, true);
        } catch (Throwable th3) {
            th = th3;
            if (sQLiteCursor != null) {
                sQLiteCursor.dispose();
            }
            throw th;
        }
        processLoadedMenuBots(tLRPC$TL_attachMenuBots, j, i, true);
    }

    public /* synthetic */ void lambda$loadAttachMenuBots$3(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (tLObject instanceof TLRPC$TL_attachMenuBotsNotModified) {
            processLoadedMenuBots(null, 0L, currentTimeMillis, false);
        } else if (!(tLObject instanceof TLRPC$TL_attachMenuBots)) {
        } else {
            TLRPC$TL_attachMenuBots tLRPC$TL_attachMenuBots = (TLRPC$TL_attachMenuBots) tLObject;
            processLoadedMenuBots(tLRPC$TL_attachMenuBots, tLRPC$TL_attachMenuBots.hash, currentTimeMillis, false);
        }
    }

    public void processLoadedMenuBots(TLRPC$TL_attachMenuBots tLRPC$TL_attachMenuBots, long j, int i, boolean z) {
        if (tLRPC$TL_attachMenuBots != null && i != 0) {
            this.attachMenuBots = tLRPC$TL_attachMenuBots;
            this.menuBotsUpdateHash = j;
        }
        this.menuBotsUpdateDate = i;
        if (tLRPC$TL_attachMenuBots != null) {
            getMessagesController().putUsers(tLRPC$TL_attachMenuBots.users, z);
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda12(this));
        }
        if (!z) {
            putMenuBotsToCache(tLRPC$TL_attachMenuBots, j, i);
        } else if (Math.abs((System.currentTimeMillis() / 1000) - i) < 3600) {
        } else {
            loadAttachMenuBots(false, true);
        }
    }

    public /* synthetic */ void lambda$processLoadedMenuBots$4() {
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.attachMenuBotsDidLoad, new Object[0]);
    }

    private void putMenuBotsToCache(TLRPC$TL_attachMenuBots tLRPC$TL_attachMenuBots, long j, int i) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda95(this, tLRPC$TL_attachMenuBots, j, i));
    }

    public /* synthetic */ void lambda$putMenuBotsToCache$5(TLRPC$TL_attachMenuBots tLRPC$TL_attachMenuBots, long j, int i) {
        try {
            if (tLRPC$TL_attachMenuBots != null) {
                getMessagesStorage().getDatabase().executeFast("DELETE FROM attach_menu_bots").stepThis().dispose();
                SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO attach_menu_bots VALUES(?, ?, ?)");
                executeFast.requery();
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tLRPC$TL_attachMenuBots.getObjectSize());
                tLRPC$TL_attachMenuBots.serializeToStream(nativeByteBuffer);
                executeFast.bindByteBuffer(1, nativeByteBuffer);
                executeFast.bindLong(2, j);
                executeFast.bindInteger(3, i);
                executeFast.step();
                nativeByteBuffer.reuse();
                executeFast.dispose();
            } else {
                SQLitePreparedStatement executeFast2 = getMessagesStorage().getDatabase().executeFast("UPDATE attach_menu_bots SET date = ?");
                executeFast2.requery();
                executeFast2.bindLong(1, i);
                executeFast2.step();
                executeFast2.dispose();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void loadPremiumPromo(boolean z) {
        this.isLoadingPremiumPromo = true;
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda15(this));
            return;
        }
        getConnectionsManager().sendRequest(new TLRPC$TL_help_getPremiumPromo(), new MediaDataController$$ExternalSyntheticLambda135(this));
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadPremiumPromo$6() {
        SQLiteCursor sQLiteCursor;
        Throwable th;
        Exception e;
        TLRPC$TL_help_premiumPromo tLRPC$TL_help_premiumPromo;
        SQLiteCursor sQLiteCursor2 = null;
        r0 = null;
        r0 = null;
        TLRPC$TL_help_premiumPromo tLRPC$TL_help_premiumPromo2 = null;
        sQLiteCursor2 = null;
        int i = 0;
        try {
            try {
                sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT data, date FROM premium_promo", new Object[0]);
            } catch (Exception e2) {
                e = e2;
                tLRPC$TL_help_premiumPromo = null;
            }
        } catch (Throwable th2) {
            sQLiteCursor = sQLiteCursor2;
            th = th2;
        }
        try {
            if (sQLiteCursor.next()) {
                NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
                if (byteBufferValue != null) {
                    tLRPC$TL_help_premiumPromo2 = TLRPC$TL_help_premiumPromo.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), true);
                    byteBufferValue.reuse();
                }
                i = sQLiteCursor.intValue(1);
            }
            sQLiteCursor.dispose();
        } catch (Exception e3) {
            e = e3;
            tLRPC$TL_help_premiumPromo = tLRPC$TL_help_premiumPromo2;
            sQLiteCursor2 = sQLiteCursor;
            FileLog.e((Throwable) e, false);
            if (sQLiteCursor2 != null) {
                sQLiteCursor2.dispose();
            }
            tLRPC$TL_help_premiumPromo2 = tLRPC$TL_help_premiumPromo;
            if (tLRPC$TL_help_premiumPromo2 == null) {
            }
        } catch (Throwable th3) {
            th = th3;
            if (sQLiteCursor != null) {
                sQLiteCursor.dispose();
            }
            throw th;
        }
        if (tLRPC$TL_help_premiumPromo2 == null) {
            processLoadedPremiumPromo(tLRPC$TL_help_premiumPromo2, i, true);
        }
    }

    public /* synthetic */ void lambda$loadPremiumPromo$7(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (tLObject instanceof TLRPC$TL_help_premiumPromo) {
            processLoadedPremiumPromo((TLRPC$TL_help_premiumPromo) tLObject, currentTimeMillis, false);
        }
    }

    private void processLoadedPremiumPromo(TLRPC$TL_help_premiumPromo tLRPC$TL_help_premiumPromo, int i, boolean z) {
        this.premiumPromo = tLRPC$TL_help_premiumPromo;
        this.premiumPromoUpdateDate = i;
        getMessagesController().putUsers(tLRPC$TL_help_premiumPromo.users, z);
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda7(this));
        if (!z) {
            putPremiumPromoToCache(tLRPC$TL_help_premiumPromo, i);
        } else if (Math.abs((System.currentTimeMillis() / 1000) - i) < 86400 && !BuildVars.DEBUG_PRIVATE_VERSION) {
        } else {
            loadPremiumPromo(false);
        }
    }

    public /* synthetic */ void lambda$processLoadedPremiumPromo$8() {
        getNotificationCenter().postNotificationName(NotificationCenter.premiumPromoUpdated, new Object[0]);
    }

    private void putPremiumPromoToCache(TLRPC$TL_help_premiumPromo tLRPC$TL_help_premiumPromo, int i) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda101(this, tLRPC$TL_help_premiumPromo, i));
    }

    public /* synthetic */ void lambda$putPremiumPromoToCache$9(TLRPC$TL_help_premiumPromo tLRPC$TL_help_premiumPromo, int i) {
        try {
            if (tLRPC$TL_help_premiumPromo != null) {
                getMessagesStorage().getDatabase().executeFast("DELETE FROM premium_promo").stepThis().dispose();
                SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO premium_promo VALUES(?, ?)");
                executeFast.requery();
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tLRPC$TL_help_premiumPromo.getObjectSize());
                tLRPC$TL_help_premiumPromo.serializeToStream(nativeByteBuffer);
                executeFast.bindByteBuffer(1, nativeByteBuffer);
                executeFast.bindInteger(2, i);
                executeFast.step();
                nativeByteBuffer.reuse();
                executeFast.dispose();
            } else {
                SQLitePreparedStatement executeFast2 = getMessagesStorage().getDatabase().executeFast("UPDATE premium_promo SET date = ?");
                executeFast2.requery();
                executeFast2.bindInteger(1, i);
                executeFast2.step();
                executeFast2.dispose();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public List<TLRPC$TL_availableReaction> getReactionsList() {
        return this.reactionsList;
    }

    public void loadReactions(boolean z, boolean z2) {
        this.isLoadingReactions = true;
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda16(this));
            return;
        }
        TLRPC$TL_messages_getAvailableReactions tLRPC$TL_messages_getAvailableReactions = new TLRPC$TL_messages_getAvailableReactions();
        tLRPC$TL_messages_getAvailableReactions.hash = z2 ? 0 : this.reactionsUpdateHash;
        getConnectionsManager().sendRequest(tLRPC$TL_messages_getAvailableReactions, new MediaDataController$$ExternalSyntheticLambda138(this));
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0076  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadReactions$10() {
        SQLiteCursor sQLiteCursor;
        Throwable th;
        int i;
        ArrayList arrayList;
        int i2;
        Exception e;
        ArrayList arrayList2 = null;
        int i3 = 0;
        try {
            sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT data, hash, date FROM reactions", new Object[0]);
            try {
                try {
                    if (sQLiteCursor.next()) {
                        NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
                        if (byteBufferValue != null) {
                            int readInt32 = byteBufferValue.readInt32(false);
                            arrayList = new ArrayList(readInt32);
                            for (int i4 = 0; i4 < readInt32; i4++) {
                                try {
                                    arrayList.add(TLRPC$TL_availableReaction.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), true));
                                } catch (Exception e2) {
                                    e = e2;
                                    i2 = 0;
                                    FileLog.e((Throwable) e, false);
                                    if (sQLiteCursor != null) {
                                        sQLiteCursor.dispose();
                                    }
                                    i3 = i2;
                                    arrayList2 = arrayList;
                                    i = 0;
                                    processLoadedReactions(arrayList2, i3, i, true);
                                }
                            }
                            byteBufferValue.reuse();
                            arrayList2 = arrayList;
                        }
                        i2 = sQLiteCursor.intValue(1);
                        try {
                            i = sQLiteCursor.intValue(2);
                            i3 = i2;
                        } catch (Exception e3) {
                            arrayList = arrayList2;
                            e = e3;
                            FileLog.e((Throwable) e, false);
                            if (sQLiteCursor != null) {
                            }
                            i3 = i2;
                            arrayList2 = arrayList;
                            i = 0;
                            processLoadedReactions(arrayList2, i3, i, true);
                        }
                    } else {
                        i = 0;
                    }
                    sQLiteCursor.dispose();
                } catch (Throwable th2) {
                    th = th2;
                    if (sQLiteCursor != null) {
                        sQLiteCursor.dispose();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                arrayList = arrayList2;
                e = e4;
            }
        } catch (Exception e5) {
            arrayList = null;
            i2 = 0;
            e = e5;
            sQLiteCursor = null;
        } catch (Throwable th3) {
            sQLiteCursor = null;
            th = th3;
            if (sQLiteCursor != null) {
            }
            throw th;
        }
        processLoadedReactions(arrayList2, i3, i, true);
    }

    public /* synthetic */ void lambda$loadReactions$11(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        if (tLObject instanceof TLRPC$TL_messages_availableReactionsNotModified) {
            processLoadedReactions(null, 0, currentTimeMillis, false);
        } else if (!(tLObject instanceof TLRPC$TL_messages_availableReactions)) {
        } else {
            TLRPC$TL_messages_availableReactions tLRPC$TL_messages_availableReactions = (TLRPC$TL_messages_availableReactions) tLObject;
            processLoadedReactions(tLRPC$TL_messages_availableReactions.reactions, tLRPC$TL_messages_availableReactions.hash, currentTimeMillis, false);
        }
    }

    public void processLoadedReactions(List<TLRPC$TL_availableReaction> list, int i, int i2, boolean z) {
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
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda0(list));
        }
        this.isLoadingReactions = false;
        if (!z) {
            putReactionsToCache(list, i, i2);
        } else if (Math.abs((System.currentTimeMillis() / 1000) - i2) >= 3600) {
            loadReactions(false, true);
        }
    }

    public static /* synthetic */ void lambda$processLoadedReactions$12(List list) {
        for (int i = 0; i < list.size(); i++) {
            ImageReceiver imageReceiver = new ImageReceiver();
            TLRPC$TL_availableReaction tLRPC$TL_availableReaction = (TLRPC$TL_availableReaction) list.get(i);
            imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.activate_animation), null, null, null, 0, 11);
            ImageLoader.getInstance().loadImageForImageReceiver(imageReceiver);
            ImageReceiver imageReceiver2 = new ImageReceiver();
            imageReceiver2.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.appear_animation), "60_60_nolimit", null, null, 0, 11);
            ImageLoader.getInstance().loadImageForImageReceiver(imageReceiver2);
            int sizeForBigReaction = ReactionsEffectOverlay.sizeForBigReaction();
            ImageReceiver imageReceiver3 = new ImageReceiver();
            ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$TL_availableReaction.around_animation);
            imageReceiver3.setImage(forDocument, sizeForBigReaction + "_" + sizeForBigReaction, null, null, 0, 11);
            ImageLoader.getInstance().loadImageForImageReceiver(imageReceiver3);
            ImageReceiver imageReceiver4 = new ImageReceiver();
            imageReceiver4.setImage(ImageLocation.getForDocument(tLRPC$TL_availableReaction.center_icon), null, null, null, 0, 11);
            ImageLoader.getInstance().loadImageForImageReceiver(imageReceiver4);
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.reactionsDidLoad, new Object[0]);
    }

    private void putReactionsToCache(List<TLRPC$TL_availableReaction> list, int i, int i2) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda66(this, list != null ? new ArrayList(list) : null, i, i2));
    }

    public /* synthetic */ void lambda$putReactionsToCache$13(ArrayList arrayList, int i, int i2) {
        try {
            if (arrayList != null) {
                getMessagesStorage().getDatabase().executeFast("DELETE FROM reactions").stepThis().dispose();
                SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO reactions VALUES(?, ?, ?)");
                executeFast.requery();
                int i3 = 4;
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    i3 += ((TLRPC$TL_availableReaction) arrayList.get(i4)).getObjectSize();
                }
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(i3);
                nativeByteBuffer.writeInt32(arrayList.size());
                for (int i5 = 0; i5 < arrayList.size(); i5++) {
                    ((TLRPC$TL_availableReaction) arrayList.get(i5)).serializeToStream(nativeByteBuffer);
                }
                executeFast.bindByteBuffer(1, nativeByteBuffer);
                executeFast.bindInteger(2, i);
                executeFast.bindInteger(3, i2);
                executeFast.step();
                nativeByteBuffer.reuse();
                executeFast.dispose();
                return;
            }
            SQLitePreparedStatement executeFast2 = getMessagesStorage().getDatabase().executeFast("UPDATE reactions SET date = ?");
            executeFast2.requery();
            executeFast2.bindLong(1, i2);
            executeFast2.step();
            executeFast2.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void checkFeaturedStickers() {
        if (!this.loadingFeaturedStickers) {
            if (this.featuredStickersLoaded && Math.abs((System.currentTimeMillis() / 1000) - this.loadFeaturedDate) < 3600) {
                return;
            }
            loadFeaturedStickers(true, false);
        }
    }

    public ArrayList<TLRPC$Document> getRecentStickers(int i) {
        ArrayList<TLRPC$Document> arrayList = this.recentStickers[i];
        return new ArrayList<>(arrayList.subList(0, Math.min(arrayList.size(), 20)));
    }

    public ArrayList<TLRPC$Document> getRecentStickersNoCopy(int i) {
        return this.recentStickers[i];
    }

    public boolean isStickerInFavorites(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return false;
        }
        for (int i = 0; i < this.recentStickers[2].size(); i++) {
            TLRPC$Document tLRPC$Document2 = this.recentStickers[2].get(i);
            if (tLRPC$Document2.id == tLRPC$Document.id && tLRPC$Document2.dc_id == tLRPC$Document.dc_id) {
                return true;
            }
        }
        return false;
    }

    public void clearRecentStickers() {
        getConnectionsManager().sendRequest(new TLRPC$TL_messages_clearRecentStickers(), new MediaDataController$$ExternalSyntheticLambda134(this));
    }

    public /* synthetic */ void lambda$clearRecentStickers$16(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda80(this, tLObject));
    }

    public /* synthetic */ void lambda$clearRecentStickers$15(TLObject tLObject) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda8(this));
            this.recentStickers[0].clear();
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.recentDocumentsDidLoad, Boolean.FALSE, 0);
        }
    }

    public /* synthetic */ void lambda$clearRecentStickers$14() {
        try {
            getMessagesStorage().getDatabase().executeFast("DELETE FROM web_recent_v3 WHERE type = 3").stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void addRecentSticker(int i, Object obj, TLRPC$Document tLRPC$Document, int i2, boolean z) {
        boolean z2;
        int i3;
        TLRPC$Document tLRPC$Document2;
        if (i != 3) {
            if (!MessageObject.isStickerDocument(tLRPC$Document) && !MessageObject.isAnimatedStickerDocument(tLRPC$Document, true)) {
                return;
            }
            int i4 = 0;
            while (true) {
                if (i4 >= this.recentStickers[i].size()) {
                    z2 = false;
                    break;
                }
                TLRPC$Document tLRPC$Document3 = this.recentStickers[i].get(i4);
                if (tLRPC$Document3.id == tLRPC$Document.id) {
                    this.recentStickers[i].remove(i4);
                    if (!z) {
                        this.recentStickers[i].add(0, tLRPC$Document3);
                    }
                    z2 = true;
                } else {
                    i4++;
                }
            }
            if (!z2 && !z) {
                this.recentStickers[i].add(0, tLRPC$Document);
            }
            if (i == 2) {
                if (z) {
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.showBulletin, 0, tLRPC$Document, 4);
                } else {
                    boolean z3 = this.recentStickers[i].size() > getMessagesController().maxFaveStickersCount;
                    NotificationCenter globalInstance = NotificationCenter.getGlobalInstance();
                    int i5 = NotificationCenter.showBulletin;
                    Object[] objArr = new Object[3];
                    objArr[0] = 0;
                    objArr[1] = tLRPC$Document;
                    objArr[2] = Integer.valueOf(z3 ? 6 : 5);
                    globalInstance.postNotificationName(i5, objArr);
                }
                TLRPC$TL_messages_faveSticker tLRPC$TL_messages_faveSticker = new TLRPC$TL_messages_faveSticker();
                TLRPC$TL_inputDocument tLRPC$TL_inputDocument = new TLRPC$TL_inputDocument();
                tLRPC$TL_messages_faveSticker.id = tLRPC$TL_inputDocument;
                tLRPC$TL_inputDocument.id = tLRPC$Document.id;
                tLRPC$TL_inputDocument.access_hash = tLRPC$Document.access_hash;
                byte[] bArr = tLRPC$Document.file_reference;
                tLRPC$TL_inputDocument.file_reference = bArr;
                if (bArr == null) {
                    tLRPC$TL_inputDocument.file_reference = new byte[0];
                }
                tLRPC$TL_messages_faveSticker.unfave = z;
                getConnectionsManager().sendRequest(tLRPC$TL_messages_faveSticker, new MediaDataController$$ExternalSyntheticLambda159(this, obj, tLRPC$TL_messages_faveSticker));
                i3 = getMessagesController().maxFaveStickersCount;
            } else {
                if (i == 0 && z) {
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.showBulletin, 0, tLRPC$Document, 3);
                    TLRPC$TL_messages_saveRecentSticker tLRPC$TL_messages_saveRecentSticker = new TLRPC$TL_messages_saveRecentSticker();
                    TLRPC$TL_inputDocument tLRPC$TL_inputDocument2 = new TLRPC$TL_inputDocument();
                    tLRPC$TL_messages_saveRecentSticker.id = tLRPC$TL_inputDocument2;
                    tLRPC$TL_inputDocument2.id = tLRPC$Document.id;
                    tLRPC$TL_inputDocument2.access_hash = tLRPC$Document.access_hash;
                    byte[] bArr2 = tLRPC$Document.file_reference;
                    tLRPC$TL_inputDocument2.file_reference = bArr2;
                    if (bArr2 == null) {
                        tLRPC$TL_inputDocument2.file_reference = new byte[0];
                    }
                    tLRPC$TL_messages_saveRecentSticker.unsave = true;
                    getConnectionsManager().sendRequest(tLRPC$TL_messages_saveRecentSticker, new MediaDataController$$ExternalSyntheticLambda160(this, obj, tLRPC$TL_messages_saveRecentSticker));
                }
                i3 = getMessagesController().maxRecentStickersCount;
            }
            if (this.recentStickers[i].size() > i3 || z) {
                if (z) {
                    tLRPC$Document2 = tLRPC$Document;
                } else {
                    ArrayList<TLRPC$Document>[] arrayListArr = this.recentStickers;
                    tLRPC$Document2 = arrayListArr[i].remove(arrayListArr[i].size() - 1);
                }
                getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda26(this, i, tLRPC$Document2));
            }
            if (!z) {
                ArrayList<TLRPC$Document> arrayList = new ArrayList<>();
                arrayList.add(tLRPC$Document);
                processLoadedRecentDocuments(i, arrayList, false, i2, false);
            }
            if (i != 2 && (i != 0 || !z)) {
                return;
            }
            getNotificationCenter().postNotificationName(NotificationCenter.recentDocumentsDidLoad, Boolean.FALSE, Integer.valueOf(i));
        }
    }

    public /* synthetic */ void lambda$addRecentSticker$18(Object obj, TLRPC$TL_messages_faveSticker tLRPC$TL_messages_faveSticker, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error != null && FileRefController.isFileRefError(tLRPC$TL_error.text) && obj != null) {
            getFileRefController().requestReference(obj, tLRPC$TL_messages_faveSticker);
        } else {
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda2(this));
        }
    }

    public /* synthetic */ void lambda$addRecentSticker$17() {
        getMediaDataController().loadRecents(2, false, false, true);
    }

    public /* synthetic */ void lambda$addRecentSticker$19(Object obj, TLRPC$TL_messages_saveRecentSticker tLRPC$TL_messages_saveRecentSticker, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null || !FileRefController.isFileRefError(tLRPC$TL_error.text) || obj == null) {
            return;
        }
        getFileRefController().requestReference(obj, tLRPC$TL_messages_saveRecentSticker);
    }

    public /* synthetic */ void lambda$addRecentSticker$20(int i, TLRPC$Document tLRPC$Document) {
        int i2 = i == 0 ? 3 : i == 1 ? 4 : 5;
        try {
            SQLiteDatabase database = getMessagesStorage().getDatabase();
            database.executeFast("DELETE FROM web_recent_v3 WHERE id = '" + tLRPC$Document.id + "' AND type = " + i2).stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public ArrayList<TLRPC$Document> getRecentGifs() {
        return new ArrayList<>(this.recentGifs);
    }

    public void removeRecentGif(TLRPC$Document tLRPC$Document) {
        int size = this.recentGifs.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            } else if (this.recentGifs.get(i).id == tLRPC$Document.id) {
                this.recentGifs.remove(i);
                break;
            } else {
                i++;
            }
        }
        TLRPC$TL_messages_saveGif tLRPC$TL_messages_saveGif = new TLRPC$TL_messages_saveGif();
        TLRPC$TL_inputDocument tLRPC$TL_inputDocument = new TLRPC$TL_inputDocument();
        tLRPC$TL_messages_saveGif.id = tLRPC$TL_inputDocument;
        tLRPC$TL_inputDocument.id = tLRPC$Document.id;
        tLRPC$TL_inputDocument.access_hash = tLRPC$Document.access_hash;
        byte[] bArr = tLRPC$Document.file_reference;
        tLRPC$TL_inputDocument.file_reference = bArr;
        if (bArr == null) {
            tLRPC$TL_inputDocument.file_reference = new byte[0];
        }
        tLRPC$TL_messages_saveGif.unsave = true;
        getConnectionsManager().sendRequest(tLRPC$TL_messages_saveGif, new MediaDataController$$ExternalSyntheticLambda169(this, tLRPC$TL_messages_saveGif));
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda88(this, tLRPC$Document));
    }

    public /* synthetic */ void lambda$removeRecentGif$21(TLRPC$TL_messages_saveGif tLRPC$TL_messages_saveGif, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null || !FileRefController.isFileRefError(tLRPC$TL_error.text)) {
            return;
        }
        getFileRefController().requestReference("gif", tLRPC$TL_messages_saveGif);
    }

    public /* synthetic */ void lambda$removeRecentGif$22(TLRPC$Document tLRPC$Document) {
        try {
            SQLiteDatabase database = getMessagesStorage().getDatabase();
            database.executeFast("DELETE FROM web_recent_v3 WHERE id = '" + tLRPC$Document.id + "' AND type = 2").stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public boolean hasRecentGif(TLRPC$Document tLRPC$Document) {
        for (int i = 0; i < this.recentGifs.size(); i++) {
            TLRPC$Document tLRPC$Document2 = this.recentGifs.get(i);
            if (tLRPC$Document2.id == tLRPC$Document.id) {
                this.recentGifs.remove(i);
                this.recentGifs.add(0, tLRPC$Document2);
                return true;
            }
        }
        return false;
    }

    public void addRecentGif(TLRPC$Document tLRPC$Document, int i, boolean z) {
        boolean z2;
        if (tLRPC$Document == null) {
            return;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= this.recentGifs.size()) {
                z2 = false;
                break;
            }
            TLRPC$Document tLRPC$Document2 = this.recentGifs.get(i2);
            if (tLRPC$Document2.id == tLRPC$Document.id) {
                this.recentGifs.remove(i2);
                this.recentGifs.add(0, tLRPC$Document2);
                z2 = true;
                break;
            }
            i2++;
        }
        if (!z2) {
            this.recentGifs.add(0, tLRPC$Document);
        }
        if ((this.recentGifs.size() > getMessagesController().savedGifsLimitDefault && !UserConfig.getInstance(this.currentAccount).isPremium()) || this.recentGifs.size() > getMessagesController().savedGifsLimitPremium) {
            ArrayList<TLRPC$Document> arrayList = this.recentGifs;
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda87(this, arrayList.remove(arrayList.size() - 1)));
            if (z) {
                AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda120(tLRPC$Document));
            }
        }
        ArrayList<TLRPC$Document> arrayList2 = new ArrayList<>();
        arrayList2.add(tLRPC$Document);
        processLoadedRecentDocuments(0, arrayList2, true, i, false);
    }

    public /* synthetic */ void lambda$addRecentGif$23(TLRPC$Document tLRPC$Document) {
        try {
            SQLiteDatabase database = getMessagesStorage().getDatabase();
            database.executeFast("DELETE FROM web_recent_v3 WHERE id = '" + tLRPC$Document.id + "' AND type = 2").stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static /* synthetic */ void lambda$addRecentGif$24(TLRPC$Document tLRPC$Document) {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.showBulletin, 0, tLRPC$Document, 7);
    }

    public boolean isLoadingStickers(int i) {
        return this.loadingStickers[i];
    }

    /* JADX WARN: Type inference failed for: r7v0, types: [boolean] */
    public void replaceStickerSet(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet2 = this.stickerSetsById.get(tLRPC$TL_messages_stickerSet.set.id);
        String str = this.diceEmojiStickerSetsById.get(tLRPC$TL_messages_stickerSet.set.id);
        if (str != null) {
            this.diceStickerSetsByEmoji.put(str, tLRPC$TL_messages_stickerSet);
            putDiceStickersToCache(str, tLRPC$TL_messages_stickerSet, (int) (System.currentTimeMillis() / 1000));
        }
        if (tLRPC$TL_messages_stickerSet2 == null) {
            tLRPC$TL_messages_stickerSet2 = this.stickerSetsByName.get(tLRPC$TL_messages_stickerSet.set.short_name);
        }
        boolean z = true;
        boolean z2 = tLRPC$TL_messages_stickerSet2 == null && (tLRPC$TL_messages_stickerSet2 = this.groupStickerSets.get(tLRPC$TL_messages_stickerSet.set.id)) != null;
        if (tLRPC$TL_messages_stickerSet2 == null) {
            return;
        }
        if ("AnimatedEmojies".equals(tLRPC$TL_messages_stickerSet.set.short_name)) {
            tLRPC$TL_messages_stickerSet2.documents = tLRPC$TL_messages_stickerSet.documents;
            tLRPC$TL_messages_stickerSet2.packs = tLRPC$TL_messages_stickerSet.packs;
            tLRPC$TL_messages_stickerSet2.set = tLRPC$TL_messages_stickerSet.set;
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda104(this, tLRPC$TL_messages_stickerSet));
        } else {
            LongSparseArray longSparseArray = new LongSparseArray();
            int size = tLRPC$TL_messages_stickerSet.documents.size();
            for (int i = 0; i < size; i++) {
                TLRPC$Document tLRPC$Document = tLRPC$TL_messages_stickerSet.documents.get(i);
                longSparseArray.put(tLRPC$Document.id, tLRPC$Document);
            }
            int size2 = tLRPC$TL_messages_stickerSet2.documents.size();
            boolean z3 = false;
            for (int i2 = 0; i2 < size2; i2++) {
                TLRPC$Document tLRPC$Document2 = (TLRPC$Document) longSparseArray.get(tLRPC$TL_messages_stickerSet2.documents.get(i2).id);
                if (tLRPC$Document2 != null) {
                    tLRPC$TL_messages_stickerSet2.documents.set(i2, tLRPC$Document2);
                    z3 = true;
                }
            }
            z = z3;
        }
        if (!z) {
            return;
        }
        if (z2) {
            putSetToCache(tLRPC$TL_messages_stickerSet2);
            return;
        }
        ?? r7 = tLRPC$TL_messages_stickerSet.set.masks;
        putStickersToCache(r7 == true ? 1 : 0, this.stickerSets[r7 == true ? 1 : 0], this.loadDate[r7], this.loadHash[r7]);
        if (!"AnimatedEmojies".equals(tLRPC$TL_messages_stickerSet.set.short_name)) {
            return;
        }
        putStickersToCache(4, this.stickerSets[4], this.loadDate[4], this.loadHash[4]);
    }

    public /* synthetic */ void lambda$replaceStickerSet$25(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        LongSparseArray<TLRPC$Document> stickerByIds = getStickerByIds(4);
        for (int i = 0; i < tLRPC$TL_messages_stickerSet.documents.size(); i++) {
            TLRPC$Document tLRPC$Document = tLRPC$TL_messages_stickerSet.documents.get(i);
            stickerByIds.put(tLRPC$Document.id, tLRPC$Document);
        }
    }

    public TLRPC$TL_messages_stickerSet getStickerSetByName(String str) {
        return this.stickerSetsByName.get(str);
    }

    public TLRPC$TL_messages_stickerSet getStickerSetByEmojiOrName(String str) {
        return this.diceStickerSetsByEmoji.get(str);
    }

    public TLRPC$TL_messages_stickerSet getStickerSetById(long j) {
        return this.stickerSetsById.get(j);
    }

    public TLRPC$TL_messages_stickerSet getGroupStickerSetById(TLRPC$StickerSet tLRPC$StickerSet) {
        TLRPC$StickerSet tLRPC$StickerSet2;
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = this.stickerSetsById.get(tLRPC$StickerSet.id);
        if (tLRPC$TL_messages_stickerSet == null) {
            tLRPC$TL_messages_stickerSet = this.groupStickerSets.get(tLRPC$StickerSet.id);
            if (tLRPC$TL_messages_stickerSet == null || (tLRPC$StickerSet2 = tLRPC$TL_messages_stickerSet.set) == null) {
                loadGroupStickerSet(tLRPC$StickerSet, true);
            } else if (tLRPC$StickerSet2.hash != tLRPC$StickerSet.hash) {
                loadGroupStickerSet(tLRPC$StickerSet, false);
            }
        }
        return tLRPC$TL_messages_stickerSet;
    }

    public void putGroupStickerSet(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        this.groupStickerSets.put(tLRPC$TL_messages_stickerSet.set.id, tLRPC$TL_messages_stickerSet);
    }

    private void loadGroupStickerSet(TLRPC$StickerSet tLRPC$StickerSet, boolean z) {
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda91(this, tLRPC$StickerSet));
            return;
        }
        TLRPC$TL_messages_getStickerSet tLRPC$TL_messages_getStickerSet = new TLRPC$TL_messages_getStickerSet();
        TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID = new TLRPC$TL_inputStickerSetID();
        tLRPC$TL_messages_getStickerSet.stickerset = tLRPC$TL_inputStickerSetID;
        tLRPC$TL_inputStickerSetID.id = tLRPC$StickerSet.id;
        tLRPC$TL_inputStickerSetID.access_hash = tLRPC$StickerSet.access_hash;
        getConnectionsManager().sendRequest(tLRPC$TL_messages_getStickerSet, new MediaDataController$$ExternalSyntheticLambda133(this));
    }

    public /* synthetic */ void lambda$loadGroupStickerSet$27(TLRPC$StickerSet tLRPC$StickerSet) {
        TLRPC$StickerSet tLRPC$StickerSet2;
        NativeByteBuffer byteBufferValue;
        try {
            SQLiteDatabase database = getMessagesStorage().getDatabase();
            SQLiteCursor queryFinalized = database.queryFinalized("SELECT document FROM web_recent_v3 WHERE id = 's_" + tLRPC$StickerSet.id + "'", new Object[0]);
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = null;
            if (queryFinalized.next() && !queryFinalized.isNull(0) && (byteBufferValue = queryFinalized.byteBufferValue(0)) != null) {
                tLRPC$TL_messages_stickerSet = TLRPC$messages_StickerSet.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                byteBufferValue.reuse();
            }
            queryFinalized.dispose();
            if (tLRPC$TL_messages_stickerSet == null || (tLRPC$StickerSet2 = tLRPC$TL_messages_stickerSet.set) == null || tLRPC$StickerSet2.hash != tLRPC$StickerSet.hash) {
                loadGroupStickerSet(tLRPC$StickerSet, false);
            }
            if (tLRPC$TL_messages_stickerSet == null || tLRPC$TL_messages_stickerSet.set == null) {
                return;
            }
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda103(this, tLRPC$TL_messages_stickerSet));
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public /* synthetic */ void lambda$loadGroupStickerSet$26(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        this.groupStickerSets.put(tLRPC$TL_messages_stickerSet.set.id, tLRPC$TL_messages_stickerSet);
        getNotificationCenter().postNotificationName(NotificationCenter.groupStickersDidLoad, Long.valueOf(tLRPC$TL_messages_stickerSet.set.id));
    }

    public /* synthetic */ void lambda$loadGroupStickerSet$29(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda102(this, (TLRPC$TL_messages_stickerSet) tLObject));
        }
    }

    public /* synthetic */ void lambda$loadGroupStickerSet$28(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        this.groupStickerSets.put(tLRPC$TL_messages_stickerSet.set.id, tLRPC$TL_messages_stickerSet);
        getNotificationCenter().postNotificationName(NotificationCenter.groupStickersDidLoad, Long.valueOf(tLRPC$TL_messages_stickerSet.set.id));
    }

    private void putSetToCache(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda105(this, tLRPC$TL_messages_stickerSet));
    }

    public /* synthetic */ void lambda$putSetToCache$30(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        try {
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO web_recent_v3 VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            executeFast.requery();
            executeFast.bindString(1, "s_" + tLRPC$TL_messages_stickerSet.set.id);
            executeFast.bindInteger(2, 6);
            executeFast.bindString(3, "");
            executeFast.bindString(4, "");
            executeFast.bindString(5, "");
            executeFast.bindInteger(6, 0);
            executeFast.bindInteger(7, 0);
            executeFast.bindInteger(8, 0);
            executeFast.bindInteger(9, 0);
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tLRPC$TL_messages_stickerSet.getObjectSize());
            tLRPC$TL_messages_stickerSet.serializeToStream(nativeByteBuffer);
            executeFast.bindByteBuffer(10, nativeByteBuffer);
            executeFast.step();
            nativeByteBuffer.reuse();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public HashMap<String, ArrayList<TLRPC$Document>> getAllStickers() {
        return this.allStickers;
    }

    public HashMap<String, ArrayList<TLRPC$Document>> getAllStickersFeatured() {
        return this.allStickersFeatured;
    }

    public TLRPC$Document getEmojiAnimatedSticker(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        String replace = charSequence.toString().replace("️", "");
        ArrayList<TLRPC$TL_messages_stickerSet> stickerSets = getStickerSets(4);
        int size = stickerSets.size();
        for (int i = 0; i < size; i++) {
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = stickerSets.get(i);
            int size2 = tLRPC$TL_messages_stickerSet.packs.size();
            for (int i2 = 0; i2 < size2; i2++) {
                TLRPC$TL_stickerPack tLRPC$TL_stickerPack = tLRPC$TL_messages_stickerSet.packs.get(i2);
                if (!tLRPC$TL_stickerPack.documents.isEmpty() && TextUtils.equals(tLRPC$TL_stickerPack.emoticon, replace)) {
                    return getStickerByIds(4).get(tLRPC$TL_stickerPack.documents.get(0).longValue());
                }
            }
        }
        return null;
    }

    public boolean canAddStickerToFavorites() {
        return !this.stickersLoaded[0] || this.stickerSets[0].size() >= 5 || !this.recentStickers[2].isEmpty();
    }

    public ArrayList<TLRPC$TL_messages_stickerSet> getStickerSets(int i) {
        if (i == 3) {
            return this.stickerSets[2];
        }
        return this.stickerSets[i];
    }

    public LongSparseArray<TLRPC$Document> getStickerByIds(int i) {
        return this.stickersByIds[i];
    }

    public ArrayList<TLRPC$StickerSetCovered> getFeaturedStickerSets() {
        return this.featuredStickerSets;
    }

    public ArrayList<Long> getUnreadStickerSets() {
        return this.unreadStickerSets;
    }

    public boolean areAllTrendingStickerSetsUnread() {
        int size = this.featuredStickerSets.size();
        for (int i = 0; i < size; i++) {
            TLRPC$StickerSetCovered tLRPC$StickerSetCovered = this.featuredStickerSets.get(i);
            if (!isStickerPackInstalled(tLRPC$StickerSetCovered.set.id) && ((!tLRPC$StickerSetCovered.covers.isEmpty() || tLRPC$StickerSetCovered.cover != null) && !this.unreadStickerSets.contains(Long.valueOf(tLRPC$StickerSetCovered.set.id)))) {
                return false;
            }
        }
        return true;
    }

    public boolean isStickerPackInstalled(long j) {
        return this.installedStickerSetsById.indexOfKey(j) >= 0;
    }

    public boolean isStickerPackUnread(long j) {
        return this.unreadStickerSets.contains(Long.valueOf(j));
    }

    public boolean isStickerPackInstalled(String str) {
        return this.stickerSetsByName.containsKey(str);
    }

    public String getEmojiForSticker(long j) {
        String str = this.stickersByEmoji.get(j);
        return str != null ? str : "";
    }

    public static boolean canShowAttachMenuBotForTarget(TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot, String str) {
        Iterator<TLRPC$AttachMenuPeerType> it = tLRPC$TL_attachMenuBot.peer_types.iterator();
        while (it.hasNext()) {
            TLRPC$AttachMenuPeerType next = it.next();
            if (((next instanceof TLRPC$TL_attachMenuPeerTypeSameBotPM) || (next instanceof TLRPC$TL_attachMenuPeerTypeBotPM)) && str.equals("bots")) {
                return true;
            }
            if ((next instanceof TLRPC$TL_attachMenuPeerTypeBroadcast) && str.equals("channels")) {
                return true;
            }
            if ((next instanceof TLRPC$TL_attachMenuPeerTypeChat) && str.equals("groups")) {
                return true;
            }
            if ((next instanceof TLRPC$TL_attachMenuPeerTypePM) && str.equals("users")) {
                return true;
            }
        }
        return false;
    }

    public static boolean canShowAttachMenuBot(TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot, TLObject tLObject) {
        TLRPC$Chat tLRPC$Chat = null;
        TLRPC$User tLRPC$User = tLObject instanceof TLRPC$User ? (TLRPC$User) tLObject : null;
        if (tLObject instanceof TLRPC$Chat) {
            tLRPC$Chat = (TLRPC$Chat) tLObject;
        }
        Iterator<TLRPC$AttachMenuPeerType> it = tLRPC$TL_attachMenuBot.peer_types.iterator();
        while (it.hasNext()) {
            TLRPC$AttachMenuPeerType next = it.next();
            if ((next instanceof TLRPC$TL_attachMenuPeerTypeSameBotPM) && tLRPC$User != null && tLRPC$User.bot && tLRPC$User.id == tLRPC$TL_attachMenuBot.bot_id) {
                return true;
            }
            if ((next instanceof TLRPC$TL_attachMenuPeerTypeBotPM) && tLRPC$User != null && tLRPC$User.bot && tLRPC$User.id != tLRPC$TL_attachMenuBot.bot_id) {
                return true;
            }
            if ((next instanceof TLRPC$TL_attachMenuPeerTypePM) && tLRPC$User != null && !tLRPC$User.bot) {
                return true;
            }
            if ((next instanceof TLRPC$TL_attachMenuPeerTypeChat) && tLRPC$Chat != null && !ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat)) {
                return true;
            }
            if ((next instanceof TLRPC$TL_attachMenuPeerTypeBroadcast) && tLRPC$Chat != null && ChatObject.isChannelAndNotMegaGroup(tLRPC$Chat)) {
                return true;
            }
        }
        return false;
    }

    public static TLRPC$TL_attachMenuBotIcon getAnimatedAttachMenuBotIcon(TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot) {
        Iterator<TLRPC$TL_attachMenuBotIcon> it = tLRPC$TL_attachMenuBot.icons.iterator();
        while (it.hasNext()) {
            TLRPC$TL_attachMenuBotIcon next = it.next();
            if (next.name.equals("android_animated")) {
                return next;
            }
        }
        return null;
    }

    public static TLRPC$TL_attachMenuBotIcon getStaticAttachMenuBotIcon(TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot) {
        Iterator<TLRPC$TL_attachMenuBotIcon> it = tLRPC$TL_attachMenuBot.icons.iterator();
        while (it.hasNext()) {
            TLRPC$TL_attachMenuBotIcon next = it.next();
            if (next.name.equals("default_static")) {
                return next;
            }
        }
        return null;
    }

    public static TLRPC$TL_attachMenuBotIcon getPlaceholderStaticAttachMenuBotIcon(TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot) {
        Iterator<TLRPC$TL_attachMenuBotIcon> it = tLRPC$TL_attachMenuBot.icons.iterator();
        while (it.hasNext()) {
            TLRPC$TL_attachMenuBotIcon next = it.next();
            if (next.name.equals("placeholder_static")) {
                return next;
            }
        }
        return null;
    }

    public static long calcDocumentsHash(ArrayList<TLRPC$Document> arrayList) {
        return calcDocumentsHash(arrayList, 200);
    }

    public static long calcDocumentsHash(ArrayList<TLRPC$Document> arrayList, int i) {
        long j = 0;
        if (arrayList == null) {
            return 0L;
        }
        int min = Math.min(i, arrayList.size());
        for (int i2 = 0; i2 < min; i2++) {
            TLRPC$Document tLRPC$Document = arrayList.get(i2);
            if (tLRPC$Document != null) {
                j = calcHash(j, tLRPC$Document.id);
            }
        }
        return j;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x001d, code lost:
        if (r6.recentStickersLoaded[r7] != false) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x001f, code lost:
        r9 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0020, code lost:
        if (r9 == false) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0022, code lost:
        getMessagesStorage().getStorageQueue().postRunnable(new org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda110(r6, r8, r7));
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0034, code lost:
        r9 = org.telegram.messenger.MessagesController.getEmojiSettings(r6.currentAccount);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x003b, code lost:
        if (r10 != false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x003f, code lost:
        if (r8 == false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0041, code lost:
        r9 = r9.getLong("lastGifLoadTime", 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0048, code lost:
        if (r7 != 0) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x004a, code lost:
        r9 = r9.getLong("lastStickersLoadTime", 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0051, code lost:
        if (r7 != 1) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0053, code lost:
        r9 = r9.getLong("lastStickersLoadTimeMask", 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x005a, code lost:
        if (r7 != 3) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x005c, code lost:
        r9 = r9.getLong("lastStickersLoadTimeGreet", 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0063, code lost:
        r9 = r9.getLong("lastStickersLoadTimeFavs", 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0077, code lost:
        if (java.lang.Math.abs(java.lang.System.currentTimeMillis() - r9) >= 3600000) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0079, code lost:
        if (r8 == false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x007b, code lost:
        r6.loadingRecentGifs = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x007e, code lost:
        r6.loadingRecentStickers[r7] = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0082, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0083, code lost:
        if (r8 == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0085, code lost:
        r8 = new org.telegram.tgnet.TLRPC$TL_messages_getSavedGifs();
        r8.hash = calcDocumentsHash(r6.recentGifs);
        getConnectionsManager().sendRequest(r8, new org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda141(r6, r7));
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00a0, code lost:
        if (r7 != 2) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00a2, code lost:
        r8 = new org.telegram.tgnet.TLRPC$TL_messages_getFavedStickers();
        r8.hash = calcDocumentsHash(r6.recentStickers[r7]);
        r8 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00b2, code lost:
        if (r7 != 3) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00b4, code lost:
        r8 = new org.telegram.tgnet.TLRPC$TL_messages_getStickers();
        r8.emoticon = "👋" + org.telegram.messenger.Emoji.fixEmoji("⭐");
        r8.hash = calcDocumentsHash(r6.recentStickers[r7]);
        r8 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00dd, code lost:
        r8 = new org.telegram.tgnet.TLRPC$TL_messages_getRecentStickers();
        r8.hash = calcDocumentsHash(r6.recentStickers[r7]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00ec, code lost:
        if (r7 != 1) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00ee, code lost:
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00ef, code lost:
        r8.attached = r0;
        r8 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00f1, code lost:
        getConnectionsManager().sendRequest(r8, new org.telegram.messenger.MediaDataController$$ExternalSyntheticLambda142(r6, r7));
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00fd, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x000d, code lost:
        if (r6.recentGifsLoaded != false) goto L14;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadRecents(int i, boolean z, boolean z2, boolean z3) {
        boolean z4 = false;
        if (z) {
            if (this.loadingRecentGifs) {
                return;
            }
            this.loadingRecentGifs = true;
        } else {
            boolean[] zArr = this.loadingRecentStickers;
            if (zArr[i]) {
                return;
            }
            zArr[i] = true;
        }
    }

    public /* synthetic */ void lambda$loadRecents$32(boolean z, int i) {
        NativeByteBuffer byteBufferValue;
        int i2 = 3;
        if (z) {
            i2 = 2;
        } else if (i != 0) {
            i2 = i == 1 ? 4 : i == 3 ? 6 : 5;
        }
        try {
            SQLiteDatabase database = getMessagesStorage().getDatabase();
            SQLiteCursor queryFinalized = database.queryFinalized("SELECT document FROM web_recent_v3 WHERE type = " + i2 + " ORDER BY date DESC", new Object[0]);
            ArrayList arrayList = new ArrayList();
            while (queryFinalized.next()) {
                if (!queryFinalized.isNull(0) && (byteBufferValue = queryFinalized.byteBufferValue(0)) != null) {
                    TLRPC$Document TLdeserialize = TLRPC$Document.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                    if (TLdeserialize != null) {
                        arrayList.add(TLdeserialize);
                    }
                    byteBufferValue.reuse();
                }
            }
            queryFinalized.dispose();
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda113(this, z, arrayList, i));
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$loadRecents$31(boolean z, ArrayList arrayList, int i) {
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
        getNotificationCenter().postNotificationName(NotificationCenter.recentDocumentsDidLoad, Boolean.valueOf(z), Integer.valueOf(i));
        loadRecents(i, z, false, false);
    }

    public /* synthetic */ void lambda$loadRecents$33(int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        processLoadedRecentDocuments(i, tLObject instanceof TLRPC$TL_messages_savedGifs ? ((TLRPC$TL_messages_savedGifs) tLObject).gifs : null, true, 0, true);
    }

    public /* synthetic */ void lambda$loadRecents$34(int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ArrayList<TLRPC$Document> arrayList;
        if (i == 3) {
            if (tLObject instanceof TLRPC$TL_messages_stickers) {
                arrayList = ((TLRPC$TL_messages_stickers) tLObject).stickers;
            }
            arrayList = null;
        } else if (i == 2) {
            if (tLObject instanceof TLRPC$TL_messages_favedStickers) {
                arrayList = ((TLRPC$TL_messages_favedStickers) tLObject).stickers;
            }
            arrayList = null;
        } else {
            if (tLObject instanceof TLRPC$TL_messages_recentStickers) {
                arrayList = ((TLRPC$TL_messages_recentStickers) tLObject).stickers;
            }
            arrayList = null;
        }
        processLoadedRecentDocuments(i, arrayList, false, 0, true);
    }

    private void preloadNextGreetingsSticker() {
        if (this.recentStickers[3].isEmpty()) {
            return;
        }
        ArrayList<TLRPC$Document>[] arrayListArr = this.recentStickers;
        this.greetingsSticker = arrayListArr[3].get(Utilities.random.nextInt(arrayListArr[3].size()));
        getFileLoader().loadFile(ImageLocation.getForDocument(this.greetingsSticker), this.greetingsSticker, null, 0, 1);
    }

    public TLRPC$Document getGreetingsSticker() {
        TLRPC$Document tLRPC$Document = this.greetingsSticker;
        preloadNextGreetingsSticker();
        return tLRPC$Document;
    }

    public void processLoadedRecentDocuments(int i, ArrayList<TLRPC$Document> arrayList, boolean z, int i2, boolean z2) {
        if (arrayList != null) {
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda112(this, z, i, arrayList, z2, i2));
        }
        if (i2 == 0) {
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda111(this, z, i, arrayList));
        }
    }

    public /* synthetic */ void lambda$processLoadedRecentDocuments$35(boolean z, int i, ArrayList arrayList, boolean z2, int i2) {
        int i3;
        try {
            SQLiteDatabase database = getMessagesStorage().getDatabase();
            int i4 = 2;
            if (z) {
                i3 = getMessagesController().maxRecentGifsCount;
            } else if (i == 3) {
                i3 = 200;
            } else if (i == 2) {
                i3 = getMessagesController().maxFaveStickersCount;
            } else {
                i3 = getMessagesController().maxRecentStickersCount;
            }
            database.beginTransaction();
            SQLitePreparedStatement executeFast = database.executeFast("REPLACE INTO web_recent_v3 VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            int size = arrayList.size();
            int i5 = z ? 2 : i == 0 ? 3 : i == 1 ? 4 : i == 3 ? 6 : 5;
            if (z2) {
                database.executeFast("DELETE FROM web_recent_v3 WHERE type = " + i5).stepThis().dispose();
            }
            int i6 = 0;
            while (i6 < size && i6 != i3) {
                TLRPC$Document tLRPC$Document = (TLRPC$Document) arrayList.get(i6);
                executeFast.requery();
                StringBuilder sb = new StringBuilder();
                sb.append("");
                int i7 = i6;
                sb.append(tLRPC$Document.id);
                executeFast.bindString(1, sb.toString());
                executeFast.bindInteger(i4, i5);
                executeFast.bindString(3, "");
                executeFast.bindString(4, "");
                executeFast.bindString(5, "");
                executeFast.bindInteger(6, 0);
                executeFast.bindInteger(7, 0);
                executeFast.bindInteger(8, 0);
                executeFast.bindInteger(9, i2 != 0 ? i2 : size - i7);
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tLRPC$Document.getObjectSize());
                tLRPC$Document.serializeToStream(nativeByteBuffer);
                executeFast.bindByteBuffer(10, nativeByteBuffer);
                executeFast.step();
                nativeByteBuffer.reuse();
                i6 = i7 + 1;
                i4 = 2;
            }
            executeFast.dispose();
            database.commitTransaction();
            if (arrayList.size() < i3) {
                return;
            }
            database.beginTransaction();
            while (i3 < arrayList.size()) {
                database.executeFast("DELETE FROM web_recent_v3 WHERE id = '" + ((TLRPC$Document) arrayList.get(i3)).id + "' AND type = " + i5).stepThis().dispose();
                i3++;
            }
            database.commitTransaction();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$processLoadedRecentDocuments$36(boolean z, int i, ArrayList arrayList) {
        SharedPreferences.Editor edit = MessagesController.getEmojiSettings(this.currentAccount).edit();
        if (z) {
            this.loadingRecentGifs = false;
            this.recentGifsLoaded = true;
            edit.putLong("lastGifLoadTime", System.currentTimeMillis()).commit();
        } else {
            this.loadingRecentStickers[i] = false;
            this.recentStickersLoaded[i] = true;
            if (i == 0) {
                edit.putLong("lastStickersLoadTime", System.currentTimeMillis()).commit();
            } else if (i == 1) {
                edit.putLong("lastStickersLoadTimeMask", System.currentTimeMillis()).commit();
            } else if (i == 3) {
                edit.putLong("lastStickersLoadTimeGreet", System.currentTimeMillis()).commit();
            } else {
                edit.putLong("lastStickersLoadTimeFavs", System.currentTimeMillis()).commit();
            }
        }
        if (arrayList != null) {
            if (z) {
                this.recentGifs = arrayList;
            } else {
                this.recentStickers[i] = arrayList;
            }
            if (i == 3) {
                preloadNextGreetingsSticker();
            }
            getNotificationCenter().postNotificationName(NotificationCenter.recentDocumentsDidLoad, Boolean.valueOf(z), Integer.valueOf(i));
        }
    }

    public void reorderStickers(int i, ArrayList<Long> arrayList) {
        Collections.sort(this.stickerSets[i], new MediaDataController$$ExternalSyntheticLambda124(arrayList));
        this.loadHash[i] = calcStickersHash(this.stickerSets[i]);
        getNotificationCenter().postNotificationName(NotificationCenter.stickersDidLoad, Integer.valueOf(i));
        loadStickers(i, false, true);
    }

    public static /* synthetic */ int lambda$reorderStickers$37(ArrayList arrayList, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet2) {
        int indexOf = arrayList.indexOf(Long.valueOf(tLRPC$TL_messages_stickerSet.set.id));
        int indexOf2 = arrayList.indexOf(Long.valueOf(tLRPC$TL_messages_stickerSet2.set.id));
        if (indexOf > indexOf2) {
            return 1;
        }
        return indexOf < indexOf2 ? -1 : 0;
    }

    public void calcNewHash(int i) {
        this.loadHash[i] = calcStickersHash(this.stickerSets[i]);
    }

    public void storeTempStickerSet(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        this.stickerSetsById.put(tLRPC$TL_messages_stickerSet.set.id, tLRPC$TL_messages_stickerSet);
        this.stickerSetsByName.put(tLRPC$TL_messages_stickerSet.set.short_name, tLRPC$TL_messages_stickerSet);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v5, types: [boolean, int] */
    public void addNewStickerSet(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        if (this.stickerSetsById.indexOfKey(tLRPC$TL_messages_stickerSet.set.id) >= 0 || this.stickerSetsByName.containsKey(tLRPC$TL_messages_stickerSet.set.short_name)) {
            return;
        }
        ?? r0 = tLRPC$TL_messages_stickerSet.set.masks;
        this.stickerSets[r0 == true ? 1 : 0].add(0, tLRPC$TL_messages_stickerSet);
        this.stickerSetsById.put(tLRPC$TL_messages_stickerSet.set.id, tLRPC$TL_messages_stickerSet);
        this.installedStickerSetsById.put(tLRPC$TL_messages_stickerSet.set.id, tLRPC$TL_messages_stickerSet);
        this.stickerSetsByName.put(tLRPC$TL_messages_stickerSet.set.short_name, tLRPC$TL_messages_stickerSet);
        LongSparseArray longSparseArray = new LongSparseArray();
        for (int i = 0; i < tLRPC$TL_messages_stickerSet.documents.size(); i++) {
            TLRPC$Document tLRPC$Document = tLRPC$TL_messages_stickerSet.documents.get(i);
            longSparseArray.put(tLRPC$Document.id, tLRPC$Document);
        }
        for (int i2 = 0; i2 < tLRPC$TL_messages_stickerSet.packs.size(); i2++) {
            TLRPC$TL_stickerPack tLRPC$TL_stickerPack = tLRPC$TL_messages_stickerSet.packs.get(i2);
            String replace = tLRPC$TL_stickerPack.emoticon.replace("️", "");
            tLRPC$TL_stickerPack.emoticon = replace;
            ArrayList<TLRPC$Document> arrayList = this.allStickers.get(replace);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                this.allStickers.put(tLRPC$TL_stickerPack.emoticon, arrayList);
            }
            for (int i3 = 0; i3 < tLRPC$TL_stickerPack.documents.size(); i3++) {
                Long l = tLRPC$TL_stickerPack.documents.get(i3);
                if (this.stickersByEmoji.indexOfKey(l.longValue()) < 0) {
                    this.stickersByEmoji.put(l.longValue(), tLRPC$TL_stickerPack.emoticon);
                }
                TLRPC$Document tLRPC$Document2 = (TLRPC$Document) longSparseArray.get(l.longValue());
                if (tLRPC$Document2 != null) {
                    arrayList.add(tLRPC$Document2);
                }
            }
        }
        this.loadHash[r0] = calcStickersHash(this.stickerSets[r0]);
        getNotificationCenter().postNotificationName(NotificationCenter.stickersDidLoad, Integer.valueOf(r0 == true ? 1 : 0));
        loadStickers(r0, false, true);
    }

    public void loadFeaturedStickers(boolean z, boolean z2) {
        if (this.loadingFeaturedStickers) {
            return;
        }
        this.loadingFeaturedStickers = true;
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda10(this));
            return;
        }
        TLRPC$TL_messages_getFeaturedStickers tLRPC$TL_messages_getFeaturedStickers = new TLRPC$TL_messages_getFeaturedStickers();
        tLRPC$TL_messages_getFeaturedStickers.hash = z2 ? 0L : this.loadFeaturedHash;
        getConnectionsManager().sendRequest(tLRPC$TL_messages_getFeaturedStickers, new MediaDataController$$ExternalSyntheticLambda168(this, tLRPC$TL_messages_getFeaturedStickers));
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0098 A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadFeaturedStickers$38() {
        ArrayList<TLRPC$StickerSetCovered> arrayList;
        int i;
        SQLiteCursor sQLiteCursor;
        Throwable th;
        int i2;
        ArrayList<TLRPC$StickerSetCovered> arrayList2;
        long j;
        boolean z;
        ArrayList<Long> arrayList3 = new ArrayList<>();
        ArrayList<TLRPC$StickerSetCovered> arrayList4 = null;
        int i3 = 0;
        long j2 = 0;
        try {
            sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT data, unread, date, hash, premium FROM stickers_featured WHERE 1", new Object[0]);
            try {
                if (sQLiteCursor.next()) {
                    NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
                    if (byteBufferValue != null) {
                        arrayList = new ArrayList<>();
                        try {
                            int readInt32 = byteBufferValue.readInt32(false);
                            for (int i4 = 0; i4 < readInt32; i4++) {
                                arrayList.add(TLRPC$StickerSetCovered.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false));
                            }
                            byteBufferValue.reuse();
                            arrayList4 = arrayList;
                        } catch (Throwable th2) {
                            th = th2;
                            i = 0;
                            try {
                                FileLog.e(th);
                                i2 = i;
                                arrayList2 = arrayList;
                                j = j2;
                                z = false;
                                processLoadedFeaturedStickers(arrayList2, arrayList3, z, true, i2, j);
                            } finally {
                                if (sQLiteCursor != null) {
                                    sQLiteCursor.dispose();
                                }
                            }
                        }
                    }
                    NativeByteBuffer byteBufferValue2 = sQLiteCursor.byteBufferValue(1);
                    if (byteBufferValue2 != null) {
                        int readInt322 = byteBufferValue2.readInt32(false);
                        for (int i5 = 0; i5 < readInt322; i5++) {
                            arrayList3.add(Long.valueOf(byteBufferValue2.readInt64(false)));
                        }
                        byteBufferValue2.reuse();
                    }
                    int intValue = sQLiteCursor.intValue(2);
                    try {
                        j2 = calcFeaturedStickersHash(arrayList4);
                        if (sQLiteCursor.intValue(4) == 1) {
                            i3 = 1;
                        }
                        z = i3;
                        i3 = intValue;
                        j = j2;
                    } catch (Throwable th3) {
                        arrayList = arrayList4;
                        th = th3;
                        i = intValue;
                        FileLog.e(th);
                        i2 = i;
                        arrayList2 = arrayList;
                        j = j2;
                        z = false;
                        processLoadedFeaturedStickers(arrayList2, arrayList3, z, true, i2, j);
                    }
                } else {
                    j = 0;
                    z = false;
                }
                sQLiteCursor.dispose();
                i2 = i3;
                arrayList2 = arrayList4;
            } catch (Throwable th4) {
                arrayList = arrayList4;
                th = th4;
            }
        } catch (Throwable th5) {
            arrayList = null;
            i = 0;
            th = th5;
            sQLiteCursor = null;
        }
        processLoadedFeaturedStickers(arrayList2, arrayList3, z, true, i2, j);
    }

    public /* synthetic */ void lambda$loadFeaturedStickers$40(TLRPC$TL_messages_getFeaturedStickers tLRPC$TL_messages_getFeaturedStickers, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda84(this, tLObject, tLRPC$TL_messages_getFeaturedStickers));
    }

    public /* synthetic */ void lambda$loadFeaturedStickers$39(TLObject tLObject, TLRPC$TL_messages_getFeaturedStickers tLRPC$TL_messages_getFeaturedStickers) {
        if (tLObject instanceof TLRPC$TL_messages_featuredStickers) {
            TLRPC$TL_messages_featuredStickers tLRPC$TL_messages_featuredStickers = (TLRPC$TL_messages_featuredStickers) tLObject;
            processLoadedFeaturedStickers(tLRPC$TL_messages_featuredStickers.sets, tLRPC$TL_messages_featuredStickers.unread, tLRPC$TL_messages_featuredStickers.premium, false, (int) (System.currentTimeMillis() / 1000), tLRPC$TL_messages_featuredStickers.hash);
            return;
        }
        processLoadedFeaturedStickers(null, null, false, false, (int) (System.currentTimeMillis() / 1000), tLRPC$TL_messages_getFeaturedStickers.hash);
    }

    private void processLoadedFeaturedStickers(ArrayList<TLRPC$StickerSetCovered> arrayList, ArrayList<Long> arrayList2, boolean z, boolean z2, int i, long j) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda13(this));
        Utilities.stageQueue.postRunnable(new MediaDataController$$ExternalSyntheticLambda115(this, z2, arrayList, i, j, arrayList2, z));
    }

    public /* synthetic */ void lambda$processLoadedFeaturedStickers$41() {
        this.loadingFeaturedStickers = false;
        this.featuredStickersLoaded = true;
    }

    public /* synthetic */ void lambda$processLoadedFeaturedStickers$45(boolean z, ArrayList arrayList, int i, long j, ArrayList arrayList2, boolean z2) {
        long j2 = 0;
        if ((z && (arrayList == null || Math.abs((System.currentTimeMillis() / 1000) - i) >= 3600)) || (!z && arrayList == null && j == 0)) {
            MediaDataController$$ExternalSyntheticLambda68 mediaDataController$$ExternalSyntheticLambda68 = new MediaDataController$$ExternalSyntheticLambda68(this, arrayList, j);
            if (arrayList == null && !z) {
                j2 = 1000;
            }
            AndroidUtilities.runOnUIThread(mediaDataController$$ExternalSyntheticLambda68, j2);
            if (arrayList == null) {
                return;
            }
        }
        if (arrayList != null) {
            try {
                ArrayList<TLRPC$StickerSetCovered> arrayList3 = new ArrayList<>();
                LongSparseArray longSparseArray = new LongSparseArray();
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    TLRPC$StickerSetCovered tLRPC$StickerSetCovered = (TLRPC$StickerSetCovered) arrayList.get(i2);
                    arrayList3.add(tLRPC$StickerSetCovered);
                    longSparseArray.put(tLRPC$StickerSetCovered.set.id, tLRPC$StickerSetCovered);
                }
                if (!z) {
                    putFeaturedStickersToCache(arrayList3, arrayList2, i, j, z2);
                }
                AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda73(this, arrayList2, longSparseArray, arrayList3, j, i, z2));
                return;
            } catch (Throwable th) {
                FileLog.e(th);
                return;
            }
        }
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda18(this, i));
        putFeaturedStickersToCache(null, null, i, 0L, z2);
    }

    public /* synthetic */ void lambda$processLoadedFeaturedStickers$42(ArrayList arrayList, long j) {
        if (arrayList != null && j != 0) {
            this.loadFeaturedHash = j;
        }
        loadFeaturedStickers(false, false);
    }

    public /* synthetic */ void lambda$processLoadedFeaturedStickers$43(ArrayList arrayList, LongSparseArray longSparseArray, ArrayList arrayList2, long j, int i, boolean z) {
        this.unreadStickerSets = arrayList;
        this.featuredStickerSetsById = longSparseArray;
        this.featuredStickerSets = arrayList2;
        this.loadFeaturedHash = j;
        this.loadFeaturedDate = i;
        this.loadFeaturedPremium = z;
        loadStickers(3, true, false);
        getNotificationCenter().postNotificationName(NotificationCenter.featuredStickersDidLoad, new Object[0]);
    }

    public /* synthetic */ void lambda$processLoadedFeaturedStickers$44(int i) {
        this.loadFeaturedDate = i;
    }

    private void putFeaturedStickersToCache(ArrayList<TLRPC$StickerSetCovered> arrayList, ArrayList<Long> arrayList2, int i, long j, boolean z) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda74(this, arrayList != null ? new ArrayList(arrayList) : null, arrayList2, i, j, z));
    }

    public /* synthetic */ void lambda$putFeaturedStickersToCache$46(ArrayList arrayList, ArrayList arrayList2, int i, long j, boolean z) {
        int i2 = 1;
        try {
            if (arrayList != null) {
                SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO stickers_featured VALUES(?, ?, ?, ?, ?, ?)");
                executeFast.requery();
                int i3 = 4;
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    i3 += ((TLRPC$StickerSetCovered) arrayList.get(i4)).getObjectSize();
                }
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(i3);
                NativeByteBuffer nativeByteBuffer2 = new NativeByteBuffer((arrayList2.size() * 8) + 4);
                nativeByteBuffer.writeInt32(arrayList.size());
                for (int i5 = 0; i5 < arrayList.size(); i5++) {
                    ((TLRPC$StickerSetCovered) arrayList.get(i5)).serializeToStream(nativeByteBuffer);
                }
                nativeByteBuffer2.writeInt32(arrayList2.size());
                for (int i6 = 0; i6 < arrayList2.size(); i6++) {
                    nativeByteBuffer2.writeInt64(((Long) arrayList2.get(i6)).longValue());
                }
                executeFast.bindInteger(1, 1);
                executeFast.bindByteBuffer(2, nativeByteBuffer);
                executeFast.bindByteBuffer(3, nativeByteBuffer2);
                executeFast.bindInteger(4, i);
                executeFast.bindLong(5, j);
                if (!z) {
                    i2 = 0;
                }
                executeFast.bindInteger(6, i2);
                executeFast.step();
                nativeByteBuffer.reuse();
                nativeByteBuffer2.reuse();
                executeFast.dispose();
                return;
            }
            SQLitePreparedStatement executeFast2 = getMessagesStorage().getDatabase().executeFast("UPDATE stickers_featured SET date = ?");
            executeFast2.requery();
            executeFast2.bindInteger(1, i);
            executeFast2.step();
            executeFast2.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private long calcFeaturedStickersHash(ArrayList<TLRPC$StickerSetCovered> arrayList) {
        long j = 0;
        if (arrayList != null && !arrayList.isEmpty()) {
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC$StickerSet tLRPC$StickerSet = arrayList.get(i).set;
                if (!tLRPC$StickerSet.archived) {
                    j = calcHash(j, tLRPC$StickerSet.id);
                    if (this.unreadStickerSets.contains(Long.valueOf(tLRPC$StickerSet.id))) {
                        j = calcHash(j, 1L);
                    }
                }
            }
        }
        return j;
    }

    public void markFaturedStickersAsRead(boolean z) {
        if (this.unreadStickerSets.isEmpty()) {
            return;
        }
        this.unreadStickerSets.clear();
        this.loadFeaturedHash = calcFeaturedStickersHash(this.featuredStickerSets);
        getNotificationCenter().postNotificationName(NotificationCenter.featuredStickersDidLoad, new Object[0]);
        putFeaturedStickersToCache(this.featuredStickerSets, this.unreadStickerSets, this.loadFeaturedDate, this.loadFeaturedHash, this.loadFeaturedPremium);
        if (!z) {
            return;
        }
        getConnectionsManager().sendRequest(new TLRPC$TL_messages_readFeaturedStickers(), MediaDataController$$ExternalSyntheticLambda173.INSTANCE);
    }

    public long getFeaturesStickersHashWithoutUnread() {
        long j = 0;
        for (int i = 0; i < this.featuredStickerSets.size(); i++) {
            TLRPC$StickerSet tLRPC$StickerSet = this.featuredStickerSets.get(i).set;
            if (!tLRPC$StickerSet.archived) {
                j = calcHash(j, tLRPC$StickerSet.id);
            }
        }
        return j;
    }

    public void markFaturedStickersByIdAsRead(long j) {
        if (!this.unreadStickerSets.contains(Long.valueOf(j)) || this.readingStickerSets.contains(Long.valueOf(j))) {
            return;
        }
        this.readingStickerSets.add(Long.valueOf(j));
        TLRPC$TL_messages_readFeaturedStickers tLRPC$TL_messages_readFeaturedStickers = new TLRPC$TL_messages_readFeaturedStickers();
        tLRPC$TL_messages_readFeaturedStickers.id.add(Long.valueOf(j));
        getConnectionsManager().sendRequest(tLRPC$TL_messages_readFeaturedStickers, MediaDataController$$ExternalSyntheticLambda174.INSTANCE);
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda31(this, j), 1000L);
    }

    public /* synthetic */ void lambda$markFaturedStickersByIdAsRead$49(long j) {
        this.unreadStickerSets.remove(Long.valueOf(j));
        this.readingStickerSets.remove(Long.valueOf(j));
        this.loadFeaturedHash = calcFeaturedStickersHash(this.featuredStickerSets);
        getNotificationCenter().postNotificationName(NotificationCenter.featuredStickersDidLoad, new Object[0]);
        putFeaturedStickersToCache(this.featuredStickerSets, this.unreadStickerSets, this.loadFeaturedDate, this.loadFeaturedHash, this.loadFeaturedPremium);
    }

    public int getArchivedStickersCount(int i) {
        return this.archivedStickersCount[i];
    }

    public void verifyAnimatedStickerMessage(TLRPC$Message tLRPC$Message) {
        verifyAnimatedStickerMessage(tLRPC$Message, false);
    }

    public void verifyAnimatedStickerMessage(TLRPC$Message tLRPC$Message, boolean z) {
        if (tLRPC$Message == null) {
            return;
        }
        TLRPC$Document document = MessageObject.getDocument(tLRPC$Message);
        String stickerSetName = MessageObject.getStickerSetName(document);
        if (TextUtils.isEmpty(stickerSetName)) {
            return;
        }
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = this.stickerSetsByName.get(stickerSetName);
        if (tLRPC$TL_messages_stickerSet == null) {
            if (z) {
                AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda90(this, tLRPC$Message, stickerSetName));
                return;
            } else {
                lambda$verifyAnimatedStickerMessage$50(tLRPC$Message, stickerSetName);
                return;
            }
        }
        int size = tLRPC$TL_messages_stickerSet.documents.size();
        for (int i = 0; i < size; i++) {
            TLRPC$Document tLRPC$Document = tLRPC$TL_messages_stickerSet.documents.get(i);
            if (tLRPC$Document.id == document.id && tLRPC$Document.dc_id == document.dc_id) {
                tLRPC$Message.stickerVerified = 1;
                return;
            }
        }
    }

    /* renamed from: verifyAnimatedStickerMessageInternal */
    public void lambda$verifyAnimatedStickerMessage$50(TLRPC$Message tLRPC$Message, String str) {
        ArrayList<TLRPC$Message> arrayList = this.verifyingMessages.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.verifyingMessages.put(str, arrayList);
        }
        arrayList.add(tLRPC$Message);
        TLRPC$TL_messages_getStickerSet tLRPC$TL_messages_getStickerSet = new TLRPC$TL_messages_getStickerSet();
        tLRPC$TL_messages_getStickerSet.stickerset = MessageObject.getInputStickerSet(tLRPC$Message);
        getConnectionsManager().sendRequest(tLRPC$TL_messages_getStickerSet, new MediaDataController$$ExternalSyntheticLambda161(this, str));
    }

    public /* synthetic */ void lambda$verifyAnimatedStickerMessageInternal$52(String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda60(this, str, tLObject));
    }

    public /* synthetic */ void lambda$verifyAnimatedStickerMessageInternal$51(String str, TLObject tLObject) {
        ArrayList<TLRPC$Message> arrayList = this.verifyingMessages.get(str);
        if (tLObject != null) {
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) tLObject;
            storeTempStickerSet(tLRPC$TL_messages_stickerSet);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                TLRPC$Message tLRPC$Message = arrayList.get(i);
                TLRPC$Document document = MessageObject.getDocument(tLRPC$Message);
                int size2 = tLRPC$TL_messages_stickerSet.documents.size();
                int i2 = 0;
                while (true) {
                    if (i2 >= size2) {
                        break;
                    }
                    TLRPC$Document tLRPC$Document = tLRPC$TL_messages_stickerSet.documents.get(i2);
                    if (tLRPC$Document.id == document.id && tLRPC$Document.dc_id == document.dc_id) {
                        tLRPC$Message.stickerVerified = 1;
                        break;
                    }
                    i2++;
                }
                if (tLRPC$Message.stickerVerified == 0) {
                    tLRPC$Message.stickerVerified = 2;
                }
            }
        } else {
            int size3 = arrayList.size();
            for (int i3 = 0; i3 < size3; i3++) {
                arrayList.get(i3).stickerVerified = 2;
            }
        }
        getNotificationCenter().postNotificationName(NotificationCenter.didVerifyMessagesStickers, arrayList);
        getMessagesStorage().updateMessageVerifyFlags(arrayList);
    }

    public void loadArchivedStickersCount(int i, boolean z) {
        boolean z2 = true;
        if (z) {
            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
            int i2 = notificationsSettings.getInt("archivedStickersCount" + i, -1);
            if (i2 == -1) {
                loadArchivedStickersCount(i, false);
                return;
            }
            this.archivedStickersCount[i] = i2;
            getNotificationCenter().postNotificationName(NotificationCenter.archivedStickersCountDidLoad, Integer.valueOf(i));
            return;
        }
        TLRPC$TL_messages_getArchivedStickers tLRPC$TL_messages_getArchivedStickers = new TLRPC$TL_messages_getArchivedStickers();
        tLRPC$TL_messages_getArchivedStickers.limit = 0;
        if (i != 1) {
            z2 = false;
        }
        tLRPC$TL_messages_getArchivedStickers.masks = z2;
        getConnectionsManager().sendRequest(tLRPC$TL_messages_getArchivedStickers, new MediaDataController$$ExternalSyntheticLambda144(this, i));
    }

    public /* synthetic */ void lambda$loadArchivedStickersCount$54(int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda99(this, tLRPC$TL_error, tLObject, i));
    }

    public /* synthetic */ void lambda$loadArchivedStickersCount$53(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, int i) {
        if (tLRPC$TL_error == null) {
            TLRPC$TL_messages_archivedStickers tLRPC$TL_messages_archivedStickers = (TLRPC$TL_messages_archivedStickers) tLObject;
            this.archivedStickersCount[i] = tLRPC$TL_messages_archivedStickers.count;
            SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            edit.putInt("archivedStickersCount" + i, tLRPC$TL_messages_archivedStickers.count).commit();
            getNotificationCenter().postNotificationName(NotificationCenter.archivedStickersCountDidLoad, Integer.valueOf(i));
        }
    }

    private void processLoadStickersResponse(int i, TLRPC$TL_messages_allStickers tLRPC$TL_messages_allStickers) {
        ArrayList<TLRPC$TL_messages_stickerSet> arrayList = new ArrayList<>();
        long j = 1000;
        if (tLRPC$TL_messages_allStickers.sets.isEmpty()) {
            processLoadedStickers(i, arrayList, false, (int) (System.currentTimeMillis() / 1000), tLRPC$TL_messages_allStickers.hash);
        } else {
            LongSparseArray longSparseArray = new LongSparseArray();
            int i2 = 0;
            while (i2 < tLRPC$TL_messages_allStickers.sets.size()) {
                TLRPC$StickerSet tLRPC$StickerSet = tLRPC$TL_messages_allStickers.sets.get(i2);
                TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = this.stickerSetsById.get(tLRPC$StickerSet.id);
                if (tLRPC$TL_messages_stickerSet != null) {
                    TLRPC$StickerSet tLRPC$StickerSet2 = tLRPC$TL_messages_stickerSet.set;
                    if (tLRPC$StickerSet2.hash == tLRPC$StickerSet.hash) {
                        tLRPC$StickerSet2.archived = tLRPC$StickerSet.archived;
                        tLRPC$StickerSet2.installed = tLRPC$StickerSet.installed;
                        tLRPC$StickerSet2.official = tLRPC$StickerSet.official;
                        longSparseArray.put(tLRPC$StickerSet2.id, tLRPC$TL_messages_stickerSet);
                        arrayList.add(tLRPC$TL_messages_stickerSet);
                        if (longSparseArray.size() == tLRPC$TL_messages_allStickers.sets.size()) {
                            processLoadedStickers(i, arrayList, false, (int) (System.currentTimeMillis() / j), tLRPC$TL_messages_allStickers.hash);
                        }
                        i2++;
                        j = 1000;
                    }
                }
                arrayList.add(null);
                TLRPC$TL_messages_getStickerSet tLRPC$TL_messages_getStickerSet = new TLRPC$TL_messages_getStickerSet();
                TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID = new TLRPC$TL_inputStickerSetID();
                tLRPC$TL_messages_getStickerSet.stickerset = tLRPC$TL_inputStickerSetID;
                tLRPC$TL_inputStickerSetID.id = tLRPC$StickerSet.id;
                tLRPC$TL_inputStickerSetID.access_hash = tLRPC$StickerSet.access_hash;
                getConnectionsManager().sendRequest(tLRPC$TL_messages_getStickerSet, new MediaDataController$$ExternalSyntheticLambda164(this, arrayList, i2, longSparseArray, tLRPC$StickerSet, tLRPC$TL_messages_allStickers, i));
                i2++;
                j = 1000;
            }
        }
    }

    public /* synthetic */ void lambda$processLoadStickersResponse$56(ArrayList arrayList, int i, LongSparseArray longSparseArray, TLRPC$StickerSet tLRPC$StickerSet, TLRPC$TL_messages_allStickers tLRPC$TL_messages_allStickers, int i2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda82(this, tLObject, arrayList, i, longSparseArray, tLRPC$StickerSet, tLRPC$TL_messages_allStickers, i2));
    }

    public /* synthetic */ void lambda$processLoadStickersResponse$55(TLObject tLObject, ArrayList arrayList, int i, LongSparseArray longSparseArray, TLRPC$StickerSet tLRPC$StickerSet, TLRPC$TL_messages_allStickers tLRPC$TL_messages_allStickers, int i2) {
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) tLObject;
        arrayList.set(i, tLRPC$TL_messages_stickerSet);
        longSparseArray.put(tLRPC$StickerSet.id, tLRPC$TL_messages_stickerSet);
        if (longSparseArray.size() == tLRPC$TL_messages_allStickers.sets.size()) {
            int i3 = 0;
            while (i3 < arrayList.size()) {
                if (arrayList.get(i3) == null) {
                    arrayList.remove(i3);
                    i3--;
                }
                i3++;
            }
            processLoadedStickers(i2, arrayList, false, (int) (System.currentTimeMillis() / 1000), tLRPC$TL_messages_allStickers.hash);
        }
    }

    public void loadStickersByEmojiOrName(String str, boolean z, boolean z2) {
        if (!this.loadingDiceStickerSets.contains(str)) {
            if (z && this.diceStickerSetsByEmoji.get(str) != null) {
                return;
            }
            this.loadingDiceStickerSets.add(str);
            if (z2) {
                getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda63(this, str, z));
                return;
            }
            TLRPC$TL_messages_getStickerSet tLRPC$TL_messages_getStickerSet = new TLRPC$TL_messages_getStickerSet();
            if (z) {
                TLRPC$TL_inputStickerSetDice tLRPC$TL_inputStickerSetDice = new TLRPC$TL_inputStickerSetDice();
                tLRPC$TL_inputStickerSetDice.emoticon = str;
                tLRPC$TL_messages_getStickerSet.stickerset = tLRPC$TL_inputStickerSetDice;
            } else {
                TLRPC$TL_inputStickerSetShortName tLRPC$TL_inputStickerSetShortName = new TLRPC$TL_inputStickerSetShortName();
                tLRPC$TL_inputStickerSetShortName.short_name = str;
                tLRPC$TL_messages_getStickerSet.stickerset = tLRPC$TL_inputStickerSetShortName;
            }
            getConnectionsManager().sendRequest(tLRPC$TL_messages_getStickerSet, new MediaDataController$$ExternalSyntheticLambda163(this, str, z));
        }
    }

    public /* synthetic */ void lambda$loadStickersByEmojiOrName$57(String str, boolean z) {
        Throwable th;
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet;
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet2;
        int i;
        SQLiteCursor sQLiteCursor = null;
        r0 = null;
        r0 = null;
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet3 = null;
        int i2 = 0;
        try {
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT data, date FROM stickers_dice WHERE emoji = ?", str);
            try {
                if (queryFinalized.next()) {
                    NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(0);
                    if (byteBufferValue != null) {
                        tLRPC$TL_messages_stickerSet3 = TLRPC$messages_StickerSet.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                        byteBufferValue.reuse();
                    }
                    i2 = queryFinalized.intValue(1);
                }
                queryFinalized.dispose();
                tLRPC$TL_messages_stickerSet2 = tLRPC$TL_messages_stickerSet3;
                i = i2;
            } catch (Throwable th2) {
                th = th2;
                tLRPC$TL_messages_stickerSet = tLRPC$TL_messages_stickerSet3;
                sQLiteCursor = queryFinalized;
                try {
                    FileLog.e(th);
                    tLRPC$TL_messages_stickerSet2 = tLRPC$TL_messages_stickerSet;
                    i = 0;
                    processLoadedDiceStickers(str, z, tLRPC$TL_messages_stickerSet2, true, i);
                } finally {
                    if (sQLiteCursor != null) {
                        sQLiteCursor.dispose();
                    }
                }
            }
        } catch (Throwable th3) {
            th = th3;
            tLRPC$TL_messages_stickerSet = null;
        }
        processLoadedDiceStickers(str, z, tLRPC$TL_messages_stickerSet2, true, i);
    }

    public /* synthetic */ void lambda$loadStickersByEmojiOrName$59(String str, boolean z, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda100(this, tLRPC$TL_error, tLObject, str, z));
    }

    public /* synthetic */ void lambda$loadStickersByEmojiOrName$58(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, String str, boolean z) {
        if (tLObject instanceof TLRPC$TL_messages_stickerSet) {
            processLoadedDiceStickers(str, z, (TLRPC$TL_messages_stickerSet) tLObject, false, (int) (System.currentTimeMillis() / 1000));
        } else {
            processLoadedDiceStickers(str, z, null, false, (int) (System.currentTimeMillis() / 1000));
        }
    }

    public /* synthetic */ void lambda$processLoadedDiceStickers$60(String str) {
        this.loadingDiceStickerSets.remove(str);
    }

    private void processLoadedDiceStickers(String str, boolean z, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet, boolean z2, int i) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda56(this, str));
        Utilities.stageQueue.postRunnable(new MediaDataController$$ExternalSyntheticLambda117(this, z2, tLRPC$TL_messages_stickerSet, i, str, z));
    }

    public /* synthetic */ void lambda$processLoadedDiceStickers$63(boolean z, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet, int i, String str, boolean z2) {
        long j = 1000;
        if ((z && (tLRPC$TL_messages_stickerSet == null || Math.abs((System.currentTimeMillis() / 1000) - i) >= 86400)) || (!z && tLRPC$TL_messages_stickerSet == null)) {
            MediaDataController$$ExternalSyntheticLambda62 mediaDataController$$ExternalSyntheticLambda62 = new MediaDataController$$ExternalSyntheticLambda62(this, str, z2);
            if (tLRPC$TL_messages_stickerSet != null || z) {
                j = 0;
            }
            AndroidUtilities.runOnUIThread(mediaDataController$$ExternalSyntheticLambda62, j);
            if (tLRPC$TL_messages_stickerSet == null) {
                return;
            }
        }
        if (tLRPC$TL_messages_stickerSet != null) {
            if (!z) {
                putDiceStickersToCache(str, tLRPC$TL_messages_stickerSet, i);
            }
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda61(this, str, tLRPC$TL_messages_stickerSet));
        } else if (z) {
        } else {
            putDiceStickersToCache(str, null, i);
        }
    }

    public /* synthetic */ void lambda$processLoadedDiceStickers$61(String str, boolean z) {
        loadStickersByEmojiOrName(str, z, false);
    }

    public /* synthetic */ void lambda$processLoadedDiceStickers$62(String str, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        this.diceStickerSetsByEmoji.put(str, tLRPC$TL_messages_stickerSet);
        this.diceEmojiStickerSetsById.put(tLRPC$TL_messages_stickerSet.set.id, str);
        getNotificationCenter().postNotificationName(NotificationCenter.diceStickersDidLoad, str);
    }

    private void putDiceStickersToCache(String str, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet, int i) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda106(this, tLRPC$TL_messages_stickerSet, str, i));
    }

    public /* synthetic */ void lambda$putDiceStickersToCache$64(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet, String str, int i) {
        try {
            if (tLRPC$TL_messages_stickerSet != null) {
                SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO stickers_dice VALUES(?, ?, ?)");
                executeFast.requery();
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tLRPC$TL_messages_stickerSet.getObjectSize());
                tLRPC$TL_messages_stickerSet.serializeToStream(nativeByteBuffer);
                executeFast.bindString(1, str);
                executeFast.bindByteBuffer(2, nativeByteBuffer);
                executeFast.bindInteger(3, i);
                executeFast.step();
                nativeByteBuffer.reuse();
                executeFast.dispose();
            } else {
                SQLitePreparedStatement executeFast2 = getMessagesStorage().getDatabase().executeFast("UPDATE stickers_dice SET date = ?");
                executeFast2.requery();
                executeFast2.bindInteger(1, i);
                executeFast2.step();
                executeFast2.dispose();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void loadStickers(int i, boolean z, boolean z2) {
        loadStickers(i, z, z2, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void loadStickers(int i, boolean z, boolean z2, boolean z3) {
        TLRPC$TL_messages_getMaskStickers tLRPC$TL_messages_getMaskStickers;
        if (this.loadingStickers[i]) {
            if (!z3) {
                return;
            }
            this.scheduledLoadStickers[i] = new MediaDataController$$ExternalSyntheticLambda27(this, i, z2);
            return;
        }
        if (i == 3) {
            if (this.featuredStickerSets.isEmpty() || !getMessagesController().preloadFeaturedStickers) {
                return;
            }
        } else if (i != 4) {
            loadArchivedStickersCount(i, z);
        }
        this.loadingStickers[i] = true;
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda19(this, i));
        } else if (i == 3) {
            TLRPC$TL_messages_allStickers tLRPC$TL_messages_allStickers = new TLRPC$TL_messages_allStickers();
            tLRPC$TL_messages_allStickers.hash = this.loadFeaturedHash;
            int size = this.featuredStickerSets.size();
            for (int i2 = 0; i2 < size; i2++) {
                tLRPC$TL_messages_allStickers.sets.add(this.featuredStickerSets.get(i2).set);
            }
            processLoadStickersResponse(i, tLRPC$TL_messages_allStickers);
        } else if (i == 4) {
            TLRPC$TL_messages_getStickerSet tLRPC$TL_messages_getStickerSet = new TLRPC$TL_messages_getStickerSet();
            tLRPC$TL_messages_getStickerSet.stickerset = new TLRPC$TL_inputStickerSetAnimatedEmoji();
            getConnectionsManager().sendRequest(tLRPC$TL_messages_getStickerSet, new MediaDataController$$ExternalSyntheticLambda143(this, i));
        } else {
            long j = 0;
            if (i == 0) {
                TLRPC$TL_messages_getAllStickers tLRPC$TL_messages_getAllStickers = new TLRPC$TL_messages_getAllStickers();
                if (!z2) {
                    j = this.loadHash[i];
                }
                tLRPC$TL_messages_getAllStickers.hash = j;
                tLRPC$TL_messages_getMaskStickers = tLRPC$TL_messages_getAllStickers;
            } else {
                TLRPC$TL_messages_getMaskStickers tLRPC$TL_messages_getMaskStickers2 = new TLRPC$TL_messages_getMaskStickers();
                if (!z2) {
                    j = this.loadHash[i];
                }
                tLRPC$TL_messages_getMaskStickers2.hash = j;
                tLRPC$TL_messages_getMaskStickers = tLRPC$TL_messages_getMaskStickers2;
            }
            getConnectionsManager().sendRequest(tLRPC$TL_messages_getMaskStickers, new MediaDataController$$ExternalSyntheticLambda145(this, i, j));
        }
    }

    public /* synthetic */ void lambda$loadStickers$65(int i, boolean z) {
        loadStickers(i, false, z, false);
    }

    public /* synthetic */ void lambda$loadStickers$66(int i) {
        ArrayList<TLRPC$TL_messages_stickerSet> arrayList;
        SQLiteCursor sQLiteCursor;
        Throwable th;
        int i2;
        long j;
        ArrayList<TLRPC$TL_messages_stickerSet> arrayList2;
        ArrayList<TLRPC$TL_messages_stickerSet> arrayList3 = null;
        int i3 = 0;
        long j2 = 0;
        try {
            sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT data, date, hash FROM stickers_v2 WHERE id = " + (i + 1), new Object[0]);
            try {
                if (sQLiteCursor.next()) {
                    NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
                    if (byteBufferValue != null) {
                        arrayList = new ArrayList<>();
                        try {
                            int readInt32 = byteBufferValue.readInt32(false);
                            for (int i4 = 0; i4 < readInt32; i4++) {
                                arrayList.add(TLRPC$messages_StickerSet.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false));
                            }
                            byteBufferValue.reuse();
                            arrayList3 = arrayList;
                        } catch (Throwable th2) {
                            th = th2;
                            try {
                                FileLog.e(th);
                                i2 = i3;
                                j = 0;
                                arrayList2 = arrayList;
                                processLoadedStickers(i, arrayList2, true, i2, j);
                            } finally {
                                if (sQLiteCursor != null) {
                                    sQLiteCursor.dispose();
                                }
                            }
                        }
                    }
                    i3 = sQLiteCursor.intValue(1);
                    j2 = calcStickersHash(arrayList3);
                }
                sQLiteCursor.dispose();
                arrayList2 = arrayList3;
                i2 = i3;
                j = j2;
            } catch (Throwable th3) {
                arrayList = arrayList3;
                th = th3;
            }
        } catch (Throwable th4) {
            arrayList = null;
            th = th4;
            sQLiteCursor = null;
        }
        processLoadedStickers(i, arrayList2, true, i2, j);
    }

    public /* synthetic */ void lambda$loadStickers$67(int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject instanceof TLRPC$TL_messages_stickerSet) {
            ArrayList<TLRPC$TL_messages_stickerSet> arrayList = new ArrayList<>();
            arrayList.add((TLRPC$TL_messages_stickerSet) tLObject);
            processLoadedStickers(i, arrayList, false, (int) (System.currentTimeMillis() / 1000), calcStickersHash(arrayList));
            return;
        }
        processLoadedStickers(i, null, false, (int) (System.currentTimeMillis() / 1000), 0L);
    }

    public /* synthetic */ void lambda$loadStickers$69(int i, long j, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda81(this, tLObject, i, j));
    }

    public /* synthetic */ void lambda$loadStickers$68(TLObject tLObject, int i, long j) {
        if (tLObject instanceof TLRPC$TL_messages_allStickers) {
            processLoadStickersResponse(i, (TLRPC$TL_messages_allStickers) tLObject);
        } else {
            processLoadedStickers(i, null, false, (int) (System.currentTimeMillis() / 1000), j);
        }
    }

    private void putStickersToCache(int i, ArrayList<TLRPC$TL_messages_stickerSet> arrayList, int i2, long j) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda67(this, arrayList != null ? new ArrayList(arrayList) : null, i, i2, j));
    }

    public /* synthetic */ void lambda$putStickersToCache$70(ArrayList arrayList, int i, int i2, long j) {
        try {
            if (arrayList != null) {
                SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO stickers_v2 VALUES(?, ?, ?, ?)");
                executeFast.requery();
                int i3 = 4;
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    i3 += ((TLRPC$TL_messages_stickerSet) arrayList.get(i4)).getObjectSize();
                }
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(i3);
                nativeByteBuffer.writeInt32(arrayList.size());
                for (int i5 = 0; i5 < arrayList.size(); i5++) {
                    ((TLRPC$TL_messages_stickerSet) arrayList.get(i5)).serializeToStream(nativeByteBuffer);
                }
                executeFast.bindInteger(1, i + 1);
                executeFast.bindByteBuffer(2, nativeByteBuffer);
                executeFast.bindInteger(3, i2);
                executeFast.bindLong(4, j);
                executeFast.step();
                nativeByteBuffer.reuse();
                executeFast.dispose();
                return;
            }
            SQLitePreparedStatement executeFast2 = getMessagesStorage().getDatabase().executeFast("UPDATE stickers_v2 SET date = ?");
            executeFast2.requery();
            executeFast2.bindLong(1, i2);
            executeFast2.step();
            executeFast2.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public String getStickerSetName(long j) {
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = this.stickerSetsById.get(j);
        if (tLRPC$TL_messages_stickerSet != null) {
            return tLRPC$TL_messages_stickerSet.set.short_name;
        }
        TLRPC$StickerSetCovered tLRPC$StickerSetCovered = this.featuredStickerSetsById.get(j);
        if (tLRPC$StickerSetCovered == null) {
            return null;
        }
        return tLRPC$StickerSetCovered.set.short_name;
    }

    public static long getStickerSetId(TLRPC$Document tLRPC$Document) {
        for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                TLRPC$InputStickerSet tLRPC$InputStickerSet = tLRPC$DocumentAttribute.stickerset;
                if (!(tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetID)) {
                    return -1L;
                }
                return tLRPC$InputStickerSet.id;
            }
        }
        return -1L;
    }

    public static TLRPC$InputStickerSet getInputStickerSet(TLRPC$Document tLRPC$Document) {
        for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                TLRPC$InputStickerSet tLRPC$InputStickerSet = tLRPC$DocumentAttribute.stickerset;
                if (!(tLRPC$InputStickerSet instanceof TLRPC$TL_inputStickerSetEmpty)) {
                    return tLRPC$InputStickerSet;
                }
                return null;
            }
        }
        return null;
    }

    private static long calcStickersHash(ArrayList<TLRPC$TL_messages_stickerSet> arrayList) {
        long j = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            TLRPC$StickerSet tLRPC$StickerSet = arrayList.get(i).set;
            if (!tLRPC$StickerSet.archived) {
                j = calcHash(j, tLRPC$StickerSet.hash);
            }
        }
        return j;
    }

    private void processLoadedStickers(int i, ArrayList<TLRPC$TL_messages_stickerSet> arrayList, boolean z, int i2, long j) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda20(this, i));
        Utilities.stageQueue.postRunnable(new MediaDataController$$ExternalSyntheticLambda114(this, z, arrayList, i2, j, i));
    }

    public /* synthetic */ void lambda$processLoadedStickers$71(int i) {
        this.loadingStickers[i] = false;
        this.stickersLoaded[i] = true;
        Runnable[] runnableArr = this.scheduledLoadStickers;
        if (runnableArr[i] != null) {
            runnableArr[i].run();
            this.scheduledLoadStickers[i] = null;
        }
    }

    public /* synthetic */ void lambda$processLoadedStickers$75(boolean z, ArrayList arrayList, int i, long j, int i2) {
        int i3;
        String str;
        int i4;
        MediaDataController mediaDataController = this;
        ArrayList arrayList2 = arrayList;
        long j2 = 1000;
        if ((z && (arrayList2 == null || BuildVars.DEBUG_PRIVATE_VERSION || Math.abs((System.currentTimeMillis() / 1000) - i) >= 3600)) || (!z && arrayList2 == null && j == 0)) {
            MediaDataController$$ExternalSyntheticLambda71 mediaDataController$$ExternalSyntheticLambda71 = new MediaDataController$$ExternalSyntheticLambda71(this, arrayList, j, i2);
            if (arrayList2 != null || z) {
                j2 = 0;
            }
            AndroidUtilities.runOnUIThread(mediaDataController$$ExternalSyntheticLambda71, j2);
            if (arrayList2 == null) {
                return;
            }
        }
        if (arrayList2 != null) {
            try {
                ArrayList<TLRPC$TL_messages_stickerSet> arrayList3 = new ArrayList<>();
                LongSparseArray longSparseArray = new LongSparseArray();
                HashMap hashMap = new HashMap();
                LongSparseArray longSparseArray2 = new LongSparseArray();
                LongSparseArray longSparseArray3 = new LongSparseArray();
                HashMap hashMap2 = new HashMap();
                int i5 = 0;
                while (i5 < arrayList.size()) {
                    TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = (TLRPC$TL_messages_stickerSet) arrayList2.get(i5);
                    if (tLRPC$TL_messages_stickerSet != null && mediaDataController.removingStickerSetsUndos.indexOfKey(tLRPC$TL_messages_stickerSet.set.id) < 0) {
                        arrayList3.add(tLRPC$TL_messages_stickerSet);
                        longSparseArray.put(tLRPC$TL_messages_stickerSet.set.id, tLRPC$TL_messages_stickerSet);
                        hashMap.put(tLRPC$TL_messages_stickerSet.set.short_name, tLRPC$TL_messages_stickerSet);
                        int i6 = 0;
                        while (i6 < tLRPC$TL_messages_stickerSet.documents.size()) {
                            TLRPC$Document tLRPC$Document = tLRPC$TL_messages_stickerSet.documents.get(i6);
                            if (tLRPC$Document != null && !(tLRPC$Document instanceof TLRPC$TL_documentEmpty)) {
                                i4 = i5;
                                longSparseArray3.put(tLRPC$Document.id, tLRPC$Document);
                                i6++;
                                i5 = i4;
                            }
                            i4 = i5;
                            i6++;
                            i5 = i4;
                        }
                        i3 = i5;
                        if (!tLRPC$TL_messages_stickerSet.set.archived) {
                            int i7 = 0;
                            while (i7 < tLRPC$TL_messages_stickerSet.packs.size()) {
                                TLRPC$TL_stickerPack tLRPC$TL_stickerPack = tLRPC$TL_messages_stickerSet.packs.get(i7);
                                if (tLRPC$TL_stickerPack != null && (str = tLRPC$TL_stickerPack.emoticon) != null) {
                                    String replace = str.replace("️", "");
                                    tLRPC$TL_stickerPack.emoticon = replace;
                                    ArrayList arrayList4 = (ArrayList) hashMap2.get(replace);
                                    if (arrayList4 == null) {
                                        arrayList4 = new ArrayList();
                                        hashMap2.put(tLRPC$TL_stickerPack.emoticon, arrayList4);
                                    }
                                    int i8 = 0;
                                    while (i8 < tLRPC$TL_stickerPack.documents.size()) {
                                        Long l = tLRPC$TL_stickerPack.documents.get(i8);
                                        HashMap hashMap3 = hashMap2;
                                        if (longSparseArray2.indexOfKey(l.longValue()) < 0) {
                                            longSparseArray2.put(l.longValue(), tLRPC$TL_stickerPack.emoticon);
                                        }
                                        TLRPC$Document tLRPC$Document2 = (TLRPC$Document) longSparseArray3.get(l.longValue());
                                        if (tLRPC$Document2 != null) {
                                            arrayList4.add(tLRPC$Document2);
                                        }
                                        i8++;
                                        hashMap2 = hashMap3;
                                    }
                                }
                                i7++;
                                hashMap2 = hashMap2;
                            }
                        }
                        i5 = i3 + 1;
                        mediaDataController = this;
                        arrayList2 = arrayList;
                        hashMap2 = hashMap2;
                    }
                    i3 = i5;
                    i5 = i3 + 1;
                    mediaDataController = this;
                    arrayList2 = arrayList;
                    hashMap2 = hashMap2;
                }
                HashMap hashMap4 = hashMap2;
                if (!z) {
                    putStickersToCache(i2, arrayList3, i, j);
                }
                AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda23(this, i2, longSparseArray, hashMap, arrayList3, j, i, longSparseArray3, hashMap4, longSparseArray2));
            } catch (Throwable th) {
                FileLog.e(th);
            }
        } else if (!z) {
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda21(this, i2, i));
            putStickersToCache(i2, null, i, 0L);
        }
    }

    public /* synthetic */ void lambda$processLoadedStickers$72(ArrayList arrayList, long j, int i) {
        if (arrayList != null && j != 0) {
            this.loadHash[i] = j;
        }
        loadStickers(i, false, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$processLoadedStickers$73(int i, LongSparseArray longSparseArray, HashMap hashMap, ArrayList arrayList, long j, int i2, LongSparseArray longSparseArray2, HashMap hashMap2, LongSparseArray longSparseArray3) {
        for (int i3 = 0; i3 < this.stickerSets[i].size(); i3++) {
            TLRPC$StickerSet tLRPC$StickerSet = this.stickerSets[i].get(i3).set;
            this.stickerSetsById.remove(tLRPC$StickerSet.id);
            this.stickerSetsByName.remove(tLRPC$StickerSet.short_name);
            if (i != 3 && i != 4) {
                this.installedStickerSetsById.remove(tLRPC$StickerSet.id);
            }
        }
        for (int i4 = 0; i4 < longSparseArray.size(); i4++) {
            this.stickerSetsById.put(longSparseArray.keyAt(i4), (TLRPC$TL_messages_stickerSet) longSparseArray.valueAt(i4));
            if (i != 3 && i != 4) {
                this.installedStickerSetsById.put(longSparseArray.keyAt(i4), (TLRPC$TL_messages_stickerSet) longSparseArray.valueAt(i4));
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
        getNotificationCenter().postNotificationName(NotificationCenter.stickersDidLoad, Integer.valueOf(i));
    }

    public /* synthetic */ void lambda$processLoadedStickers$74(int i, int i2) {
        this.loadDate[i] = i2;
    }

    public boolean cancelRemovingStickerSet(long j) {
        Runnable runnable = this.removingStickerSetsUndos.get(j);
        if (runnable != null) {
            runnable.run();
            return true;
        }
        return false;
    }

    public void preloadStickerSetThumb(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        ArrayList<TLRPC$Document> arrayList;
        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$TL_messages_stickerSet.set.thumbs, 90);
        if (closestPhotoSizeWithSize == null || (arrayList = tLRPC$TL_messages_stickerSet.documents) == null || arrayList.isEmpty()) {
            return;
        }
        loadStickerSetThumbInternal(closestPhotoSizeWithSize, tLRPC$TL_messages_stickerSet, arrayList.get(0), tLRPC$TL_messages_stickerSet.set.thumb_version);
    }

    public void preloadStickerSetThumb(TLRPC$StickerSetCovered tLRPC$StickerSetCovered) {
        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$StickerSetCovered.set.thumbs, 90);
        if (closestPhotoSizeWithSize != null) {
            TLRPC$Document tLRPC$Document = tLRPC$StickerSetCovered.cover;
            if (tLRPC$Document == null) {
                if (tLRPC$StickerSetCovered.covers.isEmpty()) {
                    return;
                }
                tLRPC$Document = tLRPC$StickerSetCovered.covers.get(0);
            }
            loadStickerSetThumbInternal(closestPhotoSizeWithSize, tLRPC$StickerSetCovered, tLRPC$Document, tLRPC$StickerSetCovered.set.thumb_version);
        }
    }

    private void loadStickerSetThumbInternal(TLRPC$PhotoSize tLRPC$PhotoSize, Object obj, TLRPC$Document tLRPC$Document, int i) {
        ImageLocation forSticker = ImageLocation.getForSticker(tLRPC$PhotoSize, tLRPC$Document, i);
        if (forSticker != null) {
            getFileLoader().loadFile(forSticker, obj, forSticker.imageType == 1 ? "tgs" : "webp", 2, 1);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v0, types: [boolean, int] */
    public void toggleStickerSet(Context context, TLObject tLObject, int i, BaseFragment baseFragment, boolean z, boolean z2) {
        TLRPC$StickerSet tLRPC$StickerSet;
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet;
        int i2;
        TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet2;
        if (tLObject instanceof TLRPC$TL_messages_stickerSet) {
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet3 = (TLRPC$TL_messages_stickerSet) tLObject;
            tLRPC$TL_messages_stickerSet = tLRPC$TL_messages_stickerSet3;
            tLRPC$StickerSet = tLRPC$TL_messages_stickerSet3.set;
        } else if (tLObject instanceof TLRPC$StickerSetCovered) {
            TLRPC$StickerSet tLRPC$StickerSet2 = ((TLRPC$StickerSetCovered) tLObject).set;
            if (i != 2) {
                tLRPC$TL_messages_stickerSet2 = this.stickerSetsById.get(tLRPC$StickerSet2.id);
                if (tLRPC$TL_messages_stickerSet2 == null) {
                    return;
                }
            } else {
                tLRPC$TL_messages_stickerSet2 = null;
            }
            tLRPC$StickerSet = tLRPC$StickerSet2;
            tLRPC$TL_messages_stickerSet = tLRPC$TL_messages_stickerSet2;
        } else {
            throw new IllegalArgumentException("Invalid type of the given stickerSetObject: " + tLObject.getClass());
        }
        ?? r14 = tLRPC$StickerSet.masks;
        tLRPC$StickerSet.archived = i == 1;
        int i3 = 0;
        while (true) {
            if (i3 >= this.stickerSets[r14 == true ? 1 : 0].size()) {
                i2 = 0;
                break;
            }
            TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet4 = this.stickerSets[r14].get(i3);
            if (tLRPC$TL_messages_stickerSet4.set.id == tLRPC$StickerSet.id) {
                this.stickerSets[r14].remove(i3);
                if (i == 2) {
                    this.stickerSets[r14].add(0, tLRPC$TL_messages_stickerSet4);
                } else {
                    this.stickerSetsById.remove(tLRPC$TL_messages_stickerSet4.set.id);
                    this.installedStickerSetsById.remove(tLRPC$TL_messages_stickerSet4.set.id);
                    this.stickerSetsByName.remove(tLRPC$TL_messages_stickerSet4.set.short_name);
                }
                i2 = i3;
            } else {
                i3++;
            }
        }
        this.loadHash[r14] = calcStickersHash(this.stickerSets[r14]);
        putStickersToCache(r14, this.stickerSets[r14], this.loadDate[r14], this.loadHash[r14]);
        getNotificationCenter().postNotificationName(NotificationCenter.stickersDidLoad, Integer.valueOf(r14 == true ? 1 : 0));
        if (i == 2) {
            if (cancelRemovingStickerSet(tLRPC$StickerSet.id)) {
                return;
            }
            toggleStickerSetInternal(context, i, baseFragment, z, tLObject, tLRPC$StickerSet, r14, z2);
        } else if (!z2 || baseFragment == null) {
            toggleStickerSetInternal(context, i, baseFragment, z, tLObject, tLRPC$StickerSet, r14, false);
        } else {
            StickerSetBulletinLayout stickerSetBulletinLayout = new StickerSetBulletinLayout(context, tLObject, i);
            Bulletin.UndoButton delayedAction = new Bulletin.UndoButton(context, false).setUndoAction(new MediaDataController$$ExternalSyntheticLambda93(this, tLRPC$StickerSet, r14, i2, tLRPC$TL_messages_stickerSet)).setDelayedAction(new MediaDataController$$ExternalSyntheticLambda51(this, context, i, baseFragment, z, tLObject, tLRPC$StickerSet, r14));
            stickerSetBulletinLayout.setButton(delayedAction);
            LongSparseArray<Runnable> longSparseArray = this.removingStickerSetsUndos;
            long j = tLRPC$StickerSet.id;
            delayedAction.getClass();
            longSparseArray.put(j, new MediaDataController$$ExternalSyntheticLambda121(delayedAction));
            Bulletin.make(baseFragment, stickerSetBulletinLayout, 2750).show();
        }
    }

    public /* synthetic */ void lambda$toggleStickerSet$76(TLRPC$StickerSet tLRPC$StickerSet, int i, int i2, TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        tLRPC$StickerSet.archived = false;
        this.stickerSets[i].add(i2, tLRPC$TL_messages_stickerSet);
        this.stickerSetsById.put(tLRPC$StickerSet.id, tLRPC$TL_messages_stickerSet);
        this.installedStickerSetsById.put(tLRPC$StickerSet.id, tLRPC$TL_messages_stickerSet);
        this.stickerSetsByName.put(tLRPC$StickerSet.short_name, tLRPC$TL_messages_stickerSet);
        this.removingStickerSetsUndos.remove(tLRPC$StickerSet.id);
        this.loadHash[i] = calcStickersHash(this.stickerSets[i]);
        putStickersToCache(i, this.stickerSets[i], this.loadDate[i], this.loadHash[i]);
        getNotificationCenter().postNotificationName(NotificationCenter.stickersDidLoad, Integer.valueOf(i));
    }

    public /* synthetic */ void lambda$toggleStickerSet$77(Context context, int i, BaseFragment baseFragment, boolean z, TLObject tLObject, TLRPC$StickerSet tLRPC$StickerSet, int i2) {
        toggleStickerSetInternal(context, i, baseFragment, z, tLObject, tLRPC$StickerSet, i2, false);
    }

    private void toggleStickerSetInternal(Context context, int i, BaseFragment baseFragment, boolean z, TLObject tLObject, TLRPC$StickerSet tLRPC$StickerSet, int i2, boolean z2) {
        TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID = new TLRPC$TL_inputStickerSetID();
        tLRPC$TL_inputStickerSetID.access_hash = tLRPC$StickerSet.access_hash;
        tLRPC$TL_inputStickerSetID.id = tLRPC$StickerSet.id;
        if (i != 0) {
            TLRPC$TL_messages_installStickerSet tLRPC$TL_messages_installStickerSet = new TLRPC$TL_messages_installStickerSet();
            tLRPC$TL_messages_installStickerSet.stickerset = tLRPC$TL_inputStickerSetID;
            boolean z3 = true;
            if (i != 1) {
                z3 = false;
            }
            tLRPC$TL_messages_installStickerSet.archived = z3;
            getConnectionsManager().sendRequest(tLRPC$TL_messages_installStickerSet, new MediaDataController$$ExternalSyntheticLambda167(this, tLRPC$StickerSet, baseFragment, z, i2, z2, context, tLObject));
            return;
        }
        TLRPC$TL_messages_uninstallStickerSet tLRPC$TL_messages_uninstallStickerSet = new TLRPC$TL_messages_uninstallStickerSet();
        tLRPC$TL_messages_uninstallStickerSet.stickerset = tLRPC$TL_inputStickerSetID;
        getConnectionsManager().sendRequest(tLRPC$TL_messages_uninstallStickerSet, new MediaDataController$$ExternalSyntheticLambda166(this, tLRPC$StickerSet, i2));
    }

    public /* synthetic */ void lambda$toggleStickerSetInternal$79(TLRPC$StickerSet tLRPC$StickerSet, BaseFragment baseFragment, boolean z, int i, boolean z2, Context context, TLObject tLObject, TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda94(this, tLRPC$StickerSet, tLObject2, baseFragment, z, i, tLRPC$TL_error, z2, context, tLObject));
    }

    public /* synthetic */ void lambda$toggleStickerSetInternal$78(TLRPC$StickerSet tLRPC$StickerSet, TLObject tLObject, BaseFragment baseFragment, boolean z, int i, TLRPC$TL_error tLRPC$TL_error, boolean z2, Context context, TLObject tLObject2) {
        this.removingStickerSetsUndos.remove(tLRPC$StickerSet.id);
        if (tLObject instanceof TLRPC$TL_messages_stickerSetInstallResultArchive) {
            processStickerSetInstallResultArchive(baseFragment, z, i, (TLRPC$TL_messages_stickerSetInstallResultArchive) tLObject);
        }
        loadStickers(i, false, false, true);
        if (tLRPC$TL_error != null || !z2 || baseFragment == null) {
            return;
        }
        Bulletin.make(baseFragment, new StickerSetBulletinLayout(context, tLObject2, 2), 1500).show();
    }

    public /* synthetic */ void lambda$toggleStickerSetInternal$81(TLRPC$StickerSet tLRPC$StickerSet, int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda92(this, tLRPC$StickerSet, i));
    }

    public /* synthetic */ void lambda$toggleStickerSetInternal$80(TLRPC$StickerSet tLRPC$StickerSet, int i) {
        this.removingStickerSetsUndos.remove(tLRPC$StickerSet.id);
        loadStickers(i, false, true);
    }

    public void toggleStickerSets(ArrayList<TLRPC$StickerSet> arrayList, int i, int i2, BaseFragment baseFragment, boolean z) {
        int size = arrayList.size();
        ArrayList<TLRPC$InputStickerSet> arrayList2 = new ArrayList<>(size);
        int i3 = 0;
        while (true) {
            boolean z2 = true;
            if (i3 >= size) {
                break;
            }
            TLRPC$StickerSet tLRPC$StickerSet = arrayList.get(i3);
            TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID = new TLRPC$TL_inputStickerSetID();
            tLRPC$TL_inputStickerSetID.access_hash = tLRPC$StickerSet.access_hash;
            tLRPC$TL_inputStickerSetID.id = tLRPC$StickerSet.id;
            arrayList2.add(tLRPC$TL_inputStickerSetID);
            if (i2 != 0) {
                if (i2 != 1) {
                    z2 = false;
                }
                tLRPC$StickerSet.archived = z2;
            }
            int size2 = this.stickerSets[i].size();
            int i4 = 0;
            while (true) {
                if (i4 < size2) {
                    TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = this.stickerSets[i].get(i4);
                    if (tLRPC$TL_messages_stickerSet.set.id == tLRPC$TL_inputStickerSetID.id) {
                        this.stickerSets[i].remove(i4);
                        if (i2 == 2) {
                            this.stickerSets[i].add(0, tLRPC$TL_messages_stickerSet);
                        } else {
                            this.stickerSetsById.remove(tLRPC$TL_messages_stickerSet.set.id);
                            this.installedStickerSetsById.remove(tLRPC$TL_messages_stickerSet.set.id);
                            this.stickerSetsByName.remove(tLRPC$TL_messages_stickerSet.set.short_name);
                        }
                    } else {
                        i4++;
                    }
                }
            }
            i3++;
        }
        this.loadHash[i] = calcStickersHash(this.stickerSets[i]);
        putStickersToCache(i, this.stickerSets[i], this.loadDate[i], this.loadHash[i]);
        getNotificationCenter().postNotificationName(NotificationCenter.stickersDidLoad, Integer.valueOf(i));
        TLRPC$TL_messages_toggleStickerSets tLRPC$TL_messages_toggleStickerSets = new TLRPC$TL_messages_toggleStickerSets();
        tLRPC$TL_messages_toggleStickerSets.stickersets = arrayList2;
        if (i2 == 0) {
            tLRPC$TL_messages_toggleStickerSets.uninstall = true;
        } else if (i2 == 1) {
            tLRPC$TL_messages_toggleStickerSets.archive = true;
        } else if (i2 == 2) {
            tLRPC$TL_messages_toggleStickerSets.unarchive = true;
        }
        getConnectionsManager().sendRequest(tLRPC$TL_messages_toggleStickerSets, new MediaDataController$$ExternalSyntheticLambda148(this, i2, baseFragment, z, i));
    }

    public /* synthetic */ void lambda$toggleStickerSets$83(int i, BaseFragment baseFragment, boolean z, int i2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda25(this, i, tLObject, baseFragment, z, i2));
    }

    public /* synthetic */ void lambda$toggleStickerSets$82(int i, TLObject tLObject, BaseFragment baseFragment, boolean z, int i2) {
        if (i != 0) {
            if (tLObject instanceof TLRPC$TL_messages_stickerSetInstallResultArchive) {
                processStickerSetInstallResultArchive(baseFragment, z, i2, (TLRPC$TL_messages_stickerSetInstallResultArchive) tLObject);
            }
            loadStickers(i2, false, false, true);
            return;
        }
        loadStickers(i2, false, true);
    }

    public void processStickerSetInstallResultArchive(BaseFragment baseFragment, boolean z, int i, TLRPC$TL_messages_stickerSetInstallResultArchive tLRPC$TL_messages_stickerSetInstallResultArchive) {
        int size = tLRPC$TL_messages_stickerSetInstallResultArchive.sets.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.installedStickerSetsById.remove(tLRPC$TL_messages_stickerSetInstallResultArchive.sets.get(i2).set.id);
        }
        loadArchivedStickersCount(i, false);
        getNotificationCenter().postNotificationName(NotificationCenter.needAddArchivedStickers, tLRPC$TL_messages_stickerSetInstallResultArchive.sets);
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        baseFragment.showDialog(new StickersArchiveAlert(baseFragment.getParentActivity(), z ? baseFragment : null, tLRPC$TL_messages_stickerSetInstallResultArchive.sets).create());
    }

    private int getMask() {
        int i = 1;
        if (this.lastReturnedNum >= this.searchResultMessages.size() - 1) {
            boolean[] zArr = this.messagesSearchEndReached;
            if (zArr[0] && zArr[1]) {
                i = 0;
            }
        }
        return this.lastReturnedNum > 0 ? i | 2 : i;
    }

    public ArrayList<MessageObject> getFoundMessageObjects() {
        return this.searchResultMessages;
    }

    public void clearFoundMessageObjects() {
        this.searchResultMessages.clear();
    }

    public boolean isMessageFound(int i, boolean z) {
        return this.searchResultMessagesMap[z ? 1 : 0].indexOfKey(i) >= 0;
    }

    public void searchMessagesInChat(String str, long j, long j2, int i, int i2, int i3, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat) {
        searchMessagesInChat(str, j, j2, i, i2, i3, false, tLRPC$User, tLRPC$Chat, true);
    }

    public void jumpToSearchedMessage(int i, int i2) {
        if (i2 < 0 || i2 >= this.searchResultMessages.size()) {
            return;
        }
        this.lastReturnedNum = i2;
        MessageObject messageObject = this.searchResultMessages.get(i2);
        NotificationCenter notificationCenter = getNotificationCenter();
        int i3 = NotificationCenter.chatSearchResultsAvailable;
        int[] iArr = this.messagesSearchCount;
        notificationCenter.postNotificationName(i3, Integer.valueOf(i), Integer.valueOf(messageObject.getId()), Integer.valueOf(getMask()), Long.valueOf(messageObject.getDialogId()), Integer.valueOf(this.lastReturnedNum), Integer.valueOf(iArr[0] + iArr[1]), Boolean.TRUE);
    }

    public void loadMoreSearchMessages() {
        if (!this.loadingMoreSearchMessages) {
            boolean[] zArr = this.messagesSearchEndReached;
            if (zArr[0] && this.lastMergeDialogId == 0 && zArr[1]) {
                return;
            }
            int size = this.searchResultMessages.size();
            this.lastReturnedNum = this.searchResultMessages.size();
            searchMessagesInChat(null, this.lastDialogId, this.lastMergeDialogId, this.lastGuid, 1, this.lastReplyMessageId, false, this.lastSearchUser, this.lastSearchChat, false);
            this.lastReturnedNum = size;
            this.loadingMoreSearchMessages = true;
        }
    }

    private void searchMessagesInChat(String str, long j, long j2, int i, int i2, int i3, boolean z, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, boolean z2) {
        long j3;
        int i4;
        String str2;
        long j4;
        String str3;
        long j5;
        int i5;
        boolean z3 = !z;
        if (this.reqId != 0) {
            getConnectionsManager().cancelRequest(this.reqId, true);
            this.reqId = 0;
        }
        if (this.mergeReqId != 0) {
            getConnectionsManager().cancelRequest(this.mergeReqId, true);
            this.mergeReqId = 0;
        }
        if (str != null) {
            if (z3) {
                boolean[] zArr = this.messagesSearchEndReached;
                zArr[1] = false;
                zArr[0] = false;
                int[] iArr = this.messagesSearchCount;
                iArr[1] = 0;
                iArr[0] = 0;
                this.searchResultMessages.clear();
                this.searchResultMessagesMap[0].clear();
                this.searchResultMessagesMap[1].clear();
                getNotificationCenter().postNotificationName(NotificationCenter.chatSearchResultsLoading, Integer.valueOf(i));
            }
            str2 = str;
            j3 = j;
            i4 = 0;
        } else if (this.searchResultMessages.isEmpty()) {
            return;
        } else {
            if (i2 != 1) {
                if (i2 != 2) {
                    return;
                }
                int i6 = this.lastReturnedNum - 1;
                this.lastReturnedNum = i6;
                if (i6 < 0) {
                    this.lastReturnedNum = 0;
                    return;
                }
                if (i6 >= this.searchResultMessages.size()) {
                    this.lastReturnedNum = this.searchResultMessages.size() - 1;
                }
                MessageObject messageObject = this.searchResultMessages.get(this.lastReturnedNum);
                NotificationCenter notificationCenter = getNotificationCenter();
                int i7 = NotificationCenter.chatSearchResultsAvailable;
                int[] iArr2 = this.messagesSearchCount;
                notificationCenter.postNotificationName(i7, Integer.valueOf(i), Integer.valueOf(messageObject.getId()), Integer.valueOf(getMask()), Long.valueOf(messageObject.getDialogId()), Integer.valueOf(this.lastReturnedNum), Integer.valueOf(iArr2[0] + iArr2[1]), Boolean.valueOf(z2));
                return;
            }
            int i8 = this.lastReturnedNum + 1;
            this.lastReturnedNum = i8;
            if (i8 < this.searchResultMessages.size()) {
                MessageObject messageObject2 = this.searchResultMessages.get(this.lastReturnedNum);
                NotificationCenter notificationCenter2 = getNotificationCenter();
                int i9 = NotificationCenter.chatSearchResultsAvailable;
                int[] iArr3 = this.messagesSearchCount;
                notificationCenter2.postNotificationName(i9, Integer.valueOf(i), Integer.valueOf(messageObject2.getId()), Integer.valueOf(getMask()), Long.valueOf(messageObject2.getDialogId()), Integer.valueOf(this.lastReturnedNum), Integer.valueOf(iArr3[0] + iArr3[1]), Boolean.valueOf(z2));
                return;
            }
            boolean[] zArr2 = this.messagesSearchEndReached;
            if (zArr2[0] && j2 == 0 && zArr2[1]) {
                this.lastReturnedNum--;
                return;
            }
            String str4 = this.lastSearchQuery;
            ArrayList<MessageObject> arrayList = this.searchResultMessages;
            MessageObject messageObject3 = arrayList.get(arrayList.size() - 1);
            if (messageObject3.getDialogId() == j && !this.messagesSearchEndReached[0]) {
                i5 = messageObject3.getId();
                j5 = j;
            } else {
                i5 = messageObject3.getDialogId() == j2 ? messageObject3.getId() : 0;
                this.messagesSearchEndReached[1] = false;
                j5 = j2;
            }
            j3 = j5;
            i4 = i5;
            str2 = str4;
            z3 = false;
        }
        boolean[] zArr3 = this.messagesSearchEndReached;
        if (!zArr3[0] || zArr3[1]) {
            j4 = 0;
        } else {
            j4 = 0;
            if (j2 != 0) {
                j3 = j2;
            }
        }
        if (j3 != j || !z3) {
            str3 = str2;
        } else if (j2 != j4) {
            TLRPC$InputPeer inputPeer = getMessagesController().getInputPeer(j2);
            if (inputPeer == null) {
                return;
            }
            TLRPC$TL_messages_search tLRPC$TL_messages_search = new TLRPC$TL_messages_search();
            tLRPC$TL_messages_search.peer = inputPeer;
            this.lastMergeDialogId = j2;
            tLRPC$TL_messages_search.limit = 1;
            tLRPC$TL_messages_search.q = str2;
            if (tLRPC$User != null) {
                tLRPC$TL_messages_search.from_id = MessagesController.getInputPeer(tLRPC$User);
                tLRPC$TL_messages_search.flags = 1 | tLRPC$TL_messages_search.flags;
            } else if (tLRPC$Chat != null) {
                tLRPC$TL_messages_search.from_id = MessagesController.getInputPeer(tLRPC$Chat);
                tLRPC$TL_messages_search.flags = 1 | tLRPC$TL_messages_search.flags;
            }
            tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterEmpty();
            this.mergeReqId = getConnectionsManager().sendRequest(tLRPC$TL_messages_search, new MediaDataController$$ExternalSyntheticLambda158(this, j2, tLRPC$TL_messages_search, j, i, i2, i3, tLRPC$User, tLRPC$Chat, z2), 2);
            return;
        } else {
            str3 = str2;
            this.lastMergeDialogId = 0L;
            zArr3[1] = true;
            this.messagesSearchCount[1] = 0;
        }
        TLRPC$TL_messages_search tLRPC$TL_messages_search2 = new TLRPC$TL_messages_search();
        TLRPC$InputPeer inputPeer2 = getMessagesController().getInputPeer(j3);
        tLRPC$TL_messages_search2.peer = inputPeer2;
        if (inputPeer2 == null) {
            return;
        }
        this.lastGuid = i;
        this.lastDialogId = j;
        this.lastSearchUser = tLRPC$User;
        this.lastSearchChat = tLRPC$Chat;
        this.lastReplyMessageId = i3;
        tLRPC$TL_messages_search2.limit = 21;
        tLRPC$TL_messages_search2.q = str3 != null ? str3 : "";
        tLRPC$TL_messages_search2.offset_id = i4;
        if (tLRPC$User != null) {
            tLRPC$TL_messages_search2.from_id = MessagesController.getInputPeer(tLRPC$User);
            tLRPC$TL_messages_search2.flags |= 1;
        } else if (tLRPC$Chat != null) {
            tLRPC$TL_messages_search2.from_id = MessagesController.getInputPeer(tLRPC$Chat);
            tLRPC$TL_messages_search2.flags |= 1;
        }
        int i10 = this.lastReplyMessageId;
        if (i10 != 0) {
            tLRPC$TL_messages_search2.top_msg_id = i10;
            tLRPC$TL_messages_search2.flags |= 2;
        }
        tLRPC$TL_messages_search2.filter = new TLRPC$TL_inputMessagesFilterEmpty();
        int i11 = this.lastReqId + 1;
        this.lastReqId = i11;
        this.lastSearchQuery = str3;
        this.reqId = getConnectionsManager().sendRequest(tLRPC$TL_messages_search2, new MediaDataController$$ExternalSyntheticLambda162(this, str3, i11, z2, tLRPC$TL_messages_search2, j3, j, i, j2, i3, tLRPC$User, tLRPC$Chat), 2);
    }

    public /* synthetic */ void lambda$searchMessagesInChat$85(long j, TLRPC$TL_messages_search tLRPC$TL_messages_search, long j2, int i, int i2, int i3, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, boolean z, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda45(this, j, tLObject, tLRPC$TL_messages_search, j2, i, i2, i3, tLRPC$User, tLRPC$Chat, z));
    }

    public /* synthetic */ void lambda$searchMessagesInChat$84(long j, TLObject tLObject, TLRPC$TL_messages_search tLRPC$TL_messages_search, long j2, int i, int i2, int i3, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, boolean z) {
        if (this.lastMergeDialogId == j) {
            this.mergeReqId = 0;
            if (tLObject != null) {
                TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
                this.messagesSearchEndReached[1] = tLRPC$messages_Messages.messages.isEmpty();
                this.messagesSearchCount[1] = tLRPC$messages_Messages instanceof TLRPC$TL_messages_messagesSlice ? tLRPC$messages_Messages.count : tLRPC$messages_Messages.messages.size();
                searchMessagesInChat(tLRPC$TL_messages_search.q, j2, j, i, i2, i3, true, tLRPC$User, tLRPC$Chat, z);
                return;
            }
            this.messagesSearchEndReached[1] = true;
            this.messagesSearchCount[1] = 0;
            searchMessagesInChat(tLRPC$TL_messages_search.q, j2, j, i, i2, i3, true, tLRPC$User, tLRPC$Chat, z);
        }
    }

    public /* synthetic */ void lambda$searchMessagesInChat$87(String str, int i, boolean z, TLRPC$TL_messages_search tLRPC$TL_messages_search, long j, long j2, int i2, long j3, int i3, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ArrayList arrayList = new ArrayList();
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            int min = Math.min(tLRPC$messages_Messages.messages.size(), 20);
            for (int i4 = 0; i4 < min; i4++) {
                MessageObject messageObject = new MessageObject(this.currentAccount, tLRPC$messages_Messages.messages.get(i4), false, false);
                messageObject.setQuery(str);
                arrayList.add(messageObject);
            }
        }
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda28(this, i, z, tLObject, tLRPC$TL_messages_search, j, j2, i2, arrayList, j3, i3, tLRPC$User, tLRPC$Chat));
    }

    public /* synthetic */ void lambda$searchMessagesInChat$86(int i, boolean z, TLObject tLObject, TLRPC$TL_messages_search tLRPC$TL_messages_search, long j, long j2, int i2, ArrayList arrayList, long j3, int i3, TLRPC$User tLRPC$User, TLRPC$Chat tLRPC$Chat) {
        if (i == this.lastReqId) {
            this.reqId = 0;
            if (!z) {
                this.loadingMoreSearchMessages = false;
            }
            if (tLObject == null) {
                return;
            }
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            int i4 = 0;
            while (i4 < tLRPC$messages_Messages.messages.size()) {
                TLRPC$Message tLRPC$Message = tLRPC$messages_Messages.messages.get(i4);
                if ((tLRPC$Message instanceof TLRPC$TL_messageEmpty) || (tLRPC$Message.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                    tLRPC$messages_Messages.messages.remove(i4);
                    i4--;
                }
                i4++;
            }
            getMessagesStorage().putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
            getMessagesController().putUsers(tLRPC$messages_Messages.users, false);
            getMessagesController().putChats(tLRPC$messages_Messages.chats, false);
            if (tLRPC$TL_messages_search.offset_id == 0 && j == j2) {
                this.lastReturnedNum = 0;
                this.searchResultMessages.clear();
                this.searchResultMessagesMap[0].clear();
                this.searchResultMessagesMap[1].clear();
                this.messagesSearchCount[0] = 0;
                getNotificationCenter().postNotificationName(NotificationCenter.chatSearchResultsLoading, Integer.valueOf(i2));
            }
            int min = Math.min(tLRPC$messages_Messages.messages.size(), 20);
            int i5 = 0;
            boolean z2 = false;
            while (i5 < min) {
                tLRPC$messages_Messages.messages.get(i5);
                MessageObject messageObject = (MessageObject) arrayList.get(i5);
                this.searchResultMessages.add(messageObject);
                this.searchResultMessagesMap[j == j2 ? (char) 0 : (char) 1].put(messageObject.getId(), messageObject);
                i5++;
                z2 = true;
            }
            this.messagesSearchEndReached[j == j2 ? (char) 0 : (char) 1] = tLRPC$messages_Messages.messages.size() < 21;
            this.messagesSearchCount[j == j2 ? (char) 0 : (char) 1] = ((tLRPC$messages_Messages instanceof TLRPC$TL_messages_messagesSlice) || (tLRPC$messages_Messages instanceof TLRPC$TL_messages_channelMessages)) ? tLRPC$messages_Messages.count : tLRPC$messages_Messages.messages.size();
            if (this.searchResultMessages.isEmpty()) {
                getNotificationCenter().postNotificationName(NotificationCenter.chatSearchResultsAvailable, Integer.valueOf(i2), 0, Integer.valueOf(getMask()), 0L, 0, 0, Boolean.valueOf(z));
            } else if (z2) {
                if (this.lastReturnedNum >= this.searchResultMessages.size()) {
                    this.lastReturnedNum = this.searchResultMessages.size() - 1;
                }
                MessageObject messageObject2 = this.searchResultMessages.get(this.lastReturnedNum);
                NotificationCenter notificationCenter = getNotificationCenter();
                int i6 = NotificationCenter.chatSearchResultsAvailable;
                int[] iArr = this.messagesSearchCount;
                notificationCenter.postNotificationName(i6, Integer.valueOf(i2), Integer.valueOf(messageObject2.getId()), Integer.valueOf(getMask()), Long.valueOf(messageObject2.getDialogId()), Integer.valueOf(this.lastReturnedNum), Integer.valueOf(iArr[0] + iArr[1]), Boolean.valueOf(z));
            }
            if (j != j2) {
                return;
            }
            boolean[] zArr = this.messagesSearchEndReached;
            if (!zArr[0] || j3 == 0 || zArr[1]) {
                return;
            }
            searchMessagesInChat(this.lastSearchQuery, j2, j3, i2, 0, i3, true, tLRPC$User, tLRPC$Chat, z);
        }
    }

    public String getLastSearchQuery() {
        return this.lastSearchQuery;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x002a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadMedia(long j, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        boolean z;
        if (DialogObject.isChatDialog(j) && ChatObject.isChannel(-j, this.currentAccount)) {
            z = true;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("load media did " + j + " count = " + i + " max_id " + i2 + " type = " + i4 + " cache = " + i5 + " classGuid = " + i6);
            }
            if (i5 == 0 || DialogObject.isEncryptedDialog(j)) {
                loadMediaDatabase(j, i, i2, i3, i4, i6, z, i5, i7);
            }
            TLRPC$TL_messages_search tLRPC$TL_messages_search = new TLRPC$TL_messages_search();
            tLRPC$TL_messages_search.limit = i;
            if (i3 != 0) {
                tLRPC$TL_messages_search.offset_id = i3;
                tLRPC$TL_messages_search.add_offset = -i;
            } else {
                tLRPC$TL_messages_search.offset_id = i2;
            }
            if (i4 == 0) {
                tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterPhotoVideo();
            } else if (i4 == 6) {
                tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterPhotos();
            } else if (i4 == 7) {
                tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterVideo();
            } else if (i4 == 1) {
                tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterDocument();
            } else if (i4 == 2) {
                tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterRoundVoice();
            } else if (i4 == 3) {
                tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterUrl();
            } else if (i4 == 4) {
                tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterMusic();
            } else if (i4 == 5) {
                tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterGif();
            }
            tLRPC$TL_messages_search.q = "";
            TLRPC$InputPeer inputPeer = getMessagesController().getInputPeer(j);
            tLRPC$TL_messages_search.peer = inputPeer;
            if (inputPeer == null) {
                return;
            }
            getConnectionsManager().bindRequestToGuid(getConnectionsManager().sendRequest(tLRPC$TL_messages_search, new MediaDataController$$ExternalSyntheticLambda152(this, j, i3, i, i2, i4, i6, z, i7)), i6);
            return;
        }
        z = false;
        if (BuildVars.LOGS_ENABLED) {
        }
        if (i5 == 0) {
        }
        loadMediaDatabase(j, i, i2, i3, i4, i6, z, i5, i7);
    }

    public /* synthetic */ void lambda$loadMedia$88(long j, int i, int i2, int i3, int i4, int i5, boolean z, int i6, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            getMessagesController().removeDeletedMessagesFromArray(j, tLRPC$messages_Messages.messages);
            boolean z2 = false;
            if (i == 0 ? tLRPC$messages_Messages.messages.size() == 0 : tLRPC$messages_Messages.messages.size() <= 1) {
                z2 = true;
            }
            processLoadedMedia(tLRPC$messages_Messages, j, i2, i3, i, i4, 0, i5, z, z2, i6);
        }
    }

    public void getMediaCounts(long j, int i) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda34(this, j, i));
    }

    public /* synthetic */ void lambda$getMediaCounts$93(long j, int i) {
        try {
            int[] iArr = new int[8];
            iArr[0] = -1;
            iArr[1] = -1;
            iArr[2] = -1;
            iArr[3] = -1;
            iArr[4] = -1;
            iArr[5] = -1;
            iArr[6] = -1;
            iArr[7] = -1;
            int[] iArr2 = new int[8];
            iArr2[0] = -1;
            iArr2[1] = -1;
            iArr2[2] = -1;
            iArr2[3] = -1;
            iArr2[4] = -1;
            iArr2[5] = -1;
            iArr2[6] = -1;
            iArr2[7] = -1;
            int[] iArr3 = new int[8];
            iArr3[0] = 0;
            iArr3[1] = 0;
            iArr3[2] = 0;
            iArr3[3] = 0;
            iArr3[4] = 0;
            iArr3[5] = 0;
            iArr3[6] = 0;
            iArr3[7] = 0;
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT type, count, old FROM media_counts_v2 WHERE uid = %d", Long.valueOf(j)), new Object[0]);
            while (queryFinalized.next()) {
                int intValue = queryFinalized.intValue(0);
                if (intValue >= 0 && intValue < 8) {
                    int intValue2 = queryFinalized.intValue(1);
                    iArr[intValue] = intValue2;
                    iArr2[intValue] = intValue2;
                    iArr3[intValue] = queryFinalized.intValue(2);
                }
            }
            queryFinalized.dispose();
            if (DialogObject.isEncryptedDialog(j)) {
                for (int i2 = 0; i2 < 8; i2++) {
                    if (iArr[i2] == -1) {
                        SQLiteCursor queryFinalized2 = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT COUNT(mid) FROM media_v4 WHERE uid = %d AND type = %d LIMIT 1", Long.valueOf(j), Integer.valueOf(i2)), new Object[0]);
                        if (queryFinalized2.next()) {
                            iArr[i2] = queryFinalized2.intValue(0);
                        } else {
                            iArr[i2] = 0;
                        }
                        queryFinalized2.dispose();
                        putMediaCountDatabase(j, i2, iArr[i2]);
                    }
                }
                AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda48(this, j, iArr));
                return;
            }
            TLRPC$TL_messages_getSearchCounters tLRPC$TL_messages_getSearchCounters = new TLRPC$TL_messages_getSearchCounters();
            tLRPC$TL_messages_getSearchCounters.peer = getMessagesController().getInputPeer(j);
            int i3 = 0;
            boolean z = false;
            for (int i4 = 8; i3 < i4; i4 = 8) {
                if (tLRPC$TL_messages_getSearchCounters.peer == null) {
                    iArr[i3] = 0;
                } else if (iArr[i3] == -1 || iArr3[i3] == 1) {
                    if (i3 == 0) {
                        tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterPhotoVideo());
                    } else if (i3 == 1) {
                        tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterDocument());
                    } else if (i3 == 2) {
                        tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterRoundVoice());
                    } else if (i3 == 3) {
                        tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterUrl());
                    } else if (i3 == 4) {
                        tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterMusic());
                    } else if (i3 == 6) {
                        tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterPhotos());
                    } else if (i3 == 7) {
                        tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterVideo());
                    } else {
                        tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterGif());
                    }
                    if (iArr[i3] == -1) {
                        z = true;
                    } else if (iArr3[i3] == 1) {
                        iArr[i3] = -1;
                    }
                }
                i3++;
            }
            if (!tLRPC$TL_messages_getSearchCounters.filters.isEmpty()) {
                getConnectionsManager().bindRequestToGuid(getConnectionsManager().sendRequest(tLRPC$TL_messages_getSearchCounters, new MediaDataController$$ExternalSyntheticLambda170(this, iArr, j)), i);
            }
            if (z) {
                return;
            }
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda50(this, j, iArr2));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$getMediaCounts$89(long j, int[] iArr) {
        getNotificationCenter().postNotificationName(NotificationCenter.mediaCountsDidLoad, Long.valueOf(j), iArr);
    }

    public /* synthetic */ void lambda$getMediaCounts$91(int[] iArr, long j, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        int i;
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] < 0) {
                iArr[i2] = 0;
            }
        }
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            int size = tLRPC$Vector.objects.size();
            for (int i3 = 0; i3 < size; i3++) {
                TLRPC$TL_messages_searchCounter tLRPC$TL_messages_searchCounter = (TLRPC$TL_messages_searchCounter) tLRPC$Vector.objects.get(i3);
                TLRPC$MessagesFilter tLRPC$MessagesFilter = tLRPC$TL_messages_searchCounter.filter;
                if (tLRPC$MessagesFilter instanceof TLRPC$TL_inputMessagesFilterPhotoVideo) {
                    i = 0;
                } else if (tLRPC$MessagesFilter instanceof TLRPC$TL_inputMessagesFilterDocument) {
                    i = 1;
                } else if (tLRPC$MessagesFilter instanceof TLRPC$TL_inputMessagesFilterRoundVoice) {
                    i = 2;
                } else if (tLRPC$MessagesFilter instanceof TLRPC$TL_inputMessagesFilterUrl) {
                    i = 3;
                } else if (tLRPC$MessagesFilter instanceof TLRPC$TL_inputMessagesFilterMusic) {
                    i = 4;
                } else if (tLRPC$MessagesFilter instanceof TLRPC$TL_inputMessagesFilterGif) {
                    i = 5;
                } else if (tLRPC$MessagesFilter instanceof TLRPC$TL_inputMessagesFilterPhotos) {
                    i = 6;
                } else if (tLRPC$MessagesFilter instanceof TLRPC$TL_inputMessagesFilterVideo) {
                    i = 7;
                }
                iArr[i] = tLRPC$TL_messages_searchCounter.count;
                putMediaCountDatabase(j, i, iArr[i]);
            }
        }
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda49(this, j, iArr));
    }

    public /* synthetic */ void lambda$getMediaCounts$90(long j, int[] iArr) {
        getNotificationCenter().postNotificationName(NotificationCenter.mediaCountsDidLoad, Long.valueOf(j), iArr);
    }

    public /* synthetic */ void lambda$getMediaCounts$92(long j, int[] iArr) {
        getNotificationCenter().postNotificationName(NotificationCenter.mediaCountsDidLoad, Long.valueOf(j), iArr);
    }

    public void getMediaCount(long j, int i, int i2, boolean z) {
        if (z || DialogObject.isEncryptedDialog(j)) {
            getMediaCountDatabase(j, i, i2);
            return;
        }
        TLRPC$TL_messages_getSearchCounters tLRPC$TL_messages_getSearchCounters = new TLRPC$TL_messages_getSearchCounters();
        if (i == 0) {
            tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterPhotoVideo());
        } else if (i == 1) {
            tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterDocument());
        } else if (i == 2) {
            tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterRoundVoice());
        } else if (i == 3) {
            tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterUrl());
        } else if (i == 4) {
            tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterMusic());
        } else if (i == 5) {
            tLRPC$TL_messages_getSearchCounters.filters.add(new TLRPC$TL_inputMessagesFilterGif());
        }
        TLRPC$InputPeer inputPeer = getMessagesController().getInputPeer(j);
        tLRPC$TL_messages_getSearchCounters.peer = inputPeer;
        if (inputPeer == null) {
            return;
        }
        getConnectionsManager().bindRequestToGuid(getConnectionsManager().sendRequest(tLRPC$TL_messages_getSearchCounters, new MediaDataController$$ExternalSyntheticLambda151(this, j, i, i2)), i2);
    }

    public /* synthetic */ void lambda$getMediaCount$94(long j, int i, int i2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            TLRPC$Vector tLRPC$Vector = (TLRPC$Vector) tLObject;
            if (tLRPC$Vector.objects.isEmpty()) {
                return;
            }
            processLoadedMediaCount(((TLRPC$TL_messages_searchCounter) tLRPC$Vector.objects.get(0)).count, j, i, i2, false, 0);
        }
    }

    public static int getMediaType(TLRPC$Message tLRPC$Message) {
        if (tLRPC$Message == null) {
            return -1;
        }
        TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            return 0;
        }
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
            if (tLRPC$Document == null) {
                return -1;
            }
            boolean z = false;
            boolean z2 = false;
            boolean z3 = false;
            boolean z4 = false;
            boolean z5 = false;
            for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeVideo) {
                    z = tLRPC$DocumentAttribute.round_message;
                    z2 = !z;
                } else if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAnimated) {
                    z3 = true;
                } else if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                    z = tLRPC$DocumentAttribute.voice;
                    z5 = !z;
                } else if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) {
                    z4 = true;
                }
            }
            if (z) {
                return 2;
            }
            if (z2 && !z3 && !z4) {
                return 0;
            }
            if (z4) {
                return -1;
            }
            if (z3) {
                return 5;
            }
            return z5 ? 4 : 1;
        }
        if (!tLRPC$Message.entities.isEmpty()) {
            for (int i2 = 0; i2 < tLRPC$Message.entities.size(); i2++) {
                TLRPC$MessageEntity tLRPC$MessageEntity = tLRPC$Message.entities.get(i2);
                if ((tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUrl) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityEmail)) {
                    return 3;
                }
            }
        }
        return -1;
    }

    public static boolean canAddMessageToMedia(TLRPC$Message tLRPC$Message) {
        int i;
        boolean z = tLRPC$Message instanceof TLRPC$TL_message_secret;
        if (!z || ((!(tLRPC$Message.media instanceof TLRPC$TL_messageMediaPhoto) && !MessageObject.isVideoMessage(tLRPC$Message) && !MessageObject.isGifMessage(tLRPC$Message)) || (i = tLRPC$Message.media.ttl_seconds) == 0 || i > 60)) {
            if (!z && (tLRPC$Message instanceof TLRPC$TL_message)) {
                TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
                if (((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument)) && tLRPC$MessageMedia.ttl_seconds != 0) {
                    return false;
                }
            }
            return getMediaType(tLRPC$Message) != -1;
        }
        return false;
    }

    public void processLoadedMedia(TLRPC$messages_Messages tLRPC$messages_Messages, long j, int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2, int i7) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("process load media did " + j + " count = " + i + " max_id=" + i2 + " min_id=" + i3 + " type = " + i4 + " cache = " + i5 + " classGuid = " + i6);
        }
        if (i5 != 0 && (((tLRPC$messages_Messages.messages.isEmpty() && i3 == 0) || (tLRPC$messages_Messages.messages.size() <= 1 && i3 != 0)) && !DialogObject.isEncryptedDialog(j))) {
            if (i5 == 2) {
                return;
            }
            loadMedia(j, i, i2, i3, i4, 0, i6, i7);
            return;
        }
        if (i5 == 0) {
            ImageLoader.saveMessagesThumbs(tLRPC$messages_Messages.messages);
            getMessagesStorage().putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
            putMediaDatabase(j, i4, tLRPC$messages_Messages.messages, i2, i3, z2);
        }
        Utilities.searchQueue.postRunnable(new MediaDataController$$ExternalSyntheticLambda108(this, tLRPC$messages_Messages, i5, j, i6, i4, z2, i3, i7));
    }

    public /* synthetic */ void lambda$processLoadedMedia$96(TLRPC$messages_Messages tLRPC$messages_Messages, int i, long j, int i2, int i3, boolean z, int i4, int i5) {
        LongSparseArray longSparseArray = new LongSparseArray();
        for (int i6 = 0; i6 < tLRPC$messages_Messages.users.size(); i6++) {
            TLRPC$User tLRPC$User = tLRPC$messages_Messages.users.get(i6);
            longSparseArray.put(tLRPC$User.id, tLRPC$User);
        }
        ArrayList<MessageObject> arrayList = new ArrayList<>();
        for (int i7 = 0; i7 < tLRPC$messages_Messages.messages.size(); i7++) {
            MessageObject messageObject = new MessageObject(this.currentAccount, tLRPC$messages_Messages.messages.get(i7), (LongSparseArray<TLRPC$User>) longSparseArray, true, false);
            messageObject.createStrippedThumb();
            arrayList.add(messageObject);
        }
        getFileLoader().checkMediaExistance(arrayList);
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda109(this, tLRPC$messages_Messages, i, j, arrayList, i2, i3, z, i4, i5));
    }

    public /* synthetic */ void lambda$processLoadedMedia$95(TLRPC$messages_Messages tLRPC$messages_Messages, int i, long j, ArrayList arrayList, int i2, int i3, boolean z, int i4, int i5) {
        int i6 = tLRPC$messages_Messages.count;
        boolean z2 = true;
        getMessagesController().putUsers(tLRPC$messages_Messages.users, i != 0);
        getMessagesController().putChats(tLRPC$messages_Messages.chats, i != 0);
        NotificationCenter notificationCenter = getNotificationCenter();
        int i7 = NotificationCenter.mediaDidLoad;
        Object[] objArr = new Object[8];
        objArr[0] = Long.valueOf(j);
        objArr[1] = Integer.valueOf(i6);
        objArr[2] = arrayList;
        objArr[3] = Integer.valueOf(i2);
        objArr[4] = Integer.valueOf(i3);
        objArr[5] = Boolean.valueOf(z);
        if (i4 == 0) {
            z2 = false;
        }
        objArr[6] = Boolean.valueOf(z2);
        objArr[7] = Integer.valueOf(i5);
        notificationCenter.postNotificationName(i7, objArr);
    }

    private void processLoadedMediaCount(int i, long j, int i2, int i3, boolean z, int i4) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda47(this, j, z, i, i2, i4, i3));
    }

    public /* synthetic */ void lambda$processLoadedMediaCount$97(long j, boolean z, int i, int i2, int i3, int i4) {
        boolean isEncryptedDialog = DialogObject.isEncryptedDialog(j);
        int i5 = 0;
        boolean z2 = z && (i == -1 || (i == 0 && i2 == 2)) && !isEncryptedDialog;
        if (z2 || (i3 == 1 && !isEncryptedDialog)) {
            getMediaCount(j, i2, i4, false);
        }
        if (!z2) {
            if (!z) {
                putMediaCountDatabase(j, i2, i);
            }
            NotificationCenter notificationCenter = getNotificationCenter();
            int i6 = NotificationCenter.mediaCountDidLoad;
            Object[] objArr = new Object[4];
            objArr[0] = Long.valueOf(j);
            if (!z || i != -1) {
                i5 = i;
            }
            objArr[1] = Integer.valueOf(i5);
            objArr[2] = Boolean.valueOf(z);
            objArr[3] = Integer.valueOf(i2);
            notificationCenter.postNotificationName(i6, objArr);
        }
    }

    private void putMediaCountDatabase(long j, int i, int i2) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda37(this, j, i, i2));
    }

    public /* synthetic */ void lambda$putMediaCountDatabase$98(long j, int i, int i2) {
        try {
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO media_counts_v2 VALUES(?, ?, ?, ?)");
            executeFast.requery();
            executeFast.bindLong(1, j);
            executeFast.bindInteger(2, i);
            executeFast.bindInteger(3, i2);
            executeFast.bindInteger(4, 0);
            executeFast.step();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private void getMediaCountDatabase(long j, int i, int i2) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda38(this, j, i, i2));
    }

    public /* synthetic */ void lambda$getMediaCountDatabase$99(long j, int i, int i2) {
        Exception e;
        int i3;
        int i4;
        try {
            SQLiteDatabase database = getMessagesStorage().getDatabase();
            Locale locale = Locale.US;
            SQLiteCursor queryFinalized = database.queryFinalized(String.format(locale, "SELECT count, old FROM media_counts_v2 WHERE uid = %d AND type = %d LIMIT 1", Long.valueOf(j), Integer.valueOf(i)), new Object[0]);
            if (queryFinalized.next()) {
                i4 = queryFinalized.intValue(0);
                i3 = queryFinalized.intValue(1);
            } else {
                i4 = -1;
                i3 = 0;
            }
            queryFinalized.dispose();
            if (i4 == -1 && DialogObject.isEncryptedDialog(j)) {
                SQLiteCursor queryFinalized2 = getMessagesStorage().getDatabase().queryFinalized(String.format(locale, "SELECT COUNT(mid) FROM media_v4 WHERE uid = %d AND type = %d LIMIT 1", Long.valueOf(j), Integer.valueOf(i)), new Object[0]);
                if (queryFinalized2.next()) {
                    i4 = queryFinalized2.intValue(0);
                }
                queryFinalized2.dispose();
                if (i4 != -1) {
                    try {
                        putMediaCountDatabase(j, i, i4);
                        processLoadedMediaCount(i4, j, i, i2, true, i3);
                    } catch (Exception e2) {
                        e = e2;
                        FileLog.e(e);
                        return;
                    }
                }
            }
            processLoadedMediaCount(i4, j, i, i2, true, i3);
        } catch (Exception e3) {
            e = e3;
        }
    }

    /* renamed from: org.telegram.messenger.MediaDataController$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ int val$classGuid;
        final /* synthetic */ int val$count;
        final /* synthetic */ int val$fromCache;
        final /* synthetic */ boolean val$isChannel;
        final /* synthetic */ int val$max_id;
        final /* synthetic */ int val$min_id;
        final /* synthetic */ int val$requestIndex;
        final /* synthetic */ int val$type;
        final /* synthetic */ long val$uid;

        AnonymousClass1(int i, long j, int i2, int i3, int i4, int i5, int i6, boolean z, int i7) {
            MediaDataController.this = r1;
            this.val$count = i;
            this.val$uid = j;
            this.val$min_id = i2;
            this.val$type = i3;
            this.val$max_id = i4;
            this.val$classGuid = i5;
            this.val$fromCache = i6;
            this.val$isChannel = z;
            this.val$requestIndex = i7;
        }

        /* JADX WARN: Removed duplicated region for block: B:62:0x034d A[Catch: all -> 0x040b, Exception -> 0x040e, TryCatch #0 {Exception -> 0x040e, blocks: (B:3:0x0007, B:5:0x0029, B:7:0x002d, B:9:0x0053, B:12:0x005b, B:14:0x0082, B:16:0x0088, B:18:0x00aa, B:20:0x00af, B:22:0x00b3, B:24:0x00e3, B:26:0x00ec, B:28:0x00f1, B:29:0x012a, B:30:0x015b, B:32:0x015f, B:34:0x0191, B:36:0x019b, B:38:0x01a1, B:39:0x01db, B:41:0x0211, B:43:0x023b, B:45:0x0241, B:47:0x0247, B:48:0x0278, B:51:0x02a4, B:53:0x02a9, B:56:0x02e1, B:58:0x02e5, B:59:0x031a, B:60:0x0347, B:62:0x034d, B:64:0x0353, B:66:0x037a, B:69:0x0385, B:70:0x038c, B:71:0x0392, B:73:0x039b, B:76:0x03a7, B:77:0x03b6, B:79:0x03bc, B:80:0x03cb, B:82:0x03d5, B:84:0x03d9, B:85:0x03e5), top: B:100:0x0007, outer: #1 }] */
        /* JADX WARN: Removed duplicated region for block: B:73:0x039b A[Catch: all -> 0x040b, Exception -> 0x040e, TRY_LEAVE, TryCatch #0 {Exception -> 0x040e, blocks: (B:3:0x0007, B:5:0x0029, B:7:0x002d, B:9:0x0053, B:12:0x005b, B:14:0x0082, B:16:0x0088, B:18:0x00aa, B:20:0x00af, B:22:0x00b3, B:24:0x00e3, B:26:0x00ec, B:28:0x00f1, B:29:0x012a, B:30:0x015b, B:32:0x015f, B:34:0x0191, B:36:0x019b, B:38:0x01a1, B:39:0x01db, B:41:0x0211, B:43:0x023b, B:45:0x0241, B:47:0x0247, B:48:0x0278, B:51:0x02a4, B:53:0x02a9, B:56:0x02e1, B:58:0x02e5, B:59:0x031a, B:60:0x0347, B:62:0x034d, B:64:0x0353, B:66:0x037a, B:69:0x0385, B:70:0x038c, B:71:0x0392, B:73:0x039b, B:76:0x03a7, B:77:0x03b6, B:79:0x03bc, B:80:0x03cb, B:82:0x03d5, B:84:0x03d9, B:85:0x03e5), top: B:100:0x0007, outer: #1 }] */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:71:0x0392 -> B:55:0x02df). Please submit an issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:72:0x0397 -> B:55:0x02df). Please submit an issue!!! */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            int i;
            boolean z;
            boolean z2;
            int i2;
            int i3;
            int i4;
            int i5;
            int i6;
            int i7;
            long j;
            MediaDataController mediaDataController;
            ArrayList arrayList;
            ArrayList arrayList2;
            int i8;
            SQLiteDatabase database;
            boolean z3;
            boolean z4;
            boolean z5;
            SQLiteCursor sQLiteCursor;
            boolean z6;
            int i9;
            int i10;
            int intValue;
            TLRPC$TL_messages_messages tLRPC$TL_messages_messages = new TLRPC$TL_messages_messages();
            try {
                try {
                    arrayList = new ArrayList();
                    arrayList2 = new ArrayList();
                    i8 = this.val$count + 1;
                    database = MediaDataController.this.getMessagesStorage().getDatabase();
                    z3 = false;
                } catch (Exception e) {
                    tLRPC$TL_messages_messages.messages.clear();
                    tLRPC$TL_messages_messages.chats.clear();
                    tLRPC$TL_messages_messages.users.clear();
                    FileLog.e(e);
                    AndroidUtilities.runOnUIThread(new MediaDataController$1$$ExternalSyntheticLambda0(this, this, this.val$classGuid));
                    mediaDataController = MediaDataController.this;
                    j = this.val$uid;
                    i7 = this.val$count;
                    i6 = this.val$max_id;
                    i5 = this.val$min_id;
                    i4 = this.val$type;
                    i3 = this.val$fromCache;
                    i2 = this.val$classGuid;
                    z2 = this.val$isChannel;
                    i = this.val$requestIndex;
                    z = false;
                }
                if (!DialogObject.isEncryptedDialog(this.val$uid)) {
                    if (this.val$min_id == 0) {
                        Locale locale = Locale.US;
                        SQLiteCursor queryFinalized = database.queryFinalized(String.format(locale, "SELECT start FROM media_holes_v2 WHERE uid = %d AND type = %d AND start IN (0, 1)", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type)), new Object[0]);
                        if (queryFinalized.next()) {
                            if (queryFinalized.intValue(0) == 1) {
                                z6 = true;
                                queryFinalized.dispose();
                            }
                        } else {
                            queryFinalized.dispose();
                            queryFinalized = database.queryFinalized(String.format(locale, "SELECT min(mid) FROM media_v4 WHERE uid = %d AND type = %d AND mid > 0", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type)), new Object[0]);
                            if (queryFinalized.next() && (intValue = queryFinalized.intValue(0)) != 0) {
                                SQLitePreparedStatement executeFast = database.executeFast("REPLACE INTO media_holes_v2 VALUES(?, ?, ?, ?)");
                                executeFast.requery();
                                executeFast.bindLong(1, this.val$uid);
                                executeFast.bindInteger(2, this.val$type);
                                executeFast.bindInteger(3, 0);
                                executeFast.bindInteger(4, intValue);
                                executeFast.step();
                                executeFast.dispose();
                            }
                        }
                        z6 = false;
                        queryFinalized.dispose();
                    } else {
                        z6 = false;
                    }
                    if (this.val$max_id != 0) {
                        Locale locale2 = Locale.US;
                        SQLiteCursor queryFinalized2 = database.queryFinalized(String.format(locale2, "SELECT start, end FROM media_holes_v2 WHERE uid = %d AND type = %d AND start <= %d ORDER BY end DESC LIMIT 1", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type), Integer.valueOf(this.val$max_id)), new Object[0]);
                        if (queryFinalized2.next()) {
                            queryFinalized2.intValue(0);
                            i10 = queryFinalized2.intValue(1);
                        } else {
                            i10 = 0;
                        }
                        queryFinalized2.dispose();
                        if (i10 > 1) {
                            sQLiteCursor = database.queryFinalized(String.format(locale2, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid > 0 AND mid < %d AND mid >= %d AND type = %d ORDER BY date DESC, mid DESC LIMIT %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$max_id), Integer.valueOf(i10), Integer.valueOf(this.val$type), Integer.valueOf(i8)), new Object[0]);
                            z6 = false;
                        } else {
                            sQLiteCursor = database.queryFinalized(String.format(locale2, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid > 0 AND mid < %d AND type = %d ORDER BY date DESC, mid DESC LIMIT %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$max_id), Integer.valueOf(this.val$type), Integer.valueOf(i8)), new Object[0]);
                        }
                    } else if (this.val$min_id != 0) {
                        Locale locale3 = Locale.US;
                        SQLiteCursor queryFinalized3 = database.queryFinalized(String.format(locale3, "SELECT start, end FROM media_holes_v2 WHERE uid = %d AND type = %d AND end >= %d ORDER BY end ASC LIMIT 1", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type), Integer.valueOf(this.val$min_id)), new Object[0]);
                        if (queryFinalized3.next()) {
                            i9 = queryFinalized3.intValue(0);
                            queryFinalized3.intValue(1);
                        } else {
                            i9 = 0;
                        }
                        queryFinalized3.dispose();
                        if (i9 > 1) {
                            sQLiteCursor = database.queryFinalized(String.format(locale3, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid > 0 AND mid >= %d AND mid <= %d AND type = %d ORDER BY date ASC, mid ASC LIMIT %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$min_id), Integer.valueOf(i9), Integer.valueOf(this.val$type), Integer.valueOf(i8)), new Object[0]);
                        } else {
                            sQLiteCursor = database.queryFinalized(String.format(locale3, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid > 0 AND mid >= %d AND type = %d ORDER BY date ASC, mid ASC LIMIT %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$min_id), Integer.valueOf(this.val$type), Integer.valueOf(i8)), new Object[0]);
                            z6 = true;
                        }
                        z3 = true;
                    } else {
                        Locale locale4 = Locale.US;
                        SQLiteCursor queryFinalized4 = database.queryFinalized(String.format(locale4, "SELECT max(end) FROM media_holes_v2 WHERE uid = %d AND type = %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type)), new Object[0]);
                        int intValue2 = queryFinalized4.next() ? queryFinalized4.intValue(0) : 0;
                        queryFinalized4.dispose();
                        sQLiteCursor = intValue2 > 1 ? database.queryFinalized(String.format(locale4, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid >= %d AND type = %d ORDER BY date DESC, mid DESC LIMIT %d", Long.valueOf(this.val$uid), Integer.valueOf(intValue2), Integer.valueOf(this.val$type), Integer.valueOf(i8)), new Object[0]) : database.queryFinalized(String.format(locale4, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid > 0 AND type = %d ORDER BY date DESC, mid DESC LIMIT %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type), Integer.valueOf(i8)), new Object[0]);
                        z3 = false;
                    }
                    z5 = z6;
                } else {
                    if (this.val$max_id != 0) {
                        sQLiteCursor = database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid, r.random_id FROM media_v4 as m LEFT JOIN randoms_v2 as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid > %d AND type = %d ORDER BY m.mid ASC LIMIT %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$max_id), Integer.valueOf(this.val$type), Integer.valueOf(i8)), new Object[0]);
                    } else if (this.val$min_id != 0) {
                        sQLiteCursor = database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid, r.random_id FROM media_v4 as m LEFT JOIN randoms_v2 as r ON r.mid = m.mid WHERE m.uid = %d AND m.mid < %d AND type = %d ORDER BY m.mid DESC LIMIT %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$min_id), Integer.valueOf(this.val$type), Integer.valueOf(i8)), new Object[0]);
                    } else {
                        z4 = false;
                        sQLiteCursor = database.queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid, r.random_id FROM media_v4 as m LEFT JOIN randoms_v2 as r ON r.mid = m.mid WHERE m.uid = %d AND type = %d ORDER BY m.mid ASC LIMIT %d", Long.valueOf(this.val$uid), Integer.valueOf(this.val$type), Integer.valueOf(i8)), new Object[0]);
                        z5 = true;
                        z3 = false;
                        if (!sQLiteCursor.next()) {
                            int i11 = z4 ? 1 : 0;
                            int i12 = z4 ? 1 : 0;
                            NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(i11);
                            if (byteBufferValue != null) {
                                TLRPC$Message TLdeserialize = TLRPC$Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(z4), z4);
                                TLdeserialize.readAttachPath(byteBufferValue, MediaDataController.this.getUserConfig().clientUserId);
                                byteBufferValue.reuse();
                                TLdeserialize.id = sQLiteCursor.intValue(1);
                                long j2 = this.val$uid;
                                TLdeserialize.dialog_id = j2;
                                if (DialogObject.isEncryptedDialog(j2)) {
                                    TLdeserialize.random_id = sQLiteCursor.longValue(2);
                                }
                                if (z3) {
                                    tLRPC$TL_messages_messages.messages.add(0, TLdeserialize);
                                } else {
                                    tLRPC$TL_messages_messages.messages.add(TLdeserialize);
                                }
                                MessagesStorage.addUsersAndChatsFromMessage(TLdeserialize, arrayList, arrayList2);
                            }
                        } else {
                            sQLiteCursor.dispose();
                            if (!arrayList.isEmpty()) {
                                MediaDataController.this.getMessagesStorage().getUsersInternal(TextUtils.join(",", arrayList), tLRPC$TL_messages_messages.users);
                            }
                            if (!arrayList2.isEmpty()) {
                                MediaDataController.this.getMessagesStorage().getChatsInternal(TextUtils.join(",", arrayList2), tLRPC$TL_messages_messages.chats);
                            }
                            if (tLRPC$TL_messages_messages.messages.size() > this.val$count && this.val$min_id == 0) {
                                ArrayList<TLRPC$Message> arrayList3 = tLRPC$TL_messages_messages.messages;
                                arrayList3.remove(arrayList3.size() - 1);
                            } else if (this.val$min_id == 0) {
                                z = z5;
                                AndroidUtilities.runOnUIThread(new MediaDataController$1$$ExternalSyntheticLambda0(this, this, this.val$classGuid));
                                mediaDataController = MediaDataController.this;
                                j = this.val$uid;
                                i7 = this.val$count;
                                i6 = this.val$max_id;
                                i5 = this.val$min_id;
                                i4 = this.val$type;
                                i3 = this.val$fromCache;
                                i2 = this.val$classGuid;
                                z2 = this.val$isChannel;
                                i = this.val$requestIndex;
                                mediaDataController.processLoadedMedia(tLRPC$TL_messages_messages, j, i7, i6, i5, i4, i3, i2, z2, z, i);
                                return;
                            }
                            z = false;
                            AndroidUtilities.runOnUIThread(new MediaDataController$1$$ExternalSyntheticLambda0(this, this, this.val$classGuid));
                            mediaDataController = MediaDataController.this;
                            j = this.val$uid;
                            i7 = this.val$count;
                            i6 = this.val$max_id;
                            i5 = this.val$min_id;
                            i4 = this.val$type;
                            i3 = this.val$fromCache;
                            i2 = this.val$classGuid;
                            z2 = this.val$isChannel;
                            i = this.val$requestIndex;
                            mediaDataController.processLoadedMedia(tLRPC$TL_messages_messages, j, i7, i6, i5, i4, i3, i2, z2, z, i);
                            return;
                        }
                    }
                    z5 = true;
                    z3 = false;
                }
                z4 = false;
                if (!sQLiteCursor.next()) {
                }
            } catch (Throwable th) {
                AndroidUtilities.runOnUIThread(new MediaDataController$1$$ExternalSyntheticLambda0(this, this, this.val$classGuid));
                MediaDataController.this.processLoadedMedia(tLRPC$TL_messages_messages, this.val$uid, this.val$count, this.val$max_id, this.val$min_id, this.val$type, this.val$fromCache, this.val$classGuid, this.val$isChannel, false, this.val$requestIndex);
                throw th;
            }
        }

        public /* synthetic */ void lambda$run$0(Runnable runnable, int i) {
            MediaDataController.this.getMessagesStorage().completeTaskForGuid(runnable, i);
        }
    }

    private void loadMediaDatabase(long j, int i, int i2, int i3, int i4, int i5, boolean z, int i6, int i7) {
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(i, j, i3, i4, i2, i5, i6, z, i7);
        MessagesStorage messagesStorage = getMessagesStorage();
        messagesStorage.getStorageQueue().postRunnable(anonymousClass1);
        messagesStorage.bindTaskToGuid(anonymousClass1, i5);
    }

    private void putMediaDatabase(long j, int i, ArrayList<TLRPC$Message> arrayList, int i2, int i3, boolean z) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda24(this, i3, arrayList, z, j, i2, i));
    }

    public /* synthetic */ void lambda$putMediaDatabase$100(int i, ArrayList arrayList, boolean z, long j, int i2, int i3) {
        if (i == 0) {
            try {
                if (arrayList.isEmpty() || z) {
                    getMessagesStorage().doneHolesInMedia(j, i2, i3);
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
        SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO media_v4 VALUES(?, ?, ?, ?, ?)");
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            TLRPC$Message tLRPC$Message = (TLRPC$Message) it.next();
            if (canAddMessageToMedia(tLRPC$Message)) {
                executeFast.requery();
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tLRPC$Message.getObjectSize());
                tLRPC$Message.serializeToStream(nativeByteBuffer);
                executeFast.bindInteger(1, tLRPC$Message.id);
                executeFast.bindLong(2, j);
                executeFast.bindInteger(3, tLRPC$Message.date);
                executeFast.bindInteger(4, i3);
                executeFast.bindByteBuffer(5, nativeByteBuffer);
                executeFast.step();
                nativeByteBuffer.reuse();
            }
        }
        executeFast.dispose();
        if (!z || i2 != 0 || i != 0) {
            int i4 = (!z || i != 0) ? ((TLRPC$Message) arrayList.get(arrayList.size() - 1)).id : 1;
            if (i != 0) {
                getMessagesStorage().closeHolesInMedia(j, i4, ((TLRPC$Message) arrayList.get(0)).id, i3);
            } else if (i2 != 0) {
                getMessagesStorage().closeHolesInMedia(j, i4, i2, i3);
            } else {
                getMessagesStorage().closeHolesInMedia(j, i4, Integer.MAX_VALUE, i3);
            }
        }
        getMessagesStorage().getDatabase().commitTransaction();
    }

    public void loadMusic(long j, long j2, long j3) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda41(this, j, j2, j3));
    }

    public /* synthetic */ void lambda$loadMusic$102(long j, long j2, long j3) {
        SQLiteCursor sQLiteCursor;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int i = 0;
        while (i < 2) {
            ArrayList arrayList3 = i == 0 ? arrayList : arrayList2;
            if (i == 0) {
                try {
                    if (!DialogObject.isEncryptedDialog(j)) {
                        sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid < %d AND type = %d ORDER BY date DESC, mid DESC LIMIT 1000", Long.valueOf(j), Long.valueOf(j2), 4), new Object[0]);
                    } else {
                        sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid > %d AND type = %d ORDER BY date DESC, mid DESC LIMIT 1000", Long.valueOf(j), Long.valueOf(j2), 4), new Object[0]);
                    }
                } catch (Exception e) {
                    e = e;
                    FileLog.e(e);
                    AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda44(this, j, arrayList, arrayList2));
                }
            } else if (!DialogObject.isEncryptedDialog(j)) {
                sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid > %d AND type = %d ORDER BY date DESC, mid DESC LIMIT 1000", Long.valueOf(j), Long.valueOf(j3), 4), new Object[0]);
            } else {
                sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid FROM media_v4 WHERE uid = %d AND mid < %d AND type = %d ORDER BY date DESC, mid DESC LIMIT 1000", Long.valueOf(j), Long.valueOf(j3), 4), new Object[0]);
            }
            while (sQLiteCursor.next()) {
                NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(0);
                if (byteBufferValue != null) {
                    TLRPC$Message TLdeserialize = TLRPC$Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                    TLdeserialize.readAttachPath(byteBufferValue, getUserConfig().clientUserId);
                    byteBufferValue.reuse();
                    if (MessageObject.isMusicMessage(TLdeserialize)) {
                        TLdeserialize.id = sQLiteCursor.intValue(1);
                        try {
                            TLdeserialize.dialog_id = j;
                        } catch (Exception e2) {
                            e = e2;
                        }
                        try {
                            arrayList3.add(0, new MessageObject(this.currentAccount, TLdeserialize, false, true));
                        } catch (Exception e3) {
                            e = e3;
                            FileLog.e(e);
                            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda44(this, j, arrayList, arrayList2));
                        }
                    }
                }
            }
            sQLiteCursor.dispose();
            i++;
        }
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda44(this, j, arrayList, arrayList2));
    }

    public /* synthetic */ void lambda$loadMusic$101(long j, ArrayList arrayList, ArrayList arrayList2) {
        getNotificationCenter().postNotificationName(NotificationCenter.musicDidLoad, Long.valueOf(j), arrayList, arrayList2);
    }

    public void buildShortcuts() {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        int maxShortcutCountPerActivity = ShortcutManagerCompat.getMaxShortcutCountPerActivity(ApplicationLoader.applicationContext) - 2;
        if (maxShortcutCountPerActivity <= 0) {
            maxShortcutCountPerActivity = 5;
        }
        ArrayList arrayList = new ArrayList();
        if (SharedConfig.passcodeHash.length() <= 0) {
            for (int i = 0; i < this.hints.size(); i++) {
                arrayList.add(this.hints.get(i));
                if (arrayList.size() == maxShortcutCountPerActivity - 2) {
                    break;
                }
            }
        }
        Utilities.globalQueue.postRunnable(new MediaDataController$$ExternalSyntheticLambda65(this, arrayList));
    }

    /* JADX WARN: Removed duplicated region for block: B:73:0x0262  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0279  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0292 A[Catch: all -> 0x02cd, TryCatch #3 {all -> 0x02cd, blocks: (B:3:0x0004, B:5:0x0009, B:6:0x002a, B:9:0x0045, B:11:0x004b, B:12:0x004f, B:14:0x0055, B:16:0x0077, B:18:0x007d, B:20:0x008d, B:21:0x0090, B:22:0x0096, B:24:0x009c, B:27:0x00a3, B:29:0x00ee, B:30:0x00f4, B:31:0x00f9, B:33:0x0102, B:34:0x0107, B:35:0x0112, B:37:0x0118, B:39:0x0133, B:40:0x0146, B:42:0x015c, B:47:0x0168, B:49:0x0174, B:50:0x0177, B:52:0x017d, B:55:0x0187, B:72:0x025e, B:74:0x0264, B:77:0x027b, B:79:0x0292, B:81:0x0297, B:82:0x029f, B:83:0x02ab, B:85:0x02b8, B:86:0x02be, B:87:0x02c3), top: B:98:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0297 A[Catch: all -> 0x02cd, TryCatch #3 {all -> 0x02cd, blocks: (B:3:0x0004, B:5:0x0009, B:6:0x002a, B:9:0x0045, B:11:0x004b, B:12:0x004f, B:14:0x0055, B:16:0x0077, B:18:0x007d, B:20:0x008d, B:21:0x0090, B:22:0x0096, B:24:0x009c, B:27:0x00a3, B:29:0x00ee, B:30:0x00f4, B:31:0x00f9, B:33:0x0102, B:34:0x0107, B:35:0x0112, B:37:0x0118, B:39:0x0133, B:40:0x0146, B:42:0x015c, B:47:0x0168, B:49:0x0174, B:50:0x0177, B:52:0x017d, B:55:0x0187, B:72:0x025e, B:74:0x0264, B:77:0x027b, B:79:0x0292, B:81:0x0297, B:82:0x029f, B:83:0x02ab, B:85:0x02b8, B:86:0x02be, B:87:0x02c3), top: B:98:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x029f A[Catch: all -> 0x02cd, TryCatch #3 {all -> 0x02cd, blocks: (B:3:0x0004, B:5:0x0009, B:6:0x002a, B:9:0x0045, B:11:0x004b, B:12:0x004f, B:14:0x0055, B:16:0x0077, B:18:0x007d, B:20:0x008d, B:21:0x0090, B:22:0x0096, B:24:0x009c, B:27:0x00a3, B:29:0x00ee, B:30:0x00f4, B:31:0x00f9, B:33:0x0102, B:34:0x0107, B:35:0x0112, B:37:0x0118, B:39:0x0133, B:40:0x0146, B:42:0x015c, B:47:0x0168, B:49:0x0174, B:50:0x0177, B:52:0x017d, B:55:0x0187, B:72:0x025e, B:74:0x0264, B:77:0x027b, B:79:0x0292, B:81:0x0297, B:82:0x029f, B:83:0x02ab, B:85:0x02b8, B:86:0x02be, B:87:0x02c3), top: B:98:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02b8 A[Catch: all -> 0x02cd, TryCatch #3 {all -> 0x02cd, blocks: (B:3:0x0004, B:5:0x0009, B:6:0x002a, B:9:0x0045, B:11:0x004b, B:12:0x004f, B:14:0x0055, B:16:0x0077, B:18:0x007d, B:20:0x008d, B:21:0x0090, B:22:0x0096, B:24:0x009c, B:27:0x00a3, B:29:0x00ee, B:30:0x00f4, B:31:0x00f9, B:33:0x0102, B:34:0x0107, B:35:0x0112, B:37:0x0118, B:39:0x0133, B:40:0x0146, B:42:0x015c, B:47:0x0168, B:49:0x0174, B:50:0x0177, B:52:0x017d, B:55:0x0187, B:72:0x025e, B:74:0x0264, B:77:0x027b, B:79:0x0292, B:81:0x0297, B:82:0x029f, B:83:0x02ab, B:85:0x02b8, B:86:0x02be, B:87:0x02c3), top: B:98:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x02be A[Catch: all -> 0x02cd, TryCatch #3 {all -> 0x02cd, blocks: (B:3:0x0004, B:5:0x0009, B:6:0x002a, B:9:0x0045, B:11:0x004b, B:12:0x004f, B:14:0x0055, B:16:0x0077, B:18:0x007d, B:20:0x008d, B:21:0x0090, B:22:0x0096, B:24:0x009c, B:27:0x00a3, B:29:0x00ee, B:30:0x00f4, B:31:0x00f9, B:33:0x0102, B:34:0x0107, B:35:0x0112, B:37:0x0118, B:39:0x0133, B:40:0x0146, B:42:0x015c, B:47:0x0168, B:49:0x0174, B:50:0x0177, B:52:0x017d, B:55:0x0187, B:72:0x025e, B:74:0x0264, B:77:0x027b, B:79:0x0292, B:81:0x0297, B:82:0x029f, B:83:0x02ab, B:85:0x02b8, B:86:0x02be, B:87:0x02c3), top: B:98:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01b7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$buildShortcuts$103(ArrayList arrayList) {
        TLRPC$Chat tLRPC$Chat;
        TLRPC$User tLRPC$User;
        String str;
        TLRPC$FileLocation tLRPC$FileLocation;
        Bitmap bitmap;
        Throwable th;
        Bitmap bitmap2;
        String str2;
        ArrayList arrayList2 = arrayList;
        try {
            int i = 0;
            if (SharedConfig.directShareHash == null) {
                SharedConfig.directShareHash = UUID.randomUUID().toString();
                ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putString("directShareHash2", SharedConfig.directShareHash).commit();
            }
            List<ShortcutInfoCompat> dynamicShortcuts = ShortcutManagerCompat.getDynamicShortcuts(ApplicationLoader.applicationContext);
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            ArrayList arrayList5 = new ArrayList();
            if (dynamicShortcuts != null && !dynamicShortcuts.isEmpty()) {
                arrayList4.add("compose");
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    arrayList4.add("did3_" + MessageObject.getPeerId(((TLRPC$TL_topPeer) arrayList2.get(i2)).peer));
                }
                for (int i3 = 0; i3 < dynamicShortcuts.size(); i3++) {
                    String id = dynamicShortcuts.get(i3).getId();
                    if (!arrayList4.remove(id)) {
                        arrayList5.add(id);
                    }
                    arrayList3.add(id);
                }
                if (arrayList4.isEmpty() && arrayList5.isEmpty()) {
                    return;
                }
            }
            Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            intent.setAction("new_dialog");
            ArrayList arrayList6 = new ArrayList();
            arrayList6.add(new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, "compose").setShortLabel(LocaleController.getString("NewConversationShortcut", 2131626778)).setLongLabel(LocaleController.getString("NewConversationShortcut", 2131626778)).setIcon(IconCompat.createWithResource(ApplicationLoader.applicationContext, 2131166139)).setIntent(intent).build());
            if (arrayList3.contains("compose")) {
                ShortcutManagerCompat.updateShortcuts(ApplicationLoader.applicationContext, arrayList6);
            } else {
                ShortcutManagerCompat.addDynamicShortcuts(ApplicationLoader.applicationContext, arrayList6);
            }
            arrayList6.clear();
            if (!arrayList5.isEmpty()) {
                ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList5);
            }
            boolean z = true;
            HashSet hashSet = new HashSet(1);
            hashSet.add(SHORTCUT_CATEGORY);
            while (i < arrayList.size()) {
                Intent intent2 = new Intent(ApplicationLoader.applicationContext, OpenChatReceiver.class);
                long peerId = MessageObject.getPeerId(((TLRPC$TL_topPeer) arrayList2.get(i)).peer);
                if (DialogObject.isUserDialog(peerId)) {
                    intent2.putExtra("userId", peerId);
                    tLRPC$User = getMessagesController().getUser(Long.valueOf(peerId));
                    tLRPC$Chat = null;
                } else {
                    long j = -peerId;
                    TLRPC$Chat chat = getMessagesController().getChat(Long.valueOf(j));
                    intent2.putExtra("chatId", j);
                    tLRPC$Chat = chat;
                    tLRPC$User = null;
                }
                if ((tLRPC$User != null && !UserObject.isDeleted(tLRPC$User)) || tLRPC$Chat != null) {
                    if (tLRPC$User != null) {
                        str = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = tLRPC$User.photo;
                        if (tLRPC$UserProfilePhoto != null) {
                            tLRPC$FileLocation = tLRPC$UserProfilePhoto.photo_small;
                            intent2.putExtra("currentAccount", this.currentAccount);
                            intent2.setAction("com.tmessages.openchat" + peerId);
                            intent2.putExtra("dialogId", peerId);
                            intent2.putExtra("hash", SharedConfig.directShareHash);
                            intent2.addFlags(67108864);
                            if (tLRPC$FileLocation == null) {
                                try {
                                    Bitmap decodeFile = BitmapFactory.decodeFile(getFileLoader().getPathToAttach(tLRPC$FileLocation, z).toString());
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
                                        } catch (Throwable th2) {
                                            th = th2;
                                            bitmap = decodeFile;
                                            FileLog.e(th);
                                            str2 = "did3_" + peerId;
                                            if (TextUtils.isEmpty(str)) {
                                            }
                                            ShortcutInfoCompat.Builder intent3 = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str2).setShortLabel(str).setLongLabel(str).setIntent(intent2);
                                            if (SharedConfig.directShare) {
                                            }
                                            if (bitmap != null) {
                                            }
                                            arrayList6.add(intent3.build());
                                            if (arrayList3.contains(str2)) {
                                            }
                                            arrayList6.clear();
                                            i++;
                                            arrayList2 = arrayList;
                                            z = true;
                                        }
                                    } else {
                                        bitmap = decodeFile;
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    bitmap = null;
                                }
                            } else {
                                bitmap = null;
                            }
                            str2 = "did3_" + peerId;
                            if (TextUtils.isEmpty(str)) {
                                str = " ";
                            }
                            ShortcutInfoCompat.Builder intent32 = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str2).setShortLabel(str).setLongLabel(str).setIntent(intent2);
                            if (SharedConfig.directShare) {
                                intent32.setCategories(hashSet);
                            }
                            if (bitmap != null) {
                                intent32.setIcon(IconCompat.createWithBitmap(bitmap));
                            } else {
                                intent32.setIcon(IconCompat.createWithResource(ApplicationLoader.applicationContext, 2131166140));
                            }
                            arrayList6.add(intent32.build());
                            if (arrayList3.contains(str2)) {
                                ShortcutManagerCompat.updateShortcuts(ApplicationLoader.applicationContext, arrayList6);
                            } else {
                                ShortcutManagerCompat.addDynamicShortcuts(ApplicationLoader.applicationContext, arrayList6);
                            }
                            arrayList6.clear();
                        }
                        tLRPC$FileLocation = null;
                        intent2.putExtra("currentAccount", this.currentAccount);
                        intent2.setAction("com.tmessages.openchat" + peerId);
                        intent2.putExtra("dialogId", peerId);
                        intent2.putExtra("hash", SharedConfig.directShareHash);
                        intent2.addFlags(67108864);
                        if (tLRPC$FileLocation == null) {
                        }
                        str2 = "did3_" + peerId;
                        if (TextUtils.isEmpty(str)) {
                        }
                        ShortcutInfoCompat.Builder intent322 = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str2).setShortLabel(str).setLongLabel(str).setIntent(intent2);
                        if (SharedConfig.directShare) {
                        }
                        if (bitmap != null) {
                        }
                        arrayList6.add(intent322.build());
                        if (arrayList3.contains(str2)) {
                        }
                        arrayList6.clear();
                    } else {
                        String str3 = tLRPC$Chat.title;
                        TLRPC$ChatPhoto tLRPC$ChatPhoto = tLRPC$Chat.photo;
                        if (tLRPC$ChatPhoto != null) {
                            TLRPC$FileLocation tLRPC$FileLocation2 = tLRPC$ChatPhoto.photo_small;
                            str = str3;
                            tLRPC$FileLocation = tLRPC$FileLocation2;
                            intent2.putExtra("currentAccount", this.currentAccount);
                            intent2.setAction("com.tmessages.openchat" + peerId);
                            intent2.putExtra("dialogId", peerId);
                            intent2.putExtra("hash", SharedConfig.directShareHash);
                            intent2.addFlags(67108864);
                            if (tLRPC$FileLocation == null) {
                            }
                            str2 = "did3_" + peerId;
                            if (TextUtils.isEmpty(str)) {
                            }
                            ShortcutInfoCompat.Builder intent3222 = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str2).setShortLabel(str).setLongLabel(str).setIntent(intent2);
                            if (SharedConfig.directShare) {
                            }
                            if (bitmap != null) {
                            }
                            arrayList6.add(intent3222.build());
                            if (arrayList3.contains(str2)) {
                            }
                            arrayList6.clear();
                        } else {
                            str = str3;
                            tLRPC$FileLocation = null;
                            intent2.putExtra("currentAccount", this.currentAccount);
                            intent2.setAction("com.tmessages.openchat" + peerId);
                            intent2.putExtra("dialogId", peerId);
                            intent2.putExtra("hash", SharedConfig.directShareHash);
                            intent2.addFlags(67108864);
                            if (tLRPC$FileLocation == null) {
                            }
                            str2 = "did3_" + peerId;
                            if (TextUtils.isEmpty(str)) {
                            }
                            ShortcutInfoCompat.Builder intent32222 = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, str2).setShortLabel(str).setLongLabel(str).setIntent(intent2);
                            if (SharedConfig.directShare) {
                            }
                            if (bitmap != null) {
                            }
                            arrayList6.add(intent32222.build());
                            if (arrayList3.contains(str2)) {
                            }
                            arrayList6.clear();
                        }
                    }
                }
                i++;
                arrayList2 = arrayList;
                z = true;
            }
        } catch (Throwable unused2) {
        }
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
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda4(this));
            this.loaded = true;
            return;
        }
        this.loading = true;
        TLRPC$TL_contacts_getTopPeers tLRPC$TL_contacts_getTopPeers = new TLRPC$TL_contacts_getTopPeers();
        tLRPC$TL_contacts_getTopPeers.hash = 0L;
        tLRPC$TL_contacts_getTopPeers.bots_pm = false;
        tLRPC$TL_contacts_getTopPeers.correspondents = true;
        tLRPC$TL_contacts_getTopPeers.groups = false;
        tLRPC$TL_contacts_getTopPeers.channels = false;
        tLRPC$TL_contacts_getTopPeers.bots_inline = true;
        tLRPC$TL_contacts_getTopPeers.offset = 0;
        tLRPC$TL_contacts_getTopPeers.limit = 20;
        getConnectionsManager().sendRequest(tLRPC$TL_contacts_getTopPeers, new MediaDataController$$ExternalSyntheticLambda137(this));
    }

    public /* synthetic */ void lambda$loadHints$105() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList<TLRPC$User> arrayList3 = new ArrayList<>();
        ArrayList<TLRPC$Chat> arrayList4 = new ArrayList<>();
        long clientUserId = getUserConfig().getClientUserId();
        try {
            ArrayList arrayList5 = new ArrayList();
            ArrayList arrayList6 = new ArrayList();
            int i = 0;
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT did, type, rating FROM chat_hints WHERE 1 ORDER BY rating DESC", new Object[0]);
            while (queryFinalized.next()) {
                long longValue = queryFinalized.longValue(i);
                if (longValue != clientUserId) {
                    int intValue = queryFinalized.intValue(1);
                    TLRPC$TL_topPeer tLRPC$TL_topPeer = new TLRPC$TL_topPeer();
                    tLRPC$TL_topPeer.rating = queryFinalized.doubleValue(2);
                    if (longValue > 0) {
                        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                        tLRPC$TL_topPeer.peer = tLRPC$TL_peerUser;
                        tLRPC$TL_peerUser.user_id = longValue;
                        arrayList5.add(Long.valueOf(longValue));
                    } else {
                        TLRPC$TL_peerChat tLRPC$TL_peerChat = new TLRPC$TL_peerChat();
                        tLRPC$TL_topPeer.peer = tLRPC$TL_peerChat;
                        long j = -longValue;
                        tLRPC$TL_peerChat.chat_id = j;
                        arrayList6.add(Long.valueOf(j));
                    }
                    if (intValue == 0) {
                        arrayList.add(tLRPC$TL_topPeer);
                    } else if (intValue == 1) {
                        arrayList2.add(tLRPC$TL_topPeer);
                    }
                    i = 0;
                }
            }
            queryFinalized.dispose();
            if (!arrayList5.isEmpty()) {
                getMessagesStorage().getUsersInternal(TextUtils.join(",", arrayList5), arrayList3);
            }
            if (!arrayList6.isEmpty()) {
                getMessagesStorage().getChatsInternal(TextUtils.join(",", arrayList6), arrayList4);
            }
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda75(this, arrayList3, arrayList4, arrayList, arrayList2));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$loadHints$104(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4) {
        getMessagesController().putUsers(arrayList, true);
        getMessagesController().putChats(arrayList2, true);
        this.loading = false;
        this.loaded = true;
        this.hints = arrayList3;
        this.inlineBots = arrayList4;
        buildShortcuts();
        getNotificationCenter().postNotificationName(NotificationCenter.reloadHints, new Object[0]);
        getNotificationCenter().postNotificationName(NotificationCenter.reloadInlineHints, new Object[0]);
        if (Math.abs(getUserConfig().lastHintsSyncTime - ((int) (System.currentTimeMillis() / 1000))) >= 86400) {
            loadHints(false);
        }
    }

    public /* synthetic */ void lambda$loadHints$110(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject instanceof TLRPC$TL_contacts_topPeers) {
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda79(this, tLObject));
        } else if (!(tLObject instanceof TLRPC$TL_contacts_topPeersDisabled)) {
        } else {
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda14(this));
        }
    }

    public /* synthetic */ void lambda$loadHints$108(TLObject tLObject) {
        TLRPC$TL_contacts_topPeers tLRPC$TL_contacts_topPeers = (TLRPC$TL_contacts_topPeers) tLObject;
        getMessagesController().putUsers(tLRPC$TL_contacts_topPeers.users, false);
        getMessagesController().putChats(tLRPC$TL_contacts_topPeers.chats, false);
        for (int i = 0; i < tLRPC$TL_contacts_topPeers.categories.size(); i++) {
            TLRPC$TL_topPeerCategoryPeers tLRPC$TL_topPeerCategoryPeers = tLRPC$TL_contacts_topPeers.categories.get(i);
            if (tLRPC$TL_topPeerCategoryPeers.category instanceof TLRPC$TL_topPeerCategoryBotsInline) {
                this.inlineBots = tLRPC$TL_topPeerCategoryPeers.peers;
                getUserConfig().botRatingLoadTime = (int) (System.currentTimeMillis() / 1000);
            } else {
                this.hints = tLRPC$TL_topPeerCategoryPeers.peers;
                long clientUserId = getUserConfig().getClientUserId();
                int i2 = 0;
                while (true) {
                    if (i2 >= this.hints.size()) {
                        break;
                    } else if (this.hints.get(i2).peer.user_id == clientUserId) {
                        this.hints.remove(i2);
                        break;
                    } else {
                        i2++;
                    }
                }
                getUserConfig().ratingLoadTime = (int) (System.currentTimeMillis() / 1000);
            }
        }
        getUserConfig().saveConfig(false);
        buildShortcuts();
        getNotificationCenter().postNotificationName(NotificationCenter.reloadHints, new Object[0]);
        getNotificationCenter().postNotificationName(NotificationCenter.reloadInlineHints, new Object[0]);
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda96(this, tLRPC$TL_contacts_topPeers));
    }

    public /* synthetic */ void lambda$loadHints$107(TLRPC$TL_contacts_topPeers tLRPC$TL_contacts_topPeers) {
        try {
            getMessagesStorage().getDatabase().executeFast("DELETE FROM chat_hints WHERE 1").stepThis().dispose();
            getMessagesStorage().getDatabase().beginTransaction();
            getMessagesStorage().putUsersAndChats(tLRPC$TL_contacts_topPeers.users, tLRPC$TL_contacts_topPeers.chats, false, false);
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO chat_hints VALUES(?, ?, ?, ?)");
            for (int i = 0; i < tLRPC$TL_contacts_topPeers.categories.size(); i++) {
                TLRPC$TL_topPeerCategoryPeers tLRPC$TL_topPeerCategoryPeers = tLRPC$TL_contacts_topPeers.categories.get(i);
                int i2 = tLRPC$TL_topPeerCategoryPeers.category instanceof TLRPC$TL_topPeerCategoryBotsInline ? 1 : 0;
                for (int i3 = 0; i3 < tLRPC$TL_topPeerCategoryPeers.peers.size(); i3++) {
                    TLRPC$TL_topPeer tLRPC$TL_topPeer = tLRPC$TL_topPeerCategoryPeers.peers.get(i3);
                    executeFast.requery();
                    executeFast.bindLong(1, MessageObject.getPeerId(tLRPC$TL_topPeer.peer));
                    executeFast.bindInteger(2, i2);
                    executeFast.bindDouble(3, tLRPC$TL_topPeer.rating);
                    executeFast.bindInteger(4, 0);
                    executeFast.step();
                }
            }
            executeFast.dispose();
            getMessagesStorage().getDatabase().commitTransaction();
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda17(this));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$loadHints$106() {
        getUserConfig().suggestContacts = true;
        getUserConfig().lastHintsSyncTime = (int) (System.currentTimeMillis() / 1000);
        getUserConfig().saveConfig(false);
    }

    public /* synthetic */ void lambda$loadHints$109() {
        getUserConfig().suggestContacts = false;
        getUserConfig().lastHintsSyncTime = (int) (System.currentTimeMillis() / 1000);
        getUserConfig().saveConfig(false);
        clearTopPeers();
    }

    public void clearTopPeers() {
        this.hints.clear();
        this.inlineBots.clear();
        getNotificationCenter().postNotificationName(NotificationCenter.reloadHints, new Object[0]);
        getNotificationCenter().postNotificationName(NotificationCenter.reloadInlineHints, new Object[0]);
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda11(this));
        buildShortcuts();
    }

    public /* synthetic */ void lambda$clearTopPeers$111() {
        try {
            getMessagesStorage().getDatabase().executeFast("DELETE FROM chat_hints WHERE 1").stepThis().dispose();
        } catch (Exception unused) {
        }
    }

    public void increaseInlineRaiting(long j) {
        if (!getUserConfig().suggestContacts) {
            return;
        }
        int max = getUserConfig().botRatingLoadTime != 0 ? Math.max(1, ((int) (System.currentTimeMillis() / 1000)) - getUserConfig().botRatingLoadTime) : 60;
        TLRPC$TL_topPeer tLRPC$TL_topPeer = null;
        int i = 0;
        while (true) {
            if (i >= this.inlineBots.size()) {
                break;
            }
            TLRPC$TL_topPeer tLRPC$TL_topPeer2 = this.inlineBots.get(i);
            if (tLRPC$TL_topPeer2.peer.user_id == j) {
                tLRPC$TL_topPeer = tLRPC$TL_topPeer2;
                break;
            }
            i++;
        }
        if (tLRPC$TL_topPeer == null) {
            tLRPC$TL_topPeer = new TLRPC$TL_topPeer();
            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
            tLRPC$TL_topPeer.peer = tLRPC$TL_peerUser;
            tLRPC$TL_peerUser.user_id = j;
            this.inlineBots.add(tLRPC$TL_topPeer);
        }
        tLRPC$TL_topPeer.rating += Math.exp(max / getMessagesController().ratingDecay);
        Collections.sort(this.inlineBots, MediaDataController$$ExternalSyntheticLambda128.INSTANCE);
        if (this.inlineBots.size() > 20) {
            ArrayList<TLRPC$TL_topPeer> arrayList = this.inlineBots;
            arrayList.remove(arrayList.size() - 1);
        }
        savePeer(j, 1, tLRPC$TL_topPeer.rating);
        getNotificationCenter().postNotificationName(NotificationCenter.reloadInlineHints, new Object[0]);
    }

    public static /* synthetic */ int lambda$increaseInlineRaiting$112(TLRPC$TL_topPeer tLRPC$TL_topPeer, TLRPC$TL_topPeer tLRPC$TL_topPeer2) {
        double d = tLRPC$TL_topPeer.rating;
        double d2 = tLRPC$TL_topPeer2.rating;
        if (d > d2) {
            return -1;
        }
        return d < d2 ? 1 : 0;
    }

    public void removeInline(long j) {
        for (int i = 0; i < this.inlineBots.size(); i++) {
            if (this.inlineBots.get(i).peer.user_id == j) {
                this.inlineBots.remove(i);
                TLRPC$TL_contacts_resetTopPeerRating tLRPC$TL_contacts_resetTopPeerRating = new TLRPC$TL_contacts_resetTopPeerRating();
                tLRPC$TL_contacts_resetTopPeerRating.category = new TLRPC$TL_topPeerCategoryBotsInline();
                tLRPC$TL_contacts_resetTopPeerRating.peer = getMessagesController().getInputPeer(j);
                getConnectionsManager().sendRequest(tLRPC$TL_contacts_resetTopPeerRating, MediaDataController$$ExternalSyntheticLambda175.INSTANCE);
                deletePeer(j, 1);
                getNotificationCenter().postNotificationName(NotificationCenter.reloadInlineHints, new Object[0]);
                return;
            }
        }
    }

    public void removePeer(long j) {
        for (int i = 0; i < this.hints.size(); i++) {
            if (this.hints.get(i).peer.user_id == j) {
                this.hints.remove(i);
                getNotificationCenter().postNotificationName(NotificationCenter.reloadHints, new Object[0]);
                TLRPC$TL_contacts_resetTopPeerRating tLRPC$TL_contacts_resetTopPeerRating = new TLRPC$TL_contacts_resetTopPeerRating();
                tLRPC$TL_contacts_resetTopPeerRating.category = new TLRPC$TL_topPeerCategoryCorrespondents();
                tLRPC$TL_contacts_resetTopPeerRating.peer = getMessagesController().getInputPeer(j);
                deletePeer(j, 0);
                getConnectionsManager().sendRequest(tLRPC$TL_contacts_resetTopPeerRating, MediaDataController$$ExternalSyntheticLambda172.INSTANCE);
                return;
            }
        }
    }

    public void increasePeerRaiting(long j) {
        TLRPC$User user;
        if (getUserConfig().suggestContacts && DialogObject.isUserDialog(j) && (user = getMessagesController().getUser(Long.valueOf(j))) != null && !user.bot && !user.self) {
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda30(this, j));
        }
    }

    public /* synthetic */ void lambda$increasePeerRaiting$117(long j) {
        int i;
        double d = 0.0d;
        try {
            int i2 = 0;
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT MAX(mid), MAX(date) FROM messages_v2 WHERE uid = %d AND out = 1", Long.valueOf(j)), new Object[0]);
            if (queryFinalized.next()) {
                i2 = queryFinalized.intValue(0);
                i = queryFinalized.intValue(1);
            } else {
                i = 0;
            }
            queryFinalized.dispose();
            if (i2 > 0 && getUserConfig().ratingLoadTime != 0) {
                d = i - getUserConfig().ratingLoadTime;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda33(this, j, d));
    }

    public /* synthetic */ void lambda$increasePeerRaiting$116(long j, double d) {
        TLRPC$TL_topPeer tLRPC$TL_topPeer;
        int i = 0;
        while (true) {
            if (i >= this.hints.size()) {
                tLRPC$TL_topPeer = null;
                break;
            }
            tLRPC$TL_topPeer = this.hints.get(i);
            if (tLRPC$TL_topPeer.peer.user_id == j) {
                break;
            }
            i++;
        }
        if (tLRPC$TL_topPeer == null) {
            tLRPC$TL_topPeer = new TLRPC$TL_topPeer();
            TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
            tLRPC$TL_topPeer.peer = tLRPC$TL_peerUser;
            tLRPC$TL_peerUser.user_id = j;
            this.hints.add(tLRPC$TL_topPeer);
        }
        double d2 = tLRPC$TL_topPeer.rating;
        double d3 = getMessagesController().ratingDecay;
        Double.isNaN(d3);
        tLRPC$TL_topPeer.rating = d2 + Math.exp(d / d3);
        Collections.sort(this.hints, MediaDataController$$ExternalSyntheticLambda127.INSTANCE);
        savePeer(j, 0, tLRPC$TL_topPeer.rating);
        getNotificationCenter().postNotificationName(NotificationCenter.reloadHints, new Object[0]);
    }

    public static /* synthetic */ int lambda$increasePeerRaiting$115(TLRPC$TL_topPeer tLRPC$TL_topPeer, TLRPC$TL_topPeer tLRPC$TL_topPeer2) {
        double d = tLRPC$TL_topPeer.rating;
        double d2 = tLRPC$TL_topPeer2.rating;
        if (d > d2) {
            return -1;
        }
        return d < d2 ? 1 : 0;
    }

    private void savePeer(long j, int i, double d) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda36(this, j, i, d));
    }

    public /* synthetic */ void lambda$savePeer$118(long j, int i, double d) {
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

    private void deletePeer(long j, int i) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda35(this, j, i));
    }

    public /* synthetic */ void lambda$deletePeer$119(long j, int i) {
        try {
            getMessagesStorage().getDatabase().executeFast(String.format(Locale.US, "DELETE FROM chat_hints WHERE did = %d AND type = %d", Long.valueOf(j), Integer.valueOf(i))).stepThis().dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private Intent createIntrnalShortcutIntent(long j) {
        Intent intent = new Intent(ApplicationLoader.applicationContext, OpenChatReceiver.class);
        if (DialogObject.isEncryptedDialog(j)) {
            int encryptedChatId = DialogObject.getEncryptedChatId(j);
            intent.putExtra("encId", encryptedChatId);
            if (getMessagesController().getEncryptedChat(Integer.valueOf(encryptedChatId)) == null) {
                return null;
            }
        } else if (DialogObject.isUserDialog(j)) {
            intent.putExtra("userId", j);
        } else if (!DialogObject.isChatDialog(j)) {
            return null;
        } else {
            intent.putExtra("chatId", -j);
        }
        intent.putExtra("currentAccount", this.currentAccount);
        intent.setAction("com.tmessages.openchat" + j);
        intent.addFlags(67108864);
        return intent;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(20:106|3|(2:5|(1:7)(1:8))(2:9|(1:11)(2:13|(2:15|(5:19|(3:21|(1:23)(2:25|(1:27)(4:28|(2:30|35)|34|35))|24)(4:31|(2:33|35)|34|35)|(2:(2:104|40)(1:43)|(8:46|103|47|(3:49|(1:51)(1:52)|53)(3:54|(1:56)|57)|58|101|59|60))(1:38)|63|(4:65|(1:67)(1:(2:69|(1:71)(1:72))(2:73|(1:78)(1:77)))|79|107)(4:80|(1:82)(2:83|(2:85|(1:87)(1:88))(2:89|(1:94)(1:93)))|95|108))(1:18))(1:96)))|12|(0)|19|(0)(0)|(0)|(0)(0)|(0)|46|103|47|(0)(0)|58|101|59|60|63|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0171, code lost:
        r0 = th;
     */
    /* JADX WARN: Removed duplicated region for block: B:104:0x00a8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0062 A[Catch: Exception -> 0x0253, TryCatch #3 {Exception -> 0x0253, blocks: (B:3:0x0002, B:5:0x000d, B:8:0x0020, B:9:0x002f, B:11:0x0035, B:13:0x0044, B:15:0x004a, B:21:0x0062, B:23:0x0068, B:25:0x0074, B:27:0x007a, B:28:0x0084, B:30:0x0090, B:31:0x0093, B:33:0x0099, B:62:0x0172, B:63:0x0175, B:65:0x0187, B:67:0x01a9, B:69:0x01b3, B:71:0x01b7, B:72:0x01c1, B:73:0x01cb, B:75:0x01d1, B:77:0x01d5, B:78:0x01df, B:79:0x01e8, B:80:0x01f2, B:82:0x01f9, B:85:0x0203, B:87:0x0207, B:88:0x0211, B:89:0x021b, B:91:0x0221, B:93:0x0225, B:94:0x022f, B:95:0x0238), top: B:106:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0093 A[Catch: Exception -> 0x0253, TryCatch #3 {Exception -> 0x0253, blocks: (B:3:0x0002, B:5:0x000d, B:8:0x0020, B:9:0x002f, B:11:0x0035, B:13:0x0044, B:15:0x004a, B:21:0x0062, B:23:0x0068, B:25:0x0074, B:27:0x007a, B:28:0x0084, B:30:0x0090, B:31:0x0093, B:33:0x0099, B:62:0x0172, B:63:0x0175, B:65:0x0187, B:67:0x01a9, B:69:0x01b3, B:71:0x01b7, B:72:0x01c1, B:73:0x01cb, B:75:0x01d1, B:77:0x01d5, B:78:0x01df, B:79:0x01e8, B:80:0x01f2, B:82:0x01f9, B:85:0x0203, B:87:0x0207, B:88:0x0211, B:89:0x021b, B:91:0x0221, B:93:0x0225, B:94:0x022f, B:95:0x0238), top: B:106:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00d8 A[Catch: all -> 0x0171, TryCatch #1 {all -> 0x0171, blocks: (B:47:0x00c4, B:49:0x00d8, B:51:0x00e3, B:52:0x00e9, B:53:0x00ec, B:54:0x00f3, B:56:0x00fe, B:57:0x010c, B:58:0x0142, B:59:0x016c), top: B:103:0x00c4 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00f3 A[Catch: all -> 0x0171, TryCatch #1 {all -> 0x0171, blocks: (B:47:0x00c4, B:49:0x00d8, B:51:0x00e3, B:52:0x00e9, B:53:0x00ec, B:54:0x00f3, B:56:0x00fe, B:57:0x010c, B:58:0x0142, B:59:0x016c), top: B:103:0x00c4 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0187 A[Catch: Exception -> 0x0253, TryCatch #3 {Exception -> 0x0253, blocks: (B:3:0x0002, B:5:0x000d, B:8:0x0020, B:9:0x002f, B:11:0x0035, B:13:0x0044, B:15:0x004a, B:21:0x0062, B:23:0x0068, B:25:0x0074, B:27:0x007a, B:28:0x0084, B:30:0x0090, B:31:0x0093, B:33:0x0099, B:62:0x0172, B:63:0x0175, B:65:0x0187, B:67:0x01a9, B:69:0x01b3, B:71:0x01b7, B:72:0x01c1, B:73:0x01cb, B:75:0x01d1, B:77:0x01d5, B:78:0x01df, B:79:0x01e8, B:80:0x01f2, B:82:0x01f9, B:85:0x0203, B:87:0x0207, B:88:0x0211, B:89:0x021b, B:91:0x0221, B:93:0x0225, B:94:0x022f, B:95:0x0238), top: B:106:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01f2 A[Catch: Exception -> 0x0253, TryCatch #3 {Exception -> 0x0253, blocks: (B:3:0x0002, B:5:0x000d, B:8:0x0020, B:9:0x002f, B:11:0x0035, B:13:0x0044, B:15:0x004a, B:21:0x0062, B:23:0x0068, B:25:0x0074, B:27:0x007a, B:28:0x0084, B:30:0x0090, B:31:0x0093, B:33:0x0099, B:62:0x0172, B:63:0x0175, B:65:0x0187, B:67:0x01a9, B:69:0x01b3, B:71:0x01b7, B:72:0x01c1, B:73:0x01cb, B:75:0x01d1, B:77:0x01d5, B:78:0x01df, B:79:0x01e8, B:80:0x01f2, B:82:0x01f9, B:85:0x0203, B:87:0x0207, B:88:0x0211, B:89:0x021b, B:91:0x0221, B:93:0x0225, B:94:0x022f, B:95:0x0238), top: B:106:0x0002 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void installShortcut(long j) {
        TLRPC$Chat tLRPC$Chat;
        TLRPC$User tLRPC$User;
        boolean z;
        TLRPC$FileLocation tLRPC$FileLocation;
        String str;
        Bitmap bitmap;
        TLRPC$User tLRPC$User2;
        try {
            Intent createIntrnalShortcutIntent = createIntrnalShortcutIntent(j);
            if (DialogObject.isEncryptedDialog(j)) {
                TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j)));
                if (encryptedChat == null) {
                    return;
                }
                tLRPC$User2 = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
            } else if (DialogObject.isUserDialog(j)) {
                tLRPC$User2 = getMessagesController().getUser(Long.valueOf(j));
            } else if (!DialogObject.isChatDialog(j)) {
                return;
            } else {
                tLRPC$Chat = getMessagesController().getChat(Long.valueOf(-j));
                tLRPC$User = null;
                if (tLRPC$User != null && tLRPC$Chat == null) {
                    return;
                }
                if (tLRPC$User == null) {
                    if (UserObject.isReplyUser(tLRPC$User)) {
                        str = LocaleController.getString("RepliesTitle", 2131627920);
                    } else if (UserObject.isUserSelf(tLRPC$User)) {
                        str = LocaleController.getString("SavedMessages", 2131628077);
                    } else {
                        str = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                        TLRPC$UserProfilePhoto tLRPC$UserProfilePhoto = tLRPC$User.photo;
                        if (tLRPC$UserProfilePhoto != null) {
                            tLRPC$FileLocation = tLRPC$UserProfilePhoto.photo_small;
                            z = false;
                        }
                        tLRPC$FileLocation = null;
                        z = false;
                    }
                    tLRPC$FileLocation = null;
                    z = true;
                } else {
                    str = tLRPC$Chat.title;
                    TLRPC$ChatPhoto tLRPC$ChatPhoto = tLRPC$Chat.photo;
                    if (tLRPC$ChatPhoto != null) {
                        tLRPC$FileLocation = tLRPC$ChatPhoto.photo_small;
                        z = false;
                    }
                    tLRPC$FileLocation = null;
                    z = false;
                }
                if (!z || tLRPC$FileLocation != null) {
                    if (z) {
                        try {
                            bitmap = BitmapFactory.decodeFile(getFileLoader().getPathToAttach(tLRPC$FileLocation, true).toString());
                        } catch (Throwable th) {
                            th = th;
                            bitmap = null;
                            FileLog.e(th);
                            if (Build.VERSION.SDK_INT < 26) {
                            }
                        }
                    } else {
                        bitmap = null;
                    }
                    if (!z || bitmap != null) {
                        int dp = AndroidUtilities.dp(58.0f);
                        Bitmap createBitmap = Bitmap.createBitmap(dp, dp, Bitmap.Config.ARGB_8888);
                        createBitmap.eraseColor(0);
                        Canvas canvas = new Canvas(createBitmap);
                        if (!z) {
                            AvatarDrawable avatarDrawable = new AvatarDrawable(tLRPC$User);
                            if (UserObject.isReplyUser(tLRPC$User)) {
                                avatarDrawable.setAvatarType(12);
                            } else {
                                avatarDrawable.setAvatarType(1);
                            }
                            avatarDrawable.setBounds(0, 0, dp, dp);
                            avatarDrawable.draw(canvas);
                        } else {
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
                            bitmapRect.set(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
                            canvas.drawRoundRect(bitmapRect, bitmap.getWidth(), bitmap.getHeight(), roundPaint);
                            canvas.restore();
                        }
                        Drawable drawable = ApplicationLoader.applicationContext.getResources().getDrawable(2131165280);
                        int dp2 = AndroidUtilities.dp(15.0f);
                        int i = dp - dp2;
                        int dp3 = i - AndroidUtilities.dp(2.0f);
                        int dp4 = i - AndroidUtilities.dp(2.0f);
                        drawable.setBounds(dp3, dp4, dp3 + dp2, dp2 + dp4);
                        drawable.draw(canvas);
                        canvas.setBitmap(null);
                        bitmap = createBitmap;
                    }
                } else {
                    bitmap = null;
                }
                if (Build.VERSION.SDK_INT < 26) {
                    ShortcutInfoCompat.Builder intent = new ShortcutInfoCompat.Builder(ApplicationLoader.applicationContext, "sdid_" + j).setShortLabel(str).setIntent(createIntrnalShortcutIntent);
                    if (bitmap != null) {
                        intent.setIcon(IconCompat.createWithBitmap(bitmap));
                    } else if (tLRPC$User != null) {
                        if (tLRPC$User.bot) {
                            intent.setIcon(IconCompat.createWithResource(ApplicationLoader.applicationContext, 2131165277));
                        } else {
                            intent.setIcon(IconCompat.createWithResource(ApplicationLoader.applicationContext, 2131165281));
                        }
                    } else if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                        intent.setIcon(IconCompat.createWithResource(ApplicationLoader.applicationContext, 2131165278));
                    } else {
                        intent.setIcon(IconCompat.createWithResource(ApplicationLoader.applicationContext, 2131165279));
                    }
                    ShortcutManagerCompat.requestPinShortcut(ApplicationLoader.applicationContext, intent.build(), null);
                    return;
                }
                Intent intent2 = new Intent();
                if (bitmap != null) {
                    intent2.putExtra("android.intent.extra.shortcut.ICON", bitmap);
                } else if (tLRPC$User != null) {
                    if (tLRPC$User.bot) {
                        intent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(ApplicationLoader.applicationContext, 2131165277));
                    } else {
                        intent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(ApplicationLoader.applicationContext, 2131165281));
                    }
                } else if (ChatObject.isChannel(tLRPC$Chat) && !tLRPC$Chat.megagroup) {
                    intent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(ApplicationLoader.applicationContext, 2131165278));
                } else {
                    intent2.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(ApplicationLoader.applicationContext, 2131165279));
                }
                intent2.putExtra("android.intent.extra.shortcut.INTENT", createIntrnalShortcutIntent);
                intent2.putExtra("android.intent.extra.shortcut.NAME", str);
                intent2.putExtra("duplicate", false);
                intent2.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                ApplicationLoader.applicationContext.sendBroadcast(intent2);
                return;
            }
            tLRPC$User = tLRPC$User2;
            tLRPC$Chat = null;
            if (tLRPC$User != null) {
            }
            if (tLRPC$User == null) {
            }
            if (!z) {
            }
            if (z) {
            }
            if (!z) {
            }
            int dp5 = AndroidUtilities.dp(58.0f);
            Bitmap createBitmap2 = Bitmap.createBitmap(dp5, dp5, Bitmap.Config.ARGB_8888);
            createBitmap2.eraseColor(0);
            Canvas canvas2 = new Canvas(createBitmap2);
            if (!z) {
            }
            Drawable drawable2 = ApplicationLoader.applicationContext.getResources().getDrawable(2131165280);
            int dp22 = AndroidUtilities.dp(15.0f);
            int i2 = dp5 - dp22;
            int dp32 = i2 - AndroidUtilities.dp(2.0f);
            int dp42 = i2 - AndroidUtilities.dp(2.0f);
            drawable2.setBounds(dp32, dp42, dp32 + dp22, dp22 + dp42);
            drawable2.draw(canvas2);
            canvas2.setBitmap(null);
            bitmap = createBitmap2;
            if (Build.VERSION.SDK_INT < 26) {
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00a4 A[Catch: Exception -> 0x00d4, TryCatch #0 {Exception -> 0x00d4, blocks: (B:2:0x0000, B:4:0x0006, B:6:0x003c, B:7:0x004b, B:9:0x0052, B:12:0x0065, B:13:0x0074, B:15:0x007a, B:17:0x008a, B:19:0x0090, B:24:0x00a4, B:25:0x00ad, B:26:0x00af), top: B:31:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00ad A[Catch: Exception -> 0x00d4, TryCatch #0 {Exception -> 0x00d4, blocks: (B:2:0x0000, B:4:0x0006, B:6:0x003c, B:7:0x004b, B:9:0x0052, B:12:0x0065, B:13:0x0074, B:15:0x007a, B:17:0x008a, B:19:0x0090, B:24:0x00a4, B:25:0x00ad, B:26:0x00af), top: B:31:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void uninstallShortcut(long j) {
        TLRPC$Chat tLRPC$Chat;
        String str;
        TLRPC$User tLRPC$User;
        try {
            int i = Build.VERSION.SDK_INT;
            if (i >= 26) {
                ArrayList arrayList = new ArrayList();
                arrayList.add("sdid_" + j);
                arrayList.add("ndid_" + j);
                ShortcutManagerCompat.removeDynamicShortcuts(ApplicationLoader.applicationContext, arrayList);
                if (i < 30) {
                    return;
                }
                ((ShortcutManager) ApplicationLoader.applicationContext.getSystemService(ShortcutManager.class)).removeLongLivedShortcuts(arrayList);
                return;
            }
            TLRPC$User tLRPC$User2 = null;
            if (DialogObject.isEncryptedDialog(j)) {
                TLRPC$EncryptedChat encryptedChat = getMessagesController().getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j)));
                if (encryptedChat == null) {
                    return;
                }
                tLRPC$User = getMessagesController().getUser(Long.valueOf(encryptedChat.user_id));
            } else if (DialogObject.isUserDialog(j)) {
                tLRPC$User = getMessagesController().getUser(Long.valueOf(j));
            } else if (!DialogObject.isChatDialog(j)) {
                return;
            } else {
                tLRPC$Chat = getMessagesController().getChat(Long.valueOf(-j));
                if (tLRPC$User2 != null && tLRPC$Chat == null) {
                    return;
                }
                if (tLRPC$User2 == null) {
                    str = ContactsController.formatName(tLRPC$User2.first_name, tLRPC$User2.last_name);
                } else {
                    str = tLRPC$Chat.title;
                }
                Intent intent = new Intent();
                intent.putExtra("android.intent.extra.shortcut.INTENT", createIntrnalShortcutIntent(j));
                intent.putExtra("android.intent.extra.shortcut.NAME", str);
                intent.putExtra("duplicate", false);
                intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
                ApplicationLoader.applicationContext.sendBroadcast(intent);
            }
            tLRPC$User2 = tLRPC$User;
            tLRPC$Chat = null;
            if (tLRPC$User2 != null) {
            }
            if (tLRPC$User2 == null) {
            }
            Intent intent2 = new Intent();
            intent2.putExtra("android.intent.extra.shortcut.INTENT", createIntrnalShortcutIntent(j));
            intent2.putExtra("android.intent.extra.shortcut.NAME", str);
            intent2.putExtra("duplicate", false);
            intent2.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
            ApplicationLoader.applicationContext.sendBroadcast(intent2);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static /* synthetic */ int lambda$static$120(TLRPC$MessageEntity tLRPC$MessageEntity, TLRPC$MessageEntity tLRPC$MessageEntity2) {
        int i = tLRPC$MessageEntity.offset;
        int i2 = tLRPC$MessageEntity2.offset;
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    public void loadPinnedMessages(long j, int i, int i2) {
        if (this.loadingPinnedMessages.indexOfKey(j) >= 0) {
            return;
        }
        this.loadingPinnedMessages.put(j, Boolean.TRUE);
        TLRPC$TL_messages_search tLRPC$TL_messages_search = new TLRPC$TL_messages_search();
        tLRPC$TL_messages_search.peer = getMessagesController().getInputPeer(j);
        tLRPC$TL_messages_search.limit = 40;
        tLRPC$TL_messages_search.offset_id = i;
        tLRPC$TL_messages_search.q = "";
        tLRPC$TL_messages_search.filter = new TLRPC$TL_inputMessagesFilterPinned();
        getConnectionsManager().sendRequest(tLRPC$TL_messages_search, new MediaDataController$$ExternalSyntheticLambda147(this, i2, tLRPC$TL_messages_search, j, i));
    }

    public /* synthetic */ void lambda$loadPinnedMessages$122(int i, TLRPC$TL_messages_search tLRPC$TL_messages_search, long j, int i2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        boolean z;
        int i3;
        int i4;
        ArrayList<Integer> arrayList = new ArrayList<>();
        HashMap<Integer, MessageObject> hashMap = new HashMap<>();
        if (tLObject instanceof TLRPC$messages_Messages) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            LongSparseArray longSparseArray = new LongSparseArray();
            for (int i5 = 0; i5 < tLRPC$messages_Messages.users.size(); i5++) {
                TLRPC$User tLRPC$User = tLRPC$messages_Messages.users.get(i5);
                longSparseArray.put(tLRPC$User.id, tLRPC$User);
            }
            LongSparseArray longSparseArray2 = new LongSparseArray();
            for (int i6 = 0; i6 < tLRPC$messages_Messages.chats.size(); i6++) {
                TLRPC$Chat tLRPC$Chat = tLRPC$messages_Messages.chats.get(i6);
                longSparseArray2.put(tLRPC$Chat.id, tLRPC$Chat);
            }
            getMessagesStorage().putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
            getMessagesController().putUsers(tLRPC$messages_Messages.users, false);
            getMessagesController().putChats(tLRPC$messages_Messages.chats, false);
            int size = tLRPC$messages_Messages.messages.size();
            for (int i7 = 0; i7 < size; i7++) {
                TLRPC$Message tLRPC$Message = tLRPC$messages_Messages.messages.get(i7);
                if (!(tLRPC$Message instanceof TLRPC$TL_messageService) && !(tLRPC$Message instanceof TLRPC$TL_messageEmpty)) {
                    arrayList.add(Integer.valueOf(tLRPC$Message.id));
                    hashMap.put(Integer.valueOf(tLRPC$Message.id), new MessageObject(this.currentAccount, tLRPC$Message, (LongSparseArray<TLRPC$User>) longSparseArray, (LongSparseArray<TLRPC$Chat>) longSparseArray2, false, false));
                }
            }
            if (i != 0 && arrayList.isEmpty()) {
                arrayList.add(Integer.valueOf(i));
            }
            boolean z2 = tLRPC$messages_Messages.messages.size() < tLRPC$TL_messages_search.limit;
            i3 = Math.max(tLRPC$messages_Messages.count, tLRPC$messages_Messages.messages.size());
            z = z2;
        } else {
            if (i != 0) {
                arrayList.add(Integer.valueOf(i));
                i4 = 1;
            } else {
                i4 = 0;
            }
            i3 = i4;
            z = false;
        }
        getMessagesStorage().updatePinnedMessages(j, arrayList, true, i3, i2, z, hashMap);
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda29(this, j));
    }

    public /* synthetic */ void lambda$loadPinnedMessages$121(long j) {
        this.loadingPinnedMessages.remove(j);
    }

    public /* synthetic */ void lambda$loadPinnedMessages$123(long j, long j2, ArrayList arrayList) {
        loadPinnedMessageInternal(j, j2, arrayList, false);
    }

    public ArrayList<MessageObject> loadPinnedMessages(long j, long j2, ArrayList<Integer> arrayList, boolean z) {
        if (z) {
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda42(this, j, j2, arrayList));
            return null;
        }
        return loadPinnedMessageInternal(j, j2, arrayList, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0174 A[Catch: Exception -> 0x01c7, TryCatch #1 {Exception -> 0x01c7, blocks: (B:42:0x0165, B:44:0x016e, B:46:0x0174, B:48:0x017a, B:50:0x018a, B:52:0x0190, B:55:0x01a2, B:57:0x01b5), top: B:64:0x0165 }] */
    /* JADX WARN: Removed duplicated region for block: B:76:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v10, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r10v11, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r17v0, types: [org.telegram.messenger.MediaDataController, org.telegram.messenger.BaseController] */
    /* JADX WARN: Type inference failed for: r3v1, types: [java.lang.Object[]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private ArrayList<MessageObject> loadPinnedMessageInternal(long j, long j2, ArrayList<Integer> arrayList, boolean z) {
        Exception e;
        ?? r10;
        ArrayList<TLRPC$User> arrayList2;
        ArrayList<TLRPC$Chat> arrayList3;
        try {
            ArrayList<Integer> arrayList4 = new ArrayList<>(arrayList);
            if (j2 != 0) {
                r10 = new StringBuilder();
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    Integer num = arrayList.get(i);
                    if (r10.length() != 0) {
                        r10.append(",");
                    }
                    r10.append(num);
                }
            } else {
                r10 = TextUtils.join(",", arrayList);
            }
            ArrayList arrayList5 = new ArrayList();
            ArrayList<TLRPC$User> arrayList6 = new ArrayList<>();
            ArrayList<TLRPC$Chat> arrayList7 = new ArrayList<>();
            ArrayList arrayList8 = new ArrayList();
            ArrayList arrayList9 = new ArrayList();
            long j3 = getUserConfig().clientUserId;
            boolean z2 = false;
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid, date FROM messages_v2 WHERE mid IN (%s) AND uid = %d", new Object[]{r10, Long.valueOf(j)}), new Object[0]);
            while (queryFinalized.next()) {
                int i2 = z2 ? 1 : 0;
                int i3 = z2 ? 1 : 0;
                NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(i2);
                if (byteBufferValue != null) {
                    TLRPC$Message TLdeserialize = TLRPC$Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(z2), z2);
                    if (!(TLdeserialize.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                        TLdeserialize.readAttachPath(byteBufferValue, j3);
                        TLdeserialize.id = queryFinalized.intValue(1);
                        TLdeserialize.date = queryFinalized.intValue(2);
                        TLdeserialize.dialog_id = j;
                        MessagesStorage.addUsersAndChatsFromMessage(TLdeserialize, arrayList8, arrayList9);
                        arrayList5.add(TLdeserialize);
                        arrayList4.remove(Integer.valueOf(TLdeserialize.id));
                    }
                    byteBufferValue.reuse();
                }
                z2 = false;
            }
            queryFinalized.dispose();
            if (!arrayList4.isEmpty()) {
                SQLiteCursor queryFinalized2 = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data FROM chat_pinned_v2 WHERE uid = %d AND mid IN (%s)", Long.valueOf(j), TextUtils.join(",", arrayList4)), new Object[0]);
                while (queryFinalized2.next()) {
                    NativeByteBuffer byteBufferValue2 = queryFinalized2.byteBufferValue(0);
                    if (byteBufferValue2 != null) {
                        TLRPC$Message TLdeserialize2 = TLRPC$Message.TLdeserialize(byteBufferValue2, byteBufferValue2.readInt32(false), false);
                        if (!(TLdeserialize2.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                            TLdeserialize2.readAttachPath(byteBufferValue2, j3);
                            TLdeserialize2.dialog_id = j;
                            MessagesStorage.addUsersAndChatsFromMessage(TLdeserialize2, arrayList8, arrayList9);
                            arrayList5.add(TLdeserialize2);
                            arrayList4.remove(Integer.valueOf(TLdeserialize2.id));
                        }
                        byteBufferValue2.reuse();
                    }
                }
                queryFinalized2.dispose();
            }
            if (!arrayList4.isEmpty()) {
                if (j2 != 0) {
                    TLRPC$TL_channels_getMessages tLRPC$TL_channels_getMessages = new TLRPC$TL_channels_getMessages();
                    tLRPC$TL_channels_getMessages.channel = getMessagesController().getInputChannel(j2);
                    tLRPC$TL_channels_getMessages.id = arrayList4;
                    getConnectionsManager().sendRequest(tLRPC$TL_channels_getMessages, new MediaDataController$$ExternalSyntheticLambda155(this, j2, j, tLRPC$TL_channels_getMessages));
                } else {
                    TLRPC$TL_messages_getMessages tLRPC$TL_messages_getMessages = new TLRPC$TL_messages_getMessages();
                    tLRPC$TL_messages_getMessages.id = arrayList4;
                    try {
                        getConnectionsManager().sendRequest(tLRPC$TL_messages_getMessages, new MediaDataController$$ExternalSyntheticLambda157(this, j, tLRPC$TL_messages_getMessages));
                        if (!arrayList5.isEmpty()) {
                            return null;
                        }
                        if (!arrayList8.isEmpty()) {
                            arrayList2 = arrayList6;
                            getMessagesStorage().getUsersInternal(TextUtils.join(",", arrayList8), arrayList2);
                        } else {
                            arrayList2 = arrayList6;
                        }
                        if (!arrayList9.isEmpty()) {
                            arrayList3 = arrayList7;
                            getMessagesStorage().getChatsInternal(TextUtils.join(",", arrayList9), arrayList3);
                        } else {
                            arrayList3 = arrayList7;
                        }
                        if (z) {
                            return broadcastPinnedMessage(arrayList5, arrayList2, arrayList3, true, true);
                        }
                        broadcastPinnedMessage(arrayList5, arrayList2, arrayList3, true, false);
                        return null;
                    } catch (Exception e2) {
                        e = e2;
                        FileLog.e(e);
                        return null;
                    }
                }
            }
            if (!arrayList5.isEmpty()) {
            }
        } catch (Exception e3) {
            e = e3;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0048  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadPinnedMessageInternal$124(long j, long j2, TLRPC$TL_channels_getMessages tLRPC$TL_channels_getMessages, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        boolean z = true;
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            removeEmptyMessages(tLRPC$messages_Messages.messages);
            if (!tLRPC$messages_Messages.messages.isEmpty()) {
                getMessagesController().getChat(Long.valueOf(j));
                ImageLoader.saveMessagesThumbs(tLRPC$messages_Messages.messages);
                broadcastPinnedMessage(tLRPC$messages_Messages.messages, tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, false, false);
                getMessagesStorage().putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
                savePinnedMessages(j2, tLRPC$messages_Messages.messages);
                if (!z) {
                    return;
                }
                getMessagesStorage().updatePinnedMessages(j2, tLRPC$TL_channels_getMessages.id, false, -1, 0, false, null);
                return;
            }
        }
        z = false;
        if (!z) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0037  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$loadPinnedMessageInternal$125(long j, TLRPC$TL_messages_getMessages tLRPC$TL_messages_getMessages, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        boolean z = true;
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            removeEmptyMessages(tLRPC$messages_Messages.messages);
            if (!tLRPC$messages_Messages.messages.isEmpty()) {
                ImageLoader.saveMessagesThumbs(tLRPC$messages_Messages.messages);
                broadcastPinnedMessage(tLRPC$messages_Messages.messages, tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, false, false);
                getMessagesStorage().putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
                savePinnedMessages(j, tLRPC$messages_Messages.messages);
                if (!z) {
                    return;
                }
                getMessagesStorage().updatePinnedMessages(j, tLRPC$TL_messages_getMessages.id, false, -1, 0, false, null);
                return;
            }
        }
        z = false;
        if (!z) {
        }
    }

    private void savePinnedMessages(long j, ArrayList<TLRPC$Message> arrayList) {
        if (arrayList.isEmpty()) {
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda69(this, arrayList, j));
    }

    public /* synthetic */ void lambda$savePinnedMessages$126(ArrayList arrayList, long j) {
        try {
            getMessagesStorage().getDatabase().beginTransaction();
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO chat_pinned_v2 VALUES(?, ?, ?)");
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                TLRPC$Message tLRPC$Message = (TLRPC$Message) arrayList.get(i);
                NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tLRPC$Message.getObjectSize());
                tLRPC$Message.serializeToStream(nativeByteBuffer);
                executeFast.requery();
                executeFast.bindLong(1, j);
                executeFast.bindInteger(2, tLRPC$Message.id);
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

    private ArrayList<MessageObject> broadcastPinnedMessage(ArrayList<TLRPC$Message> arrayList, ArrayList<TLRPC$User> arrayList2, ArrayList<TLRPC$Chat> arrayList3, boolean z, boolean z2) {
        if (arrayList.isEmpty()) {
            return null;
        }
        LongSparseArray longSparseArray = new LongSparseArray();
        for (int i = 0; i < arrayList2.size(); i++) {
            TLRPC$User tLRPC$User = arrayList2.get(i);
            longSparseArray.put(tLRPC$User.id, tLRPC$User);
        }
        LongSparseArray longSparseArray2 = new LongSparseArray();
        for (int i2 = 0; i2 < arrayList3.size(); i2++) {
            TLRPC$Chat tLRPC$Chat = arrayList3.get(i2);
            longSparseArray2.put(tLRPC$Chat.id, tLRPC$Chat);
        }
        ArrayList<MessageObject> arrayList4 = new ArrayList<>();
        if (z2) {
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda76(this, arrayList2, z, arrayList3));
            int size = arrayList.size();
            int i3 = 0;
            int i4 = 0;
            while (i4 < size) {
                TLRPC$Message tLRPC$Message = arrayList.get(i4);
                TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
                if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto)) {
                    i3++;
                }
                int i5 = i3;
                arrayList4.add(new MessageObject(this.currentAccount, tLRPC$Message, (LongSparseArray<TLRPC$User>) longSparseArray, (LongSparseArray<TLRPC$Chat>) longSparseArray2, false, i5 < 30));
                i4++;
                i3 = i5;
            }
            return arrayList4;
        }
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda78(this, arrayList2, z, arrayList3, arrayList, arrayList4, longSparseArray, longSparseArray2));
        return null;
    }

    public /* synthetic */ void lambda$broadcastPinnedMessage$127(ArrayList arrayList, boolean z, ArrayList arrayList2) {
        getMessagesController().putUsers(arrayList, z);
        getMessagesController().putChats(arrayList2, z);
    }

    public /* synthetic */ void lambda$broadcastPinnedMessage$129(ArrayList arrayList, boolean z, ArrayList arrayList2, ArrayList arrayList3, ArrayList arrayList4, LongSparseArray longSparseArray, LongSparseArray longSparseArray2) {
        getMessagesController().putUsers(arrayList, z);
        getMessagesController().putChats(arrayList2, z);
        int size = arrayList3.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            TLRPC$Message tLRPC$Message = (TLRPC$Message) arrayList3.get(i2);
            TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
            if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto)) {
                i++;
            }
            arrayList4.add(new MessageObject(this.currentAccount, tLRPC$Message, (LongSparseArray<TLRPC$User>) longSparseArray, (LongSparseArray<TLRPC$Chat>) longSparseArray2, false, i < 30));
        }
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda64(this, arrayList4));
    }

    public /* synthetic */ void lambda$broadcastPinnedMessage$128(ArrayList arrayList) {
        getNotificationCenter().postNotificationName(NotificationCenter.didLoadPinnedMessages, Long.valueOf(((MessageObject) arrayList.get(0)).getDialogId()), null, Boolean.TRUE, arrayList, 0, 0, -1, Boolean.FALSE);
    }

    private static void removeEmptyMessages(ArrayList<TLRPC$Message> arrayList) {
        int i = 0;
        while (i < arrayList.size()) {
            TLRPC$Message tLRPC$Message = arrayList.get(i);
            if (tLRPC$Message == null || (tLRPC$Message instanceof TLRPC$TL_messageEmpty) || (tLRPC$Message.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                arrayList.remove(i);
                i--;
            }
            i++;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x00b6, code lost:
        if (r12 != 0) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00bf, code lost:
        if (r12 != 0) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00c1, code lost:
        r10 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00c2, code lost:
        r8 = r1.replyMessageObject;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00c4, code lost:
        if (r8 == null) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00c6, code lost:
        r8 = r8.messageOwner;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00c8, code lost:
        if (r8 == null) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00ca, code lost:
        r8 = r8.peer_id;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00cc, code lost:
        if (r8 == null) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00d0, code lost:
        if ((r7 instanceof org.telegram.tgnet.TLRPC$TL_messageEmpty) == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00d7, code lost:
        if (r8.channel_id != r10) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00da, code lost:
        r7 = (android.util.SparseArray) r3.get(r16);
        r8 = (java.util.ArrayList) r4.get(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00e6, code lost:
        if (r7 != null) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00e8, code lost:
        r7 = new android.util.SparseArray();
        r3.put(r16, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00f0, code lost:
        if (r8 != null) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00f2, code lost:
        r8 = new java.util.ArrayList();
        r4.put(r10, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00fa, code lost:
        r10 = (java.util.ArrayList) r7.get(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0100, code lost:
        if (r10 != null) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0102, code lost:
        r10 = new java.util.ArrayList();
        r7.put(r9, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0112, code lost:
        if (r8.contains(java.lang.Integer.valueOf(r9)) != false) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0114, code lost:
        r8.add(java.lang.Integer.valueOf(r9));
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x011b, code lost:
        r10.add(r1);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadReplyMessagesForMessages(ArrayList<MessageObject> arrayList, long j, boolean z, Runnable runnable) {
        long j2;
        int i = 0;
        if (DialogObject.isEncryptedDialog(j)) {
            ArrayList arrayList2 = new ArrayList();
            LongSparseArray longSparseArray = new LongSparseArray();
            while (i < arrayList.size()) {
                MessageObject messageObject = arrayList.get(i);
                if (messageObject != null && messageObject.isReply() && messageObject.replyMessageObject == null) {
                    long j3 = messageObject.messageOwner.reply_to.reply_to_random_id;
                    ArrayList arrayList3 = (ArrayList) longSparseArray.get(j3);
                    if (arrayList3 == null) {
                        arrayList3 = new ArrayList();
                        longSparseArray.put(j3, arrayList3);
                    }
                    arrayList3.add(messageObject);
                    if (!arrayList2.contains(Long.valueOf(j3))) {
                        arrayList2.add(Long.valueOf(j3));
                    }
                }
                i++;
            }
            if (!arrayList2.isEmpty()) {
                getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda72(this, arrayList2, j, longSparseArray, runnable));
                return;
            } else if (runnable == null) {
                return;
            } else {
                runnable.run();
                return;
            }
        }
        LongSparseArray longSparseArray2 = new LongSparseArray();
        LongSparseArray longSparseArray3 = new LongSparseArray();
        while (i < arrayList.size()) {
            MessageObject messageObject2 = arrayList.get(i);
            if (messageObject2 != null && messageObject2.getId() > 0 && messageObject2.isReply()) {
                TLRPC$Message tLRPC$Message = messageObject2.messageOwner;
                TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = tLRPC$Message.reply_to;
                int i2 = tLRPC$TL_messageReplyHeader.reply_to_msg_id;
                TLRPC$Peer tLRPC$Peer = tLRPC$TL_messageReplyHeader.reply_to_peer_id;
                long j4 = 0;
                if (tLRPC$Peer != null) {
                    j2 = tLRPC$Peer.channel_id;
                } else {
                    j2 = tLRPC$Message.peer_id.channel_id;
                }
            }
            i++;
        }
        if (!longSparseArray2.isEmpty()) {
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda52(this, longSparseArray2, longSparseArray3, z, j, runnable));
        } else if (runnable == null) {
        } else {
            runnable.run();
        }
    }

    public /* synthetic */ void lambda$loadReplyMessagesForMessages$131(ArrayList arrayList, long j, LongSparseArray longSparseArray, Runnable runnable) {
        try {
            ArrayList arrayList2 = new ArrayList();
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT m.data, m.mid, m.date, r.random_id FROM randoms_v2 as r INNER JOIN messages_v2 as m ON r.mid = m.mid AND r.uid = m.uid WHERE r.random_id IN(%s)", TextUtils.join(",", arrayList)), new Object[0]);
            while (queryFinalized.next()) {
                NativeByteBuffer byteBufferValue = queryFinalized.byteBufferValue(0);
                if (byteBufferValue != null) {
                    TLRPC$Message TLdeserialize = TLRPC$Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                    TLdeserialize.readAttachPath(byteBufferValue, getUserConfig().clientUserId);
                    byteBufferValue.reuse();
                    TLdeserialize.id = queryFinalized.intValue(1);
                    TLdeserialize.date = queryFinalized.intValue(2);
                    TLdeserialize.dialog_id = j;
                    long longValue = queryFinalized.longValue(3);
                    ArrayList arrayList3 = (ArrayList) longSparseArray.get(longValue);
                    longSparseArray.remove(longValue);
                    if (arrayList3 != null) {
                        MessageObject messageObject = new MessageObject(this.currentAccount, TLdeserialize, false, false);
                        arrayList2.add(messageObject);
                        for (int i = 0; i < arrayList3.size(); i++) {
                            MessageObject messageObject2 = (MessageObject) arrayList3.get(i);
                            messageObject2.replyMessageObject = messageObject;
                            messageObject2.messageOwner.reply_to = new TLRPC$TL_messageReplyHeader();
                            messageObject2.messageOwner.reply_to.reply_to_msg_id = messageObject.getId();
                        }
                    }
                }
            }
            queryFinalized.dispose();
            if (longSparseArray.size() != 0) {
                for (int i2 = 0; i2 < longSparseArray.size(); i2++) {
                    ArrayList arrayList4 = (ArrayList) longSparseArray.valueAt(i2);
                    for (int i3 = 0; i3 < arrayList4.size(); i3++) {
                        TLRPC$TL_messageReplyHeader tLRPC$TL_messageReplyHeader = ((MessageObject) arrayList4.get(i3)).messageOwner.reply_to;
                        if (tLRPC$TL_messageReplyHeader != null) {
                            tLRPC$TL_messageReplyHeader.reply_to_random_id = 0L;
                        }
                    }
                }
            }
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda43(this, j, arrayList2));
            if (runnable == null) {
                return;
            }
            runnable.run();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$loadReplyMessagesForMessages$130(long j, ArrayList arrayList) {
        getNotificationCenter().postNotificationName(NotificationCenter.replyMessagesDidLoad, Long.valueOf(j), arrayList, 0);
    }

    public /* synthetic */ void lambda$loadReplyMessagesForMessages$135(LongSparseArray longSparseArray, LongSparseArray longSparseArray2, boolean z, long j, Runnable runnable) {
        int i;
        int i2;
        boolean z2;
        SQLiteCursor sQLiteCursor;
        LongSparseArray longSparseArray3 = longSparseArray;
        try {
            ArrayList<TLRPC$Message> arrayList = new ArrayList<>();
            ArrayList<TLRPC$User> arrayList2 = new ArrayList<>();
            ArrayList<TLRPC$Chat> arrayList3 = new ArrayList<>();
            ArrayList arrayList4 = new ArrayList();
            ArrayList arrayList5 = new ArrayList();
            int i3 = 0;
            for (int size = longSparseArray.size(); i3 < size; size = i2) {
                long keyAt = longSparseArray3.keyAt(i3);
                SparseArray sparseArray = (SparseArray) longSparseArray3.valueAt(i3);
                ArrayList arrayList6 = (ArrayList) longSparseArray2.get(keyAt);
                if (arrayList6 == null) {
                    i2 = size;
                } else {
                    if (z) {
                        i2 = size;
                        sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, mid, date, uid FROM scheduled_messages_v2 WHERE mid IN(%s) AND uid = %d", TextUtils.join(",", arrayList6), Long.valueOf(j)), new Object[0]);
                        z2 = false;
                    } else {
                        i2 = size;
                        SQLiteDatabase database = getMessagesStorage().getDatabase();
                        Locale locale = Locale.US;
                        String join = TextUtils.join(",", arrayList6);
                        z2 = false;
                        sQLiteCursor = database.queryFinalized(String.format(locale, "SELECT data, mid, date, uid FROM messages_v2 WHERE mid IN(%s) AND uid = %d", join, Long.valueOf(j)), new Object[0]);
                    }
                    while (sQLiteCursor.next()) {
                        int i4 = z2 ? 1 : 0;
                        int i5 = z2 ? 1 : 0;
                        int i6 = z2 ? 1 : 0;
                        int i7 = z2 ? 1 : 0;
                        NativeByteBuffer byteBufferValue = sQLiteCursor.byteBufferValue(i4);
                        if (byteBufferValue != null) {
                            TLRPC$Message TLdeserialize = TLRPC$Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(z2), z2);
                            TLdeserialize.readAttachPath(byteBufferValue, getUserConfig().clientUserId);
                            byteBufferValue.reuse();
                            TLdeserialize.id = sQLiteCursor.intValue(1);
                            TLdeserialize.date = sQLiteCursor.intValue(2);
                            TLdeserialize.dialog_id = j;
                            MessagesStorage.addUsersAndChatsFromMessage(TLdeserialize, arrayList4, arrayList5);
                            arrayList.add(TLdeserialize);
                            TLRPC$Peer tLRPC$Peer = TLdeserialize.peer_id;
                            long j2 = tLRPC$Peer != null ? tLRPC$Peer.channel_id : 0L;
                            ArrayList arrayList7 = (ArrayList) longSparseArray2.get(j2);
                            if (arrayList7 != null) {
                                arrayList7.remove(Integer.valueOf(TLdeserialize.id));
                                if (arrayList7.isEmpty()) {
                                    longSparseArray2.remove(j2);
                                }
                            }
                        }
                        z2 = false;
                    }
                    sQLiteCursor.dispose();
                }
                i3++;
                longSparseArray3 = longSparseArray;
            }
            if (!arrayList4.isEmpty()) {
                getMessagesStorage().getUsersInternal(TextUtils.join(",", arrayList4), arrayList2);
            }
            if (!arrayList5.isEmpty()) {
                getMessagesStorage().getChatsInternal(TextUtils.join(",", arrayList5), arrayList3);
            }
            broadcastReplyMessages(arrayList, longSparseArray, arrayList2, arrayList3, j, true);
            if (longSparseArray2.isEmpty()) {
                if (runnable == null) {
                    return;
                }
                AndroidUtilities.runOnUIThread(runnable);
                return;
            }
            int size2 = longSparseArray2.size();
            int i8 = 0;
            while (i8 < size2) {
                long keyAt2 = longSparseArray2.keyAt(i8);
                if (z) {
                    TLRPC$TL_messages_getScheduledMessages tLRPC$TL_messages_getScheduledMessages = new TLRPC$TL_messages_getScheduledMessages();
                    tLRPC$TL_messages_getScheduledMessages.peer = getMessagesController().getInputPeer(j);
                    tLRPC$TL_messages_getScheduledMessages.id = (ArrayList) longSparseArray2.valueAt(i8);
                    i = size2;
                    getConnectionsManager().sendRequest(tLRPC$TL_messages_getScheduledMessages, new MediaDataController$$ExternalSyntheticLambda154(this, j, keyAt2, longSparseArray, z, runnable));
                } else {
                    i = size2;
                    if (keyAt2 != 0) {
                        TLRPC$TL_channels_getMessages tLRPC$TL_channels_getMessages = new TLRPC$TL_channels_getMessages();
                        tLRPC$TL_channels_getMessages.channel = getMessagesController().getInputChannel(keyAt2);
                        tLRPC$TL_channels_getMessages.id = (ArrayList) longSparseArray2.valueAt(i8);
                        getConnectionsManager().sendRequest(tLRPC$TL_channels_getMessages, new MediaDataController$$ExternalSyntheticLambda153(this, j, keyAt2, longSparseArray, z, runnable));
                    } else {
                        TLRPC$TL_messages_getMessages tLRPC$TL_messages_getMessages = new TLRPC$TL_messages_getMessages();
                        tLRPC$TL_messages_getMessages.id = (ArrayList) longSparseArray2.valueAt(i8);
                        getConnectionsManager().sendRequest(tLRPC$TL_messages_getMessages, new MediaDataController$$ExternalSyntheticLambda156(this, j, longSparseArray, z, runnable));
                    }
                }
                i8++;
                size2 = i;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$loadReplyMessagesForMessages$132(long j, long j2, LongSparseArray longSparseArray, boolean z, Runnable runnable, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            for (int i = 0; i < tLRPC$messages_Messages.messages.size(); i++) {
                TLRPC$Message tLRPC$Message = tLRPC$messages_Messages.messages.get(i);
                if (tLRPC$Message.dialog_id == 0) {
                    tLRPC$Message.dialog_id = j;
                }
            }
            MessageObject.fixMessagePeer(tLRPC$messages_Messages.messages, j2);
            ImageLoader.saveMessagesThumbs(tLRPC$messages_Messages.messages);
            broadcastReplyMessages(tLRPC$messages_Messages.messages, longSparseArray, tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, j, false);
            getMessagesStorage().putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
            saveReplyMessages(longSparseArray, tLRPC$messages_Messages.messages, z);
        }
        if (runnable != null) {
            AndroidUtilities.runOnUIThread(runnable);
        }
    }

    public /* synthetic */ void lambda$loadReplyMessagesForMessages$133(long j, long j2, LongSparseArray longSparseArray, boolean z, Runnable runnable, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            for (int i = 0; i < tLRPC$messages_Messages.messages.size(); i++) {
                TLRPC$Message tLRPC$Message = tLRPC$messages_Messages.messages.get(i);
                if (tLRPC$Message.dialog_id == 0) {
                    tLRPC$Message.dialog_id = j;
                }
            }
            MessageObject.fixMessagePeer(tLRPC$messages_Messages.messages, j2);
            ImageLoader.saveMessagesThumbs(tLRPC$messages_Messages.messages);
            broadcastReplyMessages(tLRPC$messages_Messages.messages, longSparseArray, tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, j, false);
            getMessagesStorage().putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
            saveReplyMessages(longSparseArray, tLRPC$messages_Messages.messages, z);
        }
        if (runnable != null) {
            AndroidUtilities.runOnUIThread(runnable);
        }
    }

    public /* synthetic */ void lambda$loadReplyMessagesForMessages$134(long j, LongSparseArray longSparseArray, boolean z, Runnable runnable, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            for (int i = 0; i < tLRPC$messages_Messages.messages.size(); i++) {
                TLRPC$Message tLRPC$Message = tLRPC$messages_Messages.messages.get(i);
                if (tLRPC$Message.dialog_id == 0) {
                    tLRPC$Message.dialog_id = j;
                }
            }
            ImageLoader.saveMessagesThumbs(tLRPC$messages_Messages.messages);
            broadcastReplyMessages(tLRPC$messages_Messages.messages, longSparseArray, tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, j, false);
            getMessagesStorage().putUsersAndChats(tLRPC$messages_Messages.users, tLRPC$messages_Messages.chats, true, true);
            saveReplyMessages(longSparseArray, tLRPC$messages_Messages.messages, z);
        }
        if (runnable != null) {
            AndroidUtilities.runOnUIThread(runnable);
        }
    }

    private void saveReplyMessages(LongSparseArray<SparseArray<ArrayList<MessageObject>>> longSparseArray, ArrayList<TLRPC$Message> arrayList, boolean z) {
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda116(this, z, arrayList, longSparseArray));
    }

    public /* synthetic */ void lambda$saveReplyMessages$136(boolean z, ArrayList arrayList, LongSparseArray longSparseArray) {
        SQLitePreparedStatement sQLitePreparedStatement;
        ArrayList arrayList2;
        try {
            getMessagesStorage().getDatabase().beginTransaction();
            if (z) {
                sQLitePreparedStatement = getMessagesStorage().getDatabase().executeFast("UPDATE scheduled_messages_v2 SET replydata = ?, reply_to_message_id = ? WHERE mid = ? AND uid = ?");
            } else {
                sQLitePreparedStatement = getMessagesStorage().getDatabase().executeFast("UPDATE messages_v2 SET replydata = ?, reply_to_message_id = ? WHERE mid = ? AND uid = ?");
            }
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC$Message tLRPC$Message = (TLRPC$Message) arrayList.get(i);
                SparseArray sparseArray = (SparseArray) longSparseArray.get(MessageObject.getDialogId(tLRPC$Message));
                if (sparseArray != null && (arrayList2 = (ArrayList) sparseArray.get(tLRPC$Message.id)) != null) {
                    NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tLRPC$Message.getObjectSize());
                    tLRPC$Message.serializeToStream(nativeByteBuffer);
                    for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                        MessageObject messageObject = (MessageObject) arrayList2.get(i2);
                        sQLitePreparedStatement.requery();
                        sQLitePreparedStatement.bindByteBuffer(1, nativeByteBuffer);
                        sQLitePreparedStatement.bindInteger(2, tLRPC$Message.id);
                        sQLitePreparedStatement.bindInteger(3, messageObject.getId());
                        sQLitePreparedStatement.bindLong(4, messageObject.getDialogId());
                        sQLitePreparedStatement.step();
                    }
                    nativeByteBuffer.reuse();
                }
            }
            sQLitePreparedStatement.dispose();
            getMessagesStorage().getDatabase().commitTransaction();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private void broadcastReplyMessages(ArrayList<TLRPC$Message> arrayList, LongSparseArray<SparseArray<ArrayList<MessageObject>>> longSparseArray, ArrayList<TLRPC$User> arrayList2, ArrayList<TLRPC$Chat> arrayList3, long j, boolean z) {
        LongSparseArray longSparseArray2 = new LongSparseArray();
        for (int i = 0; i < arrayList2.size(); i++) {
            TLRPC$User tLRPC$User = arrayList2.get(i);
            longSparseArray2.put(tLRPC$User.id, tLRPC$User);
        }
        LongSparseArray longSparseArray3 = new LongSparseArray();
        for (int i2 = 0; i2 < arrayList3.size(); i2++) {
            TLRPC$Chat tLRPC$Chat = arrayList3.get(i2);
            longSparseArray3.put(tLRPC$Chat.id, tLRPC$Chat);
        }
        ArrayList arrayList4 = new ArrayList();
        int size = arrayList.size();
        for (int i3 = 0; i3 < size; i3++) {
            arrayList4.add(new MessageObject(this.currentAccount, arrayList.get(i3), (LongSparseArray<TLRPC$User>) longSparseArray2, (LongSparseArray<TLRPC$Chat>) longSparseArray3, false, false));
        }
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda77(this, arrayList2, z, arrayList3, arrayList4, longSparseArray, j));
    }

    public /* synthetic */ void lambda$broadcastReplyMessages$137(ArrayList arrayList, boolean z, ArrayList arrayList2, ArrayList arrayList3, LongSparseArray longSparseArray, long j) {
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
                    TLRPC$MessageAction tLRPC$MessageAction = messageObject2.messageOwner.action;
                    if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPinMessage) {
                        messageObject2.generatePinMessageText(null, null);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionGameScore) {
                        messageObject2.generateGameMessageText(null);
                    } else if (tLRPC$MessageAction instanceof TLRPC$TL_messageActionPaymentSent) {
                        messageObject2.generatePaymentSentMessageText(null);
                    }
                }
                z2 = true;
            }
        }
        if (z2) {
            getNotificationCenter().postNotificationName(NotificationCenter.replyMessagesDidLoad, Long.valueOf(j), arrayList3, longSparseArray);
        }
    }

    public static void sortEntities(ArrayList<TLRPC$MessageEntity> arrayList) {
        Collections.sort(arrayList, entityComparator);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0027 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0029 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static boolean checkInclusion(int i, List<TLRPC$MessageEntity> list, boolean z) {
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            for (int i2 = 0; i2 < size; i2++) {
                TLRPC$MessageEntity tLRPC$MessageEntity = list.get(i2);
                int i3 = tLRPC$MessageEntity.offset;
                if (z) {
                    if (i3 >= i) {
                        continue;
                    }
                    if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length <= i) {
                        return true;
                    }
                } else {
                    if (i3 > i) {
                        continue;
                    }
                    if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length <= i) {
                    }
                }
            }
        }
        return false;
    }

    private static boolean checkIntersection(int i, int i2, List<TLRPC$MessageEntity> list) {
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            for (int i3 = 0; i3 < size; i3++) {
                TLRPC$MessageEntity tLRPC$MessageEntity = list.get(i3);
                int i4 = tLRPC$MessageEntity.offset;
                if (i4 > i && i4 + tLRPC$MessageEntity.length <= i2) {
                    return true;
                }
            }
        }
        return false;
    }

    public CharSequence substring(CharSequence charSequence, int i, int i2) {
        if (charSequence instanceof SpannableStringBuilder) {
            return charSequence.subSequence(i, i2);
        }
        if (charSequence instanceof SpannedString) {
            return charSequence.subSequence(i, i2);
        }
        return TextUtils.substring(charSequence, i, i2);
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
        if (!(characterStyle instanceof URLSpanReplacement)) {
            return null;
        }
        return new URLSpanReplacement(((URLSpanReplacement) characterStyle).getURL(), textStyleRun3);
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x008a  */
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

    public static void addTextStyleRuns(MessageObject messageObject, Spannable spannable) {
        addTextStyleRuns(messageObject.messageOwner.entities, messageObject.messageText, spannable, -1);
    }

    public static void addTextStyleRuns(TLRPC$DraftMessage tLRPC$DraftMessage, Spannable spannable, int i) {
        addTextStyleRuns(tLRPC$DraftMessage.entities, tLRPC$DraftMessage.message, spannable, i);
    }

    public static void addTextStyleRuns(MessageObject messageObject, Spannable spannable, int i) {
        addTextStyleRuns(messageObject.messageOwner.entities, messageObject.messageText, spannable, i);
    }

    public static void addTextStyleRuns(ArrayList<TLRPC$MessageEntity> arrayList, CharSequence charSequence, Spannable spannable) {
        addTextStyleRuns(arrayList, charSequence, spannable, -1);
    }

    public static void addTextStyleRuns(ArrayList<TLRPC$MessageEntity> arrayList, CharSequence charSequence, Spannable spannable, int i) {
        for (TextStyleSpan textStyleSpan : (TextStyleSpan[]) spannable.getSpans(0, spannable.length(), TextStyleSpan.class)) {
            spannable.removeSpan(textStyleSpan);
        }
        Iterator<TextStyleSpan.TextStyleRun> it = getTextStyleRuns(arrayList, charSequence, i).iterator();
        while (it.hasNext()) {
            TextStyleSpan.TextStyleRun next = it.next();
            addStyleToText(new TextStyleSpan(next), next.start, next.end, spannable, true);
        }
    }

    public static ArrayList<TextStyleSpan.TextStyleRun> getTextStyleRuns(ArrayList<TLRPC$MessageEntity> arrayList, CharSequence charSequence, int i) {
        int i2;
        ArrayList<TextStyleSpan.TextStyleRun> arrayList2 = new ArrayList<>();
        ArrayList arrayList3 = new ArrayList(arrayList);
        Collections.sort(arrayList3, MediaDataController$$ExternalSyntheticLambda125.INSTANCE);
        int size = arrayList3.size();
        for (int i3 = 0; i3 < size; i3++) {
            TLRPC$MessageEntity tLRPC$MessageEntity = (TLRPC$MessageEntity) arrayList3.get(i3);
            if (tLRPC$MessageEntity != null && tLRPC$MessageEntity.length > 0 && (i2 = tLRPC$MessageEntity.offset) >= 0 && i2 < charSequence.length()) {
                if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length > charSequence.length()) {
                    tLRPC$MessageEntity.length = charSequence.length() - tLRPC$MessageEntity.offset;
                }
                TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                int i4 = tLRPC$MessageEntity.offset;
                textStyleRun.start = i4;
                textStyleRun.end = i4 + tLRPC$MessageEntity.length;
                if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntitySpoiler) {
                    textStyleRun.flags = 256;
                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityStrike) {
                    textStyleRun.flags = 8;
                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUnderline) {
                    textStyleRun.flags = 16;
                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBlockquote) {
                    textStyleRun.flags = 32;
                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBold) {
                    textStyleRun.flags = 1;
                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityItalic) {
                    textStyleRun.flags = 2;
                } else if ((tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCode) || (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre)) {
                    textStyleRun.flags = 4;
                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMentionName) {
                    textStyleRun.flags = 64;
                    textStyleRun.urlEntity = tLRPC$MessageEntity;
                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_inputMessageEntityMentionName) {
                    textStyleRun.flags = 64;
                    textStyleRun.urlEntity = tLRPC$MessageEntity;
                } else {
                    textStyleRun.flags = 128;
                    textStyleRun.urlEntity = tLRPC$MessageEntity;
                }
                textStyleRun.flags &= i;
                int size2 = arrayList2.size();
                int i5 = 0;
                while (i5 < size2) {
                    TextStyleSpan.TextStyleRun textStyleRun2 = arrayList2.get(i5);
                    int i6 = textStyleRun.start;
                    int i7 = textStyleRun2.start;
                    if (i6 > i7) {
                        int i8 = textStyleRun2.end;
                        if (i6 < i8) {
                            if (textStyleRun.end < i8) {
                                TextStyleSpan.TextStyleRun textStyleRun3 = new TextStyleSpan.TextStyleRun(textStyleRun);
                                textStyleRun3.merge(textStyleRun2);
                                int i9 = i5 + 1;
                                arrayList2.add(i9, textStyleRun3);
                                TextStyleSpan.TextStyleRun textStyleRun4 = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                textStyleRun4.start = textStyleRun.end;
                                i5 = i9 + 1;
                                size2 = size2 + 1 + 1;
                                arrayList2.add(i5, textStyleRun4);
                            } else {
                                TextStyleSpan.TextStyleRun textStyleRun5 = new TextStyleSpan.TextStyleRun(textStyleRun);
                                textStyleRun5.merge(textStyleRun2);
                                textStyleRun5.end = textStyleRun2.end;
                                i5++;
                                size2++;
                                arrayList2.add(i5, textStyleRun5);
                            }
                            int i10 = textStyleRun.start;
                            textStyleRun.start = textStyleRun2.end;
                            textStyleRun2.end = i10;
                        }
                    } else {
                        int i11 = textStyleRun.end;
                        if (i7 < i11) {
                            int i12 = textStyleRun2.end;
                            if (i11 == i12) {
                                textStyleRun2.merge(textStyleRun);
                            } else if (i11 < i12) {
                                TextStyleSpan.TextStyleRun textStyleRun6 = new TextStyleSpan.TextStyleRun(textStyleRun2);
                                textStyleRun6.merge(textStyleRun);
                                textStyleRun6.end = textStyleRun.end;
                                i5++;
                                size2++;
                                arrayList2.add(i5, textStyleRun6);
                                textStyleRun2.start = textStyleRun.end;
                            } else {
                                TextStyleSpan.TextStyleRun textStyleRun7 = new TextStyleSpan.TextStyleRun(textStyleRun);
                                textStyleRun7.start = textStyleRun2.end;
                                i5++;
                                size2++;
                                arrayList2.add(i5, textStyleRun7);
                                textStyleRun2.merge(textStyleRun);
                            }
                            textStyleRun.end = i7;
                        }
                    }
                    i5++;
                }
                if (textStyleRun.start < textStyleRun.end) {
                    arrayList2.add(textStyleRun);
                }
            }
        }
        return arrayList2;
    }

    public static /* synthetic */ int lambda$getTextStyleRuns$138(TLRPC$MessageEntity tLRPC$MessageEntity, TLRPC$MessageEntity tLRPC$MessageEntity2) {
        int i = tLRPC$MessageEntity.offset;
        int i2 = tLRPC$MessageEntity2.offset;
        if (i > i2) {
            return 1;
        }
        return i < i2 ? -1 : 0;
    }

    public void addStyle(int i, int i2, int i3, ArrayList<TLRPC$MessageEntity> arrayList) {
        if ((i & 256) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC$TL_messageEntitySpoiler(), i2, i3));
        }
        if ((i & 1) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC$TL_messageEntityBold(), i2, i3));
        }
        if ((i & 2) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC$TL_messageEntityItalic(), i2, i3));
        }
        if ((i & 4) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC$TL_messageEntityCode(), i2, i3));
        }
        if ((i & 8) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC$TL_messageEntityStrike(), i2, i3));
        }
        if ((i & 16) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC$TL_messageEntityUnderline(), i2, i3));
        }
        if ((i & 32) != 0) {
            arrayList.add(setEntityStartEnd(new TLRPC$TL_messageEntityBlockquote(), i2, i3));
        }
    }

    private TLRPC$MessageEntity setEntityStartEnd(TLRPC$MessageEntity tLRPC$MessageEntity, int i, int i2) {
        tLRPC$MessageEntity.offset = i;
        tLRPC$MessageEntity.length = i2 - i;
        return tLRPC$MessageEntity;
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x0052, code lost:
        if (r1 != null) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0054, code lost:
        r1 = new java.util.ArrayList<>();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0059, code lost:
        if (r4 == false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x005b, code lost:
        r12 = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x005d, code lost:
        r12 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x005e, code lost:
        r12 = r12 + r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0065, code lost:
        if (r12 >= r19[0].length()) goto L161;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x006d, code lost:
        if (r19[0].charAt(r12) != '`') goto L162;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x006f, code lost:
        r5 = r5 + 1;
        r12 = r12 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0074, code lost:
        if (r4 == false) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0076, code lost:
        r10 = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0078, code lost:
        r10 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0079, code lost:
        r10 = r10 + r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x007a, code lost:
        if (r4 == false) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x007c, code lost:
        if (r6 <= 0) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x007e, code lost:
        r4 = r19[0].charAt(r6 - 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0087, code lost:
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x008a, code lost:
        if (r4 == ' ') goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x008c, code lost:
        if (r4 != '\n') goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x008f, code lost:
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0091, code lost:
        r4 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0092, code lost:
        r13 = substring(r19[0], 0, r6 - r4);
        r14 = substring(r19[0], r6 + 3, r5);
        r15 = r5 + 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00aa, code lost:
        if (r15 >= r19[0].length()) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00ac, code lost:
        r3 = r19[0].charAt(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00b3, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00b4, code lost:
        r11 = r19[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00b6, code lost:
        if (r3 == ' ') goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00b8, code lost:
        if (r3 != '\n') goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00bb, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00bd, code lost:
        r3 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00be, code lost:
        r3 = substring(r11, r15 + r3, r19[0].length());
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00cf, code lost:
        if (r13.length() == 0) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00d1, code lost:
        r13 = org.telegram.messenger.AndroidUtilities.concat(r13, "\n");
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00dc, code lost:
        r4 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00e1, code lost:
        if (r3.length() == 0) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00e3, code lost:
        r3 = org.telegram.messenger.AndroidUtilities.concat("\n", r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00f1, code lost:
        if (android.text.TextUtils.isEmpty(r14) != false) goto L155;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00f3, code lost:
        r19[0] = org.telegram.messenger.AndroidUtilities.concat(r13, r14, r3);
        r3 = new org.telegram.tgnet.TLRPC$TL_messageEntityPre();
        r3.offset = (r4 ^ 1) + r6;
        r3.length = ((r5 - r6) - 3) + (r4 ^ 1);
        r3.language = "";
        r1.add(r3);
        r10 = r10 - 6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x011e, code lost:
        r3 = r6 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0120, code lost:
        if (r3 == r5) goto L157;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0122, code lost:
        r19[0] = org.telegram.messenger.AndroidUtilities.concat(substring(r19[0], 0, r6), substring(r19[0], r3, r5), substring(r19[0], r5 + 1, r19[0].length()));
        r3 = new org.telegram.tgnet.TLRPC$TL_messageEntityCode();
        r3.offset = r6;
        r3.length = (r5 - r6) - 1;
        r1.add(r3);
        r10 = r10 - 2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ArrayList<TLRPC$MessageEntity> getEntities(CharSequence[] charSequenceArr, boolean z) {
        int i;
        int i2;
        ArrayList<TLRPC$MessageEntity> arrayList = null;
        if (charSequenceArr != null && charSequenceArr[0] != null) {
            int i3 = -1;
            boolean z2 = false;
            int i4 = 0;
            loop0: while (true) {
                i = -1;
                while (true) {
                    int indexOf = TextUtils.indexOf(charSequenceArr[0], !z2 ? "`" : "```", i4);
                    int i5 = 1;
                    if (indexOf == i3) {
                        break loop0;
                    } else if (i != i3) {
                        break;
                    } else {
                        z2 = charSequenceArr[0].length() - indexOf > 2 && charSequenceArr[0].charAt(indexOf + 1) == '`' && charSequenceArr[0].charAt(indexOf + 2) == '`';
                        if (z2) {
                            i5 = 3;
                        }
                        i = indexOf;
                        i4 = indexOf + i5;
                    }
                }
                i4 = i2;
                i3 = -1;
                z2 = false;
            }
            if (i != i3 && z2) {
                charSequenceArr[0] = AndroidUtilities.concat(substring(charSequenceArr[0], 0, i), substring(charSequenceArr[0], i + 2, charSequenceArr[0].length()));
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                TLRPC$MessageEntity tLRPC$TL_messageEntityCode = new TLRPC$TL_messageEntityCode();
                tLRPC$TL_messageEntityCode.offset = i;
                tLRPC$TL_messageEntityCode.length = 1;
                arrayList.add(tLRPC$TL_messageEntityCode);
            }
            if (charSequenceArr[0] instanceof Spanned) {
                Spanned spanned = (Spanned) charSequenceArr[0];
                TextStyleSpan[] textStyleSpanArr = (TextStyleSpan[]) spanned.getSpans(0, charSequenceArr[0].length(), TextStyleSpan.class);
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
                    for (int i6 = 0; i6 < uRLSpanUserMentionArr.length; i6++) {
                        TLRPC$TL_inputMessageEntityMentionName tLRPC$TL_inputMessageEntityMentionName = new TLRPC$TL_inputMessageEntityMentionName();
                        TLRPC$InputUser inputUser = getMessagesController().getInputUser(Utilities.parseLong(uRLSpanUserMentionArr[i6].getURL()).longValue());
                        tLRPC$TL_inputMessageEntityMentionName.user_id = inputUser;
                        if (inputUser != null) {
                            tLRPC$TL_inputMessageEntityMentionName.offset = spanned.getSpanStart(uRLSpanUserMentionArr[i6]);
                            int min = Math.min(spanned.getSpanEnd(uRLSpanUserMentionArr[i6]), charSequenceArr[0].length());
                            int i7 = tLRPC$TL_inputMessageEntityMentionName.offset;
                            int i8 = min - i7;
                            tLRPC$TL_inputMessageEntityMentionName.length = i8;
                            if (charSequenceArr[0].charAt((i7 + i8) - 1) == ' ') {
                                tLRPC$TL_inputMessageEntityMentionName.length--;
                            }
                            arrayList.add(tLRPC$TL_inputMessageEntityMentionName);
                        }
                    }
                }
                URLSpanReplacement[] uRLSpanReplacementArr = (URLSpanReplacement[]) spanned.getSpans(0, charSequenceArr[0].length(), URLSpanReplacement.class);
                if (uRLSpanReplacementArr != null && uRLSpanReplacementArr.length > 0) {
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                    }
                    for (int i9 = 0; i9 < uRLSpanReplacementArr.length; i9++) {
                        TLRPC$MessageEntity tLRPC$TL_messageEntityTextUrl = new TLRPC$TL_messageEntityTextUrl();
                        tLRPC$TL_messageEntityTextUrl.offset = spanned.getSpanStart(uRLSpanReplacementArr[i9]);
                        tLRPC$TL_messageEntityTextUrl.length = Math.min(spanned.getSpanEnd(uRLSpanReplacementArr[i9]), charSequenceArr[0].length()) - tLRPC$TL_messageEntityTextUrl.offset;
                        tLRPC$TL_messageEntityTextUrl.url = uRLSpanReplacementArr[i9].getURL();
                        arrayList.add(tLRPC$TL_messageEntityTextUrl);
                        TextStyleSpan.TextStyleRun textStyleRun = uRLSpanReplacementArr[i9].getTextStyleRun();
                        if (textStyleRun != null) {
                            int i10 = textStyleRun.flags;
                            int i11 = tLRPC$TL_messageEntityTextUrl.offset;
                            addStyle(i10, i11, tLRPC$TL_messageEntityTextUrl.length + i11, arrayList);
                        }
                    }
                }
                if (spanned instanceof Spannable) {
                    AndroidUtilities.addLinks((Spannable) spanned, 1);
                    URLSpan[] uRLSpanArr = (URLSpan[]) spanned.getSpans(0, charSequenceArr[0].length(), URLSpan.class);
                    if (uRLSpanArr != null && uRLSpanArr.length > 0) {
                        if (arrayList == null) {
                            arrayList = new ArrayList<>();
                        }
                        for (int i12 = 0; i12 < uRLSpanArr.length; i12++) {
                            if (!(uRLSpanArr[i12] instanceof URLSpanReplacement) && !(uRLSpanArr[i12] instanceof URLSpanUserMention)) {
                                TLRPC$MessageEntity tLRPC$TL_messageEntityUrl = new TLRPC$TL_messageEntityUrl();
                                tLRPC$TL_messageEntityUrl.offset = spanned.getSpanStart(uRLSpanArr[i12]);
                                tLRPC$TL_messageEntityUrl.length = Math.min(spanned.getSpanEnd(uRLSpanArr[i12]), charSequenceArr[0].length()) - tLRPC$TL_messageEntityUrl.offset;
                                tLRPC$TL_messageEntityUrl.url = uRLSpanArr[i12].getURL();
                                arrayList.add(tLRPC$TL_messageEntityUrl);
                            }
                        }
                    }
                }
            }
            CharSequence charSequence = charSequenceArr[0];
            if (arrayList == null) {
                arrayList = new ArrayList<>();
            }
            CharSequence parsePattern = parsePattern(parsePattern(parsePattern(charSequence, BOLD_PATTERN, arrayList, MediaDataController$$ExternalSyntheticLambda131.INSTANCE), ITALIC_PATTERN, arrayList, MediaDataController$$ExternalSyntheticLambda129.INSTANCE), SPOILER_PATTERN, arrayList, MediaDataController$$ExternalSyntheticLambda132.INSTANCE);
            if (z) {
                parsePattern = parsePattern(parsePattern, STRIKE_PATTERN, arrayList, MediaDataController$$ExternalSyntheticLambda130.INSTANCE);
            }
            charSequenceArr[0] = parsePattern;
        }
        return arrayList;
    }

    public static /* synthetic */ TLRPC$MessageEntity lambda$getEntities$139(Void r0) {
        return new TLRPC$TL_messageEntityBold();
    }

    public static /* synthetic */ TLRPC$MessageEntity lambda$getEntities$140(Void r0) {
        return new TLRPC$TL_messageEntityItalic();
    }

    public static /* synthetic */ TLRPC$MessageEntity lambda$getEntities$141(Void r0) {
        return new TLRPC$TL_messageEntitySpoiler();
    }

    public static /* synthetic */ TLRPC$MessageEntity lambda$getEntities$142(Void r0) {
        return new TLRPC$TL_messageEntityStrike();
    }

    private CharSequence parsePattern(CharSequence charSequence, Pattern pattern, List<TLRPC$MessageEntity> list, GenericProvider<Void, TLRPC$MessageEntity> genericProvider) {
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
                charSequence = ((Object) charSequence.subSequence(0, matcher.start() - i)) + group + ((Object) charSequence.subSequence(matcher.end() - i, charSequence.length()));
                TLRPC$MessageEntity provide = genericProvider.provide(null);
                provide.offset = matcher.start() - i;
                provide.length = group.length();
                list.add(provide);
            }
            i += (matcher.end() - matcher.start()) - group.length();
        }
        return charSequence;
    }

    public void loadDraftsIfNeed() {
        if (getUserConfig().draftsLoaded || this.loadingDrafts) {
            return;
        }
        this.loadingDrafts = true;
        getConnectionsManager().sendRequest(new TLRPC$TL_messages_getAllDrafts(), new MediaDataController$$ExternalSyntheticLambda139(this));
    }

    public /* synthetic */ void lambda$loadDraftsIfNeed$143() {
        this.loadingDrafts = false;
    }

    public /* synthetic */ void lambda$loadDraftsIfNeed$145(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error != null) {
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda9(this));
            return;
        }
        getMessagesController().processUpdates((TLRPC$Updates) tLObject, false);
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda5(this));
    }

    public /* synthetic */ void lambda$loadDraftsIfNeed$144() {
        this.loadingDrafts = false;
        UserConfig userConfig = getUserConfig();
        userConfig.draftsLoaded = true;
        userConfig.saveConfig(false);
    }

    public int getDraftFolderId(long j) {
        return this.draftsFolderIds.get(j, 0).intValue();
    }

    public void setDraftFolderId(long j, int i) {
        this.draftsFolderIds.put(j, Integer.valueOf(i));
    }

    public void clearDraftsFolderIds() {
        this.draftsFolderIds.clear();
    }

    public LongSparseArray<SparseArray<TLRPC$DraftMessage>> getDrafts() {
        return this.drafts;
    }

    public TLRPC$DraftMessage getDraft(long j, int i) {
        SparseArray<TLRPC$DraftMessage> sparseArray = this.drafts.get(j);
        if (sparseArray == null) {
            return null;
        }
        return sparseArray.get(i);
    }

    public TLRPC$Message getDraftMessage(long j, int i) {
        SparseArray<TLRPC$Message> sparseArray = this.draftMessages.get(j);
        if (sparseArray == null) {
            return null;
        }
        return sparseArray.get(i);
    }

    public void saveDraft(long j, int i, CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, TLRPC$Message tLRPC$Message, boolean z) {
        saveDraft(j, i, charSequence, arrayList, tLRPC$Message, z, false);
    }

    public void saveDraft(long j, int i, CharSequence charSequence, ArrayList<TLRPC$MessageEntity> arrayList, TLRPC$Message tLRPC$Message, boolean z, boolean z2) {
        TLRPC$DraftMessage tLRPC$DraftMessage;
        if (!TextUtils.isEmpty(charSequence) || tLRPC$Message != null) {
            tLRPC$DraftMessage = new TLRPC$TL_draftMessage();
        } else {
            tLRPC$DraftMessage = new TLRPC$TL_draftMessageEmpty();
        }
        TLRPC$DraftMessage tLRPC$DraftMessage2 = tLRPC$DraftMessage;
        tLRPC$DraftMessage2.date = (int) (System.currentTimeMillis() / 1000);
        tLRPC$DraftMessage2.message = charSequence == null ? "" : charSequence.toString();
        tLRPC$DraftMessage2.no_webpage = z;
        if (tLRPC$Message != null) {
            tLRPC$DraftMessage2.reply_to_msg_id = tLRPC$Message.id;
            tLRPC$DraftMessage2.flags |= 1;
        }
        if (arrayList != null && !arrayList.isEmpty()) {
            tLRPC$DraftMessage2.entities = arrayList;
            tLRPC$DraftMessage2.flags |= 8;
        }
        SparseArray<TLRPC$DraftMessage> sparseArray = this.drafts.get(j);
        TLRPC$DraftMessage tLRPC$DraftMessage3 = sparseArray == null ? null : sparseArray.get(i);
        if (!z2) {
            if (tLRPC$DraftMessage3 != null && tLRPC$DraftMessage3.message.equals(tLRPC$DraftMessage2.message) && tLRPC$DraftMessage3.reply_to_msg_id == tLRPC$DraftMessage2.reply_to_msg_id && tLRPC$DraftMessage3.no_webpage == tLRPC$DraftMessage2.no_webpage) {
                return;
            }
            if (tLRPC$DraftMessage3 == null && TextUtils.isEmpty(tLRPC$DraftMessage2.message) && tLRPC$DraftMessage2.reply_to_msg_id == 0) {
                return;
            }
        }
        saveDraft(j, i, tLRPC$DraftMessage2, tLRPC$Message, false);
        if (i == 0) {
            if (!DialogObject.isEncryptedDialog(j)) {
                TLRPC$TL_messages_saveDraft tLRPC$TL_messages_saveDraft = new TLRPC$TL_messages_saveDraft();
                TLRPC$InputPeer inputPeer = getMessagesController().getInputPeer(j);
                tLRPC$TL_messages_saveDraft.peer = inputPeer;
                if (inputPeer == null) {
                    return;
                }
                tLRPC$TL_messages_saveDraft.message = tLRPC$DraftMessage2.message;
                tLRPC$TL_messages_saveDraft.no_webpage = tLRPC$DraftMessage2.no_webpage;
                tLRPC$TL_messages_saveDraft.reply_to_msg_id = tLRPC$DraftMessage2.reply_to_msg_id;
                tLRPC$TL_messages_saveDraft.entities = tLRPC$DraftMessage2.entities;
                tLRPC$TL_messages_saveDraft.flags = tLRPC$DraftMessage2.flags;
                getConnectionsManager().sendRequest(tLRPC$TL_messages_saveDraft, MediaDataController$$ExternalSyntheticLambda171.INSTANCE);
            }
            getMessagesController().sortDialogs(null);
            getNotificationCenter().postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        }
    }

    public void saveDraft(long j, int i, TLRPC$DraftMessage tLRPC$DraftMessage, TLRPC$Message tLRPC$Message, boolean z) {
        TLRPC$Chat tLRPC$Chat;
        StringBuilder sb;
        String str;
        SharedPreferences.Editor edit = this.draftPreferences.edit();
        MessagesController messagesController = getMessagesController();
        if (tLRPC$DraftMessage == null || (tLRPC$DraftMessage instanceof TLRPC$TL_draftMessageEmpty)) {
            SparseArray<TLRPC$DraftMessage> sparseArray = this.drafts.get(j);
            if (sparseArray != null) {
                sparseArray.remove(i);
                if (sparseArray.size() == 0) {
                    this.drafts.remove(j);
                }
            }
            SparseArray<TLRPC$Message> sparseArray2 = this.draftMessages.get(j);
            if (sparseArray2 != null) {
                sparseArray2.remove(i);
                if (sparseArray2.size() == 0) {
                    this.draftMessages.remove(j);
                }
            }
            if (i == 0) {
                this.draftPreferences.edit().remove("" + j).remove("r_" + j).commit();
            } else {
                this.draftPreferences.edit().remove("t_" + j + "_" + i).remove("rt_" + j + "_" + i).commit();
            }
            messagesController.removeDraftDialogIfNeed(j);
        } else {
            SparseArray<TLRPC$DraftMessage> sparseArray3 = this.drafts.get(j);
            if (sparseArray3 == null) {
                sparseArray3 = new SparseArray<>();
                this.drafts.put(j, sparseArray3);
            }
            sparseArray3.put(i, tLRPC$DraftMessage);
            if (i == 0) {
                messagesController.putDraftDialogIfNeed(j, tLRPC$DraftMessage);
            }
            try {
                SerializedData serializedData = new SerializedData(tLRPC$DraftMessage.getObjectSize());
                tLRPC$DraftMessage.serializeToStream(serializedData);
                if (i == 0) {
                    str = "" + j;
                } else {
                    str = "t_" + j + "_" + i;
                }
                edit.putString(str, Utilities.bytesToHex(serializedData.toByteArray()));
                serializedData.cleanup();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        SparseArray<TLRPC$Message> sparseArray4 = this.draftMessages.get(j);
        if (tLRPC$Message == null) {
            if (sparseArray4 != null) {
                sparseArray4.remove(i);
                if (sparseArray4.size() == 0) {
                    this.draftMessages.remove(j);
                }
            }
            if (i == 0) {
                edit.remove("r_" + j);
            } else {
                edit.remove("rt_" + j + "_" + i);
            }
        } else {
            if (sparseArray4 == null) {
                sparseArray4 = new SparseArray<>();
                this.draftMessages.put(j, sparseArray4);
            }
            sparseArray4.put(i, tLRPC$Message);
            SerializedData serializedData2 = new SerializedData(tLRPC$Message.getObjectSize());
            tLRPC$Message.serializeToStream(serializedData2);
            if (i == 0) {
                sb = new StringBuilder();
                sb.append("r_");
                sb.append(j);
            } else {
                sb = new StringBuilder();
                sb.append("rt_");
                sb.append(j);
                sb.append("_");
                sb.append(i);
            }
            edit.putString(sb.toString(), Utilities.bytesToHex(serializedData2.toByteArray()));
            serializedData2.cleanup();
        }
        edit.commit();
        if (!z || i != 0) {
            return;
        }
        if (tLRPC$DraftMessage != null && tLRPC$DraftMessage.reply_to_msg_id != 0 && tLRPC$Message == null) {
            TLRPC$Chat tLRPC$Chat2 = null;
            if (DialogObject.isUserDialog(j)) {
                tLRPC$Chat2 = getMessagesController().getUser(Long.valueOf(j));
                tLRPC$Chat = tLRPC$Chat2;
            } else {
                tLRPC$Chat = getMessagesController().getChat(Long.valueOf(-j));
            }
            if (tLRPC$Chat2 != null || tLRPC$Chat != null) {
                getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda22(this, tLRPC$DraftMessage.reply_to_msg_id, j, ChatObject.isChannel(tLRPC$Chat) ? tLRPC$Chat.id : 0L, i));
            }
        }
        getNotificationCenter().postNotificationName(NotificationCenter.newDraftReceived, Long.valueOf(j));
    }

    public /* synthetic */ void lambda$saveDraft$149(int i, long j, long j2, int i2) {
        NativeByteBuffer byteBufferValue;
        TLRPC$Message tLRPC$Message = null;
        try {
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT data FROM messages_v2 WHERE mid = %d and uid = %d", Integer.valueOf(i), Long.valueOf(j)), new Object[0]);
            if (queryFinalized.next() && (byteBufferValue = queryFinalized.byteBufferValue(0)) != null) {
                tLRPC$Message = TLRPC$Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                tLRPC$Message.readAttachPath(byteBufferValue, getUserConfig().clientUserId);
                byteBufferValue.reuse();
            }
            queryFinalized.dispose();
            if (tLRPC$Message != null) {
                saveDraftReplyMessage(j, i2, tLRPC$Message);
            } else if (j2 != 0) {
                TLRPC$TL_channels_getMessages tLRPC$TL_channels_getMessages = new TLRPC$TL_channels_getMessages();
                tLRPC$TL_channels_getMessages.channel = getMessagesController().getInputChannel(j2);
                tLRPC$TL_channels_getMessages.id.add(Integer.valueOf(i));
                getConnectionsManager().sendRequest(tLRPC$TL_channels_getMessages, new MediaDataController$$ExternalSyntheticLambda149(this, j, i2));
            } else {
                TLRPC$TL_messages_getMessages tLRPC$TL_messages_getMessages = new TLRPC$TL_messages_getMessages();
                tLRPC$TL_messages_getMessages.id.add(Integer.valueOf(i));
                getConnectionsManager().sendRequest(tLRPC$TL_messages_getMessages, new MediaDataController$$ExternalSyntheticLambda150(this, j, i2));
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$saveDraft$147(long j, int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            if (tLRPC$messages_Messages.messages.isEmpty()) {
                return;
            }
            saveDraftReplyMessage(j, i, tLRPC$messages_Messages.messages.get(0));
        }
    }

    public /* synthetic */ void lambda$saveDraft$148(long j, int i, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            TLRPC$messages_Messages tLRPC$messages_Messages = (TLRPC$messages_Messages) tLObject;
            if (tLRPC$messages_Messages.messages.isEmpty()) {
                return;
            }
            saveDraftReplyMessage(j, i, tLRPC$messages_Messages.messages.get(0));
        }
    }

    private void saveDraftReplyMessage(long j, int i, TLRPC$Message tLRPC$Message) {
        if (tLRPC$Message == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda39(this, j, i, tLRPC$Message));
    }

    public /* synthetic */ void lambda$saveDraftReplyMessage$150(long j, int i, TLRPC$Message tLRPC$Message) {
        String str;
        SparseArray<TLRPC$DraftMessage> sparseArray = this.drafts.get(j);
        TLRPC$DraftMessage tLRPC$DraftMessage = sparseArray != null ? sparseArray.get(i) : null;
        if (tLRPC$DraftMessage == null || tLRPC$DraftMessage.reply_to_msg_id != tLRPC$Message.id) {
            return;
        }
        SparseArray<TLRPC$Message> sparseArray2 = this.draftMessages.get(j);
        if (sparseArray2 == null) {
            sparseArray2 = new SparseArray<>();
            this.draftMessages.put(j, sparseArray2);
        }
        sparseArray2.put(i, tLRPC$Message);
        SerializedData serializedData = new SerializedData(tLRPC$Message.getObjectSize());
        tLRPC$Message.serializeToStream(serializedData);
        SharedPreferences.Editor edit = this.draftPreferences.edit();
        if (i == 0) {
            str = "r_" + j;
        } else {
            str = "rt_" + j + "_" + i;
        }
        edit.putString(str, Utilities.bytesToHex(serializedData.toByteArray())).commit();
        getNotificationCenter().postNotificationName(NotificationCenter.newDraftReceived, Long.valueOf(j));
        serializedData.cleanup();
    }

    public void clearAllDrafts(boolean z) {
        this.drafts.clear();
        this.draftMessages.clear();
        this.draftsFolderIds.clear();
        this.draftPreferences.edit().clear().commit();
        if (z) {
            getMessagesController().sortDialogs(null);
            getNotificationCenter().postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        }
    }

    public void cleanDraft(long j, int i, boolean z) {
        SparseArray<TLRPC$DraftMessage> sparseArray = this.drafts.get(j);
        TLRPC$DraftMessage tLRPC$DraftMessage = sparseArray != null ? sparseArray.get(i) : null;
        if (tLRPC$DraftMessage == null) {
            return;
        }
        if (!z) {
            SparseArray<TLRPC$DraftMessage> sparseArray2 = this.drafts.get(j);
            if (sparseArray2 != null) {
                sparseArray2.remove(i);
                if (sparseArray2.size() == 0) {
                    this.drafts.remove(j);
                }
            }
            SparseArray<TLRPC$Message> sparseArray3 = this.draftMessages.get(j);
            if (sparseArray3 != null) {
                sparseArray3.remove(i);
                if (sparseArray3.size() == 0) {
                    this.draftMessages.remove(j);
                }
            }
            if (i == 0) {
                this.draftPreferences.edit().remove("" + j).remove("r_" + j).commit();
                getMessagesController().sortDialogs(null);
                getNotificationCenter().postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                return;
            }
            this.draftPreferences.edit().remove("t_" + j + "_" + i).remove("rt_" + j + "_" + i).commit();
        } else if (tLRPC$DraftMessage.reply_to_msg_id == 0) {
        } else {
            tLRPC$DraftMessage.reply_to_msg_id = 0;
            tLRPC$DraftMessage.flags &= -2;
            saveDraft(j, i, tLRPC$DraftMessage.message, tLRPC$DraftMessage.entities, null, tLRPC$DraftMessage.no_webpage, true);
        }
    }

    public void beginTransaction() {
        this.inTransaction = true;
    }

    public void endTransaction() {
        this.inTransaction = false;
    }

    public void clearBotKeyboard(long j, ArrayList<Integer> arrayList) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda70(this, arrayList, j));
    }

    public /* synthetic */ void lambda$clearBotKeyboard$151(ArrayList arrayList, long j) {
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                long j2 = this.botKeyboardsByMids.get(((Integer) arrayList.get(i)).intValue());
                if (j2 != 0) {
                    this.botKeyboards.remove(j2);
                    this.botKeyboardsByMids.delete(((Integer) arrayList.get(i)).intValue());
                    getNotificationCenter().postNotificationName(NotificationCenter.botKeyboardDidLoad, null, Long.valueOf(j2));
                }
            }
            return;
        }
        this.botKeyboards.remove(j);
        getNotificationCenter().postNotificationName(NotificationCenter.botKeyboardDidLoad, null, Long.valueOf(j));
    }

    public void loadBotKeyboard(long j) {
        TLRPC$Message tLRPC$Message = this.botKeyboards.get(j);
        if (tLRPC$Message != null) {
            getNotificationCenter().postNotificationName(NotificationCenter.botKeyboardDidLoad, tLRPC$Message, Long.valueOf(j));
        } else {
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda32(this, j));
        }
    }

    public /* synthetic */ void lambda$loadBotKeyboard$153(long j) {
        NativeByteBuffer byteBufferValue;
        TLRPC$Message tLRPC$Message = null;
        try {
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT info FROM bot_keyboard WHERE uid = %d", Long.valueOf(j)), new Object[0]);
            if (queryFinalized.next() && !queryFinalized.isNull(0) && (byteBufferValue = queryFinalized.byteBufferValue(0)) != null) {
                tLRPC$Message = TLRPC$Message.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
                byteBufferValue.reuse();
            }
            queryFinalized.dispose();
            if (tLRPC$Message == null) {
                return;
            }
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda89(this, tLRPC$Message, j));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$loadBotKeyboard$152(TLRPC$Message tLRPC$Message, long j) {
        getNotificationCenter().postNotificationName(NotificationCenter.botKeyboardDidLoad, tLRPC$Message, Long.valueOf(j));
    }

    private TLRPC$BotInfo loadBotInfoInternal(long j, long j2) throws SQLiteException {
        TLRPC$BotInfo tLRPC$BotInfo;
        NativeByteBuffer byteBufferValue;
        SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT info FROM bot_info_v2 WHERE uid = %d AND dialogId = %d", Long.valueOf(j), Long.valueOf(j2)), new Object[0]);
        if (!queryFinalized.next() || queryFinalized.isNull(0) || (byteBufferValue = queryFinalized.byteBufferValue(0)) == null) {
            tLRPC$BotInfo = null;
        } else {
            tLRPC$BotInfo = TLRPC$BotInfo.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), false);
            byteBufferValue.reuse();
        }
        queryFinalized.dispose();
        return tLRPC$BotInfo;
    }

    public void loadBotInfo(long j, long j2, boolean z, int i) {
        if (z) {
            HashMap<String, TLRPC$BotInfo> hashMap = this.botInfos;
            TLRPC$BotInfo tLRPC$BotInfo = hashMap.get(j + "_" + j2);
            if (tLRPC$BotInfo != null) {
                getNotificationCenter().postNotificationName(NotificationCenter.botInfoDidLoad, tLRPC$BotInfo, Integer.valueOf(i));
                return;
            }
        }
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda40(this, j, j2, i));
    }

    public /* synthetic */ void lambda$loadBotInfo$155(long j, long j2, int i) {
        try {
            TLRPC$BotInfo loadBotInfoInternal = loadBotInfoInternal(j, j2);
            if (loadBotInfoInternal == null) {
                return;
            }
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda85(this, loadBotInfoInternal, i));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$loadBotInfo$154(TLRPC$BotInfo tLRPC$BotInfo, int i) {
        getNotificationCenter().postNotificationName(NotificationCenter.botInfoDidLoad, tLRPC$BotInfo, Integer.valueOf(i));
    }

    public void putBotKeyboard(long j, TLRPC$Message tLRPC$Message) {
        if (tLRPC$Message == null) {
            return;
        }
        try {
            int i = 0;
            SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized(String.format(Locale.US, "SELECT mid FROM bot_keyboard WHERE uid = %d", Long.valueOf(j)), new Object[0]);
            if (queryFinalized.next()) {
                i = queryFinalized.intValue(0);
            }
            queryFinalized.dispose();
            if (i >= tLRPC$Message.id) {
                return;
            }
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO bot_keyboard VALUES(?, ?, ?)");
            executeFast.requery();
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tLRPC$Message.getObjectSize());
            tLRPC$Message.serializeToStream(nativeByteBuffer);
            executeFast.bindLong(1, j);
            executeFast.bindInteger(2, tLRPC$Message.id);
            executeFast.bindByteBuffer(3, nativeByteBuffer);
            executeFast.step();
            nativeByteBuffer.reuse();
            executeFast.dispose();
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda46(this, j, tLRPC$Message));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$putBotKeyboard$156(long j, TLRPC$Message tLRPC$Message) {
        TLRPC$Message tLRPC$Message2 = this.botKeyboards.get(j);
        this.botKeyboards.put(j, tLRPC$Message);
        if (MessageObject.getChannelId(tLRPC$Message) == 0) {
            if (tLRPC$Message2 != null) {
                this.botKeyboardsByMids.delete(tLRPC$Message2.id);
            }
            this.botKeyboardsByMids.put(tLRPC$Message.id, j);
        }
        getNotificationCenter().postNotificationName(NotificationCenter.botKeyboardDidLoad, tLRPC$Message, Long.valueOf(j));
    }

    public void putBotInfo(long j, TLRPC$BotInfo tLRPC$BotInfo) {
        if (tLRPC$BotInfo == null) {
            return;
        }
        HashMap<String, TLRPC$BotInfo> hashMap = this.botInfos;
        hashMap.put(tLRPC$BotInfo.user_id + "_" + j, tLRPC$BotInfo);
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda86(this, tLRPC$BotInfo, j));
    }

    public /* synthetic */ void lambda$putBotInfo$157(TLRPC$BotInfo tLRPC$BotInfo, long j) {
        try {
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO bot_info_v2 VALUES(?, ?, ?)");
            executeFast.requery();
            NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tLRPC$BotInfo.getObjectSize());
            tLRPC$BotInfo.serializeToStream(nativeByteBuffer);
            executeFast.bindLong(1, tLRPC$BotInfo.user_id);
            executeFast.bindLong(2, j);
            executeFast.bindByteBuffer(3, nativeByteBuffer);
            executeFast.step();
            nativeByteBuffer.reuse();
            executeFast.dispose();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void updateBotInfo(long j, TLRPC$TL_updateBotCommands tLRPC$TL_updateBotCommands) {
        HashMap<String, TLRPC$BotInfo> hashMap = this.botInfos;
        TLRPC$BotInfo tLRPC$BotInfo = hashMap.get(tLRPC$TL_updateBotCommands.bot_id + "_" + j);
        if (tLRPC$BotInfo != null) {
            tLRPC$BotInfo.commands = tLRPC$TL_updateBotCommands.commands;
            getNotificationCenter().postNotificationName(NotificationCenter.botInfoDidLoad, tLRPC$BotInfo, 0);
        }
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda107(this, tLRPC$TL_updateBotCommands, j));
    }

    public /* synthetic */ void lambda$updateBotInfo$158(TLRPC$TL_updateBotCommands tLRPC$TL_updateBotCommands, long j) {
        try {
            TLRPC$BotInfo loadBotInfoInternal = loadBotInfoInternal(tLRPC$TL_updateBotCommands.bot_id, j);
            if (loadBotInfoInternal != null) {
                loadBotInfoInternal.commands = tLRPC$TL_updateBotCommands.commands;
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

    public HashMap<String, TLRPC$TL_availableReaction> getReactionsMap() {
        return this.reactionsMap;
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
        if (string != null && getReactionsMap().get(string) != null) {
            this.doubleTapReaction = string;
            return string;
        }
        return getReactionsList().get(0).reaction;
    }

    public void setDoubleTapReaction(String str) {
        MessagesController.getEmojiSettings(this.currentAccount).edit().putString("reaction_on_double_tap", str).apply();
        this.doubleTapReaction = str;
    }

    public List<TLRPC$TL_availableReaction> getEnabledReactionsList() {
        return this.enabledReactionsList;
    }

    public void uploadRingtone(String str) {
        if (this.ringtoneUploaderHashMap.containsKey(str)) {
            return;
        }
        this.ringtoneUploaderHashMap.put(str, new RingtoneUploader(str, this.currentAccount));
        this.ringtoneDataStore.addUploadingTone(str);
    }

    public void onRingtoneUploaded(String str, TLRPC$Document tLRPC$Document, boolean z) {
        this.ringtoneUploaderHashMap.remove(str);
        this.ringtoneDataStore.onRingtoneUploaded(str, tLRPC$Document, z);
    }

    public void checkRingtones() {
        this.ringtoneDataStore.lambda$new$0();
    }

    public boolean saveToRingtones(TLRPC$Document tLRPC$Document) {
        if (tLRPC$Document == null) {
            return false;
        }
        if (this.ringtoneDataStore.contains(tLRPC$Document.id)) {
            return true;
        }
        if (tLRPC$Document.size > MessagesController.getInstance(this.currentAccount).ringtoneSizeMax) {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.showBulletin, 4, LocaleController.formatString("TooLargeError", 2131628670, new Object[0]), LocaleController.formatString("ErrorRingtoneSizeTooBig", 2131625660, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).ringtoneSizeMax / 1024)));
            return false;
        }
        for (int i = 0; i < tLRPC$Document.attributes.size(); i++) {
            TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i);
            if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) && tLRPC$DocumentAttribute.duration > MessagesController.getInstance(this.currentAccount).ringtoneDurationMax) {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.showBulletin, 4, LocaleController.formatString("TooLongError", 2131628671, new Object[0]), LocaleController.formatString("ErrorRingtoneDurationTooLong", 2131625658, Integer.valueOf(MessagesController.getInstance(UserConfig.selectedAccount).ringtoneDurationMax)));
                return false;
            }
        }
        TLRPC$TL_account_saveRingtone tLRPC$TL_account_saveRingtone = new TLRPC$TL_account_saveRingtone();
        TLRPC$TL_inputDocument tLRPC$TL_inputDocument = new TLRPC$TL_inputDocument();
        tLRPC$TL_account_saveRingtone.id = tLRPC$TL_inputDocument;
        tLRPC$TL_inputDocument.id = tLRPC$Document.id;
        tLRPC$TL_inputDocument.file_reference = tLRPC$Document.file_reference;
        tLRPC$TL_inputDocument.access_hash = tLRPC$Document.access_hash;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_saveRingtone, new MediaDataController$$ExternalSyntheticLambda165(this, tLRPC$Document));
        return true;
    }

    public /* synthetic */ void lambda$saveToRingtones$160(TLRPC$Document tLRPC$Document, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda83(this, tLObject, tLRPC$Document));
    }

    public /* synthetic */ void lambda$saveToRingtones$159(TLObject tLObject, TLRPC$Document tLRPC$Document) {
        if (tLObject != null) {
            if (tLObject instanceof TLRPC$TL_account_savedRingtoneConverted) {
                this.ringtoneDataStore.addTone(((TLRPC$TL_account_savedRingtoneConverted) tLObject).document);
            } else {
                this.ringtoneDataStore.addTone(tLRPC$Document);
            }
        }
    }

    public void preloadPremiumPreviewStickers() {
        if (this.previewStickersLoading || !this.premiumPreviewStickers.isEmpty()) {
            int i = 0;
            while (i < Math.min(this.premiumPreviewStickers.size(), 3)) {
                ArrayList<TLRPC$Document> arrayList = this.premiumPreviewStickers;
                TLRPC$Document tLRPC$Document = arrayList.get(i == 2 ? arrayList.size() - 1 : i);
                if (MessageObject.isPremiumSticker(tLRPC$Document)) {
                    ImageReceiver imageReceiver = new ImageReceiver();
                    imageReceiver.setImage(ImageLocation.getForDocument(tLRPC$Document), null, null, "webp", null, 1);
                    ImageLoader.getInstance().loadImageForImageReceiver(imageReceiver);
                    ImageReceiver imageReceiver2 = new ImageReceiver();
                    imageReceiver2.setImage(ImageLocation.getForDocument(MessageObject.getPremiumStickerAnimation(tLRPC$Document), tLRPC$Document), (String) null, (ImageLocation) null, (String) null, "tgs", (Object) null, 1);
                    ImageLoader.getInstance().loadImageForImageReceiver(imageReceiver2);
                }
                i++;
            }
            return;
        }
        TLRPC$TL_messages_getStickers tLRPC$TL_messages_getStickers = new TLRPC$TL_messages_getStickers();
        tLRPC$TL_messages_getStickers.emoticon = Emoji.fixEmoji("⭐") + Emoji.fixEmoji("⭐");
        tLRPC$TL_messages_getStickers.hash = 0L;
        this.previewStickersLoading = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getStickers, new MediaDataController$$ExternalSyntheticLambda140(this));
    }

    public /* synthetic */ void lambda$preloadPremiumPreviewStickers$162(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda98(this, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$preloadPremiumPreviewStickers$161(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        if (tLRPC$TL_error != null) {
            return;
        }
        this.previewStickersLoading = false;
        this.premiumPreviewStickers.clear();
        this.premiumPreviewStickers.addAll(((TLRPC$TL_messages_stickers) tLObject).stickers);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.premiumStickersPreviewLoaded, new Object[0]);
    }

    public void chekAllMedia(boolean z) {
        if (z) {
            this.reactionsUpdateDate = 0;
            this.loadFeaturedDate = 0;
        }
        loadRecents(2, false, true, false);
        loadRecents(3, false, true, false);
        checkFeaturedStickers();
        checkReactions();
        checkMenuBots();
        checkPremiumPromo();
    }

    public void fetchNewEmojiKeywords(String[] strArr) {
        if (strArr == null) {
            return;
        }
        for (String str : strArr) {
            if (TextUtils.isEmpty(str) || this.currentFetchingEmoji.get(str) != null) {
                return;
            }
            this.currentFetchingEmoji.put(str, Boolean.TRUE);
            getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda58(this, str));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$fetchNewEmojiKeywords$168(String str) {
        int i;
        TLRPC$TL_messages_getEmojiKeywordsDifference tLRPC$TL_messages_getEmojiKeywordsDifference;
        Exception e;
        String str2 = null;
        long j = 0;
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
                    if (i != -1) {
                    }
                    getConnectionsManager().sendRequest(tLRPC$TL_messages_getEmojiKeywordsDifference, new MediaDataController$$ExternalSyntheticLambda146(this, i, str2, str));
                }
            } else {
                i = -1;
            }
            queryFinalized.dispose();
        } catch (Exception e3) {
            e = e3;
            i = -1;
        }
        if (BuildVars.DEBUG_VERSION && Math.abs(System.currentTimeMillis() - j) < 3600000) {
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda59(this, str));
            return;
        }
        if (i != -1) {
            TLRPC$TL_messages_getEmojiKeywords tLRPC$TL_messages_getEmojiKeywords = new TLRPC$TL_messages_getEmojiKeywords();
            tLRPC$TL_messages_getEmojiKeywords.lang_code = str;
            tLRPC$TL_messages_getEmojiKeywordsDifference = tLRPC$TL_messages_getEmojiKeywords;
        } else {
            TLRPC$TL_messages_getEmojiKeywordsDifference tLRPC$TL_messages_getEmojiKeywordsDifference2 = new TLRPC$TL_messages_getEmojiKeywordsDifference();
            tLRPC$TL_messages_getEmojiKeywordsDifference2.lang_code = str;
            tLRPC$TL_messages_getEmojiKeywordsDifference2.from_version = i;
            tLRPC$TL_messages_getEmojiKeywordsDifference = tLRPC$TL_messages_getEmojiKeywordsDifference2;
        }
        getConnectionsManager().sendRequest(tLRPC$TL_messages_getEmojiKeywordsDifference, new MediaDataController$$ExternalSyntheticLambda146(this, i, str2, str));
    }

    public /* synthetic */ void lambda$fetchNewEmojiKeywords$163(String str) {
        this.currentFetchingEmoji.remove(str);
    }

    public /* synthetic */ void lambda$fetchNewEmojiKeywords$167(int i, String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            TLRPC$TL_emojiKeywordsDifference tLRPC$TL_emojiKeywordsDifference = (TLRPC$TL_emojiKeywordsDifference) tLObject;
            if (i != -1 && !tLRPC$TL_emojiKeywordsDifference.lang_code.equals(str)) {
                getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda54(this, str2));
                return;
            } else {
                putEmojiKeywords(str2, tLRPC$TL_emojiKeywordsDifference);
                return;
            }
        }
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda57(this, str2));
    }

    public /* synthetic */ void lambda$fetchNewEmojiKeywords$165(String str) {
        try {
            SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("DELETE FROM emoji_keywords_info_v2 WHERE lang = ?");
            executeFast.bindString(1, str);
            executeFast.step();
            executeFast.dispose();
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda55(this, str));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$fetchNewEmojiKeywords$164(String str) {
        this.currentFetchingEmoji.remove(str);
        fetchNewEmojiKeywords(new String[]{str});
    }

    public /* synthetic */ void lambda$fetchNewEmojiKeywords$166(String str) {
        this.currentFetchingEmoji.remove(str);
    }

    private void putEmojiKeywords(String str, TLRPC$TL_emojiKeywordsDifference tLRPC$TL_emojiKeywordsDifference) {
        if (tLRPC$TL_emojiKeywordsDifference == null) {
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda97(this, tLRPC$TL_emojiKeywordsDifference, str));
    }

    public /* synthetic */ void lambda$putEmojiKeywords$170(TLRPC$TL_emojiKeywordsDifference tLRPC$TL_emojiKeywordsDifference, String str) {
        try {
            if (!tLRPC$TL_emojiKeywordsDifference.keywords.isEmpty()) {
                SQLitePreparedStatement executeFast = getMessagesStorage().getDatabase().executeFast("REPLACE INTO emoji_keywords_v2 VALUES(?, ?, ?)");
                SQLitePreparedStatement executeFast2 = getMessagesStorage().getDatabase().executeFast("DELETE FROM emoji_keywords_v2 WHERE lang = ? AND keyword = ? AND emoji = ?");
                getMessagesStorage().getDatabase().beginTransaction();
                int size = tLRPC$TL_emojiKeywordsDifference.keywords.size();
                for (int i = 0; i < size; i++) {
                    TLRPC$EmojiKeyword tLRPC$EmojiKeyword = tLRPC$TL_emojiKeywordsDifference.keywords.get(i);
                    if (tLRPC$EmojiKeyword instanceof TLRPC$TL_emojiKeyword) {
                        TLRPC$TL_emojiKeyword tLRPC$TL_emojiKeyword = (TLRPC$TL_emojiKeyword) tLRPC$EmojiKeyword;
                        String lowerCase = tLRPC$TL_emojiKeyword.keyword.toLowerCase();
                        int size2 = tLRPC$TL_emojiKeyword.emoticons.size();
                        for (int i2 = 0; i2 < size2; i2++) {
                            executeFast.requery();
                            executeFast.bindString(1, tLRPC$TL_emojiKeywordsDifference.lang_code);
                            executeFast.bindString(2, lowerCase);
                            executeFast.bindString(3, tLRPC$TL_emojiKeyword.emoticons.get(i2));
                            executeFast.step();
                        }
                    } else if (tLRPC$EmojiKeyword instanceof TLRPC$TL_emojiKeywordDeleted) {
                        TLRPC$TL_emojiKeywordDeleted tLRPC$TL_emojiKeywordDeleted = (TLRPC$TL_emojiKeywordDeleted) tLRPC$EmojiKeyword;
                        String lowerCase2 = tLRPC$TL_emojiKeywordDeleted.keyword.toLowerCase();
                        int size3 = tLRPC$TL_emojiKeywordDeleted.emoticons.size();
                        for (int i3 = 0; i3 < size3; i3++) {
                            executeFast2.requery();
                            executeFast2.bindString(1, tLRPC$TL_emojiKeywordsDifference.lang_code);
                            executeFast2.bindString(2, lowerCase2);
                            executeFast2.bindString(3, tLRPC$TL_emojiKeywordDeleted.emoticons.get(i3));
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
            executeFast3.bindString(2, tLRPC$TL_emojiKeywordsDifference.lang_code);
            executeFast3.bindInteger(3, tLRPC$TL_emojiKeywordsDifference.version);
            executeFast3.bindLong(4, System.currentTimeMillis());
            executeFast3.step();
            executeFast3.dispose();
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda53(this, str));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$putEmojiKeywords$169(String str) {
        this.currentFetchingEmoji.remove(str);
        getNotificationCenter().postNotificationName(NotificationCenter.newEmojiSuggestionsAvailable, str);
    }

    public void getEmojiSuggestions(String[] strArr, String str, boolean z, KeywordResultCallback keywordResultCallback) {
        getEmojiSuggestions(strArr, str, z, keywordResultCallback, null);
    }

    public void getEmojiSuggestions(String[] strArr, String str, boolean z, KeywordResultCallback keywordResultCallback, CountDownLatch countDownLatch) {
        if (keywordResultCallback == null) {
            return;
        }
        if (TextUtils.isEmpty(str) || strArr == null) {
            keywordResultCallback.run(new ArrayList<>(), null);
            return;
        }
        getMessagesStorage().getStorageQueue().postRunnable(new MediaDataController$$ExternalSyntheticLambda118(this, strArr, keywordResultCallback, str, z, new ArrayList(Emoji.recentEmoji), countDownLatch));
        if (countDownLatch == null) {
            return;
        }
        try {
            countDownLatch.await();
        } catch (Throwable unused) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0122  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$getEmojiSuggestions$174(String[] strArr, KeywordResultCallback keywordResultCallback, String str, boolean z, ArrayList arrayList, CountDownLatch countDownLatch) {
        Exception e;
        String str2;
        SQLiteCursor sQLiteCursor;
        ArrayList<KeywordResult> arrayList2 = new ArrayList<>();
        HashMap hashMap = new HashMap();
        String str3 = null;
        boolean z2 = false;
        for (int i = 0; i < strArr.length; i++) {
            try {
                SQLiteCursor queryFinalized = getMessagesStorage().getDatabase().queryFinalized("SELECT alias FROM emoji_keywords_info_v2 WHERE lang = ?", strArr[i]);
                if (queryFinalized.next()) {
                    str3 = queryFinalized.stringValue(0);
                }
                queryFinalized.dispose();
                if (str3 != null) {
                    z2 = true;
                }
            } catch (Exception e2) {
                e = e2;
            }
        }
        try {
        } catch (Exception e3) {
            e = e3;
            FileLog.e(e);
            Collections.sort(arrayList2, new MediaDataController$$ExternalSyntheticLambda123(arrayList));
            if (countDownLatch == null) {
            }
        }
        if (!z2) {
            AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda119(this, strArr, keywordResultCallback, arrayList2));
            return;
        }
        String lowerCase = str.toLowerCase();
        for (int i2 = 0; i2 < 2; i2++) {
            if (i2 == 1) {
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
            if (z) {
                sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT emoji, keyword FROM emoji_keywords_v2 WHERE keyword = ?", lowerCase);
            } else if (str2 != null) {
                sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT emoji, keyword FROM emoji_keywords_v2 WHERE keyword >= ? AND keyword < ?", lowerCase, str2);
            } else {
                lowerCase = lowerCase + "%";
                sQLiteCursor = getMessagesStorage().getDatabase().queryFinalized("SELECT emoji, keyword FROM emoji_keywords_v2 WHERE keyword LIKE ?", lowerCase);
            }
            while (sQLiteCursor.next()) {
                String replace = sQLiteCursor.stringValue(0).replace("️", "");
                if (hashMap.get(replace) == null) {
                    hashMap.put(replace, Boolean.TRUE);
                    KeywordResult keywordResult = new KeywordResult();
                    keywordResult.emoji = replace;
                    keywordResult.keyword = sQLiteCursor.stringValue(1);
                    arrayList2.add(keywordResult);
                }
            }
            sQLiteCursor.dispose();
        }
        Collections.sort(arrayList2, new MediaDataController$$ExternalSyntheticLambda123(arrayList));
        if (countDownLatch == null) {
            keywordResultCallback.run(arrayList2, str3);
            countDownLatch.countDown();
            return;
        }
        AndroidUtilities.runOnUIThread(new MediaDataController$$ExternalSyntheticLambda1(keywordResultCallback, arrayList2, str3));
    }

    public /* synthetic */ void lambda$getEmojiSuggestions$171(String[] strArr, KeywordResultCallback keywordResultCallback, ArrayList arrayList) {
        for (String str : strArr) {
            if (this.currentFetchingEmoji.get(str) != null) {
                return;
            }
        }
        keywordResultCallback.run(arrayList, null);
    }

    public static /* synthetic */ int lambda$getEmojiSuggestions$172(ArrayList arrayList, KeywordResult keywordResult, KeywordResult keywordResult2) {
        int indexOf = arrayList.indexOf(keywordResult.emoji);
        int i = Integer.MAX_VALUE;
        if (indexOf < 0) {
            indexOf = Integer.MAX_VALUE;
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

    public void loadEmojiThemes() {
        Context context = ApplicationLoader.applicationContext;
        SharedPreferences sharedPreferences = context.getSharedPreferences("emojithemes_config_" + this.currentAccount, 0);
        int i = sharedPreferences.getInt("count", 0);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ChatThemeBottomSheet.ChatThemeItem(EmojiThemes.createHomePreviewTheme()));
        for (int i2 = 0; i2 < i; i2++) {
            SerializedData serializedData = new SerializedData(Utilities.hexToBytes(sharedPreferences.getString("theme_" + i2, "")));
            try {
                EmojiThemes createPreviewFullTheme = EmojiThemes.createPreviewFullTheme(TLRPC$Theme.TLdeserialize(serializedData, serializedData.readInt32(true), true));
                if (createPreviewFullTheme.items.size() >= 4) {
                    arrayList.add(new ChatThemeBottomSheet.ChatThemeItem(createPreviewFullTheme));
                }
                ChatThemeController.chatThemeQueue.postRunnable(new AnonymousClass2(arrayList));
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
    }

    /* renamed from: org.telegram.messenger.MediaDataController$2 */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements Runnable {
        final /* synthetic */ ArrayList val$previewItems;

        AnonymousClass2(ArrayList arrayList) {
            MediaDataController.this = r1;
            this.val$previewItems = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            for (int i = 0; i < this.val$previewItems.size(); i++) {
                ((ChatThemeBottomSheet.ChatThemeItem) this.val$previewItems.get(i)).chatTheme.loadPreviewColors(0);
            }
            AndroidUtilities.runOnUIThread(new MediaDataController$2$$ExternalSyntheticLambda0(this, this.val$previewItems));
        }

        public /* synthetic */ void lambda$run$0(ArrayList arrayList) {
            MediaDataController.this.defaultEmojiThemes.clear();
            MediaDataController.this.defaultEmojiThemes.addAll(arrayList);
        }
    }

    public void generateEmojiPreviewThemes(ArrayList<TLRPC$TL_theme> arrayList, int i) {
        Context context = ApplicationLoader.applicationContext;
        SharedPreferences.Editor edit = context.getSharedPreferences("emojithemes_config_" + i, 0).edit();
        edit.putInt("count", arrayList.size());
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            TLRPC$TL_theme tLRPC$TL_theme = arrayList.get(i2);
            SerializedData serializedData = new SerializedData(tLRPC$TL_theme.getObjectSize());
            tLRPC$TL_theme.serializeToStream(serializedData);
            edit.putString("theme_" + i2, Utilities.bytesToHex(serializedData.toByteArray()));
        }
        edit.apply();
        if (!arrayList.isEmpty()) {
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(new ChatThemeBottomSheet.ChatThemeItem(EmojiThemes.createHomePreviewTheme()));
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                EmojiThemes createPreviewFullTheme = EmojiThemes.createPreviewFullTheme(arrayList.get(i3));
                ChatThemeBottomSheet.ChatThemeItem chatThemeItem = new ChatThemeBottomSheet.ChatThemeItem(createPreviewFullTheme);
                if (createPreviewFullTheme.items.size() >= 4) {
                    arrayList2.add(chatThemeItem);
                }
            }
            ChatThemeController.chatThemeQueue.postRunnable(new AnonymousClass3(arrayList2, i));
            return;
        }
        this.defaultEmojiThemes.clear();
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.emojiPreviewThemesChanged, new Object[0]);
    }

    /* renamed from: org.telegram.messenger.MediaDataController$3 */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements Runnable {
        final /* synthetic */ int val$currentAccount;
        final /* synthetic */ ArrayList val$previewItems;

        AnonymousClass3(ArrayList arrayList, int i) {
            MediaDataController.this = r1;
            this.val$previewItems = arrayList;
            this.val$currentAccount = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            for (int i = 0; i < this.val$previewItems.size(); i++) {
                ((ChatThemeBottomSheet.ChatThemeItem) this.val$previewItems.get(i)).chatTheme.loadPreviewColors(this.val$currentAccount);
            }
            AndroidUtilities.runOnUIThread(new MediaDataController$3$$ExternalSyntheticLambda0(this, this.val$previewItems));
        }

        public /* synthetic */ void lambda$run$0(ArrayList arrayList) {
            MediaDataController.this.defaultEmojiThemes.clear();
            MediaDataController.this.defaultEmojiThemes.addAll(arrayList);
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.emojiPreviewThemesChanged, new Object[0]);
        }
    }
}
